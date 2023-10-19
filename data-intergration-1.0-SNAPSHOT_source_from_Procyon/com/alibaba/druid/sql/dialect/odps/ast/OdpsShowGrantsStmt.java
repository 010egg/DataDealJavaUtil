// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class OdpsShowGrantsStmt extends SQLStatementImpl
{
    private boolean label;
    private SQLExpr user;
    private SQLExpr objectType;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OdpsASTVisitor)visitor);
    }
    
    public void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.user);
            this.acceptChild(visitor, this.objectType);
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
    
    public SQLExpr getObjectType() {
        return this.objectType;
    }
    
    public void setObjectType(final SQLExpr objectType) {
        if (objectType != null) {
            objectType.setParent(this);
        }
        this.objectType = objectType;
    }
    
    public boolean isLabel() {
        return this.label;
    }
    
    public void setLabel(final boolean label) {
        this.label = label;
    }
}
