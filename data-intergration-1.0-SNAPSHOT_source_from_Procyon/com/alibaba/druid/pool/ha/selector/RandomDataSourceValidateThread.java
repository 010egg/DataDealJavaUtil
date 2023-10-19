// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.selector;

import com.alibaba.druid.support.logging.LogFactory;
import java.sql.Connection;
import java.sql.Driver;
import com.alibaba.druid.util.JdbcUtils;
import java.util.Properties;
import java.util.List;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.Set;
import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import java.util.concurrent.ExecutorService;
import java.util.Map;
import com.alibaba.druid.support.logging.Log;

public class RandomDataSourceValidateThread implements Runnable
{
    public static final int DEFAULT_CHECKING_INTERVAL_SECONDS = 10;
    public static final int DEFAULT_BLACKLIST_THRESHOLD = 3;
    private static final Log LOG;
    private static Map<String, Long> successTimes;
    private int checkingIntervalSeconds;
    private int validationSleepSeconds;
    private int blacklistThreshold;
    private RandomDataSourceSelector selector;
    private ExecutorService checkExecutor;
    private Map<String, Integer> errorCounts;
    private Map<String, Long> lastCheckTimes;
    
    public static void logSuccessTime(final DataSourceProxy dataSource) {
        if (dataSource != null && !StringUtils.isEmpty(dataSource.getName())) {
            final String name = dataSource.getName();
            final long time = System.currentTimeMillis();
            RandomDataSourceValidateThread.LOG.debug("Log successTime [" + time + "] for " + name);
            RandomDataSourceValidateThread.successTimes.put(name, time);
        }
    }
    
    public RandomDataSourceValidateThread(final RandomDataSourceSelector selector) {
        this.checkingIntervalSeconds = 10;
        this.validationSleepSeconds = 0;
        this.blacklistThreshold = 3;
        this.checkExecutor = Executors.newFixedThreadPool(5);
        this.errorCounts = new ConcurrentHashMap<String, Integer>();
        this.lastCheckTimes = new ConcurrentHashMap<String, Long>();
        RandomDataSourceValidateThread.LOG.debug("Create a RandomDataSourceValidateThread, hashCode=" + this.hashCode());
        this.selector = selector;
    }
    
    @Override
    public void run() {
        while (this.selector != null) {
            this.checkAllDataSources();
            this.maintainBlacklist();
            this.cleanup();
            this.sleepForNextValidation();
        }
    }
    
    private void sleepForNextValidation() {
        int errorCountBelowThreshold = 0;
        for (final int count : this.errorCounts.values()) {
            if (count > 0 && count < this.blacklistThreshold && count > errorCountBelowThreshold) {
                errorCountBelowThreshold = count;
            }
        }
        int newSleepSeconds = this.checkingIntervalSeconds / (errorCountBelowThreshold + 1);
        if (newSleepSeconds < 1) {
            newSleepSeconds = 1;
        }
        try {
            RandomDataSourceValidateThread.LOG.debug("[RandomDataSourceValidateThread@" + this.hashCode() + "] Sleep " + newSleepSeconds + " second(s) until next checking.");
            Thread.sleep(newSleepSeconds * 1000);
        }
        catch (InterruptedException ex) {}
    }
    
    private void cleanup() {
        final Map<String, DataSource> dataSourceMap = this.selector.getFullDataSourceMap();
        final Set<String> dataSources = new HashSet<String>();
        for (final DataSource ds : dataSourceMap.values()) {
            if (ds instanceof DruidDataSource) {
                dataSources.add(((DruidDataSource)ds).getName());
            }
        }
        this.cleanupMap(RandomDataSourceValidateThread.successTimes, dataSources);
        this.cleanupMap(this.errorCounts, dataSources);
        this.cleanupMap(this.lastCheckTimes, dataSources);
    }
    
    private void cleanupMap(final Map<String, ?> mapToClean, final Set<String> keys) {
        final Set<String> keysToRemove = new HashSet<String>();
        for (final String k : mapToClean.keySet()) {
            if (!keys.contains(k)) {
                keysToRemove.add(k);
            }
        }
        for (final String k : keysToRemove) {
            mapToClean.remove(k);
        }
    }
    
    private void maintainBlacklist() {
        final Map<String, DataSource> dataSourceMap = this.selector.getFullDataSourceMap();
        for (final String name : this.errorCounts.keySet()) {
            final Integer count = this.errorCounts.get(name);
            DataSource dataSource = dataSourceMap.get(name);
            if (dataSource == null) {
                for (final DataSource ds : dataSourceMap.values()) {
                    if (ds instanceof DruidDataSource && name.equals(((DruidDataSource)ds).getName())) {
                        dataSource = ds;
                    }
                }
            }
            if (count == null || count <= 0) {
                this.selector.removeBlacklist(dataSource);
            }
            else {
                if (count == null || count < this.blacklistThreshold || this.selector.containInBlacklist(dataSource)) {
                    continue;
                }
                RandomDataSourceValidateThread.LOG.warn("Adding " + name + " to blacklist.");
                this.selector.addBlacklist(dataSource);
            }
        }
    }
    
