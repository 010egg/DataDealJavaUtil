// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLUnionDataType extends SQLDataTypeImpl
{
    private final List<SQLDataType> items;
    
    public SQLUnionDataType() {
        this.items = new ArrayList<SQLDataType>();
    }
    
    @Override
    public String getName() {
        return "UNIONTYPE";
    }
    
    public List<SQLDataType> getItems() {
        return this.items;
    }
    
    public void add(final SQLDataType item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.items);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLUnionDataType clone() {
        final SQLUnionDataType x = new SQLUnionDataType();
        for (final SQLDataType item : this.items) {
            x.add(item.clone());
        }
        return x;
    }
}
