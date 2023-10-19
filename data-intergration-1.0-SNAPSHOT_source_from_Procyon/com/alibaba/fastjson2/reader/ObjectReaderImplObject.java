// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.function.Supplier;
import com.alibaba.fastjson2.JSONArray;
import java.util.HashSet;
import java.util.HashMap;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Map;
import java.util.Collection;
import com.alibaba.fastjson2.JSONObject;

public final class ObjectReaderImplObject extends ObjectReaderPrimitive
{
    public static final ObjectReaderImplObject INSTANCE;
    
    public ObjectReaderImplObject() {
        super(Object.class);
    }
    
    @Override
    public Object createInstance(final long features) {
        return new JSONObject();
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        return collection;
    }
    
    @Override
    public Object createInstance(final Map map, final long features) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final Object typeKey = map.get(this.getTypeKey());
        if (typeKey instanceof String) {
            final String typeName = (String)typeKey;
            final long typeHash = Fnv.hashCode64(typeName);
            ObjectReader reader = null;
            if ((features & JSONReader.Feature.SupportAutoType.mask) != 0x0L) {
                reader = this.autoType(provider, typeHash);
            }
            if (reader == null) {
                reader = provider.getObjectReader(typeName, this.getObjectClass(), features | this.getFeatures());
                if (reader == null) {
                    throw new JSONException("No suitable ObjectReader found for" + typeName);
                }
            }
            if (reader != this) {
                return reader.createInstance(map, features);
            }
        }
        return map;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.jsonb) {
            return jsonReader.readAny();
        }
        final JSONReader.Context context = jsonReader.getContext();
        String typeName = null;
        if (jsonReader.isObject()) {
            jsonReader.nextIfObjectStart();
            long hash = 0L;
            if (jsonReader.isString()) {
                hash = jsonReader.readFieldNameHashCode();
                if (hash == ObjectReaderImplObject.HASH_TYPE) {
                    final boolean supportAutoType = context.isEnabled(JSONReader.Feature.SupportAutoType);
                    ObjectReader autoTypeObjectReader;
                    if (supportAutoType) {
                        final long typeHash = jsonReader.readTypeHashCode();
                        autoTypeObjectReader = context.getObjectReaderAutoType(typeHash);
                        if (autoTypeObjectReader != null) {
                            final Class objectClass = autoTypeObjectReader.getObjectClass();
                            if (objectClass != null) {
                                final ClassLoader objectClassLoader = objectClass.getClassLoader();
                                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                                if (objectClassLoader != classLoader) {
                                    Class contextClass = null;
                                    typeName = jsonReader.getString();
                                    try {
                                        if (classLoader == null) {
                                            classLoader = this.getClass().getClassLoader();
                                        }
                                        contextClass = classLoader.loadClass(typeName);
                                    }
                                    catch (ClassNotFoundException ex) {}
                                    if (!objectClass.equals(contextClass)) {
                                        autoTypeObjectReader = context.getObjectReader(contextClass);
                                    }
                                }
                            }
                        }
                        if (autoTypeObjectReader == null) {
                            typeName = jsonReader.getString();
                            autoTypeObjectReader = context.getObjectReaderAutoType(typeName, null);
                        }
                    }
                    else {
                        typeName = jsonReader.readString();
                        autoTypeObjectReader = context.getObjectReaderAutoType(typeName, null);
                        if (autoTypeObjectReader == null && jsonReader.getContext().isEnabled(JSONReader.Feature.ErrorOnNotSupportAutoType)) {
                            throw new JSONException(jsonReader.info("autoType not support : " + typeName));
                        }
                    }
                    if (autoTypeObjectReader != null) {
                        jsonReader.setTypeRedirect(true);
                        return autoTypeObjectReader.readObject(jsonReader, fieldType, fieldName, features);
                    }
                }
            }
            final Supplier<Map> objectSupplier = jsonReader.getContext().getObjectSupplier();
            Map object;
            if (objectSupplier != null) {
                object = objectSupplier.get();
            }
            else if (((features | context.getFeatures()) & JSONReader.Feature.UseNativeObject.mask) != 0x0L) {
                object = new HashMap();
            }
            else {
                object = (Map)ObjectReaderImplMap.INSTANCE_OBJECT.createInstance(jsonReader.features(features));
            }
            if (typeName != null) {
                final String s = typeName;
                switch (s) {
                    case "java.util.ImmutableCollections$Map1":
                    case "java.util.ImmutableCollections$MapN": {
                        break;
                    }
                    default: {
                        object.put("@type", typeName);
                        break;
                    }
                }
                hash = 0L;
            }
            int i = 0;
            while (!jsonReader.nextIfObjectEnd()) {
                Object name;
                if (i == 0 && typeName == null && hash != 0L) {
                    name = jsonReader.getFieldName();
                }
                else if (jsonReader.isNumber()) {
                    name = jsonReader.readNumber();
                    jsonReader.nextIfMatch(':');
                }
                else {
                    name = jsonReader.readFieldName();
                }
                if (name == null) {
                    final char current = jsonReader.current();
                    if (current == '{' || current == '[') {
                        name = jsonReader.readAny();
                        if (!jsonReader.nextIfMatch(':')) {
                            throw new JSONException(jsonReader.info("illegal input"));
                        }
                    }
                    else {
                        name = jsonReader.readFieldNameUnquote();
                        if (jsonReader.current() == ':') {
                            jsonReader.next();
                        }
                    }
                }
                Object value = null;
                switch (jsonReader.current()) {
                    case '+':
                    case '-':
                    case '.':
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9': {
                        value = jsonReader.readNumber();
                        break;
                    }
                    case '[': {
                        value = jsonReader.readArray();
                        break;
                    }
                    case '{': {
                        value = jsonReader.readObject();
                        break;
                    }
                    case '\"':
                    case '\'': {
                        value = jsonReader.readString();
                        break;
                    }
                    case 'f':
                    case 't': {
                        value = jsonReader.readBoolValue();
                        break;
                    }
                    case 'n': {
                        jsonReader.readNull();
                        value = null;
                        break;
                    }
                    case 'S': {
                        if (jsonReader.nextIfSet()) {
                            value = jsonReader.read(HashSet.class);
                            break;
                        }
                        throw new JSONException(jsonReader.info());
                    }
                    default: {
                        throw new JSONException(jsonReader.info());
                    }
                }
                final Object origin = object.put(name, value);
                if (origin != null) {
                    final long contextFeatures = features | context.getFeatures();
                    if ((contextFeatures & JSONReader.Feature.DuplicateKeyValueAsArray.mask) != 0x0L) {
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
            jsonReader.nextIfComma();
            return object;
        }
        char ch = jsonReader.current();
        if (ch == '/') {
            jsonReader.skipLineComment();
            ch = jsonReader.current();
        }
        Object value2 = null;
        switch (ch) {
            case '+':
            case '-':
            case '.':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                value2 = jsonReader.readNumber();
                break;
            }
            case '[': {
                value2 = jsonReader.readArray();
                break;
            }
            case '\"':
            case '\'': {
                value2 = jsonReader.readString();
                break;
            }
            case 'f':
            case 't': {
                value2 = jsonReader.readBoolValue();
                break;
            }
            case 'n': {
                value2 = jsonReader.readNullOrNewDate();
                break;
            }
            case 'S': {
                if (jsonReader.nextIfSet()) {
                    final HashSet<Object> set = new HashSet<Object>();
                    jsonReader.read(set);
                    value2 = set;
                    break;
                }
                throw new JSONException(jsonReader.info());
            }
            default: {
                throw new JSONException(jsonReader.info());
            }
        }
        return value2;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final byte type = jsonReader.getType();
        if (type >= 73 && type <= 125) {
            return jsonReader.readString();
        }
        if (type == -110) {
            final ObjectReader autoTypeObjectReader = jsonReader.checkAutoType(Object.class, 0L, features);
            if (autoTypeObjectReader != null) {
                return autoTypeObjectReader.readJSONBObject(jsonReader, fieldType, fieldName, features);
            }
        }
        if (type == -81) {
            jsonReader.next();
            return null;
        }
        return jsonReader.readAny();
    }
    
    static {
        INSTANCE = new ObjectReaderImplObject();
    }
}
