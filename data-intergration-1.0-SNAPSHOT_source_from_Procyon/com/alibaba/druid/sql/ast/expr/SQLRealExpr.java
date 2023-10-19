// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;

public class SQLRealExpr extends SQLNumericLiteralExpr implements SQLValuableExpr
{
    private float value;
    
    public SQLRealExpr() {
    }
    
    public SQLRealExpr(final float value) {
        this.value = value;
    }
    
    public SQLRealExpr(final String value) {
        this.value = Float.valueOf(value);
    }
    
    @Override
    public SQLRealExpr clone() {
        return new SQLRealExpr(this.value);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
    
    @Override
    public Number getNumber() {
        return this.value;
    }
    
    @Override
    public Float getValue() {
        return this.value;
    }
    
    public void setValue(final Float value) {
        this.value = value;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public void setValue(final float value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLRealExpr that = (SQLRealExpr)o;
        return Float.compare(that.value, this.value) == 0;
    }
    
    @Override
    public int hashCode() {
        return (this.value != 0.0f) ? Float.floatToIntBits(this.value) : 0;
    }
    
    @Override
    public void setNumber(final Number number) {
        if (number == null) {
            this.setValue(null);
            return;
        }
        this.setValue(number.floatValue());
    }
}
