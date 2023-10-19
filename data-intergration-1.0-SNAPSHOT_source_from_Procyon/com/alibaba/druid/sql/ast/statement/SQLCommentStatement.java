// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCommentStatement extends SQLStatementImpl
{
    private SQLExprTableSource on;
    private Type type;
    private SQLExpr comment;
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr comment) {
        this.comment = comment;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public void setType(final Type type) {
        this.type = type;
    }
    
    public SQLExprTableSource getOn() {
        return this.on;
    }
    
    public void setOn(final SQLExprTableSource on) {
        if (on != null) {
            on.setParent(this);
        }
        this.on = on;
    }
    
    public void setOn(final SQLName on) {
        this.setOn(new SQLExprTableSource(on));
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.on);
            this.acceptChild(visitor, this.comment);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.on != null) {
            children.add(this.on);
        }
        if (this.comment != null) {
            children.add(this.comment);
        }
        return children;
    }
    
    public enum Type
    {
        TABLE, 
        COLUMN;
    }
}
