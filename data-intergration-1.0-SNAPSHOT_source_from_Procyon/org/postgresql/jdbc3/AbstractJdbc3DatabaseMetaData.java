// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc3;

import org.postgresql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.postgresql.jdbc2.AbstractJdbc2Connection;
import org.postgresql.jdbc2.AbstractJdbc2DatabaseMetaData;

public abstract class AbstractJdbc3DatabaseMetaData extends AbstractJdbc2DatabaseMetaData
{
    public AbstractJdbc3DatabaseMetaData(final AbstractJdbc3Connection conn) {
        super(conn);
    }
    
    public boolean supportsSavepoints() throws SQLException {
        return this.connection.haveMinimumServerVersion("8.0");
    }
    
    public boolean supportsNamedParameters() throws SQLException {
        return false;
    }
    
    public boolean supportsMultipleOpenResults() throws SQLException {
        return false;
    }
    
    public boolean supportsGetGeneratedKeys() throws SQLException {
        return this.connection.haveMinimumServerVersion("8.2");
    }
    
    public ResultSet getSuperTypes(final String catalog, final String schemaPattern, final String typeNamePattern) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getSuperTypes(String,String,String)");
    }
    
    public ResultSet getSuperTables(final String catalog, final String schemaPattern, final String tableNamePattern) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getSuperTables(String,String,String,String)");
    }
    
    public ResultSet getAttributes(final String catalog, final String schemaPattern, final String typeNamePattern, final String attributeNamePattern) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getAttributes(String,String,String,String)");
    }
    
    public boolean supportsResultSetHoldability(final int holdability) throws SQLException {
        return true;
    }
    
    public int getResultSetHoldability() throws SQLException {
        return 1;
    }
    
    public int getDatabaseMajorVersion() throws SQLException {
        return this.connection.getServerMajorVersion();
    }
    
    public int getDatabaseMinorVersion() throws SQLException {
        return this.connection.getServerMinorVersion();
    }
    
    public int getJDBCMajorVersion() throws SQLException {
        return 3;
    }
    
    public int getJDBCMinorVersion() throws SQLException {
        return 0;
    }
    
    public int getSQLStateType() throws SQLException {
        return 2;
    }
    
    public boolean locatorsUpdateCopy() throws SQLException {
        return true;
    }
    
    public boolean supportsStatementPooling() throws SQLException {
        return false;
    }
    
    @Override
    public ResultSet getColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern) throws SQLException {
        return this.getColumns(3, catalog, schemaPattern, tableNamePattern, columnNamePattern);
    }
    
    @Override
    public ResultSet getSchemas() throws SQLException {
        return this.getSchemas(3, null, null);
    }
}
