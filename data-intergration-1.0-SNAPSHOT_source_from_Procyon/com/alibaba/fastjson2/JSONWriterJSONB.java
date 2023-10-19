// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import com.alibaba.fastjson2.util.TypeUtils;
import java.math.BigInteger;
import java.util.UUID;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.internal.trove.map.hash.TLongIntHashMap;

final class JSONWriterJSONB extends JSONWriter
{
    static final byte[] SHANGHAI_ZONE_ID_NAME_BYTES;
    static final byte[] OFFSET_8_ZONE_ID_NAME_BYTES;
    static final long WRITE_ENUM_USING_STRING_MASK;
    private final JSONFactory.CacheItem cacheItem;
    private byte[] bytes;
    private TLongIntHashMap symbols;
    private int symbolIndex;
    private long rootTypeNameHash;
    static final long WRITE_NUM_NULL_MASK;
    
    JSONWriterJSONB(final Context ctx, final SymbolTable symbolTable) {
        super(ctx, symbolTable, true, StandardCharsets.UTF_8);
        this.cacheItem = JSONFactory.CACHE_ITEMS[System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1];
        byte[] bytes = JSONFactory.BYTES_UPDATER.getAndSet(this.cacheItem, null);
        if (bytes == null) {
            bytes = new byte[8192];
        }
        this.bytes = bytes;
    }
    
    @Override
    public void close() {
        final byte[] bytes = this.bytes;
        if (bytes.length < 1048576) {
            JSONFactory.BYTES_UPDATER.lazySet(this.cacheItem, bytes);
        }
    }
    
    @Override
    public void writeAny(final Object value) {
        if (value == null) {
            this.writeNull();
            return;
        }
        final boolean fieldBased = (this.context.features & Feature.FieldBased.mask) != 0x0L;
        final Class<?> valueClass = value.getClass();
        final ObjectWriter objectWriter = this.context.provider.getObjectWriter(valueClass, valueClass, fieldBased);
        if (this.isBeanToArray()) {
            objectWriter.writeArrayMappingJSONB(this, value, null, null, 0L);
        }
        else {
            objectWriter.writeJSONB(this, value, null, null, 0L);
        }
    }
    
    @Override
    public void startObject() {
        if (this.level >= this.context.maxLevel) {
            throw new JSONException("level too large : " + this.level);
        }
        ++this.level;
        final int off = this.off;
        if (off == this.bytes.length) {
            this.ensureCapacity(off + 1);
        }
        this.bytes[off] = -90;
        this.off = off + 1;
    }
    
    @Override
    public void endObject() {
        --this.level;
        final int off = this.off;
        if (off == this.bytes.length) {
            this.ensureCapacity(off + 1);
        }
        this.bytes[off] = -91;
        this.off = off + 1;
    }
    
    @Override
    public void startArray() {
        throw new JSONException("unsupported operation");
    }
    
    @Override
    public void startArray(final Object array, final int size) {
        if (this.isWriteTypeInfo(array)) {
            this.writeTypeName(array.getClass().getName());
        }
        final int off = this.off;
        if (off == this.bytes.length) {
            this.ensureCapacity(off + 1);
        }
        final byte[] bytes = this.bytes;
        final boolean tinyInt = size <= 15;
        bytes[off] = (byte)(tinyInt ? ((byte)(-108 + size)) : -92);
        this.off = off + 1;
        if (!tinyInt) {
            this.writeInt32(size);
        }
    }
    
    @Override
    public void startArray(final int size) {
        final int off = this.off;
        if (off == this.bytes.length) {
            this.ensureCapacity(off + 1);
        }
        final byte[] bytes = this.bytes;
        final boolean tinyInt = size <= 15;
        bytes[off] = (byte)(tinyInt ? ((byte)(-108 + size)) : -92);
        this.off = off + 1;
        if (!tinyInt) {
            this.writeInt32(size);
        }
    }
    
    @Override
    public void writeRaw(final byte b) {
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = b;
    }
    
    @Override
    public void writeChar(final char ch) {
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = -112;
        this.writeInt32(ch);
    }
    
    @Override
    public void writeName(final String name) {
        this.writeString(name);
    }
    
    @Override
    public void writeNull() {
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = -81;
    }
    
    @Override
    public void writeStringNull() {
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = -81;
    }
    
    @Override
    public void endArray() {
    }
    
    @Override
    public void writeComma() {
        throw new JSONException("unsupported operation");
    }
    
    @Override
    protected void write0(final char ch) {
        throw new JSONException("unsupported operation");
    }
    
    @Override
    public void writeString(final char[] chars, final int off, final int len, final boolean quote) {
        if (chars == null) {
            this.writeNull();
            return;
        }
        boolean ascii = true;
        for (int i = 0; i < len; ++i) {
            if (chars[i + off] > '\u00ff') {
                ascii = false;
                break;
            }
        }
        if (ascii) {
            if (len <= 47) {
                this.bytes[this.off++] = (byte)(len + 73);
            }
            else {
                this.bytes[this.off++] = 121;
                this.writeInt32(len);
            }
            for (int i = 0; i < len; ++i) {
                this.bytes[this.off++] = (byte)chars[off + i];
            }
            return;
        }
        this.writeString(new String(chars, off, len));
    }
    
    @Override
    public void writeStringLatin1(final byte[] value) {
        if (value == null) {
            this.writeStringNull();
            return;
        }
        int off = this.off;
        final int strlen = value.length;
        final int minCapacity = value.length + off + 5 + 1;
        if (minCapacity - this.bytes.length > 0) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (strlen <= 47) {
            bytes[off++] = (byte)(strlen + 73);
        }
        else if (strlen <= 2047) {
            putStringSizeSmall(bytes, off, strlen);
            off += 3;
        }
        else {
            off += putStringSizeLarge(bytes, off, strlen);
        }
        System.arraycopy(value, 0, bytes, off, value.length);
        this.off = off + strlen;
    }
    
    private static void putStringSizeSmall(final byte[] bytes, final int off, final int val) {
        bytes[off] = 121;
        bytes[off + 1] = (byte)(56 + (val >> 8));
        bytes[off + 2] = (byte)val;
    }
    
