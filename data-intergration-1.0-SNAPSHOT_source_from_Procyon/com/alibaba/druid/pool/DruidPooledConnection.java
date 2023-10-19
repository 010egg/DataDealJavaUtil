// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.concurrent.Executor;
import java.sql.SQLFeatureNotSupportedException;
import com.alibaba.druid.util.JdbcUtils;
import javax.sql.StatementEventListener;
import java.sql.Struct;
import java.sql.Array;
import java.util.Properties;
import java.sql.SQLClientInfoException;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.Blob;
import java.util.Map;
import java.sql.SQLWarning;
import java.sql.DatabaseMetaData;
import java.sql.Clob;
import java.sql.Savepoint;
import java.sql.CallableStatement;
import java.sql.Statement;
import com.alibaba.druid.filter.Filter;
import java.util.List;
import java.util.Iterator;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.alibaba.druid.filter.FilterChainImpl;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import com.alibaba.druid.proxy.jdbc.TransactionInfo;
import com.alibaba.druid.support.logging.Log;
import java.sql.Connection;
import javax.sql.PooledConnection;

public class DruidPooledConnection extends PoolableWrapper implements PooledConnection, Connection
{
    private static final Log LOG;
    public static final int MAX_RECORD_SQL_COUNT = 10;
    protected Connection conn;
    protected volatile DruidConnectionHolder holder;
    protected TransactionInfo transactionInfo;
    private final boolean dupCloseLogEnable;
    protected volatile boolean traceEnable;
    private volatile boolean disable;
    protected volatile boolean closed;
    static AtomicIntegerFieldUpdater CLOSING_UPDATER;
    protected final Thread ownerThread;
    private long connectedTimeMillis;
    private long connectedTimeNano;
    private volatile boolean running;
    private volatile boolean abandoned;
    protected StackTraceElement[] connectStackTrace;
    protected Throwable disableError;
    final ReentrantLock lock;
    protected volatile int closing;
    
    public DruidPooledConnection(final DruidConnectionHolder holder) {
        super(holder.getConnection());
        this.traceEnable = false;
        this.disable = false;
        this.closed = false;
        this.running = false;
        this.abandoned = false;
        this.disableError = null;
        this.closing = 0;
        this.conn = holder.getConnection();
        this.holder = holder;
        this.lock = holder.lock;
        this.dupCloseLogEnable = holder.getDataSource().isDupCloseLogEnable();
        this.ownerThread = Thread.currentThread();
        this.connectedTimeMillis = System.currentTimeMillis();
    }
    
    public long getConnectedTimeMillis() {
        return this.connectedTimeMillis;
    }
    
    public Thread getOwnerThread() {
        return this.ownerThread;
    }
    
    public StackTraceElement[] getConnectStackTrace() {
        return this.connectStackTrace;
    }
    
    public void setConnectStackTrace(final StackTraceElement[] connectStackTrace) {
        this.connectStackTrace = connectStackTrace;
    }
    
    public long getConnectedTimeNano() {
        return this.connectedTimeNano;
    }
    
    public void setConnectedTimeNano() {
        if (this.connectedTimeNano <= 0L) {
            this.setConnectedTimeNano(System.nanoTime());
        }
    }
    
    public void setConnectedTimeNano(final long connectedTimeNano) {
        this.connectedTimeNano = connectedTimeNano;
    }
    
    public boolean isTraceEnable() {
        return this.traceEnable;
    }
    
    public void setTraceEnable(final boolean traceEnable) {
        this.traceEnable = traceEnable;
    }
    
    public SQLException handleException(final Throwable t) throws SQLException {
        return this.handleException(t, null);
    }
    
    public SQLException handleException(final Throwable t, final String sql) throws SQLException {
        final DruidConnectionHolder holder = this.holder;
        if (holder != null) {
            final DruidAbstractDataSource dataSource = holder.getDataSource();
            dataSource.handleConnectionException(this, t, sql);
        }
        if (t instanceof SQLException) {
            throw (SQLException)t;
        }
        throw new SQLException("Error", t);
    }
    
