// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatement;

public class DrdsRollbackDDLJob extends MySqlStatementImpl implements SQLStatement
{
    private List<Long> jobIds;
    
    public DrdsRollbackDDLJob() {
        this.jobIds = new ArrayList<Long>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public List<Long> getJobIds() {
        return this.jobIds;
    }
    
    public void addJobId(final long id) {
        this.jobIds.add(id);
    }
}
