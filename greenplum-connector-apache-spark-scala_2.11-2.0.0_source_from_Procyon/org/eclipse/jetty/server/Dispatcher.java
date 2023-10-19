// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Enumeration;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.Attributes;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.DispatcherType;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import org.eclipse.jetty.server.handler.ContextHandler;
import javax.servlet.RequestDispatcher;

public class Dispatcher implements RequestDispatcher
{
    public static final String __INCLUDE_PREFIX = "javax.servlet.include.";
    public static final String __FORWARD_PREFIX = "javax.servlet.forward.";
    private final ContextHandler _contextHandler;
    private final String _uri;
    private final String _path;
    private final String _query;
    private final String _named;
    
    public Dispatcher(final ContextHandler contextHandler, final String uri, final String pathInContext, final String query) {
        this._contextHandler = contextHandler;
        this._uri = uri;
        this._path = pathInContext;
        this._query = query;
        this._named = null;
    }
    
    public Dispatcher(final ContextHandler contextHandler, final String name) throws IllegalStateException {
        this._contextHandler = contextHandler;
        this._named = name;
        this._uri = null;
        this._path = null;
        this._query = null;
    }
    
    @Override
    public void forward(final ServletRequest request, final ServletResponse response) throws ServletException, IOException {
        this.forward(request, response, DispatcherType.FORWARD);
    }
    
    public void error(final ServletRequest request, final ServletResponse response) throws ServletException, IOException {
        this.forward(request, response, DispatcherType.ERROR);
    }
    
