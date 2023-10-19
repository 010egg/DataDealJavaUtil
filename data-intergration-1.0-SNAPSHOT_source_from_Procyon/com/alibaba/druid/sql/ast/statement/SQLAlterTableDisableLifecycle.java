// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableDisableLifecycle extends SQLObjectImpl implements SQLAlterTableItem
{
    private final List<SQLAssignItem> partition;
    
    public SQLAlterTableDisableLifecycle() {
        this.partition = new ArrayList<SQLAssignItem>(4);
    }
    
    public List<SQLAssignItem> getPartition() {
        return this.partition;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partition);
        }
        visitor.endVisit(this);
    }
}
