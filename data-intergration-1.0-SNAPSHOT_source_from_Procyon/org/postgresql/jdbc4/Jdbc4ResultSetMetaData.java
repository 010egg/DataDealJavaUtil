// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.sql.SQLException;
import org.postgresql.core.Field;
import org.postgresql.core.BaseConnection;
import java.sql.ResultSetMetaData;

public class Jdbc4ResultSetMetaData extends AbstractJdbc4ResultSetMetaData implements ResultSetMetaData
{
    public Jdbc4ResultSetMetaData(final BaseConnection connection, final Field[] fields) {
        super(connection, fields);
    }
}
