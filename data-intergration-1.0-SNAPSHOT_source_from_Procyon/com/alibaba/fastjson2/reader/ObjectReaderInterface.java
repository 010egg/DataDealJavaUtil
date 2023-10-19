// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.Map;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ObjectReaderInterface<T> extends ObjectReaderAdapter<T>
{
    public ObjectReaderInterface(final Class objectClass, final String typeKey, final String typeName, final long features, final Supplier creator, final Function buildFunction, final FieldReader[] fieldReaders) {
        super(objectClass, typeKey, typeName, features, null, creator, buildFunction, (FieldReader[])fieldReaders);
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
        if (!jsonReader.isArray()) {
            final boolean objectStart = jsonReader.nextIfObjectStart();
            final JSONObject jsonObject = new JSONObject();
            Object object = null;
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
                        jsonObject.put(jsonReader.getFieldName(), jsonReader.readAny());
                    }
                    else {
                        final Object fieldValue = fieldReader.readFieldValue(jsonReader);
                        jsonObject.put(fieldReader.fieldName, fieldValue);
                    }
                }
                ++i;
            }
            object = TypeUtils.newProxyInstance((Class<Object>)this.objectClass, jsonObject);
            if (this.schema != null) {
                this.schema.assertValidate(object);
            }
            return (T)object;
        }
        if (jsonReader.isSupportBeanArray()) {
            return this.readArrayMappingJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        throw new JSONException(jsonReader.info("expect object, but " + JSONB.typeName(jsonReader.getType())));
    }
    
    @Override
    public T readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        if (jsonReader.nextIfNull()) {
            jsonReader.nextIfComma();
            return null;
        }
        if (jsonReader.isArray() && jsonReader.isSupportBeanArray(this.getFeatures() | features)) {
            return this.readArrayMappingObject(jsonReader, fieldType, fieldName, features);
        }
        T object = null;
        final JSONObject jsonObject = new JSONObject();
        final boolean objectStart = jsonReader.nextIfObjectStart();
        if (!objectStart) {
            final char ch = jsonReader.current();
            if (ch == 't' || ch == 'f') {
                jsonReader.readBoolValue();
                return null;
            }
            if (ch != '\"' && ch != '\'' && ch != '}') {
                throw new JSONException(jsonReader.info());
            }
        }
        int i = 0;
        while (!jsonReader.nextIfObjectEnd()) {
            final JSONReader.Context context = jsonReader.getContext();
            final long hash = jsonReader.readFieldNameHashCode();
            final JSONReader.AutoTypeBeforeHandler autoTypeFilter = context.getContextAutoTypeBeforeHandler();
            final long features2;
            if (i == 0 && hash == this.getTypeKeyHash() && (((features2 = (features | this.getFeatures() | context.getFeatures())) & JSONReader.Feature.SupportAutoType.mask) != 0x0L || autoTypeFilter != null)) {
                ObjectReader reader = null;
                final long typeHash = jsonReader.readTypeHashCode();
                if (autoTypeFilter != null) {
                    Class<?> filterClass = autoTypeFilter.apply(typeHash, this.objectClass, features2);
                    if (filterClass == null) {
                        filterClass = autoTypeFilter.apply(jsonReader.getString(), this.objectClass, features2);
                        if (filterClass != null) {
                            reader = context.getObjectReader(filterClass);
                        }
                    }
                }
                if (reader == null) {
                    reader = this.autoType(context, typeHash);
                }
                String typeName = null;
                if (reader == null) {
                    typeName = jsonReader.getString();
                    reader = context.getObjectReaderAutoType(typeName, this.objectClass, features2);
                    if (reader == null) {
                        throw new JSONException(jsonReader.info("No suitable ObjectReader found for" + typeName));
                    }
                }
                if (reader != this) {
                    final FieldReader fieldReader = reader.getFieldReader(hash);
                    if (fieldReader != null && typeName == null) {
                        typeName = jsonReader.getString();
                    }
                    object = reader.readObject(jsonReader, null, null, features | this.getFeatures());
                    if (fieldReader != null) {
                        fieldReader.accept(object, typeName);
                    }
                    return object;
                }
            }
            else {
                FieldReader fieldReader2 = this.getFieldReader(hash);
                if (fieldReader2 == null && jsonReader.isSupportSmartMatch(features | this.getFeatures())) {
                    final long nameHashCodeLCase = jsonReader.getNameHashCodeLCase();
                    fieldReader2 = this.getFieldReaderLCase(nameHashCodeLCase);
                }
                if (fieldReader2 == null) {
                    jsonObject.put(jsonReader.getFieldName(), jsonReader.readAny());
                }
                else {
                    final Object fieldValue = fieldReader2.readFieldValue(jsonReader);
                    jsonObject.put(fieldReader2.fieldName, fieldValue);
                }
            }
            ++i;
        }
        jsonReader.nextIfComma();
        object = TypeUtils.newProxyInstance((Class<T>)this.objectClass, jsonObject);
        final Function buildFunction = this.getBuildFunction();
        if (buildFunction != null) {
            object = buildFunction.apply(object);
        }
        if (this.schema != null) {
            this.schema.assertValidate(object);
        }
        return object;
    }
    
    @Override
    public T createInstance(final long features) {
        final JSONObject object = new JSONObject();
        return TypeUtils.newProxyInstance((Class<T>)this.objectClass, object);
    }
    
    @Override
    public T createInstance(final Map map, final long features) {
        JSONObject object;
        if (map instanceof JSONObject) {
            object = (JSONObject)map;
        }
        else {
            object = new JSONObject(map);
        }
        return TypeUtils.newProxyInstance((Class<T>)this.objectClass, object);
    }
}
