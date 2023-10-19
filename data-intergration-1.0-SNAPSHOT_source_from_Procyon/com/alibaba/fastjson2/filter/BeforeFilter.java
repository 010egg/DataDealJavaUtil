// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.filter;

import com.alibaba.fastjson2.JSONWriter;

public abstract class BeforeFilter implements Filter
{
    private static final ThreadLocal<JSONWriter> serializerLocal;
    
    public void writeBefore(final JSONWriter serializer, final Object object) {
        final JSONWriter last = BeforeFilter.serializerLocal.get();
        BeforeFilter.serializerLocal.set(serializer);
        this.writeBefore(object);
        BeforeFilter.serializerLocal.set(last);
    }
    
    protected final void writeKeyValue(final String key, final Object value) {
        final JSONWriter serializer = BeforeFilter.serializerLocal.get();
        final boolean ref = serializer.containsReference(value);
        serializer.writeName(key);
        serializer.writeColon();
        serializer.writeAny(value);
        if (!ref) {
            serializer.removeReference(value);
        }
    }
    
    public abstract void writeBefore(final Object p0);
    
    static {
        serializerLocal = new ThreadLocal<JSONWriter>();
    }
}
