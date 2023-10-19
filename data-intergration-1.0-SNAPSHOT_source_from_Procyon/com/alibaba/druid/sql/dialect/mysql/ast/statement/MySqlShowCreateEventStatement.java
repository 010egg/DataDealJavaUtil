// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;

public class MySqlShowCreateEventStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLExpr eventName;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.eventName);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getEventName() {
        return this.eventName;
    }
    
    public void setEventName(final SQLExpr eventName) {
        this.eventName = eventName;
    }
}
