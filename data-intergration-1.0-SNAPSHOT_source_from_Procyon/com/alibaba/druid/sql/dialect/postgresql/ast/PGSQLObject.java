// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast;

import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;

public interface PGSQLObject extends SQLObject
{
    void accept0(final PGASTVisitor p0);
}
