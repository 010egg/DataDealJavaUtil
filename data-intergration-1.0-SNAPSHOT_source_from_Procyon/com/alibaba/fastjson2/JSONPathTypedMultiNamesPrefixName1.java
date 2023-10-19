// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.function.BiFunction;
import java.time.ZoneId;
import java.lang.reflect.Type;

public class JSONPathTypedMultiNamesPrefixName1 extends JSONPathTypedMultiNames
{
    final JSONPathSingleName prefixName;
    final long prefixNameHash;
    
    JSONPathTypedMultiNamesPrefixName1(final JSONPath[] paths, final JSONPath prefix, final JSONPath[] namePaths, final Type[] types, final String[] formats, final long[] pathFeatures, final ZoneId zoneId, final long features) {
        super(paths, prefix, namePaths, types, formats, pathFeatures, zoneId, features);
        this.prefixName = (JSONPathSingleName)prefix;
        this.prefixNameHash = this.prefixName.nameHashCode;
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        if (jsonReader.nextIfNull()) {
            return new Object[this.paths.length];
        }
        if (!jsonReader.nextIfObjectStart()) {
            throw new JSONException(jsonReader.info("illegal input, expect '[', but " + jsonReader.current()));
        }
        while (!jsonReader.nextIfObjectEnd()) {
            if (jsonReader.isEnd()) {
                throw new JSONException(jsonReader.info("illegal input, expect '[', but " + jsonReader.current()));
            }
            final long nameHashCode = jsonReader.readFieldNameHashCode();
            final boolean match = nameHashCode == this.prefixNameHash;
            if (!match) {
                jsonReader.skipValue();
            }
            else {
                if (jsonReader.nextIfNull()) {
                    return new Object[this.paths.length];
                }
                if (!jsonReader.nextIfObjectStart()) {
                    throw new JSONException(jsonReader.info("illegal input, expect '[', but " + jsonReader.current()));
                }
                return this.objectReader.readObject(jsonReader);
            }
        }
        return new Object[this.paths.length];
    }
}
