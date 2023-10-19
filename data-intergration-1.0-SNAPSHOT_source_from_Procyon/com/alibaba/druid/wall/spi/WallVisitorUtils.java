// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlOptimizeStatement;
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement;
import com.alibaba.druid.sql.ast.statement.SQLBlockStatement;
import com.alibaba.druid.sql.ast.statement.SQLStartTransactionStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLockTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLLockTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlHintStatement;
import com.alibaba.druid.sql.ast.statement.SQLUseStatement;
import com.alibaba.druid.sql.ast.statement.SQLRollbackStatement;
import com.alibaba.druid.sql.ast.statement.SQLCommitStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainStatement;
import com.alibaba.druid.sql.ast.statement.SQLDescribeStatement;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleExecuteImmediateStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerExecStatement;
import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import com.alibaba.druid.sql.ast.statement.SQLMergeStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLCommentStatement;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Enumeration;
import java.io.IOException;
import java.io.Closeable;
import com.alibaba.druid.util.JdbcUtils;
import java.net.URL;
import com.alibaba.druid.sql.ast.statement.SQLUnionOperator;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLReplaceStatement;
import com.alibaba.druid.sql.ast.statement.SQLTruncateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alibaba.druid.sql.ast.expr.SQLExistsExpr;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowGrantsStatement;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;
import com.alibaba.druid.sql.visitor.functions.Function;
import com.alibaba.druid.sql.visitor.functions.Nil;
import com.alibaba.druid.sql.ast.expr.SQLUnaryExpr;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.expr.SQLNotExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOrderingExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.DbType;
import java.util.Collections;
import com.alibaba.druid.wall.WallUpdateCheckHandler;
import java.util.Set;
import com.alibaba.druid.wall.WallUpdateCheckItem;
import com.alibaba.druid.wall.Violation;
import com.alibaba.druid.wall.violation.IllegalSQLObjectViolation;
import java.util.HashSet;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import java.util.Collection;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.wall.WallProvider;
import java.util.Stack;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.util.ServletPathMatcher;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.visitor.ExportParameterVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.visitor.SQLEvalVisitorUtils;
import com.alibaba.druid.sql.ast.expr.SQLNCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.sql.ast.expr.SQLBooleanExpr;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.wall.WallSqlTableStat;
import com.alibaba.druid.wall.WallContext;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExprGroup;
import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.wall.WallVisitor;
import com.alibaba.druid.support.logging.Log;

public class WallVisitorUtils
{
    private static final Log LOG;
    public static final String HAS_TRUE_LIKE = "hasTrueLike";
    public static final String[] whiteHints;
    private static ThreadLocal<WallConditionContext> wallConditionContextLocal;
    private static ThreadLocal<WallTopStatementContext> wallTopStatementContextLocal;
    
    public static void check(final WallVisitor visitor, final SQLInListExpr x) {
    }
    
    public static boolean check(final WallVisitor visitor, final SQLBinaryOpExpr x) {
        if (x.getOperator() == SQLBinaryOperator.BooleanOr || x.getOperator() == SQLBinaryOperator.BooleanAnd) {
            final List<SQLExpr> groupList = SQLBinaryOpExpr.split(x);
            for (final SQLExpr item : groupList) {
                item.accept(visitor);
            }
            return false;
        }
        if (x.getOperator() == SQLBinaryOperator.Add || x.getOperator() == SQLBinaryOperator.Concat) {
            final List<SQLExpr> groupList = SQLBinaryOpExpr.split(x);
            if (groupList.size() >= 4) {
                int chrCount = 0;
                for (int i = 0; i < groupList.size(); ++i) {
                    final SQLExpr item2 = groupList.get(i);
                    if (item2 instanceof SQLMethodInvokeExpr) {
                        final SQLMethodInvokeExpr methodExpr = (SQLMethodInvokeExpr)item2;
                        final String methodName = methodExpr.getMethodName().toLowerCase();
                        if (("chr".equals(methodName) || "char".equals(methodName)) && methodExpr.getArguments().get(0) instanceof SQLLiteralExpr) {
                            ++chrCount;
                        }
                    }
                    else if (item2 instanceof SQLCharExpr && ((SQLCharExpr)item2).getText().length() > 5) {
                        chrCount = 0;
                        continue;
                    }
                    if (chrCount >= 4) {
                        addViolation(visitor, 2112, "evil concat", x);
                        break;
                    }
                }
            }
        }
        return true;
    }
    
    public static boolean check(final WallVisitor visitor, final SQLBinaryOpExprGroup x) {
        return true;
    }
    
    public static void check(final WallVisitor visitor, final SQLCreateTableStatement x) {
        final String tableName = x.getName().getSimpleName();
        final WallContext context = WallContext.current();
        if (context != null) {
            final WallSqlTableStat tableStat = context.getTableStat(tableName);
            if (tableStat != null) {
                tableStat.incrementCreateCount();
            }
        }
    }
    
    public static void check(final WallVisitor visitor, final SQLAlterTableStatement x) {
        final String tableName = x.getName().getSimpleName();
        final WallContext context = WallContext.current();
        if (context != null) {
            final WallSqlTableStat tableStat = context.getTableStat(tableName);
            if (tableStat != null) {
                tableStat.incrementAlterCount();
            }
        }
    }
    
    public static void check(final WallVisitor visitor, final SQLDropTableStatement x) {
        for (final SQLTableSource item : x.getTableSources()) {
            if (item instanceof SQLExprTableSource) {
                final SQLExpr expr = ((SQLExprTableSource)item).getExpr();
                final String tableName = ((SQLName)expr).getSimpleName();
                final WallContext context = WallContext.current();
                if (context == null) {
                    continue;
                }
                final WallSqlTableStat tableStat = context.getTableStat(tableName);
                if (tableStat == null) {
                    continue;
                }
                tableStat.incrementDropCount();
            }
        }
    }
    
