// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import java.util.Map;
import com.alibaba.fastjson2.JSONPath;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Parameter;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.codec.FieldInfo;
import java.lang.reflect.AccessibleObject;
import com.alibaba.fastjson2.internal.asm.ASMUtils;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Arrays;
import com.alibaba.fastjson2.util.BeanUtils;
import java.lang.reflect.Constructor;
import java.util.List;

final class ObjectReaderException<T> extends ObjectReaderAdapter<T>
{
    static final long HASH_TYPE;
    static final long HASH_MESSAGE;
    static final long HASH_DETAIL_MESSAGE;
    static final long HASH_LOCALIZED_MESSAGE;
    static final long HASH_CAUSE;
    static final long HASH_STACKTRACE;
    static final long HASH_SUPPRESSED_EXCEPTIONS;
    private final FieldReader fieldReaderStackTrace;
    final List<Constructor> constructors;
    final Constructor constructorDefault;
    final Constructor constructorMessage;
    final Constructor constructorMessageCause;
    final Constructor constructorCause;
    final List<String[]> constructorParameters;
    
    ObjectReaderException(final Class<T> objectClass) {
        this(objectClass, (List)Arrays.asList((Constructor[])BeanUtils.getConstructor(objectClass)), (FieldReader[])new FieldReader[] { ObjectReaders.fieldReader("stackTrace", StackTraceElement[].class, Throwable::setStackTrace) });
    }
    
    ObjectReaderException(final Class<T> objectClass, final List<Constructor> constructors, final FieldReader... fieldReaders) {
        super(objectClass, null, objectClass.getName(), 0L, null, null, (Function)null, (FieldReader[])fieldReaders);
        this.constructors = constructors;
        Constructor constructorDefault = null;
        Constructor constructorMessage = null;
        Constructor constructorMessageCause = null;
        Constructor constructorCause = null;
        for (final Constructor constructor : constructors) {
            if (constructor != null && constructorMessageCause == null) {
                final int paramCount = constructor.getParameterCount();
                if (paramCount == 0) {
                    constructorDefault = constructor;
                }
                else {
                    final Class[] paramTypes = constructor.getParameterTypes();
                    final Class paramType0 = paramTypes[0];
                    if (paramCount == 1) {
                        if (paramType0 == String.class) {
                            constructorMessage = constructor;
                        }
                        else if (Throwable.class.isAssignableFrom(paramType0)) {
                            constructorCause = constructor;
                        }
                    }
                    if (paramCount != 2 || paramType0 != String.class || !Throwable.class.isAssignableFrom(paramTypes[1])) {
                        continue;
                    }
                    constructorMessageCause = constructor;
                }
            }
        }
        this.constructorDefault = constructorDefault;
        this.constructorMessage = constructorMessage;
        this.constructorMessageCause = constructorMessageCause;
        this.constructorCause = constructorCause;
        final int x;
        final int y;
        constructors.sort((left, right) -> {
            x = left.getParameterCount();
            y = right.getParameterCount();
            return Integer.compare(y, x);
        });
        this.constructorParameters = new ArrayList<String[]>(constructors.size());
        for (final Constructor constructor : constructors) {
            final int paramCount = constructor.getParameterCount();
            String[] parameterNames = null;
            if (paramCount > 0) {
                parameterNames = ASMUtils.lookupParameterNames(constructor);
                final Parameter[] parameters = constructor.getParameters();
                final FieldInfo fieldInfo = new FieldInfo();
                for (int i = 0; i < parameters.length && i < parameterNames.length; ++i) {
                    fieldInfo.init();
                    final Parameter parameter = parameters[i];
                    final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
                    provider.getFieldInfo(fieldInfo, objectClass, constructor, i, parameter);
                    if (fieldInfo.fieldName != null) {
                        parameterNames[i] = fieldInfo.fieldName;
                    }
                }
            }
            this.constructorParameters.add(parameterNames);
        }
        FieldReader fieldReaderStackTrace = null;
        for (final FieldReader fieldReader : fieldReaders) {
            if ("stackTrace".equals(fieldReader.fieldName) && fieldReader.fieldClass == StackTraceElement[].class) {
                fieldReaderStackTrace = fieldReader;
            }
        }
        this.fieldReaderStackTrace = fieldReaderStackTrace;
    }
    
