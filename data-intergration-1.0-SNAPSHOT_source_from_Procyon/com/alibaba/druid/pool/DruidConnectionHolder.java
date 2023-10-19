// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.Utils;
import java.util.Date;
import com.alibaba.druid.util.JdbcUtils;
import java.sql.SQLFeatureNotSupportedException;
import com.alibaba.druid.DbType;
import com.alibaba.druid.proxy.jdbc.WrapperProxy;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Map;
import java.sql.Statement;
import javax.sql.StatementEventListener;
import javax.sql.ConnectionEventListener;
import java.util.List;
import java.sql.Connection;
import com.alibaba.druid.support.logging.Log;

public final class DruidConnectionHolder
{
    private static final Log LOG;
    public static boolean holdabilityUnsupported;
    protected final DruidAbstractDataSource dataSource;
    protected final long connectionId;
    protected final Connection conn;
    protected final List<ConnectionEventListener> connectionEventListeners;
    protected final List<StatementEventListener> statementEventListeners;
    protected final long connectTimeMillis;
    protected volatile long lastActiveTimeMillis;
    protected volatile long lastExecTimeMillis;
    protected volatile long lastKeepTimeMillis;
    protected volatile long lastValidTimeMillis;
    protected long useCount;
    private long keepAliveCheckCount;
    private long lastNotEmptyWaitNanos;
    private final long createNanoSpan;
    protected PreparedStatementPool statementPool;
    protected final List<Statement> statementTrace;
    protected final boolean defaultReadOnly;
    protected final int defaultHoldability;
    protected final int defaultTransactionIsolation;
    protected final boolean defaultAutoCommit;
    protected boolean underlyingReadOnly;
    protected int underlyingHoldability;
    protected int underlyingTransactionIsolation;
    protected boolean underlyingAutoCommit;
    protected volatile boolean discard;
    protected volatile boolean active;
    protected final Map<String, Object> variables;
    protected final Map<String, Object> globleVariables;
    final ReentrantLock lock;
    protected String initSchema;
    
    public DruidConnectionHolder(final DruidAbstractDataSource dataSource, final DruidAbstractDataSource.PhysicalConnectionInfo pyConnectInfo) throws SQLException {
        this(dataSource, pyConnectInfo.getPhysicalConnection(), pyConnectInfo.getConnectNanoSpan(), pyConnectInfo.getVairiables(), pyConnectInfo.getGlobalVairiables());
    }
    
    public DruidConnectionHolder(final DruidAbstractDataSource dataSource, final Connection conn, final long connectNanoSpan) throws SQLException {
        this(dataSource, conn, connectNanoSpan, null, null);
    }
    
