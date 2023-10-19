// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import java.util.List;

public class SQLRecordDataType extends SQLDataTypeImpl implements SQLDataType
{
    private final List<SQLColumnDefinition> columns;
    
    public SQLRecordDataType() {
        this.columns = new ArrayList<SQLColumnDefinition>();
    }
    
    public List<SQLColumnDefinition> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLColumnDefinition column) {
        column.setParent(this);
        this.columns.add(column);
    }
    
    @Override
    public SQLRecordDataType clone() {
        final SQLRecordDataType x = new SQLRecordDataType();
        this.cloneTo(x);
        for (final SQLColumnDefinition c : this.columns) {
            final SQLColumnDefinition c2 = c.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        return x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.columns);
        }
        visitor.endVisit(this);
    }
}
