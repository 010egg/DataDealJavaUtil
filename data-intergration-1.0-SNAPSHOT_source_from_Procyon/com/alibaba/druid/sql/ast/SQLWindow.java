// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLWindow extends SQLObjectImpl
{
    private SQLName name;
    private SQLOver over;
    
    public SQLWindow(final SQLName name, final SQLOver over) {
        this.setName(name);
        this.setOver(over);
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
    
    public SQLOver getOver() {
        return this.over;
    }
    
    public void setOver(final SQLOver x) {
        if (x != null) {
            x.setParent(this);
        }
        this.over = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.name);
            this.acceptChild(v, this.over);
        }
        v.endVisit(this);
    }
}
