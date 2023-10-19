// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import java.util.function.Function;
import java.math.BigDecimal;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.util.TypeUtils;
import java.text.DecimalFormat;

final class ObjectWriterArrayFinal extends ObjectWriterPrimitiveImpl
{
    public static final ObjectWriterArrayFinal FLOAT_ARRAY;
    public static final ObjectWriterArrayFinal DOUBLE_ARRAY;
    public static final ObjectWriterArrayFinal DECIMAL_ARRAY;
    final byte[] typeNameBytes;
    final long typeNameHash;
    final Class itemClass;
    volatile ObjectWriter itemObjectWriter;
    public final DecimalFormat format;
    public final boolean refDetect;
    
    public ObjectWriterArrayFinal(final Class itemClass, final DecimalFormat format) {
        this.itemClass = itemClass;
        this.format = format;
        final String typeName = '[' + TypeUtils.getTypeName(itemClass);
        this.typeNameBytes = JSONB.toBytes(typeName);
        this.typeNameHash = Fnv.hashCode64(typeName);
        this.refDetect = !ObjectWriterProvider.isNotReferenceDetect(itemClass);
    }
    
    public ObjectWriter getItemObjectWriter(final JSONWriter jsonWriter) {
        ObjectWriter itemObjectWriter = this.itemObjectWriter;
        if (itemObjectWriter == null) {
            if (this.itemClass == Float.class) {
                if (this.format != null) {
                    itemObjectWriter = new ObjectWriterImplFloat(this.format);
                }
                else {
                    itemObjectWriter = ObjectWriterImplFloat.INSTANCE;
                }
            }
            else if (this.itemClass == Double.class) {
                if (this.format != null) {
                    itemObjectWriter = new ObjectWriterImplDouble(this.format);
                }
                else {
                    itemObjectWriter = ObjectWriterImplDouble.INSTANCE;
                }
            }
            else if (this.itemClass == BigDecimal.class) {
                if (this.format != null) {
                    itemObjectWriter = new ObjectWriterImplBigDecimal(this.format, null);
                }
                else {
                    itemObjectWriter = ObjectWriterImplBigDecimal.INSTANCE;
                }
            }
            else {
                itemObjectWriter = jsonWriter.getObjectWriter(this.itemClass);
            }
            this.itemObjectWriter = itemObjectWriter;
        }
        return itemObjectWriter;
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
        if (refDetect) {
            refDetect = this.refDetect;
        }
        final Object[] list = (Object[])object;
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
                final ObjectWriter itemObjectWriter = this.getItemObjectWriter(jsonWriter);
                if (refDetect) {
                    final String refPath = jsonWriter.setPath(i, item);
                    if (refPath != null) {
                        jsonWriter.writeReference(refPath);
                        jsonWriter.popPath(item);
                        continue;
                    }
                }
                itemObjectWriter.write(jsonWriter, item, i, this.itemClass, features);
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
        if (refDetect) {
            refDetect = this.refDetect;
        }
        final Object[] list = (Object[])object;
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
                final ObjectWriter itemObjectWriter = this.getItemObjectWriter(jsonWriter);
                if (refDetect) {
                    final String refPath = jsonWriter.setPath(i, item);
                    if (refPath != null) {
                        jsonWriter.writeReference(refPath);
                        jsonWriter.popPath(item);
                        continue;
                    }
                }
                itemObjectWriter.writeJSONB(jsonWriter, item, i, this.itemClass, 0L);
                if (refDetect) {
                    jsonWriter.popPath(item);
                }
            }
        }
    }
    
    static {
        FLOAT_ARRAY = new ObjectWriterArrayFinal(Float.class, null);
        DOUBLE_ARRAY = new ObjectWriterArrayFinal(Double.class, null);
        DECIMAL_ARRAY = new ObjectWriterArrayFinal(BigDecimal.class, null);
    }
}
