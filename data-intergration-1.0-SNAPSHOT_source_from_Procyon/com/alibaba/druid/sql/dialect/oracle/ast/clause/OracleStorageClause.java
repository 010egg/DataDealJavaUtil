// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.clause;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class OracleStorageClause extends OracleSQLObjectImpl
{
    private SQLExpr initial;
    private SQLExpr next;
    private SQLExpr minExtents;
    private SQLExpr maxExtents;
    private SQLExpr maxSize;
    private SQLExpr pctIncrease;
    private SQLExpr freeLists;
    private SQLExpr freeListGroups;
    private SQLExpr bufferPool;
    private SQLExpr objno;
    private FlashCacheType flashCache;
    private FlashCacheType cellFlashCache;
    
    @Override
    public OracleStorageClause clone() {
        final OracleStorageClause x = new OracleStorageClause();
        if (this.initial != null) {
            x.setInitial(this.initial.clone());
        }
        if (this.next != null) {
            x.setNext(this.next.clone());
        }
        if (this.minExtents != null) {
            x.setMinExtents(this.minExtents.clone());
        }
        if (this.maxExtents != null) {
            x.setMinExtents(this.maxExtents.clone());
        }
        if (this.maxSize != null) {
            x.setMaxSize(this.maxSize.clone());
        }
        if (this.pctIncrease != null) {
            x.setPctIncrease(this.pctIncrease.clone());
        }
        if (this.freeLists != null) {
            x.setFreeLists(this.freeLists.clone());
        }
        if (this.freeListGroups != null) {
            x.setFreeListGroups(this.freeListGroups.clone());
        }
        if (this.bufferPool != null) {
            x.setBufferPool(this.bufferPool.clone());
        }
        if (this.objno != null) {
            x.setObjno(this.objno.clone());
        }
        x.flashCache = this.flashCache;
        x.cellFlashCache = this.cellFlashCache;
        return x;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.initial);
            this.acceptChild(visitor, this.next);
            this.acceptChild(visitor, this.minExtents);
            this.acceptChild(visitor, this.maxExtents);
            this.acceptChild(visitor, this.maxSize);
            this.acceptChild(visitor, this.pctIncrease);
            this.acceptChild(visitor, this.freeLists);
            this.acceptChild(visitor, this.freeListGroups);
            this.acceptChild(visitor, this.bufferPool);
            this.acceptChild(visitor, this.objno);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getMaxSize() {
        return this.maxSize;
    }
    
    public void setMaxSize(final SQLExpr maxSize) {
        this.maxSize = maxSize;
    }
    
    public FlashCacheType getFlashCache() {
        return this.flashCache;
    }
    
    public void setFlashCache(final FlashCacheType flashCache) {
        this.flashCache = flashCache;
    }
    
    public FlashCacheType getCellFlashCache() {
        return this.cellFlashCache;
    }
    
    public void setCellFlashCache(final FlashCacheType cellFlashCache) {
        this.cellFlashCache = cellFlashCache;
    }
    
    public SQLExpr getPctIncrease() {
        return this.pctIncrease;
    }
    
    public void setPctIncrease(final SQLExpr pctIncrease) {
        this.pctIncrease = pctIncrease;
    }
    
    public SQLExpr getNext() {
        return this.next;
    }
    
    public void setNext(final SQLExpr next) {
        this.next = next;
    }
    
    public SQLExpr getMinExtents() {
        return this.minExtents;
    }
    
    public void setMinExtents(final SQLExpr minExtents) {
        this.minExtents = minExtents;
    }
    
    public SQLExpr getMaxExtents() {
        return this.maxExtents;
    }
    
    public void setMaxExtents(final SQLExpr maxExtents) {
        this.maxExtents = maxExtents;
    }
    
    public SQLExpr getObjno() {
        return this.objno;
    }
    
    public void setObjno(final SQLExpr objno) {
        this.objno = objno;
    }
    
    public SQLExpr getInitial() {
        return this.initial;
    }
    
    public void setInitial(final SQLExpr initial) {
        this.initial = initial;
    }
    
    public SQLExpr getFreeLists() {
        return this.freeLists;
    }
    
    public void setFreeLists(final SQLExpr freeLists) {
        this.freeLists = freeLists;
    }
    
    public SQLExpr getFreeListGroups() {
        return this.freeListGroups;
    }
    
    public void setFreeListGroups(final SQLExpr freeListGroups) {
        this.freeListGroups = freeListGroups;
    }
    
    public SQLExpr getBufferPool() {
        return this.bufferPool;
    }
    
    public void setBufferPool(final SQLExpr bufferPool) {
        this.bufferPool = bufferPool;
    }
    
    public enum FlashCacheType
    {
        KEEP, 
        NONE, 
        DEFAULT;
    }
}
