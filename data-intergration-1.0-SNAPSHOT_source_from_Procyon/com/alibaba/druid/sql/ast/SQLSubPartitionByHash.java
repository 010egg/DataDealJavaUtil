// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLSubPartitionByHash extends SQLSubPartitionBy
{
    protected SQLExpr expr;
    private boolean key;
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
            this.acceptChild(visitor, this.subPartitionsCount);
        }
        visitor.endVisit(this);
    }
    
    public boolean isKey() {
        return this.key;
    }
    
    public void setKey(final boolean key) {
        this.key = key;
    }
    
    @Override
    public SQLSubPartitionByHash clone() {
        final SQLSubPartitionByHash x = new SQLSubPartitionByHash();
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        x.key = this.key;
        return x;
    }
    
    @Override
    public boolean isPartitionByColumn(final long columnNameHashCode64) {
        return this.expr instanceof SQLName && ((SQLName)this.expr).nameHashCode64() == columnNameHashCode64;
    }
}
