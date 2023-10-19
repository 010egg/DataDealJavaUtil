// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;

public class OracleSelectPivot extends OracleSelectPivotBase
{
    private boolean xml;
    private final List<Item> items;
    private final List<SQLExpr> pivotFor;
    private final List<Item> pivotIn;
    
    public OracleSelectPivot() {
        this.items = new ArrayList<Item>();
        this.pivotFor = new ArrayList<SQLExpr>();
        this.pivotIn = new ArrayList<Item>();
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.items);
            this.acceptChild(visitor, this.pivotFor);
            this.acceptChild(visitor, this.pivotIn);
        }
        visitor.endVisit(this);
    }
    
    public List<Item> getPivotIn() {
        return this.pivotIn;
    }
    
    @Override
    public List<SQLExpr> getPivotFor() {
        return this.pivotFor;
    }
    
    public boolean isXml() {
        return this.xml;
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
    
    public void setXml(final boolean xml) {
        this.xml = xml;
    }
    
    @Override
    public OracleSelectPivot clone() {
        final OracleSelectPivot x = new OracleSelectPivot();
        x.setXml(this.xml);
        for (final Item e : this.items) {
            final Item e2 = e.clone();
            e2.setParent(x);
            x.getItems().add(e2);
        }
        for (final SQLExpr e3 : this.pivotFor) {
            final SQLExpr e4 = e3.clone();
            e4.setParent(x);
            x.getPivotFor().add(e4);
        }
        for (final Item e : this.pivotIn) {
            final Item e2 = e.clone();
            e2.setParent(x);
            x.getPivotIn().add(e2);
        }
        return x;
    }
    
    public static class Item extends OracleSQLObjectImpl
    {
        private String alias;
        private SQLExpr expr;
        
        public String getAlias() {
            return this.alias;
        }
        
        public void setAlias(final String alias) {
            this.alias = alias;
        }
        
        public SQLExpr getExpr() {
            return this.expr;
        }
        
        public void setExpr(final SQLExpr expr) {
            if (expr != null) {
                expr.setParent(this);
            }
            this.expr = expr;
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.expr);
            }
            visitor.endVisit(this);
        }
        
        @Override
        public Item clone() {
            final Item x = new Item();
            x.setAlias(this.alias);
            if (this.alias != null) {
                x.setExpr(this.expr.clone());
            }
            return x;
        }
    }
}
