// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.time.Instant;
import java.util.Date;
import com.alibaba.fastjson2.codec.DateTimeCodec;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Constructor;
import com.alibaba.fastjson2.reader.ObjectReaderImplDate;
import java.io.Reader;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.support.LambdaMiscCodec;
import java.util.function.Function;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodType;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.util.Locale;
import java.util.function.LongFunction;

public class JdbcSupport
{
    static Class CLASS_CLOB;
    static volatile boolean CLASS_CLOB_ERROR;
    static volatile LongFunction TIMESTAMP_CREATOR;
    static volatile boolean TIMESTAMP_CREATOR_ERROR;
    static volatile LongFunction DATE_CREATOR;
    static volatile boolean DATE_CREATOR_ERROR;
    static volatile LongFunction TIME_CREATOR;
    static volatile boolean TIME_CREATOR_ERROR;
    
    public static ObjectReader createTimeReader(final Class objectClass, final String format, final Locale locale) {
        return new TimeReader(objectClass, format, locale);
    }
    
    public static ObjectReader createTimestampReader(final Class objectClass, final String format, final Locale locale) {
        return new TimestampReader(objectClass, format, locale);
    }
    
    public static ObjectReader createDateReader(final Class objectClass, final String format, final Locale locale) {
        return new DateReader(objectClass, format, locale);
    }
    
    public static ObjectWriter createTimeWriter(final String format) {
        if (format == null) {
            return TimeWriter.INSTANCE;
        }
        return new TimeWriter(format);
    }
    
    public static Object createTimestamp(final long millis) {
        if (JdbcSupport.TIMESTAMP_CREATOR == null && !JdbcSupport.TIMESTAMP_CREATOR_ERROR) {
            try {
                JdbcSupport.TIMESTAMP_CREATOR = createFunction("java.sql.Timestamp");
            }
            catch (Throwable ignored) {
                JdbcSupport.TIMESTAMP_CREATOR_ERROR = true;
            }
        }
        if (JdbcSupport.TIMESTAMP_CREATOR == null) {
            throw new JSONException("create java.sql.Timestamp error");
        }
        return JdbcSupport.TIMESTAMP_CREATOR.apply(millis);
    }
    
    public static Object createDate(final long millis) {
        if (JdbcSupport.DATE_CREATOR == null && !JdbcSupport.DATE_CREATOR_ERROR) {
            try {
                JdbcSupport.DATE_CREATOR = createFunction("java.sql.Date");
            }
            catch (Throwable ignored) {
                JdbcSupport.DATE_CREATOR_ERROR = true;
            }
        }
        if (JdbcSupport.DATE_CREATOR == null) {
            throw new JSONException("create java.sql.Date error");
        }
        return JdbcSupport.DATE_CREATOR.apply(millis);
    }
    
    public static Object createTime(final long millis) {
        if (JdbcSupport.TIME_CREATOR == null && !JdbcSupport.TIME_CREATOR_ERROR) {
            try {
                JdbcSupport.TIME_CREATOR = createFunction("java.sql.Time");
            }
            catch (Throwable ignored) {
                JdbcSupport.TIME_CREATOR_ERROR = true;
            }
        }
        if (JdbcSupport.TIME_CREATOR == null) {
            throw new JSONException("create java.sql.Timestamp error");
        }
        return JdbcSupport.TIME_CREATOR.apply(millis);
    }
    
    static LongFunction createFunction(final String className) throws Throwable {
        final Class<?> timestampClass = Class.forName(className);
        final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(timestampClass);
        final MethodHandle constructor = lookup.findConstructor(timestampClass, MethodType.methodType(Void.TYPE, Long.TYPE));
        final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", MethodType.methodType(LongFunction.class), MethodType.methodType(Object.class, Long.TYPE), constructor, MethodType.methodType(Object.class, Long.TYPE));
        final MethodHandle target = callSite.getTarget();
        return target.invokeExact();
    }
    
    public static ObjectWriter createClobWriter(final Class objectClass) {
        return new ClobWriter(objectClass);
    }
    
    public static ObjectWriter createTimestampWriter(final Class objectClass, final String format) {
        return new TimestampWriter(objectClass, format);
    }
    
