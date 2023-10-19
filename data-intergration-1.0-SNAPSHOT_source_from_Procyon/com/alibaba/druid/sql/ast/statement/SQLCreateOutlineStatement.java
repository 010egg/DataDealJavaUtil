// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCreateOutlineStatement extends SQLStatementImpl
{
    private SQLName name;
    private SQLExpr where;
    private SQLStatement on;
    private SQLStatement to;
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.name = x;
    }
    
    public SQLStatement getOn() {
        return this.on;
    }
    
    public void setOn(final SQLStatement x) {
        if (x != null) {
            x.setParent(this);
        }
        this.on = x;
    }
    
    public SQLStatement getTo() {
        return this.to;
    }
    
    public void setTo(final SQLStatement x) {
        if (x != null) {
            x.setParent(this);
        }
        this.to = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.name);
            this.acceptChild(v, this.where);
            this.acceptChild(v, this.on);
            this.acceptChild(v, this.to);
        }
        v.endVisit(this);
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
}
