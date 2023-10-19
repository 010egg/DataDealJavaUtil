// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.logging;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.util.JdbcUtils;
import java.util.Map;
import com.alibaba.druid.proxy.jdbc.CallableStatementProxy;
import java.sql.ResultSetMetaData;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import java.util.List;
import java.util.ArrayList;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import java.sql.SQLException;
import java.sql.Savepoint;
import com.alibaba.druid.filter.FilterChain;
import java.sql.Connection;
import com.alibaba.druid.util.MySqlUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import java.util.Properties;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.filter.FilterEventAdapter;

public abstract class LogFilter extends FilterEventAdapter implements LogFilterMBean
{
    protected String dataSourceLoggerName;
    protected String connectionLoggerName;
    protected String statementLoggerName;
    protected String resultSetLoggerName;
    private boolean connectionConnectBeforeLogEnable;
    private boolean connectionConnectAfterLogEnable;
    private boolean connectionCommitAfterLogEnable;
    private boolean connectionRollbackAfterLogEnable;
    private boolean connectionCloseAfterLogEnable;
    private boolean statementCreateAfterLogEnable;
    private boolean statementPrepareAfterLogEnable;
    private boolean statementPrepareCallAfterLogEnable;
    private boolean statementExecuteAfterLogEnable;
    private boolean statementExecuteQueryAfterLogEnable;
    private boolean statementExecuteUpdateAfterLogEnable;
    private boolean statementExecuteBatchAfterLogEnable;
    private boolean statementExecutableSqlLogEnable;
    private boolean statementCloseAfterLogEnable;
    private boolean statementParameterClearLogEnable;
    private boolean statementParameterSetLogEnable;
    private boolean resultSetNextAfterLogEnable;
    private boolean resultSetOpenAfterLogEnable;
    private boolean resultSetCloseAfterLogEnable;
    private boolean dataSourceLogEnabled;
    private boolean connectionLogEnabled;
    private boolean connectionLogErrorEnabled;
    private boolean statementLogEnabled;
    private boolean statementLogErrorEnabled;
    private boolean resultSetLogEnabled;
    private boolean resultSetLogErrorEnabled;
    private SQLUtils.FormatOption statementSqlFormatOption;
    private boolean statementLogSqlPrettyFormat;
    protected DataSourceProxy dataSource;
    
    public LogFilter() {
        this.dataSourceLoggerName = "druid.sql.DataSource";
        this.connectionLoggerName = "druid.sql.Connection";
        this.statementLoggerName = "druid.sql.Statement";
        this.resultSetLoggerName = "druid.sql.ResultSet";
        this.connectionConnectBeforeLogEnable = true;
        this.connectionConnectAfterLogEnable = true;
        this.connectionCommitAfterLogEnable = true;
        this.connectionRollbackAfterLogEnable = true;
        this.connectionCloseAfterLogEnable = true;
        this.statementCreateAfterLogEnable = true;
        this.statementPrepareAfterLogEnable = true;
        this.statementPrepareCallAfterLogEnable = true;
        this.statementExecuteAfterLogEnable = true;
        this.statementExecuteQueryAfterLogEnable = true;
        this.statementExecuteUpdateAfterLogEnable = true;
        this.statementExecuteBatchAfterLogEnable = true;
        this.statementExecutableSqlLogEnable = false;
        this.statementCloseAfterLogEnable = true;
        this.statementParameterClearLogEnable = true;
        this.statementParameterSetLogEnable = true;
        this.resultSetNextAfterLogEnable = true;
        this.resultSetOpenAfterLogEnable = true;
        this.resultSetCloseAfterLogEnable = true;
        this.dataSourceLogEnabled = true;
        this.connectionLogEnabled = true;
        this.connectionLogErrorEnabled = true;
        this.statementLogEnabled = true;
        this.statementLogErrorEnabled = true;
        this.resultSetLogEnabled = true;
        this.resultSetLogErrorEnabled = true;
        this.statementSqlFormatOption = new SQLUtils.FormatOption(false, true);
        this.statementLogSqlPrettyFormat = false;
        this.configFromProperties(System.getProperties());
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
        if (properties == null) {
            return;
        }
        String prop = properties.getProperty("druid.log.conn");
        if ("false".equals(prop)) {
            this.connectionLogEnabled = false;
        }
        else if ("true".equals(prop)) {
            this.connectionLogEnabled = true;
        }
        prop = properties.getProperty("druid.log.stmt");
        if ("false".equals(prop)) {
            this.statementLogEnabled = false;
        }
        else if ("true".equals(prop)) {
            this.statementLogEnabled = true;
        }
        prop = properties.getProperty("druid.log.rs");
        if ("false".equals(prop)) {
            this.resultSetLogEnabled = false;
        }
        else if ("true".equals(prop)) {
            this.resultSetLogEnabled = true;
        }
        prop = properties.getProperty("druid.log.stmt.executableSql");
        if ("true".equals(prop)) {
            this.statementExecutableSqlLogEnable = true;
        }
        else if ("false".equals(prop)) {
            this.statementExecutableSqlLogEnable = false;
        }
        prop = properties.getProperty("druid.log.conn.logError");
        if ("false".equals(prop)) {
            this.connectionLogErrorEnabled = false;
        }
        else if ("true".equals(prop)) {
            this.connectionLogErrorEnabled = true;
        }
        prop = properties.getProperty("druid.log.stmt.logError");
        if ("false".equals(prop)) {
            this.statementLogErrorEnabled = false;
        }
        else if ("true".equals(prop)) {
            this.statementLogErrorEnabled = true;
        }
        prop = properties.getProperty("druid.log.rs.logError");
        if ("false".equals(prop)) {
            this.resultSetLogErrorEnabled = false;
        }
        else if ("true".equals(prop)) {
            this.resultSetLogErrorEnabled = true;
        }
    }
    
