// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

@Deprecated
public class JdbcTraceManager implements JdbcTraceManagerMBean
{
    private static final JdbcTraceManager instance;
    
    public static JdbcTraceManager getInstance() {
        return JdbcTraceManager.instance;
    }
    
    static {
        instance = new JdbcTraceManager();
    }
}
