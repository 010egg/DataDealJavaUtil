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

public class SQLAlterTableDropSubpartition extends SQLObjectImpl implements SQLAlterTableItem
{
    private List<SQLIntegerExpr> partitionIds;
    private List<SQLIntegerExpr> subpartitionIds;
    
    public SQLAlterTableDropSubpartition() {
        this.partitionIds = new ArrayList<SQLIntegerExpr>();
        this.subpartitionIds = new ArrayList<SQLIntegerExpr>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.partitionIds);
            this.acceptChild(visitor, this.subpartitionIds);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLIntegerExpr> getPartitionIds() {
        return this.partitionIds;
    }
    
    public void setPartitionIds(final List<SQLIntegerExpr> partitionIds) {
        this.partitionIds = partitionIds;
    }
    
    public List<SQLIntegerExpr> getSubpartitionIds() {
        return this.subpartitionIds;
    }
    
    public void setSubpartitionIds(final List<SQLIntegerExpr> subpartitionIds) {
        this.subpartitionIds = subpartitionIds;
    }
}
