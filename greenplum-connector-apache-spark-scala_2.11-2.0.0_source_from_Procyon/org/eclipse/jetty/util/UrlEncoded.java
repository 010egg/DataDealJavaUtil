// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util;

import org.eclipse.jetty.util.log.Log;
import java.io.Writer;
import java.io.Reader;
import java.io.StringWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.nio.charset.Charset;
import org.eclipse.jetty.util.log.Logger;

public class UrlEncoded extends MultiMap<String> implements Cloneable
{
    static final Logger LOG;
    public static final Charset ENCODING;
    
    public UrlEncoded(final UrlEncoded url) {
        super(url);
    }
    
    public UrlEncoded() {
    }
    
    public UrlEncoded(final String query) {
        decodeTo(query, this, UrlEncoded.ENCODING, -1);
    }
    
    public void decode(final String query) {
        decodeTo(query, this, UrlEncoded.ENCODING, -1);
    }
    
    public void decode(final String query, final Charset charset) {
        decodeTo(query, this, charset, -1);
    }
    
    public String encode() {
        return this.encode(UrlEncoded.ENCODING, false);
    }
    
    public String encode(final Charset charset) {
        return this.encode(charset, false);
    }
    
    public synchronized String encode(final Charset charset, final boolean equalsForNullValue) {
        return encode(this, charset, equalsForNullValue);
    }
    
    public static String encode(final MultiMap<String> map, Charset charset, final boolean equalsForNullValue) {
        if (charset == null) {
            charset = UrlEncoded.ENCODING;
        }
        final StringBuilder result = new StringBuilder(128);
        boolean delim = false;
        for (final Map.Entry<String, List<String>> entry : map.entrySet()) {
            final String key = entry.getKey().toString();
            final List<String> list = entry.getValue();
            final int s = list.size();
            if (delim) {
                result.append('&');
            }
            if (s == 0) {
                result.append(encodeString(key, charset));
                if (equalsForNullValue) {
                    result.append('=');
                }
            }
            else {
                for (int i = 0; i < s; ++i) {
                    if (i > 0) {
                        result.append('&');
                    }
                    final String val = list.get(i);
                    result.append(encodeString(key, charset));
                    if (val != null) {
                        final String str = val.toString();
                        if (str.length() > 0) {
                            result.append('=');
                            result.append(encodeString(str, charset));
                        }
                        else if (equalsForNullValue) {
                            result.append('=');
                        }
                    }
                    else if (equalsForNullValue) {
                        result.append('=');
                    }
                }
            }
            delim = true;
        }
        return result.toString();
    }
    
    public static void decodeTo(final String content, final MultiMap<String> map, final String charset, final int maxKeys) {
        decodeTo(content, map, (charset == null) ? null : Charset.forName(charset), maxKeys);
    }
    
    public static void decodeTo(final String content, final MultiMap<String> map, Charset charset, final int maxKeys) {
        if (charset == null) {
            charset = UrlEncoded.ENCODING;
        }
        synchronized (map) {
            String key = null;
            String value = null;
            int mark = -1;
            boolean encoded = false;
            for (int i = 0; i < content.length(); ++i) {
                final char c = content.charAt(i);
                switch (c) {
                    case '&': {
                        final int l = i - mark - 1;
                        value = ((l == 0) ? "" : (encoded ? decodeString(content, mark + 1, l, charset) : content.substring(mark + 1, i)));
                        mark = i;
                        encoded = false;
                        if (key != null) {
                            map.add(key, value);
                        }
                        else if (value != null && value.length() > 0) {
                            map.add(value, "");
                        }
                        key = null;
                        value = null;
                        if (maxKeys > 0 && map.size() > maxKeys) {
                            throw new IllegalStateException("Form too many keys");
                        }
                        break;
                    }
                    case '=': {
                        if (key != null) {
                            break;
                        }
                        key = (encoded ? decodeString(content, mark + 1, i - mark - 1, charset) : content.substring(mark + 1, i));
                        mark = i;
                        encoded = false;
                        break;
                    }
                    case '+': {
                        encoded = true;
                        break;
                    }
                    case '%': {
                        encoded = true;
                        break;
                    }
                }
            }
            if (key != null) {
                final int j = content.length() - mark - 1;
                value = ((j == 0) ? "" : (encoded ? decodeString(content, mark + 1, j, charset) : content.substring(mark + 1)));
                map.add(key, value);
            }
            else if (mark < content.length()) {
                key = (encoded ? decodeString(content, mark + 1, content.length() - mark - 1, charset) : content.substring(mark + 1));
                if (key != null && key.length() > 0) {
                    map.add(key, "");
                }
            }
        }
    }
    
