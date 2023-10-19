// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.util.Properties;

public interface WallProviderStatLogger
{
    void log(final WallProviderStatValue p0);
    
    void configFromProperties(final Properties p0);
}
