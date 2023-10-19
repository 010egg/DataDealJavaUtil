// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import com.alibaba.druid.support.logging.LogFactory;
import java.sql.ResultSetMetaData;
import com.alibaba.druid.util.ServletPathMatcher;
import com.alibaba.druid.util.StringUtils;
import java.util.HashMap;
import com.alibaba.druid.proxy.jdbc.ResultSetMetaDataProxy;
import java.net.URL;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.SQLXML;
import java.sql.RowId;
import java.sql.Ref;
import java.sql.NClob;
import java.util.Calendar;
import java.sql.Date;
import java.sql.Clob;
import java.io.Reader;
import java.sql.Blob;
import java.math.BigDecimal;
import java.io.InputStream;
import java.sql.Array;
import java.sql.DatabaseMetaData;
import java.sql.Wrapper;
import com.alibaba.druid.wall.violation.SyntaxErrorViolation;
import com.alibaba.druid.VERSION;
import java.util.Iterator;
import java.util.Map;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.ArrayList;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.CallableStatementProxy;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import java.sql.SQLException;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.filter.FilterChain;
import java.util.Collections;
import java.util.Set;
import com.alibaba.druid.wall.spi.ClickhouseWallProvider;
import com.alibaba.druid.wall.spi.SQLiteWallProvider;
import com.alibaba.druid.wall.spi.DB2WallProvider;
import com.alibaba.druid.wall.spi.PGWallProvider;
import com.alibaba.druid.wall.spi.SQLServerWallProvider;
import com.alibaba.druid.wall.spi.OracleWallProvider;
import com.alibaba.druid.wall.spi.MySqlWallProvider;
import com.alibaba.druid.DbType;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.alibaba.druid.util.Utils;
import java.util.Properties;
import java.util.List;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.filter.FilterAdapter;

public class WallFilter extends FilterAdapter implements WallFilterMBean
{
    private static final Log LOG;
    private boolean inited;
    private WallProvider provider;
    private String dbTypeName;
    private WallConfig config;
    private volatile boolean logViolation;
    private volatile boolean throwException;
    public static final String ATTR_SQL_STAT = "wall.sqlStat";
    public static final String ATTR_UPDATE_CHECK_ITEMS = "wall.updateCheckItems";
    private static final ThreadLocal<List<Integer>> tenantColumnsLocal;
    
    public WallFilter() {
        this.inited = false;
        this.logViolation = false;
        this.throwException = true;
        this.configFromProperties(System.getProperties());
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
        Boolean value = Utils.getBoolean(properties, "druid.wall.logViolation");
        if (value != null) {
            this.logViolation = value;
        }
        value = Utils.getBoolean(properties, "druid.wall.throwException");
        if (value != null) {
            this.throwException = value;
        }
        if (this.config != null) {
            this.config.configFromProperties(properties);
        }
    }
    
    @Override
    public synchronized void init(final DataSourceProxy dataSource) {
        if (dataSource == null) {
            WallFilter.LOG.error("dataSource should not be null");
            return;
        }
        if (this.dbTypeName == null || this.dbTypeName.trim().length() == 0) {
            if (dataSource.getDbType() != null) {
                this.dbTypeName = dataSource.getDbType();
            }
            else {
                this.dbTypeName = JdbcUtils.getDbType(dataSource.getRawJdbcUrl(), "");
            }
        }
        if (this.dbTypeName == null) {
            this.dbTypeName = JdbcUtils.getDbType(dataSource.getUrl(), null);
        }
        final DbType dbType = DbType.of(this.dbTypeName);
        switch (dbType) {
            case mysql:
            case oceanbase:
            case drds:
            case mariadb:
            case h2:
            case presto:
            case trino: {
                if (this.config == null) {
                    this.config = new WallConfig("META-INF/druid/wall/mysql");
                }
                this.provider = new MySqlWallProvider(this.config);
                break;
            }
            case oracle:
            case ali_oracle:
            case oceanbase_oracle: {
                if (this.config == null) {
                    this.config = new WallConfig("META-INF/druid/wall/oracle");
                }
                this.provider = new OracleWallProvider(this.config);
                break;
            }
            case sqlserver:
            case jtds: {
                if (this.config == null) {
                    this.config = new WallConfig("META-INF/druid/wall/sqlserver");
                }
                this.provider = new SQLServerWallProvider(this.config);
                break;
            }
            case postgresql:
            case edb:
            case polardb:
            case greenplum:
            case gaussdb: {
                if (this.config == null) {
                    this.config = new WallConfig("META-INF/druid/wall/postgres");
                }
                this.provider = new PGWallProvider(this.config);
                break;
            }
            case db2: {
                if (this.config == null) {
                    this.config = new WallConfig("META-INF/druid/wall/db2");
                }
                this.provider = new DB2WallProvider(this.config);
                break;
            }
            case sqlite: {
                if (this.config == null) {
                    this.config = new WallConfig("META-INF/druid/wall/sqlite");
                }
                this.provider = new SQLiteWallProvider(this.config);
                break;
            }
            case clickhouse: {
                if (this.config == null) {
                    this.config = new WallConfig("META-INF/druid/wall/clickhouse");
                }
                this.provider = new ClickhouseWallProvider(this.config);
                break;
            }
            default: {
                throw new IllegalStateException("dbType not support : " + dbType + ", url " + dataSource.getUrl());
            }
        }
        this.provider.setName(dataSource.getName());
        this.inited = true;
    }
    
