// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import com.alibaba.druid.support.logging.LogFactory;
import java.sql.SQLWarning;
import com.alibaba.druid.VERSION;
import java.util.Iterator;
import java.sql.Connection;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.util.MySqlUtils;
import com.alibaba.druid.DbType;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Wrapper;
import java.sql.ResultSet;
import java.util.List;
import com.alibaba.druid.support.logging.Log;
import java.sql.Statement;

public class DruidPooledStatement extends PoolableWrapper implements Statement
{
    private static final Log LOG;
    private final Statement stmt;
    protected DruidPooledConnection conn;
    protected List<ResultSet> resultSetTrace;
    protected boolean closed;
    protected int fetchRowPeak;
    protected int exceptionCount;
    
    public DruidPooledStatement(final DruidPooledConnection conn, final Statement stmt) {
        super(stmt);
        this.closed = false;
        this.fetchRowPeak = -1;
        this.exceptionCount = 0;
        this.conn = conn;
        this.stmt = stmt;
    }
    
    protected void addResultSetTrace(final ResultSet resultSet) {
        if (this.resultSetTrace == null) {
            this.resultSetTrace = new ArrayList<ResultSet>(1);
        }
        else if (this.resultSetTrace.size() > 0) {
            final int lastIndex = this.resultSetTrace.size() - 1;
            final ResultSet lastResultSet = this.resultSetTrace.get(lastIndex);
            try {
                if (lastResultSet.isClosed()) {
                    this.resultSetTrace.set(lastIndex, resultSet);
                    return;
                }
            }
            catch (SQLException ex) {}
        }
        this.resultSetTrace.add(resultSet);
    }
    
    protected void recordFetchRowCount(final int fetchRowCount) {
        if (this.fetchRowPeak < fetchRowCount) {
            this.fetchRowPeak = fetchRowCount;
        }
    }
    
    public int getFetchRowPeak() {
        return this.fetchRowPeak;
    }
    
    protected SQLException checkException(final Throwable error) throws SQLException {
        String sql = null;
        if (this instanceof DruidPooledPreparedStatement) {
            sql = ((DruidPooledPreparedStatement)this).getSql();
        }
        this.handleSocketTimeout(error);
        ++this.exceptionCount;
        return this.conn.handleException(error, sql);
    }
    
    protected SQLException checkException(final Throwable error, final String sql) throws SQLException {
        this.handleSocketTimeout(error);
        ++this.exceptionCount;
        return this.conn.handleException(error, sql);
    }
    
    protected void handleSocketTimeout(final Throwable error) throws SQLException {
        if (this.conn == null || this.conn.transactionInfo != null || this.conn.holder == null) {
            return;
        }
        DruidDataSource dataSource = null;
        final DruidConnectionHolder holder = this.conn.holder;
        if (holder.dataSource instanceof DruidDataSource) {
            dataSource = (DruidDataSource)holder.dataSource;
        }
        if (dataSource == null) {
            return;
        }
        if (!dataSource.killWhenSocketReadTimeout) {
            return;
        }
        SQLException sqlException = null;
        if (error instanceof SQLException) {
            sqlException = (SQLException)error;
        }
        if (sqlException == null) {
            return;
        }
        final Throwable cause = error.getCause();
        final boolean socketReadTimeout = cause instanceof SocketTimeoutException && "Read timed out".equals(cause.getMessage());
        if (!socketReadTimeout) {
            return;
        }
        if (DbType.mysql != DbType.of(dataSource.dbTypeName)) {
            return;
        }
        final String killQuery = MySqlUtils.buildKillQuerySql(this.conn.getConnection(), (SQLException)error);
        if (killQuery == null) {
            return;
        }
        DruidPooledConnection killQueryConn = null;
        Statement killQueryStmt = null;
        try {
            killQueryConn = dataSource.getConnection(1000L);
            if (killQueryConn == null) {
                return;
            }
            killQueryStmt = killQueryConn.createStatement();
            killQueryStmt.execute(killQuery);
            if (DruidPooledStatement.LOG.isDebugEnabled()) {
                DruidPooledStatement.LOG.debug(killQuery + " success.");
            }
        }
        catch (Exception ex) {
            DruidPooledStatement.LOG.warn(killQuery + " error.", ex);
        }
        finally {
            JdbcUtils.close(killQueryStmt);
            JdbcUtils.close(killQueryConn);
        }
    }
    
    public DruidPooledConnection getPoolableConnection() {
        return this.conn;
    }
    
