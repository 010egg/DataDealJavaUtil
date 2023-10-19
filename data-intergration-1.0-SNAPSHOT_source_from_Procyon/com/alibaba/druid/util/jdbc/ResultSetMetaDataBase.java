// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSetMetaData;

public class ResultSetMetaDataBase implements ResultSetMetaData
{
    private final List<ColumnMetaData> columns;
    
    public ResultSetMetaDataBase() {
        this.columns = new ArrayList<ColumnMetaData>();
    }
    
    public List<ColumnMetaData> getColumns() {
        return this.columns;
    }
    
    public int findColumn(final String columnName) throws SQLException {
        for (int i = 0; i < this.columns.size(); ++i) {
            final ColumnMetaData column = this.columns.get(i);
            if (column.getColumnName().equals(columnName)) {
                return i + 1;
            }
        }
        throw new SQLException("column '" + columnName + "' not found.");
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (this.isWrapperFor(iface)) {
            return (T)this;
        }
        return null;
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface != null && iface.isAssignableFrom(this.getClass());
    }
    
    @Override
    public int getColumnCount() throws SQLException {
        return this.columns.size();
    }
    
    @Override
    public boolean isAutoIncrement(final int column) throws SQLException {
        return this.getColumn(column).isAutoIncrement();
    }
    
    @Override
    public boolean isCaseSensitive(final int column) throws SQLException {
        return this.getColumn(column).isCaseSensitive();
    }
    
    @Override
    public boolean isSearchable(final int column) throws SQLException {
        return this.getColumn(column).isSearchable();
    }
    
    @Override
    public boolean isCurrency(final int column) throws SQLException {
        return this.getColumn(column).isCurrency();
    }
    
    @Override
    public int isNullable(final int column) throws SQLException {
        return this.getColumn(column).getNullable();
    }
    
    @Override
    public boolean isSigned(final int column) throws SQLException {
        return this.getColumn(column).isSigned();
    }
    
    @Override
    public int getColumnDisplaySize(final int column) throws SQLException {
        return this.getColumn(column).getColumnDisplaySize();
    }
    
    @Override
    public String getColumnLabel(final int column) throws SQLException {
        return this.getColumn(column).getColumnLabel();
    }
    
    public ColumnMetaData getColumn(final int column) {
        return this.columns.get(column - 1);
    }
    
    @Override
    public String getColumnName(final int column) throws SQLException {
        return this.getColumn(column).getColumnName();
    }
    
    @Override
    public String getSchemaName(final int column) throws SQLException {
        return this.getColumn(column).getSchemaName();
    }
    
    @Override
    public int getPrecision(final int column) throws SQLException {
        return this.getColumn(column).getPrecision();
    }
    
    @Override
    public int getScale(final int column) throws SQLException {
        return this.getColumn(column).getScale();
    }
    
    @Override
    public String getTableName(final int column) throws SQLException {
        return this.getColumn(column).getTableName();
    }
    
    @Override
    public String getCatalogName(final int column) throws SQLException {
        return this.getColumn(column).getCatalogName();
    }
    
    @Override
    public int getColumnType(final int column) throws SQLException {
        return this.getColumn(column).getColumnType();
    }
    
    @Override
    public String getColumnTypeName(final int column) throws SQLException {
        return this.getColumn(column).getColumnTypeName();
    }
    
    @Override
    public boolean isReadOnly(final int column) throws SQLException {
        return this.getColumn(column).isReadOnly();
    }
    
    @Override
    public boolean isWritable(final int column) throws SQLException {
        return this.getColumn(column).isWritable();
    }
    
    @Override
    public boolean isDefinitelyWritable(final int column) throws SQLException {
        return this.getColumn(column).isDefinitelyWritable();
    }
    
    @Override
    public String getColumnClassName(final int column) throws SQLException {
        return this.getColumn(column).getColumnClassName();
    }
    
