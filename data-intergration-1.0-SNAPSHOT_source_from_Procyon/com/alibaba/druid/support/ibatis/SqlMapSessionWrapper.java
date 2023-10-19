// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.ibatis;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.client.SqlMapSession;

public class SqlMapSessionWrapper extends SqlMapExecutorWrapper implements SqlMapSession
{
    private SqlMapSession session;
    
    public SqlMapSessionWrapper(final ExtendedSqlMapClient client, final SqlMapSession session) {
        super(client, (SqlMapExecutor)session);
        this.session = session;
    }
    
    public void startTransaction() throws SQLException {
        this.session.startTransaction();
    }
    
    public void startTransaction(final int transactionIsolation) throws SQLException {
        this.session.startTransaction(transactionIsolation);
    }
    
    public void commitTransaction() throws SQLException {
        this.session.commitTransaction();
    }
    
    public void endTransaction() throws SQLException {
        this.session.endTransaction();
    }
    
    public void setUserConnection(final Connection connection) throws SQLException {
        this.session.setUserConnection(connection);
    }
    
    @Deprecated
    public Connection getUserConnection() throws SQLException {
        return this.session.getUserConnection();
    }
    
    public Connection getCurrentConnection() throws SQLException {
        return this.session.getCurrentConnection();
    }
    
    public DataSource getDataSource() {
        return this.session.getDataSource();
    }
    
    public void close() {
        this.session.close();
    }
}
