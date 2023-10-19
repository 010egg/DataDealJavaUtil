// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

public interface Query
{
    ParameterList createParameterList();
    
    String toString(final ParameterList p0);
    
    void close();
    
    boolean isStatementDescribed();
    
    boolean isEmpty();
}
