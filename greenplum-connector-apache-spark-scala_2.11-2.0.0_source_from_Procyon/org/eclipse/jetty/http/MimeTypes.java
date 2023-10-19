// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.http;

import java.nio.charset.StandardCharsets;
import org.eclipse.jetty.util.BufferUtil;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.jetty.util.ArrayTrie;
import org.eclipse.jetty.util.log.Log;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import org.eclipse.jetty.util.StringUtil;
import java.util.HashMap;
import java.util.Map;
import java.nio.ByteBuffer;
import org.eclipse.jetty.util.Trie;
import org.eclipse.jetty.util.log.Logger;

public class MimeTypes
{
    private static final Logger LOG;
    public static final Trie<Type> CACHE;
    private static final Trie<ByteBuffer> TYPES;
    private static final Map<String, String> __dftMimeMap;
    private static final Map<String, String> __encodings;
    private final Map<String, String> _mimeMap;
    
    public MimeTypes() {
        this._mimeMap = new HashMap<String, String>();
    }
    
    public synchronized Map<String, String> getMimeMap() {
        return this._mimeMap;
    }
    
    public void setMimeMap(final Map<String, String> mimeMap) {
        this._mimeMap.clear();
        if (mimeMap != null) {
            for (final Map.Entry<String, String> ext : mimeMap.entrySet()) {
                this._mimeMap.put(StringUtil.asciiToLowerCase(ext.getKey()), normalizeMimeType(ext.getValue()));
            }
        }
    }
    
    public String getMimeByExtension(final String filename) {
        String type = null;
        if (filename != null) {
            int i = -1;
            while (type == null) {
                i = filename.indexOf(".", i + 1);
                if (i < 0) {
                    break;
                }
                if (i >= filename.length()) {
                    break;
                }
                final String ext = StringUtil.asciiToLowerCase(filename.substring(i + 1));
                if (this._mimeMap != null) {
                    type = this._mimeMap.get(ext);
                }
                if (type != null) {
                    continue;
                }
                type = MimeTypes.__dftMimeMap.get(ext);
            }
        }
        if (type == null) {
            if (this._mimeMap != null) {
                type = this._mimeMap.get("*");
            }
            if (type == null) {
                type = MimeTypes.__dftMimeMap.get("*");
            }
        }
        return type;
    }
    
    public void addMimeMapping(final String extension, final String type) {
        this._mimeMap.put(StringUtil.asciiToLowerCase(extension), normalizeMimeType(type));
    }
    
    public static Set<String> getKnownMimeTypes() {
        return new HashSet<String>(MimeTypes.__dftMimeMap.values());
    }
    
    private static String normalizeMimeType(final String type) {
        final Type t = MimeTypes.CACHE.get(type);
        if (t != null) {
            return t.asString();
        }
        return StringUtil.asciiToLowerCase(type);
    }
    
