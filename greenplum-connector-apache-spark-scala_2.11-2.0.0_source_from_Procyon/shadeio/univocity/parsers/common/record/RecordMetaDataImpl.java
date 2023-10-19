// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.record;

import shadeio.univocity.parsers.annotations.Format;
import shadeio.univocity.parsers.common.ArgumentUtils;
import shadeio.univocity.parsers.annotations.BooleanString;
import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.Calendar;
import java.util.Date;
import shadeio.univocity.parsers.annotations.Parsed;
import java.lang.reflect.AnnotatedElement;
import shadeio.univocity.parsers.annotations.helpers.AnnotationHelper;
import shadeio.univocity.parsers.common.fields.FieldSet;
import java.util.Arrays;
import java.util.HashMap;
import shadeio.univocity.parsers.common.fields.FieldConversionMapping;
import java.lang.annotation.Annotation;
import shadeio.univocity.parsers.conversions.Conversion;
import java.util.Map;
import shadeio.univocity.parsers.common.Context;

class RecordMetaDataImpl<C extends Context> implements RecordMetaData
{
    final C context;
    private Map<Class, Conversion> conversionByType;
    private Map<Class, Map<Annotation, Conversion>> conversionsByAnnotation;
    private Map<Integer, Annotation> annotationHashes;
    private MetaData[] indexMap;
    private FieldConversionMapping conversions;
    
    RecordMetaDataImpl(final C context) {
        this.conversionByType = new HashMap<Class, Conversion>();
        this.conversionsByAnnotation = new HashMap<Class, Map<Annotation, Conversion>>();
        this.annotationHashes = new HashMap<Integer, Annotation>();
        this.conversions = null;
        this.context = context;
    }
    
    private MetaData getMetaData(final String name) {
        final int index = this.context.indexOf(name);
        if (index == -1) {
            this.getValidatedHeaders();
            throw new IllegalArgumentException("Header name '" + name + "' not found. Available columns are: " + Arrays.asList(this.headers()));
        }
        return this.getMetaData(index);
    }
    
    private String[] getValidatedHeaders() {
        final String[] headers = this.context.headers();
        if (headers == null || headers.length == 0) {
            throw new IllegalStateException("No headers parsed from input nor provided in the user settings. Only index-based operations are available.");
        }
        return headers;
    }
    
    private MetaData getMetaData(final Enum<?> column) {
        final String[] headers = this.context.headers();
        if (headers == null || headers.length == 0) {
            throw new IllegalStateException("No headers parsed from input nor provided in the user settings. Only index-based operations are available.");
        }
        return this.getMetaData(this.context.indexOf(column));
    }
    
    public MetaData getMetaData(final int index) {
        if (this.indexMap == null || this.indexMap.length < index + 1 || this.indexMap[index] == null) {
            synchronized (this) {
                if (this.indexMap == null || this.indexMap.length < index + 1 || this.indexMap[index] == null) {
                    int startFrom = 0;
                    int lastIndex = index;
                    if (this.indexMap != null) {
                        startFrom = this.indexMap.length;
                        this.indexMap = Arrays.copyOf(this.indexMap, index + 1);
                    }
                    else {
                        final String[] headers = this.context.headers();
                        if (headers != null && lastIndex < headers.length) {
                            lastIndex = headers.length;
                        }
                        final int[] indexes = this.context.extractedFieldIndexes();
                        if (indexes != null) {
                            for (int i = 0; i < indexes.length; ++i) {
                                if (lastIndex < indexes[i]) {
                                    lastIndex = indexes[i];
                                }
                            }
                        }
                        this.indexMap = new MetaData[lastIndex + 1];
                    }
                    for (int j = startFrom; j < lastIndex + 1; ++j) {
                        this.indexMap[j] = new MetaData(j);
                    }
                }
            }
        }
        return this.indexMap[index];
    }
    
    @Override
    public int indexOf(final Enum<?> column) {
        return this.getMetaData(column).index;
    }
    
    MetaData metadataOf(final String headerName) {
        return this.getMetaData(headerName);
    }
    
