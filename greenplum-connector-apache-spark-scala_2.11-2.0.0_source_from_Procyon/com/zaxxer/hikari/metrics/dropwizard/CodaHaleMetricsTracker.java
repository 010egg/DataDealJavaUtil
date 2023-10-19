// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.metrics.dropwizard;

import java.util.concurrent.TimeUnit;
import com.codahale.metrics.Metric;
import com.codahale.metrics.Gauge;
import com.zaxxer.hikari.metrics.PoolStats;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Timer;
import com.zaxxer.hikari.metrics.IMetricsTracker;

public final class CodaHaleMetricsTracker implements IMetricsTracker
{
    private final String poolName;
    private final Timer connectionObtainTimer;
    private final Histogram connectionUsage;
    private final Histogram connectionCreation;
    private final Meter connectionTimeoutMeter;
    private final MetricRegistry registry;
    private static final String METRIC_CATEGORY = "pool";
    private static final String METRIC_NAME_WAIT = "Wait";
    private static final String METRIC_NAME_USAGE = "Usage";
    private static final String METRIC_NAME_CONNECT = "ConnectionCreation";
    private static final String METRIC_NAME_TIMEOUT_RATE = "ConnectionTimeoutRate";
    private static final String METRIC_NAME_TOTAL_CONNECTIONS = "TotalConnections";
    private static final String METRIC_NAME_IDLE_CONNECTIONS = "IdleConnections";
    private static final String METRIC_NAME_ACTIVE_CONNECTIONS = "ActiveConnections";
    private static final String METRIC_NAME_PENDING_CONNECTIONS = "PendingConnections";
    
    public CodaHaleMetricsTracker(final String poolName, final PoolStats poolStats, final MetricRegistry registry) {
        this.poolName = poolName;
        this.registry = registry;
        this.connectionObtainTimer = registry.timer(MetricRegistry.name(poolName, new String[] { "pool", "Wait" }));
        this.connectionUsage = registry.histogram(MetricRegistry.name(poolName, new String[] { "pool", "Usage" }));
        this.connectionCreation = registry.histogram(MetricRegistry.name(poolName, new String[] { "pool", "ConnectionCreation" }));
        this.connectionTimeoutMeter = registry.meter(MetricRegistry.name(poolName, new String[] { "pool", "ConnectionTimeoutRate" }));
        registry.register(MetricRegistry.name(poolName, new String[] { "pool", "TotalConnections" }), (Metric)new Gauge<Integer>() {
            public Integer getValue() {
                return poolStats.getTotalConnections();
            }
        });
        registry.register(MetricRegistry.name(poolName, new String[] { "pool", "IdleConnections" }), (Metric)new Gauge<Integer>() {
            public Integer getValue() {
                return poolStats.getIdleConnections();
            }
        });
        registry.register(MetricRegistry.name(poolName, new String[] { "pool", "ActiveConnections" }), (Metric)new Gauge<Integer>() {
            public Integer getValue() {
                return poolStats.getActiveConnections();
            }
        });
        registry.register(MetricRegistry.name(poolName, new String[] { "pool", "PendingConnections" }), (Metric)new Gauge<Integer>() {
            public Integer getValue() {
                return poolStats.getPendingThreads();
            }
        });
    }
    
    @Override
    public void close() {
        this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "Wait" }));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "Usage" }));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "ConnectionCreation" }));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "ConnectionTimeoutRate" }));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "TotalConnections" }));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "IdleConnections" }));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "ActiveConnections" }));
        this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "PendingConnections" }));
    }
    
    @Override
    public void recordConnectionAcquiredNanos(final long elapsedAcquiredNanos) {
        this.connectionObtainTimer.update(elapsedAcquiredNanos, TimeUnit.NANOSECONDS);
    }
    
    @Override
    public void recordConnectionUsageMillis(final long elapsedBorrowedMillis) {
        this.connectionUsage.update(elapsedBorrowedMillis);
    }
    
    @Override
    public void recordConnectionTimeout() {
        this.connectionTimeoutMeter.mark();
    }
    
    @Override
    public void recordConnectionCreatedMillis(final long connectionCreatedMillis) {
        this.connectionCreation.update(connectionCreatedMillis);
    }
    
    public Timer getConnectionAcquisitionTimer() {
        return this.connectionObtainTimer;
    }
    
    public Histogram getConnectionDurationHistogram() {
        return this.connectionUsage;
    }
    
    public Histogram getConnectionCreationHistogram() {
        return this.connectionCreation;
    }
}
