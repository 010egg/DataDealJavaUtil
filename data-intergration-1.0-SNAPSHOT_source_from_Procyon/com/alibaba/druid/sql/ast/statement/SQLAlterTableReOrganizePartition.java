// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableReOrganizePartition extends SQLObjectImpl implements SQLAlterTableItem
{
    private final List<SQLName> names;
    private final List<SQLObject> partitions;
    
    public SQLAlterTableReOrganizePartition() {
        this.names = new ArrayList<SQLName>();
        this.partitions = new ArrayList<SQLObject>(4);
    }
    
    public List<SQLObject> getPartitions() {
        return this.partitions;
    }
    
    public void addPartition(final SQLObject partition) {
        if (partition != null) {
            partition.setParent(this);
        }
        this.partitions.add(partition);
    }
    
    public List<SQLName> getNames() {
        return this.names;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partitions);
        }
        visitor.endVisit(this);
    }
}
