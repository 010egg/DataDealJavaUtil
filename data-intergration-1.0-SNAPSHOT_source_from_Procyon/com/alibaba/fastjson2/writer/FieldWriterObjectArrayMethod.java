// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.function.Function;
import java.math.BigDecimal;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

final class FieldWriterObjectArrayMethod<T> extends FieldWriter<T>
{
    final Type itemType;
    final Class itemClass;
    ObjectWriter itemObjectWriter;
    
    FieldWriterObjectArrayMethod(final String fieldName, final Type itemType, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method) {
        super(fieldName, ordinal, features, format, label, fieldType, fieldClass, field, method);
        this.itemType = itemType;
        if (itemType instanceof Class) {
            this.itemClass = (Class)itemType;
        }
        else {
            this.itemClass = TypeUtils.getMapping(itemType);
        }
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        try {
            return this.method.invoke(object, new Object[0]);
        }
        catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex2) {
            final Exception ex;
            final Exception e = ex;
            throw new JSONException("field.get error, " + this.fieldName, e);
        }
    }
    
    @Override
    public ObjectWriter getItemWriter(final JSONWriter jsonWriter, final Type itemType) {
        if (itemType != null && itemType != this.itemType) {
            return jsonWriter.getObjectWriter(itemType, null);
        }
        if (this.itemObjectWriter != null) {
            return this.itemObjectWriter;
        }
        if (itemType == Float[].class) {
            if (this.decimalFormat != null) {
                return new ObjectWriterArrayFinal(Float.class, this.decimalFormat);
            }
            return ObjectWriterArrayFinal.FLOAT_ARRAY;
        }
        else if (itemType == Double[].class) {
            if (this.decimalFormat != null) {
                return new ObjectWriterArrayFinal(Double.class, this.decimalFormat);
            }
            return ObjectWriterArrayFinal.DOUBLE_ARRAY;
        }
        else if (itemType == BigDecimal[].class) {
            if (this.decimalFormat != null) {
                return new ObjectWriterArrayFinal(BigDecimal.class, this.decimalFormat);
            }
            return ObjectWriterArrayFinal.DECIMAL_ARRAY;
        }
        else if (itemType == Float.class) {
            if (this.decimalFormat != null) {
                return new ObjectWriterImplFloat(this.decimalFormat);
            }
            return ObjectWriterImplFloat.INSTANCE;
        }
        else if (itemType == Double.class) {
            if (this.decimalFormat != null) {
                return new ObjectWriterImplDouble(this.decimalFormat);
            }
            return ObjectWriterImplDouble.INSTANCE;
        }
        else {
            if (itemType != BigDecimal.class) {
                return this.itemObjectWriter = jsonWriter.getObjectWriter(this.itemType, this.itemClass);
            }
            if (this.decimalFormat != null) {
                return new ObjectWriterImplBigDecimal(this.decimalFormat, null);
            }
            return ObjectWriterImplBigDecimal.INSTANCE;
        }
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
        if (writeFieldName) {
            this.writeFieldName(jsonWriter);
        }
        boolean previousItemRefDetect;
        final boolean refDetect = previousItemRefDetect = jsonWriter.isRefDetect();
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
}
