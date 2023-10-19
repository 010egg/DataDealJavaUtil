// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONB;
import java.util.Iterator;
import java.util.Collection;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Arrays;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.BeanUtils;
import java.util.function.Function;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.function.Supplier;
import java.util.Map;
import java.lang.reflect.Constructor;

public class ObjectReaderAdapter<T> extends ObjectReaderBean<T>
{
    protected final String typeKey;
    protected final long typeKeyHashCode;
    protected final FieldReader[] fieldReaders;
    final long[] hashCodes;
    final short[] mapping;
    final long[] hashCodesLCase;
    final short[] mappingLCase;
    final Constructor constructor;
    volatile boolean instantiationError;
    final Class[] seeAlso;
    final String[] seeAlsoNames;
    final Class seeAlsoDefault;
    final Map<Long, Class> seeAlsoMapping;
    
    public ObjectReaderAdapter(final Class objectClass, final Supplier<T> creator, final FieldReader... fieldReaders) {
        this(objectClass, null, null, 0L, null, creator, null, (FieldReader[])fieldReaders);
    }
    
    public ObjectReaderAdapter(final Class objectClass, final String typeKey, final String typeName, final long features, final JSONSchema schema, final Supplier<T> creator, final Function buildFunction, final FieldReader... fieldReaders) {
        this(objectClass, typeKey, typeName, features, schema, creator, buildFunction, null, null, (Class)null, (FieldReader[])fieldReaders);
    }
    
    public ObjectReaderAdapter(final Class objectClass, final String typeKey, final String typeName, final long features, final Supplier<T> creator, final Function buildFunction, final FieldReader... fieldReaders) {
        this(objectClass, typeKey, typeName, features, null, creator, buildFunction, (FieldReader[])fieldReaders);
    }
    
    public ObjectReaderAdapter(final Class objectClass, final String typeKey, final String typeName, final long features, final JSONSchema schema, final Supplier<T> creator, final Function buildFunction, final Class[] seeAlso, final String[] seeAlsoNames, final FieldReader... fieldReaders) {
        this(objectClass, typeKey, typeName, features, schema, creator, buildFunction, seeAlso, seeAlsoNames, (Class)null, (FieldReader[])fieldReaders);
    }
    
    public ObjectReaderAdapter(final Class objectClass, final String typeKey, final String typeName, final long features, final JSONSchema schema, final Supplier<T> creator, final Function buildFunction, final Class[] seeAlso, final String[] seeAlsoNames, final Class seeAlsoDefault, final FieldReader... fieldReaders) {
        super(objectClass, creator, typeName, features, schema, buildFunction);
        this.constructor = ((objectClass == null) ? null : BeanUtils.getDefaultConstructor(objectClass, true));
        if (this.constructor != null) {
            this.constructor.setAccessible(true);
        }
        if (typeKey == null || typeKey.isEmpty()) {
            this.typeKey = "@type";
            this.typeKeyHashCode = ObjectReaderAdapter.HASH_TYPE;
        }
        else {
            this.typeKey = typeKey;
            this.typeKeyHashCode = Fnv.hashCode64(typeKey);
        }
        this.fieldReaders = fieldReaders;
        final long[] hashCodes = new long[fieldReaders.length];
        final long[] hashCodesLCase = new long[fieldReaders.length];
        for (int i = 0; i < fieldReaders.length; ++i) {
            final FieldReader fieldReader = fieldReaders[i];
            hashCodes[i] = fieldReader.fieldNameHash;
            hashCodesLCase[i] = fieldReader.fieldNameHashLCase;
            if (fieldReader.isUnwrapped() && (this.extraFieldReader == null || !(this.extraFieldReader instanceof FieldReaderAnySetter))) {
                this.extraFieldReader = fieldReader;
            }
            if (fieldReader.defaultValue != null) {
                this.hasDefaultValue = true;
            }
        }
        Arrays.sort(this.hashCodes = Arrays.copyOf(hashCodes, hashCodes.length));
        this.mapping = new short[this.hashCodes.length];
        for (int i = 0; i < hashCodes.length; ++i) {
            final long hashCode = hashCodes[i];
            final int index = Arrays.binarySearch(this.hashCodes, hashCode);
            this.mapping[index] = (short)i;
        }
        Arrays.sort(this.hashCodesLCase = Arrays.copyOf(hashCodesLCase, hashCodesLCase.length));
        this.mappingLCase = new short[this.hashCodesLCase.length];
        for (int i = 0; i < hashCodesLCase.length; ++i) {
            final long hashCode = hashCodesLCase[i];
            final int index = Arrays.binarySearch(this.hashCodesLCase, hashCode);
            this.mappingLCase[index] = (short)i;
        }
        if ((this.seeAlso = seeAlso) != null) {
            this.seeAlsoMapping = new HashMap<Long, Class>(seeAlso.length);
            this.seeAlsoNames = new String[seeAlso.length];
            for (int i = 0; i < seeAlso.length; ++i) {
                final Class seeAlsoClass = seeAlso[i];
                String seeAlsoTypeName = null;
                if (seeAlsoNames != null && seeAlsoNames.length >= i + 1) {
                    seeAlsoTypeName = seeAlsoNames[i];
                }
                if (seeAlsoTypeName == null || seeAlsoTypeName.isEmpty()) {
                    seeAlsoTypeName = seeAlsoClass.getSimpleName();
                }
                final long hashCode2 = Fnv.hashCode64(seeAlsoTypeName);
                this.seeAlsoMapping.put(hashCode2, seeAlsoClass);
                this.seeAlsoNames[i] = seeAlsoTypeName;
            }
        }
        else {
            this.seeAlsoMapping = null;
            this.seeAlsoNames = null;
        }
        this.seeAlsoDefault = seeAlsoDefault;
    }
    
