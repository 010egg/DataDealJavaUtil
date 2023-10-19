// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public abstract class OdpsStatementImpl extends SQLStatementImpl
{
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OdpsASTVisitor)visitor);
    }
    
    protected abstract void accept0(final OdpsASTVisitor p0);
    
    @Override
    public String toString() {
        return SQLUtils.toOdpsString(this);
    }
}
