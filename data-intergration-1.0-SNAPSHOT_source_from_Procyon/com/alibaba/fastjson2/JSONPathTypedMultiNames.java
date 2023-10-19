// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.math.BigDecimal;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.Map;
import java.time.ZoneId;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.reader.ObjectReaderAdapter;
import com.alibaba.fastjson2.reader.FieldReader;

class JSONPathTypedMultiNames extends JSONPathTypedMulti
{
    final JSONPath prefix;
    final JSONPath[] namePaths;
    final String[] names;
    final FieldReader[] fieldReaders;
    final ObjectReaderAdapter<Object[]> objectReader;
    
    JSONPathTypedMultiNames(final JSONPath[] paths, final JSONPath prefix, final JSONPath[] namePaths, final Type[] types, final String[] formats, final long[] pathFeatures, final ZoneId zoneId, final long features) {
        super(paths, types, formats, pathFeatures, zoneId, features);
        this.prefix = prefix;
        this.namePaths = namePaths;
        this.names = new String[paths.length];
        for (int j = 0; j < paths.length; ++j) {
            final JSONPathSingleName jsonPathSingleName = (JSONPathSingleName)namePaths[j];
            final String fieldName = jsonPathSingleName.name;
            this.names[j] = fieldName;
        }
        final long[] fieldReaderFeatures = new long[this.names.length];
        if (pathFeatures != null) {
            for (int k = 0; k < pathFeatures.length; ++k) {
                if ((pathFeatures[k] & Feature.NullOnError.mask) != 0x0L) {
                    final long[] array = fieldReaderFeatures;
                    final int n = k;
                    array[n] |= JSONReader.Feature.NullOnError.mask;
                }
            }
        }
        final Type[] fieldTypes = types.clone();
        for (int l = 0; l < fieldTypes.length; ++l) {
            final Type fieldType = fieldTypes[l];
            if (fieldType == Boolean.TYPE) {
                fieldTypes[l] = Boolean.class;
            }
            else if (fieldType == Character.TYPE) {
                fieldTypes[l] = Character.class;
            }
            else if (fieldType == Byte.TYPE) {
                fieldTypes[l] = Byte.class;
            }
            else if (fieldType == Short.TYPE) {
                fieldTypes[l] = Short.class;
            }
            else if (fieldType == Integer.TYPE) {
                fieldTypes[l] = Integer.class;
            }
            else if (fieldType == Long.TYPE) {
                fieldTypes[l] = Long.class;
            }
            else if (fieldType == Float.TYPE) {
                fieldTypes[l] = Float.class;
            }
            else if (fieldType == Double.TYPE) {
                fieldTypes[l] = Double.class;
            }
        }
        final int length = this.names.length;
        this.objectReader = (ObjectReaderAdapter<Object[]>)(ObjectReaderAdapter)JSONFactory.getDefaultObjectReaderProvider().createObjectReader(this.names, fieldTypes, fieldReaderFeatures, () -> new Object[length], (o, i, v) -> o[i] = v);
        this.fieldReaders = this.objectReader.getFieldReaders();
    }
    
    @Override
    public boolean isRef() {
        return true;
    }
    
    @Override
    public Object eval(final Object root) {
        final Object[] array = new Object[this.paths.length];
        Object object = root;
        if (this.prefix != null) {
            object = this.prefix.eval(root);
        }
        if (object == null) {
            return new Object[this.paths.length];
        }
        if (object instanceof Map) {
            return this.objectReader.createInstance((Map)object, 0L);
        }
        final ObjectWriter objectReader = JSONFactory.defaultObjectWriterProvider.getObjectWriter(object.getClass());
        for (int i = 0; i < this.names.length; ++i) {
            final FieldWriter fieldWriter = objectReader.getFieldWriter(this.names[i]);
            if (fieldWriter != null) {
                Object result = fieldWriter.getFieldValue(object);
                final Type type = this.types[i];
                if (result != null && result.getClass() != type) {
                    if (type == Long.class) {
                        result = TypeUtils.toLong(result);
                    }
                    else if (type == BigDecimal.class) {
                        result = TypeUtils.toBigDecimal(result);
                    }
                    else if (type == String[].class) {
                        result = TypeUtils.toStringArray(result);
                    }
                    else {
                        result = TypeUtils.cast(result, type);
                    }
                }
                array[i] = result;
            }
        }
        return array;
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        if (this.prefix != null) {
            final Object object = jsonReader.readAny();
            return this.eval(object);
        }
        if (jsonReader.nextIfNull()) {
            return new Object[this.paths.length];
        }
        if (!jsonReader.nextIfObjectStart()) {
            throw new JSONException(jsonReader.info("illegal input, expect '[', but " + jsonReader.current()));
        }
        return this.objectReader.readObject(jsonReader, null, null, 0L);
    }
}
