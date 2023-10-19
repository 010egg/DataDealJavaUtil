// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.selector;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Properties;
import com.alibaba.druid.pool.DruidDataSource;
import java.util.HashMap;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import com.alibaba.druid.pool.ha.HighAvailableDataSource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Random;
import com.alibaba.druid.support.logging.Log;

public class RandomDataSourceSelector implements DataSourceSelector
{
    private static final String PROP_PREFIX = "druid.ha.random.";
    public static final String PROP_CHECKING_INTERVAL = "druid.ha.random.checkingIntervalSeconds";
    public static final String PROP_RECOVERY_INTERVAL = "druid.ha.random.recoveryIntervalSeconds";
    public static final String PROP_VALIDATION_SLEEP = "druid.ha.random.validationSleepSeconds";
    public static final String PROP_BLACKLIST_THRESHOLD = "druid.ha.random.blacklistThreshold";
    private static final Log LOG;
    private Random random;
    private List<DataSource> blacklist;
    private HighAvailableDataSource highAvailableDataSource;
    private RandomDataSourceValidateThread validateThread;
    private RandomDataSourceRecoverThread recoverThread;
    private Thread runningValidateThread;
    private Thread runningRecoverThread;
    private int checkingIntervalSeconds;
    private int recoveryIntervalSeconds;
    private int validationSleepSeconds;
    private int blacklistThreshold;
    
    public RandomDataSourceSelector(final HighAvailableDataSource highAvailableDataSource) {
        this.random = new Random();
        this.blacklist = new CopyOnWriteArrayList<DataSource>();
        this.checkingIntervalSeconds = 10;
        this.recoveryIntervalSeconds = 120;
        this.validationSleepSeconds = 0;
        this.blacklistThreshold = 3;
        this.highAvailableDataSource = highAvailableDataSource;
    }
    
    @Override
    public void init() {
        if (this.highAvailableDataSource == null) {
            RandomDataSourceSelector.LOG.warn("highAvailableDataSource is NULL!");
            return;
        }
        if (!this.highAvailableDataSource.isTestOnBorrow() && !this.highAvailableDataSource.isTestOnReturn()) {
            this.loadProperties();
            this.initThreads();
        }
        else {
            RandomDataSourceSelector.LOG.info("testOnBorrow or testOnReturn has been set to true, ignore validateThread");
        }
    }
    
    @Override
    public void destroy() {
        if (this.runningValidateThread != null) {
            this.runningValidateThread.interrupt();
            this.validateThread.setSelector(null);
        }
        if (this.runningRecoverThread != null) {
            this.runningRecoverThread.interrupt();
            this.recoverThread.setSelector(null);
        }
    }
    
    @Override
    public String getName() {
        return DataSourceSelectorEnum.RANDOM.getName();
    }
    
    @Override
    public DataSource get() {
        final Map<String, DataSource> dataSourceMap = this.getDataSourceMap();
        if (dataSourceMap == null || dataSourceMap.isEmpty()) {
            return null;
        }
        final Collection<DataSource> targetDataSourceSet = this.removeBlackList(dataSourceMap);
        this.removeBusyDataSource(targetDataSourceSet);
        final DataSource dataSource = this.getRandomDataSource(targetDataSourceSet);
        return dataSource;
    }
    
    @Override
    public void setTarget(final String name) {
    }
    
    public Map<String, DataSource> getFullDataSourceMap() {
        if (this.highAvailableDataSource != null) {
            return this.highAvailableDataSource.getDataSourceMap();
        }
        return new HashMap<String, DataSource>();
    }
    
    public Map<String, DataSource> getDataSourceMap() {
        if (this.highAvailableDataSource != null) {
            return this.highAvailableDataSource.getAvailableDataSourceMap();
        }
        return new HashMap<String, DataSource>();
    }
    
    public List<DataSource> getBlacklist() {
        return this.blacklist;
    }
    
    public boolean containInBlacklist(final DataSource dataSource) {
        return dataSource != null && this.blacklist.contains(dataSource);
    }
    
    public void addBlacklist(final DataSource dataSource) {
        if (dataSource != null && !this.blacklist.contains(dataSource)) {
            this.blacklist.add(dataSource);
            if (dataSource instanceof DruidDataSource) {
                ((DruidDataSource)dataSource).setTestOnReturn(true);
            }
        }
    }
    
    public void removeBlacklist(final DataSource dataSource) {
        if (this.containInBlacklist(dataSource)) {
            this.blacklist.remove(dataSource);
            if (dataSource instanceof DruidDataSource) {
                ((DruidDataSource)dataSource).setTestOnReturn(this.highAvailableDataSource.isTestOnReturn());
            }
        }
    }
    
    private void loadProperties() {
        this.checkingIntervalSeconds = this.loadInteger("druid.ha.random.checkingIntervalSeconds", this.checkingIntervalSeconds);
        this.recoveryIntervalSeconds = this.loadInteger("druid.ha.random.recoveryIntervalSeconds", this.recoveryIntervalSeconds);
        this.validationSleepSeconds = this.loadInteger("druid.ha.random.validationSleepSeconds", this.validationSleepSeconds);
        this.blacklistThreshold = this.loadInteger("druid.ha.random.blacklistThreshold", this.blacklistThreshold);
    }
    
