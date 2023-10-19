// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling;

import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.spi.LifeCycle;

public interface RollingPolicy extends LifeCycle
{
    void rollover() throws RolloverFailure;
    
    String getActiveFileName();
    
    CompressionMode getCompressionMode();
    
    void setParent(final FileAppender<?> p0);
}
