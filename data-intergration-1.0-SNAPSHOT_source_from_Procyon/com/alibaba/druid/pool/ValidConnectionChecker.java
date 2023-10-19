// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.util.Properties;
import java.sql.Connection;

public interface ValidConnectionChecker
{
    boolean isValidConnection(final Connection p0, final String p1, final int p2) throws Exception;
    
    void configFromProperties(final Properties p0);
}
