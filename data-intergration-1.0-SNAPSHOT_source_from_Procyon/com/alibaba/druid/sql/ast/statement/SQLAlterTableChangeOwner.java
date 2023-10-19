// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableChangeOwner extends SQLObjectImpl implements SQLAlterTableItem
{
    private final List<SQLAssignItem> partition;
    private SQLName owner;
    
    public SQLAlterTableChangeOwner() {
        this.partition = new ArrayList<SQLAssignItem>(4);
    }
    
    public List<SQLAssignItem> getPartition() {
        return this.partition;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {}
        visitor.endVisit(this);
    }
    
    public SQLName getOwner() {
        return this.owner;
    }
    
    public void setOwner(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.owner = x;
    }
}
