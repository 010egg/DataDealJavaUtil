// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.monitor.dao;

import java.util.Date;
import java.sql.SQLException;
import com.alibaba.druid.stat.JdbcSqlStatValue;
import java.util.Map;
import com.alibaba.druid.support.http.stat.WebAppStatValue;
import com.alibaba.druid.support.http.stat.WebURIStatValue;
import com.alibaba.druid.support.spring.stat.SpringMethodStatValue;
import com.alibaba.druid.wall.WallProviderStatValue;
import com.alibaba.druid.pool.DruidDataSourceStatValue;
import java.util.List;
import com.alibaba.druid.support.monitor.MonitorContext;

public interface MonitorDao
{
    void saveSql(final MonitorContext p0, final List<DruidDataSourceStatValue> p1);
    
    void saveSqlWall(final MonitorContext p0, final List<WallProviderStatValue> p1);
    
    void saveSpringMethod(final MonitorContext p0, final List<SpringMethodStatValue> p1);
    
    void saveWebURI(final MonitorContext p0, final List<WebURIStatValue> p1);
    
    void saveWebApp(final MonitorContext p0, final List<WebAppStatValue> p1);
    
    List<JdbcSqlStatValue> loadSqlList(final Map<String, Object> p0);
    
    void insertAppIfNotExits(final String p0, final String p1) throws SQLException;
    
    void insertClusterIfNotExits(final String p0, final String p1, final String p2) throws SQLException;
    
    void insertOrUpdateInstance(final String p0, final String p1, final String p2, final String p3, final String p4, final Date p5, final long p6) throws SQLException;
}
