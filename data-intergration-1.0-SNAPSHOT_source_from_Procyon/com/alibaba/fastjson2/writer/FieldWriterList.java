// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.List;
import java.util.function.Function;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Locale;
import java.util.Date;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

abstract class FieldWriterList<T> extends FieldWriter<T>
{
    final Type itemType;
    final Class itemClass;
    final boolean itemClassNotReferenceDetect;
    ObjectWriter listWriter;
    ObjectWriter itemObjectWriter;
    
    FieldWriterList(final String name, final Type itemType, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method) {
        super(name, ordinal, features, format, label, fieldType, fieldClass, field, method);
        this.itemType = ((itemType == null) ? Object.class : itemType);
        if (this.itemType instanceof Class) {
            this.itemClass = (Class)itemType;
            if (this.itemClass != null) {
                if (Enum.class.isAssignableFrom(this.itemClass)) {
                    this.listWriter = new ObjectWriterImplListEnum(fieldClass, this.itemClass, features);
                }
                else if (this.itemClass == String.class) {
                    this.listWriter = ObjectWriterImplListStr.INSTANCE;
                }
                else {
                    this.listWriter = new ObjectWriterImplList(fieldClass, fieldType, this.itemClass, itemType, features);
                }
            }
        }
        else {
            this.itemClass = TypeUtils.getMapping(itemType);
        }
        this.itemClassNotReferenceDetect = (this.itemClass != null && ObjectWriterProvider.isNotReferenceDetect(this.itemClass));
        if (format != null && this.itemClass == Date.class) {
            this.itemObjectWriter = new ObjectWriterImplDate(format, null);
        }
    }
    
    @Override
    public Type getItemType() {
        return this.itemType;
    }
    
    @Override
    public Class getItemClass() {
        return this.itemClass;
    }
    
    @Override
    public ObjectWriter getItemWriter(final JSONWriter jsonWriter, final Type itemType) {
        if (itemType != null && itemType != this.itemType) {
            return jsonWriter.getObjectWriter(itemType, TypeUtils.getClass(itemType));
        }
        if (this.itemObjectWriter != null) {
            return this.itemObjectWriter;
        }
        if (this.format != null) {
            if (itemType == Double.class) {
                return this.itemObjectWriter = new ObjectWriterImplDouble(new DecimalFormat(this.format));
            }
            if (itemType == Float.class) {
                return this.itemObjectWriter = new ObjectWriterImplFloat(new DecimalFormat(this.format));
            }
            if (itemType == BigDecimal.class) {
                return this.itemObjectWriter = new ObjectWriterImplBigDecimal(new DecimalFormat(this.format), null);
            }
        }
        return this.itemObjectWriter = jsonWriter.getObjectWriter(this.itemType, this.itemClass);
    }
    
    @Override
    public ObjectWriter getObjectWriter(final JSONWriter jsonWriter, final Class valueClass) {
        final ObjectWriter listWriter = this.listWriter;
        if (listWriter != null && this.fieldClass.isAssignableFrom(valueClass)) {
            return listWriter;
        }
        if (listWriter == null && valueClass == this.fieldClass) {
            return this.listWriter = jsonWriter.getObjectWriter(valueClass);
        }
        return jsonWriter.getObjectWriter(valueClass);
    }
    
