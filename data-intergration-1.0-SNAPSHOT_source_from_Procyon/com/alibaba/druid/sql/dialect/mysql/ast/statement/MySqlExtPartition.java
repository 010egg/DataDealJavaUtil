// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MySqlExtPartition extends MySqlObjectImpl implements Cloneable
{
    private final List<Item> items;
    
    public MySqlExtPartition() {
        this.items = new ArrayList<Item>();
    }
    
    public List<Item> getItems() {
        return this.items;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            for (int i = 0; i < this.items.size(); ++i) {
                this.items.get(i).accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public MySqlExtPartition clone() {
        final MySqlExtPartition x = new MySqlExtPartition();
        for (final Item item : this.items) {
            final Item item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }
        return x;
    }
    
    public static class Item extends MySqlObjectImpl implements Cloneable
    {
        private SQLName dbPartition;
        private SQLExpr dbPartitionBy;
        private SQLName tbPartition;
        private SQLExpr tbPartitionBy;
        
        @Override
        public void accept0(final MySqlASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.dbPartition);
                this.acceptChild(visitor, this.dbPartitionBy);
                this.acceptChild(visitor, this.tbPartition);
                this.acceptChild(visitor, this.tbPartitionBy);
            }
            visitor.endVisit(this);
        }
        
        @Override
        public Item clone() {
            final Item x = new Item();
            if (this.dbPartition != null) {
                x.setDbPartition(this.dbPartition.clone());
            }
            if (this.dbPartitionBy != null) {
                x.setDbPartitionBy(this.dbPartitionBy.clone());
            }
            if (this.tbPartition != null) {
                x.setTbPartition(this.tbPartition.clone());
            }
            if (this.tbPartitionBy != null) {
                x.setTbPartitionBy(this.tbPartitionBy.clone());
            }
            return x;
        }
        
        public SQLName getDbPartition() {
            return this.dbPartition;
        }
        
        public void setDbPartition(final SQLName x) {
            if (x != null) {
                x.setParent(this);
            }
            this.dbPartition = x;
        }
        
        public SQLName getTbPartition() {
            return this.tbPartition;
        }
        
        public void setTbPartition(final SQLName x) {
            if (x != null) {
                x.setParent(this);
            }
            this.tbPartition = x;
        }
        
        public SQLExpr getDbPartitionBy() {
            return this.dbPartitionBy;
        }
        
        public void setDbPartitionBy(final SQLExpr x) {
            if (x != null) {
                x.setParent(this);
            }
            this.dbPartitionBy = x;
        }
        
        public SQLExpr getTbPartitionBy() {
            return this.tbPartitionBy;
        }
        
        public void setTbPartitionBy(final SQLExpr x) {
            if (x != null) {
                x.setParent(this);
            }
            this.tbPartitionBy = x;
        }
    }
}
