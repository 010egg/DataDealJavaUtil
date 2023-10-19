// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.function.Supplier;
import java.util.EnumMap;
import java.util.concurrent.ConcurrentMap;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.util.ReferenceKey;
import com.alibaba.fastjson2.JSONReader;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONException;
import java.util.Collection;
import java.util.List;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.HashMap;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Map;
import java.lang.reflect.Modifier;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Constructor;
import java.util.function.Function;
import java.lang.reflect.Type;

class ObjectReaderImplMapTyped implements ObjectReader
{
    final Class mapType;
    final Class instanceType;
    final Type keyType;
    final Type valueType;
    final Class valueClass;
    final long features;
    final Function builder;
    final Constructor defaultConstructor;
    ObjectReader valueObjectReader;
    ObjectReader keyObjectReader;
    
    public ObjectReaderImplMapTyped(final Class mapType, final Class instanceType, Type keyType, final Type valueType, final long features, final Function builder) {
        if (keyType == Object.class) {
            keyType = null;
        }
        this.mapType = mapType;
        this.instanceType = instanceType;
        this.keyType = keyType;
        this.valueType = valueType;
        this.valueClass = TypeUtils.getClass(valueType);
        this.features = features;
        this.builder = builder;
        Constructor defaultConstructor = null;
        final Constructor[] declaredConstructors;
        final Constructor[] constructors = declaredConstructors = this.instanceType.getDeclaredConstructors();
        for (final Constructor constructor : declaredConstructors) {
            if (constructor.getParameterCount() == 0 && !Modifier.isPublic(constructor.getModifiers())) {
                constructor.setAccessible(true);
                defaultConstructor = constructor;
                break;
            }
        }
        this.defaultConstructor = defaultConstructor;
    }
    
    @Override
    public Class getObjectClass() {
        return this.mapType;
    }
    
