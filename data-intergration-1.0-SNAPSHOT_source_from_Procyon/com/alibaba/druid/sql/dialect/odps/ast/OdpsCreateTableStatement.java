// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateTableStatement;

public class OdpsCreateTableStatement extends HiveCreateTableStatement
{
    protected final List<SQLExpr> withSerdeproperties;
    protected SQLExpr lifecycle;
    protected SQLExpr storedBy;
    
    public OdpsCreateTableStatement() {
        super(DbType.odps);
        this.withSerdeproperties = new ArrayList<SQLExpr>();
    }
    
    @Override
    public SQLExprTableSource getLike() {
        return this.like;
    }
    
    @Override
    public void setLike(final SQLName like) {
        this.setLike(new SQLExprTableSource(like));
    }
    
    @Override
    public void setLike(final SQLExprTableSource like) {
        this.like = like;
    }
    
    @Override
    public List<SQLColumnDefinition> getPartitionColumns() {
        return this.partitionColumns;
    }
    
    @Override
    public void addPartitionColumn(final SQLColumnDefinition column) {
        if (column != null) {
            column.setParent(this);
        }
        this.partitionColumns.add(column);
    }
    
    public SQLExpr getLifecycle() {
        return this.lifecycle;
    }
    
    public void setLifecycle(final SQLExpr lifecycle) {
        this.lifecycle = lifecycle;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v instanceof OdpsASTVisitor) {
            this.accept0((OdpsASTVisitor)v);
            return;
        }
        super.accept0(v);
    }
    
    protected void accept0(final OdpsASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v);
        }
        v.endVisit(this);
    }
    
    @Override
    protected void acceptChild(final SQLASTVisitor v) {
        super.acceptChild(v);
        this.acceptChild(v, this.withSerdeproperties);
        this.acceptChild(v, this.lifecycle);
        this.acceptChild(v, this.storedBy);
    }
    
    public SQLExpr getStoredBy() {
        return this.storedBy;
    }
    
    public void setStoredBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.storedBy = x;
    }
    
    public List<SQLExpr> getWithSerdeproperties() {
        return this.withSerdeproperties;
    }
}
