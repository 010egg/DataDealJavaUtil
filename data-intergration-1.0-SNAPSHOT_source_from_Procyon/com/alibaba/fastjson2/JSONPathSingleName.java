// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.List;
import com.alibaba.fastjson2.reader.ValueConsumer;
import java.util.function.BiFunction;
import java.util.Collection;
import java.util.function.Function;
import com.alibaba.fastjson2.reader.ObjectReaderBean;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.util.Iterator;
import com.alibaba.fastjson2.util.IOUtils;
import java.util.Map;

final class JSONPathSingleName extends JSONPathSingle
{
    final long nameHashCode;
    final String name;
    
    public JSONPathSingleName(final String path, final JSONPathSegmentName segment, final Feature... features) {
        super(segment, path, features);
        this.name = segment.name;
        this.nameHashCode = segment.nameHashCode;
    }
    
    @Override
    public Object eval(final Object root) {
        Object value;
        if (root instanceof Map) {
            final Map map = (Map)root;
            value = map.get(this.name);
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
        }
        else {
            final ObjectWriter objectWriter = this.getWriterContext().getObjectWriter(root.getClass());
            if (objectWriter == null) {
                return null;
            }
            final FieldWriter fieldWriter = objectWriter.getFieldWriter(this.nameHashCode);
            if (fieldWriter == null) {
                return null;
            }
            value = fieldWriter.getFieldValue(root);
        }
        if ((this.features & Feature.AlwaysReturnList.mask) != 0x0L) {
            value = ((value == null) ? new JSONArray() : JSONArray.of(value));
        }
        return value;
    }
    
