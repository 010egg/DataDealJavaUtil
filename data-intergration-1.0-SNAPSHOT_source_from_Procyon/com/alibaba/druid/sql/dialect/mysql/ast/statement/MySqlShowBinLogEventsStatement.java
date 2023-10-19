// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLExpr;

public class MySqlShowBinLogEventsStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLExpr in;
    private SQLExpr from;
    private SQLLimit limit;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.in);
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.limit);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getIn() {
        return this.in;
    }
    
    public void setIn(final SQLExpr in) {
        this.in = in;
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
        if (limit != null) {
            limit.setParent(this);
        }
        this.limit = limit;
    }
}
