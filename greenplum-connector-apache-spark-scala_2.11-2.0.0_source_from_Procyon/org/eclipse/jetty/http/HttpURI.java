// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.http;

import java.io.UnsupportedEncodingException;
import org.eclipse.jetty.util.UrlEncoded;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.Utf8StringBuilder;
import org.eclipse.jetty.util.TypeUtil;
import org.eclipse.jetty.util.StringUtil;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.eclipse.jetty.util.URIUtil;
import java.nio.charset.Charset;

public class HttpURI
{
    private static final byte[] __empty;
    private static final int START = 0;
    private static final int AUTH_OR_PATH = 1;
    private static final int SCHEME_OR_PATH = 2;
    private static final int AUTH = 4;
    private static final int IPV6 = 5;
    private static final int PORT = 6;
    private static final int PATH = 7;
    private static final int PARAM = 8;
    private static final int QUERY = 9;
    private static final int ASTERISK = 10;
    final Charset _charset;
    boolean _partial;
    byte[] _raw;
    String _rawString;
    int _scheme;
    int _authority;
    int _host;
    int _port;
    int _portValue;
    int _path;
    int _param;
    int _query;
    int _fragment;
    int _end;
    boolean _encoded;
    
    public HttpURI() {
        this._partial = false;
        this._raw = HttpURI.__empty;
        this._encoded = false;
        this._charset = URIUtil.__CHARSET;
    }
    
    public HttpURI(final Charset charset) {
        this._partial = false;
        this._raw = HttpURI.__empty;
        this._encoded = false;
        this._charset = charset;
    }
    
    public HttpURI(final boolean parsePartialAuth) {
        this._partial = false;
        this._raw = HttpURI.__empty;
        this._encoded = false;
        this._partial = parsePartialAuth;
        this._charset = URIUtil.__CHARSET;
    }
    
    public HttpURI(final String raw) {
        this._partial = false;
        this._raw = HttpURI.__empty;
        this._encoded = false;
        this._rawString = raw;
        final byte[] b = raw.getBytes(StandardCharsets.UTF_8);
        this.parse(b, 0, b.length);
        this._charset = URIUtil.__CHARSET;
    }
    
    public HttpURI(final byte[] raw, final int offset, final int length) {
        this._partial = false;
        this._raw = HttpURI.__empty;
        this._encoded = false;
        this.parse2(raw, offset, length);
        this._charset = URIUtil.__CHARSET;
    }
    
    public HttpURI(final URI uri) {
        this._partial = false;
        this._raw = HttpURI.__empty;
        this._encoded = false;
        this.parse(uri.toASCIIString());
        this._charset = URIUtil.__CHARSET;
    }
    
    public void parse(final String raw) {
        final byte[] b = StringUtil.getUtf8Bytes(raw);
        this.parse2(b, 0, b.length);
        this._rawString = raw;
    }
    
    public void parseConnect(final String raw) {
        final byte[] b = StringUtil.getBytes(raw);
        this.parseConnect(b, 0, b.length);
        this._rawString = raw;
    }
    
    public void parse(final byte[] raw, final int offset, final int length) {
        this._rawString = null;
        this.parse2(raw, offset, length);
    }
    
    public void parseConnect(final byte[] raw, final int offset, final int length) {
        this._rawString = null;
        this._encoded = false;
        this._raw = raw;
        int i = offset;
        final int e = offset + length;
        int state = 4;
        this._end = offset + length;
        this._scheme = offset;
        this._authority = offset;
        this._host = offset;
        this._port = this._end;
        this._portValue = -1;
        this._path = this._end;
        this._param = this._end;
        this._query = this._end;
        this._fragment = this._end;
    Label_0272:
        while (i < e) {
            final char c = (char)(0xFF & this._raw[i]);
            final int s = i++;
            switch (state) {
                case 4: {
                    switch (c) {
                        case ':': {
                            this._port = s;
                            break Label_0272;
                        }
                        case '[': {
                            state = 5;
                            continue;
                        }
                    }
                    continue;
                }
                case 5: {
                    switch (c) {
                        case '/': {
                            throw new IllegalArgumentException("No closing ']' for " + new String(this._raw, offset, length, this._charset));
                        }
                        case ']': {
                            state = 4;
                            continue;
                        }
                    }
                    continue;
                }
                default: {
                    continue;
                }
            }
        }
        if (this._port < this._path) {
            this._portValue = TypeUtil.parseInt(this._raw, this._port + 1, this._path - this._port - 1, 10);
            this._path = offset;
            return;
        }
        throw new IllegalArgumentException("No port");
    }
    
