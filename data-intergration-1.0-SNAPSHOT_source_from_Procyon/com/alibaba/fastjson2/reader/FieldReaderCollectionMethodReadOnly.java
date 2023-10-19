// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Map;
import java.util.Collections;
import com.alibaba.fastjson2.JSONException;
import java.util.Collection;
import java.lang.reflect.ParameterizedType;
import java.util.function.BiConsumer;
import java.util.Locale;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Type;

class FieldReaderCollectionMethodReadOnly<T> extends FieldReaderObject<T>
{
    FieldReaderCollectionMethodReadOnly(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final JSONSchema schema, final Method setter, final Field field) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, null, null, schema, setter, field, null);
        Type itemType = null;
        if (fieldType instanceof ParameterizedType) {
            final Type[] actualTypeArguments = ((ParameterizedType)fieldType).getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                itemType = actualTypeArguments[0];
            }
        }
        this.itemType = itemType;
    }
    
    @Override
    public void accept(final T object, final Object value) {
        if (value == null) {
            return;
        }
        Collection collection;
        try {
            collection = (Collection)this.method.invoke(object, new Object[0]);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
        if (collection == Collections.EMPTY_LIST || collection == Collections.EMPTY_SET || collection == null) {
            if (this.schema != null) {
                this.schema.assertValidate(collection);
            }
            return;
        }
        final String name = collection.getClass().getName();
        if ("java.util.Collections$UnmodifiableRandomAccessList".equals(name) || "java.util.Arrays$ArrayList".equals(name) || "java.util.Collections$SingletonList".equals(name) || name.startsWith("java.util.ImmutableCollections$")) {
            return;
        }
        if (value == collection) {
            return;
        }
        final Collection values = (Collection)value;
        for (Object item : values) {
            if (item == null) {
                collection.add(null);
            }
            else {
                if (item instanceof Map && this.itemType instanceof Class && !((Class)this.itemType).isAssignableFrom(item.getClass())) {
                    if (this.itemReader == null) {
                        this.itemReader = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(this.itemType);
                    }
                    item = this.itemReader.createInstance((Map)item, 0L);
                }
                collection.add(item);
            }
        }
        if (this.schema != null) {
            this.schema.assertValidate(collection);
        }
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
        final Object value = jsonReader.jsonb ? this.initReader.readJSONBObject(jsonReader, this.fieldType, this.fieldName, 0L) : this.initReader.readObject(jsonReader, this.fieldType, this.fieldName, 0L);
        this.accept(object, value);
    }
}
