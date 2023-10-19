// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import com.alibaba.fastjson2.util.Fnv;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.List;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import java.util.Iterator;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

class JSONPathSegmentName extends JSONPathSegment
{
    static final long HASH_NAME;
    static final long HASH_ORDINAL;
    final String name;
    final long nameHashCode;
    
    public JSONPathSegmentName(final String name, final long nameHashCode) {
        this.name = name;
        this.nameHashCode = nameHashCode;
    }
    
    @Override
    public boolean remove(final JSONPath.Context context) {
        final Object object = (context.parent == null) ? context.root : context.parent.value;
        if (object instanceof Map) {
            final Map map = (Map)object;
            map.remove(this.name);
            return context.eval = true;
        }
        if (object instanceof Collection) {
            final Collection collection = (Collection)object;
            for (final Object item : collection) {
                if (item == null) {
                    continue;
                }
                if (item instanceof Map) {
                    final Map map2 = (Map)item;
                    map2.remove(this.name);
                }
                else {
                    final ObjectReaderProvider provider = context.path.getReaderContext().getProvider();
                    final ObjectReader objectReader = provider.getObjectReader(item.getClass());
                    final FieldReader fieldReader = objectReader.getFieldReader(this.nameHashCode);
                    if (fieldReader == null) {
                        continue;
                    }
                    fieldReader.accept(item, null);
                }
            }
            return context.eval = true;
        }
        final ObjectReaderProvider provider2 = context.path.getReaderContext().getProvider();
        final ObjectReader objectReader2 = provider2.getObjectReader(object.getClass());
        final FieldReader fieldReader2 = objectReader2.getFieldReader(this.nameHashCode);
        if (fieldReader2 != null) {
            fieldReader2.accept(object, null);
        }
        return context.eval = true;
    }
    
    @Override
    public boolean contains(final JSONPath.Context context) {
        final Object object = (context.parent == null) ? context.root : context.parent.value;
        if (object == null) {
            return false;
        }
        if (object instanceof Map) {
            return ((Map)object).containsKey(this.name);
        }
        if (object instanceof Collection) {
            for (final Object item : (Collection)object) {
                if (item == null) {
                    continue;
                }
                if (item instanceof Map && ((Map)item).get(this.name) != null) {
                    return true;
                }
                final ObjectWriter<?> objectWriter = context.path.getWriterContext().getObjectWriter(item.getClass());
                if (!(objectWriter instanceof ObjectWriterAdapter)) {
                    continue;
                }
                final FieldWriter fieldWriter = objectWriter.getFieldWriter(this.nameHashCode);
                if (fieldWriter != null && fieldWriter.getFieldValue(item) != null) {
                    return true;
                }
            }
            return false;
        }
        if (object instanceof JSONPath.Sequence) {
            final JSONPath.Sequence sequence = (JSONPath.Sequence)object;
            for (final Object item2 : sequence.values) {
                if (item2 == null) {
                    continue;
                }
                if (item2 instanceof Map && ((Map)item2).get(this.name) != null) {
                    return true;
                }
                final ObjectWriter<?> objectWriter2 = context.path.getWriterContext().getObjectWriter(item2.getClass());
                if (!(objectWriter2 instanceof ObjectWriterAdapter)) {
                    continue;
                }
                final FieldWriter fieldWriter2 = objectWriter2.getFieldWriter(this.nameHashCode);
                if (fieldWriter2 != null && fieldWriter2.getFieldValue(item2) != null) {
                    return true;
                }
            }
            return false;
        }
        if (object instanceof Object[]) {
            final Object[] array2;
            final Object[] array = array2 = (Object[])object;
            for (final Object item3 : array2) {
                if (item3 != null) {
                    if (item3 instanceof Map && ((Map)item3).get(this.name) != null) {
                        return true;
                    }
                    final ObjectWriter<?> objectWriter3 = context.path.getWriterContext().getObjectWriter(item3.getClass());
                    if (objectWriter3 instanceof ObjectWriterAdapter) {
                        final FieldWriter fieldWriter3 = objectWriter3.getFieldWriter(this.nameHashCode);
                        if (fieldWriter3 != null && fieldWriter3.getFieldValue(item3) != null) {
                            return true;
                        }
                    }
                }
            }
        }
        final ObjectWriter<?> objectWriter4 = context.path.getWriterContext().getObjectWriter(object.getClass());
        if (objectWriter4 instanceof ObjectWriterAdapter) {
            final FieldWriter fieldWriter4 = objectWriter4.getFieldWriter(this.nameHashCode);
            if (fieldWriter4 != null) {
                return fieldWriter4.getFieldValue(object) != null;
            }
        }
        return false;
    }
    
