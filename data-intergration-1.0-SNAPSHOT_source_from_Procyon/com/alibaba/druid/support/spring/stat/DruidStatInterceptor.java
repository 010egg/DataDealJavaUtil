// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.stat;

import com.alibaba.druid.filter.stat.StatFilterContextListenerAdapter;
import com.alibaba.druid.support.logging.LogFactory;
import org.springframework.aop.TargetSource;
import java.lang.reflect.Method;
import org.springframework.aop.framework.Advised;
import org.aopalliance.intercept.MethodInvocation;
import com.alibaba.druid.filter.stat.StatFilterContextListener;
import com.alibaba.druid.filter.stat.StatFilterContext;
import com.alibaba.druid.support.logging.Log;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.aopalliance.intercept.MethodInterceptor;

public class DruidStatInterceptor implements MethodInterceptor, InitializingBean, DisposableBean
{
    public static final String PROP_NAME_PROFILE = "druid.profile";
    private static final Log LOG;
    private static SpringStat springStat;
    private SpringMethodContextListener statContextListener;
    
    public DruidStatInterceptor() {
        this.statContextListener = new SpringMethodContextListener();
    }
    
    public void afterPropertiesSet() throws Exception {
        SpringStatManager.getInstance().addSpringStat(DruidStatInterceptor.springStat);
        StatFilterContext.getInstance().addContextListener(this.statContextListener);
    }
    
    public void destroy() throws Exception {
        StatFilterContext.getInstance().removeContextListener(this.statContextListener);
    }
    
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final SpringMethodStat lastMethodStat = SpringMethodStat.current();
        final SpringMethodInfo methodInfo = this.getMethodInfo(invocation);
        final SpringMethodStat methodStat = DruidStatInterceptor.springStat.getMethodStat(methodInfo, true);
        if (methodStat != null) {
            methodStat.beforeInvoke();
        }
        final long startNanos = System.nanoTime();
        Throwable error = null;
        try {
            return invocation.proceed();
        }
        catch (Throwable e) {
            error = e;
            throw e;
        }
        finally {
            final long endNanos = System.nanoTime();
            final long nanos = endNanos - startNanos;
            if (methodStat != null) {
                methodStat.afterInvoke(error, nanos);
            }
            SpringMethodStat.setCurrent(lastMethodStat);
        }
    }
    
    public SpringMethodInfo getMethodInfo(final MethodInvocation invocation) {
        Object thisObject = invocation.getThis();
        final Method method = invocation.getMethod();
        if (thisObject == null) {
            return new SpringMethodInfo(method.getDeclaringClass(), method);
        }
        if (method.getDeclaringClass() == thisObject.getClass()) {
            return new SpringMethodInfo(method.getDeclaringClass(), method);
        }
        Class<?> clazz = thisObject.getClass();
        boolean isCglibProxy = false;
        boolean isJavassistProxy = false;
        for (final Class<?> item : clazz.getInterfaces()) {
            if (item.getName().equals("net.sf.cglib.proxy.Factory")) {
                isCglibProxy = true;
                break;
            }
            if (item.getName().equals("javassist.util.proxy.ProxyObject")) {
                isJavassistProxy = true;
                break;
            }
        }
        if (isCglibProxy || isJavassistProxy) {
            final Class<?> superClazz = clazz.getSuperclass();
            return new SpringMethodInfo(superClazz, method);
        }
        clazz = null;
        try {
            Object target;
            for (int i = 0; i < 10 && thisObject instanceof Advised; thisObject = target, ++i) {
                final TargetSource targetSource = ((Advised)thisObject).getTargetSource();
                if (targetSource == null) {
                    break;
                }
                target = targetSource.getTarget();
                if (target == null) {
                    clazz = (Class<?>)targetSource.getTargetClass();
                    break;
                }
            }
        }
        catch (Exception ex) {
            DruidStatInterceptor.LOG.error("getMethodInfo error", ex);
        }
        if (clazz == null) {
            return new SpringMethodInfo(method.getDeclaringClass(), method);
        }
        return new SpringMethodInfo(clazz, method);
    }
    
    static {
        LOG = LogFactory.getLog(DruidStatInterceptor.class);
        DruidStatInterceptor.springStat = new SpringStat();
    }
    
    class SpringMethodContextListener extends StatFilterContextListenerAdapter
    {
        @Override
        public void addUpdateCount(final int updateCount) {
            final SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.addJdbcUpdateCount(updateCount);
            }
        }
        
        @Override
        public void addFetchRowCount(final int fetchRowCount) {
            final SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.addJdbcFetchRowCount(fetchRowCount);
            }
        }
        
        @Override
        public void executeBefore(final String sql, final boolean inTransaction) {
            final SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcExecuteCount();
            }
        }
        
        @Override
        public void executeAfter(final String sql, final long nanos, final Throwable error) {
            final SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.addJdbcExecuteTimeNano(nanos);
                if (error != null) {
                    springMethodStat.incrementJdbcExecuteErrorCount();
                }
            }
        }
        
        @Override
        public void commit() {
            final SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcCommitCount();
            }
        }
        
        @Override
        public void rollback() {
            final SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcRollbackCount();
            }
        }
        
        @Override
        public void pool_connect() {
            final SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcPoolConnectionOpenCount();
            }
        }
        
        @Override
        public void pool_close(final long nanos) {
            final SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcPoolConnectionCloseCount();
            }
        }
        
        @Override
        public void resultSet_open() {
            final SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcResultSetOpenCount();
            }
        }
        
        @Override
        public void resultSet_close(final long nanos) {
            final SpringMethodStat springMethodStat = SpringMethodStat.current();
            if (springMethodStat != null) {
                springMethodStat.incrementJdbcResultSetCloseCount();
            }
        }
    }
}
