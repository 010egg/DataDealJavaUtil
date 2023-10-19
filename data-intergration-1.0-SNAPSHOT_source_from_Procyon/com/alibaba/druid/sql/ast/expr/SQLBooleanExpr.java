// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public final class SQLBooleanExpr extends SQLExprImpl implements SQLExpr, SQLLiteralExpr, SQLValuableExpr
{
    public static final SQLDataType DATA_TYPE;
    private boolean value;
    
    public SQLBooleanExpr() {
    }
    
    public SQLBooleanExpr(final boolean value) {
        this.value = value;
    }
    
    public boolean getBooleanValue() {
        return this.value;
    }
    
    @Override
    public Boolean getValue() {
        return this.value;
    }
    
    public void setValue(final boolean value) {
        this.value = value;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append(this.value ? "true" : "false");
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + (this.value ? 1231 : 1237);
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
        final SQLBooleanExpr other = (SQLBooleanExpr)obj;
        return this.value == other.value;
    }
    
    @Override
    public SQLDataType computeDataType() {
        return SQLBooleanExpr.DATA_TYPE;
    }
    
    @Override
    public SQLBooleanExpr clone() {
        return new SQLBooleanExpr(this.value);
    }
    
    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
    
    static {
        DATA_TYPE = new SQLDataTypeImpl("BOOLEAN");
    }
    
    public enum Type
    {
        ON_OFF;
    }
}