    MetaData metadataOf(final Enum<?> column) {
        return this.getMetaData(column);
    }
    
    MetaData metadataOf(final int columnIndex) {
        return this.getMetaData(columnIndex);
    }
    
    @Override
    public int indexOf(final String headerName) {
        return this.getMetaData(headerName).index;
    }
    
    @Override
    public Class<?> typeOf(final Enum<?> column) {
        return this.getMetaData(column).type;
    }
    
    @Override
    public Class<?> typeOf(final String headerName) {
        return this.getMetaData(headerName).type;
    }
    
    @Override
    public Class<?> typeOf(final int columnIndex) {
        return this.getMetaData(columnIndex).type;
    }
    
    @Override
    public <T> void setDefaultValueOfColumns(final T defaultValue, final Enum<?>... columns) {
        for (final Enum<?> column : columns) {
            this.getMetaData(column).defaultValue = defaultValue;
        }
    }
    
    @Override
    public <T> void setDefaultValueOfColumns(final T defaultValue, final String... headerNames) {
        for (final String headerName : headerNames) {
            this.getMetaData(headerName).defaultValue = defaultValue;
        }
    }
    
    @Override
    public <T> void setDefaultValueOfColumns(final T defaultValue, final int... columnIndexes) {
        for (final int columnIndex : columnIndexes) {
            this.getMetaData(columnIndex).defaultValue = defaultValue;
        }
    }
    
    @Override
    public Object defaultValueOf(final Enum<?> column) {
        return this.getMetaData(column).defaultValue;
    }
    
    @Override
    public Object defaultValueOf(final String headerName) {
        return this.getMetaData(headerName).defaultValue;
    }
    
    @Override
    public Object defaultValueOf(final int columnIndex) {
        return this.getMetaData(columnIndex).defaultValue;
    }
    
    private FieldConversionMapping getConversions() {
        if (this.conversions == null) {
            this.conversions = new FieldConversionMapping();
        }
        return this.conversions;
    }
    
    @Override
    public <T extends Enum<T>> FieldSet<T> convertFields(final Class<T> enumType, final Conversion... conversions) {
        return (FieldSet<T>)this.getConversions().applyConversionsOnFieldEnums((Conversion<String, ?>[])conversions);
    }
    
    @Override
    public FieldSet<String> convertFields(final Conversion... conversions) {
        return this.getConversions().applyConversionsOnFieldNames((Conversion<String, ?>[])conversions);
    }
    
    @Override
    public FieldSet<Integer> convertIndexes(final Conversion... conversions) {
        return this.getConversions().applyConversionsOnFieldIndexes((Conversion<String, ?>[])conversions);
    }
    
    @Override
    public String[] headers() {
        return this.context.headers();
    }
    
    @Override
    public String[] selectedHeaders() {
        return this.context.selectedHeaders();
    }
    
    String getValue(final String[] data, final String headerName) {
        final MetaData md = this.metadataOf(headerName);
        if (md.index >= data.length) {
            return null;
        }
        return data[md.index];
    }
    
    String getValue(final String[] data, final int columnIndex) {
        final MetaData md = this.metadataOf(columnIndex);
        return data[md.index];
    }
    
    String getValue(final String[] data, final Enum<?> column) {
        final MetaData md = this.metadataOf(column);
        return data[md.index];
    }
    
    private <T> T convert(final MetaData md, final String[] data, final Class<T> expectedType, final Conversion[] conversions) {
        return expectedType.cast(convert(md, data, conversions));
    }
    
    private Object convert(final MetaData md, final String[] data, final Object defaultValue, final Conversion[] conversions) {
        final Object out = convert(md, data, conversions);
        return (out == null) ? defaultValue : out;
    }
    
    private static Object convert(final MetaData md, final String[] data, final Conversion[] conversions) {
        Object out = data[md.index];
        for (int i = 0; i < conversions.length; ++i) {
            out = conversions[i].execute(out);
        }
        return out;
    }
    
