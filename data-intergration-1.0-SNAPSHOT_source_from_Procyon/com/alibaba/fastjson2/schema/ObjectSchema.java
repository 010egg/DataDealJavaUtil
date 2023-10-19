// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

import java.util.HashMap;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.Predicate;
import com.alibaba.fastjson2.annotation.JSONField;
import java.util.List;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONArray;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.Fnv;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.LinkedHashMap;
import com.alibaba.fastjson2.JSONObject;
import java.util.Set;
import java.util.Map;

public final class ObjectSchema extends JSONSchema
{
    final boolean typed;
    final Map<String, JSONSchema> definitions;
    final Map<String, JSONSchema> defs;
    final Map<String, JSONSchema> properties;
    final Set<String> required;
    final boolean additionalProperties;
    final JSONSchema additionalPropertySchema;
    final long[] requiredHashCode;
    final PatternProperty[] patternProperties;
    final JSONSchema propertyNames;
    final int minProperties;
    final int maxProperties;
    final Map<String, String[]> dependentRequired;
    final Map<Long, long[]> dependentRequiredHashCodes;
    final Map<String, JSONSchema> dependentSchemas;
    final Map<Long, JSONSchema> dependentSchemasHashMapping;
    final JSONSchema ifSchema;
    final JSONSchema thenSchema;
    final JSONSchema elseSchema;
    final AllOf allOf;
    final AnyOf anyOf;
    final OneOf oneOf;
    
    public ObjectSchema(final JSONObject input) {
        this(input, null);
    }
    
