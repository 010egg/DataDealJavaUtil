// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.wall.violation.IllegalSQLObjectViolation;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.DbType;
import com.alibaba.druid.wall.WallProvider;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.wall.WallVisitor;

public class MySqlWallVisitor extends WallVisitorBase implements WallVisitor, MySqlASTVisitor
{
    public MySqlWallVisitor(final WallProvider provider) {
        super(provider);
    }
    
    @Override
    public DbType getDbType() {
        return DbType.mysql;
    }
    
    @Override
    public boolean visit(final MySqlSelectQueryBlock x) {
        WallVisitorUtils.checkSelelct(this, x);
        return true;
    }
    
    @Override
    public boolean visit(final MySqlDeleteStatement x) {
        WallVisitorUtils.checkReadOnly(this, x.getFrom());
        return this.visit(x);
    }
    
    @Override
    public boolean visit(final MySqlUpdateStatement x) {
        return this.visit(x);
    }
    
    @Override
    public boolean visit(final MySqlInsertStatement x) {
        return this.visit(x);
    }
    
    @Override
    public boolean visit(final SQLIdentifierExpr x) {
        return true;
    }
    
    @Override
    public boolean visit(final SQLPropertyExpr x) {
        if (x.getOwner() instanceof SQLVariantRefExpr) {
            final SQLVariantRefExpr varExpr = (SQLVariantRefExpr)x.getOwner();
            final SQLObject parent = x.getParent();
            final String varName = varExpr.getName();
            if (varName.equalsIgnoreCase("@@session") || varName.equalsIgnoreCase("@@global")) {
                if (!(parent instanceof SQLSelectItem) && !(parent instanceof SQLAssignItem)) {
                    this.violations.add(new IllegalSQLObjectViolation(2003, "variable in condition not allow", this.toSQL(x)));
                    return false;
                }
                if (!this.checkVar(x.getParent(), x.getName())) {
                    final boolean isTop = WallVisitorUtils.isTopNoneFromSelect(this, x);
                    if (!isTop) {
                        boolean allow = true;
                        if (this.isDeny(varName) && (WallVisitorUtils.isWhereOrHaving(x) || WallVisitorUtils.checkSqlExpr(varExpr))) {
                            allow = false;
                        }
                        if (!allow) {
                            this.violations.add(new IllegalSQLObjectViolation(2003, "variable not allow : " + x.getName(), this.toSQL(x)));
                        }
                    }
                }
                return false;
            }
        }
        WallVisitorUtils.check(this, x);
        return true;
    }
    
    public boolean checkVar(final SQLObject parent, String varName) {
        if (varName == null) {
            return false;
        }
        if (varName.equals("?")) {
            return true;
        }
        if (!this.config.isVariantCheck()) {
            return true;
        }
        if (varName.startsWith("@@")) {
            if (!(parent instanceof SQLSelectItem) && !(parent instanceof SQLAssignItem)) {
                return false;
            }
            varName = varName.substring(2);
        }
        return this.config.getPermitVariants().contains(varName);
    }
    
    public boolean isDeny(String varName) {
        if (varName.startsWith("@@")) {
            varName = varName.substring(2);
        }
        varName = varName.toLowerCase();
        return this.config.getDenyVariants().contains(varName);
    }
    
    @Override
    public boolean visit(final SQLVariantRefExpr x) {
        final String varName = x.getName();
        if (varName == null) {
            return false;
        }
        if (varName.startsWith("@@") && !this.checkVar(x.getParent(), x.getName())) {
            final WallVisitorUtils.WallTopStatementContext topStatementContext = WallVisitorUtils.getWallTopStatementContext();
            if (topStatementContext != null && (topStatementContext.fromSysSchema() || topStatementContext.fromSysTable())) {
                return false;
            }
            final boolean isTop = WallVisitorUtils.isTopNoneFromSelect(this, x);
            if (!isTop) {
                boolean allow = true;
                if (this.isDeny(varName) && (WallVisitorUtils.isWhereOrHaving(x) || WallVisitorUtils.checkSqlExpr(x))) {
                    allow = false;
                }
                if (!allow) {
                    this.violations.add(new IllegalSQLObjectViolation(2003, "variable not allow : " + x.getName(), this.toSQL(x)));
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final MySqlOutFileExpr x) {
        if (!this.config.isSelectIntoOutfileAllow() && !WallVisitorUtils.isTopSelectOutFile(x)) {
            this.violations.add(new IllegalSQLObjectViolation(3000, "into out file not allow", this.toSQL(x)));
        }
        return true;
    }
    
    @Override
    public boolean isDenyTable(final String name) {
        return this.config.isTableCheck() && !this.provider.checkDenyTable(name);
    }
    
    @Override
    public boolean visit(final MySqlCreateTableStatement x) {
        WallVisitorUtils.check(this, x);
        return true;
    }
}
