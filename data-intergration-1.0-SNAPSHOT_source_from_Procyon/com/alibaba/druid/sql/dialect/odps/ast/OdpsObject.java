// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;

public interface OdpsObject extends SQLObject
{
    void accept0(final OdpsASTVisitor p0);
}
