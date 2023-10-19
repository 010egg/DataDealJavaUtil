// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import javax.management.ObjectName;
import javax.sql.DataSource;

public interface ManagedDataSource extends DataSource
{
    boolean isEnable();
    
    void setEnable(final boolean p0);
    
    ObjectName getObjectName();
    
    void setObjectName(final ObjectName p0);
}