    @Override
    public void init(final DataSourceProxy dataSource) {
        this.dataSource = dataSource;
        this.configFromProperties(dataSource.getConnectProperties());
        this.configFromProperties(System.getProperties());
    }
    
    @Override
    public boolean isConnectionLogErrorEnabled() {
        return this.connectionLogErrorEnabled;
    }
    
    @Override
    public boolean isResultSetCloseAfterLogEnabled() {
        return this.isResultSetLogEnabled() && this.resultSetCloseAfterLogEnable;
    }
    
    @Override
    public void setResultSetCloseAfterLogEnabled(final boolean resultSetCloseAfterLogEnable) {
        this.resultSetCloseAfterLogEnable = resultSetCloseAfterLogEnable;
    }
    
    @Override
    public void setConnectionLogErrorEnabled(final boolean connectionLogErrorEnabled) {
        this.connectionLogErrorEnabled = connectionLogErrorEnabled;
    }
    
    @Override
    public boolean isResultSetLogErrorEnabled() {
        return this.resultSetLogErrorEnabled;
    }
    
    @Override
    public void setResultSetLogErrorEnabled(final boolean resultSetLogErrorEnabled) {
        this.resultSetLogErrorEnabled = resultSetLogErrorEnabled;
    }
    
    @Override
    public boolean isConnectionConnectBeforeLogEnabled() {
        return this.isConnectionLogEnabled() && this.connectionConnectBeforeLogEnable;
    }
    
    @Override
    public void setConnectionConnectBeforeLogEnabled(final boolean beforeConnectionConnectLogEnable) {
        this.connectionConnectBeforeLogEnable = beforeConnectionConnectLogEnable;
    }
    
    @Override
    public boolean isConnectionCloseAfterLogEnabled() {
        return this.isConnectionLogEnabled() && this.connectionCloseAfterLogEnable;
    }
    
    public boolean isConnectionRollbackAfterLogEnabled() {
        return this.isConnectionLogEnabled() && this.connectionRollbackAfterLogEnable;
    }
    
    public void setConnectionRollbackAfterLogEnabled(final boolean connectionRollbackAfterLogEnable) {
        this.connectionRollbackAfterLogEnable = connectionRollbackAfterLogEnable;
    }
    
    @Override
    public void setConnectionCloseAfterLogEnabled(final boolean afterConnectionCloseLogEnable) {
        this.connectionCloseAfterLogEnable = afterConnectionCloseLogEnable;
    }
    
    @Override
    public boolean isConnectionCommitAfterLogEnabled() {
        return this.isConnectionLogEnabled() && this.connectionCommitAfterLogEnable;
    }
    
    @Override
    public void setConnectionCommitAfterLogEnabled(final boolean afterConnectionCommitLogEnable) {
        this.connectionCommitAfterLogEnable = afterConnectionCommitLogEnable;
    }
    
    @Override
    public boolean isConnectionConnectAfterLogEnabled() {
        return this.isConnectionLogEnabled() && this.connectionConnectAfterLogEnable;
    }
    
    @Override
    public void setConnectionConnectAfterLogEnabled(final boolean afterConnectionConnectLogEnable) {
        this.connectionConnectAfterLogEnable = afterConnectionConnectLogEnable;
    }
    
