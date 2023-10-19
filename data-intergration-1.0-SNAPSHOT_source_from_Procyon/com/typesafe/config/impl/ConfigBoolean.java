// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.io.ObjectStreamException;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;
import com.typesafe.config.ConfigOrigin;
import java.io.Serializable;

final class ConfigBoolean extends AbstractConfigValue implements Serializable
{
    private static final long serialVersionUID = 2L;
    private final boolean value;
    
    ConfigBoolean(final ConfigOrigin origin, final boolean value) {
        super(origin);
        this.value = value;
    }
    
    @Override
    public ConfigValueType valueType() {
        return ConfigValueType.BOOLEAN;
    }
    
    @Override
    public Boolean unwrapped() {
        return this.value;
    }
    
    @Override
    String transformToString() {
        return this.value ? "true" : "false";
    }
    
    @Override
    protected ConfigBoolean newCopy(final ConfigOrigin origin) {
        return new ConfigBoolean(origin, this.value);
    }
    
    private Object writeReplace() throws ObjectStreamException {
        return new SerializedConfigValue(this);
    }
}
