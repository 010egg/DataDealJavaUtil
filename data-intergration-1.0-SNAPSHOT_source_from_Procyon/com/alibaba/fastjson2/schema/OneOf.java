// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;

final class OneOf extends JSONSchema
{
    final JSONSchema[] items;
    
    public OneOf(final JSONSchema[] items) {
        super(null, null);
        this.items = items;
    }
    
    public OneOf(final JSONObject input, final JSONSchema parent) {
        super(input);
        final JSONArray items = input.getJSONArray("oneOf");
        if (items == null || items.isEmpty()) {
            throw new JSONException("oneOf not found");
        }
        this.items = new JSONSchema[items.size()];
        for (int i = 0; i < this.items.length; ++i) {
            final Object item = items.get(i);
            if (item instanceof Boolean) {
                this.items[i] = (((boolean)item) ? Any.INSTANCE : Any.NOT_ANY);
            }
            else {
                this.items[i] = JSONSchema.of((JSONObject)item, parent);
            }
        }
    }
    
    @Override
    public Type getType() {
        return Type.OneOf;
    }
    
    @Override
    public ValidateResult validate(final Object value) {
        int count = 0;
        for (final JSONSchema item : this.items) {
            final ValidateResult result = item.validate(value);
            if (result.isSuccess() && ++count > 1) {
                return OneOf.FAIL_ONE_OF;
            }
        }
        return (count != 1) ? OneOf.FAIL_ONE_OF : OneOf.SUCCESS;
    }
}
