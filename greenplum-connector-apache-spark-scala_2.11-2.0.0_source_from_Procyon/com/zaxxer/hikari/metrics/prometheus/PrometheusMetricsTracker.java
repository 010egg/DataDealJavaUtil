// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.metrics.prometheus;

import java.util.concurrent.TimeUnit;
import io.prometheus.client.Summary;
import io.prometheus.client.Counter;
import com.zaxxer.hikari.metrics.IMetricsTracker;

class PrometheusMetricsTracker implements IMetricsTracker
{
    private static final Counter CONNECTION_TIMEOUT_COUNTER;
    private static final Summary ELAPSED_ACQUIRED_SUMMARY;
    private static final Summary ELAPSED_BORROWED_SUMMARY;
    private static final Summary ELAPSED_CREATION_SUMMARY;
    private final Counter.Child connectionTimeoutCounterChild;
    private final Summary.Child elapsedAcquiredSummaryChild;
    private final Summary.Child elapsedBorrowedSummaryChild;
    private final Summary.Child elapsedCreationSummaryChild;
    
    private static Summary registerSummary(final String name, final String help) {
        return (Summary)((Summary.Builder)((Summary.Builder)((Summary.Builder)Summary.build().name(name)).labelNames(new String[] { "pool" })).help(help)).quantile(0.5, 0.05).quantile(0.95, 0.01).quantile(0.99, 0.001).maxAgeSeconds(TimeUnit.MINUTES.toSeconds(5L)).ageBuckets(5).register();
    }
    
    PrometheusMetricsTracker(final String poolName) {
        this.connectionTimeoutCounterChild = (Counter.Child)PrometheusMetricsTracker.CONNECTION_TIMEOUT_COUNTER.labels(new String[] { poolName });
        this.elapsedAcquiredSummaryChild = (Summary.Child)PrometheusMetricsTracker.ELAPSED_ACQUIRED_SUMMARY.labels(new String[] { poolName });
        this.elapsedBorrowedSummaryChild = (Summary.Child)PrometheusMetricsTracker.ELAPSED_BORROWED_SUMMARY.labels(new String[] { poolName });
        this.elapsedCreationSummaryChild = (Summary.Child)PrometheusMetricsTracker.ELAPSED_CREATION_SUMMARY.labels(new String[] { poolName });
    }
    
    @Override
    public void recordConnectionAcquiredNanos(final long elapsedAcquiredNanos) {
        this.elapsedAcquiredSummaryChild.observe((double)elapsedAcquiredNanos);
    }
    
    @Override
    public void recordConnectionUsageMillis(final long elapsedBorrowedMillis) {
        this.elapsedBorrowedSummaryChild.observe((double)elapsedBorrowedMillis);
    }
    
    @Override
    public void recordConnectionCreatedMillis(final long connectionCreatedMillis) {
        this.elapsedCreationSummaryChild.observe((double)connectionCreatedMillis);
    }
    
    @Override
    public void recordConnectionTimeout() {
        this.connectionTimeoutCounterChild.inc();
    }
    
    static {
        CONNECTION_TIMEOUT_COUNTER = (Counter)((Counter.Builder)((Counter.Builder)((Counter.Builder)Counter.build().name("hikaricp_connection_timeout_total")).labelNames(new String[] { "pool" })).help("Connection timeout total count")).register();
        ELAPSED_ACQUIRED_SUMMARY = registerSummary("hikaricp_connection_acquired_nanos", "Connection acquired time (ns)");
        ELAPSED_BORROWED_SUMMARY = registerSummary("hikaricp_connection_usage_millis", "Connection usage (ms)");
        ELAPSED_CREATION_SUMMARY = registerSummary("hikaricp_connection_creation_millis", "Connection creation (ms)");
    }
}
