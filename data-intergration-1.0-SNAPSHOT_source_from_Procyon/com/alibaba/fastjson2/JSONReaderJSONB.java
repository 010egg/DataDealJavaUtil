// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.UUID;
import java.time.ZoneOffset;
import java.time.OffsetDateTime;
import com.alibaba.fastjson2.reader.ObjectReaderImplInt32ValueArray;
import com.alibaba.fastjson2.reader.ObjectReaderImplInt32Array;
import com.alibaba.fastjson2.reader.ObjectReaderImplInt64Array;
import com.alibaba.fastjson2.reader.ObjectReaderImplInt64ValueArray;
import com.alibaba.fastjson2.reader.ObjectReaderImplStringArray;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import java.time.ZoneId;
import java.time.Instant;
import java.time.ZonedDateTime;
import com.alibaba.fastjson2.util.DateUtils;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.Date;
import java.math.BigDecimal;
import com.alibaba.fastjson2.util.IOUtils;
import java.math.BigInteger;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.util.JDKUtils;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.Arrays;
import java.io.InputStream;
import java.nio.charset.Charset;

final class JSONReaderJSONB extends JSONReader
{
    static final long BASE;
    static final byte[] SHANGHAI_ZONE_ID_NAME_BYTES;
    static Charset GB18030;
    protected final byte[] bytes;
    protected final int length;
    protected final int end;
    protected byte type;
    protected int strlen;
    protected byte strtype;
    protected int strBegin;
    protected byte[] valueBytes;
    protected final JSONFactory.CacheItem cacheItem;
    protected final SymbolTable symbolTable;
    protected long symbol0Hash;
    protected int symbol0Begin;
    protected int symbol0Length;
    protected byte symbol0StrType;
    protected long[] symbols;
    
    JSONReaderJSONB(final Context ctx, final InputStream is) {
        super(ctx, true, false);
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
        this.end = this.length;
        this.symbolTable = ctx.symbolTable;
    }
    
    JSONReaderJSONB(final Context ctx, final byte[] bytes, final int off, final int length) {
        super(ctx, true, false);
        this.bytes = bytes;
        this.offset = off;
        this.length = length;
        this.end = off + length;
        this.symbolTable = ctx.symbolTable;
        this.cacheItem = JSONFactory.CACHE_ITEMS[System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1];
    }
    
