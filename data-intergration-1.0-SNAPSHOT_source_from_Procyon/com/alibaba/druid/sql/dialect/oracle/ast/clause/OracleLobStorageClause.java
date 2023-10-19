// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.clause;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributesImpl;

public class OracleLobStorageClause extends OracleSegmentAttributesImpl implements OracleSQLObject
{
    private SQLName segementName;
    private final List<SQLName> items;
    private boolean secureFile;
    private boolean basicFile;
    private Boolean enable;
    private SQLExpr chunk;
    private Boolean cache;
    private Boolean logging;
    private Boolean compress;
    private Boolean keepDuplicate;
    private boolean retention;
    private OracleStorageClause storageClause;
    private SQLExpr pctversion;
    
    public OracleLobStorageClause() {
        this.items = new ArrayList<SQLName>();
        this.secureFile = false;
        this.basicFile = false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.segementName);
            this.acceptChild(visitor, this.items);
            this.acceptChild(visitor, this.tablespace);
        }
        visitor.endVisit(this);
    }
    
    public void cloneTo(final OracleLobStorageClause x) {
        super.cloneTo(x);
        if (this.segementName != null) {
            x.setSegementName(this.segementName.clone());
        }
        for (final SQLName item : this.items) {
            final SQLName item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }
        x.secureFile = this.secureFile;
        x.basicFile = this.basicFile;
        x.enable = this.enable;
        if (this.chunk != null) {
            x.setChunk(this.chunk.clone());
        }
        x.cache = this.cache;
        x.logging = this.logging;
        x.compress = this.compress;
        x.keepDuplicate = this.keepDuplicate;
        x.retention = this.retention;
        if (this.storageClause != null) {
            x.setStorageClause(this.storageClause.clone());
        }
        if (this.pctversion != null) {
            x.setPctversion(this.pctversion.clone());
        }
    }
    
    @Override
    public OracleLobStorageClause clone() {
        final OracleLobStorageClause x = new OracleLobStorageClause();
        this.cloneTo(x);
        return x;
    }
    
    public Boolean getEnable() {
        return this.enable;
    }
    
    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }
    
    public SQLExpr getChunk() {
        return this.chunk;
    }
    
    public void setChunk(final SQLExpr chunk) {
        this.chunk = chunk;
    }
    
    public List<SQLName> getItems() {
        return this.items;
    }
    
    public boolean isSecureFile() {
        return this.secureFile;
    }
    
    public void setSecureFile(final boolean secureFile) {
        this.secureFile = secureFile;
    }
    
    public boolean isBasicFile() {
        return this.basicFile;
    }
    
    public void setBasicFile(final boolean basicFile) {
        this.basicFile = basicFile;
    }
    
    public Boolean getCache() {
        return this.cache;
    }
    
    public void setCache(final Boolean cache) {
        this.cache = cache;
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
    public Boolean getCompress() {
        return this.compress;
    }
    
    @Override
    public void setCompress(final Boolean compress) {
        this.compress = compress;
    }
    
    public Boolean getKeepDuplicate() {
        return this.keepDuplicate;
    }
    
    public void setKeepDuplicate(final Boolean keepDuplicate) {
        this.keepDuplicate = keepDuplicate;
    }
    
    public boolean isRetention() {
        return this.retention;
    }
    
    public void setRetention(final boolean retention) {
        this.retention = retention;
    }
    
    public OracleStorageClause getStorageClause() {
        return this.storageClause;
    }
    
    public void setStorageClause(final OracleStorageClause storageClause) {
        if (storageClause != null) {
            storageClause.setParent(this);
        }
        this.storageClause = storageClause;
    }
    
    public SQLExpr getPctversion() {
        return this.pctversion;
    }
    
    public void setPctversion(final SQLExpr pctversion) {
        if (pctversion != null) {
            pctversion.setParent(this);
        }
        this.pctversion = pctversion;
    }
    
    public SQLName getSegementName() {
        return this.segementName;
    }
    
    public void setSegementName(final SQLName segementName) {
        this.segementName = segementName;
    }
}
