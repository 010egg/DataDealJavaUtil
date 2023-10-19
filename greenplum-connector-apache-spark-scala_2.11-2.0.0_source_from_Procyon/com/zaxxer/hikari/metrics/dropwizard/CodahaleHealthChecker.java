// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.metrics.dropwizard;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.SortedMap;
import java.util.Properties;
import java.util.Map;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;

public final class CodahaleHealthChecker
{
    public static void registerHealthChecks(final HikariPool pool, final HikariConfig hikariConfig, final HealthCheckRegistry registry) {
        final Properties healthCheckProperties = hikariConfig.getHealthCheckProperties();
        final MetricRegistry metricRegistry = (MetricRegistry)hikariConfig.getMetricRegistry();
        final long checkTimeoutMs = Long.parseLong(healthCheckProperties.getProperty("connectivityCheckTimeoutMs", String.valueOf(hikariConfig.getConnectionTimeout())));
        registry.register(MetricRegistry.name(hikariConfig.getPoolName(), new String[] { "pool", "ConnectivityCheck" }), (HealthCheck)new ConnectivityHealthCheck(pool, checkTimeoutMs));
        final long expected99thPercentile = Long.parseLong(healthCheckProperties.getProperty("expected99thPercentileMs", "0"));
        if (metricRegistry != null && expected99thPercentile > 0L) {
            final SortedMap<String, Timer> timers = (SortedMap<String, Timer>)metricRegistry.getTimers((MetricFilter)new MetricFilter() {
                public boolean matches(final String name, final Metric metric) {
                    return name.equals(MetricRegistry.name(hikariConfig.getPoolName(), new String[] { "pool", "Wait" }));
                }
            });
            if (!timers.isEmpty()) {
                final Timer timer = timers.entrySet().iterator().next().getValue();
                registry.register(MetricRegistry.name(hikariConfig.getPoolName(), new String[] { "pool", "Connection99Percent" }), (HealthCheck)new Connection99Percent(timer, expected99thPercentile));
            }
        }
    }
    
    private CodahaleHealthChecker() {
    }
    
    private static class ConnectivityHealthCheck extends HealthCheck
    {
        private final HikariPool pool;
        private final long checkTimeoutMs;
        
        ConnectivityHealthCheck(final HikariPool pool, final long checkTimeoutMs) {
            this.pool = pool;
            this.checkTimeoutMs = ((checkTimeoutMs > 0L && checkTimeoutMs != 2147483647L) ? checkTimeoutMs : TimeUnit.SECONDS.toMillis(10L));
        }
        
        protected HealthCheck.Result check() throws Exception {
            try (final Connection connection = this.pool.getConnection(this.checkTimeoutMs)) {
                return HealthCheck.Result.healthy();
            }
            catch (SQLException e) {
                return HealthCheck.Result.unhealthy((Throwable)e);
            }
        }
    }
    
    private static class Connection99Percent extends HealthCheck
    {
        private final Timer waitTimer;
        private final long expected99thPercentile;
        
        Connection99Percent(final Timer waitTimer, final long expected99thPercentile) {
            this.waitTimer = waitTimer;
            this.expected99thPercentile = expected99thPercentile;
        }
        
        protected HealthCheck.Result check() throws Exception {
            final long the99thPercentile = TimeUnit.NANOSECONDS.toMillis(Math.round(this.waitTimer.getSnapshot().get99thPercentile()));
            return (the99thPercentile <= this.expected99thPercentile) ? HealthCheck.Result.healthy() : HealthCheck.Result.unhealthy("99th percentile connection wait time of %dms exceeds the threshold %dms", new Object[] { the99thPercentile, this.expected99thPercentile });
        }
    }
}
