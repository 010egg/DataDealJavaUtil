// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import org.h2.message.TraceObject;
import java.sql.SQLException;
import org.h2.jdbc.JdbcConnection;
import javax.sql.XAConnection;
import java.sql.Connection;
import org.h2.jdbcx.JdbcDataSourceFactory;
import java.lang.reflect.Method;
import org.h2.jdbcx.JdbcXAConnection;
import java.lang.reflect.Constructor;

public class H2Utils
{
    private static volatile Constructor<JdbcXAConnection> constructor;
    private static volatile Method method;
    public static final int XA_DATA_SOURCE = 13;
    
    public static Object createJdbcDataSourceFactory() {
        return new JdbcDataSourceFactory();
    }
    
    public static XAConnection createXAConnection(final Object factory, final Connection physicalConn) throws SQLException {
        try {
            if (H2Utils.constructor == null) {
                (H2Utils.constructor = JdbcXAConnection.class.getDeclaredConstructor(JdbcDataSourceFactory.class, Integer.TYPE, JdbcConnection.class)).setAccessible(true);
            }
            final int id = getNextId(13);
            return (XAConnection)H2Utils.constructor.newInstance(factory, id, physicalConn);
        }
        catch (Exception e) {
            throw new SQLException("createXAConnection error", e);
        }
    }
    
    public static int getNextId(final int type) throws Exception {
        if (H2Utils.method == null) {
            (H2Utils.method = TraceObject.class.getDeclaredMethod("getNextId", Integer.TYPE)).setAccessible(true);
        }
        return (int)H2Utils.method.invoke(null, type);
    }
}
