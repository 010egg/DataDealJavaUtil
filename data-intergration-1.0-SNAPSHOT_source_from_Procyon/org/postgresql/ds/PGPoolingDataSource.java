// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.ds;

import javax.sql.DataSource;
import org.postgresql.ds.jdbc4.AbstractJdbc4PoolingDataSource;

public class PGPoolingDataSource extends AbstractJdbc4PoolingDataSource implements DataSource
{
    @Override
    protected void addDataSource(final String dataSourceName) {
        PGPoolingDataSource.dataSources.put(dataSourceName, this);
    }
}
