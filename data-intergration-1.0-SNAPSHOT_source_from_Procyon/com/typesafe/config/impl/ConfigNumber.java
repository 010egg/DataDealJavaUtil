// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.io.ObjectStreamException;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigOrigin;
import java.io.Serializable;

abstract class ConfigNumber extends AbstractConfigValue implements Serializable
{
    private static final long serialVersionUID = 2L;
    protected final String originalText;
    
    protected ConfigNumber(final ConfigOrigin origin, final String originalText) {
        super(origin);
        this.originalText = originalText;
    }
    
    @Override
    public abstract Number unwrapped();
    
    @Override
    String transformToString() {
        return this.originalText;
    }
    
    int intValueRangeChecked(final String path) {
        final long l = this.longValue();
        if (l < -2147483648L || l > 2147483647L) {
            throw new ConfigException.WrongType(this.origin(), path, "32-bit integer", "out-of-range value " + l);
        }
        return (int)l;
    }
    
    protected abstract long longValue();
    
    protected abstract double doubleValue();
    
    private boolean isWhole() {
        final long asLong = this.longValue();
        return asLong == this.doubleValue();
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof ConfigNumber;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ConfigNumber) || !this.canEqual(other)) {
            return false;
        }
        final ConfigNumber n = (ConfigNumber)other;
        if (this.isWhole()) {
            return n.isWhole() && this.longValue() == n.longValue();
        }
        return !n.isWhole() && this.doubleValue() == n.doubleValue();
    }
    
    @Override
    public int hashCode() {
        long asLong;
        if (this.isWhole()) {
            asLong = this.longValue();
        }
        else {
            asLong = Double.doubleToLongBits(this.doubleValue());
        }
        return (int)(asLong ^ asLong >>> 32);
    }
    
    static ConfigNumber newNumber(final ConfigOrigin origin, final long number, final String originalText) {
        if (number <= 2147483647L && number >= -2147483648L) {
            return new ConfigInt(origin, (int)number, originalText);
        }
        return new ConfigLong(origin, number, originalText);
    }
    
    static ConfigNumber newNumber(final ConfigOrigin origin, final double number, final String originalText) {
        final long asLong = (long)number;
        if (asLong == number) {
            return newNumber(origin, asLong, originalText);
        }
        return new ConfigDouble(origin, number, originalText);
    }
    
    private Object writeReplace() throws ObjectStreamException {
        return new SerializedConfigValue(this);
    }
}
