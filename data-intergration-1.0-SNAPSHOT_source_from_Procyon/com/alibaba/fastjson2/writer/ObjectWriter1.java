// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.List;

public class ObjectWriter1<T> extends ObjectWriterAdapter<T>
{
    public final FieldWriter fieldWriter0;
    
    public ObjectWriter1(final Class<T> objectClass, final String typeKey, final String typeName, final long features, final List<FieldWriter> fieldWriters) {
        super(objectClass, typeKey, typeName, features, fieldWriters);
        this.fieldWriter0 = fieldWriters.get(0);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        final long featuresAll = features | this.features | jsonWriter.getFeatures();
        if (jsonWriter.jsonb) {
            if ((featuresAll & JSONWriter.Feature.BeanToArray.mask) != 0x0L) {
                this.writeArrayMappingJSONB(jsonWriter, object, fieldName, fieldType, features);
                return;
            }
            this.writeJSONB(jsonWriter, object, fieldName, fieldType, features);
        }
        else {
            if ((featuresAll & JSONWriter.Feature.BeanToArray.mask) != 0x0L) {
                this.writeArrayMapping(jsonWriter, object, fieldName, fieldType, features);
                return;
            }
            if (!this.serializable) {
                if ((featuresAll & JSONWriter.Feature.ErrorOnNoneSerializable.mask) != 0x0L) {
                    this.errorOnNoneSerializable();
                    return;
                }
                if ((featuresAll & JSONWriter.Feature.IgnoreNoneSerializable.mask) != 0x0L) {
                    jsonWriter.writeNull();
                    return;
                }
            }
            if (this.hasFilter(jsonWriter)) {
                this.writeWithFilter(jsonWriter, object, fieldName, fieldType, 0L);
                return;
            }
            jsonWriter.startObject();
            if (((features | this.features) & JSONWriter.Feature.WriteClassName.mask) != 0x0L || jsonWriter.isWriteTypeInfo(object, features)) {
                this.writeTypeInfo(jsonWriter);
            }
            this.fieldWriter0.write(jsonWriter, object);
            jsonWriter.endObject();
        }
    }
    
    @Override
    public final FieldWriter getFieldWriter(final long hashCode) {
        if (hashCode == this.fieldWriter0.hashCode) {
            return this.fieldWriter0;
        }
        return null;
    }
}
