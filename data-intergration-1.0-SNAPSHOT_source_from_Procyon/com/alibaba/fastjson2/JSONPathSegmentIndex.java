// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.IOUtils;
import java.util.function.BiFunction;
import java.util.SortedMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.SortedSet;
import java.util.List;

final class JSONPathSegmentIndex extends JSONPathSegment
{
    static final JSONPathSegmentIndex ZERO;
    static final JSONPathSegmentIndex ONE;
    static final JSONPathSegmentIndex TWO;
    static final JSONPathSegmentIndex LAST;
    final int index;
    
    public JSONPathSegmentIndex(final int index) {
        this.index = index;
    }
    
    static JSONPathSegmentIndex of(final int index) {
        if (index == 0) {
            return JSONPathSegmentIndex.ZERO;
        }
        if (index == 1) {
            return JSONPathSegmentIndex.ONE;
        }
        if (index == 2) {
            return JSONPathSegmentIndex.TWO;
        }
        if (index == -1) {
            return JSONPathSegmentIndex.LAST;
        }
        return new JSONPathSegmentIndex(index);
    }
    
    @Override
    public void eval(final JSONPath.Context context) {
        final Object object = (context.parent == null) ? context.root : context.parent.value;
        if (object == null) {
            context.eval = true;
            return;
        }
        if (object instanceof List) {
            final List list = (List)object;
            if (this.index >= 0) {
                if (this.index < list.size()) {
                    context.value = list.get(this.index);
                }
            }
            else {
                final int itemIndex = list.size() + this.index;
                if (itemIndex >= 0 && itemIndex < list.size()) {
                    context.value = list.get(itemIndex);
                }
            }
            context.eval = true;
            return;
        }
        if (object instanceof SortedSet || object instanceof LinkedHashSet || (this.index == 0 && object instanceof Collection && ((Collection)object).size() == 1)) {
            final Collection collection = (Collection)object;
            int i = 0;
            for (final Object item : collection) {
                if (i == this.index) {
                    context.value = item;
                    break;
                }
                ++i;
            }
            context.eval = true;
            return;
        }
        if (object instanceof Object[]) {
            final Object[] array = (Object[])object;
            if (this.index >= 0) {
                if (this.index < array.length) {
                    context.value = array[this.index];
                }
            }
            else {
                final int itemIndex = array.length + this.index;
                if (itemIndex >= 0 && itemIndex < array.length) {
                    context.value = array[itemIndex];
                }
            }
            context.eval = true;
            return;
        }
        final Class objectClass = object.getClass();
        if (objectClass.isArray()) {
            final int length = Array.getLength(object);
            if (this.index >= 0) {
                if (this.index < length) {
                    context.value = Array.get(object, this.index);
                }
            }
            else {
                final int itemIndex2 = length + this.index;
                if (itemIndex2 >= 0 && itemIndex2 < length) {
                    context.value = Array.get(object, itemIndex2);
                }
            }
            context.eval = true;
            return;
        }
        if (object instanceof JSONPath.Sequence) {
            final List sequence = ((JSONPath.Sequence)object).values;
            final JSONArray values = new JSONArray(sequence.size());
            for (final Object o : sequence) {
                context.value = o;
                final JSONPath.Context itemContext = new JSONPath.Context(context.path, context, context.current, context.next, context.readerFeatures);
                this.eval(itemContext);
                values.add(itemContext.value);
            }
            if (context.next != null) {
                context.value = new JSONPath.Sequence(values);
            }
            else {
                context.value = values;
            }
            context.eval = true;
            return;
        }
        if (Map.class.isAssignableFrom(objectClass)) {
            context.value = this.eval((Map)object);
            context.eval = true;
            return;
        }
        if (this.index == 0) {
            context.value = object;
            context.eval = true;
            return;
        }
        throw new JSONException("jsonpath not support operate : " + context.path + ", objectClass" + objectClass.getName());
    }
    
