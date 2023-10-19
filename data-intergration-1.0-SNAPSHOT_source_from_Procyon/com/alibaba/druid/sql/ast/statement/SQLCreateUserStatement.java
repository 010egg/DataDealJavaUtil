// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCreateUserStatement extends SQLStatementImpl
{
    private SQLName user;
    private SQLExpr password;
    private SQLName defaultTableSpace;
    
    public SQLName getUser() {
        return this.user;
    }
    
    public void setUser(final SQLName user) {
        if (user != null) {
            user.setParent(this);
        }
        this.user = user;
    }
    
    public SQLExpr getPassword() {
        return this.password;
    }
    
    public void setPassword(final SQLExpr password) {
        if (password != null) {
            password.setParent(this);
        }
        this.password = password;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.user);
            this.acceptChild(visitor, this.password);
        }
        visitor.endVisit(this);
    }
}
