// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowVariantsStatement extends SQLStatementImpl implements SQLShowStatement
{
    private boolean global;
    private boolean session;
    private SQLExpr like;
    private SQLExpr where;
    
    public SQLShowVariantsStatement() {
        this.global = false;
        this.session = false;
    }
    
    public boolean isGlobal() {
        return this.global;
    }
    
    public void setGlobal(final boolean global) {
        this.global = global;
    }
    
    public boolean isSession() {
        return this.session;
    }
    
    public void setSession(final boolean session) {
        this.session = session;
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
            this.acceptChild(visitor, this.like);
            this.acceptChild(visitor, this.where);
        }
        visitor.endVisit(this);
    }
}
