// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.JodaSupport;
import com.alibaba.fastjson2.util.JdbcSupport;
import java.util.Optional;
import java.util.function.Function;
import java.time.LocalTime;
import java.time.LocalDate;
import com.alibaba.fastjson2.JSONFactory;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.List;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.Instant;
import java.util.Date;
import java.io.Closeable;
import com.alibaba.fastjson2.util.IOUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Member;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.JSONException;
import java.util.Map;
import com.alibaba.fastjson2.SymbolTable;
import com.alibaba.fastjson2.util.JDKUtils;
import java.math.BigDecimal;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Modifier;
import java.io.Serializable;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.util.Fnv;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.lang.reflect.Type;

public abstract class FieldWriter<T> implements Comparable
{
    public final String fieldName;
    public final Type fieldType;
    public final Class fieldClass;
    public final long features;
    public final int ordinal;
    public final String format;
    public final DecimalFormat decimalFormat;
    public final String label;
    public final Field field;
    public final Method method;
    protected final long fieldOffset;
    protected final boolean primitive;
    final long hashCode;
    final byte[] nameWithColonUTF8;
    final char[] nameWithColonUTF16;
    final byte[] nameJSONB;
    long nameSymbolCache;
    final boolean fieldClassSerializable;
    final JSONWriter.Path rootParentPath;
    final boolean symbol;
    final boolean trim;
    final boolean raw;
    transient JSONWriter.Path path;
    volatile ObjectWriter initObjectWriter;
    Object defaultValue;
    static final AtomicReferenceFieldUpdater<FieldWriter, ObjectWriter> initObjectWriterUpdater;
    
    FieldWriter(final String name, final int ordinal, long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method) {
        if ("string".equals(format) && fieldClass != String.class) {
            features |= JSONWriter.Feature.WriteNonStringValueAsString.mask;
        }
        this.fieldName = name;
        this.ordinal = ordinal;
        this.format = format;
        this.label = label;
        this.hashCode = Fnv.hashCode64(name);
        this.features = features;
        this.fieldType = TypeUtils.intern(fieldType);
        this.fieldClassSerializable = ((this.fieldClass = fieldClass) != null && (Serializable.class.isAssignableFrom(fieldClass) || !Modifier.isFinal(fieldClass.getModifiers())));
        this.field = field;
        this.method = method;
        this.primitive = fieldClass.isPrimitive();
        this.nameJSONB = JSONB.toBytes(this.fieldName);
        DecimalFormat decimalFormat = null;
        if (format != null && (fieldClass == Float.TYPE || fieldClass == float[].class || fieldClass == Float.class || fieldClass == Float[].class || fieldClass == Double.TYPE || fieldClass == double[].class || fieldClass == Double.class || fieldClass == Double[].class || fieldClass == BigDecimal.class || fieldClass == BigDecimal[].class)) {
            decimalFormat = new DecimalFormat(format);
        }
        this.decimalFormat = decimalFormat;
        long fieldOffset = -1L;
        if (field != null) {
            fieldOffset = JDKUtils.UNSAFE.objectFieldOffset(field);
        }
        this.fieldOffset = fieldOffset;
        this.symbol = "symbol".equals(format);
        this.trim = "trim".equals(format);
        this.raw = ((features & 0x4000000000000L) != 0x0L);
        this.rootParentPath = new JSONWriter.Path(JSONWriter.Path.ROOT, name);
        final int nameLength = name.length();
        int utflen = nameLength + 3;
        for (int i = 0; i < nameLength; ++i) {
            final char c = name.charAt(i);
            if (c < '\u0001' || c > '\u007f') {
                if (c > '\u07ff') {
                    utflen += 2;
                }
                else {
                    ++utflen;
                }
            }
        }
        final byte[] bytes = new byte[utflen];
        int off = 0;
        bytes[off++] = 34;
        for (int j = 0; j < nameLength; ++j) {
            final char c2 = name.charAt(j);
            if (c2 >= '\u0001' && c2 <= '\u007f') {
                bytes[off++] = (byte)c2;
            }
            else if (c2 > '\u07ff') {
                bytes[off++] = (byte)(0xE0 | (c2 >> 12 & 0xF));
                bytes[off++] = (byte)(0x80 | (c2 >> 6 & 0x3F));
                bytes[off++] = (byte)(0x80 | (c2 & '?'));
            }
            else {
                bytes[off++] = (byte)(0xC0 | (c2 >> 6 & 0x1F));
                bytes[off++] = (byte)(0x80 | (c2 & '?'));
            }
        }
        bytes[off++] = 34;
        bytes[off] = 58;
        this.nameWithColonUTF8 = bytes;
        final char[] chars = new char[nameLength + 3];
        chars[0] = '\"';
        name.getChars(0, name.length(), chars, 1);
        chars[chars.length - 2] = '\"';
        chars[chars.length - 1] = ':';
        this.nameWithColonUTF16 = chars;
    }
    