    @Override
    public String getString() {
        if (this.strtype == -81) {
            return null;
        }
        if (this.strlen < 0) {
            return this.symbolTable.getName(-this.strlen);
        }
        Charset charset;
        if (this.strtype == 121) {
            charset = StandardCharsets.ISO_8859_1;
        }
        else if (this.strtype >= 73 && this.strtype <= 120) {
            if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                final char[] chars = new char[this.strlen];
                for (int i = 0; i < this.strlen; ++i) {
                    chars[i] = (char)(this.bytes[this.strBegin + i] & 0xFF);
                }
                return JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
            }
            if (JDKUtils.STRING_CREATOR_JDK11 != null) {
                final byte[] chars2 = new byte[this.strlen];
                System.arraycopy(this.bytes, this.strBegin, chars2, 0, this.strlen);
                return JDKUtils.STRING_CREATOR_JDK11.apply(chars2, JDKUtils.LATIN1);
            }
            charset = StandardCharsets.ISO_8859_1;
        }
        else if (this.strtype == 122) {
            charset = StandardCharsets.UTF_8;
        }
        else if (this.strtype == 123) {
            charset = StandardCharsets.UTF_16;
        }
        else if (this.strtype == 124) {
            charset = StandardCharsets.UTF_16LE;
        }
        else if (this.strtype == 125) {
            charset = StandardCharsets.UTF_16BE;
        }
        else {
            if (this.strtype == 127) {
                final int symbol = this.strlen;
                final int index = symbol * 2;
                throw new JSONException("TODO : " + JSONB.typeName(this.strtype));
            }
            throw new JSONException("TODO : " + JSONB.typeName(this.strtype));
        }
        return new String(this.bytes, this.strBegin, this.strlen, charset);
    }
    
    public int readLength() {
        final byte type = this.bytes[this.offset++];
        if (type >= -16 && type <= 47) {
            return type;
        }
        if (type >= 48 && type <= 63) {
            return (type - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
        }
        if (type >= 64 && type <= 71) {
            final int len = getInt3(this.bytes, this.offset, type);
            this.offset += 2;
            return len;
        }
        if (type != 72) {
            throw new JSONException("not support length type : " + JSONB.typeName(type));
        }
        final int len = getInt(this.bytes, this.offset);
        this.offset += 4;
        if (len > 268435456) {
            throw new JSONException("input length overflow");
        }
        return len;
    }
    
    static int getInt3(final byte[] bytes, final int offset, final int type) {
        return (type - 68 << 16) + ((bytes[offset] & 0xFF) << 8) + (bytes[offset + 1] & 0xFF);
    }
    
    @Override
    public boolean isArray() {
        if (this.offset >= this.bytes.length) {
            return false;
        }
        final byte type = this.bytes[this.offset];
        return type >= -108 && type <= -92;
    }
    
    @Override
    public boolean isObject() {
        return this.offset < this.end && this.bytes[this.offset] == -90;
    }
    
    @Override
    public boolean isNumber() {
        final byte type = this.bytes[this.offset];
        return type >= -78 && type <= 72;
    }
    
    @Override
    public boolean isString() {
        return this.offset < this.bytes.length && (this.type = this.bytes[this.offset]) >= 73;
    }
    
    @Override
    public boolean nextIfMatch(final char ch) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public boolean nextIfArrayStart() {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public boolean nextIfComma() {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public boolean nextIfArrayEnd() {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public boolean nextIfObjectStart() {
        if (this.bytes[this.offset] != -90) {
            return false;
        }
        ++this.offset;
        return true;
    }
    
    @Override
    public boolean nextIfObjectEnd() {
        if (this.bytes[this.offset] != -91) {
            return false;
        }
        ++this.offset;
        return true;
    }
    
    @Override
    public boolean nextIfNullOrEmptyString() {
        if (this.bytes[this.offset] == -81) {
            ++this.offset;
            return true;
        }
        if (this.bytes[this.offset] != 73) {
            return false;
        }
        ++this.offset;
        return true;
    }
    
    @Override
    public <T> T read(final Type type) {
        final boolean fieldBased = (this.context.features & Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = this.context.provider.getObjectReader(type, fieldBased);
        return objectReader.readJSONBObject(this, null, null, 0L);
    }
    
    @Override
    public <T> T read(final Class<T> type) {
        final boolean fieldBased = (this.context.features & Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = this.context.provider.getObjectReader(type, fieldBased);
        return objectReader.readJSONBObject(this, null, null, 0L);
    }
    
    @Override
    public Map<String, Object> readObject() {
        this.type = this.bytes[this.offset++];
        if (this.type == -81) {
            return null;
        }
        if (this.type >= -90) {
            Map map;
            if ((this.context.features & Feature.UseNativeObject.mask) != 0x0L) {
                if (JDKUtils.JVM_VERSION == 8 && this.bytes[this.offset] != -91) {
                    map = new HashMap(10);
                }
                else {
                    map = new HashMap();
                }
            }
            else if (JDKUtils.JVM_VERSION == 8 && this.bytes[this.offset] != -91) {
                map = new JSONObject(10);
            }
            else {
                map = new JSONObject();
            }
            int i = 0;
            while (true) {
                this.type = this.bytes[this.offset];
                if (this.type == -91) {
                    break;
                }
                Object name;
                if (this.isString()) {
                    name = this.readFieldName();
                }
                else {
                    name = this.readAny();
                }
                if (this.offset < this.bytes.length && this.bytes[this.offset] == -109) {
                    final String reference = this.readReference();
                    if ("..".equals(reference)) {
                        map.put(name, map);
                    }
                    else {
                        this.addResolveTask(map, name, JSONPath.of(reference));
                    }
                }
                else {
                    final byte valueType = this.bytes[this.offset];
                    Object value;
                    if (valueType >= 73 && valueType <= 126) {
                        value = this.readString();
                    }
                    else if (valueType >= -16 && valueType <= 47) {
                        ++this.offset;
                        value = valueType;
                    }
                    else if (valueType == -79) {
                        ++this.offset;
                        value = Boolean.TRUE;
                    }
                    else if (valueType == -80) {
                        ++this.offset;
                        value = Boolean.FALSE;
                    }
                    else if (valueType == -90) {
                        value = this.readObject();
                    }
                    else if (valueType == -66) {
                        final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset + 1L);
                        this.offset += 9;
                        value = (JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
                    }
                    else if (valueType >= -108 && valueType <= -92) {
                        ++this.offset;
                        int len;
                        if (valueType == -92) {
                            final byte itemType = this.bytes[this.offset];
                            if (itemType >= -16 && itemType <= 47) {
                                ++this.offset;
                                len = itemType;
                            }
                            else {
                                len = this.readLength();
                            }
                        }
                        else {
                            len = valueType + 108;
                        }
                        if (len == 0) {
                            if ((this.context.features & Feature.UseNativeObject.mask) != 0x0L) {
                                value = new ArrayList();
                            }
                            else if (this.context.arraySupplier != null) {
                                value = this.context.arraySupplier.get();
                            }
                            else {
                                value = new JSONArray();
                            }
                        }
                        else {
                            List list;
                            if ((this.context.features & Feature.UseNativeObject.mask) != 0x0L) {
                                list = new ArrayList(len);
                            }
                            else {
                                list = new JSONArray(len);
                            }
                            for (int j = 0; j < len; ++j) {
                                if (this.isReference()) {
                                    final String reference2 = this.readReference();
                                    if ("..".equals(reference2)) {
                                        list.add(list);
                                    }
                                    else {
                                        list.add(null);
                                        this.addResolveTask(list, j, JSONPath.of(reference2));
                                    }
                                }
                                else {
                                    final byte itemType2 = this.bytes[this.offset];
                                    Object item;
                                    if (itemType2 >= 73 && itemType2 <= 126) {
                                        item = this.readString();
                                    }
                                    else if (itemType2 == -90) {
                                        item = this.readObject();
                                    }
                                    else {
                                        item = this.readAny();
                                    }
                                    list.add(item);
                                }
                            }
                            value = list;
                        }
                    }
                    else if (valueType >= 48 && valueType <= 63) {
                        value = (valueType - 56 << 8) + (this.bytes[this.offset + 1] & 0xFF);
                        this.offset += 2;
                    }
                    else if (valueType >= 64 && valueType <= 71) {
                        final int int32Value = getInt3(this.bytes, this.offset + 1, valueType);
                        this.offset += 3;
                        value = int32Value;
                    }
                    else if (valueType == 72) {
                        final int int32Value = getInt(this.bytes, this.offset + 1);
                        this.offset += 5;
                        value = int32Value;
                    }
                    else {
                        value = this.readAny();
                    }
                    map.put(name, value);
                }
                ++i;
            }
            ++this.offset;
            return (Map<String, Object>)map;
        }
        if (this.type == -110) {
            final ObjectReader objectReader = this.checkAutoType(Map.class, 0L, 0L);
            return objectReader.readObject(this, null, null, 0L);
        }
        throw new JSONException("object not support input " + this.error(this.type));
    }
    
    @Override
    public void read(final Map map, final long features) {
        if (this.bytes[this.offset] != -90) {
            throw new JSONException("object not support input " + this.error(this.type));
        }
        ++this.offset;
        int i = 0;
        while (true) {
            final byte type = this.bytes[this.offset];
            if (type == -91) {
                break;
            }
            Object name;
            if (type >= 73) {
                name = this.readFieldName();
            }
            else {
                name = this.readAny();
            }
            if (this.isReference()) {
                final String reference = this.readReference();
                if ("..".equals(reference)) {
                    map.put(name, map);
                }
                else {
                    this.addResolveTask(map, name, JSONPath.of(reference));
                    map.put(name, null);
                }
            }
            else {
                final byte valueType = this.bytes[this.offset];
                Object value;
                if (valueType >= 73 && valueType <= 126) {
                    value = this.readString();
                }
                else if (valueType >= -16 && valueType <= 47) {
                    ++this.offset;
                    value = valueType;
                }
                else if (valueType == -79) {
                    ++this.offset;
                    value = Boolean.TRUE;
                }
                else if (valueType == -80) {
                    ++this.offset;
                    value = Boolean.FALSE;
                }
                else if (valueType == -90) {
                    value = this.readObject();
                }
                else {
                    value = this.readAny();
                }
                map.put(name, value);
            }
            ++i;
        }
        ++this.offset;
    }
    
    @Override
    public Object readAny() {
        if (this.offset >= this.bytes.length) {
            throw new JSONException("readAny overflow : " + this.offset + "/" + this.bytes.length);
        }
        switch (this.type = this.bytes[this.offset++]) {
            case -81: {
                return null;
            }
            case -79: {
                return true;
            }
            case -80: {
                return false;
            }
            case -67: {
                return this.bytes[this.offset++];
            }
            case -68: {
                return (short)((this.bytes[this.offset++] << 8) + (this.bytes[this.offset++] & 0xFF));
            }
            case 72: {
                final int int32Value = getInt(this.bytes, this.offset);
                this.offset += 4;
                return int32Value;
            }
            case -65: {
                final int int32Value = getInt(this.bytes, this.offset);
                this.offset += 4;
                return int32Value;
            }
            case -66: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value);
            }
            case -69: {
                final int len = this.readInt32Value();
                final byte[] bytes = new byte[len];
                System.arraycopy(this.bytes, this.offset, bytes, 0, len);
                this.offset += len;
                return new BigInteger(bytes);
            }
            case -73: {
                final int int32Value = getInt(this.bytes, this.offset);
                this.offset += 4;
                return Float.intBitsToFloat(int32Value);
            }
            case -74: {
                return this.readInt32Value();
            }
            case -75: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return Double.longBitsToDouble(JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
            }
            case -76: {
                return this.readInt64Value();
            }
            case 122: {
                final int strlen = this.readLength();
                if (JDKUtils.STRING_CREATOR_JDK11 != null && !JDKUtils.BIG_ENDIAN) {
                    if (this.valueBytes == null) {
                        this.valueBytes = JSONFactory.BYTES_UPDATER.getAndSet(this.cacheItem, null);
                        if (this.valueBytes == null) {
                            this.valueBytes = new byte[8192];
                        }
                    }
                    final int minCapacity = strlen << 1;
                    if (minCapacity > this.valueBytes.length) {
                        this.valueBytes = new byte[minCapacity];
                    }
                    final int utf16_len = IOUtils.decodeUTF8(this.bytes, this.offset, strlen, this.valueBytes);
                    if (utf16_len != -1) {
                        final byte[] value = new byte[utf16_len];
                        System.arraycopy(this.valueBytes, 0, value, 0, utf16_len);
                        final String str = JDKUtils.STRING_CREATOR_JDK11.apply(value, JDKUtils.UTF16);
                        this.offset += strlen;
                        return str;
                    }
                }
                final String str2 = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_8);
                this.offset += strlen;
                return str2;
            }
            case 123: {
                final int strlen = this.readLength();
                final String str2 = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_16);
                this.offset += strlen;
                return str2;
            }
            case 124: {
                final int strlen = this.readLength();
                String str2;
                if (JDKUtils.STRING_CREATOR_JDK11 != null && !JDKUtils.BIG_ENDIAN) {
                    final byte[] chars = new byte[strlen];
                    System.arraycopy(this.bytes, this.offset, chars, 0, strlen);
                    str2 = JDKUtils.STRING_CREATOR_JDK11.apply(chars, (strlen == 0) ? JDKUtils.LATIN1 : JDKUtils.UTF16);
                }
                else {
                    str2 = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_16LE);
                }
                this.offset += strlen;
                return str2;
            }
            case 125: {
                final int strlen = this.readLength();
                String str2;
                if (JDKUtils.STRING_CREATOR_JDK11 != null && JDKUtils.BIG_ENDIAN) {
                    final byte[] chars = new byte[strlen];
                    System.arraycopy(this.bytes, this.offset, chars, 0, strlen);
                    str2 = JDKUtils.STRING_CREATOR_JDK11.apply(chars, (strlen == 0) ? JDKUtils.LATIN1 : JDKUtils.UTF16);
                }
                else {
                    str2 = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_16BE);
                }
                this.offset += strlen;
                return str2;
            }
            case 126: {
                if (JSONReaderJSONB.GB18030 == null) {
                    JSONReaderJSONB.GB18030 = Charset.forName("GB18030");
                }
                final int strlen = this.readLength();
                final String str2 = new String(this.bytes, this.offset, strlen, JSONReaderJSONB.GB18030);
                this.offset += strlen;
                return str2;
            }
            case -71: {
                final int scale = this.readInt32Value();
                final BigInteger unscaledValue = this.readBigInteger();
                BigDecimal decimal;
                if (scale == 0) {
                    decimal = new BigDecimal(unscaledValue);
                }
                else {
                    decimal = new BigDecimal(unscaledValue, scale);
                }
                return decimal;
            }
            case -72: {
                return BigDecimal.valueOf(this.readInt64Value());
            }
            case -111: {
                final int len = this.readLength();
                final byte[] binary = Arrays.copyOfRange(this.bytes, this.offset, this.offset + len);
                this.offset += len;
                return binary;
            }
            case -83: {
                final long minutes = getInt(this.bytes, this.offset);
                this.offset += 4;
                return new Date(minutes * 60L * 1000L);
            }
            case -84: {
                final long seconds = getInt(this.bytes, this.offset);
                this.offset += 4;
                return new Date(seconds * 1000L);
            }
            case -87: {
                final int year = (this.bytes[this.offset++] << 8) + (this.bytes[this.offset++] & 0xFF);
                final byte month = this.bytes[this.offset++];
                final byte dayOfMonth = this.bytes[this.offset++];
                return LocalDate.of(year, month, dayOfMonth);
            }
            case -89: {
                final byte hour = this.bytes[this.offset++];
                final byte minute = this.bytes[this.offset++];
                final byte second = this.bytes[this.offset++];
                final int nano = this.readInt32Value();
                return LocalTime.of(hour, minute, second, nano);
            }
            case -88: {
                final int year = (this.bytes[this.offset++] << 8) + (this.bytes[this.offset++] & 0xFF);
                final byte month = this.bytes[this.offset++];
                final byte dayOfMonth = this.bytes[this.offset++];
                final byte hour2 = this.bytes[this.offset++];
                final byte minute2 = this.bytes[this.offset++];
                final byte second2 = this.bytes[this.offset++];
                final int nano2 = this.readInt32Value();
                return LocalDateTime.of(year, month, dayOfMonth, hour2, minute2, second2, nano2);
            }
            case -86: {
                final int year = (this.bytes[this.offset++] << 8) + (this.bytes[this.offset++] & 0xFF);
                final byte month = this.bytes[this.offset++];
                final byte dayOfMonth = this.bytes[this.offset++];
                final byte hour2 = this.bytes[this.offset++];
                final byte minute2 = this.bytes[this.offset++];
                final byte second2 = this.bytes[this.offset++];
                final int nano2 = this.readInt32Value();
                final byte[] shanghaiZoneIdNameBytes = JSONReaderJSONB.SHANGHAI_ZONE_ID_NAME_BYTES;
                boolean shanghai;
                if (this.offset + shanghaiZoneIdNameBytes.length < this.bytes.length) {
                    shanghai = true;
                    for (int i = 0; i < shanghaiZoneIdNameBytes.length; ++i) {
                        if (this.bytes[this.offset + i] != shanghaiZoneIdNameBytes[i]) {
                            shanghai = false;
                            break;
                        }
                    }
                }
                else {
                    shanghai = false;
                }
                ZoneId zoneId;
                if (shanghai) {
                    this.offset += shanghaiZoneIdNameBytes.length;
                    zoneId = DateUtils.SHANGHAI_ZONE_ID;
                }
                else {
                    final String zoneIdStr = this.readString();
                    zoneId = DateUtils.getZoneId(zoneIdStr, DateUtils.SHANGHAI_ZONE_ID);
                }
                final LocalDateTime ldt = LocalDateTime.of(year, month, dayOfMonth, hour2, minute2, second2, nano2);
                return ZonedDateTime.of(ldt, zoneId);
            }
            case -82: {
                final long epochSeconds = this.readInt64Value();
                final int nano3 = this.readInt32Value();
                return Instant.ofEpochSecond(epochSeconds, nano3);
            }
            case -85: {
                final long millis = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return new Date(JDKUtils.BIG_ENDIAN ? millis : Long.reverseBytes(millis));
            }
            case -70: {
                return BigInteger.valueOf(this.readInt64Value());
            }
            case -110: {
                final long typeHash = this.readTypeHashCode();
                if (this.context.autoTypeBeforeHandler != null) {
                    Class<?> filterClass = this.context.autoTypeBeforeHandler.apply(typeHash, null, this.context.features);
                    if (filterClass == null) {
                        final String typeName = this.getString();
                        filterClass = this.context.autoTypeBeforeHandler.apply(typeName, null, this.context.features);
                    }
                    if (filterClass != null) {
                        final ObjectReader autoTypeObjectReader = this.context.getObjectReader(filterClass);
                        return autoTypeObjectReader.readJSONBObject(this, null, null, 0L);
                    }
                }
                final boolean supportAutoType = (this.context.features & Feature.SupportAutoType.mask) != 0x0L;
                if (supportAutoType) {
                    ObjectReader autoTypeObjectReader = this.context.getObjectReaderAutoType(typeHash);
                    if (autoTypeObjectReader == null) {
                        final String typeName2 = this.getString();
                        autoTypeObjectReader = this.context.getObjectReaderAutoType(typeName2, null);
                        if (autoTypeObjectReader == null) {
                            throw new JSONException("auoType not support : " + typeName2 + ", offset " + this.offset + "/" + this.bytes.length);
                        }
                    }
                    return autoTypeObjectReader.readJSONBObject(this, null, null, 0L);
                }
                if (this.isObject()) {
                    return this.readObject();
                }
                if (this.isArray()) {
                    return this.readArray();
                }
                throw new JSONException("auoType not support , offset " + this.offset + "/" + this.bytes.length);
            }
            case -78: {
                return 0.0;
            }
            case -77: {
                return 1.0;
            }
            case -112: {
                final int intValue = this.readInt32Value();
                return (char)intValue;
            }
            case -90: {
                Map map = null;
                final boolean supportAutoType = (this.context.features & Feature.SupportAutoType.mask) != 0x0L;
                int j = 0;
                while (true) {
                    final byte type = this.bytes[this.offset];
                    if (type == -91) {
                        ++this.offset;
                        if (map == null) {
                            if ((this.context.features & Feature.UseNativeObject.mask) != 0x0L) {
                                map = new HashMap();
                            }
                            else {
                                map = new JSONObject();
                            }
                        }
                        return map;
                    }
                    Object name;
                    if (supportAutoType && j == 0 && type >= 73) {
                        final long hash = this.readFieldNameHashCode();
                        if (hash == ObjectReader.HASH_TYPE) {
                            final long typeHash2 = this.readValueHashCode();
                            ObjectReader autoTypeObjectReader2 = this.context.getObjectReaderAutoType(typeHash2);
                            if (autoTypeObjectReader2 == null) {
                                final String typeName3 = this.getString();
                                autoTypeObjectReader2 = this.context.getObjectReaderAutoType(typeName3, null);
                                if (autoTypeObjectReader2 == null) {
                                    throw new JSONException("auotype not support : " + typeName3 + ", offset " + this.offset + "/" + this.bytes.length);
                                }
                            }
                            this.typeRedirect = true;
                            return autoTypeObjectReader2.readJSONBObject(this, null, null, 0L);
                        }
                        name = this.getFieldName();
                    }
                    else if (type >= 73) {
                        name = this.readFieldName();
                    }
                    else {
                        name = this.readAny();
                    }
                    if (map == null) {
                        if ((this.context.features & Feature.UseNativeObject.mask) != 0x0L) {
                            map = new HashMap();
                        }
                        else {
                            map = new JSONObject();
                        }
                    }
                    if (this.isReference()) {
                        final String reference = this.readReference();
                        if ("..".equals(reference)) {
                            map.put(name, map);
                        }
                        else {
                            this.addResolveTask(map, name, JSONPath.of(reference));
                            map.put(name, null);
                        }
                    }
                    else {
                        final byte valueType = this.bytes[this.offset];
                        Object value2;
                        if (valueType >= 73 && valueType <= 126) {
                            value2 = this.readString();
                        }
                        else if (valueType >= -16 && valueType <= 47) {
                            ++this.offset;
                            value2 = valueType;
                        }
                        else if (valueType == -79) {
                            ++this.offset;
                            value2 = Boolean.TRUE;
                        }
                        else if (valueType == -80) {
                            ++this.offset;
                            value2 = Boolean.FALSE;
                        }
                        else if (valueType == -90) {
                            value2 = this.readObject();
                        }
                        else {
                            value2 = this.readAny();
                        }
                        map.put(name, value2);
                    }
                    ++j;
                }
                break;
            }
            default: {
                if (this.type >= -16 && this.type <= 47) {
                    return this.type;
                }
                if (this.type >= 48 && this.type <= 63) {
                    return (this.type - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
                }
                if (this.type >= 64 && this.type <= 71) {
                    final int int3 = getInt3(this.bytes, this.offset, this.type);
                    this.offset += 2;
                    return int3;
                }
                if (this.type >= -40 && this.type <= -17) {
                    return -8L + (this.type + 40);
                }
                if (this.type >= -56 && this.type <= -41) {
                    return (this.type + 48 << 8) + (long)(this.bytes[this.offset++] & 0xFF);
                }
                if (this.type >= -64 && this.type <= -57) {
                    return (this.type + 60 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
                }
                if (this.type >= -108 && this.type <= -92) {
                    final int len2 = (this.type == -92) ? this.readLength() : (this.type + 108);
                    if (len2 != 0) {
                        List list;
                        if ((this.context.features & Feature.UseNativeObject.mask) != 0x0L) {
                            list = new ArrayList(len2);
                        }
                        else {
                            list = new JSONArray(len2);
                        }
                        for (int j = 0; j < len2; ++j) {
                            if (this.isReference()) {
                                final String reference2 = this.readReference();
                                if ("..".equals(reference2)) {
                                    list.add(list);
                                }
                                else {
                                    list.add(null);
                                    this.addResolveTask(list, j, JSONPath.of(reference2));
                                }
                            }
                            else {
                                final Object item = this.readAny();
                                list.add(item);
                            }
                        }
                        return list;
                    }
                    if ((this.context.features & Feature.UseNativeObject.mask) != 0x0L) {
                        return new ArrayList();
                    }
                    if (this.context.arraySupplier != null) {
                        return this.context.arraySupplier.get();
                    }
                    return new JSONArray();
                }
                else if (this.type >= 73 && this.type <= 121) {
                    this.strlen = ((this.type == 121) ? this.readLength() : (this.type - 73));
                    if (this.strlen < 0) {
                        return this.symbolTable.getName(-this.strlen);
                    }
                    if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                        final char[] chars2 = new char[this.strlen];
                        for (int k = 0; k < this.strlen; ++k) {
                            chars2[k] = (char)(this.bytes[this.offset + k] & 0xFF);
                        }
                        this.offset += this.strlen;
                        String str3 = JDKUtils.STRING_CREATOR_JDK8.apply(chars2, Boolean.TRUE);
                        if ((this.context.features & Feature.TrimString.mask) != 0x0L) {
                            str3 = str3.trim();
                        }
                        return str3;
                    }
                    if (JDKUtils.STRING_CREATOR_JDK11 != null) {
                        final byte[] chars3 = new byte[this.strlen];
                        System.arraycopy(this.bytes, this.offset, chars3, 0, this.strlen);
                        this.offset += this.strlen;
                        String str3 = JDKUtils.STRING_CREATOR_JDK11.apply(chars3, JDKUtils.LATIN1);
                        if ((this.context.features & Feature.TrimString.mask) != 0x0L) {
                            str3 = str3.trim();
                        }
                        return str3;
                    }
                    String str2 = new String(this.bytes, this.offset, this.strlen, StandardCharsets.ISO_8859_1);
                    this.offset += this.strlen;
                    if ((this.context.features & Feature.TrimString.mask) != 0x0L) {
                        str2 = str2.trim();
                    }
                    return str2;
                }
                else {
                    if (this.type != 127) {
                        throw new JSONException("not support type : " + this.error(this.type));
                    }
                    this.strlen = this.readLength();
                    if (this.strlen >= 0) {
                        throw new JSONException("not support symbol : " + this.strlen);
                    }
                    return this.symbolTable.getName(-this.strlen);
                }
                break;
            }
        }
    }
    
    @Override
    public byte getType() {
        return this.bytes[this.offset];
    }
    
    @Override
    public List readArray() {
        final int entryCnt = this.startArray();
        final JSONArray array = new JSONArray(entryCnt);
        for (int i = 0; i < entryCnt; ++i) {
            final byte valueType = this.bytes[this.offset];
            Object value;
            if (valueType >= 73 && valueType <= 126) {
                value = this.readString();
            }
            else if (valueType >= -16 && valueType <= 47) {
                ++this.offset;
                value = valueType;
            }
            else if (valueType == -79) {
                ++this.offset;
                value = Boolean.TRUE;
            }
            else if (valueType == -80) {
                ++this.offset;
                value = Boolean.FALSE;
            }
            else if (valueType == -90) {
                value = this.readObject();
            }
            else if (valueType == -66) {
                ++this.offset;
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                value = (JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
            }
            else if (valueType >= -108 && valueType <= -92) {
                ++this.offset;
                final int len = (valueType == -92) ? this.readLength() : (valueType + 108);
                if (len == 0) {
                    if ((this.context.features & Feature.UseNativeObject.mask) != 0x0L) {
                        value = new ArrayList();
                    }
                    else if (this.context.arraySupplier != null) {
                        value = this.context.arraySupplier.get();
                    }
                    else {
                        value = new JSONArray();
                    }
                }
                else {
                    List list;
                    if ((this.context.features & Feature.UseNativeObject.mask) != 0x0L) {
                        list = new ArrayList(len);
                    }
                    else {
                        list = new JSONArray(len);
                    }
                    for (int j = 0; j < len; ++j) {
                        if (this.isReference()) {
                            final String reference = this.readReference();
                            if ("..".equals(reference)) {
                                list.add(list);
                            }
                            else {
                                list.add(null);
                                this.addResolveTask(list, j, JSONPath.of(reference));
                            }
                        }
                        else {
                            final byte itemType = this.bytes[this.offset];
                            Object item;
                            if (itemType >= 73 && itemType <= 126) {
                                item = this.readString();
                            }
                            else if (itemType == -90) {
                                item = this.readObject();
                            }
                            else {
                                item = this.readAny();
                            }
                            list.add(item);
                        }
                    }
                    value = list;
                }
            }
            else if (valueType >= 48 && valueType <= 63) {
                value = (valueType - 56 << 8) + (this.bytes[this.offset + 1] & 0xFF);
                this.offset += 2;
            }
            else if (valueType >= 64 && valueType <= 71) {
                final int int3 = getInt3(this.bytes, this.offset + 1, valueType);
                this.offset += 3;
                value = int3;
            }
            else if (valueType == 72) {
                final int int32Value = JDKUtils.UNSAFE.getInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 5;
                value = (JDKUtils.BIG_ENDIAN ? int32Value : Integer.reverseBytes(int32Value));
            }
            else if (valueType == -109) {
                final String reference2 = this.readReference();
                if (!"..".equals(reference2)) {
                    this.addResolveTask(array, i, JSONPath.of(reference2));
                    continue;
                }
                value = array;
            }
            else {
                value = this.readAny();
            }
            array.add(value);
        }
        return array;
    }
    
    @Override
    public List readArray(final Type itemType) {
        if (this.nextIfNull()) {
            return null;
        }
        if (this.bytes[this.offset] != -110) {
            final int entryCnt = this.startArray();
            final JSONArray array = new JSONArray(entryCnt);
            for (int i = 0; i < entryCnt; ++i) {
                array.add(this.read(itemType));
            }
            return array;
        }
        final Object obj = this.readAny();
        if (obj instanceof List) {
            return (List)obj;
        }
        if (obj instanceof Collection) {
            return new JSONArray((Collection<?>)obj);
        }
        throw new JSONException("not support class " + obj.getClass());
    }
    
    @Override
    public List readList(final Type[] types) {
        if (this.nextIfNull()) {
            return null;
        }
        final int entryCnt = this.startArray();
        final JSONArray array = new JSONArray(entryCnt);
        for (final Type itemType : types) {
            array.add(this.read(itemType));
        }
        return array;
    }
    
    @Override
    public byte[] readHex() {
        final String str = this.readString();
        final byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < bytes.length; ++i) {
            final char c0 = str.charAt(i * 2);
            final char c2 = str.charAt(i * 2 + 1);
            final int b0 = c0 - ((c0 <= '9') ? '0' : '7');
            final int b2 = c2 - ((c2 <= '9') ? '0' : '7');
            bytes[i] = (byte)(b0 << 4 | b2);
        }
        return bytes;
    }
    
    @Override
    public boolean isReference() {
        return this.offset < this.bytes.length && this.bytes[this.offset] == -109;
    }
    
    @Override
    public String readReference() {
        if (this.bytes[this.offset] != -109) {
            return null;
        }
        ++this.offset;
        if (this.isString()) {
            return this.readString();
        }
        throw new JSONException("reference not support input " + this.error(this.type));
    }
    
    Object readAnyObject() {
        if (this.bytes[this.offset] != -110) {
            return this.readAny();
        }
        final Context context = this.context;
        ++this.offset;
        final long typeHash = this.readTypeHashCode();
        ObjectReader autoTypeObjectReader = null;
        final AutoTypeBeforeHandler autoTypeBeforeHandler = context.autoTypeBeforeHandler;
        if (autoTypeBeforeHandler != null) {
            Class<?> objectClass = autoTypeBeforeHandler.apply(typeHash, Object.class, 0L);
            if (objectClass == null) {
                objectClass = autoTypeBeforeHandler.apply(this.getString(), Object.class, 0L);
            }
            if (objectClass != null) {
                autoTypeObjectReader = context.getObjectReader(objectClass);
            }
        }
        final long features = context.features;
        if (autoTypeObjectReader == null) {
            if ((features & Feature.SupportAutoType.mask) == 0x0L) {
                if ((features & Feature.ErrorOnNotSupportAutoType.mask) == 0x0L) {
                    return null;
                }
                this.autoTypeError();
            }
            autoTypeObjectReader = context.provider.getObjectReader(typeHash);
        }
        if (autoTypeObjectReader != null) {
            final Class objectClass2 = autoTypeObjectReader.getObjectClass();
            if (objectClass2 != null) {
                final ClassLoader classLoader = objectClass2.getClassLoader();
                if (classLoader != null) {
                    final ClassLoader tcl = Thread.currentThread().getContextClassLoader();
                    if (classLoader != tcl) {
                        autoTypeObjectReader = this.getObjectReaderContext(autoTypeObjectReader, objectClass2, tcl);
                    }
                }
            }
        }
        if (autoTypeObjectReader == null) {
            autoTypeObjectReader = context.provider.getObjectReader(this.getString(), Object.class, features);
            if (autoTypeObjectReader == null) {
                if ((features & Feature.ErrorOnNotSupportAutoType.mask) == 0x0L) {
                    return null;
                }
                this.autoTypeError();
            }
        }
        this.type = this.bytes[this.offset];
        return autoTypeObjectReader.readJSONBObject(this, Object.class, null, context.features);
    }
    
    @Override
    public ObjectReader checkAutoType(final Class expectClass, final long expectClassHash, final long features) {
        ObjectReader autoTypeObjectReader = null;
        if (this.bytes[this.offset] == -110) {
            ++this.offset;
            final Context context = this.context;
            final long typeHash = this.readTypeHashCode();
            if (expectClassHash == typeHash) {
                final ObjectReader objectReader = context.getObjectReader(expectClass);
                final Class objectClass = objectReader.getObjectClass();
                if (objectClass != null && objectClass == expectClass) {
                    context.provider.registerIfAbsent(typeHash, objectReader);
                    return objectReader;
                }
            }
            final AutoTypeBeforeHandler autoTypeBeforeHandler = context.autoTypeBeforeHandler;
            if (autoTypeBeforeHandler != null) {
                final ObjectReader objectReader2 = this.checkAutoTypeWithHandler(expectClass, features, autoTypeBeforeHandler, typeHash);
                if (objectReader2 != null) {
                    return objectReader2;
                }
            }
            final long features2 = context.features | features;
            final boolean isSupportAutoType = (features2 & Feature.SupportAutoType.mask) != 0x0L;
            if (!isSupportAutoType) {
                if ((features2 & Feature.ErrorOnNotSupportAutoType.mask) == 0x0L) {
                    return null;
                }
                this.autoTypeError();
            }
            autoTypeObjectReader = context.provider.getObjectReader(typeHash);
            if (autoTypeObjectReader != null) {
                final Class objectClass2 = autoTypeObjectReader.getObjectClass();
                if (objectClass2 != null) {
                    final ClassLoader classLoader = objectClass2.getClassLoader();
                    if (classLoader != null) {
                        final ClassLoader tcl = Thread.currentThread().getContextClassLoader();
                        if (classLoader != tcl) {
                            autoTypeObjectReader = this.getObjectReaderContext(autoTypeObjectReader, objectClass2, tcl);
                        }
                    }
                }
            }
            if (autoTypeObjectReader == null) {
                autoTypeObjectReader = context.provider.getObjectReader(this.getString(), expectClass, features2);
                if (autoTypeObjectReader == null) {
                    if ((features2 & Feature.ErrorOnNotSupportAutoType.mask) == 0x0L) {
                        return null;
                    }
                    this.autoTypeError();
                }
            }
            this.type = this.bytes[this.offset];
        }
        return autoTypeObjectReader;
    }
    
    ObjectReader checkAutoTypeWithHandler(final Class expectClass, final long features, final AutoTypeBeforeHandler autoTypeBeforeHandler, final long typeHash) {
        Class<?> objectClass = autoTypeBeforeHandler.apply(typeHash, expectClass, features);
        if (objectClass == null) {
            objectClass = autoTypeBeforeHandler.apply(this.getString(), expectClass, features);
        }
        if (objectClass != null) {
            final ObjectReader objectReader = this.context.getObjectReader(objectClass);
            if (objectReader != null) {
                return objectReader;
            }
        }
        return null;
    }
    
    void autoTypeError() {
        throw new JSONException("auotype not support : " + this.getString());
    }
    
    private ObjectReader getObjectReaderContext(ObjectReader autoTypeObjectReader, final Class objectClass, ClassLoader contextClassLoader) {
        final String typeName = this.getString();
        Class contextClass = TypeUtils.getMapping(typeName);
        if (contextClass == null) {
            try {
                if (contextClassLoader == null) {
                    contextClassLoader = JSON.class.getClassLoader();
                }
                contextClass = contextClassLoader.loadClass(typeName);
            }
            catch (ClassNotFoundException ex) {}
        }
        if (contextClass != null && !objectClass.equals(contextClass)) {
            autoTypeObjectReader = this.getObjectReader(contextClass);
        }
        return autoTypeObjectReader;
    }
    
    @Override
    public int startArray() {
        final byte type2 = this.bytes[this.offset++];
        this.type = type2;
        final byte type = type2;
        if (type == -81) {
            return -1;
        }
        if (type >= -108 && type <= -93) {
            this.ch = (char)(-type);
            return type + 108;
        }
        if (type == -111) {
            return this.readInt32Value();
        }
        if (type != -92) {
            throw new JSONException("array not support input " + this.error(type));
        }
        return this.readInt32Value();
    }
    
    public String error(final byte type) {
        final StringBuilder buf = new StringBuilder();
        buf.append(JSONB.typeName(type));
        if (this.isString()) {
            final int mark = this.offset;
            --this.offset;
            String str = null;
            try {
                str = this.readString();
            }
            catch (Throwable t) {}
            if (str != null) {
                buf.append(' ');
                buf.append(str);
            }
            this.offset = mark;
        }
        buf.append(", offset ");
        buf.append(this.offset);
        buf.append('/');
        buf.append(this.bytes.length);
        return buf.toString();
    }
    
    @Override
    public void next() {
        ++this.offset;
    }
    
    @Override
    public long readFieldNameHashCode() {
        this.strtype = this.bytes[this.offset++];
        final boolean typeSymbol = this.strtype == 127;
        if (typeSymbol) {
            this.strtype = this.bytes[this.offset];
            if (this.strtype >= -16 && this.strtype <= 72) {
                int symbol;
                if (this.strtype <= 47) {
                    ++this.offset;
                    symbol = this.strtype;
                }
                else {
                    symbol = this.readInt32Value();
                }
                if (symbol < 0) {
                    return this.symbolTable.getHashCode(-symbol);
                }
                if (symbol == 0) {
                    this.strtype = this.symbol0StrType;
                    this.strlen = this.symbol0Length;
                    this.strBegin = this.symbol0Begin;
                    if (this.symbol0Hash == 0L) {
                        this.symbol0Hash = this.getNameHashCode();
                    }
                    return this.symbol0Hash;
                }
                final int index = symbol * 2;
                final long strInfo = this.symbols[index + 1];
                this.strtype = (byte)strInfo;
                this.strlen = (int)strInfo >> 8;
                this.strBegin = (int)(strInfo >> 32);
                long nameHashCode = this.symbols[index];
                if (nameHashCode == 0L) {
                    nameHashCode = this.getNameHashCode();
                    this.symbols[index] = nameHashCode;
                }
                return nameHashCode;
            }
            else {
                ++this.offset;
            }
        }
        this.strBegin = this.offset;
        if (this.strtype >= 73 && this.strtype <= 120) {
            this.strlen = this.strtype - 73;
        }
        else {
            if (this.strtype != 121 && this.strtype != 122) {
                throw this.readFieldNameHashCodeEror();
            }
            this.strlen = this.readLength();
            this.strBegin = this.offset;
        }
        long hashCode;
        if (this.strlen < 0) {
            hashCode = this.symbolTable.getHashCode(-this.strlen);
        }
        else {
            long nameValue = 0L;
            if (this.strlen <= 8 && this.offset + this.strlen <= this.bytes.length) {
                switch (this.strlen) {
                    case 1: {
                        nameValue = this.bytes[this.offset];
                        break;
                    }
                    case 2: {
                        nameValue = ((long)JDKUtils.UNSAFE.getShort(this.bytes, JSONReaderJSONB.BASE + this.offset) & 0xFFFFL);
                        break;
                    }
                    case 3: {
                        nameValue = (this.bytes[this.offset + 2] << 16) + ((long)JDKUtils.UNSAFE.getShort(this.bytes, JSONReaderJSONB.BASE + this.offset) & 0xFFFFL);
                        break;
                    }
                    case 4: {
                        nameValue = JDKUtils.UNSAFE.getInt(this.bytes, JSONReaderJSONB.BASE + this.offset);
                        break;
                    }
                    case 5: {
                        nameValue = ((long)this.bytes[this.offset + 4] << 32) + ((long)JDKUtils.UNSAFE.getInt(this.bytes, JSONReaderJSONB.BASE + this.offset) & 0xFFFFFFFFL);
                        break;
                    }
                    case 6: {
                        nameValue = ((long)JDKUtils.UNSAFE.getShort(this.bytes, JSONReaderJSONB.BASE + this.offset + 4L) << 32) + ((long)JDKUtils.UNSAFE.getInt(this.bytes, JSONReaderJSONB.BASE + this.offset) & 0xFFFFFFFFL);
                        break;
                    }
                    case 7: {
                        nameValue = ((long)this.bytes[this.offset + 6] << 48) + (((long)this.bytes[this.offset + 5] & 0xFFL) << 40) + (((long)this.bytes[this.offset + 4] & 0xFFL) << 32) + ((long)JDKUtils.UNSAFE.getInt(this.bytes, JSONReaderJSONB.BASE + this.offset) & 0xFFFFFFFFL);
                        break;
                    }
                    case 8: {
                        nameValue = JDKUtils.UNSAFE.getLong(this.bytes, JSONReaderJSONB.BASE + this.offset);
                        break;
                    }
                }
            }
            if (nameValue != 0L) {
                this.offset += this.strlen;
                hashCode = nameValue;
            }
            else {
                hashCode = -3750763034362895579L;
                for (int i = 0; i < this.strlen; ++i) {
                    final byte c = this.bytes[this.offset++];
                    hashCode ^= c;
                    hashCode *= 1099511628211L;
                }
            }
        }
        if (typeSymbol) {
            final byte type = this.bytes[this.offset];
            this.type = type;
            int symbol2;
            if (type >= -16 && this.type <= 47) {
                symbol2 = this.type;
                ++this.offset;
            }
            else {
                symbol2 = this.readInt32Value();
            }
            if (symbol2 == 0) {
                this.symbol0Begin = this.strBegin;
                this.symbol0Length = this.strlen;
                this.symbol0StrType = this.strtype;
                return this.symbol0Hash = hashCode;
            }
            final long strInfo2 = ((long)this.strBegin << 32) + ((long)this.strlen << 8) + this.strtype;
            final int symbolIndex = symbol2 << 1;
            final int minCapacity = symbolIndex + 2;
            if (this.symbols == null) {
                this.symbols = new long[Math.max(minCapacity, 32)];
            }
            else if (this.symbols.length < minCapacity) {
                this.symbols = Arrays.copyOf(this.symbols, minCapacity + 16);
            }
            this.symbols[symbolIndex] = hashCode;
            this.symbols[symbolIndex + 1] = strInfo2;
        }
        return hashCode;
    }
    
    protected JSONException readFieldNameHashCodeEror() {
        final StringBuilder message = new StringBuilder().append("fieldName not support input type ").append(JSONB.typeName(this.strtype));
        if (this.strtype == -109) {
            message.append(" ").append(this.readString());
        }
        message.append(", offset ").append(this.offset);
        final JSONException error = new JSONException(message.toString());
        return error;
    }
    
    @Override
    public boolean isInt() {
        final int type = this.bytes[this.offset];
        return (type >= -70 && type <= 72) || type == -84 || type == -83 || type == -85;
    }
    
    @Override
    public boolean isNull() {
        return this.bytes[this.offset] == -81;
    }
    
    @Override
    public Date readNullOrNewDate() {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public boolean nextIfNull() {
        if (this.bytes[this.offset] == -81) {
            ++this.offset;
            return true;
        }
        return false;
    }
    
    @Override
    public void readNull() {
        this.type = this.bytes[this.offset++];
        if (this.type != -81) {
            throw new JSONException("null not match, " + this.type);
        }
    }
    
    @Override
    public boolean readIfNull() {
        if (this.bytes[this.offset] == -81) {
            ++this.offset;
            return true;
        }
        return false;
    }
    
    @Override
    public long readTypeHashCode() {
        this.strtype = this.bytes[this.offset];
        final byte[] bytes = this.bytes;
        final boolean typeSymbol = this.strtype == 127;
        if (typeSymbol) {
            ++this.offset;
            this.strtype = bytes[this.offset];
            if (this.strtype >= -16 && this.strtype <= 72) {
                int symbol;
                if (this.strtype <= 47) {
                    ++this.offset;
                    symbol = this.strtype;
                }
                else {
                    symbol = this.readInt32Value();
                }
                if (symbol < 0) {
                    return this.symbolTable.getHashCode(-symbol);
                }
                if (symbol == 0) {
                    this.strtype = this.symbol0StrType;
                    this.strlen = this.symbol0Length;
                    this.strBegin = this.symbol0Begin;
                    if (this.symbol0Hash == 0L) {
                        this.symbol0Hash = this.getNameHashCode();
                    }
                    return this.symbol0Hash;
                }
                final int index = symbol * 2;
                final long strInfo = this.symbols[index + 1];
                this.strtype = (byte)strInfo;
                this.strlen = (int)strInfo >> 8;
                this.strBegin = (int)(strInfo >> 32);
                long nameHashCode = this.symbols[index];
                if (nameHashCode == 0L) {
                    nameHashCode = this.getNameHashCode();
                    this.symbols[index] = nameHashCode;
                }
                return nameHashCode;
            }
        }
        if (this.strtype < -16 || this.strtype > 72) {
            ++this.offset;
            this.strBegin = this.offset;
            if (this.strtype >= 73 && this.strtype <= 120) {
                this.strlen = this.strtype - 73;
            }
            else {
                if (this.strtype != 121 && this.strtype != 122 && this.strtype != 123 && this.strtype != 124 && this.strtype != 125) {
                    throw new JSONException("string value not support input " + JSONB.typeName(this.type) + " offset " + this.offset + "/" + bytes.length);
                }
                final byte strType = bytes[this.offset];
                if (strType >= -16 && strType <= 47) {
                    ++this.offset;
                    this.strlen = strType;
                }
                else if (strType >= 48 && strType <= 63) {
                    ++this.offset;
                    this.strlen = (strType - 56 << 8) + (bytes[this.offset++] & 0xFF);
                }
                else {
                    this.strlen = this.readLength();
                }
                this.strBegin = this.offset;
            }
            long hashCode;
            if (this.strlen < 0) {
                hashCode = this.symbolTable.getHashCode(-this.strlen);
            }
            else if (this.strtype == 122) {
                hashCode = -3750763034362895579L;
                final int end = this.offset + this.strlen;
                while (this.offset < end) {
                    int c = bytes[this.offset];
                    if (c >= 0) {
                        ++this.offset;
                    }
                    else {
                        c &= 0xFF;
                        switch (c >> 4) {
                            case 12:
                            case 13: {
                                final int c2 = bytes[this.offset + 1];
                                if ((c2 & 0xC0) != 0x80) {
                                    throw new JSONException("malformed input around byte " + this.offset);
                                }
                                c = (char)((c & 0x1F) << 6 | (c2 & 0x3F));
                                this.offset += 2;
                                break;
                            }
                            case 14: {
                                final int c2 = bytes[this.offset + 1];
                                final int c3 = bytes[this.offset + 2];
                                if ((c2 & 0xC0) != 0x80 || (c3 & 0xC0) != 0x80) {
                                    throw new JSONException("malformed input around byte " + this.offset);
                                }
                                c = (char)((c & 0xF) << 12 | (c2 & 0x3F) << 6 | (c3 & 0x3F));
                                this.offset += 3;
                                break;
                            }
                            default: {
                                throw new JSONException("malformed input around byte " + this.offset);
                            }
                        }
                    }
                    hashCode ^= c;
                    hashCode *= 1099511628211L;
                }
            }
            else if (this.strtype == 123 || this.strtype == 125) {
                hashCode = -3750763034362895579L;
                for (int i = 0; i < this.strlen; i += 2) {
                    final byte c4 = bytes[this.offset + i];
                    final byte c5 = bytes[this.offset + i + 1];
                    final char ch = (char)((c5 & 0xFF) | (c4 & 0xFF) << 8);
                    hashCode ^= ch;
                    hashCode *= 1099511628211L;
                }
            }
            else if (this.strtype == 124) {
                hashCode = -3750763034362895579L;
                for (int i = 0; i < this.strlen; i += 2) {
                    final byte c4 = bytes[this.offset + i];
                    final byte c5 = bytes[this.offset + i + 1];
                    final char ch = (char)((c4 & 0xFF) | (c5 & 0xFF) << 8);
                    hashCode ^= ch;
                    hashCode *= 1099511628211L;
                }
            }
            else {
                long nameValue = 0L;
                if (this.strlen <= 8) {
                    int j = 0;
                    final int start = this.offset;
                    while (j < this.strlen) {
                        final byte c6 = bytes[this.offset];
                        if (c6 < 0 || (c6 == 0 && bytes[start] == 0)) {
                            nameValue = 0L;
                            this.offset = start;
                            break;
                        }
                        switch (j) {
                            case 0: {
                                nameValue = c6;
                                break;
                            }
                            case 1: {
                                nameValue = (c6 << 8) + (nameValue & 0xFFL);
                                break;
                            }
                            case 2: {
                                nameValue = (c6 << 16) + (nameValue & 0xFFFFL);
                                break;
                            }
                            case 3: {
                                nameValue = (c6 << 24) + (nameValue & 0xFFFFFFL);
                                break;
                            }
                            case 4: {
                                nameValue = ((long)c6 << 32) + (nameValue & 0xFFFFFFFFL);
                                break;
                            }
                            case 5: {
                                nameValue = ((long)c6 << 40) + (nameValue & 0xFFFFFFFFFFL);
                                break;
                            }
                            case 6: {
                                nameValue = ((long)c6 << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                                break;
                            }
                            case 7: {
                                nameValue = ((long)c6 << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                                break;
                            }
                        }
                        ++this.offset;
                        ++j;
                    }
                }
                if (nameValue != 0L) {
                    hashCode = nameValue;
                }
                else {
                    hashCode = -3750763034362895579L;
                    for (int j = 0; j < this.strlen; ++j) {
                        final byte c7 = bytes[this.offset++];
                        hashCode ^= c7;
                        hashCode *= 1099511628211L;
                    }
                }
            }
            final byte type = bytes[this.offset];
            this.type = type;
            int symbol2;
            if (type >= -16 && this.type <= 47) {
                symbol2 = this.type;
                ++this.offset;
            }
            else {
                symbol2 = this.readInt32Value();
            }
            if (symbol2 == 0) {
                this.symbol0Begin = this.strBegin;
                this.symbol0Length = this.strlen;
                this.symbol0StrType = this.strtype;
                this.symbol0Hash = hashCode;
            }
            else {
                final int minCapacity = symbol2 * 2 + 2;
                if (this.symbols == null) {
                    this.symbols = new long[Math.max(minCapacity, 32)];
                }
                else if (this.symbols.length < minCapacity) {
                    this.symbols = Arrays.copyOf(this.symbols, minCapacity + 16);
                }
                final long strInfo2 = ((long)this.strBegin << 32) + ((long)this.strlen << 8) + this.strtype;
                this.symbols[symbol2 * 2 + 1] = strInfo2;
            }
            return hashCode;
        }
        int typeIndex;
        if (this.strtype <= 47) {
            ++this.offset;
            typeIndex = this.strtype;
        }
        else if (this.strtype <= 63) {
            ++this.offset;
            typeIndex = (this.strtype - 56 << 8) + (bytes[this.offset++] & 0xFF);
        }
        else {
            typeIndex = this.readInt32Value();
        }
        long refTypeHash;
        if (typeIndex == 0) {
            this.strtype = this.symbol0StrType;
            this.strlen = this.symbol0Length;
            this.strBegin = this.symbol0Begin;
            if (this.symbol0Hash == 0L) {
                this.symbol0Hash = Fnv.hashCode64(this.getString());
            }
            refTypeHash = this.symbol0Hash;
        }
        else if (typeIndex < 0) {
            this.strlen = this.strtype;
            refTypeHash = this.symbolTable.getHashCode(-typeIndex);
        }
        else {
            refTypeHash = this.symbols[typeIndex * 2];
            if (refTypeHash == 0L) {
                final long strInfo3 = this.symbols[typeIndex * 2 + 1];
                this.strtype = (byte)strInfo3;
                this.strlen = (int)strInfo3 >> 8;
                this.strBegin = (int)(strInfo3 >> 32);
                refTypeHash = Fnv.hashCode64(this.getString());
            }
        }
        if (refTypeHash == -1L) {
            throw new JSONException("type ref not found : " + typeIndex);
        }
        return refTypeHash;
    }
    
    @Override
    public long readValueHashCode() {
        this.strtype = this.bytes[this.offset];
        ++this.offset;
        this.strBegin = this.offset;
        if (this.strtype >= 73 && this.strtype <= 120) {
            this.strlen = this.strtype - 73;
        }
        else if (this.strtype == 121 || this.strtype == 122 || this.strtype == 123 || this.strtype == 124 || this.strtype == 125) {
            this.strlen = this.readLength();
            this.strBegin = this.offset;
        }
        else {
            if (this.strtype != 127) {
                throw new JSONException("string value not support input " + JSONB.typeName(this.type) + " offset " + this.offset + "/" + this.bytes.length);
            }
            this.strlen = this.readLength();
            this.strBegin = this.offset;
        }
        long hashCode;
        if (this.strlen < 0) {
            hashCode = this.symbolTable.getHashCode(-this.strlen);
        }
        else if (this.strtype == 122) {
            hashCode = -3750763034362895579L;
            final int end = this.offset + this.strlen;
            while (this.offset < end) {
                int c = this.bytes[this.offset];
                if (c >= 0) {
                    ++this.offset;
                }
                else {
                    c &= 0xFF;
                    switch (c >> 4) {
                        case 12:
                        case 13: {
                            final int c2 = this.bytes[this.offset + 1];
                            if ((c2 & 0xC0) != 0x80) {
                                throw new JSONException("malformed input around byte " + this.offset);
                            }
                            c = (char)((c & 0x1F) << 6 | (c2 & 0x3F));
                            this.offset += 2;
                            break;
                        }
                        case 14: {
                            final int c2 = this.bytes[this.offset + 1];
                            final int c3 = this.bytes[this.offset + 2];
                            if ((c2 & 0xC0) != 0x80 || (c3 & 0xC0) != 0x80) {
                                throw new JSONException("malformed input around byte " + this.offset);
                            }
                            c = (char)((c & 0xF) << 12 | (c2 & 0x3F) << 6 | (c3 & 0x3F));
                            this.offset += 3;
                            break;
                        }
                        default: {
                            throw new JSONException("malformed input around byte " + this.offset);
                        }
                    }
                }
                hashCode ^= c;
                hashCode *= 1099511628211L;
            }
        }
        else if (this.strtype == 123) {
            hashCode = -3750763034362895579L;
            if (this.bytes[this.offset] == -2 && this.bytes[this.offset + 1] == -1) {
                if (this.strlen <= 16) {
                    long nameValue = 0L;
                    for (int i = 2; i < this.strlen; i += 2) {
                        final byte c4 = this.bytes[this.offset + i];
                        final byte c5 = this.bytes[this.offset + i + 1];
                        final char ch = (char)((c5 & 0xFF) | (c4 & 0xFF) << 8);
                        if (ch > '\u007f' || (i == 0 && ch == '\0')) {
                            nameValue = 0L;
                            break;
                        }
                        final byte c6 = (byte)ch;
                        switch (i - 2 >> 1) {
                            case 0: {
                                nameValue = c6;
                                break;
                            }
                            case 1: {
                                nameValue = (c6 << 8) + (nameValue & 0xFFL);
                                break;
                            }
                            case 2: {
                                nameValue = (c6 << 16) + (nameValue & 0xFFFFL);
                                break;
                            }
                            case 3: {
                                nameValue = (c6 << 24) + (nameValue & 0xFFFFFFL);
                                break;
                            }
                            case 4: {
                                nameValue = ((long)c6 << 32) + (nameValue & 0xFFFFFFFFL);
                                break;
                            }
                            case 5: {
                                nameValue = ((long)c6 << 40) + (nameValue & 0xFFFFFFFFFFL);
                                break;
                            }
                            case 6: {
                                nameValue = ((long)c6 << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                                break;
                            }
                            case 7: {
                                nameValue = ((long)c6 << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                                break;
                            }
                        }
                    }
                    if (nameValue != 0L) {
                        return nameValue;
                    }
                }
                for (int j = 2; j < this.strlen; j += 2) {
                    final byte c7 = this.bytes[this.offset + j];
                    final byte c8 = this.bytes[this.offset + j + 1];
                    final char ch2 = (char)((c8 & 0xFF) | (c7 & 0xFF) << 8);
                    hashCode ^= ch2;
                    hashCode *= 1099511628211L;
                }
            }
            else if (this.bytes[this.offset] == -1 && this.bytes[this.offset + 1] == -2) {
                for (int j = 2; j < this.strlen; j += 2) {
                    final byte c9 = this.bytes[this.offset + j];
                    final byte c10 = this.bytes[this.offset + j + 1];
                    final char ch2 = (char)((c9 & 0xFF) | (c10 & 0xFF) << 8);
                    hashCode ^= ch2;
                    hashCode *= 1099511628211L;
                }
            }
            else {
                for (int j = 0; j < this.strlen; j += 2) {
                    final byte c7 = this.bytes[this.offset + j];
                    final byte c8 = this.bytes[this.offset + j + 1];
                    final char ch2 = (char)((c7 & 0xFF) | (c8 & 0xFF) << 8);
                    hashCode ^= ch2;
                    hashCode *= 1099511628211L;
                }
            }
        }
        else if (this.strtype == 125) {
            if (this.strlen <= 16) {
                long nameValue = 0L;
                for (int i = 0; i < this.strlen; i += 2) {
                    final byte c4 = this.bytes[this.offset + i];
                    final byte c5 = this.bytes[this.offset + i + 1];
                    final char ch = (char)((c5 & 0xFF) | (c4 & 0xFF) << 8);
                    if (ch > '\u007f' || (i == 0 && ch == '\0')) {
                        nameValue = 0L;
                        break;
                    }
                    final byte c6 = (byte)ch;
                    switch (i >> 1) {
                        case 0: {
                            nameValue = c6;
                            break;
                        }
                        case 1: {
                            nameValue = (c6 << 8) + (nameValue & 0xFFL);
                            break;
                        }
                        case 2: {
                            nameValue = (c6 << 16) + (nameValue & 0xFFFFL);
                            break;
                        }
                        case 3: {
                            nameValue = (c6 << 24) + (nameValue & 0xFFFFFFL);
                            break;
                        }
                        case 4: {
                            nameValue = ((long)c6 << 32) + (nameValue & 0xFFFFFFFFL);
                            break;
                        }
                        case 5: {
                            nameValue = ((long)c6 << 40) + (nameValue & 0xFFFFFFFFFFL);
                            break;
                        }
                        case 6: {
                            nameValue = ((long)c6 << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                            break;
                        }
                        case 7: {
                            nameValue = ((long)c6 << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                            break;
                        }
                    }
                }
                if (nameValue != 0L) {
                    return nameValue;
                }
            }
            hashCode = -3750763034362895579L;
            for (int j = 0; j < this.strlen; j += 2) {
                final byte c7 = this.bytes[this.offset + j];
                final byte c8 = this.bytes[this.offset + j + 1];
                final char ch2 = (char)((c8 & 0xFF) | (c7 & 0xFF) << 8);
                hashCode ^= ch2;
                hashCode *= 1099511628211L;
            }
        }
        else if (this.strtype == 124) {
            if (this.strlen <= 16) {
                long nameValue = 0L;
                for (int i = 0; i < this.strlen; i += 2) {
                    final byte c4 = this.bytes[this.offset + i];
                    final byte c5 = this.bytes[this.offset + i + 1];
                    final char ch = (char)((c4 & 0xFF) | (c5 & 0xFF) << 8);
                    if (ch > '\u007f' || (i == 0 && ch == '\0')) {
                        nameValue = 0L;
                        break;
                    }
                    final byte c6 = (byte)ch;
                    switch (i >> 1) {
                        case 0: {
                            nameValue = c6;
                            break;
                        }
                        case 1: {
                            nameValue = (c6 << 8) + (nameValue & 0xFFL);
                            break;
                        }
                        case 2: {
                            nameValue = (c6 << 16) + (nameValue & 0xFFFFL);
                            break;
                        }
                        case 3: {
                            nameValue = (c6 << 24) + (nameValue & 0xFFFFFFL);
                            break;
                        }
                        case 4: {
                            nameValue = ((long)c6 << 32) + (nameValue & 0xFFFFFFFFL);
                            break;
                        }
                        case 5: {
                            nameValue = ((long)c6 << 40) + (nameValue & 0xFFFFFFFFFFL);
                            break;
                        }
                        case 6: {
                            nameValue = ((long)c6 << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                            break;
                        }
                        case 7: {
                            nameValue = ((long)c6 << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                            break;
                        }
                    }
                }
                if (nameValue != 0L) {
                    return nameValue;
                }
            }
            hashCode = -3750763034362895579L;
            for (int j = 0; j < this.strlen; j += 2) {
                final byte c7 = this.bytes[this.offset + j];
                final byte c8 = this.bytes[this.offset + j + 1];
                final char ch2 = (char)((c7 & 0xFF) | (c8 & 0xFF) << 8);
                hashCode ^= ch2;
                hashCode *= 1099511628211L;
            }
        }
        else {
            if (this.strlen <= 8) {
                long nameValue = 0L;
                int i = 0;
                final int start = this.offset;
                while (i < this.strlen) {
                    final byte c11 = this.bytes[this.offset];
                    if (c11 < 0 || (c11 == 0 && this.bytes[start] == 0)) {
                        nameValue = 0L;
                        this.offset = start;
                        break;
                    }
                    switch (i) {
                        case 0: {
                            nameValue = c11;
                            break;
                        }
                        case 1: {
                            nameValue = (c11 << 8) + (nameValue & 0xFFL);
                            break;
                        }
                        case 2: {
                            nameValue = (c11 << 16) + (nameValue & 0xFFFFL);
                            break;
                        }
                        case 3: {
                            nameValue = (c11 << 24) + (nameValue & 0xFFFFFFL);
                            break;
                        }
                        case 4: {
                            nameValue = ((long)c11 << 32) + (nameValue & 0xFFFFFFFFL);
                            break;
                        }
                        case 5: {
                            nameValue = ((long)c11 << 40) + (nameValue & 0xFFFFFFFFFFL);
                            break;
                        }
                        case 6: {
                            nameValue = ((long)c11 << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                            break;
                        }
                        case 7: {
                            nameValue = ((long)c11 << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                            break;
                        }
                    }
                    ++this.offset;
                    ++i;
                }
                if (nameValue != 0L) {
                    return nameValue;
                }
            }
            hashCode = -3750763034362895579L;
            for (int j = 0; j < this.strlen; ++j) {
                final byte c12 = this.bytes[this.offset++];
                hashCode ^= c12;
                hashCode *= 1099511628211L;
            }
        }
        return hashCode;
    }
    
    protected long getNameHashCode() {
        int offset = this.strBegin;
        long nameValue = 0L;
        for (int i = 0; i < this.strlen; ++i, ++offset) {
            final byte c = this.bytes[offset];
            if (c < 0 || i >= 8 || (i == 0 && this.bytes[this.strBegin] == 0)) {
                offset = this.strBegin;
                nameValue = 0L;
                break;
            }
            switch (i) {
                case 0: {
                    nameValue = c;
                    break;
                }
                case 1: {
                    nameValue = (c << 8) + (nameValue & 0xFFL);
                    break;
                }
                case 2: {
                    nameValue = (c << 16) + (nameValue & 0xFFFFL);
                    break;
                }
                case 3: {
                    nameValue = (c << 24) + (nameValue & 0xFFFFFFL);
                    break;
                }
                case 4: {
                    nameValue = ((long)c << 32) + (nameValue & 0xFFFFFFFFL);
                    break;
                }
                case 5: {
                    nameValue = ((long)c << 40) + (nameValue & 0xFFFFFFFFFFL);
                    break;
                }
                case 6: {
                    nameValue = ((long)c << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                    break;
                }
                case 7: {
                    nameValue = ((long)c << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
                    break;
                }
            }
        }
        if (nameValue != 0L) {
            return nameValue;
        }
        long hashCode = -3750763034362895579L;
        for (int j = 0; j < this.strlen; ++j) {
            final byte c2 = this.bytes[offset++];
            hashCode ^= c2;
            hashCode *= 1099511628211L;
        }
        return hashCode;
    }
    
    @Override
    public long getNameHashCodeLCase() {
        int offset = this.strBegin;
        long nameValue = 0L;
        int i = 0;
        while (i < this.strlen) {
            byte c = this.bytes[offset];
            if (c < 0 || i >= 8 || (i == 0 && this.bytes[this.strBegin] == 0)) {
                offset = this.strBegin;
                nameValue = 0L;
                break;
            }
            Label_0298: {
                if (c == 95 || c == 45 || c == 32) {
                    final byte c2 = this.bytes[offset + 1];
                    if (c2 != c) {
                        break Label_0298;
                    }
                }
                if (c >= 65 && c <= 90) {
                    c += 32;
                }
                switch (i) {
                    case 0: {
                        nameValue = c;
                        break;
                    }
                    case 1: {
                        nameValue = (c << 8) + (nameValue & 0xFFL);
                        break;
                    }
                    case 2: {
                        nameValue = (c << 16) + (nameValue & 0xFFFFL);
                        break;
                    }
                    case 3: {
                        nameValue = (c << 24) + (nameValue & 0xFFFFFFL);
                        break;
                    }
                    case 4: {
                        nameValue = ((long)c << 32) + (nameValue & 0xFFFFFFFFL);
                        break;
                    }
                    case 5: {
                        nameValue = ((long)c << 40) + (nameValue & 0xFFFFFFFFFFL);
                        break;
                    }
                    case 6: {
                        nameValue = ((long)c << 48) + (nameValue & 0xFFFFFFFFFFFFL);
                        break;
                    }
                    case 7: {
                        nameValue = ((long)c << 56) + (nameValue & 0xFFFFFFFFFFFFFFL);
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
        for (int j = 0; j < this.strlen; ++j) {
            byte c3 = this.bytes[offset++];
            if (c3 >= 65 && c3 <= 90) {
                c3 += 32;
            }
            if (c3 != 95 && c3 != 45) {
                if (c3 != 32) {
                    hashCode ^= c3;
                    hashCode *= 1099511628211L;
                }
            }
        }
        return hashCode;
    }
    
    @Override
    public void skipValue() {
        final byte type = this.bytes[this.offset++];
        switch (type) {
            case -108:
            case -81:
            case -80:
            case -79:
            case 73: {}
            case -67: {
                ++this.offset;
            }
            case -68: {
                this.offset += 2;
            }
            case -84:
            case -83:
            case -73:
            case 72: {
                this.offset += 4;
            }
            case -74: {
                this.readInt32Value();
            }
            case -85:
            case -75:
            case -66: {
                this.offset += 8;
            }
            case -76:
            case -72: {
                this.readInt64Value();
            }
            case -71: {
                this.readInt32Value();
                this.readBigInteger();
            }
            case -89: {
                this.offset += 3;
                this.readInt32Value();
            }
            case -88: {
                this.offset += 7;
                this.readInt32Value();
            }
            case -86: {
                this.offset += 7;
                this.readInt32Value();
                this.readString();
            }
            case -111: {
                final int byteslen = this.readInt32Value();
                this.offset += byteslen;
            }
            case 121:
            case 122:
            case 123:
            case 124:
            case 125: {
                final int strlen = this.readInt32Value();
                this.offset += strlen;
            }
            case -110: {
                this.readTypeHashCode();
                this.skipValue();
            }
            case -90: {
                int i = 0;
                while (this.bytes[this.offset] != -91) {
                    this.skipName();
                    this.skipValue();
                    ++i;
                }
                ++this.offset;
            }
            case -109: {
                if (this.isString()) {
                    this.skipName();
                    return;
                }
                throw new JSONException("skip not support type " + JSONB.typeName(type));
            }
            default: {
                if (type >= -16 && type <= 47) {
                    return;
                }
                if (type >= -40 && type <= -17) {
                    return;
                }
                if (type >= 48 && type <= 63) {
                    ++this.offset;
                    return;
                }
                if (type >= 73 && type <= 120) {
                    this.offset += type - 73;
                    return;
                }
                if (type >= -56 && type <= -41) {
                    ++this.offset;
                    return;
                }
                if (type >= -108 && type <= -92) {
                    int itemCnt;
                    if (type == -92) {
                        itemCnt = this.readInt32Value();
                    }
                    else {
                        itemCnt = type + 108;
                    }
                    for (int j = 0; j < itemCnt; ++j) {
                        this.skipValue();
                    }
                    return;
                }
                throw new JSONException("skip not support type " + JSONB.typeName(type));
            }
        }
    }
    
    @Override
    public boolean skipName() {
        this.strtype = this.bytes[this.offset++];
        if (this.strtype >= 73 && this.strtype <= 120) {
            this.offset += this.strtype - 73;
            return true;
        }
        if (this.strtype == 121 || this.strtype == 122 || this.strtype == 123 || this.strtype == 124 || this.strtype == 125) {
            this.strlen = this.readLength();
            this.offset += this.strlen;
            return true;
        }
        if (this.strtype != 127) {
            throw new JSONException("name not support input : " + JSONB.typeName(this.strtype));
        }
        final int type = this.bytes[this.offset];
        if (type >= -16 && type <= 72) {
            this.readInt32Value();
            return true;
        }
        final String str = this.readString();
        this.readInt32Value();
        return true;
    }
    
    @Override
    public String readFieldName() {
        this.strtype = this.bytes[this.offset];
        if (this.strtype == -81) {
            ++this.offset;
            return null;
        }
        ++this.offset;
        final boolean typeSymbol = this.strtype == 127;
        if (typeSymbol) {
            this.strtype = this.bytes[this.offset];
            if (this.strtype >= -16 && this.strtype <= 72) {
                final int symbol = this.readInt32Value();
                if (symbol < 0) {
                    return this.symbolTable.getName(-symbol);
                }
                if (symbol == 0) {
                    this.strtype = this.symbol0StrType;
                    this.strlen = this.symbol0Length;
                    this.strBegin = this.symbol0Begin;
                    return this.getFieldName();
                }
                final int index = symbol * 2 + 1;
                final long strInfo = this.symbols[index];
                this.strtype = (byte)strInfo;
                this.strlen = (int)strInfo >> 8;
                this.strBegin = (int)(strInfo >> 32);
                return this.getString();
            }
            else {
                ++this.offset;
            }
        }
        this.strBegin = this.offset;
        Charset charset = null;
        String str = null;
        if (this.strtype == 74) {
            str = TypeUtils.toString((char)(this.bytes[this.offset] & 0xFF));
            this.strlen = 1;
            ++this.offset;
        }
        else if (this.strtype == 75) {
            str = TypeUtils.toString((char)(this.bytes[this.offset] & 0xFF), (char)(this.bytes[this.offset + 1] & 0xFF));
            this.strlen = 2;
            this.offset += 2;
        }
        else if (this.strtype >= 73 && this.strtype <= 121) {
            long nameValue0 = -1L;
            long nameValue2 = -1L;
            if (this.strtype == 121) {
                this.strlen = this.readLength();
                this.strBegin = this.offset;
            }
            else {
                this.strlen = this.strtype - 73;
                if (this.offset + this.strlen > this.bytes.length) {
                    throw new JSONException("illegal jsonb data");
                }
                switch (this.strlen) {
                    case 3: {
                        nameValue0 = (this.bytes[this.offset + 2] << 16) + (((long)this.bytes[this.offset + 1] & 0xFFL) << 8) + ((long)this.bytes[this.offset] & 0xFFL);
                        break;
                    }
                    case 4: {
                        nameValue0 = JDKUtils.UNSAFE.getInt(this.bytes, JSONReaderJSONB.BASE + this.offset);
                        break;
                    }
                    case 5: {
                        nameValue0 = ((long)this.bytes[this.offset + 4] << 32) + ((long)JDKUtils.UNSAFE.getInt(this.bytes, JSONReaderJSONB.BASE + this.offset) & 0xFFFFFFFFL);
                        break;
                    }
                    case 6: {
                        nameValue0 = ((long)this.bytes[this.offset + 5] << 40) + (((long)this.bytes[this.offset + 4] & 0xFFL) << 32) + ((long)JDKUtils.UNSAFE.getInt(this.bytes, JSONReaderJSONB.BASE + this.offset) & 0xFFFFFFFFL);
                        break;
                    }
                    case 7: {
                        nameValue0 = ((long)this.bytes[this.offset + 6] << 48) + (((long)this.bytes[this.offset + 5] & 0xFFL) << 40) + (((long)this.bytes[this.offset + 4] & 0xFFL) << 32) + ((long)JDKUtils.UNSAFE.getInt(this.bytes, JSONReaderJSONB.BASE + this.offset) & 0xFFFFFFFFL);
                        break;
                    }
                    case 8: {
                        nameValue0 = JDKUtils.UNSAFE.getLong(this.bytes, JSONReaderJSONB.BASE + this.offset);
                        break;
                    }
                    case 9: {
                        nameValue0 = this.bytes[this.offset];
                        nameValue2 = JDKUtils.UNSAFE.getLong(this.bytes, JSONReaderJSONB.BASE + this.offset + 1L);
                        break;
                    }
                    case 10: {
                        nameValue0 = JDKUtils.UNSAFE.getShort(this.bytes, JSONReaderJSONB.BASE + this.offset);
                        nameValue2 = JDKUtils.UNSAFE.getLong(this.bytes, JSONReaderJSONB.BASE + this.offset + 2L);
                        break;
                    }
                    case 11: {
                        nameValue0 = (this.bytes[this.offset] << 16) + (((long)this.bytes[this.offset + 1] & 0xFFL) << 8) + ((long)this.bytes[this.offset + 2] & 0xFFL);
                        nameValue2 = JDKUtils.UNSAFE.getLong(this.bytes, JSONReaderJSONB.BASE + this.offset + 3L);
                        break;
                    }
                    case 12: {
                        nameValue0 = JDKUtils.UNSAFE.getInt(this.bytes, JSONReaderJSONB.BASE + this.offset);
                        nameValue2 = JDKUtils.UNSAFE.getLong(this.bytes, JSONReaderJSONB.BASE + this.offset + 4L);
                        break;
                    }
                    case 13: {
                        nameValue0 = ((long)this.bytes[this.offset + 4] << 32) + ((long)JDKUtils.UNSAFE.getInt(this.bytes, JSONReaderJSONB.BASE + this.offset) & 0xFFFFFFFFL);
                        nameValue2 = JDKUtils.UNSAFE.getLong(this.bytes, JSONReaderJSONB.BASE + this.offset + 5L);
                        break;
                    }
                    case 14: {
                        nameValue0 = ((long)this.bytes[this.offset + 5] << 40) + (((long)this.bytes[this.offset + 4] & 0xFFL) << 32) + ((long)JDKUtils.UNSAFE.getInt(this.bytes, JSONReaderJSONB.BASE + this.offset) & 0xFFFFFFFFL);
                        nameValue2 = JDKUtils.UNSAFE.getLong(this.bytes, JSONReaderJSONB.BASE + this.offset + 6L);
                        break;
                    }
                    case 15: {
                        nameValue0 = ((long)this.bytes[this.offset + 6] << 48) + (((long)this.bytes[this.offset + 5] & 0xFFL) << 40) + (((long)this.bytes[this.offset + 4] & 0xFFL) << 32) + ((long)JDKUtils.UNSAFE.getInt(this.bytes, JSONReaderJSONB.BASE + this.offset) & 0xFFFFFFFFL);
                        nameValue2 = JDKUtils.UNSAFE.getLong(this.bytes, JSONReaderJSONB.BASE + this.offset + 7L);
                        break;
                    }
                    case 16: {
                        nameValue0 = JDKUtils.UNSAFE.getLong(this.bytes, JSONReaderJSONB.BASE + this.offset);
                        nameValue2 = JDKUtils.UNSAFE.getLong(this.bytes, JSONReaderJSONB.BASE + this.offset + 8L);
                        break;
                    }
                }
            }
            if (this.bytes[this.offset + this.strlen - 1] > 0 && nameValue0 != -1L) {
                if (nameValue2 != -1L) {
                    final int indexMask = (int)nameValue2 & JSONFactory.NAME_CACHE2.length - 1;
                    final JSONFactory.NameCacheEntry2 entry = JSONFactory.NAME_CACHE2[indexMask];
                    if (entry == null) {
                        String name;
                        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                            final char[] chars = new char[this.strlen];
                            for (int i = 0; i < this.strlen; ++i) {
                                chars[i] = (char)(this.bytes[this.offset + i] & 0xFF);
                            }
                            name = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
                        }
                        else {
                            name = new String(this.bytes, this.offset, this.strlen, StandardCharsets.ISO_8859_1);
                        }
                        JSONFactory.NAME_CACHE2[indexMask] = new JSONFactory.NameCacheEntry2(name, nameValue0, nameValue2);
                        this.offset += this.strlen;
                        str = name;
                    }
                    else if (entry.value0 == nameValue0 && entry.value1 == nameValue2) {
                        this.offset += this.strlen;
                        str = entry.name;
                    }
                }
                else {
                    final int indexMask = (int)nameValue0 & JSONFactory.NAME_CACHE.length - 1;
                    final JSONFactory.NameCacheEntry entry2 = JSONFactory.NAME_CACHE[indexMask];
                    if (entry2 == null) {
                        String name;
                        if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                            final char[] chars = new char[this.strlen];
                            for (int i = 0; i < this.strlen; ++i) {
                                chars[i] = (char)(this.bytes[this.offset + i] & 0xFF);
                            }
                            name = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
                        }
                        else {
                            name = new String(this.bytes, this.offset, this.strlen, StandardCharsets.ISO_8859_1);
                        }
                        JSONFactory.NAME_CACHE[indexMask] = new JSONFactory.NameCacheEntry(name, nameValue0);
                        this.offset += this.strlen;
                        str = name;
                    }
                    else if (entry2.value == nameValue0) {
                        this.offset += this.strlen;
                        str = entry2.name;
                    }
                }
            }
            if (str == null) {
                if (JDKUtils.STRING_CREATOR_JDK8 != null && this.strlen >= 0) {
                    final char[] chars2 = new char[this.strlen];
                    for (int j = 0; j < this.strlen; ++j) {
                        chars2[j] = (char)(this.bytes[this.offset + j] & 0xFF);
                    }
                    this.offset += this.strlen;
                    str = JDKUtils.STRING_CREATOR_JDK8.apply(chars2, Boolean.TRUE);
                }
                else if (JDKUtils.STRING_CREATOR_JDK11 != null && this.strlen >= 0) {
                    final byte[] chars3 = new byte[this.strlen];
                    System.arraycopy(this.bytes, this.offset, chars3, 0, this.strlen);
                    str = JDKUtils.STRING_CREATOR_JDK11.apply(chars3, JDKUtils.LATIN1);
                    this.offset += this.strlen;
                }
                charset = StandardCharsets.ISO_8859_1;
            }
        }
        else if (this.strtype == 122) {
            this.strlen = this.readLength();
            this.strBegin = this.offset;
            if (JDKUtils.STRING_CREATOR_JDK11 != null && !JDKUtils.BIG_ENDIAN) {
                if (this.valueBytes == null) {
                    this.valueBytes = JSONFactory.BYTES_UPDATER.getAndSet(this.cacheItem, null);
                    if (this.valueBytes == null) {
                        this.valueBytes = new byte[8192];
                    }
                }
                final int minCapacity = this.strlen << 1;
                if (minCapacity > this.valueBytes.length) {
                    this.valueBytes = new byte[minCapacity];
                }
                final int utf16_len = IOUtils.decodeUTF8(this.bytes, this.offset, this.strlen, this.valueBytes);
                if (utf16_len != -1) {
                    final byte[] value = new byte[utf16_len];
                    System.arraycopy(this.valueBytes, 0, value, 0, utf16_len);
                    str = JDKUtils.STRING_CREATOR_JDK11.apply(value, JDKUtils.UTF16);
                    this.offset += this.strlen;
                }
            }
            charset = StandardCharsets.UTF_8;
        }
        else if (this.strtype == 123) {
            this.strlen = this.readLength();
            this.strBegin = this.offset;
            charset = StandardCharsets.UTF_16;
        }
        else if (this.strtype == 124) {
            this.strlen = this.readLength();
            this.strBegin = this.offset;
            if (JDKUtils.STRING_CREATOR_JDK11 != null && !JDKUtils.BIG_ENDIAN) {
                final byte[] chars4 = new byte[this.strlen];
                System.arraycopy(this.bytes, this.offset, chars4, 0, this.strlen);
                str = JDKUtils.STRING_CREATOR_JDK11.apply(chars4, JDKUtils.UTF16);
                this.offset += this.strlen;
            }
            charset = StandardCharsets.UTF_16LE;
        }
        else if (this.strtype == 125) {
            this.strlen = this.readLength();
            this.strBegin = this.offset;
            if (JDKUtils.STRING_CREATOR_JDK11 != null && JDKUtils.BIG_ENDIAN) {
                final byte[] chars4 = new byte[this.strlen];
                System.arraycopy(this.bytes, this.offset, chars4, 0, this.strlen);
                str = JDKUtils.STRING_CREATOR_JDK11.apply(chars4, JDKUtils.UTF16);
                this.offset += this.strlen;
            }
            charset = StandardCharsets.UTF_16BE;
        }
        else if (this.strtype == 126) {
            this.strlen = this.readLength();
            if (JSONReaderJSONB.GB18030 == null) {
                JSONReaderJSONB.GB18030 = Charset.forName("GB18030");
            }
            charset = JSONReaderJSONB.GB18030;
        }
        if (this.strlen < 0) {
            str = this.symbolTable.getName(-this.strlen);
        }
        if (str == null) {
            str = new String(this.bytes, this.offset, this.strlen, charset);
            this.offset += this.strlen;
        }
        if (typeSymbol) {
            final int symbol2 = this.readInt32Value();
            if (symbol2 == 0) {
                this.symbol0Begin = this.strBegin;
                this.symbol0Length = this.strlen;
                this.symbol0StrType = this.strtype;
            }
            else {
                final int minCapacity2 = symbol2 * 2 + 2;
                if (this.symbols == null) {
                    this.symbols = new long[Math.max(minCapacity2, 32)];
                }
                else if (this.symbols.length < minCapacity2) {
                    this.symbols = Arrays.copyOf(this.symbols, this.symbols.length + 16);
                }
                final long strInfo2 = ((long)this.strBegin << 32) + ((long)this.strlen << 8) + this.strtype;
                this.symbols[symbol2 * 2 + 1] = strInfo2;
            }
        }
        return str;
    }
    
    @Override
    public String getFieldName() {
        return this.getString();
    }
    
    @Override
    public String readString() {
        final byte strtype = this.bytes[this.offset++];
        this.strtype = strtype;
        if (strtype == -81) {
            return null;
        }
        this.strBegin = this.offset;
        String str = null;
        boolean ascii = false;
        if (strtype >= 73 && strtype <= 121) {
            ascii = true;
            int strlen;
            if (strtype == 121) {
                final byte strType = this.bytes[this.offset];
                if (strType >= -16 && strType <= 47) {
                    ++this.offset;
                    strlen = strType;
                }
                else {
                    strlen = this.readLength();
                }
                this.strBegin = this.offset;
            }
            else {
                strlen = strtype - 73;
            }
            this.strlen = strlen;
            if (strlen >= 0) {
                if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                    final char[] chars = new char[strlen];
                    for (int i = 0; i < strlen; ++i) {
                        chars[i] = (char)(this.bytes[this.offset + i] & 0xFF);
                    }
                    this.offset += strlen;
                    str = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
                }
                else if (JDKUtils.STRING_CREATOR_JDK11 != null) {
                    final byte[] chars2 = new byte[strlen];
                    System.arraycopy(this.bytes, this.offset, chars2, 0, strlen);
                    str = JDKUtils.STRING_CREATOR_JDK11.apply(chars2, JDKUtils.LATIN1);
                    this.offset += strlen;
                }
            }
            if (str != null) {
                if ((this.context.features & Feature.TrimString.mask) != 0x0L) {
                    str = str.trim();
                }
                return str;
            }
        }
        return this.readStringNonAscii(null, ascii);
    }
    
    private String readStringNonAscii(String str, final boolean ascii) {
        Charset charset;
        if (ascii) {
            charset = StandardCharsets.ISO_8859_1;
        }
        else if (this.strtype == 122) {
            str = this.readStringUTF8();
            charset = StandardCharsets.UTF_8;
        }
        else if (this.strtype == 123) {
            this.strlen = this.readLength();
            this.strBegin = this.offset;
            charset = StandardCharsets.UTF_16;
        }
        else if (this.strtype == 124) {
            str = this.readUTF16LE();
            charset = StandardCharsets.UTF_16LE;
        }
        else if (this.strtype == 125) {
            str = this.readUTF16BE();
            if (str != null) {
                return str;
            }
            charset = StandardCharsets.UTF_16BE;
        }
        else {
            if (this.strtype != 126) {
                return this.readStringTypeNotMatch();
            }
            this.readGB18030();
            charset = JSONReaderJSONB.GB18030;
        }
        if (str != null) {
            if ((this.context.features & Feature.TrimString.mask) != 0x0L) {
                str = str.trim();
            }
            return str;
        }
        return this.readString(charset);
    }
    
    private String readString(final Charset charset) {
        if (this.strlen < 0) {
            return this.symbolTable.getName(-this.strlen);
        }
        char[] chars = null;
        if (JDKUtils.JVM_VERSION == 8 && this.strtype == 122 && this.strlen < 8192) {
            final int cacheIndex = System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1;
            final JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[cacheIndex];
            chars = JSONFactory.CHARS_UPDATER.getAndSet(cacheItem, null);
            if (chars == null) {
                chars = new char[8192];
            }
        }
        String str;
        if (chars != null) {
            final int len = IOUtils.decodeUTF8(this.bytes, this.offset, this.strlen, chars);
            str = new String(chars, 0, len);
            if (chars.length < 1048576) {
                JSONFactory.CHARS_UPDATER.lazySet(this.cacheItem, chars);
            }
        }
        else {
            str = new String(this.bytes, this.offset, this.strlen, charset);
        }
        this.offset += this.strlen;
        if ((this.context.features & Feature.TrimString.mask) != 0x0L) {
            str = str.trim();
        }
        return str;
    }
    
    private void readGB18030() {
        this.strlen = this.readLength();
        this.strBegin = this.offset;
        if (JSONReaderJSONB.GB18030 == null) {
            JSONReaderJSONB.GB18030 = Charset.forName("GB18030");
        }
    }
    
    private String readUTF16BE() {
        this.strlen = this.readLength();
        this.strBegin = this.offset;
        if (JDKUtils.STRING_CREATOR_JDK11 != null && JDKUtils.BIG_ENDIAN) {
            final byte[] chars = new byte[this.strlen];
            System.arraycopy(this.bytes, this.offset, chars, 0, this.strlen);
            String str = JDKUtils.STRING_CREATOR_JDK11.apply(chars, JDKUtils.UTF16);
            this.offset += this.strlen;
            if ((this.context.features & Feature.TrimString.mask) != 0x0L) {
                str = str.trim();
            }
            return str;
        }
        return null;
    }
    
    private String readUTF16LE() {
        final byte strType = this.bytes[this.offset];
        if (strType >= -16 && strType <= 47) {
            ++this.offset;
            this.strlen = strType;
        }
        else if (strType >= 48 && strType <= 63) {
            ++this.offset;
            this.strlen = (strType - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
        }
        else {
            this.strlen = this.readLength();
        }
        this.strBegin = this.offset;
        if (this.strlen == 0) {
            return "";
        }
        if (JDKUtils.STRING_CREATOR_JDK11 != null && !JDKUtils.BIG_ENDIAN) {
            final byte[] chars = new byte[this.strlen];
            System.arraycopy(this.bytes, this.offset, chars, 0, this.strlen);
            String str = JDKUtils.STRING_CREATOR_JDK11.apply(chars, JDKUtils.UTF16);
            this.offset += this.strlen;
            if ((this.context.features & Feature.TrimString.mask) != 0x0L) {
                str = str.trim();
            }
            return str;
        }
        return null;
    }
    
    private String readStringUTF8() {
        final byte strType = this.bytes[this.offset];
        if (strType >= -16 && strType <= 47) {
            ++this.offset;
            this.strlen = strType;
        }
        else if (strType >= 48 && strType <= 63) {
            ++this.offset;
            this.strlen = (strType - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
        }
        else {
            this.strlen = this.readLength();
        }
        this.strBegin = this.offset;
        if (JDKUtils.STRING_CREATOR_JDK11 != null && !JDKUtils.BIG_ENDIAN) {
            if (this.valueBytes == null) {
                this.valueBytes = JSONFactory.BYTES_UPDATER.getAndSet(this.cacheItem, null);
                if (this.valueBytes == null) {
                    this.valueBytes = new byte[8192];
                }
            }
            final int minCapacity = this.strlen << 1;
            if (minCapacity > this.valueBytes.length) {
                this.valueBytes = new byte[minCapacity];
            }
            final int utf16_len = IOUtils.decodeUTF8(this.bytes, this.offset, this.strlen, this.valueBytes);
            if (utf16_len != -1) {
                final byte[] value = new byte[utf16_len];
                System.arraycopy(this.valueBytes, 0, value, 0, utf16_len);
                String str = JDKUtils.STRING_CREATOR_JDK11.apply(value, JDKUtils.UTF16);
                this.offset += this.strlen;
                if ((this.context.features & Feature.TrimString.mask) != 0x0L) {
                    str = str.trim();
                }
                return str;
            }
        }
        return null;
    }
    
    private String readStringTypeNotMatch() {
        if (this.strtype >= -16 && this.strtype <= 47) {
            return Byte.toString(this.strtype);
        }
        if (this.strtype >= 48 && this.strtype <= 63) {
            final int intValue = (this.strtype - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
            return Integer.toString(intValue);
        }
        if (this.strtype >= 64 && this.strtype <= 71) {
            final int int3 = getInt3(this.bytes, this.offset, this.strtype);
            this.offset += 2;
            return Integer.toString(int3);
        }
        if (this.strtype >= -40 && this.strtype <= -17) {
            final int intValue = -8 + (this.strtype + 40);
            return Integer.toString(intValue);
        }
        if (this.strtype >= -56 && this.strtype <= -41) {
            final int intValue = (this.strtype + 48 << 8) + (this.bytes[this.offset++] & 0xFF);
            return Integer.toString(intValue);
        }
        if (this.strtype >= -64 && this.strtype <= -57) {
            final int intValue = (this.strtype + 60 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
            return Integer.toString(intValue);
        }
        switch (this.strtype) {
            case -81: {
                return null;
            }
            case -78: {
                return "0.0";
            }
            case -77: {
                return "1.0";
            }
            case -65:
            case 72: {
                final int int32Value = JDKUtils.UNSAFE.getInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 4;
                return Long.toString(JDKUtils.BIG_ENDIAN ? ((long)int32Value) : ((long)Integer.reverseBytes(int32Value)));
            }
            case -74: {
                return Float.toString((float)this.readInt32Value());
            }
            case -73: {
                final int int32Value = JDKUtils.UNSAFE.getInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 4;
                final float floatValue = Float.intBitsToFloat(JDKUtils.BIG_ENDIAN ? int32Value : Integer.reverseBytes(int32Value));
                return Float.toString(floatValue);
            }
            case -75: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                final double doubleValue = Double.longBitsToDouble(JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
                return Double.toString(doubleValue);
            }
            case -84: {
                final int seconds = JDKUtils.UNSAFE.getInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 4;
                final long millis = (JDKUtils.BIG_ENDIAN ? seconds : Integer.reverseBytes(seconds)) * 1000L;
                final Date date = new Date(millis);
                return DateUtils.toString(date);
            }
            case -83: {
                final int minutes = JDKUtils.UNSAFE.getInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 4;
                final long millis = (JDKUtils.BIG_ENDIAN ? minutes : Integer.reverseBytes(minutes)) * 60000L;
                final Date date = new Date(millis);
                return DateUtils.toString(date);
            }
            case -85: {
                final long millis2 = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                final Date date2 = new Date(JDKUtils.BIG_ENDIAN ? millis2 : Long.reverseBytes(millis2));
                return DateUtils.toString(date2);
            }
            case -66: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return Long.toString(JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
            }
            case -69: {
                final int len = this.readInt32Value();
                final byte[] bytes = new byte[len];
                System.arraycopy(this.bytes, this.offset, bytes, 0, len);
                this.offset += len;
                return new BigInteger(bytes).toString();
            }
            case -71: {
                final int scale = this.readInt32Value();
                final BigInteger unscaledValue = this.readBigInteger();
                BigDecimal decimal;
                if (scale == 0) {
                    decimal = new BigDecimal(unscaledValue);
                }
                else {
                    decimal = new BigDecimal(unscaledValue, scale);
                }
                return decimal.toString();
            }
            case -110: {
                --this.offset;
                final Object typedAny = this.readAny();
                return (typedAny == null) ? null : JSON.toJSONString(typedAny, JSONWriter.Feature.WriteThrowableClassName);
            }
            case -72:
            case -70: {
                return Long.toString(this.readInt64Value());
            }
            case -76: {
                final double doubleValue = (double)this.readInt64Value();
                return Double.toString(doubleValue);
            }
            default: {
                throw new JSONException("readString not support type " + JSONB.typeName(this.strtype) + ", offset " + this.offset + "/" + this.bytes.length);
            }
        }
    }
    
    @Override
    public String[] readStringArray() {
        if (this.nextIfMatch((byte)(-110))) {
            final long typeHash = this.readTypeHashCode();
            if (typeHash != ObjectReaderImplStringArray.HASH_TYPE) {
                throw new JSONException(this.info("not support type " + this.getString()));
            }
        }
        final int entryCnt = this.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final String[] array = new String[entryCnt];
        for (int i = 0; i < entryCnt; ++i) {
            array[i] = this.readString();
        }
        return array;
    }
    
    @Override
    public char readCharValue() {
        final byte type = this.bytes[this.offset];
        if (type == -112) {
            ++this.offset;
            return (char)this.readInt32Value();
        }
        if (type == 73) {
            ++this.offset;
            return '\0';
        }
        if (type > 73 && type < 120) {
            ++this.offset;
            return (char)(this.bytes[this.offset++] & 0xFF);
        }
        final String str = this.readString();
        if (str == null || str.isEmpty()) {
            return '\0';
        }
        return str.charAt(0);
    }
    
    @Override
    public int[] readInt32ValueArray() {
        if (this.nextIfMatch((byte)(-110))) {
            final long typeHash = this.readTypeHashCode();
            if (typeHash != ObjectReaderImplInt64ValueArray.HASH_TYPE && typeHash != ObjectReaderImplInt64Array.HASH_TYPE && typeHash != ObjectReaderImplInt32Array.HASH_TYPE && typeHash != ObjectReaderImplInt32ValueArray.HASH_TYPE) {
                throw new JSONException(this.info("not support " + this.getString()));
            }
        }
        final int entryCnt = this.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final int[] array = new int[entryCnt];
        for (int i = 0; i < entryCnt; ++i) {
            array[i] = this.readInt32Value();
        }
        return array;
    }
    
    @Override
    public long[] readInt64ValueArray() {
        if (this.nextIfMatch((byte)(-110))) {
            final long typeHash = this.readTypeHashCode();
            if (typeHash != ObjectReaderImplInt64ValueArray.HASH_TYPE && typeHash != ObjectReaderImplInt64Array.HASH_TYPE && typeHash != ObjectReaderImplInt32Array.HASH_TYPE && typeHash != ObjectReaderImplInt32ValueArray.HASH_TYPE) {
                throw new JSONException(this.info("not support " + this.getString()));
            }
        }
        final int entryCnt = this.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final long[] array = new long[entryCnt];
        for (int i = 0; i < entryCnt; ++i) {
            array[i] = this.readInt64Value();
        }
        return array;
    }
    
    @Override
    public long readInt64Value() {
        this.wasNull = false;
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        final byte type = bytes[offset++];
        long int64Value;
        if (type >= -40 && type <= -17) {
            int64Value = -8 + (type + 40);
        }
        else if (type >= -56 && type <= -41) {
            int64Value = (type + 48 << 8) + (bytes[offset] & 0xFF);
            ++offset;
        }
        else if (type >= -64 && type <= -57) {
            int64Value = (type + 60 << 16) + ((bytes[offset] & 0xFF) << 8) + (bytes[offset + 1] & 0xFF);
            offset += 2;
        }
        else if (type == -65) {
            final int int32Value = JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset);
            int64Value = (JDKUtils.BIG_ENDIAN ? int32Value : ((long)Integer.reverseBytes(int32Value)));
            offset += 4;
        }
        else {
            if (type != -66) {
                this.offset = offset;
                return this.readInt64Value0(bytes, type);
            }
            int64Value = JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset);
            if (!JDKUtils.BIG_ENDIAN) {
                int64Value = Long.reverseBytes(int64Value);
            }
            offset += 8;
        }
        this.offset = offset;
        return int64Value;
    }
    
    private long readInt64Value0(final byte[] bytes, final byte type) {
        if (type >= 48 && type <= 63) {
            return (type - 56 << 8) + (bytes[this.offset++] & 0xFF);
        }
        if (type >= -16 && type <= 47) {
            return type;
        }
        if (type >= 64 && type <= 71) {
            final int int3 = getInt3(bytes, this.offset, type);
            this.offset += 2;
            return int3;
        }
        switch (type) {
            case -81: {
                if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) != 0x0L) {
                    throw new JSONException(this.info("long value not support input null"));
                }
                this.wasNull = true;
                return 0L;
            }
            case -80:
            case -78: {
                return 0L;
            }
            case -79:
            case -77: {
                return 1L;
            }
            case -67: {
                return bytes[this.offset++];
            }
            case -68: {
                final int int16Value = (bytes[this.offset + 1] & 0xFF) + (bytes[this.offset] << 8);
                this.offset += 2;
                return int16Value;
            }
            case 72: {
                final int int32Value = JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 4;
                return JDKUtils.BIG_ENDIAN ? int32Value : ((long)Integer.reverseBytes(int32Value));
            }
            case -73: {
                final int int32Value = JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 4;
                final float floatValue = Float.intBitsToFloat(JDKUtils.BIG_ENDIAN ? int32Value : Integer.reverseBytes(int32Value));
                return (long)floatValue;
            }
            case -75: {
                --this.offset;
                return (long)this.readDoubleValue();
            }
            case -74: {
                return (long)(float)this.readInt32Value();
            }
            case -76: {
                return (long)(double)this.readInt64Value();
            }
            case -83: {
                final long minutes = getInt(bytes, this.offset);
                this.offset += 4;
                return minutes * 60L * 1000L;
            }
            case -84: {
                final long seconds = getInt(bytes, this.offset);
                this.offset += 4;
                return seconds * 1000L;
            }
            case -85: {
                final long int64Value = JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value);
            }
            case -71: {
                final int scale = this.readInt32Value();
                final BigInteger unscaledValue = this.readBigInteger();
                BigDecimal decimal;
                if (scale == 0) {
                    decimal = new BigDecimal(unscaledValue);
                }
                else {
                    decimal = new BigDecimal(unscaledValue, scale);
                }
                return decimal.longValue();
            }
            case 121: {
                final int strlen = this.readInt32Value();
                final String str = new String(bytes, this.offset, strlen, StandardCharsets.ISO_8859_1);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str).intValue();
                }
                return TypeUtils.toBigDecimal(str).intValue();
            }
            case 122: {
                final int strlen = this.readInt32Value();
                final String str = new String(bytes, this.offset, strlen, StandardCharsets.UTF_8);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str).intValue();
                }
                return TypeUtils.toBigDecimal(str).intValue();
            }
            case 124: {
                final int strlen = this.readInt32Value();
                final String str = new String(bytes, this.offset, strlen, StandardCharsets.UTF_16LE);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str).intValue();
                }
                return TypeUtils.toBigDecimal(str).intValue();
            }
            default: {
                if (type < 73 || type > 120) {
                    throw new JSONException("readInt64Value not support " + JSONB.typeName(type) + ", offset " + this.offset + "/" + bytes.length);
                }
                final int strlen = type - 73;
                final String str = this.readFixedAsciiString(strlen);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str).longValue();
                }
                return TypeUtils.toBigDecimal(str).longValue();
            }
        }
    }
    
    @Override
    public int readInt32Value() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        final byte type = bytes[offset++];
        int int32Value;
        if (type >= -16 && type <= 47) {
            int32Value = type;
        }
        else if (type >= 48 && type <= 63) {
            int32Value = (type - 56 << 8) + (bytes[offset] & 0xFF);
            ++offset;
        }
        else if (type >= 64 && type <= 71) {
            int32Value = (type - 68 << 16) + ((bytes[offset] & 0xFF) << 8) + (bytes[offset + 1] & 0xFF);
            offset += 2;
        }
        else {
            if (type != 72) {
                this.offset = offset;
                return this.readInt32Value0(bytes, type);
            }
            int32Value = JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset);
            if (!JDKUtils.BIG_ENDIAN) {
                int32Value = Integer.reverseBytes(int32Value);
            }
            offset += 4;
        }
        this.offset = offset;
        return int32Value;
    }
    
    private int readInt32Value0(final byte[] bytes, final byte type) {
        if (type >= -40 && type <= -17) {
            return -8 + (type + 40);
        }
        if (type >= -56 && type <= -41) {
            return (type + 48 << 8) + (bytes[this.offset++] & 0xFF);
        }
        if (type >= -64 && type <= -57) {
            return (type + 60 << 16) + ((bytes[this.offset++] & 0xFF) << 8) + (bytes[this.offset++] & 0xFF);
        }
        switch (type) {
            case -81: {
                if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) != 0x0L) {
                    throw new JSONException(this.info("int value not support input null"));
                }
                this.wasNull = true;
                return 0;
            }
            case -80:
            case -78: {
                return 0;
            }
            case -79:
            case -77: {
                return 1;
            }
            case -67: {
                return bytes[this.offset++];
            }
            case -68: {
                final int int16Value = (bytes[this.offset + 1] & 0xFF) + (bytes[this.offset] << 8);
                this.offset += 2;
                return int16Value;
            }
            case -76: {
                return (int)this.readInt64Value();
            }
            case -66: {
                final long int64Value = JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return (int)(JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
            }
            case -74: {
                return (int)(float)this.readInt32Value();
            }
            case -73: {
                final int int32Value = getInt(bytes, this.offset);
                this.offset += 4;
                final float floatValue = Float.intBitsToFloat(int32Value);
                return (int)floatValue;
            }
            case -75: {
                --this.offset;
                return (int)this.readDoubleValue();
            }
            case -84:
            case -83:
            case -65: {
                final int int32Value = getInt(bytes, this.offset);
                this.offset += 4;
                return int32Value;
            }
            case 121: {
                final int strlen = this.readInt32Value();
                final String str = new String(bytes, this.offset, strlen, StandardCharsets.ISO_8859_1);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str).intValue();
                }
                return TypeUtils.toBigDecimal(str).intValue();
            }
            case 124: {
                final int strlen = this.readInt32Value();
                final String str = new String(bytes, this.offset, strlen, StandardCharsets.UTF_16LE);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str).intValue();
                }
                return TypeUtils.toBigDecimal(str).intValue();
            }
            case 122: {
                final int strlen = this.readInt32Value();
                final String str = new String(bytes, this.offset, strlen, StandardCharsets.UTF_8);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str).intValue();
                }
                return TypeUtils.toBigDecimal(str).intValue();
            }
            case -71: {
                final int scale = this.readInt32Value();
                final BigInteger unscaledValue = this.readBigInteger();
                BigDecimal decimal;
                if (scale == 0) {
                    decimal = new BigDecimal(unscaledValue);
                }
                else {
                    decimal = new BigDecimal(unscaledValue, scale);
                }
                return decimal.intValue();
            }
            default: {
                if (type < 73 || type > 120) {
                    throw new JSONException("readInt32Value not support " + JSONB.typeName(type) + ", offset " + this.offset + "/" + bytes.length);
                }
                final int strlen = type - 73;
                final String str = this.readFixedAsciiString(strlen);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str).intValue();
                }
                return TypeUtils.toBigDecimal(str).intValue();
            }
        }
    }
    
    @Override
    public boolean isBinary() {
        return this.bytes[this.offset] == -111;
    }
    
    @Override
    public byte[] readBinary() {
        final byte type = this.bytes[this.offset++];
        if (type != -111) {
            throw new JSONException("not support input : " + JSONB.typeName(type));
        }
        final int len = this.readLength();
        final byte[] bytes = new byte[len];
        System.arraycopy(this.bytes, this.offset, bytes, 0, len);
        this.offset += len;
        return bytes;
    }
    
    @Override
    public Integer readInt32() {
        if (this.bytes[this.offset] == -81) {
            ++this.offset;
            this.wasNull = true;
            return null;
        }
        this.wasNull = false;
        final int value = this.readInt32Value();
        if (this.wasNull) {
            return null;
        }
        return value;
    }
    
    @Override
    public Long readInt64() {
        final byte[] bytes = this.bytes;
        int offset = this.offset;
        final byte type = bytes[offset++];
        if (type == -81) {
            this.offset = offset;
            return null;
        }
        long int64Value;
        if (type >= -40 && type <= -17) {
            int64Value = -8 + (type + 40);
        }
        else if (type >= -56 && type <= -41) {
            int64Value = (type + 48 << 8) + (bytes[offset] & 0xFF);
            ++offset;
        }
        else if (type >= -64 && type <= -57) {
            int64Value = (type + 60 << 16) + ((bytes[offset] & 0xFF) << 8) + (bytes[offset + 1] & 0xFF);
            offset += 2;
        }
        else if (type == -65) {
            final int int32Val = JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset);
            int64Value = (JDKUtils.BIG_ENDIAN ? int32Val : ((long)Integer.reverseBytes(int32Val)));
            offset += 4;
        }
        else {
            if (type != -66) {
                this.offset = offset;
                return this.readInt64Value0(bytes, type);
            }
            int64Value = JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset);
            if (!JDKUtils.BIG_ENDIAN) {
                int64Value = Long.reverseBytes(int64Value);
            }
            offset += 8;
        }
        this.offset = offset;
        return int64Value;
    }
    
    protected String readFixedAsciiString(final int strlen) {
        String str;
        if (strlen == 1) {
            str = TypeUtils.toString((char)(this.bytes[this.offset] & 0xFF));
        }
        else if (strlen == 2) {
            str = TypeUtils.toString((char)(this.bytes[this.offset] & 0xFF), (char)(this.bytes[this.offset + 1] & 0xFF));
        }
        else if (JDKUtils.STRING_CREATOR_JDK8 != null) {
            final char[] chars = new char[strlen];
            for (int i = 0; i < strlen; ++i) {
                chars[i] = (char)(this.bytes[this.offset + i] & 0xFF);
            }
            str = JDKUtils.STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
        }
        else {
            str = new String(this.bytes, this.offset, strlen, StandardCharsets.ISO_8859_1);
        }
        return str;
    }
    
    @Override
    public Float readFloat() {
        final byte[] bytes = this.bytes;
        final int offset = this.offset;
        final byte type = bytes[offset];
        if (type == -73) {
            final int int32Value = (bytes[offset + 4] & 0xFF) + ((bytes[offset + 3] & 0xFF) << 8) + ((bytes[offset + 2] & 0xFF) << 16) + (bytes[offset + 1] << 24);
            this.offset = offset + 5;
            return Float.intBitsToFloat(int32Value);
        }
        if (type == -81) {
            this.offset = offset + 1;
            return null;
        }
        return this.readFloat0();
    }
    
    @Override
    public float readFloatValue() {
        final byte[] bytes = this.bytes;
        final int offset = this.offset;
        if (bytes[offset] == -73) {
            final int int32Val = JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset + 1L);
            this.offset = offset + 5;
            return Float.intBitsToFloat(JDKUtils.BIG_ENDIAN ? int32Val : Integer.reverseBytes(int32Val));
        }
        return this.readFloat0();
    }
    
    private float readFloat0() {
        final byte type = this.bytes[this.offset++];
        switch (type) {
            case -81: {
                if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) != 0x0L) {
                    throw new JSONException(this.info("long value not support input null"));
                }
                this.wasNull = true;
                return 0.0f;
            }
            case -67: {
                return this.bytes[this.offset++];
            }
            case -68: {
                final int int16Value = (this.bytes[this.offset + 1] & 0xFF) + (this.bytes[this.offset] << 8);
                this.offset += 2;
                return (float)int16Value;
            }
            case -66: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return (float)(JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
            }
            case -65:
            case 72: {
                final int int32Value = getInt(this.bytes, this.offset);
                this.offset += 4;
                return (float)int32Value;
            }
            case -75: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return (float)Double.longBitsToDouble(JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
            }
            case -74: {
                return (float)this.readInt32Value();
            }
            case -76: {
                return (float)this.readInt64Value();
            }
            case 121: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.ISO_8859_1);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return (float)new BigInteger(str).intValue();
                }
                return (float)TypeUtils.toBigDecimal(str).intValue();
            }
            case 124: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_16LE);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return (float)new BigInteger(str).intValue();
                }
                return (float)TypeUtils.toBigDecimal(str).intValue();
            }
            case 122: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_8);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return (float)new BigInteger(str).intValue();
                }
                return (float)TypeUtils.toBigDecimal(str).intValue();
            }
            case -71: {
                final int scale = this.readInt32Value();
                final BigInteger unscaledValue = this.readBigInteger();
                BigDecimal decimal;
                if (scale == 0) {
                    decimal = new BigDecimal(unscaledValue);
                }
                else {
                    decimal = new BigDecimal(unscaledValue, scale);
                }
                return (float)decimal.intValue();
            }
            case -80:
            case -78: {
                return 0.0f;
            }
            case -79:
            case -77: {
                return 1.0f;
            }
            default: {
                if (type >= -16 && type <= 47) {
                    return type;
                }
                if (type >= 48 && type <= 63) {
                    return (float)((type - 56 << 8) + (this.bytes[this.offset++] & 0xFF));
                }
                if (type >= 64 && type <= 71) {
                    final int int3 = getInt3(this.bytes, this.offset, type);
                    this.offset += 2;
                    return (float)int3;
                }
                if (type >= -40 && type <= -17) {
                    return (float)(-8 + (type + 40));
                }
                if (type >= -56 && type <= -41) {
                    return (float)((type + 48 << 8) + (this.bytes[this.offset++] & 0xFF));
                }
                if (type >= -64 && type <= -57) {
                    return (float)((type + 60 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF));
                }
                if (type < 73 || type > 120) {
                    throw new JSONException("TODO : " + JSONB.typeName(type));
                }
                final int strlen = type - 73;
                final String str = this.readFixedAsciiString(strlen);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return (float)new BigInteger(str).intValue();
                }
                return (float)TypeUtils.toBigDecimal(str).intValue();
            }
        }
    }
    
    @Override
    public double readDoubleValue() {
        final byte[] bytes = this.bytes;
        final int offset = this.offset;
        if (bytes[offset] == -75) {
            final long int64Value = ((long)bytes[offset + 8] & 0xFFL) + (((long)bytes[offset + 7] & 0xFFL) << 8) + (((long)bytes[offset + 6] & 0xFFL) << 16) + (((long)bytes[offset + 5] & 0xFFL) << 24) + (((long)bytes[offset + 4] & 0xFFL) << 32) + (((long)bytes[offset + 3] & 0xFFL) << 40) + (((long)bytes[offset + 2] & 0xFFL) << 48) + ((long)bytes[offset + 1] << 56);
            this.offset = offset + 9;
            return Double.longBitsToDouble(int64Value);
        }
        return this.readDoubleValue0();
    }
    
    private double readDoubleValue0() {
        final byte type = this.bytes[this.offset++];
        switch (type) {
            case -81: {
                if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) != 0x0L) {
                    throw new JSONException(this.info("long value not support input null"));
                }
                this.wasNull = true;
                return 0.0;
            }
            case -67: {
                return this.bytes[this.offset++];
            }
            case -68: {
                final int int16Value = (this.bytes[this.offset + 1] & 0xFF) + (this.bytes[this.offset] << 8);
                this.offset += 2;
                return int16Value;
            }
            case -66: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return (double)(JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
            }
            case -65:
            case 72: {
                final int int32Value = JDKUtils.UNSAFE.getInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 4;
                return JDKUtils.BIG_ENDIAN ? int32Value : ((double)Integer.reverseBytes(int32Value));
            }
            case -73: {
                final int int32Value = JDKUtils.UNSAFE.getInt(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 4;
                return Float.intBitsToFloat(JDKUtils.BIG_ENDIAN ? int32Value : Integer.reverseBytes(int32Value));
            }
            case -74: {
                return (float)this.readInt32Value();
            }
            case -76: {
                return (double)this.readInt64Value();
            }
            case 121: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.ISO_8859_1);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str).intValue();
                }
                return TypeUtils.toBigDecimal(str).intValue();
            }
            case 124: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_16LE);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str).intValue();
                }
                return TypeUtils.toBigDecimal(str).intValue();
            }
            case 122: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_8);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str).intValue();
                }
                return TypeUtils.toBigDecimal(str).intValue();
            }
            case -71: {
                final int scale = this.readInt32Value();
                final BigInteger unscaledValue = this.readBigInteger();
                BigDecimal decimal;
                if (scale == 0) {
                    decimal = new BigDecimal(unscaledValue);
                }
                else {
                    decimal = new BigDecimal(unscaledValue, scale);
                }
                return decimal.intValue();
            }
            case -80:
            case -78: {
                return 0.0;
            }
            case -79:
            case -77: {
                return 1.0;
            }
            default: {
                if (type >= -16 && type <= 47) {
                    return type;
                }
                if (type >= 48 && type <= 63) {
                    return (type - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
                }
                if (type >= 64 && type <= 71) {
                    final int int3 = getInt3(this.bytes, this.offset, type);
                    this.offset += 2;
                    return int3;
                }
                if (type >= -40 && type <= -17) {
                    return (double)(-8L + (type + 40));
                }
                if (type >= -56 && type <= -41) {
                    return (type + 48 << 8) + (this.bytes[this.offset++] & 0xFF);
                }
                if (type >= -64 && type <= -57) {
                    return (type + 60 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
                }
                if (type < 73 || type > 120) {
                    throw new JSONException("TODO : " + JSONB.typeName(type));
                }
                final int strlen = type - 73;
                final String str = this.readFixedAsciiString(strlen);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str).intValue();
                }
                return TypeUtils.toBigDecimal(str).intValue();
            }
        }
    }
    
    @Override
    protected void readNumber0() {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public Number readNumber() {
        final byte type = this.bytes[this.offset++];
        if (type >= -16 && type <= 47) {
            return type;
        }
        if (type >= 48 && type <= 63) {
            return (type - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
        }
        if (type >= 64 && type <= 71) {
            final int int3 = getInt3(this.bytes, this.offset, type);
            this.offset += 2;
            return int3;
        }
        if (type >= -40 && type <= -17) {
            return -8L + (type + 40);
        }
        if (type >= -56 && type <= -41) {
            return (type + 48 << 8) + (this.bytes[this.offset++] & 0xFF);
        }
        if (type >= -64 && type <= -57) {
            return (type + 60 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
        }
        switch (type) {
            case -81: {
                return null;
            }
            case -80:
            case -78: {
                return 0.0;
            }
            case -79:
            case -77: {
                return 1.0;
            }
            case -67: {
                return this.bytes[this.offset++];
            }
            case -68: {
                final int int16Value = (this.bytes[this.offset + 1] & 0xFF) + (this.bytes[this.offset] << 8);
                this.offset += 2;
                return (short)int16Value;
            }
            case 72: {
                final int int32Value = getInt(this.bytes, this.offset);
                this.offset += 4;
                return int32Value;
            }
            case -65: {
                final int int32Value = getInt(this.bytes, this.offset);
                this.offset += 4;
                return int32Value;
            }
            case -66: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value);
            }
            case -69: {
                final int len = this.readInt32Value();
                final byte[] bytes = new byte[len];
                System.arraycopy(this.bytes, this.offset, bytes, 0, len);
                this.offset += len;
                return new BigInteger(bytes);
            }
            case -70: {
                return BigInteger.valueOf(this.readInt64Value());
            }
            case -73: {
                final int int32Value = getInt(this.bytes, this.offset);
                this.offset += 4;
                return Float.intBitsToFloat(int32Value);
            }
            case -74: {
                return this.readInt32Value();
            }
            case -75: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return Double.longBitsToDouble(JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
            }
            case -76: {
                return this.readInt64Value();
            }
            case -71: {
                final int scale = this.readInt32Value();
                final BigInteger unscaledValue = this.readBigInteger();
                if (scale == 0) {
                    return new BigDecimal(unscaledValue);
                }
                return new BigDecimal(unscaledValue, scale);
            }
            case -72: {
                return BigDecimal.valueOf(this.readInt64Value());
            }
            case 121: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.ISO_8859_1);
                this.offset += strlen;
                return TypeUtils.toBigDecimal(str);
            }
            case 122: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_8);
                this.offset += strlen;
                return TypeUtils.toBigDecimal(str);
            }
            case -110: {
                final String typeName = this.readString();
                throw new JSONException("not support input type : " + typeName);
            }
            default: {
                if (type >= 73 && type <= 120) {
                    final int strlen = type - 73;
                    final String str = this.readFixedAsciiString(strlen);
                    this.offset += strlen;
                    return TypeUtils.toBigDecimal(str);
                }
                throw new JSONException("not support type :" + JSONB.typeName(type));
            }
        }
    }
    
    @Override
    public BigDecimal readBigDecimal() {
        final byte type = this.bytes[this.offset++];
        BigDecimal decimal;
        if (type == -71) {
            final int scale = this.readInt32Value();
            if (this.bytes[this.offset] == -70) {
                ++this.offset;
                final long unscaledLongValue = this.readInt64Value();
                decimal = BigDecimal.valueOf(unscaledLongValue, scale);
            }
            else if (this.bytes[this.offset] == 72) {
                decimal = BigDecimal.valueOf(getInt(this.bytes, this.offset + 1), scale);
                this.offset += 5;
            }
            else if (this.bytes[this.offset] == -66) {
                final long unscaledValue = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset + 1L);
                decimal = BigDecimal.valueOf(JDKUtils.BIG_ENDIAN ? unscaledValue : Long.reverseBytes(unscaledValue), scale);
                this.offset += 9;
            }
            else {
                final BigInteger unscaledValue2 = this.readBigInteger();
                decimal = ((scale == 0) ? new BigDecimal(unscaledValue2) : new BigDecimal(unscaledValue2, scale));
            }
        }
        else if (type == -72) {
            decimal = BigDecimal.valueOf(this.readInt64Value());
        }
        else {
            decimal = this.readDecimal0(type);
        }
        return decimal;
    }
    
    private BigDecimal readDecimal0(final byte type) {
        switch (type) {
            case -81: {
                return null;
            }
            case -80:
            case -78: {
                return BigDecimal.ZERO;
            }
            case -79:
            case -77: {
                return BigDecimal.ONE;
            }
            case -67: {
                return BigDecimal.valueOf(this.bytes[this.offset++]);
            }
            case -68: {
                final int int16Value = (this.bytes[this.offset + 1] & 0xFF) + (this.bytes[this.offset] << 8);
                this.offset += 2;
                return BigDecimal.valueOf(int16Value);
            }
            case -65:
            case 72: {
                final int int32Value = getInt(this.bytes, this.offset);
                this.offset += 4;
                return BigDecimal.valueOf(int32Value);
            }
            case -74: {
                final float floatValue = (float)this.readInt32Value();
                return BigDecimal.valueOf((long)floatValue);
            }
            case -73: {
                final int int32Value = getInt(this.bytes, this.offset);
                this.offset += 4;
                final float floatValue2 = Float.intBitsToFloat(int32Value);
                return BigDecimal.valueOf((long)floatValue2);
            }
            case -75: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                final double doubleValue = Double.longBitsToDouble(JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
                return BigDecimal.valueOf((long)doubleValue);
            }
            case -66: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return BigDecimal.valueOf(JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
            }
            case -69: {
                final BigInteger bigInt = this.readBigInteger();
                return new BigDecimal(bigInt);
            }
            case -76: {
                final double doubleValue2 = (double)this.readInt64Value();
                return BigDecimal.valueOf((long)doubleValue2);
            }
            case 121: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.ISO_8859_1);
                this.offset += strlen;
                return TypeUtils.toBigDecimal(str);
            }
            case 124: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_16LE);
                this.offset += strlen;
                return TypeUtils.toBigDecimal(str);
            }
            case 122: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_8);
                this.offset += strlen;
                return TypeUtils.toBigDecimal(str);
            }
            default: {
                if (type >= -16 && type <= 47) {
                    return BigDecimal.valueOf(type);
                }
                if (type >= 48 && type <= 63) {
                    final int intValue = (type - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
                    return BigDecimal.valueOf(intValue);
                }
                if (type >= 64 && type <= 71) {
                    final int int3 = getInt3(this.bytes, this.offset, type);
                    this.offset += 2;
                    return BigDecimal.valueOf(int3);
                }
                if (type >= -40 && type <= -17) {
                    final int intValue = -8 + (type + 40);
                    return BigDecimal.valueOf(intValue);
                }
                if (type >= -56 && type <= -41) {
                    final int intValue = (type + 48 << 8) + (this.bytes[this.offset++] & 0xFF);
                    return BigDecimal.valueOf(intValue);
                }
                if (type >= -64 && type <= -57) {
                    final int intValue = (type + 60 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
                    return BigDecimal.valueOf(intValue);
                }
                if (type >= 73 && type <= 120) {
                    final int strlen = type - 73;
                    final String str = this.readFixedAsciiString(strlen);
                    this.offset += strlen;
                    return TypeUtils.toBigDecimal(str);
                }
                throw new JSONException("not support type :" + JSONB.typeName(type));
            }
        }
    }
    
    @Override
    public BigInteger readBigInteger() {
        final byte type = this.bytes[this.offset++];
        BigInteger bigInt;
        if (type == -70) {
            bigInt = BigInteger.valueOf(this.readInt64Value());
        }
        else if (type == -69) {
            final int len = this.readInt32Value();
            final byte[] bytes = new byte[len];
            System.arraycopy(this.bytes, this.offset, bytes, 0, len);
            this.offset += len;
            bigInt = new BigInteger(bytes);
        }
        else {
            bigInt = this.readBigInteger0(type);
        }
        return bigInt;
    }
    
    private BigInteger readBigInteger0(final byte type) {
        switch (type) {
            case -81: {
                return null;
            }
            case -80:
            case -78: {
                return BigInteger.ZERO;
            }
            case -79:
            case -77: {
                return BigInteger.ONE;
            }
            case -67: {
                return BigInteger.valueOf(this.bytes[this.offset++]);
            }
            case -68: {
                final int int16Value = (this.bytes[this.offset + 1] & 0xFF) + (this.bytes[this.offset] << 8);
                this.offset += 2;
                return BigInteger.valueOf(int16Value);
            }
            case -65:
            case 72: {
                final int int32Value = getInt(this.bytes, this.offset);
                this.offset += 4;
                return BigInteger.valueOf(int32Value);
            }
            case -74: {
                final float floatValue = (float)this.readInt32Value();
                return BigInteger.valueOf((long)floatValue);
            }
            case -73: {
                final int int32Value = getInt(this.bytes, this.offset);
                this.offset += 4;
                final float floatValue2 = Float.intBitsToFloat(int32Value);
                return BigInteger.valueOf((long)floatValue2);
            }
            case -75: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                final double doubleValue = Double.longBitsToDouble(JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
                return BigInteger.valueOf((long)doubleValue);
            }
            case -66: {
                final long int64Value = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return BigInteger.valueOf(JDKUtils.BIG_ENDIAN ? int64Value : Long.reverseBytes(int64Value));
            }
            case -111: {
                final int len = this.readInt32Value();
                final byte[] bytes = new byte[len];
                System.arraycopy(this.bytes, this.offset, bytes, 0, len);
                this.offset += len;
                return new BigInteger(bytes);
            }
            case -71: {
                final int scale = this.readInt32Value();
                final BigInteger unscaledValue = this.readBigInteger();
                BigDecimal decimal;
                if (scale == 0) {
                    decimal = new BigDecimal(unscaledValue);
                }
                else {
                    decimal = new BigDecimal(unscaledValue, scale);
                }
                return decimal.toBigInteger();
            }
            case -76: {
                final double doubleValue2 = (double)this.readInt64Value();
                return BigInteger.valueOf((long)doubleValue2);
            }
            case 121: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.ISO_8859_1);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str);
                }
                return TypeUtils.toBigDecimal(str).toBigInteger();
            }
            case 122: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_8);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str);
                }
                return TypeUtils.toBigDecimal(str).toBigInteger();
            }
            case 124: {
                final int strlen = this.readInt32Value();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_16LE);
                this.offset += strlen;
                if (str.indexOf(46) == -1) {
                    return new BigInteger(str);
                }
                return TypeUtils.toBigDecimal(str).toBigInteger();
            }
            default: {
                if (type >= -16 && type <= 47) {
                    return BigInteger.valueOf(type);
                }
                if (type >= 48 && type <= 63) {
                    final int intValue = (type - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
                    return BigInteger.valueOf(intValue);
                }
                if (type >= 64 && type <= 71) {
                    final int int3 = getInt3(this.bytes, this.offset, type);
                    this.offset += 2;
                    return BigInteger.valueOf(int3);
                }
                if (type >= -40 && type <= -17) {
                    final int intValue = -8 + (type + 40);
                    return BigInteger.valueOf(intValue);
                }
                if (type >= -56 && type <= -41) {
                    final int intValue = (type + 48 << 8) + (this.bytes[this.offset++] & 0xFF);
                    return BigInteger.valueOf(intValue);
                }
                if (type >= -64 && type <= -57) {
                    final int intValue = (type + 60 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
                    return BigInteger.valueOf(intValue);
                }
                if (type >= 73 && type <= 120) {
                    final int strlen = type - 73;
                    final String str = this.readFixedAsciiString(strlen);
                    this.offset += strlen;
                    return new BigInteger(str);
                }
                throw new JSONException("not support type :" + JSONB.typeName(type));
            }
        }
    }
    
    @Override
    public LocalDate readLocalDate() {
        final int type = this.bytes[this.offset];
        if (type == -87) {
            ++this.offset;
            final int year = (this.bytes[this.offset++] << 8) + (this.bytes[this.offset++] & 0xFF);
            final int month = this.bytes[this.offset++];
            final int dayOfMonth = this.bytes[this.offset++];
            return LocalDate.of(year, month, dayOfMonth);
        }
        return this.readLocalDate0(type);
    }
    
    private LocalDate readLocalDate0(final int type) {
        if (type == -88) {
            return this.readLocalDateTime().toLocalDate();
        }
        if (type == -86) {
            return this.readZonedDateTime().toLocalDate();
        }
        if (type < 73 || type > 120) {
            if (type == 122 || type == 121) {
                this.strtype = (byte)type;
                ++this.offset;
                switch (this.strlen = this.readLength()) {
                    case 8: {
                        return this.readLocalDate8();
                    }
                    case 9: {
                        return this.readLocalDate9();
                    }
                    case 10: {
                        return this.readLocalDate10();
                    }
                    case 11: {
                        return this.readLocalDate11();
                    }
                }
            }
            throw new JSONException("not support type : " + JSONB.typeName((byte)type));
        }
        final int len = this.getStringLength();
        switch (len) {
            case 8: {
                return this.readLocalDate8();
            }
            case 9: {
                return this.readLocalDate9();
            }
            case 10: {
                return this.readLocalDate10();
            }
            case 11: {
                return this.readLocalDate11();
            }
            default: {
                if (this.bytes[this.offset + len] == 90) {
                    final ZonedDateTime zdt = this.readZonedDateTime();
                    return zdt.toLocalDate();
                }
                throw new JSONException("TODO : " + len + ", " + this.readString());
            }
        }
    }
    
    @Override
    public LocalDateTime readLocalDateTime() {
        final int type = this.bytes[this.offset];
        if (type == -88) {
            ++this.offset;
            final int year = (this.bytes[this.offset++] << 8) + (this.bytes[this.offset++] & 0xFF);
            final int month = this.bytes[this.offset++];
            final int dayOfMonth = this.bytes[this.offset++];
            final int hour = this.bytes[this.offset++];
            final int minute = this.bytes[this.offset++];
            final int second = this.bytes[this.offset++];
            final int nano = this.readInt32Value();
            return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nano);
        }
        return this.readLocalDateTime0(type);
    }
    
    private LocalDateTime readLocalDateTime0(final int type) {
        if (type == -86) {
            return this.readZonedDateTime().toLocalDateTime();
        }
        if (type >= 73 && type <= 120) {
            final int len = this.getStringLength();
            switch (len) {
                case 8: {
                    final LocalDate localDate = this.readLocalDate8();
                    return (localDate == null) ? null : LocalDateTime.of(localDate, LocalTime.MIN);
                }
                case 9: {
                    final LocalDate localDate = this.readLocalDate9();
                    return (localDate == null) ? null : LocalDateTime.of(localDate, LocalTime.MIN);
                }
                case 10: {
                    final LocalDate localDate = this.readLocalDate10();
                    return (localDate == null) ? null : LocalDateTime.of(localDate, LocalTime.MIN);
                }
                case 11: {
                    final LocalDate localDate = this.readLocalDate11();
                    return (localDate == null) ? null : LocalDateTime.of(localDate, LocalTime.MIN);
                }
                case 16: {
                    return this.readLocalDateTime16();
                }
                case 17: {
                    return this.readLocalDateTime17();
                }
                case 18: {
                    return this.readLocalDateTime18();
                }
                case 19: {
                    return this.readLocalDateTime19();
                }
                case 20: {
                    return this.readLocalDateTime20();
                }
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29: {
                    final LocalDateTime ldt = this.readLocalDateTimeX(len);
                    if (ldt != null) {
                        return ldt;
                    }
                    final ZonedDateTime zdt = this.readZonedDateTimeX(len);
                    if (zdt != null) {
                        return zdt.toLocalDateTime();
                    }
                    break;
                }
            }
            throw new JSONException("TODO : " + len + ", " + this.readString());
        }
        throw new JSONException("not support type : " + JSONB.typeName((byte)type));
    }
    
    @Override
    protected LocalDateTime readLocalDateTime12() {
        final LocalDateTime ldt;
        if (this.bytes[this.offset] != 85 || (ldt = DateUtils.parseLocalDateTime12(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 13;
        return ldt;
    }
    
    @Override
    protected LocalDateTime readLocalDateTime14() {
        final LocalDateTime ldt;
        if (this.bytes[this.offset] != 87 || (ldt = DateUtils.parseLocalDateTime14(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 15;
        return ldt;
    }
    
    @Override
    protected LocalDateTime readLocalDateTime16() {
        final LocalDateTime ldt;
        if (this.bytes[this.offset] != 89 || (ldt = DateUtils.parseLocalDateTime16(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 17;
        return ldt;
    }
    
    @Override
    protected LocalDateTime readLocalDateTime17() {
        final LocalDateTime ldt;
        if (this.bytes[this.offset] != 90 || (ldt = DateUtils.parseLocalDateTime17(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 18;
        return ldt;
    }
    
    @Override
    protected LocalTime readLocalTime10() {
        final LocalTime time;
        if (this.bytes[this.offset] != 83 || (time = DateUtils.parseLocalTime10(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 11;
        return time;
    }
    
    @Override
    protected LocalTime readLocalTime11() {
        final LocalTime time;
        if (this.bytes[this.offset] != 84 || (time = DateUtils.parseLocalTime11(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 12;
        return time;
    }
    
    @Override
    protected ZonedDateTime readZonedDateTimeX(final int len) {
        this.type = this.bytes[this.offset];
        if (this.type < 73 || this.type > 120) {
            throw new JSONException("date only support string input");
        }
        final ZonedDateTime ldt;
        if (len < 19 || (ldt = DateUtils.parseZonedDateTime(this.bytes, this.offset + 1, len, this.context.zoneId)) == null) {
            throw new JSONException("illegal LocalDateTime string : " + this.readString());
        }
        this.offset += len + 1;
        return ldt;
    }
    
    @Override
    public void skipLineComment() {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public LocalTime readLocalTime() {
        final int type = this.bytes[this.offset];
        if (type == -89) {
            ++this.offset;
            final int hour = this.bytes[this.offset++];
            final int minute = this.bytes[this.offset++];
            final int second = this.bytes[this.offset++];
            final int nano = this.readInt32Value();
            return LocalTime.of(hour, minute, second, nano);
        }
        if (type < 73 || type > 120) {
            throw new UnsupportedOperationException();
        }
        final int len = this.getStringLength();
        switch (len) {
            case 5: {
                return this.readLocalTime5();
            }
            case 8: {
                return this.readLocalTime8();
            }
            case 10: {
                return this.readLocalTime10();
            }
            case 11: {
                return this.readLocalTime11();
            }
            case 12: {
                return this.readLocalTime12();
            }
            case 18: {
                return this.readLocalTime18();
            }
            default: {
                throw new JSONException("not support len : " + len);
            }
        }
    }
    
    @Override
    public Instant readInstant() {
        final int type = this.bytes[this.offset++];
        switch (type) {
            case -82: {
                return Instant.ofEpochSecond(this.readInt64Value(), this.readInt32Value());
            }
            case -83: {
                final long minutes = getInt(this.bytes, this.offset);
                this.offset += 4;
                return Instant.ofEpochSecond(minutes * 60L, 0L);
            }
            case -84: {
                final long seconds = getInt(this.bytes, this.offset);
                this.offset += 4;
                return Instant.ofEpochSecond(seconds, 0L);
            }
            case -85:
            case -66: {
                final long millis = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                return Instant.ofEpochMilli(JDKUtils.BIG_ENDIAN ? millis : Long.reverseBytes(millis));
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    @Override
    public OffsetDateTime readOffsetDateTime() {
        return this.readZonedDateTime().toOffsetDateTime();
    }
    
    @Override
    public ZonedDateTime readZonedDateTime() {
        final int type = this.bytes[this.offset++];
        switch (type) {
            case -82: {
                final long second = this.readInt64Value();
                final int nano = this.readInt32Value();
                final Instant instant = Instant.ofEpochSecond(second, nano);
                return ZonedDateTime.ofInstant(instant, DateUtils.DEFAULT_ZONE_ID);
            }
            case -83: {
                final long minutes = getInt(this.bytes, this.offset);
                this.offset += 4;
                final Instant instant2 = Instant.ofEpochSecond(minutes * 60L);
                return ZonedDateTime.ofInstant(instant2, DateUtils.DEFAULT_ZONE_ID);
            }
            case -84: {
                final long seconds = getInt(this.bytes, this.offset);
                this.offset += 4;
                final Instant instant2 = Instant.ofEpochSecond(seconds);
                return ZonedDateTime.ofInstant(instant2, DateUtils.DEFAULT_ZONE_ID);
            }
            case -87: {
                final int year = (this.bytes[this.offset++] << 8) + (this.bytes[this.offset++] & 0xFF);
                final byte month = this.bytes[this.offset++];
                final byte dayOfMonth = this.bytes[this.offset++];
                final LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
                return ZonedDateTime.of(localDate, LocalTime.MIN, DateUtils.DEFAULT_ZONE_ID);
            }
            case -88: {
                final int year = (this.bytes[this.offset++] << 8) + (this.bytes[this.offset++] & 0xFF);
                final byte month = this.bytes[this.offset++];
                final byte dayOfMonth = this.bytes[this.offset++];
                final byte hour = this.bytes[this.offset++];
                final byte minute = this.bytes[this.offset++];
                final byte second2 = this.bytes[this.offset++];
                final int nano2 = this.readInt32Value();
                final LocalDateTime ldt = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second2, nano2);
                return ZonedDateTime.of(ldt, DateUtils.DEFAULT_ZONE_ID);
            }
            case -85:
            case -66: {
                final long millis = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                this.offset += 8;
                final Instant instant2 = Instant.ofEpochMilli(JDKUtils.BIG_ENDIAN ? millis : Long.reverseBytes(millis));
                return ZonedDateTime.ofInstant(instant2, DateUtils.DEFAULT_ZONE_ID);
            }
            case -86: {
                final int year = (this.bytes[this.offset++] << 8) + (this.bytes[this.offset++] & 0xFF);
                final int month2 = this.bytes[this.offset++];
                final int dayOfMonth2 = this.bytes[this.offset++];
                final int hour2 = this.bytes[this.offset++];
                final int minute2 = this.bytes[this.offset++];
                final int second3 = this.bytes[this.offset++];
                final int nano2 = this.readInt32Value();
                final LocalDateTime ldt = LocalDateTime.of(year, month2, dayOfMonth2, hour2, minute2, second3, nano2);
                final long zoneIdHash = this.readValueHashCode();
                final long SHANGHAI_ZONE_ID_HASH = -4800907791268808639L;
                ZoneId zoneId;
                if (zoneIdHash == -4800907791268808639L) {
                    zoneId = DateUtils.SHANGHAI_ZONE_ID;
                }
                else {
                    final String zoneIdStr = this.getString();
                    final ZoneId contextZoneId = this.context.getZoneId();
                    if (contextZoneId.getId().equals(zoneIdStr)) {
                        zoneId = contextZoneId;
                    }
                    else {
                        zoneId = DateUtils.getZoneId(zoneIdStr, DateUtils.SHANGHAI_ZONE_ID);
                    }
                }
                return ZonedDateTime.ofLocal(ldt, zoneId, null);
            }
            default: {
                if (type >= 73 && type <= 120) {
                    --this.offset;
                    return this.readZonedDateTimeX(type - 73);
                }
                throw new JSONException("type not support : " + JSONB.typeName((byte)type));
            }
        }
    }
    
    @Override
    public UUID readUUID() {
        final byte type = this.bytes[this.offset++];
        switch (type) {
            case -81: {
                return null;
            }
            case -111: {
                final int len = this.readLength();
                if (len != 16) {
                    throw new JSONException("uuid not support " + len);
                }
                final long msb = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset);
                final long lsb = JDKUtils.UNSAFE.getLong(this.bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + this.offset + 8L);
                this.offset += 16;
                return new UUID(JDKUtils.BIG_ENDIAN ? msb : Long.reverseBytes(msb), JDKUtils.BIG_ENDIAN ? lsb : Long.reverseBytes(lsb));
            }
            case 105: {
                long hi = 0L;
                for (int i = 0; i < 16; ++i) {
                    hi = (hi << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + i] - 48];
                }
                long lo = 0L;
                for (int j = 16; j < 32; ++j) {
                    lo = (lo << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + j] - 48];
                }
                this.offset += 32;
                return new UUID(hi, lo);
            }
            case 109: {
                final byte ch1 = this.bytes[this.offset + 8];
                final byte ch2 = this.bytes[this.offset + 13];
                final byte ch3 = this.bytes[this.offset + 18];
                final byte ch4 = this.bytes[this.offset + 23];
                if (ch1 == 45 && ch2 == 45 && ch3 == 45 && ch4 == 45) {
                    long hi2 = 0L;
                    for (int k = 0; k < 8; ++k) {
                        hi2 = (hi2 << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + k] - 48];
                    }
                    for (int k = 9; k < 13; ++k) {
                        hi2 = (hi2 << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + k] - 48];
                    }
                    for (int k = 14; k < 18; ++k) {
                        hi2 = (hi2 << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + k] - 48];
                    }
                    long lo2 = 0L;
                    for (int l = 19; l < 23; ++l) {
                        lo2 = (lo2 << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + l] - 48];
                    }
                    for (int l = 24; l < 36; ++l) {
                        lo2 = (lo2 << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + l] - 48];
                    }
                    this.offset += 36;
                    return new UUID(hi2, lo2);
                }
                throw new JSONException("Invalid UUID string:  " + new String(this.bytes, this.offset, 36, StandardCharsets.ISO_8859_1));
            }
            case 121:
            case 122: {
                final int strlen = this.readLength();
                if (strlen == 32) {
                    long hi3 = 0L;
                    for (int m = 0; m < 16; ++m) {
                        hi3 = (hi3 << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + m] - 48];
                    }
                    long lo3 = 0L;
                    for (int i2 = 16; i2 < 32; ++i2) {
                        lo3 = (lo3 << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + i2] - 48];
                    }
                    this.offset += 32;
                    return new UUID(hi3, lo3);
                }
                if (strlen == 36) {
                    final byte ch5 = this.bytes[this.offset + 8];
                    final byte ch6 = this.bytes[this.offset + 13];
                    final byte ch7 = this.bytes[this.offset + 18];
                    final byte ch8 = this.bytes[this.offset + 23];
                    if (ch5 == 45 && ch6 == 45 && ch7 == 45 && ch8 == 45) {
                        long hi4 = 0L;
                        for (int i3 = 0; i3 < 8; ++i3) {
                            hi4 = (hi4 << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + i3] - 48];
                        }
                        for (int i3 = 9; i3 < 13; ++i3) {
                            hi4 = (hi4 << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + i3] - 48];
                        }
                        for (int i3 = 14; i3 < 18; ++i3) {
                            hi4 = (hi4 << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + i3] - 48];
                        }
                        long lo4 = 0L;
                        for (int i4 = 19; i4 < 23; ++i4) {
                            lo4 = (lo4 << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + i4] - 48];
                        }
                        for (int i4 = 24; i4 < 36; ++i4) {
                            lo4 = (lo4 << 4) + JSONFactory.UUID_VALUES[this.bytes[this.offset + i4] - 48];
                        }
                        this.offset += 36;
                        return new UUID(hi4, lo4);
                    }
                }
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_8);
                this.offset += strlen;
                throw new JSONException("Invalid UUID string:  " + str);
            }
            default: {
                throw new JSONException("type not support : " + JSONB.typeName(type));
            }
        }
    }
    
    @Override
    public Boolean readBool() {
        final byte type = this.bytes[this.offset++];
        if (type == -81) {
            return null;
        }
        if (type == -79) {
            return true;
        }
        if (type == -80) {
            return false;
        }
        return this.readBoolValue0(type);
    }
    
    @Override
    public boolean readBoolValue() {
        this.wasNull = false;
        final byte type = this.bytes[this.offset++];
        return type == -79 || (type != -80 && this.readBoolValue0(type));
    }
    
    private boolean readBoolValue0(final byte type) {
        final byte[] bytes = this.bytes;
        switch (type) {
            case 1: {
                return true;
            }
            case 0: {
                return false;
            }
            case -81: {
                if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) != 0x0L) {
                    throw new JSONException(this.info("long value not support input null"));
                }
                this.wasNull = true;
                return false;
            }
            case 74: {
                if (bytes[this.offset] == 49 || bytes[this.offset] == 89) {
                    ++this.offset;
                    return true;
                }
                if (bytes[this.offset] == 48 || bytes[this.offset] == 78) {
                    ++this.offset;
                    return false;
                }
            }
            case 77: {
                if (bytes[this.offset] == 116 && bytes[this.offset + 1] == 114 && bytes[this.offset + 2] == 117 && bytes[this.offset + 3] == 101) {
                    this.offset += 4;
                    return true;
                }
                if (bytes[this.offset] == 84 && bytes[this.offset + 1] == 82 && bytes[this.offset + 2] == 85 && bytes[this.offset + 3] == 69) {
                    this.offset += 4;
                    return true;
                }
            }
            case 78: {
                if (bytes[this.offset] == 102 && bytes[this.offset + 1] == 97 && bytes[this.offset + 2] == 108 && bytes[this.offset + 3] == 115 && bytes[this.offset + 4] == 101) {
                    this.offset += 5;
                    return false;
                }
                if (bytes[this.offset] == 70 && bytes[this.offset + 1] == 65 && bytes[this.offset + 2] == 76 && bytes[this.offset + 3] == 83 && bytes[this.offset + 4] == 69) {
                    this.offset += 5;
                    return false;
                }
            }
            case 121:
            case 122: {
                this.strlen = this.readLength();
                if (this.strlen == 1) {
                    if (bytes[this.offset] == 89) {
                        ++this.offset;
                        return true;
                    }
                    if (bytes[this.offset] == 78) {
                        ++this.offset;
                        return true;
                    }
                }
                else {
                    if (this.strlen == 4 && bytes[this.offset] == 116 && bytes[this.offset + 1] == 114 && bytes[this.offset + 2] == 117 && bytes[this.offset + 3] == 101) {
                        this.offset += 4;
                        return true;
                    }
                    if (this.strlen == 5) {
                        if (bytes[this.offset] == 102 && bytes[this.offset + 1] == 97 && bytes[this.offset + 2] == 108 && bytes[this.offset + 3] == 115 && bytes[this.offset + 4] == 101) {
                            this.offset += 5;
                            return false;
                        }
                        if (bytes[this.offset] == 70 && bytes[this.offset + 1] == 65 && bytes[this.offset + 2] == 76 && bytes[this.offset + 3] == 83 && bytes[this.offset + 4] == 69) {
                            this.offset += 5;
                            return false;
                        }
                    }
                }
                final String str = new String(bytes, this.offset, this.strlen, StandardCharsets.ISO_8859_1);
                this.offset += this.strlen;
                throw new JSONException("not support input " + str);
            }
            case 123:
            case 124:
            case 125: {
                this.strlen = this.readLength();
                final byte[] chars = new byte[this.strlen];
                System.arraycopy(bytes, this.offset, chars, 0, this.strlen);
                final Charset charset = (type == 125) ? StandardCharsets.UTF_16BE : ((type == 124) ? StandardCharsets.UTF_16LE : StandardCharsets.UTF_16);
                final String str2 = new String(chars, charset);
                this.offset += this.strlen;
                final String s = str2;
                switch (s) {
                    case "0":
                    case "N":
                    case "false":
                    case "FALSE": {
                        return false;
                    }
                    case "1":
                    case "Y":
                    case "true":
                    case "TRUE": {
                        return true;
                    }
                    default: {
                        throw new JSONException("not support input " + str2);
                    }
                }
                break;
            }
            default: {
                throw new JSONException("not support type : " + JSONB.typeName(type));
            }
        }
    }
    
    @Override
    public boolean nextIfMatch(final byte type) {
        if (this.bytes[this.offset] == type) {
            ++this.offset;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean nextIfMatchTypedAny() {
        if (this.bytes[this.offset] == -110) {
            ++this.offset;
            return true;
        }
        return false;
    }
    
    @Override
    protected int getStringLength() {
        this.type = this.bytes[this.offset];
        if (this.type >= 73 && this.type < 120) {
            return this.type - 73;
        }
        throw new UnsupportedOperationException();
    }
    
    public LocalDate readLocalDate8() {
        final LocalDate ldt;
        if (this.bytes[this.offset] != 81 || (ldt = DateUtils.parseLocalDate8(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 9;
        return ldt;
    }
    
    public LocalDate readLocalDate9() {
        final LocalDate ldt;
        if (this.bytes[this.offset] != 82 || (ldt = DateUtils.parseLocalDate9(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 10;
        return ldt;
    }
    
    @Override
    protected LocalDate readLocalDate10() {
        LocalDate ldt;
        if ((this.strtype == 121 || this.strtype == 122) && this.strlen == 10) {
            ldt = DateUtils.parseLocalDate10(this.bytes, this.offset);
        }
        else if (this.bytes[this.offset] != 83 || (ldt = DateUtils.parseLocalDate10(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 11;
        return ldt;
    }
    
    @Override
    protected LocalDate readLocalDate11() {
        LocalDate ldt;
        if ((this.strtype == 121 || this.strtype == 122) && this.strlen == 11) {
            ldt = DateUtils.parseLocalDate11(this.bytes, this.offset);
        }
        else if (this.bytes[this.offset] != 84 || (ldt = DateUtils.parseLocalDate11(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 12;
        return ldt;
    }
    
    @Override
    protected LocalTime readLocalTime5() {
        final LocalTime time;
        if (this.bytes[this.offset] != 78 || (time = DateUtils.parseLocalTime5(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 6;
        return time;
    }
    
    @Override
    protected LocalTime readLocalTime8() {
        final LocalTime time;
        if (this.bytes[this.offset] != 81 || (time = DateUtils.parseLocalTime8(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 9;
        return time;
    }
    
    @Override
    protected LocalTime readLocalTime9() {
        final LocalTime time;
        if (this.bytes[this.offset] != 81 || (time = DateUtils.parseLocalTime8(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 10;
        return time;
    }
    
    @Override
    protected LocalTime readLocalTime12() {
        final LocalTime time;
        if (this.bytes[this.offset] != 85 || (time = DateUtils.parseLocalTime12(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 13;
        return time;
    }
    
    @Override
    protected LocalTime readLocalTime18() {
        final LocalTime time;
        if (this.bytes[this.offset] != 91 || (time = DateUtils.parseLocalTime18(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 19;
        return time;
    }
    
    @Override
    protected LocalDateTime readLocalDateTime18() {
        final LocalDateTime ldt;
        if (this.bytes[this.offset] != 91 || (ldt = DateUtils.parseLocalDateTime18(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 19;
        return ldt;
    }
    
    @Override
    protected LocalDateTime readLocalDateTime20() {
        final LocalDateTime ldt;
        if (this.bytes[this.offset] != 93 || (ldt = DateUtils.parseLocalDateTime20(this.bytes, this.offset + 1)) == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 21;
        return ldt;
    }
    
    @Override
    public long readMillis19() {
        if (this.bytes[this.offset] != 92) {
            throw new JSONException("date only support string input");
        }
        final long millis = DateUtils.parseMillis19(this.bytes, this.offset + 1, this.context.zoneId);
        this.offset += 20;
        return millis;
    }
    
    @Override
    protected LocalDateTime readLocalDateTime19() {
        this.type = this.bytes[this.offset];
        if (this.type != 92) {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt = DateUtils.parseLocalDateTime19(this.bytes, this.offset + 1);
        if (ldt == null) {
            throw new JSONException("date only support string input");
        }
        this.offset += 20;
        return ldt;
    }
    
    @Override
    protected LocalDateTime readLocalDateTimeX(final int len) {
        this.type = this.bytes[this.offset];
        if (this.type < 73 || this.type > 120) {
            throw new JSONException("date only support string input");
        }
        final LocalDateTime ldt;
        if (len < 21 || len > 29 || (ldt = DateUtils.parseLocalDateTimeX(this.bytes, this.offset + 1, len)) == null) {
            throw new JSONException("illegal LocalDateTime string : " + this.readString());
        }
        this.offset += len + 1;
        return ldt;
    }
    
    @Override
    public String readPattern() {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public boolean nextIfMatchIdent(final char c0, final char c1, final char c2) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public long readFieldNameHashCodeUnquote() {
        return this.readFieldNameHashCode();
    }
    
    @Override
    public boolean nextIfSet() {
        return false;
    }
    
    @Override
    public boolean nextIfInfinity() {
        return false;
    }
    
    @Override
    public boolean nextIfMatchIdent(final char c0, final char c1, final char c2, final char c3) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public boolean nextIfMatchIdent(final char c0, final char c1, final char c2, final char c3, final char c4) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public boolean nextIfMatchIdent(final char c0, final char c1, final char c2, final char c3, final char c4, final char c5) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public SavePoint mark() {
        return new SavePoint(this.offset, this.type);
    }
    
    @Override
    public void reset(final SavePoint savePoint) {
        this.offset = savePoint.offset;
        this.type = (byte)savePoint.current;
    }
    
    @Override
    public void close() {
        final byte[] valueBytes = this.valueBytes;
        if (valueBytes != null && valueBytes.length < 1048576) {
            JSONFactory.BYTES_UPDATER.lazySet(this.cacheItem, valueBytes);
        }
    }
    
    @Override
    public boolean isEnd() {
        return this.offset >= this.end;
    }
    
    static int getInt(final byte[] bytes, final int offset) {
        final int int32Value = JDKUtils.UNSAFE.getInt(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + offset);
        return JDKUtils.BIG_ENDIAN ? int32Value : Integer.reverseBytes(int32Value);
    }
    
    static {
        BASE = JDKUtils.UNSAFE.arrayBaseOffset(byte[].class);
        SHANGHAI_ZONE_ID_NAME_BYTES = JSONB.toBytes("Asia/Shanghai");
    }
}
