// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.ArrayList;
import java.lang.reflect.ParameterizedType;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.List;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplListStr extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplListStr INSTANCE;
    
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
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        Class fieldClass = null;
        if (fieldType == TypeUtils.PARAM_TYPE_LIST_STR) {
            fieldClass = List.class;
        }
        else if (fieldType instanceof Class) {
            fieldClass = (Class)fieldType;
        }
        else if (fieldType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)fieldType;
            final Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                fieldClass = (Class)rawType;
            }
        }
        final Class<?> objectClass = object.getClass();
        if (objectClass != ArrayList.class && jsonWriter.isWriteTypeInfo(object, fieldClass, features)) {
            jsonWriter.writeTypeName(TypeUtils.getTypeName((objectClass == ObjectWriterImplList.CLASS_SUBLIST) ? ArrayList.class : objectClass));
        }
        final List<String> list = (List<String>)object;
        jsonWriter.writeString(list);
    }
    
    static {
        INSTANCE = new ObjectWriterImplListStr();
    }
}
