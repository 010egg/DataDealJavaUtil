// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableAddClusteringKey extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLName name;
    private final List<SQLName> columns;
    
    public SQLAlterTableAddClusteringKey() {
        this.columns = new ArrayList<SQLName>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.name != null) {
                this.name.accept(visitor);
            }
            for (final SQLName column : this.columns) {
                column.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
}
