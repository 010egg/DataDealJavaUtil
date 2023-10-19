// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.util.MultiException;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import org.eclipse.jetty.util.log.Log;
import javax.servlet.http.HttpUpgradeHandler;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;
import org.eclipse.jetty.util.IO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.charset.Charset;
import java.util.HashMap;
import javax.servlet.ServletRequestAttributeEvent;
import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.server.session.AbstractSession;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import java.net.UnknownHostException;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.URIUtil;
import javax.servlet.RequestDispatcher;
import java.security.Principal;
import java.io.Reader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Iterator;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import org.eclipse.jetty.http.HttpHeader;
import javax.servlet.ServletRequest;
import org.eclipse.jetty.util.AttributesMap;
import java.util.Collections;
import java.util.Enumeration;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import java.io.InputStream;
import java.io.IOException;
import org.eclipse.jetty.util.UrlEncoded;
import org.eclipse.jetty.http.MimeTypes;
import java.io.UnsupportedEncodingException;
import javax.servlet.AsyncListener;
import java.util.EventListener;
import java.util.ArrayList;
import org.eclipse.jetty.util.MultiPartInputStreamParser;
import org.eclipse.jetty.http.HttpURI;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.net.InetSocketAddress;
import java.io.BufferedReader;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.http.HttpMethod;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.Attributes;
import javax.servlet.ServletRequestAttributeListener;
import java.util.List;
import org.eclipse.jetty.http.HttpFields;
import java.util.Locale;
import java.util.Collection;
import org.eclipse.jetty.util.log.Logger;
import javax.servlet.http.HttpServletRequest;

public class Request implements HttpServletRequest
{
    public static final String __MULTIPART_CONFIG_ELEMENT = "org.eclipse.jetty.multipartConfig";
    public static final String __MULTIPART_INPUT_STREAM = "org.eclipse.jetty.multiPartInputStream";
    public static final String __MULTIPART_CONTEXT = "org.eclipse.jetty.multiPartContext";
    private static final Logger LOG;
    private static final Collection<Locale> __defaultLocale;
    private static final int __NONE = 0;
    private static final int _STREAM = 1;
    private static final int __READER = 2;
    private final HttpChannel<?> _channel;
    private final HttpFields _fields;
    private final List<ServletRequestAttributeListener> _requestAttributeListeners;
    private final HttpInput<?> _input;
    private boolean _secure;
    private boolean _asyncSupported;
    private boolean _newContext;
    private boolean _cookiesExtracted;
    private boolean _handled;
    private boolean _paramsExtracted;
    private boolean _requestedSessionIdFromCookie;
    private volatile Attributes _attributes;
    private Authentication _authentication;
    private String _characterEncoding;
    private ContextHandler.Context _context;
    private String _contextPath;
    private CookieCutter _cookies;
    private DispatcherType _dispatcherType;
    private int _inputState;
    private HttpMethod _httpMethod;
    private String _httpMethodString;
    private MultiMap<String> _queryParameters;
    private MultiMap<String> _contentParameters;
    private MultiMap<String> _parameters;
    private String _pathInfo;
    private int _port;
    private HttpVersion _httpVersion;
    private String _queryEncoding;
    private String _queryString;
    private BufferedReader _reader;
    private String _readerEncoding;
    private InetSocketAddress _remote;
    private String _requestedSessionId;
    private String _requestURI;
    private Map<Object, HttpSession> _savedNewSessions;
    private String _scheme;
    private UserIdentity.Scope _scope;
    private String _serverName;
    private String _servletPath;
    private HttpSession _session;
    private SessionManager _sessionManager;
    private long _timeStamp;
    private HttpURI _uri;
    private MultiPartInputStreamParser _multiPartInputStream;
    private AsyncContextState _async;
    
    public Request(final HttpChannel<?> channel, final HttpInput<?> input) {
        this._fields = new HttpFields();
        this._requestAttributeListeners = new ArrayList<ServletRequestAttributeListener>();
        this._asyncSupported = true;
        this._cookiesExtracted = false;
        this._handled = false;
        this._requestedSessionIdFromCookie = false;
        this._inputState = 0;
        this._httpVersion = HttpVersion.HTTP_1_1;
        this._scheme = "http";
        this._channel = channel;
        this._input = input;
    }
    
    public HttpFields getHttpFields() {
        return this._fields;
    }
    
    public HttpInput<?> getHttpInput() {
        return this._input;
    }
    
    public void addEventListener(final EventListener listener) {
        if (listener instanceof ServletRequestAttributeListener) {
            this._requestAttributeListeners.add((ServletRequestAttributeListener)listener);
        }
        if (listener instanceof AsyncListener) {
            throw new IllegalArgumentException(listener.getClass().toString());
        }
    }
    
    public void extractParameters() {
        if (this._paramsExtracted) {
            return;
        }
        this._paramsExtracted = true;
        if (this._queryParameters == null) {
            this._queryParameters = this.extractQueryParameters();
        }
        if (this._contentParameters == null) {
            this._contentParameters = this.extractContentParameters();
        }
        this._parameters = this.restoreParameters();
    }
    
    private MultiMap<String> extractQueryParameters() {
        final MultiMap<String> result = new MultiMap<String>();
        if (this._uri != null && this._uri.hasQuery()) {
            if (this._queryEncoding == null) {
                this._uri.decodeQueryTo(result);
            }
            else {
                try {
                    this._uri.decodeQueryTo(result, this._queryEncoding);
                }
                catch (UnsupportedEncodingException e) {
                    if (Request.LOG.isDebugEnabled()) {
                        Request.LOG.warn(e);
                    }
                    else {
                        Request.LOG.warn(e.toString(), new Object[0]);
                    }
                }
            }
        }
        return result;
    }
    
