// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import java.util.Iterator;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONFactory;
import java.time.LocalDateTime;
import java.time.LocalDate;
import com.alibaba.fastjson2.util.DateUtils;
import java.util.Date;
import com.alibaba.fastjson2.filter.AfterFilter;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.filter.LabelFilter;
import com.alibaba.fastjson2.filter.ContextValueFilter;
import com.alibaba.fastjson2.filter.ContextNameFilter;
import com.alibaba.fastjson2.filter.BeforeFilter;
import com.alibaba.fastjson2.filter.BeanContext;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.JSONObject;
import java.util.Map;
import java.util.Collection;
import com.alibaba.fastjson2.SymbolTable;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Arrays;
import java.io.Serializable;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.List;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.PropertyFilter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;

public class ObjectWriterAdapter<T> implements ObjectWriter<T>
{
    boolean hasFilter;
    PropertyPreFilter propertyPreFilter;
    PropertyFilter propertyFilter;
    NameFilter nameFilter;
    ValueFilter valueFilter;
    static final String TYPE = "@type";
    final Class objectClass;
    final List<FieldWriter> fieldWriters;
    protected final FieldWriter[] fieldWriterArray;
    final String typeKey;
    byte[] typeKeyJSONB;
    protected final String typeName;
    protected final long typeNameHash;
    protected long typeNameSymbolCache;
    protected final byte[] typeNameJSONB;
    byte[] nameWithColonUTF8;
    char[] nameWithColonUTF16;
    final long features;
    final long[] hashCodes;
    final short[] mapping;
    final boolean hasValueField;
    final boolean serializable;
    final boolean containsNoneFieldGetter;
    final boolean googleCollection;
    byte[] jsonbClassInfo;
    
    public ObjectWriterAdapter(final Class<T> objectClass, final List<FieldWriter> fieldWriters) {
        this(objectClass, null, null, 0L, fieldWriters);
    }
    
    public ObjectWriterAdapter(final Class<T> objectClass, final String typeKey, String typeName, final long features, final List<FieldWriter> fieldWriters) {
        if (typeName == null && objectClass != null) {
            if (Enum.class.isAssignableFrom(objectClass) && !objectClass.isEnum()) {
                typeName = objectClass.getSuperclass().getName();
            }
            else {
                typeName = TypeUtils.getTypeName(objectClass);
            }
        }
        this.objectClass = objectClass;
        this.typeKey = ((typeKey == null || typeKey.isEmpty()) ? "@type" : typeKey);
        this.typeName = typeName;
        this.typeNameHash = ((typeName != null) ? Fnv.hashCode64(typeName) : 0L);
        this.typeNameJSONB = JSONB.toBytes(typeName);
        this.features = features;
        this.fieldWriters = fieldWriters;
        this.serializable = (objectClass == null || Serializable.class.isAssignableFrom(objectClass));
        this.googleCollection = ("com.google.common.collect.AbstractMapBasedMultimap$RandomAccessWrappedList".equals(typeName) || "com.google.common.collect.AbstractMapBasedMultimap$WrappedSet".equals(typeName));
        fieldWriters.toArray(this.fieldWriterArray = new FieldWriter[fieldWriters.size()]);
        this.hasValueField = (this.fieldWriterArray.length == 1 && (this.fieldWriterArray[0].features & 0x1000000000000L) != 0x0L);
        boolean containsNoneFieldGetter = false;
        final long[] hashCodes = new long[this.fieldWriterArray.length];
        for (int i = 0; i < this.fieldWriterArray.length; ++i) {
            final FieldWriter fieldWriter = this.fieldWriterArray[i];
            final long hashCode = Fnv.hashCode64(fieldWriter.fieldName);
            hashCodes[i] = hashCode;
            if (fieldWriter.method != null && (fieldWriter.features & 0x10000000000000L) == 0x0L) {
                containsNoneFieldGetter = true;
            }
        }
        this.containsNoneFieldGetter = containsNoneFieldGetter;
        Arrays.sort(this.hashCodes = Arrays.copyOf(hashCodes, hashCodes.length));
        this.mapping = new short[this.hashCodes.length];
        for (int i = 0; i < hashCodes.length; ++i) {
            final long hashCode2 = hashCodes[i];
            final int index = Arrays.binarySearch(this.hashCodes, hashCode2);
            this.mapping[index] = (short)i;
        }
    }
    
    @Override
    public long getFeatures() {
        return this.features;
    }
    
    @Override
    public FieldWriter getFieldWriter(final long hashCode) {
        final int m = Arrays.binarySearch(this.hashCodes, hashCode);
        if (m < 0) {
            return null;
        }
        final int index = this.mapping[m];
        return this.fieldWriterArray[index];
    }
    
