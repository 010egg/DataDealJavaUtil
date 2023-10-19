// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.support.csv;

import java.nio.charset.StandardCharsets;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.alibaba.fastjson2.util.DoubleToDecimal;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.IOUtils;
import java.io.IOException;
import com.alibaba.fastjson2.JSONException;
import java.time.ZoneId;
import java.nio.charset.Charset;
import java.io.OutputStream;

final class CSVWriterUTF8 extends CSVWriter
{
    static final byte[] BYTES_TRUE;
    static final byte[] BYTES_FALSE;
    static final byte[] BYTES_LONG_MIN;
    final OutputStream out;
    final Charset charset;
    final byte[] bytes;
    
    CSVWriterUTF8(final OutputStream out, final Charset charset, final ZoneId zoneId, final Feature... features) {
        super(zoneId, features);
        this.out = out;
        this.charset = charset;
        this.bytes = new byte[65536];
    }
    
    void writeDirect(final byte[] bytes, final int off, final int len) {
        try {
            this.out.write(bytes, off, len);
        }
        catch (IOException e) {
            throw new JSONException("write csv error", e);
        }
    }
    
    @Override
    public void writeComma() {
        if (this.off + 1 == this.bytes.length) {
            this.flush();
        }
        this.bytes[this.off++] = 44;
    }
    
    @Override
    protected void writeQuote() {
        if (this.off + 1 == this.bytes.length) {
            this.flush();
        }
        this.bytes[this.off++] = 34;
    }
    
    @Override
    public void writeLine() {
        if (this.off + 1 == this.bytes.length) {
            this.flush();
        }
        this.bytes[this.off++] = 10;
    }
    
    @Override
    public void writeBoolean(final boolean booleanValue) {
        final byte[] valueBytes = booleanValue ? CSVWriterUTF8.BYTES_TRUE : CSVWriterUTF8.BYTES_FALSE;
        this.writeRaw(valueBytes);
    }
    
    @Override
    public void writeInt64(final long longValue) {
        final int minCapacity = this.off + 21;
        if (minCapacity - this.bytes.length > 0) {
            this.flush();
        }
        this.off = IOUtils.writeInt64(this.bytes, this.off, longValue);
    }
    
    @Override
    public void writeDateYYYMMDD10(final int year, final int month, final int dayOfMonth) {
        if (this.off + 11 >= this.bytes.length) {
            this.flush();
        }
        this.off = IOUtils.writeLocalDate(this.bytes, this.off, year, month, dayOfMonth);
    }
    
