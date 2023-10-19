// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import org.postgresql.core.Field;
import org.postgresql.core.BaseConnection;
import org.postgresql.PGResultSetMetaData;

public abstract class AbstractJdbc2ResultSetMetaData implements PGResultSetMetaData
{
    protected final BaseConnection connection;
    protected final Field[] fields;
    private boolean fieldInfoFetched;
    private CacheMetadata _cache;
    
    public AbstractJdbc2ResultSetMetaData(final BaseConnection connection, final Field[] fields) {
        this.connection = connection;
        this.fields = fields;
        this.fieldInfoFetched = false;
        this._cache = new CacheMetadata();
    }
    
    public int getColumnCount() throws SQLException {
        return this.fields.length;
    }
    
    public boolean isAutoIncrement(final int column) throws SQLException {
        this.fetchFieldMetaData();
        final Field field = this.getField(column);
        return field.getAutoIncrement();
    }
    
    public boolean isCaseSensitive(final int column) throws SQLException {
        final Field field = this.getField(column);
        return this.connection.getTypeInfo().isCaseSensitive(field.getOID());
    }
    
    public boolean isSearchable(final int column) throws SQLException {
        return true;
    }
    
    public boolean isCurrency(final int column) throws SQLException {
        final String type_name = this.getPGType(column);
        return type_name.equals("cash") || type_name.equals("money");
    }
    
    public int isNullable(final int column) throws SQLException {
        this.fetchFieldMetaData();
        final Field field = this.getField(column);
        return field.getNullable();
    }
    
    public boolean isSigned(final int column) throws SQLException {
        final Field field = this.getField(column);
        return this.connection.getTypeInfo().isSigned(field.getOID());
    }
    
    public int getColumnDisplaySize(final int column) throws SQLException {
        final Field field = this.getField(column);
        return this.connection.getTypeInfo().getDisplaySize(field.getOID(), field.getMod());
    }
    
    public String getColumnLabel(final int column) throws SQLException {
        final Field field = this.getField(column);
        return field.getColumnLabel();
    }
    
    public String getColumnName(final int column) throws SQLException {
        return this.getColumnLabel(column);
    }
    
    @Override
    public String getBaseColumnName(final int column) throws SQLException {
        this.fetchFieldMetaData();
        final Field field = this.getField(column);
        return field.getColumnName();
    }
    
    public String getSchemaName(final int column) throws SQLException {
        return "";
    }
    
    private void fetchFieldMetaData() throws SQLException {
        if (this.fieldInfoFetched) {
            return;
        }
        final String idFields = this._cache.getIdFields(this.fields);
        if (this._cache.isCached(idFields)) {
            this._cache.getCache(idFields, this.fields);
            this.fieldInfoFetched = true;
            return;
        }
        this.fieldInfoFetched = true;
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.oid, a.attnum, a.attname, c.relname, n.nspname, ");
        sql.append("a.attnotnull OR (t.typtype = 'd' AND t.typnotnull), ");
        sql.append("pg_catalog.pg_get_expr(d.adbin, d.adrelid) LIKE '%nextval(%' ");
        sql.append("FROM pg_catalog.pg_class c ");
        sql.append("JOIN pg_catalog.pg_namespace n ON (c.relnamespace = n.oid) ");
        sql.append("JOIN pg_catalog.pg_attribute a ON (c.oid = a.attrelid) ");
        sql.append("JOIN pg_catalog.pg_type t ON (a.atttypid = t.oid) ");
        sql.append("LEFT JOIN pg_catalog.pg_attrdef d ON (d.adrelid = a.attrelid AND d.adnum = a.attnum) ");
        sql.append("JOIN (");
        boolean hasSourceInfo = false;
        for (int i = 0; i < this.fields.length; ++i) {
            if (this.fields[i].getTableOid() != 0) {
                if (hasSourceInfo) {
                    sql.append(" UNION ALL ");
                }
                sql.append("SELECT ");
                sql.append(this.fields[i].getTableOid());
                if (!hasSourceInfo) {
                    sql.append(" AS oid ");
                }
                sql.append(", ");
                sql.append(this.fields[i].getPositionInTable());
                if (!hasSourceInfo) {
                    sql.append(" AS attnum");
                }
                if (!hasSourceInfo) {
                    hasSourceInfo = true;
                }
            }
        }
        sql.append(") vals ON (c.oid = vals.oid AND a.attnum = vals.attnum) ");
        if (!hasSourceInfo) {
            return;
        }
        final Statement stmt = this.connection.createStatement();
        final ResultSet rs = stmt.executeQuery(sql.toString());
        while (rs.next()) {
            final int table = (int)rs.getLong(1);
            final int column = (int)rs.getLong(2);
            final String columnName = rs.getString(3);
            final String tableName = rs.getString(4);
            final String schemaName = rs.getString(5);
            final int nullable = rs.getBoolean(6) ? 0 : 1;
            final boolean autoIncrement = rs.getBoolean(7);
            for (int j = 0; j < this.fields.length; ++j) {
                if (this.fields[j].getTableOid() == table && this.fields[j].getPositionInTable() == column) {
                    this.fields[j].setColumnName(columnName);
                    this.fields[j].setTableName(tableName);
                    this.fields[j].setSchemaName(schemaName);
                    this.fields[j].setNullable(nullable);
                    this.fields[j].setAutoIncrement(autoIncrement);
                }
            }
        }
        stmt.close();
        this._cache.setCache(idFields, this.fields);
    }
    
