// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import com.alibaba.fastjson2.util.JDKUtils;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.Charset;

final class JSONBDump
{
    static Charset GB18030;
    final byte[] bytes;
    final boolean raw;
    int offset;
    byte type;
    int strlen;
    byte strtype;
    int strBegin;
    String lastReference;
    final JSONWriter jsonWriter;
    final SymbolTable symbolTable;
    final Map<Integer, String> symbols;
    
    public JSONBDump(final byte[] bytes, final boolean raw) {
        this.symbols = new HashMap<Integer, String>();
        this.bytes = bytes;
        this.raw = raw;
        this.jsonWriter = JSONWriter.ofPretty();
        this.symbolTable = null;
        this.dumpAny();
    }
    
    public JSONBDump(final byte[] bytes, final SymbolTable symbolTable, final boolean raw) {
        this.symbols = new HashMap<Integer, String>();
        this.bytes = bytes;
        this.raw = raw;
        this.symbolTable = symbolTable;
        this.jsonWriter = JSONWriter.ofPretty();
        this.dumpAny();
    }
    
    private void dumpAny() {
        if (this.offset >= this.bytes.length) {
            return;
        }
        switch (this.type = this.bytes[this.offset++]) {
            case -81: {
                this.jsonWriter.writeNull();
            }
            case -79: {
                this.jsonWriter.writeBool(true);
            }
            case -80: {
                this.jsonWriter.writeBool(false);
            }
            case -78: {
                this.jsonWriter.writeDouble(0.0);
            }
            case -77: {
                this.jsonWriter.writeDouble(1.0);
            }
            case -67: {
                this.jsonWriter.writeInt8(this.bytes[this.offset++]);
            }
            case -68: {
                this.jsonWriter.writeInt16((short)((this.bytes[this.offset++] << 8) + (this.bytes[this.offset++] & 0xFF)));
            }
            case -84:
            case -83:
            case 72: {
                final int int32Value = (this.bytes[this.offset + 3] & 0xFF) + ((this.bytes[this.offset + 2] & 0xFF) << 8) + ((this.bytes[this.offset + 1] & 0xFF) << 16) + (this.bytes[this.offset] << 24);
                this.offset += 4;
                this.jsonWriter.writeInt32(int32Value);
            }
            case -65: {
                final int int32Value = (this.bytes[this.offset + 3] & 0xFF) + ((this.bytes[this.offset + 2] & 0xFF) << 8) + ((this.bytes[this.offset + 1] & 0xFF) << 16) + (this.bytes[this.offset] << 24);
                this.offset += 4;
                this.jsonWriter.writeInt64(int32Value);
            }
            case -85:
            case -66: {
                final long int64Value = ((long)this.bytes[this.offset + 7] & 0xFFL) + (((long)this.bytes[this.offset + 6] & 0xFFL) << 8) + (((long)this.bytes[this.offset + 5] & 0xFFL) << 16) + (((long)this.bytes[this.offset + 4] & 0xFFL) << 24) + (((long)this.bytes[this.offset + 3] & 0xFFL) << 32) + (((long)this.bytes[this.offset + 2] & 0xFFL) << 40) + (((long)this.bytes[this.offset + 1] & 0xFFL) << 48) + ((long)this.bytes[this.offset] << 56);
                this.offset += 8;
                this.jsonWriter.writeInt64(int64Value);
            }
            case -69: {
                final int len = this.readInt32Value();
                final byte[] bytes = new byte[len];
                System.arraycopy(this.bytes, this.offset, bytes, 0, len);
                this.offset += len;
                this.jsonWriter.writeBigInt(new BigInteger(bytes));
            }
            case -70: {
                this.jsonWriter.writeInt64(this.readInt64Value());
            }
            case -73: {
                final int int32Value = (this.bytes[this.offset + 3] & 0xFF) + ((this.bytes[this.offset + 2] & 0xFF) << 8) + ((this.bytes[this.offset + 1] & 0xFF) << 16) + (this.bytes[this.offset] << 24);
                this.offset += 4;
                this.jsonWriter.writeFloat(Float.intBitsToFloat(int32Value));
            }
            case -74: {
                this.jsonWriter.writeFloat((float)this.readInt32Value());
            }
            case -75: {
                final long int64Value = ((long)this.bytes[this.offset + 7] & 0xFFL) + (((long)this.bytes[this.offset + 6] & 0xFFL) << 8) + (((long)this.bytes[this.offset + 5] & 0xFFL) << 16) + (((long)this.bytes[this.offset + 4] & 0xFFL) << 24) + (((long)this.bytes[this.offset + 3] & 0xFFL) << 32) + (((long)this.bytes[this.offset + 2] & 0xFFL) << 40) + (((long)this.bytes[this.offset + 1] & 0xFFL) << 48) + ((long)this.bytes[this.offset] << 56);
                this.offset += 8;
                this.jsonWriter.writeDouble(Double.longBitsToDouble(int64Value));
            }
            case -76: {
                this.jsonWriter.writeDouble((double)this.readInt64Value());
            }
            case 122: {
                final int strlen = this.readLength();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_8);
                this.offset += strlen;
                this.jsonWriter.writeString(str);
            }
            case -112: {
                final int intValue = this.readInt32Value();
                this.jsonWriter.writeChar((char)intValue);
            }
            case 123: {
                final int strlen = this.readLength();
                final String str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_16);
                this.offset += strlen;
                this.jsonWriter.writeString(str);
            }
            case 124: {
                final int strlen = this.readLength();
                String str;
                if (JDKUtils.STRING_CREATOR_JDK11 != null && !JDKUtils.BIG_ENDIAN) {
                    final byte[] chars = new byte[strlen];
                    System.arraycopy(this.bytes, this.offset, chars, 0, strlen);
                    str = JDKUtils.STRING_CREATOR_JDK11.apply(chars, JDKUtils.UTF16);
                }
                else {
                    str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_16LE);
                }
                this.offset += strlen;
                this.jsonWriter.writeString(str);
            }
            case 125: {
                final int strlen = this.readLength();
                String str;
                if (JDKUtils.STRING_CREATOR_JDK11 != null && JDKUtils.BIG_ENDIAN) {
                    final byte[] chars = new byte[strlen];
                    System.arraycopy(this.bytes, this.offset, chars, 0, strlen);
                    str = JDKUtils.STRING_CREATOR_JDK11.apply(chars, JDKUtils.UTF16);
                }
                else {
                    str = new String(this.bytes, this.offset, strlen, StandardCharsets.UTF_16BE);
                }
                this.offset += strlen;
                this.jsonWriter.writeString(str);
            }
            case 126: {
                if (JSONBDump.GB18030 == null) {
                    JSONBDump.GB18030 = Charset.forName("GB18030");
                }
                final int strlen = this.readLength();
                final String str = new String(this.bytes, this.offset, strlen, JSONBDump.GB18030);
                this.offset += strlen;
                this.jsonWriter.writeString(str);
            }
            case Byte.MAX_VALUE: {
                if (this.isInt()) {
                    final int symbol = this.readInt32Value();
                    if (this.raw) {
                        this.jsonWriter.writeString("#" + symbol);
                    }
                    else {
                        final String name = this.getString(symbol);
                        this.jsonWriter.writeString(name);
                    }
                }
                else {
                    final String name = this.readString();
                    final int symbol = this.readInt32Value();
                    this.symbols.put(symbol, name);
                    if (this.raw) {
                        this.jsonWriter.writeString(name + "#" + symbol);
                    }
                    else {
                        this.jsonWriter.writeString(name);
                    }
                }
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
                this.jsonWriter.writeDecimal(decimal, 0L, null);
            }
            case -72: {
                this.jsonWriter.writeDecimal(BigDecimal.valueOf(this.readInt64Value()), 0L, null);
            }
            case -110: {
                final boolean isInt = this.isInt();
                String typeName = null;
                int symbol2;
                if (isInt) {
                    symbol2 = this.readInt32Value();
                }
                else {
                    typeName = this.readString();
                    symbol2 = this.readInt32Value();
                    this.symbols.put(symbol2, typeName);
                }
                if (!this.raw && this.bytes[this.offset] == -90) {
                    if (typeName == null) {
                        typeName = this.getString(symbol2);
                    }
                    ++this.offset;
                    this.dumpObject(typeName);
                    return;
                }
                this.jsonWriter.startObject();
                this.jsonWriter.writeName("@type");
                this.jsonWriter.writeColon();
                if (typeName == null) {
                    if (symbol2 < 0) {
                        if (this.raw) {
                            this.jsonWriter.writeString("#" + symbol2);
                        }
                        else {
                            final String name2 = this.symbolTable.getName(-symbol2);
                            this.jsonWriter.writeString(name2);
                        }
                    }
                    else {
                        this.jsonWriter.writeString("#" + symbol2);
                    }
                }
                else if (this.raw) {
                    this.jsonWriter.writeString(typeName + "#" + symbol2);
                }
                else {
                    this.jsonWriter.writeString(typeName);
                }
                this.jsonWriter.writeName("@value");
                this.jsonWriter.writeColon();
                this.dumpAny();
                this.jsonWriter.endObject();
            }
            case -111: {
                final int len = this.readInt32Value();
                final byte[] bytes = new byte[len];
                System.arraycopy(this.bytes, this.offset, bytes, 0, len);
                this.offset += len;
                this.jsonWriter.writeBinary(bytes);
            }
            case -109: {
                this.dumpReference();
                break;
            }
            case -88: {
                final int year = (this.bytes[this.offset++] << 8) + (this.bytes[this.offset++] & 0xFF);
                final int month = this.bytes[this.offset++];
                final int dayOfMonth = this.bytes[this.offset++];
                final int hour = this.bytes[this.offset++];
                final int minute = this.bytes[this.offset++];
                final int second = this.bytes[this.offset++];
                final int nano = this.readInt32Value();
                final LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nano);
                this.jsonWriter.writeLocalDateTime(localDateTime);
                break;
            }
            case -87: {
                final int year = (this.bytes[this.offset++] << 8) + (this.bytes[this.offset++] & 0xFF);
                final int month = this.bytes[this.offset++];
                final int dayOfMonth = this.bytes[this.offset++];
                final LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
                this.jsonWriter.writeLocalDate(localDate);
                break;
            }
            case -82: {
                final long epochSeconds = this.readInt64Value();
                final int nano2 = this.readInt32Value();
                this.jsonWriter.writeInstant(Instant.ofEpochSecond(epochSeconds, nano2));
                break;
            }
            case -90: {
                this.dumpObject(null);
                break;
            }
            default: {
                if (this.type >= -16 && this.type <= 47) {
                    this.jsonWriter.writeInt32(this.type);
                    return;
                }
                if (this.type >= -40 && this.type <= -17) {
                    final long value = -8 + (this.type + 40);
                    this.jsonWriter.writeInt64(value);
                    return;
                }
                if (this.type >= 48 && this.type <= 63) {
                    final int value2 = (this.type - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
                    this.jsonWriter.writeInt32(value2);
                    return;
                }
                if (this.type >= 64 && this.type <= 71) {
                    final int value2 = (this.type - 68 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
                    this.jsonWriter.writeInt32(value2);
                    return;
                }
                if (this.type >= -56 && this.type <= -41) {
                    final int value2 = (this.type + 48 << 8) + (this.bytes[this.offset++] & 0xFF);
                    this.jsonWriter.writeInt32(value2);
                    return;
                }
                if (this.type >= -64 && this.type <= -57) {
                    final int value2 = (this.type + 60 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
                    this.jsonWriter.writeInt64(value2);
                    return;
                }
                if (this.type >= -108 && this.type <= -92) {
                    this.dumpArray();
                    return;
                }
                if (this.type < 73) {
                    throw new JSONException("not support type : " + JSONB.typeName(this.type) + ", offset " + this.offset);
                }
                this.strlen = ((this.type == 121) ? this.readLength() : (this.type - 73));
                if (this.strlen < 0) {
                    this.jsonWriter.writeRaw("{\"$symbol\":");
                    this.jsonWriter.writeInt32(this.strlen);
                    this.jsonWriter.writeRaw("}");
                    return;
                }
                final String str2 = new String(this.bytes, this.offset, this.strlen, StandardCharsets.ISO_8859_1);
                this.offset += this.strlen;
                this.jsonWriter.writeString(str2);
            }
        }
    }
    
    private void dumpArray() {
        final int len = (this.type == -92) ? this.readLength() : (this.type + 108);
        if (len == 0) {
            this.jsonWriter.writeRaw("[]");
            return;
        }
        if (len == 1) {
            this.type = this.bytes[this.offset];
            if (this.isInt() || this.type == -81 || (this.type >= 73 && this.type <= 120)) {
                this.jsonWriter.writeRaw("[");
                this.dumpAny();
                this.jsonWriter.writeRaw("]");
                return;
            }
        }
        this.jsonWriter.startArray();
        for (int i = 0; i < len; ++i) {
            if (i != 0) {
                this.jsonWriter.writeComma();
            }
            if (this.isReference()) {
                this.dumpReference();
            }
            else {
                this.dumpAny();
            }
        }
        this.jsonWriter.endArray();
    }
    
    private void dumpObject(final String typeName) {
        if (typeName != null) {
            this.jsonWriter.startObject();
            this.jsonWriter.writeName("@type");
            this.jsonWriter.writeColon();
            this.jsonWriter.writeString(typeName);
        }
        else {
            if (this.bytes[this.offset] == -91) {
                this.jsonWriter.writeRaw("{}");
                ++this.offset;
                return;
            }
            this.jsonWriter.startObject();
        }
        int valueCount = 0;
        while (true) {
            final byte type = this.bytes[this.offset];
            switch (type) {
                case -91: {
                    ++this.offset;
                    this.jsonWriter.endObject();
                    return;
                }
                case -79: {
                    ++this.offset;
                    this.jsonWriter.writeName("true");
                    break;
                }
                case -80: {
                    ++this.offset;
                    this.jsonWriter.writeName("false");
                    break;
                }
                case -109: {
                    this.dumpReference();
                    break;
                }
                case Byte.MAX_VALUE: {
                    ++this.offset;
                    if (this.isInt()) {
                        final int symbol = this.readInt32Value();
                        if (this.raw) {
                            this.jsonWriter.writeName("#" + symbol);
                        }
                        else {
                            final String name = this.symbols.get(symbol);
                            if (name == null) {
                                throw new JSONException("symbol not found " + symbol);
                            }
                            this.jsonWriter.writeName(name);
                        }
                        break;
                    }
                    final String name2 = this.readString();
                    final int symbol2 = this.readInt32Value();
                    this.symbols.put(symbol2, name2);
                    if (this.raw) {
                        this.jsonWriter.writeName(name2 + "#" + symbol2);
                    }
                    else {
                        this.jsonWriter.writeName(name2);
                    }
                    break;
                }
                case -81: {
                    this.jsonWriter.writeNameRaw("null".toCharArray());
                    ++this.offset;
                    break;
                }
                default: {
                    if (this.isString()) {
                        this.jsonWriter.writeName(this.readString());
                        break;
                    }
                    if (type >= -16 && type <= 72) {
                        this.jsonWriter.writeName(this.readInt32Value());
                        break;
                    }
                    if ((type >= -40 && type <= -17) || type == -66) {
                        this.jsonWriter.writeName(this.readInt64Value());
                        break;
                    }
                    if (valueCount != 0) {
                        this.jsonWriter.writeComma();
                    }
                    this.dumpAny();
                    break;
                }
            }
            ++valueCount;
            this.jsonWriter.writeColon();
            if (this.isReference()) {
                this.dumpReference();
            }
            else {
                this.dumpAny();
            }
        }
    }
    
    private void dumpReference() {
        this.jsonWriter.writeRaw("{\"$ref\":");
        final String reference = this.readReference();
        this.jsonWriter.writeString(reference);
        if (!"#-1".equals(reference)) {
            this.lastReference = reference;
        }
        this.jsonWriter.writeRaw("}");
    }
    
    int readInt32Value() {
        final byte type = this.bytes[this.offset++];
        if (type >= -16 && type <= 47) {
            return type;
        }
        if (type >= 48 && type <= 63) {
            return (type - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
        }
        if (type >= 64 && type <= 71) {
            return (type - 68 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
        }
        switch (type) {
            case -84:
            case -83:
            case 72: {
                final int int32Value = (this.bytes[this.offset + 3] & 0xFF) + ((this.bytes[this.offset + 2] & 0xFF) << 8) + ((this.bytes[this.offset + 1] & 0xFF) << 16) + (this.bytes[this.offset] << 24);
                this.offset += 4;
                return int32Value;
            }
            default: {
                throw new JSONException("readInt32Value not support " + JSONB.typeName(type) + ", offset " + this.offset + "/" + this.bytes.length);
            }
        }
    }
    
    long readInt64Value() {
        final byte type = this.bytes[this.offset++];
        if (type >= -16 && type <= 47) {
            return type;
        }
        if (type >= 48 && type <= 63) {
            return (type - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
        }
        if (type >= 64 && type <= 71) {
            return (type - 68 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
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
            case -67: {
                return this.bytes[this.offset++];
            }
            case -68: {
                final int int16Value = (this.bytes[this.offset + 1] & 0xFF) + (this.bytes[this.offset] << 8);
                this.offset += 2;
                return int16Value;
            }
            case -83: {
                final long minutes = (this.bytes[this.offset + 3] & 0xFF) + ((this.bytes[this.offset + 2] & 0xFF) << 8) + ((this.bytes[this.offset + 1] & 0xFF) << 16) + (this.bytes[this.offset] << 24);
                this.offset += 4;
                return minutes * 60L * 1000L;
            }
            case -84: {
                final long seconds = (this.bytes[this.offset + 3] & 0xFF) + ((this.bytes[this.offset + 2] & 0xFF) << 8) + ((this.bytes[this.offset + 1] & 0xFF) << 16) + (this.bytes[this.offset] << 24);
                this.offset += 4;
                return seconds * 1000L;
            }
            case -65:
            case 72: {
                final int int32Value = (this.bytes[this.offset + 3] & 0xFF) + ((this.bytes[this.offset + 2] & 0xFF) << 8) + ((this.bytes[this.offset + 1] & 0xFF) << 16) + (this.bytes[this.offset] << 24);
                this.offset += 4;
                return int32Value;
            }
            case -85:
            case -66: {
                final long int64Value = ((long)this.bytes[this.offset + 7] & 0xFFL) + (((long)this.bytes[this.offset + 6] & 0xFFL) << 8) + (((long)this.bytes[this.offset + 5] & 0xFFL) << 16) + (((long)this.bytes[this.offset + 4] & 0xFFL) << 24) + (((long)this.bytes[this.offset + 3] & 0xFFL) << 32) + (((long)this.bytes[this.offset + 2] & 0xFFL) << 40) + (((long)this.bytes[this.offset + 1] & 0xFFL) << 48) + ((long)this.bytes[this.offset] << 56);
                this.offset += 8;
                return int64Value;
            }
            default: {
                throw new JSONException("readInt64Value not support " + JSONB.typeName(type) + ", offset " + this.offset + "/" + this.bytes.length);
            }
        }
    }
    
    int readLength() {
        final byte type = this.bytes[this.offset++];
        if (type >= -16 && type <= 47) {
            return type;
        }
        if (type >= 64 && type <= 71) {
            return (type - 68 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
        }
        if (type >= -40 && type <= -17) {
            return -8 + (type + 40);
        }
        if (type >= 48 && type <= 63) {
            return (type - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
        }
        if (type == 72) {
            return (this.bytes[this.offset++] << 24) + ((this.bytes[this.offset++] & 0xFF) << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
        }
        throw new JSONException("not support length type : " + type);
    }
    
    BigInteger readBigInteger() {
        final int type = this.bytes[this.offset++];
        if (type >= -16 && type <= 47) {
            return BigInteger.valueOf(type);
        }
        if (type >= 48 && type <= 63) {
            final int intValue = (type - 56 << 8) + (this.bytes[this.offset++] & 0xFF);
            return BigInteger.valueOf(intValue);
        }
        if (type >= 64 && type <= 71) {
            final int intValue = (type - 68 << 16) + ((this.bytes[this.offset++] & 0xFF) << 8) + (this.bytes[this.offset++] & 0xFF);
            return BigInteger.valueOf(intValue);
        }
        switch (type) {
            case -81: {
                return null;
            }
            case -80: {
                return BigInteger.ZERO;
            }
            case -79: {
                return BigInteger.ONE;
            }
            case 72: {
                final int int32Value = (this.bytes[this.offset + 3] & 0xFF) + ((this.bytes[this.offset + 2] & 0xFF) << 8) + ((this.bytes[this.offset + 1] & 0xFF) << 16) + (this.bytes[this.offset] << 24);
                this.offset += 4;
                return BigInteger.valueOf(int32Value);
            }
            case -66: {
                final long int64Value = ((long)this.bytes[this.offset + 7] & 0xFFL) + (((long)this.bytes[this.offset + 6] & 0xFFL) << 8) + (((long)this.bytes[this.offset + 5] & 0xFFL) << 16) + (((long)this.bytes[this.offset + 4] & 0xFFL) << 24) + (((long)this.bytes[this.offset + 3] & 0xFFL) << 32) + (((long)this.bytes[this.offset + 2] & 0xFFL) << 40) + (((long)this.bytes[this.offset + 1] & 0xFFL) << 48) + ((long)this.bytes[this.offset] << 56);
                this.offset += 8;
                return BigInteger.valueOf(int64Value);
            }
            case -111:
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
            default: {
                throw new JSONException("not support type :" + type);
            }
        }
    }
    
    boolean isReference() {
        return this.offset < this.bytes.length && this.bytes[this.offset] == -109;
    }
    
    boolean isString() {
        final byte type = this.bytes[this.offset];
        return type >= 73 && type <= 125;
    }
    
    String readString() {
        this.strtype = this.bytes[this.offset++];
        if (this.strtype == -81) {
            return null;
        }
        this.strBegin = this.offset;
        Charset charset;
        if (this.strtype >= 73 && this.strtype <= 121) {
            if (this.strtype == 121) {
                this.strlen = this.readLength();
                this.strBegin = this.offset;
            }
            else {
                this.strlen = this.strtype - 73;
            }
            charset = StandardCharsets.ISO_8859_1;
        }
        else if (this.strtype == 122) {
            this.strlen = this.readLength();
            this.strBegin = this.offset;
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
                final byte[] chars = new byte[this.strlen];
                System.arraycopy(this.bytes, this.offset, chars, 0, this.strlen);
                final String str = JDKUtils.STRING_CREATOR_JDK11.apply(chars, JDKUtils.UTF16);
                this.offset += this.strlen;
                return str;
            }
            charset = StandardCharsets.UTF_16LE;
        }
        else if (this.strtype == 125) {
            this.strlen = this.readLength();
            this.strBegin = this.offset;
            if (JDKUtils.STRING_CREATOR_JDK11 != null && JDKUtils.BIG_ENDIAN) {
                final byte[] chars = new byte[this.strlen];
                System.arraycopy(this.bytes, this.offset, chars, 0, this.strlen);
                final String str = JDKUtils.STRING_CREATOR_JDK11.apply(chars, JDKUtils.UTF16);
                this.offset += this.strlen;
                return str;
            }
            charset = StandardCharsets.UTF_16BE;
        }
        else {
            if (this.strtype >= -16 && this.strtype <= 47) {
                return Byte.toString(this.strtype);
            }
            throw new JSONException("readString not support type " + JSONB.typeName(this.strtype) + ", offset " + this.offset + "/" + this.bytes.length);
        }
        if (this.strlen < 0) {
            return this.symbolTable.getName(-this.strlen);
        }
        final String str2 = new String(this.bytes, this.offset, this.strlen, charset);
        this.offset += this.strlen;
        return str2;
    }
    
    String readReference() {
        if (this.bytes[this.offset] != -109) {
            return null;
        }
        ++this.offset;
        if (this.isString()) {
            return this.readString();
        }
        throw new JSONException("reference not support input " + JSONB.typeName(this.type));
    }
    
    @Override
    public String toString() {
        return this.jsonWriter.toString();
    }
    
    boolean isInt() {
        final int type = this.bytes[this.offset];
        return (type >= -70 && type <= 72) || type == -83 || type == -84 || type == -85;
    }
    
    public String getString(final int symbol) {
        String name;
        if (symbol < 0) {
            name = this.symbolTable.getName(-symbol);
        }
        else {
            name = this.symbols.get(symbol);
        }
        if (name == null) {
            throw new JSONException("symbol not found : " + symbol);
        }
        return name;
    }
}
