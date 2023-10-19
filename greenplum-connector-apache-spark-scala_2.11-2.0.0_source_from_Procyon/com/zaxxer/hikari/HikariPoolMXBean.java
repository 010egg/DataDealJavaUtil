// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari;

public interface HikariPoolMXBean
{
    int getIdleConnections();
    
    int getActiveConnections();
    
    int getTotalConnections();
    
    int getThreadsAwaitingConnection();
    
    void softEvictConnections();
    
    void suspendPool();
    
    void resumePool();
}
