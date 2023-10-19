// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.logging;

import org.slf4j.Marker;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;
import org.slf4j.Logger;

public class SLF4JImpl implements Log
{
    private static final String callerFQCN;
    private static final Logger testLogger;
    private int errorCount;
    private int warnCount;
    private int infoCount;
    private int debugCount;
    private LocationAwareLogger log;
    
    public SLF4JImpl(final LocationAwareLogger log) {
        this.log = log;
    }
    
    public SLF4JImpl(final String loggerName) {
        this.log = (LocationAwareLogger)LoggerFactory.getLogger(loggerName);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }
    
    @Override
    public void error(final String msg, final Throwable e) {
        this.log.log(null, SLF4JImpl.callerFQCN, 40, msg, null, e);
        ++this.errorCount;
    }
    
    @Override
    public void error(final String msg) {
        this.log.log(null, SLF4JImpl.callerFQCN, 40, msg, null, null);
        ++this.errorCount;
    }
    
    @Override
    public boolean isInfoEnabled() {
        return this.log.isInfoEnabled();
    }
    
    @Override
    public void info(final String msg) {
        ++this.infoCount;
        this.log.log(null, SLF4JImpl.callerFQCN, 20, msg, null, null);
    }
    
    @Override
    public void debug(final String msg) {
        ++this.debugCount;
        this.log.log(null, SLF4JImpl.callerFQCN, 10, msg, null, null);
    }
    
    @Override
    public void debug(final String msg, final Throwable e) {
        ++this.debugCount;
        this.log.log(null, SLF4JImpl.callerFQCN, 40, msg, null, e);
    }
    
    @Override
    public boolean isWarnEnabled() {
        return this.log.isWarnEnabled();
    }
    
    @Override
    public boolean isErrorEnabled() {
        return this.log.isErrorEnabled();
    }
    
    @Override
    public void warn(final String msg) {
        this.log.log(null, SLF4JImpl.callerFQCN, 30, msg, null, null);
        ++this.warnCount;
    }
    
    @Override
    public void warn(final String msg, final Throwable e) {
        this.log.log(null, SLF4JImpl.callerFQCN, 30, msg, null, e);
        ++this.warnCount;
    }
    
    @Override
    public int getErrorCount() {
        return this.errorCount;
    }
    
    @Override
    public int getWarnCount() {
        return this.warnCount;
    }
    
    @Override
    public int getInfoCount() {
        return this.infoCount;
    }
    
    @Override
    public int getDebugCount() {
        return this.debugCount;
    }
    
    @Override
    public void resetStat() {
        this.errorCount = 0;
        this.warnCount = 0;
        this.infoCount = 0;
        this.debugCount = 0;
    }
    
    static {
        callerFQCN = SLF4JImpl.class.getName();
        testLogger = LoggerFactory.getLogger(SLF4JImpl.class);
        if (!(SLF4JImpl.testLogger instanceof LocationAwareLogger)) {
            throw new UnsupportedOperationException(SLF4JImpl.testLogger.getClass() + " is not a suitable logger");
        }
    }
}
