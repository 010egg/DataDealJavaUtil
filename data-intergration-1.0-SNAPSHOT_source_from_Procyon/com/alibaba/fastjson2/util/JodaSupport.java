// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import com.alibaba.fastjson2.codec.DateTimeCodec;
import com.alibaba.fastjson2.JSONB;
import java.time.LocalDate;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.util.function.ToIntFunction;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.time.Instant;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.Map;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Constructor;
import com.alibaba.fastjson2.support.LambdaMiscCodec;
import java.util.function.LongFunction;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.writer.ObjectWriter;

public class JodaSupport
{
    static final long HASH_YEAR;
    static final long HASH_MONTH;
    static final long HASH_DAY;
    static final long HASH_HOUR;
    static final long HASH_MINUTE;
    static final long HASH_SECOND;
    static final long HASH_MILLIS;
    static final long HASH_CHRONOLOGY;
    
    public static ObjectWriter createLocalDateTimeWriter(final Class objectClass, final String format) {
        return new LocalDateTimeWriter(objectClass, format);
    }
    
    public static ObjectWriter createLocalDateWriter(final Class objectClass, final String format) {
        return new LocalDateWriter(objectClass, format);
    }
    
    public static ObjectReader createChronologyReader(final Class objectClass) {
        return new ChronologyReader(objectClass);
    }
    
    public static ObjectReader createLocalDateReader(final Class objectClass) {
        return new LocalDateReader(objectClass);
    }
    
    public static ObjectReader createLocalDateTimeReader(final Class objectClass) {
        return new LocalDateTimeReader(objectClass);
    }
    
    public static ObjectReader createInstantReader(final Class objectClass) {
        return new InstantReader(objectClass);
    }
    
    public static ObjectWriter createGregorianChronologyWriter(final Class objectClass) {
        return new GregorianChronologyWriter(objectClass);
    }
    
    public static ObjectWriter createISOChronologyWriter(final Class objectClass) {
        return new ISOChronologyWriter(objectClass);
    }
    
    static {
        HASH_YEAR = Fnv.hashCode64("year");
        HASH_MONTH = Fnv.hashCode64("month");
        HASH_DAY = Fnv.hashCode64("day");
        HASH_HOUR = Fnv.hashCode64("hour");
        HASH_MINUTE = Fnv.hashCode64("minute");
        HASH_SECOND = Fnv.hashCode64("second");
        HASH_MILLIS = Fnv.hashCode64("millis");
        HASH_CHRONOLOGY = Fnv.hashCode64("chronology");
    }
    
    static class InstantReader implements ObjectReader
    {
        final Class objectClass;
        final LongFunction constructor;
        
        InstantReader(final Class objectClass) {
            this.objectClass = objectClass;
            try {
                this.constructor = LambdaMiscCodec.createLongFunction(objectClass.getConstructor(Long.TYPE));
            }
            catch (NoSuchMethodException e) {
                throw new JSONException("create joda instant reader error", e);
            }
        }
        
        @Override
        public Class getObjectClass() {
            return this.objectClass;
        }
        
        @Override
        public Object createInstance(final Map map, final long features) {
            final Number millis = map.get("millis");
            if (millis != null) {
                return this.createInstanceFromMillis(millis.longValue());
            }
            final Number epochSecond = map.get("epochSecond");
            if (epochSecond != null) {
                final long epochMillis = epochSecond.longValue() * 1000L;
                return this.createInstanceFromMillis(epochMillis);
            }
            throw new JSONException("create joda instant error");
        }
        
        public Object createInstanceFromMillis(final long millis) {
            return this.constructor.apply(millis);
        }
        
        @Override
        public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            if (jsonReader.nextIfNull()) {
                return null;
            }
            if (jsonReader.isInt()) {
                final long millis = jsonReader.readInt64Value();
                return this.createInstanceFromMillis(millis);
            }
            if (jsonReader.isString()) {
                final Instant jdkInstant = jsonReader.readInstant();
                if (jdkInstant == null) {
                    return null;
                }
                final long millis2 = jdkInstant.toEpochMilli();
                return this.createInstanceFromMillis(millis2);
            }
            else {
                if (jsonReader.isObject()) {
                    final Map object = jsonReader.readObject();
                    return this.createInstance(object, features);
                }
                throw new JSONException(jsonReader.info("not support"));
            }
        }
        
