// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.util.Arrays;
import com.alibaba.druid.support.logging.LogFactory;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.ParameterMetaData;
import java.net.URL;
import java.util.Calendar;
import java.sql.ResultSetMetaData;
import java.sql.Array;
import java.sql.Clob;
import java.sql.Blob;
import java.sql.Ref;
import java.io.Reader;
import com.alibaba.druid.util.OracleUtils;
import java.io.InputStream;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.alibaba.druid.support.logging.Log;
import java.sql.PreparedStatement;

public class DruidPooledPreparedStatement extends DruidPooledStatement implements PreparedStatement
{
    private static final Log LOG;
    private final PreparedStatementHolder holder;
    private final PreparedStatement stmt;
    private final String sql;
    private int defaultMaxFieldSize;
    private int defaultMaxRows;
    private int defaultQueryTimeout;
    private int defaultFetchDirection;
    private int defaultFetchSize;
    private int currentMaxFieldSize;
    private int currentMaxRows;
    private int currentQueryTimeout;
    private int currentFetchDirection;
    private int currentFetchSize;
    private boolean pooled;
    
    public DruidPooledPreparedStatement(final DruidPooledConnection conn, final PreparedStatementHolder holder) throws SQLException {
        super(conn, holder.statement);
        this.pooled = false;
        this.stmt = holder.statement;
        this.holder = holder;
        this.sql = holder.key.sql;
        this.pooled = conn.getConnectionHolder().isPoolPreparedStatements();
        if (this.pooled) {
            try {
                this.defaultMaxFieldSize = this.stmt.getMaxFieldSize();
            }
            catch (SQLException e) {
                DruidPooledPreparedStatement.LOG.error("getMaxFieldSize error", e);
            }
            try {
                this.defaultMaxRows = this.stmt.getMaxRows();
            }
            catch (SQLException e) {
                DruidPooledPreparedStatement.LOG.error("getMaxRows error", e);
            }
            try {
                this.defaultQueryTimeout = this.stmt.getQueryTimeout();
            }
            catch (SQLException e) {
                DruidPooledPreparedStatement.LOG.error("getMaxRows error", e);
            }
            try {
                this.defaultFetchDirection = this.stmt.getFetchDirection();
            }
            catch (SQLException e) {
                DruidPooledPreparedStatement.LOG.error("getFetchDirection error", e);
            }
            try {
                this.defaultFetchSize = this.stmt.getFetchSize();
            }
            catch (SQLException e) {
                DruidPooledPreparedStatement.LOG.error("getFetchSize error", e);
            }
        }
        this.currentMaxFieldSize = this.defaultMaxFieldSize;
        this.currentMaxRows = this.defaultMaxRows;
        this.currentQueryTimeout = this.defaultQueryTimeout;
        this.currentFetchDirection = this.defaultFetchDirection;
        this.currentFetchSize = this.defaultFetchSize;
    }
    
    public PreparedStatementHolder getPreparedStatementHolder() {
        return this.holder;
    }
    
    public int getHitCount() {
        return this.holder.getHitCount();
    }
    
