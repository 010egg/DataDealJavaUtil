// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLDataTypeRefExpr extends SQLExprImpl
{
    private SQLDataType dataType;
    
    public SQLDataTypeRefExpr(final SQLDataType dataType) {
        this.dataType = dataType;
    }
    
    public SQLDataType getDataType() {
        return this.dataType;
    }
    
    public void setDataType(final SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.dataType = x;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLDataTypeRefExpr that = (SQLDataTypeRefExpr)o;
        return (this.dataType != null) ? this.dataType.equals(that.dataType) : (that.dataType == null);
    }
    
    @Override
    public int hashCode() {
        return (this.dataType != null) ? this.dataType.hashCode() : 0;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.dataType);
        }
        v.endVisit(this);
    }
    
    @Override
    public SQLExpr clone() {
        return new SQLDataTypeRefExpr((this.dataType == null) ? null : this.dataType.clone());
    }
}
