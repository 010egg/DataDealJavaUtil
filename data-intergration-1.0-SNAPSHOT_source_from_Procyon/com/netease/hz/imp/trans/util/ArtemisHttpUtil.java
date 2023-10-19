// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.net.URLEncoder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.ByteBuffer;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import java.util.TreeMap;
import java.util.Collections;
import org.apache.commons.codec.binary.Base64;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import java.net.MalformedURLException;
import java.util.UUID;
import java.util.Date;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ClientConnectionManager;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import java.security.SecureRandom;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManager;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLContext;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpPost;
import java.net.URLDecoder;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;
import org.slf4j.Logger;
import java.util.List;

public class ArtemisHttpUtil
{
    private static final List<String> CUSTOM_HEADERS_TO_SIGN_PREFIX;
    private static final Logger logger;
    
    public static String doPostStringArtemis(final Map<String, String> path, final String body, final Map<String, String> querys, final String accept, final String contentType) {
        final String httpSchema = (String)path.keySet().toArray()[0];
        if (httpSchema != null && !StringUtils.isEmpty(httpSchema)) {
            String responseStr = null;
            try {
                final Map<String, String> headers = new HashMap<String, String>();
                if (StringUtils.isNotBlank(accept)) {
                    headers.put("Accept", accept);
                }
                else {
                    headers.put("Accept", "*/*");
                }
                if (StringUtils.isNotBlank(contentType)) {
                    headers.put("Content-Type", contentType);
                }
                else {
                    headers.put("Content-Type", "application/text;charset=UTF-8");
                }
                ArtemisHttpUtil.CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
                final Response response = httpPost("https://192.168.21.250:443", path.get(httpSchema), 1000, headers, querys, body, ArtemisHttpUtil.CUSTOM_HEADERS_TO_SIGN_PREFIX, "20656519", "QWoB9a30c5YjRHWBuuV6");
                responseStr = getResponseResult(response);
            }
            catch (Exception var10) {
                ArtemisHttpUtil.logger.error("the Artemis PostString Request is failed[doPostStringArtemis]", var10);
            }
            return responseStr;
        }
        throw new RuntimeException("http\u548chttps\u53c2\u6570\u9519\u8befhttpSchema: " + httpSchema);
    }
    