    private void parse2(final byte[] raw, final int offset, final int length) {
        this._encoded = false;
        this._raw = raw;
        int i = offset;
        final int e = offset + length;
        int state = 0;
        int m = offset;
        this._end = offset + length;
        this._scheme = offset;
        this._authority = offset;
        this._host = offset;
        this._port = offset;
        this._portValue = -1;
        this._path = offset;
        this._param = this._end;
        this._query = this._end;
        this._fragment = this._end;
        while (i < e) {
            char c = (char)(0xFF & this._raw[i]);
            int s = i++;
            switch (state) {
                case 0: {
                    m = s;
                    switch (c) {
                        case '/': {
                            state = 1;
                            continue;
                        }
                        case ';': {
                            this._param = s;
                            state = 8;
                            continue;
                        }
                        case '?': {
                            this._param = s;
                            this._query = s;
                            state = 9;
                            continue;
                        }
                        case '#': {
                            this._param = s;
                            this._query = s;
                            this._fragment = s;
                            continue;
                        }
                        case '*': {
                            this._path = s;
                            state = 10;
                            continue;
                        }
                        default: {
                            state = 2;
                            continue;
                        }
                    }
                    break;
                }
                case 1: {
                    if ((this._partial || this._scheme != this._authority) && c == '/') {
                        this._host = i;
                        this._port = this._end;
                        this._path = this._end;
                        state = 4;
                        continue;
                    }
                    if (c == ';' || c == '?' || c == '#') {
                        --i;
                        state = 7;
                        continue;
                    }
                    this._host = m;
                    this._port = m;
                    state = 7;
                    continue;
                }
                case 2: {
                    if (length > 6 && c == 't') {
                        if (this._raw[offset + 3] == 58) {
                            s = offset + 3;
                            i = offset + 4;
                            c = ':';
                        }
                        else if (this._raw[offset + 4] == 58) {
                            s = offset + 4;
                            i = offset + 5;
                            c = ':';
                        }
                        else if (this._raw[offset + 5] == 58) {
                            s = offset + 5;
                            i = offset + 6;
                            c = ':';
                        }
                    }
                    switch (c) {
                        case ':': {
                            m = i++;
                            this._authority = m;
                            this._path = m;
                            c = (char)(0xFF & this._raw[i]);
                            if (c == '/') {
                                state = 1;
                                continue;
                            }
                            this._host = m;
                            this._port = m;
                            state = 7;
                            continue;
                        }
                        case '/': {
                            state = 7;
                            continue;
                        }
                        case ';': {
                            this._param = s;
                            state = 8;
                            continue;
                        }
                        case '?': {
                            this._param = s;
                            this._query = s;
                            state = 9;
                            continue;
                        }
                        case '#': {
                            this._param = s;
                            this._query = s;
                            this._fragment = s;
                            continue;
                        }
                    }
                    continue;
                }
                case 4: {
                    switch (c) {
                        case '/': {
                            m = s;
                            this._path = m;
                            this._port = this._path;
                            state = 7;
                            continue;
                        }
                        case '@': {
                            this._host = i;
                            continue;
                        }
                        case ':': {
                            this._port = s;
                            state = 6;
                            continue;
                        }
                        case '[': {
                            state = 5;
                            continue;
                        }
                    }
                    continue;
                }
                case 5: {
                    switch (c) {
                        case '/': {
                            throw new IllegalArgumentException("No closing ']' for " + new String(this._raw, offset, length, this._charset));
                        }
                        case ']': {
                            state = 4;
                            continue;
                        }
                    }
                    continue;
                }
                case 6: {
                    if (c == '/') {
                        m = s;
                        this._path = m;
                        if (this._port <= this._authority) {
                            this._port = this._path;
                        }
                        state = 7;
                        continue;
                    }
                    continue;
                }
                case 7: {
                    switch (c) {
                        case ';': {
                            this._param = s;
                            state = 8;
                            continue;
                        }
                        case '?': {
                            this._param = s;
                            this._query = s;
                            state = 9;
                            continue;
                        }
                        case '#': {
                            this._param = s;
                            this._query = s;
                            this._fragment = s;
                            continue;
                        }
                        case '%': {
                            this._encoded = true;
                            continue;
                        }
                    }
                    continue;
                }
                case 8: {
                    switch (c) {
                        case '?': {
                            this._query = s;
                            state = 9;
                            continue;
                        }
                        case '#': {
                            this._query = s;
                            this._fragment = s;
                            continue;
                        }
                    }
                    continue;
                }
                case 9: {
                    if (c == '#') {
                        this._fragment = s;
                        continue;
                    }
                    continue;
                }
                case 10: {
                    throw new IllegalArgumentException("only '*'");
                }
            }
        }
        if (this._port < this._path) {
            this._portValue = TypeUtil.parseInt(this._raw, this._port + 1, this._path - this._port - 1, 10);
        }
    }
    
