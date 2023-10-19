// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.selector;

import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import com.alibaba.druid.pool.ha.HighAvailableDataSource;
import com.alibaba.druid.support.logging.Log;

public class StickyRandomDataSourceSelector extends RandomDataSourceSelector
{
    private static final Log LOG;
    private ThreadLocal<StickyDataSourceHolder> holders;
    private int expireSeconds;
    
    public StickyRandomDataSourceSelector(final HighAvailableDataSource highAvailableDataSource) {
        super(highAvailableDataSource);
        this.holders = new ThreadLocal<StickyDataSourceHolder>();
        this.expireSeconds = 5;
    }
    
    @Override
    public String getName() {
        return DataSourceSelectorEnum.STICKY_RANDOM.getName();
    }
    
    @Override
    public DataSource get() {
        StickyDataSourceHolder holder = this.holders.get();
        if (holder != null && this.isAvailable(holder)) {
            StickyRandomDataSourceSelector.LOG.debug("Return the sticky DataSource " + holder.getDataSource().toString() + " directly.");
            return holder.getDataSource();
        }
        StickyRandomDataSourceSelector.LOG.debug("Return a random DataSource.");
        final DataSource dataSource = super.get();
        holder = new StickyDataSourceHolder(dataSource);
        this.holders.remove();
        this.holders.set(holder);
        return dataSource;
    }
    
    private boolean isAvailable(final StickyDataSourceHolder holder) {
        boolean flag = this.isValid(holder) && !this.isExpired(holder);
        if (flag && holder.getDataSource() instanceof DruidDataSource) {
            flag = (((DruidDataSource)holder.getDataSource()).getPoolingCount() > 0);
        }
        return flag;
    }
    
    private boolean isValid(final StickyDataSourceHolder holder) {
        final boolean flag = holder.isValid() && !this.getBlacklist().contains(holder.getDataSource());
        if (!(holder.getDataSource() instanceof DruidDataSource) || !flag) {
            return flag;
        }
        final DruidDataSource dataSource = (DruidDataSource)holder.getDataSource();
        return flag && dataSource.getActiveCount() < dataSource.getMaxActive();
    }
    
    private boolean isExpired(final StickyDataSourceHolder holder) {
        return System.currentTimeMillis() - holder.getRetrievingTime() > this.expireSeconds * 1000;
    }
    
    public int getExpireSeconds() {
        return this.expireSeconds;
    }
    
    public void setExpireSeconds(final int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }
    
    static {
        LOG = LogFactory.getLog(StickyRandomDataSourceSelector.class);
    }
}
