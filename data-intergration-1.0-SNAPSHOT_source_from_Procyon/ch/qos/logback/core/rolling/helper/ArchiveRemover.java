// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling.helper;

import java.util.concurrent.Future;
import java.util.Date;
import ch.qos.logback.core.spi.ContextAware;

public interface ArchiveRemover extends ContextAware
{
    void clean(final Date p0);
    
    void setMaxHistory(final int p0);
    
    void setTotalSizeCap(final long p0);
    
    Future<?> cleanAsynchronously(final Date p0);
}
