// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLPartitionSpec extends SQLObjectImpl implements Cloneable
{
    private List<Item> items;
    
    public SQLPartitionSpec() {
        this.items = new ArrayList<Item>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.items);
        }
        v.endVisit(this);
    }
    
    public void addItem(final Item item) {
        item.setParent(this);
        this.items.add(item);
    }
    
    public List<Item> getItems() {
        return this.items;
    }
    
    @Override
    public SQLPartitionSpec clone() {
        final SQLPartitionSpec x = new SQLPartitionSpec();
        for (final Item item : this.items) {
            x.addItem(item.clone());
        }
        return x;
    }
    
    public static class Item extends SQLObjectImpl implements Cloneable
    {
        private SQLName column;
        private SQLExpr value;
        
        @Override
        protected void accept0(final SQLASTVisitor v) {
            if (v.visit(this)) {
                this.acceptChild(v, this.column);
                this.acceptChild(v, this.value);
            }
            v.endVisit(this);
        }
        
        @Override
        public Item clone() {
            final Item x = new Item();
            if (this.column != null) {
                x.setColumn(this.column.clone());
            }
            if (this.value != null) {
                x.setValue(this.value.clone());
            }
            return x;
        }
        
        public SQLName getColumn() {
            return this.column;
        }
        
        public void setColumn(final SQLName column) {
            this.column = column;
        }
        
        public SQLExpr getValue() {
            return this.value;
        }
        
        public void setValue(final SQLExpr value) {
            this.value = value;
        }
    }
}
