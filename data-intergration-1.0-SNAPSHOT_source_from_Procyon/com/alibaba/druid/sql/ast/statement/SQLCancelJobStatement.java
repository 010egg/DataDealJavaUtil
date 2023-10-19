// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCancelJobStatement extends SQLStatementImpl
{
    private SQLName jobName;
    private boolean isImport;
    
    public SQLCancelJobStatement() {
        this.isImport = false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.jobName);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getJobName() {
        return this.jobName;
    }
    
    public void setJobName(final SQLName jobName) {
        this.jobName = jobName;
    }
    
    public boolean isImport() {
        return this.isImport;
    }
    
    public void setImport(final boolean anImport) {
        this.isImport = anImport;
    }
}
