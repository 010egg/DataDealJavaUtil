// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public final class SQLAllColumnExpr extends SQLExprImpl
{
    private transient SQLTableSource resolvedTableSource;
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append('*');
        }
        catch (IOException e) {
            throw new FastsqlException("output error", e);
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
        return o instanceof SQLAllColumnExpr;
    }
    
    @Override
    public SQLAllColumnExpr clone() {
        final SQLAllColumnExpr x = new SQLAllColumnExpr();
        x.resolvedTableSource = this.resolvedTableSource;
        return x;
    }
    
    public SQLTableSource getResolvedTableSource() {
        return this.resolvedTableSource;
    }
    
    public void setResolvedTableSource(final SQLTableSource resolvedTableSource) {
        this.resolvedTableSource = resolvedTableSource;
    }
    
    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
}
