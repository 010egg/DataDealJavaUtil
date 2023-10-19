// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http;

import com.alibaba.druid.support.http.stat.WebRequestStat;
import com.alibaba.druid.filter.stat.StatFilterContextListenerAdapter;
import com.alibaba.druid.support.logging.LogFactory;
import javax.servlet.http.Cookie;
import com.alibaba.druid.util.DruidWebUtils;
import javax.servlet.http.HttpSession;
import com.alibaba.druid.support.http.stat.WebSessionStat;
import javax.servlet.http.HttpServletRequest;
import com.alibaba.druid.support.http.stat.WebAppStat;
import com.alibaba.druid.support.logging.Log;

public class AbstractWebStatImpl
{
    private static final Log LOG;
    public static final int DEFAULT_MAX_STAT_SESSION_COUNT = 1000;
    protected WebAppStat webAppStat;
    protected boolean sessionStatEnable;
    protected int sessionStatMaxCount;
    protected boolean createSession;
    protected boolean profileEnable;
    protected String contextPath;
    protected String principalSessionName;
    protected String principalCookieName;
    protected String realIpHeader;
    protected WebStatFilterContextListener statFilterContextListener;
    
    public AbstractWebStatImpl() {
        this.webAppStat = null;
        this.sessionStatEnable = true;
        this.sessionStatMaxCount = 1000;
        this.createSession = false;
        this.profileEnable = false;
        this.statFilterContextListener = new WebStatFilterContextListener();
    }
    
    public boolean isSessionStatEnable() {
        return this.sessionStatEnable;
    }
    
    public void setSessionStatEnable(final boolean sessionStatEnable) {
        this.sessionStatEnable = sessionStatEnable;
    }
    
    public boolean isProfileEnable() {
        return this.profileEnable;
    }
    
    public void setProfileEnable(final boolean profileEnable) {
        this.profileEnable = profileEnable;
    }
    
    public String getContextPath() {
        return this.contextPath;
    }
    
    public int getSessionStatMaxCount() {
        return this.sessionStatMaxCount;
    }
    
