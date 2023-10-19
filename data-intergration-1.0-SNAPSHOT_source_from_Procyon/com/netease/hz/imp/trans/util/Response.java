// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import java.util.HashMap;
import org.apache.http.HttpResponse;
import java.util.Map;

public class Response
{
    private int statusCode;
    private String contentType;
    private String requestId;
    private String errorMessage;
    private Map<String, String> headers;
    private String body;
    private HttpResponse response;
    
    public int getStatusCode() {
        return this.statusCode;
    }
    
    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }
    
    public String getContentType() {
        return this.contentType;
    }
    
    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }
    
    public String getRequestId() {
        return this.requestId;
    }
    
    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Map<String, String> getHeaders() {
        return this.headers;
    }
    
    public String getHeader(final String key) {
        return (null != this.headers) ? this.headers.get(key) : null;
    }
    
    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }
    
    public void setHeader(final String key, final String value) {
        if (null == this.headers) {
            this.headers = new HashMap<String, String>();
        }
        this.headers.put(key, value);
    }
    
    public String getBody() {
        return this.body;
    }
    
    public void setBody(final String body) {
        this.body = body;
    }
    
    public HttpResponse getResponse() {
        return this.response;
    }
    
    public void setResponse(final HttpResponse response) {
        this.response = response;
    }
}
