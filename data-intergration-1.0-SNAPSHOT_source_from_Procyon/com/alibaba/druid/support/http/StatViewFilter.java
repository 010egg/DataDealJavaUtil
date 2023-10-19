// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http;

import com.alibaba.druid.support.logging.LogFactory;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletException;
import com.alibaba.druid.support.http.util.IPRange;
import com.alibaba.druid.util.StringUtils;
import javax.servlet.FilterConfig;
import com.alibaba.druid.stat.DruidStatService;
import com.alibaba.druid.support.logging.Log;
import javax.servlet.Filter;

public class StatViewFilter implements Filter
{
    public static final String PARAM_NAME_PATH = "path";
    private static final Log LOG;
    private String servletPath;
    private String resourcePath;
    private ResourceServlet.ResourceHandler handler;
    private DruidStatService statService;
    
    public StatViewFilter() {
        this.servletPath = "/druid";
        this.resourcePath = "support/http/resources";
        this.statService = DruidStatService.getInstance();
    }
    
    public void init(final FilterConfig config) throws ServletException {
        if (config == null) {
            return;
        }
        final String path = config.getInitParameter("path");
        if (path != null && !path.isEmpty()) {
            this.servletPath = path;
        }
        this.handler = new ResourceServlet.ResourceHandler(this.resourcePath);
        final String paramUserName = config.getInitParameter("loginUsername");
        if (!StringUtils.isEmpty(paramUserName)) {
            this.handler.username = paramUserName;
        }
        final String paramPassword = config.getInitParameter("loginPassword");
        if (!StringUtils.isEmpty(paramPassword)) {
            this.handler.password = paramPassword;
        }
        final String paramRemoteAddressHeader = config.getInitParameter("remoteAddress");
        if (!StringUtils.isEmpty(paramRemoteAddressHeader)) {
            this.handler.remoteAddressHeader = paramRemoteAddressHeader;
        }
        try {
            String param = config.getInitParameter("allow");
            if (param != null && param.trim().length() != 0) {
                param = param.trim();
                final String[] split;
                final String[] items = split = param.split(",");
                for (final String item : split) {
                    if (item != null) {
                        if (item.length() != 0) {
                            final IPRange ipRange = new IPRange(item);
                            this.handler.allowList.add(ipRange);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            final String msg = "initParameter config error, allow : " + config.getInitParameter("allow");
            StatViewFilter.LOG.error(msg, e);
        }
        try {
            String param = config.getInitParameter("deny");
            if (param != null && param.trim().length() != 0) {
                param = param.trim();
                final String[] split2;
                final String[] items = split2 = param.split(",");
                for (final String item : split2) {
                    if (item != null) {
                        if (item.length() != 0) {
                            final IPRange ipRange = new IPRange(item);
                            this.handler.denyList.add(ipRange);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            final String msg = "initParameter config error, deny : " + config.getInitParameter("deny");
            StatViewFilter.LOG.error(msg, e);
        }
    }
    
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpReq = (HttpServletRequest)request;
        final HttpServletResponse httpResp = (HttpServletResponse)response;
        final String contextPath = ((HttpServletRequest)request).getContextPath();
        String requestURI = httpReq.getRequestURI();
        if (!contextPath.equals("")) {
            requestURI = requestURI.substring(((HttpServletRequest)request).getContextPath().length());
        }
        if (requestURI.equals(this.servletPath)) {
            httpResp.sendRedirect(httpReq.getRequestURI() + '/');
        }
        this.handler.service(httpReq, httpResp, this.servletPath, new ResourceServlet.ProcessCallback() {
            @Override
            public String process(final String url) {
                return StatViewFilter.this.statService.service(url);
            }
        });
    }
    
    public void destroy() {
    }
    
    static {
        LOG = LogFactory.getLog(StatViewFilter.class);
    }
}
