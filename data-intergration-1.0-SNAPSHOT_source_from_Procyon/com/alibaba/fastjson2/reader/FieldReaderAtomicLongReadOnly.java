// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.reflect.Field;
import java.util.Locale;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;

final class FieldReaderAtomicLongReadOnly<T> extends FieldReader<T>
{
    FieldReaderAtomicLongReadOnly(final String fieldName, final Class fieldType, final int ordinal, final JSONSchema schema, final Method method) {
        super(fieldName, fieldType, fieldType, ordinal, 0L, null, null, null, schema, method, null);
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
            final AtomicLong atomic = (AtomicLong)this.method.invoke(object, new Object[0]);
            final long longValue = ((Number)value).longValue();
            atomic.set(longValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final Long value = jsonReader.readInt64();
        this.accept(object, value);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        final long longValue = jsonReader.readInt64Value();
        if (jsonReader.wasNull()) {
            return null;
        }
        return new AtomicLong(longValue);
    }
}
