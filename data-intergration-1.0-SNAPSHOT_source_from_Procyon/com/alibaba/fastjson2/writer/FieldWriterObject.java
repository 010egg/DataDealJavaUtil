// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.List;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONException;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.text.DecimalFormat;
import java.util.Locale;
import com.alibaba.fastjson2.util.BeanUtils;
import java.util.Map;
import com.alibaba.fastjson2.JSONWriter;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.Collection;
import java.util.Currency;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class FieldWriterObject<T> extends FieldWriter<T>
{
    volatile Class initValueClass;
    final boolean unwrapped;
    final boolean array;
    final boolean number;
    static final AtomicReferenceFieldUpdater<FieldWriterObject, Class> initValueClassUpdater;
    
    protected FieldWriterObject(final String name, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method) {
        super(name, ordinal, features, format, label, fieldType, fieldClass, field, method);
        this.unwrapped = ((features & 0x2000000000000L) != 0x0L);
        if (fieldClass == Currency.class) {
            this.initValueClass = fieldClass;
            this.initObjectWriter = ObjectWriterImplCurrency.INSTANCE_FOR_FIELD;
        }
        this.array = (fieldClass.isArray() || Collection.class.isAssignableFrom(fieldClass) || fieldClass == AtomicLongArray.class || fieldClass == AtomicIntegerArray.class);
        this.number = Number.class.isAssignableFrom(fieldClass);
    }
    
    @Override
    public ObjectWriter getInitWriter() {
        return this.initObjectWriter;
    }
    
    @Override
    public boolean unwrapped() {
        return this.unwrapped;
    }
    
    @Override
    public ObjectWriter getObjectWriter(final JSONWriter jsonWriter, final Class valueClass) {
        final Class initValueClass = this.initValueClass;
        if (initValueClass == null || this.initObjectWriter == ObjectWriterBaseModule.VoidObjectWriter.INSTANCE) {
            return this.getObjectWriterVoid(jsonWriter, valueClass);
        }
        boolean typeMatch = initValueClass == valueClass || (initValueClass == Map.class && initValueClass.isAssignableFrom(valueClass));
        if (!typeMatch && initValueClass.isPrimitive()) {
            typeMatch = typeMatch(initValueClass, valueClass);
        }
        if (typeMatch) {
            ObjectWriter objectWriter;
            if (this.initObjectWriter == null) {
                objectWriter = this.getObjectWriterTypeMatch(jsonWriter, valueClass);
            }
            else {
                objectWriter = this.initObjectWriter;
            }
            return objectWriter;
        }
        return this.getObjectWriterTypeNotMatch(jsonWriter, valueClass);
    }
    
    private ObjectWriter getObjectWriterVoid(final JSONWriter jsonWriter, final Class valueClass) {
        ObjectWriter formattedWriter = null;
        if (BeanUtils.isExtendedMap(valueClass) && "$super$".equals(this.fieldName)) {
            final JSONWriter.Context context = jsonWriter.context;
            final boolean fieldBased = ((this.features | context.getFeatures()) & JSONWriter.Feature.FieldBased.mask) != 0x0L;
            formattedWriter = context.provider.getObjectWriter(this.fieldType, this.fieldClass, fieldBased);
            if (this.initObjectWriter == null) {
                final boolean success = FieldWriterObject.initValueClassUpdater.compareAndSet(this, null, valueClass);
                if (success) {
                    FieldWriterObject.initObjectWriterUpdater.compareAndSet(this, null, formattedWriter);
                }
            }
            return formattedWriter;
        }
        if (this.format == null) {
            final JSONWriter.Context context = jsonWriter.context;
            final boolean fieldBased = ((this.features | context.getFeatures()) & JSONWriter.Feature.FieldBased.mask) != 0x0L;
            formattedWriter = context.provider.getObjectWriterFromCache(valueClass, valueClass, fieldBased);
        }
        final DecimalFormat decimalFormat = this.decimalFormat;
        if (valueClass == Float[].class) {
            if (decimalFormat != null) {
                formattedWriter = new ObjectWriterArrayFinal(Float.class, decimalFormat);
            }
            else {
                formattedWriter = ObjectWriterArrayFinal.FLOAT_ARRAY;
            }
        }
        else if (valueClass == Double[].class) {
            if (decimalFormat != null) {
                formattedWriter = new ObjectWriterArrayFinal(Double.class, decimalFormat);
            }
            else {
                formattedWriter = ObjectWriterArrayFinal.DOUBLE_ARRAY;
            }
        }
        else if (valueClass == float[].class) {
            if (decimalFormat != null) {
                formattedWriter = new ObjectWriterImplFloatValueArray(decimalFormat);
            }
            else {
                formattedWriter = ObjectWriterImplFloatValueArray.INSTANCE;
            }
        }
        else if (valueClass == double[].class) {
            if (decimalFormat != null) {
                formattedWriter = new ObjectWriterImplDoubleValueArray(decimalFormat);
            }
            else {
                formattedWriter = ObjectWriterImplDoubleValueArray.INSTANCE;
            }
        }
        if (formattedWriter == null) {
            formattedWriter = FieldWriter.getObjectWriter(this.fieldType, this.fieldClass, this.format, null, valueClass);
        }
        if (formattedWriter == null) {
            final boolean success2 = FieldWriterObject.initValueClassUpdater.compareAndSet(this, null, valueClass);
            formattedWriter = jsonWriter.getObjectWriter(valueClass);
            if (success2) {
                FieldWriterObject.initObjectWriterUpdater.compareAndSet(this, null, formattedWriter);
            }
        }
        else if (this.initObjectWriter == null) {
            final boolean success2 = FieldWriterObject.initValueClassUpdater.compareAndSet(this, null, valueClass);
            if (success2) {
                FieldWriterObject.initObjectWriterUpdater.compareAndSet(this, null, formattedWriter);
            }
        }
        return formattedWriter;
    }
    
    static boolean typeMatch(final Class initValueClass, final Class valueClass) {
        return (initValueClass == Integer.TYPE && valueClass == Integer.class) || (initValueClass == Long.TYPE && valueClass == Long.class) || (initValueClass == Boolean.TYPE && valueClass == Boolean.class) || (initValueClass == Short.TYPE && valueClass == Short.class) || (initValueClass == Byte.TYPE && valueClass == Byte.class) || (initValueClass == Float.TYPE && valueClass == Float.class) || (initValueClass == Double.TYPE && valueClass == Double.class) || (initValueClass == Character.TYPE && valueClass == Character.class);
    }
    
    private ObjectWriter getObjectWriterTypeNotMatch(final JSONWriter jsonWriter, final Class valueClass) {
        if (!Map.class.isAssignableFrom(valueClass)) {
            ObjectWriter objectWriter = null;
            if (this.format != null) {
                objectWriter = FieldWriter.getObjectWriter(this.fieldType, this.fieldClass, this.format, null, valueClass);
            }
            if (objectWriter == null) {
                objectWriter = jsonWriter.getObjectWriter(valueClass);
            }
            return objectWriter;
        }
        if (this.fieldClass.isAssignableFrom(valueClass)) {
            return ObjectWriterImplMap.of(this.fieldType, valueClass);
        }
        return ObjectWriterImplMap.of(valueClass);
    }
    
    private ObjectWriter getObjectWriterTypeMatch(final JSONWriter jsonWriter, final Class valueClass) {
        ObjectWriter objectWriter;
        if (Map.class.isAssignableFrom(valueClass)) {
            if (this.fieldClass.isAssignableFrom(valueClass)) {
                objectWriter = ObjectWriterImplMap.of(this.fieldType, valueClass);
            }
            else {
                objectWriter = ObjectWriterImplMap.of(valueClass);
            }
        }
        else {
            objectWriter = jsonWriter.getObjectWriter(valueClass);
        }
        FieldWriterObject.initObjectWriterUpdater.compareAndSet(this, null, objectWriter);
        return objectWriter;
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final long features = this.features | jsonWriter.getFeatures();
        if (!this.fieldClassSerializable && (features & JSONWriter.Feature.IgnoreNoneSerializable.mask) != 0x0L) {
            return false;
        }
        Object value;
        try {
            value = this.getFieldValue(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        if (value == null) {
            if ((features & JSONWriter.Feature.WriteNulls.mask) != 0x0L && (features & JSONWriter.Feature.NotWriteDefaultValue.mask) == 0x0L) {
                this.writeFieldName(jsonWriter);
                if (this.array) {
                    jsonWriter.writeArrayNull();
                }
                else if (this.number) {
                    jsonWriter.writeNumberNull();
                }
                else if (this.fieldClass == Appendable.class || this.fieldClass == StringBuffer.class || this.fieldClass == StringBuilder.class) {
                    jsonWriter.writeStringNull();
                }
                else {
                    jsonWriter.writeNull();
                }
                return true;
            }
            if ((features & (JSONWriter.Feature.WriteNullNumberAsZero.mask | JSONWriter.Feature.NullAsDefaultValue.mask)) != 0x0L && this.number) {
                this.writeFieldName(jsonWriter);
                jsonWriter.writeInt32(0);
                return true;
            }
            if ((features & (JSONWriter.Feature.WriteNullBooleanAsFalse.mask | JSONWriter.Feature.NullAsDefaultValue.mask)) != 0x0L && (this.fieldClass == Boolean.class || this.fieldClass == AtomicBoolean.class)) {
                this.writeFieldName(jsonWriter);
                jsonWriter.writeBool(false);
                return true;
            }
            return false;
        }
        else {
            if (value == object && this.fieldClass == Throwable.class && this.field != null && this.field.getDeclaringClass() == Throwable.class) {
                return false;
            }
            if ((features & JSONWriter.Feature.IgnoreNoneSerializable.mask) != 0x0L && !(value instanceof Serializable)) {
                return false;
            }
            final boolean refDetect = jsonWriter.isRefDetect(value);
            if (refDetect) {
                if (value == object) {
                    this.writeFieldName(jsonWriter);
                    jsonWriter.writeReference("..");
                    return true;
                }
                final String refPath = jsonWriter.setPath(this, value);
                if (refPath != null) {
                    this.writeFieldName(jsonWriter);
                    jsonWriter.writeReference(refPath);
                    jsonWriter.popPath(value);
                    return true;
                }
            }
            final Class<?> valueClass = value.getClass();
            if (valueClass == byte[].class) {
                this.writeBinary(jsonWriter, (byte[])value);
                return true;
            }
            final ObjectWriter valueWriter = this.getObjectWriter(jsonWriter, valueClass);
            if (valueWriter == null) {
                throw new JSONException("get objectWriter error : " + valueClass);
            }
            if (this.unwrapped) {
                if (value instanceof Map) {
                    for (final Map.Entry entry : ((Map)value).entrySet()) {
                        final String entryKey = entry.getKey().toString();
                        final Object entryValue = entry.getValue();
                        if (entryValue == null && (features & JSONWriter.Feature.WriteNulls.mask) == 0x0L) {
                            continue;
                        }
                        jsonWriter.writeName(entryKey);
                        jsonWriter.writeColon();
                        if (entryValue == null) {
                            jsonWriter.writeNull();
                        }
                        else {
                            final Class<?> entryValueClass = entryValue.getClass();
                            final ObjectWriter entryValueWriter = jsonWriter.getObjectWriter(entryValueClass);
                            entryValueWriter.write(jsonWriter, entryValue);
                        }
                    }
                    if (refDetect) {
                        jsonWriter.popPath(value);
                    }
                    return true;
                }
                if (valueWriter instanceof ObjectWriterAdapter) {
                    final ObjectWriterAdapter writerAdapter = (ObjectWriterAdapter)valueWriter;
                    final List<FieldWriter> fieldWriters = writerAdapter.fieldWriters;
                    for (final FieldWriter fieldWriter : fieldWriters) {
                        fieldWriter.write(jsonWriter, value);
                    }
                    return true;
                }
            }
            this.writeFieldName(jsonWriter);
            final boolean jsonb = jsonWriter.jsonb;
            if ((this.features & JSONWriter.Feature.BeanToArray.mask) != 0x0L) {
                if (jsonb) {
                    valueWriter.writeArrayMappingJSONB(jsonWriter, value, this.fieldName, this.fieldType, this.features);
                }
                else {
                    valueWriter.writeArrayMapping(jsonWriter, value, this.fieldName, this.fieldType, this.features);
                }
            }
            else if (jsonb) {
                valueWriter.writeJSONB(jsonWriter, value, this.fieldName, this.fieldType, this.features);
            }
            else {
                valueWriter.write(jsonWriter, value, this.fieldName, this.fieldType, this.features);
            }
            if (refDetect) {
                jsonWriter.popPath(value);
            }
            return true;
        }
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final Object value = this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNull();
            return;
        }
        final Class<?> valueClass = value.getClass();
        ObjectWriter valueWriter;
        if (this.initValueClass == null) {
            this.initValueClass = valueClass;
            valueWriter = jsonWriter.getObjectWriter(valueClass);
            FieldWriterObject.initObjectWriterUpdater.compareAndSet(this, null, valueWriter);
        }
        else if (this.initValueClass == valueClass) {
            valueWriter = this.initObjectWriter;
        }
        else {
            valueWriter = jsonWriter.getObjectWriter(valueClass);
        }
        if (valueWriter == null) {
            throw new JSONException("get value writer error, valueType : " + valueClass);
        }
        final boolean refDetect = jsonWriter.isRefDetect() && !ObjectWriterProvider.isNotReferenceDetect(valueClass);
        if (refDetect) {
            if (value == object) {
                jsonWriter.writeReference("..");
                return;
            }
            final String refPath = jsonWriter.setPath(this.fieldName, value);
            if (refPath != null) {
                jsonWriter.writeReference(refPath);
                jsonWriter.popPath(value);
                return;
            }
        }
        if (jsonWriter.jsonb) {
            if (jsonWriter.isBeanToArray()) {
                valueWriter.writeArrayMappingJSONB(jsonWriter, value, this.fieldName, this.fieldClass, this.features);
            }
            else {
                valueWriter.writeJSONB(jsonWriter, value, this.fieldName, this.fieldClass, this.features);
            }
        }
        else {
            valueWriter.write(jsonWriter, value, this.fieldName, this.fieldClass, this.features);
        }
        if (refDetect) {
            jsonWriter.popPath(value);
        }
    }
    
    static {
        initValueClassUpdater = AtomicReferenceFieldUpdater.newUpdater(FieldWriterObject.class, Class.class, "initValueClass");
    }
}