    public static class ColumnMetaData
    {
        private boolean autoIncrement;
        private boolean caseSensitive;
        private boolean searchable;
        private boolean currency;
        private int nullable;
        private boolean signed;
        private int columnDisplaySize;
        private String columnLabel;
        private String columnName;
        private String schemaName;
        private int precision;
        private int scale;
        private String tableName;
        private String catalogName;
        private int columnType;
        private String columnTypeName;
        private boolean readOnly;
        private boolean writable;
        private boolean definitelyWritable;
        private String columnClassName;
        
        public ColumnMetaData() {
            this.autoIncrement = false;
            this.nullable = 0;
        }
        
        public boolean isAutoIncrement() {
            return this.autoIncrement;
        }
        
        public void setAutoIncrement(final boolean autoIncrement) {
            this.autoIncrement = autoIncrement;
        }
        
        public boolean isCaseSensitive() {
            return this.caseSensitive;
        }
        
        public void setCaseSensitive(final boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }
        
        public boolean isSearchable() {
            return this.searchable;
        }
        
        public void setSearchable(final boolean searchable) {
            this.searchable = searchable;
        }
        
        public boolean isCurrency() {
            return this.currency;
        }
        
        public void setCurrency(final boolean currency) {
            this.currency = currency;
        }
        
        public int getNullable() {
            return this.nullable;
        }
        
        public void setNullable(final int nullable) {
            this.nullable = nullable;
        }
        
        public boolean isSigned() {
            return this.signed;
        }
        
        public void setSigned(final boolean signed) {
            this.signed = signed;
        }
        
        public int getColumnDisplaySize() {
            return this.columnDisplaySize;
        }
        
        public void setColumnDisplaySize(final int columnDisplaySize) {
            this.columnDisplaySize = columnDisplaySize;
        }
        
        public String getColumnLabel() {
            return this.columnLabel;
        }
        
        public void setColumnLabel(final String columnLabel) {
            this.columnLabel = columnLabel;
        }
        
        public String getColumnName() {
            return this.columnName;
        }
        
        public void setColumnName(final String columnName) {
            this.columnName = columnName;
        }
        
        public String getSchemaName() {
            return this.schemaName;
        }
        
        public void setSchemaName(final String schemaName) {
            this.schemaName = schemaName;
        }
        
        public int getPrecision() {
            return this.precision;
        }
        
        public void setPrecision(final int precision) {
            this.precision = precision;
        }
        
        public int getScale() {
            return this.scale;
        }
        
        public void setScale(final int scale) {
            this.scale = scale;
        }
        
        public String getTableName() {
            return this.tableName;
        }
        
        public void setTableName(final String tableName) {
            this.tableName = tableName;
        }
        
        public String getCatalogName() {
            return this.catalogName;
        }
        
        public void setCatalogName(final String catalogName) {
            this.catalogName = catalogName;
        }
        
        public int getColumnType() {
            return this.columnType;
        }
        
        public void setColumnType(final int columnType) {
            this.columnType = columnType;
        }
        
        public String getColumnTypeName() {
            return this.columnTypeName;
        }
        
        public void setColumnTypeName(final String columnTypeName) {
            this.columnTypeName = columnTypeName;
        }
        
        public boolean isReadOnly() {
            return this.readOnly;
        }
        
        public void setReadOnly(final boolean readOnly) {
            this.readOnly = readOnly;
        }
        
        public boolean isWritable() {
            return this.writable;
        }
        
        public void setWritable(final boolean writable) {
            this.writable = writable;
        }
        
        public boolean isDefinitelyWritable() {
            return this.definitelyWritable;
        }
        
        public void setDefinitelyWritable(final boolean definitelyWritable) {
            this.definitelyWritable = definitelyWritable;
        }
        
        public String getColumnClassName() {
            return this.columnClassName;
        }
        
        public void setColumnClassName(final String columnClassName) {
            this.columnClassName = columnClassName;
        }
    }
}