    private Object eval(final Map object) {
        Object value = object.get(this.index);
        if (value == null) {
            value = object.get(Integer.toString(this.index));
        }
        if (value == null) {
            final int size = object.size();
            final Iterator it = object.entrySet().iterator();
            if (size == 1 || object instanceof LinkedHashMap || object instanceof SortedMap) {
                for (int i = 0; i <= this.index && i < size && it.hasNext(); ++i) {
                    final Map.Entry entry = it.next();
                    final Object entryKey = entry.getKey();
                    final Object entryValue = entry.getValue();
                    if (entryKey instanceof Long) {
                        if (entryKey.equals((long)this.index)) {
                            value = entryValue;
                            break;
                        }
                    }
                    else if (i == this.index) {
                        value = entryValue;
                    }
                }
            }
            else {
                for (int i = 0; i <= this.index && i < object.size() && it.hasNext(); ++i) {
                    final Map.Entry entry = it.next();
                    final Object entryKey = entry.getKey();
                    final Object entryValue = entry.getValue();
                    if (entryKey instanceof Long && entryKey.equals((long)this.index)) {
                        value = entryValue;
                        break;
                    }
                }
            }
        }
        return value;
    }
    
    @Override
    public void set(final JSONPath.Context context, final Object value) {
        final Object object = (context.parent == null) ? context.root : context.parent.value;
        if (object instanceof List) {
            final List list = (List)object;
            if (this.index >= 0) {
                if (this.index > list.size()) {
                    for (int i = list.size(); i < this.index; ++i) {
                        list.add(null);
                    }
                }
                if (this.index < list.size()) {
                    list.set(this.index, value);
                }
                else if (this.index <= list.size()) {
                    list.add(value);
                }
            }
            else {
                final int itemIndex = list.size() + this.index;
                if (itemIndex >= 0) {
                    list.set(itemIndex, value);
                }
            }
            return;
        }
        if (object instanceof Object[]) {
            final Object[] array = (Object[])object;
            final int length = array.length;
            if (this.index >= 0) {
                if (this.index < length) {
                    array[this.index] = value;
                }
            }
            else {
                final int arrayIndex = length + this.index;
                if (arrayIndex >= 0 && arrayIndex < length) {
                    array[arrayIndex] = value;
                }
            }
            return;
        }
        if (object != null && object.getClass().isArray()) {
            final int length2 = Array.getLength(object);
            if (this.index >= 0) {
                if (this.index < length2) {
                    Array.set(object, this.index, value);
                }
            }
            else {
                final int arrayIndex2 = length2 + this.index;
                if (arrayIndex2 >= 0 && arrayIndex2 < length2) {
                    Array.set(object, arrayIndex2, value);
                }
            }
            return;
        }
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public void setCallback(final JSONPath.Context context, final BiFunction callback) {
        final Object object = (context.parent == null) ? context.root : context.parent.value;
        if (object instanceof List) {
            final List list = (List)object;
            if (this.index >= 0) {
                if (this.index < list.size()) {
                    Object value = list.get(this.index);
                    value = callback.apply(object, value);
                    list.set(this.index, value);
                }
            }
            else {
                final int itemIndex = list.size() + this.index;
                if (itemIndex >= 0) {
                    Object value2 = list.get(itemIndex);
                    value2 = callback.apply(object, value2);
                    list.set(itemIndex, value2);
                }
            }
            return;
        }
        if (object instanceof Object[]) {
            final Object[] array = (Object[])object;
            if (this.index >= 0) {
                if (this.index < array.length) {
                    Object value = array[this.index];
                    value = callback.apply(object, value);
                    array[this.index] = value;
                }
            }
            else {
                final int itemIndex = array.length + this.index;
                if (itemIndex >= 0) {
                    Object value2 = array[itemIndex];
                    value2 = callback.apply(object, value2);
                    array[itemIndex] = value2;
                }
            }
            return;
        }
        if (object != null && object.getClass().isArray()) {
            final int length = Array.getLength(object);
            if (this.index >= 0) {
                if (this.index < length) {
                    Object value = Array.get(object, this.index);
                    value = callback.apply(object, value);
                    Array.set(object, this.index, value);
                }
            }
            else {
                final int arrayIndex = length + this.index;
                if (arrayIndex >= 0) {
                    Object value2 = Array.get(object, arrayIndex);
                    value2 = callback.apply(object, value2);
                    Array.set(object, arrayIndex, value2);
                }
            }
            return;
        }
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public boolean remove(final JSONPath.Context context) {
        final Object object = (context.parent == null) ? context.root : context.parent.value;
        if (object instanceof List) {
            final List list = (List)object;
            if (this.index >= 0) {
                if (this.index < list.size()) {
                    list.remove(this.index);
                    return true;
                }
            }
            else {
                final int itemIndex = list.size() + this.index;
                if (itemIndex >= 0) {
                    list.remove(itemIndex);
                    return true;
                }
            }
            return false;
        }
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public void setInt(final JSONPath.Context context, final int value) {
        final Object object = (context.parent == null) ? context.root : context.parent.value;
        if (object instanceof int[]) {
            final int[] array = (int[])object;
            if (this.index >= 0) {
                if (this.index < array.length) {
                    array[this.index] = value;
                }
            }
            else {
                final int arrayIndex = array.length + this.index;
                if (arrayIndex >= 0) {
                    array[arrayIndex] = value;
                }
            }
            return;
        }
        if (object instanceof long[]) {
            final long[] array2 = (long[])object;
            if (this.index >= 0) {
                if (this.index < array2.length) {
                    array2[this.index] = value;
                }
            }
            else {
                final int arrayIndex = array2.length + this.index;
                if (arrayIndex >= 0) {
                    array2[arrayIndex] = value;
                }
            }
            return;
        }
        this.set(context, value);
    }
    
    @Override
    public void setLong(final JSONPath.Context context, final long value) {
        final Object object = (context.parent == null) ? context.root : context.parent.value;
        if (object instanceof int[]) {
            final int[] array = (int[])object;
            if (this.index >= 0) {
                if (this.index < array.length) {
                    array[this.index] = (int)value;
                }
            }
            else {
                final int arrayIndex = array.length + this.index;
                if (arrayIndex >= 0) {
                    array[arrayIndex] = (int)value;
                }
            }
            return;
        }
        if (object instanceof long[]) {
            final long[] array2 = (long[])object;
            if (this.index >= 0) {
                if (this.index < array2.length) {
                    array2[this.index] = value;
                }
            }
            else {
                final int arrayIndex = array2.length + this.index;
                if (arrayIndex >= 0) {
                    array2[arrayIndex] = value;
                }
            }
            return;
        }
        this.set(context, value);
    }
    
    @Override
    public void accept(final JSONReader jsonReader, final JSONPath.Context context) {
        if (context.parent != null && (context.parent.eval || (context.parent.current instanceof CycleNameSegment && context.next == null))) {
            this.eval(context);
            return;
        }
        if (jsonReader.jsonb) {
            final int itemCnt = jsonReader.startArray();
            int i = 0;
            while (i < itemCnt) {
                final boolean match = this.index == i;
                if (!match) {
                    jsonReader.skipValue();
                    ++i;
                }
                else {
                    if ((jsonReader.isArray() || jsonReader.isObject()) && context.next != null) {
                        break;
                    }
                    context.value = jsonReader.readAny();
                    context.eval = true;
                    break;
                }
            }
            return;
        }
        if (jsonReader.ch == '{') {
            final Map object = jsonReader.readObject();
            context.value = this.eval(object);
            context.eval = true;
            return;
        }
        jsonReader.next();
        int j = 0;
    Label_0799:
        while (jsonReader.ch != '\u001a') {
            if (jsonReader.ch == ']') {
                jsonReader.next();
                break;
            }
            final boolean match2 = this.index == -1 || this.index == j;
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
                        if (context.next != null && !(context.next instanceof EvalSegment)) {
                            break Label_0799;
                        }
                        val = jsonReader.readArray();
                        break;
                    }
                    case '{': {
                        if (context.next != null && !(context.next instanceof EvalSegment)) {
                            break Label_0799;
                        }
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
                        throw new JSONException(jsonReader.info("not support : " + jsonReader.ch));
                    }
                }
                if (this.index == -1) {
                    if (jsonReader.ch == ']') {
                        context.value = val;
                    }
                }
                else {
                    context.value = val;
                }
            }
            ++j;
        }
    }
    
    @Override
    public String toString() {
        final int size = (this.index < 0) ? (IOUtils.stringSize(-this.index) + 1) : IOUtils.stringSize(this.index);
        final byte[] bytes = new byte[size + 2];
        bytes[0] = 91;
        IOUtils.getChars(this.index, bytes.length - 1, bytes);
        bytes[bytes.length - 1] = 93;
        String str;
        if (JDKUtils.STRING_CREATOR_JDK11 != null) {
            str = JDKUtils.STRING_CREATOR_JDK11.apply(bytes, JDKUtils.LATIN1);
        }
        else {
            str = new String(bytes, StandardCharsets.ISO_8859_1);
        }
        return str;
    }
    
    static {
        ZERO = new JSONPathSegmentIndex(0);
        ONE = new JSONPathSegmentIndex(1);
        TWO = new JSONPathSegmentIndex(2);
        LAST = new JSONPathSegmentIndex(-1);
    }
}
