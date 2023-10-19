// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v3;

public interface TypeTransferModeRegistry
{
    boolean useBinaryForSend(final int p0);
    
    boolean useBinaryForReceive(final int p0);
}