    public ObjectSchema(final JSONObject input, final JSONSchema root) {
        super(input);
        this.typed = "object".equalsIgnoreCase(input.getString("type"));
        this.properties = new LinkedHashMap<String, JSONSchema>();
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
        final JSONObject properties = input.getJSONObject("properties");
        if (properties != null) {
            for (final Map.Entry<String, Object> entry3 : properties.entrySet()) {
                final String entryKey3 = entry3.getKey();
                final Object entryValue3 = entry3.getValue();
                JSONSchema schema3;
                if (entryValue3 instanceof Boolean) {
                    schema3 = (((boolean)entryValue3) ? Any.INSTANCE : Any.NOT_ANY);
                }
                else if (entryValue3 instanceof JSONSchema) {
                    schema3 = (JSONSchema)entryValue3;
                }
                else {
                    schema3 = JSONSchema.of((JSONObject)entryValue3, (root == null) ? this : root);
                }
                this.properties.put(entryKey3, schema3);
            }
        }
        final JSONObject patternProperties = input.getJSONObject("patternProperties");
        if (patternProperties != null) {
            this.patternProperties = new PatternProperty[patternProperties.size()];
            int index = 0;
            for (final Map.Entry<String, Object> entry4 : patternProperties.entrySet()) {
                final String entryKey4 = entry4.getKey();
                final Object entryValue4 = entry4.getValue();
                JSONSchema schema4;
                if (entryValue4 instanceof Boolean) {
                    schema4 = (((boolean)entryValue4) ? Any.INSTANCE : Any.NOT_ANY);
                }
                else {
                    schema4 = JSONSchema.of((JSONObject)entryValue4, (root == null) ? this : root);
                }
                this.patternProperties[index++] = new PatternProperty(Pattern.compile(entryKey4), schema4);
            }
        }
        else {
            this.patternProperties = new PatternProperty[0];
        }
        final JSONArray required = input.getJSONArray("required");
        if (required == null || required.isEmpty()) {
            this.required = Collections.emptySet();
            this.requiredHashCode = new long[0];
        }
        else {
            this.required = new LinkedHashSet<String>(required.size());
            for (int i = 0; i < required.size(); ++i) {
                this.required.add(required.getString(i));
            }
            this.requiredHashCode = new long[this.required.size()];
            int i = 0;
            for (final String item : this.required) {
                this.requiredHashCode[i++] = Fnv.hashCode64(item);
            }
        }
        final Object additionalProperties = input.get("additionalProperties");
        if (additionalProperties instanceof Boolean) {
            this.additionalPropertySchema = null;
            this.additionalProperties = (boolean)additionalProperties;
        }
        else if (additionalProperties instanceof JSONObject) {
            this.additionalPropertySchema = JSONSchema.of((JSONObject)additionalProperties, root);
            this.additionalProperties = false;
        }
        else {
            this.additionalPropertySchema = null;
            this.additionalProperties = true;
        }
        final Object propertyNames = input.get("propertyNames");
        if (propertyNames == null) {
            this.propertyNames = null;
        }
        else if (propertyNames instanceof Boolean) {
            this.propertyNames = (((boolean)propertyNames) ? Any.INSTANCE : Any.NOT_ANY);
        }
        else {
            this.propertyNames = new StringSchema((JSONObject)propertyNames);
        }
        this.minProperties = input.getIntValue("minProperties", -1);
        this.maxProperties = input.getIntValue("maxProperties", -1);
        final JSONObject dependentRequired = input.getJSONObject("dependentRequired");
        if (dependentRequired != null && !dependentRequired.isEmpty()) {
            this.dependentRequired = new LinkedHashMap<String, String[]>(dependentRequired.size());
            this.dependentRequiredHashCodes = new LinkedHashMap<Long, long[]>(dependentRequired.size());
            final Set<String> keys = ((LinkedHashMap<String, V>)dependentRequired).keySet();
            for (final String key : keys) {
                final String[] dependentRequiredProperties = dependentRequired.getObject(key, String[].class, new JSONReader.Feature[0]);
                final long[] dependentRequiredPropertiesHash = new long[dependentRequiredProperties.length];
                for (int j = 0; j < dependentRequiredProperties.length; ++j) {
                    dependentRequiredPropertiesHash[j] = Fnv.hashCode64(dependentRequiredProperties[j]);
                }
                this.dependentRequired.put(key, dependentRequiredProperties);
                this.dependentRequiredHashCodes.put(Fnv.hashCode64(key), dependentRequiredPropertiesHash);
            }
        }
        else {
            this.dependentRequired = null;
            this.dependentRequiredHashCodes = null;
        }
        final JSONObject dependentSchemas = input.getJSONObject("dependentSchemas");
        if (dependentSchemas != null && !dependentSchemas.isEmpty()) {
            this.dependentSchemas = new LinkedHashMap<String, JSONSchema>(dependentSchemas.size());
            this.dependentSchemasHashMapping = new LinkedHashMap<Long, JSONSchema>(dependentSchemas.size());
            final Set<String> keys2 = ((LinkedHashMap<String, V>)dependentSchemas).keySet();
            for (final String key2 : keys2) {
                final JSONSchema dependentSchema = dependentSchemas.getObject(key2, JSONSchema::of);
                this.dependentSchemas.put(key2, dependentSchema);
                this.dependentSchemasHashMapping.put(Fnv.hashCode64(key2), dependentSchema);
            }
        }
        else {
            this.dependentSchemas = null;
            this.dependentSchemasHashMapping = null;
        }
        this.ifSchema = input.getObject("if", JSONSchema::of);
        this.elseSchema = input.getObject("else", JSONSchema::of);
        this.thenSchema = input.getObject("then", JSONSchema::of);
        this.allOf = JSONSchema.allOf(input, null);
        this.anyOf = JSONSchema.anyOf(input, null);
        this.oneOf = JSONSchema.oneOf(input, null);
    }
    
    @Override
    public Type getType() {
        return Type.Object;
    }
    
