// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.math.BigDecimal;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.List;
import java.time.ZoneId;
import java.lang.reflect.Type;

final class JSONPathTypedMultiIndexes extends JSONPathTypedMulti
{
    final JSONPath prefix;
    final JSONPath[] indexPaths;
    final int[] indexes;
    final int maxIndex;
    final boolean duplicate;
    
    JSONPathTypedMultiIndexes(final JSONPath[] paths, final JSONPath prefix, final JSONPath[] indexPaths, final Type[] types, final String[] formats, final long[] pathFeatures, final ZoneId zoneId, final long features) {
        super(paths, types, formats, pathFeatures, zoneId, features);
        this.prefix = prefix;
        this.indexPaths = indexPaths;
        final int[] indexes = new int[paths.length];
        for (int i = 0; i < indexPaths.length; ++i) {
            final JSONPathSingleIndex indexPath = (JSONPathSingleIndex)indexPaths[i];
            indexes[i] = indexPath.index;
        }
        this.indexes = indexes;
        boolean duplicate = false;
        int maxIndex = -1;
        for (int j = 0; j < indexes.length; ++j) {
            final int index = indexes[j];
            if (j == 0) {
                maxIndex = index;
            }
            else {
                maxIndex = Math.max(maxIndex, index);
            }
            for (int k = 0; k < indexes.length && !duplicate; ++k) {
                if (k != j && index == indexes[k]) {
                    duplicate = true;
                    break;
                }
            }
        }
        this.duplicate = duplicate;
        this.maxIndex = maxIndex;
    }
    
    @Override
    public Object eval(final Object root) {
        final Object[] array = new Object[this.paths.length];
        Object object = root;
        if (this.prefix != null) {
            object = this.prefix.eval(root);
        }
        if (object == null) {
            return array;
        }
        if (object instanceof List) {
            final List list = (List)object;
            for (int i = 0; i < this.indexes.length; ++i) {
                final int index = this.indexes[i];
                Object result = (index < list.size()) ? list.get(index) : null;
                final Type type = this.types[i];
                try {
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
                catch (Exception e) {
                    if (!this.ignoreError(i)) {
                        throw new JSONException("jsonpath eval path, path : " + this.paths[i] + ", msg : " + e.getMessage(), e);
                    }
                }
            }
        }
        else {
            for (int j = 0; j < this.paths.length; ++j) {
                final JSONPath jsonPath = this.indexPaths[j];
                final Type type2 = this.types[j];
                try {
                    Object result = jsonPath.eval(object);
                    if (result != null && result.getClass() != type2) {
                        if (type2 == Long.class) {
                            result = TypeUtils.toLong(result);
                        }
                        else if (type2 == BigDecimal.class) {
                            result = TypeUtils.toBigDecimal(result);
                        }
                        else if (type2 == String[].class) {
                            result = TypeUtils.toStringArray(result);
                        }
                        else {
                            result = TypeUtils.cast(result, type2);
                        }
                    }
                    array[j] = result;
                }
                catch (Exception e2) {
                    if (!this.ignoreError(j)) {
                        throw new JSONException("jsonpath eval path, path : " + this.paths[j] + ", msg : " + e2.getMessage(), e2);
                    }
                }
            }
        }
        return array;
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        if (jsonReader.nextIfNull()) {
            return new Object[this.indexes.length];
        }
        if (this.prefix instanceof JSONPathSingleName) {
            final JSONPathSingleName prefixName = (JSONPathSingleName)this.prefix;
            final long prefixNameHash = prefixName.nameHashCode;
            if (!jsonReader.nextIfObjectStart()) {
                throw new JSONException(jsonReader.info("illegal input, expect '[', but " + jsonReader.current()));
            }
            while (!jsonReader.nextIfObjectEnd()) {
                final long nameHashCode = jsonReader.readFieldNameHashCode();
                final boolean match = nameHashCode == prefixNameHash;
                if (match) {
                    break;
                }
                jsonReader.skipValue();
            }
            if (jsonReader.nextIfNull()) {
                return new Object[this.indexes.length];
            }
        }
        else if (this.prefix instanceof JSONPathSingleIndex) {
            for (int index = ((JSONPathSingleIndex)this.prefix).index, max = jsonReader.startArray(), i = 0; i < index && i < max; ++i) {
                jsonReader.skipValue();
            }
            if (jsonReader.nextIfNull()) {
                return null;
            }
        }
        else if (this.prefix != null) {
            final Object object = jsonReader.readAny();
            return this.eval(object);
        }
        final int max2 = jsonReader.startArray();
        final Object[] array = new Object[this.indexes.length];
        for (int i = 0; i <= this.maxIndex && i < max2 && (jsonReader.jsonb || !jsonReader.nextIfArrayEnd()); ++i) {
            Integer index2 = null;
            for (int j = 0; j < this.indexes.length; ++j) {
                if (this.indexes[j] == i) {
                    index2 = j;
                    break;
                }
            }
            if (index2 == null) {
                jsonReader.skipValue();
            }
            else {
                final Type type = this.types[index2];
                Object value;
                try {
                    value = jsonReader.read(type);
                }
                catch (Exception e) {
                    if (!this.ignoreError(index2)) {
                        throw e;
                    }
                    value = null;
                }
                array[index2] = value;
                if (this.duplicate) {
                    for (int k = index2 + 1; k < this.indexes.length; ++k) {
                        if (this.indexes[k] == i) {
                            final Type typeJ = this.types[k];
                            Object valueJ;
                            if (typeJ == type) {
                                valueJ = value;
                            }
                            else {
                                valueJ = TypeUtils.cast(value, typeJ);
                            }
                            array[k] = valueJ;
                        }
                    }
                }
            }
        }
        return array;
    }
}
