// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.eclipse.jetty.util.StringUtil;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import org.eclipse.jetty.util.BufferUtil;
import java.nio.ByteBuffer;

public class HttpTester
{
    private HttpTester() {
    }
    
    public static Request newRequest() {
        return new Request();
    }
    
    public static Request parseRequest(final String request) {
        final Request r = new Request();
        final HttpParser parser = new HttpParser(r);
        parser.parseNext(BufferUtil.toBuffer(request));
        return r;
    }
    
    public static Request parseRequest(final ByteBuffer request) {
        final Request r = new Request();
        final HttpParser parser = new HttpParser(r);
        parser.parseNext(request);
        return r;
    }
    
    public static Response parseResponse(final String response) {
        final Response r = new Response();
        final HttpParser parser = new HttpParser(r);
        parser.parseNext(BufferUtil.toBuffer(response));
        return r;
    }
    
    public static Response parseResponse(final ByteBuffer response) {
        final Response r = new Response();
        final HttpParser parser = new HttpParser(r);
        parser.parseNext(response);
        return r;
    }
    
    public abstract static class Message extends HttpFields implements HttpParser.HttpHandler<ByteBuffer>
    {
        ByteArrayOutputStream _content;
        HttpVersion _version;
        
        public Message() {
            this._version = HttpVersion.HTTP_1_0;
        }
        
        public HttpVersion getVersion() {
            return this._version;
        }
        
        public void setVersion(final String version) {
            this.setVersion(HttpVersion.CACHE.get(version));
        }
        
        public void setVersion(final HttpVersion version) {
            this._version = version;
        }
        
        public void setContent(final byte[] bytes) {
            try {
                (this._content = new ByteArrayOutputStream()).write(bytes);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public void setContent(final String content) {
            try {
                (this._content = new ByteArrayOutputStream()).write(StringUtil.getBytes(content));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public void setContent(final ByteBuffer content) {
            try {
                (this._content = new ByteArrayOutputStream()).write(BufferUtil.toArray(content));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public boolean parsedHeader(final HttpField field) {
            this.put(field.getName(), field.getValue());
            return false;
        }
        
        @Override
        public boolean messageComplete() {
            return true;
        }
        
        @Override
        public boolean headerComplete() {
            this._content = new ByteArrayOutputStream();
            return false;
        }
        
        @Override
        public void earlyEOF() {
        }
        
        @Override
        public boolean content(final ByteBuffer ref) {
            try {
                this._content.write(BufferUtil.toArray(ref));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            return false;
        }
        
        @Override
        public void badMessage(final int status, final String reason) {
            throw new RuntimeException(reason);
        }
        
        public ByteBuffer generate() {
            try {
                final HttpGenerator generator = new HttpGenerator();
                final HttpGenerator.Info info = this.getInfo();
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                ByteBuffer header = null;
                ByteBuffer chunk = null;
                final ByteBuffer content = (this._content == null) ? null : ByteBuffer.wrap(this._content.toByteArray());
            Label_0248:
                while (!generator.isEnd()) {
                    final HttpGenerator.Result result = (info instanceof HttpGenerator.RequestInfo) ? generator.generateRequest((HttpGenerator.RequestInfo)info, header, chunk, content, true) : generator.generateResponse((HttpGenerator.ResponseInfo)info, header, chunk, content, true);
                    switch (result) {
                        case NEED_HEADER: {
                            header = BufferUtil.allocate(8192);
                            continue;
                        }
                        case NEED_CHUNK: {
                            chunk = BufferUtil.allocate(12);
                            continue;
                        }
                        case NEED_INFO: {
                            throw new IllegalStateException();
                        }
                        case FLUSH: {
                            if (BufferUtil.hasContent(header)) {
                                out.write(BufferUtil.toArray(header));
                                BufferUtil.clear(header);
                            }
                            if (BufferUtil.hasContent(chunk)) {
                                out.write(BufferUtil.toArray(chunk));
                                BufferUtil.clear(chunk);
                            }
                            if (BufferUtil.hasContent(content)) {
                                out.write(BufferUtil.toArray(content));
                                BufferUtil.clear(content);
                                continue;
                            }
                            continue;
                        }
                        case SHUTDOWN_OUT: {
                            break Label_0248;
                        }
                    }
                }
                return ByteBuffer.wrap(out.toByteArray());
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public abstract HttpGenerator.Info getInfo();
        
        @Override
        public int getHeaderCacheSize() {
            return 0;
        }
    }
    
    public static class Request extends Message implements HttpParser.RequestHandler<ByteBuffer>
    {
        private String _method;
        private String _uri;
        
        @Override
        public boolean startRequest(final HttpMethod method, final String methodString, final ByteBuffer uri, final HttpVersion version) {
            this._method = methodString;
            this._uri = BufferUtil.toUTF8String(uri);
            this._version = version;
            return false;
        }
        
        public String getMethod() {
            return this._method;
        }
        
        public String getUri() {
            return this._uri;
        }
        
        public void setMethod(final String method) {
            this._method = method;
        }
        
        public void setURI(final String uri) {
            this._uri = uri;
        }
        
        @Override
        public HttpGenerator.RequestInfo getInfo() {
            return new HttpGenerator.RequestInfo(this._version, this, (this._content == null) ? 0L : this._content.size(), this._method, this._uri);
        }
        
        @Override
        public String toString() {
            return String.format("%s %s %s\n%s\n", this._method, this._uri, this._version, super.toString());
        }
        
        public void setHeader(final String name, final String value) {
            this.put(name, value);
        }
        
        @Override
        public boolean parsedHostHeader(final String host, final int port) {
            return false;
        }
    }
    
    public static class Response extends Message implements HttpParser.ResponseHandler<ByteBuffer>
    {
        private int _status;
        private String _reason;
        
        @Override
        public boolean startResponse(final HttpVersion version, final int status, final String reason) {
            this._version = version;
            this._status = status;
            this._reason = reason;
            return false;
        }
        
        public int getStatus() {
            return this._status;
        }
        
        public String getReason() {
            return this._reason;
        }
        
        public byte[] getContentBytes() {
            if (this._content == null) {
                return null;
            }
            return this._content.toByteArray();
        }
        
        public String getContent() {
            if (this._content == null) {
                return null;
            }
            final byte[] bytes = this._content.toByteArray();
            final String content_type = this.get(HttpHeader.CONTENT_TYPE);
            final String encoding = MimeTypes.getCharsetFromContentType(content_type);
            final Charset charset = (encoding == null) ? StandardCharsets.UTF_8 : Charset.forName(encoding);
            return new String(bytes, charset);
        }
        
        @Override
        public HttpGenerator.ResponseInfo getInfo() {
            return new HttpGenerator.ResponseInfo(this._version, this, (this._content == null) ? -1L : this._content.size(), this._status, this._reason, false);
        }
        
        @Override
        public String toString() {
            return String.format("%s %s %s\n%s\n", this._version, this._status, this._reason, super.toString());
        }
    }
}
