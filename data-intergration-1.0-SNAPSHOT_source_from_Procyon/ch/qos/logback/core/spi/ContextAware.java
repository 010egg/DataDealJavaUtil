// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.spi;

import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.Context;

public interface ContextAware
{
    void setContext(final Context p0);
    
    Context getContext();
    
    void addStatus(final Status p0);
    
    void addInfo(final String p0);
    
    void addInfo(final String p0, final Throwable p1);
    
    void addWarn(final String p0);
    
    void addWarn(final String p0, final Throwable p1);
    
    void addError(final String p0);
    
    void addError(final String p0, final Throwable p1);
}
