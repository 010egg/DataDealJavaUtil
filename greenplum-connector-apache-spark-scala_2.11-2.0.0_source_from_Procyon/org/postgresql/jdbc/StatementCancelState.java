// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc;

enum StatementCancelState
{
    IDLE, 
    IN_QUERY, 
    CANCELING, 
    CANCELLED;
}