    public DruidConnectionHolder(final DruidAbstractDataSource dataSource, final Connection conn, final long connectNanoSpan, final Map<String, Object> variables, final Map<String, Object> globleVariables) throws SQLException {
        this.connectionEventListeners = new CopyOnWriteArrayList<ConnectionEventListener>();
        this.statementEventListeners = new CopyOnWriteArrayList<StatementEventListener>();
        this.useCount = 0L;
        this.keepAliveCheckCount = 0L;
        this.statementTrace = new ArrayList<Statement>(2);
        this.discard = false;
        this.active = false;
        this.lock = new ReentrantLock();
        this.dataSource = dataSource;
        this.conn = conn;
        this.createNanoSpan = connectNanoSpan;
        this.variables = variables;
        this.globleVariables = globleVariables;
        this.connectTimeMillis = System.currentTimeMillis();
        this.lastActiveTimeMillis = this.connectTimeMillis;
        this.lastExecTimeMillis = this.connectTimeMillis;
        this.underlyingAutoCommit = conn.getAutoCommit();
        if (conn instanceof WrapperProxy) {
            this.connectionId = ((WrapperProxy)conn).getId();
        }
        else {
            this.connectionId = dataSource.createConnectionId();
        }
        boolean initUnderlyHoldability = !DruidConnectionHolder.holdabilityUnsupported;
        final DbType dbType = DbType.of(dataSource.dbTypeName);
        if (dbType == DbType.sybase || dbType == DbType.db2 || dbType == DbType.hive || dbType == DbType.odps) {
            initUnderlyHoldability = false;
        }
        if (initUnderlyHoldability) {
            try {
                this.underlyingHoldability = conn.getHoldability();
            }
            catch (UnsupportedOperationException e) {
                DruidConnectionHolder.holdabilityUnsupported = true;
                DruidConnectionHolder.LOG.warn("getHoldability unsupported", e);
            }
            catch (SQLFeatureNotSupportedException e2) {
                DruidConnectionHolder.holdabilityUnsupported = true;
                DruidConnectionHolder.LOG.warn("getHoldability unsupported", e2);
            }
            catch (SQLException e3) {
                if ("Method not supported".equals(e3.getMessage())) {
                    DruidConnectionHolder.holdabilityUnsupported = true;
                }
                DruidConnectionHolder.LOG.warn("getHoldability error", e3);
            }
        }
        this.underlyingReadOnly = conn.isReadOnly();
        try {
            this.underlyingTransactionIsolation = conn.getTransactionIsolation();
        }
        catch (SQLException e4) {
            if (!"HY000".equals(e4.getSQLState())) {
                if (!"com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException".equals(e4.getClass().getName())) {
                    throw e4;
                }
            }
        }
        this.defaultHoldability = this.underlyingHoldability;
        this.defaultTransactionIsolation = this.underlyingTransactionIsolation;
        this.defaultAutoCommit = this.underlyingAutoCommit;
        this.defaultReadOnly = this.underlyingReadOnly;
    }
    
    public long getConnectTimeMillis() {
        return this.connectTimeMillis;
    }
    
    public boolean isUnderlyingReadOnly() {
        return this.underlyingReadOnly;
    }
    
    public void setUnderlyingReadOnly(final boolean underlyingReadOnly) {
        this.underlyingReadOnly = underlyingReadOnly;
    }
    
    public int getUnderlyingHoldability() {
        return this.underlyingHoldability;
    }
    
    public void setUnderlyingHoldability(final int underlyingHoldability) {
        this.underlyingHoldability = underlyingHoldability;
    }
    
    public int getUnderlyingTransactionIsolation() {
        return this.underlyingTransactionIsolation;
    }
    
    public void setUnderlyingTransactionIsolation(final int underlyingTransactionIsolation) {
        this.underlyingTransactionIsolation = underlyingTransactionIsolation;
    }
    
    public boolean isUnderlyingAutoCommit() {
        return this.underlyingAutoCommit;
    }
    
    public void setUnderlyingAutoCommit(final boolean underlyingAutoCommit) {
        this.underlyingAutoCommit = underlyingAutoCommit;
    }
    
    public long getLastActiveTimeMillis() {
        return this.lastActiveTimeMillis;
    }
    
    public void setLastActiveTimeMillis(final long lastActiveMillis) {
        this.lastActiveTimeMillis = lastActiveMillis;
    }
    
    public long getLastExecTimeMillis() {
        return this.lastExecTimeMillis;
    }
    
    public void setLastExecTimeMillis(final long lastExecTimeMillis) {
        this.lastExecTimeMillis = lastExecTimeMillis;
    }
    
