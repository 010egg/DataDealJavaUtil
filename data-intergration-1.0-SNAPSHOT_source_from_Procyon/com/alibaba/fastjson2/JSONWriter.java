// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import com.alibaba.fastjson2.util.IOUtils;
import java.util.Arrays;
import java.util.Objects;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.filter.ContextNameFilter;
import com.alibaba.fastjson2.filter.ContextValueFilter;
import com.alibaba.fastjson2.filter.LabelFilter;
import com.alibaba.fastjson2.filter.AfterFilter;
import com.alibaba.fastjson2.filter.BeforeFilter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.PropertyFilter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;
import java.util.Locale;
import java.time.ZoneId;
import java.io.OutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.io.Reader;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.UUID;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.List;
import java.text.DecimalFormat;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.util.AbstractMap;
import java.util.Map;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import com.alibaba.fastjson2.writer.FieldWriter;
import java.util.Collections;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.IdentityHashMap;
import java.nio.charset.Charset;
import java.io.Closeable;

public abstract class JSONWriter implements Closeable
{
    static final long WRITE_ARRAY_NULL_MASK;
    static final char[] DIGITS;
    public final Context context;
    public final boolean utf8;
    public final boolean utf16;
    public final boolean jsonb;
    public final boolean useSingleQuote;
    public final SymbolTable symbolTable;
    protected final Charset charset;
    protected final char quote;
    protected final int maxArraySize;
    protected boolean startObject;
    protected int level;
    protected int off;
    protected Object rootObject;
    protected IdentityHashMap<Object, Path> refs;
    protected Path path;
    protected String lastReference;
    protected boolean pretty;
    protected int indent;
    
    protected JSONWriter(final Context context, final SymbolTable symbolTable, final boolean jsonb, final Charset charset) {
        this.context = context;
        this.symbolTable = symbolTable;
        this.charset = charset;
        this.jsonb = jsonb;
        this.utf8 = (!jsonb && charset == StandardCharsets.UTF_8);
        this.utf16 = (!jsonb && charset == StandardCharsets.UTF_16);
        this.useSingleQuote = (!jsonb && (context.features & Feature.UseSingleQuotes.mask) != 0x0L);
        this.quote = (this.useSingleQuote ? '\'' : '\"');
        this.maxArraySize = (((context.features & Feature.LargeObject.mask) != 0x0L) ? 1073741824 : 67108864);
        this.pretty = ((context.features & Feature.PrettyFormat.mask) != 0x0L);
    }
    
    public final Charset getCharset() {
        return this.charset;
    }
    
    public final boolean isUTF8() {
        return this.utf8;
    }
    
    public final boolean isUTF16() {
        return this.utf16;
    }
    
    public final boolean isIgnoreNoneSerializable() {
        return (this.context.features & Feature.IgnoreNoneSerializable.mask) != 0x0L;
    }
    
    public final boolean isIgnoreNoneSerializable(final Object object) {
        return (this.context.features & Feature.IgnoreNoneSerializable.mask) != 0x0L && object != null && !Serializable.class.isAssignableFrom(object.getClass());
    }
    
    public final SymbolTable getSymbolTable() {
        return this.symbolTable;
    }
    
    public final void config(final Feature... features) {
        this.context.config(features);
    }
    
    public final void config(final Feature feature, final boolean state) {
        this.context.config(feature, state);
    }
    
    public final Context getContext() {
        return this.context;
    }
    
    public final int level() {
        return this.level;
    }
    
    public final void setRootObject(final Object rootObject) {
        this.rootObject = rootObject;
        this.path = Path.ROOT;
    }
    
    public final String setPath(final String name, final Object object) {
        if ((this.context.features & Feature.ReferenceDetection.mask) == 0x0L || object == Collections.EMPTY_LIST || object == Collections.EMPTY_SET) {
            return null;
        }
        this.path = new Path(this.path, name);
        Path previous;
        if (object == this.rootObject) {
            previous = Path.ROOT;
        }
        else if (this.refs == null || (previous = this.refs.get(object)) == null) {
            if (this.refs == null) {
                this.refs = new IdentityHashMap<Object, Path>(8);
            }
            this.refs.put(object, this.path);
            return null;
        }
        return previous.toString();
    }
    
    public final String setPath(final FieldWriter fieldWriter, final Object object) {
        if ((this.context.features & Feature.ReferenceDetection.mask) == 0x0L || object == Collections.EMPTY_LIST || object == Collections.EMPTY_SET) {
            return null;
        }
        this.path = ((this.path == Path.ROOT) ? fieldWriter.getRootParentPath() : fieldWriter.getPath(this.path));
        Path previous;
        if (object == this.rootObject) {
            previous = Path.ROOT;
        }
        else if (this.refs == null || (previous = this.refs.get(object)) == null) {
            if (this.refs == null) {
                this.refs = new IdentityHashMap<Object, Path>(8);
            }
            this.refs.put(object, this.path);
            return null;
        }
        return previous.toString();
    }
    
    public final boolean writeReference(final int index, final Object object) {
        final String refPath = this.setPath(index, object);
        if (refPath != null) {
            this.writeReference(refPath);
            this.popPath(object);
            return true;
        }
        return false;
    }
    
    public final String setPath(final int index, final Object object) {
        if ((this.context.features & Feature.ReferenceDetection.mask) == 0x0L || object == Collections.EMPTY_LIST || object == Collections.EMPTY_SET) {
            return null;
        }
        this.path = ((index == 0) ? ((this.path.child0 != null) ? this.path.child0 : (this.path.child0 = new Path(this.path, index))) : ((index == 1) ? ((this.path.child1 != null) ? this.path.child1 : (this.path.child1 = new Path(this.path, index))) : new Path(this.path, index)));
        Path previous;
        if (object == this.rootObject) {
            previous = Path.ROOT;
        }
        else if (this.refs == null || (previous = this.refs.get(object)) == null) {
            if (this.refs == null) {
                this.refs = new IdentityHashMap<Object, Path>(8);
            }
            this.refs.put(object, this.path);
            return null;
        }
        return previous.toString();
    }
    
    public final void popPath(final Object object) {
        if (this.path == null || (this.context.features & Feature.ReferenceDetection.mask) == 0x0L || object == Collections.EMPTY_LIST || object == Collections.EMPTY_SET) {
            return;
        }
        this.path = this.path.parent;
    }
    
    public final boolean hasFilter() {
        return this.context.hasFilter;
    }
    
    public final boolean hasFilter(final long feature) {
        return this.context.hasFilter || (this.context.features & feature) != 0x0L;
    }
    
    public final boolean hasFilter(final boolean containsNoneFieldGetter) {
        return this.context.hasFilter || (containsNoneFieldGetter && (this.context.features & Feature.IgnoreNonFieldGetter.mask) != 0x0L);
    }
    
    public final boolean isWriteNulls() {
        return (this.context.features & Feature.WriteNulls.mask) != 0x0L;
    }
    
    public final boolean isRefDetect() {
        return (this.context.features & Feature.ReferenceDetection.mask) != 0x0L;
    }
    
    public final boolean isUseSingleQuotes() {
        return this.useSingleQuote;
    }
    
    public final boolean isRefDetect(final Object object) {
        return (this.context.features & Feature.ReferenceDetection.mask) != 0x0L && object != null && !ObjectWriterProvider.isNotReferenceDetect(object.getClass());
    }
    
    public final boolean containsReference(final Object value) {
        return this.refs != null && this.refs.containsKey(value);
    }
    
    public final boolean removeReference(final Object value) {
        return this.refs != null && this.refs.remove(value) != null;
    }
    
    public final boolean isBeanToArray() {
        return (this.context.features & Feature.BeanToArray.mask) != 0x0L;
    }
    
    public final boolean isEnabled(final Feature feature) {
        return (this.context.features & feature.mask) != 0x0L;
    }
    
    public final boolean isEnabled(final long feature) {
        return (this.context.features & feature) != 0x0L;
    }
    
    public final long getFeatures() {
        return this.context.features;
    }
    
