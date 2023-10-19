// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAlterDatabaseItem;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MySqlAlterDatabaseKillJob extends MySqlObjectImpl implements SQLAlterDatabaseItem
{
    private SQLName jobType;
    private SQLName jobId;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.jobType);
            this.acceptChild(visitor, this.jobId);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getJobType() {
        return this.jobType;
    }
    
    public void setJobType(final SQLName jobType) {
        this.jobType = jobType;
    }
    
    public SQLName getJobId() {
        return this.jobId;
    }
    
    public void setJobId(final SQLName jobId) {
        this.jobId = jobId;
    }
}
