// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import java.util.Iterator;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.List;

public abstract class SQLPartitionBy extends SQLObjectImpl
{
    protected SQLSubPartitionBy subPartitionBy;
    protected SQLExpr partitionsCount;
    protected boolean linear;
    protected List<SQLPartition> partitions;
    protected List<SQLName> storeIn;
    protected List<SQLExpr> columns;
    protected SQLIntegerExpr lifecycle;
    
    public SQLPartitionBy() {
        this.partitions = new ArrayList<SQLPartition>();
        this.storeIn = new ArrayList<SQLName>();
        this.columns = new ArrayList<SQLExpr>();
    }
    
    public List<SQLPartition> getPartitions() {
        return this.partitions;
    }
    
    public void addPartition(final SQLPartition partition) {
        if (partition != null) {
            partition.setParent(this);
        }
        this.partitions.add(partition);
    }
    
    public SQLSubPartitionBy getSubPartitionBy() {
        return this.subPartitionBy;
    }
    
    public void setSubPartitionBy(final SQLSubPartitionBy subPartitionBy) {
        if (subPartitionBy != null) {
            subPartitionBy.setParent(this);
        }
        this.subPartitionBy = subPartitionBy;
    }
    
    public SQLExpr getPartitionsCount() {
        return this.partitionsCount;
    }
    
    public void setPartitionsCount(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.partitionsCount = x;
    }
    
    public void setPartitionsCount(final int partitionsCount) {
        this.partitionsCount = new SQLIntegerExpr(partitionsCount);
    }
    
    public boolean isLinear() {
        return this.linear;
    }
    
    public void setLinear(final boolean linear) {
        this.linear = linear;
    }
    
    public List<SQLName> getStoreIn() {
        return this.storeIn;
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
    
    public void cloneTo(final SQLPartitionBy x) {
        if (this.subPartitionBy != null) {
            x.setSubPartitionBy(this.subPartitionBy.clone());
        }
        if (this.partitionsCount != null) {
            x.setPartitionsCount(this.partitionsCount.clone());
        }
        x.linear = this.linear;
        for (final SQLPartition p : this.partitions) {
            final SQLPartition p2 = p.clone();
            p2.setParent(x);
            x.partitions.add(p2);
        }
        for (final SQLName name : this.storeIn) {
            final SQLName name2 = name.clone();
            name2.setParent(x);
            x.storeIn.add(name2);
        }
        x.lifecycle = this.lifecycle;
    }
    
    public boolean isPartitionByColumn(final long columnNameHashCode64) {
        for (final SQLExpr column : this.columns) {
            if (column instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)column).nameHashCode64() == columnNameHashCode64) {
                return true;
            }
        }
        return this.subPartitionBy != null && this.subPartitionBy.isPartitionByColumn(columnNameHashCode64);
    }
    
    public SQLIntegerExpr getLifecycle() {
        return this.lifecycle;
    }
    
    public void setLifecycle(final SQLIntegerExpr lifecycle) {
        this.lifecycle = lifecycle;
    }
    
    @Override
    public abstract SQLPartitionBy clone();
}
