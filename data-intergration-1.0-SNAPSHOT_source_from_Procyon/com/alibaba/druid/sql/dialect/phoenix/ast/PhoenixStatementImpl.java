// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.phoenix.ast;

import com.alibaba.druid.sql.dialect.phoenix.visitor.PhoenixASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public abstract class PhoenixStatementImpl extends SQLStatementImpl implements PhoenixObject
{
    public PhoenixStatementImpl() {
        super(DbType.phoenix);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof PhoenixASTVisitor) {
            this.accept0((PhoenixASTVisitor)visitor);
            return;
        }
        super.accept0(visitor);
    }
    
    @Override
    public abstract void accept0(final PhoenixASTVisitor p0);
}
