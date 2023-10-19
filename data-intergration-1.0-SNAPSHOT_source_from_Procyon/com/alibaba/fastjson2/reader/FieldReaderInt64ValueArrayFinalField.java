// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;

final class FieldReaderInt64ValueArrayFinalField<T> extends FieldReaderObjectField<T>
{
    FieldReaderInt64ValueArrayFinalField(final String fieldName, final Class fieldType, final int ordinal, final long features, final String format, final long[] defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldType, ordinal, features, format, defaultValue, schema, field);
    }
    
    @Override
    public boolean isReadOnly() {
        return true;
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        if (jsonReader.readIfNull()) {
            return;
        }
        long[] array;
        try {
            array = (long[])this.field.get(object);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
        if (jsonReader.nextIfArrayStart()) {
            int i = 0;
            while (!jsonReader.nextIfArrayEnd()) {
                final long value = jsonReader.readInt64Value();
                if (array != null && i < array.length) {
                    array[i] = value;
                }
                ++i;
            }
        }
    }
}