     <T> T getValue(final String[] data, final String headerName, final T defaultValue, final Conversion[] conversions) {
        return (T)this.convert(this.metadataOf(headerName), data, defaultValue, conversions);
    }
    
     <T> T getValue(final String[] data, final int columnIndex, final T defaultValue, final Conversion[] conversions) {
        return (T)this.convert(this.metadataOf(columnIndex), data, defaultValue, conversions);
    }
    
     <T> T getValue(final String[] data, final Enum<?> column, final T defaultValue, final Conversion[] conversions) {
        return (T)this.convert(this.metadataOf(column), data, defaultValue, conversions);
    }
    
     <T> T getValue(final String[] data, final String headerName, final Class<T> expectedType, final Conversion[] conversions) {
        return this.convert(this.metadataOf(headerName), data, expectedType, conversions);
    }
    
     <T> T getValue(final String[] data, final int columnIndex, final Class<T> expectedType, final Conversion[] conversions) {
        return this.convert(this.metadataOf(columnIndex), data, expectedType, conversions);
    }
    
     <T> T getValue(final String[] data, final Enum<?> column, final Class<T> expectedType, final Conversion[] conversions) {
        return this.convert(this.metadataOf(column), data, expectedType, conversions);
    }
    
    private <T> T convert(final MetaData md, final String[] data, final Class<T> type, final T defaultValue, final Annotation annotation) {
        Object out = (md.index < data.length) ? data[md.index] : null;
        if (out == null) {
            out = ((defaultValue == null) ? md.defaultValue : defaultValue);
        }
        if (annotation == null) {
            this.initializeMetadataConversions(data, md);
            out = md.convert(out);
            if (out == null) {
                out = ((defaultValue == null) ? md.defaultValue : defaultValue);
            }
        }
        if (type != null) {
            if (out != null && type.isAssignableFrom(out.getClass())) {
                return (T)out;
            }
            if (type != String.class) {
                Conversion conversion;
                if (annotation == null) {
                    conversion = this.conversionByType.get(type);
                    if (conversion == null) {
                        conversion = AnnotationHelper.getDefaultConversion(type, null, null);
                        this.conversionByType.put(type, conversion);
                    }
                }
                else {
                    Map<Annotation, Conversion> m = this.conversionsByAnnotation.get(type);
                    if (m == null) {
                        m = new HashMap<Annotation, Conversion>();
                        this.conversionsByAnnotation.put(type, m);
                    }
                    conversion = m.get(annotation);
                    if (conversion == null) {
                        conversion = AnnotationHelper.getConversion(type, annotation);
                        m.put(annotation, conversion);
                    }
                }
                if (conversion == null) {
                    String message = "";
                    if (type == Date.class || type == Calendar.class) {
                        message = ". Need to specify format for date";
                    }
                    final DataProcessingException exception = new DataProcessingException("Cannot convert '{value}' to " + type.getName() + message);
                    exception.setValue(out);
                    exception.setErrorContentLength(this.context.errorContentLength());
                    throw exception;
                }
                out = conversion.execute(out);
            }
        }
        if (type == null) {
            return (T)out;
        }
        try {
            return type.cast(out);
        }
        catch (ClassCastException e) {
            final DataProcessingException exception2 = new DataProcessingException("Cannot cast value '{value}' of type " + out.getClass().toString() + " to " + type.getName());
            exception2.setValue(out);
            exception2.setErrorContentLength(this.context.errorContentLength());
            throw exception2;
        }
    }
    
    private void initializeMetadataConversions(final String[] data, final MetaData md) {
        if (this.conversions != null) {
            synchronized (this) {
                String[] headers = this.headers();
                if (headers == null) {
                    headers = data;
                }
                this.conversions.prepareExecution(false, headers);
                md.setDefaultConversions(this.conversions.getConversions(md.index, md.type));
            }
        }
    }
    
     <T> T getObjectValue(final String[] data, final String headerName, final Class<T> type, final T defaultValue) {
        return this.convert(this.metadataOf(headerName), data, type, defaultValue, null);
    }
    
