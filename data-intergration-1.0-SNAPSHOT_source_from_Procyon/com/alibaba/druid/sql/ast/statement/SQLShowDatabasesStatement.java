// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowDatabasesStatement extends SQLStatementImpl implements SQLShowStatement, SQLReplaceable
{
    private boolean physical;
    private boolean full;
    private SQLName database;
    private SQLExpr like;
    private SQLExpr where;
    private boolean extra;
    
    public boolean isPhysical() {
        return this.physical;
    }
    
    public void setPhysical(final boolean physical) {
        this.physical = physical;
    }
    
    public SQLName getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final SQLName database) {
        this.database = database;
    }
    
    public SQLExpr getLike() {
        return this.like;
    }
    
    public void setLike(final SQLExpr like) {
        this.like = like;
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr where) {
        this.where = where;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.database);
            this.acceptChild(visitor, this.like);
            this.acceptChild(visitor, this.where);
        }
        visitor.endVisit(this);
    }
    
    public boolean isExtra() {
        return this.extra;
    }
    
    public void setExtra(final boolean extra) {
        this.extra = extra;
    }
    
    public boolean isFull() {
        return this.full;
    }
    
    public void setFull(final boolean full) {
        this.full = full;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.database == expr) {
            this.setDatabase((SQLName)target);
            return true;
        }
        if (this.like == expr) {
            this.setLike(target);
            return true;
        }
        if (this.where == expr) {
            this.setWhere(target);
            return true;
        }
        return false;
    }
}