    public ValidateResult validate(final Map map) {
        for (final String item : this.required) {
            if (!map.containsKey(item)) {
                return new ValidateResult(false, "required %s", new Object[] { item });
            }
        }
        for (final Map.Entry<String, JSONSchema> entry : this.properties.entrySet()) {
            final String key = entry.getKey();
            final JSONSchema schema = entry.getValue();
            final Object propertyValue = map.get(key);
            if (propertyValue == null && !map.containsKey(key)) {
                continue;
            }
            final ValidateResult result = schema.validate(propertyValue);
            if (!result.isSuccess()) {
                return new ValidateResult(result, "property %s invalid", new Object[] { key });
            }
        }
        for (final PatternProperty patternProperty : this.patternProperties) {
            for (final Map.Entry entry2 : map.entrySet()) {
                final Object entryKey = entry2.getKey();
                if (entryKey instanceof String) {
                    final String strKey = (String)entryKey;
                    if (!patternProperty.pattern.matcher(strKey).find()) {
                        continue;
                    }
                    final ValidateResult result2 = patternProperty.schema.validate(entry2.getValue());
                    if (!result2.isSuccess()) {
                        return result2;
                    }
                    continue;
                }
            }
        }
        if (!this.additionalProperties) {
        Label_0343:
            for (final Map.Entry entry3 : map.entrySet()) {
                final Object key2 = entry3.getKey();
                if (this.properties.containsKey(key2)) {
                    continue;
                }
                for (final PatternProperty patternProperty2 : this.patternProperties) {
                    if (key2 instanceof String) {
                        final String strKey = (String)key2;
                        if (patternProperty2.pattern.matcher(strKey).find()) {
                            continue Label_0343;
                        }
                    }
                }
                if (this.additionalPropertySchema == null) {
                    return new ValidateResult(false, "add additionalProperties %s", new Object[] { key2 });
                }
                final ValidateResult result3 = this.additionalPropertySchema.validate(entry3.getValue());
                if (!result3.isSuccess()) {
                    return result3;
                }
            }
        }
        if (this.propertyNames != null) {
            for (final Object key3 : map.keySet()) {
                final ValidateResult result4 = this.propertyNames.validate(key3);
                if (!result4.isSuccess()) {
                    return ObjectSchema.FAIL_PROPERTY_NAME;
                }
            }
        }
        if (this.minProperties >= 0 && map.size() < this.minProperties) {
            return new ValidateResult(false, "minProperties not match, expect %s, but %s", new Object[] { this.minProperties, map.size() });
        }
        if (this.maxProperties >= 0 && map.size() > this.maxProperties) {
            return new ValidateResult(false, "maxProperties not match, expect %s, but %s", new Object[] { this.maxProperties, map.size() });
        }
        if (this.dependentRequired != null) {
            for (final Map.Entry<String, String[]> entry4 : this.dependentRequired.entrySet()) {
                final String key = entry4.getKey();
                final Object value = map.get(key);
                if (value != null) {
                    final String[] array;
                    final String[] dependentRequiredProperties = array = entry4.getValue();
                    for (final String dependentRequiredProperty : array) {
                        if (!map.containsKey(dependentRequiredProperty)) {
                            return new ValidateResult(false, "property %s, dependentRequired property %s", new Object[] { key, dependentRequiredProperty });
                        }
                    }
                }
            }
        }
        if (this.dependentSchemas != null) {
            for (final Map.Entry<String, JSONSchema> entry : this.dependentSchemas.entrySet()) {
                final String key = entry.getKey();
                final Object fieldValue = map.get(key);
                if (fieldValue == null) {
                    continue;
                }
                final JSONSchema schema2 = entry.getValue();
                final ValidateResult result = schema2.validate(map);
                if (!result.isSuccess()) {
                    return result;
                }
            }
        }
        if (this.ifSchema != null) {
            final ValidateResult ifResult = this.ifSchema.validate(map);
            if (ifResult == ObjectSchema.SUCCESS) {
                if (this.thenSchema != null) {
                    final ValidateResult thenResult = this.thenSchema.validate(map);
                    if (!thenResult.isSuccess()) {
                        return thenResult;
                    }
                }
            }
            else if (this.elseSchema != null) {
                final ValidateResult elseResult = this.elseSchema.validate(map);
                if (!elseResult.isSuccess()) {
                    return elseResult;
                }
            }
        }
        if (this.allOf != null) {
            final ValidateResult result5 = this.allOf.validate(map);
            if (!result5.isSuccess()) {
                return result5;
            }
        }
        if (this.anyOf != null) {
            final ValidateResult result5 = this.anyOf.validate(map);
            if (!result5.isSuccess()) {
                return result5;
            }
        }
        if (this.oneOf != null) {
            final ValidateResult result5 = this.oneOf.validate(map);
            if (!result5.isSuccess()) {
                return result5;
            }
        }
        return ObjectSchema.SUCCESS;
    }
    