    @Override
    public boolean isResultSetNextAfterLogEnabled() {
        return this.isResultSetLogEnabled() && this.resultSetNextAfterLogEnable;
    }
    
    @Override
    public void setResultSetNextAfterLogEnabled(final boolean afterResultSetNextLogEnable) {
        this.resultSetNextAfterLogEnable = afterResultSetNextLogEnable;
    }
    
    @Override
    public boolean isResultSetOpenAfterLogEnabled() {
        return this.isResultSetLogEnabled() && this.resultSetOpenAfterLogEnable;
    }
    
    @Override
    public void setResultSetOpenAfterLogEnabled(final boolean afterResultSetOpenLogEnable) {
        this.resultSetOpenAfterLogEnable = afterResultSetOpenLogEnable;
    }
    
    @Override
    public boolean isStatementCloseAfterLogEnabled() {
        return this.isStatementLogEnabled() && this.statementCloseAfterLogEnable;
    }
    
    @Override
    public void setStatementCloseAfterLogEnabled(final boolean afterStatementCloseLogEnable) {
        this.statementCloseAfterLogEnable = afterStatementCloseLogEnable;
    }
    
    @Override
    public boolean isStatementCreateAfterLogEnabled() {
        return this.isStatementLogEnabled() && this.statementCreateAfterLogEnable;
    }
    
    @Override
    public void setStatementCreateAfterLogEnabled(final boolean afterStatementCreateLogEnable) {
        this.statementCreateAfterLogEnable = afterStatementCreateLogEnable;
    }
    
    @Override
    public boolean isStatementExecuteBatchAfterLogEnabled() {
        return this.isStatementLogEnabled() && this.statementExecuteBatchAfterLogEnable;
    }
    
    @Override
    public void setStatementExecuteBatchAfterLogEnabled(final boolean afterStatementExecuteBatchLogEnable) {
        this.statementExecuteBatchAfterLogEnable = afterStatementExecuteBatchLogEnable;
    }
    
    @Override
    public boolean isStatementExecuteAfterLogEnabled() {
        return this.isStatementLogEnabled() && this.statementExecuteAfterLogEnable;
    }
    
    @Override
    public void setStatementExecuteAfterLogEnabled(final boolean afterStatementExecuteLogEnable) {
        this.statementExecuteAfterLogEnable = afterStatementExecuteLogEnable;
    }
    
    @Override
    public boolean isStatementExecuteQueryAfterLogEnabled() {
        return this.isStatementLogEnabled() && this.statementExecuteQueryAfterLogEnable;
    }
    
    @Override
    public void setStatementExecuteQueryAfterLogEnabled(final boolean afterStatementExecuteQueryLogEnable) {
        this.statementExecuteQueryAfterLogEnable = afterStatementExecuteQueryLogEnable;
    }
    
    @Override
    public boolean isStatementExecuteUpdateAfterLogEnabled() {
        return this.isStatementLogEnabled() && this.statementExecuteUpdateAfterLogEnable;
    }
    
    @Override
    public void setStatementExecuteUpdateAfterLogEnabled(final boolean afterStatementExecuteUpdateLogEnable) {
        this.statementExecuteUpdateAfterLogEnable = afterStatementExecuteUpdateLogEnable;
    }
    
    public boolean isStatementExecutableSqlLogEnable() {
        return this.statementExecutableSqlLogEnable;
    }
    
    public void setStatementExecutableSqlLogEnable(final boolean statementExecutableSqlLogEnable) {
        this.statementExecutableSqlLogEnable = statementExecutableSqlLogEnable;
    }
    
    @Override
    public boolean isStatementPrepareCallAfterLogEnabled() {
        return this.isStatementLogEnabled() && this.statementPrepareCallAfterLogEnable;
    }
    
    @Override
    public void setStatementPrepareCallAfterLogEnabled(final boolean afterStatementPrepareCallLogEnable) {
        this.statementPrepareCallAfterLogEnable = afterStatementPrepareCallLogEnable;
    }
    
    @Override
    public boolean isStatementPrepareAfterLogEnabled() {
        return this.isStatementLogEnabled() && this.statementPrepareAfterLogEnable;
    }
    
    @Override
    public void setStatementPrepareAfterLogEnabled(final boolean afterStatementPrepareLogEnable) {
        this.statementPrepareAfterLogEnable = afterStatementPrepareLogEnable;
    }
    
    @Override
    public boolean isDataSourceLogEnabled() {
        return this.dataSourceLogEnabled;
    }
    