     <T> T getObjectValue(final String[] data, final int columnIndex, final Class<T> type, final T defaultValue) {
        return this.convert(this.metadataOf(columnIndex), data, type, defaultValue, null);
    }
    
     <T> T getObjectValue(final String[] data, final Enum<?> column, final Class<T> type, final T defaultValue) {
        return this.convert(this.metadataOf(column), data, type, defaultValue, null);
    }
    
     <T> T getObjectValue(final String[] data, final String headerName, final Class<T> type, final T defaultValue, final String format, final String... formatOptions) {
        if (format == null) {
            return this.getObjectValue(data, headerName, type, defaultValue);
        }
        return this.convert(this.metadataOf(headerName), data, type, defaultValue, this.buildAnnotation(type, format, formatOptions));
    }
    
     <T> T getObjectValue(final String[] data, final int columnIndex, final Class<T> type, final T defaultValue, final String format, final String... formatOptions) {
        if (format == null) {
            return this.getObjectValue(data, columnIndex, type, defaultValue);
        }
        return this.convert(this.metadataOf(columnIndex), data, type, defaultValue, this.buildAnnotation(type, format, formatOptions));
    }
    
     <T> T getObjectValue(final String[] data, final Enum<?> column, final Class<T> type, final T defaultValue, final String format, final String... formatOptions) {
        if (format == null) {
            return this.getObjectValue(data, column, type, defaultValue);
        }
        return this.convert(this.metadataOf(column), data, type, defaultValue, this.buildAnnotation(type, format, formatOptions));
    }
    
    static Annotation buildBooleanStringAnnotation(final String[] trueStrings, final String[] falseStrings) {
        return new BooleanString() {
            @Override
            public String[] trueStrings() {
                return (trueStrings == null) ? ArgumentUtils.EMPTY_STRING_ARRAY : trueStrings;
            }
            
            @Override
            public String[] falseStrings() {
                return (falseStrings == null) ? ArgumentUtils.EMPTY_STRING_ARRAY : falseStrings;
            }
            
            @Override
            public Class<? extends Annotation> annotationType() {
                return BooleanString.class;
            }
        };
    }
    
    private static Annotation newFormatAnnotation(final String format, final String... formatOptions) {
        return new Format() {
            @Override
            public String[] formats() {
                return new String[] { format };
            }
            
            @Override
            public String[] options() {
                return formatOptions;
            }
            
            @Override
            public Class<? extends Annotation> annotationType() {
                return Format.class;
            }
        };
    }
    
     <T> Annotation buildAnnotation(final Class<T> type, final String args1, final String... args2) {
        final Integer hash = type.hashCode() * 31 + String.valueOf(args1).hashCode() + 31 * Arrays.toString(args2).hashCode();
        Annotation out = this.annotationHashes.get(hash);
        if (out == null) {
            if (type == Boolean.class || type == Boolean.TYPE) {
                out = buildBooleanStringAnnotation((String[])((args1 == null) ? null : new String[] { args1 }), args2);
            }
            else {
                out = newFormatAnnotation(args1, args2);
            }
            this.annotationHashes.put(hash, out);
        }
        return out;
    }
    
    @Override
    public void setTypeOfColumns(final Class<?> type, final Enum... columns) {
        for (int i = 0; i < columns.length; ++i) {
            this.getMetaData(columns[i]).type = type;
        }
    }
    
    @Override
    public void setTypeOfColumns(final Class<?> type, final String... headerNames) {
        for (int i = 0; i < headerNames.length; ++i) {
            this.getMetaData(headerNames[i]).type = type;
        }
    }
    
    @Override
    public void setTypeOfColumns(final Class<?> type, final int... columnIndexes) {
        for (int i = 0; i < columnIndexes.length; ++i) {
            this.getMetaData(columnIndexes[i]).type = type;
        }
    }
    
    @Override
    public boolean containsColumn(final String headerName) {
        return headerName != null && this.context.indexOf(headerName) != -1;
    }
}
