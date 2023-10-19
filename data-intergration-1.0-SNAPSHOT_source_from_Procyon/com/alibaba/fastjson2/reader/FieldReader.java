// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.function.BiConsumer;
import java.util.Optional;
import java.time.Instant;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import com.alibaba.fastjson2.util.JdbcSupport;
import java.util.function.Function;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.Date;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Collection;
import java.util.List;
import java.lang.reflect.AnnotatedElement;
import com.alibaba.fastjson2.annotation.JSONField;
import java.lang.reflect.Member;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Modifier;
import java.io.Serializable;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public abstract class FieldReader<T> implements Comparable<FieldReader>
{
    public final int ordinal;
    public final String fieldName;
    public final Class fieldClass;
    public final Type fieldType;
    public final long features;
    public final String format;
    public final Method method;
    public final Field field;
    protected final long fieldOffset;
    public final Object defaultValue;
    public final Locale locale;
    public final JSONSchema schema;
    final boolean fieldClassSerializable;
    final long fieldNameHash;
    final long fieldNameHashLCase;
    volatile ObjectReader reader;
    volatile JSONPath referenceCache;
    final boolean noneStaticMemberClass;
    final boolean readOnly;
    Type itemType;
    Class itemClass;
    volatile ObjectReader itemReader;
    
    public FieldReader(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Method method, final Field field) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldClass = fieldClass;
        this.fieldClassSerializable = (fieldClass != null && (Serializable.class.isAssignableFrom(fieldClass) || Modifier.isInterface(fieldClass.getModifiers())));
        this.features = features;
        this.fieldNameHash = Fnv.hashCode64(fieldName);
        this.fieldNameHashLCase = Fnv.hashCode64LCase(fieldName);
        this.ordinal = ordinal;
        this.format = format;
        this.locale = locale;
        this.defaultValue = defaultValue;
        this.schema = schema;
        this.method = method;
        this.field = field;
        boolean readOnly = false;
        if (method != null && method.getParameterCount() == 0) {
            readOnly = true;
        }
        else if (field != null && Modifier.isFinal(field.getModifiers())) {
            readOnly = true;
        }
        this.readOnly = readOnly;
        long fieldOffset = -1L;
        if (field != null && (features & 0x80000000000000L) == 0x0L) {
            fieldOffset = JDKUtils.UNSAFE.objectFieldOffset(field);
        }
        this.fieldOffset = fieldOffset;
        if (fieldOffset == -1L && field != null && method == null) {
            try {
                field.setAccessible(true);
            }
            catch (Throwable e) {
                JDKUtils.setReflectErrorLast(e);
            }
        }
        Class declaringClass = null;
        if (method != null) {
            declaringClass = method.getDeclaringClass();
        }
        else if (field != null) {
            declaringClass = field.getDeclaringClass();
        }
        this.noneStaticMemberClass = BeanUtils.isNoneStaticMemberClass(declaringClass, fieldClass);
    }
    
    public void acceptDefaultValue(final T object) {
        if (this.defaultValue != null) {
            this.accept(object, this.defaultValue);
        }
    }
    
    public ObjectReader getObjectReader(final JSONReader jsonReader) {
        if (this.reader != null) {
            return this.reader;
        }
        return this.reader = jsonReader.getObjectReader(this.fieldType);
    }
    
    public ObjectReader getObjectReader(final JSONReader.Context context) {
        if (this.reader != null) {
            return this.reader;
        }
        return this.reader = context.getObjectReader(this.fieldType);
    }
    
    public ObjectReader getObjectReader(final ObjectReaderProvider provider) {
        if (this.reader != null) {
            return this.reader;
        }
        final boolean fieldBased = (this.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        return this.reader = provider.getObjectReader(this.fieldType, fieldBased);
    }
    
    public Type getItemType() {
        return this.itemType;
    }
    
    public Class getItemClass() {
        if (this.itemType == null) {
            return null;
        }
        if (this.itemClass == null) {
            this.itemClass = TypeUtils.getClass(this.itemType);
        }
        return this.itemClass;
    }
    
    public long getItemClassHash() {
        final Class itemClass = this.getItemClass();
        if (itemClass == null) {
            return 0L;
        }
        return Fnv.hashCode64(itemClass.getName());
    }
    
    @Override
    public String toString() {
        final Member member = (this.method != null) ? this.method : this.field;
        if (member != null) {
            return member.getName();
        }
        return this.fieldName;
    }
    
    public void addResolveTask(final JSONReader jsonReader, final Object object, final String reference) {
        JSONPath path;
        if (this.referenceCache != null && this.referenceCache.toString().equals(reference)) {
            path = this.referenceCache;
        }
        else {
            final JSONPath of = JSONPath.of(reference);
            this.referenceCache = of;
            path = of;
        }
        jsonReader.addResolveTask(this, object, path);
    }
    
    @Override
    public int compareTo(final FieldReader o) {
        final int nameCompare = this.fieldName.compareTo(o.fieldName);
        if (nameCompare != 0) {
            if (this.ordinal < o.ordinal) {
                return -1;
            }
            if (this.ordinal > o.ordinal) {
                return 1;
            }
            return nameCompare;
        }
        else {
            final int cmp = (this.isReadOnly() == o.isReadOnly()) ? 0 : (this.isReadOnly() ? 1 : -1);
            if (cmp != 0) {
                return cmp;
            }
            final Member thisMember = (this.field != null) ? this.field : this.method;
            final Member otherMember = (o.field != null) ? o.field : o.method;
            if (thisMember != null && otherMember != null && thisMember.getClass() != otherMember.getClass()) {
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
            }
            if (this.field != null && o.field != null) {
                final Class<?> thisDeclaringClass2 = this.field.getDeclaringClass();
                final Class<?> otherDeclaringClass2 = o.field.getDeclaringClass();
                for (Class s = thisDeclaringClass2.getSuperclass(); s != null && s != Object.class; s = s.getSuperclass()) {
                    if (s == otherDeclaringClass2) {
                        return 1;
                    }
                }
                for (Class s = otherDeclaringClass2.getSuperclass(); s != null && s != Object.class; s = s.getSuperclass()) {
                    if (s == thisDeclaringClass2) {
                        return -1;
                    }
                }
            }
            if (this.method != null && o.method != null) {
                final Class<?> thisDeclaringClass2 = this.method.getDeclaringClass();
                final Class<?> otherDeclaringClass2 = o.method.getDeclaringClass();
                for (Class s = thisDeclaringClass2.getSuperclass(); s != null && s != Object.class; s = s.getSuperclass()) {
                    if (s == otherDeclaringClass2) {
                        return -1;
                    }
                }
                for (Class s = otherDeclaringClass2.getSuperclass(); s != null && s != Object.class; s = s.getSuperclass()) {
                    if (s == thisDeclaringClass2) {
                        return 1;
                    }
                }
                if (this.method.getParameterCount() == 1 && o.method.getParameterCount() == 1) {
                    final Class<?> thisParamType = this.method.getParameterTypes()[0];
                    final Class<?> otherParamType = o.method.getParameterTypes()[0];
                    if (thisParamType != otherParamType) {
                        if (thisParamType.isAssignableFrom(otherParamType)) {
                            return 1;
                        }
                        if (otherParamType.isAssignableFrom(thisParamType)) {
                            return -1;
                        }
                        if (thisParamType.isEnum() && (otherParamType == Integer.class || otherParamType == Integer.TYPE)) {
                            return 1;
                        }
                        if (otherParamType.isEnum() && (thisParamType == Integer.class || thisParamType == Integer.TYPE)) {
                            return -1;
                        }
                        final JSONField thisAnnotation = BeanUtils.findAnnotation(this.method, JSONField.class);
                        final JSONField otherAnnotation = BeanUtils.findAnnotation(o.method, JSONField.class);
                        if (thisAnnotation != null && otherAnnotation == null) {
                            return -1;
                        }
                        if (thisAnnotation == null && otherAnnotation != null) {
                            return 1;
                        }
                    }
                }
                final String thisMethodName = this.method.getName();
                final String otherMethodName = o.method.getName();
                if (!thisMethodName.equals(otherMethodName)) {
                    final String thisName = BeanUtils.setterName(thisMethodName, null);
                    final String otherName = BeanUtils.setterName(otherMethodName, null);
                    if (this.fieldName.equals(thisName) && !o.fieldName.equals(otherName)) {
                        return 1;
                    }
                    if (o.fieldName.equals(otherName) && !this.fieldName.equals(thisName)) {
                        return -1;
                    }
                }
            }
            final ObjectReader thisInitReader = this.getInitReader();
            final ObjectReader otherInitReader = o.getInitReader();
            if (thisInitReader != null && otherInitReader == null) {
                return -1;
            }
            if (thisInitReader == null && otherInitReader != null) {
                return 1;
            }
            final Class thisFieldClass = this.fieldClass;
            final Class otherClass = o.fieldClass;
            final boolean thisClassPrimitive = thisFieldClass.isPrimitive();
            final boolean otherClassPrimitive = otherClass.isPrimitive();
            if (thisClassPrimitive && !otherClassPrimitive) {
                return -1;
            }
            if (!thisClassPrimitive && otherClassPrimitive) {
                return 1;
            }
            final boolean thisClassStartsWithJava = thisFieldClass.getName().startsWith("java.");
            final boolean otherClassStartsWithJava = otherClass.getName().startsWith("java.");
            if (thisClassStartsWithJava && !otherClassStartsWithJava) {
                return -1;
            }
            if (!thisClassStartsWithJava && otherClassStartsWithJava) {
                return 1;
            }
            return cmp;
        }
    }
    
    public boolean isUnwrapped() {
        return (this.features & 0x2000000000000L) != 0x0L;
    }
    
    public void addResolveTask(final JSONReader jsonReader, final List object, final int i, final String reference) {
        jsonReader.addResolveTask(object, i, JSONPath.of(reference));
    }
    
    public void readFieldValueJSONB(final JSONReader jsonReader, final T object) {
        this.readFieldValue(jsonReader, object);
    }
    
    public abstract Object readFieldValue(final JSONReader p0);
    
    public void accept(final T object, final boolean value) {
        this.accept(object, (Object)value);
    }
    
    public boolean supportAcceptType(final Class valueClass) {
        return this.fieldClass == valueClass;
    }
    
    public void accept(final T object, final byte value) {
        this.accept(object, (Object)value);
    }
    
    public void accept(final T object, final short value) {
        this.accept(object, (Object)value);
    }
    
    public void accept(final T object, final int value) {
        this.accept(object, (Object)value);
    }
    
    public void accept(final T object, final long value) {
        this.accept(object, (Object)value);
    }
    
    public void accept(final T object, final char value) {
        this.accept(object, (Object)value);
    }
    
    public void accept(final T object, final float value) {
        this.accept(object, (Object)value);
    }
    
    public void accept(final T object, final double value) {
        this.accept(object, (Object)value);
    }
    
    public abstract void accept(final T p0, final Object p1);
    
    protected void acceptAny(final T object, Object fieldValue, final long features) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        boolean autoCast = true;
        if (fieldValue != null) {
            final Class<?> valueClass = fieldValue.getClass();
            if (!this.supportAcceptType(valueClass)) {
                if (valueClass == String.class) {
                    if (this.fieldClass == Date.class) {
                        autoCast = false;
                    }
                }
                else if (valueClass == Integer.class && (this.fieldClass == Boolean.TYPE || this.fieldClass == Boolean.class) && (features & JSONReader.Feature.NonZeroNumberCastToBooleanAsTrue.mask) != 0x0L) {
                    final int intValue = (int)fieldValue;
                    fieldValue = (intValue != 0);
                }
                if (valueClass != this.fieldClass && autoCast) {
                    final Function typeConvert = provider.getTypeConvert(valueClass, this.fieldClass);
                    if (typeConvert != null) {
                        fieldValue = typeConvert.apply(fieldValue);
                    }
                }
            }
        }
        Object typedFieldValue;
        if (fieldValue == null || this.fieldType == fieldValue.getClass()) {
            typedFieldValue = fieldValue;
        }
        else if (fieldValue instanceof JSONObject) {
            final JSONReader.Feature[] toFeatures = ((features & JSONReader.Feature.SupportSmartMatch.mask) != 0x0L) ? new JSONReader.Feature[] { JSONReader.Feature.SupportSmartMatch } : new JSONReader.Feature[0];
            typedFieldValue = ((JSONObject)fieldValue).to(this.fieldType, toFeatures);
        }
        else if (fieldValue instanceof JSONArray) {
            typedFieldValue = ((JSONArray)fieldValue).to(this.fieldType);
        }
        else if (features == 0L && !this.fieldClass.isInstance(fieldValue) && this.format == null) {
            final ObjectReader initReader = this.getInitReader();
            if (initReader != null) {
                final String fieldValueJson = JSON.toJSONString(fieldValue);
                typedFieldValue = initReader.readObject(JSONReader.of(fieldValueJson), this.fieldType, this.fieldName, features);
            }
            else {
                typedFieldValue = TypeUtils.cast(fieldValue, (Class<Object>)this.fieldClass, provider);
            }
        }
        else if (autoCast) {
            final String fieldValueJSONString = JSON.toJSONString(fieldValue);
            final JSONReader.Context readContext = JSONFactory.createReadContext();
            if ((features & JSONReader.Feature.SupportSmartMatch.mask) != 0x0L) {
                readContext.config(JSONReader.Feature.SupportSmartMatch);
            }
            final JSONReader jsonReader = JSONReader.of(fieldValueJSONString, readContext);
            try {
                final ObjectReader fieldObjectReader = this.getObjectReader(jsonReader);
                typedFieldValue = fieldObjectReader.readObject(jsonReader, null, this.fieldName, features);
                if (jsonReader != null) {
                    jsonReader.close();
                }
            }
            catch (Throwable t) {
                if (jsonReader != null) {
                    try {
                        jsonReader.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        else {
            typedFieldValue = fieldValue;
        }
        this.accept(object, typedFieldValue);
    }
    
    public abstract void readFieldValue(final JSONReader p0, final T p1);
    
    public ObjectReader checkObjectAutoType(final JSONReader jsonReader) {
        return null;
    }
    
    public boolean isReadOnly() {
        return this.readOnly;
    }
    
    public ObjectReader getInitReader() {
        return null;
    }
    
    public void processExtra(final JSONReader jsonReader, final Object object) {
        jsonReader.skipValue();
    }
    
    public void acceptExtra(final Object object, final String name, final Object value) {
    }
    
    public ObjectReader getItemObjectReader(final JSONReader.Context ctx) {
        if (this.itemReader != null) {
            return this.itemReader;
        }
        return this.itemReader = ctx.getObjectReader(this.itemType);
    }
    
    public ObjectReader getItemObjectReader(final JSONReader jsonReader) {
        return this.getItemObjectReader(jsonReader.getContext());
    }
    
    static ObjectReader createFormattedObjectReader(final Type fieldType, final Class fieldClass, final String format, final Locale locale) {
        if (format != null && !format.isEmpty()) {
            final String typeName2;
            final String typeName = typeName2 = fieldType.getTypeName();
            switch (typeName2) {
                case "java.sql.Time": {
                    return JdbcSupport.createTimeReader((Class)fieldType, format, locale);
                }
                case "java.sql.Timestamp": {
                    return JdbcSupport.createTimestampReader((Class)fieldType, format, locale);
                }
                case "java.sql.Date": {
                    return JdbcSupport.createDateReader((Class)fieldType, format, locale);
                }
                case "byte[]":
                case "[B": {
                    return new ObjectReaderImplInt8Array(format);
                }
                default: {
                    if (Calendar.class.isAssignableFrom(fieldClass)) {
                        return ObjectReaderImplCalendar.of(format, locale);
                    }
                    if (fieldClass == ZonedDateTime.class) {
                        return ObjectReaderImplZonedDateTime.of(format, locale);
                    }
                    if (fieldClass == LocalDateTime.class) {
                        return new ObjectReaderImplLocalDateTime(format, locale);
                    }
                    if (fieldClass == LocalDate.class) {
                        return ObjectReaderImplLocalDate.of(format, locale);
                    }
                    if (fieldClass == LocalTime.class) {
                        return new ObjectReaderImplLocalTime(format, locale);
                    }
                    if (fieldClass == Instant.class) {
                        return ObjectReaderImplInstant.of(format, locale);
                    }
                    if (fieldClass == Optional.class) {
                        return ObjectReaderImplOptional.of(fieldType, format, locale);
                    }
                    if (fieldClass == Date.class) {
                        return ObjectReaderImplDate.of(format, locale);
                    }
                    break;
                }
            }
        }
        return null;
    }
    
    public BiConsumer getFunction() {
        return null;
    }
}
