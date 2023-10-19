// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.HashMap;
import java.sql.SQLException;
import com.alibaba.druid.filter.FilterChain;
import java.util.Map;
import java.sql.Wrapper;

public abstract class WrapperProxyImpl implements WrapperProxy
{
    private final Wrapper raw;
    private final long id;
    private Map<String, Object> attributes;
    
    public WrapperProxyImpl(final Wrapper wrapper, final long id) {
        this.raw = wrapper;
        this.id = id;
    }
    
    @Override
    public long getId() {
        return this.id;
    }
    
    @Override
    public Object getRawObject() {
        return this.raw;
    }
    
    public abstract FilterChain createChain();
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface != null && (iface == this.getClass() || this.createChain().isWrapperFor(this.raw, iface));
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface == null) {
            return null;
        }
        if (iface == this.getClass()) {
            return (T)this;
        }
        return this.createChain().unwrap(this.raw, iface);
    }
    
    @Override
    public int getAttributesSize() {
        if (this.attributes == null) {
            return 0;
        }
        return this.attributes.size();
    }
    
    @Override
    public void clearAttributes() {
        if (this.attributes == null) {
            return;
        }
        this.attributes.clear();
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>(4);
        }
        return this.attributes;
    }
    
    @Override
    public void putAttribute(final String key, final Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>(4);
        }
        this.attributes.put(key, value);
    }
    
    @Override
    public Object getAttribute(final String key) {
        if (this.attributes == null) {
            return null;
        }
        return this.attributes.get(key);
    }
}
