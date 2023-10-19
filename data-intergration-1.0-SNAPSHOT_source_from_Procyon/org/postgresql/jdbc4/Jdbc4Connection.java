// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.sql.Clob;
import java.sql.Blob;
import java.sql.NClob;
import java.sql.SQLXML;
import java.sql.Struct;
import java.sql.Array;
import java.sql.SQLClientInfoException;
import java.util.concurrent.Executor;
import java.util.Map;
import java.sql.DatabaseMetaData;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;
import org.postgresql.util.HostSpec;
import java.sql.Connection;

public class Jdbc4Connection extends AbstractJdbc4Connection implements Connection
{
    public Jdbc4Connection(final HostSpec[] hostSpecs, final String user, final String database, final Properties info, final String url) throws SQLException {
        super(hostSpecs, user, database, info, url);
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        this.checkClosed();
        final Jdbc4Statement s = new Jdbc4Statement(this, resultSetType, resultSetConcurrency, resultSetHoldability);
        s.setPrepareThreshold(this.getPrepareThreshold());
        return s;
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        this.checkClosed();
        final Jdbc4PreparedStatement s = new Jdbc4PreparedStatement(this, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        s.setPrepareThreshold(this.getPrepareThreshold());
        return s;
    }
    
    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        this.checkClosed();
        final Jdbc4CallableStatement s = new Jdbc4CallableStatement(this, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        s.setPrepareThreshold(this.getPrepareThreshold());
        return s;
    }
    
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        this.checkClosed();
        if (this.metadata == null) {
            this.metadata = new Jdbc4DatabaseMetaData(this);
        }
        return this.metadata;
    }
    
    @Override
    public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
        this.setTypeMapImpl(map);
    }
}