    private MultiMap<String> extractContentParameters() {
        final MultiMap<String> result = new MultiMap<String>();
        String contentType = this.getContentType();
        if (contentType != null && !contentType.isEmpty()) {
            contentType = HttpFields.valueParameters(contentType, null);
            final int contentLength = this.getContentLength();
            if (contentLength != 0) {
                if (MimeTypes.Type.FORM_ENCODED.is(contentType) && this._inputState == 0 && (HttpMethod.POST.is(this.getMethod()) || HttpMethod.PUT.is(this.getMethod()))) {
                    this.extractFormParameters(result);
                }
                else if (contentType.startsWith("multipart/form-data") && this.getAttribute("org.eclipse.jetty.multipartConfig") != null && this._multiPartInputStream == null) {
                    this.extractMultipartParameters(result);
                }
            }
        }
        return result;
    }
    
    public void extractFormParameters(final MultiMap<String> params) {
        try {
            int maxFormContentSize = -1;
            int maxFormKeys = -1;
            if (this._context != null) {
                maxFormContentSize = this._context.getContextHandler().getMaxFormContentSize();
                maxFormKeys = this._context.getContextHandler().getMaxFormKeys();
            }
            if (maxFormContentSize < 0) {
                final Object obj = this._channel.getServer().getAttribute("org.eclipse.jetty.server.Request.maxFormContentSize");
                if (obj == null) {
                    maxFormContentSize = 200000;
                }
                else if (obj instanceof Number) {
                    final Number size = (Number)obj;
                    maxFormContentSize = size.intValue();
                }
                else if (obj instanceof String) {
                    maxFormContentSize = Integer.valueOf((String)obj);
                }
            }
            if (maxFormKeys < 0) {
                final Object obj = this._channel.getServer().getAttribute("org.eclipse.jetty.server.Request.maxFormKeys");
                if (obj == null) {
                    maxFormKeys = 1000;
                }
                else if (obj instanceof Number) {
                    final Number keys = (Number)obj;
                    maxFormKeys = keys.intValue();
                }
                else if (obj instanceof String) {
                    maxFormKeys = Integer.valueOf((String)obj);
                }
            }
            final int contentLength = this.getContentLength();
            if (contentLength > maxFormContentSize && maxFormContentSize > 0) {
                throw new IllegalStateException("Form too large: " + contentLength + " > " + maxFormContentSize);
            }
            final InputStream in = this.getInputStream();
            if (this._input.isAsync()) {
                throw new IllegalStateException("Cannot extract parameters with async IO");
            }
            UrlEncoded.decodeTo(in, params, this.getCharacterEncoding(), (contentLength < 0) ? maxFormContentSize : -1, maxFormKeys);
        }
        catch (IOException e) {
            if (Request.LOG.isDebugEnabled()) {
                Request.LOG.warn(e);
            }
            else {
                Request.LOG.warn(e.toString(), new Object[0]);
            }
        }
    }
    
    private void extractMultipartParameters(final MultiMap<String> result) {
        try {
            this.getParts(result);
        }
        catch (IOException | ServletException ex2) {
            final Exception ex;
            final Exception e = ex;
            Request.LOG.warn(e);
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public AsyncContext getAsyncContext() {
        final HttpChannelState state = this.getHttpChannelState();
        if (this._async == null || !state.isAsyncStarted()) {
            throw new IllegalStateException(state.getStatusString());
        }
        return this._async;
    }
    
    public HttpChannelState getHttpChannelState() {
        return this._channel.getState();
    }
    
    @Override
    public Object getAttribute(final String name) {
        if (name.startsWith("org.eclipse.jetty")) {
            if ("org.eclipse.jetty.server.Server".equals(name)) {
                return this._channel.getServer();
            }
            if ("org.eclipse.jetty.server.HttpChannel".equals(name)) {
                return this._channel;
            }
            if ("org.eclipse.jetty.server.HttpConnection".equals(name) && this._channel.getHttpTransport() instanceof HttpConnection) {
                return this._channel.getHttpTransport();
            }
        }
        return (this._attributes == null) ? null : this._attributes.getAttribute(name);
    }
    
    @Override
    public Enumeration<String> getAttributeNames() {
        if (this._attributes == null) {
            return Collections.enumeration((Collection<String>)Collections.emptyList());
        }
        return AttributesMap.getAttributeNamesCopy(this._attributes);
    }
    
    public Attributes getAttributes() {
        if (this._attributes == null) {
            this._attributes = new AttributesMap();
        }
        return this._attributes;
    }
    
    public Authentication getAuthentication() {
        return this._authentication;
    }
    
    @Override
    public String getAuthType() {
        if (this._authentication instanceof Authentication.Deferred) {
            this.setAuthentication(((Authentication.Deferred)this._authentication).authenticate(this));
        }
        if (this._authentication instanceof Authentication.User) {
            return ((Authentication.User)this._authentication).getAuthMethod();
        }
        return null;
    }
    
    @Override
    public String getCharacterEncoding() {
        return this._characterEncoding;
    }
    
    public HttpChannel<?> getHttpChannel() {
        return this._channel;
    }
    
    @Override
    public int getContentLength() {
        return (int)this._fields.getLongField(HttpHeader.CONTENT_LENGTH.toString());
    }
    
    @Override
    public long getContentLengthLong() {
        return this._fields.getLongField(HttpHeader.CONTENT_LENGTH.toString());
    }
    
    public long getContentRead() {
        return this._input.getContentRead();
    }
    
    @Override
    public String getContentType() {
        return this._fields.getStringField(HttpHeader.CONTENT_TYPE);
    }
    
    public ContextHandler.Context getContext() {
        return this._context;
    }
    
    @Override
    public String getContextPath() {
        return this._contextPath;
    }
    
    @Override
    public Cookie[] getCookies() {
        if (this._cookiesExtracted) {
            if (this._cookies == null || this._cookies.getCookies().length == 0) {
                return null;
            }
            return this._cookies.getCookies();
        }
        else {
            this._cookiesExtracted = true;
            final Enumeration<?> enm = this._fields.getValues(HttpHeader.COOKIE.toString());
            if (enm != null) {
                if (this._cookies == null) {
                    this._cookies = new CookieCutter();
                }
                while (enm.hasMoreElements()) {
                    final String c = (String)enm.nextElement();
                    this._cookies.addCookieField(c);
                }
            }
            if (this._cookies == null || this._cookies.getCookies().length == 0) {
                return null;
            }
            return this._cookies.getCookies();
        }
    }
    
    @Override
    public long getDateHeader(final String name) {
        return this._fields.getDateField(name);
    }
    
    @Override
    public DispatcherType getDispatcherType() {
        return this._dispatcherType;
    }
    
    @Override
    public String getHeader(final String name) {
        return this._fields.getStringField(name);
    }
    
    @Override
    public Enumeration<String> getHeaderNames() {
        return this._fields.getFieldNames();
    }
    
    @Override
    public Enumeration<String> getHeaders(final String name) {
        final Enumeration<String> e = this._fields.getValues(name);
        if (e == null) {
            return Collections.enumeration((Collection<String>)Collections.emptyList());
        }
        return e;
    }
    
    public int getInputState() {
        return this._inputState;
    }
    
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this._inputState != 0 && this._inputState != 1) {
            throw new IllegalStateException("READER");
        }
        this._inputState = 1;
        if (this._channel.isExpecting100Continue()) {
            this._channel.continue100(this._input.available());
        }
        return this._input;
    }
    
