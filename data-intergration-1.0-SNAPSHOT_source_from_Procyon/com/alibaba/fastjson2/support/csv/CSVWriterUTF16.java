// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.support.csv;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.util.DoubleToDecimal;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.IOUtils;
import java.io.IOException;
import com.alibaba.fastjson2.JSONException;
import java.time.ZoneId;
import java.io.Writer;

final class CSVWriterUTF16 extends CSVWriter
{
    static final char[] BYTES_TRUE;
    static final char[] BYTES_FALSE;
    final Writer out;
    final char[] chars;
    
    CSVWriterUTF16(final Writer out, final ZoneId zoneId, final Feature... features) {
        super(zoneId, features);
        this.out = out;
        this.chars = new char[65536];
    }
    
    void writeDirect(final char[] bytes, final int off, final int len) {
        try {
            this.out.write(bytes, off, len);
        }
        catch (IOException e) {
            throw new JSONException("write csv error", e);
        }
    }
    
    @Override
    public void writeComma() {
        if (this.off + 1 == this.chars.length) {
            this.flush();
        }
        this.chars[this.off++] = ',';
    }
    
    @Override
    protected void writeQuote() {
        if (this.off + 1 == this.chars.length) {
            this.flush();
        }
        this.chars[this.off++] = '\"';
    }
    
    @Override
    public void writeLine() {
        if (this.off + 1 == this.chars.length) {
            this.flush();
        }
        this.chars[this.off++] = '\n';
    }
    
    @Override
    public void writeBoolean(final boolean booleanValue) {
        final char[] valueBytes = booleanValue ? CSVWriterUTF16.BYTES_TRUE : CSVWriterUTF16.BYTES_FALSE;
        this.writeRaw(valueBytes);
    }
    
    @Override
    public void writeInt64(final long longValue) {
        final int minCapacity = this.off + 21;
        if (minCapacity - this.chars.length > 0) {
            this.flush();
        }
        this.off = IOUtils.writeInt64(this.chars, this.off, longValue);
    }
    
    @Override
    public void writeDateYYYMMDD10(final int year, final int month, final int dayOfMonth) {
        if (this.off + 11 >= this.chars.length) {
            this.flush();
        }
        this.off = IOUtils.writeLocalDate(this.chars, this.off, year, month, dayOfMonth);
    }
    
    @Override
    public void writeDateTime19(final int year, final int month, final int dayOfMonth, final int hour, final int minute, final int second) {
        if (this.off + 20 >= this.chars.length) {
            this.flush();
        }
        final char[] chars = this.chars;
        int off = this.off;
        off = IOUtils.writeLocalDate(chars, off, year, month, dayOfMonth);
        chars[off] = ' ';
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 1 << 1), IOUtils.PACKED_DIGITS_UTF16[hour]);
        chars[off + 3] = ':';
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 4 << 1), IOUtils.PACKED_DIGITS_UTF16[minute]);
        chars[off + 6] = ':';
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 7 << 1), IOUtils.PACKED_DIGITS_UTF16[second]);
        this.off = off + 9;
    }
    
    @Override
    public void writeString(final String str) {
        if (str == null || str.isEmpty()) {
            return;
        }
        final int len = str.length();
        int escapeCount = 0;
        boolean comma = false;
        if (str.charAt(0) == '\"') {
            for (int i = 0; i < len; ++i) {
                final char ch = str.charAt(i);
                if (ch == '\"') {
                    ++escapeCount;
                }
            }
        }
        else {
            for (int i = 0; i < len; ++i) {
                final char ch = str.charAt(i);
                if (ch == ',') {
                    comma = true;
                }
                else if (ch == '\"') {
                    ++escapeCount;
                }
            }
            if (!comma) {
                escapeCount = 0;
            }
        }
        if (escapeCount == 0) {
            str.getChars(0, str.length(), this.chars, this.off);
            this.off += str.length();
            return;
        }
        if (this.off + 2 + str.length() + escapeCount >= this.chars.length) {
            this.flush();
        }
        this.chars[this.off++] = '\"';
        for (int i = 0; i < str.length(); ++i) {
            final char ch = str.charAt(i);
            if (ch == '\"') {
                this.chars[this.off++] = '\"';
                this.chars[this.off++] = '\"';
            }
            else {
                this.chars[this.off++] = ch;
            }
        }
        this.chars[this.off++] = '\"';
    }
    
    @Override
    public void writeInt32(final int intValue) {
        final int minCapacity = this.off + 11;
        if (minCapacity - this.chars.length > 0) {
            this.flush();
        }
        this.off = IOUtils.writeInt32(this.chars, this.off, intValue);
    }
    
    @Override
    public void writeDouble(final double value) {
        final int minCapacity = this.off + 24;
        if (minCapacity - this.chars.length > 0) {
            this.flush();
        }
        final int size = DoubleToDecimal.toString(value, this.chars, this.off, true);
        this.off += size;
    }
    
    @Override
    public void writeFloat(final float value) {
        final int minCapacity = this.off + 15;
        if (minCapacity - this.chars.length > 0) {
            this.flush();
        }
        final int size = DoubleToDecimal.toString(value, this.chars, this.off, true);
        this.off += size;
    }
    
    @Override
    public void flush() {
        try {
            this.out.write(this.chars, 0, this.off);
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
        final String str = new String(utf8, 0, utf8.length, StandardCharsets.UTF_8);
        this.writeString(str);
    }
    
    @Override
    public void writeDecimal(final BigDecimal value) {
        if (value == null) {
            return;
        }
        final String str = value.toString();
        final int strlen = str.length();
        str.getChars(0, strlen, this.chars, this.off);
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
        if (minCapacity - this.chars.length > 0) {
            this.flush();
        }
        this.off = IOUtils.writeDecimal(this.chars, this.off, unscaledVal, scale);
    }
    
    void writeRaw(final char[] chars) {
        if (chars.length + this.off < this.chars.length) {
            System.arraycopy(chars, 0, this.chars, this.off, chars.length);
            this.off += chars.length;
        }
        else {
            this.flush();
            if (chars.length >= this.chars.length) {
                this.writeDirect(chars, 0, chars.length);
            }
            else {
                System.arraycopy(chars, 0, this.chars, this.off, chars.length);
                this.off += chars.length;
            }
        }
    }
    
    @Override
    public void writeLocalDateTime(final LocalDateTime ldt) {
        if (ldt == null) {
            return;
        }
        this.off = IOUtils.writeLocalDate(this.chars, this.off, ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth());
        this.chars[this.off++] = ' ';
        this.off = IOUtils.writeLocalTime(this.chars, this.off, ldt.toLocalTime());
    }
    
    @Override
    protected void writeRaw(final String str) {
        if (str == null || str.isEmpty()) {
            return;
        }
        if (str.length() + this.off >= this.chars.length) {
            this.flush();
        }
        str.getChars(0, str.length(), this.chars, this.off);
        this.off += str.length();
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
        if (this.out instanceof StringWriter) {
            this.flush();
            return this.out.toString();
        }
        return super.toString();
    }
    
    static {
        BYTES_TRUE = "true".toCharArray();
        BYTES_FALSE = "false".toCharArray();
    }
}