    public static void check(final WallVisitor visitor, final SQLSelectItem x) {
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLVariantRefExpr && !isTopSelectItem(expr) && "@".equals(((SQLVariantRefExpr)expr).getName())) {
            addViolation(visitor, 2111, "@ not allow", x);
        }
        if (visitor.getConfig().isSelectAllColumnAllow()) {
            return;
        }
        if (expr instanceof SQLAllColumnExpr && x.getParent() instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)x.getParent();
            final SQLTableSource from = queryBlock.getFrom();
            if (from instanceof SQLExprTableSource) {
                addViolation(visitor, 1002, "'SELECT *' not allow", x);
            }
        }
    }
    
    public static void check(final WallVisitor visitor, final SQLPropertyExpr x) {
        checkSchema(visitor, x.getOwner());
    }
    
    public static void checkInsert(final WallVisitor visitor, final SQLInsertInto x) {
        checkReadOnly(visitor, x.getTableSource());
        if (!visitor.getConfig().isInsertAllow()) {
            addViolation(visitor, 1004, "insert not allow", x);
        }
        checkInsertForMultiTenant(visitor, x);
    }
    
    public static void checkSelelct(final WallVisitor visitor, final SQLSelectQueryBlock x) {
        if (x.getInto() != null) {
            checkReadOnly(visitor, x.getInto());
        }
        if (!visitor.getConfig().isSelectIntoAllow() && x.getInto() != null) {
            addViolation(visitor, 1003, "select into not allow", x);
            return;
        }
        final List<SQLCommentHint> hints = x.getHintsDirect();
        if (hints != null && x.getParent() instanceof SQLUnionQuery && x == ((SQLUnionQuery)x.getParent()).getRight()) {
            for (final SQLCommentHint hint : hints) {
                final String text = hint.getText();
                if (text.startsWith("!")) {
                    addViolation(visitor, 5000, "union select hint not allow", x);
                    return;
                }
            }
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            checkCondition(visitor, x.getWhere());
            final Object whereValue = getConditionValue(visitor, where, visitor.getConfig().isSelectWhereAlwayTrueCheck());
            if (Boolean.TRUE == whereValue && visitor.getConfig().isSelectWhereAlwayTrueCheck() && visitor.isSqlEndOfComment() && !isSimpleConstExpr(where)) {
                addViolation(visitor, 2100, "select alway true condition not allow", x);
            }
        }
        checkSelectForMultiTenant(visitor, x);
    }
    
    public static void checkHaving(final WallVisitor visitor, final SQLExpr x) {
        if (x == null) {
            return;
        }
        if (Boolean.TRUE == getConditionValue(visitor, x, visitor.getConfig().isSelectHavingAlwayTrueCheck()) && visitor.getConfig().isSelectHavingAlwayTrueCheck() && visitor.isSqlEndOfComment() && !isSimpleConstExpr(x)) {
            addViolation(visitor, 2100, "having alway true condition not allow", x);
        }
    }
    
    public static void checkDelete(final WallVisitor visitor, final SQLDeleteStatement x) {
        checkReadOnly(visitor, x.getTableSource());
        final WallConfig config = visitor.getConfig();
        if (!config.isDeleteAllow()) {
            addViolation(visitor, 1004, "delete not allow", x);
            return;
        }
        boolean hasUsing = false;
        if (x instanceof MySqlDeleteStatement) {
            hasUsing = (((MySqlDeleteStatement)x).getUsing() != null);
        }
        final boolean isJoinTableSource = x.getTableSource() instanceof SQLJoinTableSource;
        if (x.getWhere() == null && !hasUsing && !isJoinTableSource) {
            final WallContext context = WallContext.current();
            if (context != null) {
                context.incrementDeleteNoneConditionWarnings();
            }
            if (config.isDeleteWhereNoneCheck()) {
                addViolation(visitor, 2104, "delete none condition not allow", x);
                return;
            }
        }
        final SQLExpr where = x.getWhere();
        if (where != null) {
            checkCondition(visitor, where);
            if (Boolean.TRUE == getConditionValue(visitor, where, config.isDeleteWhereAlwayTrueCheck()) && config.isDeleteWhereAlwayTrueCheck() && visitor.isSqlEndOfComment() && !isSimpleConstExpr(where)) {
                addViolation(visitor, 2100, "delete alway true condition not allow", x);
            }
        }
    }
    
    private static boolean isSimpleConstExpr(final SQLExpr sqlExpr) {
        final List<SQLExpr> parts = getParts(sqlExpr);
        if (parts.isEmpty()) {
            return false;
        }
        for (final SQLExpr part : parts) {
            if (isFirst(part)) {
                Object evalValue = part.getAttribute("eval.value");
                if (evalValue == null) {
                    if (part instanceof SQLBooleanExpr) {
                        evalValue = ((SQLBooleanExpr)part).getBooleanValue();
                    }
                    else if (part instanceof SQLNumericLiteralExpr) {
                        evalValue = ((SQLNumericLiteralExpr)part).getNumber();
                    }
                    else if (part instanceof SQLCharExpr) {
                        evalValue = ((SQLCharExpr)part).getText();
                    }
                    else if (part instanceof SQLNCharExpr) {
                        evalValue = ((SQLNCharExpr)part).getText();
                    }
                }
                final Boolean result = SQLEvalVisitorUtils.castToBoolean(evalValue);
                if (result != null && result) {
                    return true;
                }
            }
            boolean isSimpleConstExpr = false;
            if (part == sqlExpr || part instanceof SQLLiteralExpr) {
                isSimpleConstExpr = true;
            }
            else if (part instanceof SQLBinaryOpExpr) {
                final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)part;
                if ((binaryOpExpr.getOperator() == SQLBinaryOperator.Equality || binaryOpExpr.getOperator() == SQLBinaryOperator.NotEqual || binaryOpExpr.getOperator() == SQLBinaryOperator.GreaterThan) && binaryOpExpr.getLeft() instanceof SQLIntegerExpr && binaryOpExpr.getRight() instanceof SQLIntegerExpr) {
                    isSimpleConstExpr = true;
                }
            }
            if (!isSimpleConstExpr) {
                return false;
            }
        }
        return true;
    }
    
    private static void checkCondition(final WallVisitor visitor, final SQLExpr x) {
        if (x == null) {
            return;
        }
        if (visitor.getConfig().isMustParameterized()) {
            final ExportParameterVisitor exportParameterVisitor = visitor.getProvider().createExportParameterVisitor();
            x.accept(exportParameterVisitor);
            if (exportParameterVisitor.getParameters().size() > 0) {
                addViolation(visitor, 2200, "sql must parameterized", x);
            }
        }
    }
    
    private static void checkJoinSelectForMultiTenant(final WallVisitor visitor, final SQLJoinTableSource join, final SQLSelectQueryBlock x) {
        final WallConfig.TenantCallBack tenantCallBack = visitor.getConfig().getTenantCallBack();
        final String tenantTablePattern = visitor.getConfig().getTenantTablePattern();
        if (tenantCallBack == null && (tenantTablePattern == null || tenantTablePattern.length() == 0)) {
            return;
        }
        final SQLTableSource right = join.getRight();
        if (right instanceof SQLExprTableSource) {
            final SQLExpr tableExpr = ((SQLExprTableSource)right).getExpr();
            if (tableExpr instanceof SQLIdentifierExpr) {
                final String tableName = ((SQLIdentifierExpr)tableExpr).getName();
                String alias = null;
                String tenantColumn = null;
                if (tenantCallBack != null) {
                    tenantColumn = tenantCallBack.getTenantColumn(WallConfig.TenantCallBack.StatementType.SELECT, tableName);
                }
                if (StringUtils.isEmpty(tenantColumn) && ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName)) {
                    tenantColumn = visitor.getConfig().getTenantColumn();
                }
                if (!StringUtils.isEmpty(tenantColumn)) {
                    alias = right.getAlias();
                    if (alias == null) {
                        alias = tableName;
                    }
                    SQLExpr item = null;
                    if (alias != null) {
                        item = new SQLPropertyExpr(new SQLIdentifierExpr(alias), tenantColumn);
                    }
                    else {
                        item = new SQLIdentifierExpr(tenantColumn);
                    }
                    final SQLSelectItem selectItem = new SQLSelectItem(item);
                    x.getSelectList().add(selectItem);
                    visitor.setSqlModified(true);
                }
            }
        }
    }
    
    private static boolean isSelectStatmentForMultiTenant(final SQLSelectQueryBlock queryBlock) {
        SQLObject parent;
        SQLObject x;
        for (parent = queryBlock.getParent(); parent != null && parent instanceof SQLUnionQuery; parent = x.getParent()) {
            x = parent;
        }
        if (!(parent instanceof SQLSelect)) {
            return false;
        }
        parent = ((SQLSelect)parent).getParent();
        return parent instanceof SQLSelectStatement;
    }
    
    private static void checkSelectForMultiTenant(final WallVisitor visitor, final SQLSelectQueryBlock x) {
        final WallConfig.TenantCallBack tenantCallBack = visitor.getConfig().getTenantCallBack();
        final String tenantTablePattern = visitor.getConfig().getTenantTablePattern();
        if (tenantCallBack == null && (tenantTablePattern == null || tenantTablePattern.length() == 0)) {
            return;
        }
        if (x == null) {
            throw new IllegalStateException("x is null");
        }
        if (!isSelectStatmentForMultiTenant(x)) {
            return;
        }
        final SQLTableSource tableSource = x.getFrom();
        String alias = null;
        String matchTableName = null;
        String tenantColumn = null;
        if (tableSource instanceof SQLExprTableSource) {
            final SQLExpr tableExpr = ((SQLExprTableSource)tableSource).getExpr();
            if (tableExpr instanceof SQLIdentifierExpr) {
                final String tableName = ((SQLIdentifierExpr)tableExpr).getName();
                if (tenantCallBack != null) {
                    tenantColumn = tenantCallBack.getTenantColumn(WallConfig.TenantCallBack.StatementType.SELECT, tableName);
                }
                if (StringUtils.isEmpty(tenantColumn) && ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName)) {
                    tenantColumn = visitor.getConfig().getTenantColumn();
                }
                if (!StringUtils.isEmpty(tenantColumn)) {
                    matchTableName = tableName;
                    alias = tableSource.getAlias();
                }
            }
        }
        else if (tableSource instanceof SQLJoinTableSource) {
            final SQLJoinTableSource join = (SQLJoinTableSource)tableSource;
            if (join.getLeft() instanceof SQLExprTableSource) {
                final SQLExpr tableExpr2 = ((SQLExprTableSource)join.getLeft()).getExpr();
                if (tableExpr2 instanceof SQLIdentifierExpr) {
                    final String tableName2 = ((SQLIdentifierExpr)tableExpr2).getName();
                    if (tenantCallBack != null) {
                        tenantColumn = tenantCallBack.getTenantColumn(WallConfig.TenantCallBack.StatementType.SELECT, tableName2);
                    }
                    if (StringUtils.isEmpty(tenantColumn) && ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName2)) {
                        tenantColumn = visitor.getConfig().getTenantColumn();
                    }
                    if (!StringUtils.isEmpty(tenantColumn)) {
                        matchTableName = tableName2;
                        alias = join.getLeft().getAlias();
                        if (alias == null) {
                            alias = tableName2;
                        }
                    }
                }
                checkJoinSelectForMultiTenant(visitor, join, x);
            }
            else {
                checkJoinSelectForMultiTenant(visitor, join, x);
            }
        }
        if (matchTableName == null) {
            return;
        }
        SQLExpr item = null;
        if (alias != null) {
            item = new SQLPropertyExpr(new SQLIdentifierExpr(alias), tenantColumn);
        }
        else {
            item = new SQLIdentifierExpr(tenantColumn);
        }
        final SQLSelectItem selectItem = new SQLSelectItem(item);
        x.getSelectList().add(selectItem);
        visitor.setSqlModified(true);
    }
    
    private static void checkUpdateForMultiTenant(final WallVisitor visitor, final SQLUpdateStatement x) {
        final WallConfig.TenantCallBack tenantCallBack = visitor.getConfig().getTenantCallBack();
        final String tenantTablePattern = visitor.getConfig().getTenantTablePattern();
        if (tenantCallBack == null && (tenantTablePattern == null || tenantTablePattern.length() == 0)) {
            return;
        }
        if (x == null) {
            throw new IllegalStateException("x is null");
        }
        final SQLTableSource tableSource = x.getTableSource();
        String alias = null;
        String matchTableName = null;
        String tenantColumn = null;
        if (tableSource instanceof SQLExprTableSource) {
            final SQLExpr tableExpr = ((SQLExprTableSource)tableSource).getExpr();
            if (tableExpr instanceof SQLIdentifierExpr) {
                final String tableName = ((SQLIdentifierExpr)tableExpr).getName();
                if (tenantCallBack != null) {
                    tenantColumn = tenantCallBack.getTenantColumn(WallConfig.TenantCallBack.StatementType.UPDATE, tableName);
                }
                if (StringUtils.isEmpty(tenantColumn) && ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName)) {
                    tenantColumn = visitor.getConfig().getTenantColumn();
                }
                if (!StringUtils.isEmpty(tenantColumn)) {
                    matchTableName = tableName;
                    alias = tableSource.getAlias();
                }
            }
        }
        if (matchTableName == null) {
            return;
        }
        SQLExpr item = null;
        if (alias != null) {
            item = new SQLPropertyExpr(new SQLIdentifierExpr(alias), tenantColumn);
        }
        else {
            item = new SQLIdentifierExpr(tenantColumn);
        }
        final SQLExpr value = generateTenantValue(visitor, alias, WallConfig.TenantCallBack.StatementType.UPDATE, matchTableName);
        final SQLUpdateSetItem updateSetItem = new SQLUpdateSetItem();
        updateSetItem.setColumn(item);
        updateSetItem.setValue(value);
        x.addItem(updateSetItem);
        visitor.setSqlModified(true);
    }
    
    private static void checkInsertForMultiTenant(final WallVisitor visitor, final SQLInsertInto x) {
        final WallConfig.TenantCallBack tenantCallBack = visitor.getConfig().getTenantCallBack();
        final String tenantTablePattern = visitor.getConfig().getTenantTablePattern();
        if (tenantCallBack == null && (tenantTablePattern == null || tenantTablePattern.length() == 0)) {
            return;
        }
        if (x == null) {
            throw new IllegalStateException("x is null");
        }
        final SQLExprTableSource tableSource = x.getTableSource();
        String alias = null;
        String matchTableName = null;
        String tenantColumn = null;
        final SQLExpr tableExpr = tableSource.getExpr();
        if (tableExpr instanceof SQLIdentifierExpr) {
            final String tableName = ((SQLIdentifierExpr)tableExpr).getName();
            if (tenantCallBack != null) {
                tenantColumn = tenantCallBack.getTenantColumn(WallConfig.TenantCallBack.StatementType.INSERT, tableName);
            }
            if (StringUtils.isEmpty(tenantColumn) && ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName)) {
                tenantColumn = visitor.getConfig().getTenantColumn();
            }
            if (!StringUtils.isEmpty(tenantColumn)) {
                matchTableName = tableName;
                alias = tableSource.getAlias();
            }
        }
        if (matchTableName == null) {
            return;
        }
        SQLExpr item = null;
        if (alias != null) {
            item = new SQLPropertyExpr(new SQLIdentifierExpr(alias), tenantColumn);
        }
        else {
            item = new SQLIdentifierExpr(tenantColumn);
        }
        final SQLExpr value = generateTenantValue(visitor, alias, WallConfig.TenantCallBack.StatementType.INSERT, matchTableName);
        x.getColumns().add(item);
        List<SQLInsertStatement.ValuesClause> valuesClauses = null;
        SQLInsertStatement.ValuesClause valuesClause = null;
        if (x instanceof MySqlInsertStatement) {
            valuesClauses = ((MySqlInsertStatement)x).getValuesList();
        }
        else if (x instanceof SQLServerInsertStatement) {
            valuesClauses = ((MySqlInsertStatement)x).getValuesList();
        }
        else {
            valuesClause = x.getValues();
        }
        if (valuesClauses != null && valuesClauses.size() > 0) {
            for (final SQLInsertStatement.ValuesClause clause : valuesClauses) {
                clause.addValue(value);
            }
        }
        if (valuesClause != null) {
            valuesClause.addValue(value);
        }
        final SQLSelect select = x.getQuery();
        if (select != null) {
            final List<SQLSelectQueryBlock> queryBlocks = splitSQLSelectQuery(select.getQuery());
            for (final SQLSelectQueryBlock queryBlock : queryBlocks) {
                queryBlock.getSelectList().add(new SQLSelectItem(value));
            }
        }
        visitor.setSqlModified(true);
    }
    
    private static List<SQLSelectQueryBlock> splitSQLSelectQuery(final SQLSelectQuery x) {
        final List<SQLSelectQueryBlock> groupList = new ArrayList<SQLSelectQueryBlock>();
        final Stack<SQLSelectQuery> stack = new Stack<SQLSelectQuery>();
        stack.push(x);
        do {
            final SQLSelectQuery query = stack.pop();
            if (query instanceof SQLSelectQueryBlock) {
                groupList.add((SQLSelectQueryBlock)query);
            }
            else {
                if (!(query instanceof SQLUnionQuery)) {
                    continue;
                }
                final SQLUnionQuery unionQuery = (SQLUnionQuery)query;
                stack.push(unionQuery.getLeft());
                stack.push(unionQuery.getRight());
            }
        } while (!stack.empty());
        return groupList;
    }
    
    @Deprecated
    public static void checkConditionForMultiTenant(final WallVisitor visitor, final SQLExpr x, final SQLObject parent) {
        final String tenantTablePattern = visitor.getConfig().getTenantTablePattern();
        if (tenantTablePattern == null || tenantTablePattern.length() == 0) {
            return;
        }
        if (parent == null) {
            throw new IllegalStateException("parent is null");
        }
        String alias = null;
        WallConfig.TenantCallBack.StatementType statementType = null;
        SQLTableSource tableSource;
        if (parent instanceof SQLDeleteStatement) {
            tableSource = ((SQLDeleteStatement)parent).getTableSource();
            statementType = WallConfig.TenantCallBack.StatementType.DELETE;
        }
        else if (parent instanceof SQLUpdateStatement) {
            tableSource = ((SQLUpdateStatement)parent).getTableSource();
            statementType = WallConfig.TenantCallBack.StatementType.UPDATE;
        }
        else {
            if (!(parent instanceof SQLSelectQueryBlock)) {
                throw new IllegalStateException("not support parent : " + parent.getClass());
            }
            tableSource = ((SQLSelectQueryBlock)parent).getFrom();
            statementType = WallConfig.TenantCallBack.StatementType.SELECT;
        }
        String matchTableName = null;
        if (tableSource instanceof SQLExprTableSource) {
            final SQLExpr tableExpr = ((SQLExprTableSource)tableSource).getExpr();
            if (tableExpr instanceof SQLIdentifierExpr) {
                final String tableName = ((SQLIdentifierExpr)tableExpr).getName();
                if (ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName)) {
                    matchTableName = tableName;
                    alias = tableSource.getAlias();
                }
            }
        }
        else if (tableSource instanceof SQLJoinTableSource) {
            final SQLJoinTableSource join = (SQLJoinTableSource)tableSource;
            if (join.getLeft() instanceof SQLExprTableSource) {
                final SQLExpr tableExpr2 = ((SQLExprTableSource)join.getLeft()).getExpr();
                if (tableExpr2 instanceof SQLIdentifierExpr) {
                    final String tableName2 = ((SQLIdentifierExpr)tableExpr2).getName();
                    if (ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName2)) {
                        matchTableName = tableName2;
                        alias = join.getLeft().getAlias();
                    }
                }
                checkJoinConditionForMultiTenant(visitor, join, false, statementType);
            }
            else {
                checkJoinConditionForMultiTenant(visitor, join, true, statementType);
            }
        }
        if (matchTableName == null) {
            return;
        }
        final SQLBinaryOpExpr tenantCondition = createTenantCondition(visitor, alias, statementType, matchTableName);
        SQLExpr condition;
        if (x == null) {
            condition = tenantCondition;
        }
        else {
            condition = new SQLBinaryOpExpr(tenantCondition, SQLBinaryOperator.BooleanAnd, x);
        }
        if (parent instanceof SQLDeleteStatement) {
            final SQLDeleteStatement deleteStmt = (SQLDeleteStatement)parent;
            deleteStmt.setWhere(condition);
            visitor.setSqlModified(true);
        }
        else if (parent instanceof SQLUpdateStatement) {
            final SQLUpdateStatement updateStmt = (SQLUpdateStatement)parent;
            updateStmt.setWhere(condition);
            visitor.setSqlModified(true);
        }
        else if (parent instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)parent;
            queryBlock.setWhere(condition);
            visitor.setSqlModified(true);
        }
    }
    
    @Deprecated
    public static void checkJoinConditionForMultiTenant(final WallVisitor visitor, final SQLJoinTableSource join, final boolean checkLeft, final WallConfig.TenantCallBack.StatementType statementType) {
        final String tenantTablePattern = visitor.getConfig().getTenantTablePattern();
        if (tenantTablePattern == null || tenantTablePattern.length() == 0) {
            return;
        }
        SQLExpr condition = join.getCondition();
        final SQLTableSource right = join.getRight();
        if (right instanceof SQLExprTableSource) {
            final SQLExpr tableExpr = ((SQLExprTableSource)right).getExpr();
            if (tableExpr instanceof SQLIdentifierExpr) {
                final String tableName = ((SQLIdentifierExpr)tableExpr).getName();
                if (ServletPathMatcher.getInstance().matches(tenantTablePattern, tableName)) {
                    String alias = right.getAlias();
                    if (alias == null) {
                        alias = tableName;
                    }
                    final SQLBinaryOpExpr tenantCondition = createTenantCondition(visitor, alias, statementType, tableName);
                    if (condition == null) {
                        condition = tenantCondition;
                    }
                    else {
                        condition = new SQLBinaryOpExpr(tenantCondition, SQLBinaryOperator.BooleanAnd, condition);
                    }
                }
            }
        }
        if (condition != join.getCondition()) {
            join.setCondition(condition);
            visitor.setSqlModified(true);
        }
    }
    
    @Deprecated
    private static SQLBinaryOpExpr createTenantCondition(final WallVisitor visitor, final String alias, final WallConfig.TenantCallBack.StatementType statementType, final String tableName) {
        SQLExpr left;
        if (alias != null) {
            left = new SQLPropertyExpr(new SQLIdentifierExpr(alias), visitor.getConfig().getTenantColumn());
        }
        else {
            left = new SQLIdentifierExpr(visitor.getConfig().getTenantColumn());
        }
        final SQLExpr right = generateTenantValue(visitor, alias, statementType, tableName);
        final SQLBinaryOpExpr tenantCondition = new SQLBinaryOpExpr(left, SQLBinaryOperator.Equality, right);
        return tenantCondition;
    }
    
    private static SQLExpr generateTenantValue(final WallVisitor visitor, final String alias, final WallConfig.TenantCallBack.StatementType statementType, final String tableName) {
        final WallConfig.TenantCallBack callBack = visitor.getConfig().getTenantCallBack();
        if (callBack != null) {
            WallProvider.setTenantValue(callBack.getTenantValue(statementType, tableName));
        }
        final Object tenantValue = WallProvider.getTenantValue();
        SQLExpr value;
        if (tenantValue instanceof Number) {
            value = new SQLNumberExpr((Number)tenantValue);
        }
        else {
            if (!(tenantValue instanceof String)) {
                throw new IllegalStateException("tenant value not support type " + tenantValue);
            }
            value = new SQLCharExpr((String)tenantValue);
        }
        return value;
    }
    
    public static void checkReadOnly(final WallVisitor visitor, final SQLTableSource tableSource) {
        if (tableSource instanceof SQLExprTableSource) {
            String tableName = null;
            final SQLExpr tableNameExpr = ((SQLExprTableSource)tableSource).getExpr();
            if (tableNameExpr instanceof SQLName) {
                tableName = ((SQLName)tableNameExpr).getSimpleName();
            }
            final boolean readOnlyValid = visitor.getProvider().checkReadOnlyTable(tableName);
            if (!readOnlyValid) {
                addViolation(visitor, 4000, "table readonly : " + tableName, tableSource);
            }
        }
        else if (tableSource instanceof SQLJoinTableSource) {
            final SQLJoinTableSource join = (SQLJoinTableSource)tableSource;
            checkReadOnly(visitor, join.getLeft());
            checkReadOnly(visitor, join.getRight());
        }
    }
    
    public static void checkUpdate(final WallVisitor visitor, final SQLUpdateStatement x) {
        checkReadOnly(visitor, x.getTableSource());
        final WallConfig config = visitor.getConfig();
        if (!config.isUpdateAllow()) {
            addViolation(visitor, 1006, "update not allow", x);
            return;
        }
        final SQLExpr where = x.getWhere();
        if (where == null) {
            final WallContext context = WallContext.current();
            if (context != null) {
                context.incrementUpdateNoneConditionWarnings();
            }
            if (config.isUpdateWhereNoneCheck()) {
                if (!(x instanceof MySqlUpdateStatement)) {
                    addViolation(visitor, 2104, "update none condition not allow", x);
                    return;
                }
                final MySqlUpdateStatement mysqlUpdate = (MySqlUpdateStatement)x;
                if (mysqlUpdate.getLimit() == null) {
                    addViolation(visitor, 2104, "update none condition not allow", x);
                    return;
                }
            }
        }
        else {
            checkCondition(visitor, where);
            if (Boolean.TRUE == getConditionValue(visitor, where, config.isUpdateWhereAlayTrueCheck()) && config.isUpdateWhereAlayTrueCheck() && visitor.isSqlEndOfComment() && !isSimpleConstExpr(where)) {
                addViolation(visitor, 2100, "update alway true condition not allow", x);
            }
            final SQLName table = x.getTableName();
            if (table == null) {
                return;
            }
            final String tableName = table.getSimpleName();
            final Set<String> updateCheckColumns = config.getUpdateCheckTable(tableName);
            final boolean isUpdateCheckTable = updateCheckColumns != null && !updateCheckColumns.isEmpty();
            final WallUpdateCheckHandler updateCheckHandler = config.getUpdateCheckHandler();
            if (isUpdateCheckTable && updateCheckHandler != null) {
                final String checkColumn = updateCheckColumns.iterator().next();
                SQLExpr valueExpr = null;
                for (final SQLUpdateSetItem item : x.getItems()) {
                    if (item.columnMatch(checkColumn)) {
                        valueExpr = item.getValue();
                        break;
                    }
                }
                if (valueExpr != null) {
                    List<SQLExpr> conditions;
                    if (where instanceof SQLBinaryOpExpr) {
                        conditions = SQLBinaryOpExpr.split((SQLBinaryOpExpr)where, SQLBinaryOperator.BooleanAnd);
                    }
                    else if (where instanceof SQLBinaryOpExprGroup) {
                        conditions = new ArrayList<SQLExpr>();
                        for (final SQLExpr each : ((SQLBinaryOpExprGroup)where).getItems()) {
                            if (each instanceof SQLBinaryOpExpr) {
                                conditions.addAll(SQLBinaryOpExpr.split((SQLBinaryOpExpr)each, SQLBinaryOperator.BooleanAnd));
                            }
                            else {
                                if (!(each instanceof SQLInListExpr)) {
                                    continue;
                                }
                                conditions.add(each);
                            }
                        }
                    }
                    else {
                        conditions = new ArrayList<SQLExpr>();
                        conditions.add(where);
                    }
                    final List<SQLExpr> filterValueExprList = new ArrayList<SQLExpr>();
                    for (final SQLExpr condition : conditions) {
                        if (condition instanceof SQLBinaryOpExpr) {
                            final SQLBinaryOpExpr binaryCondition = (SQLBinaryOpExpr)condition;
                            if (binaryCondition.getOperator() != SQLBinaryOperator.Equality || !binaryCondition.conditionContainsColumn(checkColumn)) {
                                continue;
                            }
                            final SQLExpr left = binaryCondition.getLeft();
                            final SQLExpr right = binaryCondition.getRight();
                            if (left instanceof SQLValuableExpr || left instanceof SQLVariantRefExpr) {
                                filterValueExprList.add(left);
                            }
                            else {
                                if (!(right instanceof SQLValuableExpr) && !(right instanceof SQLVariantRefExpr)) {
                                    continue;
                                }
                                filterValueExprList.add(right);
                            }
                        }
                        else {
                            if (!(condition instanceof SQLInListExpr)) {
                                continue;
                            }
                            final SQLInListExpr listExpr = (SQLInListExpr)condition;
                            if (!(listExpr.getExpr() instanceof SQLIdentifierExpr)) {
                                continue;
                            }
                            final SQLIdentifierExpr nameExpr = (SQLIdentifierExpr)listExpr.getExpr();
                            if (!nameExpr.getName().equals(checkColumn)) {
                                continue;
                            }
                            filterValueExprList.addAll(((SQLInListExpr)condition).getTargetList());
                        }
                    }
                    boolean allValue = valueExpr instanceof SQLValuableExpr;
                    if (allValue) {
                        for (final SQLExpr filterValue : filterValueExprList) {
                            if (!(filterValue instanceof SQLValuableExpr)) {
                                allValue = false;
                                break;
                            }
                        }
                    }
                    if (allValue) {
                        final Object setValue = ((SQLValuableExpr)valueExpr).getValue();
                        List<Object> filterValues = new ArrayList<Object>(filterValueExprList.size());
                        for (final SQLExpr expr : filterValueExprList) {
                            filterValues.add(((SQLValuableExpr)expr).getValue());
                        }
                        filterValues = new ArrayList<Object>(new HashSet<Object>(filterValues));
                        final boolean validate = updateCheckHandler.check(tableName, checkColumn, setValue, filterValues);
                        if (!validate) {
                            visitor.addViolation(new IllegalSQLObjectViolation(9000, "update check failed.", visitor.toSQL(x)));
                        }
                    }
                    else {
                        visitor.addWallUpdateCheckItem(new WallUpdateCheckItem(tableName, checkColumn, valueExpr, filterValueExprList));
                    }
                }
            }
        }
        checkUpdateForMultiTenant(visitor, x);
    }
    
    public static Object getValue(final WallVisitor visitor, final SQLBinaryOpExprGroup x) {
        final List<SQLExpr> groupList = x.getItems();
        if (x.getOperator() == SQLBinaryOperator.BooleanOr) {
            return getValue_or(visitor, groupList);
        }
        if (x.getOperator() == SQLBinaryOperator.BooleanAnd) {
            return getValue_and(visitor, groupList);
        }
        return null;
    }
    
    public static Object getValue(final WallVisitor visitor, final SQLBinaryOpExpr x) {
        if (x.getOperator() == SQLBinaryOperator.BooleanOr) {
            final List<SQLExpr> groupList = SQLBinaryOpExpr.split(x);
            return getValue_or(visitor, groupList);
        }
        if (x.getOperator() == SQLBinaryOperator.BooleanAnd) {
            final List<SQLExpr> groupList = SQLBinaryOpExpr.split(x);
            return getValue_and(visitor, groupList);
        }
        final boolean checkCondition = visitor != null && (!visitor.getConfig().isConstArithmeticAllow() || !visitor.getConfig().isConditionOpBitwseAllow() || !visitor.getConfig().isConditionOpXorAllow());
        if (x.getLeft() instanceof SQLName) {
            if (x.getRight() instanceof SQLName) {
                if (x.getLeft().toString().equalsIgnoreCase(x.getRight().toString())) {
                    switch (x.getOperator()) {
                        case Equality:
                        case Like: {
                            return Boolean.TRUE;
                        }
                        case NotEqual:
                        case GreaterThan:
                        case GreaterThanOrEqual:
                        case LessThan:
                        case LessThanOrEqual:
                        case LessThanOrGreater:
                        case NotLike: {
                            return Boolean.FALSE;
                        }
                    }
                }
            }
            else if (!checkCondition) {
                switch (x.getOperator()) {
                    case Equality:
                    case NotEqual:
                    case GreaterThan:
                    case GreaterThanOrEqual:
                    case LessThan:
                    case LessThanOrEqual:
                    case LessThanOrGreater: {
                        return null;
                    }
                }
            }
        }
        if (x.getLeft() instanceof SQLValuableExpr && x.getRight() instanceof SQLValuableExpr) {
            final Object leftValue = ((SQLValuableExpr)x.getLeft()).getValue();
            final Object rightValue = ((SQLValuableExpr)x.getRight()).getValue();
            if (x.getOperator() == SQLBinaryOperator.Equality) {
                final boolean evalValue = SQLEvalVisitorUtils.eq(leftValue, rightValue);
                x.putAttribute("eval.value", evalValue);
                return evalValue;
            }
            if (x.getOperator() == SQLBinaryOperator.NotEqual) {
                final boolean evalValue = SQLEvalVisitorUtils.eq(leftValue, rightValue);
                x.putAttribute("eval.value", !evalValue);
                return !evalValue;
            }
        }
        final Object leftResult = getValue(visitor, x.getLeft());
        final Object rightResult = getValue(visitor, x.getRight());
        if (x.getOperator() == SQLBinaryOperator.Like && leftResult instanceof String && leftResult.equals(rightResult)) {
            addViolation(visitor, 2108, "same const like", x);
        }
        if (x.getOperator() == SQLBinaryOperator.Like || x.getOperator() == SQLBinaryOperator.NotLike) {
            final WallContext context = WallContext.current();
            if (context != null && (rightResult instanceof Number || leftResult instanceof Number)) {
                context.incrementLikeNumberWarnings();
            }
        }
        DbType dbType = null;
        final WallContext wallContext = WallContext.current();
        if (wallContext != null) {
            dbType = wallContext.getDbType();
        }
        return eval(visitor, dbType, x, Collections.emptyList());
    }
    
    private static Object getValue_or(final WallVisitor visitor, final List<SQLExpr> groupList) {
        boolean allFalse = true;
        for (int i = groupList.size() - 1; i >= 0; --i) {
            final SQLExpr item = groupList.get(i);
            final Object result = getValue(visitor, item);
            final Boolean booleanVal = SQLEvalVisitorUtils.castToBoolean(result);
            if (booleanVal != null && booleanVal) {
                final WallConditionContext wallContext = getWallConditionContext();
                if (wallContext != null && !isFirst(item)) {
                    wallContext.setPartAlwayTrue(true);
                }
                return true;
            }
            if (booleanVal == null) {
                allFalse = false;
                break;
            }
        }
        if (allFalse) {
            return false;
        }
        return null;
    }
    
    private static Object getValue_and(final WallVisitor visitor, final List<SQLExpr> groupList) {
        int dalConst = 0;
        Boolean allTrue = Boolean.TRUE;
        for (int i = groupList.size() - 1; i >= 0; --i) {
            final SQLExpr item = groupList.get(i);
            final Object result = getValue(visitor, item);
            final Boolean booleanVal = SQLEvalVisitorUtils.castToBoolean(result);
            if (Boolean.TRUE == booleanVal) {
                final WallConditionContext wallContext = getWallConditionContext();
                if (wallContext != null && !isFirst(item)) {
                    wallContext.setPartAlwayTrue(true);
                }
                ++dalConst;
            }
            else if (Boolean.FALSE == booleanVal) {
                final WallConditionContext wallContext = getWallConditionContext();
                if (wallContext != null && !isFirst(item)) {
                    wallContext.setPartAlwayFalse(true);
                }
                allTrue = Boolean.FALSE;
                ++dalConst;
            }
            else {
                if (allTrue != Boolean.FALSE) {
                    allTrue = null;
                }
                dalConst = 0;
            }
            if (dalConst == 2 && visitor != null && !visitor.getConfig().isConditionDoubleConstAllow()) {
                addViolation(visitor, 2107, "double const condition", item);
            }
        }
        if (Boolean.TRUE == allTrue) {
            return true;
        }
        if (Boolean.FALSE == allTrue) {
            return false;
        }
        return null;
    }
    
    public static SQLExpr getFirst(final SQLExpr x) {
        if (x instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binary = (SQLBinaryOpExpr)x;
            if (binary.getOperator() == SQLBinaryOperator.BooleanAnd || binary.getOperator() == SQLBinaryOperator.BooleanOr) {
                return getFirst(((SQLBinaryOpExpr)x).getLeft());
            }
        }
        return x;
    }
    
    public static List<SQLExpr> getParts(final SQLExpr x) {
        List<SQLExpr> exprs = new ArrayList<SQLExpr>();
        exprs.add(x);
        while (true) {
            final List<SQLExpr> tmp = partExpr(exprs);
            if (tmp.size() == exprs.size()) {
                break;
            }
            exprs = tmp;
        }
        return exprs;
    }
    
    public static List<SQLExpr> partExpr(final List<SQLExpr> exprs) {
        final List<SQLExpr> partList = new ArrayList<SQLExpr>();
        for (final SQLExpr x : exprs) {
            if (x instanceof SQLBinaryOpExpr) {
                final SQLBinaryOpExpr binary = (SQLBinaryOpExpr)x;
                if (binary.getOperator() == SQLBinaryOperator.BooleanAnd || binary.getOperator() == SQLBinaryOperator.BooleanOr) {
                    partList.add(((SQLBinaryOpExpr)x).getLeft());
                    partList.add(((SQLBinaryOpExpr)x).getRight());
                    continue;
                }
            }
            partList.add(x);
        }
        return partList;
    }
    
    public static boolean isFirst(SQLObject x) {
        if (x == null) {
            return true;
        }
        while (true) {
            final SQLObject parent = x.getParent();
            if (!(parent instanceof SQLExpr)) {
                return true;
            }
            if (parent instanceof SQLBinaryOpExprGroup && x != ((SQLBinaryOpExprGroup)parent).getItems().get(0)) {
                return false;
            }
            if (parent instanceof SQLBinaryOpExpr) {
                final SQLBinaryOpExpr binaryExpr = (SQLBinaryOpExpr)parent;
                if (x == binaryExpr.getRight()) {
                    return false;
                }
            }
            x = parent;
        }
    }
    
    private static boolean hasWhere(final SQLSelectQuery selectQuery) {
        if (selectQuery instanceof SQLSelectQueryBlock) {
            return ((SQLSelectQueryBlock)selectQuery).getWhere() != null;
        }
        if (selectQuery instanceof SQLUnionQuery) {
            final SQLUnionQuery union = (SQLUnionQuery)selectQuery;
            return hasWhere(union.getLeft()) || hasWhere(union.getRight());
        }
        return false;
    }
    
    public static boolean checkSqlExpr(final SQLExpr x) {
        if (x == null) {
            return false;
        }
        SQLObject obj = x;
        while (true) {
            final SQLObject parent = obj.getParent();
            if (parent == null) {
                return false;
            }
            if (parent instanceof SQLSelectGroupByClause) {
                return true;
            }
            if (parent instanceof SQLOrderBy) {
                return true;
            }
            if (parent instanceof SQLLimit) {
                return true;
            }
            if (parent instanceof MySqlOrderingExpr) {
                return true;
            }
            obj = parent;
        }
    }
    
    public static boolean isWhereOrHaving(SQLObject x) {
        if (x == null) {
            return false;
        }
        while (true) {
            final SQLObject parent = x.getParent();
            if (parent == null) {
                return false;
            }
            if (parent instanceof SQLJoinTableSource) {
                final SQLJoinTableSource joinTableSource = (SQLJoinTableSource)parent;
                if (joinTableSource.getCondition() == x) {
                    return true;
                }
            }
            if (parent instanceof SQLUnionQuery) {
                final SQLUnionQuery union = (SQLUnionQuery)parent;
                if (union.getRight() == x && hasWhere(union.getLeft())) {
                    return true;
                }
            }
            if (parent instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock query = (SQLSelectQueryBlock)parent;
                if (query.getWhere() == x) {
                    return true;
                }
            }
            if (parent instanceof SQLDeleteStatement) {
                final SQLDeleteStatement delete = (SQLDeleteStatement)parent;
                return delete.getWhere() == x;
            }
            if (parent instanceof SQLUpdateStatement) {
                final SQLUpdateStatement update = (SQLUpdateStatement)parent;
                return update.getWhere() == x;
            }
            if (parent instanceof SQLSelectGroupByClause) {
                final SQLSelectGroupByClause groupBy = (SQLSelectGroupByClause)parent;
                return x == groupBy.getHaving();
            }
            x = parent;
        }
    }
    
    public static WallConditionContext getWallConditionContext() {
        return WallVisitorUtils.wallConditionContextLocal.get();
    }
    
    public static WallTopStatementContext getWallTopStatementContext() {
        return WallVisitorUtils.wallTopStatementContextLocal.get();
    }
    
    public static void clearWallTopStatementContext() {
        WallVisitorUtils.wallTopStatementContextLocal.set(null);
    }
    
    public static void initWallTopStatementContext() {
        WallVisitorUtils.wallTopStatementContextLocal.set(new WallTopStatementContext());
    }
    
    public static Object getConditionValue(final WallVisitor visitor, final SQLExpr x, final boolean alwayTrueCheck) {
        final WallConditionContext old = WallVisitorUtils.wallConditionContextLocal.get();
        try {
            WallVisitorUtils.wallConditionContextLocal.set(new WallConditionContext());
            final Object value = getValue(visitor, x);
            final WallConditionContext current = WallVisitorUtils.wallConditionContextLocal.get();
            final WallContext context = WallContext.current();
            if (context != null && (current.hasPartAlwayTrue() || Boolean.TRUE == value) && !isFirst(x)) {
                context.incrementWarnings();
            }
            if (current.hasPartAlwayTrue() && !visitor.getConfig().isConditionAndAlwayTrueAllow()) {
                addViolation(visitor, 2100, "part alway true condition not allow", x);
            }
            if (current.hasPartAlwayFalse() && !visitor.getConfig().isConditionAndAlwayFalseAllow()) {
                addViolation(visitor, 2113, "part alway false condition not allow", x);
            }
            if (current.hasConstArithmetic() && !visitor.getConfig().isConstArithmeticAllow()) {
                addViolation(visitor, 2101, "const arithmetic not allow", x);
            }
            if (current.hasXor() && !visitor.getConfig().isConditionOpXorAllow()) {
                addViolation(visitor, 2102, "xor not allow", x);
            }
            if (current.hasBitwise() && !visitor.getConfig().isConditionOpBitwseAllow()) {
                addViolation(visitor, 2103, "bitwise operator not allow", x);
            }
            return value;
        }
        finally {
            WallVisitorUtils.wallConditionContextLocal.set(old);
        }
    }
    
    public static Object getValueFromAttributes(final WallVisitor visitor, final SQLObject sqlObject) {
        if (sqlObject == null) {
            return null;
        }
        if (visitor != null && visitor.getConfig().isConditionLikeTrueAllow() && sqlObject.getAttributes().containsKey("hasTrueLike")) {
            return null;
        }
        return sqlObject.getAttribute("eval.value");
    }
    
    public static Object getValue(final SQLExpr x) {
        return getValue(null, x);
    }
    
    public static Object getValue(final WallVisitor visitor, final SQLExpr x) {
        if (x != null && x.containsAttribute("eval.value")) {
            return getValueFromAttributes(visitor, x);
        }
        if (x instanceof SQLBinaryOpExpr) {
            return getValue(visitor, (SQLBinaryOpExpr)x);
        }
        if (x instanceof SQLBinaryOpExprGroup) {
            return getValue(visitor, (SQLBinaryOpExprGroup)x);
        }
        if (x instanceof SQLBooleanExpr) {
            return ((SQLBooleanExpr)x).getBooleanValue();
        }
        if (x instanceof SQLNumericLiteralExpr) {
            return ((SQLNumericLiteralExpr)x).getNumber();
        }
        if (x instanceof SQLCharExpr) {
            return ((SQLCharExpr)x).getText();
        }
        if (x instanceof SQLNCharExpr) {
            return ((SQLNCharExpr)x).getText();
        }
        if (x instanceof SQLNotExpr) {
            final Object result = getValue(visitor, ((SQLNotExpr)x).getExpr());
            if (result instanceof Boolean) {
                return !(boolean)result;
            }
        }
        if (x instanceof SQLQueryExpr) {
            if (isSimpleCountTableSource(visitor, ((SQLQueryExpr)x).getSubQuery())) {
                return 1;
            }
            if (isSimpleCaseTableSource(visitor, ((SQLQueryExpr)x).getSubQuery())) {
                final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)((SQLQueryExpr)x).getSubQuery().getQuery();
                final SQLCaseExpr caseExpr = (SQLCaseExpr)queryBlock.getSelectList().get(0).getExpr();
                final Object result2 = getValue(caseExpr);
                if (visitor != null && !visitor.getConfig().isCaseConditionConstAllow()) {
                    boolean leftIsName = false;
                    if (x.getParent() instanceof SQLBinaryOpExpr) {
                        final SQLExpr left = ((SQLBinaryOpExpr)x.getParent()).getLeft();
                        if (left instanceof SQLName) {
                            leftIsName = true;
                        }
                    }
                    if (!leftIsName && result2 != null) {
                        addViolation(visitor, 2109, "const case condition", caseExpr);
                    }
                }
                return result2;
            }
        }
        DbType dbType = null;
        if (visitor != null) {
            dbType = visitor.getDbType();
        }
        if (x instanceof SQLMethodInvokeExpr || x instanceof SQLBetweenExpr || x instanceof SQLInListExpr || x instanceof SQLUnaryExpr) {
            return eval(visitor, dbType, x, Collections.emptyList());
        }
        if (x instanceof SQLCaseExpr) {
            if (visitor != null && !visitor.getConfig().isCaseConditionConstAllow()) {
                final SQLCaseExpr caseExpr = (SQLCaseExpr)x;
                boolean leftIsName2 = false;
                if (caseExpr.getParent() instanceof SQLBinaryOpExpr) {
                    final SQLExpr left2 = ((SQLBinaryOpExpr)caseExpr.getParent()).getLeft();
                    if (left2 instanceof SQLName) {
                        leftIsName2 = true;
                    }
                }
                if (!leftIsName2 && caseExpr.getValueExpr() == null && caseExpr.getItems().size() > 0) {
                    final SQLCaseExpr.Item item = caseExpr.getItems().get(0);
                    final Object conditionVal = getValue(visitor, item.getConditionExpr());
                    final Object itemVal = getValue(visitor, item.getValueExpr());
                    if (conditionVal instanceof Boolean && itemVal != null) {
                        addViolation(visitor, 2109, "const case condition", caseExpr);
                    }
                }
            }
            return eval(visitor, dbType, x, Collections.emptyList());
        }
        return null;
    }
    
    public static Object eval(final WallVisitor wallVisitor, final DbType dbType, final SQLObject sqlObject, final List<Object> parameters) {
        final SQLEvalVisitor visitor = SQLEvalVisitorUtils.createEvalVisitor(dbType);
        visitor.setParameters(parameters);
        visitor.registerFunction("rand", Nil.instance);
        visitor.registerFunction("sin", Nil.instance);
        visitor.registerFunction("cos", Nil.instance);
        visitor.registerFunction("asin", Nil.instance);
        visitor.registerFunction("acos", Nil.instance);
        sqlObject.accept(visitor);
        if (sqlObject instanceof SQLNumericLiteralExpr) {
            return ((SQLNumericLiteralExpr)sqlObject).getNumber();
        }
        return getValueFromAttributes(wallVisitor, sqlObject);
    }
    
    public static boolean isSimpleCountTableSource(final WallVisitor visitor, final SQLTableSource tableSource) {
        if (!(tableSource instanceof SQLSubqueryTableSource)) {
            return false;
        }
        final SQLSubqueryTableSource subQuery = (SQLSubqueryTableSource)tableSource;
        return isSimpleCountTableSource(visitor, subQuery.getSelect());
    }
    
    public static boolean isSimpleCountTableSource(final WallVisitor visitor, final SQLSelect select) {
        final SQLSelectQuery query = select.getQuery();
        if (query instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
            boolean allawTrueWhere = false;
            if (queryBlock.getWhere() == null) {
                allawTrueWhere = true;
            }
            else {
                final Object whereValue = getValue(visitor, queryBlock.getWhere());
                if (whereValue instanceof Boolean) {
                    if (!(boolean)whereValue) {
                        return false;
                    }
                    allawTrueWhere = true;
                }
            }
            boolean simpleCount = false;
            if (queryBlock.getSelectList().size() == 1) {
                final SQLExpr selectItemExpr = queryBlock.getSelectList().get(0).getExpr();
                if (selectItemExpr instanceof SQLAggregateExpr && ((SQLAggregateExpr)selectItemExpr).methodNameHashCode64() == FnvHash.Constants.COUNT) {
                    simpleCount = true;
                }
            }
            if (allawTrueWhere && simpleCount) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isSimpleCaseTableSource(final WallVisitor visitor, final SQLSelect select) {
        final SQLSelectQuery query = select.getQuery();
        if (query instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
            if (queryBlock.getWhere() != null) {
                final Object whereValue = getValue(visitor, queryBlock.getWhere());
                if (whereValue instanceof Boolean && !(boolean)whereValue) {
                    return false;
                }
            }
            boolean simpleCase = false;
            if (queryBlock.getSelectList().size() == 1) {
                final SQLExpr selectItemExpr = queryBlock.getSelectList().get(0).getExpr();
                if (selectItemExpr instanceof SQLCaseExpr) {
                    simpleCase = true;
                }
            }
            if (simpleCase) {
                return true;
            }
        }
        return false;
    }
    
    public static void checkFunctionInTableSource(final WallVisitor visitor, final SQLMethodInvokeExpr x) {
        final WallTopStatementContext topStatementContext = WallVisitorUtils.wallTopStatementContextLocal.get();
        if (topStatementContext != null && (topStatementContext.fromSysSchema || topStatementContext.fromSysTable)) {
            return;
        }
        checkSchema(visitor, x.getOwner());
        final String methodName = x.getMethodName().toLowerCase();
        if (!visitor.getProvider().checkDenyTable(methodName) && (isTopStatementWithTableSource(x) || isFirstSelectTableSource(x)) && topStatementContext != null) {
            topStatementContext.setFromSysSchema(Boolean.TRUE);
            clearViolation(visitor);
        }
    }
    
    public static void checkFunction(final WallVisitor visitor, final SQLMethodInvokeExpr x) {
        final WallTopStatementContext topStatementContext = WallVisitorUtils.wallTopStatementContextLocal.get();
        if (topStatementContext != null && (topStatementContext.fromSysSchema || topStatementContext.fromSysTable)) {
            return;
        }
        final String methodName = x.getMethodName().toLowerCase();
        final SQLExpr owner = x.getOwner();
        final WallProvider provider = visitor.getProvider();
        if (!visitor.getConfig().isFunctionCheck()) {
            return;
        }
        final WallContext context = WallContext.current();
        if (context != null) {
            context.incrementFunctionInvoke(methodName);
        }
        if (owner != null) {
            final String fullName = (owner.toString() + '.' + methodName).toLowerCase();
            if (provider.getConfig().getPermitFunctions().contains(fullName)) {
                return;
            }
            checkSchema(visitor, owner);
        }
        if (provider.checkDenyFunction(methodName)) {
            return;
        }
        final boolean isTopNoneFrom = isTopNoneFromSelect(visitor, x);
        if (isTopNoneFrom) {
            return;
        }
        if (isTopFromDenySchema(visitor, x)) {
            return;
        }
        final boolean isShow = x.getParent() instanceof MySqlShowGrantsStatement;
        if (isShow) {
            return;
        }
        if (isWhereOrHaving(x) || checkSqlExpr(x)) {
            addViolation(visitor, 2001, "deny function : " + methodName, x);
        }
    }
    
    public static SQLSelectQueryBlock getQueryBlock(final SQLObject x) {
        if (x == null) {
            return null;
        }
        if (x instanceof SQLSelectQueryBlock) {
            return (SQLSelectQueryBlock)x;
        }
        final SQLObject parent = x.getParent();
        if (parent instanceof SQLExpr) {
            return getQueryBlock(parent);
        }
        if (parent instanceof SQLSelectItem) {
            return getQueryBlock(parent);
        }
        if (parent instanceof SQLSelectQueryBlock) {
            return (SQLSelectQueryBlock)parent;
        }
        return null;
    }
    
    public static boolean isTopNoneFromSelect(final WallVisitor visitor, SQLObject x) {
        while (x.getParent() instanceof SQLExpr || x.getParent() instanceof SQLCaseExpr.Item) {
            x = x.getParent();
        }
        if (!(x.getParent() instanceof SQLSelectItem)) {
            return false;
        }
        final SQLSelectItem item = (SQLSelectItem)x.getParent();
        if (!(item.getParent() instanceof SQLSelectQueryBlock)) {
            return false;
        }
        final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)item.getParent();
        if (!queryBlockFromIsNull(visitor, queryBlock)) {
            return false;
        }
        if (!(queryBlock.getParent() instanceof SQLSelect)) {
            return false;
        }
        final SQLSelect select = (SQLSelect)queryBlock.getParent();
        if (!(select.getParent() instanceof SQLSelectStatement)) {
            return false;
        }
        final SQLSelectStatement stmt = (SQLSelectStatement)select.getParent();
        return stmt.getParent() == null;
    }
    
    private static boolean isTopFromDenySchema(final WallVisitor visitor, final SQLMethodInvokeExpr x) {
        SQLObject parent;
        for (parent = x.getParent(); parent instanceof SQLExpr || parent instanceof SQLCaseExpr.Item || parent instanceof SQLSelectItem; parent = parent.getParent()) {}
        if (!(parent instanceof SQLSelectQueryBlock)) {
            return false;
        }
        final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)parent;
        if (!(queryBlock.getParent() instanceof SQLSelect)) {
            return false;
        }
        final SQLSelect select = (SQLSelect)queryBlock.getParent();
        if (!(select.getParent() instanceof SQLSelectStatement)) {
            return false;
        }
        final SQLSelectStatement stmt = (SQLSelectStatement)select.getParent();
        if (stmt.getParent() != null) {
            return false;
        }
        final SQLTableSource from = queryBlock.getFrom();
        if (from instanceof SQLExprTableSource) {
            final SQLExpr fromExpr = ((SQLExprTableSource)from).getExpr();
            if (fromExpr instanceof SQLName) {
                final String fromTableName = fromExpr.toString();
                return visitor.isDenyTable(fromTableName);
            }
        }
        return false;
    }
    
    private static boolean checkSchema(final WallVisitor visitor, final SQLExpr x) {
        final WallTopStatementContext topStatementContext = WallVisitorUtils.wallTopStatementContextLocal.get();
        if (topStatementContext != null && (topStatementContext.fromSysSchema || topStatementContext.fromSysTable)) {
            return true;
        }
        if (x instanceof SQLName) {
            String owner = ((SQLName)x).getSimpleName();
            owner = form(owner);
            if (isInTableSource(x) && !visitor.getProvider().checkDenySchema(owner)) {
                if (!isTopStatementWithTableSource(x) && !isFirstSelectTableSource(x) && !isFirstInSubQuery(x)) {
                    SQLObject parent;
                    for (parent = x.getParent(); parent != null && !(parent instanceof SQLStatement); parent = parent.getParent()) {}
                    boolean sameToTopSelectSchema = false;
                    if (parent instanceof SQLSelectStatement) {
                        final SQLSelectStatement selectStmt = (SQLSelectStatement)parent;
                        final SQLSelectQuery query = selectStmt.getSelect().getQuery();
                        if (query instanceof SQLSelectQueryBlock) {
                            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
                            SQLTableSource from;
                            for (from = queryBlock.getFrom(); from instanceof SQLJoinTableSource; from = ((SQLJoinTableSource)from).getLeft()) {}
                            if (from instanceof SQLExprTableSource) {
                                final SQLExpr expr = ((SQLExprTableSource)from).getExpr();
                                if (expr instanceof SQLPropertyExpr) {
                                    final SQLExpr schemaExpr = ((SQLPropertyExpr)expr).getOwner();
                                    if (schemaExpr instanceof SQLIdentifierExpr) {
                                        String schema = ((SQLIdentifierExpr)schemaExpr).getName();
                                        schema = form(schema);
                                        if (schema.equalsIgnoreCase(owner)) {
                                            sameToTopSelectSchema = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (!sameToTopSelectSchema) {
                        addViolation(visitor, 2002, "deny schema : " + owner, x);
                    }
                }
                else if (topStatementContext != null) {
                    topStatementContext.setFromSysSchema(Boolean.TRUE);
                    clearViolation(visitor);
                }
                return true;
            }
            if (visitor.getConfig().isDenyObjects(owner)) {
                addViolation(visitor, 2005, "deny object : " + owner, x);
                return true;
            }
        }
        return !(x instanceof SQLPropertyExpr) || checkSchema(visitor, ((SQLPropertyExpr)x).getOwner());
    }
    
    private static boolean isInTableSource(SQLObject x) {
        while (x instanceof SQLExpr) {
            x = x.getParent();
        }
        return x instanceof SQLExprTableSource;
    }
    
    private static boolean isFirstInSubQuery(SQLObject x) {
        while (x instanceof SQLExpr) {
            x = x.getParent();
        }
        if (!(x instanceof SQLExprTableSource)) {
            return false;
        }
        SQLSelect sqlSelect = null;
        for (SQLObject parent = x.getParent(); parent != null; parent = x.getParent()) {
            if (parent instanceof SQLSelect) {
                sqlSelect = (SQLSelect)parent;
                break;
            }
            x = parent;
        }
        if (sqlSelect == null) {
            return false;
        }
        SQLObject parent = sqlSelect.getParent();
        if (!(parent instanceof SQLInSubQueryExpr) || !isFirst(parent)) {
            return false;
        }
        final SQLInSubQueryExpr sqlInSubQueryExpr = (SQLInSubQueryExpr)parent;
        if (!(sqlInSubQueryExpr.getParent() instanceof SQLSelectQueryBlock)) {
            return false;
        }
        final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)sqlInSubQueryExpr.getParent();
        if (!(queryBlock.getParent() instanceof SQLSelect)) {
            return false;
        }
        final SQLSelect select = (SQLSelect)queryBlock.getParent();
        if (!(select.getParent() instanceof SQLSelectStatement)) {
            return false;
        }
        final SQLSelectStatement stmt = (SQLSelectStatement)select.getParent();
        return stmt.getParent() == null;
    }
    
    private static boolean isFirstSelectTableSource(SQLObject x) {
        while (x instanceof SQLExpr) {
            x = x.getParent();
        }
        if (!(x instanceof SQLExprTableSource)) {
            return false;
        }
        SQLSelectQueryBlock queryBlock = null;
        SQLObject parent;
        for (parent = x.getParent(); parent != null; parent = x.getParent()) {
            if (parent instanceof SQLSelectQueryBlock) {
                queryBlock = (SQLSelectQueryBlock)parent;
                break;
            }
            x = parent;
        }
        if (queryBlock == null) {
            return false;
        }
        boolean isWhereQueryExpr = false;
        boolean isSelectItem = false;
        do {
            x = parent;
            parent = parent.getParent();
            if (parent instanceof SQLUnionQuery) {
                final SQLUnionQuery union = (SQLUnionQuery)parent;
                if (union.getRight() == x && hasTableSource(union.getLeft())) {
                    return false;
                }
                continue;
            }
            else if (parent instanceof SQLQueryExpr || parent instanceof SQLInSubQueryExpr || parent instanceof SQLExistsExpr) {
                isWhereQueryExpr = isWhereOrHaving(parent);
            }
            else if (parent instanceof SQLSelectItem) {
                isSelectItem = true;
            }
            else {
                if ((isWhereQueryExpr || isSelectItem) && parent instanceof SQLSelectQueryBlock && hasTableSource((SQLSelectQuery)parent)) {
                    return false;
                }
                continue;
            }
        } while (parent != null);
        return true;
    }
    
    private static boolean hasTableSource(final SQLSelectQuery x) {
        if (x instanceof SQLUnionQuery) {
            final SQLUnionQuery union = (SQLUnionQuery)x;
            return hasTableSource(union.getLeft()) || hasTableSource(union.getRight());
        }
        return x instanceof SQLSelectQueryBlock && hasTableSource(((SQLSelectQueryBlock)x).getFrom());
    }
    
    private static boolean hasTableSource(final SQLTableSource x) {
        if (x == null) {
            return false;
        }
        if (x instanceof SQLExprTableSource) {
            final SQLExpr fromExpr = ((SQLExprTableSource)x).getExpr();
            if (fromExpr instanceof SQLName) {
                String name = fromExpr.toString();
                name = form(name);
                if (name.equalsIgnoreCase("DUAL")) {
                    return false;
                }
            }
            return true;
        }
        if (x instanceof SQLJoinTableSource) {
            final SQLJoinTableSource join = (SQLJoinTableSource)x;
            return hasTableSource(join.getLeft()) || hasTableSource(join.getRight());
        }
        return x instanceof SQLSubqueryTableSource && hasTableSource(((SQLSubqueryTableSource)x).getSelect().getQuery());
    }
    
    private static boolean isTopStatementWithTableSource(SQLObject x) {
        while (x instanceof SQLExpr) {
            x = x.getParent();
        }
        if (x instanceof SQLExprTableSource) {
            x = x.getParent();
            if (x instanceof SQLStatement) {
                x = x.getParent();
                if (x == null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean isTopSelectItem(SQLObject x) {
        while (x.getParent() instanceof SQLExpr || x.getParent() instanceof SQLCaseExpr.Item) {
            x = x.getParent();
        }
        if (!(x.getParent() instanceof SQLSelectItem)) {
            return false;
        }
        final SQLSelectItem item = (SQLSelectItem)x.getParent();
        return isTopSelectStatement(item.getParent());
    }
    
    private static boolean isTopSelectStatement(final SQLObject x) {
        if (!(x instanceof SQLSelectQueryBlock)) {
            return false;
        }
        final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)x;
        if (!(queryBlock.getParent() instanceof SQLSelect)) {
            return false;
        }
        final SQLSelect select = (SQLSelect)queryBlock.getParent();
        if (!(select.getParent() instanceof SQLSelectStatement)) {
            return false;
        }
        final SQLSelectStatement stmt = (SQLSelectStatement)select.getParent();
        return stmt.getParent() == null;
    }
    
    public static boolean isTopSelectOutFile(final MySqlOutFileExpr x) {
        if (!(x.getParent() instanceof SQLExprTableSource)) {
            return false;
        }
        final SQLExprTableSource tableSource = (SQLExprTableSource)x.getParent();
        return isTopSelectStatement(tableSource.getParent());
    }
    
    public static boolean check(final WallVisitor visitor, final SQLExprTableSource x) {
        final WallTopStatementContext topStatementContext = WallVisitorUtils.wallTopStatementContextLocal.get();
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLPropertyExpr) {
            final boolean checkResult = checkSchema(visitor, ((SQLPropertyExpr)expr).getOwner());
            if (!checkResult) {
                return false;
            }
        }
        if (expr instanceof SQLName) {
            final String tableName = ((SQLName)expr).getSimpleName();
            final WallContext context = WallContext.current();
            if (context != null) {
                final WallSqlTableStat tableStat = context.getTableStat(tableName);
                if (tableStat != null) {
                    SQLObject parent;
                    for (parent = x.getParent(); parent instanceof SQLTableSource; parent = parent.getParent()) {}
                    if (parent instanceof SQLSelectQueryBlock) {
                        final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)parent;
                        if (x == queryBlock.getInto()) {
                            tableStat.incrementSelectIntoCount();
                        }
                        else {
                            tableStat.incrementSelectCount();
                        }
                    }
                    else if (parent instanceof SQLTruncateStatement) {
                        tableStat.incrementTruncateCount();
                    }
                    else if (parent instanceof SQLInsertStatement) {
                        tableStat.incrementInsertCount();
                    }
                    else if (parent instanceof SQLDeleteStatement) {
                        tableStat.incrementDeleteCount();
                    }
                    else if (parent instanceof SQLUpdateStatement) {
                        tableStat.incrementUpdateCount();
                    }
                    else if (parent instanceof SQLReplaceStatement) {
                        tableStat.incrementReplaceCount();
                    }
                }
            }
            if (topStatementContext != null && (topStatementContext.fromSysSchema || topStatementContext.fromSysTable)) {
                return true;
            }
            if (visitor.isDenyTable(tableName) && (topStatementContext == null || !topStatementContext.fromPermitTable())) {
                if (isTopStatementWithTableSource(x) || isFirstSelectTableSource(x)) {
                    if (topStatementContext != null) {
                        topStatementContext.setFromSysTable(Boolean.TRUE);
                        clearViolation(visitor);
                    }
                    return false;
                }
                final boolean isTopNoneFrom = isTopNoneFromSelect(visitor, x);
                if (isTopNoneFrom) {
                    return false;
                }
                addViolation(visitor, 2004, "deny table : " + tableName, x);
                return false;
            }
            else if (visitor.getConfig().getPermitTables().contains(tableName) && isFirstSelectTableSource(x)) {
                if (topStatementContext != null) {
                    topStatementContext.setFromPermitTable(Boolean.TRUE);
                }
                return false;
            }
        }
        return true;
    }
    
    private static void addViolation(final WallVisitor visitor, final int errorCode, final String message, final SQLObject x) {
        visitor.addViolation(new IllegalSQLObjectViolation(errorCode, message, visitor.toSQL(x)));
    }
    
    private static void clearViolation(final WallVisitor visitor) {
        visitor.getViolations().clear();
    }
    
    public static boolean checkUnion(final WallVisitor visitor, final SQLUnionQuery x) {
        if (x.getRelations().size() > 2) {
            for (int i = 0; i < x.getRelations().size(); ++i) {
                final SQLSelectQuery item = x.getRelations().get(i);
                if (!(item instanceof SQLSelectQueryBlock) || !queryBlockFromIsNull(visitor, item)) {
                    item.accept(visitor);
                }
            }
            return false;
        }
        final SQLUnionOperator operator = x.getOperator();
        final SQLSelectQuery left = x.getLeft();
        final SQLSelectQuery right = x.getRight();
        final boolean bracket = x.isParenthesized() && !(x.getParent() instanceof SQLUnionQueryTableSource);
        if (!bracket && left instanceof SQLUnionQuery && ((SQLUnionQuery)left).getOperator() == operator && !right.isParenthesized() && x.getOrderBy() == null) {
            SQLUnionQuery leftUnion = (SQLUnionQuery)left;
            final List<SQLSelectQuery> rights = new ArrayList<SQLSelectQuery>();
            rights.add(right);
            SQLSelectQuery leftLeft;
            SQLSelectQuery leftRight;
            while (true) {
                leftLeft = leftUnion.getLeft();
                leftRight = leftUnion.getRight();
                if (leftUnion.isParenthesized() || leftUnion.getOrderBy() != null || leftLeft.isParenthesized() || leftRight.isParenthesized() || !(leftLeft instanceof SQLUnionQuery) || ((SQLUnionQuery)leftLeft).getOperator() != operator) {
                    break;
                }
                rights.add(leftRight);
                leftUnion = (SQLUnionQuery)leftLeft;
            }
            rights.add(leftRight);
            rights.add(leftLeft);
            for (int j = rights.size() - 1; j >= 0; --j) {
                final SQLSelectQuery item2 = rights.get(j);
                if (!(item2 instanceof SQLSelectQueryBlock) || !queryBlockFromIsNull(visitor, item2)) {
                    item2.accept(visitor);
                }
            }
            return false;
        }
        if (x.getOperator() == SQLUnionOperator.MINUS && !visitor.getConfig().isMinusAllow()) {
            addViolation(visitor, 1008, "minus not allow", x);
            return true;
        }
        if (x.getOperator() == SQLUnionOperator.INTERSECT && !visitor.getConfig().isIntersectAllow()) {
            addViolation(visitor, 1008, "intersect not allow", x);
            return true;
        }
        if (!queryBlockFromIsNull(visitor, x.getLeft()) && queryBlockFromIsNull(visitor, x.getRight())) {
            boolean isTopUpdateStatement = false;
            boolean isTopInsertStatement = false;
            SQLObject selectParent;
            for (selectParent = x.getParent(); selectParent instanceof SQLSelectQuery || selectParent instanceof SQLJoinTableSource || selectParent instanceof SQLSubqueryTableSource || selectParent instanceof SQLSelect; selectParent = selectParent.getParent()) {}
            if (selectParent instanceof SQLUpdateStatement) {
                isTopUpdateStatement = true;
            }
            if (selectParent instanceof SQLInsertStatement) {
                isTopInsertStatement = true;
            }
            if (isTopUpdateStatement || isTopInsertStatement) {
                return true;
            }
            if (x.getLeft() instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock left2 = (SQLSelectQueryBlock)x.getLeft();
                final SQLTableSource tableSource = left2.getFrom();
                if (left2.getWhere() == null && tableSource != null && tableSource instanceof SQLExprTableSource) {
                    return true;
                }
            }
            final WallContext context = WallContext.current();
            if (context != null) {
                context.incrementUnionWarnings();
            }
            if (((x.getOperator() == SQLUnionOperator.UNION || x.getOperator() == SQLUnionOperator.UNION_ALL || x.getOperator() == SQLUnionOperator.DISTINCT) && visitor.getConfig().isSelectUnionCheck() && visitor.isSqlEndOfComment()) || (x.getOperator() == SQLUnionOperator.MINUS && visitor.getConfig().isSelectMinusCheck()) || (x.getOperator() == SQLUnionOperator.INTERSECT && visitor.getConfig().isSelectIntersectCheck()) || (x.getOperator() == SQLUnionOperator.EXCEPT && visitor.getConfig().isSelectExceptCheck())) {
                addViolation(visitor, 5000, x.getOperator().toString() + " query not contains 'from clause'", x);
            }
        }
        return true;
    }
    
    public static boolean queryBlockFromIsNull(final WallVisitor visitor, final SQLSelectQuery query) {
        return queryBlockFromIsNull(visitor, query, true);
    }
    
    public static boolean queryBlockFromIsNull(final WallVisitor visitor, final SQLSelectQuery query, final boolean checkSelectConst) {
        if (query instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
            final SQLTableSource from = queryBlock.getFrom();
            if (queryBlock.getSelectList().size() < 1) {
                return false;
            }
            if (from == null) {
                boolean itemIsConst = true;
                boolean itemHasAlias = false;
                for (final SQLSelectItem item : queryBlock.getSelectList()) {
                    if (item.getExpr() instanceof SQLIdentifierExpr || item.getExpr() instanceof SQLPropertyExpr) {
                        itemIsConst = false;
                        break;
                    }
                    if (item.getAlias() != null) {
                        itemHasAlias = true;
                        break;
                    }
                }
                return itemIsConst && !itemHasAlias;
            }
            if (from instanceof SQLExprTableSource) {
                final SQLExpr fromExpr = ((SQLExprTableSource)from).getExpr();
                if (fromExpr instanceof SQLName) {
                    String name = fromExpr.toString();
                    name = form(name);
                    if (name.equalsIgnoreCase("DUAL")) {
                        return true;
                    }
                }
            }
            if (queryBlock.getSelectList().size() == 1 && queryBlock.getSelectList().get(0).getExpr() instanceof SQLAllColumnExpr && from instanceof SQLSubqueryTableSource) {
                final SQLSelectQuery subQuery = ((SQLSubqueryTableSource)from).getSelect().getQuery();
                if (queryBlockFromIsNull(visitor, subQuery)) {
                    return true;
                }
            }
            if (checkSelectConst) {
                final SQLExpr where = queryBlock.getWhere();
                if (where != null) {
                    final Object whereValue = getValue(visitor, where);
                    if (Boolean.TRUE == whereValue) {
                        boolean allIsConst = true;
                        for (final SQLSelectItem item2 : queryBlock.getSelectList()) {
                            if (getValue(visitor, item2.getExpr()) == null) {
                                allIsConst = false;
                                break;
                            }
                        }
                        if (allIsConst) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static String form(String name) {
        if (name.startsWith("\"") && name.endsWith("\"")) {
            name = name.substring(1, name.length() - 1);
        }
        if (name.startsWith("'") && name.endsWith("'")) {
            name = name.substring(1, name.length() - 1);
        }
        if (name.startsWith("`") && name.endsWith("`")) {
            name = name.substring(1, name.length() - 1);
        }
        name = name.toLowerCase();
        return name;
    }
    
    public static void loadResource(final Set<String> names, String resource) {
        try {
            boolean hasResource = false;
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null) {
                final Enumeration<URL> e = Thread.currentThread().getContextClassLoader().getResources(resource);
                while (e.hasMoreElements()) {
                    final URL url = e.nextElement();
                    InputStream in = null;
                    try {
                        in = url.openStream();
                        readFromInputStream(names, in);
                        hasResource = true;
                    }
                    finally {
                        JdbcUtils.close(in);
                    }
                }
            }
            if (!hasResource) {
                if (!resource.startsWith("/")) {
                    resource = "/" + resource;
                }
                InputStream in2 = null;
                try {
                    in2 = WallVisitorUtils.class.getResourceAsStream(resource);
                    if (in2 != null) {
                        readFromInputStream(names, in2);
                    }
                }
                finally {
                    JdbcUtils.close(in2);
                }
            }
        }
        catch (IOException e2) {
            WallVisitorUtils.LOG.error("load oracle deny tables errror", e2);
        }
    }
    
    private static void readFromInputStream(final Set<String> names, final InputStream in) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (line.length() <= 0) {
                    continue;
                }
                line = line.toLowerCase();
                names.add(line);
            }
        }
        finally {
            JdbcUtils.close(reader);
        }
    }
    
    public static void preVisitCheck(final WallVisitor visitor, final SQLObject x) {
        final WallConfig config = visitor.getProvider().getConfig();
        if (!(x instanceof SQLStatement)) {
            return;
        }
        boolean allow = false;
        if (x instanceof SQLCommentStatement) {
            return;
        }
        String denyMessage;
        int errorCode;
        if (x instanceof SQLInsertStatement) {
            allow = config.isInsertAllow();
            denyMessage = "insert not allow";
            errorCode = 1004;
        }
        else if (x instanceof SQLSelectStatement) {
            allow = true;
            denyMessage = "select not allow";
            errorCode = 1002;
        }
        else if (x instanceof SQLDeleteStatement) {
            allow = config.isDeleteAllow();
            denyMessage = "delete not allow";
            errorCode = 1005;
        }
        else if (x instanceof SQLUpdateStatement) {
            allow = config.isUpdateAllow();
            denyMessage = "update not allow";
            errorCode = 1006;
        }
        else if (x instanceof OracleMultiInsertStatement || x instanceof OracleMultiInsertStatement.InsertIntoClause) {
            allow = true;
            denyMessage = "multi-insert not allow";
            errorCode = 1004;
        }
        else if (x instanceof SQLMergeStatement) {
            allow = config.isMergeAllow();
            denyMessage = "merge not allow";
            errorCode = 1009;
        }
        else if (x instanceof SQLCallStatement || x instanceof SQLServerExecStatement || x instanceof OracleExecuteImmediateStatement) {
            allow = config.isCallAllow();
            denyMessage = "call not allow";
            errorCode = 1300;
        }
        else if (x instanceof SQLTruncateStatement) {
            allow = config.isTruncateAllow();
            denyMessage = "truncate not allow";
            errorCode = 1100;
        }
        else if (x instanceof SQLCreateStatement) {
            allow = config.isCreateTableAllow();
            denyMessage = "create table not allow";
            errorCode = 1101;
        }
        else if (x instanceof MySqlRenameTableStatement) {
            allow = config.isRenameTableAllow();
            denyMessage = "rename table not allow";
            errorCode = 1105;
        }
        else if (x instanceof SQLAlterStatement) {
            allow = config.isAlterTableAllow();
            denyMessage = "alter table not allow";
            errorCode = 1102;
        }
        else if (x instanceof SQLDropStatement) {
            allow = config.isDropTableAllow();
            denyMessage = "drop table not allow";
            errorCode = 1103;
        }
        else if (x instanceof SQLSetStatement) {
            allow = config.isSetAllow();
            denyMessage = "set not allow";
            errorCode = 1200;
        }
        else if (x instanceof SQLReplaceStatement) {
            allow = config.isReplaceAllow();
            denyMessage = "replace not allow";
            errorCode = 1010;
        }
        else if (x instanceof SQLDescribeStatement || (x instanceof MySqlExplainStatement && ((MySqlExplainStatement)x).isDescribe())) {
            allow = config.isDescribeAllow();
            denyMessage = "describe not allow";
            errorCode = 1201;
        }
        else if (x instanceof SQLShowStatement) {
            allow = config.isShowAllow();
            denyMessage = "show not allow";
            errorCode = 1202;
        }
        else if (x instanceof SQLCommitStatement) {
            allow = config.isCommitAllow();
            denyMessage = "commit not allow";
            errorCode = 1301;
        }
        else if (x instanceof SQLRollbackStatement) {
            allow = config.isRollbackAllow();
            denyMessage = "rollback not allow";
            errorCode = 1302;
        }
        else if (x instanceof SQLUseStatement) {
            allow = config.isUseAllow();
            denyMessage = "use not allow";
            errorCode = 1203;
        }
        else if (x instanceof MySqlHintStatement) {
            allow = config.isHintAllow();
            denyMessage = "hint not allow";
            errorCode = 1400;
        }
        else if (x instanceof SQLLockTableStatement) {
            allow = config.isLockTableAllow();
            denyMessage = "lock table not allow";
            errorCode = 1106;
        }
        else if (x instanceof OracleLockTableStatement) {
            allow = config.isLockTableAllow();
            denyMessage = "lock table not allow";
            errorCode = 1106;
        }
        else if (x instanceof SQLStartTransactionStatement) {
            allow = config.isStartTransactionAllow();
            denyMessage = "start transaction not allow";
            errorCode = 1303;
        }
        else if (x instanceof SQLBlockStatement) {
            allow = config.isBlockAllow();
            denyMessage = "block statement not allow";
            errorCode = 1304;
        }
        else if (x instanceof SQLExplainStatement || x instanceof MySqlOptimizeStatement) {
            allow = true;
            errorCode = 0;
            denyMessage = null;
        }
        else {
            allow = config.isNoneBaseStatementAllow();
            errorCode = 1999;
            denyMessage = x.getClass() + " not allow";
        }
        if (!allow) {
            addViolation(visitor, errorCode, denyMessage, x);
        }
    }
    
    public static void check(final WallVisitor visitor, final SQLCommentHint x) {
        if (!visitor.getConfig().isHintAllow()) {
            addViolation(visitor, 2110, "hint not allow", x);
            return;
        }
        String text = x.getText();
        text = text.trim();
        if (text.startsWith("!")) {
            text = text.substring(1);
        }
        if (text.length() == 0) {
            return;
        }
        int pos;
        for (pos = 0; pos < text.length(); ++pos) {
            final char ch = text.charAt(pos);
            if (ch < '0' || ch > '9') {
                break;
            }
        }
        if (pos == 5) {
            text = text.substring(5);
            text = text.trim();
        }
        text = text.toUpperCase();
        boolean isWhite = false;
        for (final String hint : WallVisitorUtils.whiteHints) {
            if (text.equals(hint)) {
                isWhite = true;
                break;
            }
        }
        if (!isWhite && (text.startsWith("FORCE INDEX") || text.startsWith("IGNORE INDEX"))) {
            isWhite = true;
        }
        if (!isWhite && text.startsWith("SET")) {
            final SQLStatementParser parser = new MySqlStatementParser(text);
            final List<SQLStatement> statementList = parser.parseStatementList();
            if (statementList != null && statementList.size() > 0) {
                final SQLStatement statement = statementList.get(0);
                if (statement instanceof SQLSetStatement) {
                    isWhite = true;
                }
            }
        }
        if (!isWhite && visitor.getDbType() == DbType.oracle && text.startsWith("+")) {
            isWhite = true;
        }
        if (!isWhite) {
            addViolation(visitor, 2110, "hint not allow", x);
        }
    }
    
    public static void check(final WallVisitor visitor, final SQLJoinTableSource x) {
        final SQLExpr condition = x.getCondition();
        if (condition instanceof SQLName) {
            addViolation(visitor, 6000, "invalid join condition", x);
        }
    }
    
    static {
        LOG = LogFactory.getLog(WallVisitorUtils.class);
        whiteHints = new String[] { "LOCAL", "TEMPORARY", "SQL_NO_CACHE", "SQL_CACHE", "HIGH_PRIORITY", "LOW_PRIORITY", "STRAIGHT_JOIN", "SQL_BUFFER_RESULT", "SQL_BIG_RESULT", "SQL_SMALL_RESULT", "DELAYED" };
        WallVisitorUtils.wallConditionContextLocal = new ThreadLocal<WallConditionContext>();
        WallVisitorUtils.wallTopStatementContextLocal = new ThreadLocal<WallTopStatementContext>();
    }
    
    public static class WallTopStatementContext
    {
        private boolean fromSysTable;
        private boolean fromSysSchema;
        private boolean fromPermitTable;
        
        public WallTopStatementContext() {
            this.fromSysTable = false;
            this.fromSysSchema = false;
            this.fromPermitTable = false;
        }
        
        public boolean fromSysTable() {
            return this.fromSysTable;
        }
        
        public void setFromSysTable(final boolean fromSysTable) {
            this.fromSysTable = fromSysTable;
        }
        
        public boolean fromSysSchema() {
            return this.fromSysSchema;
        }
        
        public void setFromSysSchema(final boolean fromSysSchema) {
            this.fromSysSchema = fromSysSchema;
        }
        
        public boolean fromPermitTable() {
            return this.fromPermitTable;
        }
        
        public void setFromPermitTable(final boolean fromPermitTable) {
            this.fromPermitTable = fromPermitTable;
        }
    }
    
    public static class WallConditionContext
    {
        private boolean partAlwayTrue;
        private boolean partAlwayFalse;
        private boolean constArithmetic;
        private boolean xor;
        private boolean bitwise;
        
        public WallConditionContext() {
            this.partAlwayTrue = false;
            this.partAlwayFalse = false;
            this.constArithmetic = false;
            this.xor = false;
            this.bitwise = false;
        }
        
        public boolean hasPartAlwayTrue() {
            return this.partAlwayTrue;
        }
        
        public void setPartAlwayTrue(final boolean partAllowTrue) {
            this.partAlwayTrue = partAllowTrue;
        }
        
        public boolean hasPartAlwayFalse() {
            return this.partAlwayFalse;
        }
        
        public void setPartAlwayFalse(final boolean partAlwayFalse) {
            this.partAlwayFalse = partAlwayFalse;
        }
        
        public boolean hasConstArithmetic() {
            return this.constArithmetic;
        }
        
        public void setConstArithmetic(final boolean constArithmetic) {
            this.constArithmetic = constArithmetic;
        }
        
        public boolean hasXor() {
            return this.xor;
        }
        
        public void setXor(final boolean xor) {
            this.xor = xor;
        }
        
        public boolean hasBitwise() {
            return this.bitwise;
        }
        
        public void setBitwise(final boolean bitwise) {
            this.bitwise = bitwise;
        }
    }
}