    @Override
    public int getIntHeader(final String name) {
        return (int)this._fields.getLongField(name);
    }
    
    @Override
    public Locale getLocale() {
        final Enumeration<String> enm = this._fields.getValues(HttpHeader.ACCEPT_LANGUAGE.toString(), ", \t");
        if (enm == null || !enm.hasMoreElements()) {
            return Locale.getDefault();
        }
        final List<?> acceptLanguage = HttpFields.qualityList(enm);
        if (acceptLanguage.size() == 0) {
            return Locale.getDefault();
        }
        final int size = acceptLanguage.size();
        if (size > 0) {
            String language = (String)acceptLanguage.get(0);
            language = HttpFields.valueParameters(language, null);
            String country = "";
            final int dash = language.indexOf(45);
            if (dash > -1) {
                country = language.substring(dash + 1).trim();
                language = language.substring(0, dash).trim();
            }
            return new Locale(language, country);
        }
        return Locale.getDefault();
    }
    
    @Override
    public Enumeration<Locale> getLocales() {
        final Enumeration<String> enm = this._fields.getValues(HttpHeader.ACCEPT_LANGUAGE.toString(), ", \t");
        if (enm == null || !enm.hasMoreElements()) {
            return Collections.enumeration(Request.__defaultLocale);
        }
        final List<String> acceptLanguage = HttpFields.qualityList(enm);
        if (acceptLanguage.size() == 0) {
            return Collections.enumeration(Request.__defaultLocale);
        }
        final List<Locale> langs = new ArrayList<Locale>();
        for (String language : acceptLanguage) {
            language = HttpFields.valueParameters(language, null);
            String country = "";
            final int dash = language.indexOf(45);
            if (dash > -1) {
                country = language.substring(dash + 1).trim();
                language = language.substring(0, dash).trim();
            }
            langs.add(new Locale(language, country));
        }
        if (langs.size() == 0) {
            return Collections.enumeration(Request.__defaultLocale);
        }
        return Collections.enumeration(langs);
    }
    
    @Override
    public String getLocalAddr() {
        final InetSocketAddress local = this._channel.getLocalAddress();
        if (local == null) {
            return "";
        }
        final InetAddress address = local.getAddress();
        if (address == null) {
            return local.getHostString();
        }
        return address.getHostAddress();
    }
    
    @Override
    public String getLocalName() {
        final InetSocketAddress local = this._channel.getLocalAddress();
        return local.getHostString();
    }
    
    @Override
    public int getLocalPort() {
        final InetSocketAddress local = this._channel.getLocalAddress();
        return local.getPort();
    }
    
    @Override
    public String getMethod() {
        return this._httpMethodString;
    }
    
    @Override
    public String getParameter(final String name) {
        if (!this._paramsExtracted) {
            this.extractParameters();
        }
        if (this._parameters == null) {
            this._parameters = this.restoreParameters();
        }
        return this._parameters.getValue(name, 0);
    }
    
    @Override
    public Map<String, String[]> getParameterMap() {
        if (!this._paramsExtracted) {
            this.extractParameters();
        }
        if (this._parameters == null) {
            this._parameters = this.restoreParameters();
        }
        return Collections.unmodifiableMap((Map<? extends String, ? extends String[]>)this._parameters.toStringArrayMap());
    }
    
    @Override
    public Enumeration<String> getParameterNames() {
        if (!this._paramsExtracted) {
            this.extractParameters();
        }
        if (this._parameters == null) {
            this._parameters = this.restoreParameters();
        }
        return Collections.enumeration((Collection<String>)this._parameters.keySet());
    }
    
