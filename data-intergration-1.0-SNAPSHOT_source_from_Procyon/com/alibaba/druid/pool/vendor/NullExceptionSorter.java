// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.vendor;

import java.util.Properties;
import java.sql.SQLException;
import com.alibaba.druid.pool.ExceptionSorter;

public class NullExceptionSorter implements ExceptionSorter
{
    private static final NullExceptionSorter instance;
    
    public static final NullExceptionSorter getInstance() {
        return NullExceptionSorter.instance;
    }
    
    @Override
    public boolean isExceptionFatal(final SQLException e) {
        return false;
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
    }
    
    static {
        instance = new NullExceptionSorter();
    }
}
