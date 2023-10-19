// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableTruncatePartition extends SQLObjectImpl implements SQLAlterTableItem
{
    private final List<SQLName> partitions;
    
    public SQLAlterTableTruncatePartition() {
        this.partitions = new ArrayList<SQLName>(4);
    }
    
    public List<SQLName> getPartitions() {
        return this.partitions;
    }
    
    public void addPartition(final SQLName partition) {
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
