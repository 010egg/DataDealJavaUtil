// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import com.alibaba.fastjson2.util.TypeUtils;
import java.util.function.BiFunction;
import java.util.Map;
import com.alibaba.fastjson2.function.impl.ToDouble;
import java.util.UUID;
import java.util.Iterator;
import java.lang.reflect.Array;
import java.util.Collection;
import java.math.BigInteger;
import java.util.List;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.function.Function;

final class JSONPathFunction extends JSONPathSegment implements EvalSegment
{
    static final JSONPathFunction FUNC_TYPE;
    static final JSONPathFunction FUNC_DOUBLE;
    static final JSONPathFunction FUNC_FLOOR;
    static final JSONPathFunction FUNC_CEIL;
    static final JSONPathFunction FUNC_ABS;
    static final JSONPathFunction FUNC_NEGATIVE;
    static final JSONPathFunction FUNC_EXISTS;
    static final JSONPathFunction FUNC_LOWER;
    static final JSONPathFunction FUNC_UPPER;
    static final JSONPathFunction FUNC_TRIM;
    static final JSONPathFunction FUNC_FIRST;
    static final JSONPathFunction FUNC_LAST;
    final Function function;
    
    public JSONPathFunction(final Function function) {
        this.function = function;
    }
    
    static Object floor(final Object value) {
        if (value instanceof Double) {
            return Math.floor((double)value);
        }
        if (value instanceof Float) {
            return Math.floor((float)value);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).setScale(0, RoundingMode.FLOOR);
        }
        if (value instanceof List) {
            final List list = (List)value;
            for (int i = 0, l = list.size(); i < l; ++i) {
                final Object item = list.get(i);
                if (item instanceof Double) {
                    list.set(i, Math.floor((double)item));
                }
                else if (item instanceof Float) {
                    list.set(i, Math.floor((float)item));
                }
                else if (item instanceof BigDecimal) {
                    list.set(i, ((BigDecimal)item).setScale(0, RoundingMode.FLOOR));
                }
            }
        }
        return value;
    }
    
    static Object ceil(final Object value) {
        if (value instanceof Double) {
            return Math.ceil((double)value);
        }
        if (value instanceof Float) {
            return Math.ceil((float)value);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).setScale(0, RoundingMode.CEILING);
        }
        if (value instanceof List) {
            final List list = (List)value;
            for (int i = 0, l = list.size(); i < l; ++i) {
                final Object item = list.get(i);
                if (item instanceof Double) {
                    list.set(i, Math.ceil((double)item));
                }
                else if (item instanceof Float) {
                    list.set(i, Math.ceil((float)item));
                }
                else if (item instanceof BigDecimal) {
                    list.set(i, ((BigDecimal)item).setScale(0, RoundingMode.CEILING));
                }
            }
        }
        return value;
    }
    
    static Object exists(final Object value) {
        return value != null;
    }
    
    static Object negative(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            final int intValue = (int)value;
            if (intValue == Integer.MIN_VALUE) {
                return -intValue;
            }
            return -intValue;
        }
        else if (value instanceof Long) {
            final long longValue = (long)value;
            if (longValue == Long.MIN_VALUE) {
                return BigInteger.valueOf(longValue).negate();
            }
            return -longValue;
        }
        else if (value instanceof Byte) {
            final byte byteValue = (byte)value;
            if (byteValue == -128) {
                return -byteValue;
            }
            return (byte)(-byteValue);
        }
        else if (value instanceof Short) {
            final short shortValue = (short)value;
            if (shortValue == -32768) {
                return -shortValue;
            }
            return (short)(-shortValue);
        }
        else {
            if (value instanceof Double) {
                return -(double)value;
            }
            if (value instanceof Float) {
                return -(float)value;
            }
            if (value instanceof BigDecimal) {
                return ((BigDecimal)value).negate();
            }
            if (value instanceof BigInteger) {
                return ((BigInteger)value).negate();
            }
            if (value instanceof List) {
                final List list = (List)value;
                final JSONArray values = new JSONArray(list.size());
                for (int i = 0; i < list.size(); ++i) {
                    final Object item = list.get(i);
                    final Object negativeItem = negative(item);
                    values.add(negativeItem);
                }
                return values;
            }
            return value;
        }
    }
    
    static Object first(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof JSONPath.Sequence) {
            value = ((JSONPath.Sequence)value).values;
        }
        if (value instanceof List) {
            if (((List)value).isEmpty()) {
                return null;
            }
            return ((List)value).get(0);
        }
        else if (value instanceof Collection) {
            final Collection<?> collection = (Collection<?>)value;
            if (collection.isEmpty()) {
                return null;
            }
            return collection.iterator().next();
        }
        else {
            if (!value.getClass().isArray()) {
                return value;
            }
            final int len = Array.getLength(value);
            if (len == 0) {
                return null;
            }
            return Array.get(value, 0);
        }
    }
    
    static Object last(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof JSONPath.Sequence) {
            value = ((JSONPath.Sequence)value).values;
        }
        if (value instanceof List) {
            final List list = (List)value;
            final int size = list.size();
            if (size == 0) {
                return null;
            }
            return list.get(size - 1);
        }
        else if (value instanceof Collection) {
            final Collection<?> collection = (Collection<?>)value;
            if (collection.isEmpty()) {
                return null;
            }
            Object last = null;
            final Iterator<?> iterator = collection.iterator();
            while (iterator.hasNext()) {
                final Object o = last = iterator.next();
            }
            return last;
        }
        else {
            if (!value.getClass().isArray()) {
                return value;
            }
            final int len = Array.getLength(value);
            if (len == 0) {
                return null;
            }
            return Array.get(value, len - 1);
        }
    }
    
    static Object abs(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            final int intValue = (int)value;
            if (intValue < 0) {
                return -intValue;
            }
            return value;
        }
        else if (value instanceof Long) {
            final long longValue = (long)value;
            if (longValue < 0L) {
                return -longValue;
            }
            return value;
        }
        else if (value instanceof Byte) {
            final byte byteValue = (byte)value;
            if (byteValue < 0) {
                return (byte)(-byteValue);
            }
            return value;
        }
        else if (value instanceof Short) {
            final short shortValue = (short)value;
            if (shortValue < 0) {
                return (short)(-shortValue);
            }
            return value;
        }
        else if (value instanceof Double) {
            final double doubleValue = (double)value;
            if (doubleValue < 0.0) {
                return -doubleValue;
            }
            return value;
        }
        else if (value instanceof Float) {
            final float floatValue = (float)value;
            if (floatValue < 0.0f) {
                return -floatValue;
            }
            return value;
        }
        else {
            if (value instanceof BigDecimal) {
                return ((BigDecimal)value).abs();
            }
            if (value instanceof BigInteger) {
                return ((BigInteger)value).abs();
            }
            if (value instanceof List) {
                final List list = (List)value;
                final JSONArray values = new JSONArray(list.size());
                for (int i = 0; i < list.size(); ++i) {
                    final Object item = list.get(i);
                    values.add(abs(item));
                }
                return values;
            }
            throw new JSONException("abs not support " + value);
        }
    }
    
    static String type(final Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Collection) {
            return "array";
        }
        if (value instanceof Number) {
            return "number";
        }
        if (value instanceof Boolean) {
            return "boolean";
        }
        if (value instanceof String || value instanceof UUID || value instanceof Enum) {
            return "string";
        }
        return "object";
    }
    
    static Object lower(final Object value) {
        if (value == null) {
            return null;
        }
        String str;
        if (value instanceof String) {
            str = (String)value;
        }
        else {
            str = value.toString();
        }
        return str.toLowerCase();
    }
    
    static Object upper(final Object value) {
        if (value == null) {
            return null;
        }
        String str;
        if (value instanceof String) {
            str = (String)value;
        }
        else {
            str = value.toString();
        }
        return str.toUpperCase();
    }
    
    static Object trim(final Object value) {
        if (value == null) {
            return null;
        }
        String str;
        if (value instanceof String) {
            str = (String)value;
        }
        else {
            str = value.toString();
        }
        return str.trim();
    }
    
    @Override
    public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
        if (context.parent == null) {
            context.root = jsonReader.readAny();
            context.eval = true;
        }
        this.eval(context);
    }
    
    @Override
    public void eval(final JSONPath.Context context) {
        final Object value = (context.parent == null) ? context.root : context.parent.value;
        context.value = this.function.apply(value);
    }
    
    static {
        FUNC_TYPE = new JSONPathFunction(JSONPathFunction::type);
        FUNC_DOUBLE = new JSONPathFunction(new ToDouble(null));
        FUNC_FLOOR = new JSONPathFunction(JSONPathFunction::floor);
        FUNC_CEIL = new JSONPathFunction(JSONPathFunction::ceil);
        FUNC_ABS = new JSONPathFunction(JSONPathFunction::abs);
        FUNC_NEGATIVE = new JSONPathFunction(JSONPathFunction::negative);
        FUNC_EXISTS = new JSONPathFunction(JSONPathFunction::exists);
        FUNC_LOWER = new JSONPathFunction(JSONPathFunction::lower);
        FUNC_UPPER = new JSONPathFunction(JSONPathFunction::upper);
        FUNC_TRIM = new JSONPathFunction(JSONPathFunction::trim);
        FUNC_FIRST = new JSONPathFunction(JSONPathFunction::first);
        FUNC_LAST = new JSONPathFunction(JSONPathFunction::last);
    }
    
    static final class TypeFunction implements Function
    {
        static final TypeFunction INSTANCE;
        
        @Override
        public Object apply(final Object object) {
            return JSONPathFunction.type(object);
        }
        
        static {
            INSTANCE = new TypeFunction();
        }
    }
    
    static final class SizeFunction implements Function
    {
        static final SizeFunction INSTANCE;
        
        @Override
        public Object apply(final Object value) {
            if (value == null) {
                return -1;
            }
            if (value instanceof Collection) {
                return ((Collection)value).size();
            }
            if (value.getClass().isArray()) {
                return Array.getLength(value);
            }
            if (value instanceof Map) {
                return ((Map)value).size();
            }
            if (value instanceof JSONPath.Sequence) {
                return ((JSONPath.Sequence)value).values.size();
            }
            return 1;
        }
        
        static {
            INSTANCE = new SizeFunction();
        }
    }
    
    static final class BiFunctionAdapter implements BiFunction
    {
        private final Function function;
        
        BiFunctionAdapter(final Function function) {
            this.function = function;
        }
        
        @Override
        public Object apply(final Object o1, final Object o2) {
            return this.function.apply(o2);
        }
    }
    
    abstract static class Index implements Function
    {
        protected abstract boolean eq(final Object p0);
        
        @Override
        public final Object apply(final Object o) {
            if (o == null) {
                return null;
            }
            if (o instanceof List) {
                final List list = (List)o;
                for (int i = 0; i < list.size(); ++i) {
                    if (this.eq(list.get(i))) {
                        return i;
                    }
                }
                return -1;
            }
            if (o.getClass().isArray()) {
                for (int len = Array.getLength(o), i = 0; i < len; ++i) {
                    final Object item = Array.get(o, i);
                    if (this.eq(item)) {
                        return i;
                    }
                }
                return -1;
            }
            if (this.eq(o)) {
                return 0;
            }
            return null;
        }
    }
    
    static final class IndexInt extends Index
    {
        final long value;
        transient BigDecimal decimalValue;
        
        public IndexInt(final long value) {
            this.value = value;
        }
        
        @Override
        protected boolean eq(final Object item) {
            if (item instanceof Integer || item instanceof Long || item instanceof Byte || item instanceof Short) {
                return ((Number)item).longValue() == this.value;
            }
            if (item instanceof Float || item instanceof Double) {
                return ((Number)item).doubleValue() == this.value;
            }
            if (item instanceof BigDecimal) {
                BigDecimal decimal = (BigDecimal)item;
                decimal = decimal.stripTrailingZeros();
                if (this.decimalValue == null) {
                    this.decimalValue = BigDecimal.valueOf(this.value);
                }
                return this.decimalValue.equals(decimal);
            }
            return false;
        }
    }
    
    static final class IndexDecimal extends Index
    {
        final BigDecimal value;
        
        public IndexDecimal(final BigDecimal value) {
            this.value = value;
        }
        
        @Override
        protected boolean eq(final Object item) {
            if (item == null) {
                return false;
            }
            if (item instanceof BigDecimal) {
                BigDecimal decimal = (BigDecimal)item;
                decimal = decimal.stripTrailingZeros();
                return this.value.equals(decimal);
            }
            if (item instanceof Float || item instanceof Double) {
                final double doubleValue = ((Number)item).doubleValue();
                final BigDecimal decimal2 = new BigDecimal(doubleValue).stripTrailingZeros();
                return this.value.equals(decimal2);
            }
            if (item instanceof String) {
                final String str = (String)item;
                if (TypeUtils.isNumber(str)) {
                    final BigDecimal decimal3 = new BigDecimal(str).stripTrailingZeros();
                    return this.value.equals(decimal3);
                }
            }
            return false;
        }
    }
    
    static final class IndexString extends Index
    {
        final String value;
        
        public IndexString(final String value) {
            this.value = value;
        }
        
        @Override
        protected boolean eq(final Object item) {
            return item != null && this.value.equals(item.toString());
        }
    }
}
