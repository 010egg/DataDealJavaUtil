// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;

public class MySqlHelpStatement extends MySqlStatementImpl
{
    private SQLExpr content;
    
    public SQLExpr getContent() {
        return this.content;
    }
    
    public void setContent(final SQLExpr content) {
        this.content = content;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.content);
        }
        visitor.endVisit(this);
    }
}
