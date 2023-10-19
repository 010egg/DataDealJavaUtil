// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2Impl implements Log
{
    private Logger log;
    private int errorCount;
    private int warnCount;
    private int infoCount;
    private int debugCount;
    
    public Log4j2Impl(final Logger log) {
        this.log = log;
    }
    
    public Log4j2Impl(final String loggerName) {
        this.log = LogManager.getLogger(loggerName);
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
        this.log.error(s, e);
    }
    
    @Override
    public void error(final String s) {
        ++this.errorCount;
        this.log.error(s);
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
        this.log.info(msg);
    }
    
    @Override
    public boolean isWarnEnabled() {
        return this.log.isEnabled(Level.WARN);
    }
    
    @Override
    public boolean isErrorEnabled() {
        return this.log.isErrorEnabled();
    }
    
    @Override
    public int getInfoCount() {
        return this.infoCount;
    }
    
    @Override
    public String toString() {
        return this.log.toString();
    }
}
