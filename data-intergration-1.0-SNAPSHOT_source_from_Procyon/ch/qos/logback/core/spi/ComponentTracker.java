// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.spi;

import java.util.Set;
import java.util.Collection;

public interface ComponentTracker<C>
{
    public static final int DEFAULT_TIMEOUT = 1800000;
    public static final int DEFAULT_MAX_COMPONENTS = Integer.MAX_VALUE;
    
    int getComponentCount();
    
    C find(final String p0);
    
    C getOrCreate(final String p0, final long p1);
    
    void removeStaleComponents(final long p0);
    
    void endOfLife(final String p0);
    
    Collection<C> allComponents();
    
    Set<String> allKeys();
}
