// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.util.Iterator;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.ArrayList;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;

final class ObjectWriterImplList extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplList INSTANCE;
    static final ObjectWriterImplList INSTANCE_JSON_ARRAY;
    static final ObjectWriterImplList INSTANCE_JSON_ARRAY_1x;
    static final Class CLASS_SUBLIST;
    static final String TYPE_NAME_ARRAY_LIST;
    static final byte[] TYPE_NAME_JSONB_ARRAY_LIST;
    static final long TYPE_NAME_HASH_ARRAY_LIST;
    final Class defineClass;
    final Type defineType;
    final Class itemClass;
    final Type itemType;
    final long features;
    final boolean itemClassRefDetect;
    volatile ObjectWriter itemClassWriter;
    
    public ObjectWriterImplList(final Class defineClass, final Type defineType, final Class itemClass, final Type itemType, final long features) {
        this.defineClass = defineClass;
        this.defineType = defineType;
        this.itemClass = itemClass;
        this.itemType = itemType;
        this.features = features;
        this.itemClassRefDetect = (itemClass != null && !ObjectWriterProvider.isNotReferenceDetect(itemClass));
    }
    
    @Override
    public void writeArrayMappingJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        final List list = this.getList(object);
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        jsonWriter.startArray(list.size());
        for (int i = 0; i < list.size(); ++i) {
            final Object item = list.get(i);
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
                itemObjectWriter.writeArrayMappingJSONB(jsonWriter, item, i, this.itemType, this.features | features);
            }
        }
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        Class fieldItemClass = null;
        Class fieldClass = null;
        if (fieldType instanceof Class) {
            fieldClass = (Class)fieldType;
        }
        else if (fieldType == this.defineType) {
            fieldClass = this.itemClass;
        }
        else if (fieldType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)fieldType;
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                final Type fieldItemType = actualTypeArguments[0];
                if (fieldItemType instanceof Class) {
                    fieldItemClass = (Class)fieldItemType;
                }
            }
            final Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                fieldClass = (Class)rawType;
            }
        }
        final Class<?> objectClass = object.getClass();
        if (jsonWriter.isWriteTypeInfo(object, fieldClass, features)) {
            if (objectClass == ObjectWriterImplList.CLASS_SUBLIST || objectClass == ArrayList.class) {
                jsonWriter.writeTypeName(ObjectWriterImplList.TYPE_NAME_JSONB_ARRAY_LIST, ObjectWriterImplList.TYPE_NAME_HASH_ARRAY_LIST);
            }
            else {
                final String typeName = TypeUtils.getTypeName(objectClass);
                jsonWriter.writeTypeName(typeName);
            }
        }
        final List list = this.getList(object);
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        final int size = list.size();
        if (size == 0) {
            jsonWriter.writeRaw((byte)(-108));
            return;
        }
        final boolean beanToArray = jsonWriter.isBeanToArray();
        if (beanToArray) {
            jsonWriter.startArray(size);
            for (int i = 0; i < size; ++i) {
                final Object item = list.get(i);
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
                    itemObjectWriter.writeArrayMappingJSONB(jsonWriter, item, i, fieldItemClass, features);
                }
            }
            jsonWriter.endArray();
            return;
        }
        final JSONWriter.Context context = jsonWriter.context;
        jsonWriter.startArray(size);
        for (int j = 0; j < size; ++j) {
            final Object item2 = list.get(j);
            if (item2 == null) {
                jsonWriter.writeNull();
            }
            else {
                final Class<?> itemClass2 = item2.getClass();
                if (itemClass2 == String.class) {
                    jsonWriter.writeString((String)item2);
                }
                else {
                    boolean refDetect = (itemClass2 == this.itemClass) ? (this.itemClassRefDetect && jsonWriter.isRefDetect()) : jsonWriter.isRefDetect(item2);
                    ObjectWriter itemObjectWriter2;
                    if (itemClass2 == this.itemClass && this.itemClassWriter != null) {
                        itemObjectWriter2 = this.itemClassWriter;
                    }
                    else if (itemClass2 == previousClass) {
                        itemObjectWriter2 = previousObjectWriter;
                    }
                    else {
                        refDetect = jsonWriter.isRefDetect();
                        if (itemClass2 == JSONObject.class) {
                            itemObjectWriter2 = ObjectWriterImplMap.INSTANCE;
                        }
                        else if (itemClass2 == TypeUtils.CLASS_JSON_OBJECT_1x) {
                            itemObjectWriter2 = ObjectWriterImplMap.INSTANCE_1x;
                        }
                        else if (itemClass2 == JSONArray.class) {
                            itemObjectWriter2 = ObjectWriterImplList.INSTANCE_JSON_ARRAY;
                        }
                        else if (itemClass2 == TypeUtils.CLASS_JSON_ARRAY_1x) {
                            itemObjectWriter2 = ObjectWriterImplList.INSTANCE_JSON_ARRAY_1x;
                        }
                        else {
                            itemObjectWriter2 = context.getObjectWriter(itemClass2);
                        }
                        previousClass = itemClass2;
                        previousObjectWriter = itemObjectWriter2;
                        if (itemClass2 == this.itemClass) {
                            this.itemClassWriter = itemObjectWriter2;
                        }
                    }
                    if (refDetect) {
                        final String refPath = jsonWriter.setPath(j, item2);
                        if (refPath != null) {
                            jsonWriter.writeReference(refPath);
                            jsonWriter.popPath(item2);
                            continue;
                        }
                    }
                    itemObjectWriter2.writeJSONB(jsonWriter, item2, j, this.itemType, this.features);
                    if (refDetect) {
                        jsonWriter.popPath(item2);
                    }
                }
            }
        }
        jsonWriter.endArray();
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        final List list = this.getList(object);
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        boolean previousRefDetect = true;
        if (jsonWriter.jsonb) {
            jsonWriter.startArray(list.size());
            for (int i = 0; i < list.size(); ++i) {
                final Object item = list.get(i);
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
                    itemObjectWriter.writeJSONB(jsonWriter, item, i, this.itemType, features);
                }
            }
            return;
        }
        final JSONWriter.Context context = jsonWriter.context;
        final ObjectWriterProvider provider = context.provider;
        jsonWriter.startArray();
        for (int j = 0; j < list.size(); ++j) {
            if (j != 0) {
                jsonWriter.writeComma();
            }
            final Object item2 = list.get(j);
            if (item2 == null) {
                jsonWriter.writeNull();
            }
            else {
                final Class<?> itemClass2 = item2.getClass();
                if (itemClass2 == String.class) {
                    jsonWriter.writeString((String)item2);
                }
                else if (itemClass2 == Integer.class) {
                    if ((provider.userDefineMask & 0x2L) == 0x0L) {
                        jsonWriter.writeInt32((Integer)item2);
                    }
                    else {
                        final ObjectWriter valueWriter = provider.getObjectWriter(itemClass2, itemClass2, false);
                        valueWriter.write(jsonWriter, item2, j, Integer.class, features);
                    }
                }
                else if (itemClass2 == Long.class) {
                    if ((provider.userDefineMask & 0x4L) == 0x0L) {
                        jsonWriter.writeInt64((Long)item2);
                    }
                    else {
                        final ObjectWriter valueWriter = provider.getObjectWriter(itemClass2, itemClass2, false);
                        valueWriter.write(jsonWriter, item2, j, Long.class, features);
                    }
                }
                else if (itemClass2 == Boolean.class) {
                    if ((provider.userDefineMask & 0x2L) == 0x0L) {
                        jsonWriter.writeBool((boolean)item2);
                    }
                    else {
                        final ObjectWriter valueWriter = provider.getObjectWriter(itemClass2, itemClass2, false);
                        valueWriter.write(jsonWriter, item2, j, Boolean.class, features);
                    }
                }
                else if (itemClass2 == BigDecimal.class) {
                    if ((provider.userDefineMask & 0x8L) == 0x0L) {
                        jsonWriter.writeDecimal((BigDecimal)item2, features, null);
                    }
                    else {
                        final ObjectWriter valueWriter = provider.getObjectWriter(itemClass2, itemClass2, false);
                        valueWriter.write(jsonWriter, item2, j, BigDecimal.class, features);
                    }
                }
                else {
                    ObjectWriter itemObjectWriter2;
                    boolean refDetect;
                    if (itemClass2 == this.itemClass && this.itemClassWriter != null) {
                        itemObjectWriter2 = this.itemClassWriter;
                        refDetect = (this.itemClassRefDetect && jsonWriter.isRefDetect());
                    }
                    else if (itemClass2 == previousClass) {
                        itemObjectWriter2 = previousObjectWriter;
                        refDetect = previousRefDetect;
                    }
                    else {
                        if (itemClass2 == JSONObject.class) {
                            itemObjectWriter2 = ObjectWriterImplMap.INSTANCE;
                            refDetect = jsonWriter.isRefDetect();
                        }
                        else if (itemClass2 == TypeUtils.CLASS_JSON_OBJECT_1x) {
                            itemObjectWriter2 = ObjectWriterImplMap.INSTANCE_1x;
                            refDetect = jsonWriter.isRefDetect();
                        }
                        else if (itemClass2 == JSONArray.class) {
                            itemObjectWriter2 = ObjectWriterImplList.INSTANCE_JSON_ARRAY;
                            refDetect = jsonWriter.isRefDetect();
                        }
                        else if (itemClass2 == TypeUtils.CLASS_JSON_ARRAY_1x) {
                            itemObjectWriter2 = ObjectWriterImplList.INSTANCE_JSON_ARRAY_1x;
                            refDetect = jsonWriter.isRefDetect();
                        }
                        else {
                            itemObjectWriter2 = context.getObjectWriter(itemClass2);
                            refDetect = jsonWriter.isRefDetect(item2);
                        }
                        previousClass = itemClass2;
                        previousObjectWriter = itemObjectWriter2;
                        previousRefDetect = refDetect;
                        if (itemClass2 == this.itemClass) {
                            this.itemClassWriter = itemObjectWriter2;
                        }
                    }
                    if (refDetect) {
                        final String refPath = jsonWriter.setPath(j, item2);
                        if (refPath != null) {
                            jsonWriter.writeReference(refPath);
                            jsonWriter.popPath(item2);
                            continue;
                        }
                    }
                    itemObjectWriter2.write(jsonWriter, item2, j, this.itemType, this.features);
                    if (refDetect) {
                        jsonWriter.popPath(item2);
                    }
                }
            }
        }
        jsonWriter.endArray();
    }
    
    private List getList(final Object object) {
        List list;
        if (object instanceof Iterable) {
            list = new ArrayList();
            final Iterator iterator = ((Iterable)object).iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        else {
            list = (List)object;
        }
        return list;
    }
    
    static {
        INSTANCE = new ObjectWriterImplList(null, null, null, null, 0L);
        INSTANCE_JSON_ARRAY = new ObjectWriterImplList(JSONArray.class, null, null, null, 0L);
        if (TypeUtils.CLASS_JSON_ARRAY_1x == null) {
            INSTANCE_JSON_ARRAY_1x = null;
        }
        else {
            INSTANCE_JSON_ARRAY_1x = new ObjectWriterImplList(TypeUtils.CLASS_JSON_ARRAY_1x, null, null, null, 0L);
        }
        CLASS_SUBLIST = new ArrayList().subList(0, 0).getClass();
        TYPE_NAME_ARRAY_LIST = TypeUtils.getTypeName(ArrayList.class);
        TYPE_NAME_JSONB_ARRAY_LIST = JSONB.toBytes(ObjectWriterImplList.TYPE_NAME_ARRAY_LIST);
        TYPE_NAME_HASH_ARRAY_LIST = Fnv.hashCode64(ObjectWriterImplList.TYPE_NAME_ARRAY_LIST);
    }
}
