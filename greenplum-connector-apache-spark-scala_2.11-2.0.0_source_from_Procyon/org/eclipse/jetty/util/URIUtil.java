// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util;

import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;

public class URIUtil implements Cloneable
{
    public static final String SLASH = "/";
    public static final String HTTP = "http";
    public static final String HTTP_COLON = "http:";
    public static final String HTTPS = "https";
    public static final String HTTPS_COLON = "https:";
    public static final Charset __CHARSET;
    
    private URIUtil() {
    }
    
    public static String encodePath(final String path) {
        if (path == null || path.length() == 0) {
            return path;
        }
        final StringBuilder buf = encodePath(null, path);
        return (buf == null) ? path : buf.toString();
    }
    
    public static StringBuilder encodePath(StringBuilder buf, final String path) {
        byte[] bytes = null;
        if (buf == null) {
            int i = 0;
        Label_0223:
            while (i < path.length()) {
                final char c = path.charAt(i);
                switch (c) {
                    case ' ':
                    case '\"':
                    case '#':
                    case '%':
                    case '\'':
                    case ';':
                    case '<':
                    case '>':
                    case '?': {
                        buf = new StringBuilder(path.length() * 2);
                        break Label_0223;
                    }
                    default: {
                        if (c > '\u007f') {
                            bytes = path.getBytes(URIUtil.__CHARSET);
                            buf = new StringBuilder(path.length() * 2);
                            break Label_0223;
                        }
                        ++i;
                        continue;
                    }
                }
            }
            if (buf == null) {
                return null;
            }
        }
        synchronized (buf) {
            if (bytes != null) {
                for (int j = 0; j < bytes.length; ++j) {
                    final byte c2 = bytes[j];
                    switch (c2) {
                        case 37: {
                            buf.append("%25");
                            break;
                        }
                        case 63: {
                            buf.append("%3F");
                            break;
                        }
                        case 59: {
                            buf.append("%3B");
                            break;
                        }
                        case 35: {
                            buf.append("%23");
                            break;
                        }
                        case 34: {
                            buf.append("%22");
                            break;
                        }
                        case 39: {
                            buf.append("%27");
                            break;
                        }
                        case 60: {
                            buf.append("%3C");
                            break;
                        }
                        case 62: {
                            buf.append("%3E");
                            break;
                        }
                        case 32: {
                            buf.append("%20");
                            break;
                        }
                        default: {
                            if (c2 < 0) {
                                buf.append('%');
                                TypeUtil.toHex(c2, buf);
                                break;
                            }
                            buf.append((char)c2);
                            break;
                        }
                    }
                }
            }
            else {
                for (int j = 0; j < path.length(); ++j) {
                    final char c3 = path.charAt(j);
                    switch (c3) {
                        case '%': {
                            buf.append("%25");
                            break;
                        }
                        case '?': {
                            buf.append("%3F");
                            break;
                        }
                        case ';': {
                            buf.append("%3B");
                            break;
                        }
                        case '#': {
                            buf.append("%23");
                            break;
                        }
                        case '\"': {
                            buf.append("%22");
                            break;
                        }
                        case '\'': {
                            buf.append("%27");
                            break;
                        }
                        case '<': {
                            buf.append("%3C");
                            break;
                        }
                        case '>': {
                            buf.append("%3E");
                            break;
                        }
                        case ' ': {
                            buf.append("%20");
                            break;
                        }
                        default: {
                            buf.append(c3);
                            break;
                        }
                    }
                }
            }
        }
        return buf;
    }
    
    public static StringBuilder encodeString(StringBuilder buf, final String path, final String encode) {
        if (buf == null) {
            for (int i = 0; i < path.length(); ++i) {
                final char c = path.charAt(i);
                if (c == '%' || encode.indexOf(c) >= 0) {
                    buf = new StringBuilder(path.length() << 1);
                    break;
                }
            }
            if (buf == null) {
                return null;
            }
        }
        synchronized (buf) {
            for (int j = 0; j < path.length(); ++j) {
                final char c2 = path.charAt(j);
                if (c2 == '%' || encode.indexOf(c2) >= 0) {
                    buf.append('%');
                    StringUtil.append(buf, (byte)('\u00ff' & c2), 16);
                }
                else {
                    buf.append(c2);
                }
            }
        }
        return buf;
    }
    
