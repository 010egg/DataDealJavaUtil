// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.replication.fluent;

import org.postgresql.replication.LogSequenceNumber;

public interface CommonOptions
{
    String getSlotName();
    
    LogSequenceNumber getStartLSNPosition();
    
    int getStatusInterval();
}
