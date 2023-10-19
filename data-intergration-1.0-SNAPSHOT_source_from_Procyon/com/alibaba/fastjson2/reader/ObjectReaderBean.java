// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.filter.ExtraProcessor;
import java.util.List;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import java.io.Serializable;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ObjectReaderBean<T> implements ObjectReader<T>
{
    protected final Class objectClass;
    protected final Supplier<T> creator;
    protected final Function buildFunction;
    protected final long features;
    protected final String typeName;
    protected final long typeNameHash;
    protected FieldReader extraFieldReader;
    protected boolean hasDefaultValue;
    protected final boolean serializable;
    protected final JSONSchema schema;
    protected JSONReader.AutoTypeBeforeHandler autoTypeBeforeHandler;
    
    protected ObjectReaderBean(final Class objectClass, final Supplier<T> creator, String typeName, final long features, final JSONSchema schema, final Function buildFunction) {
        if (typeName == null && objectClass != null) {
            typeName = TypeUtils.getTypeName(objectClass);
        }
        this.objectClass = objectClass;
        this.creator = creator;
        this.buildFunction = buildFunction;
        this.features = features;
        this.typeNameHash = (((this.typeName = typeName) != null) ? Fnv.hashCode64(typeName) : 0L);
        this.schema = schema;
        this.serializable = (objectClass != null && Serializable.class.isAssignableFrom(objectClass));
    }
    
    @Override
    public Class<T> getObjectClass() {
        return (Class<T>)this.objectClass;
    }
    
    protected T processObjectInputSingleItemArray(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        String message = "expect {, but [, class " + this.typeName;
        if (fieldName != null) {
            message = message + ", parent fieldName " + fieldName;
        }
        final String info = jsonReader.info(message);
        final long featuresAll = jsonReader.features(features);
        if ((featuresAll & JSONReader.Feature.SupportSmartMatch.mask) != 0x0L) {
            final Type itemType = (fieldType == null) ? this.objectClass : fieldType;
            final List list = jsonReader.readArray(itemType);
            if (list.size() == 1) {
                return list.get(0);
            }
        }
        throw new JSONException(info);
    }
    
    protected void processExtra(final JSONReader jsonReader, final Object object) {
        if (this.extraFieldReader != null && object != null) {
            this.extraFieldReader.processExtra(jsonReader, object);
            return;
        }
        if ((jsonReader.features(this.features) & JSONReader.Feature.SupportSmartMatch.mask) != 0x0L) {
            final String fieldName = jsonReader.getFieldName();
            if (fieldName.startsWith("is")) {
                final String fieldName2 = fieldName.substring(2);
                final long hashCode64LCase = Fnv.hashCode64LCase(fieldName2);
                final FieldReader fieldReader = this.getFieldReaderLCase(hashCode64LCase);
                if (fieldReader != null && fieldReader.fieldClass == Boolean.class) {
                    fieldReader.readFieldValue(jsonReader, object);
                    return;
                }
            }
        }
        final ExtraProcessor extraProcessor = jsonReader.getContext().getExtraProcessor();
        if (extraProcessor != null) {
            final String fieldName3 = jsonReader.getFieldName();
            final Type type = extraProcessor.getType(fieldName3);
            final Object extraValue = jsonReader.read(type);
            extraProcessor.processExtra(object, fieldName3, extraValue);
            return;
        }
        jsonReader.skipValue();
    }
    
    @Override
    public void acceptExtra(final Object object, final String fieldName, final Object fieldValue) {
        if (this.extraFieldReader == null || object == null) {
            return;
        }
        this.extraFieldReader.acceptExtra(object, fieldName, fieldValue);
    }
    
    public final ObjectReader checkAutoType(final JSONReader jsonReader, final Class expectClass, final long features) {
        if (!jsonReader.nextIfMatchTypedAny()) {
            return null;
        }
        final long typeHash = jsonReader.readTypeHashCode();
        final JSONReader.Context context = jsonReader.getContext();
        final long features2 = jsonReader.features(features | this.features);
        final JSONReader.AutoTypeBeforeHandler autoTypeFilter = context.getContextAutoTypeBeforeHandler();
        if (autoTypeFilter != null) {
            Class<?> filterClass = autoTypeFilter.apply(typeHash, expectClass, features);
            if (filterClass == null) {
                final String typeName = jsonReader.getString();
                filterClass = autoTypeFilter.apply(typeName, expectClass, features);
                if (!expectClass.isAssignableFrom(filterClass)) {
                    if ((jsonReader.features(features) & JSONReader.Feature.IgnoreAutoTypeNotMatch.mask) == 0x0L) {
                        throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
                    }
                    filterClass = (Class<?>)expectClass;
                }
            }
            return context.getObjectReader(filterClass);
        }
        final ObjectReader autoTypeObjectReader = jsonReader.getObjectReaderAutoType(typeHash, expectClass, features);
        if (autoTypeObjectReader == null) {
            throw new JSONException(jsonReader.info("auotype not support"));
        }
        final Class autoTypeObjectReaderClass = autoTypeObjectReader.getObjectClass();
        if (expectClass != null && autoTypeObjectReaderClass != null && !expectClass.isAssignableFrom(autoTypeObjectReaderClass)) {
            if ((features2 & JSONReader.Feature.IgnoreAutoTypeNotMatch.mask) != 0x0L) {
                return context.getObjectReader(expectClass);
            }
            throw new JSONException("type not match. " + this.typeName + " -> " + expectClass.getName());
        }
        else {
            if (typeHash == this.typeNameHash) {
                return this;
            }
            if ((features2 & JSONReader.Feature.SupportAutoType.mask) == 0x0L) {
                return null;
            }
            return autoTypeObjectReader;
        }
    }
    
    protected void initDefaultValue(final T object) {
    }
    
    public void readObject(final JSONReader jsonReader, final Object object, final long features) {
        if (jsonReader.nextIfNull()) {
            jsonReader.nextIfComma();
            return;
        }
        final boolean objectStart = jsonReader.nextIfObjectStart();
        if (!objectStart) {
            throw new JSONException(jsonReader.info());
        }
        while (!jsonReader.nextIfObjectEnd()) {
            final long hash = jsonReader.readFieldNameHashCode();
            FieldReader fieldReader = this.getFieldReader(hash);
            if (fieldReader == null && jsonReader.isSupportSmartMatch(features | this.getFeatures())) {
                final long nameHashCodeLCase = jsonReader.getNameHashCodeLCase();
                fieldReader = this.getFieldReaderLCase(nameHashCodeLCase);
            }
            if (fieldReader == null) {
                this.processExtra(jsonReader, object);
            }
            else {
                fieldReader.readFieldValue(jsonReader, object);
            }
        }
        jsonReader.nextIfComma();
        if (this.schema != null) {
            this.schema.assertValidate(object);
        }
    }
    
    @Override
    public T readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        if (jsonReader.nextIfNullOrEmptyString()) {
            jsonReader.nextIfComma();
            return null;
        }
        final long featuresAll = jsonReader.features(this.getFeatures() | features);
        if (!jsonReader.isArray()) {
            T object = null;
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
                JSONReader.AutoTypeBeforeHandler autoTypeFilter = this.autoTypeBeforeHandler;
                if (autoTypeFilter == null) {
                    autoTypeFilter = context.getContextAutoTypeBeforeHandler();
                }
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
                    if (object == null) {
                        object = this.createInstance(jsonReader.getContext().getFeatures() | features);
                    }
                    if (fieldReader2 == null) {
                        this.processExtra(jsonReader, object);
                    }
                    else {
                        fieldReader2.readFieldValue(jsonReader, object);
                    }
                }
                ++i;
            }
            if (object == null) {
                object = this.createInstance(jsonReader.getContext().getFeatures() | features);
                if (object != null && (featuresAll & JSONReader.Feature.InitStringFieldAsEmpty.mask) != 0x0L) {
                    this.initStringFieldAsEmpty(object);
                }
            }
            jsonReader.nextIfComma();
            final Function buildFunction = this.getBuildFunction();
            if (buildFunction != null) {
                object = buildFunction.apply(object);
            }
            if (this.schema != null) {
                this.schema.assertValidate(object);
            }
            return object;
        }
        if ((featuresAll & JSONReader.Feature.SupportArrayToBean.mask) != 0x0L) {
            return this.readArrayMappingObject(jsonReader, fieldType, fieldName, features);
        }
        return this.processObjectInputSingleItemArray(jsonReader, fieldType, fieldName, featuresAll);
    }
    
    protected void initStringFieldAsEmpty(final Object object) {
    }
    
    public JSONReader.AutoTypeBeforeHandler getAutoTypeBeforeHandler() {
        return this.autoTypeBeforeHandler;
    }
    
    public void setAutoTypeBeforeHandler(final JSONReader.AutoTypeBeforeHandler autoTypeBeforeHandler) {
        this.autoTypeBeforeHandler = autoTypeBeforeHandler;
    }
    
    protected boolean readFieldValueWithLCase(final JSONReader jsonReader, final Object object, final long hashCode64, final long features2) {
        if (jsonReader.isSupportSmartMatch(features2)) {
            final long hashCode64L = jsonReader.getNameHashCodeLCase();
            if (hashCode64L != hashCode64) {
                final FieldReader fieldReader = this.getFieldReaderLCase(hashCode64L);
                if (fieldReader != null) {
                    fieldReader.readFieldValue(jsonReader, object);
                    return true;
                }
            }
        }
        return false;
    }
}
