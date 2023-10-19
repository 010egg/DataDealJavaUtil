// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.visitor;

import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerWaitForStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerRollbackStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerOutput;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerSetTransactionIsolationLevelStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerExecStatement;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerUpdateStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.expr.SQLServerObjectReferenceExpr;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerTop;
import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

public class SQLServerSchemaStatVisitor extends SchemaStatVisitor implements SQLServerASTVisitor
{
    public SQLServerSchemaStatVisitor() {
        super(DbType.sqlserver);
    }
    
    public SQLServerSchemaStatVisitor(final SchemaRepository repository) {
        super(repository);
    }
    
    @Override
    public boolean visit(final SQLServerTop x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerObjectReferenceExpr x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerUpdateStatement x) {
        final TableStat stat = this.getTableStat(x.getTableName());
        stat.incrementUpdateCount();
        this.accept(x.getItems());
        this.accept(x.getFrom());
        this.accept(x.getWhere());
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerExecStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerSetTransactionIsolationLevelStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerOutput x) {
        return false;
    }
    
    @Override
    public boolean visit(final SQLServerRollbackStatement x) {
        return true;
    }
    
    @Override
    public boolean visit(final SQLServerWaitForStatement x) {
        return true;
    }
    
    @Override
    public boolean visit(final SQLServerExecStatement.SQLServerParameter x) {
        return false;
    }
}
