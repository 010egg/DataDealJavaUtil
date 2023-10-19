// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MySqlAlterTableModifyColumn extends MySqlObjectImpl implements SQLAlterTableItem
{
    private SQLColumnDefinition newColumnDefinition;
    private boolean first;
    private SQLName firstColumn;
    private SQLName afterColumn;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.newColumnDefinition);
            this.acceptChild(visitor, this.firstColumn);
            this.acceptChild(visitor, this.afterColumn);
        }
    }
    
    public SQLName getFirstColumn() {
        return this.firstColumn;
    }
    
    public void setFirstColumn(final SQLName firstColumn) {
        this.firstColumn = firstColumn;
    }
    
    public SQLName getAfterColumn() {
        return this.afterColumn;
    }
    
    public void setAfterColumn(final SQLName afterColumn) {
        this.afterColumn = afterColumn;
    }
    
    public SQLColumnDefinition getNewColumnDefinition() {
        return this.newColumnDefinition;
    }
    
    public void setNewColumnDefinition(final SQLColumnDefinition newColumnDefinition) {
        this.newColumnDefinition = newColumnDefinition;
    }
    
    public boolean isFirst() {
        return this.first;
    }
    
    public void setFirst(final boolean first) {
        this.first = first;
    }
}