    @Override
    public void eval(final JSONPath.Context context) {
        final Object object = (context.parent == null) ? context.root : context.parent.value;
        if (object == null) {
            return;
        }
        if (object instanceof Map) {
            final Map map = (Map)object;
            Object value = map.get(this.name);
            if (value == null) {
                final boolean isNum = IOUtils.isNumber(this.name);
                Long longValue = null;
                for (final Object o : map.entrySet()) {
                    final Map.Entry entry = (Map.Entry)o;
                    final Object entryKey = entry.getKey();
                    if (entryKey instanceof Enum && ((Enum)entryKey).name().equals(this.name)) {
                        value = entry.getValue();
                        break;
                    }
                    if (!(entryKey instanceof Long)) {
                        continue;
                    }
                    if (longValue == null && isNum) {
                        longValue = Long.parseLong(this.name);
                    }
                    if (entryKey.equals(longValue)) {
                        value = entry.getValue();
                        break;
                    }
                }
            }
            context.value = value;
            return;
        }
        if (object instanceof Collection) {
            final Collection<?> collection = (Collection<?>)object;
            final int size = collection.size();
            Collection values = null;
            for (final Object item : collection) {
                if (item instanceof Map) {
                    final Object val = ((Map)item).get(this.name);
                    if (val == null) {
                        continue;
                    }
                    if (val instanceof Collection) {
                        if (size == 1) {
                            values = (Collection)val;
                        }
                        else {
                            if (values == null) {
                                values = new JSONArray(size);
                            }
                            values.addAll((Collection)val);
                        }
                    }
                    else {
                        if (values == null) {
                            values = new JSONArray(size);
                        }
                        values.add(val);
                    }
                }
            }
            context.value = values;
            return;
        }
        if (object instanceof JSONPath.Sequence) {
            final List sequence = ((JSONPath.Sequence)object).values;
            final JSONArray values2 = new JSONArray(sequence.size());
            for (final Object o2 : sequence) {
                context.value = o2;
                final JSONPath.Context itemContext = new JSONPath.Context(context.path, context, context.current, context.next, context.readerFeatures);
                this.eval(itemContext);
                final Object val = itemContext.value;
                if (val == null && (context.path.features & JSONPath.Feature.KeepNullValue.mask) == 0x0L) {
                    continue;
                }
                if (val instanceof Collection) {
                    values2.addAll((Collection<?>)val);
                }
                else {
                    values2.add(val);
                }
            }
            if (context.next != null) {
                context.value = new JSONPath.Sequence(values2);
            }
            else {
                context.value = values2;
            }
            context.eval = true;
            return;
        }
        final JSONWriter.Context writerContext = context.path.getWriterContext();
        final ObjectWriter<?> objectWriter = writerContext.getObjectWriter(object.getClass());
        if (objectWriter instanceof ObjectWriterAdapter) {
            final FieldWriter fieldWriter = objectWriter.getFieldWriter(this.nameHashCode);
            if (fieldWriter != null) {
                context.value = fieldWriter.getFieldValue(object);
            }
            return;
        }
        if (this.nameHashCode == JSONPathSegmentName.HASH_NAME && object instanceof Enum) {
            context.value = ((Enum)object).name();
            return;
        }
        if (this.nameHashCode == JSONPathSegmentName.HASH_ORDINAL && object instanceof Enum) {
            context.value = ((Enum)object).ordinal();
            return;
        }
        if (object instanceof String) {
            final String str = (String)object;
            if (!str.isEmpty() && str.charAt(0) == '{') {
                context.value = JSONPath.of("$." + this.name).extract(JSONReader.of(str));
                return;
            }
            context.value = null;
        }
        else {
            if (object instanceof Number || object instanceof Boolean) {
                context.value = null;
                return;
            }
            throw new JSONException("not support : " + object.getClass());
        }
    }
    
