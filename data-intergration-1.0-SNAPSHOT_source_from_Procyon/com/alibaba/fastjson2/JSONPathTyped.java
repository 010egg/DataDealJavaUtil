// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Type;

class JSONPathTyped extends JSONPath
{
    final JSONPath jsonPath;
    final Type type;
    
    protected JSONPathTyped(final JSONPath jsonPath, final Type type) {
        super(jsonPath.path, jsonPath.features);
        this.type = type;
        this.jsonPath = jsonPath;
    }
    
    @Override
    public JSONPath getParent() {
        return this.jsonPath.getParent();
    }
    
    @Override
    public boolean isRef() {
        return this.jsonPath.isRef();
    }
    
    @Override
    public boolean contains(final Object object) {
        return this.jsonPath.contains(object);
    }
    
    @Override
    public Object eval(final Object object) {
        final Object result = this.jsonPath.eval(object);
        return TypeUtils.cast(result, this.type);
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        final Object result = this.jsonPath.extract(jsonReader);
        return TypeUtils.cast(result, this.type);
    }
    
    @Override
    public String extractScalar(final JSONReader jsonReader) {
        return this.jsonPath.extractScalar(jsonReader);
    }
    
    @Override
    public void set(final Object object, final Object value) {
        this.jsonPath.set(object, value);
    }
    
    @Override
    public void set(final Object object, final Object value, final JSONReader.Feature... readerFeatures) {
        this.jsonPath.set(object, value, readerFeatures);
    }
    
    @Override
    public void setCallback(final Object object, final BiFunction callback) {
        this.jsonPath.setCallback(object, callback);
    }
    
    @Override
    public void setInt(final Object object, final int value) {
        this.jsonPath.setInt(object, value);
    }
    
    @Override
    public void setLong(final Object object, final long value) {
        this.jsonPath.setLong(object, value);
    }
    
    @Override
    public boolean remove(final Object object) {
        return this.jsonPath.remove(object);
    }
    
    public Type getType() {
        return this.type;
    }
    
    public static JSONPath of(final JSONPath jsonPath, final Type type) {
        if (type == null || type == Object.class) {
            return jsonPath;
        }
        if (jsonPath instanceof JSONPathTyped) {
            final JSONPathTyped jsonPathTyped = (JSONPathTyped)jsonPath;
            if (jsonPathTyped.type.equals(type)) {
                return jsonPath;
            }
        }
        if (jsonPath instanceof JSONPathSingleName) {
            if (type == Integer.class) {
                return new JSONPathSingleNameInteger((JSONPathSingleName)jsonPath);
            }
            if (type == Long.class) {
                return new JSONPathSingleNameLong((JSONPathSingleName)jsonPath);
            }
            if (type == String.class) {
                return new JSONPathSingleNameString((JSONPathSingleName)jsonPath);
            }
            if (type == BigDecimal.class) {
                return new JSONPathSingleNameDecimal((JSONPathSingleName)jsonPath);
            }
        }
        return new JSONPathTyped(jsonPath, type);
    }
}
