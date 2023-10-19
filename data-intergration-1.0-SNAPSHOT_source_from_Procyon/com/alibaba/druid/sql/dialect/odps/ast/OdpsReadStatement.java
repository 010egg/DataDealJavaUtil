// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;

public class OdpsReadStatement extends OdpsStatementImpl
{
    private SQLExprTableSource table;
    private List<SQLAssignItem> partition;
    private List<SQLName> columns;
    private SQLExpr rowCount;
    
    public OdpsReadStatement() {
        this.partition = new ArrayList<SQLAssignItem>();
        this.columns = new ArrayList<SQLName>();
    }
    
    @Override
    protected void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.table);
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.partition);
            this.acceptChild(visitor, this.rowCount);
        }
        visitor.endVisit(this);
    }
    
    public SQLExprTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLExprTableSource table) {
        if (table != null) {
            table.setParent(table);
        }
        this.table = table;
    }
    
    public void setTable(final SQLName table) {
        this.setTable(new SQLExprTableSource(table));
    }
    
    public List<SQLAssignItem> getPartition() {
        return this.partition;
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public SQLExpr getRowCount() {
        return this.rowCount;
    }
    
    public void setRowCount(final SQLExpr rowCount) {
        this.rowCount = rowCount;
    }
}
