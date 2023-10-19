// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.Iterator;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.List;

public class ObjectWriterException extends ObjectWriterAdapter<Exception>
{
    public ObjectWriterException(final Class objectType, final long features, final List<FieldWriter> fieldWriters) {
        super(objectType, null, null, features, fieldWriters);
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        this.writeClassInfo(jsonWriter);
        final int size = this.fieldWriters.size();
        jsonWriter.startObject();
        for (int i = 0; i < size; ++i) {
            final FieldWriter fw = this.fieldWriters.get(i);
            fw.write(jsonWriter, object);
        }
        jsonWriter.endObject();
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (jsonWriter.jsonb) {
            this.writeJSONB(jsonWriter, object, fieldName, fieldType, features);
            return;
        }
        if (this.hasFilter(jsonWriter)) {
            this.writeWithFilter(jsonWriter, object);
            return;
        }
        jsonWriter.startObject();
        if ((jsonWriter.getFeatures(features) & (JSONWriter.Feature.WriteClassName.mask | JSONWriter.Feature.WriteThrowableClassName.mask)) != 0x0L) {
            this.writeTypeInfo(jsonWriter);
        }
        for (final FieldWriter fieldWriter : this.fieldWriters) {
            fieldWriter.write(jsonWriter, object);
        }
        jsonWriter.endObject();
    }
}
