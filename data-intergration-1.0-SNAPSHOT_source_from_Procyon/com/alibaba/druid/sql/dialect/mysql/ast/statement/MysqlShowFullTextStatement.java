// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.ast.FullTextType;

public class MysqlShowFullTextStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private FullTextType type;
    
    public FullTextType getType() {
        return this.type;
    }
    
    public void setType(final FullTextType type) {
        this.type = type;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
