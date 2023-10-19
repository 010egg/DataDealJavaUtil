// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.visitor;

import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSelectQueryBlock;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsExstoreStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsCountStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsUnloadStatement;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddTableStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAlterTableSetChangeLogs;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsRemoveUserStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddUserStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSetLabelStatement;
import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveSchemaStatVisitor;

public class OdpsSchemaStatVisitor extends HiveSchemaStatVisitor implements OdpsASTVisitor
{
    public OdpsSchemaStatVisitor() {
        super(DbType.odps);
    }
    
    public OdpsSchemaStatVisitor(final SchemaRepository repository) {
        super(repository);
    }
    
    @Override
    public boolean visit(final OdpsSetLabelStatement x) {
        if (x.getTable() != null) {
            x.getTable().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final OdpsAddUserStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OdpsRemoveUserStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final OdpsAlterTableSetChangeLogs x) {
        return false;
    }
    
    @Override
    public boolean visit(final OdpsAddTableStatement x) {
        final SQLExprTableSource table = x.getTable();
        final TableStat stat = this.getTableStatWithUnwrap(table.getExpr());
        if (stat != null) {
            stat.incrementAddCount();
        }
        this.resolvePartitions(table, x.getPartitions());
        return false;
    }
    
    @Override
    public boolean visit(final OdpsUnloadStatement x) {
        final SQLExprTableSource table = (SQLExprTableSource)x.getFrom();
        final TableStat stat = this.getTableStatWithUnwrap(table.getExpr());
        if (stat != null) {
            stat.incrementSelectCount();
        }
        this.resolvePartitions(table, x.getPartitions());
        return false;
    }
    
    @Override
    public boolean visit(final OdpsCountStatement x) {
        final SQLExprTableSource table = x.getTable();
        final TableStat stat = this.getTableStatWithUnwrap(table.getExpr());
        if (stat != null) {
            stat.incrementSelectCount();
        }
        this.resolvePartitions(table, x.getPartitions());
        return false;
    }
    
    @Override
    public boolean visit(final OdpsExstoreStatement x) {
        final SQLExprTableSource table = x.getTable();
        final TableStat stat = this.getTableStatWithUnwrap(table.getExpr());
        if (stat != null) {
            stat.incrementSelectCount();
        }
        this.resolvePartitions(table, x.getPartitions());
        return false;
    }
    
    private void resolvePartitions(final SQLExprTableSource table, final List<SQLAssignItem> parttions) {
        for (final SQLAssignItem partition : parttions) {
            final SQLExpr target = partition.getTarget();
            if (target instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr columnName = (SQLIdentifierExpr)target;
                columnName.setResolvedTableSource(table);
                columnName.accept(this);
            }
        }
    }
    
    @Override
    public boolean visit(final OdpsSelectQueryBlock x) {
        return this.visit(x);
    }
}