    public static void decodeUtf8To(final byte[] raw, final int offset, final int length, final MultiMap<String> map) {
        final Utf8StringBuilder buffer = new Utf8StringBuilder();
        synchronized (map) {
            String key = null;
            String value = null;
            for (int end = offset + length, i = offset; i < end; ++i) {
                final byte b = raw[i];
                try {
                    switch ((char)(0xFF & b)) {
                        case '&': {
                            value = buffer.toReplacedString();
                            buffer.reset();
                            if (key != null) {
                                map.add(key, value);
                            }
                            else if (value != null && value.length() > 0) {
                                map.add(value, "");
                            }
                            key = null;
                            value = null;
                            break;
                        }
                        case '=': {
                            if (key != null) {
                                buffer.append(b);
                                break;
                            }
                            key = buffer.toReplacedString();
                            buffer.reset();
                            break;
                        }
                        case '+': {
                            buffer.append((byte)32);
                            break;
                        }
                        case '%': {
                            if (i + 2 >= end) {
                                buffer.getStringBuilder().append('\ufffd');
                                i = end;
                                break;
                            }
                            if (117 != raw[i + 1]) {
                                final byte hi = raw[++i];
                                final byte lo = raw[++i];
                                buffer.append((byte)((TypeUtil.convertHexDigit(hi) << 4) + TypeUtil.convertHexDigit(lo)));
                                break;
                            }
                            if (++i + 4 < end) {
                                final byte top = raw[++i];
                                final byte hi2 = raw[++i];
                                final byte lo2 = raw[++i];
                                final byte bot = raw[++i];
                                buffer.getStringBuilder().append(Character.toChars((TypeUtil.convertHexDigit(top) << 12) + (TypeUtil.convertHexDigit(hi2) << 8) + (TypeUtil.convertHexDigit(lo2) << 4) + TypeUtil.convertHexDigit(bot)));
                                break;
                            }
                            buffer.getStringBuilder().append('\ufffd');
                            i = end;
                            break;
                        }
                        default: {
                            buffer.append(b);
                            break;
                        }
                    }
                }
                catch (Utf8Appendable.NotUtf8Exception e) {
                    UrlEncoded.LOG.warn(e.toString(), new Object[0]);
                    UrlEncoded.LOG.debug(e);
                }
                catch (NumberFormatException e2) {
                    buffer.append(Utf8Appendable.REPLACEMENT_UTF8, 0, 3);
                    UrlEncoded.LOG.warn(e2.toString(), new Object[0]);
                    UrlEncoded.LOG.debug(e2);
                }
            }
            if (key != null) {
                value = buffer.toReplacedString();
                buffer.reset();
                map.add(key, value);
            }
            else if (buffer.length() > 0) {
                map.add(buffer.toReplacedString(), "");
            }
        }
    }
    
