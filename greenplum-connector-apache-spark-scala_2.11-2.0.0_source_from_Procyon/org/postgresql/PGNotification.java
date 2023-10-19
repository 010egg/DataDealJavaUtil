// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql;

public interface PGNotification
{
    String getName();
    
    int getPID();
    
    String getParameter();
}
