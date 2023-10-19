// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.lang.reflect.Type;

final class JSONPathSingleNameLong extends JSONPathTyped
{
    final long nameHashCode;
    final String name;
    
    public JSONPathSingleNameLong(final JSONPathSingleName jsonPath) {
        super(jsonPath, Long.class);
        this.nameHashCode = jsonPath.nameHashCode;
        this.name = jsonPath.name;
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        if (jsonReader.jsonb) {
            if (jsonReader.isObject()) {
                jsonReader.nextIfObjectStart();
                while (!jsonReader.nextIfObjectEnd()) {
                    final long nameHashCode = jsonReader.readFieldNameHashCode();
                    if (nameHashCode == 0L) {
                        continue;
                    }
                    final boolean match = nameHashCode == this.nameHashCode;
                    if (match || jsonReader.isObject() || jsonReader.isArray()) {
                        return jsonReader.readInt64();
                    }
                    jsonReader.skipValue();
                }
            }
        }
        else if (jsonReader.nextIfObjectStart()) {
            while (!jsonReader.nextIfObjectEnd()) {
                final long nameHashCode = jsonReader.readFieldNameHashCode();
                final boolean match = nameHashCode == this.nameHashCode;
                if (match) {
                    return jsonReader.readInt64();
                }
                jsonReader.skipValue();
            }
        }
        return null;
    }
}
