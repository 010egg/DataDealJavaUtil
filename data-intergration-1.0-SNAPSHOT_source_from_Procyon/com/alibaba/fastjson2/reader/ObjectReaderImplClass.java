// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

final class ObjectReaderImplClass extends ObjectReaderPrimitive
{
    static final ObjectReaderImplClass INSTANCE;
    static final long TYPE_HASH;
    
    ObjectReaderImplClass() {
        super(Class.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfMatch((byte)(-110))) {
            final long valueHashCode = jsonReader.readTypeHashCode();
            if (valueHashCode != ObjectReaderImplClass.TYPE_HASH) {
                throw new JSONException(jsonReader.info("not support autoType : " + jsonReader.getString()));
            }
        }
        return this.readObject(jsonReader, fieldType, fieldName, features);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final long classNameHash = jsonReader.readValueHashCode();
        final JSONReader.Context context = jsonReader.getContext();
        final JSONReader.AutoTypeBeforeHandler typeFilter = context.getContextAutoTypeBeforeHandler();
        if (typeFilter != null) {
            Class<?> filterClass = typeFilter.apply(classNameHash, Class.class, features);
            if (filterClass == null) {
                final String className = jsonReader.getString();
                filterClass = typeFilter.apply(className, Class.class, features);
            }
            if (filterClass != null) {
                return filterClass;
            }
        }
        final String className2 = jsonReader.getString();
        final boolean classForName = ((context.getFeatures() | features) & JSONReader.Feature.SupportClassForName.mask) != 0x0L;
        if (!classForName) {
            final String msg = jsonReader.info("not support ClassForName : " + className2 + ", you can config 'JSONReader.Feature.SupportClassForName'");
            throw new JSONException(msg);
        }
        final Class mappingClass = TypeUtils.getMapping(className2);
        if (mappingClass != null) {
            return mappingClass;
        }
        final ObjectReaderProvider provider = context.getProvider();
        final Class<?> resolvedClass = provider.checkAutoType(className2, null, JSONReader.Feature.SupportAutoType.mask);
        if (resolvedClass == null) {
            throw new JSONException(jsonReader.info("class not found " + className2));
        }
        return resolvedClass;
    }
    
    static {
        INSTANCE = new ObjectReaderImplClass();
        TYPE_HASH = Fnv.hashCode64("java.lang.Class");
    }
}
