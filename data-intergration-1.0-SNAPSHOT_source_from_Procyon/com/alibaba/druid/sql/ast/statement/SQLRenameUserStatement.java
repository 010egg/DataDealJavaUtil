// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLRenameUserStatement extends SQLStatementImpl
{
    private SQLName name;
    private SQLName to;
    
    public SQLRenameUserStatement() {
        this.dbType = DbType.mysql;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.name);
            this.acceptChild(v, this.to);
        }
        v.endVisit(this);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public SQLName getTo() {
        return this.to;
    }
    
    public void setTo(final SQLName to) {
        this.to = to;
    }
}