    @Override
    public final void writeListValueJSONB(final JSONWriter jsonWriter, final List list) {
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        final long features = jsonWriter.getFeatures(this.features);
        final boolean beanToArray = (features & JSONWriter.Feature.BeanToArray.mask) != 0x0L;
        final int size = list.size();
        boolean refDetect = (features & JSONWriter.Feature.ReferenceDetection.mask) != 0x0L;
        if (jsonWriter.isWriteTypeInfo(list, this.fieldClass)) {
            jsonWriter.writeTypeName(TypeUtils.getTypeName(list.getClass()));
        }
        jsonWriter.startArray(size);
        for (int i = 0; i < size; ++i) {
            final Object item = list.get(i);
            if (item == null) {
                jsonWriter.writeNull();
            }
            else {
                final Class<?> itemClass = item.getClass();
                if (itemClass != previousClass) {
                    refDetect = jsonWriter.isRefDetect();
                    if (itemClass == this.itemType && this.itemObjectWriter != null) {
                        previousObjectWriter = this.itemObjectWriter;
                    }
                    else {
                        previousObjectWriter = this.getItemWriter(jsonWriter, itemClass);
                    }
                    previousClass = itemClass;
                    if (refDetect) {
                        if (itemClass == this.itemClass) {
                            refDetect = !this.itemClassNotReferenceDetect;
                        }
                        else {
                            refDetect = !ObjectWriterProvider.isNotReferenceDetect(itemClass);
                        }
                    }
                }
                final ObjectWriter itemObjectWriter = previousObjectWriter;
                if (!refDetect || !jsonWriter.writeReference(i, item)) {
                    if (beanToArray) {
                        itemObjectWriter.writeArrayMappingJSONB(jsonWriter, item, i, this.itemType, features);
                    }
                    else {
                        itemObjectWriter.writeJSONB(jsonWriter, item, i, this.itemType, features);
                    }
                    if (refDetect) {
                        jsonWriter.popPath(item);
                    }
                }
            }
        }
    }
    
    @Override
    public void writeListValue(final JSONWriter jsonWriter, final List list) {
        if (jsonWriter.jsonb) {
            this.writeListJSONB(jsonWriter, list);
            return;
        }
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        final long features = jsonWriter.getFeatures(this.features);
        boolean previousItemRefDetect = (features & JSONWriter.Feature.ReferenceDetection.mask) != 0x0L;
        jsonWriter.startArray();
        for (int i = 0; i < list.size(); ++i) {
            if (i != 0) {
                jsonWriter.writeComma();
            }
            final Object item = list.get(i);
            if (item == null) {
                jsonWriter.writeNull();
            }
            else {
                final Class<?> itemClass = item.getClass();
                if (itemClass == String.class) {
                    jsonWriter.writeString((String)item);
                }
                else {
                    ObjectWriter itemObjectWriter;
                    boolean itemRefDetect;
                    if (itemClass == previousClass) {
                        itemObjectWriter = previousObjectWriter;
                        itemRefDetect = previousItemRefDetect;
                    }
                    else {
                        itemRefDetect = ((features & JSONWriter.Feature.ReferenceDetection.mask) != 0x0L);
                        itemObjectWriter = this.getItemWriter(jsonWriter, itemClass);
                        previousClass = itemClass;
                        previousObjectWriter = itemObjectWriter;
                        if (itemRefDetect) {
                            itemRefDetect = !ObjectWriterProvider.isNotReferenceDetect(itemClass);
                        }
                        previousItemRefDetect = itemRefDetect;
                    }
                    if (!itemRefDetect || !jsonWriter.writeReference(i, item)) {
                        itemObjectWriter.write(jsonWriter, item, null, this.itemType, features);
                        if (itemRefDetect) {
                            jsonWriter.popPath(item);
                        }
                    }
                }
            }
        }
        jsonWriter.endArray();
    }
    
