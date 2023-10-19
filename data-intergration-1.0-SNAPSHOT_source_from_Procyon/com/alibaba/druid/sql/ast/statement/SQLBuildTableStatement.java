// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLBuildTableStatement extends SQLStatementImpl
{
    private SQLName table;
    private SQLIntegerExpr version;
    private boolean withSplit;
    private boolean force;
    
    public SQLBuildTableStatement() {
        this.withSplit = false;
        this.force = false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.table);
            this.acceptChild(visitor, this.version);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getTable() {
        return this.table;
    }
    
    public void setTable(final SQLName table) {
        this.table = table;
    }
    
    public SQLIntegerExpr getVersion() {
        return this.version;
    }
    
    public void setVersion(final SQLIntegerExpr version) {
        this.version = version;
    }
    
    public boolean isWithSplit() {
        return this.withSplit;
    }
    
    public void setWithSplit(final boolean withSplit) {
        this.withSplit = withSplit;
    }
    
    public boolean isForce() {
        return this.force;
    }
    
    public void setForce(final boolean force) {
        this.force = force;
    }
}
