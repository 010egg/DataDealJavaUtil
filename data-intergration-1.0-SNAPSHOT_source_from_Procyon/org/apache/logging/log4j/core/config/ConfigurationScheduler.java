// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Logger;
import java.util.Date;
import org.apache.logging.log4j.core.util.CronExpression;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.logging.log4j.core.AbstractLifeCycle;

public class ConfigurationScheduler extends AbstractLifeCycle
{
    private static final String SIMPLE_NAME;
    private static final int MAX_SCHEDULED_ITEMS = 5;
    private ScheduledExecutorService executorService;
    private int scheduledItems;
    
    public ConfigurationScheduler() {
        this.scheduledItems = 0;
    }
    
    @Override
    public void start() {
        super.start();
        if (this.scheduledItems > 0) {
            ConfigurationScheduler.LOGGER.debug("{} starting {} threads", (Object)this.scheduledItems, ConfigurationScheduler.SIMPLE_NAME);
            this.scheduledItems = Math.min(this.scheduledItems, 5);
            this.executorService = new ScheduledThreadPoolExecutor(this.scheduledItems, Log4jThreadFactory.createDaemonThreadFactory("Scheduled"));
        }
        else {
            ConfigurationScheduler.LOGGER.debug("{}: No scheduled items", ConfigurationScheduler.SIMPLE_NAME);
        }
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        if (this.executorService != null) {
            ConfigurationScheduler.LOGGER.debug("{} shutting down threads in {}", ConfigurationScheduler.SIMPLE_NAME, this.executorService);
            this.executorService.shutdown();
        }
        this.setStopped();
        return true;
    }
    
    public void incrementScheduledItems() {
        if (!this.isStarted()) {
            ++this.scheduledItems;
        }
        else {
            ConfigurationScheduler.LOGGER.error("{} attempted to increment scheduled items after start", ConfigurationScheduler.SIMPLE_NAME);
        }
    }
    
    public void decrementScheduledItems() {
        if (!this.isStarted() && this.scheduledItems > 0) {
            --this.scheduledItems;
        }
    }
    
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        return this.executorService.schedule(callable, delay, unit);
    }
    
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        return this.executorService.schedule(command, delay, unit);
    }
    
    public CronScheduledFuture<?> scheduleWithCron(final CronExpression cronExpression, final Runnable command) {
        final Date fireDate = cronExpression.getNextValidTimeAfter(new Date());
        final CronRunnable runnable = new CronRunnable(command, cronExpression);
        final ScheduledFuture<?> future = this.schedule(runnable, this.nextFireInterval(fireDate), TimeUnit.MILLISECONDS);
        final CronScheduledFuture<?> cronScheduledFuture = new CronScheduledFuture<Object>(future, fireDate);
        runnable.setScheduledFuture(cronScheduledFuture);
        return cronScheduledFuture;
    }
    
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        return this.executorService.scheduleAtFixedRate(command, initialDelay, period, unit);
    }
    
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        return this.executorService.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
    
    public long nextFireInterval(final Date fireDate) {
        return fireDate.getTime() - new Date().getTime();
    }
    
    static {
        SIMPLE_NAME = "Log4j2 " + ConfigurationScheduler.class.getSimpleName();
    }
    
    public class CronRunnable implements Runnable
    {
        private final CronExpression cronExpression;
        private final Runnable runnable;
        private CronScheduledFuture<?> scheduledFuture;
        
        public CronRunnable(final Runnable runnable, final CronExpression cronExpression) {
            this.cronExpression = cronExpression;
            this.runnable = runnable;
        }
        
        public void setScheduledFuture(final CronScheduledFuture<?> future) {
            this.scheduledFuture = future;
        }
        
        @Override
        public void run() {
            try {
                this.runnable.run();
            }
            catch (Throwable ex) {
                ConfigurationScheduler.LOGGER.error("{} caught error running command", ConfigurationScheduler.SIMPLE_NAME, ex);
            }
            finally {
                final Date fireDate = this.cronExpression.getNextValidTimeAfter(new Date());
                final ScheduledFuture<?> future = ConfigurationScheduler.this.schedule(this, ConfigurationScheduler.this.nextFireInterval(fireDate), TimeUnit.MILLISECONDS);
                this.scheduledFuture.reset(future, fireDate);
            }
        }
    }
}
