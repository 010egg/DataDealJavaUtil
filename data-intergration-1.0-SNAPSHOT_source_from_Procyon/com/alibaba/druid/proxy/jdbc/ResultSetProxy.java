// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Map;
import java.util.List;
import com.alibaba.druid.stat.JdbcSqlStat;
import java.sql.ResultSet;

public interface ResultSetProxy extends ResultSet, WrapperProxy
{
    ResultSet getResultSetRaw();
    
    StatementProxy getStatementProxy();
    
    String getSql();
    
    JdbcSqlStat getSqlStat();
    
    int getCursorIndex();
    
    int getFetchRowCount();
    
    long getConstructNano();
    
    void setConstructNano(final long p0);
    
    void setConstructNano();
    
    int getCloseCount();
    
    void addReadStringLength(final int p0);
    
    long getReadStringLength();
    
    void addReadBytesLength(final int p0);
    
    long getReadBytesLength();
    
    void incrementOpenInputStreamCount();
    
    int getOpenInputStreamCount();
    
    void incrementOpenReaderCount();
    
    int getOpenReaderCount();
    
    int getPhysicalColumn(final int p0);
    
    int getLogicColumn(final int p0);
    
    List<Integer> getHiddenColumns();
    
    int getHiddenColumnCount();
    
    void setLogicColumnMap(final Map<Integer, Integer> p0);
    
    void setPhysicalColumnMap(final Map<Integer, Integer> p0);
    
    void setHiddenColumns(final List<Integer> p0);
}
