// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.Objects;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.util.Map;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.function.BiFunction;
import java.util.List;
import com.alibaba.fastjson2.util.IOUtils;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.function.Function;

abstract class JSONPathFilter extends JSONPathSegment implements EvalSegment
{
    private boolean and;
    
    JSONPathFilter() {
        this.and = true;
    }
    
    public boolean isAnd() {
        return this.and;
    }
    
    public JSONPathFilter setAnd(final boolean and) {
        this.and = and;
        return this;
    }
    
    abstract boolean apply(final JSONPath.Context p0, final Object p1);
    
    enum Operator
    {
        EQ, 
        NE, 
        GT, 
        GE, 
        LT, 
        LE, 
        LIKE, 
        NOT_LIKE, 
        RLIKE, 
        NOT_RLIKE, 
        IN, 
        NOT_IN, 
        BETWEEN, 
        NOT_BETWEEN, 
        AND, 
        OR, 
        REG_MATCH, 
        STARTS_WITH, 
        ENDS_WITH, 
        CONTAINS, 
        NOT_CONTAINS;
        
        @Override
        public String toString() {
            switch (this) {
                case EQ: {
                    return "==";
                }
                case NE: {
                    return "!=";
                }
                case GT: {
                    return ">";
                }
                case GE: {
                    return ">=";
                }
                case LT: {
                    return "<";
                }
                case LE: {
                    return "<=";
                }
                case LIKE: {
                    return "like";
                }
                case NOT_LIKE: {
                    return "not like";
                }
                case RLIKE: {
                    return "rlike";
                }
                case NOT_RLIKE: {
                    return "not rlike";
                }
                case BETWEEN: {
                    return "between";
                }
                case NOT_BETWEEN: {
                    return "not between";
                }
                case AND: {
                    return "and";
                }
                case OR: {
                    return "or";
                }
                case STARTS_WITH: {
                    return "starts with";
                }
                case ENDS_WITH: {
                    return "ends with";
                }
                case CONTAINS: {
                    return "contains";
                }
                case NOT_CONTAINS: {
                    return "not contains";
                }
                default: {
                    return this.name();
                }
            }
        }
        
