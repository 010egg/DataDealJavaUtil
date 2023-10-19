// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.util.Map;
import java.sql.SQLException;
import java.sql.CallableStatement;

class Jdbc4CallableStatement extends Jdbc4PreparedStatement implements CallableStatement
{
    Jdbc4CallableStatement(final Jdbc4Connection connection, final String sql, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(connection, sql, true, rsType, rsConcurrency, rsHoldability);
        if (!connection.haveMinimumServerVersion("8.1") || connection.getProtocolVersion() == 2) {
            this.adjustIndex = this.outParmBeforeFunc;
        }
    }
    
    @Override
    public Object getObject(final int i, final Map<String, Class<?>> map) throws SQLException {
        return this.getObjectImpl(i, map);
    }
    
    @Override
    public Object getObject(final String s, final Map<String, Class<?>> map) throws SQLException {
        return this.getObjectImpl(s, map);
    }
}
