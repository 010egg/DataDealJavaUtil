// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.List;

final class JSONPathSingleIndex extends JSONPathSingle
{
    final JSONPathSegmentIndex segment;
    final int index;
    
    public JSONPathSingleIndex(final String path, final JSONPathSegmentIndex segment, final Feature... features) {
        super(segment, path, features);
        this.segment = segment;
        this.index = segment.index;
    }
    
    @Override
    public Object eval(final Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof List) {
            Object value = null;
            final List list = (List)object;
            if (this.index < list.size()) {
                value = list.get(this.index);
            }
            return value;
        }
        final Context context = new Context(this, null, this.segment, null, 0L);
        context.root = object;
        this.segment.eval(context);
        return context.value;
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        final int max = jsonReader.startArray();
        if (jsonReader.jsonb && this.index >= max) {
            return null;
        }
        if (!jsonReader.jsonb && jsonReader.nextIfArrayEnd()) {
            return null;
        }
        for (int i = 0; i < this.index && i < max; ++i) {
            jsonReader.skipValue();
            if (!jsonReader.jsonb && jsonReader.nextIfArrayEnd()) {
                return null;
            }
        }
        return jsonReader.readAny();
    }
}
