// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDumpStatement extends SQLStatementImpl
{
    private boolean overwrite;
    private SQLExprTableSource into;
    private SQLSelect select;
    
    public SQLSelect getSelect() {
        return this.select;
    }
    
    public void setSelect(final SQLSelect x) {
        if (x != null) {
            x.setParent(this);
        }
        this.select = x;
    }
    
    public SQLExprTableSource getInto() {
        return this.into;
    }
    
    public void setInto(final SQLExpr x) {
        if (x == null) {
            return;
        }
        this.setInto(new SQLExprTableSource(x));
    }
    
    public void setInto(final SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.into = x;
    }
    
    public boolean isOverwrite() {
        return this.overwrite;
    }
    
    public void setOverwrite(final boolean overwrite) {
        this.overwrite = overwrite;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.into != null) {
                this.into.accept(visitor);
            }
            if (this.select != null) {
                this.select.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
}
