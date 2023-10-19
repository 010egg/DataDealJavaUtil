// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.ast.SQLPartition;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributes;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;

public class OracleCreateIndexStatement extends SQLCreateIndexStatement implements OracleDDLStatement, OracleSegmentAttributes, SQLCreateStatement
{
    private boolean online;
    private boolean indexOnlyTopLevel;
    private boolean cluster;
    private boolean noParallel;
    private SQLExpr parallel;
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
    private Boolean sort;
    private boolean reverse;
    protected SQLName tablespace;
    protected SQLObject storage;
    private Boolean enable;
    private boolean computeStatistics;
    private boolean local;
    private List<SQLName> localStoreIn;
    private List<SQLPartition> localPartitions;
    private boolean global;
    private List<SQLPartitionBy> globalPartitions;
    
    public OracleCreateIndexStatement() {
        super(DbType.oracle);
        this.online = false;
        this.indexOnlyTopLevel = false;
        this.cluster = false;
        this.enable = null;
        this.computeStatistics = false;
        this.localStoreIn = new ArrayList<SQLName>();
        this.localPartitions = new ArrayList<SQLPartition>();
        this.globalPartitions = new ArrayList<SQLPartitionBy>();
    }
    
    public SQLExpr getParallel() {
        return this.parallel;
    }
    
    public void setParallel(final SQLExpr parallel) {
        this.parallel = parallel;
    }
    
    public boolean isNoParallel() {
        return this.noParallel;
    }
    
    public void setNoParallel(final boolean noParallel) {
        this.noParallel = noParallel;
    }
    
    public boolean isIndexOnlyTopLevel() {
        return this.indexOnlyTopLevel;
    }
    
    public void setIndexOnlyTopLevel(final boolean indexOnlyTopLevel) {
        this.indexOnlyTopLevel = indexOnlyTopLevel;
    }
    
    public Boolean getSort() {
        return this.sort;
    }
    
    public void setSort(final Boolean sort) {
        this.sort = sort;
    }
    
    public boolean isReverse() {
        return this.reverse;
    }
    
    public void setReverse(final boolean reverse) {
        this.reverse = reverse;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
            this.acceptChild(visitor, this.getTable());
            this.acceptChild(visitor, this.getItems());
            this.acceptChild(visitor, this.getTablespace());
            this.acceptChild(visitor, this.parallel);
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
    
    public boolean isOnline() {
        return this.online;
    }
    
    public void setOnline(final boolean online) {
        this.online = online;
    }
    
    public boolean isCluster() {
        return this.cluster;
    }
    
    public void setCluster(final boolean cluster) {
        this.cluster = cluster;
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
    
    public Integer getPctthreshold() {
        return this.pctthreshold;
    }
    
    public void setPctthreshold(final Integer pctthreshold) {
        this.pctthreshold = pctthreshold;
    }
    
    @Override
    public Integer getPctfree() {
        return this.pctfree;
    }
    
    @Override
    public void setPctfree(final Integer ptcfree) {
        this.pctfree = ptcfree;
    }
    
    @Override
    public Integer getPctused() {
        return this.pctused;
    }
    
    @Override
    public void setPctused(final Integer ptcused) {
        this.pctused = ptcused;
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
    public Boolean getLogging() {
        return this.logging;
    }
    
    @Override
    public void setLogging(final Boolean logging) {
        this.logging = logging;
    }
    
    @Override
    public SQLObject getStorage() {
        return this.storage;
    }
    
    @Override
    public void setStorage(final SQLObject storage) {
        this.storage = storage;
    }
    
    @Override
    public boolean isCompressForOltp() {
        return this.compressForOltp;
    }
    
    @Override
    public void setCompressForOltp(final boolean compressForOltp) {
        this.compressForOltp = compressForOltp;
    }
    
    public List<SQLPartition> getLocalPartitions() {
        return this.localPartitions;
    }
    
    @Override
    public boolean isLocal() {
        return this.local;
    }
    
    @Override
    public void setLocal(final boolean local) {
        this.local = local;
    }
    
    public List<SQLName> getLocalStoreIn() {
        return this.localStoreIn;
    }
    
    public List<SQLPartitionBy> getGlobalPartitions() {
        return this.globalPartitions;
    }
    
    @Override
    public boolean isGlobal() {
        return this.global;
    }
    
    @Override
    public void setGlobal(final boolean global) {
        this.global = global;
    }
}
