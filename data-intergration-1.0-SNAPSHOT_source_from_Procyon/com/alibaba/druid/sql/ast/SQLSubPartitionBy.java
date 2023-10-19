// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;

public abstract class SQLSubPartitionBy extends SQLObjectImpl
{
    protected SQLExpr subPartitionsCount;
    protected boolean linear;
    protected List<SQLAssignItem> options;
    protected List<SQLSubPartition> subPartitionTemplate;
    protected SQLIntegerExpr lifecycle;
    
    public SQLSubPartitionBy() {
        this.options = new ArrayList<SQLAssignItem>();
        this.subPartitionTemplate = new ArrayList<SQLSubPartition>();
    }
    
    public SQLExpr getSubPartitionsCount() {
        return this.subPartitionsCount;
    }
    
    public void setSubPartitionsCount(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.subPartitionsCount = x;
    }
    
    public boolean isLinear() {
        return this.linear;
    }
    
    public void setLinear(final boolean linear) {
        this.linear = linear;
    }
    
    public List<SQLAssignItem> getOptions() {
        return this.options;
    }
    
    public List<SQLSubPartition> getSubPartitionTemplate() {
        return this.subPartitionTemplate;
    }
    
    public void cloneTo(final SQLSubPartitionBy x) {
        if (this.subPartitionsCount != null) {
            x.setSubPartitionsCount(this.subPartitionsCount.clone());
        }
        x.linear = this.linear;
        for (final SQLAssignItem option : this.options) {
            final SQLAssignItem option2 = option.clone();
            option2.setParent(x);
            x.options.add(option2);
        }
        for (final SQLSubPartition p : this.subPartitionTemplate) {
            final SQLSubPartition p2 = p.clone();
            p2.setParent(x);
            x.subPartitionTemplate.add(p2);
        }
        x.lifecycle = this.lifecycle;
    }
    
    public SQLIntegerExpr getLifecycle() {
        return this.lifecycle;
    }
    
    public void setLifecycle(final SQLIntegerExpr lifecycle) {
        this.lifecycle = lifecycle;
    }
    
    public boolean isPartitionByColumn(final long columnNameHashCode64) {
        return false;
    }
    
    @Override
    public abstract SQLSubPartitionBy clone();
}
