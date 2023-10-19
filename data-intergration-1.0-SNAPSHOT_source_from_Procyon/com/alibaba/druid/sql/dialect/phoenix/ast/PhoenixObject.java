// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.phoenix.ast;

import com.alibaba.druid.sql.dialect.phoenix.visitor.PhoenixASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;

public interface PhoenixObject extends SQLObject
{
    void accept0(final PhoenixASTVisitor p0);
}
