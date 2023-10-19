// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.lang.reflect.Type;
import java.util.Locale;

class ObjectReaderImplOptional extends ObjectReaderPrimitive
{
    static final ObjectReaderImplOptional INSTANCE;
    final String format;
    final Locale locale;
    final Type itemType;
    final Class itemClass;
    ObjectReader itemObjectReader;
    
    static ObjectReaderImplOptional of(final Type type, final String format, final Locale locale) {
        if (type == null) {
            return ObjectReaderImplOptional.INSTANCE;
        }
        return new ObjectReaderImplOptional(type, format, locale);
    }
    
    public ObjectReaderImplOptional(final Type type, final String format, final Locale locale) {
        super(Optional.class);
        Type itemType = null;
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)type;
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                itemType = actualTypeArguments[0];
            }
        }
        this.itemType = itemType;
        this.itemClass = TypeUtils.getClass(itemType);
        this.format = format;
        this.locale = locale;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        Object value;
        if (this.itemType == null) {
            value = jsonReader.readAny();
        }
        else {
            if (this.itemObjectReader == null) {
                ObjectReader formattedObjectReader = null;
                if (this.format != null) {
                    formattedObjectReader = FieldReader.createFormattedObjectReader(this.itemType, this.itemClass, this.format, this.locale);
                }
                if (formattedObjectReader == null) {
                    this.itemObjectReader = jsonReader.getObjectReader(this.itemType);
                }
                else {
                    this.itemObjectReader = formattedObjectReader;
                }
            }
            value = this.itemObjectReader.readJSONBObject(jsonReader, this.itemType, fieldName, 0L);
        }
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        Object value;
        if (this.itemType == null) {
            value = jsonReader.readAny();
        }
        else {
            if (this.itemObjectReader == null) {
                ObjectReader formattedObjectReader = null;
                if (this.format != null) {
                    formattedObjectReader = FieldReader.createFormattedObjectReader(this.itemType, this.itemClass, this.format, this.locale);
                }
                if (formattedObjectReader == null) {
                    this.itemObjectReader = jsonReader.getObjectReader(this.itemType);
                }
                else {
                    this.itemObjectReader = formattedObjectReader;
                }
            }
            value = this.itemObjectReader.readObject(jsonReader, this.itemType, fieldName, 0L);
        }
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }
    
    static {
        INSTANCE = new ObjectReaderImplOptional(null, null, null);
    }
}
