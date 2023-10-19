// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLAnnIndex extends SQLObjectImpl
{
    private int indexType;
    private int rtIndexType;
    private Distance distance;
    
    @Override
    public SQLAnnIndex clone() {
        final SQLAnnIndex x = new SQLAnnIndex();
        x.indexType = this.indexType;
        x.rtIndexType = this.rtIndexType;
        x.distance = this.distance;
        return x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {}
        v.endVisit(this);
    }
    
    public void setIndexType(final String type) {
        this.indexType = of(type);
    }
    
    public void setIndexType(final IndexType indexType, final boolean state) {
        if (state) {
            this.indexType |= indexType.mask;
        }
        else {
            this.indexType &= ~indexType.mask;
        }
    }
    
    public void setRtIndexType(final IndexType indexType, final boolean state) {
        if (state) {
            this.rtIndexType |= indexType.mask;
        }
        else {
            this.rtIndexType &= ~indexType.mask;
        }
    }
    
    public void setRtIndexType(final String type) {
        this.rtIndexType = of(type);
    }
    
    public int getIndexType() {
        return this.indexType;
    }
    
    public int getRtIndexType() {
        return this.rtIndexType;
    }
    
    public Distance getDistance() {
        return this.distance;
    }
    
    public void setDistance(final Distance distance) {
        this.distance = distance;
    }
    
    public void setDistance(final String distance) {
        if (distance == null) {
            this.distance = null;
            return;
        }
        if (distance.equalsIgnoreCase("Hamming")) {
            this.distance = Distance.Hamming;
        }
        else if (distance.equalsIgnoreCase("SquaredEuclidean")) {
            this.distance = Distance.SquaredEuclidean;
        }
        else if (distance.equalsIgnoreCase("DotProduct")) {
            this.distance = Distance.DotProduct;
        }
    }
    
    public enum IndexType
    {
        Flat(1), 
        FastIndex(2);
        
        public final int mask;
        
        private IndexType(final int ordinal) {
            this.mask = 1 << ordinal;
        }
        
        private static int of(final String type) {
            if (type == null || type.length() == 0) {
                return 0;
            }
            int v = 0;
            final String[] split;
            final String[] items = split = type.split(",");
            for (final String item : split) {
                if (item.trim().equalsIgnoreCase("Flat")) {
                    v |= IndexType.Flat.mask;
                }
                else if (item.trim().equalsIgnoreCase("FastIndex") || item.trim().equalsIgnoreCase("FAST_INDEX")) {
                    v |= IndexType.FastIndex.mask;
                }
            }
            return v;
        }
    }
    
    public enum Distance
    {
        Hamming, 
        SquaredEuclidean, 
        DotProduct;
    }
}
