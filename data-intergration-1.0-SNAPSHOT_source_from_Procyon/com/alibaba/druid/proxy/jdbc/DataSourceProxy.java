// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Properties;
import com.alibaba.druid.filter.Filter;
import java.util.List;
import java.sql.Driver;
import com.alibaba.druid.stat.JdbcDataSourceStat;

public interface DataSourceProxy
{
    JdbcDataSourceStat getDataSourceStat();
    
    String getName();
    
    String getDbType();
    
    Driver getRawDriver();
    
    String getUrl();
    
    String getRawJdbcUrl();
    
    List<Filter> getProxyFilters();
    
    long createConnectionId();
    
    long createStatementId();
    
    long createResultSetId();
    
    long createMetaDataId();
    
    long createTransactionId();
    
    Properties getConnectProperties();
}
