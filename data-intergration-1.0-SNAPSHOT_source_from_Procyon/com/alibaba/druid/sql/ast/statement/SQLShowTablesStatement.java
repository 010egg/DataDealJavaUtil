// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowTablesStatement extends SQLStatementImpl implements SQLShowStatement, SQLReplaceable
{
    private boolean extended;
    protected SQLName database;
    protected SQLExpr like;
    protected boolean full;
    protected SQLExpr where;
    
    public SQLShowTablesStatement() {
        this.extended = false;
    }
    
    public SQLName getDatabase() {
        return this.database;
    }
    
    public SQLName getFrom() {
        return this.database;
    }
    
    public void setDatabase(final SQLName database) {
        if (database != null) {
            database.setParent(this);
        }
        this.database = database;
    }
    
    public SQLExpr getLike() {
        return this.like;
    }
    
    public void setLike(final SQLExpr like) {
        if (like != null) {
            like.setParent(this);
        }
        this.like = like;
    }
    
    public boolean isFull() {
        return this.full;
    }
    
    public void setFull(final boolean full) {
        this.full = full;
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr where) {
        this.where = where;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.database);
            this.acceptChild(visitor, this.like);
        }
        visitor.endVisit(this);
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
    
    public boolean isExtended() {
        return this.extended;
    }
    
    public void setExtended(final boolean extended) {
        this.extended = extended;
    }
}
