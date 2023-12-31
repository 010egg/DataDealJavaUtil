// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.http;

import org.eclipse.jetty.util.ArrayTrie;
import org.eclipse.jetty.util.StringUtil;
import java.nio.ByteBuffer;
import org.eclipse.jetty.util.Trie;

public enum HttpHeader
{
    CONNECTION("Connection"), 
    CACHE_CONTROL("Cache-Control"), 
    DATE("Date"), 
    PRAGMA("Pragma"), 
    PROXY_CONNECTION("Proxy-Connection"), 
    TRAILER("Trailer"), 
    TRANSFER_ENCODING("Transfer-Encoding"), 
    UPGRADE("Upgrade"), 
    VIA("Via"), 
    WARNING("Warning"), 
    NEGOTIATE("Negotiate"), 
    ALLOW("Allow"), 
    CONTENT_ENCODING("Content-Encoding"), 
    CONTENT_LANGUAGE("Content-Language"), 
    CONTENT_LENGTH("Content-Length"), 
    CONTENT_LOCATION("Content-Location"), 
    CONTENT_MD5("Content-MD5"), 
    CONTENT_RANGE("Content-Range"), 
    CONTENT_TYPE("Content-Type"), 
    EXPIRES("Expires"), 
    LAST_MODIFIED("Last-Modified"), 
    ACCEPT("Accept"), 
    ACCEPT_CHARSET("Accept-Charset"), 
    ACCEPT_ENCODING("Accept-Encoding"), 
    ACCEPT_LANGUAGE("Accept-Language"), 
    AUTHORIZATION("Authorization"), 
    EXPECT("Expect"), 
    FORWARDED("Forwarded"), 
    FROM("From"), 
    HOST("Host"), 
    IF_MATCH("If-Match"), 
    IF_MODIFIED_SINCE("If-Modified-Since"), 
    IF_NONE_MATCH("If-None-Match"), 
    IF_RANGE("If-Range"), 
    IF_UNMODIFIED_SINCE("If-Unmodified-Since"), 
    KEEP_ALIVE("Keep-Alive"), 
    MAX_FORWARDS("Max-Forwards"), 
    PROXY_AUTHORIZATION("Proxy-Authorization"), 
    RANGE("Range"), 
    REQUEST_RANGE("Request-Range"), 
    REFERER("Referer"), 
    TE("TE"), 
    USER_AGENT("User-Agent"), 
    X_FORWARDED_FOR("X-Forwarded-For"), 
    X_FORWARDED_PROTO("X-Forwarded-Proto"), 
    X_FORWARDED_SERVER("X-Forwarded-Server"), 
    X_FORWARDED_HOST("X-Forwarded-Host"), 
    ACCEPT_RANGES("Accept-Ranges"), 
    AGE("Age"), 
    ETAG("ETag"), 
    LOCATION("Location"), 
    PROXY_AUTHENTICATE("Proxy-Authenticate"), 
    RETRY_AFTER("Retry-After"), 
    SERVER("Server"), 
    SERVLET_ENGINE("Servlet-Engine"), 
    VARY("Vary"), 
    WWW_AUTHENTICATE("WWW-Authenticate"), 
    COOKIE("Cookie"), 
    SET_COOKIE("Set-Cookie"), 
    SET_COOKIE2("Set-Cookie2"), 
    MIME_VERSION("MIME-Version"), 
    IDENTITY("identity"), 
    X_POWERED_BY("X-Powered-By"), 
    UNKNOWN("::UNKNOWN::");
    
    public static final Trie<HttpHeader> CACHE;
    private final String _string;
    private final byte[] _bytes;
    private final byte[] _bytesColonSpace;
    private final ByteBuffer _buffer;
    
    private HttpHeader(final String s) {
        this._string = s;
        this._bytes = StringUtil.getBytes(s);
        this._bytesColonSpace = StringUtil.getBytes(s + ": ");
        this._buffer = ByteBuffer.wrap(this._bytes);
    }
    
    public ByteBuffer toBuffer() {
        return this._buffer.asReadOnlyBuffer();
    }
    
    public byte[] getBytes() {
        return this._bytes;
    }
    
    public byte[] getBytesColonSpace() {
        return this._bytesColonSpace;
    }
    
    public boolean is(final String s) {
        return this._string.equalsIgnoreCase(s);
    }
    
    public String asString() {
        return this._string;
    }
    
    @Override
    public String toString() {
        return this._string;
    }
    
    static {
        CACHE = new ArrayTrie<HttpHeader>(512);
        for (final HttpHeader header : values()) {
            if (header != HttpHeader.UNKNOWN) {
                HttpHeader.CACHE.put(header.toString(), header);
            }
        }
    }
}
