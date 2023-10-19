// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.util.HashMap;
import java.util.Arrays;
import shadeio.univocity.parsers.common.fields.FieldSet;
import shadeio.univocity.parsers.common.fields.FieldConversionMapping;
import shadeio.univocity.parsers.conversions.Conversion;
import java.util.Map;

public abstract class DefaultConversionProcessor implements ConversionProcessor
{
    private Map<Class<?>, Conversion[]> conversionsByType;
    protected FieldConversionMapping conversions;
    private boolean conversionsInitialized;
    private int[] fieldIndexes;
    private boolean fieldsReordered;
    ProcessorErrorHandler errorHandler;
    Context context;
    
    public DefaultConversionProcessor() {
        this.errorHandler = NoopProcessorErrorHandler.instance;
    }
    
    @Override
    public final FieldSet<Integer> convertIndexes(final Conversion... conversions) {
        return this.getConversions().applyConversionsOnFieldIndexes((Conversion<String, ?>[])conversions);
    }
    
    @Override
    public final void convertAll(final Conversion... conversions) {
        this.getConversions().applyConversionsOnAllFields((Conversion<String, ?>[])conversions);
    }
    
    @Override
    public final FieldSet<String> convertFields(final Conversion... conversions) {
        return this.getConversions().applyConversionsOnFieldNames((Conversion<String, ?>[])conversions);
    }
    
    private FieldConversionMapping getConversions() {
        if (this.conversions == null) {
            this.conversions = new FieldConversionMapping();
        }
        return this.conversions;
    }
    
    protected void initializeConversions(final String[] row, final Context context) {
        this.conversionsInitialized = true;
        this.fieldIndexes = null;
        this.fieldsReordered = false;
        this.conversionsInitialized = false;
        final String[] contextHeaders = (String[])((context == null) ? null : context.headers());
        if (contextHeaders != null && contextHeaders.length > 0) {
            this.getConversions().prepareExecution(false, contextHeaders);
        }
        else {
            this.getConversions().prepareExecution(false, row);
        }
        if (context != null) {
            this.fieldIndexes = context.extractedFieldIndexes();
            this.fieldsReordered = context.columnsReordered();
        }
    }
    
    public final Object[] applyConversions(final String[] row, final Context context) {
        boolean keepRow = true;
        final Object[] objectRow = new Object[row.length];
        final boolean[] convertedFlags = (boolean[])((this.conversionsByType != null) ? new boolean[row.length] : null);
        System.arraycopy(row, 0, objectRow, 0, row.length);
        if (this.conversions != null) {
            if (!this.conversionsInitialized) {
                this.initializeConversions(row, context);
            }
            for (int length = (!this.fieldsReordered && this.fieldIndexes == null) ? objectRow.length : this.fieldIndexes.length, i = 0; i < length; ++i) {
                try {
                    if (!this.fieldsReordered) {
                        if (this.fieldIndexes == null) {
                            objectRow[i] = this.conversions.applyConversions(i, row[i], convertedFlags);
                        }
                        else {
                            final int index = this.fieldIndexes[i];
                            objectRow[index] = this.conversions.applyConversions(index, row[index], convertedFlags);
                        }
                    }
                    else {
                        objectRow[i] = this.conversions.applyConversions(this.fieldIndexes[i], row[i], convertedFlags);
                    }
                }
                catch (Throwable ex) {
                    keepRow = this.handleConversionError(ex, objectRow, i);
                }
            }
        }
        if (keepRow && convertedFlags != null) {
            keepRow = this.applyConversionsByType(false, objectRow, convertedFlags);
        }
        if (keepRow && this.validateAllValues(objectRow)) {
            return objectRow;
        }
        return null;
    }
    
    private boolean validateAllValues(final Object[] objectRow) {
        if (this.conversions != null && this.conversions.validatedIndexes != null) {
            boolean keepRow = true;
            for (int i = 0; keepRow && i < this.conversions.validatedIndexes.length; ++i) {
                final int index = this.conversions.validatedIndexes[i];
                try {
                    final Object value = (index < objectRow.length) ? objectRow[index] : null;
                    this.conversions.executeValidations(index, value);
                }
                catch (Throwable ex) {
                    keepRow = this.handleConversionError(ex, objectRow, index);
                }
            }
            return keepRow;
        }
        return true;
    }
    
