// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLDataType;

public class SQLFloatExpr extends SQLNumericLiteralExpr implements SQLValuableExpr, Comparable<SQLFloatExpr>
{
    public static final SQLDataType DATA_TYPE;
    private float value;
    
    public SQLFloatExpr() {
    }
    
    public SQLFloatExpr(final String value) {
        this.value = Float.parseFloat(value);
    }
    
    public SQLFloatExpr(final float value) {
        this.value = value;
    }
    
    @Override
    public SQLFloatExpr clone() {
        return new SQLFloatExpr(this.value);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
    
    @Override
    public Float getNumber() {
        return this.value;
    }
    
    @Override
    public Float getValue() {
        return this.value;
    }
    
    public void setValue(final float value) {
        this.value = value;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLFloatExpr that = (SQLFloatExpr)o;
        return Double.compare(that.value, this.value) == 0;
    }
    
    @Override
    public int hashCode() {
        final long temp = Double.doubleToLongBits(this.value);
        return (int)(temp ^ temp >>> 32);
    }
    
    @Override
    public void setNumber(final Number number) {
        if (number == null) {
            this.setValue(Float.NaN);
            return;
        }
        this.setValue(number.floatValue());
    }
    
    @Override
    public int compareTo(final SQLFloatExpr o) {
        return Float.compare(this.value, o.value);
    }
    
    static {
        DATA_TYPE = new SQLDataTypeImpl("FLOAT");
    }
}
