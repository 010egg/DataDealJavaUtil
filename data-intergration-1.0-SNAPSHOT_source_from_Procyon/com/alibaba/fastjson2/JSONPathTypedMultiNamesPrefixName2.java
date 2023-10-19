// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.function.BiFunction;
import java.time.ZoneId;
import java.lang.reflect.Type;

public class JSONPathTypedMultiNamesPrefixName2 extends JSONPathTypedMultiNames
{
    final String prefixName0;
    final long prefixNameHash0;
    final String prefixName1;
    final long prefixNameHash1;
    
    JSONPathTypedMultiNamesPrefixName2(final JSONPath[] paths, final JSONPath prefix, final JSONPath[] namePaths, final Type[] types, final String[] formats, final long[] pathFeatures, final ZoneId zoneId, final long features) {
        super(paths, prefix, namePaths, types, formats, pathFeatures, zoneId, features);
        final JSONPathTwoSegment prefixTwo = (JSONPathTwoSegment)prefix;
        this.prefixName0 = ((JSONPathSegmentName)prefixTwo.first).name;
        this.prefixNameHash0 = ((JSONPathSegmentName)prefixTwo.first).nameHashCode;
        this.prefixName1 = ((JSONPathSegmentName)prefixTwo.second).name;
        this.prefixNameHash1 = ((JSONPathSegmentName)prefixTwo.second).nameHashCode;
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        if (jsonReader.nextIfNull()) {
            return new Object[this.paths.length];
        }
        if (!jsonReader.nextIfObjectStart()) {
            throw error(jsonReader);
        }
        while (!jsonReader.nextIfObjectEnd()) {
            if (jsonReader.isEnd()) {
                throw error(jsonReader);
            }
            boolean match = jsonReader.readFieldNameHashCode() == this.prefixNameHash0;
            if (!match) {
                jsonReader.skipValue();
            }
            else {
                if (jsonReader.nextIfNull()) {
                    return new Object[this.paths.length];
                }
                if (!jsonReader.nextIfObjectStart()) {
                    throw error(jsonReader);
                }
                while (!jsonReader.nextIfObjectEnd()) {
                    if (jsonReader.isEnd()) {
                        throw error(jsonReader);
                    }
                    match = (jsonReader.readFieldNameHashCode() == this.prefixNameHash1);
                    if (!match) {
                        jsonReader.skipValue();
                    }
                    else {
                        if (jsonReader.nextIfNull()) {
                            return new Object[this.paths.length];
                        }
                        return this.objectReader.readObject(jsonReader);
                    }
                }
                return new Object[this.paths.length];
            }
        }
        return new Object[this.paths.length];
    }
    
    private static JSONException error(final JSONReader jsonReader) {
        return new JSONException(jsonReader.info("illegal input, expect '[', but " + jsonReader.current()));
    }
}
