// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.mvc;

import com.alibaba.druid.filter.stat.StatFilterContextListener;
import com.alibaba.druid.filter.stat.StatFilterContext;
import org.springframework.web.servlet.HandlerMapping;
import com.alibaba.druid.support.profile.ProfileEntryReqStat;
import com.alibaba.druid.support.profile.ProfileEntryKey;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletContext;
import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.alibaba.druid.util.DruidWebUtils;
import com.alibaba.druid.support.http.stat.WebURIStat;
import com.alibaba.druid.support.http.stat.WebSessionStat;
import com.alibaba.druid.support.http.stat.WebAppStat;
import com.alibaba.druid.support.profile.Profiler;
import com.alibaba.druid.support.http.stat.WebRequestStat;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.HandlerInterceptor;
import com.alibaba.druid.support.http.AbstractWebStatImpl;

public class StatHandlerInterceptor extends AbstractWebStatImpl implements HandlerInterceptor, InitializingBean, DisposableBean
{
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final WebAppStat webAppStat = this.getWebAppStat(request);
        String requestURI = this.getRequestURI(request);
        final long startNano = System.nanoTime();
        final long startMillis = System.currentTimeMillis();
        final WebRequestStat requestStat = new WebRequestStat(startNano, startMillis);
        WebRequestStat.set(requestStat);
        final WebSessionStat sessionStat = this.getSessionStat(request);
        webAppStat.beforeInvoke();
        WebURIStat uriStat = webAppStat.getURIStat(requestURI, false);
        if (uriStat == null) {
            final int index = requestURI.indexOf(";jsessionid=");
            if (index != -1) {
                requestURI = requestURI.substring(0, index);
                uriStat = webAppStat.getURIStat(requestURI, false);
            }
        }
        if (this.isProfileEnable()) {
            Profiler.initLocal();
            Profiler.enter(requestURI, "WEB");
        }
        if (uriStat != null) {
            uriStat.beforeInvoke();
        }
        if (sessionStat != null) {
            sessionStat.beforeInvoke();
        }
        return true;
    }
    
    public WebAppStat getWebAppStat(final HttpServletRequest request) {
        if (this.webAppStat != null) {
            return this.webAppStat;
        }
        final ServletContext context = request.getSession().getServletContext();
        final String contextPath = DruidWebUtils.getContextPath(context);
        return this.webAppStat = WebAppStatManager.getInstance().getWebAppStat(contextPath);
    }
    
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
    }
    
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception error) throws Exception {
        final WebRequestStat requestStat = WebRequestStat.current();
        final long endNano = System.nanoTime();
        requestStat.setEndNano(endNano);
        WebSessionStat sessionStat = this.getSessionStat(request);
        final WebURIStat uriStat = WebURIStat.current();
        final long nanos = endNano - requestStat.getStartNano();
        this.webAppStat.afterInvoke(null, nanos);
        if (sessionStat == null) {
            sessionStat = this.getSessionStat(request);
            if (sessionStat != null) {
                sessionStat.beforeInvoke();
            }
        }
        if (sessionStat != null) {
            sessionStat.afterInvoke(error, nanos);
            sessionStat.setPrincipal(this.getPrincipal(request));
        }
        if (uriStat != null) {
            uriStat.afterInvoke(error, nanos);
        }
        WebRequestStat.set(null);
        if (this.isProfileEnable()) {
            Profiler.release(nanos);
            final Map<ProfileEntryKey, ProfileEntryReqStat> requestStatsMap = Profiler.getStatsMap();
            if (uriStat != null) {
                uriStat.getProfiletat().record(requestStatsMap);
            }
            Profiler.removeLocal();
        }
    }
    
    @Override
    public String getRequestURI(final HttpServletRequest request) {
        return (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    }
    
    public void afterPropertiesSet() throws Exception {
        StatFilterContext.getInstance().addContextListener(this.statFilterContextListener);
    }
    
    public void destroy() throws Exception {
        StatFilterContext.getInstance().removeContextListener(this.statFilterContextListener);
    }
}
