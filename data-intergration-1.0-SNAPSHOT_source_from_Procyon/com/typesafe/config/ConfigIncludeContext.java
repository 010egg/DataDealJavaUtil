// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

public interface ConfigIncludeContext
{
    ConfigParseable relativeTo(final String p0);
    
    ConfigParseOptions parseOptions();
    
    ConfigIncludeContext setParseOptions(final ConfigParseOptions p0);
}
