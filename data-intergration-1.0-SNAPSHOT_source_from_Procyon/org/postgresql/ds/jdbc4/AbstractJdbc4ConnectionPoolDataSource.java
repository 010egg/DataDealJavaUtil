// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.ds.jdbc4;

import java.sql.SQLFeatureNotSupportedException;
import org.postgresql.Driver;
import java.util.logging.Logger;
import org.postgresql.ds.jdbc23.AbstractJdbc23ConnectionPoolDataSource;

public class AbstractJdbc4ConnectionPoolDataSource extends AbstractJdbc23ConnectionPoolDataSource
{
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw Driver.notImplemented(this.getClass(), "getParentLogger()");
    }
}
