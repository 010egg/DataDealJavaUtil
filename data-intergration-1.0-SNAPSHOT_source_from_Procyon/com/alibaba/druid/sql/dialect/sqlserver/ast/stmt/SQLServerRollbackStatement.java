// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast.stmt;

import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerStatement;
import com.alibaba.druid.sql.ast.statement.SQLRollbackStatement;

public class SQLServerRollbackStatement extends SQLRollbackStatement implements SQLServerStatement
{
    private boolean work;
    private SQLExpr name;
    
    public SQLServerRollbackStatement() {
        this.work = false;
    }
    
    public boolean isWork() {
        return this.work;
    }
    
    public void setWork(final boolean work) {
        this.work = work;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof SQLServerASTVisitor) {
            this.accept0((SQLServerASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    @Override
    public void accept0(final SQLServerASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public SQLExpr getName() {
        return this.name;
    }
    
    public void setName(final SQLExpr name) {
        this.name = name;
    }
}
