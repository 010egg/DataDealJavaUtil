// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.selector;

import java.util.Iterator;
import java.util.Map;
import javax.sql.DataSource;
import com.alibaba.druid.pool.ha.HighAvailableDataSource;

public class NamedDataSourceSelector implements DataSourceSelector
{
    public static final String DEFAULT_NAME = "default";
    private HighAvailableDataSource highAvailableDataSource;
    private ThreadLocal<String> targetDataSourceName;
    private String defaultName;
    
    public NamedDataSourceSelector(final HighAvailableDataSource highAvailableDataSource) {
        this.targetDataSourceName = new ThreadLocal<String>();
        this.defaultName = "default";
        this.highAvailableDataSource = highAvailableDataSource;
    }
    
    @Override
    public void init() {
    }
    
    @Override
    public void destroy() {
    }
    
    @Override
    public String getName() {
        return DataSourceSelectorEnum.BY_NAME.getName();
    }
    
    @Override
    public DataSource get() {
        if (this.highAvailableDataSource == null) {
            return null;
        }
        final Map<String, DataSource> dataSourceMap = this.highAvailableDataSource.getAvailableDataSourceMap();
        if (dataSourceMap == null || dataSourceMap.isEmpty()) {
            return null;
        }
        if (dataSourceMap.size() == 1) {
            final Iterator<DataSource> iterator = dataSourceMap.values().iterator();
            if (iterator.hasNext()) {
                final DataSource v = iterator.next();
                return v;
            }
        }
        final String name = this.getTarget();
        if (name != null) {
            return dataSourceMap.get(name);
        }
        if (dataSourceMap.get(this.getDefaultName()) != null) {
            return dataSourceMap.get(this.getDefaultName());
        }
        return null;
    }
    
    @Override
    public void setTarget(final String name) {
        this.targetDataSourceName.set(name);
    }
    
    public String getTarget() {
        return this.targetDataSourceName.get();
    }
    
    public void resetDataSourceName() {
        this.targetDataSourceName.remove();
    }
    
    public String getDefaultName() {
        return this.defaultName;
    }
    
    public void setDefaultName(final String defaultName) {
        this.defaultName = defaultName;
    }
}