    public static void decode88591To(final InputStream in, final MultiMap<String> map, final int maxLength, final int maxKeys) throws IOException {
        synchronized (map) {
            final StringBuffer buffer = new StringBuffer();
            String key = null;
            String value = null;
            int totalLength = 0;
            int b;
            while ((b = in.read()) >= 0) {
                switch ((char)b) {
                    case '&': {
                        value = ((buffer.length() == 0) ? "" : buffer.toString());
                        buffer.setLength(0);
                        if (key != null) {
                            map.add(key, value);
                        }
                        else if (value != null && value.length() > 0) {
                            map.add(value, "");
                        }
                        key = null;
                        value = null;
                        if (maxKeys > 0 && map.size() > maxKeys) {
                            throw new IllegalStateException("Form too many keys");
                        }
                        break;
                    }
                    case '=': {
                        if (key != null) {
                            buffer.append((char)b);
                            break;
                        }
                        key = buffer.toString();
                        buffer.setLength(0);
                        break;
                    }
                    case '+': {
                        buffer.append(' ');
                        break;
                    }
                    case '%': {
                        final int code0 = in.read();
                        if (117 == code0) {
                            final int code2 = in.read();
                            if (code2 >= 0) {
                                final int code3 = in.read();
                                if (code3 >= 0) {
                                    final int code4 = in.read();
                                    if (code4 >= 0) {
                                        buffer.append(Character.toChars((TypeUtil.convertHexDigit(code0) << 12) + (TypeUtil.convertHexDigit(code2) << 8) + (TypeUtil.convertHexDigit(code3) << 4) + TypeUtil.convertHexDigit(code4)));
                                    }
                                }
                            }
                            break;
                        }
                        if (code0 >= 0) {
                            final int code2 = in.read();
                            if (code2 >= 0) {
                                buffer.append((char)((TypeUtil.convertHexDigit(code0) << 4) + TypeUtil.convertHexDigit(code2)));
                            }
                            break;
                        }
                        break;
                    }
                    default: {
                        buffer.append((char)b);
                        break;
                    }
                }
                if (maxLength >= 0 && ++totalLength > maxLength) {
                    throw new IllegalStateException("Form too large");
                }
            }
            if (key != null) {
                value = ((buffer.length() == 0) ? "" : buffer.toString());
                buffer.setLength(0);
                map.add(key, value);
            }
            else if (buffer.length() > 0) {
                map.add(buffer.toString(), "");
            }
        }
    }
    
    public static void decodeUtf8To(final InputStream in, final MultiMap<String> map, final int maxLength, final int maxKeys) throws IOException {
        synchronized (map) {
            final Utf8StringBuilder buffer = new Utf8StringBuilder();
            String key = null;
            String value = null;
            int totalLength = 0;
            int b;
            while ((b = in.read()) >= 0) {
                try {
                    switch ((char)b) {
                        case '&': {
                            value = buffer.toReplacedString();
                            buffer.reset();
                            if (key != null) {
                                map.add(key, value);
                            }
                            else if (value != null && value.length() > 0) {
                                map.add(value, "");
                            }
                            key = null;
                            value = null;
                            if (maxKeys > 0 && map.size() > maxKeys) {
                                throw new IllegalStateException("Form too many keys");
                            }
                            break;
                        }
                        case '=': {
                            if (key != null) {
                                buffer.append((byte)b);
                                break;
                            }
                            key = buffer.toReplacedString();
                            buffer.reset();
                            break;
                        }
                        case '+': {
                            buffer.append((byte)32);
                            break;
                        }
                        case '%': {
                            int code0 = in.read();
                            boolean decoded = false;
                            if (117 == code0) {
                                code0 = in.read();
                                if (code0 >= 0) {
                                    final int code2 = in.read();
                                    if (code2 >= 0) {
                                        final int code3 = in.read();
                                        if (code3 >= 0) {
                                            final int code4 = in.read();
                                            if (code4 >= 0) {
                                                buffer.getStringBuilder().append(Character.toChars((TypeUtil.convertHexDigit(code0) << 12) + (TypeUtil.convertHexDigit(code2) << 8) + (TypeUtil.convertHexDigit(code3) << 4) + TypeUtil.convertHexDigit(code4)));
                                                decoded = true;
                                            }
                                        }
                                    }
                                }
                            }
                            else if (code0 >= 0) {
                                final int code2 = in.read();
                                if (code2 >= 0) {
                                    buffer.append((byte)((TypeUtil.convertHexDigit(code0) << 4) + TypeUtil.convertHexDigit(code2)));
                                    decoded = true;
                                }
                            }
                            if (!decoded) {
                                buffer.getStringBuilder().append('\ufffd');
                                break;
                            }
                            break;
                        }
                        default: {
                            buffer.append((byte)b);
                            break;
                        }
                    }
                }
                catch (Utf8Appendable.NotUtf8Exception e) {
                    UrlEncoded.LOG.warn(e.toString(), new Object[0]);
                    UrlEncoded.LOG.debug(e);
                }
                catch (NumberFormatException e2) {
                    buffer.append(Utf8Appendable.REPLACEMENT_UTF8, 0, 3);
                    UrlEncoded.LOG.warn(e2.toString(), new Object[0]);
                    UrlEncoded.LOG.debug(e2);
                }
                if (maxLength >= 0 && ++totalLength > maxLength) {
                    throw new IllegalStateException("Form too large");
                }
            }
            if (key != null) {
                value = buffer.toReplacedString();
                buffer.reset();
                map.add(key, value);
            }
            else if (buffer.length() > 0) {
                map.add(buffer.toReplacedString(), "");
            }
        }
    }
    