    @Override
    public void setFetchSize(final int rows) throws SQLException {
        super.setFetchSize(this.currentFetchSize = rows);
    }
    
    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        super.setFetchDirection(this.currentFetchDirection = direction);
    }
    
    @Override
    public void setMaxFieldSize(final int max) throws SQLException {
        super.setMaxFieldSize(this.currentMaxFieldSize = max);
    }
    
    @Override
    public void setMaxRows(final int max) throws SQLException {
        super.setMaxRows(this.currentMaxRows = max);
    }
    
    @Override
    public void setQueryTimeout(final int seconds) throws SQLException {
        super.setQueryTimeout(this.currentQueryTimeout = seconds);
    }
    
    public String getSql() {
        return this.sql;
    }
    
    public PreparedStatementKey getKey() {
        return this.holder.key;
    }
    
    public PreparedStatement getRawPreparedStatement() {
        return this.stmt;
    }
    
    public PreparedStatement getRawStatement() {
        return this.stmt;
    }
    
    @Override
    public void close() throws SQLException {
        if (this.isClosed()) {
            return;
        }
        final boolean connectionClosed = this.conn.isClosed();
        if (this.pooled && !connectionClosed) {
            try {
                if (this.defaultMaxFieldSize != this.currentMaxFieldSize) {
                    this.stmt.setMaxFieldSize(this.defaultMaxFieldSize);
                    this.currentMaxFieldSize = this.defaultMaxFieldSize;
                }
                if (this.defaultMaxRows != this.currentMaxRows) {
                    this.stmt.setMaxRows(this.defaultMaxRows);
                    this.currentMaxRows = this.defaultMaxRows;
                }
                if (this.defaultQueryTimeout != this.currentQueryTimeout) {
                    this.stmt.setQueryTimeout(this.defaultQueryTimeout);
                    this.currentQueryTimeout = this.defaultQueryTimeout;
                }
                if (this.defaultFetchDirection != this.currentFetchDirection) {
                    this.stmt.setFetchDirection(this.defaultFetchDirection);
                    this.currentFetchDirection = this.defaultFetchDirection;
                }
                if (this.defaultFetchSize != this.currentFetchSize) {
                    this.stmt.setFetchSize(this.defaultFetchSize);
                    this.currentFetchSize = this.defaultFetchSize;
                }
            }
            catch (Exception e) {
                this.conn.handleException(e, null);
            }
        }
        this.conn.closePoolableStatement(this);
    }
    
    public boolean isPooled() {
        return this.pooled;
    }
    
    void closeInternal() throws SQLException {
        super.close();
    }
    
    void setClosed(final boolean value) {
        this.closed = value;
    }
    
    @Override
    public ResultSet executeQuery() throws SQLException {
        this.checkOpen();
        this.incrementExecuteQueryCount();
        this.transactionRecord(this.sql);
        this.oracleSetRowPrefetch();
        this.conn.beforeExecute();
        try {
            final ResultSet rs = this.stmt.executeQuery();
            if (rs == null) {
                return null;
            }
            final DruidPooledResultSet poolableResultSet = new DruidPooledResultSet(this, rs);
            this.addResultSetTrace(poolableResultSet);
            return poolableResultSet;
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t);
        }
        finally {
            this.conn.afterExecute();
        }
    }
    
    @Override
    public int executeUpdate() throws SQLException {
        this.checkOpen();
        this.incrementExecuteUpdateCount();
        this.transactionRecord(this.sql);
        this.conn.beforeExecute();
        try {
            return this.stmt.executeUpdate();
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t);
        }
        finally {
            this.conn.afterExecute();
        }
    }
    
    @Override
    public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setNull(parameterIndex, sqlType);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setBoolean(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setByte(final int parameterIndex, final byte x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setByte(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setShort(final int parameterIndex, final short x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setShort(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setInt(final int parameterIndex, final int x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setInt(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setLong(final int parameterIndex, final long x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setLong(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setFloat(final int parameterIndex, final float x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setFloat(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setDouble(final int parameterIndex, final double x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setDouble(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setBigDecimal(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setString(final int parameterIndex, final String x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setString(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setBytes(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setDate(final int parameterIndex, final Date x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setDate(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setTime(final int parameterIndex, final Time x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setTime(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setTimestamp(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setAsciiStream(parameterIndex, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Deprecated
    @Override
    public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setUnicodeStream(parameterIndex, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setBinaryStream(parameterIndex, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void clearParameters() throws SQLException {
        this.checkOpen();
        try {
            this.stmt.clearParameters();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setObject(parameterIndex, x, targetSqlType);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setObject(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean execute() throws SQLException {
        this.checkOpen();
        this.incrementExecuteCount();
        this.transactionRecord(this.sql);
        this.oracleSetRowPrefetch();
        this.conn.beforeExecute();
        try {
            return this.stmt.execute();
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t);
        }
        finally {
            this.conn.afterExecute();
        }
    }
    
    protected void oracleSetRowPrefetch() throws SQLException {
        if (!this.conn.isOracle()) {
            return;
        }
        if (this.holder.getHitCount() == 0) {
            return;
        }
        final int fetchRowPeak = this.holder.getFetchRowPeak();
        if (fetchRowPeak < 0) {
            return;
        }
        if (this.holder.getDefaultRowPrefetch() == -1) {
            final int defaultRowPretch = OracleUtils.getRowPrefetch(this);
            if (defaultRowPretch != this.holder.getDefaultRowPrefetch()) {
                this.holder.setDefaultRowPrefetch(defaultRowPretch);
                this.holder.setRowPrefetch(defaultRowPretch);
            }
        }
        int rowPrefetch;
        if (fetchRowPeak <= 1) {
            rowPrefetch = 2;
        }
        else if (fetchRowPeak > this.holder.getDefaultRowPrefetch()) {
            rowPrefetch = this.holder.getDefaultRowPrefetch();
        }
        else {
            rowPrefetch = fetchRowPeak + 1;
        }
        if (rowPrefetch != this.holder.getRowPrefetch()) {
            OracleUtils.setRowPrefetch(this, rowPrefetch);
            this.holder.setRowPrefetch(rowPrefetch);
        }
    }
    
    @Override
    public void addBatch() throws SQLException {
        this.checkOpen();
        try {
            this.stmt.addBatch();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int[] executeBatch() throws SQLException {
        this.checkOpen();
        this.incrementExecuteBatchCount();
        this.transactionRecord(this.sql);
        this.conn.beforeExecute();
        try {
            return this.stmt.executeBatch();
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t);
        }
        finally {
            this.conn.afterExecute();
        }
    }
    
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setCharacterStream(parameterIndex, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setRef(final int parameterIndex, final Ref x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setRef(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setBlob(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setClob(final int parameterIndex, final Clob x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setClob(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setArray(final int parameterIndex, final Array x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setArray(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        this.checkOpen();
        if (!this.conn.holder.isUnderlyingAutoCommit()) {
            this.conn.createTransactionInfo();
        }
        try {
            return this.stmt.getMetaData();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setDate(parameterIndex, x, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setTime(parameterIndex, x, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setTimestamp(parameterIndex, x, cal);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setNull(parameterIndex, sqlType, typeName);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setURL(final int parameterIndex, final URL x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setURL(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        this.checkOpen();
        if (!this.conn.holder.isUnderlyingAutoCommit()) {
            this.conn.createTransactionInfo();
        }
        try {
            return this.stmt.getParameterMetaData();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setRowId(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNString(final int parameterIndex, final String value) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setNString(parameterIndex, value);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setNCharacterStream(parameterIndex, value, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setNClob(parameterIndex, value);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setClob(parameterIndex, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setBlob(parameterIndex, inputStream, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setNClob(parameterIndex, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setSQLXML(parameterIndex, xmlObject);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setAsciiStream(parameterIndex, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setBinaryStream(parameterIndex, x, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setCharacterStream(parameterIndex, reader, length);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setAsciiStream(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setBinaryStream(parameterIndex, x);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setCharacterStream(parameterIndex, reader);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setNCharacterStream(parameterIndex, value);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setClob(parameterIndex, reader);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setBlob(parameterIndex, inputStream);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setNClob(parameterIndex, reader);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface == PreparedStatementHolder.class || super.isWrapperFor(iface);
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface == PreparedStatementHolder.class) {
            return (T)this.holder;
        }
        return super.unwrap(iface);
    }
    
    static {
        LOG = LogFactory.getLog(DruidPooledPreparedStatement.class);
    }
    
    public static class PreparedStatementKey
    {
        protected final String sql;
        protected final String catalog;
        protected final PreparedStatementPool.MethodType methodType;
        public final int resultSetType;
        public final int resultSetConcurrency;
        public final int resultSetHoldability;
        public final int autoGeneratedKeys;
        private final int[] columnIndexes;
        private final String[] columnNames;
        
        public PreparedStatementKey(final String sql, final String catalog, final PreparedStatementPool.MethodType methodType) throws SQLException {
            this(sql, catalog, methodType, 0, 0, 0, 0, null, null);
        }
        
        public PreparedStatementKey(final String sql, final String catalog, final PreparedStatementPool.MethodType methodType, final int resultSetType, final int resultSetConcurrency) throws SQLException {
            this(sql, catalog, methodType, resultSetType, resultSetConcurrency, 0, 0, null, null);
        }
        
        public PreparedStatementKey(final String sql, final String catalog, final PreparedStatementPool.MethodType methodType, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
            this(sql, catalog, methodType, resultSetType, resultSetConcurrency, resultSetHoldability, 0, null, null);
        }
        
        public PreparedStatementKey(final String sql, final String catalog, final PreparedStatementPool.MethodType methodType, final int autoGeneratedKeys) throws SQLException {
            this(sql, catalog, methodType, 0, 0, 0, autoGeneratedKeys, null, null);
        }
        
        public PreparedStatementKey(final String sql, final String catalog, final PreparedStatementPool.MethodType methodType, final int[] columnIndexes) throws SQLException {
            this(sql, catalog, methodType, 0, 0, 0, 0, columnIndexes, null);
        }
        
        public PreparedStatementKey(final String sql, final String catalog, final PreparedStatementPool.MethodType methodType, final String[] columnNames) throws SQLException {
            this(sql, catalog, methodType, 0, 0, 0, 0, null, columnNames);
        }
        
        public PreparedStatementKey(final String sql, final String catalog, final PreparedStatementPool.MethodType methodType, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability, final int autoGeneratedKeys, final int[] columnIndexes, final String[] columnNames) throws SQLException {
            if (sql == null) {
                throw new SQLException("sql is null");
            }
            this.sql = sql;
            this.catalog = catalog;
            this.methodType = methodType;
            this.resultSetType = resultSetType;
            this.resultSetConcurrency = resultSetConcurrency;
            this.resultSetHoldability = resultSetHoldability;
            this.autoGeneratedKeys = autoGeneratedKeys;
            this.columnIndexes = columnIndexes;
            this.columnNames = columnNames;
        }
        
        public int getResultSetType() {
            return this.resultSetType;
        }
        
        public int getResultSetConcurrency() {
            return this.resultSetConcurrency;
        }
        
        public int getResultSetHoldability() {
            return this.resultSetHoldability;
        }
        
        @Override
        public boolean equals(final Object object) {
            final PreparedStatementKey that = (PreparedStatementKey)object;
            if (!this.sql.equals(that.sql)) {
                return false;
            }
            if (this.catalog == null) {
                if (that.catalog != null) {
                    return false;
                }
            }
            else if (!this.catalog.equals(that.catalog)) {
                return false;
            }
            return this.methodType == that.methodType && this.resultSetType == that.resultSetType && this.resultSetConcurrency == that.resultSetConcurrency && this.resultSetHoldability == that.resultSetHoldability && this.autoGeneratedKeys == that.autoGeneratedKeys && Arrays.equals(this.columnIndexes, that.columnIndexes) && Arrays.equals(this.columnNames, that.columnNames);
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + ((this.sql == null) ? 0 : this.sql.hashCode());
            result = 31 * result + ((this.catalog == null) ? 0 : this.catalog.hashCode());
            result = 31 * result + ((this.methodType == null) ? 0 : this.methodType.hashCode());
            result = 31 * result + this.resultSetConcurrency;
            result = 31 * result + this.resultSetHoldability;
            result = 31 * result + this.resultSetType;
            result = 31 * result + this.autoGeneratedKeys;
            result = 31 * result + Arrays.hashCode(this.columnIndexes);
            result = 31 * result + Arrays.hashCode(this.columnNames);
            return result;
        }
        
        public String getSql() {
            return this.sql;
        }
    }
}
