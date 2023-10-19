// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.Function;
import java.util.function.Supplier;
import com.alibaba.fastjson2.schema.JSONSchema;

public class ObjectReader2<T> extends ObjectReaderAdapter<T>
{
    protected final FieldReader fieldReader0;
    protected final FieldReader fieldReader1;
    protected final long hashCode0;
    protected final long hashCode1;
    protected final long hashCode0LCase;
    protected final long hashCode1LCase;
    protected ObjectReader objectReader0;
    protected ObjectReader objectReader1;
    
    public ObjectReader2(final Class objectClass, final long features, final JSONSchema schema, final Supplier<T> creator, final Function buildFunction, final FieldReader first, final FieldReader second) {
        this(objectClass, null, null, features, schema, creator, buildFunction, (FieldReader[])new FieldReader[] { first, second });
    }
    
    public ObjectReader2(final Class objectClass, final String typeKey, final String typeName, final long features, final Supplier<T> creator, final Function buildFunction, final FieldReader... fieldReaders) {
        this(objectClass, typeKey, typeName, features, null, creator, buildFunction, (FieldReader[])fieldReaders);
    }
    
    public ObjectReader2(final Class objectClass, final String typeKey, final String typeName, final long features, final JSONSchema schema, final Supplier<T> creator, final Function buildFunction, final FieldReader... fieldReaders) {
        super(objectClass, typeKey, typeName, features, schema, creator, buildFunction, fieldReaders);
        this.fieldReader0 = fieldReaders[0];
        this.fieldReader1 = fieldReaders[1];
        this.hashCode0 = this.fieldReader0.fieldNameHash;
        this.hashCode0LCase = this.fieldReader0.fieldNameHashLCase;
        this.hashCode1 = this.fieldReader1.fieldNameHash;
        this.hashCode1LCase = this.fieldReader1.fieldNameHashLCase;
        this.hasDefaultValue = (this.fieldReader0.defaultValue != null || this.fieldReader1.defaultValue != null);
    }
    
    @Override
    protected void initDefaultValue(final T object) {
        this.fieldReader0.acceptDefaultValue(object);
        this.fieldReader1.acceptDefaultValue(object);
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
        final T object = this.creator.get();
        final int entryCnt = jsonReader.startArray();
        if (entryCnt > 0) {
            this.fieldReader0.readFieldValue(jsonReader, object);
            if (entryCnt > 1) {
                this.fieldReader1.readFieldValue(jsonReader, object);
                for (int i = 2; i < entryCnt; ++i) {
                    jsonReader.skipValue();
                }
            }
        }
        if (this.buildFunction != null) {
            return this.buildFunction.apply(object);
        }
        return object;
    }
    
    @Override
    public T readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (!this.serializable) {
            jsonReader.errorOnNoneSerializable(this.objectClass);
        }
        final ObjectReader autoTypeReader = jsonReader.checkAutoType(this.objectClass, this.typeNameHash, this.features | features);
        if (autoTypeReader != null && autoTypeReader.getObjectClass() != this.objectClass) {
            return autoTypeReader.readJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        if (jsonReader.isArray()) {
            final T object = this.creator.get();
            if (this.hasDefaultValue) {
                this.initDefaultValue(object);
            }
            final int entryCnt = jsonReader.startArray();
            if (entryCnt > 0) {
                this.fieldReader0.readFieldValue(jsonReader, object);
                if (entryCnt > 1) {
                    this.fieldReader1.readFieldValue(jsonReader, object);
                    for (int i = 2; i < entryCnt; ++i) {
                        jsonReader.skipValue();
                    }
                }
            }
            if (this.buildFunction != null) {
                return this.buildFunction.apply(object);
            }
            return object;
        }
        else {
            if (!jsonReader.nextIfMatch((byte)(-90))) {
                throw new JSONException(jsonReader.info("expect object, but " + JSONB.typeName(jsonReader.getType())));
            }
            T object = null;
            Label_0303: {
                if (this.creator != null) {
                    object = this.creator.get();
                }
                else {
                    if (((features | jsonReader.getContext().getFeatures()) & JSONReader.Feature.FieldBased.mask) != 0x0L) {
                        try {
                            object = (T)JDKUtils.UNSAFE.allocateInstance(this.objectClass);
                            break Label_0303;
                        }
                        catch (InstantiationException e) {
                            throw new JSONException(jsonReader.info("create instance error"), e);
                        }
                    }
                    object = null;
                }
            }
            if (object != null && this.hasDefaultValue) {
                this.initDefaultValue(object);
            }
            if (object != null && jsonReader.isInitStringFieldAsEmpty()) {
                this.initStringFieldAsEmpty(object);
            }
            while (!jsonReader.nextIfMatch((byte)(-91))) {
                final long hashCode = jsonReader.readFieldNameHashCode();
                if (hashCode == 0L) {
                    continue;
                }
                if (hashCode == this.hashCode0) {
                    this.fieldReader0.readFieldValue(jsonReader, object);
                }
                else if (hashCode == this.hashCode1) {
                    this.fieldReader1.readFieldValueJSONB(jsonReader, object);
                }
                else {
                    if (jsonReader.isSupportSmartMatch(features | this.features)) {
                        final long nameHashCodeLCase = jsonReader.getNameHashCodeLCase();
                        if (nameHashCodeLCase == this.hashCode0LCase) {
                            this.fieldReader0.readFieldValueJSONB(jsonReader, object);
                            continue;
                        }
                        if (nameHashCodeLCase == this.hashCode1LCase) {
                            this.fieldReader1.readFieldValueJSONB(jsonReader, object);
                            continue;
                        }
                    }
                    this.processExtra(jsonReader, object);
                }
            }
            if (this.buildFunction != null) {
                object = this.buildFunction.apply(object);
            }
            if (this.schema != null) {
                this.schema.assertValidate(object);
            }
            return object;
        }
    }
    
