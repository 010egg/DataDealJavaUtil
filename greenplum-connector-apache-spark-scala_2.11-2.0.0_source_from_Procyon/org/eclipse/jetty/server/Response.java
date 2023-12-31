// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import java.io.Writer;
import org.eclipse.jetty.util.log.Log;
import java.util.Enumeration;
import java.util.ArrayList;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.HttpHeaderValue;
import java.util.List;
import java.nio.channels.IllegalSelectorException;
import org.eclipse.jetty.io.RuntimeIOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Collection;
import java.nio.ByteBuffer;
import org.eclipse.jetty.http.HttpGenerator;
import javax.servlet.ServletOutputStream;
import java.io.OutputStream;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.ByteArrayISO8859Writer;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.http.HttpStatus;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.util.URIUtil;
import java.util.Iterator;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.DateGenerator;
import org.eclipse.jetty.util.QuotedStringTokenizer;
import javax.servlet.http.Cookie;
import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpContent;
import org.eclipse.jetty.http.MimeTypes;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.util.log.Logger;
import javax.servlet.http.HttpServletResponse;

public class Response implements HttpServletResponse
{
    private static final Logger LOG;
    private static final String __COOKIE_DELIM = "\",;\\ \t";
    private static final String __01Jan1970_COOKIE;
    private static final int __MIN_BUFFER_SIZE = 1;
    private static final ThreadLocal<StringBuilder> __cookieBuilder;
    public static final String SET_INCLUDE_HEADER_PREFIX = "org.eclipse.jetty.server.include.";
    public static final String HTTP_ONLY_COMMENT = "__HTTP_ONLY__";
    private final HttpChannel<?> _channel;
    private final HttpFields _fields;
    private final AtomicInteger _include;
    private HttpOutput _out;
    private int _status;
    private String _reason;
    private Locale _locale;
    private MimeTypes.Type _mimeType;
    private String _characterEncoding;
    private boolean _explicitEncoding;
    private String _contentType;
    private OutputType _outputType;
    private ResponseWriter _writer;
    private long _contentLength;
    
    public static Response getResponse(final HttpServletResponse response) {
        if (response instanceof Response) {
            return (Response)response;
        }
        return HttpChannel.getCurrentHttpChannel().getResponse();
    }
    
    public Response(final HttpChannel<?> channel, final HttpOutput out) {
        this._fields = new HttpFields();
        this._include = new AtomicInteger();
        this._status = 200;
        this._outputType = OutputType.NONE;
        this._contentLength = -1L;
        this._channel = channel;
        this._out = out;
    }
    
    protected HttpChannel<?> getHttpChannel() {
        return this._channel;
    }
    
    protected void recycle() {
        this._status = 200;
        this._reason = null;
        this._locale = null;
        this._mimeType = null;
        this._characterEncoding = null;
        this._contentType = null;
        this._outputType = OutputType.NONE;
        this._contentLength = -1L;
        this._out.reset();
        this._fields.clear();
        this._explicitEncoding = false;
    }
    
    public void setHeaders(final HttpContent httpContent) {
        final Response response = this._channel.getResponse();
        final String contentType = httpContent.getContentType();
        if (contentType != null && !response.getHttpFields().containsKey(HttpHeader.CONTENT_TYPE.asString())) {
            this.setContentType(contentType);
        }
        if (httpContent.getContentLength() > 0L) {
            this.setLongContentLength(httpContent.getContentLength());
        }
        final String lm = httpContent.getLastModified();
        if (lm != null) {
            response.getHttpFields().put(HttpHeader.LAST_MODIFIED, lm);
        }
        else if (httpContent.getResource() != null) {
            final long lml = httpContent.getResource().lastModified();
            if (lml != -1L) {
                response.getHttpFields().putDateField(HttpHeader.LAST_MODIFIED, lml);
            }
        }
        final String etag = httpContent.getETag();
        if (etag != null) {
            response.getHttpFields().put(HttpHeader.ETAG, etag);
        }
    }
    
    public HttpOutput getHttpOutput() {
        return this._out;
    }
    
    public void setHttpOutput(final HttpOutput out) {
        this._out = out;
    }
    
    public boolean isIncluding() {
        return this._include.get() > 0;
    }
    
