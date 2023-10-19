// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableRenameColumn extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLName column;
    private SQLName to;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.column);
            this.acceptChild(visitor, this.to);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getColumn() {
        return this.column;
    }
    
    public void setColumn(final SQLName column) {
        if (column != null) {
            column.setParent(this);
        }
        this.column = column;
    }
    
    public SQLName getTo() {
        return this.to;
    }
    
    public void setTo(final SQLName to) {
        if (to != null) {
            to.setParent(this);
        }
        this.to = to;
    }
}