    @Override
    public boolean remove(final Object root) {
        if (root == null) {
            return false;
        }
        if (root instanceof Map) {
            return ((Map)root).remove(this.name) != null;
        }
        final ObjectReaderProvider provider = this.getReaderContext().getProvider();
        final ObjectReader objectReader = provider.getObjectReader(root.getClass());
        if (objectReader == null) {
            return false;
        }
        final FieldReader fieldReader = objectReader.getFieldReader(this.nameHashCode);
        if (fieldReader == null) {
            return false;
        }
        try {
            fieldReader.accept(root, null);
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean isRef() {
        return true;
    }
    
    @Override
    public boolean contains(final Object root) {
        if (root instanceof Map) {
            return ((Map)root).containsKey(this.name);
        }
        final ObjectWriterProvider provider = this.getWriterContext().provider;
        final ObjectWriter objectWriter = provider.getObjectWriter(root.getClass());
        if (objectWriter == null) {
            return false;
        }
        final FieldWriter fieldWriter = objectWriter.getFieldWriter(this.nameHashCode);
        return fieldWriter != null && fieldWriter.getFieldValue(root) != null;
    }
    
    @Override
    public void set(final Object rootObject, Object value) {
        if (rootObject instanceof Map) {
            final Map map = (Map)rootObject;
            map.put(this.name, value);
            return;
        }
        final ObjectReaderProvider provider = this.getReaderContext().getProvider();
        final ObjectReader objectReader = provider.getObjectReader(rootObject.getClass());
        final FieldReader fieldReader = objectReader.getFieldReader(this.nameHashCode);
        if (fieldReader != null) {
            if (value != null) {
                final Class<?> valueClass = value.getClass();
                final Class fieldClass = fieldReader.fieldClass;
                if (!fieldReader.supportAcceptType(valueClass)) {
                    final Function typeConvert = provider.getTypeConvert(valueClass, fieldClass);
                    if (typeConvert != null) {
                        value = typeConvert.apply(value);
                    }
                }
            }
            fieldReader.accept(rootObject, value);
        }
        else if (objectReader instanceof ObjectReaderBean) {
            ((ObjectReaderBean)objectReader).acceptExtra(rootObject, this.name, value);
        }
    }
    
    @Override
    public void set(final Object rootObject, Object value, final JSONReader.Feature... readerFeatures) {
        if (rootObject instanceof Map) {
            final Map map = (Map)rootObject;
            final Object origin = map.put(this.name, value);
            if (origin != null) {
                boolean duplicateKeyValueAsArray = false;
                for (final JSONReader.Feature feature : readerFeatures) {
                    if (feature == JSONReader.Feature.DuplicateKeyValueAsArray) {
                        duplicateKeyValueAsArray = true;
                        break;
                    }
                }
                if (duplicateKeyValueAsArray) {
                    if (origin instanceof Collection) {
                        ((Collection)origin).add(value);
                        map.put(this.name, value);
                    }
                    else {
                        final JSONArray array = JSONArray.of(origin, value);
                        map.put(this.name, array);
                    }
                }
            }
            return;
        }
        final ObjectReaderProvider provider = this.getReaderContext().getProvider();
        final ObjectReader objectReader = provider.getObjectReader(rootObject.getClass());
        final FieldReader fieldReader = objectReader.getFieldReader(this.nameHashCode);
        if (value != null) {
            final Class<?> valueClass = value.getClass();
            final Class fieldClass = fieldReader.fieldClass;
            if (valueClass != fieldClass) {
                final Function typeConvert = provider.getTypeConvert(valueClass, fieldClass);
                if (typeConvert != null) {
                    value = typeConvert.apply(value);
                }
            }
        }
        fieldReader.accept(rootObject, value);
    }
    
    @Override
    public void setCallback(final Object object, final BiFunction callback) {
        if (object instanceof Map) {
            final Map map = (Map)object;
            final Object originValue = map.get(this.name);
            if (originValue != null || map.containsKey(this.name)) {
                map.put(this.name, callback.apply(map, originValue));
            }
            return;
        }
        final Class<?> objectClass = object.getClass();
        if (this.readerContext == null) {
            this.readerContext = JSONFactory.createReadContext();
        }
        final FieldReader fieldReader = this.readerContext.provider.getObjectReader(objectClass).getFieldReader(this.nameHashCode);
        if (this.writerContext == null) {
            this.writerContext = JSONFactory.createWriteContext();
        }
        final FieldWriter fieldWriter = this.writerContext.provider.getObjectWriter(objectClass).getFieldWriter(this.nameHashCode);
        if (fieldReader != null && fieldWriter != null) {
            final Object fieldValue = fieldWriter.getFieldValue(object);
            final Object value = callback.apply(object, fieldValue);
            fieldReader.accept(object, value);
        }
    }
    
    @Override
    public void setInt(final Object obejct, final int value) {
        if (obejct instanceof Map) {
            ((Map)obejct).put(this.name, value);
            return;
        }
        final ObjectReaderProvider provider = this.getReaderContext().getProvider();
        final ObjectReader objectReader = provider.getObjectReader(obejct.getClass());
        objectReader.setFieldValue(obejct, this.name, this.nameHashCode, value);
    }
    
    @Override
    public void setLong(final Object object, final long value) {
        if (object instanceof Map) {
            ((Map)object).put(this.name, value);
            return;
        }
        final ObjectReaderProvider provider = this.getReaderContext().getProvider();
        final ObjectReader objectReader = provider.getObjectReader(object.getClass());
        objectReader.setFieldValue(object, this.name, this.nameHashCode, value);
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        if (jsonReader.jsonb) {
            if (jsonReader.nextIfObjectStart()) {
                while (!jsonReader.nextIfObjectEnd()) {
                    final long nameHashCode = jsonReader.readFieldNameHashCode();
                    if (nameHashCode == 0L) {
                        continue;
                    }
                    final boolean match = nameHashCode == this.nameHashCode;
                    if (!match && !jsonReader.isObject() && !jsonReader.isArray()) {
                        jsonReader.skipValue();
                    }
                    else {
                        if (jsonReader.isNumber()) {
                            return jsonReader.readNumber();
                        }
                        throw new JSONException("TODO");
                    }
                }
            }
            if ((this.features & Feature.AlwaysReturnList.mask) != 0x0L) {
                return new JSONArray();
            }
            return null;
        }
        else {
            if (jsonReader.nextIfObjectStart()) {
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
                                val = jsonReader.readNumber();
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
                        if ((this.features & Feature.AlwaysReturnList.mask) != 0x0L) {
                            if (val == null) {
                                val = new JSONArray();
                            }
                            else {
                                val = JSONArray.of(val);
                            }
                        }
                        return val;
                    }
                    jsonReader.skipValue();
                }
            }
            if ((this.features & Feature.AlwaysReturnList.mask) != 0x0L) {
                return new JSONArray();
            }
            return null;
        }
    }
    
    @Override
    public String extractScalar(final JSONReader jsonReader) {
        if (jsonReader.nextIfObjectStart()) {
            while (jsonReader.ch != '}') {
                final long nameHashCode = jsonReader.readFieldNameHashCode();
                final boolean match = nameHashCode == this.nameHashCode;
                final char ch = jsonReader.ch;
                if (match || ch == '{' || ch == '[') {
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
                            val = jsonReader.readNumber();
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
                    return JSON.toJSONString(val);
                }
                jsonReader.skipValue();
            }
            jsonReader.next();
        }
        return null;
    }
    