    public static boolean isClob(final Class objectClass) {
        if (JdbcSupport.CLASS_CLOB == null && !JdbcSupport.CLASS_CLOB_ERROR) {
            try {
                JdbcSupport.CLASS_CLOB = Class.forName("java.sql.Clob");
            }
            catch (Throwable e) {
                JdbcSupport.CLASS_CLOB_ERROR = true;
            }
        }
        return JdbcSupport.CLASS_CLOB != null && JdbcSupport.CLASS_CLOB.isAssignableFrom(objectClass);
    }
    
    static class ClobWriter implements ObjectWriter
    {
        final Class objectClass;
        final Function function;
        
        public ClobWriter(final Class objectClass) {
            if (JdbcSupport.CLASS_CLOB == null && !JdbcSupport.CLASS_CLOB_ERROR) {
                try {
                    JdbcSupport.CLASS_CLOB = Class.forName("java.sql.Clob");
                }
                catch (Throwable e) {
                    JdbcSupport.CLASS_CLOB_ERROR = true;
                }
            }
            if (JdbcSupport.CLASS_CLOB == null) {
                throw new JSONException("class java.sql.Clob not found");
            }
            this.objectClass = objectClass;
            try {
                final Method getCharacterStream = JdbcSupport.CLASS_CLOB.getMethod("getCharacterStream", (Class[])new Class[0]);
                this.function = LambdaMiscCodec.createFunction(getCharacterStream);
            }
            catch (Throwable e) {
                throw new JSONException("getMethod getCharacterStream error", e);
            }
        }
        
