// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.metrics.prometheus;

import java.util.Collections;
import io.prometheus.client.GaugeMetricFamily;
import java.util.function.Function;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import com.zaxxer.hikari.metrics.PoolStats;
import java.util.Map;
import java.util.List;
import io.prometheus.client.Collector;

class HikariCPCollector extends Collector
{
    private static final List<String> LABEL_NAMES;
    private final Map<String, PoolStats> poolStatsMap;
    
    HikariCPCollector() {
        this.poolStatsMap = new ConcurrentHashMap<String, PoolStats>();
    }
    
    public List<Collector.MetricFamilySamples> collect() {
        return Arrays.asList((Collector.MetricFamilySamples)this.createGauge("hikaricp_active_connections", "Active connections", PoolStats::getActiveConnections), (Collector.MetricFamilySamples)this.createGauge("hikaricp_idle_connections", "Idle connections", PoolStats::getIdleConnections), (Collector.MetricFamilySamples)this.createGauge("hikaricp_pending_threads", "Pending threads", PoolStats::getPendingThreads), (Collector.MetricFamilySamples)this.createGauge("hikaricp_connections", "The number of current connections", PoolStats::getTotalConnections));
    }
    
    protected HikariCPCollector add(final String name, final PoolStats poolStats) {
        this.poolStatsMap.put(name, poolStats);
        return this;
    }
    
    private GaugeMetricFamily createGauge(final String metric, final String help, final Function<PoolStats, Integer> metricValueFunction) {
        final GaugeMetricFamily metricFamily = new GaugeMetricFamily(metric, help, (List)HikariCPCollector.LABEL_NAMES);
        this.poolStatsMap.forEach((k, v) -> metricFamily.addMetric((List)Collections.singletonList(k), (double)metricValueFunction.apply(v)));
        return metricFamily;
    }
    
    static {
        LABEL_NAMES = Collections.singletonList("pool");
    }
}
