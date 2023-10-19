// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class OdpsAddUserStatement extends SQLStatementImpl
{
    private SQLName user;
    
    public OdpsAddUserStatement() {
        super(DbType.odps);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OdpsASTVisitor)visitor);
    }
    
    public void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.user);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getUser() {
        return this.user;
    }
    
    public void setUser(final SQLName user) {
        if (user != null) {
            user.setParent(this);
        }
        this.user = user;
    }
}
