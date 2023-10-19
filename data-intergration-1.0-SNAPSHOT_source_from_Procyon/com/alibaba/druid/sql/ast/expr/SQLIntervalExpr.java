// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLIntervalExpr extends SQLExprImpl implements SQLReplaceable
{
    public static final SQLDataType DATA_TYPE;
    private SQLExpr value;
    private SQLIntervalUnit unit;
    
    public SQLIntervalExpr() {
    }
    
    public SQLIntervalExpr(final SQLExpr value, final SQLIntervalUnit unit) {
        this.setValue(value);
        this.unit = unit;
    }
    
    @Override
    public SQLIntervalExpr clone() {
        final SQLIntervalExpr x = new SQLIntervalExpr();
        if (this.value != null) {
            x.setValue(this.value.clone());
        }
        x.unit = this.unit;
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.value == expr) {
            this.setValue(target);
            return true;
        }
        return false;
    }
    
    public SQLExpr getValue() {
        return this.value;
    }
    
    public void setValue(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.value = x;
    }
    
    public SQLIntervalUnit getUnit() {
        return this.unit;
    }
    
    public void setUnit(final SQLIntervalUnit unit) {
        this.unit = unit;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append("INTERVAL ");
            this.value.output(buf);
            if (this.unit != null) {
                buf.append(' ');
                buf.append(this.unit.name());
            }
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.value != null) {
            this.value.accept(visitor);
        }
        visitor.endVisit(this);
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
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final SQLIntervalExpr other = (SQLIntervalExpr)obj;
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
        return SQLIntervalExpr.DATA_TYPE;
    }
    
    static {
        DATA_TYPE = new SQLDataTypeImpl("interval");
    }
}
