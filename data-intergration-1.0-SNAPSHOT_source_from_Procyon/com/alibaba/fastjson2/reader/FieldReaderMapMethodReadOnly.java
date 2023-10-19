// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.Collections;
import com.alibaba.fastjson2.JSONException;
import java.util.Map;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.BiConsumer;
import java.util.Locale;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Type;

class FieldReaderMapMethodReadOnly<T> extends FieldReaderObject<T>
{
    FieldReaderMapMethodReadOnly(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final JSONSchema schema, final Method method, final Field field) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, null, null, schema, method, field, null);
    }
    
    @Override
    public ObjectReader getItemObjectReader(final JSONReader jsonReader) {
        if (this.itemReader != null) {
            return this.itemReader;
        }
        final ObjectReader objectReader = this.getObjectReader(jsonReader);
        if (objectReader instanceof ObjectReaderImplMap) {
            return this.itemReader = ObjectReaderImplString.INSTANCE;
        }
        if (objectReader instanceof ObjectReaderImplMapTyped) {
            final Type valueType = ((ObjectReaderImplMapTyped)objectReader).valueType;
            return this.itemReader = jsonReader.getObjectReader(valueType);
        }
        return ObjectReaderImplObject.INSTANCE;
    }
    
    @Override
    public void accept(final T object, final Object value) {
        if (value == null) {
            return;
        }
        Map map;
        try {
            map = (Map)this.method.invoke(object, new Object[0]);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
        if (map == Collections.EMPTY_MAP || map == null) {
            return;
        }
        final String name = map.getClass().getName();
        if ("java.util.Collections$UnmodifiableMap".equals(name)) {
            return;
        }
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        map.putAll((Map)value);
    }
    
    @Override
    public void processExtra(final JSONReader jsonReader, final Object object) {
        Map map;
        try {
            map = (Map)this.method.invoke(object, new Object[0]);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
        final String name = jsonReader.getFieldName();
        final ObjectReader itemObjectReader = this.getItemObjectReader(jsonReader);
        final Object value = itemObjectReader.readObject(jsonReader, this.getItemType(), this.fieldName, 0L);
        map.put(name, value);
    }
    
    @Override
    public void acceptExtra(final Object object, final String name, final Object value) {
        Map map;
        try {
            map = (Map)this.method.invoke(object, new Object[0]);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error");
        }
        map.put(name, value);
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
        Object value;
        if (jsonReader.jsonb) {
            value = this.initReader.readJSONBObject(jsonReader, this.getItemType(), this.fieldName, this.features);
        }
        else {
            value = this.initReader.readObject(jsonReader, this.getItemType(), this.fieldName, this.features);
        }
        this.accept(object, value);
    }
}
