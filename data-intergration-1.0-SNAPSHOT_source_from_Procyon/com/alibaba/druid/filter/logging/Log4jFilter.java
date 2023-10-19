// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.logging;

import org.apache.log4j.Priority;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log4jFilter extends LogFilter implements Log4jFilterMBean
{
    private Logger dataSourceLogger;
    private Logger connectionLogger;
    private Logger statementLogger;
    private Logger resultSetLogger;
    
    public Log4jFilter() {
        this.dataSourceLogger = Logger.getLogger(this.dataSourceLoggerName);
        this.connectionLogger = Logger.getLogger(this.connectionLoggerName);
        this.statementLogger = Logger.getLogger(this.statementLoggerName);
        this.resultSetLogger = Logger.getLogger(this.resultSetLoggerName);
    }
    
    @Override
    public String getDataSourceLoggerName() {
        return this.dataSourceLoggerName;
    }
    
    @Override
    public void setDataSourceLoggerName(final String dataSourceLoggerName) {
        this.dataSourceLoggerName = dataSourceLoggerName;
        this.dataSourceLogger = Logger.getLogger(dataSourceLoggerName);
    }
    
    public void setDataSourceLogger(final Logger dataSourceLogger) {
        this.dataSourceLogger = dataSourceLogger;
        this.dataSourceLoggerName = dataSourceLogger.getName();
    }
    
    @Override
    public String getConnectionLoggerName() {
        return this.connectionLoggerName;
    }
    
    @Override
    public void setConnectionLoggerName(final String connectionLoggerName) {
        this.connectionLoggerName = connectionLoggerName;
        this.connectionLogger = Logger.getLogger(connectionLoggerName);
    }
    
    public void setConnectionLogger(final Logger connectionLogger) {
        this.connectionLogger = connectionLogger;
        this.connectionLoggerName = connectionLogger.getName();
    }
    
    @Override
    public String getStatementLoggerName() {
        return this.statementLoggerName;
    }
    
    @Override
    public void setStatementLoggerName(final String statementLoggerName) {
        this.statementLoggerName = statementLoggerName;
        this.statementLogger = Logger.getLogger(statementLoggerName);
    }
    
    public void setStatementLogger(final Logger statementLogger) {
        this.statementLogger = statementLogger;
        this.statementLoggerName = statementLogger.getName();
    }
    
    @Override
    public String getResultSetLoggerName() {
        return this.resultSetLoggerName;
    }
    
    @Override
    public void setResultSetLoggerName(final String resultSetLoggerName) {
        this.resultSetLoggerName = resultSetLoggerName;
        this.resultSetLogger = Logger.getLogger(resultSetLoggerName);
    }
    
    public void setResultSetLogger(final Logger resultSetLogger) {
        this.resultSetLogger = resultSetLogger;
        this.resultSetLoggerName = resultSetLogger.getName();
    }
    
    @Override
    public boolean isConnectionLogErrorEnabled() {
        return this.connectionLogger.isEnabledFor((Priority)Level.ERROR) && super.isConnectionLogErrorEnabled();
    }
    
    @Override
    public boolean isDataSourceLogEnabled() {
        return this.dataSourceLogger.isDebugEnabled() && super.isDataSourceLogEnabled();
    }
    
    @Override
    public boolean isConnectionLogEnabled() {
        return this.connectionLogger.isDebugEnabled() && super.isConnectionLogEnabled();
    }
    
    @Override
    public boolean isStatementLogEnabled() {
        return this.statementLogger.isDebugEnabled() && super.isStatementLogEnabled();
    }
    
    @Override
    public boolean isResultSetLogEnabled() {
        return this.resultSetLogger.isDebugEnabled() && super.isResultSetLogEnabled();
    }
    
    @Override
    public boolean isResultSetLogErrorEnabled() {
        return this.resultSetLogger.isEnabledFor((Priority)Level.ERROR) && super.isResultSetLogErrorEnabled();
    }
    
    @Override
    public boolean isStatementLogErrorEnabled() {
        return this.statementLogger.isEnabledFor((Priority)Level.ERROR) && super.isStatementLogErrorEnabled();
    }
    
    @Override
    protected void connectionLog(final String message) {
        this.connectionLogger.debug((Object)message);
    }
    
    @Override
    protected void statementLog(final String message) {
        this.statementLogger.debug((Object)message);
    }
    
    @Override
    protected void resultSetLog(final String message) {
        this.resultSetLogger.debug((Object)message);
    }
    
    @Override
    protected void resultSetLogError(final String message, final Throwable error) {
        this.resultSetLogger.error((Object)message, error);
    }
    
    @Override
    protected void statementLogError(final String message, final Throwable error) {
        this.statementLogger.error((Object)message, error);
    }
}