    @Override
    public String getBaseSchemaName(final int column) throws SQLException {
        this.fetchFieldMetaData();
        final Field field = this.getField(column);
        return field.getSchemaName();
    }
    
    public int getPrecision(final int column) throws SQLException {
        final Field field = this.getField(column);
        return this.connection.getTypeInfo().getPrecision(field.getOID(), field.getMod());
    }
    
    public int getScale(final int column) throws SQLException {
        final Field field = this.getField(column);
        return this.connection.getTypeInfo().getScale(field.getOID(), field.getMod());
    }
    
    public String getTableName(final int column) throws SQLException {
        return this.getBaseTableName(column);
    }
    
    @Override
    public String getBaseTableName(final int column) throws SQLException {
        this.fetchFieldMetaData();
        final Field field = this.getField(column);
        return field.getTableName();
    }
    
    public String getCatalogName(final int column) throws SQLException {
        return "";
    }
    
    public int getColumnType(final int column) throws SQLException {
        return this.getSQLType(column);
    }
    
    @Override
    public int getFormat(final int column) throws SQLException {
        return this.getField(column).getFormat();
    }
    
    public String getColumnTypeName(final int column) throws SQLException {
        final String type = this.getPGType(column);
        if (this.isAutoIncrement(column)) {
            if ("int4".equals(type)) {
                return "serial";
            }
            if ("int8".equals(type)) {
                return "bigserial";
            }
        }
        return type;
    }
    
    public boolean isReadOnly(final int column) throws SQLException {
        return false;
    }
    
    public boolean isWritable(final int column) throws SQLException {
        return !this.isReadOnly(column);
    }
    
    public boolean isDefinitelyWritable(final int column) throws SQLException {
        return false;
    }
    
    protected Field getField(final int columnIndex) throws SQLException {
        if (columnIndex < 1 || columnIndex > this.fields.length) {
            throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", new Object[] { new Integer(columnIndex), new Integer(this.fields.length) }), PSQLState.INVALID_PARAMETER_VALUE);
        }
        return this.fields[columnIndex - 1];
    }
    
    protected String getPGType(final int columnIndex) throws SQLException {
        return this.connection.getTypeInfo().getPGType(this.getField(columnIndex).getOID());
    }
    
    protected int getSQLType(final int columnIndex) throws SQLException {
        return this.connection.getTypeInfo().getSQLType(this.getField(columnIndex).getOID());
    }
    
    public String getColumnClassName(final int column) throws SQLException {
        final Field field = this.getField(column);
        final String result = this.connection.getTypeInfo().getJavaClass(field.getOID());
        if (result != null) {
            return result;
        }
        final int sqlType = this.getSQLType(column);
        switch (sqlType) {
            case 2003: {
                return "java.sql.Array";
            }
            default: {
                final String type = this.getPGType(column);
                if ("unknown".equals(type)) {
                    return "java.lang.String";
                }
                return "java.lang.Object";
            }
        }
    }
}
