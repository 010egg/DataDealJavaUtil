// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.clickhouse.ast;

import com.alibaba.druid.sql.dialect.clickhouse.visitor.ClickhouseVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;

public class ClickhouseCreateTableStatement extends SQLCreateTableStatement
{
    protected final List<SQLAssignItem> settings;
    private SQLOrderBy orderBy;
    private SQLExpr partitionBy;
    private SQLExpr sampleBy;
    
    public ClickhouseCreateTableStatement() {
        super(DbType.clickhouse);
        this.settings = new ArrayList<SQLAssignItem>();
    }
    
    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }
    
    public void setOrderBy(final SQLOrderBy x) {
        if (x != null) {
            x.setParent(this);
        }
        this.orderBy = x;
    }
    
    public SQLExpr getPartitionBy() {
        return this.partitionBy;
    }
    
    public void setPartitionBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.partitionBy = x;
    }
    
    public SQLExpr getSampleBy() {
        return this.sampleBy;
    }
    
    public void setSampleBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.sampleBy = x;
    }
    
    public List<SQLAssignItem> getSettings() {
        return this.settings;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v instanceof ClickhouseVisitor) {
            final ClickhouseVisitor vv = (ClickhouseVisitor)v;
            if (vv.visit(this)) {
                this.acceptChild(vv);
            }
            vv.endVisit(this);
            return;
        }
        if (v.visit(this)) {
            this.acceptChild(v);
        }
        v.endVisit(this);
    }
}
