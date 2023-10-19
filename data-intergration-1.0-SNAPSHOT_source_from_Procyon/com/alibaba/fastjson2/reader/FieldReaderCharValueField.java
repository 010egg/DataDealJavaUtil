// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;

final class FieldReaderCharValueField<T> extends FieldReaderObjectField<T>
{
    FieldReaderCharValueField(final String fieldName, final int ordinal, final long features, final String format, final Character defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, Character.TYPE, Character.TYPE, ordinal, features, format, defaultValue, schema, field);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final char ch = jsonReader.readCharValue();
        if (ch == '\0' && jsonReader.wasNull()) {
            return;
        }
        this.accept(object, ch);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        final String str = jsonReader.readString();
        return (str == null || str.isEmpty()) ? '\0' : str.charAt(0);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        char charValue;
        if (value instanceof String) {
            charValue = ((String)value).charAt(0);
        }
        else {
            if (!(value instanceof Character)) {
                throw new JSONException("cast to char error");
            }
            charValue = (char)value;
        }
        this.accept(object, charValue);
    }
}
