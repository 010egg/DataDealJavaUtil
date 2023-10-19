// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Map;
import java.sql.Wrapper;

public interface WrapperProxy extends Wrapper
{
    long getId();
    
    Object getRawObject();
    
    int getAttributesSize();
    
    void clearAttributes();
    
    Map<String, Object> getAttributes();
    
    Object getAttribute(final String p0);
    
    void putAttribute(final String p0, final Object p1);
}