    @Override
    public void set(final JSONPath.Context context, Object value) {
        final Object object = (context.parent == null) ? context.root : context.parent.value;
        if (object instanceof Map) {
            final Map map = (Map)object;
            final Object origin = map.put(this.name, value);
            if (origin != null && (context.readerFeatures & JSONReader.Feature.DuplicateKeyValueAsArray.mask) != 0x0L) {
                if (origin instanceof Collection) {
                    ((Collection)origin).add(value);
                    map.put(this.name, value);
                }
                else {
                    final JSONArray array = JSONArray.of(origin, value);
                    map.put(this.name, array);
                }
            }
            return;
        }
        if (object instanceof Collection) {
            final Collection collection = (Collection)object;
            for (final Object item : collection) {
                if (item == null) {
                    continue;
                }
                if (item instanceof Map) {
                    final Map map2 = (Map)item;
                    final Object origin2 = map2.put(this.name, value);
                    if (origin2 == null || (context.readerFeatures & JSONReader.Feature.DuplicateKeyValueAsArray.mask) == 0x0L) {
                        continue;
                    }
                    if (origin2 instanceof Collection) {
                        ((Collection)origin2).add(value);
                        map2.put(this.name, value);
                    }
                    else {
                        final JSONArray array2 = JSONArray.of(origin2, value);
                        map2.put(this.name, array2);
                    }
                }
                else {
                    final ObjectReaderProvider provider = context.path.getReaderContext().getProvider();
                    final ObjectReader objectReader = provider.getObjectReader(item.getClass());
                    final FieldReader fieldReader = objectReader.getFieldReader(this.nameHashCode);
                    if (fieldReader == null) {
                        continue;
                    }
                    fieldReader.accept(item, null);
                }
            }
            return;
        }
        final ObjectReaderProvider provider2 = context.path.getReaderContext().getProvider();
        final ObjectReader objectReader2 = provider2.getObjectReader(object.getClass());
        final FieldReader fieldReader2 = objectReader2.getFieldReader(this.nameHashCode);
        if (fieldReader2 == null) {
            return;
        }
        if (value != null) {
            final Class<?> valueClass = value.getClass();
            final Class fieldClass = fieldReader2.fieldClass;
            if (valueClass != fieldClass) {
                final Function typeConvert = provider2.getTypeConvert(valueClass, fieldClass);
                if (typeConvert != null) {
                    value = typeConvert.apply(value);
                }
            }
        }
        fieldReader2.accept(object, value);
    }
    
    @Override
    public void setCallback(final JSONPath.Context context, final BiFunction callback) {
        final Object object = (context.parent == null) ? context.root : context.parent.value;
        if (object instanceof Map) {
            final Map map = (Map)object;
            final Object origin = map.get(this.name);
            if (origin != null) {
                final Object applyValue = callback.apply(map, origin);
                map.put(this.name, applyValue);
            }
            return;
        }
        final ObjectReaderProvider provider = context.path.getReaderContext().getProvider();
        final ObjectReader objectReader = provider.getObjectReader(object.getClass());
        final ObjectWriter objectWriter = context.path.getWriterContext().provider.getObjectWriter(object.getClass());
        final FieldReader fieldReader = objectReader.getFieldReader(this.nameHashCode);
        final FieldWriter fieldWriter = objectWriter.getFieldWriter(this.nameHashCode);
        if (fieldReader == null || fieldWriter == null) {
            return;
        }
        final Object fieldValue = fieldWriter.getFieldValue(object);
        final Object applyValue2 = callback.apply(object, fieldValue);
        fieldReader.accept(object, applyValue2);
    }
    
