// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowQueryTaskStatement extends SQLStatementImpl implements SQLShowStatement, SQLReplaceable
{
    private boolean full;
    private SQLExpr where;
    private SQLOrderBy orderBy;
    private SQLLimit limit;
    private SQLExpr user;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.where);
            this.acceptChild(visitor, this.orderBy);
            this.acceptChild(visitor, this.limit);
            this.acceptChild(visitor, this.user);
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
    
    public SQLExpr getUser() {
        return this.user;
    }
    
    public SQLExpr getFor() {
        return this.user;
    }
    
    public void setUser(final SQLExpr user) {
        this.user = user;
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
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.where == expr) {
            this.setWhere(target);
            return true;
        }
        if (this.user == expr) {
            this.setUser(target);
            return true;
        }
        return false;
    }
    
    public boolean isFull() {
        return this.full;
    }
    
    public void setFull(final boolean full) {
        this.full = full;
    }
}
