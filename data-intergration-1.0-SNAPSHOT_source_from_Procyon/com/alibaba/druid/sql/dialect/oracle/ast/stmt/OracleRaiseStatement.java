// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;

public class OracleRaiseStatement extends OracleStatementImpl
{
    private SQLExpr exception;
    
    public SQLExpr getException() {
        return this.exception;
    }
    
    public void setException(final SQLExpr exception) {
        this.exception = exception;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.exception);
        }
        visitor.endVisit(this);
    }
}
