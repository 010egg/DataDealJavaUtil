// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLExportTableStatement extends SQLStatementImpl
{
    private SQLExprTableSource table;
    private List<SQLAssignItem> partition;
    private SQLExpr to;
    private SQLExpr forReplication;
    
    public SQLExportTableStatement() {
        this.partition = new ArrayList<SQLAssignItem>();
        this.dbType = DbType.hive;
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
    
    public List<SQLAssignItem> getPartition() {
        return this.partition;
    }
    
    public SQLExpr getTo() {
        return this.to;
    }
    
    public void setTo(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.to = x;
    }
    
    public SQLExpr getForReplication() {
        return this.forReplication;
    }
    
    public void setForReplication(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.forReplication = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.table);
            this.acceptChild(v, this.partition);
            this.acceptChild(v, this.to);
            this.acceptChild(v, this.forReplication);
        }
        v.endVisit(this);
    }
}
