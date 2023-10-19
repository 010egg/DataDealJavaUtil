// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.io.ObjectStreamException;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;
import com.typesafe.config.ConfigOrigin;
import java.io.Serializable;

final class ConfigInt extends ConfigNumber implements Serializable
{
    private static final long serialVersionUID = 2L;
    private final int value;
    
    ConfigInt(final ConfigOrigin origin, final int value, final String originalText) {
        super(origin, originalText);
        this.value = value;
    }
    
    @Override
    public ConfigValueType valueType() {
        return ConfigValueType.NUMBER;
    }
    
    @Override
    public Integer unwrapped() {
        return this.value;
    }
    
    @Override
    String transformToString() {
        final String s = super.transformToString();
        if (s == null) {
            return Integer.toString(this.value);
        }
        return s;
    }
    
    @Override
    protected long longValue() {
        return this.value;
    }
    
    @Override
    protected double doubleValue() {
        return this.value;
    }
    
    @Override
    protected ConfigInt newCopy(final ConfigOrigin origin) {
        return new ConfigInt(origin, this.value, this.originalText);
    }
    
    private Object writeReplace() throws ObjectStreamException {
        return new SerializedConfigValue(this);
    }
}
