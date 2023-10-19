// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowGrantsStatement extends SQLStatementImpl implements SQLShowStatement
{
    protected SQLExpr user;
    protected SQLExpr on;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.user);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getUser() {
        return this.user;
    }
    
    public void setUser(final SQLExpr user) {
        if (user != null) {
            user.setParent(this);
        }
        this.user = user;
    }
    
    public SQLExpr getOn() {
        return this.on;
    }
    
    public void setOn(final SQLExpr x) {
        if (x == null) {
            x.setParent(this);
        }
        this.on = x;
    }
}
