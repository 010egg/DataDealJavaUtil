// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLPartitionRef extends SQLObjectImpl
{
    private List<Item> items;
    
    public SQLPartitionRef() {
        this.items = new ArrayList<Item>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.items);
        }
        v.endVisit(this);
    }
    
    public List<Item> getItems() {
        return this.items;
    }
    
    public void addItem(final Item item) {
        item.setParent(this);
        this.items.add(item);
    }
    
    public void addItem(final SQLIdentifierExpr name, final SQLExpr value) {
        final Item item = new Item();
        item.setColumnName(name);
        item.setValue(value);
        item.setParent(this);
        this.items.add(item);
    }
    
    public static class Item extends SQLObjectImpl
    {
        private SQLIdentifierExpr columnName;
        private SQLExpr value;
        private SQLBinaryOperator operator;
        
        public Item() {
        }
        
        public Item(final SQLIdentifierExpr columnName) {
            this.setColumnName(columnName);
        }
        
        @Override
        protected void accept0(final SQLASTVisitor v) {
        }
        
        public SQLIdentifierExpr getColumnName() {
            return this.columnName;
        }
        
        public void setColumnName(final SQLIdentifierExpr x) {
            if (x != null) {
                x.setParent(this);
            }
            this.columnName = x;
        }
        
        public SQLExpr getValue() {
            return this.value;
        }
        
        public void setValue(final SQLExpr x) {
            if (x != null) {
                x.setParent(this);
            }
            this.value = x;
        }
        
        public SQLBinaryOperator getOperator() {
            return this.operator;
        }
        
        public void setOperator(final SQLBinaryOperator operator) {
            this.operator = operator;
        }
    }
}
