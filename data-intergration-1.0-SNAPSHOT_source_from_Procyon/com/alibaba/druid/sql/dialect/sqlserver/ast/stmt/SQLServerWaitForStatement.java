// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerStatementImpl;

public class SQLServerWaitForStatement extends SQLServerStatementImpl implements SQLServerStatement
{
    private SQLExpr delay;
    private SQLExpr time;
    private SQLStatement statement;
    private SQLExpr timeout;
    
    @Override
    public void accept0(final SQLServerASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.delay);
            this.acceptChild(visitor, this.time);
            this.acceptChild(visitor, this.statement);
            this.acceptChild(visitor, this.timeout);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getDelay() {
        return this.delay;
    }
    
    public void setDelay(final SQLExpr delay) {
        this.delay = delay;
    }
    
    public SQLExpr getTime() {
        return this.time;
    }
    
    public void setTime(final SQLExpr time) {
        this.time = time;
    }
    
    public SQLStatement getStatement() {
        return this.statement;
    }
    
    public void setStatement(final SQLStatement statement) {
        this.statement = statement;
    }
    
    public SQLExpr getTimeout() {
        return this.timeout;
    }
    
    public void setTimeout(final SQLExpr timeout) {
        this.timeout = timeout;
    }
}
