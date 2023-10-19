// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class PGStartTransactionStatement extends SQLStatementImpl implements PGSQLStatement
{
    public PGStartTransactionStatement() {
        super(DbType.postgresql);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof PGASTVisitor) {
            this.accept0((PGASTVisitor)visitor);
        }
    }
    
    @Override
    public void accept0(final PGASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
