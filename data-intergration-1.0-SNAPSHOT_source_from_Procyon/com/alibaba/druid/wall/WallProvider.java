// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.util.concurrent.locks.Lock;
import com.alibaba.druid.util.Utils;
import com.alibaba.druid.util.JdbcSqlStatUtils;
import java.security.PrivilegedAction;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlHintStatement;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.wall.violation.SyntaxErrorViolation;
import com.alibaba.druid.sql.parser.NotAllowCommentException;
import com.alibaba.druid.wall.violation.IllegalSQLObjectViolation;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.ArrayList;
import com.alibaba.druid.wall.spi.WallVisitorUtils;
import com.alibaba.druid.sql.visitor.ExportParameterVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import com.alibaba.druid.DbType;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.alibaba.druid.util.LRUCache;
import java.util.Map;

public abstract class WallProvider
{
    private String name;
    private final Map<String, Object> attributes;
    private boolean whiteListEnable;
    private LRUCache<String, WallSqlStat> whiteList;
    private int MAX_SQL_LENGTH;
    private int whiteSqlMaxSize;
    private boolean blackListEnable;
    private LRUCache<String, WallSqlStat> blackList;
    private LRUCache<String, WallSqlStat> blackMergedList;
    private int blackSqlMaxSize;
    protected final WallConfig config;
    private final ReentrantReadWriteLock lock;
    private static final ThreadLocal<Boolean> privileged;
    private final ConcurrentMap<String, WallFunctionStat> functionStats;
    private final ConcurrentMap<String, WallTableStat> tableStats;
    public final WallDenyStat commentDeniedStat;
    protected DbType dbType;
    protected final AtomicLong checkCount;
    protected final AtomicLong hardCheckCount;
    protected final AtomicLong whiteListHitCount;
    protected final AtomicLong blackListHitCount;
    protected final AtomicLong syntaxErrorCount;
    protected final AtomicLong violationCount;
    protected final AtomicLong violationEffectRowCount;
    private static final ThreadLocal<Object> tenantValueLocal;
    
    public WallProvider(final WallConfig config) {
        this.attributes = new ConcurrentHashMap<String, Object>(1, 0.75f, 1);
        this.whiteListEnable = true;
        this.MAX_SQL_LENGTH = 8192;
        this.whiteSqlMaxSize = 1000;
        this.blackListEnable = true;
        this.blackSqlMaxSize = 200;
        this.lock = new ReentrantReadWriteLock();
        this.functionStats = new ConcurrentHashMap<String, WallFunctionStat>(16, 0.75f, 1);
        this.tableStats = new ConcurrentHashMap<String, WallTableStat>(16, 0.75f, 1);
        this.commentDeniedStat = new WallDenyStat();
        this.dbType = null;
        this.checkCount = new AtomicLong();
        this.hardCheckCount = new AtomicLong();
        this.whiteListHitCount = new AtomicLong();
        this.blackListHitCount = new AtomicLong();
        this.syntaxErrorCount = new AtomicLong();
        this.violationCount = new AtomicLong();
        this.violationEffectRowCount = new AtomicLong();
        this.config = config;
    }
    
    public WallProvider(final WallConfig config, final String dbType) {
        this(config, DbType.of(dbType));
    }
    
