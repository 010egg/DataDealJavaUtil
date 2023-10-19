// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.metrics.prometheus;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.PoolStats;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;

public class PrometheusMetricsTrackerFactory implements MetricsTrackerFactory
{
    private static HikariCPCollector collector;
    
    @Override
    public IMetricsTracker create(final String poolName, final PoolStats poolStats) {
        this.getCollector().add(poolName, poolStats);
        return new PrometheusMetricsTracker(poolName);
    }
    
    private HikariCPCollector getCollector() {
        if (PrometheusMetricsTrackerFactory.collector == null) {
            PrometheusMetricsTrackerFactory.collector = (HikariCPCollector)new HikariCPCollector().register();
        }
        return PrometheusMetricsTrackerFactory.collector;
    }
}