    @Override
    public void setDataSourceLogEnabled(final boolean dataSourceLogEnabled) {
        this.dataSourceLogEnabled = dataSourceLogEnabled;
    }
    
    @Override
    public boolean isConnectionLogEnabled() {
        return this.connectionLogEnabled;
    }
    
    @Override
    public void setConnectionLogEnabled(final boolean connectionLogEnabled) {
        this.connectionLogEnabled = connectionLogEnabled;
    }
    
    @Override
    public boolean isStatementLogEnabled() {
        return this.statementLogEnabled;
    }
    
    @Override
    public void setStatementLogEnabled(final boolean statementLogEnabled) {
        this.statementLogEnabled = statementLogEnabled;
    }
    
    @Override
    public boolean isStatementLogErrorEnabled() {
        return this.statementLogErrorEnabled;
    }
    
    @Override
    public void setStatementLogErrorEnabled(final boolean statementLogErrorEnabled) {
        this.statementLogErrorEnabled = statementLogErrorEnabled;
    }
    
    @Override
    public boolean isResultSetLogEnabled() {
        return this.resultSetLogEnabled;
    }
    
    @Override
    public void setResultSetLogEnabled(final boolean resultSetLogEnabled) {
        this.resultSetLogEnabled = resultSetLogEnabled;
    }
    
    @Override
    public boolean isStatementParameterSetLogEnabled() {
        return this.isStatementLogEnabled() && this.statementParameterSetLogEnable;
    }
    
    @Override
    public void setStatementParameterSetLogEnabled(final boolean statementParameterSetLogEnable) {
        this.statementParameterSetLogEnable = statementParameterSetLogEnable;
    }
    
    public boolean isStatementParameterClearLogEnable() {
        return this.isStatementLogEnabled() && this.statementParameterClearLogEnable;
    }
    
    public void setStatementParameterClearLogEnable(final boolean statementParameterClearLogEnable) {
        this.statementParameterClearLogEnable = statementParameterClearLogEnable;
    }
    
    @Override
    public SQLUtils.FormatOption getStatementSqlFormatOption() {
        return this.statementSqlFormatOption;
    }
    
    @Override
    public void setStatementSqlFormatOption(final SQLUtils.FormatOption formatOption) {
        this.statementSqlFormatOption = formatOption;
    }
    
    @Override
    public boolean isStatementSqlPrettyFormat() {
        return this.statementLogSqlPrettyFormat;
    }
    
    @Override
    public void setStatementSqlPrettyFormat(final boolean statementSqlPrettyFormat) {
        this.statementLogSqlPrettyFormat = statementSqlPrettyFormat;
    }
    
    protected abstract void connectionLog(final String p0);
    
    protected abstract void statementLog(final String p0);
    
    protected abstract void statementLogError(final String p0, final Throwable p1);
    
    protected abstract void resultSetLog(final String p0);
    
    protected abstract void resultSetLogError(final String p0, final Throwable p1);
    
    @Override
    public void connection_connectAfter(final ConnectionProxy connection) {
        if (connection == null) {
            return;
        }
        if (this.connectionConnectAfterLogEnable && this.isConnectionLogEnabled()) {
            final StringBuilder msg = new StringBuilder(34).append("{conn-").append(connection.getId());
            final Connection impl = connection.getRawObject();
            if (DbType.mysql == DbType.of(this.dataSource.getDbType())) {
                final Long procId = MySqlUtils.getId(impl);
                if (procId != null) {
                    msg.append(",procId-").append(procId);
                }
            }
            msg.append("} connected");
            this.connectionLog(msg.toString());
        }
    }
    
    @Override
    public Savepoint connection_setSavepoint(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        final Savepoint savepoint = chain.connection_setSavepoint(connection);
        if (this.isConnectionLogEnabled()) {
            this.connectionLog("{conn " + connection.getId() + "} setSavepoint-" + this.savepointToString(savepoint));
        }
        return savepoint;
    }
    
    @Override
    public Savepoint connection_setSavepoint(final FilterChain chain, final ConnectionProxy connection, final String name) throws SQLException {
        final Savepoint savepoint = chain.connection_setSavepoint(connection, name);
        if (this.isConnectionLogEnabled()) {
            this.connectionLog("{conn " + connection.getId() + "} setSavepoint-" + name);
        }
        return savepoint;
    }
    
    @Override
    public void connection_rollback(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        super.connection_rollback(chain, connection);
        if (this.connectionRollbackAfterLogEnable && this.isConnectionLogEnabled()) {
            this.connectionLog("{conn " + connection.getId() + "} rollback");
        }
    }
    
