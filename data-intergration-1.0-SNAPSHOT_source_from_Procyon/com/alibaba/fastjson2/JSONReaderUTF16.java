// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.OffsetDateTime;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import com.alibaba.fastjson2.util.DateUtils;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import java.math.BigInteger;
import com.alibaba.fastjson2.util.TypeUtils;
import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;
import java.io.Reader;
import java.io.Closeable;

class JSONReaderUTF16 extends JSONReader
{
    static final long CHAR_MASK;
    protected final String str;
    protected final char[] chars;
    protected final int length;
    protected final int start;
    protected final int end;
    private int nameBegin;
    private int nameEnd;
    private int nameLength;
    private int referenceBegin;
    private Closeable input;
    private int cacheIndex;
    
    JSONReaderUTF16(final Context ctx, final byte[] bytes, final int offset, final int length) {
        super(ctx, false, false);
        this.cacheIndex = -1;
        this.str = null;
        this.chars = new char[length / 2];
        int j = 0;
        for (int bytesEnd = offset + length, i = offset; i < bytesEnd; i += 2, ++j) {
            final byte c0 = bytes[i];
            final byte c2 = bytes[i + 1];
            this.chars[j] = (char)((c2 & 0xFF) | (c0 & 0xFF) << 8);
        }
        this.start = offset;
        final int n = j;
        this.length = n;
        this.end = n;
        if (this.offset >= this.end) {
            this.ch = '\u001a';
            return;
        }
        this.ch = this.chars[this.offset];
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            ++this.offset;
            if (this.offset >= length) {
                this.ch = '\u001a';
                return;
            }
            this.ch = this.chars[this.offset];
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            ++this.offset;
            if (this.offset >= length) {
                this.ch = '\u001a';
                return;
            }
            this.ch = this.chars[this.offset];
        }
        ++this.offset;
        if (this.ch == '\ufffe' || this.ch == '\ufeff') {
            this.next();
        }
        while (this.ch == '/') {
            this.next();
            if (this.ch != '/') {
                throw new JSONException("input not support " + this.ch + ", offset " + offset);
            }
            this.skipLineComment();
        }
    }
    
    @Override
    public final byte[] readHex() {
        if (this.ch == 'x') {
            this.next();
        }
        char ch = this.ch;
        int offset = this.offset;
        final char[] chars = this.chars;
        final char quote = ch;
        if (quote != '\'' && quote != '\"') {
            throw new JSONException("illegal state. " + ch);
        }
        final int start = offset;
        ++offset;
        while (true) {
            ch = chars[offset++];
            if (ch < '0' || ch > '9') {
                if (ch >= 'A' && ch <= 'F') {
                    continue;
                }
                break;
            }
        }
        if (ch != quote) {
            throw new JSONException("illegal state. " + ch);
        }
        ch = chars[offset++];
        final int len = offset - start - 2;
        if (len == 0) {
            return new byte[0];
        }
        if (len % 2 != 0) {
            throw new JSONException("illegal state. " + len);
        }
        final byte[] bytes = new byte[len / 2];
        for (int i = 0; i < bytes.length; ++i) {
            final char c0 = chars[start + i * 2];
            final char c2 = chars[start + i * 2 + 1];
            final int b0 = c0 - ((c0 <= '9') ? '0' : '7');
            final int b2 = c2 - ((c2 <= '9') ? '0' : '7');
            bytes[i] = (byte)(b0 << 4 | b2);
        }
        while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
            if (offset >= this.end) {
                ch = '\u001a';
            }
            else {
                ch = chars[offset++];
            }
        }
        if (ch != ',' || offset >= this.end) {
            this.offset = offset;
            this.ch = ch;
            return bytes;
        }
        this.comma = true;
        for (ch = chars[offset]; ch == '\0' || (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L); ch = chars[offset]) {
            if (++offset >= this.end) {
                this.offset = offset;
                this.ch = '\u001a';
                return bytes;
            }
        }
        this.offset = offset + 1;
        this.ch = ch;
        while (this.ch == '/' && this.offset < chars.length && chars[this.offset] == '/') {
            this.skipLineComment();
        }
        return bytes;
    }
    
    @Override
    public final boolean isReference() {
        final char[] chars = this.chars;
        char ch = this.ch;
        int offset = this.offset;
        if (ch != '{') {
            return false;
        }
        if (offset == this.end) {
            return false;
        }
        for (ch = chars[offset]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = chars[offset]) {
            if (++offset >= this.end) {
                return false;
            }
        }
        final char quote = ch;
        if (offset + 6 >= this.end || chars[offset + 1] != '$' || chars[offset + 2] != 'r' || chars[offset + 3] != 'e' || chars[offset + 4] != 'f' || chars[offset + 5] != quote) {
            return false;
        }
        for (offset += 6, ch = chars[offset]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = chars[offset]) {
            if (++offset >= this.end) {
                return false;
            }
        }
        if (ch != ':' || offset + 1 >= this.end) {
            return false;
        }
        for (ch = chars[++offset]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = chars[offset]) {
            if (++offset >= this.end) {
                return false;
            }
        }
        if (ch != quote) {
            return false;
        }
        this.referenceBegin = offset;
        return true;
    }
    
    @Override
    public final String readReference() {
        if (this.referenceBegin == this.end) {
            return null;
        }
        this.offset = this.referenceBegin;
        this.ch = this.chars[this.offset++];
        final String reference = this.readString();
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            ++this.offset;
            if (this.offset >= this.length) {
                this.ch = '\u001a';
                return reference;
            }
            this.ch = this.chars[this.offset];
        }
        if (this.ch != '}') {
            throw new JSONException("illegal reference : " + reference);
        }
        if (this.offset == this.end) {
            this.ch = '\u001a';
        }
        else {
            this.ch = this.chars[this.offset++];
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
            }
        }
        if (this.comma = (this.ch == ',')) {
            this.ch = this.chars[this.offset++];
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = this.chars[this.offset++];
                    }
                }
            }
        }
        return reference;
    }
    
    JSONReaderUTF16(final Context ctx, final Reader input) {
        super(ctx, false, false);
        this.cacheIndex = -1;
        this.input = input;
        this.cacheIndex = (System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1);
        final JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[this.cacheIndex];
        char[] chars = JSONFactory.CHARS_UPDATER.getAndSet(cacheItem, null);
        if (chars == null) {
            chars = new char[8192];
        }
        int off = 0;
        try {
            while (true) {
                final int n = input.read(chars, off, chars.length - off);
                if (n == -1) {
                    break;
                }
                off += n;
                if (off != chars.length) {
                    continue;
                }
                final int oldCapacity = chars.length;
                final int newCapacity = oldCapacity + (oldCapacity >> 1);
                chars = Arrays.copyOf(chars, newCapacity);
            }
        }
        catch (IOException ioe) {
            throw new JSONException("read error", ioe);
        }
        this.str = null;
        this.chars = chars;
        this.offset = 0;
        this.length = off;
        this.start = 0;
        this.end = this.length;
        if (this.offset >= this.end) {
            this.ch = '\u001a';
            return;
        }
        this.ch = chars[this.offset];
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            ++this.offset;
            if (this.offset >= this.length) {
                this.ch = '\u001a';
                return;
            }
            this.ch = chars[this.offset];
        }
        ++this.offset;
        if (this.ch == '\ufffe' || this.ch == '\ufeff') {
            this.next();
        }
        while (this.ch == '/') {
            this.next();
            if (this.ch != '/') {
                throw new JSONException("input not support " + this.ch + ", offset " + this.offset);
            }
            this.skipLineComment();
        }
    }
    
    JSONReaderUTF16(final Context ctx, final String str, final int offset, final int length) {
        super(ctx, false, false);
        this.cacheIndex = -1;
        this.cacheIndex = (System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1);
        final JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[this.cacheIndex];
        char[] chars = JSONFactory.CHARS_UPDATER.getAndSet(cacheItem, null);
        if (chars == null || chars.length < length) {
            chars = new char[Math.max(length, 8192)];
        }
        str.getChars(offset, length, chars, 0);
        this.str = str;
        this.chars = chars;
        this.offset = 0;
        this.length = length;
        this.start = 0;
        this.end = this.length;
        if (this.offset >= this.end) {
            this.ch = '\u001a';
            return;
        }
        this.ch = chars[this.offset];
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            ++this.offset;
            if (this.offset >= this.length) {
                this.ch = '\u001a';
                return;
            }
            this.ch = chars[this.offset];
        }
        ++this.offset;
        if (this.ch == '\ufffe' || this.ch == '\ufeff') {
            this.next();
        }
        while (this.ch == '/' && this.offset < this.chars.length && this.chars[this.offset] == '/') {
            this.skipLineComment();
        }
    }
    
    JSONReaderUTF16(final Context ctx, final String str, final char[] chars, final int offset, final int length) {
        super(ctx, false, false);
        this.cacheIndex = -1;
        this.str = str;
        this.chars = chars;
        this.offset = offset;
        this.length = length;
        this.start = offset;
        this.end = offset + length;
        if (this.offset >= this.end) {
            this.ch = '\u001a';
            return;
        }
        this.ch = chars[this.offset];
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            ++this.offset;
            if (this.offset >= length) {
                this.ch = '\u001a';
                return;
            }
            this.ch = chars[this.offset];
        }
        ++this.offset;
        if (this.ch == '\ufffe' || this.ch == '\ufeff') {
            this.next();
        }
        while (this.ch == '/' && this.offset < this.chars.length && this.chars[this.offset] == '/') {
            this.skipLineComment();
        }
    }
    
    JSONReaderUTF16(final Context ctx, final InputStream input) {
        super(ctx, false, false);
        this.cacheIndex = -1;
        this.input = input;
        final int cacheIndex = System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1;
        final JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[cacheIndex];
        byte[] bytes = JSONFactory.BYTES_UPDATER.getAndSet(cacheItem, null);
        final int bufferSize = ctx.bufferSize;
        if (bytes == null) {
            bytes = new byte[bufferSize];
        }
        char[] chars;
        try {
            int off = 0;
            while (true) {
                final int n = input.read(bytes, off, bytes.length - off);
                if (n == -1) {
                    break;
                }
                off += n;
                if (off != bytes.length) {
                    continue;
                }
                bytes = Arrays.copyOf(bytes, bytes.length + bufferSize);
            }
            if (off % 2 == 1) {
                throw new JSONException("illegal input utf16 bytes, length " + off);
            }
            chars = new char[off / 2];
            for (int i = 0, j = 0; i < off; i += 2, ++j) {
                final byte c0 = bytes[i];
                final byte c2 = bytes[i + 1];
                chars[j] = (char)((c2 & 0xFF) | (c0 & 0xFF) << 8);
            }
        }
        catch (IOException ioe) {
            throw new JSONException("read error", ioe);
        }
        finally {
            JSONFactory.BYTES_UPDATER.lazySet(cacheItem, bytes);
        }
        final int length = chars.length;
        this.str = null;
        this.chars = chars;
        this.offset = 0;
        this.length = length;
        this.start = 0;
        this.end = length;
        if (this.end == 0) {
            this.ch = '\u001a';
            return;
        }
        int offset;
        char ch;
        for (offset = 0, ch = chars[offset]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = chars[offset]) {
            if (++offset >= length) {
                this.ch = '\u001a';
                return;
            }
        }
        this.ch = ch;
        ++this.offset;
        if (ch == '\ufffe' || ch == '\ufeff') {
            this.next();
        }
        while (this.ch == '/') {
            this.next();
            if (this.ch != '/') {
                throw new JSONException("input not support " + ch + ", offset " + offset);
            }
            this.skipLineComment();
        }
    }
    
    @Override
    public final boolean nextIfMatch(final char m) {
        final char[] chars = this.chars;
        int offset = this.offset;
        char ch = this.ch;
        while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
            if (offset >= this.end) {
                ch = '\u001a';
            }
            else {
                ch = chars[offset++];
            }
        }
        if (ch != m) {
            return false;
        }
        this.comma = (m == ',');
        if (offset >= this.end) {
            this.offset = offset;
            this.ch = '\u001a';
            return true;
        }
        for (ch = chars[offset]; ch == '\0' || (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L); ch = chars[offset]) {
            if (++offset >= this.end) {
                this.offset = offset;
                this.ch = '\u001a';
                return true;
            }
        }
        this.offset = offset + 1;
        this.ch = ch;
        while (this.ch == '/' && this.offset < chars.length && chars[this.offset] == '/') {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final boolean nextIfComma() {
        final char[] chars = this.chars;
        int offset = this.offset;
        char ch = this.ch;
        if (ch != ',') {
            this.ch = ch;
            this.offset = offset;
            return false;
        }
        this.comma = true;
        if (offset >= this.end) {
            this.offset = offset;
            this.ch = '\u001a';
            return true;
        }
        for (ch = chars[offset]; ch == '\0' || (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L); ch = chars[offset]) {
            if (++offset >= this.end) {
                this.offset = offset;
                this.ch = '\u001a';
                return true;
            }
        }
        this.offset = offset + 1;
        this.ch = ch;
        while (this.ch == '/' && this.offset < chars.length && chars[this.offset] == '/') {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final boolean nextIfArrayStart() {
        char ch = this.ch;
        if (ch != '[') {
            return false;
        }
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return true;
        }
        char[] chars;
        for (chars = this.chars, ch = chars[offset++]; ch == '\0' || (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L); ch = chars[offset++]) {
            if (offset >= this.end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        this.ch = ch;
        this.offset = offset;
        while (this.ch == '/' && this.offset < chars.length && chars[this.offset] == '/') {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final boolean nextIfArrayEnd() {
        char ch = this.ch;
        if (ch == '}' || ch == '\u001a') {
            throw new JSONException(this.info("Illegal syntax: `" + ch + '`'));
        }
        if (ch != ']') {
            return false;
        }
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return true;
        }
        char[] chars;
        for (chars = this.chars, ch = chars[offset++]; ch == '\0' || (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L); ch = chars[offset++]) {
            if (offset >= this.end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        if (ch == ',') {
            this.comma = true;
            for (ch = chars[offset++]; ch == '\0' || (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L); ch = chars[offset++]) {
                if (offset >= this.end) {
                    this.ch = '\u001a';
                    this.offset = offset;
                    return true;
                }
            }
        }
        this.ch = ch;
        this.offset = offset;
        while (this.ch == '/' && this.offset < chars.length && chars[this.offset] == '/') {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final boolean nextIfNullOrEmptyString() {
        final char first = this.ch;
        final int end = this.end;
        int offset = this.offset;
        final char[] chars = this.chars;
        if (first == 'n' && offset + 2 < end && chars[offset] == 'u' && chars[offset + 1] == 'l' && chars[offset + 2] == 'l') {
            offset += 3;
        }
        else {
            if ((first != '\"' && first != '\'') || offset >= end || chars[offset] != first) {
                return false;
            }
            ++offset;
        }
        char ch;
        for (ch = ((offset == end) ? '\u001a' : chars[offset]); ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = chars[offset]) {
            if (++offset >= end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        if (this.comma = (ch == ',')) {
            if (++offset >= end) {
                ch = '\u001a';
            }
            else {
                ch = chars[offset];
            }
        }
        if (offset >= end) {
            this.ch = '\u001a';
            this.offset = offset;
            return true;
        }
        while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
            if (++offset >= end) {
                this.ch = '\u001a';
                return true;
            }
            ch = chars[offset];
        }
        this.offset = offset + 1;
        this.ch = ch;
        return true;
    }
    
    @Override
    public final boolean nextIfMatchIdent(final char c0, final char c1, final char c2) {
        if (this.ch != c0) {
            return false;
        }
        final int offset2 = this.offset + 2;
        if (offset2 > this.end || this.chars[this.offset] != c1 || this.chars[this.offset + 1] != c2) {
            return false;
        }
        if (offset2 == this.end) {
            this.offset = offset2;
            this.ch = '\u001a';
            return true;
        }
        int offset3;
        char ch;
        for (offset3 = offset2, ch = this.chars[offset3]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = this.chars[offset3]) {
            if (++offset3 == this.end) {
                ch = '\u001a';
                break;
            }
        }
        if (offset3 == offset2) {
            return false;
        }
        this.offset = offset3 + 1;
        this.ch = ch;
        return true;
    }
    
    @Override
    public final boolean nextIfMatchIdent(final char c0, final char c1, final char c2, final char c3) {
        if (this.ch != c0) {
            return false;
        }
        final int offset3 = this.offset + 3;
        if (offset3 > this.end || this.chars[this.offset] != c1 || this.chars[this.offset + 1] != c2 || this.chars[this.offset + 2] != c3) {
            return false;
        }
        if (offset3 == this.end) {
            this.offset = offset3;
            this.ch = '\u001a';
            return true;
        }
        int offset4;
        char ch;
        for (offset4 = offset3, ch = this.chars[offset4]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = this.chars[offset4]) {
            if (++offset4 == this.end) {
                ch = '\u001a';
                break;
            }
        }
        if (offset4 == offset3 && ch != '(' && ch != '[' && ch != ']' && ch != ')' && ch != ':' && ch != ',') {
            return false;
        }
        this.offset = offset4 + 1;
        this.ch = ch;
        return true;
    }
    
    @Override
    public final boolean nextIfMatchIdent(final char c0, final char c1, final char c2, final char c3, final char c4) {
        if (this.ch != c0) {
            return false;
        }
        final int offset4 = this.offset + 4;
        if (offset4 > this.end || this.chars[this.offset] != c1 || this.chars[this.offset + 1] != c2 || this.chars[this.offset + 2] != c3 || this.chars[this.offset + 3] != c4) {
            return false;
        }
        if (offset4 == this.end) {
            this.offset = offset4;
            this.ch = '\u001a';
            return true;
        }
        int offset5;
        char ch;
        for (offset5 = offset4, ch = this.chars[offset5]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = this.chars[offset5]) {
            if (++offset5 == this.end) {
                ch = '\u001a';
                break;
            }
        }
        if (offset5 == offset4 && ch != '(' && ch != '[' && ch != ']' && ch != ')' && ch != ':' && ch != ',') {
            return false;
        }
        this.offset = offset5 + 1;
        this.ch = ch;
        return true;
    }
    
    @Override
    public final boolean nextIfMatchIdent(final char c0, final char c1, final char c2, final char c3, final char c4, final char c5) {
        if (this.ch != c0) {
            return false;
        }
        final int offset5 = this.offset + 5;
        if (offset5 > this.end || this.chars[this.offset] != c1 || this.chars[this.offset + 1] != c2 || this.chars[this.offset + 2] != c3 || this.chars[this.offset + 3] != c4 || this.chars[this.offset + 4] != c5) {
            return false;
        }
        if (offset5 == this.end) {
            this.offset = offset5;
            this.ch = '\u001a';
            return true;
        }
        int offset6;
        char ch;
        for (offset6 = offset5, ch = this.chars[offset6]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = this.chars[offset6]) {
            if (++offset6 == this.end) {
                ch = '\u001a';
                break;
            }
        }
        if (offset6 == offset5 && ch != '(' && ch != '[' && ch != ']' && ch != ')' && ch != ':' && ch != ',') {
            return false;
        }
        this.offset = offset6 + 1;
        this.ch = ch;
        return true;
    }
    
    @Override
    public final boolean nextIfSet() {
        final char[] chars = this.chars;
        int offset = this.offset;
        char ch = this.ch;
        if (ch == 'S' && offset + 1 < this.end && chars[offset] == 'e' && chars[offset + 1] == 't') {
            offset += 2;
            if (offset >= this.end) {
                ch = '\u001a';
            }
            else {
                for (ch = chars[offset++]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = chars[offset++]) {
                    if (offset == this.end) {
                        ch = '\u001a';
                        break;
                    }
                }
            }
            this.offset = offset;
            this.ch = ch;
            return true;
        }
        return false;
    }
    
    @Override
    public final boolean nextIfInfinity() {
        final char[] chars = this.chars;
        int offset = this.offset;
        char ch = this.ch;
        if (ch == 'I' && offset + 6 < this.end && chars[offset] == 'n' && chars[offset + 1] == 'f' && chars[offset + 2] == 'i' && chars[offset + 3] == 'n' && chars[offset + 4] == 'i' && chars[offset + 5] == 't' && chars[offset + 6] == 'y') {
            offset += 7;
            if (offset >= this.end) {
                ch = '\u001a';
            }
            else {
                for (ch = chars[offset++]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = chars[offset++]) {
                    if (offset == this.end) {
                        ch = '\u001a';
                        break;
                    }
                }
            }
            this.offset = offset;
            this.ch = ch;
            return true;
        }
        return false;
    }
    
    @Override
    public final boolean nextIfObjectStart() {
        char ch = this.ch;
        if (ch != '{') {
            return false;
        }
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return true;
        }
        char[] chars;
        for (chars = this.chars, ch = chars[offset++]; ch == '\0' || (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L); ch = chars[offset++]) {
            if (offset >= this.end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        this.ch = ch;
        this.offset = offset;
        while (this.ch == '/' && this.offset < chars.length && chars[this.offset] == '/') {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final boolean nextIfObjectEnd() {
        char ch = this.ch;
        if (ch == ']' || ch == '\u001a') {
            throw new JSONException(this.info("Illegal syntax: `" + ch + '`'));
        }
        if (ch != '}') {
            return false;
        }
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return true;
        }
        char[] chars;
        for (chars = this.chars, ch = chars[offset++]; ch == '\0' || (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L); ch = chars[offset++]) {
            if (offset >= this.end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        if (ch == ',') {
            this.comma = true;
            for (ch = chars[offset++]; ch == '\0' || (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L); ch = chars[offset++]) {
                if (offset >= this.end) {
                    this.ch = '\u001a';
                    this.offset = offset;
                    return true;
                }
            }
        }
        this.ch = ch;
        this.offset = offset;
        while (this.ch == '/' && this.offset < chars.length && chars[this.offset] == '/') {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final void next() {
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return;
        }
        char[] chars;
        char ch;
        for (chars = this.chars, ch = chars[offset]; ch == '\0' || (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L); ch = chars[offset]) {
            if (++offset >= this.end) {
                this.offset = offset;
                this.ch = '\u001a';
                return;
            }
        }
        this.offset = offset + 1;
        this.ch = ch;
        while (this.ch == '/' && this.offset < chars.length && chars[this.offset] == '/') {
            this.skipLineComment();
        }
    }
    
    @Override
    public final long readFieldNameHashCodeUnquote() {
        this.nameEscape = false;
        this.nameBegin = this.offset - 1;
        final char first = this.ch;
        long nameValue = 0L;
        int i = 0;
    Label_0893:
        while (this.offset <= this.end) {
            switch (this.ch) {
                case '\b':
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case '\u001a':
                case ' ':
                case '!':
                case '(':
                case ')':
                case '*':
                case '+':
                case ',':
                case '-':
                case '.':
                case '/':
                case ':':
                case '<':
                case '=':
                case '>':
                case '[':
                case ']':
                case '{':
                case '}': {
                    this.nameLength = i;
                    if (this.ch == '\u001a') {
                        this.nameEnd = this.offset;
                    }
                    else {
                        this.nameEnd = this.offset - 1;
                    }
                    while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                        this.next();
                    }
                    break Label_0893;
                }
                default: {
                    if (this.ch == '\\') {
                        this.nameEscape = true;
                        switch (this.ch = this.chars[this.offset++]) {
                            case 'u': {
                                final char c1 = this.chars[this.offset++];
                                final char c2 = this.chars[this.offset++];
                                final char c3 = this.chars[this.offset++];
                                final char c4 = this.chars[this.offset++];
                                this.ch = JSONReader.char4(c1, c2, c3, c4);
                                break;
                            }
                            case 'x': {
                                final char c1 = this.chars[this.offset++];
                                final char c2 = this.chars[this.offset++];
                                this.ch = JSONReader.char2(c1, c2);
                                break;
                            }
                            case '\"':
                            case '*':
                            case '+':
                            case '-':
                            case '.':
                            case '/':
                            case ':':
                            case '<':
                            case '=':
                            case '>':
                            case '@':
                            case '\\': {
                                break;
                            }
                            default: {
                                this.ch = this.char1(this.ch);
                                break;
                            }
                        }
                    }
                    if (this.ch > '\u00ff' || i >= 8 || (i == 0 && this.ch == '\0')) {
                        nameValue = 0L;
                        this.ch = first;
                        this.offset = this.nameBegin + 1;
                        break Label_0893;
                    }
                    final byte c5 = (byte)this.ch;
                    switch (i) {
                        case 0: {
                            nameValue = c5;
                            break;
                        }
                        case 1: {
                            nameValue = (c5 << 8) + (nameValue & 0xFFL);
                            break;
                        }
                        case 2: {
                            nameValue = (c5 << 16) + (nameValue & 0xFFFFL);
                            break;
                        }
                        case 3: {
                            nameValue = (c5 << 24) + (nameValue & 0xFFFFFFL);
                            break;
                        }
                        case 4: {
                            nameValue = ((long)c5 << 32) + (nameValue & 0xFFFFFFFFL);
                            break;
                        }
                        case 5: {
                            nameValue = ((long)c5 << 40) + (nameValue & 0xFFFFFFFFFFL);
                            break;
                        }
                        case 6: {
                            nameValue = ((long)c5 << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                            break;
                        }
                        case 7: {
                            nameValue = ((long)c5 << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                            break;
                        }
                    }
                    this.ch = ((this.offset >= this.end) ? '\u001a' : this.chars[this.offset++]);
                    ++i;
                    continue;
                }
            }
        }
        long hashCode;
        if (nameValue != 0L) {
            hashCode = nameValue;
        }
        else {
            hashCode = -3750763034362895579L;
            int j = 0;
        Label_1460:
            while (true) {
                if (this.ch == '\\') {
                    this.nameEscape = true;
                    switch (this.ch = this.chars[this.offset++]) {
                        case 'u': {
                            final char c6 = this.chars[this.offset++];
                            final char c7 = this.chars[this.offset++];
                            final char c8 = this.chars[this.offset++];
                            final char c9 = this.chars[this.offset++];
                            this.ch = JSONReader.char4(c6, c7, c8, c9);
                            break;
                        }
                        case 'x': {
                            final char c6 = this.chars[this.offset++];
                            final char c7 = this.chars[this.offset++];
                            this.ch = JSONReader.char2(c6, c7);
                            break;
                        }
                        case '\"':
                        case '*':
                        case '+':
                        case '-':
                        case '.':
                        case '/':
                        case ':':
                        case '<':
                        case '=':
                        case '>':
                        case '@':
                        case '\\': {
                            break;
                        }
                        default: {
                            this.ch = this.char1(this.ch);
                            break;
                        }
                    }
                    hashCode ^= this.ch;
                    hashCode *= 1099511628211L;
                    this.next();
                }
                else {
                    switch (this.ch) {
                        case '\b':
                        case '\t':
                        case '\n':
                        case '\f':
                        case '\r':
                        case '\u001a':
                        case ' ':
                        case '!':
                        case '(':
                        case ')':
                        case '*':
                        case '+':
                        case ',':
                        case '-':
                        case '.':
                        case '/':
                        case ':':
                        case '<':
                        case '=':
                        case '>':
                        case '[':
                        case ']':
                        case '{':
                        case '}': {
                            break Label_1460;
                        }
                        default: {
                            hashCode ^= this.ch;
                            hashCode *= 1099511628211L;
                            this.ch = ((this.offset >= this.end) ? '\u001a' : this.chars[this.offset++]);
                            break;
                        }
                    }
                }
                ++j;
            }
            this.nameLength = j;
            if (this.ch == '\u001a') {
                this.nameEnd = this.offset;
            }
            else {
                this.nameEnd = this.offset - 1;
            }
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                this.next();
            }
        }
        if (this.ch == ':') {
            if (this.offset == this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
            }
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                    break;
                }
                this.ch = this.chars[this.offset++];
            }
        }
        return hashCode;
    }
    
    @Override
    public final long readFieldNameHashCode() {
        final char[] chars = this.chars;
        if (this.ch != '\"' && this.ch != '\'') {
            if ((this.context.features & Feature.AllowUnQuotedFieldNames.mask) != 0x0L && JSONReader.isFirstIdentifier(this.ch)) {
                return this.readFieldNameHashCodeUnquote();
            }
            if (this.ch == '}' || this.isNull()) {
                return -1L;
            }
            String errorMsg;
            if (this.ch == '[' && this.nameBegin > 0) {
                errorMsg = "illegal fieldName input " + this.ch + ", previous fieldName " + this.getFieldName();
            }
            else {
                errorMsg = "illegal fieldName input" + this.ch;
            }
            throw new JSONException(this.info(errorMsg));
        }
        else {
            final char quote = this.ch;
            this.stringValue = null;
            this.nameEscape = false;
            final int offset2 = this.offset;
            this.nameBegin = offset2;
            int offset = offset2;
            long nameValue = 0L;
            if (offset + 9 < this.end) {
                final char c0 = chars[offset];
                final char c2 = chars[offset + 1];
                final char c3 = chars[offset + 2];
                final char c4 = chars[offset + 3];
                final char c5 = chars[offset + 4];
                final char c6 = chars[offset + 5];
                final char c7 = chars[offset + 6];
                final char c8 = chars[offset + 7];
                final char c9 = chars[offset + 8];
                if (c0 == quote) {
                    nameValue = 0L;
                }
                else if (c2 == quote && c0 != '\0' && c0 != '\\' && c0 <= '\u00ff') {
                    nameValue = (byte)c0;
                    this.nameLength = 1;
                    this.nameEnd = offset + 1;
                    offset += 2;
                }
                else if (c3 == quote && c0 != '\0' && c0 != '\\' && c2 != '\\' && c0 <= '\u00ff' && c2 <= '\u00ff') {
                    nameValue = ((byte)c2 << 8) + c0;
                    this.nameLength = 2;
                    this.nameEnd = offset + 2;
                    offset += 3;
                }
                else if (c4 == quote && c0 != '\0' && c0 != '\\' && c2 != '\\' && c3 != '\\' && c0 <= '\u00ff' && c2 <= '\u00ff' && c3 <= '\u00ff') {
                    nameValue = ((byte)c3 << 16) + (c2 << 8) + c0;
                    this.nameLength = 3;
                    this.nameEnd = offset + 3;
                    offset += 4;
                }
                else if (c5 == quote && c0 != '\0' && c0 != '\\' && c2 != '\\' && c3 != '\\' && c4 != '\\' && c0 <= '\u00ff' && c2 <= '\u00ff' && c3 <= '\u00ff' && c4 <= '\u00ff') {
                    nameValue = ((byte)c4 << 24) + (c3 << 16) + (c2 << 8) + c0;
                    this.nameLength = 4;
                    this.nameEnd = offset + 4;
                    offset += 5;
                }
                else if (c6 == quote && c0 != '\0' && c0 != '\\' && c2 != '\\' && c3 != '\\' && c4 != '\\' && c5 != '\\' && c0 <= '\u00ff' && c2 <= '\u00ff' && c3 <= '\u00ff' && c4 <= '\u00ff' && c5 <= '\u00ff') {
                    nameValue = ((long)(byte)c5 << 32) + ((long)c4 << 24) + ((long)c3 << 16) + ((long)c2 << 8) + c0;
                    this.nameLength = 5;
                    this.nameEnd = offset + 5;
                    offset += 6;
                }
                else if (c7 == quote && c0 != '\0' && c0 != '\\' && c2 != '\\' && c3 != '\\' && c4 != '\\' && c5 != '\\' && c6 != '\\' && c0 <= '\u00ff' && c2 <= '\u00ff' && c3 <= '\u00ff' && c4 <= '\u00ff' && c5 <= '\u00ff' && c6 <= '\u00ff') {
                    nameValue = ((long)(byte)c6 << 40) + ((long)c5 << 32) + ((long)c4 << 24) + ((long)c3 << 16) + ((long)c2 << 8) + c0;
                    this.nameLength = 6;
                    this.nameEnd = offset + 6;
                    offset += 7;
                }
                else if (c8 == quote && c0 != '\0' && c0 != '\\' && c2 != '\\' && c3 != '\\' && c4 != '\\' && c5 != '\\' && c6 != '\\' && c7 != '\\' && c0 <= '\u00ff' && c2 <= '\u00ff' && c3 <= '\u00ff' && c4 <= '\u00ff' && c5 <= '\u00ff' && c6 <= '\u00ff' && c7 <= '\u00ff') {
                    nameValue = ((long)(byte)c7 << 48) + ((long)c6 << 40) + ((long)c5 << 32) + ((long)c4 << 24) + ((long)c3 << 16) + ((long)c2 << 8) + c0;
                    this.nameLength = 7;
                    this.nameEnd = offset + 7;
                    offset += 8;
                }
                else if (c9 == quote && c0 != '\0' && c0 != '\\' && c2 != '\\' && c3 != '\\' && c4 != '\\' && c5 != '\\' && c6 != '\\' && c7 != '\\' && c8 != '\\' && c0 <= '\u00ff' && c2 <= '\u00ff' && c3 <= '\u00ff' && c4 <= '\u00ff' && c5 <= '\u00ff' && c6 <= '\u00ff' && c7 <= '\u00ff' && c8 <= '\u00ff') {
                    nameValue = ((long)(byte)c8 << 56) + ((long)c7 << 48) + ((long)c6 << 40) + ((long)c5 << 32) + ((long)c4 << 24) + ((long)c3 << 16) + ((long)c2 << 8) + c0;
                    this.nameLength = 8;
                    this.nameEnd = offset + 8;
                    offset += 9;
                }
            }
            if (nameValue == 0L) {
                int i = 0;
                while (offset < this.end) {
                    char c10 = chars[offset];
                    if (c10 == quote) {
                        if (i == 0) {
                            offset = this.nameBegin;
                            break;
                        }
                        this.nameLength = i;
                        this.nameEnd = offset;
                        ++offset;
                        break;
                    }
                    else {
                        if (c10 == '\\') {
                            this.nameEscape = true;
                            c10 = chars[++offset];
                            switch (c10) {
                                case 'u': {
                                    final char c11 = chars[++offset];
                                    final char c12 = chars[++offset];
                                    final char c13 = chars[++offset];
                                    final char c14 = chars[++offset];
                                    c10 = JSONReader.char4(c11, c12, c13, c14);
                                    break;
                                }
                                case 'x': {
                                    final char c11 = chars[++offset];
                                    final char c12 = chars[++offset];
                                    c10 = JSONReader.char2(c11, c12);
                                    break;
                                }
                                default: {
                                    c10 = this.char1(c10);
                                    break;
                                }
                            }
                        }
                        if (c10 > '\u00ff' || i >= 8 || (i == 0 && c10 == '\0')) {
                            nameValue = 0L;
                            offset = this.nameBegin;
                            break;
                        }
                        switch (i) {
                            case 0: {
                                nameValue = (byte)c10;
                                break;
                            }
                            case 1: {
                                nameValue = ((byte)c10 << 8) + (nameValue & 0xFFL);
                                break;
                            }
                            case 2: {
                                nameValue = ((byte)c10 << 16) + (nameValue & 0xFFFFL);
                                break;
                            }
                            case 3: {
                                nameValue = ((byte)c10 << 24) + (nameValue & 0xFFFFFFL);
                                break;
                            }
                            case 4: {
                                nameValue = ((long)(byte)c10 << 32) + (nameValue & 0xFFFFFFFFL);
                                break;
                            }
                            case 5: {
                                nameValue = ((long)(byte)c10 << 40) + (nameValue & 0xFFFFFFFFFFL);
                                break;
                            }
                            case 6: {
                                nameValue = ((long)(byte)c10 << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                                break;
                            }
                            case 7: {
                                nameValue = ((long)(byte)c10 << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                                break;
                            }
                        }
                        ++offset;
                        ++i;
                    }
                }
            }
            long hashCode;
            if (nameValue != 0L) {
                hashCode = nameValue;
            }
            else {
                hashCode = -3750763034362895579L;
                int j = 0;
                while (true) {
                    char c15 = chars[offset];
                    if (c15 == '\\') {
                        this.nameEscape = true;
                        c15 = chars[++offset];
                        switch (c15) {
                            case 'u': {
                                final char c16 = chars[++offset];
                                final char c17 = chars[++offset];
                                final char c18 = chars[++offset];
                                final char c19 = chars[++offset];
                                c15 = JSONReader.char4(c16, c17, c18, c19);
                                break;
                            }
                            case 'x': {
                                final char c16 = chars[++offset];
                                final char c17 = chars[++offset];
                                c15 = JSONReader.char2(c16, c17);
                                break;
                            }
                            default: {
                                c15 = this.char1(c15);
                                break;
                            }
                        }
                        ++offset;
                        hashCode ^= c15;
                        hashCode *= 1099511628211L;
                    }
                    else {
                        if (c15 == quote) {
                            break;
                        }
                        ++offset;
                        hashCode ^= c15;
                        hashCode *= 1099511628211L;
                    }
                    ++j;
                }
                this.nameLength = j;
                this.nameEnd = offset;
                ++offset;
            }
            char c20;
            if (offset < this.end) {
                for (c20 = chars[offset]; c20 <= ' ' && (1L << c20 & 0x100003700L) != 0x0L; c20 = chars[offset]) {
                    ++offset;
                }
            }
            else {
                c20 = '\u001a';
            }
            if (c20 != ':') {
                throw new JSONException(this.info("expect ':', but " + c20));
            }
            if (++offset == this.end) {
                c20 = '\u001a';
            }
            else {
                c20 = chars[offset];
            }
            while (c20 <= ' ' && (1L << c20 & 0x100003700L) != 0x0L) {
                ++offset;
                c20 = chars[offset];
            }
            this.offset = offset + 1;
            this.ch = c20;
            return hashCode;
        }
    }
    
    @Override
    public final long readValueHashCode() {
        if (this.ch != '\"' && this.ch != '\'') {
            return -1L;
        }
        final char quote = this.ch;
        this.nameEscape = false;
        final int offset2 = this.offset;
        this.nameBegin = offset2;
        int offset = offset2;
        long nameValue = 0L;
        int i = 0;
        while (offset < this.end) {
            char c = this.chars[offset];
            if (c == quote) {
                if (i == 0) {
                    nameValue = 0L;
                    offset = this.nameBegin;
                    break;
                }
                this.nameLength = i;
                this.nameEnd = offset;
                ++offset;
                break;
            }
            else {
                if (c == '\\') {
                    this.nameEscape = true;
                    c = this.chars[++offset];
                    switch (c) {
                        case 'u': {
                            final char c2 = this.chars[++offset];
                            final char c3 = this.chars[++offset];
                            final char c4 = this.chars[++offset];
                            final char c5 = this.chars[++offset];
                            c = JSONReader.char4(c2, c3, c4, c5);
                            break;
                        }
                        case 'x': {
                            final char c2 = this.chars[++offset];
                            final char c3 = this.chars[++offset];
                            c = JSONReader.char2(c2, c3);
                            break;
                        }
                        default: {
                            c = this.char1(c);
                            break;
                        }
                    }
                }
                if (c > '\u00ff' || i >= 8 || (i == 0 && c == '\0')) {
                    nameValue = 0L;
                    offset = this.nameBegin;
                    break;
                }
                switch (i) {
                    case 0: {
                        nameValue = (byte)c;
                        break;
                    }
                    case 1: {
                        nameValue = ((byte)c << 8) + (nameValue & 0xFFL);
                        break;
                    }
                    case 2: {
                        nameValue = ((byte)c << 16) + (nameValue & 0xFFFFL);
                        break;
                    }
                    case 3: {
                        nameValue = ((byte)c << 24) + (nameValue & 0xFFFFFFL);
                        break;
                    }
                    case 4: {
                        nameValue = ((long)(byte)c << 32) + (nameValue & 0xFFFFFFFFL);
                        break;
                    }
                    case 5: {
                        nameValue = ((long)(byte)c << 40) + (nameValue & 0xFFFFFFFFFFL);
                        break;
                    }
                    case 6: {
                        nameValue = ((long)(byte)c << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                        break;
                    }
                    case 7: {
                        nameValue = ((long)(byte)c << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                        break;
                    }
                }
                ++offset;
                ++i;
            }
        }
        long hashCode;
        if (nameValue != 0L) {
            hashCode = nameValue;
        }
        else {
            hashCode = -3750763034362895579L;
            int j = 0;
            while (true) {
                char c6 = this.chars[offset];
                if (c6 == '\\') {
                    this.nameEscape = true;
                    c6 = this.chars[++offset];
                    switch (c6) {
                        case 'u': {
                            final char c7 = this.chars[++offset];
                            final char c8 = this.chars[++offset];
                            final char c9 = this.chars[++offset];
                            final char c10 = this.chars[++offset];
                            c6 = JSONReader.char4(c7, c8, c9, c10);
                            break;
                        }
                        case 'x': {
                            final char c7 = this.chars[++offset];
                            final char c8 = this.chars[++offset];
                            c6 = JSONReader.char2(c7, c8);
                            break;
                        }
                        default: {
                            c6 = this.char1(c6);
                            break;
                        }
                    }
                    ++offset;
                    hashCode ^= c6;
                    hashCode *= 1099511628211L;
                }
                else {
                    if (c6 == '\"') {
                        break;
                    }
                    ++offset;
                    hashCode ^= c6;
                    hashCode *= 1099511628211L;
                }
                ++j;
            }
            this.nameLength = j;
            this.nameEnd = offset;
            this.stringValue = null;
            ++offset;
        }
        char c11;
        if (offset == this.end) {
            c11 = '\u001a';
        }
        else {
            c11 = this.chars[offset];
        }
        while (c11 <= ' ' && (1L << c11 & 0x100003700L) != 0x0L) {
            ++offset;
            c11 = this.chars[offset];
        }
        if (this.comma = (c11 == ',')) {
            if (++offset == this.end) {
                c11 = '\u001a';
            }
            else {
                c11 = this.chars[offset];
            }
            while (c11 <= ' ' && (1L << c11 & 0x100003700L) != 0x0L) {
                ++offset;
                c11 = this.chars[offset];
            }
        }
        this.offset = offset + 1;
        this.ch = c11;
        return hashCode;
    }
    
    @Override
    public final long getNameHashCodeLCase() {
        int offset = this.nameBegin;
        long nameValue = 0L;
        int i = 0;
        while (offset < this.end) {
            char c = this.chars[offset];
            if (c == '\\') {
                c = this.chars[++offset];
                switch (c) {
                    case 'u': {
                        final int c2 = this.chars[++offset];
                        final int c3 = this.chars[++offset];
                        final int c4 = this.chars[++offset];
                        final int c5 = this.chars[++offset];
                        c = JSONReader.char4(c2, c3, c4, c5);
                        break;
                    }
                    case 'x': {
                        final int c2 = this.chars[++offset];
                        final int c3 = this.chars[++offset];
                        c = JSONReader.char2(c2, c3);
                        break;
                    }
                    default: {
                        c = this.char1(c);
                        break;
                    }
                }
            }
            else if (c == '\"') {
                break;
            }
            if (c > '\u00ff' || i >= 8 || (i == 0 && c == '\0')) {
                nameValue = 0L;
                offset = this.nameBegin;
                break;
            }
            Label_0494: {
                if (c == '_' || c == '-' || c == ' ') {
                    final char c6 = this.chars[offset + 1];
                    if (c6 != '\"' && c6 != '\'' && c6 != c) {
                        break Label_0494;
                    }
                }
                if (c >= 'A' && c <= 'Z') {
                    c += ' ';
                }
                switch (i) {
                    case 0: {
                        nameValue = (byte)c;
                        break;
                    }
                    case 1: {
                        nameValue = ((byte)c << 8) + (nameValue & 0xFFL);
                        break;
                    }
                    case 2: {
                        nameValue = ((byte)c << 16) + (nameValue & 0xFFFFL);
                        break;
                    }
                    case 3: {
                        nameValue = ((byte)c << 24) + (nameValue & 0xFFFFFFL);
                        break;
                    }
                    case 4: {
                        nameValue = ((long)(byte)c << 32) + (nameValue & 0xFFFFFFFFL);
                        break;
                    }
                    case 5: {
                        nameValue = ((long)(byte)c << 40) + (nameValue & 0xFFFFFFFFFFL);
                        break;
                    }
                    case 6: {
                        nameValue = ((long)(byte)c << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                        break;
                    }
                    case 7: {
                        nameValue = ((long)(byte)c << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                        break;
                    }
                }
                ++i;
            }
            ++offset;
        }
        if (nameValue != 0L) {
            return nameValue;
        }
        long hashCode = -3750763034362895579L;
        while (offset < this.end) {
            char c7 = this.chars[offset];
            if (c7 == '\\') {
                c7 = this.chars[++offset];
                switch (c7) {
                    case 'u': {
                        final int c8 = this.chars[++offset];
                        final int c9 = this.chars[++offset];
                        final int c10 = this.chars[++offset];
                        final int c11 = this.chars[++offset];
                        c7 = JSONReader.char4(c8, c9, c10, c11);
                        break;
                    }
                    case 'x': {
                        final int c8 = this.chars[++offset];
                        final int c9 = this.chars[++offset];
                        c7 = JSONReader.char2(c8, c9);
                        break;
                    }
                    default: {
                        c7 = this.char1(c7);
                        break;
                    }
                }
            }
            else if (c7 == '\"') {
                break;
            }
            ++offset;
            if (c7 == '_' || c7 == '-' || c7 == ' ') {
                final char c12 = this.chars[offset];
                if (c12 != '\"' && c12 != '\'' && c12 != c7) {
                    continue;
                }
            }
            if (c7 >= 'A' && c7 <= 'Z') {
                c7 += ' ';
            }
            hashCode ^= c7;
            hashCode *= 1099511628211L;
        }
        return hashCode;
    }
    
    @Override
    public final String getFieldName() {
        if (this.nameEscape) {
            final char[] chars = new char[this.nameLength];
            for (int offset = this.nameBegin, i = 0; offset < this.nameEnd; ++offset, ++i) {
                char c = this.chars[offset];
                if (c == '\\') {
                    c = this.chars[++offset];
                    switch (c) {
                        case 'u': {
                            final int c2 = this.chars[++offset];
                            final int c3 = this.chars[++offset];
                            final int c4 = this.chars[++offset];
                            final int c5 = this.chars[++offset];
                            c = JSONReader.char4(c2, c3, c4, c5);
                            break;
                        }
                        case 'x': {
                            final int c2 = this.chars[++offset];
                            final int c3 = this.chars[++offset];
                            c = JSONReader.char2(c2, c3);
                            break;
                        }
                        case '\"':
                        case '*':
                        case '+':
                        case '-':
                        case '.':
                        case '/':
                        case ':':
                        case '<':
                        case '=':
                        case '>':
                        case '@':
                        case '\\': {
                            break;
                        }
                        default: {
                            c = this.char1(c);
                            break;
                        }
                    }
                }
                else if (c == '\"') {
                    break;
                }
                chars[i] = c;
            }
            return new String(chars);
        }
        if (this.str != null) {
            return this.str.substring(this.nameBegin, this.nameEnd);
        }
        return new String(this.chars, this.nameBegin, this.nameEnd - this.nameBegin);
    }
    
    @Override
    public final String readFieldName() {
        if (this.ch != '\"' && this.ch != '\'') {
            if ((this.context.features & Feature.AllowUnQuotedFieldNames.mask) != 0x0L && JSONReader.isFirstIdentifier(this.ch)) {
                return this.readFieldNameUnquote();
            }
            return null;
        }
        else {
            final char quote = this.ch;
            this.nameEscape = false;
            final int offset2 = this.offset;
            this.nameBegin = offset2;
            int offset = offset2;
            int i = 0;
            while (offset < this.end) {
                int c = this.chars[offset];
                if (c == 92) {
                    this.nameEscape = true;
                    c = this.chars[++offset];
                    switch (c) {
                        case 117: {
                            offset += 4;
                            break;
                        }
                        case 120: {
                            offset += 2;
                            break;
                        }
                    }
                    ++offset;
                }
                else if (c == quote) {
                    this.nameLength = i;
                    this.nameEnd = offset;
                    if (++offset < this.end) {
                        c = this.chars[offset];
                    }
                    else {
                        c = 26;
                    }
                    while (c <= 32 && (1L << c & 0x100003700L) != 0x0L) {
                        ++offset;
                        c = this.chars[offset];
                    }
                    if (c != 58) {
                        throw new JSONException("syntax error : " + offset);
                    }
                    if (++offset == this.end) {
                        c = 26;
                    }
                    else {
                        c = this.chars[offset];
                    }
                    while (c <= 32 && (1L << c & 0x100003700L) != 0x0L) {
                        ++offset;
                        c = this.chars[offset];
                    }
                    this.offset = offset + 1;
                    this.ch = (char)c;
                    break;
                }
                else {
                    ++offset;
                }
                ++i;
            }
            if (this.nameEnd < this.nameBegin) {
                throw new JSONException("syntax error : " + offset);
            }
            if (this.nameEscape) {
                return this.getFieldName();
            }
            long nameValue0 = -1L;
            long nameValue2 = -1L;
            switch (this.nameLength) {
                case 1: {
                    return TypeUtils.toString(this.chars[this.nameBegin]);
                }
                case 2: {
                    return TypeUtils.toString(this.chars[this.nameBegin], this.chars[this.nameBegin + 1]);
                }
                case 3: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4) {
                        nameValue0 = (c4 << 16) + (c3 << 8) + c2;
                        break;
                    }
                    break;
                }
                case 4: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5) {
                        nameValue0 = (c5 << 24) + (c4 << 16) + (c3 << 8) + c2;
                        break;
                    }
                    break;
                }
                case 5: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    final int c6 = this.chars[this.nameBegin + 4];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5 && (c6 & 0xFF) == c6) {
                        nameValue0 = ((long)c6 << 32) + ((long)c5 << 24) + ((long)c4 << 16) + ((long)c3 << 8) + c2;
                        break;
                    }
                    break;
                }
                case 6: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    final int c6 = this.chars[this.nameBegin + 4];
                    final int c7 = this.chars[this.nameBegin + 5];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5 && (c6 & 0xFF) == c6 && (c7 & 0xFF) == c7) {
                        nameValue0 = ((long)c7 << 40) + ((long)c6 << 32) + ((long)c5 << 24) + ((long)c4 << 16) + ((long)c3 << 8) + c2;
                        break;
                    }
                    break;
                }
                case 7: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    final int c6 = this.chars[this.nameBegin + 4];
                    final int c7 = this.chars[this.nameBegin + 5];
                    final int c8 = this.chars[this.nameBegin + 6];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5 && (c6 & 0xFF) == c6 && (c7 & 0xFF) == c7 && (c8 & 0xFF) == c8) {
                        nameValue0 = ((long)c8 << 48) + ((long)c7 << 40) + ((long)c6 << 32) + ((long)c5 << 24) + ((long)c4 << 16) + ((long)c3 << 8) + c2;
                        break;
                    }
                    break;
                }
                case 8: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    final int c6 = this.chars[this.nameBegin + 4];
                    final int c7 = this.chars[this.nameBegin + 5];
                    final int c8 = this.chars[this.nameBegin + 6];
                    final int c9 = this.chars[this.nameBegin + 7];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5 && (c6 & 0xFF) == c6 && (c7 & 0xFF) == c7 && (c8 & 0xFF) == c8 && (c9 & 0xFF) == c9) {
                        nameValue0 = ((long)c9 << 56) + ((long)c8 << 48) + ((long)c7 << 40) + ((long)c6 << 32) + ((long)c5 << 24) + ((long)c4 << 16) + ((long)c3 << 8) + c2;
                        break;
                    }
                    break;
                }
                case 9: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    final int c6 = this.chars[this.nameBegin + 4];
                    final int c7 = this.chars[this.nameBegin + 5];
                    final int c8 = this.chars[this.nameBegin + 6];
                    final int c9 = this.chars[this.nameBegin + 7];
                    final int c10 = this.chars[this.nameBegin + 8];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5 && (c6 & 0xFF) == c6 && (c7 & 0xFF) == c7 && (c8 & 0xFF) == c8 && (c9 & 0xFF) == c9 && (c10 & 0xFF) == c10) {
                        nameValue0 = c2;
                        nameValue2 = ((long)c10 << 56) + ((long)c9 << 48) + ((long)c8 << 40) + ((long)c7 << 32) + ((long)c6 << 24) + ((long)c5 << 16) + ((long)c4 << 8) + c3;
                        break;
                    }
                    break;
                }
                case 10: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    final int c6 = this.chars[this.nameBegin + 4];
                    final int c7 = this.chars[this.nameBegin + 5];
                    final int c8 = this.chars[this.nameBegin + 6];
                    final int c9 = this.chars[this.nameBegin + 7];
                    final int c10 = this.chars[this.nameBegin + 8];
                    final int c11 = this.chars[this.nameBegin + 9];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5 && (c6 & 0xFF) == c6 && (c7 & 0xFF) == c7 && (c8 & 0xFF) == c8 && (c9 & 0xFF) == c9 && (c10 & 0xFF) == c10 && (c11 & 0xFF) == c11) {
                        nameValue0 = (c3 << 8) + c2;
                        nameValue2 = ((long)c11 << 56) + ((long)c10 << 48) + ((long)c9 << 40) + ((long)c8 << 32) + ((long)c7 << 24) + ((long)c6 << 16) + ((long)c5 << 8) + c4;
                        break;
                    }
                    break;
                }
                case 11: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    final int c6 = this.chars[this.nameBegin + 4];
                    final int c7 = this.chars[this.nameBegin + 5];
                    final int c8 = this.chars[this.nameBegin + 6];
                    final int c9 = this.chars[this.nameBegin + 7];
                    final int c10 = this.chars[this.nameBegin + 8];
                    final int c11 = this.chars[this.nameBegin + 9];
                    final int c12 = this.chars[this.nameBegin + 10];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5 && (c6 & 0xFF) == c6 && (c7 & 0xFF) == c7 && (c8 & 0xFF) == c8 && (c9 & 0xFF) == c9 && (c10 & 0xFF) == c10 && (c11 & 0xFF) == c11 && (c12 & 0xFF) == c12) {
                        nameValue0 = (c4 << 16) + (c3 << 8) + c2;
                        nameValue2 = ((long)c12 << 56) + ((long)c11 << 48) + ((long)c10 << 40) + ((long)c9 << 32) + ((long)c8 << 24) + ((long)c7 << 16) + ((long)c6 << 8) + c5;
                        break;
                    }
                    break;
                }
                case 12: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    final int c6 = this.chars[this.nameBegin + 4];
                    final int c7 = this.chars[this.nameBegin + 5];
                    final int c8 = this.chars[this.nameBegin + 6];
                    final int c9 = this.chars[this.nameBegin + 7];
                    final int c10 = this.chars[this.nameBegin + 8];
                    final int c11 = this.chars[this.nameBegin + 9];
                    final int c12 = this.chars[this.nameBegin + 10];
                    final int c13 = this.chars[this.nameBegin + 11];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5 && (c6 & 0xFF) == c6 && (c7 & 0xFF) == c7 && (c8 & 0xFF) == c8 && (c9 & 0xFF) == c9 && (c10 & 0xFF) == c10 && (c11 & 0xFF) == c11 && (c12 & 0xFF) == c12 && (c13 & 0xFF) == c13) {
                        nameValue0 = (c5 << 24) + (c4 << 16) + (c3 << 8) + c2;
                        nameValue2 = ((long)c13 << 56) + ((long)c12 << 48) + ((long)c11 << 40) + ((long)c10 << 32) + ((long)c9 << 24) + ((long)c8 << 16) + ((long)c7 << 8) + c6;
                        break;
                    }
                    break;
                }
                case 13: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    final int c6 = this.chars[this.nameBegin + 4];
                    final int c7 = this.chars[this.nameBegin + 5];
                    final int c8 = this.chars[this.nameBegin + 6];
                    final int c9 = this.chars[this.nameBegin + 7];
                    final int c10 = this.chars[this.nameBegin + 8];
                    final int c11 = this.chars[this.nameBegin + 9];
                    final int c12 = this.chars[this.nameBegin + 10];
                    final int c13 = this.chars[this.nameBegin + 11];
                    final int c14 = this.chars[this.nameBegin + 12];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5 && (c6 & 0xFF) == c6 && (c7 & 0xFF) == c7 && (c8 & 0xFF) == c8 && (c9 & 0xFF) == c9 && (c10 & 0xFF) == c10 && (c11 & 0xFF) == c11 && (c12 & 0xFF) == c12 && (c13 & 0xFF) == c13 && (c14 & 0xFF) == c14) {
                        nameValue0 = ((long)c6 << 32) + ((long)c5 << 24) + ((long)c4 << 16) + ((long)c3 << 8) + c2;
                        nameValue2 = ((long)c14 << 56) + ((long)c13 << 48) + ((long)c12 << 40) + ((long)c11 << 32) + ((long)c10 << 24) + ((long)c9 << 16) + ((long)c8 << 8) + c7;
                        break;
                    }
                    break;
                }
                case 14: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    final int c6 = this.chars[this.nameBegin + 4];
                    final int c7 = this.chars[this.nameBegin + 5];
                    final int c8 = this.chars[this.nameBegin + 6];
                    final int c9 = this.chars[this.nameBegin + 7];
                    final int c10 = this.chars[this.nameBegin + 8];
                    final int c11 = this.chars[this.nameBegin + 9];
                    final int c12 = this.chars[this.nameBegin + 10];
                    final int c13 = this.chars[this.nameBegin + 11];
                    final int c14 = this.chars[this.nameBegin + 12];
                    final int c15 = this.chars[this.nameBegin + 13];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5 && (c6 & 0xFF) == c6 && (c7 & 0xFF) == c7 && (c8 & 0xFF) == c8 && (c9 & 0xFF) == c9 && (c10 & 0xFF) == c10 && (c11 & 0xFF) == c11 && (c12 & 0xFF) == c12 && (c13 & 0xFF) == c13 && (c14 & 0xFF) == c14 && (c15 & 0xFF) == c15) {
                        nameValue0 = ((long)c7 << 40) + ((long)c6 << 32) + ((long)c5 << 24) + ((long)c4 << 16) + ((long)c3 << 8) + c2;
                        nameValue2 = ((long)c15 << 56) + ((long)c14 << 48) + ((long)c13 << 40) + ((long)c12 << 32) + ((long)c11 << 24) + ((long)c10 << 16) + ((long)c9 << 8) + c8;
                        break;
                    }
                    break;
                }
                case 15: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    final int c6 = this.chars[this.nameBegin + 4];
                    final int c7 = this.chars[this.nameBegin + 5];
                    final int c8 = this.chars[this.nameBegin + 6];
                    final int c9 = this.chars[this.nameBegin + 7];
                    final int c10 = this.chars[this.nameBegin + 8];
                    final int c11 = this.chars[this.nameBegin + 9];
                    final int c12 = this.chars[this.nameBegin + 10];
                    final int c13 = this.chars[this.nameBegin + 11];
                    final int c14 = this.chars[this.nameBegin + 12];
                    final int c15 = this.chars[this.nameBegin + 13];
                    final int c16 = this.chars[this.nameBegin + 14];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5 && (c6 & 0xFF) == c6 && (c7 & 0xFF) == c7 && (c8 & 0xFF) == c8 && (c9 & 0xFF) == c9 && (c10 & 0xFF) == c10 && (c11 & 0xFF) == c11 && (c12 & 0xFF) == c12 && (c13 & 0xFF) == c13 && (c14 & 0xFF) == c14 && (c15 & 0xFF) == c15 && (c16 & 0xFF) == c16) {
                        nameValue0 = ((long)c8 << 48) + ((long)c7 << 40) + ((long)c6 << 32) + ((long)c5 << 24) + ((long)c4 << 16) + ((long)c3 << 8) + c2;
                        nameValue2 = ((long)c16 << 56) + ((long)c15 << 48) + ((long)c14 << 40) + ((long)c13 << 32) + ((long)c12 << 24) + ((long)c11 << 16) + ((long)c10 << 8) + c9;
                        break;
                    }
                    break;
                }
                case 16: {
                    final int c2 = this.chars[this.nameBegin];
                    final int c3 = this.chars[this.nameBegin + 1];
                    final int c4 = this.chars[this.nameBegin + 2];
                    final int c5 = this.chars[this.nameBegin + 3];
                    final int c6 = this.chars[this.nameBegin + 4];
                    final int c7 = this.chars[this.nameBegin + 5];
                    final int c8 = this.chars[this.nameBegin + 6];
                    final int c9 = this.chars[this.nameBegin + 7];
                    final int c10 = this.chars[this.nameBegin + 8];
                    final int c11 = this.chars[this.nameBegin + 9];
                    final int c12 = this.chars[this.nameBegin + 10];
                    final int c13 = this.chars[this.nameBegin + 11];
                    final int c14 = this.chars[this.nameBegin + 12];
                    final int c15 = this.chars[this.nameBegin + 13];
                    final int c16 = this.chars[this.nameBegin + 14];
                    final int c17 = this.chars[this.nameBegin + 15];
                    if ((c2 & 0xFF) == c2 && (c3 & 0xFF) == c3 && (c4 & 0xFF) == c4 && (c5 & 0xFF) == c5 && (c6 & 0xFF) == c6 && (c7 & 0xFF) == c7 && (c8 & 0xFF) == c8 && (c9 & 0xFF) == c9 && (c10 & 0xFF) == c10 && (c11 & 0xFF) == c11 && (c12 & 0xFF) == c12 && (c13 & 0xFF) == c13 && (c14 & 0xFF) == c14 && (c15 & 0xFF) == c15 && (c16 & 0xFF) == c16 && (c17 & 0xFF) == c17) {
                        nameValue0 = ((long)c9 << 56) + ((long)c8 << 48) + ((long)c7 << 40) + ((long)c6 << 32) + ((long)c5 << 24) + ((long)c4 << 16) + ((long)c3 << 8) + c2;
                        nameValue2 = ((long)c17 << 56) + ((long)c16 << 48) + ((long)c15 << 40) + ((long)c14 << 32) + ((long)c13 << 24) + ((long)c12 << 16) + ((long)c11 << 8) + c10;
                        break;
                    }
                    break;
                }
            }
            if (nameValue0 != -1L) {
                if (nameValue2 != -1L) {
                    final int indexMask = (int)nameValue2 & JSONFactory.NAME_CACHE2.length - 1;
                    final JSONFactory.NameCacheEntry2 entry = JSONFactory.NAME_CACHE2[indexMask];
                    if (entry == null) {
                        String name;
                        if (this.str != null) {
                            name = this.str.substring(this.nameBegin, this.nameEnd);
                        }
                        else {
                            name = new String(this.chars, this.nameBegin, this.nameEnd - this.nameBegin);
                        }
                        JSONFactory.NAME_CACHE2[indexMask] = new JSONFactory.NameCacheEntry2(name, nameValue0, nameValue2);
                        return name;
                    }
                    if (entry.value0 == nameValue0 && entry.value1 == nameValue2) {
                        return entry.name;
                    }
                }
                else {
                    final int indexMask = (int)nameValue0 & JSONFactory.NAME_CACHE.length - 1;
                    final JSONFactory.NameCacheEntry entry2 = JSONFactory.NAME_CACHE[indexMask];
                    if (entry2 == null) {
                        String name;
                        if (this.str != null) {
                            name = this.str.substring(this.nameBegin, this.nameEnd);
                        }
                        else {
                            name = new String(this.chars, this.nameBegin, this.nameEnd - this.nameBegin);
                        }
                        JSONFactory.NAME_CACHE[indexMask] = new JSONFactory.NameCacheEntry(name, nameValue0);
                        return name;
                    }
                    if (entry2.value == nameValue0) {
                        return entry2.name;
                    }
                }
            }
            if (this.str != null) {
                return this.str.substring(this.nameBegin, this.nameEnd);
            }
            return new String(this.chars, this.nameBegin, this.nameEnd - this.nameBegin);
        }
    }
    
    @Override
    public final boolean skipName() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException(this.info("not support unquoted name"));
        }
        final char quote = this.ch;
        int offset = this.offset;
        while (true) {
            char c = this.chars[offset];
            if (c == '\\') {
                c = this.chars[++offset];
                switch (c) {
                    case 'u': {
                        offset += 4;
                        break;
                    }
                    case 'x': {
                        offset += 2;
                        break;
                    }
                }
                ++offset;
            }
            else {
                if (c == quote) {
                    break;
                }
                ++offset;
            }
        }
        ++offset;
        char c;
        for (c = this.chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = this.chars[offset]) {
            ++offset;
        }
        if (c != ':') {
            throw new JSONException("syntax error, expect ',', but '" + c + "'");
        }
        ++offset;
        for (c = this.chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = this.chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final int readInt32Value() {
        boolean negative = false;
        final int firstOffset = this.offset;
        final char firstChar = this.ch;
        final char[] chars = this.chars;
        int intValue = 0;
        char quote = '\0';
        if (firstChar == '\"' || firstChar == '\'') {
            quote = this.ch;
            this.ch = chars[this.offset++];
        }
        if (this.ch == '-') {
            negative = true;
            this.ch = chars[this.offset++];
        }
        else if (this.ch == '+') {
            this.ch = chars[this.offset++];
        }
        boolean overflow = false;
        while (this.ch >= '0' && this.ch <= '9') {
            final int intValue2 = intValue * 10 + (this.ch - '0');
            if (intValue2 < intValue) {
                overflow = true;
                break;
            }
            intValue = intValue2;
            if (this.offset == this.end) {
                this.ch = '\u001a';
                break;
            }
            this.ch = chars[this.offset++];
        }
        boolean notMatch = false;
        if (this.ch == '.' || this.ch == 'e' || this.ch == 'E' || this.ch == 't' || this.ch == 'f' || this.ch == 'n' || this.ch == '{' || this.ch == '[' || overflow) {
            notMatch = true;
        }
        else if (quote != '\0' && this.ch != quote) {
            notMatch = true;
        }
        if (!notMatch) {
            if (quote != '\0') {
                this.wasNull = (firstOffset + 1 == this.offset);
                this.ch = ((this.offset == this.end) ? '\u001a' : chars[this.offset++]);
            }
            if (this.ch == 'L' || this.ch == 'F' || this.ch == 'D' || this.ch == 'B' || this.ch == 'S') {
                switch (this.ch) {
                    case 'B': {
                        this.valueType = 9;
                        break;
                    }
                    case 'S': {
                        this.valueType = 10;
                        break;
                    }
                    case 'L': {
                        this.valueType = 11;
                        break;
                    }
                    case 'F': {
                        this.valueType = 12;
                        break;
                    }
                    case 'D': {
                        this.valueType = 13;
                        break;
                    }
                }
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = chars[this.offset++];
                }
            }
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = chars[this.offset++];
                }
            }
            final boolean comma = this.ch == ',';
            this.comma = comma;
            if (comma) {
                this.ch = ((this.offset == this.end) ? '\u001a' : chars[this.offset++]);
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = chars[this.offset++];
                    }
                }
            }
            return negative ? (-intValue) : intValue;
        }
        this.offset = firstOffset;
        this.ch = firstChar;
        this.readNumber0();
        if (this.valueType == 1) {
            final BigInteger bigInteger = this.getBigInteger();
            try {
                return bigInteger.intValueExact();
            }
            catch (ArithmeticException ex) {
                throw new JSONException("int overflow, value " + bigInteger);
            }
        }
        if (this.valueType == 5 && (this.context.features & Feature.ErrorOnNullForPrimitives.mask) != 0x0L) {
            throw new JSONException(this.info("int value not support input null"));
        }
        return this.getInt32Value();
    }
    
    @Override
    public final Integer readInt32() {
        boolean negative = false;
        final int firstOffset = this.offset;
        final char firstChar = this.ch;
        int intValue = 0;
        char quote = '\0';
        if (this.ch == '\"' || this.ch == '\'') {
            quote = this.ch;
            this.ch = this.chars[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                    while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                        if (this.offset >= this.end) {
                            this.ch = '\u001a';
                        }
                        else {
                            this.ch = this.chars[this.offset++];
                        }
                    }
                    this.nextIfComma();
                }
                return null;
            }
        }
        if (this.ch == '-') {
            negative = true;
            this.ch = this.chars[this.offset++];
        }
        else if (this.ch == '+') {
            this.ch = this.chars[this.offset++];
        }
        boolean overflow = false;
        while (this.ch >= '0' && this.ch <= '9') {
            final int intValue2 = intValue * 10 + (this.ch - '0');
            if (intValue2 < intValue) {
                overflow = true;
                break;
            }
            intValue = intValue2;
            if (this.offset == this.end) {
                this.ch = '\u001a';
                ++this.offset;
                break;
            }
            this.ch = this.chars[this.offset++];
        }
        boolean notMatch = false;
        if (this.ch == '.' || this.ch == 'e' || this.ch == 'E' || this.ch == 't' || this.ch == 'f' || this.ch == 'n' || this.ch == '{' || this.ch == '[' || overflow) {
            notMatch = true;
        }
        else if (quote != '\0' && this.ch != quote) {
            notMatch = true;
        }
        if (!notMatch) {
            if (quote != '\0') {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
            }
            if (this.ch == 'L' || this.ch == 'F' || this.ch == 'D' || this.ch == 'B' || this.ch == 'S') {
                switch (this.ch) {
                    case 'B': {
                        this.valueType = 9;
                        break;
                    }
                    case 'S': {
                        this.valueType = 10;
                        break;
                    }
                    case 'L': {
                        this.valueType = 11;
                        break;
                    }
                    case 'F': {
                        this.valueType = 12;
                        break;
                    }
                    case 'D': {
                        this.valueType = 13;
                        break;
                    }
                }
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
            }
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
            }
            if (this.comma = (this.ch == ',')) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                    while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                        if (this.offset >= this.end) {
                            this.ch = '\u001a';
                        }
                        else {
                            this.ch = this.chars[this.offset++];
                        }
                    }
                }
            }
            return negative ? (-intValue) : intValue;
        }
        this.offset = firstOffset;
        this.ch = firstChar;
        this.readNumber0();
        if (this.wasNull) {
            return null;
        }
        return this.getInt32Value();
    }
    
    @Override
    public final long readInt64Value() {
        boolean negative = false;
        final int firstOffset = this.offset;
        final char firstChar = this.ch;
        final char[] chars = this.chars;
        long longValue = 0L;
        char quote = '\0';
        if (this.ch == '\"' || this.ch == '\'') {
            quote = this.ch;
            this.ch = chars[this.offset++];
        }
        if (this.ch == '-') {
            negative = true;
            this.ch = chars[this.offset++];
        }
        else if (this.ch == '+') {
            this.ch = chars[this.offset++];
        }
        boolean overflow = false;
        while (this.ch >= '0' && this.ch <= '9') {
            final long intValue10 = longValue * 10L + (this.ch - '0');
            if (intValue10 < longValue) {
                overflow = true;
                break;
            }
            longValue = intValue10;
            if (this.offset >= this.end) {
                this.ch = '\u001a';
                break;
            }
            this.ch = chars[this.offset++];
        }
        boolean notMatch = false;
        if (this.ch == '.' || this.ch == 'e' || this.ch == 'E' || this.ch == 't' || this.ch == 'f' || this.ch == 'n' || this.ch == '{' || this.ch == '[' || overflow) {
            notMatch = true;
        }
        else if (quote != '\0' && this.ch != quote) {
            notMatch = true;
        }
        if (notMatch) {
            this.offset = firstOffset;
            this.ch = firstChar;
            this.readNumber0();
            if (this.valueType == 1) {
                final BigInteger bigInteger = this.getBigInteger();
                try {
                    return bigInteger.longValueExact();
                }
                catch (ArithmeticException ex) {
                    throw new JSONException("long overflow, value " + bigInteger);
                }
            }
            return this.getInt64Value();
        }
        if (quote != '\0') {
            this.wasNull = (firstOffset + 1 == this.offset);
            this.ch = ((this.offset == this.end) ? '\u001a' : chars[this.offset++]);
        }
        if (this.ch == 'L' || this.ch == 'F' || this.ch == 'D' || this.ch == 'B' || this.ch == 'S') {
            switch (this.ch) {
                case 'B': {
                    this.valueType = 9;
                    break;
                }
                case 'S': {
                    this.valueType = 10;
                    break;
                }
                case 'L': {
                    this.valueType = 11;
                    break;
                }
                case 'F': {
                    this.valueType = 12;
                    break;
                }
                case 'D': {
                    this.valueType = 13;
                    break;
                }
            }
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = chars[this.offset++];
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = chars[this.offset++];
            }
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            this.ch = ((this.offset == this.end) ? '\u001a' : chars[this.offset++]);
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = chars[this.offset++];
                }
            }
        }
        return negative ? (-longValue) : longValue;
    }
    
    @Override
    public final Long readInt64() {
        boolean negative = false;
        final int firstOffset = this.offset;
        final char firstChar = this.ch;
        long longValue = 0L;
        char quote = '\0';
        if (this.ch == '\"' || this.ch == '\'') {
            quote = this.ch;
            this.ch = this.chars[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = this.chars[this.offset++];
                    }
                }
                this.nextIfComma();
                return null;
            }
        }
        if (this.ch == '-') {
            negative = true;
            this.ch = this.chars[this.offset++];
        }
        else if (this.ch == '+') {
            this.ch = this.chars[this.offset++];
        }
        boolean overflow = false;
        while (this.ch >= '0' && this.ch <= '9') {
            final long intValue10 = longValue * 10L + (this.ch - '0');
            if (intValue10 < longValue) {
                overflow = true;
                break;
            }
            longValue = intValue10;
            if (this.offset == this.end) {
                this.ch = '\u001a';
                break;
            }
            this.ch = this.chars[this.offset++];
        }
        boolean notMatch = false;
        if (this.ch == '.' || this.ch == 'e' || this.ch == 'E' || this.ch == 't' || this.ch == 'f' || this.ch == 'n' || this.ch == '{' || this.ch == '[' || overflow) {
            notMatch = true;
        }
        else if (quote != '\0' && this.ch != quote) {
            notMatch = true;
        }
        if (notMatch) {
            this.offset = firstOffset;
            this.ch = firstChar;
            this.readNumber0();
            return this.getInt64();
        }
        if (quote != '\0') {
            if (this.offset == this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
            }
        }
        if (this.ch == 'L' || this.ch == 'F' || this.ch == 'D' || this.ch == 'B' || this.ch == 'S') {
            switch (this.ch) {
                case 'B': {
                    this.valueType = 9;
                    break;
                }
                case 'S': {
                    this.valueType = 10;
                    break;
                }
                case 'L': {
                    this.valueType = 11;
                    break;
                }
                case 'F': {
                    this.valueType = 12;
                    break;
                }
                case 'D': {
                    this.valueType = 13;
                    break;
                }
            }
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
            }
        }
        if (this.comma = (this.ch == ',')) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = this.chars[this.offset++];
                    }
                }
            }
        }
        return negative ? (-longValue) : longValue;
    }
    
    @Override
    public final double readDoubleValue() {
        this.wasNull = false;
        boolean value = false;
        double doubleValue = 0.0;
        final char[] chars = this.chars;
        char quote = '\0';
        if (this.ch == '\"' || this.ch == '\'') {
            quote = this.ch;
            this.ch = chars[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = chars[this.offset++];
                }
                this.nextIfComma();
                this.wasNull = true;
                return 0.0;
            }
        }
        final int start = this.offset;
        if (this.ch == '-') {
            this.negative = true;
            this.ch = chars[this.offset++];
        }
        else {
            this.negative = false;
            if (this.ch == '+') {
                this.ch = chars[this.offset++];
            }
        }
        this.valueType = 1;
        boolean overflow = false;
        long longValue = 0L;
        while (this.ch >= '0' && this.ch <= '9') {
            if (!overflow) {
                final long intValue10 = longValue * 10L + (this.ch - '0');
                if (intValue10 < longValue) {
                    overflow = true;
                }
                else {
                    longValue = intValue10;
                }
            }
            if (this.offset == this.end) {
                this.ch = '\u001a';
                ++this.offset;
                break;
            }
            this.ch = chars[this.offset++];
        }
        this.scale = 0;
        if (this.ch == '.') {
            this.valueType = 2;
            this.ch = chars[this.offset++];
            while (this.ch >= '0' && this.ch <= '9') {
                ++this.scale;
                if (!overflow) {
                    final long intValue10 = longValue * 10L + (this.ch - '0');
                    if (intValue10 < longValue) {
                        overflow = true;
                    }
                    else {
                        longValue = intValue10;
                    }
                }
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                    ++this.offset;
                    break;
                }
                this.ch = chars[this.offset++];
            }
        }
        int expValue = 0;
        if (this.ch == 'e' || this.ch == 'E') {
            boolean negativeExp = false;
            this.ch = chars[this.offset++];
            if (this.ch == '-') {
                negativeExp = true;
                this.ch = chars[this.offset++];
            }
            else if (this.ch == '+') {
                this.ch = chars[this.offset++];
            }
            while (this.ch >= '0' && this.ch <= '9') {
                final int byteVal = this.ch - '0';
                expValue = expValue * 10 + byteVal;
                if (expValue > 1023) {
                    throw new JSONException("too large exp value : " + expValue);
                }
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                    ++this.offset;
                    break;
                }
                this.ch = chars[this.offset++];
            }
            if (negativeExp) {
                expValue = -expValue;
            }
            this.exponent = (short)expValue;
            this.valueType = 2;
        }
        if (this.offset == start) {
            if (this.ch == 'n') {
                if (chars[this.offset++] == 'u' && chars[this.offset++] == 'l' && chars[this.offset++] == 'l') {
                    if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) != 0x0L) {
                        throw new JSONException(this.info("long value not support input null"));
                    }
                    this.wasNull = true;
                    value = true;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = chars[this.offset++];
                    }
                }
            }
            else if (this.ch == 't') {
                if (chars[this.offset++] == 'r' && chars[this.offset++] == 'u' && chars[this.offset++] == 'e') {
                    value = true;
                    doubleValue = 1.0;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = chars[this.offset++];
                    }
                }
            }
            else if (this.ch == 'f') {
                if (this.offset + 4 <= this.end && JDKUtils.UNSAFE.getLong(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (this.offset << 1)) == IOUtils.ALSE_64) {
                    this.offset += 4;
                    doubleValue = 0.0;
                    value = true;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = chars[this.offset++];
                    }
                }
            }
            else if (this.ch == '{' && quote == '\0') {
                final Map<String, Object> obj = this.readObject();
                if (!obj.isEmpty()) {
                    throw new JSONException(this.info());
                }
                value = true;
                this.wasNull = true;
            }
            else if (this.ch == '[' && quote == '\0') {
                final List array = this.readArray();
                if (!array.isEmpty()) {
                    throw new JSONException(this.info());
                }
                value = true;
                this.wasNull = true;
            }
        }
        final int len = this.offset - start;
        String str = null;
        if (quote != '\0') {
            if (this.ch != quote) {
                --this.offset;
                this.ch = quote;
                str = this.readString();
            }
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = chars[this.offset++];
            }
        }
        if (!value) {
            if (!overflow) {
                if (longValue == 0L) {
                    if (this.scale == 1) {
                        doubleValue = 0.0;
                        value = true;
                    }
                }
                else {
                    final int scale = this.scale - expValue;
                    if (scale == 0) {
                        doubleValue = (double)longValue;
                        if (this.negative) {
                            doubleValue = -doubleValue;
                        }
                        value = true;
                    }
                    else if ((long)(double)longValue == longValue) {
                        if (0 < scale && scale < JSONFactory.DOUBLE_10_POW.length) {
                            doubleValue = longValue / JSONFactory.DOUBLE_10_POW[scale];
                            if (this.negative) {
                                doubleValue = -doubleValue;
                            }
                            value = true;
                        }
                        else if (0 > scale && scale > -JSONFactory.DOUBLE_10_POW.length) {
                            doubleValue = longValue * JSONFactory.DOUBLE_10_POW[-scale];
                            if (this.negative) {
                                doubleValue = -doubleValue;
                            }
                            value = true;
                        }
                    }
                    if (!value && scale > -128 && scale < 128) {
                        doubleValue = TypeUtils.doubleValue(this.negative ? -1 : 1, longValue, scale);
                        value = true;
                    }
                }
            }
            Label_1528: {
                if (!value) {
                    if (str != null) {
                        try {
                            doubleValue = Double.parseDouble(str);
                            break Label_1528;
                        }
                        catch (NumberFormatException ex) {
                            throw new JSONException(this.info(), ex);
                        }
                    }
                    doubleValue = TypeUtils.parseDouble(chars, start - 1, len);
                }
            }
            if (this.ch == 'L' || this.ch == 'F' || this.ch == 'D' || this.ch == 'B' || this.ch == 'S') {
                switch (this.ch) {
                    case 'B': {
                        this.valueType = 9;
                        break;
                    }
                    case 'S': {
                        this.valueType = 10;
                        break;
                    }
                    case 'L': {
                        this.valueType = 11;
                        break;
                    }
                    case 'F': {
                        this.valueType = 12;
                        break;
                    }
                    case 'D': {
                        this.valueType = 13;
                        break;
                    }
                }
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = chars[this.offset++];
                }
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = chars[this.offset++];
            }
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = chars[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = chars[this.offset++];
                    }
                }
            }
        }
        return doubleValue;
    }
    
    @Override
    public final float readFloatValue() {
        this.wasNull = false;
        boolean value = false;
        float floatValue = 0.0f;
        char quote = '\0';
        if (this.ch == '\"' || this.ch == '\'') {
            quote = this.ch;
            this.ch = this.chars[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
                this.nextIfComma();
                this.wasNull = true;
                return 0.0f;
            }
        }
        final int start = this.offset;
        if (this.ch == '-') {
            this.negative = true;
            this.ch = this.chars[this.offset++];
        }
        else {
            this.negative = false;
            if (this.ch == '+') {
                this.ch = this.chars[this.offset++];
            }
        }
        this.valueType = 1;
        boolean overflow = false;
        long longValue = 0L;
        while (this.ch >= '0' && this.ch <= '9') {
            if (!overflow) {
                final long intValue10 = longValue * 10L + (this.ch - '0');
                if (intValue10 < longValue) {
                    overflow = true;
                }
                else {
                    longValue = intValue10;
                }
            }
            if (this.offset == this.end) {
                this.ch = '\u001a';
                ++this.offset;
                break;
            }
            this.ch = this.chars[this.offset++];
        }
        this.scale = 0;
        if (this.ch == '.') {
            this.valueType = 2;
            this.ch = this.chars[this.offset++];
            while (this.ch >= '0' && this.ch <= '9') {
                ++this.scale;
                if (!overflow) {
                    final long intValue10 = longValue * 10L + (this.ch - '0');
                    if (intValue10 < longValue) {
                        overflow = true;
                    }
                    else {
                        longValue = intValue10;
                    }
                }
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                    ++this.offset;
                    break;
                }
                this.ch = this.chars[this.offset++];
            }
        }
        int expValue = 0;
        if (this.ch == 'e' || this.ch == 'E') {
            boolean negativeExp = false;
            this.ch = this.chars[this.offset++];
            if (this.ch == '-') {
                negativeExp = true;
                this.ch = this.chars[this.offset++];
            }
            else if (this.ch == '+') {
                this.ch = this.chars[this.offset++];
            }
            while (this.ch >= '0' && this.ch <= '9') {
                final int byteVal = this.ch - '0';
                expValue = expValue * 10 + byteVal;
                if (expValue > 1023) {
                    throw new JSONException("too large exp value : " + expValue);
                }
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                    ++this.offset;
                    break;
                }
                this.ch = this.chars[this.offset++];
            }
            if (negativeExp) {
                expValue = -expValue;
            }
            this.exponent = (short)expValue;
            this.valueType = 2;
        }
        if (this.offset == start) {
            if (this.ch == 'n') {
                if (this.chars[this.offset++] == 'u' && this.chars[this.offset++] == 'l' && this.chars[this.offset++] == 'l') {
                    if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) != 0x0L) {
                        throw new JSONException(this.info("long value not support input null"));
                    }
                    this.wasNull = true;
                    value = true;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = this.chars[this.offset++];
                    }
                }
            }
            else if (this.ch == 't') {
                if (this.chars[this.offset++] == 'r' && this.chars[this.offset++] == 'u' && this.chars[this.offset++] == 'e') {
                    value = true;
                    floatValue = 1.0f;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = this.chars[this.offset++];
                    }
                }
            }
            else if (this.ch == 'f') {
                if (this.chars[this.offset++] == 'a' && this.chars[this.offset++] == 'l' && this.chars[this.offset++] == 's' && this.chars[this.offset++] == 'e') {
                    floatValue = 0.0f;
                    value = true;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = this.chars[this.offset++];
                    }
                }
            }
            else if (this.ch == '{' && quote == '\0') {
                final Map<String, Object> obj = this.readObject();
                if (!obj.isEmpty()) {
                    throw new JSONException(this.info());
                }
                value = true;
                this.wasNull = true;
            }
            else if (this.ch == '[' && quote == '\0') {
                final List array = this.readArray();
                if (!array.isEmpty()) {
                    throw new JSONException(this.info());
                }
                value = true;
                this.wasNull = true;
            }
        }
        final int len = this.offset - start;
        String str = null;
        if (quote != '\0') {
            if (this.ch != quote) {
                overflow = true;
                --this.offset;
                this.ch = quote;
                str = this.readString();
            }
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
            }
        }
        if (!value) {
            if (!overflow) {
                final int scale = this.scale - expValue;
                if (scale == 0) {
                    floatValue = (float)longValue;
                    if (this.negative) {
                        floatValue = -floatValue;
                    }
                    value = true;
                }
                else if ((long)(float)longValue == longValue) {
                    if (0 < scale && scale < JSONFactory.FLOAT_10_POW.length) {
                        floatValue = longValue / JSONFactory.FLOAT_10_POW[scale];
                        if (this.negative) {
                            floatValue = -floatValue;
                        }
                    }
                    else if (0 > scale && scale > -JSONFactory.FLOAT_10_POW.length) {
                        floatValue = longValue * JSONFactory.FLOAT_10_POW[-scale];
                        if (this.negative) {
                            floatValue = -floatValue;
                        }
                    }
                }
                if (!value && scale > -128 && scale < 128) {
                    floatValue = TypeUtils.floatValue(this.negative ? -1 : 1, longValue, scale);
                    value = true;
                }
            }
            Label_1590: {
                if (!value) {
                    if (str != null) {
                        try {
                            floatValue = Float.parseFloat(str);
                            break Label_1590;
                        }
                        catch (NumberFormatException ex) {
                            throw new JSONException(this.info(), ex);
                        }
                    }
                    floatValue = TypeUtils.parseFloat(this.chars, start - 1, len);
                }
            }
            if (this.ch == 'L' || this.ch == 'F' || this.ch == 'D' || this.ch == 'B' || this.ch == 'S') {
                switch (this.ch) {
                    case 'B': {
                        this.valueType = 9;
                        break;
                    }
                    case 'S': {
                        this.valueType = 10;
                        break;
                    }
                    case 'L': {
                        this.valueType = 11;
                        break;
                    }
                    case 'F': {
                        this.valueType = 12;
                        break;
                    }
                    case 'D': {
                        this.valueType = 13;
                        break;
                    }
                }
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
            }
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = this.chars[this.offset++];
                    }
                }
            }
        }
        return floatValue;
    }
    
    private void skipString() {
        final char[] chars = this.chars;
        int offset = this.offset;
        final char quote = this.ch;
        char ch = chars[offset++];
        while (true) {
            if (ch == '\\') {
                if (offset >= this.end) {
                    throw new JSONException(this.info("illegal string, end"));
                }
                ch = chars[offset++];
                if (ch == '\\' || ch == '\"') {
                    ch = chars[offset++];
                }
                else if (ch == 'u') {
                    ch = chars[offset + 4];
                    offset += 5;
                }
                else {
                    ch = this.char1(ch);
                }
            }
            else {
                if (ch == quote) {
                    ch = ((offset < this.end) ? chars[offset++] : '\u001a');
                    break;
                }
                if (offset >= this.end) {
                    ch = '\u001a';
                    break;
                }
                ch = chars[offset++];
            }
        }
        while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
            ch = chars[offset++];
        }
        final boolean comma = ch == ',';
        this.comma = comma;
        if (comma) {
            if (offset >= this.end) {
                this.offset = offset;
                this.ch = '\u001a';
                return;
            }
            for (ch = chars[offset]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = chars[offset]) {
                if (++offset >= this.end) {
                    this.offset = offset;
                    this.ch = '\u001a';
                    return;
                }
            }
            ++offset;
        }
        this.offset = offset;
        this.ch = ch;
    }
    
    @Override
    public final String getString() {
        if (this.stringValue != null) {
            return this.stringValue;
        }
        final int length = this.nameEnd - this.nameBegin;
        if (!this.nameEscape) {
            return new String(this.chars, this.nameBegin, length);
        }
        final char[] chars = new char[this.nameLength];
        int offset = this.nameBegin;
        int i = 0;
        while (true) {
            char c = this.chars[offset];
            if (c == '\\') {
                c = this.chars[++offset];
                switch (c) {
                    case 'u': {
                        final int c2 = this.chars[++offset];
                        final int c3 = this.chars[++offset];
                        final int c4 = this.chars[++offset];
                        final int c5 = this.chars[++offset];
                        c = JSONReader.char4(c2, c3, c4, c5);
                        break;
                    }
                    case 'x': {
                        final int c2 = this.chars[++offset];
                        final int c3 = this.chars[++offset];
                        c = JSONReader.char2(c2, c3);
                        break;
                    }
                    case '\"':
                    case '\\': {
                        break;
                    }
                    default: {
                        c = this.char1(c);
                        break;
                    }
                }
            }
            else if (c == '\"') {
                break;
            }
            chars[i] = c;
            ++offset;
            ++i;
        }
        return this.stringValue = new String(chars);
    }
    
    protected final void readString0() {
        final char quote = this.ch;
        final int start;
        int offset = start = this.offset;
        this.valueEscape = false;
        int i = 0;
        while (true) {
            char c = this.chars[offset];
            if (c == '\\') {
                this.valueEscape = true;
                c = this.chars[++offset];
                switch (c) {
                    case 'u': {
                        offset += 4;
                        break;
                    }
                    case 'x': {
                        offset += 2;
                        break;
                    }
                }
                ++offset;
            }
            else {
                if (c == quote) {
                    break;
                }
                ++offset;
            }
            ++i;
        }
        final int valueLength = i;
        String str;
        if (this.valueEscape) {
            final char[] chars = new char[valueLength];
            offset = start;
            int j = 0;
            while (true) {
                char c2 = this.chars[offset];
                if (c2 == '\\') {
                    c2 = this.chars[++offset];
                    switch (c2) {
                        case 'u': {
                            final int c3 = this.chars[++offset];
                            final int c4 = this.chars[++offset];
                            final int c5 = this.chars[++offset];
                            final int c6 = this.chars[++offset];
                            c2 = JSONReader.char4(c3, c4, c5, c6);
                            break;
                        }
                        case 'x': {
                            final int c3 = this.chars[++offset];
                            final int c4 = this.chars[++offset];
                            c2 = JSONReader.char2(c3, c4);
                            break;
                        }
                        case '\"':
                        case '\\': {
                            break;
                        }
                        default: {
                            c2 = this.char1(c2);
                            break;
                        }
                    }
                }
                else if (c2 == '\"') {
                    break;
                }
                chars[j] = c2;
                ++offset;
                ++j;
            }
            str = new String(chars);
        }
        else {
            str = new String(this.chars, this.offset, offset - this.offset);
        }
        int b;
        if (++offset == this.end) {
            b = 26;
        }
        else {
            b = this.chars[offset];
        }
        while (b <= 32 && (1L << b & 0x100003700L) != 0x0L) {
            b = this.chars[++offset];
        }
        final boolean comma = b == 44;
        this.comma = comma;
        if (comma) {
            this.offset = offset + 1;
            this.ch = this.chars[this.offset++];
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
            }
        }
        else {
            this.offset = offset + 1;
            this.ch = (char)b;
        }
        this.stringValue = str;
    }
    
    @Override
    public String readString() {
        if (this.ch == '\"' || this.ch == '\'') {
            final char quote = this.ch;
            final int start;
            int offset = start = this.offset;
            boolean valueEscape = false;
            int i = 0;
            char c0 = '\0';
            char c2 = '\0';
            char c3 = '\0';
            char c4 = '\0';
            boolean quoted = false;
            for (int upperBound = offset + (this.end - offset & 0xFFFFFFFC); offset < upperBound; offset += 4, i += 4) {
                c0 = this.chars[offset];
                c2 = this.chars[offset + 1];
                c3 = this.chars[offset + 2];
                c4 = this.chars[offset + 3];
                if (c0 == '\\' || c2 == '\\' || c3 == '\\') {
                    break;
                }
                if (c4 == '\\') {
                    break;
                }
                if (c0 == quote || c2 == quote || c3 == quote || c4 == quote) {
                    quoted = true;
                    break;
                }
            }
            int valueLength = 0;
            Label_0356: {
                if (!quoted) {
                    while (offset < this.end) {
                        char c5 = this.chars[offset];
                        if (c5 == '\\') {
                            valueEscape = true;
                            c5 = this.chars[++offset];
                            switch (c5) {
                                case 'u': {
                                    offset += 4;
                                    break;
                                }
                                case 'x': {
                                    offset += 2;
                                    break;
                                }
                            }
                            ++offset;
                        }
                        else {
                            if (c5 == quote) {
                                valueLength = i;
                                break Label_0356;
                            }
                            ++offset;
                        }
                        ++i;
                    }
                    throw new JSONException(this.info("invalid escape character EOI"));
                }
                if (c0 != quote) {
                    if (c2 == quote) {
                        ++offset;
                        ++i;
                    }
                    else if (c3 == quote) {
                        offset += 2;
                        i += 2;
                    }
                    else {
                        offset += 3;
                        i += 3;
                    }
                }
                valueLength = i;
            }
            String str;
            if (valueEscape) {
                final char[] chars = new char[valueLength];
                offset = start;
                int j = 0;
                while (true) {
                    char c6 = this.chars[offset];
                    if (c6 == '\\') {
                        c6 = this.chars[++offset];
                        switch (c6) {
                            case 'u': {
                                final char c7 = this.chars[++offset];
                                final char c8 = this.chars[++offset];
                                final char c9 = this.chars[++offset];
                                final char c10 = this.chars[++offset];
                                c6 = JSONReader.char4(c7, c8, c9, c10);
                                break;
                            }
                            case 'x': {
                                final char c7 = this.chars[++offset];
                                final char c8 = this.chars[++offset];
                                c6 = JSONReader.char2(c7, c8);
                                break;
                            }
                            case '\"':
                            case '\\': {
                                break;
                            }
                            case 'b': {
                                c6 = '\b';
                                break;
                            }
                            case 't': {
                                c6 = '\t';
                                break;
                            }
                            case 'n': {
                                c6 = '\n';
                                break;
                            }
                            case 'f': {
                                c6 = '\f';
                                break;
                            }
                            case 'r': {
                                c6 = '\r';
                                break;
                            }
                            default: {
                                c6 = this.char1(c6);
                                break;
                            }
                        }
                    }
                    else if (c6 == quote) {
                        break;
                    }
                    chars[j] = c6;
                    ++offset;
                    ++j;
                }
                if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                    str = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
                }
                else {
                    str = new String(chars);
                }
            }
            else {
                final int strlen = offset - this.offset;
                if (strlen == 1 && (c0 = this.chars[this.offset]) < '\u0080') {
                    str = TypeUtils.toString(c0);
                }
                else if (strlen == 2 && (c0 = this.chars[this.offset]) < '\u0080' && (c2 = this.chars[this.offset + 1]) < '\u0080') {
                    str = TypeUtils.toString(c0, c2);
                }
                else if (this.str != null && (JDKUtils.JVM_VERSION > 8 || JDKUtils.ANDROID)) {
                    str = this.str.substring(this.offset, offset);
                }
                else {
                    str = new String(this.chars, this.offset, offset - this.offset);
                }
            }
            if ((this.context.features & Feature.TrimString.mask) != 0x0L) {
                str = str.trim();
            }
            Label_1054: {
                if (++offset != this.end) {
                    char e;
                    for (e = this.chars[offset++]; e <= ' ' && (1L << e & 0x100003700L) != 0x0L; e = this.chars[offset++]) {
                        if (offset == this.end) {
                            break Label_1054;
                        }
                    }
                    final boolean comma = e == ',';
                    this.comma = comma;
                    if (comma) {
                        if (offset == this.end) {
                            e = '\u001a';
                        }
                        else {
                            for (e = this.chars[offset++]; e <= ' ' && (1L << e & 0x100003700L) != 0x0L; e = this.chars[offset++]) {
                                if (offset == this.end) {
                                    e = '\u001a';
                                    break;
                                }
                            }
                        }
                    }
                    this.ch = e;
                    this.offset = offset;
                    return str;
                }
            }
            this.ch = '\u001a';
            this.comma = false;
            this.offset = offset;
            return str;
        }
        switch (this.ch) {
            case '[': {
                return this.toString(this.readArray());
            }
            case '{': {
                return this.toString(this.readObject());
            }
            case '+':
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                this.readNumber0();
                final Number number = this.getNumber();
                return number.toString();
            }
            case 'f':
            case 't': {
                this.boolValue = this.readBoolValue();
                return this.boolValue ? "true" : "false";
            }
            case 'n': {
                this.readNull();
                return null;
            }
            default: {
                throw new JSONException(this.info("illegal input : " + this.ch));
            }
        }
    }
    
    @Override
    public final void skipValue() {
        switch (this.ch) {
            case '[': {
                this.next();
                int i = 0;
                while (this.ch != ']') {
                    if (i != 0 && !this.comma) {
                        throw new JSONValidException(this.info("illegal value"));
                    }
                    this.comma = false;
                    this.skipValue();
                    ++i;
                }
                this.next();
                break;
            }
            case '{': {
                this.next();
                while (this.ch != '}') {
                    this.skipName();
                    this.skipValue();
                }
                this.comma = false;
                this.next();
                break;
            }
            case '\"': {
                this.skipString();
                break;
            }
            case '+':
            case '-':
            case '.':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                final boolean sign = this.ch == '-' || this.ch == '+';
                if (sign) {
                    if (this.offset >= this.end) {
                        throw new JSONException("illegal number, offset " + this.offset);
                    }
                    this.ch = this.chars[this.offset++];
                }
                final boolean dot = this.ch == '.';
                boolean num = false;
                Label_0683: {
                    if (!dot && this.ch >= '0' && this.ch <= '9') {
                        num = true;
                        while (this.offset < this.end) {
                            this.ch = this.chars[this.offset++];
                            if (this.ch < '0' || this.ch > '9') {
                                break Label_0683;
                            }
                        }
                        this.ch = '\u001a';
                        return;
                    }
                }
                if (num && (this.ch == 'L' || this.ch == 'F' || this.ch == 'D' || this.ch == 'B' || this.ch == 'S')) {
                    this.ch = this.chars[this.offset++];
                }
                boolean small = false;
                Label_0885: {
                    if (this.ch == '.') {
                        small = true;
                        if (this.offset >= this.end) {
                            this.ch = '\u001a';
                            return;
                        }
                        this.ch = this.chars[this.offset++];
                        if (this.ch >= '0' && this.ch <= '9') {
                            while (this.offset < this.end) {
                                this.ch = this.chars[this.offset++];
                                if (this.ch < '0' || this.ch > '9') {
                                    break Label_0885;
                                }
                            }
                            this.ch = '\u001a';
                            return;
                        }
                    }
                }
                if (!num && !small) {
                    throw new JSONException("illegal number, offset " + this.offset + ", char " + this.ch);
                }
                Label_1194: {
                    if (this.ch == 'e' || this.ch == 'E') {
                        this.ch = this.chars[this.offset++];
                        boolean eSign = false;
                        if (this.ch == '+' || this.ch == '-') {
                            eSign = true;
                            if (this.offset >= this.end) {
                                throw new JSONException("illegal number, offset " + this.offset);
                            }
                            this.ch = this.chars[this.offset++];
                        }
                        if (this.ch >= '0' && this.ch <= '9') {
                            while (this.offset < this.end) {
                                this.ch = this.chars[this.offset++];
                                if (this.ch < '0') {
                                    break Label_1194;
                                }
                                if (this.ch > '9') {
                                    break Label_1194;
                                }
                            }
                            this.ch = '\u001a';
                            return;
                        }
                        if (eSign) {
                            throw new JSONException("illegal number, offset " + this.offset + ", char " + this.ch);
                        }
                    }
                }
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                        return;
                    }
                    this.ch = this.chars[this.offset++];
                }
                if (this.ch == '}' || this.ch == ']') {
                    return;
                }
                if (this.ch != ',') {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                this.comma = true;
                if (this.offset >= this.end) {
                    throw new JSONException("illegal number, offset " + this.offset);
                }
                this.ch = this.chars[this.offset];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    ++this.offset;
                    if (this.offset >= this.end) {
                        throw new JSONException("illegal number, offset " + this.offset);
                    }
                    this.ch = this.chars[this.offset];
                }
                ++this.offset;
                return;
            }
            case 't': {
                if (this.offset + 3 > this.end) {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                if (this.chars[this.offset] != 'r' || this.chars[this.offset + 1] != 'u' || this.chars[this.offset + 2] != 'e') {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                this.offset += 3;
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                    return;
                }
                this.ch = this.chars[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                        return;
                    }
                    this.ch = this.chars[this.offset++];
                }
                if (this.ch == '}' || this.ch == ']') {
                    return;
                }
                break;
            }
            case 'f': {
                if (this.offset + 4 > this.end) {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                if (this.chars[this.offset] != 'a' || this.chars[this.offset + 1] != 'l' || this.chars[this.offset + 2] != 's' || this.chars[this.offset + 3] != 'e') {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                this.offset += 4;
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                    return;
                }
                this.ch = this.chars[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                        return;
                    }
                    this.ch = this.chars[this.offset++];
                }
                if (this.ch == '}' || this.ch == ']') {
                    return;
                }
                break;
            }
            case 'n': {
                if (this.offset + 3 > this.end) {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                if (this.chars[this.offset] != 'u' || this.chars[this.offset + 1] != 'l' || this.chars[this.offset + 2] != 'l') {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                this.offset += 3;
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                    return;
                }
                this.ch = this.chars[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                        return;
                    }
                    this.ch = this.chars[this.offset++];
                }
                if (this.ch == '}' || this.ch == ']') {
                    return;
                }
                break;
            }
            case 'S': {
                if (this.nextIfSet()) {
                    this.skipValue();
                    break;
                }
                throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
            }
            default: {
                throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
            }
        }
        if (this.ch == ',') {
            this.comma = true;
            if (this.offset >= this.length) {
                throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
            }
            this.ch = this.chars[this.offset];
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                ++this.offset;
                if (this.offset >= this.length) {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                this.ch = this.chars[this.offset];
            }
            ++this.offset;
        }
        else if (!this.comma && this.ch != '}' && this.ch != ']' && this.ch != '\u001a') {
            throw new JSONValidException(this.info("illegal ch " + this.ch));
        }
    }
    
    @Override
    public final void skipLineComment() {
        while (this.ch != '\n') {
            ++this.offset;
            if (this.offset >= this.end) {
                this.ch = '\u001a';
                return;
            }
            this.ch = this.chars[this.offset];
        }
        ++this.offset;
        if (this.offset >= this.end) {
            this.ch = '\u001a';
            return;
        }
        this.ch = this.chars[this.offset];
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            ++this.offset;
            if (this.offset >= this.end) {
                this.ch = '\u001a';
                return;
            }
            this.ch = this.chars[this.offset];
        }
        ++this.offset;
    }
    
    public final void readNumber0() {
        this.wasNull = false;
        this.mag0 = 0;
        this.mag1 = 0;
        this.mag2 = 0;
        this.mag3 = 0;
        this.negative = false;
        this.exponent = 0;
        this.scale = 0;
        final int firstOffset = this.offset;
        char quote = '\0';
        if (this.ch == '\"' || this.ch == '\'') {
            quote = this.ch;
            this.ch = this.chars[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
                this.nextIfComma();
                this.wasNull = true;
                return;
            }
        }
        final int start = this.offset;
        int multmin;
        if (this.ch == '-') {
            final int limit = Integer.MIN_VALUE;
            multmin = -214748364;
            this.negative = true;
            this.ch = this.chars[this.offset++];
        }
        else {
            if (this.ch == '+') {
                this.ch = this.chars[this.offset++];
            }
            final int limit = -2147483647;
            multmin = -214748364;
        }
        boolean intOverflow = false;
        this.valueType = 1;
        while (this.ch >= '0' && this.ch <= '9') {
            if (!intOverflow) {
                final int digit = this.ch - '0';
                this.mag3 *= 10;
                if (this.mag3 < multmin) {
                    intOverflow = true;
                }
                else {
                    this.mag3 -= digit;
                    if (this.mag3 < multmin) {
                        intOverflow = true;
                    }
                }
            }
            if (this.offset == this.end) {
                this.ch = '\u001a';
                ++this.offset;
                break;
            }
            this.ch = this.chars[this.offset++];
        }
        if (this.ch == '.') {
            this.valueType = 2;
            this.ch = this.chars[this.offset++];
            while (this.ch >= '0' && this.ch <= '9') {
                if (!intOverflow) {
                    final int digit = this.ch - '0';
                    this.mag3 *= 10;
                    if (this.mag3 < multmin) {
                        intOverflow = true;
                    }
                    else {
                        this.mag3 -= digit;
                        if (this.mag3 < multmin) {
                            intOverflow = true;
                        }
                    }
                }
                ++this.scale;
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                    ++this.offset;
                    break;
                }
                this.ch = this.chars[this.offset++];
            }
        }
        if (intOverflow) {
            final int numStart = this.negative ? start : (start - 1);
            final int numDigits = (this.scale > 0) ? (this.offset - 2 - numStart) : (this.offset - 1 - numStart);
            if (numDigits > 38) {
                this.valueType = 8;
                this.stringValue = new String(this.chars, numStart, this.offset - 1 - numStart);
            }
            else {
                this.bigInt(this.chars, numStart, this.offset - 1);
            }
        }
        else {
            this.mag3 = -this.mag3;
        }
        if (this.ch == 'e' || this.ch == 'E') {
            boolean negativeExp = false;
            int expValue = 0;
            this.ch = this.chars[this.offset++];
            if (this.ch == '-') {
                negativeExp = true;
                this.ch = this.chars[this.offset++];
            }
            else if (this.ch == '+') {
                this.ch = this.chars[this.offset++];
            }
            while (this.ch >= '0' && this.ch <= '9') {
                final int byteVal = this.ch - '0';
                expValue = expValue * 10 + byteVal;
                if (expValue > 1023) {
                    throw new JSONException("too large exp value : " + expValue);
                }
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                    break;
                }
                this.ch = this.chars[this.offset++];
            }
            if (negativeExp) {
                expValue = -expValue;
            }
            this.exponent = (short)expValue;
            this.valueType = 2;
        }
        if (this.offset == start) {
            if (this.ch == 'n') {
                if (this.chars[this.offset++] == 'u' && this.chars[this.offset++] == 'l' && this.chars[this.offset++] == 'l') {
                    this.wasNull = true;
                    this.valueType = 5;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = this.chars[this.offset++];
                    }
                }
            }
            else if (this.ch == 't') {
                if (this.chars[this.offset++] == 'r' && this.chars[this.offset++] == 'u' && this.chars[this.offset++] == 'e') {
                    this.boolValue = true;
                    this.valueType = 4;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = this.chars[this.offset++];
                    }
                }
            }
            else if (this.ch == 'f') {
                if (this.chars[this.offset++] == 'a' && this.chars[this.offset++] == 'l' && this.chars[this.offset++] == 's' && this.chars[this.offset++] == 'e') {
                    this.boolValue = false;
                    this.valueType = 4;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = this.chars[this.offset++];
                    }
                }
            }
            else {
                if (this.ch == '{' && quote == '\0') {
                    this.complex = this.readObject();
                    this.valueType = 6;
                    return;
                }
                if (this.ch == '[' && quote == '\0') {
                    this.complex = this.readArray();
                    this.valueType = 7;
                    return;
                }
            }
        }
        if (quote != '\0') {
            if (this.ch != quote) {
                this.offset = firstOffset;
                this.ch = quote;
                this.readString0();
                this.valueType = 3;
                return;
            }
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
            }
        }
        if (this.ch == 'L' || this.ch == 'F' || this.ch == 'D' || this.ch == 'B' || this.ch == 'S') {
            switch (this.ch) {
                case 'B': {
                    this.valueType = 9;
                    break;
                }
                case 'S': {
                    this.valueType = 10;
                    break;
                }
                case 'L': {
                    this.valueType = 11;
                    break;
                }
                case 'F': {
                    this.valueType = 12;
                    break;
                }
                case 'D': {
                    this.valueType = 13;
                    break;
                }
            }
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
            }
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = this.chars[this.offset++];
                    }
                }
            }
        }
    }
    
    @Override
    public final boolean readIfNull() {
        if (this.ch == 'n' && this.chars[this.offset] == 'u' && this.chars[this.offset + 1] == 'l' && this.chars[this.offset + 2] == 'l') {
            if (this.offset + 3 == this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset + 3];
            }
            this.offset += 4;
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
            }
            if (this.comma = (this.ch == ',')) {
                this.ch = ((this.offset == this.end) ? '\u001a' : this.chars[this.offset++]);
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = this.chars[this.offset++];
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public final Date readNullOrNewDate() {
        Date date = null;
        if (this.offset + 2 < this.end && this.chars[this.offset] == 'u' && this.chars[this.offset + 1] == 'l' && this.chars[this.offset + 2] == 'l') {
            if (this.offset + 3 == this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset + 3];
            }
            this.offset += 4;
        }
        else {
            if (this.offset + 1 >= this.end || this.chars[this.offset] != 'e' || this.chars[this.offset + 1] != 'w') {
                throw new JSONException("json syntax error, not match null or new Date" + this.offset);
            }
            if (this.offset + 3 == this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset + 2];
            }
            this.offset += 3;
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
            }
            if (this.offset + 4 >= this.end || this.ch != 'D' || this.chars[this.offset] != 'a' || this.chars[this.offset + 1] != 't' || this.chars[this.offset + 2] != 'e') {
                throw new JSONException("json syntax error, not match new Date" + this.offset);
            }
            if (this.offset + 3 == this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset + 3];
            }
            this.offset += 4;
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
            }
            if (this.ch != '(' || this.offset >= this.end) {
                throw new JSONException("json syntax error, not match new Date" + this.offset);
            }
            this.ch = this.chars[this.offset++];
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
            }
            final long millis = this.readInt64Value();
            if (this.ch != ')') {
                throw new JSONException("json syntax error, not match new Date" + this.offset);
            }
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
            }
            date = new Date(millis);
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = this.chars[this.offset++];
            }
        }
        if (this.comma = (this.ch == ',')) {
            this.ch = ((this.offset == this.end) ? '\u001a' : this.chars[this.offset++]);
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
            }
        }
        return date;
    }
    
    @Override
    public final boolean isNull() {
        return this.ch == 'n' && this.offset < this.end && this.chars[this.offset] == 'u';
    }
    
    @Override
    public final boolean nextIfNull() {
        if (this.ch == 'n' && this.offset + 2 < this.end && this.chars[this.offset] == 'u') {
            this.readNull();
            return true;
        }
        return false;
    }
    
    @Override
    public final void readNull() {
        final char[] chars = this.chars;
        int offset = this.offset;
        char ch = this.ch;
        if (chars[offset] == 'u' && chars[offset + 1] == 'l' && chars[offset + 2] == 'l') {
            if (offset + 3 == this.end) {
                ch = '\u001a';
            }
            else {
                ch = chars[offset + 3];
            }
            offset += 4;
            while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
                if (offset >= this.end) {
                    ch = '\u001a';
                }
                else {
                    ch = chars[offset++];
                }
            }
            if (this.comma = (ch == ',')) {
                if (offset >= this.end) {
                    ch = '\u001a';
                }
                else {
                    ch = chars[offset++];
                }
                while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
                    if (offset >= this.end) {
                        ch = '\u001a';
                    }
                    else {
                        ch = chars[offset++];
                    }
                }
            }
            this.ch = ch;
            this.offset = offset;
            return;
        }
        throw new JSONException("json syntax error, not match null, offset " + offset);
    }
    
    @Override
    public final BigDecimal readBigDecimal() {
        final char[] chars = this.chars;
        boolean value = false;
        BigDecimal decimal = null;
        char quote = '\0';
        if (this.ch == '\"' || this.ch == '\'') {
            quote = this.ch;
            this.ch = chars[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = chars[this.offset++];
                }
                this.nextIfComma();
                return null;
            }
        }
        final int start = this.offset;
        if (this.ch == '-') {
            this.negative = true;
            this.ch = chars[this.offset++];
        }
        else {
            this.negative = false;
            if (this.ch == '+') {
                this.ch = chars[this.offset++];
            }
        }
        this.valueType = 1;
        boolean overflow = false;
        long longValue = 0L;
        while (this.ch >= '0' && this.ch <= '9') {
            if (!overflow) {
                final long r = longValue * 10L;
                if ((longValue | 0xAL) >>> 31 == 0L || r / 10L == longValue) {
                    longValue = r + (this.ch - '0');
                }
                else {
                    overflow = true;
                }
            }
            if (this.offset == this.end) {
                this.ch = '\u001a';
                ++this.offset;
                break;
            }
            this.ch = chars[this.offset++];
        }
        this.scale = 0;
        if (this.ch == '.') {
            this.valueType = 2;
            this.ch = chars[this.offset++];
            while (this.ch >= '0' && this.ch <= '9') {
                ++this.scale;
                if (!overflow) {
                    final long r = longValue * 10L;
                    if ((longValue | 0xAL) >>> 31 == 0L || r / 10L == longValue) {
                        longValue = r + (this.ch - '0');
                    }
                    else {
                        overflow = true;
                    }
                }
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                    ++this.offset;
                    break;
                }
                this.ch = chars[this.offset++];
            }
        }
        int expValue = 0;
        if (this.ch == 'e' || this.ch == 'E') {
            boolean negativeExp = false;
            this.ch = chars[this.offset++];
            if (this.ch == '-') {
                negativeExp = true;
                this.ch = chars[this.offset++];
            }
            else if (this.ch == '+') {
                this.ch = chars[this.offset++];
            }
            while (this.ch >= '0' && this.ch <= '9') {
                final int byteVal = this.ch - '0';
                expValue = expValue * 10 + byteVal;
                if (expValue > 1023) {
                    throw new JSONException("too large exp value : " + expValue);
                }
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                    ++this.offset;
                    break;
                }
                this.ch = chars[this.offset++];
            }
            if (negativeExp) {
                expValue = -expValue;
            }
            this.exponent = (short)expValue;
            this.valueType = 2;
        }
        if (this.offset == start) {
            if (this.ch == 'n') {
                if (chars[this.offset++] == 'u' && chars[this.offset++] == 'l' && chars[this.offset++] == 'l') {
                    if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) != 0x0L) {
                        throw new JSONException(this.info("long value not support input null"));
                    }
                    this.wasNull = true;
                    value = true;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = chars[this.offset++];
                    }
                }
            }
            else if (this.ch == 't') {
                if (chars[this.offset++] == 'r' && chars[this.offset++] == 'u' && chars[this.offset++] == 'e') {
                    value = true;
                    decimal = BigDecimal.ONE;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = chars[this.offset++];
                    }
                }
            }
            else if (this.ch == 'f') {
                if (chars[this.offset++] == 'a' && chars[this.offset++] == 'l' && chars[this.offset++] == 's' && chars[this.offset++] == 'e') {
                    decimal = BigDecimal.ZERO;
                    value = true;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = chars[this.offset++];
                    }
                }
            }
            else if (this.ch == '{' && quote == '\0') {
                final JSONObject jsonObject = new JSONObject();
                this.readObject(jsonObject, 0L);
                decimal = this.decimal(jsonObject);
                value = true;
                this.wasNull = true;
            }
            else if (this.ch == '[' && quote == '\0') {
                final List array = this.readArray();
                if (!array.isEmpty()) {
                    throw new JSONException(this.info());
                }
                value = true;
                this.wasNull = true;
            }
        }
        final int len = this.offset - start;
        if (quote != '\0') {
            if (this.ch != quote) {
                --this.offset;
                this.ch = quote;
                final String str = this.readString();
                try {
                    return TypeUtils.toBigDecimal(str);
                }
                catch (NumberFormatException e) {
                    throw new JSONException(this.info(e.getMessage()), e);
                }
            }
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = chars[this.offset++];
            }
        }
        if (!value) {
            if (expValue == 0 && !overflow && longValue != 0L) {
                decimal = BigDecimal.valueOf(this.negative ? (-longValue) : longValue, this.scale);
                value = true;
            }
            if (!value) {
                decimal = TypeUtils.parseBigDecimal(chars, start - 1, len);
            }
            if (this.ch == 'L' || this.ch == 'F' || this.ch == 'D' || this.ch == 'B' || this.ch == 'S') {
                switch (this.ch) {
                    case 'B': {
                        this.valueType = 9;
                        break;
                    }
                    case 'S': {
                        this.valueType = 10;
                        break;
                    }
                    case 'L': {
                        this.valueType = 11;
                        break;
                    }
                    case 'F': {
                        this.valueType = 12;
                        break;
                    }
                    case 'D': {
                        this.valueType = 13;
                        break;
                    }
                }
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = chars[this.offset++];
                }
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = chars[this.offset++];
            }
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = chars[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = chars[this.offset++];
                    }
                }
            }
        }
        return decimal;
    }
    
    @Override
    public final UUID readUUID() {
        char ch = this.ch;
        if (ch == 'n') {
            this.readNull();
            return null;
        }
        if (ch != '\"' && ch != '\'') {
            throw new JSONException(this.info("syntax error, can not read uuid"));
        }
        final char quote = ch;
        final char[] chars = this.chars;
        int offset = this.offset;
        if (offset + 36 < chars.length && chars[offset + 36] == quote) {
            final char ch2 = chars[offset + 8];
            final char ch3 = chars[offset + 13];
            final char ch4 = chars[offset + 18];
            final char ch5 = chars[offset + 23];
            if (ch2 == '-' && ch3 == '-' && ch4 == '-' && ch5 == '-') {
                long hi = 0L;
                for (int i = 0; i < 8; ++i) {
                    hi = (hi << 4) + JSONFactory.UUID_VALUES[chars[offset + i] - '0'];
                }
                for (int i = 9; i < 13; ++i) {
                    hi = (hi << 4) + JSONFactory.UUID_VALUES[chars[offset + i] - '0'];
                }
                for (int i = 14; i < 18; ++i) {
                    hi = (hi << 4) + JSONFactory.UUID_VALUES[chars[offset + i] - '0'];
                }
                long lo = 0L;
                for (int j = 19; j < 23; ++j) {
                    lo = (lo << 4) + JSONFactory.UUID_VALUES[chars[offset + j] - '0'];
                }
                for (int j = 24; j < 36; ++j) {
                    lo = (lo << 4) + JSONFactory.UUID_VALUES[chars[offset + j] - '0'];
                }
                final UUID uuid = new UUID(hi, lo);
                offset += 37;
                if (offset == this.end) {
                    ch = '\u001a';
                }
                else {
                    ch = chars[offset++];
                }
                while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
                    if (offset >= this.end) {
                        ch = '\u001a';
                    }
                    else {
                        ch = chars[offset++];
                    }
                }
                this.offset = offset;
                final boolean comma = ch == ',';
                this.comma = comma;
                if (comma) {
                    this.next();
                }
                else {
                    this.ch = ch;
                }
                return uuid;
            }
        }
        else if (offset + 32 < chars.length && chars[offset + 32] == quote) {
            long hi2 = 0L;
            for (int k = 0; k < 16; ++k) {
                hi2 = (hi2 << 4) + JSONFactory.UUID_VALUES[chars[offset + k] - '0'];
            }
            long lo2 = 0L;
            for (int l = 16; l < 32; ++l) {
                lo2 = (lo2 << 4) + JSONFactory.UUID_VALUES[chars[offset + l] - '0'];
            }
            final UUID uuid2 = new UUID(hi2, lo2);
            offset += 33;
            if (offset == this.end) {
                ch = '\u001a';
            }
            else {
                ch = chars[offset++];
            }
            while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
                if (offset >= this.end) {
                    ch = '\u001a';
                }
                else {
                    ch = chars[offset++];
                }
            }
            this.offset = offset;
            final boolean comma2 = ch == ',';
            this.comma = comma2;
            if (comma2) {
                this.next();
            }
            else {
                this.ch = ch;
            }
            return uuid2;
        }
        final String str = this.readString();
        return UUID.fromString(str);
    }
    
    public final int getStringLength() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("string length only support string input");
        }
        final char quote = this.ch;
        int len = 0;
        int i = this.offset;
        final char[] chars = this.chars;
        final int i2 = i + 8;
        if (i2 < this.end && i2 < chars.length && chars[i] != quote && chars[i + 1] != quote && chars[i + 2] != quote && chars[i + 3] != quote && chars[i + 4] != quote && chars[i + 5] != quote && chars[i + 6] != quote && chars[i + 7] != quote) {
            i += 8;
            len += 8;
        }
        while (i < this.end && chars[i] != quote) {
            ++i;
            ++len;
        }
        return len;
    }
    
    @Override
    protected final LocalDateTime readLocalDateTime14() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime14(this.chars, this.offset);
        if (ldt == null) {
            return null;
        }
        this.offset += 15;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return ldt;
    }
    
    @Override
    protected final LocalDateTime readLocalDateTime12() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime12(this.chars, this.offset);
        if (ldt == null) {
            return null;
        }
        this.offset += 13;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return ldt;
    }
    
    @Override
    protected final LocalDateTime readLocalDateTime16() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime16(this.chars, this.offset);
        if (ldt == null) {
            return null;
        }
        this.offset += 17;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return ldt;
    }
    
    @Override
    protected final LocalDateTime readLocalDateTime17() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime17(this.chars, this.offset);
        if (ldt == null) {
            return null;
        }
        this.offset += 18;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return ldt;
    }
    
    @Override
    protected final LocalDateTime readLocalDateTime18() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime18(this.chars, this.offset);
        this.offset += 19;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return ldt;
    }
    
    @Override
    protected final LocalTime readLocalTime5() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        final LocalTime time = DateUtils.parseLocalTime5(this.chars, this.offset);
        if (time == null) {
            return null;
        }
        this.offset += 6;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return time;
    }
    
    @Override
    protected final LocalTime readLocalTime8() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        final LocalTime time = DateUtils.parseLocalTime8(this.chars, this.offset);
        if (time == null) {
            return null;
        }
        this.offset += 9;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return time;
    }
    
    @Override
    protected final LocalTime readLocalTime9() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        final LocalTime time = DateUtils.parseLocalTime8(this.chars, this.offset);
        if (time == null) {
            return null;
        }
        this.offset += 10;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return time;
    }
    
    public final LocalDate readLocalDate8() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localDate only support string input");
        }
        LocalDate ldt;
        try {
            ldt = DateUtils.parseLocalDate8(this.chars, this.offset);
        }
        catch (DateTimeException ex) {
            throw new JSONException(this.info("read date error"), ex);
        }
        this.offset += 9;
        this.next();
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            this.next();
        }
        return ldt;
    }
    
    public final LocalDate readLocalDate9() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localDate only support string input");
        }
        LocalDate ldt;
        try {
            ldt = DateUtils.parseLocalDate9(this.chars, this.offset);
        }
        catch (DateTimeException ex) {
            throw new JSONException(this.info("read date error"), ex);
        }
        this.offset += 10;
        this.next();
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            this.next();
        }
        return ldt;
    }
    
    public final LocalDate readLocalDate10() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localDate only support string input");
        }
        LocalDate ldt;
        try {
            ldt = DateUtils.parseLocalDate10(this.chars, this.offset);
        }
        catch (DateTimeException ex) {
            throw new JSONException(this.info("read date error"), ex);
        }
        if (ldt == null) {
            return null;
        }
        this.offset += 11;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return ldt;
    }
    
    public final LocalDate readLocalDate11() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localDate only support string input");
        }
        final LocalDate ldt = DateUtils.parseLocalDate11(this.chars, this.offset);
        if (ldt == null) {
            return null;
        }
        this.offset += 12;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return ldt;
    }
    
    @Override
    public final LocalDate readLocalDate() {
        final char[] chars = this.chars;
        if ((this.ch == '\"' || this.ch == '\'') && (this.context.dateFormat == null || this.context.formatyyyyMMddhhmmss19 || this.context.formatyyyyMMddhhmmssT19 || this.context.formatyyyyMMdd8 || this.context.formatISO8601)) {
            final char quote = this.ch;
            final int c10 = this.offset + 10;
            if (c10 < chars.length && c10 < this.end && chars[this.offset + 4] == '-' && chars[this.offset + 7] == '-' && chars[this.offset + 10] == quote) {
                final char y0 = chars[this.offset];
                final char y2 = chars[this.offset + 1];
                final char y3 = chars[this.offset + 2];
                final char y4 = chars[this.offset + 3];
                final char m0 = chars[this.offset + 5];
                final char m2 = chars[this.offset + 6];
                final char d0 = chars[this.offset + 8];
                final char d2 = chars[this.offset + 9];
                if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
                    return super.readLocalDate();
                }
                final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
                if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
                    return super.readLocalDate();
                }
                final int month = (m0 - '0') * 10 + (m2 - '0');
                if (d0 >= '0' && d0 <= '9' && d2 >= '0' && d2 <= '9') {
                    final int dom = (d0 - '0') * 10 + (d2 - '0');
                    LocalDate ldt;
                    try {
                        if (year == 0 && month == 0 && dom == 0) {
                            ldt = null;
                        }
                        else {
                            ldt = LocalDate.of(year, month, dom);
                        }
                    }
                    catch (DateTimeException ex) {
                        throw new JSONException(this.info("read date error"), ex);
                    }
                    this.offset += 11;
                    this.next();
                    final boolean comma = this.ch == ',';
                    this.comma = comma;
                    if (comma) {
                        this.next();
                    }
                    return ldt;
                }
                return super.readLocalDate();
            }
        }
        return super.readLocalDate();
    }
    
    @Override
    public final OffsetDateTime readOffsetDateTime() {
        final char[] chars = this.chars;
        final int offset = this.offset;
        final Context context = this.context;
        if ((this.ch == '\"' || this.ch == '\'') && (context.dateFormat == null || context.formatyyyyMMddhhmmss19 || context.formatyyyyMMddhhmmssT19 || context.formatyyyyMMdd8 || context.formatISO8601)) {
            final char quote = this.ch;
            final int off21 = offset + 19;
            final char c10;
            if (off21 < chars.length && off21 < this.end && chars[offset + 4] == '-' && chars[offset + 7] == '-' && ((c10 = chars[offset + 10]) == ' ' || c10 == 'T') && chars[offset + 13] == ':' && chars[offset + 16] == ':') {
                final char y0 = chars[offset];
                final char y2 = chars[offset + 1];
                final char y3 = chars[offset + 2];
                final char y4 = chars[offset + 3];
                final char m0 = chars[offset + 5];
                final char m2 = chars[offset + 6];
                final char d0 = chars[offset + 8];
                final char d2 = chars[offset + 9];
                final char h0 = chars[offset + 11];
                final char h2 = chars[offset + 12];
                final char i0 = chars[offset + 14];
                final char i2 = chars[offset + 15];
                final char s0 = chars[offset + 17];
                final char s2 = chars[offset + 18];
                if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
                    return this.readZonedDateTime().toOffsetDateTime();
                }
                final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
                if (m0 < '0' || m0 > '9' || m2 < '0' || m2 > '9') {
                    return this.readZonedDateTime().toOffsetDateTime();
                }
                final int month = (m0 - '0') * 10 + (m2 - '0');
                if (d0 < '0' || d0 > '9' || d2 < '0' || d2 > '9') {
                    return this.readZonedDateTime().toOffsetDateTime();
                }
                final int dom = (d0 - '0') * 10 + (d2 - '0');
                if (h0 < '0' || h0 > '9' || h2 < '0' || h2 > '9') {
                    return this.readZonedDateTime().toOffsetDateTime();
                }
                final int hour = (h0 - '0') * 10 + (h2 - '0');
                if (i0 < '0' || i0 > '9' || i2 < '0' || i2 > '9') {
                    return this.readZonedDateTime().toOffsetDateTime();
                }
                final int minute = (i0 - '0') * 10 + (i2 - '0');
                if (s0 < '0' || s0 > '9' || s2 < '0' || s2 > '9') {
                    return this.readZonedDateTime().toOffsetDateTime();
                }
                final int second = (s0 - '0') * 10 + (s2 - '0');
                LocalDate localDate;
                try {
                    if (year == 0 && month == 0 && dom == 0) {
                        localDate = null;
                    }
                    else {
                        localDate = LocalDate.of(year, month, dom);
                    }
                }
                catch (DateTimeException ex) {
                    throw new JSONException(this.info("read date error"), ex);
                }
                int nanoSize = -1;
                int len = 0;
                int j;
                for (int start = j = offset + 19, end = offset + 31; j < end && j < this.end && j < chars.length; ++j) {
                    if (chars[j] == quote && chars[j - 1] == 'Z') {
                        nanoSize = j - start - 2;
                        len = j - offset + 1;
                        break;
                    }
                }
                if (nanoSize != -1 || len == 21) {
                    final int nano = (nanoSize <= 0) ? 0 : DateUtils.readNanos(chars, nanoSize, offset + 20);
                    final LocalTime localTime = LocalTime.of(hour, minute, second, nano);
                    final LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
                    final OffsetDateTime oft = OffsetDateTime.of(ldt, ZoneOffset.UTC);
                    this.offset += len;
                    this.next();
                    if (this.comma = (this.ch == ',')) {
                        this.next();
                    }
                    return oft;
                }
            }
        }
        return this.readZonedDateTime().toOffsetDateTime();
    }
    
    @Override
    protected final ZonedDateTime readZonedDateTimeX(final int len) {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        if (len < 19) {
            return null;
        }
        ZonedDateTime zdt;
        if (len == 30 && this.chars[this.offset + 29] == 'Z') {
            final LocalDateTime ldt = DateUtils.parseLocalDateTime29(this.chars, this.offset);
            zdt = ZonedDateTime.of(ldt, ZoneOffset.UTC);
        }
        else if (len == 29 && this.chars[this.offset + 28] == 'Z') {
            final LocalDateTime ldt = DateUtils.parseLocalDateTime28(this.chars, this.offset);
            zdt = ZonedDateTime.of(ldt, ZoneOffset.UTC);
        }
        else if (len == 28 && this.chars[this.offset + 27] == 'Z') {
            final LocalDateTime ldt = DateUtils.parseLocalDateTime27(this.chars, this.offset);
            zdt = ZonedDateTime.of(ldt, ZoneOffset.UTC);
        }
        else if (len == 27 && this.chars[this.offset + 26] == 'Z') {
            final LocalDateTime ldt = DateUtils.parseLocalDateTime26(this.chars, this.offset);
            zdt = ZonedDateTime.of(ldt, ZoneOffset.UTC);
        }
        else {
            zdt = DateUtils.parseZonedDateTime(this.chars, this.offset, len, this.context.zoneId);
        }
        if (zdt == null) {
            return null;
        }
        this.offset += len + 1;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return zdt;
    }
    
    @Override
    protected final LocalDateTime readLocalDateTime19() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime19(this.chars, this.offset);
        if (ldt == null) {
            return null;
        }
        this.offset += 20;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return ldt;
    }
    
    @Override
    protected final LocalDateTime readLocalDateTime20() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime20(this.chars, this.offset);
        if (ldt == null) {
            return null;
        }
        this.offset += 21;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return ldt;
    }
    
    @Override
    public final long readMillis19() {
        final char quote = this.ch;
        if (quote != '\"' && quote != '\'') {
            throw new JSONException("date only support string input");
        }
        if (this.offset + 18 >= this.end) {
            this.wasNull = true;
            return 0L;
        }
        final long millis = DateUtils.parseMillis19(this.chars, this.offset, this.context.zoneId);
        if (this.chars[this.offset + 19] != quote) {
            throw new JSONException(this.info("illegal date input"));
        }
        this.offset += 20;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return millis;
    }
    
    @Override
    protected final LocalDateTime readLocalDateTimeX(final int len) {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("date only support string input");
        }
        LocalDateTime ldt;
        if (this.chars[this.offset + len - 1] == 'Z') {
            final ZonedDateTime zdt = DateUtils.parseZonedDateTime(this.chars, this.offset, len);
            ldt = zdt.toLocalDateTime();
        }
        else {
            ldt = DateUtils.parseLocalDateTimeX(this.chars, this.offset, len);
        }
        if (ldt == null) {
            return null;
        }
        this.offset += len + 1;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return ldt;
    }
    
    @Override
    protected final LocalTime readLocalTime10() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        final LocalTime time = DateUtils.parseLocalTime10(this.chars, this.offset);
        if (time == null) {
            return null;
        }
        this.offset += 11;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return time;
    }
    
    @Override
    protected final LocalTime readLocalTime11() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        final LocalTime time = DateUtils.parseLocalTime11(this.chars, this.offset);
        if (time == null) {
            return null;
        }
        this.offset += 12;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return time;
    }
    
    @Override
    protected final LocalTime readLocalTime12() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        final LocalTime time = DateUtils.parseLocalTime12(this.chars, this.offset);
        if (time == null) {
            return null;
        }
        this.offset += 13;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return time;
    }
    
    @Override
    protected final LocalTime readLocalTime18() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        final LocalTime time = DateUtils.parseLocalTime18(this.chars, this.offset);
        if (time == null) {
            return null;
        }
        this.offset += 19;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return time;
    }
    
    @Override
    public final String readPattern() {
        if (this.ch != '/') {
            throw new JSONException("illegal pattern");
        }
        int offset = this.offset;
        do {
            final char c = this.chars[offset];
            if (c == '/') {
                break;
            }
        } while (++offset < this.end);
        final String str = new String(this.chars, this.offset, offset - this.offset);
        if (offset + 1 == this.end) {
            this.offset = this.end;
            this.ch = '\u001a';
            return str;
        }
        int b;
        for (b = this.chars[++offset]; b <= 32 && (1L << b & 0x100003700L) != 0x0L; b = this.chars[++offset]) {}
        if (this.comma = (b == 44)) {
            this.offset = offset + 1;
            this.ch = this.chars[this.offset++];
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = this.chars[this.offset++];
                }
            }
        }
        else {
            this.offset = offset + 1;
            this.ch = (char)b;
        }
        return str;
    }
    
    @Override
    public final boolean readBoolValue() {
        this.wasNull = false;
        final char[] chars = this.chars;
        int offset = this.offset;
        char ch = this.ch;
        boolean val;
        if (ch == 't' && offset + 2 < chars.length && chars[offset] == 'r' && chars[offset + 1] == 'u' && chars[offset + 2] == 'e') {
            offset += 3;
            val = true;
        }
        else if (ch == 'f' && offset + 3 < chars.length && chars[offset] == 'a' && chars[offset + 1] == 'l' && chars[offset + 2] == 's' && chars[offset + 3] == 'e') {
            offset += 4;
            val = false;
        }
        else if (ch == '-' || (ch >= '0' && ch <= '9')) {
            this.readNumber();
            if (this.valueType != 1) {
                return false;
            }
            if ((this.context.features & Feature.NonZeroNumberCastToBooleanAsTrue.mask) != 0x0L) {
                return this.mag0 != 0 || this.mag1 != 0 || this.mag2 != 0 || this.mag3 != 0;
            }
            return this.mag0 == 0 && this.mag1 == 0 && this.mag2 == 0 && this.mag3 == 1;
        }
        else if (ch == 'n' && offset + 2 < chars.length && chars[offset] == 'u' && chars[offset + 1] == 'l' && chars[offset + 2] == 'l') {
            if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) != 0x0L) {
                throw new JSONException(this.info("boolean value not support input null"));
            }
            this.wasNull = true;
            offset += 3;
            val = false;
        }
        else {
            if (ch != '\"') {
                throw new JSONException("syntax error : " + ch);
            }
            if (offset + 1 < chars.length && chars[offset + 1] == '\"') {
                final char c0 = chars[offset];
                offset += 2;
                if (c0 == '0' || c0 == 'N') {
                    val = false;
                }
                else {
                    if (c0 != '1' && c0 != 'Y') {
                        throw new JSONException("can not convert to boolean : " + c0);
                    }
                    val = true;
                }
            }
            else {
                final String str = this.readString();
                if ("true".equalsIgnoreCase(str)) {
                    return true;
                }
                if ("false".equalsIgnoreCase(str)) {
                    return false;
                }
                if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                    this.wasNull = true;
                    return false;
                }
                throw new JSONException("can not convert to boolean : " + str);
            }
        }
        ch = ((offset == this.end) ? '\u001a' : chars[offset++]);
        while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
            if (offset >= this.end) {
                ch = '\u001a';
            }
            else {
                ch = chars[offset++];
            }
        }
        if (this.comma = (ch == ',')) {
            ch = ((offset == this.end) ? '\u001a' : chars[offset++]);
            while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
                if (offset >= this.end) {
                    ch = '\u001a';
                }
                else {
                    ch = chars[offset++];
                }
            }
        }
        this.offset = offset;
        this.ch = ch;
        return val;
    }
    
    @Override
    public final String info(final String message) {
        int line = 1;
        int column = 1;
        for (int i = 0; i < this.offset & i < this.end; ++i, ++column) {
            if (this.chars[i] == '\n') {
                column = 1;
                ++line;
            }
        }
        final StringBuilder buf = new StringBuilder();
        if (message != null && !message.isEmpty()) {
            buf.append(message).append(", ");
        }
        buf.append("offset ").append(this.offset).append(", character ").append(this.ch).append(", line ").append(line).append(", column ").append(column).append(", fastjson-version ").append("2.0.39").append((line > 1) ? '\n' : ' ');
        final int MAX_OUTPUT_LENGTH = 65535;
        buf.append(this.chars, this.start, Math.min(this.length, 65535));
        return buf.toString();
    }
    
    @Override
    public final void close() {
        if (this.cacheIndex != -1) {
            final JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[this.cacheIndex];
            JSONFactory.CHARS_UPDATER.lazySet(cacheItem, this.chars);
        }
        if (this.input != null) {
            try {
                this.input.close();
            }
            catch (IOException ex) {}
        }
    }
    
    @Override
    public final int getRawInt() {
        if (this.offset + 3 < this.chars.length) {
            return getInt(this.chars, this.offset - 1);
        }
        return 0;
    }
    
    static int getInt(final char[] chars, final int offset) {
        long int64Val = JDKUtils.UNSAFE.getLong(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (offset << 1));
        if ((int64Val & JSONReaderUTF16.CHAR_MASK) != 0x0L) {
            return 0;
        }
        if (JDKUtils.BIG_ENDIAN) {
            int64Val >>= 8;
        }
        return (int)((int64Val & 0xFFL) | (int64Val & 0xFF0000L) >> 8 | (int64Val & 0xFF00000000L) >> 16 | (int64Val & 0xFF000000000000L) >> 24);
    }
    
    @Override
    public final long getRawLong() {
        if (this.offset + 7 < this.chars.length) {
            return getLong(this.chars, this.offset - 1);
        }
        return 0L;
    }
    
    static long getLong(final char[] chars, final int offset) {
        final long arrayOffset = JDKUtils.ARRAY_CHAR_BASE_OFFSET + (offset << 1);
        long int64Val0 = JDKUtils.UNSAFE.getLong(chars, arrayOffset);
        long int64Val2 = JDKUtils.UNSAFE.getLong(chars, arrayOffset + 8L);
        if (((int64Val0 | int64Val2) & JSONReaderUTF16.CHAR_MASK) != 0x0L) {
            return 0L;
        }
        if (JDKUtils.BIG_ENDIAN) {
            int64Val0 >>= 8;
            int64Val2 >>= 8;
        }
        return (int64Val0 & 0xFFL) | (int64Val0 & 0xFF0000L) >> 8 | (int64Val0 & 0xFF00000000L) >> 16 | (int64Val0 & 0xFF000000000000L) >> 24 | (int64Val2 & 0xFFL) << 32 | (int64Val2 & 0xFF0000L) << 24 | (int64Val2 & 0xFF00000000L) << 16 | (int64Val2 & 0xFF000000000000L) << 8;
    }
    
    @Override
    public final boolean nextIfName8Match0() {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 7;
        if (offset == this.end) {
            this.ch = '\u001a';
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName8Match1() {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 8;
        if (offset >= this.end) {
            return false;
        }
        if (chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName8Match2() {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 9;
        if (offset >= this.end) {
            return false;
        }
        if (chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match2() {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 4;
        if (offset >= this.end) {
            return false;
        }
        if (chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match3() {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 5;
        if (offset >= this.end) {
            return false;
        }
        if (chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match4(final byte c4) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 6;
        if (offset >= this.end) {
            return false;
        }
        if (chars[offset - 3] != c4 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c5;
        for (c5 = chars[offset]; c5 <= ' ' && (1L << c5 & 0x100003700L) != 0x0L; c5 = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c5;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match5(final int name1) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 7;
        if (offset >= this.end) {
            return false;
        }
        if (getInt(chars, offset - 4) != name1) {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match6(final int name1) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 8;
        if (offset >= this.end) {
            return false;
        }
        if (getInt(chars, offset - 5) != name1 || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match7(final int name1) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 9;
        if (offset >= this.end) {
            return false;
        }
        if (getInt(chars, offset - 6) != name1 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match8(final int name1, final byte c8) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 10;
        if (offset >= this.end) {
            return false;
        }
        if (getInt(chars, offset - 7) != name1 || chars[offset - 3] != c8 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c9;
        for (c9 = chars[offset]; c9 <= ' ' && (1L << c9 & 0x100003700L) != 0x0L; c9 = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c9;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match9(final long name1) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 11;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 8) != name1) {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match10(final long name1) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 12;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 9) != name1 || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match11(final long name1) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 13;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 10) != name1 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match12(final long name1, final byte c12) {
        final char[] bytes = this.chars;
        int offset = this.offset;
        offset += 14;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(bytes, offset - 11) != name1 || bytes[offset - 3] != c12 || bytes[offset - 2] != '\"' || bytes[offset - 1] != ':') {
            return false;
        }
        char c13;
        for (c13 = bytes[offset]; c13 <= ' ' && (1L << c13 & 0x100003700L) != 0x0L; c13 = bytes[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c13;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match13(final long name1, final int name2) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 15;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 12) != name1 || getInt(chars, offset - 4) != name2) {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match14(final long name1, final int name2) {
        final char[] bytes = this.chars;
        int offset = this.offset;
        offset += 16;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(bytes, offset - 13) != name1 || getInt(bytes, offset - 5) != name2 || bytes[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = bytes[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = bytes[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match15(final long name1, final int name2) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 17;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 14) != name1 || getInt(chars, offset - 6) != name2 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match16(final long name1, final int name2, final byte c16) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 18;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 15) != name1 || getInt(chars, offset - 7) != name2 || chars[offset - 3] != c16 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c17;
        for (c17 = chars[offset]; c17 <= ' ' && (1L << c17 & 0x100003700L) != 0x0L; c17 = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c17;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match17(final long name1, final long name2) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 19;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 16) != name1 || getLong(chars, offset - 8) != name2) {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match18(final long name1, final long name2) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 20;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 17) != name1 || getLong(chars, offset - 9) != name2 || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match19(final long name1, final long name2) {
        final char[] bytes = this.chars;
        int offset = this.offset;
        offset += 21;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(bytes, offset - 18) != name1 || getLong(bytes, offset - 10) != name2 || bytes[offset - 2] != '\"' || bytes[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = bytes[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = bytes[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match20(final long name1, final long name2, final byte c20) {
        final char[] bytes = this.chars;
        int offset = this.offset;
        offset += 22;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(bytes, offset - 19) != name1 || getLong(bytes, offset - 11) != name2 || bytes[offset - 3] != c20 || bytes[offset - 2] != '\"' || bytes[offset - 1] != ':') {
            return false;
        }
        char c21;
        for (c21 = bytes[offset]; c21 <= ' ' && (1L << c21 & 0x100003700L) != 0x0L; c21 = bytes[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c21;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match21(final long name1, final long name2, final int name3) {
        final char[] bytes = this.chars;
        int offset = this.offset;
        offset += 23;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(bytes, offset - 20) != name1 || getLong(bytes, offset - 12) != name2 || getInt(bytes, offset - 4) != name3) {
            return false;
        }
        char c;
        for (c = bytes[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = bytes[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match22(final long name1, final long name2, final int name3) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 24;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 21) != name1 || getLong(chars, offset - 13) != name2 || getInt(chars, offset - 5) != name3 || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match23(final long name1, final long name2, final int name3) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 25;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 22) != name1 || getLong(chars, offset - 14) != name2 || getInt(chars, offset - 6) != name3 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match24(final long name1, final long name2, final int name3, final byte c24) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 26;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 23) != name1 || getLong(chars, offset - 15) != name2 || getInt(chars, offset - 7) != name3 || chars[offset - 3] != c24 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c25;
        for (c25 = chars[offset]; c25 <= ' ' && (1L << c25 & 0x100003700L) != 0x0L; c25 = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c25;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match25(final long name1, final long name2, final long name3) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 27;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 24) != name1 || getLong(chars, offset - 16) != name2 || getLong(chars, offset - 8) != name3) {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match26(final long name1, final long name2, final long name3) {
        final char[] bytes = this.chars;
        int offset = this.offset;
        offset += 28;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(bytes, offset - 25) != name1 || getLong(bytes, offset - 17) != name2 || getLong(bytes, offset - 9) != name3 || bytes[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = bytes[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = bytes[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match27(final long name1, final long name2, final long name3) {
        final char[] bytes = this.chars;
        int offset = this.offset;
        offset += 29;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(bytes, offset - 26) != name1 || getLong(bytes, offset - 18) != name2 || getLong(bytes, offset - 10) != name3 || bytes[offset - 2] != '\"' || bytes[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = bytes[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = bytes[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match28(final long name1, final long name2, final long name3, final byte c29) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 30;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 27) != name1 || getLong(chars, offset - 19) != name2 || getLong(chars, offset - 11) != name3 || chars[offset - 3] != c29 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c30;
        for (c30 = chars[offset]; c30 <= ' ' && (1L << c30 & 0x100003700L) != 0x0L; c30 = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c30;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match29(final long name1, final long name2, final long name3, final int name4) {
        final char[] bytes = this.chars;
        int offset = this.offset;
        offset += 31;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(bytes, offset - 28) != name1 || getLong(bytes, offset - 20) != name2 || getLong(bytes, offset - 12) != name3 || getInt(bytes, offset - 4) != name4) {
            return false;
        }
        char c;
        for (c = bytes[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = bytes[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match30(final long name1, final long name2, final long name3, final int name4) {
        final char[] bytes = this.chars;
        int offset = this.offset;
        offset += 32;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(bytes, offset - 29) != name1 || getLong(bytes, offset - 21) != name2 || getLong(bytes, offset - 13) != name3 || getInt(bytes, offset - 5) != name4 || bytes[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = bytes[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = bytes[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match31(final long name1, final long name2, final long name3, final int name4) {
        final char[] bytes = this.chars;
        int offset = this.offset;
        offset += 33;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(bytes, offset - 30) != name1 || getLong(bytes, offset - 22) != name2 || getLong(bytes, offset - 14) != name3 || getInt(bytes, offset - 6) != name4 || bytes[offset - 2] != '\"' || bytes[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = bytes[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = bytes[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match32(final long name1, final long name2, final long name3, final int name4, final byte c32) {
        final char[] bytes = this.chars;
        int offset = this.offset;
        offset += 34;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(bytes, offset - 31) != name1 || getLong(bytes, offset - 23) != name2 || getLong(bytes, offset - 15) != name3 || getInt(bytes, offset - 7) != name4 || bytes[offset - 3] != c32 || bytes[offset - 2] != '\"' || bytes[offset - 1] != ':') {
            return false;
        }
        char c33;
        for (c33 = bytes[offset]; c33 <= ' ' && (1L << c33 & 0x100003700L) != 0x0L; c33 = bytes[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c33;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match33(final long name1, final long name2, final long name3, final long name4) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 35;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 32) != name1 || getLong(chars, offset - 24) != name2 || getLong(chars, offset - 16) != name3 || getLong(chars, offset - 8) != name4) {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match34(final long name1, final long name2, final long name3, final long name4) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 36;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 33) != name1 || getLong(chars, offset - 25) != name2 || getLong(chars, offset - 17) != name3 || getLong(chars, offset - 9) != name4 || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match35(final long name1, final long name2, final long name3, final long name4) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 37;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 34) != name1 || getLong(chars, offset - 26) != name2 || getLong(chars, offset - 18) != name3 || getLong(chars, offset - 10) != name4 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match36(final long name1, final long name2, final long name3, final long name4, final byte c36) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 38;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 35) != name1 || getLong(chars, offset - 27) != name2 || getLong(chars, offset - 19) != name3 || getLong(chars, offset - 11) != name4 || chars[offset - 3] != c36 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c37;
        for (c37 = chars[offset]; c37 <= ' ' && (1L << c37 & 0x100003700L) != 0x0L; c37 = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c37;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match37(final long name1, final long name2, final long name3, final long name4, final int name5) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 39;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 36) != name1 || getLong(chars, offset - 28) != name2 || getLong(chars, offset - 20) != name3 || getLong(chars, offset - 12) != name4 || getInt(chars, offset - 4) != name5) {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match38(final long name1, final long name2, final long name3, final long name4, final int name5) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 40;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 37) != name1 || getLong(chars, offset - 29) != name2 || getLong(chars, offset - 21) != name3 || getLong(chars, offset - 13) != name4 || getInt(chars, offset - 5) != name5 || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match39(final long name1, final long name2, final long name3, final long name4, final int name5) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 41;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 38) != name1 || getLong(chars, offset - 30) != name2 || getLong(chars, offset - 22) != name3 || getLong(chars, offset - 14) != name4 || getInt(chars, offset - 6) != name5 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match40(final long name1, final long name2, final long name3, final long name4, final int name5, final byte c40) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 42;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 39) != name1 || getLong(chars, offset - 31) != name2 || getLong(chars, offset - 23) != name3 || getLong(chars, offset - 15) != name4 || getInt(chars, offset - 7) != name5 || chars[offset - 3] != c40 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c41;
        for (c41 = chars[offset]; c41 <= ' ' && (1L << c41 & 0x100003700L) != 0x0L; c41 = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c41;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match41(final long name1, final long name2, final long name3, final long name4, final long name5) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 43;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 40) != name1 || getLong(chars, offset - 32) != name2 || getLong(chars, offset - 24) != name3 || getLong(chars, offset - 16) != name4 || getLong(chars, offset - 8) != name5) {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match42(final long name1, final long name2, final long name3, final long name4, final long name5) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 44;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 41) != name1 || getLong(chars, offset - 33) != name2 || getLong(chars, offset - 25) != name3 || getLong(chars, offset - 17) != name4 || getLong(chars, offset - 9) != name5 || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match43(final long name1, final long name2, final long name3, final long name4, final long name5) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 45;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 42) != name1 || getLong(chars, offset - 34) != name2 || getLong(chars, offset - 26) != name3 || getLong(chars, offset - 18) != name4 || getLong(chars, offset - 10) != name5 || chars[offset - 2] != '\"' || chars[offset - 1] != ':') {
            return false;
        }
        char c;
        for (c = chars[offset]; c <= ' ' && (1L << c & 0x100003700L) != 0x0L; c = chars[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match2() {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 3;
        if (offset >= this.end) {
            return false;
        }
        char c = chars[offset];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            c = ((++offset == this.end) ? '\u001a' : chars[offset]);
        }
        while (c <= ' ' && (1L << c & 0x100003700L) != 0x0L) {
            ++offset;
            c = chars[offset];
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match3() {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 4;
        if (offset >= this.end) {
            return false;
        }
        if (chars[offset - 1] != '\"') {
            return false;
        }
        char c = chars[offset];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            c = ((++offset == this.end) ? '\u001a' : chars[offset]);
        }
        while (c <= ' ' && (1L << c & 0x100003700L) != 0x0L) {
            ++offset;
            c = chars[offset];
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match4(final byte c4) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 5;
        if (offset >= this.end) {
            return false;
        }
        if (chars[offset - 2] != c4 || chars[offset - 1] != '\"') {
            return false;
        }
        char c5 = chars[offset];
        if (c5 != ',' && c5 != '}' && c5 != ']') {
            return false;
        }
        if (c5 == ',') {
            this.comma = true;
            c5 = ((++offset == this.end) ? '\u001a' : chars[offset]);
        }
        while (c5 <= ' ' && (1L << c5 & 0x100003700L) != 0x0L) {
            ++offset;
            c5 = chars[offset];
        }
        this.offset = offset + 1;
        this.ch = c5;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match5(final byte c4, final byte c5) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 6;
        if (offset >= this.end) {
            return false;
        }
        if (chars[offset - 3] != c4 || chars[offset - 2] != c5 || chars[offset - 1] != '\"') {
            return false;
        }
        char c6 = chars[offset];
        if (c6 != ',' && c6 != '}' && c6 != ']') {
            return false;
        }
        if (c6 == ',') {
            this.comma = true;
            c6 = ((++offset == this.end) ? '\u001a' : chars[offset]);
        }
        while (c6 <= ' ' && (1L << c6 & 0x100003700L) != 0x0L) {
            ++offset;
            c6 = chars[offset];
        }
        this.offset = offset + 1;
        this.ch = c6;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match6(final int name1) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 7;
        if (offset >= this.end) {
            return false;
        }
        if (getInt(chars, offset - 4) != name1) {
            return false;
        }
        char c = chars[offset];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            c = ((++offset == this.end) ? '\u001a' : chars[offset]);
        }
        while (c <= ' ' && (1L << c & 0x100003700L) != 0x0L) {
            ++offset;
            c = chars[offset];
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match7(final int name1) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 8;
        if (offset >= this.end) {
            return false;
        }
        if (getInt(chars, offset - 5) != name1 || chars[offset - 1] != '\"') {
            return false;
        }
        char c = chars[offset];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            c = ((++offset == this.end) ? '\u001a' : chars[offset]);
        }
        while (c <= ' ' && (1L << c & 0x100003700L) != 0x0L) {
            ++offset;
            c = chars[offset];
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match8(final int name1, final byte c8) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 9;
        if (offset >= this.end) {
            return false;
        }
        if (getInt(chars, offset - 6) != name1 || chars[offset - 2] != c8 || chars[offset - 1] != '\"') {
            return false;
        }
        char c9 = chars[offset];
        if (c9 != ',' && c9 != '}' && c9 != ']') {
            return false;
        }
        if (c9 == ',') {
            this.comma = true;
            c9 = ((++offset == this.end) ? '\u001a' : chars[offset]);
        }
        while (c9 <= ' ' && (1L << c9 & 0x100003700L) != 0x0L) {
            ++offset;
            c9 = chars[offset];
        }
        this.offset = offset + 1;
        this.ch = c9;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match9(final int name1, final byte c8, final byte c9) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 10;
        if (offset >= this.end) {
            return false;
        }
        if (getInt(chars, offset - 7) != name1 || chars[offset - 3] != c8 || chars[offset - 2] != c9 || chars[offset - 1] != '\"') {
            return false;
        }
        char c10 = chars[offset];
        if (c10 != ',' && c10 != '}' && c10 != ']') {
            return false;
        }
        if (c10 == ',') {
            this.comma = true;
            c10 = ((++offset == this.end) ? '\u001a' : chars[offset]);
        }
        while (c10 <= ' ' && (1L << c10 & 0x100003700L) != 0x0L) {
            ++offset;
            c10 = chars[offset];
        }
        this.offset = offset + 1;
        this.ch = c10;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match10(final long name1) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 11;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 8) != name1) {
            return false;
        }
        char c = chars[offset];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            c = ((++offset == this.end) ? '\u001a' : chars[offset]);
        }
        while (c <= ' ' && (1L << c & 0x100003700L) != 0x0L) {
            ++offset;
            c = chars[offset];
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match11(final long name1) {
        final char[] chars = this.chars;
        int offset = this.offset;
        offset += 12;
        if (offset >= this.end) {
            return false;
        }
        if (getLong(chars, offset - 9) != name1 || chars[offset - 1] != '\"') {
            return false;
        }
        char c = chars[offset];
        if (c != ',' && c != '}' && c != ']') {
            return false;
        }
        if (c == ',') {
            this.comma = true;
            c = ((++offset == this.end) ? '\u001a' : chars[offset]);
        }
        while (c <= ' ' && (1L << c & 0x100003700L) != 0x0L) {
            ++offset;
            c = chars[offset];
        }
        this.offset = offset + 1;
        this.ch = c;
        return true;
    }
    
    static {
        CHAR_MASK = (JDKUtils.BIG_ENDIAN ? 71777214294589695L : -71777214294589696L);
    }
}
