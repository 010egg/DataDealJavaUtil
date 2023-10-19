// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.UUID;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.alibaba.fastjson2.util.DateUtils;
import java.time.OffsetDateTime;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Date;
import com.alibaba.fastjson2.reader.ValueConsumer;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson2.util.IOUtils;
import java.math.BigInteger;
import com.alibaba.fastjson2.util.TypeUtils;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.util.JDKUtils;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.Arrays;
import java.io.InputStream;

class JSONReaderUTF8 extends JSONReader
{
    protected final byte[] bytes;
    protected final int length;
    protected final int start;
    protected final int end;
    protected int nameBegin;
    protected int nameEnd;
    protected int nameLength;
    protected boolean nameAscii;
    protected int referenceBegin;
    protected final InputStream in;
    protected final JSONFactory.CacheItem cacheItem;
    
    JSONReaderUTF8(final Context ctx, final InputStream is) {
        super(ctx, false, true);
        final int cacheIndex = System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1;
        this.cacheItem = JSONFactory.CACHE_ITEMS[cacheIndex];
        byte[] bytes = JSONFactory.BYTES_UPDATER.getAndSet(this.cacheItem, null);
        final int bufferSize = ctx.bufferSize;
        if (bytes == null) {
            bytes = new byte[bufferSize];
        }
        int off = 0;
        try {
            while (true) {
                final int n = is.read(bytes, off, bytes.length - off);
                if (n == -1) {
                    break;
                }
                off += n;
                if (off != bytes.length) {
                    continue;
                }
                bytes = Arrays.copyOf(bytes, bytes.length + bufferSize);
            }
        }
        catch (IOException ioe) {
            throw new JSONException("read error", ioe);
        }
        this.bytes = bytes;
        this.offset = 0;
        this.length = off;
        this.in = is;
        this.start = 0;
        this.end = this.length;
        this.next();
        while (this.ch == '/' && this.offset < this.bytes.length && this.bytes[this.offset] == 47) {
            this.skipLineComment();
        }
    }
    
    JSONReaderUTF8(final Context ctx, final ByteBuffer buffer) {
        super(ctx, false, true);
        final int cacheIndex = System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1;
        this.cacheItem = JSONFactory.CACHE_ITEMS[cacheIndex];
        byte[] bytes = JSONFactory.BYTES_UPDATER.getAndSet(this.cacheItem, null);
        final int remaining = buffer.remaining();
        if (bytes == null || bytes.length < remaining) {
            bytes = new byte[remaining];
        }
        buffer.get(bytes, 0, remaining);
        this.bytes = bytes;
        this.offset = 0;
        this.length = remaining;
        this.in = null;
        this.start = 0;
        this.end = this.length;
        this.next();
        while (this.ch == '/' && this.offset < this.bytes.length && this.bytes[this.offset] == 47) {
            this.skipLineComment();
        }
    }
    
    JSONReaderUTF8(final Context ctx, final String str, final byte[] bytes, final int offset, final int length) {
        super(ctx, false, true);
        this.bytes = bytes;
        this.offset = offset;
        this.length = length;
        this.in = null;
        this.start = offset;
        this.end = offset + length;
        this.cacheItem = null;
        this.next();
    }
    
