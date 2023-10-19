// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;

public class MySqlShowJobStatusStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private boolean sync;
    private SQLExpr where;
    
    public MySqlShowJobStatusStatement() {
        this.sync = false;
    }
    
    public boolean isSync() {
        return this.sync;
    }
    
    public void setSync(final boolean sync) {
        this.sync = sync;
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr where) {
        this.where = where;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.where);
        }
        visitor.endVisit(this);
    }
}