    @Override
    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        final Request baseRequest = (Request)((request instanceof Request) ? request : HttpChannel.getCurrentHttpChannel().getRequest());
        if (!(request instanceof HttpServletRequest)) {
            request = new ServletRequestHttpWrapper(request);
        }
        if (!(response instanceof HttpServletResponse)) {
            response = new ServletResponseHttpWrapper(response);
        }
        final DispatcherType old_type = baseRequest.getDispatcherType();
        final Attributes old_attr = baseRequest.getAttributes();
        final MultiMap<String> old_query_params = baseRequest.getQueryParameters();
        try {
            baseRequest.setDispatcherType(DispatcherType.INCLUDE);
            baseRequest.getResponse().include();
            if (this._named != null) {
                this._contextHandler.handle(this._named, baseRequest, (HttpServletRequest)request, (HttpServletResponse)response);
            }
            else {
                final IncludeAttributes attr = new IncludeAttributes(old_attr);
                attr._requestURI = this._uri;
                attr._contextPath = this._contextHandler.getContextPath();
                attr._servletPath = null;
                attr._pathInfo = this._path;
                attr._query = this._query;
                if (this._query != null) {
                    baseRequest.mergeQueryParameters(this._query, false);
                }
                baseRequest.setAttributes(attr);
                this._contextHandler.handle(this._path, baseRequest, (HttpServletRequest)request, (HttpServletResponse)response);
            }
        }
        finally {
            baseRequest.setAttributes(old_attr);
            baseRequest.getResponse().included();
            baseRequest.setQueryParameters(old_query_params);
            baseRequest.resetParameters();
            baseRequest.setDispatcherType(old_type);
        }
    }
    
    protected void forward(ServletRequest request, ServletResponse response, final DispatcherType dispatch) throws ServletException, IOException {
        final Request baseRequest = (Request)((request instanceof Request) ? request : HttpChannel.getCurrentHttpChannel().getRequest());
        final Response base_response = baseRequest.getResponse();
        base_response.resetForForward();
        if (!(request instanceof HttpServletRequest)) {
            request = new ServletRequestHttpWrapper(request);
        }
        if (!(response instanceof HttpServletResponse)) {
            response = new ServletResponseHttpWrapper(response);
        }
        final boolean old_handled = baseRequest.isHandled();
        final String old_uri = baseRequest.getRequestURI();
        final String old_context_path = baseRequest.getContextPath();
        final String old_servlet_path = baseRequest.getServletPath();
        final String old_path_info = baseRequest.getPathInfo();
        final String old_query = baseRequest.getQueryString();
        final MultiMap<String> old_query_params = baseRequest.getQueryParameters();
        final Attributes old_attr = baseRequest.getAttributes();
        final DispatcherType old_type = baseRequest.getDispatcherType();
        try {
            baseRequest.setHandled(false);
            baseRequest.setDispatcherType(dispatch);
            if (this._named != null) {
                this._contextHandler.handle(this._named, baseRequest, (HttpServletRequest)request, (HttpServletResponse)response);
            }
            else {
                final ForwardAttributes attr = new ForwardAttributes(old_attr);
                if (old_attr.getAttribute("javax.servlet.forward.request_uri") != null) {
                    attr._pathInfo = (String)old_attr.getAttribute("javax.servlet.forward.path_info");
                    attr._query = (String)old_attr.getAttribute("javax.servlet.forward.query_string");
                    attr._requestURI = (String)old_attr.getAttribute("javax.servlet.forward.request_uri");
                    attr._contextPath = (String)old_attr.getAttribute("javax.servlet.forward.context_path");
                    attr._servletPath = (String)old_attr.getAttribute("javax.servlet.forward.servlet_path");
                }
                else {
                    attr._pathInfo = old_path_info;
                    attr._query = old_query;
                    attr._requestURI = old_uri;
                    attr._contextPath = old_context_path;
                    attr._servletPath = old_servlet_path;
                }
                baseRequest.setRequestURI(this._uri);
                baseRequest.setContextPath(this._contextHandler.getContextPath());
                baseRequest.setServletPath(null);
                baseRequest.setPathInfo(this._uri);
                if (this._query != null) {
                    baseRequest.mergeQueryParameters(this._query, true);
                }
                baseRequest.setAttributes(attr);
                this._contextHandler.handle(this._path, baseRequest, (HttpServletRequest)request, (HttpServletResponse)response);
                if (!baseRequest.getHttpChannelState().isAsync()) {
                    this.commitResponse(response, baseRequest);
                }
            }
        }
        finally {
            baseRequest.setHandled(old_handled);
            baseRequest.setRequestURI(old_uri);
            baseRequest.setContextPath(old_context_path);
            baseRequest.setServletPath(old_servlet_path);
            baseRequest.setPathInfo(old_path_info);
            baseRequest.setQueryString(old_query);
            baseRequest.setQueryParameters(old_query_params);
            baseRequest.resetParameters();
            baseRequest.setAttributes(old_attr);
            baseRequest.setDispatcherType(old_type);
        }
    }
    
    private void commitResponse(final ServletResponse response, final Request baseRequest) throws IOException {
        if (baseRequest.getResponse().isWriting()) {
            try {
                response.getWriter().close();
            }
            catch (IllegalStateException e) {
                response.getOutputStream().close();
            }
        }
        else {
            try {
                response.getOutputStream().close();
            }
            catch (IllegalStateException e) {
                response.getWriter().close();
            }
        }
    }
    
    private class ForwardAttributes implements Attributes
    {
        final Attributes _attr;
        String _requestURI;
        String _contextPath;
        String _servletPath;
        String _pathInfo;
        String _query;
        
        ForwardAttributes(final Attributes attributes) {
            this._attr = attributes;
        }
        
        @Override
        public Object getAttribute(final String key) {
            if (Dispatcher.this._named == null) {
                if (key.equals("javax.servlet.forward.path_info")) {
                    return this._pathInfo;
                }
                if (key.equals("javax.servlet.forward.request_uri")) {
                    return this._requestURI;
                }
                if (key.equals("javax.servlet.forward.servlet_path")) {
                    return this._servletPath;
                }
                if (key.equals("javax.servlet.forward.context_path")) {
                    return this._contextPath;
                }
                if (key.equals("javax.servlet.forward.query_string")) {
                    return this._query;
                }
            }
            if (key.startsWith("javax.servlet.include.")) {
                return null;
            }
            return this._attr.getAttribute(key);
        }
        
        @Override
        public Enumeration<String> getAttributeNames() {
            final HashSet<String> set = new HashSet<String>();
            final Enumeration<String> e = this._attr.getAttributeNames();
            while (e.hasMoreElements()) {
                final String name = e.nextElement();
                if (!name.startsWith("javax.servlet.include.") && !name.startsWith("javax.servlet.forward.")) {
                    set.add(name);
                }
            }
            if (Dispatcher.this._named == null) {
                if (this._pathInfo != null) {
                    set.add("javax.servlet.forward.path_info");
                }
                else {
                    set.remove("javax.servlet.forward.path_info");
                }
                set.add("javax.servlet.forward.request_uri");
                set.add("javax.servlet.forward.servlet_path");
                set.add("javax.servlet.forward.context_path");
                if (this._query != null) {
                    set.add("javax.servlet.forward.query_string");
                }
                else {
                    set.remove("javax.servlet.forward.query_string");
                }
            }
            return Collections.enumeration(set);
        }
        
        @Override
        public void setAttribute(final String key, final Object value) {
            if (Dispatcher.this._named == null && key.startsWith("javax.servlet.")) {
                if (key.equals("javax.servlet.forward.path_info")) {
                    this._pathInfo = (String)value;
                }
                else if (key.equals("javax.servlet.forward.request_uri")) {
                    this._requestURI = (String)value;
                }
                else if (key.equals("javax.servlet.forward.servlet_path")) {
                    this._servletPath = (String)value;
                }
                else if (key.equals("javax.servlet.forward.context_path")) {
                    this._contextPath = (String)value;
                }
                else if (key.equals("javax.servlet.forward.query_string")) {
                    this._query = (String)value;
                }
                else if (value == null) {
                    this._attr.removeAttribute(key);
                }
                else {
                    this._attr.setAttribute(key, value);
                }
            }
            else if (value == null) {
                this._attr.removeAttribute(key);
            }
            else {
                this._attr.setAttribute(key, value);
            }
        }
        
        @Override
        public String toString() {
            return "FORWARD+" + this._attr.toString();
        }
        
        @Override
        public void clearAttributes() {
            throw new IllegalStateException();
        }
        
        @Override
        public void removeAttribute(final String name) {
            this.setAttribute(name, null);
        }
    }
    
    private class IncludeAttributes implements Attributes
    {
        final Attributes _attr;
        String _requestURI;
        String _contextPath;
        String _servletPath;
        String _pathInfo;
        String _query;
        
        IncludeAttributes(final Attributes attributes) {
            this._attr = attributes;
        }
        
        @Override
        public Object getAttribute(final String key) {
            if (Dispatcher.this._named == null) {
                if (key.equals("javax.servlet.include.path_info")) {
                    return this._pathInfo;
                }
                if (key.equals("javax.servlet.include.servlet_path")) {
                    return this._servletPath;
                }
                if (key.equals("javax.servlet.include.context_path")) {
                    return this._contextPath;
                }
                if (key.equals("javax.servlet.include.query_string")) {
                    return this._query;
                }
                if (key.equals("javax.servlet.include.request_uri")) {
                    return this._requestURI;
                }
            }
            else if (key.startsWith("javax.servlet.include.")) {
                return null;
            }
            return this._attr.getAttribute(key);
        }
        
        @Override
        public Enumeration<String> getAttributeNames() {
            final HashSet<String> set = new HashSet<String>();
            final Enumeration<String> e = this._attr.getAttributeNames();
            while (e.hasMoreElements()) {
                final String name = e.nextElement();
                if (!name.startsWith("javax.servlet.include.")) {
                    set.add(name);
                }
            }
            if (Dispatcher.this._named == null) {
                if (this._pathInfo != null) {
                    set.add("javax.servlet.include.path_info");
                }
                else {
                    set.remove("javax.servlet.include.path_info");
                }
                set.add("javax.servlet.include.request_uri");
                set.add("javax.servlet.include.servlet_path");
                set.add("javax.servlet.include.context_path");
                if (this._query != null) {
                    set.add("javax.servlet.include.query_string");
                }
                else {
                    set.remove("javax.servlet.include.query_string");
                }
            }
            return Collections.enumeration(set);
        }
        
        @Override
        public void setAttribute(final String key, final Object value) {
            if (Dispatcher.this._named == null && key.startsWith("javax.servlet.")) {
                if (key.equals("javax.servlet.include.path_info")) {
                    this._pathInfo = (String)value;
                }
                else if (key.equals("javax.servlet.include.request_uri")) {
                    this._requestURI = (String)value;
                }
                else if (key.equals("javax.servlet.include.servlet_path")) {
                    this._servletPath = (String)value;
                }
                else if (key.equals("javax.servlet.include.context_path")) {
                    this._contextPath = (String)value;
                }
                else if (key.equals("javax.servlet.include.query_string")) {
                    this._query = (String)value;
                }
                else if (value == null) {
                    this._attr.removeAttribute(key);
                }
                else {
                    this._attr.setAttribute(key, value);
                }
            }
            else if (value == null) {
                this._attr.removeAttribute(key);
            }
            else {
                this._attr.setAttribute(key, value);
            }
        }
        
        @Override
        public String toString() {
            return "INCLUDE+" + this._attr.toString();
        }
        
        @Override
        public void clearAttributes() {
            throw new IllegalStateException();
        }
        
        @Override
        public void removeAttribute(final String name) {
            this.setAttribute(name, null);
        }
    }
}
