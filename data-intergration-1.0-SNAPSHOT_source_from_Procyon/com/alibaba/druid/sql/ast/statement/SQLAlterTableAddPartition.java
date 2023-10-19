// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableAddPartition extends SQLObjectImpl implements SQLAlterTableItem
{
    private boolean ifNotExists;
    private final List<SQLObject> partitions;
    private SQLExpr partitionCount;
    private SQLExpr location;
    
    public SQLAlterTableAddPartition() {
        this.ifNotExists = false;
        this.partitions = new ArrayList<SQLObject>(4);
    }
    
    public List<SQLObject> getPartitions() {
        return this.partitions;
    }
    
    public void addPartition(final SQLObject x) {
        if (x != null) {
            x.setParent(this);
        }
        this.partitions.add(x);
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
    
    public SQLExpr getPartitionCount() {
        return this.partitionCount;
    }
    
    public void setPartitionCount(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.partitionCount = x;
    }
    
    public SQLExpr getLocation() {
        return this.location;
    }
    
    public void setLocation(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.location = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partitions);
            this.acceptChild(visitor, this.partitionCount);
            this.acceptChild(visitor, this.location);
        }
        visitor.endVisit(this);
    }
}