    @Override
    public long extractInt64Value(final JSONReader jsonReader) {
        Label_0532: {
            if (jsonReader.nextIfObjectStart()) {
                while (jsonReader.ch != '}') {
                    final long nameHashCode = jsonReader.readFieldNameHashCode();
                    final boolean match = nameHashCode == this.nameHashCode;
                    if (!match) {
                        jsonReader.skipValue();
                    }
                    else {
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
                                return jsonReader.readInt64Value();
                            }
                            case '[':
                            case '{': {
                                final Map object = jsonReader.readObject();
                                return jsonReader.toLong(object);
                            }
                            case '\"':
                            case '\'': {
                                final String str = jsonReader.readString();
                                return Long.parseLong(str);
                            }
                            case 'f':
                            case 't': {
                                final boolean booleanValue = jsonReader.readBoolValue();
                                return booleanValue ? 1 : 0;
                            }
                            case 'n': {
                                jsonReader.readNull();
                                jsonReader.wasNull = true;
                                return 0L;
                            }
                            case ']': {
                                jsonReader.next();
                                break Label_0532;
                            }
                            default: {
                                throw new JSONException("TODO : " + jsonReader.ch);
                            }
                        }
                    }
                }
                jsonReader.wasNull = true;
                return 0L;
            }
        }
        jsonReader.wasNull = true;
        return 0L;
    }
    
    @Override
    public int extractInt32Value(final JSONReader jsonReader) {
        Label_0299: {
            if (jsonReader.nextIfObjectStart()) {
                while (jsonReader.ch != '}') {
                    final long nameHashCode = jsonReader.readFieldNameHashCode();
                    final boolean match = nameHashCode == this.nameHashCode;
                    if (!match) {
                        jsonReader.skipValue();
                    }
                    else {
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
                                return jsonReader.readInt32Value();
                            }
                            case '\"':
                            case '\'': {
                                final String str = jsonReader.readString();
                                return Integer.parseInt(str);
                            }
                            case 'f':
                            case 't': {
                                final boolean booleanValue = jsonReader.readBoolValue();
                                return booleanValue ? 1 : 0;
                            }
                            case 'n': {
                                jsonReader.readNull();
                                jsonReader.wasNull = true;
                                return 0;
                            }
                            case ']': {
                                jsonReader.next();
                                break Label_0299;
                            }
                            default: {
                                throw new JSONException("TODO : " + jsonReader.ch);
                            }
                        }
                    }
                }
                jsonReader.wasNull = true;
                return 0;
            }
        }
        jsonReader.wasNull = true;
        return 0;
    }
    
    @Override
    public void extractScalar(final JSONReader jsonReader, final ValueConsumer consumer) {
        Label_0540: {
            if (jsonReader.nextIfObjectStart()) {
                while (jsonReader.ch != '}') {
                    final long nameHashCode = jsonReader.readFieldNameHashCode();
                    final boolean match = nameHashCode == this.nameHashCode;
                    if (!match) {
                        jsonReader.skipValue();
                    }
                    else {
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
                                jsonReader.readNumber(consumer, false);
                                return;
                            }
                            case '[': {
                                final List array = jsonReader.readArray();
                                consumer.accept(array);
                                return;
                            }
                            case '{': {
                                final Map object = jsonReader.readObject();
                                consumer.accept(object);
                                return;
                            }
                            case '\"':
                            case '\'': {
                                jsonReader.readString(consumer, false);
                                return;
                            }
                            case 'f':
                            case 't': {
                                consumer.accept(jsonReader.readBoolValue());
                                return;
                            }
                            case 'n': {
                                jsonReader.readNull();
                                consumer.acceptNull();
                                return;
                            }
                            case ']': {
                                jsonReader.next();
                                break Label_0540;
                            }
                            default: {
                                throw new JSONException("TODO : " + jsonReader.ch);
                            }
                        }
                    }
                }
                consumer.acceptNull();
                return;
            }
        }
        consumer.acceptNull();
    }
    
    @Override
    public void extract(final JSONReader jsonReader, final ValueConsumer consumer) {
        if (jsonReader.nextIfObjectStart()) {
            while (jsonReader.ch != '}') {
                final long nameHashCode = jsonReader.readFieldNameHashCode();
                final boolean match = nameHashCode == this.nameHashCode;
                if (!match) {
                    jsonReader.skipValue();
                }
                else {
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
                            jsonReader.readNumber(consumer, true);
                            return;
                        }
                        case '[': {
                            final List array = jsonReader.readArray();
                            consumer.accept(array);
                            return;
                        }
                        case '{': {
                            final Map object = jsonReader.readObject();
                            consumer.accept(object);
                            return;
                        }
                        case '\"':
                        case '\'': {
                            jsonReader.readString(consumer, true);
                            return;
                        }
                        case 'f':
                        case 't': {
                            consumer.accept(jsonReader.readBoolValue());
                            return;
                        }
                        case 'n': {
                            jsonReader.readNull();
                            consumer.acceptNull();
                            return;
                        }
                        default: {
                            throw new JSONException("TODO : " + jsonReader.ch);
                        }
                    }
                }
            }
            consumer.acceptNull();
            return;
        }
        consumer.acceptNull();
    }
}
