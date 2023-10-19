// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLLimit;

public class MySqlShowWarningsStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private boolean count;
    private SQLLimit limit;
    
    public MySqlShowWarningsStatement() {
        this.count = false;
    }
    
    public boolean isCount() {
        return this.count;
    }
    
    public void setCount(final boolean count) {
        this.count = count;
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
            this.acceptChild(visitor, this.limit);
        }
        visitor.endVisit(this);
    }
}
