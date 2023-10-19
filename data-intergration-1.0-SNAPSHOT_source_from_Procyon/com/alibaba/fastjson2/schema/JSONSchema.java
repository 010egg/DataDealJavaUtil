// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.util.concurrent.ConcurrentHashMap;
import com.alibaba.fastjson2.reader.FieldReader;
import java.util.function.Predicate;
import java.net.URL;
import com.alibaba.fastjson2.JSONSchemaValidException;
import java.net.URLDecoder;
import com.alibaba.fastjson2.JSON;
import java.util.ArrayList;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.reader.ObjectReaderAdapter;
import com.alibaba.fastjson2.reader.ObjectReaderBean;
import com.alibaba.fastjson2.JSONFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import com.alibaba.fastjson2.annotation.JSONCreator;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONArray;
import java.util.Collection;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.math.BigInteger;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import java.util.Map;
import com.alibaba.fastjson2.annotation.JSONType;

@JSONType(serializer = JSONSchemaWriter.class)
public abstract class JSONSchema
{
    static final Map<String, JSONSchema> CACHE;
    final String title;
    final String description;
    static final JSONReader.Context CONTEXT;
    static final ValidateResult SUCCESS;
    static final ValidateResult FAIL_INPUT_NULL;
    static final ValidateResult FAIL_ANY_OF;
    static final ValidateResult FAIL_ONE_OF;
    static final ValidateResult FAIL_NOT;
    static final ValidateResult FAIL_TYPE_NOT_MATCH;
    static final ValidateResult FAIL_PROPERTY_NAME;
    static final ValidateResult CONTAINS_NOT_MATCH;
    static final ValidateResult UNIQUE_ITEMS_NOT_MATCH;
    static final ValidateResult REQUIRED_NOT_MATCH;
    
    JSONSchema(final JSONObject input) {
        this.title = input.getString("title");
        this.description = input.getString("description");
    }
    
    JSONSchema(final String title, final String description) {
        this.title = title;
        this.description = description;
    }
    
