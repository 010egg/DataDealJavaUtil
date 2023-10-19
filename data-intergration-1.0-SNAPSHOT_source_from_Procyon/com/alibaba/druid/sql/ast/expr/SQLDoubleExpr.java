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

public class SQLDoubleExpr extends SQLNumericLiteralExpr implements SQLValuableExpr, Comparable<SQLDoubleExpr>
{
    public static final SQLDataType DATA_TYPE;
    private double value;
    
    public SQLDoubleExpr() {
    }
    
    public SQLDoubleExpr(final String value) {
        this.value = Double.parseDouble(value);
    }
    
    public SQLDoubleExpr(final double value) {
        this.value = value;
    }
    
    @Override
    public SQLDoubleExpr clone() {
        return new SQLDoubleExpr(this.value);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
    
    @Override
    public Double getNumber() {
        return this.value;
    }
    
    @Override
    public Double getValue() {
        return this.value;
    }
    
    public void setValue(final double value) {
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
        final SQLDoubleExpr that = (SQLDoubleExpr)o;
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
            this.setValue(Double.NaN);
            return;
        }
        this.setValue(number.doubleValue());
    }
    
    @Override
    public int compareTo(final SQLDoubleExpr o) {
        return Double.compare(this.value, o.value);
    }
    
    static {
        DATA_TYPE = new SQLDataTypeImpl("DOUBLE");
    }
}
