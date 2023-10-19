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

abstract class ConfigString extends AbstractConfigValue implements Serializable
{
    private static final long serialVersionUID = 2L;
    protected final String value;
    
    protected ConfigString(final ConfigOrigin origin, final String value) {
        super(origin);
        this.value = value;
    }
    
    boolean wasQuoted() {
        return this instanceof Quoted;
    }
    
    @Override
    public ConfigValueType valueType() {
        return ConfigValueType.STRING;
    }
    
    @Override
    public String unwrapped() {
        return this.value;
    }
    
    @Override
    String transformToString() {
        return this.value;
    }
    
    @Override
    protected void render(final StringBuilder sb, final int indent, final boolean atRoot, final ConfigRenderOptions options) {
        String rendered;
        if (options.getJson()) {
            rendered = ConfigImplUtil.renderJsonString(this.value);
        }
        else {
            rendered = ConfigImplUtil.renderStringUnquotedIfPossible(this.value);
        }
        sb.append(rendered);
    }
    
    static final class Quoted extends ConfigString
    {
        Quoted(final ConfigOrigin origin, final String value) {
            super(origin, value);
        }
        
        @Override
        protected Quoted newCopy(final ConfigOrigin origin) {
            return new Quoted(origin, this.value);
        }
        
        private Object writeReplace() throws ObjectStreamException {
            return new SerializedConfigValue(this);
        }
    }
    
    static final class Unquoted extends ConfigString
    {
        Unquoted(final ConfigOrigin origin, final String value) {
            super(origin, value);
        }
        
        @Override
        protected Unquoted newCopy(final ConfigOrigin origin) {
            return new Unquoted(origin, this.value);
        }
        
        private Object writeReplace() throws ObjectStreamException {
            return new SerializedConfigValue(this);
        }
    }
}