    public Statement getStatement() {
        return this.stmt;
    }
    
    protected void checkOpen() throws SQLException {
        if (!this.closed) {
            return;
        }
        Throwable disableError = null;
        if (this.conn != null) {
            disableError = this.conn.getDisableError();
        }
        if (disableError != null) {
            throw new SQLException("statement is closed", disableError);
        }
        throw new SQLException("statement is closed");
    }
    
    protected void clearResultSet() {
        if (this.resultSetTrace == null) {
            return;
        }
        for (final ResultSet rs : this.resultSetTrace) {
            try {
                if (rs.isClosed()) {
                    continue;
                }
                rs.close();
            }
            catch (SQLException ex) {
                DruidPooledStatement.LOG.error("clearResultSet error", ex);
            }
        }
        this.resultSetTrace.clear();
    }
    
    public void incrementExecuteCount() {
        final DruidPooledConnection conn = this.getPoolableConnection();
        if (conn == null) {
            return;
        }
        final DruidConnectionHolder holder = conn.getConnectionHolder();
        if (holder == null) {
            return;
        }
        final DruidAbstractDataSource dataSource = holder.getDataSource();
        if (dataSource == null) {
            return;
        }
        dataSource.incrementExecuteCount();
    }
    
    public void incrementExecuteBatchCount() {
        final DruidPooledConnection conn = this.getPoolableConnection();
        if (conn == null) {
            return;
        }
        final DruidConnectionHolder holder = conn.getConnectionHolder();
        if (holder == null) {
            return;
        }
        if (holder.getDataSource() == null) {
            return;
        }
        final DruidAbstractDataSource dataSource = holder.getDataSource();
        if (dataSource == null) {
            return;
        }
        dataSource.incrementExecuteBatchCount();
    }
    
    public void incrementExecuteUpdateCount() {
        final DruidPooledConnection conn = this.getPoolableConnection();
        if (conn == null) {
            return;
        }
        final DruidConnectionHolder holder = conn.getConnectionHolder();
        if (holder == null) {
            return;
        }
        final DruidAbstractDataSource dataSource = holder.getDataSource();
        if (dataSource == null) {
            return;
        }
        dataSource.incrementExecuteUpdateCount();
    }
    
    public void incrementExecuteQueryCount() {
        final DruidPooledConnection conn = this.getPoolableConnection();
        if (conn == null) {
            return;
        }
        final DruidConnectionHolder holder = conn.getConnectionHolder();
        if (holder == null) {
            return;
        }
        final DruidAbstractDataSource dataSource = holder.getDataSource();
        if (dataSource == null) {
            return;
        }
        dataSource.incrementExecuteQueryCount();
    }
    
    protected void transactionRecord(final String sql) throws SQLException {
        this.conn.transactionRecord(sql);
    }
    
