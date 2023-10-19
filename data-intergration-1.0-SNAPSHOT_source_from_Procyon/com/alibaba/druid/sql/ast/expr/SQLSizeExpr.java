// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.math.BigDecimal;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLSizeExpr extends SQLExprImpl
{
    private SQLExpr value;
    private Unit unit;
    
    public SQLSizeExpr() {
    }
    
    public SQLSizeExpr(final String value, final char unit) {
        this.unit = Unit.valueOf(Character.toString(unit).toUpperCase());
        if (value.indexOf(46) == -1) {
            this.value = new SQLIntegerExpr(Integer.parseInt(value));
        }
        else {
            this.value = new SQLNumberExpr(new BigDecimal(value));
        }
    }
    
    public SQLSizeExpr(final SQLExpr value, final Unit unit) {
        this.value = value;
        this.unit = unit;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.value != null) {
            this.value.accept(visitor);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.value);
    }
    
    public SQLExpr getValue() {
        return this.value;
    }
    
    public void setValue(final SQLExpr value) {
        this.value = value;
    }
    
    public Unit getUnit() {
        return this.unit;
    }
    
    public void setUnit(final Unit unit) {
        this.unit = unit;
    }
    
    @Override
    public SQLSizeExpr clone() {
        final SQLSizeExpr x = new SQLSizeExpr();
        if (this.value != null) {
            x.setValue(this.value.clone());
        }
        x.unit = this.unit;
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
        final SQLSizeExpr that = (SQLSizeExpr)o;
        if (this.value != null) {
            if (this.value.equals(that.value)) {
                return this.unit == that.unit;
            }
        }
        else if (that.value == null) {
            return this.unit == that.unit;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.value != null) ? this.value.hashCode() : 0;
        result = 31 * result + ((this.unit != null) ? this.unit.hashCode() : 0);
        return result;
    }
    
    public enum Unit
    {
        B, 
        K, 
        M, 
        G, 
        T, 
        P, 
        E;
    }
}
