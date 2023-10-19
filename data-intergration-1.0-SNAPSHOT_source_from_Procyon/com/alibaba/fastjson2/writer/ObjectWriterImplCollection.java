// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.util.Iterator;
import java.util.SortedSet;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import java.lang.reflect.ParameterizedType;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;

final class ObjectWriterImplCollection extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplCollection INSTANCE;
    static final byte[] LINKED_HASH_SET_JSONB_TYPE_NAME_BYTES;
    static final long LINKED_HASH_SET_JSONB_TYPE_HASH;
    static final byte[] TREE_SET_JSONB_TYPE_NAME_BYTES;
    static final long TREE_SET_JSONB_TYPE_HASH;
    Type itemType;
    long features;
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        Type fieldItemType = null;
        Class fieldClass = null;
        if (fieldType instanceof Class) {
            fieldClass = (Class)fieldType;
        }
        else if (fieldType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)fieldType;
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                fieldItemType = actualTypeArguments[0];
            }
            final Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                fieldClass = (Class)rawType;
            }
        }
        final Collection collection = (Collection)object;
        final Class<?> objectClass = object.getClass();
        boolean writeTypeInfo = jsonWriter.isWriteTypeInfo(object, fieldClass);
        if (writeTypeInfo) {
            if (fieldClass == Set.class && objectClass == HashSet.class) {
                writeTypeInfo = false;
            }
            else if (fieldType == Collection.class && objectClass == ArrayList.class) {
                writeTypeInfo = false;
            }
        }
        if (writeTypeInfo) {
            if (objectClass == LinkedHashSet.class) {
                jsonWriter.writeTypeName(ObjectWriterImplCollection.LINKED_HASH_SET_JSONB_TYPE_NAME_BYTES, ObjectWriterImplCollection.LINKED_HASH_SET_JSONB_TYPE_HASH);
            }
            else if (objectClass == TreeSet.class) {
                jsonWriter.writeTypeName(ObjectWriterImplCollection.TREE_SET_JSONB_TYPE_NAME_BYTES, ObjectWriterImplCollection.TREE_SET_JSONB_TYPE_HASH);
            }
            else {
                jsonWriter.writeTypeName(TypeUtils.getTypeName(objectClass));
            }
        }
        boolean refDetect = jsonWriter.isRefDetect();
        if (collection.size() > 1 && !(collection instanceof SortedSet) && !(collection instanceof LinkedHashSet)) {
            refDetect = false;
        }
        jsonWriter.startArray(collection.size());
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        int i = 0;
        for (final Object item : collection) {
            Label_0431: {
                if (item == null) {
                    jsonWriter.writeNull();
                }
                else {
                    final Class<?> itemClass = item.getClass();
                    ObjectWriter itemObjectWriter;
                    if (itemClass == previousClass) {
                        itemObjectWriter = previousObjectWriter;
                    }
                    else {
                        itemObjectWriter = jsonWriter.getObjectWriter(itemClass);
                        previousClass = itemClass;
                        previousObjectWriter = itemObjectWriter;
                    }
                    final boolean itemRefDetect = refDetect && !ObjectWriterProvider.isNotReferenceDetect(itemClass);
                    if (itemRefDetect) {
                        final String refPath = jsonWriter.setPath(i, item);
                        if (refPath != null) {
                            jsonWriter.writeReference(refPath);
                            jsonWriter.popPath(item);
                            break Label_0431;
                        }
                    }
                    itemObjectWriter.writeJSONB(jsonWriter, item, i, fieldItemType, features);
                    if (itemRefDetect) {
                        jsonWriter.popPath(item);
                    }
                }
            }
            ++i;
        }
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (jsonWriter.jsonb) {
            this.writeJSONB(jsonWriter, object, fieldName, fieldType, features);
            return;
        }
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final long features2;
        if (object instanceof Set && jsonWriter.isWriteTypeInfo(object, features2 = jsonWriter.getFeatures(features | this.features)) && (features2 & JSONWriter.Feature.NotWriteSetClassName.mask) == 0x0L) {
            jsonWriter.writeRaw("Set");
        }
        final Iterable iterable = (Iterable)object;
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        jsonWriter.startArray();
        int i = 0;
        for (final Object o : iterable) {
            if (i != 0) {
                jsonWriter.writeComma();
            }
            if (o == null) {
                jsonWriter.writeNull();
                ++i;
            }
            else {
                final Class<?> itemClass = o.getClass();
                ObjectWriter itemObjectWriter;
                if (itemClass == previousClass) {
                    itemObjectWriter = previousObjectWriter;
                }
                else {
                    itemObjectWriter = jsonWriter.getObjectWriter(itemClass);
                    previousClass = itemClass;
                    previousObjectWriter = itemObjectWriter;
                }
                itemObjectWriter.write(jsonWriter, o, i, this.itemType, this.features);
                ++i;
            }
        }
        jsonWriter.endArray();
    }
    
    static {
        INSTANCE = new ObjectWriterImplCollection();
        LINKED_HASH_SET_JSONB_TYPE_NAME_BYTES = JSONB.toBytes(TypeUtils.getTypeName(LinkedHashSet.class));
        LINKED_HASH_SET_JSONB_TYPE_HASH = Fnv.hashCode64(TypeUtils.getTypeName(LinkedHashSet.class));
        TREE_SET_JSONB_TYPE_NAME_BYTES = JSONB.toBytes(TypeUtils.getTypeName(TreeSet.class));
        TREE_SET_JSONB_TYPE_HASH = Fnv.hashCode64(TypeUtils.getTypeName(TreeSet.class));
    }
}
