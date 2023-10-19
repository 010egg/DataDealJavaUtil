// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import java.time.OffsetTime;
import java.time.temporal.Temporal;
import scala.util.Try$;
import scala.util.Try;
import java.time.temporal.TemporalField;
import java.time.temporal.ChronoField;
import scala.runtime.BoxesRunTime;
import scala.PartialFunction;
import scala.Function0;
import scala.Function1;
import scala.Tuple2;
import scala.reflect.ClassTag$;
import scala.Array$;
import org.apache.spark.sql.types.StructField;
import scala.Predef$;
import scala.runtime.BoxedUnit;
import org.apache.spark.sql.catalyst.expressions.SpecificInternalRow;
import scala.Function2;
import org.apache.spark.sql.types.StructType;
import scala.collection.mutable.StringBuilder;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.LocalDate;

public final class DataTypeConverterFactory$
{
    public static final DataTypeConverterFactory$ MODULE$;
    private final LocalDate io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$epochDate;
    private final ZoneId io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$localTimeZone;
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter timeFormatter;
    private final String optionalTzPattern;
    private final DateTimeFormatter io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$timeTzFormatter;
    private final DateTimeFormatterBuilder timestampFormatterBuilder;
    private final DateTimeFormatter io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$timestampFormatter;
    private final DateTimeFormatter io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$timestampTzFormatter;
    
    static {
        new DataTypeConverterFactory$();
    }
    
    public boolean io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$parseBoolean(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("For input string: \"null\"");
        }
        final String lowerCase = s.toLowerCase();
        boolean b;
        if ("t".equals(lowerCase)) {
            b = true;
        }
        else {
            if (!"f".equals(lowerCase)) {
                throw new IllegalArgumentException(new StringBuilder().append((Object)"For input string: \"").append((Object)s).append((Object)"\"").toString());
            }
            b = false;
        }
        return b;
    }
    
    public LocalDate io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$epochDate() {
        return this.io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$epochDate;
    }
    
    public ZoneId io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$localTimeZone() {
        return this.io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$localTimeZone;
    }
    
    private DateTimeFormatter dateFormatter() {
        return this.dateFormatter;
    }
    
    private DateTimeFormatter timeFormatter() {
        return this.timeFormatter;
    }
    
    private String optionalTzPattern() {
        return this.optionalTzPattern;
    }
    
    public DateTimeFormatter io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$timeTzFormatter() {
        return this.io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$timeTzFormatter;
    }
    
    private DateTimeFormatterBuilder timestampFormatterBuilder() {
        return this.timestampFormatterBuilder;
    }
    
    public DateTimeFormatter io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$timestampFormatter() {
        return this.io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$timestampFormatter;
    }
    
    public DateTimeFormatter io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$timestampTzFormatter() {
        return this.io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$timestampTzFormatter;
    }
    
    public Function2<String, SpecificInternalRow, BoxedUnit>[] create(final StructType schema) {
        final Function2[] conversionFunctions = (Function2[])Predef$.MODULE$.refArrayOps((Object[])Predef$.MODULE$.refArrayOps((Object[])schema.fields()).zipWithIndex(Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)Tuple2.class)))).map((Function1)new DataTypeConverterFactory$$anonfun.DataTypeConverterFactory$$anonfun$1(), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)Function2.class)));
        return (Function2<String, SpecificInternalRow, BoxedUnit>[])conversionFunctions;
    }
    
    public long io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$convertToSqlTimestamp(final String value) {
        return BoxesRunTime.unboxToLong(this.parseTimestamp(value).orElse((Function0)new DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$convertToSqlTimestamp.DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$convertToSqlTimestamp$2(value)).orElse((Function0)new DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$convertToSqlTimestamp.DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$convertToSqlTimestamp$3(value)).orElse((Function0)new DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$convertToSqlTimestamp.DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$convertToSqlTimestamp$4(value)).recoverWith((PartialFunction)new DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$convertToSqlTimestamp.DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$convertToSqlTimestamp$1(value)).get());
    }
    
    public int io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$parseDate(final String value) {
        return (int)LocalDate.parse(value, this.dateFormatter()).getLong(ChronoField.EPOCH_DAY);
    }
    
    private Try<Object> parseTimestamp(final String value) {
        return (Try<Object>)Try$.MODULE$.apply((Function0)new DataTypeConverterFactory$$anonfun$parseTimestamp.DataTypeConverterFactory$$anonfun$parseTimestamp$1(value));
    }
    
    public Try<Object> io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$parseTimestampTz(final String value) {
        return (Try<Object>)Try$.MODULE$.apply((Function0)new DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$parseTimestampTz.DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$parseTimestampTz$1(value));
    }
    
    public Try<Object> io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$parseTimeTz(final String value) {
        return (Try<Object>)Try$.MODULE$.apply((Function0)new DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$parseTimeTz.DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$parseTimeTz$1(value));
    }
    
    public Try<Object> io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$parseTime(final String value) {
        return (Try<Object>)Try$.MODULE$.apply((Function0)new DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$parseTime.DataTypeConverterFactory$$anonfun$io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$parseTime$1(value));
    }
    
    public long io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$microsFor(final Temporal temporal) {
        return temporal.getLong(ChronoField.INSTANT_SECONDS) * 1000L * 1000L + temporal.getLong(ChronoField.MICRO_OF_SECOND);
    }
    
    public long io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$microsFor(final OffsetTime dateTime) {
        final long secondOfDay = dateTime.getLong(ChronoField.SECOND_OF_DAY);
        final long offsetSeconds = dateTime.getLong(ChronoField.OFFSET_SECONDS);
        final long offsetMicrosOfDay = (secondOfDay - offsetSeconds) * 1000L * 1000L;
        return offsetMicrosOfDay + dateTime.getLong(ChronoField.MICRO_OF_SECOND);
    }
    
    private DataTypeConverterFactory$() {
        MODULE$ = this;
        this.io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$epochDate = LocalDate.parse("1970-01-01");
        this.io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$localTimeZone = ZoneId.systemDefault();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.optionalTzPattern = "[xxx][x]";
        this.io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$timeTzFormatter = new DateTimeFormatterBuilder().append(this.timeFormatter()).appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true).appendPattern(this.optionalTzPattern()).toFormatter();
        this.timestampFormatterBuilder = new DateTimeFormatterBuilder().append(this.dateFormatter()).appendLiteral(" ").append(this.timeFormatter()).appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true);
        this.io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$timestampFormatter = this.timestampFormatterBuilder().toFormatter();
        this.io$pivotal$greenplum$spark$externaltable$DataTypeConverterFactory$$timestampTzFormatter = this.timestampFormatterBuilder().appendPattern(this.optionalTzPattern()).toFormatter();
    }
}
