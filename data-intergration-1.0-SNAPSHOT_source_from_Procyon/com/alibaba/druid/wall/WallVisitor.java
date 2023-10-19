// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import com.alibaba.druid.sql.ast.statement.SQLShowCreateTableStatement;
import com.alibaba.druid.sql.ast.TDDLHint;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTriggerStatement;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.PagerUtils;
import com.alibaba.druid.wall.violation.IllegalSQLObjectViolation;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.wall.spi.WallVisitorUtils;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface WallVisitor extends SQLASTVisitor
{
    WallConfig getConfig();
    
    WallProvider getProvider();
    
    List<Violation> getViolations();
    
    void addViolation(final Violation p0);
    
    boolean isDenyTable(final String p0);
    
    default String toSQL(final SQLObject obj) {
        return SQLUtils.toSQLString(obj, this.getDbType());
    }
    
    boolean isSqlModified();
    
    void setSqlModified(final boolean p0);
    
    DbType getDbType();
    
    boolean isSqlEndOfComment();
    
    void setSqlEndOfComment(final boolean p0);
    
    void addWallUpdateCheckItem(final WallUpdateCheckItem p0);
    
    List<WallUpdateCheckItem> getUpdateCheckItems();
    
    default boolean visit(final SQLPropertyExpr x) {
        WallVisitorUtils.check(this, x);
        return true;
    }
    
    default boolean visit(final SQLInListExpr x) {
        WallVisitorUtils.check(this, x);
        return true;
    }
    
    default boolean visit(final SQLBinaryOpExpr x) {
        return WallVisitorUtils.check(this, x);
    }
    
    default boolean visit(final SQLMethodInvokeExpr x) {
        WallVisitorUtils.checkFunction(this, x);
        return true;
    }
    
    default boolean visit(final SQLSelectQueryBlock x) {
        WallVisitorUtils.checkSelelct(this, x);
        return true;
    }
    
    default boolean visit(final SQLSelectGroupByClause x) {
        WallVisitorUtils.checkHaving(this, x.getHaving());
        return true;
    }
    
    default boolean visit(final SQLSelectItem x) {
        WallVisitorUtils.check(this, x);
        return true;
    }
    
    default boolean visit(final SQLJoinTableSource x) {
        WallVisitorUtils.check(this, x);
        return true;
    }
    
    default boolean visit(final SQLCreateTableStatement x) {
        WallVisitorUtils.check(this, x);
        return true;
    }
    
    default boolean visit(final SQLAlterTableStatement x) {
        WallVisitorUtils.check(this, x);
        return true;
    }
    
    default boolean visit(final SQLDropTableStatement x) {
        WallVisitorUtils.check(this, x);
        return true;
    }
    
    default boolean visit(final SQLUpdateStatement x) {
        WallVisitorUtils.initWallTopStatementContext();
        WallVisitorUtils.checkUpdate(this, x);
        return true;
    }
    
    default void endVisit(final SQLUpdateStatement x) {
        WallVisitorUtils.clearWallTopStatementContext();
    }
    
    default boolean visit(final SQLInsertStatement x) {
        WallVisitorUtils.initWallTopStatementContext();
        WallVisitorUtils.checkInsert(this, x);
        return true;
    }
    
    default void endVisit(final SQLInsertStatement x) {
        WallVisitorUtils.clearWallTopStatementContext();
    }
    
    default boolean visit(final SQLDeleteStatement x) {
        WallVisitorUtils.checkDelete(this, x);
        return true;
    }
    
    default void preVisit(final SQLObject x) {
        WallVisitorUtils.preVisitCheck(this, x);
    }
    
    default boolean visit(final SQLSelectStatement x) {
        final WallConfig config = this.getConfig();
        if (!config.isSelectAllow()) {
            this.getViolations().add(new IllegalSQLObjectViolation(1002, "select not allow", this.toSQL(x)));
            return false;
        }
        WallVisitorUtils.initWallTopStatementContext();
        final int selectLimit = config.getSelectLimit();
        if (selectLimit >= 0) {
            final SQLSelect select = x.getSelect();
            PagerUtils.limit(select, this.getDbType(), 0, selectLimit, true);
            this.setSqlModified(true);
        }
        return true;
    }
    
    default void endVisit(final SQLSelectStatement x) {
        WallVisitorUtils.clearWallTopStatementContext();
    }
    
    default boolean visit(final SQLExprTableSource x) {
        WallVisitorUtils.check(this, x);
        return !(x.getExpr() instanceof SQLName);
    }
    
    default boolean visit(final SQLIdentifierExpr x) {
        final WallConfig config = this.getConfig();
        String name = x.getName();
        name = WallVisitorUtils.form(name);
        if (config.isVariantCheck() && config.getDenyVariants().contains(name)) {
            this.getViolations().add(new IllegalSQLObjectViolation(2003, "variable not allow : " + name, this.toSQL(x)));
        }
        return true;
    }
    
    default boolean visit(final SQLUnionQuery x) {
        return WallVisitorUtils.checkUnion(this, x);
    }
    
    default void endVisit(final SQLDeleteStatement x) {
        WallVisitorUtils.clearWallTopStatementContext();
    }
    
    default boolean visit(final SQLLimit x) {
        if (x.getRowCount() instanceof SQLNumericLiteralExpr) {
            final WallContext context = WallContext.current();
            final int rowCount = ((SQLNumericLiteralExpr)x.getRowCount()).getNumber().intValue();
            if (rowCount == 0) {
                if (context != null) {
                    context.incrementWarnings();
                }
                if (!this.getProvider().getConfig().isLimitZeroAllow()) {
                    this.getViolations().add(new IllegalSQLObjectViolation(2200, "limit row 0", this.toSQL(x)));
                }
            }
        }
        return true;
    }
    
    default boolean visit(final SQLCreateTriggerStatement x) {
        return false;
    }
    
    default boolean visit(final SQLSetStatement x) {
        return false;
    }
    
    default boolean visit(final SQLCallStatement x) {
        return false;
    }
    
    default boolean visit(final SQLCommentHint x) {
        if (x instanceof TDDLHint) {
            return false;
        }
        WallVisitorUtils.check(this, x);
        return true;
    }
    
    default boolean visit(final SQLShowCreateTableStatement x) {
        final String tableName = x.getName().getSimpleName();
        final WallContext context = WallContext.current();
        if (context != null) {
            final WallSqlTableStat tableStat = context.getTableStat(tableName);
            if (tableStat != null) {
                tableStat.incrementShowCount();
            }
        }
        return false;
    }
}
