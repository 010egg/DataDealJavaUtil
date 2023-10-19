// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.DbType;

public class SQLASTVisitorAdapter implements SQLASTVisitor
{
    protected DbType dbType;
    protected int features;
    
    public final boolean isEnabled(final VisitorFeature feature) {
        return VisitorFeature.isEnabled(this.features, feature);
    }
    
    public void config(final VisitorFeature feature, final boolean state) {
        this.features = VisitorFeature.config(this.features, feature, state);
    }
    
    public int getFeatures() {
        return this.features;
    }
    
    public void setFeatures(final int features) {
        this.features = features;
    }
}
