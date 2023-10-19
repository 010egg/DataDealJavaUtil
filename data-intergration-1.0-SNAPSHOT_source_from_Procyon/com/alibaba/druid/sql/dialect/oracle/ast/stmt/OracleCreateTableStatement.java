// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributesImpl;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleLobStorageClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributes;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;

public class OracleCreateTableStatement extends SQLCreateTableStatement implements OracleDDLStatement, OracleSegmentAttributes
{
    private boolean inMemoryMetadata;
    private boolean cursorSpecificSegment;
    private Boolean parallel;
    private SQLExpr parallelValue;
    private OracleStorageClause storage;
    private OracleLobStorageClause lobStorage;
    private Integer pctfree;
    private Integer pctused;
    private Integer initrans;
    private Integer maxtrans;
    private Integer pctincrease;
    private Integer compressLevel;
    private boolean compressForOltp;
    private Boolean cache;
    private DeferredSegmentCreation deferredSegmentCreation;
    private Boolean enableRowMovement;
    private List<SQLName> clusterColumns;
    private SQLName cluster;
    private Organization organization;
    private SQLName of;
    private OIDIndex oidIndex;
    private boolean monitoring;
    private List<SQLName> including;
    private OracleXmlColumnProperties xmlTypeColumnProperties;
    
    @Override
    public void simplify() {
        this.tablespace = null;
        this.storage = null;
        this.lobStorage = null;
        this.pctfree = null;
        this.pctused = null;
        this.initrans = null;
        this.maxtrans = null;
        this.pctincrease = null;
        this.logging = null;
        this.compress = null;
        this.compressLevel = null;
        this.compressForOltp = false;
        this.onCommitPreserveRows = false;
        this.onCommitDeleteRows = false;
        super.simplify();
    }
    
    public OracleCreateTableStatement() {
        super(DbType.oracle);
        this.clusterColumns = new ArrayList<SQLName>();
        this.including = new ArrayList<SQLName>();
    }
    
    public OracleLobStorageClause getLobStorage() {
        return this.lobStorage;
    }
    
    public void setLobStorage(final OracleLobStorageClause lobStorage) {
        this.lobStorage = lobStorage;
    }
    
    public DeferredSegmentCreation getDeferredSegmentCreation() {
        return this.deferredSegmentCreation;
    }
    
    public void setDeferredSegmentCreation(final DeferredSegmentCreation deferredSegmentCreation) {
        this.deferredSegmentCreation = deferredSegmentCreation;
    }
    
    public Boolean getCache() {
        return this.cache;
    }
    
    public void setCache(final Boolean cache) {
        this.cache = cache;
    }
    
    public boolean isOnCommitDeleteRows() {
        return this.onCommitDeleteRows;
    }
    
    public void setOnCommitDeleteRows(final boolean onCommitDeleteRows) {
        this.onCommitDeleteRows = onCommitDeleteRows;
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
    
    public Boolean getParallel() {
        return this.parallel;
    }
    
    public void setParallel(final Boolean parallel) {
        this.parallel = parallel;
    }
    
    public SQLExpr getParallelValue() {
        return this.parallelValue;
    }
    
    public void setParallelValue(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.parallelValue = x;
    }
    
    public boolean isCursorSpecificSegment() {
        return this.cursorSpecificSegment;
    }
    
    public void setCursorSpecificSegment(final boolean cursorSpecificSegment) {
        this.cursorSpecificSegment = cursorSpecificSegment;
    }
    
    public boolean isInMemoryMetadata() {
        return this.inMemoryMetadata;
    }
    
    public void setInMemoryMetadata(final boolean inMemoryMetadata) {
        this.inMemoryMetadata = inMemoryMetadata;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public OracleStorageClause getStorage() {
        return this.storage;
    }
    
    @Override
    public void setStorage(final SQLObject storage) {
        if (storage != null) {
            storage.setParent(this);
        }
        this.storage = (OracleStorageClause)storage;
    }
    
    public SQLName getOf() {
        return this.of;
    }
    
    public void setOf(final SQLName of) {
        if (of != null) {
            of.setParent(this);
        }
        this.of = of;
    }
    
    public OIDIndex getOidIndex() {
        return this.oidIndex;
    }
    
    public void setOidIndex(final OIDIndex oidIndex) {
        if (oidIndex != null) {
            oidIndex.setParent(this);
        }
        this.oidIndex = oidIndex;
    }
    
    public boolean isMonitoring() {
        return this.monitoring;
    }
    
    public void setMonitoring(final boolean monitoring) {
        this.monitoring = monitoring;
    }
    
    @Override
    public boolean isCompressForOltp() {
        return this.compressForOltp;
    }
    
    @Override
    public void setCompressForOltp(final boolean compressForOltp) {
        this.compressForOltp = compressForOltp;
    }
    
    public Boolean getEnableRowMovement() {
        return this.enableRowMovement;
    }
    
    public void setEnableRowMovement(final Boolean enableRowMovement) {
        this.enableRowMovement = enableRowMovement;
    }
    
    public List<SQLName> getClusterColumns() {
        return this.clusterColumns;
    }
    
    public SQLName getCluster() {
        return this.cluster;
    }
    
    public void setCluster(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.cluster = x;
    }
    
    public List<SQLName> getIncluding() {
        return this.including;
    }
    
    public Organization getOrganization() {
        return this.organization;
    }
    
    public void setOrganization(final Organization organization) {
        if (organization != null) {
            organization.setParent(this);
        }
        this.organization = organization;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.of);
            this.acceptChild(visitor, this.tableElementList);
            this.acceptChild(visitor, this.tablespace);
            this.acceptChild(visitor, this.select);
            this.acceptChild(visitor, this.storage);
            this.acceptChild(visitor, this.partitioning);
        }
        visitor.endVisit(this);
    }
    
