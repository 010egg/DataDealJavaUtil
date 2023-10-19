// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.parser;

import com.typesafe.config.ConfigValue;

public interface ConfigDocument
{
    ConfigDocument withValueText(final String p0, final String p1);
    
    ConfigDocument withValue(final String p0, final ConfigValue p1);
    
    ConfigDocument withoutPath(final String p0);
    
    boolean hasPath(final String p0);
    
    String render();
}