    public WallProvider(final WallConfig config, final DbType dbType) {
        this.attributes = new ConcurrentHashMap<String, Object>(1, 0.75f, 1);
        this.whiteListEnable = true;
        this.MAX_SQL_LENGTH = 8192;
        this.whiteSqlMaxSize = 1000;
        this.blackListEnable = true;
        this.blackSqlMaxSize = 200;
        this.lock = new ReentrantReadWriteLock();
        this.functionStats = new ConcurrentHashMap<String, WallFunctionStat>(16, 0.75f, 1);
        this.tableStats = new ConcurrentHashMap<String, WallTableStat>(16, 0.75f, 1);
        this.commentDeniedStat = new WallDenyStat();
        this.dbType = null;
        this.checkCount = new AtomicLong();
        this.hardCheckCount = new AtomicLong();
        this.whiteListHitCount = new AtomicLong();
        this.blackListHitCount = new AtomicLong();
        this.syntaxErrorCount = new AtomicLong();
        this.violationCount = new AtomicLong();
        this.violationEffectRowCount = new AtomicLong();
        this.config = config;
        this.dbType = dbType;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
    
    public void reset() {
        this.checkCount.set(0L);
        this.hardCheckCount.set(0L);
        this.violationCount.set(0L);
        this.whiteListHitCount.set(0L);
        this.blackListHitCount.set(0L);
        this.clearWhiteList();
        this.clearBlackList();
        this.functionStats.clear();
        this.tableStats.clear();
    }
    
    public ConcurrentMap<String, WallTableStat> getTableStats() {
        return this.tableStats;
    }
    
    public ConcurrentMap<String, WallFunctionStat> getFunctionStats() {
        return this.functionStats;
    }
    
    public WallSqlStat getSqlStat(final String sql) {
        WallSqlStat sqlStat = this.getWhiteSql(sql);
        if (sqlStat == null) {
            sqlStat = this.getBlackSql(sql);
        }
        return sqlStat;
    }
    
    public WallTableStat getTableStat(final String tableName) {
        String lowerCaseName = tableName.toLowerCase();
        if (lowerCaseName.startsWith("`") && lowerCaseName.endsWith("`")) {
            lowerCaseName = lowerCaseName.substring(1, lowerCaseName.length() - 1);
        }
        return this.getTableStatWithLowerName(lowerCaseName);
    }
    
    public void addUpdateCount(final WallSqlStat sqlStat, final long updateCount) {
        sqlStat.addUpdateCount(updateCount);
        final Map<String, WallSqlTableStat> sqlTableStats = sqlStat.getTableStats();
        if (sqlTableStats == null) {
            return;
        }
        for (final Map.Entry<String, WallSqlTableStat> entry : sqlTableStats.entrySet()) {
            final String tableName = entry.getKey();
            final WallTableStat tableStat = this.getTableStat(tableName);
            if (tableStat == null) {
                continue;
            }
            final WallSqlTableStat sqlTableStat = entry.getValue();
            if (sqlTableStat.getDeleteCount() > 0) {
                tableStat.addDeleteDataCount(updateCount);
            }
            else if (sqlTableStat.getUpdateCount() > 0) {
                tableStat.addUpdateDataCount(updateCount);
            }
            else {
                if (sqlTableStat.getInsertCount() <= 0) {
                    continue;
                }
                tableStat.addInsertDataCount(updateCount);
            }
        }
    }
    
    public void addFetchRowCount(final WallSqlStat sqlStat, final long fetchRowCount) {
        sqlStat.addAndFetchRowCount(fetchRowCount);
        final Map<String, WallSqlTableStat> sqlTableStats = sqlStat.getTableStats();
        if (sqlTableStats == null) {
            return;
        }
        for (final Map.Entry<String, WallSqlTableStat> entry : sqlTableStats.entrySet()) {
            final String tableName = entry.getKey();
            final WallTableStat tableStat = this.getTableStat(tableName);
            if (tableStat == null) {
                continue;
            }
            final WallSqlTableStat sqlTableStat = entry.getValue();
            if (sqlTableStat.getSelectCount() <= 0) {
                continue;
            }
            tableStat.addFetchRowCount(fetchRowCount);
        }
    }
    
    public WallTableStat getTableStatWithLowerName(final String lowerCaseName) {
        WallTableStat stat = this.tableStats.get(lowerCaseName);
        if (stat == null) {
            if (this.tableStats.size() > 10000) {
                return null;
            }
            this.tableStats.putIfAbsent(lowerCaseName, new WallTableStat());
            stat = this.tableStats.get(lowerCaseName);
        }
        return stat;
    }
    
    public WallFunctionStat getFunctionStat(final String functionName) {
        final String lowerCaseName = functionName.toLowerCase();
        return this.getFunctionStatWithLowerName(lowerCaseName);
    }
    
    public WallFunctionStat getFunctionStatWithLowerName(final String lowerCaseName) {
        WallFunctionStat stat = this.functionStats.get(lowerCaseName);
        if (stat == null) {
            if (this.functionStats.size() > 10000) {
                return null;
            }
            this.functionStats.putIfAbsent(lowerCaseName, new WallFunctionStat());
            stat = this.functionStats.get(lowerCaseName);
        }
        return stat;
    }
    
    public WallConfig getConfig() {
        return this.config;
    }
    
    public WallSqlStat addWhiteSql(final String sql, final Map<String, WallSqlTableStat> tableStats, final Map<String, WallSqlFunctionStat> functionStats, final boolean syntaxError) {
        if (!this.whiteListEnable) {
            final WallSqlStat stat = new WallSqlStat(tableStats, functionStats, syntaxError);
            return stat;
        }
        String mergedSql;
        try {
            mergedSql = ParameterizedOutputVisitorUtils.parameterize(sql, this.dbType);
        }
        catch (Exception ex) {
            final WallSqlStat stat2 = new WallSqlStat(tableStats, functionStats, syntaxError);
            stat2.incrementAndGetExecuteCount();
            return stat2;
        }
        if (mergedSql != sql) {
            this.lock.readLock().lock();
            WallSqlStat mergedStat;
            try {
                if (this.whiteList == null) {
                    this.whiteList = new LRUCache<String, WallSqlStat>(this.whiteSqlMaxSize);
                }
                mergedStat = this.whiteList.get(mergedSql);
            }
            finally {
                this.lock.readLock().unlock();
            }
            if (mergedStat == null) {
                final WallSqlStat newStat = new WallSqlStat(tableStats, functionStats, syntaxError);
                newStat.setSample(sql);
                this.lock.writeLock().lock();
                try {
                    mergedStat = this.whiteList.get(mergedSql);
                    if (mergedStat == null) {
                        this.whiteList.put(mergedSql, newStat);
                        mergedStat = newStat;
                    }
                }
                finally {
                    this.lock.writeLock().unlock();
                }
            }
            mergedStat.incrementAndGetExecuteCount();
            return mergedStat;
        }
        this.lock.writeLock().lock();
        try {
            if (this.whiteList == null) {
                this.whiteList = new LRUCache<String, WallSqlStat>(this.whiteSqlMaxSize);
            }
            WallSqlStat wallStat = this.whiteList.get(sql);
            if (wallStat == null) {
                wallStat = new WallSqlStat(tableStats, functionStats, syntaxError);
                this.whiteList.put(sql, wallStat);
                wallStat.setSample(sql);
                wallStat.incrementAndGetExecuteCount();
            }
            return wallStat;
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public WallSqlStat addBlackSql(final String sql, final Map<String, WallSqlTableStat> tableStats, final Map<String, WallSqlFunctionStat> functionStats, final List<Violation> violations, final boolean syntaxError) {
        if (!this.blackListEnable) {
            return new WallSqlStat(tableStats, functionStats, violations, syntaxError);
        }
        String mergedSql;
        try {
            mergedSql = ParameterizedOutputVisitorUtils.parameterize(sql, this.dbType);
        }
        catch (Exception ex) {
            mergedSql = sql;
        }
        this.lock.writeLock().lock();
        try {
            if (this.blackList == null) {
                this.blackList = new LRUCache<String, WallSqlStat>(this.blackSqlMaxSize);
            }
            if (this.blackMergedList == null) {
                this.blackMergedList = new LRUCache<String, WallSqlStat>(this.blackSqlMaxSize);
            }
            WallSqlStat wallStat = this.blackList.get(sql);
            if (wallStat == null) {
                wallStat = this.blackMergedList.get(mergedSql);
                if (wallStat == null) {
                    wallStat = new WallSqlStat(tableStats, functionStats, violations, syntaxError);
                    this.blackMergedList.put(mergedSql, wallStat);
                    wallStat.setSample(sql);
                }
                wallStat.incrementAndGetExecuteCount();
                this.blackList.put(sql, wallStat);
            }
            return wallStat;
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public Set<String> getWhiteList() {
        final Set<String> hashSet = new HashSet<String>();
        this.lock.readLock().lock();
        try {
            if (this.whiteList != null) {
                hashSet.addAll((Collection<? extends String>)this.whiteList.keySet());
            }
        }
        finally {
            this.lock.readLock().unlock();
        }
        return Collections.unmodifiableSet((Set<? extends String>)hashSet);
    }
    
    public Set<String> getSqlList() {
        final Set<String> hashSet = new HashSet<String>();
        this.lock.readLock().lock();
        try {
            if (this.whiteList != null) {
                hashSet.addAll((Collection<? extends String>)this.whiteList.keySet());
            }
            if (this.blackMergedList != null) {
                hashSet.addAll((Collection<? extends String>)this.blackMergedList.keySet());
            }
        }
        finally {
            this.lock.readLock().unlock();
        }
        return Collections.unmodifiableSet((Set<? extends String>)hashSet);
    }
    
    public Set<String> getBlackList() {
        final Set<String> hashSet = new HashSet<String>();
        this.lock.readLock().lock();
        try {
            if (this.blackList != null) {
                hashSet.addAll((Collection<? extends String>)this.blackList.keySet());
            }
        }
        finally {
            this.lock.readLock().unlock();
        }
        return Collections.unmodifiableSet((Set<? extends String>)hashSet);
    }
    
    public void clearCache() {
        this.lock.writeLock().lock();
        try {
            if (this.whiteList != null) {
                this.whiteList = null;
            }
            if (this.blackList != null) {
                this.blackList = null;
            }
            if (this.blackMergedList != null) {
                this.blackMergedList = null;
            }
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public void clearWhiteList() {
        this.lock.writeLock().lock();
        try {
            if (this.whiteList != null) {
                this.whiteList = null;
            }
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public void clearBlackList() {
        this.lock.writeLock().lock();
        try {
            if (this.blackList != null) {
                this.blackList = null;
            }
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public WallSqlStat getWhiteSql(final String sql) {
        WallSqlStat stat = null;
        this.lock.readLock().lock();
        try {
            if (this.whiteList == null) {
                return null;
            }
            stat = this.whiteList.get(sql);
        }
        finally {
            this.lock.readLock().unlock();
        }
        if (stat != null) {
            return stat;
        }
        String mergedSql;
        try {
            mergedSql = ParameterizedOutputVisitorUtils.parameterize(sql, this.dbType, (List<Object>)null);
        }
        catch (Exception ex) {
            return null;
        }
        this.lock.readLock().lock();
        try {
            stat = this.whiteList.get(mergedSql);
        }
        finally {
            this.lock.readLock().unlock();
        }
        return stat;
    }
    
    public WallSqlStat getBlackSql(final String sql) {
        this.lock.readLock().lock();
        try {
            if (this.blackList == null) {
                return null;
            }
            return this.blackList.get(sql);
        }
        finally {
            this.lock.readLock().unlock();
        }
    }
    
    public boolean whiteContains(final String sql) {
        return this.getWhiteSql(sql) != null;
    }
    
    public abstract SQLStatementParser createParser(final String p0);
    
    public abstract WallVisitor createWallVisitor();
    
    public abstract ExportParameterVisitor createExportParameterVisitor();
    
    public boolean checkValid(final String sql) {
        final WallContext originalContext = WallContext.current();
        try {
            WallContext.create(this.dbType);
            final WallCheckResult result = this.checkInternal(sql);
            return result.getViolations().isEmpty();
        }
        finally {
            if (originalContext == null) {
                WallContext.clearContext();
            }
        }
    }
    
    public void incrementCommentDeniedCount() {
        this.commentDeniedStat.incrementAndGetDenyCount();
    }
    
    public boolean checkDenyFunction(String functionName) {
        if (functionName == null) {
            return true;
        }
        functionName = functionName.toLowerCase();
        return !this.getConfig().getDenyFunctions().contains(functionName);
    }
    
    public boolean checkDenySchema(String schemaName) {
        if (schemaName == null) {
            return true;
        }
        if (!this.config.isSchemaCheck()) {
            return true;
        }
        schemaName = schemaName.toLowerCase();
        return !this.getConfig().getDenySchemas().contains(schemaName);
    }
    
    public boolean checkDenyTable(String tableName) {
        if (tableName == null) {
            return true;
        }
        tableName = WallVisitorUtils.form(tableName);
        return !this.getConfig().getDenyTables().contains(tableName);
    }
    
    public boolean checkReadOnlyTable(String tableName) {
        if (tableName == null) {
            return true;
        }
        tableName = WallVisitorUtils.form(tableName);
        return !this.getConfig().isReadOnly(tableName);
    }
    
    public WallDenyStat getCommentDenyStat() {
        return this.commentDeniedStat;
    }
    
    public WallCheckResult check(final String sql) {
        final WallContext originalContext = WallContext.current();
        try {
            WallContext.createIfNotExists(this.dbType);
            return this.checkInternal(sql);
        }
        finally {
            if (originalContext == null) {
                WallContext.clearContext();
            }
        }
    }
    
    private WallCheckResult checkInternal(final String sql) {
        this.checkCount.incrementAndGet();
        final WallContext context = WallContext.current();
        if (this.config.isDoPrivilegedAllow() && ispPrivileged()) {
            final WallCheckResult checkResult = new WallCheckResult();
            checkResult.setSql(sql);
            return checkResult;
        }
        final boolean mulltiTenant = this.config.getTenantTablePattern() != null && this.config.getTenantTablePattern().length() > 0;
        if (!mulltiTenant) {
            final WallCheckResult checkResult2 = this.checkWhiteAndBlackList(sql);
            if (checkResult2 != null) {
                checkResult2.setSql(sql);
                return checkResult2;
            }
        }
        this.hardCheckCount.incrementAndGet();
        final List<Violation> violations = new ArrayList<Violation>();
        final List<SQLStatement> statementList = new ArrayList<SQLStatement>();
        boolean syntaxError = false;
        boolean endOfComment = false;
        try {
            final SQLStatementParser parser = this.createParser(sql);
            parser.getLexer().setCommentHandler(WallCommentHandler.instance);
            if (!this.config.isCommentAllow()) {
                parser.getLexer().setAllowComment(false);
            }
            if (!this.config.isCompleteInsertValuesCheck()) {
                parser.setParseCompleteValues(false);
                parser.setParseValuesSize(this.config.getInsertValuesCheckSize());
            }
            parser.parseStatementList(statementList);
            final Token lastToken = parser.getLexer().token();
            if (lastToken != Token.EOF && this.config.isStrictSyntaxCheck()) {
                violations.add(new IllegalSQLObjectViolation(1001, "not terminal sql, token " + lastToken, sql));
            }
            endOfComment = parser.getLexer().isEndOfComment();
        }
        catch (NotAllowCommentException e4) {
            violations.add(new IllegalSQLObjectViolation(1104, "comment not allow", sql));
            this.incrementCommentDeniedCount();
        }
        catch (ParserException e) {
            this.syntaxErrorCount.incrementAndGet();
            syntaxError = true;
            if (this.config.isStrictSyntaxCheck()) {
                violations.add(new SyntaxErrorViolation(e, sql));
            }
        }
        catch (Exception e2) {
            if (this.config.isStrictSyntaxCheck()) {
                violations.add(new SyntaxErrorViolation(e2, sql));
            }
        }
        if (statementList.size() > 1 && !this.config.isMultiStatementAllow()) {
            violations.add(new IllegalSQLObjectViolation(2201, "multi-statement not allow", sql));
        }
        final WallVisitor visitor = this.createWallVisitor();
        visitor.setSqlEndOfComment(endOfComment);
        if (statementList.size() > 0) {
            boolean lastIsHint = false;
            for (int i = 0; i < statementList.size(); ++i) {
                final SQLStatement stmt = statementList.get(i);
                if ((i == 0 || lastIsHint) && stmt instanceof MySqlHintStatement) {
                    lastIsHint = true;
                }
                else {
                    try {
                        stmt.accept(visitor);
                    }
                    catch (ParserException e3) {
                        violations.add(new SyntaxErrorViolation(e3, sql));
                    }
                }
            }
        }
        if (visitor.getViolations().size() > 0) {
            violations.addAll(visitor.getViolations());
        }
        final Map<String, WallSqlTableStat> tableStat = context.getTableStats();
        boolean updateCheckHandlerEnable = false;
        final WallUpdateCheckHandler updateCheckHandler = this.config.getUpdateCheckHandler();
        if (updateCheckHandler != null) {
            for (final SQLStatement stmt2 : statementList) {
                if (stmt2 instanceof SQLUpdateStatement) {
                    final SQLUpdateStatement updateStmt = (SQLUpdateStatement)stmt2;
                    final SQLName table = updateStmt.getTableName();
                    if (table == null) {
                        continue;
                    }
                    final String tableName = table.getSimpleName();
                    final Set<String> updateCheckColumns = this.config.getUpdateCheckTable(tableName);
                    if (updateCheckColumns != null && updateCheckColumns.size() > 0) {
                        updateCheckHandlerEnable = true;
                        break;
                    }
                    continue;
                }
            }
        }
        WallSqlStat sqlStat = null;
        if (violations.size() > 0) {
            this.violationCount.incrementAndGet();
            if (!updateCheckHandlerEnable && sql.length() < this.MAX_SQL_LENGTH) {
                sqlStat = this.addBlackSql(sql, tableStat, context.getFunctionStats(), violations, syntaxError);
            }
        }
        else if (!updateCheckHandlerEnable && sql.length() < this.MAX_SQL_LENGTH) {
            boolean selectLimit = false;
            if (this.config.getSelectLimit() > 0) {
                for (final SQLStatement stmt3 : statementList) {
                    if (stmt3 instanceof SQLSelectStatement) {
                        selectLimit = true;
                        break;
                    }
                }
            }
            if (!selectLimit) {
                sqlStat = this.addWhiteSql(sql, tableStat, context.getFunctionStats(), syntaxError);
            }
        }
        if (sqlStat == null && updateCheckHandlerEnable) {
            sqlStat = new WallSqlStat(tableStat, context.getFunctionStats(), violations, syntaxError);
        }
        Map<String, WallSqlTableStat> tableStats = null;
        Map<String, WallSqlFunctionStat> functionStats = null;
        if (context != null) {
            tableStats = context.getTableStats();
            functionStats = context.getFunctionStats();
            this.recordStats(tableStats, functionStats);
        }
        WallCheckResult result;
        if (sqlStat != null) {
            context.setSqlStat(sqlStat);
            result = new WallCheckResult(sqlStat, statementList);
        }
        else {
            result = new WallCheckResult(null, violations, tableStats, functionStats, statementList, syntaxError);
        }
        String resultSql;
        if (visitor.isSqlModified()) {
            resultSql = SQLUtils.toSQLString(statementList, this.dbType);
        }
        else {
            resultSql = sql;
        }
        result.setSql(resultSql);
        result.setUpdateCheckItems(visitor.getUpdateCheckItems());
        return result;
    }
    
    private WallCheckResult checkWhiteAndBlackList(final String sql) {
        if (this.config.getUpdateCheckHandler() != null) {
            return null;
        }
        if (this.blackListEnable) {
            final WallSqlStat sqlStat = this.getBlackSql(sql);
            if (sqlStat != null) {
                this.blackListHitCount.incrementAndGet();
                this.violationCount.incrementAndGet();
                if (sqlStat.isSyntaxError()) {
                    this.syntaxErrorCount.incrementAndGet();
                }
                sqlStat.incrementAndGetExecuteCount();
                this.recordStats(sqlStat.getTableStats(), sqlStat.getFunctionStats());
                return new WallCheckResult(sqlStat);
            }
        }
        if (this.whiteListEnable) {
            final WallSqlStat sqlStat = this.getWhiteSql(sql);
            if (sqlStat != null) {
                this.whiteListHitCount.incrementAndGet();
                sqlStat.incrementAndGetExecuteCount();
                if (sqlStat.isSyntaxError()) {
                    this.syntaxErrorCount.incrementAndGet();
                }
                this.recordStats(sqlStat.getTableStats(), sqlStat.getFunctionStats());
                final WallContext context = WallContext.current();
                if (context != null) {
                    context.setSqlStat(sqlStat);
                }
                return new WallCheckResult(sqlStat);
            }
        }
        return null;
    }
    
    void recordStats(final Map<String, WallSqlTableStat> tableStats, final Map<String, WallSqlFunctionStat> functionStats) {
        if (tableStats != null) {
            for (final Map.Entry<String, WallSqlTableStat> entry : tableStats.entrySet()) {
                final String tableName = entry.getKey();
                final WallSqlTableStat sqlTableStat = entry.getValue();
                final WallTableStat tableStat = this.getTableStat(tableName);
                if (tableStat != null) {
                    tableStat.addSqlTableStat(sqlTableStat);
                }
            }
        }
        if (functionStats != null) {
            for (final Map.Entry<String, WallSqlFunctionStat> entry2 : functionStats.entrySet()) {
                final String tableName = entry2.getKey();
                final WallSqlFunctionStat sqlTableStat2 = entry2.getValue();
                final WallFunctionStat functionStat = this.getFunctionStatWithLowerName(tableName);
                if (functionStat != null) {
                    functionStat.addSqlFunctionStat(sqlTableStat2);
                }
            }
        }
    }
    
    public static boolean ispPrivileged() {
        final Boolean value = WallProvider.privileged.get();
        return value != null && value;
    }
    
    public static <T> T doPrivileged(final PrivilegedAction<T> action) {
        final Boolean original = WallProvider.privileged.get();
        WallProvider.privileged.set(Boolean.TRUE);
        try {
            return action.run();
        }
        finally {
            WallProvider.privileged.set(original);
        }
    }
    
    public static void setTenantValue(final Object value) {
        WallProvider.tenantValueLocal.set(value);
    }
    
    public static Object getTenantValue() {
        return WallProvider.tenantValueLocal.get();
    }
    
    public long getWhiteListHitCount() {
        return this.whiteListHitCount.get();
    }
    
    public long getBlackListHitCount() {
        return this.blackListHitCount.get();
    }
    
    public long getSyntaxErrorCount() {
        return this.syntaxErrorCount.get();
    }
    
    public long getCheckCount() {
        return this.checkCount.get();
    }
    
    public long getViolationCount() {
        return this.violationCount.get();
    }
    
    public long getHardCheckCount() {
        return this.hardCheckCount.get();
    }
    
    public long getViolationEffectRowCount() {
        return this.violationEffectRowCount.get();
    }
    
    public void addViolationEffectRowCount(final long rowCount) {
        this.violationEffectRowCount.addAndGet(rowCount);
    }
    
    public WallProviderStatValue getStatValue(final boolean reset) {
        final WallProviderStatValue statValue = new WallProviderStatValue();
        statValue.setName(this.name);
        statValue.setCheckCount(JdbcSqlStatUtils.get(this.checkCount, reset));
        statValue.setHardCheckCount(JdbcSqlStatUtils.get(this.hardCheckCount, reset));
        statValue.setViolationCount(JdbcSqlStatUtils.get(this.violationCount, reset));
        statValue.setViolationEffectRowCount(JdbcSqlStatUtils.get(this.violationEffectRowCount, reset));
        statValue.setBlackListHitCount(JdbcSqlStatUtils.get(this.blackListHitCount, reset));
        statValue.setWhiteListHitCount(JdbcSqlStatUtils.get(this.whiteListHitCount, reset));
        statValue.setSyntaxErrorCount(JdbcSqlStatUtils.get(this.syntaxErrorCount, reset));
        for (final Map.Entry<String, WallTableStat> entry : this.tableStats.entrySet()) {
            final String tableName = entry.getKey();
            final WallTableStat tableStat = entry.getValue();
            final WallTableStatValue tableStatValue = tableStat.getStatValue(reset);
            if (tableStatValue.getTotalExecuteCount() == 0L) {
                continue;
            }
            tableStatValue.setName(tableName);
            statValue.getTables().add(tableStatValue);
        }
        for (final Map.Entry<String, WallFunctionStat> entry2 : this.functionStats.entrySet()) {
            final String functionName = entry2.getKey();
            final WallFunctionStat functionStat = entry2.getValue();
            final WallFunctionStatValue functionStatValue = functionStat.getStatValue(reset);
            if (functionStatValue.getInvokeCount() == 0L) {
                continue;
            }
            functionStatValue.setName(functionName);
            statValue.getFunctions().add(functionStatValue);
        }
        final Lock lock = reset ? this.lock.writeLock() : this.lock.readLock();
        lock.lock();
        try {
            if (this.whiteList != null) {
                for (final Map.Entry<String, WallSqlStat> entry3 : this.whiteList.entrySet()) {
                    final String sql = entry3.getKey();
                    final WallSqlStat sqlStat = entry3.getValue();
                    final WallSqlStatValue sqlStatValue = sqlStat.getStatValue(reset);
                    if (sqlStatValue.getExecuteCount() == 0L) {
                        continue;
                    }
                    sqlStatValue.setSql(sql);
                    long sqlHash = sqlStat.getSqlHash();
                    if (sqlHash == 0L) {
                        sqlHash = Utils.fnv_64(sql);
                        sqlStat.setSqlHash(sqlHash);
                    }
                    sqlStatValue.setSqlHash(sqlHash);
                    statValue.getWhiteList().add(sqlStatValue);
                }
            }
            if (this.blackMergedList != null) {
                for (final Map.Entry<String, WallSqlStat> entry3 : this.blackMergedList.entrySet()) {
                    final String sql = entry3.getKey();
                    final WallSqlStat sqlStat = entry3.getValue();
                    final WallSqlStatValue sqlStatValue = sqlStat.getStatValue(reset);
                    if (sqlStatValue.getExecuteCount() == 0L) {
                        continue;
                    }
                    sqlStatValue.setSql(sql);
                    statValue.getBlackList().add(sqlStatValue);
                }
            }
        }
        finally {
            lock.unlock();
        }
        return statValue;
    }
    
    public Map<String, Object> getStatsMap() {
        return this.getStatValue(false).toMap();
    }
    
    public boolean isWhiteListEnable() {
        return this.whiteListEnable;
    }
    
    public void setWhiteListEnable(final boolean whiteListEnable) {
        this.whiteListEnable = whiteListEnable;
    }
    
    public boolean isBlackListEnable() {
        return this.blackListEnable;
    }
    
    public void setBlackListEnable(final boolean blackListEnable) {
        this.blackListEnable = blackListEnable;
    }
    
    static {
        privileged = new ThreadLocal<Boolean>();
        tenantValueLocal = new ThreadLocal<Object>();
    }
    
    public static class WallCommentHandler implements Lexer.CommentHandler
    {
        public static final WallCommentHandler instance;
        
        @Override
        public boolean handle(final Token lastToken, final String comment) {
            if (lastToken == null) {
                return false;
            }
            switch (lastToken) {
                case SELECT:
                case INSERT:
                case DELETE:
                case UPDATE:
                case TRUNCATE:
                case SET:
                case CREATE:
                case ALTER:
                case DROP:
                case SHOW:
                case REPLACE: {
                    return true;
                }
                default: {
                    final WallContext context = WallContext.current();
                    if (context != null) {
                        context.incrementCommentCount();
                    }
                    return false;
                }
            }
        }
        
        static {
            instance = new WallCommentHandler();
        }
    }
}
