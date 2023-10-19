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

public class SQLAlterTableModifyClusteredBy extends SQLObjectImpl implements SQLAlterTableItem
{
    private List<SQLName> clusterColumns;
    
    public SQLAlterTableModifyClusteredBy() {
        this.clusterColumns = new ArrayList<SQLName>();
    }
    
    public List<SQLName> getClusterColumns() {
        return this.clusterColumns;
    }
    
    public void addClusterColumn(final SQLName name) {
        this.clusterColumns.add(name);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.clusterColumns);
        }
        visitor.endVisit(this);
    }
}
