// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

import com.alibaba.fastjson2.JSONObject;

final class NullSchema extends JSONSchema
{
    NullSchema(final JSONObject input) {
        super(input);
    }
    
    @Override
    public Type getType() {
        return Type.Null;
    }
    
    @Override
    public ValidateResult validate(final Object value) {
        if (value == null) {
            return NullSchema.SUCCESS;
        }
        return new ValidateResult(false, "expect type %s, but %s", new Object[] { Type.Null, value.getClass() });
    }
}
