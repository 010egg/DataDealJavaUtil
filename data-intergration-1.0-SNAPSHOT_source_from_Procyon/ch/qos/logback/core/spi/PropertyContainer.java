// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.spi;

import java.util.Map;

public interface PropertyContainer
{
    String getProperty(final String p0);
    
    Map<String, String> getCopyOfPropertyMap();
}
