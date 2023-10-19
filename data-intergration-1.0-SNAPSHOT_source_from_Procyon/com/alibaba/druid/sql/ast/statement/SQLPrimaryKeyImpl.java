// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLPrimaryKeyImpl extends SQLUnique implements SQLPrimaryKey, SQLTableConstraint
{
    protected boolean disableNovalidate;
    protected boolean clustered;
    
    public SQLPrimaryKeyImpl() {
        this.disableNovalidate = false;
        this.clustered = false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
            this.acceptChild(visitor, this.getColumns());
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLPrimaryKeyImpl clone() {
        final SQLPrimaryKeyImpl x = new SQLPrimaryKeyImpl();
        this.cloneTo(x);
        return x;
    }
    
    public void cloneTo(final SQLPrimaryKeyImpl x) {
        super.cloneTo(x);
        x.disableNovalidate = this.disableNovalidate;
        x.clustered = this.clustered;
    }
    
    public boolean isDisableNovalidate() {
        return this.disableNovalidate;
    }
    
    public void setDisableNovalidate(final boolean disableNovalidate) {
        this.disableNovalidate = disableNovalidate;
    }
    
    public boolean isClustered() {
        return this.clustered;
    }
    
    public void setClustered(final boolean clustered) {
        this.clustered = clustered;
    }
}
