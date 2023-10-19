// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.PropertyFilter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.Fnv;
import java.util.Collections;
import java.util.List;

public interface ObjectWriter<T>
{
    default long getFeatures() {
        return 0L;
    }
    
    default List<FieldWriter> getFieldWriters() {
        return (List<FieldWriter>)Collections.emptyList();
    }
    
    default FieldWriter getFieldWriter(final long hashCode) {
        return null;
    }
    
    default FieldWriter getFieldWriter(final String name) {
        final long nameHash = Fnv.hashCode64(name);
        FieldWriter fieldWriter = this.getFieldWriter(nameHash);
        if (fieldWriter == null) {
            final long nameHashLCase = Fnv.hashCode64LCase(name);
            if (nameHashLCase != nameHash) {
                fieldWriter = this.getFieldWriter(nameHashLCase);
            }
        }
        return fieldWriter;
    }
    
    default boolean writeTypeInfo(final JSONWriter jsonWriter) {
        return false;
    }
    
    default void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        this.write(jsonWriter, object, fieldName, fieldType, features);
    }
    
    default void writeArrayMappingJSONB(final JSONWriter jsonWriter, final Object object) {
        this.writeArrayMappingJSONB(jsonWriter, object, null, null, 0L);
    }
    
    default void writeArrayMappingJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        final List<FieldWriter> fieldWriters = this.getFieldWriters();
        final int size = fieldWriters.size();
        jsonWriter.startArray(size);
        for (int i = 0; i < size; ++i) {
            final FieldWriter fieldWriter = fieldWriters.get(i);
            fieldWriter.writeValue(jsonWriter, object);
        }
    }
    
    default void writeArrayMapping(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (jsonWriter.jsonb) {
            this.writeArrayMappingJSONB(jsonWriter, object, fieldName, fieldType, features);
            return;
        }
        final List<FieldWriter> fieldWriters = this.getFieldWriters();
        jsonWriter.startArray();
        final boolean hasFilter = this.hasFilter(jsonWriter);
        if (!hasFilter) {
            for (int i = 0, size = fieldWriters.size(); i < size; ++i) {
                if (i != 0) {
                    jsonWriter.writeComma();
                }
                final FieldWriter fieldWriter = fieldWriters.get(i);
                fieldWriter.writeValue(jsonWriter, object);
            }
        }
        else {
            final JSONWriter.Context ctx = jsonWriter.context;
            final PropertyPreFilter propertyPreFilter = ctx.getPropertyPreFilter();
            final ValueFilter valueFilter = ctx.getValueFilter();
            final PropertyFilter propertyFilter = ctx.getPropertyFilter();
            for (int j = 0, size2 = fieldWriters.size(); j < size2; ++j) {
                if (j != 0) {
                    jsonWriter.writeComma();
                }
                final FieldWriter fieldWriter2 = fieldWriters.get(j);
                if (propertyPreFilter != null && !propertyPreFilter.process(jsonWriter, object, fieldWriter2.fieldName)) {
                    jsonWriter.writeNull();
                }
                else {
                    final Object fieldValue = fieldWriter2.getFieldValue(object);
                    if (propertyFilter != null && !propertyFilter.apply(object, fieldWriter2.fieldName, fieldValue)) {
                        jsonWriter.writeNull();
                    }
                    else if (valueFilter != null) {
                        final Object processValue = valueFilter.apply(object, fieldWriter2.fieldName, fieldValue);
                        if (processValue == null) {
                            jsonWriter.writeNull();
                        }
                        else {
                            final ObjectWriter processValueWriter = fieldWriter2.getObjectWriter(jsonWriter, processValue.getClass());
                            processValueWriter.write(jsonWriter, fieldValue);
                        }
                    }
                    else if (fieldValue == null) {
                        jsonWriter.writeNull();
                    }
                    else {
                        final ObjectWriter fieldValueWriter = fieldWriter2.getObjectWriter(jsonWriter, fieldValue.getClass());
                        fieldValueWriter.write(jsonWriter, fieldValue);
                    }
                }
            }
        }
        jsonWriter.endArray();
    }
    
    default boolean hasFilter(final JSONWriter jsonWriter) {
        return jsonWriter.hasFilter(JSONWriter.Feature.IgnoreNonFieldGetter.mask);
    }
    
    default void write(final JSONWriter jsonWriter, final Object object) {
        this.write(jsonWriter, object, null, null, 0L);
    }
    
    default String toJSONString(final T object, final JSONWriter.Feature... features) {
        final JSONWriter jsonWriter = JSONWriter.of(features);
        this.write(jsonWriter, object, null, null, 0L);
        return jsonWriter.toString();
    }
    
    void write(final JSONWriter p0, final Object p1, final Object p2, final Type p3, final long p4);
    
    default void writeWithFilter(final JSONWriter jsonWriter, final Object object) {
        this.writeWithFilter(jsonWriter, object, null, null, 0L);
    }
    
    default void writeWithFilter(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        throw new UnsupportedOperationException();
    }
    
    default void setPropertyFilter(final PropertyFilter propertyFilter) {
    }
    
    default void setValueFilter(final ValueFilter valueFilter) {
    }
    
    default void setNameFilter(final NameFilter nameFilter) {
    }
    
    default void setPropertyPreFilter(final PropertyPreFilter propertyPreFilter) {
    }
    
    default void setFilter(final Filter filter) {
        if (filter instanceof PropertyFilter) {
            this.setPropertyFilter((PropertyFilter)filter);
        }
        if (filter instanceof ValueFilter) {
            this.setValueFilter((ValueFilter)filter);
        }
        if (filter instanceof NameFilter) {
            this.setNameFilter((NameFilter)filter);
        }
        if (filter instanceof PropertyPreFilter) {
            this.setPropertyPreFilter((PropertyPreFilter)filter);
        }
    }
}
