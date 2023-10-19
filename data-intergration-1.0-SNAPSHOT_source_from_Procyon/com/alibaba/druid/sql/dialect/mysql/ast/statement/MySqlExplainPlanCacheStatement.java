// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlExplainPlanCacheStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
