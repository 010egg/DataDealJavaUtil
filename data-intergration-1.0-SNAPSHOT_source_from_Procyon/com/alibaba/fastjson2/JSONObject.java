// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.filter.ValueFilter;
import java.util.Iterator;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import java.util.Objects;
import com.alibaba.fastjson2.annotation.JSONField;
import java.lang.reflect.AnnotatedElement;
import com.alibaba.fastjson2.util.BeanUtils;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import com.alibaba.fastjson2.util.JDKUtils;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.reader.ObjectReaderImplEnum;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import java.util.function.Function;
import java.time.Instant;
import com.alibaba.fastjson2.util.TypeUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Base64;
import java.time.temporal.TemporalAccessor;
import com.alibaba.fastjson2.util.DateUtils;
import java.util.Date;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.util.List;
import java.lang.reflect.Array;
import java.util.Collection;
import java.lang.reflect.Type;
import java.util.UUID;
import java.util.Map;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.InvocationHandler;
import java.util.LinkedHashMap;

public class JSONObject extends LinkedHashMap<String, Object> implements InvocationHandler
{
    private static final long serialVersionUID = 1L;
    static ObjectReader<JSONArray> arrayReader;
    static final long NONE_DIRECT_FEATURES;
    
    public JSONObject() {
    }
    
    public JSONObject(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public JSONObject(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
    }
    
    public JSONObject(final int initialCapacity, final float loadFactor, final boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }
    
    public JSONObject(final Map map) {
        super(map);
    }
    
    public Object get(final String key) {
        return super.get(key);
    }
    
    @Override
    public Object get(final Object key) {
        if (key instanceof Number || key instanceof Character || key instanceof Boolean || key instanceof UUID) {
            final Object value = super.get(key.toString());
            if (value != null) {
                return value;
            }
        }
        return super.get(key);
    }
    
    public Object getByPath(final String jsonPath) {
        final JSONPath path = JSONPath.of(jsonPath);
        if (path instanceof JSONPathSingleName) {
            final String name = ((JSONPathSingleName)path).name;
            return this.get(name);
        }
        return path.eval(this);
    }
    
    public boolean containsKey(final String key) {
        return super.containsKey(key);
    }
    
    @Override
    public boolean containsKey(final Object key) {
        if (key instanceof Number || key instanceof Character || key instanceof Boolean || key instanceof UUID) {
            return super.containsKey(key) || super.containsKey(key.toString());
        }
        return super.containsKey(key);
    }
    
    public Object getOrDefault(final String key, final Object defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }
    
    @Override
    public Object getOrDefault(final Object key, final Object defaultValue) {
        if (key instanceof Number || key instanceof Character || key instanceof Boolean || key instanceof UUID) {
            return super.getOrDefault(key.toString(), defaultValue);
        }
        return super.getOrDefault(key, defaultValue);
    }
    
    public JSONArray getJSONArray(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof JSONArray) {
            return (JSONArray)value;
        }
        if (value instanceof JSONObject) {
            return JSONArray.of(value);
        }
        if (value instanceof String) {
            final String str = (String)value;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            final JSONReader reader = JSONReader.of(str);
            if (JSONObject.arrayReader == null) {
                JSONObject.arrayReader = (ObjectReader<JSONArray>)reader.getObjectReader(JSONArray.class);
            }
            return JSONObject.arrayReader.readObject(reader, null, null, 0L);
        }
        else {
            if (value instanceof Collection) {
                final JSONArray array = new JSONArray((Collection<?>)value);
                ((HashMap<String, JSONArray>)this).put(key, array);
                return array;
            }
            if (value instanceof Object[]) {
                return JSONArray.of((Object[])value);
            }
            final Class<?> valueClass = value.getClass();
            if (valueClass.isArray()) {
                final int length = Array.getLength(value);
                final JSONArray jsonArray = new JSONArray(length);
                for (int i = 0; i < length; ++i) {
                    final Object item = Array.get(value, i);
                    jsonArray.add(item);
                }
                return jsonArray;
            }
            return null;
        }
    }
    
    public <T> List<T> getList(final String key, final Class<T> itemClass, final JSONReader.Feature... features) {
        final JSONArray jsonArray = this.getJSONArray(key);
        if (jsonArray == null) {
            return null;
        }
        return jsonArray.toList(itemClass, features);
    }
    