        private static /* synthetic */ Operator[] $values() {
            return new Operator[] { Operator.EQ, Operator.NE, Operator.GT, Operator.GE, Operator.LT, Operator.LE, Operator.LIKE, Operator.NOT_LIKE, Operator.RLIKE, Operator.NOT_RLIKE, Operator.IN, Operator.NOT_IN, Operator.BETWEEN, Operator.NOT_BETWEEN, Operator.AND, Operator.OR, Operator.REG_MATCH, Operator.STARTS_WITH, Operator.ENDS_WITH, Operator.CONTAINS, Operator.NOT_CONTAINS };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    static final class NameIsNull extends NameFilter
    {
        public NameIsNull(final String fieldName, final long fieldNameNameHash, final String[] fieldName2, final long[] fieldNameNameHash2, final Function function) {
            super(fieldName, fieldNameNameHash, fieldName2, fieldNameNameHash2, function);
        }
        
        @Override
        protected boolean applyNull() {
            return true;
        }
        
        @Override
        boolean apply(Object fieldValue) {
            if (this.function != null) {
                fieldValue = this.function.apply(fieldValue);
            }
            return fieldValue == null;
        }
    }
    
    static final class NameIntOpSegment extends NameFilter
    {
        final Operator operator;
        final long value;
        
        public NameIntOpSegment(final String name, final long nameHashCode, final String[] fieldName2, final long[] fieldNameNameHash2, final Function expr, final Operator operator, final long value) {
            super(name, nameHashCode, fieldName2, fieldNameNameHash2, expr);
            this.operator = operator;
            this.value = value;
        }
        
        @Override
        protected boolean applyNull() {
            return this.operator == Operator.NE;
        }
        
        public boolean apply(final Object fieldValue) {
            final boolean objInt = fieldValue instanceof Boolean || fieldValue instanceof Byte || fieldValue instanceof Short || fieldValue instanceof Integer || fieldValue instanceof Long;
            if (objInt) {
                long fieldValueInt;
                if (fieldValue instanceof Boolean) {
                    fieldValueInt = (((boolean)fieldValue) ? 1 : 0);
                }
                else {
                    fieldValueInt = ((Number)fieldValue).longValue();
                }
                switch (this.operator) {
                    case LT: {
                        return fieldValueInt < this.value;
                    }
                    case LE: {
                        return fieldValueInt <= this.value;
                    }
                    case EQ: {
                        return fieldValueInt == this.value;
                    }
                    case NE: {
                        return fieldValueInt != this.value;
                    }
                    case GT: {
                        return fieldValueInt > this.value;
                    }
                    case GE: {
                        return fieldValueInt >= this.value;
                    }
                    default: {
                        throw new UnsupportedOperationException();
                    }
                }
            }
            else {
                int cmp;
                if (fieldValue instanceof BigDecimal) {
                    cmp = ((BigDecimal)fieldValue).compareTo(BigDecimal.valueOf(this.value));
                }
                else if (fieldValue instanceof BigInteger) {
                    cmp = ((BigInteger)fieldValue).compareTo(BigInteger.valueOf(this.value));
                }
                else if (fieldValue instanceof Float) {
                    cmp = ((Float)fieldValue).compareTo(Float.valueOf(this.value));
                }
                else if (fieldValue instanceof Double) {
                    cmp = ((Double)fieldValue).compareTo(Double.valueOf(this.value));
                }
                else {
                    if (!(fieldValue instanceof String)) {
                        throw new UnsupportedOperationException();
                    }
                    final String fieldValueStr = (String)fieldValue;
                    if (IOUtils.isNumber(fieldValueStr)) {
                        try {
                            cmp = Long.compare(Long.parseLong(fieldValueStr), this.value);
                        }
                        catch (Exception ignored) {
                            cmp = fieldValueStr.compareTo(Long.toString(this.value));
                        }
                    }
                    else {
                        cmp = fieldValueStr.compareTo(Long.toString(this.value));
                    }
                }
                switch (this.operator) {
                    case LT: {
                        return cmp < 0;
                    }
                    case LE: {
                        return cmp <= 0;
                    }
                    case EQ: {
                        return cmp == 0;
                    }
                    case NE: {
                        return cmp != 0;
                    }
                    case GT: {
                        return cmp > 0;
                    }
                    case GE: {
                        return cmp >= 0;
                    }
                    default: {
                        throw new UnsupportedOperationException();
                    }
                }
            }
        }
        
        @Override
        public void set(final JSONPath.Context context, final Object value) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof List) {
                final List list = (List)object;
                for (int i = 0; i < list.size(); ++i) {
                    final Object item = list.get(i);
                    if (this.apply(context, item)) {
                        list.set(i, value);
                    }
                }
                return;
            }
            throw new JSONException("UnsupportedOperation ");
        }
        