    @Override
    public void connection_rollback(final FilterChain chain, final ConnectionProxy connection, final Savepoint savePoint) throws SQLException {
        super.connection_rollback(chain, connection, savePoint);
        if (this.connectionRollbackAfterLogEnable && this.isConnectionLogEnabled()) {
            this.connectionLog("{conn " + connection.getId() + "} rollback -> " + this.savepointToString(savePoint));
        }
    }
    
    @Override
    public void connection_commit(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        super.connection_commit(chain, connection);
        if (this.connectionCommitAfterLogEnable && this.isConnectionLogEnabled()) {
            this.connectionLog("{conn-" + connection.getId() + "} commited");
        }
    }
    
    @Override
    public void connection_setAutoCommit(final FilterChain chain, final ConnectionProxy connection, final boolean autoCommit) throws SQLException {
        this.connectionLog("{conn-" + connection.getId() + "} setAutoCommit " + autoCommit);
        chain.connection_setAutoCommit(connection, autoCommit);
    }
    
    @Override
    public void connection_close(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        super.connection_close(chain, connection);
        if (this.connectionCloseAfterLogEnable && this.isConnectionLogEnabled()) {
            this.connectionLog("{conn-" + connection.getId() + "} closed");
        }
    }
    
    @Override
    public void statement_close(final FilterChain chain, final StatementProxy statement) throws SQLException {
        super.statement_close(chain, statement);
        if (this.statementCloseAfterLogEnable && this.isStatementLogEnabled()) {
            this.statementLog("{conn-" + statement.getConnectionProxy().getId() + ", " + this.stmtId(statement) + "} closed");
        }
    }
    
    @Override
    protected void statementExecuteBefore(final StatementProxy statement, final String sql) {
        statement.setLastExecuteStartNano();
        if (statement instanceof PreparedStatementProxy) {
            this.logParameter((PreparedStatementProxy)statement);
        }
    }
    
    @Override
    protected void statementExecuteAfter(final StatementProxy statement, final String sql, final boolean firstResult) {
        this.logExecutableSql(statement, sql);
        if (this.statementExecuteAfterLogEnable && this.isStatementLogEnabled()) {
            statement.setLastExecuteTimeNano();
            final double nanos = (double)statement.getLastExecuteTimeNano();
            final double millis = nanos / 1000000.0;
            this.statementLog("{conn-" + statement.getConnectionProxy().getId() + ", " + this.stmtId(statement) + "} executed. " + millis + " millis. " + sql);
        }
    }
    
    @Override
    protected void statementExecuteBatchBefore(final StatementProxy statement) {
        statement.setLastExecuteStartNano();
    }
    
    @Override
    protected void statementExecuteBatchAfter(final StatementProxy statement, final int[] result) {
        String sql;
        if (statement instanceof PreparedStatementProxy) {
            sql = ((PreparedStatementProxy)statement).getSql();
        }
        else {
            sql = statement.getBatchSql();
        }
        this.logExecutableSql(statement, sql);
        if (this.statementExecuteBatchAfterLogEnable && this.isStatementLogEnabled()) {
            statement.setLastExecuteTimeNano();
            final double nanos = (double)statement.getLastExecuteTimeNano();
            final double millis = nanos / 1000000.0;
            this.statementLog("{conn-" + statement.getConnectionProxy().getId() + ", " + this.stmtId(statement) + "} batch executed. " + millis + " millis. " + sql);
        }
    }
    
    @Override
    protected void statementExecuteQueryBefore(final StatementProxy statement, final String sql) {
        statement.setLastExecuteStartNano();
        if (statement instanceof PreparedStatementProxy) {
            this.logParameter((PreparedStatementProxy)statement);
        }
    }
    
    @Override
    protected void statementExecuteQueryAfter(final StatementProxy statement, final String sql, final ResultSetProxy resultSet) {
        this.logExecutableSql(statement, sql);
        if (this.statementExecuteQueryAfterLogEnable && this.isStatementLogEnabled()) {
            statement.setLastExecuteTimeNano();
            final double nanos = (double)statement.getLastExecuteTimeNano();
            final double millis = nanos / 1000000.0;
            this.statementLog("{conn-" + statement.getConnectionProxy().getId() + ", " + this.stmtId(statement) + ", rs-" + resultSet.getId() + "} query executed. " + millis + " millis. " + sql);
        }
    }
    
