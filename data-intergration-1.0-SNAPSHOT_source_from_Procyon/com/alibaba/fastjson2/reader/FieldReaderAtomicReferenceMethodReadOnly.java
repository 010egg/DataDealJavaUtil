// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Type;

final class FieldReaderAtomicReferenceMethodReadOnly<T> extends FieldReaderAtomicReference<T>
{
    FieldReaderAtomicReferenceMethodReadOnly(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final JSONSchema schema, final Method method) {
        super(fieldName, fieldType, fieldClass, ordinal, 0L, null, schema, method, null);
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
            final AtomicReference atomic = (AtomicReference)this.method.invoke(object, new Object[0]);
            atomic.set(value);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
