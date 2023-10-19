// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.Iterator;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.util.Map;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.LocalDate;
import com.alibaba.fastjson2.util.DoubleToDecimal;
import java.util.Arrays;
import java.util.UUID;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.math.BigInteger;
import sun.misc.Unsafe;
import java.util.List;
import java.io.IOException;
import java.io.Writer;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import java.nio.charset.StandardCharsets;

class JSONWriterUTF16 extends JSONWriter
{
    static final char[] REF_PREF;
    static final int[] HEX256;
    protected char[] chars;
    final JSONFactory.CacheItem cacheItem;
    
    JSONWriterUTF16(final Context ctx) {
        super(ctx, null, false, StandardCharsets.UTF_16);
        final int cacheIndex = System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1;
        this.cacheItem = JSONFactory.CACHE_ITEMS[cacheIndex];
        char[] chars = JSONFactory.CHARS_UPDATER.getAndSet(this.cacheItem, null);
        if (chars == null) {
            chars = new char[8192];
        }
        this.chars = chars;
    }
    
    @Override
    public final void writeNull() {
        final int minCapacity = this.off + 4;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        JDKUtils.UNSAFE.putLong(this.chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (this.off << 1), IOUtils.NULL_64);
        this.off += 4;
    }
    
    @Override
    public final void flushTo(final Writer to) {
        try {
            final int off = this.off;
            if (off > 0) {
                to.write(this.chars, 0, off);
                this.off = 0;
            }
        }
        catch (IOException e) {
            throw new JSONException("flushTo error", e);
        }
    }
    
    @Override
    public final void close() {
        final char[] chars = this.chars;
        if (chars.length > 1048576) {
            return;
        }
        JSONFactory.CHARS_UPDATER.lazySet(this.cacheItem, chars);
    }
    
    @Override
    protected final void write0(final char c) {
        final int off = this.off;
        if (off == this.chars.length) {
            this.ensureCapacity(off + 1);
        }
        this.chars[off] = c;
        this.off = off + 1;
    }
    
    @Override
    public final void writeColon() {
        final int off = this.off;
        if (off == this.chars.length) {
            this.ensureCapacity(off + 1);
        }
        this.chars[off] = ':';
        this.off = off + 1;
    }
    
    @Override
    public final void startObject() {
        if (this.level >= this.context.maxLevel) {
            throw new JSONException("level too large : " + this.level);
        }
        ++this.level;
        this.startObject = true;
        int off = this.off;
        final int minCapacity = off + (this.pretty ? (3 + this.indent) : 1);
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = '{';
        if (this.pretty) {
            ++this.indent;
            chars[off++] = '\n';
            for (int i = 0; i < this.indent; ++i) {
                chars[off++] = '\t';
            }
        }
        this.off = off;
    }
    
    @Override
    public final void endObject() {
        --this.level;
        int off = this.off;
        final int minCapacity = off + (this.pretty ? (2 + this.indent) : 1);
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        if (this.pretty) {
            --this.indent;
            chars[off++] = '\n';
            for (int i = 0; i < this.indent; ++i) {
                chars[off++] = '\t';
            }
        }
        chars[off] = '}';
        this.off = off + 1;
        this.startObject = false;
    }
    
    @Override
    public final void writeComma() {
        this.startObject = false;
        int off = this.off;
        final int minCapacity = off + (this.pretty ? (2 + this.indent) : 1);
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = ',';
        if (this.pretty) {
            chars[off++] = '\n';
            for (int i = 0; i < this.indent; ++i) {
                chars[off++] = '\t';
            }
        }
        this.off = off;
    }
    
    @Override
    public final void startArray() {
        if (this.level >= this.context.maxLevel) {
            throw new JSONException("level too large : " + this.level);
        }
        ++this.level;
        int off = this.off;
        final int minCapacity = off + (this.pretty ? (3 + this.indent) : 1);
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = '[';
        if (this.pretty) {
            ++this.indent;
            chars[off++] = '\n';
            for (int i = 0; i < this.indent; ++i) {
                chars[off++] = '\t';
            }
        }
        this.off = off;
    }
    
    @Override
    public final void endArray() {
        --this.level;
        int off = this.off;
        final int minCapacity = off + (this.pretty ? (2 + this.indent) : 1);
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        if (this.pretty) {
            --this.indent;
            chars[off++] = '\n';
            for (int i = 0; i < this.indent; ++i) {
                chars[off++] = '\t';
            }
        }
        chars[off] = ']';
        this.off = off + 1;
        this.startObject = false;
    }
    