    public OracleXmlColumnProperties getXmlTypeColumnProperties() {
        return this.xmlTypeColumnProperties;
    }
    
    public void setXmlTypeColumnProperties(final OracleXmlColumnProperties x) {
        if (x != null) {
            x.setParent(this);
        }
        this.xmlTypeColumnProperties = x;
    }
    
    public enum DeferredSegmentCreation
    {
        IMMEDIATE, 
        DEFERRED;
    }
    
    public static class Organization extends OracleSegmentAttributesImpl implements OracleSegmentAttributes, OracleSQLObject
    {
        public String type;
        private SQLName externalType;
        private SQLExpr externalDirectory;
        private SQLExternalRecordFormat externalDirectoryRecordFormat;
        private List<SQLExpr> externalDirectoryLocation;
        private SQLExpr externalRejectLimit;
        
        public Organization() {
            this.externalDirectoryLocation = new ArrayList<SQLExpr>();
        }
        
        @Override
        protected void accept0(final SQLASTVisitor visitor) {
            this.accept0((OracleASTVisitor)visitor);
        }
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.tablespace);
                this.acceptChild(visitor, this.storage);
            }
            visitor.endVisit(this);
        }
        
        public String getType() {
            return this.type;
        }
        
        public void setType(final String type) {
            this.type = type;
        }
        
        public SQLName getExternalType() {
            return this.externalType;
        }
        
        public void setExternalType(final SQLName externalType) {
            this.externalType = externalType;
        }
        
        public SQLExpr getExternalDirectory() {
            return this.externalDirectory;
        }
        
        public void setExternalDirectory(final SQLExpr externalDirectory) {
            this.externalDirectory = externalDirectory;
        }
        
        public SQLExternalRecordFormat getExternalDirectoryRecordFormat() {
            return this.externalDirectoryRecordFormat;
        }
        
        public void setExternalDirectoryRecordFormat(final SQLExternalRecordFormat recordFormat) {
            if (recordFormat != null) {
                recordFormat.setParent(this);
            }
            this.externalDirectoryRecordFormat = recordFormat;
        }
        
        public SQLExpr getExternalRejectLimit() {
            return this.externalRejectLimit;
        }
        
        public void setExternalRejectLimit(final SQLExpr externalRejectLimit) {
            if (externalRejectLimit != null) {
                externalRejectLimit.setParent(this);
            }
            this.externalRejectLimit = externalRejectLimit;
        }
        
        public List<SQLExpr> getExternalDirectoryLocation() {
            return this.externalDirectoryLocation;
        }
    }
    
    public static class OIDIndex extends OracleSegmentAttributesImpl implements OracleSQLObject
    {
        private SQLName name;
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.name);
                this.acceptChild(visitor, this.tablespace);
                this.acceptChild(visitor, this.storage);
            }
            visitor.endVisit(this);
        }
        
        @Override
        protected void accept0(final SQLASTVisitor visitor) {
            this.accept0((OracleASTVisitor)visitor);
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
    }
}
