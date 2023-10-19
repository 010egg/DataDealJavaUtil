// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.turbo;

import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.Marker;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.ContextAwareBase;

public abstract class TurboFilter extends ContextAwareBase implements LifeCycle
{
    private String name;
    boolean start;
    
    public TurboFilter() {
        this.start = false;
    }
    
    public abstract FilterReply decide(final Marker p0, final Logger p1, final Level p2, final String p3, final Object[] p4, final Throwable p5);
    
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
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
