// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.Arrays;
import java.util.function.Supplier;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ObjectReaderNoneDefaultConstructor<T> extends ObjectReaderAdapter<T>
{
    final String[] paramNames;
    final FieldReader[] setterFieldReaders;
    private final Function<Map<Long, Object>, T> creator;
    
    public ObjectReaderNoneDefaultConstructor(final Class objectClass, final String typeKey, final String typeName, final long features, final Function<Map<Long, Object>, T> creator, final List<Constructor> alternateConstructors, final String[] paramNames, final FieldReader[] paramFieldReaders, final FieldReader[] setterFieldReaders, final Class[] seeAlso, final String[] seeAlsoNames) {
        super(objectClass, typeKey, typeName, features, null, null, null, seeAlso, seeAlsoNames, (FieldReader[])concat(paramFieldReaders, setterFieldReaders));
        this.paramNames = paramNames;
        this.creator = creator;
        this.setterFieldReaders = setterFieldReaders;
    }
    
    static FieldReader[] concat(FieldReader[] a, final FieldReader[] b) {
        if (b == null) {
            return a;
        }
        final int alen = a.length;
        a = Arrays.copyOf(a, alen + b.length);
        System.arraycopy(b, 0, a, alen, b.length);
        return a;
    }
    
    @Override
    public T createInstanceNoneDefaultConstructor(final Map<Long, Object> values) {
        return this.creator.apply(values);
    }
    
    @Override
    public T readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (!this.serializable) {
            jsonReader.errorOnNoneSerializable(this.objectClass);
        }
        final byte type = jsonReader.getType();
        if (type == -81) {
            jsonReader.next();
            return null;
        }
        if (type == -110) {
            final ObjectReader objectReader = jsonReader.checkAutoType(this.objectClass, this.typeNameHash, this.features | features);
            if (objectReader != null && objectReader != this) {
                return objectReader.readJSONBObject(jsonReader, fieldType, fieldName, features);
            }
        }
        LinkedHashMap<Long, Object> valueMap = null;
        Map<Long, String> references = null;
        if (jsonReader.isArray()) {
            if (!jsonReader.isSupportBeanArray()) {
                throw new JSONException(jsonReader.info("expect object, but " + JSONB.typeName(jsonReader.getType())));
            }
            for (int entryCnt = jsonReader.startArray(), i = 0; i < entryCnt; ++i) {
                final FieldReader fieldReader = this.fieldReaders[i];
                final Object fieldValue = fieldReader.readFieldValue(jsonReader);
                if (valueMap == null) {
                    valueMap = new LinkedHashMap<Long, Object>();
                }
                valueMap.put(fieldReader.fieldNameHash, fieldValue);
            }
        }
        else {
            jsonReader.nextIfObjectStart();
            int j = 0;
            while (!jsonReader.nextIfObjectEnd()) {
                final long hashCode = jsonReader.readFieldNameHashCode();
                if (hashCode != 0L) {
                    if (hashCode == ObjectReaderNoneDefaultConstructor.HASH_TYPE && j == 0) {
                        final long typeHash = jsonReader.readTypeHashCode();
                        final JSONReader.Context context = jsonReader.getContext();
                        ObjectReader autoTypeObjectReader = context.getObjectReaderAutoType(typeHash);
                        if (autoTypeObjectReader == null) {
                            final String typeName = jsonReader.getString();
                            autoTypeObjectReader = context.getObjectReaderAutoType(typeName, this.objectClass);
                            if (autoTypeObjectReader == null) {
                                throw new JSONException(jsonReader.info("auotype not support : " + typeName));
                            }
                        }
                        final Object object = autoTypeObjectReader.readJSONBObject(jsonReader, fieldType, fieldName, features);
                        jsonReader.nextIfComma();
                        return (T)object;
                    }
                    final FieldReader fieldReader2 = this.getFieldReader(hashCode);
                    if (fieldReader2 == null) {
                        this.processExtra(jsonReader, null);
                    }
                    else if (jsonReader.isReference()) {
                        jsonReader.next();
                        final String reference = jsonReader.readString();
                        if (references == null) {
                            references = new HashMap<Long, String>();
                        }
                        references.put(hashCode, reference);
                    }
                    else {
                        final Object fieldValue2 = fieldReader2.readFieldValue(jsonReader);
                        if (valueMap == null) {
                            valueMap = new LinkedHashMap<Long, Object>();
                        }
                        valueMap.put(fieldReader2.fieldNameHash, fieldValue2);
                    }
                }
                ++j;
            }
        }
        final Map<Long, Object> args = (valueMap == null) ? Collections.emptyMap() : valueMap;
        final T object2 = this.createInstanceNoneDefaultConstructor(args);
        if (this.setterFieldReaders != null) {
            for (int k = 0; k < this.setterFieldReaders.length; ++k) {
                final FieldReader fieldReader2 = this.setterFieldReaders[k];
                final Object fieldValue2 = args.get(fieldReader2.fieldNameHash);
                fieldReader2.accept(object2, fieldValue2);
            }
        }
        if (references != null) {
            for (final Map.Entry<Long, String> entry : references.entrySet()) {
                final Long hashCode2 = entry.getKey();
                final String reference = entry.getValue();
                final FieldReader fieldReader3 = this.getFieldReader(hashCode2);
                if ("..".equals(reference)) {
                    fieldReader3.accept(object2, object2);
                }
                else {
                    fieldReader3.addResolveTask(jsonReader, object2, reference);
                }
            }
        }
        return object2;
    }
    
    @Override
    public T readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (!this.serializable) {
            jsonReader.errorOnNoneSerializable(this.objectClass);
        }
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, 0L);
        }
        if (!jsonReader.isSupportBeanArray(features | this.features) || !jsonReader.nextIfArrayStart()) {
            final boolean objectStart = jsonReader.nextIfObjectStart();
            if (!objectStart) {
                if (jsonReader.isTypeRedirect()) {
                    jsonReader.setTypeRedirect(false);
                }
                else if (jsonReader.nextIfNullOrEmptyString()) {
                    return null;
                }
            }
            IdentityHashMap<FieldReader, String> refMap = null;
            final JSONReader.Context context = jsonReader.getContext();
            final long featuresAll = this.features | features | context.getFeatures();
            LinkedHashMap<Long, Object> valueMap = null;
            int i = 0;
            while (!jsonReader.nextIfObjectEnd()) {
                final long hashCode = jsonReader.readFieldNameHashCode();
                if (hashCode != 0L) {
                    if (hashCode == this.typeKeyHashCode && i == 0) {
                        final long typeHash = jsonReader.readTypeHashCode();
                        if (typeHash != this.typeNameHash) {
                            final boolean supportAutoType = (featuresAll & JSONReader.Feature.SupportAutoType.mask) != 0x0L;
                            ObjectReader autoTypeObjectReader;
                            if (supportAutoType) {
                                autoTypeObjectReader = jsonReader.getObjectReaderAutoType(typeHash, this.objectClass, this.features);
                            }
                            else {
                                final String typeName = jsonReader.getString();
                                autoTypeObjectReader = context.getObjectReaderAutoType(typeName, this.objectClass);
                            }
                            if (autoTypeObjectReader == null) {
                                final String typeName = jsonReader.getString();
                                autoTypeObjectReader = context.getObjectReaderAutoType(typeName, this.objectClass, this.features);
                            }
                            if (autoTypeObjectReader != null) {
                                final Object object = autoTypeObjectReader.readObject(jsonReader, fieldType, fieldName, 0L);
                                jsonReader.nextIfComma();
                                return (T)object;
                            }
                        }
                    }
                    else {
                        FieldReader fieldReader = this.getFieldReader(hashCode);
                        if (fieldReader == null && (featuresAll & JSONReader.Feature.SupportSmartMatch.mask) != 0x0L) {
                            final long hashCodeLCase = jsonReader.getNameHashCodeLCase();
                            fieldReader = this.getFieldReaderLCase(hashCodeLCase);
                        }
                        if (fieldReader == null) {
                            this.processExtra(jsonReader, null);
                        }
                        else if (jsonReader.isReference()) {
                            final String ref = jsonReader.readReference();
                            if (refMap == null) {
                                refMap = new IdentityHashMap<FieldReader, String>();
                            }
                            refMap.put(fieldReader, ref);
                        }
                        else {
                            final Object fieldValue = fieldReader.readFieldValue(jsonReader);
                            if (valueMap == null) {
                                valueMap = new LinkedHashMap<Long, Object>();
                            }
                            long hash;
                            if (fieldReader instanceof FieldReaderObjectParam) {
                                hash = ((FieldReaderObjectParam)fieldReader).paramNameHash;
                            }
                            else {
                                hash = fieldReader.fieldNameHash;
                            }
                            valueMap.put(hash, fieldValue);
                        }
                    }
                }
                ++i;
            }
            final Map<Long, Object> argsMap = (valueMap == null) ? Collections.emptyMap() : valueMap;
            final T object2 = this.creator.apply(argsMap);
            if (this.setterFieldReaders != null && valueMap != null) {
                for (int j = 0; j < this.setterFieldReaders.length; ++j) {
                    final FieldReader fieldReader = this.setterFieldReaders[j];
                    final Object fieldValue = valueMap.get(fieldReader.fieldNameHash);
                    if (fieldValue != null) {
                        fieldReader.accept(object2, fieldValue);
                    }
                }
            }
            if (refMap != null) {
                for (final Map.Entry<FieldReader, String> entry : refMap.entrySet()) {
                    final FieldReader fieldReader2 = entry.getKey();
                    final String reference = entry.getValue();
                    fieldReader2.addResolveTask(jsonReader, object2, reference);
                }
            }
            jsonReader.nextIfComma();
            return object2;
        }
        LinkedHashMap<Long, Object> valueMap2 = null;
        for (int k = 0; k < this.fieldReaders.length; ++k) {
            final FieldReader fieldReader3 = this.fieldReaders[k];
            final Object fieldValue2 = fieldReader3.readFieldValue(jsonReader);
            if (valueMap2 == null) {
                valueMap2 = new LinkedHashMap<Long, Object>();
            }
            final long hash2 = fieldReader3.fieldNameHash;
            valueMap2.put(hash2, fieldValue2);
        }
        if (!jsonReader.nextIfArrayEnd()) {
            throw new JSONException(jsonReader.info("array not end, " + jsonReader.current()));
        }
        jsonReader.nextIfComma();
        return this.createInstanceNoneDefaultConstructor((valueMap2 == null) ? Collections.emptyMap() : valueMap2);
    }
    
    public T readFromCSV(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (!this.serializable) {
            jsonReader.errorOnNoneSerializable(this.objectClass);
        }
        final LinkedHashMap<Long, Object> valueMap = new LinkedHashMap<Long, Object>();
        for (int i = 0; i < this.fieldReaders.length; ++i) {
            final FieldReader fieldReader = this.fieldReaders[i];
            final Object fieldValue = fieldReader.readFieldValue(jsonReader);
            valueMap.put(fieldReader.fieldNameHash, fieldValue);
        }
        jsonReader.nextIfMatch('\n');
        return this.createInstanceNoneDefaultConstructor(valueMap);
    }
    
    @Override
    public T createInstance(final Collection collection) {
        int index = 0;
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final LinkedHashMap<Long, Object> valueMap = new LinkedHashMap<Long, Object>();
        for (Object fieldValue : collection) {
            if (index >= this.fieldReaders.length) {
                break;
            }
            final FieldReader fieldReader = this.fieldReaders[index];
            if (fieldValue != null) {
                final Class<?> valueClass = fieldValue.getClass();
                final Class fieldClass = fieldReader.fieldClass;
                if (valueClass != fieldClass) {
                    final Function typeConvert = provider.getTypeConvert(valueClass, fieldClass);
                    if (typeConvert != null) {
                        fieldValue = typeConvert.apply(fieldValue);
                    }
                }
            }
            long hash;
            if (fieldReader instanceof FieldReaderObjectParam) {
                hash = ((FieldReaderObjectParam)fieldReader).paramNameHash;
            }
            else {
                hash = fieldReader.fieldNameHash;
            }
            valueMap.put(hash, fieldValue);
            ++index;
        }
        return this.createInstanceNoneDefaultConstructor(valueMap);
    }
    
    @Override
    public T createInstance(final Map map, final long features) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final Object typeKey = map.get(this.getTypeKey());
        if (typeKey instanceof String) {
            final String typeName = (String)typeKey;
            final long typeHash = Fnv.hashCode64(typeName);
            ObjectReader<T> reader = null;
            if ((features & JSONReader.Feature.SupportAutoType.mask) != 0x0L) {
                reader = (ObjectReader<T>)this.autoType(provider, typeHash);
            }
            if (reader == null) {
                reader = (ObjectReader<T>)provider.getObjectReader(typeName, this.getObjectClass(), features | this.getFeatures());
            }
            if (reader != this && reader != null) {
                return reader.createInstance(map, features);
            }
        }
        LinkedHashMap<Long, Object> valueMap = null;
        for (final Map.Entry entry : map.entrySet()) {
            final String fieldName = entry.getKey().toString();
            Object fieldValue = entry.getValue();
            final FieldReader fieldReader = this.getFieldReader(fieldName);
            if (fieldReader != null) {
                if (fieldValue != null) {
                    final Class<?> valueClass = fieldValue.getClass();
                    final Class fieldClass = fieldReader.fieldClass;
                    if (valueClass != fieldClass) {
                        final Function typeConvert = provider.getTypeConvert(valueClass, fieldClass);
                        if (typeConvert != null) {
                            fieldValue = typeConvert.apply(fieldValue);
                        }
                    }
                }
                if (valueMap == null) {
                    valueMap = new LinkedHashMap<Long, Object>();
                }
                long hash;
                if (fieldReader instanceof FieldReaderObjectParam) {
                    hash = ((FieldReaderObjectParam)fieldReader).paramNameHash;
                }
                else {
                    hash = fieldReader.fieldNameHash;
                }
                valueMap.put(hash, fieldValue);
            }
        }
        final T object = this.createInstanceNoneDefaultConstructor((valueMap == null) ? Collections.emptyMap() : valueMap);
        for (int i = 0; i < this.setterFieldReaders.length; ++i) {
            final FieldReader fieldReader2 = this.setterFieldReaders[i];
            Object fieldValue = map.get(fieldReader2.fieldName);
            if (fieldValue != null) {
                final Class<?> valueClass2 = fieldValue.getClass();
                final Class fieldClass2 = fieldReader2.fieldClass;
                if (valueClass2 != fieldClass2) {
                    final Function typeConvert2 = provider.getTypeConvert(valueClass2, fieldClass2);
                    if (typeConvert2 != null) {
                        fieldValue = typeConvert2.apply(fieldValue);
                    }
                    else if (fieldValue instanceof Map) {
                        final ObjectReader objectReader = fieldReader2.getObjectReader(JSONFactory.createReadContext(provider, new JSONReader.Feature[0]));
                        fieldValue = objectReader.createInstance((Map)fieldValue, features | fieldReader2.features);
                    }
                }
                fieldReader2.accept(object, fieldValue);
            }
        }
        return object;
    }
}
