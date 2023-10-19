// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Jdk14LoggingImpl implements Log
{
    private Logger log;
    private int errorCount;
    private int warnCount;
    private int infoCount;
    private int debugCount;
    private String loggerName;
    
    public Jdk14LoggingImpl(final String loggerName) {
        this.loggerName = loggerName;
        this.log = Logger.getLogger(loggerName);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.log.isLoggable(Level.FINE);
    }
    
    @Override
    public void error(final String s, final Throwable e) {
        this.log.logp(Level.SEVERE, this.loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), s, e);
        ++this.errorCount;
    }
    
    @Override
    public void error(final String s) {
        this.log.logp(Level.SEVERE, this.loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), s);
        ++this.errorCount;
    }
    
    @Override
    public void debug(final String s) {
        ++this.debugCount;
        this.log.logp(Level.FINE, this.loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), s);
    }
    
    @Override
    public void debug(final String s, final Throwable e) {
        ++this.debugCount;
        this.log.logp(Level.FINE, this.loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), s, e);
    }
    
    @Override
    public void warn(final String s) {
        this.log.logp(Level.WARNING, this.loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), s);
        ++this.warnCount;
    }
    
    @Override
    public void warn(final String s, final Throwable e) {
        this.log.logp(Level.WARNING, this.loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), s, e);
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
    public boolean isInfoEnabled() {
        return this.log.isLoggable(Level.INFO);
    }
    
    @Override
    public void info(final String msg) {
        this.log.logp(Level.INFO, this.loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), msg);
        ++this.infoCount;
    }
    
    @Override
    public int getInfoCount() {
        return this.infoCount;
    }
    
    @Override
    public boolean isWarnEnabled() {
        return this.log.isLoggable(Level.WARNING);
    }
    
    @Override
    public int getDebugCount() {
        return this.debugCount;
    }
    
    @Override
    public boolean isErrorEnabled() {
        return this.log.isLoggable(Level.SEVERE);
    }
}
