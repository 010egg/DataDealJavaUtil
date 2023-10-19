// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import java.util.Collection;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterTableStatement extends SQLStatementImpl implements SQLDDLStatement, SQLAlterStatement
{
    private SQLExprTableSource tableSource;
    private List<SQLAlterTableItem> items;
    private boolean ignore;
    private boolean online;
    private boolean offline;
    private boolean updateGlobalIndexes;
    private boolean invalidateGlobalIndexes;
    private boolean removePatiting;
    private boolean upgradePatiting;
    private List<SQLAssignItem> tableOptions;
    private SQLPartitionBy partition;
    private boolean mergeSmallFiles;
    protected boolean range;
    protected final List<SQLSelectOrderByItem> clusteredBy;
    protected final List<SQLSelectOrderByItem> sortedBy;
    protected int buckets;
    protected int shards;
    private boolean ifExists;
    private boolean notClustered;
    
    public SQLAlterTableStatement() {
        this.items = new ArrayList<SQLAlterTableItem>();
        this.ignore = false;
        this.online = false;
        this.offline = false;
        this.updateGlobalIndexes = false;
        this.invalidateGlobalIndexes = false;
        this.removePatiting = false;
        this.upgradePatiting = false;
        this.tableOptions = new ArrayList<SQLAssignItem>();
        this.partition = null;
        this.mergeSmallFiles = false;
        this.clusteredBy = new ArrayList<SQLSelectOrderByItem>();
        this.sortedBy = new ArrayList<SQLSelectOrderByItem>();
        this.ifExists = false;
        this.notClustered = false;
    }
    
    public SQLAlterTableStatement(final DbType dbType) {
        super(dbType);
        this.items = new ArrayList<SQLAlterTableItem>();
        this.ignore = false;
        this.online = false;
        this.offline = false;
        this.updateGlobalIndexes = false;
        this.invalidateGlobalIndexes = false;
        this.removePatiting = false;
        this.upgradePatiting = false;
        this.tableOptions = new ArrayList<SQLAssignItem>();
        this.partition = null;
        this.mergeSmallFiles = false;
        this.clusteredBy = new ArrayList<SQLSelectOrderByItem>();
        this.sortedBy = new ArrayList<SQLSelectOrderByItem>();
        this.ifExists = false;
        this.notClustered = false;
    }
    
    public boolean isIgnore() {
        return this.ignore;
    }
    
    public void setIgnore(final boolean ignore) {
        this.ignore = ignore;
    }
    
    public boolean isOnline() {
        return this.online;
    }
    
    public void setOnline(final boolean online) {
        this.online = online;
    }
    
    public boolean isOffline() {
        return this.offline;
    }
    
    public void setOffline(final boolean offline) {
        this.offline = offline;
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
    
    public boolean isRemovePatiting() {
        return this.removePatiting;
    }
    
    public void setRemovePatiting(final boolean removePatiting) {
        this.removePatiting = removePatiting;
    }
    
    public boolean isUpgradePatiting() {
        return this.upgradePatiting;
    }
    
    public void setUpgradePatiting(final boolean upgradePatiting) {
        this.upgradePatiting = upgradePatiting;
    }
    
    public boolean isUpdateGlobalIndexes() {
        return this.updateGlobalIndexes;
    }
    
    public void setUpdateGlobalIndexes(final boolean updateGlobalIndexes) {
        this.updateGlobalIndexes = updateGlobalIndexes;
    }
    
    public boolean isInvalidateGlobalIndexes() {
        return this.invalidateGlobalIndexes;
    }
    
    public void setInvalidateGlobalIndexes(final boolean invalidateGlobalIndexes) {
        this.invalidateGlobalIndexes = invalidateGlobalIndexes;
    }
    
    public boolean isMergeSmallFiles() {
        return this.mergeSmallFiles;
    }
    
    public void setMergeSmallFiles(final boolean mergeSmallFiles) {
        this.mergeSmallFiles = mergeSmallFiles;
    }
    
    public List<SQLAlterTableItem> getItems() {
        return this.items;
    }
    
    public void addItem(final SQLAlterTableItem item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
    
    public SQLExprTableSource getTableSource() {
        return this.tableSource;
    }
    
    public void setTableSource(final SQLExprTableSource tableSource) {
        this.tableSource = tableSource;
    }
    
    public void setTableSource(final SQLExpr table) {
        this.setTableSource(new SQLExprTableSource(table));
    }
    
    public SQLName getName() {
        if (this.getTableSource() == null) {
            return null;
        }
        return (SQLName)this.getTableSource().getExpr();
    }
    
    public long nameHashCode64() {
        if (this.getTableSource() == null) {
            return 0L;
        }
        return ((SQLName)this.getTableSource().getExpr()).nameHashCode64();
    }
    
    public void setName(final SQLName name) {
        this.setTableSource(new SQLExprTableSource(name));
    }
    
    public List<SQLAssignItem> getTableOptions() {
        return this.tableOptions;
    }
    
    public SQLPartitionBy getPartition() {
        return this.partition;
    }
    
    public void setPartition(final SQLPartitionBy partition) {
        this.partition = partition;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getTableSource());
            this.acceptChild(visitor, this.getItems());
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.tableSource != null) {
            children.add(this.tableSource);
        }
        children.addAll(this.items);
        return children;
    }
    
    public String getTableName() {
        if (this.tableSource == null) {
            return null;
        }
        final SQLExpr expr = this.tableSource.getExpr();
        if (expr instanceof SQLIdentifierExpr) {
            return ((SQLIdentifierExpr)expr).getName();
        }
        if (expr instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)expr).getName();
        }
        return null;
    }
    
    public String getSchema() {
        final SQLName name = this.getName();
        if (name == null) {
            return null;
        }
        if (name instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)name).getOwnernName();
        }
        return null;
    }
    
    public void setItems(final List<SQLAlterTableItem> items) {
        this.items = items;
    }
    
    public boolean isRange() {
        return this.range;
    }
    
    public void setRange(final boolean range) {
        this.range = range;
    }
    
    public List<SQLSelectOrderByItem> getClusteredBy() {
        return this.clusteredBy;
    }
    
    public void addClusteredByItem(final SQLSelectOrderByItem item) {
        item.setParent(this);
        this.clusteredBy.add(item);
    }
    
    public List<SQLSelectOrderByItem> getSortedBy() {
        return this.sortedBy;
    }
    
    public void addSortedByItem(final SQLSelectOrderByItem item) {
        item.setParent(this);
        this.sortedBy.add(item);
    }
    
    public int getBuckets() {
        return this.buckets;
    }
    
    public void setBuckets(final int buckets) {
        this.buckets = buckets;
    }
    
    public int getShards() {
        return this.shards;
    }
    
    public void setShards(final int shards) {
        this.shards = shards;
    }
    
    public boolean isNotClustered() {
        return this.notClustered;
    }
    
    public void setNotClustered(final boolean notClustered) {
        this.notClustered = notClustered;
    }
}
