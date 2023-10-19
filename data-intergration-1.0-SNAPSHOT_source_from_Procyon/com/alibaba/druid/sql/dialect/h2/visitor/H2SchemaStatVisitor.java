// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.h2.visitor;

import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

public class H2SchemaStatVisitor extends SchemaStatVisitor implements H2ASTVisitor
{
    public H2SchemaStatVisitor() {
    }
    
    public H2SchemaStatVisitor(final SchemaRepository repository) {
        super(repository);
    }
}
