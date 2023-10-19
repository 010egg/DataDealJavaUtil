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

public class SQLAlterTableDropColumnItem extends SQLObjectImpl implements SQLAlterTableItem
{
    private List<SQLName> columns;
    private boolean cascade;
    
    public SQLAlterTableDropColumnItem() {
        this.columns = new ArrayList<SQLName>();
        this.cascade = false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.columns);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLName column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }
    
    public boolean isCascade() {
        return this.cascade;
    }
    
    public void setCascade(final boolean cascade) {
        this.cascade = cascade;
    }
}
