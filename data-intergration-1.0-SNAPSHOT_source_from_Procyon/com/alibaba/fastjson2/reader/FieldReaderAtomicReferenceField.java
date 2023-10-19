// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Type;

final class FieldReaderAtomicReferenceField<T> extends FieldReaderAtomicReference<T>
{
    final boolean readOnly;
    
    FieldReaderAtomicReferenceField(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final String format, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldClass, ordinal, 0L, format, schema, null, field);
        this.readOnly = Modifier.isFinal(field.getModifiers());
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
            if (this.readOnly) {
                final AtomicReference atomic = (AtomicReference)this.field.get(object);
                atomic.set(value);
            }
            else {
                this.field.set(object, new AtomicReference(value));
            }
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
