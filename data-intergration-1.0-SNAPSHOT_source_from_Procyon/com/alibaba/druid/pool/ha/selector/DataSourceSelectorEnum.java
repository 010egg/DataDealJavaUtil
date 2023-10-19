// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.selector;

import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.pool.ha.HighAvailableDataSource;
import com.alibaba.druid.support.logging.Log;

public enum DataSourceSelectorEnum
{
    BY_NAME("byName", (Class<? extends DataSourceSelector>)NamedDataSourceSelector.class), 
    RANDOM("random", (Class<? extends DataSourceSelector>)RandomDataSourceSelector.class), 
    STICKY_RANDOM("stickyRandom", (Class<? extends DataSourceSelector>)StickyRandomDataSourceSelector.class);
    
    private static final Log LOG;
    private String name;
    private Class<? extends DataSourceSelector> clazz;
    
    private DataSourceSelectorEnum(final String name, final Class<? extends DataSourceSelector> clazz) {
        this.name = name;
        this.clazz = clazz;
    }
    
    public DataSourceSelector newInstance(final HighAvailableDataSource dataSource) {
        if (dataSource == null) {
            DataSourceSelectorEnum.LOG.warn("You should provide an instance of HighAvailableDataSource!");
            return null;
        }
        DataSourceSelector selector = null;
        try {
            selector = (DataSourceSelector)this.clazz.getDeclaredConstructor(HighAvailableDataSource.class).newInstance(dataSource);
        }
        catch (Exception e) {
            DataSourceSelectorEnum.LOG.error("Can not create new instance of " + this.clazz.getName(), e);
        }
        return selector;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Class<? extends DataSourceSelector> getClazz() {
        return this.clazz;
    }
    
    static {
        LOG = LogFactory.getLog(DataSourceSelectorEnum.class);
    }
}
