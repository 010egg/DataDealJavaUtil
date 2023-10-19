// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableRenameIndex extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLName name;
    private SQLName to;
    
    public SQLAlterTableRenameIndex(final SQLName name, final SQLName to) {
        this.setName(name);
        this.setTo(to);
    }
    
    public SQLAlterTableRenameIndex() {
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.to);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.name = x;
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
}