    public static Response httpPost(final String host, final String path, final int connectTimeout, Map<String, String> headers, final Map<String, String> querys, final String body, final List<String> signHeaderPrefixList, final String appKey, final String appSecret) throws Exception {
        final String contentType = headers.get("Content-Type");
        if ("application/x-www-form-urlencoded;charset=UTF-8".equals(contentType)) {
            final Map<String, String> paramMap = strToMap(body);
            final String modelDatas = paramMap.get("modelDatas");
            if (StringUtils.isNotBlank(modelDatas)) {
                paramMap.put("modelDatas", URLDecoder.decode(modelDatas));
            }
            headers = initialBasicHeader("POST", path, headers, querys, paramMap, signHeaderPrefixList, appKey, appSecret);
        }
        else {
            headers = initialBasicHeader("POST", path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);
        }
        final HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter("http.connection.timeout", (connectTimeout == 0) ? 1000 : connectTimeout);
        final HttpPost post = new HttpPost(initUrl(host, path, querys));
        for (final Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), utf8ToIso88591(e.getValue()));
        }
        if (StringUtils.isNotBlank(body)) {
            post.setEntity(new StringEntity(body, "UTF-8"));
        }
        return convert(httpClient.execute(post));
    }
    
    public static String utf8ToIso88591(final String str) {
        if (str == null) {
            return str;
        }
        try {
            return new String(str.getBytes("UTF-8"), "ISO-8859-1");
        }
        catch (UnsupportedEncodingException var2) {
            throw new RuntimeException(var2.getMessage(), var2);
        }
    }
    
    private static HttpClient wrapClient(final String host) {
        final HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }
        return httpClient;
    }
    
    private static void sslClient(final HttpClient httpClient) {
        try {
            final SSLContext ctx = SSLContext.getInstance("TLS");
            final X509TrustManager tm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                
                @Override
                public void checkClientTrusted(final X509Certificate[] xcs, final String str) {
                }
                
                @Override
                public void checkServerTrusted(final X509Certificate[] xcs, final String str) {
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            final SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            final ClientConnectionManager ccm = httpClient.getConnectionManager();
            final SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        }
        catch (KeyManagementException var6) {
            throw new RuntimeException(var6);
        }
        catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException(var7);
        }
    }
    
    private static Map<String, String> initialBasicHeader(final String method, final String path, Map<String, String> headers, final Map<String, String> querys, final Map<String, String> bodys, final List<String> signHeaderPrefixList, final String appKey, final String appSecret) throws MalformedURLException {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put("x-ca-timestamp", String.valueOf(new Date().getTime()));
        headers.put("x-ca-nonce", UUID.randomUUID().toString());
        headers.put("x-ca-key", appKey);
        headers.put("x-ca-signature", sign(appSecret, method, path, headers, querys, bodys, signHeaderPrefixList));
        return headers;
    }
    
    public static String sign(final String secret, final String method, final String path, final Map<String, String> headers, final Map<String, String> querys, final Map<String, String> bodys, final List<String> signHeaderPrefixList) {
        try {
            final Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            final byte[] keyBytes = secret.getBytes("UTF-8");
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));
            return new String(Base64.encodeBase64(hmacSha256.doFinal(buildStringToSign(method, path, headers, querys, bodys, signHeaderPrefixList).getBytes("UTF-8"))), "UTF-8");
        }
        catch (Exception var9) {
            throw new RuntimeException(var9);
        }
    }
    
    private static String buildStringToSign(final String method, final String path, final Map<String, String> headers, final Map<String, String> querys, final Map<String, String> bodys, final List<String> signHeaderPrefixList) {
        final StringBuilder sb = new StringBuilder();
        sb.append(method.toUpperCase()).append("\n");
        if (null != headers) {
            if (null != headers.get("Accept")) {
                sb.append(headers.get("Accept"));
                sb.append("\n");
            }
            if (null != headers.get("Content-MD5")) {
                sb.append(headers.get("Content-MD5"));
                sb.append("\n");
            }
            if (null != headers.get("Content-Type")) {
                sb.append(headers.get("Content-Type"));
                sb.append("\n");
            }
            if (null != headers.get("Date")) {
                sb.append(headers.get("Date"));
                sb.append("\n");
            }
        }
        sb.append(buildHeaders(headers, signHeaderPrefixList));
        sb.append(buildResource(path, querys, bodys));
        return sb.toString();
    }
    
    private static String buildHeaders(final Map<String, String> headers, final List<String> signHeaderPrefixList) {
        final StringBuilder sb = new StringBuilder();
        if (null != signHeaderPrefixList) {
            signHeaderPrefixList.remove("x-ca-signature");
            signHeaderPrefixList.remove("Accept");
            signHeaderPrefixList.remove("Content-MD5");
            signHeaderPrefixList.remove("Content-Type");
            signHeaderPrefixList.remove("Date");
            Collections.sort(signHeaderPrefixList);
        }
        if (null != headers) {
            final Map<String, String> sortMap = new TreeMap<String, String>();
            sortMap.putAll(headers);
            final StringBuilder signHeadersStringBuilder = new StringBuilder();
            for (final Map.Entry<String, String> header : sortMap.entrySet()) {
                if (isHeaderToSign(header.getKey(), signHeaderPrefixList)) {
                    sb.append(header.getKey());
                    sb.append(":");
                    if (!StringUtils.isBlank(header.getValue())) {
                        sb.append(header.getValue());
                    }
                    sb.append("\n");
                    if (0 < signHeadersStringBuilder.length()) {
                        signHeadersStringBuilder.append(",");
                    }
                    signHeadersStringBuilder.append(header.getKey());
                }
            }
            headers.put("x-ca-signature-headers", signHeadersStringBuilder.toString());
        }
        return sb.toString();
    }
    
    private static boolean isHeaderToSign(final String headerName, final List<String> signHeaderPrefixList) {
        if (StringUtils.isBlank(headerName)) {
            return false;
        }
        if (headerName.startsWith("x-ca-")) {
            return true;
        }
        if (null != signHeaderPrefixList) {
            for (final String signHeaderPrefix : signHeaderPrefixList) {
                if (headerName.equalsIgnoreCase(signHeaderPrefix)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static String buildResource(final String path, final Map<String, String> querys, final Map<String, String> bodys) {
        final StringBuilder sb = new StringBuilder();
        if (!StringUtils.isBlank(path)) {
            sb.append(path);
        }
        final Map<Object, Object> sortMap = new TreeMap<Object, Object>();
        if (null != querys) {
            for (final Map.Entry body : querys.entrySet()) {
                if (!StringUtils.isBlank(body.getKey())) {
                    sortMap.put(body.getKey(), body.getValue());
                }
            }
        }
        if (null != bodys) {
            for (final Map.Entry body : bodys.entrySet()) {
                if (!StringUtils.isBlank(body.getKey())) {
                    sortMap.put(body.getKey(), body.getValue());
                }
            }
        }
        final StringBuilder sbParam = new StringBuilder();
        for (final Map.Entry<String, String> item : sortMap.entrySet()) {
            if (!StringUtils.isBlank(item.getKey())) {
                if (0 < sbParam.length()) {
                    sbParam.append("&");
                }
                sbParam.append(item.getKey());
                if (StringUtils.isBlank(item.getValue())) {
                    continue;
                }
                sbParam.append("=").append(item.getValue());
            }
        }
        if (0 < sbParam.length()) {
            sb.append("?");
            sb.append((CharSequence)sbParam);
        }
        return sb.toString();
    }
    
    private static Map<String, String> strToMap(final String str) {
        final Map<String, String> map = new HashMap<String, String>();
        try {
            final String[] var3;
            final String[] params = var3 = str.split("&");
            for (int var4 = params.length, var5 = 0; var5 < var4; ++var5) {
                final String param = var3[var5];
                final String[] a = param.split("=");
                map.put(a[0], a[1]);
            }
            return map;
        }
        catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }
    
    private static String getResponseResult(final Response response) {
        String responseStr = null;
        final int statusCode = response.getStatusCode();
        if (!String.valueOf(statusCode).startsWith("2") && !String.valueOf(statusCode).startsWith("3")) {
            final String msg = response.getErrorMessage();
            responseStr = response.getBody();
            ArtemisHttpUtil.logger.error("the Artemis Request is Failed,statusCode:" + statusCode + " errorMsg:" + msg);
        }
        else {
            responseStr = response.getBody();
            ArtemisHttpUtil.logger.info("the Artemis Request is Success,statusCode:" + statusCode + " SuccessMsg:" + response.getBody());
        }
        return responseStr;
    }
    
    private static Response convert(final HttpResponse response) throws IOException {
        final Response res = new Response();
        if (null != response) {
            res.setStatusCode(response.getStatusLine().getStatusCode());
            for (final Header header : response.getAllHeaders()) {
                res.setHeader(header.getName(), iso88591ToUtf8(header.getValue()));
            }
            res.setContentType(res.getHeader("Content-Type"));
            res.setRequestId(res.getHeader("X-Ca-Request-Id"));
            res.setErrorMessage(res.getHeader("X-Ca-Error-Message"));
            if (response.getEntity() == null) {
                res.setBody(null);
            }
            else {
                res.setBody(readStreamAsStr(response.getEntity().getContent()));
            }
        }
        else {
            res.setStatusCode(500);
            res.setErrorMessage("No Response");
        }
        return res;
    }
    
    public static String iso88591ToUtf8(final String str) {
        if (str == null) {
            return str;
        }
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException var2) {
            throw new RuntimeException(var2.getMessage(), var2);
        }
    }
    
    public static String readStreamAsStr(final InputStream is) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final WritableByteChannel dest = Channels.newChannel(bos);
        final ReadableByteChannel src = Channels.newChannel(is);
        final ByteBuffer bb = ByteBuffer.allocate(4096);
        while (src.read(bb) != -1) {
            bb.flip();
            dest.write(bb);
            bb.clear();
        }
        src.close();
        dest.close();
        return new String(bos.toByteArray(), "UTF-8");
    }
    
    public static String initUrl(final String host, final String path, final Map<String, String> querys) throws UnsupportedEncodingException {
        final StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            final StringBuilder sbQuery = new StringBuilder();
            for (final Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (StringUtils.isBlank(query.getValue())) {
                        continue;
                    }
                    sbQuery.append("=");
                    sbQuery.append(URLEncoder.encode(query.getValue(), "UTF-8"));
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append((CharSequence)sbQuery);
            }
        }
        return sbUrl.toString();
    }
    
    static {
        CUSTOM_HEADERS_TO_SIGN_PREFIX = new ArrayList<String>();
        logger = LoggerFactory.getLogger(ArtemisHttpUtil.class);
    }
}
