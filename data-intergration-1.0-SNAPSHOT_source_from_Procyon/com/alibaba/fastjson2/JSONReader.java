// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodType;
import java.util.function.BiFunction;
import java.util.Objects;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.filter.ExtraProcessor;
import java.util.function.Supplier;
import java.util.TimeZone;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.filter.ContextAutoTypeBeforeHandler;
import java.nio.ByteBuffer;
import java.io.Reader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import com.alibaba.fastjson2.util.JDKUtils;
import java.time.DateTimeException;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.function.Function;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import com.alibaba.fastjson2.util.Wrapper;
import com.alibaba.fastjson2.reader.ObjectReaderBean;
import com.alibaba.fastjson2.reader.ObjectReaderImplObject;
import com.alibaba.fastjson2.reader.ValueConsumer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson2.util.DateUtils;
import java.util.Calendar;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import com.alibaba.fastjson2.util.IOUtils;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Base64;
import java.util.ArrayList;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.reader.FieldReader;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashMap;
import com.alibaba.fastjson2.util.ReferenceKey;
import java.util.Map;
import java.time.ZoneId;
import java.util.Locale;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.io.Closeable;

public abstract class JSONReader implements Closeable
{
    static final int MAX_EXP = 1023;
    static final byte JSON_TYPE_INT = 1;
    static final byte JSON_TYPE_DEC = 2;
    static final byte JSON_TYPE_STRING = 3;
    static final byte JSON_TYPE_BOOL = 4;
    static final byte JSON_TYPE_NULL = 5;
    static final byte JSON_TYPE_OBJECT = 6;
    static final byte JSON_TYPE_ARRAY = 7;
    static final byte JSON_TYPE_BIG_DEC = 8;
    static final byte JSON_TYPE_INT8 = 9;
    static final byte JSON_TYPE_INT16 = 10;
    static final byte JSON_TYPE_INT64 = 11;
    static final byte JSON_TYPE_FLOAT = 12;
    static final byte JSON_TYPE_DOUBLE = 13;
    static final char EOI = '\u001a';
    static final long SPACE = 4294981376L;
    protected final Context context;
    public final boolean jsonb;
    public final boolean utf8;
    List<ResolveTask> resolveTasks;
    protected int offset;
    protected char ch;
    protected boolean comma;
    protected boolean nameEscape;
    protected boolean valueEscape;
    protected boolean wasNull;
    protected boolean boolValue;
    protected boolean negative;
    protected byte valueType;
    protected short exponent;
    protected byte scale;
    protected int mag0;
    protected int mag1;
    protected int mag2;
    protected int mag3;
    protected int level;
    protected String stringValue;
    protected Object complex;
    protected boolean typeRedirect;
    protected char[] doubleChars;
    
    public final char current() {
        return this.ch;
    }
    
    public boolean isEnd() {
        return this.ch == '\u001a';
    }
    
    public byte getType() {
        return -128;
    }
    
    public boolean isInt() {
        return this.ch == '-' || this.ch == '+' || (this.ch >= '0' && this.ch <= '9');
    }
    
    public abstract boolean isNull();
    
    public final boolean hasComma() {
        return this.comma;
    }
    
    public abstract Date readNullOrNewDate();
    
    public abstract boolean nextIfNull();
    
    public JSONReader(final Context context, final boolean jsonb, final boolean utf8) {
        this.context = context;
        this.jsonb = jsonb;
        this.utf8 = utf8;
    }
    
    public final Context getContext() {
        return this.context;
    }
    
    public final void errorOnNoneSerializable(final Class objectClass) {
        if ((this.context.features & Feature.ErrorOnNoneSerializable.mask) != 0x0L && !Serializable.class.isAssignableFrom(objectClass)) {
            throw new JSONException("not support none-Serializable, class " + objectClass.getName());
        }
    }
    
    public final boolean isEnabled(final Feature feature) {
        return (this.context.features & feature.mask) != 0x0L;
    }
    
    public final Locale getLocale() {
        return this.context.getLocale();
    }
    
    public final ZoneId getZoneId() {
        return this.context.getZoneId();
    }
    
    public final long features(final long features) {
        return this.context.features | features;
    }
    
    public int getRawInt() {
        return 0;
    }
    
    public long getRawLong() {
        return 0L;
    }
    
    public boolean nextIfName4Match2() {
        return false;
    }
    
    public boolean nextIfValue4Match2() {
        return false;
    }
    
    public boolean nextIfName4Match3() {
        return false;
    }
    
    public boolean nextIfValue4Match3() {
        return false;
    }
    
    public boolean nextIfName4Match4(final byte c4) {
        return false;
    }
    
    public boolean nextIfValue4Match4(final byte c4) {
        return false;
    }
    
    public boolean nextIfName4Match5(final int name1) {
        return false;
    }
    
    public boolean nextIfValue4Match5(final byte c4, final byte c5) {
        return false;
    }
    
    public boolean nextIfName4Match6(final int name1) {
        return false;
    }
    
    public boolean nextIfValue4Match6(final int name1) {
        return false;
    }
    
    public boolean nextIfName4Match7(final int name1) {
        return false;
    }
    
    public boolean nextIfValue4Match7(final int name1) {
        return false;
    }
    
    public boolean nextIfName4Match8(final int name1, final byte c8) {
        return false;
    }
    
    public boolean nextIfValue4Match8(final int name1, final byte c8) {
        return false;
    }
    
    public boolean nextIfName4Match9(final long name1) {
        return false;
    }
    
    public boolean nextIfValue4Match9(final int name1, final byte c8, final byte c9) {
        return false;
    }
    
    public boolean nextIfName4Match10(final long name1) {
        return false;
    }
    
    public boolean nextIfValue4Match10(final long name1) {
        return false;
    }
    
    public boolean nextIfName4Match11(final long name1) {
        return false;
    }
    
    public boolean nextIfValue4Match11(final long name1) {
        return false;
    }
    
    public boolean nextIfName4Match12(final long name1, final byte c12) {
        return false;
    }
    
    public boolean nextIfName4Match13(final long name1, final int name2) {
        return false;
    }
    
    public boolean nextIfName4Match14(final long name1, final int name2) {
        return false;
    }
    
    public boolean nextIfName4Match15(final long name1, final int name2) {
        return false;
    }
    
    public boolean nextIfName4Match16(final long name1, final int name2, final byte c16) {
        return false;
    }
    
    public boolean nextIfName4Match17(final long name1, final long name2) {
        return false;
    }
    
    public boolean nextIfName4Match18(final long name1, final long name2) {
        return false;
    }
    
    public boolean nextIfName4Match19(final long name1, final long name2) {
        return false;
    }
    
    public boolean nextIfName4Match20(final long name1, final long name2, final byte c20) {
        return false;
    }
    
    public boolean nextIfName4Match21(final long name1, final long name2, final int name3) {
        return false;
    }
    
    public boolean nextIfName4Match22(final long name1, final long name2, final int name3) {
        return false;
    }
    
    public boolean nextIfName4Match23(final long name1, final long name2, final int name3) {
        return false;
    }
    
    public boolean nextIfName4Match24(final long name1, final long name2, final int name3, final byte c24) {
        return false;
    }
    
    public boolean nextIfName4Match25(final long name1, final long name2, final long name3) {
        return false;
    }
    
    public boolean nextIfName4Match26(final long name1, final long name2, final long name3) {
        return false;
    }
    
    public boolean nextIfName4Match27(final long name1, final long name2, final long name3) {
        return false;
    }
    
    public boolean nextIfName4Match28(final long name1, final long name2, final long name3, final byte c28) {
        return false;
    }
    
    public boolean nextIfName4Match29(final long name1, final long name2, final long name3, final int name4) {
        return false;
    }
    
    public boolean nextIfName4Match30(final long name1, final long name2, final long name3, final int name4) {
        return false;
    }
    
    public boolean nextIfName4Match31(final long name1, final long name2, final long name3, final int name4) {
        return false;
    }
    
    public boolean nextIfName4Match32(final long name1, final long name2, final long name3, final int name4, final byte c32) {
        return false;
    }
    
    public boolean nextIfName4Match33(final long name1, final long name2, final long name3, final long name4) {
        return false;
    }
    
    public boolean nextIfName4Match34(final long name1, final long name2, final long name3, final long name4) {
        return false;
    }
    
    public boolean nextIfName4Match35(final long name1, final long name2, final long name3, final long name4) {
        return false;
    }
    
    public boolean nextIfName4Match36(final long name1, final long name2, final long name3, final long name4, final byte c35) {
        return false;
    }
    
    public boolean nextIfName4Match37(final long name1, final long name2, final long name3, final long name4, final int name5) {
        return false;
    }
    
    public boolean nextIfName4Match38(final long name1, final long name2, final long name3, final long name4, final int name5) {
        return false;
    }
    
    public boolean nextIfName4Match39(final long name1, final long name2, final long name3, final long name4, final int name5) {
        return false;
    }
    
    public boolean nextIfName4Match40(final long name1, final long name2, final long name3, final long name4, final int name5, final byte c40) {
        return false;
    }
    
    public boolean nextIfName4Match41(final long name1, final long name2, final long name3, final long name4, final long name5) {
        return false;
    }
    
    public boolean nextIfName4Match42(final long name1, final long name2, final long name3, final long name4, final long name5) {
        return false;
    }
    
    public boolean nextIfName4Match43(final long name1, final long name2, final long name3, final long name4, final long name5) {
        return false;
    }
    
    public boolean nextIfName8Match0() {
        return false;
    }
    
    public boolean nextIfName8Match1() {
        return false;
    }
    
    public boolean nextIfName8Match2() {
        return false;
    }
    
    public final void handleResolveTasks(final Object root) {
        if (this.resolveTasks == null) {
            return;
        }
        Object previous = null;
        for (final ResolveTask resolveTask : this.resolveTasks) {
            final JSONPath path = resolveTask.reference;
            final FieldReader fieldReader = resolveTask.fieldReader;
            Object fieldValue;
            if (path.isPrevious()) {
                fieldValue = previous;
            }
            else {
                if (!path.isRef()) {
                    throw new JSONException("reference path invalid : " + path);
                }
                path.setReaderContext(this.context);
                if ((this.context.features & Feature.FieldBased.mask) != 0x0L) {
                    final JSONWriter.Context writeContext2;
                    final JSONWriter.Context writeContext = writeContext2 = JSONFactory.createWriteContext();
                    writeContext2.features |= JSONWriter.Feature.FieldBased.mask;
                    path.setWriterContext(writeContext);
                }
                fieldValue = (previous = path.eval(root));
            }
            final Object resolvedName = resolveTask.name;
            final Object resolvedObject = resolveTask.object;
            if (resolvedName != null) {
                if (resolvedObject instanceof Map) {
                    final Map map = (Map)resolvedObject;
                    if (!(resolvedName instanceof ReferenceKey)) {
                        map.put(resolvedName, fieldValue);
                        continue;
                    }
                    if (!(map instanceof LinkedHashMap)) {
                        map.put(fieldValue, map.remove(resolvedName));
                        continue;
                    }
                    final int size = map.size();
                    if (size == 0) {
                        continue;
                    }
                    final Object[] keys = new Object[size];
                    final Object[] values = new Object[size];
                    int index = 0;
                    for (final Object o : map.entrySet()) {
                        final Map.Entry entry = (Map.Entry)o;
                        final Object entryKey = entry.getKey();
                        if (resolvedName == entryKey) {
                            keys[index] = fieldValue;
                        }
                        else {
                            keys[index] = entryKey;
                        }
                        values[index++] = entry.getValue();
                    }
                    map.clear();
                    for (int j = 0; j < keys.length; ++j) {
                        map.put(keys[j], values[j]);
                    }
                    continue;
                }
                else if (resolvedName instanceof Integer) {
                    if (resolvedObject instanceof List) {
                        final int index2 = (int)resolvedName;
                        final List list = (List)resolvedObject;
                        if (index2 == list.size()) {
                            list.add(fieldValue);
                            continue;
                        }
                        list.set(index2, fieldValue);
                        continue;
                    }
                    else {
                        if (resolvedObject instanceof Object[]) {
                            final int index2 = (int)resolvedName;
                            final Object[] array = (Object[])resolvedObject;
                            array[index2] = fieldValue;
                            continue;
                        }
                        if (resolvedObject instanceof Collection) {
                            final Collection collection = (Collection)resolvedObject;
                            collection.add(fieldValue);
                            continue;
                        }
                    }
                }
            }
            fieldReader.accept(resolvedObject, fieldValue);
        }
    }
    
    public final ObjectReader getObjectReader(final Type type) {
        final boolean fieldBased = (this.context.features & Feature.FieldBased.mask) != 0x0L;
        return this.context.provider.getObjectReader(type, fieldBased);
    }
    
    public final boolean isSupportSmartMatch() {
        return (this.context.features & Feature.SupportSmartMatch.mask) != 0x0L;
    }
    
    public final boolean isInitStringFieldAsEmpty() {
        return (this.context.features & Feature.InitStringFieldAsEmpty.mask) != 0x0L;
    }
    
    public final boolean isSupportSmartMatch(final long features) {
        return ((this.context.features | features) & Feature.SupportSmartMatch.mask) != 0x0L;
    }
    
    public final boolean isSupportBeanArray() {
        return (this.context.features & Feature.SupportArrayToBean.mask) != 0x0L;
    }
    
    public final boolean isSupportBeanArray(final long features) {
        return ((this.context.features | features) & Feature.SupportArrayToBean.mask) != 0x0L;
    }
    
    public final boolean isSupportAutoType(final long features) {
        return ((this.context.features | features) & Feature.SupportAutoType.mask) != 0x0L;
    }
    
    public final boolean isSupportAutoTypeOrHandler(final long features) {
        return ((this.context.features | features) & Feature.SupportAutoType.mask) != 0x0L || this.context.autoTypeBeforeHandler != null;
    }
    
    public final boolean isJSONB() {
        return this.jsonb;
    }
    
    public final boolean isIgnoreNoneSerializable() {
        return (this.context.features & Feature.IgnoreNoneSerializable.mask) != 0x0L;
    }
    
    public ObjectReader checkAutoType(final Class expectClass, final long expectClassHash, final long features) {
        return null;
    }
    
    final char char1(final int c) {
        switch (c) {
            case 48: {
                return '\0';
            }
            case 49: {
                return '\u0001';
            }
            case 50: {
                return '\u0002';
            }
            case 51: {
                return '\u0003';
            }
            case 52: {
                return '\u0004';
            }
            case 53: {
                return '\u0005';
            }
            case 54: {
                return '\u0006';
            }
            case 55: {
                return '\u0007';
            }
            case 98: {
                return '\b';
            }
            case 116: {
                return '\t';
            }
            case 110: {
                return '\n';
            }
            case 118: {
                return '\u000b';
            }
            case 70:
            case 102: {
                return '\f';
            }
            case 114: {
                return '\r';
            }
            case 34:
            case 35:
            case 38:
            case 39:
            case 40:
            case 41:
            case 46:
            case 47:
            case 64:
            case 91:
            case 92:
            case 93:
            case 95: {
                return (char)c;
            }
            default: {
                throw new JSONException(this.info("unclosed.str '\\" + (char)c));
            }
        }
    }
    
    static char char2(final int c1, final int c2) {
        return (char)(JSONFactory.DIGITS2[c1] * 16 + JSONFactory.DIGITS2[c2]);
    }
    
    static char char4(final int c1, final int c2, final int c3, final int c4) {
        return (char)(JSONFactory.DIGITS2[c1] * 4096 + JSONFactory.DIGITS2[c2] * 256 + JSONFactory.DIGITS2[c3] * 16 + JSONFactory.DIGITS2[c4]);
    }
    
    public abstract boolean nextIfObjectStart();
    
