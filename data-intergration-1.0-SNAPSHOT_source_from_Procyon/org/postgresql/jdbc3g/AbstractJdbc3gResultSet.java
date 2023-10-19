// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc3g;

import org.postgresql.util.ByteConverter;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.util.UUID;
import java.sql.SQLException;
import org.postgresql.core.ResultCursor;
import java.util.List;
import org.postgresql.core.Field;
import org.postgresql.core.BaseStatement;
import org.postgresql.core.Query;
import org.postgresql.jdbc3.AbstractJdbc3ResultSet;

public abstract class AbstractJdbc3gResultSet extends AbstractJdbc3ResultSet
{
    public AbstractJdbc3gResultSet(final Query originalQuery, final BaseStatement statement, final Field[] fields, final List tuples, final ResultCursor cursor, final int maxRows, final int maxFieldSize, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(originalQuery, statement, fields, tuples, cursor, maxRows, maxFieldSize, rsType, rsConcurrency, rsHoldability);
    }
    
    @Override
    protected Object getUUID(final String data) throws SQLException {
        UUID uuid;
        try {
            uuid = UUID.fromString(data);
        }
        catch (IllegalArgumentException iae) {
            throw new PSQLException(GT.tr("Invalid UUID data."), PSQLState.INVALID_PARAMETER_VALUE, iae);
        }
        return uuid;
    }
    
    @Override
    protected Object getUUID(final byte[] data) throws SQLException {
        return new UUID(ByteConverter.int8(data, 0), ByteConverter.int8(data, 8));
    }
}