    public void include() {
        this._include.incrementAndGet();
    }
    
    public void included() {
        this._include.decrementAndGet();
        if (this._outputType == OutputType.WRITER) {
            this._writer.reopen();
        }
        this._out.reopen();
    }
    
    public void addCookie(final HttpCookie cookie) {
        this.addSetCookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), cookie.getMaxAge(), cookie.getComment(), cookie.isSecure(), cookie.isHttpOnly(), cookie.getVersion());
    }
    
    @Override
    public void addCookie(final Cookie cookie) {
        String comment = cookie.getComment();
        boolean httpOnly = false;
        if (comment != null) {
            final int i = comment.indexOf("__HTTP_ONLY__");
            if (i >= 0) {
                httpOnly = true;
                comment = comment.replace("__HTTP_ONLY__", "").trim();
                if (comment.length() == 0) {
                    comment = null;
                }
            }
        }
        this.addSetCookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), cookie.getMaxAge(), comment, cookie.getSecure(), httpOnly || cookie.isHttpOnly(), cookie.getVersion());
    }
    
    public void addSetCookie(final String name, final String value, final String domain, final String path, final long maxAge, final String comment, final boolean isSecure, final boolean isHttpOnly, int version) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Bad cookie name");
        }
        final StringBuilder buf = Response.__cookieBuilder.get();
        buf.setLength(0);
        final boolean quote_name = isQuoteNeededForCookie(name);
        quoteOnlyOrAppend(buf, name, quote_name);
        buf.append('=');
        final String name_equals = buf.toString();
        final boolean quote_value = isQuoteNeededForCookie(value);
        quoteOnlyOrAppend(buf, value, quote_value);
        final boolean has_domain = domain != null && domain.length() > 0;
        final boolean quote_domain = has_domain && isQuoteNeededForCookie(domain);
        final boolean has_path = path != null && path.length() > 0;
        final boolean quote_path = has_path && isQuoteNeededForCookie(path);
        if (version == 0 && (comment != null || quote_name || quote_value || quote_domain || quote_path || QuotedStringTokenizer.isQuoted(name) || QuotedStringTokenizer.isQuoted(value) || QuotedStringTokenizer.isQuoted(path) || QuotedStringTokenizer.isQuoted(domain))) {
            version = 1;
        }
        if (version == 1) {
            buf.append(";Version=1");
        }
        else if (version > 1) {
            buf.append(";Version=").append(version);
        }
        if (has_path) {
            buf.append(";Path=");
            quoteOnlyOrAppend(buf, path, quote_path);
        }
        if (has_domain) {
            buf.append(";Domain=");
            quoteOnlyOrAppend(buf, domain, quote_domain);
        }
        if (maxAge >= 0L) {
            buf.append(";Expires=");
            if (maxAge == 0L) {
                buf.append(Response.__01Jan1970_COOKIE);
            }
            else {
                DateGenerator.formatCookieDate(buf, System.currentTimeMillis() + 1000L * maxAge);
            }
            if (version >= 1) {
                buf.append(";Max-Age=");
                buf.append(maxAge);
            }
        }
        if (isSecure) {
            buf.append(";Secure");
        }
        if (isHttpOnly) {
            buf.append(";HttpOnly");
        }
        if (comment != null) {
            buf.append(";Comment=");
            quoteOnlyOrAppend(buf, comment, isQuoteNeededForCookie(comment));
        }
        final Iterator<HttpField> i = this._fields.iterator();
        while (i.hasNext()) {
            final HttpField field = i.next();
            if (field.getHeader() == HttpHeader.SET_COOKIE) {
                final String val = field.getValue();
                if (val == null || !val.startsWith(name_equals) || ((has_domain || val.contains("Domain")) && (!has_domain || !val.contains(domain))) || ((has_path || val.contains("Path")) && (!has_path || !val.contains(path)))) {
                    continue;
                }
                i.remove();
            }
        }
        this._fields.add(HttpHeader.SET_COOKIE.toString(), buf.toString());
        this._fields.put(HttpHeader.EXPIRES.toString(), DateGenerator.__01Jan1970);
    }
    
    private static boolean isQuoteNeededForCookie(final String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        if (QuotedStringTokenizer.isQuoted(s)) {
            return false;
        }
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if ("\",;\\ \t".indexOf(c) >= 0) {
                return true;
            }
            if (c < ' ' || c >= '\u007f') {
                throw new IllegalArgumentException("Illegal character in cookie value");
            }
        }
        return false;
    }
    
    private static void quoteOnlyOrAppend(final StringBuilder buf, final String s, final boolean quote) {
        if (quote) {
            QuotedStringTokenizer.quoteOnly(buf, s);
        }
        else {
            buf.append(s);
        }
    }
    
    @Override
    public boolean containsHeader(final String name) {
        return this._fields.containsKey(name);
    }
    
    @Override
    public String encodeURL(final String url) {
        final Request request = this._channel.getRequest();
        final SessionManager sessionManager = request.getSessionManager();
        if (sessionManager == null) {
            return url;
        }
        HttpURI uri = null;
        if (sessionManager.isCheckingRemoteSessionIdEncoding() && URIUtil.hasScheme(url)) {
            uri = new HttpURI(url);
            String path = uri.getPath();
            path = ((path == null) ? "" : path);
            int port = uri.getPort();
            if (port < 0) {
                port = (HttpScheme.HTTPS.asString().equalsIgnoreCase(uri.getScheme()) ? 443 : 80);
            }
            if (!request.getServerName().equalsIgnoreCase(uri.getHost()) || request.getServerPort() != port || !path.startsWith(request.getContextPath())) {
                return url;
            }
        }
        final String sessionURLPrefix = sessionManager.getSessionIdPathParameterNamePrefix();
        if (sessionURLPrefix == null) {
            return url;
        }
        if (url == null) {
            return null;
        }
        if ((sessionManager.isUsingCookies() && request.isRequestedSessionIdFromCookie()) || !sessionManager.isUsingURLs()) {
            final int prefix = url.indexOf(sessionURLPrefix);
            if (prefix == -1) {
                return url;
            }
            int suffix = url.indexOf("?", prefix);
            if (suffix < 0) {
                suffix = url.indexOf("#", prefix);
            }
            if (suffix <= prefix) {
                return url.substring(0, prefix);
            }
            return url.substring(0, prefix) + url.substring(suffix);
        }
        else {
            final HttpSession session = request.getSession(false);
            if (session == null) {
                return url;
            }
            if (!sessionManager.isValid(session)) {
                return url;
            }
            final String id = sessionManager.getNodeId(session);
            if (uri == null) {
                uri = new HttpURI(url);
            }
            final int prefix2 = url.indexOf(sessionURLPrefix);
            if (prefix2 != -1) {
                int suffix2 = url.indexOf("?", prefix2);
                if (suffix2 < 0) {
                    suffix2 = url.indexOf("#", prefix2);
                }
                if (suffix2 <= prefix2) {
                    return url.substring(0, prefix2 + sessionURLPrefix.length()) + id;
                }
                return url.substring(0, prefix2 + sessionURLPrefix.length()) + id + url.substring(suffix2);
            }
            else {
                int suffix2 = url.indexOf(63);
                if (suffix2 < 0) {
                    suffix2 = url.indexOf(35);
                }
                if (suffix2 < 0) {
                    return url + (((HttpScheme.HTTPS.is(uri.getScheme()) || HttpScheme.HTTP.is(uri.getScheme())) && uri.getPath() == null) ? "/" : "") + sessionURLPrefix + id;
                }
                return url.substring(0, suffix2) + (((HttpScheme.HTTPS.is(uri.getScheme()) || HttpScheme.HTTP.is(uri.getScheme())) && uri.getPath() == null) ? "/" : "") + sessionURLPrefix + id + url.substring(suffix2);
            }
        }
    }
    
    @Override
    public String encodeRedirectURL(final String url) {
        return this.encodeURL(url);
    }
    
    @Deprecated
    @Override
    public String encodeUrl(final String url) {
        return this.encodeURL(url);
    }
    
    @Deprecated
    @Override
    public String encodeRedirectUrl(final String url) {
        return this.encodeRedirectURL(url);
    }
    
    @Override
    public void sendError(final int sc) throws IOException {
        this.sendError(sc, null);
    }
    
    @Override
    public void sendError(final int code, String message) throws IOException {
        if (this.isIncluding()) {
            return;
        }
        switch (code) {
            case -1: {
                this._channel.abort();
            }
            case 102: {
                this.sendProcessing();
            }
            default: {
                if (this.isCommitted()) {
                    Response.LOG.warn("Committed before " + code + " " + message, new Object[0]);
                }
                this.resetBuffer();
                this._characterEncoding = null;
                this.setHeader(HttpHeader.EXPIRES, null);
                this.setHeader(HttpHeader.LAST_MODIFIED, null);
                this.setHeader(HttpHeader.CACHE_CONTROL, null);
                this.setHeader(HttpHeader.CONTENT_TYPE, null);
                this.setHeader(HttpHeader.CONTENT_LENGTH, null);
                this._outputType = OutputType.NONE;
                this.setStatus(code);
                this._reason = message;
                final Request request = this._channel.getRequest();
                final Throwable cause = (Throwable)request.getAttribute("javax.servlet.error.exception");
                if (message == null) {
                    message = ((cause == null) ? HttpStatus.getMessage(code) : cause.toString());
                }
                if (code != 204 && code != 304 && code != 206 && code >= 200) {
                    final ErrorHandler error_handler = ErrorHandler.getErrorHandler(this._channel.getServer(), (request.getContext() == null) ? null : request.getContext().getContextHandler());
                    if (error_handler != null) {
                        request.setAttribute("javax.servlet.error.status_code", new Integer(code));
                        request.setAttribute("javax.servlet.error.message", message);
                        request.setAttribute("javax.servlet.error.request_uri", request.getRequestURI());
                        request.setAttribute("javax.servlet.error.servlet_name", request.getServletName());
                        error_handler.handle(null, this._channel.getRequest(), this._channel.getRequest(), this);
                    }
                    else {
                        this.setHeader(HttpHeader.CACHE_CONTROL, "must-revalidate,no-cache,no-store");
                        this.setContentType(MimeTypes.Type.TEXT_HTML_8859_1.toString());
                        try (final ByteArrayISO8859Writer writer = new ByteArrayISO8859Writer(2048)) {
                            message = StringUtil.sanitizeXmlString(message);
                            String uri = request.getRequestURI();
                            uri = StringUtil.sanitizeXmlString(uri);
                            writer.write("<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html;charset=ISO-8859-1\"/>\n");
                            writer.write("<title>Error ");
                            writer.write(Integer.toString(code));
                            writer.write(' ');
                            if (message == null) {
                                writer.write(message);
                            }
                            writer.write("</title>\n</head>\n<body>\n<h2>HTTP ERROR: ");
                            writer.write(Integer.toString(code));
                            writer.write("</h2>\n<p>Problem accessing ");
                            writer.write(uri);
                            writer.write(". Reason:\n<pre>    ");
                            writer.write(message);
                            writer.write("</pre>");
                            writer.write("</p>\n<hr /><i><small>Powered by Jetty://</small></i>");
                            writer.write("\n</body>\n</html>\n");
                            writer.flush();
                            this.setContentLength(writer.size());
                            try (final ServletOutputStream outputStream = this.getOutputStream()) {
                                writer.writeTo(outputStream);
                                writer.destroy();
                            }
                        }
                    }
                }
                else if (code != 206) {
                    this._channel.getRequest().getHttpFields().remove(HttpHeader.CONTENT_TYPE);
                    this._channel.getRequest().getHttpFields().remove(HttpHeader.CONTENT_LENGTH);
                    this._characterEncoding = null;
                    this._mimeType = null;
                }
                this.closeOutput();
            }
        }
    }
    
    public void sendProcessing() throws IOException {
        if (this._channel.isExpecting102Processing() && !this.isCommitted()) {
            this._channel.sendResponse(HttpGenerator.PROGRESS_102_INFO, null, true);
        }
    }
    
    public void sendRedirect(final int code, String location) throws IOException {
        if (code < 300 || code >= 400) {
            throw new IllegalArgumentException("Not a 3xx redirect code");
        }
        if (this.isIncluding()) {
            return;
        }
        if (location == null) {
            throw new IllegalArgumentException();
        }
        if (!URIUtil.hasScheme(location)) {
            final StringBuilder buf = this._channel.getRequest().getRootURL();
            if (location.startsWith("/")) {
                location = URIUtil.canonicalPath(location);
            }
            else {
                final String path = this._channel.getRequest().getRequestURI();
                final String parent = path.endsWith("/") ? path : URIUtil.parentPath(path);
                location = URIUtil.canonicalPath(URIUtil.addPaths(parent, location));
                if (!location.startsWith("/")) {
                    buf.append('/');
                }
            }
            if (location == null) {
                throw new IllegalStateException("path cannot be above root");
            }
            buf.append(location);
            location = buf.toString();
        }
        this.resetBuffer();
        this.setHeader(HttpHeader.LOCATION, location);
        this.setStatus(code);
        this.closeOutput();
    }
    
    @Override
    public void sendRedirect(final String location) throws IOException {
        this.sendRedirect(302, location);
    }
    
    @Override
    public void setDateHeader(final String name, final long date) {
        if (!this.isIncluding()) {
            this._fields.putDateField(name, date);
        }
    }
    
    @Override
    public void addDateHeader(final String name, final long date) {
        if (!this.isIncluding()) {
            this._fields.addDateField(name, date);
        }
    }
    
    public void setHeader(final HttpHeader name, final String value) {
        if (HttpHeader.CONTENT_TYPE == name) {
            this.setContentType(value);
        }
        else {
            if (this.isIncluding()) {
                return;
            }
            this._fields.put(name, value);
            if (HttpHeader.CONTENT_LENGTH == name) {
                if (value == null) {
                    this._contentLength = -1L;
                }
                else {
                    this._contentLength = Long.parseLong(value);
                }
            }
        }
    }
    
    @Override
    public void setHeader(String name, final String value) {
        if (HttpHeader.CONTENT_TYPE.is(name)) {
            this.setContentType(value);
        }
        else {
            if (this.isIncluding()) {
                if (!name.startsWith("org.eclipse.jetty.server.include.")) {
                    return;
                }
                name = name.substring("org.eclipse.jetty.server.include.".length());
            }
            this._fields.put(name, value);
            if (HttpHeader.CONTENT_LENGTH.is(name)) {
                if (value == null) {
                    this._contentLength = -1L;
                }
                else {
                    this._contentLength = Long.parseLong(value);
                }
            }
        }
    }
    
    @Override
    public Collection<String> getHeaderNames() {
        final HttpFields fields = this._fields;
        return fields.getFieldNamesCollection();
    }
    
    @Override
    public String getHeader(final String name) {
        return this._fields.getStringField(name);
    }
    
    @Override
    public Collection<String> getHeaders(final String name) {
        final HttpFields fields = this._fields;
        final Collection<String> i = fields.getValuesList(name);
        if (i == null) {
            return (Collection<String>)Collections.emptyList();
        }
        return i;
    }
    
    @Override
    public void addHeader(String name, final String value) {
        if (this.isIncluding()) {
            if (!name.startsWith("org.eclipse.jetty.server.include.")) {
                return;
            }
            name = name.substring("org.eclipse.jetty.server.include.".length());
        }
        if (HttpHeader.CONTENT_TYPE.is(name)) {
            this.setContentType(value);
            return;
        }
        if (HttpHeader.CONTENT_LENGTH.is(name)) {
            this.setHeader(name, value);
            return;
        }
        this._fields.add(name, value);
    }
    
    @Override
    public void setIntHeader(final String name, final int value) {
        if (!this.isIncluding()) {
            this._fields.putLongField(name, value);
            if (HttpHeader.CONTENT_LENGTH.is(name)) {
                this._contentLength = value;
            }
        }
    }
    
    @Override
    public void addIntHeader(final String name, final int value) {
        if (!this.isIncluding()) {
            this._fields.add(name, Integer.toString(value));
            if (HttpHeader.CONTENT_LENGTH.is(name)) {
                this._contentLength = value;
            }
        }
    }
    
    @Override
    public void setStatus(final int sc) {
        if (sc <= 0) {
            throw new IllegalArgumentException();
        }
        if (!this.isIncluding()) {
            this._status = sc;
            this._reason = null;
        }
    }
    
    @Deprecated
    @Override
    public void setStatus(final int sc, final String sm) {
        this.setStatusWithReason(sc, sm);
    }
    
    public void setStatusWithReason(final int sc, final String sm) {
        if (sc <= 0) {
            throw new IllegalArgumentException();
        }
        if (!this.isIncluding()) {
            this._status = sc;
            this._reason = sm;
        }
    }
    
    @Override
    public String getCharacterEncoding() {
        if (this._characterEncoding == null) {
            this._characterEncoding = "ISO-8859-1";
        }
        return this._characterEncoding;
    }
    
    @Override
    public String getContentType() {
        return this._contentType;
    }
    
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (this._outputType == OutputType.WRITER) {
            throw new IllegalStateException("WRITER");
        }
        this._outputType = OutputType.STREAM;
        return this._out;
    }
    
    public boolean isWriting() {
        return this._outputType == OutputType.WRITER;
    }
    
    @Override
    public PrintWriter getWriter() throws IOException {
        if (this._outputType == OutputType.STREAM) {
            throw new IllegalStateException("STREAM");
        }
        if (this._outputType == OutputType.NONE) {
            String encoding = this._characterEncoding;
            if (encoding == null) {
                if (this._mimeType != null && this._mimeType.isCharsetAssumed()) {
                    encoding = this._mimeType.getCharset().toString();
                }
                else {
                    encoding = MimeTypes.inferCharsetFromContentType(this._contentType);
                    if (encoding == null) {
                        encoding = "ISO-8859-1";
                    }
                    this.setCharacterEncoding(encoding, false);
                }
            }
            if (this._writer != null && this._writer.isFor(encoding)) {
                this._writer.reopen();
            }
            else if ("ISO-8859-1".equalsIgnoreCase(encoding)) {
                this._writer = new ResponseWriter(new Iso88591HttpWriter(this._out), encoding);
            }
            else if ("UTF-8".equalsIgnoreCase(encoding)) {
                this._writer = new ResponseWriter(new Utf8HttpWriter(this._out), encoding);
            }
            else {
                this._writer = new ResponseWriter(new EncodingHttpWriter(this._out, encoding), encoding);
            }
            this._outputType = OutputType.WRITER;
        }
        return this._writer;
    }
    
    @Override
    public void setContentLength(final int len) {
        if (this.isCommitted() || this.isIncluding()) {
            return;
        }
        this._contentLength = len;
        if (this._contentLength > 0L) {
            final long written = this._out.getWritten();
            if (written > len) {
                throw new IllegalArgumentException("setContentLength(" + len + ") when already written " + written);
            }
            this._fields.putLongField(HttpHeader.CONTENT_LENGTH, len);
            if (this.isAllContentWritten(written)) {
                try {
                    this.closeOutput();
                }
                catch (IOException e) {
                    throw new RuntimeIOException(e);
                }
            }
        }
        else if (this._contentLength == 0L) {
            final long written = this._out.getWritten();
            if (written > 0L) {
                throw new IllegalArgumentException("setContentLength(0) when already written " + written);
            }
            this._fields.put(HttpHeader.CONTENT_LENGTH, "0");
        }
        else {
            this._fields.remove(HttpHeader.CONTENT_LENGTH);
        }
    }
    
    public long getContentLength() {
        return this._contentLength;
    }
    
    public boolean isAllContentWritten(final long written) {
        return this._contentLength >= 0L && written >= this._contentLength;
    }
    
    public void closeOutput() throws IOException {
        switch (this._outputType) {
            case WRITER: {
                this._writer.close();
                if (!this._out.isClosed()) {
                    this._out.close();
                    break;
                }
                break;
            }
            case STREAM: {
                this.getOutputStream().close();
                break;
            }
            default: {
                this._out.close();
                break;
            }
        }
    }
    
    public long getLongContentLength() {
        return this._contentLength;
    }
    
    public void setLongContentLength(final long len) {
        if (this.isCommitted() || this.isIncluding()) {
            return;
        }
        this._contentLength = len;
        this._fields.putLongField(HttpHeader.CONTENT_LENGTH.toString(), len);
    }
    
    @Override
    public void setContentLengthLong(final long length) {
        this.setLongContentLength(length);
    }
    
    @Override
    public void setCharacterEncoding(final String encoding) {
        this.setCharacterEncoding(encoding, true);
    }
    
    private void setCharacterEncoding(final String encoding, final boolean explicit) {
        if (this.isIncluding() || this.isWriting()) {
            return;
        }
        if (this._outputType == OutputType.NONE && !this.isCommitted()) {
            if (encoding == null) {
                this._explicitEncoding = false;
                if (this._characterEncoding != null) {
                    this._characterEncoding = null;
                    if (this._mimeType != null) {
                        this._mimeType = this._mimeType.getBaseType();
                        this._contentType = this._mimeType.asString();
                        this._fields.put(this._mimeType.getContentTypeField());
                    }
                    else if (this._contentType != null) {
                        this._contentType = MimeTypes.getContentTypeWithoutCharset(this._contentType);
                        this._fields.put(HttpHeader.CONTENT_TYPE, this._contentType);
                    }
                }
            }
            else {
                this._explicitEncoding = explicit;
                this._characterEncoding = (HttpGenerator.__STRICT ? encoding : StringUtil.normalizeCharset(encoding));
                if (this._mimeType != null) {
                    this._contentType = this._mimeType.getBaseType().asString() + "; charset=" + this._characterEncoding;
                    this._mimeType = MimeTypes.CACHE.get(this._contentType);
                    if (this._mimeType == null || HttpGenerator.__STRICT) {
                        this._fields.put(HttpHeader.CONTENT_TYPE, this._contentType);
                    }
                    else {
                        this._fields.put(this._mimeType.getContentTypeField());
                    }
                }
                else if (this._contentType != null) {
                    this._contentType = MimeTypes.getContentTypeWithoutCharset(this._contentType) + "; charset=" + this._characterEncoding;
                    this._fields.put(HttpHeader.CONTENT_TYPE, this._contentType);
                }
            }
        }
    }
    
    @Override
    public void setContentType(final String contentType) {
        if (this.isCommitted() || this.isIncluding()) {
            return;
        }
        if (contentType == null) {
            if (this.isWriting() && this._characterEncoding != null) {
                throw new IllegalSelectorException();
            }
            if (this._locale == null) {
                this._characterEncoding = null;
            }
            this._mimeType = null;
            this._contentType = null;
            this._fields.remove(HttpHeader.CONTENT_TYPE);
        }
        else {
            this._contentType = contentType;
            this._mimeType = MimeTypes.CACHE.get(contentType);
            String charset;
            if (this._mimeType != null && this._mimeType.getCharset() != null && !this._mimeType.isCharsetAssumed()) {
                charset = this._mimeType.getCharset().toString();
            }
            else {
                charset = MimeTypes.getCharsetFromContentType(contentType);
            }
            if (charset == null) {
                if (this._characterEncoding != null) {
                    this._contentType = contentType + "; charset=" + this._characterEncoding;
                    this._mimeType = null;
                }
            }
            else if (this.isWriting() && !charset.equals(this._characterEncoding)) {
                this._mimeType = null;
                this._contentType = MimeTypes.getContentTypeWithoutCharset(this._contentType);
                if (this._characterEncoding != null) {
                    this._contentType = this._contentType + "; charset=" + this._characterEncoding;
                }
            }
            else {
                this._characterEncoding = charset;
                this._explicitEncoding = true;
            }
            if (HttpGenerator.__STRICT || this._mimeType == null) {
                this._fields.put(HttpHeader.CONTENT_TYPE, this._contentType);
            }
            else {
                this._contentType = this._mimeType.asString();
                this._fields.put(this._mimeType.getContentTypeField());
            }
        }
    }
    
    @Override
    public void setBufferSize(int size) {
        if (this.isCommitted() || this.getContentCount() > 0L) {
            throw new IllegalStateException("Committed or content written");
        }
        if (size <= 0) {
            size = 1;
        }
        this._out.setBufferSize(size);
    }
    
    @Override
    public int getBufferSize() {
        return this._out.getBufferSize();
    }
    
    @Override
    public void flushBuffer() throws IOException {
        if (!this._out.isClosed()) {
            this._out.flush();
        }
    }
    
    @Override
    public void reset() {
        this.resetForForward();
        this._status = 200;
        this._reason = null;
        this._contentLength = -1L;
        this._fields.clear();
        final String connection = this._channel.getRequest().getHttpFields().getStringField(HttpHeader.CONNECTION);
        if (connection != null) {
            for (final String value : StringUtil.csvSplit(null, connection, 0, connection.length())) {
                final HttpHeaderValue cb = HttpHeaderValue.CACHE.get(value);
                if (cb != null) {
                    switch (cb) {
                        case CLOSE: {
                            this._fields.put(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE.toString());
                            continue;
                        }
                        case KEEP_ALIVE: {
                            if (HttpVersion.HTTP_1_0.is(this._channel.getRequest().getProtocol())) {
                                this._fields.put(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE.toString());
                                continue;
                            }
                            continue;
                        }
                        case TE: {
                            this._fields.put(HttpHeader.CONNECTION, HttpHeaderValue.TE.toString());
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    public void reset(final boolean preserveCookies) {
        if (!preserveCookies) {
            this.reset();
        }
        else {
            final ArrayList<String> cookieValues = new ArrayList<String>(5);
            final Enumeration<String> vals = this._fields.getValues(HttpHeader.SET_COOKIE.asString());
            while (vals.hasMoreElements()) {
                cookieValues.add(vals.nextElement());
            }
            this.reset();
            for (final String v : cookieValues) {
                this._fields.add(HttpHeader.SET_COOKIE, v);
            }
        }
    }
    
    public void resetForForward() {
        this.resetBuffer();
        this._outputType = OutputType.NONE;
    }
    
    @Override
    public void resetBuffer() {
        if (this.isCommitted()) {
            throw new IllegalStateException("Committed");
        }
        switch (this._outputType) {
            case WRITER:
            case STREAM: {
                this._out.reset();
                break;
            }
        }
        this._out.resetBuffer();
    }
    
    protected HttpGenerator.ResponseInfo newResponseInfo() {
        return new HttpGenerator.ResponseInfo(this._channel.getRequest().getHttpVersion(), this._fields, this.getLongContentLength(), this.getStatus(), this.getReason(), this._channel.getRequest().isHead());
    }
    
    @Override
    public boolean isCommitted() {
        return this._channel.isCommitted();
    }
    
    @Override
    public void setLocale(final Locale locale) {
        if (locale == null || this.isCommitted() || this.isIncluding()) {
            return;
        }
        this._locale = locale;
        this._fields.put(HttpHeader.CONTENT_LANGUAGE, locale.toString().replace('_', '-'));
        if (this._outputType != OutputType.NONE) {
            return;
        }
        if (this._channel.getRequest().getContext() == null) {
            return;
        }
        final String charset = this._channel.getRequest().getContext().getContextHandler().getLocaleEncoding(locale);
        if (charset != null && charset.length() > 0 && !this._explicitEncoding) {
            this.setCharacterEncoding(charset, false);
        }
    }
    
    @Override
    public Locale getLocale() {
        if (this._locale == null) {
            return Locale.getDefault();
        }
        return this._locale;
    }
    
    @Override
    public int getStatus() {
        return this._status;
    }
    
    public String getReason() {
        return this._reason;
    }
    
    public HttpFields getHttpFields() {
        return this._fields;
    }
    
    public long getContentCount() {
        return this._out.getWritten();
    }
    
    @Override
    public String toString() {
        return String.format("%s %d %s%n%s", this._channel.getRequest().getHttpVersion(), this._status, (this._reason == null) ? "" : this._reason, this._fields);
    }
    
    static {
        LOG = Log.getLogger(Response.class);
        __01Jan1970_COOKIE = DateGenerator.formatCookieDate(0L).trim();
        __cookieBuilder = new ThreadLocal<StringBuilder>() {
            @Override
            protected StringBuilder initialValue() {
                return new StringBuilder(128);
            }
        };
    }
    
    public enum OutputType
    {
        NONE, 
        STREAM, 
        WRITER;
    }
    
    private static class ResponseWriter extends PrintWriter
    {
        private final String _encoding;
        private final HttpWriter _httpWriter;
        
        public ResponseWriter(final HttpWriter httpWriter, final String encoding) {
            super(httpWriter);
            this._httpWriter = httpWriter;
            this._encoding = encoding;
        }
        
        public boolean isFor(final String encoding) {
            return this._encoding.equalsIgnoreCase(encoding);
        }
        
        protected void reopen() {
            super.clearError();
            this.out = this._httpWriter;
        }
    }
}
