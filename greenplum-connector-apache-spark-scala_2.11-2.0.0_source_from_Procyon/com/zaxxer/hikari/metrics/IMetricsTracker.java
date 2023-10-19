// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.metrics;

public interface IMetricsTracker extends AutoCloseable
{
    default void recordConnectionCreatedMillis(final long connectionCreatedMillis) {
    }
    
    default void recordConnectionAcquiredNanos(final long elapsedAcquiredNanos) {
    }
    
    default void recordConnectionUsageMillis(final long elapsedBorrowedMillis) {
    }
    
    default void recordConnectionTimeout() {
    }
    
    default void close() {
    }
}
