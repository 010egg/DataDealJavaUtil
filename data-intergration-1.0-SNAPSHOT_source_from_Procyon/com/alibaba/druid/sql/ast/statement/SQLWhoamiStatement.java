// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLWhoamiStatement extends SQLStatementImpl
{
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {}
        v.endVisit(this);
    }
}
