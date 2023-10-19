// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLPartitionByList extends SQLPartitionBy
{
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
    public SQLPartitionByList clone() {
        final SQLPartitionByList x = new SQLPartitionByList();
        this.cloneTo(x);
        for (final SQLExpr column : this.columns) {
            final SQLExpr c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        return x;
    }
    
    public void cloneTo(final SQLPartitionByList x) {
        super.cloneTo(x);
    }
}
