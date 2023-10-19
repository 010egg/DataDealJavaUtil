// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableAddSupplemental extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLTableElement element;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.element);
        }
        visitor.endVisit(this);
    }
    
    public SQLTableElement getElement() {
        return this.element;
    }
    
    public void setElement(final SQLTableElement x) {
        if (x != null) {
            x.setParent(this);
        }
        this.element = x;
    }
}
