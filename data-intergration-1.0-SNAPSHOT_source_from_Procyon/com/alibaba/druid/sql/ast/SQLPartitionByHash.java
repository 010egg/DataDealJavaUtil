// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLPartitionByHash extends SQLPartitionBy
{
    protected boolean key;
    protected boolean unique;
    
    public boolean isKey() {
        return this.key;
    }
    
    public void setKey(final boolean key) {
        this.key = key;
    }
    
    public boolean isUnique() {
        return this.unique;
    }
    
    public void setUnique(final boolean unique) {
        this.unique = unique;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.partitionsCount);
            this.acceptChild(visitor, this.getPartitions());
            this.acceptChild(visitor, this.subPartitionBy);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLPartitionByHash clone() {
        final SQLPartitionByHash x = new SQLPartitionByHash();
        this.cloneTo(x);
        x.key = this.key;
        x.unique = this.unique;
        for (final SQLExpr column : this.columns) {
            final SQLExpr c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        return x;
    }
    
    public void cloneTo(final SQLPartitionByHash x) {
        super.cloneTo(x);
    }
}
