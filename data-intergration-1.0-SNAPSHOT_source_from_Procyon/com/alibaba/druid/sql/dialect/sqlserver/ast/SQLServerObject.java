// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast;

import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import com.alibaba.druid.sql.dialect.transact.ast.TransactSQLObject;

public interface SQLServerObject extends TransactSQLObject
{
    void accept0(final SQLServerASTVisitor p0);
}
