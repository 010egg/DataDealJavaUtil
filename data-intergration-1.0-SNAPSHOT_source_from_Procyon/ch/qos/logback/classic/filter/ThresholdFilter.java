// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.filter;

import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;

public class ThresholdFilter extends Filter<ILoggingEvent>
{
    Level level;
    
    @Override
    public FilterReply decide(final ILoggingEvent event) {
        if (!this.isStarted()) {
            return FilterReply.NEUTRAL;
        }
        if (event.getLevel().isGreaterOrEqual(this.level)) {
            return FilterReply.NEUTRAL;
        }
        return FilterReply.DENY;
    }
    
    public void setLevel(final String level) {
        this.level = Level.toLevel(level);
    }
    
    @Override
    public void start() {
        if (this.level != null) {
            super.start();
        }
    }
}
