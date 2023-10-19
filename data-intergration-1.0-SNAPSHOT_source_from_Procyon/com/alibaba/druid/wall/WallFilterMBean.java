// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.sql.SQLException;
import java.util.Set;

public interface WallFilterMBean
{
    String getDbType();
    
    boolean isLogViolation();
    
    void setLogViolation(final boolean p0);
    
    boolean isThrowException();
    
    void setThrowException(final boolean p0);
    
    boolean isInited();
    
    void clearProviderCache();
    
    Set<String> getProviderWhiteList();
    
    String check(final String p0) throws SQLException;
    
    long getViolationCount();
    
    void resetViolationCount();
    
    void clearWhiteList();
    
    boolean checkValid(final String p0);
}
