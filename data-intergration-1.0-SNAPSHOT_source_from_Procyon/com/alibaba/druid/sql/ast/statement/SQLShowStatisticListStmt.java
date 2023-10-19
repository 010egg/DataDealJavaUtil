// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowStatisticListStmt extends SQLStatementImpl implements SQLShowStatement
{
    private SQLExprTableSource tableSource;
    private boolean full;
    
    public SQLShowStatisticListStmt() {
        super(DbType.odps);
    }
    
    public SQLExprTableSource getTableSource() {
        return this.tableSource;
    }
    
    public void setTableSource(final SQLExprTableSource tableSource) {
        this.tableSource = tableSource;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSource);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.tableSource);
    }
    
    public boolean isFull() {
        return this.full;
    }
    
    public void setFull(final boolean full) {
        this.full = full;
    }
}