    @Override
    public Object createInstance(final Map input, final long features) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        Map object;
        if (this.instanceType == Map.class || this.instanceType == HashMap.class) {
            object = new HashMap();
        }
        else {
            object = (Map)this.createInstance(features);
        }
        for (final Map.Entry entry : input.entrySet()) {
            final Object key = entry.getKey();
            final Object fieldValue = entry.getValue();
            Object fieldName;
            if (this.keyType == null || this.keyType == String.class) {
                fieldName = key.toString();
            }
            else {
                fieldName = TypeUtils.cast(key, this.keyType);
            }
            Object value = fieldValue;
            if (value != null) {
                final Class<?> valueClass = value.getClass();
                if (this.valueType != Object.class) {
                    if (valueClass == JSONObject.class || valueClass == TypeUtils.CLASS_JSON_OBJECT_1x) {
                        if (this.valueObjectReader == null) {
                            this.valueObjectReader = provider.getObjectReader(this.valueType);
                        }
                        try {
                            value = this.valueObjectReader.createInstance((Map)value, features);
                        }
                        catch (Exception ex) {}
                    }
                    else if ((valueClass == JSONArray.class || valueClass == TypeUtils.CLASS_JSON_ARRAY_1x) && this.valueClass == List.class) {
                        if (this.valueObjectReader == null) {
                            this.valueObjectReader = provider.getObjectReader(this.valueType);
                        }
                        try {
                            value = this.valueObjectReader.createInstance((Collection)value);
                        }
                        catch (Exception ex2) {}
                    }
                    else {
                        final Function typeConvert;
                        if ((typeConvert = provider.getTypeConvert(valueClass, this.valueType)) != null) {
                            value = typeConvert.apply(value);
                        }
                        else if (value instanceof Map) {
                            final Map map = (Map)value;
                            if (this.valueObjectReader == null) {
                                this.valueObjectReader = provider.getObjectReader(this.valueType);
                            }
                            try {
                                value = this.valueObjectReader.createInstance(map, features);
                            }
                            catch (Exception ex3) {}
                        }
                        else if (value instanceof Collection) {
                            if (this.valueObjectReader == null) {
                                this.valueObjectReader = provider.getObjectReader(this.valueType);
                            }
                            value = this.valueObjectReader.createInstance((Collection)value);
                        }
                        else if (!valueClass.isInstance(value)) {
                            throw new JSONException("can not convert from " + valueClass + " to " + this.valueType);
                        }
                    }
                }
            }
            object.put(fieldName, value);
        }
        if (this.builder != null) {
            return this.builder.apply(object);
        }
        return object;
    }
    
    @Override
    public Object createInstance(final long features) {
        if (this.instanceType != null && !this.instanceType.isInterface()) {
            try {
                if (this.defaultConstructor != null) {
                    return this.defaultConstructor.newInstance(new Object[0]);
                }
                return this.instanceType.newInstance();
            }
            catch (Exception e) {
                throw new JSONException("create map error", e);
            }
        }
        return new HashMap();
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        ObjectReader objectReader = null;
        Function builder = this.builder;
        if (jsonReader.getType() == -110) {
            objectReader = jsonReader.checkAutoType(this.mapType, 0L, this.features | features);
            if (objectReader != null && objectReader != this) {
                builder = objectReader.getBuildFunction();
                if (!(objectReader instanceof ObjectReaderImplMap) && !(objectReader instanceof ObjectReaderImplMapTyped)) {
                    return objectReader.readJSONBObject(jsonReader, fieldType, fieldName, features);
                }
            }
        }
        final byte firstType = jsonReader.getType();
        if (firstType == -81) {
            jsonReader.next();
            return null;
        }
        if (firstType == -90) {
            jsonReader.next();
        }
        final JSONReader.Context context = jsonReader.getContext();
        final long contextFeatures = features | context.getFeatures();
        Map object;
        if (objectReader != null) {
            object = objectReader.createInstance(contextFeatures);
        }
        else {
            object = ((this.instanceType == HashMap.class) ? new HashMap() : ((Map)this.createInstance()));
        }
        int i = 0;
        while (true) {
            final byte type = jsonReader.getType();
            if (type == -91) {
                break;
            }
            Object name;
            if (this.keyType == String.class || jsonReader.isString()) {
                name = jsonReader.readFieldName();
            }
            else if (jsonReader.isReference()) {
                final String reference = jsonReader.readReference();
                name = new ReferenceKey(i);
                jsonReader.addResolveTask(object, name, JSONPath.of(reference));
            }
            else {
                if (this.keyObjectReader == null && this.keyType != null) {
                    this.keyObjectReader = jsonReader.getObjectReader(this.keyType);
                }
                if (this.keyObjectReader == null) {
                    name = jsonReader.readAny();
                }
                else {
                    name = this.keyObjectReader.readJSONBObject(jsonReader, null, null, features);
                }
            }
            if (jsonReader.isReference()) {
                final String reference = jsonReader.readReference();
                if ("..".equals(reference)) {
                    object.put(name, object);
                }
                else {
                    jsonReader.addResolveTask(object, name, JSONPath.of(reference));
                    if (!(object instanceof ConcurrentMap)) {
                        object.put(name, null);
                    }
                }
            }
            else if (jsonReader.nextIfNull()) {
                object.put(name, null);
            }
            else {
                Object value;
                if (this.valueType == Object.class) {
                    value = jsonReader.readAny();
                }
                else {
                    final ObjectReader autoTypeValueReader = jsonReader.checkAutoType(this.valueClass, 0L, features);
                    if (autoTypeValueReader != null && autoTypeValueReader != this) {
                        value = autoTypeValueReader.readJSONBObject(jsonReader, this.valueType, name, features);
                    }
                    else {
                        if (this.valueObjectReader == null) {
                            this.valueObjectReader = jsonReader.getObjectReader(this.valueType);
                        }
                        value = this.valueObjectReader.readJSONBObject(jsonReader, this.valueType, name, features);
                    }
                }
                object.put(name, value);
            }
            ++i;
        }
        jsonReader.next();
        if (builder == null) {
            return object;
        }
        if (builder == ObjectReaderImplMap.ENUM_MAP_BUILDER && object.isEmpty()) {
            return new EnumMap((Class<Enum>)this.keyType);
        }
        return builder.apply(object);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        int index = 0;
        if (!jsonReader.nextIfObjectStart()) {
            if (jsonReader.isTypeRedirect()) {
                index = 1;
                jsonReader.setTypeRedirect(false);
            }
            else {
                if (jsonReader.nextIfNullOrEmptyString()) {
                    return null;
                }
                throw new JSONException(jsonReader.info("expect '{', but '" + jsonReader.current() + "'"));
            }
        }
        final JSONReader.Context context = jsonReader.getContext();
        final long contextFeatures = context.getFeatures() | features;
        Map innerMap = null;
        Map object;
        if (this.instanceType == HashMap.class) {
            final Supplier<Map> objectSupplier = context.getObjectSupplier();
            if (this.mapType == Map.class && objectSupplier != null) {
                object = objectSupplier.get();
                innerMap = TypeUtils.getInnerMap(object);
            }
            else {
                object = new HashMap();
            }
        }
        else {
            object = (Map)this.createInstance(contextFeatures);
        }
        while (!jsonReader.nextIfObjectEnd() && !jsonReader.isEnd()) {
            Label_0750: {
                Object name;
                if (jsonReader.nextIfNull()) {
                    if (!jsonReader.nextIfMatch(':')) {
                        throw new JSONException(jsonReader.info("illegal json"));
                    }
                    name = null;
                }
                else if (this.keyType == String.class) {
                    name = jsonReader.readFieldName();
                    if (index == 0 && (contextFeatures & JSONReader.Feature.SupportAutoType.mask) != 0x0L && name.equals(this.getTypeKey())) {
                        final long typeHashCode = jsonReader.readTypeHashCode();
                        final ObjectReader objectReaderAutoType = jsonReader.getObjectReaderAutoType(typeHashCode, this.mapType, features);
                        if (objectReaderAutoType != null && objectReaderAutoType instanceof ObjectReaderImplMap && !object.getClass().equals(((ObjectReaderImplMap)objectReaderAutoType).instanceType)) {
                            object = objectReaderAutoType.createInstance(features);
                        }
                        break Label_0750;
                    }
                    else if (name == null) {
                        name = jsonReader.readString();
                        if (!jsonReader.nextIfMatch(':')) {
                            throw new JSONException(jsonReader.info("illegal json"));
                        }
                    }
                }
                else if (index == 0 && jsonReader.isEnabled(JSONReader.Feature.SupportAutoType) && jsonReader.current() == '\"' && (!(this.keyType instanceof Class) || !Enum.class.isAssignableFrom((Class<?>)this.keyType))) {
                    name = jsonReader.readFieldName();
                    if (name.equals(this.getTypeKey())) {
                        final long typeHashCode = jsonReader.readTypeHashCode();
                        final ObjectReader objectReaderAutoType = jsonReader.getObjectReaderAutoType(typeHashCode, this.mapType, features);
                        if (objectReaderAutoType != null && objectReaderAutoType instanceof ObjectReaderImplMap && !object.getClass().equals(((ObjectReaderImplMap)objectReaderAutoType).instanceType)) {
                            object = objectReaderAutoType.createInstance(features);
                        }
                        break Label_0750;
                    }
                    else {
                        name = TypeUtils.cast(name, this.keyType);
                    }
                }
                else {
                    if (this.keyObjectReader != null) {
                        name = this.keyObjectReader.readObject(jsonReader, null, null, 0L);
                    }
                    else {
                        name = jsonReader.read(this.keyType);
                    }
                    if (index == 0 && (contextFeatures & JSONReader.Feature.SupportAutoType.mask) != 0x0L && name.equals(this.getTypeKey())) {
                        break Label_0750;
                    }
                    jsonReader.nextIfMatch(':');
                }
                if (this.valueObjectReader == null) {
                    this.valueObjectReader = jsonReader.getObjectReader(this.valueType);
                }
                final Object value = this.valueObjectReader.readObject(jsonReader, this.valueType, fieldName, 0L);
                Object origin;
                if (innerMap != null) {
                    origin = innerMap.put(name, value);
                }
                else {
                    origin = object.put(name, value);
                }
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
            ++index;
        }
        jsonReader.nextIfComma();
        if (this.builder != null) {
            return this.builder.apply(object);
        }
        return object;
    }
}
