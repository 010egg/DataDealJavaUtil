// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;

public class PGSelectStatement extends SQLSelectStatement implements PGSQLStatement
{
    public PGSelectStatement() {
        super(DbType.postgresql);
    }
    
    public PGSelectStatement(final SQLSelect select) {
        super(select, DbType.postgresql);
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
            this.acceptChild(visitor, this.select);
        }
        visitor.endVisit(this);
    }
}