    @Override
    public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
        if (context.parent != null && (context.parent.eval || context.parent.current instanceof JSONPathFilter || context.parent.current instanceof MultiIndexSegment)) {
            this.eval(context);
            return;
        }
        if (!jsonReader.jsonb) {
            if (jsonReader.nextIfObjectStart()) {
                if (jsonReader.ch == '}') {
                    jsonReader.next();
                    if (jsonReader.isEnd()) {
                        return;
                    }
                    jsonReader.nextIfComma();
                }
                while (!jsonReader.nextIfObjectEnd()) {
                    final long nameHashCode = jsonReader.readFieldNameHashCode();
                    final boolean match = nameHashCode == this.nameHashCode;
                    if (match) {
                        Object val = null;
                        switch (jsonReader.ch) {
                            case '+':
                            case '-':
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9': {
                                jsonReader.readNumber0();
                                val = jsonReader.getNumber();
                                break;
                            }
                            case '[': {
                                if (context.next != null && !(context.next instanceof EvalSegment) && !(context.next instanceof JSONPathSegmentName) && !(context.next instanceof AllSegment)) {
                                    return;
                                }
                                val = jsonReader.readArray();
                                context.eval = true;
                                break;
                            }
                            case '{': {
                                if (context.next != null && !(context.next instanceof EvalSegment) && !(context.next instanceof AllSegment)) {
                                    return;
                                }
                                val = jsonReader.readObject();
                                context.eval = true;
                                break;
                            }
                            case '\"':
                            case '\'': {
                                val = jsonReader.readString();
                                break;
                            }
                            case 'f':
                            case 't': {
                                val = jsonReader.readBoolValue();
                                break;
                            }
                            case 'n': {
                                jsonReader.readNull();
                                val = null;
                                break;
                            }
                            default: {
                                throw new JSONException("TODO : " + jsonReader.ch);
                            }
                        }
                        context.value = val;
                        return;
                    }
                    jsonReader.skipValue();
                    if (jsonReader.ch != ',') {
                        continue;
                    }
                    jsonReader.next();
                }
                jsonReader.next();
            }
            else if (jsonReader.ch == '[' && context.parent != null && context.parent.current instanceof AllSegment) {
                jsonReader.next();
                final List values = new JSONArray();
                while (jsonReader.ch != '\u001a') {
                    if (jsonReader.ch == ']') {
                        jsonReader.next();
                        break;
                    }
                    Label_1445: {
                        if (jsonReader.ch == '{') {
                            jsonReader.next();
                            while (jsonReader.ch != '}') {
                                final long nameHashCode2 = jsonReader.readFieldNameHashCode();
                                final boolean match2 = nameHashCode2 == this.nameHashCode;
                                if (!match2) {
                                    jsonReader.skipValue();
                                    if (jsonReader.ch != ',') {
                                        continue;
                                    }
                                    jsonReader.next();
                                }
                                else {
                                    Object val2 = null;
                                    switch (jsonReader.ch) {
                                        case '+':
                                        case '-':
                                        case '.':
                                        case '0':
                                        case '1':
                                        case '2':
                                        case '3':
                                        case '4':
                                        case '5':
                                        case '6':
                                        case '7':
                                        case '8':
                                        case '9': {
                                            jsonReader.readNumber0();
                                            val2 = jsonReader.getNumber();
                                            break;
                                        }
                                        case '[': {
                                            if (context.next != null) {
                                                break Label_1445;
                                            }
                                            val2 = jsonReader.readArray();
                                            break;
                                        }
                                        case '{': {
                                            if (context.next != null) {
                                                break Label_1445;
                                            }
                                            val2 = jsonReader.readObject();
                                            break;
                                        }
                                        case '\"':
                                        case '\'': {
                                            val2 = jsonReader.readString();
                                            break;
                                        }
                                        case 'f':
                                        case 't': {
                                            val2 = jsonReader.readBoolValue();
                                            break;
                                        }
                                        case 'n': {
                                            jsonReader.readNull();
                                            val2 = null;
                                            break;
                                        }
                                        default: {
                                            throw new JSONException("TODO : " + jsonReader.ch);
                                        }
                                    }
                                    values.add(val2);
                                }
                            }
                            jsonReader.next();
                        }
                        else {
                            jsonReader.skipValue();
                        }
                    }
                    if (jsonReader.ch != ',') {
                        continue;
                    }
                    jsonReader.next();
                }
                context.value = values;
            }
            return;
        }
        if (jsonReader.nextIfObjectStart()) {
            int i = 0;
            while (!jsonReader.nextIfObjectEnd()) {
                final long nameHashCode2 = jsonReader.readFieldNameHashCode();
                if (nameHashCode2 != 0L) {
                    final boolean match2 = nameHashCode2 == this.nameHashCode;
                    if (match2) {
                        if ((!jsonReader.isArray() && !jsonReader.isObject()) || context.next == null) {
                            context.value = jsonReader.readAny();
                            context.eval = true;
                        }
                        return;
                    }
                    jsonReader.skipValue();
                }
                ++i;
            }
            return;
        }
        if (jsonReader.isArray() && context.parent != null && context.parent.current instanceof AllSegment) {
            final List values = new JSONArray();
            for (int itemCnt = jsonReader.startArray(), j = 0; j < itemCnt; ++j) {
                if (jsonReader.nextIfMatch((byte)(-90))) {
                    int k = 0;
                    while (!jsonReader.nextIfMatch((byte)(-91))) {
                        final long nameHashCode3 = jsonReader.readFieldNameHashCode();
                        final boolean match3 = nameHashCode3 == this.nameHashCode;
                        if (!match3) {
                            jsonReader.skipValue();
                        }
                        else {
                            if ((jsonReader.isArray() || jsonReader.isObject()) && context.next != null) {
                                break;
                            }
                            values.add(jsonReader.readAny());
                        }
                        ++k;
                    }
                }
                else {
                    jsonReader.skipValue();
                }
            }
            context.value = values;
            context.eval = true;
            return;
        }
        throw new JSONException("TODO");
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final JSONPathSegmentName that = (JSONPathSegmentName)o;
        return this.nameHashCode == that.nameHashCode && Objects.equals(this.name, that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.nameHashCode);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    static {
        HASH_NAME = Fnv.hashCode64("name");
        HASH_ORDINAL = Fnv.hashCode64("ordinal");
    }
}
