// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLListResourceGroupStatement extends SQLStatementImpl implements SQLCreateStatement
{
    public void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {}
        v.endVisit(this);
    }
}
