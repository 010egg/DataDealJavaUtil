// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.sql.SQLException;
import com.alibaba.druid.proxy.jdbc.WrapperProxy;
import java.sql.Wrapper;

public class PoolableWrapper implements Wrapper
{
    private final Wrapper wrapper;
    
    public PoolableWrapper(final Wrapper wraaper) {
        this.wrapper = wraaper;
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return null != this.wrapper && iface != null && (iface == this.wrapper.getClass() || iface == this.getClass() || (!(this.wrapper instanceof WrapperProxy) && iface.isInstance(this.wrapper)) || this.wrapper.isWrapperFor(iface));
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (null == this.wrapper) {
            return null;
        }
        if (iface == null) {
            return null;
        }
        if (iface == this.wrapper.getClass()) {
            return (T)this.wrapper;
        }
        if (iface == this.getClass()) {
            return (T)this;
        }
        if (!(this.wrapper instanceof WrapperProxy) && iface.isInstance(this.wrapper)) {
            return (T)this.wrapper;
        }
        return this.wrapper.unwrap(iface);
    }
}
