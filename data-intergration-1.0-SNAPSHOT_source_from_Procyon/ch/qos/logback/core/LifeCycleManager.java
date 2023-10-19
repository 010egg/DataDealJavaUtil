// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core;

import java.util.Iterator;
import java.util.HashSet;
import ch.qos.logback.core.spi.LifeCycle;
import java.util.Set;

public class LifeCycleManager
{
    private final Set<LifeCycle> components;
    
    public LifeCycleManager() {
        this.components = new HashSet<LifeCycle>();
    }
    
    public void register(final LifeCycle component) {
        this.components.add(component);
    }
    
    public void reset() {
        for (final LifeCycle component : this.components) {
            if (component.isStarted()) {
                component.stop();
            }
        }
        this.components.clear();
    }
}
