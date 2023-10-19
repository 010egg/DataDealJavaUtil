// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter;

import com.alibaba.druid.proxy.jdbc.ResultSetMetaDataProxy;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.proxy.jdbc.NClobProxyImpl;
import com.alibaba.druid.proxy.jdbc.ClobProxyImpl;
import java.io.Writer;
import java.io.OutputStream;
import com.alibaba.druid.proxy.jdbc.NClobProxy;
import java.sql.ParameterMetaData;
import com.alibaba.druid.proxy.jdbc.ClobProxy;
import java.sql.RowId;
import java.net.URL;
import java.util.Calendar;
import java.sql.Ref;
import java.io.Reader;
import com.alibaba.druid.proxy.jdbc.ResultSetProxyImpl;
import java.sql.ResultSet;
import com.alibaba.druid.proxy.jdbc.ResultSetMetaDataProxyImpl;
import java.sql.ResultSetMetaData;
import java.io.InputStream;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import java.util.concurrent.Executor;
import java.sql.SQLClientInfoException;
import java.sql.Savepoint;
import java.sql.PreparedStatement;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxyImpl;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import java.sql.CallableStatement;
import com.alibaba.druid.proxy.jdbc.CallableStatementProxyImpl;
import com.alibaba.druid.proxy.jdbc.CallableStatementProxy;
import java.sql.SQLWarning;
import java.util.Map;
import java.sql.DatabaseMetaData;
import java.util.List;
import java.sql.Struct;
import java.sql.Statement;
import com.alibaba.druid.proxy.jdbc.StatementProxyImpl;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.Clob;
import java.sql.Blob;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Driver;
import com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import java.util.Properties;
import java.sql.SQLException;
import java.sql.Wrapper;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;

public class FilterChainImpl implements FilterChain
{
    protected int pos;
    private final DataSourceProxy dataSource;
    private final int filterSize;
    
    public FilterChainImpl(final DataSourceProxy dataSource) {
        this.pos = 0;
        this.dataSource = dataSource;
        this.filterSize = this.getFilters().size();
    }
    
    public FilterChainImpl(final DataSourceProxy dataSource, final int pos) {
        this.pos = 0;
        this.dataSource = dataSource;
        this.pos = pos;
        this.filterSize = this.getFilters().size();
    }
    
    @Override
    public int getFilterSize() {
        return this.filterSize;
    }
    
    @Override
    public int getPos() {
        return this.pos;
    }
    
    public void reset() {
        this.pos = 0;
    }
    
    @Override
    public FilterChain cloneChain() {
        return new FilterChainImpl(this.dataSource, this.pos);
    }
    
    @Override
    public DataSourceProxy getDataSource() {
        return this.dataSource;
    }
    