    public static void decodeUtf16To(final InputStream in, final MultiMap<String> map, final int maxLength, final int maxKeys) throws IOException {
        final InputStreamReader input = new InputStreamReader(in, StandardCharsets.UTF_16);
        final StringWriter buf = new StringWriter(8192);
        IO.copy(input, buf, maxLength);
        decodeTo(buf.getBuffer().toString(), map, StandardCharsets.UTF_16, maxKeys);
    }
    
    public static void decodeTo(final InputStream in, final MultiMap<String> map, final String charset, final int maxLength, final int maxKeys) throws IOException {
        if (charset == null) {
            if (UrlEncoded.ENCODING.equals(StandardCharsets.UTF_8)) {
                decodeUtf8To(in, map, maxLength, maxKeys);
            }
            else {
                decodeTo(in, map, UrlEncoded.ENCODING, maxLength, maxKeys);
            }
        }
        else if ("UTF-8".equalsIgnoreCase(charset)) {
            decodeUtf8To(in, map, maxLength, maxKeys);
        }
        else if ("ISO-8859-1".equalsIgnoreCase(charset)) {
            decode88591To(in, map, maxLength, maxKeys);
        }
        else if ("UTF-16".equalsIgnoreCase(charset)) {
            decodeUtf16To(in, map, maxLength, maxKeys);
        }
        else {
            decodeTo(in, map, Charset.forName(charset), maxLength, maxKeys);
        }
    }
    
    public static void decodeTo(final InputStream in, final MultiMap<String> map, Charset charset, final int maxLength, final int maxKeys) throws IOException {
        if (charset == null) {
            charset = UrlEncoded.ENCODING;
        }
        if (StandardCharsets.UTF_8.equals(charset)) {
            decodeUtf8To(in, map, maxLength, maxKeys);
            return;
        }
        if (StandardCharsets.ISO_8859_1.equals(charset)) {
            decode88591To(in, map, maxLength, maxKeys);
            return;
        }
        if (StandardCharsets.UTF_16.equals(charset)) {
            decodeUtf16To(in, map, maxLength, maxKeys);
            return;
        }
        synchronized (map) {
            String key = null;
            String value = null;
            int totalLength = 0;
            try (final ByteArrayOutputStream2 output = new ByteArrayOutputStream2()) {
                int size = 0;
                int c;
                while ((c = in.read()) > 0) {
                    switch ((char)c) {
                        case '&': {
                            size = output.size();
                            value = ((size == 0) ? "" : output.toString(charset));
                            output.setCount(0);
                            if (key != null) {
                                map.add(key, value);
                            }
                            else if (value != null && value.length() > 0) {
                                map.add(value, "");
                            }
                            key = null;
                            value = null;
                            if (maxKeys > 0 && map.size() > maxKeys) {
                                throw new IllegalStateException("Form too many keys");
                            }
                            break;
                        }
                        case '=': {
                            if (key != null) {
                                output.write(c);
                                break;
                            }
                            size = output.size();
                            key = ((size == 0) ? "" : output.toString(charset));
                            output.setCount(0);
                            break;
                        }
                        case '+': {
                            output.write(32);
                            break;
                        }
                        case '%': {
                            final int code0 = in.read();
                            if (117 == code0) {
                                final int code2 = in.read();
                                if (code2 >= 0) {
                                    final int code3 = in.read();
                                    if (code3 >= 0) {
                                        final int code4 = in.read();
                                        if (code4 >= 0) {
                                            output.write(new String(Character.toChars((TypeUtil.convertHexDigit(code0) << 12) + (TypeUtil.convertHexDigit(code2) << 8) + (TypeUtil.convertHexDigit(code3) << 4) + TypeUtil.convertHexDigit(code4))).getBytes(charset));
                                        }
                                    }
                                }
                                break;
                            }
                            if (code0 >= 0) {
                                final int code2 = in.read();
                                if (code2 >= 0) {
                                    output.write((TypeUtil.convertHexDigit(code0) << 4) + TypeUtil.convertHexDigit(code2));
                                }
                                break;
                            }
                            break;
                        }
                        default: {
                            output.write(c);
                            break;
                        }
                    }
                    ++totalLength;
                    if (maxLength >= 0 && totalLength > maxLength) {
                        throw new IllegalStateException("Form too large");
                    }
                }
                size = output.size();
                if (key != null) {
                    value = ((size == 0) ? "" : output.toString(charset));
                    output.setCount(0);
                    map.add(key, value);
                }
                else if (size > 0) {
                    map.add(output.toString(charset), "");
                }
            }
        }
    }
    
