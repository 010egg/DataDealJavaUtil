// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.db2.ast;

import com.alibaba.druid.sql.dialect.db2.visitor.DB2ASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public abstract class DB2StatementImpl extends SQLStatementImpl implements DB2Object
{
    public DB2StatementImpl() {
        super(DbType.db2);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof DB2ASTVisitor) {
            this.accept0((DB2ASTVisitor)visitor);
            return;
        }
        super.accept0(visitor);
    }
    
    @Override
    public abstract void accept0(final DB2ASTVisitor p0);
}