    @Override
    public T readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final boolean objectStart = jsonReader.nextIfObjectStart();
        if (!objectStart && jsonReader.nextIfNullOrEmptyString()) {
            return null;
        }
        String message = null;
        String localizedMessage = null;
        Throwable cause = null;
        StackTraceElement[] stackTrace = null;
        List<Throwable> suppressedExceptions = null;
        String stackTraceReference = null;
        String suppressedExceptionsReference = null;
        String causeReference = null;
        Map<String, Object> fieldValues = null;
        Map<String, String> references = null;
        int i = 0;
        while (!jsonReader.nextIfObjectEnd()) {
            final long hash = jsonReader.readFieldNameHashCode();
            if (i == 0 && hash == ObjectReaderException.HASH_TYPE && jsonReader.isSupportAutoType(features)) {
                final long typeHash = jsonReader.readTypeHashCode();
                final JSONReader.Context context = jsonReader.getContext();
                ObjectReader reader = this.autoType(context, typeHash);
                String typeName = null;
                if (reader == null) {
                    typeName = jsonReader.getString();
                    reader = context.getObjectReaderAutoType(typeName, this.objectClass, features);
                    if (reader == null) {
                        throw new JSONException(jsonReader.info("No suitable ObjectReader found for" + typeName));
                    }
                }
                if (reader != this) {
                    return reader.readObject(jsonReader);
                }
            }
            else if (hash == ObjectReaderException.HASH_MESSAGE || hash == ObjectReaderException.HASH_DETAIL_MESSAGE) {
                message = jsonReader.readString();
            }
            else if (hash == ObjectReaderException.HASH_LOCALIZED_MESSAGE) {
                localizedMessage = jsonReader.readString();
            }
            else if (hash == ObjectReaderException.HASH_CAUSE) {
                if (jsonReader.isReference()) {
                    causeReference = jsonReader.readReference();
                }
                else {
                    cause = jsonReader.read(Throwable.class);
                }
            }
            else if (hash == ObjectReaderException.HASH_STACKTRACE) {
                if (jsonReader.isReference()) {
                    stackTraceReference = jsonReader.readReference();
                }
                else {
                    stackTrace = jsonReader.read(StackTraceElement[].class);
                }
            }
            else if (hash == ObjectReaderException.HASH_SUPPRESSED_EXCEPTIONS) {
                if (jsonReader.isReference()) {
                    suppressedExceptionsReference = jsonReader.readReference();
                }
                else if (jsonReader.getType() == -110) {
                    suppressedExceptions = (List<Throwable>)jsonReader.readAny();
                }
                else {
                    suppressedExceptions = (List<Throwable>)jsonReader.readArray(Throwable.class);
                }
            }
            else {
                final FieldReader fieldReader = this.getFieldReader(hash);
                if (fieldValues == null) {
                    fieldValues = new HashMap<String, Object>();
                }
                String name;
                if (fieldReader != null) {
                    name = fieldReader.fieldName;
                }
                else {
                    name = jsonReader.getFieldName();
                }
                if (jsonReader.isReference()) {
                    final String reference = jsonReader.readReference();
                    if (references == null) {
                        references = new HashMap<String, String>();
                    }
                    references.put(name, reference);
                }
                else {
                    Object fieldValue;
                    if (fieldReader != null) {
                        fieldValue = fieldReader.readFieldValue(jsonReader);
                    }
                    else {
                        fieldValue = jsonReader.readAny();
                    }
                    fieldValues.put(name, fieldValue);
                }
            }
            ++i;
        }
        Throwable object = this.createObject(message, cause);
        if (object == null) {
            for (int j = 0; j < this.constructors.size(); ++j) {
                final String[] paramNames = this.constructorParameters.get(j);
                if (paramNames != null) {
                    if (paramNames.length != 0) {
                        boolean matchAll = true;
                        for (final String paramName : paramNames) {
                            if (paramName == null) {
                                matchAll = false;
                                break;
                            }
                            final String s = paramName;
                            switch (s) {
                                case "message":
                                case "cause": {
                                    break;
                                }
                                case "errorIndex": {
                                    if (this.objectClass == DateTimeParseException.class) {
                                        break;
                                    }
                                    if (!fieldValues.containsKey(paramName)) {
                                        matchAll = false;
                                        break;
                                    }
                                    break;
                                }
                                default: {
                                    if (!fieldValues.containsKey(paramName)) {
                                        matchAll = false;
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                        if (matchAll) {
                            final Object[] args = new Object[paramNames.length];
                            for (int k = 0; k < paramNames.length; ++k) {
                                final String s2;
                                final String paramName2 = s2 = paramNames[k];
                                Object fieldValue2 = null;
                                switch (s2) {
                                    case "message": {
                                        fieldValue2 = message;
                                        break;
                                    }
                                    case "cause": {
                                        fieldValue2 = cause;
                                        break;
                                    }
                                    case "errorIndex": {
                                        fieldValue2 = fieldValues.get(paramName2);
                                        if (fieldValue2 == null && this.objectClass == DateTimeParseException.class) {
                                            fieldValue2 = 0;
                                            break;
                                        }
                                        break;
                                    }
                                    default: {
                                        fieldValue2 = fieldValues.get(paramName2);
                                        break;
                                    }
                                }
                                args[k] = fieldValue2;
                            }
                            final Constructor constructor = this.constructors.get(j);
                            try {
                                object = constructor.newInstance(args);
                                break;
                            }
                            catch (Throwable e) {
                                throw new JSONException("create error, objectClass " + constructor + ", " + e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        }
        if (object == null) {
            throw new JSONException(jsonReader.info(jsonReader.info("not support : " + this.objectClass.getName())));
        }
        if (stackTrace != null) {
            int nullCount = 0;
            for (final StackTraceElement item : stackTrace) {
                if (item == null) {
                    ++nullCount;
                }
            }
            if (stackTrace.length == 0 || nullCount != stackTrace.length) {
                object.setStackTrace(stackTrace);
            }
        }
        if (stackTraceReference != null) {
            jsonReader.addResolveTask(this.fieldReaderStackTrace, object, JSONPath.of(stackTraceReference));
        }
        if (fieldValues != null) {
            for (final Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                final FieldReader fieldReader = this.getFieldReader(entry.getKey());
                if (fieldReader != null) {
                    fieldReader.accept(object, entry.getValue());
                }
            }
        }
        if (references != null) {
            for (final Map.Entry<String, String> entry2 : references.entrySet()) {
                final FieldReader fieldReader = this.getFieldReader(entry2.getKey());
                if (fieldReader == null) {
                    continue;
                }
                fieldReader.addResolveTask(jsonReader, object, entry2.getValue());
            }
        }
        return (T)object;
    }
    
    @Override
    public T readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.getType() == -110) {
            final JSONReader.Context context = jsonReader.getContext();
            if (jsonReader.isSupportAutoType(features) || context.getContextAutoTypeBeforeHandler() != null) {
                jsonReader.next();
                final long typeHash = jsonReader.readTypeHashCode();
                ObjectReader autoTypeObjectReader = context.getObjectReaderAutoType(typeHash);
                if (autoTypeObjectReader == null) {
                    final String typeName = jsonReader.getString();
                    autoTypeObjectReader = context.getObjectReaderAutoType(typeName, null);
                    if (autoTypeObjectReader == null) {
                        throw new JSONException("auoType not support : " + typeName + ", offset " + jsonReader.getOffset());
                    }
                }
                return autoTypeObjectReader.readJSONBObject(jsonReader, fieldType, fieldName, 0L);
            }
        }
        return this.readObject(jsonReader, fieldType, fieldName, features);
    }
    
    private Throwable createObject(final String message, final Throwable cause) {
        try {
            if (this.constructorMessageCause != null && cause != null && message != null) {
                return this.constructorMessageCause.newInstance(message, cause);
            }
            if (this.constructorMessage != null && message != null) {
                return this.constructorMessage.newInstance(message);
            }
            if (this.constructorCause != null && cause != null) {
                return this.constructorCause.newInstance(cause);
            }
            if (this.constructorMessageCause != null && (cause != null || message != null)) {
                return this.constructorMessageCause.newInstance(message, cause);
            }
            if (this.constructorDefault != null) {
                return this.constructorDefault.newInstance(new Object[0]);
            }
            if (this.constructorMessageCause != null) {
                return this.constructorMessageCause.newInstance(message, cause);
            }
            if (this.constructorMessage != null) {
                return this.constructorMessage.newInstance(message);
            }
            if (this.constructorCause != null) {
                return this.constructorCause.newInstance(cause);
            }
        }
        catch (Throwable e) {
            throw new JSONException("create Exception error, class " + this.objectClass.getName() + ", " + e.getMessage(), e);
        }
        return null;
    }
    
    static {
        HASH_TYPE = Fnv.hashCode64("@type");
        HASH_MESSAGE = Fnv.hashCode64("message");
        HASH_DETAIL_MESSAGE = Fnv.hashCode64("detailMessage");
        HASH_LOCALIZED_MESSAGE = Fnv.hashCode64("localizedMessage");
        HASH_CAUSE = Fnv.hashCode64("cause");
        HASH_STACKTRACE = Fnv.hashCode64("stackTrace");
        HASH_SUPPRESSED_EXCEPTIONS = Fnv.hashCode64("suppressedExceptions");
    }
}
