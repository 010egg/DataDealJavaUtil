// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.annotation.JSONType;
import java.util.Arrays;
import java.util.function.Function;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.Supplier;

final class ObjectReaderSeeAlso<T> extends ObjectReaderAdapter<T>
{
    ObjectReaderSeeAlso(final Class objectType, final Supplier<T> defaultCreator, final String typeKey, final Class[] seeAlso, final String[] seeAlsoNames, final Class seeAlsoDefault, final FieldReader... fieldReaders) {
        super(objectType, typeKey, null, JSONReader.Feature.SupportAutoType.mask, null, defaultCreator, null, seeAlso, seeAlsoNames, seeAlsoDefault, fieldReaders);
    }
    
    ObjectReaderSeeAlso addSubType(final Class subTypeClass, String subTypeClassName) {
        for (int i = 0; i < this.seeAlso.length; ++i) {
            if (this.seeAlso[i] == subTypeClass) {
                return this;
            }
        }
        final Class[] seeAlso1 = Arrays.copyOf(this.seeAlso, this.seeAlso.length + 1);
        final String[] seeAlsoNames1 = Arrays.copyOf(this.seeAlsoNames, this.seeAlsoNames.length + 1);
        seeAlso1[seeAlso1.length - 1] = subTypeClass;
        if (subTypeClassName == null) {
            final JSONType jsonType = subTypeClass.getAnnotation(JSONType.class);
            if (jsonType != null) {
                subTypeClassName = jsonType.typeName();
            }
        }
        if (subTypeClassName != null) {
            seeAlsoNames1[seeAlsoNames1.length - 1] = subTypeClassName;
        }
        return new ObjectReaderSeeAlso(this.objectClass, this.creator, this.typeKey, seeAlso1, seeAlsoNames1, this.seeAlsoDefault, this.fieldReaders);
    }
    
    @Override
    public T createInstance(final long features) {
        if (this.creator == null) {
            return null;
        }
        return this.creator.get();
    }
    
    @Override
    public T readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        if (!this.serializable) {
            jsonReader.errorOnNoneSerializable(this.objectClass);
        }
        if (jsonReader.nextIfNull()) {
            jsonReader.nextIfComma();
            return null;
        }
        if (jsonReader.isString()) {
            final long valueHashCode = jsonReader.readValueHashCode();
            for (int i = 0; i < this.seeAlso.length; ++i) {
                final Class seeAlsoType = this.seeAlso[i];
                if (Enum.class.isAssignableFrom(seeAlsoType)) {
                    final ObjectReader seeAlsoTypeReader = jsonReader.getObjectReader(seeAlsoType);
                    Enum e = null;
                    if (seeAlsoTypeReader instanceof ObjectReaderImplEnum) {
                        e = ((ObjectReaderImplEnum)seeAlsoTypeReader).getEnumByHashCode(valueHashCode);
                    }
                    if (e != null) {
                        return (T)e;
                    }
                }
            }
            final String strVal = jsonReader.getString();
            throw new JSONException(jsonReader.info("not support input " + strVal));
        }
        final JSONReader.SavePoint savePoint = jsonReader.mark();
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
            int j = 0;
            while (!jsonReader.nextIfObjectEnd()) {
                final JSONReader.Context context = jsonReader.getContext();
                final long hash = jsonReader.readFieldNameHashCode();
                final JSONReader.AutoTypeBeforeHandler autoTypeFilter = context.getContextAutoTypeBeforeHandler();
                final long features2;
                if (hash == this.getTypeKeyHash() && (((features2 = (features | this.getFeatures() | context.getFeatures())) & JSONReader.Feature.SupportAutoType.mask) != 0x0L || autoTypeFilter != null)) {
                    ObjectReader reader = null;
                    long typeHash = jsonReader.readTypeHashCode();
                    Number typeNumber = null;
                    String typeNumberStr = null;
                    if (typeHash == -1L && jsonReader.isNumber()) {
                        typeNumber = jsonReader.readNumber();
                        typeNumberStr = typeNumber.toString();
                        typeHash = Fnv.hashCode64(typeNumberStr);
                    }
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
                        if (reader == null && this.seeAlsoDefault != null) {
                            reader = context.getObjectReader(this.seeAlsoDefault);
                        }
                        if (reader == null) {
                            throw new JSONException(jsonReader.info("No suitable ObjectReader found for" + typeName));
                        }
                    }
                    if (reader != this) {
                        final FieldReader fieldReader = reader.getFieldReader(hash);
                        if (fieldReader != null && typeName == null) {
                            if (typeNumberStr != null) {
                                typeName = typeNumberStr;
                            }
                            else {
                                typeName = jsonReader.getString();
                            }
                        }
                        if (j != 0) {
                            jsonReader.reset(savePoint);
                        }
                        object = reader.readObject(jsonReader, fieldType, fieldName, features | this.getFeatures());
                        if (fieldReader != null) {
                            if (typeNumber != null) {
                                fieldReader.accept(object, typeNumber);
                            }
                            else {
                                fieldReader.accept(object, typeName);
                            }
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
                ++j;
            }
            if (object == null) {
                object = this.createInstance(jsonReader.getContext().getFeatures() | features);
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
}
