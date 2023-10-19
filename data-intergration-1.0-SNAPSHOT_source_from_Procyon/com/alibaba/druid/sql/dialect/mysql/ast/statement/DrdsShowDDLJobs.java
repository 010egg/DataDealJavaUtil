// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLShowStatement;

public class DrdsShowDDLJobs extends MySqlStatementImpl implements SQLShowStatement
{
    private boolean full;
    private List<Long> jobIds;
    
    public DrdsShowDDLJobs() {
        this.full = false;
        this.jobIds = new ArrayList<Long>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public boolean isFull() {
        return this.full;
    }
    
    public void setFull(final boolean full) {
        this.full = full;
    }
    
    public List<Long> getJobIds() {
        return this.jobIds;
    }
    
    public void addJobId(final long id) {
        this.jobIds.add(id);
    }
}