    @Override
    protected void statementExecuteUpdateBefore(final StatementProxy statement, final String sql) {
        statement.setLastExecuteStartNano();
        if (statement instanceof PreparedStatementProxy) {
            this.logParameter((PreparedStatementProxy)statement);
        }
    }
    
    @Override
    protected void statementExecuteUpdateAfter(final StatementProxy statement, final String sql, final int updateCount) {
        this.logExecutableSql(statement, sql);
        if (this.statementExecuteUpdateAfterLogEnable && this.isStatementLogEnabled()) {
            statement.setLastExecuteTimeNano();
            final double nanos = (double)statement.getLastExecuteTimeNano();
            final double millis = nanos / 1000000.0;
            this.statementLog("{conn-" + statement.getConnectionProxy().getId() + ", " + this.stmtId(statement) + "} update executed. effort " + updateCount + ". " + millis + " millis. " + sql);
        }
    }
    
    private void logExecutableSql(final StatementProxy statement, final String sql) {
        if (!this.isStatementExecutableSqlLogEnable() || !this.isStatementLogEnabled()) {
            return;
        }
        final int parametersSize = statement.getParametersSize();
        if (parametersSize == 0) {
            this.statementLog("{conn-" + statement.getConnectionProxy().getId() + ", " + this.stmtId(statement) + "} executed. " + sql);
            return;
        }
        final List<Object> parameters = new ArrayList<Object>(parametersSize);
        for (int i = 0; i < parametersSize; ++i) {
            final JdbcParameter jdbcParam = statement.getParameter(i);
            parameters.add((jdbcParam != null) ? jdbcParam.getValue() : null);
        }
        final DbType dbType = DbType.of(statement.getConnectionProxy().getDirectDataSource().getDbType());
        final String formattedSql = SQLUtils.format(sql, dbType, parameters, this.statementSqlFormatOption);
        this.statementLog("{conn-" + statement.getConnectionProxy().getId() + ", " + this.stmtId(statement) + "} executed. " + formattedSql);
    }
    
    @Override
    public void resultSet_close(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_close(resultSet);
        final StringBuffer buf = new StringBuffer();
        buf.append("{conn-");
        buf.append(resultSet.getStatementProxy().getConnectionProxy().getId());
        buf.append(", ");
        buf.append(this.stmtId(resultSet));
        buf.append(", rs-");
        buf.append(resultSet.getId());
        buf.append("} closed");
        if (this.isResultSetCloseAfterLogEnabled()) {
            this.resultSetLog(buf.toString());
        }
    }
    
    @Override
    public boolean resultSet_next(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        final boolean moreRows = super.resultSet_next(chain, resultSet);
        if (moreRows && this.resultSetNextAfterLogEnable && this.isResultSetLogEnabled()) {
            try {
                final StringBuffer buf = new StringBuffer();
                buf.append("{conn-");
                buf.append(resultSet.getStatementProxy().getConnectionProxy().getId());
                buf.append(", ");
                buf.append(this.stmtId(resultSet));
                buf.append(", rs-");
                buf.append(resultSet.getId());
                buf.append("}");
                buf.append(" Result: [");
                final ResultSetMetaData meta = resultSet.getMetaData();
                for (int i = 0, size = meta.getColumnCount(); i < size; ++i) {
                    if (i != 0) {
                        buf.append(", ");
                    }
                    final int columnIndex = i + 1;
                    final int type = meta.getColumnType(columnIndex);
                    Object value;
                    if (type == 93) {
                        value = resultSet.getTimestamp(columnIndex);
                    }
                    else if (type == 2004) {
                        value = "<BLOB>";
                    }
                    else if (type == 2005) {
                        value = "<CLOB>";
                    }
                    else if (type == 2011) {
                        value = "<NCLOB>";
                    }
                    else if (type == -2) {
                        value = "<BINARY>";
                    }
                    else {
                        value = resultSet.getObject(columnIndex);
                    }
                    buf.append(value);
                }
                buf.append("]");
                this.resultSetLog(buf.toString());
            }
            catch (SQLException ex) {
                this.resultSetLogError("logging error", ex);
            }
        }
        return moreRows;
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex) throws SQLException {
        final Object obj = chain.callableStatement_getObject(statement, parameterIndex);
        if (obj instanceof ResultSetProxy) {
            this.resultSetOpenAfter((ResultSetProxy)obj);
        }
        return obj;
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final int parameterIndex, final Map<String, Class<?>> map) throws SQLException {
        final Object obj = chain.callableStatement_getObject(statement, parameterIndex, map);
        if (obj instanceof ResultSetProxy) {
            this.resultSetOpenAfter((ResultSetProxy)obj);
        }
        return obj;
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName) throws SQLException {
        final Object obj = chain.callableStatement_getObject(statement, parameterName);
        if (obj instanceof ResultSetProxy) {
            this.resultSetOpenAfter((ResultSetProxy)obj);
        }
        return obj;
    }
    
