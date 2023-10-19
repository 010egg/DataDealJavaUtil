// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class OracleIntervalExpr extends SQLExprImpl implements SQLLiteralExpr, OracleExpr, SQLReplaceable
{
    private SQLExpr value;
    private OracleIntervalType type;
    private SQLExpr precision;
    private Integer factionalSecondsPrecision;
    private OracleIntervalType toType;
    private SQLExpr toFactionalSecondsPrecision;
    
    @Override
    public OracleIntervalExpr clone() {
        final OracleIntervalExpr x = new OracleIntervalExpr();
        if (this.value != null) {
            x.setValue(this.value.clone());
        }
        x.type = this.type;
        x.precision = this.precision;
        x.factionalSecondsPrecision = this.factionalSecondsPrecision;
        x.toType = this.toType;
        x.toFactionalSecondsPrecision = this.toFactionalSecondsPrecision;
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.value == expr) {
            this.setValue(target);
            return true;
        }
        if (this.precision == expr) {
            this.setPrecision(target);
            return true;
        }
        if (this.toFactionalSecondsPrecision == expr) {
            this.setToFactionalSecondsPrecision(target);
            return true;
        }
        return false;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.value);
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
    
    public OracleIntervalType getType() {
        return this.type;
    }
    
    public void setType(final OracleIntervalType type) {
        this.type = type;
    }
    
    public SQLExpr getPrecision() {
        return this.precision;
    }
    
    public void setPrecision(final Integer precision) {
        this.setPrecision(new SQLIntegerExpr(precision));
    }
    
    public void setPrecision(final SQLExpr precision) {
        if (precision != null) {
            precision.setParent(this);
        }
        this.precision = precision;
    }
    
    public Integer getFactionalSecondsPrecision() {
        return this.factionalSecondsPrecision;
    }
    
    public void setFactionalSecondsPrecision(final Integer factionalSecondsPrecision) {
        this.factionalSecondsPrecision = factionalSecondsPrecision;
    }
    
    public OracleIntervalType getToType() {
        return this.toType;
    }
    
    public void setToType(final OracleIntervalType toType) {
        this.toType = toType;
    }
    
    public SQLExpr getToFactionalSecondsPrecision() {
        return this.toFactionalSecondsPrecision;
    }
    
    public void setToFactionalSecondsPrecision(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.toFactionalSecondsPrecision = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.value);
            this.acceptChild(visitor, this.precision);
            this.acceptChild(visitor, this.toFactionalSecondsPrecision);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.factionalSecondsPrecision == null) ? 0 : this.factionalSecondsPrecision.hashCode());
        result = 31 * result + ((this.precision == null) ? 0 : this.precision.hashCode());
        result = 31 * result + ((this.toFactionalSecondsPrecision == null) ? 0 : this.toFactionalSecondsPrecision.hashCode());
        result = 31 * result + ((this.toType == null) ? 0 : this.toType.hashCode());
        result = 31 * result + ((this.type == null) ? 0 : this.type.hashCode());
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
        final OracleIntervalExpr other = (OracleIntervalExpr)obj;
        if (this.factionalSecondsPrecision == null) {
            if (other.factionalSecondsPrecision != null) {
                return false;
            }
        }
        else if (!this.factionalSecondsPrecision.equals(other.factionalSecondsPrecision)) {
            return false;
        }
        if (this.precision == null) {
            if (other.precision != null) {
                return false;
            }
        }
        else if (!this.precision.equals(other.precision)) {
            return false;
        }
        if (this.toFactionalSecondsPrecision == null) {
            if (other.toFactionalSecondsPrecision != null) {
                return false;
            }
        }
        else if (!this.toFactionalSecondsPrecision.equals(other.toFactionalSecondsPrecision)) {
            return false;
        }
        if (this.toType != other.toType) {
            return false;
        }
        if (this.type != other.type) {
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
}
