// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.stat;

public class MergeStatFilter extends StatFilter
{
    public MergeStatFilter() {
        super.setMergeSql(true);
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        return iface == MergeStatFilter.class || iface == StatFilter.class;
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) {
        if (iface == MergeStatFilter.class || iface == StatFilter.class) {
            return (T)this;
        }
        return null;
    }
}
