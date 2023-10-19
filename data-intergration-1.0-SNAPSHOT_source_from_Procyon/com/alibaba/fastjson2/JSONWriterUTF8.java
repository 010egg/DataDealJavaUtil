// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.Iterator;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.util.Map;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.LocalDate;
import com.alibaba.fastjson2.util.DoubleToDecimal;
import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import java.nio.charset.StandardCharsets;

class JSONWriterUTF8 extends JSONWriter
{
    static final byte[] REF_PREF;
    static final short[] HEX256;
    final JSONFactory.CacheItem cacheItem;
    protected byte[] bytes;
    
    JSONWriterUTF8(final Context ctx) {
        super(ctx, null, false, StandardCharsets.UTF_8);
        final int cacheIndex = System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1;
        this.cacheItem = JSONFactory.CACHE_ITEMS[cacheIndex];
        byte[] bytes = JSONFactory.BYTES_UPDATER.getAndSet(this.cacheItem, null);
        if (bytes == null) {
            bytes = new byte[8192];
        }
        this.bytes = bytes;
    }
    
    @Override
    public final void writeNull() {
        final int minCapacity = this.off + 4;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        JDKUtils.UNSAFE.putInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.off, IOUtils.NULL_32);
        this.off += 4;
    }
    
    @Override
    public final void writeReference(final String path) {
        this.lastReference = path;
        this.writeRaw(JSONWriterUTF8.REF_PREF);
        this.writeString(path);
        final int off = this.off;
        if (off == this.bytes.length) {
            this.ensureCapacity(off + 1);
        }
        this.bytes[off] = 125;
        this.off = off + 1;
    }
    
    @Override
    public final void writeBase64(final byte[] value) {
        final int charsLen = (value.length - 1) / 3 + 1 << 2;
        int off = this.off;
        this.ensureCapacity(off + charsLen + 2);
        final byte[] bytes = this.bytes;
        bytes[off++] = (byte)this.quote;
        final int eLen = value.length / 3 * 3;
        int i;
        for (int s = 0; s < eLen; i = ((value[s++] & 0xFF) << 16 | (value[s++] & 0xFF) << 8 | (value[s++] & 0xFF)), bytes[off] = (byte)JSONFactory.CA[i >>> 18 & 0x3F], bytes[off + 1] = (byte)JSONFactory.CA[i >>> 12 & 0x3F], bytes[off + 2] = (byte)JSONFactory.CA[i >>> 6 & 0x3F], bytes[off + 3] = (byte)JSONFactory.CA[i & 0x3F], off += 4) {}
        final int left = value.length - eLen;
        if (left > 0) {
            i = ((value[eLen] & 0xFF) << 10 | ((left == 2) ? ((value[value.length - 1] & 0xFF) << 2) : 0));
            bytes[off] = (byte)JSONFactory.CA[i >> 12];
            bytes[off + 1] = (byte)JSONFactory.CA[i >>> 6 & 0x3F];
            bytes[off + 2] = (byte)((left == 2) ? ((byte)JSONFactory.CA[i & 0x3F]) : 61);
            bytes[off + 3] = 61;
            off += 4;
        }
        bytes[off] = (byte)this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeHex(final byte[] values) {
        if (values == null) {
            this.writeNull();
            return;
        }
        final int charsLen = values.length * 2 + 3;
        int off = this.off;
        this.ensureCapacity(off + charsLen + 2);
        final byte[] bytes = this.bytes;
        bytes[off] = 120;
        bytes[off + 1] = 39;
        off += 2;
        for (int i = 0; i < values.length; ++i) {
            final byte b = values[i];
            final int a = b & 0xFF;
            final int b2 = a >> 4;
            final int b3 = a & 0xF;
            bytes[off] = (byte)(b2 + ((b2 < 10) ? 48 : 55));
            bytes[off + 1] = (byte)(b3 + ((b3 < 10) ? 48 : 55));
            off += 2;
        }
        bytes[off] = 39;
        this.off = off + 1;
    }
    
    @Override
    public final void close() {
        final byte[] bytes = this.bytes;
        if (bytes.length > 1048576) {
            return;
        }
        JSONFactory.BYTES_UPDATER.lazySet(this.cacheItem, bytes);
    }
    
    @Override
    public final int size() {
        return this.off;
    }
    
    @Override
    public final byte[] getBytes() {
        return Arrays.copyOf(this.bytes, this.off);
    }
    
    @Override
    public final byte[] getBytes(final Charset charset) {
        if (charset == StandardCharsets.UTF_8) {
            return Arrays.copyOf(this.bytes, this.off);
        }
        final String str = this.toString();
        return str.getBytes(charset);
    }
    
    @Override
    public final int flushTo(final OutputStream to) throws IOException {
        final int off = this.off;
        if (off > 0) {
            to.write(this.bytes, 0, off);
            this.off = 0;
        }
        return off;
    }
    
    @Override
    protected final void write0(final char c) {
        final int off = this.off;
        if (off == this.bytes.length) {
            this.ensureCapacity(off + 1);
        }
        this.bytes[off] = (byte)c;
        this.off = off + 1;
    }
    
    @Override
    public final void writeColon() {
        final int off = this.off;
        if (off == this.bytes.length) {
            this.ensureCapacity(off + 1);
        }
        this.bytes[off] = 58;
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
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = 123;
        if (this.pretty) {
            ++this.indent;
            bytes[off++] = 10;
            for (int i = 0; i < this.indent; ++i) {
                bytes[off++] = 9;
            }
        }
        this.off = off;
    }
    
    @Override
    public final void endObject() {
        --this.level;
        int off = this.off;
        final int minCapacity = off + (this.pretty ? (2 + this.indent) : 1);
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (this.pretty) {
            --this.indent;
            bytes[off++] = 10;
            for (int i = 0; i < this.indent; ++i) {
                bytes[off++] = 9;
            }
        }
        bytes[off] = 125;
        this.off = off + 1;
        this.startObject = false;
    }
    
    @Override
    public final void writeComma() {
        this.startObject = false;
        int off = this.off;
        final int minCapacity = off + (this.pretty ? (2 + this.indent) : 1);
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = 44;
        if (this.pretty) {
            bytes[off++] = 10;
            for (int i = 0; i < this.indent; ++i) {
                bytes[off++] = 9;
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
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = 91;
        if (this.pretty) {
            ++this.indent;
            bytes[off++] = 10;
            for (int i = 0; i < this.indent; ++i) {
                bytes[off++] = 9;
            }
        }
        this.off = off;
    }
    
    @Override
    public final void endArray() {
        --this.level;
        int off = this.off;
        final int minCapacity = off + (this.pretty ? (2 + this.indent) : 1);
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (this.pretty) {
            --this.indent;
            bytes[off++] = 10;
            for (int i = 0; i < this.indent; ++i) {
                bytes[off++] = 9;
            }
        }
        bytes[off] = 93;
        this.off = off + 1;
        this.startObject = false;
    }
    
    @Override
    public void writeString(final String str) {
        if (str == null) {
            this.writeStringNull();
            return;
        }
        final char[] chars = JDKUtils.getCharArray(str);
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
        int off = this.off;
        int minCapacity = off + chars.length * 3 + 2;
        if (escapeNoneAscii || browserSecure) {
            minCapacity += chars.length * 3;
        }
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = (byte)this.quote;
        int i;
        for (i = 0; i < chars.length; ++i) {
            final char c0 = chars[i];
            if (c0 == this.quote || c0 == '\\' || c0 < ' ' || c0 > '\u007f') {
                break;
            }
            if (browserSecure) {
                if (c0 == '<' || c0 == '>' || c0 == '(') {
                    break;
                }
                if (c0 == ')') {
                    break;
                }
            }
            bytes[off++] = (byte)c0;
        }
        if (i == chars.length) {
            bytes[off] = (byte)this.quote;
            this.off = off + 1;
            return;
        }
        this.off = off;
        if (i < chars.length) {
            this.writeStringEscapedRest(chars, chars.length, browserSecure, escapeNoneAscii, i);
        }
        this.bytes[this.off++] = (byte)this.quote;
    }
    
    @Override
    public void writeStringLatin1(final byte[] values) {
        if (values == null) {
            this.writeStringNull();
            return;
        }
        boolean escape = false;
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        final byte quote = (byte)this.quote;
        for (int i = 0; i < values.length; ++i) {
            final byte c = values[i];
            if (c == quote || c == 92 || c < 32 || (browserSecure && (c == 60 || c == 62 || c == 40 || c == 41))) {
                escape = true;
                break;
            }
        }
        int off = this.off;
        if (!escape) {
            final int minCapacity = off + values.length + 2;
            if (minCapacity >= this.bytes.length) {
                this.ensureCapacity(minCapacity);
            }
            final byte[] bytes = this.bytes;
            bytes[off] = quote;
            System.arraycopy(values, 0, bytes, off + 1, values.length);
            off += values.length + 1;
            bytes[off] = quote;
            this.off = off + 1;
            return;
        }
        this.writeStringEscaped(values);
    }
    
    @Override
    public final void writeStringUTF16(final byte[] value) {
        if (value == null) {
            this.writeStringNull();
            return;
        }
        final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
        int off = this.off;
        int minCapacity = off + value.length * 4 + 2;
        if (escapeNoneAscii) {
            minCapacity += value.length * 2;
        }
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = (byte)this.quote;
        int valueOffset = 0;
        while (valueOffset < value.length) {
            byte b0 = value[valueOffset];
            byte b2 = value[valueOffset + 1];
            valueOffset += 2;
            if (b2 == 0 && b0 >= 0) {
                switch (b0) {
                    case 92: {
                        bytes[off + 1] = (bytes[off] = 92);
                        off += 2;
                        continue;
                    }
                    case 10: {
                        bytes[off] = 92;
                        bytes[off + 1] = 110;
                        off += 2;
                        continue;
                    }
                    case 13: {
                        bytes[off] = 92;
                        bytes[off + 1] = 114;
                        off += 2;
                        continue;
                    }
                    case 12: {
                        bytes[off] = 92;
                        bytes[off + 1] = 102;
                        off += 2;
                        continue;
                    }
                    case 8: {
                        bytes[off] = 92;
                        bytes[off + 1] = 98;
                        off += 2;
                        continue;
                    }
                    case 9: {
                        bytes[off] = 92;
                        bytes[off + 1] = 116;
                        off += 2;
                        continue;
                    }
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7: {
                        bytes[off] = 92;
                        bytes[off + 1] = 117;
                        bytes[off + 2] = 48;
                        bytes[off + 4] = (bytes[off + 3] = 48);
                        bytes[off + 5] = (byte)(48 + b0);
                        off += 6;
                        continue;
                    }
                    case 11:
                    case 14:
                    case 15: {
                        bytes[off] = 92;
                        bytes[off + 1] = 117;
                        bytes[off + 2] = 48;
                        bytes[off + 4] = (bytes[off + 3] = 48);
                        bytes[off + 5] = (byte)(97 + (b0 - 10));
                        off += 6;
                        continue;
                    }
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25: {
                        bytes[off] = 92;
                        bytes[off + 1] = 117;
                        bytes[off + 3] = (bytes[off + 2] = 48);
                        bytes[off + 4] = 49;
                        bytes[off + 5] = (byte)(48 + (b0 - 16));
                        off += 6;
                        continue;
                    }
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31: {
                        bytes[off] = 92;
                        bytes[off + 1] = 117;
                        bytes[off + 3] = (bytes[off + 2] = 48);
                        bytes[off + 4] = 49;
                        bytes[off + 5] = (byte)(97 + (b0 - 26));
                        off += 6;
                        continue;
                    }
                    default: {
                        if (b0 == this.quote) {
                            bytes[off] = 92;
                            bytes[off + 1] = (byte)this.quote;
                            off += 2;
                            continue;
                        }
                        bytes[off++] = b0;
                        continue;
                    }
                }
            }
            else {
                final char c = (char)((b0 & 0xFF) | (b2 & 0xFF) << 8);
                if (c < '\u0800') {
                    bytes[off] = (byte)(0xC0 | c >> 6);
                    bytes[off + 1] = (byte)(0x80 | (c & '?'));
                    off += 2;
                }
                else if (escapeNoneAscii) {
                    bytes[off] = 92;
                    bytes[off + 1] = 117;
                    bytes[off + 2] = (byte)JSONWriterUTF8.DIGITS[c >>> 12 & 0xF];
                    bytes[off + 3] = (byte)JSONWriterUTF8.DIGITS[c >>> 8 & 0xF];
                    bytes[off + 4] = (byte)JSONWriterUTF8.DIGITS[c >>> 4 & 0xF];
                    bytes[off + 5] = (byte)JSONWriterUTF8.DIGITS[c & '\u000f'];
                    off += 6;
                }
                else if (c >= '\ud800' && c < '\ue000') {
                    final int ip = valueOffset - 1;
                    if (c < '\udc00') {
                        int uc;
                        if (value.length - ip < 2) {
                            uc = -1;
                        }
                        else {
                            b0 = value[ip + 1];
                            b2 = value[ip + 2];
                            final char d = (char)((b0 & 0xFF) | (b2 & 0xFF) << 8);
                            if (d < '\udc00' || d >= '\ue000') {
                                bytes[off++] = 63;
                                continue;
                            }
                            valueOffset += 2;
                            uc = (c << 10) + d - 56613888;
                        }
                        if (uc < 0) {
                            bytes[off++] = 63;
                        }
                        else {
                            bytes[off] = (byte)(0xF0 | uc >> 18);
                            bytes[off + 1] = (byte)(0x80 | (uc >> 12 & 0x3F));
                            bytes[off + 2] = (byte)(0x80 | (uc >> 6 & 0x3F));
                            bytes[off + 3] = (byte)(0x80 | (uc & 0x3F));
                            off += 4;
                        }
                    }
                    else {
                        bytes[off++] = 63;
                    }
                }
                else {
                    bytes[off] = (byte)(0xE0 | c >> 12);
                    bytes[off + 1] = (byte)(0x80 | (c >> 6 & 0x3F));
                    bytes[off + 2] = (byte)(0x80 | (c & '?'));
                    off += 3;
                }
            }
        }
        bytes[off] = (byte)this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeString(final char[] chars) {
        if (chars == null) {
            this.writeStringNull();
            return;
        }
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
        int off = this.off;
        int minCapacity = off + chars.length * 3 + 2;
        if (escapeNoneAscii || browserSecure) {
            minCapacity += chars.length * 3;
        }
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = (byte)this.quote;
        int i;
        for (i = 0; i < chars.length; ++i) {
            final char c = chars[i];
            if (c == this.quote || c == '\\' || c < ' ' || c > '\u007f') {
                break;
            }
            if (browserSecure) {
                if (c == '<' || c == '>' || c == '(') {
                    break;
                }
                if (c == ')') {
                    break;
                }
            }
            bytes[off++] = (byte)c;
        }
        this.off = off;
        final int rest = chars.length - i;
        minCapacity = off + rest * 6 + 2;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        if (i < chars.length) {
            this.writeStringEscapedRest(chars, chars.length, browserSecure, escapeNoneAscii, i);
        }
        this.bytes[this.off++] = (byte)this.quote;
    }
    
    @Override
    public final void writeString(final char[] chars, final int stroff, final int strlen) {
        if (chars != null) {
            final int end = stroff + strlen;
            final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
            final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
            int off = this.off;
            int minCapacity = off + strlen * 3 + 2;
            if (escapeNoneAscii || browserSecure) {
                minCapacity += strlen * 3;
            }
            if (minCapacity >= this.bytes.length) {
                this.ensureCapacity(minCapacity);
            }
            final byte[] bytes = this.bytes;
            bytes[off++] = (byte)this.quote;
            int i;
            for (i = stroff; i < end; ++i) {
                final char c0 = chars[i];
                if (c0 == this.quote || c0 == '\\' || c0 < ' ' || c0 > '\u007f') {
                    break;
                }
                if (browserSecure) {
                    if (c0 == '<' || c0 == '>' || c0 == '(') {
                        break;
                    }
                    if (c0 == ')') {
                        break;
                    }
                }
                bytes[off++] = (byte)c0;
            }
            this.off = off;
            final int rest = end - i;
            minCapacity = off + rest * 6 + 2;
            if (minCapacity >= this.bytes.length) {
                this.ensureCapacity(minCapacity);
            }
            if (i < end) {
                this.writeStringEscapedRest(chars, end, browserSecure, escapeNoneAscii, i);
            }
            this.bytes[this.off++] = (byte)this.quote;
            return;
        }
        if (this.isEnabled(Feature.NullAsDefaultValue.mask | Feature.WriteNullStringAsEmpty.mask)) {
            this.writeString("");
            return;
        }
        this.writeNull();
    }
    
    protected final void writeStringEscaped(final byte[] values) {
        final int minCapacity = this.off + values.length * 4 + 2;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        final byte[] bytes = this.bytes;
        int off = this.off;
        bytes[off++] = (byte)this.quote;
        for (int i = 0; i < values.length; ++i) {
            final byte ch = values[i];
            switch (ch) {
                case 92: {
                    bytes[off + 1] = (bytes[off] = 92);
                    off += 2;
                    break;
                }
                case 10: {
                    bytes[off] = 92;
                    bytes[off + 1] = 110;
                    off += 2;
                    break;
                }
                case 13: {
                    bytes[off] = 92;
                    bytes[off + 1] = 114;
                    off += 2;
                    break;
                }
                case 12: {
                    bytes[off] = 92;
                    bytes[off + 1] = 102;
                    off += 2;
                    break;
                }
                case 8: {
                    bytes[off] = 92;
                    bytes[off + 1] = 98;
                    off += 2;
                    break;
                }
                case 9: {
                    bytes[off] = 92;
                    bytes[off + 1] = 116;
                    off += 2;
                    break;
                }
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7: {
                    bytes[off] = 92;
                    bytes[off + 1] = 117;
                    bytes[off + 2] = 48;
                    bytes[off + 4] = (bytes[off + 3] = 48);
                    bytes[off + 5] = (byte)(48 + ch);
                    off += 6;
                    break;
                }
                case 11:
                case 14:
                case 15: {
                    bytes[off] = 92;
                    bytes[off + 1] = 117;
                    bytes[off + 2] = 48;
                    bytes[off + 4] = (bytes[off + 3] = 48);
                    bytes[off + 5] = (byte)(97 + (ch - 10));
                    off += 6;
                    break;
                }
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25: {
                    bytes[off] = 92;
                    bytes[off + 1] = 117;
                    bytes[off + 3] = (bytes[off + 2] = 48);
                    bytes[off + 4] = 49;
                    bytes[off + 5] = (byte)(48 + (ch - 16));
                    off += 6;
                    break;
                }
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31: {
                    bytes[off] = 92;
                    bytes[off + 1] = 117;
                    bytes[off + 3] = (bytes[off + 2] = 48);
                    bytes[off + 4] = 49;
                    bytes[off + 5] = (byte)(97 + (ch - 26));
                    off += 6;
                    break;
                }
                case 40:
                case 41:
                case 60:
                case 62: {
                    if (browserSecure) {
                        bytes[off] = 92;
                        bytes[off + 1] = 117;
                        bytes[off + 3] = (bytes[off + 2] = 48);
                        bytes[off + 4] = (byte)JSONWriterUTF8.DIGITS[ch >>> 4 & 0xF];
                        bytes[off + 5] = (byte)JSONWriterUTF8.DIGITS[ch & 0xF];
                        off += 6;
                        break;
                    }
                    bytes[off++] = ch;
                    break;
                }
                default: {
                    if (ch == this.quote) {
                        bytes[off] = 92;
                        bytes[off + 1] = (byte)this.quote;
                        off += 2;
                        break;
                    }
                    if (ch < 0) {
                        final int c = ch & 0xFF;
                        bytes[off] = (byte)(0xC0 | c >> 6);
                        bytes[off + 1] = (byte)(0x80 | (c & 0x3F));
                        off += 2;
                        break;
                    }
                    bytes[off++] = ch;
                    break;
                }
            }
        }
        bytes[off] = (byte)this.quote;
        this.off = off + 1;
    }
    
    protected final void writeStringEscapedRest(final char[] chars, final int end, final boolean browserSecure, final boolean escapeNoneAscii, int i) {
        final int rest = chars.length - i;
        final int minCapacity = this.off + rest * 6 + 2;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        int off = this.off;
        while (i < end) {
            final char ch = chars[i];
            Label_1399: {
                if (ch <= '\u007f') {
                    switch (ch) {
                        case '\\': {
                            bytes[off + 1] = (bytes[off] = 92);
                            off += 2;
                            break;
                        }
                        case '\n': {
                            bytes[off] = 92;
                            bytes[off + 1] = 110;
                            off += 2;
                            break;
                        }
                        case '\r': {
                            bytes[off] = 92;
                            bytes[off + 1] = 114;
                            off += 2;
                            break;
                        }
                        case '\f': {
                            bytes[off] = 92;
                            bytes[off + 1] = 102;
                            off += 2;
                            break;
                        }
                        case '\b': {
                            bytes[off] = 92;
                            bytes[off + 1] = 98;
                            off += 2;
                            break;
                        }
                        case '\t': {
                            bytes[off] = 92;
                            bytes[off + 1] = 116;
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
                            bytes[off] = 92;
                            bytes[off + 1] = 117;
                            bytes[off + 2] = 48;
                            bytes[off + 4] = (bytes[off + 3] = 48);
                            bytes[off + 5] = (byte)('0' + ch);
                            off += 6;
                            break;
                        }
                        case '\u000b':
                        case '\u000e':
                        case '\u000f': {
                            bytes[off] = 92;
                            bytes[off + 1] = 117;
                            bytes[off + 2] = 48;
                            bytes[off + 4] = (bytes[off + 3] = 48);
                            bytes[off + 5] = (byte)(97 + (ch - '\n'));
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
                            bytes[off] = 92;
                            bytes[off + 1] = 117;
                            bytes[off + 3] = (bytes[off + 2] = 48);
                            bytes[off + 4] = 49;
                            bytes[off + 5] = (byte)(48 + (ch - '\u0010'));
                            off += 6;
                            break;
                        }
                        case '\u001a':
                        case '\u001b':
                        case '\u001c':
                        case '\u001d':
                        case '\u001e':
                        case '\u001f': {
                            bytes[off] = 92;
                            bytes[off + 1] = 117;
                            bytes[off + 3] = (bytes[off + 2] = 48);
                            bytes[off + 4] = 49;
                            bytes[off + 5] = (byte)(97 + (ch - '\u001a'));
                            off += 6;
                            break;
                        }
                        case '(':
                        case ')':
                        case '<':
                        case '>': {
                            if (browserSecure) {
                                bytes[off] = 92;
                                bytes[off + 1] = 117;
                                bytes[off + 3] = (bytes[off + 2] = 48);
                                bytes[off + 4] = (byte)JSONWriterUTF8.DIGITS[ch >>> 4 & 0xF];
                                bytes[off + 5] = (byte)JSONWriterUTF8.DIGITS[ch & '\u000f'];
                                off += 6;
                                break;
                            }
                            bytes[off++] = (byte)ch;
                            break;
                        }
                        default: {
                            if (ch == this.quote) {
                                bytes[off] = 92;
                                bytes[off + 1] = (byte)this.quote;
                                off += 2;
                                break;
                            }
                            bytes[off++] = (byte)ch;
                            break;
                        }
                    }
                }
                else if (escapeNoneAscii) {
                    bytes[off] = 92;
                    bytes[off + 1] = 117;
                    bytes[off + 2] = (byte)JSONWriterUTF8.DIGITS[ch >>> 12 & 0xF];
                    bytes[off + 3] = (byte)JSONWriterUTF8.DIGITS[ch >>> 8 & 0xF];
                    bytes[off + 4] = (byte)JSONWriterUTF8.DIGITS[ch >>> 4 & 0xF];
                    bytes[off + 5] = (byte)JSONWriterUTF8.DIGITS[ch & '\u000f'];
                    off += 6;
                }
                else if (ch >= '\ud800' && ch < '\ue000') {
                    if (ch < '\udc00') {
                        int uc;
                        if (chars.length - i < 2) {
                            uc = -1;
                        }
                        else {
                            final char d = chars[i + 1];
                            if (d < '\udc00' || d >= '\ue000') {
                                bytes[off++] = 63;
                                break Label_1399;
                            }
                            uc = (ch << 10) + d - 56613888;
                        }
                        if (uc < 0) {
                            bytes[off++] = 63;
                        }
                        else {
                            bytes[off] = (byte)(0xF0 | uc >> 18);
                            bytes[off + 1] = (byte)(0x80 | (uc >> 12 & 0x3F));
                            bytes[off + 2] = (byte)(0x80 | (uc >> 6 & 0x3F));
                            bytes[off + 3] = (byte)(0x80 | (uc & 0x3F));
                            off += 4;
                            ++i;
                        }
                    }
                    else {
                        bytes[off++] = 63;
                    }
                }
                else if (ch > '\u07ff') {
                    bytes[off] = (byte)(0xE0 | (ch >> 12 & 0xF));
                    bytes[off + 1] = (byte)(0x80 | (ch >> 6 & 0x3F));
                    bytes[off + 2] = (byte)(0x80 | (ch & '?'));
                    off += 3;
                }
                else {
                    bytes[off] = (byte)(0xC0 | (ch >> 6 & 0x1F));
                    bytes[off + 1] = (byte)(0x80 | (ch & '?'));
                    off += 2;
                }
            }
            ++i;
        }
        this.off = off;
    }
    
    @Override
    public final void writeString(final char[] chars, final int offset, final int len, final boolean quoted) {
        final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
        int minCapacity = this.off + chars.length * 3 + 2;
        if (escapeNoneAscii) {
            minCapacity += len * 3;
        }
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        int off = this.off;
        if (quoted) {
            bytes[off++] = (byte)this.quote;
        }
        int end;
        int i;
        for (end = offset + len, i = offset; i < end; ++i) {
            final char c0 = chars[i];
            if (c0 == this.quote || c0 == '\\' || c0 < ' ') {
                break;
            }
            if (c0 > '\u007f') {
                break;
            }
            bytes[off++] = (byte)c0;
        }
        if (i == end) {
            if (quoted) {
                bytes[off++] = (byte)this.quote;
            }
            this.off = off;
            return;
        }
        while (i < len) {
            final char ch = chars[i];
            Label_1457: {
                if (ch <= '\u007f') {
                    switch (ch) {
                        case '\\': {
                            bytes[off + 1] = (bytes[off] = 92);
                            off += 2;
                            break;
                        }
                        case '\n': {
                            bytes[off] = 92;
                            bytes[off + 1] = 110;
                            off += 2;
                            break;
                        }
                        case '\r': {
                            bytes[off] = 92;
                            bytes[off + 1] = 114;
                            off += 2;
                            break;
                        }
                        case '\f': {
                            bytes[off] = 92;
                            bytes[off + 1] = 102;
                            off += 2;
                            break;
                        }
                        case '\b': {
                            bytes[off] = 92;
                            bytes[off + 1] = 98;
                            off += 2;
                            break;
                        }
                        case '\t': {
                            bytes[off] = 92;
                            bytes[off + 1] = 116;
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
                            bytes[off] = 92;
                            bytes[off + 1] = 117;
                            bytes[off + 2] = 48;
                            bytes[off + 4] = (bytes[off + 3] = 48);
                            bytes[off + 5] = (byte)('0' + ch);
                            off += 6;
                            break;
                        }
                        case '\u000b':
                        case '\u000e':
                        case '\u000f': {
                            bytes[off] = 92;
                            bytes[off + 1] = 117;
                            bytes[off + 2] = 48;
                            bytes[off + 4] = (bytes[off + 3] = 48);
                            bytes[off + 5] = (byte)(97 + (ch - '\n'));
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
                            bytes[off] = 92;
                            bytes[off + 1] = 117;
                            bytes[off + 3] = (bytes[off + 2] = 48);
                            bytes[off + 4] = 49;
                            bytes[off + 5] = (byte)(48 + (ch - '\u0010'));
                            off += 6;
                            break;
                        }
                        case '\u001a':
                        case '\u001b':
                        case '\u001c':
                        case '\u001d':
                        case '\u001e':
                        case '\u001f': {
                            bytes[off] = 92;
                            bytes[off + 1] = 117;
                            bytes[off + 3] = (bytes[off + 2] = 48);
                            bytes[off + 4] = 49;
                            bytes[off + 5] = (byte)(97 + (ch - '\u001a'));
                            off += 6;
                            break;
                        }
                        default: {
                            if (ch == this.quote) {
                                bytes[off] = 92;
                                bytes[off + 1] = (byte)this.quote;
                                off += 2;
                                break;
                            }
                            bytes[off++] = (byte)ch;
                            break;
                        }
                    }
                }
                else if (escapeNoneAscii) {
                    bytes[off] = 92;
                    bytes[off + 1] = 117;
                    bytes[off + 2] = (byte)JSONWriterUTF8.DIGITS[ch >>> 12 & 0xF];
                    bytes[off + 3] = (byte)JSONWriterUTF8.DIGITS[ch >>> 8 & 0xF];
                    bytes[off + 4] = (byte)JSONWriterUTF8.DIGITS[ch >>> 4 & 0xF];
                    bytes[off + 5] = (byte)JSONWriterUTF8.DIGITS[ch & '\u000f'];
                    off += 6;
                }
                else if (ch >= '\ud800' && ch < '\ue000') {
                    if (ch < '\udc00') {
                        int uc;
                        if (chars.length - i < 2) {
                            uc = -1;
                        }
                        else {
                            final char d = chars[i + 1];
                            if (d < '\udc00' || d >= '\ue000') {
                                bytes[off++] = 63;
                                break Label_1457;
                            }
                            uc = (ch << 10) + d - 56613888;
                        }
                        if (uc < 0) {
                            bytes[off++] = 63;
                        }
                        else {
                            bytes[off] = (byte)(0xF0 | uc >> 18);
                            bytes[off + 1] = (byte)(0x80 | (uc >> 12 & 0x3F));
                            bytes[off + 2] = (byte)(0x80 | (uc >> 6 & 0x3F));
                            bytes[off + 3] = (byte)(0x80 | (uc & 0x3F));
                            off += 4;
                            ++i;
                        }
                    }
                    else {
                        bytes[off++] = 63;
                    }
                }
                else if (ch > '\u07ff') {
                    bytes[off] = (byte)(0xE0 | (ch >> 12 & 0xF));
                    bytes[off + 1] = (byte)(0x80 | (ch >> 6 & 0x3F));
                    bytes[off + 2] = (byte)(0x80 | (ch & '?'));
                    off += 3;
                }
                else {
                    bytes[off] = (byte)(0xC0 | (ch >> 6 & 0x1F));
                    bytes[off + 1] = (byte)(0x80 | (ch & '?'));
                    off += 2;
                }
            }
            ++i;
        }
        if (quoted) {
            bytes[off++] = (byte)this.quote;
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
    public final void writeChar(final char ch) {
        int off = this.off;
        final int minCapacity = off + 8;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = (byte)this.quote;
        if (ch <= '\u007f') {
            switch (ch) {
                case '\\': {
                    bytes[off + 1] = (bytes[off] = 92);
                    off += 2;
                    break;
                }
                case '\n': {
                    bytes[off] = 92;
                    bytes[off + 1] = 110;
                    off += 2;
                    break;
                }
                case '\r': {
                    bytes[off] = 92;
                    bytes[off + 1] = 114;
                    off += 2;
                    break;
                }
                case '\f': {
                    bytes[off] = 92;
                    bytes[off + 1] = 102;
                    off += 2;
                    break;
                }
                case '\b': {
                    bytes[off] = 92;
                    bytes[off + 1] = 98;
                    off += 2;
                    break;
                }
                case '\t': {
                    bytes[off] = 92;
                    bytes[off + 1] = 116;
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
                    bytes[off] = 92;
                    bytes[off + 1] = 117;
                    bytes[off + 2] = 48;
                    bytes[off + 4] = (bytes[off + 3] = 48);
                    bytes[off + 5] = (byte)('0' + ch);
                    off += 6;
                    break;
                }
                case '\u000b':
                case '\u000e':
                case '\u000f': {
                    bytes[off] = 92;
                    bytes[off + 1] = 117;
                    bytes[off + 2] = 48;
                    bytes[off + 4] = (bytes[off + 3] = 48);
                    bytes[off + 5] = (byte)(97 + (ch - '\n'));
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
                    bytes[off] = 92;
                    bytes[off + 1] = 117;
                    bytes[off + 3] = (bytes[off + 2] = 48);
                    bytes[off + 4] = 49;
                    bytes[off + 5] = (byte)(48 + (ch - '\u0010'));
                    off += 6;
                    break;
                }
                case '\u001a':
                case '\u001b':
                case '\u001c':
                case '\u001d':
                case '\u001e':
                case '\u001f': {
                    bytes[off] = 92;
                    bytes[off + 1] = 117;
                    bytes[off + 3] = (bytes[off + 2] = 48);
                    bytes[off + 4] = 49;
                    bytes[off + 5] = (byte)(97 + (ch - '\u001a'));
                    off += 6;
                    break;
                }
                default: {
                    if (ch == this.quote) {
                        bytes[off] = 92;
                        bytes[off + 1] = (byte)this.quote;
                        off += 2;
                        break;
                    }
                    bytes[off++] = (byte)ch;
                    break;
                }
            }
        }
        else {
            if (ch >= '\ud800' && ch < '\ue000') {
                throw new JSONException("illegal char " + ch);
            }
            if (ch > '\u07ff') {
                bytes[off] = (byte)(0xE0 | (ch >> 12 & 0xF));
                bytes[off + 1] = (byte)(0x80 | (ch >> 6 & 0x3F));
                bytes[off + 2] = (byte)(0x80 | (ch & '?'));
                off += 3;
            }
            else {
                bytes[off] = (byte)(0xC0 | (ch >> 6 & 0x1F));
                bytes[off + 1] = (byte)(0x80 | (ch & '?'));
                off += 2;
            }
        }
        bytes[off] = (byte)this.quote;
        this.off = off + 1;
    }
    
    static int packDigits(final int b0, final int b1) {
        final int v = JSONWriterUTF8.HEX256[b0 & 0xFF] | JSONWriterUTF8.HEX256[b1 & 0xFF] << 16;
        return JDKUtils.BIG_ENDIAN ? Integer.reverseBytes(v) : v;
    }
    
    static long packDigits(final int b0, final int b1, final int b2, final int b3) {
        final short[] digits = JSONWriterUTF8.HEX256;
        final long v = (long)digits[b0 & 0xFF] | (long)digits[b1 & 0xFF] << 16 | (long)digits[b2 & 0xFF] << 32 | (long)digits[b3 & 0xFF] << 48;
        return JDKUtils.BIG_ENDIAN ? Long.reverseBytes(v) : v;
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
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] buf = this.bytes;
        final int off = this.off;
        buf[off] = 34;
        JDKUtils.UNSAFE.putLong(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, packDigits((int)(msb >> 56), (int)(msb >> 48), (int)(msb >> 40), (int)(msb >> 32)));
        buf[off + 9] = 45;
        JDKUtils.UNSAFE.putLong(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 10L, packDigits((int)msb >> 24, (int)msb >> 16));
        buf[off + 14] = 45;
        JDKUtils.UNSAFE.putLong(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 15L, packDigits((int)msb >> 8, (int)msb));
        buf[off + 19] = 45;
        JDKUtils.UNSAFE.putLong(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 20L, packDigits((int)(lsb >> 56), (int)(lsb >> 48)));
        buf[off + 24] = 45;
        JDKUtils.UNSAFE.putLong(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 25L, packDigits((int)(lsb >> 40), (int)(lsb >> 32), (int)lsb >> 24, (int)lsb >> 16));
        JDKUtils.UNSAFE.putLong(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 33L, packDigits((int)lsb >> 8, (int)lsb));
        buf[off + 37] = 34;
        this.off += 38;
    }
    
    @Override
    public final void writeRaw(final String str) {
        final char[] chars = JDKUtils.getCharArray(str);
        int off = this.off;
        final int minCapacity = off + chars.length * 3;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        for (int i = 0; i < chars.length; ++i) {
            final char c = chars[i];
            if (c >= '\u0001' && c <= '\u007f') {
                bytes[off++] = (byte)c;
            }
            else if (c > '\u07ff') {
                bytes[off] = (byte)(0xE0 | (c >> 12 & 0xF));
                bytes[off + 1] = (byte)(0x80 | (c >> 6 & 0x3F));
                bytes[off + 2] = (byte)(0x80 | (c & '?'));
                off += 3;
            }
            else {
                bytes[off] = (byte)(0xC0 | (c >> 6 & 0x1F));
                bytes[off + 1] = (byte)(0x80 | (c & '?'));
                off += 2;
            }
        }
        this.off = off;
    }
    
    @Override
    public final void writeRaw(final byte[] bytes) {
        final int minCapacity = this.off + bytes.length;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        System.arraycopy(bytes, 0, this.bytes, this.off, bytes.length);
        this.off += bytes.length;
    }
    
    @Override
    public final void writeNameRaw(final byte[] name) {
        int off = this.off;
        final int minCapacity = off + name.length + 2 + this.indent;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            final byte[] bytes = this.bytes;
            bytes[off++] = 44;
            if (this.pretty) {
                bytes[off++] = 10;
                for (int i = 0; i < this.indent; ++i) {
                    bytes[off++] = 9;
                }
            }
        }
        System.arraycopy(name, 0, this.bytes, off, name.length);
        this.off = off + name.length;
    }
    
    @Override
    public void writeName3Raw(final long name) {
        int off = this.off;
        final int minCapacity = off + 10 + this.indent;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            bytes[off++] = 44;
            if (this.pretty) {
                bytes[off++] = 10;
                for (int i = 0; i < this.indent; ++i) {
                    bytes[off++] = 9;
                }
            }
        }
        JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off, name);
        this.off = off + 6;
    }
    
    @Override
    public void writeName4Raw(final long name) {
        int off = this.off;
        final int minCapacity = off + 10 + this.indent;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            bytes[off++] = 44;
            if (this.pretty) {
                bytes[off++] = 10;
                for (int i = 0; i < this.indent; ++i) {
                    bytes[off++] = 9;
                }
            }
        }
        JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off, name);
        this.off = off + 7;
    }
    
    @Override
    public void writeName5Raw(final long name) {
        int off = this.off;
        final int minCapacity = off + 10 + this.indent;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            bytes[off++] = 44;
            if (this.pretty) {
                bytes[off++] = 10;
                for (int i = 0; i < this.indent; ++i) {
                    bytes[off++] = 9;
                }
            }
        }
        JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off, name);
        this.off = off + 8;
    }
    
    @Override
    public void writeName6Raw(final long name) {
        int off = this.off;
        final int minCapacity = off + 11 + this.indent;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            bytes[off++] = 44;
            if (this.pretty) {
                bytes[off++] = 10;
                for (int i = 0; i < this.indent; ++i) {
                    bytes[off++] = 9;
                }
            }
        }
        JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off, name);
        bytes[off + 8] = 58;
        this.off = off + 9;
    }
    
    @Override
    public void writeName7Raw(final long name) {
        int off = this.off;
        final int minCapacity = off + 12 + this.indent;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            bytes[off++] = 44;
            if (this.pretty) {
                bytes[off++] = 10;
                for (int i = 0; i < this.indent; ++i) {
                    bytes[off++] = 9;
                }
            }
        }
        JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off, name);
        bytes[off + 8] = (byte)this.quote;
        bytes[off + 9] = 58;
        this.off = off + 10;
    }
    
    @Override
    public void writeName8Raw(final long name) {
        int off = this.off;
        final int minCapacity = off + 13 + this.indent;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            bytes[off++] = 44;
            if (this.pretty) {
                bytes[off++] = 10;
                for (int i = 0; i < this.indent; ++i) {
                    bytes[off++] = 9;
                }
            }
        }
        bytes[off] = (byte)this.quote;
        JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, name);
        bytes[off + 9] = (byte)this.quote;
        bytes[off + 10] = 58;
        this.off = off + 11;
    }
    
    @Override
    public void writeName9Raw(final long name0, final int name1) {
        int off = this.off;
        final int minCapacity = off + 14 + this.indent;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            bytes[off++] = 44;
            if (this.pretty) {
                bytes[off++] = 10;
                for (int i = 0; i < this.indent; ++i) {
                    bytes[off++] = 9;
                }
            }
        }
        JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off, name0);
        JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 8L, name1);
        this.off = off + 12;
    }
    
    @Override
    public final void writeRaw(final char ch) {
        if (ch > '\u0080') {
            throw new JSONException("not support " + ch);
        }
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = (byte)ch;
    }
    
    @Override
    public final void writeRaw(final char c0, final char c1) {
        if (c0 > '\u0080') {
            throw new JSONException("not support " + c0);
        }
        if (c1 > '\u0080') {
            throw new JSONException("not support " + c1);
        }
        final int off = this.off;
        if (off + 1 >= this.bytes.length) {
            this.ensureCapacity(off + 2);
        }
        this.bytes[off] = (byte)c0;
        this.bytes[off + 1] = (byte)c1;
        this.off = off + 2;
    }
    
    @Override
    public final void writeNameRaw(final byte[] bytes, final int off, final int len) {
        final int minCapacity = this.off + len + 2 + this.indent;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            this.writeComma();
        }
        System.arraycopy(bytes, off, this.bytes, this.off, len);
        this.off += len;
    }
    
    final void ensureCapacity(final int minCapacity) {
        if (minCapacity >= this.bytes.length) {
            final int oldCapacity = this.bytes.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            if (newCapacity - minCapacity < 0) {
                newCapacity = minCapacity;
            }
            if (newCapacity - this.maxArraySize > 0) {
                throw new OutOfMemoryError();
            }
            this.bytes = Arrays.copyOf(this.bytes, newCapacity);
        }
    }
    
    @Override
    public final void writeInt32(final int[] values) {
        if (values == null) {
            this.writeNull();
            return;
        }
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        final int minCapacity = off + values.length * 13 + 2;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = 91;
        for (int i = 0; i < values.length; ++i) {
            if (i != 0) {
                bytes[off++] = 44;
            }
            if (writeAsString) {
                bytes[off++] = (byte)this.quote;
            }
            off = IOUtils.writeInt32(bytes, off, values[i]);
            if (writeAsString) {
                bytes[off++] = (byte)this.quote;
            }
        }
        bytes[off] = 93;
        this.off = off + 1;
    }
    
    @Override
    public final void writeInt8(final byte i) {
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        final int minCapacity = off + 5;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (writeAsString) {
            bytes[off++] = (byte)this.quote;
        }
        off = IOUtils.writeInt32(bytes, off, i);
        if (writeAsString) {
            bytes[off++] = (byte)this.quote;
        }
        this.off = off;
    }
    
    @Override
    public final void writeInt16(final short i) {
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        final int minCapacity = off + 7;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (writeAsString) {
            bytes[off++] = (byte)this.quote;
        }
        off = IOUtils.writeInt32(bytes, off, i);
        if (writeAsString) {
            bytes[off++] = (byte)this.quote;
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
    public final void writeInt32(final int i) {
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        final int minCapacity = off + 13;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (writeAsString) {
            bytes[off++] = (byte)this.quote;
        }
        off = IOUtils.writeInt32(bytes, off, i);
        if (writeAsString) {
            bytes[off++] = (byte)this.quote;
        }
        this.off = off;
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
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = 91;
        for (int i = 0; i < size; ++i) {
            if (i != 0) {
                bytes[off++] = 44;
            }
            final Number item = values.get(i);
            if (item == null) {
                JDKUtils.UNSAFE.putInt(off, JDKUtils.ARRAY_BYTE_BASE_OFFSET, IOUtils.NULL_32);
                off += 4;
            }
            else {
                final int v = item.intValue();
                if (writeAsString) {
                    bytes[off++] = (byte)this.quote;
                }
                off = IOUtils.writeInt32(bytes, off, v);
                if (writeAsString) {
                    bytes[off++] = (byte)this.quote;
                }
            }
        }
        bytes[off] = 93;
        this.off = off + 1;
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
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = 91;
        for (int i = 0; i < values.length; ++i) {
            if (i != 0) {
                bytes[off++] = 44;
            }
            final long v = values[i];
            final boolean writeAsString = nonStringAsString || (browserCompatible && v <= 9007199254740991L && v >= -9007199254740991L);
            if (writeAsString) {
                bytes[off++] = (byte)this.quote;
            }
            off = IOUtils.writeInt64(bytes, off, v);
            if (writeAsString) {
                bytes[off++] = (byte)this.quote;
            }
        }
        bytes[off] = 93;
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
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = 91;
        for (int i = 0; i < size; ++i) {
            if (i != 0) {
                bytes[off++] = 44;
            }
            final Long item = values.get(i);
            if (item == null) {
                JDKUtils.UNSAFE.putInt(off, JDKUtils.ARRAY_BYTE_BASE_OFFSET, IOUtils.NULL_32);
                off += 4;
            }
            else {
                final long v = item;
                final boolean writeAsString = nonStringAsString || (browserCompatible && v <= 9007199254740991L && v >= -9007199254740991L);
                if (writeAsString) {
                    bytes[off++] = (byte)this.quote;
                }
                off = IOUtils.writeInt64(bytes, off, v);
                if (writeAsString) {
                    bytes[off++] = (byte)this.quote;
                }
            }
        }
        bytes[off] = 93;
        this.off = off + 1;
    }
    
    @Override
    public final void writeInt64(final long i) {
        final long features = this.context.features;
        final boolean writeAsString = (features & (Feature.WriteNonStringValueAsString.mask | Feature.WriteLongAsString.mask)) != 0x0L || ((features & Feature.BrowserCompatible.mask) != 0x0L && (i > 9007199254740991L || i < -9007199254740991L));
        int off = this.off;
        final int minCapacity = off + 23;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (writeAsString) {
            bytes[off++] = (byte)this.quote;
        }
        off = IOUtils.writeInt64(bytes, off, i);
        if (writeAsString) {
            bytes[off++] = (byte)this.quote;
        }
        else if ((features & Feature.WriteClassName.mask) != 0x0L && (features & Feature.NotWriteNumberClassName.mask) == 0x0L && i >= -2147483648L && i <= 2147483647L) {
            bytes[off++] = 76;
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
        final int minCapacity = off + 17;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        if (writeAsString) {
            this.bytes[off++] = 34;
        }
        final int len = DoubleToDecimal.toString(value, this.bytes, off, true);
        off += len;
        if (writeAsString) {
            this.bytes[off++] = 34;
        }
        this.off = off;
    }
    
    @Override
    public final void writeDouble(final double value) {
        final boolean writeAsString = (this.context.features & Feature.WriteNonStringValueAsString.mask) != 0x0L;
        int off = this.off;
        final int minCapacity = off + 26;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (writeAsString) {
            bytes[off++] = 34;
        }
        final int len = DoubleToDecimal.toString(value, bytes, off, true);
        off += len;
        if (writeAsString) {
            bytes[off++] = 34;
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
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = 91;
        for (int i = 0; i < values.length; ++i) {
            if (i != 0) {
                bytes[off++] = 44;
            }
            if (writeAsString) {
                bytes[off++] = 34;
            }
            final float value = values[i];
            final int len = DoubleToDecimal.toString(value, bytes, off, true);
            off += len;
            if (writeAsString) {
                bytes[off++] = 34;
            }
        }
        bytes[off] = 93;
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
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = 91;
        for (int i = 0; i < values.length; ++i) {
            if (i != 0) {
                bytes[off++] = 44;
            }
            if (writeAsString) {
                bytes[off++] = 34;
            }
            final double value = values[i];
            final int len = DoubleToDecimal.toString(value, bytes, off, true);
            off += len;
            if (writeAsString) {
                bytes[off++] = 34;
            }
        }
        bytes[off] = 93;
        this.off = off + 1;
    }
    
    @Override
    public final void writeDateTime14(final int year, final int month, final int dayOfMonth, final int hour, final int minute, final int second) {
        final int off = this.off;
        final int minCapacity = off + 16;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off] = (byte)this.quote;
        if (year < 0 || year > 9999) {
            throw new IllegalArgumentException("Only 4 digits numbers are supported. Provided: " + year);
        }
        final int y01 = year / 100;
        final int y2 = year - y01 * 100;
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, IOUtils.PACKED_DIGITS[y01]);
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 3L, IOUtils.PACKED_DIGITS[y2]);
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 5L, IOUtils.PACKED_DIGITS[month]);
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 7L, IOUtils.PACKED_DIGITS[dayOfMonth]);
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 9L, IOUtils.PACKED_DIGITS[hour]);
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 11L, IOUtils.PACKED_DIGITS[minute]);
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 13L, IOUtils.PACKED_DIGITS[second]);
        bytes[off + 15] = (byte)this.quote;
        this.off = off + 16;
    }
    
    @Override
    public final void writeDateTime19(final int year, final int month, final int dayOfMonth, final int hour, final int minute, final int second) {
        int off = this.off;
        final int minCapacity = off + 21;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off] = (byte)this.quote;
        off = IOUtils.writeLocalDate(bytes, off + 1, year, month, dayOfMonth);
        bytes[off] = 32;
        IOUtils.writeLocalTime(bytes, off + 1, hour, minute, second);
        bytes[off + 9] = (byte)this.quote;
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
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = (byte)this.quote;
        off = IOUtils.writeLocalDate(bytes, off, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        bytes[off] = (byte)this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeLocalDateTime(final LocalDateTime dateTime) {
        int off = this.off;
        final int minCapacity = off + 38;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = (byte)this.quote;
        final LocalDate localDate = dateTime.toLocalDate();
        off = IOUtils.writeLocalDate(bytes, off, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        bytes[off++] = 32;
        off = IOUtils.writeLocalTime(bytes, off, dateTime.toLocalTime());
        bytes[off] = (byte)this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeDateYYYMMDD8(final int year, final int month, final int dayOfMonth) {
        final int off = this.off;
        final int minCapacity = off + 10;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off] = (byte)this.quote;
        if (year < 0 || year > 9999) {
            throw new IllegalArgumentException("Only 4 digits numbers are supported. Provided: " + year);
        }
        final int y01 = year / 100;
        final int y2 = year - y01 * 100;
        JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, IOUtils.PACKED_DIGITS[y01]);
        JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 3L, IOUtils.PACKED_DIGITS[y2]);
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 5L, IOUtils.PACKED_DIGITS[month]);
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 7L, IOUtils.PACKED_DIGITS[dayOfMonth]);
        bytes[off + 9] = (byte)this.quote;
        this.off = off + 10;
    }
    
    @Override
    public final void writeDateYYYMMDD10(final int year, final int month, final int dayOfMonth) {
        int off = this.off;
        final int minCapacity = off + 13;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = (byte)this.quote;
        off = IOUtils.writeLocalDate(bytes, off, year, month, dayOfMonth);
        bytes[off] = (byte)this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeTimeHHMMSS8(final int hour, final int minute, final int second) {
        final int off = this.off;
        final int minCapacity = off + 10;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off] = (byte)this.quote;
        IOUtils.writeLocalTime(bytes, off + 1, hour, minute, second);
        bytes[off + 9] = (byte)this.quote;
        this.off = off + 10;
    }
    
    @Override
    public final void writeLocalTime(final LocalTime time) {
        int off = this.off;
        final int minCapacity = off + 20;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = (byte)this.quote;
        off = IOUtils.writeLocalTime(bytes, off, time);
        bytes[off] = (byte)this.quote;
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
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = (byte)this.quote;
        final LocalDate localDate = dateTime.toLocalDate();
        off = IOUtils.writeLocalDate(bytes, off, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        bytes[off++] = 84;
        off = IOUtils.writeLocalTime(bytes, off, dateTime.toLocalTime());
        if (zoneSize == 1) {
            bytes[off++] = 90;
        }
        else if (firstZoneChar == '+' || firstZoneChar == '-') {
            zoneId.getBytes(0, zoneIdLength, bytes, off);
            off += zoneIdLength;
        }
        else {
            bytes[off++] = 91;
            zoneId.getBytes(0, zoneIdLength, bytes, off);
            off += zoneIdLength;
            bytes[off++] = 93;
        }
        bytes[off] = (byte)this.quote;
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
        final boolean utc = ZoneOffset.UTC == offset || "UTC".equals(zoneId) || "Z".equals(zoneId);
        int zoneIdLength;
        if (utc) {
            zoneId = "Z";
            zoneIdLength = 1;
        }
        else {
            zoneIdLength = zoneId.length();
        }
        final int minCapacity = this.off + zoneIdLength + 40;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        int off = this.off;
        bytes[off++] = (byte)this.quote;
        final LocalDateTime ldt = dateTime.toLocalDateTime();
        final LocalDate date = ldt.toLocalDate();
        off = IOUtils.writeLocalDate(bytes, off, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        bytes[off++] = 84;
        off = IOUtils.writeLocalTime(bytes, off, ldt.toLocalTime());
        if (utc) {
            bytes[off++] = 90;
        }
        else {
            zoneId.getBytes(0, zoneIdLength, bytes, off);
            off += zoneIdLength;
        }
        bytes[off] = (byte)this.quote;
        this.off = off + 1;
    }
    
    @Override
    public final void writeBigInt(final BigInteger value, final long features) {
        if (value == null) {
            this.writeNumberNull();
            return;
        }
        final String str = value.toString(10);
        if (((this.context.features | features) & Feature.BrowserCompatible.mask) != 0x0L && (value.compareTo(JSONFactory.LOW_BIGINT) < 0 || value.compareTo(JSONFactory.HIGH_BIGINT) > 0)) {
            this.writeString(str);
            return;
        }
        final int strlen = str.length();
        final int minCapacity = this.off + strlen;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        str.getBytes(0, strlen, this.bytes, this.off);
        this.off += strlen;
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
        final int minCapacity = this.off + 25 + zonelen;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        int off = this.off;
        bytes[off] = (byte)this.quote;
        off = IOUtils.writeLocalDate(bytes, off + 1, year, month, dayOfMonth);
        bytes[off] = (byte)(timeZone ? 84 : 32);
        IOUtils.writeLocalTime(bytes, off + 1, hour, minute, second);
        off += 9;
        if (millis > 0) {
            final int div = millis / 10;
            final int div2 = div / 10;
            final int rem1 = millis - div * 10;
            if (rem1 != 0) {
                IOUtils.putInt(bytes, off, (IOUtils.DIGITS_K_32[millis] & 0xFFFFFF00) | 0x2E);
                off += 4;
            }
            else {
                bytes[off++] = 46;
                final int rem2 = div - div2 * 10;
                if (rem2 != 0) {
                    JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off, IOUtils.PACKED_DIGITS[div]);
                    off += 2;
                }
                else {
                    bytes[off++] = (byte)(div2 + 48);
                }
            }
        }
        if (timeZone) {
            final int offset = offsetSeconds / 3600;
            if (offsetSeconds == 0) {
                bytes[off++] = 90;
            }
            else {
                final int offsetAbs = Math.abs(offset);
                bytes[off] = (byte)((offset >= 0) ? 43 : 45);
                JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, IOUtils.PACKED_DIGITS[offsetAbs]);
                bytes[off + 3] = 58;
                int offsetMinutes = (offsetSeconds - offset * 3600) / 60;
                if (offsetMinutes < 0) {
                    offsetMinutes = -offsetMinutes;
                }
                JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 4L, IOUtils.PACKED_DIGITS[offsetMinutes]);
                off += 6;
            }
        }
        bytes[off] = (byte)this.quote;
        this.off = off + 1;
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
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (writeAsString) {
            bytes[off++] = 34;
        }
        final boolean asPlain = (features & Feature.WriteBigDecimalAsPlain.mask) != 0x0L;
        final int scale;
        final long unscaleValue;
        if (precision < 19 && (scale = value.scale()) >= 0 && JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET != -1L && (unscaleValue = JDKUtils.UNSAFE.getLong(value, JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET)) != Long.MIN_VALUE && !asPlain) {
            off = IOUtils.writeDecimal(bytes, off, unscaleValue, scale);
        }
        else {
            final String str2 = asPlain ? value.toPlainString() : value.toString();
            str2.getBytes(0, str2.length(), bytes, off);
            off += str2.length();
        }
        if (writeAsString) {
            bytes[off++] = 34;
        }
        this.off = off;
    }
    
    @Override
    public final void writeNameRaw(final char[] chars) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public final void writeNameRaw(final char[] bytes, final int offset, final int len) {
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
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = 123;
        boolean first = true;
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            final Object value = entry.getValue();
            if (!first) {
                if (this.off == this.bytes.length) {
                    this.ensureCapacity(this.off + 1);
                }
                if (value != null && (this.context.features & Feature.WriteMapNullValue.mask) == 0x0L) {
                    this.bytes[this.off++] = 44;
                }
            }
            if (value == null && (this.context.features & Feature.WriteMapNullValue.mask) == 0x0L) {
                continue;
            }
            first = false;
            final Object key = entry.getKey();
            if (key instanceof String) {
                this.writeString((String)key);
            }
            else {
                this.writeAny(key);
            }
            if (this.off == this.bytes.length) {
                this.ensureCapacity(this.off + 1);
            }
            this.bytes[this.off++] = 58;
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
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = 125;
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
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = 91;
        boolean first = true;
        for (int i = 0; i < array.size(); ++i) {
            final Object o = array.get(i);
            if (!first) {
                if (this.off == this.bytes.length) {
                    this.ensureCapacity(this.off + 1);
                }
                this.bytes[this.off++] = 44;
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
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = 93;
    }
    
    @Override
    public void writeBool(final boolean value) {
        final int minCapacity = this.off + 5;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        int off = this.off;
        if ((this.context.features & Feature.WriteBooleanAsNumber.mask) != 0x0L) {
            bytes[off++] = (byte)(value ? 49 : 48);
        }
        else {
            if (!value) {
                bytes[off++] = 102;
            }
            JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off, value ? IOUtils.TRUE : IOUtils.ALSE);
            off += 4;
        }
        this.off = off;
    }
    
    @Override
    public final String toString() {
        return new String(this.bytes, 0, this.off, StandardCharsets.UTF_8);
    }
    
    @Override
    public final int flushTo(final OutputStream out, final Charset charset) throws IOException {
        if (charset != null && charset != StandardCharsets.UTF_8) {
            throw new JSONException("UnsupportedOperation");
        }
        if (this.off == 0) {
            return 0;
        }
        final int len = this.off;
        out.write(this.bytes, 0, this.off);
        this.off = 0;
        return len;
    }
    
    static {
        REF_PREF = "{\"$ref\":".getBytes(StandardCharsets.ISO_8859_1);
        final short[] digits = new short[256];
        for (int i = 0; i < 16; ++i) {
            final short hi = (short)((i < 10) ? (i + 48) : (i - 10 + 97));
            for (int j = 0; j < 16; ++j) {
                final short lo = (short)((j < 10) ? (j + 48) : (j - 10 + 97));
                digits[(i << 4) + j] = (JDKUtils.BIG_ENDIAN ? ((short)(hi << 8 | lo)) : ((short)(hi | lo << 8)));
            }
        }
        HEX256 = digits;
    }
}