    public static String decodePath(final String path) {
        if (path == null) {
            return null;
        }
        char[] chars = null;
        int n = 0;
        byte[] bytes = null;
        int b = 0;
        for (int len = path.length(), i = 0; i < len; ++i) {
            final char c = path.charAt(i);
            if (c == '%' && i + 2 < len) {
                if (chars == null) {
                    chars = new char[len];
                    bytes = new byte[len];
                    path.getChars(0, i, chars, 0);
                }
                bytes[b++] = (byte)(0xFF & TypeUtil.parseInt(path, i + 1, 2, 16));
                i += 2;
            }
            else if (c == ';') {
                if (chars == null) {
                    chars = new char[len];
                    path.getChars(0, i, chars, 0);
                    n = i;
                    break;
                }
                break;
            }
            else if (bytes == null) {
                ++n;
            }
            else {
                if (b > 0) {
                    final String s = new String(bytes, 0, b, URIUtil.__CHARSET);
                    s.getChars(0, s.length(), chars, n);
                    n += s.length();
                    b = 0;
                }
                chars[n++] = c;
            }
        }
        if (chars == null) {
            return path;
        }
        if (b > 0) {
            final String s2 = new String(bytes, 0, b, URIUtil.__CHARSET);
            s2.getChars(0, s2.length(), chars, n);
            n += s2.length();
        }
        return new String(chars, 0, n);
    }
    
    public static String decodePath(final byte[] buf, final int offset, int length) {
        byte[] bytes = null;
        int n = 0;
        for (int i = 0; i < length; ++i) {
            byte b = buf[i + offset];
            if (b == 37 && i + 2 < length) {
                b = (byte)(0xFF & TypeUtil.parseInt(buf, i + offset + 1, 2, 16));
                i += 2;
            }
            else {
                if (b == 59) {
                    length = i;
                    break;
                }
                if (bytes == null) {
                    ++n;
                    continue;
                }
            }
            if (bytes == null) {
                bytes = new byte[length];
                for (int j = 0; j < n; ++j) {
                    bytes[j] = buf[j + offset];
                }
            }
            bytes[n++] = b;
        }
        if (bytes == null) {
            return new String(buf, offset, length, URIUtil.__CHARSET);
        }
        return new String(bytes, 0, n, URIUtil.__CHARSET);
    }
    
    public static String addPaths(final String p1, final String p2) {
        if (p1 == null || p1.length() == 0) {
            if (p1 != null && p2 == null) {
                return p1;
            }
            return p2;
        }
        else {
            if (p2 == null || p2.length() == 0) {
                return p1;
            }
            int split = p1.indexOf(59);
            if (split < 0) {
                split = p1.indexOf(63);
            }
            if (split == 0) {
                return p2 + p1;
            }
            if (split < 0) {
                split = p1.length();
            }
            final StringBuilder buf = new StringBuilder(p1.length() + p2.length() + 2);
            buf.append(p1);
            if (buf.charAt(split - 1) == '/') {
                if (p2.startsWith("/")) {
                    buf.deleteCharAt(split - 1);
                    buf.insert(split - 1, p2);
                }
                else {
                    buf.insert(split, p2);
                }
            }
            else if (p2.startsWith("/")) {
                buf.insert(split, p2);
            }
            else {
                buf.insert(split, '/');
                buf.insert(split + 1, p2);
            }
            return buf.toString();
        }
    }
    
    public static String parentPath(final String p) {
        if (p == null || "/".equals(p)) {
            return null;
        }
        final int slash = p.lastIndexOf(47, p.length() - 2);
        if (slash >= 0) {
            return p.substring(0, slash + 1);
        }
        return null;
    }
    