    @Override
    public ValidateResult validate(final Object value) {
        if (value == null) {
            return this.typed ? ObjectSchema.FAIL_INPUT_NULL : ObjectSchema.SUCCESS;
        }
        if (value instanceof Map) {
            return this.validate((Map)value);
        }
        final Class valueClass = value.getClass();
        final ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter(valueClass);
        if (!(objectWriter instanceof ObjectWriterAdapter)) {
            return this.typed ? new ValidateResult(false, "expect type %s, but %s", new Object[] { Type.Object, valueClass }) : ObjectSchema.SUCCESS;
        }
        for (int i = 0; i < this.requiredHashCode.length; ++i) {
            final long nameHash = this.requiredHashCode[i];
            final FieldWriter fieldWriter = objectWriter.getFieldWriter(nameHash);
            Object fieldValue = null;
            if (fieldWriter != null) {
                fieldValue = fieldWriter.getFieldValue(value);
            }
            if (fieldValue == null) {
                String fieldName = null;
                int j = 0;
                for (final String itemName : this.required) {
                    if (j == i) {
                        fieldName = itemName;
                    }
                    ++j;
                }
                return new ValidateResult(false, "required property %s", new Object[] { fieldName });
            }
        }
        for (final Map.Entry<String, JSONSchema> entry : this.properties.entrySet()) {
            final String key = entry.getKey();
            final long keyHash = Fnv.hashCode64(key);
            final JSONSchema schema = entry.getValue();
            final FieldWriter fieldWriter2 = objectWriter.getFieldWriter(keyHash);
            if (fieldWriter2 != null) {
                final Object propertyValue = fieldWriter2.getFieldValue(value);
                if (propertyValue == null) {
                    continue;
                }
                final ValidateResult result = schema.validate(propertyValue);
                if (!result.isSuccess()) {
                    return result;
                }
                continue;
            }
        }
        if (this.minProperties >= 0 || this.maxProperties >= 0) {
            int fieldValueCount = 0;
            final List<FieldWriter> fieldWriters = (List<FieldWriter>)objectWriter.getFieldWriters();
            for (final FieldWriter fieldWriter : fieldWriters) {
                final Object fieldValue = fieldWriter.getFieldValue(value);
                if (fieldValue != null) {
                    ++fieldValueCount;
                }
            }
            if (this.minProperties >= 0 && fieldValueCount < this.minProperties) {
                return new ValidateResult(false, "minProperties not match, expect %s, but %s", new Object[] { this.minProperties, fieldValueCount });
            }
            if (this.maxProperties >= 0 && fieldValueCount > this.maxProperties) {
                return new ValidateResult(false, "maxProperties not match, expect %s, but %s", new Object[] { this.maxProperties, fieldValueCount });
            }
        }
        if (this.dependentRequiredHashCodes != null) {
            int propertyIndex = 0;
            for (final Map.Entry<Long, long[]> entry2 : this.dependentRequiredHashCodes.entrySet()) {
                final Long keyHash2 = entry2.getKey();
                final long[] dependentRequiredProperties = entry2.getValue();
                final FieldWriter fieldWriter3 = objectWriter.getFieldWriter(keyHash2);
                final Object fieldValue2 = fieldWriter3.getFieldValue(value);
                if (fieldValue2 == null) {
                    ++propertyIndex;
                }
                else {
                    for (int requiredIndex = 0; requiredIndex < dependentRequiredProperties.length; ++requiredIndex) {
                        final long dependentRequiredHash = dependentRequiredProperties[requiredIndex];
                        final FieldWriter dependentFieldWriter = objectWriter.getFieldWriter(dependentRequiredHash);
                        if (dependentFieldWriter == null || dependentFieldWriter.getFieldValue(value) == null) {
                            int k = 0;
                            String property = null;
                            String dependentRequiredProperty = null;
                            final Iterator<Map.Entry<String, String[]>> it = this.dependentRequired.entrySet().iterator();
                            while (it.hasNext()) {
                                if (propertyIndex == k) {
                                    final Map.Entry<String, String[]> dependentRequiredEntry = it.next();
                                    property = dependentRequiredEntry.getKey();
                                    dependentRequiredProperty = dependentRequiredEntry.getValue()[requiredIndex];
                                }
                                ++k;
                            }
                            return new ValidateResult(false, "property %s, dependentRequired property %s", new Object[] { property, dependentRequiredProperty });
                        }
                    }
                    ++propertyIndex;
                }
            }
        }
        if (this.dependentSchemasHashMapping != null) {
            for (final Map.Entry<Long, JSONSchema> entry3 : this.dependentSchemasHashMapping.entrySet()) {
                final Long keyHash3 = entry3.getKey();
                final FieldWriter fieldWriter = objectWriter.getFieldWriter(keyHash3);
                if (fieldWriter != null) {
                    if (fieldWriter.getFieldValue(value) == null) {
                        continue;
                    }
                    final JSONSchema schema2 = entry3.getValue();
                    final ValidateResult result2 = schema2.validate(value);
                    if (!result2.isSuccess()) {
                        return result2;
                    }
                    continue;
                }
            }
        }
        if (this.ifSchema != null) {
            final ValidateResult ifResult = this.ifSchema.validate(value);
            if (ifResult.isSuccess()) {
                if (this.thenSchema != null) {
                    final ValidateResult thenResult = this.thenSchema.validate(value);
                    if (!thenResult.isSuccess()) {
                        return thenResult;
                    }
                }
            }
            else if (this.elseSchema != null) {
                final ValidateResult elseResult = this.elseSchema.validate(value);
                if (!elseResult.isSuccess()) {
                    return elseResult;
                }
            }
        }
        if (this.allOf != null) {
            final ValidateResult result3 = this.allOf.validate(value);
            if (!result3.isSuccess()) {
                return result3;
            }
        }
        if (this.anyOf != null) {
            final ValidateResult result3 = this.anyOf.validate(value);
            if (!result3.isSuccess()) {
                return result3;
            }
        }
        if (this.oneOf != null) {
            final ValidateResult result3 = this.oneOf.validate(value);
            if (!result3.isSuccess()) {
                return result3;
            }
        }
        return ObjectSchema.SUCCESS;
    }
    
