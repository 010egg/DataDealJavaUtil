// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLPartitionByValue extends SQLPartitionBy
{
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.storeIn);
            this.acceptChild(visitor, this.partitions);
            this.acceptChild(visitor, this.subPartitionBy);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLPartitionByValue clone() {
        final SQLPartitionByValue x = new SQLPartitionByValue();
        this.cloneTo(x);
        for (final SQLExpr column : this.columns) {
            final SQLExpr c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        return x;
    }
    
    public void cloneTo(final SQLPartitionByValue x) {
        super.cloneTo(x);
    }
}
