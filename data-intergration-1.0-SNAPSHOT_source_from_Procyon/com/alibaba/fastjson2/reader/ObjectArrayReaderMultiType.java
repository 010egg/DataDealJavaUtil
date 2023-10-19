// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.Collection;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.MultiType;
import java.lang.reflect.Type;

final class ObjectArrayReaderMultiType implements ObjectReader
{
    final Type[] types;
    final ObjectReader[] readers;
    
    ObjectArrayReaderMultiType(final MultiType multiType) {
        final Type[] types = new Type[multiType.size()];
        for (int i = 0; i < multiType.size(); ++i) {
            types[i] = multiType.getType(i);
        }
        this.types = types;
        this.readers = new ObjectReader[types.length];
    }
    
    ObjectReader getObjectReader(final JSONReader jsonReader, final int index) {
        ObjectReader objectReader = this.readers[index];
        if (objectReader == null) {
            final Type type = this.types[index];
            objectReader = jsonReader.getObjectReader(type);
            this.readers[index] = objectReader;
        }
        return objectReader;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, 0L);
        }
        if (jsonReader.nextIfNullOrEmptyString()) {
            return null;
        }
        final Object[] values = new Object[this.types.length];
        if (jsonReader.nextIfArrayStart()) {
            int i = 0;
            while (!jsonReader.nextIfArrayEnd()) {
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
                    final ObjectReader objectReader = this.getObjectReader(jsonReader, i);
                    value = objectReader.readObject(jsonReader, this.types[i], i, features);
                }
                values[i] = value;
                jsonReader.nextIfComma();
                ++i;
            }
            jsonReader.nextIfComma();
            return values;
        }
        throw new JSONException(jsonReader.info("TODO"));
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final int entryCnt = jsonReader.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final Object[] values = new Object[this.types.length];
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
                final ObjectReader objectReader = this.getObjectReader(jsonReader, i);
                value = objectReader.readObject(jsonReader, this.types[i], i, features);
            }
            values[i] = value;
        }
        return values;
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        return new Object[this.types.length];
    }
}
