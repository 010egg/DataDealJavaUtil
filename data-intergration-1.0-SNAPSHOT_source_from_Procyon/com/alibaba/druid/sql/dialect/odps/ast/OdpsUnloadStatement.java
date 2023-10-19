// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class OdpsUnloadStatement extends SQLStatementImpl
{
    protected final List<SQLAssignItem> serdeProperties;
    protected final List<SQLAssignItem> properties;
    private final List<SQLAssignItem> partitions;
    protected SQLExpr location;
    protected SQLExternalRecordFormat rowFormat;
    protected SQLExpr storedAs;
    private SQLTableSource from;
    
    public OdpsUnloadStatement() {
        super(DbType.odps);
        this.serdeProperties = new ArrayList<SQLAssignItem>();
        this.properties = new ArrayList<SQLAssignItem>();
        this.partitions = new ArrayList<SQLAssignItem>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OdpsASTVisitor)visitor);
    }
    
    protected void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.partitions);
            this.acceptChild(visitor, this.location);
            this.acceptChild(visitor, this.rowFormat);
            this.acceptChild(visitor, this.storedAs);
            this.acceptChild(visitor, this.properties);
        }
        visitor.endVisit(this);
    }
    
    public SQLTableSource getFrom() {
        return this.from;
    }
    
    public void setFrom(final SQLName x) {
        this.setFrom(new SQLExprTableSource(x));
    }
    
    public void setFrom(final SQLTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.from = x;
    }
    
    public List<SQLAssignItem> getPartitions() {
        return this.partitions;
    }
    
    public SQLExpr getLocation() {
        return this.location;
    }
    
    public void setLocation(final SQLExpr location) {
        this.location = location;
    }
    
    public SQLExternalRecordFormat getRowFormat() {
        return this.rowFormat;
    }
    
    public void setRowFormat(final SQLExternalRecordFormat x) {
        if (x != null) {
            x.setParent(this);
        }
        this.rowFormat = x;
    }
    
    public SQLExpr getStoredAs() {
        return this.storedAs;
    }
    
    public void setStoredAs(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.storedAs = x;
    }
    
    public List<SQLAssignItem> getSerdeProperties() {
        return this.serdeProperties;
    }
    
    public List<SQLAssignItem> getProperties() {
        return this.properties;
    }
}
