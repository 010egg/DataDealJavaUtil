// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.function.Function;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

final class FieldWriterObjectArrayField<T> extends FieldWriter<T>
{
    final Type itemType;
    final Class itemClass;
    ObjectWriter itemObjectWriter;
    
    FieldWriterObjectArrayField(final String fieldName, final Type itemType, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field) {
        super(fieldName, ordinal, features, format, label, fieldType, fieldClass, field, null);
        this.itemType = itemType;
        if (itemType instanceof Class) {
            this.itemClass = (Class)itemType;
        }
        else {
            this.itemClass = TypeUtils.getMapping(itemType);
        }
    }
    
    @Override
    public ObjectWriter getItemWriter(final JSONWriter jsonWriter, final Type itemType) {
        if (itemType != null && itemType != this.itemType) {
            return jsonWriter.getObjectWriter(itemType, TypeUtils.getClass(itemType));
        }
        if (this.itemObjectWriter != null) {
            return this.itemObjectWriter;
        }
        if (itemType == Double.class) {
            this.itemObjectWriter = new ObjectWriterImplDouble(new DecimalFormat(this.format));
        }
        else if (itemType == Float.class) {
            this.itemObjectWriter = new ObjectWriterImplFloat(new DecimalFormat(this.format));
        }
        else if (itemType == BigDecimal.class && this.decimalFormat != null) {
            this.itemObjectWriter = new ObjectWriterImplBigDecimal(this.decimalFormat, null);
        }
        else {
            this.itemObjectWriter = jsonWriter.getObjectWriter(this.itemType, this.itemClass);
        }
        return this.itemObjectWriter;
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final Object[] value = (Object[])this.getFieldValue(object);
        if (value != null) {
            this.writeArray(jsonWriter, true, value);
            return true;
        }
        final long features = this.features | jsonWriter.getFeatures();
        if ((features & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask)) != 0x0L) {
            this.writeFieldName(jsonWriter);
            jsonWriter.writeArrayNull();
            return true;
        }
        return false;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final Object[] value = (Object[])this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNull();
            return;
        }
        this.writeArray(jsonWriter, false, value);
    }
    
    public void writeArray(final JSONWriter jsonWriter, final boolean writeFieldName, final Object[] array) {
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        final long features = jsonWriter.getFeatures();
        boolean previousItemRefDetect;
        final boolean refDetect = previousItemRefDetect = ((features & JSONWriter.Feature.ReferenceDetection.mask) != 0x0L);
        if (writeFieldName) {
            if (array.length == 0 && (features & JSONWriter.Feature.NotWriteEmptyArray.mask) != 0x0L) {
                return;
            }
            this.writeFieldName(jsonWriter);
        }
        if (refDetect) {
            final String path = jsonWriter.setPath(this.fieldName, array);
            if (path != null) {
                jsonWriter.writeReference(path);
                return;
            }
        }
        if (jsonWriter.jsonb) {
            final Class arrayClass = array.getClass();
            if (arrayClass != this.fieldClass) {
                jsonWriter.writeTypeName(TypeUtils.getTypeName(arrayClass));
            }
            final int size = array.length;
            jsonWriter.startArray(size);
            for (int i = 0; i < size; ++i) {
                final Object item = array[i];
                if (item == null) {
                    jsonWriter.writeNull();
                }
                else {
                    final Class<?> itemClass = item.getClass();
                    boolean itemRefDetect;
                    if (itemClass != previousClass) {
                        itemRefDetect = jsonWriter.isRefDetect();
                        previousObjectWriter = this.getItemWriter(jsonWriter, itemClass);
                        previousClass = itemClass;
                        if (itemRefDetect) {
                            itemRefDetect = !ObjectWriterProvider.isNotReferenceDetect(itemClass);
                        }
                        previousItemRefDetect = itemRefDetect;
                    }
                    else {
                        itemRefDetect = previousItemRefDetect;
                    }
                    final ObjectWriter itemObjectWriter = previousObjectWriter;
                    if (itemRefDetect) {
                        final String refPath = jsonWriter.setPath(i, item);
                        if (refPath != null) {
                            jsonWriter.writeReference(refPath);
                            jsonWriter.popPath(item);
                            continue;
                        }
                    }
                    itemObjectWriter.writeJSONB(jsonWriter, item, i, this.itemType, this.features);
                    if (itemRefDetect) {
                        jsonWriter.popPath(item);
                    }
                }
            }
            if (refDetect) {
                jsonWriter.popPath(array);
            }
            return;
        }
        jsonWriter.startArray();
        for (int j = 0; j < array.length; ++j) {
            if (j != 0) {
                jsonWriter.writeComma();
            }
            final Object item2 = array[j];
            if (item2 == null) {
                jsonWriter.writeNull();
            }
            else {
                final Class<?> itemClass2 = item2.getClass();
                ObjectWriter itemObjectWriter2;
                if (itemClass2 == previousClass) {
                    itemObjectWriter2 = previousObjectWriter;
                }
                else {
                    itemObjectWriter2 = this.getItemWriter(jsonWriter, itemClass2);
                    previousClass = itemClass2;
                    previousObjectWriter = itemObjectWriter2;
                }
                itemObjectWriter2.write(jsonWriter, item2);
            }
        }
        jsonWriter.endArray();
    }
    
    @Override
    public ObjectWriter getObjectWriter(final JSONWriter jsonWriter, final Class valueClass) {
        if (valueClass == String[].class) {
            return ObjectWriterImplStringArray.INSTANCE;
        }
        if (valueClass == Float[].class) {
            if (this.decimalFormat != null) {
                return new ObjectWriterArrayFinal(Float.class, this.decimalFormat);
            }
            return ObjectWriterArrayFinal.FLOAT_ARRAY;
        }
        else if (valueClass == Double[].class) {
            if (this.decimalFormat != null) {
                return new ObjectWriterArrayFinal(Double.class, this.decimalFormat);
            }
            return ObjectWriterArrayFinal.DOUBLE_ARRAY;
        }
        else {
            if (valueClass != BigDecimal[].class) {
                return jsonWriter.getObjectWriter(valueClass);
            }
            if (this.decimalFormat != null) {
                return new ObjectWriterArrayFinal(BigDecimal.class, this.decimalFormat);
            }
            return ObjectWriterArrayFinal.DECIMAL_ARRAY;
        }
    }
}
