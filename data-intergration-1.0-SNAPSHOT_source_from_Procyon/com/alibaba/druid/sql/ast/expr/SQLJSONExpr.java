// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLJSONExpr extends SQLExprImpl implements SQLValuableExpr
{
    public static final SQLDataType DATA_TYPE;
    protected String literal;
    
    public SQLJSONExpr() {
    }
    
    public SQLJSONExpr(final String literal) {
        this.literal = literal;
    }
    
    @Override
    public SQLJSONExpr clone() {
        final SQLJSONExpr x = new SQLJSONExpr(this.literal);
        return x;
    }
    
    @Override
    public String getValue() {
        return this.literal;
    }
    
    public String getLiteral() {
        return this.literal;
    }
    
    public void setLiteral(final String literal) {
        this.literal = literal;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLJSONExpr that = (SQLJSONExpr)o;
        return (this.literal != null) ? this.literal.equals(that.literal) : (that.literal == null);
    }
    
    @Override
    public int hashCode() {
        return (this.literal != null) ? this.literal.hashCode() : 0;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this, (DbType)null);
    }
    
    @Override
    public SQLDataType computeDataType() {
        return SQLJSONExpr.DATA_TYPE;
    }
    
    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
    
    static {
        DATA_TYPE = new SQLDataTypeImpl("JSON");
    }
}
