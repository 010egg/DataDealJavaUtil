// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

import java.util.HashMap;
import java.util.function.Predicate;
import java.util.Set;
import java.util.Collection;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONArray;
import java.util.LinkedHashMap;
import com.alibaba.fastjson2.JSONObject;
import java.util.Map;

public final class ArraySchema extends JSONSchema
{
    final Map<String, JSONSchema> definitions;
    final Map<String, JSONSchema> defs;
    final boolean typed;
    final int maxLength;
    final int minLength;
    JSONSchema itemSchema;
    final JSONSchema[] prefixItems;
    final boolean additionalItems;
    final JSONSchema additionalItem;
    final JSONSchema contains;
    final int minContains;
    final int maxContains;
    final boolean uniqueItems;
    final AllOf allOf;
    final AnyOf anyOf;
    final OneOf oneOf;
    
    public ArraySchema(final JSONObject input, final JSONSchema root) {
        super(input);
        this.typed = "array".equals(input.get("type"));
        this.definitions = new LinkedHashMap<String, JSONSchema>();
        this.defs = new LinkedHashMap<String, JSONSchema>();
        final JSONObject definitions = input.getJSONObject("definitions");
        if (definitions != null) {
            for (final Map.Entry<String, Object> entry : definitions.entrySet()) {
                final String entryKey = entry.getKey();
                final JSONObject entryValue = entry.getValue();
                final JSONSchema schema = JSONSchema.of(entryValue, (root == null) ? this : root);
                this.definitions.put(entryKey, schema);
            }
        }
        final JSONObject defs = input.getJSONObject("$defs");
        if (defs != null) {
            for (final Map.Entry<String, Object> entry2 : defs.entrySet()) {
                final String entryKey2 = entry2.getKey();
                final JSONObject entryValue2 = entry2.getValue();
                final JSONSchema schema2 = JSONSchema.of(entryValue2, (root == null) ? this : root);
                this.defs.put(entryKey2, schema2);
            }
        }
        this.minLength = input.getIntValue("minItems", -1);
        this.maxLength = input.getIntValue("maxItems", -1);
        final Object items = input.get("items");
        final Object additionalItems = input.get("additionalItems");
        JSONArray prefixItems = input.getJSONArray("prefixItems");
        if (items == null) {
            final boolean additionalItemsSupport = true;
            this.itemSchema = null;
        }
        else if (items instanceof Boolean) {
            final boolean additionalItemsSupport = (boolean)items;
            this.itemSchema = null;
        }
        else if (items instanceof JSONArray) {
            if (prefixItems != null) {
                throw new JSONException("schema error, items : " + items);
            }
            prefixItems = (JSONArray)items;
            this.itemSchema = null;
            final boolean additionalItemsSupport = true;
        }
        else {
            final boolean additionalItemsSupport = true;
            this.itemSchema = JSONSchema.of((JSONObject)items, (root != null) ? root : this);
        }
        boolean additionalItemsSupport;
        if (additionalItems instanceof JSONObject) {
            this.additionalItem = JSONSchema.of((JSONObject)additionalItems, (root == null) ? this : root);
            additionalItemsSupport = true;
        }
        else if (additionalItems instanceof Boolean) {
            additionalItemsSupport = (boolean)additionalItems;
            this.additionalItem = null;
        }
        else {
            this.additionalItem = null;
        }
        if (this.itemSchema != null && !(this.itemSchema instanceof Any)) {
            additionalItemsSupport = true;
        }
        else if (prefixItems == null && !(items instanceof Boolean)) {
            additionalItemsSupport = true;
        }
        this.additionalItems = additionalItemsSupport;
        if (prefixItems == null) {
            this.prefixItems = new JSONSchema[0];
        }
        else {
            this.prefixItems = new JSONSchema[prefixItems.size()];
            for (int i = 0; i < prefixItems.size(); ++i) {
                final Object prefixItem = prefixItems.get(i);
                JSONSchema schema3;
                if (prefixItem instanceof Boolean) {
                    schema3 = (((boolean)prefixItem) ? Any.INSTANCE : Any.NOT_ANY);
                }
                else {
                    final JSONObject jsonObject = (JSONObject)prefixItem;
                    schema3 = JSONSchema.of(jsonObject, (root == null) ? this : root);
                }
                this.prefixItems[i] = schema3;
            }
        }
        this.contains = input.getObject("contains", JSONSchema::of);
        this.minContains = input.getIntValue("minContains", -1);
        this.maxContains = input.getIntValue("maxContains", -1);
        this.uniqueItems = input.getBooleanValue("uniqueItems");
        this.allOf = JSONSchema.allOf(input, null);
        this.anyOf = JSONSchema.anyOf(input, null);
        this.oneOf = JSONSchema.oneOf(input, null);
    }
    
