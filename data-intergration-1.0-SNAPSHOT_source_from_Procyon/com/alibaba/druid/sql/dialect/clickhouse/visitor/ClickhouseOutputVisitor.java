// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.clickhouse.visitor;

import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.clickhouse.ast.ClickhouseCreateTableStatement;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStructDataType;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;

public class ClickhouseOutputVisitor extends SQLASTOutputVisitor implements ClickhouseVisitor
{
    public ClickhouseOutputVisitor(final Appendable appender) {
        super(appender, DbType.clickhouse);
    }
    
    public ClickhouseOutputVisitor(final Appendable appender, final DbType dbType) {
        super(appender, dbType);
    }
    
    public ClickhouseOutputVisitor(final Appendable appender, final boolean parameterized) {
        super(appender, parameterized);
    }
    
    @Override
    public boolean visit(final SQLWithSubqueryClause.Entry x) {
        if (x.getExpr() != null) {
            x.getExpr().accept(this);
        }
        else if (x.getSubQuery() != null) {
            this.print('(');
            this.println();
            final SQLSelect query = x.getSubQuery();
            if (query != null) {
                query.accept(this);
            }
            else {
                x.getReturningStatement().accept(this);
            }
            this.println();
            this.print(')');
        }
        this.print(' ');
        this.print0(this.ucase ? "AS " : "as ");
        this.print0(x.getAlias());
        return false;
    }
    
    @Override
    public boolean visit(final SQLStructDataType x) {
        this.print0(this.ucase ? "NESTED (" : "nested (");
        this.incrementIndent();
        this.println();
        this.printlnAndAccept(x.getFields(), ",");
        this.decrementIndent();
        this.println();
        this.print(')');
        return false;
    }
    
    @Override
    public boolean visit(final SQLStructDataType.Field x) {
        final SQLName name = x.getName();
        if (name != null) {
            name.accept(this);
        }
        final SQLDataType dataType = x.getDataType();
        if (dataType != null) {
            this.print(' ');
            dataType.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final ClickhouseCreateTableStatement x) {
        super.visit(x);
        final SQLExpr partitionBy = x.getPartitionBy();
        if (partitionBy != null) {
            this.println();
            this.print0(this.ucase ? "PARTITION BY " : "partition by ");
            partitionBy.accept(this);
        }
        final SQLOrderBy orderBy = x.getOrderBy();
        if (orderBy != null) {
            this.println();
            orderBy.accept(this);
        }
        final SQLExpr sampleBy = x.getSampleBy();
        if (sampleBy != null) {
            this.println();
            this.print0(this.ucase ? "SAMPLE BY " : "sample by ");
            sampleBy.accept(this);
        }
        final List<SQLAssignItem> settings = x.getSettings();
        if (!settings.isEmpty()) {
            this.println();
            this.print0(this.ucase ? "SETTINGS " : "settings ");
            this.printAndAccept(settings, ", ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddColumn x) {
        this.print0(this.ucase ? "ADD COLUMN " : "add column ");
        this.printAndAccept(x.getColumns(), ", ");
        return false;
    }
}
