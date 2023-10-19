// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.ast;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLInsertInto;

public class HiveInsert extends SQLInsertInto
{
    public HiveInsert() {
        this.partitions = new ArrayList<SQLAssignItem>();
    }
    
    public void setPartitions(final List<SQLAssignItem> partitions) {
        this.partitions = partitions;
    }
    
    @Override
    public SQLInsertInto clone() {
        final HiveInsert x = new HiveInsert();
        this.cloneTo(x);
        return x;
    }
    
    @Override
    public SQLExprTableSource getTableSource() {
        return this.tableSource;
    }
    
    @Override
    public void setTableSource(final SQLExprTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSource = tableSource;
    }
    
    @Override
    public void setTableSource(final SQLName tableName) {
        this.setTableSource(new SQLExprTableSource(tableName));
    }
    
    @Override
    public SQLSelect getQuery() {
        return this.query;
    }
    
    @Override
    public void setQuery(final SQLSelect query) {
        if (query != null) {
            query.setParent(this);
        }
        this.query = query;
    }
    
    @Override
    public List<SQLInsertStatement.ValuesClause> getValuesList() {
        return this.valuesList;
    }
    
    @Override
    public void addValueCause(final SQLInsertStatement.ValuesClause valueClause) {
        if (valueClause != null) {
            valueClause.setParent(this);
        }
        this.valuesList.add(valueClause);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof HiveASTVisitor) {
            this.accept0((HiveASTVisitor)visitor);
        }
        else {
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.partitions);
            this.acceptChild(visitor, this.valuesList);
            this.acceptChild(visitor, this.query);
        }
    }
    
    protected void accept0(final HiveASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.partitions);
            this.acceptChild(visitor, this.valuesList);
            this.acceptChild(visitor, this.query);
        }
        visitor.endVisit(this);
    }
}
