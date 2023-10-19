// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.stat;

public interface StatFilterMBean
{
    boolean isMergeSql();
    
    void setMergeSql(final boolean p0);
    
    boolean isLogSlowSql();
    
    void setLogSlowSql(final boolean p0);
    
    String mergeSql(final String p0, final String p1);
    
    long getSlowSqlMillis();
    
    void setSlowSqlMillis(final long p0);
}
