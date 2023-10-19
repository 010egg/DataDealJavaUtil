// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MySqlEventSchedule extends MySqlObjectImpl
{
    private SQLExpr at;
    private SQLExpr every;
    private SQLExpr starts;
    private SQLExpr ends;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.at);
            this.acceptChild(visitor, this.every);
            this.acceptChild(visitor, this.starts);
            this.acceptChild(visitor, this.ends);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getAt() {
        return this.at;
    }
    
    public void setAt(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.at = x;
    }
    
    public SQLExpr getEvery() {
        return this.every;
    }
    
    public void setEvery(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.every = x;
    }
    
    public SQLExpr getStarts() {
        return this.starts;
    }
    
    public void setStarts(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.starts = x;
    }
    
    public SQLExpr getEnds() {
        return this.ends;
    }
    
    public void setEnds(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.ends = x;
    }
}