    public String getRequestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }
    
    public String getPrincipalSessionName() {
        return this.principalSessionName;
    }
    
    public String getPrincipalCookieName() {
        return this.principalCookieName;
    }
    
    public WebSessionStat getSessionStat(final HttpServletRequest request) {
        if (!this.isSessionStatEnable()) {
            return null;
        }
        WebSessionStat sessionStat = null;
        final String sessionId = this.getSessionId(request);
        if (sessionId != null) {
            sessionStat = this.webAppStat.getSessionStat(sessionId, true);
        }
        if (sessionStat != null) {
            final long currentMillis = System.currentTimeMillis();
            final String userAgent = request.getHeader("user-agent");
            if (sessionStat.getCreateTimeMillis() == -1L) {
                final HttpSession session = request.getSession(false);
                if (session != null) {
                    sessionStat.setCreateTimeMillis(session.getCreationTime());
                }
                else {
                    sessionStat.setCreateTimeMillis(currentMillis);
                }
                this.webAppStat.computeUserAgent(userAgent);
                this.webAppStat.incrementSessionCount();
            }
            sessionStat.setUserAgent(userAgent);
            final String ip = this.getRemoteAddress(request);
            sessionStat.addRemoteAddress(ip);
        }
        return sessionStat;
    }
    
    protected String getRemoteAddress(final HttpServletRequest request) {
        String ip = null;
        if (this.realIpHeader != null && this.realIpHeader.length() != 0) {
            ip = request.getHeader(this.realIpHeader);
        }
        if (ip == null || ip.length() == 0) {
            ip = DruidWebUtils.getRemoteAddr(request);
        }
        return ip;
    }
    
    public String getSessionId(final HttpServletRequest httpRequest) {
        String sessionId = null;
        final HttpSession session = httpRequest.getSession(this.createSession);
        if (session != null) {
            sessionId = session.getId();
        }
        else {
            final Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null) {
                for (final Cookie cookie : cookies) {
                    if (cookie.getName().equals("JSESSIONID")) {
                        sessionId = cookie.getValue();
                        break;
                    }
                }
                if (sessionId == null) {
                    for (final Cookie cookie : cookies) {
                        if (cookie.getName().equals("JWT-SESSION")) {
                            sessionId = cookie.getValue();
                            break;
                        }
                    }
                }
            }
        }
        return sessionId;
    }
    
    public String getPrincipal(final HttpServletRequest httpRequest) {
        if (this.principalSessionName == null) {
            if (this.principalCookieName != null && httpRequest.getCookies() != null) {
                for (final Cookie cookie : httpRequest.getCookies()) {
                    if (this.principalCookieName.equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
            return null;
        }
        final HttpSession session = httpRequest.getSession(this.createSession);
        if (session == null) {
            return null;
        }
        Object sessionValue = null;
        try {
            sessionValue = session.getAttribute(this.principalSessionName);
        }
        catch (Exception ex) {
            if (AbstractWebStatImpl.LOG.isErrorEnabled()) {
                AbstractWebStatImpl.LOG.error("session.getAttribute error", ex);
            }
        }
        if (sessionValue == null) {
            return null;
        }
        return sessionValue.toString();
    }
    
    static {
        LOG = LogFactory.getLog(AbstractWebStatImpl.class);
    }
    
    public class WebStatFilterContextListener extends StatFilterContextListenerAdapter
    {
        @Override
        public void addUpdateCount(final int updateCount) {
            final WebRequestStat reqStat = WebRequestStat.current();
            if (reqStat != null) {
                reqStat.addJdbcUpdateCount(updateCount);
            }
        }
        
        @Override
        public void addFetchRowCount(final int fetchRowCount) {
            final WebRequestStat reqStat = WebRequestStat.current();
            if (reqStat != null) {
                reqStat.addJdbcFetchRowCount(fetchRowCount);
            }
        }
        
        @Override
        public void executeBefore(final String sql, final boolean inTransaction) {
            final WebRequestStat reqStat = WebRequestStat.current();
            if (reqStat != null) {
                reqStat.incrementJdbcExecuteCount();
            }
        }
        
        @Override
        public void executeAfter(final String sql, final long nanos, final Throwable error) {
            final WebRequestStat reqStat = WebRequestStat.current();
            if (reqStat != null) {
                reqStat.addJdbcExecuteTimeNano(nanos);
                if (error != null) {
                    reqStat.incrementJdbcExecuteErrorCount();
                }
            }
        }
        
        @Override
        public void commit() {
            final WebRequestStat reqStat = WebRequestStat.current();
            if (reqStat != null) {
                reqStat.incrementJdbcCommitCount();
            }
        }
        
        @Override
        public void rollback() {
            final WebRequestStat reqStat = WebRequestStat.current();
            if (reqStat != null) {
                reqStat.incrementJdbcRollbackCount();
            }
        }
        
        @Override
        public void pool_connect() {
            final WebRequestStat reqStat = WebRequestStat.current();
            if (reqStat != null) {
                reqStat.incrementJdbcPoolConnectCount();
            }
        }
        
        @Override
        public void pool_close(final long nanos) {
            final WebRequestStat reqStat = WebRequestStat.current();
            if (reqStat != null) {
                reqStat.incrementJdbcPoolCloseCount();
            }
        }
        
        @Override
        public void resultSet_open() {
            final WebRequestStat reqStat = WebRequestStat.current();
            if (reqStat != null) {
                reqStat.incrementJdbcResultSetOpenCount();
            }
        }
        
        @Override
        public void resultSet_close(final long nanos) {
            final WebRequestStat reqStat = WebRequestStat.current();
            if (reqStat != null) {
                reqStat.incrementJdbcResultSetCloseCount();
            }
        }
    }
}
