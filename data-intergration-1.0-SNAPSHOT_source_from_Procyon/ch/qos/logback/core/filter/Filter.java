// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.filter;

import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.ContextAwareBase;

public abstract class Filter<E> extends ContextAwareBase implements LifeCycle
{
    private String name;
    boolean start;
    
    public Filter() {
        this.start = false;
    }
    
    @Override
    public void start() {
        this.start = true;
    }
    
    @Override
    public boolean isStarted() {
        return this.start;
    }
    
    @Override
    public void stop() {
        this.start = false;
    }
    
    public abstract FilterReply decide(final E p0);
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
