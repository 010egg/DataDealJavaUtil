// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.alibaba.druid.wall.violation.IllegalSQLObjectViolation;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.DbType;
import com.alibaba.druid.wall.WallProvider;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.wall.WallVisitor;

public class OracleWallVisitor extends WallVisitorBase implements WallVisitor, OracleASTVisitor
{
    public OracleWallVisitor(final WallProvider provider) {
        super(provider);
    }
    
    @Override
    public DbType getDbType() {
        return DbType.oracle;
    }
    
    @Override
    public boolean visit(final SQLIdentifierExpr x) {
        String name = x.getName();
        name = WallVisitorUtils.form(name);
        if (this.config.isVariantCheck() && this.config.getDenyVariants().contains(name)) {
            this.getViolations().add(new IllegalSQLObjectViolation(2003, "variable not allow : " + name, this.toSQL(x)));
        }
        return true;
    }
    
    @Override
    public boolean visit(final OracleSelectTableReference x) {
        return WallVisitorUtils.check(this, x);
    }
    
    @Override
    public boolean isDenyTable(String name) {
        if (!this.config.isTableCheck()) {
            return false;
        }
        name = WallVisitorUtils.form(name);
        return name.startsWith("v$") || name.startsWith("v_$") || !this.provider.checkDenyTable(name);
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement.InsertIntoClause x) {
        WallVisitorUtils.checkInsert(this, x);
        return true;
    }
    
    @Override
    public boolean visit(final OracleMultiInsertStatement x) {
        if (!this.config.isInsertAllow()) {
            this.getViolations().add(new IllegalSQLObjectViolation(1004, "insert not allow", this.toSQL(x)));
            return false;
        }
        WallVisitorUtils.initWallTopStatementContext();
        return true;
    }
    
    @Override
    public void endVisit(final OracleMultiInsertStatement x) {
        WallVisitorUtils.clearWallTopStatementContext();
    }
}
