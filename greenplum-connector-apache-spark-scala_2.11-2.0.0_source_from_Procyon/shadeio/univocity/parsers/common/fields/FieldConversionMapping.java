// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.fields;

import shadeio.univocity.parsers.annotations.Parsed;
import java.lang.reflect.AnnotatedElement;
import shadeio.univocity.parsers.annotations.helpers.AnnotationHelper;
import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.Iterator;
import java.util.Collection;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;
import shadeio.univocity.parsers.conversions.ValidatedConversion;
import java.util.Map;
import java.util.List;
import shadeio.univocity.parsers.conversions.Conversion;

public class FieldConversionMapping
{
    private static final Conversion[] EMPTY_CONVERSION_ARRAY;
    public int[] validatedIndexes;
    private final List<FieldSelector> conversionSequence;
    private final AbstractConversionMapping<String> fieldNameConversionMapping;
    private final AbstractConversionMapping<Integer> fieldIndexConversionMapping;
    private final AbstractConversionMapping<Enum> fieldEnumConversionMapping;
    private final AbstractConversionMapping<Integer> convertAllMapping;
    private Map<Integer, List<Conversion<?, ?>>> conversionsByIndex;
    private Map<Integer, List<ValidatedConversion>> validationsByIndex;
    
    public FieldConversionMapping() {
        this.conversionSequence = new ArrayList<FieldSelector>();
        this.fieldNameConversionMapping = new AbstractConversionMapping<String>((List)this.conversionSequence) {
            @Override
            protected FieldSelector newFieldSelector() {
                return new FieldNameSelector();
            }
        };
        this.fieldIndexConversionMapping = new AbstractConversionMapping<Integer>((List)this.conversionSequence) {
            @Override
            protected FieldSelector newFieldSelector() {
                return new FieldIndexSelector();
            }
        };
        this.fieldEnumConversionMapping = new AbstractConversionMapping<Enum>((List)this.conversionSequence) {
            @Override
            protected FieldSelector newFieldSelector() {
                return new FieldEnumSelector();
            }
        };
        this.convertAllMapping = new AbstractConversionMapping<Integer>((List)this.conversionSequence) {
            @Override
            protected FieldSelector newFieldSelector() {
                return new AllIndexesSelector();
            }
        };
        this.conversionsByIndex = Collections.emptyMap();
        this.validationsByIndex = Collections.emptyMap();
    }
    
    public void prepareExecution(final boolean writing, final String[] values) {
        if (this.fieldNameConversionMapping.isEmpty() && this.fieldEnumConversionMapping.isEmpty() && this.fieldIndexConversionMapping.isEmpty() && this.convertAllMapping.isEmpty()) {
            return;
        }
        if (!this.conversionsByIndex.isEmpty()) {
            return;
        }
        this.conversionsByIndex = new HashMap<Integer, List<Conversion<?, ?>>>();
        for (final FieldSelector next : this.conversionSequence) {
            this.fieldNameConversionMapping.prepareExecution(writing, next, this.conversionsByIndex, values);
            this.fieldIndexConversionMapping.prepareExecution(writing, next, this.conversionsByIndex, values);
            this.fieldEnumConversionMapping.prepareExecution(writing, next, this.conversionsByIndex, values);
            this.convertAllMapping.prepareExecution(writing, next, this.conversionsByIndex, values);
        }
        final Iterator<Map.Entry<Integer, List<Conversion<?, ?>>>> entryIterator = this.conversionsByIndex.entrySet().iterator();
        while (entryIterator.hasNext()) {
            final Map.Entry<Integer, List<Conversion<?, ?>>> e = entryIterator.next();
            final Iterator<Conversion<?, ?>> it = e.getValue().iterator();
            while (it.hasNext()) {
                final Conversion conversion = it.next();
                if (conversion instanceof ValidatedConversion) {
                    if (this.validationsByIndex.isEmpty()) {
                        this.validationsByIndex = new TreeMap<Integer, List<ValidatedConversion>>();
                    }
                    it.remove();
                    List<ValidatedConversion> validations = this.validationsByIndex.get(e.getKey());
                    if (validations == null) {
                        validations = new ArrayList<ValidatedConversion>(1);
                        this.validationsByIndex.put(e.getKey(), validations);
                    }
                    validations.add((ValidatedConversion)conversion);
                }
            }
            if (e.getValue().isEmpty()) {
                entryIterator.remove();
            }
        }
        this.validatedIndexes = ArgumentUtils.toIntArray(this.validationsByIndex.keySet());
    }
    
