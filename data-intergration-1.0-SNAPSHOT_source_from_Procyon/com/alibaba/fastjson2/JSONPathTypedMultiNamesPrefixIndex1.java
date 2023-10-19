// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.function.BiFunction;
import java.time.ZoneId;
import java.lang.reflect.Type;

public class JSONPathTypedMultiNamesPrefixIndex1 extends JSONPathTypedMultiNames
{
    final int index;
    
    JSONPathTypedMultiNamesPrefixIndex1(final JSONPath[] paths, final JSONPathSingleIndex prefix, final JSONPath[] namePaths, final Type[] types, final String[] formats, final long[] pathFeatures, final ZoneId zoneId, final long features) {
        super(paths, prefix, namePaths, types, formats, pathFeatures, zoneId, features);
        this.index = prefix.index;
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        if (jsonReader.nextIfNull()) {
            return new Object[this.paths.length];
        }
        if (!jsonReader.nextIfArrayStart()) {
            throw new JSONException(jsonReader.info("illegal input, expect '[', but " + jsonReader.current()));
        }
        for (int i = 0; i < this.index; ++i) {
            if (jsonReader.nextIfArrayEnd()) {
                return new Object[this.paths.length];
            }
            if (jsonReader.isEnd()) {
                throw new JSONException(jsonReader.info("illegal input, expect '[', but " + jsonReader.current()));
            }
            jsonReader.skipValue();
        }
        if (jsonReader.nextIfNull()) {
            return new Object[this.paths.length];
        }
        if (jsonReader.nextIfArrayEnd()) {
            return new Object[this.paths.length];
        }
        return this.objectReader.readObject(jsonReader);
    }
}
