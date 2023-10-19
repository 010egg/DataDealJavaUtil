// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast;

import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public abstract class SQLServerStatementImpl extends SQLStatementImpl implements SQLServerStatement
{
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((SQLServerASTVisitor)visitor);
    }
    
    @Override
    public abstract void accept0(final SQLServerASTVisitor p0);
}