    @Override
    public final String getTypeKey() {
        return this.typeKey;
    }
    
    @Override
    public final long getTypeKeyHash() {
        return this.typeKeyHashCode;
    }
    
    @Override
    public final long getFeatures() {
        return this.features;
    }
    
    public FieldReader[] getFieldReaders() {
        return Arrays.copyOf(this.fieldReaders, this.fieldReaders.length);
    }
    
    public void apply(final Consumer<FieldReader> fieldReaderConsumer) {
        for (final FieldReader fieldReader : this.fieldReaders) {
            fieldReaderConsumer.accept(fieldReader);
        }
    }
    
    public Object auoType(final JSONReader jsonReader, final Class expectClass, final long features) {
        final long typeHash = jsonReader.readTypeHashCode();
        final JSONReader.Context context = jsonReader.getContext();
        ObjectReader autoTypeObjectReader = null;
        if (jsonReader.isSupportAutoTypeOrHandler(features)) {
            autoTypeObjectReader = context.getObjectReaderAutoType(typeHash);
        }
        if (autoTypeObjectReader == null) {
            final String typeName = jsonReader.getString();
            autoTypeObjectReader = context.getObjectReaderAutoType(typeName, expectClass, this.features | features | context.getFeatures());
            if (autoTypeObjectReader == null) {
                if (expectClass != this.objectClass) {
                    throw new JSONException(jsonReader.info("auotype not support : " + typeName));
                }
                autoTypeObjectReader = this;
            }
        }
        return autoTypeObjectReader.readObject(jsonReader, null, null, features);
    }
    
    @Override
    public final Function getBuildFunction() {
        return this.buildFunction;
    }
    