        @Override
        public void setCallback(final JSONPath.Context context, final BiFunction callback) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof List) {
                final List list = (List)object;
                for (int i = 0; i < list.size(); ++i) {
                    final Object item = list.get(i);
                    if (this.apply(context, item)) {
                        final Object value = callback.apply(list, item);
                        if (value != item) {
                            list.set(i, value);
                        }
                    }
                }
                return;
            }
            throw new JSONException("UnsupportedOperation ");
        }
        
        @Override
        public String toString() {
            return "[?(" + ((this.fieldName2 == null) ? "@" : this.fieldName2) + '.' + this.fieldName + ' ' + this.operator + ' ' + this.value + ")]";
        }
    }
    
    static final class NameDecimalOpSegment extends NameFilter
    {
        final Operator operator;
        final BigDecimal value;
        
        public NameDecimalOpSegment(final String name, final long nameHashCode, final Operator operator, final BigDecimal value) {
            super(name, nameHashCode);
            this.operator = operator;
            this.value = value;
        }
        
        @Override
        protected boolean applyNull() {
            return this.operator == Operator.NE;
        }
        
        public boolean apply(final Object fieldValue) {
            if (fieldValue == null) {
                return false;
            }
            BigDecimal fieldValueDecimal;
            if (fieldValue instanceof Boolean) {
                fieldValueDecimal = (fieldValue ? BigDecimal.ONE : BigDecimal.ZERO);
            }
            else if (fieldValue instanceof Byte || fieldValue instanceof Short || fieldValue instanceof Integer || fieldValue instanceof Long) {
                fieldValueDecimal = BigDecimal.valueOf(((Number)fieldValue).longValue());
            }
            else if (fieldValue instanceof BigDecimal) {
                fieldValueDecimal = (BigDecimal)fieldValue;
            }
            else {
                if (!(fieldValue instanceof BigInteger)) {
                    throw new UnsupportedOperationException();
                }
                fieldValueDecimal = new BigDecimal((BigInteger)fieldValue);
            }
            final int cmp = fieldValueDecimal.compareTo(this.value);
            switch (this.operator) {
                case LT: {
                    return cmp < 0;
                }
                case LE: {
                    return cmp <= 0;
                }
                case EQ: {
                    return cmp == 0;
                }
                case NE: {
                    return cmp != 0;
                }
                case GT: {
                    return cmp > 0;
                }
                case GE: {
                    return cmp >= 0;
                }
                default: {
                    throw new UnsupportedOperationException();
                }
            }
        }
    }
    
    static final class NameRLikeSegment extends NameFilter
    {
        final Pattern pattern;
        final boolean not;
        
        public NameRLikeSegment(final String fieldName, final long fieldNameNameHash, final Pattern pattern, final boolean not) {
            super(fieldName, fieldNameNameHash);
            this.pattern = pattern;
            this.not = not;
        }
        
        @Override
        boolean apply(final Object fieldValue) {
            final String strPropertyValue = fieldValue.toString();
            final Matcher m = this.pattern.matcher(strPropertyValue);
            boolean match = m.matches();
            if (this.not) {
                match = !match;
            }
            return match;
        }
    }
    
    static final class StartsWithSegment extends NameFilter
    {
        final String prefix;
        
        public StartsWithSegment(final String fieldName, final long fieldNameNameHash, final String prefix) {
            super(fieldName, fieldNameNameHash);
            this.prefix = prefix;
        }
        
        @Override
        boolean apply(final Object fieldValue) {
            final String propertyValue = fieldValue.toString();
            return propertyValue != null && propertyValue.startsWith(this.prefix);
        }
    }
    
    static final class EndsWithSegment extends NameFilter
    {
        final String prefix;
        
        public EndsWithSegment(final String fieldName, final long fieldNameNameHash, final String prefix) {
            super(fieldName, fieldNameNameHash);
            this.prefix = prefix;
        }
        
        @Override
        boolean apply(final Object fieldValue) {
            final String propertyValue = fieldValue.toString();
            return propertyValue != null && propertyValue.endsWith(this.prefix);
        }
    }
    
    static final class GroupFilter extends JSONPathSegment implements EvalSegment
    {
        final List<JSONPathFilter> filters;
        
        public GroupFilter(final List<JSONPathFilter> filters) {
            this.filters = filters;
        }
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            if (context.parent == null) {
                context.root = jsonReader.readAny();
            }
            this.eval(context);
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            List<JSONPathFilter> orderedFilters = new ArrayList<JSONPathFilter>();
            if (this.filters != null) {
                orderedFilters = this.filters.stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)JSONPathFilter::isAnd)).collect((Collector<? super Object, ?, List<JSONPathFilter>>)Collectors.toList());
            }
            if (object instanceof List) {
                final List list = (List)object;
                final JSONArray array = new JSONArray(list.size());
                for (int i = 0; i < list.size(); ++i) {
                    final Object item = list.get(i);
                    boolean match = false;
                    for (final JSONPathFilter filter : orderedFilters) {
                        final boolean and = match = filter.isAnd();
                        final boolean result = filter.apply(context, item);
                        if (and) {
                            if (!result) {
                                match = false;
                                break;
                            }
                            continue;
                        }
                        else {
                            if (result) {
                                match = true;
                                break;
                            }
                            continue;
                        }
                    }
                    if (match) {
                        array.add(item);
                    }
                }
                context.value = array;
                context.eval = true;
                return;
            }
            boolean match2 = false;
            for (final JSONPathFilter filter2 : orderedFilters) {
                final boolean and2 = match2 = filter2.isAnd();
                final boolean result2 = filter2.apply(context, object);
                if (and2) {
                    if (!result2) {
                        match2 = false;
                        break;
                    }
                    continue;
                }
                else {
                    if (result2) {
                        match2 = true;
                        break;
                    }
                    continue;
                }
            }
            if (match2) {
                context.value = object;
            }
            context.eval = true;
        }
    }
    
    abstract static class NameFilter extends JSONPathFilter
    {
        final String fieldName;
        final long fieldNameNameHash;
        final String[] fieldName2;
        final long[] fieldNameNameHash2;
        final Function function;
        
        public NameFilter(final String fieldName, final long fieldNameNameHash) {
            this.fieldName = fieldName;
            this.fieldNameNameHash = fieldNameNameHash;
            this.fieldName2 = null;
            this.fieldNameNameHash2 = null;
            this.function = null;
        }
        
        public NameFilter(final String fieldName, final long fieldNameNameHash, final String[] fieldName2, final long[] fieldNameNameHash2, final Function function) {
            this.fieldName = fieldName;
            this.fieldNameNameHash = fieldNameNameHash;
            this.fieldName2 = fieldName2;
            this.fieldNameNameHash2 = fieldNameNameHash2;
            this.function = function;
        }
        
        abstract boolean apply(final Object p0);
        
        @Override
        public final void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            if (context.parent == null) {
                context.root = jsonReader.readAny();
            }
            this.eval(context);
        }
        
        @Override
        public boolean remove(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof List) {
                final List list = (List)object;
                for (int i = list.size() - 1; i >= 0; --i) {
                    final Object item = list.get(i);
                    if (this.apply(context, item)) {
                        list.remove(i);
                    }
                }
                return true;
            }
            throw new JSONException("UnsupportedOperation " + this.getClass());
        }
        
        @Override
        public final void eval(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof List) {
                final List list = (List)object;
                final JSONArray array = new JSONArray(list.size());
                for (int i = 0; i < list.size(); ++i) {
                    final Object item = list.get(i);
                    if (this.apply(context, item)) {
                        array.add(item);
                    }
                }
                context.value = array;
                context.eval = true;
                return;
            }
            if (object instanceof Object[]) {
                final Object[] list2 = (Object[])object;
                final JSONArray array = new JSONArray(list2.length);
                for (final Object item2 : list2) {
                    if (this.apply(context, item2)) {
                        array.add(item2);
                    }
                }
                context.value = array;
                context.eval = true;
                return;
            }
            if (object instanceof JSONPath.Sequence) {
                final JSONPath.Sequence sequence = (JSONPath.Sequence)object;
                final JSONArray array = new JSONArray();
                for (final Object value : sequence.values) {
                    if (value instanceof Collection) {
                        for (final Object valueItem : (Collection)value) {
                            if (this.apply(context, valueItem)) {
                                array.add(valueItem);
                            }
                        }
                    }
                    else {
                        if (!this.apply(context, value)) {
                            continue;
                        }
                        array.add(value);
                    }
                }
                context.value = array;
                context.eval = true;
                return;
            }
            if (this.apply(context, object)) {
                context.value = object;
                context.eval = true;
            }
        }
        
        protected boolean applyNull() {
            return false;
        }
        
        public final boolean apply(final JSONPath.Context context, final Object object) {
            if (object == null) {
                return false;
            }
            final JSONWriter.Context writerContext = context.path.getWriterContext();
            if (object instanceof Map) {
                Object fieldValue = (this.fieldName == null) ? object : ((Map)object).get(this.fieldName);
                if (fieldValue == null) {
                    return this.applyNull();
                }
                if (this.fieldName2 != null) {
                    for (int i = 0; i < this.fieldName2.length; ++i) {
                        final String name = this.fieldName2[i];
                        if (fieldValue instanceof Map) {
                            fieldValue = ((Map)fieldValue).get(name);
                        }
                        else {
                            final ObjectWriter objectWriter2 = writerContext.getObjectWriter(fieldValue.getClass());
                            if (!(objectWriter2 instanceof ObjectWriterAdapter)) {
                                return false;
                            }
                            final FieldWriter fieldWriter2 = objectWriter2.getFieldWriter(this.fieldNameNameHash2[i]);
                            if (fieldWriter2 == null) {
                                return false;
                            }
                            fieldValue = fieldWriter2.getFieldValue(fieldValue);
                        }
                        if (fieldValue == null) {
                            return this instanceof NameIsNull;
                        }
                    }
                }
                if (this.function != null) {
                    fieldValue = this.function.apply(fieldValue);
                }
                return this.apply(fieldValue);
            }
            else {
                final ObjectWriter objectWriter3 = writerContext.getObjectWriter(object.getClass());
                if (objectWriter3 instanceof ObjectWriterAdapter) {
                    final FieldWriter fieldWriter3 = objectWriter3.getFieldWriter(this.fieldNameNameHash);
                    Object fieldValue2 = fieldWriter3.getFieldValue(object);
                    if (fieldValue2 == null) {
                        return false;
                    }
                    if (this.fieldName2 != null) {
                        for (int j = 0; j < this.fieldName2.length; ++j) {
                            final String name2 = this.fieldName2[j];
                            if (fieldValue2 instanceof Map) {
                                fieldValue2 = ((Map)fieldValue2).get(name2);
                            }
                            else {
                                final ObjectWriter objectWriter4 = writerContext.getObjectWriter(fieldValue2.getClass());
                                if (!(objectWriter4 instanceof ObjectWriterAdapter)) {
                                    return false;
                                }
                                final FieldWriter fieldWriter4 = objectWriter4.getFieldWriter(this.fieldNameNameHash2[j]);
                                if (fieldWriter4 == null) {
                                    return false;
                                }
                                fieldValue2 = fieldWriter4.getFieldValue(fieldValue2);
                            }
                            if (fieldValue2 == null) {
                                return false;
                            }
                        }
                    }
                    if (this.function != null) {
                        fieldValue2 = this.function.apply(fieldValue2);
                    }
                    return this.apply(fieldValue2);
                }
                else {
                    if (this.function != null) {
                        final Object fieldValue3 = this.function.apply(object);
                        return this.apply(fieldValue3);
                    }
                    return this.fieldName == null && this.apply(object);
                }
            }
        }
    }
    
    static final class NameStringOpSegment extends NameFilter
    {
        final Operator operator;
        final String value;
        
        public NameStringOpSegment(final String fieldName, final long fieldNameNameHash, final String[] fieldName2, final long[] fieldNameNameHash2, final Function expr, final Operator operator, final String value) {
            super(fieldName, fieldNameNameHash, fieldName2, fieldNameNameHash2, expr);
            this.operator = operator;
            this.value = value;
        }
        
        @Override
        protected boolean applyNull() {
            return this.operator == Operator.NE;
        }
        
        public boolean apply(final Object fieldValue) {
            if (!(fieldValue instanceof String)) {
                return false;
            }
            final int cmp = ((String)fieldValue).compareTo(this.value);
            switch (this.operator) {
                case LT: {
                    return cmp < 0;
                }
                case LE: {
                    return cmp <= 0;
                }
                case EQ: {
                    return cmp == 0;
                }
                case NE: {
                    return cmp != 0;
                }
                case GT: {
                    return cmp > 0;
                }
                case GE: {
                    return cmp >= 0;
                }
                default: {
                    throw new UnsupportedOperationException();
                }
            }
        }
    }
    
    static final class NameArrayOpSegment extends NameFilter
    {
        final Operator operator;
        final JSONArray array;
        
        public NameArrayOpSegment(final String fieldName, final long fieldNameNameHash, final String[] fieldName2, final long[] fieldNameNameHash2, final Function function, final Operator operator, final JSONArray array) {
            super(fieldName, fieldNameNameHash, fieldName2, fieldNameNameHash2, function);
            this.operator = operator;
            this.array = array;
        }
        
        @Override
        boolean apply(final Object fieldValue) {
            if (Objects.requireNonNull(this.operator) == Operator.EQ) {
                return this.array.equals(fieldValue);
            }
            throw new JSONException("not support operator : " + this.operator);
        }
    }
    
    static final class NameObjectOpSegment extends NameFilter
    {
        final Operator operator;
        final JSONObject object;
        
        public NameObjectOpSegment(final String fieldName, final long fieldNameNameHash, final String[] fieldName2, final long[] fieldNameNameHash2, final Function function, final Operator operator, final JSONObject object) {
            super(fieldName, fieldNameNameHash, fieldName2, fieldNameNameHash2, function);
            this.operator = operator;
            this.object = object;
        }
        
        @Override
        boolean apply(final Object fieldValue) {
            if (Objects.requireNonNull(this.operator) == Operator.EQ) {
                return this.object.equals(fieldValue);
            }
            throw new JSONException("not support operator : " + this.operator);
        }
    }
    
    static final class NameStringInSegment extends NameFilter
    {
        private final String[] values;
        private final boolean not;
        
        public NameStringInSegment(final String fieldName, final long fieldNameNameHash, final String[] values, final boolean not) {
            super(fieldName, fieldNameNameHash);
            this.values = values;
            this.not = not;
        }
        
        @Override
        protected boolean applyNull() {
            return this.not;
        }
        
        public boolean apply(final Object fieldValue) {
            for (final String value : this.values) {
                if (value == fieldValue) {
                    return !this.not;
                }
                if (value != null && value.equals(fieldValue)) {
                    return !this.not;
                }
            }
            return this.not;
        }
    }
    
    static final class NameStringContainsSegment extends NameFilter
    {
        private final String[] values;
        private final boolean not;
        
        public NameStringContainsSegment(final String fieldName, final long fieldNameNameHash, final String[] fieldName2, final long[] fieldNameNameHash2, final String[] values, final boolean not) {
            super(fieldName, fieldNameNameHash, fieldName2, fieldNameNameHash2, null);
            this.values = values;
            this.not = not;
        }
        
        public boolean apply(final Object fieldValue) {
            if (fieldValue instanceof Collection) {
                final Collection collection = (Collection)fieldValue;
                boolean containsAll = true;
                for (final String value : this.values) {
                    if (!collection.contains(value)) {
                        containsAll = false;
                        break;
                    }
                }
                if (containsAll) {
                    return !this.not;
                }
            }
            return this.not;
        }
    }
    
    static final class NameMatchFilter extends NameFilter
    {
        final String startsWithValue;
        final String endsWithValue;
        final String[] containsValues;
        final int minLength;
        final boolean not;
        
        public NameMatchFilter(final String fieldName, final long fieldNameNameHash, final String startsWithValue, final String endsWithValue, final String[] containsValues, final boolean not) {
            super(fieldName, fieldNameNameHash);
            this.startsWithValue = startsWithValue;
            this.endsWithValue = endsWithValue;
            this.containsValues = containsValues;
            this.not = not;
            int len = 0;
            if (startsWithValue != null) {
                len += startsWithValue.length();
            }
            if (endsWithValue != null) {
                len += endsWithValue.length();
            }
            if (containsValues != null) {
                for (final String item : containsValues) {
                    len += item.length();
                }
            }
            this.minLength = len;
        }
        
        @Override
        boolean apply(final Object arg) {
            if (!(arg instanceof String)) {
                return false;
            }
            final String fieldValue = (String)arg;
            if (fieldValue.length() < this.minLength) {
                return this.not;
            }
            int start = 0;
            if (this.startsWithValue != null) {
                if (!fieldValue.startsWith(this.startsWithValue)) {
                    return this.not;
                }
                start += this.startsWithValue.length();
            }
            if (this.containsValues != null) {
                for (final String containsValue : this.containsValues) {
                    final int index = fieldValue.indexOf(containsValue, start);
                    if (index == -1) {
                        return this.not;
                    }
                    start = index + containsValue.length();
                }
            }
            if (this.endsWithValue != null && !fieldValue.endsWith(this.endsWithValue)) {
                return this.not;
            }
            return !this.not;
        }
    }
    
    static final class NameExistsFilter extends JSONPathFilter
    {
        final String name;
        final long nameHashCode;
        
        public NameExistsFilter(final String name, final long nameHashCode) {
            this.name = name;
            this.nameHashCode = nameHashCode;
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            final JSONArray array = new JSONArray();
            if (object instanceof List) {
                final List list = (List)object;
                for (int i = 0; i < list.size(); ++i) {
                    final Object item = list.get(i);
                    if (item instanceof Map && ((Map)item).containsKey(this.name)) {
                        array.add(item);
                    }
                }
                context.value = array;
                return;
            }
            if (object instanceof Map) {
                final Map map = (Map)object;
                final Object value = map.get(this.name);
                context.value = ((value != null) ? object : null);
                return;
            }
            if (object instanceof JSONPath.Sequence) {
                final List list = ((JSONPath.Sequence)object).values;
                for (int i = 0; i < list.size(); ++i) {
                    final Object item = list.get(i);
                    if (item instanceof Map && ((Map)item).containsKey(this.name)) {
                        array.add(item);
                    }
                }
                if (context.next != null) {
                    context.value = new JSONPath.Sequence(array);
                }
                else {
                    context.value = array;
                }
                return;
            }
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            this.eval(context);
        }
        
        @Override
        public String toString() {
            return '?' + this.name;
        }
        
        public boolean apply(final JSONPath.Context context, final Object object) {
            throw new UnsupportedOperationException();
        }
    }
    
    static final class NameIntBetweenSegment extends NameFilter
    {
        private final long begin;
        private final long end;
        private final boolean not;
        
        public NameIntBetweenSegment(final String fieldName, final long fieldNameNameHash, final long begin, final long end, final boolean not) {
            super(fieldName, fieldNameNameHash);
            this.begin = begin;
            this.end = end;
            this.not = not;
        }
        
        @Override
        protected boolean applyNull() {
            return this.not;
        }
        
        public boolean apply(final Object fieldValue) {
            if (fieldValue instanceof Byte || fieldValue instanceof Short || fieldValue instanceof Integer || fieldValue instanceof Long) {
                final long fieldValueLong = ((Number)fieldValue).longValue();
                if (fieldValueLong >= this.begin && fieldValueLong <= this.end) {
                    return !this.not;
                }
                return this.not;
            }
            else if (fieldValue instanceof Float || fieldValue instanceof Double) {
                final double fieldValueDouble = ((Number)fieldValue).doubleValue();
                if (fieldValueDouble >= this.begin && fieldValueDouble <= this.end) {
                    return !this.not;
                }
                return this.not;
            }
            else if (fieldValue instanceof BigDecimal) {
                final BigDecimal decimal = (BigDecimal)fieldValue;
                final int cmpBegin = decimal.compareTo(BigDecimal.valueOf(this.begin));
                final int cmpEnd = decimal.compareTo(BigDecimal.valueOf(this.end));
                if (cmpBegin >= 0 && cmpEnd <= 0) {
                    return !this.not;
                }
                return this.not;
            }
            else {
                if (!(fieldValue instanceof BigInteger)) {
                    return this.not;
                }
                final BigInteger bigInt = (BigInteger)fieldValue;
                final int cmpBegin = bigInt.compareTo(BigInteger.valueOf(this.begin));
                final int cmpEnd = bigInt.compareTo(BigInteger.valueOf(this.end));
                if (cmpBegin >= 0 && cmpEnd <= 0) {
                    return !this.not;
                }
                return this.not;
            }
        }
    }
    
    static final class NameLongContainsSegment extends NameFilter
    {
        private final long[] values;
        private final boolean not;
        
        public NameLongContainsSegment(final String fieldName, final long fieldNameNameHash, final String[] fieldName2, final long[] fieldNameNameHash2, final long[] values, final boolean not) {
            super(fieldName, fieldNameNameHash, fieldName2, fieldNameNameHash2, null);
            this.values = values;
            this.not = not;
        }
        
        public boolean apply(final Object fieldValue) {
            if (fieldValue instanceof Collection) {
                final Collection collection = (Collection)fieldValue;
                boolean containsAll = true;
                for (final long value : this.values) {
                    boolean containsItem = false;
                    for (final Object item : collection) {
                        if (item instanceof Byte || item instanceof Short || item instanceof Integer || item instanceof Long) {
                            final long longItem = ((Number)item).longValue();
                            if (longItem == value) {
                                containsItem = true;
                                break;
                            }
                        }
                        if (item instanceof Float && value == (float)item) {
                            containsItem = true;
                            break;
                        }
                        if (item instanceof Double && value == (double)item) {
                            containsItem = true;
                            break;
                        }
                        if (item instanceof BigDecimal) {
                            final BigDecimal decimal = (BigDecimal)item;
                            final long longValue = decimal.longValue();
                            if (value == longValue && decimal.compareTo(BigDecimal.valueOf(value)) == 0) {
                                containsItem = true;
                                break;
                            }
                        }
                        if (!(item instanceof BigInteger)) {
                            continue;
                        }
                        final BigInteger bigiInt = (BigInteger)item;
                        final long longValue = bigiInt.longValue();
                        if (value == longValue && bigiInt.equals(BigInteger.valueOf(value))) {
                            containsItem = true;
                            break;
                        }
                    }
                    if (!containsItem) {
                        containsAll = false;
                        break;
                    }
                }
                if (containsAll) {
                    return !this.not;
                }
            }
            return this.not;
        }
    }
    
    static final class NameIntInSegment extends NameFilter
    {
        private final long[] values;
        private final boolean not;
        
        public NameIntInSegment(final String fieldName, final long fieldNameNameHash, final String[] fieldName2, final long[] fieldNameNameHash2, final Function expr, final long[] values, final boolean not) {
            super(fieldName, fieldNameNameHash, fieldName2, fieldNameNameHash2, expr);
            this.values = values;
            this.not = not;
        }
        
        @Override
        protected boolean applyNull() {
            return this.not;
        }
        
        public boolean apply(final Object fieldValue) {
            if (fieldValue instanceof Byte || fieldValue instanceof Short || fieldValue instanceof Integer || fieldValue instanceof Long) {
                final long fieldValueLong = ((Number)fieldValue).longValue();
                for (final long value : this.values) {
                    if (value == fieldValueLong) {
                        return !this.not;
                    }
                }
                return this.not;
            }
            if (fieldValue instanceof Float || fieldValue instanceof Double) {
                final double fieldValueDouble = ((Number)fieldValue).doubleValue();
                for (final long value : this.values) {
                    if (value == fieldValueDouble) {
                        return !this.not;
                    }
                }
                return this.not;
            }
            if (fieldValue instanceof BigDecimal) {
                final BigDecimal decimal = (BigDecimal)fieldValue;
                final long longValue = decimal.longValue();
                for (final long value2 : this.values) {
                    if (value2 == longValue && decimal.compareTo(BigDecimal.valueOf(value2)) == 0) {
                        return !this.not;
                    }
                }
                return this.not;
            }
            if (fieldValue instanceof BigInteger) {
                final BigInteger bigiInt = (BigInteger)fieldValue;
                final long longValue = bigiInt.longValue();
                for (final long value2 : this.values) {
                    if (value2 == longValue && bigiInt.equals(BigInteger.valueOf(value2))) {
                        return !this.not;
                    }
                }
                return this.not;
            }
            return this.not;
        }
    }
}
