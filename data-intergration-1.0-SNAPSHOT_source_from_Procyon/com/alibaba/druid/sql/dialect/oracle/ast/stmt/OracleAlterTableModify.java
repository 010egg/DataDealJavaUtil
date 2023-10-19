// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import java.util.List;

public class OracleAlterTableModify extends OracleAlterTableItem
{
    private List<SQLColumnDefinition> columns;
    
    public OracleAlterTableModify() {
        this.columns = new ArrayList<SQLColumnDefinition>();
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
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
}