    @Override
    public T readObject(final JSONReader jsonReader) {
        return this.readObject(jsonReader, null, null, this.features);
    }
    
    @Override
    public T readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (!this.serializable) {
            jsonReader.errorOnNoneSerializable(this.objectClass);
        }
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        if (jsonReader.nextIfNull()) {
            jsonReader.nextIfComma();
            return null;
        }
        final long featuresAll = jsonReader.features(this.features | features);
        if (!jsonReader.isArray()) {
            jsonReader.nextIfObjectStart();
            T object = this.creator.get();
            if (this.hasDefaultValue) {
                this.initDefaultValue(object);
            }
            if (object != null && (featuresAll & JSONReader.Feature.InitStringFieldAsEmpty.mask) != 0x0L) {
                this.initStringFieldAsEmpty(object);
            }
            int i = 0;
            while (true) {
                while (!jsonReader.nextIfObjectEnd()) {
                    final long hashCode = jsonReader.readFieldNameHashCode();
                    Label_0451: {
                        if (i == 0 && hashCode == ObjectReader2.HASH_TYPE) {
                            final long typeHash = jsonReader.readTypeHashCode();
                            final JSONReader.Context context = jsonReader.getContext();
                            ObjectReader autoTypeObjectReader = context.getObjectReaderAutoType(typeHash);
                            if (autoTypeObjectReader == null) {
                                final String typeName = jsonReader.getString();
                                autoTypeObjectReader = context.getObjectReaderAutoType(typeName, this.objectClass);
                                if (autoTypeObjectReader == null) {
                                    break Label_0451;
                                }
                            }
                            if (autoTypeObjectReader != this) {
                                object = autoTypeObjectReader.readObject(jsonReader, fieldType, fieldName, features);
                                jsonReader.nextIfMatch(',');
                                if (this.buildFunction != null) {
                                    try {
                                        object = this.buildFunction.apply(object);
                                    }
                                    catch (IllegalStateException e) {
                                        throw new JSONException(jsonReader.info("build object error"), e);
                                    }
                                }
                                if (this.schema != null) {
                                    this.schema.assertValidate(object);
                                }
                                return object;
                            }
                        }
                        else if (hashCode == this.hashCode0) {
                            this.fieldReader0.readFieldValue(jsonReader, object);
                        }
                        else if (hashCode == this.hashCode1) {
                            this.fieldReader1.readFieldValue(jsonReader, object);
                        }
                        else {
                            if (jsonReader.isSupportSmartMatch(features | this.features)) {
                                final long nameHashCodeLCase = jsonReader.getNameHashCodeLCase();
                                if (nameHashCodeLCase == this.hashCode0LCase) {
                                    this.fieldReader0.readFieldValue(jsonReader, object);
                                    break Label_0451;
                                }
                                if (nameHashCodeLCase == this.hashCode1LCase) {
                                    this.fieldReader1.readFieldValue(jsonReader, object);
                                    break Label_0451;
                                }
                            }
                            this.processExtra(jsonReader, object);
                        }
                    }
                    ++i;
                }
                continue;
            }
        }
        if ((featuresAll & JSONReader.Feature.SupportArrayToBean.mask) == 0x0L) {
            return this.processObjectInputSingleItemArray(jsonReader, fieldType, fieldName, featuresAll);
        }
        jsonReader.next();
        T object = this.creator.get();
        if (this.hasDefaultValue) {
            this.initDefaultValue(object);
        }
        this.fieldReader0.readFieldValue(jsonReader, object);
        this.fieldReader1.readFieldValue(jsonReader, object);
        if (jsonReader.current() != ']') {
            throw new JSONException(jsonReader.info("array to bean end error"));
        }
        jsonReader.next();
        return object;
    }
    
    @Override
    public FieldReader getFieldReader(final long hashCode) {
        if (hashCode == this.hashCode0) {
            return this.fieldReader0;
        }
        if (hashCode == this.hashCode1) {
            return this.fieldReader1;
        }
        return null;
    }
    
    @Override
    public FieldReader getFieldReaderLCase(final long hashCode) {
        if (hashCode == this.hashCode0LCase) {
            return this.fieldReader0;
        }
        if (hashCode == this.hashCode1LCase) {
            return this.fieldReader1;
        }
        return null;
    }
}