    public static String decodeString(final String encoded, final int offset, final int length, final Charset charset) {
        if (charset == null || StandardCharsets.UTF_8.equals(charset)) {
            Utf8StringBuffer buffer = null;
            for (int i = 0; i < length; ++i) {
                final char c = encoded.charAt(offset + i);
                if (c < '\0' || c > '\u00ff') {
                    if (buffer == null) {
                        buffer = new Utf8StringBuffer(length);
                        buffer.getStringBuffer().append(encoded, offset, offset + i + 1);
                    }
                    else {
                        buffer.getStringBuffer().append(c);
                    }
                }
                else if (c == '+') {
                    if (buffer == null) {
                        buffer = new Utf8StringBuffer(length);
                        buffer.getStringBuffer().append(encoded, offset, offset + i);
                    }
                    buffer.getStringBuffer().append(' ');
                }
                else if (c == '%') {
                    if (buffer == null) {
                        buffer = new Utf8StringBuffer(length);
                        buffer.getStringBuffer().append(encoded, offset, offset + i);
                    }
                    if (i + 2 < length) {
                        try {
                            if ('u' == encoded.charAt(offset + i + 1)) {
                                if (i + 5 < length) {
                                    final int o = offset + i + 2;
                                    i += 5;
                                    final String unicode = new String(Character.toChars(TypeUtil.parseInt(encoded, o, 4, 16)));
                                    buffer.getStringBuffer().append(unicode);
                                }
                                else {
                                    i = length;
                                    buffer.getStringBuffer().append('\ufffd');
                                }
                            }
                            else {
                                final int o = offset + i + 1;
                                i += 2;
                                final byte b = (byte)TypeUtil.parseInt(encoded, o, 2, 16);
                                buffer.append(b);
                            }
                        }
                        catch (Utf8Appendable.NotUtf8Exception e) {
                            UrlEncoded.LOG.warn(e.toString(), new Object[0]);
                            UrlEncoded.LOG.debug(e);
                        }
                        catch (NumberFormatException e2) {
                            UrlEncoded.LOG.warn(e2.toString(), new Object[0]);
                            UrlEncoded.LOG.debug(e2);
                            buffer.getStringBuffer().append('\ufffd');
                        }
                    }
                    else {
                        buffer.getStringBuffer().append('\ufffd');
                        i = length;
                    }
                }
                else if (buffer != null) {
                    buffer.getStringBuffer().append(c);
                }
            }
            if (buffer != null) {
                return buffer.toReplacedString();
            }
            if (offset == 0 && encoded.length() == length) {
                return encoded;
            }
            return encoded.substring(offset, offset + length);
        }
        else {
            StringBuffer buffer2 = null;
            for (int i = 0; i < length; ++i) {
                char c = encoded.charAt(offset + i);
                if (c < '\0' || c > '\u00ff') {
                    if (buffer2 == null) {
                        buffer2 = new StringBuffer(length);
                        buffer2.append(encoded, offset, offset + i + 1);
                    }
                    else {
                        buffer2.append(c);
                    }
                }
                else if (c == '+') {
                    if (buffer2 == null) {
                        buffer2 = new StringBuffer(length);
                        buffer2.append(encoded, offset, offset + i);
                    }
                    buffer2.append(' ');
                }
                else if (c == '%') {
                    if (buffer2 == null) {
                        buffer2 = new StringBuffer(length);
                        buffer2.append(encoded, offset, offset + i);
                    }
                    final byte[] ba = new byte[length];
                    int n = 0;
                    while (c >= '\0' && c <= '\u00ff') {
                        if (c == '%') {
                            if (i + 2 < length) {
                                try {
                                    if ('u' == encoded.charAt(offset + i + 1)) {
                                        if (i + 6 < length) {
                                            final int o2 = offset + i + 2;
                                            i += 6;
                                            final String unicode2 = new String(Character.toChars(TypeUtil.parseInt(encoded, o2, 4, 16)));
                                            final byte[] reencoded = unicode2.getBytes(charset);
                                            System.arraycopy(reencoded, 0, ba, n, reencoded.length);
                                            n += reencoded.length;
                                        }
                                        else {
                                            ba[n++] = 63;
                                            i = length;
                                        }
                                    }
                                    else {
                                        final int o2 = offset + i + 1;
                                        i += 3;
                                        ba[n] = (byte)TypeUtil.parseInt(encoded, o2, 2, 16);
                                        ++n;
                                    }
                                }
                                catch (Exception e3) {
                                    UrlEncoded.LOG.warn(e3.toString(), new Object[0]);
                                    UrlEncoded.LOG.debug(e3);
                                    ba[n++] = 63;
                                }
                            }
                            else {
                                ba[n++] = 63;
                                i = length;
                            }
                        }
                        else if (c == '+') {
                            ba[n++] = 32;
                            ++i;
                        }
                        else {
                            ba[n++] = (byte)c;
                            ++i;
                        }
                        if (i >= length) {
                            break;
                        }
                        c = encoded.charAt(offset + i);
                    }
                    --i;
                    buffer2.append(new String(ba, 0, n, charset));
                }
                else if (buffer2 != null) {
                    buffer2.append(c);
                }
            }
            if (buffer2 != null) {
                return buffer2.toString();
            }
            if (offset == 0 && encoded.length() == length) {
                return encoded;
            }
            return encoded.substring(offset, offset + length);
        }
    }
    
