// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableSubpartitionLifecycle extends SQLObjectImpl implements SQLAlterTableItem
{
    private List<SQLIntegerExpr> partitionIds;
    private List<SQLIntegerExpr> subpartitionLifeCycle;
    
    public SQLAlterTableSubpartitionLifecycle() {
        this.partitionIds = new ArrayList<SQLIntegerExpr>();
        this.subpartitionLifeCycle = new ArrayList<SQLIntegerExpr>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partitionIds);
            this.acceptChild(visitor, this.subpartitionLifeCycle);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLIntegerExpr> getPartitionIds() {
        return this.partitionIds;
    }
    
    public void setPartitionIds(final List<SQLIntegerExpr> partitionIds) {
        this.partitionIds = partitionIds;
    }
    
    public List<SQLIntegerExpr> getSubpartitionLifeCycle() {
        return this.subpartitionLifeCycle;
    }
    
    public void setSubpartitionLifeCycle(final List<SQLIntegerExpr> subpartitionLifeCycle) {
        this.subpartitionLifeCycle = subpartitionLifeCycle;
    }
}
