// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.stat;

import com.alibaba.druid.support.logging.LogFactory;
import java.io.Reader;
import java.util.Map;
import com.alibaba.druid.proxy.jdbc.ClobProxy;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import java.util.Iterator;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.io.InputStream;
import com.alibaba.druid.support.json.JSONWriter;
import com.alibaba.druid.proxy.jdbc.StatementExecuteType;
import com.alibaba.druid.support.profile.Profiler;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.stat.JdbcStatContext;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import com.alibaba.druid.stat.JdbcSqlStat;
import com.alibaba.druid.proxy.jdbc.CallableStatementProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import java.sql.Savepoint;
import com.alibaba.druid.stat.JdbcConnectionStat;
import com.alibaba.druid.stat.JdbcDataSourceStat;
import java.util.Date;
import java.sql.SQLException;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import com.alibaba.druid.filter.FilterChain;
import java.util.Properties;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.alibaba.druid.VERSION;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import java.util.List;
import com.alibaba.druid.sql.parser.SQLSelectListCache;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import com.alibaba.druid.stat.JdbcStatManager;
import java.util.concurrent.locks.ReentrantLock;
import com.alibaba.druid.DbType;
import com.alibaba.druid.stat.JdbcResultSetStat;
import com.alibaba.druid.stat.JdbcStatementStat;
import java.util.concurrent.locks.Lock;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.filter.FilterEventAdapter;

public class StatFilter extends FilterEventAdapter implements StatFilterMBean
{
    private static final Log LOG;
    private static final String SYS_PROP_LOG_SLOW_SQL = "druid.stat.logSlowSql";
    private static final String SYS_PROP_SLOW_SQL_MILLIS = "druid.stat.slowSqlMillis";
    private static final String SYS_PROP_SLOW_SQL_LOG_LEVEL = "druid.stat.slowSqlLogLevel";
    private static final String SYS_PROP_MERGE_SQL = "druid.stat.mergeSql";
    public static final String ATTR_NAME_CONNECTION_STAT = "stat.conn";
    public static final String ATTR_TRANSACTION = "stat.tx";
    private final Lock lock;
    @Deprecated
    protected final JdbcStatementStat statementStat;
    @Deprecated
    protected final JdbcResultSetStat resultSetStat;
    private boolean connectionStackTraceEnable;
    protected long slowSqlMillis;
    protected boolean logSlowSql;
    protected String slowSqlLogLevel;
    private DbType dbType;
    private boolean mergeSql;
    
    public StatFilter() {
        this.lock = new ReentrantLock();
        this.statementStat = JdbcStatManager.getInstance().getStatementStat();
        this.resultSetStat = JdbcStatManager.getInstance().getResultSetStat();
        this.connectionStackTraceEnable = false;
        this.slowSqlMillis = 3000L;
        this.logSlowSql = false;
        this.slowSqlLogLevel = "ERROR";
        this.mergeSql = false;
    }
    
    public DbType getDbType() {
        return this.dbType;
    }
    
    public void setDbType(final DbType dbType) {
        this.dbType = dbType;
    }
    
    public void setDbType(final String dbType) {
        this.dbType = DbType.of(dbType);
    }
    
    @Override
    public long getSlowSqlMillis() {
        return this.slowSqlMillis;
    }
    
    @Override
    public void setSlowSqlMillis(final long slowSqlMillis) {
        this.slowSqlMillis = slowSqlMillis;
    }
    
    @Override
    public boolean isLogSlowSql() {
        return this.logSlowSql;
    }
    
    @Override
    public void setLogSlowSql(final boolean logSlowSql) {
        this.logSlowSql = logSlowSql;
    }
    
    public boolean isConnectionStackTraceEnable() {
        return this.connectionStackTraceEnable;
    }
    
    public void setConnectionStackTraceEnable(final boolean connectionStackTraceEnable) {
        this.connectionStackTraceEnable = connectionStackTraceEnable;
    }
    
    @Override
    public boolean isMergeSql() {
        return this.mergeSql;
    }
    
    @Override
    public void setMergeSql(final boolean mergeSql) {
        this.mergeSql = mergeSql;
    }
    
    public String getSlowSqlLogLevel() {
        return this.slowSqlLogLevel;
    }
    
    public void setSlowSqlLogLevel(final String slowSqlLogLevel) {
        this.slowSqlLogLevel = slowSqlLogLevel;
    }
    
