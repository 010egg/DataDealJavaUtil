// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.reader.ObjectReaderImplEnum;
import java.util.function.Function;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import java.util.List;
import java.time.Instant;
import com.alibaba.fastjson2.util.TypeUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.TemporalAccessor;
import java.util.UUID;
import com.alibaba.fastjson2.util.DateUtils;
import java.util.Date;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.util.Map;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.util.ArrayList;

public class JSONArray extends ArrayList<Object>
{
    private static final long serialVersionUID = 1L;
    static ObjectWriter<JSONArray> arrayWriter;
    
    public JSONArray() {
    }
    
    public JSONArray(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public JSONArray(final Collection<?> collection) {
        super(collection);
    }
    
    public JSONArray(final Object... items) {
        super(items.length);
        super.addAll(Arrays.asList(items));
    }
    
    @Override
    public Object set(int index, final Object element) {
        final int size = super.size();
        if (index < 0) {
            index += size;
            if (index < 0) {
                super.add(0, element);
                return null;
            }
            return super.set(index, element);
        }
        else {
            if (index < size) {
                return super.set(index, element);
            }
            if (index < size + 4096) {
                while (index-- != size) {
                    super.add(null);
                }
                super.add(element);
            }
            return null;
        }
    }
    
    public JSONArray getJSONArray(final int index) {
        final Object value = this.get(index);
        if (value == null) {
            return null;
        }
        if (value instanceof JSONArray) {
            return (JSONArray)value;
        }
        if (value instanceof String) {
            final String str = (String)value;
            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            final JSONReader reader = JSONReader.of(str);
            return JSONFactory.ARRAY_READER.readObject(reader, null, null, 0L);
        }
        else {
            if (value instanceof Collection) {
                final JSONArray array = new JSONArray((Collection<?>)value);
                this.set(index, array);
                return array;
            }
            if (value instanceof Object[]) {
                return of((Object[])value);
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
    
    public JSONObject getJSONObject(final int index) {
        final Object value = this.get(index);
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
                this.set(index, object);
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
    
    public String getString(final int index) {
        final Object value = this.get(index);
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
    
    public Double getDouble(final int index) {
        final Object value = this.get(index);
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
    
    public double getDoubleValue(final int index) {
        final Object value = this.get(index);
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
    
    public Float getFloat(final int index) {
        final Object value = this.get(index);
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
    
    public float getFloatValue(final int index) {
        final Object value = this.get(index);
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
    
    public Long getLong(final int index) {
        final Object value = this.get(index);
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
        return Long.parseLong(str);
    }
    
    public long getLongValue(final int index) {
        final Object value = this.get(index);
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
        return Long.parseLong(str);
    }
    
    public Integer getInteger(final int index) {
        final Object value = this.get(index);
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
        return Integer.parseInt(str);
    }
    
    public int getIntValue(final int index) {
        final Object value = this.get(index);
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
        return Integer.parseInt(str);
    }
    
    public Short getShort(final int index) {
        final Object value = this.get(index);
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
    
    public short getShortValue(final int index) {
        final Object value = this.get(index);
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
    
    public Byte getByte(final int index) {
        final Object value = this.get(index);
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
    
    public byte getByteValue(final int index) {
        final Object value = this.get(index);
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
    
    public Boolean getBoolean(final int index) {
        final Object value = this.get(index);
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
    
    public boolean getBooleanValue(final int index) {
        final Object value = this.get(index);
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
    
    public BigInteger getBigInteger(final int index) {
        final Object value = this.get(index);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            if (value instanceof BigInteger) {
                return (BigInteger)value;
            }
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
    
    public BigDecimal getBigDecimal(final int index) {
        final Object value = this.get(index);
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
                return TypeUtils.toBigDecimal((String)value);
            }
            if (value instanceof Boolean) {
                return value ? BigDecimal.ONE : BigDecimal.ZERO;
            }
            throw new JSONException("Can not cast '" + value.getClass() + "' to BigDecimal");
        }
    }
    
    public Date getDate(final int index) {
        final Object value = this.get(index);
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return (Date)value;
        }
        if (!(value instanceof Number)) {
            return TypeUtils.toDate(value);
        }
        final long millis = ((Number)value).longValue();
        if (millis == 0L) {
            return null;
        }
        return new Date(millis);
    }
    
    public Date getDate(final int index, final Date defaultValue) {
        Date date = this.getDate(index);
        if (date == null) {
            date = defaultValue;
        }
        return date;
    }
    
    public Instant getInstant(final int index) {
        final Object value = this.get(index);
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
            if ((writer.context.features & JSONObject.NONE_DIRECT_FEATURES) == 0x0L) {
                writer.write(this);
            }
            else {
                writer.setRootObject(this);
                if (JSONArray.arrayWriter == null) {
                    JSONArray.arrayWriter = (ObjectWriter<JSONArray>)writer.getObjectWriter(JSONArray.class, JSONArray.class);
                }
                JSONArray.arrayWriter.write(writer, this, null, null, 0L);
            }
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
    
    public <T> T to(final Type type) {
        if (type == String.class) {
            return (T)this.toString();
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(type);
        return objectReader.createInstance(this);
    }
    
    public <T> T to(final Class<T> type) {
        if (type == String.class) {
            return (T)this.toString();
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(type);
        return objectReader.createInstance(this);
    }
    
    @Deprecated
    public <T> T toJavaObject(final Type type) {
        return (T)this.to(type);
    }
    
    public <T> List<T> toList(final Class<T> itemClass, final JSONReader.Feature... features) {
        boolean fieldBased = false;
        long featuresValue = 0L;
        for (final JSONReader.Feature feature : features) {
            featuresValue |= feature.mask;
            if (feature == JSONReader.Feature.FieldBased) {
                fieldBased = true;
            }
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final ObjectReader<?> objectReader = (ObjectReader<?>)provider.getObjectReader(itemClass, fieldBased);
        final List<T> list = new ArrayList<T>(this.size());
        for (int i = 0; i < this.size(); ++i) {
            final Object item = this.get(i);
            T classItem;
            if (item instanceof JSONObject) {
                classItem = (T)objectReader.createInstance((Map)item, featuresValue);
            }
            else if (item instanceof Map) {
                classItem = (T)objectReader.createInstance((Map)item, featuresValue);
            }
            else if (item == null || itemClass.isInstance(item)) {
                classItem = (T)item;
            }
            else {
                final Class<?> currentItemClass = item.getClass();
                final Function typeConvert = provider.getTypeConvert(currentItemClass, itemClass);
                if (typeConvert != null) {
                    final Object converted = typeConvert.apply(item);
                    list.add((T)converted);
                    continue;
                }
                throw new JSONException(currentItemClass + " cannot be converted to " + itemClass);
            }
            list.add(classItem);
        }
        return list;
    }
    
    public <T> T[] toArray(final Class<T> itemClass, final JSONReader.Feature... features) {
        boolean fieldBased = false;
        long featuresValue = 0L;
        for (final JSONReader.Feature feature : features) {
            featuresValue |= feature.mask;
            if (feature == JSONReader.Feature.FieldBased) {
                fieldBased = true;
            }
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final ObjectReader<?> objectReader = (ObjectReader<?>)provider.getObjectReader(itemClass, fieldBased);
        final T[] list = (T[])Array.newInstance(itemClass, this.size());
        for (int i = 0; i < this.size(); ++i) {
            final Object item = this.get(i);
            T classItem;
            if (item instanceof JSONObject) {
                classItem = (T)objectReader.createInstance((Map)item, featuresValue);
            }
            else if (item instanceof Map) {
                classItem = (T)objectReader.createInstance((Map)item, featuresValue);
            }
            else if (item == null || itemClass.isInstance(item)) {
                classItem = (T)item;
            }
            else {
                final Class<?> currentItemClass = item.getClass();
                final Function typeConvert = provider.getTypeConvert(currentItemClass, itemClass);
                if (typeConvert != null) {
                    final Object converted = typeConvert.apply(item);
                    list[i] = (T)converted;
                    continue;
                }
                throw new JSONException(currentItemClass + " cannot be converted to " + itemClass);
            }
            list[i] = classItem;
        }
        return list;
    }
    
    public <T> List<T> toJavaList(final Class<T> clazz, final JSONReader.Feature... features) {
        return (List<T>)this.toList((Class<Object>)clazz, features);
    }
    
    public <T> T getObject(final int index, final Type type, final JSONReader.Feature... features) {
        final Object value = this.get(index);
        if (value == null) {
            return null;
        }
        final Class<?> valueClass = value.getClass();
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final Function typeConvert = provider.getTypeConvert(valueClass, type);
        if (typeConvert != null) {
            return typeConvert.apply(value);
        }
        boolean fieldBased = false;
        long featuresValue = 0L;
        for (final JSONReader.Feature feature : features) {
            featuresValue |= feature.mask;
            if (feature == JSONReader.Feature.FieldBased) {
                fieldBased = true;
            }
        }
        if (value instanceof Map) {
            final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(type, fieldBased);
            return objectReader.createInstance((Map)value, featuresValue);
        }
        if (value instanceof Collection) {
            final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(type, fieldBased);
            return objectReader.createInstance((Collection)value);
        }
        final Class clazz = TypeUtils.getMapping(type);
        if (clazz.isInstance(value)) {
            return (T)value;
        }
        final String json = JSON.toJSONString(value);
        final JSONReader jsonReader = JSONReader.of(json);
        jsonReader.context.config(features);
        final ObjectReader objectReader2 = provider.getObjectReader(clazz, fieldBased);
        return objectReader2.readObject(jsonReader, null, null, 0L);
    }
    
    public <T> T getObject(final int index, final Class<T> type, final JSONReader.Feature... features) {
        final Object value = this.get(index);
        if (value == null) {
            return null;
        }
        final Class<?> valueClass = value.getClass();
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final Function typeConvert = provider.getTypeConvert(valueClass, type);
        if (typeConvert != null) {
            return typeConvert.apply(value);
        }
        boolean fieldBased = false;
        long featuresValue = 0L;
        for (final JSONReader.Feature feature : features) {
            featuresValue |= feature.mask;
            if (feature == JSONReader.Feature.FieldBased) {
                fieldBased = true;
            }
        }
        if (value instanceof Map) {
            final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(type, fieldBased);
            return objectReader.createInstance((Map)value, featuresValue);
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
    
    public <T> T getObject(final int index, final Function<JSONObject, T> creator) {
        final JSONObject object = this.getJSONObject(index);
        if (object == null) {
            return null;
        }
        return creator.apply(object);
    }
    
    public JSONObject addObject() {
        final JSONObject object = new JSONObject();
        ((ArrayList<JSONObject>)this).add(object);
        return object;
    }
    
    public JSONArray addArray() {
        final JSONArray array = new JSONArray();
        ((ArrayList<JSONArray>)this).add(array);
        return array;
    }
    
    public JSONArray fluentAdd(final Object element) {
        this.add(element);
        return this;
    }
    
    public JSONArray fluentClear() {
        this.clear();
        return this;
    }
    
    public JSONArray fluentRemove(final int index) {
        this.remove(index);
        return this;
    }
    
    public JSONArray fluentSet(final int index, final Object element) {
        this.set(index, element);
        return this;
    }
    
    public JSONArray fluentRemove(final Object o) {
        this.remove(o);
        return this;
    }
    
    public JSONArray fluentRemoveAll(final Collection<?> c) {
        this.removeAll(c);
        return this;
    }
    
    public JSONArray fluentAddAll(final Collection<?> c) {
        this.addAll(c);
        return this;
    }
    
    public boolean isValid(final JSONSchema schema) {
        return schema.validate(this).isSuccess();
    }
    
    @Override
    public Object clone() {
        return new JSONArray(this);
    }
    
    public static JSONArray of(final Object... items) {
        return new JSONArray(items);
    }
    
    public static JSONArray of(final Object item) {
        final JSONArray array = new JSONArray(1);
        array.add(item);
        return array;
    }
    
    public static JSONArray copyOf(final Collection collection) {
        return new JSONArray(collection);
    }
    
    public static JSONArray of(final Object first, final Object second) {
        final JSONArray array = new JSONArray(2);
        array.add(first);
        array.add(second);
        return array;
    }
    
    public static JSONArray of(final Object first, final Object second, final Object third) {
        final JSONArray array = new JSONArray(3);
        array.add(first);
        array.add(second);
        array.add(third);
        return array;
    }
    
    public static JSONArray parseArray(final String text, final JSONReader.Feature... features) {
        return JSON.parseArray(text, features);
    }
    
    public static <T> List<T> parseArray(final String text, final Class<T> type, final JSONReader.Feature... features) {
        return JSON.parseArray(text, type, features);
    }
    
    public static JSONArray parse(final String text, final JSONReader.Feature... features) {
        return JSON.parseArray(text, features);
    }
    
    public static <T> List<T> parseArray(final String input, final Class<T> type) {
        return JSON.parseArray(input, type);
    }
    
    public static JSONArray from(final Object obj) {
        return (JSONArray)JSON.toJSON(obj);
    }
    
    public static JSONArray from(final Object obj, final JSONWriter.Feature... writeFeatures) {
        return (JSONArray)JSON.toJSON(obj, writeFeatures);
    }
}
