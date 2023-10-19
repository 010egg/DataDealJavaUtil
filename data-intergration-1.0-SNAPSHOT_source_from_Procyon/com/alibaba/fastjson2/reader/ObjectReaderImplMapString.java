// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONArray;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.Function;
import java.lang.reflect.Type;

final class ObjectReaderImplMapString extends ObjectReaderImplMapTyped
{
    public ObjectReaderImplMapString(final Class mapType, final Class instanceType, final long features) {
        super(mapType, instanceType, null, String.class, features, null);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        final boolean match = jsonReader.nextIfObjectStart();
        if (!match) {
            if (jsonReader.current() == '[') {
                jsonReader.next();
                if (jsonReader.current() == '{') {
                    final Object arrayItem = this.readObject(jsonReader, String.class, fieldName, features);
                    if (jsonReader.nextIfArrayEnd()) {
                        jsonReader.nextIfComma();
                        return arrayItem;
                    }
                }
                throw new JSONException(jsonReader.info("expect '{', but '['"));
            }
            if (jsonReader.nextIfNullOrEmptyString() || jsonReader.nextIfMatchIdent('\"', 'n', 'u', 'l', 'l', '\"')) {
                return null;
            }
        }
        final JSONReader.Context context = jsonReader.getContext();
        final Map<String, Object> object = (this.instanceType == HashMap.class) ? new HashMap<String, Object>() : ((Map)this.createInstance(context.getFeatures() | features));
        final long contextFeatures = features | context.getFeatures();
        int i = 0;
        while (!jsonReader.nextIfObjectEnd()) {
            final String name = jsonReader.readFieldName();
            final String value = jsonReader.readString();
            if (i != 0 || (contextFeatures & JSONReader.Feature.SupportAutoType.mask) == 0x0L || !name.equals(this.getTypeKey())) {
                final Object origin = object.put(name, value);
                if (origin != null && (contextFeatures & JSONReader.Feature.DuplicateKeyValueAsArray.mask) != 0x0L) {
                    if (origin instanceof Collection) {
                        ((Collection)origin).add(value);
                        object.put(name, origin);
                    }
                    else {
                        final JSONArray array = JSONArray.of(origin, value);
                        object.put(name, array);
                    }
                }
            }
            ++i;
        }
        jsonReader.nextIfMatch(',');
        return object;
    }
}
