// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.ibatis;

import com.alibaba.druid.support.logging.LogFactory;
import com.ibatis.sqlmap.client.SqlMapTransactionManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapSessionImpl;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.druid.support.logging.Log;

public class IbatisUtils
{
    private static Log LOG;
    private static boolean VERSION_2_3_4;
    private static Method methodGetId;
    private static Method methodGetResource;
    private static Field sessionField;
    
    public static boolean isVersion2_3_4() {
        return IbatisUtils.VERSION_2_3_4;
    }
    
    public static SqlMapExecutor setClientImpl(final SqlMapExecutor session, final SqlMapClientImplWrapper clientImplWrapper) {
        if (session == null || clientImplWrapper == null) {
            return session;
        }
        if (session.getClass() == SqlMapSessionImpl.class) {
            final SqlMapSessionImpl sessionImpl = (SqlMapSessionImpl)session;
            set(sessionImpl, clientImplWrapper);
        }
        return session;
    }
    
    protected static String getId(final Object statement) {
        try {
            if (IbatisUtils.methodGetId == null) {
                final Class<?> clazz = statement.getClass();
                IbatisUtils.methodGetId = clazz.getMethod("getId", (Class<?>[])new Class[0]);
            }
            final Object returnValue = IbatisUtils.methodGetId.invoke(statement, new Object[0]);
            if (returnValue == null) {
                return null;
            }
            return returnValue.toString();
        }
        catch (Exception ex) {
            IbatisUtils.LOG.error("createIdError", ex);
            return null;
        }
    }
    
    protected static String getResource(final Object statement) {
        try {
            if (IbatisUtils.methodGetResource == null) {
                IbatisUtils.methodGetResource = statement.getClass().getMethod("getResource", (Class<?>[])new Class[0]);
            }
            return (String)IbatisUtils.methodGetResource.invoke(statement, new Object[0]);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static void set(final SqlMapSessionImpl session, final SqlMapClientImpl client) {
        if (IbatisUtils.sessionField == null) {
            for (final Field field : SqlMapSessionImpl.class.getDeclaredFields()) {
                if (field.getName().equals("session") || field.getName().equals("sessionScope")) {
                    (IbatisUtils.sessionField = field).setAccessible(true);
                    break;
                }
            }
        }
        if (IbatisUtils.sessionField != null) {
            try {
                final SessionScope sessionScope = (SessionScope)IbatisUtils.sessionField.get(session);
                if (sessionScope != null) {
                    if (sessionScope.getSqlMapClient() != null && sessionScope.getSqlMapClient().getClass() == SqlMapClientImpl.class) {
                        sessionScope.setSqlMapClient((SqlMapClient)client);
                    }
                    if (sessionScope.getSqlMapExecutor() != null && sessionScope.getSqlMapExecutor().getClass() == SqlMapClientImpl.class) {
                        sessionScope.setSqlMapExecutor((SqlMapExecutor)client);
                    }
                    if (sessionScope.getSqlMapTxMgr() != null && sessionScope.getSqlMapTxMgr().getClass() == SqlMapClientImpl.class) {
                        sessionScope.setSqlMapTxMgr((SqlMapTransactionManager)client);
                    }
                }
            }
            catch (Exception e) {
                IbatisUtils.LOG.error(e.getMessage(), e);
            }
        }
    }
    
    static {
        IbatisUtils.LOG = LogFactory.getLog(IbatisUtils.class);
        IbatisUtils.VERSION_2_3_4 = false;
        IbatisUtils.methodGetId = null;
        IbatisUtils.methodGetResource = null;
        try {
            final Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass("com.ibatis.sqlmap.engine.mapping.result.AutoResultMap");
            final Method[] methods2;
            final Method[] methods = methods2 = clazz.getMethods();
            for (final Method method : methods2) {
                if (method.getName().equals("setResultObjectValues")) {
                    IbatisUtils.VERSION_2_3_4 = true;
                    break;
                }
            }
        }
        catch (Throwable e) {
            IbatisUtils.LOG.error("Error while initializing", e);
        }
    }
}
