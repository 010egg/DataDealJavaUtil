// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Collection;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.Function;

public final class ObjectReaderImplInt64ValueArray extends ObjectReaderPrimitive
{
    static final ObjectReaderImplInt64ValueArray INSTANCE;
    public static final long HASH_TYPE;
    final Function<long[], Object> builder;
    
    ObjectReaderImplInt64ValueArray(final Class objectClass, final Function<long[], Object> builder) {
        super(objectClass);
        this.builder = builder;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final long[] array = jsonReader.readInt64ValueArray();
        if (array != null && this.builder != null) {
            return this.builder.apply(array);
        }
        return array;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final long[] array = jsonReader.readInt64ValueArray();
        if (array != null && this.builder != null) {
            return this.builder.apply(array);
        }
        return array;
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        final long[] array = new long[collection.size()];
        int i = 0;
        for (final Object item : collection) {
            long value;
            if (item == null) {
                value = 0L;
            }
            else if (item instanceof Number) {
                value = ((Number)item).longValue();
            }
            else {
                final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(item.getClass(), Long.TYPE);
                if (typeConvert == null) {
                    throw new JSONException("can not cast to long " + item.getClass());
                }
                value = typeConvert.apply(item);
            }
            array[i++] = value;
        }
        if (this.builder != null) {
            return this.builder.apply(array);
        }
        return array;
    }
    
    static {
        INSTANCE = new ObjectReaderImplInt64ValueArray(long[].class, null);
        HASH_TYPE = Fnv.hashCode64("[J");
    }
}
