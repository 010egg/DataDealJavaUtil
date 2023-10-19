// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowRoleStatement extends SQLStatementImpl implements SQLShowStatement
{
    private SQLName grant;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.grant);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getGrant() {
        return this.grant;
    }
    
    public void setGrant(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.grant = x;
    }
}
