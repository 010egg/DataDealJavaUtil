// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.db2.visitor;

import com.alibaba.druid.sql.dialect.db2.ast.DB2Object;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2CreateTableStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2ValuesStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2SelectQueryBlock;
import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

public class DB2SchemaStatVisitor extends SchemaStatVisitor implements DB2ASTVisitor
{
    public DB2SchemaStatVisitor() {
        super(DbType.db2);
    }
    
    public DB2SchemaStatVisitor(final SchemaRepository repository) {
        super(repository);
    }
    
    @Override
    public boolean visit(final DB2SelectQueryBlock x) {
        return this.visit(x);
    }
    
    @Override
    public void endVisit(final DB2SelectQueryBlock x) {
        super.endVisit(x);
    }
    
    @Override
    public boolean visit(final DB2ValuesStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final DB2CreateTableStatement x) {
        return this.visit(x);
    }
    
    @Override
    public void endVisit(final DB2CreateTableStatement x) {
    }
    
    @Override
    protected boolean isPseudoColumn(final long hash64) {
        return hash64 == DB2Object.Constants.CURRENT_DATE || hash64 == DB2Object.Constants.CURRENT_DATE2 || hash64 == DB2Object.Constants.CURRENT_TIME || hash64 == DB2Object.Constants.CURRENT_SCHEMA;
    }
}
