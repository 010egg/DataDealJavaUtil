// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

public class ObjectReaderImplField implements ObjectReader
{
    static final long HASH_DECLARING_CLASS;
    static final long HASH_NAME;
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return this.readObject(jsonReader, fieldType, fieldName, features);
    }
    
    @Override
    public Object readArrayMappingJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final int entryCount = jsonReader.startArray();
        if (entryCount != 2) {
            throw new JSONException("not support input " + jsonReader.info());
        }
        final String declaringClassName = jsonReader.readString();
        final String methodName = jsonReader.readString();
        return this.getField(jsonReader.getContext().getFeatures() | features, methodName, declaringClassName);
    }
    
    @Override
    public Object readArrayMappingObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final boolean arrayStart = jsonReader.nextIfArrayStart();
        if (!arrayStart) {
            throw new JSONException("not support input " + jsonReader.info());
        }
        final String declaringClassName = jsonReader.readString();
        final String methodName = jsonReader.readString();
        final boolean arrayEnd = jsonReader.nextIfArrayEnd();
        if (!arrayEnd) {
            throw new JSONException("not support input " + jsonReader.info());
        }
        jsonReader.nextIfComma();
        return this.getField(jsonReader.getContext().getFeatures() | features, methodName, declaringClassName);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final boolean objectStart = jsonReader.nextIfObjectStart();
        if (objectStart) {
            String methodName = null;
            String declaringClassName = null;
            while (!jsonReader.nextIfObjectEnd()) {
                final long nameHashCode = jsonReader.readFieldNameHashCode();
                if (nameHashCode == ObjectReaderImplField.HASH_DECLARING_CLASS) {
                    declaringClassName = jsonReader.readString();
                }
                else if (nameHashCode == ObjectReaderImplField.HASH_NAME) {
                    methodName = jsonReader.readString();
                }
                else {
                    jsonReader.skipValue();
                }
            }
            if (!jsonReader.jsonb) {
                jsonReader.nextIfComma();
            }
            return this.getField(jsonReader.getContext().getFeatures() | features, methodName, declaringClassName);
        }
        if (!jsonReader.isSupportBeanArray(features)) {
            throw new JSONException("not support input " + jsonReader.info());
        }
        if (jsonReader.jsonb) {
            return this.readArrayMappingJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        return this.readArrayMappingObject(jsonReader, fieldType, fieldName, features);
    }
    
    private Field getField(final long features, final String methodName, final String declaringClassName) {
        final boolean supportClassForName = (features & JSONReader.Feature.SupportClassForName.mask) != 0x0L;
        if (!supportClassForName) {
            throw new JSONException("ClassForName not support");
        }
        final Class declaringClass = TypeUtils.loadClass(declaringClassName);
        try {
            return declaringClass.getDeclaredField(methodName);
        }
        catch (NoSuchFieldException e) {
            throw new JSONException("method not found", e);
        }
    }
    
    static {
        HASH_DECLARING_CLASS = Fnv.hashCode64("declaringClass");
        HASH_NAME = Fnv.hashCode64("name");
    }
}