    public void applyConversionsOnAllFields(final Conversion<String, ?>... conversions) {
        this.convertAllMapping.registerConversions(conversions);
    }
    
    public FieldSet<Integer> applyConversionsOnFieldIndexes(final Conversion<String, ?>... conversions) {
        return this.fieldIndexConversionMapping.registerConversions(conversions);
    }
    
    public FieldSet<String> applyConversionsOnFieldNames(final Conversion<String, ?>... conversions) {
        return this.fieldNameConversionMapping.registerConversions(conversions);
    }
    
    public FieldSet<Enum> applyConversionsOnFieldEnums(final Conversion<String, ?>... conversions) {
        return (FieldSet<Enum>)this.fieldEnumConversionMapping.registerConversions(conversions);
    }
    
    public void executeValidations(final int index, final Object value) {
        final List<ValidatedConversion> validations = this.validationsByIndex.get(index);
        if (validations != null) {
            for (int i = 0; i < validations.size(); ++i) {
                validations.get(i).execute(value);
            }
        }
    }
    
    public Object reverseConversions(final boolean executeInReverseOrder, final int index, Object value, final boolean[] convertedFlags) {
        final List<Conversion<?, ?>> conversions = this.conversionsByIndex.get(index);
        if (conversions != null) {
            if (convertedFlags != null) {
                convertedFlags[index] = true;
            }
            Conversion conversion = null;
            try {
                if (executeInReverseOrder) {
                    for (int i = conversions.size() - 1; i >= 0; --i) {
                        conversion = conversions.get(i);
                        value = conversion.revert(value);
                    }
                }
                else {
                    final Iterator i$ = conversions.iterator();
                    while (i$.hasNext()) {
                        final Conversion<?, ?> c = (Conversion<?, ?>)(conversion = i$.next());
                        value = conversion.revert(value);
                    }
                }
            }
            catch (DataProcessingException ex) {
                ex.setValue(value);
                ex.setColumnIndex(index);
                ex.markAsNonFatal();
                throw ex;
            }
            catch (Throwable ex2) {
                DataProcessingException exception;
                if (conversion != null) {
                    exception = new DataProcessingException("Error converting value '{value}' using conversion " + conversion.getClass().getName(), ex2);
                }
                else {
                    exception = new DataProcessingException("Error converting value '{value}'", ex2);
                }
                exception.setValue(value);
                exception.setColumnIndex(index);
                exception.markAsNonFatal();
                throw exception;
            }
        }
        return value;
    }
    
    public Object applyConversions(final int index, final String stringValue, final boolean[] convertedFlags) {
        final List<Conversion<?, ?>> conversions = this.conversionsByIndex.get(index);
        if (conversions != null) {
            if (convertedFlags != null) {
                convertedFlags[index] = true;
            }
            Object result = stringValue;
            for (final Conversion conversion : conversions) {
                try {
                    result = conversion.execute(result);
                }
                catch (DataProcessingException ex) {
                    ex.setColumnIndex(index);
                    ex.markAsNonFatal();
                    throw ex;
                }
                catch (Throwable ex2) {
                    final DataProcessingException exception = new DataProcessingException("Error converting value '{value}' using conversion " + conversion.getClass().getName(), ex2);
                    exception.setValue(result);
                    exception.setColumnIndex(index);
                    exception.markAsNonFatal();
                    throw exception;
                }
            }
            return result;
        }
        return stringValue;
    }
    
    public Conversion[] getConversions(final int index, final Class<?> expectedType) {
        final List<Conversion<?, ?>> conversions = this.conversionsByIndex.get(index);
        Conversion[] out;
        if (conversions != null) {
            out = new Conversion[conversions.size()];
            int i = 0;
            for (final Conversion conversion : conversions) {
                out[i++] = conversion;
            }
        }
        else {
            if (expectedType == String.class) {
                return FieldConversionMapping.EMPTY_CONVERSION_ARRAY;
            }
            out = new Conversion[] { AnnotationHelper.getDefaultConversion(expectedType, null, null) };
            if (out[0] == null) {
                return FieldConversionMapping.EMPTY_CONVERSION_ARRAY;
            }
        }
        return out;
    }
    
    static {
        EMPTY_CONVERSION_ARRAY = new Conversion[0];
    }
}
