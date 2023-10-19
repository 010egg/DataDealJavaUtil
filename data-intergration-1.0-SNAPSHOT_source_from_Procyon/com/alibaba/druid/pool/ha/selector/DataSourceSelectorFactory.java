// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.selector;

import com.alibaba.druid.pool.ha.HighAvailableDataSource;

public class DataSourceSelectorFactory
{
    public static DataSourceSelector getSelector(final String name, final HighAvailableDataSource highAvailableDataSource) {
        for (final DataSourceSelectorEnum e : DataSourceSelectorEnum.values()) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e.newInstance(highAvailableDataSource);
            }
        }
        return null;
    }
}