    public abstract boolean nextIfNullOrEmptyString();
    
    public abstract boolean nextIfObjectEnd();
    
    public int startArray() {
        if (!this.nextIfArrayStart()) {
            throw new JSONException(this.info("illegal input, expect '[', but " + this.ch));
        }
        return Integer.MAX_VALUE;
    }
    
    public abstract boolean isReference();
    
    public abstract String readReference();
    
    public final void addResolveTask(final FieldReader fieldReader, final Object object, final JSONPath path) {
        if (this.resolveTasks == null) {
            this.resolveTasks = new ArrayList<ResolveTask>();
        }
        this.resolveTasks.add(new ResolveTask(fieldReader, object, fieldReader.fieldName, path));
    }
    
    public final void addResolveTask(final Map object, final Object key, final JSONPath reference) {
        if (this.resolveTasks == null) {
            this.resolveTasks = new ArrayList<ResolveTask>();
        }
        if (object instanceof LinkedHashMap) {
            object.put(key, null);
        }
        this.resolveTasks.add(new ResolveTask(null, object, key, reference));
    }
    
    public final void addResolveTask(final Collection object, final int i, final JSONPath reference) {
        if (this.resolveTasks == null) {
            this.resolveTasks = new ArrayList<ResolveTask>();
        }
        this.resolveTasks.add(new ResolveTask(null, object, i, reference));
    }
    
    public final void addResolveTask(final Object[] object, final int i, final JSONPath reference) {
        if (this.resolveTasks == null) {
            this.resolveTasks = new ArrayList<ResolveTask>();
        }
        this.resolveTasks.add(new ResolveTask(null, object, i, reference));
    }
    
    public boolean isArray() {
        return this.ch == '[';
    }
    
    public boolean isObject() {
        return this.ch == '{';
    }
    
    public boolean isNumber() {
        switch (this.ch) {
            case '+':
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public boolean isString() {
        return this.ch == '\"' || this.ch == '\'';
    }
    
    public void endArray() {
        this.next();
    }
    
    public abstract boolean nextIfMatch(final char p0);
    
    public abstract boolean nextIfComma();
    
    public abstract boolean nextIfArrayStart();
    
    public abstract boolean nextIfArrayEnd();
    
    public abstract boolean nextIfSet();
    
    public abstract boolean nextIfInfinity();
    
    public abstract String readPattern();
    
    public final int getOffset() {
        return this.offset;
    }
    
    public abstract void next();
    
    public abstract long readValueHashCode();
    
    public long readTypeHashCode() {
        return this.readValueHashCode();
    }
    
    public abstract long readFieldNameHashCode();
    
    public abstract long getNameHashCodeLCase();
    
    public abstract String readFieldName();
    
    public abstract String getFieldName();
    
    public final void setTypeRedirect(final boolean typeRedirect) {
        this.typeRedirect = typeRedirect;
    }
    
    public final boolean isTypeRedirect() {
        return this.typeRedirect;
    }
    
    public abstract long readFieldNameHashCodeUnquote();
    
    public final String readFieldNameUnquote() {
        this.readFieldNameHashCodeUnquote();
        return this.getFieldName();
    }
    
    public abstract boolean skipName();
    
    public abstract void skipValue();
    
    public boolean isBinary() {
        return false;
    }
    
    public abstract byte[] readHex();
    
    public byte[] readBinary() {
        if (this.ch == 'x') {
            return this.readHex();
        }
        if (this.isString()) {
            final String str = this.readString();
            if (str.isEmpty()) {
                return null;
            }
            if ((this.context.features & Feature.Base64StringAsByteArray.mask) != 0x0L) {
                return Base64.getDecoder().decode(str);
            }
            throw new JSONException(this.info("not support input " + str));
        }
        else {
            if (this.nextIfArrayStart()) {
                int index = 0;
                byte[] bytes = new byte[64];
                while (this.ch != ']') {
                    if (index == bytes.length) {
                        final int oldCapacity = bytes.length;
                        final int newCapacity = oldCapacity + (oldCapacity >> 1);
                        bytes = Arrays.copyOf(bytes, newCapacity);
                    }
                    bytes[index++] = (byte)this.readInt32Value();
                }
                this.next();
                this.nextIfComma();
                return Arrays.copyOf(bytes, index);
            }
            throw new JSONException(this.info("not support read binary"));
        }
    }
    
    public abstract int readInt32Value();
    
    public int[] readInt32ValueArray() {
        if (this.nextIfNull()) {
            return null;
        }
        if (this.nextIfArrayStart()) {
            int[] values = new int[8];
            int size = 0;
            while (!this.nextIfArrayEnd()) {
                if (this.isEnd()) {
                    throw new JSONException(this.info("input end"));
                }
                if (size == values.length) {
                    values = Arrays.copyOf(values, values.length << 1);
                }
                values[size++] = this.readInt32Value();
            }
            this.nextIfComma();
            int[] array;
            if (size == values.length) {
                array = values;
            }
            else {
                array = Arrays.copyOf(values, size);
            }
            return array;
        }
        if (!this.isString()) {
            throw new JSONException(this.info("TODO"));
        }
        final String str = this.readString();
        if (str.isEmpty()) {
            return null;
        }
        throw new JSONException(this.info("not support input " + str));
    }
    
    public boolean nextIfMatch(final byte type) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public boolean nextIfMatchTypedAny() {
        throw new JSONException("UnsupportedOperation");
    }
    
    public abstract boolean nextIfMatchIdent(final char p0, final char p1, final char p2);
    
    public abstract boolean nextIfMatchIdent(final char p0, final char p1, final char p2, final char p3);
    
    public abstract boolean nextIfMatchIdent(final char p0, final char p1, final char p2, final char p3, final char p4);
    
    public abstract boolean nextIfMatchIdent(final char p0, final char p1, final char p2, final char p3, final char p4, final char p5);
    
    public final Byte readInt8() {
        final Integer i = this.readInt32();
        if (i == null) {
            return null;
        }
        return i.byteValue();
    }
    
    public byte readInt8Value() {
        final int i = this.readInt32Value();
        return (byte)i;
    }
    
    public final Short readInt16() {
        final Integer i = this.readInt32();
        if (i == null) {
            return null;
        }
        return i.shortValue();
    }
    
    public short readInt16Value() {
        final int i = this.readInt32Value();
        return (short)i;
    }
    
    public abstract Integer readInt32();
    
    public final int getInt32Value() {
        switch (this.valueType) {
            case 1: {
                if (this.mag1 == 0 && this.mag2 == 0 && this.mag3 != Integer.MIN_VALUE) {
                    return this.negative ? (-this.mag3) : this.mag3;
                }
                final Number number = this.getNumber();
                if (!(number instanceof Long)) {
                    return number.intValue();
                }
                final long longValue = number.longValue();
                if (longValue < -2147483648L || longValue > 2147483647L) {
                    throw new JSONException(this.info("integer overflow " + longValue));
                }
                return (int)longValue;
            }
            case 2: {
                return this.getNumber().intValue();
            }
            case 4: {
                return this.boolValue ? 1 : 0;
            }
            case 5: {
                if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) != 0x0L) {
                    throw new JSONException(this.info("int value not support input null"));
                }
                return 0;
            }
            case 3: {
                return this.toInt32(this.stringValue);
            }
            case 6: {
                final Number num = this.toNumber((Map)this.complex);
                if (num != null) {
                    return num.intValue();
                }
                return 0;
            }
            case 7: {
                return this.toInt((List)this.complex);
            }
            default: {
                throw new JSONException("TODO : " + this.valueType);
            }
        }
    }
    
    public final long getInt64Value() {
        switch (this.valueType) {
            case 1: {
                if (this.mag1 == 0 && this.mag2 == 0 && this.mag3 != Integer.MIN_VALUE) {
                    return this.negative ? (-this.mag3) : ((long)this.mag3);
                }
                return this.getNumber().longValue();
            }
            case 2: {
                return this.getNumber().longValue();
            }
            case 4: {
                return this.boolValue ? 1 : 0;
            }
            case 5: {
                if ((this.context.features & Feature.ErrorOnNullForPrimitives.mask) != 0x0L) {
                    throw new JSONException(this.info("long value not support input null"));
                }
                return 0L;
            }
            case 3: {
                return this.toInt64(this.stringValue);
            }
            case 6: {
                return this.toLong((Map)this.complex);
            }
            case 7: {
                return this.toInt((List)this.complex);
            }
            default: {
                throw new JSONException("TODO");
            }
        }
    }
    
    protected final Long getInt64() {
        switch (this.valueType) {
            case 1: {
                if (this.mag1 == 0 && this.mag2 == 0 && this.mag3 != Integer.MIN_VALUE) {
                    return (long)(this.negative ? (-this.mag3) : this.mag3);
                }
                int[] mag;
                if (this.mag0 == 0) {
                    if (this.mag1 == 0) {
                        if (this.mag2 == Integer.MIN_VALUE && this.mag3 == 0 && !this.negative) {
                            return Long.MIN_VALUE;
                        }
                        final long v3 = (long)this.mag3 & 0xFFFFFFFFL;
                        final long v4 = (long)this.mag2 & 0xFFFFFFFFL;
                        if (v4 <= 2147483647L) {
                            final long v5 = (v4 << 32) + v3;
                            return this.negative ? (-v5) : v5;
                        }
                        mag = new int[] { this.mag2, this.mag3 };
                    }
                    else {
                        mag = new int[] { this.mag1, this.mag2, this.mag3 };
                    }
                }
                else {
                    mag = new int[] { this.mag0, this.mag1, this.mag2, this.mag3 };
                }
                final int signum = this.negative ? -1 : 1;
                final BigInteger bigInt = BigIntegerCreator.BIG_INTEGER_CREATOR.apply(signum, mag);
                return bigInt.longValue();
            }
            case 2: {
                return this.getNumber().longValue();
            }
            case 4: {
                return (long)(this.boolValue ? 1 : 0);
            }
            case 5: {
                return null;
            }
            case 3: {
                return this.toInt64(this.stringValue);
            }
            case 6: {
                final Number num = this.toNumber((Map)this.complex);
                if (num != null) {
                    return num.longValue();
                }
                return null;
            }
            default: {
                throw new JSONException("TODO");
            }
        }
    }
    
    public long[] readInt64ValueArray() {
        if (this.nextIfNull()) {
            return null;
        }
        if (this.nextIfArrayStart()) {
            long[] values = new long[8];
            int size = 0;
            while (!this.nextIfArrayEnd()) {
                if (this.isEnd()) {
                    throw new JSONException(this.info("input end"));
                }
                if (size == values.length) {
                    values = Arrays.copyOf(values, values.length << 1);
                }
                values[size++] = this.readInt64Value();
            }
            this.nextIfComma();
            long[] array;
            if (size == values.length) {
                array = values;
            }
            else {
                array = Arrays.copyOf(values, size);
            }
            return array;
        }
        if (!this.isString()) {
            throw new JSONException(this.info("TODO"));
        }
        final String str = this.readString();
        if (str.isEmpty()) {
            return null;
        }
        throw new JSONException(this.info("not support input " + str));
    }
    
    public abstract long readInt64Value();
    
    public abstract Long readInt64();
    
    public abstract float readFloatValue();
    
    public Float readFloat() {
        if (this.nextIfNull()) {
            return null;
        }
        this.wasNull = false;
        final float value = this.readFloatValue();
        if (this.wasNull) {
            return null;
        }
        return value;
    }
    
    public abstract double readDoubleValue();
    
    public final Double readDouble() {
        if (this.nextIfNull()) {
            return null;
        }
        this.wasNull = false;
        final double value = this.readDoubleValue();
        if (this.wasNull) {
            return null;
        }
        return value;
    }
    
    public Number readNumber() {
        this.readNumber0();
        return this.getNumber();
    }
    
    public BigInteger readBigInteger() {
        this.readNumber0();
        return this.getBigInteger();
    }
    
    public abstract BigDecimal readBigDecimal();
    
    public abstract UUID readUUID();
    
    public final boolean isLocalDate() {
        if (!this.isString()) {
            return false;
        }
        final int len = this.getStringLength();
        LocalDate localDate = null;
        switch (len) {
            case 8: {
                localDate = this.readLocalDate8();
                break;
            }
            case 9: {
                localDate = this.readLocalDate9();
                break;
            }
            case 10: {
                localDate = this.readLocalDate10();
                break;
            }
            case 11: {
                localDate = this.readLocalDate11();
                break;
            }
            default: {
                return false;
            }
        }
        return localDate != null;
    }
    
    public LocalDate readLocalDate() {
        if (this.nextIfNull()) {
            return null;
        }
        if (this.isInt()) {
            long millis = this.readInt64Value();
            if (this.context.formatUnixTime) {
                millis *= 1000L;
            }
            final Instant instant = Instant.ofEpochMilli(millis);
            final ZonedDateTime zdt = instant.atZone(this.context.getZoneId());
            return zdt.toLocalDate();
        }
        if (this.context.dateFormat == null || this.context.formatyyyyMMddhhmmss19 || this.context.formatyyyyMMddhhmmssT19 || this.context.formatyyyyMMdd8 || this.context.formatISO8601) {
            final int len = this.getStringLength();
            LocalDateTime ldt = null;
            switch (len) {
                case 8: {
                    final LocalDate localDate = this.readLocalDate8();
                    ldt = ((localDate == null) ? null : LocalDateTime.of(localDate, LocalTime.MIN));
                    break;
                }
                case 9: {
                    final LocalDate localDate = this.readLocalDate9();
                    ldt = ((localDate == null) ? null : LocalDateTime.of(localDate, LocalTime.MIN));
                    break;
                }
                case 10: {
                    final LocalDate localDate = this.readLocalDate10();
                    ldt = ((localDate == null) ? null : LocalDateTime.of(localDate, LocalTime.MIN));
                    break;
                }
                case 11: {
                    final LocalDate localDate = this.readLocalDate11();
                    ldt = ((localDate == null) ? null : LocalDateTime.of(localDate, LocalTime.MIN));
                    break;
                }
                case 19: {
                    ldt = this.readLocalDateTime19();
                    break;
                }
                case 20: {
                    ldt = this.readLocalDateTime20();
                    break;
                }
                default: {
                    if (len > 20) {
                        ldt = this.readLocalDateTimeX(len);
                        break;
                    }
                    break;
                }
            }
            if (ldt != null) {
                return ldt.toLocalDate();
            }
        }
        final String str = this.readString();
        if (str.isEmpty() || "null".equals(str)) {
            return null;
        }
        final DateTimeFormatter formatter = this.context.getDateFormatter();
        if (formatter != null) {
            if (this.context.formatHasHour) {
                return LocalDateTime.parse(str, formatter).toLocalDate();
            }
            return LocalDate.parse(str, formatter);
        }
        else {
            if (IOUtils.isNumber(str)) {
                final long millis2 = Long.parseLong(str);
                final Instant instant2 = Instant.ofEpochMilli(millis2);
                final ZonedDateTime zdt2 = instant2.atZone(this.context.getZoneId());
                return zdt2.toLocalDate();
            }
            throw new JSONException("not support input : " + str);
        }
    }
    
    public final boolean isLocalDateTime() {
        if (!this.isString()) {
            return false;
        }
        final int len = this.getStringLength();
        switch (len) {
            case 16: {
                return this.readLocalDateTime16() != null;
            }
            case 17: {
                return this.readLocalDateTime17() != null;
            }
            case 18: {
                return this.readLocalDateTime18() != null;
            }
            case 19: {
                return this.readLocalDateTime19() != null;
            }
            case 20: {
                return this.readLocalDateTime20() != null;
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
                return this.readLocalDateTimeX(len) != null;
            }
            default: {
                return false;
            }
        }
    }
    
