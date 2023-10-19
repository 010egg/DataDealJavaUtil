// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class OdpsShowChangelogsStatement extends SQLStatementImpl
{
    protected SQLExprTableSource table;
    private boolean tables;
    private List<SQLAssignItem> properties;
    private List<SQLAssignItem> partitions;
    private SQLExpr id;
    
    public OdpsShowChangelogsStatement() {
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
        this.accept0((OdpsASTVisitor)visitor);
    }
    
    public void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.table);
            this.acceptChild(visitor, this.properties);
            this.acceptChild(visitor, this.partitions);
            this.acceptChild(visitor, this.id);
        }
        visitor.endVisit(this);
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
    
    public SQLExpr getId() {
        return this.id;
    }
    
    public void setId(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.id = x;
    }
}
