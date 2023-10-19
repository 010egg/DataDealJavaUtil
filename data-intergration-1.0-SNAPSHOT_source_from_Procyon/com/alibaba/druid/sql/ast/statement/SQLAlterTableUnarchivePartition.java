// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableUnarchivePartition extends SQLObjectImpl implements SQLAlterTableItem
{
    private final List<SQLAssignItem> partitions;
    
    public SQLAlterTableUnarchivePartition() {
        this.partitions = new ArrayList<SQLAssignItem>(4);
    }
    
    public List<SQLAssignItem> getPartitions() {
        return this.partitions;
    }
    
    public void addPartition(final SQLAssignItem partition) {
        if (partition != null) {
            partition.setParent(this);
        }
        this.partitions.add(partition);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partitions);
        }
        visitor.endVisit(this);
    }
}
