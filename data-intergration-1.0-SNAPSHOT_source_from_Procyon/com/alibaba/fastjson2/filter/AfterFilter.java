// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.filter;

import com.alibaba.fastjson2.JSONWriter;

public abstract class AfterFilter implements Filter
{
    private static final ThreadLocal<JSONWriter> writerLocal;
    
    public void writeAfter(final JSONWriter serializer, final Object object) {
        final JSONWriter last = AfterFilter.writerLocal.get();
        AfterFilter.writerLocal.set(serializer);
        this.writeAfter(object);
        AfterFilter.writerLocal.set(last);
    }
    
    protected final void writeKeyValue(final String key, final Object value) {
        final JSONWriter serializer = AfterFilter.writerLocal.get();
        final boolean ref = serializer.containsReference(value);
        serializer.writeName(key);
        serializer.writeColon();
        serializer.writeAny(value);
        if (!ref) {
            serializer.removeReference(value);
        }
    }
    
    public abstract void writeAfter(final Object p0);
    
    static {
        writerLocal = new ThreadLocal<JSONWriter>();
    }
}