    @Override
    public final ResultSet executeQuery(final String sql) throws SQLException {
        this.checkOpen();
        this.incrementExecuteQueryCount();
        this.transactionRecord(sql);
        this.conn.beforeExecute();
        try {
            final ResultSet rs = this.stmt.executeQuery(sql);
            if (rs == null) {
                return rs;
            }
            final DruidPooledResultSet poolableResultSet = new DruidPooledResultSet(this, rs);
            this.addResultSetTrace(poolableResultSet);
            return poolableResultSet;
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t, sql);
        }
        finally {
            this.conn.afterExecute();
        }
    }
    
    @Override
    public final int executeUpdate(final String sql) throws SQLException {
        this.checkOpen();
        this.incrementExecuteUpdateCount();
        this.transactionRecord(sql);
        this.conn.beforeExecute();
        try {
            return this.stmt.executeUpdate(sql);
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t, sql);
        }
        finally {
            this.conn.afterExecute();
        }
    }
    
    protected final void errorCheck(final Throwable t) {
        final String errorClassName = t.getClass().getName();
        if (errorClassName.endsWith(".CommunicationsException") && this.conn.holder != null && this.conn.holder.dataSource.testWhileIdle) {
            final DruidConnectionHolder holder = this.conn.holder;
            final DruidAbstractDataSource dataSource = holder.dataSource;
            final long currentTimeMillis = System.currentTimeMillis();
            long lastActiveTimeMillis = holder.lastActiveTimeMillis;
            if (lastActiveTimeMillis < holder.lastKeepTimeMillis) {
                lastActiveTimeMillis = holder.lastKeepTimeMillis;
            }
            final long idleMillis = currentTimeMillis - lastActiveTimeMillis;
            final long lastValidIdleMillis = currentTimeMillis - holder.lastActiveTimeMillis;
            String errorMsg = "CommunicationsException, druid version " + VERSION.getVersionNumber() + ", jdbcUrl : " + dataSource.jdbcUrl + ", testWhileIdle " + dataSource.testWhileIdle + ", idle millis " + idleMillis + ", minIdle " + dataSource.minIdle + ", poolingCount " + dataSource.getPoolingCount() + ", timeBetweenEvictionRunsMillis " + dataSource.timeBetweenEvictionRunsMillis + ", lastValidIdleMillis " + lastValidIdleMillis + ", driver " + dataSource.driver.getClass().getName();
            if (dataSource.exceptionSorter != null) {
                errorMsg = errorMsg + ", exceptionSorter " + dataSource.exceptionSorter.getClass().getName();
            }
            DruidPooledStatement.LOG.error(errorMsg);
        }
    }
    
    @Override
    public final int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        this.checkOpen();
        this.incrementExecuteUpdateCount();
        this.transactionRecord(sql);
        this.conn.beforeExecute();
        try {
            return this.stmt.executeUpdate(sql, autoGeneratedKeys);
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t, sql);
        }
        finally {
            this.conn.afterExecute();
        }
    }
    
    @Override
    public final int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        this.checkOpen();
        this.incrementExecuteUpdateCount();
        this.transactionRecord(sql);
        this.conn.beforeExecute();
        try {
            return this.stmt.executeUpdate(sql, columnIndexes);
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t, sql);
        }
        finally {
            this.conn.afterExecute();
        }
    }
    
    @Override
    public final int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        this.checkOpen();
        this.incrementExecuteUpdateCount();
        this.transactionRecord(sql);
        this.conn.beforeExecute();
        try {
            return this.stmt.executeUpdate(sql, columnNames);
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t, sql);
        }
        finally {
            this.conn.afterExecute();
        }
    }
    
    @Override
    public final boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        this.checkOpen();
        this.incrementExecuteCount();
        this.transactionRecord(sql);
        this.conn.beforeExecute();
        try {
            return this.stmt.execute(sql, autoGeneratedKeys);
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t, sql);
        }
        finally {
            this.conn.afterExecute();
        }
    }
    
    @Override
    public final boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        this.checkOpen();
        this.incrementExecuteCount();
        this.transactionRecord(sql);
        this.conn.beforeExecute();
        try {
            return this.stmt.execute(sql, columnIndexes);
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t, sql);
        }
        finally {
            this.conn.afterExecute();
        }
    }
    
    @Override
    public final boolean execute(final String sql, final String[] columnNames) throws SQLException {
        this.checkOpen();
        this.incrementExecuteCount();
        this.transactionRecord(sql);
        this.conn.beforeExecute();
        try {
            return this.stmt.execute(sql, columnNames);
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t, sql);
        }
        finally {
            this.conn.afterExecute();
        }
    }
    
    @Override
    public int getMaxFieldSize() throws SQLException {
        this.checkOpen();
        try {
            return this.stmt.getMaxFieldSize();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void close() throws SQLException {
        if (this.closed) {
            return;
        }
        this.clearResultSet();
        if (this.stmt != null) {
            this.stmt.close();
        }
        this.closed = true;
        final DruidConnectionHolder connHolder = this.conn.getConnectionHolder();
        if (connHolder != null) {
            connHolder.removeTrace(this);
        }
    }
    
    @Override
    public void setMaxFieldSize(final int max) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setMaxFieldSize(max);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final int getMaxRows() throws SQLException {
        this.checkOpen();
        try {
            return this.stmt.getMaxRows();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setMaxRows(final int max) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setMaxRows(max);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final void setEscapeProcessing(final boolean enable) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setEscapeProcessing(enable);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final int getQueryTimeout() throws SQLException {
        this.checkOpen();
        try {
            return this.stmt.getQueryTimeout();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setQueryTimeout(final int seconds) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setQueryTimeout(seconds);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final void cancel() throws SQLException {
        this.checkOpen();
        try {
            this.stmt.cancel();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final SQLWarning getWarnings() throws SQLException {
        this.checkOpen();
        try {
            return this.stmt.getWarnings();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final void clearWarnings() throws SQLException {
        this.checkOpen();
        try {
            this.stmt.clearWarnings();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final void setCursorName(final String name) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setCursorName(name);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final boolean execute(final String sql) throws SQLException {
        this.checkOpen();
        this.incrementExecuteCount();
        this.transactionRecord(sql);
        try {
            return this.stmt.execute(sql);
        }
        catch (Throwable t) {
            this.errorCheck(t);
            throw this.checkException(t, sql);
        }
    }
    
    @Override
    public final ResultSet getResultSet() throws SQLException {
        this.checkOpen();
        try {
            final ResultSet rs = this.stmt.getResultSet();
            if (rs == null) {
                return null;
            }
            final DruidPooledResultSet poolableResultSet = new DruidPooledResultSet(this, rs);
            this.addResultSetTrace(poolableResultSet);
            return poolableResultSet;
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final int getUpdateCount() throws SQLException {
        this.checkOpen();
        try {
            return this.stmt.getUpdateCount();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final boolean getMoreResults() throws SQLException {
        this.checkOpen();
        try {
            final boolean moreResults = this.stmt.getMoreResults();
            if (this.resultSetTrace != null && this.resultSetTrace.size() > 0) {
                final ResultSet lastResultSet = this.resultSetTrace.get(this.resultSetTrace.size() - 1);
                if (lastResultSet instanceof DruidPooledResultSet) {
                    final DruidPooledResultSet pooledResultSet = (DruidPooledResultSet)lastResultSet;
                    pooledResultSet.closed = true;
                }
            }
            return moreResults;
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setFetchDirection(direction);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final int getFetchDirection() throws SQLException {
        this.checkOpen();
        try {
            return this.stmt.getFetchDirection();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public void setFetchSize(final int rows) throws SQLException {
        this.checkOpen();
        try {
            this.stmt.setFetchSize(rows);
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final int getFetchSize() throws SQLException {
        this.checkOpen();
        try {
            return this.stmt.getFetchSize();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final int getResultSetConcurrency() throws SQLException {
        this.checkOpen();
        try {
            return this.stmt.getResultSetConcurrency();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final int getResultSetType() throws SQLException {
        this.checkOpen();
        try {
            return this.stmt.getResultSetType();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final void addBatch(final String sql) throws SQLException {
        this.checkOpen();
        this.transactionRecord(sql);
        try {
            this.stmt.addBatch(sql);
        }
        catch (Throwable t) {
            throw this.checkException(t, sql);
        }
    }
    
    @Override
    public final void clearBatch() throws SQLException {
        if (this.closed) {
            return;
        }
        try {
            this.stmt.clearBatch();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public int[] executeBatch() throws SQLException {
        this.checkOpen();
        this.incrementExecuteBatchCount();
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
    public final Connection getConnection() throws SQLException {
        this.checkOpen();
        return this.conn;
    }
    
    @Override
    public final boolean getMoreResults(final int current) throws SQLException {
        this.checkOpen();
        try {
            final boolean results = this.stmt.getMoreResults(current);
            if (this.resultSetTrace != null && this.resultSetTrace.size() > 0) {
                final ResultSet lastResultSet = this.resultSetTrace.get(this.resultSetTrace.size() - 1);
                if (lastResultSet instanceof DruidPooledResultSet) {
                    final DruidPooledResultSet pooledResultSet = (DruidPooledResultSet)lastResultSet;
                    pooledResultSet.closed = true;
                }
            }
            return results;
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final ResultSet getGeneratedKeys() throws SQLException {
        this.checkOpen();
        try {
            final ResultSet rs = this.stmt.getGeneratedKeys();
            final DruidPooledResultSet poolableResultSet = new DruidPooledResultSet(this, rs);
            this.addResultSetTrace(poolableResultSet);
            return poolableResultSet;
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final int getResultSetHoldability() throws SQLException {
        this.checkOpen();
        try {
            return this.stmt.getResultSetHoldability();
        }
        catch (Throwable t) {
            throw this.checkException(t);
        }
    }
    
    @Override
    public final boolean isClosed() throws SQLException {
        return this.closed;
    }
    
    @Override
    public final void setPoolable(final boolean poolable) throws SQLException {
        if (poolable) {
            return;
        }
        throw new SQLException("not support");
    }
    
    @Override
    public final boolean isPoolable() throws SQLException {
        return false;
    }
    
    @Override
    public String toString() {
        return this.stmt.toString();
    }
    
    @Override
    public void closeOnCompletion() throws SQLException {
        this.stmt.closeOnCompletion();
    }
    
    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return this.stmt.isCloseOnCompletion();
    }
    
    static {
        LOG = LogFactory.getLog(DruidPooledStatement.class);
    }
}
