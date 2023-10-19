// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.stmt;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class HiveMsckRepairStatement extends SQLStatementImpl implements SQLAlterStatement
{
    private SQLExprTableSource table;
    private SQLName database;
    private boolean addPartitions;
    
    public HiveMsckRepairStatement() {
        super(DbType.hive);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof HiveASTVisitor) {
            this.accept0((HiveASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    protected void accept0(final HiveASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.table);
            this.acceptChild(visitor, this.database);
        }
        visitor.endVisit(this);
    }
    
    public boolean isAddPartitions() {
        return this.addPartitions;
    }
    
    public void setAddPartitions(final boolean addPartitions) {
        this.addPartitions = addPartitions;
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
    
    public void setTable(final SQLExpr x) {
        if (x == null) {
            this.table = null;
            return;
        }
        this.setTable(new SQLExprTableSource(x));
    }
    
    public SQLName getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.database = x;
    }
}
