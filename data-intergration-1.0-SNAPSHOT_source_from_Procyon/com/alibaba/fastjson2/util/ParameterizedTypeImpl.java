// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import java.util.Objects;
import java.util.Arrays;
import com.alibaba.fastjson2.annotation.JSONCreator;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.annotation.JSONType;
import java.lang.reflect.ParameterizedType;

@JSONType(deserializeFeatures = { JSONReader.Feature.SupportAutoType }, typeName = "java.lang.reflect.ParameterizedType")
public class ParameterizedTypeImpl implements ParameterizedType
{
    private final Type[] actualTypeArguments;
    private final Type ownerType;
    private final Type rawType;
    
    @JSONCreator
    public ParameterizedTypeImpl(final Type[] actualTypeArguments, final Type ownerType, final Type rawType) {
        this.actualTypeArguments = actualTypeArguments;
        this.ownerType = ownerType;
        this.rawType = rawType;
    }
    
    public ParameterizedTypeImpl(final Type rawType, final Type... actualTypeArguments) {
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
        this.ownerType = null;
    }
    
    @Override
    public Type[] getActualTypeArguments() {
        return this.actualTypeArguments;
    }
    
    @Override
    public Type getOwnerType() {
        return this.ownerType;
    }
    
    @Override
    public Type getRawType() {
        return this.rawType;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ParameterizedTypeImpl that = (ParameterizedTypeImpl)o;
        return Arrays.equals(this.actualTypeArguments, that.actualTypeArguments) && Objects.equals(this.ownerType, that.ownerType) && Objects.equals(this.rawType, that.rawType);
    }
    
    @Override
    public int hashCode() {
        int result = (this.actualTypeArguments != null) ? Arrays.hashCode(this.actualTypeArguments) : 0;
        result = 31 * result + ((this.ownerType != null) ? this.ownerType.hashCode() : 0);
        result = 31 * result + ((this.rawType != null) ? this.rawType.hashCode() : 0);
        return result;
    }
}
