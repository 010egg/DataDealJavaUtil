// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.concurrent.atomic.AtomicBoolean;

final class FieldReaderAtomicBooleanFieldReadOnly<T> extends FieldReader<T>
{
    FieldReaderAtomicBooleanFieldReadOnly(final String fieldName, final Class fieldClass, final int ordinal, final String format, final AtomicBoolean defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldClass, fieldClass, ordinal, 0L, format, null, defaultValue, schema, null, field);
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
            final AtomicBoolean atomic = (AtomicBoolean)this.field.get(object);
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
