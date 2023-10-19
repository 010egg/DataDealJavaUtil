// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;

public class OracleLabelStatement extends OracleStatementImpl
{
    private SQLName label;
    
    public OracleLabelStatement() {
    }
    
    public OracleLabelStatement(final SQLName label) {
        this.label = label;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.label);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getLabel() {
        return this.label;
    }
    
    public void setLabel(final SQLName label) {
        this.label = label;
    }
}
