// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.pool;

import com.zaxxer.hikari.util.FastList;
import java.util.concurrent.Executor;
import java.sql.Struct;
import java.sql.Array;
import java.util.Properties;
import java.sql.SQLClientInfoException;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Savepoint;
import java.util.Map;
import java.sql.SQLWarning;
import java.sql.DatabaseMetaData;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Wrapper;
import java.sql.Connection;

public class HikariProxyConnection extends ProxyConnection implements Connection, Wrapper, AutoCloseable
{
    @Override
    public Statement createStatement() throws SQLException {
        try {
            return super.createStatement();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        try {
            return super.prepareStatement(sql);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        try {
            return super.prepareCall(sql);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public String nativeSQL(final String s) throws SQLException {
        try {
            return super.delegate.nativeSQL(s);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        try {
            super.setAutoCommit(autoCommit);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public boolean getAutoCommit() throws SQLException {
        try {
            return super.delegate.getAutoCommit();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void commit() throws SQLException {
        try {
            super.commit();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void rollback() throws SQLException {
        try {
            super.rollback();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        try {
            return super.isClosed();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        try {
            return super.getMetaData();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        try {
            super.setReadOnly(readOnly);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public boolean isReadOnly() throws SQLException {
        try {
            return super.delegate.isReadOnly();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setCatalog(final String catalog) throws SQLException {
        try {
            super.setCatalog(catalog);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public String getCatalog() throws SQLException {
        try {
            return super.delegate.getCatalog();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setTransactionIsolation(final int transactionIsolation) throws SQLException {
        try {
            super.setTransactionIsolation(transactionIsolation);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public int getTransactionIsolation() throws SQLException {
        try {
            return super.delegate.getTransactionIsolation();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        try {
            return super.delegate.getWarnings();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        try {
            super.delegate.clearWarnings();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int concurrency) throws SQLException {
        try {
            return super.createStatement(resultSetType, concurrency);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int concurrency) throws SQLException {
        try {
            return super.prepareStatement(sql, resultSetType, concurrency);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int concurrency) throws SQLException {
        try {
            return super.prepareCall(sql, resultSetType, concurrency);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public Map getTypeMap() throws SQLException {
        try {
            return super.delegate.getTypeMap();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setTypeMap(final Map typeMap) throws SQLException {
        try {
            super.delegate.setTypeMap(typeMap);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setHoldability(final int holdability) throws SQLException {
        try {
            super.delegate.setHoldability(holdability);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public int getHoldability() throws SQLException {
        try {
            return super.delegate.getHoldability();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public Savepoint setSavepoint() throws SQLException {
        try {
            return super.delegate.setSavepoint();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public Savepoint setSavepoint(final String savepoint) throws SQLException {
        try {
            return super.delegate.setSavepoint(savepoint);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void rollback(final Savepoint savepoint) throws SQLException {
        try {
            super.rollback(savepoint);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
        try {
            super.delegate.releaseSavepoint(savepoint);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int concurrency, final int holdability) throws SQLException {
        try {
            return super.createStatement(resultSetType, concurrency, holdability);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int concurrency, final int holdability) throws SQLException {
        try {
            return super.prepareStatement(sql, resultSetType, concurrency, holdability);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int concurrency, final int holdability) throws SQLException {
        try {
            return super.prepareCall(sql, resultSetType, concurrency, holdability);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return super.prepareStatement(sql, autoGeneratedKeys);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return super.prepareStatement(sql, columnIndexes);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
        try {
            return super.prepareStatement(sql, columnNames);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public Clob createClob() throws SQLException {
        try {
            return super.delegate.createClob();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public Blob createBlob() throws SQLException {
        try {
            return super.delegate.createBlob();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public NClob createNClob() throws SQLException {
        try {
            return super.delegate.createNClob();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public SQLXML createSQLXML() throws SQLException {
        try {
            return super.delegate.createSQLXML();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public boolean isValid(final int n) throws SQLException {
        try {
            return super.delegate.isValid(n);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setClientInfo(final String s, final String s2) throws SQLClientInfoException {
        super.delegate.setClientInfo(s, s2);
    }
    
    @Override
    public void setClientInfo(final Properties clientInfo) throws SQLClientInfoException {
        super.delegate.setClientInfo(clientInfo);
    }
    
    @Override
    public String getClientInfo(final String s) throws SQLException {
        try {
            return super.delegate.getClientInfo(s);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public Properties getClientInfo() throws SQLException {
        try {
            return super.delegate.getClientInfo();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public Array createArrayOf(final String s, final Object[] array) throws SQLException {
        try {
            return super.delegate.createArrayOf(s, array);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public Struct createStruct(final String s, final Object[] array) throws SQLException {
        try {
            return super.delegate.createStruct(s, array);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setSchema(final String schema) throws SQLException {
        try {
            super.setSchema(schema);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public String getSchema() throws SQLException {
        try {
            return super.delegate.getSchema();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void abort(final Executor executor) throws SQLException {
        try {
            super.delegate.abort(executor);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
        try {
            super.setNetworkTimeout(executor, milliseconds);
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public int getNetworkTimeout() throws SQLException {
        try {
            return super.delegate.getNetworkTimeout();
        }
        catch (SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    protected HikariProxyConnection(final PoolEntry poolEntry, final Connection connection, final FastList openStatements, final ProxyLeakTask leakTask, final long now, final boolean isReadOnly, final boolean isAutoCommit) {
        super(poolEntry, connection, openStatements, leakTask, now, isReadOnly, isAutoCommit);
    }
}
