// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;

public class MySqlRenameTableStatement extends MySqlStatementImpl implements SQLAlterStatement
{
    private List<Item> items;
    
    public MySqlRenameTableStatement() {
        this.items = new ArrayList<Item>(2);
    }
    
    public List<Item> getItems() {
        return this.items;
    }
    
    public void addItem(final Item item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.items);
        }
        visitor.endVisit(this);
    }
    
    public static class Item extends MySqlObjectImpl
    {
        private SQLName name;
        private SQLName to;
        
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
        
        @Override
        public void accept0(final MySqlASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.name);
                this.acceptChild(visitor, this.to);
            }
            visitor.endVisit(this);
        }
    }
}
