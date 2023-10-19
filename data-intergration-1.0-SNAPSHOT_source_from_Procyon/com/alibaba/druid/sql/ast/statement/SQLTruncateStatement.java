// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLTruncateStatement extends SQLStatementImpl
{
    protected List<SQLExprTableSource> tableSources;
    private boolean purgeSnapshotLog;
    private boolean only;
    private Boolean restartIdentity;
    private Boolean cascade;
    private boolean dropStorage;
    private boolean reuseStorage;
    private boolean immediate;
    private boolean ignoreDeleteTriggers;
    private boolean restrictWhenDeleteTriggers;
    private boolean continueIdentity;
    protected boolean ifExists;
    protected List<SQLAssignItem> partitions;
    protected boolean partitionAll;
    protected List<SQLIntegerExpr> partitionsForADB;
    
    public SQLTruncateStatement() {
        this.tableSources = new ArrayList<SQLExprTableSource>(2);
        this.purgeSnapshotLog = false;
        this.dropStorage = false;
        this.reuseStorage = false;
        this.immediate = false;
        this.ignoreDeleteTriggers = false;
        this.restrictWhenDeleteTriggers = false;
        this.continueIdentity = false;
        this.ifExists = false;
        this.partitions = new ArrayList<SQLAssignItem>();
        this.partitionAll = false;
        this.partitionsForADB = new ArrayList<SQLIntegerExpr>();
    }
    
    public SQLTruncateStatement(final DbType dbType) {
        super(dbType);
        this.tableSources = new ArrayList<SQLExprTableSource>(2);
        this.purgeSnapshotLog = false;
        this.dropStorage = false;
        this.reuseStorage = false;
        this.immediate = false;
        this.ignoreDeleteTriggers = false;
        this.restrictWhenDeleteTriggers = false;
        this.continueIdentity = false;
        this.ifExists = false;
        this.partitions = new ArrayList<SQLAssignItem>();
        this.partitionAll = false;
        this.partitionsForADB = new ArrayList<SQLIntegerExpr>();
    }
    
    public List<SQLExprTableSource> getTableSources() {
        return this.tableSources;
    }
    
    public void setTableSources(final List<SQLExprTableSource> tableSources) {
        this.tableSources = tableSources;
    }
    
    public void addTableSource(final SQLName name) {
        final SQLExprTableSource tableSource = new SQLExprTableSource(name);
        tableSource.setParent(this);
        this.tableSources.add(tableSource);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSources);
        }
        visitor.endVisit(this);
    }
    
    public boolean isPurgeSnapshotLog() {
        return this.purgeSnapshotLog;
    }
    
    public void setPurgeSnapshotLog(final boolean purgeSnapshotLog) {
        this.purgeSnapshotLog = purgeSnapshotLog;
    }
    
    public boolean isOnly() {
        return this.only;
    }
    
    public void setOnly(final boolean only) {
        this.only = only;
    }
    
    public Boolean getRestartIdentity() {
        return this.restartIdentity;
    }
    
    public void setRestartIdentity(final Boolean restartIdentity) {
        this.restartIdentity = restartIdentity;
    }
    
    public Boolean getCascade() {
        return this.cascade;
    }
    
    public void setCascade(final Boolean cascade) {
        this.cascade = cascade;
    }
    
    public boolean isDropStorage() {
        return this.dropStorage;
    }
    
    public void setDropStorage(final boolean dropStorage) {
        this.dropStorage = dropStorage;
    }
    
    public boolean isReuseStorage() {
        return this.reuseStorage;
    }
    
    public void setReuseStorage(final boolean reuseStorage) {
        this.reuseStorage = reuseStorage;
    }
    
    public boolean isImmediate() {
        return this.immediate;
    }
    
    public void setImmediate(final boolean immediate) {
        this.immediate = immediate;
    }
    
    public boolean isIgnoreDeleteTriggers() {
        return this.ignoreDeleteTriggers;
    }
    
    public void setIgnoreDeleteTriggers(final boolean ignoreDeleteTriggers) {
        this.ignoreDeleteTriggers = ignoreDeleteTriggers;
    }
    
    public boolean isRestrictWhenDeleteTriggers() {
        return this.restrictWhenDeleteTriggers;
    }
    
    public void setRestrictWhenDeleteTriggers(final boolean restrictWhenDeleteTriggers) {
        this.restrictWhenDeleteTriggers = restrictWhenDeleteTriggers;
    }
    
    public boolean isContinueIdentity() {
        return this.continueIdentity;
    }
    
    public void setContinueIdentity(final boolean continueIdentity) {
        this.continueIdentity = continueIdentity;
    }
    
    @Override
    public List getChildren() {
        return this.tableSources;
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
    
    public List<SQLAssignItem> getPartitions() {
        return this.partitions;
    }
    
    public boolean isPartitionAll() {
        return this.partitionAll;
    }
    
    public void setPartitionAll(final boolean partitionAll) {
        this.partitionAll = partitionAll;
    }
    
    public List<SQLIntegerExpr> getPartitionsForADB() {
        return this.partitionsForADB;
    }
}
