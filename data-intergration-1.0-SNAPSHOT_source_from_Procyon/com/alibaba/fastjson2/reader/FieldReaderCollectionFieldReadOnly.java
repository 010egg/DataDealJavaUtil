// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import java.util.Collections;
import com.alibaba.fastjson2.JSONException;
import java.util.Collection;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Type;

final class FieldReaderCollectionFieldReadOnly<T> extends FieldReaderObjectField<T>
{
    FieldReaderCollectionFieldReadOnly(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, null, schema, field);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        if (value == null) {
            return;
        }
        Collection collection;
        try {
            collection = (Collection)this.field.get(object);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
        if (collection == Collections.EMPTY_LIST || collection == Collections.EMPTY_SET || collection == null) {
            return;
        }
        final String name = collection.getClass().getName();
        if ("java.util.Collections$UnmodifiableRandomAccessList".equals(name) || "java.util.Arrays$ArrayList".equals(name) || "java.util.Collections$SingletonList".equals(name) || name.startsWith("java.util.ImmutableCollections$")) {
            return;
        }
        collection.addAll((Collection)value);
    }
    
    @Override
    public boolean isReadOnly() {
        return true;
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        if (this.initReader == null) {
            this.initReader = jsonReader.getContext().getObjectReader(this.fieldType);
        }
        final Object value = this.initReader.readObject(jsonReader, this.fieldType, this.fieldName, 0L);
        this.accept(object, value);
    }
}
