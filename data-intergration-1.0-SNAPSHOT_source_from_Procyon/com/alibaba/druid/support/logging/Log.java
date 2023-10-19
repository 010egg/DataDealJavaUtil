// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.logging;

public interface Log
{
    boolean isDebugEnabled();
    
    void error(final String p0, final Throwable p1);
    
    void error(final String p0);
    
    boolean isInfoEnabled();
    
    void info(final String p0);
    
    void debug(final String p0);
    
    void debug(final String p0, final Throwable p1);
    
    boolean isWarnEnabled();
    
    void warn(final String p0);
    
    void warn(final String p0, final Throwable p1);
    
    boolean isErrorEnabled();
    
    int getErrorCount();
    
    int getWarnCount();
    
    int getInfoCount();
    
    int getDebugCount();
    
    void resetStat();
}
