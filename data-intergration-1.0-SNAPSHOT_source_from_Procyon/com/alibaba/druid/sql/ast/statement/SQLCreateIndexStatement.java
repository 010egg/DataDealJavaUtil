// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.Collection;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.ast.SQLIndex;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCreateIndexStatement extends SQLStatementImpl implements SQLCreateStatement, SQLIndex
{
    private SQLIndexDefinition indexDefinition;
    private boolean concurrently;
    protected SQLName tablespace;
    protected boolean deferedRebuild;
    protected SQLTableSource in;
    protected SQLExternalRecordFormat rowFormat;
    protected SQLName storedAs;
    protected List<SQLAssignItem> properties;
    protected List<SQLAssignItem> tableProperties;
    protected boolean storing;
    protected boolean ifNotExists;
    
    public SQLCreateIndexStatement() {
        this.indexDefinition = new SQLIndexDefinition();
        this.properties = new ArrayList<SQLAssignItem>();
        this.tableProperties = new ArrayList<SQLAssignItem>();
        this.indexDefinition.setParent(this);
    }
    
    public SQLCreateIndexStatement(final DbType dbType) {
        super(dbType);
        this.indexDefinition = new SQLIndexDefinition();
        this.properties = new ArrayList<SQLAssignItem>();
        this.tableProperties = new ArrayList<SQLAssignItem>();
        this.indexDefinition.setParent(this);
    }
    
    public SQLIndexDefinition getIndexDefinition() {
        return this.indexDefinition;
    }
    
    public SQLTableSource getTable() {
        return this.indexDefinition.getTable();
    }
    
    public void setTable(final SQLName table) {
        this.setTable(new SQLExprTableSource(table));
    }
    
    public void setTable(final SQLTableSource table) {
        this.indexDefinition.setTable(table);
    }
    
    public String getTableName() {
        if (this.indexDefinition.getTable() instanceof SQLExprTableSource) {
            final SQLExpr expr = ((SQLExprTableSource)this.indexDefinition.getTable()).getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                return ((SQLIdentifierExpr)expr).getName();
            }
            if (expr instanceof SQLPropertyExpr) {
                return ((SQLPropertyExpr)expr).getName();
            }
        }
        return null;
    }
    
    public List<SQLSelectOrderByItem> getItems() {
        return this.indexDefinition.getColumns();
    }
    
    public void addItem(final SQLSelectOrderByItem item) {
        if (item != null) {
            item.setParent(this);
        }
        this.indexDefinition.getColumns().add(item);
    }
    
    public SQLName getName() {
        return this.indexDefinition.getName();
    }
    
    public void setName(final SQLName name) {
        this.indexDefinition.setName(name);
    }
    
    public String getType() {
        return this.indexDefinition.getType();
    }
    
    public void setType(final String type) {
        this.indexDefinition.setType(type);
    }
    
    public String getUsing() {
        return this.indexDefinition.hasOptions() ? this.indexDefinition.getOptions().getIndexType() : null;
    }
    
    public void setUsing(final String using) {
        this.indexDefinition.getOptions().setIndexType(using);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.indexDefinition.getName());
            this.acceptChild(visitor, this.indexDefinition.getTable());
            this.acceptChild(visitor, this.indexDefinition.getColumns());
            this.acceptChild(visitor, this.tablespace);
            this.acceptChild(visitor, this.in);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.indexDefinition.getName() != null) {
            children.add(this.indexDefinition.getName());
        }
        if (this.indexDefinition.getTable() != null) {
            children.add(this.indexDefinition.getTable());
        }
        children.addAll(this.indexDefinition.getColumns());
        return children;
    }
    
    public String getSchema() {
        SQLName name = null;
        if (this.indexDefinition.getTable() instanceof SQLExprTableSource) {
            final SQLExpr expr = ((SQLExprTableSource)this.indexDefinition.getTable()).getExpr();
            if (expr instanceof SQLName) {
                name = (SQLName)expr;
            }
        }
        if (name == null) {
            return null;
        }
        if (name instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)name).getOwnernName();
        }
        return null;
    }
    
    @Override
    public SQLCreateIndexStatement clone() {
        final SQLCreateIndexStatement x = new SQLCreateIndexStatement();
        this.indexDefinition.cloneTo(x.indexDefinition);
        x.setIfNotExists(this.ifNotExists);
        return x;
    }
    
    public SQLExpr getComment() {
        return this.indexDefinition.hasOptions() ? this.indexDefinition.getOptions().getComment() : null;
    }
    
    public void setComment(final SQLExpr x) {
        this.indexDefinition.getOptions().setComment(x);
    }
    
    public SQLName getTablespace() {
        return this.tablespace;
    }
    
    public void setTablespace(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.tablespace = x;
    }
    
    public boolean isConcurrently() {
        return this.concurrently;
    }
    
    public void setConcurrently(final boolean concurrently) {
        this.concurrently = concurrently;
    }
    
    public List<SQLAssignItem> getOptions() {
        return this.indexDefinition.getCompatibleOptions();
    }
    
    public boolean isDeferedRebuild() {
        return this.deferedRebuild;
    }
    
    public void setDeferedRebuild(final boolean deferedRebuild) {
        this.deferedRebuild = deferedRebuild;
    }
    
    public SQLTableSource getIn() {
        return this.in;
    }
    
    public void setIn(final SQLName x) {
        if (x == null) {
            this.in = null;
            return;
        }
        this.setIn(new SQLExprTableSource(x));
    }
    
    public void setIn(final SQLTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.in = x;
    }
    
    public SQLName getStoredAs() {
        return this.storedAs;
    }
    
    public void setStoredAs(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.storedAs = x;
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
    
    public List<SQLAssignItem> getProperties() {
        return this.properties;
    }
    
    public List<SQLAssignItem> getTableProperties() {
        return this.tableProperties;
    }
    
    public void addOption(final String name, final SQLExpr value) {
        final SQLAssignItem assignItem = new SQLAssignItem(new SQLIdentifierExpr(name), value);
        assignItem.setParent(this);
        this.indexDefinition.getOptions().getOtherOptions().add(assignItem);
        this.indexDefinition.getCompatibleOptions().add(assignItem);
    }
    
    public boolean isGlobal() {
        return this.indexDefinition.isGlobal();
    }
    
    public void setGlobal(final boolean global) {
        this.indexDefinition.setGlobal(global);
    }
    
    public boolean isLocal() {
        return this.indexDefinition.isLocal();
    }
    
    public void setLocal(final boolean local) {
        this.indexDefinition.setLocal(local);
    }
    
    public SQLExpr getDbPartitionBy() {
        return this.indexDefinition.getDbPartitionBy();
    }
    
    public void setDbPartitionBy(final SQLExpr x) {
        this.indexDefinition.setDbPartitionBy(x);
    }
    
    public SQLExpr getTablePartitions() {
        return this.indexDefinition.getTbPartitions();
    }
    
    public void setTablePartitions(final SQLExpr x) {
        this.indexDefinition.setTbPartitions(x);
    }
    
    public SQLExpr getTablePartitionBy() {
        return this.indexDefinition.getTbPartitionBy();
    }
    
    public void setTablePartitionBy(final SQLExpr x) {
        this.indexDefinition.setTbPartitionBy(x);
    }
    
    public boolean isStoring() {
        return this.storing;
    }
    
    public void setStoring(final boolean storing) {
        this.storing = storing;
    }
    
    @Override
    public List<SQLName> getCovering() {
        return this.indexDefinition.getCovering();
    }
    
    @Override
    public List<SQLSelectOrderByItem> getColumns() {
        return this.indexDefinition.getColumns();
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
}