    public boolean isOracle() {
        return this.holder.getDataSource().isOracle();
    }
    
    public void closePoolableStatement(final DruidPooledPreparedStatement stmt) throws SQLException {
        final PreparedStatement rawStatement = stmt.getRawPreparedStatement();
        final DruidConnectionHolder holder = this.holder;
        if (holder == null) {
            return;
        }
        if (stmt.isPooled()) {
            try {
                rawStatement.clearParameters();
            }
            catch (SQLException ex) {
                this.handleException(ex, null);
                if (rawStatement.getConnection().isClosed()) {
                    return;
                }
                DruidPooledConnection.LOG.error("clear parameter error", ex);
            }
            try {
                rawStatement.clearBatch();
            }
            catch (SQLException ex) {
                this.handleException(ex, null);
                if (rawStatement.getConnection().isClosed()) {
                    return;
                }
                DruidPooledConnection.LOG.error("clear batch error", ex);
            }
        }
        final PreparedStatementHolder stmtHolder = stmt.getPreparedStatementHolder();
        stmtHolder.decrementInUseCount();
        if (stmt.isPooled() && holder.isPoolPreparedStatements() && stmt.exceptionCount == 0) {
            holder.getStatementPool().put(stmtHolder);
            stmt.clearResultSet();
            holder.removeTrace(stmt);
            stmtHolder.setFetchRowPeak(stmt.getFetchRowPeak());
            stmt.setClosed(true);
        }
        else if (stmt.isPooled() && holder.isPoolPreparedStatements()) {
            stmt.clearResultSet();
            holder.removeTrace(stmt);
            holder.getStatementPool().remove(stmtHolder);
        }
        else {
            try {
                stmt.closeInternal();
            }
            catch (SQLException ex2) {
                this.handleException(ex2, null);
                throw ex2;
            }
            finally {
                holder.getDataSource().incrementClosedPreparedStatementCount();
            }
        }
    }
    
    public DruidConnectionHolder getConnectionHolder() {
        return this.holder;
    }
    
    @Override
    public Connection getConnection() {
        if (!this.holder.underlyingAutoCommit) {
            this.createTransactionInfo();
        }
        return this.conn;
    }
    
    public void disable() {
        this.disable(null);
    }
    
    public void disable(final Throwable error) {
        if (this.holder != null) {
            this.holder.clearStatementCache();
        }
        this.traceEnable = false;
        this.holder = null;
        this.transactionInfo = null;
        this.disable = true;
        this.disableError = error;
    }
    
    public boolean isDisable() {
        return this.disable;
    }
    
    @Override
    public void close() throws SQLException {
        if (this.disable) {
            return;
        }
        final DruidConnectionHolder holder = this.holder;
        if (holder == null) {
            if (this.dupCloseLogEnable) {
                DruidPooledConnection.LOG.error("dup close");
            }
            return;
        }
        final DruidAbstractDataSource dataSource = holder.getDataSource();
        final boolean isSameThread = this.getOwnerThread() == Thread.currentThread();
        if (!isSameThread) {
            dataSource.setAsyncCloseConnectionEnable(true);
        }
        if (dataSource.isAsyncCloseConnectionEnable()) {
            this.syncClose();
            return;
        }
        if (!DruidPooledConnection.CLOSING_UPDATER.compareAndSet(this, 0, 1)) {
            return;
        }
        try {
            for (final ConnectionEventListener listener : holder.getConnectionEventListeners()) {
                listener.connectionClosed(new ConnectionEvent(this));
            }
            final List<Filter> filters = dataSource.getProxyFilters();
            if (filters.size() > 0) {
                final FilterChainImpl filterChain = new FilterChainImpl(dataSource);
                filterChain.dataSource_recycle(this);
            }
            else {
                this.recycle();
            }
        }
        finally {
            DruidPooledConnection.CLOSING_UPDATER.set(this, 0);
        }
        this.disable = true;
    }
    
