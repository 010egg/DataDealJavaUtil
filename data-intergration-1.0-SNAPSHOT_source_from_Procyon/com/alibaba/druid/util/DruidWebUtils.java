// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class DruidWebUtils
{
    public static String getRemoteAddr(final HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && !isValidAddress(ip)) {
            ip = null;
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (ip != null && !isValidAddress(ip)) {
                ip = null;
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            if (ip != null && !isValidAddress(ip)) {
                ip = null;
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip != null && !isValidAddress(ip)) {
                ip = null;
            }
        }
        return ip;
    }
    
    private static boolean isValidAddress(final String ip) {
        if (ip == null) {
            return false;
        }
        for (int i = 0; i < ip.length(); ++i) {
            final char ch = ip.charAt(i);
            if (ch < '0' || ch > '9') {
                if (ch < 'A' || ch > 'F') {
                    if (ch < 'a' || ch > 'f') {
                        if (ch != '.') {
                            if (ch != ':') {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private static String getContextPath_2_5(final ServletContext context) {
        String contextPath = context.getContextPath();
        if (contextPath == null || contextPath.length() == 0) {
            contextPath = "/";
        }
        return contextPath;
    }
    
    public static String getContextPath(final ServletContext context) {
        if (context.getMajorVersion() == 2 && context.getMinorVersion() < 5) {
            return null;
        }
        try {
            return getContextPath_2_5(context);
        }
        catch (NoSuchMethodError error) {
            return null;
        }
    }
    
    public static Boolean getBoolean(final GenericServlet servlet, final String key) {
        final String property = servlet.getInitParameter(key);
        if ("true".equals(property)) {
            return Boolean.TRUE;
        }
        if ("false".equals(property)) {
            return Boolean.FALSE;
        }
        return null;
    }
}