    public String getScheme() {
        if (this._scheme == this._authority) {
            return null;
        }
        final int l = this._authority - this._scheme;
        if (l == 5 && this._raw[this._scheme] == 104 && this._raw[this._scheme + 1] == 116 && this._raw[this._scheme + 2] == 116 && this._raw[this._scheme + 3] == 112) {
            return HttpScheme.HTTP.asString();
        }
        if (l == 6 && this._raw[this._scheme] == 104 && this._raw[this._scheme + 1] == 116 && this._raw[this._scheme + 2] == 116 && this._raw[this._scheme + 3] == 112 && this._raw[this._scheme + 4] == 115) {
            return HttpScheme.HTTPS.asString();
        }
        return new String(this._raw, this._scheme, this._authority - this._scheme - 1, this._charset);
    }
    
    public String getAuthority() {
        if (this._authority == this._path) {
            return null;
        }
        return new String(this._raw, this._authority, this._path - this._authority, this._charset);
    }
    
    public String getHost() {
        if (this._host == this._port) {
            return null;
        }
        return new String(this._raw, this._host, this._port - this._host, this._charset);
    }
    
    public int getPort() {
        return this._portValue;
    }
    
    public String getPath() {
        if (this._path == this._param) {
            return null;
        }
        return new String(this._raw, this._path, this._param - this._path, this._charset);
    }
    
