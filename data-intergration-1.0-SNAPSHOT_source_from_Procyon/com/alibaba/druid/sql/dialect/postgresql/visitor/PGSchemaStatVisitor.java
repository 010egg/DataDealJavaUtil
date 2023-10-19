// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.visitor;

import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGAlterSchemaStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDropSchemaStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGCreateSchemaStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGConnectToStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGStartTransactionStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGShowStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGTypeCastExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGUpdateStatement;
import com.alibaba.druid.util.PGUtils;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectStatement;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.repository.SchemaResolveVisitor;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDeleteStatement;
import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

public class PGSchemaStatVisitor extends SchemaStatVisitor implements PGASTVisitor
{
    public PGSchemaStatVisitor() {
        super(DbType.postgresql);
    }
    
    public PGSchemaStatVisitor(final SchemaRepository repository) {
        super(repository);
    }
    
    @Override
    public DbType getDbType() {
        return DbType.postgresql;
    }
    
    @Override
    public boolean visit(final PGDeleteStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        if (x.getWith() != null) {
            x.getWith().accept(this);
        }
        final SQLTableSource using = x.getUsing();
        if (using != null) {
            using.accept(this);
        }
        x.putAttribute("_original_use_mode", this.getMode());
        this.setMode(x, TableStat.Mode.Delete);
        final TableStat stat = this.getTableStat(x.getTableName());
        stat.incrementDeleteCount();
        this.accept(x.getWhere());
        return false;
    }
    
    @Override
    public boolean visit(final PGInsertStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        if (x.getWith() != null) {
            x.getWith().accept(this);
        }
        x.putAttribute("_original_use_mode", this.getMode());
        this.setMode(x, TableStat.Mode.Insert);
        final SQLName tableName = x.getTableName();
        final TableStat stat = this.getTableStat(tableName);
        stat.incrementInsertCount();
        this.accept(x.getColumns());
        this.accept(x.getQuery());
        return false;
    }
    
    @Override
    public void endVisit(final PGSelectStatement x) {
    }
    
    @Override
    public boolean visit(final PGSelectStatement x) {
        return this.visit(x);
    }
    
    public boolean isPseudoColumn(final long hash) {
        return PGUtils.isPseudoColumn(hash);
    }
    
    @Override
    public boolean visit(final PGUpdateStatement x) {
        if (this.repository != null && x.getParent() == null) {
            this.repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        }
        if (x.getWith() != null) {
            x.getWith().accept(this);
        }
        final TableStat stat = this.getTableStat(x.getTableName());
        stat.incrementUpdateCount();
        this.accept(x.getFrom());
        this.accept(x.getItems());
        this.accept(x.getWhere());
        return false;
    }
    
    @Override
    public boolean visit(final PGTypeCastExpr x) {
        x.getExpr().accept(this);
        return false;
    }
    
    @Override
    public boolean visit(final PGShowStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final PGStartTransactionStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final PGConnectToStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final PGCreateSchemaStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final PGDropSchemaStatement x) {
        return false;
    }
    
    @Override
    public boolean visit(final PGAlterSchemaStatement x) {
        return false;
    }
}
