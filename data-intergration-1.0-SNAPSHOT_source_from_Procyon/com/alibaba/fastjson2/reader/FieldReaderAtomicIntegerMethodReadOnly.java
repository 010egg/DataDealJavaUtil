// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.reflect.Field;
import java.util.Locale;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;

class FieldReaderAtomicIntegerMethodReadOnly<T> extends FieldReader<T>
{
    FieldReaderAtomicIntegerMethodReadOnly(final String fieldName, final Class fieldType, final int ordinal, final JSONSchema jsonSchema, final Method method) {
        super(fieldName, fieldType, fieldType, ordinal, 0L, null, null, null, jsonSchema, method, null);
    }
    
    @Override
    public boolean isReadOnly() {
        return true;
    }
    
    @Override
    public void accept(final T object, final Object value) {
        if (value == null) {
            return;
        }
        try {
            final AtomicInteger atomic = (AtomicInteger)this.method.invoke(object, new Object[0]);
            final int intValue = ((Number)value).intValue();
            atomic.set(intValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final Integer value = jsonReader.readInt32();
        this.accept(object, value);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        final int intValue = jsonReader.readInt32Value();
        if (jsonReader.wasNull()) {
            return null;
        }
        return new AtomicInteger(intValue);
    }
}
