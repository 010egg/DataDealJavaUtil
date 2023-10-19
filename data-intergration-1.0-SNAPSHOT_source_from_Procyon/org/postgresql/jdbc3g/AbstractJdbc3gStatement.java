// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc3g;

import org.postgresql.util.ByteConverter;
import java.util.UUID;
import java.sql.SQLException;
import org.postgresql.jdbc3.AbstractJdbc3Connection;
import org.postgresql.jdbc3.AbstractJdbc3Statement;

public abstract class AbstractJdbc3gStatement extends AbstractJdbc3Statement
{
    public AbstractJdbc3gStatement(final AbstractJdbc3Connection c, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(c, rsType, rsConcurrency, rsHoldability);
    }
    
    public AbstractJdbc3gStatement(final AbstractJdbc3Connection connection, final String sql, final boolean isCallable, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(connection, sql, isCallable, rsType, rsConcurrency, rsHoldability);
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x) throws SQLException {
        if (x instanceof UUID && this.connection.haveMinimumServerVersion("8.3")) {
            this.setUuid(parameterIndex, (UUID)x);
        }
        else {
            super.setObject(parameterIndex, x);
        }
    }
    
    @Override
    public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scale) throws SQLException {
        if (targetSqlType == 1111 && x instanceof UUID && this.connection.haveMinimumServerVersion("8.3")) {
            this.setUuid(parameterIndex, (UUID)x);
        }
        else {
            super.setObject(parameterIndex, x, targetSqlType, scale);
        }
    }
    
    private void setUuid(final int parameterIndex, final UUID uuid) throws SQLException {
        if (this.connection.binaryTransferSend(2950)) {
            final byte[] val = new byte[16];
            ByteConverter.int8(val, 0, uuid.getMostSignificantBits());
            ByteConverter.int8(val, 8, uuid.getLeastSignificantBits());
            this.bindBytes(parameterIndex, val, 2950);
        }
        else {
            this.bindLiteral(parameterIndex, uuid.toString(), 2950);
        }
    }
}
