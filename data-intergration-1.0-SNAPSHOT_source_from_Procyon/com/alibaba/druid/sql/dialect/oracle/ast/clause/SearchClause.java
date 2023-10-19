// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.clause;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class SearchClause extends OracleSQLObjectImpl
{
    private Type type;
    private final List<SQLSelectOrderByItem> items;
    private SQLIdentifierExpr orderingColumn;
    
    public SearchClause() {
        this.items = new ArrayList<SQLSelectOrderByItem>();
    }
    
    public Type getType() {
        return this.type;
    }
    
    public void setType(final Type type) {
        this.type = type;
    }
    
    public List<SQLSelectOrderByItem> getItems() {
        return this.items;
    }
    
    public void addItem(final SQLSelectOrderByItem item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
    
    public SQLIdentifierExpr getOrderingColumn() {
        return this.orderingColumn;
    }
    
    public void setOrderingColumn(final SQLIdentifierExpr orderingColumn) {
        if (orderingColumn != null) {
            orderingColumn.setParent(this);
        }
        this.orderingColumn = orderingColumn;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.items);
            this.acceptChild(visitor, this.orderingColumn);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SearchClause clone() {
        final SearchClause x = new SearchClause();
        x.type = this.type;
        for (final SQLSelectOrderByItem item : this.items) {
            final SQLSelectOrderByItem item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }
        if (this.orderingColumn != null) {
            x.setOrderingColumn(this.orderingColumn.clone());
        }
        return x;
    }
    
    public enum Type
    {
        DEPTH, 
        BREADTH;
    }
}
