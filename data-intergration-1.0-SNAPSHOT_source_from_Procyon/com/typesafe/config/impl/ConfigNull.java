// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.io.ObjectStreamException;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueType;
import com.typesafe.config.ConfigOrigin;
import java.io.Serializable;

final class ConfigNull extends AbstractConfigValue implements Serializable
{
    private static final long serialVersionUID = 2L;
    
    ConfigNull(final ConfigOrigin origin) {
        super(origin);
    }
    
    @Override
    public ConfigValueType valueType() {
        return ConfigValueType.NULL;
    }
    
    @Override
    public Object unwrapped() {
        return null;
    }
    
    @Override
    String transformToString() {
        return "null";
    }
    
    @Override
    protected void render(final StringBuilder sb, final int indent, final boolean atRoot, final ConfigRenderOptions options) {
        sb.append("null");
    }
    
    @Override
    protected ConfigNull newCopy(final ConfigOrigin origin) {
        return new ConfigNull(origin);
    }
    
    private Object writeReplace() throws ObjectStreamException {
        return new SerializedConfigValue(this);
    }
}
