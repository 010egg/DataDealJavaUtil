// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.util.concurrent.Executor;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Struct;
import java.sql.Array;
import java.sql.SQLClientInfoException;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Map;
import java.sql.DatabaseMetaData;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.sql.Connection;
import com.alibaba.druid.util.jdbc.ConnectionBase;

public class MockConnection extends ConnectionBase implements Connection
{
    private boolean closed;
    private MockDriver driver;
    private int savepointIdSeed;
    private List<Savepoint> savepoints;
    private long id;
    private final long createdTimeMillis;
    private long lastActiveTimeMillis;
    private SQLException error;
    private String lastSql;
    
    public MockConnection() {
        this(null, null, null);
    }
    
    public MockConnection(final MockDriver driver, final String url, final Properties connectProperties) {
        super(url, connectProperties);
        this.closed = false;
        this.savepointIdSeed = 0;
        this.savepoints = new ArrayList<Savepoint>();
        this.createdTimeMillis = System.currentTimeMillis();
        this.lastActiveTimeMillis = System.currentTimeMillis();
        this.driver = driver;
        if (driver != null) {
            this.id = driver.generateConnectionId();
        }
    }
    
    public String getLastSql() {
        return this.lastSql;
    }
    
    public void setLastSql(final String lastSql) {
        this.lastSql = lastSql;
    }
    
    public SQLException getError() {
        return this.error;
    }
    
    public void setError(final SQLException error) {
        this.error = error;
    }
    
    public List<Savepoint> getSavepoints() {
        return this.savepoints;
    }
    
    public long getLastActiveTimeMillis() {
        return this.lastActiveTimeMillis;
    }
    
    public void setLastActiveTimeMillis(final long lastActiveTimeMillis) {
        this.lastActiveTimeMillis = lastActiveTimeMillis;
    }
    
    public long getCreatedTimeMillis() {
        return this.createdTimeMillis;
    }
    
    public long getId() {
        return this.id;
    }
    
    public MockDriver getDriver() {
        return this.driver;
    }
    
