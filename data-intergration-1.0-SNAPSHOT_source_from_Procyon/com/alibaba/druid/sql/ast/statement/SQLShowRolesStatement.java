// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowRolesStatement extends SQLStatementImpl implements SQLShowStatement
{
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {}
        visitor.endVisit(this);
    }
}
