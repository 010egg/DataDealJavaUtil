// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatement;

public class DrdsChangeDDLJob extends MySqlStatementImpl implements SQLStatement
{
    private long jobId;
    private boolean skip;
    private boolean add;
    private List<String> groupAndTableNameList;
    
    public DrdsChangeDDLJob() {
        this.jobId = 0L;
        this.skip = false;
        this.add = false;
        this.groupAndTableNameList = new ArrayList<String>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public long getJobId() {
        return this.jobId;
    }
    
    public void setJobId(final long jobId) {
        this.jobId = jobId;
    }
    
    public boolean isSkip() {
        return this.skip;
    }
    
    public void setSkip(final boolean skip) {
        this.skip = skip;
    }
    
    public boolean isAdd() {
        return this.add;
    }
    
    public void setAdd(final boolean add) {
        this.add = add;
    }
    
    public List<String> getGroupAndTableNameList() {
        return this.groupAndTableNameList;
    }
    
    public void addGroupAndTableNameList(final String groupAndTableName) {
        this.groupAndTableNameList.add(groupAndTableName);
    }
}
