// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.SQLUtils;
import java.util.Arrays;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLObjectWithDataType;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLCastExpr extends SQLExprImpl implements SQLObjectWithDataType, SQLReplaceable
{
    protected boolean isTry;
    protected SQLExpr expr;
    protected SQLDataType dataType;
    
    public SQLCastExpr() {
    }
    
    public SQLCastExpr(final SQLExpr expr, final SQLDataType dataType) {
        this.setExpr(expr);
        this.setDataType(dataType);
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }
    
    @Override
    public SQLDataType getDataType() {
        return this.dataType;
    }
    
    public long dateTypeHashCode() {
        if (this.dataType == null) {
            return 0L;
        }
        return this.dataType.nameHashCode64();
    }
    
    @Override
    public void setDataType(final SQLDataType dataType) {
        if (dataType != null) {
            dataType.setParent(this);
        }
        this.dataType = dataType;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.expr != null) {
                this.expr.accept(visitor);
            }
            if (this.dataType != null) {
                this.dataType.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        return Arrays.asList(this.expr, this.dataType);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLCastExpr castExpr = (SQLCastExpr)o;
        if (this.isTry != castExpr.isTry) {
            return false;
        }
        if (this.expr != null) {
            if (this.expr.equals(castExpr.expr)) {
                return (this.dataType != null) ? this.dataType.equals(castExpr.dataType) : (castExpr.dataType == null);
            }
        }
        else if (castExpr.expr == null) {
            return (this.dataType != null) ? this.dataType.equals(castExpr.dataType) : (castExpr.dataType == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.isTry ? 1 : 0;
        result = 31 * result + ((this.expr != null) ? this.expr.hashCode() : 0);
        result = 31 * result + ((this.dataType != null) ? this.dataType.hashCode() : 0);
        return result;
    }
    
    @Override
    public SQLDataType computeDataType() {
        return this.dataType;
    }
    
    @Override
    public SQLCastExpr clone() {
        final SQLCastExpr x = new SQLCastExpr();
        x.isTry = this.isTry;
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        if (this.dataType != null) {
            x.setDataType(this.dataType.clone());
        }
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.expr == expr) {
            this.setExpr(target);
            return true;
        }
        return false;
    }
    
    public boolean isTry() {
        return this.isTry;
    }
    
    public void setTry(final boolean aTry) {
        this.isTry = aTry;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this);
    }
}
