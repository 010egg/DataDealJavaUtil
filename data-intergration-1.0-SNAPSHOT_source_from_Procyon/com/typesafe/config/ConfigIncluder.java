// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

public interface ConfigIncluder
{
    ConfigIncluder withFallback(final ConfigIncluder p0);
    
    ConfigObject include(final ConfigIncludeContext p0, final String p1);
}
