// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;

public class OracleBinaryFloatExpr extends SQLNumericLiteralExpr implements OracleExpr
{
    private Float value;
    
    public OracleBinaryFloatExpr() {
    }
    
    public OracleBinaryFloatExpr(final Float value) {
        this.value = value;
    }
    
    @Override
    public OracleBinaryFloatExpr clone() {
        return new OracleBinaryFloatExpr(this.value);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
    
    @Override
    public Number getNumber() {
        return this.value;
    }
    
    public Float getValue() {
        return this.value;
    }
    
    public void setValue(final Float value) {
        this.value = value;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
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
        final OracleBinaryFloatExpr other = (OracleBinaryFloatExpr)obj;
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
        this.setValue(number.floatValue());
    }
}