    public JSONObject getJSONObject(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof JSONObject) {
            return (JSONObject)value;
        }
        if (value instanceof String) {
            final String str = (String)value;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            final JSONReader reader = JSONReader.of(str);
            return JSONFactory.OBJECT_READER.readObject(reader, null, null, 0L);
        }
        else {
            if (value instanceof Map) {
                final JSONObject object = new JSONObject((Map)value);
                ((HashMap<String, JSONObject>)this).put(key, object);
                return object;
            }
            final Class valueClass = value.getClass();
            final ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter(valueClass);
            if (objectWriter instanceof ObjectWriterAdapter) {
                final ObjectWriterAdapter writerAdapter = (ObjectWriterAdapter)objectWriter;
                return writerAdapter.toJSONObject(value);
            }
            return null;
        }
    }
    
    public String getString(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String)value;
        }
        if (value instanceof Date) {
            final long timeMillis = ((Date)value).getTime();
            return DateUtils.toString(timeMillis, false, DateUtils.DEFAULT_ZONE_ID);
        }
        if (value instanceof Boolean || value instanceof Character || value instanceof Number || value instanceof UUID || value instanceof Enum || value instanceof TemporalAccessor) {
            return value.toString();
        }
        return JSON.toJSONString(value);
    }
    
    public Double getDouble(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Double) {
            return (Double)value;
        }
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to Double");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return null;
        }
        return Double.parseDouble(str);
    }
    
    public double getDoubleValue(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return 0.0;
        }
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to double value");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return 0.0;
        }
        return Double.parseDouble(str);
    }
    
    public Float getFloat(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Float) {
            return (Float)value;
        }
        if (value instanceof Number) {
            return ((Number)value).floatValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to Float");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return null;
        }
        return Float.parseFloat(str);
    }
    
    public float getFloatValue(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return 0.0f;
        }
        if (value instanceof Number) {
            return ((Number)value).floatValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to float value");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return 0.0f;
        }
        return Float.parseFloat(str);
    }
    
    public Long getLong(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long)value;
        }
        if (value instanceof Number) {
            return ((Number)value).longValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to Long");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return null;
        }
        if (str.indexOf(46) != -1) {
            return (long)Double.parseDouble(str);
        }
        return Long.parseLong(str);
    }
    
    public long getLongValue(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number)value).longValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to long value");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return 0L;
        }
        if (str.indexOf(46) != -1) {
            return (long)Double.parseDouble(str);
        }
        return Long.parseLong(str);
    }
    
    public long getLongValue(final String key, final long defaultValue) {
        final Object value = super.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number)value).longValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to long value");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return defaultValue;
        }
        if (str.indexOf(46) != -1) {
            return (long)Double.parseDouble(str);
        }
        return Long.parseLong(str);
    }
    
    public Integer getInteger(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer)value;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to Integer");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return null;
        }
        if (str.indexOf(46) != -1) {
            return (int)Double.parseDouble(str);
        }
        return Integer.parseInt(str);
    }
    
    public int getIntValue(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to int value");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return 0;
        }
        if (str.indexOf(46) != -1) {
            return (int)Double.parseDouble(str);
        }
        return Integer.parseInt(str);
    }
    
    public int getIntValue(final String key, final int defaultValue) {
        final Object value = super.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to int value");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return defaultValue;
        }
        if (str.indexOf(46) != -1) {
            return (int)Double.parseDouble(str);
        }
        return Integer.parseInt(str);
    }
    
    public Short getShort(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Short) {
            return (Short)value;
        }
        if (value instanceof Number) {
            return ((Number)value).shortValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to Short");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return null;
        }
        return Short.parseShort(str);
    }
    
    public short getShortValue(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number)value).shortValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to short value");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return 0;
        }
        return Short.parseShort(str);
    }
    
    public Byte getByte(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number)value).byteValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to Byte");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return null;
        }
        return Byte.parseByte(str);
    }
    
    public byte getByteValue(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number)value).byteValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to byte value");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return 0;
        }
        return Byte.parseByte(str);
    }
    
    public byte[] getBytes(final String key) {
        final Object value = this.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof byte[]) {
            return (byte[])value;
        }
        if (value instanceof String) {
            return Base64.getDecoder().decode((String)value);
        }
        throw new JSONException("can not cast to byte[], value : " + value);
    }
    
    public Boolean getBoolean(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean)value;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue() == 1;
        }
        if (!(value instanceof String)) {
            throw new JSONException("Can not cast '" + value.getClass() + "' to Boolean");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return null;
        }
        return "true".equalsIgnoreCase(str) || "1".equals(str);
    }
    
    public boolean getBooleanValue(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (boolean)value;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue() == 1;
        }
        if (value instanceof String) {
            final String str = (String)value;
            return "true".equalsIgnoreCase(str) || "1".equals(str);
        }
        throw new JSONException("Can not cast '" + value.getClass() + "' to boolean value");
    }
    
    public boolean getBooleanValue(final String key, final boolean defaultValue) {
        final Object value = super.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean) {
            return (boolean)value;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue() == 1;
        }
        if (value instanceof String) {
            final String str = (String)value;
            return "true".equalsIgnoreCase(str) || "1".equals(str);
        }
        throw new JSONException("Can not cast '" + value.getClass() + "' to boolean value");
    }
    
    public BigInteger getBigInteger(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof BigInteger) {
            return (BigInteger)value;
        }
        if (value instanceof Number) {
            if (value instanceof BigDecimal) {
                return ((BigDecimal)value).toBigInteger();
            }
            final long longValue = ((Number)value).longValue();
            return BigInteger.valueOf(longValue);
        }
        else {
            if (!(value instanceof String)) {
                throw new JSONException("Can not cast '" + value.getClass() + "' to BigInteger");
            }
            final String str = (String)value;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            return new BigInteger(str);
        }
    }
    
    public BigDecimal getBigDecimal(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            if (value instanceof BigDecimal) {
                return (BigDecimal)value;
            }
            if (value instanceof BigInteger) {
                return new BigDecimal((BigInteger)value);
            }
            if (value instanceof Float) {
                final float floatValue = (float)value;
                return TypeUtils.toBigDecimal(floatValue);
            }
            if (value instanceof Double) {
                final double doubleValue = (double)value;
                return TypeUtils.toBigDecimal(doubleValue);
            }
            final long longValue = ((Number)value).longValue();
            return BigDecimal.valueOf(longValue);
        }
        else {
            if (value instanceof String) {
                final String str = (String)value;
                return TypeUtils.toBigDecimal(str);
            }
            if (value instanceof Boolean) {
                return value ? BigDecimal.ONE : BigDecimal.ZERO;
            }
            throw new JSONException("Can not cast '" + value.getClass() + "' to BigDecimal");
        }
    }
    
    public Date getDate(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return (Date)value;
        }
        if (value instanceof String) {
            return DateUtils.parseDate((String)value);
        }
        if (value instanceof Number) {
            final long millis = ((Number)value).longValue();
            return new Date(millis);
        }
        return TypeUtils.toDate(value);
    }
    
    public Date getDate(final String key, final Date defaultValue) {
        Date date = this.getDate(key);
        if (date == null) {
            date = defaultValue;
        }
        return date;
    }
    
    public Instant getInstant(final String key) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Instant) {
            return (Instant)value;
        }
        if (!(value instanceof Number)) {
            return TypeUtils.toInstant(value);
        }
        final long millis = ((Number)value).longValue();
        if (millis == 0L) {
            return null;
        }
        return Instant.ofEpochMilli(millis);
    }
    
    @Override
    public String toString() {
        final JSONWriter writer = JSONWriter.of();
        try {
            writer.setRootObject(this);
            writer.write(this);
            final String string = writer.toString();
            if (writer != null) {
                writer.close();
            }
            return string;
        }
        catch (Throwable t) {
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    public String toString(final JSONWriter.Feature... features) {
        final JSONWriter writer = JSONWriter.of(features);
        try {
            writer.setRootObject(this);
            writer.write(this);
            final String string = writer.toString();
            if (writer != null) {
                writer.close();
            }
            return string;
        }
        catch (Throwable t) {
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    public String toJSONString(final JSONWriter.Feature... features) {
        return this.toString(features);
    }
    
    public static String toJSONString(final Object object, final JSONWriter.Feature... features) {
        return JSON.toJSONString(object, features);
    }
    
    public byte[] toJSONBBytes(final JSONWriter.Feature... features) {
        final JSONWriter writer = JSONWriter.ofJSONB(features);
        try {
            writer.setRootObject(this);
            writer.write(this);
            final byte[] bytes = writer.getBytes();
            if (writer != null) {
                writer.close();
            }
            return bytes;
        }
        catch (Throwable t) {
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    public <T> T to(final Function<JSONObject, T> function) {
        return function.apply(this);
    }
    
    public <T> T to(final Type type, final JSONReader.Feature... features) {
        long featuresValue = 0L;
        boolean fieldBased = false;
        for (final JSONReader.Feature feature : features) {
            if (feature == JSONReader.Feature.FieldBased) {
                fieldBased = true;
            }
            featuresValue |= feature.mask;
        }
        if (type == String.class) {
            return (T)this.toString();
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(type, fieldBased);
        return objectReader.createInstance(this, featuresValue);
    }
    
    public <T> T to(final TypeReference<T> typeReference, final JSONReader.Feature... features) {
        return this.to(typeReference.getType(), features);
    }
    
    public <T> T to(final Class<T> clazz, final JSONReader.Feature... features) {
        long featuresValue = 0L;
        boolean fieldBased = false;
        for (final JSONReader.Feature feature : features) {
            if (feature == JSONReader.Feature.FieldBased) {
                fieldBased = true;
            }
            featuresValue |= feature.mask;
        }
        if (clazz == String.class) {
            return (T)this.toString();
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(clazz, fieldBased);
        return objectReader.createInstance(this, featuresValue);
    }
    
    public <T> T toJavaObject(final Class<T> clazz, final JSONReader.Feature... features) {
        return (T)this.to((Class<Object>)clazz, features);
    }
    
    @Deprecated
    public <T> T toJavaObject(final Type type, final JSONReader.Feature... features) {
        return (T)this.to(type, features);
    }
    
    @Deprecated
    public <T> T toJavaObject(final TypeReference<T> typeReference, final JSONReader.Feature... features) {
        return (T)this.to((TypeReference<Object>)typeReference, features);
    }
    
    public <T> T getObject(final String key, final Class<T> type, final JSONReader.Feature... features) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (type == Object.class && features.length == 0) {
            return (T)value;
        }
        boolean fieldBased = false;
        for (final JSONReader.Feature feature : features) {
            if (feature == JSONReader.Feature.FieldBased) {
                fieldBased = true;
                break;
            }
        }
        final Class<?> valueClass = value.getClass();
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final Function typeConvert = provider.getTypeConvert(valueClass, type);
        if (typeConvert != null) {
            return typeConvert.apply(value);
        }
        if (value instanceof Map) {
            final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(type, fieldBased);
            return objectReader.createInstance((Map)value, features);
        }
        if (value instanceof Collection) {
            final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(type, fieldBased);
            return objectReader.createInstance((Collection)value);
        }
        final Class clazz = TypeUtils.getMapping(type);
        if (clazz.isInstance(value)) {
            return (T)value;
        }
        ObjectReader objectReader2 = null;
        if (value instanceof String) {
            final String str = (String)value;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            if (clazz.isEnum()) {
                objectReader2 = provider.getObjectReader(clazz, fieldBased);
                if (objectReader2 instanceof ObjectReaderImplEnum) {
                    final long hashCode64 = Fnv.hashCode64(str);
                    final ObjectReaderImplEnum enumReader = (ObjectReaderImplEnum)objectReader2;
                    return (T)enumReader.getEnumByHashCode(hashCode64);
                }
            }
        }
        final String json = JSON.toJSONString(value);
        final JSONReader jsonReader = JSONReader.of(json);
        jsonReader.context.config(features);
        if (objectReader2 == null) {
            objectReader2 = provider.getObjectReader(clazz, fieldBased);
        }
        final T object = objectReader2.readObject(jsonReader, null, null, 0L);
        if (!jsonReader.isEnd()) {
            throw new JSONException("not support input " + json);
        }
        return object;
    }
    
    public <T> T getObject(final String key, final Type type, final JSONReader.Feature... features) {
        final Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (type == Object.class && features.length == 0) {
            return (T)value;
        }
        boolean fieldBased = false;
        for (final JSONReader.Feature feature : features) {
            if (feature == JSONReader.Feature.FieldBased) {
                fieldBased = true;
                break;
            }
        }
        final Class<?> valueClass = value.getClass();
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final Function typeConvert = provider.getTypeConvert(valueClass, type);
        if (typeConvert != null) {
            return typeConvert.apply(value);
        }
        if (value instanceof Map) {
            final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(type, fieldBased);
            return objectReader.createInstance((Map)value, features);
        }
        if (value instanceof Collection) {
            final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(type, fieldBased);
            return objectReader.createInstance((Collection)value);
        }
        if (type instanceof Class) {
            final Class clazz = (Class)type;
            if (clazz.isInstance(value)) {
                return (T)value;
            }
        }
        if (value instanceof String) {
            final String str = (String)value;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
        }
        final String json = JSON.toJSONString(value);
        final JSONReader jsonReader = JSONReader.of(json);
        jsonReader.context.config(features);
        final ObjectReader objectReader2 = provider.getObjectReader(type, fieldBased);
        return objectReader2.readObject(jsonReader, null, null, 0L);
    }
    
    public <T> T getObject(final String key, final TypeReference<T> typeReference, final JSONReader.Feature... features) {
        return this.getObject(key, typeReference.type, features);
    }
    
    public <T> T getObject(final String key, final Function<JSONObject, T> creator) {
        final JSONObject object = this.getJSONObject(key);
        if (object == null) {
            return null;
        }
        return creator.apply(object);
    }
    
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final String methodName = method.getName();
        final int parameterCount = method.getParameterCount();
        final Class<?> returnType = method.getReturnType();
        if (parameterCount == 1) {
            if ("equals".equals(methodName)) {
                return this.equals(args[0]);
            }
            Class proxyInterface = null;
            final Class<?>[] interfaces = proxy.getClass().getInterfaces();
            if (interfaces.length == 1) {
                proxyInterface = interfaces[0];
            }
            if (returnType != Void.TYPE && returnType != proxyInterface) {
                throw new JSONException("This method '" + methodName + "' is not a setter");
            }
            String name = this.getJSONFieldName(method);
            if (name == null) {
                name = methodName;
                if (!name.startsWith("set")) {
                    throw new JSONException("This method '" + methodName + "' is not a setter");
                }
                name = name.substring(3);
                if (name.length() == 0) {
                    throw new JSONException("This method '" + methodName + "' is an illegal setter");
                }
                name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
            }
            this.put(name, args[0]);
            if (returnType != Void.TYPE) {
                return proxy;
            }
            return null;
        }
        else {
            if (parameterCount != 0) {
                throw new UnsupportedOperationException(method.toGenericString());
            }
            if (returnType == Void.TYPE) {
                throw new JSONException("This method '" + methodName + "' is not a getter");
            }
            String name2 = this.getJSONFieldName(method);
            Object value;
            if (name2 == null) {
                name2 = methodName;
                boolean with = false;
                final int prefix;
                if ((name2.startsWith("get") || (with = name2.startsWith("with"))) && name2.length() > (prefix = (with ? 4 : 3))) {
                    final char[] chars = new char[name2.length() - prefix];
                    name2.getChars(prefix, name2.length(), chars, 0);
                    if (chars[0] >= 'A' && chars[0] <= 'Z') {
                        chars[0] += ' ';
                    }
                    final String fieldName = new String(chars);
                    if (fieldName.isEmpty()) {
                        throw new JSONException("This method '" + methodName + "' is an illegal getter");
                    }
                    value = this.get(fieldName);
                    if (value == null) {
                        return null;
                    }
                }
                else if (name2.startsWith("is")) {
                    if ("isEmpty".equals(name2)) {
                        value = this.get("empty");
                        if (value == null) {
                            return this.isEmpty();
                        }
                    }
                    else {
                        name2 = name2.substring(2);
                        if (name2.isEmpty()) {
                            throw new JSONException("This method '" + methodName + "' is an illegal getter");
                        }
                        name2 = Character.toLowerCase(name2.charAt(0)) + name2.substring(1);
                        value = this.get(name2);
                        if (value == null) {
                            return false;
                        }
                    }
                }
                else {
                    if ("hashCode".equals(name2)) {
                        return this.hashCode();
                    }
                    if ("toString".equals(name2)) {
                        return this.toString();
                    }
                    if (name2.startsWith("entrySet")) {
                        return this.entrySet();
                    }
                    if ("size".equals(name2)) {
                        return this.size();
                    }
                    final Class<?> declaringClass = method.getDeclaringClass();
                    if (declaringClass.isInterface() && !Modifier.isAbstract(method.getModifiers()) && !JDKUtils.ANDROID && !JDKUtils.GRAAL) {
                        final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
                        final MethodHandle methodHandle = lookup.findSpecial(declaringClass, method.getName(), MethodType.methodType(returnType), declaringClass);
                        return methodHandle.invoke(proxy);
                    }
                    throw new JSONException("This method '" + methodName + "' is not a getter");
                }
            }
            else {
                value = this.get(name2);
                if (value == null) {
                    return null;
                }
            }
            if (!returnType.isInstance(value)) {
                final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(value.getClass(), method.getGenericReturnType());
                if (typeConvert != null) {
                    value = typeConvert.apply(value);
                }
            }
            return value;
        }
    }
    
    private String getJSONFieldName(final Method method) {
        String name = null;
        final Annotation[] annotations2;
        final Annotation[] annotations = annotations2 = BeanUtils.getAnnotations(method);
        for (final Annotation annotation : annotations2) {
            final Class<? extends Annotation> annotationType = annotation.annotationType();
            final JSONField jsonField = BeanUtils.findAnnotation(annotation, JSONField.class);
            if (Objects.nonNull(jsonField)) {
                name = jsonField.name();
                if (name.isEmpty()) {
                    name = null;
                }
            }
            else if ("com.alibaba.fastjson.annotation.JSONField".equals(annotationType.getName())) {
                final NameConsumer nameConsumer = new NameConsumer(annotation);
                BeanUtils.annotationMethods(annotationType, nameConsumer);
                if (nameConsumer.name != null) {
                    name = nameConsumer.name;
                }
            }
        }
        return name;
    }
    
    public JSONArray putArray(final String name) {
        final JSONArray array = new JSONArray();
        ((HashMap<String, JSONArray>)this).put(name, array);
        return array;
    }
    
    public JSONObject putObject(final String name) {
        final JSONObject object = new JSONObject();
        ((HashMap<String, JSONObject>)this).put(name, object);
        return object;
    }
    
    public JSONObject fluentPut(final String key, final Object value) {
        this.put(key, value);
        return this;
    }
    
    public boolean isValid(final JSONSchema schema) {
        return schema.isValid(this);
    }
    
    static void nameFilter(final Iterable<?> iterable, final NameFilter nameFilter) {
        for (final Object item : iterable) {
            if (item instanceof JSONObject) {
                ((JSONObject)item).nameFilter(nameFilter);
            }
            else {
                if (!(item instanceof Iterable)) {
                    continue;
                }
                nameFilter((Iterable<?>)item, nameFilter);
            }
        }
    }
    
    static void nameFilter(final Map map, final NameFilter nameFilter) {
        JSONObject changed = null;
        final Iterator<?> it = (Iterator<?>)map.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry entry = (Map.Entry)it.next();
            final Object entryKey = entry.getKey();
            final Object entryValue = entry.getValue();
            if (entryValue instanceof JSONObject) {
                ((JSONObject)entryValue).nameFilter(nameFilter);
            }
            else if (entryValue instanceof Iterable) {
                nameFilter((Iterable<?>)entryValue, nameFilter);
            }
            if (entryKey instanceof String) {
                final String key = (String)entryKey;
                final String processName = nameFilter.process(map, key, entryValue);
                if (processName == null || processName.equals(key)) {
                    continue;
                }
                if (changed == null) {
                    changed = new JSONObject();
                }
                changed.put(processName, entryValue);
                it.remove();
            }
        }
        if (changed != null) {
            map.putAll(changed);
        }
    }
    
    static void valueFilter(final Iterable<?> iterable, final ValueFilter valueFilter) {
        for (final Object item : iterable) {
            if (item instanceof Map) {
                valueFilter((Map)item, valueFilter);
            }
            else {
                if (!(item instanceof Iterable)) {
                    continue;
                }
                valueFilter((Iterable<?>)item, valueFilter);
            }
        }
    }
    
    static void valueFilter(final Map map, final ValueFilter valueFilter) {
        for (final Object o : map.entrySet()) {
            final Map.Entry entry = (Map.Entry)o;
            final Object entryKey = entry.getKey();
            final Object entryValue = entry.getValue();
            if (entryValue instanceof Map) {
                valueFilter((Map)entryValue, valueFilter);
            }
            else if (entryValue instanceof Iterable) {
                valueFilter((Iterable<?>)entryValue, valueFilter);
            }
            if (entryKey instanceof String) {
                final String key = (String)entryKey;
                final Object applyValue = valueFilter.apply(map, key, entryValue);
                if (applyValue == entryValue) {
                    continue;
                }
                entry.setValue(applyValue);
            }
        }
    }
    
    public void valueFilter(final ValueFilter valueFilter) {
        valueFilter(this, valueFilter);
    }
    
    public void nameFilter(final NameFilter nameFilter) {
        nameFilter(this, nameFilter);
    }
    
    @Override
    public JSONObject clone() {
        return new JSONObject(this);
    }
    
    public Object eval(final JSONPath path) {
        return path.eval(this);
    }
    
    public int getSize(final String key) {
        final Object value = this.get(key);
        if (value instanceof Map) {
            return ((Map)value).size();
        }
        if (value instanceof Collection) {
            return ((Collection)value).size();
        }
        return 0;
    }
    
    public static JSONObject of() {
        return new JSONObject();
    }
    
    public static JSONObject of(final String key, final Object value) {
        final JSONObject object = new JSONObject(1);
        object.put(key, value);
        return object;
    }
    
    public static JSONObject of(final String k1, final Object v1, final String k2, final Object v2) {
        final JSONObject object = new JSONObject(2);
        object.put(k1, v1);
        object.put(k2, v2);
        return object;
    }
    
    public static JSONObject of(final String k1, final Object v1, final String k2, final Object v2, final String k3, final Object v3) {
        final JSONObject object = new JSONObject(3);
        object.put(k1, v1);
        object.put(k2, v2);
        object.put(k3, v3);
        return object;
    }
    
    public static JSONObject of(final String k1, final Object v1, final String k2, final Object v2, final String k3, final Object v3, final String k4, final Object v4) {
        final JSONObject object = new JSONObject(4);
        object.put(k1, v1);
        object.put(k2, v2);
        object.put(k3, v3);
        object.put(k4, v4);
        return object;
    }
    
    public static JSONObject of(final String k1, final Object v1, final String k2, final Object v2, final String k3, final Object v3, final String k4, final Object v4, final String k5, final Object v5) {
        final JSONObject object = new JSONObject(5);
        object.put(k1, v1);
        object.put(k2, v2);
        object.put(k3, v3);
        object.put(k4, v4);
        object.put(k5, v5);
        return object;
    }
    
    public static <T> T parseObject(final String text, final Class<T> objectClass) {
        return JSON.parseObject(text, objectClass);
    }
    
    public static <T> T parseObject(final String text, final Class<T> objectClass, final JSONReader.Feature... features) {
        return JSON.parseObject(text, objectClass, features);
    }
    
    public static <T> T parseObject(final String text, final Type objectType, final JSONReader.Feature... features) {
        return JSON.parseObject(text, objectType, features);
    }
    
    public static <T> T parseObject(final String text, final TypeReference<T> typeReference, final JSONReader.Feature... features) {
        return JSON.parseObject(text, typeReference, features);
    }
    
    public static JSONObject parseObject(final String text) {
        return JSON.parseObject(text);
    }
    
    public static JSONObject parse(final String text, final JSONReader.Feature... features) {
        return JSON.parseObject(text, features);
    }
    
    public static JSONObject from(final Object obj) {
        return (JSONObject)JSON.toJSON(obj);
    }
    
    public static JSONObject from(final Object obj, final JSONWriter.Feature... writeFeatures) {
        return (JSONObject)JSON.toJSON(obj, writeFeatures);
    }
    
    static {
        NONE_DIRECT_FEATURES = (JSONWriter.Feature.ReferenceDetection.mask | JSONWriter.Feature.PrettyFormat.mask | JSONWriter.Feature.NotWriteEmptyArray.mask | JSONWriter.Feature.NotWriteDefaultValue.mask);
    }
    
    static class NameConsumer implements Consumer<Method>
    {
        final Annotation annotation;
        String name;
        
        NameConsumer(final Annotation annotation) {
            this.annotation = annotation;
        }
        
        @Override
        public void accept(final Method method) {
            final String methodName = method.getName();
            if ("name".equals(methodName)) {
                try {
                    final String result = (String)method.invoke(this.annotation, new Object[0]);
                    if (!result.isEmpty()) {
                        this.name = result;
                    }
                }
                catch (IllegalAccessException ex) {}
                catch (InvocationTargetException ex2) {}
            }
        }
    }
}
