// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatement;

public class DrdsRemoveDDLJob extends MySqlStatementImpl implements SQLStatement
{
    private boolean allCompleted;
    private boolean allPending;
    private List<Long> jobIds;
    
    public DrdsRemoveDDLJob() {
        this.allCompleted = false;
        this.allPending = false;
        this.jobIds = new ArrayList<Long>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public boolean isAllCompleted() {
        return this.allCompleted;
    }
    
    public void setAllCompleted(final boolean allCompleted) {
        this.allCompleted = allCompleted;
    }
    
    public boolean isAllPending() {
        return this.allPending;
    }
    
    public void setAllPending(final boolean allPending) {
        this.allPending = allPending;
    }
    
    public List<Long> getJobIds() {
        return this.jobIds;
    }
    
    public void addJobId(final long id) {
        this.jobIds.add(id);
    }
}
