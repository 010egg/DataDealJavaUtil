// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObject;
import com.alibaba.druid.sql.ast.SQLSubPartitionBy;

public class MySqlSubPartitionByList extends SQLSubPartitionBy implements MySqlObject
{
    private List<SQLExpr> keys;
    private List<SQLColumnDefinition> columns;
    
    public MySqlSubPartitionByList() {
        this.keys = new ArrayList<SQLExpr>();
        this.columns = new ArrayList<SQLColumnDefinition>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
            return;
        }
        throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.keys);
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.subPartitionsCount);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLExpr> getKeys() {
        return this.keys;
    }
    
    public void addKey(final SQLExpr key) {
        if (key != null) {
            key.setParent(this);
        }
        this.keys.add(key);
    }
    
    public List<SQLColumnDefinition> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLColumnDefinition column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }
    
    public void cloneTo(final MySqlSubPartitionByList x) {
        super.cloneTo(x);
        for (final SQLExpr key : this.keys) {
            final SQLExpr k2 = key.clone();
            k2.setParent(x);
            x.keys.add(k2);
        }
        for (final SQLColumnDefinition column : this.columns) {
            final SQLColumnDefinition c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
    }
    
    @Override
    public MySqlSubPartitionByList clone() {
        final MySqlSubPartitionByList x = new MySqlSubPartitionByList();
        this.cloneTo(x);
        return x;
    }
}