    @Override
    public void writeDateTime19(final int year, final int month, final int dayOfMonth, final int hour, final int minute, final int second) {
        if (this.off + 20 >= this.bytes.length) {
            this.flush();
        }
        final byte[] bytes = this.bytes;
        int off = this.off;
        off = IOUtils.writeLocalDate(bytes, off, year, month, dayOfMonth);
        bytes[off] = 32;
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, IOUtils.PACKED_DIGITS[hour]);
        bytes[off + 3] = 58;
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 4L, IOUtils.PACKED_DIGITS[minute]);
        bytes[off + 6] = 58;
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 7L, IOUtils.PACKED_DIGITS[second]);
        this.off = off + 9;
    }
    
    @Override
    public void writeString(final String value) {
        byte[] bytes;
        if (JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER.applyAsInt(value) == JDKUtils.LATIN1) {
            bytes = JDKUtils.STRING_VALUE.apply(value);
        }
        else {
            bytes = value.getBytes(this.charset);
        }
        this.writeString(bytes);
    }
    
    @Override
    public void writeInt32(final int intValue) {
        final int minCapacity = this.off + 11;
        if (minCapacity - this.bytes.length > 0) {
            this.flush();
        }
        this.off = IOUtils.writeInt32(this.bytes, this.off, intValue);
    }
    
    @Override
    public void writeDouble(final double value) {
        final int minCapacity = this.off + 24;
        if (minCapacity - this.bytes.length > 0) {
            this.flush();
        }
        final int size = DoubleToDecimal.toString(value, this.bytes, this.off, true);
        this.off += size;
    }
    
    @Override
    public void writeFloat(final float value) {
        final int minCapacity = this.off + 15;
        if (minCapacity - this.bytes.length > 0) {
            this.flush();
        }
        final int size = DoubleToDecimal.toString(value, this.bytes, this.off, true);
        this.off += size;
    }
    
    @Override
    public void flush() {
        try {
            this.out.write(this.bytes, 0, this.off);
            this.off = 0;
            this.out.flush();
        }
        catch (IOException e) {
            throw new JSONException("write csv error", e);
        }
    }
    
    @Override
    public void writeString(final byte[] utf8) {
        if (utf8 == null || utf8.length == 0) {
            return;
        }
        final int len = utf8.length;
        int escapeCount = 0;
        boolean comma = false;
        if (utf8[0] == 34) {
            for (final byte ch : utf8) {
                if (ch == 34) {
                    ++escapeCount;
                }
            }
        }
        else {
            for (final byte ch : utf8) {
                if (ch == 44) {
                    comma = true;
                }
                else if (ch == 34) {
                    ++escapeCount;
                }
            }
            if (!comma) {
                escapeCount = 0;
            }
        }
        if (escapeCount == 0) {
            this.writeRaw(utf8);
            return;
        }
        if (this.off + 2 + utf8.length + escapeCount >= this.bytes.length) {
            this.flush();
        }
        this.bytes[this.off++] = 34;
        for (final byte ch : utf8) {
            if (ch == 34) {
                this.bytes[this.off++] = 34;
                this.bytes[this.off++] = 34;
            }
            else {
                this.bytes[this.off++] = ch;
            }
        }
        this.bytes[this.off++] = 34;
    }
    
    @Override
    public void writeDecimal(final BigDecimal value) {
        if (value == null) {
            return;
        }
        final String str = value.toString();
        final int strlen = str.length();
        str.getBytes(0, strlen, this.bytes, this.off);
        this.off += strlen;
    }
    
    @Override
    public void writeDecimal(final long unscaledVal, final int scale) {
        if (scale == 0) {
            this.writeInt64(unscaledVal);
            return;
        }
        if (unscaledVal == Long.MIN_VALUE || scale >= 20 || scale < 0) {
            this.writeDecimal(BigDecimal.valueOf(unscaledVal, scale));
            return;
        }
        final int minCapacity = this.off + 24;
        if (minCapacity - this.bytes.length > 0) {
            this.flush();
        }
        this.off = IOUtils.writeDecimal(this.bytes, this.off, unscaledVal, scale);
    }
    
    private void writeRaw(final byte[] strBytes) {
        if (strBytes.length + this.off < this.bytes.length) {
            System.arraycopy(strBytes, 0, this.bytes, this.off, strBytes.length);
            this.off += strBytes.length;
        }
        else {
            this.flush();
            if (strBytes.length >= this.bytes.length) {
                this.writeDirect(strBytes, 0, strBytes.length);
            }
            else {
                System.arraycopy(strBytes, 0, this.bytes, this.off, strBytes.length);
                this.off += strBytes.length;
            }
        }
    }
    
    @Override
    protected void writeRaw(final String str) {
        if (str == null || str.isEmpty()) {
            return;
        }
        final byte[] strBytes = str.getBytes(this.charset);
        if (strBytes.length + this.off < this.bytes.length) {
            System.arraycopy(strBytes, 0, this.bytes, this.off, strBytes.length);
            this.off += strBytes.length;
        }
        else {
            this.flush();
            if (strBytes.length >= this.bytes.length) {
                this.writeDirect(strBytes, 0, strBytes.length);
            }
            else {
                System.arraycopy(strBytes, 0, this.bytes, this.off, strBytes.length);
                this.off += strBytes.length;
            }
        }
    }
    
    @Override
    public void writeLocalDateTime(final LocalDateTime ldt) {
        if (ldt == null) {
            return;
        }
        this.off = IOUtils.writeLocalDate(this.bytes, this.off, ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth());
        this.bytes[this.off++] = 32;
        this.off = IOUtils.writeLocalTime(this.bytes, this.off, ldt.toLocalTime());
    }
    
    @Override
    public void close() throws IOException {
        if (this.off > 0) {
            this.flush();
        }
        this.out.close();
    }
    
    @Override
    public String toString() {
        if (this.out instanceof ByteArrayOutputStream) {
            this.flush();
            final byte[] strBytes = ((ByteArrayOutputStream)this.out).toByteArray();
            return new String(strBytes, StandardCharsets.UTF_8);
        }
        return super.toString();
    }
    
    static {
        BYTES_TRUE = "true".getBytes();
        BYTES_FALSE = "false".getBytes();
        BYTES_LONG_MIN = "-9223372036854775808".getBytes();
    }
}
