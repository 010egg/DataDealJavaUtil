// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.vendor;

import java.util.Properties;
import com.alibaba.druid.mock.MockConnectionClosedException;
import java.sql.SQLException;
import com.alibaba.druid.pool.ExceptionSorter;

public class MockExceptionSorter implements ExceptionSorter
{
    private static final MockExceptionSorter instance;
    
    public static final MockExceptionSorter getInstance() {
        return MockExceptionSorter.instance;
    }
    
    @Override
    public boolean isExceptionFatal(final SQLException e) {
        return e instanceof MockConnectionClosedException;
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
    }
    
    static {
        instance = new MockExceptionSorter();
    }
}
