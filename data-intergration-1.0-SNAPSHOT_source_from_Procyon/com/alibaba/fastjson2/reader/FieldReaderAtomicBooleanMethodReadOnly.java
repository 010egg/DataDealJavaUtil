// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.reflect.Field;
import java.util.Locale;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;

final class FieldReaderAtomicBooleanMethodReadOnly<T> extends FieldReader<T>
{
    FieldReaderAtomicBooleanMethodReadOnly(final String fieldName, final Class fieldClass, final int ordinal, final JSONSchema schema, final Method method) {
        super(fieldName, fieldClass, fieldClass, ordinal, 0L, null, null, null, schema, method, null);
    }
    
    @Override
    public boolean isReadOnly() {
        return true;
    }
    
    @Override
    public void accept(final T object, Object value) {
        if (value == null) {
            return;
        }
        try {
            final AtomicBoolean atomic = (AtomicBoolean)this.method.invoke(object, new Object[0]);
            if (value instanceof AtomicBoolean) {
                value = ((AtomicBoolean)value).get();
            }
            atomic.set((boolean)value);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final Boolean value = jsonReader.readBool();
        this.accept(object, value);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readBool();
    }
}
