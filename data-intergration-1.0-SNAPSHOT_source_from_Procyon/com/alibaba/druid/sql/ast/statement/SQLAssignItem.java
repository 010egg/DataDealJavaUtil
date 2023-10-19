// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLAssignItem extends SQLExprImpl implements SQLReplaceable
{
    private SQLExpr target;
    private SQLExpr value;
    
    public SQLAssignItem() {
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLAssignItem that = (SQLAssignItem)o;
        if (this.target != null) {
            if (this.target.equals(that.target)) {
                return (this.value != null) ? this.value.equals(that.value) : (that.value == null);
            }
        }
        else if (that.target == null) {
            return (this.value != null) ? this.value.equals(that.value) : (that.value == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.target != null) ? this.target.hashCode() : 0;
        result = 31 * result + ((this.value != null) ? this.value.hashCode() : 0);
        return result;
    }
    
    public SQLAssignItem(final SQLExpr target, final SQLExpr value) {
        this.setTarget(target);
        this.setValue(value);
    }
    
    @Override
    public SQLAssignItem clone() {
        final SQLAssignItem x = new SQLAssignItem();
        if (this.target != null) {
            x.setTarget(this.target.clone());
        }
        if (this.value != null) {
            x.setValue(this.value.clone());
        }
        return x;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return null;
    }
    
    public SQLExpr getTarget() {
        return this.target;
    }
    
    public void setTarget(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.target = x;
    }
    
    public SQLExpr getValue() {
        return this.value;
    }
    
    public void setValue(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.value = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.target);
            this.acceptChild(visitor, this.value);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.target == expr) {
            this.setTarget(target);
            return true;
        }
        if (this.value == expr) {
            this.setValue(target);
            return true;
        }
        return false;
    }
}
