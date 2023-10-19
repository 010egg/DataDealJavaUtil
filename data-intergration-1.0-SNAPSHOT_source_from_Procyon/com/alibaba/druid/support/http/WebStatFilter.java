// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http;

import javax.servlet.http.HttpServletResponseWrapper;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.alibaba.druid.support.http.stat.WebAppStat;
import com.alibaba.druid.util.DruidWebUtils;
import com.alibaba.druid.filter.stat.StatFilterContextListener;
import com.alibaba.druid.filter.stat.StatFilterContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import javax.servlet.FilterConfig;
import java.util.Iterator;
import com.alibaba.druid.support.profile.ProfileEntryReqStat;
import com.alibaba.druid.support.profile.ProfileEntryKey;
import java.util.Map;
import com.alibaba.druid.support.http.stat.WebURIStat;
import com.alibaba.druid.support.http.stat.WebSessionStat;
import javax.servlet.ServletException;
import java.io.IOException;
import com.alibaba.druid.support.profile.Profiler;
import com.alibaba.druid.support.http.stat.WebRequestStat;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import com.alibaba.druid.util.ServletPathMatcher;
import java.util.Set;
import com.alibaba.druid.util.PatternMatcher;
import com.alibaba.druid.support.logging.Log;
import javax.servlet.Filter;

public class WebStatFilter extends AbstractWebStatImpl implements Filter
{
    private static final Log LOG;
    public static final String PARAM_NAME_PROFILE_ENABLE = "profileEnable";
    public static final String PARAM_NAME_SESSION_STAT_ENABLE = "sessionStatEnable";
    public static final String PARAM_NAME_SESSION_STAT_MAX_COUNT = "sessionStatMaxCount";
    public static final String PARAM_NAME_EXCLUSIONS = "exclusions";
    public static final String PARAM_NAME_PRINCIPAL_SESSION_NAME = "principalSessionName";
    public static final String PARAM_NAME_PRINCIPAL_COOKIE_NAME = "principalCookieName";
    public static final String PARAM_NAME_REAL_IP_HEADER = "realIpHeader";
    protected PatternMatcher pathMatcher;
    private Set<String> excludesPattern;
    
