// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLExpr;

public class MySqlShowRelayLogEventsStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLExpr logName;
    private SQLExpr from;
    private SQLLimit limit;
    
    public SQLExpr getLogName() {
        return this.logName;
    }
    
    public void setLogName(final SQLExpr logName) {
        this.logName = logName;
    }
    
    public SQLExpr getFrom() {
        return this.from;
    }
    
    public void setFrom(final SQLExpr from) {
        this.from = from;
    }
    
    public SQLLimit getLimit() {
        return this.limit;
    }
    
    public void setLimit(final SQLLimit limit) {
        this.limit = limit;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.logName);
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.limit);
        }
        visitor.endVisit(this);
    }
}
