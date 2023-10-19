// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableDropPartition extends SQLObjectImpl implements SQLAlterTableItem
{
    private boolean ifExists;
    private boolean purge;
    private final List<SQLExpr> partitions;
    
    public SQLAlterTableDropPartition() {
        this.ifExists = false;
        this.partitions = new ArrayList<SQLExpr>(4);
    }
    
    public List<SQLExpr> getPartitions() {
        return this.partitions;
    }
    
    public void addPartition(final SQLExpr partition) {
        if (partition != null) {
            partition.setParent(this);
        }
        this.partitions.add(partition);
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
    
    public boolean isPurge() {
        return this.purge;
    }
    
    public void setPurge(final boolean purge) {
        this.purge = purge;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partitions);
        }
        visitor.endVisit(this);
    }
}
