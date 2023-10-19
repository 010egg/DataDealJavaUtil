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
import java.math.BigDecimal;
import com.alibaba.druid.sql.ast.SQLDataType;

public class SQLDecimalExpr extends SQLNumericLiteralExpr implements SQLValuableExpr, Comparable<SQLDecimalExpr>
{
    public static final SQLDataType DATA_TYPE;
    private BigDecimal value;
    private transient String literal;
    
    public SQLDecimalExpr() {
    }
    
    public SQLDecimalExpr(final BigDecimal value) {
        this.value = value;
    }
    
    public SQLDecimalExpr(final String value) {
        this.value = new BigDecimal(value);
        this.literal = value;
    }
    
    public String getLiteral() {
        return this.literal;
    }
    
    @Override
    public SQLDecimalExpr clone() {
        return new SQLDecimalExpr(this.value);
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
    public BigDecimal getValue() {
        return this.value;
    }
    
    public void setValue(final BigDecimal value) {
        this.value = value;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
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
        final SQLDecimalExpr other = (SQLDecimalExpr)obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        }
        else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }
    
    @Override
    public void setNumber(final Number number) {
        if (number == null) {
            this.setValue(null);
            return;
        }
        this.setValue((BigDecimal)number);
    }
    
    @Override
    public int compareTo(final SQLDecimalExpr o) {
        return this.value.compareTo(o.value);
    }
    
    static {
        DATA_TYPE = new SQLDataTypeImpl("DECIMAL");
    }
}
