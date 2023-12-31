// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server.handler;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.BufferUtil;
import java.nio.ByteBuffer;
import org.eclipse.jetty.http.HttpFields;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.eclipse.jetty.http.HttpStatus;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.ByteArrayISO8859Writer;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.MimeTypes;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import org.eclipse.jetty.server.Dispatcher;
import org.eclipse.jetty.http.HttpMethod;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.util.log.Logger;

public class ErrorHandler extends AbstractHandler
{
    private static final Logger LOG;
    public static final String ERROR_PAGE = "org.eclipse.jetty.server.error_page";
    boolean _showStacks;
    boolean _showMessageInTitle;
    String _cacheControl;
    
    public ErrorHandler() {
        this._showStacks = true;
        this._showMessageInTitle = true;
        this._cacheControl = "must-revalidate,no-cache,no-store";
    }
    
    @Override
    public void handle(final String target, final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String method = request.getMethod();
        if (!HttpMethod.GET.is(method) && !HttpMethod.POST.is(method) && !HttpMethod.HEAD.is(method)) {
            baseRequest.setHandled(true);
            return;
        }
        if (this instanceof ErrorPageMapper) {
            final String error_page = ((ErrorPageMapper)this).getErrorPage(request);
            if (error_page != null && request.getServletContext() != null) {
                final String old_error_page = (String)request.getAttribute("org.eclipse.jetty.server.error_page");
                if (old_error_page == null || !old_error_page.equals(error_page)) {
                    request.setAttribute("org.eclipse.jetty.server.error_page", error_page);
                    final Dispatcher dispatcher = (Dispatcher)request.getServletContext().getRequestDispatcher(error_page);
                    try {
                        if (dispatcher != null) {
                            dispatcher.error(request, response);
                            return;
                        }
                        ErrorHandler.LOG.warn("No error page " + error_page, new Object[0]);
                    }
                    catch (ServletException e) {
                        ErrorHandler.LOG.warn("EXCEPTION ", e);
                        return;
                    }
                }
            }
        }
        baseRequest.setHandled(true);
        response.setContentType(MimeTypes.Type.TEXT_HTML_8859_1.asString());
        if (this._cacheControl != null) {
            response.setHeader(HttpHeader.CACHE_CONTROL.asString(), this._cacheControl);
        }
        final ByteArrayISO8859Writer writer = new ByteArrayISO8859Writer(4096);
        final String reason = (response instanceof Response) ? ((Response)response).getReason() : null;
        this.handleErrorPage(request, writer, response.getStatus(), reason);
        writer.flush();
        response.setContentLength(writer.size());
        writer.writeTo(response.getOutputStream());
        writer.destroy();
    }
    
    protected void handleErrorPage(final HttpServletRequest request, final Writer writer, final int code, final String message) throws IOException {
        this.writeErrorPage(request, writer, code, message, this._showStacks);
    }
    
    protected void writeErrorPage(final HttpServletRequest request, final Writer writer, final int code, String message, final boolean showStacks) throws IOException {
        if (message == null) {
            message = HttpStatus.getMessage(code);
        }
        writer.write("<html>\n<head>\n");
        this.writeErrorPageHead(request, writer, code, message);
        writer.write("</head>\n<body>");
        this.writeErrorPageBody(request, writer, code, message, showStacks);
        writer.write("\n</body>\n</html>\n");
    }
    
    protected void writeErrorPageHead(final HttpServletRequest request, final Writer writer, final int code, final String message) throws IOException {
        writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n");
        writer.write("<title>Error ");
        writer.write(Integer.toString(code));
        if (this._showMessageInTitle) {
            writer.write(32);
            this.write(writer, message);
        }
        writer.write("</title>\n");
    }
    
    protected void writeErrorPageBody(final HttpServletRequest request, final Writer writer, final int code, final String message, final boolean showStacks) throws IOException {
        final String uri = request.getRequestURI();
        this.writeErrorPageMessage(request, writer, code, message, uri);
        if (showStacks) {
            this.writeErrorPageStacks(request, writer);
        }
        writer.write("<hr><i><small>Powered by Jetty://</small></i><hr/>\n");
    }
    
    protected void writeErrorPageMessage(final HttpServletRequest request, final Writer writer, final int code, final String message, final String uri) throws IOException {
        writer.write("<h2>HTTP ERROR ");
        writer.write(Integer.toString(code));
        writer.write("</h2>\n<p>Problem accessing ");
        this.write(writer, uri);
        writer.write(". Reason:\n<pre>    ");
        this.write(writer, message);
        writer.write("</pre></p>");
    }
    
    protected void writeErrorPageStacks(final HttpServletRequest request, final Writer writer) throws IOException {
        for (Throwable th = (Throwable)request.getAttribute("javax.servlet.error.exception"); th != null; th = th.getCause()) {
            writer.write("<h3>Caused by:</h3><pre>");
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            th.printStackTrace(pw);
            pw.flush();
            this.write(writer, sw.getBuffer().toString());
            writer.write("</pre>\n");
        }
    }
    
    public ByteBuffer badMessageError(final int status, String reason, final HttpFields fields) {
        if (reason == null) {
            reason = HttpStatus.getMessage(status);
        }
        fields.put(HttpHeader.CONTENT_TYPE, MimeTypes.Type.TEXT_HTML_8859_1.asString());
        return BufferUtil.toBuffer("<h1>Bad Message " + status + "</h1><pre>reason: " + reason + "</pre>");
    }
    
    public String getCacheControl() {
        return this._cacheControl;
    }
    
    public void setCacheControl(final String cacheControl) {
        this._cacheControl = cacheControl;
    }
    
    public boolean isShowStacks() {
        return this._showStacks;
    }
    
    public void setShowStacks(final boolean showStacks) {
        this._showStacks = showStacks;
    }
    
    public void setShowMessageInTitle(final boolean showMessageInTitle) {
        this._showMessageInTitle = showMessageInTitle;
    }
    
    public boolean getShowMessageInTitle() {
        return this._showMessageInTitle;
    }
    
    protected void write(final Writer writer, final String string) throws IOException {
        if (string == null) {
            return;
        }
        writer.write(StringUtil.sanitizeXmlString(string));
    }
    
    public static ErrorHandler getErrorHandler(final Server server, final ContextHandler context) {
        ErrorHandler error_handler = null;
        if (context != null) {
            error_handler = context.getErrorHandler();
        }
        if (error_handler == null && server != null) {
            error_handler = server.getBean(ErrorHandler.class);
        }
        return error_handler;
    }
    
    static {
        LOG = Log.getLogger(ErrorHandler.class);
    }
    
    public interface ErrorPageMapper
    {
        String getErrorPage(final HttpServletRequest p0);
    }
}
