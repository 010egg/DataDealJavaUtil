// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;

public class MySqlRaftMemberChangeStatement extends MySqlStatementImpl
{
    private SQLCharExpr shard;
    private SQLCharExpr host;
    private SQLCharExpr status;
    private boolean force;
    private boolean noLeader;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.shard);
            this.acceptChild(visitor, this.host);
            this.acceptChild(visitor, this.status);
        }
        visitor.endVisit(this);
    }
    
    public SQLCharExpr getShard() {
        return this.shard;
    }
    
    public void setShard(final SQLCharExpr shard) {
        this.shard = shard;
    }
    
    public SQLCharExpr getHost() {
        return this.host;
    }
    
    public void setHost(final SQLCharExpr host) {
        this.host = host;
    }
    
    public SQLCharExpr getStatus() {
        return this.status;
    }
    
    public void setStatus(final SQLCharExpr status) {
        this.status = status;
    }
    
    public boolean isForce() {
        return this.force;
    }
    
    public void setForce(final boolean force) {
        this.force = force;
    }
    
    public boolean isNoLeader() {
        return this.noLeader;
    }
    
    public void setNoLeader(final boolean noLeader) {
        this.noLeader = noLeader;
    }
}