    public static String encodeString(final String string) {
        return encodeString(string, UrlEncoded.ENCODING);
    }
    
    public static String encodeString(final String string, Charset charset) {
        if (charset == null) {
            charset = UrlEncoded.ENCODING;
        }
        byte[] bytes = null;
        bytes = string.getBytes(charset);
        final int len = bytes.length;
        final byte[] encoded = new byte[bytes.length * 3];
        int n = 0;
        boolean noEncode = true;
        for (final byte b : bytes) {
            if (b == 32) {
                noEncode = false;
                encoded[n++] = 43;
            }
            else if ((b >= 97 && b <= 122) || (b >= 65 && b <= 90) || (b >= 48 && b <= 57)) {
                encoded[n++] = b;
            }
            else {
                noEncode = false;
                encoded[n++] = 37;
                byte nibble = (byte)((b & 0xF0) >> 4);
                if (nibble >= 10) {
                    encoded[n++] = (byte)(65 + nibble - 10);
                }
                else {
                    encoded[n++] = (byte)(48 + nibble);
                }
                nibble = (byte)(b & 0xF);
                if (nibble >= 10) {
                    encoded[n++] = (byte)(65 + nibble - 10);
                }
                else {
                    encoded[n++] = (byte)(48 + nibble);
                }
            }
        }
        if (noEncode) {
            return string;
        }
        return new String(encoded, 0, n, charset);
    }
    
    @Override
    public Object clone() {
        return new UrlEncoded(this);
    }
    
    static {
        LOG = Log.getLogger(UrlEncoded.class);
        Charset encoding;
        try {
            final String charset = System.getProperty("org.eclipse.jetty.util.UrlEncoding.charset");
            encoding = ((charset == null) ? StandardCharsets.UTF_8 : Charset.forName(charset));
        }
        catch (Exception e) {
            UrlEncoded.LOG.warn(e);
            encoding = StandardCharsets.UTF_8;
        }
        ENCODING = encoding;
    }
}
