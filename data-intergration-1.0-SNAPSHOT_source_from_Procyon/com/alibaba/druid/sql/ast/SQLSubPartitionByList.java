// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLSubPartitionByList extends SQLSubPartitionBy
{
    protected SQLName column;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.column);
            this.acceptChild(visitor, this.subPartitionsCount);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getColumn() {
        return this.column;
    }
    
    public void setColumn(final SQLName column) {
        if (column != null) {
            column.setParent(this);
        }
        this.column = column;
    }
    
    @Override
    public SQLSubPartitionByList clone() {
        final SQLSubPartitionByList x = new SQLSubPartitionByList();
        if (this.column != null) {
            x.setColumn(this.column.clone());
        }
        return x;
    }
    
    @Override
    public boolean isPartitionByColumn(final long columnNameHashCode64) {
        return this.column != null && this.column.nameHashCode64() == columnNameHashCode64;
    }
}
