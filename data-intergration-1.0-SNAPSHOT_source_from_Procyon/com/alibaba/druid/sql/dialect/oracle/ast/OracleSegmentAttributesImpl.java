// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public abstract class OracleSegmentAttributesImpl extends SQLObjectImpl implements OracleSegmentAttributes
{
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
    protected SQLName tablespace;
    protected SQLObject storage;
    
    @Override
    public SQLName getTablespace() {
        return this.tablespace;
    }
    
    @Override
    public void setTablespace(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.tablespace = x;
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
    
    public void cloneTo(final OracleSegmentAttributesImpl x) {
        x.pctfree = this.pctfree;
        x.pctused = this.pctused;
        x.initrans = this.initrans;
        x.maxtrans = this.maxtrans;
        x.pctincrease = this.pctincrease;
        x.freeLists = this.freeLists;
        x.compress = this.compress;
        x.compressLevel = this.compressLevel;
        x.compressForOltp = this.compressForOltp;
        x.pctthreshold = this.pctthreshold;
        x.logging = this.logging;
        if (this.tablespace != null) {
            x.setTablespace(this.tablespace.clone());
        }
        if (this.storage != null) {
            x.setStorage(this.storage.clone());
        }
    }
}
