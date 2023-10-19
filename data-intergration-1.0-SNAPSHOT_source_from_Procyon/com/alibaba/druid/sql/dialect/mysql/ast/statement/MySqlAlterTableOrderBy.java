// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public class MySqlAlterTableOrderBy extends MySqlObjectImpl implements SQLAlterTableItem
{
    private List<SQLSelectOrderByItem> columns;
    
    public MySqlAlterTableOrderBy() {
        this.columns = new ArrayList<SQLSelectOrderByItem>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.columns);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLSelectOrderByItem> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLSelectOrderByItem column) {
        this.columns.add(column);
    }
}
