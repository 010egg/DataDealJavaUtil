// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class PGConnectToStatement extends SQLStatementImpl implements PGSQLStatement
{
    private SQLName target;
    
    public PGConnectToStatement() {
        super(DbType.postgresql);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((PGASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final PGASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.target);
        }
        v.endVisit(this);
    }
    
    public SQLName getTarget() {
        return this.target;
    }
    
    public void setTarget(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.target = x;
    }
}
