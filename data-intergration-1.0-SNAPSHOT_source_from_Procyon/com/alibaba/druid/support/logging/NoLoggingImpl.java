// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.logging;

public class NoLoggingImpl implements Log
{
    private int infoCount;
    private int errorCount;
    private int warnCount;
    private int debugCount;
    private String loggerName;
    private boolean debugEnable;
    private boolean infoEnable;
    private boolean warnEnable;
    private boolean errorEnable;
    
    public NoLoggingImpl(final String loggerName) {
        this.debugEnable = false;
        this.infoEnable = true;
        this.warnEnable = true;
        this.errorEnable = true;
        this.loggerName = loggerName;
    }
    
    public String getLoggerName() {
        return this.loggerName;
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.debugEnable;
    }
    
    @Override
    public void error(final String s, final Throwable e) {
        if (!this.errorEnable) {
            return;
        }
        this.error(s);
        if (e != null) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void error(final String s) {
        ++this.errorCount;
        if (s != null) {
            System.err.println(this.loggerName + " : " + s);
        }
    }
    
    @Override
    public void debug(final String s) {
        ++this.debugCount;
    }
    
    @Override
    public void debug(final String s, final Throwable e) {
        ++this.debugCount;
    }
    
    @Override
    public void warn(final String s) {
        ++this.warnCount;
    }
    
    @Override
    public void warn(final String s, final Throwable e) {
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
    public void resetStat() {
        this.errorCount = 0;
        this.warnCount = 0;
        this.infoCount = 0;
        this.debugCount = 0;
    }
    
    @Override
    public boolean isInfoEnabled() {
        return this.infoEnable;
    }
    
    @Override
    public void info(final String s) {
        ++this.infoCount;
    }
    
    @Override
    public boolean isWarnEnabled() {
        return this.warnEnable;
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
    public boolean isErrorEnabled() {
        return this.errorEnable;
    }
    
    public void setErrorEnabled(final boolean value) {
        this.errorEnable = value;
    }
}
