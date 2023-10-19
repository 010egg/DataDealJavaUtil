// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ChronoField;
import java.time.OffsetTime;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Locale;
import com.alibaba.fastjson2.codec.DateTimeCodec;

final class ObjectWriterImplOffsetTime extends DateTimeCodec implements ObjectWriter
{
    static final ObjectWriterImplOffsetTime INSTANCE;
    
    public ObjectWriterImplOffsetTime(final String format, final Locale locale) {
        super(format, locale);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final JSONWriter.Context ctx = jsonWriter.context;
        final OffsetTime time = (OffsetTime)object;
        DateTimeFormatter formatter = this.getDateFormatter();
        if (formatter == null) {
            formatter = ctx.getDateFormatter();
        }
        if (formatter == null) {
            final int hour = time.get(ChronoField.HOUR_OF_DAY);
            final int minute = time.get(ChronoField.MINUTE_OF_HOUR);
            final int second = time.get(ChronoField.SECOND_OF_MINUTE);
            jsonWriter.writeTimeHHMMSS8(hour, minute, second);
        }
        else {
            final String str = formatter.format(time);
            jsonWriter.writeString(str);
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImplOffsetTime(null, null);
    }
}
