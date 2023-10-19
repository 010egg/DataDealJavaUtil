// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.function.Function;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONArray;
import java.util.Map;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Collection;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONException;
import java.util.Arrays;
import java.lang.reflect.Array;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;

final class ObjectArrayTypedReader extends ObjectReaderPrimitive
{
    final Class componentType;
    final Class componentClass;
    final long componentClassHash;
    final String typeName;
    final long typeNameHashCode;
    
    ObjectArrayTypedReader(final Class objectClass) {
        super(objectClass);
        this.componentType = objectClass.getComponentType();
        final String componentTypeName = TypeUtils.getTypeName(this.componentType);
        this.componentClassHash = Fnv.hashCode64(componentTypeName);
        this.typeName = '[' + componentTypeName;
        this.typeNameHashCode = Fnv.hashCode64(this.typeName);
        this.componentClass = TypeUtils.getClass(this.componentType);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, 0L);
        }
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (jsonReader.nextIfArrayStart()) {
            Object[] values = (Object[])Array.newInstance(this.componentType, 16);
            int size = 0;
            while (!jsonReader.nextIfArrayEnd()) {
                final int minCapacity = size + 1;
                if (minCapacity - values.length > 0) {
                    final int oldCapacity = values.length;
                    int newCapacity = oldCapacity + (oldCapacity >> 1);
                    if (newCapacity - minCapacity < 0) {
                        newCapacity = minCapacity;
                    }
                    values = Arrays.copyOf(values, newCapacity);
                }
                final Object value = jsonReader.read((Class<Object>)this.componentType);
                values[size++] = value;
                jsonReader.nextIfComma();
            }
            jsonReader.nextIfMatch(',');
            return Arrays.copyOf(values, size);
        }
        if (jsonReader.current() == '\"') {
            final String str = jsonReader.readString();
            if (str.isEmpty()) {
                return null;
            }
        }
        throw new JSONException(jsonReader.info("TODO"));
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.getType() == -110) {
            jsonReader.next();
            final long typeHash = jsonReader.readTypeHashCode();
            if (typeHash != ObjectArrayReader.TYPE_HASH_CODE) {
                if (typeHash != this.typeNameHashCode) {
                    if (!jsonReader.isSupportAutoType(features)) {
                        throw new JSONException(jsonReader.info("not support autotype : " + jsonReader.getString()));
                    }
                    final ObjectReader autoTypeObjectReader = jsonReader.getObjectReaderAutoType(typeHash, this.objectClass, features);
                    if (autoTypeObjectReader == null) {
                        throw new JSONException(jsonReader.info("auotype not support : " + jsonReader.getString()));
                    }
                    return autoTypeObjectReader.readObject(jsonReader, fieldType, fieldName, features);
                }
            }
        }
        final int entryCnt = jsonReader.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final Object[] values = (Object[])Array.newInstance(this.componentClass, entryCnt);
        for (int i = 0; i < entryCnt; ++i) {
            Object value;
            if (jsonReader.isReference()) {
                final String reference = jsonReader.readReference();
                if ("..".equals(reference)) {
                    value = values;
                }
                else {
                    value = null;
                    jsonReader.addResolveTask(values, i, JSONPath.of(reference));
                }
            }
            else {
                final ObjectReader autoTypeReader = jsonReader.checkAutoType(this.componentClass, this.componentClassHash, features);
                if (autoTypeReader != null) {
                    value = autoTypeReader.readJSONBObject(jsonReader, null, null, features);
                }
                else {
                    value = jsonReader.read((Class<Object>)this.componentType);
                }
            }
            values[i] = value;
        }
        return values;
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        final Object[] values = (Object[])Array.newInstance(this.componentClass, collection.size());
        int index = 0;
        for (Object item : collection) {
            if (item != null) {
                final Class<?> valueClass = item.getClass();
                if (valueClass != this.componentType) {
                    final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
                    final Function typeConvert = provider.getTypeConvert(valueClass, this.componentType);
                    if (typeConvert != null) {
                        item = typeConvert.apply(item);
                    }
                }
            }
            if (!this.componentType.isInstance(item)) {
                final ObjectReader objectReader = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(this.componentType);
                if (item instanceof Map) {
                    item = objectReader.createInstance((Map)item, new JSONReader.Feature[0]);
                }
                else if (item instanceof Collection) {
                    item = objectReader.createInstance((Collection)item);
                }
                else if (item instanceof Object[]) {
                    item = objectReader.createInstance(JSONArray.of((Object[])item));
                }
                else if (item != null) {
                    final Class<?> itemClass = item.getClass();
                    if (!itemClass.isArray()) {
                        throw new JSONException("component type not match, expect " + this.componentType.getName() + ", but " + itemClass);
                    }
                    final int length = Array.getLength(item);
                    final JSONArray array = new JSONArray(length);
                    for (int i = 0; i < length; ++i) {
                        array.add(Array.get(item, i));
                    }
                    item = objectReader.createInstance(array);
                }
            }
            values[index++] = item;
        }
        return values;
    }
}
