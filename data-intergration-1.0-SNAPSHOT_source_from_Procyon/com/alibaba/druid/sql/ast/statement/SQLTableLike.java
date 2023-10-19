// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLTableLike extends SQLObjectImpl implements SQLTableElement
{
    private SQLExprTableSource table;
    private boolean includeProperties;
    private boolean excludeProperties;
    
    public SQLTableLike() {
        this.includeProperties = false;
        this.excludeProperties = false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.table);
        }
        v.endVisit(this);
    }
    
    @Override
    public SQLTableLike clone() {
        final SQLTableLike x = new SQLTableLike();
        if (this.table != null) {
            x.setTable(this.table.clone());
        }
        return x;
    }
    
    public SQLExprTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.table = x;
    }
    
    public boolean isIncludeProperties() {
        return this.includeProperties;
    }
    
    public void setIncludeProperties(final boolean includeProperties) {
        this.includeProperties = includeProperties;
    }
    
    public boolean isExcludeProperties() {
        return this.excludeProperties;
    }
    
    public void setExcludeProperties(final boolean excludeProperties) {
        this.excludeProperties = excludeProperties;
    }
}
