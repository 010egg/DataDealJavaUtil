// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.sql.ParameterMetaData;
import org.postgresql.core.BaseConnection;
import org.postgresql.core.BaseStatement;
import java.sql.ResultSet;
import org.postgresql.core.ResultCursor;
import java.util.List;
import org.postgresql.core.Field;
import org.postgresql.core.Query;
import java.sql.SQLException;
import java.sql.Statement;

class Jdbc4Statement extends AbstractJdbc4Statement implements Statement
{
    Jdbc4Statement(final Jdbc4Connection c, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(c, rsType, rsConcurrency, rsHoldability);
    }
    
    protected Jdbc4Statement(final Jdbc4Connection connection, final String sql, final boolean isCallable, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(connection, sql, isCallable, rsType, rsConcurrency, rsHoldability);
    }
    
    @Override
    public ResultSet createResultSet(final Query originalQuery, final Field[] fields, final List tuples, final ResultCursor cursor) throws SQLException {
        final Jdbc4ResultSet newResult = new Jdbc4ResultSet(originalQuery, this, fields, tuples, cursor, this.getMaxRows(), this.getMaxFieldSize(), this.getResultSetType(), this.getResultSetConcurrency(), this.getResultSetHoldability());
        newResult.setFetchSize(this.getFetchSize());
        newResult.setFetchDirection(this.getFetchDirection());
        return newResult;
    }
    
    @Override
    public ParameterMetaData createParameterMetaData(final BaseConnection conn, final int[] oids) throws SQLException {
        return new Jdbc4ParameterMetaData(conn, oids);
    }
}
