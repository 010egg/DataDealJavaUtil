// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.quartz;

import java.sql.SQLException;
import org.quartz.utils.ConnectionProvider;
import com.alibaba.druid.pool.DruidDataSource;

public class DruidQuartzConnectionProvider extends DruidDataSource implements ConnectionProvider
{
    private static final long serialVersionUID = 1L;
    
    public void initialize() throws SQLException {
        this.init();
    }
    
    public void shutdown() throws SQLException {
        this.close();
    }
}
