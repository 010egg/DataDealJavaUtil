// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.ibatis;

import java.util.Map;
import javax.sql.DataSource;
import com.ibatis.sqlmap.engine.datasource.DataSourceFactory;

public class DruidDataSourceFactory implements DataSourceFactory
{
    private DataSource dataSource;
    
    public void initialize(final Map map) {
        try {
            this.dataSource = com.alibaba.druid.pool.DruidDataSourceFactory.createDataSource(map);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new RuntimeException("init data source error", e2);
        }
    }
    
    public DataSource getDataSource() {
        return this.dataSource;
    }
}
