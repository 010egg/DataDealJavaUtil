// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

public interface ConfigParseable
{
    ConfigObject parse(final ConfigParseOptions p0);
    
    ConfigOrigin origin();
    
    ConfigParseOptions options();
}
