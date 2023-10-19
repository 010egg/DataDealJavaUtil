// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.sql.SQLException;
import org.postgresql.core.BaseConnection;
import java.sql.Blob;

public class Jdbc4Blob extends AbstractJdbc4Blob implements Blob
{
    public Jdbc4Blob(final BaseConnection conn, final long oid) throws SQLException {
        super(conn, oid);
    }
}
