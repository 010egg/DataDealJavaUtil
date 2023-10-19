// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.Collection;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public abstract class SQLInsertInto extends SQLStatementImpl implements SQLReplaceable
{
    protected List<String> insertBeforeComments;
    protected SQLExprTableSource tableSource;
    protected final List<SQLExpr> columns;
    protected transient String columnsString;
    protected transient long columnsStringHash;
    protected SQLSelect query;
    protected final List<SQLInsertStatement.ValuesClause> valuesList;
    protected boolean overwrite;
    protected List<SQLAssignItem> partitions;
    
    public SQLInsertInto() {
        this.columns = new ArrayList<SQLExpr>();
        this.valuesList = new ArrayList<SQLInsertStatement.ValuesClause>();
        this.overwrite = false;
    }
    
    public void cloneTo(final SQLInsertInto x) {
        if (this.tableSource != null) {
            x.setTableSource(this.tableSource.clone());
        }
        for (final SQLExpr column : this.columns) {
            final SQLExpr column2 = column.clone();
            column2.setParent(x);
            x.columns.add(column2);
        }
        if (this.query != null) {
            x.setQuery(this.query.clone());
        }
        for (final SQLInsertStatement.ValuesClause v : this.valuesList) {
            final SQLInsertStatement.ValuesClause v2 = v.clone();
            v2.setParent(x);
            x.valuesList.add(v2);
        }
        if (this.hint != null) {
            x.setHint(this.hint.clone());
        }
        x.overwrite = this.overwrite;
        if (this.partitions != null) {
            for (final SQLAssignItem item : this.partitions) {
                x.addPartition(item.clone());
            }
        }
    }
    
    public void addInsertBeforeComment(final List<String> comments) {
        if (this.insertBeforeComments == null) {
            this.insertBeforeComments = new ArrayList<String>();
        }
        this.insertBeforeComments.addAll(comments);
    }
    
    public List<String> getInsertBeforeCommentsDirect() {
        return this.insertBeforeComments;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        for (int i = 0; i < this.columns.size(); ++i) {
            if (this.columns.get(i) == expr) {
                target.setParent(this);
                this.columns.set(i, target);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public SQLCommentHint getHint() {
        return this.hint;
    }
    
    @Override
    public void setHint(final SQLCommentHint x) {
        if (x != null) {
            x.setParent(this);
        }
        this.hint = x;
    }
    
    @Override
    public abstract SQLInsertInto clone();
    
    public String getAlias() {
        return this.tableSource.getAlias();
    }
    
    public void setAlias(final String alias) {
        this.tableSource.setAlias(alias);
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
    
    public SQLName getTableName() {
        return (SQLName)this.tableSource.getExpr();
    }
    
    public void setTableName(final SQLName tableName) {
        this.setTableSource(new SQLExprTableSource(tableName));
    }
    
    public void setTableSource(final SQLName tableName) {
        this.setTableSource(new SQLExprTableSource(tableName));
    }
    
    public SQLSelect getQuery() {
        return this.query;
    }
    
    public void setQuery(final SQLSelectQuery query) {
        this.setQuery(new SQLSelect(query));
    }
    
    public void setQuery(final SQLSelect query) {
        if (query != null) {
            query.setParent(this);
        }
        this.query = query;
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
    
    public SQLInsertStatement.ValuesClause getValues() {
        if (this.valuesList.size() == 0) {
            return null;
        }
        return this.valuesList.get(0);
    }
    
    public void setValues(final SQLInsertStatement.ValuesClause values) {
        if (this.valuesList.size() == 0) {
            this.valuesList.add(values);
        }
        else {
            this.valuesList.set(0, values);
        }
    }
    
    public List<SQLInsertStatement.ValuesClause> getValuesList() {
        return this.valuesList;
    }
    
    public void addValueCause(final SQLInsertStatement.ValuesClause valueClause) {
        if (valueClause != null) {
            valueClause.setParent(this);
        }
        this.valuesList.add(valueClause);
    }
    
    public String getColumnsString() {
        return this.columnsString;
    }
    
    public long getColumnsStringHash() {
        return this.columnsStringHash;
    }
    
    public void setColumnsString(final String columnsString, final long columnsStringHash) {
        this.columnsString = columnsString;
        this.columnsStringHash = columnsStringHash;
    }
    
    public boolean isOverwrite() {
        return this.overwrite;
    }
    
    public void setOverwrite(final boolean overwrite) {
        this.overwrite = overwrite;
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
