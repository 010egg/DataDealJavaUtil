// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast;

import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public abstract class SQLServerObjectImpl extends SQLObjectImpl implements SQLServerObject
{
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((SQLServerASTVisitor)visitor);
    }
    
    @Override
    public abstract void accept0(final SQLServerASTVisitor p0);
}
