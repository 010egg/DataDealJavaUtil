// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public final class SQLKeep extends SQLObjectImpl
{
    protected DenseRank denseRank;
    protected SQLOrderBy orderBy;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.orderBy);
        }
        visitor.endVisit(this);
    }
    
    public DenseRank getDenseRank() {
        return this.denseRank;
    }
    
    public void setDenseRank(final DenseRank denseRank) {
        this.denseRank = denseRank;
    }
    
    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }
    
    public void setOrderBy(final SQLOrderBy orderBy) {
        if (orderBy != null) {
            orderBy.setParent(this);
        }
        this.orderBy = orderBy;
    }
    
    @Override
    public SQLKeep clone() {
        final SQLKeep x = new SQLKeep();
        x.denseRank = this.denseRank;
        if (this.orderBy != null) {
            x.setOrderBy(this.orderBy.clone());
        }
        return x;
    }
    
    public enum DenseRank
    {
        FIRST, 
        LAST;
    }
}
