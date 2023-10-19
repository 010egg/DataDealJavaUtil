// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;

public class MySqlShowEngineStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLExpr name;
    private Option option;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getName() {
        return this.name;
    }
    
    public void setName(final SQLExpr name) {
        this.name = name;
    }
    
    public Option getOption() {
        return this.option;
    }
    
    public void setOption(final Option option) {
        this.option = option;
    }
    
    public enum Option
    {
        STATUS, 
        MUTEX;
    }
}