    public LocalDateTime readLocalDateTime() {
        if (this.isInt()) {
            final long millis = this.readInt64Value();
            final Instant instant = Instant.ofEpochMilli(millis);
            final ZonedDateTime zdt = instant.atZone(this.context.getZoneId());
            return zdt.toLocalDateTime();
        }
        if (this.context.dateFormat == null || this.context.formatyyyyMMddhhmmss19 || this.context.formatyyyyMMddhhmmssT19 || this.context.formatyyyyMMdd8 || this.context.formatISO8601) {
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
                    final LocalDateTime ldt = this.readLocalDateTime17();
                    if (ldt != null) {
                        return ldt;
                    }
                    break;
                }
                case 18: {
                    final LocalDateTime ldt = this.readLocalDateTime18();
                    if (ldt != null) {
                        return ldt;
                    }
                    break;
                }
                case 19: {
                    final LocalDateTime ldt = this.readLocalDateTime19();
                    if (ldt != null) {
                        return ldt;
                    }
                    break;
                }
                case 20: {
                    final LocalDateTime ldt = this.readLocalDateTime20();
                    if (ldt != null) {
                        return ldt;
                    }
                    final ZonedDateTime zdt = this.readZonedDateTimeX(len);
                    if (zdt != null) {
                        return zdt.toLocalDateTime();
                    }
                    break;
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
                    LocalDateTime ldt = this.readLocalDateTimeX(len);
                    if (ldt != null) {
                        return ldt;
                    }
                    final ZonedDateTime zdt = this.readZonedDateTimeX(len);
                    if (zdt != null) {
                        final ZoneId contextZoneId = this.context.getZoneId();
                        if (!zdt.getZone().equals(contextZoneId)) {
                            ldt = zdt.toInstant().atZone(contextZoneId).toLocalDateTime();
                        }
                        else {
                            ldt = zdt.toLocalDateTime();
                        }
                        return ldt;
                    }
                    break;
                }
            }
        }
        final String str = this.readString();
        if (str.isEmpty() || "null".equals(str)) {
            this.wasNull = true;
            return null;
        }
        final DateTimeFormatter formatter = this.context.getDateFormatter();
        if (formatter != null) {
            if (!this.context.formatHasHour) {
                return LocalDateTime.of(LocalDate.parse(str, formatter), LocalTime.MIN);
            }
            return LocalDateTime.parse(str, formatter);
        }
        else {
            if (IOUtils.isNumber(str)) {
                long millis2 = Long.parseLong(str);
                if (this.context.formatUnixTime) {
                    millis2 *= 1000L;
                }
                final Instant instant2 = Instant.ofEpochMilli(millis2);
                return LocalDateTime.ofInstant(instant2, this.context.getZoneId());
            }
            if (str.startsWith("/Date(") && str.endsWith(")/")) {
                String dotnetDateStr = str.substring(6, str.length() - 2);
                int i = dotnetDateStr.indexOf(43);
                if (i == -1) {
                    i = dotnetDateStr.indexOf(45);
                }
                if (i != -1) {
                    dotnetDateStr = dotnetDateStr.substring(0, i);
                }
                final long millis3 = Long.parseLong(dotnetDateStr);
                final Instant instant3 = Instant.ofEpochMilli(millis3);
                return LocalDateTime.ofInstant(instant3, this.context.getZoneId());
            }
            if (str.equals("0000-00-00 00:00:00")) {
                this.wasNull = true;
                return null;
            }
            throw new JSONException(this.info("read LocalDateTime error " + str));
        }
    }
    
    public abstract OffsetDateTime readOffsetDateTime();
    
    public ZonedDateTime readZonedDateTime() {
        if (this.isInt()) {
            long millis = this.readInt64Value();
            if (this.context.formatUnixTime) {
                millis *= 1000L;
            }
            final Instant instant = Instant.ofEpochMilli(millis);
            return instant.atZone(this.context.getZoneId());
        }
        if (!this.isString()) {
            throw new JSONException("TODO : " + this.ch);
        }
        if (this.context.dateFormat == null || this.context.formatyyyyMMddhhmmss19 || this.context.formatyyyyMMddhhmmssT19 || this.context.formatyyyyMMdd8 || this.context.formatISO8601) {
            final int len = this.getStringLength();
            LocalDateTime ldt = null;
            switch (len) {
                case 8: {
                    final LocalDate localDate = this.readLocalDate8();
                    ldt = ((localDate == null) ? null : LocalDateTime.of(localDate, LocalTime.MIN));
                    break;
                }
                case 9: {
                    final LocalDate localDate = this.readLocalDate9();
                    ldt = ((localDate == null) ? null : LocalDateTime.of(localDate, LocalTime.MIN));
                    break;
                }
                case 10: {
                    final LocalDate localDate = this.readLocalDate10();
                    ldt = ((localDate == null) ? null : LocalDateTime.of(localDate, LocalTime.MIN));
                    break;
                }
                case 11: {
                    final LocalDate localDate = this.readLocalDate11();
                    ldt = LocalDateTime.of(localDate, LocalTime.MIN);
                    break;
                }
                case 16: {
                    ldt = this.readLocalDateTime16();
                    break;
                }
                case 17: {
                    ldt = this.readLocalDateTime17();
                    break;
                }
                case 18: {
                    ldt = this.readLocalDateTime18();
                    break;
                }
                case 19: {
                    ldt = this.readLocalDateTime19();
                    break;
                }
                case 20: {
                    ldt = this.readLocalDateTime20();
                    break;
                }
                default: {
                    final ZonedDateTime zdt = this.readZonedDateTimeX(len);
                    if (zdt != null) {
                        return zdt;
                    }
                    break;
                }
            }
            if (ldt != null) {
                return ZonedDateTime.ofLocal(ldt, this.context.getZoneId(), null);
            }
        }
        final String str = this.readString();
        if (str.isEmpty() || "null".equals(str)) {
            return null;
        }
        final DateTimeFormatter formatter = this.context.getDateFormatter();
        if (formatter != null) {
            if (!this.context.formatHasHour) {
                final LocalDate localDate = LocalDate.parse(str, formatter);
                return ZonedDateTime.of(localDate, LocalTime.MIN, this.context.getZoneId());
            }
            final LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);
            return ZonedDateTime.of(localDateTime, this.context.getZoneId());
        }
        else {
            if (IOUtils.isNumber(str)) {
                long millis2 = Long.parseLong(str);
                if (this.context.formatUnixTime) {
                    millis2 *= 1000L;
                }
                final Instant instant2 = Instant.ofEpochMilli(millis2);
                return instant2.atZone(this.context.getZoneId());
            }
            return ZonedDateTime.parse(str);
        }
    }
    
    public OffsetTime readOffsetTime() {
        throw new JSONException("TODO");
    }
    
    public Calendar readCalendar() {
        if (this.isString()) {
            final long millis = this.readMillisFromString();
            if (millis == 0L && this.wasNull) {
                return null;
            }
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            return calendar;
        }
        else {
            if (this.readIfNull()) {
                return null;
            }
            long millis = this.readInt64Value();
            if (this.context.formatUnixTime) {
                millis *= 1000L;
            }
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            return calendar;
        }
    }
    
    public Date readDate() {
        if (this.isInt()) {
            final long millis = this.readInt64Value();
            return new Date(millis);
        }
        if (this.readIfNull()) {
            return null;
        }
        if (this.nextIfNullOrEmptyString()) {
            return null;
        }
        long millis;
        if (this.isTypeRedirect() && this.nextIfMatchIdent('\"', 'v', 'a', 'l', '\"')) {
            this.nextIfMatch(':');
            millis = this.readInt64Value();
            this.nextIfObjectEnd();
            this.setTypeRedirect(false);
        }
        else {
            millis = this.readMillisFromString();
        }
        if (millis == 0L && this.wasNull) {
            return null;
        }
        return new Date(millis);
    }
    
    public LocalTime readLocalTime() {
        if (this.nextIfNull()) {
            return null;
        }
        if (this.isInt()) {
            final long millis = this.readInt64Value();
            final Instant instant = Instant.ofEpochMilli(millis);
            final ZonedDateTime zdt = instant.atZone(this.context.getZoneId());
            return zdt.toLocalTime();
        }
        final int len = this.getStringLength();
        switch (len) {
            case 5: {
                return this.readLocalTime5();
            }
            case 8: {
                return this.readLocalTime8();
            }
            case 9: {
                return this.readLocalTime9();
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
            case 19: {
                return this.readLocalDateTime19().toLocalTime();
            }
            case 20: {
                return this.readLocalDateTime20().toLocalTime();
            }
            default: {
                final String str = this.readString();
                if (str.isEmpty() || "null".equals(str)) {
                    return null;
                }
                if (IOUtils.isNumber(str)) {
                    final long millis2 = Long.parseLong(str);
                    final Instant instant2 = Instant.ofEpochMilli(millis2);
                    final ZonedDateTime zdt2 = instant2.atZone(this.context.getZoneId());
                    return zdt2.toLocalTime();
                }
                throw new JSONException("not support len : " + str);
            }
        }
    }
    
    protected abstract int getStringLength();
    
    public Instant readInstant() {
        if (this.nextIfNull()) {
            return null;
        }
        if (this.isNumber()) {
            long millis = this.readInt64Value();
            if (this.context.formatUnixTime) {
                millis *= 1000L;
            }
            return Instant.ofEpochMilli(millis);
        }
        if (this.isObject()) {
            return this.getObjectReader(Instant.class).createInstance(this.readObject(), 0L);
        }
        final ZonedDateTime zdt = this.readZonedDateTime();
        if (zdt == null) {
            return null;
        }
        return Instant.ofEpochSecond(zdt.toEpochSecond(), zdt.toLocalTime().getNano());
    }
    
    public final long readMillisFromString() {
        this.wasNull = false;
        final String format = this.context.dateFormat;
        if (format == null || this.context.formatyyyyMMddhhmmss19 || this.context.formatyyyyMMddhhmmssT19 || this.context.formatyyyyMMdd8 || this.context.formatISO8601) {
            final int len = this.getStringLength();
            LocalDateTime ldt = null;
            switch (len) {
                case 8: {
                    final LocalDate localDate = this.readLocalDate8();
                    if (localDate == null) {
                        throw new JSONException("TODO : " + this.readString());
                    }
                    ldt = LocalDateTime.of(localDate, LocalTime.MIN);
                    break;
                }
                case 9: {
                    final LocalDate localDate = this.readLocalDate9();
                    if (localDate != null) {
                        ldt = LocalDateTime.of(localDate, LocalTime.MIN);
                        break;
                    }
                    break;
                }
                case 10: {
                    final LocalDate localDate = this.readLocalDate10();
                    if (localDate != null) {
                        ldt = LocalDateTime.of(localDate, LocalTime.MIN);
                        break;
                    }
                    final String str = this.readString();
                    if ("0000-00-00".equals(str)) {
                        this.wasNull = true;
                        return 0L;
                    }
                    if (IOUtils.isNumber(str)) {
                        return Long.parseLong(str);
                    }
                    throw new JSONException("TODO : " + str);
                }
                case 11: {
                    final LocalDate localDate = this.readLocalDate11();
                    if (localDate != null) {
                        ldt = LocalDateTime.of(localDate, LocalTime.MIN);
                        break;
                    }
                    break;
                }
                case 12: {
                    ldt = this.readLocalDateTime12();
                    break;
                }
                case 14: {
                    ldt = this.readLocalDateTime14();
                    break;
                }
                case 16: {
                    ldt = this.readLocalDateTime16();
                    break;
                }
                case 17: {
                    ldt = this.readLocalDateTime17();
                    break;
                }
                case 18: {
                    ldt = this.readLocalDateTime18();
                    break;
                }
                case 19: {
                    final long millis = this.readMillis19();
                    if (millis != 0L || !this.wasNull) {
                        return millis;
                    }
                    ldt = this.readLocalDateTime19();
                }
                case 20: {
                    ldt = this.readLocalDateTime20();
                    break;
                }
            }
            ZonedDateTime zdt = null;
            if (ldt != null) {
                zdt = ZonedDateTime.ofLocal(ldt, this.context.getZoneId(), null);
            }
            else if (len >= 20) {
                zdt = this.readZonedDateTimeX(len);
                if (zdt == null && len >= 32 && len <= 35) {
                    final String str2 = this.readString();
                    zdt = DateUtils.parseZonedDateTime(str2, null);
                }
            }
            if (zdt != null) {
                final long seconds = zdt.toEpochSecond();
                final int nanos = zdt.toLocalTime().getNano();
                if (seconds < 0L && nanos > 0) {
                    final long millis2 = (seconds + 1L) * 1000L;
                    final long adjustment = nanos / 1000000 - 1000;
                    return millis2 + adjustment;
                }
                final long millis2 = seconds * 1000L;
                return millis2 + nanos / 1000000;
            }
        }
        final String str3 = this.readString();
        if (str3.isEmpty() || "null".equals(str3)) {
            this.wasNull = true;
            return 0L;
        }
        if (this.context.formatMillis || this.context.formatUnixTime) {
            long millis3 = Long.parseLong(str3);
            if (this.context.formatUnixTime) {
                millis3 *= 1000L;
            }
            return millis3;
        }
        if (format != null && !format.isEmpty()) {
            if ("yyyy-MM-dd HH:mm:ss".equals(format)) {
                if ((str3.length() < 4 || str3.charAt(4) != '-') && IOUtils.isNumber(str3)) {
                    return Long.parseLong(str3);
                }
                return DateUtils.parseMillis19(str3, null);
            }
            else {
                final SimpleDateFormat utilFormat = new SimpleDateFormat(format);
                try {
                    return utilFormat.parse(str3).getTime();
                }
                catch (ParseException e) {
                    throw new JSONException("parse date error, " + str3 + ", expect format " + utilFormat.toPattern());
                }
            }
        }
        if ("0000-00-00T00:00:00".equals(str3) || "0001-01-01T00:00:00+08:00".equals(str3)) {
            return 0L;
        }
        if (str3.startsWith("/Date(") && str3.endsWith(")/")) {
            String dotnetDateStr = str3.substring(6, str3.length() - 2);
            int i = dotnetDateStr.indexOf(43);
            if (i == -1) {
                i = dotnetDateStr.indexOf(45);
            }
            if (i != -1) {
                dotnetDateStr = dotnetDateStr.substring(0, i);
            }
            return Long.parseLong(dotnetDateStr);
        }
        if (IOUtils.isNumber(str3)) {
            return Long.parseLong(str3);
        }
        throw new JSONException(this.info("format " + format + " not support, input " + str3));
    }
    
    protected abstract LocalDateTime readLocalDateTime12();
    
    protected abstract LocalDateTime readLocalDateTime14();
    
    protected abstract LocalDateTime readLocalDateTime16();
    
    protected abstract LocalDateTime readLocalDateTime17();
    
    protected abstract LocalDateTime readLocalDateTime18();
    
    protected abstract LocalDateTime readLocalDateTime19();
    
    protected abstract LocalDateTime readLocalDateTime20();
    
    public abstract long readMillis19();
    
    protected abstract LocalDateTime readLocalDateTimeX(final int p0);
    
    protected abstract LocalTime readLocalTime5();
    
    protected abstract LocalTime readLocalTime8();
    
    protected abstract LocalTime readLocalTime9();
    
    protected abstract LocalTime readLocalTime10();
    
    protected abstract LocalTime readLocalTime11();
    
    protected abstract LocalTime readLocalTime12();
    
    protected abstract LocalTime readLocalTime18();
    
    protected abstract LocalDate readLocalDate8();
    
    protected abstract LocalDate readLocalDate9();
    
    protected abstract LocalDate readLocalDate10();
    
    protected abstract LocalDate readLocalDate11();
    
    protected abstract ZonedDateTime readZonedDateTimeX(final int p0);
    
    public void readNumber(final ValueConsumer consumer, final boolean quoted) {
        this.readNumber0();
        final Number number = this.getNumber();
        consumer.accept(number);
    }
    
    public void readString(final ValueConsumer consumer, final boolean quoted) {
        final String str = this.readString();
        if (quoted) {
            consumer.accept(JSON.toJSONString(str));
        }
        else {
            consumer.accept(str);
        }
    }
    
    protected abstract void readNumber0();
    
    public abstract String readString();
    
    public String[] readStringArray() {
        if (this.ch == 'n' && this.nextIfNull()) {
            return null;
        }
        if (this.nextIfArrayStart()) {
            String[] values = null;
            int size = 0;
            while (!this.nextIfArrayEnd()) {
                if (this.isEnd()) {
                    throw new JSONException(this.info("input end"));
                }
                if (values == null) {
                    values = new String[16];
                }
                else if (size == values.length) {
                    values = Arrays.copyOf(values, values.length << 1);
                }
                values[size++] = this.readString();
            }
            if (values == null) {
                values = new String[0];
            }
            this.nextIfComma();
            if (values.length == size) {
                return values;
            }
            return Arrays.copyOf(values, size);
        }
        else {
            if (this.ch != '\"' && this.ch != '\'') {
                throw new JSONException(this.info("not support input"));
            }
            final String str = this.readString();
            if (str.isEmpty()) {
                return null;
            }
            throw new JSONException(this.info("not support input " + str));
        }
    }
    
    public char readCharValue() {
        final String str = this.readString();
        if (str == null || str.isEmpty()) {
            this.wasNull = true;
            return '\0';
        }
        return str.charAt(0);
    }
    
    public Character readCharacter() {
        final String str = this.readString();
        if (str == null || str.isEmpty()) {
            this.wasNull = true;
            return '\0';
        }
        return str.charAt(0);
    }
    
    public abstract void readNull();
    
    public abstract boolean readIfNull();
    
    public abstract String getString();
    
    public boolean wasNull() {
        return this.wasNull;
    }
    
    public <T> T read(final Type type) {
        final boolean fieldBased = (this.context.features & Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = this.context.provider.getObjectReader(type, fieldBased);
        return objectReader.readObject(this, null, null, 0L);
    }
    
    public final void read(final List list) {
        if (!this.nextIfArrayStart()) {
            throw new JSONException("illegal input, offset " + this.offset + ", char " + this.ch);
        }
        ++this.level;
        if (this.level >= this.context.maxLevel) {
            throw new JSONException("level too large : " + this.level);
        }
        while (!this.nextIfArrayEnd()) {
            final Object item = ObjectReaderImplObject.INSTANCE.readObject(this, null, null, 0L);
            list.add(item);
            this.nextIfComma();
        }
        --this.level;
        this.nextIfComma();
    }
    
    public final void read(final Collection list) {
        if (!this.nextIfArrayStart()) {
            throw new JSONException("illegal input, offset " + this.offset + ", char " + this.ch);
        }
        ++this.level;
        if (this.level >= this.context.maxLevel) {
            throw new JSONException("level too large : " + this.level);
        }
        while (!this.nextIfArrayEnd()) {
            final Object item = this.readAny();
            list.add(item);
            this.nextIfComma();
        }
        --this.level;
        this.nextIfComma();
    }
    
    public final void readObject(final Object object, final Feature... features) {
        long featuresLong = 0L;
        for (final Feature feature : features) {
            featuresLong |= feature.mask;
        }
        this.readObject(object, featuresLong);
    }
    
    public final void readObject(final Object object, final long features) {
        if (object == null) {
            throw new JSONException("object is null");
        }
        final Class objectClass = object.getClass();
        final boolean fieldBased = ((this.context.features | features) & Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = this.context.provider.getObjectReader(objectClass, fieldBased);
        if (objectReader instanceof ObjectReaderBean) {
            final ObjectReaderBean objectReaderBean = (ObjectReaderBean)objectReader;
            objectReaderBean.readObject(this, object, features);
        }
        else {
            if (!(object instanceof Map)) {
                throw new JSONException("read object not support");
            }
            this.read((Map)object, features);
        }
    }
    
    public void read(final Map object, final ObjectReader itemReader, final long features) {
        this.nextIfObjectStart();
        Map map;
        if (object instanceof Wrapper) {
            map = object.unwrap(Map.class);
        }
        else {
            map = (Map)object;
        }
        int i = 0;
        while (true) {
            if (this.ch == '/') {
                this.skipLineComment();
            }
            if (this.nextIfObjectEnd()) {
                this.nextIfComma();
                return;
            }
            if (i != 0 && !this.comma) {
                throw new JSONException(this.info());
            }
            final String name = this.readFieldName();
            final Object value = itemReader.readObject(this, itemReader.getObjectClass(), name, features);
            final Object origin = map.put(name, value);
            if (origin != null) {
                final long contextFeatures = features | this.context.getFeatures();
                if ((contextFeatures & Feature.DuplicateKeyValueAsArray.mask) != 0x0L) {
                    if (origin instanceof Collection) {
                        ((Collection)origin).add(value);
                        map.put(name, origin);
                    }
                    else {
                        final JSONArray array = JSONArray.of(origin, value);
                        map.put(name, array);
                    }
                }
            }
            ++i;
        }
    }
    
    public void read(final Map object, final long features) {
        boolean match = this.nextIfObjectStart();
        boolean typeRedirect = false;
        if (!match) {
            if (!(typeRedirect = this.isTypeRedirect())) {
                if (this.isString()) {
                    final String str = this.readString();
                    if (str.isEmpty()) {
                        return;
                    }
                }
                throw new JSONException("illegal input\uff0c offset " + this.offset + ", char " + this.ch);
            }
            this.setTypeRedirect(false);
        }
        Map map;
        if (object instanceof Wrapper) {
            map = object.unwrap(Map.class);
        }
        else {
            map = (Map)object;
        }
        int i = 0;
        while (true) {
            if (this.ch == '/') {
                this.skipLineComment();
            }
            if (this.nextIfObjectEnd()) {
                this.nextIfComma();
                return;
            }
            if (i != 0 && !this.comma) {
                throw new JSONException(this.info());
            }
            Object name;
            if (match || typeRedirect) {
                if (this.ch >= '1' && this.ch <= '9') {
                    name = null;
                }
                else {
                    name = this.readFieldName();
                }
            }
            else {
                name = this.getFieldName();
                match = true;
            }
            if (name == null) {
                if (this.isNumber()) {
                    name = this.readNumber();
                    if ((this.context.features & Feature.NonStringKeyAsString.mask) != 0x0L) {
                        name = name.toString();
                    }
                }
                else {
                    if ((this.context.features & Feature.AllowUnQuotedFieldNames.mask) == 0x0L) {
                        throw new JSONException(this.info("not allow unquoted fieldName"));
                    }
                    name = this.readFieldNameUnquote();
                }
                if (this.ch == ':') {
                    this.next();
                }
            }
            this.comma = false;
            Label_1136: {
                Object value = null;
                switch (this.ch) {
                    case '+':
                    case '-':
                    case '.':
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9': {
                        value = this.readNumber();
                        break;
                    }
                    case '[': {
                        value = this.readArray();
                        break;
                    }
                    case '{': {
                        if (typeRedirect) {
                            value = ObjectReaderImplObject.INSTANCE.readObject(this, null, name, features);
                            break;
                        }
                        value = this.readObject();
                        break;
                    }
                    case '\"':
                    case '\'': {
                        value = this.readString();
                        break;
                    }
                    case 'f':
                    case 't': {
                        value = this.readBoolValue();
                        break;
                    }
                    case 'n': {
                        value = this.readNullOrNewDate();
                        break;
                    }
                    case '/': {
                        this.next();
                        if (this.ch == '/') {
                            this.skipLineComment();
                            break Label_1136;
                        }
                        throw new JSONException("FASTJSON2.0.39input not support " + this.ch + ", offset " + this.offset);
                    }
                    case 'S': {
                        if (this.nextIfSet()) {
                            value = this.read(HashSet.class);
                            break;
                        }
                        throw new JSONException("FASTJSON2.0.39error, offset " + this.offset + ", char " + this.ch);
                    }
                    case 'I': {
                        if (this.nextIfInfinity()) {
                            value = Double.POSITIVE_INFINITY;
                            break;
                        }
                        throw new JSONException("FASTJSON2.0.39error, offset " + this.offset + ", char " + this.ch);
                    }
                    case 'x': {
                        value = this.readBinary();
                        break;
                    }
                    default: {
                        throw new JSONException("FASTJSON2.0.39error, offset " + this.offset + ", char " + this.ch);
                    }
                }
                final Object origin = map.put(name, value);
                if (origin != null) {
                    final long contextFeatures = features | this.context.getFeatures();
                    if ((contextFeatures & Feature.DuplicateKeyValueAsArray.mask) != 0x0L) {
                        if (origin instanceof Collection) {
                            ((Collection)origin).add(value);
                            map.put(name, origin);
                        }
                        else {
                            final JSONArray array = JSONArray.of(origin, value);
                            map.put(name, array);
                        }
                    }
                }
            }
            ++i;
        }
    }
    
    public final void read(final Map object, final Type keyType, final Type valueType, final long features) {
        final boolean match = this.nextIfObjectStart();
        if (!match) {
            throw new JSONException("illegal input\uff0c offset " + this.offset + ", char " + this.ch);
        }
        final ObjectReader keyReader = this.context.getObjectReader(keyType);
        final ObjectReader valueReader = this.context.getObjectReader(valueType);
        int i = 0;
        while (true) {
            if (this.ch == '/') {
                this.skipLineComment();
            }
            if (this.nextIfObjectEnd()) {
                this.nextIfComma();
                return;
            }
            if (i != 0 && !this.comma) {
                throw new JSONException(this.info());
            }
            Object name;
            if (keyType == String.class) {
                name = this.readFieldName();
            }
            else {
                name = keyReader.readObject(this, null, null, 0L);
                this.nextIfMatch(':');
            }
            final Object value = valueReader.readObject(this, null, null, 0L);
            final Object origin = object.put(name, value);
            if (origin != null) {
                final long contextFeatures = features | this.context.getFeatures();
                if ((contextFeatures & Feature.DuplicateKeyValueAsArray.mask) != 0x0L) {
                    if (origin instanceof Collection) {
                        ((Collection)origin).add(value);
                        object.put(name, origin);
                    }
                    else {
                        final JSONArray array = JSONArray.of(origin, value);
                        object.put(name, array);
                    }
                }
            }
            ++i;
        }
    }
    
    public <T> T read(final Class<T> type) {
        final boolean fieldBased = (this.context.features & Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = this.context.provider.getObjectReader(type, fieldBased);
        return objectReader.readObject(this, null, null, 0L);
    }
    
    public Map<String, Object> readObject() {
        this.nextIfObjectStart();
        ++this.level;
        if (this.level >= this.context.maxLevel) {
            throw new JSONException("level too large : " + this.level);
        }
        Map object;
        if (this.context.objectSupplier == null) {
            if ((this.context.features & Feature.UseNativeObject.mask) != 0x0L) {
                object = new HashMap();
            }
            else {
                object = new JSONObject();
            }
        }
        else {
            object = this.context.objectSupplier.get();
        }
        int i = 0;
        while (this.ch != '}') {
            Object name = this.readFieldName();
            if (name == null) {
                if (this.ch == '\u001a') {
                    throw new JSONException("input end");
                }
                if (this.ch == '-' || (this.ch >= '0' && this.ch <= '9')) {
                    this.readNumber0();
                    name = this.getNumber();
                }
                else {
                    name = this.readFieldNameUnquote();
                }
                this.nextIfMatch(':');
            }
            if (i == 0 && (this.context.features & Feature.ErrorOnNotSupportAutoType.mask) != 0x0L && "@type".equals(name)) {
                final String typeName = this.readString();
                throw new JSONException("autoType not support : " + typeName);
            }
            Label_0905: {
                Object val = null;
                switch (this.ch) {
                    case '+':
                    case '-':
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9': {
                        this.readNumber0();
                        val = this.getNumber();
                        break;
                    }
                    case '[': {
                        val = this.readArray();
                        break;
                    }
                    case '{': {
                        val = this.readObject();
                        break;
                    }
                    case '\"':
                    case '\'': {
                        val = this.readString();
                        break;
                    }
                    case 'f':
                    case 't': {
                        val = this.readBoolValue();
                        break;
                    }
                    case 'n': {
                        this.readNull();
                        val = null;
                        break;
                    }
                    case '/': {
                        this.next();
                        if (this.ch == '/') {
                            this.skipLineComment();
                        }
                        break Label_0905;
                    }
                    case 'I': {
                        if (this.nextIfInfinity()) {
                            val = Double.POSITIVE_INFINITY;
                            break;
                        }
                        throw new JSONException(this.info("illegal input " + this.ch));
                    }
                    case 'S': {
                        if (this.nextIfSet()) {
                            val = this.read(Set.class);
                            break;
                        }
                        throw new JSONException(this.info("illegal input " + this.ch));
                    }
                    default: {
                        throw new JSONException(this.info("illegal input " + this.ch));
                    }
                }
                object.put(name, val);
            }
            ++i;
        }
        this.next();
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            this.next();
        }
        --this.level;
        return (Map<String, Object>)object;
    }
    
    public abstract void skipLineComment();
    
    public Boolean readBool() {
        if (this.isNull()) {
            this.readNull();
            return null;
        }
        final boolean boolValue = this.readBoolValue();
        if (!boolValue && this.wasNull) {
            return null;
        }
        return boolValue;
    }
    
    public abstract boolean readBoolValue();
    
    public Object readAny() {
        return this.read(Object.class);
    }
    
    public List readArray(final Type itemType) {
        if (this.nextIfNull()) {
            return null;
        }
        if (!this.nextIfArrayStart()) {
            throw new JSONException(this.info("syntax error : " + this.ch));
        }
        final boolean fieldBased = (this.context.features & Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = this.context.provider.getObjectReader(itemType, fieldBased);
        final List list = new ArrayList();
        while (!this.nextIfArrayEnd()) {
            final int mark = this.offset;
            final Object item = objectReader.readObject(this, null, null, 0L);
            if (mark == this.offset || this.ch == '}' || this.ch == '\u001a') {
                throw new JSONException("illegal input : " + this.ch + ", offset " + this.getOffset());
            }
            list.add(item);
        }
        if (this.comma = (this.ch == ',')) {
            this.next();
        }
        return list;
    }
    
    public List readList(final Type[] types) {
        if (this.nextIfNull()) {
            return null;
        }
        if (!this.nextIfArrayStart()) {
            throw new JSONException("syntax error : " + this.ch);
        }
        int i = 0;
        final int max = types.length;
        final List list = new ArrayList(max);
        while (!this.nextIfArrayEnd() && i < max) {
            final int mark = this.offset;
            final Object item = this.read(types[i++]);
            if (mark == this.offset || this.ch == '}' || this.ch == '\u001a') {
                throw new JSONException("illegal input : " + this.ch + ", offset " + this.getOffset());
            }
            list.add(item);
        }
        if (i != max) {
            throw new JSONException(this.info("element length mismatch"));
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            this.next();
        }
        return list;
    }
    
    public final Object[] readArray(final Type[] types) {
        if (this.nextIfNull()) {
            return null;
        }
        if (!this.nextIfArrayStart()) {
            throw new JSONException(this.info("syntax error"));
        }
        int i;
        int max;
        Object[] list;
        int mark;
        Object item;
        for (i = 0, max = types.length, list = new Object[max]; !this.nextIfArrayEnd() && i < max; list[i++] = item) {
            mark = this.offset;
            item = this.read(types[i]);
            if (mark == this.offset || this.ch == '}' || this.ch == '\u001a') {
                throw new JSONException("illegal input : " + this.ch + ", offset " + this.getOffset());
            }
        }
        if (i != max) {
            throw new JSONException(this.info("element length mismatch"));
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            this.next();
        }
        return list;
    }
    
    public final void readArray(final List list, final Type itemType) {
        this.readArray((Collection)list, itemType);
    }
    
    public final void readArray(final Collection list, final Type itemType) {
        if (this.nextIfArrayStart()) {
            while (!this.nextIfArrayEnd()) {
                final Object item = this.read(itemType);
                list.add(item);
            }
            return;
        }
        if (this.isString()) {
            final String str = this.readString();
            if (itemType == String.class) {
                list.add(str);
            }
            else {
                final Function typeConvert = this.context.getProvider().getTypeConvert(String.class, itemType);
                if (typeConvert == null) {
                    throw new JSONException(this.info("not support input " + str));
                }
                if (str.indexOf(44) != -1) {
                    final String[] split;
                    final String[] items = split = str.split(",");
                    for (final String strItem : split) {
                        final Object item2 = typeConvert.apply(strItem);
                        list.add(item2);
                    }
                }
                else {
                    final Object item3 = typeConvert.apply(str);
                    list.add(item3);
                }
            }
        }
        else {
            final Object item = this.read(itemType);
            list.add(item);
        }
        final boolean comma = this.ch == ',';
        this.comma = comma;
        if (comma) {
            this.next();
        }
    }
    
    public final JSONArray readJSONArray() {
        final JSONArray array = new JSONArray();
        this.read(array);
        return array;
    }
    
    public final JSONObject readJSONObject() {
        final JSONObject object = new JSONObject();
        this.read(object, 0L);
        return object;
    }
    
    public List readArray() {
        this.next();
        ++this.level;
        if (this.level >= this.context.maxLevel) {
            throw new JSONException("level too large : " + this.level);
        }
        int i = 0;
        List<Object> list = null;
        Object first = null;
        Object second = null;
        while (true) {
            Label_0701: {
                Object val = null;
                switch (this.ch) {
                    case ']': {
                        this.next();
                        if (list == null) {
                            if (this.context.arraySupplier != null) {
                                list = this.context.arraySupplier.get();
                            }
                            else if (this.context.isEnabled(Feature.UseNativeObject)) {
                                list = ((i == 2) ? new ArrayList<Object>(2) : new ArrayList<Object>(1));
                            }
                            else {
                                list = ((i == 2) ? new JSONArray(2) : new JSONArray(1));
                            }
                            if (i == 1) {
                                list.add(first);
                            }
                            else if (i == 2) {
                                list.add(first);
                                list.add(second);
                            }
                        }
                        final boolean comma = this.ch == ',';
                        this.comma = comma;
                        if (comma) {
                            this.next();
                        }
                        --this.level;
                        return list;
                    }
                    case '[': {
                        val = this.readArray();
                        break;
                    }
                    case '{': {
                        if (this.context.autoTypeBeforeHandler != null || (this.context.features & Feature.SupportAutoType.mask) != 0x0L) {
                            val = ObjectReaderImplObject.INSTANCE.readObject(this, null, null, 0L);
                            break;
                        }
                        val = this.readObject();
                        break;
                    }
                    case '\"':
                    case '\'': {
                        val = this.readString();
                        break;
                    }
                    case '+':
                    case '-':
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9': {
                        this.readNumber0();
                        val = this.getNumber();
                        break;
                    }
                    case 'f':
                    case 't': {
                        val = this.readBoolValue();
                        break;
                    }
                    case 'n': {
                        this.readNull();
                        val = null;
                        break;
                    }
                    case '/': {
                        this.skipLineComment();
                        break Label_0701;
                    }
                    default: {
                        throw new JSONException("TODO : " + this.ch);
                    }
                }
                if (i == 0) {
                    first = val;
                }
                else if (i == 1) {
                    second = val;
                }
                else if (i == 2) {
                    if (this.context.arraySupplier != null) {
                        list = this.context.arraySupplier.get();
                    }
                    else {
                        list = new JSONArray();
                    }
                    list.add(first);
                    list.add(second);
                    list.add(val);
                }
                else {
                    list.add(val);
                }
            }
            ++i;
        }
    }
    
    public final BigInteger getBigInteger() {
        final Number number = this.getNumber();
        if (number == null) {
            return null;
        }
        if (number instanceof BigInteger) {
            return (BigInteger)number;
        }
        return BigInteger.valueOf(number.longValue());
    }
    
    public final BigDecimal getBigDecimal() {
        if (this.wasNull) {
            return null;
        }
        switch (this.valueType) {
            case 1: {
                if (this.mag1 == 0 && this.mag2 == 0 && this.mag3 >= 0) {
                    return BigDecimal.valueOf(this.negative ? (-this.mag3) : ((long)this.mag3));
                }
                int[] mag;
                if (this.mag0 == 0) {
                    if (this.mag1 == 0) {
                        final long v3 = (long)this.mag3 & 0xFFFFFFFFL;
                        final long v4 = (long)this.mag2 & 0xFFFFFFFFL;
                        if (v4 <= 2147483647L) {
                            final long v5 = (v4 << 32) + v3;
                            return BigDecimal.valueOf(this.negative ? (-v5) : v5);
                        }
                        mag = new int[] { this.mag2, this.mag3 };
                    }
                    else {
                        mag = new int[] { this.mag1, this.mag2, this.mag3 };
                    }
                }
                else {
                    mag = new int[] { this.mag0, this.mag1, this.mag2, this.mag3 };
                }
                final int signum = this.negative ? -1 : 1;
                final BigInteger bigInt = BigIntegerCreator.BIG_INTEGER_CREATOR.apply(signum, mag);
                return new BigDecimal(bigInt);
            }
            case 2: {
                BigDecimal decimal = null;
                if (this.exponent == 0 && this.mag0 == 0 && this.mag1 == 0) {
                    if (this.mag2 == 0 && this.mag3 >= 0) {
                        final int unscaledVal = this.negative ? (-this.mag3) : this.mag3;
                        decimal = BigDecimal.valueOf(unscaledVal, this.scale);
                    }
                    else {
                        final long v3 = (long)this.mag3 & 0xFFFFFFFFL;
                        final long v4 = (long)this.mag2 & 0xFFFFFFFFL;
                        if (v4 <= 2147483647L) {
                            final long v5 = (v4 << 32) + v3;
                            final long unscaledVal2 = this.negative ? (-v5) : v5;
                            decimal = BigDecimal.valueOf(unscaledVal2, this.scale);
                        }
                    }
                }
                if (decimal == null) {
                    final int[] mag2 = (this.mag0 == 0) ? ((this.mag1 == 0) ? ((this.mag2 == 0) ? new int[] { this.mag3 } : new int[] { this.mag2, this.mag3 }) : new int[] { this.mag1, this.mag2, this.mag3 }) : new int[] { this.mag0, this.mag1, this.mag2, this.mag3 };
                    final int signum2 = this.negative ? -1 : 1;
                    final BigInteger bigInt2 = BigIntegerCreator.BIG_INTEGER_CREATOR.apply(signum2, mag2);
                    decimal = new BigDecimal(bigInt2, this.scale);
                }
                if (this.exponent != 0) {
                    final String doubleStr = decimal.toPlainString() + "E" + this.exponent;
                    final double doubleValue = Double.parseDouble(doubleStr);
                    return TypeUtils.toBigDecimal(doubleValue);
                }
                return decimal;
            }
            case 8: {
                return TypeUtils.toBigDecimal(this.stringValue);
            }
            case 4: {
                return this.boolValue ? BigDecimal.ONE : BigDecimal.ZERO;
            }
            case 3: {
                try {
                    return TypeUtils.toBigDecimal(this.stringValue);
                }
                catch (NumberFormatException ex) {
                    throw new JSONException(this.info("read decimal error, value " + this.stringValue), ex);
                }
            }
            case 6: {
                final JSONObject object = (JSONObject)this.complex;
                BigDecimal decimal2 = object.getBigDecimal("value");
                if (decimal2 == null) {
                    decimal2 = object.getBigDecimal("$numberDecimal");
                }
                if (decimal2 != null) {
                    return decimal2;
                }
                throw new JSONException("TODO : " + this.valueType);
            }
            default: {
                throw new JSONException("TODO : " + this.valueType);
            }
        }
    }
    
    public final Number getNumber() {
        if (this.wasNull) {
            return null;
        }
        switch (this.valueType) {
            case 1:
            case 11: {
                if (this.mag0 != 0 || this.mag1 != 0 || this.mag2 != 0 || this.mag3 == Integer.MIN_VALUE) {
                    int[] mag;
                    if (this.mag0 == 0) {
                        if (this.mag1 == 0) {
                            final long v3 = (long)this.mag3 & 0xFFFFFFFFL;
                            final long v4 = (long)this.mag2 & 0xFFFFFFFFL;
                            if (v4 <= 2147483647L) {
                                final long v5 = (v4 << 32) + v3;
                                return this.negative ? (-v5) : v5;
                            }
                            mag = new int[] { this.mag2, this.mag3 };
                        }
                        else {
                            mag = new int[] { this.mag1, this.mag2, this.mag3 };
                        }
                    }
                    else {
                        mag = new int[] { this.mag0, this.mag1, this.mag2, this.mag3 };
                    }
                    final int signum = this.negative ? -1 : 1;
                    return BigIntegerCreator.BIG_INTEGER_CREATOR.apply(signum, mag);
                }
                int intVlaue;
                if (this.negative) {
                    if (this.mag3 < 0) {
                        return -((long)this.mag3 & 0xFFFFFFFFL);
                    }
                    intVlaue = -this.mag3;
                }
                else {
                    if (this.mag3 < 0) {
                        return (long)this.mag3 & 0xFFFFFFFFL;
                    }
                    intVlaue = this.mag3;
                }
                if (this.valueType == 11) {
                    return intVlaue;
                }
                return intVlaue;
            }
            case 10: {
                if (this.mag0 == 0 && this.mag1 == 0 && this.mag2 == 0 && this.mag3 >= 0) {
                    final int intValue = this.negative ? (-this.mag3) : this.mag3;
                    return (short)intValue;
                }
                throw new JSONException(this.info("shortValue overflow"));
            }
            case 9: {
                if (this.mag0 == 0 && this.mag1 == 0 && this.mag2 == 0 && this.mag3 >= 0) {
                    final int intValue = this.negative ? (-this.mag3) : this.mag3;
                    return (byte)intValue;
                }
                throw new JSONException(this.info("shortValue overflow"));
            }
            case 2: {
                BigDecimal decimal = null;
                if (this.mag0 == 0 && this.mag1 == 0) {
                    if (this.mag2 == 0 && this.mag3 >= 0) {
                        final int unscaledVal = this.negative ? (-this.mag3) : this.mag3;
                        if (this.exponent == 0) {
                            if ((this.context.features & Feature.UseBigDecimalForFloats.mask) != 0x0L) {
                                switch (this.scale) {
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                    case 10: {
                                        return (float)(unscaledVal / JSONFactory.DOUBLE_10_POW[this.scale]);
                                    }
                                }
                            }
                            else if ((this.context.features & Feature.UseBigDecimalForDoubles.mask) != 0x0L) {
                                if (unscaledVal == 0) {
                                    return JSONFactory.DOUBLE_ZERO;
                                }
                                switch (this.scale) {
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                    case 10:
                                    case 11:
                                    case 12:
                                    case 13:
                                    case 14:
                                    case 15: {
                                        return unscaledVal / JSONFactory.DOUBLE_10_POW[this.scale];
                                    }
                                }
                            }
                        }
                        decimal = BigDecimal.valueOf(unscaledVal, this.scale);
                    }
                    else {
                        final long v3 = (long)this.mag3 & 0xFFFFFFFFL;
                        final long v4 = (long)this.mag2 & 0xFFFFFFFFL;
                        if (v4 <= 2147483647L) {
                            final long v5 = (v4 << 32) + v3;
                            final long unscaledVal2 = this.negative ? (-v5) : v5;
                            if (this.exponent == 0) {
                                if ((this.context.features & Feature.UseBigDecimalForFloats.mask) != 0x0L) {
                                    boolean isNegative;
                                    long unsignedUnscaledVal;
                                    if (unscaledVal2 < 0L) {
                                        isNegative = true;
                                        unsignedUnscaledVal = -unscaledVal2;
                                    }
                                    else {
                                        isNegative = false;
                                        unsignedUnscaledVal = unscaledVal2;
                                    }
                                    final int len = IOUtils.stringSize(unsignedUnscaledVal);
                                    if (this.doubleChars == null) {
                                        this.doubleChars = new char[20];
                                    }
                                    IOUtils.getChars(unsignedUnscaledVal, len, this.doubleChars);
                                    return TypeUtils.floatValue(isNegative, len - this.scale, this.doubleChars, len);
                                }
                                if ((this.context.features & Feature.UseBigDecimalForDoubles.mask) != 0x0L) {
                                    boolean isNegative;
                                    long unsignedUnscaledVal;
                                    if (unscaledVal2 < 0L) {
                                        isNegative = true;
                                        unsignedUnscaledVal = -unscaledVal2;
                                    }
                                    else {
                                        isNegative = false;
                                        unsignedUnscaledVal = unscaledVal2;
                                    }
                                    if (unsignedUnscaledVal < 4503599627370496L) {
                                        if (this.scale > 0 && this.scale < JSONFactory.DOUBLE_10_POW.length) {
                                            return unscaledVal2 / JSONFactory.DOUBLE_10_POW[this.scale];
                                        }
                                        if (this.scale < 0 && this.scale > -JSONFactory.DOUBLE_10_POW.length) {
                                            return unscaledVal2 * JSONFactory.DOUBLE_10_POW[-this.scale];
                                        }
                                    }
                                    final int len = (unsignedUnscaledVal < 10000000000000000L) ? 16 : ((unsignedUnscaledVal < 100000000000000000L) ? 17 : ((unsignedUnscaledVal < 1000000000000000000L) ? 18 : 19));
                                    if (this.doubleChars == null) {
                                        this.doubleChars = new char[20];
                                    }
                                    IOUtils.getChars(unsignedUnscaledVal, len, this.doubleChars);
                                    return TypeUtils.doubleValue(isNegative, len - this.scale, this.doubleChars, len);
                                }
                            }
                            decimal = BigDecimal.valueOf(unscaledVal2, this.scale);
                        }
                    }
                }
                if (decimal == null) {
                    final int[] mag2 = (this.mag0 == 0) ? ((this.mag1 == 0) ? new int[] { this.mag2, this.mag3 } : new int[] { this.mag1, this.mag2, this.mag3 }) : new int[] { this.mag0, this.mag1, this.mag2, this.mag3 };
                    final int signum2 = this.negative ? -1 : 1;
                    final BigInteger bigInt = BigIntegerCreator.BIG_INTEGER_CREATOR.apply(signum2, mag2);
                    final int adjustedScale = this.scale - this.exponent;
                    decimal = new BigDecimal(bigInt, adjustedScale);
                    if (this.exponent != 0) {
                        return decimal.doubleValue();
                    }
                }
                if (this.exponent != 0) {
                    final String decimalStr = decimal.toPlainString();
                    return Double.parseDouble(decimalStr + "E" + this.exponent);
                }
                if ((this.context.features & Feature.UseBigDecimalForFloats.mask) != 0x0L) {
                    return decimal.floatValue();
                }
                if ((this.context.features & Feature.UseBigDecimalForDoubles.mask) != 0x0L) {
                    return decimal.doubleValue();
                }
                return decimal;
            }
            case 8: {
                if (this.scale > 0) {
                    return TypeUtils.toBigDecimal(this.stringValue);
                }
                return new BigInteger(this.stringValue);
            }
            case 12:
            case 13: {
                final int[] mag = (this.mag0 == 0) ? ((this.mag1 == 0) ? ((this.mag2 == 0) ? new int[] { this.mag3 } : new int[] { this.mag2, this.mag3 }) : new int[] { this.mag1, this.mag2, this.mag3 }) : new int[] { this.mag0, this.mag1, this.mag2, this.mag3 };
                final int signum = this.negative ? -1 : 1;
                final BigInteger bigInt2 = BigIntegerCreator.BIG_INTEGER_CREATOR.apply(signum, mag);
                final BigDecimal decimal2 = new BigDecimal(bigInt2, this.scale);
                if (this.valueType == 12) {
                    if (this.exponent != 0) {
                        return Float.parseFloat(decimal2 + "E" + this.exponent);
                    }
                    return decimal2.floatValue();
                }
                else {
                    if (this.exponent != 0) {
                        return Double.parseDouble(decimal2 + "E" + this.exponent);
                    }
                    return decimal2.doubleValue();
                }
                break;
            }
            case 4: {
                return this.boolValue ? 1 : 0;
            }
            case 5: {
                return null;
            }
            case 3: {
                return this.toInt64(this.stringValue);
            }
            case 6: {
                return this.toNumber((Map)this.complex);
            }
            case 7: {
                return this.toNumber((List)this.complex);
            }
            default: {
                throw new JSONException("TODO : " + this.valueType);
            }
        }
    }
    
    @Override
    public abstract void close();
    
    protected final int toInt32(final String val) {
        if (IOUtils.isNumber(val)) {
            return Integer.parseInt(val);
        }
        throw new JSONException("parseInt error, value : " + val);
    }
    
    protected final long toInt64(final String val) {
        if (IOUtils.isNumber(val)) {
            return Long.parseLong(val);
        }
        if (val.length() > 10 && val.length() < 40) {
            try {
                return DateUtils.parseMillis(val, this.context.zoneId);
            }
            catch (DateTimeException ex) {}
            catch (JSONException ex2) {}
        }
        throw new JSONException("parseLong error, value : " + val);
    }
    
    protected final long toLong(final Map map) {
        final Object val = map.get("val");
        if (val instanceof Number) {
            return ((Number)val).intValue();
        }
        throw new JSONException("parseLong error, value : " + map);
    }
    
    protected final int toInt(final List list) {
        if (list.size() == 1) {
            final Object val = list.get(0);
            if (val instanceof Number) {
                return ((Number)val).intValue();
            }
            if (val instanceof String) {
                return Integer.parseInt((String)val);
            }
        }
        throw new JSONException("parseLong error, field : value " + list);
    }
    
    protected final Number toNumber(final Map map) {
        final Object val = map.get("val");
        if (val instanceof Number) {
            return (Number)val;
        }
        return null;
    }
    
    protected final BigDecimal decimal(final JSONObject object) {
        BigDecimal decimal = object.getBigDecimal("value");
        if (decimal == null) {
            decimal = object.getBigDecimal("$numberDecimal");
        }
        if (decimal != null) {
            return decimal;
        }
        throw new JSONException("can not cast to decimal " + object);
    }
    
    protected final Number toNumber(final List list) {
        if (list.size() == 1) {
            final Object val = list.get(0);
            if (val instanceof Number) {
                return (Number)val;
            }
            if (val instanceof String) {
                return TypeUtils.toBigDecimal((String)val);
            }
        }
        return null;
    }
    
    protected final String toString(final List array) {
        final JSONWriter writer = JSONWriter.of();
        writer.setRootObject(array);
        writer.write(array);
        return writer.toString();
    }
    
    protected final String toString(final Map object) {
        final JSONWriter writer = JSONWriter.of();
        writer.setRootObject(object);
        writer.write(object);
        return writer.toString();
    }
    
    public static JSONReader of(final byte[] utf8Bytes) {
        boolean ascii = false;
        if (JDKUtils.PREDICATE_IS_ASCII != null) {
            ascii = JDKUtils.PREDICATE_IS_ASCII.test(utf8Bytes);
        }
        final Context context = JSONFactory.createReadContext();
        if (ascii) {
            if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII != null) {
                return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII.create(context, null, utf8Bytes, 0, utf8Bytes.length);
            }
            return new JSONReaderASCII(context, null, utf8Bytes, 0, utf8Bytes.length);
        }
        else {
            if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8 != null) {
                return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8.create(context, null, utf8Bytes, 0, utf8Bytes.length);
            }
            return new JSONReaderUTF8(context, null, utf8Bytes, 0, utf8Bytes.length);
        }
    }
    
    @Deprecated
    public static JSONReader of(final Context context, final byte[] utf8Bytes) {
        boolean ascii = false;
        if (JDKUtils.PREDICATE_IS_ASCII != null) {
            ascii = JDKUtils.PREDICATE_IS_ASCII.test(utf8Bytes);
        }
        if (ascii) {
            if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII != null) {
                return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII.create(context, null, utf8Bytes, 0, utf8Bytes.length);
            }
            return new JSONReaderASCII(context, null, utf8Bytes, 0, utf8Bytes.length);
        }
        else {
            if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8 != null) {
                return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8.create(context, null, utf8Bytes, 0, utf8Bytes.length);
            }
            return new JSONReaderUTF8(context, null, utf8Bytes, 0, utf8Bytes.length);
        }
    }
    
    public static JSONReader of(final byte[] utf8Bytes, final Context context) {
        boolean ascii = false;
        if (JDKUtils.PREDICATE_IS_ASCII != null) {
            ascii = JDKUtils.PREDICATE_IS_ASCII.test(utf8Bytes);
        }
        if (ascii) {
            if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII != null) {
                return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII.create(context, null, utf8Bytes, 0, utf8Bytes.length);
            }
            return new JSONReaderASCII(context, null, utf8Bytes, 0, utf8Bytes.length);
        }
        else {
            if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8 != null) {
                return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8.create(context, null, utf8Bytes, 0, utf8Bytes.length);
            }
            return new JSONReaderUTF8(context, null, utf8Bytes, 0, utf8Bytes.length);
        }
    }
    
    public static JSONReader of(final char[] chars) {
        final Context context = JSONFactory.createReadContext();
        if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16 != null) {
            return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16.create(context, null, chars, 0, chars.length);
        }
        return new JSONReaderUTF16(context, null, chars, 0, chars.length);
    }
    
    @Deprecated
    public static JSONReader of(final Context context, final char[] chars) {
        if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16 != null) {
            return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16.create(context, null, chars, 0, chars.length);
        }
        return new JSONReaderUTF16(context, null, chars, 0, chars.length);
    }
    
    public static JSONReader of(final char[] chars, final Context context) {
        return new JSONReaderUTF16(context, null, chars, 0, chars.length);
    }
    
    public static JSONReader ofJSONB(final byte[] jsonbBytes) {
        return new JSONReaderJSONB(JSONFactory.createReadContext(), jsonbBytes, 0, jsonbBytes.length);
    }
    
    @Deprecated
    public static JSONReader ofJSONB(final Context context, final byte[] jsonbBytes) {
        return new JSONReaderJSONB(context, jsonbBytes, 0, jsonbBytes.length);
    }
    
    public static JSONReader ofJSONB(final byte[] jsonbBytes, final Context context) {
        return new JSONReaderJSONB(context, jsonbBytes, 0, jsonbBytes.length);
    }
    
    public static JSONReader ofJSONB(final byte[] jsonbBytes, final Feature... features) {
        final Context context = JSONFactory.createReadContext();
        context.config(features);
        return new JSONReaderJSONB(context, jsonbBytes, 0, jsonbBytes.length);
    }
    
    public static JSONReader ofJSONB(final byte[] bytes, final int offset, final int length) {
        return new JSONReaderJSONB(JSONFactory.createReadContext(), bytes, offset, length);
    }
    
    public static JSONReader ofJSONB(final byte[] bytes, final int offset, final int length, final Context context) {
        return new JSONReaderJSONB(context, bytes, offset, length);
    }
    
    public static JSONReader ofJSONB(final byte[] bytes, final int offset, final int length, final SymbolTable symbolTable) {
        return new JSONReaderJSONB(JSONFactory.createReadContext(symbolTable), bytes, offset, length);
    }
    
    public static JSONReader of(final byte[] bytes, final int offset, final int length, final Charset charset) {
        final Context context = JSONFactory.createReadContext();
        if (charset == StandardCharsets.UTF_8) {
            if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8 != null) {
                return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8.create(context, null, bytes, offset, length);
            }
            return new JSONReaderUTF8(context, null, bytes, offset, length);
        }
        else {
            if (charset == StandardCharsets.UTF_16) {
                return new JSONReaderUTF16(context, bytes, offset, length);
            }
            if (charset != StandardCharsets.US_ASCII && charset != StandardCharsets.ISO_8859_1) {
                throw new JSONException("not support charset " + charset);
            }
            if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII != null) {
                return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII.create(context, null, bytes, offset, length);
            }
            return new JSONReaderASCII(context, null, bytes, offset, length);
        }
    }
    
    public static JSONReader of(final byte[] bytes, final int offset, final int length, final Charset charset, final Context context) {
        if (charset == StandardCharsets.UTF_8) {
            if (offset == 0 && bytes.length == length) {
                return of(bytes, context);
            }
            boolean hasNegative = true;
            if (JDKUtils.METHOD_HANDLE_HAS_NEGATIVE != null) {
                try {
                    hasNegative = JDKUtils.METHOD_HANDLE_HAS_NEGATIVE.invoke(bytes, 0, bytes.length);
                }
                catch (Throwable t) {}
            }
            if (!hasNegative) {
                if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII != null) {
                    return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII.create(context, null, bytes, offset, length);
                }
                return new JSONReaderASCII(context, null, bytes, offset, length);
            }
            else {
                if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8 != null) {
                    return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8.create(context, null, bytes, offset, length);
                }
                return new JSONReaderUTF8(context, null, bytes, offset, length);
            }
        }
        else {
            if (charset == StandardCharsets.UTF_16) {
                return new JSONReaderUTF16(context, bytes, offset, length);
            }
            if (charset != StandardCharsets.US_ASCII && charset != StandardCharsets.ISO_8859_1) {
                throw new JSONException("not support charset " + charset);
            }
            if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII != null) {
                return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII.create(context, null, bytes, offset, length);
            }
            return new JSONReaderASCII(context, null, bytes, offset, length);
        }
    }
    
    public static JSONReader of(final byte[] bytes, final int offset, final int length) {
        final Context context = JSONFactory.createReadContext();
        if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8 != null) {
            return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8.create(context, null, bytes, offset, length);
        }
        return new JSONReaderUTF8(context, null, bytes, offset, length);
    }
    
    public static JSONReader of(final byte[] bytes, final int offset, final int length, final Context context) {
        if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8 != null) {
            return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF8.create(context, null, bytes, offset, length);
        }
        return new JSONReaderUTF8(context, null, bytes, offset, length);
    }
    
    public static JSONReader of(final char[] chars, final int offset, final int length) {
        final Context context = JSONFactory.createReadContext();
        if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16 != null) {
            return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16.create(context, null, chars, offset, length);
        }
        return new JSONReaderUTF16(context, null, chars, offset, length);
    }
    
    public static JSONReader of(final char[] chars, final int offset, final int length, final Context context) {
        if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16 != null) {
            return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16.create(context, null, chars, offset, length);
        }
        return new JSONReaderUTF16(context, null, chars, offset, length);
    }
    
    public static JSONReader of(final URL url, final Context context) throws IOException {
        final InputStream is = url.openStream();
        try {
            final JSONReader of = of(is, StandardCharsets.UTF_8, context);
            if (is != null) {
                is.close();
            }
            return of;
        }
        catch (Throwable t) {
            if (is != null) {
                try {
                    is.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    public static JSONReader of(final InputStream is, final Charset charset) {
        final Context context = JSONFactory.createReadContext();
        return of(is, charset, context);
    }
    
    public static JSONReader of(final InputStream is, final Charset charset, final Context context) {
        if (charset == StandardCharsets.UTF_8 || charset == null) {
            return new JSONReaderUTF8(context, is);
        }
        if (charset == StandardCharsets.UTF_16) {
            return new JSONReaderUTF16(context, is);
        }
        throw new JSONException("not support charset " + charset);
    }
    
    public static JSONReader of(final Reader is) {
        return new JSONReaderUTF16(JSONFactory.createReadContext(), is);
    }
    
    public static JSONReader of(final Reader is, final Context context) {
        return new JSONReaderUTF16(context, is);
    }
    
    public static JSONReader of(final ByteBuffer buffer, final Charset charset) {
        final Context context = JSONFactory.createReadContext();
        if (charset == StandardCharsets.UTF_8 || charset == null) {
            return new JSONReaderUTF8(context, buffer);
        }
        throw new JSONException("not support charset " + charset);
    }
    
    public static JSONReader of(final ByteBuffer buffer, final Charset charset, final Context context) {
        if (charset == StandardCharsets.UTF_8 || charset == null) {
            return new JSONReaderUTF8(context, buffer);
        }
        throw new JSONException("not support charset " + charset);
    }
    
    @Deprecated
    public static JSONReader of(final Context context, final String str) {
        return of(str, context);
    }
    
    public static JSONReader of(final String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        final Context context = JSONFactory.createReadContext();
        if (JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER != null && JDKUtils.PREDICATE_IS_ASCII != null) {
            try {
                final int LATIN1 = 0;
                final int coder = JDKUtils.STRING_CODER.applyAsInt(str);
                if (coder == 0) {
                    final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
                    if (JDKUtils.PREDICATE_IS_ASCII.test(bytes)) {
                        if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII != null) {
                            return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII.create(context, str, bytes, 0, bytes.length);
                        }
                        return new JSONReaderASCII(context, str, bytes, 0, bytes.length);
                    }
                }
            }
            catch (Exception e) {
                throw new JSONException("unsafe get String.coder error");
            }
        }
        final int length = str.length();
        if (JDKUtils.JVM_VERSION == 8) {
            final char[] chars = JDKUtils.getCharArray(str);
            return new JSONReaderUTF16(context, str, chars, 0, length);
        }
        if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16 != null) {
            return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16.create(context, str, null, 0, length);
        }
        return new JSONReaderUTF16(context, str, 0, length);
    }
    
    public static JSONReader of(final String str, final Context context) {
        if (str == null || context == null) {
            throw new NullPointerException();
        }
        if (JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER != null) {
            try {
                final int LATIN1 = 0;
                final int coder = JDKUtils.STRING_CODER.applyAsInt(str);
                if (coder == 0) {
                    final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
                    if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII != null) {
                        return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII.create(context, str, bytes, 0, bytes.length);
                    }
                    return new JSONReaderASCII(context, str, bytes, 0, bytes.length);
                }
            }
            catch (Exception e) {
                throw new JSONException("unsafe get String.coder error");
            }
        }
        final int length = str.length();
        char[] chars;
        if (JDKUtils.JVM_VERSION == 8) {
            chars = JDKUtils.getCharArray(str);
        }
        else {
            chars = str.toCharArray();
        }
        if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16 != null) {
            return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16.create(context, str, chars, 0, length);
        }
        return new JSONReaderUTF16(context, str, chars, 0, length);
    }
    
    public static JSONReader of(final String str, final int offset, final int length) {
        if (str == null) {
            throw new NullPointerException();
        }
        final Context context = JSONFactory.createReadContext();
        if (JDKUtils.STRING_VALUE != null) {
            try {
                final int LATIN1 = 0;
                final int coder = JDKUtils.STRING_CODER.applyAsInt(str);
                if (coder == 0) {
                    final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
                    if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII != null) {
                        return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII.create(context, str, bytes, offset, length);
                    }
                    return new JSONReaderASCII(context, str, bytes, offset, length);
                }
            }
            catch (Exception e) {
                throw new JSONException("unsafe get String.coder error");
            }
        }
        char[] chars;
        if (JDKUtils.JVM_VERSION == 8) {
            chars = JDKUtils.getCharArray(str);
        }
        else {
            chars = str.toCharArray();
        }
        if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16 != null) {
            return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16.create(context, str, chars, offset, length);
        }
        return new JSONReaderUTF16(context, str, chars, offset, length);
    }
    
    public static JSONReader of(final String str, final int offset, final int length, final Context context) {
        if (str == null || context == null) {
            throw new NullPointerException();
        }
        if (JDKUtils.STRING_VALUE != null && JDKUtils.STRING_CODER != null) {
            try {
                final int LATIN1 = 0;
                final int coder = JDKUtils.STRING_CODER.applyAsInt(str);
                if (coder == 0) {
                    final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
                    if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII != null) {
                        return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_ASCII.create(context, str, bytes, offset, length);
                    }
                    return new JSONReaderASCII(context, str, bytes, offset, length);
                }
            }
            catch (Exception e) {
                throw new JSONException("unsafe get String.coder error");
            }
        }
        char[] chars;
        if (JDKUtils.JVM_VERSION == 8) {
            chars = JDKUtils.getCharArray(str);
        }
        else {
            chars = str.toCharArray();
        }
        if (JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16 != null) {
            return JSONFactory.INCUBATOR_VECTOR_READER_CREATOR_UTF16.create(context, str, chars, offset, length);
        }
        return new JSONReaderUTF16(context, str, chars, offset, length);
    }
    
    final void bigInt(final char[] chars, final int off, final int len) {
        int cursor = off;
        int numDigits = len - cursor;
        if (this.scale > 0) {
            --numDigits;
        }
        if (numDigits > 38) {
            throw new JSONException("number too large : " + new String(chars, off, numDigits));
        }
        int firstGroupLen = numDigits % 9;
        if (firstGroupLen == 0) {
            firstGroupLen = 9;
        }
        int start = cursor;
        int end;
        cursor = (end = cursor + firstGroupLen);
        char c = chars[start++];
        if (c == '.') {
            c = chars[start++];
            ++cursor;
        }
        int result = c - '0';
        for (int index = start; index < end; ++index) {
            c = chars[index];
            if (c == '.') {
                c = chars[++index];
                ++cursor;
                if (end < len) {
                    ++end;
                }
            }
            final int nextVal = c - '0';
            result = 10 * result + nextVal;
        }
        this.mag3 = result;
        while (cursor < len) {
            int start2 = cursor;
            cursor += 9;
            int end2 = cursor;
            char c2 = chars[start2++];
            if (c2 == '.') {
                c2 = chars[start2++];
                ++cursor;
                ++end2;
            }
            int result2 = c2 - '0';
            for (int index2 = start2; index2 < end2; ++index2) {
                c2 = chars[index2];
                if (c2 == '.') {
                    c2 = chars[++index2];
                    ++cursor;
                    ++end2;
                }
                final int nextVal2 = c2 - '0';
                result2 = 10 * result2 + nextVal2;
            }
            final int groupVal = result2;
            final long ylong = 1000000000L;
            long product = 0L;
            long carry = 0L;
            for (int i = 3; i >= 0; --i) {
                switch (i) {
                    case 0: {
                        product = ylong * ((long)this.mag0 & 0xFFFFFFFFL) + carry;
                        this.mag0 = (int)product;
                        break;
                    }
                    case 1: {
                        product = ylong * ((long)this.mag1 & 0xFFFFFFFFL) + carry;
                        this.mag1 = (int)product;
                        break;
                    }
                    case 2: {
                        product = ylong * ((long)this.mag2 & 0xFFFFFFFFL) + carry;
                        this.mag2 = (int)product;
                        break;
                    }
                    case 3: {
                        product = ylong * ((long)this.mag3 & 0xFFFFFFFFL) + carry;
                        this.mag3 = (int)product;
                        break;
                    }
                    default: {
                        throw new ArithmeticException("BigInteger would overflow supported range");
                    }
                }
                carry = product >>> 32;
            }
            final long zlong = (long)groupVal & 0xFFFFFFFFL;
            long sum = ((long)this.mag3 & 0xFFFFFFFFL) + zlong;
            this.mag3 = (int)sum;
            carry = sum >>> 32;
            for (int j = 2; j >= 0; --j) {
                switch (j) {
                    case 0: {
                        sum = ((long)this.mag0 & 0xFFFFFFFFL) + carry;
                        this.mag0 = (int)sum;
                        break;
                    }
                    case 1: {
                        sum = ((long)this.mag1 & 0xFFFFFFFFL) + carry;
                        this.mag1 = (int)sum;
                        break;
                    }
                    case 2: {
                        sum = ((long)this.mag2 & 0xFFFFFFFFL) + carry;
                        this.mag2 = (int)sum;
                        break;
                    }
                    case 3: {
                        sum = ((long)this.mag3 & 0xFFFFFFFFL) + carry;
                        this.mag3 = (int)sum;
                        break;
                    }
                    default: {
                        throw new ArithmeticException("BigInteger would overflow supported range");
                    }
                }
                carry = sum >>> 32;
            }
        }
    }
    
    final void bigInt(final byte[] chars, final int off, final int len) {
        int cursor = off;
        int numDigits = len - cursor;
        if (this.scale > 0) {
            --numDigits;
        }
        if (numDigits > 38) {
            throw new JSONException("number too large : " + new String(chars, off, numDigits));
        }
        int firstGroupLen = numDigits % 9;
        if (firstGroupLen == 0) {
            firstGroupLen = 9;
        }
        int start = cursor;
        int end;
        cursor = (end = cursor + firstGroupLen);
        char c = (char)chars[start++];
        if (c == '.') {
            c = (char)chars[start++];
            ++cursor;
        }
        int result = c - '0';
        for (int index = start; index < end; ++index) {
            c = (char)chars[index];
            if (c == '.') {
                c = (char)chars[++index];
                ++cursor;
                if (end < len) {
                    ++end;
                }
            }
            final int nextVal = c - '0';
            result = 10 * result + nextVal;
        }
        this.mag3 = result;
        while (cursor < len) {
            int start2 = cursor;
            cursor += 9;
            int end2 = cursor;
            char c2 = (char)chars[start2++];
            if (c2 == '.') {
                c2 = (char)chars[start2++];
                ++cursor;
                ++end2;
            }
            int result2 = c2 - '0';
            for (int index2 = start2; index2 < end2; ++index2) {
                c2 = (char)chars[index2];
                if (c2 == '.') {
                    c2 = (char)chars[++index2];
                    ++cursor;
                    ++end2;
                }
                final int nextVal2 = c2 - '0';
                result2 = 10 * result2 + nextVal2;
            }
            final int groupVal = result2;
            final long ylong = 1000000000L;
            final long zlong = (long)groupVal & 0xFFFFFFFFL;
            long product = 0L;
            long carry = 0L;
            for (int i = 3; i >= 0; --i) {
                switch (i) {
                    case 0: {
                        product = ylong * ((long)this.mag0 & 0xFFFFFFFFL) + carry;
                        this.mag0 = (int)product;
                        break;
                    }
                    case 1: {
                        product = ylong * ((long)this.mag1 & 0xFFFFFFFFL) + carry;
                        this.mag1 = (int)product;
                        break;
                    }
                    case 2: {
                        product = ylong * ((long)this.mag2 & 0xFFFFFFFFL) + carry;
                        this.mag2 = (int)product;
                        break;
                    }
                    case 3: {
                        product = ylong * ((long)this.mag3 & 0xFFFFFFFFL) + carry;
                        this.mag3 = (int)product;
                        break;
                    }
                    default: {
                        throw new ArithmeticException("BigInteger would overflow supported range");
                    }
                }
                carry = product >>> 32;
            }
            long sum = ((long)this.mag3 & 0xFFFFFFFFL) + zlong;
            this.mag3 = (int)sum;
            carry = sum >>> 32;
            for (int j = 2; j >= 0; --j) {
                switch (j) {
                    case 0: {
                        sum = ((long)this.mag0 & 0xFFFFFFFFL) + carry;
                        this.mag0 = (int)sum;
                        break;
                    }
                    case 1: {
                        sum = ((long)this.mag1 & 0xFFFFFFFFL) + carry;
                        this.mag1 = (int)sum;
                        break;
                    }
                    case 2: {
                        sum = ((long)this.mag2 & 0xFFFFFFFFL) + carry;
                        this.mag2 = (int)sum;
                        break;
                    }
                    case 3: {
                        sum = ((long)this.mag3 & 0xFFFFFFFFL) + carry;
                        this.mag3 = (int)sum;
                        break;
                    }
                    default: {
                        throw new ArithmeticException("BigInteger would overflow supported range");
                    }
                }
                carry = sum >>> 32;
            }
        }
    }
    
    public static AutoTypeBeforeHandler autoTypeFilter(final String... names) {
        return new ContextAutoTypeBeforeHandler(names);
    }
    
    public static AutoTypeBeforeHandler autoTypeFilter(final boolean includeBasic, final String... names) {
        return new ContextAutoTypeBeforeHandler(includeBasic, names);
    }
    
    public static AutoTypeBeforeHandler autoTypeFilter(final Class... types) {
        return new ContextAutoTypeBeforeHandler(types);
    }
    
    public static AutoTypeBeforeHandler autoTypeFilter(final boolean includeBasic, final Class... types) {
        return new ContextAutoTypeBeforeHandler(includeBasic, types);
    }
    
    public SavePoint mark() {
        return new SavePoint(this.offset, this.ch);
    }
    
    public void reset(final SavePoint savePoint) {
        this.offset = savePoint.offset;
        this.ch = (char)savePoint.current;
    }
    
    public final String info() {
        return this.info(null);
    }
    
    public String info(final String message) {
        if (message == null || message.isEmpty()) {
            return "offset " + this.offset;
        }
        return message + ", offset " + this.offset;
    }
    
    static boolean isFirstIdentifier(final char ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || ch == '_' || ch == '$' || (ch >= '0' && ch <= '9') || ch > '\u007f';
    }
    
    public ObjectReader getObjectReaderAutoType(final long typeHash, final Class expectClass, final long features) {
        final ObjectReader autoTypeObjectReader = this.context.getObjectReaderAutoType(typeHash);
        if (autoTypeObjectReader != null) {
            return autoTypeObjectReader;
        }
        final String typeName = this.getString();
        if (this.context.autoTypeBeforeHandler != null) {
            final Class<?> autoTypeClass = this.context.autoTypeBeforeHandler.apply(typeName, expectClass, features);
            if (autoTypeClass != null) {
                final boolean fieldBased = (features & Feature.FieldBased.mask) != 0x0L;
                return this.context.provider.getObjectReader(autoTypeClass, fieldBased);
            }
        }
        return this.context.provider.getObjectReader(typeName, expectClass, this.context.features | features);
    }
    
    public interface AutoTypeBeforeHandler extends Filter
    {
        default Class<?> apply(final long typeNameHash, final Class<?> expectClass, final long features) {
            return null;
        }
        
        Class<?> apply(final String p0, final Class<?> p1, final long p2);
    }
    
    public static final class Context
    {
        String dateFormat;
        boolean formatyyyyMMddhhmmss19;
        boolean formatyyyyMMddhhmmssT19;
        boolean yyyyMMddhhmm16;
        boolean formatyyyyMMdd8;
        boolean formatMillis;
        boolean formatUnixTime;
        boolean formatISO8601;
        boolean formatHasDay;
        boolean formatHasHour;
        boolean useSimpleFormatter;
        int maxLevel;
        int bufferSize;
        DateTimeFormatter dateFormatter;
        ZoneId zoneId;
        long features;
        Locale locale;
        TimeZone timeZone;
        Supplier<Map> objectSupplier;
        Supplier<List> arraySupplier;
        AutoTypeBeforeHandler autoTypeBeforeHandler;
        ExtraProcessor extraProcessor;
        final ObjectReaderProvider provider;
        final SymbolTable symbolTable;
        
        public Context(final ObjectReaderProvider provider) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = provider;
            this.objectSupplier = JSONFactory.defaultObjectSupplier;
            this.arraySupplier = JSONFactory.defaultArraySupplier;
            this.symbolTable = null;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            final String format = JSONFactory.defaultReaderFormat;
            if (format != null) {
                this.setDateFormat(format);
            }
        }
        
        public Context(final ObjectReaderProvider provider, final long features) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = features;
            this.provider = provider;
            this.objectSupplier = JSONFactory.defaultObjectSupplier;
            this.arraySupplier = JSONFactory.defaultArraySupplier;
            this.symbolTable = null;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            final String format = JSONFactory.defaultReaderFormat;
            if (format != null) {
                this.setDateFormat(format);
            }
        }
        
        public Context(final Feature... features) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = JSONFactory.getDefaultObjectReaderProvider();
            this.objectSupplier = JSONFactory.defaultObjectSupplier;
            this.arraySupplier = JSONFactory.defaultArraySupplier;
            this.symbolTable = null;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            final String format = JSONFactory.defaultReaderFormat;
            if (format != null) {
                this.setDateFormat(format);
            }
            for (final Feature feature : features) {
                this.features |= feature.mask;
            }
        }
        
        public Context(final String dateFormat, final Feature... features) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = JSONFactory.getDefaultObjectReaderProvider();
            this.objectSupplier = JSONFactory.defaultObjectSupplier;
            this.arraySupplier = JSONFactory.defaultArraySupplier;
            this.symbolTable = null;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            final String format = JSONFactory.defaultReaderFormat;
            if (format != null) {
                this.setDateFormat(format);
            }
            for (final Feature feature : features) {
                this.features |= feature.mask;
            }
            this.setDateFormat(dateFormat);
        }
        
        public Context(final ObjectReaderProvider provider, final Feature... features) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = provider;
            this.objectSupplier = JSONFactory.defaultObjectSupplier;
            this.arraySupplier = JSONFactory.defaultArraySupplier;
            this.symbolTable = null;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            final String format = JSONFactory.defaultReaderFormat;
            if (format != null) {
                this.setDateFormat(format);
            }
            for (final Feature feature : features) {
                this.features |= feature.mask;
            }
        }
        
        public Context(final ObjectReaderProvider provider, final Filter filter, final Feature... features) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = provider;
            this.objectSupplier = JSONFactory.defaultObjectSupplier;
            this.arraySupplier = JSONFactory.defaultArraySupplier;
            this.symbolTable = null;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            this.config(filter);
            final String format = JSONFactory.defaultReaderFormat;
            if (format != null) {
                this.setDateFormat(format);
            }
            for (final Feature feature : features) {
                this.features |= feature.mask;
            }
        }
        
        public Context(final ObjectReaderProvider provider, final SymbolTable symbolTable) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = provider;
            this.symbolTable = symbolTable;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            final String format = JSONFactory.defaultReaderFormat;
            if (format != null) {
                this.setDateFormat(format);
            }
        }
        
        public Context(final ObjectReaderProvider provider, final SymbolTable symbolTable, final Feature... features) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = provider;
            this.symbolTable = symbolTable;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            final String format = JSONFactory.defaultReaderFormat;
            if (format != null) {
                this.setDateFormat(format);
            }
            for (final Feature feature : features) {
                this.features |= feature.mask;
            }
        }
        
        public Context(final ObjectReaderProvider provider, final SymbolTable symbolTable, final Filter[] filters, final Feature... features) {
            this.maxLevel = 2048;
            this.bufferSize = 524288;
            this.features = JSONFactory.defaultReaderFeatures;
            this.provider = provider;
            this.symbolTable = symbolTable;
            this.zoneId = JSONFactory.defaultReaderZoneId;
            this.config(filters);
            final String format = JSONFactory.defaultReaderFormat;
            if (format != null) {
                this.setDateFormat(format);
            }
            for (final Feature feature : features) {
                this.features |= feature.mask;
            }
        }
        
        public boolean isFormatUnixTime() {
            return this.formatUnixTime;
        }
        
        public boolean isFormatyyyyMMddhhmmss19() {
            return this.formatyyyyMMddhhmmss19;
        }
        
        public boolean isFormatyyyyMMddhhmmssT19() {
            return this.formatyyyyMMddhhmmssT19;
        }
        
        public boolean isFormatyyyyMMdd8() {
            return this.formatyyyyMMdd8;
        }
        
        public boolean isFormatMillis() {
            return this.formatMillis;
        }
        
        public boolean isFormatISO8601() {
            return this.formatISO8601;
        }
        
        public boolean isFormatHasHour() {
            return this.formatHasHour;
        }
        
        public ObjectReader getObjectReader(final Type type) {
            final boolean fieldBased = (this.features & Feature.FieldBased.mask) != 0x0L;
            return this.provider.getObjectReader(type, fieldBased);
        }
        
        public ObjectReaderProvider getProvider() {
            return this.provider;
        }
        
        public ObjectReader getObjectReaderAutoType(final long hashCode) {
            return this.provider.getObjectReader(hashCode);
        }
        
        public ObjectReader getObjectReaderAutoType(final String typeName, final Class expectClass) {
            if (this.autoTypeBeforeHandler != null) {
                final Class<?> autoTypeClass = this.autoTypeBeforeHandler.apply(typeName, expectClass, this.features);
                if (autoTypeClass != null) {
                    final boolean fieldBased = (this.features & Feature.FieldBased.mask) != 0x0L;
                    return this.provider.getObjectReader(autoTypeClass, fieldBased);
                }
            }
            return this.provider.getObjectReader(typeName, expectClass, this.features);
        }
        
        public AutoTypeBeforeHandler getContextAutoTypeBeforeHandler() {
            return this.autoTypeBeforeHandler;
        }
        
        public ObjectReader getObjectReaderAutoType(final String typeName, final Class expectClass, final long features) {
            if (this.autoTypeBeforeHandler != null) {
                final Class<?> autoTypeClass = this.autoTypeBeforeHandler.apply(typeName, expectClass, features);
                if (autoTypeClass != null) {
                    final boolean fieldBased = (features & Feature.FieldBased.mask) != 0x0L;
                    return this.provider.getObjectReader(autoTypeClass, fieldBased);
                }
            }
            return this.provider.getObjectReader(typeName, expectClass, this.features | features);
        }
        
        public ExtraProcessor getExtraProcessor() {
            return this.extraProcessor;
        }
        
        public void setExtraProcessor(final ExtraProcessor extraProcessor) {
            this.extraProcessor = extraProcessor;
        }
        
        public Supplier<Map> getObjectSupplier() {
            return this.objectSupplier;
        }
        
        public void setObjectSupplier(final Supplier<Map> objectSupplier) {
            this.objectSupplier = objectSupplier;
        }
        
        public Supplier<List> getArraySupplier() {
            return this.arraySupplier;
        }
        
        public void setArraySupplier(final Supplier<List> arraySupplier) {
            this.arraySupplier = arraySupplier;
        }
        
        public DateTimeFormatter getDateFormatter() {
            if (this.dateFormatter == null && this.dateFormat != null && !this.formatMillis && !this.formatISO8601 && !this.formatUnixTime) {
                this.dateFormatter = ((this.locale == null) ? DateTimeFormatter.ofPattern(this.dateFormat) : DateTimeFormatter.ofPattern(this.dateFormat, this.locale));
            }
            return this.dateFormatter;
        }
        
        public void setDateFormatter(final DateTimeFormatter dateFormatter) {
            this.dateFormatter = dateFormatter;
        }
        
        public String getDateFormat() {
            return this.dateFormat;
        }
        
        public void setDateFormat(String format) {
            if (format != null && format.isEmpty()) {
                format = null;
            }
            boolean formatUnixTime = false;
            boolean formatISO8601 = false;
            boolean formatMillis = false;
            boolean hasDay = false;
            boolean hasHour = false;
            boolean useSimpleFormatter = false;
            if (format != null) {
                final String s = format;
                switch (s) {
                    case "unixtime": {
                        formatUnixTime = true;
                        break;
                    }
                    case "iso8601": {
                        formatISO8601 = true;
                        break;
                    }
                    case "millis": {
                        formatMillis = true;
                        break;
                    }
                    case "yyyyMMddHHmmssSSSZ": {
                        useSimpleFormatter = true;
                    }
                    case "yyyy-MM-dd HH:mm:ss":
                    case "yyyy-MM-ddTHH:mm:ss": {
                        this.formatyyyyMMddhhmmss19 = true;
                        hasDay = true;
                        hasHour = true;
                        break;
                    }
                    case "yyyy-MM-dd'T'HH:mm:ss": {
                        this.formatyyyyMMddhhmmssT19 = true;
                        hasDay = true;
                        hasHour = true;
                        break;
                    }
                    case "yyyy-MM-dd": {
                        this.formatyyyyMMdd8 = true;
                        hasDay = true;
                        hasHour = false;
                        break;
                    }
                    case "yyyy-MM-dd HH:mm": {
                        this.yyyyMMddhhmm16 = true;
                        break;
                    }
                    default: {
                        hasDay = (format.indexOf(100) != -1);
                        hasHour = (format.indexOf(72) != -1 || format.indexOf(104) != -1 || format.indexOf(75) != -1 || format.indexOf(107) != -1);
                        break;
                    }
                }
            }
            if (!Objects.equals(this.dateFormat, format)) {
                this.dateFormatter = null;
            }
            this.dateFormat = format;
            this.formatUnixTime = formatUnixTime;
            this.formatMillis = formatMillis;
            this.formatISO8601 = formatISO8601;
            this.formatHasDay = hasDay;
            this.formatHasHour = hasHour;
            this.useSimpleFormatter = useSimpleFormatter;
        }
        
        public ZoneId getZoneId() {
            if (this.zoneId == null) {
                this.zoneId = DateUtils.DEFAULT_ZONE_ID;
            }
            return this.zoneId;
        }
        
        public long getFeatures() {
            return this.features;
        }
        
        public void setZoneId(final ZoneId zoneId) {
            this.zoneId = zoneId;
        }
        
        public int getMaxLevel() {
            return this.maxLevel;
        }
        
        public void setMaxLevel(final int maxLevel) {
            this.maxLevel = maxLevel;
        }
        
        public int getBufferSize() {
            return this.bufferSize;
        }
        
        public Context setBufferSize(final int bufferSize) {
            if (bufferSize < 0) {
                throw new IllegalArgumentException("buffer size can not be less than zero");
            }
            this.bufferSize = bufferSize;
            return this;
        }
        
        public Locale getLocale() {
            return this.locale;
        }
        
        public void setLocale(final Locale locale) {
            this.locale = locale;
        }
        
        public TimeZone getTimeZone() {
            return this.timeZone;
        }
        
        public void setTimeZone(final TimeZone timeZone) {
            this.timeZone = timeZone;
        }
        
        public void config(final Feature... features) {
            for (int i = 0; i < features.length; ++i) {
                this.features |= features[i].mask;
            }
        }
        
        public void config(final Filter filter, final Feature... features) {
            if (filter instanceof AutoTypeBeforeHandler) {
                this.autoTypeBeforeHandler = (AutoTypeBeforeHandler)filter;
            }
            if (filter instanceof ExtraProcessor) {
                this.extraProcessor = (ExtraProcessor)filter;
            }
            for (final Feature feature : features) {
                this.features |= feature.mask;
            }
        }
        
        public void config(final Filter filter) {
            if (filter instanceof AutoTypeBeforeHandler) {
                this.autoTypeBeforeHandler = (AutoTypeBeforeHandler)filter;
            }
            if (filter instanceof ExtraProcessor) {
                this.extraProcessor = (ExtraProcessor)filter;
            }
        }
        
        public void config(final Filter[] filters, final Feature... features) {
            for (final Filter filter : filters) {
                if (filter instanceof AutoTypeBeforeHandler) {
                    this.autoTypeBeforeHandler = (AutoTypeBeforeHandler)filter;
                }
                if (filter instanceof ExtraProcessor) {
                    this.extraProcessor = (ExtraProcessor)filter;
                }
            }
            for (final Feature feature : features) {
                this.features |= feature.mask;
            }
        }
        
        public void config(final Filter[] filters) {
            for (final Filter filter : filters) {
                if (filter instanceof AutoTypeBeforeHandler) {
                    this.autoTypeBeforeHandler = (AutoTypeBeforeHandler)filter;
                }
                if (filter instanceof ExtraProcessor) {
                    this.extraProcessor = (ExtraProcessor)filter;
                }
            }
        }
        
        public boolean isEnabled(final Feature feature) {
            return (this.features & feature.mask) != 0x0L;
        }
        
        public void config(final Feature feature, final boolean state) {
            if (state) {
                this.features |= feature.mask;
            }
            else {
                this.features &= ~feature.mask;
            }
        }
    }
    
    public enum Feature
    {
        FieldBased(1L), 
        IgnoreNoneSerializable(2L), 
        ErrorOnNoneSerializable(4L), 
        SupportArrayToBean(8L), 
        InitStringFieldAsEmpty(16L), 
        @Deprecated
        SupportAutoType(32L), 
        SupportSmartMatch(64L), 
        UseNativeObject(128L), 
        SupportClassForName(256L), 
        IgnoreSetNullValue(512L), 
        UseDefaultConstructorAsPossible(1024L), 
        UseBigDecimalForFloats(2048L), 
        UseBigDecimalForDoubles(4096L), 
        ErrorOnEnumNotMatch(8192L), 
        TrimString(16384L), 
        ErrorOnNotSupportAutoType(32768L), 
        DuplicateKeyValueAsArray(65536L), 
        AllowUnQuotedFieldNames(131072L), 
        NonStringKeyAsString(262144L), 
        Base64StringAsByteArray(524288L), 
        IgnoreCheckClose(1048576L), 
        ErrorOnNullForPrimitives(2097152L), 
        NullOnError(4194304L), 
        IgnoreAutoTypeNotMatch(8388608L), 
        NonZeroNumberCastToBooleanAsTrue(16777216L);
        
        public final long mask;
        
        private Feature(final long mask) {
            this.mask = mask;
        }
        
        public static long of(final Feature[] features) {
            if (features == null) {
                return 0L;
            }
            long value = 0L;
            for (final Feature feature : features) {
                value |= feature.mask;
            }
            return value;
        }
        
        private static /* synthetic */ Feature[] $values() {
            return new Feature[] { Feature.FieldBased, Feature.IgnoreNoneSerializable, Feature.ErrorOnNoneSerializable, Feature.SupportArrayToBean, Feature.InitStringFieldAsEmpty, Feature.SupportAutoType, Feature.SupportSmartMatch, Feature.UseNativeObject, Feature.SupportClassForName, Feature.IgnoreSetNullValue, Feature.UseDefaultConstructorAsPossible, Feature.UseBigDecimalForFloats, Feature.UseBigDecimalForDoubles, Feature.ErrorOnEnumNotMatch, Feature.TrimString, Feature.ErrorOnNotSupportAutoType, Feature.DuplicateKeyValueAsArray, Feature.AllowUnQuotedFieldNames, Feature.NonStringKeyAsString, Feature.Base64StringAsByteArray, Feature.IgnoreCheckClose, Feature.ErrorOnNullForPrimitives, Feature.NullOnError, Feature.IgnoreAutoTypeNotMatch, Feature.NonZeroNumberCastToBooleanAsTrue };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    static class ResolveTask
    {
        final FieldReader fieldReader;
        final Object object;
        final Object name;
        final JSONPath reference;
        
        ResolveTask(final FieldReader fieldReader, final Object object, final Object name, final JSONPath reference) {
            this.fieldReader = fieldReader;
            this.object = object;
            this.name = name;
            this.reference = reference;
        }
        
        @Override
        public String toString() {
            return this.reference.toString();
        }
    }
    
    public static class SavePoint
    {
        protected final int offset;
        protected final int current;
        
        protected SavePoint(final int offset, final int current) {
            this.offset = offset;
            this.current = current;
        }
    }
    
    static final class BigIntegerCreator implements BiFunction<Integer, int[], BigInteger>
    {
        static final BiFunction<Integer, int[], BigInteger> BIG_INTEGER_CREATOR;
        
        @Override
        public BigInteger apply(final Integer integer, final int[] mag) {
            final int signum = integer;
            int bitLength;
            if (mag.length == 0) {
                bitLength = 0;
            }
            else {
                final int bitLengthForInt = 32 - Integer.numberOfLeadingZeros(mag[0]);
                final int magBitLength = (mag.length - 1 << 5) + bitLengthForInt;
                if (signum < 0) {
                    boolean pow2 = Integer.bitCount(mag[0]) == 1;
                    for (int i = 1; i < mag.length && pow2; pow2 = (mag[i] == 0), ++i) {}
                    bitLength = (pow2 ? (magBitLength - 1) : magBitLength);
                }
                else {
                    bitLength = magBitLength;
                }
            }
            final int byteLen = bitLength / 8 + 1;
            final byte[] bytes = new byte[byteLen];
            int j = byteLen - 1;
            int bytesCopied = 4;
            int nextInt = 0;
            int intIndex = 0;
            while (j >= 0) {
                if (bytesCopied == 4) {
                    final int n = intIndex++;
                    if (n < 0) {
                        nextInt = 0;
                    }
                    else if (n >= mag.length) {
                        nextInt = ((signum < 0) ? -1 : 0);
                    }
                    else {
                        final int magInt = mag[mag.length - n - 1];
                        if (signum >= 0) {
                            nextInt = magInt;
                        }
                        else {
                            final int mlen = mag.length;
                            int k;
                            for (k = mlen - 1; k >= 0 && mag[k] == 0; --k) {}
                            final int firstNonzeroIntNum = mlen - k - 1;
                            if (n <= firstNonzeroIntNum) {
                                nextInt = -magInt;
                            }
                            else {
                                nextInt = ~magInt;
                            }
                        }
                    }
                    bytesCopied = 1;
                }
                else {
                    nextInt >>>= 8;
                    ++bytesCopied;
                }
                bytes[j] = (byte)nextInt;
                --j;
            }
            return new BigInteger(bytes);
        }
        
        static {
            BiFunction<Integer, int[], BigInteger> bigIntegerCreator = null;
            if (!JDKUtils.ANDROID && !JDKUtils.GRAAL) {
                try {
                    final MethodHandles.Lookup caller = JDKUtils.trustedLookup(BigInteger.class);
                    final MethodHandle handle = caller.findConstructor(BigInteger.class, MethodType.methodType(Void.TYPE, Integer.TYPE, int[].class));
                    final CallSite callSite = LambdaMetafactory.metafactory(caller, "apply", MethodType.methodType(BiFunction.class), handle.type().generic(), handle, MethodType.methodType(BigInteger.class, Integer.class, int[].class));
                    bigIntegerCreator = (BiFunction<Integer, int[], BigInteger>)callSite.getTarget().invokeExact();
                }
                catch (Throwable t) {}
            }
            if (bigIntegerCreator == null) {
                bigIntegerCreator = new BigIntegerCreator();
            }
            BIG_INTEGER_CREATOR = bigIntegerCreator;
        }
    }
}