    public final long getFeatures(final long features) {
        return this.context.features | features;
    }
    
    public final boolean isIgnoreErrorGetter() {
        return (this.context.features & Feature.IgnoreErrorGetter.mask) != 0x0L;
    }
    
    public final boolean isWriteTypeInfo(final Object object, final Class fieldClass) {
        final long features = this.context.features;
        if ((features & Feature.WriteClassName.mask) == 0x0L) {
            return false;
        }
        if (object == null) {
            return false;
        }
        final Class objectClass = object.getClass();
        return objectClass != fieldClass && ((features & Feature.NotWriteHashMapArrayListClassName.mask) == 0x0L || (objectClass != HashMap.class && objectClass != ArrayList.class)) && ((features & Feature.NotWriteRootClassName.mask) == 0x0L || object != this.rootObject);
    }
    
    public final boolean isWriteTypeInfo(final Object object, final Type fieldType) {
        final long features = this.context.features;
        if ((features & Feature.WriteClassName.mask) == 0x0L || object == null) {
            return false;
        }
        final Class objectClass = object.getClass();
        Class fieldClass = null;
        if (fieldType instanceof Class) {
            fieldClass = (Class)fieldType;
        }
        else if (fieldType instanceof GenericArrayType) {
            if (isWriteTypeInfoGenericArray((GenericArrayType)fieldType, objectClass)) {
                return false;
            }
        }
        else if (fieldType instanceof ParameterizedType) {
            final Type rawType = ((ParameterizedType)fieldType).getRawType();
            if (rawType instanceof Class) {
                fieldClass = (Class)rawType;
            }
        }
        return objectClass != fieldClass && ((features & Feature.NotWriteHashMapArrayListClassName.mask) == 0x0L || (objectClass != HashMap.class && objectClass != ArrayList.class)) && ((features & Feature.NotWriteRootClassName.mask) == 0x0L || object != this.rootObject);
    }
    
    private static boolean isWriteTypeInfoGenericArray(final GenericArrayType fieldType, final Class objectClass) {
        Type componentType = fieldType.getGenericComponentType();
        if (componentType instanceof ParameterizedType) {
            componentType = ((ParameterizedType)componentType).getRawType();
        }
        return objectClass.isArray() && objectClass.getComponentType().equals(componentType);
    }
    
    public final boolean isWriteTypeInfo(final Object object) {
        final long features = this.context.features;
        if ((features & Feature.WriteClassName.mask) == 0x0L) {
            return false;
        }
        if ((features & Feature.NotWriteHashMapArrayListClassName.mask) != 0x0L && object != null) {
            final Class objectClass = object.getClass();
            if (objectClass == HashMap.class || objectClass == ArrayList.class) {
                return false;
            }
        }
        return (features & Feature.NotWriteRootClassName.mask) == 0x0L || object != this.rootObject;
    }
    
    public final boolean isWriteTypeInfo(final Object object, final Type fieldType, long features) {
        features |= this.context.features;
        if ((features & Feature.WriteClassName.mask) == 0x0L) {
            return false;
        }
        if (object == null) {
            return false;
        }
        final Class objectClass = object.getClass();
        Class fieldClass = null;
        if (fieldType instanceof Class) {
            fieldClass = (Class)fieldType;
        }
        else if (fieldType instanceof ParameterizedType) {
            final Type rawType = ((ParameterizedType)fieldType).getRawType();
            if (rawType instanceof Class) {
                fieldClass = (Class)rawType;
            }
        }
        if (objectClass == fieldClass) {
            return false;
        }
        if ((features & Feature.NotWriteHashMapArrayListClassName.mask) != 0x0L) {
            if (objectClass == HashMap.class) {
                if (fieldClass == null || fieldClass == Object.class || fieldClass == Map.class || fieldClass == AbstractMap.class) {
                    return false;
                }
            }
            else if (objectClass == ArrayList.class) {
                return false;
            }
        }
        return (features & Feature.NotWriteRootClassName.mask) == 0x0L || object != this.rootObject;
    }
    
    public final boolean isWriteTypeInfo(final Object object, final Class fieldClass, long features) {
        if (object == null) {
            return false;
        }
        final Class objectClass = object.getClass();
        if (objectClass == fieldClass) {
            return false;
        }
        features |= this.context.features;
        if ((features & Feature.WriteClassName.mask) == 0x0L) {
            return false;
        }
        if ((features & Feature.NotWriteHashMapArrayListClassName.mask) != 0x0L) {
            if (objectClass == HashMap.class) {
                if (fieldClass == null || fieldClass == Object.class || fieldClass == Map.class || fieldClass == AbstractMap.class) {
                    return false;
                }
            }
            else if (objectClass == ArrayList.class) {
                return false;
            }
        }
        return (features & Feature.NotWriteRootClassName.mask) == 0x0L || object != this.rootObject;
    }
    
    public final boolean isWriteMapTypeInfo(final Object object, final Class fieldClass, long features) {
        if (object == null) {
            return false;
        }
        final Class objectClass = object.getClass();
        if (objectClass == fieldClass) {
            return false;
        }
        features |= this.context.features;
        return (features & Feature.WriteClassName.mask) != 0x0L && ((features & Feature.NotWriteHashMapArrayListClassName.mask) == 0x0L || objectClass != HashMap.class) && ((features & Feature.NotWriteRootClassName.mask) == 0x0L || object != this.rootObject);
    }
    
    public final boolean isWriteTypeInfo(final Object object, long features) {
        features |= this.context.features;
        if ((features & Feature.WriteClassName.mask) == 0x0L) {
            return false;
        }
        if ((features & Feature.NotWriteHashMapArrayListClassName.mask) != 0x0L && object != null) {
            final Class objectClass = object.getClass();
            if (objectClass == HashMap.class || objectClass == ArrayList.class) {
                return false;
            }
        }
        return (features & Feature.NotWriteRootClassName.mask) == 0x0L || object != this.rootObject;
    }
    
    public final ObjectWriter getObjectWriter(final Class objectClass) {
        final boolean fieldBased = (this.context.features & Feature.FieldBased.mask) != 0x0L;
        return this.context.provider.getObjectWriter(objectClass, objectClass, fieldBased);
    }
    
    public final ObjectWriter getObjectWriter(final Type objectType, final Class objectClass) {
        final boolean fieldBased = (this.context.features & Feature.FieldBased.mask) != 0x0L;
        return this.context.provider.getObjectWriter(objectType, objectClass, fieldBased);
    }
    
    public static JSONWriter of() {
        final Context writeContext = new Context(JSONFactory.defaultObjectWriterProvider);
        JSONWriter jsonWriter;
        if (JDKUtils.JVM_VERSION == 8) {
            if (JDKUtils.FIELD_STRING_VALUE != null && !JDKUtils.ANDROID && !JDKUtils.OPENJ9) {
                jsonWriter = new JSONWriterUTF16JDK8UF(writeContext);
            }
            else {
                jsonWriter = new JSONWriterUTF16JDK8(writeContext);
            }
        }
        else if ((JSONFactory.defaultWriterFeatures & Feature.OptimizedForAscii.mask) != 0x0L) {
            if (JDKUtils.STRING_VALUE != null) {
                if (JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF8 != null) {
                    jsonWriter = JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF8.apply(writeContext);
                }
                else {
                    jsonWriter = new JSONWriterUTF8JDK9(writeContext);
                }
            }
            else {
                jsonWriter = new JSONWriterUTF8(writeContext);
            }
        }
        else if (JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF16 != null) {
            jsonWriter = JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF16.apply(writeContext);
        }
        else if (JDKUtils.FIELD_STRING_VALUE != null && JDKUtils.STRING_CODER != null && JDKUtils.STRING_VALUE != null) {
            jsonWriter = new JSONWriterUTF16JDK9UF(writeContext);
        }
        else {
            jsonWriter = new JSONWriterUTF16(writeContext);
        }
        return jsonWriter;
    }
    
    public static JSONWriter of(final ObjectWriterProvider provider, final Feature... features) {
        final Context context = new Context(provider);
        context.config(features);
        return of(context);
    }
    
