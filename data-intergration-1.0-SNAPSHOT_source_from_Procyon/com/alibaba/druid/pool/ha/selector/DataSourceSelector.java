// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.selector;

import javax.sql.DataSource;

public interface DataSourceSelector
{
    DataSource get();
    
    void setTarget(final String p0);
    
    String getName();
    
    void init();
    
    void destroy();
}