    private void checkAllDataSources() {
        final Map<String, DataSource> dataSourceMap = this.selector.getFullDataSourceMap();
        final List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
        RandomDataSourceValidateThread.LOG.debug("Checking all DataSource(s).");
        for (final Map.Entry<String, DataSource> e : dataSourceMap.entrySet()) {
            if (!(e.getValue() instanceof DruidDataSource)) {
                continue;
            }
            if (this.selector.containInBlacklist(e.getValue())) {
                RandomDataSourceValidateThread.LOG.debug(e.getKey() + " is already in blacklist, skip.");
            }
            else {
                tasks.add(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        final DruidDataSource dataSource = e.getValue();
                        final String name = dataSource.getName();
                        if (RandomDataSourceValidateThread.this.isSkipChecking(dataSource)) {
                            RandomDataSourceValidateThread.LOG.debug("Skip checking DataSource[" + name + "] this time.");
                            return true;
                        }
                        RandomDataSourceValidateThread.LOG.debug("Start checking " + name + ".");
                        final boolean flag = RandomDataSourceValidateThread.this.check(dataSource);
                        if (flag) {
                            RandomDataSourceValidateThread.logSuccessTime(dataSource);
                            RandomDataSourceValidateThread.this.errorCounts.put(name, 0);
                        }
                        else {
                            if (!RandomDataSourceValidateThread.this.errorCounts.containsKey(name)) {
                                RandomDataSourceValidateThread.this.errorCounts.put(name, 0);
                            }
                            final int count = RandomDataSourceValidateThread.this.errorCounts.get(name);
                            RandomDataSourceValidateThread.this.errorCounts.put(name, count + 1);
                        }
                        RandomDataSourceValidateThread.this.lastCheckTimes.put(name, System.currentTimeMillis());
                        return flag;
                    }
                });
            }
        }
        try {
            this.checkExecutor.invokeAll((Collection<? extends Callable<Object>>)tasks);
        }
        catch (Exception e2) {
            RandomDataSourceValidateThread.LOG.warn("Exception occurred while checking DataSource.", e2);
        }
    }
    
    private boolean isSkipChecking(final DruidDataSource dataSource) {
        final String name = dataSource.getName();
        final Long lastSuccessTime = RandomDataSourceValidateThread.successTimes.get(dataSource.getName());
        final Long lastCheckTime = this.lastCheckTimes.get(dataSource.getName());
        final long currentTime = System.currentTimeMillis();
        RandomDataSourceValidateThread.LOG.debug("DataSource=" + name + ", lastSuccessTime=" + lastSuccessTime + ", lastCheckTime=" + lastCheckTime + ", currentTime=" + currentTime);
        return lastSuccessTime != null && lastCheckTime != null && currentTime - lastSuccessTime <= this.checkingIntervalSeconds * 1000 && currentTime - lastCheckTime <= 5 * this.checkingIntervalSeconds * 1000 && (!this.errorCounts.containsKey(name) || this.errorCounts.get(name) < 1);
    }
    
    private boolean check(final DruidDataSource dataSource) {
        boolean result = true;
        final String name = dataSource.getName();
        final Driver driver = dataSource.getRawDriver();
        final Properties info = new Properties(dataSource.getConnectProperties());
        final String username = dataSource.getUsername();
        final String password = dataSource.getPassword();
        final String url = dataSource.getUrl();
        Connection conn = null;
        if (info.getProperty("user") == null && username != null) {
            info.setProperty("user", username);
        }
        if (info.getProperty("password") == null && password != null) {
            info.setProperty("password", password);
        }
        try {
            RandomDataSourceValidateThread.LOG.debug("[RandomDataSourceValidateThread@" + this.hashCode() + "] Validating " + name + " every " + this.checkingIntervalSeconds + " seconds.");
            conn = driver.connect(url, info);
            this.sleepBeforeValidation();
            dataSource.validateConnection(conn);
        }
        catch (Exception e) {
            RandomDataSourceValidateThread.LOG.warn("Validation FAILED for " + name + " with url [" + url + "] and username [" + info.getProperty("user") + "]. Exception: " + e.getMessage());
            result = false;
        }
        finally {
            JdbcUtils.close(conn);
        }
        return result;
    }
    
    private void sleepBeforeValidation() {
        if (this.validationSleepSeconds <= 0) {
            return;
        }
        try {
            RandomDataSourceValidateThread.LOG.debug("Sleep " + this.validationSleepSeconds + " second(s) before validation.");
            Thread.sleep(this.validationSleepSeconds * 1000L);
        }
        catch (InterruptedException ex) {}
    }
    
    public int getCheckingIntervalSeconds() {
        return this.checkingIntervalSeconds;
    }
    
    public void setCheckingIntervalSeconds(final int checkingIntervalSeconds) {
        this.checkingIntervalSeconds = checkingIntervalSeconds;
    }
    
    public int getValidationSleepSeconds() {
        return this.validationSleepSeconds;
    }
    
    public void setValidationSleepSeconds(final int validationSleepSeconds) {
        this.validationSleepSeconds = validationSleepSeconds;
    }
    
    public int getBlacklistThreshold() {
        return this.blacklistThreshold;
    }
    
    public void setBlacklistThreshold(final int blacklistThreshold) {
        this.blacklistThreshold = blacklistThreshold;
    }
    
    public RandomDataSourceSelector getSelector() {
        return this.selector;
    }
    
    public void setSelector(final RandomDataSourceSelector selector) {
        this.selector = selector;
    }
    
    static {
        LOG = LogFactory.getLog(RandomDataSourceValidateThread.class);
        RandomDataSourceValidateThread.successTimes = new ConcurrentHashMap<String, Long>();
    }
}
