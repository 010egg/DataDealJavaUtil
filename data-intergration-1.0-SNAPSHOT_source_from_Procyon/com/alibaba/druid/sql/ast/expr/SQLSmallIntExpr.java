// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;

public class SQLSmallIntExpr extends SQLNumericLiteralExpr implements SQLValuableExpr
{
    private Short value;
    
    public SQLSmallIntExpr() {
    }
    
    public SQLSmallIntExpr(final short value) {
        this.value = value;
    }
    
    public SQLSmallIntExpr(String value) {
        if (value.startsWith("--")) {
            value = value.substring(2);
        }
        this.value = Short.valueOf(value);
    }
    
    @Override
    public SQLSmallIntExpr clone() {
        return new SQLSmallIntExpr(this.value);
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
    public Short getValue() {
        return this.value;
    }
    
    public void setValue(final Short value) {
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
        final SQLSmallIntExpr other = (SQLSmallIntExpr)obj;
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
        this.setValue((Short)number);
    }
}