    public Map<String, JSONSchema> getProperties() {
        return this.properties;
    }
    
    public JSONSchema getProperty(final String key) {
        return this.properties.get(key);
    }
    
    public Set<String> getRequired() {
        return this.required;
    }
    
    @JSONField(true)
    @Override
    public JSONObject toJSONObject() {
        final JSONObject object = new JSONObject();
        ((HashMap<String, String>)object).put("type", "object");
        if (this.title != null) {
            ((HashMap<String, String>)object).put("title", this.title);
        }
        if (this.description != null) {
            ((HashMap<String, String>)object).put("description", this.description);
        }
        if (!this.definitions.isEmpty()) {
            ((HashMap<String, Map<String, JSONSchema>>)object).put("definitions", this.definitions);
        }
        if (!this.defs.isEmpty()) {
            ((HashMap<String, Map<String, JSONSchema>>)object).put("defs", this.defs);
        }
        if (!this.properties.isEmpty()) {
            ((HashMap<String, Map<String, JSONSchema>>)object).put("properties", this.properties);
        }
        if (!this.required.isEmpty()) {
            ((HashMap<String, Set<String>>)object).put("required", this.required);
        }
        if (!this.additionalProperties) {
            if (this.additionalPropertySchema != null) {
                ((HashMap<String, JSONSchema>)object).put("additionalProperties", this.additionalPropertySchema);
            }
            else {
                ((HashMap<String, Boolean>)object).put("additionalProperties", this.additionalProperties);
            }
        }
        if (this.patternProperties != null && this.patternProperties.length != 0) {
            ((HashMap<String, PatternProperty[]>)object).put("patternProperties", this.patternProperties);
        }
        if (this.propertyNames != null) {
            ((HashMap<String, JSONSchema>)object).put("propertyNames", this.propertyNames);
        }
        if (this.minProperties != -1) {
            ((HashMap<String, Integer>)object).put("minProperties", this.minProperties);
        }
        if (this.maxProperties != -1) {
            ((HashMap<String, Integer>)object).put("maxProperties", this.maxProperties);
        }
        if (this.dependentRequired != null && !this.dependentRequired.isEmpty()) {
            ((HashMap<String, Map<String, String[]>>)object).put("dependentRequired", this.dependentRequired);
        }
        if (this.dependentSchemas != null && !this.dependentSchemas.isEmpty()) {
            ((HashMap<String, Map<String, JSONSchema>>)object).put("dependentSchemas", this.dependentSchemas);
        }
        if (this.ifSchema != null) {
            ((HashMap<String, JSONSchema>)object).put("if", this.ifSchema);
        }
        if (this.thenSchema != null) {
            ((HashMap<String, JSONSchema>)object).put("then", this.thenSchema);
        }
        if (this.elseSchema != null) {
            ((HashMap<String, JSONSchema>)object).put("else", this.elseSchema);
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
        if (v.test(this)) {
            final Collection<JSONSchema> values = this.properties.values();
            Objects.requireNonNull(v);
            values.forEach(v::test);
        }
    }
    
    static final class PatternProperty
    {
        final Pattern pattern;
        final JSONSchema schema;
        
        public PatternProperty(final Pattern pattern, final JSONSchema schema) {
            this.pattern = pattern;
            this.schema = schema;
        }
    }
}
