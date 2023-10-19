// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;

public class SQLColumnReference extends SQLConstraintImpl implements SQLColumnConstraint
{
    private SQLName table;
    private List<SQLName> columns;
    private SQLForeignKeyImpl.Match referenceMatch;
    protected SQLForeignKeyImpl.Option onUpdate;
    protected SQLForeignKeyImpl.Option onDelete;
    
    public SQLColumnReference() {
        this.columns = new ArrayList<SQLName>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
        }
        visitor.endVisit(this);
    }
    
    public SQLName getTable() {
        return this.table;
    }
    
    public void setTable(final SQLName table) {
        this.table = table;
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public void setColumns(final List<SQLName> columns) {
        this.columns = columns;
    }
    
    @Override
    public SQLColumnReference clone() {
        final SQLColumnReference x = new SQLColumnReference();
        super.cloneTo(x);
        if (this.table != null) {
            x.setTable(this.table.clone());
        }
        for (final SQLName column : this.columns) {
            final SQLName columnCloned = column.clone();
            columnCloned.setParent(x);
            x.columns.add(columnCloned);
        }
        x.referenceMatch = this.referenceMatch;
        x.onUpdate = this.onUpdate;
        x.onDelete = this.onDelete;
        return x;
    }
    
    public SQLForeignKeyImpl.Match getReferenceMatch() {
        return this.referenceMatch;
    }
    
    public void setReferenceMatch(final SQLForeignKeyImpl.Match referenceMatch) {
        this.referenceMatch = referenceMatch;
    }
    
    public SQLForeignKeyImpl.Option getOnUpdate() {
        return this.onUpdate;
    }
    
    public void setOnUpdate(final SQLForeignKeyImpl.Option onUpdate) {
        this.onUpdate = onUpdate;
    }
    
    public SQLForeignKeyImpl.Option getOnDelete() {
        return this.onDelete;
    }
    
    public void setOnDelete(final SQLForeignKeyImpl.Option onDelete) {
        this.onDelete = onDelete;
    }
}