    @Override
    public Object callableStatement_getObject(final FilterChain chain, final CallableStatementProxy statement, final String parameterName, final Map<String, Class<?>> map) throws SQLException {
        final Object obj = chain.callableStatement_getObject(statement, parameterName, map);
        if (obj instanceof ResultSetProxy) {
            this.resultSetOpenAfter((ResultSetProxy)obj);
        }
        return obj;
    }
    
    @Override
    protected void resultSetOpenAfter(final ResultSetProxy resultSet) {
        if (this.resultSetOpenAfterLogEnable && this.isResultSetLogEnabled()) {
            try {
                final StringBuffer buf = new StringBuffer();
                buf.append("{conn-");
                buf.append(resultSet.getStatementProxy().getConnectionProxy().getId());
                buf.append(", ");
                buf.append(this.stmtId(resultSet));
                buf.append(", rs-");
                buf.append(resultSet.getId());
                buf.append("}");
                final String resultId = buf.toString();
                this.resultSetLog(resultId + " open");
                buf.append(" Header: [");
                final ResultSetMetaData meta = resultSet.getMetaData();
                for (int i = 0, size = meta.getColumnCount(); i < size; ++i) {
                    if (i != 0) {
                        buf.append(", ");
                    }
                    buf.append(meta.getColumnName(i + 1));
                }
                buf.append("]");
                this.resultSetLog(buf.toString());
            }
            catch (SQLException ex) {
                this.resultSetLogError("logging error", ex);
            }
        }
    }
    
    @Override
    protected void statementCreateAfter(final StatementProxy statement) {
        if (this.statementCreateAfterLogEnable && this.isStatementLogEnabled()) {
            this.statementLog("{conn-" + statement.getConnectionProxy().getId() + ", stmt-" + statement.getId() + "} created");
        }
    }
    
    @Override
    protected void statementPrepareAfter(final PreparedStatementProxy statement) {
        if (this.statementPrepareAfterLogEnable && this.isStatementLogEnabled()) {
            this.statementLog("{conn-" + statement.getConnectionProxy().getId() + ", pstmt-" + statement.getId() + "} created. " + statement.getSql());
        }
    }
    
    @Override
    protected void statementPrepareCallAfter(final CallableStatementProxy statement) {
        if (this.statementPrepareCallAfterLogEnable && this.isStatementLogEnabled()) {
            this.statementLog("{conn-" + statement.getConnectionProxy().getId() + ", cstmt-" + statement.getId() + "} created. " + statement.getSql());
        }
    }
    
    @Override
    protected void statement_executeErrorAfter(final StatementProxy statement, final String sql, final Throwable error) {
        if (this.isStatementLogErrorEnabled()) {
            if (!this.isStatementExecutableSqlLogEnable()) {
                this.statementLogError("{conn-" + statement.getConnectionProxy().getId() + ", " + this.stmtId(statement) + "} execute error. " + sql, error);
            }
            else {
                final int parametersSize = statement.getParametersSize();
                if (parametersSize > 0) {
                    final List<Object> parameters = new ArrayList<Object>(parametersSize);
                    for (int i = 0; i < parametersSize; ++i) {
                        final JdbcParameter jdbcParam = statement.getParameter(i);
                        parameters.add((jdbcParam != null) ? jdbcParam.getValue() : null);
                    }
                    final DbType dbType = DbType.of(statement.getConnectionProxy().getDirectDataSource().getDbType());
                    final String formattedSql = SQLUtils.format(sql, dbType, parameters, this.statementSqlFormatOption);
                    this.statementLogError("{conn-" + statement.getConnectionProxy().getId() + ", " + this.stmtId(statement) + "} execute error. " + formattedSql, error);
                }
                else {
                    this.statementLogError("{conn-" + statement.getConnectionProxy().getId() + ", " + this.stmtId(statement) + "} execute error. " + sql, error);
                }
            }
        }
    }
    
    private String stmtId(final ResultSetProxy resultSet) {
        return this.stmtId(resultSet.getStatementProxy());
    }
    
