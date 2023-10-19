// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLPartition;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributesImpl;

public class OracleUsingIndexClause extends OracleSegmentAttributesImpl implements OracleSQLObject
{
    private SQLObject index;
    private Boolean enable;
    private boolean computeStatistics;
    private boolean reverse;
    private List<SQLPartition> localPartitionIndex;
    
    public OracleUsingIndexClause() {
        this.enable = null;
        this.computeStatistics = false;
        this.localPartitionIndex = new ArrayList<SQLPartition>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.index);
            this.acceptChild(visitor, this.tablespace);
            this.acceptChild(visitor, this.storage);
        }
        visitor.endVisit(this);
    }
    
    public Boolean getEnable() {
        return this.enable;
    }
    
    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }
    
    public boolean isComputeStatistics() {
        return this.computeStatistics;
    }
    
    public void setComputeStatistics(final boolean computeStatistics) {
        this.computeStatistics = computeStatistics;
    }
    
    public SQLObject getIndex() {
        return this.index;
    }
    
    public void setIndex(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.index = x;
    }
    
    public void setIndex(final SQLCreateIndexStatement x) {
        if (x != null) {
            x.setParent(this);
        }
        this.index = x;
    }
    
    public boolean isReverse() {
        return this.reverse;
    }
    
    public void setReverse(final boolean reverse) {
        this.reverse = reverse;
    }
    
    public List<SQLPartition> getLocalPartitionIndex() {
        return this.localPartitionIndex;
    }
    
    public void cloneTo(final OracleUsingIndexClause x) {
        super.cloneTo(x);
        if (this.index != null) {
            final SQLObject idx = this.index.clone();
            idx.setParent(x);
            x.index = idx;
        }
        x.enable = this.enable;
        x.computeStatistics = this.computeStatistics;
        x.reverse = this.reverse;
        for (final SQLPartition p : this.localPartitionIndex) {
            final SQLPartition p2 = p.clone();
            p2.setParent(x);
            x.localPartitionIndex.add(p2);
        }
    }
    
    @Override
    public OracleUsingIndexClause clone() {
        final OracleUsingIndexClause x = new OracleUsingIndexClause();
        this.cloneTo(x);
        return x;
    }
}