        @Override
        public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            return this.readObject(jsonReader, fieldType, fieldName, features);
        }
    }
    
    static class ChronologyReader implements ObjectReader
    {
        static final long HASH_ZONE_ID;
        final Class objectClass;
        final Class gregorianChronology;
        final Class dateTimeZone;
        final Function forID;
        final Function getInstance;
        final Object utc;
        
        ChronologyReader(final Class objectClass) {
            this.objectClass = objectClass;
            final ClassLoader classLoader = objectClass.getClassLoader();
            try {
                this.gregorianChronology = classLoader.loadClass("org.joda.time.chrono.GregorianChronology");
                this.dateTimeZone = classLoader.loadClass("org.joda.time.DateTimeZone");
                this.utc = this.gregorianChronology.getMethod("getInstanceUTC", (Class[])new Class[0]).invoke(null, new Object[0]);
                this.forID = LambdaMiscCodec.createFunction(this.dateTimeZone.getMethod("forID", String.class));
                this.getInstance = LambdaMiscCodec.createFunction(this.gregorianChronology.getMethod("getInstance", this.dateTimeZone));
            }
            catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("create ChronologyReader error", e);
            }
        }
        
        @Override
        public Class getObjectClass() {
            return this.objectClass;
        }
        
        @Override
        public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            throw new JSONException(jsonReader.info("not support"));
        }
        
        @Override
        public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            Integer minimumDaysInFirstWeek = null;
            String zoneId = null;
            jsonReader.nextIfObjectStart();
            while (!jsonReader.nextIfObjectEnd()) {
                final long HASH_MINIMUM_DAYS_IN_FIRST_WEEK = 8244232525129275563L;
                final long fieldNameHashCode = jsonReader.readFieldNameHashCode();
                if (fieldNameHashCode == 8244232525129275563L) {
                    minimumDaysInFirstWeek = jsonReader.readInt32Value();
                }
                else {
                    if (fieldNameHashCode != ChronologyReader.HASH_ZONE_ID) {
                        throw new JSONException(jsonReader.info("not support fieldName " + jsonReader.getFieldName()));
                    }
                    zoneId = jsonReader.readString();
                }
            }
            if (minimumDaysInFirstWeek != null) {
                throw new JSONException(jsonReader.info("not support"));
            }
            if ("UTC".equals(zoneId)) {
                return this.utc;
            }
            final Object datetimeZone = this.forID.apply(zoneId);
            return this.getInstance.apply(datetimeZone);
        }
        
        static {
            HASH_ZONE_ID = Fnv.hashCode64("zoneId");
        }
    }
    
    static class GregorianChronologyWriter implements ObjectWriter
    {
        final Class objectClass;
        final ToIntFunction getMinimumDaysInFirstWeek;
        final Function getZone;
        final Function getID;
        
        GregorianChronologyWriter(final Class objectClass) {
            this.objectClass = objectClass;
            try {
                this.getMinimumDaysInFirstWeek = LambdaMiscCodec.createToIntFunction(objectClass.getMethod("getMinimumDaysInFirstWeek", (Class[])new Class[0]));
                final Method method = objectClass.getMethod("getZone", (Class[])new Class[0]);
                this.getZone = LambdaMiscCodec.createFunction(method);
                this.getID = LambdaMiscCodec.createFunction(method.getReturnType().getMethod("getID", (Class<?>[])new Class[0]));
            }
            catch (NoSuchMethodException e) {
                throw new JSONException("getMethod error", e);
            }
        }
        
        @Override
        public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            final Object zone = this.getZone.apply(object);
            final String zoneId = this.getID.apply(zone);
            final int minDaysInFirstWeek = this.getMinimumDaysInFirstWeek.applyAsInt(object);
            jsonWriter.startObject();
            if (minDaysInFirstWeek != 4) {
                jsonWriter.writeName("minimumDaysInFirstWeek");
                jsonWriter.writeInt32(minDaysInFirstWeek);
            }
            jsonWriter.writeName("zoneId");
            jsonWriter.writeString(zoneId);
            jsonWriter.endObject();
        }
        
        @Override
        public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            final Object zone = this.getZone.apply(object);
            final String zoneId = this.getID.apply(zone);
            final int minDaysInFirstWeek = this.getMinimumDaysInFirstWeek.applyAsInt(object);
            jsonWriter.startObject();
            jsonWriter.writeName("minimumDaysInFirstWeek");
            jsonWriter.writeInt32(minDaysInFirstWeek);
            jsonWriter.writeName("zoneId");
            jsonWriter.writeString(zoneId);
            jsonWriter.endObject();
        }
    }
    
    static class ISOChronologyWriter implements ObjectWriter
    {
        final Class objectClass;
        final Function getZone;
        final Function getID;
        
        ISOChronologyWriter(final Class objectClass) {
            this.objectClass = objectClass;
            try {
                final Method method = objectClass.getMethod("getZone", (Class[])new Class[0]);
                this.getZone = LambdaMiscCodec.createFunction(method);
                this.getID = LambdaMiscCodec.createFunction(method.getReturnType().getMethod("getID", (Class<?>[])new Class[0]));
            }
            catch (NoSuchMethodException e) {
                throw new JSONException("getMethod error", e);
            }
        }
        
        @Override
        public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            final Object zone = this.getZone.apply(object);
            final String zoneId = this.getID.apply(zone);
            jsonWriter.startObject();
            jsonWriter.writeName("zoneId");
            jsonWriter.writeString(zoneId);
            jsonWriter.endObject();
        }
        
        @Override
        public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            final Object zone = this.getZone.apply(object);
            final String zoneId = this.getID.apply(zone);
            jsonWriter.startObject();
            jsonWriter.writeName("zoneId");
            jsonWriter.writeString(zoneId);
            jsonWriter.endObject();
        }
    }
    
    static class LocalDateReader implements ObjectReader
    {
        final Class objectClass;
        final Constructor constructor3;
        final Constructor constructor4;
        final Class classISOChronology;
        final Class classChronology;
        final Object utc;
        
        LocalDateReader(final Class objectClass) {
            this.objectClass = objectClass;
            try {
                final ClassLoader classLoader = objectClass.getClassLoader();
                this.classChronology = classLoader.loadClass("org.joda.time.Chronology");
                this.constructor3 = objectClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
                this.constructor4 = objectClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, this.classChronology);
                this.classISOChronology = classLoader.loadClass("org.joda.time.chrono.ISOChronology");
                this.utc = this.classISOChronology.getMethod("getInstance", (Class[])new Class[0]).invoke(null, new Object[0]);
            }
            catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
                final Exception ex;
                final Exception e = ex;
                throw new JSONException("create LocalDateWriter error", e);
            }
        }
        
        @Override
        public Class getObjectClass() {
            return this.objectClass;
        }
        
        @Override
        public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            if (jsonReader.nextIfNull()) {
                return null;
            }
            final LocalDate localDate = jsonReader.readLocalDate();
            if (localDate == null) {
                return null;
            }
            try {
                return this.constructor4.newInstance(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), null);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException(jsonReader.info("read org.joda.time.LocalDate error"), e);
            }
        }
        
        @Override
        public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            final byte type = jsonReader.getType();
            if (type == -87) {
                final LocalDate localDate = jsonReader.readLocalDate();
                try {
                    return this.constructor3.newInstance(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
                }
                catch (InstantiationException | IllegalAccessException | InvocationTargetException ex3) {
                    final ReflectiveOperationException ex;
                    final ReflectiveOperationException e = ex;
                    throw new JSONException(jsonReader.info("read org.joda.time.LocalDate error"), e);
                }
            }
            if (jsonReader.isObject()) {
                Integer year = null;
                Integer month = null;
                Integer day = null;
                Object chronology = null;
                jsonReader.nextIfObjectStart();
                while (!jsonReader.nextIfObjectEnd()) {
                    final long fieldNameHashCode = jsonReader.readFieldNameHashCode();
                    if (fieldNameHashCode == JodaSupport.HASH_YEAR) {
                        year = jsonReader.readInt32Value();
                    }
                    else if (fieldNameHashCode == JodaSupport.HASH_MONTH) {
                        month = jsonReader.readInt32Value();
                    }
                    else if (fieldNameHashCode == JodaSupport.HASH_DAY) {
                        day = jsonReader.readInt32Value();
                    }
                    else {
                        if (fieldNameHashCode != JodaSupport.HASH_CHRONOLOGY) {
                            throw new JSONException(jsonReader.info("not support fieldName " + jsonReader.getFieldName()));
                        }
                        chronology = jsonReader.read((Class<Object>)this.classChronology);
                    }
                }
                try {
                    return this.constructor4.newInstance(year, month, day, chronology);
                }
                catch (InstantiationException | IllegalAccessException | InvocationTargetException ex4) {
                    final ReflectiveOperationException ex2;
                    final ReflectiveOperationException e2 = ex2;
                    throw new JSONException(jsonReader.info("read org.joda.time.LocalDate error"), e2);
                }
            }
            throw new JSONException(jsonReader.info("not support " + JSONB.typeName(type)));
        }
    }
    
    static class LocalDateWriter extends DateTimeCodec implements ObjectWriter
    {
        final Class objectClass;
        final ToIntFunction getYear;
        final ToIntFunction getMonthOfYear;
        final ToIntFunction getDayOfMonth;
        final Function getChronology;
        final Class isoChronology;
        final Object utc;
        
        LocalDateWriter(final Class objectClass, final String format) {
            super(format);
            this.objectClass = objectClass;
            try {
                final ClassLoader classLoader = objectClass.getClassLoader();
                this.isoChronology = classLoader.loadClass("org.joda.time.chrono.ISOChronology");
                final Object instance = this.isoChronology.getMethod("getInstance", (Class[])new Class[0]).invoke(null, new Object[0]);
                this.utc = this.isoChronology.getMethod("withUTC", (Class[])new Class[0]).invoke(instance, new Object[0]);
                this.getYear = LambdaMiscCodec.createToIntFunction(objectClass.getMethod("getYear", (Class[])new Class[0]));
                this.getMonthOfYear = LambdaMiscCodec.createToIntFunction(objectClass.getMethod("getMonthOfYear", (Class[])new Class[0]));
                this.getDayOfMonth = LambdaMiscCodec.createToIntFunction(objectClass.getMethod("getDayOfMonth", (Class[])new Class[0]));
                this.getChronology = LambdaMiscCodec.createFunction(objectClass.getMethod("getChronology", (Class[])new Class[0]));
            }
            catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
                final Exception ex;
                final Exception e = ex;
                throw new JSONException("create LocalDateWriter error", e);
            }
        }
        
        @Override
        public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            final int year = this.getYear.applyAsInt(object);
            final int monthOfYear = this.getMonthOfYear.applyAsInt(object);
            final int dayOfMonth = this.getDayOfMonth.applyAsInt(object);
            final Object chronology = this.getChronology.apply(object);
            if (jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
                jsonWriter.writeTypeName(TypeUtils.getTypeName(object.getClass()));
            }
            if (chronology == this.utc || chronology == null) {
                jsonWriter.writeLocalDate(LocalDate.of(year, monthOfYear, dayOfMonth));
                return;
            }
            jsonWriter.startObject();
            jsonWriter.writeName("year");
            jsonWriter.writeInt32(year);
            jsonWriter.writeName("month");
            jsonWriter.writeInt32(monthOfYear);
            jsonWriter.writeName("day");
            jsonWriter.writeInt32(dayOfMonth);
            jsonWriter.writeName("chronology");
            jsonWriter.writeAny(chronology);
            jsonWriter.endObject();
        }
        
        @Override
        public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            final int year = this.getYear.applyAsInt(object);
            final int monthOfYear = this.getMonthOfYear.applyAsInt(object);
            final int dayOfMonth = this.getDayOfMonth.applyAsInt(object);
            final Object chronology = this.getChronology.apply(object);
            if (chronology != this.utc && chronology != null) {
                jsonWriter.startObject();
                jsonWriter.writeName("year");
                jsonWriter.writeInt32(year);
                jsonWriter.writeName("month");
                jsonWriter.writeInt32(monthOfYear);
                jsonWriter.writeName("day");
                jsonWriter.writeInt32(dayOfMonth);
                jsonWriter.writeName("chronology");
                jsonWriter.writeAny(chronology);
                jsonWriter.endObject();
                return;
            }
            final LocalDate localDate = LocalDate.of(year, monthOfYear, dayOfMonth);
            DateTimeFormatter formatter = this.getDateFormatter();
            if (formatter == null) {
                formatter = jsonWriter.context.getDateFormatter();
            }
            if (formatter == null) {
                jsonWriter.writeLocalDate(localDate);
                return;
            }
            final String str = formatter.format(localDate);
            jsonWriter.writeString(str);
        }
    }
    
    static class LocalDateTimeReader implements ObjectReader
    {
        final Class objectClass;
        final Constructor constructor7;
        final Constructor constructor8;
        final Class classISOChronology;
        final Class classChronology;
        final Object utc;
        
        LocalDateTimeReader(final Class objectClass) {
            this.objectClass = objectClass;
            try {
                final ClassLoader classLoader = objectClass.getClassLoader();
                this.classChronology = classLoader.loadClass("org.joda.time.Chronology");
                this.constructor7 = objectClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
                this.constructor8 = objectClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, this.classChronology);
                this.classISOChronology = classLoader.loadClass("org.joda.time.chrono.ISOChronology");
                this.utc = this.classISOChronology.getMethod("getInstance", (Class[])new Class[0]).invoke(null, new Object[0]);
            }
            catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
                final Exception ex;
                final Exception e = ex;
                throw new JSONException("create LocalDateWriter error", e);
            }
        }
        
        @Override
        public Class getObjectClass() {
            return this.objectClass;
        }
        
        @Override
        public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            if (jsonReader.isString() || jsonReader.isInt()) {
                final LocalDateTime ldt = jsonReader.readLocalDateTime();
                if (ldt == null) {
                    return null;
                }
                try {
                    return this.constructor7.newInstance(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond(), ldt.getNano() / 1000000);
                }
                catch (InstantiationException | IllegalAccessException | InvocationTargetException ex2) {
                    final ReflectiveOperationException ex;
                    final ReflectiveOperationException e = ex;
                    throw new JSONException(jsonReader.info("read org.joda.time.LocalDate error"), e);
                }
            }
            throw new JSONException(jsonReader.info("not support"));
        }
        
        @Override
        public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            final byte type = jsonReader.getType();
            if (type == -87) {
                final LocalDate localDate = jsonReader.readLocalDate();
                try {
                    return this.constructor7.newInstance(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0, 0);
                }
                catch (InstantiationException | IllegalAccessException | InvocationTargetException ex4) {
                    final ReflectiveOperationException ex;
                    final ReflectiveOperationException e = ex;
                    throw new JSONException(jsonReader.info("read org.joda.time.LocalDate error"), e);
                }
            }
            if (type == -88) {
                final LocalDateTime ldt = jsonReader.readLocalDateTime();
                try {
                    return this.constructor7.newInstance(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond(), ldt.getNano() / 1000000);
                }
                catch (InstantiationException | IllegalAccessException | InvocationTargetException ex5) {
                    final ReflectiveOperationException ex2;
                    final ReflectiveOperationException e = ex2;
                    throw new JSONException(jsonReader.info("read org.joda.time.LocalDate error"), e);
                }
            }
            if (jsonReader.isObject()) {
                Integer year = null;
                Integer month = null;
                Integer day = null;
                Integer hour = null;
                Integer minute = null;
                Integer second = null;
                Integer millis = null;
                Object chronology = null;
                jsonReader.nextIfObjectStart();
                while (!jsonReader.nextIfObjectEnd()) {
                    final long fieldNameHashCode = jsonReader.readFieldNameHashCode();
                    if (fieldNameHashCode == JodaSupport.HASH_YEAR) {
                        year = jsonReader.readInt32Value();
                    }
                    else if (fieldNameHashCode == JodaSupport.HASH_MONTH) {
                        month = jsonReader.readInt32Value();
                    }
                    else if (fieldNameHashCode == JodaSupport.HASH_DAY) {
                        day = jsonReader.readInt32Value();
                    }
                    else if (fieldNameHashCode == JodaSupport.HASH_HOUR) {
                        hour = jsonReader.readInt32Value();
                    }
                    else if (fieldNameHashCode == JodaSupport.HASH_MINUTE) {
                        minute = jsonReader.readInt32Value();
                    }
                    else if (fieldNameHashCode == JodaSupport.HASH_SECOND) {
                        second = jsonReader.readInt32Value();
                    }
                    else if (fieldNameHashCode == JodaSupport.HASH_MILLIS) {
                        millis = jsonReader.readInt32Value();
                    }
                    else {
                        if (fieldNameHashCode != JodaSupport.HASH_CHRONOLOGY) {
                            throw new JSONException(jsonReader.info("not support fieldName " + jsonReader.getFieldName()));
                        }
                        chronology = jsonReader.read((Class<Object>)this.classChronology);
                    }
                }
                try {
                    return this.constructor8.newInstance(year, month, day, hour, minute, second, millis, chronology);
                }
                catch (InstantiationException | IllegalAccessException | InvocationTargetException ex6) {
                    final ReflectiveOperationException ex3;
                    final ReflectiveOperationException e2 = ex3;
                    throw new JSONException(jsonReader.info("read org.joda.time.LocalDate error"), e2);
                }
            }
            throw new JSONException(jsonReader.info("not support " + JSONB.typeName(type)));
        }
    }
    
    static class LocalDateTimeWriter extends DateTimeCodec implements ObjectWriter
    {
        final Class objectClass;
        final Method getYear;
        final Method getMonthOfYear;
        final Method getDayOfMonth;
        final ToIntFunction getHourOfDay;
        final ToIntFunction getMinuteOfHour;
        final ToIntFunction getSecondOfMinute;
        final ToIntFunction getMillisOfSecond;
        final Function getChronology;
        final Class isoChronology;
        final Object utc;
        
        LocalDateTimeWriter(final Class objectClass, final String format) {
            super(format);
            this.objectClass = objectClass;
            try {
                final ClassLoader classLoader = objectClass.getClassLoader();
                this.isoChronology = classLoader.loadClass("org.joda.time.chrono.ISOChronology");
                final Object instance = this.isoChronology.getMethod("getInstance", (Class[])new Class[0]).invoke(null, new Object[0]);
                this.utc = this.isoChronology.getMethod("withUTC", (Class[])new Class[0]).invoke(instance, new Object[0]);
                this.getYear = objectClass.getMethod("getYear", (Class[])new Class[0]);
                this.getMonthOfYear = objectClass.getMethod("getMonthOfYear", (Class[])new Class[0]);
                this.getDayOfMonth = objectClass.getMethod("getDayOfMonth", (Class[])new Class[0]);
                this.getHourOfDay = LambdaMiscCodec.createToIntFunction(objectClass.getMethod("getHourOfDay", (Class[])new Class[0]));
                this.getMinuteOfHour = LambdaMiscCodec.createToIntFunction(objectClass.getMethod("getMinuteOfHour", (Class[])new Class[0]));
                this.getSecondOfMinute = LambdaMiscCodec.createToIntFunction(objectClass.getMethod("getSecondOfMinute", (Class[])new Class[0]));
                this.getMillisOfSecond = LambdaMiscCodec.createToIntFunction(objectClass.getMethod("getMillisOfSecond", (Class[])new Class[0]));
                this.getChronology = LambdaMiscCodec.createFunction(objectClass.getMethod("getChronology", (Class[])new Class[0]));
            }
            catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
                final Exception ex;
                final Exception e = ex;
                throw new JSONException("create LocalDateWriter error", e);
            }
        }
        
        @Override
        public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            try {
                final int year = (int)this.getYear.invoke(object, new Object[0]);
                final int monthOfYear = (int)this.getMonthOfYear.invoke(object, new Object[0]);
                final int dayOfMonth = (int)this.getDayOfMonth.invoke(object, new Object[0]);
                final int hour = this.getHourOfDay.applyAsInt(object);
                final int minute = this.getMinuteOfHour.applyAsInt(object);
                final int second = this.getSecondOfMinute.applyAsInt(object);
                final int millis = this.getMillisOfSecond.applyAsInt(object);
                final Object chronology = this.getChronology.apply(object);
                if (jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
                    jsonWriter.writeTypeName(TypeUtils.getTypeName(object.getClass()));
                }
                if (chronology == this.utc || chronology == null) {
                    jsonWriter.writeLocalDateTime(LocalDateTime.of(year, monthOfYear, dayOfMonth, hour, minute, second, millis * 1000000));
                    return;
                }
                jsonWriter.startObject();
                jsonWriter.writeName("year");
                jsonWriter.writeInt32(year);
                jsonWriter.writeName("month");
                jsonWriter.writeInt32(monthOfYear);
                jsonWriter.writeName("day");
                jsonWriter.writeInt32(dayOfMonth);
                jsonWriter.writeName("hour");
                jsonWriter.writeInt32(hour);
                jsonWriter.writeName("minute");
                jsonWriter.writeInt32(minute);
                jsonWriter.writeName("second");
                jsonWriter.writeInt32(second);
                jsonWriter.writeName("millis");
                jsonWriter.writeInt32(millis);
                jsonWriter.writeName("chronology");
                jsonWriter.writeAny(chronology);
                jsonWriter.endObject();
            }
            catch (IllegalAccessException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("write LocalDateWriter error", e);
            }
        }
        
        @Override
        public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            try {
                final int year = (int)this.getYear.invoke(object, new Object[0]);
                final int monthOfYear = (int)this.getMonthOfYear.invoke(object, new Object[0]);
                final int dayOfMonth = (int)this.getDayOfMonth.invoke(object, new Object[0]);
                final int hour = this.getHourOfDay.applyAsInt(object);
                final int minute = this.getMinuteOfHour.applyAsInt(object);
                final int second = this.getSecondOfMinute.applyAsInt(object);
                final int millis = this.getMillisOfSecond.applyAsInt(object);
                final Object chronology = this.getChronology.apply(object);
                if (jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
                    jsonWriter.writeTypeName(TypeUtils.getTypeName(object.getClass()));
                }
                if (chronology == this.utc || chronology == null) {
                    final int nanoOfSecond = millis * 1000000;
                    final LocalDateTime ldt = LocalDateTime.of(year, monthOfYear, dayOfMonth, hour, minute, second, nanoOfSecond);
                    DateTimeFormatter formatter = this.getDateFormatter();
                    if (formatter == null) {
                        formatter = jsonWriter.context.getDateFormatter();
                    }
                    if (formatter == null) {
                        jsonWriter.writeLocalDateTime(ldt);
                        return;
                    }
                    final String str = formatter.format(ldt);
                    jsonWriter.writeString(str);
                }
                else {
                    jsonWriter.startObject();
                    jsonWriter.writeName("year");
                    jsonWriter.writeInt32(year);
                    jsonWriter.writeName("month");
                    jsonWriter.writeInt32(monthOfYear);
                    jsonWriter.writeName("day");
                    jsonWriter.writeInt32(dayOfMonth);
                    jsonWriter.writeName("hour");
                    jsonWriter.writeInt32(hour);
                    jsonWriter.writeName("minute");
                    jsonWriter.writeInt32(minute);
                    jsonWriter.writeName("second");
                    jsonWriter.writeInt32(second);
                    jsonWriter.writeName("millis");
                    jsonWriter.writeInt32(millis);
                    jsonWriter.writeName("chronology");
                    jsonWriter.writeAny(chronology);
                    jsonWriter.endObject();
                }
            }
            catch (IllegalAccessException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("write LocalDateWriter error", e);
            }
        }
    }
    
    public static final class DateTimeFromZDT implements Function
    {
        static Constructor CONS;
        static Method FOR_ID;
        
        @Override
        public Object apply(final Object o) {
            final ZonedDateTime zdt = (ZonedDateTime)o;
            try {
                if (DateTimeFromZDT.FOR_ID == null) {
                    final Class<?> zoneClass = Class.forName("org.joda.time.DateTimeZone");
                    DateTimeFromZDT.FOR_ID = zoneClass.getMethod("forID", String.class);
                }
                if (DateTimeFromZDT.CONS == null) {
                    DateTimeFromZDT.CONS = Class.forName("org.joda.time.DateTime").getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, DateTimeFromZDT.FOR_ID.getDeclaringClass());
                }
                String zondId = zdt.getZone().getId();
                if ("Z".equals(zondId)) {
                    zondId = "UTC";
                }
                return DateTimeFromZDT.CONS.newInstance(zdt.getYear(), zdt.getMonthValue(), zdt.getDayOfMonth(), zdt.getHour(), zdt.getMinute(), zdt.getSecond(), zdt.getNano() / 1000000, DateTimeFromZDT.FOR_ID.invoke(null, zondId));
            }
            catch (Exception e) {
                throw new JSONException("build DateTime error", e);
            }
        }
    }
    
    public static final class DateTime2ZDT implements Function
    {
        static Class CLASS;
        static ToIntFunction YEAR;
        static ToIntFunction MONTH;
        static ToIntFunction DAY_OF_MONTH;
        static ToIntFunction HOUR;
        static ToIntFunction MINUTE;
        static ToIntFunction SECOND;
        static ToIntFunction MILLIS;
        static Function GET_ZONE;
        static Function GET_ID;
        
        @Override
        public Object apply(final Object o) {
            try {
                if (DateTime2ZDT.CLASS == null) {
                    DateTime2ZDT.CLASS = Class.forName("org.joda.time.DateTime");
                }
                if (DateTime2ZDT.YEAR == null) {
                    DateTime2ZDT.YEAR = LambdaMiscCodec.createToIntFunction(DateTime2ZDT.CLASS.getMethod("getYear", (Class[])new Class[0]));
                }
                if (DateTime2ZDT.MONTH == null) {
                    DateTime2ZDT.MONTH = LambdaMiscCodec.createToIntFunction(DateTime2ZDT.CLASS.getMethod("getMonthOfYear", (Class[])new Class[0]));
                }
                if (DateTime2ZDT.DAY_OF_MONTH == null) {
                    DateTime2ZDT.DAY_OF_MONTH = LambdaMiscCodec.createToIntFunction(DateTime2ZDT.CLASS.getMethod("getDayOfMonth", (Class[])new Class[0]));
                }
                if (DateTime2ZDT.HOUR == null) {
                    DateTime2ZDT.HOUR = LambdaMiscCodec.createToIntFunction(DateTime2ZDT.CLASS.getMethod("getHourOfDay", (Class[])new Class[0]));
                }
                if (DateTime2ZDT.MINUTE == null) {
                    DateTime2ZDT.MINUTE = LambdaMiscCodec.createToIntFunction(DateTime2ZDT.CLASS.getMethod("getMinuteOfHour", (Class[])new Class[0]));
                }
                if (DateTime2ZDT.SECOND == null) {
                    DateTime2ZDT.SECOND = LambdaMiscCodec.createToIntFunction(DateTime2ZDT.CLASS.getMethod("getSecondOfMinute", (Class[])new Class[0]));
                }
                if (DateTime2ZDT.MILLIS == null) {
                    DateTime2ZDT.MILLIS = LambdaMiscCodec.createToIntFunction(DateTime2ZDT.CLASS.getMethod("getMillisOfSecond", (Class[])new Class[0]));
                }
                if (DateTime2ZDT.GET_ZONE == null) {
                    DateTime2ZDT.GET_ZONE = LambdaMiscCodec.createFunction(DateTime2ZDT.CLASS.getMethod("getZone", (Class[])new Class[0]));
                }
                if (DateTime2ZDT.GET_ID == null) {
                    DateTime2ZDT.GET_ID = LambdaMiscCodec.createFunction(Class.forName("org.joda.time.DateTimeZone").getMethod("getID", (Class<?>[])new Class[0]));
                }
                final Object zone = DateTime2ZDT.GET_ZONE.apply(o);
                final String zonIdStr = DateTime2ZDT.GET_ID.apply(zone);
                final ZoneId zoneId = ZoneId.of(zonIdStr);
                return ZonedDateTime.of(DateTime2ZDT.YEAR.applyAsInt(o), DateTime2ZDT.MONTH.applyAsInt(o), DateTime2ZDT.DAY_OF_MONTH.applyAsInt(o), DateTime2ZDT.HOUR.applyAsInt(o), DateTime2ZDT.MINUTE.applyAsInt(o), DateTime2ZDT.SECOND.applyAsInt(o), DateTime2ZDT.MILLIS.applyAsInt(o) * 1000000, zoneId);
            }
            catch (Exception e) {
                throw new JSONException("convert joda org.joda.time.DateTime to java.time.ZonedDateTime error", e);
            }
        }
    }
}
