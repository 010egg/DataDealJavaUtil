// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling;

import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.spi.ContextAware;

public interface TimeBasedFileNamingAndTriggeringPolicy<E> extends TriggeringPolicy<E>, ContextAware
{
    void setTimeBasedRollingPolicy(final TimeBasedRollingPolicy<E> p0);
    
    String getElapsedPeriodsFileName();
    
    String getCurrentPeriodsFileNameWithoutCompressionSuffix();
    
    ArchiveRemover getArchiveRemover();
    
    long getCurrentTime();
    
    void setCurrentTime(final long p0);
}
