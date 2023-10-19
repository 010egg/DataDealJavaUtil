// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.logging;

import org.apache.log4j.Priority;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log4jImpl implements Log
{
    private static final String callerFQCN;
    private Logger log;
    private int errorCount;
    private int warnCount;
    private int infoCount;
    private int debugCount;
    
    public Log4jImpl(final Logger log) {
        this.log = log;
    }
    
    public Log4jImpl(final String loggerName) {
        this.log = Logger.getLogger(loggerName);
    }
    
    public Logger getLog() {
        return this.log;
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }
    
    @Override
    public void error(final String s, final Throwable e) {
        ++this.errorCount;
        this.log.log(Log4jImpl.callerFQCN, (Priority)Level.ERROR, (Object)s, e);
    }
    
    @Override
    public void error(final String s) {
        ++this.errorCount;
        this.log.log(Log4jImpl.callerFQCN, (Priority)Level.ERROR, (Object)s, (Throwable)null);
    }
    
    @Override
    public void debug(final String s) {
        ++this.debugCount;
        this.log.log(Log4jImpl.callerFQCN, (Priority)Level.DEBUG, (Object)s, (Throwable)null);
    }
    
    @Override
    public void debug(final String s, final Throwable e) {
        ++this.debugCount;
        this.log.log(Log4jImpl.callerFQCN, (Priority)Level.DEBUG, (Object)s, e);
    }
    
    @Override
    public void warn(final String s) {
        this.log.log(Log4jImpl.callerFQCN, (Priority)Level.WARN, (Object)s, (Throwable)null);
        ++this.warnCount;
    }
    
    @Override
    public void warn(final String s, final Throwable e) {
        this.log.log(Log4jImpl.callerFQCN, (Priority)Level.WARN, (Object)s, e);
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
        this.debugCount = 0;
    }
    
    @Override
    public int getDebugCount() {
        return this.debugCount;
    }
    
    @Override
    public boolean isInfoEnabled() {
        return this.log.isInfoEnabled();
    }
    
    @Override
    public void info(final String msg) {
        ++this.infoCount;
        this.log.log(Log4jImpl.callerFQCN, (Priority)Level.INFO, (Object)msg, (Throwable)null);
    }
    
    @Override
    public boolean isWarnEnabled() {
        return this.log.isEnabledFor((Priority)Level.WARN);
    }
    
    @Override
    public boolean isErrorEnabled() {
        return this.log.isEnabledFor((Priority)Level.ERROR);
    }
    
    @Override
    public int getInfoCount() {
        return this.infoCount;
    }
    
    @Override
    public String toString() {
        return this.log.toString();
    }
    
    static {
        callerFQCN = Log4jImpl.class.getName();
    }
}
