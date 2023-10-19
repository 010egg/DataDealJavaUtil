// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.repository.function;

public interface Function
{
    FunctionType getType();
    
    FunctionHandler findHandler();
    
    FunctionHandler findHandler(final String p0);
}
