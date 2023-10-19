// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server.session;

import java.sql.Blob;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;
import javax.naming.InitialContext;
import java.util.Collection;
import java.util.Set;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import org.eclipse.jetty.util.thread.Scheduler;
import javax.sql.DataSource;
import java.sql.Driver;
import org.eclipse.jetty.server.Server;
import java.util.HashSet;
import org.eclipse.jetty.util.log.Logger;

public class JDBCSessionIdManager extends AbstractSessionIdManager
{
    static final Logger LOG;
    public static final int MAX_INTERVAL_NOT_SET = -999;
    protected final HashSet<String> _sessionIds;
    protected Server _server;
    protected Driver _driver;
    protected String _driverClassName;
    protected String _connectionUrl;
    protected DataSource _datasource;
    protected String _jndiName;
    protected int _deleteBlockSize;
    protected Scheduler.Task _task;
    protected Scheduler _scheduler;
    protected Scavenger _scavenger;
    protected boolean _ownScheduler;
    protected long _lastScavengeTime;
    protected long _scavengeIntervalMs;
    protected String _createSessionIdTable;
    protected String _createSessionTable;
    protected String _selectBoundedExpiredSessions;
    private String _selectExpiredSessions;
    protected String _insertId;
    protected String _deleteId;
    protected String _queryId;
    protected String _insertSession;
    protected String _deleteSession;
    protected String _updateSession;
    protected String _updateSessionNode;
    protected String _updateSessionAccessTime;
    protected DatabaseAdaptor _dbAdaptor;
    protected SessionIdTableSchema _sessionIdTableSchema;
    protected SessionTableSchema _sessionTableSchema;
    
    public JDBCSessionIdManager(final Server server) {
        this._sessionIds = new HashSet<String>();
        this._deleteBlockSize = 10;
        this._scavengeIntervalMs = 600000L;
        this._dbAdaptor = new DatabaseAdaptor();
        this._sessionIdTableSchema = new SessionIdTableSchema();
        this._sessionTableSchema = new SessionTableSchema();
        this._server = server;
    }
    
    public JDBCSessionIdManager(final Server server, final Random random) {
        super(random);
        this._sessionIds = new HashSet<String>();
        this._deleteBlockSize = 10;
        this._scavengeIntervalMs = 600000L;
        this._dbAdaptor = new DatabaseAdaptor();
        this._sessionIdTableSchema = new SessionIdTableSchema();
        this._sessionTableSchema = new SessionTableSchema();
        this._server = server;
    }
    
    public void setDriverInfo(final String driverClassName, final String connectionUrl) {
        this._driverClassName = driverClassName;
        this._connectionUrl = connectionUrl;
    }
    
    public void setDriverInfo(final Driver driverClass, final String connectionUrl) {
        this._driver = driverClass;
        this._connectionUrl = connectionUrl;
    }
    
    public void setDatasource(final DataSource ds) {
        this._datasource = ds;
    }
    
    public DataSource getDataSource() {
        return this._datasource;
    }
    
    public String getDriverClassName() {
        return this._driverClassName;
    }
    
    public String getConnectionUrl() {
        return this._connectionUrl;
    }
    
    public void setDatasourceName(final String jndi) {
        this._jndiName = jndi;
    }
    
    public String getDatasourceName() {
        return this._jndiName;
    }
    
    @Deprecated
    public void setBlobType(final String name) {
        this._dbAdaptor.setBlobType(name);
    }
    
    public DatabaseAdaptor getDbAdaptor() {
        return this._dbAdaptor;
    }
    
    public void setDbAdaptor(final DatabaseAdaptor dbAdaptor) {
        if (dbAdaptor == null) {
            throw new IllegalStateException("DbAdaptor cannot be null");
        }
        this._dbAdaptor = dbAdaptor;
    }
    
    @Deprecated
    public String getBlobType() {
        return this._dbAdaptor.getBlobType();
    }
    
    @Deprecated
    public String getLongType() {
        return this._dbAdaptor.getLongType();
    }
    
    @Deprecated
    public void setLongType(final String longType) {
        this._dbAdaptor.setLongType(longType);
    }
    
    public SessionIdTableSchema getSessionIdTableSchema() {
        return this._sessionIdTableSchema;
    }
    
    public void setSessionIdTableSchema(final SessionIdTableSchema sessionIdTableSchema) {
        if (sessionIdTableSchema == null) {
            throw new IllegalArgumentException("Null SessionIdTableSchema");
        }
        this._sessionIdTableSchema = sessionIdTableSchema;
    }
    
    public SessionTableSchema getSessionTableSchema() {
        return this._sessionTableSchema;
    }
    
    public void setSessionTableSchema(final SessionTableSchema sessionTableSchema) {
        this._sessionTableSchema = sessionTableSchema;
    }
    
    public void setDeleteBlockSize(final int bsize) {
        this._deleteBlockSize = bsize;
    }
    
    public int getDeleteBlockSize() {
        return this._deleteBlockSize;
    }
    
