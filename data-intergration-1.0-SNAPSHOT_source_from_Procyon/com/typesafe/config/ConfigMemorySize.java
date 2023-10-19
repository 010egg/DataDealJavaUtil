// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

public final class ConfigMemorySize
{
    private final long bytes;
    
    private ConfigMemorySize(final long bytes) {
        if (bytes < 0L) {
            throw new IllegalArgumentException("Attempt to construct ConfigMemorySize with negative number: " + bytes);
        }
        this.bytes = bytes;
    }
    
    public static ConfigMemorySize ofBytes(final long bytes) {
        return new ConfigMemorySize(bytes);
    }
    
    public long toBytes() {
        return this.bytes;
    }
    
    @Override
    public String toString() {
        return "ConfigMemorySize(" + this.bytes + ")";
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof ConfigMemorySize && ((ConfigMemorySize)other).bytes == this.bytes;
    }
    
    @Override
    public int hashCode() {
        return Long.valueOf(this.bytes).hashCode();
    }
}
