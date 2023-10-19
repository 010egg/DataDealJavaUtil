// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

import java.util.List;

public interface ConfigList extends List<ConfigValue>, ConfigValue
{
    List<Object> unwrapped();
    
    ConfigList withOrigin(final ConfigOrigin p0);
}
