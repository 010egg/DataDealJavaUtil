// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.sql.SQLException;

public interface MockDriverMBean
{
    long getConnectionCloseCount();
    
    int getMajorVersion();
    
    int getMinorVersion();
    
    boolean jdbcCompliant();
    
    boolean acceptsURL(final String p0) throws SQLException;
    
    boolean isLogExecuteQueryEnable();
    
    void setLogExecuteQueryEnable(final boolean p0);
    
    long getIdleTimeCount();
    
    void setIdleTimeCount(final long p0);
    
    void closeAllConnections() throws SQLException;
    
    int getConnectionsSize();
}
