// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLPurgeLogsStatement extends SQLStatementImpl implements SQLDropStatement
{
    private boolean binary;
    private boolean master;
    private boolean all;
    private SQLExpr to;
    private SQLExpr before;
    
    public SQLPurgeLogsStatement() {
    }
    
    public SQLPurgeLogsStatement(final DbType dbType) {
        super(dbType);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.to);
            this.acceptChild(visitor, this.before);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getTo() {
        return this.to;
    }
    
    public void setTo(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.to = x;
    }
    
    public SQLExpr getBefore() {
        return this.before;
    }
    
    public void setBefore(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.before = x;
    }
    
    public boolean isBinary() {
        return this.binary;
    }
    
    public void setBinary(final boolean binary) {
        this.binary = binary;
    }
    
    public boolean isMaster() {
        return this.master;
    }
    
    public void setMaster(final boolean master) {
        this.master = master;
    }
    
    public boolean isAll() {
        return this.all;
    }
    
    public void setAll(final boolean all) {
        this.all = all;
    }
}
