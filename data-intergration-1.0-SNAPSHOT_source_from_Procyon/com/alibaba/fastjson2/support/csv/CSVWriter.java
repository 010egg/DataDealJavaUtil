// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.support.csv;

import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.time.DateTimeException;
import com.alibaba.fastjson2.util.DateUtils;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.IntFunction;
import java.time.temporal.TemporalAccessor;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import com.alibaba.fastjson2.JSONFactory;
import java.nio.charset.Charset;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.io.Flushable;
import java.io.Closeable;

public abstract class CSVWriter implements Closeable, Flushable
{
    private long features;
    final ZoneId zoneId;
    int off;
    
    CSVWriter(final ZoneId zoneId, final Feature... features) {
        for (final Feature feature : features) {
            this.features |= feature.mask;
        }
        this.zoneId = zoneId;
    }
    
    public static CSVWriter of() {
        return of(new ByteArrayOutputStream(), new Feature[0]);
    }
    
    public static CSVWriter of(final File file) throws FileNotFoundException {
        return of(new FileOutputStream(file), StandardCharsets.UTF_8);
    }
    
    public static CSVWriter of(final File file, final Charset charset) throws FileNotFoundException {
        return of(new FileOutputStream(file), charset);
    }
    
    public final void writeLineObject(final Object object) {
        if (object == null) {
            this.writeLine();
            return;
        }
        final ObjectWriterProvider provider = JSONFactory.getDefaultObjectWriterProvider();
        final Class<?> objectClass = object.getClass();
        final ObjectWriter objectWriter = provider.getObjectWriter(objectClass);
        if (objectWriter instanceof ObjectWriterAdapter) {
            final ObjectWriterAdapter adapter = (ObjectWriterAdapter)objectWriter;
            final List<FieldWriter> fieldWriters = (List<FieldWriter>)adapter.getFieldWriters();
            if (fieldWriters.size() == 1 && (fieldWriters.get(0).features & 0x1000000000000L) != 0x0L) {
                final Object fieldValue = fieldWriters.get(0).getFieldValue(object);
                this.writeLineObject(fieldValue);
                return;
            }
            final Object[] values = new Object[fieldWriters.size()];
            for (int i = 0; i < fieldWriters.size(); ++i) {
                values[i] = fieldWriters.get(i).getFieldValue(object);
            }
            this.writeLine(values);
        }
        else {
            this.writeLine(object);
        }
    }
    
    public final void writeDate(final Date date) {
        if (date == null) {
            return;
        }
        final long millis = date.getTime();
        this.writeDate(millis);
    }
    
    public final void writeInstant(final Instant instant) {
        if (instant == null) {
            return;
        }
        final int nano = instant.getNano();
        if (nano % 1000000 == 0) {
            final long millis = instant.toEpochMilli();
            this.writeDate(millis);
            return;
        }
        if ((this.features & Feature.AlwaysQuoteStrings.mask) != 0x0L) {
            this.writeQuote();
        }
        final LocalDateTime ldt = instant.atZone(this.zoneId).toLocalDateTime();
        this.writeLocalDateTime(ldt);
    }
    
    public void writeLocalDate(final LocalDate date) {
        if (date == null) {
            return;
        }
        final String str = DateTimeFormatter.ISO_LOCAL_DATE.format(date);
        this.writeRaw(str);
    }
    
    public abstract void writeLocalDateTime(final LocalDateTime p0);
    
    public final void writeLine(final int columnCount, final IntFunction function) {
        for (int i = 0; i < columnCount; ++i) {
            final Object value = function.apply(i);
            if (i != 0) {
                this.writeComma();
            }
            this.writeValue(value);
        }
        this.writeLine();
    }
    
    public final void writeLine(final List values) {
        for (int i = 0; i < values.size(); ++i) {
            if (i != 0) {
                this.writeComma();
            }
            this.writeValue(values.get(i));
        }
        this.writeLine();
    }
    
    public final void writeLine(final Object... values) {
        for (int i = 0; i < values.length; ++i) {
            if (i != 0) {
                this.writeComma();
            }
            this.writeValue(values[i]);
        }
        this.writeLine();
    }
    
    public abstract void writeComma();
    
    protected abstract void writeQuote();
    
    public abstract void writeLine();
    