    public void syncClose() throws SQLException {
        this.lock.lock();
        try {
            if (this.disable || DruidPooledConnection.CLOSING_UPDATER.get(this) != 0) {
                return;
            }
            final DruidConnectionHolder holder = this.holder;
            if (holder == null) {
                if (this.dupCloseLogEnable) {
                    DruidPooledConnection.LOG.error("dup close");
                }
                return;
            }
            if (!DruidPooledConnection.CLOSING_UPDATER.compareAndSet(this, 0, 1)) {
                return;
            }
            for (final ConnectionEventListener listener : holder.getConnectionEventListeners()) {
                listener.connectionClosed(new ConnectionEvent(this));
            }
            final DruidAbstractDataSource dataSource = holder.getDataSource();
            final List<Filter> filters = dataSource.getProxyFilters();
            if (filters.size() > 0) {
                final FilterChainImpl filterChain = new FilterChainImpl(dataSource);
                filterChain.dataSource_recycle(this);
            }
            else {
                this.recycle();
            }
            this.disable = true;
        }
        finally {
            DruidPooledConnection.CLOSING_UPDATER.set(this, 0);
            this.lock.unlock();
        }
    }
    
    public void recycle() throws SQLException {
        if (this.disable) {
            return;
        }
        final DruidConnectionHolder holder = this.holder;
        if (holder == null) {
            if (this.dupCloseLogEnable) {
                DruidPooledConnection.LOG.error("dup close");
            }
            return;
        }
        if (!this.abandoned) {
            final DruidAbstractDataSource dataSource = holder.getDataSource();
            dataSource.recycle(this);
        }
        this.holder = null;
        this.conn = null;
        this.transactionInfo = null;
        this.closed = true;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        this.checkState();
        PreparedStatementHolder stmtHolder = null;
        final DruidPooledPreparedStatement.PreparedStatementKey key = new DruidPooledPreparedStatement.PreparedStatementKey(sql, this.getCatalog(), PreparedStatementPool.MethodType.M1);
        final boolean poolPreparedStatements = this.holder.isPoolPreparedStatements();
        if (poolPreparedStatements) {
            stmtHolder = this.holder.getStatementPool().get(key);
        }
        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, this.conn.prepareStatement(sql));
                this.holder.getDataSource().incrementPreparedStatementCount();
            }
            catch (SQLException ex) {
                this.handleException(ex, sql);
            }
        }
        this.initStatement(stmtHolder);
        final DruidPooledPreparedStatement rtnVal = new DruidPooledPreparedStatement(this, stmtHolder);
        this.holder.addTrace(rtnVal);
        return rtnVal;
    }
    
    private void initStatement(final PreparedStatementHolder stmtHolder) throws SQLException {
        stmtHolder.incrementInUseCount();
        this.holder.getDataSource().initStatement(this, stmtHolder.statement);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        this.checkState();
        PreparedStatementHolder stmtHolder = null;
        final DruidPooledPreparedStatement.PreparedStatementKey key = new DruidPooledPreparedStatement.PreparedStatementKey(sql, this.getCatalog(), PreparedStatementPool.MethodType.M2, resultSetType, resultSetConcurrency);
        final boolean poolPreparedStatements = this.holder.isPoolPreparedStatements();
        if (poolPreparedStatements) {
            stmtHolder = this.holder.getStatementPool().get(key);
        }
        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency));
                this.holder.getDataSource().incrementPreparedStatementCount();
            }
            catch (SQLException ex) {
                this.handleException(ex, sql);
            }
        }
        this.initStatement(stmtHolder);
        final DruidPooledPreparedStatement rtnVal = new DruidPooledPreparedStatement(this, stmtHolder);
        this.holder.addTrace(rtnVal);
        return rtnVal;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        this.checkState();
        PreparedStatementHolder stmtHolder = null;
        final DruidPooledPreparedStatement.PreparedStatementKey key = new DruidPooledPreparedStatement.PreparedStatementKey(sql, this.getCatalog(), PreparedStatementPool.MethodType.M3, resultSetType, resultSetConcurrency, resultSetHoldability);
        final boolean poolPreparedStatements = this.holder.isPoolPreparedStatements();
        if (poolPreparedStatements) {
            stmtHolder = this.holder.getStatementPool().get(key);
        }
        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
                this.holder.getDataSource().incrementPreparedStatementCount();
            }
            catch (SQLException ex) {
                this.handleException(ex, sql);
            }
        }
        this.initStatement(stmtHolder);
        final DruidPooledPreparedStatement rtnVal = new DruidPooledPreparedStatement(this, stmtHolder);
        this.holder.addTrace(rtnVal);
        return rtnVal;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
        this.checkState();
        final DruidPooledPreparedStatement.PreparedStatementKey key = new DruidPooledPreparedStatement.PreparedStatementKey(sql, this.getCatalog(), PreparedStatementPool.MethodType.M4, columnIndexes);
        PreparedStatementHolder stmtHolder = null;
        final boolean poolPreparedStatements = this.holder.isPoolPreparedStatements();
        if (poolPreparedStatements) {
            stmtHolder = this.holder.getStatementPool().get(key);
        }
        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, this.conn.prepareStatement(sql, columnIndexes));
                this.holder.getDataSource().incrementPreparedStatementCount();
            }
            catch (SQLException ex) {
                this.handleException(ex, sql);
            }
        }
        this.initStatement(stmtHolder);
        final DruidPooledPreparedStatement rtnVal = new DruidPooledPreparedStatement(this, stmtHolder);
        this.holder.addTrace(rtnVal);
        return rtnVal;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
        this.checkState();
        final DruidPooledPreparedStatement.PreparedStatementKey key = new DruidPooledPreparedStatement.PreparedStatementKey(sql, this.getCatalog(), PreparedStatementPool.MethodType.M5, columnNames);
        PreparedStatementHolder stmtHolder = null;
        final boolean poolPreparedStatements = this.holder.isPoolPreparedStatements();
        if (poolPreparedStatements) {
            stmtHolder = this.holder.getStatementPool().get(key);
        }
        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, this.conn.prepareStatement(sql, columnNames));
                this.holder.getDataSource().incrementPreparedStatementCount();
            }
            catch (SQLException ex) {
                this.handleException(ex, sql);
            }
        }
        this.initStatement(stmtHolder);
        final DruidPooledPreparedStatement rtnVal = new DruidPooledPreparedStatement(this, stmtHolder);
        this.holder.addTrace(rtnVal);
        return rtnVal;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
        this.checkState();
        final DruidPooledPreparedStatement.PreparedStatementKey key = new DruidPooledPreparedStatement.PreparedStatementKey(sql, this.getCatalog(), PreparedStatementPool.MethodType.M6, autoGeneratedKeys);
        PreparedStatementHolder stmtHolder = null;
        final boolean poolPreparedStatements = this.holder.isPoolPreparedStatements();
        if (poolPreparedStatements) {
            stmtHolder = this.holder.getStatementPool().get(key);
        }
        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, this.conn.prepareStatement(sql, autoGeneratedKeys));
                this.holder.getDataSource().incrementPreparedStatementCount();
            }
            catch (SQLException ex) {
                this.handleException(ex, sql);
            }
        }
        this.initStatement(stmtHolder);
        final DruidPooledPreparedStatement rtnVal = new DruidPooledPreparedStatement(this, stmtHolder);
        this.holder.addTrace(rtnVal);
        return rtnVal;
    }
    
    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        this.checkState();
        PreparedStatementHolder stmtHolder = null;
        final DruidPooledPreparedStatement.PreparedStatementKey key = new DruidPooledPreparedStatement.PreparedStatementKey(sql, this.getCatalog(), PreparedStatementPool.MethodType.Precall_1);
        final boolean poolPreparedStatements = this.holder.isPoolPreparedStatements();
        if (poolPreparedStatements) {
            stmtHolder = this.holder.getStatementPool().get(key);
        }
        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, this.conn.prepareCall(sql));
                this.holder.getDataSource().incrementPreparedStatementCount();
            }
            catch (SQLException ex) {
                this.handleException(ex, sql);
            }
        }
        this.initStatement(stmtHolder);
        final DruidPooledCallableStatement rtnVal = new DruidPooledCallableStatement(this, stmtHolder);
        this.holder.addTrace(rtnVal);
        return rtnVal;
    }
    
    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        this.checkState();
        PreparedStatementHolder stmtHolder = null;
        final DruidPooledPreparedStatement.PreparedStatementKey key = new DruidPooledPreparedStatement.PreparedStatementKey(sql, this.getCatalog(), PreparedStatementPool.MethodType.Precall_2, resultSetType, resultSetConcurrency, resultSetHoldability);
        final boolean poolPreparedStatements = this.holder.isPoolPreparedStatements();
        if (poolPreparedStatements) {
            stmtHolder = this.holder.getStatementPool().get(key);
        }
        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, this.conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
                this.holder.getDataSource().incrementPreparedStatementCount();
            }
            catch (SQLException ex) {
                this.handleException(ex, sql);
            }
        }
        this.initStatement(stmtHolder);
        final DruidPooledCallableStatement rtnVal = new DruidPooledCallableStatement(this, stmtHolder);
        this.holder.addTrace(rtnVal);
        return rtnVal;
    }
    
    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        this.checkState();
        PreparedStatementHolder stmtHolder = null;
        final DruidPooledPreparedStatement.PreparedStatementKey key = new DruidPooledPreparedStatement.PreparedStatementKey(sql, this.getCatalog(), PreparedStatementPool.MethodType.Precall_3, resultSetType, resultSetConcurrency);
        final boolean poolPreparedStatements = this.holder.isPoolPreparedStatements();
        if (poolPreparedStatements) {
            stmtHolder = this.holder.getStatementPool().get(key);
        }
        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, this.conn.prepareCall(sql, resultSetType, resultSetConcurrency));
                this.holder.getDataSource().incrementPreparedStatementCount();
            }
            catch (SQLException ex) {
                this.handleException(ex, sql);
            }
        }
        this.initStatement(stmtHolder);
        final DruidPooledCallableStatement rtnVal = new DruidPooledCallableStatement(this, stmtHolder);
        this.holder.addTrace(rtnVal);
        return rtnVal;
    }
    
    @Override
    public Statement createStatement() throws SQLException {
        this.checkState();
        Statement stmt = null;
        try {
            stmt = this.conn.createStatement();
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
        }
        this.holder.getDataSource().initStatement(this, stmt);
        final DruidPooledStatement poolableStatement = new DruidPooledStatement(this, stmt);
        this.holder.addTrace(poolableStatement);
        return poolableStatement;
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        this.checkState();
        Statement stmt = null;
        try {
            stmt = this.conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
        }
        this.holder.getDataSource().initStatement(this, stmt);
        final DruidPooledStatement poolableStatement = new DruidPooledStatement(this, stmt);
        this.holder.addTrace(poolableStatement);
        return poolableStatement;
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
        this.checkState();
        Statement stmt = null;
        try {
            stmt = this.conn.createStatement(resultSetType, resultSetConcurrency);
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
        }
        this.holder.getDataSource().initStatement(this, stmt);
        final DruidPooledStatement poolableStatement = new DruidPooledStatement(this, stmt);
        this.holder.addTrace(poolableStatement);
        return poolableStatement;
    }
    
    @Override
    public String nativeSQL(final String sql) throws SQLException {
        this.checkState();
        return this.conn.nativeSQL(sql);
    }
    
    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        this.checkState();
        final boolean useLocalSessionState = this.holder.getDataSource().isUseLocalSessionState();
        if (useLocalSessionState && autoCommit == this.holder.underlyingAutoCommit) {
            return;
        }
        try {
            this.conn.setAutoCommit(autoCommit);
            this.holder.setUnderlyingAutoCommit(autoCommit);
            this.holder.setLastExecTimeMillis(System.currentTimeMillis());
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
        }
    }
    
    protected void transactionRecord(final String sql) throws SQLException {
        if (this.holder != null) {
            this.holder.setLastExecTimeMillis(System.currentTimeMillis());
        }
        if (this.transactionInfo == null && !this.conn.getAutoCommit()) {
            final DruidAbstractDataSource dataSource = this.holder.getDataSource();
            dataSource.incrementStartTransactionCount();
            this.transactionInfo = new TransactionInfo(dataSource.createTransactionId());
        }
        if (this.transactionInfo != null) {
            final List<String> sqlList = this.transactionInfo.getSqlList();
            if (sqlList.size() < 10) {
                sqlList.add(sql);
            }
        }
    }
    
    @Override
    public boolean getAutoCommit() throws SQLException {
        this.checkState();
        return this.conn.getAutoCommit();
    }
    
    @Override
    public void commit() throws SQLException {
        this.checkState();
        final DruidAbstractDataSource dataSource = this.holder.getDataSource();
        dataSource.incrementCommitCount();
        try {
            this.conn.commit();
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
        }
        finally {
            this.handleEndTransaction(dataSource, null);
        }
    }
    
    public TransactionInfo getTransactionInfo() {
        return this.transactionInfo;
    }
    
    protected void createTransactionInfo() {
        final DruidAbstractDataSource dataSource = this.holder.getDataSource();
        dataSource.incrementStartTransactionCount();
        this.transactionInfo = new TransactionInfo(dataSource.createTransactionId());
    }
    
    @Override
    public void rollback() throws SQLException {
        if (this.transactionInfo == null) {
            return;
        }
        if (this.holder == null) {
            return;
        }
        final DruidAbstractDataSource dataSource = this.holder.getDataSource();
        dataSource.incrementRollbackCount();
        try {
            this.conn.rollback();
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
        }
        finally {
            this.handleEndTransaction(dataSource, null);
        }
    }
    
    @Override
    public Savepoint setSavepoint(final String name) throws SQLException {
        this.checkState();
        try {
            return this.conn.setSavepoint(name);
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
            return null;
        }
    }
    
    @Override
    public void rollback(final Savepoint savepoint) throws SQLException {
        if (this.holder == null) {
            return;
        }
        final DruidAbstractDataSource dataSource = this.holder.getDataSource();
        dataSource.incrementRollbackCount();
        try {
            this.conn.rollback(savepoint);
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
        }
        finally {
            this.handleEndTransaction(dataSource, savepoint);
        }
    }
    
    private void handleEndTransaction(final DruidAbstractDataSource dataSource, final Savepoint savepoint) {
        if (this.transactionInfo != null && savepoint == null) {
            this.transactionInfo.setEndTimeMillis();
            final long transactionMillis = this.transactionInfo.getEndTimeMillis() - this.transactionInfo.getStartTimeMillis();
            dataSource.getTransactionHistogram().record(transactionMillis);
            dataSource.logTransaction(this.transactionInfo);
            this.transactionInfo = null;
        }
    }
    
    @Override
    public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
        this.checkState();
        try {
            this.conn.releaseSavepoint(savepoint);
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
        }
    }
    
    @Override
    public Clob createClob() throws SQLException {
        this.checkState();
        try {
            return this.conn.createClob();
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
            return null;
        }
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        return this.holder == null || this.closed || this.disable;
    }
    
    public boolean isAbandonded() {
        return this.abandoned;
    }
    
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        this.checkState();
        if (!this.holder.underlyingAutoCommit) {
            this.createTransactionInfo();
        }
        return this.conn.getMetaData();
    }
    
    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        this.checkState();
        final boolean useLocalSessionState = this.holder.getDataSource().isUseLocalSessionState();
        if (useLocalSessionState && readOnly == this.holder.isUnderlyingReadOnly()) {
            return;
        }
        try {
            this.conn.setReadOnly(readOnly);
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
        }
        this.holder.setUnderlyingReadOnly(readOnly);
    }
    
    @Override
    public boolean isReadOnly() throws SQLException {
        this.checkState();
        return this.conn.isReadOnly();
    }
    
    @Override
    public void setCatalog(final String catalog) throws SQLException {
        this.checkState();
        try {
            this.conn.setCatalog(catalog);
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
        }
    }
    
    @Override
    public String getCatalog() throws SQLException {
        this.checkState();
        return this.conn.getCatalog();
    }
    
    @Override
    public void setTransactionIsolation(final int level) throws SQLException {
        this.checkState();
        final boolean useLocalSessionState = this.holder.getDataSource().isUseLocalSessionState();
        if (useLocalSessionState && level == this.holder.getUnderlyingTransactionIsolation()) {
            return;
        }
        try {
            this.conn.setTransactionIsolation(level);
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
        }
        this.holder.setUnderlyingTransactionIsolation(level);
    }
    
    @Override
    public int getTransactionIsolation() throws SQLException {
        this.checkState();
        return this.holder.getUnderlyingTransactionIsolation();
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        this.checkState();
        return this.conn.getWarnings();
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        this.checkState();
        try {
            this.conn.clearWarnings();
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
        }
    }
    
    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        this.checkState();
        return this.conn.getTypeMap();
    }
    
    @Override
    public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
        this.checkState();
        this.conn.setTypeMap(map);
    }
    
    @Override
    public void setHoldability(final int holdability) throws SQLException {
        this.checkState();
        final boolean useLocalSessionState = this.holder.getDataSource().isUseLocalSessionState();
        if (useLocalSessionState && holdability == this.holder.getUnderlyingHoldability()) {
            return;
        }
        this.conn.setHoldability(holdability);
        this.holder.setUnderlyingHoldability(holdability);
    }
    
    @Override
    public int getHoldability() throws SQLException {
        this.checkState();
        if (!this.holder.underlyingAutoCommit) {
            this.createTransactionInfo();
        }
        return this.conn.getHoldability();
    }
    
    @Override
    public Savepoint setSavepoint() throws SQLException {
        this.checkState();
        try {
            return this.conn.setSavepoint();
        }
        catch (SQLException ex) {
            this.handleException(ex, null);
            return null;
        }
    }
    
    @Override
    public Blob createBlob() throws SQLException {
        this.checkState();
        return this.conn.createBlob();
    }
    
    @Override
    public NClob createNClob() throws SQLException {
        this.checkState();
        return this.conn.createNClob();
    }
    
    @Override
    public SQLXML createSQLXML() throws SQLException {
        this.checkState();
        return this.conn.createSQLXML();
    }
    
    @Override
    public boolean isValid(final int timeout) throws SQLException {
        this.checkState();
        return this.conn.isValid(timeout);
    }
    
    @Override
    public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
        if (this.holder == null) {
            throw new SQLClientInfoException();
        }
        this.conn.setClientInfo(name, value);
    }
    
    @Override
    public void setClientInfo(final Properties properties) throws SQLClientInfoException {
        if (this.holder == null) {
            throw new SQLClientInfoException();
        }
        this.conn.setClientInfo(properties);
    }
    
    @Override
    public String getClientInfo(final String name) throws SQLException {
        this.checkState();
        return this.conn.getClientInfo(name);
    }
    
    @Override
    public Properties getClientInfo() throws SQLException {
        this.checkState();
        return this.conn.getClientInfo();
    }
    
    @Override
    public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
        this.checkState();
        return this.conn.createArrayOf(typeName, elements);
    }
    
    @Override
    public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
        this.checkState();
        return this.conn.createStruct(typeName, attributes);
    }
    
    @Override
    public void addConnectionEventListener(final ConnectionEventListener listener) {
        if (this.holder == null) {
            throw new IllegalStateException();
        }
        this.holder.getConnectionEventListeners().add(listener);
    }
    
    @Override
    public void removeConnectionEventListener(final ConnectionEventListener listener) {
        if (this.holder == null) {
            throw new IllegalStateException();
        }
        this.holder.getConnectionEventListeners().remove(listener);
    }
    
    @Override
    public void addStatementEventListener(final StatementEventListener listener) {
        if (this.holder == null) {
            throw new IllegalStateException();
        }
        this.holder.getStatementEventListeners().add(listener);
    }
    
    @Override
    public void removeStatementEventListener(final StatementEventListener listener) {
        if (this.holder == null) {
            throw new IllegalStateException();
        }
        this.holder.getStatementEventListeners().remove(listener);
    }
    
    public Throwable getDisableError() {
        return this.disableError;
    }
    
    public void checkState() throws SQLException {
        final boolean asyncCloseEnabled = this.holder != null && this.holder.getDataSource().isAsyncCloseConnectionEnable();
        if (asyncCloseEnabled) {
            this.lock.lock();
            try {
                this.checkStateInternal();
            }
            finally {
                this.lock.unlock();
            }
        }
        else {
            this.checkStateInternal();
        }
    }
    
    private void checkStateInternal() throws SQLException {
        if (this.closed) {
            if (this.disableError != null) {
                throw new SQLException("connection closed", this.disableError);
            }
            throw new SQLException("connection closed");
        }
        else if (this.disable) {
            if (this.disableError != null) {
                throw new SQLException("connection disabled", this.disableError);
            }
            throw new SQLException("connection disabled");
        }
        else {
            if (this.holder != null) {
                return;
            }
            if (this.disableError != null) {
                throw new SQLException("connection holder is null", this.disableError);
            }
            throw new SQLException("connection holder is null");
        }
    }
    
    @Override
    public String toString() {
        if (this.conn != null) {
            return this.conn.toString();
        }
        return "closed-conn-" + System.identityHashCode(this);
    }
    
    @Override
    public void setSchema(final String schema) throws SQLException {
        if (JdbcUtils.isMysqlDbType(this.holder.dataSource.getDbType())) {
            if (this.holder.initSchema == null) {
                this.holder.initSchema = this.conn.getSchema();
            }
            this.conn.setSchema(schema);
            if (this.holder.statementPool != null) {
                this.holder.clearStatementCache();
            }
            return;
        }
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public String getSchema() throws SQLException {
        return this.conn.getSchema();
    }
    
    @Override
    public void abort(final Executor executor) throws SQLException {
        this.conn.abort(executor);
    }
    
    @Override
    public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
        this.conn.setNetworkTimeout(executor, milliseconds);
    }
    
    @Override
    public int getNetworkTimeout() throws SQLException {
        return this.conn.getNetworkTimeout();
    }
    
    final void beforeExecute() {
        final DruidConnectionHolder holder = this.holder;
        if (holder != null && holder.dataSource.removeAbandoned) {
            this.running = true;
        }
    }
    
    final void afterExecute() {
        final DruidConnectionHolder holder = this.holder;
        if (holder != null) {
            final DruidAbstractDataSource dataSource = holder.dataSource;
            if (dataSource.removeAbandoned) {
                this.running = false;
                holder.lastActiveTimeMillis = System.currentTimeMillis();
            }
            dataSource.onFatalError = false;
        }
    }
    
    boolean isRunning() {
        return this.running;
    }
    
    public void abandond() {
        this.abandoned = true;
    }
    
    public long getPhysicalConnectNanoSpan() {
        return this.holder.getCreateNanoSpan();
    }
    
    public long getPhysicalConnectionUsedCount() {
        return this.holder.getUseCount();
    }
    
    public long getConnectNotEmptyWaitNanos() {
        return this.holder.getLastNotEmptyWaitNanos();
    }
    
    public Map<String, Object> getVariables() {
        return this.holder.variables;
    }
    
    public Map<String, Object> getGloablVariables() {
        return this.holder.globleVariables;
    }
    
    static {
        LOG = LogFactory.getLog(DruidPooledConnection.class);
        DruidPooledConnection.CLOSING_UPDATER = AtomicIntegerFieldUpdater.newUpdater(DruidPooledConnection.class, "closing");
    }
}
