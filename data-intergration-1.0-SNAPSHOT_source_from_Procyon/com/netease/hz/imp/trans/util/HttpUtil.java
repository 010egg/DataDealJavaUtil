// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import org.slf4j.LoggerFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.NameValuePair;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;
import org.apache.http.StatusLine;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.commons.codec.digest.DigestUtils;
import javax.servlet.http.Cookie;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;

public class HttpUtil
{
    private static final Logger LOGGER;
    
    public static String getClientIpAddress(final HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !StringUtils.equalsIgnoreCase("unKnown", ip)) {
            final int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            }
            return ip;
        }
        else {
            ip = request.getHeader("X-Real-IP");
            if (StringUtils.isNotEmpty(ip) && !StringUtils.equalsIgnoreCase("unKnown", ip)) {
                return ip;
            }
            return request.getRemoteAddr();
        }
    }
    
    public static Cookie getCookieByName(final HttpServletRequest request, final String name) {
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (final Cookie cookie : cookies) {
                if (StringUtils.equalsIgnoreCase(name, cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }
    
    public static String getMd5(final String raw) {
        return DigestUtils.md5Hex(raw);
    }
    
    public static String postMethod(final String url, final String content) throws IOException {
        final HttpClient client = HttpConnectionManager.getHttpClient();
        final HttpPost post = new HttpPost(url);
        final StringEntity se = new StringEntity(content, "utf-8");
        se.setContentType("application/json");
        post.setEntity(se);
        final HttpResponse response = client.execute(post);
        final StatusLine sl = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        final String contentRep = EntityUtils.toString(entity, "utf-8");
        if (sl.getStatusCode() == 200) {
            return contentRep;
        }
        HttpUtil.LOGGER.warn("req url failed, url: {} ,retcode: {}", url, sl.getStatusCode());
        return null;
    }
    
    public static String postMethod(final String url, final String content, final Map<String, String> headers) throws IOException {
        final HttpClient client = HttpConnectionManager.getHttpClient();
        final HttpPost post = new HttpPost(url);
        for (final String key : headers.keySet()) {
            post.addHeader(key, headers.get(key));
        }
        final StringEntity se = new StringEntity(content, "utf-8");
        se.setContentType("application/json");
        post.setEntity(se);
        final HttpResponse response = client.execute(post);
        final StatusLine sl = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        final String contentrep = EntityUtils.toString(entity, "utf-8");
        if (sl.getStatusCode() == 200) {
            return contentrep;
        }
        HttpUtil.LOGGER.warn(String.format("req url failed, url: %s ,retcode: %d, content: %s", url, sl.getStatusCode(), contentrep));
        return null;
    }
    
    public static String postMethod(final String url, final List<NameValuePair> nvps) throws IOException {
        final HttpClient client = HttpConnectionManager.getHttpClient();
        final HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        final HttpResponse response = client.execute(post);
        final StatusLine sl = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        final String content = EntityUtils.toString(entity, "utf-8");
        if (sl.getStatusCode() == 200) {
            return content;
        }
        HttpUtil.LOGGER.warn("req url failed, url: {} ,retcode: {}, content: {}", url, sl.getStatusCode(), content);
        return null;
    }
    
    public static String postMethod(final String url, final Map<String, Object> params) throws IOException {
        final HttpClient client = HttpConnectionManager.getHttpClient();
        final HttpPost post = new HttpPost(url);
        final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (final String key : params.keySet()) {
            nvps.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
        post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        final HttpResponse response = client.execute(post);
        final StatusLine sl = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        final String content = EntityUtils.toString(entity, "utf-8");
        HttpUtil.LOGGER.debug("rsp: {}", content);
        if (sl.getStatusCode() == 200) {
            return content;
        }
        HttpUtil.LOGGER.warn("req url failed, url: {}, retcode: {}, content: {}", url, sl.getStatusCode(), content);
        return null;
    }
    
    public static String postMethod(final String url, final Map<String, Object> params, final Map<String, String> headers) throws IOException {
        final HttpClient client = HttpConnectionManager.getHttpClient();
        final HttpPost post = new HttpPost(url);
        for (final String key : headers.keySet()) {
            post.addHeader(key, headers.get(key));
        }
        final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (final String key2 : params.keySet()) {
            nvps.add(new BasicNameValuePair(key2, params.get(key2).toString()));
        }
        post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        final HttpResponse response = client.execute(post);
        final StatusLine sl = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        final String content = EntityUtils.toString(entity, "utf-8");
        HttpUtil.LOGGER.debug("rsp: {}", content);
        if (sl.getStatusCode() == 200) {
            return content;
        }
        HttpUtil.LOGGER.warn("req url failed, url: {}, retcode: {}, content: {}", url, sl.getStatusCode(), content);
        return null;
    }
    
    public static String getMethod(final String url) throws IOException {
        final HttpClient client = HttpConnectionManager.getHttpClient();
        final HttpGet get = new HttpGet(url);
        final HttpResponse response = client.execute(get);
        final StatusLine sl = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        final String content = EntityUtils.toString(entity, "utf-8");
        if (sl.getStatusCode() == 200) {
            return content;
        }
        HttpUtil.LOGGER.warn("req url failed, url: {} ,retcode: {}, content: {}", url, sl.getStatusCode(), content);
        return null;
    }
    
    public static String getMethod(final String url, final Map<String, String> headers) throws IOException {
        final HttpClient client = HttpConnectionManager.getHttpClient();
        final HttpGet get = new HttpGet(url);
        for (final String key : headers.keySet()) {
            get.addHeader(key, headers.get(key));
        }
        final HttpResponse response = client.execute(get);
        final StatusLine sl = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        final String content = EntityUtils.toString(entity, "utf-8");
        if (sl.getStatusCode() == 200) {
            return content;
        }
        HttpUtil.LOGGER.warn(String.format("req url failed, url: %s ,retcode: %d, content: %s", url, sl.getStatusCode(), content));
        return null;
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(HttpUtil.class);
    }
}
