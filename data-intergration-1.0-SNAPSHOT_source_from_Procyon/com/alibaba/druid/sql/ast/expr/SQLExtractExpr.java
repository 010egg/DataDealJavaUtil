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
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLExtractExpr extends SQLExprImpl implements SQLReplaceable
{
    private SQLExpr value;
    private SQLIntervalUnit unit;
    
    @Override
    public SQLExtractExpr clone() {
        final SQLExtractExpr x = new SQLExtractExpr();
        if (this.value != null) {
            x.setValue(this.value.clone());
        }
        x.unit = this.unit;
        return x;
    }
    
    public SQLExpr getValue() {
        return this.value;
    }
    
    public void setValue(final SQLExpr value) {
        if (value != null) {
            value.setParent(this);
        }
        this.value = value;
    }
    
    public SQLIntervalUnit getUnit() {
        return this.unit;
    }
    
    public void setUnit(final SQLIntervalUnit unit) {
        this.unit = unit;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.value != null) {
            this.value.accept(visitor);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.value == expr) {
            this.setValue(target);
            return true;
        }
        return false;
    }
    
    @Override
    public List getChildren() {
        return Collections.singletonList(this.value);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.unit == null) ? 0 : this.unit.hashCode());
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
        if (!(obj instanceof SQLExtractExpr)) {
            return false;
        }
        final SQLExtractExpr other = (SQLExtractExpr)obj;
        if (this.unit != other.unit) {
            return false;
        }
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
    public SQLDataType computeDataType() {
        return SQLIntegerExpr.DATA_TYPE;
    }
}
