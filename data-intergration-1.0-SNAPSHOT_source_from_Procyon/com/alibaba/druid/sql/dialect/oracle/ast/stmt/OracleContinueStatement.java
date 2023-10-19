// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;

public class OracleContinueStatement extends OracleStatementImpl
{
    private SQLExpr when;
    private String label;
    
    public SQLExpr getWhen() {
        return this.when;
    }
    
    public void setWhen(final SQLExpr when) {
        if (when != null) {
            when.setParent(this);
        }
        this.when = when;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.when);
        }
        visitor.endVisit(this);
    }
}
