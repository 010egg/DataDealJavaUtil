// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;

public class PGDeleteStatement extends SQLDeleteStatement implements PGSQLStatement
{
    private boolean returning;
    
    public PGDeleteStatement() {
        super(DbType.postgresql);
    }
    
    public boolean isReturning() {
        return this.returning;
    }
    
    public void setReturning(final boolean returning) {
        this.returning = returning;
    }
    
    @Override
    public String getAlias() {
        if (this.tableSource == null) {
            return null;
        }
        return this.tableSource.getAlias();
    }
    
    @Override
    public void setAlias(final String alias) {
        this.tableSource.setAlias(alias);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof PGASTVisitor) {
            this.accept0((PGASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    @Override
    public void accept0(final PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.with);
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.using);
            this.acceptChild(visitor, this.where);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public PGDeleteStatement clone() {
        final PGDeleteStatement x = new PGDeleteStatement();
        this.cloneTo(x);
        x.returning = this.returning;
        return x;
    }
}
