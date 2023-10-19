// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.sql.Wrapper;

public class WrapperAdapter implements Wrapper
{
    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        return iface != null && iface.isInstance(this);
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) {
        if (iface == null) {
            return null;
        }
        if (iface.isInstance(this)) {
            return (T)this;
        }
        return null;
    }
}
