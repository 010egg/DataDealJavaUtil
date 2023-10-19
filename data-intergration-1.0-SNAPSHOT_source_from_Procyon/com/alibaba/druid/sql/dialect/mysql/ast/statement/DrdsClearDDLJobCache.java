// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatement;

public class DrdsClearDDLJobCache extends MySqlStatementImpl implements SQLStatement
{
    private boolean allJobs;
    private List<Long> jobIds;
    
    public DrdsClearDDLJobCache() {
        this.allJobs = false;
        this.jobIds = new ArrayList<Long>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public boolean isAllJobs() {
        return this.allJobs;
    }
    
    public void setAllJobs(final boolean allJobs) {
        this.allJobs = allJobs;
    }
    
    public List<Long> getJobIds() {
        return this.jobIds;
    }
    
    public void addJobId(final long id) {
        this.jobIds.add(id);
    }
}