    @Override
    public String getDbType() {
        return this.dbTypeName;
    }
    
    public void setDbType(final String dbType) {
        this.dbTypeName = dbType;
    }
    
    public void setDbType(final DbType dbType) {
        if (dbType == null) {
            this.dbTypeName = null;
        }
        else {
            this.dbTypeName = dbType.name();
        }
    }
    
    @Override
    public boolean isLogViolation() {
        return this.logViolation;
    }
    
    @Override
    public void setLogViolation(final boolean logViolation) {
        this.logViolation = logViolation;
    }
    
    @Override
    public boolean isThrowException() {
        return this.throwException;
    }
    
    @Override
    public void setThrowException(final boolean throwException) {
        this.throwException = throwException;
    }
    
    @Override
    public void clearProviderCache() {
        if (this.provider != null) {
            this.provider.clearCache();
        }
    }
    
    @Override
    public Set<String> getProviderWhiteList() {
        if (this.provider == null) {
            return Collections.emptySet();
        }
        return this.provider.getWhiteList();
    }
    
    public WallProvider getProvider() {
        return this.provider;
    }
    
    public WallConfig getConfig() {
        return this.config;
    }
    
    public void setConfig(final WallConfig config) {
        this.config = config;
    }
    
    public void setTenantColumn(final String tenantColumn) {
        this.config.setTenantColumn(tenantColumn);
    }
    
    public String getTenantColumn() {
        return this.config.getTenantColumn();
    }
    
    @Override
    public boolean isInited() {
        return this.inited;
    }
    
