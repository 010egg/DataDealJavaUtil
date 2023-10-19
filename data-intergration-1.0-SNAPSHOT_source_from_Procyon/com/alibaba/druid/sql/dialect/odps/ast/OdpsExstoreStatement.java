// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;

public class OdpsExstoreStatement extends OdpsStatementImpl
{
    private SQLExprTableSource table;
    private final List<SQLAssignItem> partitions;
    
    public OdpsExstoreStatement() {
        this.partitions = new ArrayList<SQLAssignItem>();
    }
    
    @Override
    protected void accept0(final OdpsASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.table);
            this.acceptChild(v, this.partitions);
        }
        v.endVisit(this);
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
    
    public List<SQLAssignItem> getPartitions() {
        return this.partitions;
    }
}
