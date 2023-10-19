// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderBean;
import java.lang.reflect.Type;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Optional;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Iterator;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import java.util.Map;
import com.alibaba.fastjson2.util.Fnv;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

abstract class JSONPathSegment
{
    public abstract void accept(final JSONReader p0, final JSONPath.Context p1);
    
    public abstract void eval(final JSONPath.Context p0);
    
    public boolean contains(final JSONPath.Context context) {
        this.eval(context);
        return context.value != null;
    }
    
    public boolean remove(final JSONPath.Context context) {
        throw new JSONException("UnsupportedOperation " + this.getClass());
    }
    
    public void set(final JSONPath.Context context, final Object value) {
        throw new JSONException("UnsupportedOperation " + this.getClass());
    }
    
    public void setCallback(final JSONPath.Context context, final BiFunction callback) {
        throw new JSONException("UnsupportedOperation " + this.getClass());
    }
    
    public void setInt(final JSONPath.Context context, final int value) {
        this.set(context, value);
    }
    
    public void setLong(final JSONPath.Context context, final long value) {
        this.set(context, value);
    }
    
    static final class RandomIndexSegment extends JSONPathSegment
    {
        public static final RandomIndexSegment INSTANCE;
        Random random;
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            if (context.parent != null && (context.parent.eval || (context.parent.current instanceof CycleNameSegment && context.next == null))) {
                this.eval(context);
                return;
            }
            if (jsonReader.jsonb) {
                final JSONArray array = new JSONArray();
                for (int itemCnt = jsonReader.startArray(), i = 0; i < itemCnt; ++i) {
                    array.add(jsonReader.readAny());
                }
                if (this.random == null) {
                    this.random = new Random();
                }
                final int index = Math.abs(this.random.nextInt()) % array.size();
                context.value = array.get(index);
                context.eval = true;
                return;
            }
            final JSONArray array = new JSONArray();
            jsonReader.next();
            int j = 0;
            while (jsonReader.ch != '\u001a') {
                if (jsonReader.ch == ']') {
                    jsonReader.next();
                    break;
                }
                Object val = null;
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
                        val = jsonReader.getNumber();
                        break;
                    }
                    case '[': {
                        val = jsonReader.readArray();
                        break;
                    }
                    case '{': {
                        val = jsonReader.readObject();
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
                array.add(val);
                ++j;
            }
            if (this.random == null) {
                this.random = new Random();
            }
            final int index = Math.abs(this.random.nextInt()) % array.size();
            context.value = array.get(index);
            context.eval = true;
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof List) {
                final List list = (List)object;
                if (list.isEmpty()) {
                    return;
                }
                if (this.random == null) {
                    this.random = new Random();
                }
                final int randomIndex = Math.abs(this.random.nextInt()) % list.size();
                context.value = list.get(randomIndex);
                context.eval = true;
            }
            else {
                if (!(object instanceof Object[])) {
                    throw new JSONException("TODO");
                }
                final Object[] array = (Object[])object;
                if (array.length == 0) {
                    return;
                }
                if (this.random == null) {
                    this.random = new Random();
                }
                final int randomIndex = this.random.nextInt() % array.length;
                context.value = array[randomIndex];
                context.eval = true;
            }
        }
        
        static {
            INSTANCE = new RandomIndexSegment();
        }
    }
    
    static final class RangeIndexSegment extends JSONPathSegment
    {
        final int begin;
        final int end;
        
        public RangeIndexSegment(final int begin, final int end) {
            this.begin = begin;
            this.end = end;
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            final List result = new JSONArray();
            if (object instanceof List) {
                final List list = (List)object;
                for (int i = 0, size = list.size(); i < size; ++i) {
                    boolean match;
                    if (this.begin >= 0) {
                        match = (i >= this.begin && i < this.end);
                    }
                    else {
                        final int ni = i - size;
                        match = (ni >= this.begin && ni < this.end);
                    }
                    if (match) {
                        result.add(list.get(i));
                    }
                }
                context.value = result;
                context.eval = true;
                return;
            }
            if (object instanceof Object[]) {
                final Object[] array = (Object[])object;
                for (int i = 0; i < array.length; ++i) {
                    final boolean match2 = (i >= this.begin && i <= this.end) || (i - array.length > this.begin && i - array.length <= this.end);
                    if (match2) {
                        result.add(array[i]);
                    }
                }
                context.value = result;
                context.eval = true;
                return;
            }
            throw new JSONException("TODO");
        }
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            if (context.parent != null && (context.parent.eval || (context.parent.current instanceof CycleNameSegment && context.next == null))) {
                this.eval(context);
                return;
            }
            if (jsonReader.jsonb) {
                final JSONArray array = new JSONArray();
                for (int itemCnt = jsonReader.startArray(), i = 0; i < itemCnt; ++i) {
                    final boolean match = this.begin < 0 || (i >= this.begin && i < this.end);
                    if (!match) {
                        jsonReader.skipValue();
                    }
                    else {
                        array.add(jsonReader.readAny());
                    }
                }
                if (this.begin < 0) {
                    final int size = array.size();
                    for (int i = size - 1; i >= 0; --i) {
                        final int ni = i - size;
                        if (ni < this.begin || ni >= this.end) {
                            array.remove(i);
                        }
                    }
                }
                context.value = array;
                context.eval = true;
                return;
            }
            final JSONArray array = new JSONArray();
            jsonReader.next();
            int j = 0;
            while (jsonReader.ch != '\u001a') {
                if (jsonReader.ch == ']') {
                    jsonReader.next();
                    break;
                }
                final boolean match2 = this.begin < 0 || (j >= this.begin && j < this.end);
                if (!match2) {
                    jsonReader.skipValue();
                    if (jsonReader.ch == ',') {
                        jsonReader.next();
                    }
                }
                else {
                    Object val = null;
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
                            val = jsonReader.getNumber();
                            break;
                        }
                        case '[': {
                            val = jsonReader.readArray();
                            break;
                        }
                        case '{': {
                            val = jsonReader.readObject();
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
                    array.add(val);
                }
                ++j;
            }
            if (this.begin < 0) {
                final int size = array.size();
                for (int i = size - 1; i >= 0; --i) {
                    final int ni = i - size;
                    if (ni < this.begin || ni >= this.end) {
                        array.remove(i);
                    }
                }
            }
            context.value = array;
            context.eval = true;
        }
        
        @Override
        public void set(final JSONPath.Context context, final Object value) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof List) {
                final List list = (List)object;
                for (int i = 0, size = list.size(); i < size; ++i) {
                    boolean match;
                    if (this.begin >= 0) {
                        match = (i >= this.begin && i < this.end);
                    }
                    else {
                        final int ni = i - size;
                        match = (ni >= this.begin && ni < this.end);
                    }
                    if (match) {
                        list.set(i, value);
                    }
                }
                return;
            }
            throw new JSONException("UnsupportedOperation " + this.getClass());
        }
        
        @Override
        public boolean remove(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof List) {
                final List list = (List)object;
                int removeCount = 0;
                final int size = list.size();
                for (int i = size - 1; i >= 0; --i) {
                    boolean match;
                    if (this.begin >= 0) {
                        match = (i >= this.begin && i < this.end);
                    }
                    else {
                        final int ni = i - size;
                        match = (ni >= this.begin && ni < this.end);
                    }
                    if (match) {
                        list.remove(i);
                        ++removeCount;
                    }
                }
                return removeCount > 0;
            }
            throw new JSONException("UnsupportedOperation " + this.getClass());
        }
    }
    
    static final class MultiIndexSegment extends JSONPathSegment
    {
        final int[] indexes;
        
        public MultiIndexSegment(final int[] indexes) {
            this.indexes = indexes;
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            final List<Object> result = new JSONArray();
            if (object instanceof JSONPath.Sequence) {
                final List list = ((JSONPath.Sequence)object).values;
                for (int i = 0; i < list.size(); ++i) {
                    final Object item = list.get(i);
                    context.value = item;
                    final JSONPath.Context itemContext = new JSONPath.Context(context.path, context, context.current, context.next, context.readerFeatures);
                    this.eval(itemContext);
                    final Object value = itemContext.value;
                    if (value instanceof Collection) {
                        result.addAll((Collection<?>)value);
                    }
                    else {
                        result.add(value);
                    }
                }
                context.value = result;
                return;
            }
            for (final int index : this.indexes) {
                Label_0352: {
                    Object value;
                    if (object instanceof List) {
                        final List list2 = (List)object;
                        if (index >= 0) {
                            if (index >= list2.size()) {
                                break Label_0352;
                            }
                            value = list2.get(index);
                        }
                        else {
                            final int itemIndex = list2.size() + index;
                            if (itemIndex < 0) {
                                break Label_0352;
                            }
                            value = list2.get(itemIndex);
                        }
                    }
                    else {
                        if (!(object instanceof Object[])) {
                            break Label_0352;
                        }
                        final Object[] array = (Object[])object;
                        if (index >= 0) {
                            if (index >= array.length) {
                                break Label_0352;
                            }
                            value = array[index];
                        }
                        else {
                            final int itemIndex = array.length + index;
                            if (itemIndex < 0) {
                                break Label_0352;
                            }
                            value = array[itemIndex];
                        }
                    }
                    if (value instanceof Collection) {
                        result.addAll((Collection<?>)value);
                    }
                    else {
                        result.add(value);
                    }
                }
            }
            context.value = result;
        }
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            if (context.parent != null && context.parent.current instanceof CycleNameSegment && context.next == null) {
                this.eval(context);
                return;
            }
            if (jsonReader.jsonb) {
                final JSONArray array = new JSONArray();
                for (int itemCnt = jsonReader.startArray(), i = 0; i < itemCnt; ++i) {
                    final boolean match = Arrays.binarySearch(this.indexes, i) >= 0;
                    if (!match) {
                        jsonReader.skipValue();
                    }
                    else {
                        array.add(jsonReader.readAny());
                    }
                }
                context.value = array;
                return;
            }
            final JSONArray array = new JSONArray();
            jsonReader.next();
            int j = 0;
            while (jsonReader.ch != '\u001a') {
                if (jsonReader.ch == ']') {
                    jsonReader.next();
                    break;
                }
                final boolean match2 = Arrays.binarySearch(this.indexes, j) >= 0;
                if (!match2) {
                    jsonReader.skipValue();
                    if (jsonReader.ch == ',') {
                        jsonReader.next();
                    }
                }
                else {
                    Object val = null;
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
                            val = jsonReader.getNumber();
                            break;
                        }
                        case '[': {
                            val = jsonReader.readArray();
                            break;
                        }
                        case '{': {
                            val = jsonReader.readObject();
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
                    array.add(val);
                }
                ++j;
            }
            context.value = array;
        }
    }
    
    static final class MultiNameSegment extends JSONPathSegment
    {
        final String[] names;
        final long[] nameHashCodes;
        final Set<String> nameSet;
        
        public MultiNameSegment(final String[] names) {
            this.names = names;
            this.nameHashCodes = new long[names.length];
            this.nameSet = new HashSet<String>();
            for (int i = 0; i < names.length; ++i) {
                this.nameHashCodes[i] = Fnv.hashCode64(names[i]);
                this.nameSet.add(names[i]);
            }
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof Map) {
                final Map map = (Map)object;
                final JSONArray array = new JSONArray(this.names.length);
                for (final String name : this.names) {
                    final Object value = map.get(name);
                    array.add(value);
                }
                context.value = array;
                return;
            }
            if (object instanceof Collection) {
                context.value = object;
                return;
            }
            final ObjectWriterProvider provider = context.path.getWriterContext().provider;
            final ObjectWriter objectWriter = provider.getObjectWriter(object.getClass());
            final JSONArray array2 = new JSONArray(this.names.length);
            for (int i = 0; i < this.names.length; ++i) {
                final FieldWriter fieldWriter = objectWriter.getFieldWriter(this.nameHashCodes[i]);
                Object fieldValue = null;
                if (fieldWriter != null) {
                    fieldValue = fieldWriter.getFieldValue(object);
                }
                array2.add(fieldValue);
            }
            context.value = array2;
        }
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            if (context.parent != null && (context.parent.eval || context.parent.current instanceof JSONPathFilter || context.parent.current instanceof MultiIndexSegment)) {
                this.eval(context);
                return;
            }
            final Object object = jsonReader.readAny();
            if (object instanceof Map) {
                final Map map = (Map)object;
                final JSONArray array = new JSONArray(this.names.length);
                for (final String name : this.names) {
                    final Object value = map.get(name);
                    array.add(value);
                }
                context.value = array;
                return;
            }
            if (!(object instanceof Collection)) {
                final ObjectWriterProvider provider = context.path.getWriterContext().provider;
                final ObjectWriter objectWriter = provider.getObjectWriter(object.getClass());
                final JSONArray array2 = new JSONArray(this.names.length);
                for (int i = 0; i < this.names.length; ++i) {
                    final FieldWriter fieldWriter = objectWriter.getFieldWriter(this.nameHashCodes[i]);
                    Object fieldValue = null;
                    if (fieldWriter != null) {
                        fieldValue = fieldWriter.getFieldValue(object);
                    }
                    array2.add(fieldValue);
                }
                context.value = array2;
                return;
            }
            if (context.next == null) {
                final Collection collection = (Collection)object;
                final JSONArray collectionArray = new JSONArray(collection.size());
                for (final Object item : collection) {
                    if (item instanceof Map) {
                        final Map map2 = (Map)item;
                        final JSONArray array3 = new JSONArray(this.names.length);
                        for (final String name2 : this.names) {
                            final Object value2 = map2.get(name2);
                            array3.add(value2);
                        }
                        ((ArrayList<JSONArray>)collectionArray).add(array3);
                    }
                }
                context.value = collectionArray;
                return;
            }
            context.value = object;
        }
    }
    
    static final class AllSegment extends JSONPathSegment
    {
        static final AllSegment INSTANCE;
        static final AllSegment INSTANCE_ARRAY;
        final boolean array;
        
        AllSegment(final boolean array) {
            this.array = array;
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object == null) {
                context.value = null;
                context.eval = true;
                return;
            }
            if (object instanceof Map) {
                final Map map = (Map)object;
                final JSONArray array = new JSONArray(map.size());
                for (final Object value : map.values()) {
                    if (this.array && value instanceof Collection) {
                        array.addAll((Collection<?>)value);
                    }
                    else {
                        array.add(value);
                    }
                }
                if (context.next != null) {
                    context.value = new JSONPath.Sequence(array);
                }
                else {
                    context.value = array;
                }
                context.eval = true;
                return;
            }
            if (object instanceof List) {
                final List list = (List)object;
                final JSONArray values = new JSONArray(list.size());
                if (context.next == null && !this.array) {
                    for (int i = 0; i < list.size(); ++i) {
                        final Object item = list.get(i);
                        if (item instanceof Map) {
                            values.addAll(((Map)item).values());
                        }
                        else {
                            values.add(item);
                        }
                    }
                    context.value = values;
                    context.eval = true;
                    return;
                }
                if (context.next != null) {
                    context.value = new JSONPath.Sequence(list);
                }
                else {
                    context.value = object;
                }
                context.eval = true;
            }
            else {
                if (object instanceof Collection) {
                    context.value = object;
                    context.eval = true;
                    return;
                }
                if (!(object instanceof JSONPath.Sequence)) {
                    final ObjectWriterProvider provider = context.path.getWriterContext().provider;
                    final ObjectWriter objectWriter = provider.getObjectWriter(object.getClass());
                    final List<FieldWriter> fieldWriters = (List<FieldWriter>)objectWriter.getFieldWriters();
                    final int size = fieldWriters.size();
                    final JSONArray array2 = new JSONArray(size);
                    for (int j = 0; j < size; ++j) {
                        final Object fieldValue = fieldWriters.get(j).getFieldValue(object);
                        array2.add(fieldValue);
                    }
                    context.value = array2;
                    context.eval = true;
                    return;
                }
                final List list = ((JSONPath.Sequence)object).values;
                final JSONArray values = new JSONArray(list.size());
                if (context.next == null) {
                    for (int i = 0; i < list.size(); ++i) {
                        final Object item = list.get(i);
                        if (item instanceof Map && !this.array) {
                            values.addAll(((Map)item).values());
                        }
                        else if (item instanceof Collection) {
                            final Collection collection = (Collection)item;
                            values.addAll(collection);
                        }
                        else {
                            values.add(item);
                        }
                    }
                    context.value = values;
                    context.eval = true;
                    return;
                }
                context.value = new JSONPath.Sequence(list);
                context.eval = true;
            }
        }
        
        @Override
        public boolean remove(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof Map) {
                ((Map)object).clear();
                return true;
            }
            if (object instanceof Collection) {
                ((Collection)object).clear();
                return true;
            }
            throw new JSONException("UnsupportedOperation " + this.getClass());
        }
        
        @Override
        public void set(final JSONPath.Context context, final Object value) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof Map) {
                final Map map = (Map)object;
                for (final Object o : map.entrySet()) {
                    final Map.Entry entry = (Map.Entry)o;
                    entry.setValue(value);
                }
                return;
            }
            if (object instanceof List) {
                Collections.fill((List<? super Object>)object, value);
                return;
            }
            if (object != null && object.getClass().isArray()) {
                for (int len = Array.getLength(object), i = 0; i < len; ++i) {
                    Array.set(object, i, value);
                }
                return;
            }
            throw new JSONException("UnsupportedOperation " + this.getClass());
        }
        
        @Override
        public void setCallback(final JSONPath.Context context, final BiFunction callback) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof Map) {
                final Map map = (Map)object;
                for (final Object o : map.entrySet()) {
                    final Map.Entry entry = (Map.Entry)o;
                    final Object value = entry.getValue();
                    final Object apply = callback.apply(object, value);
                    if (apply != value) {
                        entry.setValue(apply);
                    }
                }
                return;
            }
            if (object instanceof List) {
                final List list = (List)object;
                for (int i = 0; i < list.size(); ++i) {
                    final Object value2 = list.get(i);
                    final Object apply2 = callback.apply(object, value2);
                    if (apply2 != value2) {
                        list.set(i, apply2);
                    }
                }
                return;
            }
            if (object != null && object.getClass().isArray()) {
                for (int len = Array.getLength(object), i = 0; i < len; ++i) {
                    final Object value2 = Array.get(object, i);
                    final Object apply2 = callback.apply(object, value2);
                    if (apply2 != value2) {
                        Array.set(object, i, apply2);
                    }
                }
                return;
            }
            throw new JSONException("UnsupportedOperation " + this.getClass());
        }
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            if (context.parent != null && context.parent.eval) {
                this.eval(context);
                return;
            }
            if (jsonReader.isEnd()) {
                context.eval = true;
                return;
            }
            if (jsonReader.jsonb) {
                final List<Object> values = new JSONArray();
                if (jsonReader.nextIfMatch((byte)(-90))) {
                    while (!jsonReader.nextIfMatch((byte)(-91))) {
                        if (jsonReader.skipName()) {
                            final Object val = jsonReader.readAny();
                            if (this.array && val instanceof Collection) {
                                values.addAll((Collection<?>)val);
                            }
                            else {
                                values.add(val);
                            }
                        }
                    }
                    context.value = values;
                    return;
                }
                if (jsonReader.isArray() && context.next != null) {
                    return;
                }
                throw new JSONException("TODO");
            }
            else {
                final boolean alwaysReturnList = context.next == null && (context.path.features & JSONPath.Feature.AlwaysReturnList.mask) != 0x0L;
                final List<Object> values2 = new JSONArray();
                if (jsonReader.nextIfObjectStart()) {
                    while (true) {
                        while (jsonReader.ch != '}') {
                            jsonReader.skipName();
                            Object val2 = null;
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
                                    val2 = jsonReader.getNumber();
                                    break;
                                }
                                case '[': {
                                    val2 = jsonReader.readArray();
                                    break;
                                }
                                case '{': {
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
                                case ']': {
                                    jsonReader.next();
                                    context.value = values2;
                                    context.eval = true;
                                    return;
                                }
                                default: {
                                    throw new JSONException("TODO : " + jsonReader.ch);
                                }
                            }
                            if (val2 instanceof Collection) {
                                if (alwaysReturnList) {
                                    values2.add(val2);
                                }
                                else {
                                    values2.addAll((Collection<?>)val2);
                                }
                            }
                            else {
                                values2.add(val2);
                            }
                            if (jsonReader.ch == ',') {
                                jsonReader.next();
                            }
                        }
                        jsonReader.next();
                        continue;
                    }
                }
                if (jsonReader.ch == '[') {
                    jsonReader.next();
                    while (jsonReader.ch != ']') {
                        final Object value = jsonReader.readAny();
                        values2.add(value);
                        if (jsonReader.ch == ',') {
                            jsonReader.next();
                        }
                    }
                    jsonReader.next();
                    if (context.next != null) {
                        context.value = new JSONPath.Sequence(values2);
                    }
                    else {
                        context.value = values2;
                    }
                    context.eval = true;
                    return;
                }
                throw new JSONException("TODO");
            }
        }
        
        static {
            INSTANCE = new AllSegment(false);
            INSTANCE_ARRAY = new AllSegment(true);
        }
    }
    
    static final class SelfSegment extends JSONPathSegment
    {
        static final SelfSegment INSTANCE;
        
        private SelfSegment() {
        }
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            context.value = jsonReader.readAny();
            context.eval = true;
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            context.value = ((context.parent == null) ? context.root : context.parent.value);
        }
        
        static {
            INSTANCE = new SelfSegment();
        }
    }
    
    static final class RootSegment extends JSONPathSegment
    {
        static final RootSegment INSTANCE;
        
        private RootSegment() {
        }
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            if (context.parent != null) {
                throw new JSONException("not support operation");
            }
            context.value = jsonReader.readAny();
            context.eval = true;
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            context.value = ((context.parent == null) ? context.root : context.parent.root);
        }
        
        static {
            INSTANCE = new RootSegment();
        }
    }
    
    static final class CycleNameSegment extends JSONPathSegment
    {
        static final long HASH_STAR;
        static final long HASH_EMPTY;
        final String name;
        final long nameHashCode;
        
        public CycleNameSegment(final String name, final long nameHashCode) {
            this.name = name;
            this.nameHashCode = nameHashCode;
        }
        
        @Override
        public String toString() {
            return ".." + this.name;
        }
        
        @Override
        public boolean remove(final JSONPath.Context context) {
            this.set(context, null);
            return context.eval = true;
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            final List values = new JSONArray();
            Consumer action;
            if (this.nameHashCode == CycleNameSegment.HASH_STAR || this.nameHashCode == CycleNameSegment.HASH_EMPTY) {
                action = new MapRecursive(context, values, 0);
            }
            else {
                action = new MapLoop(context, values);
            }
            action.accept(object);
            if (values.size() == 1 && values.get(0) instanceof Collection) {
                context.value = values.get(0);
            }
            else {
                context.value = values;
            }
            if (context.value instanceof List && context.next instanceof JSONPathFilter) {
                context.value = new JSONPath.Sequence((List)context.value);
            }
            context.eval = true;
        }
        
        @Override
        public void set(final JSONPath.Context context, final Object value) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            final LoopSet action = new LoopSet(context, value);
            action.accept(object);
        }
        
        @Override
        public void setCallback(final JSONPath.Context context, final BiFunction callback) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            final LoopCallback action = new LoopCallback(context, callback);
            action.accept(object);
        }
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            final List values = new JSONArray();
            this.accept(jsonReader, context, values);
            context.value = values;
            context.eval = true;
        }
        
        public void accept(final JSONReader jsonReader, final JSONPath.Context context, final List<Object> values) {
            if (!jsonReader.jsonb) {
                if (jsonReader.ch == '{') {
                    jsonReader.next();
                    while (jsonReader.ch != '}') {
                        final long nameHashCode = jsonReader.readFieldNameHashCode();
                        final boolean match = nameHashCode == this.nameHashCode;
                        final char ch = jsonReader.ch;
                        if (!match && ch != '{' && ch != '[') {
                            jsonReader.skipValue();
                        }
                        else {
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
                                case '[':
                                case '{': {
                                    if (match) {
                                        val = ((ch == '[') ? jsonReader.readArray() : jsonReader.readObject());
                                        break;
                                    }
                                    this.accept(jsonReader, context, values);
                                    continue;
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
                            if (val instanceof Collection) {
                                values.addAll((Collection<?>)val);
                            }
                            else {
                                values.add(val);
                            }
                            if (jsonReader.ch != ',') {
                                continue;
                            }
                            jsonReader.next();
                        }
                    }
                    jsonReader.next();
                    if (jsonReader.ch == ',') {
                        jsonReader.next();
                    }
                }
                else {
                    if (jsonReader.ch == '[') {
                        jsonReader.next();
                        while (true) {
                            while (jsonReader.ch != ']') {
                                if (jsonReader.ch == '{' || jsonReader.ch == '[') {
                                    this.accept(jsonReader, context, values);
                                }
                                else {
                                    jsonReader.skipValue();
                                }
                                if (jsonReader.ch == ',') {
                                    jsonReader.next();
                                    if (jsonReader.ch == ',') {
                                        jsonReader.next();
                                    }
                                    return;
                                }
                            }
                            jsonReader.next();
                            continue;
                        }
                    }
                    jsonReader.skipValue();
                }
                return;
            }
            if (jsonReader.nextIfMatch((byte)(-90))) {
                while (!jsonReader.nextIfMatch((byte)(-91))) {
                    final long nameHashCode = jsonReader.readFieldNameHashCode();
                    if (nameHashCode == 0L) {
                        continue;
                    }
                    final boolean match = nameHashCode == this.nameHashCode;
                    if (match) {
                        if (jsonReader.isArray()) {
                            values.addAll(jsonReader.readArray());
                        }
                        else {
                            values.add(jsonReader.readAny());
                        }
                    }
                    else if (jsonReader.isObject() || jsonReader.isArray()) {
                        this.accept(jsonReader, context, values);
                    }
                    else {
                        jsonReader.skipValue();
                    }
                }
                return;
            }
            if (jsonReader.isArray()) {
                for (int itemCnt = jsonReader.startArray(), i = 0; i < itemCnt; ++i) {
                    if (jsonReader.isObject() || jsonReader.isArray()) {
                        this.accept(jsonReader, context, values);
                    }
                    else {
                        jsonReader.skipValue();
                    }
                }
            }
            else {
                jsonReader.skipValue();
            }
        }
        
        static {
            HASH_STAR = Fnv.hashCode64("*");
            HASH_EMPTY = Fnv.hashCode64("");
        }
        
        class MapLoop implements BiConsumer, Consumer
        {
            final JSONPath.Context context;
            final List values;
            
            public MapLoop(final JSONPath.Context context, final List values) {
                this.context = context;
                this.values = values;
            }
            
            @Override
            public void accept(final Object key, final Object value) {
                if (CycleNameSegment.this.name.equals(key)) {
                    this.values.add(value);
                }
                if (value instanceof Map) {
                    ((Map)value).forEach(this);
                }
                else if (value instanceof List) {
                    ((List)value).forEach(this);
                }
                else if (CycleNameSegment.this.nameHashCode == CycleNameSegment.HASH_STAR) {
                    this.values.add(value);
                }
            }
            
            @Override
            public void accept(final Object value) {
                if (value == null) {
                    return;
                }
                if (value instanceof Map) {
                    ((Map)value).forEach(this);
                }
                else if (value instanceof List) {
                    ((List)value).forEach(this);
                }
                else {
                    final ObjectWriter<?> objectWriter = this.context.path.getWriterContext().getObjectWriter(value.getClass());
                    if (objectWriter instanceof ObjectWriterAdapter) {
                        FieldWriter fieldWriter = objectWriter.getFieldWriter(CycleNameSegment.this.nameHashCode);
                        if (fieldWriter != null) {
                            final Object fieldValue = fieldWriter.getFieldValue(value);
                            if (fieldValue != null) {
                                this.values.add(fieldValue);
                            }
                            return;
                        }
                        for (int i = 0; i < objectWriter.getFieldWriters().size(); ++i) {
                            fieldWriter = objectWriter.getFieldWriters().get(i);
                            final Object fieldValue2 = fieldWriter.getFieldValue(value);
                            this.accept(fieldValue2);
                        }
                    }
                    else if (CycleNameSegment.this.nameHashCode == CycleNameSegment.HASH_STAR) {
                        this.values.add(value);
                    }
                }
            }
        }
        
        static final class MapRecursive implements Consumer
        {
            static final int maxLevel = 2048;
            final JSONPath.Context context;
            final List values;
            final int level;
            
            public MapRecursive(final JSONPath.Context context, final List values, final int level) {
                this.context = context;
                this.values = values;
                this.level = level;
            }
            
            @Override
            public void accept(final Object value) {
                if (this.level >= 2048) {
                    throw new JSONException("level too large");
                }
                if (value instanceof Map) {
                    final Collection collection = ((Map)value).values();
                    this.values.addAll(collection);
                    collection.forEach(this);
                }
                else if (value instanceof Collection) {
                    final Collection collection = (Collection)value;
                    this.values.addAll(collection);
                    collection.forEach(this);
                }
                else if (value != null) {
                    final ObjectWriter<?> objectWriter = this.context.path.getWriterContext().getObjectWriter(value.getClass());
                    if (objectWriter instanceof ObjectWriterAdapter) {
                        final ObjectWriterAdapter writerAdapter = (ObjectWriterAdapter)objectWriter;
                        final Object temp = Optional.ofNullable(writerAdapter.getFieldWriters()).orElseGet(() -> new ArrayList()).stream().filter(Objects::nonNull).map(v -> v.getFieldValue(value)).collect((Collector<? super Object, ?, List<Object>>)Collectors.toList());
                        this.accept(temp);
                    }
                }
            }
        }
        
        class LoopSet
        {
            final JSONPath.Context context;
            final Object value;
            
            public LoopSet(final JSONPath.Context context, final Object value) {
                this.context = context;
                this.value = value;
            }
            
            public void accept(final Object object) {
                if (object instanceof Map) {
                    for (final Map.Entry entry : ((Map)object).entrySet()) {
                        if (CycleNameSegment.this.name.equals(entry.getKey())) {
                            entry.setValue(this.value);
                            this.context.eval = true;
                        }
                        else {
                            final Object entryValue = entry.getValue();
                            if (entryValue == null) {
                                continue;
                            }
                            this.accept(entryValue);
                        }
                    }
                }
                else if (object instanceof Collection) {
                    for (final Object item : (List)object) {
                        this.accept(item);
                    }
                }
                else {
                    final Class<?> entryValueClass = object.getClass();
                    final ObjectReader objectReader = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(entryValueClass);
                    if (objectReader instanceof ObjectReaderBean) {
                        final FieldReader fieldReader = objectReader.getFieldReader(CycleNameSegment.this.nameHashCode);
                        if (fieldReader != null) {
                            fieldReader.accept(object, this.value);
                            this.context.eval = true;
                            return;
                        }
                    }
                    final ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter(entryValueClass);
                    final List<FieldWriter> fieldWriters = (List<FieldWriter>)objectWriter.getFieldWriters();
                    for (final FieldWriter fieldWriter : fieldWriters) {
                        final Object fieldValue = fieldWriter.getFieldValue(object);
                        this.accept(fieldValue);
                    }
                }
            }
        }
        
        class LoopCallback
        {
            final JSONPath.Context context;
            final BiFunction callback;
            
            public LoopCallback(final JSONPath.Context context, final BiFunction callback) {
                this.context = context;
                this.callback = callback;
            }
            
            public void accept(final Object object) {
                if (object instanceof Map) {
                    for (final Map.Entry entry : ((Map)object).entrySet()) {
                        final Object entryValue = entry.getValue();
                        if (CycleNameSegment.this.name.equals(entry.getKey())) {
                            final Object applyValue = this.callback.apply(object, entryValue);
                            entry.setValue(applyValue);
                            this.context.eval = true;
                        }
                        else {
                            if (entryValue == null) {
                                continue;
                            }
                            this.accept(entryValue);
                        }
                    }
                }
                else if (object instanceof Collection) {
                    for (final Object item : (List)object) {
                        this.accept(item);
                    }
                }
                else {
                    final Class<?> entryValueClass = object.getClass();
                    final ObjectReader objectReader = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(entryValueClass);
                    final ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter(entryValueClass);
                    if (objectReader instanceof ObjectReaderBean) {
                        final FieldReader fieldReader = objectReader.getFieldReader(CycleNameSegment.this.nameHashCode);
                        final FieldWriter fieldWriter = objectWriter.getFieldWriter(CycleNameSegment.this.nameHashCode);
                        if (fieldWriter != null && fieldReader != null) {
                            Object fieldValue = fieldWriter.getFieldValue(object);
                            fieldValue = this.callback.apply(object, fieldValue);
                            fieldReader.accept(object, fieldValue);
                            this.context.eval = true;
                            return;
                        }
                    }
                    final List<FieldWriter> fieldWriters = (List<FieldWriter>)objectWriter.getFieldWriters();
                    for (final FieldWriter fieldWriter2 : fieldWriters) {
                        final Object fieldValue2 = fieldWriter2.getFieldValue(object);
                        this.accept(fieldValue2);
                    }
                }
            }
        }
    }
    
    static final class MinSegment extends JSONPathSegment implements EvalSegment
    {
        static final MinSegment INSTANCE;
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            this.eval(context);
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object value = (context.parent == null) ? context.root : context.parent.value;
            if (value == null) {
                return;
            }
            Object min = null;
            if (value instanceof Collection) {
                for (final Object item : (Collection)value) {
                    if (item == null) {
                        continue;
                    }
                    if (min == null) {
                        min = item;
                    }
                    else {
                        if (TypeUtils.compare(min, item) <= 0) {
                            continue;
                        }
                        min = item;
                    }
                }
            }
            else if (value instanceof Object[]) {
                final Object[] array2;
                final Object[] array = array2 = (Object[])value;
                for (final Object item2 : array2) {
                    if (item2 != null) {
                        if (min == null) {
                            min = item2;
                        }
                        else if (TypeUtils.compare(min, item2) > 0) {
                            min = item2;
                        }
                    }
                }
            }
            else {
                if (!(value instanceof JSONPath.Sequence)) {
                    throw new UnsupportedOperationException();
                }
                for (final Object item : ((JSONPath.Sequence)value).values) {
                    if (item == null) {
                        continue;
                    }
                    if (min == null) {
                        min = item;
                    }
                    else {
                        if (TypeUtils.compare(min, item) <= 0) {
                            continue;
                        }
                        min = item;
                    }
                }
            }
            context.value = min;
            context.eval = true;
        }
        
        static {
            INSTANCE = new MinSegment();
        }
    }
    
    static final class MaxSegment extends JSONPathSegment implements EvalSegment
    {
        static final MaxSegment INSTANCE;
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            this.eval(context);
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object value = (context.parent == null) ? context.root : context.parent.value;
            if (value == null) {
                return;
            }
            Object max = null;
            if (value instanceof Collection) {
                for (final Object item : (Collection)value) {
                    if (item == null) {
                        continue;
                    }
                    if (max == null) {
                        max = item;
                    }
                    else {
                        if (TypeUtils.compare(max, item) >= 0) {
                            continue;
                        }
                        max = item;
                    }
                }
            }
            else if (value instanceof Object[]) {
                final Object[] array2;
                final Object[] array = array2 = (Object[])value;
                for (final Object item2 : array2) {
                    if (item2 != null) {
                        if (max == null) {
                            max = item2;
                        }
                        else if (TypeUtils.compare(max, item2) < 0) {
                            max = item2;
                        }
                    }
                }
            }
            else {
                if (!(value instanceof JSONPath.Sequence)) {
                    throw new UnsupportedOperationException();
                }
                for (final Object item : ((JSONPath.Sequence)value).values) {
                    if (item == null) {
                        continue;
                    }
                    if (max == null) {
                        max = item;
                    }
                    else {
                        if (TypeUtils.compare(max, item) >= 0) {
                            continue;
                        }
                        max = item;
                    }
                }
            }
            context.value = max;
            context.eval = true;
        }
        
        static {
            INSTANCE = new MaxSegment();
        }
    }
    
    static final class SumSegment extends JSONPathSegment implements EvalSegment
    {
        static final SumSegment INSTANCE;
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            this.eval(context);
        }
        
        static Number add(final Number a, final Number b) {
            final boolean aIsInt = a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long;
            final boolean bIsInt = b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long;
            if (aIsInt && bIsInt) {
                return a.longValue() + b.longValue();
            }
            throw new JSONException("not support operation");
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object value = (context.parent == null) ? context.root : context.parent.value;
            if (value == null) {
                return;
            }
            Number sum = 0;
            if (value instanceof Collection) {
                for (final Object item : (Collection)value) {
                    if (item == null) {
                        continue;
                    }
                    sum = add(sum, (Number)item);
                }
            }
            else if (value instanceof Object[]) {
                final Object[] array2;
                final Object[] array = array2 = (Object[])value;
                for (final Object item2 : array2) {
                    if (item2 != null) {
                        sum = add(sum, (Number)item2);
                    }
                }
            }
            else {
                if (!(value instanceof JSONPath.Sequence)) {
                    throw new UnsupportedOperationException();
                }
                for (final Object item : ((JSONPath.Sequence)value).values) {
                    if (item == null) {
                        continue;
                    }
                    sum = add(sum, (Number)item);
                }
            }
            context.value = sum;
            context.eval = true;
        }
        
        static {
            INSTANCE = new SumSegment();
        }
    }
    
    static final class LengthSegment extends JSONPathSegment implements EvalSegment
    {
        static final LengthSegment INSTANCE;
        
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
            if (value == null) {
                return;
            }
            int length = 1;
            if (value instanceof Collection) {
                length = ((Collection)value).size();
            }
            else if (value.getClass().isArray()) {
                length = Array.getLength(value);
            }
            else if (value instanceof Map) {
                length = ((Map)value).size();
            }
            else if (value instanceof String) {
                length = ((String)value).length();
            }
            else if (value instanceof JSONPath.Sequence) {
                length = ((JSONPath.Sequence)value).values.size();
            }
            context.value = length;
        }
        
        static {
            INSTANCE = new LengthSegment();
        }
    }
    
    static final class ValuesSegment extends JSONPathSegment implements EvalSegment
    {
        static final ValuesSegment INSTANCE;
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            this.eval(context);
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object == null) {
                context.value = null;
                context.eval = true;
                return;
            }
            if (object instanceof Map) {
                context.value = new JSONArray(((Map)object).values());
                context.eval = true;
                return;
            }
            throw new JSONException("TODO");
        }
        
        static {
            INSTANCE = new ValuesSegment();
        }
    }
    
    static final class KeysSegment extends JSONPathSegment implements EvalSegment
    {
        static final KeysSegment INSTANCE;
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            if (jsonReader.isObject()) {
                jsonReader.next();
                final JSONArray array = new JSONArray();
                while (!jsonReader.nextIfObjectEnd()) {
                    final String fieldName = jsonReader.readFieldName();
                    ((ArrayList<String>)array).add(fieldName);
                    jsonReader.skipValue();
                }
                context.value = array;
                return;
            }
            throw new JSONException("TODO");
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof Map) {
                context.value = new JSONArray(((Map)object).keySet());
                context.eval = true;
                return;
            }
            throw new JSONException("TODO");
        }
        
        static {
            INSTANCE = new KeysSegment();
        }
    }
    
    static final class EntrySetSegment extends JSONPathSegment implements EvalSegment
    {
        static final EntrySetSegment INSTANCE;
        
        @Override
        public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
            if (jsonReader.isObject()) {
                jsonReader.next();
                final JSONArray array = new JSONArray();
                while (!jsonReader.nextIfObjectEnd()) {
                    final String fieldName = jsonReader.readFieldName();
                    final Object value = jsonReader.readAny();
                    ((ArrayList<JSONObject>)array).add(JSONObject.of("key", fieldName, "value", value));
                }
                context.value = array;
                return;
            }
            throw new JSONException("TODO");
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object instanceof Map) {
                final Map map = (Map)object;
                final JSONArray array = new JSONArray(map.size());
                for (final Map.Entry entry : ((Map)object).entrySet()) {
                    ((ArrayList<JSONObject>)array).add(JSONObject.of("key", entry.getKey(), "value", entry.getValue()));
                }
                context.value = array;
                context.eval = true;
                return;
            }
            throw new JSONException("TODO");
        }
        
        static {
            INSTANCE = new EntrySetSegment();
        }
    }
    
    interface EvalSegment
    {
    }
}
