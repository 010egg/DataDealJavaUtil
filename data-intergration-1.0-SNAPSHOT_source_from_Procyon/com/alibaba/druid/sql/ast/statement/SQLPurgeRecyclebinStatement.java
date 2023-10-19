// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLPurgeRecyclebinStatement extends SQLStatementImpl implements SQLDropStatement
{
    public SQLPurgeRecyclebinStatement() {
    }
    
    public SQLPurgeRecyclebinStatement(final DbType dbType) {
        super(dbType);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {}
        visitor.endVisit(this);
    }
}
