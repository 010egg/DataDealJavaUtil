// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.sql.SQLException;
import org.postgresql.core.BaseConnection;
import java.sql.Clob;

public class Jdbc4Clob extends AbstractJdbc4Clob implements Clob
{
    public Jdbc4Clob(final BaseConnection conn, final long oid) throws SQLException {
        super(conn, oid);
    }
}
