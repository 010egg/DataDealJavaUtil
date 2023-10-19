// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLEvalVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public final class SQLNullExpr extends SQLExprImpl implements SQLLiteralExpr, SQLValuableExpr
{
    public SQLNullExpr() {
    }
    
    public SQLNullExpr(final SQLObject parent) {
        this.parent = parent;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append("NULL");
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof SQLNullExpr;
    }
    
    @Override
    public Object getValue() {
        return SQLEvalVisitor.EVAL_VALUE_NULL;
    }
    
    @Override
    public SQLNullExpr clone() {
        return new SQLNullExpr();
    }
    
    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
}
