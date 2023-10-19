// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.filter.AfterFilter;
import com.alibaba.fastjson2.filter.PropertyFilter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;
import com.alibaba.fastjson2.filter.BeforeFilter;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.TreeMap;
import com.alibaba.fastjson2.JSONArray;
import java.util.SortedMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.ParameterizedType;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public final class ObjectWriterImplMap extends ObjectWriterPrimitiveImpl
{
    static final byte[] TYPE_NAME_JSONObject1O;
    static final long TYPE_HASH_JSONObject1O;
    static final ObjectWriterImplMap INSTANCE;
    static final ObjectWriterImplMap INSTANCE_1x;
    final Type objectType;
    final Class objectClass;
    final Type keyType;
    final Type valueType;
    final boolean valueTypeRefDetect;
    volatile ObjectWriter keyWriter;
    volatile ObjectWriter valueWriter;
    final byte[] jsonbTypeInfo;
    final long typeNameHash;
    final long features;
    final boolean jsonObject1;
    final Field jsonObject1InnerMap;
    final long jsonObject1InnerMapOffset;
    final char[] typeInfoUTF16;
    final byte[] typeInfoUTF8;
    
    public ObjectWriterImplMap(final Class objectClass, final long features) {
        this(null, null, objectClass, objectClass, features);
    }
    
    public ObjectWriterImplMap(final Type keyType, final Type valueType, final Class objectClass, final Type objectType, final long features) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.objectClass = objectClass;
        this.objectType = objectType;
        this.features = features;
        if (valueType == null) {
            this.valueTypeRefDetect = true;
        }
        else {
            this.valueTypeRefDetect = !ObjectWriterProvider.isNotReferenceDetect(TypeUtils.getClass(valueType));
        }
        final String typeName = TypeUtils.getTypeName(objectClass);
        final String typeInfoStr = "\"@type\":\"" + objectClass.getName() + "\"";
        this.typeInfoUTF16 = typeInfoStr.toCharArray();
        this.typeInfoUTF8 = typeInfoStr.getBytes(StandardCharsets.UTF_8);
        this.jsonObject1 = "JO1".equals(typeName);
        this.jsonbTypeInfo = JSONB.toBytes(typeName);
        this.typeNameHash = Fnv.hashCode64(typeName);
        long jsonObject1InnerMapOffset = -1L;
        if (this.jsonObject1) {
            this.jsonObject1InnerMap = BeanUtils.getDeclaredField(objectClass, "map");
            if (this.jsonObject1InnerMap != null) {
                this.jsonObject1InnerMap.setAccessible(true);
                jsonObject1InnerMapOffset = JDKUtils.UNSAFE.objectFieldOffset(this.jsonObject1InnerMap);
            }
        }
        else {
            this.jsonObject1InnerMap = null;
        }
        this.jsonObject1InnerMapOffset = jsonObject1InnerMapOffset;
    }
    
    public static ObjectWriterImplMap of(final Class objectClass) {
        if (objectClass == JSONObject.class) {
            return ObjectWriterImplMap.INSTANCE;
        }
        if (objectClass == TypeUtils.CLASS_JSON_OBJECT_1x) {
            return ObjectWriterImplMap.INSTANCE_1x;
        }
        return new ObjectWriterImplMap(null, null, objectClass, objectClass, 0L);
    }
    
    public static ObjectWriterImplMap of(final Type type) {
        final Class objectClass = TypeUtils.getClass(type);
        return new ObjectWriterImplMap(objectClass, 0L);
    }
    
    public static ObjectWriterImplMap of(final Type type, final Class defineClass) {
        Type keyType = null;
        Type valueType = null;
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)type;
            final Type rawType = parameterizedType.getRawType();
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length == 2) {
                keyType = actualTypeArguments[0];
                valueType = actualTypeArguments[1];
            }
        }
        return new ObjectWriterImplMap(keyType, valueType, defineClass, type, 0L);
    }
    
    @Override
    public void writeArrayMappingJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        final Map map = (Map)object;
        jsonWriter.startObject();
        final boolean writeNulls = jsonWriter.isWriteNulls();
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            if (value == null) {
                if (!writeNulls) {
                    continue;
                }
                jsonWriter.writeString(key);
                jsonWriter.writeNull();
            }
            else {
                jsonWriter.writeString(key);
                final Class<?> valueType = value.getClass();
                if (valueType == String.class) {
                    jsonWriter.writeString((String)value);
                }
                else {
                    final ObjectWriter valueWriter = jsonWriter.getObjectWriter(valueType);
                    valueWriter.writeJSONB(jsonWriter, value, key, this.valueType, this.features);
                }
            }
        }
        jsonWriter.endObject();
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if ((fieldType == this.objectType && jsonWriter.isWriteMapTypeInfo(object, this.objectClass, features)) || jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
            boolean ordered = false;
            if (this.jsonObject1InnerMap != null) {
                if (this.jsonObject1InnerMapOffset != -1L) {
                    final Object innerMap = JDKUtils.UNSAFE.getObject(object, this.jsonObject1InnerMapOffset);
                    ordered = (innerMap instanceof LinkedHashMap);
                }
                else {
                    try {
                        final Object innerMap = this.jsonObject1InnerMap.get(object);
                        ordered = (innerMap instanceof LinkedHashMap);
                    }
                    catch (IllegalAccessException ex) {}
                }
            }
            if (ordered) {
                jsonWriter.writeTypeName(ObjectWriterImplMap.TYPE_NAME_JSONObject1O, ObjectWriterImplMap.TYPE_HASH_JSONObject1O);
            }
            else {
                jsonWriter.writeTypeName(this.jsonbTypeInfo, this.typeNameHash);
            }
        }
        final Map map = (Map)object;
        final JSONWriter.Context context = jsonWriter.context;
        jsonWriter.startObject();
        Type fieldValueType = this.valueType;
        if (fieldType == this.objectType) {
            fieldValueType = this.valueType;
        }
        else if (fieldType instanceof ParameterizedType) {
            final Type[] actualTypeArguments = ((ParameterizedType)fieldType).getActualTypeArguments();
            if (actualTypeArguments.length == 2) {
                fieldValueType = actualTypeArguments[1];
            }
        }
        final long contextFeatures = context.getFeatures();
        final boolean writeNulls = (contextFeatures & (JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask)) != 0x0L;
        final boolean fieldBased = (contextFeatures & JSONWriter.Feature.FieldBased.mask) != 0x0L;
        final ObjectWriterProvider provider = context.provider;
        Class itemClass = null;
        ObjectWriter itemWriter = null;
        final boolean contextRefDetect = (contextFeatures & JSONWriter.Feature.ReferenceDetection.mask) != 0x0L;
        int i = 0;
        for (final Map.Entry entry : map.entrySet()) {
            final Object entryKey = entry.getKey();
            final Object value = entry.getValue();
            Label_1119: {
                if (value == null) {
                    if (writeNulls) {
                        if (entryKey instanceof String) {
                            jsonWriter.writeString((String)entryKey);
                        }
                        else {
                            final Class<?> entryKeyClass = entryKey.getClass();
                            final boolean keyRefDetect = contextRefDetect && !ObjectWriterProvider.isNotReferenceDetect(entryKeyClass);
                            String refPath = null;
                            if (keyRefDetect) {
                                jsonWriter.setPath(i, entry);
                                refPath = jsonWriter.setPath("key", entryKey);
                            }
                            if (refPath != null) {
                                jsonWriter.writeReference(refPath);
                            }
                            else {
                                final ObjectWriter keyWriter = provider.getObjectWriter(entryKeyClass, entryKeyClass, fieldBased);
                                keyWriter.writeJSONB(jsonWriter, entryKey, null, null, 0L);
                            }
                            if (keyRefDetect) {
                                jsonWriter.popPath(entry);
                                jsonWriter.popPath(entryKey);
                            }
                        }
                        jsonWriter.writeNull();
                    }
                }
                else {
                    if (entryKey instanceof String || (contextFeatures & JSONWriter.Feature.WriteClassName.mask) == 0x0L) {
                        String key;
                        if (entryKey instanceof String) {
                            key = (String)entryKey;
                        }
                        else {
                            key = entryKey.toString();
                        }
                        if (jsonWriter.symbolTable != null) {
                            jsonWriter.writeSymbol(key);
                            if (value instanceof String) {
                                jsonWriter.writeSymbol((String)value);
                                break Label_1119;
                            }
                        }
                        else {
                            jsonWriter.writeString(key);
                        }
                    }
                    else if (entryKey == null) {
                        jsonWriter.writeNull();
                    }
                    else {
                        if (contextRefDetect) {
                            jsonWriter.config(JSONWriter.Feature.ReferenceDetection, false);
                        }
                        final Class<?> entryKeyClass = entryKey.getClass();
                        final ObjectWriter keyWriter2 = provider.getObjectWriter(entryKeyClass, entryKeyClass, fieldBased);
                        keyWriter2.writeJSONB(jsonWriter, entryKey, null, null, 0L);
                        if (contextRefDetect) {
                            jsonWriter.config(JSONWriter.Feature.ReferenceDetection, true);
                        }
                    }
                    final Class<?> valueClass = value.getClass();
                    if (valueClass == String.class) {
                        jsonWriter.writeString((String)value);
                    }
                    else if (valueClass == Integer.class) {
                        jsonWriter.writeInt32((Integer)value);
                    }
                    else if (valueClass == Long.class) {
                        jsonWriter.writeInt64((Long)value);
                    }
                    else {
                        boolean valueRefDetecChanged = false;
                        boolean valueRefDetect;
                        if (valueClass == this.valueType) {
                            valueRefDetect = (contextRefDetect && this.valueTypeRefDetect);
                        }
                        else {
                            valueRefDetect = (contextRefDetect && !ObjectWriterProvider.isNotReferenceDetect(valueClass));
                        }
                        if (valueRefDetect) {
                            if (value == object) {
                                jsonWriter.writeReference("..");
                                break Label_1119;
                            }
                            String refPath2;
                            if (entryKey instanceof String) {
                                refPath2 = jsonWriter.setPath((String)entryKey, value);
                            }
                            else if (ObjectWriterProvider.isPrimitiveOrEnum(entryKey.getClass())) {
                                refPath2 = jsonWriter.setPath(entryKey.toString(), value);
                            }
                            else if (map.size() != 1 && !(map instanceof SortedMap) && !(map instanceof LinkedHashMap)) {
                                refPath2 = null;
                                jsonWriter.config(JSONWriter.Feature.ReferenceDetection, false);
                                valueRefDetecChanged = true;
                                valueRefDetect = false;
                            }
                            else {
                                refPath2 = jsonWriter.setPath(i, value);
                            }
                            if (refPath2 != null) {
                                jsonWriter.writeReference(refPath2);
                                jsonWriter.popPath(value);
                                break Label_1119;
                            }
                        }
                        ObjectWriter valueWriter;
                        if (valueClass == this.valueType && this.valueWriter != null) {
                            valueWriter = this.valueWriter;
                        }
                        else if (itemClass == valueClass) {
                            valueWriter = itemWriter;
                        }
                        else {
                            if (valueClass == JSONObject.class) {
                                valueWriter = ObjectWriterImplMap.INSTANCE;
                            }
                            else if (valueClass == TypeUtils.CLASS_JSON_OBJECT_1x) {
                                valueWriter = ObjectWriterImplMap.INSTANCE_1x;
                            }
                            else if (valueClass == JSONArray.class) {
                                valueWriter = ObjectWriterImplList.INSTANCE;
                            }
                            else if (valueClass == TypeUtils.CLASS_JSON_ARRAY_1x) {
                                valueWriter = ObjectWriterImplList.INSTANCE;
                            }
                            else {
                                valueWriter = provider.getObjectWriter(valueClass, valueClass, fieldBased);
                            }
                            if (itemWriter == null) {
                                itemWriter = valueWriter;
                                itemClass = valueClass;
                            }
                            if (valueClass == this.valueType) {
                                this.valueWriter = valueWriter;
                            }
                        }
                        valueWriter.writeJSONB(jsonWriter, value, entryKey, fieldValueType, this.features);
                        if (valueRefDetecChanged) {
                            jsonWriter.config(JSONWriter.Feature.ReferenceDetection, true);
                        }
                        else if (valueRefDetect) {
                            jsonWriter.popPath(value);
                        }
                    }
                }
            }
            ++i;
        }
        jsonWriter.endObject();
    }
    
    @Override
    public boolean writeTypeInfo(final JSONWriter jsonWriter) {
        if (jsonWriter.utf8) {
            jsonWriter.writeNameRaw(this.typeInfoUTF8);
        }
        else {
            jsonWriter.writeNameRaw(this.typeInfoUTF16);
        }
        return true;
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, long features) {
        if (jsonWriter.jsonb) {
            this.writeJSONB(jsonWriter, object, fieldName, fieldType, features);
            return;
        }
        if (this.hasFilter(jsonWriter)) {
            this.writeWithFilter(jsonWriter, object, fieldName, fieldType, features);
            return;
        }
        final boolean refDetect = jsonWriter.isRefDetect();
        jsonWriter.startObject();
        if ((fieldType == this.objectType && jsonWriter.isWriteMapTypeInfo(object, this.objectClass, features)) || jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
            this.writeTypeInfo(jsonWriter);
        }
        Map map = (Map)object;
        features |= jsonWriter.getFeatures();
        if ((features & JSONWriter.Feature.MapSortField.mask) != 0x0L && !(map instanceof SortedMap) && map.getClass() != LinkedHashMap.class) {
            map = new TreeMap(map);
        }
        final ObjectWriterProvider provider = jsonWriter.context.provider;
        for (final Map.Entry entry : map.entrySet()) {
            final Object value = entry.getValue();
            final Object key = entry.getKey();
            if (value == null) {
                if ((features & JSONWriter.Feature.WriteNulls.mask) == 0x0L) {
                    continue;
                }
                if (key == null) {
                    jsonWriter.writeName("null");
                }
                else if (key instanceof String) {
                    jsonWriter.writeName((String)key);
                }
                else if ((features & (JSONWriter.Feature.WriteNonStringKeyAsString.mask | JSONWriter.Feature.BrowserCompatible.mask)) != 0x0L) {
                    jsonWriter.writeName(key.toString());
                }
                else if (key instanceof Integer) {
                    jsonWriter.writeName((int)key);
                }
                else if (key instanceof Long) {
                    jsonWriter.writeName((long)key);
                }
                else {
                    jsonWriter.writeNameAny(key);
                }
                jsonWriter.writeColon();
                jsonWriter.writeNull();
            }
            else {
                if ((features & JSONWriter.Feature.NotWriteEmptyArray.mask) != 0x0L) {
                    if (value instanceof Collection && ((Collection)value).isEmpty()) {
                        continue;
                    }
                    if (value.getClass().isArray() && Array.getLength(value) == 0) {
                        continue;
                    }
                }
                String strKey = null;
                if (this.keyWriter != null) {
                    this.keyWriter.write(jsonWriter, key, null, null, 0L);
                }
                else if (key == null) {
                    jsonWriter.writeName("null");
                }
                else if (key instanceof String) {
                    jsonWriter.writeName(strKey = (String)key);
                }
                else if ((features & (JSONWriter.Feature.WriteNonStringKeyAsString.mask | JSONWriter.Feature.BrowserCompatible.mask)) != 0x0L) {
                    jsonWriter.writeName(strKey = key.toString());
                }
                else if (key instanceof Integer) {
                    jsonWriter.writeName((int)key);
                }
                else if (key instanceof Long) {
                    final long longKey = (long)key;
                    jsonWriter.writeName(longKey);
                }
                else {
                    jsonWriter.writeNameAny(key);
                }
                jsonWriter.writeColon();
                final Class<?> valueClass = value.getClass();
                if (valueClass == String.class) {
                    jsonWriter.writeString((String)value);
                }
                else if (valueClass == Integer.class) {
                    jsonWriter.writeInt32((Integer)value);
                }
                else if (valueClass == Long.class) {
                    if ((provider.userDefineMask & 0x4L) == 0x0L) {
                        jsonWriter.writeInt64((Long)value);
                    }
                    else {
                        final ObjectWriter valueWriter = jsonWriter.getObjectWriter(valueClass);
                        valueWriter.write(jsonWriter, value, strKey, Long.class, features);
                    }
                }
                else if (valueClass == Boolean.class) {
                    jsonWriter.writeBool((boolean)value);
                }
                else if (valueClass == BigDecimal.class) {
                    if ((provider.userDefineMask & 0x8L) == 0x0L) {
                        jsonWriter.writeDecimal((BigDecimal)value, features, null);
                    }
                    else {
                        final ObjectWriter valueWriter = jsonWriter.getObjectWriter(valueClass);
                        valueWriter.write(jsonWriter, value, key, this.valueType, this.features);
                    }
                }
                else {
                    ObjectWriter valueWriter2;
                    boolean isPrimitiveOrEnum;
                    if (valueClass == this.valueType) {
                        if (this.valueWriter != null) {
                            valueWriter2 = this.valueWriter;
                        }
                        else {
                            final ObjectWriter objectWriter = jsonWriter.getObjectWriter(valueClass);
                            this.valueWriter = objectWriter;
                            valueWriter2 = objectWriter;
                        }
                        isPrimitiveOrEnum = ObjectWriterProvider.isPrimitiveOrEnum(value.getClass());
                    }
                    else if (valueClass == JSONObject.class) {
                        valueWriter2 = ObjectWriterImplMap.INSTANCE;
                        isPrimitiveOrEnum = false;
                    }
                    else if (valueClass == TypeUtils.CLASS_JSON_OBJECT_1x) {
                        valueWriter2 = ObjectWriterImplMap.INSTANCE_1x;
                        isPrimitiveOrEnum = false;
                    }
                    else if (valueClass == JSONArray.class) {
                        valueWriter2 = ObjectWriterImplList.INSTANCE;
                        isPrimitiveOrEnum = false;
                    }
                    else if (valueClass == TypeUtils.CLASS_JSON_ARRAY_1x) {
                        valueWriter2 = ObjectWriterImplList.INSTANCE;
                        isPrimitiveOrEnum = false;
                    }
                    else {
                        valueWriter2 = jsonWriter.getObjectWriter(valueClass);
                        isPrimitiveOrEnum = ObjectWriterProvider.isPrimitiveOrEnum(value.getClass());
                    }
                    final boolean valueRefDetect = refDetect && strKey != null && !isPrimitiveOrEnum;
                    if (valueRefDetect) {
                        if (value == object) {
                            jsonWriter.writeReference("..");
                            continue;
                        }
                        final String refPath = jsonWriter.setPath(strKey, value);
                        if (refPath != null) {
                            jsonWriter.writeReference(refPath);
                            jsonWriter.popPath(value);
                            continue;
                        }
                    }
                    valueWriter2.write(jsonWriter, value, key, this.valueType, this.features);
                    if (!valueRefDetect) {
                        continue;
                    }
                    jsonWriter.popPath(value);
                }
            }
        }
        jsonWriter.endObject();
    }
    
    @Override
    public void writeWithFilter(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.startObject();
        Map map = (Map)object;
        features |= jsonWriter.getFeatures();
        if ((features & JSONWriter.Feature.MapSortField.mask) != 0x0L && !(map instanceof SortedMap) && map.getClass() != LinkedHashMap.class) {
            map = new TreeMap(map);
        }
        final JSONWriter.Context context = jsonWriter.context;
        final BeforeFilter beforeFilter = context.getBeforeFilter();
        if (beforeFilter != null) {
            beforeFilter.writeBefore(jsonWriter, object);
        }
        final PropertyPreFilter propertyPreFilter = context.getPropertyPreFilter();
        final NameFilter nameFilter = context.getNameFilter();
        final ValueFilter valueFilter = context.getValueFilter();
        final PropertyFilter propertyFilter = context.getPropertyFilter();
        final AfterFilter afterFilter = context.getAfterFilter();
        final boolean writeNulls = context.isEnabled(JSONWriter.Feature.WriteNulls.mask);
        for (final Map.Entry entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value == null && !writeNulls) {
                continue;
            }
            final Object entryKey = entry.getKey();
            String key;
            if (entryKey == null) {
                key = null;
            }
            else {
                key = entryKey.toString();
            }
            if (propertyPreFilter != null && !propertyPreFilter.process(jsonWriter, object, key)) {
                continue;
            }
            if (nameFilter != null) {
                key = nameFilter.process(object, key, value);
            }
            if (propertyFilter != null && !propertyFilter.apply(object, key, value)) {
                continue;
            }
            if (valueFilter != null) {
                value = valueFilter.apply(object, key, value);
            }
            if (value == null && (jsonWriter.getFeatures(features) & JSONWriter.Feature.WriteNulls.mask) == 0x0L) {
                continue;
            }
            jsonWriter.writeName(key);
            jsonWriter.writeColon();
            if (value == null) {
                jsonWriter.writeNull();
            }
            else {
                final Class<?> valueType = value.getClass();
                final ObjectWriter valueWriter = jsonWriter.getObjectWriter(valueType);
                valueWriter.write(jsonWriter, value, fieldName, fieldType, this.features);
            }
        }
        if (afterFilter != null) {
            afterFilter.writeAfter(jsonWriter, object);
        }
        jsonWriter.endObject();
    }
    
    static {
        TYPE_NAME_JSONObject1O = JSONB.toBytes("JO10");
        TYPE_HASH_JSONObject1O = Fnv.hashCode64("JO10");
        INSTANCE = new ObjectWriterImplMap(String.class, Object.class, JSONObject.class, JSONObject.class, 0L);
        if (TypeUtils.CLASS_JSON_OBJECT_1x == null) {
            INSTANCE_1x = null;
        }
        else {
            INSTANCE_1x = new ObjectWriterImplMap(String.class, Object.class, TypeUtils.CLASS_JSON_OBJECT_1x, TypeUtils.CLASS_JSON_OBJECT_1x, 0L);
        }
    }
}
