// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLPartitionSpec;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableMergePartition extends SQLObjectImpl implements SQLAlterTableItem
{
    private final List<SQLPartitionSpec> partitions;
    private boolean ifExists;
    private SQLPartitionSpec overwritePartition;
    private boolean purge;
    
    public SQLAlterTableMergePartition() {
        this.partitions = new ArrayList<SQLPartitionSpec>(4);
        this.ifExists = false;
    }
    
    public List<SQLPartitionSpec> getPartitions() {
        return this.partitions;
    }
    
    public void addPartition(final SQLPartitionSpec x) {
        if (x != null) {
            x.setParent(this);
        }
        this.partitions.add(x);
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partitions);
        }
        visitor.endVisit(this);
    }
    
    public SQLPartitionSpec getOverwritePartition() {
        return this.overwritePartition;
    }
    
    public void setOverwritePartition(final SQLPartitionSpec x) {
        if (x != null) {
            x.setParent(this);
        }
        this.overwritePartition = x;
    }
    
    public boolean isPurge() {
        return this.purge;
    }
    
    public void setPurge(final boolean purge) {
        this.purge = purge;
    }
}
