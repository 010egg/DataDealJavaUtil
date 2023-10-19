// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public interface LoggerContextListener
{
    boolean isResetResistant();
    
    void onStart(final LoggerContext p0);
    
    void onReset(final LoggerContext p0);
    
    void onStop(final LoggerContext p0);
    
    void onLevelChange(final Logger p0, final Level p1);
}