    public static String getCharsetFromContentType(final String value) {
        if (value == null) {
            return null;
        }
        final int end = value.length();
        int state = 0;
        int start = 0;
        boolean quote = false;
        int i;
        for (i = 0; i < end; ++i) {
            final char b = value.charAt(i);
            if (quote && state != 10) {
                if ('\"' == b) {
                    quote = false;
                }
            }
            else {
                switch (state) {
                    case 0: {
                        if ('\"' == b) {
                            quote = true;
                            break;
                        }
                        if (';' == b) {
                            state = 1;
                            break;
                        }
                        break;
                    }
                    case 1: {
                        if ('c' == b) {
                            state = 2;
                            break;
                        }
                        if (' ' != b) {
                            state = 0;
                            break;
                        }
                        break;
                    }
                    case 2: {
                        if ('h' == b) {
                            state = 3;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 3: {
                        if ('a' == b) {
                            state = 4;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 4: {
                        if ('r' == b) {
                            state = 5;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 5: {
                        if ('s' == b) {
                            state = 6;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 6: {
                        if ('e' == b) {
                            state = 7;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 7: {
                        if ('t' == b) {
                            state = 8;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 8: {
                        if ('=' == b) {
                            state = 9;
                            break;
                        }
                        if (' ' != b) {
                            state = 0;
                            break;
                        }
                        break;
                    }
                    case 9: {
                        if (' ' == b) {
                            break;
                        }
                        if ('\"' == b) {
                            quote = true;
                            start = i + 1;
                            state = 10;
                            break;
                        }
                        start = i;
                        state = 10;
                        break;
                    }
                    case 10: {
                        if ((!quote && (';' == b || ' ' == b)) || (quote && '\"' == b)) {
                            return StringUtil.normalizeCharset(value, start, i - start);
                        }
                        break;
                    }
                }
            }
        }
        if (state == 10) {
            return StringUtil.normalizeCharset(value, start, i - start);
        }
        return null;
    }
    
    public static String inferCharsetFromContentType(final String value) {
        return MimeTypes.__encodings.get(value);
    }
    
    public static String getContentTypeWithoutCharset(final String value) {
        final int end = value.length();
        int state = 0;
        int start = 0;
        boolean quote = false;
        int i = 0;
        StringBuilder builder = null;
        while (i < end) {
            final char b = value.charAt(i);
            if ('\"' == b) {
                quote = !quote;
                switch (state) {
                    case 11: {
                        builder.append(b);
                        break;
                    }
                    case 10: {
                        break;
                    }
                    case 9: {
                        builder = new StringBuilder();
                        builder.append(value, 0, start + 1);
                        state = 10;
                        break;
                    }
                    default: {
                        start = i;
                        state = 0;
                        break;
                    }
                }
            }
            else if (quote) {
                if (builder != null && state != 10) {
                    builder.append(b);
                }
            }
            else {
                switch (state) {
                    case 0: {
                        if (';' == b) {
                            state = 1;
                            break;
                        }
                        if (' ' != b) {
                            start = i;
                            break;
                        }
                        break;
                    }
                    case 1: {
                        if ('c' == b) {
                            state = 2;
                            break;
                        }
                        if (' ' != b) {
                            state = 0;
                            break;
                        }
                        break;
                    }
                    case 2: {
                        if ('h' == b) {
                            state = 3;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 3: {
                        if ('a' == b) {
                            state = 4;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 4: {
                        if ('r' == b) {
                            state = 5;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 5: {
                        if ('s' == b) {
                            state = 6;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 6: {
                        if ('e' == b) {
                            state = 7;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 7: {
                        if ('t' == b) {
                            state = 8;
                            break;
                        }
                        state = 0;
                        break;
                    }
                    case 8: {
                        if ('=' == b) {
                            state = 9;
                            break;
                        }
                        if (' ' != b) {
                            state = 0;
                            break;
                        }
                        break;
                    }
                    case 9: {
                        if (' ' == b) {
                            break;
                        }
                        builder = new StringBuilder();
                        builder.append(value, 0, start + 1);
                        state = 10;
                        break;
                    }
                    case 10: {
                        if (';' == b) {
                            builder.append(b);
                            state = 11;
                            break;
                        }
                        break;
                    }
                    case 11: {
                        if (' ' != b) {
                            builder.append(b);
                            break;
                        }
                        break;
                    }
                }
            }
            ++i;
        }
        if (builder == null) {
            return value;
        }
        return builder.toString();
    }
    
    static {
        LOG = Log.getLogger(MimeTypes.class);
        CACHE = new ArrayTrie<Type>(512);
        TYPES = new ArrayTrie<ByteBuffer>(512);
        __dftMimeMap = new HashMap<String, String>();
        __encodings = new HashMap<String, String>();
        for (final Type type : Type.values()) {
            MimeTypes.CACHE.put(type.toString(), type);
            MimeTypes.TYPES.put(type.toString(), type.asBuffer());
            final int charset = type.toString().indexOf(";charset=");
            if (charset > 0) {
                MimeTypes.CACHE.put(type.toString().replace(";charset=", "; charset="), type);
                MimeTypes.TYPES.put(type.toString().replace(";charset=", "; charset="), type.asBuffer());
            }
        }
        try {
            final ResourceBundle mime = ResourceBundle.getBundle("org/eclipse/jetty/http/mime");
            final Enumeration<String> i = mime.getKeys();
            while (i.hasMoreElements()) {
                final String ext = i.nextElement();
                final String m = mime.getString(ext);
                MimeTypes.__dftMimeMap.put(StringUtil.asciiToLowerCase(ext), normalizeMimeType(m));
            }
        }
        catch (MissingResourceException e) {
            MimeTypes.LOG.warn(e.toString(), new Object[0]);
            MimeTypes.LOG.debug(e);
        }
        try {
            final ResourceBundle encoding = ResourceBundle.getBundle("org/eclipse/jetty/http/encoding");
            final Enumeration<String> i = encoding.getKeys();
            while (i.hasMoreElements()) {
                final String type2 = i.nextElement();
                MimeTypes.__encodings.put(type2, encoding.getString(type2));
            }
        }
        catch (MissingResourceException e) {
            MimeTypes.LOG.warn(e.toString(), new Object[0]);
            MimeTypes.LOG.debug(e);
        }
    }
    
    public enum Type
    {
        FORM_ENCODED("application/x-www-form-urlencoded"), 
        MESSAGE_HTTP("message/http"), 
        MULTIPART_BYTERANGES("multipart/byteranges"), 
        TEXT_HTML("text/html"), 
        TEXT_PLAIN("text/plain"), 
        TEXT_XML("text/xml"), 
        TEXT_JSON("text/json", StandardCharsets.UTF_8), 
        APPLICATION_JSON("application/json", StandardCharsets.UTF_8), 
        TEXT_HTML_8859_1("text/html; charset=ISO-8859-1", Type.TEXT_HTML), 
        TEXT_HTML_UTF_8("text/html; charset=UTF-8", Type.TEXT_HTML), 
        TEXT_PLAIN_8859_1("text/plain; charset=ISO-8859-1", Type.TEXT_PLAIN), 
        TEXT_PLAIN_UTF_8("text/plain; charset=UTF-8", Type.TEXT_PLAIN), 
        TEXT_XML_8859_1("text/xml; charset=ISO-8859-1", Type.TEXT_XML), 
        TEXT_XML_UTF_8("text/xml; charset=UTF-8", Type.TEXT_XML), 
        TEXT_JSON_8859_1("text/json; charset=ISO-8859-1", Type.TEXT_JSON), 
        TEXT_JSON_UTF_8("text/json; charset=UTF-8", Type.TEXT_JSON), 
        APPLICATION_JSON_8859_1("text/json; charset=ISO-8859-1", Type.APPLICATION_JSON), 
        APPLICATION_JSON_UTF_8("text/json; charset=UTF-8", Type.APPLICATION_JSON);
        
        private final String _string;
        private final Type _base;
        private final ByteBuffer _buffer;
        private final Charset _charset;
        private final boolean _assumedCharset;
        private final HttpField _field;
        
        private Type(final String s) {
            this._string = s;
            this._buffer = BufferUtil.toBuffer(s);
            this._base = this;
            this._charset = null;
            this._assumedCharset = false;
            this._field = new HttpGenerator.CachedHttpField(HttpHeader.CONTENT_TYPE, this._string);
        }
        
        private Type(final String s, final Type base) {
            this._string = s;
            this._buffer = BufferUtil.toBuffer(s);
            this._base = base;
            final int i = s.indexOf("; charset=");
            this._charset = Charset.forName(s.substring(i + 10));
            this._assumedCharset = false;
            this._field = new HttpGenerator.CachedHttpField(HttpHeader.CONTENT_TYPE, this._string);
        }
        
        private Type(final String s, final Charset cs) {
            this._string = s;
            this._base = this;
            this._buffer = BufferUtil.toBuffer(s);
            this._charset = cs;
            this._assumedCharset = true;
            this._field = new HttpGenerator.CachedHttpField(HttpHeader.CONTENT_TYPE, this._string);
        }
        
        public ByteBuffer asBuffer() {
            return this._buffer.asReadOnlyBuffer();
        }
        
        public Charset getCharset() {
            return this._charset;
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
        
        public boolean isCharsetAssumed() {
            return this._assumedCharset;
        }
        
        public HttpField getContentTypeField() {
            return this._field;
        }
        
        public Type getBaseType() {
            return this._base;
        }
    }
}
