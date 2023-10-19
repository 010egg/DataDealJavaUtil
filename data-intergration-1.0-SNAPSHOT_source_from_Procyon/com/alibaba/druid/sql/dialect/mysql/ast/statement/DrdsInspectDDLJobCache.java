// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;

public class DrdsInspectDDLJobCache extends MySqlStatementImpl implements SQLStatement
{
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