    public final boolean reverseConversions(final boolean executeInReverseOrder, final Object[] row, final String[] headers, final int[] indexesToWrite) {
        boolean keepRow = true;
        final boolean[] convertedFlags = (boolean[])((this.conversionsByType != null) ? new boolean[row.length] : null);
        if (this.conversions != null) {
            if (!this.conversionsInitialized) {
                this.conversionsInitialized = true;
                this.conversions.prepareExecution(true, (headers != null) ? headers : new String[row.length]);
                this.fieldIndexes = indexesToWrite;
            }
            for (int last = (this.fieldIndexes == null) ? row.length : this.fieldIndexes.length, i = 0; i < last; ++i) {
                try {
                    if (this.fieldIndexes == null || this.fieldIndexes[i] == -1) {
                        row[i] = this.conversions.reverseConversions(executeInReverseOrder, i, row[i], convertedFlags);
                    }
                    else {
                        final int index = this.fieldIndexes[i];
                        row[index] = this.conversions.reverseConversions(executeInReverseOrder, index, row[index], convertedFlags);
                    }
                }
                catch (Throwable ex) {
                    keepRow = this.handleConversionError(ex, row, i);
                }
            }
        }
        if (keepRow && convertedFlags != null) {
            keepRow = this.applyConversionsByType(true, row, convertedFlags);
        }
        return keepRow && this.validateAllValues(row);
    }
    
    private boolean applyConversionsByType(final boolean reverse, final Object[] row, final boolean[] convertedFlags) {
        boolean keepRow = true;
        for (int i = 0; i < row.length; ++i) {
            if (!convertedFlags[i]) {
                try {
                    row[i] = this.applyTypeConversion(reverse, row[i]);
                }
                catch (Throwable ex) {
                    keepRow = this.handleConversionError(ex, row, i);
                }
            }
        }
        return keepRow;
    }
    
    protected final boolean handleConversionError(final Throwable ex, Object[] row, final int column) {
        if (row != null && row.length < column) {
            row = Arrays.copyOf(row, column + 1);
        }
        final DataProcessingException error = this.toDataProcessingException(ex, row, column);
        if (column > -1 && this.errorHandler instanceof RetryableErrorHandler) {
            ((RetryableErrorHandler)this.errorHandler).prepareToRun();
        }
        error.markAsHandled(this.errorHandler);
        this.errorHandler.handleError(error, row, this.context);
        if (column > -1 && this.errorHandler instanceof RetryableErrorHandler) {
            final RetryableErrorHandler retry = (RetryableErrorHandler)this.errorHandler;
            final Object defaultValue = retry.getDefaultValue();
            row[column] = defaultValue;
            return !retry.isRecordSkipped();
        }
        return false;
    }
    
    protected DataProcessingException toDataProcessingException(final Throwable ex, final Object[] row, final int column) {
        DataProcessingException error;
        if (ex instanceof DataProcessingException) {
            error = (DataProcessingException)ex;
            error.setRow(row);
            error.setColumnIndex(column);
        }
        else {
            error = new DataProcessingException("Error processing data conversions", column, row, ex);
        }
        error.markAsNonFatal();
        error.setContext(this.context);
        return error;
    }
    
    @Override
    public final void convertType(final Class<?> type, final Conversion... conversions) {
        ArgumentUtils.noNulls("Type to convert", type);
        ArgumentUtils.noNulls("Sequence of conversions to apply over data of type " + type.getSimpleName(), (Conversion[])conversions);
        if (this.conversionsByType == null) {
            this.conversionsByType = new HashMap<Class<?>, Conversion[]>();
        }
        this.conversionsByType.put(type, conversions);
    }
    
    private Object applyTypeConversion(final boolean revert, Object input) {
        if (this.conversionsByType == null || input == null) {
            return input;
        }
        final Conversion[] conversionSequence = this.conversionsByType.get(input.getClass());
        if (conversionSequence == null) {
            return input;
        }
        if (revert) {
            for (int i = 0; i < conversionSequence.length; ++i) {
                input = conversionSequence[i].revert(input);
            }
        }
        else {
            for (int i = 0; i < conversionSequence.length; ++i) {
                input = conversionSequence[i].execute(input);
            }
        }
        return input;
    }
}