    public static JSONWriter of(Context context) {
        if (context == null) {
            context = JSONFactory.createWriteContext();
        }
        JSONWriter jsonWriter;
        if (JDKUtils.JVM_VERSION == 8) {
            if (JDKUtils.FIELD_STRING_VALUE != null && !JDKUtils.ANDROID && !JDKUtils.OPENJ9) {
                jsonWriter = new JSONWriterUTF16JDK8UF(context);
            }
            else {
                jsonWriter = new JSONWriterUTF16JDK8(context);
            }
        }
        else if ((context.features & Feature.OptimizedForAscii.mask) != 0x0L) {
            if (JDKUtils.STRING_VALUE != null) {
                if (JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF8 != null) {
                    jsonWriter = JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF8.apply(context);
                }
                else {
                    jsonWriter = new JSONWriterUTF8JDK9(context);
                }
            }
            else {
                jsonWriter = new JSONWriterUTF8(context);
            }
        }
        else if (JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF16 != null) {
            jsonWriter = JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF16.apply(context);
        }
        else {
            jsonWriter = new JSONWriterUTF16(context);
        }
        return jsonWriter;
    }
    
    public static JSONWriter of(final Feature... features) {
        final Context writeContext = JSONFactory.createWriteContext(features);
        JSONWriter jsonWriter;
        if (JDKUtils.JVM_VERSION == 8) {
            if (JDKUtils.FIELD_STRING_VALUE != null && !JDKUtils.ANDROID && !JDKUtils.OPENJ9) {
                jsonWriter = new JSONWriterUTF16JDK8UF(writeContext);
            }
            else {
                jsonWriter = new JSONWriterUTF16JDK8(writeContext);
            }
        }
        else if ((writeContext.features & Feature.OptimizedForAscii.mask) != 0x0L) {
            if (JDKUtils.STRING_VALUE != null) {
                if (JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF8 != null) {
                    jsonWriter = JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF8.apply(writeContext);
                }
                else {
                    jsonWriter = new JSONWriterUTF8JDK9(writeContext);
                }
            }
            else {
                jsonWriter = new JSONWriterUTF8(writeContext);
            }
        }
        else if (JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF16 != null) {
            jsonWriter = JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF16.apply(writeContext);
        }
        else {
            jsonWriter = new JSONWriterUTF16(writeContext);
        }
        return jsonWriter;
    }
    
    public static JSONWriter ofUTF16(final Feature... features) {
        final Context writeContext = JSONFactory.createWriteContext(features);
        JSONWriter jsonWriter;
        if (JDKUtils.JVM_VERSION == 8) {
            if (JDKUtils.FIELD_STRING_VALUE != null && !JDKUtils.ANDROID && !JDKUtils.OPENJ9) {
                jsonWriter = new JSONWriterUTF16JDK8UF(writeContext);
            }
            else {
                jsonWriter = new JSONWriterUTF16JDK8(writeContext);
            }
        }
        else if (JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF16 != null) {
            jsonWriter = JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF16.apply(writeContext);
        }
        else {
            jsonWriter = new JSONWriterUTF16(writeContext);
        }
        return jsonWriter;
    }
    
    public static JSONWriter ofJSONB() {
        return new JSONWriterJSONB(new Context(JSONFactory.defaultObjectWriterProvider), null);
    }
    
    public static JSONWriter ofJSONB(final Context context) {
        return new JSONWriterJSONB(context, null);
    }
    
    public static JSONWriter ofJSONB(final Context context, final SymbolTable symbolTable) {
        return new JSONWriterJSONB(context, symbolTable);
    }
    
    public static JSONWriter ofJSONB(final Feature... features) {
        return new JSONWriterJSONB(new Context(JSONFactory.defaultObjectWriterProvider, features), null);
    }
    
    public static JSONWriter ofJSONB(final SymbolTable symbolTable) {
        return new JSONWriterJSONB(new Context(JSONFactory.defaultObjectWriterProvider), symbolTable);
    }
    
    public static JSONWriter ofPretty() {
        return of(Feature.PrettyFormat);
    }
    
    public static JSONWriter ofPretty(final JSONWriter writer) {
        if (!writer.pretty) {
            writer.pretty = true;
            final Context context = writer.context;
            context.features |= Feature.PrettyFormat.mask;
        }
        return writer;
    }
    
    public static JSONWriter ofUTF8() {
        final Context context = JSONFactory.createWriteContext();
        JSONWriter jsonWriter;
        if (JDKUtils.STRING_VALUE != null) {
            if (JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF8 != null) {
                jsonWriter = JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF8.apply(context);
            }
            else {
                jsonWriter = new JSONWriterUTF8JDK9(context);
            }
        }
        else {
            jsonWriter = new JSONWriterUTF8(context);
        }
        return jsonWriter;
    }
    
    public static JSONWriter ofUTF8(final Context context) {
        JSONWriter jsonWriter;
        if (JDKUtils.STRING_VALUE != null) {
            if (JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF8 != null) {
                jsonWriter = JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF8.apply(context);
            }
            else {
                jsonWriter = new JSONWriterUTF8JDK9(context);
            }
        }
        else {
            jsonWriter = new JSONWriterUTF8(context);
        }
        return jsonWriter;
    }
    
    public static JSONWriter ofUTF8(final Feature... features) {
        final Context context = JSONFactory.createWriteContext(features);
        JSONWriter jsonWriter;
        if (JDKUtils.STRING_VALUE != null) {
            if (JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF8 != null) {
                jsonWriter = JSONFactory.INCUBATOR_VECTOR_WRITER_CREATOR_UTF8.apply(context);
            }
            else {
                jsonWriter = new JSONWriterUTF8JDK9(context);
            }
        }
        else {
            jsonWriter = new JSONWriterUTF8(context);
        }
        return jsonWriter;
    }
    
    public void writeBinary(final byte[] bytes) {
        if (bytes == null) {
            this.writeArrayNull();
            return;
        }
        if ((this.context.features & Feature.WriteByteArrayAsBase64.mask) != 0x0L) {
            this.writeBase64(bytes);
            return;
        }
        this.startArray();
        for (int i = 0; i < bytes.length; ++i) {
            if (i != 0) {
                this.writeComma();
            }
            this.writeInt32(bytes[i]);
        }
        this.endArray();
    }
    
    public abstract void writeBase64(final byte[] p0);
    
    public abstract void writeHex(final byte[] p0);
    
    protected abstract void write0(final char p0);
    
    public abstract void writeRaw(final String p0);
    
    public abstract void writeRaw(final byte[] p0);
    
    public void writeRaw(final byte b) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public void writeNameRaw(final byte[] bytes, final int offset, final int len) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public final void writeRaw(final char[] chars) {
        this.writeRaw(chars, 0, chars.length);
    }
    
    public void writeRaw(final char[] chars, final int off, final int charslen) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public abstract void writeChar(final char p0);
    
    public abstract void writeRaw(final char p0);
    
    public void writeRaw(final char c0, final char c1) {
        this.writeRaw(c0);
        this.writeRaw(c1);
    }
    
    public abstract void writeNameRaw(final byte[] p0);
    
    public void writeName3Raw(final long name) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public void writeName4Raw(final long name) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public void writeName5Raw(final long name) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public void writeName6Raw(final long name) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public void writeName7Raw(final long name) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public void writeName8Raw(final long name0) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public void writeName9Raw(final long name0, final int name1) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public void writeSymbol(final int symbol) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public void writeNameRaw(final byte[] name, final long nameHash) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public abstract void writeNameRaw(final char[] p0);
    
    public abstract void writeNameRaw(final char[] p0, final int p1, final int p2);
    
    public boolean xxxbac() {
        return this.utf8 && (this.context.features & 0x165EL) == 0x0L;
    }
    