    public boolean isFieldClassSerializable() {
        return this.fieldClassSerializable;
    }
    
    public boolean isDateFormatMillis() {
        return false;
    }
    
    public boolean isDateFormatISO8601() {
        return false;
    }
    
    public void writeEnumJSONB(final JSONWriter jsonWriter, final Enum e) {
        throw new UnsupportedOperationException();
    }
    
    public ObjectWriter getInitWriter() {
        return null;
    }
    
    public boolean unwrapped() {
        return false;
    }
    
    public final void writeFieldNameJSONB(final JSONWriter jsonWriter) {
        final SymbolTable symbolTable = jsonWriter.symbolTable;
        if (symbolTable != null && this.writeFieldNameSymbol(jsonWriter, symbolTable)) {
            return;
        }
        jsonWriter.writeNameRaw(this.nameJSONB, this.hashCode);
    }
    
    public final void writeFieldName(final JSONWriter jsonWriter) {
        if (!jsonWriter.jsonb) {
            if (!jsonWriter.useSingleQuote && (jsonWriter.context.getFeatures() & JSONWriter.Feature.UnquoteFieldName.mask) == 0x0L) {
                if (jsonWriter.utf8) {
                    jsonWriter.writeNameRaw(this.nameWithColonUTF8);
                    return;
                }
                if (jsonWriter.utf16) {
                    jsonWriter.writeNameRaw(this.nameWithColonUTF16);
                    return;
                }
            }
            jsonWriter.writeName(this.fieldName);
            jsonWriter.writeColon();
            return;
        }
        final SymbolTable symbolTable = jsonWriter.symbolTable;
        if (symbolTable != null && this.writeFieldNameSymbol(jsonWriter, symbolTable)) {
            return;
        }
        jsonWriter.writeNameRaw(this.nameJSONB, this.hashCode);
    }
    
    private boolean writeFieldNameSymbol(final JSONWriter jsonWriter, final SymbolTable symbolTable) {
        final int symbolTableIdentity = System.identityHashCode(symbolTable);
        int symbol;
        if (this.nameSymbolCache == 0L) {
            symbol = symbolTable.getOrdinalByHashCode(this.hashCode);
            this.nameSymbolCache = ((long)symbol << 32 | (long)symbolTableIdentity);
        }
        else if ((int)this.nameSymbolCache == symbolTableIdentity) {
            symbol = (int)(this.nameSymbolCache >> 32);
        }
        else {
            symbol = symbolTable.getOrdinalByHashCode(this.hashCode);
            this.nameSymbolCache = ((long)symbol << 32 | (long)symbolTableIdentity);
        }
        if (symbol != -1) {
            jsonWriter.writeSymbol(-symbol);
            return true;
        }
        return false;
    }
    
    public final JSONWriter.Path getRootParentPath() {
        return this.rootParentPath;
    }
    
    public final JSONWriter.Path getPath(final JSONWriter.Path parent) {
        if (this.path == null) {
            return this.path = new JSONWriter.Path(parent, this.fieldName);
        }
        if (this.path.parent == parent) {
            return this.path;
        }
        return new JSONWriter.Path(parent, this.fieldName);
    }
    
    public Type getItemType() {
        return null;
    }
    
