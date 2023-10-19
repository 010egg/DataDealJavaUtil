// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLReplaceStatement extends SQLStatementImpl
{
    protected boolean lowPriority;
    protected boolean delayed;
    protected SQLExprTableSource tableSource;
    protected final List<SQLExpr> columns;
    protected List<SQLInsertStatement.ValuesClause> valuesList;
    protected SQLQueryExpr query;
    protected List<SQLCommentHint> hints;
    protected List<SQLAssignItem> partitions;
    
    public SQLReplaceStatement() {
        this.lowPriority = false;
        this.delayed = false;
        this.columns = new ArrayList<SQLExpr>();
        this.valuesList = new ArrayList<SQLInsertStatement.ValuesClause>();
    }
    
    public SQLName getTableName() {
        if (this.tableSource == null) {
            return null;
        }
        return (SQLName)this.tableSource.getExpr();
    }
    
    public void setTableName(final SQLName tableName) {
        this.setTableSource(new SQLExprTableSource(tableName));
    }
    
    public SQLExprTableSource getTableSource() {
        return this.tableSource;
    }
    
    public void setTableSource(final SQLExprTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSource = tableSource;
    }
    
    public List<SQLExpr> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLExpr column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }
    
    public boolean isLowPriority() {
        return this.lowPriority;
    }
    
    public void setLowPriority(final boolean lowPriority) {
        this.lowPriority = lowPriority;
    }
    
    public boolean isDelayed() {
        return this.delayed;
    }
    
    public void setDelayed(final boolean delayed) {
        this.delayed = delayed;
    }
    
    public SQLQueryExpr getQuery() {
        return this.query;
    }
    
    public void setQuery(final SQLQueryExpr query) {
        if (query != null) {
            query.setParent(this);
        }
        this.query = query;
    }
    
    public List<SQLInsertStatement.ValuesClause> getValuesList() {
        return this.valuesList;
    }
    
    @Override
    public SQLStatement clone() {
        final SQLReplaceStatement x = new SQLReplaceStatement();
        x.setDbType(this.dbType);
        if (this.headHints != null) {
            for (final SQLCommentHint h : this.headHints) {
                final SQLCommentHint clone = h.clone();
                clone.setParent(x);
                x.headHints.add(clone);
            }
        }
        if (this.hints != null && !this.hints.isEmpty()) {
            for (final SQLCommentHint h : this.hints) {
                final SQLCommentHint clone = h.clone();
                clone.setParent(x);
                x.getHints().add(clone);
            }
        }
        x.lowPriority = this.lowPriority;
        x.delayed = this.delayed;
        if (this.tableSource != null) {
            x.tableSource = this.tableSource.clone();
        }
        for (final SQLInsertStatement.ValuesClause clause : this.valuesList) {
            x.getValuesList().add(clause.clone());
        }
        for (final SQLExpr column : this.columns) {
            x.addColumn(column.clone());
        }
        if (this.query != null) {
            x.query = this.query.clone();
        }
        if (this.partitions != null) {
            for (final SQLAssignItem partition : this.partitions) {
                x.addPartition(partition.clone());
            }
        }
        return x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.valuesList);
            this.acceptChild(visitor, this.query);
        }
        visitor.endVisit(this);
    }
    
    public int getHintsSize() {
        if (this.hints == null) {
            return 0;
        }
        return this.hints.size();
    }
    
    public List<SQLCommentHint> getHints() {
        if (this.hints == null) {
            this.hints = new ArrayList<SQLCommentHint>(2);
        }
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
    
    public void addPartition(final SQLAssignItem partition) {
        if (partition != null) {
            partition.setParent(this);
        }
        if (this.partitions == null) {
            this.partitions = new ArrayList<SQLAssignItem>();
        }
        this.partitions.add(partition);
    }
    
    public List<SQLAssignItem> getPartitions() {
        return this.partitions;
    }
}
