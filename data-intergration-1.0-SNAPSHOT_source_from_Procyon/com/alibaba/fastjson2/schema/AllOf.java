// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;

final class AllOf extends JSONSchema
{
    final JSONSchema[] items;
    
    public AllOf(final JSONSchema[] items) {
        super(null, null);
        this.items = items;
    }
    
    public AllOf(final JSONObject input, final JSONSchema parent) {
        super(input);
        final JSONArray items = input.getJSONArray("allOf");
        if (items == null || items.isEmpty()) {
            throw new JSONException("allOf not found");
        }
        this.items = new JSONSchema[items.size()];
        Type type = null;
        for (int i = 0; i < this.items.length; ++i) {
            JSONSchema itemSchema = null;
            final Object item = items.get(i);
            if (item instanceof Boolean) {
                itemSchema = (((boolean)item) ? Any.INSTANCE : Any.NOT_ANY);
            }
            else {
                final JSONObject itemObject = (JSONObject)item;
                if (!itemObject.containsKey("type") && type != null) {
                    switch (type) {
                        case String: {
                            itemSchema = new StringSchema(itemObject);
                            break;
                        }
                        case Integer: {
                            itemSchema = new IntegerSchema(itemObject);
                            break;
                        }
                        case Number: {
                            itemSchema = new NumberSchema(itemObject);
                            break;
                        }
                        case Boolean: {
                            itemSchema = new BooleanSchema(itemObject);
                            break;
                        }
                        case Array: {
                            itemSchema = new ArraySchema(itemObject, null);
                            break;
                        }
                        case Object: {
                            itemSchema = new ObjectSchema(itemObject);
                            break;
                        }
                    }
                }
                if (itemSchema == null) {
                    itemSchema = JSONSchema.of(itemObject, parent);
                }
            }
            type = itemSchema.getType();
            this.items[i] = itemSchema;
        }
    }
    
    @Override
    public Type getType() {
        return Type.AllOf;
    }
    
    @Override
    public ValidateResult validate(final Object value) {
        for (final JSONSchema item : this.items) {
            final ValidateResult result = item.validate(value);
            if (!result.isSuccess()) {
                return result;
            }
        }
        return AllOf.SUCCESS;
    }
}
