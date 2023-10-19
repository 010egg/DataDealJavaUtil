// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc3g;

import java.sql.SQLException;
import org.postgresql.core.TypeInfo;
import java.util.Properties;
import org.postgresql.util.HostSpec;
import org.postgresql.jdbc3.AbstractJdbc3Connection;

public abstract class AbstractJdbc3gConnection extends AbstractJdbc3Connection
{
    public AbstractJdbc3gConnection(final HostSpec[] hostSpecs, final String user, final String database, final Properties info, final String url) throws SQLException {
        super(hostSpecs, user, database, info, url);
        final TypeInfo types = this.getTypeInfo();
        if (this.haveMinimumServerVersion("8.3")) {
            types.addCoreType("uuid", 2950, 1111, "java.util.UUID", 2951);
        }
    }
}
