// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowACLStatement extends SQLStatementImpl implements SQLShowStatement, SQLReplaceable
{
    protected SQLExprTableSource table;
    
    public SQLExprTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.table = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.table != null) {
            this.table.accept(visitor);
        }
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        return this.table != null && this.table.replace(expr, target);
    }
}
