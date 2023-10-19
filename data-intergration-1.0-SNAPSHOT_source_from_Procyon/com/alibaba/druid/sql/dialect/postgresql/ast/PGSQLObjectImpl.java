// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast;

import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public abstract class PGSQLObjectImpl extends SQLObjectImpl implements PGSQLObject
{
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((PGASTVisitor)visitor);
    }
    
    @Override
    public abstract void accept0(final PGASTVisitor p0);
}