    public String getDecodedPath() {
        if (this._path == this._param) {
            return null;
        }
        Utf8StringBuilder utf8b = null;
        for (int i = this._path; i < this._param; ++i) {
            byte b = this._raw[i];
            if (b == 37) {
                if (utf8b == null) {
                    utf8b = new Utf8StringBuilder();
                    utf8b.append(this._raw, this._path, i - this._path);
                }
                if (i + 2 >= this._param) {
                    throw new IllegalArgumentException("Bad % encoding: " + this);
                }
                if (this._raw[i + 1] == 117) {
                    if (i + 5 >= this._param) {
                        throw new IllegalArgumentException("Bad %u encoding: " + this);
                    }
                    try {
                        final String unicode = new String(Character.toChars(TypeUtil.parseInt(this._raw, i + 2, 4, 16)));
                        utf8b.getStringBuilder().append(unicode);
                        i += 5;
                        continue;
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                b = (byte)(0xFF & TypeUtil.parseInt(this._raw, i + 1, 2, 16));
                utf8b.append(b);
                i += 2;
            }
            else if (utf8b != null) {
                utf8b.append(b);
            }
        }
        if (utf8b == null) {
            return StringUtil.toUTF8String(this._raw, this._path, this._param - this._path);
        }
        return utf8b.toString();
    }
    
    public String getDecodedPath(final String encoding) {
        return this.getDecodedPath(Charset.forName(encoding));
    }
    
    public String getDecodedPath(final Charset encoding) {
        if (this._path == this._param) {
            return null;
        }
        final int length = this._param - this._path;
        byte[] bytes = null;
        int n = 0;
        for (int i = this._path; i < this._param; ++i) {
            byte b = this._raw[i];
            if (b == 37) {
                if (bytes == null) {
                    bytes = new byte[length];
                    System.arraycopy(this._raw, this._path, bytes, 0, n);
                }
                if (i + 2 >= this._param) {
                    throw new IllegalArgumentException("Bad % encoding: " + this);
                }
                if (this._raw[i + 1] == 117) {
                    if (i + 5 >= this._param) {
                        throw new IllegalArgumentException("Bad %u encoding: " + this);
                    }
                    try {
                        final String unicode = new String(Character.toChars(TypeUtil.parseInt(this._raw, i + 2, 4, 16)));
                        final byte[] encoded = unicode.getBytes(encoding);
                        System.arraycopy(encoded, 0, bytes, n, encoded.length);
                        n += encoded.length;
                        i += 5;
                        continue;
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                b = (byte)(0xFF & TypeUtil.parseInt(this._raw, i + 1, 2, 16));
                bytes[n++] = b;
                i += 2;
            }
            else if (bytes == null) {
                ++n;
            }
            else {
                bytes[n++] = b;
            }
        }
        if (bytes == null) {
            return new String(this._raw, this._path, this._param - this._path, encoding);
        }
        return new String(bytes, 0, n, encoding);
    }
    
    public String getPathAndParam() {
        if (this._path == this._query) {
            return null;
        }
        return new String(this._raw, this._path, this._query - this._path, this._charset);
    }
    
    public String getCompletePath() {
        if (this._path == this._end) {
            return null;
        }
        return new String(this._raw, this._path, this._end - this._path, this._charset);
    }
    
    public String getParam() {
        if (this._param == this._query) {
            return null;
        }
        return new String(this._raw, this._param + 1, this._query - this._param - 1, this._charset);
    }
    
    public String getQuery() {
        if (this._query == this._fragment) {
            return null;
        }
        return new String(this._raw, this._query + 1, this._fragment - this._query - 1, this._charset);
    }
    
    public String getQuery(final String encoding) {
        if (this._query == this._fragment) {
            return null;
        }
        return StringUtil.toString(this._raw, this._query + 1, this._fragment - this._query - 1, encoding);
    }
    
    public boolean hasQuery() {
        return this._fragment > this._query;
    }
    
    public String getFragment() {
        if (this._fragment == this._end) {
            return null;
        }
        return new String(this._raw, this._fragment + 1, this._end - this._fragment - 1, this._charset);
    }
    
    public void decodeQueryTo(final MultiMap<String> parameters) {
        if (this._query == this._fragment) {
            return;
        }
        if (this._charset.equals(StandardCharsets.UTF_8)) {
            UrlEncoded.decodeUtf8To(this._raw, this._query + 1, this._fragment - this._query - 1, parameters);
        }
        else {
            UrlEncoded.decodeTo(new String(this._raw, this._query + 1, this._fragment - this._query - 1, this._charset), parameters, this._charset, -1);
        }
    }
    
    public void decodeQueryTo(final MultiMap<String> parameters, final String encoding) throws UnsupportedEncodingException {
        if (this._query == this._fragment) {
            return;
        }
        if (encoding == null || StringUtil.isUTF8(encoding)) {
            UrlEncoded.decodeUtf8To(this._raw, this._query + 1, this._fragment - this._query - 1, parameters);
        }
        else {
            UrlEncoded.decodeTo(StringUtil.toString(this._raw, this._query + 1, this._fragment - this._query - 1, encoding), parameters, encoding, -1);
        }
    }
    
    public void decodeQueryTo(final MultiMap<String> parameters, final Charset encoding) throws UnsupportedEncodingException {
        if (this._query == this._fragment) {
            return;
        }
        if (encoding == null || StandardCharsets.UTF_8.equals(encoding)) {
            UrlEncoded.decodeUtf8To(this._raw, this._query + 1, this._fragment - this._query - 1, parameters);
        }
        else {
            UrlEncoded.decodeTo(new String(this._raw, this._query + 1, this._fragment - this._query - 1, encoding), parameters, encoding, -1);
        }
    }
    
    public void clear() {
        final int scheme = 0;
        this._end = scheme;
        this._fragment = scheme;
        this._query = scheme;
        this._param = scheme;
        this._path = scheme;
        this._port = scheme;
        this._host = scheme;
        this._authority = scheme;
        this._scheme = scheme;
        this._raw = HttpURI.__empty;
        this._rawString = "";
        this._encoded = false;
    }
    
    @Override
    public String toString() {
        if (this._rawString == null) {
            this._rawString = new String(this._raw, this._scheme, this._end - this._scheme, this._charset);
        }
        return this._rawString;
    }
    
    public void writeTo(final Utf8StringBuilder buf) {
        buf.append(this._raw, this._scheme, this._end - this._scheme);
    }
    
    static {
        __empty = new byte[0];
    }
}
