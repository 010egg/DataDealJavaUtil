// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.logging;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class CommonsLogFilter extends LogFilter
{
    private Log dataSourceLogger;
    private Log connectionLogger;
    private Log statementLogger;
    private Log resultSetLogger;
    
    public CommonsLogFilter() {
        this.dataSourceLogger = LogFactory.getLog(this.dataSourceLoggerName);
        this.connectionLogger = LogFactory.getLog(this.connectionLoggerName);
        this.statementLogger = LogFactory.getLog(this.statementLoggerName);
        this.resultSetLogger = LogFactory.getLog(this.resultSetLoggerName);
    }
    
    public void setDataSourceLogger(final Log dataSourceLogger) {
        this.dataSourceLogger = dataSourceLogger;
        if (dataSourceLogger instanceof Log4JLogger) {
            this.dataSourceLoggerName = ((Log4JLogger)dataSourceLogger).getLogger().getName();
        }
    }
    
    @Override
    public String getDataSourceLoggerName() {
        return this.dataSourceLoggerName;
    }
    
    @Override
    public void setDataSourceLoggerName(final String dataSourceLoggerName) {
        this.dataSourceLoggerName = dataSourceLoggerName;
        this.dataSourceLogger = LogFactory.getLog(dataSourceLoggerName);
    }
    
    @Override
    public String getConnectionLoggerName() {
        return this.connectionLoggerName;
    }
    
    @Override
    public void setConnectionLoggerName(final String connectionLoggerName) {
        this.connectionLoggerName = connectionLoggerName;
        this.connectionLogger = LogFactory.getLog(connectionLoggerName);
    }
    
    public void setConnectionLogger(final Log connectionLogger) {
        this.connectionLogger = connectionLogger;
        if (connectionLogger instanceof Log4JLogger) {
            this.connectionLoggerName = ((Log4JLogger)connectionLogger).getLogger().getName();
        }
    }
    
    @Override
    public String getStatementLoggerName() {
        return this.statementLoggerName;
    }
    
    @Override
    public void setStatementLoggerName(final String statementLoggerName) {
        this.statementLoggerName = statementLoggerName;
        this.statementLogger = LogFactory.getLog(statementLoggerName);
    }
    
    public void setStatementLogger(final Log statementLogger) {
        this.statementLogger = statementLogger;
        if (statementLogger instanceof Log4JLogger) {
            this.statementLoggerName = ((Log4JLogger)statementLogger).getLogger().getName();
        }
    }
    
    @Override
    public String getResultSetLoggerName() {
        return this.resultSetLoggerName;
    }
    
    @Override
    public void setResultSetLoggerName(final String resultSetLoggerName) {
        this.resultSetLoggerName = resultSetLoggerName;
        this.resultSetLogger = LogFactory.getLog(resultSetLoggerName);
    }
    
    public void setResultSetLogger(final Log resultSetLogger) {
        this.resultSetLogger = this.statementLogger;
        if (resultSetLogger instanceof Log4JLogger) {
            this.resultSetLoggerName = ((Log4JLogger)resultSetLogger).getLogger().getName();
        }
    }
    
    @Override
    public boolean isDataSourceLogEnabled() {
        return this.dataSourceLogger.isDebugEnabled() && super.isDataSourceLogEnabled();
    }
    
    @Override
    public boolean isConnectionLogErrorEnabled() {
        return this.connectionLogger.isErrorEnabled() && super.isConnectionLogErrorEnabled();
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
        return this.resultSetLogger.isErrorEnabled() && super.isResultSetLogErrorEnabled();
    }
    
    @Override
    public boolean isStatementLogErrorEnabled() {
        return this.statementLogger.isErrorEnabled() && super.isStatementLogErrorEnabled();
    }
    
    @Override
    protected void connectionLog(final String message) {
        this.connectionLogger.debug(message);
    }
    
    @Override
    protected void statementLog(final String message) {
        this.statementLogger.debug(message);
    }
    
    @Override
    protected void resultSetLog(final String message) {
        this.resultSetLogger.debug(message);
    }
    
    @Override
    protected void resultSetLogError(final String message, final Throwable error) {
        this.resultSetLogger.error(message, error);
    }
    
    @Override
    protected void statementLogError(final String message, final Throwable error) {
        this.statementLogger.error(message, error);
    }
}