    @Override
    public String[] getParameterValues(final String name) {
        if (!this._paramsExtracted) {
            this.extractParameters();
        }
        if (this._parameters == null) {
            this._parameters = this.restoreParameters();
        }
        final List<String> vals = this._parameters.getValues(name);
        if (vals == null) {
            return null;
        }
        return vals.toArray(new String[vals.size()]);
    }
    
    private MultiMap<String> restoreParameters() {
        final MultiMap<String> result = new MultiMap<String>();
        if (this._queryParameters == null) {
            this._queryParameters = this.extractQueryParameters();
        }
        result.addAllValues(this._queryParameters);
        result.addAllValues(this._contentParameters);
        return result;
    }
    
    public MultiMap<String> getQueryParameters() {
        return this._queryParameters;
    }
    
    public void setQueryParameters(final MultiMap<String> queryParameters) {
        this._queryParameters = queryParameters;
    }
    
    public void setContentParameters(final MultiMap<String> contentParameters) {
        this._contentParameters = contentParameters;
    }
    
    public void resetParameters() {
        this._parameters = null;
    }
    
    @Override
    public String getPathInfo() {
        return this._pathInfo;
    }
    
    @Override
    public String getPathTranslated() {
        if (this._pathInfo == null || this._context == null) {
            return null;
        }
        return this._context.getRealPath(this._pathInfo);
    }
    
    @Override
    public String getProtocol() {
        return this._httpVersion.toString();
    }
    
    public HttpVersion getHttpVersion() {
        return this._httpVersion;
    }
    
    public String getQueryEncoding() {
        return this._queryEncoding;
    }
    
    @Override
    public String getQueryString() {
        if (this._queryString == null && this._uri != null) {
            if (this._queryEncoding == null) {
                this._queryString = this._uri.getQuery();
            }
            else {
                this._queryString = this._uri.getQuery(this._queryEncoding);
            }
        }
        return this._queryString;
    }
    
    @Override
    public BufferedReader getReader() throws IOException {
        if (this._inputState != 0 && this._inputState != 2) {
            throw new IllegalStateException("STREAMED");
        }
        if (this._inputState == 2) {
            return this._reader;
        }
        String encoding = this.getCharacterEncoding();
        if (encoding == null) {
            encoding = "ISO-8859-1";
        }
        if (this._reader == null || !encoding.equalsIgnoreCase(this._readerEncoding)) {
            final ServletInputStream in = this.getInputStream();
            this._readerEncoding = encoding;
            this._reader = new BufferedReader(new InputStreamReader(in, encoding)) {
                @Override
                public void close() throws IOException {
                    in.close();
                }
            };
        }
        this._inputState = 2;
        return this._reader;
    }
    
    @Override
    public String getRealPath(final String path) {
        if (this._context == null) {
            return null;
        }
        return this._context.getRealPath(path);
    }
    
    public InetSocketAddress getRemoteInetSocketAddress() {
        InetSocketAddress remote = this._remote;
        if (remote == null) {
            remote = this._channel.getRemoteAddress();
        }
        return remote;
    }
    
    @Override
    public String getRemoteAddr() {
        InetSocketAddress remote = this._remote;
        if (remote == null) {
            remote = this._channel.getRemoteAddress();
        }
        if (remote == null) {
            return "";
        }
        final InetAddress address = remote.getAddress();
        if (address == null) {
            return remote.getHostString();
        }
        return address.getHostAddress();
    }
    
    @Override
    public String getRemoteHost() {
        InetSocketAddress remote = this._remote;
        if (remote == null) {
            remote = this._channel.getRemoteAddress();
        }
        return (remote == null) ? "" : remote.getHostString();
    }
    
    @Override
    public int getRemotePort() {
        InetSocketAddress remote = this._remote;
        if (remote == null) {
            remote = this._channel.getRemoteAddress();
        }
        return (remote == null) ? 0 : remote.getPort();
    }
    
