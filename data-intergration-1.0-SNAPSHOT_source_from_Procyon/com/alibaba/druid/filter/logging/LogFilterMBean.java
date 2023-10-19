// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.logging;

import com.alibaba.druid.sql.SQLUtils;

public interface LogFilterMBean
{
    String getDataSourceLoggerName();
    
    void setDataSourceLoggerName(final String p0);
    
    boolean isDataSourceLogEnabled();
    
    void setDataSourceLogEnabled(final boolean p0);
    
    String getConnectionLoggerName();
    
    void setConnectionLoggerName(final String p0);
    
    boolean isConnectionLogEnabled();
    
    void setConnectionLogEnabled(final boolean p0);
    
    boolean isConnectionLogErrorEnabled();
    
    void setConnectionLogErrorEnabled(final boolean p0);
    
    boolean isConnectionConnectBeforeLogEnabled();
    
    void setConnectionConnectBeforeLogEnabled(final boolean p0);
    
    boolean isConnectionConnectAfterLogEnabled();
    
    void setConnectionConnectAfterLogEnabled(final boolean p0);
    
    boolean isConnectionCloseAfterLogEnabled();
    
    void setConnectionCloseAfterLogEnabled(final boolean p0);
    
    boolean isConnectionCommitAfterLogEnabled();
    
    void setConnectionCommitAfterLogEnabled(final boolean p0);
    
    String getStatementLoggerName();
    
    void setStatementLoggerName(final String p0);
    
    boolean isStatementLogEnabled();
    
    void setStatementLogEnabled(final boolean p0);
    
    boolean isStatementCloseAfterLogEnabled();
    
    void setStatementCloseAfterLogEnabled(final boolean p0);
    
    boolean isStatementCreateAfterLogEnabled();
    
    void setStatementCreateAfterLogEnabled(final boolean p0);
    
    boolean isStatementExecuteBatchAfterLogEnabled();
    
    void setStatementExecuteBatchAfterLogEnabled(final boolean p0);
    
    boolean isStatementExecuteAfterLogEnabled();
    
    void setStatementExecuteAfterLogEnabled(final boolean p0);
    
    boolean isStatementExecuteQueryAfterLogEnabled();
    
    void setStatementExecuteQueryAfterLogEnabled(final boolean p0);
    
    boolean isStatementExecuteUpdateAfterLogEnabled();
    
    void setStatementExecuteUpdateAfterLogEnabled(final boolean p0);
    
    boolean isStatementPrepareCallAfterLogEnabled();
    
    void setStatementPrepareCallAfterLogEnabled(final boolean p0);
    
    boolean isStatementPrepareAfterLogEnabled();
    
    void setStatementPrepareAfterLogEnabled(final boolean p0);
    
    boolean isStatementLogErrorEnabled();
    
    void setStatementLogErrorEnabled(final boolean p0);
    
    boolean isStatementParameterSetLogEnabled();
    
    void setStatementParameterSetLogEnabled(final boolean p0);
    
    String getResultSetLoggerName();
    
    void setResultSetLoggerName(final String p0);
    
    boolean isResultSetLogEnabled();
    
    void setResultSetLogEnabled(final boolean p0);
    
    boolean isResultSetNextAfterLogEnabled();
    
    void setResultSetNextAfterLogEnabled(final boolean p0);
    
    boolean isResultSetOpenAfterLogEnabled();
    
    void setResultSetOpenAfterLogEnabled(final boolean p0);
    
    boolean isResultSetLogErrorEnabled();
    
    void setResultSetLogErrorEnabled(final boolean p0);
    
    boolean isResultSetCloseAfterLogEnabled();
    
    void setResultSetCloseAfterLogEnabled(final boolean p0);
    
    SQLUtils.FormatOption getStatementSqlFormatOption();
    
    void setStatementSqlFormatOption(final SQLUtils.FormatOption p0);
    
    boolean isStatementSqlPrettyFormat();
    
    void setStatementSqlPrettyFormat(final boolean p0);
}
