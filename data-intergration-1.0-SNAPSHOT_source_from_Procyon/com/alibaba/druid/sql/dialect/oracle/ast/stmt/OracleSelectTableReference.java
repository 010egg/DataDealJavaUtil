// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.SampleClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.PartitionExtensionClause;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;

public class OracleSelectTableReference extends SQLExprTableSource implements OracleSelectTableSource
{
    private boolean only;
    protected OracleSelectPivotBase pivot;
    protected PartitionExtensionClause partition;
    protected SampleClause sampleClause;
    
    public OracleSelectTableReference() {
        this.only = false;
    }
    
    public OracleSelectTableReference(final SQLExpr expr) {
        this.only = false;
        this.setExpr(expr);
    }
    
    public PartitionExtensionClause getPartition() {
        return this.partition;
    }
    
    public void setPartition(final PartitionExtensionClause partition) {
        if (partition != null) {
            partition.setParent(this);
        }
        this.partition = partition;
    }
    
    public boolean isOnly() {
        return this.only;
    }
    
    public void setOnly(final boolean only) {
        this.only = only;
    }
    
    public SampleClause getSampleClause() {
        return this.sampleClause;
    }
    
    public void setSampleClause(final SampleClause sampleClause) {
        if (sampleClause != null) {
            sampleClause.setParent(this);
        }
        this.sampleClause = sampleClause;
    }
    
    @Override
    public OracleSelectPivotBase getPivot() {
        return this.pivot;
    }
    
    @Override
    public void setPivot(final OracleSelectPivotBase pivot) {
        if (pivot != null) {
            pivot.setParent(this);
        }
        this.pivot = pivot;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            this.accept0((OracleASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    protected void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
            this.acceptChild(visitor, this.partition);
            this.acceptChild(visitor, this.sampleClause);
            this.acceptChild(visitor, this.pivot);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final OracleSelectTableReference that = (OracleSelectTableReference)o;
        if (this.only != that.only) {
            return false;
        }
        Label_0085: {
            if (this.pivot != null) {
                if (this.pivot.equals(that.pivot)) {
                    break Label_0085;
                }
            }
            else if (that.pivot == null) {
                break Label_0085;
            }
            return false;
        }
        Label_0118: {
            if (this.partition != null) {
                if (this.partition.equals(that.partition)) {
                    break Label_0118;
                }
            }
            else if (that.partition == null) {
                break Label_0118;
            }
            return false;
        }
        if (this.sampleClause != null) {
            if (this.sampleClause.equals(that.sampleClause)) {
                return (this.flashback != null) ? this.flashback.equals(that.flashback) : (that.flashback == null);
            }
        }
        else if (that.sampleClause == null) {
            return (this.flashback != null) ? this.flashback.equals(that.flashback) : (that.flashback == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (this.only ? 1 : 0);
        result = 31 * result + ((this.pivot != null) ? this.pivot.hashCode() : 0);
        result = 31 * result + ((this.partition != null) ? this.partition.hashCode() : 0);
        result = 31 * result + ((this.sampleClause != null) ? this.sampleClause.hashCode() : 0);
        result = 31 * result + ((this.flashback != null) ? this.flashback.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toOracleString(this);
    }
    
    @Override
    public OracleSelectTableReference clone() {
        final OracleSelectTableReference x = new OracleSelectTableReference();
        this.cloneTo(x);
        x.only = this.only;
        if (this.pivot != null) {
            x.setPivot(this.pivot.clone());
        }
        if (this.partition != null) {
            x.setPartition(this.partition.clone());
        }
        if (this.sampleClause != null) {
            x.setSampleClause(this.sampleClause.clone());
        }
        if (this.flashback != null) {
            this.setFlashback(this.flashback.clone());
        }
        return x;
    }
}
