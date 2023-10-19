// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

public interface ConfigValue extends ConfigMergeable
{
    ConfigOrigin origin();
    
    ConfigValueType valueType();
    
    Object unwrapped();
    
    String render();
    
    String render(final ConfigRenderOptions p0);
    
    ConfigValue withFallback(final ConfigMergeable p0);
    
    Config atPath(final String p0);
    
    Config atKey(final String p0);
    
    ConfigValue withOrigin(final ConfigOrigin p0);
}
