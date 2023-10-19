// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.util.Properties;
import java.sql.SQLException;

public interface ExceptionSorter
{
    boolean isExceptionFatal(final SQLException p0);
    
    void configFromProperties(final Properties p0);
}
