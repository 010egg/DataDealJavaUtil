// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLDefaultExpr extends SQLExprImpl implements SQLLiteralExpr
{
    @Override
    public boolean equals(final Object o) {
        return o instanceof SQLDefaultExpr;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public String toString() {
        return "DEFAULT";
    }
    
    @Override
    public SQLDefaultExpr clone() {
        return new SQLDefaultExpr();
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
}
