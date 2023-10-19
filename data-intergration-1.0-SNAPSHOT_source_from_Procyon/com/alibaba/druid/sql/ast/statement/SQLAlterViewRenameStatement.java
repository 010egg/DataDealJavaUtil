// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterViewRenameStatement extends SQLStatementImpl implements SQLAlterStatement
{
    private SQLName name;
    private SQLName to;
    private SQLName changeOwnerTo;
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public SQLName getTo() {
        return this.to;
    }
    
    public void setTo(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.to = x;
    }
    
    public SQLName getChangeOwnerTo() {
        return this.changeOwnerTo;
    }
    
    public void setChangeOwnerTo(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.changeOwnerTo = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.to);
            this.acceptChild(visitor, this.changeOwnerTo);
        }
        visitor.endVisit(this);
    }
}