    @Override
    public String getRemoteUser() {
        final Principal p = this.getUserPrincipal();
        if (p == null) {
            return null;
        }
        return p.getName();
    }
    
    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        if (path == null || this._context == null) {
            return null;
        }
        if (!path.startsWith("/")) {
            String relTo = URIUtil.addPaths(this._servletPath, this._pathInfo);
            final int slash = relTo.lastIndexOf("/");
            if (slash > 1) {
                relTo = relTo.substring(0, slash + 1);
            }
            else {
                relTo = "/";
            }
            path = URIUtil.addPaths(relTo, path);
        }
        return this._context.getRequestDispatcher(path);
    }
    
    @Override
    public String getRequestedSessionId() {
        return this._requestedSessionId;
    }
    
    @Override
    public String getRequestURI() {
        if (this._requestURI == null && this._uri != null) {
            this._requestURI = this._uri.getPathAndParam();
        }
        return this._requestURI;
    }
    
    @Override
    public StringBuffer getRequestURL() {
        final StringBuffer url = new StringBuffer(128);
        URIUtil.appendSchemeHostPort(url, this.getScheme(), this.getServerName(), this.getServerPort());
        url.append(this.getRequestURI());
        return url;
    }
    
    public Response getResponse() {
        return this._channel.getResponse();
    }
    
    public StringBuilder getRootURL() {
        final StringBuilder url = new StringBuilder(128);
        URIUtil.appendSchemeHostPort(url, this.getScheme(), this.getServerName(), this.getServerPort());
        return url;
    }
    
    @Override
    public String getScheme() {
        return this._scheme;
    }
    
    @Override
    public String getServerName() {
        if (this._serverName != null) {
            return this._serverName;
        }
        if (this._uri == null) {
            throw new IllegalStateException("No uri");
        }
        this._serverName = this._uri.getHost();
        if (this._serverName != null) {
            this._port = this._uri.getPort();
            return this._serverName;
        }
        final String hostPort = this._fields.getStringField(HttpHeader.HOST);
        this._port = 0;
        if (hostPort != null) {
            int i;
            int len = i = hostPort.length();
        Label_0188:
            while (i-- > 0) {
                final char c2 = (char)('\u00ff' & hostPort.charAt(i));
                switch (c2) {
                    case ']': {
                        break Label_0188;
                    }
                    case ':': {
                        try {
                            len = i;
                            this._port = StringUtil.toInt(hostPort.substring(i + 1));
                            break Label_0188;
                        }
                        catch (NumberFormatException e) {
                            Request.LOG.warn(e);
                            this._serverName = hostPort;
                            this._port = 0;
                            return this._serverName;
                        }
                        continue;
                    }
                }
            }
            if (hostPort.charAt(0) == '[') {
                if (hostPort.charAt(len - 1) != ']') {
                    Request.LOG.warn("Bad IPv6 " + hostPort, new Object[0]);
                    this._serverName = hostPort;
                    this._port = 0;
                    return this._serverName;
                }
                this._serverName = hostPort.substring(0, len);
            }
            else if (len == hostPort.length()) {
                this._serverName = hostPort;
            }
            else {
                this._serverName = hostPort.substring(0, len);
            }
            return this._serverName;
        }
        if (this._channel != null) {
            this._serverName = this.getLocalName();
            this._port = this.getLocalPort();
            if (this._serverName != null && !"0.0.0.0".equals(this._serverName)) {
                return this._serverName;
            }
        }
        try {
            this._serverName = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e2) {
            Request.LOG.ignore(e2);
        }
        return this._serverName;
    }
    
    @Override
    public int getServerPort() {
        if (this._port <= 0) {
            if (this._serverName == null) {
                this.getServerName();
            }
            if (this._port <= 0) {
                if (this._serverName != null && this._uri != null) {
                    this._port = this._uri.getPort();
                }
                else {
                    final InetSocketAddress local = this._channel.getLocalAddress();
                    this._port = ((local == null) ? 0 : local.getPort());
                }
            }
        }
        if (this._port > 0) {
            return this._port;
        }
        if (this.getScheme().equalsIgnoreCase("https")) {
            return 443;
        }
        return 80;
    }
    
    @Override
    public ServletContext getServletContext() {
        return this._context;
    }
    
    public String getServletName() {
        if (this._scope != null) {
            return this._scope.getName();
        }
        return null;
    }
    
    @Override
    public String getServletPath() {
        if (this._servletPath == null) {
            this._servletPath = "";
        }
        return this._servletPath;
    }
    
    public ServletResponse getServletResponse() {
        return this._channel.getResponse();
    }
    
    @Override
    public String changeSessionId() {
        final HttpSession session = this.getSession(false);
        if (session == null) {
            throw new IllegalStateException("No session");
        }
        if (session instanceof AbstractSession) {
            final AbstractSession abstractSession = (AbstractSession)session;
            abstractSession.renewId(this);
            if (this.getRemoteUser() != null) {
                abstractSession.setAttribute("org.eclipse.jetty.security.sessionKnownOnlytoAuthenticated", Boolean.TRUE);
            }
            if (abstractSession.isIdChanged()) {
                this._channel.getResponse().addCookie(this._sessionManager.getSessionCookie(abstractSession, this.getContextPath(), this.isSecure()));
            }
        }
        return session.getId();
    }
    
    @Override
    public HttpSession getSession() {
        return this.getSession(true);
    }
    
    @Override
    public HttpSession getSession(final boolean create) {
        if (this._session != null) {
            if (this._sessionManager == null || this._sessionManager.isValid(this._session)) {
                return this._session;
            }
            this._session = null;
        }
        if (!create) {
            return null;
        }
        if (this.getResponse().isCommitted()) {
            throw new IllegalStateException("Response is committed");
        }
        if (this._sessionManager == null) {
            throw new IllegalStateException("No SessionManager");
        }
        this._session = this._sessionManager.newHttpSession(this);
        final HttpCookie cookie = this._sessionManager.getSessionCookie(this._session, this.getContextPath(), this.isSecure());
        if (cookie != null) {
            this._channel.getResponse().addCookie(cookie);
        }
        return this._session;
    }
    
    public SessionManager getSessionManager() {
        return this._sessionManager;
    }
    
    public long getTimeStamp() {
        return this._timeStamp;
    }
    
    public HttpURI getUri() {
        return this._uri;
    }
    
    public UserIdentity getUserIdentity() {
        if (this._authentication instanceof Authentication.Deferred) {
            this.setAuthentication(((Authentication.Deferred)this._authentication).authenticate(this));
        }
        if (this._authentication instanceof Authentication.User) {
            return ((Authentication.User)this._authentication).getUserIdentity();
        }
        return null;
    }
    
    public UserIdentity getResolvedUserIdentity() {
        if (this._authentication instanceof Authentication.User) {
            return ((Authentication.User)this._authentication).getUserIdentity();
        }
        return null;
    }
    
    public UserIdentity.Scope getUserIdentityScope() {
        return this._scope;
    }
    
    @Override
    public Principal getUserPrincipal() {
        if (this._authentication instanceof Authentication.Deferred) {
            this.setAuthentication(((Authentication.Deferred)this._authentication).authenticate(this));
        }
        if (this._authentication instanceof Authentication.User) {
            final UserIdentity user = ((Authentication.User)this._authentication).getUserIdentity();
            return user.getUserPrincipal();
        }
        return null;
    }
    
    public boolean isHandled() {
        return this._handled;
    }
    
    @Override
    public boolean isAsyncStarted() {
        return this.getHttpChannelState().isAsyncStarted();
    }
    
    @Override
    public boolean isAsyncSupported() {
        return this._asyncSupported;
    }
    
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return this._requestedSessionId != null && this._requestedSessionIdFromCookie;
    }
    
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return this._requestedSessionId != null && !this._requestedSessionIdFromCookie;
    }
    
    @Override
    public boolean isRequestedSessionIdFromURL() {
        return this._requestedSessionId != null && !this._requestedSessionIdFromCookie;
    }
    
    @Override
    public boolean isRequestedSessionIdValid() {
        if (this._requestedSessionId == null) {
            return false;
        }
        final HttpSession session = this.getSession(false);
        return session != null && this._sessionManager.getSessionIdManager().getClusterId(this._requestedSessionId).equals(this._sessionManager.getClusterId(session));
    }
    
    @Override
    public boolean isSecure() {
        return this._secure;
    }
    
    public void setSecure(final boolean secure) {
        this._secure = secure;
    }
    
    @Override
    public boolean isUserInRole(final String role) {
        if (this._authentication instanceof Authentication.Deferred) {
            this.setAuthentication(((Authentication.Deferred)this._authentication).authenticate(this));
        }
        return this._authentication instanceof Authentication.User && ((Authentication.User)this._authentication).isUserInRole(this._scope, role);
    }
    
    public HttpSession recoverNewSession(final Object key) {
        if (this._savedNewSessions == null) {
            return null;
        }
        return this._savedNewSessions.get(key);
    }
    
    protected void recycle() {
        if (this._context != null) {
            throw new IllegalStateException("Request in context!");
        }
        if (this._inputState == 2) {
            try {
                for (int r = this._reader.read(); r != -1; r = this._reader.read()) {}
            }
            catch (Exception e) {
                Request.LOG.ignore(e);
                this._reader = null;
            }
        }
        this._dispatcherType = null;
        this.setAuthentication(Authentication.NOT_CHECKED);
        this.getHttpChannelState().recycle();
        if (this._async != null) {
            this._async.reset();
        }
        this._async = null;
        this._asyncSupported = true;
        this._handled = false;
        if (this._attributes != null) {
            this._attributes.clearAttributes();
        }
        this._characterEncoding = null;
        this._contextPath = null;
        if (this._cookies != null) {
            this._cookies.reset();
        }
        this._cookiesExtracted = false;
        this._context = null;
        this._newContext = false;
        this._serverName = null;
        this._httpMethod = null;
        this._httpMethodString = null;
        this._pathInfo = null;
        this._port = 0;
        this._httpVersion = HttpVersion.HTTP_1_1;
        this._queryEncoding = null;
        this._queryString = null;
        this._requestedSessionId = null;
        this._requestedSessionIdFromCookie = false;
        this._secure = false;
        this._session = null;
        this._sessionManager = null;
        this._requestURI = null;
        this._scope = null;
        this._scheme = "http";
        this._servletPath = null;
        this._timeStamp = 0L;
        this._uri = null;
        this._queryParameters = null;
        this._contentParameters = null;
        this._parameters = null;
        this._paramsExtracted = false;
        this._inputState = 0;
        if (this._savedNewSessions != null) {
            this._savedNewSessions.clear();
        }
        this._savedNewSessions = null;
        this._multiPartInputStream = null;
        this._remote = null;
        this._fields.clear();
        this._input.recycle();
    }
    
    @Override
    public void removeAttribute(final String name) {
        final Object old_value = (this._attributes == null) ? null : this._attributes.getAttribute(name);
        if (this._attributes != null) {
            this._attributes.removeAttribute(name);
        }
        if (old_value != null && !this._requestAttributeListeners.isEmpty()) {
            final ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(this._context, this, name, old_value);
            for (final ServletRequestAttributeListener listener : this._requestAttributeListeners) {
                listener.attributeRemoved(event);
            }
        }
    }
    
    public void removeEventListener(final EventListener listener) {
        this._requestAttributeListeners.remove(listener);
    }
    
    public void saveNewSession(final Object key, final HttpSession session) {
        if (this._savedNewSessions == null) {
            this._savedNewSessions = new HashMap<Object, HttpSession>();
        }
        this._savedNewSessions.put(key, session);
    }
    
    public void setAsyncSupported(final boolean supported) {
        this._asyncSupported = supported;
    }
    
    @Override
    public void setAttribute(final String name, final Object value) {
        final Object old_value = (this._attributes == null) ? null : this._attributes.getAttribute(name);
        if ("org.eclipse.jetty.server.Request.queryEncoding".equals(name)) {
            this.setQueryEncoding((value == null) ? null : value.toString());
        }
        else if ("org.eclipse.jetty.server.sendContent".equals(name)) {
            Request.LOG.warn("Deprecated: org.eclipse.jetty.server.sendContent", new Object[0]);
        }
        if (this._attributes == null) {
            this._attributes = new AttributesMap();
        }
        this._attributes.setAttribute(name, value);
        if (!this._requestAttributeListeners.isEmpty()) {
            final ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(this._context, this, name, (old_value == null) ? value : old_value);
            for (final ServletRequestAttributeListener l : this._requestAttributeListeners) {
                if (old_value == null) {
                    l.attributeAdded(event);
                }
                else if (value == null) {
                    l.attributeRemoved(event);
                }
                else {
                    l.attributeReplaced(event);
                }
            }
        }
    }
    
    public void setAttributes(final Attributes attributes) {
        this._attributes = attributes;
    }
    
    public void setAuthentication(final Authentication authentication) {
        this._authentication = authentication;
    }
    
    @Override
    public void setCharacterEncoding(final String encoding) throws UnsupportedEncodingException {
        if (this._inputState != 0) {
            return;
        }
        this._characterEncoding = encoding;
        if (!StringUtil.isUTF8(encoding)) {
            try {
                Charset.forName(encoding);
            }
            catch (UnsupportedCharsetException e) {
                throw new UnsupportedEncodingException(e.getMessage());
            }
        }
    }
    
    public void setCharacterEncodingUnchecked(final String encoding) {
        this._characterEncoding = encoding;
    }
    
    public void setContentType(final String contentType) {
        this._fields.put(HttpHeader.CONTENT_TYPE, contentType);
    }
    
    public void setContext(final ContextHandler.Context context) {
        this._newContext = (this._context != context);
        this._context = context;
    }
    
    public boolean takeNewContext() {
        final boolean nc = this._newContext;
        this._newContext = false;
        return nc;
    }
    
    public void setContextPath(final String contextPath) {
        this._contextPath = contextPath;
    }
    
    public void setCookies(final Cookie[] cookies) {
        if (this._cookies == null) {
            this._cookies = new CookieCutter();
        }
        this._cookies.setCookies(cookies);
    }
    
    public void setDispatcherType(final DispatcherType type) {
        this._dispatcherType = type;
    }
    
    public void setHandled(final boolean h) {
        this._handled = h;
    }
    
    public void setMethod(final HttpMethod httpMethod, final String method) {
        this._httpMethod = httpMethod;
        this._httpMethodString = method;
    }
    
    public boolean isHead() {
        return HttpMethod.HEAD == this._httpMethod;
    }
    
    public void setPathInfo(final String pathInfo) {
        this._pathInfo = pathInfo;
    }
    
    public void setHttpVersion(final HttpVersion version) {
        this._httpVersion = version;
    }
    
    public void setQueryEncoding(final String queryEncoding) {
        this._queryEncoding = queryEncoding;
        this._queryString = null;
    }
    
    public void setQueryString(final String queryString) {
        this._queryString = queryString;
        this._queryEncoding = null;
    }
    
    public void setRemoteAddr(final InetSocketAddress addr) {
        this._remote = addr;
    }
    
    public void setRequestedSessionId(final String requestedSessionId) {
        this._requestedSessionId = requestedSessionId;
    }
    
    public void setRequestedSessionIdFromCookie(final boolean requestedSessionIdCookie) {
        this._requestedSessionIdFromCookie = requestedSessionIdCookie;
    }
    
    public void setRequestURI(final String requestURI) {
        this._requestURI = requestURI;
    }
    
    public void setScheme(final String scheme) {
        this._scheme = scheme;
    }
    
    public void setServerName(final String host) {
        this._serverName = host;
    }
    
    public void setServerPort(final int port) {
        this._port = port;
    }
    
    public void setServletPath(final String servletPath) {
        this._servletPath = servletPath;
    }
    
    public void setSession(final HttpSession session) {
        this._session = session;
    }
    
    public void setSessionManager(final SessionManager sessionManager) {
        this._sessionManager = sessionManager;
    }
    
    public void setTimeStamp(final long ts) {
        this._timeStamp = ts;
    }
    
    public void setUri(final HttpURI uri) {
        this._uri = uri;
    }
    
    public void setUserIdentityScope(final UserIdentity.Scope scope) {
        this._scope = scope;
    }
    
    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        if (!this._asyncSupported) {
            throw new IllegalStateException("!asyncSupported");
        }
        final HttpChannelState state = this.getHttpChannelState();
        if (this._async == null) {
            this._async = new AsyncContextState(state);
        }
        final AsyncContextEvent event = new AsyncContextEvent(this._context, this._async, state, this, this, this.getResponse());
        state.startAsync(event);
        return this._async;
    }
    
    @Override
    public AsyncContext startAsync(final ServletRequest servletRequest, final ServletResponse servletResponse) throws IllegalStateException {
        if (!this._asyncSupported) {
            throw new IllegalStateException("!asyncSupported");
        }
        final HttpChannelState state = this.getHttpChannelState();
        if (this._async == null) {
            this._async = new AsyncContextState(state);
        }
        final AsyncContextEvent event = new AsyncContextEvent(this._context, this._async, state, this, servletRequest, servletResponse);
        event.setDispatchContext(this.getServletContext());
        event.setDispatchPath(URIUtil.addPaths(this.getServletPath(), this.getPathInfo()));
        state.startAsync(event);
        return this._async;
    }
    
    @Override
    public String toString() {
        return String.format("%s%s%s %s%s@%x", this.getClass().getSimpleName(), this._handled ? "[" : "(", this.getMethod(), this._uri, this._handled ? "]" : ")", this.hashCode());
    }
    
    @Override
    public boolean authenticate(final HttpServletResponse response) throws IOException, ServletException {
        if (this._authentication instanceof Authentication.Deferred) {
            this.setAuthentication(((Authentication.Deferred)this._authentication).authenticate(this, response));
            return !(this._authentication instanceof Authentication.ResponseSent);
        }
        response.sendError(401);
        return false;
    }
    
    @Override
    public Part getPart(final String name) throws IOException, ServletException {
        this.getParts();
        return this._multiPartInputStream.getPart(name);
    }
    
    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        if (this.getContentType() == null || !this.getContentType().startsWith("multipart/form-data")) {
            throw new ServletException("Content-Type != multipart/form-data");
        }
        return this.getParts(null);
    }
    
    private Collection<Part> getParts(final MultiMap<String> params) throws IOException, ServletException {
        if (this._multiPartInputStream == null) {
            this._multiPartInputStream = (MultiPartInputStreamParser)this.getAttribute("org.eclipse.jetty.multiPartInputStream");
        }
        if (this._multiPartInputStream == null) {
            final MultipartConfigElement config = (MultipartConfigElement)this.getAttribute("org.eclipse.jetty.multipartConfig");
            if (config == null) {
                throw new IllegalStateException("No multipart config for servlet");
            }
            this.setAttribute("org.eclipse.jetty.multiPartInputStream", this._multiPartInputStream = new MultiPartInputStreamParser(this.getInputStream(), this.getContentType(), config, (this._context != null) ? ((File)this._context.getAttribute("javax.servlet.context.tempdir")) : null));
            this.setAttribute("org.eclipse.jetty.multiPartContext", this._context);
            final Collection<Part> parts = this._multiPartInputStream.getParts();
            ByteArrayOutputStream os = null;
            for (final Part p : parts) {
                final MultiPartInputStreamParser.MultiPart mp = (MultiPartInputStreamParser.MultiPart)p;
                if (mp.getContentDispositionFilename() == null) {
                    String charset = null;
                    if (mp.getContentType() != null) {
                        charset = MimeTypes.getCharsetFromContentType(mp.getContentType());
                    }
                    try (final InputStream is = mp.getInputStream()) {
                        if (os == null) {
                            os = new ByteArrayOutputStream();
                        }
                        IO.copy(is, os);
                        final String content = new String(os.toByteArray(), (charset == null) ? StandardCharsets.UTF_8 : Charset.forName(charset));
                        if (this._contentParameters == null) {
                            this._contentParameters = ((params == null) ? new MultiMap<String>() : params);
                        }
                        this._contentParameters.add(mp.getName(), content);
                    }
                    os.reset();
                }
            }
        }
        return this._multiPartInputStream.getParts();
    }
    
    @Override
    public void login(final String username, final String password) throws ServletException {
        if (!(this._authentication instanceof Authentication.Deferred)) {
            throw new Authentication.Failed("Authenticated failed for username '" + username + "'. Already authenticated as " + this._authentication);
        }
        this._authentication = ((Authentication.Deferred)this._authentication).login(username, password, this);
        if (this._authentication == null) {
            throw new Authentication.Failed("Authentication failed for username '" + username + "'");
        }
    }
    
    @Override
    public void logout() throws ServletException {
        if (this._authentication instanceof Authentication.User) {
            ((Authentication.User)this._authentication).logout();
        }
        this._authentication = Authentication.UNAUTHENTICATED;
    }
    
    public void mergeQueryParameters(final String newQuery, final boolean updateQueryString) {
        final MultiMap<String> newQueryParams = new MultiMap<String>();
        UrlEncoded.decodeTo(newQuery, newQueryParams, UrlEncoded.ENCODING, -1);
        MultiMap<String> oldQueryParams = this._queryParameters;
        if (oldQueryParams == null && this._queryString != null) {
            oldQueryParams = new MultiMap<String>();
            UrlEncoded.decodeTo(this._queryString, oldQueryParams, this.getQueryEncoding(), -1);
        }
        MultiMap<String> mergedQueryParams = newQueryParams;
        if (oldQueryParams != null) {
            mergedQueryParams = new MultiMap<String>(newQueryParams);
            mergedQueryParams.addAllValues(oldQueryParams);
        }
        this.setQueryParameters(mergedQueryParams);
        this.resetParameters();
        if (updateQueryString) {
            final StringBuilder mergedQuery = new StringBuilder(newQuery);
            for (final Map.Entry<String, List<String>> entry : mergedQueryParams.entrySet()) {
                if (newQueryParams.containsKey(entry.getKey())) {
                    continue;
                }
                for (final String value : entry.getValue()) {
                    mergedQuery.append("&").append(entry.getKey()).append("=").append(value);
                }
            }
            this.setQueryString(mergedQuery.toString());
        }
    }
    
    @Override
    public <T extends HttpUpgradeHandler> T upgrade(final Class<T> handlerClass) throws IOException, ServletException {
        if (this.getContext() == null) {
            throw new ServletException("Unable to instantiate " + handlerClass);
        }
        try {
            final T h = this.getContext().createInstance(handlerClass);
            return h;
        }
        catch (Exception e) {
            if (e instanceof ServletException) {
                throw (ServletException)e;
            }
            throw new ServletException(e);
        }
    }
    
    static {
        LOG = Log.getLogger(Request.class);
        __defaultLocale = Collections.singleton(Locale.getDefault());
    }
    
    public static class MultiPartCleanerListener implements ServletRequestListener
    {
        @Override
        public void requestDestroyed(final ServletRequestEvent sre) {
            final MultiPartInputStreamParser mpis = (MultiPartInputStreamParser)sre.getServletRequest().getAttribute("org.eclipse.jetty.multiPartInputStream");
            if (mpis != null) {
                final ContextHandler.Context context = (ContextHandler.Context)sre.getServletRequest().getAttribute("org.eclipse.jetty.multiPartContext");
                if (context == sre.getServletContext()) {
                    try {
                        mpis.deleteParts();
                    }
                    catch (MultiException e) {
                        sre.getServletContext().log("Errors deleting multipart tmp files", e);
                    }
                }
            }
        }
        
        @Override
        public void requestInitialized(final ServletRequestEvent sre) {
        }
    }
}
