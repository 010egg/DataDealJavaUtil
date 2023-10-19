// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import com.alibaba.druid.filter.FilterChain;
import java.util.concurrent.Executor;
import java.sql.SQLClientInfoException;
import java.sql.Savepoint;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.SQLWarning;
import java.util.Map;
import java.sql.DatabaseMetaData;
import java.sql.Struct;
import java.sql.Statement;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.Clob;
import java.sql.Blob;
import java.sql.Array;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Wrapper;
import com.alibaba.druid.filter.FilterChainImpl;
import java.util.Properties;
import java.sql.Connection;

public class ConnectionProxyImpl extends WrapperProxyImpl implements ConnectionProxy
{
    private final Connection connection;
    private final DataSourceProxy dataSource;
    private final Properties properties;
    private final long connectedTime;
    private TransactionInfo transactionInfo;
    private int closeCount;
    private FilterChainImpl filterChain;
    
    public ConnectionProxyImpl(final DataSourceProxy dataSource, final Connection connection, final Properties properties, final long id) {
        super(connection, id);
        this.filterChain = null;
        this.dataSource = dataSource;
        this.connection = connection;
        this.properties = properties;
        this.connectedTime = System.currentTimeMillis();
    }
    
    @Override
    public Date getConnectedTime() {
        return new Date(this.connectedTime);
    }
    
    @Override
    public Properties getProperties() {
        return this.properties;
    }
    
    public Connection getConnectionRaw() {
        return this.connection;
    }
    
    @Override
    public Connection getRawObject() {
        return this.connection;
    }
    
    @Override
    public DataSourceProxy getDirectDataSource() {
        return this.dataSource;
    }
    
    @Override
    public FilterChainImpl createChain() {
        FilterChainImpl chain = this.filterChain;
        if (chain == null) {
            chain = new FilterChainImpl(this.dataSource);
        }
        else {
            this.filterChain = null;
        }
        return chain;
    }
    
    public void recycleFilterChain(final FilterChainImpl chain) {
        chain.reset();
        this.filterChain = chain;
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_clearWarnings(this);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void close() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_close(this);
        ++this.closeCount;
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void commit() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_commit(this);
        if (this.transactionInfo != null) {
            this.transactionInfo.setEndTimeMillis();
        }
        this.recycleFilterChain(chain);
    }
    
    @Override
    public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Array value = chain.connection_createArrayOf(this, typeName, elements);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Blob createBlob() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Blob value = chain.connection_createBlob(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Clob createClob() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Clob value = chain.connection_createClob(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public NClob createNClob() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final NClob value = chain.connection_createNClob(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public SQLXML createSQLXML() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final SQLXML value = chain.connection_createSQLXML(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Statement createStatement() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Statement stmt = chain.connection_createStatement(this);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Statement stmt = chain.connection_createStatement(this, resultSetType, resultSetConcurrency);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Statement stmt = chain.connection_createStatement(this, resultSetType, resultSetConcurrency, resultSetHoldability);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Struct value = chain.connection_createStruct(this, typeName, attributes);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean getAutoCommit() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.connection_getAutoCommit(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getCatalog() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.connection_getCatalog(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Properties getClientInfo() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Properties value = chain.connection_getClientInfo(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String getClientInfo(final String name) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.connection_getClientInfo(this, name);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getHoldability() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.connection_getHoldability(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final DatabaseMetaData value = chain.connection_getMetaData(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public int getTransactionIsolation() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int value = chain.connection_getTransactionIsolation(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Map<String, Class<?>> value = chain.connection_getTypeMap(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final SQLWarning value = chain.connection_getWarnings(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.connection_isClosed(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isReadOnly() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.connection_isReadOnly(this);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public boolean isValid(final int timeout) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final boolean value = chain.connection_isValid(this, timeout);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public String nativeSQL(final String sql) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String value = chain.connection_nativeSQL(this, sql);
        this.recycleFilterChain(chain);
        return value;
    }
    
    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final CallableStatement stmt = chain.connection_prepareCall(this, sql);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final CallableStatement stmt = chain.connection_prepareCall(this, sql, resultSetType, resultSetConcurrency);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final CallableStatement stmt = chain.connection_prepareCall(this, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final PreparedStatement stmt = chain.connection_prepareStatement(this, sql);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final PreparedStatement stmt = chain.connection_prepareStatement(this, sql, autoGeneratedKeys);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final PreparedStatement stmt = chain.connection_prepareStatement(this, sql, columnIndexes);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final PreparedStatement stmt = chain.connection_prepareStatement(this, sql, columnNames);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final PreparedStatement stmt = chain.connection_prepareStatement(this, sql, resultSetType, resultSetConcurrency);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final PreparedStatement stmt = chain.connection_prepareStatement(this, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        this.recycleFilterChain(chain);
        return stmt;
    }
    
    @Override
    public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_releaseSavepoint(this, savepoint);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void rollback() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_rollback(this);
        this.recycleFilterChain(chain);
        if (this.transactionInfo != null) {
            this.transactionInfo.setEndTimeMillis();
        }
    }
    
    @Override
    public void rollback(final Savepoint savepoint) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_rollback(this, savepoint);
        this.recycleFilterChain(chain);
        if (this.transactionInfo != null) {
            this.transactionInfo.setEndTimeMillis();
        }
    }
    
    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        if (!autoCommit) {
            if (this.transactionInfo == null) {
                final long transactionId = this.dataSource.createTransactionId();
                this.putAttribute("stat.tx", this.transactionInfo = new TransactionInfo(transactionId));
            }
        }
        else {
            this.transactionInfo = null;
        }
        final FilterChainImpl chain = this.createChain();
        chain.connection_setAutoCommit(this, autoCommit);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void setCatalog(final String catalog) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_setCatalog(this, catalog);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void setClientInfo(final Properties properties) throws SQLClientInfoException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_setClientInfo(this, properties);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_setClientInfo(this, name, value);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void setHoldability(final int holdability) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_setHoldability(this, holdability);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_setReadOnly(this, readOnly);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public Savepoint setSavepoint() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Savepoint savepoint = chain.connection_setSavepoint(this);
        this.recycleFilterChain(chain);
        return savepoint;
    }
    
    @Override
    public Savepoint setSavepoint(final String name) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final Savepoint savepoint = chain.connection_setSavepoint(this, name);
        this.recycleFilterChain(chain);
        return savepoint;
    }
    
    @Override
    public void setTransactionIsolation(final int level) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_setTransactionIsolation(this, level);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_setTypeMap(this, map);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void setSchema(final String schema) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_setSchema(this, schema);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public String getSchema() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final String schema = chain.connection_getSchema(this);
        this.recycleFilterChain(chain);
        return schema;
    }
    
    @Override
    public void abort(final Executor executor) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_abort(this, executor);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
        final FilterChainImpl chain = this.createChain();
        chain.connection_setNetworkTimeout(this, executor, milliseconds);
        this.recycleFilterChain(chain);
    }
    
    @Override
    public int getNetworkTimeout() throws SQLException {
        final FilterChainImpl chain = this.createChain();
        final int networkTimeout = chain.connection_getNetworkTimeout(this);
        this.recycleFilterChain(chain);
        return networkTimeout;
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface == this.getClass() || iface == ConnectionProxy.class) {
            return (T)this;
        }
        return super.unwrap(iface);
    }
    
    @Override
    public TransactionInfo getTransactionInfo() {
        return this.transactionInfo;
    }
    
    @Override
    public int getCloseCount() {
        return this.closeCount;
    }
}
