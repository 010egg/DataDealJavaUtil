// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.metrics.micrometer;

import java.util.concurrent.TimeUnit;
import io.micrometer.core.instrument.MeterRegistry;
import com.zaxxer.hikari.metrics.PoolStats;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import com.zaxxer.hikari.metrics.IMetricsTracker;

public class MicrometerMetricsTracker implements IMetricsTracker
{
    private static final String METRIC_CATEGORY = "pool";
    private static final String METRIC_NAME_WAIT = "hikaricp.connections.acquire";
    private static final String METRIC_NAME_USAGE = "hikaricp.connections.usage";
    private static final String METRIC_NAME_CONNECT = "hikaricp.connections.creation";
    private static final String METRIC_NAME_TIMEOUT_RATE = "hikaricp.connections.timeout";
    private static final String METRIC_NAME_TOTAL_CONNECTIONS = "hikaricp.connections";
    private static final String METRIC_NAME_IDLE_CONNECTIONS = "hikaricp.connections.idle";
    private static final String METRIC_NAME_ACTIVE_CONNECTIONS = "hikaricp.connections.active";
    private static final String METRIC_NAME_PENDING_CONNECTIONS = "hikaricp.connections.pending";
    private final Timer connectionObtainTimer;
    private final Counter connectionTimeoutCounter;
    private final Timer connectionUsage;
    private final Timer connectionCreation;
    private final Gauge totalConnectionGauge;
    private final Gauge idleConnectionGauge;
    private final Gauge activeConnectionGauge;
    private final Gauge pendingConnectionGauge;
    
    MicrometerMetricsTracker(final String poolName, final PoolStats poolStats, final MeterRegistry meterRegistry) {
        this.connectionObtainTimer = Timer.builder("hikaricp.connections.acquire").description("Connection acquire time").publishPercentiles(new double[] { 0.95 }).tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.connectionCreation = Timer.builder("hikaricp.connections.creation").description("Connection creation time").publishPercentiles(new double[] { 0.95 }).tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.connectionUsage = Timer.builder("hikaricp.connections.usage").description("Connection usage time").publishPercentiles(new double[] { 0.95 }).tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.connectionTimeoutCounter = Counter.builder("hikaricp.connections.timeout").description("Connection timeout total count").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.totalConnectionGauge = Gauge.builder("hikaricp.connections", (Object)poolStats, PoolStats::getTotalConnections).description("Total connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.idleConnectionGauge = Gauge.builder("hikaricp.connections.idle", (Object)poolStats, PoolStats::getIdleConnections).description("Idle connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.activeConnectionGauge = Gauge.builder("hikaricp.connections.active", (Object)poolStats, PoolStats::getActiveConnections).description("Active connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.pendingConnectionGauge = Gauge.builder("hikaricp.connections.pending", (Object)poolStats, PoolStats::getPendingThreads).description("Pending threads").tags(new String[] { "pool", poolName }).register(meterRegistry);
    }
    
    @Override
    public void recordConnectionAcquiredNanos(final long elapsedAcquiredNanos) {
        this.connectionObtainTimer.record(elapsedAcquiredNanos, TimeUnit.NANOSECONDS);
    }
    
    @Override
    public void recordConnectionUsageMillis(final long elapsedBorrowedMillis) {
        this.connectionUsage.record(elapsedBorrowedMillis, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void recordConnectionTimeout() {
        this.connectionTimeoutCounter.increment();
    }
    
    @Override
    public void recordConnectionCreatedMillis(final long connectionCreatedMillis) {
        this.connectionCreation.record(connectionCreatedMillis, TimeUnit.MILLISECONDS);
    }
}