    private int loadInteger(final String name, final int defaultValue) {
        if (name == null) {
            return defaultValue;
        }
        final Properties properties = this.highAvailableDataSource.getConnectProperties();
        int value = defaultValue;
        try {
            if (properties.containsKey(name)) {
                value = Integer.parseInt(properties.getProperty(name));
            }
        }
        catch (Exception e) {
            RandomDataSourceSelector.LOG.error("Exception occurred while parsing " + name, e);
        }
        return value;
    }
    
    private void initThreads() {
        if (this.validateThread == null) {
            (this.validateThread = new RandomDataSourceValidateThread(this)).setCheckingIntervalSeconds(this.checkingIntervalSeconds);
            this.validateThread.setValidationSleepSeconds(this.validationSleepSeconds);
            this.validateThread.setBlacklistThreshold(this.blacklistThreshold);
        }
        else {
            this.validateThread.setSelector(this);
        }
        if (this.runningValidateThread != null) {
            this.runningValidateThread.interrupt();
        }
        (this.runningValidateThread = new Thread(this.validateThread, "RandomDataSourceSelector-validate-thread")).start();
        if (this.recoverThread == null) {
            (this.recoverThread = new RandomDataSourceRecoverThread(this)).setRecoverIntervalSeconds(this.recoveryIntervalSeconds);
            this.recoverThread.setValidationSleepSeconds(this.validationSleepSeconds);
        }
        else {
            this.recoverThread.setSelector(this);
        }
        if (this.runningRecoverThread != null) {
            this.runningRecoverThread.interrupt();
        }
        (this.runningRecoverThread = new Thread(this.recoverThread, "RandomDataSourceSelector-recover-thread")).start();
    }
    
    private Collection<DataSource> removeBlackList(final Map<String, DataSource> dataSourceMap) {
        Collection<DataSource> dataSourceSet;
        if (this.blacklist == null || this.blacklist.isEmpty() || this.blacklist.size() >= dataSourceMap.size()) {
            dataSourceSet = dataSourceMap.values();
        }
        else {
            dataSourceSet = new HashSet<DataSource>(dataSourceMap.values());
            for (final DataSource b : this.blacklist) {
                dataSourceSet.remove(b);
            }
            RandomDataSourceSelector.LOG.info(this.blacklist.size() + " Blacklist DataSource removed, return " + dataSourceSet.size() + " DataSource(s).");
        }
        return dataSourceSet;
    }
    
    private void removeBusyDataSource(final Collection<DataSource> dataSourceSet) {
        final Collection<DataSource> busyDataSourceSet = new HashSet<DataSource>();
        for (final DataSource ds : dataSourceSet) {
            if (ds instanceof DruidDataSource && ((DruidDataSource)ds).getPoolingCount() <= 0) {
                busyDataSourceSet.add(ds);
            }
        }
        if (!busyDataSourceSet.isEmpty() && busyDataSourceSet.size() < dataSourceSet.size()) {
            RandomDataSourceSelector.LOG.info("Busy DataSouces: " + busyDataSourceSet.size() + "/" + dataSourceSet.size());
            for (final DataSource ds : busyDataSourceSet) {
                dataSourceSet.remove(ds);
            }
        }
    }
    
    private DataSource getRandomDataSource(final Collection<DataSource> dataSourceSet) {
        final DataSource[] dataSources = dataSourceSet.toArray(new DataSource[0]);
        if (dataSources != null && dataSources.length > 0) {
            return dataSources[this.random.nextInt(dataSourceSet.size())];
        }
        return null;
    }
    
    public HighAvailableDataSource getHighAvailableDataSource() {
        return this.highAvailableDataSource;
    }
    
    public RandomDataSourceValidateThread getValidateThread() {
        return this.validateThread;
    }
    
    public void setValidateThread(final RandomDataSourceValidateThread validateThread) {
        this.validateThread = validateThread;
    }
    
    public RandomDataSourceRecoverThread getRecoverThread() {
        return this.recoverThread;
    }
    
    public void setRecoverThread(final RandomDataSourceRecoverThread recoverThread) {
        this.recoverThread = recoverThread;
    }
    
    public int getCheckingIntervalSeconds() {
        return this.checkingIntervalSeconds;
    }
    
    public void setCheckingIntervalSeconds(final int checkingIntervalSeconds) {
        this.checkingIntervalSeconds = checkingIntervalSeconds;
    }
    
    public int getRecoveryIntervalSeconds() {
        return this.recoveryIntervalSeconds;
    }
    
    public void setRecoveryIntervalSeconds(final int recoveryIntervalSeconds) {
        this.recoveryIntervalSeconds = recoveryIntervalSeconds;
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
    
    static {
        LOG = LogFactory.getLog(RandomDataSourceSelector.class);
    }
}
