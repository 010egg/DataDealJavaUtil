// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;

public class PGUpdateStatement extends SQLUpdateStatement implements PGSQLStatement
{
    private boolean only;
    
    public PGUpdateStatement() {
        super(DbType.postgresql);
        this.only = false;
    }
    
    public boolean isOnly() {
        return this.only;
    }
    
    public void setOnly(final boolean only) {
        this.only = only;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof PGASTVisitor) {
            this.accept0((PGASTVisitor)visitor);
            return;
        }
        super.accept0(visitor);
    }
    
    @Override
    public void accept0(final PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor);
        }
        visitor.endVisit(this);
    }
}