    @Override
    public T readArrayMappingObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (!this.serializable) {
            jsonReader.errorOnNoneSerializable(this.objectClass);
        }
        jsonReader.nextIfArrayStart();
        final Object object = this.creator.get();
        for (int i = 0; i < this.fieldReaders.length; ++i) {
            this.fieldReaders[i].readFieldValue(jsonReader, object);
        }
        if (!jsonReader.nextIfArrayEnd()) {
            throw new JSONException(jsonReader.info("array to bean end error"));
        }
        jsonReader.nextIfComma();
        if (this.buildFunction != null) {
            return this.buildFunction.apply(object);
        }
        return (T)object;
    }
    
    @Override
    public T readArrayMappingJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (!this.serializable) {
            jsonReader.errorOnNoneSerializable(this.objectClass);
        }
        final ObjectReader autoTypeReader = this.checkAutoType(jsonReader, this.objectClass, this.features | features);
        if (autoTypeReader != null && autoTypeReader != this && autoTypeReader.getObjectClass() != this.objectClass) {
            return autoTypeReader.readArrayMappingJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        final int entryCnt = jsonReader.startArray();
        final T object = this.createInstance(0L);
        for (int i = 0; i < this.fieldReaders.length; ++i) {
            if (i < entryCnt) {
                final FieldReader fieldReader = this.fieldReaders[i];
                fieldReader.readFieldValue(jsonReader, object);
            }
        }
        for (int i = this.fieldReaders.length; i < entryCnt; ++i) {
            jsonReader.skipValue();
        }
        if (this.buildFunction != null) {
            return this.buildFunction.apply(object);
        }
        return object;
    }
    
    protected Object createInstance0(final long features) {
        if ((features & JSONReader.Feature.UseDefaultConstructorAsPossible.mask) != 0x0L && this.constructor != null && this.constructor.getParameterCount() == 0) {
            T object;
            try {
                object = this.constructor.newInstance(new Object[0]);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException ex3) {
                final ReflectiveOperationException ex2;
                final ReflectiveOperationException ex = ex2;
                throw new JSONException("create instance error, " + this.objectClass, ex);
            }
            if (this.hasDefaultValue) {
                this.initDefaultValue(object);
            }
            return object;
        }
        if (this.creator == null) {
            throw new JSONException("create instance error, " + this.objectClass);
        }
        return this.creator.get();
    }
    
    @Override
    protected void initDefaultValue(final T object) {
        for (int i = 0; i < this.fieldReaders.length; ++i) {
            final FieldReader fieldReader = this.fieldReaders[i];
            final Object defaultValue = fieldReader.defaultValue;
            if (defaultValue != null) {
                fieldReader.accept(object, defaultValue);
            }
        }
    }
    
    @Override
    public T createInstance(final Collection collection) {
        final T object = this.createInstance(0L);
        int index = 0;
        for (final Object fieldValue : collection) {
            if (index >= this.fieldReaders.length) {
                break;
            }
            final FieldReader fieldReader = this.fieldReaders[index];
            fieldReader.accept(object, fieldValue);
            ++index;
        }
        return object;
    }
    
    @Override
    public T createInstance(final long features) {
        if (this.instantiationError && this.constructor != null) {
            T object;
            try {
                object = this.constructor.newInstance(new Object[0]);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException ex5) {
                final ReflectiveOperationException ex3;
                final ReflectiveOperationException ex = ex3;
                throw new JSONException("create instance error, " + this.objectClass, ex);
            }
            if (this.hasDefaultValue) {
                this.initDefaultValue(object);
            }
            return object;
        }
        try {
            final T object2 = (T)this.createInstance0(features);
            if (this.hasDefaultValue) {
                this.initDefaultValue(object2);
            }
            return object2;
        }
        catch (Exception ex2) {
            final Exception error = ex2;
            this.instantiationError = true;
            if (this.constructor != null) {
                try {
                    final T object2 = this.constructor.newInstance(new Object[0]);
                    if (this.hasDefaultValue) {
                        this.initDefaultValue(object2);
                    }
                    return object2;
                }
                catch (InstantiationException | IllegalAccessException | InvocationTargetException ex6) {
                    final ReflectiveOperationException ex4;
                    final ReflectiveOperationException ex = ex4;
                    throw new JSONException("create instance error, " + this.objectClass, ex);
                }
            }
            throw new JSONException("create instance error, " + this.objectClass, error);
        }
    }
    
    @Override
    public FieldReader getFieldReader(final long hashCode) {
        final int m = Arrays.binarySearch(this.hashCodes, hashCode);
        if (m < 0) {
            return null;
        }
        final int index = this.mapping[m];
        return this.fieldReaders[index];
    }
    
    public int getFieldOrdinal(final long hashCode) {
        final int m = Arrays.binarySearch(this.hashCodes, hashCode);
        return (m < 0) ? -1 : this.mapping[m];
    }
    
    @Override
    public FieldReader getFieldReaderLCase(final long hashCode) {
        final int m = Arrays.binarySearch(this.hashCodesLCase, hashCode);
        if (m < 0) {
            return null;
        }
        final int index = this.mappingLCase[m];
        return this.fieldReaders[index];
    }
    
    protected T autoType(final JSONReader jsonReader) {
        final long typeHash = jsonReader.readTypeHashCode();
        final JSONReader.Context context = jsonReader.getContext();
        ObjectReader autoTypeObjectReader = this.autoType(context, typeHash);
        if (autoTypeObjectReader == null) {
            final String typeName = jsonReader.getString();
            autoTypeObjectReader = context.getObjectReaderAutoType(typeName, null);
            if (autoTypeObjectReader == null) {
                throw new JSONException(jsonReader.info("auotype not support : " + typeName));
            }
        }
        return autoTypeObjectReader.readJSONBObject(jsonReader, null, null, this.features);
    }
    
    @Override
    public T readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        final ObjectReader autoTypeReader = jsonReader.checkAutoType(this.objectClass, this.typeNameHash, this.features | features);
        if (autoTypeReader != null && autoTypeReader.getObjectClass() != this.objectClass) {
            return autoTypeReader.readJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        if (!this.serializable) {
            jsonReader.errorOnNoneSerializable(this.objectClass);
        }
        if (!jsonReader.isArray()) {
            final boolean objectStart = jsonReader.nextIfObjectStart();
            T object = null;
            int i = 0;
            while (!jsonReader.nextIfObjectEnd()) {
                final long hash = jsonReader.readFieldNameHashCode();
                if (hash == this.typeKeyHashCode && i == 0) {
                    final long typeHash = jsonReader.readValueHashCode();
                    final JSONReader.Context context = jsonReader.getContext();
                    ObjectReader autoTypeObjectReader = this.autoType(context, typeHash);
                    if (autoTypeObjectReader == null) {
                        final String typeName = jsonReader.getString();
                        autoTypeObjectReader = context.getObjectReaderAutoType(typeName, null);
                        if (autoTypeObjectReader == null) {
                            throw new JSONException(jsonReader.info("auotype not support : " + typeName));
                        }
                    }
                    if (autoTypeObjectReader != this) {
                        jsonReader.setTypeRedirect(true);
                        return autoTypeObjectReader.readJSONBObject(jsonReader, fieldType, fieldName, features);
                    }
                }
                else if (hash != 0L) {
                    FieldReader fieldReader = this.getFieldReader(hash);
                    if (fieldReader == null && jsonReader.isSupportSmartMatch(features | this.features)) {
                        final long nameHashCodeLCase = jsonReader.getNameHashCodeLCase();
                        fieldReader = this.getFieldReaderLCase(nameHashCodeLCase);
                    }
                    if (fieldReader == null) {
                        this.processExtra(jsonReader, object);
                    }
                    else {
                        if (object == null) {
                            object = this.createInstance(jsonReader.getContext().getFeatures() | features);
                        }
                        fieldReader.readFieldValue(jsonReader, object);
                    }
                }
                ++i;
            }
            if (object == null) {
                object = this.createInstance(jsonReader.getContext().getFeatures() | features);
            }
            if (this.schema != null) {
                this.schema.assertValidate(object);
            }
            return object;
        }
        if (jsonReader.isSupportBeanArray()) {
            return this.readArrayMappingJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        throw new JSONException(jsonReader.info("expect object, but " + JSONB.typeName(jsonReader.getType())));
    }
    
    @Override
    public ObjectReader autoType(final ObjectReaderProvider provider, final long typeHash) {
        if (this.seeAlsoMapping == null || this.seeAlsoMapping.size() <= 0) {
            return provider.getObjectReader(typeHash);
        }
        final Class seeAlsoClass = this.seeAlsoMapping.get(typeHash);
        if (seeAlsoClass == null) {
            return null;
        }
        return provider.getObjectReader(seeAlsoClass);
    }
    
    @Override
    public ObjectReader autoType(final JSONReader.Context context, final long typeHash) {
        if (this.seeAlsoMapping == null || this.seeAlsoMapping.size() <= 0) {
            return context.getObjectReaderAutoType(typeHash);
        }
        final Class seeAlsoClass = this.seeAlsoMapping.get(typeHash);
        if (seeAlsoClass == null) {
            return null;
        }
        return context.getObjectReader(seeAlsoClass);
    }
    
    @Override
    protected void initStringFieldAsEmpty(final Object object) {
        for (int i = 0; i < this.fieldReaders.length; ++i) {
            final FieldReader fieldReader = this.fieldReaders[i];
            if (fieldReader.fieldClass == String.class) {
                fieldReader.accept(object, "");
            }
        }
    }
    
    @Override
    public T createInstance(final Map map, final long features) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final Object typeKey = map.get(this.typeKey);
        if (typeKey instanceof String) {
            final String typeName = (String)typeKey;
            final long typeHash = Fnv.hashCode64(typeName);
            ObjectReader<T> reader = null;
            if ((features & JSONReader.Feature.SupportAutoType.mask) != 0x0L || this instanceof ObjectReaderSeeAlso) {
                reader = (ObjectReader<T>)this.autoType(provider, typeHash);
            }
            if (reader == null) {
                reader = (ObjectReader<T>)provider.getObjectReader(typeName, this.getObjectClass(), features | this.getFeatures());
            }
            if (reader != this && reader != null) {
                return reader.createInstance(map, features);
            }
        }
        final T object = this.createInstance(0L);
        if (this.extraFieldReader == null && ((features | this.features) & JSONReader.Feature.SupportSmartMatch.mask) == 0x0L) {
            for (int i = 0; i < this.fieldReaders.length; ++i) {
                final FieldReader fieldReader = this.fieldReaders[i];
                final Object fieldValue = map.get(fieldReader.fieldName);
                if (fieldValue != null) {
                    if (fieldValue.getClass() == fieldReader.fieldType) {
                        fieldReader.accept(object, fieldValue);
                    }
                    else if (fieldReader instanceof FieldReaderList && fieldValue instanceof JSONArray) {
                        final ObjectReader objectReader = fieldReader.getObjectReader(provider);
                        final Object fieldValueList = objectReader.createInstance((Collection)fieldValue);
                        fieldReader.accept(object, fieldValueList);
                    }
                    else if (fieldValue instanceof JSONObject && fieldReader.fieldType != JSONObject.class) {
                        final JSONObject jsonObject = (JSONObject)fieldValue;
                        final boolean fieldBased = ((this.features | features) & JSONReader.Feature.FieldBased.mask) != 0x0L;
                        final ObjectReader<T> objectReader2 = (ObjectReader<T>)provider.getObjectReader(fieldReader.fieldType, fieldBased);
                        final Object fieldValueJavaBean = objectReader2.createInstance(jsonObject, features);
                        fieldReader.accept(object, fieldValueJavaBean);
                    }
                    else {
                        fieldReader.acceptAny(object, fieldValue, features);
                    }
                }
            }
        }
        else {
            for (final Map.Entry entry : map.entrySet()) {
                final String entryKey = entry.getKey().toString();
                final Object fieldValue2 = entry.getValue();
                final FieldReader fieldReader2 = this.getFieldReader(entryKey);
                if (fieldReader2 == null) {
                    this.acceptExtra(object, entryKey, entry.getValue());
                }
                else if (fieldValue2 != null && fieldValue2.getClass() == fieldReader2.fieldType) {
                    fieldReader2.accept(object, fieldValue2);
                }
                else {
                    fieldReader2.acceptAny(object, fieldValue2, features);
                }
            }
        }
        final Function buildFunction = this.getBuildFunction();
        if (buildFunction != null) {
            return buildFunction.apply(object);
        }
        return object;
    }
}
