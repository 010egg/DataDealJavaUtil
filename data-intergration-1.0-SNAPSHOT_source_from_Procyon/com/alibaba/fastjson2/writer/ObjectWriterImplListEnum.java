// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.List;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplListEnum extends ObjectWriterPrimitiveImpl
{
    final Class defineClass;
    final Class enumType;
    final long features;
    byte[] typeNameJSONB;
    
    public ObjectWriterImplListEnum(final Class defineClass, final Class enumType, final long features) {
        this.defineClass = defineClass;
        this.enumType = enumType;
        this.features = features;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final Class<?> objectClass = object.getClass();
        if (jsonWriter.isWriteTypeInfo(object) && this.defineClass != objectClass) {
            jsonWriter.writeTypeName(TypeUtils.getTypeName(objectClass));
        }
        final List list = (List)object;
        final int size = list.size();
        jsonWriter.startArray(size);
        final boolean writeEnumUsingToString = jsonWriter.isEnabled(JSONWriter.Feature.WriteEnumUsingToString);
        for (int i = 0; i < size; ++i) {
            final Enum e = list.get(i);
            final Class enumClass = e.getClass();
            if (enumClass != this.enumType) {
                final ObjectWriter enumWriter = jsonWriter.getObjectWriter(enumClass);
                enumWriter.writeJSONB(jsonWriter, e, null, this.enumType, this.features | features);
            }
            else {
                String str;
                if (writeEnumUsingToString) {
                    str = e.toString();
                }
                else {
                    str = e.name();
                }
                jsonWriter.writeString(str);
            }
        }
        jsonWriter.endArray();
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final List list = (List)object;
        jsonWriter.startArray();
        for (int i = 0; i < list.size(); ++i) {
            if (i != 0) {
                jsonWriter.writeComma();
            }
            final String item = list.get(i);
            if (item == null) {
                jsonWriter.writeNull();
            }
            else {
                jsonWriter.writeString(item);
            }
        }
        jsonWriter.endArray();
    }
}