    public void setDriver(final MockDriver driver) {
        this.driver = driver;
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T)this;
        }
        return null;
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }
    
    @Override
    public Statement createStatement() throws SQLException {
        this.checkState();
        return this.createMockStatement();
    }
    
    private MockStatement createMockStatement() {
        if (this.driver != null) {
            return this.driver.createMockStatement(this);
        }
        return new MockStatement(this);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        this.checkState();
        return this.createMockPreparedStatement(sql);
    }
    
    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        this.checkState();
        return this.createMockCallableStatement(sql);
    }
    
    @Override
    public String nativeSQL(final String sql) throws SQLException {
        this.checkState();
        return sql;
    }
    
    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        this.checkState();
        super.setAutoCommit(autoCommit);
    }
    
    @Override
    public void commit() throws SQLException {
        this.checkState();
    }
    
    @Override
    public void rollback() throws SQLException {
        this.checkState();
        this.savepoints.clear();
    }
    
    @Override
    public void close() throws SQLException {
        if (!this.closed) {
            this.closed = true;
            if (this.driver != null) {
                this.driver.afterConnectionClose(this);
            }
        }
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        return this.closed;
    }
    
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return null;
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
        this.checkState();
        final MockStatement stmt = this.createMockStatement();
        stmt.setResultSetType(resultSetType);
        stmt.setResultSetConcurrency(resultSetConcurrency);
        return stmt;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        this.checkState();
        final MockPreparedStatement stmt = this.createMockPreparedStatement(sql);
        stmt.setResultSetType(resultSetType);
        stmt.setResultSetConcurrency(resultSetConcurrency);
        return stmt;
    }
    
    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        this.checkState();
        final MockCallableStatement stmt = this.createMockCallableStatement(sql);
        stmt.setResultSetType(resultSetType);
        stmt.setResultSetConcurrency(resultSetConcurrency);
        return stmt;
    }
    
    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return null;
    }
    
    @Override
    public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
    }
    
    @Override
    public Savepoint setSavepoint() throws SQLException {
        this.checkState();
        final MockSavepoint savepoint = new MockSavepoint();
        savepoint.setSavepointId(this.savepointIdSeed++);
        this.savepoints.add(savepoint);
        return savepoint;
    }
    
    @Override
    public Savepoint setSavepoint(final String name) throws SQLException {
        this.checkState();
        final MockSavepoint savepoint = new MockSavepoint();
        savepoint.setSavepointId(this.savepointIdSeed++);
        savepoint.setSavepointName(name);
        this.savepoints.add(savepoint);
        return savepoint;
    }
    
    @Override
    public void checkState() throws SQLException {
        if (this.error != null) {
            throw this.error;
        }
        if (this.closed) {
            throw new MockConnectionClosedException();
        }
    }
    
    @Override
    public void rollback(final Savepoint savepoint) throws SQLException {
        this.checkState();
        final int index = this.savepoints.indexOf(savepoint);
        if (index == -1) {
            throw new SQLException("savepoint not contained");
        }
        for (int i = this.savepoints.size() - 1; i >= index; --i) {
            this.savepoints.remove(i);
        }
    }
    
    @Override
    public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
        this.checkState();
        if (savepoint == null) {
            throw new SQLException("argument is null");
        }
        final int index = this.savepoints.indexOf(savepoint);
        if (index == -1) {
            throw new SQLException("savepoint not contained");
        }
        this.savepoints.remove(savepoint);
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        this.checkState();
        final MockStatement stmt = this.createMockStatement();
        stmt.setResultSetType(resultSetType);
        stmt.setResultSetConcurrency(resultSetConcurrency);
        stmt.setResultSetHoldability(resultSetHoldability);
        return stmt;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        this.checkState();
        final MockPreparedStatement stmt = this.createMockPreparedStatement(sql);
        stmt.setResultSetType(resultSetType);
        stmt.setResultSetConcurrency(resultSetConcurrency);
        stmt.setResultSetHoldability(resultSetHoldability);
        return stmt;
    }
    
    private MockPreparedStatement createMockPreparedStatement(final String sql) {
        if (this.driver != null) {
            return this.driver.createMockPreparedStatement(this, sql);
        }
        return new MockPreparedStatement(this, sql);
    }
    
    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        this.checkState();
        final MockCallableStatement stmt = this.createMockCallableStatement(sql);
        stmt.setResultSetType(resultSetType);
        stmt.setResultSetConcurrency(resultSetConcurrency);
        stmt.setResultSetHoldability(resultSetHoldability);
        return stmt;
    }
    
    private MockCallableStatement createMockCallableStatement(final String sql) {
        if (this.driver != null) {
            return this.driver.createMockCallableStatement(this, sql);
        }
        return new MockCallableStatement(this, sql);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
        this.checkState();
        return this.createMockPreparedStatement(sql);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
        this.checkState();
        return this.createMockPreparedStatement(sql);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
        this.checkState();
        return this.createMockPreparedStatement(sql);
    }
    
    @Override
    public Clob createClob() throws SQLException {
        if (this.driver != null) {
            return this.driver.createClob(this);
        }
        return new MockClob();
    }
    
    @Override
    public Blob createBlob() throws SQLException {
        if (this.driver != null) {
            return this.driver.createBlob(this);
        }
        return new MockBlob();
    }
    
    @Override
    public NClob createNClob() throws SQLException {
        if (this.driver != null) {
            return this.driver.createNClob(this);
        }
        return new MockNClob();
    }
    
    @Override
    public SQLXML createSQLXML() throws SQLException {
        if (this.driver != null) {
            return this.driver.createSQLXML(this);
        }
        return new MockSQLXML();
    }
    
    @Override
    public boolean isValid(final int timeout) throws SQLException {
        return false;
    }
    
    @Override
    public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
    }
    
    @Override
    public void setClientInfo(final Properties properties) throws SQLClientInfoException {
    }
    
    @Override
    public String getClientInfo(final String name) throws SQLException {
        return null;
    }
    
    @Override
    public Properties getClientInfo() throws SQLException {
        return new Properties();
    }
    
    @Override
    public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
        return new MockArray();
    }
    
    @Override
    public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
        return new MockStruct();
    }
    
    @Override
    public void setSchema(final String schema) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public String getSchema() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public void abort(final Executor executor) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public int getNetworkTimeout() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        this.checkState();
        super.setReadOnly(readOnly);
    }
    
    public void handleSleep() {
        if (this.getConnectProperties() != null) {
            final Object propertyValue = this.getConnectProperties().get("executeSleep");
            if (propertyValue != null) {
                final long millis = Long.parseLong(propertyValue.toString());
                try {
                    Thread.sleep(millis);
                }
                catch (InterruptedException ex) {}
            }
        }
    }
}