    @Override
    public boolean isWrapperFor(final Wrapper wrapper, final Class<?> iface) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().isWrapperFor(this, wrapper, iface);
        }
        return iface.isInstance(wrapper) || wrapper.isWrapperFor(iface);
    }
    
    @Override
    public <T> T unwrap(final Wrapper wrapper, final Class<T> iface) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().unwrap(this, wrapper, iface);
        }
        if (iface == null) {
            return null;
        }
        if (iface.isAssignableFrom(wrapper.getClass())) {
            return (T)wrapper;
        }
        return wrapper.unwrap(iface);
    }
    
    @Override
    public ConnectionProxy connection_connect(final Properties info) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_connect(this, info);
        }
        final Driver driver = this.dataSource.getRawDriver();
        final String url = this.dataSource.getRawJdbcUrl();
        final Connection nativeConnection = driver.connect(url, info);
        if (nativeConnection == null) {
            return null;
        }
        return new ConnectionProxyImpl(this.dataSource, nativeConnection, info, this.dataSource.createConnectionId());
    }
    
    @Override
    public void connection_clearWarnings(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_clearWarnings(this, connection);
            return;
        }
        connection.getRawObject().clearWarnings();
    }
    
    @Override
    public void connection_close(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_close(this, connection);
            return;
        }
        connection.getRawObject().close();
        connection.clearAttributes();
    }
    
    @Override
    public void connection_commit(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_commit(this, connection);
            return;
        }
        connection.getRawObject().commit();
    }
    
    @Override
    public Array connection_createArrayOf(final ConnectionProxy connection, final String typeName, final Object[] elements) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_createArrayOf(this, connection, typeName, elements);
        }
        return connection.getRawObject().createArrayOf(typeName, elements);
    }
    
    @Override
    public Blob connection_createBlob(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_createBlob(this, connection);
        }
        return connection.getRawObject().createBlob();
    }
    
    @Override
    public Clob connection_createClob(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_createClob(this, connection);
        }
        return this.wrap(connection, connection.getRawObject().createClob());
    }
    
    @Override
    public NClob connection_createNClob(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_createNClob(this, connection);
        }
        return this.wrap(connection, connection.getRawObject().createNClob());
    }
    
    @Override
    public SQLXML connection_createSQLXML(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_createSQLXML(this, connection);
        }
        return connection.getRawObject().createSQLXML();
    }
    
    @Override
    public StatementProxy connection_createStatement(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_createStatement(this, connection);
        }
        final Statement statement = connection.getRawObject().createStatement();
        if (statement == null) {
            return null;
        }
        return new StatementProxyImpl(connection, statement, this.dataSource.createStatementId());
    }
    
    @Override
    public StatementProxy connection_createStatement(final ConnectionProxy connection, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_createStatement(this, connection, resultSetType, resultSetConcurrency);
        }
        final Statement statement = connection.getRawObject().createStatement(resultSetType, resultSetConcurrency);
        if (statement == null) {
            return null;
        }
        return new StatementProxyImpl(connection, statement, this.dataSource.createStatementId());
    }
    
    @Override
    public StatementProxy connection_createStatement(final ConnectionProxy connection, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_createStatement(this, connection, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        final Statement statement = connection.getRawObject().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        if (statement == null) {
            return null;
        }
        return new StatementProxyImpl(connection, statement, this.dataSource.createStatementId());
    }
    
    @Override
    public Struct connection_createStruct(final ConnectionProxy connection, final String typeName, final Object[] attributes) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_createStruct(this, connection, typeName, attributes);
        }
        return connection.getRawObject().createStruct(typeName, attributes);
    }
    
    @Override
    public boolean connection_getAutoCommit(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_getAutoCommit(this, connection);
        }
        return connection.getRawObject().getAutoCommit();
    }
    
    @Override
    public String connection_getCatalog(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_getCatalog(this, connection);
        }
        return connection.getRawObject().getCatalog();
    }
    
    @Override
    public Properties connection_getClientInfo(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_getClientInfo(this, connection);
        }
        return connection.getRawObject().getClientInfo();
    }
    
    @Override
    public String connection_getClientInfo(final ConnectionProxy connection, final String name) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_getClientInfo(this, connection, name);
        }
        return connection.getRawObject().getClientInfo(name);
    }
    
    public List<Filter> getFilters() {
        return this.dataSource.getProxyFilters();
    }
    
    @Override
    public int connection_getHoldability(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_getHoldability(this, connection);
        }
        return connection.getRawObject().getHoldability();
    }
    
    @Override
    public DatabaseMetaData connection_getMetaData(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_getMetaData(this, connection);
        }
        return connection.getRawObject().getMetaData();
    }
    
    @Override
    public int connection_getTransactionIsolation(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_getTransactionIsolation(this, connection);
        }
        return connection.getRawObject().getTransactionIsolation();
    }
    
    @Override
    public Map<String, Class<?>> connection_getTypeMap(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_getTypeMap(this, connection);
        }
        return connection.getRawObject().getTypeMap();
    }
    
    @Override
    public SQLWarning connection_getWarnings(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_getWarnings(this, connection);
        }
        return connection.getRawObject().getWarnings();
    }
    
    @Override
    public boolean connection_isClosed(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_isClosed(this, connection);
        }
        return connection.getRawObject().isClosed();
    }
    
    @Override
    public boolean connection_isReadOnly(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_isReadOnly(this, connection);
        }
        return connection.getRawObject().isReadOnly();
    }
    
    @Override
    public boolean connection_isValid(final ConnectionProxy connection, final int timeout) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_isValid(this, connection, timeout);
        }
        return connection.getRawObject().isValid(timeout);
    }
    
    @Override
    public String connection_nativeSQL(final ConnectionProxy connection, final String sql) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_nativeSQL(this, connection, sql);
        }
        return connection.getRawObject().nativeSQL(sql);
    }
    
    private Filter nextFilter() {
        return this.getFilters().get(this.pos++);
    }
    
    @Override
    public CallableStatementProxy connection_prepareCall(final ConnectionProxy connection, final String sql) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_prepareCall(this, connection, sql);
        }
        final CallableStatement statement = connection.getRawObject().prepareCall(sql);
        if (statement == null) {
            return null;
        }
        return new CallableStatementProxyImpl(connection, statement, sql, this.dataSource.createStatementId());
    }
    
    @Override
    public CallableStatementProxy connection_prepareCall(final ConnectionProxy connection, final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_prepareCall(this, connection, sql, resultSetType, resultSetConcurrency);
        }
        final CallableStatement statement = connection.getRawObject().prepareCall(sql, resultSetType, resultSetConcurrency);
        if (statement == null) {
            return null;
        }
        return new CallableStatementProxyImpl(connection, statement, sql, this.dataSource.createStatementId());
    }
    
    @Override
    public CallableStatementProxy connection_prepareCall(final ConnectionProxy connection, final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_prepareCall(this, connection, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        final CallableStatement statement = connection.getRawObject().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        if (statement == null) {
            return null;
        }
        return new CallableStatementProxyImpl(connection, statement, sql, this.dataSource.createStatementId());
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final ConnectionProxy connection, final String sql) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_prepareStatement(this, connection, sql);
        }
        final PreparedStatement statement = connection.getRawObject().prepareStatement(sql);
        if (statement == null) {
            return null;
        }
        return new PreparedStatementProxyImpl(connection, statement, sql, this.dataSource.createStatementId());
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final ConnectionProxy connection, final String sql, final int autoGeneratedKeys) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_prepareStatement(this, connection, sql, autoGeneratedKeys);
        }
        final PreparedStatement statement = connection.getRawObject().prepareStatement(sql, autoGeneratedKeys);
        if (statement == null) {
            return null;
        }
        return new PreparedStatementProxyImpl(connection, statement, sql, this.dataSource.createStatementId());
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final ConnectionProxy connection, final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_prepareStatement(this, connection, sql, resultSetType, resultSetConcurrency);
        }
        final PreparedStatement statement = connection.getRawObject().prepareStatement(sql, resultSetType, resultSetConcurrency);
        if (statement == null) {
            return null;
        }
        return new PreparedStatementProxyImpl(connection, statement, sql, this.dataSource.createStatementId());
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final ConnectionProxy connection, final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_prepareStatement(this, connection, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        final PreparedStatement statement = connection.getRawObject().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        if (statement == null) {
            return null;
        }
        return new PreparedStatementProxyImpl(connection, statement, sql, this.dataSource.createStatementId());
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final ConnectionProxy connection, final String sql, final int[] columnIndexes) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_prepareStatement(this, connection, sql, columnIndexes);
        }
        final PreparedStatement statement = connection.getRawObject().prepareStatement(sql, columnIndexes);
        if (statement == null) {
            return null;
        }
        return new PreparedStatementProxyImpl(connection, statement, sql, this.dataSource.createStatementId());
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final ConnectionProxy connection, final String sql, final String[] columnNames) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_prepareStatement(this, connection, sql, columnNames);
        }
        final PreparedStatement statement = connection.getRawObject().prepareStatement(sql, columnNames);
        if (statement == null) {
            return null;
        }
        return new PreparedStatementProxyImpl(connection, statement, sql, this.dataSource.createStatementId());
    }
    
    @Override
    public void connection_releaseSavepoint(final ConnectionProxy connection, final Savepoint savepoint) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_releaseSavepoint(this, connection, savepoint);
            return;
        }
        connection.getRawObject().releaseSavepoint(savepoint);
    }
    
    @Override
    public void connection_rollback(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_rollback(this, connection);
            return;
        }
        connection.getRawObject().rollback();
    }
    
    @Override
    public void connection_rollback(final ConnectionProxy connection, final Savepoint savepoint) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_rollback(this, connection, savepoint);
            return;
        }
        connection.getRawObject().rollback(savepoint);
    }
    
    @Override
    public void connection_setAutoCommit(final ConnectionProxy connection, final boolean autoCommit) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_setAutoCommit(this, connection, autoCommit);
            return;
        }
        connection.getRawObject().setAutoCommit(autoCommit);
    }
    
    @Override
    public void connection_setCatalog(final ConnectionProxy connection, final String catalog) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_setCatalog(this, connection, catalog);
            return;
        }
        connection.getRawObject().setCatalog(catalog);
    }
    
    @Override
    public void connection_setClientInfo(final ConnectionProxy connection, final Properties properties) throws SQLClientInfoException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_setClientInfo(this, connection, properties);
            return;
        }
        connection.getRawObject().setClientInfo(properties);
    }
    
    @Override
    public void connection_setClientInfo(final ConnectionProxy connection, final String name, final String value) throws SQLClientInfoException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_setClientInfo(this, connection, name, value);
            return;
        }
        connection.getRawObject().setClientInfo(name, value);
    }
    
    @Override
    public void connection_setHoldability(final ConnectionProxy connection, final int holdability) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_setHoldability(this, connection, holdability);
            return;
        }
        connection.getRawObject().setHoldability(holdability);
    }
    
    @Override
    public void connection_setReadOnly(final ConnectionProxy connection, final boolean readOnly) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_setReadOnly(this, connection, readOnly);
            return;
        }
        connection.getRawObject().setReadOnly(readOnly);
    }
    
    @Override
    public Savepoint connection_setSavepoint(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_setSavepoint(this, connection);
        }
        return connection.getRawObject().setSavepoint();
    }
    
    @Override
    public Savepoint connection_setSavepoint(final ConnectionProxy connection, final String name) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_setSavepoint(this, connection, name);
        }
        return connection.getRawObject().setSavepoint(name);
    }
    
    @Override
    public void connection_setTransactionIsolation(final ConnectionProxy connection, final int level) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_setTransactionIsolation(this, connection, level);
            return;
        }
        connection.getRawObject().setTransactionIsolation(level);
    }
    
    @Override
    public void connection_setTypeMap(final ConnectionProxy connection, final Map<String, Class<?>> map) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_setTypeMap(this, connection, map);
            return;
        }
        connection.getRawObject().setTypeMap(map);
    }
    
    @Override
    public String connection_getSchema(final ConnectionProxy connection) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_getSchema(this, connection);
        }
        return connection.getRawObject().getSchema();
    }
    
    @Override
    public void connection_setSchema(final ConnectionProxy connection, final String schema) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_setSchema(this, connection, schema);
            return;
        }
        connection.getRawObject().setSchema(schema);
    }
    
    @Override
    public void connection_abort(final ConnectionProxy conn, final Executor executor) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_abort(this, conn, executor);
            return;
        }
        conn.getRawObject().abort(executor);
    }
    
    @Override
    public void connection_setNetworkTimeout(final ConnectionProxy conn, final Executor executor, final int milliseconds) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().connection_setNetworkTimeout(this, conn, executor, milliseconds);
            return;
        }
        conn.getRawObject().setNetworkTimeout(executor, milliseconds);
    }
    
    @Override
    public int connection_getNetworkTimeout(final ConnectionProxy conn) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().connection_getNetworkTimeout(this, conn);
        }
        return conn.getRawObject().getNetworkTimeout();
    }
    
    @Override
    public boolean resultSet_next(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_next(this, rs);
        }
        return rs.getResultSetRaw().next();
    }
    
    @Override
    public void resultSet_close(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_close(this, rs);
            return;
        }
        rs.getResultSetRaw().close();
        rs.clearAttributes();
    }
    
    @Override
    public boolean resultSet_wasNull(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_wasNull(this, rs);
        }
        return rs.getResultSetRaw().wasNull();
    }
    
    @Override
    public String resultSet_getString(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getString(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getString(columnIndex);
    }
    
    @Override
    public boolean resultSet_getBoolean(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getBoolean(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getBoolean(columnIndex);
    }
    
    @Override
    public byte resultSet_getByte(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getByte(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getByte(columnIndex);
    }
    
    @Override
    public short resultSet_getShort(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getShort(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getShort(columnIndex);
    }
    
    @Override
    public int resultSet_getInt(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getInt(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getInt(columnIndex);
    }
    
    @Override
    public long resultSet_getLong(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getLong(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getLong(columnIndex);
    }
    
    @Override
    public float resultSet_getFloat(final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getFloat(this, resultSet, columnIndex);
        }
        return resultSet.getResultSetRaw().getFloat(columnIndex);
    }
    
    @Override
    public double resultSet_getDouble(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getDouble(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getDouble(columnIndex);
    }
    
    @Override
    public BigDecimal resultSet_getBigDecimal(final ResultSetProxy rs, final int columnIndex, final int scale) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getBigDecimal(this, rs, columnIndex, scale);
        }
        return rs.getResultSetRaw().getBigDecimal(columnIndex, scale);
    }
    
    @Override
    public byte[] resultSet_getBytes(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getBytes(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getBytes(columnIndex);
    }
    
    @Override
    public Date resultSet_getDate(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getDate(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getDate(columnIndex);
    }
    
    @Override
    public Time resultSet_getTime(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getTime(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getTime(columnIndex);
    }
    
    @Override
    public Timestamp resultSet_getTimestamp(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getTimestamp(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getTimestamp(columnIndex);
    }
    
    @Override
    public InputStream resultSet_getAsciiStream(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getAsciiStream(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getAsciiStream(columnIndex);
    }
    
    @Override
    public InputStream resultSet_getUnicodeStream(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getUnicodeStream(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getUnicodeStream(columnIndex);
    }
    
    @Override
    public InputStream resultSet_getBinaryStream(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getBinaryStream(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getBinaryStream(columnIndex);
    }
    
    @Override
    public String resultSet_getString(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getString(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getString(columnLabel);
    }
    
    @Override
    public boolean resultSet_getBoolean(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getBoolean(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getBoolean(columnLabel);
    }
    
    @Override
    public byte resultSet_getByte(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getByte(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getByte(columnLabel);
    }
    
    @Override
    public short resultSet_getShort(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getShort(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getShort(columnLabel);
    }
    
    @Override
    public int resultSet_getInt(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getInt(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getInt(columnLabel);
    }
    
    @Override
    public long resultSet_getLong(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getLong(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getLong(columnLabel);
    }
    
    @Override
    public float resultSet_getFloat(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getFloat(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getFloat(columnLabel);
    }
    
    @Override
    public double resultSet_getDouble(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getDouble(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getDouble(columnLabel);
    }
    
    @Override
    public BigDecimal resultSet_getBigDecimal(final ResultSetProxy rs, final String columnLabel, final int scale) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getBigDecimal(this, rs, columnLabel, scale);
        }
        return rs.getResultSetRaw().getBigDecimal(columnLabel, scale);
    }
    
    @Override
    public byte[] resultSet_getBytes(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getBytes(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getBytes(columnLabel);
    }
    
    @Override
    public Date resultSet_getDate(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getDate(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getDate(columnLabel);
    }
    
    @Override
    public Time resultSet_getTime(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getTime(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getTime(columnLabel);
    }
    
    @Override
    public Timestamp resultSet_getTimestamp(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getTimestamp(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getTimestamp(columnLabel);
    }
    
    @Override
    public InputStream resultSet_getAsciiStream(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getAsciiStream(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getAsciiStream(columnLabel);
    }
    
    @Override
    public InputStream resultSet_getUnicodeStream(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getUnicodeStream(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getUnicodeStream(columnLabel);
    }
    
    @Override
    public InputStream resultSet_getBinaryStream(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getBinaryStream(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getBinaryStream(columnLabel);
    }
    
    @Override
    public SQLWarning resultSet_getWarnings(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getWarnings(this, rs);
        }
        return rs.getResultSetRaw().getWarnings();
    }
    
    @Override
    public void resultSet_clearWarnings(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_clearWarnings(this, rs);
            return;
        }
        rs.getResultSetRaw().clearWarnings();
    }
    
    @Override
    public String resultSet_getCursorName(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getCursorName(this, rs);
        }
        return rs.getResultSetRaw().getCursorName();
    }
    
    @Override
    public ResultSetMetaData resultSet_getMetaData(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getMetaData(this, rs);
        }
        final ResultSetMetaData metaData = rs.getResultSetRaw().getMetaData();
        if (metaData == null) {
            return null;
        }
        return new ResultSetMetaDataProxyImpl(metaData, this.dataSource.createMetaDataId(), rs);
    }
    
    @Override
    public Object resultSet_getObject(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getObject(this, rs, columnIndex);
        }
        final Object obj = rs.getResultSetRaw().getObject(columnIndex);
        if (obj instanceof ResultSet) {
            final StatementProxy statement = rs.getStatementProxy();
            return new ResultSetProxyImpl(statement, (ResultSet)obj, this.dataSource.createResultSetId(), statement.getLastExecuteSql());
        }
        if (obj instanceof Clob) {
            return this.wrap(rs.getStatementProxy(), (Clob)obj);
        }
        return obj;
    }
    
    @Override
    public <T> T resultSet_getObject(final ResultSetProxy rs, final int columnIndex, final Class<T> type) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getObject(this, rs, columnIndex, type);
        }
        final Object obj = rs.getResultSetRaw().getObject(columnIndex, type);
        if (obj instanceof ResultSet) {
            final StatementProxy statement = rs.getStatementProxy();
            return (T)new ResultSetProxyImpl(statement, (ResultSet)obj, this.dataSource.createResultSetId(), statement.getLastExecuteSql());
        }
        if (obj instanceof Clob) {
            return (T)this.wrap(rs.getStatementProxy(), (Clob)obj);
        }
        return (T)obj;
    }
    
    @Override
    public Object resultSet_getObject(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getObject(this, rs, columnLabel);
        }
        final Object obj = rs.getResultSetRaw().getObject(columnLabel);
        if (obj instanceof ResultSet) {
            final StatementProxy stmt = rs.getStatementProxy();
            return new ResultSetProxyImpl(stmt, (ResultSet)obj, this.dataSource.createResultSetId(), stmt.getLastExecuteSql());
        }
        if (obj instanceof Clob) {
            return this.wrap(rs.getStatementProxy(), (Clob)obj);
        }
        return obj;
    }
    
    @Override
    public <T> T resultSet_getObject(final ResultSetProxy rs, final String columnLabel, final Class<T> type) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getObject(this, rs, columnLabel, type);
        }
        final T obj = rs.getResultSetRaw().getObject(columnLabel, type);
        if (obj instanceof ResultSet) {
            final StatementProxy stmt = rs.getStatementProxy();
            return (T)new ResultSetProxyImpl(stmt, (ResultSet)obj, this.dataSource.createResultSetId(), stmt.getLastExecuteSql());
        }
        if (obj instanceof Clob) {
            return (T)this.wrap(rs.getStatementProxy(), (Clob)obj);
        }
        return obj;
    }
    
    @Override
    public int resultSet_findColumn(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_findColumn(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().findColumn(columnLabel);
    }
    
    @Override
    public Reader resultSet_getCharacterStream(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getCharacterStream(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getCharacterStream(columnIndex);
    }
    
    @Override
    public Reader resultSet_getCharacterStream(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getCharacterStream(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getCharacterStream(columnLabel);
    }
    
    @Override
    public BigDecimal resultSet_getBigDecimal(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getBigDecimal(this, rs, columnIndex);
        }
        return rs.getResultSetRaw().getBigDecimal(columnIndex);
    }
    
    @Override
    public BigDecimal resultSet_getBigDecimal(final ResultSetProxy rs, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getBigDecimal(this, rs, columnLabel);
        }
        return rs.getResultSetRaw().getBigDecimal(columnLabel);
    }
    
    @Override
    public boolean resultSet_isBeforeFirst(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_isBeforeFirst(this, rs);
        }
        return rs.getResultSetRaw().isBeforeFirst();
    }
    
    @Override
    public boolean resultSet_isAfterLast(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_isAfterLast(this, rs);
        }
        return rs.getResultSetRaw().isAfterLast();
    }
    
    @Override
    public boolean resultSet_isFirst(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_isFirst(this, rs);
        }
        return rs.getResultSetRaw().isFirst();
    }
    
    @Override
    public boolean resultSet_isLast(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_isLast(this, rs);
        }
        return rs.getResultSetRaw().isLast();
    }
    
    @Override
    public void resultSet_beforeFirst(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_beforeFirst(this, rs);
            return;
        }
        rs.getResultSetRaw().beforeFirst();
    }
    
    @Override
    public void resultSet_afterLast(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_afterLast(this, rs);
            return;
        }
        rs.getResultSetRaw().afterLast();
    }
    
    @Override
    public boolean resultSet_first(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_first(this, rs);
        }
        return rs.getResultSetRaw().first();
    }
    
    @Override
    public boolean resultSet_last(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_last(this, rs);
        }
        return rs.getResultSetRaw().last();
    }
    
    @Override
    public int resultSet_getRow(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getRow(this, rs);
        }
        return rs.getResultSetRaw().getRow();
    }
    
    @Override
    public boolean resultSet_absolute(final ResultSetProxy rs, final int row) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_absolute(this, rs, row);
        }
        return rs.getResultSetRaw().absolute(row);
    }
    
    @Override
    public boolean resultSet_relative(final ResultSetProxy rs, final int rows) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_relative(this, rs, rows);
        }
        return rs.getResultSetRaw().relative(rows);
    }
    
    @Override
    public boolean resultSet_previous(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_previous(this, rs);
        }
        return rs.getResultSetRaw().previous();
    }
    
    @Override
    public void resultSet_setFetchDirection(final ResultSetProxy rs, final int direction) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_setFetchDirection(this, rs, direction);
            return;
        }
        rs.getResultSetRaw().setFetchDirection(direction);
    }
    
    @Override
    public int resultSet_getFetchDirection(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getFetchDirection(this, rs);
        }
        return rs.getResultSetRaw().getFetchDirection();
    }
    
    @Override
    public void resultSet_setFetchSize(final ResultSetProxy rs, final int rows) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_setFetchSize(this, rs, rows);
            return;
        }
        rs.getResultSetRaw().setFetchSize(rows);
    }
    
    @Override
    public int resultSet_getFetchSize(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getFetchSize(this, rs);
        }
        return rs.getResultSetRaw().getFetchSize();
    }
    
    @Override
    public int resultSet_getType(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getType(this, rs);
        }
        return rs.getResultSetRaw().getType();
    }
    
    @Override
    public int resultSet_getConcurrency(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getConcurrency(this, rs);
        }
        return rs.getResultSetRaw().getConcurrency();
    }
    
    @Override
    public boolean resultSet_rowUpdated(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_rowUpdated(this, rs);
        }
        return rs.getResultSetRaw().rowUpdated();
    }
    
    @Override
    public boolean resultSet_rowInserted(final ResultSetProxy rs) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_rowInserted(this, rs);
        }
        return rs.getResultSetRaw().rowInserted();
    }
    
    @Override
    public boolean resultSet_rowDeleted(final ResultSetProxy resultSet) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_rowDeleted(this, resultSet);
        }
        return resultSet.getResultSetRaw().rowDeleted();
    }
    
    @Override
    public void resultSet_updateNull(final ResultSetProxy rs, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNull(this, rs, columnIndex);
            return;
        }
        rs.getResultSetRaw().updateNull(columnIndex);
    }
    
    @Override
    public void resultSet_updateBoolean(final ResultSetProxy rs, final int columnIndex, final boolean x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBoolean(this, rs, columnIndex, x);
            return;
        }
        rs.getResultSetRaw().updateBoolean(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateByte(final ResultSetProxy resultSet, final int columnIndex, final byte x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateByte(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateByte(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateShort(final ResultSetProxy resultSet, final int columnIndex, final short x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateShort(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateShort(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateInt(final ResultSetProxy resultSet, final int columnIndex, final int x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateInt(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateInt(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateLong(final ResultSetProxy resultSet, final int columnIndex, final long x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateLong(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateLong(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateFloat(final ResultSetProxy resultSet, final int columnIndex, final float x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateFloat(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateFloat(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateDouble(final ResultSetProxy resultSet, final int columnIndex, final double x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateDouble(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateDouble(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateBigDecimal(final ResultSetProxy resultSet, final int columnIndex, final BigDecimal x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBigDecimal(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateBigDecimal(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateString(final ResultSetProxy resultSet, final int columnIndex, final String x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateString(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateString(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateBytes(final ResultSetProxy resultSet, final int columnIndex, final byte[] x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBytes(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateBytes(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateDate(final ResultSetProxy resultSet, final int columnIndex, final Date x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateDate(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateDate(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateTime(final ResultSetProxy resultSet, final int columnIndex, final Time x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateTime(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateTime(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateTimestamp(final ResultSetProxy resultSet, final int columnIndex, final Timestamp x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateTimestamp(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateTimestamp(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final ResultSetProxy resultSet, final int columnIndex, final InputStream x, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateAsciiStream(this, resultSet, columnIndex, x, length);
            return;
        }
        resultSet.getResultSetRaw().updateAsciiStream(columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final ResultSetProxy resultSet, final int columnIndex, final InputStream x, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBinaryStream(this, resultSet, columnIndex, x, length);
            return;
        }
        resultSet.getResultSetRaw().updateBinaryStream(columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final ResultSetProxy resultSet, final int columnIndex, final Reader x, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateCharacterStream(this, resultSet, columnIndex, x, length);
            return;
        }
        resultSet.getResultSetRaw().updateCharacterStream(columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateObject(final ResultSetProxy resultSet, final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateObject(this, resultSet, columnIndex, x, scaleOrLength);
            return;
        }
        resultSet.getResultSetRaw().updateObject(columnIndex, x, scaleOrLength);
    }
    
    @Override
    public void resultSet_updateObject(final ResultSetProxy resultSet, final int columnIndex, final Object x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateObject(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateObject(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateNull(final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNull(this, resultSet, columnLabel);
            return;
        }
        resultSet.getResultSetRaw().updateNull(columnLabel);
    }
    
    @Override
    public void resultSet_updateBoolean(final ResultSetProxy resultSet, final String columnLabel, final boolean x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBoolean(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateBoolean(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateByte(final ResultSetProxy resultSet, final String columnLabel, final byte x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateByte(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateByte(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateShort(final ResultSetProxy resultSet, final String columnLabel, final short x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateShort(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateShort(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateInt(final ResultSetProxy resultSet, final String columnLabel, final int x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateInt(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateInt(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateLong(final ResultSetProxy resultSet, final String columnLabel, final long x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateLong(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateLong(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateFloat(final ResultSetProxy resultSet, final String columnLabel, final float x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateFloat(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateFloat(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateDouble(final ResultSetProxy resultSet, final String columnLabel, final double x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateDouble(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateDouble(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateBigDecimal(final ResultSetProxy resultSet, final String columnLabel, final BigDecimal x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBigDecimal(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateBigDecimal(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateString(final ResultSetProxy resultSet, final String columnLabel, final String x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateString(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateString(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateBytes(final ResultSetProxy resultSet, final String columnLabel, final byte[] x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBytes(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateBytes(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateDate(final ResultSetProxy resultSet, final String columnLabel, final Date x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateDate(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateDate(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateTime(final ResultSetProxy resultSet, final String columnLabel, final Time x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateTime(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateTime(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateTimestamp(final ResultSetProxy resultSet, final String columnLabel, final Timestamp x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateTimestamp(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateTimestamp(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final ResultSetProxy resultSet, final String columnLabel, final InputStream x, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateAsciiStream(this, resultSet, columnLabel, x, length);
            return;
        }
        resultSet.getResultSetRaw().updateAsciiStream(columnLabel, x, length);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final ResultSetProxy resultSet, final String columnLabel, final InputStream x, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBinaryStream(this, resultSet, columnLabel, x, length);
            return;
        }
        resultSet.getResultSetRaw().updateBinaryStream(columnLabel, x, length);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final ResultSetProxy resultSet, final String columnLabel, final Reader reader, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateCharacterStream(this, resultSet, columnLabel, reader, length);
            return;
        }
        resultSet.getResultSetRaw().updateCharacterStream(columnLabel, reader, length);
    }
    
    @Override
    public void resultSet_updateObject(final ResultSetProxy resultSet, final String columnLabel, final Object x, final int scaleOrLength) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateObject(this, resultSet, columnLabel, x, scaleOrLength);
            return;
        }
        resultSet.getResultSetRaw().updateObject(columnLabel, x, scaleOrLength);
    }
    
    @Override
    public void resultSet_updateObject(final ResultSetProxy resultSet, final String columnLabel, final Object x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateObject(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateObject(columnLabel, x);
    }
    
    @Override
    public void resultSet_insertRow(final ResultSetProxy resultSet) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_insertRow(this, resultSet);
            return;
        }
        resultSet.getResultSetRaw().insertRow();
    }
    
    @Override
    public void resultSet_updateRow(final ResultSetProxy resultSet) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateRow(this, resultSet);
            return;
        }
        resultSet.getResultSetRaw().updateRow();
    }
    
    @Override
    public void resultSet_deleteRow(final ResultSetProxy resultSet) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_deleteRow(this, resultSet);
            return;
        }
        resultSet.getResultSetRaw().deleteRow();
    }
    
    @Override
    public void resultSet_refreshRow(final ResultSetProxy resultSet) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_refreshRow(this, resultSet);
            return;
        }
        resultSet.getResultSetRaw().refreshRow();
    }
    
    @Override
    public void resultSet_cancelRowUpdates(final ResultSetProxy resultSet) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_cancelRowUpdates(this, resultSet);
            return;
        }
        resultSet.getResultSetRaw().cancelRowUpdates();
    }
    
    @Override
    public void resultSet_moveToInsertRow(final ResultSetProxy resultSet) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_moveToInsertRow(this, resultSet);
            return;
        }
        resultSet.getResultSetRaw().moveToInsertRow();
    }
    
    @Override
    public void resultSet_moveToCurrentRow(final ResultSetProxy resultSet) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_moveToCurrentRow(this, resultSet);
            return;
        }
        resultSet.getResultSetRaw().moveToCurrentRow();
    }
    
    @Override
    public Statement resultSet_getStatement(final ResultSetProxy resultSet) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getStatement(this, resultSet);
        }
        return resultSet.getResultSetRaw().getStatement();
    }
    
    @Override
    public Object resultSet_getObject(final ResultSetProxy resultSet, final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getObject(this, resultSet, columnIndex, map);
        }
        final Object obj = resultSet.getResultSetRaw().getObject(columnIndex, map);
        if (obj instanceof ResultSet) {
            final StatementProxy statement = resultSet.getStatementProxy();
            return new ResultSetProxyImpl(statement, (ResultSet)obj, this.dataSource.createResultSetId(), statement.getLastExecuteSql());
        }
        if (obj instanceof Clob) {
            return this.wrap(resultSet.getStatementProxy(), (Clob)obj);
        }
        return obj;
    }
    
    @Override
    public Ref resultSet_getRef(final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getRef(this, resultSet, columnIndex);
        }
        return resultSet.getResultSetRaw().getRef(columnIndex);
    }
    
    @Override
    public Blob resultSet_getBlob(final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getBlob(this, resultSet, columnIndex);
        }
        return resultSet.getResultSetRaw().getBlob(columnIndex);
    }
    
    @Override
    public Clob resultSet_getClob(final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getClob(this, resultSet, columnIndex);
        }
        final Clob clob = resultSet.getResultSetRaw().getClob(columnIndex);
        return this.wrap(resultSet.getStatementProxy(), clob);
    }
    
    @Override
    public Array resultSet_getArray(final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getArray(this, resultSet, columnIndex);
        }
        final Array rawArray = resultSet.getResultSetRaw().getArray(columnIndex);
        return rawArray;
    }
    
    @Override
    public Object resultSet_getObject(final ResultSetProxy resultSet, final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getObject(this, resultSet, columnLabel, map);
        }
        final Object obj = resultSet.getResultSetRaw().getObject(columnLabel, map);
        if (obj instanceof ResultSet) {
            final StatementProxy statement = resultSet.getStatementProxy();
            return new ResultSetProxyImpl(statement, (ResultSet)obj, this.dataSource.createResultSetId(), statement.getLastExecuteSql());
        }
        if (obj instanceof Clob) {
            return this.wrap(resultSet.getStatementProxy(), (Clob)obj);
        }
        return obj;
    }
    
    @Override
    public Ref resultSet_getRef(final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getRef(this, resultSet, columnLabel);
        }
        return resultSet.getResultSetRaw().getRef(columnLabel);
    }
    
    @Override
    public Blob resultSet_getBlob(final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getBlob(this, resultSet, columnLabel);
        }
        return resultSet.getResultSetRaw().getBlob(columnLabel);
    }
    
    @Override
    public Clob resultSet_getClob(final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getClob(this, resultSet, columnLabel);
        }
        final Clob clob = resultSet.getResultSetRaw().getClob(columnLabel);
        return this.wrap(resultSet.getStatementProxy(), clob);
    }
    
    @Override
    public Array resultSet_getArray(final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getArray(this, resultSet, columnLabel);
        }
        return resultSet.getResultSetRaw().getArray(columnLabel);
    }
    
    @Override
    public Date resultSet_getDate(final ResultSetProxy resultSet, final int columnIndex, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getDate(this, resultSet, columnIndex, cal);
        }
        return resultSet.getResultSetRaw().getDate(columnIndex, cal);
    }
    
    @Override
    public Date resultSet_getDate(final ResultSetProxy resultSet, final String columnLabel, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getDate(this, resultSet, columnLabel, cal);
        }
        return resultSet.getResultSetRaw().getDate(columnLabel, cal);
    }
    
    @Override
    public Time resultSet_getTime(final ResultSetProxy resultSet, final int columnIndex, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getTime(this, resultSet, columnIndex, cal);
        }
        return resultSet.getResultSetRaw().getTime(columnIndex, cal);
    }
    
    @Override
    public Time resultSet_getTime(final ResultSetProxy resultSet, final String columnLabel, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getTime(this, resultSet, columnLabel, cal);
        }
        return resultSet.getResultSetRaw().getTime(columnLabel, cal);
    }
    
    @Override
    public Timestamp resultSet_getTimestamp(final ResultSetProxy resultSet, final int columnIndex, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getTimestamp(this, resultSet, columnIndex, cal);
        }
        return resultSet.getResultSetRaw().getTimestamp(columnIndex, cal);
    }
    
    @Override
    public Timestamp resultSet_getTimestamp(final ResultSetProxy resultSet, final String columnLabel, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getTimestamp(this, resultSet, columnLabel, cal);
        }
        return resultSet.getResultSetRaw().getTimestamp(columnLabel, cal);
    }
    
    @Override
    public URL resultSet_getURL(final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getURL(this, resultSet, columnIndex);
        }
        return resultSet.getResultSetRaw().getURL(columnIndex);
    }
    
    @Override
    public URL resultSet_getURL(final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getURL(this, resultSet, columnLabel);
        }
        return resultSet.getResultSetRaw().getURL(columnLabel);
    }
    
    @Override
    public void resultSet_updateRef(final ResultSetProxy resultSet, final int columnIndex, final Ref x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateRef(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateRef(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateRef(final ResultSetProxy resultSet, final String columnLabel, final Ref x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateRef(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateRef(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateBlob(final ResultSetProxy resultSet, final int columnIndex, final Blob x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBlob(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateBlob(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateBlob(final ResultSetProxy resultSet, final String columnLabel, final Blob x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBlob(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateBlob(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateClob(final ResultSetProxy resultSet, final int columnIndex, final Clob x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateClob(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateClob(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateClob(final ResultSetProxy resultSet, final String columnLabel, final Clob x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateClob(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateClob(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateArray(final ResultSetProxy resultSet, final int columnIndex, final Array x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateArray(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateArray(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateArray(final ResultSetProxy resultSet, final String columnLabel, final Array x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateArray(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateArray(columnLabel, x);
    }
    
    @Override
    public RowId resultSet_getRowId(final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getRowId(this, resultSet, columnIndex);
        }
        return resultSet.getResultSetRaw().getRowId(columnIndex);
    }
    
    @Override
    public RowId resultSet_getRowId(final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getRowId(this, resultSet, columnLabel);
        }
        return resultSet.getResultSetRaw().getRowId(columnLabel);
    }
    
    @Override
    public void resultSet_updateRowId(final ResultSetProxy resultSet, final int columnIndex, final RowId x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateRowId(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateRowId(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateRowId(final ResultSetProxy resultSet, final String columnLabel, final RowId x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateRowId(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateRowId(columnLabel, x);
    }
    
    @Override
    public int resultSet_getHoldability(final ResultSetProxy resultSet) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getHoldability(this, resultSet);
        }
        return resultSet.getResultSetRaw().getHoldability();
    }
    
    @Override
    public boolean resultSet_isClosed(final ResultSetProxy resultSet) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_isClosed(this, resultSet);
        }
        return resultSet.getResultSetRaw().isClosed();
    }
    
    @Override
    public void resultSet_updateNString(final ResultSetProxy resultSet, final int columnIndex, final String x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNString(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateNString(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateNString(final ResultSetProxy resultSet, final String columnLabel, final String nString) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNString(this, resultSet, columnLabel, nString);
            return;
        }
        resultSet.getResultSetRaw().updateNString(columnLabel, nString);
    }
    
    @Override
    public void resultSet_updateNClob(final ResultSetProxy resultSet, final int columnIndex, final NClob x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNClob(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateNClob(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateNClob(final ResultSetProxy resultSet, final String columnLabel, final NClob x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNClob(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateNClob(columnLabel, x);
    }
    
    @Override
    public NClob resultSet_getNClob(final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getNClob(this, resultSet, columnIndex);
        }
        final NClob nclob = resultSet.getResultSetRaw().getNClob(columnIndex);
        return this.wrap(resultSet.getStatementProxy().getConnectionProxy(), nclob);
    }
    
    @Override
    public NClob resultSet_getNClob(final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getNClob(this, resultSet, columnLabel);
        }
        final NClob nclob = resultSet.getResultSetRaw().getNClob(columnLabel);
        return this.wrap(resultSet.getStatementProxy().getConnectionProxy(), nclob);
    }
    
    @Override
    public SQLXML resultSet_getSQLXML(final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getSQLXML(this, resultSet, columnIndex);
        }
        return resultSet.getResultSetRaw().getSQLXML(columnIndex);
    }
    
    @Override
    public SQLXML resultSet_getSQLXML(final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getSQLXML(this, resultSet, columnLabel);
        }
        return resultSet.getResultSetRaw().getSQLXML(columnLabel);
    }
    
    @Override
    public void resultSet_updateSQLXML(final ResultSetProxy resultSet, final int columnIndex, final SQLXML xmlObject) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateSQLXML(this, resultSet, columnIndex, xmlObject);
            return;
        }
        resultSet.getResultSetRaw().updateSQLXML(columnIndex, xmlObject);
    }
    
    @Override
    public void resultSet_updateSQLXML(final ResultSetProxy resultSet, final String columnLabel, final SQLXML xmlObject) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateSQLXML(this, resultSet, columnLabel, xmlObject);
            return;
        }
        resultSet.getResultSetRaw().updateSQLXML(columnLabel, xmlObject);
    }
    
    @Override
    public String resultSet_getNString(final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getNString(this, resultSet, columnIndex);
        }
        return resultSet.getResultSetRaw().getNString(columnIndex);
    }
    
    @Override
    public String resultSet_getNString(final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getNString(this, resultSet, columnLabel);
        }
        return resultSet.getResultSetRaw().getNString(columnLabel);
    }
    
    @Override
    public Reader resultSet_getNCharacterStream(final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getNCharacterStream(this, resultSet, columnIndex);
        }
        return resultSet.getResultSetRaw().getNCharacterStream(columnIndex);
    }
    
    @Override
    public Reader resultSet_getNCharacterStream(final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSet_getNCharacterStream(this, resultSet, columnLabel);
        }
        return resultSet.getResultSetRaw().getNCharacterStream(columnLabel);
    }
    
    @Override
    public void resultSet_updateNCharacterStream(final ResultSetProxy resultSet, final int columnIndex, final Reader x, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNCharacterStream(this, resultSet, columnIndex, x, length);
            return;
        }
        resultSet.getResultSetRaw().updateNCharacterStream(columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateNCharacterStream(final ResultSetProxy resultSet, final String columnLabel, final Reader reader, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNCharacterStream(this, resultSet, columnLabel, reader, length);
            return;
        }
        resultSet.getResultSetRaw().updateNCharacterStream(columnLabel, reader, length);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final ResultSetProxy resultSet, final int columnIndex, final InputStream x, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateAsciiStream(this, resultSet, columnIndex, x, length);
            return;
        }
        resultSet.getResultSetRaw().updateAsciiStream(columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final ResultSetProxy resultSet, final int columnIndex, final InputStream x, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBinaryStream(this, resultSet, columnIndex, x, length);
            return;
        }
        resultSet.getResultSetRaw().updateBinaryStream(columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final ResultSetProxy resultSet, final int columnIndex, final Reader x, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateCharacterStream(this, resultSet, columnIndex, x, length);
            return;
        }
        resultSet.getResultSetRaw().updateCharacterStream(columnIndex, x, length);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final ResultSetProxy resultSet, final String columnLabel, final InputStream x, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateAsciiStream(this, resultSet, columnLabel, x, length);
            return;
        }
        resultSet.getResultSetRaw().updateAsciiStream(columnLabel, x, length);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final ResultSetProxy resultSet, final String columnLabel, final InputStream x, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBinaryStream(this, resultSet, columnLabel, x, length);
            return;
        }
        resultSet.getResultSetRaw().updateBinaryStream(columnLabel, x, length);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final ResultSetProxy resultSet, final String columnLabel, final Reader reader, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateCharacterStream(this, resultSet, columnLabel, reader, length);
            return;
        }
        resultSet.getResultSetRaw().updateCharacterStream(columnLabel, reader, length);
    }
    
    @Override
    public void resultSet_updateBlob(final ResultSetProxy resultSet, final int columnIndex, final InputStream inputStream, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBlob(this, resultSet, columnIndex, inputStream, length);
            return;
        }
        resultSet.getResultSetRaw().updateBlob(columnIndex, inputStream, length);
    }
    
    @Override
    public void resultSet_updateBlob(final ResultSetProxy resultSet, final String columnLabel, final InputStream inputStream, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBlob(this, resultSet, columnLabel, inputStream, length);
            return;
        }
        resultSet.getResultSetRaw().updateBlob(columnLabel, inputStream, length);
    }
    
    @Override
    public void resultSet_updateClob(final ResultSetProxy resultSet, final int columnIndex, final Reader reader, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateClob(this, resultSet, columnIndex, reader, length);
            return;
        }
        resultSet.getResultSetRaw().updateClob(columnIndex, reader, length);
    }
    
    @Override
    public void resultSet_updateClob(final ResultSetProxy resultSet, final String columnLabel, final Reader reader, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateClob(this, resultSet, columnLabel, reader, length);
            return;
        }
        resultSet.getResultSetRaw().updateClob(columnLabel, reader, length);
    }
    
    @Override
    public void resultSet_updateNClob(final ResultSetProxy resultSet, final int columnIndex, final Reader reader, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNClob(this, resultSet, columnIndex, reader, length);
            return;
        }
        resultSet.getResultSetRaw().updateNClob(columnIndex, reader, length);
    }
    
    @Override
    public void resultSet_updateNClob(final ResultSetProxy resultSet, final String columnLabel, final Reader reader, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNClob(this, resultSet, columnLabel, reader, length);
            return;
        }
        resultSet.getResultSetRaw().updateNClob(columnLabel, reader, length);
    }
    
    @Override
    public void resultSet_updateNCharacterStream(final ResultSetProxy resultSet, final int columnIndex, final Reader x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNCharacterStream(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateNCharacterStream(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateNCharacterStream(final ResultSetProxy resultSet, final String columnLabel, final Reader reader) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNCharacterStream(this, resultSet, columnLabel, reader);
            return;
        }
        resultSet.getResultSetRaw().updateNCharacterStream(columnLabel, reader);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final ResultSetProxy resultSet, final int columnIndex, final InputStream x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateAsciiStream(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateAsciiStream(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final ResultSetProxy resultSet, final int columnIndex, final InputStream x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBinaryStream(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateBinaryStream(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final ResultSetProxy resultSet, final int columnIndex, final Reader x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateCharacterStream(this, resultSet, columnIndex, x);
            return;
        }
        resultSet.getResultSetRaw().updateCharacterStream(columnIndex, x);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final ResultSetProxy resultSet, final String columnLabel, final InputStream x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateAsciiStream(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateAsciiStream(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final ResultSetProxy resultSet, final String columnLabel, final InputStream x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBinaryStream(this, resultSet, columnLabel, x);
            return;
        }
        resultSet.getResultSetRaw().updateBinaryStream(columnLabel, x);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final ResultSetProxy resultSet, final String columnLabel, final Reader reader) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateCharacterStream(this, resultSet, columnLabel, reader);
            return;
        }
        resultSet.getResultSetRaw().updateCharacterStream(columnLabel, reader);
    }
    
    @Override
    public void resultSet_updateBlob(final ResultSetProxy resultSet, final int columnIndex, final InputStream inputStream) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBlob(this, resultSet, columnIndex, inputStream);
            return;
        }
        resultSet.getResultSetRaw().updateBlob(columnIndex, inputStream);
    }
    
    @Override
    public void resultSet_updateBlob(final ResultSetProxy resultSet, final String columnLabel, final InputStream inputStream) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateBlob(this, resultSet, columnLabel, inputStream);
            return;
        }
        resultSet.getResultSetRaw().updateBlob(columnLabel, inputStream);
    }
    
    @Override
    public void resultSet_updateClob(final ResultSetProxy resultSet, final int columnIndex, final Reader reader) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateClob(this, resultSet, columnIndex, reader);
            return;
        }
        resultSet.getResultSetRaw().updateClob(columnIndex, reader);
    }
    
    @Override
    public void resultSet_updateClob(final ResultSetProxy resultSet, final String columnLabel, final Reader reader) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateClob(this, resultSet, columnLabel, reader);
            return;
        }
        resultSet.getResultSetRaw().updateClob(columnLabel, reader);
    }
    
    @Override
    public void resultSet_updateNClob(final ResultSetProxy resultSet, final int columnIndex, final Reader reader) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNClob(this, resultSet, columnIndex, reader);
            return;
        }
        resultSet.getResultSetRaw().updateNClob(columnIndex, reader);
    }
    
    @Override
    public void resultSet_updateNClob(final ResultSetProxy resultSet, final String columnLabel, final Reader reader) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().resultSet_updateNClob(this, resultSet, columnLabel, reader);
            return;
        }
        resultSet.getResultSetRaw().updateNClob(columnLabel, reader);
    }
    
    @Override
    public ResultSetProxy statement_executeQuery(final StatementProxy statement, final String sql) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_executeQuery(this, statement, sql);
        }
        final ResultSet resultSet = statement.getRawObject().executeQuery(sql);
        if (resultSet == null) {
            return null;
        }
        return new ResultSetProxyImpl(statement, resultSet, this.dataSource.createResultSetId(), statement.getLastExecuteSql());
    }
    
    @Override
    public int statement_executeUpdate(final StatementProxy statement, final String sql) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_executeUpdate(this, statement, sql);
        }
        return statement.getRawObject().executeUpdate(sql);
    }
    
    @Override
    public void statement_close(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_close(this, statement);
            return;
        }
        statement.getRawObject().close();
    }
    
    @Override
    public int statement_getMaxFieldSize(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getMaxFieldSize(this, statement);
        }
        return statement.getRawObject().getMaxFieldSize();
    }
    
    @Override
    public void statement_setMaxFieldSize(final StatementProxy statement, final int max) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_setMaxFieldSize(this, statement, max);
            return;
        }
        statement.getRawObject().setMaxFieldSize(max);
    }
    
    @Override
    public int statement_getMaxRows(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getMaxRows(this, statement);
        }
        return statement.getRawObject().getMaxRows();
    }
    
    @Override
    public void statement_setMaxRows(final StatementProxy statement, final int max) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_setMaxRows(this, statement, max);
            return;
        }
        statement.getRawObject().setMaxRows(max);
    }
    
    @Override
    public void statement_setEscapeProcessing(final StatementProxy statement, final boolean enable) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_setEscapeProcessing(this, statement, enable);
            return;
        }
        statement.getRawObject().setEscapeProcessing(enable);
    }
    
    @Override
    public int statement_getQueryTimeout(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getQueryTimeout(this, statement);
        }
        return statement.getRawObject().getQueryTimeout();
    }
    
    @Override
    public void statement_setQueryTimeout(final StatementProxy statement, final int seconds) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_setQueryTimeout(this, statement, seconds);
            return;
        }
        statement.getRawObject().setQueryTimeout(seconds);
    }
    
    @Override
    public void statement_cancel(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_cancel(this, statement);
            return;
        }
        statement.getRawObject().cancel();
    }
    
    @Override
    public SQLWarning statement_getWarnings(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getWarnings(this, statement);
        }
        return statement.getRawObject().getWarnings();
    }
    
    @Override
    public void statement_clearWarnings(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_clearWarnings(this, statement);
            return;
        }
        statement.getRawObject().clearWarnings();
    }
    
    @Override
    public void statement_setCursorName(final StatementProxy statement, final String name) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_setCursorName(this, statement, name);
            return;
        }
        statement.getRawObject().setCursorName(name);
    }
    
    @Override
    public boolean statement_execute(final StatementProxy statement, final String sql) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_execute(this, statement, sql);
        }
        return statement.getRawObject().execute(sql);
    }
    
    @Override
    public ResultSetProxy statement_getResultSet(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getResultSet(this, statement);
        }
        final ResultSet resultSet = statement.getRawObject().getResultSet();
        if (resultSet == null) {
            return null;
        }
        return new ResultSetProxyImpl(statement, resultSet, this.dataSource.createResultSetId(), statement.getLastExecuteSql());
    }
    
    @Override
    public int statement_getUpdateCount(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getUpdateCount(this, statement);
        }
        return statement.getRawObject().getUpdateCount();
    }
    
    @Override
    public boolean statement_getMoreResults(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getMoreResults(this, statement);
        }
        return statement.getRawObject().getMoreResults();
    }
    
    @Override
    public void statement_setFetchDirection(final StatementProxy statement, final int direction) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_setFetchDirection(this, statement, direction);
            return;
        }
        statement.getRawObject().setFetchDirection(direction);
    }
    
    @Override
    public int statement_getFetchDirection(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getFetchDirection(this, statement);
        }
        return statement.getRawObject().getFetchDirection();
    }
    
    @Override
    public void statement_setFetchSize(final StatementProxy statement, final int rows) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_setFetchSize(this, statement, rows);
            return;
        }
        statement.getRawObject().setFetchSize(rows);
    }
    
    @Override
    public int statement_getFetchSize(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getFetchSize(this, statement);
        }
        return statement.getRawObject().getFetchSize();
    }
    
    @Override
    public int statement_getResultSetConcurrency(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getResultSetConcurrency(this, statement);
        }
        return statement.getRawObject().getResultSetConcurrency();
    }
    
    @Override
    public int statement_getResultSetType(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getResultSetType(this, statement);
        }
        return statement.getRawObject().getResultSetType();
    }
    
    @Override
    public void statement_addBatch(final StatementProxy statement, final String sql) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_addBatch(this, statement, sql);
            return;
        }
        statement.getRawObject().addBatch(sql);
    }
    
    @Override
    public void statement_clearBatch(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_clearBatch(this, statement);
            return;
        }
        statement.getRawObject().clearBatch();
    }
    
    @Override
    public int[] statement_executeBatch(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_executeBatch(this, statement);
        }
        return statement.getRawObject().executeBatch();
    }
    
    @Override
    public Connection statement_getConnection(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getConnection(this, statement);
        }
        return statement.getRawObject().getConnection();
    }
    
    @Override
    public boolean statement_getMoreResults(final StatementProxy statement, final int current) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getMoreResults(this, statement, current);
        }
        return statement.getRawObject().getMoreResults(current);
    }
    
    @Override
    public ResultSetProxy statement_getGeneratedKeys(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getGeneratedKeys(this, statement);
        }
        final ResultSet resultSet = statement.getRawObject().getGeneratedKeys();
        if (resultSet == null) {
            return null;
        }
        return new ResultSetProxyImpl(statement, resultSet, this.dataSource.createResultSetId(), statement.getLastExecuteSql());
    }
    
    @Override
    public int statement_executeUpdate(final StatementProxy statement, final String sql, final int autoGeneratedKeys) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_executeUpdate(this, statement, sql, autoGeneratedKeys);
        }
        return statement.getRawObject().executeUpdate(sql, autoGeneratedKeys);
    }
    
    @Override
    public int statement_executeUpdate(final StatementProxy statement, final String sql, final int[] columnIndexes) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_executeUpdate(this, statement, sql, columnIndexes);
        }
        return statement.getRawObject().executeUpdate(sql, columnIndexes);
    }
    
    @Override
    public int statement_executeUpdate(final StatementProxy statement, final String sql, final String[] columnNames) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_executeUpdate(this, statement, sql, columnNames);
        }
        return statement.getRawObject().executeUpdate(sql, columnNames);
    }
    
    @Override
    public boolean statement_execute(final StatementProxy statement, final String sql, final int autoGeneratedKeys) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_execute(this, statement, sql, autoGeneratedKeys);
        }
        return statement.getRawObject().execute(sql, autoGeneratedKeys);
    }
    
    @Override
    public boolean statement_execute(final StatementProxy statement, final String sql, final int[] columnIndexes) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_execute(this, statement, sql, columnIndexes);
        }
        return statement.getRawObject().execute(sql, columnIndexes);
    }
    
    @Override
    public boolean statement_execute(final StatementProxy statement, final String sql, final String[] columnNames) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_execute(this, statement, sql, columnNames);
        }
        return statement.getRawObject().execute(sql, columnNames);
    }
    
    @Override
    public int statement_getResultSetHoldability(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_getResultSetHoldability(this, statement);
        }
        return statement.getRawObject().getResultSetHoldability();
    }
    
    @Override
    public boolean statement_isClosed(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_isClosed(this, statement);
        }
        return statement.getRawObject().isClosed();
    }
    
    @Override
    public void statement_setPoolable(final StatementProxy statement, final boolean poolable) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().statement_setPoolable(this, statement, poolable);
            return;
        }
        statement.getRawObject().setPoolable(poolable);
    }
    
    @Override
    public boolean statement_isPoolable(final StatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().statement_isPoolable(this, statement);
        }
        return statement.getRawObject().isPoolable();
    }
    
    @Override
    public ResultSetProxy preparedStatement_executeQuery(final PreparedStatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().preparedStatement_executeQuery(this, statement);
        }
        final ResultSet resultSet = statement.getRawObject().executeQuery();
        if (resultSet == null) {
            return null;
        }
        return new ResultSetProxyImpl(statement, resultSet, this.dataSource.createResultSetId(), statement.getLastExecuteSql());
    }
    
    @Override
    public int preparedStatement_executeUpdate(final PreparedStatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().preparedStatement_executeUpdate(this, statement);
        }
        return statement.getRawObject().executeUpdate();
    }
    
    @Override
    public void preparedStatement_setNull(final PreparedStatementProxy statement, final int parameterIndex, final int sqlType) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setNull(this, statement, parameterIndex, sqlType);
            return;
        }
        statement.getRawObject().setNull(parameterIndex, sqlType);
    }
    
    @Override
    public void preparedStatement_setBoolean(final PreparedStatementProxy statement, final int parameterIndex, final boolean x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setBoolean(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setBoolean(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setByte(final PreparedStatementProxy statement, final int parameterIndex, final byte x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setByte(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setByte(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setShort(final PreparedStatementProxy statement, final int parameterIndex, final short x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setShort(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setShort(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setInt(final PreparedStatementProxy statement, final int parameterIndex, final int x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setInt(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setInt(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setLong(final PreparedStatementProxy statement, final int parameterIndex, final long x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setLong(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setLong(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setFloat(final PreparedStatementProxy statement, final int parameterIndex, final float x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setFloat(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setFloat(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setDouble(final PreparedStatementProxy statement, final int parameterIndex, final double x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setDouble(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setDouble(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setBigDecimal(final PreparedStatementProxy statement, final int parameterIndex, final BigDecimal x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setBigDecimal(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setBigDecimal(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setString(final PreparedStatementProxy statement, final int parameterIndex, final String x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setString(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setString(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setBytes(final PreparedStatementProxy statement, final int parameterIndex, final byte[] x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setBytes(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setBytes(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setDate(final PreparedStatementProxy statement, final int parameterIndex, final Date x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setDate(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setDate(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setTime(final PreparedStatementProxy statement, final int parameterIndex, final Time x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setTime(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setTime(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setTimestamp(final PreparedStatementProxy statement, final int parameterIndex, final Timestamp x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setTimestamp(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setTimestamp(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setAsciiStream(final PreparedStatementProxy statement, final int parameterIndex, final InputStream x, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setAsciiStream(this, statement, parameterIndex, x, length);
            return;
        }
        statement.getRawObject().setAsciiStream(parameterIndex, x, length);
    }
    
    @Override
    public void preparedStatement_setUnicodeStream(final PreparedStatementProxy statement, final int parameterIndex, final InputStream x, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setUnicodeStream(this, statement, parameterIndex, x, length);
            return;
        }
        statement.getRawObject().setUnicodeStream(parameterIndex, x, length);
    }
    
    @Override
    public void preparedStatement_setBinaryStream(final PreparedStatementProxy statement, final int parameterIndex, final InputStream x, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setBinaryStream(this, statement, parameterIndex, x, length);
            return;
        }
        statement.getRawObject().setBinaryStream(parameterIndex, x, length);
    }
    
    @Override
    public void preparedStatement_clearParameters(final PreparedStatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_clearParameters(this, statement);
            return;
        }
        statement.getRawObject().clearParameters();
    }
    
    @Override
    public void preparedStatement_setObject(final PreparedStatementProxy statement, final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setObject(this, statement, parameterIndex, x, targetSqlType);
            return;
        }
        statement.getRawObject().setObject(parameterIndex, x, targetSqlType);
    }
    
    @Override
    public void preparedStatement_setObject(final PreparedStatementProxy statement, final int parameterIndex, final Object x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setObject(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setObject(parameterIndex, x);
    }
    
    @Override
    public boolean preparedStatement_execute(final PreparedStatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().preparedStatement_execute(this, statement);
        }
        return statement.getRawObject().execute();
    }
    
    @Override
    public void preparedStatement_addBatch(final PreparedStatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_addBatch(this, statement);
            return;
        }
        statement.getRawObject().addBatch();
    }
    
    @Override
    public void preparedStatement_setCharacterStream(final PreparedStatementProxy statement, final int parameterIndex, final Reader reader, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setCharacterStream(this, statement, parameterIndex, reader, length);
            return;
        }
        statement.getRawObject().setCharacterStream(parameterIndex, reader, length);
    }
    
    @Override
    public void preparedStatement_setRef(final PreparedStatementProxy statement, final int parameterIndex, final Ref x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setRef(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setRef(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setBlob(final PreparedStatementProxy statement, final int parameterIndex, final Blob x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setBlob(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setBlob(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setClob(final PreparedStatementProxy statement, final int parameterIndex, Clob x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setClob(this, statement, parameterIndex, x);
            return;
        }
        if (x instanceof ClobProxy) {
            x = ((ClobProxy)x).getRawClob();
        }
        statement.getRawObject().setClob(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setArray(final PreparedStatementProxy statement, final int parameterIndex, final Array x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setArray(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setArray(parameterIndex, x);
    }
    
    @Override
    public ResultSetMetaData preparedStatement_getMetaData(final PreparedStatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().preparedStatement_getMetaData(this, statement);
        }
        return statement.getRawObject().getMetaData();
    }
    
    @Override
    public void preparedStatement_setDate(final PreparedStatementProxy statement, final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setDate(this, statement, parameterIndex, x, cal);
            return;
        }
        statement.getRawObject().setDate(parameterIndex, x, cal);
    }
    
    @Override
    public void preparedStatement_setTime(final PreparedStatementProxy statement, final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setTime(this, statement, parameterIndex, x, cal);
            return;
        }
        statement.getRawObject().setTime(parameterIndex, x, cal);
    }
    
    @Override
    public void preparedStatement_setTimestamp(final PreparedStatementProxy statement, final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setTimestamp(this, statement, parameterIndex, x, cal);
            return;
        }
        statement.getRawObject().setTimestamp(parameterIndex, x, cal);
    }
    
    @Override
    public void preparedStatement_setNull(final PreparedStatementProxy statement, final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setNull(this, statement, parameterIndex, sqlType, typeName);
            return;
        }
        statement.getRawObject().setNull(parameterIndex, sqlType, typeName);
    }
    
    @Override
    public void preparedStatement_setURL(final PreparedStatementProxy statement, final int parameterIndex, final URL x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setURL(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setURL(parameterIndex, x);
    }
    
    @Override
    public ParameterMetaData preparedStatement_getParameterMetaData(final PreparedStatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().preparedStatement_getParameterMetaData(this, statement);
        }
        return statement.getRawObject().getParameterMetaData();
    }
    
    @Override
    public void preparedStatement_setRowId(final PreparedStatementProxy statement, final int parameterIndex, final RowId x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setRowId(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setRowId(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setNString(final PreparedStatementProxy statement, final int parameterIndex, final String value) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setNString(this, statement, parameterIndex, value);
            return;
        }
        statement.getRawObject().setNString(parameterIndex, value);
    }
    
    @Override
    public void preparedStatement_setNCharacterStream(final PreparedStatementProxy statement, final int parameterIndex, final Reader value, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setNCharacterStream(this, statement, parameterIndex, value, length);
            return;
        }
        statement.getRawObject().setNCharacterStream(parameterIndex, value, length);
    }
    
    @Override
    public void preparedStatement_setNClob(final PreparedStatementProxy statement, final int parameterIndex, NClob x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setNClob(this, statement, parameterIndex, x);
            return;
        }
        if (x instanceof NClobProxy) {
            x = ((NClobProxy)x).getRawNClob();
        }
        statement.getRawObject().setNClob(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setClob(final PreparedStatementProxy statement, final int parameterIndex, final Reader reader, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setClob(this, statement, parameterIndex, reader, length);
            return;
        }
        statement.getRawObject().setClob(parameterIndex, reader, length);
    }
    
    @Override
    public void preparedStatement_setBlob(final PreparedStatementProxy statement, final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setBlob(this, statement, parameterIndex, inputStream, length);
            return;
        }
        statement.getRawObject().setBlob(parameterIndex, inputStream, length);
    }
    
    @Override
    public void preparedStatement_setNClob(final PreparedStatementProxy statement, final int parameterIndex, final Reader reader, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setNClob(this, statement, parameterIndex, reader, length);
            return;
        }
        statement.getRawObject().setNClob(parameterIndex, reader, length);
    }
    
    @Override
    public void preparedStatement_setSQLXML(final PreparedStatementProxy statement, final int parameterIndex, final SQLXML xmlObject) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setSQLXML(this, statement, parameterIndex, xmlObject);
            return;
        }
        statement.getRawObject().setSQLXML(parameterIndex, xmlObject);
    }
    
    @Override
    public void preparedStatement_setObject(final PreparedStatementProxy statement, final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setObject(this, statement, parameterIndex, x, targetSqlType, scaleOrLength);
            return;
        }
        statement.getRawObject().setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }
    
    @Override
    public void preparedStatement_setAsciiStream(final PreparedStatementProxy statement, final int parameterIndex, final InputStream x, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setAsciiStream(this, statement, parameterIndex, x, length);
            return;
        }
        statement.getRawObject().setAsciiStream(parameterIndex, x, length);
    }
    
    @Override
    public void preparedStatement_setBinaryStream(final PreparedStatementProxy statement, final int parameterIndex, final InputStream x, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setBinaryStream(this, statement, parameterIndex, x, length);
            return;
        }
        statement.getRawObject().setBinaryStream(parameterIndex, x, length);
    }
    
    @Override
    public void preparedStatement_setCharacterStream(final PreparedStatementProxy statement, final int parameterIndex, final Reader reader, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setCharacterStream(this, statement, parameterIndex, reader, length);
            return;
        }
        statement.getRawObject().setCharacterStream(parameterIndex, reader, length);
    }
    
    @Override
    public void preparedStatement_setAsciiStream(final PreparedStatementProxy statement, final int parameterIndex, final InputStream x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setAsciiStream(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setAsciiStream(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setBinaryStream(final PreparedStatementProxy statement, final int parameterIndex, final InputStream x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setBinaryStream(this, statement, parameterIndex, x);
            return;
        }
        statement.getRawObject().setBinaryStream(parameterIndex, x);
    }
    
    @Override
    public void preparedStatement_setCharacterStream(final PreparedStatementProxy statement, final int parameterIndex, final Reader reader) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setCharacterStream(this, statement, parameterIndex, reader);
            return;
        }
        statement.getRawObject().setCharacterStream(parameterIndex, reader);
    }
    
    @Override
    public void preparedStatement_setNCharacterStream(final PreparedStatementProxy statement, final int parameterIndex, final Reader value) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setNCharacterStream(this, statement, parameterIndex, value);
            return;
        }
        statement.getRawObject().setNCharacterStream(parameterIndex, value);
    }
    
    @Override
    public void preparedStatement_setClob(final PreparedStatementProxy statement, final int parameterIndex, final Reader reader) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setClob(this, statement, parameterIndex, reader);
            return;
        }
        statement.getRawObject().setClob(parameterIndex, reader);
    }
    
    @Override
    public void preparedStatement_setBlob(final PreparedStatementProxy statement, final int parameterIndex, final InputStream inputStream) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setBlob(this, statement, parameterIndex, inputStream);
            return;
        }
        statement.getRawObject().setBlob(parameterIndex, inputStream);
    }
    
    @Override
    public void preparedStatement_setNClob(final PreparedStatementProxy statement, final int parameterIndex, final Reader reader) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().preparedStatement_setNClob(this, statement, parameterIndex, reader);
            return;
        }
        statement.getRawObject().setNClob(parameterIndex, reader);
    }
    
    @Override
    public void callableStatement_registerOutParameter(final CallableStatementProxy statement, final int parameterIndex, final int sqlType) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_registerOutParameter(this, statement, parameterIndex, sqlType);
            return;
        }
        statement.getRawObject().registerOutParameter(parameterIndex, sqlType);
    }
    
    @Override
    public void callableStatement_registerOutParameter(final CallableStatementProxy statement, final int parameterIndex, final int sqlType, final int scale) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_registerOutParameter(this, statement, parameterIndex, sqlType, scale);
            return;
        }
        statement.getRawObject().registerOutParameter(parameterIndex, sqlType, scale);
    }
    
    @Override
    public boolean callableStatement_wasNull(final CallableStatementProxy statement) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_wasNull(this, statement);
        }
        return statement.getRawObject().wasNull();
    }
    
    @Override
    public String callableStatement_getString(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getString(this, statement, parameterIndex);
        }
        return statement.getRawObject().getString(parameterIndex);
    }
    
    @Override
    public boolean callableStatement_getBoolean(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getBoolean(this, statement, parameterIndex);
        }
        return statement.getRawObject().getBoolean(parameterIndex);
    }
    
    @Override
    public byte callableStatement_getByte(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getByte(this, statement, parameterIndex);
        }
        return statement.getRawObject().getByte(parameterIndex);
    }
    
    @Override
    public short callableStatement_getShort(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getShort(this, statement, parameterIndex);
        }
        return statement.getRawObject().getShort(parameterIndex);
    }
    
    @Override
    public int callableStatement_getInt(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getInt(this, statement, parameterIndex);
        }
        return statement.getRawObject().getInt(parameterIndex);
    }
    
    @Override
    public long callableStatement_getLong(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getLong(this, statement, parameterIndex);
        }
        return statement.getRawObject().getLong(parameterIndex);
    }
    
    @Override
    public float callableStatement_getFloat(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getFloat(this, statement, parameterIndex);
        }
        return statement.getRawObject().getFloat(parameterIndex);
    }
    
    @Override
    public double callableStatement_getDouble(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getDouble(this, statement, parameterIndex);
        }
        return statement.getRawObject().getDouble(parameterIndex);
    }
    
    @Override
    public BigDecimal callableStatement_getBigDecimal(final CallableStatementProxy statement, final int parameterIndex, final int scale) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getBigDecimal(this, statement, parameterIndex, scale);
        }
        return statement.getRawObject().getBigDecimal(parameterIndex, scale);
    }
    
    @Override
    public byte[] callableStatement_getBytes(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getBytes(this, statement, parameterIndex);
        }
        return statement.getRawObject().getBytes(parameterIndex);
    }
    
    @Override
    public Date callableStatement_getDate(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getDate(this, statement, parameterIndex);
        }
        return statement.getRawObject().getDate(parameterIndex);
    }
    
    @Override
    public Time callableStatement_getTime(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getTime(this, statement, parameterIndex);
        }
        return statement.getRawObject().getTime(parameterIndex);
    }
    
    @Override
    public Timestamp callableStatement_getTimestamp(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getTimestamp(this, statement, parameterIndex);
        }
        return statement.getRawObject().getTimestamp(parameterIndex);
    }
    
    @Override
    public Object callableStatement_getObject(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getObject(this, statement, parameterIndex);
        }
        final Object obj = statement.getRawObject().getObject(parameterIndex);
        if (obj instanceof ResultSet) {
            return new ResultSetProxyImpl(statement, (ResultSet)obj, this.dataSource.createResultSetId(), statement.getLastExecuteSql());
        }
        if (obj instanceof Clob) {
            return this.wrap(statement, (Clob)obj);
        }
        return obj;
    }
    
    @Override
    public Object callableStatement_getObject(final CallableStatementProxy statement, final int parameterIndex, final Map<String, Class<?>> map) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getObject(this, statement, parameterIndex, map);
        }
        final Object obj = statement.getRawObject().getObject(parameterIndex, map);
        if (obj instanceof ResultSet) {
            return new ResultSetProxyImpl(statement, (ResultSet)obj, this.dataSource.createResultSetId(), statement.getLastExecuteSql());
        }
        if (obj instanceof Clob) {
            return this.wrap(statement, (Clob)obj);
        }
        return obj;
    }
    
    @Override
    public Object callableStatement_getObject(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getObject(this, statement, parameterName);
        }
        final Object obj = statement.getRawObject().getObject(parameterName);
        if (obj instanceof ResultSet) {
            return new ResultSetProxyImpl(statement, (ResultSet)obj, this.dataSource.createResultSetId(), statement.getLastExecuteSql());
        }
        if (obj instanceof Clob) {
            return this.wrap(statement, (Clob)obj);
        }
        return obj;
    }
    
    @Override
    public Object callableStatement_getObject(final CallableStatementProxy statement, final String parameterName, final Map<String, Class<?>> map) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getObject(this, statement, parameterName, map);
        }
        final Object obj = statement.getRawObject().getObject(parameterName, map);
        if (obj instanceof ResultSet) {
            return new ResultSetProxyImpl(statement, (ResultSet)obj, this.dataSource.createResultSetId(), statement.getLastExecuteSql());
        }
        if (obj instanceof Clob) {
            return this.wrap(statement, (Clob)obj);
        }
        return obj;
    }
    
    @Override
    public BigDecimal callableStatement_getBigDecimal(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getBigDecimal(this, statement, parameterIndex);
        }
        return statement.getRawObject().getBigDecimal(parameterIndex);
    }
    
    @Override
    public Ref callableStatement_getRef(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getRef(this, statement, parameterIndex);
        }
        return statement.getRawObject().getRef(parameterIndex);
    }
    
    @Override
    public Blob callableStatement_getBlob(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getBlob(this, statement, parameterIndex);
        }
        return statement.getRawObject().getBlob(parameterIndex);
    }
    
    @Override
    public Clob callableStatement_getClob(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getClob(this, statement, parameterIndex);
        }
        final Clob clob = statement.getRawObject().getClob(parameterIndex);
        return this.wrap(statement, clob);
    }
    
    @Override
    public Array callableStatement_getArray(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getArray(this, statement, parameterIndex);
        }
        return statement.getRawObject().getArray(parameterIndex);
    }
    
    @Override
    public Date callableStatement_getDate(final CallableStatementProxy statement, final int parameterIndex, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getDate(this, statement, parameterIndex, cal);
        }
        return statement.getRawObject().getDate(parameterIndex, cal);
    }
    
    @Override
    public Time callableStatement_getTime(final CallableStatementProxy statement, final int parameterIndex, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getTime(this, statement, parameterIndex, cal);
        }
        return statement.getRawObject().getTime(parameterIndex, cal);
    }
    
    @Override
    public Timestamp callableStatement_getTimestamp(final CallableStatementProxy statement, final int parameterIndex, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getTimestamp(this, statement, parameterIndex, cal);
        }
        return statement.getRawObject().getTimestamp(parameterIndex, cal);
    }
    
    @Override
    public void callableStatement_registerOutParameter(final CallableStatementProxy statement, final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_registerOutParameter(this, statement, parameterIndex, sqlType, typeName);
            return;
        }
        statement.getRawObject().registerOutParameter(parameterIndex, sqlType, typeName);
    }
    
    @Override
    public void callableStatement_registerOutParameter(final CallableStatementProxy statement, final String parameterName, final int sqlType) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_registerOutParameter(this, statement, parameterName, sqlType);
            return;
        }
        statement.getRawObject().registerOutParameter(parameterName, sqlType);
    }
    
    @Override
    public void callableStatement_registerOutParameter(final CallableStatementProxy statement, final String parameterName, final int sqlType, final int scale) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_registerOutParameter(this, statement, parameterName, sqlType, scale);
            return;
        }
        statement.getRawObject().registerOutParameter(parameterName, sqlType, scale);
    }
    
    @Override
    public void callableStatement_registerOutParameter(final CallableStatementProxy statement, final String parameterName, final int sqlType, final String typeName) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_registerOutParameter(this, statement, parameterName, sqlType, typeName);
            return;
        }
        statement.getRawObject().registerOutParameter(parameterName, sqlType, typeName);
    }
    
    @Override
    public URL callableStatement_getURL(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getURL(this, statement, parameterIndex);
        }
        return statement.getRawObject().getURL(parameterIndex);
    }
    
    @Override
    public void callableStatement_setURL(final CallableStatementProxy statement, final String parameterName, final URL val) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setURL(this, statement, parameterName, val);
            return;
        }
        statement.getRawObject().setURL(parameterName, val);
    }
    
    @Override
    public void callableStatement_setNull(final CallableStatementProxy statement, final String parameterName, final int sqlType) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setNull(this, statement, parameterName, sqlType);
            return;
        }
        statement.getRawObject().setNull(parameterName, sqlType);
    }
    
    @Override
    public void callableStatement_setBoolean(final CallableStatementProxy statement, final String parameterName, final boolean x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setBoolean(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setBoolean(parameterName, x);
    }
    
    @Override
    public void callableStatement_setByte(final CallableStatementProxy statement, final String parameterName, final byte x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setByte(this, statement, parameterName, x);
        }
        statement.getRawObject().setByte(parameterName, x);
    }
    
    @Override
    public void callableStatement_setShort(final CallableStatementProxy statement, final String parameterName, final short x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setShort(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setShort(parameterName, x);
    }
    
    @Override
    public void callableStatement_setInt(final CallableStatementProxy statement, final String parameterName, final int x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setInt(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setInt(parameterName, x);
    }
    
    @Override
    public void callableStatement_setLong(final CallableStatementProxy statement, final String parameterName, final long x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setLong(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setLong(parameterName, x);
    }
    
    @Override
    public void callableStatement_setFloat(final CallableStatementProxy statement, final String parameterName, final float x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setFloat(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setFloat(parameterName, x);
    }
    
    @Override
    public void callableStatement_setDouble(final CallableStatementProxy statement, final String parameterName, final double x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setDouble(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setDouble(parameterName, x);
    }
    
    @Override
    public void callableStatement_setBigDecimal(final CallableStatementProxy statement, final String parameterName, final BigDecimal x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setBigDecimal(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setBigDecimal(parameterName, x);
    }
    
    @Override
    public void callableStatement_setString(final CallableStatementProxy statement, final String parameterName, final String x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setString(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setString(parameterName, x);
    }
    
    @Override
    public void callableStatement_setBytes(final CallableStatementProxy statement, final String parameterName, final byte[] x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setBytes(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setBytes(parameterName, x);
    }
    
    @Override
    public void callableStatement_setDate(final CallableStatementProxy statement, final String parameterName, final Date x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setDate(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setDate(parameterName, x);
    }
    
    @Override
    public void callableStatement_setTime(final CallableStatementProxy statement, final String parameterName, final Time x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setTime(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setTime(parameterName, x);
    }
    
    @Override
    public void callableStatement_setTimestamp(final CallableStatementProxy statement, final String parameterName, final Timestamp x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setTimestamp(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setTimestamp(parameterName, x);
    }
    
    @Override
    public void callableStatement_setAsciiStream(final CallableStatementProxy statement, final String parameterName, final InputStream x, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setAsciiStream(this, statement, parameterName, x, length);
            return;
        }
        statement.getRawObject().setAsciiStream(parameterName, x, length);
    }
    
    @Override
    public void callableStatement_setBinaryStream(final CallableStatementProxy statement, final String parameterName, final InputStream x, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setBinaryStream(this, statement, parameterName, x, length);
            return;
        }
        statement.getRawObject().setBinaryStream(parameterName, x, length);
    }
    
    @Override
    public void callableStatement_setObject(final CallableStatementProxy statement, final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setObject(this, statement, parameterName, x, targetSqlType, scale);
            return;
        }
        statement.getRawObject().setObject(parameterName, x, targetSqlType, scale);
    }
    
    @Override
    public void callableStatement_setObject(final CallableStatementProxy statement, final String parameterName, final Object x, final int targetSqlType) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setObject(this, statement, parameterName, x, targetSqlType);
            return;
        }
        statement.getRawObject().setObject(parameterName, x, targetSqlType);
    }
    
    @Override
    public void callableStatement_setObject(final CallableStatementProxy statement, final String parameterName, final Object x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setObject(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setObject(parameterName, x);
    }
    
    @Override
    public void callableStatement_setCharacterStream(final CallableStatementProxy statement, final String parameterName, final Reader reader, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setCharacterStream(this, statement, parameterName, reader, length);
            return;
        }
        statement.getRawObject().setCharacterStream(parameterName, reader, length);
    }
    
    @Override
    public void callableStatement_setDate(final CallableStatementProxy statement, final String parameterName, final Date x, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setDate(this, statement, parameterName, x, cal);
            return;
        }
        statement.getRawObject().setDate(parameterName, x, cal);
    }
    
    @Override
    public void callableStatement_setTime(final CallableStatementProxy statement, final String parameterName, final Time x, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setTime(this, statement, parameterName, x, cal);
            return;
        }
        statement.getRawObject().setTime(parameterName, x, cal);
    }
    
    @Override
    public void callableStatement_setTimestamp(final CallableStatementProxy statement, final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setTimestamp(this, statement, parameterName, x, cal);
            return;
        }
        statement.getRawObject().setTimestamp(parameterName, x, cal);
    }
    
    @Override
    public void callableStatement_setNull(final CallableStatementProxy statement, final String parameterName, final int sqlType, final String typeName) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setNull(this, statement, parameterName, sqlType, typeName);
            return;
        }
        statement.getRawObject().setNull(parameterName, sqlType, typeName);
    }
    
    @Override
    public String callableStatement_getString(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getString(this, statement, parameterName);
        }
        return statement.getRawObject().getString(parameterName);
    }
    
    @Override
    public boolean callableStatement_getBoolean(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getBoolean(this, statement, parameterName);
        }
        return statement.getRawObject().getBoolean(parameterName);
    }
    
    @Override
    public byte callableStatement_getByte(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getByte(this, statement, parameterName);
        }
        return statement.getRawObject().getByte(parameterName);
    }
    
    @Override
    public short callableStatement_getShort(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getShort(this, statement, parameterName);
        }
        return statement.getRawObject().getShort(parameterName);
    }
    
    @Override
    public int callableStatement_getInt(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getInt(this, statement, parameterName);
        }
        return statement.getRawObject().getInt(parameterName);
    }
    
    @Override
    public long callableStatement_getLong(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getLong(this, statement, parameterName);
        }
        return statement.getRawObject().getLong(parameterName);
    }
    
    @Override
    public float callableStatement_getFloat(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getFloat(this, statement, parameterName);
        }
        return statement.getRawObject().getFloat(parameterName);
    }
    
    @Override
    public double callableStatement_getDouble(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getDouble(this, statement, parameterName);
        }
        return statement.getRawObject().getDouble(parameterName);
    }
    
    @Override
    public byte[] callableStatement_getBytes(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getBytes(this, statement, parameterName);
        }
        return statement.getRawObject().getBytes(parameterName);
    }
    
    @Override
    public Date callableStatement_getDate(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getDate(this, statement, parameterName);
        }
        return statement.getRawObject().getDate(parameterName);
    }
    
    @Override
    public Time callableStatement_getTime(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getTime(this, statement, parameterName);
        }
        return statement.getRawObject().getTime(parameterName);
    }
    
    @Override
    public Timestamp callableStatement_getTimestamp(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getTimestamp(this, statement, parameterName);
        }
        return statement.getRawObject().getTimestamp(parameterName);
    }
    
    @Override
    public BigDecimal callableStatement_getBigDecimal(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getBigDecimal(this, statement, parameterName);
        }
        return statement.getRawObject().getBigDecimal(parameterName);
    }
    
    @Override
    public Ref callableStatement_getRef(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getRef(this, statement, parameterName);
        }
        return statement.getRawObject().getRef(parameterName);
    }
    
    @Override
    public Blob callableStatement_getBlob(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getBlob(this, statement, parameterName);
        }
        return statement.getRawObject().getBlob(parameterName);
    }
    
    @Override
    public Clob callableStatement_getClob(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getClob(this, statement, parameterName);
        }
        final Clob clob = statement.getRawObject().getClob(parameterName);
        return this.wrap(statement, clob);
    }
    
    @Override
    public Array callableStatement_getArray(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getArray(this, statement, parameterName);
        }
        return statement.getRawObject().getArray(parameterName);
    }
    
    @Override
    public Date callableStatement_getDate(final CallableStatementProxy statement, final String parameterName, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getDate(this, statement, parameterName, cal);
        }
        return statement.getRawObject().getDate(parameterName, cal);
    }
    
    @Override
    public Time callableStatement_getTime(final CallableStatementProxy statement, final String parameterName, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getTime(this, statement, parameterName, cal);
        }
        return statement.getRawObject().getTime(parameterName, cal);
    }
    
    @Override
    public Timestamp callableStatement_getTimestamp(final CallableStatementProxy statement, final String parameterName, final Calendar cal) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getTimestamp(this, statement, parameterName, cal);
        }
        return statement.getRawObject().getTimestamp(parameterName, cal);
    }
    
    @Override
    public URL callableStatement_getURL(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getURL(this, statement, parameterName);
        }
        return statement.getRawObject().getURL(parameterName);
    }
    
    @Override
    public RowId callableStatement_getRowId(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getRowId(this, statement, parameterIndex);
        }
        return statement.getRawObject().getRowId(parameterIndex);
    }
    
    @Override
    public RowId callableStatement_getRowId(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getRowId(this, statement, parameterName);
        }
        return statement.getRawObject().getRowId(parameterName);
    }
    
    @Override
    public void callableStatement_setRowId(final CallableStatementProxy statement, final String parameterName, final RowId x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setRowId(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setRowId(parameterName, x);
    }
    
    @Override
    public void callableStatement_setNString(final CallableStatementProxy statement, final String parameterName, final String value) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setNString(this, statement, parameterName, value);
            return;
        }
        statement.getRawObject().setNString(parameterName, value);
    }
    
    @Override
    public void callableStatement_setNCharacterStream(final CallableStatementProxy statement, final String parameterName, final Reader value, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setNCharacterStream(this, statement, parameterName, value, length);
            return;
        }
        statement.getRawObject().setNCharacterStream(parameterName, value, length);
    }
    
    @Override
    public void callableStatement_setNClob(final CallableStatementProxy statement, final String parameterName, NClob x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setNClob(this, statement, parameterName, x);
            return;
        }
        if (x instanceof NClobProxy) {
            x = ((NClobProxy)x).getRawNClob();
        }
        statement.getRawObject().setNClob(parameterName, x);
    }
    
    @Override
    public void callableStatement_setClob(final CallableStatementProxy statement, final String parameterName, final Reader reader, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setClob(this, statement, parameterName, reader, length);
            return;
        }
        statement.getRawObject().setClob(parameterName, reader, length);
    }
    
    @Override
    public void callableStatement_setBlob(final CallableStatementProxy statement, final String parameterName, final InputStream inputStream, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setBlob(this, statement, parameterName, inputStream, length);
            return;
        }
        statement.getRawObject().setBlob(parameterName, inputStream, length);
    }
    
    @Override
    public void callableStatement_setNClob(final CallableStatementProxy statement, final String parameterName, final Reader reader, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setNClob(this, statement, parameterName, reader, length);
            return;
        }
        statement.getRawObject().setNClob(parameterName, reader, length);
    }
    
    @Override
    public NClob callableStatement_getNClob(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getNClob(this, statement, parameterIndex);
        }
        final NClob nclob = statement.getRawObject().getNClob(parameterIndex);
        return this.wrap(statement.getConnectionProxy(), nclob);
    }
    
    @Override
    public NClob callableStatement_getNClob(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getNClob(this, statement, parameterName);
        }
        final NClob nclob = statement.getRawObject().getNClob(parameterName);
        return this.wrap(statement.getConnectionProxy(), nclob);
    }
    
    @Override
    public void callableStatement_setSQLXML(final CallableStatementProxy statement, final String parameterName, final SQLXML xmlObject) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setSQLXML(this, statement, parameterName, xmlObject);
            return;
        }
        statement.getRawObject().setSQLXML(parameterName, xmlObject);
    }
    
    @Override
    public SQLXML callableStatement_getSQLXML(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getSQLXML(this, statement, parameterIndex);
        }
        return statement.getRawObject().getSQLXML(parameterIndex);
    }
    
    @Override
    public SQLXML callableStatement_getSQLXML(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getSQLXML(this, statement, parameterName);
        }
        return statement.getRawObject().getSQLXML(parameterName);
    }
    
    @Override
    public String callableStatement_getNString(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getNString(this, statement, parameterIndex);
        }
        return statement.getRawObject().getNString(parameterIndex);
    }
    
    @Override
    public String callableStatement_getNString(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getNString(this, statement, parameterName);
        }
        return statement.getRawObject().getNString(parameterName);
    }
    
    @Override
    public Reader callableStatement_getNCharacterStream(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getNCharacterStream(this, statement, parameterIndex);
        }
        return statement.getRawObject().getNCharacterStream(parameterIndex);
    }
    
    @Override
    public Reader callableStatement_getNCharacterStream(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getNCharacterStream(this, statement, parameterName);
        }
        return statement.getRawObject().getNCharacterStream(parameterName);
    }
    
    @Override
    public Reader callableStatement_getCharacterStream(final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getCharacterStream(this, statement, parameterIndex);
        }
        return statement.getRawObject().getCharacterStream(parameterIndex);
    }
    
    @Override
    public Reader callableStatement_getCharacterStream(final CallableStatementProxy statement, final String parameterName) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().callableStatement_getCharacterStream(this, statement, parameterName);
        }
        return statement.getRawObject().getCharacterStream(parameterName);
    }
    
    @Override
    public void callableStatement_setBlob(final CallableStatementProxy statement, final String parameterName, final Blob x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setBlob(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setBlob(parameterName, x);
    }
    
    @Override
    public void callableStatement_setClob(final CallableStatementProxy statement, final String parameterName, Clob x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setClob(this, statement, parameterName, x);
            return;
        }
        if (x instanceof ClobProxy) {
            x = ((ClobProxy)x).getRawClob();
        }
        statement.getRawObject().setClob(parameterName, x);
    }
    
    @Override
    public void callableStatement_setAsciiStream(final CallableStatementProxy statement, final String parameterName, final InputStream x, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setAsciiStream(this, statement, parameterName, x, length);
            return;
        }
        statement.getRawObject().setAsciiStream(parameterName, x, length);
    }
    
    @Override
    public void callableStatement_setBinaryStream(final CallableStatementProxy statement, final String parameterName, final InputStream x, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setBinaryStream(this, statement, parameterName, x, length);
            return;
        }
        statement.getRawObject().setBinaryStream(parameterName, x, length);
    }
    
    @Override
    public void callableStatement_setCharacterStream(final CallableStatementProxy statement, final String parameterName, final Reader reader, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setCharacterStream(this, statement, parameterName, reader, length);
            return;
        }
        statement.getRawObject().setCharacterStream(parameterName, reader, length);
    }
    
    @Override
    public void callableStatement_setAsciiStream(final CallableStatementProxy statement, final String parameterName, final InputStream x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setAsciiStream(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setAsciiStream(parameterName, x);
    }
    
    @Override
    public void callableStatement_setBinaryStream(final CallableStatementProxy statement, final String parameterName, final InputStream x) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setBinaryStream(this, statement, parameterName, x);
            return;
        }
        statement.getRawObject().setBinaryStream(parameterName, x);
    }
    
    @Override
    public void callableStatement_setCharacterStream(final CallableStatementProxy statement, final String parameterName, final Reader reader) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setCharacterStream(this, statement, parameterName, reader);
            return;
        }
        statement.getRawObject().setCharacterStream(parameterName, reader);
    }
    
    @Override
    public void callableStatement_setNCharacterStream(final CallableStatementProxy statement, final String parameterName, final Reader value) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setNCharacterStream(this, statement, parameterName, value);
            return;
        }
        statement.getRawObject().setNCharacterStream(parameterName, value);
    }
    
    @Override
    public void callableStatement_setClob(final CallableStatementProxy statement, final String parameterName, final Reader reader) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setClob(this, statement, parameterName, reader);
            return;
        }
        statement.getRawObject().setClob(parameterName, reader);
    }
    
    @Override
    public void callableStatement_setBlob(final CallableStatementProxy statement, final String parameterName, final InputStream inputStream) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setBlob(this, statement, parameterName, inputStream);
            return;
        }
        statement.getRawObject().setBlob(parameterName, inputStream);
    }
    
    @Override
    public void callableStatement_setNClob(final CallableStatementProxy statement, final String parameterName, final Reader reader) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().callableStatement_setNClob(this, statement, parameterName, reader);
            return;
        }
        statement.getRawObject().setNClob(parameterName, reader);
    }
    
    @Override
    public long clob_length(final ClobProxy clob) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().clob_length(this, clob);
        }
        return clob.getRawClob().length();
    }
    
    @Override
    public String clob_getSubString(final ClobProxy clob, final long pos, final int length) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().clob_getSubString(this, clob, pos, length);
        }
        return clob.getRawClob().getSubString(pos, length);
    }
    
    @Override
    public Reader clob_getCharacterStream(final ClobProxy clob) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().clob_getCharacterStream(this, clob);
        }
        return clob.getRawClob().getCharacterStream();
    }
    
    @Override
    public InputStream clob_getAsciiStream(final ClobProxy clob) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().clob_getAsciiStream(this, clob);
        }
        return clob.getRawClob().getAsciiStream();
    }
    
    @Override
    public long clob_position(final ClobProxy clob, final String searchstr, final long start) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().clob_position(this, clob, searchstr, start);
        }
        return clob.getRawClob().position(searchstr, start);
    }
    
    @Override
    public long clob_position(final ClobProxy clob, final Clob searchstr, final long start) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().clob_position(this, clob, searchstr, start);
        }
        return clob.getRawClob().position(searchstr, start);
    }
    
    @Override
    public int clob_setString(final ClobProxy clob, final long pos, final String str) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().clob_setString(this, clob, pos, str);
        }
        return clob.getRawClob().setString(pos, str);
    }
    
    @Override
    public int clob_setString(final ClobProxy clob, final long pos, final String str, final int offset, final int len) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().clob_setString(this, clob, pos, str, offset, len);
        }
        return clob.getRawClob().setString(pos, str, offset, len);
    }
    
    @Override
    public OutputStream clob_setAsciiStream(final ClobProxy clob, final long pos) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().clob_setAsciiStream(this, clob, pos);
        }
        return clob.getRawClob().setAsciiStream(pos);
    }
    
    @Override
    public Writer clob_setCharacterStream(final ClobProxy clob, final long pos) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().clob_setCharacterStream(this, clob, pos);
        }
        return clob.getRawClob().setCharacterStream(pos);
    }
    
    @Override
    public void clob_truncate(final ClobProxy clob, final long len) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().clob_truncate(this, clob, len);
            return;
        }
        clob.getRawClob().truncate(len);
    }
    
    @Override
    public void clob_free(final ClobProxy clob) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().clob_free(this, clob);
            return;
        }
        clob.getRawClob().free();
    }
    
    @Override
    public Reader clob_getCharacterStream(final ClobProxy clob, final long pos, final long length) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().clob_getCharacterStream(this, clob, pos, length);
        }
        return clob.getRawClob().getCharacterStream(pos, length);
    }
    
    public ClobProxy wrap(final ConnectionProxy conn, final Clob clob) {
        if (clob == null) {
            return null;
        }
        if (clob instanceof NClob) {
            return this.wrap(conn, (NClob)clob);
        }
        return new ClobProxyImpl(this.dataSource, conn, clob);
    }
    
    public NClobProxy wrap(final ConnectionProxy conn, final NClob clob) {
        if (clob == null) {
            return null;
        }
        return new NClobProxyImpl(this.dataSource, conn, clob);
    }
    
    public ClobProxy wrap(final StatementProxy stmt, final Clob clob) {
        if (clob == null) {
            return null;
        }
        if (clob instanceof NClob) {
            return this.wrap(stmt, (NClob)clob);
        }
        return new ClobProxyImpl(this.dataSource, stmt.getConnectionProxy(), clob);
    }
    
    public NClobProxy wrap(final StatementProxy stmt, final NClob nclob) {
        if (nclob == null) {
            return null;
        }
        return new NClobProxyImpl(this.dataSource, stmt.getConnectionProxy(), nclob);
    }
    
    @Override
    public void dataSource_recycle(final DruidPooledConnection connection) throws SQLException {
        if (this.pos < this.filterSize) {
            this.nextFilter().dataSource_releaseConnection(this, connection);
            return;
        }
        connection.recycle();
    }
    
    @Override
    public DruidPooledConnection dataSource_connect(final DruidDataSource dataSource, final long maxWaitMillis) throws SQLException {
        if (this.pos < this.filterSize) {
            final DruidPooledConnection conn = this.nextFilter().dataSource_getConnection(this, dataSource, maxWaitMillis);
            return conn;
        }
        return dataSource.getConnectionDirect(maxWaitMillis);
    }
    
    @Override
    public int resultSetMetaData_getColumnCount(final ResultSetMetaDataProxy metaData) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_getColumnCount(this, metaData);
        }
        return metaData.getResultSetMetaDataRaw().getColumnCount();
    }
    
    @Override
    public boolean resultSetMetaData_isAutoIncrement(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_isAutoIncrement(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().isAutoIncrement(column);
    }
    
    @Override
    public boolean resultSetMetaData_isCaseSensitive(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_isCaseSensitive(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().isCaseSensitive(column);
    }
    
    @Override
    public boolean resultSetMetaData_isSearchable(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_isSearchable(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().isSearchable(column);
    }
    
    @Override
    public boolean resultSetMetaData_isCurrency(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_isCurrency(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().isCurrency(column);
    }
    
    @Override
    public int resultSetMetaData_isNullable(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_isNullable(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().isNullable(column);
    }
    
    @Override
    public boolean resultSetMetaData_isSigned(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_isSigned(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().isSigned(column);
    }
    
    @Override
    public int resultSetMetaData_getColumnDisplaySize(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_getColumnDisplaySize(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().getColumnDisplaySize(column);
    }
    
    @Override
    public String resultSetMetaData_getColumnLabel(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_getColumnLabel(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().getColumnLabel(column);
    }
    
    @Override
    public String resultSetMetaData_getColumnName(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_getColumnName(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().getColumnName(column);
    }
    
    @Override
    public String resultSetMetaData_getSchemaName(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_getSchemaName(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().getSchemaName(column);
    }
    
    @Override
    public int resultSetMetaData_getPrecision(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_getPrecision(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().getPrecision(column);
    }
    
    @Override
    public int resultSetMetaData_getScale(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_getScale(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().getScale(column);
    }
    
    @Override
    public String resultSetMetaData_getTableName(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_getTableName(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().getTableName(column);
    }
    
    @Override
    public String resultSetMetaData_getCatalogName(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_getCatalogName(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().getCatalogName(column);
    }
    
    @Override
    public int resultSetMetaData_getColumnType(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_getColumnType(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().getColumnType(column);
    }
    
    @Override
    public String resultSetMetaData_getColumnTypeName(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_getColumnTypeName(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().getColumnTypeName(column);
    }
    
    @Override
    public boolean resultSetMetaData_isReadOnly(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_isReadOnly(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().isReadOnly(column);
    }
    
    @Override
    public boolean resultSetMetaData_isWritable(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_isWritable(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().isWritable(column);
    }
    
    @Override
    public boolean resultSetMetaData_isDefinitelyWritable(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_isDefinitelyWritable(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().isDefinitelyWritable(column);
    }
    
    @Override
    public String resultSetMetaData_getColumnClassName(final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        if (this.pos < this.filterSize) {
            return this.nextFilter().resultSetMetaData_getColumnClassName(this, metaData, column);
        }
        return metaData.getResultSetMetaDataRaw().getColumnClassName(column);
    }
}