    @Override
    public boolean nextIfMatch(final char e) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        int ch = this.ch;
        while (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L) {
            if (offset >= this.end) {
                ch = 26;
            }
            else {
                ch = bytes[offset++];
            }
        }
        if (ch != e) {
            return false;
        }
        this.comma = (ch == 44);
        if (offset >= this.end) {
            this.offset = offset;
            this.ch = '\u001a';
            return true;
        }
        for (ch = bytes[offset]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset]) {
            if (++offset >= this.end) {
                this.offset = offset;
                this.ch = '\u001a';
                return true;
            }
        }
        if (ch >= 0) {
            ++offset;
        }
        else {
            ch &= 0xFF;
            switch (ch >> 4) {
                case 12:
                case 13: {
                    offset += 2;
                    final int char2 = bytes[offset - 1];
                    if ((char2 & 0xC0) != 0x80) {
                        throw new JSONException("malformed input around byte " + offset);
                    }
                    ch = ((ch & 0x1F) << 6 | (char2 & 0x3F));
                    break;
                }
                case 14: {
                    offset += 3;
                    final int char2 = bytes[offset - 2];
                    final int char3 = bytes[offset - 1];
                    if ((char2 & 0xC0) != 0x80 || (char3 & 0xC0) != 0x80) {
                        throw new JSONException("malformed input around byte " + (offset - 1));
                    }
                    ch = ((ch & 0xF) << 12 | (char2 & 0x3F) << 6 | (char3 & 0x3F));
                    break;
                }
                default: {
                    throw new JSONException("malformed input around byte " + offset);
                }
            }
        }
        this.offset = offset;
        this.ch = (char)ch;
        while (this.ch == '/' && offset < bytes.length && bytes[offset] == 47) {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public boolean nextIfComma() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        int ch = this.ch;
        if (ch != 44) {
            this.offset = offset;
            this.ch = (char)ch;
            return false;
        }
        this.comma = true;
        if (offset >= this.end) {
            this.offset = offset;
            this.ch = '\u001a';
            return true;
        }
        for (ch = bytes[offset]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset]) {
            if (++offset >= this.end) {
                this.offset = offset;
                this.ch = '\u001a';
                return true;
            }
        }
        if (ch >= 0) {
            ++offset;
        }
        else {
            ch &= 0xFF;
            switch (ch >> 4) {
                case 12:
                case 13: {
                    offset += 2;
                    final int char2 = bytes[offset - 1];
                    if ((char2 & 0xC0) != 0x80) {
                        throw new JSONException("malformed input around byte " + offset);
                    }
                    ch = ((ch & 0x1F) << 6 | (char2 & 0x3F));
                    break;
                }
                case 14: {
                    offset += 3;
                    final int char2 = bytes[offset - 2];
                    final int char3 = bytes[offset - 1];
                    if ((char2 & 0xC0) != 0x80 || (char3 & 0xC0) != 0x80) {
                        throw new JSONException("malformed input around byte " + (offset - 1));
                    }
                    ch = ((ch & 0xF) << 12 | (char2 & 0x3F) << 6 | (char3 & 0x3F));
                    break;
                }
                default: {
                    throw new JSONException("malformed input around byte " + offset);
                }
            }
        }
        this.offset = offset;
        this.ch = (char)ch;
        while (this.ch == '/' && offset < bytes.length && bytes[offset] == 47) {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public boolean nextIfArrayStart() {
        int ch = this.ch;
        if (ch != 91) {
            return false;
        }
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return true;
        }
        byte[] bytes;
        for (bytes = this.bytes, ch = bytes[offset++]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset++]) {
            if (offset >= this.end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        Label_0275: {
            if (ch < 0) {
                ch &= 0xFF;
                switch (ch >> 4) {
                    case 12:
                    case 13: {
                        final int c2 = bytes[offset];
                        if ((c2 & 0xC0) != 0x80) {
                            break;
                        }
                        ch = ((ch & 0x1F) << 6 | (c2 & 0x3F));
                        ++offset;
                        break Label_0275;
                    }
                    case 14: {
                        final int c2 = bytes[offset];
                        final int c3 = bytes[offset + 1];
                        if ((c2 & 0xC0) != 0x80) {
                            break;
                        }
                        if ((c3 & 0xC0) != 0x80) {
                            break;
                        }
                        ch = ((ch & 0xF) << 12 | (c2 & 0x3F) << 6 | (c3 & 0x3F));
                        offset += 2;
                        break Label_0275;
                    }
                }
                throw new JSONException("malformed input around byte " + offset);
            }
        }
        this.ch = (char)ch;
        this.offset = offset;
        while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public boolean nextIfArrayEnd() {
        int ch = this.ch;
        if (ch == 125 || ch == 26) {
            throw new JSONException(this.info("Illegal syntax: `" + (char)ch + '`'));
        }
        if (ch != 93) {
            return false;
        }
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return true;
        }
        byte[] bytes;
        for (bytes = this.bytes, ch = bytes[offset++]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset++]) {
            if (offset >= this.end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        if (ch == 44) {
            this.comma = true;
            for (ch = bytes[offset++]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset++]) {
                if (offset >= this.end) {
                    this.ch = '\u001a';
                    this.offset = offset;
                    return true;
                }
            }
        }
        Label_0395: {
            if (ch < 0) {
                ch &= 0xFF;
                switch (ch >> 4) {
                    case 12:
                    case 13: {
                        final int c2 = bytes[offset];
                        if ((c2 & 0xC0) != 0x80) {
                            break;
                        }
                        ch = ((ch & 0x1F) << 6 | (c2 & 0x3F));
                        ++offset;
                        break Label_0395;
                    }
                    case 14: {
                        final int c2 = bytes[offset];
                        final int c3 = bytes[offset + 1];
                        if ((c2 & 0xC0) != 0x80) {
                            break;
                        }
                        if ((c3 & 0xC0) != 0x80) {
                            break;
                        }
                        ch = ((ch & 0xF) << 12 | (c2 & 0x3F) << 6 | (c3 & 0x3F));
                        offset += 2;
                        break Label_0395;
                    }
                }
                throw new JSONException("malformed input around byte " + offset);
            }
        }
        this.ch = (char)ch;
        this.offset = offset;
        while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public final boolean nextIfSet() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        byte ch = (byte)this.ch;
        if (ch == 83 && offset + 1 < this.end && bytes[offset] == 101 && bytes[offset + 1] == 116) {
            offset += 2;
            if (offset >= this.end) {
                ch = 26;
            }
            else {
                for (ch = bytes[offset++]; ch <= 32 && (1L << ch & 0x100003700L) != 0x0L; ch = bytes[offset++]) {
                    if (offset == this.end) {
                        ch = 26;
                        break;
                    }
                }
            }
            this.offset = offset;
            this.ch = (char)ch;
            return true;
        }
        return false;
    }
    
    @Override
    public final boolean nextIfInfinity() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        byte ch = (byte)this.ch;
        if (ch == 73 && offset + 6 < this.end && bytes[offset] == 110 && bytes[offset + 1] == 102 && bytes[offset + 2] == 105 && bytes[offset + 3] == 110 && bytes[offset + 4] == 105 && bytes[offset + 5] == 116 && bytes[offset + 6] == 121) {
            offset += 7;
            if (offset >= this.end) {
                ch = 26;
            }
            else {
                for (ch = bytes[offset++]; ch <= 32 && (1L << ch & 0x100003700L) != 0x0L; ch = bytes[offset++]) {
                    if (offset == this.end) {
                        ch = 26;
                        break;
                    }
                }
            }
            this.offset = offset;
            this.ch = (char)ch;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean nextIfObjectStart() {
        int ch = this.ch;
        if (ch != 123) {
            return false;
        }
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return true;
        }
        byte[] bytes;
        for (bytes = this.bytes, ch = bytes[offset++]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset++]) {
            if (offset >= this.end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        Label_0275: {
            if (ch < 0) {
                ch &= 0xFF;
                switch (ch >> 4) {
                    case 12:
                    case 13: {
                        final int c2 = bytes[offset];
                        if ((c2 & 0xC0) != 0x80) {
                            break;
                        }
                        ch = ((ch & 0x1F) << 6 | (c2 & 0x3F));
                        ++offset;
                        break Label_0275;
                    }
                    case 14: {
                        final int c2 = bytes[offset];
                        final int c3 = bytes[offset + 1];
                        if ((c2 & 0xC0) != 0x80) {
                            break;
                        }
                        if ((c3 & 0xC0) != 0x80) {
                            break;
                        }
                        ch = ((ch & 0xF) << 12 | (c2 & 0x3F) << 6 | (c3 & 0x3F));
                        offset += 2;
                        break Label_0275;
                    }
                }
                throw new JSONException("malformed input around byte " + offset);
            }
        }
        this.ch = (char)ch;
        this.offset = offset;
        while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public boolean nextIfObjectEnd() {
        int ch = this.ch;
        if (ch == 93 || ch == 26) {
            throw new JSONException(this.info("Illegal syntax: `" + (char)ch + '`'));
        }
        if (ch != 125) {
            return false;
        }
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return true;
        }
        byte[] bytes;
        for (bytes = this.bytes, ch = bytes[offset++]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset++]) {
            if (offset >= this.end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        if (ch == 44) {
            this.comma = true;
            for (ch = bytes[offset++]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset++]) {
                if (offset >= this.end) {
                    this.ch = '\u001a';
                    this.offset = offset;
                    return true;
                }
            }
        }
        Label_0395: {
            if (ch < 0) {
                ch &= 0xFF;
                switch (ch >> 4) {
                    case 12:
                    case 13: {
                        final int c2 = bytes[offset];
                        if ((c2 & 0xC0) != 0x80) {
                            break;
                        }
                        ch = ((ch & 0x1F) << 6 | (c2 & 0x3F));
                        ++offset;
                        break Label_0395;
                    }
                    case 14: {
                        final int c2 = bytes[offset];
                        final int c3 = bytes[offset + 1];
                        if ((c2 & 0xC0) != 0x80) {
                            break;
                        }
                        if ((c3 & 0xC0) != 0x80) {
                            break;
                        }
                        ch = ((ch & 0xF) << 12 | (c2 & 0x3F) << 6 | (c3 & 0x3F));
                        offset += 2;
                        break Label_0395;
                    }
                }
                throw new JSONException("malformed input around byte " + offset);
            }
        }
        this.ch = (char)ch;
        this.offset = offset;
        while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
            this.skipLineComment();
        }
        return true;
    }
    
    @Override
    public void next() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        if (offset >= this.end) {
            this.ch = '\u001a';
            return;
        }
        int ch;
        for (ch = bytes[offset]; ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L); ch = bytes[offset]) {
            if (++offset >= this.end) {
                this.ch = '\u001a';
                return;
            }
        }
        if (ch >= 0) {
            this.offset = offset + 1;
            this.ch = (char)ch;
            while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
                this.skipLineComment();
            }
            return;
        }
        ch &= 0xFF;
        switch (ch >> 4) {
            case 12:
            case 13: {
                offset += 2;
                final int char2 = bytes[offset - 1];
                if ((char2 & 0xC0) != 0x80) {
                    throw new JSONException("malformed input around byte " + offset);
                }
                ch = ((ch & 0x1F) << 6 | (char2 & 0x3F));
                break;
            }
            case 14: {
                offset += 3;
                final int char2 = bytes[offset - 2];
                final int char3 = bytes[offset - 1];
                if ((char2 & 0xC0) != 0x80 || (char3 & 0xC0) != 0x80) {
                    throw new JSONException("malformed input around byte " + (offset - 1));
                }
                ch = ((ch & 0xF) << 12 | (char2 & 0x3F) << 6 | (char3 & 0x3F));
                break;
            }
            default: {
                throw new JSONException("malformed input around byte " + offset);
            }
        }
        this.offset = offset;
        this.ch = (char)ch;
        while (this.ch == '/' && this.offset < bytes.length && bytes[this.offset] == 47) {
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
    Label_0894:
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
                    break Label_0894;
                }
                default: {
                    if (this.ch == '\\') {
                        this.nameEscape = true;
                        switch (this.ch = (char)this.bytes[this.offset++]) {
                            case 'u': {
                                final byte c1 = this.bytes[this.offset++];
                                final byte c2 = this.bytes[this.offset++];
                                final byte c3 = this.bytes[this.offset++];
                                final byte c4 = this.bytes[this.offset++];
                                this.ch = JSONReader.char4(c1, c2, c3, c4);
                                break;
                            }
                            case 'x': {
                                final byte c1 = this.bytes[this.offset++];
                                final byte c2 = this.bytes[this.offset++];
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
                    if (this.ch > '\u007f' || i >= 8 || (i == 0 && this.ch == '\0')) {
                        nameValue = 0L;
                        this.ch = first;
                        this.offset = this.nameBegin + 1;
                        break Label_0894;
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
                    this.ch = ((this.offset >= this.end) ? '\u001a' : ((char)this.bytes[this.offset++]));
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
        Label_1468:
            while (true) {
                if (this.ch == '\\') {
                    this.nameEscape = true;
                    switch (this.ch = (char)this.bytes[this.offset++]) {
                        case 'u': {
                            final char c6 = (char)this.bytes[this.offset++];
                            final char c7 = (char)this.bytes[this.offset++];
                            final char c8 = (char)this.bytes[this.offset++];
                            final char c9 = (char)this.bytes[this.offset++];
                            this.ch = JSONReader.char4(c6, c7, c8, c9);
                            break;
                        }
                        case 'x': {
                            final char c6 = (char)this.bytes[this.offset++];
                            final char c7 = (char)this.bytes[this.offset++];
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
                            break Label_1468;
                        }
                        default: {
                            hashCode ^= this.ch;
                            hashCode *= 1099511628211L;
                            this.ch = ((this.offset >= this.end) ? '\u001a' : ((char)this.bytes[this.offset++]));
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
                this.ch = (char)this.bytes[this.offset++];
            }
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                    break;
                }
                this.ch = (char)this.bytes[this.offset++];
            }
        }
        return hashCode;
    }
    
    @Override
    public final int getRawInt() {
        if (this.offset + 3 < this.bytes.length) {
            return JDKUtils.UNSAFE.getInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset - 1L);
        }
        return 0;
    }
    
    @Override
    public final long getRawLong() {
        if (this.offset + 8 < this.bytes.length) {
            return JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset - 1L);
        }
        return 0L;
    }
    
    @Override
    public final boolean nextIfName8Match2() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 9;
        if (offset >= this.end) {
            return false;
        }
        if (bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName8Match1() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 8;
        if (offset >= this.end) {
            return false;
        }
        if (bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName8Match0() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 7;
        if (offset == this.end) {
            this.ch = '\u001a';
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match2() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 4;
        if (offset >= this.end) {
            return false;
        }
        if (bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match3() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 5;
        if (offset >= this.end) {
            return false;
        }
        if (bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match4(final byte c4) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 6;
        if (offset >= this.end) {
            return false;
        }
        if (bytes[offset - 3] != c4 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c5;
        for (c5 = (bytes[offset] & 0xFF); c5 <= 32 && (1L << c5 & 0x100003700L) != 0x0L; c5 = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c5;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match5(final int name1) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 7;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 4L) != name1) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match6(final int name1) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 8;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 5L) != name1 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match7(final int name1) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 9;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 6L) != name1 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match8(final int name1, final byte c8) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 10;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 7L) != name1 || bytes[offset - 3] != c8 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c9;
        for (c9 = (bytes[offset] & 0xFF); c9 <= 32 && (1L << c9 & 0x100003700L) != 0x0L; c9 = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c9;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match9(final long name1) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 11;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 8L) != name1) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match10(final long name1) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 12;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 9L) != name1 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match11(final long name1) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 13;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 10L) != name1 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match12(final long name1, final byte c12) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 14;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 11L) != name1 || bytes[offset - 3] != c12 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c13;
        for (c13 = (bytes[offset] & 0xFF); c13 <= 32 && (1L << c13 & 0x100003700L) != 0x0L; c13 = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c13;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match13(final long name1, final int name2) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 15;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 12L) != name1 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 4L) != name2) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match14(final long name1, final int name2) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 16;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 13L) != name1 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 5L) != name2 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match15(final long name1, final int name2) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 17;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 14L) != name1 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 6L) != name2 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match16(final long name1, final int name2, final byte c16) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 18;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 15L) != name1 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 7L) != name2 || bytes[offset - 3] != c16 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c17;
        for (c17 = (bytes[offset] & 0xFF); c17 <= 32 && (1L << c17 & 0x100003700L) != 0x0L; c17 = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c17;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match17(final long name1, final long name2) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 19;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 16L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 8L) != name2) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match18(final long name1, final long name2) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 20;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 17L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 9L) != name2 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match19(final long name1, final long name2) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 21;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 18L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 10L) != name2 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match20(final long name1, final long name2, final byte c20) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 22;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 19L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 11L) != name2 || bytes[offset - 3] != c20 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c21;
        for (c21 = (bytes[offset] & 0xFF); c21 <= 32 && (1L << c21 & 0x100003700L) != 0x0L; c21 = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c21;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match21(final long name1, final long name2, final int name3) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 23;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 20L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 12L) != name2 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 4L) != name3) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match22(final long name1, final long name2, final int name3) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 24;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 21L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 13L) != name2 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 5L) != name3 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match23(final long name1, final long name2, final int name3) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 25;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 22L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 14L) != name2 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 6L) != name3 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match24(final long name1, final long name2, final int name3, final byte c24) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 26;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 23L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 15L) != name2 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 7L) != name3 || bytes[offset - 3] != c24 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c25;
        for (c25 = (bytes[offset] & 0xFF); c25 <= 32 && (1L << c25 & 0x100003700L) != 0x0L; c25 = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c25;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match25(final long name1, final long name2, final long name3) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 27;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 24L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 16L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 8L) != name3) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match26(final long name1, final long name2, final long name3) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 28;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 25L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 17L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 9L) != name3 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match27(final long name1, final long name2, final long name3) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 29;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 26L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 18L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 10L) != name3 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match28(final long name1, final long name2, final long name3, final byte c29) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 30;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 27L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 19L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 11L) != name3 || bytes[offset - 3] != c29 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c30;
        for (c30 = (bytes[offset] & 0xFF); c30 <= 32 && (1L << c30 & 0x100003700L) != 0x0L; c30 = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c30;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match29(final long name1, final long name2, final long name3, final int name4) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 31;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 28L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 20L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 12L) != name3 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 4L) != name4) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match30(final long name1, final long name2, final long name3, final int name4) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 32;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 29L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 21L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 13L) != name3 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 5L) != name4 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match31(final long name1, final long name2, final long name3, final int name4) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 33;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 30L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 22L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 14L) != name3 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 6L) != name4 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match32(final long name1, final long name2, final long name3, final int name4, final byte c32) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 34;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 31L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 23L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 15L) != name3 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 7L) != name4 || bytes[offset - 3] != c32 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c33;
        for (c33 = (bytes[offset] & 0xFF); c33 <= 32 && (1L << c33 & 0x100003700L) != 0x0L; c33 = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c33;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match33(final long name1, final long name2, final long name3, final long name4) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 35;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 32L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 24L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 16L) != name3 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 8L) != name4) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match34(final long name1, final long name2, final long name3, final long name4) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 36;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 33L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 25L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 17L) != name3 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 9L) != name4 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match35(final long name1, final long name2, final long name3, final long name4) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 37;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 34L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 26L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 18L) != name3 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 10L) != name4 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match36(final long name1, final long name2, final long name3, final long name4, final byte c36) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 38;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 35L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 27L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 19L) != name3 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 11L) != name4 || bytes[offset - 3] != c36 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c37;
        for (c37 = (bytes[offset] & 0xFF); c37 <= 32 && (1L << c37 & 0x100003700L) != 0x0L; c37 = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c37;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match37(final long name1, final long name2, final long name3, final long name4, final int name5) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 39;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 36L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 28L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 20L) != name3 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 12L) != name4 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 4L) != name5) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match38(final long name1, final long name2, final long name3, final long name4, final int name5) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 40;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 37L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 29L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 21L) != name3 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 13L) != name4 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 5L) != name5 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match39(final long name1, final long name2, final long name3, final long name4, final int name5) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 41;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 38L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 30L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 22L) != name3 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 14L) != name4 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 6L) != name5 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match40(final long name1, final long name2, final long name3, final long name4, final int name5, final byte c40) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 42;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 39L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 31L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 23L) != name3 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 15L) != name4 || JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 7L) != name5 || bytes[offset - 3] != c40 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c41;
        for (c41 = (bytes[offset] & 0xFF); c41 <= 32 && (1L << c41 & 0x100003700L) != 0x0L; c41 = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c41;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match41(final long name1, final long name2, final long name3, final long name4, final long name5) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 43;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 40L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 32L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 24L) != name3 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 16L) != name4 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 8L) != name5) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match42(final long name1, final long name2, final long name3, final long name4, final long name5) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 44;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 41L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 33L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 25L) != name3 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 17L) != name4 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 9L) != name5 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfName4Match43(final long name1, final long name2, final long name3, final long name4, final long name5) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 45;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 42L) != name1 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 34L) != name2 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 26L) != name3 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 18L) != name4 || JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 10L) != name5 || bytes[offset - 2] != 34 || bytes[offset - 1] != 58) {
            return false;
        }
        int c;
        for (c = (bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (bytes[offset] & 0xFF)) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match2() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 3;
        if (offset >= this.end) {
            return false;
        }
        int c = bytes[offset] & 0xFF;
        if (c != 44 && c != 125 && c != 93) {
            return false;
        }
        if (c == 44) {
            this.comma = true;
            c = ((++offset == this.end) ? 26 : (bytes[offset] & 0xFF));
        }
        while (c <= 32 && (1L << c & 0x100003700L) != 0x0L) {
            ++offset;
            c = (bytes[offset] & 0xFF);
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match3() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 4;
        if (offset >= this.end) {
            return false;
        }
        if (bytes[offset - 1] != 34) {
            return false;
        }
        int c = bytes[offset] & 0xFF;
        if (c != 44 && c != 125 && c != 93) {
            return false;
        }
        if (c == 44) {
            this.comma = true;
            c = ((++offset == this.end) ? 26 : (bytes[offset] & 0xFF));
        }
        while (c <= 32 && (1L << c & 0x100003700L) != 0x0L) {
            ++offset;
            c = (bytes[offset] & 0xFF);
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match4(final byte c4) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 5;
        if (offset >= this.end) {
            return false;
        }
        if (bytes[offset - 2] != c4 || bytes[offset - 1] != 34) {
            return false;
        }
        int c5 = bytes[offset] & 0xFF;
        if (c5 != 44 && c5 != 125 && c5 != 93) {
            return false;
        }
        if (c5 == 44) {
            this.comma = true;
            c5 = ((++offset == this.end) ? 26 : (bytes[offset] & 0xFF));
        }
        while (c5 <= 32 && (1L << c5 & 0x100003700L) != 0x0L) {
            ++offset;
            c5 = (bytes[offset] & 0xFF);
        }
        this.offset = offset + 1;
        this.ch = (char)c5;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match5(final byte c4, final byte c5) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 6;
        if (offset >= this.end) {
            return false;
        }
        if (bytes[offset - 3] != c4 || bytes[offset - 2] != c5 || bytes[offset - 1] != 34) {
            return false;
        }
        int c6 = bytes[offset] & 0xFF;
        if (c6 != 44 && c6 != 125 && c6 != 93) {
            return false;
        }
        if (c6 == 44) {
            this.comma = true;
            c6 = ((++offset == this.end) ? 26 : (bytes[offset] & 0xFF));
        }
        while (c6 <= 32 && (1L << c6 & 0x100003700L) != 0x0L) {
            ++offset;
            c6 = (bytes[offset] & 0xFF);
        }
        this.offset = offset + 1;
        this.ch = (char)c6;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match6(final int name1) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 7;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 4L) != name1) {
            return false;
        }
        int c = bytes[offset] & 0xFF;
        if (c != 44 && c != 125 && c != 93) {
            return false;
        }
        if (c == 44) {
            this.comma = true;
            c = ((++offset == this.end) ? 26 : (bytes[offset] & 0xFF));
        }
        while (c <= 32 && (1L << c & 0x100003700L) != 0x0L) {
            ++offset;
            c = (bytes[offset] & 0xFF);
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match7(final int name1) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 8;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 5L) != name1 || bytes[offset - 1] != 34) {
            return false;
        }
        int c = bytes[offset] & 0xFF;
        if (c != 44 && c != 125 && c != 93) {
            return false;
        }
        if (c == 44) {
            this.comma = true;
            c = ((++offset == this.end) ? 26 : (bytes[offset] & 0xFF));
        }
        while (c <= 32 && (1L << c & 0x100003700L) != 0x0L) {
            ++offset;
            c = (bytes[offset] & 0xFF);
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match8(final int name1, final byte c8) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 9;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 6L) != name1 || bytes[offset - 2] != c8 || bytes[offset - 1] != 34) {
            return false;
        }
        int c9 = bytes[offset] & 0xFF;
        if (c9 != 44 && c9 != 125 && c9 != 93) {
            return false;
        }
        if (c9 == 44) {
            this.comma = true;
            c9 = ((++offset == this.end) ? 26 : (bytes[offset] & 0xFF));
        }
        while (c9 <= 32 && (1L << c9 & 0x100003700L) != 0x0L) {
            ++offset;
            c9 = (bytes[offset] & 0xFF);
        }
        this.offset = offset + 1;
        this.ch = (char)c9;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match9(final int name1, final byte c8, final byte c9) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 10;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 7L) != name1 || bytes[offset - 3] != c8 || bytes[offset - 2] != c9 || bytes[offset - 1] != 34) {
            return false;
        }
        int c10 = bytes[offset] & 0xFF;
        if (c10 != 44 && c10 != 125 && c10 != 93) {
            return false;
        }
        if (c10 == 44) {
            this.comma = true;
            c10 = ((++offset == this.end) ? 26 : (bytes[offset] & 0xFF));
        }
        while (c10 <= 32 && (1L << c10 & 0x100003700L) != 0x0L) {
            ++offset;
            c10 = (bytes[offset] & 0xFF);
        }
        this.offset = offset + 1;
        this.ch = (char)c10;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match10(final long name1) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 11;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 8L) != name1) {
            return false;
        }
        int c = bytes[offset] & 0xFF;
        if (c != 44 && c != 125 && c != 93) {
            return false;
        }
        if (c == 44) {
            this.comma = true;
            c = ((++offset == this.end) ? 26 : (bytes[offset] & 0xFF));
        }
        while (c <= 32 && (1L << c & 0x100003700L) != 0x0L) {
            ++offset;
            c = (bytes[offset] & 0xFF);
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final boolean nextIfValue4Match11(final long name1) {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        offset += 12;
        if (offset >= this.end) {
            return false;
        }
        if (JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset - 9L) != name1 || bytes[offset - 1] != 34) {
            return false;
        }
        int c = bytes[offset] & 0xFF;
        if (c != 44 && c != 125 && c != 93) {
            return false;
        }
        if (c == 44) {
            this.comma = true;
            c = ((++offset == this.end) ? 26 : (bytes[offset] & 0xFF));
        }
        while (c <= 32 && (1L << c & 0x100003700L) != 0x0L) {
            ++offset;
            c = (bytes[offset] & 0xFF);
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public long readFieldNameHashCode() {
        final byte[] bytes = this.bytes;
        if (this.ch != '\"' && this.ch != '\'') {
            if ((this.context.features & Feature.AllowUnQuotedFieldNames.mask) != 0x0L && JSONReader.isFirstIdentifier(this.ch)) {
                return this.readFieldNameHashCodeUnquote();
            }
            if (this.ch == '}' || this.isNull()) {
                return -1L;
            }
            final String preFieldName;
            String errorMsg;
            if (this.ch == '[' && this.nameBegin > 0 && (preFieldName = this.getFieldName()) != null) {
                errorMsg = "illegal fieldName input " + this.ch + ", previous fieldName " + preFieldName;
            }
            else {
                errorMsg = "illegal fieldName input" + this.ch;
            }
            throw new JSONException(this.info(errorMsg));
        }
        else {
            final char quote = this.ch;
            this.nameAscii = true;
            this.nameEscape = false;
            final int offset2 = this.offset;
            this.nameBegin = offset2;
            int offset = offset2;
            long nameValue = 0L;
            if (offset + 9 < this.end) {
                final byte c0;
                if ((c0 = bytes[offset]) == quote) {
                    nameValue = 0L;
                }
                else {
                    final byte c2;
                    if ((c2 = bytes[offset + 1]) == quote && c0 != 92 && c0 > 0) {
                        nameValue = c0;
                        this.nameLength = 1;
                        this.nameEnd = offset + 1;
                        offset += 2;
                    }
                    else {
                        final byte c3;
                        if ((c3 = bytes[offset + 2]) == quote && c0 != 92 && c2 != 92 && c0 >= 0 && c2 > 0) {
                            nameValue = (c2 << 8) + c0;
                            this.nameLength = 2;
                            this.nameEnd = offset + 2;
                            offset += 3;
                        }
                        else {
                            final byte c4;
                            if ((c4 = bytes[offset + 3]) == quote && c0 != 92 && c2 != 92 && c3 != 92 && c0 >= 0 && c2 >= 0 && c3 > 0) {
                                nameValue = (c3 << 16) + (c2 << 8) + c0;
                                this.nameLength = 3;
                                this.nameEnd = offset + 3;
                                offset += 4;
                            }
                            else {
                                final byte c5;
                                if ((c5 = bytes[offset + 4]) == quote && c0 != 92 && c2 != 92 && c3 != 92 && c4 != 92 && c0 >= 0 && c2 >= 0 && c3 >= 0 && c4 > 0) {
                                    nameValue = (c4 << 24) + (c3 << 16) + (c2 << 8) + c0;
                                    this.nameLength = 4;
                                    this.nameEnd = offset + 4;
                                    offset += 5;
                                }
                                else {
                                    final byte c6;
                                    if ((c6 = bytes[offset + 5]) == quote && c0 != 92 && c2 != 92 && c3 != 92 && c4 != 92 && c5 != 92 && c0 >= 0 && c2 >= 0 && c3 >= 0 && c4 >= 0 && c5 > 0) {
                                        nameValue = ((long)c5 << 32) + (c4 << 24) + (c3 << 16) + (c2 << 8) + c0;
                                        this.nameLength = 5;
                                        this.nameEnd = offset + 5;
                                        offset += 6;
                                    }
                                    else {
                                        final byte c7;
                                        if ((c7 = bytes[offset + 6]) == quote && c0 != 92 && c2 != 92 && c3 != 92 && c4 != 92 && c5 != 92 && c6 != 92 && c0 >= 0 && c2 >= 0 && c3 >= 0 && c4 >= 0 && c5 >= 0 && c6 > 0) {
                                            nameValue = ((long)c6 << 40) + ((long)c5 << 32) + (c4 << 24) + (c3 << 16) + (c2 << 8) + c0;
                                            this.nameLength = 6;
                                            this.nameEnd = offset + 6;
                                            offset += 7;
                                        }
                                        else {
                                            final byte c8;
                                            if ((c8 = bytes[offset + 7]) == quote && c0 != 92 && c2 != 92 && c3 != 92 && c4 != 92 && c5 != 92 && c6 != 92 && c7 != 92 && c0 >= 0 && c2 >= 0 && c3 >= 0 && c4 >= 0 && c5 >= 0 && c6 >= 0 && c7 > 0) {
                                                nameValue = ((long)c7 << 48) + ((long)c6 << 40) + ((long)c5 << 32) + (c4 << 24) + (c3 << 16) + (c2 << 8) + c0;
                                                this.nameLength = 7;
                                                this.nameEnd = offset + 7;
                                                offset += 8;
                                            }
                                            else if (bytes[offset + 8] == quote && c0 != 92 && c2 != 92 && c3 != 92 && c4 != 92 && c5 != 92 && c6 != 92 && c7 != 92 && c8 != 92 && c0 >= 0 && c2 >= 0 && c3 >= 0 && c4 >= 0 && c5 >= 0 && c6 >= 0 && c7 >= 0 && c8 > 0) {
                                                nameValue = ((long)c8 << 56) + ((long)c7 << 48) + ((long)c6 << 40) + ((long)c5 << 32) + (c4 << 24) + (c3 << 16) + (c2 << 8) + c0;
                                                this.nameLength = 8;
                                                this.nameEnd = offset + 8;
                                                offset += 9;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (nameValue == 0L) {
                int i = 0;
                while (offset < this.end) {
                    int c9 = bytes[offset];
                    if (c9 == quote) {
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
                        if (c9 == 92) {
                            this.nameEscape = true;
                            c9 = bytes[++offset];
                            switch (c9) {
                                case 117: {
                                    final byte c10 = bytes[++offset];
                                    final byte c11 = bytes[++offset];
                                    final byte c12 = bytes[++offset];
                                    final byte c13 = bytes[++offset];
                                    c9 = JSONReader.char4(c10, c11, c12, c13);
                                    break;
                                }
                                case 120: {
                                    final byte c10 = bytes[++offset];
                                    final byte c11 = bytes[++offset];
                                    c9 = JSONReader.char2(c10, c11);
                                    break;
                                }
                                default: {
                                    c9 = this.char1(c9);
                                    break;
                                }
                            }
                            if (c9 > 255) {
                                this.nameAscii = false;
                            }
                        }
                        else if (c9 == -61 || c9 == -62) {
                            final byte c10 = bytes[++offset];
                            c9 = (char)((c9 & 0x1F) << 6 | (c10 & 0x3F));
                            this.nameAscii = false;
                        }
                        if (c9 > 255 || c9 < 0 || i >= 8 || (i == 0 && c9 == 0)) {
                            nameValue = 0L;
                            offset = this.nameBegin;
                            break;
                        }
                        switch (i) {
                            case 0: {
                                nameValue = (byte)c9;
                                break;
                            }
                            case 1: {
                                nameValue = ((byte)c9 << 8) + (nameValue & 0xFFL);
                                break;
                            }
                            case 2: {
                                nameValue = ((byte)c9 << 16) + (nameValue & 0xFFFFL);
                                break;
                            }
                            case 3: {
                                nameValue = ((byte)c9 << 24) + (nameValue & 0xFFFFFFL);
                                break;
                            }
                            case 4: {
                                nameValue = ((long)(byte)c9 << 32) + (nameValue & 0xFFFFFFFFL);
                                break;
                            }
                            case 5: {
                                nameValue = ((long)(byte)c9 << 40) + (nameValue & 0xFFFFFFFFFFL);
                                break;
                            }
                            case 6: {
                                nameValue = ((long)(byte)c9 << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                                break;
                            }
                            case 7: {
                                nameValue = ((long)(byte)c9 << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
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
                    int c14 = bytes[offset];
                    if (c14 == 92) {
                        this.nameEscape = true;
                        c14 = bytes[++offset];
                        switch (c14) {
                            case 117: {
                                final byte c15 = bytes[++offset];
                                final byte c16 = bytes[++offset];
                                final byte c17 = bytes[++offset];
                                final byte c18 = bytes[++offset];
                                c14 = JSONReader.char4(c15, c16, c17, c18);
                                break;
                            }
                            case 120: {
                                final byte c15 = bytes[++offset];
                                final byte c16 = bytes[++offset];
                                c14 = JSONReader.char2(c15, c16);
                                break;
                            }
                            default: {
                                c14 = this.char1(c14);
                                break;
                            }
                        }
                        ++offset;
                        hashCode ^= c14;
                        hashCode *= 1099511628211L;
                    }
                    else {
                        if (c14 == quote) {
                            this.nameLength = j;
                            this.nameEnd = offset;
                            ++offset;
                            break;
                        }
                        if (c14 >= 0) {
                            ++offset;
                        }
                        else {
                            c14 &= 0xFF;
                            switch (c14 >> 4) {
                                case 12:
                                case 13: {
                                    final int c19 = bytes[offset + 1];
                                    if ((c19 & 0xC0) != 0x80) {
                                        throw new JSONException("malformed input around byte " + offset);
                                    }
                                    c14 = (char)((c14 & 0x1F) << 6 | (c19 & 0x3F));
                                    offset += 2;
                                    this.nameAscii = false;
                                    break;
                                }
                                case 14: {
                                    final int c19 = bytes[offset + 1];
                                    final int c20 = bytes[offset + 2];
                                    if ((c19 & 0xC0) != 0x80 || (c20 & 0xC0) != 0x80) {
                                        throw new JSONException("malformed input around byte " + offset);
                                    }
                                    c14 = (char)((c14 & 0xF) << 12 | (c19 & 0x3F) << 6 | (c20 & 0x3F));
                                    offset += 3;
                                    this.nameAscii = false;
                                    break;
                                }
                                default: {
                                    throw new JSONException("malformed input around byte " + offset);
                                }
                            }
                        }
                        hashCode ^= c14;
                        hashCode *= 1099511628211L;
                    }
                    ++j;
                }
            }
            byte c21;
            for (c21 = bytes[offset]; c21 <= 32 && (1L << c21 & 0x100003700L) != 0x0L; c21 = bytes[offset]) {
                ++offset;
            }
            if (c21 != 58) {
                throw new JSONException(this.info("expect ':', but " + c21));
            }
            if (++offset == this.end) {
                c21 = 26;
            }
            else {
                c21 = bytes[offset];
            }
            while (c21 <= 32 && (1L << c21 & 0x100003700L) != 0x0L) {
                ++offset;
                c21 = bytes[offset];
            }
            this.offset = offset + 1;
            this.ch = (char)c21;
            return hashCode;
        }
    }
    
    @Override
    public long readValueHashCode() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException(this.info("illegal character " + this.ch));
        }
        final char quote = this.ch;
        this.nameAscii = true;
        this.nameEscape = false;
        final int offset2 = this.offset;
        this.nameBegin = offset2;
        int offset = offset2;
        long nameValue = 0L;
        int i = 0;
        while (offset < this.end) {
            int c = this.bytes[offset];
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
                if (c == 92) {
                    this.nameEscape = true;
                    c = this.bytes[++offset];
                    switch (c) {
                        case 117: {
                            final byte c2 = this.bytes[++offset];
                            final byte c3 = this.bytes[++offset];
                            final byte c4 = this.bytes[++offset];
                            final byte c5 = this.bytes[++offset];
                            c = JSONReader.char4(c2, c3, c4, c5);
                            break;
                        }
                        case 120: {
                            final byte c2 = this.bytes[++offset];
                            final byte c3 = this.bytes[++offset];
                            c = JSONReader.char2(c2, c3);
                            break;
                        }
                        default: {
                            c = this.char1(c);
                            break;
                        }
                    }
                }
                else if (c == -61 || c == -62) {
                    final byte c2 = this.bytes[++offset];
                    c = (char)((c & 0x1F) << 6 | (c2 & 0x3F));
                }
                if (c > 255 || c < 0 || i >= 8 || (i == 0 && c == 0)) {
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
                int c6 = this.bytes[offset];
                Label_1305: {
                    if (c6 == 92) {
                        this.nameEscape = true;
                        c6 = this.bytes[++offset];
                        switch (c6) {
                            case 117: {
                                final byte c7 = this.bytes[++offset];
                                final byte c8 = this.bytes[++offset];
                                final byte c9 = this.bytes[++offset];
                                final byte c10 = this.bytes[++offset];
                                c6 = JSONReader.char4(c7, c8, c9, c10);
                                break;
                            }
                            case 120: {
                                final byte c7 = this.bytes[++offset];
                                final byte c8 = this.bytes[++offset];
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
                        if (c6 == 34) {
                            this.nameLength = j;
                            this.nameEnd = offset;
                            ++offset;
                            break;
                        }
                        if (c6 >= 0) {
                            ++offset;
                        }
                        else {
                            switch ((c6 & 0xFF) >> 4) {
                                case 12:
                                case 13: {
                                    final int c11 = this.bytes[offset + 1];
                                    if ((c11 & 0xC0) != 0x80) {
                                        throw new JSONException("malformed input around byte " + offset);
                                    }
                                    c6 = (char)((c6 & 0x1F) << 6 | (c11 & 0x3F));
                                    offset += 2;
                                    this.nameAscii = false;
                                    break;
                                }
                                case 14: {
                                    final int c11 = this.bytes[offset + 1];
                                    final int c12 = this.bytes[offset + 2];
                                    if ((c11 & 0xC0) != 0x80 || (c12 & 0xC0) != 0x80) {
                                        throw new JSONException("malformed input around byte " + offset);
                                    }
                                    c6 = (char)((c6 & 0xF) << 12 | (c11 & 0x3F) << 6 | (c12 & 0x3F));
                                    offset += 3;
                                    this.nameAscii = false;
                                    break;
                                }
                                default: {
                                    if (c6 >> 3 != -2) {
                                        throw new JSONException("malformed input around byte " + offset);
                                    }
                                    ++offset;
                                    final int c11 = this.bytes[offset++];
                                    final int c12 = this.bytes[offset++];
                                    final int c13 = this.bytes[offset++];
                                    final int uc = c6 << 18 ^ c11 << 12 ^ c12 << 6 ^ (c13 ^ 0x381F80);
                                    if ((c11 & 0xC0) != 0x80 || (c12 & 0xC0) != 0x80 || (c13 & 0xC0) != 0x80 || uc < 65536 || uc >= 1114112) {
                                        throw new JSONException("malformed input around byte " + offset);
                                    }
                                    final char x1 = (char)((uc >>> 10) + 55232);
                                    final char x2 = (char)((uc & 0x3FF) + 56320);
                                    hashCode ^= x1;
                                    hashCode *= 1099511628211L;
                                    hashCode ^= x2;
                                    hashCode *= 1099511628211L;
                                    ++j;
                                    break Label_1305;
                                }
                            }
                        }
                        hashCode ^= c6;
                        hashCode *= 1099511628211L;
                    }
                }
                ++j;
            }
        }
        byte c14;
        if (offset == this.end) {
            c14 = 26;
        }
        else {
            c14 = this.bytes[offset];
        }
        while (c14 <= 32 && (1L << c14 & 0x100003700L) != 0x0L) {
            ++offset;
            c14 = this.bytes[offset];
        }
        final boolean comma = c14 == 44;
        this.comma = comma;
        if (comma) {
            if (++offset == this.end) {
                c14 = 26;
            }
            else {
                c14 = this.bytes[offset];
            }
            while (c14 <= 32 && (1L << c14 & 0x100003700L) != 0x0L) {
                ++offset;
                c14 = this.bytes[offset];
            }
        }
        this.offset = offset + 1;
        this.ch = (char)c14;
        return hashCode;
    }
    
    @Override
    public long getNameHashCodeLCase() {
        long hashCode = -3750763034362895579L;
        int offset = this.nameBegin;
        long nameValue = 0L;
        int i = 0;
        while (offset < this.end) {
            int c = this.bytes[offset];
            if (c == 92) {
                c = this.bytes[++offset];
                switch (c) {
                    case 117: {
                        final int c2 = this.bytes[++offset];
                        final int c3 = this.bytes[++offset];
                        final int c4 = this.bytes[++offset];
                        final int c5 = this.bytes[++offset];
                        c = JSONReader.char4(c2, c3, c4, c5);
                        break;
                    }
                    case 120: {
                        final int c2 = this.bytes[++offset];
                        final int c3 = this.bytes[++offset];
                        c = JSONReader.char2(c2, c3);
                        break;
                    }
                    default: {
                        c = this.char1(c);
                        break;
                    }
                }
            }
            else if (c == -61 || c == -62) {
                final byte c6 = this.bytes[++offset];
                c = (char)((c & 0x1F) << 6 | (c6 & 0x3F));
            }
            else if (c == 34) {
                break;
            }
            if (i >= 8 || c > 255 || c < 0 || (i == 0 && c == 0)) {
                nameValue = 0L;
                offset = this.nameBegin;
                break;
            }
            Label_0565: {
                if (c == 95 || c == 45 || c == 32) {
                    final byte c6 = this.bytes[offset + 1];
                    if (c6 != 34 && c6 != 39 && c6 != c) {
                        break Label_0565;
                    }
                }
                if (c >= 65 && c <= 90) {
                    c = (char)(c + 32);
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
        if (this.nameAscii && !this.nameEscape) {
            for (i = this.nameBegin; i < this.nameEnd; ++i) {
                char c7 = (char)this.bytes[i];
                if (c7 >= 'A' && c7 <= 'Z') {
                    c7 += ' ';
                }
                if (c7 == '_' || c7 == '-' || c7 == ' ') {
                    final byte c6 = this.bytes[i + 1];
                    if (c6 != 34 && c6 != 39 && c6 != c7) {
                        continue;
                    }
                }
                hashCode ^= c7;
                hashCode *= 1099511628211L;
            }
            return hashCode;
        }
        while (true) {
            int c8 = this.bytes[offset];
            if (c8 == 92) {
                c8 = (char)this.bytes[++offset];
                switch (c8) {
                    case 117: {
                        final int c9 = this.bytes[++offset];
                        final int c10 = this.bytes[++offset];
                        final int c11 = this.bytes[++offset];
                        final int c12 = this.bytes[++offset];
                        c8 = JSONReader.char4(c9, c10, c11, c12);
                        break;
                    }
                    case 120: {
                        final int c9 = this.bytes[++offset];
                        final int c10 = this.bytes[++offset];
                        c8 = JSONReader.char2(c9, c10);
                        break;
                    }
                    default: {
                        c8 = this.char1(c8);
                        break;
                    }
                }
                ++offset;
            }
            else {
                if (c8 == 34) {
                    return hashCode;
                }
                if (c8 >= 0) {
                    if (c8 >= 65 && c8 <= 90) {
                        c8 = (char)(c8 + 32);
                    }
                    ++offset;
                }
                else {
                    c8 &= 0xFF;
                    switch (c8 >> 4) {
                        case 12:
                        case 13: {
                            final int c13 = this.bytes[offset + 1];
                            c8 = (char)((c8 & 0x1F) << 6 | (c13 & 0x3F));
                            offset += 2;
                            break;
                        }
                        case 14: {
                            final int c13 = this.bytes[offset + 1];
                            final int c14 = this.bytes[offset + 2];
                            c8 = (char)((c8 & 0xF) << 12 | (c13 & 0x3F) << 6 | (c14 & 0x3F));
                            offset += 3;
                            break;
                        }
                        default: {
                            throw new JSONException("malformed input around byte " + offset);
                        }
                    }
                }
            }
            if (c8 != 95 && c8 != 45) {
                if (c8 == 32) {
                    continue;
                }
                hashCode ^= c8;
                hashCode *= 1099511628211L;
            }
        }
    }
    
    @Override
    public String getFieldName() {
        final int length = this.nameEnd - this.nameBegin;
        if (!this.nameEscape) {
            if (this.nameAscii) {
                if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                    final char[] chars = new char[length];
                    for (int i = 0; i < length; ++i) {
                        chars[i] = (char)this.bytes[this.nameBegin + i];
                    }
                    return JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
                }
                if (JDKUtils.STRING_CREATOR_JDK11 != null) {
                    final byte[] bytes = Arrays.copyOfRange(this.bytes, this.nameBegin, this.nameEnd);
                    return JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
                }
            }
            return new String(this.bytes, this.nameBegin, length, this.nameAscii ? StandardCharsets.ISO_8859_1 : StandardCharsets.UTF_8);
        }
        final char[] chars = new char[this.nameLength];
        int offset = this.nameBegin;
        int j = 0;
        while (offset < this.nameEnd) {
            int c = this.bytes[offset];
            if (c < 0) {
                c &= 0xFF;
                switch (c >> 4) {
                    case 12:
                    case 13: {
                        final int char2 = this.bytes[offset + 1];
                        if ((char2 & 0xC0) != 0x80) {
                            throw new JSONException("malformed input around byte " + offset);
                        }
                        c = ((c & 0x1F) << 6 | (char2 & 0x3F));
                        offset += 2;
                        break;
                    }
                    case 14: {
                        final int char2 = this.bytes[offset + 1];
                        final int char3 = this.bytes[offset + 2];
                        if ((char2 & 0xC0) != 0x80 || (char3 & 0xC0) != 0x80) {
                            throw new JSONException("malformed input around byte " + (offset + 2));
                        }
                        c = ((c & 0xF) << 12 | (char2 & 0x3F) << 6 | (char3 & 0x3F));
                        offset += 3;
                        break;
                    }
                    default: {
                        throw new JSONException("malformed input around byte " + offset);
                    }
                }
                chars[j] = (char)c;
            }
            else {
                if (c == 92) {
                    c = (char)this.bytes[++offset];
                    switch (c) {
                        case 117: {
                            final int c2 = this.bytes[++offset];
                            final int c3 = this.bytes[++offset];
                            final int c4 = this.bytes[++offset];
                            final int c5 = this.bytes[++offset];
                            c = JSONReader.char4(c2, c3, c4, c5);
                            break;
                        }
                        case 120: {
                            final int c2 = this.bytes[++offset];
                            final int c3 = this.bytes[++offset];
                            c = JSONReader.char2(c2, c3);
                            break;
                        }
                        case 34:
                        case 42:
                        case 43:
                        case 45:
                        case 46:
                        case 47:
                        case 58:
                        case 60:
                        case 61:
                        case 62:
                        case 64:
                        case 92: {
                            break;
                        }
                        default: {
                            c = this.char1(c);
                            break;
                        }
                    }
                }
                else if (c == 34) {
                    break;
                }
                chars[j] = (char)c;
                ++offset;
            }
            ++j;
        }
        return new String(chars);
    }
    
    @Override
    public String readFieldName() {
        if (this.ch != '\"' && this.ch != '\'') {
            if ((this.context.features & Feature.AllowUnQuotedFieldNames.mask) != 0x0L && JSONReader.isFirstIdentifier(this.ch)) {
                return this.readFieldNameUnquote();
            }
            return null;
        }
        else {
            final char quote = this.ch;
            this.nameAscii = true;
            this.nameEscape = false;
            final int offset2 = this.offset;
            this.nameBegin = offset2;
            int offset = offset2;
            int i = 0;
            while (offset < this.end) {
                int c = this.bytes[offset];
                if (c == 92) {
                    this.nameEscape = true;
                    c = this.bytes[++offset];
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
                    ++offset;
                    for (c = (this.bytes[offset] & 0xFF); c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = (this.bytes[offset] & 0xFF)) {
                        ++offset;
                    }
                    if (c != 58) {
                        throw new JSONException("syntax error : " + offset);
                    }
                    if (++offset == this.end) {
                        c = 26;
                    }
                    else {
                        c = this.bytes[offset];
                    }
                    while (c <= 32 && (1L << c & 0x100003700L) != 0x0L) {
                        ++offset;
                        c = this.bytes[offset];
                    }
                    this.offset = offset + 1;
                    this.ch = (char)c;
                    break;
                }
                else if (c >= 0) {
                    ++offset;
                }
                else {
                    if (this.nameAscii) {
                        this.nameAscii = false;
                    }
                    c &= 0xFF;
                    switch (c >> 4) {
                        case 12:
                        case 13: {
                            offset += 2;
                            break;
                        }
                        case 14: {
                            offset += 3;
                            break;
                        }
                        default: {
                            throw new JSONException("malformed input around byte " + offset);
                        }
                    }
                }
                ++i;
            }
            if (this.nameEnd < this.nameBegin) {
                throw new JSONException("syntax error : " + offset);
            }
            final int length = this.nameEnd - this.nameBegin;
            if (!this.nameEscape) {
                if (this.nameAscii) {
                    long nameValue0 = -1L;
                    long nameValue2 = -1L;
                    switch (length) {
                        case 1: {
                            return TypeUtils.toString((char)(this.bytes[this.nameBegin] & 0xFF));
                        }
                        case 2: {
                            return TypeUtils.toString((char)(this.bytes[this.nameBegin] & 0xFF), (char)(this.bytes[this.nameBegin + 1] & 0xFF));
                        }
                        case 3: {
                            nameValue0 = (this.bytes[this.nameBegin + 2] << 16) + (this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            break;
                        }
                        case 4: {
                            nameValue0 = (this.bytes[this.nameBegin + 3] << 24) + (this.bytes[this.nameBegin + 2] << 16) + (this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            break;
                        }
                        case 5: {
                            nameValue0 = ((long)this.bytes[this.nameBegin + 4] << 32) + ((long)this.bytes[this.nameBegin + 3] << 24) + ((long)this.bytes[this.nameBegin + 2] << 16) + ((long)this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            break;
                        }
                        case 6: {
                            nameValue0 = ((long)this.bytes[this.nameBegin + 5] << 40) + ((long)this.bytes[this.nameBegin + 4] << 32) + ((long)this.bytes[this.nameBegin + 3] << 24) + ((long)this.bytes[this.nameBegin + 2] << 16) + ((long)this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            break;
                        }
                        case 7: {
                            nameValue0 = ((long)this.bytes[this.nameBegin + 6] << 48) + ((long)this.bytes[this.nameBegin + 5] << 40) + ((long)this.bytes[this.nameBegin + 4] << 32) + ((long)this.bytes[this.nameBegin + 3] << 24) + ((long)this.bytes[this.nameBegin + 2] << 16) + ((long)this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            break;
                        }
                        case 8: {
                            nameValue0 = ((long)this.bytes[this.nameBegin + 7] << 56) + ((long)this.bytes[this.nameBegin + 6] << 48) + ((long)this.bytes[this.nameBegin + 5] << 40) + ((long)this.bytes[this.nameBegin + 4] << 32) + ((long)this.bytes[this.nameBegin + 3] << 24) + ((long)this.bytes[this.nameBegin + 2] << 16) + ((long)this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            break;
                        }
                        case 9: {
                            nameValue0 = this.bytes[this.nameBegin];
                            nameValue2 = ((long)this.bytes[this.nameBegin + 8] << 56) + ((long)this.bytes[this.nameBegin + 7] << 48) + ((long)this.bytes[this.nameBegin + 6] << 40) + ((long)this.bytes[this.nameBegin + 5] << 32) + ((long)this.bytes[this.nameBegin + 4] << 24) + ((long)this.bytes[this.nameBegin + 3] << 16) + ((long)this.bytes[this.nameBegin + 2] << 8) + this.bytes[this.nameBegin + 1];
                            break;
                        }
                        case 10: {
                            nameValue0 = (this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            nameValue2 = ((long)this.bytes[this.nameBegin + 9] << 56) + ((long)this.bytes[this.nameBegin + 8] << 48) + ((long)this.bytes[this.nameBegin + 7] << 40) + ((long)this.bytes[this.nameBegin + 6] << 32) + ((long)this.bytes[this.nameBegin + 5] << 24) + ((long)this.bytes[this.nameBegin + 4] << 16) + ((long)this.bytes[this.nameBegin + 3] << 8) + this.bytes[this.nameBegin + 2];
                            break;
                        }
                        case 11: {
                            nameValue0 = (this.bytes[this.nameBegin + 2] << 16) + (this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            nameValue2 = ((long)this.bytes[this.nameBegin + 10] << 56) + ((long)this.bytes[this.nameBegin + 9] << 48) + ((long)this.bytes[this.nameBegin + 8] << 40) + ((long)this.bytes[this.nameBegin + 7] << 32) + ((long)this.bytes[this.nameBegin + 6] << 24) + ((long)this.bytes[this.nameBegin + 5] << 16) + ((long)this.bytes[this.nameBegin + 4] << 8) + this.bytes[this.nameBegin + 3];
                            break;
                        }
                        case 12: {
                            nameValue0 = (this.bytes[this.nameBegin + 3] << 24) + (this.bytes[this.nameBegin + 2] << 16) + (this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            nameValue2 = ((long)this.bytes[this.nameBegin + 11] << 56) + ((long)this.bytes[this.nameBegin + 10] << 48) + ((long)this.bytes[this.nameBegin + 9] << 40) + ((long)this.bytes[this.nameBegin + 8] << 32) + ((long)this.bytes[this.nameBegin + 7] << 24) + ((long)this.bytes[this.nameBegin + 6] << 16) + ((long)this.bytes[this.nameBegin + 5] << 8) + this.bytes[this.nameBegin + 4];
                            break;
                        }
                        case 13: {
                            nameValue0 = ((long)this.bytes[this.nameBegin + 4] << 32) + ((long)this.bytes[this.nameBegin + 3] << 24) + ((long)this.bytes[this.nameBegin + 2] << 16) + ((long)this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            nameValue2 = ((long)this.bytes[this.nameBegin + 12] << 56) + ((long)this.bytes[this.nameBegin + 11] << 48) + ((long)this.bytes[this.nameBegin + 10] << 40) + ((long)this.bytes[this.nameBegin + 9] << 32) + ((long)this.bytes[this.nameBegin + 8] << 24) + ((long)this.bytes[this.nameBegin + 7] << 16) + ((long)this.bytes[this.nameBegin + 6] << 8) + this.bytes[this.nameBegin + 5];
                            break;
                        }
                        case 14: {
                            nameValue0 = ((long)this.bytes[this.nameBegin + 5] << 40) + ((long)this.bytes[this.nameBegin + 4] << 32) + ((long)this.bytes[this.nameBegin + 3] << 24) + ((long)this.bytes[this.nameBegin + 2] << 16) + ((long)this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            nameValue2 = ((long)this.bytes[this.nameBegin + 13] << 56) + ((long)this.bytes[this.nameBegin + 12] << 48) + ((long)this.bytes[this.nameBegin + 11] << 40) + ((long)this.bytes[this.nameBegin + 10] << 32) + ((long)this.bytes[this.nameBegin + 9] << 24) + ((long)this.bytes[this.nameBegin + 8] << 16) + ((long)this.bytes[this.nameBegin + 8] << 8) + this.bytes[this.nameBegin + 6];
                            break;
                        }
                        case 15: {
                            nameValue0 = ((long)this.bytes[this.nameBegin + 6] << 48) + ((long)this.bytes[this.nameBegin + 5] << 40) + ((long)this.bytes[this.nameBegin + 4] << 32) + ((long)this.bytes[this.nameBegin + 3] << 24) + ((long)this.bytes[this.nameBegin + 2] << 16) + ((long)this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            nameValue2 = ((long)this.bytes[this.nameBegin + 14] << 56) + ((long)this.bytes[this.nameBegin + 13] << 48) + ((long)this.bytes[this.nameBegin + 12] << 40) + ((long)this.bytes[this.nameBegin + 11] << 32) + ((long)this.bytes[this.nameBegin + 10] << 24) + ((long)this.bytes[this.nameBegin + 9] << 16) + ((long)this.bytes[this.nameBegin + 8] << 8) + this.bytes[this.nameBegin + 7];
                            break;
                        }
                        case 16: {
                            nameValue0 = ((long)this.bytes[this.nameBegin + 7] << 56) + ((long)this.bytes[this.nameBegin + 6] << 48) + ((long)this.bytes[this.nameBegin + 5] << 40) + ((long)this.bytes[this.nameBegin + 4] << 32) + ((long)this.bytes[this.nameBegin + 3] << 24) + ((long)this.bytes[this.nameBegin + 2] << 16) + ((long)this.bytes[this.nameBegin + 1] << 8) + this.bytes[this.nameBegin];
                            nameValue2 = ((long)this.bytes[this.nameBegin + 15] << 56) + ((long)this.bytes[this.nameBegin + 14] << 48) + ((long)this.bytes[this.nameBegin + 13] << 40) + ((long)this.bytes[this.nameBegin + 12] << 32) + ((long)this.bytes[this.nameBegin + 11] << 24) + ((long)this.bytes[this.nameBegin + 10] << 16) + ((long)this.bytes[this.nameBegin + 9] << 8) + this.bytes[this.nameBegin + 8];
                            break;
                        }
                    }
                    if (nameValue0 != -1L) {
                        if (nameValue2 != -1L) {
                            final int indexMask = (int)nameValue2 & JSONFactory.NAME_CACHE2.length - 1;
                            final JSONFactory.NameCacheEntry2 entry = JSONFactory.NAME_CACHE2[indexMask];
                            if (entry == null) {
                                String name;
                                if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                                    final char[] chars = new char[length];
                                    for (int j = 0; j < length; ++j) {
                                        chars[j] = (char)this.bytes[this.nameBegin + j];
                                    }
                                    name = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
                                }
                                else {
                                    name = new String(this.bytes, this.nameBegin, length, StandardCharsets.ISO_8859_1);
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
                                if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                                    final char[] chars = new char[length];
                                    for (int j = 0; j < length; ++j) {
                                        chars[j] = (char)this.bytes[this.nameBegin + j];
                                    }
                                    name = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
                                }
                                else {
                                    name = new String(this.bytes, this.nameBegin, length, StandardCharsets.ISO_8859_1);
                                }
                                JSONFactory.NAME_CACHE[indexMask] = new JSONFactory.NameCacheEntry(name, nameValue0);
                                return name;
                            }
                            if (entry2.value == nameValue0) {
                                return entry2.name;
                            }
                        }
                    }
                    if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                        final char[] chars2 = new char[length];
                        for (int k = 0; k < length; ++k) {
                            chars2[k] = (char)this.bytes[this.nameBegin + k];
                        }
                        return JDKUtils.STRING_CREATOR_JDK8.apply(chars2, Boolean.TRUE);
                    }
                    if (JDKUtils.STRING_CREATOR_JDK11 != null) {
                        final byte[] bytes = Arrays.copyOfRange(this.bytes, this.nameBegin, this.nameEnd);
                        return JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
                    }
                }
                return new String(this.bytes, this.nameBegin, length, this.nameAscii ? StandardCharsets.ISO_8859_1 : StandardCharsets.UTF_8);
            }
            return this.getFieldName();
        }
    }
    
    @Override
    public final int readInt32Value() {
        boolean negative = false;
        final int firstOffset = this.offset;
        final char firstChar = this.ch;
        final byte[] bytes = this.bytes;
        int intValue = 0;
        char quote = '\0';
        if (firstChar == '\"' || firstChar == '\'') {
            quote = this.ch;
            this.ch = (char)bytes[this.offset++];
        }
        if (this.ch == '-') {
            negative = true;
            this.ch = (char)bytes[this.offset++];
        }
        else if (this.ch == '+') {
            this.ch = (char)bytes[this.offset++];
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
            this.ch = (char)bytes[this.offset++];
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
                    return bigInteger.intValueExact();
                }
                catch (ArithmeticException ex) {
                    throw new JSONException("int overflow, value " + bigInteger);
                }
            }
            return this.getInt32Value();
        }
        if (quote != '\0') {
            this.wasNull = (firstOffset + 1 == this.offset);
            this.ch = ((this.offset == this.end) ? '\u001a' : ((char)bytes[this.offset++]));
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
                this.ch = (char)bytes[this.offset++];
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)bytes[this.offset++];
            }
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            this.ch = ((this.offset == this.end) ? '\u001a' : ((char)bytes[this.offset++]));
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)bytes[this.offset++];
                }
            }
        }
        return negative ? (-intValue) : intValue;
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
            this.ch = (char)this.bytes[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
                    while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                        if (this.offset >= this.end) {
                            this.ch = '\u001a';
                        }
                        else {
                            this.ch = (char)this.bytes[this.offset++];
                        }
                    }
                    this.nextIfComma();
                }
                return null;
            }
        }
        else if (this.ch == ',' || this.ch == '\r' || this.ch == '\n') {
            return null;
        }
        if (this.ch == '-') {
            negative = true;
            this.ch = (char)this.bytes[this.offset++];
        }
        else if (this.ch == '+') {
            this.ch = (char)this.bytes[this.offset++];
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
            this.ch = (char)this.bytes[this.offset++];
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
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
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
                    this.ch = (char)this.bytes[this.offset++];
                }
            }
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
                }
            }
            if (this.comma = (this.ch == ',')) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
                    while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                        if (this.offset >= this.end) {
                            this.ch = '\u001a';
                        }
                        else {
                            this.ch = (char)this.bytes[this.offset++];
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
        final byte[] bytes = this.bytes;
        long longValue = 0L;
        char quote = '\0';
        if (this.ch == '\"' || this.ch == '\'') {
            quote = this.ch;
            this.ch = (char)bytes[this.offset++];
        }
        if (this.ch == '-') {
            negative = true;
            this.ch = (char)bytes[this.offset++];
        }
        else if (this.ch == '+') {
            this.ch = (char)bytes[this.offset++];
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
            this.ch = (char)bytes[this.offset++];
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
            this.ch = ((this.offset == this.end) ? '\u001a' : ((char)bytes[this.offset++]));
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
                this.ch = (char)bytes[this.offset++];
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)bytes[this.offset++];
            }
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            this.ch = ((this.offset == this.end) ? '\u001a' : ((char)bytes[this.offset++]));
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)bytes[this.offset++];
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
            this.ch = (char)this.bytes[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
                }
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = (char)this.bytes[this.offset++];
                    }
                }
                this.nextIfComma();
                return null;
            }
        }
        else if (this.ch == ',' || this.ch == '\r' || this.ch == '\n') {
            return null;
        }
        if (this.ch == '-') {
            negative = true;
            this.ch = (char)this.bytes[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
                }
                this.nextIfComma();
                return null;
            }
        }
        else if (this.ch == '+') {
            this.ch = (char)this.bytes[this.offset++];
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
            this.ch = (char)this.bytes[this.offset++];
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
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)this.bytes[this.offset++];
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
                this.ch = (char)this.bytes[this.offset++];
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)this.bytes[this.offset++];
            }
        }
        if (this.comma = (this.ch == ',')) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)this.bytes[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = (char)this.bytes[this.offset++];
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
        final byte[] bytes = this.bytes;
        char quote = '\0';
        if (this.ch == '\"' || this.ch == '\'') {
            quote = this.ch;
            this.ch = (char)bytes[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)bytes[this.offset++];
                }
                this.nextIfComma();
                this.wasNull = true;
                return 0.0;
            }
        }
        final int start = this.offset;
        if (this.ch == '-') {
            this.negative = true;
            this.ch = (char)bytes[this.offset++];
        }
        else {
            this.negative = false;
            if (this.ch == '+') {
                this.ch = (char)bytes[this.offset++];
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
            this.ch = (char)bytes[this.offset++];
        }
        this.scale = 0;
        if (this.ch == '.') {
            this.valueType = 2;
            this.ch = (char)bytes[this.offset++];
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
                this.ch = (char)bytes[this.offset++];
            }
        }
        int expValue = 0;
        if (this.ch == 'e' || this.ch == 'E') {
            boolean negativeExp = false;
            this.ch = (char)bytes[this.offset++];
            if (this.ch == '-') {
                negativeExp = true;
                this.ch = (char)bytes[this.offset++];
            }
            else if (this.ch == '+') {
                this.ch = (char)bytes[this.offset++];
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
                this.ch = (char)bytes[this.offset++];
            }
            if (negativeExp) {
                expValue = -expValue;
            }
            this.exponent = (short)expValue;
            this.valueType = 2;
        }
        if (this.offset == start) {
            if (this.ch == 'n') {
                if (bytes[this.offset++] == 117 && bytes[this.offset++] == 108 && bytes[this.offset++] == 108) {
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
                        this.ch = (char)bytes[this.offset++];
                    }
                }
            }
            else if (this.ch == 't') {
                if (bytes[this.offset++] == 114 && bytes[this.offset++] == 117 && bytes[this.offset++] == 101) {
                    value = true;
                    doubleValue = 1.0;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = (char)bytes[this.offset++];
                    }
                }
            }
            else if (this.ch == 'f') {
                if (this.offset + 4 <= this.end && JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset) == IOUtils.ALSE) {
                    this.offset += 4;
                    doubleValue = 0.0;
                    value = true;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = (char)bytes[this.offset++];
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
                this.ch = (char)bytes[this.offset++];
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
            Label_1560: {
                if (!value) {
                    if (str != null) {
                        try {
                            doubleValue = Double.parseDouble(str);
                            break Label_1560;
                        }
                        catch (NumberFormatException ex) {
                            throw new JSONException(this.info(), ex);
                        }
                    }
                    doubleValue = TypeUtils.parseDouble(bytes, start - 1, len);
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
                    this.ch = (char)bytes[this.offset++];
                }
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)bytes[this.offset++];
            }
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)bytes[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = (char)bytes[this.offset++];
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
        final byte[] bytes = this.bytes;
        char quote = '\0';
        if (this.ch == '\"' || this.ch == '\'') {
            quote = this.ch;
            this.ch = (char)bytes[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)bytes[this.offset++];
                }
                this.nextIfComma();
                this.wasNull = true;
                return 0.0f;
            }
        }
        final int start = this.offset;
        if (this.ch == '-') {
            this.negative = true;
            this.ch = (char)bytes[this.offset++];
        }
        else {
            this.negative = false;
            if (this.ch == '+') {
                this.ch = (char)bytes[this.offset++];
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
            this.ch = (char)bytes[this.offset++];
        }
        this.scale = 0;
        if (this.ch == '.') {
            this.valueType = 2;
            this.ch = (char)bytes[this.offset++];
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
                this.ch = (char)bytes[this.offset++];
            }
        }
        int expValue = 0;
        if (this.ch == 'e' || this.ch == 'E') {
            boolean negativeExp = false;
            this.ch = (char)bytes[this.offset++];
            if (this.ch == '-') {
                negativeExp = true;
                this.ch = (char)bytes[this.offset++];
            }
            else if (this.ch == '+') {
                this.ch = (char)bytes[this.offset++];
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
                this.ch = (char)bytes[this.offset++];
            }
            if (negativeExp) {
                expValue = -expValue;
            }
            this.exponent = (short)expValue;
            this.valueType = 2;
        }
        if (this.offset == start) {
            if (this.ch == 'n') {
                if (bytes[this.offset++] == 117 && bytes[this.offset++] == 108 && bytes[this.offset++] == 108) {
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
                        this.ch = (char)bytes[this.offset++];
                    }
                }
            }
            else if (this.ch == 't') {
                if (bytes[this.offset++] == 114 && bytes[this.offset++] == 117 && bytes[this.offset++] == 101) {
                    value = true;
                    floatValue = 1.0f;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = (char)bytes[this.offset++];
                    }
                }
            }
            else if (this.ch == 'f') {
                if (this.offset + 4 <= this.end && JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset) == IOUtils.ALSE) {
                    this.offset += 4;
                    floatValue = 0.0f;
                    value = true;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = (char)bytes[this.offset++];
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
                this.ch = (char)bytes[this.offset++];
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
            Label_1503: {
                if (!value) {
                    if (str != null) {
                        try {
                            floatValue = Float.parseFloat(str);
                            break Label_1503;
                        }
                        catch (NumberFormatException ex) {
                            throw new JSONException(this.info(), ex);
                        }
                    }
                    floatValue = TypeUtils.parseFloat(bytes, start - 1, len);
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
                    this.ch = (char)bytes[this.offset++];
                }
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)bytes[this.offset++];
            }
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)bytes[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = (char)bytes[this.offset++];
                    }
                }
            }
        }
        return floatValue;
    }
    
    @Override
    public final void readString(final ValueConsumer consumer, final boolean quoted) {
        final char quote = this.ch;
        final int start;
        int offset = start = this.offset;
        this.valueEscape = false;
        int i = 0;
        while (true) {
            int c = this.bytes[offset];
            if (c == 92) {
                this.valueEscape = true;
                c = this.bytes[++offset];
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
            else if (c >= 0) {
                if (c == quote) {
                    final int valueLength = i;
                    if (this.valueEscape) {
                        final int bytesMaxiumLength = offset - this.offset;
                        final char[] chars = new char[valueLength];
                        offset = start;
                        int j = 0;
                        while (true) {
                            int c2 = this.bytes[offset];
                            if (c2 == 92) {
                                c2 = this.bytes[++offset];
                                switch (c2) {
                                    case 117: {
                                        final int c3 = this.bytes[++offset];
                                        final int c4 = this.bytes[++offset];
                                        final int c5 = this.bytes[++offset];
                                        final int c6 = this.bytes[++offset];
                                        c2 = JSONReader.char4(c3, c4, c5, c6);
                                        break;
                                    }
                                    case 120: {
                                        final int c3 = this.bytes[++offset];
                                        final int c4 = this.bytes[++offset];
                                        c2 = JSONReader.char2(c3, c4);
                                        break;
                                    }
                                    case 34:
                                    case 92: {
                                        break;
                                    }
                                    default: {
                                        c2 = this.char1(c2);
                                        break;
                                    }
                                }
                            }
                            else if (c2 == 34) {
                                if (quoted) {
                                    final JSONWriter jsonWriter = JSONWriter.of();
                                    jsonWriter.writeString(chars, 0, chars.length);
                                    final byte[] bytes = jsonWriter.getBytes();
                                    consumer.accept(bytes, 0, bytes.length);
                                }
                                else {
                                    final byte[] bytes2 = new byte[bytesMaxiumLength];
                                    final int bytesLength = IOUtils.encodeUTF8(chars, 0, chars.length, bytes2, 0);
                                    consumer.accept(bytes2, 0, bytesLength);
                                }
                                break;
                            }
                            if (c2 >= 0) {
                                chars[j] = (char)c2;
                                ++offset;
                            }
                            else {
                                switch ((c2 & 0xFF) >> 4) {
                                    case 12:
                                    case 13: {
                                        ++offset;
                                        final int c7 = this.bytes[offset++];
                                        chars[j] = (char)((c2 & 0x1F) << 6 | (c7 & 0x3F));
                                        break;
                                    }
                                    case 14: {
                                        ++offset;
                                        final int c7 = this.bytes[offset++];
                                        final int c8 = this.bytes[offset++];
                                        chars[j] = (char)((c2 & 0xF) << 12 | (c7 & 0x3F) << 6 | (c8 & 0x3F));
                                        break;
                                    }
                                    default: {
                                        if (c2 >> 3 != -2) {
                                            throw new JSONException("malformed input around byte " + offset);
                                        }
                                        ++offset;
                                        final int c7 = this.bytes[offset++];
                                        final int c8 = this.bytes[offset++];
                                        final int c9 = this.bytes[offset++];
                                        final int uc = c2 << 18 ^ c7 << 12 ^ c8 << 6 ^ (c9 ^ 0x381F80);
                                        if ((c7 & 0xC0) != 0x80 || (c8 & 0xC0) != 0x80 || (c9 & 0xC0) != 0x80 || uc < 65536 || uc >= 1114112) {
                                            throw new JSONException("malformed input around byte " + offset);
                                        }
                                        chars[j++] = (char)((uc >>> 10) + 55232);
                                        chars[j] = (char)((uc & 0x3FF) + 56320);
                                        break;
                                    }
                                }
                            }
                            ++j;
                        }
                    }
                    else {
                        final int consumStart = quoted ? (this.offset - 1) : this.offset;
                        final int consumLen = quoted ? (offset - this.offset + 2) : (offset - this.offset);
                        if (quoted && quote == '\'') {
                            final byte[] quotedBytes = new byte[consumLen];
                            System.arraycopy(this.bytes, this.offset - 1, quotedBytes, 0, consumLen);
                            quotedBytes[0] = 34;
                            quotedBytes[quotedBytes.length - 1] = 34;
                            consumer.accept(quotedBytes, 0, quotedBytes.length);
                        }
                        else {
                            consumer.accept(this.bytes, consumStart, consumLen);
                        }
                    }
                    int b;
                    for (b = this.bytes[++offset]; b <= 32 && (1L << b & 0x100003700L) != 0x0L; b = this.bytes[++offset]) {}
                    final boolean comma = b == 44;
                    this.comma = comma;
                    if (comma) {
                        this.offset = offset + 1;
                        this.next();
                    }
                    else {
                        this.offset = offset + 1;
                        this.ch = (char)b;
                    }
                    return;
                }
                ++offset;
            }
            else {
                switch ((c & 0xFF) >> 4) {
                    case 12:
                    case 13: {
                        offset += 2;
                        break;
                    }
                    case 14: {
                        offset += 3;
                        break;
                    }
                    default: {
                        if (c >> 3 == -2) {
                            offset += 4;
                            ++i;
                            break;
                        }
                        throw new JSONException("malformed input around byte " + offset);
                    }
                }
            }
            ++i;
        }
    }
    
    protected void readString0() {
        final char quote = this.ch;
        final int start;
        int offset = start = this.offset;
        boolean ascii = true;
        this.valueEscape = false;
        int i = 0;
        while (true) {
            int c = this.bytes[offset];
            if (c == 92) {
                this.valueEscape = true;
                c = this.bytes[++offset];
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
            else if (c >= 0) {
                if (c == quote) {
                    final int valueLength = i;
                    String str;
                    if (this.valueEscape) {
                        final char[] chars = new char[valueLength];
                        offset = start;
                        int j = 0;
                        while (true) {
                            int c2 = this.bytes[offset];
                            if (c2 == 92) {
                                c2 = this.bytes[++offset];
                                switch (c2) {
                                    case 117: {
                                        final int c3 = this.bytes[++offset];
                                        final int c4 = this.bytes[++offset];
                                        final int c5 = this.bytes[++offset];
                                        final int c6 = this.bytes[++offset];
                                        c2 = JSONReader.char4(c3, c4, c5, c6);
                                        break;
                                    }
                                    case 120: {
                                        final int c3 = this.bytes[++offset];
                                        final int c4 = this.bytes[++offset];
                                        c2 = JSONReader.char2(c3, c4);
                                        break;
                                    }
                                    case 34:
                                    case 92: {
                                        break;
                                    }
                                    default: {
                                        c2 = this.char1(c2);
                                        break;
                                    }
                                }
                                chars[j] = (char)c2;
                                ++offset;
                            }
                            else {
                                if (c2 == 34) {
                                    str = new String(chars);
                                    break;
                                }
                                if (c2 >= 0) {
                                    chars[j] = (char)c2;
                                    ++offset;
                                }
                                else {
                                    switch ((c2 & 0xFF) >> 4) {
                                        case 12:
                                        case 13: {
                                            ++offset;
                                            final int c7 = this.bytes[offset++];
                                            chars[j] = (char)((c2 & 0x1F) << 6 | (c7 & 0x3F));
                                            break;
                                        }
                                        case 14: {
                                            ++offset;
                                            final int c7 = this.bytes[offset++];
                                            final int c8 = this.bytes[offset++];
                                            chars[j] = (char)((c2 & 0xF) << 12 | (c7 & 0x3F) << 6 | (c8 & 0x3F));
                                            break;
                                        }
                                        default: {
                                            if (c2 >> 3 != -2) {
                                                throw new JSONException("malformed input around byte " + offset);
                                            }
                                            ++offset;
                                            final int c7 = this.bytes[offset++];
                                            final int c8 = this.bytes[offset++];
                                            final int c9 = this.bytes[offset++];
                                            final int uc = c2 << 18 ^ c7 << 12 ^ c8 << 6 ^ (c9 ^ 0x381F80);
                                            if ((c7 & 0xC0) != 0x80 || (c8 & 0xC0) != 0x80 || (c9 & 0xC0) != 0x80 || uc < 65536 || uc >= 1114112) {
                                                throw new JSONException("malformed input around byte " + offset);
                                            }
                                            chars[j++] = (char)((uc >>> 10) + 55232);
                                            chars[j] = (char)((uc & 0x3FF) + 56320);
                                            break;
                                        }
                                    }
                                }
                            }
                            ++j;
                        }
                    }
                    else if (ascii) {
                        str = new String(this.bytes, this.offset, offset - this.offset, StandardCharsets.ISO_8859_1);
                    }
                    else {
                        str = new String(this.bytes, this.offset, offset - this.offset, StandardCharsets.UTF_8);
                    }
                    int b;
                    for (b = this.bytes[++offset]; b <= 32 && (1L << b & 0x100003700L) != 0x0L; b = this.bytes[++offset]) {}
                    this.comma = (b == 44);
                    if (b == 44) {
                        this.offset = offset + 1;
                        this.next();
                    }
                    else {
                        this.offset = offset + 1;
                        this.ch = (char)b;
                    }
                    this.stringValue = str;
                    return;
                }
                ++offset;
            }
            else {
                switch ((c & 0xFF) >> 4) {
                    case 12:
                    case 13: {
                        offset += 2;
                        ascii = false;
                        break;
                    }
                    case 14: {
                        offset += 3;
                        ascii = false;
                        break;
                    }
                    default: {
                        if (c >> 3 == -2) {
                            offset += 4;
                            ++i;
                            ascii = false;
                            break;
                        }
                        throw new JSONException("malformed input around byte " + offset);
                    }
                }
            }
            ++i;
        }
    }
    
    @Override
    public final boolean skipName() {
        if (this.ch != '\"') {
            throw new JSONException("not support unquoted name");
        }
        int offset = this.offset;
        while (true) {
            final byte c = this.bytes[offset];
            if (c == 92) {
                offset += 2;
            }
            else {
                if (c == 34) {
                    break;
                }
                ++offset;
            }
        }
        ++offset;
        byte c;
        for (c = this.bytes[offset]; c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = this.bytes[offset]) {
            ++offset;
        }
        if (c != 58) {
            throw new JSONException("syntax error, expect ',', but '" + c + "'");
        }
        ++offset;
        for (c = this.bytes[offset]; c <= 32 && (1L << c & 0x100003700L) != 0x0L; c = this.bytes[offset]) {
            ++offset;
        }
        this.offset = offset + 1;
        this.ch = (char)c;
        return true;
    }
    
    @Override
    public final void skipValue() {
        this.comma = false;
        switch (this.ch) {
            case '[': {
                this.next();
                int i = 0;
                while (this.ch != ']') {
                    if (i != 0 && !this.comma) {
                        throw new JSONValidException("offset " + this.offset);
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
                    this.ch = (char)(this.bytes[this.offset++] & 0xFF);
                }
                final boolean dot = this.ch == '.';
                boolean num = false;
                Label_0708: {
                    if (!dot && this.ch >= '0' && this.ch <= '9') {
                        num = true;
                        while (this.offset < this.end) {
                            this.ch = (char)(this.bytes[this.offset++] & 0xFF);
                            if (this.ch < '0' || this.ch > '9') {
                                break Label_0708;
                            }
                        }
                        this.ch = '\u001a';
                        return;
                    }
                }
                if (num && (this.ch == 'L' || this.ch == 'F' || this.ch == 'D' || this.ch == 'B' || this.ch == 'S')) {
                    this.next();
                }
                boolean small = false;
                Label_0904: {
                    if (this.ch == '.') {
                        small = true;
                        if (this.offset >= this.end) {
                            this.ch = '\u001a';
                            return;
                        }
                        this.ch = (char)(this.bytes[this.offset++] & 0xFF);
                        if (this.ch >= '0' && this.ch <= '9') {
                            while (this.offset < this.end) {
                                this.ch = (char)(this.bytes[this.offset++] & 0xFF);
                                if (this.ch < '0' || this.ch > '9') {
                                    break Label_0904;
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
                Label_1228: {
                    if (this.ch == 'e' || this.ch == 'E') {
                        this.ch = (char)(this.bytes[this.offset++] & 0xFF);
                        boolean eSign = false;
                        if (this.ch == '+' || this.ch == '-') {
                            eSign = true;
                            if (this.offset >= this.end) {
                                throw new JSONException("illegal number, offset " + this.offset);
                            }
                            this.ch = (char)(this.bytes[this.offset++] & 0xFF);
                        }
                        if (this.ch >= '0' && this.ch <= '9') {
                            while (this.offset < this.end) {
                                this.ch = (char)(this.bytes[this.offset++] & 0xFF);
                                if (this.ch < '0') {
                                    break Label_1228;
                                }
                                if (this.ch > '9') {
                                    break Label_1228;
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
                    this.ch = (char)(this.bytes[this.offset++] & 0xFF);
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
                this.ch = (char)(this.bytes[this.offset] & 0xFF);
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    ++this.offset;
                    if (this.offset >= this.end) {
                        throw new JSONException("illegal number, offset " + this.offset);
                    }
                    this.ch = (char)(this.bytes[this.offset] & 0xFF);
                }
                this.comma = true;
                ++this.offset;
                return;
            }
            case 't': {
                if (this.offset + 3 > this.end) {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                if (this.bytes[this.offset] != 114 || this.bytes[this.offset + 1] != 117 || this.bytes[this.offset + 2] != 101) {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                this.offset += 3;
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                    return;
                }
                this.ch = (char)(this.bytes[this.offset++] & 0xFF);
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                        return;
                    }
                    this.ch = (char)(this.bytes[this.offset++] & 0xFF);
                }
                if (this.ch == '}' || this.ch == ']') {
                    return;
                }
                break;
            }
            case 'f': {
                if (this.offset + 4 >= this.end && JDKUtils.UNSAFE.getInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset) != IOUtils.ALSE) {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                this.offset += 4;
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                    return;
                }
                this.ch = (char)(this.bytes[this.offset++] & 0xFF);
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                        return;
                    }
                    this.ch = (char)(this.bytes[this.offset++] & 0xFF);
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
                if (this.bytes[this.offset] != 117 || this.bytes[this.offset + 1] != 108 || this.bytes[this.offset + 2] != 108) {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                this.offset += 3;
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                    return;
                }
                this.ch = (char)(this.bytes[this.offset++] & 0xFF);
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                        return;
                    }
                    this.ch = (char)(this.bytes[this.offset++] & 0xFF);
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
                throw new JSONException("TODO : " + this.ch);
            }
        }
        if (this.ch == ',') {
            this.comma = true;
            if (this.offset >= this.end) {
                throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
            }
            this.ch = (char)this.bytes[this.offset];
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                ++this.offset;
                if (this.offset >= this.end) {
                    throw new JSONException("error, offset " + this.offset + ", char " + this.ch);
                }
                this.ch = (char)this.bytes[this.offset];
            }
            ++this.offset;
        }
        else if (!this.comma && this.ch != '}' && this.ch != ']' && this.ch != '\u001a') {
            throw new JSONValidException("offset " + this.offset);
        }
    }
    
    @Override
    public final String getString() {
        if (this.stringValue != null) {
            return this.stringValue;
        }
        final int length = this.nameEnd - this.nameBegin;
        if (!this.nameEscape) {
            return new String(this.bytes, this.nameBegin, length, this.nameAscii ? StandardCharsets.ISO_8859_1 : StandardCharsets.UTF_8);
        }
        final char[] chars = new char[this.nameLength];
        int offset = this.nameBegin;
        int i = 0;
        while (true) {
            int c = this.bytes[offset];
            Label_0696: {
                if (c < 0) {
                    switch ((c & 0xFF) >> 4) {
                        case 12:
                        case 13: {
                            final int char2 = this.bytes[offset + 1];
                            if ((char2 & 0xC0) != 0x80) {
                                throw new JSONException("malformed input around byte " + offset);
                            }
                            c = ((c & 0x1F) << 6 | (char2 & 0x3F));
                            offset += 2;
                            break;
                        }
                        case 14: {
                            final int char2 = this.bytes[offset + 1];
                            final int char3 = this.bytes[offset + 2];
                            if ((char2 & 0xC0) != 0x80 || (char3 & 0xC0) != 0x80) {
                                throw new JSONException("malformed input around byte " + (offset + 2));
                            }
                            c = ((c & 0xF) << 12 | (char2 & 0x3F) << 6 | (char3 & 0x3F));
                            offset += 3;
                            break;
                        }
                        default: {
                            if (c >> 3 != -2) {
                                c &= 0xFF;
                                ++offset;
                                break;
                            }
                            ++offset;
                            final int c2 = this.bytes[offset++];
                            final int c3 = this.bytes[offset++];
                            final int c4 = this.bytes[offset++];
                            final int uc = c << 18 ^ c2 << 12 ^ c3 << 6 ^ (c4 ^ 0x381F80);
                            if ((c2 & 0xC0) != 0x80 || (c3 & 0xC0) != 0x80 || (c4 & 0xC0) != 0x80 || uc < 65536 || uc >= 1114112) {
                                throw new JSONException("malformed input around byte " + offset);
                            }
                            chars[i++] = (char)((uc >>> 10) + 55232);
                            chars[i] = (char)((uc & 0x3FF) + 56320);
                            break Label_0696;
                        }
                    }
                    chars[i] = (char)c;
                }
                else {
                    if (c == 92) {
                        c = (char)this.bytes[++offset];
                        switch (c) {
                            case 117: {
                                final int c5 = this.bytes[++offset];
                                final int c6 = this.bytes[++offset];
                                final int c7 = this.bytes[++offset];
                                final int c8 = this.bytes[++offset];
                                c = JSONReader.char4(c5, c6, c7, c8);
                                break;
                            }
                            case 120: {
                                final int c5 = this.bytes[++offset];
                                final int c6 = this.bytes[++offset];
                                c = JSONReader.char2(c5, c6);
                                break;
                            }
                            case 34:
                            case 92: {
                                break;
                            }
                            default: {
                                c = this.char1(c);
                                break;
                            }
                        }
                    }
                    else if (c == 34) {
                        return this.stringValue = new String(chars);
                    }
                    chars[i] = (char)c;
                    ++offset;
                }
            }
            ++i;
        }
    }
    
    protected final void skipString() {
        final byte quote;
        byte ch = quote = (byte)this.ch;
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        ch = bytes[offset++];
        while (true) {
            if (ch == 92) {
                ch = bytes[offset++];
                if (ch == 92 || ch == 34) {
                    ch = bytes[offset++];
                }
                else if (ch == 117) {
                    offset += 4;
                    ch = bytes[offset++];
                }
                else {
                    this.char1(ch);
                }
            }
            else {
                if (ch == quote) {
                    ch = (byte)((offset < this.end) ? bytes[offset++] : 26);
                    break;
                }
                if (offset >= this.end) {
                    ch = 26;
                    break;
                }
                ch = bytes[offset++];
            }
        }
        while (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L) {
            ch = bytes[offset++];
        }
        if (this.comma = (ch == 44)) {
            if (offset >= this.end) {
                this.offset = offset;
                this.ch = '\u001a';
                return;
            }
            for (ch = bytes[offset]; ch <= 32 && (1L << ch & 0x100003700L) != 0x0L; ch = bytes[offset]) {
                if (++offset >= this.end) {
                    this.ch = '\u001a';
                    this.offset = offset;
                    return;
                }
            }
            ++offset;
        }
        this.offset = offset;
        this.ch = (char)ch;
    }
    
    @Override
    public final void skipLineComment() {
        while (this.ch != '\n') {
            ++this.offset;
            if (this.offset >= this.end) {
                this.ch = '\u001a';
                return;
            }
            this.ch = (char)this.bytes[this.offset];
        }
        ++this.offset;
        if (this.offset >= this.end) {
            this.ch = '\u001a';
            return;
        }
        this.ch = (char)this.bytes[this.offset];
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            ++this.offset;
            if (this.offset >= this.end) {
                this.ch = '\u001a';
                return;
            }
            this.ch = (char)this.bytes[this.offset];
        }
        ++this.offset;
    }
    
    @Override
    public String readString() {
        if (this.ch == '\"' || this.ch == '\'') {
            final char quote = this.ch;
            final int start;
            int offset = start = this.offset;
            boolean ascii = true;
            this.valueEscape = false;
            int i = 0;
            while (offset < this.end) {
                int c = this.bytes[offset];
                if (c == 92) {
                    this.valueEscape = true;
                    c = this.bytes[++offset];
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
                else if (c >= 0) {
                    if (c == quote) {
                        final int valueLength = i;
                        String str;
                        if (this.valueEscape) {
                            final char[] chars = new char[valueLength];
                            offset = start;
                            int j = 0;
                            while (true) {
                                int c2 = this.bytes[offset];
                                if (c2 == 92) {
                                    c2 = this.bytes[++offset];
                                    switch (c2) {
                                        case 117: {
                                            final int c3 = this.bytes[++offset];
                                            final int c4 = this.bytes[++offset];
                                            final int c5 = this.bytes[++offset];
                                            final int c6 = this.bytes[++offset];
                                            c2 = JSONReader.char4(c3, c4, c5, c6);
                                            break;
                                        }
                                        case 120: {
                                            final int c3 = this.bytes[++offset];
                                            final int c4 = this.bytes[++offset];
                                            c2 = JSONReader.char2(c3, c4);
                                            break;
                                        }
                                        case 34:
                                        case 92: {
                                            break;
                                        }
                                        case 98: {
                                            c2 = 8;
                                            break;
                                        }
                                        case 116: {
                                            c2 = 9;
                                            break;
                                        }
                                        case 110: {
                                            c2 = 10;
                                            break;
                                        }
                                        case 102: {
                                            c2 = 12;
                                            break;
                                        }
                                        case 114: {
                                            c2 = 13;
                                            break;
                                        }
                                        default: {
                                            c2 = this.char1(c2);
                                            break;
                                        }
                                    }
                                    chars[j] = (char)c2;
                                    ++offset;
                                }
                                else {
                                    if (c2 == 34) {
                                        str = new String(chars);
                                        break;
                                    }
                                    if (c2 >= 0) {
                                        chars[j] = (char)c2;
                                        ++offset;
                                    }
                                    else {
                                        switch ((c2 & 0xFF) >> 4) {
                                            case 12:
                                            case 13: {
                                                ++offset;
                                                final int c7 = this.bytes[offset++];
                                                chars[j] = (char)((c2 & 0x1F) << 6 | (c7 & 0x3F));
                                                break;
                                            }
                                            case 14: {
                                                ++offset;
                                                final int c7 = this.bytes[offset++];
                                                final int c8 = this.bytes[offset++];
                                                chars[j] = (char)((c2 & 0xF) << 12 | (c7 & 0x3F) << 6 | (c8 & 0x3F));
                                                break;
                                            }
                                            default: {
                                                if (c2 >> 3 != -2) {
                                                    throw new JSONException("malformed input around byte " + offset);
                                                }
                                                ++offset;
                                                final int c7 = this.bytes[offset++];
                                                final int c8 = this.bytes[offset++];
                                                final int c9 = this.bytes[offset++];
                                                final int uc = c2 << 18 ^ c7 << 12 ^ c8 << 6 ^ (c9 ^ 0x381F80);
                                                if ((c7 & 0xC0) != 0x80 || (c8 & 0xC0) != 0x80 || (c9 & 0xC0) != 0x80 || uc < 65536 || uc >= 1114112) {
                                                    throw new JSONException("malformed input around byte " + offset);
                                                }
                                                chars[j++] = (char)((uc >>> 10) + 55232);
                                                chars[j] = (char)((uc & 0x3FF) + 56320);
                                                break;
                                            }
                                        }
                                    }
                                }
                                ++j;
                            }
                        }
                        else if (ascii) {
                            final int strlen = offset - this.offset;
                            if (strlen == 1) {
                                str = TypeUtils.toString((char)(this.bytes[this.offset] & 0xFF));
                            }
                            else if (strlen == 2) {
                                str = TypeUtils.toString((char)(this.bytes[this.offset] & 0xFF), (char)(this.bytes[this.offset + 1] & 0xFF));
                            }
                            else if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                                final char[] chars2 = new char[strlen];
                                for (int k = 0; k < strlen; ++k) {
                                    chars2[k] = (char)this.bytes[this.offset + k];
                                }
                                str = JDKUtils.STRING_CREATOR_JDK8.apply(chars2, Boolean.TRUE);
                            }
                            else if (JDKUtils.STRING_CREATOR_JDK11 != null) {
                                final byte[] bytes = Arrays.copyOfRange(this.bytes, this.offset, offset);
                                str = JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
                            }
                            else {
                                str = new String(this.bytes, this.offset, offset - this.offset, StandardCharsets.ISO_8859_1);
                            }
                        }
                        else {
                            str = new String(this.bytes, this.offset, offset - this.offset, StandardCharsets.UTF_8);
                        }
                        if ((this.context.features & Feature.TrimString.mask) != 0x0L) {
                            str = str.trim();
                        }
                        Label_1391: {
                            if (++offset != this.end) {
                                byte e;
                                for (e = this.bytes[offset++]; e <= 32 && (1L << e & 0x100003700L) != 0x0L; e = this.bytes[offset++]) {
                                    if (offset == this.end) {
                                        break Label_1391;
                                    }
                                }
                                final boolean comma = e == 44;
                                this.comma = comma;
                                if (comma) {
                                    if (offset == this.end) {
                                        e = 26;
                                    }
                                    else {
                                        for (e = this.bytes[offset++]; e <= 32 && (1L << e & 0x100003700L) != 0x0L; e = this.bytes[offset++]) {
                                            if (offset == this.end) {
                                                e = 26;
                                                break;
                                            }
                                        }
                                    }
                                }
                                this.ch = (char)e;
                                this.offset = offset;
                                return str;
                            }
                        }
                        this.ch = '\u001a';
                        this.comma = false;
                        this.offset = offset;
                        return str;
                    }
                    ++offset;
                }
                else {
                    switch ((c & 0xFF) >> 4) {
                        case 12:
                        case 13: {
                            offset += 2;
                            ascii = false;
                            break;
                        }
                        case 14: {
                            offset += 3;
                            ascii = false;
                            break;
                        }
                        default: {
                            if (c >> 3 == -2) {
                                offset += 4;
                                ++i;
                                ascii = false;
                                break;
                            }
                            throw new JSONException("malformed input around byte " + offset);
                        }
                    }
                }
                ++i;
            }
            throw new JSONException("invalid escape character EOI");
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
            this.ch = (char)this.bytes[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
                }
                this.nextIfComma();
                this.wasNull = true;
                return;
            }
        }
        else if (this.ch == ',' || this.ch == '\r' || this.ch == '\n') {
            this.wasNull = true;
            this.valueType = 5;
            return;
        }
        final int start = this.offset;
        int multmin;
        if (this.ch == '-') {
            final int limit = Integer.MIN_VALUE;
            multmin = -214748364;
            this.negative = true;
            this.ch = (char)this.bytes[this.offset++];
        }
        else {
            if (this.ch == '+') {
                this.ch = (char)this.bytes[this.offset++];
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
            this.ch = (char)this.bytes[this.offset++];
        }
        if (this.ch == '.') {
            this.valueType = 2;
            this.ch = (char)this.bytes[this.offset++];
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
                this.ch = (char)this.bytes[this.offset++];
            }
        }
        if (intOverflow) {
            final int numStart = this.negative ? start : (start - 1);
            final int numDigits = (this.scale > 0) ? (this.offset - 2 - numStart) : (this.offset - 1 - numStart);
            if (numDigits > 38) {
                this.valueType = 8;
                this.stringValue = new String(this.bytes, numStart, this.offset - 1 - numStart);
            }
            else {
                this.bigInt(this.bytes, numStart, this.offset - 1);
            }
        }
        else {
            this.mag3 = -this.mag3;
        }
        if (this.ch == 'e' || this.ch == 'E') {
            boolean negativeExp = false;
            int expValue = 0;
            this.ch = (char)this.bytes[this.offset++];
            if (this.ch == '-') {
                negativeExp = true;
                this.ch = (char)this.bytes[this.offset++];
            }
            else if (this.ch == '+') {
                this.ch = (char)this.bytes[this.offset++];
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
                this.ch = (char)this.bytes[this.offset++];
            }
            if (negativeExp) {
                expValue = -expValue;
            }
            this.exponent = (short)expValue;
            this.valueType = 2;
        }
        if (this.offset == start) {
            if (this.ch == 'n') {
                if (this.bytes[this.offset++] == 117 && this.bytes[this.offset++] == 108 && this.bytes[this.offset++] == 108) {
                    this.wasNull = true;
                    this.valueType = 5;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = (char)this.bytes[this.offset++];
                    }
                }
            }
            else if (this.ch == 't') {
                if (this.bytes[this.offset++] == 114 && this.bytes[this.offset++] == 117 && this.bytes[this.offset++] == 101) {
                    this.boolValue = true;
                    this.valueType = 4;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = (char)this.bytes[this.offset++];
                    }
                }
            }
            else if (this.ch == 'f') {
                if (this.offset + 4 <= this.end && JDKUtils.UNSAFE.getInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset) == IOUtils.ALSE) {
                    this.offset += 4;
                    this.boolValue = false;
                    this.valueType = 4;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = (char)this.bytes[this.offset++];
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
                this.ch = (char)this.bytes[this.offset++];
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
                this.ch = (char)this.bytes[this.offset++];
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)this.bytes[this.offset++];
            }
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)this.bytes[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = (char)this.bytes[this.offset++];
                    }
                }
            }
        }
    }
    
    @Override
    public final void readNumber(final ValueConsumer consumer, final boolean quoted) {
        this.wasNull = false;
        this.boolValue = false;
        this.mag0 = 0;
        this.mag1 = 0;
        this.mag2 = 0;
        this.mag3 = 0;
        this.negative = false;
        this.exponent = 0;
        this.scale = 0;
        char quote = '\0';
        if (this.ch == '\"' || this.ch == '\'') {
            quote = this.ch;
            this.ch = (char)this.bytes[this.offset++];
        }
        final int start = this.offset;
        if (this.ch == '-') {
            this.negative = true;
            this.ch = (char)this.bytes[this.offset++];
        }
        boolean intOverflow = false;
        this.valueType = 1;
        while (this.ch >= '0' && this.ch <= '9') {
            if (!intOverflow) {
                final int mag3_10 = this.mag3 * 10 + (this.ch - '0');
                if (mag3_10 < this.mag3) {
                    intOverflow = true;
                }
                else {
                    this.mag3 = mag3_10;
                }
            }
            this.ch = (char)this.bytes[this.offset++];
        }
        if (this.ch == '.') {
            this.valueType = 2;
            this.ch = (char)this.bytes[this.offset++];
            while (this.ch >= '0' && this.ch <= '9') {
                if (!intOverflow) {
                    final int mag3_10 = this.mag3 * 10 + (this.ch - '0');
                    if (mag3_10 < this.mag3) {
                        intOverflow = true;
                    }
                    else {
                        this.mag3 = mag3_10;
                    }
                }
                ++this.scale;
                this.ch = (char)this.bytes[this.offset++];
            }
        }
        if (intOverflow) {
            final int numStart = this.negative ? start : (start - 1);
            this.bigInt(this.bytes, numStart, this.offset - 1);
        }
        if (this.ch == 'e' || this.ch == 'E') {
            boolean negativeExp = false;
            int expValue = 0;
            this.ch = (char)this.bytes[this.offset++];
            if (this.ch == '-') {
                negativeExp = true;
                this.ch = (char)this.bytes[this.offset++];
            }
            else if (this.ch == '+') {
                this.ch = (char)this.bytes[this.offset++];
            }
            while (this.ch >= '0' && this.ch <= '9') {
                final int byteVal = this.ch - '0';
                expValue = expValue * 10 + byteVal;
                if (expValue > 1023) {
                    throw new JSONException("too large exp value : " + expValue);
                }
                this.ch = (char)this.bytes[this.offset++];
            }
            if (negativeExp) {
                expValue = -expValue;
            }
            this.exponent = (short)expValue;
            this.valueType = 2;
        }
        final int len = this.offset - start;
        if (this.offset == start) {
            if (this.ch == 'n') {
                if (this.bytes[this.offset++] == 117 && this.bytes[this.offset++] == 108 && this.bytes[this.offset++] == 108) {
                    this.wasNull = true;
                    this.valueType = 5;
                    this.ch = (char)this.bytes[this.offset++];
                }
            }
            else if (this.ch == 't') {
                if (this.bytes[this.offset++] == 114 && this.bytes[this.offset++] == 117 && this.bytes[this.offset++] == 101) {
                    this.boolValue = true;
                    this.valueType = 4;
                    this.ch = (char)this.bytes[this.offset++];
                }
            }
            else if (this.ch == 'f') {
                if (this.offset + 4 <= this.end && JDKUtils.UNSAFE.getInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset) == IOUtils.ALSE) {
                    this.offset += 4;
                    this.boolValue = false;
                    this.valueType = 4;
                    this.ch = (char)this.bytes[this.offset++];
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
                --this.offset;
                this.ch = quote;
                this.readString0();
                this.valueType = 3;
                return;
            }
            this.ch = (char)this.bytes[this.offset++];
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)this.bytes[this.offset++];
            }
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            this.ch = (char)this.bytes[this.offset++];
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = (char)this.bytes[this.offset++];
                    }
                }
            }
        }
        if (!quoted && (this.valueType == 1 || this.valueType == 2)) {
            consumer.accept(this.bytes, start - 1, len);
            return;
        }
        if (this.valueType == 1) {
            if (this.mag0 == 0 && this.mag1 == 0 && this.mag2 == 0 && this.mag3 != Integer.MIN_VALUE) {
                final int intValue = this.negative ? (-this.mag3) : this.mag3;
                consumer.accept(intValue);
                return;
            }
            if (this.mag0 == 0 && this.mag1 == 0) {
                final long v3 = (long)this.mag3 & 0xFFFFFFFFL;
                final long v4 = (long)this.mag2 & 0xFFFFFFFFL;
                if (v4 <= 2147483647L) {
                    final long v5 = (v4 << 32) + v3;
                    final long longValue = this.negative ? (-v5) : v5;
                    consumer.accept(longValue);
                    return;
                }
            }
        }
        final Number number = this.getNumber();
        consumer.accept(number);
    }
    
    @Override
    public final boolean readIfNull() {
        if (this.ch == 'n' && this.bytes[this.offset] == 117 && this.bytes[this.offset + 1] == 108 && this.bytes[this.offset + 2] == 108) {
            if (this.offset + 3 == this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)this.bytes[this.offset + 3];
            }
            this.offset += 4;
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
                }
            }
            if (this.comma = (this.ch == ',')) {
                this.ch = ((this.offset == this.end) ? '\u001a' : ((char)this.bytes[this.offset++]));
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = (char)this.bytes[this.offset++];
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public final boolean isNull() {
        return this.ch == 'n' && this.offset < this.end && this.bytes[this.offset] == 117;
    }
    
    @Override
    public final Date readNullOrNewDate() {
        Date date = null;
        if (this.offset + 2 < this.end && this.bytes[this.offset] == 117 && this.bytes[this.offset + 1] == 108 && this.bytes[this.offset + 2] == 108) {
            if (this.offset + 3 == this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)this.bytes[this.offset + 3];
            }
            this.offset += 4;
        }
        else {
            if (this.offset + 1 >= this.end || this.bytes[this.offset] != 101 || this.bytes[this.offset + 1] != 119) {
                throw new JSONException("json syntax error, not match null or new Date" + this.offset);
            }
            if (this.offset + 3 == this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)this.bytes[this.offset + 2];
            }
            this.offset += 3;
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
                }
            }
            if (this.offset + 4 >= this.end || this.ch != 'D' || this.bytes[this.offset] != 97 || this.bytes[this.offset + 1] != 116 || this.bytes[this.offset + 2] != 101) {
                throw new JSONException("json syntax error, not match new Date" + this.offset);
            }
            if (this.offset + 3 == this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)this.bytes[this.offset + 3];
            }
            this.offset += 4;
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
                }
            }
            if (this.ch != '(' || this.offset >= this.end) {
                throw new JSONException("json syntax error, not match new Date" + this.offset);
            }
            this.ch = (char)this.bytes[this.offset++];
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
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
                this.ch = (char)this.bytes[this.offset++];
            }
            date = new Date(millis);
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)this.bytes[this.offset++];
            }
        }
        if (this.comma = (this.ch == ',')) {
            this.ch = ((this.offset == this.end) ? '\u001a' : ((char)this.bytes[this.offset++]));
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
                }
            }
        }
        return date;
    }
    
    @Override
    public final boolean nextIfNull() {
        final int offset = this.offset;
        final byte[] bytes = this.bytes;
        if (this.ch == 'n' && offset + 2 < this.end && bytes[offset] == 117) {
            this.readNull();
            return true;
        }
        return false;
    }
    
    @Override
    public final void readNull() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        char ch = this.ch;
        if (bytes[offset] == 117 && bytes[offset + 1] == 108 && bytes[offset + 2] == 108) {
            if (offset + 3 == this.end) {
                ch = '\u001a';
            }
            else {
                ch = (char)bytes[offset + 3];
            }
            offset += 4;
            while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
                if (offset >= this.end) {
                    ch = '\u001a';
                }
                else {
                    ch = (char)bytes[offset++];
                }
            }
            if (this.comma = (ch == ',')) {
                if (offset >= this.end) {
                    ch = '\u001a';
                }
                else {
                    ch = (char)bytes[offset++];
                }
                while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
                    if (offset >= this.end) {
                        ch = '\u001a';
                    }
                    else {
                        ch = (char)bytes[offset++];
                    }
                }
            }
            this.ch = ch;
            this.offset = offset;
            return;
        }
        throw new JSONException("json syntax error, not match null" + offset);
    }
    
    public final int getStringLength() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("string length only support string input");
        }
        final char quote = this.ch;
        int len = 0;
        int i = this.offset;
        final byte[] bytes = this.bytes;
        final int i2 = i + 8;
        if (i2 < this.end && i2 < bytes.length && bytes[i] != quote && bytes[i + 1] != quote && bytes[i + 2] != quote && bytes[i + 3] != quote && bytes[i + 4] != quote && bytes[i + 5] != quote && bytes[i + 6] != quote && bytes[i + 7] != quote) {
            i += 8;
            len += 8;
        }
        while (i < this.end && bytes[i] != quote) {
            ++i;
            ++len;
        }
        return len;
    }
    
    @Override
    public final LocalDate readLocalDate() {
        final byte[] bytes = this.bytes;
        final int offset = this.offset;
        if (this.ch == '\"' || this.ch == '\'') {
            final Context context = this.context;
            if (context.dateFormat == null || context.formatyyyyMMddhhmmss19 || context.formatyyyyMMddhhmmssT19 || context.formatyyyyMMdd8 || context.formatISO8601) {
                final char quote = this.ch;
                final int c10 = offset + 10;
                if (c10 < bytes.length && c10 < this.end && bytes[offset + 4] == 45 && bytes[offset + 7] == 45 && bytes[offset + 10] == quote) {
                    final byte y0 = bytes[offset];
                    final byte y2 = bytes[offset + 1];
                    final byte y3 = bytes[offset + 2];
                    final byte y4 = bytes[offset + 3];
                    final byte m0 = bytes[offset + 5];
                    final byte m2 = bytes[offset + 6];
                    final byte d0 = bytes[offset + 8];
                    final byte d2 = bytes[offset + 9];
                    if (y0 >= 48 && y0 <= 57 && y2 >= 48 && y2 <= 57 && y3 >= 48 && y3 <= 57 && y4 >= 48 && y4 <= 57) {
                        final int year = (y0 - 48) * 1000 + (y2 - 48) * 100 + (y3 - 48) * 10 + (y4 - 48);
                        if (m0 >= 48 && m0 <= 57 && m2 >= 48 && m2 <= 57) {
                            final int month = (m0 - 48) * 10 + (m2 - 48);
                            if (d0 >= 48 && d0 <= 57 && d2 >= 48 && d2 <= 57) {
                                final int dom = (d0 - 48) * 10 + (d2 - 48);
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
                                this.offset = offset + 11;
                                this.next();
                                final boolean comma = this.ch == ',';
                                this.comma = comma;
                                if (comma) {
                                    this.next();
                                }
                                return ldt;
                            }
                        }
                    }
                }
            }
        }
        return super.readLocalDate();
    }
    
    @Override
    public final OffsetDateTime readOffsetDateTime() {
        final byte[] bytes = this.bytes;
        final int offset = this.offset;
        final Context context = this.context;
        if ((this.ch == '\"' || this.ch == '\'') && (context.dateFormat == null || context.formatyyyyMMddhhmmss19 || context.formatyyyyMMddhhmmssT19 || context.formatyyyyMMdd8 || context.formatISO8601)) {
            final char quote = this.ch;
            final int off21 = offset + 19;
            final byte c10;
            if (off21 < bytes.length && off21 < this.end && bytes[offset + 4] == 45 && bytes[offset + 7] == 45 && ((c10 = bytes[offset + 10]) == 32 || c10 == 84) && bytes[offset + 13] == 58 && bytes[offset + 16] == 58) {
                final byte y0 = bytes[offset];
                final byte y2 = bytes[offset + 1];
                final byte y3 = bytes[offset + 2];
                final byte y4 = bytes[offset + 3];
                final byte m0 = bytes[offset + 5];
                final byte m2 = bytes[offset + 6];
                final byte d0 = bytes[offset + 8];
                final byte d2 = bytes[offset + 9];
                final byte h0 = bytes[offset + 11];
                final byte h2 = bytes[offset + 12];
                final byte i0 = bytes[offset + 14];
                final byte i2 = bytes[offset + 15];
                final byte s0 = bytes[offset + 17];
                final byte s2 = bytes[offset + 18];
                if (y0 < 48 || y0 > 57 || y2 < 48 || y2 > 57 || y3 < 48 || y3 > 57 || y4 < 48 || y4 > 57) {
                    return this.readZonedDateTime().toOffsetDateTime();
                }
                final int year = (y0 - 48) * 1000 + (y2 - 48) * 100 + (y3 - 48) * 10 + (y4 - 48);
                if (m0 < 48 || m0 > 57 || m2 < 48 || m2 > 57) {
                    return this.readZonedDateTime().toOffsetDateTime();
                }
                final int month = (m0 - 48) * 10 + (m2 - 48);
                if (d0 < 48 || d0 > 57 || d2 < 48 || d2 > 57) {
                    return this.readZonedDateTime().toOffsetDateTime();
                }
                final int dom = (d0 - 48) * 10 + (d2 - 48);
                if (h0 < 48 || h0 > 57 || h2 < 48 || h2 > 57) {
                    return this.readZonedDateTime().toOffsetDateTime();
                }
                final int hour = (h0 - 48) * 10 + (h2 - 48);
                if (i0 < 48 || i0 > 57 || i2 < 48 || i2 > 57) {
                    return this.readZonedDateTime().toOffsetDateTime();
                }
                final int minute = (i0 - 48) * 10 + (i2 - 48);
                if (s0 < 48 || s0 > 57 || s2 < 48 || s2 > 57) {
                    return this.readZonedDateTime().toOffsetDateTime();
                }
                final int second = (s0 - 48) * 10 + (s2 - 48);
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
                for (int start = j = offset + 19, end = offset + 31; j < end && j < this.end && j < bytes.length; ++j) {
                    if (bytes[j] == quote && bytes[j - 1] == 90) {
                        nanoSize = j - start - 2;
                        len = j - offset + 1;
                        break;
                    }
                }
                if (nanoSize != -1 || len == 21) {
                    final int nano = (nanoSize <= 0) ? 0 : DateUtils.readNanos(bytes, nanoSize, offset + 20);
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
        if (!this.isString()) {
            throw new JSONException("date only support string input");
        }
        if (len < 19) {
            return null;
        }
        ZonedDateTime zdt;
        if (len == 30 && this.bytes[this.offset + 29] == 90) {
            final LocalDateTime ldt = DateUtils.parseLocalDateTime29(this.bytes, this.offset);
            zdt = ZonedDateTime.of(ldt, ZoneOffset.UTC);
        }
        else if (len == 29 && this.bytes[this.offset + 28] == 90) {
            final LocalDateTime ldt = DateUtils.parseLocalDateTime28(this.bytes, this.offset);
            zdt = ZonedDateTime.of(ldt, ZoneOffset.UTC);
        }
        else if (len == 28 && this.bytes[this.offset + 27] == 90) {
            final LocalDateTime ldt = DateUtils.parseLocalDateTime27(this.bytes, this.offset);
            zdt = ZonedDateTime.of(ldt, ZoneOffset.UTC);
        }
        else if (len == 27 && this.bytes[this.offset + 26] == 90) {
            final LocalDateTime ldt = DateUtils.parseLocalDateTime26(this.bytes, this.offset);
            zdt = ZonedDateTime.of(ldt, ZoneOffset.UTC);
        }
        else {
            zdt = DateUtils.parseZonedDateTime(this.bytes, this.offset, len, this.context.zoneId);
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
    
    public final LocalDate readLocalDate8() {
        if (!this.isString()) {
            throw new JSONException("localDate only support string input");
        }
        LocalDate ldt;
        try {
            ldt = DateUtils.parseLocalDate8(this.bytes, this.offset);
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
        if (!this.isString()) {
            throw new JSONException("localDate only support string input");
        }
        LocalDate ldt;
        try {
            ldt = DateUtils.parseLocalDate9(this.bytes, this.offset);
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
        if (!this.isString()) {
            throw new JSONException("localDate only support string input");
        }
        LocalDate ldt;
        try {
            ldt = DateUtils.parseLocalDate10(this.bytes, this.offset);
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
    
    @Override
    protected final LocalDate readLocalDate11() {
        if (!this.isString()) {
            throw new JSONException("localDate only support string input");
        }
        final LocalDate ldt = DateUtils.parseLocalDate11(this.bytes, this.offset);
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
    
    @Override
    protected final LocalDateTime readLocalDateTime17() {
        if (!this.isString()) {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime17(this.bytes, this.offset);
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
    protected final LocalTime readLocalTime5() {
        if (this.ch != '\"' && this.ch != '\'') {
            throw new JSONException("localTime only support string input");
        }
        final LocalTime time = DateUtils.parseLocalTime5(this.bytes, this.offset);
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
        final LocalTime time = DateUtils.parseLocalTime8(this.bytes, this.offset);
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
        final LocalTime time = DateUtils.parseLocalTime8(this.bytes, this.offset);
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
    
    @Override
    protected final LocalTime readLocalTime10() {
        if (!this.isString()) {
            throw new JSONException("localTime only support string input");
        }
        final LocalTime time = DateUtils.parseLocalTime10(this.bytes, this.offset);
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
        if (!this.isString()) {
            throw new JSONException("localTime only support string input");
        }
        final LocalTime time = DateUtils.parseLocalTime11(this.bytes, this.offset);
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
        if (!this.isString()) {
            throw new JSONException("localTime only support string input");
        }
        final LocalTime time = DateUtils.parseLocalTime12(this.bytes, this.offset);
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
        if (!this.isString()) {
            throw new JSONException("localTime only support string input");
        }
        final LocalTime time = DateUtils.parseLocalTime18(this.bytes, this.offset);
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
    protected final LocalDateTime readLocalDateTime12() {
        if (!this.isString()) {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime12(this.bytes, this.offset);
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
    protected final LocalDateTime readLocalDateTime14() {
        if (!this.isString()) {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime14(this.bytes, this.offset);
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
    protected final LocalDateTime readLocalDateTime16() {
        if (!this.isString()) {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime16(this.bytes, this.offset);
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
    protected final LocalDateTime readLocalDateTime18() {
        if (!this.isString()) {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime18(this.bytes, this.offset);
        this.offset += 19;
        this.next();
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return ldt;
    }
    
    @Override
    protected final LocalDateTime readLocalDateTime19() {
        if (!this.isString()) {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime19(this.bytes, this.offset);
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
        if (!this.isString()) {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime20(this.bytes, this.offset);
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
        final long millis = DateUtils.parseMillis19(this.bytes, this.offset, this.context.zoneId);
        if (this.bytes[this.offset + 19] != quote) {
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
        if (!this.isString()) {
            throw new JSONException("date only support string input");
        }
        LocalDateTime ldt;
        if (this.bytes[this.offset + len - 1] == 90) {
            final ZonedDateTime zdt = DateUtils.parseZonedDateTime(this.bytes, this.offset, len);
            ldt = zdt.toLocalDateTime();
        }
        else {
            ldt = DateUtils.parseLocalDateTimeX(this.bytes, this.offset, len);
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
    public final BigDecimal readBigDecimal() {
        final byte[] bytes = this.bytes;
        boolean value = false;
        BigDecimal decimal = null;
        char quote = '\0';
        if (this.ch == '\"' || this.ch == '\'') {
            quote = this.ch;
            this.ch = (char)bytes[this.offset++];
            if (this.ch == quote) {
                if (this.offset == this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)bytes[this.offset++];
                }
                this.nextIfComma();
                return null;
            }
        }
        final int start = this.offset;
        if (this.ch == '-') {
            this.negative = true;
            this.ch = (char)bytes[this.offset++];
        }
        else {
            this.negative = false;
            if (this.ch == '+') {
                this.ch = (char)bytes[this.offset++];
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
            this.ch = (char)bytes[this.offset++];
        }
        this.scale = 0;
        if (this.ch == '.') {
            this.valueType = 2;
            this.ch = (char)bytes[this.offset++];
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
                this.ch = (char)bytes[this.offset++];
            }
        }
        int expValue = 0;
        if (this.ch == 'e' || this.ch == 'E') {
            boolean negativeExp = false;
            this.ch = (char)bytes[this.offset++];
            if (this.ch == '-') {
                negativeExp = true;
                this.ch = (char)bytes[this.offset++];
            }
            else if (this.ch == '+') {
                this.ch = (char)bytes[this.offset++];
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
                this.ch = (char)bytes[this.offset++];
            }
            if (negativeExp) {
                expValue = -expValue;
            }
            this.exponent = (short)expValue;
            this.valueType = 2;
        }
        if (this.offset == start) {
            if (this.ch == 'n') {
                if (bytes[this.offset++] == 117 && bytes[this.offset++] == 108 && bytes[this.offset++] == 108) {
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
                        this.ch = (char)bytes[this.offset++];
                    }
                }
            }
            else if (this.ch == 't') {
                if (bytes[this.offset++] == 114 && bytes[this.offset++] == 117 && bytes[this.offset++] == 101) {
                    value = true;
                    decimal = BigDecimal.ONE;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = (char)bytes[this.offset++];
                    }
                }
            }
            else if (this.ch == 'f') {
                if (this.offset + 4 <= this.end && JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset) == IOUtils.ALSE) {
                    this.offset += 4;
                    decimal = BigDecimal.ZERO;
                    value = true;
                    if (this.offset == this.end) {
                        this.ch = '\u001a';
                        ++this.offset;
                    }
                    else {
                        this.ch = (char)bytes[this.offset++];
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
                this.ch = (char)bytes[this.offset++];
            }
        }
        if (!value) {
            if (expValue == 0 && !overflow && longValue != 0L) {
                decimal = BigDecimal.valueOf(this.negative ? (-longValue) : longValue, this.scale);
                value = true;
            }
            if (!value) {
                decimal = TypeUtils.parseBigDecimal(bytes, start - 1, len);
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
                    this.ch = (char)bytes[this.offset++];
                }
            }
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)bytes[this.offset++];
            }
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)bytes[this.offset++];
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = (char)bytes[this.offset++];
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
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        if (offset + 36 < bytes.length && bytes[offset + 36] == quote) {
            final char ch2 = (char)bytes[offset + 8];
            final char ch3 = (char)bytes[offset + 13];
            final char ch4 = (char)bytes[offset + 18];
            final char ch5 = (char)bytes[offset + 23];
            if (ch2 == '-' && ch3 == '-' && ch4 == '-' && ch5 == '-') {
                long hi = 0L;
                for (int i = 0; i < 8; ++i) {
                    hi = (hi << 4) + JSONFactory.UUID_VALUES[bytes[offset + i] - 48];
                }
                for (int i = 9; i < 13; ++i) {
                    hi = (hi << 4) + JSONFactory.UUID_VALUES[bytes[offset + i] - 48];
                }
                for (int i = 14; i < 18; ++i) {
                    hi = (hi << 4) + JSONFactory.UUID_VALUES[bytes[offset + i] - 48];
                }
                long lo = 0L;
                for (int j = 19; j < 23; ++j) {
                    lo = (lo << 4) + JSONFactory.UUID_VALUES[bytes[offset + j] - 48];
                }
                for (int j = 24; j < 36; ++j) {
                    lo = (lo << 4) + JSONFactory.UUID_VALUES[bytes[offset + j] - 48];
                }
                final UUID uuid = new UUID(hi, lo);
                offset += 37;
                if (offset == this.end) {
                    ch = '\u001a';
                }
                else {
                    ch = (char)bytes[offset++];
                }
                while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
                    if (offset >= this.end) {
                        ch = '\u001a';
                    }
                    else {
                        ch = (char)bytes[offset++];
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
        else if (offset + 32 < bytes.length && bytes[offset + 32] == quote) {
            long hi2 = 0L;
            for (int k = 0; k < 16; ++k) {
                hi2 = (hi2 << 4) + JSONFactory.UUID_VALUES[bytes[offset + k] - 48];
            }
            long lo2 = 0L;
            for (int l = 16; l < 32; ++l) {
                lo2 = (lo2 << 4) + JSONFactory.UUID_VALUES[bytes[offset + l] - 48];
            }
            final UUID uuid2 = new UUID(hi2, lo2);
            offset += 33;
            if (offset == this.end) {
                ch = '\u001a';
            }
            else {
                ch = (char)bytes[offset++];
            }
            while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
                if (offset >= this.end) {
                    ch = '\u001a';
                }
                else {
                    ch = (char)bytes[offset++];
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
    
    @Override
    public final String readPattern() {
        if (this.ch != '/') {
            throw new JSONException("illegal pattern");
        }
        int offset = this.offset;
        int i = 0;
        while (true) {
            final char c = (char)this.bytes[offset];
            if (c == '/') {
                break;
            }
            if (++offset >= this.end) {
                break;
            }
            ++i;
        }
        final String str = new String(this.bytes, this.offset, offset - this.offset, StandardCharsets.UTF_8);
        if (offset + 1 == this.end) {
            this.offset = this.end;
            this.ch = '\u001a';
            return str;
        }
        int b;
        for (b = (char)this.bytes[++offset]; b <= 32 && (1L << b & 0x100003700L) != 0x0L; b = (char)this.bytes[++offset]) {}
        if (this.comma = (b == 44)) {
            this.offset = offset + 1;
            this.ch = (char)this.bytes[this.offset++];
            while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                if (this.offset >= this.end) {
                    this.ch = '\u001a';
                }
                else {
                    this.ch = (char)this.bytes[this.offset++];
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
    public boolean nextIfNullOrEmptyString() {
        final char first = this.ch;
        final int end = this.end;
        int offset = this.offset;
        final byte[] bytes = this.bytes;
        if (first == 'n' && offset + 2 < end && bytes[offset] == 117 && bytes[offset + 1] == 108 && bytes[offset + 2] == 108) {
            offset += 3;
        }
        else {
            if ((first != '\"' && first != '\'') || offset >= end || bytes[offset] != first) {
                return false;
            }
            ++offset;
        }
        int ch;
        for (ch = ((offset == end) ? '\u001a' : ((char)bytes[offset])); ch >= 0 && ch <= 32 && (1L << ch & 0x100003700L) != 0x0L; ch = bytes[offset]) {
            if (++offset >= end) {
                this.ch = '\u001a';
                this.offset = offset;
                return true;
            }
        }
        if (this.comma = (ch == 44)) {
            if (++offset >= end) {
                ch = 26;
            }
            else {
                ch = bytes[offset];
            }
        }
        if (offset >= end) {
            this.ch = '\u001a';
            this.offset = offset;
            return true;
        }
        while (ch >= 0 && ch <= 32 && (1L << ch & 0x100003700L) != 0x0L) {
            if (++offset >= end) {
                this.ch = '\u001a';
                return true;
            }
            ch = bytes[offset];
        }
        if (ch >= 0) {
            this.offset = offset + 1;
            this.ch = (char)ch;
            return true;
        }
        ch &= 0xFF;
        switch (ch >> 4) {
            case 12:
            case 13: {
                offset += 2;
                final int char2 = bytes[offset - 1];
                if ((char2 & 0xC0) != 0x80) {
                    throw new JSONException("malformed input around byte " + offset);
                }
                ch = (char)((ch & 0x1F) << 6 | (char2 & 0x3F));
                break;
            }
            case 14: {
                offset += 3;
                final int char2 = bytes[offset - 2];
                final int char3 = bytes[offset - 1];
                if ((char2 & 0xC0) != 0x80 || (char3 & 0xC0) != 0x80) {
                    throw new JSONException("malformed input around byte " + (offset - 1));
                }
                ch = (char)((ch & 0xF) << 12 | (char2 & 0x3F) << 6 | (char3 & 0x3F));
                break;
            }
            default: {
                throw new JSONException("malformed input around byte " + offset);
            }
        }
        this.offset = offset;
        this.ch = (char)ch;
        return true;
    }
    
    @Override
    public final boolean nextIfMatchIdent(final char c0, final char c1, final char c2) {
        if (this.ch != c0) {
            return false;
        }
        final int offset2 = this.offset + 2;
        if (offset2 > this.end || this.bytes[this.offset] != c1 || this.bytes[this.offset + 1] != c2) {
            return false;
        }
        if (offset2 == this.end) {
            this.offset = offset2;
            this.ch = '\u001a';
            return true;
        }
        int offset3;
        char ch;
        for (offset3 = offset2, ch = (char)this.bytes[offset3]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = (char)this.bytes[offset3]) {
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
        if (offset3 > this.end || this.bytes[this.offset] != c1 || this.bytes[this.offset + 1] != c2 || this.bytes[this.offset + 2] != c3) {
            return false;
        }
        if (offset3 == this.end) {
            this.offset = offset3;
            this.ch = '\u001a';
            return true;
        }
        int offset4;
        char ch;
        for (offset4 = offset3, ch = (char)this.bytes[offset4]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = (char)this.bytes[offset4]) {
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
        if (offset4 > this.end || this.bytes[this.offset] != c1 || this.bytes[this.offset + 1] != c2 || this.bytes[this.offset + 2] != c3 || this.bytes[this.offset + 3] != c4) {
            return false;
        }
        if (offset4 == this.end) {
            this.offset = offset4;
            this.ch = '\u001a';
            return true;
        }
        int offset5;
        char ch;
        for (offset5 = offset4, ch = (char)this.bytes[offset5]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = (char)this.bytes[offset5]) {
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
        if (offset5 > this.end || this.bytes[this.offset] != c1 || this.bytes[this.offset + 1] != c2 || this.bytes[this.offset + 2] != c3 || this.bytes[this.offset + 3] != c4 || this.bytes[this.offset + 4] != c5) {
            return false;
        }
        if (offset5 == this.end) {
            this.offset = offset5;
            this.ch = '\u001a';
            return true;
        }
        int offset6;
        char ch;
        for (offset6 = offset5, ch = (char)this.bytes[offset6]; ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L; ch = (char)this.bytes[offset6]) {
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
    public final byte[] readHex() {
        if (this.ch == 'x') {
            this.next();
        }
        int ch = this.ch;
        int offset = this.offset;
        final byte[] bytes = this.bytes;
        final int quote = ch;
        if (quote != 39 && quote != 34) {
            throw new JSONException("illegal state. " + ch);
        }
        final int start = offset;
        ++offset;
        while (true) {
            ch = (char)bytes[offset++];
            if (ch < 48 || ch > 57) {
                if (ch >= 65 && ch <= 70) {
                    continue;
                }
                break;
            }
        }
        if (ch != quote) {
            throw new JSONException("illegal state. " + ch);
        }
        ch = bytes[offset++];
        final int len = offset - start - 2;
        if (len == 0) {
            return new byte[0];
        }
        if (len % 2 != 0) {
            throw new JSONException("illegal state. " + len);
        }
        final byte[] hex = new byte[len / 2];
        for (int i = 0; i < hex.length; ++i) {
            final byte c0 = this.bytes[start + i * 2];
            final byte c2 = this.bytes[start + i * 2 + 1];
            final int b0 = c0 - ((c0 <= 57) ? 48 : 55);
            final int b2 = c2 - ((c2 <= 57) ? 48 : 55);
            hex[i] = (byte)(b0 << 4 | b2);
        }
        while (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L) {
            if (offset >= this.end) {
                ch = 26;
            }
            else {
                ch = (char)(bytes[offset++] & 0xFF);
            }
        }
        if (ch != 44 || offset >= this.end) {
            this.offset = offset;
            this.ch = (char)ch;
            return hex;
        }
        this.comma = true;
        while (ch == 0 || (ch <= 32 && (1L << ch & 0x100003700L) != 0x0L)) {
            if (++offset >= this.end) {
                this.offset = offset;
                this.ch = '\u001a';
                return hex;
            }
            ch = bytes[offset];
        }
        if (ch >= 0) {
            ++offset;
        }
        else {
            ch &= 0xFF;
            switch (ch >> 4) {
                case 12:
                case 13: {
                    offset += 2;
                    final int char2 = bytes[offset - 1];
                    if ((char2 & 0xC0) != 0x80) {
                        throw new JSONException("malformed input around byte " + offset);
                    }
                    ch = ((ch & 0x1F) << 6 | (char2 & 0x3F));
                    break;
                }
                case 14: {
                    offset += 3;
                    final int char2 = bytes[offset - 2];
                    final int char3 = bytes[offset - 1];
                    if ((char2 & 0xC0) != 0x80 || (char3 & 0xC0) != 0x80) {
                        throw new JSONException("malformed input around byte " + (offset - 1));
                    }
                    ch = ((ch & 0xF) << 12 | (char2 & 0x3F) << 6 | (char3 & 0x3F));
                    break;
                }
                default: {
                    throw new JSONException("malformed input around byte " + offset);
                }
            }
        }
        this.offset = offset;
        this.ch = (char)ch;
        while (this.ch == '/' && offset < bytes.length && bytes[offset] == 47) {
            this.skipLineComment();
        }
        return hex;
    }
    
    @Override
    public boolean isReference() {
        final byte[] bytes = this.bytes;
        int ch = this.ch;
        int offset = this.offset;
        if (ch != 123) {
            return false;
        }
        if (offset == this.end) {
            return false;
        }
        for (ch = bytes[offset]; ch >= 0 && ch <= 32 && (1L << ch & 0x100003700L) != 0x0L; ch = bytes[offset]) {
            if (++offset >= this.end) {
                return false;
            }
        }
        final int quote = ch;
        if ((quote != 34 && quote != 39) || offset + 6 >= this.end || bytes[offset + 1] != 36 || bytes[offset + 2] != 114 || bytes[offset + 3] != 101 || bytes[offset + 4] != 102 || bytes[offset + 5] != quote) {
            return false;
        }
        for (offset += 6, ch = bytes[offset]; ch >= 0 && ch <= 32 && (1L << ch & 0x100003700L) != 0x0L; ch = bytes[offset]) {
            if (++offset >= this.end) {
                return false;
            }
        }
        if (ch != 58 || offset + 1 >= this.end) {
            return false;
        }
        for (ch = bytes[++offset]; ch >= 0 && ch <= 32 && (1L << ch & 0x100003700L) != 0x0L; ch = bytes[offset]) {
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
        this.ch = (char)this.bytes[this.offset++];
        final String reference = this.readString();
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            ++this.offset;
            if (this.offset >= this.length) {
                this.ch = '\u001a';
                return reference;
            }
            this.ch = (char)this.bytes[this.offset];
        }
        if (this.ch != '}') {
            throw new JSONException("illegal reference : " + reference);
        }
        if (this.offset == this.end) {
            this.ch = '\u001a';
        }
        else {
            this.ch = (char)this.bytes[this.offset++];
        }
        while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                this.ch = (char)this.bytes[this.offset++];
            }
        }
        if (this.comma = (this.ch == ',')) {
            this.ch = (char)this.bytes[this.offset++];
            if (this.offset >= this.end) {
                this.ch = '\u001a';
            }
            else {
                while (this.ch <= ' ' && (1L << this.ch & 0x100003700L) != 0x0L) {
                    if (this.offset >= this.end) {
                        this.ch = '\u001a';
                    }
                    else {
                        this.ch = (char)this.bytes[this.offset++];
                    }
                }
            }
        }
        return reference;
    }
    
    @Override
    public final boolean readBoolValue() {
        this.wasNull = false;
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        char ch = this.ch;
        boolean val;
        if (ch == 't' && offset + 2 < bytes.length && bytes[offset] == 114 && bytes[offset + 1] == 117 && bytes[offset + 2] == 101) {
            offset += 3;
            val = true;
        }
        else if (ch == 'f' && offset + 4 <= bytes.length && JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset) == IOUtils.ALSE) {
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
        else if (ch == 'n' && offset + 2 < bytes.length && bytes[offset] == 117 && bytes[offset + 1] == 108 && bytes[offset + 2] == 108) {
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
            if (offset + 1 < bytes.length && bytes[offset + 1] == 34) {
                final byte c0 = bytes[offset];
                offset += 2;
                if (c0 == 48 || c0 == 78) {
                    val = false;
                }
                else {
                    if (c0 != 49 && c0 != 89) {
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
        ch = ((offset == this.end) ? '\u001a' : ((char)bytes[offset++]));
        while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
            if (offset >= this.end) {
                ch = '\u001a';
            }
            else {
                ch = (char)bytes[offset++];
            }
        }
        if (this.comma = (ch == ',')) {
            ch = ((offset == this.end) ? '\u001a' : ((char)bytes[offset++]));
            while (ch <= ' ' && (1L << ch & 0x100003700L) != 0x0L) {
                if (offset >= this.end) {
                    ch = '\u001a';
                }
                else {
                    ch = (char)bytes[offset++];
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
        for (int i = 0; i < this.offset && i < this.end; ++i, ++column) {
            if (this.bytes[i] == 10) {
                column = 1;
                ++line;
            }
        }
        final StringBuilder buf = new StringBuilder();
        if (message != null && !message.isEmpty()) {
            buf.append(message).append(", ");
        }
        buf.append("offset ").append(this.offset).append(", character ").append(this.ch).append(", line ").append(line).append(", column ").append(column).append(", fastjson-version ").append("2.0.39").append((line > 1) ? '\n' : ' ');
        final String str = new String(this.bytes, this.start, Math.min(this.length, 65535));
        buf.append(str);
        return buf.toString();
    }
    
    @Override
    public final void close() {
        if (this.cacheItem != null) {
            JSONFactory.BYTES_UPDATER.lazySet(this.cacheItem, this.bytes);
        }
        if (this.in != null) {
            try {
                this.in.close();
            }
            catch (IOException ex) {}
        }
    }
}
