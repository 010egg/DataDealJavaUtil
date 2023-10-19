// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableRenamePartition extends SQLObjectImpl implements SQLAlterTableItem
{
    private boolean ifNotExists;
    private final List<SQLAssignItem> partition;
    private final List<SQLAssignItem> to;
    
    public SQLAlterTableRenamePartition() {
        this.ifNotExists = false;
        this.partition = new ArrayList<SQLAssignItem>(4);
        this.to = new ArrayList<SQLAssignItem>(4);
    }
    
    public List<SQLAssignItem> getPartition() {
        return this.partition;
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
    
    public List<SQLAssignItem> getTo() {
        return this.to;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partition);
            this.acceptChild(visitor, this.to);
        }
        visitor.endVisit(this);
    }
}
