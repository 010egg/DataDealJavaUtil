// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.selector;

import com.alibaba.druid.support.logging.LogFactory;
import java.sql.Connection;
import com.alibaba.druid.util.JdbcUtils;
import java.util.Iterator;
import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import com.alibaba.druid.support.logging.Log;

public class RandomDataSourceRecoverThread implements Runnable
{
    public static final int DEFAULT_RECOVER_INTERVAL_SECONDS = 120;
    private static final Log LOG;
    private RandomDataSourceSelector selector;
    private int recoverIntervalSeconds;
    private int validationSleepSeconds;
    
    public RandomDataSourceRecoverThread(final RandomDataSourceSelector selector) {
        this.recoverIntervalSeconds = 120;
        this.validationSleepSeconds = 0;
        this.selector = selector;
    }
    
    @Override
    public void run() {
        while (true) {
            if (this.selector != null && this.selector.getBlacklist() != null && !this.selector.getBlacklist().isEmpty()) {
                RandomDataSourceRecoverThread.LOG.info(this.selector.getBlacklist().size() + " DataSource in blacklist.");
                for (final DataSource dataSource : this.selector.getBlacklist()) {
                    if (!(dataSource instanceof DruidDataSource)) {
                        continue;
                    }
                    this.tryOneDataSource((DruidDataSource)dataSource);
                }
            }
            else if (this.selector == null) {
                break;
            }
            this.sleep();
        }
    }
    
    private void tryOneDataSource(final DruidDataSource dataSource) {
        if (dataSource == null) {
            return;
        }
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            this.sleepBeforeValidation();
            dataSource.validateConnection(connection);
            RandomDataSourceRecoverThread.LOG.info(dataSource.getName() + " is available now.");
            this.selector.removeBlacklist(dataSource);
        }
        catch (Exception e) {
            RandomDataSourceRecoverThread.LOG.warn("DataSource[" + dataSource.getName() + "] is still unavailable. Exception: " + e.getMessage());
        }
        finally {
            JdbcUtils.close(connection);
        }
    }
    
    private void sleepBeforeValidation() {
        if (this.validationSleepSeconds > 0) {
            try {
                RandomDataSourceRecoverThread.LOG.debug("Sleep " + this.validationSleepSeconds + " second(s) before validation.");
                Thread.sleep(this.validationSleepSeconds * 1000);
            }
            catch (InterruptedException ex) {}
        }
    }
    
    private void sleep() {
        try {
            Thread.sleep(this.recoverIntervalSeconds * 1000);
        }
        catch (InterruptedException ex) {}
    }
    
    public int getRecoverIntervalSeconds() {
        return this.recoverIntervalSeconds;
    }
    
    public void setRecoverIntervalSeconds(final int recoverIntervalSeconds) {
        this.recoverIntervalSeconds = recoverIntervalSeconds;
    }
    
    public int getValidationSleepSeconds() {
        return this.validationSleepSeconds;
    }
    
    public void setValidationSleepSeconds(final int validationSleepSeconds) {
        this.validationSleepSeconds = validationSleepSeconds;
    }
    
    public RandomDataSourceSelector getSelector() {
        return this.selector;
    }
    
    public void setSelector(final RandomDataSourceSelector selector) {
        this.selector = selector;
    }
    
    static {
        LOG = LogFactory.getLog(RandomDataSourceRecoverThread.class);
    }
}