    @Override
    public final boolean hasFilter(final JSONWriter jsonWriter) {
        return this.hasFilter || jsonWriter.hasFilter(this.containsNoneFieldGetter);
    }
    
    @Override
    public void setPropertyFilter(final PropertyFilter propertyFilter) {
        this.propertyFilter = propertyFilter;
        if (propertyFilter != null) {
            this.hasFilter = true;
        }
    }
    
    @Override
    public void setValueFilter(final ValueFilter valueFilter) {
        this.valueFilter = valueFilter;
        if (valueFilter != null) {
            this.hasFilter = true;
        }
    }
    
    @Override
    public void setNameFilter(final NameFilter nameFilter) {
        this.nameFilter = nameFilter;
        if (nameFilter != null) {
            this.hasFilter = true;
        }
    }
    
    @Override
    public void setPropertyPreFilter(final PropertyPreFilter propertyPreFilter) {
        this.propertyPreFilter = propertyPreFilter;
        if (propertyPreFilter != null) {
            this.hasFilter = true;
        }
    }
    
    @Override
    public void writeArrayMappingJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
            this.writeClassInfo(jsonWriter);
        }
        final int size = this.fieldWriters.size();
        jsonWriter.startArray(size);
        for (int i = 0; i < size; ++i) {
            final FieldWriter fieldWriter = this.fieldWriters.get(i);
            fieldWriter.writeValue(jsonWriter, object);
        }
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        final long featuresAll = features | this.features | jsonWriter.getFeatures();
        if (!this.serializable) {
            if ((featuresAll & JSONWriter.Feature.ErrorOnNoneSerializable.mask) != 0x0L) {
                this.errorOnNoneSerializable();
                return;
            }
            if ((featuresAll & JSONWriter.Feature.IgnoreNoneSerializable.mask) != 0x0L) {
                jsonWriter.writeNull();
                return;
            }
        }
        if ((featuresAll & JSONWriter.Feature.IgnoreNoneSerializable.mask) != 0x0L) {
            this.writeWithFilter(jsonWriter, object, fieldName, fieldType, features);
            return;
        }
        final int size = this.fieldWriterArray.length;
        if (jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
            this.writeClassInfo(jsonWriter);
        }
        jsonWriter.startObject();
        for (int i = 0; i < size; ++i) {
            final FieldWriter fieldWriter = this.fieldWriters.get(i);
            fieldWriter.write(jsonWriter, object);
        }
        jsonWriter.endObject();
    }
    
    protected final void writeClassInfo(final JSONWriter jsonWriter) {
        final SymbolTable symbolTable = jsonWriter.symbolTable;
        if (symbolTable != null && this.writeClassInfoSymbol(jsonWriter, symbolTable)) {
            return;
        }
        jsonWriter.writeTypeName(this.typeNameJSONB, this.typeNameHash);
    }
    
    private boolean writeClassInfoSymbol(final JSONWriter jsonWriter, final SymbolTable symbolTable) {
        final int symbolTableIdentity = System.identityHashCode(symbolTable);
        int symbol;
        if (this.typeNameSymbolCache == 0L) {
            symbol = symbolTable.getOrdinalByHashCode(this.typeNameHash);
            if (symbol != -1) {
                this.typeNameSymbolCache = ((long)symbol << 32 | (long)symbolTableIdentity);
            }
        }
        else {
            final int identity = (int)this.typeNameSymbolCache;
            if (identity == symbolTableIdentity) {
                symbol = (int)(this.typeNameSymbolCache >> 32);
            }
            else {
                symbol = symbolTable.getOrdinalByHashCode(this.typeNameHash);
                if (symbol != -1) {
                    this.typeNameSymbolCache = ((long)symbol << 32 | (long)symbolTableIdentity);
                }
            }
        }
        if (symbol != -1) {
            jsonWriter.writeRaw((byte)(-110));
            jsonWriter.writeInt32(-symbol);
            return true;
        }
        return false;
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (this.hasValueField) {
            final FieldWriter fieldWriter = this.fieldWriterArray[0];
            fieldWriter.writeValue(jsonWriter, object);
            return;
        }
        final long featuresAll = features | this.features | jsonWriter.getFeatures();
        final boolean beanToArray = (featuresAll & JSONWriter.Feature.BeanToArray.mask) != 0x0L;
        if (jsonWriter.jsonb) {
            if (beanToArray) {
                this.writeArrayMappingJSONB(jsonWriter, object, fieldName, fieldType, features);
                return;
            }
            this.writeJSONB(jsonWriter, object, fieldName, fieldType, features);
        }
        else {
            if (this.googleCollection) {
                final Collection collection = (Collection)object;
                ObjectWriterImplCollection.INSTANCE.write(jsonWriter, collection, fieldName, fieldType, features);
                return;
            }
            if (beanToArray) {
                this.writeArrayMapping(jsonWriter, object, fieldName, fieldType, features);
                return;
            }
            if (!this.serializable) {
                if ((featuresAll & JSONWriter.Feature.ErrorOnNoneSerializable.mask) != 0x0L) {
                    this.errorOnNoneSerializable();
                    return;
                }
                if ((featuresAll & JSONWriter.Feature.IgnoreNoneSerializable.mask) != 0x0L) {
                    jsonWriter.writeNull();
                    return;
                }
            }
            if (this.hasFilter(jsonWriter)) {
                this.writeWithFilter(jsonWriter, object, fieldName, fieldType, features);
                return;
            }
            jsonWriter.startObject();
            if (((features | this.features) & JSONWriter.Feature.WriteClassName.mask) != 0x0L || jsonWriter.isWriteTypeInfo(object, features)) {
                this.writeTypeInfo(jsonWriter);
            }
            for (int i = 0; i < this.fieldWriters.size(); ++i) {
                final FieldWriter fieldWriter2 = this.fieldWriters.get(i);
                fieldWriter2.write(jsonWriter, object);
            }
            jsonWriter.endObject();
        }
    }
    
    public Map<String, Object> toMap(final Object object) {
        final JSONObject map = new JSONObject(this.fieldWriters.size());
        for (int i = 0; i < this.fieldWriters.size(); ++i) {
            final FieldWriter fieldWriter = this.fieldWriters.get(i);
            map.put(fieldWriter.fieldName, fieldWriter.getFieldValue(object));
        }
        return map;
    }
    
    @Override
    public List<FieldWriter> getFieldWriters() {
        return this.fieldWriters;
    }
    
    @Override
    public boolean writeTypeInfo(final JSONWriter jsonWriter) {
        if (jsonWriter.utf8) {
            if (this.nameWithColonUTF8 == null) {
                final byte[] chars = new byte[this.typeKey.length() + this.typeName.length() + 5];
                chars[0] = 34;
                this.typeKey.getBytes(0, this.typeKey.length(), chars, 1);
                chars[this.typeKey.length() + 1] = 34;
                chars[this.typeKey.length() + 2] = 58;
                chars[this.typeKey.length() + 3] = 34;
                this.typeName.getBytes(0, this.typeName.length(), chars, this.typeKey.length() + 4);
                chars[this.typeKey.length() + this.typeName.length() + 4] = 34;
                this.nameWithColonUTF8 = chars;
            }
            jsonWriter.writeNameRaw(this.nameWithColonUTF8);
            return true;
        }
        if (jsonWriter.utf16) {
            if (this.nameWithColonUTF16 == null) {
                final char[] chars2 = new char[this.typeKey.length() + this.typeName.length() + 5];
                chars2[0] = '\"';
                this.typeKey.getChars(0, this.typeKey.length(), chars2, 1);
                chars2[this.typeKey.length() + 1] = '\"';
                chars2[this.typeKey.length() + 2] = ':';
                chars2[this.typeKey.length() + 3] = '\"';
                this.typeName.getChars(0, this.typeName.length(), chars2, this.typeKey.length() + 4);
                chars2[this.typeKey.length() + this.typeName.length() + 4] = '\"';
                this.nameWithColonUTF16 = chars2;
            }
            jsonWriter.writeNameRaw(this.nameWithColonUTF16);
            return true;
        }
        if (jsonWriter.jsonb) {
            if (this.typeKeyJSONB == null) {
                this.typeKeyJSONB = JSONB.toBytes(this.typeKey);
            }
            jsonWriter.writeRaw(this.typeKeyJSONB);
            jsonWriter.writeRaw(this.typeNameJSONB);
            return true;
        }
        jsonWriter.writeString(this.typeKey);
        jsonWriter.writeColon();
        jsonWriter.writeString(this.typeName);
        return true;
    }
    
    @Override
    public void writeWithFilter(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        if (jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
            if (jsonWriter.jsonb) {
                this.writeClassInfo(jsonWriter);
                jsonWriter.startObject();
            }
            else {
                jsonWriter.startObject();
                this.writeTypeInfo(jsonWriter);
            }
        }
        else {
            jsonWriter.startObject();
        }
        final JSONWriter.Context context = jsonWriter.context;
        final long features2 = context.getFeatures() | features;
        final boolean refDetect = (features2 & JSONWriter.Feature.ReferenceDetection.mask) != 0x0L;
        final boolean ignoreNonFieldGetter = (features2 & JSONWriter.Feature.IgnoreNonFieldGetter.mask) != 0x0L;
        final BeforeFilter beforeFilter = context.getBeforeFilter();
        if (beforeFilter != null) {
            beforeFilter.writeBefore(jsonWriter, object);
        }
        PropertyPreFilter propertyPreFilter = context.getPropertyPreFilter();
        if (propertyPreFilter == null) {
            propertyPreFilter = this.propertyPreFilter;
        }
        NameFilter nameFilter = context.getNameFilter();
        if (nameFilter == null) {
            nameFilter = this.nameFilter;
        }
        else if (this.nameFilter != null) {
            nameFilter = NameFilter.compose(this.nameFilter, nameFilter);
        }
        final ContextNameFilter contextNameFilter = context.getContextNameFilter();
        ValueFilter valueFilter = context.getValueFilter();
        if (valueFilter == null) {
            valueFilter = this.valueFilter;
        }
        else if (this.valueFilter != null) {
            valueFilter = ValueFilter.compose(this.valueFilter, valueFilter);
        }
        final ContextValueFilter contextValueFilter = context.getContextValueFilter();
        PropertyFilter propertyFilter = context.getPropertyFilter();
        if (propertyFilter == null) {
            propertyFilter = this.propertyFilter;
        }
        final LabelFilter labelFilter = context.getLabelFilter();
        for (int i = 0; i < this.fieldWriters.size(); ++i) {
            final FieldWriter fieldWriter = this.fieldWriters.get(i);
            Field field = fieldWriter.field;
            if (!ignoreNonFieldGetter || fieldWriter.method == null || (fieldWriter.features & 0x10000000000000L) != 0x0L) {
                final String fieldWriterFieldName = fieldWriter.fieldName;
                if (propertyPreFilter == null || propertyPreFilter.process(jsonWriter, object, fieldWriterFieldName)) {
                    if (labelFilter != null) {
                        final String label = fieldWriter.label;
                        if (label != null && !label.isEmpty() && !labelFilter.apply(label)) {
                            continue;
                        }
                    }
                    if (nameFilter == null && propertyFilter == null && contextValueFilter == null && contextNameFilter == null && valueFilter == null) {
                        fieldWriter.write(jsonWriter, object);
                    }
                    else {
                        Object fieldValue;
                        try {
                            fieldValue = fieldWriter.getFieldValue(object);
                        }
                        catch (Throwable e) {
                            if ((context.getFeatures() & JSONWriter.Feature.IgnoreErrorGetter.mask) != 0x0L) {
                                continue;
                            }
                            throw e;
                        }
                        if (fieldValue != null || jsonWriter.isWriteNulls()) {
                            if (!refDetect) {
                                if ("this$0".equals(fieldWriterFieldName) || "this$1".equals(fieldWriterFieldName)) {
                                    continue;
                                }
                                if ("this$2".equals(fieldWriterFieldName)) {
                                    continue;
                                }
                            }
                            BeanContext beanContext = null;
                            String filteredName = fieldWriterFieldName;
                            if (nameFilter != null) {
                                filteredName = nameFilter.process(object, filteredName, fieldValue);
                            }
                            if (contextNameFilter != null && beanContext == null) {
                                if (field == null && fieldWriter.method != null) {
                                    field = BeanUtils.getDeclaredField(this.objectClass, fieldWriter.fieldName);
                                }
                                beanContext = new BeanContext(this.objectClass, fieldWriter.method, field, fieldWriter.fieldName, fieldWriter.label, fieldWriter.fieldClass, fieldWriter.fieldType, fieldWriter.features, fieldWriter.format);
                                filteredName = contextNameFilter.process(beanContext, object, filteredName, fieldValue);
                            }
                            if (propertyFilter == null || propertyFilter.apply(object, fieldWriterFieldName, fieldValue)) {
                                final boolean nameChanged = filteredName != null && filteredName != fieldWriterFieldName;
                                Object filteredValue = fieldValue;
                                if (valueFilter != null) {
                                    filteredValue = valueFilter.apply(object, fieldWriterFieldName, fieldValue);
                                }
                                if (contextValueFilter != null) {
                                    if (beanContext == null) {
                                        if (field == null && fieldWriter.method != null) {
                                            field = BeanUtils.getDeclaredField(this.objectClass, fieldWriter.fieldName);
                                        }
                                        beanContext = new BeanContext(this.objectClass, fieldWriter.method, field, fieldWriter.fieldName, fieldWriter.label, fieldWriter.fieldClass, fieldWriter.fieldType, fieldWriter.features, fieldWriter.format);
                                    }
                                    filteredValue = contextValueFilter.process(beanContext, object, filteredName, filteredValue);
                                }
                                if (filteredValue != fieldValue) {
                                    if (nameChanged) {
                                        jsonWriter.writeName(filteredName);
                                        jsonWriter.writeColon();
                                    }
                                    else {
                                        fieldWriter.writeFieldName(jsonWriter);
                                    }
                                    if (filteredValue == null) {
                                        jsonWriter.writeNull();
                                    }
                                    else {
                                        final ObjectWriter fieldValueWriter = fieldWriter.getObjectWriter(jsonWriter, filteredValue.getClass());
                                        fieldValueWriter.write(jsonWriter, filteredValue, fieldName, fieldType, features);
                                    }
                                }
                                else if (!nameChanged) {
                                    fieldWriter.write(jsonWriter, object);
                                }
                                else {
                                    jsonWriter.writeName(filteredName);
                                    jsonWriter.writeColon();
                                    if (fieldValue == null) {
                                        final ObjectWriter fieldValueWriter = fieldWriter.getObjectWriter(jsonWriter, fieldWriter.fieldClass);
                                        fieldValueWriter.write(jsonWriter, null, fieldName, fieldType, features);
                                    }
                                    else {
                                        final ObjectWriter fieldValueWriter = fieldWriter.getObjectWriter(jsonWriter, fieldValue.getClass());
                                        fieldValueWriter.write(jsonWriter, fieldValue, fieldName, fieldType, features);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        final AfterFilter afterFilter = context.getAfterFilter();
        if (afterFilter != null) {
            afterFilter.writeAfter(jsonWriter, object);
        }
        jsonWriter.endObject();
    }
    
    public JSONObject toJSONObject(final T object) {
        final JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < this.fieldWriters.size(); ++i) {
            final FieldWriter fieldWriter = this.fieldWriters.get(i);
            Object fieldValue = fieldWriter.getFieldValue(object);
            final String format = fieldWriter.format;
            final Class fieldClass = fieldWriter.fieldClass;
            if (format != null) {
                if (fieldClass == Date.class) {
                    fieldValue = DateUtils.format((Date)fieldValue, format);
                }
                else if (fieldClass == LocalDate.class) {
                    fieldValue = DateUtils.format((LocalDate)fieldValue, format);
                }
                else if (fieldClass == LocalDateTime.class) {
                    fieldValue = DateUtils.format((LocalDateTime)fieldValue, format);
                }
                else if (fieldClass == LocalDate.class) {
                    fieldValue = DateUtils.format((LocalDate)fieldValue, format);
                }
            }
            final long fieldFeatures = fieldWriter.features;
            if ((fieldFeatures & 0x2000000000000L) != 0x0L) {
                if (fieldValue instanceof Map) {
                    jsonObject.putAll((Map<? extends String, ?>)fieldValue);
                }
                else {
                    ObjectWriter fieldObjectWriter = fieldWriter.getInitWriter();
                    if (fieldObjectWriter == null) {
                        fieldObjectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter(fieldClass);
                    }
                    final List<FieldWriter> unwrappedFieldWriters = (List<FieldWriter>)fieldObjectWriter.getFieldWriters();
                    for (int j = 0; j < unwrappedFieldWriters.size(); ++j) {
                        final FieldWriter unwrappedFieldWriter = unwrappedFieldWriters.get(j);
                        final Object unwrappedFieldValue = unwrappedFieldWriter.getFieldValue(fieldValue);
                        jsonObject.put(unwrappedFieldWriter.fieldName, unwrappedFieldValue);
                    }
                }
            }
            else {
                if (fieldValue != null) {
                    final String fieldValueClassName = fieldValue.getClass().getName();
                    if (Collection.class.isAssignableFrom(fieldClass) && (fieldValueClassName.startsWith("java.util.ImmutableCollections$") || fieldValueClassName.startsWith("java.util.Collections$"))) {
                        final Collection collection = (Collection)fieldValue;
                        final JSONArray array = new JSONArray(collection.size());
                        for (final Object item : collection) {
                            array.add(JSON.toJSON(item));
                        }
                        fieldValue = array;
                    }
                }
                jsonObject.put(fieldWriter.fieldName, fieldValue);
            }
        }
        return jsonObject;
    }
    
    @Override
    public String toString() {
        return this.objectClass.getName();
    }
    
    protected void errorOnNoneSerializable() {
        throw new JSONException("not support none serializable class " + this.objectClass.getName());
    }
}
