// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.ibatis;

import com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.client.SqlMapSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClient;

public class SqlMapClientWrapper extends SqlMapExecutorWrapper implements SqlMapClient, ExtendedSqlMapClient
{
    protected final ExtendedSqlMapClient client;
    
    public SqlMapClientWrapper(final ExtendedSqlMapClient client) {
        super(client, (SqlMapExecutor)client);
        this.client = client;
    }
    
    public ExtendedSqlMapClient getClient() {
        return this.client;
    }
    
    public void startTransaction() throws SQLException {
        this.client.startTransaction();
    }
    
    public void startTransaction(final int transactionIsolation) throws SQLException {
        this.client.startTransaction(transactionIsolation);
    }
    
    public void commitTransaction() throws SQLException {
        this.client.commitTransaction();
    }
    
    public void endTransaction() throws SQLException {
        this.client.endTransaction();
    }
    
    public void setUserConnection(final Connection connection) throws SQLException {
        this.client.setUserConnection(connection);
    }
    
    public Connection getUserConnection() throws SQLException {
        return this.client.getUserConnection();
    }
    
    public Connection getCurrentConnection() throws SQLException {
        return this.client.getCurrentConnection();
    }
    
    public DataSource getDataSource() {
        return this.client.getDataSource();
    }
    
    public SqlMapSession openSession() {
        final SqlMapSession session = this.client.openSession();
        IbatisUtils.setClientImpl((SqlMapExecutor)session, this.clientImplWrapper);
        return (SqlMapSession)new SqlMapSessionWrapper(this.client, session);
    }
    
    public SqlMapSession openSession(final Connection conn) {
        final SqlMapSession session = this.client.openSession(conn);
        IbatisUtils.setClientImpl((SqlMapExecutor)session, this.clientImplWrapper);
        return (SqlMapSession)new SqlMapSessionWrapper(this.client, session);
    }
    
    public SqlMapSession getSession() {
        final SqlMapSession session = this.client.getSession();
        IbatisUtils.setClientImpl((SqlMapExecutor)session, this.clientImplWrapper);
        return (SqlMapSession)new SqlMapSessionWrapper(this.client, session);
    }
    
    public void flushDataCache() {
        this.client.flushDataCache();
    }
    
    public void flushDataCache(final String cacheId) {
        this.client.flushDataCache(cacheId);
    }
    
    public MappedStatement getMappedStatement(final String id) {
        return this.client.getMappedStatement(id);
    }
    
    public boolean isLazyLoadingEnabled() {
        return this.client.isLazyLoadingEnabled();
    }
    
    public boolean isEnhancementEnabled() {
        return this.client.isEnhancementEnabled();
    }
    
    public SqlExecutor getSqlExecutor() {
        return this.client.getSqlExecutor();
    }
    
    public SqlMapExecutorDelegate getDelegate() {
        return this.client.getDelegate();
    }
    
    public ResultObjectFactory getResultObjectFactory() {
        return this.client.getResultObjectFactory();
    }
}
