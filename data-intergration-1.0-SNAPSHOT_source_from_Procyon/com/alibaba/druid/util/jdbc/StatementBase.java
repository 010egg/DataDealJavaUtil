// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util.jdbc;

import java.sql.SQLFeatureNotSupportedException;
import com.alibaba.druid.mock.MockConnectionClosedException;
import com.alibaba.druid.mock.MockConnection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Connection;
import java.sql.Statement;

public abstract class StatementBase implements Statement
{
    private Connection connection;
    private int fetchDirection;
    private int fetchSize;
    private int resultSetType;
    private int resultSetConcurrency;
    private int resultSetHoldability;
    private int maxFieldSize;
    private int maxRows;
    private int queryTimeout;
    private boolean escapeProcessing;
    private String cursorName;
    private SQLWarning warnings;
    private int updateCount;
    protected boolean closed;
    private boolean poolable;
    protected ResultSet generatedKeys;
    protected ResultSet resultSet;
    
    public StatementBase(final Connection connection) {
        this.closed = false;
        this.connection = connection;
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return this.connection;
    }
    
    public void setConnection(final Connection connection) {
        this.connection = connection;
    }
    
    protected void checkOpen() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        if (this.connection != null) {
            if (this.connection instanceof MockConnection) {
                ((MockConnection)this.connection).checkState();
            }
            else if (this.connection.isClosed()) {
                throw new MockConnectionClosedException();
            }
        }
    }
    
    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        this.checkOpen();
        this.fetchDirection = direction;
    }
    
    @Override
    public int getFetchDirection() throws SQLException {
        this.checkOpen();
        return this.fetchDirection;
    }
    
    @Override
    public void setFetchSize(final int rows) throws SQLException {
        this.checkOpen();
        this.fetchSize = rows;
    }
    
    @Override
    public int getFetchSize() throws SQLException {
        this.checkOpen();
        return this.fetchSize;
    }
    
    @Override
    public int getResultSetType() throws SQLException {
        this.checkOpen();
        return this.resultSetType;
    }
    
    public void setResultSetType(final int resultType) {
        this.resultSetType = resultType;
    }
    
    public void setResultSetConcurrency(final int resultSetConcurrency) {
        this.resultSetConcurrency = resultSetConcurrency;
    }
    
    @Override
    public int getResultSetConcurrency() throws SQLException {
        this.checkOpen();
        return this.resultSetConcurrency;
    }
    
    @Override
    public int getResultSetHoldability() throws SQLException {
        this.checkOpen();
        return this.resultSetHoldability;
    }
    
    public void setResultSetHoldability(final int resultSetHoldability) {
        this.resultSetHoldability = resultSetHoldability;
    }
    
    @Override
    public int getMaxFieldSize() throws SQLException {
        this.checkOpen();
        return this.maxFieldSize;
    }
    
    @Override
    public void setMaxFieldSize(final int max) throws SQLException {
        this.checkOpen();
        this.maxFieldSize = max;
    }
    
    @Override
    public int getMaxRows() throws SQLException {
        this.checkOpen();
        return this.maxRows;
    }
    
    @Override
    public void setMaxRows(final int max) throws SQLException {
        this.checkOpen();
        this.maxRows = max;
    }
    
    @Override
    public void setEscapeProcessing(final boolean enable) throws SQLException {
        this.checkOpen();
        this.escapeProcessing = enable;
    }
    
    public boolean isEscapeProcessing() {
        return this.escapeProcessing;
    }
    
    @Override
    public int getQueryTimeout() throws SQLException {
        this.checkOpen();
        return this.queryTimeout;
    }
    
    @Override
    public void setQueryTimeout(final int seconds) throws SQLException {
        this.checkOpen();
        this.queryTimeout = seconds;
    }
    
    @Override
    public void setCursorName(final String name) throws SQLException {
        this.checkOpen();
        this.cursorName = name;
    }
    
    public String getCursorName() {
        return this.cursorName;
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        this.checkOpen();
        return this.warnings;
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        this.checkOpen();
        this.warnings = null;
    }
    
    public void setWarning(final SQLWarning warning) {
        this.warnings = warning;
    }
    
    @Override
    public int getUpdateCount() throws SQLException {
        this.checkOpen();
        return this.updateCount;
    }
    
    public void setUpdateCount(final int updateCount) {
        this.updateCount = updateCount;
    }
    
    @Override
    public void close() throws SQLException {
        this.closed = true;
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (this.isWrapperFor(iface)) {
            return (T)this;
        }
        return null;
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface != null && (iface == this.getClass() || iface.isInstance(this));
    }
    
    @Override
    public boolean isPoolable() throws SQLException {
        this.checkOpen();
        return this.poolable;
    }
    
    @Override
    public void setPoolable(final boolean poolable) throws SQLException {
        this.checkOpen();
        this.poolable = poolable;
    }
    
    @Override
    public void closeOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        return this.closed;
    }
    
    @Override
    public boolean getMoreResults() throws SQLException {
        this.checkOpen();
        return false;
    }
    
    @Override
    public void addBatch(final String sql) throws SQLException {
        this.checkOpen();
    }
    
    @Override
    public void clearBatch() throws SQLException {
        this.checkOpen();
    }
    
    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        this.checkOpen();
        return this.generatedKeys;
    }
    
    public void setGeneratedKeys(final ResultSet generatedKeys) {
        this.generatedKeys = generatedKeys;
    }
    
    @Override
    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        this.checkOpen();
        return 0;
    }
    
    @Override
    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        this.checkOpen();
        return 0;
    }
    
    @Override
    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        this.checkOpen();
        return 0;
    }
    
    @Override
    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        this.checkOpen();
        return false;
    }
    
    @Override
    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        this.checkOpen();
        return false;
    }
    
    @Override
    public boolean execute(final String sql, final String[] columnNames) throws SQLException {
        this.checkOpen();
        return false;
    }
    
    @Override
    public ResultSet getResultSet() throws SQLException {
        this.checkOpen();
        return this.resultSet;
    }
    
    @Override
    public int[] executeBatch() throws SQLException {
        this.checkOpen();
        return new int[0];
    }
    
    @Override
    public boolean getMoreResults(final int current) throws SQLException {
        this.checkOpen();
        return false;
    }
    
    public void setWarnings(final SQLWarning warnings) {
        this.warnings = warnings;
    }
    
    public void setResultSet(final ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    
    @Override
    public boolean execute(final String sql) throws SQLException {
        this.checkOpen();
        return false;
    }
    
    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        return null;
    }
    
    @Override
    public int executeUpdate(final String sql) throws SQLException {
        return 0;
    }
    
    @Override
    public void cancel() throws SQLException {
        this.checkOpen();
    }
}
