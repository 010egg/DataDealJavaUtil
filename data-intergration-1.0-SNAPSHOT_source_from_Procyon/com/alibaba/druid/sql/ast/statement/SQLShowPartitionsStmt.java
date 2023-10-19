// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowPartitionsStmt extends SQLStatementImpl implements SQLShowStatement
{
    private SQLExprTableSource tableSource;
    private List<SQLAssignItem> partition;
    private SQLExpr where;
    
    public SQLShowPartitionsStmt() {
        super(DbType.odps);
        this.partition = new ArrayList<SQLAssignItem>();
    }
    
    public SQLExprTableSource getTableSource() {
        return this.tableSource;
    }
    
    public void setTableSource(final SQLExpr table) {
        if (table == null) {
            return;
        }
        this.setTableSource(new SQLExprTableSource(table));
    }
    
    public void setTableSource(final SQLExprTableSource tableSource) {
        this.tableSource = tableSource;
    }
    
    public List<SQLAssignItem> getPartition() {
        return this.partition;
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.where = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.partition);
            this.acceptChild(visitor, this.where);
        }
        visitor.endVisit(this);
    }
}
