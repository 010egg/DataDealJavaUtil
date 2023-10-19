// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLPartitionByRange extends SQLPartitionBy
{
    protected SQLExpr interval;
    
    public SQLExpr getInterval() {
        return this.interval;
    }
    
    public void setInterval(final SQLExpr interval) {
        if (interval != null) {
            interval.setParent(this);
        }
        this.interval = interval;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.interval);
            this.acceptChild(visitor, this.storeIn);
            this.acceptChild(visitor, this.partitions);
            this.acceptChild(visitor, this.subPartitionBy);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLPartitionByRange clone() {
        final SQLPartitionByRange x = new SQLPartitionByRange();
        this.cloneTo(x);
        if (this.interval != null) {
            x.setInterval(this.interval.clone());
        }
        for (final SQLExpr column : this.columns) {
            final SQLExpr c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        return x;
    }
    
    public void cloneTo(final SQLPartitionByRange x) {
        super.cloneTo(x);
    }
}
