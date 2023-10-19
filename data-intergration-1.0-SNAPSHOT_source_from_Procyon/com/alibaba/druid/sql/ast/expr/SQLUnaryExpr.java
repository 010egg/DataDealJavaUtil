// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import java.io.Serializable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLUnaryExpr extends SQLExprImpl implements Serializable, SQLReplaceable
{
    private static final long serialVersionUID = 1L;
    private SQLExpr expr;
    private SQLUnaryOperator operator;
    
    public SQLUnaryExpr() {
    }
    
    public SQLUnaryExpr(final SQLUnaryOperator operator, final SQLExpr expr) {
        this.operator = operator;
        this.setExpr(expr);
    }
    
    @Override
    public SQLUnaryExpr clone() {
        final SQLUnaryExpr x = new SQLUnaryExpr();
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        x.operator = this.operator;
        return x;
    }
    
    public SQLUnaryOperator getOperator() {
        return this.operator;
    }
    
    public void setOperator(final SQLUnaryOperator operator) {
        this.operator = operator;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
        result = 31 * result + ((this.operator == null) ? 0 : this.operator.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final SQLUnaryExpr other = (SQLUnaryExpr)obj;
        if (this.expr == null) {
            if (other.expr != null) {
                return false;
            }
        }
        else if (!this.expr.equals(other.expr)) {
            return false;
        }
        return this.operator == other.operator;
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
    public SQLDataType computeDataType() {
        switch (this.operator) {
            case Plus:
            case Negative:
            case Compl:
            case Not: {
                return this.expr.computeDataType();
            }
            default: {
                return null;
            }
        }
    }
}