    @Override
    public Type getType() {
        return Type.Array;
    }
    
    @Override
    public ValidateResult validate(final Object value) {
        if (value == null) {
            return this.typed ? ArraySchema.FAIL_INPUT_NULL : ArraySchema.SUCCESS;
        }
        Set uniqueItemsSet = null;
        if (value instanceof Object[]) {
            final Object[] array = (Object[])value;
            final int size = array.length;
            if (this.minLength >= 0 && size < this.minLength) {
                return new ValidateResult(false, "minLength not match, expect >= %s, but %s", new Object[] { this.minLength, size });
            }
            if (this.maxLength >= 0 && size > this.maxLength) {
                return new ValidateResult(false, "maxLength not match, expect <= %s, but %s", new Object[] { this.maxLength, size });
            }
            int containsCount = 0;
            for (int index = 0; index < array.length; ++index) {
                Object item = array[index];
                boolean prefixMatch = false;
                if (index < this.prefixItems.length) {
                    final ValidateResult result = this.prefixItems[index].validate(item);
                    if (!result.isSuccess()) {
                        return result;
                    }
                    prefixMatch = true;
                }
                if (!prefixMatch && this.itemSchema != null) {
                    final ValidateResult result = this.itemSchema.validate(item);
                    if (!result.isSuccess()) {
                        return result;
                    }
                }
                if (this.contains != null && (this.minContains > 0 || this.maxContains > 0 || containsCount == 0)) {
                    final ValidateResult result = this.contains.validate(item);
                    if (result == ArraySchema.SUCCESS) {
                        ++containsCount;
                    }
                }
                if (this.uniqueItems) {
                    if (uniqueItemsSet == null) {
                        uniqueItemsSet = new HashSet(size);
                    }
                    if (item instanceof BigDecimal) {
                        item = ((BigDecimal)item).stripTrailingZeros();
                    }
                    if (!uniqueItemsSet.add(item)) {
                        return ArraySchema.UNIQUE_ITEMS_NOT_MATCH;
                    }
                }
            }
            if (this.contains != null && containsCount == 0) {
                return ArraySchema.CONTAINS_NOT_MATCH;
            }
            if (this.minContains >= 0 && containsCount < this.minContains) {
                return new ValidateResult(false, "minContains not match, expect %s, but %s", new Object[] { this.minContains, containsCount });
            }
            if (this.maxContains >= 0 && containsCount > this.maxContains) {
                return new ValidateResult(false, "maxContains not match, expect %s, but %s", new Object[] { this.maxContains, containsCount });
            }
            if (!this.additionalItems && size > this.prefixItems.length) {
                return new ValidateResult(false, "additional items not match, max size %s, but %s", new Object[] { this.prefixItems.length, size });
            }
            if (this.allOf != null) {
                final ValidateResult result2 = this.allOf.validate(value);
                if (!result2.isSuccess()) {
                    return result2;
                }
            }
            if (this.anyOf != null) {
                final ValidateResult result2 = this.anyOf.validate(value);
                if (!result2.isSuccess()) {
                    return result2;
                }
            }
            if (this.oneOf != null) {
                final ValidateResult result2 = this.oneOf.validate(value);
                if (!result2.isSuccess()) {
                    return result2;
                }
            }
            return ArraySchema.SUCCESS;
        }
        else if (value.getClass().isArray()) {
            final int size2 = Array.getLength(value);
            if (this.minLength >= 0 && size2 < this.minLength) {
                return new ValidateResult(false, "minLength not match, expect >= %s, but %s", new Object[] { this.minLength, size2 });
            }
            if (this.maxLength >= 0 && size2 > this.maxLength) {
                return new ValidateResult(false, "maxLength not match, expect <= %s, but %s", new Object[] { this.maxLength, size2 });
            }
            int containsCount2 = 0;
            for (int index2 = 0; index2 < size2; ++index2) {
                Object item2 = Array.get(value, index2);
                boolean prefixMatch2 = false;
                if (index2 < this.prefixItems.length) {
                    final ValidateResult result3 = this.prefixItems[index2].validate(item2);
                    if (!result3.isSuccess()) {
                        return result3;
                    }
                    prefixMatch2 = true;
                }
                if (!prefixMatch2 && this.itemSchema != null) {
                    final ValidateResult result3 = this.itemSchema.validate(item2);
                    if (!result3.isSuccess()) {
                        return result3;
                    }
                }
                if (this.contains != null && (this.minContains > 0 || this.maxContains > 0 || containsCount2 == 0)) {
                    final ValidateResult result3 = this.contains.validate(item2);
                    if (result3 == ArraySchema.SUCCESS) {
                        ++containsCount2;
                    }
                }
                if (this.uniqueItems) {
                    if (uniqueItemsSet == null) {
                        uniqueItemsSet = new HashSet(size2);
                    }
                    if (item2 instanceof BigDecimal) {
                        item2 = ((BigDecimal)item2).stripTrailingZeros();
                    }
                    if (!uniqueItemsSet.add(item2)) {
                        return ArraySchema.UNIQUE_ITEMS_NOT_MATCH;
                    }
                }
            }
            if (this.contains != null && containsCount2 == 0) {
                return ArraySchema.CONTAINS_NOT_MATCH;
            }
            if (this.minContains >= 0 && containsCount2 < this.minContains) {
                return new ValidateResult(false, "minContains not match, expect %s, but %s", new Object[] { this.minContains, containsCount2 });
            }
            if (this.maxContains >= 0 && containsCount2 > this.maxContains) {
                return new ValidateResult(false, "maxContains not match, expect %s, but %s", new Object[] { this.maxContains, containsCount2 });
            }
            if (!this.additionalItems && size2 > this.prefixItems.length) {
                return new ValidateResult(false, "additional items not match, max size %s, but %s", new Object[] { this.prefixItems.length, size2 });
            }
            if (this.allOf != null) {
                final ValidateResult result4 = this.allOf.validate(value);
                if (!result4.isSuccess()) {
                    return result4;
                }
            }
            if (this.anyOf != null) {
                final ValidateResult result4 = this.anyOf.validate(value);
                if (!result4.isSuccess()) {
                    return result4;
                }
            }
            if (this.oneOf != null) {
                final ValidateResult result4 = this.oneOf.validate(value);
                if (!result4.isSuccess()) {
                    return result4;
                }
            }
            return ArraySchema.SUCCESS;
        }
        else {
            if (!(value instanceof Collection)) {
                return this.typed ? ArraySchema.FAIL_TYPE_NOT_MATCH : ArraySchema.SUCCESS;
            }
            final int size2 = ((Collection)value).size();
            if (this.minLength >= 0 && size2 < this.minLength) {
                return new ValidateResult(false, "minLength not match, expect >= %s, but %s", new Object[] { this.minLength, size2 });
            }
            if (this.maxLength >= 0 && size2 > this.maxLength) {
                return new ValidateResult(false, "maxLength not match, expect <= %s, but %s", new Object[] { this.maxLength, size2 });
            }
            if (!this.additionalItems && size2 > this.prefixItems.length) {
                return new ValidateResult(false, "additional items not match, max size %s, but %s", new Object[] { this.prefixItems.length, size2 });
            }
            int index3 = 0;
            int containsCount = 0;
            for (Object item : (Iterable)value) {
                boolean prefixMatch = false;
                if (index3 < this.prefixItems.length) {
                    final ValidateResult result = this.prefixItems[index3].validate(item);
                    if (!result.isSuccess()) {
                        return result;
                    }
                    prefixMatch = true;
                }
                else if (this.itemSchema == null && this.additionalItem != null) {
                    final ValidateResult result = this.additionalItem.validate(item);
                    if (!result.isSuccess()) {
                        return result;
                    }
                }
                if (!prefixMatch && this.itemSchema != null) {
                    final ValidateResult result = this.itemSchema.validate(item);
                    if (!result.isSuccess()) {
                        return result;
                    }
                }
                if (this.contains != null && (this.minContains > 0 || this.maxContains > 0 || containsCount == 0)) {
                    final ValidateResult result = this.contains.validate(item);
                    if (result == ArraySchema.SUCCESS) {
                        ++containsCount;
                    }
                }
                if (this.uniqueItems) {
                    if (uniqueItemsSet == null) {
                        uniqueItemsSet = new HashSet();
                    }
                    if (item instanceof BigDecimal) {
                        item = ((BigDecimal)item).stripTrailingZeros();
                    }
                    if (!uniqueItemsSet.add(item)) {
                        return ArraySchema.UNIQUE_ITEMS_NOT_MATCH;
                    }
                }
                ++index3;
            }
            if (this.contains != null) {
                if (this.minContains >= 0 && containsCount < this.minContains) {
                    return new ValidateResult(false, "minContains not match, expect %s, but %s", new Object[] { this.minContains, containsCount });
                }
                if (containsCount == 0 && this.minContains != 0) {
                    return ArraySchema.CONTAINS_NOT_MATCH;
                }
                if (this.maxContains >= 0 && containsCount > this.maxContains) {
                    return new ValidateResult(false, "maxContains not match, expect %s, but %s", new Object[] { this.maxContains, containsCount });
                }
            }
            if (this.allOf != null) {
                final ValidateResult result2 = this.allOf.validate(value);
                if (!result2.isSuccess()) {
                    return result2;
                }
            }
            if (this.anyOf != null) {
                final ValidateResult result2 = this.anyOf.validate(value);
                if (!result2.isSuccess()) {
                    return result2;
                }
            }
            if (this.oneOf != null) {
                final ValidateResult result2 = this.oneOf.validate(value);
                if (!result2.isSuccess()) {
                    return result2;
                }
            }
            return ArraySchema.SUCCESS;
        }
    }
    
