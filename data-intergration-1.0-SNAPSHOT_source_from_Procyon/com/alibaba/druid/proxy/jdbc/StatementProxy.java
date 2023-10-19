// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Map;
import com.alibaba.druid.stat.JdbcSqlStat;
import java.util.List;
import java.sql.Statement;

public interface StatementProxy extends Statement, WrapperProxy
{
    ConnectionProxy getConnectionProxy();
    
    Statement getRawObject();
    
    List<String> getBatchSqlList();
    
    String getBatchSql();
    
    JdbcSqlStat getSqlStat();
    
    StatementExecuteType getLastExecuteType();
    
    void setSqlStat(final JdbcSqlStat p0);
    
    String getLastExecuteSql();
    
    long getLastExecuteStartNano();
    
    void setLastExecuteStartNano(final long p0);
    
    void setLastExecuteStartNano();
    
    long getLastExecuteTimeNano();
    
    void setLastExecuteTimeNano(final long p0);
    
    void setLastExecuteTimeNano();
    
    Map<Integer, JdbcParameter> getParameters();
    
    int getParametersSize();
    
    JdbcParameter getParameter(final int p0);
    
    boolean isFirstResultSet();
}
