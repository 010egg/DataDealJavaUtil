// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

import com.alibaba.fastjson2.JSONObject;

public final class BooleanSchema extends JSONSchema
{
    BooleanSchema(final JSONObject input) {
        super(input);
    }
    
    @Override
    public Type getType() {
        return Type.Boolean;
    }
    
    @Override
    public ValidateResult validate(final Object value) {
        if (value == null) {
            return BooleanSchema.FAIL_INPUT_NULL;
        }
        if (value instanceof Boolean) {
            return BooleanSchema.SUCCESS;
        }
        return new ValidateResult(false, "expect type %s, but %s", new Object[] { Type.Boolean, value.getClass() });
    }
    
    @Override
    public JSONObject toJSONObject() {
        return JSONObject.of("type", (Object)"boolean");
    }
}
