// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class OracleLobParameters extends OracleSQLObjectImpl
{
    private SQLName tableSpace;
    private Boolean enableStorageInRow;
    private SQLExpr chunk;
    private Boolean cache;
    private Boolean logging;
    private Boolean compress;
    private Boolean keepDuplicates;
    private OracleStorageClause storage;
    private SQLExpr pctVersion;
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
    }
    
    public OracleStorageClause getStorage() {
        return this.storage;
    }
    
    public void setStorage(final OracleStorageClause x) {
        if (x != null) {
            x.setParent(this);
        }
        this.storage = x;
    }
    
    public SQLName getTableSpace() {
        return this.tableSpace;
    }
    
    public void setTableSpace(final SQLName tableSpace) {
        this.tableSpace = tableSpace;
    }
    
    public Boolean getEnableStorageInRow() {
        return this.enableStorageInRow;
    }
    
    public void setEnableStorageInRow(final Boolean enableStorageInRow) {
        this.enableStorageInRow = enableStorageInRow;
    }
    
    public SQLExpr getChunk() {
        return this.chunk;
    }
    
    public void setChunk(final SQLExpr chunk) {
        this.chunk = chunk;
    }
    
    public Boolean getCache() {
        return this.cache;
    }
    
    public void setCache(final Boolean cache) {
        this.cache = cache;
    }
    
    public Boolean getLogging() {
        return this.logging;
    }
    
    public void setLogging(final Boolean logging) {
        this.logging = logging;
    }
    
    public Boolean getCompress() {
        return this.compress;
    }
    
    public void setCompress(final Boolean compress) {
        this.compress = compress;
    }
    
    public Boolean getKeepDuplicates() {
        return this.keepDuplicates;
    }
    
    public void setKeepDuplicates(final Boolean keepDuplicates) {
        this.keepDuplicates = keepDuplicates;
    }
    
    public SQLExpr getPctVersion() {
        return this.pctVersion;
    }
    
    public void setPctVersion(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.pctVersion = x;
    }
}
