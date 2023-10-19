// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.metrics;

public interface MetricsTrackerFactory
{
    IMetricsTracker create(final String p0, final PoolStats p1);
}
