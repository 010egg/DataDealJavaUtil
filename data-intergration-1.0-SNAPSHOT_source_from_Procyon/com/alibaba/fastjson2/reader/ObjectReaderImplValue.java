// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Array;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.function.Function;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public class ObjectReaderImplValue<I, T> implements ObjectReader<T>
{
    final Type valueType;
    final Class<I> valueClass;
    final long features;
    final Constructor<T> constructor;
    final Method factoryMethod;
    final Function<I, T> function;
    final JSONSchema schema;
    final Object emptyVariantArgs;
    ObjectReader valueReader;
    
    public ObjectReaderImplValue(final Class<T> objectClass, final Type valueType, final Class<I> valueClass, final long features, final String format, final Object defaultValue, final JSONSchema schema, final Constructor<T> constructor, final Method factoryMethod, final Function<I, T> function) {
        this.valueType = valueType;
        this.valueClass = valueClass;
        this.features = features;
        this.schema = schema;
        this.constructor = constructor;
        this.factoryMethod = factoryMethod;
        this.function = function;
        if (factoryMethod != null && factoryMethod.getParameterCount() == 2) {
            final Class<?> varArgType = factoryMethod.getParameterTypes()[1].getComponentType();
            this.emptyVariantArgs = Array.newInstance(varArgType, 0);
        }
        else {
            this.emptyVariantArgs = null;
        }
    }
    
    @Override
    public T readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return this.readObject(jsonReader, fieldType, fieldName, features);
    }
    
    @Override
    public T readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (this.valueReader == null) {
            this.valueReader = jsonReader.getObjectReader(this.valueType);
        }
        final I value = this.valueReader.readObject(jsonReader, fieldType, fieldName, features | this.features);
        if (value == null) {
            return null;
        }
        if (this.schema != null) {
            this.schema.validate(value);
        }
        if (this.function != null) {
            try {
                final T object = this.function.apply(value);
                return object;
            }
            catch (Exception ex) {
                throw new JSONException(jsonReader.info("create object error"), ex);
            }
        }
        if (this.constructor != null) {
            try {
                final T object = this.constructor.newInstance(value);
                return object;
            }
            catch (Exception ex) {
                throw new JSONException(jsonReader.info("create object error"), ex);
            }
        }
        if (this.factoryMethod != null) {
            try {
                T object;
                if (this.emptyVariantArgs != null) {
                    object = (T)this.factoryMethod.invoke(null, value, this.emptyVariantArgs);
                }
                else {
                    object = (T)this.factoryMethod.invoke(null, value);
                }
                return object;
            }
            catch (Exception ex) {
                throw new JSONException(jsonReader.info("create object error"), ex);
            }
        }
        throw new JSONException(jsonReader.info("create object error"));
    }
    
    public static <I, T> ObjectReaderImplValue<I, T> of(final Class<T> objectClass, final Class<I> valueClass, final Method method) {
        return new ObjectReaderImplValue<I, T>(objectClass, valueClass, valueClass, 0L, null, null, null, null, method, null);
    }
    
    public static <I, T> ObjectReaderImplValue<I, T> of(final Class<T> objectClass, final Class<I> valueClass, final Function<I, T> function) {
        return new ObjectReaderImplValue<I, T>(objectClass, valueClass, valueClass, 0L, null, null, null, null, null, function);
    }
}
