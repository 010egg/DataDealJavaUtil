// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;

public class OracleSetTransactionStatement extends OracleStatementImpl implements OracleStatement
{
    private boolean readOnly;
    private boolean write;
    private SQLExpr name;
    
    public OracleSetTransactionStatement() {
        this.readOnly = false;
        this.write = false;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getName() {
        return this.name;
    }
    
    public void setName(final SQLExpr name) {
        this.name = name;
    }
    
    public boolean isReadOnly() {
        return this.readOnly;
    }
    
    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }
    
    public boolean isWrite() {
        return this.write;
    }
    
    public void setWrite(final boolean write) {
        this.write = write;
    }
}
