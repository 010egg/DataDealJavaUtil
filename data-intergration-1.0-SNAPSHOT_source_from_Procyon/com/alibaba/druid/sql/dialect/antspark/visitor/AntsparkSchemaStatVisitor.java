// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.antspark.visitor;

import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.antspark.ast.AntsparkCreateTableStatement;
import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

public class AntsparkSchemaStatVisitor extends SchemaStatVisitor implements AntsparkVisitor
{
    public AntsparkSchemaStatVisitor() {
        super(DbType.antspark);
        this.dbType = DbType.antspark;
    }
    
    public AntsparkSchemaStatVisitor(final SchemaRepository repository) {
        super(repository);
        this.dbType = DbType.antspark;
    }
    
    @Override
    public boolean visit(final AntsparkCreateTableStatement x) {
        return super.visit(x);
    }
    
    @Override
    public void endVisit(final AntsparkCreateTableStatement x) {
        super.endVisit(x);
    }
}