    private static int putStringSizeLarge(final byte[] bytes, final int off, final int strlen) {
        if (strlen <= 262143) {
            bytes[off] = 121;
            bytes[off + 1] = (byte)(68 + (strlen >> 16));
            bytes[off + 2] = (byte)(strlen >> 8);
            bytes[off + 3] = (byte)strlen;
            return 4;
        }
        bytes[off] = 121;
        bytes[off + 1] = 72;
        JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 2L, JDKUtils.BIG_ENDIAN ? strlen : Integer.reverseBytes(strlen));
        return 6;
    }
    
    @Override
    public void writeString(final char[] chars) {
        if (chars == null) {
            this.writeNull();
            return;
        }
        int off = this.off;
        boolean ascii = true;
        final int strlen = chars.length;
        if (chars.length < 47) {
            final int minCapacity = off + 1 + strlen;
            if (minCapacity - this.bytes.length > 0) {
                this.ensureCapacity(minCapacity);
            }
            this.bytes[off++] = (byte)(strlen + 73);
            for (int i = 0; i < chars.length; ++i) {
                final char ch = chars[i];
                if (ch > '\u00ff') {
                    ascii = false;
                    break;
                }
                this.bytes[off++] = (byte)ch;
            }
            if (ascii) {
                this.off = off;
                return;
            }
            off = this.off;
        }
        int j = 0;
        for (int upperBound = chars.length & 0xFFFFFFFC; j < upperBound; j += 4) {
            final char c0 = chars[j];
            final char c2 = chars[j + 1];
            final char c3 = chars[j + 2];
            final char c4 = chars[j + 3];
            if (c0 > '\u00ff' || c2 > '\u00ff' || c3 > '\u00ff' || c4 > '\u00ff') {
                ascii = false;
                break;
            }
        }
        if (ascii) {
            while (j < chars.length) {
                if (chars[j] > '\u00ff') {
                    ascii = false;
                    break;
                }
                ++j;
            }
        }
        final int minCapacity = (ascii ? strlen : (strlen * 3)) + off + 5 + 1;
        if (minCapacity - this.bytes.length > 0) {
            this.ensureCapacity(minCapacity);
        }
        if (ascii) {
            if (strlen <= 47) {
                this.bytes[off++] = (byte)(strlen + 73);
            }
            else if (strlen <= 2047) {
                putStringSizeSmall(this.bytes, off, strlen);
                off += 3;
            }
            else {
                off += putStringSizeLarge(this.bytes, off, strlen);
            }
            for (int i = 0; i < chars.length; ++i) {
                this.bytes[off++] = (byte)chars[i];
            }
        }
        else {
            final int maxSize = chars.length * 3;
            final int lenByteCnt = sizeOfInt(maxSize);
            this.ensureCapacity(off + maxSize + lenByteCnt + 1);
            final int result = IOUtils.encodeUTF8(chars, 0, chars.length, this.bytes, off + lenByteCnt + 1);
            final int utf8len = result - off - lenByteCnt - 1;
            final int utf8lenByteCnt = sizeOfInt(utf8len);
            if (lenByteCnt != utf8lenByteCnt) {
                System.arraycopy(this.bytes, off + lenByteCnt + 1, this.bytes, off + utf8lenByteCnt + 1, utf8len);
            }
            final byte[] bytes = this.bytes;
            bytes[off++] = 122;
            if (utf8len >= -16 && utf8len <= 47) {
                bytes[off++] = (byte)utf8len;
            }
            else if (utf8len >= -2048 && utf8len <= 2047) {
                bytes[off] = (byte)(56 + (utf8len >> 8));
                bytes[off + 1] = (byte)utf8len;
                off += 2;
            }
            else {
                off += writeInt32(bytes, off, utf8len);
            }
            off += utf8len;
        }
        this.off = off;
    }
    
    @Override
    public void writeString(final char[] chars, final int charsOff, final int len) {
        if (chars == null) {
            this.writeNull();
            return;
        }
        boolean ascii = true;
        if (len < 47) {
            final int mark = this.off;
            final int minCapacity = this.off + 1 + len;
            if (minCapacity - this.bytes.length > 0) {
                this.ensureCapacity(minCapacity);
            }
            this.bytes[this.off++] = (byte)(len + 73);
            for (int i = charsOff; i < len; ++i) {
                final char ch = chars[i];
                if (ch > '\u00ff') {
                    ascii = false;
                    break;
                }
                this.bytes[this.off++] = (byte)ch;
            }
            if (ascii) {
                return;
            }
            this.off = mark;
        }
        int j = charsOff;
        for (int upperBound = chars.length & 0xFFFFFFFC; j < upperBound; j += 4) {
            final char c0 = chars[j];
            final char c2 = chars[j + 1];
            final char c3 = chars[j + 2];
            final char c4 = chars[j + 3];
            if (c0 > '\u00ff' || c2 > '\u00ff' || c3 > '\u00ff' || c4 > '\u00ff') {
                ascii = false;
                break;
            }
        }
        if (ascii) {
            while (j < chars.length) {
                if (chars[j] > '\u00ff') {
                    ascii = false;
                    break;
                }
                ++j;
            }
        }
        final int minCapacity2 = (ascii ? len : (len * 3)) + this.off + 5 + 1;
        if (minCapacity2 - this.bytes.length > 0) {
            this.ensureCapacity(minCapacity2);
        }
        if (ascii) {
            final byte[] bytes = this.bytes;
            if (len <= 47) {
                bytes[this.off++] = (byte)(len + 73);
            }
            else if (len <= 2047) {
                final int off = this.off;
                bytes[off] = 121;
                bytes[off + 1] = (byte)(56 + (len >> 8));
                bytes[off + 2] = (byte)len;
                this.off += 3;
            }
            else {
                bytes[this.off++] = 121;
                this.writeInt32(len);
            }
            for (int i = 0; i < chars.length; ++i) {
                bytes[this.off++] = (byte)chars[i];
            }
        }
        else {
            final int maxSize = chars.length * 3;
            final int lenByteCnt = sizeOfInt(maxSize);
            this.ensureCapacity(this.off + maxSize + lenByteCnt + 1);
            final int result = IOUtils.encodeUTF8(chars, 0, chars.length, this.bytes, this.off + lenByteCnt + 1);
            final int utf8len = result - this.off - lenByteCnt - 1;
            final int utf8lenByteCnt = sizeOfInt(utf8len);
            if (lenByteCnt != utf8lenByteCnt) {
                System.arraycopy(this.bytes, this.off + lenByteCnt + 1, this.bytes, this.off + utf8lenByteCnt + 1, utf8len);
            }
            this.bytes[this.off++] = 122;
            if (utf8len >= -16 && utf8len <= 47) {
                this.bytes[this.off++] = (byte)utf8len;
            }
            else if (utf8len >= -2048 && utf8len <= 2047) {
                this.bytes[this.off] = (byte)(56 + (utf8len >> 8));
                this.bytes[this.off + 1] = (byte)utf8len;
                this.off += 2;
            }
            else {
                this.writeInt32(utf8len);
            }
            this.off += utf8len;
        }
    }
    
    @Override
    public void writeString(final String[] strings) {
        if (strings == null) {
            this.writeArrayNull();
            return;
        }
        this.startArray(strings.length);
        for (int i = 0; i < strings.length; ++i) {
            final String item = strings[i];
            if (item == null) {
                this.writeStringNull();
            }
            else {
                this.writeString(item);
            }
        }
    }
    
    @Override
    public void writeSymbol(final String str) {
        if (str == null) {
            this.writeNull();
            return;
        }
        if (this.symbolTable != null) {
            final int ordinal = this.symbolTable.getOrdinal(str);
            if (ordinal >= 0) {
                this.writeRaw((byte)127);
                this.writeInt32(-ordinal);
                return;
            }
        }
        this.writeString(str);
    }
    
    @Override
    public void writeTypeName(final String typeName) {
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = -110;
        final long hash = Fnv.hashCode64(typeName);
        int symbol = -1;
        if (this.symbolTable != null) {
            symbol = this.symbolTable.getOrdinalByHashCode(hash);
            if (symbol == -1 && this.symbols != null) {
                symbol = this.symbols.get(hash);
            }
        }
        else if (this.symbols != null) {
            symbol = this.symbols.get(hash);
        }
        if (symbol == -1) {
            if (this.symbols == null) {
                this.symbols = new TLongIntHashMap();
            }
            this.symbols.put(hash, symbol = this.symbolIndex++);
            this.writeString(typeName);
            this.writeInt32(symbol);
            return;
        }
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.writeInt32(symbol);
    }
    
    @Override
    public boolean writeTypeName(final byte[] typeName, final long hash) {
        if (this.symbolTable != null) {
            final int symbol = this.symbolTable.getOrdinalByHashCode(hash);
            if (symbol != -1) {
                return this.writeTypeNameSymbol(symbol);
            }
        }
        boolean symbolExists = false;
        int symbol2;
        if (this.rootTypeNameHash == hash) {
            symbolExists = true;
            symbol2 = 0;
        }
        else if (this.symbols != null) {
            symbol2 = this.symbols.putIfAbsent(hash, this.symbolIndex);
            if (symbol2 != this.symbolIndex) {
                symbolExists = true;
            }
            else {
                ++this.symbolIndex;
            }
        }
        else {
            symbol2 = this.symbolIndex++;
            if (symbol2 == 0) {
                this.rootTypeNameHash = hash;
            }
            if (symbol2 != 0 || (this.context.features & Feature.WriteNameAsSymbol.mask) != 0x0L) {
                this.symbols = new TLongIntHashMap(hash, symbol2);
            }
        }
        if (symbolExists) {
            this.writeTypeNameSymbol(-symbol2);
            return false;
        }
        int off = this.off;
        final int minCapacity = off + 2 + typeName.length;
        if (minCapacity > this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = -110;
        System.arraycopy(typeName, 0, bytes, off, typeName.length);
        off += typeName.length;
        if (symbol2 >= -16 && symbol2 <= 47) {
            bytes[off] = (byte)symbol2;
            this.off = off + 1;
        }
        else {
            this.off = off;
            this.writeInt32(symbol2);
        }
        return false;
    }
    
    private boolean writeTypeNameSymbol(final int symbol) {
        final int off = this.off;
        if (off + 2 >= this.bytes.length) {
            this.ensureCapacity(off + 2);
        }
        this.bytes[off] = -110;
        this.off = off + 1;
        this.writeInt32(-symbol);
        return false;
    }
    
    static int sizeOfInt(final int i) {
        if (i >= -16 && i <= 47) {
            return 1;
        }
        if (i >= -2048 && i <= 2047) {
            return 2;
        }
        if (i >= -262144 && i <= 262143) {
            return 3;
        }
        return 5;
    }
    
    @Override
    public void writeString(final List<String> list) {
        if (list == null) {
            this.writeArrayNull();
            return;
        }
        final int size = list.size();
        this.startArray(size);
        if (JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER != null) {
            final int mark = this.off;
            final int LATIN = 0;
            boolean latinAll = true;
            for (int i = 0; i < list.size(); ++i) {
                final String str = list.get(i);
                if (str == null) {
                    this.writeNull();
                }
                final int coder = JDKUtils.STRING_CODER.applyAsInt(str);
                if (coder != 0) {
                    latinAll = false;
                    this.off = mark;
                    break;
                }
                final int strlen = str.length();
                if (strlen + 3 > this.bytes.length) {
                    this.ensureCapacity(strlen + 3);
                }
                if (strlen <= 47) {
                    this.bytes[this.off++] = (byte)(strlen + 73);
                }
                else if (strlen <= 2047) {
                    final int off = this.off;
                    this.bytes[off] = 121;
                    this.bytes[off + 1] = (byte)(56 + (strlen >> 8));
                    this.bytes[off + 2] = (byte)strlen;
                    this.off += 3;
                }
                else {
                    this.bytes[this.off++] = 121;
                    this.writeInt32(strlen);
                }
                final byte[] value = JDKUtils.STRING_VALUE.apply(str);
                System.arraycopy(value, 0, this.bytes, this.off, value.length);
                this.off += strlen;
            }
            if (latinAll) {
                return;
            }
        }
        for (int j = 0; j < list.size(); ++j) {
            final String str2 = list.get(j);
            this.writeString(str2);
        }
    }
    
    @Override
    public void writeString(final String str) {
        if (str == null) {
            this.writeNull();
            return;
        }
        if (JDKUtils.STRING_VALUE != null) {
            final int coder = JDKUtils.STRING_CODER.applyAsInt(str);
            final byte[] value = JDKUtils.STRING_VALUE.apply(str);
            if (coder == 0) {
                int off = this.off;
                final int strlen = value.length;
                final int minCapacity = value.length + off + 6;
                if (minCapacity - this.bytes.length > 0) {
                    this.ensureCapacity(minCapacity);
                }
                final byte[] bytes = this.bytes;
                if (strlen <= 47) {
                    bytes[off++] = (byte)(strlen + 73);
                }
                else if (strlen <= 2047) {
                    putStringSizeSmall(bytes, off, strlen);
                    off += 3;
                }
                else {
                    off += putStringSizeLarge(bytes, off, strlen);
                }
                System.arraycopy(value, 0, bytes, off, value.length);
                this.off = off + strlen;
                return;
            }
            if (this.tryWriteStringUTF16(value)) {
                return;
            }
        }
        this.writeString(JDKUtils.getCharArray(str));
    }
    
    @Override
    public void writeStringUTF16(final byte[] value) {
        int off = this.off;
        final int strlen = value.length;
        final int minCapacity = off + strlen + 6;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off++] = (byte)(JDKUtils.BIG_ENDIAN ? 125 : 124);
        off += writeInt32(bytes, off, strlen);
        System.arraycopy(value, 0, bytes, off, strlen);
        this.off = off + strlen;
    }
    
    private boolean tryWriteStringUTF16(final byte[] value) {
        int check_cnt = 128;
        if (check_cnt > value.length) {
            check_cnt = value.length;
        }
        if ((check_cnt & 0x1) == 0x1) {
            --check_cnt;
        }
        int asciiCount = 0;
        for (int i = 0; i + 2 <= check_cnt; i += 2) {
            final byte b0 = value[i];
            final byte b2 = value[i + 1];
            if (b0 == 0 || b2 == 0) {
                ++asciiCount;
            }
        }
        boolean utf16 = value.length != 0 && (asciiCount == 0 || (check_cnt >> 1) / asciiCount >= 3);
        final int off = this.off;
        final int minCapacity = off + 6 + value.length * 2 + 1;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (!utf16) {
            final int maxSize = value.length + (value.length >> 2);
            final int lenByteCnt = sizeOfInt(maxSize);
            final int result = IOUtils.encodeUTF8(value, 0, value.length, bytes, off + lenByteCnt + 1);
            final int utf8len = result - off - lenByteCnt - 1;
            if (utf8len > value.length) {
                utf16 = true;
            }
            else if (result != -1) {
                this.off = off + writeUTF8(bytes, off, value, utf8len, asciiCount, lenByteCnt);
                return true;
            }
        }
        if (utf16) {
            this.off = off + writeUTF16(bytes, off, value);
            return true;
        }
        return false;
    }
    
    private static int writeUTF8(final byte[] bytes, final int off, final byte[] value, final int utf8len, final int asciiCount, final int lenByteCnt) {
        byte strtype;
        if (utf8len * 2 == value.length) {
            if (asciiCount <= 47) {
                bytes[off] = (byte)(73 + utf8len);
                System.arraycopy(bytes, off + 1 + lenByteCnt, bytes, off + 1, utf8len);
                return utf8len + 1;
            }
            strtype = 121;
        }
        else {
            strtype = 122;
        }
        final int utf8lenByteCnt = sizeOfInt(utf8len);
        if (lenByteCnt != utf8lenByteCnt) {
            System.arraycopy(bytes, off + lenByteCnt + 1, bytes, off + utf8lenByteCnt + 1, utf8len);
        }
        bytes[off] = strtype;
        return writeInt32(bytes, off + 1, utf8len) + utf8len + 1;
    }
    
    private static int writeUTF16(final byte[] bytes, final int off, final byte[] value) {
        bytes[off] = (byte)(JDKUtils.BIG_ENDIAN ? 125 : 124);
        final int size = writeInt32(bytes, off + 1, value.length);
        System.arraycopy(value, 0, bytes, off + size + 1, value.length);
        return value.length + size + 1;
    }
    
    void ensureCapacity(final int minCapacity) {
        if (minCapacity >= this.bytes.length) {
            final int oldCapacity = this.bytes.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            if (newCapacity - minCapacity < 0) {
                newCapacity = minCapacity;
            }
            if (newCapacity > this.maxArraySize) {
                throw new OutOfMemoryError();
            }
            this.bytes = Arrays.copyOf(this.bytes, newCapacity);
        }
    }
    
    @Override
    public void writeMillis(final long millis) {
        final int off = this.off;
        final int minCapacity = off + 9;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (millis % 1000L == 0L) {
            final long seconds = millis / 1000L;
            if (seconds >= -2147483648L && seconds <= 2147483647L) {
                final int secondsInt = (int)seconds;
                bytes[off] = -84;
                JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? secondsInt : Integer.reverseBytes(secondsInt));
                this.off = off + 5;
                return;
            }
            if (seconds % 60L == 0L) {
                final long minutes = seconds / 60L;
                if (minutes >= -2147483648L && minutes <= 2147483647L) {
                    final int minutesInt = (int)minutes;
                    bytes[off] = -83;
                    JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? minutesInt : Integer.reverseBytes(minutesInt));
                    this.off = off + 5;
                    return;
                }
            }
        }
        bytes[off] = -85;
        JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? millis : Long.reverseBytes(millis));
        this.off = off + 9;
    }
    
    @Override
    public void writeInt64(final Long i) {
        final int minCapacity = this.off + 9;
        if (minCapacity > this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        final int off = this.off;
        int size;
        if (i == null) {
            bytes[off] = (byte)(((this.context.features & JSONWriterJSONB.WRITE_NUM_NULL_MASK) == 0x0L) ? -81 : -32);
            size = 1;
        }
        else {
            final long val = i;
            if (val >= -8L && val <= 15L) {
                bytes[off] = (byte)(-40L + (val + 8L));
                size = 1;
            }
            else if (val >= -2048L && val <= 2047L) {
                bytes[off] = (byte)(-48L + (val >> 8));
                bytes[off + 1] = (byte)val;
                size = 2;
            }
            else if (val >= -262144L && val <= 262143L) {
                bytes[off] = (byte)(-60L + (val >> 16));
                bytes[off + 1] = (byte)(val >> 8);
                bytes[off + 2] = (byte)val;
                size = 3;
            }
            else if (val >= -2147483648L && val <= 2147483647L) {
                bytes[off] = -65;
                JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? ((int)val) : Integer.reverseBytes((int)val));
                size = 5;
            }
            else {
                size = writeInt64Large8(bytes, off, val);
            }
        }
        this.off = off + size;
    }
    
    @Override
    public void writeInt64(final long val) {
        final int minCapacity = this.off + 9;
        if (minCapacity > this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        final int off = this.off;
        int size;
        if (val >= -8L && val <= 15L) {
            bytes[off] = (byte)(-40L + (val + 8L));
            size = 1;
        }
        else if (val >= -2048L && val <= 2047L) {
            bytes[off] = (byte)(-48L + (val >> 8));
            bytes[off + 1] = (byte)val;
            size = 2;
        }
        else if (val >= -262144L && val <= 262143L) {
            bytes[off] = (byte)(-60L + (val >> 16));
            bytes[off + 1] = (byte)(val >> 8);
            bytes[off + 2] = (byte)val;
            size = 3;
        }
        else if (val >= -2147483648L && val <= 2147483647L) {
            bytes[off] = -65;
            JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? ((int)val) : Integer.reverseBytes((int)val));
            size = 5;
        }
        else {
            bytes[off] = -66;
            JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? val : Long.reverseBytes(val));
            size = 9;
        }
        this.off = off + size;
    }
    
    @Override
    public void writeInt64(final long[] value) {
        if (value == null) {
            this.writeArrayNull();
            return;
        }
        final int size = value.length;
        int off = this.off;
        final int minCapacity = off + size * 9 + 5;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (size <= 15) {
            bytes[off++] = (byte)(-108 + size);
        }
        else {
            bytes[off] = -92;
            off += writeInt32(bytes, off + 1, size) + 1;
        }
        for (int i = 0; i < value.length; ++i) {
            final long val = value[i];
            if (val >= -8L && val <= 15L) {
                bytes[off++] = (byte)(-40L + (val + 8L));
            }
            else if (val >= -2048L && val <= 2047L) {
                bytes[off] = (byte)(-48L + (val >> 8));
                bytes[off + 1] = (byte)val;
                off += 2;
            }
            else if (val >= -262144L && val <= 262143L) {
                bytes[off] = (byte)(-60L + (val >> 16));
                bytes[off + 1] = (byte)(val >> 8);
                bytes[off + 2] = (byte)val;
                off += 3;
            }
            else if (val >= -2147483648L && val <= 2147483647L) {
                bytes[off] = -65;
                JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? ((int)val) : Integer.reverseBytes((int)val));
                off += 5;
            }
            else {
                bytes[off] = -66;
                JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? val : Long.reverseBytes(val));
                off += 9;
            }
        }
        this.off = off;
    }
    
    @Override
    public void writeListInt64(final List<Long> values) {
        if (values == null) {
            this.writeArrayNull();
            return;
        }
        final int size = values.size();
        int off = this.off;
        final int minCapacity = off + size * 9 + 5;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (size <= 15) {
            bytes[off++] = (byte)(-108 + size);
        }
        else {
            bytes[off] = -92;
            off += writeInt32(bytes, off + 1, size) + 1;
        }
        for (int i = 0; i < size; ++i) {
            final Long item = values.get(i);
            if (item == null) {
                bytes[off++] = -81;
            }
            else {
                final long val = item;
                if (val >= -8L && val <= 15L) {
                    bytes[off++] = (byte)(-40L + (val + 8L));
                }
                else if (val >= -2048L && val <= 2047L) {
                    bytes[off] = (byte)(-48L + (val >> 8));
                    bytes[off + 1] = (byte)val;
                    off += 2;
                }
                else if (val >= -262144L && val <= 262143L) {
                    bytes[off] = (byte)(-60L + (val >> 16));
                    bytes[off + 1] = (byte)(val >> 8);
                    bytes[off + 2] = (byte)val;
                    off += 3;
                }
                else {
                    off += writeInt64Large(bytes, off, val);
                }
            }
        }
        this.off = off;
    }
    
    private static int writeInt64Large(final byte[] bytes, final int off, final long val) {
        if (val >= -2147483648L && val <= 2147483647L) {
            bytes[off] = -65;
            JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? ((int)val) : Integer.reverseBytes((int)val));
            return 5;
        }
        return writeInt64Large8(bytes, off, val);
    }
    
    private static int writeInt64Large8(final byte[] bytes, final int off, final long val) {
        bytes[off] = -66;
        JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? val : Long.reverseBytes(val));
        return 9;
    }
    
    @Override
    public void writeFloat(final float value) {
        int off = this.off;
        final int minCapacity = off + 5;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        int i = (int)value;
        if (i == value && value >= -262144.0f && value <= 262143.0f) {
            bytes[off] = -74;
            off += writeInt32(bytes, off + 1, i) + 1;
        }
        else {
            bytes[off] = -73;
            i = Float.floatToIntBits(value);
            JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? i : Integer.reverseBytes(i));
            off += 5;
        }
        this.off = off;
    }
    
    @Override
    public void writeFloat(final float[] values) {
        if (values == null) {
            this.writeNull();
            return;
        }
        this.startArray(values.length);
        for (int i = 0; i < values.length; ++i) {
            this.writeFloat(values[i]);
        }
        this.endArray();
    }
    
    @Override
    public void writeDouble(final double value) {
        if (value == 0.0) {
            this.ensureCapacity(this.off + 1);
            this.bytes[this.off++] = -78;
            return;
        }
        final int off = this.off;
        if (value == 1.0) {
            this.ensureCapacity(off + 1);
            this.bytes[off] = -77;
            this.off = off + 1;
            return;
        }
        if (value >= -2.147483648E9 && value <= 2.147483647E9) {
            final long longValue = (long)value;
            if (longValue == value) {
                this.ensureCapacity(off + 1);
                this.bytes[off] = -76;
                this.off = off + 1;
                this.writeInt64(longValue);
                return;
            }
        }
        this.ensureCapacity(off + 9);
        final byte[] bytes = this.bytes;
        bytes[off] = -75;
        final long i = Double.doubleToLongBits(value);
        JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? i : Long.reverseBytes(i));
        this.off = off + 9;
    }
    
    @Override
    public void writeDouble(final double[] values) {
        if (values == null) {
            this.writeNull();
            return;
        }
        this.startArray(values.length);
        for (int i = 0; i < values.length; ++i) {
            this.writeDouble(values[i]);
        }
        this.endArray();
    }
    
    @Override
    public void writeInt16(final short[] values) {
        if (values == null) {
            this.writeNull();
            return;
        }
        this.startArray(values.length);
        for (int i = 0; i < values.length; ++i) {
            this.writeInt32(values[i]);
        }
        this.endArray();
    }
    
    @Override
    public void writeInt32(final int[] values) {
        if (values == null) {
            this.writeArrayNull();
            return;
        }
        final int size = values.length;
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        if (size <= 15) {
            this.bytes[this.off++] = (byte)(-108 + size);
        }
        else {
            this.bytes[this.off++] = -92;
            this.writeInt32(size);
        }
        int off = this.off;
        final int minCapacity = off + values.length * 5;
        if (minCapacity - this.bytes.length > 0) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        for (int i = 0; i < values.length; ++i) {
            final int val = values[i];
            if (val >= -16 && val <= 47) {
                bytes[off++] = (byte)val;
            }
            else if (val >= -2048 && val <= 2047) {
                bytes[off++] = (byte)(56 + (val >> 8));
                bytes[off++] = (byte)val;
            }
            else if (val >= -262144 && val <= 262143) {
                bytes[off] = (byte)(68 + (val >> 16));
                bytes[off + 1] = (byte)(val >> 8);
                bytes[off + 2] = (byte)val;
                off += 3;
            }
            else {
                bytes[off] = 72;
                JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? val : Integer.reverseBytes(val));
                off += 5;
            }
        }
        this.off = off;
    }
    
    @Override
    public void writeInt8(final byte val) {
        final int off = this.off;
        final int minCapacity = off + 2;
        if (minCapacity - this.bytes.length > 0) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off] = -67;
        bytes[off + 1] = val;
        this.off = off + 2;
    }
    
    @Override
    public void writeInt16(final short val) {
        final int off = this.off;
        final int minCapacity = off + 3;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[off] = -68;
        bytes[off + 1] = (byte)(val >>> 8);
        bytes[off + 2] = (byte)val;
        this.off = off + 3;
    }
    
    @Override
    public void writeEnum(final Enum e) {
        if (e == null) {
            this.writeNull();
            return;
        }
        if ((this.context.features & JSONWriterJSONB.WRITE_ENUM_USING_STRING_MASK) != 0x0L) {
            this.writeString(((this.context.features & Feature.WriteEnumUsingToString.mask) != 0x0L) ? e.toString() : e.name());
        }
        else {
            final int val = e.ordinal();
            if (val <= 47) {
                if (this.off == this.bytes.length) {
                    this.ensureCapacity(this.off + 1);
                }
                this.bytes[this.off++] = (byte)val;
                return;
            }
            this.writeInt32(val);
        }
    }
    
    @Override
    public void writeInt32(final Integer i) {
        final int minCapacity = this.off + 5;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        final int off = this.off;
        int size;
        if (i == null) {
            if ((this.context.features & (Feature.NullAsDefaultValue.mask | Feature.WriteNullNumberAsZero.mask)) == 0x0L) {
                bytes[off] = -81;
            }
            else {
                bytes[off] = 0;
            }
            size = 1;
        }
        else {
            final int val = i;
            if (val >= -16 && val <= 47) {
                bytes[off] = (byte)val;
                size = 1;
            }
            else if (val >= -2048 && val <= 2047) {
                bytes[off] = (byte)(56 + (val >> 8));
                bytes[off + 1] = (byte)val;
                size = 2;
            }
            else if (val >= -262144 && val <= 262143) {
                bytes[off] = (byte)(68 + (val >> 16));
                bytes[off + 1] = (byte)(val >> 8);
                bytes[off + 2] = (byte)val;
                size = 3;
            }
            else {
                bytes[off] = 72;
                JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? val : Integer.reverseBytes(val));
                size = 5;
            }
        }
        this.off += size;
    }
    
    @Override
    public void writeInt32(final int val) {
        final int minCapacity = this.off + 5;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        final int off = this.off;
        int size;
        if (val >= -16 && val <= 47) {
            bytes[off] = (byte)val;
            size = 1;
        }
        else if (val >= -2048 && val <= 2047) {
            bytes[off] = (byte)(56 + (val >> 8));
            bytes[off + 1] = (byte)val;
            size = 2;
        }
        else if (val >= -262144 && val <= 262143) {
            bytes[off] = (byte)(68 + (val >> 16));
            bytes[off + 1] = (byte)(val >> 8);
            bytes[off + 2] = (byte)val;
            size = 3;
        }
        else {
            bytes[off] = 72;
            JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? val : Integer.reverseBytes(val));
            size = 5;
        }
        this.off += size;
    }
    
    @Override
    public void writeListInt32(final List<Integer> values) {
        if (values == null) {
            this.writeArrayNull();
            return;
        }
        final int size = values.size();
        int off = this.off;
        final int minCapacity = off + size * 5 + 5;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        if (size <= 15) {
            bytes[off++] = (byte)(-108 + size);
        }
        else {
            bytes[off] = -92;
            off += writeInt32(bytes, off + 1, size) + 1;
        }
        for (int i = 0; i < size; ++i) {
            final Number item = values.get(i);
            if (item == null) {
                bytes[off++] = -81;
            }
            else {
                final int val = item.intValue();
                if (val >= -16 && val <= 47) {
                    bytes[off++] = (byte)val;
                }
                else if (val >= -2048 && val <= 2047) {
                    bytes[off] = (byte)(56 + (val >> 8));
                    bytes[off + 1] = (byte)val;
                    off += 2;
                }
                else if (val >= -262144 && val <= 262143) {
                    bytes[off] = (byte)(68 + (val >> 16));
                    bytes[off + 1] = (byte)(val >> 8);
                    bytes[off + 2] = (byte)val;
                    off += 3;
                }
                else {
                    bytes[off] = 72;
                    JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? val : Integer.reverseBytes(val));
                    off += 5;
                }
            }
        }
        this.off = off;
    }
    
    public static int writeInt32(final byte[] bytes, final int off, final int val) {
        if (val >= -16 && val <= 47) {
            bytes[off] = (byte)val;
            return 1;
        }
        if (val >= -2048 && val <= 2047) {
            bytes[off] = (byte)(56 + (val >> 8));
            bytes[off + 1] = (byte)val;
            return 2;
        }
        if (val >= -262144 && val <= 262143) {
            bytes[off] = (byte)(68 + (val >> 16));
            bytes[off + 1] = (byte)(val >> 8);
            bytes[off + 2] = (byte)val;
            return 3;
        }
        bytes[off] = 72;
        JDKUtils.UNSAFE.putInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, JDKUtils.BIG_ENDIAN ? val : Integer.reverseBytes(val));
        return 5;
    }
    
    @Override
    public void writeArrayNull() {
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = (byte)(((this.context.features & JSONWriterJSONB.WRITE_ARRAY_NULL_MASK) != 0x0L) ? -108 : -81);
    }
    
    @Override
    public void writeRaw(final String str) {
        throw new JSONException("unsupported operation");
    }
    
    @Override
    public void writeRaw(final byte[] bytes) {
        final int minCapacity = this.off + bytes.length;
        if (minCapacity - this.bytes.length > 0) {
            this.ensureCapacity(minCapacity);
        }
        System.arraycopy(bytes, 0, this.bytes, this.off, bytes.length);
        this.off += bytes.length;
    }
    
    @Override
    public void writeSymbol(final int symbol) {
        final int minCapacity = this.off + 3;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        bytes[this.off++] = 127;
        if (symbol >= -16 && symbol <= 47) {
            bytes[this.off++] = (byte)symbol;
            return;
        }
        if (symbol >= -2048 && symbol <= 2047) {
            bytes[this.off] = (byte)(56 + (symbol >> 8));
            bytes[this.off + 1] = (byte)symbol;
            this.off += 2;
            return;
        }
        this.writeInt32(symbol);
    }
    
    @Override
    public void writeNameRaw(final byte[] name, final long nameHash) {
        int off = this.off;
        final int minCapacity = off + name.length + 2;
        if (minCapacity >= this.bytes.length) {
            this.ensureCapacity(minCapacity);
        }
        final byte[] bytes = this.bytes;
        int symbol;
        if (this.symbolTable == null || (symbol = this.symbolTable.getOrdinalByHashCode(nameHash)) == -1) {
            if ((this.context.features & Feature.WriteNameAsSymbol.mask) == 0x0L) {
                System.arraycopy(name, 0, bytes, off, name.length);
                this.off = off + name.length;
                return;
            }
            boolean symbolExists = false;
            if (this.symbols != null) {
                if ((symbol = this.symbols.putIfAbsent(nameHash, this.symbolIndex)) != this.symbolIndex) {
                    symbolExists = true;
                }
                else {
                    ++this.symbolIndex;
                }
            }
            else {
                (this.symbols = new TLongIntHashMap()).put(nameHash, symbol = this.symbolIndex++);
            }
            if (!symbolExists) {
                bytes[off++] = 127;
                System.arraycopy(name, 0, bytes, off, name.length);
                this.off = off + name.length;
                if (symbol >= -16 && symbol <= 47) {
                    bytes[this.off++] = (byte)symbol;
                }
                else {
                    this.writeInt32(symbol);
                }
                return;
            }
            symbol = -symbol;
        }
        bytes[off++] = 127;
        final int intValue = -symbol;
        if (intValue >= -16 && intValue <= 47) {
            bytes[off] = (byte)intValue;
            this.off = off + 1;
        }
        else {
            this.off = off;
            this.writeInt32(intValue);
        }
    }
    
    @Override
    public void writeLocalDate(final LocalDate date) {
        if (date == null) {
            this.writeNull();
            return;
        }
        final int off = this.off;
        this.ensureCapacity(off + 5);
        final byte[] bytes = this.bytes;
        bytes[off] = -87;
        final int year = date.getYear();
        bytes[off + 1] = (byte)(year >>> 8);
        bytes[off + 2] = (byte)year;
        bytes[off + 3] = (byte)date.getMonthValue();
        bytes[off + 4] = (byte)date.getDayOfMonth();
        this.off = off + 5;
    }
    
    @Override
    public void writeLocalTime(final LocalTime time) {
        if (time == null) {
            this.writeNull();
            return;
        }
        final int off = this.off;
        this.ensureCapacity(off + 4);
        final byte[] bytes = this.bytes;
        bytes[off] = -89;
        bytes[off + 1] = (byte)time.getHour();
        bytes[off + 2] = (byte)time.getMinute();
        bytes[off + 3] = (byte)time.getSecond();
        this.off = off + 4;
        final int nano = time.getNano();
        this.writeInt32(nano);
    }
    
    @Override
    public void writeLocalDateTime(final LocalDateTime dateTime) {
        if (dateTime == null) {
            this.writeNull();
            return;
        }
        final int off = this.off;
        this.ensureCapacity(off + 8);
        final byte[] bytes = this.bytes;
        bytes[off] = -88;
        final int year = dateTime.getYear();
        bytes[off + 1] = (byte)(year >>> 8);
        bytes[off + 2] = (byte)year;
        bytes[off + 3] = (byte)dateTime.getMonthValue();
        bytes[off + 4] = (byte)dateTime.getDayOfMonth();
        bytes[off + 5] = (byte)dateTime.getHour();
        bytes[off + 6] = (byte)dateTime.getMinute();
        bytes[off + 7] = (byte)dateTime.getSecond();
        this.off = off + 8;
        final int nano = dateTime.getNano();
        this.writeInt32(nano);
    }
    
    @Override
    public void writeZonedDateTime(final ZonedDateTime dateTime) {
        if (dateTime == null) {
            this.writeNull();
            return;
        }
        final int off = this.off;
        this.ensureCapacity(off + 8);
        final byte[] bytes = this.bytes;
        bytes[off] = -86;
        final int year = dateTime.getYear();
        bytes[off + 1] = (byte)(year >>> 8);
        bytes[off + 2] = (byte)year;
        bytes[off + 3] = (byte)dateTime.getMonthValue();
        bytes[off + 4] = (byte)dateTime.getDayOfMonth();
        bytes[off + 5] = (byte)dateTime.getHour();
        bytes[off + 6] = (byte)dateTime.getMinute();
        bytes[off + 7] = (byte)dateTime.getSecond();
        this.off = off + 8;
        final int nano = dateTime.getNano();
        this.writeInt32(nano);
        final ZoneId zoneId = dateTime.getZone();
        final String zoneIdStr = zoneId.getId();
        if (zoneIdStr.equals("Asia/Shanghai")) {
            this.writeRaw(JSONWriterJSONB.SHANGHAI_ZONE_ID_NAME_BYTES);
        }
        else {
            this.writeString(zoneIdStr);
        }
    }
    
    @Override
    public void writeOffsetDateTime(final OffsetDateTime dateTime) {
        if (dateTime == null) {
            this.writeNull();
            return;
        }
        final int off = this.off;
        this.ensureCapacity(off + 8);
        final byte[] bytes = this.bytes;
        bytes[off] = -86;
        final int year = dateTime.getYear();
        bytes[off + 1] = (byte)(year >>> 8);
        bytes[off + 2] = (byte)year;
        bytes[off + 3] = (byte)dateTime.getMonthValue();
        bytes[off + 4] = (byte)dateTime.getDayOfMonth();
        bytes[off + 5] = (byte)dateTime.getHour();
        bytes[off + 6] = (byte)dateTime.getMinute();
        bytes[off + 7] = (byte)dateTime.getSecond();
        this.off = off + 8;
        final int nano = dateTime.getNano();
        this.writeInt32(nano);
        final ZoneId zoneId = dateTime.getOffset();
        final String zoneIdStr = zoneId.getId();
        if (zoneIdStr.equals("+08:00")) {
            this.writeRaw(JSONWriterJSONB.OFFSET_8_ZONE_ID_NAME_BYTES);
        }
        else {
            this.writeString(zoneIdStr);
        }
    }
    
    @Override
    public void writeInstant(final Instant instant) {
        if (instant == null) {
            this.writeNull();
            return;
        }
        this.ensureCapacity(this.off + 1);
        this.bytes[this.off++] = -82;
        final long second = instant.getEpochSecond();
        final int nano = instant.getNano();
        this.writeInt64(second);
        this.writeInt32(nano);
    }
    
    @Override
    public void writeUUID(final UUID value) {
        if (value == null) {
            this.writeNull();
            return;
        }
        final int off = this.off;
        this.ensureCapacity(off + 18);
        final byte[] bytes = this.bytes;
        bytes[off] = -111;
        bytes[off + 1] = 16;
        final long msb = value.getMostSignificantBits();
        JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 2L, JDKUtils.BIG_ENDIAN ? msb : Long.reverseBytes(msb));
        final long lsb = value.getLeastSignificantBits();
        JDKUtils.UNSAFE.putLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 10L, JDKUtils.BIG_ENDIAN ? lsb : Long.reverseBytes(lsb));
        this.off = off + 18;
    }
    
    @Override
    public void writeBigInt(final BigInteger value, final long features) {
        if (value == null) {
            this.writeNull();
            return;
        }
        if (TypeUtils.isInt64(value)) {
            if (this.off == this.bytes.length) {
                this.ensureCapacity(this.off + 1);
            }
            this.bytes[this.off++] = -70;
            final long int64Value = value.longValue();
            this.writeInt64(int64Value);
            return;
        }
        final byte[] valueBytes = value.toByteArray();
        this.ensureCapacity(this.off + 5 + valueBytes.length);
        this.bytes[this.off++] = -69;
        this.writeInt32(valueBytes.length);
        System.arraycopy(valueBytes, 0, this.bytes, this.off, valueBytes.length);
        this.off += valueBytes.length;
    }
    
    @Override
    public void writeBinary(final byte[] bytes) {
        if (bytes == null) {
            this.writeNull();
            return;
        }
        this.ensureCapacity(this.off + 6 + bytes.length);
        this.bytes[this.off++] = -111;
        this.writeInt32(bytes.length);
        System.arraycopy(bytes, 0, this.bytes, this.off, bytes.length);
        this.off += bytes.length;
    }
    
    @Override
    public void writeDecimal(final BigDecimal value, final long features, final DecimalFormat format) {
        if (value == null) {
            this.writeNull();
            return;
        }
        final int precision = value.precision();
        final int scale = value.scale();
        if (precision < 19 && JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET != -1L) {
            final long intCompact = JDKUtils.UNSAFE.getLong(value, JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET);
            if (scale == 0) {
                this.ensureCapacity(this.off + 1);
                this.bytes[this.off++] = -72;
                this.writeInt64(intCompact);
                return;
            }
            this.ensureCapacity(this.off + 1);
            this.bytes[this.off++] = -71;
            this.writeInt32(scale);
            if (intCompact >= -2147483648L && intCompact <= 2147483647L) {
                this.writeInt32((int)intCompact);
            }
            else {
                this.writeInt64(intCompact);
            }
        }
        else {
            final BigInteger unscaledValue = value.unscaledValue();
            if (scale == 0 && TypeUtils.isInt64(unscaledValue)) {
                this.ensureCapacity(this.off + 1);
                this.bytes[this.off++] = -72;
                final long longValue = unscaledValue.longValue();
                this.writeInt64(longValue);
                return;
            }
            this.ensureCapacity(this.off + 1);
            this.bytes[this.off++] = -71;
            this.writeInt32(scale);
            if (TypeUtils.isInt32(unscaledValue)) {
                final int intValue = unscaledValue.intValue();
                this.writeInt32(intValue);
            }
            else if (TypeUtils.isInt64(unscaledValue)) {
                final long longValue = unscaledValue.longValue();
                this.writeInt64(longValue);
            }
            else {
                this.writeBigInt(unscaledValue, 0L);
            }
        }
    }
    
    @Override
    public void writeBool(final boolean value) {
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = (byte)(value ? -79 : -80);
    }
    
    @Override
    public void writeBool(final boolean[] valeus) {
        if (valeus == null) {
            this.writeNull();
            return;
        }
        this.startArray(valeus.length);
        for (int i = 0; i < valeus.length; ++i) {
            this.writeBool(valeus[i]);
        }
        this.endArray();
    }
    
    @Override
    public void writeReference(final String path) {
        if (this.off == this.bytes.length) {
            this.ensureCapacity(this.off + 1);
        }
        this.bytes[this.off++] = -109;
        if (path == this.lastReference) {
            this.writeString("#-1");
        }
        else {
            this.writeString(path);
        }
        this.lastReference = path;
    }
    
    @Override
    public void writeDateTime14(final int year, final int month, final int dayOfMonth, final int hour, final int minute, final int second) {
        final int off = this.off;
        this.ensureCapacity(off + 8);
        final byte[] bytes = this.bytes;
        bytes[off] = -88;
        bytes[off + 1] = (byte)(year >>> 8);
        bytes[off + 2] = (byte)year;
        bytes[off + 3] = (byte)month;
        bytes[off + 4] = (byte)dayOfMonth;
        bytes[off + 5] = (byte)hour;
        bytes[off + 6] = (byte)minute;
        bytes[off + 7] = (byte)second;
        this.off = off + 8;
        final int nano = 0;
        this.writeInt32(nano);
    }
    
    @Override
    public void writeDateTime19(final int year, final int month, final int dayOfMonth, final int hour, final int minute, final int second) {
        final int off = this.off;
        this.ensureCapacity(off + 8);
        final byte[] bytes = this.bytes;
        bytes[off] = -88;
        bytes[off + 1] = (byte)(year >>> 8);
        bytes[off + 2] = (byte)year;
        bytes[off + 3] = (byte)month;
        bytes[off + 4] = (byte)dayOfMonth;
        bytes[off + 5] = (byte)hour;
        bytes[off + 6] = (byte)minute;
        bytes[off + 7] = (byte)second;
        this.off = off + 8;
        final int nano = 0;
        this.writeInt32(nano);
    }
    
    @Override
    public void writeDateTimeISO8601(final int year, final int month, final int dayOfMonth, final int hour, final int minute, final int second, final int millis, final int offsetSeconds, final boolean timeZone) {
        throw new JSONException("unsupported operation");
    }
    
    @Override
    public void writeDateYYYMMDD8(final int year, final int month, final int dayOfMonth) {
        final int off = this.off;
        this.ensureCapacity(off + 5);
        final byte[] bytes = this.bytes;
        bytes[off] = -87;
        bytes[off + 1] = (byte)(year >>> 8);
        bytes[off + 2] = (byte)year;
        bytes[off + 3] = (byte)month;
        bytes[off + 4] = (byte)dayOfMonth;
        this.off = off + 5;
    }
    
    @Override
    public void writeDateYYYMMDD10(final int year, final int month, final int dayOfMonth) {
        throw new JSONException("unsupported operation");
    }
    
    @Override
    public void writeTimeHHMMSS8(final int hour, final int minute, final int second) {
        throw new JSONException("unsupported operation");
    }
    
    @Override
    public void writeBase64(final byte[] bytes) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public void writeHex(final byte[] bytes) {
        this.writeBinary(bytes);
    }
    
    @Override
    public void writeRaw(final char ch) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public void writeNameRaw(final byte[] bytes) {
        this.writeRaw(bytes);
    }
    
    @Override
    public void writeNameRaw(final char[] chars) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public void writeNameRaw(final char[] bytes, final int offset, final int len) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public void writeColon() {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public void write(final List array) {
        if (array == null) {
            this.writeArrayNull();
            return;
        }
        final int size = array.size();
        this.startArray(size);
        for (int i = 0; i < array.size(); ++i) {
            final Object item = array.get(i);
            this.writeAny(item);
        }
    }
    
    @Override
    public void write(final Map map) {
        if (map == null) {
            this.writeNull();
            return;
        }
        this.startObject();
        for (final Map.Entry entry : map.entrySet()) {
            this.writeAny(entry.getKey());
            this.writeAny(entry.getValue());
        }
        this.endObject();
    }
    
    @Override
    public void write(final JSONObject object) {
        if (object == null) {
            this.writeNull();
            return;
        }
        this.startObject();
        for (final Map.Entry entry : object.entrySet()) {
            this.writeAny(entry.getKey());
            this.writeAny(entry.getValue());
        }
        this.endObject();
    }
    
    @Override
    public byte[] getBytes() {
        return Arrays.copyOf(this.bytes, this.off);
    }
    
    @Override
    public int size() {
        return this.off;
    }
    
    @Override
    public byte[] getBytes(final Charset charset) {
        throw new JSONException("not support operator");
    }
    
    @Override
    public int flushTo(final OutputStream to) throws IOException {
        final int len = this.off;
        to.write(this.bytes, 0, this.off);
        this.off = 0;
        return len;
    }
    
    @Override
    public int flushTo(final OutputStream out, final Charset charset) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public String toString() {
        if (this.bytes.length == 0) {
            return "<empty>";
        }
        final byte[] jsonbBytes = this.getBytes();
        final JSONReader reader = JSONReader.ofJSONB(jsonbBytes);
        final JSONWriter writer = JSONWriter.of();
        try {
            final Object object = reader.readAny();
            writer.writeAny(object);
            return writer.toString();
        }
        catch (Exception ex) {
            return JSONB.typeName(this.bytes[0]) + ", bytes length " + this.off;
        }
    }
    
    static {
        SHANGHAI_ZONE_ID_NAME_BYTES = JSONB.toBytes("Asia/Shanghai");
        OFFSET_8_ZONE_ID_NAME_BYTES = JSONB.toBytes("+08:00");
        WRITE_ENUM_USING_STRING_MASK = (Feature.WriteEnumUsingToString.mask | Feature.WriteEnumsUsingName.mask);
        WRITE_NUM_NULL_MASK = (Feature.NullAsDefaultValue.mask | Feature.WriteNullNumberAsZero.mask);
    }
}