    @Override
    public final void writeString(final List<String> list) {
        if (this.off == this.chars.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.chars[this.off++] = '[';
        for (int i = 0, size = list.size(); i < size; ++i) {
            if (i != 0) {
                if (this.off == this.chars.length) {
                    this.ensureCapacity(this.off + 1);
                }
                this.chars[this.off++] = ',';
            }
            final String str = list.get(i);
            this.writeString(str);
        }
        if (this.off == this.chars.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.chars[this.off++] = ']';
    }
    
    @Override
    public void writeStringLatin1(final byte[] value) {
        if (value == null) {
            this.writeStringNull();
            return;
        }
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        boolean escape = false;
        int off = this.off;
        final int minCapacity = off + value.length + 2;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final int start = off;
        final char[] chars = this.chars;
        chars[off++] = this.quote;
        for (final byte c : value) {
            if (c == 92 || c == this.quote || c < 32) {
                escape = true;
                break;
            }
            if (browserSecure && (c == 60 || c == 62 || c == 40 || c == 41)) {
                escape = true;
                break;
            }
            chars[off++] = (char)c;
        }
        if (!escape) {
            chars[off] = this.quote;
            this.off = off + 1;
            return;
        }
        this.off = start;
        this.writeStringEscape(value);
    }
    
    @Override
    public void writeStringUTF16(final byte[] value) {
        if (value == null) {
            this.writeStringNull();
            return;
        }
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
        boolean escape = false;
        int off = this.off;
        final int minCapacity = off + value.length + 2;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = this.quote;
        for (int i = 0; i < value.length; i += 2) {
            final char c = JDKUtils.UNSAFE.getChar(value, Unsafe.ARRAY_BYTE_BASE_OFFSET + (long)i);
            if (c == '\\' || c == this.quote || c < ' ' || (browserSecure && (c == '<' || c == '>' || c == '(' || c == ')')) || (escapeNoneAscii && c > '\u007f')) {
                escape = true;
                break;
            }
            chars[off++] = c;
        }
        if (!escape) {
            chars[off] = this.quote;
            this.off = off + 1;
            return;
        }
        this.writeStringEscapeUTF16(value);
    }
    
    @Override
    public void writeString(final String str) {
        if (str == null) {
            this.writeStringNull();
            return;
        }
        final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        boolean escape = false;
        final char quote = this.quote;
        final int strlen = str.length();
        final int minCapacity = this.off + strlen + 2;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        for (int i = 0; i < strlen; ++i) {
            final char c = str.charAt(i);
            if (c == '\\' || c == quote || c < ' ' || (browserSecure && (c == '<' || c == '>' || c == '(' || c == ')')) || (escapeNoneAscii && c > '\u007f')) {
                escape = true;
                break;
            }
        }
        if (!escape) {
            int off = this.off;
            final char[] chars = this.chars;
            chars[off++] = quote;
            str.getChars(0, strlen, chars, off);
            off += strlen;
            chars[off] = quote;
            this.off = off + 1;
            return;
        }
        this.writeStringEscape(str);
    }
    
    protected final void writeStringEscape(final String str) {
        final int strlen = str.length();
        final char quote = this.quote;
        final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        int off = this.off;
        this.ensureCapacity(off + strlen * 6 + 2);
        final char[] chars = this.chars;
        chars[off++] = quote;
        for (int i = 0; i < strlen; ++i) {
            final char ch = str.charAt(i);
            switch (ch) {
                case '\"':
                case '\'': {
                    if (ch == quote) {
                        chars[off++] = '\\';
                    }
                    chars[off++] = ch;
                    break;
                }
                case '\\': {
                    chars[off] = '\\';
                    chars[off + 1] = ch;
                    off += 2;
                    break;
                }
                case '\r': {
                    chars[off] = '\\';
                    chars[off + 1] = 'r';
                    off += 2;
                    break;
                }
                case '\n': {
                    chars[off] = '\\';
                    chars[off + 1] = 'n';
                    off += 2;
                    break;
                }
                case '\b': {
                    chars[off] = '\\';
                    chars[off + 1] = 'b';
                    off += 2;
                    break;
                }
                case '\f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'f';
                    off += 2;
                    break;
                }
                case '\t': {
                    chars[off] = '\\';
                    chars[off + 1] = 't';
                    off += 2;
                    break;
                }
                case '\0':
                case '\u0001':
                case '\u0002':
                case '\u0003':
                case '\u0004':
                case '\u0005':
                case '\u0006':
                case '\u0007': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 2] = '0';
                    chars[off + 4] = (chars[off + 3] = '0');
                    chars[off + 5] = (char)('0' + ch);
                    off += 6;
                    break;
                }
                case '\u000b':
                case '\u000e':
                case '\u000f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 2] = '0';
                    chars[off + 4] = (chars[off + 3] = '0');
                    chars[off + 5] = (char)(97 + (ch - '\n'));
                    off += 6;
                    break;
                }
                case '\u0010':
                case '\u0011':
                case '\u0012':
                case '\u0013':
                case '\u0014':
                case '\u0015':
                case '\u0016':
                case '\u0017':
                case '\u0018':
                case '\u0019': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 3] = (chars[off + 2] = '0');
                    chars[off + 4] = '1';
                    chars[off + 5] = (char)(48 + (ch - '\u0010'));
                    off += 6;
                    break;
                }
                case '\u001a':
                case '\u001b':
                case '\u001c':
                case '\u001d':
                case '\u001e':
                case '\u001f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 3] = (chars[off + 2] = '0');
                    chars[off + 4] = '1';
                    chars[off + 5] = (char)(97 + (ch - '\u001a'));
                    off += 6;
                    break;
                }
                case '(':
                case ')':
                case '<':
                case '>': {
                    if (browserSecure) {
                        chars[off] = '\\';
                        chars[off + 1] = 'u';
                        chars[off + 3] = (chars[off + 2] = '0');
                        chars[off + 4] = JSONWriterUTF16.DIGITS[ch >>> 4 & 0xF];
                        chars[off + 5] = JSONWriterUTF16.DIGITS[ch & '\u000f'];
                        off += 6;
                        break;
                    }
                    chars[off++] = ch;
                    break;
                }
                default: {
                    if (escapeNoneAscii && ch > '\u007f') {
                        chars[off] = '\\';
                        chars[off + 1] = 'u';
                        chars[off + 2] = JSONWriterUTF16.DIGITS[ch >>> 12 & 0xF];
                        chars[off + 3] = JSONWriterUTF16.DIGITS[ch >>> 8 & 0xF];
                        chars[off + 4] = JSONWriterUTF16.DIGITS[ch >>> 4 & 0xF];
                        chars[off + 5] = JSONWriterUTF16.DIGITS[ch & '\u000f'];
                        off += 6;
                        break;
                    }
                    chars[off++] = ch;
                    break;
                }
            }
        }
        chars[off] = quote;
        this.off = off + 1;
    }
    
    protected final void writeStringEscapeUTF16(final byte[] str) {
        final int strlen = str.length;
        final char quote = this.quote;
        final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        int off = this.off;
        this.ensureCapacity(off + strlen * 6 + 2);
        final char[] chars = this.chars;
        chars[off++] = quote;
        for (int i = 0; i < strlen; i += 2) {
            final char ch = JDKUtils.UNSAFE.getChar(str, Unsafe.ARRAY_CHAR_BASE_OFFSET + (long)i);
            switch (ch) {
                case '\"':
                case '\'': {
                    if (ch == quote) {
                        chars[off++] = '\\';
                    }
                    chars[off++] = ch;
                    break;
                }
                case '\\': {
                    chars[off] = '\\';
                    chars[off + 1] = ch;
                    off += 2;
                    break;
                }
                case '\r': {
                    chars[off] = '\\';
                    chars[off + 1] = 'r';
                    off += 2;
                    break;
                }
                case '\n': {
                    chars[off] = '\\';
                    chars[off + 1] = 'n';
                    off += 2;
                    break;
                }
                case '\b': {
                    chars[off] = '\\';
                    chars[off + 1] = 'b';
                    off += 2;
                    break;
                }
                case '\f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'f';
                    off += 2;
                    break;
                }
                case '\t': {
                    chars[off] = '\\';
                    chars[off + 1] = 't';
                    off += 2;
                    break;
                }
                case '\0':
                case '\u0001':
                case '\u0002':
                case '\u0003':
                case '\u0004':
                case '\u0005':
                case '\u0006':
                case '\u0007': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 2] = '0';
                    chars[off + 4] = (chars[off + 3] = '0');
                    chars[off + 5] = (char)('0' + ch);
                    off += 6;
                    break;
                }
                case '\u000b':
                case '\u000e':
                case '\u000f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 2] = '0';
                    chars[off + 4] = (chars[off + 3] = '0');
                    chars[off + 5] = (char)(97 + (ch - '\n'));
                    off += 6;
                    break;
                }
                case '\u0010':
                case '\u0011':
                case '\u0012':
                case '\u0013':
                case '\u0014':
                case '\u0015':
                case '\u0016':
                case '\u0017':
                case '\u0018':
                case '\u0019': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 3] = (chars[off + 2] = '0');
                    chars[off + 4] = '1';
                    chars[off + 5] = (char)(48 + (ch - '\u0010'));
                    off += 6;
                    break;
                }
                case '\u001a':
                case '\u001b':
                case '\u001c':
                case '\u001d':
                case '\u001e':
                case '\u001f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 3] = (chars[off + 2] = '0');
                    chars[off + 4] = '1';
                    chars[off + 5] = (char)(97 + (ch - '\u001a'));
                    off += 6;
                    break;
                }
                case '(':
                case ')':
                case '<':
                case '>': {
                    if (browserSecure) {
                        chars[off] = '\\';
                        chars[off + 1] = 'u';
                        chars[off + 3] = (chars[off + 2] = '0');
                        chars[off + 4] = JSONWriterUTF16.DIGITS[ch >>> 4 & 0xF];
                        chars[off + 5] = JSONWriterUTF16.DIGITS[ch & '\u000f'];
                        off += 6;
                        break;
                    }
                    chars[off++] = ch;
                    break;
                }
                default: {
                    if (escapeNoneAscii && ch > '\u007f') {
                        chars[off] = '\\';
                        chars[off + 1] = 'u';
                        chars[off + 2] = JSONWriterUTF16.DIGITS[ch >>> 12 & 0xF];
                        chars[off + 3] = JSONWriterUTF16.DIGITS[ch >>> 8 & 0xF];
                        chars[off + 4] = JSONWriterUTF16.DIGITS[ch >>> 4 & 0xF];
                        chars[off + 5] = JSONWriterUTF16.DIGITS[ch & '\u000f'];
                        off += 6;
                        break;
                    }
                    chars[off++] = ch;
                    break;
                }
            }
        }
        chars[off] = quote;
        this.off = off + 1;
    }
    
    protected final void writeStringEscape(final char[] str) {
        final int strlen = str.length;
        final char quote = this.quote;
        final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        int off = this.off;
        this.ensureCapacity(off + strlen * 6 + 2);
        final char[] chars = this.chars;
        chars[off++] = quote;
        for (int i = 0; i < str.length; ++i) {
            final char ch = str[i];
            switch (ch) {
                case '\"':
                case '\'': {
                    if (ch == quote) {
                        chars[off++] = '\\';
                    }
                    chars[off++] = ch;
                    break;
                }
                case '\\': {
                    chars[off] = '\\';
                    chars[off + 1] = ch;
                    off += 2;
                    break;
                }
                case '\r': {
                    chars[off] = '\\';
                    chars[off + 1] = 'r';
                    off += 2;
                    break;
                }
                case '\n': {
                    chars[off] = '\\';
                    chars[off + 1] = 'n';
                    off += 2;
                    break;
                }
                case '\b': {
                    chars[off] = '\\';
                    chars[off + 1] = 'b';
                    off += 2;
                    break;
                }
                case '\f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'f';
                    off += 2;
                    break;
                }
                case '\t': {
                    chars[off] = '\\';
                    chars[off + 1] = 't';
                    off += 2;
                    break;
                }
                case '\0':
                case '\u0001':
                case '\u0002':
                case '\u0003':
                case '\u0004':
                case '\u0005':
                case '\u0006':
                case '\u0007': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 2] = '0';
                    chars[off + 4] = (chars[off + 3] = '0');
                    chars[off + 5] = (char)('0' + ch);
                    off += 6;
                    break;
                }
                case '\u000b':
                case '\u000e':
                case '\u000f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 2] = '0';
                    chars[off + 4] = (chars[off + 3] = '0');
                    chars[off + 5] = (char)(97 + (ch - '\n'));
                    off += 6;
                    break;
                }
                case '\u0010':
                case '\u0011':
                case '\u0012':
                case '\u0013':
                case '\u0014':
                case '\u0015':
                case '\u0016':
                case '\u0017':
                case '\u0018':
                case '\u0019': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 3] = (chars[off + 2] = '0');
                    chars[off + 4] = '1';
                    chars[off + 5] = (char)(48 + (ch - '\u0010'));
                    off += 6;
                    break;
                }
                case '\u001a':
                case '\u001b':
                case '\u001c':
                case '\u001d':
                case '\u001e':
                case '\u001f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 3] = (chars[off + 2] = '0');
                    chars[off + 4] = '1';
                    chars[off + 5] = (char)(97 + (ch - '\u001a'));
                    off += 6;
                    break;
                }
                case '(':
                case ')':
                case '<':
                case '>': {
                    if (browserSecure) {
                        chars[off] = '\\';
                        chars[off + 1] = 'u';
                        chars[off + 3] = (chars[off + 2] = '0');
                        chars[off + 4] = JSONWriterUTF16.DIGITS[ch >>> 4 & 0xF];
                        chars[off + 5] = JSONWriterUTF16.DIGITS[ch & '\u000f'];
                        off += 6;
                        break;
                    }
                    chars[off++] = ch;
                    break;
                }
                default: {
                    if (escapeNoneAscii && ch > '\u007f') {
                        chars[off] = '\\';
                        chars[off + 1] = 'u';
                        chars[off + 2] = JSONWriterUTF16.DIGITS[ch >>> 12 & 0xF];
                        chars[off + 3] = JSONWriterUTF16.DIGITS[ch >>> 8 & 0xF];
                        chars[off + 4] = JSONWriterUTF16.DIGITS[ch >>> 4 & 0xF];
                        chars[off + 5] = JSONWriterUTF16.DIGITS[ch & '\u000f'];
                        off += 6;
                        break;
                    }
                    chars[off++] = ch;
                    break;
                }
            }
        }
        chars[off] = quote;
        this.off = off + 1;
    }
    
    protected final void writeStringEscape(final byte[] str) {
        final int strlen = str.length;
        final char quote = this.quote;
        final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        int off = this.off;
        this.ensureCapacity(off + strlen * 6 + 2);
        final char[] chars = this.chars;
        chars[off++] = quote;
        for (int i = 0; i < str.length; ++i) {
            final byte b = str[i];
            final char ch = (char)(b & 0xFF);
            switch (ch) {
                case '\"':
                case '\'': {
                    if (ch == quote) {
                        chars[off++] = '\\';
                    }
                    chars[off++] = ch;
                    break;
                }
                case '\\': {
                    chars[off] = '\\';
                    chars[off + 1] = ch;
                    off += 2;
                    break;
                }
                case '\r': {
                    chars[off] = '\\';
                    chars[off + 1] = 'r';
                    off += 2;
                    break;
                }
                case '\n': {
                    chars[off] = '\\';
                    chars[off + 1] = 'n';
                    off += 2;
                    break;
                }
                case '\b': {
                    chars[off] = '\\';
                    chars[off + 1] = 'b';
                    off += 2;
                    break;
                }
                case '\f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'f';
                    off += 2;
                    break;
                }
                case '\t': {
                    chars[off] = '\\';
                    chars[off + 1] = 't';
                    off += 2;
                    break;
                }
                case '\0':
                case '\u0001':
                case '\u0002':
                case '\u0003':
                case '\u0004':
                case '\u0005':
                case '\u0006':
                case '\u0007': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 2] = '0';
                    chars[off + 4] = (chars[off + 3] = '0');
                    chars[off + 5] = (char)('0' + ch);
                    off += 6;
                    break;
                }
                case '\u000b':
                case '\u000e':
                case '\u000f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 2] = '0';
                    chars[off + 4] = (chars[off + 3] = '0');
                    chars[off + 5] = (char)(97 + (ch - '\n'));
                    off += 6;
                    break;
                }
                case '\u0010':
                case '\u0011':
                case '\u0012':
                case '\u0013':
                case '\u0014':
                case '\u0015':
                case '\u0016':
                case '\u0017':
                case '\u0018':
                case '\u0019': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 3] = (chars[off + 2] = '0');
                    chars[off + 4] = '1';
                    chars[off + 5] = (char)(48 + (ch - '\u0010'));
                    off += 6;
                    break;
                }
                case '\u001a':
                case '\u001b':
                case '\u001c':
                case '\u001d':
                case '\u001e':
                case '\u001f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 3] = (chars[off + 2] = '0');
                    chars[off + 4] = '1';
                    chars[off + 5] = (char)(97 + (ch - '\u001a'));
                    off += 6;
                    break;
                }
                case '(':
                case ')':
                case '<':
                case '>': {
                    if (browserSecure) {
                        chars[off] = '\\';
                        chars[off + 1] = 'u';
                        chars[off + 3] = (chars[off + 2] = '0');
                        chars[off + 4] = JSONWriterUTF16.DIGITS[ch >>> 4 & 0xF];
                        chars[off + 5] = JSONWriterUTF16.DIGITS[ch & '\u000f'];
                        off += 6;
                        break;
                    }
                    chars[off++] = ch;
                    break;
                }
                default: {
                    if (escapeNoneAscii && ch > '\u007f') {
                        chars[off] = '\\';
                        chars[off + 1] = 'u';
                        chars[off + 3] = (chars[off + 2] = '0');
                        chars[off + 4] = JSONWriterUTF16.DIGITS[ch >>> 4 & 0xF];
                        chars[off + 5] = JSONWriterUTF16.DIGITS[ch & '\u000f'];
                        off += 6;
                        break;
                    }
                    chars[off++] = ch;
                    break;
                }
            }
        }
        chars[off] = quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeString(final char[] str, final int offset, final int len, final boolean quoted) {
        final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
        final char quote = this.quote;
        int off = this.off;
        int minCapacity = quoted ? (off + 2) : off;
        if (escapeNoneAscii) {
            minCapacity += len * 6;
        }
        else {
            minCapacity += len * 2;
        }
        if (minCapacity - this.chars.length > 0) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        if (quoted) {
            chars[off++] = quote;
        }
        for (int i = offset; i < len; ++i) {
            final char ch = str[i];
            switch (ch) {
                case '\"':
                case '\'': {
                    if (ch == quote) {
                        chars[off++] = '\\';
                    }
                    chars[off++] = ch;
                    break;
                }
                case '\\': {
                    chars[off] = '\\';
                    chars[off + 1] = ch;
                    off += 2;
                    break;
                }
                case '\r': {
                    chars[off] = '\\';
                    chars[off + 1] = 'r';
                    off += 2;
                    break;
                }
                case '\n': {
                    chars[off] = '\\';
                    chars[off + 1] = 'n';
                    off += 2;
                    break;
                }
                case '\b': {
                    chars[off] = '\\';
                    chars[off + 1] = 'b';
                    off += 2;
                    break;
                }
                case '\f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'f';
                    off += 2;
                    break;
                }
                case '\t': {
                    chars[off] = '\\';
                    chars[off + 1] = 't';
                    off += 2;
                    break;
                }
                case '\0':
                case '\u0001':
                case '\u0002':
                case '\u0003':
                case '\u0004':
                case '\u0005':
                case '\u0006':
                case '\u0007': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 2] = '0';
                    chars[off + 4] = (chars[off + 3] = '0');
                    chars[off + 5] = (char)('0' + ch);
                    off += 6;
                    break;
                }
                case '\u000b':
                case '\u000e':
                case '\u000f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 2] = '0';
                    chars[off + 4] = (chars[off + 3] = '0');
                    chars[off + 5] = (char)(97 + (ch - '\n'));
                    off += 6;
                    break;
                }
                case '\u0010':
                case '\u0011':
                case '\u0012':
                case '\u0013':
                case '\u0014':
                case '\u0015':
                case '\u0016':
                case '\u0017':
                case '\u0018':
                case '\u0019': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 3] = (chars[off + 2] = '0');
                    chars[off + 4] = '1';
                    chars[off + 5] = (char)(48 + (ch - '\u0010'));
                    off += 6;
                    break;
                }
                case '\u001a':
                case '\u001b':
                case '\u001c':
                case '\u001d':
                case '\u001e':
                case '\u001f': {
                    chars[off] = '\\';
                    chars[off + 1] = 'u';
                    chars[off + 3] = (chars[off + 2] = '0');
                    chars[off + 4] = '1';
                    chars[off + 5] = (char)(97 + (ch - '\u001a'));
                    off += 6;
                    break;
                }
                default: {
                    if (escapeNoneAscii && ch > '\u007f') {
                        chars[off] = '\\';
                        chars[off + 1] = 'u';
                        chars[off + 2] = JSONWriterUTF16.DIGITS[ch >>> 12 & 0xF];
                        chars[off + 3] = JSONWriterUTF16.DIGITS[ch >>> 8 & 0xF];
                        chars[off + 4] = JSONWriterUTF16.DIGITS[ch >>> 4 & 0xF];
                        chars[off + 5] = JSONWriterUTF16.DIGITS[ch & '\u000f'];
                        off += 6;
                        break;
                    }
                    chars[off++] = ch;
                    break;
                }
            }
        }
        if (quoted) {
            chars[off++] = quote;
        }
        this.off = off;
    }
    
    @Override
    public final void writeString(final String[] strings) {
        if (strings == null) {
            this.writeArrayNull();
            return;
        }
        this.startArray();
        for (int i = 0; i < strings.length; ++i) {
            if (i != 0) {
                this.writeComma();
            }
            final String item = strings[i];
            if (item == null) {
                if (this.isEnabled(Feature.NullAsDefaultValue.mask | Feature.WriteNullStringAsEmpty.mask)) {
                    this.writeString("");
                }
                else {
                    this.writeNull();
                }
            }
            else {
                this.writeString(item);
            }
        }
        this.endArray();
    }
    
    @Override
    public final void writeReference(final String path) {
        this.lastReference = path;
        this.writeRaw(JSONWriterUTF16.REF_PREF, 0, JSONWriterUTF16.REF_PREF.length);
        this.writeString(path);
        final int off = this.off;
        if (off == this.chars.length) {
            this.ensureCapacity(off + 1);
        }
        this.chars[off] = '}';
        this.off = off + 1;
    }
    
    @Override
    public final void writeBase64(final byte[] bytes) {
        if (bytes == null) {
            this.writeArrayNull();
            return;
        }
        final int charsLen = (bytes.length - 1) / 3 + 1 << 2;
        int off = this.off;
        this.ensureCapacity(off + charsLen + 2);
        final char[] chars = this.chars;
        chars[off++] = this.quote;
        final int eLen = bytes.length / 3 * 3;
        int i;
        for (int s = 0; s < eLen; i = ((bytes[s++] & 0xFF) << 16 | (bytes[s++] & 0xFF) << 8 | (bytes[s++] & 0xFF)), chars[off] = JSONFactory.CA[i >>> 18 & 0x3F], chars[off + 1] = JSONFactory.CA[i >>> 12 & 0x3F], chars[off + 2] = JSONFactory.CA[i >>> 6 & 0x3F], chars[off + 3] = JSONFactory.CA[i & 0x3F], off += 4) {}
        final int left = bytes.length - eLen;
        if (left > 0) {
            i = ((bytes[eLen] & 0xFF) << 10 | ((left == 2) ? ((bytes[bytes.length - 1] & 0xFF) << 2) : 0));
            chars[off] = JSONFactory.CA[i >> 12];
            chars[off + 1] = JSONFactory.CA[i >>> 6 & 0x3F];
            chars[off + 2] = ((left == 2) ? JSONFactory.CA[i & 0x3F] : '=');
            chars[off + 3] = '=';
            off += 4;
        }
        chars[off] = this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeHex(final byte[] bytes) {
        if (bytes == null) {
            this.writeNull();
            return;
        }
        final int charsLen = bytes.length * 2 + 3;
        int off = this.off;
        this.ensureCapacity(off + charsLen + 2);
        final char[] chars = this.chars;
        chars[off] = 'x';
        chars[off + 1] = '\'';
        off += 2;
        for (int i = 0; i < bytes.length; ++i) {
            final byte b = bytes[i];
            final int a = b & 0xFF;
            final int b2 = a >> 4;
            final int b3 = a & 0xF;
            chars[off] = (char)(b2 + ((b2 < 10) ? 48 : 55));
            chars[off + 1] = (char)(b3 + ((b3 < 10) ? 48 : 55));
            off += 2;
        }
        chars[off] = '\'';
        this.off = off + 1;
    }
    
    @Override
    public final void writeBigInt(final BigInteger value, long features) {
        if (value == null) {
            this.writeNumberNull();
            return;
        }
        final String str = value.toString(10);
        features |= this.context.features;
        final boolean browserCompatible = (features & Feature.BrowserCompatible.mask) != 0x0L && (value.compareTo(JSONFactory.LOW_BIGINT) < 0 || value.compareTo(JSONFactory.HIGH_BIGINT) > 0);
        final boolean nonStringAsString = (features & (Feature.WriteNonStringValueAsString.mask | Feature.WriteLongAsString.mask)) != 0x0L;
        final boolean writeAsString = browserCompatible || nonStringAsString;
        final int strlen = str.length();
        this.ensureCapacity(this.off + strlen + 2);
        final char[] chars = this.chars;
        int off = this.off;
        if (writeAsString) {
            chars[off++] = '\"';
            str.getChars(0, strlen, chars, off);
            off += strlen;
            chars[off++] = '\"';
        }
        else {
            str.getChars(0, strlen, chars, off);
            off += strlen;
        }
        this.off = off;
    }
    
    @Override
    public final void writeDecimal(final BigDecimal value, long features, final DecimalFormat format) {
        if (value == null) {
            this.writeNumberNull();
            return;
        }
        if (format != null) {
            final String str = format.format(value);
            this.writeRaw(str);
            return;
        }
        features |= this.context.features;
        final int precision = value.precision();
        final boolean nonStringAsString = (features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        final boolean writeAsString = nonStringAsString || ((features & Feature.BrowserCompatible.mask) != 0x0L && precision >= 16 && (value.compareTo(JSONFactory.LOW) < 0 || value.compareTo(JSONFactory.HIGH) > 0));
        int off = this.off;
        final int minCapacity = off + precision + 7;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        if (writeAsString) {
            chars[off++] = '\"';
        }
        final boolean asPlain = (features & Feature.WriteBigDecimalAsPlain.mask) != 0x0L;
        final int scale;
        final long unscaleValue;
        if (precision < 19 && (scale = value.scale()) >= 0 && JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET != -1L && (unscaleValue = JDKUtils.UNSAFE.getLong(value, JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET)) != Long.MIN_VALUE && !asPlain) {
            off = IOUtils.writeDecimal(chars, off, unscaleValue, scale);
        }
        else {
            final String str2 = asPlain ? value.toPlainString() : value.toString();
            str2.getChars(0, str2.length(), chars, off);
            off += str2.length();
        }
        if (writeAsString) {
            chars[off++] = '\"';
        }
        this.off = off;
    }
    
    static void putLong(final char[] buf, final int off, final int b0, final int b1) {
        final long v = (long)JSONWriterUTF16.HEX256[b0 & 0xFF] | (long)JSONWriterUTF16.HEX256[b1 & 0xFF] << 32;
        JDKUtils.UNSAFE.putLong(buf, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off << 1), JDKUtils.BIG_ENDIAN ? Long.reverseBytes(v << 8) : v);
    }
    
    @Override
    public final void writeUUID(final UUID value) {
        if (value == null) {
            this.writeNull();
            return;
        }
        final long msb = value.getMostSignificantBits();
        final long lsb = value.getLeastSignificantBits();
        final int minCapacity = this.off + 38;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] buf = this.chars;
        final int off = this.off;
        buf[off] = '\"';
        putLong(buf, off + 1, (int)(msb >> 56), (int)(msb >> 48));
        putLong(buf, off + 5, (int)(msb >> 40), (int)(msb >> 32));
        buf[off + 9] = '-';
        putLong(buf, off + 10, (int)msb >> 24, (int)msb >> 16);
        buf[off + 14] = '-';
        putLong(buf, off + 15, (int)msb >> 8, (int)msb);
        buf[off + 19] = '-';
        putLong(buf, off + 20, (int)(lsb >> 56), (int)(lsb >> 48));
        buf[off + 24] = '-';
        putLong(buf, off + 25, (int)(lsb >> 40), (int)(lsb >> 32));
        putLong(buf, off + 29, (int)lsb >> 24, (int)lsb >> 16);
        putLong(buf, off + 33, (int)lsb >> 8, (int)lsb);
        buf[off + 37] = '\"';
        this.off += 38;
    }
    
    @Override
    public final void writeRaw(final String str) {
        this.ensureCapacity(this.off + str.length());
        str.getChars(0, str.length(), this.chars, this.off);
        this.off += str.length();
    }
    
    @Override
    public final void writeRaw(final char[] chars, final int off, final int charslen) {
        final int minCapacity = this.off + charslen;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        System.arraycopy(chars, off, this.chars, this.off, charslen);
        this.off += charslen;
    }
    
    @Override
    public final void writeChar(final char ch) {
        int off = this.off;
        final int minCapacity = off + 8;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = this.quote;
        switch (ch) {
            case '\"':
            case '\'': {
                if (ch == this.quote) {
                    chars[off++] = '\\';
                }
                chars[off++] = ch;
                break;
            }
            case '\\': {
                chars[off] = '\\';
                chars[off + 1] = ch;
                off += 2;
                break;
            }
            case '\r': {
                chars[off] = '\\';
                chars[off + 1] = 'r';
                off += 2;
                break;
            }
            case '\n': {
                chars[off] = '\\';
                chars[off + 1] = 'n';
                off += 2;
                break;
            }
            case '\b': {
                chars[off] = '\\';
                chars[off + 1] = 'b';
                off += 2;
                break;
            }
            case '\f': {
                chars[off] = '\\';
                chars[off + 1] = 'f';
                off += 2;
                break;
            }
            case '\t': {
                chars[off] = '\\';
                chars[off + 1] = 't';
                off += 2;
                break;
            }
            case '\0':
            case '\u0001':
            case '\u0002':
            case '\u0003':
            case '\u0004':
            case '\u0005':
            case '\u0006':
            case '\u0007': {
                chars[off] = '\\';
                chars[off + 1] = 'u';
                chars[off + 2] = '0';
                chars[off + 4] = (chars[off + 3] = '0');
                chars[off + 5] = (char)('0' + ch);
                off += 6;
                break;
            }
            case '\u000b':
            case '\u000e':
            case '\u000f': {
                chars[off] = '\\';
                chars[off + 1] = 'u';
                chars[off + 2] = '0';
                chars[off + 4] = (chars[off + 3] = '0');
                chars[off + 5] = (char)(97 + (ch - '\n'));
                off += 6;
                break;
            }
            case '\u0010':
            case '\u0011':
            case '\u0012':
            case '\u0013':
            case '\u0014':
            case '\u0015':
            case '\u0016':
            case '\u0017':
            case '\u0018':
            case '\u0019': {
                chars[off] = '\\';
                chars[off + 1] = 'u';
                chars[off + 3] = (chars[off + 2] = '0');
                chars[off + 4] = '1';
                chars[off + 5] = (char)(48 + (ch - '\u0010'));
                off += 6;
                break;
            }
            case '\u001a':
            case '\u001b':
            case '\u001c':
            case '\u001d':
            case '\u001e':
            case '\u001f': {
                chars[off] = '\\';
                chars[off + 1] = 'u';
                chars[off + 3] = (chars[off + 2] = '0');
                chars[off + 4] = '1';
                chars[off + 5] = (char)(97 + (ch - '\u001a'));
                off += 6;
                break;
            }
            default: {
                chars[off++] = ch;
                break;
            }
        }
        chars[off] = this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeRaw(final char ch) {
        if (this.off == this.chars.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.chars[this.off++] = ch;
    }
    
    @Override
    public final void writeRaw(final char c0, final char c1) {
        final int off = this.off;
        if (off + 1 >= this.chars.length) {
            this.ensureCapacity(off + 2);
        }
        this.chars[off] = c0;
        this.chars[off + 1] = c1;
        this.off = off + 2;
    }
    
    @Override
    public final void writeNameRaw(final char[] name) {
        int off = this.off;
        final int minCapacity = off + name.length + 2 + this.indent;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            final char[] chars = this.chars;
            chars[off++] = ',';
            if (this.pretty) {
                chars[off++] = '\n';
                for (int i = 0; i < this.indent; ++i) {
                    chars[off++] = '\t';
                }
            }
        }
        System.arraycopy(name, 0, this.chars, off, name.length);
        this.off = off + name.length;
    }
    
    @Override
    public final void writeNameRaw(final char[] chars, final int off, final int len) {
        final int minCapacity = this.off + len + 2 + this.indent;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            this.chars[this.off++] = ',';
        }
        System.arraycopy(chars, off, this.chars, this.off, len);
        this.off += len;
    }
    
    final void ensureCapacity(final int minCapacity) {
        if (minCapacity - this.chars.length > 0) {
            final int oldCapacity = this.chars.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            if (newCapacity - minCapacity < 0) {
                newCapacity = minCapacity;
            }
            if (newCapacity - this.maxArraySize > 0) {
                throw new OutOfMemoryError();
            }
            this.chars = Arrays.copyOf(this.chars, newCapacity);
        }
    }
    
    @Override
    public final void writeInt32(final int[] value) {
        if (value == null) {
            this.writeNull();
            return;
        }
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        final int minCapacity = off + value.length * 13 + 2;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = '[';
        for (int i = 0; i < value.length; ++i) {
            if (i != 0) {
                chars[off++] = ',';
            }
            if (writeAsString) {
                chars[off++] = this.quote;
            }
            off = IOUtils.writeInt32(chars, off, value[i]);
            if (writeAsString) {
                chars[off++] = this.quote;
            }
        }
        chars[off] = ']';
        this.off = off + 1;
    }
    
    @Override
    public final void writeInt8(final byte i) {
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        final int minCapacity = off + 7;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        if (writeAsString) {
            chars[off++] = this.quote;
        }
        off = IOUtils.writeInt32(chars, off, i);
        if (writeAsString) {
            chars[off++] = this.quote;
        }
        this.off = off;
    }
    
    @Override
    public final void writeInt16(final short i) {
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        final int minCapacity = off + 7;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        if (writeAsString) {
            chars[off++] = this.quote;
        }
        off = IOUtils.writeInt32(chars, off, i);
        if (writeAsString) {
            chars[off++] = this.quote;
        }
        this.off = off;
    }
    
    @Override
    public final void writeInt32(final int i) {
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        final int minCapacity = off + 13;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        if (writeAsString) {
            chars[off++] = this.quote;
        }
        off = IOUtils.writeInt32(chars, off, i);
        if (writeAsString) {
            chars[off++] = this.quote;
        }
        this.off = off;
    }
    
    @Override
    public final void writeInt32(final Integer i) {
        if (i == null) {
            this.writeNumberNull();
        }
        else {
            this.writeInt32((int)i);
        }
    }
    
    @Override
    public final void writeInt64(final long[] values) {
        if (values == null) {
            this.writeNull();
            return;
        }
        final long features = this.context.features;
        final boolean browserCompatible = (features & Feature.BrowserCompatible.mask) != 0x0L;
        final boolean nonStringAsString = (features & (Feature.WriteNonStringValueAsString.mask | Feature.WriteLongAsString.mask)) != 0x0L;
        int off = this.off;
        final int minCapacity = off + 2 + values.length * 23;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = '[';
        for (int i = 0; i < values.length; ++i) {
            if (i != 0) {
                chars[off++] = ',';
            }
            final long v = values[i];
            final boolean writeAsString = nonStringAsString || (browserCompatible && v <= 9007199254740991L && v >= -9007199254740991L);
            if (writeAsString) {
                chars[off++] = this.quote;
            }
            off = IOUtils.writeInt64(chars, off, v);
            if (writeAsString) {
                chars[off++] = this.quote;
            }
        }
        chars[off] = ']';
        this.off = off + 1;
    }
    
    @Override
    public final void writeListInt32(final List<Integer> values) {
        if (values == null) {
            this.writeNull();
            return;
        }
        final int size = values.size();
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        final int minCapacity = off + 2 + size * 23;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = '[';
        for (int i = 0; i < size; ++i) {
            if (i != 0) {
                chars[off++] = ',';
            }
            final Number item = values.get(i);
            if (item == null) {
                chars[off] = 'n';
                chars[off + 1] = 'u';
                chars[off + 3] = (chars[off + 2] = 'l');
                off += 4;
            }
            else {
                final int v = item.intValue();
                if (writeAsString) {
                    chars[off++] = this.quote;
                }
                off = IOUtils.writeInt32(chars, off, v);
                if (writeAsString) {
                    chars[off++] = this.quote;
                }
            }
        }
        chars[off] = ']';
        this.off = off + 1;
    }
    
    @Override
    public final void writeListInt64(final List<Long> values) {
        if (values == null) {
            this.writeNull();
            return;
        }
        final int size = values.size();
        final long features = this.context.features;
        final boolean browserCompatible = (features & Feature.BrowserCompatible.mask) != 0x0L;
        final boolean nonStringAsString = (features & (Feature.WriteNonStringValueAsString.mask | Feature.WriteLongAsString.mask)) != 0x0L;
        int off = this.off;
        final int minCapacity = off + 2 + size * 23;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = '[';
        for (int i = 0; i < size; ++i) {
            if (i != 0) {
                chars[off++] = ',';
            }
            final Long item = values.get(i);
            if (item == null) {
                chars[off] = 'n';
                chars[off + 1] = 'u';
                chars[off + 3] = (chars[off + 2] = 'l');
                off += 4;
            }
            else {
                final long v = item;
                final boolean writeAsString = nonStringAsString || (browserCompatible && v <= 9007199254740991L && v >= -9007199254740991L);
                if (writeAsString) {
                    chars[off++] = this.quote;
                }
                off = IOUtils.writeInt64(chars, off, v);
                if (writeAsString) {
                    chars[off++] = this.quote;
                }
            }
        }
        chars[off] = ']';
        this.off = off + 1;
    }
    
    @Override
    public final void writeInt64(final long i) {
        final long features = this.context.features;
        final boolean writeAsString = (features & (Feature.WriteNonStringValueAsString.mask | Feature.WriteLongAsString.mask)) != 0x0L || ((features & Feature.BrowserCompatible.mask) != 0x0L && (i > 9007199254740991L || i < -9007199254740991L));
        int off = this.off;
        final int minCapacity = off + 23;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        if (writeAsString) {
            chars[off++] = this.quote;
        }
        off = IOUtils.writeInt64(chars, off, i);
        if (writeAsString) {
            chars[off++] = this.quote;
        }
        else if ((features & Feature.WriteClassName.mask) != 0x0L && (features & Feature.NotWriteNumberClassName.mask) == 0x0L && i >= -2147483648L && i <= 2147483647L) {
            chars[off++] = 'L';
        }
        this.off = off;
    }
    
    @Override
    public final void writeInt64(final Long i) {
        if (i == null) {
            this.writeNumberNull();
        }
        else {
            this.writeInt64((long)i);
        }
    }
    
    @Override
    public final void writeFloat(final float value) {
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        int minCapacity = off + 15;
        if (writeAsString) {
            minCapacity += 2;
        }
        this.ensureCapacity(minCapacity);
        final char[] chars = this.chars;
        if (writeAsString) {
            chars[off++] = '\"';
        }
        final int len = DoubleToDecimal.toString(value, chars, off, true);
        off += len;
        if (writeAsString) {
            chars[off++] = '\"';
        }
        this.off = off;
    }
    
    @Override
    public final void writeFloat(final float[] values) {
        if (values == null) {
            this.writeArrayNull();
            return;
        }
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        final int minCapacity = off + values.length * (writeAsString ? 16 : 18) + 1;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = '[';
        for (int i = 0; i < values.length; ++i) {
            if (i != 0) {
                chars[off++] = ',';
            }
            if (writeAsString) {
                chars[off++] = '\"';
            }
            final float value = values[i];
            final int len = DoubleToDecimal.toString(value, chars, off, true);
            off += len;
            if (writeAsString) {
                chars[off++] = '\"';
            }
        }
        chars[off] = ']';
        this.off = off + 1;
    }
    
    @Override
    public final void writeDouble(final double value) {
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        int minCapacity = off + 24;
        if (writeAsString) {
            minCapacity += 2;
        }
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        if (writeAsString) {
            chars[off++] = '\"';
        }
        final int len = DoubleToDecimal.toString(value, chars, off, true);
        off += len;
        if (writeAsString) {
            chars[off++] = '\"';
        }
        this.off = off;
    }
    
    @Override
    public final void writeDoubleArray(final double value0, final double value1) {
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        int minCapacity = off + 48 + 3;
        if (writeAsString) {
            minCapacity += 2;
        }
        this.ensureCapacity(minCapacity);
        final char[] chars = this.chars;
        chars[off++] = '[';
        if (writeAsString) {
            chars[off++] = '\"';
        }
        final int len0 = DoubleToDecimal.toString(value0, chars, off, true);
        off += len0;
        if (writeAsString) {
            chars[off++] = '\"';
        }
        chars[off++] = ',';
        if (writeAsString) {
            chars[off++] = '\"';
        }
        final int len2 = DoubleToDecimal.toString(value1, chars, off, true);
        off += len2;
        if (writeAsString) {
            chars[off++] = '\"';
        }
        chars[off] = ']';
        this.off = off + 1;
    }
    
    @Override
    public final void writeDouble(final double[] values) {
        if (values == null) {
            this.writeNull();
            return;
        }
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        final int minCapacity = off + values.length * 27 + 1;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = '[';
        for (int i = 0; i < values.length; ++i) {
            if (i != 0) {
                chars[off++] = ',';
            }
            if (writeAsString) {
                chars[off++] = '\"';
            }
            final double value = values[i];
            final int len = DoubleToDecimal.toString(value, chars, off, true);
            off += len;
            if (writeAsString) {
                chars[off++] = '\"';
            }
        }
        chars[off] = ']';
        this.off = off + 1;
    }
    
    @Override
    public final void writeDateTime14(final int year, final int month, final int dayOfMonth, final int hour, final int minute, final int second) {
        final int off = this.off;
        final int minCapacity = off + 16;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] bytes = this.chars;
        bytes[off] = this.quote;
        if (year < 0 || year > 9999) {
            throw new IllegalArgumentException("Only 4 digits numbers are supported. Provided: " + year);
        }
        final int y01 = year / 100;
        final int y2 = year - y01 * 100;
        JDKUtils.UNSAFE.putInt(this.chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 1 << 1), IOUtils.PACKED_DIGITS_UTF16[y01]);
        JDKUtils.UNSAFE.putInt(this.chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 3 << 1), IOUtils.PACKED_DIGITS_UTF16[y2]);
        JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 5 << 1), IOUtils.PACKED_DIGITS_UTF16[month]);
        JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 7 << 1), IOUtils.PACKED_DIGITS_UTF16[dayOfMonth]);
        JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 9 << 1), IOUtils.PACKED_DIGITS_UTF16[hour]);
        JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 11 << 1), IOUtils.PACKED_DIGITS_UTF16[minute]);
        JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 13 << 1), IOUtils.PACKED_DIGITS_UTF16[second]);
        bytes[off + 15] = this.quote;
        this.off = off + 16;
    }
    
    @Override
    public final void writeDateTime19(final int year, final int month, final int dayOfMonth, final int hour, final int minute, final int second) {
        this.ensureCapacity(this.off + 21);
        final char[] chars = this.chars;
        int off = this.off;
        chars[off] = this.quote;
        if (year < 0 || year > 9999) {
            throw new IllegalArgumentException("Only 4 digits numbers are supported. Provided: " + year);
        }
        off = IOUtils.writeLocalDate(chars, off + 1, year, month, dayOfMonth);
        chars[off] = ' ';
        IOUtils.writeLocalTime(chars, off + 1, hour, minute, second);
        chars[off + 9] = this.quote;
        this.off = off + 10;
    }
    
    @Override
    public final void writeLocalDate(final LocalDate date) {
        if (date == null) {
            this.writeNull();
            return;
        }
        final Context context = this.context;
        if (context.dateFormat != null && this.writeLocalDateWithFormat(date, context)) {
            return;
        }
        int off = this.off;
        final int minCapacity = off + 18;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = this.quote;
        off = IOUtils.writeLocalDate(chars, off, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        chars[off] = this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeLocalDateTime(final LocalDateTime dateTime) {
        int off = this.off;
        final int minCapacity = off + 38;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = this.quote;
        final LocalDate localDate = dateTime.toLocalDate();
        off = IOUtils.writeLocalDate(chars, off, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        chars[off++] = ' ';
        off = IOUtils.writeLocalTime(chars, off, dateTime.toLocalTime());
        chars[off] = this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeDateTimeISO8601(final int year, final int month, final int dayOfMonth, final int hour, final int minute, final int second, final int millis, final int offsetSeconds, final boolean timeZone) {
        int zonelen;
        if (timeZone) {
            zonelen = ((offsetSeconds == 0) ? 1 : 6);
        }
        else {
            zonelen = 0;
        }
        int off = this.off;
        final int minCapacity = off + 25 + zonelen;
        if (off + minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off] = this.quote;
        off = IOUtils.writeLocalDate(chars, off + 1, year, month, dayOfMonth);
        chars[off] = (timeZone ? 'T' : ' ');
        IOUtils.writeLocalTime(chars, off + 1, hour, minute, second);
        off += 9;
        if (millis > 0) {
            final int div = millis / 10;
            final int div2 = div / 10;
            final int rem1 = millis - div * 10;
            if (rem1 != 0) {
                IOUtils.putLong(chars, off, (IOUtils.DIGITS_K_64[millis] & 0xFFFFFFFFFFFF0000L) | IOUtils.DOT_X0);
                off += 4;
            }
            else {
                chars[off++] = '.';
                final int rem2 = div - div2 * 10;
                if (rem2 != 0) {
                    JDKUtils.UNSAFE.putInt(this.chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off << 1), IOUtils.PACKED_DIGITS_UTF16[div]);
                    off += 2;
                }
                else {
                    chars[off++] = (char)(byte)(div2 + 48);
                }
            }
        }
        if (timeZone) {
            final int offset = offsetSeconds / 3600;
            if (offsetSeconds == 0) {
                chars[off++] = 'Z';
            }
            else {
                final int offsetAbs = Math.abs(offset);
                chars[off] = ((offset >= 0) ? '+' : '-');
                JDKUtils.UNSAFE.putInt(this.chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 1 << 1), IOUtils.PACKED_DIGITS_UTF16[offsetAbs]);
                chars[off + 3] = ':';
                int offsetMinutes = (offsetSeconds - offset * 3600) / 60;
                if (offsetMinutes < 0) {
                    offsetMinutes = -offsetMinutes;
                }
                JDKUtils.UNSAFE.putInt(this.chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 4 << 1), IOUtils.PACKED_DIGITS_UTF16[offsetMinutes]);
                off += 6;
            }
        }
        chars[off] = this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeDateYYYMMDD8(final int year, final int month, final int dayOfMonth) {
        final int off = this.off;
        final int minCapacity = off + 10;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off] = this.quote;
        if (year < 0 || year > 9999) {
            throw new IllegalArgumentException("Only 4 digits numbers are supported. Provided: " + year);
        }
        final int y01 = year / 100;
        final int y2 = year - y01 * 100;
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 1 << 1), IOUtils.PACKED_DIGITS_UTF16[y01]);
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 3 << 1), IOUtils.PACKED_DIGITS_UTF16[y2]);
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 5 << 1), IOUtils.PACKED_DIGITS_UTF16[month]);
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 7 << 1), IOUtils.PACKED_DIGITS_UTF16[dayOfMonth]);
        chars[off + 9] = this.quote;
        this.off = off + 10;
    }
    
    @Override
    public final void writeDateYYYMMDD10(final int year, final int month, final int dayOfMonth) {
        int off = this.off;
        final int minCapacity = off + 13;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = this.quote;
        off = IOUtils.writeLocalDate(chars, off, year, month, dayOfMonth);
        chars[off] = this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeTimeHHMMSS8(final int hour, final int minute, final int second) {
        final int off = this.off;
        final int minCapacity = off + 10;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off] = (char)(byte)this.quote;
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 1 << 1), IOUtils.PACKED_DIGITS_UTF16[hour]);
        chars[off + 3] = ':';
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 4 << 1), IOUtils.PACKED_DIGITS_UTF16[minute]);
        chars[off + 6] = ':';
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 7 << 1), IOUtils.PACKED_DIGITS_UTF16[second]);
        chars[off + 9] = (char)(byte)this.quote;
        this.off = off + 10;
    }
    
    @Override
    public final void writeLocalTime(final LocalTime time) {
        int off = this.off;
        final int minCapacity = off + 20;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = this.quote;
        off = IOUtils.writeLocalTime(chars, off, time);
        chars[off] = this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeZonedDateTime(final ZonedDateTime dateTime) {
        if (dateTime == null) {
            this.writeNull();
            return;
        }
        final ZoneId zone = dateTime.getZone();
        String zoneId = zone.getId();
        final int zoneIdLength = zoneId.length();
        char firstZoneChar = '\0';
        int zoneSize;
        if (ZoneOffset.UTC == zone || (zoneIdLength <= 3 && ("UTC".equals(zoneId) || "Z".equals(zoneId)))) {
            zoneId = "Z";
            zoneSize = 1;
        }
        else if (zoneIdLength != 0 && ((firstZoneChar = zoneId.charAt(0)) == '+' || firstZoneChar == '-')) {
            zoneSize = zoneIdLength;
        }
        else {
            zoneSize = 2 + zoneIdLength;
        }
        int off = this.off;
        final int minCapacity = off + zoneSize + 38;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = this.quote;
        final LocalDate localDate = dateTime.toLocalDate();
        off = IOUtils.writeLocalDate(chars, off, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        chars[off++] = 'T';
        off = IOUtils.writeLocalTime(chars, off, dateTime.toLocalTime());
        if (zoneSize == 1) {
            chars[off++] = 'Z';
        }
        else if (firstZoneChar == '+' || firstZoneChar == '-') {
            zoneId.getChars(0, zoneIdLength, chars, off);
            off += zoneIdLength;
        }
        else {
            chars[off++] = '[';
            zoneId.getChars(0, zoneIdLength, chars, off);
            off += zoneIdLength;
            chars[off++] = ']';
        }
        chars[off] = this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeOffsetDateTime(final OffsetDateTime dateTime) {
        if (dateTime == null) {
            this.writeNull();
            return;
        }
        final ZoneOffset offset = dateTime.getOffset();
        String zoneId = offset.getId();
        final int zoneIdLength = zoneId.length();
        final boolean utc = ZoneOffset.UTC == offset || (zoneIdLength <= 3 && ("UTC".equals(zoneId) || "Z".equals(zoneId)));
        if (utc) {
            zoneId = "Z";
        }
        int off = this.off;
        final int minCapacity = off + zoneIdLength + 40;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        chars[off++] = this.quote;
        final LocalDateTime ldt = dateTime.toLocalDateTime();
        final LocalDate date = ldt.toLocalDate();
        off = IOUtils.writeLocalDate(chars, off, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        chars[off++] = 'T';
        off = IOUtils.writeLocalTime(chars, off, ldt.toLocalTime());
        if (utc) {
            chars[off++] = 'Z';
        }
        else {
            zoneId.getChars(0, zoneIdLength, chars, off);
            off += zoneIdLength;
        }
        chars[off] = this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeNameRaw(final byte[] bytes) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public final int flushTo(final OutputStream out) throws IOException {
        if (out == null) {
            throw new JSONException("out is nulll");
        }
        boolean ascii = true;
        for (int i = 0; i < this.off; ++i) {
            if (this.chars[i] >= '\u0080') {
                ascii = false;
                break;
            }
        }
        if (ascii) {
            final byte[] bytes = new byte[this.off];
            for (int j = 0; j < this.off; ++j) {
                bytes[j] = (byte)this.chars[j];
            }
            out.write(bytes);
            this.off = 0;
            return bytes.length;
        }
        final byte[] utf8 = new byte[this.off * 3];
        final int utf8Length = IOUtils.encodeUTF8(this.chars, 0, this.off, utf8, 0);
        out.write(utf8, 0, utf8Length);
        this.off = 0;
        return utf8Length;
    }
    
    @Override
    public final int flushTo(final OutputStream out, final Charset charset) throws IOException {
        if (this.off == 0) {
            return 0;
        }
        if (out == null) {
            throw new JSONException("out is null");
        }
        final byte[] bytes = this.getBytes(charset);
        out.write(bytes);
        this.off = 0;
        return bytes.length;
    }
    
    @Override
    public final String toString() {
        return new String(this.chars, 0, this.off);
    }
    
    @Override
    public final byte[] getBytes() {
        boolean ascii = true;
        for (int i = 0; i < this.off; ++i) {
            if (this.chars[i] >= '\u0080') {
                ascii = false;
                break;
            }
        }
        if (ascii) {
            final byte[] bytes = new byte[this.off];
            for (int j = 0; j < this.off; ++j) {
                bytes[j] = (byte)this.chars[j];
            }
            return bytes;
        }
        final byte[] utf8 = new byte[this.off * 3];
        final int utf8Length = IOUtils.encodeUTF8(this.chars, 0, this.off, utf8, 0);
        return Arrays.copyOf(utf8, utf8Length);
    }
    
    @Override
    public final int size() {
        return this.off;
    }
    
    @Override
    public final byte[] getBytes(Charset charset) {
        boolean ascii = true;
        for (int i = 0; i < this.off; ++i) {
            if (this.chars[i] >= '\u0080') {
                ascii = false;
                break;
            }
        }
        if (ascii && (charset == StandardCharsets.UTF_8 || charset == StandardCharsets.ISO_8859_1 || charset == StandardCharsets.US_ASCII)) {
            final byte[] bytes = new byte[this.off];
            for (int j = 0; j < this.off; ++j) {
                bytes[j] = (byte)this.chars[j];
            }
            return bytes;
        }
        final String str = new String(this.chars, 0, this.off);
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        return str.getBytes(charset);
    }
    
    @Override
    public final void writeRaw(final byte[] bytes) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public final void write(final JSONObject map) {
        if (map == null) {
            this.writeNull();
            return;
        }
        final long NONE_DIRECT_FEATURES = Feature.ReferenceDetection.mask | Feature.PrettyFormat.mask | Feature.NotWriteEmptyArray.mask | Feature.NotWriteDefaultValue.mask;
        if ((this.context.features & NONE_DIRECT_FEATURES) != 0x0L) {
            final ObjectWriter objectWriter = this.context.getObjectWriter(map.getClass());
            objectWriter.write(this, map, null, null, 0L);
            return;
        }
        if (this.off == this.chars.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.chars[this.off++] = '{';
        boolean first = true;
        for (final Map.Entry entry : map.entrySet()) {
            final Object value = entry.getValue();
            if (value == null && (this.context.features & Feature.WriteMapNullValue.mask) == 0x0L) {
                continue;
            }
            if (!first) {
                if (this.off == this.chars.length) {
                    this.ensureCapacity(this.off + 1);
                }
                this.chars[this.off++] = ',';
            }
            first = false;
            final Object key = entry.getKey();
            if (key instanceof String) {
                this.writeString((String)key);
            }
            else {
                this.writeAny(key);
            }
            if (this.off == this.chars.length) {
                this.ensureCapacity(this.off + 1);
            }
            this.chars[this.off++] = ':';
            if (value == null) {
                this.writeNull();
            }
            else {
                final Class<?> valueClass = value.getClass();
                if (valueClass == String.class) {
                    this.writeString((String)value);
                }
                else if (valueClass == Integer.class) {
                    this.writeInt32((Integer)value);
                }
                else if (valueClass == Long.class) {
                    this.writeInt64((Long)value);
                }
                else if (valueClass == Boolean.class) {
                    this.writeBool((boolean)value);
                }
                else if (valueClass == BigDecimal.class) {
                    this.writeDecimal((BigDecimal)value, 0L, null);
                }
                else if (valueClass == JSONArray.class) {
                    this.write((List)value);
                }
                else if (valueClass == JSONObject.class) {
                    this.write((JSONObject)value);
                }
                else {
                    final ObjectWriter objectWriter2 = this.context.getObjectWriter(valueClass, valueClass);
                    objectWriter2.write(this, value, null, null, 0L);
                }
            }
        }
        if (this.off == this.chars.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.chars[this.off++] = '}';
    }
    
    @Override
    public final void write(final List array) {
        if (array == null) {
            this.writeArrayNull();
            return;
        }
        final long NONE_DIRECT_FEATURES = Feature.ReferenceDetection.mask | Feature.PrettyFormat.mask | Feature.NotWriteEmptyArray.mask | Feature.NotWriteDefaultValue.mask;
        if ((this.context.features & NONE_DIRECT_FEATURES) != 0x0L) {
            final ObjectWriter objectWriter = this.context.getObjectWriter(array.getClass());
            objectWriter.write(this, array, null, null, 0L);
            return;
        }
        if (this.off == this.chars.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.chars[this.off++] = '[';
        boolean first = true;
        for (int i = 0; i < array.size(); ++i) {
            final Object o = array.get(i);
            if (!first) {
                if (this.off == this.chars.length) {
                    this.ensureCapacity(this.off + 1);
                }
                this.chars[this.off++] = ',';
            }
            first = false;
            if (o == null) {
                this.writeNull();
            }
            else {
                final Class<?> valueClass = o.getClass();
                if (valueClass == String.class) {
                    this.writeString((String)o);
                }
                else if (valueClass == Integer.class) {
                    this.writeInt32((Integer)o);
                }
                else if (valueClass == Long.class) {
                    this.writeInt64((Long)o);
                }
                else if (valueClass == Boolean.class) {
                    this.writeBool((boolean)o);
                }
                else if (valueClass == BigDecimal.class) {
                    this.writeDecimal((BigDecimal)o, 0L, null);
                }
                else if (valueClass == JSONArray.class) {
                    this.write((List)o);
                }
                else if (valueClass == JSONObject.class) {
                    this.write((JSONObject)o);
                }
                else {
                    final ObjectWriter objectWriter2 = this.context.getObjectWriter(valueClass, valueClass);
                    objectWriter2.write(this, o, null, null, 0L);
                }
            }
        }
        if (this.off == this.chars.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.chars[this.off++] = ']';
    }
    
    @Override
    public final void writeString(final char[] chars) {
        if (chars == null) {
            this.writeStringNull();
            return;
        }
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        boolean special = false;
        for (int i = 0; i < chars.length; ++i) {
            final char c = chars[i];
            if (c == '\\' || c == this.quote || c < ' ') {
                special = true;
                break;
            }
            if (browserSecure && (c == '<' || c == '>' || c == '(' || c == ')')) {
                special = true;
                break;
            }
        }
        if (!special) {
            final int minCapacity = this.off + chars.length + 2;
            if (minCapacity > this.chars.length) {
                this.ensureCapacity(minCapacity);
            }
            this.chars[this.off++] = this.quote;
            System.arraycopy(chars, 0, this.chars, this.off, chars.length);
            this.off += chars.length;
            this.chars[this.off++] = this.quote;
            return;
        }
        this.writeStringEscape(chars);
    }
    
    @Override
    public final void writeString(final char[] chars, final int off, final int len) {
        if (chars == null) {
            this.writeStringNull();
            return;
        }
        boolean special = false;
        for (int i = off; i < len; ++i) {
            if (chars[i] == '\\' || chars[i] == '\"') {
                special = true;
                break;
            }
        }
        if (!special) {
            final int minCapacity = this.off + len + 2;
            if (minCapacity >= this.chars.length) {
                this.ensureCapacity(minCapacity);
            }
            this.chars[this.off++] = this.quote;
            System.arraycopy(chars, off, this.chars, this.off, len);
            this.off += len;
            this.chars[this.off++] = this.quote;
            return;
        }
        this.writeStringEscape(new String(chars, off, len));
    }
    
    @Override
    public void writeBool(final boolean value) {
        final int minCapacity = this.off + 5;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final char[] chars = this.chars;
        int off = this.off;
        if ((this.context.features & Feature.WriteBooleanAsNumber.mask) != 0x0L) {
            chars[off++] = (value ? '1' : '0');
        }
        else {
            if (!value) {
                chars[off++] = 'f';
            }
            JDKUtils.UNSAFE.putLong(chars, JDKUtils.ARRAY_BYTE_BASE_OFFSET + (off << 1), value ? IOUtils.TRUE_64 : IOUtils.ALSE_64);
            off += 4;
        }
        this.off = off;
    }
    
    static {
        REF_PREF = "{\"$ref\":".toCharArray();
        final int[] digits = new int[256];
        for (int i = 0; i < 16; ++i) {
            final int hi = (short)((i < 10) ? (i + 48) : (i - 10 + 97));
            for (int j = 0; j < 16; ++j) {
                final int lo = (short)((j < 10) ? (j + 48) : (j - 10 + 97));
                digits[(i << 4) + j] = (hi | lo << 16);
            }
        }
        if (JDKUtils.BIG_ENDIAN) {
            for (int i = 0; i < digits.length; ++i) {
                digits[i] = Integer.reverseBytes(digits[i] << 8);
            }
        }
        HEX256 = digits;
    }
}
