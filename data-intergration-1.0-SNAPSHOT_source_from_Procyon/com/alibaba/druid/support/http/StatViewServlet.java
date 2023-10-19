// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http;

import com.alibaba.druid.support.logging.LogFactory;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import java.util.Map;
import javax.management.remote.JMXConnectorFactory;
import java.util.HashMap;
import javax.management.remote.JMXServiceURL;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.management.MBeanServerConnection;
import com.alibaba.druid.stat.DruidStatService;
import com.alibaba.druid.support.logging.Log;

public class StatViewServlet extends ResourceServlet
{
    private static final Log LOG;
    private static final long serialVersionUID = 1L;
    public static final String PARAM_NAME_RESET_ENABLE = "resetEnable";
    public static final String PARAM_NAME_JMX_URL = "jmxUrl";
    public static final String PARAM_NAME_JMX_USERNAME = "jmxUsername";
    public static final String PARAM_NAME_JMX_PASSWORD = "jmxPassword";
    private DruidStatService statService;
    private String jmxUrl;
    private String jmxUsername;
    private String jmxPassword;
    private MBeanServerConnection conn;
    
    public StatViewServlet() {
        super("support/http/resources");
        this.statService = DruidStatService.getInstance();
        this.jmxUrl = null;
        this.jmxUsername = null;
        this.jmxPassword = null;
        this.conn = null;
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            String param = this.getInitParameter("resetEnable");
            if (param != null && param.trim().length() != 0) {
                param = param.trim();
                final boolean resetEnable = Boolean.parseBoolean(param);
                this.statService.setResetEnable(resetEnable);
            }
        }
        catch (Exception e) {
            final String msg = "initParameter config error, resetEnable : " + this.getInitParameter("resetEnable");
            StatViewServlet.LOG.error(msg, e);
        }
        String param = this.readInitParam("jmxUrl");
        if (param != null) {
            this.jmxUrl = param;
            this.jmxUsername = this.readInitParam("jmxUsername");
            this.jmxPassword = this.readInitParam("jmxPassword");
            try {
                this.initJmxConn();
            }
            catch (IOException e2) {
                StatViewServlet.LOG.error("init jmx connection error", e2);
            }
        }
    }
    
    private String readInitParam(final String key) {
        String value = null;
        try {
            String param = this.getInitParameter(key);
            if (param != null) {
                param = param.trim();
                if (param.length() > 0) {
                    value = param;
                }
            }
        }
        catch (Exception e) {
            final String msg = "initParameter config [" + key + "] error";
            StatViewServlet.LOG.warn(msg, e);
        }
        return value;
    }
    
    private void initJmxConn() throws IOException {
        if (this.jmxUrl != null) {
            final JMXServiceURL url = new JMXServiceURL(this.jmxUrl);
            Map<String, String[]> env = null;
            if (this.jmxUsername != null) {
                env = new HashMap<String, String[]>();
                final String[] credentials = { this.jmxUsername, this.jmxPassword };
                env.put("jmx.remote.credentials", credentials);
            }
            final JMXConnector jmxc = JMXConnectorFactory.connect(url, env);
            this.conn = jmxc.getMBeanServerConnection();
        }
    }
    
    private String getJmxResult(final MBeanServerConnection connetion, final String url) throws Exception {
        final ObjectName name = new ObjectName("com.alibaba.druid:type=DruidStatService");
        final String result = (String)this.conn.invoke(name, "service", new String[] { url }, new String[] { String.class.getName() });
        return result;
    }
    
    @Override
    protected String process(final String url) {
        String resp = null;
        if (this.jmxUrl == null) {
            resp = this.statService.service(url);
        }
        else if (this.conn == null) {
            try {
                this.initJmxConn();
            }
            catch (IOException e) {
                StatViewServlet.LOG.error("init jmx connection error", e);
                resp = DruidStatService.returnJSONResult(-1, "init jmx connection error" + e.getMessage());
            }
            if (this.conn != null) {
                try {
                    resp = this.getJmxResult(this.conn, url);
                }
                catch (Exception e2) {
                    StatViewServlet.LOG.error("get jmx data error", e2);
                    resp = DruidStatService.returnJSONResult(-1, "get data error:" + e2.getMessage());
                }
            }
        }
        else {
            try {
                resp = this.getJmxResult(this.conn, url);
            }
            catch (Exception e2) {
                StatViewServlet.LOG.error("get jmx data error", e2);
                resp = DruidStatService.returnJSONResult(-1, "get data error" + e2.getMessage());
            }
        }
        return resp;
    }
    
    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        final String servletPath = request.getServletPath();
        final String requestURI = request.getRequestURI();
        response.setCharacterEncoding("utf-8");
        if (contextPath == null) {
            contextPath = "";
        }
        final String uri = contextPath + servletPath;
        final String path = requestURI.substring(contextPath.length() + servletPath.length());
        if ("".equals(path)) {
            if (contextPath.equals("") || contextPath.equals("/")) {
                response.sendRedirect("/druid/index.html");
            }
            else {
                response.sendRedirect("druid/index.html");
            }
            return;
        }
        if ("/".equals(path)) {
            response.sendRedirect("index.html");
            return;
        }
        super.service(request, response);
    }
    
    static {
        LOG = LogFactory.getLog(StatViewServlet.class);
    }
}
