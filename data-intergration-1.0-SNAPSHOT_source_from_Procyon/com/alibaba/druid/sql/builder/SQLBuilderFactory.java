// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.builder;

import com.alibaba.druid.sql.builder.impl.SQLUpdateBuilderImpl;
import com.alibaba.druid.sql.builder.impl.SQLDeleteBuilderImpl;
import com.alibaba.druid.sql.builder.impl.SQLSelectBuilderImpl;
import com.alibaba.druid.DbType;

public class SQLBuilderFactory
{
    public static SQLSelectBuilder createSelectSQLBuilder(final DbType dbType) {
        return new SQLSelectBuilderImpl(dbType);
    }
    
    public static SQLSelectBuilder createSelectSQLBuilder(final String sql, final DbType dbType) {
        return new SQLSelectBuilderImpl(sql, dbType);
    }
    
    public static SQLDeleteBuilder createDeleteBuilder(final DbType dbType) {
        return new SQLDeleteBuilderImpl(dbType);
    }
    
    public static SQLDeleteBuilder createDeleteBuilder(final String sql, final DbType dbType) {
        return new SQLDeleteBuilderImpl(sql, dbType);
    }
    
    public static SQLUpdateBuilder createUpdateBuilder(final DbType dbType) {
        return new SQLUpdateBuilderImpl(dbType);
    }
    
    public static SQLUpdateBuilder createUpdateBuilder(final String sql, final DbType dbType) {
        return new SQLUpdateBuilderImpl(sql, dbType);
    }
}
