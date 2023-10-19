// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.turbo;

import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.Marker;

public class DuplicateMessageFilter extends TurboFilter
{
    public static final int DEFAULT_CACHE_SIZE = 100;
    public static final int DEFAULT_ALLOWED_REPETITIONS = 5;
    public int allowedRepetitions;
    public int cacheSize;
    private LRUMessageCache msgCache;
    
    public DuplicateMessageFilter() {
        this.allowedRepetitions = 5;
        this.cacheSize = 100;
    }
    
    @Override
    public void start() {
        this.msgCache = new LRUMessageCache(this.cacheSize);
        super.start();
    }
    
    @Override
    public void stop() {
        this.msgCache.clear();
        this.msgCache = null;
        super.stop();
    }
    
    @Override
    public FilterReply decide(final Marker marker, final Logger logger, final Level level, final String format, final Object[] params, final Throwable t) {
        final int count = this.msgCache.getMessageCountAndThenIncrement(format);
        if (count <= this.allowedRepetitions) {
            return FilterReply.NEUTRAL;
        }
        return FilterReply.DENY;
    }
    
    public int getAllowedRepetitions() {
        return this.allowedRepetitions;
    }
    
    public void setAllowedRepetitions(final int allowedRepetitions) {
        this.allowedRepetitions = allowedRepetitions;
    }
    
    public int getCacheSize() {
        return this.cacheSize;
    }
    
    public void setCacheSize(final int cacheSize) {
        this.cacheSize = cacheSize;
    }
}
