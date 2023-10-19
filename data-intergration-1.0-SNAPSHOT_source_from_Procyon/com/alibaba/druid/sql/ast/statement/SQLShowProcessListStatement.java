// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowProcessListStatement extends SQLStatementImpl implements SQLShowStatement
{
    protected boolean mpp;
    private SQLExpr where;
    private SQLOrderBy orderBy;
    private SQLLimit limit;
    private boolean full;
    
    public SQLShowProcessListStatement() {
        this.full = false;
    }
    
    public boolean isMpp() {
        return this.mpp;
    }
    
    public void setMpp(final boolean mpp) {
        this.mpp = mpp;
    }
    
    public boolean isFull() {
        return this.full;
    }
    
    public void setFull(final boolean full) {
        this.full = full;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.where);
            this.acceptChild(visitor, this.orderBy);
            this.acceptChild(visitor, this.limit);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.where = x;
    }
    
    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }
    
    public void setOrderBy(final SQLOrderBy x) {
        if (x != null) {
            x.setParent(this);
        }
        this.orderBy = x;
    }
    
    public SQLLimit getLimit() {
        return this.limit;
    }
    
    public void setLimit(final SQLLimit x) {
        if (x != null) {
            x.setParent(this);
        }
        this.limit = x;
    }
}
