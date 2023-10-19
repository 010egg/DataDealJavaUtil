// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

final class Any extends JSONSchema
{
    public static final Any INSTANCE;
    public static final JSONSchema NOT_ANY;
    
    public Any() {
        super(null, null);
    }
    
    @Override
    public Type getType() {
        return Type.Any;
    }
    
    @Override
    public ValidateResult validate(final Object value) {
        return Any.SUCCESS;
    }
    
    static {
        INSTANCE = new Any();
        NOT_ANY = new Not(Any.INSTANCE, null, null);
    }
}
