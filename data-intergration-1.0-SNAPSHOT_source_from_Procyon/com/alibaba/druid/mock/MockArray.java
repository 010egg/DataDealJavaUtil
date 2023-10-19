// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Map;
import java.sql.SQLException;
import java.sql.Array;

public class MockArray implements Array
{
    private int baseType;
    
    @Override
    public String getBaseTypeName() throws SQLException {
        return null;
    }
    
    @Override
    public int getBaseType() throws SQLException {
        return this.baseType;
    }
    
    @Override
    public Object getArray() throws SQLException {
        return null;
    }
    
    @Override
    public Object getArray(final Map<String, Class<?>> map) throws SQLException {
        return null;
    }
    
    @Override
    public Object getArray(final long index, final int count) throws SQLException {
        return null;
    }
    
    @Override
    public Object getArray(final long index, final int count, final Map<String, Class<?>> map) throws SQLException {
        return null;
    }
    
    @Override
    public ResultSet getResultSet() throws SQLException {
        return new MockResultSet(null);
    }
    
    @Override
    public ResultSet getResultSet(final Map<String, Class<?>> map) throws SQLException {
        return new MockResultSet(null);
    }
    
    @Override
    public ResultSet getResultSet(final long index, final int count) throws SQLException {
        return new MockResultSet(null);
    }
    
    @Override
    public ResultSet getResultSet(final long index, final int count, final Map<String, Class<?>> map) throws SQLException {
        return new MockResultSet(null);
    }
    
    @Override
    public void free() throws SQLException {
    }
}
