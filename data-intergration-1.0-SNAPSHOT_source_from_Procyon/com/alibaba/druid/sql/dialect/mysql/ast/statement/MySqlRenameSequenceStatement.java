// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;

public class MySqlRenameSequenceStatement extends MySqlStatementImpl implements SQLAlterStatement
{
    private SQLName name;
    private SQLName to;
    
    @Override
    public void accept0(final MySqlASTVisitor v) {
        if (v.visit(this)) {
            if (this.name != null) {
                this.name.accept(v);
            }
            if (this.to != null) {
                this.to.accept(v);
            }
        }
        v.endVisit(this);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.name = x;
    }
    
    public SQLName getTo() {
        return this.to;
    }
    
    public void setTo(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.to = x;
    }
}
