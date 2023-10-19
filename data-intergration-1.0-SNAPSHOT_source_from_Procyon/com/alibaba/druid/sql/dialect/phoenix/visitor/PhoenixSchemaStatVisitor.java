// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.phoenix.visitor;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

public class PhoenixSchemaStatVisitor extends SchemaStatVisitor implements PhoenixASTVisitor
{
    public PhoenixSchemaStatVisitor() {
        super(DbType.phoenix);
    }
}
