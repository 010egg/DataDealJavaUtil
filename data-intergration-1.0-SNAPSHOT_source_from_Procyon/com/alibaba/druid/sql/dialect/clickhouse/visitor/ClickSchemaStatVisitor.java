// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.clickhouse.visitor;

import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

public class ClickSchemaStatVisitor extends SchemaStatVisitor implements ClickhouseVisitor
{
    public ClickSchemaStatVisitor() {
        super(DbType.antspark);
        this.dbType = DbType.antspark;
    }
    
    public ClickSchemaStatVisitor(final SchemaRepository repository) {
        super(repository);
        this.dbType = DbType.antspark;
    }
}
