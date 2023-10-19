// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLFlashbackExpr extends SQLExprImpl implements SQLReplaceable
{
    private Type type;
    private SQLExpr expr;
    
    public SQLFlashbackExpr() {
    }
    
    public SQLFlashbackExpr(final Type type, final SQLExpr expr) {
        this.type = type;
        this.setExpr(expr);
    }
    
    public Type getType() {
        return this.type;
    }
    
    public void setType(final Type type) {
        this.type = type;
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.expr == expr) {
            this.setExpr(target);
            return true;
        }
        return false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.expr != null) {
            this.expr.accept(visitor);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.expr);
    }
    
    @Override
    public SQLFlashbackExpr clone() {
        final SQLFlashbackExpr x = new SQLFlashbackExpr();
        x.type = this.type;
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        return x;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLFlashbackExpr that = (SQLFlashbackExpr)o;
        return this.type == that.type && ((this.expr != null) ? this.expr.equals(that.expr) : (that.expr == null));
    }
    
    @Override
    public int hashCode() {
        int result = (this.type != null) ? this.type.hashCode() : 0;
        result = 31 * result + ((this.expr != null) ? this.expr.hashCode() : 0);
        return result;
    }
    
    public enum Type
    {
        SCN, 
        TIMESTAMP;
    }
}
