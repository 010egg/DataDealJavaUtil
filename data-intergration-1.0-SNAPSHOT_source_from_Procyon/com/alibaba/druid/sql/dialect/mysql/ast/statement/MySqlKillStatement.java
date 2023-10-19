// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class MySqlKillStatement extends SQLStatementImpl
{
    private Type type;
    private List<SQLExpr> threadIds;
    
    public MySqlKillStatement() {
        this.threadIds = new ArrayList<SQLExpr>();
    }
    
    public Type getType() {
        return this.type;
    }
    
    public void setType(final Type type) {
        this.type = type;
    }
    
    public SQLExpr getThreadId() {
        return this.threadIds.get(0);
    }
    
    public void setThreadId(final SQLExpr threadId) {
        if (this.threadIds.size() == 0) {
            this.threadIds.add(threadId);
            return;
        }
        this.threadIds.set(0, threadId);
    }
    
    public List<SQLExpr> getThreadIds() {
        return this.threadIds;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.threadIds);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
    
    public enum Type
    {
        CONNECTION, 
        QUERY;
    }
}
