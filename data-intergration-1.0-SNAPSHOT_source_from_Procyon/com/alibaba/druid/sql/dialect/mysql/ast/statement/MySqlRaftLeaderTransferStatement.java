// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;

public class MySqlRaftLeaderTransferStatement extends MySqlStatementImpl
{
    private SQLCharExpr shard;
    private SQLCharExpr from;
    private SQLCharExpr to;
    private SQLIntegerExpr timeout;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.shard);
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.to);
            this.acceptChild(visitor, this.timeout);
        }
        visitor.endVisit(this);
    }
    
    public SQLCharExpr getShard() {
        return this.shard;
    }
    
    public void setShard(final SQLCharExpr shard) {
        this.shard = shard;
    }
    
    public SQLCharExpr getFrom() {
        return this.from;
    }
    
    public void setFrom(final SQLCharExpr from) {
        this.from = from;
    }
    
    public SQLCharExpr getTo() {
        return this.to;
    }
    
    public void setTo(final SQLCharExpr to) {
        this.to = to;
    }
    
    public SQLIntegerExpr getTimeout() {
        return this.timeout;
    }
    
    public void setTimeout(final SQLIntegerExpr timeout) {
        this.timeout = timeout;
    }
}
