// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import java.util.function.Function;
import java.util.Iterator;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import java.util.Map;
import java.util.Collection;

public interface ObjectReader<T>
{
    public static final long HASH_TYPE = Fnv.hashCode64("@type");
    
    default T createInstance() {
        return this.createInstance(0L);
    }
    
    default T createInstance(final long features) {
        throw new UnsupportedOperationException();
    }
    
    default T createInstance(final Collection collection) {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
    
    default void acceptExtra(final Object object, final String fieldName, final Object fieldValue) {
    }
    
    default T createInstance(final Map map, final JSONReader.Feature... features) {
        long featuresValue = 0L;
        for (int i = 0; i < features.length; ++i) {
            featuresValue |= features[i].mask;
        }
        return this.createInstance(map, featuresValue);
    }
    
    default T createInstance(final Map map, final long features) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final Object typeKey = map.get(this.getTypeKey());
        if (typeKey instanceof String) {
            final String typeName = (String)typeKey;
            final long typeHash = Fnv.hashCode64(typeName);
            ObjectReader<T> reader = null;
            if ((features & JSONReader.Feature.SupportAutoType.mask) != 0x0L || this instanceof ObjectReaderSeeAlso) {
                reader = this.autoType(provider, typeHash);
            }
            if (reader == null) {
                reader = provider.getObjectReader(typeName, this.getObjectClass(), features | this.getFeatures());
            }
            if (reader != this && reader != null) {
                return reader.createInstance(map, features);
            }
        }
        final T object = this.createInstance(0L);
        for (final Map.Entry entry : map.entrySet()) {
            final String entryKey = entry.getKey().toString();
            final Object fieldValue = entry.getValue();
            final FieldReader fieldReader = this.getFieldReader(entryKey);
            if (fieldReader == null) {
                this.acceptExtra(object, entryKey, entry.getValue());
            }
            else {
                fieldReader.acceptAny(object, fieldValue, features);
            }
        }
        final Function buildFunction = this.getBuildFunction();
        if (buildFunction != null) {
            return buildFunction.apply(object);
        }
        return object;
    }
    
    default T createInstanceNoneDefaultConstructor(final Map<Long, Object> values) {
        throw new UnsupportedOperationException();
    }
    
    default long getFeatures() {
        return 0L;
    }
    
    default String getTypeKey() {
        return "@type";
    }
    
    default long getTypeKeyHash() {
        return ObjectReader.HASH_TYPE;
    }
    
    default Class<T> getObjectClass() {
        return null;
    }
    
    default FieldReader getFieldReader(final long hashCode) {
        return null;
    }
    
    default FieldReader getFieldReaderLCase(final long hashCode) {
        return null;
    }
    
    default boolean setFieldValue(final Object object, final String fieldName, final long fieldNameHashCode, final int value) {
        final FieldReader fieldReader = this.getFieldReader(fieldNameHashCode);
        if (fieldReader == null) {
            return false;
        }
        fieldReader.accept(object, value);
        return true;
    }
    
    default boolean setFieldValue(final Object object, final String fieldName, final long fieldNameHashCode, final long value) {
        final FieldReader fieldReader = this.getFieldReader(fieldNameHashCode);
        if (fieldReader == null) {
            return false;
        }
        fieldReader.accept(object, value);
        return true;
    }
    
    default FieldReader getFieldReader(final String fieldName) {
        final long fieldNameHash = Fnv.hashCode64(fieldName);
        FieldReader fieldReader = this.getFieldReader(fieldNameHash);
        if (fieldReader == null) {
            final long fieldNameHashLCase = Fnv.hashCode64LCase(fieldName);
            if (fieldNameHashLCase != fieldNameHash) {
                fieldReader = this.getFieldReaderLCase(fieldNameHashLCase);
            }
        }
        return fieldReader;
    }
    
    default Function getBuildFunction() {
        return null;
    }
    
    default ObjectReader autoType(final JSONReader.Context context, final long typeHash) {
        return context.getObjectReaderAutoType(typeHash);
    }
    
    default ObjectReader autoType(final ObjectReaderProvider provider, final long typeHash) {
        return provider.getObjectReader(typeHash);
    }
    
    default T readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.isArray() && jsonReader.isSupportBeanArray()) {
            return this.readArrayMappingJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        T object = null;
        jsonReader.nextIfObjectStart();
        int i = 0;
        while (!jsonReader.nextIfObjectEnd()) {
            final long hash = jsonReader.readFieldNameHashCode();
            if (hash == this.getTypeKeyHash() && i == 0) {
                final long typeHash = jsonReader.readTypeHashCode();
                final JSONReader.Context context = jsonReader.getContext();
                ObjectReader reader = this.autoType(context, typeHash);
                if (reader == null) {
                    final String typeName = jsonReader.getString();
                    reader = context.getObjectReaderAutoType(typeName, null);
                    if (reader == null) {
                        throw new JSONException(jsonReader.info("No suitable ObjectReader found for" + typeName));
                    }
                }
                if (reader != this) {
                    return (T)reader.readJSONBObject(jsonReader, fieldType, fieldName, features);
                }
            }
            else if (hash != 0L) {
                FieldReader fieldReader = this.getFieldReader(hash);
                if (fieldReader == null && jsonReader.isSupportSmartMatch(features | this.getFeatures())) {
                    final long nameHashCodeLCase = jsonReader.getNameHashCodeLCase();
                    fieldReader = this.getFieldReaderLCase(nameHashCodeLCase);
                }
                if (fieldReader == null) {
                    jsonReader.skipValue();
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
        return object;
    }
    
    default T readArrayMappingJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        throw new UnsupportedOperationException();
    }
    
    default T readArrayMappingObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        throw new UnsupportedOperationException();
    }
    
    default T readObject(final JSONReader jsonReader) {
        return this.readObject(jsonReader, null, null, this.getFeatures());
    }
    
    default T readObject(final JSONReader jsonReader, final long features) {
        return this.readObject(jsonReader, null, null, features);
    }
    
    T readObject(final JSONReader p0, final Type p1, final Object p2, final long p3);
}
