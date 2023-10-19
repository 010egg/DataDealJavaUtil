// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.xa;

import javax.transaction.xa.XAResource;
import javax.sql.StatementEventListener;
import javax.sql.ConnectionEventListener;
import java.sql.SQLException;
import java.sql.Connection;
import com.alibaba.druid.pool.DruidPooledConnection;
import javax.sql.XAConnection;

public class DruidPooledXAConnection implements XAConnection
{
    private DruidPooledConnection pooledConnection;
    private XAConnection xaConnection;
    
    public DruidPooledXAConnection(final DruidPooledConnection pooledConnection, final XAConnection xaConnection) {
        this.pooledConnection = pooledConnection;
        this.xaConnection = xaConnection;
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return this.pooledConnection;
    }
    
    @Override
    public void close() throws SQLException {
        this.pooledConnection.close();
    }
    
    @Override
    public void addConnectionEventListener(final ConnectionEventListener listener) {
        this.pooledConnection.addConnectionEventListener(listener);
    }
    
    @Override
    public void removeConnectionEventListener(final ConnectionEventListener listener) {
        this.pooledConnection.removeConnectionEventListener(listener);
    }
    
    @Override
    public void addStatementEventListener(final StatementEventListener listener) {
        this.pooledConnection.addStatementEventListener(listener);
    }
    
    @Override
    public void removeStatementEventListener(final StatementEventListener listener) {
        this.pooledConnection.removeStatementEventListener(listener);
    }
    
    @Override
    public XAResource getXAResource() throws SQLException {
        return this.xaConnection.getXAResource();
    }
}
