// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MysqlShowHtcStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private boolean full;
    private boolean isHis;
    
    public MysqlShowHtcStatement() {
        this.full = false;
        this.isHis = false;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public boolean isFull() {
        return this.full;
    }
    
    public void setFull(final boolean full) {
        this.full = full;
    }
    
    public boolean isHis() {
        return this.isHis;
    }
    
    public void setHis(final boolean his) {
        this.isHis = his;
    }
}
