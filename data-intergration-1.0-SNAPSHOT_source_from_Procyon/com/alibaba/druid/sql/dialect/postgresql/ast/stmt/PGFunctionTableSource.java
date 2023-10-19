// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLParameter;
import java.util.List;
import com.alibaba.druid.sql.dialect.postgresql.ast.PGSQLObject;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;

public class PGFunctionTableSource extends SQLExprTableSource implements PGSQLObject
{
    private final List<SQLParameter> parameters;
    
    public PGFunctionTableSource() {
        this.parameters = new ArrayList<SQLParameter>();
    }
    
    public PGFunctionTableSource(final SQLExpr expr) {
        this.parameters = new ArrayList<SQLParameter>();
        this.expr = expr;
    }
    
    public List<SQLParameter> getParameters() {
        return this.parameters;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((PGASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
            this.acceptChild(visitor, this.parameters);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public PGFunctionTableSource clone() {
        final PGFunctionTableSource x = new PGFunctionTableSource();
        x.setAlias(this.alias);
        for (final SQLParameter e : this.parameters) {
            final SQLParameter e2 = e.clone();
            e2.setParent(x);
            x.getParameters().add(e2);
        }
        if (this.flashback != null) {
            x.setFlashback(this.flashback.clone());
        }
        if (this.hints != null) {
            for (final SQLHint e3 : this.hints) {
                final SQLHint e4 = e3.clone();
                e4.setParent(x);
                x.getHints().add(e4);
            }
        }
        return x;
    }
}