    public static JSONSchema of(final JSONObject input, final Class objectClass) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        if (objectClass == null || objectClass == Object.class) {
            return of(input);
        }
        if (objectClass == Byte.TYPE || objectClass == Short.TYPE || objectClass == Integer.TYPE || objectClass == Long.TYPE || objectClass == Byte.class || objectClass == Short.class || objectClass == Integer.class || objectClass == Long.class || objectClass == BigInteger.class || objectClass == AtomicInteger.class || objectClass == AtomicLong.class) {
            if (input.containsKey("AnyOf") || input.containsKey("anyOf")) {
                return anyOf(input, objectClass);
            }
            if (input.containsKey("oneOf")) {
                return oneOf(input, objectClass);
            }
            if (input.containsKey("not")) {
                return ofNot(input, objectClass);
            }
            return new IntegerSchema(input);
        }
        else if (objectClass == BigDecimal.class || objectClass == Float.TYPE || objectClass == Double.TYPE || objectClass == Float.class || objectClass == Double.class || objectClass == Number.class) {
            if (input.containsKey("AnyOf") || input.containsKey("anyOf")) {
                return anyOf(input, objectClass);
            }
            if (input.containsKey("oneOf")) {
                return oneOf(input, objectClass);
            }
            if (input.containsKey("not")) {
                return ofNot(input, objectClass);
            }
            return new NumberSchema(input);
        }
        else {
            if (objectClass == Boolean.TYPE || objectClass == Boolean.class) {
                return new BooleanSchema(input);
            }
            if (objectClass == String.class) {
                return new StringSchema(input);
            }
            if (Collection.class.isAssignableFrom(objectClass)) {
                return new ArraySchema(input, null);
            }
            if (objectClass.isArray()) {
                return new ArraySchema(input, null);
            }
            return new ObjectSchema(input, null);
        }
    }
    
    static Not ofNot(final JSONObject input, final Class objectClass) {
        final Object not = input.get("not");
        if (not instanceof Boolean) {
            return new Not(null, null, (Boolean)not);
        }
        final JSONObject object = (JSONObject)not;
        if (object == null || object.isEmpty()) {
            return new Not(null, new Type[] { Type.Any }, null);
        }
        if (object.size() == 1) {
            final Object type = object.get("type");
            if (type instanceof JSONArray) {
                final JSONArray array = (JSONArray)type;
                final Type[] types = new Type[array.size()];
                for (int i = 0; i < array.size(); ++i) {
                    types[i] = array.getObject(i, Type.class, new JSONReader.Feature[0]);
                }
                return new Not(null, types, null);
            }
        }
        final JSONSchema schema = of(object, objectClass);
        return new Not(schema, null, null);
    }
    
    public static JSONSchema parseSchema(final String schema) {
        if ("true".equals(schema)) {
            return Any.INSTANCE;
        }
        if ("false".equals(schema)) {
            return Any.NOT_ANY;
        }
        final JSONReader reader = JSONReader.of(schema);
        try {
            final ObjectReader<?> objectReader = (ObjectReader<?>)reader.getObjectReader(Object.class);
            final JSONObject object = (JSONObject)objectReader.readObject(reader, null, null, 0L);
            final JSONSchema of = of(object);
            if (reader != null) {
                reader.close();
            }
            return of;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    @JSONCreator
    public static JSONSchema of(final JSONObject input) {
        return of(input, (JSONSchema)null);
    }
    
    public static JSONSchema of(final java.lang.reflect.Type type) {
        return of(type, null);
    }
    
    public static JSONSchema ofValue(final Object value) {
        return ofValue(value, null);
    }
    
    static JSONSchema ofValue(final Object value, final JSONSchema root) {
        if (value == null) {
            return null;
        }
        if (value instanceof Collection) {
            final Collection collection = (Collection)value;
            if (collection.isEmpty()) {
                return new ArraySchema(JSONObject.of("type", (Object)"array"), root);
            }
            Object firstItem = null;
            Class firstItemClass = null;
            boolean sameClass = true;
            for (final Object item : collection) {
                if (item != null) {
                    if (firstItem == null) {
                        firstItem = item;
                    }
                    if (firstItemClass == null) {
                        firstItemClass = item.getClass();
                    }
                    else {
                        if (firstItemClass == item.getClass()) {
                            continue;
                        }
                        sameClass = false;
                    }
                }
            }
            if (sameClass) {
                JSONSchema itemSchema;
                if (Map.class.isAssignableFrom(firstItemClass)) {
                    itemSchema = ofValue(firstItem, root);
                }
                else {
                    itemSchema = of(firstItemClass, root);
                }
                final ArraySchema schema = new ArraySchema(JSONObject.of("type", (Object)"array"), root);
                schema.itemSchema = itemSchema;
                return schema;
            }
        }
        if (value instanceof Map) {
            final JSONObject object = JSONObject.of("type", (Object)"object");
            final ObjectSchema schema2 = new ObjectSchema(object, root);
            final Map map = (Map)value;
            for (final Map.Entry entry : map.entrySet()) {
                final Object entryKey = entry.getKey();
                final Object entryValue = entry.getValue();
                if (entryKey instanceof String) {
                    JSONSchema valueSchema;
                    if (entryValue == null) {
                        valueSchema = new StringSchema(JSONObject.of());
                    }
                    else {
                        valueSchema = ofValue(entryValue, (root == null) ? schema2 : root);
                    }
                    schema2.properties.put((String)entryKey, valueSchema);
                }
            }
            return schema2;
        }
        return of(value.getClass(), root);
    }
    
    static JSONSchema of(final java.lang.reflect.Type type, final JSONSchema root) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType paramType = (ParameterizedType)type;
            final java.lang.reflect.Type rawType = paramType.getRawType();
            final java.lang.reflect.Type[] arguments = paramType.getActualTypeArguments();
            if (rawType instanceof Class && Collection.class.isAssignableFrom((Class<?>)rawType)) {
                final JSONObject object = JSONObject.of("type", (Object)"array");
                final ArraySchema arraySchema = new ArraySchema(object, root);
                if (arguments.length == 1) {
                    arraySchema.itemSchema = of(arguments[0], (root == null) ? arraySchema : root);
                }
                return arraySchema;
            }
            if (rawType instanceof Class && Map.class.isAssignableFrom((Class<?>)rawType)) {
                final JSONObject object = JSONObject.of("type", (Object)"object");
                final ObjectSchema schema = new ObjectSchema(object, root);
                return schema;
            }
        }
        if (type instanceof GenericArrayType) {
            final GenericArrayType arrayType = (GenericArrayType)type;
            final java.lang.reflect.Type componentType = arrayType.getGenericComponentType();
            final JSONObject object2 = JSONObject.of("type", (Object)"array");
            final ArraySchema arraySchema2 = new ArraySchema(object2, root);
            arraySchema2.itemSchema = of(componentType, (root == null) ? arraySchema2 : root);
            return arraySchema2;
        }
        if (type == Byte.TYPE || type == Short.TYPE || type == Integer.TYPE || type == Long.TYPE || type == Byte.class || type == Short.class || type == Integer.class || type == Long.class || type == BigInteger.class || type == AtomicInteger.class || type == AtomicLong.class) {
            return new IntegerSchema(JSONObject.of("type", (Object)"integer"));
        }
        if (type == Float.TYPE || type == Double.TYPE || type == Float.class || type == Double.class || type == BigDecimal.class) {
            return new NumberSchema(JSONObject.of("type", (Object)"number"));
        }
        if (type == Boolean.TYPE || type == Boolean.class || type == AtomicBoolean.class) {
            return new BooleanSchema(JSONObject.of("type", (Object)"boolean"));
        }
        if (type == String.class) {
            return new StringSchema(JSONObject.of("type", (Object)"string"));
        }
        if (type instanceof Class) {
            final Class schemaClass = (Class)type;
            if (Enum.class.isAssignableFrom(schemaClass)) {
                final Object[] enums = schemaClass.getEnumConstants();
                final String[] names = new String[enums.length];
                for (int i = 0; i < enums.length; ++i) {
                    names[i] = ((Enum)enums[i]).name();
                }
                return new StringSchema(JSONObject.of("type", "string", "enum", names));
            }
            if (schemaClass.isArray()) {
                final Class componentType2 = schemaClass.getComponentType();
                final JSONObject object2 = JSONObject.of("type", (Object)"array");
                final ArraySchema arraySchema2 = new ArraySchema(object2, root);
                arraySchema2.itemSchema = of(componentType2, (root == null) ? arraySchema2 : root);
                return arraySchema2;
            }
            if (Map.class.isAssignableFrom(schemaClass)) {
                return new ObjectSchema(JSONObject.of("type", (Object)"object"), root);
            }
            if (Collection.class.isAssignableFrom(schemaClass)) {
                return new ArraySchema(JSONObject.of("type", (Object)"array"), root);
            }
        }
        final ObjectReader reader = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(type);
        if (reader instanceof ObjectReaderBean) {
            final ObjectReaderAdapter adapter = (ObjectReaderAdapter)reader;
            final JSONArray required = new JSONArray();
            final ArrayList<String> list;
            adapter.apply(e -> {
                if (e.fieldClass.isPrimitive()) {
                    list.add(e.fieldName);
                }
                return;
            });
            final JSONObject object = JSONObject.of("type", "object", "required", (Object)required);
            final ObjectSchema schema = new ObjectSchema(object);
            final ObjectSchema objectSchema;
            adapter.apply(e -> objectSchema.properties.put(e.fieldName, of(e.fieldType, (root == null) ? objectSchema : root)));
            return schema;
        }
        throw new JSONException("TODO : " + type);
    }
    
    @JSONCreator
    public static JSONSchema of(final JSONObject input, final JSONSchema parent) {
        final Type type = Type.of(input.getString("type"));
        if (type == null) {
            final Object[] enums = input.getObject("enum", Object[].class, new JSONReader.Feature[0]);
            if (enums != null) {
                boolean nonString = false;
                for (final Object anEnum : enums) {
                    if (!(anEnum instanceof String)) {
                        nonString = true;
                        break;
                    }
                }
                if (!nonString) {
                    return new StringSchema(input);
                }
                return new EnumSchema(enums);
            }
            else {
                final Object constValue = input.get("const");
                if (constValue instanceof String) {
                    return new StringSchema(input);
                }
                if (constValue instanceof Integer || constValue instanceof Long) {
                    return new IntegerSchema(input);
                }
                if (input.size() == 1) {
                    final String ref = input.getString("$ref");
                    if (ref != null && !ref.isEmpty()) {
                        if ("http://json-schema.org/draft-04/schema#".equals(ref)) {
                            JSONSchema schema = JSONSchema.CACHE.get(ref);
                            if (schema == null) {
                                final URL draf4Resource = JSONSchema.class.getClassLoader().getResource("schema/draft-04.json");
                                schema = of(JSON.parseObject(draf4Resource), (JSONSchema)null);
                                final JSONSchema origin = JSONSchema.CACHE.putIfAbsent(ref, schema);
                                if (origin != null) {
                                    schema = origin;
                                }
                            }
                            return schema;
                        }
                        if ("#".equals(ref)) {
                            return parent;
                        }
                        Map<String, JSONSchema> definitions = null;
                        Map<String, JSONSchema> defs = null;
                        Map<String, JSONSchema> properties = null;
                        if (parent instanceof ObjectSchema) {
                            final ObjectSchema objectSchema = (ObjectSchema)parent;
                            definitions = objectSchema.definitions;
                            defs = objectSchema.defs;
                            properties = objectSchema.properties;
                        }
                        else if (parent instanceof ArraySchema) {
                            definitions = ((ArraySchema)parent).definitions;
                            defs = ((ArraySchema)parent).defs;
                        }
                        if (definitions != null && ref.startsWith("#/definitions/")) {
                            final int PREFIX_LEN = 14;
                            final String refName = ref.substring(14);
                            return definitions.get(refName);
                        }
                        if (defs != null && ref.startsWith("#/$defs/")) {
                            final int PREFIX_LEN = 8;
                            String refName = ref.substring(8);
                            refName = URLDecoder.decode(refName);
                            JSONSchema refSchema = defs.get(refName);
                            if (refSchema == null) {
                                refSchema = Any.NOT_ANY;
                            }
                            return refSchema;
                        }
                        if (properties != null && ref.startsWith("#/properties/")) {
                            final int PREFIX_LEN = 13;
                            final String refName = ref.substring(13);
                            return properties.get(refName);
                        }
                        if (ref.startsWith("#/prefixItems/") && parent instanceof ArraySchema) {
                            final int PREFIX_LEN = 14;
                            final int index = Integer.parseInt(ref.substring(14));
                            return ((ArraySchema)parent).prefixItems[index];
                        }
                    }
                    final Object exclusiveMaximum = input.get("exclusiveMaximum");
                    final Object exclusiveMinimum = input.get("exclusiveMinimum");
                    if (exclusiveMaximum instanceof Integer || exclusiveMinimum instanceof Integer || exclusiveMaximum instanceof Long || exclusiveMinimum instanceof Long) {
                        return new IntegerSchema(input);
                    }
                    if (exclusiveMaximum instanceof Number || exclusiveMinimum instanceof Number) {
                        return new NumberSchema(input);
                    }
                }
                if (input.containsKey("properties") || input.containsKey("dependentSchemas") || input.containsKey("if") || input.containsKey("required") || input.containsKey("patternProperties") || input.containsKey("additionalProperties") || input.containsKey("minProperties") || input.containsKey("maxProperties") || input.containsKey("propertyNames") || input.containsKey("$ref")) {
                    return new ObjectSchema(input, parent);
                }
                if (input.containsKey("maxItems") || input.containsKey("minItems") || input.containsKey("additionalItems") || input.containsKey("items") || input.containsKey("prefixItems") || input.containsKey("uniqueItems") || input.containsKey("maxContains") || input.containsKey("minContains")) {
                    return new ArraySchema(input, parent);
                }
                if (input.containsKey("pattern") || input.containsKey("format") || input.containsKey("minLength") || input.containsKey("maxLength")) {
                    return new StringSchema(input);
                }
                final boolean allOf = input.containsKey("allOf");
                final boolean anyOf = input.containsKey("anyOf");
                final boolean oneOf = input.containsKey("oneOf");
                if (allOf || anyOf || oneOf) {
                    final int count = (allOf + anyOf + oneOf) ? 1 : 0;
                    if (count != 1) {
                        final JSONSchema[] items = new JSONSchema[count];
                        int index = 0;
                        if (allOf) {
                            items[index++] = new AllOf(input, parent);
                        }
                        if (anyOf) {
                            items[index++] = new AnyOf(input, parent);
                        }
                        if (oneOf) {
                            items[index++] = new OneOf(input, parent);
                        }
                        return new AllOf(items);
                    }
                    if (allOf) {
                        return new AllOf(input, parent);
                    }
                    if (anyOf) {
                        return new AnyOf(input, parent);
                    }
                    return new OneOf(input, parent);
                }
                else {
                    if (input.containsKey("not")) {
                        return ofNot(input, null);
                    }
                    if (input.get("maximum") instanceof Number || input.get("minimum") instanceof Number || input.containsKey("multipleOf")) {
                        return new NumberSchema(input);
                    }
                    if (input.isEmpty()) {
                        return Any.INSTANCE;
                    }
                    if (input.size() == 1) {
                        final Object propertyType = input.get("type");
                        if (propertyType instanceof JSONArray) {
                            final JSONArray array = (JSONArray)propertyType;
                            final JSONSchema[] typeSchemas = new JSONSchema[array.size()];
                            for (int i = 0; i < array.size(); ++i) {
                                final Type itemType = Type.of(array.getString(i));
                                switch (itemType) {
                                    case String: {
                                        typeSchemas[i] = new StringSchema(JSONObject.of("type", (Object)"string"));
                                        break;
                                    }
                                    case Integer: {
                                        typeSchemas[i] = new IntegerSchema(JSONObject.of("type", (Object)"integer"));
                                        break;
                                    }
                                    case Number: {
                                        typeSchemas[i] = new NumberSchema(JSONObject.of("type", (Object)"number"));
                                        break;
                                    }
                                    case Boolean: {
                                        typeSchemas[i] = new BooleanSchema(JSONObject.of("type", (Object)"boolean"));
                                        break;
                                    }
                                    case Null: {
                                        typeSchemas[i] = new NullSchema(JSONObject.of("type", (Object)"null"));
                                        break;
                                    }
                                    case Object: {
                                        typeSchemas[i] = new ObjectSchema(JSONObject.of("type", (Object)"object"));
                                        break;
                                    }
                                    case Array: {
                                        typeSchemas[i] = new ArraySchema(JSONObject.of("type", (Object)"array"), null);
                                        break;
                                    }
                                    default: {
                                        throw new JSONSchemaValidException("not support type : " + itemType);
                                    }
                                }
                            }
                            return new AnyOf(typeSchemas);
                        }
                    }
                    if (input.getString("type") == null) {
                        throw new JSONSchemaValidException("type required");
                    }
                    throw new JSONSchemaValidException("not support type : " + input.getString("type"));
                }
            }
        }
        else {
            switch (type) {
                case String: {
                    return new StringSchema(input);
                }
                case Integer: {
                    return new IntegerSchema(input);
                }
                case Number: {
                    return new NumberSchema(input);
                }
                case Boolean: {
                    return new BooleanSchema(input);
                }
                case Null: {
                    return new NullSchema(input);
                }
                case Object: {
                    return new ObjectSchema(input, parent);
                }
                case Array: {
                    return new ArraySchema(input, parent);
                }
                default: {
                    throw new JSONSchemaValidException("not support type : " + type);
                }
            }
        }
    }
    
    static AnyOf anyOf(final JSONObject input, final Class type) {
        final JSONArray array = input.getJSONArray("anyOf");
        if (array == null || array.isEmpty()) {
            return null;
        }
        final JSONSchema[] items = new JSONSchema[array.size()];
        for (int i = 0; i < items.length; ++i) {
            items[i] = of(array.getJSONObject(i), type);
        }
        return new AnyOf(items);
    }
    
    static AnyOf anyOf(final JSONArray array, final Class type) {
        if (array == null || array.isEmpty()) {
            return null;
        }
        final JSONSchema[] items = new JSONSchema[array.size()];
        for (int i = 0; i < items.length; ++i) {
            items[i] = of(array.getJSONObject(i), type);
        }
        return new AnyOf(items);
    }
    
    static AllOf allOf(final JSONObject input, final Class type) {
        final JSONArray array = input.getJSONArray("allOf");
        if (array == null || array.isEmpty()) {
            return null;
        }
        final JSONSchema[] items = new JSONSchema[array.size()];
        for (int i = 0; i < items.length; ++i) {
            items[i] = of(array.getJSONObject(i), type);
        }
        return new AllOf(items);
    }
    
    static OneOf oneOf(final JSONObject input, final Class type) {
        final JSONArray array = input.getJSONArray("oneOf");
        if (array == null || array.isEmpty()) {
            return null;
        }
        final JSONSchema[] items = new JSONSchema[array.size()];
        for (int i = 0; i < items.length; ++i) {
            items[i] = of(array.getJSONObject(i), type);
        }
        return new OneOf(items);
    }
    
    static OneOf oneOf(final JSONArray array, final Class type) {
        if (array == null || array.isEmpty()) {
            return null;
        }
        final JSONSchema[] items = new JSONSchema[array.size()];
        for (int i = 0; i < items.length; ++i) {
            items[i] = of(array.getJSONObject(i), type);
        }
        return new OneOf(items);
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public abstract Type getType();
    
    public abstract ValidateResult validate(final Object p0);
    
    public boolean isValid(final Object value) {
        return this.validate(value).isSuccess();
    }
    
    public boolean isValid(final long value) {
        return this.validate(value).isSuccess();
    }
    
    public boolean isValid(final double value) {
        return this.validate(value).isSuccess();
    }
    
    public boolean isValid(final Double value) {
        return this.validate(value).isSuccess();
    }
    
    public boolean isValid(final float value) {
        return this.validate(value).isSuccess();
    }
    
    public boolean isValid(final Float value) {
        return this.validate(value).isSuccess();
    }
    
    public boolean isValid(final Integer value) {
        return this.validate(value).isSuccess();
    }
    
    public boolean isValid(final Long value) {
        return this.validate(value).isSuccess();
    }
    
    public ValidateResult validate(final long value) {
        return this.validate((Object)value);
    }
    
    public ValidateResult validate(final double value) {
        return this.validate((Object)value);
    }
    
    public ValidateResult validate(final Float value) {
        return this.validate((Object)value);
    }
    
    public ValidateResult validate(final Double value) {
        return this.validate((Object)value);
    }
    
    public ValidateResult validate(final Integer value) {
        return this.validate((Object)value);
    }
    
    public ValidateResult validate(final Long value) {
        return this.validate((Object)value);
    }
    
    public void assertValidate(final Object value) {
        final ValidateResult result = this.validate(value);
        if (result.isSuccess()) {
            return;
        }
        throw new JSONSchemaValidException(result.getMessage());
    }
    
    public void assertValidate(final Integer value) {
        final ValidateResult result = this.validate(value);
        if (result.isSuccess()) {
            return;
        }
        throw new JSONSchemaValidException(result.getMessage());
    }
    
    public void assertValidate(final Long value) {
        final ValidateResult result = this.validate(value);
        if (result.isSuccess()) {
            return;
        }
        throw new JSONSchemaValidException(result.getMessage());
    }
    
    public void assertValidate(final Double value) {
        final ValidateResult result = this.validate(value);
        if (result.isSuccess()) {
            return;
        }
        throw new JSONSchemaValidException(result.getMessage());
    }
    
    public void assertValidate(final Float value) {
        final ValidateResult result = this.validate(value);
        if (result.isSuccess()) {
            return;
        }
        throw new JSONSchemaValidException(result.getMessage());
    }
    
    public void assertValidate(final long value) {
        final ValidateResult result = this.validate(value);
        if (result.isSuccess()) {
            return;
        }
        throw new JSONSchemaValidException(result.getMessage());
    }
    
    public void assertValidate(final double value) {
        final ValidateResult result = this.validate(value);
        if (result.isSuccess()) {
            return;
        }
        throw new JSONSchemaValidException(result.getMessage());
    }
    
    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final JSONSchema that = (JSONSchema)o;
        final JSONObject thisObj = this.toJSONObject();
        final JSONObject thatObj = that.toJSONObject();
        return thisObj.equals(thatObj);
    }
    
    @Override
    public int hashCode() {
        return this.toJSONObject().hashCode();
    }
    
    public JSONObject toJSONObject() {
        return new JSONObject();
    }
    
    public void accept(final Predicate<JSONSchema> v) {
        v.test(this);
    }
    
    static {
        CACHE = new ConcurrentHashMap<String, JSONSchema>();
        CONTEXT = JSONFactory.createReadContext();
        SUCCESS = new ValidateResult(true, "success", new Object[0]);
        FAIL_INPUT_NULL = new ValidateResult(false, "input null", new Object[0]);
        FAIL_ANY_OF = new ValidateResult(false, "anyOf fail", new Object[0]);
        FAIL_ONE_OF = new ValidateResult(false, "oneOf fail", new Object[0]);
        FAIL_NOT = new ValidateResult(false, "not fail", new Object[0]);
        FAIL_TYPE_NOT_MATCH = new ValidateResult(false, "type not match", new Object[0]);
        FAIL_PROPERTY_NAME = new ValidateResult(false, "propertyName not match", new Object[0]);
        CONTAINS_NOT_MATCH = new ValidateResult(false, "contains not match", new Object[0]);
        UNIQUE_ITEMS_NOT_MATCH = new ValidateResult(false, "uniqueItems not match", new Object[0]);
        REQUIRED_NOT_MATCH = new ValidateResult(false, "required", new Object[0]);
    }
    
    public enum Type
    {
        Null, 
        Boolean, 
        Object, 
        Array, 
        Number, 
        String, 
        Integer, 
        Enum, 
        Const, 
        OneOf, 
        AllOf, 
        AnyOf, 
        Any;
        
        public static Type of(final String typeStr) {
            if (typeStr == null) {
                return null;
            }
            switch (typeStr) {
                case "Null":
                case "null": {
                    return Type.Null;
                }
                case "String":
                case "string": {
                    return Type.String;
                }
                case "Integer":
                case "integer": {
                    return Type.Integer;
                }
                case "Number":
                case "number": {
                    return Type.Number;
                }
                case "Boolean":
                case "boolean": {
                    return Type.Boolean;
                }
                case "Object":
                case "object": {
                    return Type.Object;
                }
                case "Array":
                case "array": {
                    return Type.Array;
                }
                default: {
                    return null;
                }
            }
        }
        
        private static /* synthetic */ Type[] $values() {
            return new Type[] { Type.Null, Type.Boolean, Type.Object, Type.Array, Type.Number, Type.String, Type.Integer, Type.Enum, Type.Const, Type.OneOf, Type.AllOf, Type.AnyOf, Type.Any };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    static class JSONSchemaWriter implements ObjectWriter
    {
        @Override
        public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final java.lang.reflect.Type fieldType, final long features) {
            final JSONObject jsonObject = ((JSONSchema)object).toJSONObject();
            jsonWriter.write(jsonObject);
        }
    }
}
