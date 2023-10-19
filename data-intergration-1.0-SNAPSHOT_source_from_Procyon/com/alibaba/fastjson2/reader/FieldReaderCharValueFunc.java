// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.function.ObjCharConsumer;

final class FieldReaderCharValueFunc<T> extends FieldReader<T>
{
    final ObjCharConsumer<T> function;
    
    FieldReaderCharValueFunc(final String fieldName, final int ordinal, final String format, final Character defaultValue, final JSONSchema schema, final Method method, final ObjCharConsumer<T> function) {
        super(fieldName, Character.TYPE, Character.TYPE, ordinal, 0L, format, null, defaultValue, schema, method, null);
        this.function = function;
    }
    
    @Override
    public void accept(final T object, final char value) {
        this.function.accept(object, value);
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
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final char ch = jsonReader.readCharValue();
        if (ch == '\0' && jsonReader.wasNull()) {
            return;
        }
        this.function.accept(object, ch);
    }
    
    @Override
    public String readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readString();
    }
}
