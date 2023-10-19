// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.xa.jdbc4;

import java.sql.SQLFeatureNotSupportedException;
import org.postgresql.Driver;
import java.util.logging.Logger;
import org.postgresql.xa.jdbc3.AbstractJdbc3XADataSource;

public class AbstractJdbc4XADataSource extends AbstractJdbc3XADataSource
{
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw Driver.notImplemented(this.getClass(), "getParentLogger()");
    }
}
