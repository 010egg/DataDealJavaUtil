// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.logging;

import org.apache.commons.logging.LogFactory;

public class JakartaCommonsLoggingImpl implements Log
{
    private org.apache.commons.logging.Log log;
    private int errorCount;
    private int warnCount;
    private int infoCount;
    private int debugCount;
    
    public JakartaCommonsLoggingImpl(final org.apache.commons.logging.Log log) {
        this.log = log;
    }
    
    public JakartaCommonsLoggingImpl(final String loggerName) {
        this.log = LogFactory.getLog(loggerName);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }
    
    @Override
    public void error(final String s, final Throwable e) {
        this.log.error(s, e);
        ++this.errorCount;
    }
    
    @Override
    public void error(final String s) {
        this.log.error(s);
        ++this.errorCount;
    }
    
    @Override
    public void debug(final String s) {
        ++this.debugCount;
        this.log.debug(s);
    }
    
    @Override
    public void debug(final String s, final Throwable e) {
        ++this.debugCount;
        this.log.debug(s, e);
    }
    
    @Override
    public void warn(final String s) {
        this.log.warn(s);
        ++this.warnCount;
    }
    
    @Override
    public void warn(final String s, final Throwable e) {
        this.log.warn(s, e);
        ++this.warnCount;
    }
    
    @Override
    public int getWarnCount() {
        return this.warnCount;
    }
    
    @Override
    public int getErrorCount() {
        return this.errorCount;
    }
    
    @Override
    public void resetStat() {
        this.errorCount = 0;
        this.warnCount = 0;
        this.infoCount = 0;
        ++this.debugCount;
    }
    
    @Override
    public boolean isInfoEnabled() {
        return this.log.isInfoEnabled();
    }
    
    @Override
    public void info(final String msg) {
        this.log.info(msg);
        ++this.infoCount;
    }
    
    @Override
    public int getInfoCount() {
        return this.infoCount;
    }
    
    @Override
    public boolean isWarnEnabled() {
        return this.log.isWarnEnabled();
    }
    
    @Override
    public int getDebugCount() {
        return this.debugCount;
    }
    
    @Override
    public boolean isErrorEnabled() {
        return this.log.isErrorEnabled();
    }
}
