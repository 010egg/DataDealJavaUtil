// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.sql.SQLException;
import java.sql.PreparedStatement;

class Jdbc4PreparedStatement extends Jdbc4Statement implements PreparedStatement
{
    Jdbc4PreparedStatement(final Jdbc4Connection connection, final String sql, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        this(connection, sql, false, rsType, rsConcurrency, rsHoldability);
    }
    
    protected Jdbc4PreparedStatement(final Jdbc4Connection connection, final String sql, final boolean isCallable, final int rsType, final int rsConcurrency, final int rsHoldability) throws SQLException {
        super(connection, sql, isCallable, rsType, rsConcurrency, rsHoldability);
    }
}