    @Deprecated
    public String mergeSql(final String sql) {
        return this.mergeSql(sql, this.dbType);
    }
    
    @Override
    public String mergeSql(final String sql, final String dbType) {
        return this.mergeSql(sql, DbType.of(dbType));
    }
    
    public String mergeSql(String sql, final DbType dbType) {
        if (!this.mergeSql) {
            return sql;
        }
        try {
            sql = ParameterizedOutputVisitorUtils.parameterize(sql, dbType, null, (List<Object>)null, (VisitorFeature[])null);
        }
        catch (Exception e) {
            StatFilter.LOG.error("merge sql error, dbType " + dbType + ", druid-" + VERSION.getVersionNumber() + ", sql : " + sql, e);
        }
        return sql;
    }
    
    @Override
    public void init(final DataSourceProxy dataSource) {
        this.lock.lock();
        try {
            if (this.dbType == null) {
                this.dbType = DbType.of(dataSource.getDbType());
            }
            this.configFromProperties(dataSource.getConnectProperties());
            this.configFromProperties(System.getProperties());
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
        if (properties == null) {
            return;
        }
        String property = properties.getProperty("druid.stat.mergeSql");
        if ("true".equals(property)) {
            this.mergeSql = true;
        }
        else if ("false".equals(property)) {
            this.mergeSql = false;
        }
        property = properties.getProperty("druid.stat.slowSqlMillis");
        if (property != null && property.trim().length() > 0) {
            property = property.trim();
            try {
                this.slowSqlMillis = Long.parseLong(property);
            }
            catch (Exception e) {
                StatFilter.LOG.error("property 'druid.stat.slowSqlMillis' format error");
            }
        }
        property = properties.getProperty("druid.stat.logSlowSql");
        if ("true".equals(property)) {
            this.logSlowSql = true;
        }
        else if ("false".equals(property)) {
            this.logSlowSql = false;
        }
        property = properties.getProperty("druid.stat.slowSqlLogLevel");
        if ("error".equalsIgnoreCase(property)) {
            this.slowSqlLogLevel = "ERROR";
        }
        else if ("warn".equalsIgnoreCase(property)) {
            this.slowSqlLogLevel = "WARN";
        }
        else if ("info".equalsIgnoreCase(property)) {
            this.slowSqlLogLevel = "INFO";
        }
        else if ("debug".equalsIgnoreCase(property)) {
            this.slowSqlLogLevel = "DEBUG";
        }
    }
    
    @Override
    public ConnectionProxy connection_connect(final FilterChain chain, final Properties info) throws SQLException {
        ConnectionProxy connection = null;
        final long startNano = System.nanoTime();
        final long startTime = System.currentTimeMillis();
        final long nowTime = System.currentTimeMillis();
        final JdbcDataSourceStat dataSourceStat = chain.getDataSource().getDataSourceStat();
        dataSourceStat.getConnectionStat().beforeConnect();
        long nanoSpan;
        try {
            connection = chain.connection_connect(info);
            nanoSpan = System.nanoTime() - startNano;
        }
        catch (SQLException ex) {
            dataSourceStat.getConnectionStat().connectError(ex);
            throw ex;
        }
        dataSourceStat.getConnectionStat().afterConnected(nanoSpan);
        if (connection != null) {
            final JdbcConnectionStat.Entry statEntry = this.getConnectionInfo(connection);
            dataSourceStat.getConnections().put(connection.getId(), statEntry);
            statEntry.setConnectTime(new Date(startTime));
            statEntry.setConnectTimespanNano(nanoSpan);
            statEntry.setEstablishNano(System.nanoTime());
            statEntry.setEstablishTime(nowTime);
            statEntry.setConnectStackTrace(new Exception());
            dataSourceStat.getConnectionStat().setActiveCount(dataSourceStat.getConnections().size());
        }
        return connection;
    }
    
    @Override
    public void connection_close(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        if (connection.getCloseCount() == 0) {
            final long nowNano = System.nanoTime();
            final JdbcDataSourceStat dataSourceStat = chain.getDataSource().getDataSourceStat();
            dataSourceStat.getConnectionStat().incrementConnectionCloseCount();
            final JdbcConnectionStat.Entry connectionInfo = this.getConnectionInfo(connection);
            final long aliveNanoSpan = nowNano - connectionInfo.getEstablishNano();
            final JdbcConnectionStat.Entry existsConnection = dataSourceStat.getConnections().remove(connection.getId());
            if (existsConnection != null) {
                dataSourceStat.getConnectionStat().afterClose(aliveNanoSpan);
            }
        }
        chain.connection_close(connection);
    }
    
    @Override
    public void connection_commit(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        chain.connection_commit(connection);
        final JdbcDataSourceStat dataSourceStat = chain.getDataSource().getDataSourceStat();
        dataSourceStat.getConnectionStat().incrementConnectionCommitCount();
    }
    
    @Override
    public void connection_rollback(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        chain.connection_rollback(connection);
        final JdbcDataSourceStat dataSourceStat = chain.getDataSource().getDataSourceStat();
        dataSourceStat.getConnectionStat().incrementConnectionRollbackCount();
    }
    
    @Override
    public void connection_rollback(final FilterChain chain, final ConnectionProxy connection, final Savepoint savepoint) throws SQLException {
        chain.connection_rollback(connection, savepoint);
        final JdbcDataSourceStat dataSourceStat = connection.getDirectDataSource().getDataSourceStat();
        dataSourceStat.getConnectionStat().incrementConnectionRollbackCount();
    }
    
    public void statementCreateAfter(final StatementProxy statement) {
        final JdbcDataSourceStat dataSourceStat = statement.getConnectionProxy().getDirectDataSource().getDataSourceStat();
        dataSourceStat.getStatementStat().incrementCreateCounter();
        super.statementCreateAfter(statement);
    }
    
    public void statementPrepareCallAfter(final CallableStatementProxy statement) {
        final JdbcDataSourceStat dataSourceStat = statement.getConnectionProxy().getDirectDataSource().getDataSourceStat();
        dataSourceStat.getStatementStat().incrementPrepareCallCount();
        final JdbcSqlStat sqlStat = this.createSqlStat(statement, statement.getSql());
        statement.setSqlStat(sqlStat);
    }
    
    public void statementPrepareAfter(final PreparedStatementProxy statement) {
        final JdbcDataSourceStat dataSourceStat = statement.getConnectionProxy().getDirectDataSource().getDataSourceStat();
        dataSourceStat.getStatementStat().incrementPrepareCounter();
        final JdbcSqlStat sqlStat = this.createSqlStat(statement, statement.getSql());
        statement.setSqlStat(sqlStat);
    }
    
    @Override
    public void statement_close(final FilterChain chain, final StatementProxy statement) throws SQLException {
        chain.statement_close(statement);
        final JdbcDataSourceStat dataSourceStat = chain.getDataSource().getDataSourceStat();
        dataSourceStat.getStatementStat().incrementStatementCloseCounter();
        final JdbcStatContext context = JdbcStatManager.getInstance().getStatContext();
        if (context != null) {
            context.setName(null);
            context.setFile(null);
            context.setSql(null);
        }
    }
    
    @Override
    protected void statementExecuteUpdateBefore(final StatementProxy statement, final String sql) {
        this.internalBeforeStatementExecute(statement, sql);
    }
    
    @Override
    protected void statementExecuteUpdateAfter(final StatementProxy statement, final String sql, final int updateCount) {
        this.internalAfterStatementExecute(statement, false, updateCount);
    }
    
    @Override
    protected void statementExecuteQueryBefore(final StatementProxy statement, final String sql) {
        this.internalBeforeStatementExecute(statement, sql);
    }
    
    @Override
    protected void statementExecuteQueryAfter(final StatementProxy statement, final String sql, final ResultSetProxy resultSet) {
        this.internalAfterStatementExecute(statement, true, new int[0]);
    }
    
    @Override
    protected void statementExecuteBefore(final StatementProxy statement, final String sql) {
        this.internalBeforeStatementExecute(statement, sql);
    }
    
    @Override
    protected void statementExecuteAfter(final StatementProxy statement, final String sql, final boolean firstResult) {
        this.internalAfterStatementExecute(statement, firstResult, new int[0]);
    }
    
    @Override
    protected void statementExecuteBatchBefore(final StatementProxy statement) {
        final String sql = statement.getBatchSql();
        final int batchSize = statement.getBatchSqlList().size();
        JdbcSqlStat sqlStat = statement.getSqlStat();
        if (sqlStat == null || sqlStat.isRemoved()) {
            sqlStat = this.createSqlStat(statement, sql);
            statement.setSqlStat(sqlStat);
        }
        if (sqlStat != null) {
            sqlStat.addExecuteBatchCount(batchSize);
        }
        this.internalBeforeStatementExecute(statement, sql);
    }
    
    @Override
    protected void statementExecuteBatchAfter(final StatementProxy statement, final int[] result) {
        this.internalAfterStatementExecute(statement, false, result);
    }
    
    private final void internalBeforeStatementExecute(final StatementProxy statement, final String sql) {
        final JdbcDataSourceStat dataSourceStat = statement.getConnectionProxy().getDirectDataSource().getDataSourceStat();
        dataSourceStat.getStatementStat().beforeExecute();
        final ConnectionProxy connection = statement.getConnectionProxy();
        final JdbcConnectionStat.Entry connectionCounter = this.getConnectionInfo(connection);
        statement.setLastExecuteStartNano();
        connectionCounter.setLastSql(sql);
        if (this.connectionStackTraceEnable) {
            connectionCounter.setLastStatementStatckTrace(new Exception());
        }
        JdbcSqlStat sqlStat = statement.getSqlStat();
        if (sqlStat == null || sqlStat.isRemoved()) {
            sqlStat = this.createSqlStat(statement, sql);
            statement.setSqlStat(sqlStat);
        }
        final JdbcStatContext statContext = JdbcStatManager.getInstance().getStatContext();
        if (statContext != null) {
            sqlStat.setName(statContext.getName());
            sqlStat.setFile(statContext.getFile());
        }
        boolean inTransaction = false;
        try {
            inTransaction = !statement.getConnectionProxy().getAutoCommit();
        }
        catch (SQLException e) {
            StatFilter.LOG.error("getAutoCommit error", e);
        }
        if (sqlStat != null) {
            sqlStat.setExecuteLastStartTime(System.currentTimeMillis());
            sqlStat.incrementRunningCount();
            if (inTransaction) {
                sqlStat.incrementInTransactionCount();
            }
        }
        StatFilterContext.getInstance().executeBefore(sql, inTransaction);
        String mergedSql;
        if (sqlStat != null) {
            mergedSql = sqlStat.getSql();
        }
        else {
            mergedSql = sql;
        }
        Profiler.enter(mergedSql, "SQL");
    }
    
    private final void internalAfterStatementExecute(final StatementProxy statement, final boolean firstResult, final int... updateCountArray) {
        final long nowNano = System.nanoTime();
        final long nanos = nowNano - statement.getLastExecuteStartNano();
        final JdbcDataSourceStat dataSourceStat = statement.getConnectionProxy().getDirectDataSource().getDataSourceStat();
        dataSourceStat.getStatementStat().afterExecute(nanos);
        final JdbcSqlStat sqlStat = statement.getSqlStat();
        if (sqlStat != null) {
            sqlStat.incrementExecuteSuccessCount();
            sqlStat.decrementRunningCount();
            sqlStat.addExecuteTime(statement.getLastExecuteType(), firstResult, nanos);
            statement.setLastExecuteTimeNano(nanos);
            if (!firstResult && statement.getLastExecuteType() == StatementExecuteType.Execute) {
                try {
                    final int updateCount = statement.getUpdateCount();
                    sqlStat.addUpdateCount(updateCount);
                }
                catch (SQLException e) {
                    StatFilter.LOG.error("getUpdateCount error", e);
                }
            }
            else {
                for (final int updateCount2 : updateCountArray) {
                    sqlStat.addUpdateCount(updateCount2);
                    sqlStat.addFetchRowCount(0L);
                    StatFilterContext.getInstance().addUpdateCount(updateCount2);
                }
            }
            final long millis = nanos / 1000000L;
            if (millis >= this.slowSqlMillis) {
                final String slowParameters = this.buildSlowParameters(statement);
                sqlStat.setLastSlowParameters(slowParameters);
                final String lastExecSql = statement.getLastExecuteSql();
                if (this.logSlowSql) {
                    final String msg = "slow sql " + millis + " millis. " + lastExecSql + "" + slowParameters;
                    final String slowSqlLogLevel = this.slowSqlLogLevel;
                    switch (slowSqlLogLevel) {
                        case "WARN": {
                            StatFilter.LOG.warn(msg);
                            break;
                        }
                        case "INFO": {
                            StatFilter.LOG.info(msg);
                            break;
                        }
                        case "DEBUG": {
                            StatFilter.LOG.debug(msg);
                            break;
                        }
                        default: {
                            StatFilter.LOG.error(msg);
                            break;
                        }
                    }
                }
                this.handleSlowSql(statement);
            }
        }
        final String sql = statement.getLastExecuteSql();
        StatFilterContext.getInstance().executeAfter(sql, nanos, null);
        Profiler.release(nanos);
    }
    
    protected void handleSlowSql(final StatementProxy statementProxy) {
    }
    
    @Override
    protected void statement_executeErrorAfter(final StatementProxy statement, final String sql, final Throwable error) {
        final ConnectionProxy connection = statement.getConnectionProxy();
        final JdbcConnectionStat.Entry connectionCounter = this.getConnectionInfo(connection);
        final long nanos = System.nanoTime() - statement.getLastExecuteStartNano();
        final JdbcDataSourceStat dataSourceStat = statement.getConnectionProxy().getDirectDataSource().getDataSourceStat();
        dataSourceStat.getStatementStat().error(error);
        dataSourceStat.getStatementStat().afterExecute(nanos);
        connectionCounter.error(error);
        final JdbcSqlStat sqlStat = statement.getSqlStat();
        if (sqlStat != null) {
            sqlStat.decrementExecutingCount();
            sqlStat.error(error);
            sqlStat.addExecuteTime(statement.getLastExecuteType(), statement.isFirstResultSet(), nanos);
            statement.setLastExecuteTimeNano(nanos);
        }
        StatFilterContext.getInstance().executeAfter(sql, nanos, error);
        Profiler.release(nanos);
    }
    
    protected String buildSlowParameters(final StatementProxy statement) {
        final JSONWriter out = new JSONWriter();
        out.writeArrayStart();
        for (int i = 0, parametersSize = statement.getParametersSize(); i < parametersSize; ++i) {
            final JdbcParameter parameter = statement.getParameter(i);
            if (i != 0) {
                out.writeComma();
            }
            if (parameter != null) {
                final Object value = parameter.getValue();
                if (value == null) {
                    out.writeNull();
                }
                else if (value instanceof String) {
                    final String text = (String)value;
                    if (text.length() > 100) {
                        out.writeString(text.substring(0, 97) + "...");
                    }
                    else {
                        out.writeString(text);
                    }
                }
                else if (value instanceof Number) {
                    out.writeObject(value);
                }
                else if (value instanceof Date) {
                    out.writeObject(value);
                }
                else if (value instanceof Boolean) {
                    out.writeObject(value);
                }
                else if (value instanceof InputStream) {
                    out.writeString("<InputStream>");
                }
                else if (value instanceof NClob) {
                    out.writeString("<NClob>");
                }
                else if (value instanceof Clob) {
                    out.writeString("<Clob>");
                }
                else if (value instanceof Blob) {
                    out.writeString("<Blob>");
                }
                else {
                    out.writeString('<' + value.getClass().getName() + '>');
                }
            }
        }
        out.writeArrayEnd();
        return out.toString();
    }
    
    @Override
    protected void resultSetOpenAfter(final ResultSetProxy resultSet) {
        final JdbcDataSourceStat dataSourceStat = resultSet.getStatementProxy().getConnectionProxy().getDirectDataSource().getDataSourceStat();
        dataSourceStat.getResultSetStat().beforeOpen();
        resultSet.setConstructNano();
        StatFilterContext.getInstance().resultSet_open();
    }
    
    @Override
    public void resultSet_close(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        final long nanos = System.nanoTime() - resultSet.getConstructNano();
        final int fetchRowCount = resultSet.getFetchRowCount();
        final JdbcDataSourceStat dataSourceStat = chain.getDataSource().getDataSourceStat();
        dataSourceStat.getResultSetStat().afterClose(nanos);
        dataSourceStat.getResultSetStat().addFetchRowCount(fetchRowCount);
        dataSourceStat.getResultSetStat().incrementCloseCounter();
        StatFilterContext.getInstance().addFetchRowCount(fetchRowCount);
        final String sql = resultSet.getSql();
        if (sql != null) {
            final JdbcSqlStat sqlStat = resultSet.getSqlStat();
            if (sqlStat != null && resultSet.getCloseCount() == 0) {
                sqlStat.addFetchRowCount(fetchRowCount);
                final long stmtExecuteNano = resultSet.getStatementProxy().getLastExecuteTimeNano();
                sqlStat.addResultSetHoldTimeNano(stmtExecuteNano, nanos);
                if (resultSet.getReadStringLength() > 0L) {
                    sqlStat.addStringReadLength(resultSet.getReadStringLength());
                }
                if (resultSet.getReadBytesLength() > 0L) {
                    sqlStat.addReadBytesLength(resultSet.getReadBytesLength());
                }
                if (resultSet.getOpenInputStreamCount() > 0) {
                    sqlStat.addInputStreamOpenCount(resultSet.getOpenInputStreamCount());
                }
                if (resultSet.getOpenReaderCount() > 0) {
                    sqlStat.addReaderOpenCount(resultSet.getOpenReaderCount());
                }
            }
        }
        chain.resultSet_close(resultSet);
        StatFilterContext.getInstance().resultSet_close(nanos);
    }
    
    public JdbcConnectionStat.Entry getConnectionInfo(final ConnectionProxy connection) {
        JdbcConnectionStat.Entry counter = (JdbcConnectionStat.Entry)connection.getAttribute("stat.conn");
        if (counter == null) {
            final String dataSourceName = connection.getDirectDataSource().getName();
            connection.putAttribute("stat.conn", new JdbcConnectionStat.Entry(dataSourceName, connection.getId()));
            counter = (JdbcConnectionStat.Entry)connection.getAttribute("stat.conn");
        }
        return counter;
    }
    
    public JdbcSqlStat createSqlStat(final StatementProxy statement, String sql) {
        final DataSourceProxy dataSource = statement.getConnectionProxy().getDirectDataSource();
        final JdbcDataSourceStat dataSourceStat = dataSource.getDataSourceStat();
        final JdbcStatContext context = JdbcStatManager.getInstance().getStatContext();
        final String contextSql = (context != null) ? context.getSql() : null;
        if (contextSql != null && contextSql.length() > 0) {
            return dataSourceStat.createSqlStat(contextSql);
        }
        DbType dbType = this.dbType;
        if (dbType == null) {
            dbType = DbType.of(dataSource.getDbType());
        }
        sql = this.mergeSql(sql, dbType);
        return dataSourceStat.createSqlStat(sql);
    }
    
    public static StatFilter getStatFilter(final DataSourceProxy dataSource) {
        for (final Filter filter : dataSource.getProxyFilters()) {
            if (filter instanceof StatFilter) {
                return (StatFilter)filter;
            }
        }
        return null;
    }
    
    @Override
    public void dataSource_releaseConnection(final FilterChain chain, final DruidPooledConnection conn) throws SQLException {
        chain.dataSource_recycle(conn);
        final long nanos = System.nanoTime() - conn.getConnectedTimeNano();
        final long millis = nanos / 1000000L;
        final JdbcDataSourceStat dataSourceStat = chain.getDataSource().getDataSourceStat();
        dataSourceStat.getConnectionHoldHistogram().record(millis);
        StatFilterContext.getInstance().pool_connection_close(nanos);
    }
    
    @Override
    public DruidPooledConnection dataSource_getConnection(final FilterChain chain, final DruidDataSource dataSource, final long maxWaitMillis) throws SQLException {
        final DruidPooledConnection conn = chain.dataSource_connect(dataSource, maxWaitMillis);
        if (conn != null) {
            conn.setConnectedTimeNano();
            StatFilterContext.getInstance().pool_connection_open();
        }
        return conn;
    }
    
    @Override
    public Clob resultSet_getClob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        final Clob clob = chain.resultSet_getClob(resultSet, columnIndex);
        if (clob != null) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), resultSet, (ClobProxy)clob);
        }
        return clob;
    }
    
    @Override
    public Clob resultSet_getClob(final FilterChain chain, final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        final Clob clob = chain.resultSet_getClob(resultSet, columnLabel);
        if (clob != null) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), resultSet, (ClobProxy)clob);
        }
        return clob;
    }
    
    @Override
    public Blob callableStatement_getBlob(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        final Blob blob = chain.callableStatement_getBlob(statement, parameterIndex);
        if (blob != null) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), statement, blob);
        }
        return blob;
    }
    
    @Override
    public Blob callableStatement_getBlob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        final Blob blob = chain.callableStatement_getBlob(statement, parameterName);
        if (blob != null) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), statement, blob);
        }
        return blob;
    }
    
    @Override
    public Blob resultSet_getBlob(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        final Blob blob = chain.resultSet_getBlob(result, columnIndex);
        if (blob != null) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), result, blob);
        }
        return blob;
    }
    
    @Override
    public Blob resultSet_getBlob(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        final Blob blob = chain.resultSet_getBlob(result, columnLabel);
        if (blob != null) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), result, blob);
        }
        return blob;
    }
    
    @Override
    public Clob callableStatement_getClob(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        final Clob clob = chain.callableStatement_getClob(statement, parameterIndex);
        if (clob != null) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), statement, (ClobProxy)clob);
        }
        return clob;
    }
    
    @Override
    public Clob callableStatement_getClob(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        final Clob clob = chain.callableStatement_getClob(statement, parameterName);
        if (clob != null) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), statement, (ClobProxy)clob);
        }
        return clob;
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        final Object obj = chain.resultSet_getObject(result, columnIndex);
        if (obj instanceof Clob) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), result, (ClobProxy)obj);
        }
        else if (obj instanceof Blob) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), result, (Blob)obj);
        }
        else if (obj instanceof String) {
            result.addReadStringLength(((String)obj).length());
        }
        return obj;
    }
    
    @Override
    public <T> T resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Class<T> type) throws SQLException {
        final T obj = chain.resultSet_getObject(result, columnIndex, type);
        if (obj instanceof Clob) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), result, (ClobProxy)obj);
        }
        else if (obj instanceof Blob) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), result, (Blob)obj);
        }
        else if (obj instanceof String) {
            result.addReadStringLength(((String)obj).length());
        }
        return obj;
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
        final Object obj = chain.resultSet_getObject(result, columnIndex, map);
        if (obj instanceof Clob) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), result, (ClobProxy)obj);
        }
        else if (obj instanceof Blob) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), result, (Blob)obj);
        }
        else if (obj instanceof String) {
            result.addReadStringLength(((String)obj).length());
        }
        return obj;
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        final Object obj = chain.resultSet_getObject(result, columnLabel);
        if (obj instanceof Clob) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), result, (ClobProxy)obj);
        }
        else if (obj instanceof Blob) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), result, (Blob)obj);
        }
        else if (obj instanceof String) {
            result.addReadStringLength(((String)obj).length());
        }
        return obj;
    }
    
    @Override
    public <T> T resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Class<T> type) throws SQLException {
        final T obj = chain.resultSet_getObject(result, columnLabel, type);
        if (obj instanceof Clob) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), result, (ClobProxy)obj);
        }
        else if (obj instanceof Blob) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), result, (Blob)obj);
        }
        else if (obj instanceof String) {
            result.addReadStringLength(((String)obj).length());
        }
        return obj;
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy result, final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
        final Object obj = chain.resultSet_getObject(result, columnLabel, map);
        if (obj instanceof Clob) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), result, (ClobProxy)obj);
        }
        else if (obj instanceof Blob) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), result, (Blob)obj);
        }
        else if (obj instanceof String) {
            result.addReadStringLength(((String)obj).length());
        }
        return obj;
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        final Object obj = chain.callableStatement_getObject(statement, parameterIndex);
        if (obj instanceof Clob) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), statement, (ClobProxy)obj);
        }
        else if (obj instanceof Blob) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), statement, (Blob)obj);
        }
        return obj;
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex, final Map<String, Class<?>> map) throws SQLException {
        final Object obj = chain.callableStatement_getObject(statement, parameterIndex, map);
        if (obj instanceof Clob) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), statement, (ClobProxy)obj);
        }
        else if (obj instanceof Blob) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), statement, (Blob)obj);
        }
        return obj;
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        final Object obj = chain.callableStatement_getObject(statement, parameterName);
        if (obj instanceof Clob) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), statement, (ClobProxy)obj);
        }
        else if (obj instanceof Blob) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), statement, (Blob)obj);
        }
        return obj;
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Map<String, Class<?>> map) throws SQLException {
        final Object obj = chain.callableStatement_getObject(statement, parameterName, map);
        if (obj instanceof Clob) {
            this.clobOpenAfter(chain.getDataSource().getDataSourceStat(), statement, (ClobProxy)obj);
        }
        else if (obj instanceof Blob) {
            this.blobOpenAfter(chain.getDataSource().getDataSourceStat(), statement, (Blob)obj);
        }
        return obj;
    }
    
    private void blobOpenAfter(final JdbcDataSourceStat dataSourceStat, final ResultSetProxy rs, final Blob blob) {
        this.blobOpenAfter(dataSourceStat, rs.getStatementProxy(), blob);
    }
    
    private void clobOpenAfter(final JdbcDataSourceStat dataSourceStat, final ResultSetProxy rs, final ClobProxy clob) {
        this.clobOpenAfter(dataSourceStat, rs.getStatementProxy(), clob);
    }
    
    private void blobOpenAfter(final JdbcDataSourceStat dataSourceStat, final StatementProxy stmt, final Blob blob) {
        dataSourceStat.incrementBlobOpenCount();
        if (stmt != null) {
            final JdbcSqlStat sqlStat = stmt.getSqlStat();
            if (sqlStat != null) {
                sqlStat.incrementBlobOpenCount();
            }
        }
        StatFilterContext.getInstance().blob_open();
    }
    
    private void clobOpenAfter(final JdbcDataSourceStat dataSourceStat, final StatementProxy stmt, final ClobProxy clob) {
        dataSourceStat.incrementClobOpenCount();
        if (stmt != null) {
            final JdbcSqlStat sqlStat = stmt.getSqlStat();
            if (sqlStat != null) {
                sqlStat.incrementClobOpenCount();
            }
        }
        StatFilterContext.getInstance().clob_open();
    }
    
    @Override
    public String resultSet_getString(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        final String value = chain.resultSet_getString(result, columnIndex);
        if (value != null) {
            result.addReadStringLength(value.length());
        }
        return value;
    }
    
    @Override
    public String resultSet_getString(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        final String value = chain.resultSet_getString(result, columnLabel);
        if (value != null) {
            result.addReadStringLength(value.length());
        }
        return value;
    }
    
    @Override
    public byte[] resultSet_getBytes(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        final byte[] value = chain.resultSet_getBytes(result, columnIndex);
        if (value != null) {
            result.addReadBytesLength(value.length);
        }
        return value;
    }
    
    @Override
    public byte[] resultSet_getBytes(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        final byte[] value = chain.resultSet_getBytes(result, columnLabel);
        if (value != null) {
            result.addReadBytesLength(value.length);
        }
        return value;
    }
    
    @Override
    public InputStream resultSet_getBinaryStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        final InputStream input = chain.resultSet_getBinaryStream(result, columnIndex);
        if (input != null) {
            result.incrementOpenInputStreamCount();
        }
        return input;
    }
    
    @Override
    public InputStream resultSet_getBinaryStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        final InputStream input = chain.resultSet_getBinaryStream(result, columnLabel);
        if (input != null) {
            result.incrementOpenInputStreamCount();
        }
        return input;
    }
    
    @Override
    public InputStream resultSet_getAsciiStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        final InputStream input = chain.resultSet_getAsciiStream(result, columnIndex);
        if (input != null) {
            result.incrementOpenInputStreamCount();
        }
        return input;
    }
    
    @Override
    public InputStream resultSet_getAsciiStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        final InputStream input = chain.resultSet_getAsciiStream(result, columnLabel);
        if (input != null) {
            result.incrementOpenInputStreamCount();
        }
        return input;
    }
    
    @Override
    public Reader resultSet_getCharacterStream(final FilterChain chain, final ResultSetProxy result, final int columnIndex) throws SQLException {
        final Reader reader = chain.resultSet_getCharacterStream(result, columnIndex);
        if (reader != null) {
            result.incrementOpenReaderCount();
        }
        return reader;
    }
    
    @Override
    public Reader resultSet_getCharacterStream(final FilterChain chain, final ResultSetProxy result, final String columnLabel) throws SQLException {
        final Reader reader = chain.resultSet_getCharacterStream(result, columnLabel);
        if (reader != null) {
            result.incrementOpenReaderCount();
        }
        return reader;
    }
    
    static {
        LOG = LogFactory.getLog(StatFilter.class);
    }
}
