// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSelect;

public class MySqlShowPlanCacheStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLSelect select;
    
    public MySqlShowPlanCacheStatement() {
    }
    
    public MySqlShowPlanCacheStatement(final SQLSelect select) {
        this.setSelect(select);
    }
    
    @Override
    public void accept0(final MySqlASTVisitor v) {
        if (v.visit(this) && this.select != null) {
            this.select.accept(v);
        }
        v.endVisit(this);
    }
    
    public SQLSelect getSelect() {
        return this.select;
    }
    
    public void setSelect(final SQLSelect x) {
        if (x != null) {
            x.setParent(this);
        }
        this.select = x;
    }
}