    public WebStatFilter() {
        this.pathMatcher = new ServletPathMatcher();
    }
    
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest)request;
        final HttpServletResponse httpResponse = (HttpServletResponse)response;
        final StatHttpServletResponseWrapper responseWrapper = new StatHttpServletResponseWrapper(httpResponse);
        String requestURI = this.getRequestURI(httpRequest);
        if (this.isExclusion(requestURI)) {
            chain.doFilter(request, response);
            return;
        }
        final long startNano = System.nanoTime();
        final long startMillis = System.currentTimeMillis();
        final WebRequestStat requestStat = new WebRequestStat(startNano, startMillis);
        WebRequestStat.set(requestStat);
        WebSessionStat sessionStat = this.getSessionStat(httpRequest);
        this.webAppStat.beforeInvoke();
        WebURIStat uriStat = this.webAppStat.getURIStat(requestURI, false);
        if (uriStat == null) {
            final int index = requestURI.indexOf(";jsessionid=");
            if (index != -1) {
                requestURI = requestURI.substring(0, index);
                uriStat = this.webAppStat.getURIStat(requestURI, false);
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
        Throwable error = null;
        try {
            chain.doFilter(request, (ServletResponse)responseWrapper);
        }
        catch (IOException e) {
            error = e;
            throw e;
        }
        catch (ServletException e2) {
            error = (Throwable)e2;
            throw e2;
        }
        catch (RuntimeException e3) {
            error = e3;
            throw e3;
        }
        catch (Error e4) {
            error = e4;
            throw e4;
        }
        finally {
            final long endNano = System.nanoTime();
            requestStat.setEndNano(endNano);
            final long nanos = endNano - startNano;
            this.webAppStat.afterInvoke(error, nanos);
            if (sessionStat == null) {
                sessionStat = this.getSessionStat(httpRequest);
                if (sessionStat != null) {
                    sessionStat.beforeInvoke();
                }
            }
            if (sessionStat != null) {
                sessionStat.afterInvoke(error, nanos);
                sessionStat.setPrincipal(this.getPrincipal(httpRequest));
            }
            if (uriStat == null) {
                final int status = responseWrapper.getStatus();
                if (status == 404) {
                    final String errorUrl = this.contextPath + "error_" + status;
                    uriStat = this.webAppStat.getURIStat(errorUrl, true);
                }
                else {
                    uriStat = this.webAppStat.getURIStat(requestURI, true);
                }
                if (uriStat != null) {
                    uriStat.beforeInvoke();
                }
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
    }
    
    public boolean isExclusion(String requestURI) {
        if (this.excludesPattern == null || requestURI == null) {
            return false;
        }
        if (this.contextPath != null && requestURI.startsWith(this.contextPath)) {
            requestURI = requestURI.substring(this.contextPath.length());
            if (!requestURI.startsWith("/")) {
                requestURI = "/" + requestURI;
            }
        }
        for (final String pattern : this.excludesPattern) {
            if (this.pathMatcher.matches(pattern, requestURI)) {
                return true;
            }
        }
        return false;
    }
    
    public void init(final FilterConfig config) throws ServletException {
        final String exclusions = config.getInitParameter("exclusions");
        if (exclusions != null && exclusions.trim().length() != 0) {
            this.excludesPattern = new HashSet<String>(Arrays.asList(exclusions.split("\\s*,\\s*")));
        }
        String param = config.getInitParameter("principalSessionName");
        if (param != null) {
            param = param.trim();
            if (param.length() != 0) {
                this.principalSessionName = param;
            }
        }
        param = config.getInitParameter("principalCookieName");
        if (param != null) {
            param = param.trim();
            if (param.length() != 0) {
                this.principalCookieName = param;
            }
        }
        param = config.getInitParameter("sessionStatEnable");
        if (param != null && param.trim().length() != 0) {
            param = param.trim();
            if ("true".equals(param)) {
                this.sessionStatEnable = true;
            }
            else if ("false".equals(param)) {
                this.sessionStatEnable = false;
            }
            else {
                WebStatFilter.LOG.error("WebStatFilter Parameter 'sessionStatEnable' config error");
            }
        }
        param = config.getInitParameter("profileEnable");
        if (param != null && param.trim().length() != 0) {
            param = param.trim();
            if ("true".equals(param)) {
                this.profileEnable = true;
            }
            else if ("false".equals(param)) {
                this.profileEnable = false;
            }
            else {
                WebStatFilter.LOG.error("WebStatFilter Parameter 'profileEnable' config error");
            }
        }
        param = config.getInitParameter("sessionStatMaxCount");
        if (param != null && param.trim().length() != 0) {
            param = param.trim();
            try {
                this.sessionStatMaxCount = Integer.parseInt(param);
            }
            catch (NumberFormatException e) {
                WebStatFilter.LOG.error("WebStatFilter Parameter 'sessionStatEnable' config error", e);
            }
        }
        param = config.getInitParameter("realIpHeader");
        if (param != null) {
            param = param.trim();
            if (param.length() != 0) {
                this.realIpHeader = param;
            }
        }
        StatFilterContext.getInstance().addContextListener(this.statFilterContextListener);
        this.contextPath = DruidWebUtils.getContextPath(config.getServletContext());
        if (this.webAppStat == null) {
            this.webAppStat = new WebAppStat(this.contextPath, this.sessionStatMaxCount);
        }
        WebAppStatManager.getInstance().addWebAppStatSet(this.webAppStat);
    }
    
    public void destroy() {
        StatFilterContext.getInstance().removeContextListener(this.statFilterContextListener);
        if (this.webAppStat != null) {
            WebAppStatManager.getInstance().remove(this.webAppStat);
        }
    }
    
    public void setWebAppStat(final WebAppStat webAppStat) {
        this.webAppStat = webAppStat;
    }
    
    public WebAppStat getWebAppStat() {
        return this.webAppStat;
    }
    
    public WebStatFilterContextListener getStatFilterContextListener() {
        return this.statFilterContextListener;
    }
    
    static {
        LOG = LogFactory.getLog(WebStatFilter.class);
    }
    
    public static final class StatHttpServletResponseWrapper extends HttpServletResponseWrapper implements HttpServletResponse
    {
        private int status;
        
        public StatHttpServletResponseWrapper(final HttpServletResponse response) {
            super(response);
            this.status = 200;
        }
        
        public void setStatus(final int statusCode) {
            super.setStatus(statusCode);
            this.status = statusCode;
        }
        
        public void setStatus(final int statusCode, final String statusMessage) {
            super.setStatus(statusCode, statusMessage);
            this.status = statusCode;
        }
        
        public void sendError(final int statusCode, final String statusMessage) throws IOException {
            super.sendError(statusCode, statusMessage);
            this.status = statusCode;
        }
        
        public void sendError(final int statusCode) throws IOException {
            super.sendError(statusCode);
            this.status = statusCode;
        }
        
        public int getStatus() {
            return this.status;
        }
    }
}
