// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableAddColumn extends SQLObjectImpl implements SQLAlterTableItem
{
    private final List<SQLColumnDefinition> columns;
    private SQLName firstColumn;
    private SQLName afterColumn;
    private boolean first;
    private Boolean restrict;
    private boolean cascade;
    
    public SQLAlterTableAddColumn() {
        this.columns = new ArrayList<SQLColumnDefinition>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.columns);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLColumnDefinition> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLColumnDefinition column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }
    
    public SQLName getFirstColumn() {
        return this.firstColumn;
    }
    
    public void setFirstColumn(final SQLName first) {
        this.firstColumn = first;
    }
    
    public boolean isFirst() {
        return this.first;
    }
    
    public void setFirst(final boolean first) {
        this.first = first;
    }
    
    public SQLName getAfterColumn() {
        return this.afterColumn;
    }
    
    public void setAfterColumn(final SQLName after) {
        this.afterColumn = after;
    }
    
    public Boolean getRestrict() {
        return this.restrict;
    }
    
    public boolean isRestrict() {
        if (this.restrict == null) {
            return !this.cascade;
        }
        return this.restrict;
    }
    
    public void setRestrict(final boolean restrict) {
        this.restrict = restrict;
    }
    
    public boolean isCascade() {
        return this.cascade;
    }
    
    public void setCascade(final boolean cascade) {
        this.cascade = cascade;
    }
}
