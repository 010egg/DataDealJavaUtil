// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;

public interface MySqlObject extends SQLObject
{
    void accept0(final MySqlASTVisitor p0);
}