    public void addTrace(final DruidPooledStatement stmt) {
        this.lock.lock();
        try {
            this.statementTrace.add(stmt);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public void removeTrace(final DruidPooledStatement stmt) {
        this.lock.lock();
        try {
            this.statementTrace.remove(stmt);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public List<ConnectionEventListener> getConnectionEventListeners() {
        return this.connectionEventListeners;
    }
    
    public List<StatementEventListener> getStatementEventListeners() {
        return this.statementEventListeners;
    }
    
    public PreparedStatementPool getStatementPool() {
        if (this.statementPool == null) {
            this.statementPool = new PreparedStatementPool(this);
        }
        return this.statementPool;
    }
    
    public PreparedStatementPool getStatementPoolDirect() {
        return this.statementPool;
    }
    
    public void clearStatementCache() {
        if (this.statementPool == null) {
            return;
        }
        this.statementPool.clear();
    }
    
    public DruidAbstractDataSource getDataSource() {
        return this.dataSource;
    }
    
    public boolean isPoolPreparedStatements() {
        return this.dataSource.isPoolPreparedStatements();
    }
    
    public Connection getConnection() {
        return this.conn;
    }
    
    public long getTimeMillis() {
        return this.connectTimeMillis;
    }
    
    public long getUseCount() {
        return this.useCount;
    }
    
    public long getConnectionId() {
        return this.connectionId;
    }
    
    public void incrementUseCount() {
        ++this.useCount;
    }
    
    public long getKeepAliveCheckCount() {
        return this.keepAliveCheckCount;
    }
    
    public void incrementKeepAliveCheckCount() {
        ++this.keepAliveCheckCount;
    }
    
    public void reset() throws SQLException {
        if (this.underlyingReadOnly != this.defaultReadOnly) {
            this.conn.setReadOnly(this.defaultReadOnly);
            this.underlyingReadOnly = this.defaultReadOnly;
        }
        if (this.underlyingHoldability != this.defaultHoldability) {
            this.conn.setHoldability(this.defaultHoldability);
            this.underlyingHoldability = this.defaultHoldability;
        }
        if (this.underlyingTransactionIsolation != this.defaultTransactionIsolation) {
            this.conn.setTransactionIsolation(this.defaultTransactionIsolation);
            this.underlyingTransactionIsolation = this.defaultTransactionIsolation;
        }
        if (this.underlyingAutoCommit != this.defaultAutoCommit) {
            this.conn.setAutoCommit(this.defaultAutoCommit);
            this.underlyingAutoCommit = this.defaultAutoCommit;
        }
        this.connectionEventListeners.clear();
        this.statementEventListeners.clear();
        this.lock.lock();
        try {
            for (final Object item : this.statementTrace.toArray()) {
                final Statement stmt = (Statement)item;
                JdbcUtils.close(stmt);
            }
            this.statementTrace.clear();
        }
        finally {
            this.lock.unlock();
        }
        this.conn.clearWarnings();
    }
    
    public boolean isDiscard() {
        return this.discard;
    }
    
    public void setDiscard(final boolean discard) {
        this.discard = discard;
    }
    
    public long getCreateNanoSpan() {
        return this.createNanoSpan;
    }
    
    public long getLastNotEmptyWaitNanos() {
        return this.lastNotEmptyWaitNanos;
    }
    
    protected void setLastNotEmptyWaitNanos(final long lastNotEmptyWaitNanos) {
        this.lastNotEmptyWaitNanos = lastNotEmptyWaitNanos;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("{ID:");
        buf.append(System.identityHashCode(this.conn));
        buf.append(", ConnectTime:\"");
        buf.append(Utils.toString(new Date(this.connectTimeMillis)));
        buf.append("\", UseCount:");
        buf.append(this.useCount);
        if (this.lastActiveTimeMillis > 0L) {
            buf.append(", LastActiveTime:\"");
            buf.append(Utils.toString(new Date(this.lastActiveTimeMillis)));
            buf.append("\"");
        }
        if (this.lastKeepTimeMillis > 0L) {
            buf.append(", LastKeepTimeMillis:\"");
            buf.append(Utils.toString(new Date(this.lastKeepTimeMillis)));
            buf.append("\"");
        }
        if (this.statementPool != null && this.statementPool.getMap().size() > 0) {
            buf.append("\", CachedStatementCount:");
            buf.append(this.statementPool.getMap().size());
        }
        buf.append("}");
        return buf.toString();
    }
    
    static {
        LOG = LogFactory.getLog(DruidConnectionHolder.class);
        DruidConnectionHolder.holdabilityUnsupported = false;
    }
}
