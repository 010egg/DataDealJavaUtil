// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributes;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCreateMaterializedViewStatement extends SQLStatementImpl implements OracleSegmentAttributes, SQLCreateStatement, SQLReplaceable
{
    protected SQLExpr lifyCycle;
    private SQLName name;
    private List<SQLName> columns;
    private boolean ifNotExists;
    private boolean refreshFast;
    private boolean refreshComplete;
    private boolean refreshForce;
    private boolean refreshOnCommit;
    private boolean refreshOnDemand;
    private boolean refreshStartWith;
    private boolean refreshNext;
    private boolean buildImmediate;
    private boolean buildDeferred;
    private SQLSelect query;
    private Integer pctfree;
    private Integer pctused;
    private Integer initrans;
    private Integer maxtrans;
    private Integer pctincrease;
    private Integer freeLists;
    private Boolean compress;
    private Integer compressLevel;
    private boolean compressForOltp;
    private Integer pctthreshold;
    private Boolean logging;
    private Boolean cache;
    protected SQLName tablespace;
    protected SQLObject storage;
    private Boolean parallel;
    private Integer parallelValue;
    private Boolean enableQueryRewrite;
    private SQLPartitionBy partitionBy;
    private SQLExpr startWith;
    private SQLExpr next;
    private boolean withRowId;
    protected boolean refreshOnOverWrite;
    protected List<SQLTableElement> tableElementList;
    protected SQLName distributedByType;
    protected List<SQLName> distributedBy;
    protected final List<SQLAssignItem> tableOptions;
    protected SQLExpr comment;
    private List<SQLName> partitionedOn;
    
    public SQLCreateMaterializedViewStatement() {
        this.columns = new ArrayList<SQLName>();
        this.tableElementList = new ArrayList<SQLTableElement>();
        this.distributedBy = new ArrayList<SQLName>();
        this.tableOptions = new ArrayList<SQLAssignItem>();
        this.partitionedOn = new ArrayList<SQLName>();
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
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public SQLSelect getQuery() {
        return this.query;
    }
    
    public void setQuery(final SQLSelect query) {
        if (query != null) {
            query.setParent(this);
        }
        this.query = query;
    }
    
    public boolean isBuildImmediate() {
        return this.buildImmediate;
    }
    
    public void setBuildImmediate(final boolean buildImmediate) {
        this.buildImmediate = buildImmediate;
    }
    
    public boolean isBuildDeferred() {
        return this.buildDeferred;
    }
    
    public void setBuildDeferred(final boolean buildDeferred) {
        this.buildDeferred = buildDeferred;
    }
    
    public boolean isRefresh() {
        return this.refreshFast || this.refreshComplete || this.refreshForce || this.refreshOnDemand || this.refreshOnCommit || this.refreshStartWith || this.refreshNext;
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
    
    @Override
    public Integer getPctfree() {
        return this.pctfree;
    }
    
    @Override
    public void setPctfree(final Integer pctfree) {
        this.pctfree = pctfree;
    }
    
    @Override
    public Integer getPctused() {
        return this.pctused;
    }
    
    @Override
    public void setPctused(final Integer pctused) {
        this.pctused = pctused;
    }
    
    @Override
    public Integer getInitrans() {
        return this.initrans;
    }
    
    @Override
    public void setInitrans(final Integer initrans) {
        this.initrans = initrans;
    }
    
    @Override
    public Integer getMaxtrans() {
        return this.maxtrans;
    }
    
    @Override
    public void setMaxtrans(final Integer maxtrans) {
        this.maxtrans = maxtrans;
    }
    
    @Override
    public Integer getPctincrease() {
        return this.pctincrease;
    }
    
    @Override
    public void setPctincrease(final Integer pctincrease) {
        this.pctincrease = pctincrease;
    }
    
    public Integer getFreeLists() {
        return this.freeLists;
    }
    
    public void setFreeLists(final Integer freeLists) {
        this.freeLists = freeLists;
    }
    
    @Override
    public Boolean getCompress() {
        return this.compress;
    }
    
    @Override
    public void setCompress(final Boolean compress) {
        this.compress = compress;
    }
    
    @Override
    public Integer getCompressLevel() {
        return this.compressLevel;
    }
    
    @Override
    public void setCompressLevel(final Integer compressLevel) {
        this.compressLevel = compressLevel;
    }
    
    @Override
    public boolean isCompressForOltp() {
        return this.compressForOltp;
    }
    
    @Override
    public void setCompressForOltp(final boolean compressForOltp) {
        this.compressForOltp = compressForOltp;
    }
    
    public Integer getPctthreshold() {
        return this.pctthreshold;
    }
    
    public void setPctthreshold(final Integer pctthreshold) {
        this.pctthreshold = pctthreshold;
    }
    
    @Override
    public Boolean getLogging() {
        return this.logging;
    }
    
    @Override
    public void setLogging(final Boolean logging) {
        this.logging = logging;
    }
    
    @Override
    public SQLName getTablespace() {
        return this.tablespace;
    }
    
    @Override
    public void setTablespace(final SQLName tablespace) {
        if (tablespace != null) {
            tablespace.setParent(this);
        }
        this.tablespace = tablespace;
    }
    
    @Override
    public SQLObject getStorage() {
        return this.storage;
    }
    
    @Override
    public void setStorage(final SQLObject storage) {
        if (storage != null) {
            storage.setParent(this);
        }
        this.storage = storage;
    }
    
    public Boolean getParallel() {
        return this.parallel;
    }
    
    public void setParallel(final Boolean parallel) {
        this.parallel = parallel;
    }
    
    public Integer getParallelValue() {
        return this.parallelValue;
    }
    
    public void setParallelValue(final Integer parallelValue) {
        this.parallelValue = parallelValue;
    }
    
    public Boolean getEnableQueryRewrite() {
        return this.enableQueryRewrite;
    }
    
    public void setEnableQueryRewrite(final Boolean enableQueryRewrite) {
        this.enableQueryRewrite = enableQueryRewrite;
    }
    
    public Boolean getCache() {
        return this.cache;
    }
    
    public void setCache(final Boolean cache) {
        this.cache = cache;
    }
    
    public SQLPartitionBy getPartitionBy() {
        return this.partitionBy;
    }
    
    public List<SQLTableElement> getTableElementList() {
        return this.tableElementList;
    }
    
    public List<SQLName> getDistributedBy() {
        return this.distributedBy;
    }
    
    public SQLName getDistributedByType() {
        return this.distributedByType;
    }
    
    public void setDistributedByType(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.distributedByType = x;
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
    
    public void setPartitionBy(final SQLPartitionBy x) {
        if (x != null) {
            x.setParent(this);
        }
        this.partitionBy = x;
    }
    
    public boolean isWithRowId() {
        return this.withRowId;
    }
    
    public void setWithRowId(final boolean withRowId) {
        this.withRowId = withRowId;
    }
    
    public List<SQLName> getPartitionedOn() {
        return this.partitionedOn;
    }
    
    public void addOption(final String name, final SQLExpr value) {
        final SQLAssignItem assignItem = new SQLAssignItem(new SQLIdentifierExpr(name), value);
        assignItem.setParent(this);
        this.tableOptions.add(assignItem);
    }
    
    public List<SQLAssignItem> getTableOptions() {
        return this.tableOptions;
    }
    
    public SQLExpr getOption(final String name) {
        if (name == null) {
            return null;
        }
        final long hash64 = FnvHash.hashCode64(name);
        for (final SQLAssignItem item : this.tableOptions) {
            final SQLExpr target = item.getTarget();
            if (target instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)target).hashCode64() == hash64) {
                return item.getValue();
            }
        }
        return null;
    }
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.comment = x;
    }
    
    public SQLExpr getLifyCycle() {
        return this.lifyCycle;
    }
    
    public void setLifyCycle(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.lifyCycle = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.partitionBy);
            this.acceptChild(visitor, this.query);
            this.acceptChild(visitor, this.tableElementList);
            this.acceptChild(visitor, this.distributedBy);
            this.acceptChild(visitor, this.distributedByType);
            this.acceptChild(visitor, this.startWith);
            this.acceptChild(visitor, this.next);
            this.acceptChild(visitor, this.tableOptions);
            this.acceptChild(visitor, this.comment);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (expr == this.name) {
            this.setName((SQLName)target);
            return true;
        }
        if (expr == this.next) {
            this.setNext(target);
            return true;
        }
        if (expr == this.startWith) {
            this.setStartWith(target);
            return true;
        }
        return false;
    }
}
