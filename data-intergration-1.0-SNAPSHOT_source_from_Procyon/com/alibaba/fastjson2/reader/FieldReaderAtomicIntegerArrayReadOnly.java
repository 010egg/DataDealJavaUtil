// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.lang.reflect.Field;
import java.util.Locale;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;

final class FieldReaderAtomicIntegerArrayReadOnly<T> extends FieldReader<T>
{
    FieldReaderAtomicIntegerArrayReadOnly(final String fieldName, final Class fieldType, final int ordinal, final JSONSchema jsonSchema, final Method method) {
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
            final AtomicIntegerArray atomic = (AtomicIntegerArray)this.method.invoke(object, new Object[0]);
            if (value instanceof AtomicIntegerArray) {
                final AtomicIntegerArray array = (AtomicIntegerArray)value;
                for (int i = 0; i < array.length(); ++i) {
                    atomic.set(i, array.get(i));
                }
            }
            else {
                final List values = (List)value;
                for (int i = 0; i < values.size(); ++i) {
                    final int itemValue = TypeUtils.toIntValue(values.get(i));
                    atomic.set(i, itemValue);
                }
            }
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        if (jsonReader.readIfNull()) {
            return;
        }
        AtomicIntegerArray atomic;
        try {
            atomic = (AtomicIntegerArray)this.method.invoke(object, new Object[0]);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
        if (jsonReader.nextIfArrayStart()) {
            int i = 0;
            while (!jsonReader.nextIfArrayEnd()) {
                final int value = jsonReader.readInt32Value();
                if (atomic != null && i < atomic.length()) {
                    atomic.set(i, value);
                }
                ++i;
            }
        }
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        return jsonReader.readArray(Integer.class);
    }
}
