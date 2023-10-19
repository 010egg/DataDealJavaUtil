// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.spi;

public interface LifeCycle
{
    void start();
    
    void stop();
    
    boolean isStarted();
}
