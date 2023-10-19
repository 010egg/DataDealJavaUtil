// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributesImpl;

public class SQLPartitionValue extends OracleSegmentAttributesImpl
{
    protected Operator operator;
    protected final List<SQLExpr> items;
    
    public SQLPartitionValue(final Operator operator) {
        this.items = new ArrayList<SQLExpr>();
        this.operator = operator;
    }
    
    public List<SQLExpr> getItems() {
        return this.items;
    }
    
    public void addItem(final SQLExpr item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
    
    public Operator getOperator() {
        return this.operator;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getItems());
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLPartitionValue clone() {
        final SQLPartitionValue x = new SQLPartitionValue(this.operator);
        for (final SQLExpr item : this.items) {
            final SQLExpr item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }
        return x;
    }
    
    public enum Operator
    {
        LessThan, 
        In, 
        List;
    }
}
