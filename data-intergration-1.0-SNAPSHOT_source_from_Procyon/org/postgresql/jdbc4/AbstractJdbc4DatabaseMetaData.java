// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc4;

import java.util.List;
import org.postgresql.core.BaseStatement;
import java.util.ArrayList;
import org.postgresql.core.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.postgresql.Driver;
import java.sql.RowIdLifetime;
import org.postgresql.jdbc3.AbstractJdbc3Connection;
import org.postgresql.jdbc3.AbstractJdbc3DatabaseMetaData;

public abstract class AbstractJdbc4DatabaseMetaData extends AbstractJdbc3DatabaseMetaData
{
    public AbstractJdbc4DatabaseMetaData(final AbstractJdbc4Connection conn) {
        super(conn);
    }
    
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getRowIdLifetime()");
    }
    
    public ResultSet getSchemas(final String catalog, final String schemaPattern) throws SQLException {
        return this.getSchemas(4, catalog, schemaPattern);
    }
    
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        return true;
    }
    
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        return false;
    }
    
    public ResultSet getClientInfoProperties() throws SQLException {
        final Field[] f = { new Field("NAME", 1043), new Field("MAX_LEN", 23), new Field("DEFAULT_VALUE", 1043), new Field("DESCRIPTION", 1043) };
        final List v = new ArrayList();
        if (this.connection.haveMinimumServerVersion("9.0")) {
            final byte[][] tuple = { this.connection.encodeString("ApplicationName"), this.connection.encodeString(Integer.toString(this.getMaxNameLength())), this.connection.encodeString(""), this.connection.encodeString("The name of the application currently utilizing the connection.") };
            v.add(tuple);
        }
        return ((BaseStatement)this.createMetaDataStatement()).createDriverResultSet(f, v);
    }
    
    public boolean providesQueryObjectGenerator() throws SQLException {
        return false;
    }
    
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface.isAssignableFrom(this.getClass());
    }
    
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(this.getClass())) {
            return iface.cast(this);
        }
        throw new SQLException("Cannot unwrap to " + iface.getName());
    }
    
    public ResultSet getFunctions(final String catalog, final String schemaPattern, final String functionNamePattern) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getFunction(String, String, String)");
    }
    
    public ResultSet getFunctionColumns(final String catalog, final String schemaPattern, final String functionNamePattern, final String columnNamePattern) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getFunctionColumns(String, String, String, String)");
    }
    
    @Override
    public int getJDBCMajorVersion() throws SQLException {
        return 4;
    }
    
    @Override
    public ResultSet getColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern) throws SQLException {
        return this.getColumns(4, catalog, schemaPattern, tableNamePattern, columnNamePattern);
    }
    
    @Override
    public ResultSet getProcedures(final String catalog, final String schemaPattern, final String procedureNamePattern) throws SQLException {
        return this.getProcedures(4, catalog, schemaPattern, procedureNamePattern);
    }
    
    @Override
    public ResultSet getProcedureColumns(final String catalog, final String schemaPattern, final String procedureNamePattern, final String columnNamePattern) throws SQLException {
        return this.getProcedureColumns(4, catalog, schemaPattern, procedureNamePattern, columnNamePattern);
    }
    
    public ResultSet getPseudoColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getPseudoColumns(String, String, String, String)");
    }
    
    public boolean generatedKeyAlwaysReturned() throws SQLException {
        return true;
    }
}