    private String stmtId(final StatementProxy statement) {
        final StringBuffer buf = new StringBuffer();
        if (statement instanceof CallableStatementProxy) {
            buf.append("cstmt-");
        }
        else if (statement instanceof PreparedStatementProxy) {
            buf.append("pstmt-");
        }
        else {
            buf.append("stmt-");
        }
        buf.append(statement.getId());
        return buf.toString();
    }
    
    protected void logParameter(final PreparedStatementProxy statement) {
        if (this.isStatementParameterSetLogEnabled()) {
            StringBuffer buf = new StringBuffer();
            buf.append("{conn-");
            buf.append(statement.getConnectionProxy().getId());
            buf.append(", ");
            buf.append(this.stmtId(statement));
            buf.append("}");
            buf.append(" Parameters : [");
            for (int i = 0, parametersSize = statement.getParametersSize(); i < parametersSize; ++i) {
                final JdbcParameter parameter = statement.getParameter(i);
                if (i != 0) {
                    buf.append(", ");
                }
                if (parameter != null) {
                    final int sqlType = parameter.getSqlType();
                    final Object value = parameter.getValue();
                    switch (sqlType) {
                        case 0: {
                            buf.append("NULL");
                            break;
                        }
                        default: {
                            buf.append(String.valueOf(value));
                            break;
                        }
                    }
                }
            }
            buf.append("]");
            this.statementLog(buf.toString());
            buf = new StringBuffer();
            buf.append("{conn-");
            buf.append(statement.getConnectionProxy().getId());
            buf.append(", ");
            buf.append(this.stmtId(statement));
            buf.append("}");
            buf.append(" Types : [");
            for (int i = 0, parametersSize = statement.getParametersSize(); i < parametersSize; ++i) {
                final JdbcParameter parameter = statement.getParameter(i);
                if (i != 0) {
                    buf.append(", ");
                }
                if (parameter != null) {
                    final int sqlType = parameter.getSqlType();
                    buf.append(JdbcUtils.getTypeName(sqlType));
                }
            }
            buf.append("]");
            this.statementLog(buf.toString());
        }
    }
    
    @Override
    public void dataSource_releaseConnection(final FilterChain chain, final DruidPooledConnection conn) throws SQLException {
        long connectionId = -1L;
        if (conn.getConnectionHolder() != null) {
            final ConnectionProxy connection = (ConnectionProxy)conn.getConnectionHolder().getConnection();
            connectionId = connection.getId();
        }
        chain.dataSource_recycle(conn);
        if (this.connectionCloseAfterLogEnable && this.isConnectionLogEnabled()) {
            this.connectionLog("{conn-" + connectionId + "} pool-recycle");
        }
    }
    
    @Override
    public DruidPooledConnection dataSource_getConnection(final FilterChain chain, final DruidDataSource dataSource, final long maxWaitMillis) throws SQLException {
        final DruidPooledConnection conn = chain.dataSource_connect(dataSource, maxWaitMillis);
        final ConnectionProxy connection = (ConnectionProxy)conn.getConnectionHolder().getConnection();
        if (this.connectionConnectAfterLogEnable && this.isConnectionLogEnabled()) {
            this.connectionLog("{conn-" + connection.getId() + "} pool-connect");
        }
        return conn;
    }
    
    @Override
    public void preparedStatement_clearParameters(final FilterChain chain, final PreparedStatementProxy statement) throws SQLException {
        if (this.isStatementParameterClearLogEnable()) {
            this.statementLog("{conn-" + statement.getConnectionProxy().getId() + ", pstmt-" + statement.getId() + "} clearParameters. ");
        }
        chain.preparedStatement_clearParameters(statement);
    }
    
    @Override
    public void statement_clearBatch(final FilterChain chain, final StatementProxy statement) throws SQLException {
        if (this.isStatementParameterClearLogEnable()) {
            this.statementLog("{conn-" + statement.getConnectionProxy().getId() + ", stmt-" + statement.getId() + "} clearBatch. ");
        }
        chain.statement_clearBatch(statement);
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        return iface == this.getClass() || iface == LogFilter.class;
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) {
        if (iface == this.getClass() || iface == LogFilter.class) {
            return (T)this;
        }
        return null;
    }
    
    protected String savepointToString(final Savepoint savePoint) {
        String savePointString = null;
        try {
            savePointString = savePoint.getSavepointName();
        }
        catch (SQLException e) {
            try {
                savePointString = String.valueOf(savePoint.getSavepointId());
            }
            catch (SQLException e2) {
                savePointString = savePoint.toString();
            }
        }
        return savePointString;
    }
}
