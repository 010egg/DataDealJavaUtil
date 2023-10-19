// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http;

import java.util.Iterator;
import com.alibaba.druid.support.http.util.IPAddress;
import javax.servlet.http.HttpSession;
import com.alibaba.druid.util.Utils;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.support.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.druid.support.http.util.IPRange;
import com.alibaba.druid.util.StringUtils;
import javax.servlet.ServletException;
import com.alibaba.druid.support.logging.Log;
import javax.servlet.http.HttpServlet;

public abstract class ResourceServlet extends HttpServlet
{
    private static final Log LOG;
    public static final String SESSION_USER_KEY = "druid-user";
    public static final String PARAM_NAME_USERNAME = "loginUsername";
    public static final String PARAM_NAME_PASSWORD = "loginPassword";
    public static final String PARAM_NAME_ALLOW = "allow";
    public static final String PARAM_NAME_DENY = "deny";
    public static final String PARAM_REMOTE_ADDR = "remoteAddress";
    protected final ResourceHandler handler;
    
    public ResourceServlet(final String resourcePath) {
        this.handler = new ResourceHandler(resourcePath);
    }
    
    public void init() throws ServletException {
        this.initAuthEnv();
    }
    
    private void initAuthEnv() {
        final String paramUserName = this.getInitParameter("loginUsername");
        if (!StringUtils.isEmpty(paramUserName)) {
            this.handler.username = paramUserName;
        }
        final String paramPassword = this.getInitParameter("loginPassword");
        if (!StringUtils.isEmpty(paramPassword)) {
            this.handler.password = paramPassword;
        }
        final String paramRemoteAddressHeader = this.getInitParameter("remoteAddress");
        if (!StringUtils.isEmpty(paramRemoteAddressHeader)) {
            this.handler.remoteAddressHeader = paramRemoteAddressHeader;
        }
        try {
            String param = this.getInitParameter("allow");
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
            final String msg = "initParameter config error, allow : " + this.getInitParameter("allow");
            ResourceServlet.LOG.error(msg, e);
        }
        try {
            String param = this.getInitParameter("deny");
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
            final String msg = "initParameter config error, deny : " + this.getInitParameter("deny");
            ResourceServlet.LOG.error(msg, e);
        }
    }
    
    public boolean isPermittedRequest(final String remoteAddress) {
        return this.handler.isPermittedRequest(remoteAddress);
    }
    
    protected String getFilePath(final String fileName) {
        return this.handler.resourcePath + fileName;
    }
    