    @Override
    public final void writeListJSONB(final JSONWriter jsonWriter, final List list) {
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        final long features = jsonWriter.getFeatures(this.features);
        final boolean beanToArray = (features & JSONWriter.Feature.BeanToArray.mask) != 0x0L;
        final int size = list.size();
        if ((features & JSONWriter.Feature.NotWriteEmptyArray.mask) != 0x0L && size == 0) {
            return;
        }
        this.writeFieldName(jsonWriter);
        boolean refDetect = (features & JSONWriter.Feature.ReferenceDetection.mask) != 0x0L;
        if (jsonWriter.isWriteTypeInfo(list, this.fieldClass)) {
            jsonWriter.writeTypeName(TypeUtils.getTypeName(list.getClass()));
        }
        jsonWriter.startArray(size);
        for (int i = 0; i < size; ++i) {
            final Object item = list.get(i);
            if (item == null) {
                jsonWriter.writeNull();
            }
            else {
                final Class<?> itemClass = item.getClass();
                if (itemClass != previousClass) {
                    refDetect = jsonWriter.isRefDetect();
                    if (itemClass == this.itemType && this.itemObjectWriter != null) {
                        previousObjectWriter = this.itemObjectWriter;
                    }
                    else {
                        previousObjectWriter = this.getItemWriter(jsonWriter, itemClass);
                    }
                    previousClass = itemClass;
                    if (refDetect) {
                        if (itemClass == this.itemClass) {
                            refDetect = !this.itemClassNotReferenceDetect;
                        }
                        else {
                            refDetect = !ObjectWriterProvider.isNotReferenceDetect(itemClass);
                        }
                    }
                }
                final ObjectWriter itemObjectWriter = previousObjectWriter;
                if (!refDetect || !jsonWriter.writeReference(i, item)) {
                    if (beanToArray) {
                        itemObjectWriter.writeArrayMappingJSONB(jsonWriter, item, i, this.itemType, features);
                    }
                    else {
                        itemObjectWriter.writeJSONB(jsonWriter, item, i, this.itemType, features);
                    }
                    if (refDetect) {
                        jsonWriter.popPath(item);
                    }
                }
            }
        }
    }
    
    @Override
    public void writeList(final JSONWriter jsonWriter, final List list) {
        if (jsonWriter.jsonb) {
            this.writeListJSONB(jsonWriter, list);
            return;
        }
        Class previousClass = null;
        ObjectWriter previousObjectWriter = null;
        final long features = jsonWriter.getFeatures(this.features);
        if ((features & JSONWriter.Feature.NotWriteEmptyArray.mask) != 0x0L && list.isEmpty()) {
            return;
        }
        this.writeFieldName(jsonWriter);
        boolean previousItemRefDetect = (features & JSONWriter.Feature.ReferenceDetection.mask) != 0x0L;
        jsonWriter.startArray();
        for (int i = 0; i < list.size(); ++i) {
            if (i != 0) {
                jsonWriter.writeComma();
            }
            final Object item = list.get(i);
            if (item == null) {
                jsonWriter.writeNull();
            }
            else {
                final Class<?> itemClass = item.getClass();
                if (itemClass == String.class) {
                    jsonWriter.writeString((String)item);
                }
                else {
                    ObjectWriter itemObjectWriter;
                    boolean itemRefDetect;
                    if (itemClass == previousClass) {
                        itemObjectWriter = previousObjectWriter;
                        itemRefDetect = previousItemRefDetect;
                    }
                    else {
                        itemRefDetect = jsonWriter.isRefDetect();
                        itemObjectWriter = this.getItemWriter(jsonWriter, itemClass);
                        previousClass = itemClass;
                        previousObjectWriter = itemObjectWriter;
                        if (itemRefDetect) {
                            itemRefDetect = !ObjectWriterProvider.isNotReferenceDetect(itemClass);
                        }
                        previousItemRefDetect = itemRefDetect;
                    }
                    if (!itemRefDetect || !jsonWriter.writeReference(i, item)) {
                        itemObjectWriter.write(jsonWriter, item, null, this.itemType, features);
                        if (itemRefDetect) {
                            jsonWriter.popPath(item);
                        }
                    }
                }
            }
        }
        jsonWriter.endArray();
    }
    
    @Override
    public void writeListStr(final JSONWriter jsonWriter, final boolean writeFieldName, final List<String> list) {
        if (writeFieldName) {
            this.writeFieldName(jsonWriter);
        }
        if (jsonWriter.jsonb && jsonWriter.isWriteTypeInfo(list, this.fieldClass)) {
            jsonWriter.writeTypeName(TypeUtils.getTypeName(list.getClass()));
        }
        jsonWriter.writeString(list);
    }
}