    @Override
    public void statement_addBatch(final FilterChain chain, final StatementProxy statement, String sql) throws SQLException {
        this.createWallContext(statement);
        try {
            sql = this.check(sql);
            chain.statement_addBatch(statement, sql);
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public void preparedStatement_addBatch(final FilterChain chain, final PreparedStatementProxy statement) throws SQLException {
        chain.preparedStatement_addBatch(statement);
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, String sql) throws SQLException {
        final String dbType = connection.getDirectDataSource().getDbType();
        final WallContext context = WallContext.create(dbType);
        try {
            final WallCheckResult result = this.checkInternal(sql);
            context.setWallUpdateCheckItems(result.getUpdateCheckItems());
            sql = result.getSql();
            final PreparedStatementProxy stmt = chain.connection_prepareStatement(connection, sql);
            this.setSqlStatAttribute(stmt);
            return stmt;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, String sql, final int autoGeneratedKeys) throws SQLException {
        final String dbType = connection.getDirectDataSource().getDbType();
        final WallContext context = WallContext.create(dbType);
        try {
            final WallCheckResult result = this.checkInternal(sql);
            context.setWallUpdateCheckItems(result.getUpdateCheckItems());
            sql = result.getSql();
            final PreparedStatementProxy stmt = chain.connection_prepareStatement(connection, sql, autoGeneratedKeys);
            this.setSqlStatAttribute(stmt);
            return stmt;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        final String dbType = connection.getDirectDataSource().getDbType();
        final WallContext context = WallContext.create(dbType);
        try {
            final WallCheckResult result = this.checkInternal(sql);
            context.setWallUpdateCheckItems(result.getUpdateCheckItems());
            sql = result.getSql();
            final PreparedStatementProxy stmt = chain.connection_prepareStatement(connection, sql, resultSetType, resultSetConcurrency);
            this.setSqlStatAttribute(stmt);
            return stmt;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        final String dbType = connection.getDirectDataSource().getDbType();
        final WallContext context = WallContext.create(dbType);
        try {
            final WallCheckResult result = this.checkInternal(sql);
            context.setWallUpdateCheckItems(result.getUpdateCheckItems());
            sql = result.getSql();
            final PreparedStatementProxy stmt = chain.connection_prepareStatement(connection, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
            this.setSqlStatAttribute(stmt);
            return stmt;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, String sql, final int[] columnIndexes) throws SQLException {
        final String dbType = connection.getDirectDataSource().getDbType();
        final WallContext context = WallContext.create(dbType);
        try {
            final WallCheckResult result = this.checkInternal(sql);
            context.setWallUpdateCheckItems(result.getUpdateCheckItems());
            sql = result.getSql();
            final PreparedStatementProxy stmt = chain.connection_prepareStatement(connection, sql, columnIndexes);
            this.setSqlStatAttribute(stmt);
            return stmt;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public PreparedStatementProxy connection_prepareStatement(final FilterChain chain, final ConnectionProxy connection, String sql, final String[] columnNames) throws SQLException {
        final String dbType = connection.getDirectDataSource().getDbType();
        final WallContext context = WallContext.create(dbType);
        try {
            final WallCheckResult result = this.checkInternal(sql);
            context.setWallUpdateCheckItems(result.getUpdateCheckItems());
            sql = result.getSql();
            final PreparedStatementProxy stmt = chain.connection_prepareStatement(connection, sql, columnNames);
            this.setSqlStatAttribute(stmt);
            return stmt;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public CallableStatementProxy connection_prepareCall(final FilterChain chain, final ConnectionProxy connection, String sql) throws SQLException {
        final String dbType = connection.getDirectDataSource().getDbType();
        final WallContext context = WallContext.create(dbType);
        try {
            final WallCheckResult result = this.checkInternal(sql);
            context.setWallUpdateCheckItems(result.getUpdateCheckItems());
            sql = result.getSql();
            final CallableStatementProxy stmt = chain.connection_prepareCall(connection, sql);
            this.setSqlStatAttribute(stmt);
            return stmt;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public CallableStatementProxy connection_prepareCall(final FilterChain chain, final ConnectionProxy connection, String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        final String dbType = connection.getDirectDataSource().getDbType();
        final WallContext context = WallContext.create(dbType);
        try {
            final WallCheckResult result = this.checkInternal(sql);
            context.setWallUpdateCheckItems(result.getUpdateCheckItems());
            sql = result.getSql();
            final CallableStatementProxy stmt = chain.connection_prepareCall(connection, sql, resultSetType, resultSetConcurrency);
            this.setSqlStatAttribute(stmt);
            return stmt;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public CallableStatementProxy connection_prepareCall(final FilterChain chain, final ConnectionProxy connection, String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        final String dbType = connection.getDirectDataSource().getDbType();
        final WallContext context = WallContext.create(dbType);
        try {
            final WallCheckResult result = this.checkInternal(sql);
            context.setWallUpdateCheckItems(result.getUpdateCheckItems());
            sql = result.getSql();
            final CallableStatementProxy stmt = chain.connection_prepareCall(connection, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
            this.setSqlStatAttribute(stmt);
            return stmt;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public boolean statement_execute(final FilterChain chain, final StatementProxy statement, String sql) throws SQLException {
        final WallContext originalContext = WallContext.current();
        try {
            this.createWallContext(statement);
            sql = this.check(sql);
            final boolean firstResult = chain.statement_execute(statement, sql);
            if (!firstResult) {
                final int updateCount = statement.getUpdateCount();
                this.statExecuteUpdate(updateCount);
            }
            else {
                this.setSqlStatAttribute(statement);
            }
            return firstResult;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount();
            throw ex;
        }
        finally {
            if (originalContext != null) {
                WallContext.setContext(originalContext);
            }
        }
    }
    
    @Override
    public boolean statement_execute(final FilterChain chain, final StatementProxy statement, String sql, final int autoGeneratedKeys) throws SQLException {
        this.createWallContext(statement);
        try {
            sql = this.check(sql);
            final boolean firstResult = chain.statement_execute(statement, sql, autoGeneratedKeys);
            if (!firstResult) {
                final int updateCount = statement.getUpdateCount();
                this.statExecuteUpdate(updateCount);
            }
            else {
                this.setSqlStatAttribute(statement);
            }
            return firstResult;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount();
            throw ex;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public boolean statement_execute(final FilterChain chain, final StatementProxy statement, String sql, final int[] columnIndexes) throws SQLException {
        this.createWallContext(statement);
        try {
            sql = this.check(sql);
            final boolean firstResult = chain.statement_execute(statement, sql, columnIndexes);
            if (!firstResult) {
                final int updateCount = statement.getUpdateCount();
                this.statExecuteUpdate(updateCount);
            }
            else {
                this.setSqlStatAttribute(statement);
            }
            return firstResult;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount();
            throw ex;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public boolean statement_execute(final FilterChain chain, final StatementProxy statement, String sql, final String[] columnNames) throws SQLException {
        this.createWallContext(statement);
        try {
            sql = this.check(sql);
            final boolean firstResult = chain.statement_execute(statement, sql, columnNames);
            if (!firstResult) {
                final int updateCount = statement.getUpdateCount();
                this.statExecuteUpdate(updateCount);
            }
            else {
                this.setSqlStatAttribute(statement);
            }
            return firstResult;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount();
            throw ex;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public int[] statement_executeBatch(final FilterChain chain, final StatementProxy statement) throws SQLException {
        final WallSqlStat sqlStat = (WallSqlStat)statement.getAttribute("wall.sqlStat");
        try {
            final int[] updateCounts = chain.statement_executeBatch(statement);
            int updateCount = 0;
            for (final int count : updateCounts) {
                updateCount += count;
            }
            if (sqlStat != null) {
                this.provider.addUpdateCount(sqlStat, updateCount);
            }
            return updateCounts;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount();
            throw ex;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public ResultSetProxy statement_executeQuery(final FilterChain chain, final StatementProxy statement, String sql) throws SQLException {
        this.createWallContext(statement);
        try {
            sql = this.check(sql);
            final ResultSetProxy resultSetProxy = chain.statement_executeQuery(statement, sql);
            this.preprocessResultSet(resultSetProxy);
            return resultSetProxy;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount();
            throw ex;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public int statement_executeUpdate(final FilterChain chain, final StatementProxy statement, String sql) throws SQLException {
        this.createWallContext(statement);
        try {
            sql = this.check(sql);
            final int updateCount = chain.statement_executeUpdate(statement, sql);
            this.statExecuteUpdate(updateCount);
            return updateCount;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount();
            throw ex;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public int statement_executeUpdate(final FilterChain chain, final StatementProxy statement, String sql, final int autoGeneratedKeys) throws SQLException {
        this.createWallContext(statement);
        try {
            sql = this.check(sql);
            final int updateCount = chain.statement_executeUpdate(statement, sql, autoGeneratedKeys);
            this.statExecuteUpdate(updateCount);
            return updateCount;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount();
            throw ex;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public int statement_executeUpdate(final FilterChain chain, final StatementProxy statement, String sql, final int[] columnIndexes) throws SQLException {
        this.createWallContext(statement);
        try {
            sql = this.check(sql);
            final int updateCount = chain.statement_executeUpdate(statement, sql, columnIndexes);
            this.statExecuteUpdate(updateCount);
            return updateCount;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount();
            throw ex;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    public String getDbType(final StatementProxy statement) {
        return statement.getConnectionProxy().getDirectDataSource().getDbType();
    }
    
    private WallContext createWallContext(final StatementProxy statement) {
        final String dbType = this.getDbType(statement);
        return WallContext.create(dbType);
    }
    
    @Override
    public int statement_executeUpdate(final FilterChain chain, final StatementProxy statement, String sql, final String[] columnNames) throws SQLException {
        this.createWallContext(statement);
        try {
            sql = this.check(sql);
            final int updateCount = chain.statement_executeUpdate(statement, sql, columnNames);
            this.statExecuteUpdate(updateCount);
            return updateCount;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount();
            throw ex;
        }
        finally {
            WallContext.clearContext();
        }
    }
    
    @Override
    public boolean preparedStatement_execute(final FilterChain chain, final PreparedStatementProxy statement) throws SQLException {
        try {
            this.wallUpdateCheck(statement);
            final boolean firstResult = chain.preparedStatement_execute(statement);
            if (!firstResult) {
                final WallSqlStat sqlStat = (WallSqlStat)statement.getAttribute("wall.sqlStat");
                final int updateCount = statement.getUpdateCount();
                if (sqlStat != null) {
                    this.provider.addUpdateCount(sqlStat, updateCount);
                }
            }
            return firstResult;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount(statement);
            throw ex;
        }
    }
    
    @Override
    public ResultSetProxy preparedStatement_executeQuery(final FilterChain chain, final PreparedStatementProxy statement) throws SQLException {
        try {
            final ResultSetProxy resultSetProxy = chain.preparedStatement_executeQuery(statement);
            this.preprocessResultSet(resultSetProxy);
            return resultSetProxy;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount(statement);
            throw ex;
        }
    }
    
    @Override
    public int preparedStatement_executeUpdate(final FilterChain chain, final PreparedStatementProxy statement) throws SQLException {
        try {
            this.wallUpdateCheck(statement);
            final int updateCount = chain.preparedStatement_executeUpdate(statement);
            final WallSqlStat sqlStat = (WallSqlStat)statement.getAttribute("wall.sqlStat");
            if (sqlStat != null) {
                this.provider.addUpdateCount(sqlStat, updateCount);
            }
            return updateCount;
        }
        catch (SQLException ex) {
            this.incrementExecuteErrorCount(statement);
            throw ex;
        }
    }
    
    private void wallUpdateCheck(final PreparedStatementProxy statement) throws SQLException {
        final Map<Integer, JdbcParameter> parameterMap = statement.getParameters();
        final List<WallUpdateCheckItem> wallUpdateCheckItems = (List<WallUpdateCheckItem>)statement.getAttribute("wall.updateCheckItems");
        if (wallUpdateCheckItems != null) {
            for (final WallUpdateCheckItem item : wallUpdateCheckItems) {
                Object setValue;
                if (item.value instanceof SQLValuableExpr) {
                    setValue = ((SQLValuableExpr)item.value).getValue();
                }
                else {
                    final int index = ((SQLVariantRefExpr)item.value).getIndex();
                    final JdbcParameter parameter = parameterMap.get(index);
                    if (parameter != null) {
                        setValue = parameter.getValue();
                    }
                    else {
                        setValue = null;
                    }
                }
                final List<Object> filtersValues = new ArrayList<Object>(item.filterValues.size());
                for (final SQLExpr filterValueExpr : item.filterValues) {
                    Object filterValue;
                    if (filterValueExpr instanceof SQLValuableExpr) {
                        filterValue = ((SQLValuableExpr)filterValueExpr).getValue();
                    }
                    else {
                        final int index2 = ((SQLVariantRefExpr)filterValueExpr).getIndex();
                        final JdbcParameter parameter2 = parameterMap.get(index2);
                        if (parameter2 != null) {
                            filterValue = parameter2.getValue();
                        }
                        else {
                            filterValue = null;
                        }
                    }
                    filtersValues.add(filterValue);
                }
                final boolean valid = this.config.updateCheckHandler.check(item.tableName, item.columnName, setValue, filtersValues);
                if (!valid) {
                    throw new SQLException("wall update check failed.");
                }
            }
        }
    }
    
    @Override
    public ResultSetProxy statement_getResultSet(final FilterChain chain, final StatementProxy statement) throws SQLException {
        final ResultSetProxy resultSetProxy = chain.statement_getResultSet(statement);
        this.preprocessResultSet(resultSetProxy);
        return resultSetProxy;
    }
    
    @Override
    public ResultSetProxy statement_getGeneratedKeys(final FilterChain chain, final StatementProxy statement) throws SQLException {
        final ResultSetProxy resultSetProxy = chain.statement_getGeneratedKeys(statement);
        this.preprocessResultSet(resultSetProxy);
        return resultSetProxy;
    }
    
    public void setSqlStatAttribute(final StatementProxy stmt) {
        final WallContext context = WallContext.current();
        if (context == null) {
            return;
        }
        final WallSqlStat sqlStat = context.getSqlStat();
        if (sqlStat == null) {
            return;
        }
        stmt.putAttribute("wall.sqlStat", sqlStat);
        final List<WallUpdateCheckItem> wallUpdateCheckItems = context.getWallUpdateCheckItems();
        if (wallUpdateCheckItems != null) {
            stmt.putAttribute("wall.updateCheckItems", wallUpdateCheckItems);
        }
    }
    
    public void statExecuteUpdate(final int updateCount) {
        final WallContext context = WallContext.current();
        if (context == null) {
            return;
        }
        final WallSqlStat sqlStat = context.getSqlStat();
        if (sqlStat == null) {
            return;
        }
        if (updateCount > 0) {
            this.provider.addUpdateCount(sqlStat, updateCount);
        }
    }
    
    public void incrementExecuteErrorCount(final PreparedStatementProxy statement) {
        final WallSqlStat sqlStat = (WallSqlStat)statement.getAttribute("wall.sqlStat");
        if (sqlStat != null) {
            sqlStat.incrementAndGetExecuteErrorCount();
        }
    }
    
    public void incrementExecuteErrorCount() {
        final WallContext context = WallContext.current();
        if (context == null) {
            return;
        }
        final WallSqlStat sqlStat = context.getSqlStat();
        if (sqlStat == null) {
            return;
        }
        sqlStat.incrementAndGetExecuteErrorCount();
    }
    
    @Override
    public String check(final String sql) throws SQLException {
        return this.checkInternal(sql).getSql();
    }
    
    private WallCheckResult checkInternal(final String sql) throws SQLException {
        final WallCheckResult checkResult = this.provider.check(sql);
        final List<Violation> violations = checkResult.getViolations();
        if (violations.size() > 0) {
            final Violation firstViolation = violations.get(0);
            if (this.isLogViolation()) {
                WallFilter.LOG.error("sql injection violation, dbType " + this.getDbType() + ", druid-version " + VERSION.getVersionNumber() + ", " + firstViolation.getMessage() + " : " + sql);
            }
            if (this.throwException) {
                if (violations.get(0) instanceof SyntaxErrorViolation) {
                    final SyntaxErrorViolation violation = violations.get(0);
                    throw new SQLException("sql injection violation, dbType " + this.getDbType() + ", , druid-version " + VERSION.getVersionNumber() + ", " + firstViolation.getMessage() + " : " + sql, violation.getException());
                }
                throw new SQLException("sql injection violation, dbType " + this.getDbType() + ", druid-version " + VERSION.getVersionNumber() + ", " + firstViolation.getMessage() + " : " + sql);
            }
        }
        return checkResult;
    }
    
    @Override
    public boolean isWrapperFor(final FilterChain chain, final Wrapper wrapper, final Class<?> iface) throws SQLException {
        if (this.config.isDoPrivilegedAllow() && WallProvider.ispPrivileged()) {
            return chain.isWrapperFor(wrapper, iface);
        }
        return this.provider.getConfig().isWrapAllow() && chain.isWrapperFor(wrapper, iface);
    }
    
    @Override
    public <T> T unwrap(final FilterChain chain, final Wrapper wrapper, final Class<T> iface) throws SQLException {
        if (this.config.isDoPrivilegedAllow() && WallProvider.ispPrivileged()) {
            return chain.unwrap(wrapper, iface);
        }
        if (!this.provider.getConfig().isWrapAllow()) {
            return null;
        }
        return chain.unwrap(wrapper, iface);
    }
    
    @Override
    public DatabaseMetaData connection_getMetaData(final FilterChain chain, final ConnectionProxy connection) throws SQLException {
        if (this.config.isDoPrivilegedAllow() && WallProvider.ispPrivileged()) {
            return chain.connection_getMetaData(connection);
        }
        if (!this.provider.getConfig().isMetadataAllow()) {
            if (this.isLogViolation()) {
                WallFilter.LOG.error("not support method : Connection.getMetaData");
            }
            if (this.throwException) {
                throw new WallSQLException("not support method : Connection.getMetaData");
            }
        }
        return chain.connection_getMetaData(connection);
    }
    
    @Override
    public void resultSet_close(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        chain.resultSet_close(resultSet);
        final int fetchRowCount = resultSet.getFetchRowCount();
        final WallSqlStat sqlStat = (WallSqlStat)resultSet.getStatementProxy().getAttribute("wall.sqlStat");
        if (sqlStat == null) {
            return;
        }
        this.provider.addFetchRowCount(sqlStat, fetchRowCount);
    }
    
    @Override
    public int resultSet_findColumn(final FilterChain chain, final ResultSetProxy resultSet, final String columnLabel) throws SQLException {
        final int physicalColumn = chain.resultSet_findColumn(resultSet, columnLabel);
        return resultSet.getLogicColumn(physicalColumn);
    }
    
    @Override
    public Array resultSet_getArray(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getArray(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public InputStream resultSet_getAsciiStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getAsciiStream(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public BigDecimal resultSet_getBigDecimal(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getBigDecimal(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public BigDecimal resultSet_getBigDecimal(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final int scale) throws SQLException {
        return chain.resultSet_getBigDecimal(resultSet, resultSet.getPhysicalColumn(columnIndex), scale);
    }
    
    @Override
    public InputStream resultSet_getBinaryStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getBinaryStream(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public Blob resultSet_getBlob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getBlob(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public boolean resultSet_getBoolean(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getBoolean(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public byte resultSet_getByte(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getByte(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public byte[] resultSet_getBytes(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getBytes(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public Reader resultSet_getCharacterStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getCharacterStream(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public Clob resultSet_getClob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getClob(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public Date resultSet_getDate(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getDate(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public Date resultSet_getDate(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Calendar cal) throws SQLException {
        return chain.resultSet_getDate(resultSet, resultSet.getPhysicalColumn(columnIndex), cal);
    }
    
    @Override
    public double resultSet_getDouble(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getDouble(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public float resultSet_getFloat(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getFloat(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public int resultSet_getInt(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getInt(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public long resultSet_getLong(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getLong(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public Reader resultSet_getNCharacterStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getNCharacterStream(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public NClob resultSet_getNClob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getNClob(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public String resultSet_getNString(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getNString(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getObject(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public <T> T resultSet_getObject(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Class<T> type) throws SQLException {
        return chain.resultSet_getObject(resultSet, resultSet.getPhysicalColumn(columnIndex), type);
    }
    
    @Override
    public Object resultSet_getObject(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
        return chain.resultSet_getObject(resultSet, resultSet.getPhysicalColumn(columnIndex), map);
    }
    
    @Override
    public Ref resultSet_getRef(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getRef(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public RowId resultSet_getRowId(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getRowId(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public SQLXML resultSet_getSQLXML(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getSQLXML(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public short resultSet_getShort(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getShort(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public String resultSet_getString(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getString(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public Time resultSet_getTime(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getTime(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public Time resultSet_getTime(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Calendar cal) throws SQLException {
        return chain.resultSet_getTime(resultSet, resultSet.getPhysicalColumn(columnIndex), cal);
    }
    
    @Override
    public Timestamp resultSet_getTimestamp(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getTimestamp(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public Timestamp resultSet_getTimestamp(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Calendar cal) throws SQLException {
        return chain.resultSet_getTimestamp(resultSet, resultSet.getPhysicalColumn(columnIndex), cal);
    }
    
    @Override
    public URL resultSet_getURL(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getURL(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public InputStream resultSet_getUnicodeStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        return chain.resultSet_getUnicodeStream(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public void resultSet_updateArray(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Array x) throws SQLException {
        chain.resultSet_updateArray(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final InputStream x) throws SQLException {
        chain.resultSet_updateAsciiStream(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final InputStream x, final int length) throws SQLException {
        chain.resultSet_updateAsciiStream(resultSet, resultSet.getPhysicalColumn(columnIndex), x, length);
    }
    
    @Override
    public void resultSet_updateAsciiStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final InputStream x, final long length) throws SQLException {
        chain.resultSet_updateAsciiStream(resultSet, resultSet.getPhysicalColumn(columnIndex), x, length);
    }
    
    @Override
    public void resultSet_updateBigDecimal(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final BigDecimal x) throws SQLException {
        chain.resultSet_updateBigDecimal(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final InputStream x) throws SQLException {
        chain.resultSet_updateBinaryStream(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final InputStream x, final int length) throws SQLException {
        chain.resultSet_updateBinaryStream(resultSet, resultSet.getPhysicalColumn(columnIndex), x, length);
    }
    
    @Override
    public void resultSet_updateBinaryStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final InputStream x, final long length) throws SQLException {
        chain.resultSet_updateBinaryStream(resultSet, resultSet.getPhysicalColumn(columnIndex), x, length);
    }
    
    @Override
    public void resultSet_updateBlob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final InputStream inputStream) throws SQLException {
        chain.resultSet_updateBlob(resultSet, resultSet.getPhysicalColumn(columnIndex), inputStream);
    }
    
    @Override
    public void resultSet_updateBlob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final InputStream inputStream, final long length) throws SQLException {
        chain.resultSet_updateBlob(resultSet, resultSet.getPhysicalColumn(columnIndex), inputStream, length);
    }
    
    @Override
    public void resultSet_updateBlob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Blob x) throws SQLException {
        chain.resultSet_updateBlob(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateBoolean(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final boolean x) throws SQLException {
        chain.resultSet_updateBoolean(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateByte(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final byte x) throws SQLException {
        chain.resultSet_updateByte(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateBytes(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final byte[] x) throws SQLException {
        chain.resultSet_updateBytes(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Reader x) throws SQLException {
        chain.resultSet_updateCharacterStream(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Reader x, final int length) throws SQLException {
        chain.resultSet_updateCharacterStream(resultSet, resultSet.getPhysicalColumn(columnIndex), x, length);
    }
    
    @Override
    public void resultSet_updateCharacterStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Reader x, final long length) throws SQLException {
        chain.resultSet_updateCharacterStream(resultSet, resultSet.getPhysicalColumn(columnIndex), x, length);
    }
    
    @Override
    public void resultSet_updateClob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Clob x) throws SQLException {
        chain.resultSet_updateClob(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateClob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Reader reader) throws SQLException {
        chain.resultSet_updateClob(resultSet, resultSet.getPhysicalColumn(columnIndex), reader);
    }
    
    @Override
    public void resultSet_updateClob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Reader reader, final long length) throws SQLException {
        chain.resultSet_updateClob(resultSet, resultSet.getPhysicalColumn(columnIndex), reader, length);
    }
    
    @Override
    public void resultSet_updateDate(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Date x) throws SQLException {
        chain.resultSet_updateDate(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateDouble(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final double x) throws SQLException {
        chain.resultSet_updateDouble(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateFloat(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final float x) throws SQLException {
        chain.resultSet_updateFloat(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateInt(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final int x) throws SQLException {
        chain.resultSet_updateInt(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateLong(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final long x) throws SQLException {
        chain.resultSet_updateLong(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateNCharacterStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Reader x) throws SQLException {
        chain.resultSet_updateNCharacterStream(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateNCharacterStream(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Reader x, final long length) throws SQLException {
        chain.resultSet_updateNCharacterStream(resultSet, resultSet.getPhysicalColumn(columnIndex), x, length);
    }
    
    @Override
    public void resultSet_updateNClob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final NClob nClob) throws SQLException {
        chain.resultSet_updateNClob(resultSet, resultSet.getPhysicalColumn(columnIndex), nClob);
    }
    
    @Override
    public void resultSet_updateNClob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Reader reader) throws SQLException {
        chain.resultSet_updateNClob(resultSet, resultSet.getPhysicalColumn(columnIndex), reader);
    }
    
    @Override
    public void resultSet_updateNClob(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Reader reader, final long length) throws SQLException {
        chain.resultSet_updateNClob(resultSet, resultSet.getPhysicalColumn(columnIndex), reader, length);
    }
    
    @Override
    public void resultSet_updateNString(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final String nString) throws SQLException {
        chain.resultSet_updateNString(resultSet, resultSet.getPhysicalColumn(columnIndex), nString);
    }
    
    @Override
    public void resultSet_updateNull(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex) throws SQLException {
        chain.resultSet_updateNull(resultSet, resultSet.getPhysicalColumn(columnIndex));
    }
    
    @Override
    public void resultSet_updateObject(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Object x) throws SQLException {
        chain.resultSet_updateObject(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateObject(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
        chain.resultSet_updateObject(resultSet, resultSet.getPhysicalColumn(columnIndex), x, scaleOrLength);
    }
    
    @Override
    public void resultSet_updateRef(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Ref x) throws SQLException {
        chain.resultSet_updateRef(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateRowId(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final RowId x) throws SQLException {
        chain.resultSet_updateRowId(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateShort(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final short x) throws SQLException {
        chain.resultSet_updateShort(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateSQLXML(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final SQLXML xmlObject) throws SQLException {
        chain.resultSet_updateSQLXML(resultSet, resultSet.getPhysicalColumn(columnIndex), xmlObject);
    }
    
    @Override
    public void resultSet_updateString(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final String x) throws SQLException {
        chain.resultSet_updateString(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateTime(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Time x) throws SQLException {
        chain.resultSet_updateTime(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public void resultSet_updateTimestamp(final FilterChain chain, final ResultSetProxy resultSet, final int columnIndex, final Timestamp x) throws SQLException {
        chain.resultSet_updateTimestamp(resultSet, resultSet.getPhysicalColumn(columnIndex), x);
    }
    
    @Override
    public boolean resultSet_next(final FilterChain chain, final ResultSetProxy resultSet) throws SQLException {
        final boolean hasNext = chain.resultSet_next(resultSet);
        final WallConfig.TenantCallBack callback = this.provider.getConfig().getTenantCallBack();
        if (callback != null && hasNext) {
            final List<Integer> tenantColumns = WallFilter.tenantColumnsLocal.get();
            if (tenantColumns != null && tenantColumns.size() > 0) {
                for (final Integer columnIndex : tenantColumns) {
                    final Object value = resultSet.getResultSetRaw().getObject(columnIndex);
                    callback.filterResultsetTenantColumn(value);
                }
            }
        }
        return hasNext;
    }
    
    @Override
    public int resultSetMetaData_getColumnCount(final FilterChain chain, final ResultSetMetaDataProxy metaData) throws SQLException {
        final int count = chain.resultSetMetaData_getColumnCount(metaData);
        return count - metaData.getResultSetProxy().getHiddenColumnCount();
    }
    
    @Override
    public boolean resultSetMetaData_isAutoIncrement(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isAutoIncrement(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public boolean resultSetMetaData_isCaseSensitive(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isCaseSensitive(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public boolean resultSetMetaData_isSearchable(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isSearchable(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public boolean resultSetMetaData_isCurrency(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isCurrency(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public int resultSetMetaData_isNullable(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isNullable(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public boolean resultSetMetaData_isSigned(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isSigned(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public int resultSetMetaData_getColumnDisplaySize(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getColumnDisplaySize(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public String resultSetMetaData_getColumnLabel(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getColumnLabel(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public String resultSetMetaData_getColumnName(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getColumnName(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public String resultSetMetaData_getSchemaName(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getSchemaName(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public int resultSetMetaData_getPrecision(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getPrecision(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public int resultSetMetaData_getScale(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getScale(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public String resultSetMetaData_getTableName(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getTableName(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public String resultSetMetaData_getCatalogName(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getCatalogName(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public int resultSetMetaData_getColumnType(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getColumnType(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public String resultSetMetaData_getColumnTypeName(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getColumnTypeName(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public boolean resultSetMetaData_isReadOnly(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isReadOnly(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public boolean resultSetMetaData_isWritable(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isWritable(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public boolean resultSetMetaData_isDefinitelyWritable(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_isDefinitelyWritable(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public String resultSetMetaData_getColumnClassName(final FilterChain chain, final ResultSetMetaDataProxy metaData, final int column) throws SQLException {
        return chain.resultSetMetaData_getColumnClassName(metaData, metaData.getResultSetProxy().getPhysicalColumn(column));
    }
    
    @Override
    public long getViolationCount() {
        return this.provider.getViolationCount();
    }
    
    @Override
    public void resetViolationCount() {
        this.provider.reset();
    }
    
    @Override
    public void clearWhiteList() {
        this.provider.clearCache();
    }
    
    @Override
    public boolean checkValid(final String sql) {
        return this.provider.checkValid(sql);
    }
    
    private void preprocessResultSet(final ResultSetProxy resultSet) throws SQLException {
        if (resultSet == null) {
            return;
        }
        final ResultSetMetaData metaData = resultSet.getResultSetRaw().getMetaData();
        if (metaData == null) {
            return;
        }
        final WallConfig.TenantCallBack tenantCallBack = this.provider.getConfig().getTenantCallBack();
        final String tenantTablePattern = this.provider.getConfig().getTenantTablePattern();
        if (tenantCallBack == null && (tenantTablePattern == null || tenantTablePattern.length() == 0)) {
            return;
        }
        final Map<Integer, Integer> logicColumnMap = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> physicalColumnMap = new HashMap<Integer, Integer>();
        final List<Integer> hiddenColumns = new ArrayList<Integer>();
        final List<Integer> tenantColumns = new ArrayList<Integer>();
        int physicalColumn = 1;
        int logicColumn = 1;
        while (physicalColumn <= metaData.getColumnCount()) {
            boolean isHidden = false;
            final String tableName = metaData.getTableName(physicalColumn);
            String hiddenColumn = null;
            String tenantColumn = null;
            if (tenantCallBack != null) {
                tenantColumn = tenantCallBack.getTenantColumn(WallConfig.TenantCallBack.StatementType.SELECT, tableName);
                hiddenColumn = tenantCallBack.getHiddenColumn(tableName);
            }
            if ((StringUtils.isEmpty(hiddenColumn) || StringUtils.isEmpty(tenantColumn)) && (tableName == null || ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName))) {
                if (StringUtils.isEmpty(hiddenColumn)) {
                    hiddenColumn = this.provider.getConfig().getTenantColumn();
                }
                if (StringUtils.isEmpty(tenantColumn)) {
                    tenantColumn = this.provider.getConfig().getTenantColumn();
                }
            }
            if (!StringUtils.isEmpty(hiddenColumn)) {
                final String columnName = metaData.getColumnName(physicalColumn);
                if (null != hiddenColumn && hiddenColumn.equalsIgnoreCase(columnName)) {
                    hiddenColumns.add(physicalColumn);
                    isHidden = true;
                }
            }
            if (!isHidden) {
                logicColumnMap.put(logicColumn, physicalColumn);
                physicalColumnMap.put(physicalColumn, logicColumn);
                ++logicColumn;
            }
            if (!StringUtils.isEmpty(tenantColumn) && null != tenantColumn && tenantColumn.equalsIgnoreCase(metaData.getColumnName(physicalColumn))) {
                tenantColumns.add(physicalColumn);
            }
            ++physicalColumn;
        }
        if (hiddenColumns.size() > 0) {
            resultSet.setLogicColumnMap(logicColumnMap);
            resultSet.setPhysicalColumnMap(physicalColumnMap);
            resultSet.setHiddenColumns(hiddenColumns);
        }
        WallFilter.tenantColumnsLocal.set(tenantColumns);
    }
    
    static {
        LOG = LogFactory.getLog(WallFilter.class);
        tenantColumnsLocal = new ThreadLocal<List<Integer>>();
    }
}
