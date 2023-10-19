// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableSetComment extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLExpr comment;
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.comment = comment;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.comment);
        }
        visitor.endVisit(this);
    }
}