    public Class getItemClass() {
        return null;
    }
    
    @Override
    public String toString() {
        return this.fieldName;
    }
    
    void setDefaultValue(final T object) {
        Object fieldValue = null;
        if (Iterable.class.isAssignableFrom(this.fieldClass) || Map.class.isAssignableFrom(this.fieldClass)) {
            return;
        }
        if (this.field != null && object != null) {
            try {
                this.field.setAccessible(true);
                fieldValue = this.field.get(object);
            }
            catch (Throwable t) {}
        }
        if (fieldValue == null) {
            return;
        }
        if (this.fieldClass == Boolean.TYPE) {
            if (fieldValue == Boolean.FALSE) {
                return;
            }
        }
        else if (this.fieldClass == Byte.TYPE || this.fieldClass == Short.TYPE || this.fieldClass == Integer.TYPE || this.fieldClass == Long.TYPE || this.fieldClass == Float.TYPE || this.fieldClass == Double.TYPE) {
            if (((Number)fieldValue).doubleValue() == 0.0) {
                return;
            }
        }
        else if (this.fieldClass == Character.TYPE && (char)fieldValue == '\0') {
            return;
        }
        this.defaultValue = fieldValue;
    }
    
    public Object getFieldValue(final T object) {
        if (object == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        if (this.field != null) {
            try {
                Object value;
                if (this.fieldOffset != -1L && !this.primitive) {
                    value = JDKUtils.UNSAFE.getObject(object, this.fieldOffset);
                }
                else {
                    value = this.field.get(object);
                }
                return value;
            }
            catch (IllegalArgumentException | IllegalAccessException ex2) {
                final Exception ex;
                final Exception e = ex;
                throw new JSONException("field.get error, " + this.fieldName, e);
            }
        }
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int compareTo(final Object o) {
        final FieldWriter other = (FieldWriter)o;
        final int thisOrdinal = this.ordinal;
        final int otherOrdinal = other.ordinal;
        if (thisOrdinal < otherOrdinal) {
            return -1;
        }
        if (thisOrdinal > otherOrdinal) {
            return 1;
        }
        final int nameCompare = this.fieldName.compareTo(other.fieldName);
        if (nameCompare != 0) {
            return nameCompare;
        }
        Member thisMember;
        if (this.method == null || (this.field != null && Modifier.isPublic(this.field.getModifiers()))) {
            thisMember = this.field;
        }
        else {
            thisMember = this.method;
        }
        Member otherMember;
        if (other.method == null || (other.field != null && Modifier.isPublic(other.field.getModifiers()))) {
            otherMember = other.field;
        }
        else {
            otherMember = other.method;
        }
        if (thisMember != null && otherMember != null) {
            final Class otherDeclaringClass = otherMember.getDeclaringClass();
            final Class thisDeclaringClass = thisMember.getDeclaringClass();
            if (thisDeclaringClass != otherDeclaringClass) {
                if (thisDeclaringClass.isAssignableFrom(otherDeclaringClass)) {
                    return 1;
                }
                if (otherDeclaringClass.isAssignableFrom(thisDeclaringClass)) {
                    return -1;
                }
            }
            JSONField thisField = null;
            JSONField otherField = null;
            if (thisMember instanceof Field) {
                thisField = ((Field)thisMember).getAnnotation(JSONField.class);
            }
            else if (thisMember instanceof Method) {
                thisField = ((Method)thisMember).getAnnotation(JSONField.class);
            }
            if (otherMember instanceof Field) {
                otherField = ((Field)otherMember).getAnnotation(JSONField.class);
            }
            else if (thisMember instanceof Method) {
                otherField = ((Method)otherMember).getAnnotation(JSONField.class);
            }
            if (thisField != null && otherField == null) {
                return -1;
            }
            if (thisField == null && otherField != null) {
                return 1;
            }
        }
        if (thisMember instanceof Field && otherMember instanceof Method && ((Field)thisMember).getType() == ((Method)otherMember).getReturnType()) {
            return -1;
        }
        if (thisMember instanceof Method && otherMember instanceof Field && ((Method)thisMember).getReturnType() == ((Field)otherMember).getType()) {
            return 1;
        }
        final Class otherFieldClass = other.fieldClass;
        final Class thisFieldClass = this.fieldClass;
        if (thisFieldClass != otherFieldClass && thisFieldClass != null && otherFieldClass != null) {
            if (thisFieldClass.isAssignableFrom(otherFieldClass)) {
                return 1;
            }
            if (otherFieldClass.isAssignableFrom(thisFieldClass)) {
                return -1;
            }
        }
        if (thisFieldClass == Boolean.TYPE && otherFieldClass != Boolean.TYPE) {
            return 1;
        }
        if (thisFieldClass == Boolean.class && otherFieldClass == Boolean.class && thisMember instanceof Method && otherMember instanceof Method) {
            final String thisMethodName = thisMember.getName();
            final String otherMethodName = otherMember.getName();
            if (thisMethodName.startsWith("is") && otherMethodName.startsWith("get")) {
                return 1;
            }
            if (thisMethodName.startsWith("get") && otherMethodName.startsWith("is")) {
                return -1;
            }
        }
        if (thisMember instanceof Method && otherMember instanceof Method) {
            final String thisMethodName = thisMember.getName();
            final String otherMethodName = otherMember.getName();
            if (!thisMethodName.equals(otherMethodName)) {
                final String thisSetterName = BeanUtils.getterName(thisMethodName, null);
                final String otherSetterName = BeanUtils.getterName(otherMethodName, null);
                if (this.fieldName.equals(thisSetterName) && !other.fieldName.equals(otherSetterName)) {
                    return 1;
                }
                if (this.fieldName.equals(otherSetterName) && !other.fieldName.equals(thisSetterName)) {
                    return -1;
                }
            }
        }
        if (thisFieldClass.isPrimitive() && !otherFieldClass.isPrimitive()) {
            return -1;
        }
        if (!thisFieldClass.isPrimitive() && otherFieldClass.isPrimitive()) {
            return 1;
        }
        if (thisFieldClass.getName().startsWith("java.") && !otherFieldClass.getName().startsWith("java.")) {
            return -1;
        }
        if (!thisFieldClass.getName().startsWith("java.") && otherFieldClass.getName().startsWith("java.")) {
            return 1;
        }
        return nameCompare;
    }
    
    public void writeEnum(final JSONWriter jsonWriter, final Enum e) {
        this.writeFieldName(jsonWriter);
        jsonWriter.writeEnum(e);
    }
    
    public void writeBinary(final JSONWriter jsonWriter, byte[] value) {
        if (value != null) {
            this.writeFieldName(jsonWriter);
            if ("base64".equals(this.format) || (this.format == null && (jsonWriter.getFeatures(this.features) & JSONWriter.Feature.WriteByteArrayAsBase64.mask) != 0x0L)) {
                jsonWriter.writeBase64(value);
            }
            else if ("hex".equals(this.format)) {
                jsonWriter.writeHex(value);
            }
            else if ("gzip,base64".equals(this.format) || "gzip".equals(this.format)) {
                GZIPOutputStream gzipOut = null;
                try {
                    final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                    if (value.length < 512) {
                        gzipOut = new GZIPOutputStream(byteOut, value.length);
                    }
                    else {
                        gzipOut = new GZIPOutputStream(byteOut);
                    }
                    gzipOut.write(value);
                    gzipOut.finish();
                    value = byteOut.toByteArray();
                }
                catch (IOException ex) {
                    throw new JSONException("write gzipBytes error", ex);
                }
                finally {
                    IOUtils.close(gzipOut);
                }
                jsonWriter.writeBase64(value);
            }
            else {
                jsonWriter.writeBinary(value);
            }
            return;
        }
        if (!jsonWriter.isWriteNulls()) {
            return;
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeArrayNull();
    }
    
    public void writeInt16(final JSONWriter jsonWriter, final short[] value) {
        if (value == null && !jsonWriter.isWriteNulls()) {
            return;
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeInt16(value);
    }
    
    public void writeInt32(final JSONWriter jsonWriter, final int value) {
        this.writeFieldName(jsonWriter);
        jsonWriter.writeInt32(value);
    }
    
    public void writeInt64(final JSONWriter jsonWriter, final long value) {
        this.writeFieldName(jsonWriter);
        if ((this.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0x0L) {
            jsonWriter.writeString(Long.toString(value));
        }
        else {
            jsonWriter.writeInt64(value);
        }
    }
    
    public void writeString(final JSONWriter jsonWriter, String value) {
        this.writeFieldName(jsonWriter);
        if (value == null && (this.features & (JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask)) != 0x0L) {
            jsonWriter.writeString("");
            return;
        }
        if (this.trim && value != null) {
            value = value.trim();
        }
        if (this.symbol && jsonWriter.jsonb) {
            jsonWriter.writeSymbol(value);
        }
        else if (this.raw) {
            jsonWriter.writeRaw(value);
        }
        else {
            jsonWriter.writeString(value);
        }
    }
    
    public void writeString(final JSONWriter jsonWriter, final char[] value) {
        if (value == null && !jsonWriter.isWriteNulls()) {
            return;
        }
        this.writeFieldName(jsonWriter);
        if (value == null) {
            jsonWriter.writeStringNull();
            return;
        }
        jsonWriter.writeString(value, 0, value.length);
    }
    
    public void writeFloat(final JSONWriter jsonWriter, final float value) {
        this.writeFieldName(jsonWriter);
        if (this.decimalFormat != null) {
            jsonWriter.writeFloat(value, this.decimalFormat);
        }
        else {
            jsonWriter.writeFloat(value);
        }
    }
    
    public void writeDouble(final JSONWriter jsonWriter, final double value) {
        this.writeFieldName(jsonWriter);
        if (this.decimalFormat != null) {
            jsonWriter.writeDouble(value, this.decimalFormat);
        }
        else {
            jsonWriter.writeDouble(value);
        }
    }
    
    public void writeBool(final JSONWriter jsonWriter, final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    public void writeBool(final JSONWriter jsonWriter, final boolean[] value) {
        if (value == null && !jsonWriter.isWriteNulls()) {
            return;
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeBool(value);
    }
    
    public void writeFloat(final JSONWriter jsonWriter, final float[] value) {
        if (value == null && !jsonWriter.isWriteNulls()) {
            return;
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeFloat(value);
    }
    
    public void writeDouble(final JSONWriter jsonWriter, final double[] value) {
        if (value == null && !jsonWriter.isWriteNulls()) {
            return;
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeDouble(value);
    }
    
    public void writeDouble(final JSONWriter jsonWriter, final Double value) {
        if (value == null) {
            final long features = jsonWriter.getFeatures(this.features);
            if ((features & JSONWriter.Feature.WriteNulls.mask) != 0x0L && (features & JSONWriter.Feature.NotWriteDefaultValue.mask) == 0x0L) {
                this.writeFieldName(jsonWriter);
                jsonWriter.writeNumberNull();
            }
            return;
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeDouble(value);
    }
    
    public void writeDate(final JSONWriter jsonWriter, final boolean writeFieldName, final Date value) {
        if (value == null) {
            if (writeFieldName) {
                this.writeFieldName(jsonWriter);
            }
            jsonWriter.writeNull();
            return;
        }
        this.writeDate(jsonWriter, writeFieldName, value.getTime());
    }
    
    public void writeDate(final JSONWriter jsonWriter, final long millis) {
        this.writeDate(jsonWriter, true, millis);
    }
    
    public void writeDate(final JSONWriter jsonWriter, final boolean writeFieldName, final long millis) {
        if (jsonWriter.jsonb) {
            jsonWriter.writeMillis(millis);
            return;
        }
        final int SECONDS_PER_DAY = 86400;
        final JSONWriter.Context ctx = jsonWriter.context;
        if (this.isDateFormatMillis() || ctx.isDateFormatMillis()) {
            if (writeFieldName) {
                this.writeFieldName(jsonWriter);
            }
            jsonWriter.writeInt64(millis);
            return;
        }
        final ZoneId zoneId = ctx.getZoneId();
        final String dateFormat = ctx.getDateFormat();
        if (dateFormat == null) {
            final Instant instant = Instant.ofEpochMilli(millis);
            final long epochSecond = instant.getEpochSecond();
            final ZoneOffset offset = zoneId.getRules().getOffset(instant);
            final long localSecond = epochSecond + offset.getTotalSeconds();
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
            final int year = ChronoField.YEAR.checkValidIntValue(yearEst);
            final int MINUTES_PER_HOUR = 60;
            final int SECONDS_PER_MINUTE = 60;
            final int SECONDS_PER_HOUR = 3600;
            long secondOfDay = secsOfDay;
            ChronoField.SECOND_OF_DAY.checkValidValue(secondOfDay);
            final int hours = (int)(secondOfDay / 3600L);
            secondOfDay -= hours * 3600;
            final int minutes = (int)(secondOfDay / 60L);
            secondOfDay -= minutes * 60;
            final int hour = hours;
            final int minute = minutes;
            final int second = (int)secondOfDay;
            if (writeFieldName) {
                this.writeFieldName(jsonWriter);
            }
            jsonWriter.writeDateTime19(year, month, dayOfMonth, hour, minute, second);
        }
        else {
            final ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId);
            if (this.isDateFormatISO8601() || ctx.isDateFormatISO8601()) {
                final int year2 = zdt.getYear();
                final int month2 = zdt.getMonthValue();
                final int dayOfMonth2 = zdt.getDayOfMonth();
                final int hour2 = zdt.getHour();
                final int minute2 = zdt.getMinute();
                final int second2 = zdt.getSecond();
                final int milliSeconds = zdt.getNano() / 1000000;
                final int offsetSeconds = zdt.getOffset().getTotalSeconds();
                jsonWriter.writeDateTimeISO8601(year2, month2, dayOfMonth2, hour2, minute2, second2, milliSeconds, offsetSeconds, true);
                return;
            }
            final String str = ctx.getDateFormatter().format(zdt);
            if (writeFieldName) {
                this.writeFieldName(jsonWriter);
            }
            jsonWriter.writeString(str);
        }
    }
    
    public ObjectWriter getItemWriter(final JSONWriter writer, final Type itemType) {
        return writer.getObjectWriter(itemType, null);
    }
    
    public abstract void writeValue(final JSONWriter p0, final T p1);
    
    public abstract boolean write(final JSONWriter p0, final T p1);
    
    public ObjectWriter getObjectWriter(final JSONWriter jsonWriter, final Class valueClass) {
        if (valueClass == Float[].class) {
            if (this.decimalFormat != null) {
                return new ObjectWriterArrayFinal(Float.class, this.decimalFormat);
            }
            return ObjectWriterArrayFinal.FLOAT_ARRAY;
        }
        else if (valueClass == Double[].class) {
            if (this.decimalFormat != null) {
                return new ObjectWriterArrayFinal(Double.class, this.decimalFormat);
            }
            return ObjectWriterArrayFinal.DOUBLE_ARRAY;
        }
        else {
            if (valueClass != BigDecimal[].class) {
                return jsonWriter.getObjectWriter(valueClass);
            }
            if (this.decimalFormat != null) {
                return new ObjectWriterArrayFinal(BigDecimal.class, this.decimalFormat);
            }
            return ObjectWriterArrayFinal.DECIMAL_ARRAY;
        }
    }
    
    public void writeListValueJSONB(final JSONWriter jsonWriter, final List list) {
        throw new UnsupportedOperationException();
    }
    
    public void writeListValue(final JSONWriter jsonWriter, final List list) {
        throw new UnsupportedOperationException();
    }
    
    public void writeListJSONB(final JSONWriter jsonWriter, final List list) {
        throw new UnsupportedOperationException();
    }
    
    public void writeList(final JSONWriter jsonWriter, final List list) {
        throw new UnsupportedOperationException();
    }
    
    public void writeListStr(final JSONWriter jsonWriter, final boolean writeFieldName, final List<String> list) {
        throw new UnsupportedOperationException();
    }
    
    static ObjectWriter getObjectWriter(final Type fieldType, final Class fieldClass, final String format, final Locale locale, final Class valueClass) {
        if (Map.class.isAssignableFrom(valueClass)) {
            if (fieldClass.isAssignableFrom(valueClass)) {
                return ObjectWriterImplMap.of(fieldType, valueClass);
            }
            return ObjectWriterImplMap.of(valueClass);
        }
        else if (Calendar.class.isAssignableFrom(valueClass)) {
            if (format == null || format.isEmpty()) {
                return ObjectWriterImplCalendar.INSTANCE;
            }
            return new ObjectWriterImplCalendar(format, locale);
        }
        else if (ZonedDateTime.class.isAssignableFrom(valueClass)) {
            if (format == null || format.isEmpty()) {
                return ObjectWriterImplZonedDateTime.INSTANCE;
            }
            return new ObjectWriterImplZonedDateTime(format, locale);
        }
        else if (LocalDateTime.class.isAssignableFrom(valueClass)) {
            final ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter(LocalDateTime.class);
            if (objectWriter != null && objectWriter != ObjectWriterImplLocalDateTime.INSTANCE) {
                return objectWriter;
            }
            if (format == null || format.isEmpty()) {
                return ObjectWriterImplLocalDateTime.INSTANCE;
            }
            return new ObjectWriterImplLocalDateTime(format, locale);
        }
        else if (LocalDate.class.isAssignableFrom(valueClass)) {
            final ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter(LocalDate.class);
            if (objectWriter != null && objectWriter != ObjectWriterImplLocalDate.INSTANCE) {
                return objectWriter;
            }
            return ObjectWriterImplLocalDate.of(format, locale);
        }
        else if (LocalTime.class.isAssignableFrom(valueClass)) {
            final ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter(LocalTime.class);
            if (objectWriter != null && objectWriter != ObjectWriterImplLocalTime.INSTANCE) {
                return objectWriter;
            }
            if (format == null || format.isEmpty()) {
                return ObjectWriterImplLocalTime.INSTANCE;
            }
            return new ObjectWriterImplLocalTime(format, locale);
        }
        else if (Instant.class == valueClass) {
            if (format == null || format.isEmpty()) {
                return ObjectWriterImplInstant.INSTANCE;
            }
            return new ObjectWriterImplInstant(format, locale);
        }
        else if (BigDecimal.class == valueClass) {
            if (format == null || format.isEmpty()) {
                return ObjectWriterImplBigDecimal.INSTANCE;
            }
            return new ObjectWriterImplBigDecimal(new DecimalFormat(format), null);
        }
        else if (BigDecimal[].class == valueClass) {
            if (format == null || format.isEmpty()) {
                return new ObjectWriterArrayFinal(BigDecimal.class, null);
            }
            return new ObjectWriterArrayFinal(BigDecimal.class, new DecimalFormat(format));
        }
        else {
            if (Optional.class == valueClass) {
                return ObjectWriterImplOptional.of(format, locale);
            }
            final String name;
            final String className = name = valueClass.getName();
            switch (name) {
                case "java.sql.Time": {
                    return JdbcSupport.createTimeWriter(format);
                }
                case "java.sql.Date": {
                    return new ObjectWriterImplDate(format, locale);
                }
                case "java.sql.Timestamp": {
                    return JdbcSupport.createTimestampWriter(valueClass, format);
                }
                case "org.joda.time.LocalDate": {
                    return JodaSupport.createLocalDateWriter(valueClass, format);
                }
                case "org.joda.time.LocalDateTime": {
                    return JodaSupport.createLocalDateTimeWriter(valueClass, format);
                }
                default: {
                    return null;
                }
            }
        }
    }
    
    public Function getFunction() {
        return null;
    }
    
    static {
        initObjectWriterUpdater = AtomicReferenceFieldUpdater.newUpdater(FieldWriter.class, ObjectWriter.class, "initObjectWriter");
    }
}
