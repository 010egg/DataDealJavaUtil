// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLArgument;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;

public class OracleExecuteImmediateStatement extends OracleStatementImpl
{
    private SQLExpr dynamicSql;
    private final List<SQLArgument> arguments;
    private final List<SQLExpr> into;
    private final List<SQLExpr> returnInto;
    
    public OracleExecuteImmediateStatement() {
        this.arguments = new ArrayList<SQLArgument>();
        this.into = new ArrayList<SQLExpr>();
        this.returnInto = new ArrayList<SQLExpr>();
    }
    
    public OracleExecuteImmediateStatement(final String dynamicSql) {
        this.arguments = new ArrayList<SQLArgument>();
        this.into = new ArrayList<SQLExpr>();
        this.returnInto = new ArrayList<SQLExpr>();
        this.setDynamicSql(dynamicSql);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {}
        visitor.endVisit(this);
    }
    
    @Override
    public OracleExecuteImmediateStatement clone() {
        final OracleExecuteImmediateStatement x = new OracleExecuteImmediateStatement();
        if (this.dynamicSql != null) {
            x.setDynamicSql(this.dynamicSql.clone());
        }
        for (final SQLArgument arg : this.arguments) {
            final SQLArgument a2 = arg.clone();
            a2.setParent(x);
            x.arguments.add(a2);
        }
        for (final SQLExpr e : this.into) {
            final SQLExpr e2 = e.clone();
            e2.setParent(x);
            x.into.add(e2);
        }
        for (final SQLExpr e : this.returnInto) {
            final SQLExpr e2 = e.clone();
            e2.setParent(x);
            x.returnInto.add(e2);
        }
        return x;
    }
    
    public SQLExpr getDynamicSql() {
        return this.dynamicSql;
    }
    
    public void setDynamicSql(final SQLExpr dynamicSql) {
        if (dynamicSql != null) {
            dynamicSql.setParent(this);
        }
        this.dynamicSql = dynamicSql;
    }
    
    public void setDynamicSql(final String dynamicSql) {
        this.setDynamicSql(new SQLCharExpr(dynamicSql));
    }
    
    public List<SQLArgument> getArguments() {
        return this.arguments;
    }
    
    public List<SQLExpr> getInto() {
        return this.into;
    }
    
    public List<SQLExpr> getReturnInto() {
        return this.returnInto;
    }
}
