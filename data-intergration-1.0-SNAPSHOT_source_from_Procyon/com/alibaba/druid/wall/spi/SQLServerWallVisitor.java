// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.sql.dialect.sqlserver.ast.expr.SQLServerObjectReferenceExpr;
import com.alibaba.druid.wall.violation.IllegalSQLObjectViolation;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerExecStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.DbType;
import com.alibaba.druid.wall.WallProvider;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import com.alibaba.druid.wall.WallVisitor;

public class SQLServerWallVisitor extends WallVisitorBase implements WallVisitor, SQLServerASTVisitor
{
    public SQLServerWallVisitor(final WallProvider provider) {
        super(provider);
    }
    
    @Override
    public DbType getDbType() {
        return DbType.sqlserver;
    }
    
    @Override
    public boolean isDenyTable(final String name) {
        return this.config.isTableCheck() && !this.provider.checkDenyTable(name);
    }
    
    @Override
    public boolean visit(final SQLIdentifierExpr x) {
        return true;
    }
    
    @Override
    public boolean visit(final SQLMethodInvokeExpr x) {
        if (x.getParent() instanceof SQLExprTableSource) {
            WallVisitorUtils.checkFunctionInTableSource(this, x);
        }
        WallVisitorUtils.checkFunction(this, x);
        return true;
    }
    
    @Override
    public boolean visit(final SQLServerExecStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLVariantRefExpr x) {
        final String varName = x.getName();
        if (varName == null) {
            return false;
        }
        if (this.config.isVariantCheck() && varName.startsWith("@@")) {
            final WallVisitorUtils.WallTopStatementContext topStatementContext = WallVisitorUtils.getWallTopStatementContext();
            if (topStatementContext != null && (topStatementContext.fromSysSchema() || topStatementContext.fromSysTable())) {
                return false;
            }
            boolean allow = true;
            if (this.isDeny(varName) && (WallVisitorUtils.isWhereOrHaving(x) || WallVisitorUtils.checkSqlExpr(x))) {
                allow = false;
            }
            if (!allow) {
                this.violations.add(new IllegalSQLObjectViolation(2003, "variable not allow : " + x.getName(), this.toSQL(x)));
            }
        }
        return false;
    }
    
    public boolean isDeny(String varName) {
        if (varName.startsWith("@@")) {
            varName = varName.substring(2);
        }
        return this.config.getDenyVariants().contains(varName);
    }
    
    @Override
    public boolean visit(final SQLServerObjectReferenceExpr x) {
        return false;
    }
}
