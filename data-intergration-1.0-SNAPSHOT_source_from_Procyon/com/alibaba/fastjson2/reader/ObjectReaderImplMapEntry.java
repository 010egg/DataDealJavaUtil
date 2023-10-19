// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.AbstractMap;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.util.Map;
import java.lang.reflect.Type;

class ObjectReaderImplMapEntry extends ObjectReaderPrimitive
{
    final Type keyType;
    final Type valueType;
    volatile ObjectReader keyReader;
    volatile ObjectReader valueReader;
    
    public ObjectReaderImplMapEntry(final Type keyType, final Type valueType) {
        super(Map.Entry.class);
        this.keyType = keyType;
        this.valueType = valueType;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final int entryCnt = jsonReader.startArray();
        if (entryCnt != 2) {
            throw new JSONException(jsonReader.info("entryCnt must be 2, but " + entryCnt));
        }
        Object key;
        if (this.keyType == null) {
            key = jsonReader.readAny();
        }
        else {
            if (this.keyReader == null) {
                this.keyReader = jsonReader.getObjectReader(this.keyType);
            }
            key = this.keyReader.readObject(jsonReader, fieldType, fieldName, features);
        }
        Object value;
        if (this.valueType == null) {
            value = jsonReader.readAny();
        }
        else {
            if (this.valueReader == null) {
                this.valueReader = jsonReader.getObjectReader(this.valueType);
            }
            value = this.valueReader.readObject(jsonReader, fieldType, fieldName, features);
        }
        return new AbstractMap.SimpleEntry(key, value);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        jsonReader.nextIfObjectStart();
        final Object key = jsonReader.readAny();
        jsonReader.nextIfMatch(':');
        Object value;
        if (this.valueType == null) {
            value = jsonReader.readAny();
        }
        else {
            if (this.valueReader == null) {
                this.valueReader = jsonReader.getObjectReader(this.valueType);
            }
            value = this.valueReader.readObject(jsonReader, fieldType, fieldName, features);
        }
        jsonReader.nextIfObjectEnd();
        jsonReader.nextIfComma();
        return new AbstractMap.SimpleEntry(key, value);
    }
}