    protected void returnResourceFile(final String fileName, final String uri, final HttpServletResponse response) throws ServletException, IOException {
        this.handler.returnResourceFile(fileName, uri, response);
    }
    
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String servletPath = request.getServletPath();
        this.handler.service(request, response, servletPath, new ProcessCallback() {
            @Override
            public String process(final String url) {
                return ResourceServlet.this.process(url);
            }
        });
    }
    
    public boolean ContainsUser(final HttpServletRequest request) {
        return this.handler.containsUser(request);
    }
    
    public boolean checkLoginParam(final HttpServletRequest request) {
        return this.handler.checkLoginParam(request);
    }
    
    public boolean isRequireAuth() {
        return this.handler.isRequireAuth();
    }
    
    public boolean isPermittedRequest(final HttpServletRequest request) {
        return this.handler.isPermittedRequest(request);
    }
    
    protected String getRemoteAddress(final HttpServletRequest request) {
        return this.handler.getRemoteAddress(request);
    }
    
    protected abstract String process(final String p0);
    
    static {
        LOG = LogFactory.getLog(ResourceServlet.class);
    }
    
    public static class ResourceHandler
    {
        protected String username;
        protected String password;
        protected List<IPRange> allowList;
        protected List<IPRange> denyList;
        protected String resourcePath;
        protected String remoteAddressHeader;
        
        public ResourceHandler(final String resourcePath) {
            this.username = null;
            this.password = null;
            this.allowList = new ArrayList<IPRange>();
            this.denyList = new ArrayList<IPRange>();
            this.remoteAddressHeader = null;
            this.resourcePath = resourcePath;
        }
        
        protected void returnResourceFile(final String fileName, final String uri, final HttpServletResponse response) throws ServletException, IOException {
            final String filePath = this.getFilePath(fileName);
            if (filePath.endsWith(".html")) {
                response.setContentType("text/html; charset=utf-8");
            }
            if (fileName.endsWith(".jpg")) {
                final byte[] bytes = Utils.readByteArrayFromResource(filePath);
                if (bytes != null) {
                    response.getOutputStream().write(bytes);
                }
                return;
            }
            final String text = Utils.readFromResource(filePath);
            if (text == null) {
                return;
            }
            if (fileName.endsWith(".css")) {
                response.setContentType("text/css;charset=utf-8");
            }
            else if (fileName.endsWith(".js")) {
                response.setContentType("text/javascript;charset=utf-8");
            }
            response.getWriter().write(text);
        }
        
        protected String getFilePath(final String fileName) {
            return this.resourcePath + fileName;
        }
        
        public boolean checkLoginParam(final HttpServletRequest request) {
            final String usernameParam = request.getParameter("loginUsername");
            final String passwordParam = request.getParameter("loginPassword");
            return null != this.username && null != this.password && (this.username.equals(usernameParam) && this.password.equals(passwordParam));
        }
        
        protected String getRemoteAddress(final HttpServletRequest request) {
            String remoteAddress = null;
            if (this.remoteAddressHeader != null) {
                remoteAddress = request.getHeader(this.remoteAddressHeader);
            }
            if (remoteAddress == null) {
                remoteAddress = request.getRemoteAddr();
            }
            return remoteAddress;
        }
        
        public boolean containsUser(final HttpServletRequest request) {
            final HttpSession session = request.getSession(false);
            return session != null && session.getAttribute("druid-user") != null;
        }
        
        public boolean isRequireAuth() {
            return this.username != null;
        }
        
        public boolean isPermittedRequest(final HttpServletRequest request) {
            final String remoteAddress = this.getRemoteAddress(request);
            return this.isPermittedRequest(remoteAddress);
        }
        
        public boolean isPermittedRequest(final String remoteAddress) {
            final boolean ipV6 = remoteAddress != null && remoteAddress.indexOf(58) != -1;
            if (ipV6) {
                return "0:0:0:0:0:0:0:1".equals(remoteAddress) || (this.denyList.size() == 0 && this.allowList.size() == 0);
            }
            final IPAddress ipAddress = new IPAddress(remoteAddress);
            for (final IPRange range : this.denyList) {
                if (range.isIPAddressInRange(ipAddress)) {
                    return false;
                }
            }
            if (this.allowList.size() > 0) {
                for (final IPRange range : this.allowList) {
                    if (range.isIPAddressInRange(ipAddress)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public void service(final HttpServletRequest request, final HttpServletResponse response, final String servletPath, final ProcessCallback processCallback) throws ServletException, IOException {
            String contextPath = request.getContextPath();
            final String requestURI = request.getRequestURI();
            response.setCharacterEncoding("utf-8");
            if (contextPath == null) {
                contextPath = "";
            }
            final String uri = contextPath + servletPath;
            String path = requestURI.substring(contextPath.length() + servletPath.length());
            if (!this.isPermittedRequest(request)) {
                path = "/nopermit.html";
                this.returnResourceFile(path, uri, response);
                return;
            }
            if ("/submitLogin".equals(path)) {
                final String usernameParam = request.getParameter("loginUsername");
                final String passwordParam = request.getParameter("loginPassword");
                if (this.username.equals(usernameParam) && this.password.equals(passwordParam)) {
                    request.getSession().setAttribute("druid-user", (Object)this.username);
                    response.getWriter().print("success");
                }
                else {
                    response.getWriter().print("error");
                }
                return;
            }
            if (this.isRequireAuth() && !this.containsUser(request) && !this.checkLoginParam(request) && !"/login.html".equals(path) && !path.startsWith("/css") && !path.startsWith("/js") && !path.startsWith("/img")) {
                if (contextPath.equals("") || contextPath.equals("/")) {
                    response.sendRedirect("/druid/login.html");
                }
                else if ("".equals(path)) {
                    response.sendRedirect("druid/login.html");
                }
                else {
                    response.sendRedirect("login.html");
                }
                return;
            }
            if ("".equals(path) || "/".equals(path)) {
                this.returnResourceFile("/index.html", uri, response);
                return;
            }
            if (path.contains(".json")) {
                String fullUrl = path;
                if (request.getQueryString() != null && request.getQueryString().length() > 0) {
                    fullUrl = fullUrl + "?" + request.getQueryString();
                }
                response.getWriter().print(processCallback.process(fullUrl));
                return;
            }
            this.returnResourceFile(path, uri, response);
        }
    }
    
    public interface ProcessCallback
    {
        String process(final String p0);
    }
}