        @Override
        public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            final Reader reader = this.function.apply(object);
            jsonWriter.writeString(reader);
        }
    }
    
    static class TimeReader extends ObjectReaderImplDate
    {
        final LongFunction function;
        final Function functionValueOf;
        
        public TimeReader(final Class objectClass, final String format, final Locale locale) {
            super(format, locale);
            try {
                this.function = LambdaMiscCodec.createLongFunction(objectClass.getConstructor(Long.TYPE));
                final Method methodValueOf = objectClass.getMethod("valueOf", String.class);
                this.functionValueOf = LambdaMiscCodec.createFunction(methodValueOf);
            }
            catch (NoSuchMethodException e) {
                throw new IllegalStateException("illegal state", e);
            }
        }
        
        @Override
        public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            return this.readObject(jsonReader, fieldType, fieldName, features);
        }
        
        @Override
        public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            if (jsonReader.isInt()) {
                long millis = jsonReader.readInt64Value();
                if (this.formatUnixTime) {
                    millis *= 1000L;
                }
                return this.function.apply(millis);
            }
            if (jsonReader.readIfNull()) {
                return null;
            }
            if (this.formatISO8601 || this.formatMillis) {
                final long millis = jsonReader.readMillisFromString();
                return this.function.apply(millis);
            }
            if (this.formatUnixTime) {
                final long seconds = jsonReader.readInt64();
                return this.function.apply(seconds * 1000L);
            }
            long millis;
            if (this.format != null) {
                final DateTimeFormatter formatter = this.getDateFormatter(jsonReader.getLocale());
                ZonedDateTime zdt;
                if (formatter != null) {
                    final String str = jsonReader.readString();
                    if (str.isEmpty()) {
                        return null;
                    }
                    LocalDateTime ldt;
                    if (!this.formatHasHour) {
                        ldt = LocalDateTime.of(LocalDate.parse(str, formatter), LocalTime.MIN);
                    }
                    else if (!this.formatHasDay) {
                        ldt = LocalDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.parse(str, formatter));
                    }
                    else {
                        ldt = LocalDateTime.parse(str, formatter);
                    }
                    zdt = ldt.atZone(jsonReader.getContext().getZoneId());
                }
                else {
                    zdt = jsonReader.readZonedDateTime();
                }
                millis = zdt.toInstant().toEpochMilli();
            }
            else {
                final String str2 = jsonReader.readString();
                if ("0000-00-00".equals(str2) || "0000-00-00 00:00:00".equals(str2)) {
                    millis = 0L;
                }
                else if (str2.length() == 9 && str2.charAt(8) == 'Z') {
                    final LocalTime localTime = DateUtils.parseLocalTime(str2.charAt(0), str2.charAt(1), str2.charAt(2), str2.charAt(3), str2.charAt(4), str2.charAt(5), str2.charAt(6), str2.charAt(7));
                    millis = LocalDateTime.of(DateUtils.LOCAL_DATE_19700101, localTime).atZone(DateUtils.DEFAULT_ZONE_ID).toInstant().toEpochMilli();
                }
                else {
                    if (str2.isEmpty() || "null".equals(str2)) {
                        return null;
                    }
                    return this.functionValueOf.apply(str2);
                }
            }
            return this.function.apply(millis);
        }
    }
    
    static class TimeWriter extends DateTimeCodec implements ObjectWriter
    {
        public static final TimeWriter INSTANCE;
        
        public TimeWriter(final String format) {
            super(format);
        }
        
        @Override
        public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            if (object == null) {
                jsonWriter.writeNull();
                return;
            }
            final JSONWriter.Context context = jsonWriter.context;
            if (this.formatUnixTime || context.isDateFormatUnixTime()) {
                final long millis = ((Date)object).getTime();
                final long seconds = millis / 1000L;
                jsonWriter.writeInt64(seconds);
                return;
            }
            if (this.formatMillis || context.isDateFormatMillis()) {
                final long millis = ((Date)object).getTime();
                jsonWriter.writeInt64(millis);
                return;
            }
            if (this.formatISO8601 || context.isDateFormatISO8601()) {
                final ZoneId zoneId = context.getZoneId();
                final long millis2 = ((Date)object).getTime();
                final Instant instant = Instant.ofEpochMilli(millis2);
                final ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
                final int offsetSeconds = zdt.getOffset().getTotalSeconds();
                final int year = zdt.getYear();
                final int month = zdt.getMonthValue();
                final int dayOfMonth = zdt.getDayOfMonth();
                final int hour = zdt.getHour();
                final int minute = zdt.getMinute();
                final int second = zdt.getSecond();
                final int nano = 0;
                jsonWriter.writeDateTimeISO8601(year, month, dayOfMonth, hour, minute, second, nano, offsetSeconds, true);
                return;
            }
            DateTimeFormatter dateFormatter = null;
            if (this.format != null && !this.format.contains("dd")) {
                dateFormatter = this.getDateFormatter();
            }
            if (dateFormatter == null) {
                final String format = context.getDateFormat();
                if (format != null && !format.contains("dd")) {
                    dateFormatter = context.getDateFormatter();
                }
            }
            if (dateFormatter == null) {
                jsonWriter.writeString(object.toString());
                return;
            }
            final Date time = (Date)object;
            final ZoneId zoneId2 = context.getZoneId();
            final Instant instant = Instant.ofEpochMilli(time.getTime());
            final ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId2);
            final String str = dateFormatter.format(zdt);
            jsonWriter.writeString(str);
        }
        
        static {
            INSTANCE = new TimeWriter(null);
        }
    }
    
    static class TimestampWriter extends DateTimeCodec implements ObjectWriter
    {
        final ToIntFunction functionGetNano;
        final Function functionToLocalDateTime;
        
        public TimestampWriter(final Class objectClass, final String format) {
            super(format);
            try {
                this.functionGetNano = LambdaMiscCodec.createToIntFunction(objectClass.getMethod("getNanos", (Class[])new Class[0]));
                final Method methodToLocalDateTime = objectClass.getMethod("toLocalDateTime", (Class[])new Class[0]);
                this.functionToLocalDateTime = LambdaMiscCodec.createFunction(methodToLocalDateTime);
            }
            catch (NoSuchMethodException e) {
                throw new JSONException("illegal state", e);
            }
        }
        
        @Override
        public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            if (object == null) {
                jsonWriter.writeNull();
                return;
            }
            final Date date = (Date)object;
            final int nanos = this.functionGetNano.applyAsInt(object);
            if (nanos == 0) {
                jsonWriter.writeMillis(date.getTime());
                return;
            }
            final LocalDateTime localDateTime = this.functionToLocalDateTime.apply(object);
            jsonWriter.writeLocalDateTime(localDateTime);
        }
        
        @Override
        public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            if (object == null) {
                jsonWriter.writeNull();
                return;
            }
            final JSONWriter.Context ctx = jsonWriter.context;
            final Date date = (Date)object;
            if (this.formatUnixTime || ctx.isDateFormatUnixTime()) {
                final long millis = date.getTime();
                jsonWriter.writeInt64(millis / 1000L);
                return;
            }
            final ZoneId zoneId = ctx.getZoneId();
            final Instant instant = date.toInstant();
            final ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
            final int offsetSeconds = zdt.getOffset().getTotalSeconds();
            if ((this.formatISO8601 || ctx.isDateFormatISO8601()) && zdt.getNano() % 1000000 == 0) {
                final int year = zdt.getYear();
                final int month = zdt.getMonthValue();
                final int dayOfMonth = zdt.getDayOfMonth();
                final int hour = zdt.getHour();
                final int minute = zdt.getMinute();
                final int second = zdt.getSecond();
                final int nano = zdt.getNano();
                final int millis2 = nano / 1000000;
                jsonWriter.writeDateTimeISO8601(year, month, dayOfMonth, hour, minute, second, millis2, offsetSeconds, true);
                return;
            }
            DateTimeFormatter dateFormatter = this.getDateFormatter();
            if (dateFormatter == null) {
                dateFormatter = ctx.getDateFormatter();
            }
            if (dateFormatter == null) {
                if (this.formatMillis || ctx.isDateFormatMillis()) {
                    final long millis3 = date.getTime();
                    jsonWriter.writeInt64(millis3);
                    return;
                }
                final int nanos = this.functionGetNano.applyAsInt(date);
                if (nanos == 0) {
                    jsonWriter.writeInt64(date.getTime());
                    return;
                }
                final int year2 = zdt.getYear();
                final int month2 = zdt.getMonthValue();
                final int dayOfMonth2 = zdt.getDayOfMonth();
                final int hour2 = zdt.getHour();
                final int minute2 = zdt.getMinute();
                final int second2 = zdt.getSecond();
                if (nanos % 1000000 == 0) {
                    jsonWriter.writeDateTimeISO8601(year2, month2, dayOfMonth2, hour2, minute2, second2, nanos / 1000000, offsetSeconds, false);
                }
                else {
                    jsonWriter.writeLocalDateTime(zdt.toLocalDateTime());
                }
            }
            else {
                final String str = dateFormatter.format(zdt);
                jsonWriter.writeString(str);
            }
        }
    }
    
    static class TimestampReader extends ObjectReaderImplDate
    {
        final LongFunction function;
        final ObjIntConsumer functionSetNanos;
        final Function functionValueOf;
        
        public TimestampReader(final Class objectClass, final String format, final Locale locale) {
            super(format, locale);
            try {
                final Constructor constructor = objectClass.getConstructor(Long.TYPE);
                this.function = LambdaMiscCodec.createLongFunction(constructor);
            }
            catch (Throwable e) {
                throw new IllegalStateException("illegal state", e);
            }
            try {
                final Method methodSetNanos = objectClass.getMethod("setNanos", Integer.TYPE);
                this.functionSetNanos = LambdaMiscCodec.createObjIntConsumer(methodSetNanos);
            }
            catch (NoSuchMethodException e2) {
                throw new IllegalStateException("illegal state", e2);
            }
            Function functionValueOf = null;
            try {
                final Method methodValueOf = objectClass.getMethod("valueOf", LocalDateTime.class);
                functionValueOf = LambdaMiscCodec.createFunction(methodValueOf);
            }
            catch (Throwable t) {}
            this.functionValueOf = functionValueOf;
        }
        
        @Override
        public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            if (jsonReader.isInt()) {
                long millis = jsonReader.readInt64Value();
                if (this.formatUnixTime) {
                    millis *= 1000L;
                }
                return this.createTimestamp(millis, 0);
            }
            if (jsonReader.readIfNull()) {
                return null;
            }
            return this.readObject(jsonReader, fieldType, fieldName, features);
        }
        
        Object createTimestamp(final long millis, final int nanos) {
            final Object timestamp = this.function.apply(millis);
            if (nanos != 0) {
                this.functionSetNanos.accept(timestamp, nanos);
            }
            return timestamp;
        }
        
        @Override
        public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            if (jsonReader.isInt()) {
                long millis = jsonReader.readInt64Value();
                if (this.formatUnixTime) {
                    millis *= 1000L;
                }
                return this.createTimestamp(millis, 0);
            }
            if (jsonReader.nextIfNullOrEmptyString()) {
                return null;
            }
            if (this.format == null || this.formatISO8601 || this.formatMillis) {
                if (this.functionValueOf != null) {
                    final LocalDateTime localDateTime = jsonReader.readLocalDateTime();
                    if (localDateTime != null) {
                        return this.functionValueOf.apply(localDateTime);
                    }
                    if (jsonReader.wasNull()) {
                        return null;
                    }
                }
                final long millis = jsonReader.readMillisFromString();
                if (millis == 0L && jsonReader.wasNull()) {
                    return null;
                }
                return this.function.apply(millis);
            }
            else {
                final String str = jsonReader.readString();
                if (str.isEmpty()) {
                    return null;
                }
                final DateTimeFormatter dateFormatter = this.getDateFormatter();
                Instant instant;
                if (!this.formatHasHour) {
                    final LocalDate localDate = LocalDate.parse(str, dateFormatter);
                    final LocalDateTime ldt = LocalDateTime.of(localDate, LocalTime.MIN);
                    instant = ldt.atZone(jsonReader.getContext().getZoneId()).toInstant();
                }
                else {
                    final LocalDateTime ldt2 = LocalDateTime.parse(str, dateFormatter);
                    instant = ldt2.atZone(jsonReader.getContext().getZoneId()).toInstant();
                }
                final long millis2 = instant.toEpochMilli();
                final int nanos = instant.getNano();
                return this.createTimestamp(millis2, nanos);
            }
        }
    }
    
    static class DateReader extends ObjectReaderImplDate
    {
        final LongFunction function;
        final Function functionValueOf;
        
        public DateReader(final Class objectClass, final String format, final Locale locale) {
            super(format, locale);
            try {
                final Constructor constructor = objectClass.getConstructor(Long.TYPE);
                this.function = LambdaMiscCodec.createLongFunction(constructor);
            }
            catch (Throwable e) {
                throw new IllegalStateException("illegal state", e);
            }
            try {
                final Method methodValueOf = objectClass.getMethod("valueOf", LocalDate.class);
                this.functionValueOf = LambdaMiscCodec.createFunction(methodValueOf);
            }
            catch (NoSuchMethodException | SecurityException ex2) {
                final Exception ex;
                final Exception e2 = ex;
                throw new IllegalStateException("illegal state", e2);
            }
        }
        
        @Override
        public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            return this.readObject(jsonReader, fieldType, fieldName, features);
        }
        
        @Override
        public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            if (jsonReader.isInt()) {
                long millis = jsonReader.readInt64Value();
                if (this.formatUnixTime) {
                    millis *= 1000L;
                }
                return this.function.apply(millis);
            }
            if (jsonReader.readIfNull()) {
                return null;
            }
            if (this.formatUnixTime && jsonReader.isString()) {
                final String str = jsonReader.readString();
                long millis2 = Long.parseLong(str);
                millis2 *= 1000L;
                return this.function.apply(millis2);
            }
            if (this.format == null || this.formatISO8601 || this.formatMillis) {
                final LocalDateTime localDateTime = jsonReader.readLocalDateTime();
                if (localDateTime != null) {
                    return this.functionValueOf.apply(localDateTime.toLocalDate());
                }
                if (jsonReader.wasNull()) {
                    return null;
                }
                final long millis2 = jsonReader.readMillisFromString();
                if (millis2 == 0L && jsonReader.wasNull()) {
                    return null;
                }
                return this.function.apply(millis2);
            }
            else {
                final String str = jsonReader.readString();
                if (str.isEmpty()) {
                    return null;
                }
                final DateTimeFormatter dateFormatter = this.getDateFormatter();
                Instant instant;
                if (!this.formatHasHour) {
                    final LocalDate localDate = LocalDate.parse(str, dateFormatter);
                    final LocalDateTime ldt = LocalDateTime.of(localDate, LocalTime.MIN);
                    instant = ldt.atZone(jsonReader.getContext().getZoneId()).toInstant();
                }
                else {
                    final LocalDateTime ldt2 = LocalDateTime.parse(str, dateFormatter);
                    instant = ldt2.atZone(jsonReader.getContext().getZoneId()).toInstant();
                }
                return this.function.apply(instant.toEpochMilli());
            }
        }
    }
}
