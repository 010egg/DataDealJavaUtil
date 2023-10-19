// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.util.Map;
import java.sql.SQLException;
import java.sql.Struct;

public class MockStruct implements Struct
{
    @Override
    public String getSQLTypeName() throws SQLException {
        return null;
    }
    
    @Override
    public Object[] getAttributes() throws SQLException {
        return null;
    }
    
    @Override
    public Object[] getAttributes(final Map<String, Class<?>> map) throws SQLException {
        return null;
    }
}
