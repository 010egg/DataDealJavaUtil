// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.visitor;

import com.alibaba.druid.sql.dialect.hive.stmt.HiveMsckRepairStatement;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveLoadDataStatement;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateFunctionStatement;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsertStatement;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.repository.SchemaResolveVisitor;
import com.alibaba.druid.sql.dialect.hive.ast.HiveMultiInsertStatement;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsert;
import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

public class HiveSchemaStatVisitor extends SchemaStatVisitor implements HiveASTVisitor
{
    public HiveSchemaStatVisitor() {
        super(DbType.hive);
    }
    
    public HiveSchemaStatVisitor(final DbType dbType) {
        super(dbType);
    }
    
    public HiveSchemaStatVisitor(final SchemaRepository repository) {
        super(repository);
    }
    
    @Override
    public boolean visit(final HiveInsert x) {
        this.setMode(x, TableStat.Mode.Insert);
        final SQLExprTableSource tableSource = x.getTableSource();
        final SQLExpr tableName = (tableSource != null) ? tableSource.getExpr() : null;
        if (tableName instanceof SQLName) {
            final TableStat stat = this.getTableStat((SQLName)tableName);
            stat.incrementInsertCount();
        }
        for (final SQLAssignItem partition : x.getPartitions()) {
            partition.accept(this);
        }
        this.accept(x.getQuery());
        return false;
    }
    
    @Override
    public boolean visit(final HiveMultiInsertStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        return true;
    }
    
    @Override
    public boolean visit(final HiveInsertStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        final SQLWithSubqueryClause with = x.getWith();
        if (with != null) {
            with.accept(this);
        }
        this.setMode(x, TableStat.Mode.Insert);
        final SQLExprTableSource tableSource = x.getTableSource();
        final SQLExpr tableName = tableSource.getExpr();
        if (tableName instanceof SQLName) {
            final TableStat stat = this.getTableStat((SQLName)tableName);
            stat.incrementInsertCount();
            final List<SQLExpr> columns = x.getColumns();
            for (final SQLExpr column : columns) {
                if (column instanceof SQLIdentifierExpr) {
                    this.addColumn((SQLName)tableName, ((SQLIdentifierExpr)column).normalizedName());
                }
            }
        }
        for (final SQLAssignItem partition : x.getPartitions()) {
            partition.accept(this);
        }
        this.accept(x.getQuery());
        return false;
    }
    
    @Override
    public boolean visit(final HiveCreateFunctionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final HiveLoadDataStatement x) {
        final TableStat tableStat = this.getTableStat(x.getInto());
        if (tableStat != null) {
            tableStat.incrementInsertCount();
        }
        return false;
    }
    
    @Override
    public boolean visit(final HiveMsckRepairStatement x) {
        return false;
    }
}