    public void setScavengeInterval(long sec) {
        if (sec <= 0L) {
            sec = 60L;
        }
        final long old_period = this._scavengeIntervalMs;
        final long period = sec * 1000L;
        this._scavengeIntervalMs = period;
        final long tenPercent = this._scavengeIntervalMs / 10L;
        if (System.currentTimeMillis() % 2L == 0L) {
            this._scavengeIntervalMs += tenPercent;
        }
        if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
            JDBCSessionIdManager.LOG.debug("Scavenging every " + this._scavengeIntervalMs + " ms", new Object[0]);
        }
        synchronized (this) {
            if (this._scheduler != null && (period != old_period || this._task == null)) {
                if (this._task != null) {
                    this._task.cancel();
                }
                if (this._scavenger == null) {
                    this._scavenger = new Scavenger();
                }
                this._task = this._scheduler.schedule(this._scavenger, this._scavengeIntervalMs, TimeUnit.MILLISECONDS);
            }
        }
    }
    
    public long getScavengeInterval() {
        return this._scavengeIntervalMs / 1000L;
    }
    
    @Override
    public void addSession(final HttpSession session) {
        if (session == null) {
            return;
        }
        synchronized (this._sessionIds) {
            final String id = ((JDBCSessionManager.Session)session).getClusterId();
            try {
                this.insert(id);
                this._sessionIds.add(id);
            }
            catch (Exception e) {
                JDBCSessionIdManager.LOG.warn("Problem storing session id=" + id, e);
            }
        }
    }
    
    public void addSession(final String id) {
        if (id == null) {
            return;
        }
        synchronized (this._sessionIds) {
            try {
                this.insert(id);
                this._sessionIds.add(id);
            }
            catch (Exception e) {
                JDBCSessionIdManager.LOG.warn("Problem storing session id=" + id, e);
            }
        }
    }
    
    @Override
    public void removeSession(final HttpSession session) {
        if (session == null) {
            return;
        }
        this.removeSession(((JDBCSessionManager.Session)session).getClusterId());
    }
    
    public void removeSession(final String id) {
        if (id == null) {
            return;
        }
        synchronized (this._sessionIds) {
            if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
                JDBCSessionIdManager.LOG.debug("Removing sessionid=" + id, new Object[0]);
            }
            try {
                this._sessionIds.remove(id);
                this.delete(id);
            }
            catch (Exception e) {
                JDBCSessionIdManager.LOG.warn("Problem removing session id=" + id, e);
            }
        }
    }
    
    @Override
    public boolean idInUse(final String id) {
        if (id == null) {
            return false;
        }
        final String clusterId = this.getClusterId(id);
        boolean inUse = false;
        synchronized (this._sessionIds) {
            inUse = this._sessionIds.contains(clusterId);
        }
        if (inUse) {
            return true;
        }
        try {
            return this.exists(clusterId);
        }
        catch (Exception e) {
            JDBCSessionIdManager.LOG.warn("Problem checking inUse for id=" + clusterId, e);
            return false;
        }
    }
    
    @Override
    public void invalidateAll(final String id) {
        this.removeSession(id);
        synchronized (this._sessionIds) {
            final Handler[] contexts = this._server.getChildHandlersByClass(ContextHandler.class);
            for (int i = 0; contexts != null && i < contexts.length; ++i) {
                final SessionHandler sessionHandler = ((ContextHandler)contexts[i]).getChildHandlerByClass(SessionHandler.class);
                if (sessionHandler != null) {
                    final SessionManager manager = sessionHandler.getSessionManager();
                    if (manager != null && manager instanceof JDBCSessionManager) {
                        ((JDBCSessionManager)manager).invalidateSession(id);
                    }
                }
            }
        }
    }
    
    @Override
    public void renewSessionId(final String oldClusterId, final String oldNodeId, final HttpServletRequest request) {
        final String newClusterId = this.newSessionId(request.hashCode());
        synchronized (this._sessionIds) {
            this.removeSession(oldClusterId);
            this.addSession(newClusterId);
            final Handler[] contexts = this._server.getChildHandlersByClass(ContextHandler.class);
            for (int i = 0; contexts != null && i < contexts.length; ++i) {
                final SessionHandler sessionHandler = ((ContextHandler)contexts[i]).getChildHandlerByClass(SessionHandler.class);
                if (sessionHandler != null) {
                    final SessionManager manager = sessionHandler.getSessionManager();
                    if (manager != null && manager instanceof JDBCSessionManager) {
                        ((JDBCSessionManager)manager).renewSessionId(oldClusterId, oldNodeId, newClusterId, this.getNodeId(newClusterId, request));
                    }
                }
            }
        }
    }
    
    public void doStart() throws Exception {
        this.initializeDatabase();
        this.prepareTables();
        super.doStart();
        if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
            JDBCSessionIdManager.LOG.debug("Scavenging interval = " + this.getScavengeInterval() + " sec", new Object[0]);
        }
        this._scheduler = this._server.getBean(Scheduler.class);
        if (this._scheduler == null) {
            this._scheduler = new ScheduledExecutorScheduler();
            this._ownScheduler = true;
            this._scheduler.start();
        }
        else if (!this._scheduler.isStarted()) {
            throw new IllegalStateException("Shared scheduler not started");
        }
        this.setScavengeInterval(this.getScavengeInterval());
    }
    
    public void doStop() throws Exception {
        synchronized (this) {
            if (this._task != null) {
                this._task.cancel();
            }
            this._task = null;
            if (this._ownScheduler && this._scheduler != null) {
                this._scheduler.stop();
            }
            this._scheduler = null;
        }
        this._sessionIds.clear();
        super.doStop();
    }
    
    protected Connection getConnection() throws SQLException {
        if (this._datasource != null) {
            return this._datasource.getConnection();
        }
        return DriverManager.getConnection(this._connectionUrl);
    }
    
    private void prepareTables() throws SQLException {
        if (this._sessionIdTableSchema == null) {
            throw new IllegalStateException("No SessionIdTableSchema");
        }
        if (this._sessionTableSchema == null) {
            throw new IllegalStateException("No SessionTableSchema");
        }
        try (final Connection connection = this.getConnection();
             final Statement statement = connection.createStatement()) {
            connection.setAutoCommit(true);
            final DatabaseMetaData metaData = connection.getMetaData();
            this._dbAdaptor.adaptTo(metaData);
            this._sessionTableSchema.setDatabaseAdaptor(this._dbAdaptor);
            this._sessionIdTableSchema.setDatabaseAdaptor(this._dbAdaptor);
            this._createSessionIdTable = this._sessionIdTableSchema.getCreateStatementAsString();
            this._insertId = this._sessionIdTableSchema.getInsertStatementAsString();
            this._deleteId = this._sessionIdTableSchema.getDeleteStatementAsString();
            this._queryId = this._sessionIdTableSchema.getSelectStatementAsString();
            String tableName = this._dbAdaptor.convertIdentifier(this._sessionIdTableSchema.getTableName());
            try (final ResultSet result = metaData.getTables(null, null, tableName, null)) {
                if (!result.next()) {
                    statement.executeUpdate(this._createSessionIdTable);
                }
            }
            tableName = this._dbAdaptor.convertIdentifier(this._sessionTableSchema.getTableName());
            try (final ResultSet result = metaData.getTables(null, null, tableName, null)) {
                if (!result.next()) {
                    statement.executeUpdate(this._createSessionTable = this._sessionTableSchema.getCreateStatementAsString());
                }
                else {
                    ResultSet colResult = null;
                    try {
                        colResult = metaData.getColumns(null, null, this._dbAdaptor.convertIdentifier(this._sessionTableSchema.getTableName()), this._dbAdaptor.convertIdentifier(this._sessionTableSchema.getMaxIntervalColumn()));
                    }
                    catch (SQLException s) {
                        JDBCSessionIdManager.LOG.warn("Problem checking if " + this._sessionTableSchema.getTableName() + " table contains " + this._sessionTableSchema.getMaxIntervalColumn() + " column. Ensure table contains column definition: \"" + this._sessionTableSchema.getMaxIntervalColumn() + " long not null default -999\"", new Object[0]);
                        throw s;
                    }
                    try {
                        if (!colResult.next()) {
                            try {
                                statement.executeUpdate(this._sessionTableSchema.getAlterTableForMaxIntervalAsString());
                            }
                            catch (SQLException s) {
                                JDBCSessionIdManager.LOG.warn("Problem adding " + this._sessionTableSchema.getMaxIntervalColumn() + " column. Ensure table contains column definition: \"" + this._sessionTableSchema.getMaxIntervalColumn() + " long not null default -999\"", new Object[0]);
                                throw s;
                            }
                        }
                    }
                    finally {
                        colResult.close();
                    }
                }
            }
            final String index1 = "idx_" + this._sessionTableSchema.getTableName() + "_expiry";
            final String index2 = "idx_" + this._sessionTableSchema.getTableName() + "_session";
            boolean index1Exists = false;
            boolean index2Exists = false;
            try (final ResultSet result2 = metaData.getIndexInfo(null, null, tableName, false, false)) {
                while (result2.next()) {
                    final String idxName = result2.getString("INDEX_NAME");
                    if (index1.equalsIgnoreCase(idxName)) {
                        index1Exists = true;
                    }
                    else {
                        if (!index2.equalsIgnoreCase(idxName)) {
                            continue;
                        }
                        index2Exists = true;
                    }
                }
            }
            if (!index1Exists) {
                statement.executeUpdate(this._sessionTableSchema.getCreateIndexOverExpiryStatementAsString(index1));
            }
            if (!index2Exists) {
                statement.executeUpdate(this._sessionTableSchema.getCreateIndexOverSessionStatementAsString(index2));
            }
            this._insertSession = this._sessionTableSchema.getInsertSessionStatementAsString();
            this._deleteSession = this._sessionTableSchema.getDeleteSessionStatementAsString();
            this._updateSession = this._sessionTableSchema.getUpdateSessionStatementAsString();
            this._updateSessionNode = this._sessionTableSchema.getUpdateSessionNodeStatementAsString();
            this._updateSessionAccessTime = this._sessionTableSchema.getUpdateSessionAccessTimeStatementAsString();
            this._selectBoundedExpiredSessions = this._sessionTableSchema.getBoundedExpiredSessionsStatementAsString();
            this._selectExpiredSessions = this._sessionTableSchema.getSelectExpiredSessionsStatementAsString();
        }
    }
    
    private void insert(final String id) throws SQLException {
        try (final Connection connection = this.getConnection();
             final PreparedStatement query = connection.prepareStatement(this._queryId)) {
            connection.setAutoCommit(true);
            query.setString(1, id);
            try (final ResultSet result = query.executeQuery()) {
                if (!result.next()) {
                    try (final PreparedStatement statement = connection.prepareStatement(this._insertId)) {
                        statement.setString(1, id);
                        statement.executeUpdate();
                    }
                }
            }
        }
    }
    
    private void delete(final String id) throws SQLException {
        try (final Connection connection = this.getConnection();
             final PreparedStatement statement = connection.prepareStatement(this._deleteId)) {
            connection.setAutoCommit(true);
            statement.setString(1, id);
            statement.executeUpdate();
        }
    }
    
    private boolean exists(final String id) throws SQLException {
        try (final Connection connection = this.getConnection();
             final PreparedStatement statement = connection.prepareStatement(this._queryId)) {
            connection.setAutoCommit(true);
            statement.setString(1, id);
            try (final ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }
    
    private void scavenge() {
        Connection connection = null;
        try {
            if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
                JDBCSessionIdManager.LOG.debug(this.getWorkerName() + "- Scavenge sweep started at " + System.currentTimeMillis(), new Object[0]);
            }
            if (this._lastScavengeTime > 0L) {
                connection = this.getConnection();
                connection.setAutoCommit(true);
                final Set<String> expiredSessionIds = new HashSet<String>();
                final long lowerBound = this._lastScavengeTime - this._scavengeIntervalMs;
                long upperBound = this._lastScavengeTime;
                if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
                    JDBCSessionIdManager.LOG.debug(this.getWorkerName() + "- Pass 1: Searching for sessions expired between " + lowerBound + " and " + upperBound, new Object[0]);
                }
                try (final PreparedStatement statement = connection.prepareStatement(this._selectBoundedExpiredSessions)) {
                    statement.setString(1, this.getWorkerName());
                    statement.setLong(2, lowerBound);
                    statement.setLong(3, upperBound);
                    try (final ResultSet result = statement.executeQuery()) {
                        while (result.next()) {
                            final String sessionId = result.getString(this._sessionTableSchema.getIdColumn());
                            expiredSessionIds.add(sessionId);
                            if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
                                JDBCSessionIdManager.LOG.debug("Found expired sessionId=" + sessionId, new Object[0]);
                            }
                        }
                    }
                }
                this.scavengeSessions(expiredSessionIds, false);
                try (final PreparedStatement selectExpiredSessions = connection.prepareStatement(this._selectExpiredSessions)) {
                    expiredSessionIds.clear();
                    upperBound = this._lastScavengeTime - 2L * this._scavengeIntervalMs;
                    if (upperBound > 0L) {
                        if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
                            JDBCSessionIdManager.LOG.debug(this.getWorkerName() + "- Pass 2: Searching for sessions expired before " + upperBound, new Object[0]);
                        }
                        selectExpiredSessions.setLong(1, upperBound);
                        try (final ResultSet result = selectExpiredSessions.executeQuery()) {
                            while (result.next()) {
                                final String sessionId = result.getString(this._sessionTableSchema.getIdColumn());
                                final String lastNode = result.getString(this._sessionTableSchema.getLastNodeColumn());
                                if ((this.getWorkerName() == null && lastNode == null) || (this.getWorkerName() != null && this.getWorkerName().equals(lastNode))) {
                                    expiredSessionIds.add(sessionId);
                                }
                                if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
                                    JDBCSessionIdManager.LOG.debug("Found expired sessionId=" + sessionId + " last managed by " + this.getWorkerName(), new Object[0]);
                                }
                            }
                        }
                        this.scavengeSessions(expiredSessionIds, false);
                    }
                    upperBound = this._lastScavengeTime - 3L * this._scavengeIntervalMs;
                    expiredSessionIds.clear();
                    if (upperBound > 0L) {
                        if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
                            JDBCSessionIdManager.LOG.debug(this.getWorkerName() + "- Pass 3: searching for sessions expired before " + upperBound, new Object[0]);
                        }
                        selectExpiredSessions.setLong(1, upperBound);
                        try (final ResultSet result = selectExpiredSessions.executeQuery()) {
                            while (result.next()) {
                                final String sessionId = result.getString(this._sessionTableSchema.getIdColumn());
                                expiredSessionIds.add(sessionId);
                                if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
                                    JDBCSessionIdManager.LOG.debug("Found expired sessionId=" + sessionId, new Object[0]);
                                }
                            }
                        }
                        this.scavengeSessions(expiredSessionIds, true);
                    }
                }
            }
        }
        catch (Exception e) {
            if (this.isRunning()) {
                JDBCSessionIdManager.LOG.warn("Problem selecting expired sessions", e);
            }
            else {
                JDBCSessionIdManager.LOG.ignore(e);
            }
            this._lastScavengeTime = System.currentTimeMillis();
            if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
                JDBCSessionIdManager.LOG.debug(this.getWorkerName() + "- Scavenge sweep ended at " + this._lastScavengeTime, new Object[0]);
            }
            if (connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e2) {
                    JDBCSessionIdManager.LOG.warn(e2);
                }
            }
        }
        finally {
            this._lastScavengeTime = System.currentTimeMillis();
            if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
                JDBCSessionIdManager.LOG.debug(this.getWorkerName() + "- Scavenge sweep ended at " + this._lastScavengeTime, new Object[0]);
            }
            if (connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e3) {
                    JDBCSessionIdManager.LOG.warn(e3);
                }
            }
        }
    }
    
    private void scavengeSessions(final Set<String> expiredSessionIds, final boolean forceDelete) {
        final Set<String> remainingIds = new HashSet<String>(expiredSessionIds);
        final Handler[] contexts = this._server.getChildHandlersByClass(ContextHandler.class);
        for (int i = 0; contexts != null && i < contexts.length; ++i) {
            final SessionHandler sessionHandler = ((ContextHandler)contexts[i]).getChildHandlerByClass(SessionHandler.class);
            if (sessionHandler != null) {
                final SessionManager manager = sessionHandler.getSessionManager();
                if (manager != null && manager instanceof JDBCSessionManager) {
                    final Set<String> successfullyExpiredIds = ((JDBCSessionManager)manager).expire(expiredSessionIds);
                    if (successfullyExpiredIds != null) {
                        remainingIds.removeAll(successfullyExpiredIds);
                    }
                }
            }
        }
        if (!remainingIds.isEmpty() && forceDelete) {
            JDBCSessionIdManager.LOG.info("Forcibly deleting unrecoverable expired sessions {}", remainingIds);
            try {
                synchronized (this._sessionIds) {
                    this._sessionIds.removeAll(remainingIds);
                }
                this.cleanExpiredSessionIds(remainingIds);
            }
            catch (Exception e) {
                JDBCSessionIdManager.LOG.warn("Error removing expired session ids", e);
            }
        }
    }
    
    private void cleanExpiredSessionIds(final Set<String> expiredIds) throws Exception {
        if (expiredIds == null || expiredIds.isEmpty()) {
            return;
        }
        final String[] ids = expiredIds.toArray(new String[expiredIds.size()]);
        try (final Connection con = this.getConnection()) {
            con.setTransactionIsolation(2);
            con.setAutoCommit(false);
            int start = 0;
            int end = 0;
            final int blocksize = this._deleteBlockSize;
            int block = 0;
            try (final Statement statement = con.createStatement()) {
                while (end < ids.length) {
                    start = block * blocksize;
                    if (ids.length - start >= blocksize) {
                        end = start + blocksize;
                    }
                    else {
                        end = ids.length;
                    }
                    statement.executeUpdate(this.fillInClause("delete from " + this._sessionIdTableSchema.getTableName() + " where " + this._sessionIdTableSchema.getIdColumn() + " in ", ids, start, end));
                    statement.executeUpdate(this.fillInClause("delete from " + this._sessionTableSchema.getTableName() + " where " + this._sessionTableSchema.getIdColumn() + " in ", ids, start, end));
                    ++block;
                }
            }
            catch (Exception e) {
                con.rollback();
                throw e;
            }
            con.commit();
        }
    }
    
    private String fillInClause(final String sql, final String[] literals, final int start, final int end) throws Exception {
        final StringBuffer buff = new StringBuffer();
        buff.append(sql);
        buff.append("(");
        for (int i = start; i < end; ++i) {
            buff.append("'" + literals[i] + "'");
            if (i + 1 < end) {
                buff.append(",");
            }
        }
        buff.append(")");
        return buff.toString();
    }
    
    private void initializeDatabase() throws Exception {
        if (this._datasource != null) {
            return;
        }
        if (this._jndiName != null) {
            final InitialContext ic = new InitialContext();
            this._datasource = (DataSource)ic.lookup(this._jndiName);
        }
        else if (this._driver != null && this._connectionUrl != null) {
            DriverManager.registerDriver(this._driver);
        }
        else {
            if (this._driverClassName == null || this._connectionUrl == null) {
                throw new IllegalStateException("No database configured for sessions");
            }
            Class.forName(this._driverClassName);
        }
    }
    
    static {
        LOG = SessionHandler.LOG;
    }
    
    public static class SessionTableSchema
    {
        protected DatabaseAdaptor _dbAdaptor;
        protected String _tableName;
        protected String _rowIdColumn;
        protected String _idColumn;
        protected String _contextPathColumn;
        protected String _virtualHostColumn;
        protected String _lastNodeColumn;
        protected String _accessTimeColumn;
        protected String _lastAccessTimeColumn;
        protected String _createTimeColumn;
        protected String _cookieTimeColumn;
        protected String _lastSavedTimeColumn;
        protected String _expiryTimeColumn;
        protected String _maxIntervalColumn;
        protected String _mapColumn;
        
        public SessionTableSchema() {
            this._tableName = "JettySessions";
            this._rowIdColumn = "rowId";
            this._idColumn = "sessionId";
            this._contextPathColumn = "contextPath";
            this._virtualHostColumn = "virtualHost";
            this._lastNodeColumn = "lastNode";
            this._accessTimeColumn = "accessTime";
            this._lastAccessTimeColumn = "lastAccessTime";
            this._createTimeColumn = "createTime";
            this._cookieTimeColumn = "cookieTime";
            this._lastSavedTimeColumn = "lastSavedTime";
            this._expiryTimeColumn = "expiryTime";
            this._maxIntervalColumn = "maxInterval";
            this._mapColumn = "map";
        }
        
        protected void setDatabaseAdaptor(final DatabaseAdaptor dbadaptor) {
            this._dbAdaptor = dbadaptor;
        }
        
        public String getTableName() {
            return this._tableName;
        }
        
        public void setTableName(final String tableName) {
            this.checkNotNull(tableName);
            this._tableName = tableName;
        }
        
        public String getRowIdColumn() {
            if ("rowId".equals(this._rowIdColumn) && this._dbAdaptor.isRowIdReserved()) {
                this._rowIdColumn = "srowId";
            }
            return this._rowIdColumn;
        }
        
        public void setRowIdColumn(final String rowIdColumn) {
            this.checkNotNull(rowIdColumn);
            if (this._dbAdaptor == null) {
                throw new IllegalStateException("DbAdaptor is null");
            }
            if (this._dbAdaptor.isRowIdReserved() && "rowId".equals(rowIdColumn)) {
                throw new IllegalArgumentException("rowId is reserved word for Oracle");
            }
            this._rowIdColumn = rowIdColumn;
        }
        
        public String getIdColumn() {
            return this._idColumn;
        }
        
        public void setIdColumn(final String idColumn) {
            this.checkNotNull(idColumn);
            this._idColumn = idColumn;
        }
        
        public String getContextPathColumn() {
            return this._contextPathColumn;
        }
        
        public void setContextPathColumn(final String contextPathColumn) {
            this.checkNotNull(contextPathColumn);
            this._contextPathColumn = contextPathColumn;
        }
        
        public String getVirtualHostColumn() {
            return this._virtualHostColumn;
        }
        
        public void setVirtualHostColumn(final String virtualHostColumn) {
            this.checkNotNull(virtualHostColumn);
            this._virtualHostColumn = virtualHostColumn;
        }
        
        public String getLastNodeColumn() {
            return this._lastNodeColumn;
        }
        
        public void setLastNodeColumn(final String lastNodeColumn) {
            this.checkNotNull(lastNodeColumn);
            this._lastNodeColumn = lastNodeColumn;
        }
        
        public String getAccessTimeColumn() {
            return this._accessTimeColumn;
        }
        
        public void setAccessTimeColumn(final String accessTimeColumn) {
            this.checkNotNull(accessTimeColumn);
            this._accessTimeColumn = accessTimeColumn;
        }
        
        public String getLastAccessTimeColumn() {
            return this._lastAccessTimeColumn;
        }
        
        public void setLastAccessTimeColumn(final String lastAccessTimeColumn) {
            this.checkNotNull(lastAccessTimeColumn);
            this._lastAccessTimeColumn = lastAccessTimeColumn;
        }
        
        public String getCreateTimeColumn() {
            return this._createTimeColumn;
        }
        
        public void setCreateTimeColumn(final String createTimeColumn) {
            this.checkNotNull(createTimeColumn);
            this._createTimeColumn = createTimeColumn;
        }
        
        public String getCookieTimeColumn() {
            return this._cookieTimeColumn;
        }
        
        public void setCookieTimeColumn(final String cookieTimeColumn) {
            this.checkNotNull(cookieTimeColumn);
            this._cookieTimeColumn = cookieTimeColumn;
        }
        
        public String getLastSavedTimeColumn() {
            return this._lastSavedTimeColumn;
        }
        
        public void setLastSavedTimeColumn(final String lastSavedTimeColumn) {
            this.checkNotNull(lastSavedTimeColumn);
            this._lastSavedTimeColumn = lastSavedTimeColumn;
        }
        
        public String getExpiryTimeColumn() {
            return this._expiryTimeColumn;
        }
        
        public void setExpiryTimeColumn(final String expiryTimeColumn) {
            this.checkNotNull(expiryTimeColumn);
            this._expiryTimeColumn = expiryTimeColumn;
        }
        
        public String getMaxIntervalColumn() {
            return this._maxIntervalColumn;
        }
        
        public void setMaxIntervalColumn(final String maxIntervalColumn) {
            this.checkNotNull(maxIntervalColumn);
            this._maxIntervalColumn = maxIntervalColumn;
        }
        
        public String getMapColumn() {
            return this._mapColumn;
        }
        
        public void setMapColumn(final String mapColumn) {
            this.checkNotNull(mapColumn);
            this._mapColumn = mapColumn;
        }
        
        public String getCreateStatementAsString() {
            if (this._dbAdaptor == null) {
                throw new IllegalStateException("No DBAdaptor");
            }
            final String blobType = this._dbAdaptor.getBlobType();
            final String longType = this._dbAdaptor.getLongType();
            return "create table " + this._tableName + " (" + this.getRowIdColumn() + " varchar(120), " + this._idColumn + " varchar(120), " + this._contextPathColumn + " varchar(60), " + this._virtualHostColumn + " varchar(60), " + this._lastNodeColumn + " varchar(60), " + this._accessTimeColumn + " " + longType + ", " + this._lastAccessTimeColumn + " " + longType + ", " + this._createTimeColumn + " " + longType + ", " + this._cookieTimeColumn + " " + longType + ", " + this._lastSavedTimeColumn + " " + longType + ", " + this._expiryTimeColumn + " " + longType + ", " + this._maxIntervalColumn + " " + longType + ", " + this._mapColumn + " " + blobType + ", primary key(" + this.getRowIdColumn() + "))";
        }
        
        public String getCreateIndexOverExpiryStatementAsString(final String indexName) {
            return "create index " + indexName + " on " + this.getTableName() + " (" + this.getExpiryTimeColumn() + ")";
        }
        
        public String getCreateIndexOverSessionStatementAsString(final String indexName) {
            return "create index " + indexName + " on " + this.getTableName() + " (" + this.getIdColumn() + ", " + this.getContextPathColumn() + ")";
        }
        
        public String getAlterTableForMaxIntervalAsString() {
            if (this._dbAdaptor == null) {
                throw new IllegalStateException("No DBAdaptor");
            }
            final String longType = this._dbAdaptor.getLongType();
            final String stem = "alter table " + this.getTableName() + " add " + this.getMaxIntervalColumn() + " " + longType;
            if (this._dbAdaptor.getDBName().contains("oracle")) {
                return stem + " default " + -999 + " not null";
            }
            return stem + " not null default " + -999;
        }
        
        private void checkNotNull(final String s) {
            if (s == null) {
                throw new IllegalArgumentException(s);
            }
        }
        
        public String getInsertSessionStatementAsString() {
            return "insert into " + this.getTableName() + " (" + this.getRowIdColumn() + ", " + this.getIdColumn() + ", " + this.getContextPathColumn() + ", " + this.getVirtualHostColumn() + ", " + this.getLastNodeColumn() + ", " + this.getAccessTimeColumn() + ", " + this.getLastAccessTimeColumn() + ", " + this.getCreateTimeColumn() + ", " + this.getCookieTimeColumn() + ", " + this.getLastSavedTimeColumn() + ", " + this.getExpiryTimeColumn() + ", " + this.getMaxIntervalColumn() + ", " + this.getMapColumn() + ") " + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }
        
        public String getDeleteSessionStatementAsString() {
            return "delete from " + this.getTableName() + " where " + this.getRowIdColumn() + " = ?";
        }
        
        public String getUpdateSessionStatementAsString() {
            return "update " + this.getTableName() + " set " + this.getIdColumn() + " = ?, " + this.getLastNodeColumn() + " = ?, " + this.getAccessTimeColumn() + " = ?, " + this.getLastAccessTimeColumn() + " = ?, " + this.getLastSavedTimeColumn() + " = ?, " + this.getExpiryTimeColumn() + " = ?, " + this.getMaxIntervalColumn() + " = ?, " + this.getMapColumn() + " = ? where " + this.getRowIdColumn() + " = ?";
        }
        
        public String getUpdateSessionNodeStatementAsString() {
            return "update " + this.getTableName() + " set " + this.getLastNodeColumn() + " = ? where " + this.getRowIdColumn() + " = ?";
        }
        
        public String getUpdateSessionAccessTimeStatementAsString() {
            return "update " + this.getTableName() + " set " + this.getLastNodeColumn() + " = ?, " + this.getAccessTimeColumn() + " = ?, " + this.getLastAccessTimeColumn() + " = ?, " + this.getLastSavedTimeColumn() + " = ?, " + this.getExpiryTimeColumn() + " = ?, " + this.getMaxIntervalColumn() + " = ? where " + this.getRowIdColumn() + " = ?";
        }
        
        public String getBoundedExpiredSessionsStatementAsString() {
            return "select * from " + this.getTableName() + " where " + this.getLastNodeColumn() + " = ? and " + this.getExpiryTimeColumn() + " >= ? and " + this.getExpiryTimeColumn() + " <= ?";
        }
        
        public String getSelectExpiredSessionsStatementAsString() {
            return "select * from " + this.getTableName() + " where " + this.getExpiryTimeColumn() + " >0 and " + this.getExpiryTimeColumn() + " <= ?";
        }
        
        public PreparedStatement getLoadStatement(final Connection connection, final String rowId, final String contextPath, final String virtualHosts) throws SQLException {
            if (this._dbAdaptor == null) {
                throw new IllegalStateException("No DB adaptor");
            }
            if ((contextPath == null || "".equals(contextPath)) && this._dbAdaptor.isEmptyStringNull()) {
                final PreparedStatement statement = connection.prepareStatement("select * from " + this.getTableName() + " where " + this.getIdColumn() + " = ? and " + this.getContextPathColumn() + " is null and " + this.getVirtualHostColumn() + " = ?");
                statement.setString(1, rowId);
                statement.setString(2, virtualHosts);
                return statement;
            }
            final PreparedStatement statement = connection.prepareStatement("select * from " + this.getTableName() + " where " + this.getIdColumn() + " = ? and " + this.getContextPathColumn() + " = ? and " + this.getVirtualHostColumn() + " = ?");
            statement.setString(1, rowId);
            statement.setString(2, contextPath);
            statement.setString(3, virtualHosts);
            return statement;
        }
    }
    
    public static class SessionIdTableSchema
    {
        protected DatabaseAdaptor _dbAdaptor;
        protected String _tableName;
        protected String _idColumn;
        
        public SessionIdTableSchema() {
            this._tableName = "JettySessionIds";
            this._idColumn = "id";
        }
        
        public void setDatabaseAdaptor(final DatabaseAdaptor dbAdaptor) {
            this._dbAdaptor = dbAdaptor;
        }
        
        public String getIdColumn() {
            return this._idColumn;
        }
        
        public void setIdColumn(final String idColumn) {
            this.checkNotNull(idColumn);
            this._idColumn = idColumn;
        }
        
        public String getTableName() {
            return this._tableName;
        }
        
        public void setTableName(final String tableName) {
            this.checkNotNull(tableName);
            this._tableName = tableName;
        }
        
        public String getInsertStatementAsString() {
            return "insert into " + this._tableName + " (" + this._idColumn + ")  values (?)";
        }
        
        public String getDeleteStatementAsString() {
            return "delete from " + this._tableName + " where " + this._idColumn + " = ?";
        }
        
        public String getSelectStatementAsString() {
            return "select * from " + this._tableName + " where " + this._idColumn + " = ?";
        }
        
        public String getCreateStatementAsString() {
            return "create table " + this._tableName + " (" + this._idColumn + " varchar(120), primary key(" + this._idColumn + "))";
        }
        
        private void checkNotNull(final String s) {
            if (s == null) {
                throw new IllegalArgumentException(s);
            }
        }
    }
    
    public static class DatabaseAdaptor
    {
        String _dbName;
        boolean _isLower;
        boolean _isUpper;
        protected String _blobType;
        protected String _longType;
        
        public void adaptTo(final DatabaseMetaData dbMeta) throws SQLException {
            this._dbName = dbMeta.getDatabaseProductName().toLowerCase(Locale.ENGLISH);
            if (JDBCSessionIdManager.LOG.isDebugEnabled()) {
                JDBCSessionIdManager.LOG.debug("Using database {}", this._dbName);
            }
            this._isLower = dbMeta.storesLowerCaseIdentifiers();
            this._isUpper = dbMeta.storesUpperCaseIdentifiers();
        }
        
        public void setBlobType(final String blobType) {
            this._blobType = blobType;
        }
        
        public String getBlobType() {
            if (this._blobType != null) {
                return this._blobType;
            }
            if (this._dbName.startsWith("postgres")) {
                return "bytea";
            }
            return "blob";
        }
        
        public void setLongType(final String longType) {
            this._longType = longType;
        }
        
        public String getLongType() {
            if (this._longType != null) {
                return this._longType;
            }
            if (this._dbName == null) {
                throw new IllegalStateException("DbAdaptor missing metadata");
            }
            if (this._dbName.startsWith("oracle")) {
                return "number(20)";
            }
            return "bigint";
        }
        
        public String convertIdentifier(final String identifier) {
            if (this._dbName == null) {
                throw new IllegalStateException("DbAdaptor missing metadata");
            }
            if (this._isLower) {
                return identifier.toLowerCase(Locale.ENGLISH);
            }
            if (this._isUpper) {
                return identifier.toUpperCase(Locale.ENGLISH);
            }
            return identifier;
        }
        
        public String getDBName() {
            return this._dbName;
        }
        
        public InputStream getBlobInputStream(final ResultSet result, final String columnName) throws SQLException {
            if (this._dbName == null) {
                throw new IllegalStateException("DbAdaptor missing metadata");
            }
            if (this._dbName.startsWith("postgres")) {
                final byte[] bytes = result.getBytes(columnName);
                return new ByteArrayInputStream(bytes);
            }
            final Blob blob = result.getBlob(columnName);
            return blob.getBinaryStream();
        }
        
        public boolean isEmptyStringNull() {
            if (this._dbName == null) {
                throw new IllegalStateException("DbAdaptor missing metadata");
            }
            return this._dbName.startsWith("oracle");
        }
        
        public boolean isRowIdReserved() {
            if (this._dbName == null) {
                throw new IllegalStateException("DbAdaptor missing metadata");
            }
            return this._dbName != null && this._dbName.startsWith("oracle");
        }
    }
    
    protected class Scavenger implements Runnable
    {
        @Override
        public void run() {
            try {
                JDBCSessionIdManager.this.scavenge();
            }
            finally {
                if (JDBCSessionIdManager.this._scheduler != null && JDBCSessionIdManager.this._scheduler.isRunning()) {
                    JDBCSessionIdManager.this._task = JDBCSessionIdManager.this._scheduler.schedule(this, JDBCSessionIdManager.this._scavengeIntervalMs, TimeUnit.MILLISECONDS);
                }
            }
        }
    }
}