    public static String canonicalPath(final String path) {
        if (path == null || path.length() == 0) {
            return path;
        }
        int end = 0;
        int start = 0;
    Label_0114:
        for (end = path.length(), start = path.lastIndexOf(47, end); end > 0; end = start, start = path.lastIndexOf(47, end - 1)) {
            switch (end - start) {
                case 2: {
                    if (path.charAt(start + 1) != '.') {
                        break;
                    }
                    break Label_0114;
                }
                case 3: {
                    if (path.charAt(start + 1) != '.' || path.charAt(start + 2) != '.') {
                        break;
                    }
                    break Label_0114;
                }
            }
        }
        if (start >= end) {
            return path;
        }
        final StringBuilder buf = new StringBuilder(path);
        int delStart = -1;
        int delEnd = -1;
        int skip = 0;
        while (end > 0) {
            switch (end - start) {
                case 2: {
                    if (buf.charAt(start + 1) != '.') {
                        if (skip <= 0 || --skip != 0) {
                            break;
                        }
                        delStart = ((start >= 0) ? start : 0);
                        if (delStart > 0 && delEnd == buf.length() && buf.charAt(delEnd - 1) == '.') {
                            ++delStart;
                            break;
                        }
                        break;
                    }
                    else {
                        if (start < 0 && buf.length() > 2 && buf.charAt(1) == '/' && buf.charAt(2) == '/') {
                            break;
                        }
                        if (delEnd < 0) {
                            delEnd = end;
                        }
                        delStart = start;
                        if (delStart >= 0 && (delStart != 0 || buf.charAt(delStart) != '/')) {
                            if (end == buf.length()) {
                                ++delStart;
                            }
                            for (end = start--; start >= 0 && buf.charAt(start) != '/'; --start) {}
                            continue;
                        }
                        ++delStart;
                        if (delEnd < buf.length() && buf.charAt(delEnd) == '/') {
                            ++delEnd;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 3: {
                    if (buf.charAt(start + 1) == '.' && buf.charAt(start + 2) == '.') {
                        delStart = start;
                        if (delEnd < 0) {
                            delEnd = end;
                        }
                        ++skip;
                        for (end = start--; start >= 0 && buf.charAt(start) != '/'; --start) {}
                        continue;
                    }
                    if (skip <= 0 || --skip != 0) {
                        break;
                    }
                    delStart = ((start >= 0) ? start : 0);
                    if (delStart > 0 && delEnd == buf.length() && buf.charAt(delEnd - 1) == '.') {
                        ++delStart;
                        break;
                    }
                    break;
                }
                default: {
                    if (skip <= 0 || --skip != 0) {
                        break;
                    }
                    delStart = ((start >= 0) ? start : 0);
                    if (delEnd == buf.length() && buf.charAt(delEnd - 1) == '.') {
                        ++delStart;
                        break;
                    }
                    break;
                }
            }
            if (skip <= 0 && delStart >= 0 && delEnd >= delStart) {
                buf.delete(delStart, delEnd);
                delEnd = (delStart = -1);
                if (skip > 0) {
                    delEnd = end;
                }
            }
            for (end = start--; start >= 0 && buf.charAt(start) != '/'; --start) {}
        }
        if (skip > 0) {
            return null;
        }
        if (delEnd >= 0) {
            buf.delete(delStart, delEnd);
        }
        return buf.toString();
    }
    
    public static String compactPath(final String path) {
        if (path == null || path.length() == 0) {
            return path;
        }
        int state = 0;
        int end = 0;
        int i = 0;
    Label_0085:
        for (end = path.length(), i = 0; i < end; ++i) {
            final char c = path.charAt(i);
            switch (c) {
                case '?': {
                    return path;
                }
                case '/': {
                    if (++state == 2) {
                        break Label_0085;
                    }
                    break;
                }
                default: {
                    state = 0;
                    break;
                }
            }
        }
        if (state < 2) {
            return path;
        }
        final StringBuffer buf = new StringBuffer(path.length());
        buf.append(path, 0, i);
    Label_0202:
        while (i < end) {
            final char c2 = path.charAt(i);
            switch (c2) {
                case '?': {
                    buf.append(path, i, end);
                    break Label_0202;
                }
                case '/': {
                    if (state++ == 0) {
                        buf.append(c2);
                        break;
                    }
                    break;
                }
                default: {
                    state = 0;
                    buf.append(c2);
                    break;
                }
            }
            ++i;
        }
        return buf.toString();
    }
    
    public static boolean hasScheme(final String uri) {
        for (int i = 0; i < uri.length(); ++i) {
            final char c = uri.charAt(i);
            if (c == ':') {
                return true;
            }
            if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
                if (i <= 0) {
                    break;
                }
                if ((c < '0' || c > '9') && c != '.' && c != '+' && c != '-') {
                    break;
                }
            }
        }
        return false;
    }
    
    public static String newURI(final String scheme, final String server, final int port, final String path, final String query) {
        final StringBuilder builder = newURIBuilder(scheme, server, port);
        builder.append(path);
        if (query != null && query.length() > 0) {
            builder.append('?').append(query);
        }
        return builder.toString();
    }
    
    public static StringBuilder newURIBuilder(final String scheme, final String server, final int port) {
        final StringBuilder builder = new StringBuilder();
        appendSchemeHostPort(builder, scheme, server, port);
        return builder;
    }
    
    public static void appendSchemeHostPort(final StringBuilder url, final String scheme, final String server, final int port) {
        if (server.indexOf(58) >= 0 && server.charAt(0) != '[') {
            url.append(scheme).append("://").append('[').append(server).append(']');
        }
        else {
            url.append(scheme).append("://").append(server);
        }
        if (port > 0) {
            switch (scheme) {
                case "http": {
                    if (port != 80) {
                        url.append(':').append(port);
                        break;
                    }
                    break;
                }
                case "https": {
                    if (port != 443) {
                        url.append(':').append(port);
                        break;
                    }
                    break;
                }
                default: {
                    url.append(':').append(port);
                    break;
                }
            }
        }
    }
    
    public static void appendSchemeHostPort(final StringBuffer url, final String scheme, final String server, final int port) {
        synchronized (url) {
            if (server.indexOf(58) >= 0 && server.charAt(0) != '[') {
                url.append(scheme).append("://").append('[').append(server).append(']');
            }
            else {
                url.append(scheme).append("://").append(server);
            }
            if (port > 0) {
                switch (scheme) {
                    case "http": {
                        if (port != 80) {
                            url.append(':').append(port);
                            break;
                        }
                        break;
                    }
                    case "https": {
                        if (port != 443) {
                            url.append(':').append(port);
                            break;
                        }
                        break;
                    }
                    default: {
                        url.append(':').append(port);
                        break;
                    }
                }
            }
        }
    }
    
    public static boolean equalsIgnoreEncodings(final String uriA, final String uriB) {
        final int lenA = uriA.length();
        final int lenB = uriB.length();
        int a = 0;
        int b = 0;
        while (a < lenA && b < lenB) {
            int ca;
            final int oa = ca = uriA.charAt(a++);
            if (ca == 37) {
                ca = TypeUtil.convertHexDigit(uriA.charAt(a++)) * 16 + TypeUtil.convertHexDigit(uriA.charAt(a++));
            }
            int cb;
            final int ob = cb = uriB.charAt(b++);
            if (cb == 37) {
                cb = TypeUtil.convertHexDigit(uriB.charAt(b++)) * 16 + TypeUtil.convertHexDigit(uriB.charAt(b++));
            }
            if (ca == 47 && oa != ob) {
                return false;
            }
            if (ca != cb) {
                return decodePath(uriA).equals(decodePath(uriB));
            }
        }
        return a == lenA && b == lenB;
    }
    
    static {
        final String charset = System.getProperty("org.eclipse.jetty.util.URI.charset");
        __CHARSET = ((charset == null) ? StandardCharsets.UTF_8 : Charset.forName(charset));
    }
}
