// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterMaterializedViewStatement extends SQLStatementImpl implements SQLAlterStatement
{
    private SQLName name;
    private boolean refreshFast;
    private boolean refreshComplete;
    private boolean refreshForce;
    private boolean refreshOnCommit;
    private boolean refreshOnDemand;
    private boolean refreshStartWith;
    private boolean refreshNext;
    private final List<SQLExpr> partitions;
    private Boolean enableQueryRewrite;
    private SQLExpr startWith;
    private SQLExpr next;
    protected boolean refreshOnOverWrite;
    private boolean rebuild;
    
    public SQLAlterMaterializedViewStatement() {
        this.partitions = new ArrayList<SQLExpr>();
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public boolean isRefresh() {
        return this.refreshFast || this.refreshComplete || this.refreshForce || this.refreshOnDemand || this.refreshOnCommit || this.refreshStartWith || this.refreshNext || this.refreshOnOverWrite;
    }
    
    public boolean isRefreshFast() {
        return this.refreshFast;
    }
    
    public void setRefreshFast(final boolean refreshFast) {
        this.refreshFast = refreshFast;
    }
    
    public boolean isRefreshComplete() {
        return this.refreshComplete;
    }
    
    public void setRefreshComplete(final boolean refreshComplete) {
        this.refreshComplete = refreshComplete;
    }
    
    public boolean isRefreshForce() {
        return this.refreshForce;
    }
    
    public void setRefreshForce(final boolean refreshForce) {
        this.refreshForce = refreshForce;
    }
    
    public boolean isRefreshOnCommit() {
        return this.refreshOnCommit;
    }
    
    public void setRefreshOnCommit(final boolean refreshOnCommit) {
        this.refreshOnCommit = refreshOnCommit;
    }
    
    public boolean isRefreshOnDemand() {
        return this.refreshOnDemand;
    }
    
    public void setRefreshOnDemand(final boolean refreshOnDemand) {
        this.refreshOnDemand = refreshOnDemand;
    }
    
    public boolean isRefreshOnOverWrite() {
        return this.refreshOnOverWrite;
    }
    
    public void setRefreshOnOverWrite(final boolean refreshOnOverWrite) {
        this.refreshOnOverWrite = refreshOnOverWrite;
    }
    
    public boolean isRefreshStartWith() {
        return this.refreshStartWith;
    }
    
    public void setRefreshStartWith(final boolean refreshStartWith) {
        this.refreshStartWith = refreshStartWith;
    }
    
    public boolean isRefreshNext() {
        return this.refreshNext;
    }
    
    public void setRefreshNext(final boolean refreshNext) {
        this.refreshNext = refreshNext;
    }
    
    public Boolean getEnableQueryRewrite() {
        return this.enableQueryRewrite;
    }
    
    public void setEnableQueryRewrite(final Boolean enableQueryRewrite) {
        this.enableQueryRewrite = enableQueryRewrite;
    }
    
    public SQLExpr getStartWith() {
        return this.startWith;
    }
    
    public void setStartWith(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.startWith = x;
    }
    
    public SQLExpr getNext() {
        return this.next;
    }
    
    public void setNext(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.next = x;
    }
    
    public boolean isRebuild() {
        return this.rebuild;
    }
    
    public void setRebuild(final boolean rebuild) {
        this.rebuild = rebuild;
    }
    
    public List<SQLExpr> getPartitions() {
        return this.partitions;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.startWith);
            this.acceptChild(visitor, this.next);
            this.acceptChild(visitor, this.partitions);
        }
        visitor.endVisit(this);
    }
}
