// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Type;

final class ObjectWriterArray extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterArray INSTANCE;
    final byte[] typeNameBytes;
    final long typeNameHash;
    final Type itemType;
    volatile ObjectWriter itemObjectWriter;
    
    public ObjectWriterArray(final Type itemType) {
        this.itemType = itemType;
        if (itemType == Object.class) {
            this.typeNameBytes = JSONB.toBytes("[O");
            this.typeNameHash = Fnv.hashCode64("[0");
        }
        else {
            final String typeName = '[' + TypeUtils.getTypeName((Class)itemType);
            this.typeNameBytes = JSONB.toBytes(typeName);
            this.typeNameHash = Fnv.hashCode64(typeName);
        }
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (jsonWriter.jsonb) {
            this.writeJSONB(jsonWriter, object, fieldName, fieldType, features);
            return;
        }
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        boolean refDetect = jsonWriter.isRefDetect();
        final Object[] list = (Object[])object;
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        jsonWriter.startArray();
        for (int i = 0; i < list.length; ++i) {
            if (i != 0) {
                jsonWriter.writeComma();
            }
            final Object item = list[i];
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
                    refDetect = jsonWriter.isRefDetect();
                    itemObjectWriter = jsonWriter.getObjectWriter(itemClass);
                    previousClass = itemClass;
                    previousObjectWriter = itemObjectWriter;
                    if (refDetect) {
                        refDetect = !ObjectWriterProvider.isNotReferenceDetect(itemClass);
                    }
                }
                if (refDetect) {
                    final String refPath = jsonWriter.setPath(i, item);
                    if (refPath != null) {
                        jsonWriter.writeReference(refPath);
                        jsonWriter.popPath(item);
                        continue;
                    }
                }
                itemObjectWriter.write(jsonWriter, item, i, this.itemType, features);
                if (refDetect) {
                    jsonWriter.popPath(item);
                }
            }
        }
        jsonWriter.endArray();
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        boolean refDetect = jsonWriter.isRefDetect();
        final Object[] list = (Object[])object;
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        if (jsonWriter.isWriteTypeInfo(object, fieldType)) {
            jsonWriter.writeTypeName(this.typeNameBytes, this.typeNameHash);
        }
        jsonWriter.startArray(list.length);
        for (int i = 0; i < list.length; ++i) {
            final Object item = list[i];
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
                    refDetect = jsonWriter.isRefDetect();
                    itemObjectWriter = jsonWriter.getObjectWriter(itemClass);
                    previousClass = itemClass;
                    previousObjectWriter = itemObjectWriter;
                    if (refDetect) {
                        refDetect = !ObjectWriterProvider.isNotReferenceDetect(itemClass);
                    }
                }
                if (refDetect) {
                    final String refPath = jsonWriter.setPath(i, item);
                    if (refPath != null) {
                        jsonWriter.writeReference(refPath);
                        jsonWriter.popPath(item);
                        continue;
                    }
                }
                itemObjectWriter.writeJSONB(jsonWriter, item, i, this.itemType, 0L);
                if (refDetect) {
                    jsonWriter.popPath(item);
                }
            }
        }
    }
    
    static {
        INSTANCE = new ObjectWriterArray(Object.class);
    }
}
