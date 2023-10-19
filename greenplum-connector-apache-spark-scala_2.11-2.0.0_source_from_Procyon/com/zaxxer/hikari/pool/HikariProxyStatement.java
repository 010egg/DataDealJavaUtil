// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.pool;

import java.sql.Connection;
import java.sql.SQLWarning;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Wrapper;
import java.sql.Statement;

public class HikariProxyStatement extends ProxyStatement implements Statement, Wrapper, AutoCloseable
{
    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        try {
            return super.executeQuery(sql);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate(final String sql) throws SQLException {
        try {
            return super.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getMaxFieldSize() throws SQLException {
        try {
            return super.delegate.getMaxFieldSize();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setMaxFieldSize(final int maxFieldSize) throws SQLException {
        try {
            super.delegate.setMaxFieldSize(maxFieldSize);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getMaxRows() throws SQLException {
        try {
            return super.delegate.getMaxRows();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setMaxRows(final int maxRows) throws SQLException {
        try {
            super.delegate.setMaxRows(maxRows);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setEscapeProcessing(final boolean escapeProcessing) throws SQLException {
        try {
            super.delegate.setEscapeProcessing(escapeProcessing);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getQueryTimeout() throws SQLException {
        try {
            return super.delegate.getQueryTimeout();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setQueryTimeout(final int queryTimeout) throws SQLException {
        try {
            super.delegate.setQueryTimeout(queryTimeout);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void cancel() throws SQLException {
        try {
            super.delegate.cancel();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        try {
            return super.delegate.getWarnings();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        try {
            super.delegate.clearWarnings();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setCursorName(final String cursorName) throws SQLException {
        try {
            super.delegate.setCursorName(cursorName);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute(final String sql) throws SQLException {
        try {
            return super.execute(sql);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getResultSet() throws SQLException {
        try {
            return super.getResultSet();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getUpdateCount() throws SQLException {
        try {
            return super.delegate.getUpdateCount();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean getMoreResults() throws SQLException {
        try {
            return super.delegate.getMoreResults();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setFetchDirection(final int fetchDirection) throws SQLException {
        try {
            super.delegate.setFetchDirection(fetchDirection);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getFetchDirection() throws SQLException {
        try {
            return super.delegate.getFetchDirection();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setFetchSize(final int fetchSize) throws SQLException {
        try {
            super.delegate.setFetchSize(fetchSize);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getFetchSize() throws SQLException {
        try {
            return super.delegate.getFetchSize();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getResultSetConcurrency() throws SQLException {
        try {
            return super.delegate.getResultSetConcurrency();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getResultSetType() throws SQLException {
        try {
            return super.delegate.getResultSetType();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void addBatch(final String s) throws SQLException {
        try {
            super.delegate.addBatch(s);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void clearBatch() throws SQLException {
        try {
            super.delegate.clearBatch();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int[] executeBatch() throws SQLException {
        try {
            return super.executeBatch();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        try {
            return super.getConnection();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean getMoreResults(final int n) throws SQLException {
        try {
            return super.delegate.getMoreResults(n);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        try {
            return super.getGeneratedKeys();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return super.executeUpdate(sql, autoGeneratedKeys);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return super.executeUpdate(sql, columnIndexes);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        try {
            return super.executeUpdate(sql, columnNames);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return super.execute(sql, autoGeneratedKeys);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return super.execute(sql, columnIndexes);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute(final String sql, final String[] columnNames) throws SQLException {
        try {
            return super.execute(sql, columnNames);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int getResultSetHoldability() throws SQLException {
        try {
            return super.delegate.getResultSetHoldability();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        try {
            return super.delegate.isClosed();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setPoolable(final boolean poolable) throws SQLException {
        try {
            super.delegate.setPoolable(poolable);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean isPoolable() throws SQLException {
        try {
            return super.delegate.isPoolable();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void closeOnCompletion() throws SQLException {
        try {
            super.delegate.closeOnCompletion();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        try {
            return super.delegate.isCloseOnCompletion();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long getLargeUpdateCount() throws SQLException {
        try {
            return super.delegate.getLargeUpdateCount();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void setLargeMaxRows(final long largeMaxRows) throws SQLException {
        try {
            super.delegate.setLargeMaxRows(largeMaxRows);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long getLargeMaxRows() throws SQLException {
        try {
            return super.delegate.getLargeMaxRows();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long[] executeLargeBatch() throws SQLException {
        try {
            return super.delegate.executeLargeBatch();
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String sql) throws SQLException {
        try {
            return super.delegate.executeLargeUpdate(sql);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return super.delegate.executeLargeUpdate(sql, autoGeneratedKeys);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return super.delegate.executeLargeUpdate(sql, columnIndexes);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String sql, final String[] columnNames) throws SQLException {
        try {
            return super.delegate.executeLargeUpdate(sql, columnNames);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean isWrapperFor(final Class clazz) throws SQLException {
        try {
            return super.delegate.isWrapperFor(clazz);
        }
        catch (SQLException e) {
            throw this.checkException(e);
        }
    }
    
    HikariProxyStatement(final ProxyConnection connection, final Statement statement) {
        super(connection, statement);
    }
}
