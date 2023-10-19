// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson.serializer;

public abstract class AfterFilter implements SerializeFilter
{
    private static final ThreadLocal<JSONSerializer> serializerLocal;
    private static final ThreadLocal<Character> seperatorLocal;
    private static final Character COMMA;
    
    final char writeAfter(final JSONSerializer serializer, final Object object, final char seperator) {
        final JSONSerializer last = AfterFilter.serializerLocal.get();
        AfterFilter.serializerLocal.set(serializer);
        AfterFilter.seperatorLocal.set(seperator);
        this.writeAfter(object);
        AfterFilter.serializerLocal.set(last);
        return AfterFilter.seperatorLocal.get();
    }
    
    protected final void writeKeyValue(final String key, final Object value) {
        final JSONSerializer serializer = AfterFilter.serializerLocal.get();
        final char seperator = AfterFilter.seperatorLocal.get();
        final boolean ref = serializer.containsReference(value);
        serializer.writeKeyValue(seperator, key, value);
        if (!ref && serializer.references != null) {
            serializer.references.remove(value);
        }
        if (seperator != ',') {
            AfterFilter.seperatorLocal.set(AfterFilter.COMMA);
        }
    }
    
    public abstract void writeAfter(final Object p0);
    
    static {
        serializerLocal = new ThreadLocal<JSONSerializer>();
        seperatorLocal = new ThreadLocal<Character>();
        COMMA = ',';
    }
}