    @Override
    public JSONObject toJSONObject() {
        final JSONObject object = new JSONObject();
        ((HashMap<String, String>)object).put("type", "array");
        if (this.maxLength != -1) {
            ((HashMap<String, Integer>)object).put("maxLength", this.maxLength);
        }
        if (this.minLength != -1) {
            ((HashMap<String, Integer>)object).put("minLength", this.minLength);
        }
        if (this.itemSchema != null) {
            ((HashMap<String, JSONSchema>)object).put("items", this.itemSchema);
        }
        if (this.prefixItems != null && this.prefixItems.length != 0) {
            ((HashMap<String, JSONSchema[]>)object).put("prefixItems", this.prefixItems);
        }
        if (!this.additionalItems) {
            ((HashMap<String, Boolean>)object).put("additionalItems", this.additionalItems);
        }
        if (this.additionalItem != null) {
            ((HashMap<String, JSONSchema>)object).put("additionalItem", this.additionalItem);
        }
        if (this.contains != null) {
            ((HashMap<String, JSONSchema>)object).put("contains", this.contains);
        }
        if (this.minContains != -1) {
            ((HashMap<String, Integer>)object).put("minContains", this.minContains);
        }
        if (this.maxContains != -1) {
            ((HashMap<String, Integer>)object).put("maxContains", this.maxContains);
        }
        if (this.uniqueItems) {
            ((HashMap<String, Boolean>)object).put("uniqueItems", this.uniqueItems);
        }
        if (this.allOf != null) {
            ((HashMap<String, AllOf>)object).put("allOf", this.allOf);
        }
        if (this.anyOf != null) {
            ((HashMap<String, AnyOf>)object).put("anyOf", this.anyOf);
        }
        if (this.oneOf != null) {
            ((HashMap<String, OneOf>)object).put("oneOf", this.oneOf);
        }
        return object;
    }
    
    @Override
    public void accept(final Predicate<JSONSchema> v) {
        if (v.test(this) && this.itemSchema != null) {
            this.itemSchema.accept(v);
        }
    }
    
    public JSONSchema getItemSchema() {
        return this.itemSchema;
    }
}
