// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson.serializer;

public abstract class BeforeFilter implements SerializeFilter
{
    private static final ThreadLocal<JSONSerializer> serializerLocal;
    private static final ThreadLocal<Character> seperatorLocal;
    private static final Character COMMA;
    
    final char writeBefore(final JSONSerializer serializer, final Object object, final char seperator) {
        final JSONSerializer last = BeforeFilter.serializerLocal.get();
        BeforeFilter.serializerLocal.set(serializer);
        BeforeFilter.seperatorLocal.set(seperator);
        this.writeBefore(object);
        BeforeFilter.serializerLocal.set(last);
        return BeforeFilter.seperatorLocal.get();
    }
    
    protected final void writeKeyValue(final String key, final Object value) {
        final JSONSerializer serializer = BeforeFilter.serializerLocal.get();
        final char seperator = BeforeFilter.seperatorLocal.get();
        final boolean ref = serializer.references.containsKey(value);
        serializer.writeKeyValue(seperator, key, value);
        if (!ref) {
            serializer.references.remove(value);
        }
        if (seperator != ',') {
            BeforeFilter.seperatorLocal.set(BeforeFilter.COMMA);
        }
    }
    
    public abstract void writeBefore(final Object p0);
    
    static {
        serializerLocal = new ThreadLocal<JSONSerializer>();
        seperatorLocal = new ThreadLocal<Character>();
        COMMA = ',';
    }
}