    public void writeValue(Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof Optional) {
            final Optional optional = (Optional)value;
            if (!optional.isPresent()) {
                return;
            }
            value = optional.get();
        }
        if (value instanceof Integer) {
            this.writeInt32((int)value);
        }
        else if (value instanceof Long) {
            this.writeInt64((long)value);
        }
        else if (value instanceof String) {
            this.writeString((String)value);
        }
        else if (value instanceof Boolean) {
            final boolean booleanValue = (boolean)value;
            this.writeBoolean(booleanValue);
        }
        else if (value instanceof Float) {
            final float floatValue = (float)value;
            this.writeFloat(floatValue);
        }
        else if (value instanceof Double) {
            this.writeDouble((double)value);
        }
        else if (value instanceof Short) {
            this.writeInt32((int)value);
        }
        else if (value instanceof Byte) {
            this.writeInt32((int)value);
        }
        else if (value instanceof BigDecimal) {
            this.writeDecimal((BigDecimal)value);
        }
        else if (value instanceof BigInteger) {
            this.writeBigInteger((BigInteger)value);
        }
        else if (value instanceof Date) {
            this.writeDate((Date)value);
        }
        else if (value instanceof Instant) {
            this.writeInstant((Instant)value);
        }
        else if (value instanceof LocalDate) {
            this.writeLocalDate((LocalDate)value);
        }
        else if (value instanceof LocalDateTime) {
            this.writeLocalDateTime((LocalDateTime)value);
        }
        else {
            final String str = value.toString();
            this.writeString(str);
        }
    }
    
    public void writeBigInteger(final BigInteger value) {
        if (value == null) {
            return;
        }
        final String str = value.toString();
        this.writeRaw(str);
    }
    
    public abstract void writeBoolean(final boolean p0);
    
    public abstract void writeInt64(final long p0);
    
    public final void writeDate(final long millis) {
        final ZoneId zoneId = this.zoneId;
        final int SECONDS_PER_DAY = 86400;
        final long epochSecond = Math.floorDiv(millis, 1000L);
        int offsetTotalSeconds;
        if (zoneId == DateUtils.SHANGHAI_ZONE_ID || zoneId.getRules() == DateUtils.SHANGHAI_ZONE_RULES) {
            offsetTotalSeconds = DateUtils.getShanghaiZoneOffsetTotalSeconds(epochSecond);
        }
        else {
            final Instant instant = Instant.ofEpochMilli(millis);
            offsetTotalSeconds = zoneId.getRules().getOffset(instant).getTotalSeconds();
        }
        final long localSecond = epochSecond + offsetTotalSeconds;
        final long localEpochDay = Math.floorDiv(localSecond, 86400L);
        final int secsOfDay = (int)Math.floorMod(localSecond, 86400L);
        final int DAYS_PER_CYCLE = 146097;
        final long DAYS_0000_TO_1970 = 719528L;
        long zeroDay = localEpochDay + 719528L;
        zeroDay -= 60L;
        long adjust = 0L;
        if (zeroDay < 0L) {
            final long adjustCycles = (zeroDay + 1L) / 146097L - 1L;
            adjust = adjustCycles * 400L;
            zeroDay += -adjustCycles * 146097L;
        }
        long yearEst = (400L * zeroDay + 591L) / 146097L;
        long doyEst = zeroDay - (365L * yearEst + yearEst / 4L - yearEst / 100L + yearEst / 400L);
        if (doyEst < 0L) {
            --yearEst;
            doyEst = zeroDay - (365L * yearEst + yearEst / 4L - yearEst / 100L + yearEst / 400L);
        }
        yearEst += adjust;
        final int marchDoy0 = (int)doyEst;
        final int marchMonth0 = (marchDoy0 * 5 + 2) / 153;
        final int month = (marchMonth0 + 2) % 12 + 1;
        final int dayOfMonth = marchDoy0 - (marchMonth0 * 306 + 5) / 10 + 1;
        yearEst += marchMonth0 / 10;
        if (yearEst < -999999999L || yearEst > 999999999L) {
            throw new DateTimeException("Invalid year " + yearEst);
        }
        final int year = (int)yearEst;
        final int MINUTES_PER_HOUR = 60;
        final int SECONDS_PER_MINUTE = 60;
        final int SECONDS_PER_HOUR = 3600;
        long secondOfDay = secsOfDay;
        if (secondOfDay < 0L || secondOfDay > 86399L) {
            throw new DateTimeException("Invalid secondOfDay " + secondOfDay);
        }
        final int hours = (int)(secondOfDay / 3600L);
        secondOfDay -= hours * 3600;
        final int minutes = (int)(secondOfDay / 60L);
        secondOfDay -= minutes * 60;
        final int hour = hours;
        final int minute = minutes;
        final int second = (int)secondOfDay;
        if (year >= 0 && year <= 9999) {
            final int mos = (int)Math.floorMod(millis, 1000L);
            if (mos == 0) {
                if (hour == 0 && minute == 0 && second == 0) {
                    this.writeDateYYYMMDD10(year, month, dayOfMonth);
                }
                else {
                    this.writeDateTime19(year, month, dayOfMonth, hour, minute, second);
                }
                return;
            }
        }
        final String str = DateUtils.toString(millis, false, zoneId);
        this.writeRaw(str);
    }
    
    public abstract void writeDateYYYMMDD10(final int p0, final int p1, final int p2);
    
    public abstract void writeDateTime19(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    public abstract void writeString(final String p0);
    
    public abstract void writeInt32(final int p0);
    
    public abstract void writeDouble(final double p0);
    
    public abstract void writeFloat(final float p0);
    
    @Override
    public abstract void flush();
    
    public abstract void writeString(final byte[] p0);
    
    public abstract void writeDecimal(final BigDecimal p0);
    
    public abstract void writeDecimal(final long p0, final int p1);
    
    protected abstract void writeRaw(final String p0);
    
    @Override
    public abstract void close() throws IOException;
    
    public static CSVWriter of(final OutputStream out, final Feature... features) {
        return new CSVWriterUTF8(out, StandardCharsets.UTF_8, DateUtils.DEFAULT_ZONE_ID, features);
    }
    
    public static CSVWriter of(final OutputStream out, final Charset charset) {
        return of(out, charset, DateUtils.DEFAULT_ZONE_ID);
    }
    
    public static CSVWriter of(final OutputStream out, Charset charset, final ZoneId zoneId) {
        if (charset == StandardCharsets.UTF_16 || charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
            return of(new OutputStreamWriter(out, charset), zoneId);
        }
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        return new CSVWriterUTF8(out, charset, zoneId, new Feature[0]);
    }
    
    public static CSVWriter of(final Writer out) {
        return new CSVWriterUTF16(out, DateUtils.DEFAULT_ZONE_ID, new Feature[0]);
    }
    
    public static CSVWriter of(final Writer out, final ZoneId zoneId) {
        return new CSVWriterUTF16(out, zoneId, new Feature[0]);
    }
    
    public enum Feature
    {
        AlwaysQuoteStrings(1L);
        
        public final long mask;
        
        private Feature(final long mask) {
            this.mask = mask;
        }
        
        private static /* synthetic */ Feature[] $values() {
            return new Feature[] { Feature.AlwaysQuoteStrings };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
