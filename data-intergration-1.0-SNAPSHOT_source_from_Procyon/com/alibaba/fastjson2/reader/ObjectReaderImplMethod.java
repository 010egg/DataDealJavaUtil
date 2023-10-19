// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.List;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Method;

public class ObjectReaderImplMethod implements ObjectReader<Method>
{
    static final long HASH_DECLARING_CLASS;
    static final long HASH_NAME;
    static final long HASH_PARAMETER_TYPES;
    
    @Override
    public Method readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return this.readObject(jsonReader, fieldType, fieldName, features);
    }
    
    @Override
    public Method readArrayMappingJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final int entryCount = jsonReader.startArray();
        if (entryCount != 3) {
            throw new JSONException("not support input " + jsonReader.info());
        }
        final String declaringClassName = jsonReader.readString();
        final String methodName = jsonReader.readString();
        final List<String> paramTypeNames = (List<String>)jsonReader.readArray(String.class);
        return this.getMethod(jsonReader.getContext().getFeatures() | features, methodName, declaringClassName, paramTypeNames);
    }
    
    @Override
    public Method readArrayMappingObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final boolean arrayStart = jsonReader.nextIfArrayStart();
        if (!arrayStart) {
            throw new JSONException("not support input " + jsonReader.info());
        }
        final String declaringClassName = jsonReader.readString();
        final String methodName = jsonReader.readString();
        final List<String> paramTypeNames = (List<String>)jsonReader.readArray(String.class);
        final boolean arrayEnd = jsonReader.nextIfArrayEnd();
        if (!arrayEnd) {
            throw new JSONException("not support input " + jsonReader.info());
        }
        jsonReader.nextIfComma();
        return this.getMethod(jsonReader.getContext().getFeatures() | features, methodName, declaringClassName, paramTypeNames);
    }
    
    @Override
    public Method readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final boolean objectStart = jsonReader.nextIfObjectStart();
        if (objectStart) {
            String methodName = null;
            String declaringClassName = null;
            List<String> paramTypeNames = null;
            while (!jsonReader.nextIfObjectEnd()) {
                final long nameHashCode = jsonReader.readFieldNameHashCode();
                if (nameHashCode == ObjectReaderImplMethod.HASH_DECLARING_CLASS) {
                    declaringClassName = jsonReader.readString();
                }
                else if (nameHashCode == ObjectReaderImplMethod.HASH_NAME) {
                    methodName = jsonReader.readString();
                }
                else if (nameHashCode == ObjectReaderImplMethod.HASH_PARAMETER_TYPES) {
                    paramTypeNames = (List<String>)jsonReader.readArray(String.class);
                }
                else {
                    jsonReader.skipValue();
                }
            }
            if (!jsonReader.jsonb) {
                jsonReader.nextIfComma();
            }
            return this.getMethod(jsonReader.getContext().getFeatures() | features, methodName, declaringClassName, paramTypeNames);
        }
        if (!jsonReader.isSupportBeanArray(features)) {
            throw new JSONException("not support input " + jsonReader.info());
        }
        if (jsonReader.jsonb) {
            return this.readArrayMappingJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        return this.readArrayMappingObject(jsonReader, fieldType, fieldName, features);
    }
    
    private Method getMethod(final long features, final String methodName, final String declaringClassName, final List<String> paramTypeNames) {
        final boolean supportClassForName = (features & JSONReader.Feature.SupportClassForName.mask) != 0x0L;
        if (!supportClassForName) {
            throw new JSONException("ClassForName not support");
        }
        final Class declaringClass = TypeUtils.loadClass(declaringClassName);
        Class[] paramTypes;
        if (paramTypeNames == null) {
            paramTypes = new Class[0];
        }
        else {
            paramTypes = new Class[paramTypeNames.size()];
            for (int i = 0; i < paramTypeNames.size(); ++i) {
                final String paramTypeName = paramTypeNames.get(i);
                paramTypes[i] = TypeUtils.loadClass(paramTypeName);
            }
        }
        try {
            return declaringClass.getDeclaredMethod(methodName, (Class[])paramTypes);
        }
        catch (NoSuchMethodException e) {
            throw new JSONException("method not found", e);
        }
    }
    
    static {
        HASH_DECLARING_CLASS = Fnv.hashCode64("declaringClass");
        HASH_NAME = Fnv.hashCode64("name");
        HASH_PARAMETER_TYPES = Fnv.hashCode64("parameterTypes");
    }
}