    public void writeName(final String name) {
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            this.writeComma();
        }
        boolean unquote = (this.context.features & Feature.UnquoteFieldName.mask) != 0x0L;
        if (unquote && (name.indexOf(this.quote) >= 0 || name.indexOf(92) >= 0)) {
            unquote = false;
        }
        if (unquote) {
            this.writeRaw(name);
            return;
        }
        this.writeString(name);
    }
    
    public final void writeName(final long name) {
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            this.writeComma();
        }
        this.writeInt64(name);
    }
    
    public final void writeName(final int name) {
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            this.writeComma();
        }
        this.writeInt32(name);
    }
    
    public void writeNameAny(final Object name) {
        if (this.startObject) {
            this.startObject = false;
        }
        else {
            this.writeComma();
        }
        this.writeAny(name);
    }
    
    public abstract void startObject();
    
    public abstract void endObject();
    
    public abstract void startArray();
    
    public void startArray(final int size) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public void startArray(final Object array, final int size) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public abstract void endArray();
    
    public abstract void writeComma();
    
    public abstract void writeColon();
    
    public void writeInt16(final short[] value) {
        if (value == null) {
            this.writeArrayNull();
            return;
        }
        this.startArray();
        for (int i = 0; i < value.length; ++i) {
            if (i != 0) {
                this.writeComma();
            }
            this.writeInt16(value[i]);
        }
        this.endArray();
    }
    
    public abstract void writeInt8(final byte p0);
    
    public abstract void writeInt16(final short p0);
    
    public abstract void writeInt32(final int[] p0);
    
    public abstract void writeInt32(final int p0);
    
    public abstract void writeInt32(final Integer p0);
    
    public final void writeInt32(final int value, final DecimalFormat format) {
        if (format == null || this.jsonb) {
            this.writeInt32(value);
            return;
        }
        this.writeString(format.format(value));
    }
    
    public final void writeInt32(final int value, final String format) {
        if (format == null || this.jsonb) {
            this.writeInt32(value);
            return;
        }
        this.writeString(String.format(format, value));
    }
    
    public abstract void writeInt64(final long p0);
    
    public abstract void writeInt64(final Long p0);
    
    public void writeMillis(final long i) {
        this.writeInt64(i);
    }
    
    public abstract void writeInt64(final long[] p0);
    
    public abstract void writeListInt64(final List<Long> p0);
    
    public abstract void writeListInt32(final List<Integer> p0);
    
    public abstract void writeFloat(final float p0);
    
    public final void writeFloat(final float value, final DecimalFormat format) {
        if (format == null || this.jsonb) {
            this.writeFloat(value);
            return;
        }
        if (Float.isNaN(value) || Float.isInfinite(value)) {
            this.writeNull();
            return;
        }
        final String str = format.format(value);
        this.writeRaw(str);
    }
    
    public void writeFloat(final float[] value) {
        if (value == null) {
            this.writeNull();
            return;
        }
        this.startArray();
        for (int i = 0; i < value.length; ++i) {
            if (i != 0) {
                this.writeComma();
            }
            this.writeFloat(value[i]);
        }
        this.endArray();
    }
    
    public final void writeFloat(final float[] value, final DecimalFormat format) {
        if (format == null || this.jsonb) {
            this.writeFloat(value);
        }
        if (value == null) {
            this.writeNull();
            return;
        }
        this.startArray();
        for (int i = 0; i < value.length; ++i) {
            if (i != 0) {
                this.writeComma();
            }
            final String str = format.format(value[i]);
            this.writeRaw(str);
        }
        this.endArray();
    }
    
    public final void writeFloat(final Float value) {
        if (value == null) {
            this.writeNumberNull();
        }
        else {
            this.writeDouble(value);
        }
    }
    
    public abstract void writeDouble(final double p0);
    
    public final void writeDouble(final double value, final DecimalFormat format) {
        if (format == null || this.jsonb) {
            this.writeDouble(value);
            return;
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            this.writeNull();
            return;
        }
        final String str = format.format(value);
        this.writeRaw(str);
    }
    
    public void writeDoubleArray(final double value0, final double value1) {
        this.startArray();
        this.writeDouble(value0);
        this.writeComma();
        this.writeDouble(value1);
        this.endArray();
    }
    
    public final void writeDouble(final double[] value, final DecimalFormat format) {
        if (format == null || this.jsonb) {
            this.writeDouble(value);
            return;
        }
        if (value == null) {
            this.writeNull();
            return;
        }
        this.startArray();
        for (int i = 0; i < value.length; ++i) {
            if (i != 0) {
                this.writeComma();
            }
            final String str = format.format(value[i]);
            this.writeRaw(str);
        }
        this.endArray();
    }
    
    public abstract void writeDouble(final double[] p0);
    
    public abstract void writeBool(final boolean p0);
    
    public void writeBool(final boolean[] value) {
        if (value == null) {
            this.writeArrayNull();
            return;
        }
        this.startArray();
        for (int i = 0; i < value.length; ++i) {
            if (i != 0) {
                this.writeComma();
            }
            this.writeBool(value[i]);
        }
        this.endArray();
    }
    
    public abstract void writeNull();
    
    public void writeStringNull() {
        String raw;
        if ((this.context.features & (Feature.NullAsDefaultValue.mask | Feature.WriteNullStringAsEmpty.mask)) != 0x0L) {
            raw = (((this.context.features & Feature.UseSingleQuotes.mask) != 0x0L) ? "''" : "\"\"");
        }
        else {
            raw = "null";
        }
        this.writeRaw(raw);
    }
    
    public void writeArrayNull() {
        String raw;
        if ((this.context.features & (Feature.NullAsDefaultValue.mask | Feature.WriteNullListAsEmpty.mask)) != 0x0L) {
            raw = "[]";
        }
        else {
            raw = "null";
        }
        this.writeRaw(raw);
    }
    
    public final void writeNumberNull() {
        if ((this.context.features & (Feature.NullAsDefaultValue.mask | Feature.WriteNullNumberAsZero.mask)) != 0x0L) {
            this.writeInt32(0);
        }
        else {
            this.writeNull();
        }
    }
    
    public final void writeBooleanNull() {
        if ((this.context.features & (Feature.NullAsDefaultValue.mask | Feature.WriteNullBooleanAsFalse.mask)) != 0x0L) {
            this.writeBool(false);
        }
        else {
            this.writeNull();
        }
    }
    
    public final void writeDecimal(final BigDecimal value) {
        this.writeDecimal(value, 0L, null);
    }
    
    public final void writeDecimal(final BigDecimal value, final long features) {
        this.writeDecimal(value, features, null);
    }
    
    public abstract void writeDecimal(final BigDecimal p0, final long p1, final DecimalFormat p2);
    
    public void writeEnum(final Enum e) {
        if (e == null) {
            this.writeNull();
            return;
        }
        if ((this.context.features & Feature.WriteEnumUsingToString.mask) != 0x0L) {
            this.writeString(e.toString());
        }
        else if ((this.context.features & Feature.WriteEnumsUsingName.mask) != 0x0L) {
            this.writeString(e.name());
        }
        else {
            this.writeInt32(e.ordinal());
        }
    }
    
    public final void writeBigInt(final BigInteger value) {
        this.writeBigInt(value, 0L);
    }
    
    public abstract void writeBigInt(final BigInteger p0, final long p1);
    
    public abstract void writeUUID(final UUID p0);
    
    public final void checkAndWriteTypeName(final Object object, final Class fieldClass) {
        final long features = this.context.features;
        final Class objectClass;
        if ((features & Feature.WriteClassName.mask) == 0x0L || object == null || (objectClass = object.getClass()) == fieldClass || ((features & Feature.NotWriteHashMapArrayListClassName.mask) != 0x0L && (objectClass == HashMap.class || objectClass == ArrayList.class)) || ((features & Feature.NotWriteRootClassName.mask) != 0x0L && object == this.rootObject)) {
            return;
        }
        this.writeTypeName(TypeUtils.getTypeName(objectClass));
    }
    
    public void writeTypeName(final String typeName) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public boolean writeTypeName(final byte[] typeName, final long typeNameHash) {
        throw new JSONException("UnsupportedOperation");
    }
    
    public final void writeString(final Reader reader) {
        this.writeRaw(this.quote);
        try {
            final char[] chars = new char[2048];
            while (true) {
                final int len = reader.read(chars, 0, chars.length);
                if (len < 0) {
                    break;
                }
                if (len <= 0) {
                    continue;
                }
                this.writeString(chars, 0, len, false);
            }
        }
        catch (Exception ex) {
            throw new JSONException("read string from reader error", ex);
        }
        this.writeRaw(this.quote);
    }
    
    public abstract void writeString(final String p0);
    
    public abstract void writeStringLatin1(final byte[] p0);
    
    public abstract void writeStringUTF16(final byte[] p0);
    
    public void writeString(final List<String> list) {
        this.startArray();
        for (int i = 0, size = list.size(); i < size; ++i) {
            if (i != 0) {
                this.writeComma();
            }
            final String str = list.get(i);
            this.writeString(str);
        }
        this.endArray();
    }
    
    public abstract void writeString(final String[] p0);
    
    public void writeSymbol(final String string) {
        this.writeString(string);
    }
    
    public abstract void writeString(final char[] p0);
    
    public abstract void writeString(final char[] p0, final int p1, final int p2);
    
    public abstract void writeString(final char[] p0, final int p1, final int p2, final boolean p3);
    
    public abstract void writeLocalDate(final LocalDate p0);
    
    protected final boolean writeLocalDateWithFormat(final LocalDate date, final Context context) {
        if (context.dateFormatUnixTime || context.dateFormatMillis) {
            final LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIN);
            final long millis = dateTime.atZone(context.getZoneId()).toInstant().toEpochMilli();
            this.writeInt64(context.dateFormatMillis ? millis : (millis / 1000L));
            return true;
        }
        final DateTimeFormatter formatter = context.getDateFormatter();
        if (formatter != null) {
            String str;
            if (context.isDateFormatHasHour()) {
                str = formatter.format(LocalDateTime.of(date, LocalTime.MIN));
            }
            else {
                str = formatter.format(date);
            }
            this.writeString(str);
            return true;
        }
        return false;
    }
    
    public abstract void writeLocalDateTime(final LocalDateTime p0);
    
    public abstract void writeLocalTime(final LocalTime p0);
    
    public abstract void writeZonedDateTime(final ZonedDateTime p0);
    
    public abstract void writeOffsetDateTime(final OffsetDateTime p0);
    
    public void writeInstant(final Instant instant) {
        if (instant == null) {
            this.writeNull();
            return;
        }
        final String str = DateTimeFormatter.ISO_INSTANT.format(instant);
        this.writeString(str);
    }
    
    public abstract void writeDateTime14(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    public abstract void writeDateTime19(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    public abstract void writeDateTimeISO8601(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final boolean p8);
    
    public abstract void writeDateYYYMMDD8(final int p0, final int p1, final int p2);
    
    public abstract void writeDateYYYMMDD10(final int p0, final int p1, final int p2);
    
    public abstract void writeTimeHHMMSS8(final int p0, final int p1, final int p2);
    
    public void write(final List array) {
        if (array == null) {
            this.writeArrayNull();
            return;
        }
        final long NONE_DIRECT_FEATURES = Feature.ReferenceDetection.mask | Feature.PrettyFormat.mask | Feature.NotWriteEmptyArray.mask | Feature.NotWriteDefaultValue.mask;
        if ((this.context.features & NONE_DIRECT_FEATURES) != 0x0L) {
            final ObjectWriter objectWriter = this.context.getObjectWriter(array.getClass());
            objectWriter.write(this, array, null, null, 0L);
            return;
        }
        this.write0('[');
        for (int i = 0; i < array.size(); ++i) {
            final Object item = array.get(i);
            if (i != 0) {
                this.write0(',');
            }
            this.writeAny(item);
        }
        this.write0(']');
    }
    
    public void write(final Map map) {
        if (map == null) {
            this.writeNull();
            return;
        }
        final long NONE_DIRECT_FEATURES = Feature.ReferenceDetection.mask | Feature.PrettyFormat.mask | Feature.NotWriteEmptyArray.mask | Feature.NotWriteDefaultValue.mask;
        if ((this.context.features & NONE_DIRECT_FEATURES) != 0x0L) {
            final ObjectWriter objectWriter = this.context.getObjectWriter(map.getClass());
            objectWriter.write(this, map, null, null, 0L);
            return;
        }
        this.write0('{');
        boolean first = true;
        for (final Map.Entry o : map.entrySet()) {
            if (!first) {
                this.write0(',');
            }
            this.writeAny(o.getKey());
            this.write0(':');
            this.writeAny(o.getValue());
            first = false;
        }
        this.write0('}');
    }
    
    public void write(final JSONObject map) {
        this.write((Map)map);
    }
    
    public void writeAny(final Object value) {
        if (value == null) {
            this.writeNull();
            return;
        }
        final Class<?> valueClass = value.getClass();
        final ObjectWriter objectWriter = this.context.getObjectWriter(valueClass, valueClass);
        objectWriter.write(this, value, null, null, 0L);
    }
    
    public abstract void writeReference(final String p0);
    
    @Override
    public abstract void close();
    
    public abstract int size();
    
    public abstract byte[] getBytes();
    
    public abstract byte[] getBytes(final Charset p0);
    
    public void flushTo(final Writer to) {
        try {
            final String json = this.toString();
            to.write(json);
            this.off = 0;
        }
        catch (IOException e) {
            throw new JSONException("flushTo error", e);
        }
    }
    
    public abstract int flushTo(final OutputStream p0) throws IOException;
    
    public abstract int flushTo(final OutputStream p0, final Charset p1) throws IOException;
    
    static {
        WRITE_ARRAY_NULL_MASK = (Feature.NullAsDefaultValue.mask | Feature.WriteNullListAsEmpty.mask);
        DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    }
    
    public static final class Context
    {
        static final ZoneId DEFAULT_ZONE_ID;
        public final ObjectWriterProvider provider;
        DateTimeFormatter dateFormatter;
        String dateFormat;
        Locale locale;
        boolean dateFormatMillis;
        boolean dateFormatISO8601;
        boolean dateFormatUnixTime;
        boolean formatyyyyMMddhhmmss19;
        boolean formatHasDay;
        boolean formatHasHour;
        long features;
        ZoneId zoneId;
        int maxLevel;
        boolean hasFilter;
        PropertyPreFilter propertyPreFilter;
        PropertyFilter propertyFilter;
        NameFilter nameFilter;
        ValueFilter valueFilter;
        BeforeFilter beforeFilter;
        AfterFilter afterFilter;
        LabelFilter labelFilter;
        ContextValueFilter contextValueFilter;
        ContextNameFilter contextNameFilter;
        
        public Context(final ObjectWriterProvider provider) {
            this.maxLevel = 2048;
            if (provider == null) {
                throw new IllegalArgumentException("objectWriterProvider must not null");
            }
            this.features = JSONFactory.defaultWriterFeatures;
            this.provider = provider;
            this.zoneId = JSONFactory.defaultWriterZoneId;
            final String format = JSONFactory.defaultWriterFormat;
            if (format != null) {
                this.setDateFormat(format);
            }
        }
        
        public Context(final Feature... features) {
            this.maxLevel = 2048;
            this.features = JSONFactory.defaultWriterFeatures;
            this.provider = JSONFactory.getDefaultObjectWriterProvider();
            this.zoneId = JSONFactory.defaultWriterZoneId;
            final String format = JSONFactory.defaultWriterFormat;
            if (format != null) {
                this.setDateFormat(format);
            }
            for (int i = 0; i < features.length; ++i) {
                this.features |= features[i].mask;
            }
        }
        
        public Context(String format, final Feature... features) {
            this.maxLevel = 2048;
            this.features = JSONFactory.defaultWriterFeatures;
            this.provider = JSONFactory.getDefaultObjectWriterProvider();
            this.zoneId = JSONFactory.defaultWriterZoneId;
            for (int i = 0; i < features.length; ++i) {
                this.features |= features[i].mask;
            }
            if (format == null) {
                format = JSONFactory.defaultWriterFormat;
            }
            if (format != null) {
                this.setDateFormat(format);
            }
        }
        
        public Context(final ObjectWriterProvider provider, final Feature... features) {
            this.maxLevel = 2048;
            if (provider == null) {
                throw new IllegalArgumentException("objectWriterProvider must not null");
            }
            this.features = JSONFactory.defaultWriterFeatures;
            this.provider = provider;
            this.zoneId = JSONFactory.defaultWriterZoneId;
            for (int i = 0; i < features.length; ++i) {
                this.features |= features[i].mask;
            }
            final String format = JSONFactory.defaultWriterFormat;
            if (format != null) {
                this.setDateFormat(format);
            }
        }
        
        public long getFeatures() {
            return this.features;
        }
        
        public boolean isEnabled(final Feature feature) {
            return (this.features & feature.mask) != 0x0L;
        }
        
        public boolean isEnabled(final long feature) {
            return (this.features & feature) != 0x0L;
        }
        
        public void config(final Feature... features) {
            for (int i = 0; i < features.length; ++i) {
                this.features |= features[i].mask;
            }
        }
        
        public void config(final Feature feature, final boolean state) {
            if (state) {
                this.features |= feature.mask;
            }
            else {
                this.features &= ~feature.mask;
            }
        }
        
        public void configFilter(final Filter... filters) {
            for (int i = 0; i < filters.length; ++i) {
                final Filter filter = filters[i];
                if (filter instanceof NameFilter) {
                    if (this.nameFilter == null) {
                        this.nameFilter = (NameFilter)filter;
                    }
                    else {
                        this.nameFilter = NameFilter.compose(this.nameFilter, (NameFilter)filter);
                    }
                }
                if (filter instanceof ValueFilter) {
                    if (this.valueFilter == null) {
                        this.valueFilter = (ValueFilter)filter;
                    }
                    else {
                        this.valueFilter = ValueFilter.compose(this.valueFilter, (ValueFilter)filter);
                    }
                }
                if (filter instanceof PropertyFilter) {
                    this.propertyFilter = (PropertyFilter)filter;
                }
                if (filter instanceof PropertyPreFilter) {
                    this.propertyPreFilter = (PropertyPreFilter)filter;
                }
                if (filter instanceof BeforeFilter) {
                    this.beforeFilter = (BeforeFilter)filter;
                }
                if (filter instanceof AfterFilter) {
                    this.afterFilter = (AfterFilter)filter;
                }
                if (filter instanceof LabelFilter) {
                    this.labelFilter = (LabelFilter)filter;
                }
                if (filter instanceof ContextValueFilter) {
                    this.contextValueFilter = (ContextValueFilter)filter;
                }
                if (filter instanceof ContextNameFilter) {
                    this.contextNameFilter = (ContextNameFilter)filter;
                }
            }
            this.hasFilter = (this.propertyPreFilter != null || this.propertyFilter != null || this.nameFilter != null || this.valueFilter != null || this.beforeFilter != null || this.afterFilter != null || this.labelFilter != null || this.contextValueFilter != null || this.contextNameFilter != null);
        }
        
        public <T> ObjectWriter<T> getObjectWriter(final Class<T> objectType) {
            final boolean fieldBased = (this.features & Feature.FieldBased.mask) != 0x0L;
            return (ObjectWriter<T>)this.provider.getObjectWriter(objectType, objectType, fieldBased);
        }
        
        public <T> ObjectWriter<T> getObjectWriter(final Type objectType, final Class<T> objectClass) {
            final boolean fieldBased = (this.features & Feature.FieldBased.mask) != 0x0L;
            return (ObjectWriter<T>)this.provider.getObjectWriter(objectType, objectClass, fieldBased);
        }
        
        public ObjectWriterProvider getProvider() {
            return this.provider;
        }
        
        public ZoneId getZoneId() {
            if (this.zoneId == null) {
                this.zoneId = Context.DEFAULT_ZONE_ID;
            }
            return this.zoneId;
        }
        
        public void setZoneId(final ZoneId zoneId) {
            this.zoneId = zoneId;
        }
        
        public String getDateFormat() {
            return this.dateFormat;
        }
        
        public boolean isDateFormatMillis() {
            return this.dateFormatMillis;
        }
        
        public boolean isDateFormatUnixTime() {
            return this.dateFormatUnixTime;
        }
        
        public boolean isDateFormatISO8601() {
            return this.dateFormatISO8601;
        }
        
        public boolean isDateFormatHasDay() {
            return this.formatHasDay;
        }
        
        public boolean isDateFormatHasHour() {
            return this.formatHasHour;
        }
        
        public boolean isFormatyyyyMMddhhmmss19() {
            return this.formatyyyyMMddhhmmss19;
        }
        
        public DateTimeFormatter getDateFormatter() {
            if (this.dateFormatter == null && this.dateFormat != null && !this.dateFormatMillis && !this.dateFormatISO8601 && !this.dateFormatUnixTime) {
                this.dateFormatter = ((this.locale == null) ? DateTimeFormatter.ofPattern(this.dateFormat) : DateTimeFormatter.ofPattern(this.dateFormat, this.locale));
            }
            return this.dateFormatter;
        }
        
        public void setDateFormat(String dateFormat) {
            if (dateFormat == null || !dateFormat.equals(this.dateFormat)) {
                this.dateFormatter = null;
            }
            if (dateFormat != null && !dateFormat.isEmpty()) {
                boolean dateFormatMillis = false;
                boolean dateFormatISO8601 = false;
                boolean dateFormatUnixTime = false;
                boolean formatHasDay = false;
                boolean formatHasHour = false;
                boolean formatyyyyMMddhhmmss19 = false;
                final String s = dateFormat;
                switch (s) {
                    case "millis": {
                        dateFormatMillis = true;
                        break;
                    }
                    case "iso8601": {
                        dateFormatMillis = false;
                        dateFormatISO8601 = true;
                        break;
                    }
                    case "unixtime": {
                        dateFormatMillis = false;
                        dateFormatUnixTime = true;
                        break;
                    }
                    case "yyyy-MM-ddTHH:mm:ss": {
                        dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
                        formatHasDay = true;
                        formatHasHour = true;
                        break;
                    }
                    case "yyyy-MM-dd HH:mm:ss": {
                        formatyyyyMMddhhmmss19 = true;
                        formatHasDay = true;
                        formatHasHour = true;
                        break;
                    }
                    default: {
                        dateFormatMillis = false;
                        formatHasDay = dateFormat.contains("d");
                        formatHasHour = dateFormat.contains("H");
                        break;
                    }
                }
                this.dateFormatMillis = dateFormatMillis;
                this.dateFormatISO8601 = dateFormatISO8601;
                this.dateFormatUnixTime = dateFormatUnixTime;
                this.formatHasDay = formatHasDay;
                this.formatHasHour = formatHasHour;
                this.formatyyyyMMddhhmmss19 = formatyyyyMMddhhmmss19;
            }
            this.dateFormat = dateFormat;
        }
        
        public PropertyPreFilter getPropertyPreFilter() {
            return this.propertyPreFilter;
        }
        
        public void setPropertyPreFilter(final PropertyPreFilter propertyPreFilter) {
            this.propertyPreFilter = propertyPreFilter;
            if (propertyPreFilter != null) {
                this.hasFilter = true;
            }
        }
        
        public NameFilter getNameFilter() {
            return this.nameFilter;
        }
        
        public void setNameFilter(final NameFilter nameFilter) {
            this.nameFilter = nameFilter;
            if (nameFilter != null) {
                this.hasFilter = true;
            }
        }
        
        public ValueFilter getValueFilter() {
            return this.valueFilter;
        }
        
        public void setValueFilter(final ValueFilter valueFilter) {
            this.valueFilter = valueFilter;
            if (valueFilter != null) {
                this.hasFilter = true;
            }
        }
        
        public ContextValueFilter getContextValueFilter() {
            return this.contextValueFilter;
        }
        
        public void setContextValueFilter(final ContextValueFilter contextValueFilter) {
            this.contextValueFilter = contextValueFilter;
            if (contextValueFilter != null) {
                this.hasFilter = true;
            }
        }
        
        public ContextNameFilter getContextNameFilter() {
            return this.contextNameFilter;
        }
        
        public void setContextNameFilter(final ContextNameFilter contextNameFilter) {
            this.contextNameFilter = contextNameFilter;
            if (contextNameFilter != null) {
                this.hasFilter = true;
            }
        }
        
        public PropertyFilter getPropertyFilter() {
            return this.propertyFilter;
        }
        
        public void setPropertyFilter(final PropertyFilter propertyFilter) {
            this.propertyFilter = propertyFilter;
            if (propertyFilter != null) {
                this.hasFilter = true;
            }
        }
        
        public AfterFilter getAfterFilter() {
            return this.afterFilter;
        }
        
        public void setAfterFilter(final AfterFilter afterFilter) {
            this.afterFilter = afterFilter;
            if (afterFilter != null) {
                this.hasFilter = true;
            }
        }
        
        public BeforeFilter getBeforeFilter() {
            return this.beforeFilter;
        }
        
        public void setBeforeFilter(final BeforeFilter beforeFilter) {
            this.beforeFilter = beforeFilter;
            if (beforeFilter != null) {
                this.hasFilter = true;
            }
        }
        
        public LabelFilter getLabelFilter() {
            return this.labelFilter;
        }
        
        public void setLabelFilter(final LabelFilter labelFilter) {
            this.labelFilter = labelFilter;
            if (labelFilter != null) {
                this.hasFilter = true;
            }
        }
        
        static {
            DEFAULT_ZONE_ID = ZoneId.systemDefault();
        }
    }
    
    public enum Feature
    {
        FieldBased(1L), 
        IgnoreNoneSerializable(2L), 
        ErrorOnNoneSerializable(4L), 
        BeanToArray(8L), 
        WriteNulls(16L), 
        WriteMapNullValue(16L), 
        BrowserCompatible(32L), 
        NullAsDefaultValue(64L), 
        WriteBooleanAsNumber(128L), 
        WriteNonStringValueAsString(256L), 
        WriteClassName(512L), 
        NotWriteRootClassName(1024L), 
        NotWriteHashMapArrayListClassName(2048L), 
        NotWriteDefaultValue(4096L), 
        WriteEnumsUsingName(8192L), 
        WriteEnumUsingToString(16384L), 
        IgnoreErrorGetter(32768L), 
        PrettyFormat(65536L), 
        ReferenceDetection(131072L), 
        WriteNameAsSymbol(262144L), 
        WriteBigDecimalAsPlain(524288L), 
        UseSingleQuotes(1048576L), 
        MapSortField(2097152L), 
        WriteNullListAsEmpty(4194304L), 
        WriteNullStringAsEmpty(8388608L), 
        WriteNullNumberAsZero(16777216L), 
        WriteNullBooleanAsFalse(33554432L), 
        NotWriteEmptyArray(67108864L), 
        WriteNonStringKeyAsString(134217728L), 
        WritePairAsJavaBean(268435456L), 
        OptimizedForAscii(536870912L), 
        EscapeNoneAscii(1073741824L), 
        WriteByteArrayAsBase64(2147483648L), 
        IgnoreNonFieldGetter(4294967296L), 
        LargeObject(8589934592L), 
        WriteLongAsString(17179869184L), 
        BrowserSecure(34359738368L), 
        WriteEnumUsingOrdinal(68719476736L), 
        WriteThrowableClassName(137438953472L), 
        UnquoteFieldName(274877906944L), 
        NotWriteSetClassName(549755813888L), 
        NotWriteNumberClassName(1099511627776L);
        
        public final long mask;
        
        private Feature(final long mask) {
            this.mask = mask;
        }
        
        private static /* synthetic */ Feature[] $values() {
            return new Feature[] { Feature.FieldBased, Feature.IgnoreNoneSerializable, Feature.ErrorOnNoneSerializable, Feature.BeanToArray, Feature.WriteNulls, Feature.WriteMapNullValue, Feature.BrowserCompatible, Feature.NullAsDefaultValue, Feature.WriteBooleanAsNumber, Feature.WriteNonStringValueAsString, Feature.WriteClassName, Feature.NotWriteRootClassName, Feature.NotWriteHashMapArrayListClassName, Feature.NotWriteDefaultValue, Feature.WriteEnumsUsingName, Feature.WriteEnumUsingToString, Feature.IgnoreErrorGetter, Feature.PrettyFormat, Feature.ReferenceDetection, Feature.WriteNameAsSymbol, Feature.WriteBigDecimalAsPlain, Feature.UseSingleQuotes, Feature.MapSortField, Feature.WriteNullListAsEmpty, Feature.WriteNullStringAsEmpty, Feature.WriteNullNumberAsZero, Feature.WriteNullBooleanAsFalse, Feature.NotWriteEmptyArray, Feature.WriteNonStringKeyAsString, Feature.WritePairAsJavaBean, Feature.OptimizedForAscii, Feature.EscapeNoneAscii, Feature.WriteByteArrayAsBase64, Feature.IgnoreNonFieldGetter, Feature.LargeObject, Feature.WriteLongAsString, Feature.BrowserSecure, Feature.WriteEnumUsingOrdinal, Feature.WriteThrowableClassName, Feature.UnquoteFieldName, Feature.NotWriteSetClassName, Feature.NotWriteNumberClassName };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public static final class Path
    {
        public static final Path ROOT;
        public final Path parent;
        final String name;
        final int index;
        String fullPath;
        Path child0;
        Path child1;
        
        public Path(final Path parent, final String name) {
            this.parent = parent;
            this.name = name;
            this.index = -1;
        }
        
        public Path(final Path parent, final int index) {
            this.parent = parent;
            this.name = null;
            this.index = index;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Path path = (Path)o;
            return this.index == path.index && Objects.equals(this.parent, path.parent) && Objects.equals(this.name, path.name);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.parent, this.name, this.index);
        }
        
        @Override
        public String toString() {
            if (this.fullPath != null) {
                return this.fullPath;
            }
            byte[] buf = new byte[16];
            int off = 0;
            int level = 0;
            Path[] items = new Path[4];
            for (Path p = this; p != null; p = p.parent) {
                if (items.length == level) {
                    items = Arrays.copyOf(items, items.length + 4);
                }
                items[level] = p;
                ++level;
            }
            boolean ascii = true;
            for (int i = level - 1; i >= 0; --i) {
                final Path item = items[i];
                final String name = item.name;
                if (name == null) {
                    final int intValue = item.index;
                    int intValueSize;
                    int newCapacity;
                    for (intValueSize = IOUtils.stringSize(intValue); off + intValueSize + 2 >= buf.length; buf = Arrays.copyOf(buf, newCapacity)) {
                        newCapacity = buf.length + (buf.length >> 1);
                    }
                    buf[off++] = 91;
                    IOUtils.getChars(intValue, off + intValueSize, buf);
                    off += intValueSize;
                    buf[off++] = 93;
                }
                else {
                    if (off + 1 >= buf.length) {
                        final int newCapacity2 = buf.length + (buf.length >> 1);
                        buf = Arrays.copyOf(buf, newCapacity2);
                    }
                    if (i != level - 1) {
                        buf[off++] = 46;
                    }
                    if (JDKUtils.JVM_VERSION == 8) {
                        final char[] chars = JDKUtils.getCharArray(name);
                        for (int j = 0; j < chars.length; ++j) {
                            final char ch = chars[j];
                            switch (ch) {
                                case '!':
                                case '\"':
                                case '#':
                                case '%':
                                case '&':
                                case '\'':
                                case '(':
                                case ')':
                                case '*':
                                case '+':
                                case '-':
                                case '.':
                                case '/':
                                case ':':
                                case ';':
                                case '<':
                                case '=':
                                case '>':
                                case '?':
                                case '@':
                                case '[':
                                case '\\':
                                case ']':
                                case '^':
                                case '`':
                                case '~': {
                                    if (off + 1 >= buf.length) {
                                        final int newCapacity3 = buf.length + (buf.length >> 1);
                                        buf = Arrays.copyOf(buf, newCapacity3);
                                    }
                                    buf[off] = 92;
                                    buf[off + 1] = (byte)ch;
                                    off += 2;
                                    break;
                                }
                                default: {
                                    if (ch >= '\u0001' && ch <= '\u007f') {
                                        if (off == buf.length) {
                                            final int newCapacity3 = buf.length + (buf.length >> 1);
                                            buf = Arrays.copyOf(buf, newCapacity3);
                                        }
                                        buf[off++] = (byte)ch;
                                        break;
                                    }
                                    if (ch >= '\ud800' && ch < '\ue000') {
                                        ascii = false;
                                        if (ch < '\udc00') {
                                            int uc;
                                            if (name.length() - i < 2) {
                                                uc = -1;
                                            }
                                            else {
                                                final char d = name.charAt(i + 1);
                                                if (d < '\udc00' || d >= '\ue000') {
                                                    buf[off++] = 63;
                                                    break;
                                                }
                                                uc = (ch << 10) + d - 56613888;
                                            }
                                            if (uc < 0) {
                                                if (off == buf.length) {
                                                    final int newCapacity4 = buf.length + (buf.length >> 1);
                                                    buf = Arrays.copyOf(buf, newCapacity4);
                                                }
                                                buf[off++] = 63;
                                            }
                                            else {
                                                if (off + 3 >= buf.length) {
                                                    final int newCapacity4 = buf.length + (buf.length >> 1);
                                                    buf = Arrays.copyOf(buf, newCapacity4);
                                                }
                                                buf[off] = (byte)(0xF0 | uc >> 18);
                                                buf[off + 1] = (byte)(0x80 | (uc >> 12 & 0x3F));
                                                buf[off + 2] = (byte)(0x80 | (uc >> 6 & 0x3F));
                                                buf[off + 3] = (byte)(0x80 | (uc & 0x3F));
                                                off += 4;
                                                ++j;
                                            }
                                            break;
                                        }
                                        buf[off++] = 63;
                                        break;
                                    }
                                    else {
                                        if (ch > '\u07ff') {
                                            if (off + 2 >= buf.length) {
                                                final int newCapacity3 = buf.length + (buf.length >> 1);
                                                buf = Arrays.copyOf(buf, newCapacity3);
                                            }
                                            ascii = false;
                                            buf[off] = (byte)(0xE0 | (ch >> 12 & 0xF));
                                            buf[off + 1] = (byte)(0x80 | (ch >> 6 & 0x3F));
                                            buf[off + 2] = (byte)(0x80 | (ch & '?'));
                                            off += 3;
                                            break;
                                        }
                                        if (off + 1 >= buf.length) {
                                            final int newCapacity3 = buf.length + (buf.length >> 1);
                                            buf = Arrays.copyOf(buf, newCapacity3);
                                        }
                                        ascii = false;
                                        buf[off] = (byte)(0xC0 | (ch >> 6 & 0x1F));
                                        buf[off + 1] = (byte)(0x80 | (ch & '?'));
                                        off += 2;
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        for (int k = 0; k < name.length(); ++k) {
                            final char ch2 = name.charAt(k);
                            switch (ch2) {
                                case '!':
                                case '\"':
                                case '#':
                                case '%':
                                case '&':
                                case '\'':
                                case '(':
                                case ')':
                                case '*':
                                case '+':
                                case '-':
                                case '.':
                                case '/':
                                case ':':
                                case ';':
                                case '<':
                                case '=':
                                case '>':
                                case '?':
                                case '@':
                                case '[':
                                case '\\':
                                case ']':
                                case '^':
                                case '`':
                                case '~': {
                                    if (off + 1 >= buf.length) {
                                        final int newCapacity = buf.length + (buf.length >> 1);
                                        buf = Arrays.copyOf(buf, newCapacity);
                                    }
                                    buf[off] = 92;
                                    buf[off + 1] = (byte)ch2;
                                    off += 2;
                                    break;
                                }
                                default: {
                                    if (ch2 >= '\u0001' && ch2 <= '\u007f') {
                                        if (off == buf.length) {
                                            final int newCapacity = buf.length + (buf.length >> 1);
                                            buf = Arrays.copyOf(buf, newCapacity);
                                        }
                                        buf[off++] = (byte)ch2;
                                        break;
                                    }
                                    if (ch2 >= '\ud800' && ch2 < '\ue000') {
                                        ascii = false;
                                        if (ch2 < '\udc00') {
                                            int uc2;
                                            if (name.length() - i < 2) {
                                                uc2 = -1;
                                            }
                                            else {
                                                final char d2 = name.charAt(i + 1);
                                                if (d2 < '\udc00' || d2 >= '\ue000') {
                                                    buf[off++] = 63;
                                                    break;
                                                }
                                                uc2 = (ch2 << 10) + d2 - 56613888;
                                            }
                                            if (uc2 < 0) {
                                                if (off == buf.length) {
                                                    final int newCapacity3 = buf.length + (buf.length >> 1);
                                                    buf = Arrays.copyOf(buf, newCapacity3);
                                                }
                                                buf[off++] = 63;
                                            }
                                            else {
                                                if (off + 4 >= buf.length) {
                                                    final int newCapacity3 = buf.length + (buf.length >> 1);
                                                    buf = Arrays.copyOf(buf, newCapacity3);
                                                }
                                                buf[off] = (byte)(0xF0 | uc2 >> 18);
                                                buf[off + 1] = (byte)(0x80 | (uc2 >> 12 & 0x3F));
                                                buf[off + 2] = (byte)(0x80 | (uc2 >> 6 & 0x3F));
                                                buf[off + 3] = (byte)(0x80 | (uc2 & 0x3F));
                                                off += 4;
                                                ++k;
                                            }
                                            break;
                                        }
                                        buf[off++] = 63;
                                        break;
                                    }
                                    else {
                                        if (ch2 > '\u07ff') {
                                            if (off + 2 >= buf.length) {
                                                final int newCapacity = buf.length + (buf.length >> 1);
                                                buf = Arrays.copyOf(buf, newCapacity);
                                            }
                                            ascii = false;
                                            buf[off] = (byte)(0xE0 | (ch2 >> 12 & 0xF));
                                            buf[off + 1] = (byte)(0x80 | (ch2 >> 6 & 0x3F));
                                            buf[off + 2] = (byte)(0x80 | (ch2 & '?'));
                                            off += 3;
                                            break;
                                        }
                                        if (off + 1 >= buf.length) {
                                            final int newCapacity = buf.length + (buf.length >> 1);
                                            buf = Arrays.copyOf(buf, newCapacity);
                                        }
                                        ascii = false;
                                        buf[off] = (byte)(0xC0 | (ch2 >> 6 & 0x1F));
                                        buf[off + 1] = (byte)(0x80 | (ch2 & '?'));
                                        off += 2;
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (ascii) {
                if (JDKUtils.STRING_CREATOR_JDK11 != null) {
                    byte[] bytes;
                    if (off == buf.length) {
                        bytes = buf;
                    }
                    else {
                        bytes = new byte[off];
                        System.arraycopy(buf, 0, bytes, 0, off);
                    }
                    return this.fullPath = JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
                }
                if (JDKUtils.STRING_CREATOR_JDK8 != null) {
                    final char[] chars2 = new char[off];
                    for (int l = 0; l < off; ++l) {
                        chars2[l] = (char)buf[l];
                    }
                    return this.fullPath = JDKUtils.STRING_CREATOR_JDK8.apply(chars2, Boolean.TRUE);
                }
            }
            return this.fullPath = new String(buf, 0, off, ascii ? StandardCharsets.ISO_8859_1 : StandardCharsets.UTF_8);
        }
        
        static {
            ROOT = new Path(null, "$");
        }
    }
}
