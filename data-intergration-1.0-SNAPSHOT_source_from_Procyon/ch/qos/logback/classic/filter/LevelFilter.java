// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.filter;

import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;

public class LevelFilter extends AbstractMatcherFilter<ILoggingEvent>
{
    Level level;
    
    @Override
    public FilterReply decide(final ILoggingEvent event) {
        if (!this.isStarted()) {
            return FilterReply.NEUTRAL;
        }
        if (event.getLevel().equals(this.level)) {
            return this.onMatch;
        }
        return this.onMismatch;
    }
    
    public void setLevel(final Level level) {
        this.level = level;
    }
    
    @Override
    public void start() {
        if (this.level != null) {
            super.start();
        }
    }
}
