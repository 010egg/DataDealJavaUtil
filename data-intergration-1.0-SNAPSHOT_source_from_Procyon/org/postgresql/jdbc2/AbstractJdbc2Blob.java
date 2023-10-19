// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import java.sql.SQLException;
import org.postgresql.core.BaseConnection;

public abstract class AbstractJdbc2Blob extends AbstractJdbc2BlobClob
{
    public AbstractJdbc2Blob(final BaseConnection conn, final long oid) throws SQLException {
        super(conn, oid);
    }
}
