// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowHistoryStatement extends SQLStatementImpl implements SQLShowStatement
{
    protected SQLExprTableSource table;
    private boolean tables;
    private List<SQLAssignItem> properties;
    private List<SQLAssignItem> partitions;
    
    public SQLShowHistoryStatement() {
        this.properties = new ArrayList<SQLAssignItem>();
        this.partitions = new ArrayList<SQLAssignItem>();
    }
    
    public SQLExprTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.table = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.table != null) {
            this.table.accept(visitor);
        }
    }
    
    public boolean isTables() {
        return this.tables;
    }
    
    public void setTables(final boolean tables) {
        this.tables = tables;
    }
    
    public List<SQLAssignItem> getProperties() {
        return this.properties;
    }
    
    public List<SQLAssignItem> getPartitions() {
        return this.partitions;
    }
}
