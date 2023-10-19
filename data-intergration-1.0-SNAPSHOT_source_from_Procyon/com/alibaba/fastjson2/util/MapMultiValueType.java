// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import com.alibaba.fastjson2.JSONObject;
import java.util.HashMap;
import java.lang.reflect.Type;
import java.util.Map;

public class MapMultiValueType<T extends Map> implements Type
{
    private final Class<T> mapType;
    private final Map<String, Type> valueTypes;
    
    MapMultiValueType(final Class<T> mapType, final String name, final Type type) {
        this.valueTypes = new HashMap<String, Type>();
        this.mapType = mapType;
        this.valueTypes.put(name, type);
    }
    
    MapMultiValueType(final Class<T> mapType, final Map<String, Type> types) {
        this.valueTypes = new HashMap<String, Type>();
        this.mapType = mapType;
        this.valueTypes.putAll(types);
    }
    
    public Class<T> getMapType() {
        return this.mapType;
    }
    
    public Type getType(final String name) {
        return this.valueTypes.get(name);
    }
    
    public static MapMultiValueType<JSONObject> of(final String name, final Type type) {
        return new MapMultiValueType<JSONObject>(JSONObject.class, name, type);
    }
    
    public static MapMultiValueType<JSONObject> of(final Map<String, Type> types) {
        return new MapMultiValueType<JSONObject>(JSONObject.class, types);
    }
    
    public static <T extends Map> MapMultiValueType<T> of(final Class<T> mapType, final String name, final Type type) {
        return new MapMultiValueType<T>(mapType, name, type);
    }
    
    public static <T extends Map> MapMultiValueType<T> of(final Class<T> mapType, final Map<String, Type> types) {
        return new MapMultiValueType<T>(mapType, types);
    }
}
