// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowEnginesStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private boolean storage;
    
    public MySqlShowEnginesStatement() {
        this.storage = false;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public boolean isStorage() {
        return this.storage;
    }
    
    public void setStorage(final boolean storage) {
        this.storage = storage;
    }
}
