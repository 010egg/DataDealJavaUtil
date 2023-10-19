// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.mysql.ast.FullTextType;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;

public class MysqlAlterFullTextStatement extends MySqlStatementImpl implements SQLAlterStatement
{
    private FullTextType type;
    private SQLName name;
    private SQLAssignItem item;
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public FullTextType getType() {
        return this.type;
    }
    
    public void setType(final FullTextType type) {
        this.type = type;
    }
    
    public SQLAssignItem getItem() {
        return this.item;
    }
    
    public void setItem(final SQLAssignItem item) {
        if (item != null) {
            item.setParent(this);
        }
        this.item = item;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
