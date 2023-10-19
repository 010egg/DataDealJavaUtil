// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.xa;

import javax.sql.StatementEventListener;
import javax.sql.ConnectionEventListener;
import com.alibaba.druid.util.JdbcUtils;
import java.sql.SQLException;
import net.sourceforge.jtds.jdbc.XASupport;
import javax.transaction.xa.XAResource;
import java.sql.Connection;
import javax.sql.XAConnection;

public class JtdsXAConnection implements XAConnection
{
    private Connection connection;
    private final XAResource resource;
    private final int xaConnectionId;
    
    public JtdsXAConnection(final Connection connection) throws SQLException {
        this.resource = new JtdsXAResource(this, connection);
        this.connection = connection;
        this.xaConnectionId = XASupport.xa_open(connection);
    }
    
    int getXAConnectionID() {
        return this.xaConnectionId;
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return this.connection;
    }
    
    @Override
    public void close() throws SQLException {
        try {
            XASupport.xa_close(this.connection, this.xaConnectionId);
        }
        catch (SQLException ex) {}
        JdbcUtils.close(this.connection);
    }
    
    @Override
    public void addConnectionEventListener(final ConnectionEventListener listener) {
    }
    
    @Override
    public void removeConnectionEventListener(final ConnectionEventListener listener) {
    }
    
    @Override
    public void addStatementEventListener(final StatementEventListener listener) {
    }
    
    @Override
    public void removeStatementEventListener(final StatementEventListener listener) {
    }
    
    @Override
    public XAResource getXAResource() throws SQLException {
        return this.resource;
    }
}
