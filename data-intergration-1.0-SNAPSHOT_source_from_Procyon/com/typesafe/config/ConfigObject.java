// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

import java.util.Map;

public interface ConfigObject extends ConfigValue, Map<String, ConfigValue>
{
    Config toConfig();
    
    Map<String, Object> unwrapped();
    
    ConfigObject withFallback(final ConfigMergeable p0);
    
    ConfigValue get(final Object p0);
    
    ConfigObject withOnlyKey(final String p0);
    
    ConfigObject withoutKey(final String p0);
    
    ConfigObject withValue(final String p0, final ConfigValue p1);
    
    ConfigObject withOrigin(final ConfigOrigin p0);
}
