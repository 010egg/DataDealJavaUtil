// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.util.Map;
import java.sql.SQLException;
import java.sql.Ref;

public class MockRef implements Ref
{
    private String baseTypeName;
    private Object object;
    
    public void setBaseTypeName(final String baseTypeName) {
        this.baseTypeName = baseTypeName;
    }
    
    @Override
    public String getBaseTypeName() throws SQLException {
        return this.baseTypeName;
    }
    
    @Override
    public Object getObject(final Map<String, Class<?>> map) throws SQLException {
        return this.object;
    }
    
    @Override
    public Object getObject() throws SQLException {
        return this.object;
    }
    
    @Override
    public void setObject(final Object value) throws SQLException {
        this.object = value;
    }
}
