// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.SQLName;

public class MysqlCreateFullTextDictionaryStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLName name;
    private SQLColumnDefinition column;
    private String comment;
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public SQLColumnDefinition getColumn() {
        return this.column;
    }
    
    public void setColumn(final SQLColumnDefinition column) {
        this.column = column;
    }
    
    public String getComment() {
        return this.comment;
    }
    
    public void setComment(final String comment) {
        this.comment = comment;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.column);
        }
        visitor.endVisit(this);
    }
}
