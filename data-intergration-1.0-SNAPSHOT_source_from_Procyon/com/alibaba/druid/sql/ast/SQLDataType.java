// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.DbType;
import java.util.List;

public interface SQLDataType extends SQLObject
{
    String getName();
    
    long nameHashCode64();
    
    void setName(final String p0);
    
    List<SQLExpr> getArguments();
    
    Boolean getWithTimeZone();
    
    void setWithTimeZone(final Boolean p0);
    
    boolean isWithLocalTimeZone();
    
    void setWithLocalTimeZone(final boolean p0);
    
    SQLDataType clone();
    
    void setDbType(final DbType p0);
    
    DbType getDbType();
    
    int jdbcType();
    
    boolean isInt();
    
    boolean isNumberic();
    
    boolean isString();
    
    boolean hasKeyLength();
    
    public interface Constants
    {
        public static final String CHAR = "CHAR";
        public static final String NCHAR = "NCHAR";
        public static final String VARCHAR = "VARCHAR";
        public static final String VARBINARY = "VARBINARY";
        public static final String DATE = "DATE";
        public static final String TIMESTAMP = "TIMESTAMP";
        public static final String XML = "XML";
        public static final String DECIMAL = "DECIMAL";
        public static final String NUMBER = "NUMBER";
        public static final String REAL = "REAL";
        public static final String DOUBLE_PRECISION = "DOUBLE PRECISION";
        public static final String DOUBLE = "DOUBLE";
        public static final String TINYINT = "TINYINT";
        public static final String SMALLINT = "SMALLINT";
        public static final String INT = "INT";
        public static final String BIGINT = "BIGINT";
        public static final String TEXT = "TEXT";
        public static final String BYTEA = "BYTEA";
        public static final String BOOLEAN = "BOOLEAN";
        public static final String FLOAT = "FLOAT";
    }
}
