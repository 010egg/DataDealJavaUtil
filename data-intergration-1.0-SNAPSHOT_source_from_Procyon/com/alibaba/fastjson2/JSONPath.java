// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.time.ZoneId;
import java.lang.reflect.Type;
import java.util.List;
import com.alibaba.fastjson2.reader.ValueConsumer;
import java.nio.charset.Charset;
import java.util.Arrays;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.util.Iterator;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class JSONPath
{
    static final JSONReader.Context PARSE_CONTEXT;
    JSONReader.Context readerContext;
    JSONWriter.Context writerContext;
    final String path;
    final long features;
    
    protected JSONPath(final String path, final Feature... features) {
        this.path = path;
        long featuresValue = 0L;
        for (final Feature feature : features) {
            featuresValue |= feature.mask;
        }
        this.features = featuresValue;
    }
    
    protected JSONPath(final String path, final long features) {
        this.path = path;
        this.features = features;
    }
    
    public abstract JSONPath getParent();
    
    public boolean endsWithFilter() {
        return false;
    }
    
    public boolean isPrevious() {
        return false;
    }
    
    @Override
    public final String toString() {
        return this.path;
    }
    
    public static Object extract(final String json, final String path) {
        final JSONReader jsonReader = JSONReader.of(json);
        final JSONPath jsonPath = of(path);
        return jsonPath.extract(jsonReader);
    }
    
    public static Object extract(final String json, final String path, final Feature... features) {
        final JSONReader jsonReader = JSONReader.of(json);
        final JSONPath jsonPath = of(path, features);
        return jsonPath.extract(jsonReader);
    }
    
    public static Object eval(final String str, final String path) {
        return extract(str, path);
    }
    
    public static Object eval(final Object rootObject, final String path) {
        return of(path).eval(rootObject);
    }
    
    public static String set(final String json, final String path, final Object value) {
        final Object object = JSON.parse(json);
        of(path).set(object, value);
        return JSON.toJSONString(object);
    }
    
    public static boolean contains(final Object rootObject, final String path) {
        if (rootObject == null) {
            return false;
        }
        final JSONPath jsonPath = of(path);
        return jsonPath.contains(rootObject);
    }
    
    public static Object set(final Object rootObject, final String path, final Object value) {
        of(path).set(rootObject, value);
        return rootObject;
    }
    
    public static Object setCallback(final Object rootObject, final String path, final Function callback) {
        of(path).setCallback(rootObject, callback);
        return rootObject;
    }
    
    public static Object setCallback(final Object rootObject, final String path, final BiFunction callback) {
        of(path).setCallback(rootObject, callback);
        return rootObject;
    }
    
    public static String remove(final String json, final String path) {
        final Object object = JSON.parse(json);
        of(path).remove(object);
        return JSON.toJSONString(object);
    }
    
    public static void remove(final Object rootObject, final String path) {
        of(path).remove(rootObject);
    }
    
    public static Map<String, Object> paths(final Object javaObject) {
        final Map<Object, String> values = new IdentityHashMap<Object, String>();
        final Map<String, Object> paths = new HashMap<String, Object>();
        RootPath.INSTANCE.paths(values, paths, "$", javaObject);
        return paths;
    }
    
    void paths(final Map<Object, String> values, final Map<String, Object> paths, final String parent, final Object javaObject) {
        if (javaObject == null) {
            return;
        }
        final String p = values.put(javaObject, parent);
        if (p != null) {
            final Class<?> type = javaObject.getClass();
            final boolean basicType = type == String.class || type == Boolean.class || type == Character.class || type == UUID.class || javaObject instanceof Enum || javaObject instanceof Number || javaObject instanceof Date;
            if (!basicType) {
                return;
            }
        }
        paths.put(parent, javaObject);
        if (javaObject instanceof Map) {
            final Map map = (Map)javaObject;
            for (final Object entryObj : map.entrySet()) {
                final Map.Entry entry = (Map.Entry)entryObj;
                final Object key = entry.getKey();
                if (key instanceof String) {
                    final String path = parent + "." + key;
                    this.paths(values, paths, path, entry.getValue());
                }
            }
            return;
        }
        if (javaObject instanceof Collection) {
            final Collection collection = (Collection)javaObject;
            int i = 0;
            for (final Object item : collection) {
                final String path2 = parent + "[" + i + "]";
                this.paths(values, paths, path2, item);
                ++i;
            }
            return;
        }
        final Class<?> clazz = javaObject.getClass();
        if (clazz.isArray()) {
            for (int len = Array.getLength(javaObject), j = 0; j < len; ++j) {
                final Object item = Array.get(javaObject, j);
                final String path2 = parent + "[" + j + "]";
                this.paths(values, paths, path2, item);
            }
            return;
        }
        if (ObjectWriterProvider.isPrimitiveOrEnum(clazz)) {
            return;
        }
        final ObjectWriter serializer = this.getWriterContext().getObjectWriter(clazz);
        if (serializer instanceof ObjectWriterAdapter) {
            final ObjectWriterAdapter javaBeanSerializer = (ObjectWriterAdapter)serializer;
            try {
                final Map<String, Object> fieldValues = (Map<String, Object>)javaBeanSerializer.toMap(javaObject);
                for (final Map.Entry<String, Object> entry2 : fieldValues.entrySet()) {
                    final String key2 = entry2.getKey();
                    if (key2 != null) {
                        final String path3 = parent + "." + key2;
                        this.paths(values, paths, path3, entry2.getValue());
                    }
                }
            }
            catch (Exception e) {
                throw new JSONException("toJSON error", e);
            }
        }
    }
    
    public abstract boolean isRef();
    
    public void arrayAdd(final Object root, final Object... values) {
        final Object result = this.eval(root);
        if (result == null) {
            this.set(root, JSONArray.of(values));
            return;
        }
        if (result instanceof Collection) {
            final Collection collection = (Collection)result;
            collection.addAll(Arrays.asList(values));
        }
    }
    
    public abstract boolean contains(final Object p0);
    
    public abstract Object eval(final Object p0);
    
    protected JSONReader.Context createContext() {
        return JSONFactory.createReadContext();
    }
    
    public Object extract(final String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        final JSONReader jsonReader = JSONReader.of(jsonStr, this.createContext());
        try {
            final Object extract = this.extract(jsonReader);
            if (jsonReader != null) {
                jsonReader.close();
            }
            return extract;
        }
        catch (Throwable t) {
            if (jsonReader != null) {
                try {
                    jsonReader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    public Object extract(final byte[] jsonBytes) {
        if (jsonBytes == null) {
            return null;
        }
        final JSONReader jsonReader = JSONReader.of(jsonBytes, this.createContext());
        try {
            final Object extract = this.extract(jsonReader);
            if (jsonReader != null) {
                jsonReader.close();
            }
            return extract;
        }
        catch (Throwable t) {
            if (jsonReader != null) {
                try {
                    jsonReader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    public Object extract(final byte[] jsonBytes, final int off, final int len, final Charset charset) {
        if (jsonBytes == null) {
            return null;
        }
        final JSONReader jsonReader = JSONReader.of(jsonBytes, off, len, charset, this.createContext());
        try {
            final Object extract = this.extract(jsonReader);
            if (jsonReader != null) {
                jsonReader.close();
            }
            return extract;
        }
        catch (Throwable t) {
            if (jsonReader != null) {
                try {
                    jsonReader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    public abstract Object extract(final JSONReader p0);
    
    public abstract String extractScalar(final JSONReader p0);
    
    public JSONReader.Context getReaderContext() {
        if (this.readerContext == null) {
            this.readerContext = JSONFactory.createReadContext();
        }
        return this.readerContext;
    }
    
    public JSONPath setReaderContext(final JSONReader.Context context) {
        this.readerContext = context;
        return this;
    }
    
    public JSONWriter.Context getWriterContext() {
        if (this.writerContext == null) {
            this.writerContext = JSONFactory.createWriteContext();
        }
        return this.writerContext;
    }
    
    public JSONPath setWriterContext(final JSONWriter.Context writerContext) {
        this.writerContext = writerContext;
        return this;
    }
    
    public abstract void set(final Object p0, final Object p1);
    
    public abstract void set(final Object p0, final Object p1, final JSONReader.Feature... p2);
    
    public void setCallback(final Object object, final Function callback) {
        this.setCallback(object, new JSONPathFunction.BiFunctionAdapter(callback));
    }
    
    public abstract void setCallback(final Object p0, final BiFunction p1);
    
    public abstract void setInt(final Object p0, final int p1);
    
    public abstract void setLong(final Object p0, final long p1);
    
    public abstract boolean remove(final Object p0);
    
    public void extract(final JSONReader jsonReader, final ValueConsumer consumer) {
        final Object object = this.extract(jsonReader);
        if (object == null) {
            consumer.acceptNull();
            return;
        }
        if (object instanceof Number) {
            consumer.accept((Number)object);
            return;
        }
        if (object instanceof String) {
            consumer.accept((String)object);
            return;
        }
        if (object instanceof Boolean) {
            consumer.accept((boolean)object);
            return;
        }
        if (object instanceof Map) {
            consumer.accept((Map)object);
            return;
        }
        if (object instanceof List) {
            consumer.accept((List)object);
            return;
        }
        throw new JSONException("TODO : " + object.getClass());
    }
    
    public void extractScalar(final JSONReader jsonReader, final ValueConsumer consumer) {
        final Object object = this.extractScalar(jsonReader);
        if (object == null) {
            consumer.acceptNull();
            return;
        }
        final String str = object.toString();
        consumer.accept(str);
    }
    
    public Long extractInt64(final JSONReader jsonReader) {
        final long value = this.extractInt64Value(jsonReader);
        if (jsonReader.wasNull) {
            return null;
        }
        return value;
    }
    
    public long extractInt64Value(final JSONReader jsonReader) {
        final Object object = this.extract(jsonReader);
        if (object == null) {
            jsonReader.wasNull = true;
            return 0L;
        }
        if (object instanceof Number) {
            return ((Number)object).longValue();
        }
        final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(object.getClass(), Long.TYPE);
        if (typeConvert == null) {
            throw new JSONException("can not convert to long : " + object);
        }
        final Object converted = typeConvert.apply(object);
        return (long)converted;
    }
    
    public Integer extractInt32(final JSONReader jsonReader) {
        final int intValue = this.extractInt32Value(jsonReader);
        if (jsonReader.wasNull) {
            return null;
        }
        return intValue;
    }
    
    public int extractInt32Value(final JSONReader jsonReader) {
        final Object object = this.extract(jsonReader);
        if (object == null) {
            jsonReader.wasNull = true;
            return 0;
        }
        if (object instanceof Number) {
            return ((Number)object).intValue();
        }
        final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(object.getClass(), Integer.TYPE);
        if (typeConvert == null) {
            throw new JSONException("can not convert to int : " + object);
        }
        return typeConvert.apply(object);
    }
    
    @Deprecated
    public static JSONPath compile(final String path) {
        return of(path);
    }
    
    public static JSONPath compile(final String strPath, final Class objectClass) {
        final JSONPath path = of(strPath);
        final JSONFactory.JSONPathCompiler compiler = JSONFactory.getDefaultJSONPathCompiler();
        return compiler.compile(objectClass, path);
    }
    
    static JSONPathSingle of(final JSONPathSegment segment) {
        String prefix;
        if (segment instanceof JSONPathSegment.MultiIndexSegment || segment instanceof JSONPathSegmentIndex) {
            prefix = "$";
        }
        else {
            prefix = "$.";
        }
        final String path = prefix + segment.toString();
        if (segment instanceof JSONPathSegmentName) {
            return new JSONPathSingleName(path, (JSONPathSegmentName)segment, new Feature[0]);
        }
        return new JSONPathSingle(segment, path, new Feature[0]);
    }
    
    public static JSONPath of(final String path) {
        if ("#-1".equals(path)) {
            return PreviousPath.INSTANCE;
        }
        return new JSONPathParser(path).parse(new Feature[0]);
    }
    
    public static JSONPath of(final String path, final Type type) {
        final JSONPath jsonPath = of(path);
        return JSONPathTyped.of(jsonPath, type);
    }
    
    public static JSONPath of(final String path, final Type type, final Feature... features) {
        final JSONPath jsonPath = of(path, features);
        return JSONPathTyped.of(jsonPath, type);
    }
    
    public static JSONPath of(final String[] paths, final Type[] types) {
        return of(paths, types, null, null, (ZoneId)null, new JSONReader.Feature[0]);
    }
    
    public static JSONPath of(final String[] paths, final Type[] types, final JSONReader.Feature... features) {
        return of(paths, types, null, null, (ZoneId)null, features);
    }
    
    public static JSONPath of(final String[] paths, Type[] types, final String[] formats, final long[] pathFeatures, final ZoneId zoneId, final JSONReader.Feature... features) {
        if (paths.length == 0) {
            throw new JSONException("illegal paths, not support 0 length");
        }
        if (types == null) {
            types = new Type[paths.length];
            Arrays.fill(types, Object.class);
        }
        if (types.length != paths.length) {
            throw new JSONException("types.length not equals paths.length");
        }
        final JSONPath[] jsonPaths = new JSONPath[paths.length];
        for (int i = 0; i < paths.length; ++i) {
            jsonPaths[i] = of(paths[i]);
        }
        boolean allSingleName = true;
        boolean allSinglePositiveIndex = true;
        boolean allTwoName = true;
        boolean allTwoIndexPositive = true;
        boolean allThreeName = true;
        boolean sameMultiLength = true;
        JSONPathMulti firstMulti = null;
        for (int j = 0; j < jsonPaths.length; ++j) {
            final JSONPath path = jsonPaths[j];
            if (j == 0) {
                if (path instanceof JSONPathMulti) {
                    firstMulti = (JSONPathMulti)path;
                }
                else {
                    sameMultiLength = false;
                }
            }
            else if (sameMultiLength && path instanceof JSONPathMulti && ((JSONPathMulti)path).segments.size() != firstMulti.segments.size()) {
                sameMultiLength = false;
            }
            if (allSingleName && !(path instanceof JSONPathSingleName)) {
                allSingleName = false;
            }
            if (allSinglePositiveIndex && (!(path instanceof JSONPathSingleIndex) || ((JSONPathSingleIndex)path).index < 0)) {
                allSinglePositiveIndex = false;
            }
            if (allTwoName) {
                if (path instanceof JSONPathTwoSegment) {
                    final JSONPathTwoSegment two = (JSONPathTwoSegment)path;
                    if (!(two.second instanceof JSONPathSegmentName)) {
                        allTwoName = false;
                    }
                }
                else {
                    allTwoName = false;
                }
            }
            if (allTwoIndexPositive) {
                if (path instanceof JSONPathTwoSegment) {
                    final JSONPathTwoSegment two = (JSONPathTwoSegment)path;
                    if (!(two.second instanceof JSONPathSegmentIndex) || ((JSONPathSegmentIndex)two.second).index < 0) {
                        allTwoIndexPositive = false;
                    }
                }
                else {
                    allTwoIndexPositive = false;
                }
            }
            if (allThreeName) {
                if (path instanceof JSONPathMulti) {
                    final JSONPathMulti multi = (JSONPathMulti)path;
                    if (multi.segments.size() == 3) {
                        final JSONPathSegment three = multi.segments.get(2);
                        if (!(three instanceof JSONPathSegmentName)) {
                            allThreeName = false;
                        }
                    }
                    else {
                        allThreeName = false;
                    }
                }
                else {
                    allThreeName = false;
                }
            }
        }
        final long featuresValue = JSONReader.Feature.of(features);
        if (allSingleName) {
            return new JSONPathTypedMultiNames(jsonPaths, null, jsonPaths, types, formats, pathFeatures, zoneId, featuresValue);
        }
        if (allSinglePositiveIndex) {
            return new JSONPathTypedMultiIndexes(jsonPaths, null, jsonPaths, types, formats, pathFeatures, zoneId, featuresValue);
        }
        if (allTwoName || allTwoIndexPositive) {
            boolean samePrefix = true;
            final JSONPathSegment first0 = ((JSONPathTwoSegment)jsonPaths[0]).first;
            for (int k = 1; k < jsonPaths.length; ++k) {
                final JSONPathTwoSegment two2 = (JSONPathTwoSegment)jsonPaths[k];
                if (!first0.equals(two2.first)) {
                    samePrefix = false;
                    break;
                }
            }
            if (samePrefix) {
                final JSONPath firstPath = jsonPaths[0];
                if (allTwoName) {
                    final JSONPathSingleName[] names = new JSONPathSingleName[jsonPaths.length];
                    for (int l = 0; l < jsonPaths.length; ++l) {
                        final JSONPathTwoSegment two3 = (JSONPathTwoSegment)jsonPaths[l];
                        final JSONPathSegmentName name = (JSONPathSegmentName)two3.second;
                        names[l] = new JSONPathSingleName("$." + name, name, new Feature[0]);
                    }
                    final String prefixPath = firstPath.path.substring(0, firstPath.path.length() - names[0].name.length() - 1);
                    if (first0 instanceof JSONPathSegmentName) {
                        final JSONPathSegmentName name2 = (JSONPathSegmentName)first0;
                        final JSONPath prefix = new JSONPathSingleName(prefixPath, name2, new Feature[0]);
                        return new JSONPathTypedMultiNamesPrefixName1(jsonPaths, prefix, names, types, formats, pathFeatures, zoneId, featuresValue);
                    }
                    if (first0 instanceof JSONPathSegmentIndex) {
                        final JSONPathSegmentIndex first0Index = (JSONPathSegmentIndex)first0;
                        if (first0Index.index >= 0) {
                            final JSONPathSingleIndex prefix2 = new JSONPathSingleIndex(prefixPath, first0Index, new Feature[0]);
                            return new JSONPathTypedMultiNamesPrefixIndex1(jsonPaths, prefix2, names, types, formats, pathFeatures, zoneId, featuresValue);
                        }
                    }
                }
                else {
                    final JSONPathSingleIndex[] indexes = new JSONPathSingleIndex[jsonPaths.length];
                    for (int l = 0; l < jsonPaths.length; ++l) {
                        final JSONPathTwoSegment two3 = (JSONPathTwoSegment)jsonPaths[l];
                        final JSONPathSegmentIndex name3 = (JSONPathSegmentIndex)two3.second;
                        indexes[l] = new JSONPathSingleIndex("$" + name3, name3, new Feature[0]);
                    }
                    JSONPath prefix3 = null;
                    if (first0 instanceof JSONPathSegmentName) {
                        final JSONPathSegmentName name2 = (JSONPathSegmentName)first0;
                        prefix3 = new JSONPathSingleName("$." + name2.name, name2, new Feature[0]);
                    }
                    else if (first0 instanceof JSONPathSegmentIndex) {
                        final JSONPathSegmentIndex index = (JSONPathSegmentIndex)first0;
                        prefix3 = new JSONPathSingleIndex("$[" + index.index + "]", index, new Feature[0]);
                    }
                    if (prefix3 != null) {
                        return new JSONPathTypedMultiIndexes(jsonPaths, prefix3, indexes, types, formats, pathFeatures, zoneId, featuresValue);
                    }
                }
            }
        }
        else if (allThreeName) {
            boolean samePrefix = true;
            final JSONPathSegment first0 = ((JSONPathMulti)jsonPaths[0]).segments.get(0);
            final JSONPathSegment first2 = ((JSONPathMulti)jsonPaths[0]).segments.get(1);
            for (int m = 1; m < jsonPaths.length; ++m) {
                final JSONPathMulti multi2 = (JSONPathMulti)jsonPaths[m];
                if (!first0.equals(multi2.segments.get(0))) {
                    samePrefix = false;
                    break;
                }
                if (!first2.equals(multi2.segments.get(1))) {
                    samePrefix = false;
                    break;
                }
            }
            if (samePrefix) {
                final JSONPathSingleName[] names = new JSONPathSingleName[jsonPaths.length];
                for (int l = 0; l < jsonPaths.length; ++l) {
                    final JSONPathMulti multi3 = (JSONPathMulti)jsonPaths[l];
                    final JSONPathSegmentName name = multi3.segments.get(2);
                    names[l] = new JSONPathSingleName("$." + name, name, new Feature[0]);
                }
                final JSONPath firstPath2 = jsonPaths[0];
                final String prefixPath2 = firstPath2.path.substring(0, firstPath2.path.length() - names[0].name.length() - 1);
                final JSONPathTwoSegment prefix4 = new JSONPathTwoSegment(prefixPath2, first0, first2, new Feature[0]);
                if (first0 instanceof JSONPathSegmentName && first2 instanceof JSONPathSegmentName) {
                    return new JSONPathTypedMultiNamesPrefixName2(jsonPaths, prefix4, names, types, formats, pathFeatures, zoneId, featuresValue);
                }
                return new JSONPathTypedMultiNames(jsonPaths, prefix4, names, types, formats, pathFeatures, zoneId, featuresValue);
            }
        }
        if (sameMultiLength && paths.length > 1) {
            boolean samePrefix = true;
            boolean sameType = true;
            final int lastIndex = firstMulti.segments.size() - 1;
            final JSONPathSegment lastSegment = firstMulti.segments.get(lastIndex);
            for (int l = 0; l < lastIndex; ++l) {
                final JSONPathSegment segment = firstMulti.segments.get(l);
                for (int j2 = 1; j2 < paths.length; ++j2) {
                    final JSONPath jsonPath = jsonPaths[j2];
                    JSONPathSegment segment2;
                    if (jsonPath instanceof JSONPathMulti) {
                        final JSONPathMulti path2 = (JSONPathMulti)jsonPath;
                        segment2 = path2.segments.get(l);
                    }
                    else if (jsonPath instanceof JSONPathSingleName) {
                        segment2 = ((JSONPathSingleName)jsonPath).segment;
                    }
                    else if (jsonPath instanceof JSONPathSingleIndex) {
                        segment2 = ((JSONPathSingleIndex)jsonPath).segment;
                    }
                    else {
                        segment2 = null;
                    }
                    if (!segment.equals(segment2)) {
                        samePrefix = false;
                        break;
                    }
                }
                if (!samePrefix) {
                    break;
                }
            }
            if (samePrefix) {
                for (int l = 1; l < paths.length; ++l) {
                    final JSONPathMulti path3 = (JSONPathMulti)jsonPaths[l];
                    if (!lastSegment.getClass().equals(path3.segments.get(lastIndex).getClass())) {
                        sameType = false;
                        break;
                    }
                }
                if (sameType) {
                    final List<JSONPathSegment> prefixSegments = firstMulti.segments.subList(0, lastIndex - 1);
                    String prefixPath2 = null;
                    final int dotIndex = firstMulti.path.lastIndexOf(46);
                    if (dotIndex != -1) {
                        prefixPath2 = firstMulti.path.substring(0, dotIndex - 1);
                    }
                    if (prefixPath2 != null) {
                        final JSONPathMulti prefix5 = new JSONPathMulti(prefixPath2, prefixSegments, new Feature[0]);
                        if (lastSegment instanceof JSONPathSegmentIndex) {
                            final JSONPath[] indexPaths = new JSONPath[paths.length];
                            for (int i2 = 0; i2 < jsonPaths.length; ++i2) {
                                final JSONPathMulti path4 = (JSONPathMulti)jsonPaths[i2];
                                final JSONPathSegmentIndex lastSegmentIndex = path4.segments.get(lastIndex);
                                indexPaths[i2] = new JSONPathSingleIndex(lastSegmentIndex.toString(), lastSegmentIndex, new Feature[0]);
                            }
                            return new JSONPathTypedMultiIndexes(jsonPaths, prefix5, indexPaths, types, formats, pathFeatures, zoneId, featuresValue);
                        }
                    }
                }
            }
        }
        return new JSONPathTypedMulti(jsonPaths, types, formats, pathFeatures, zoneId, featuresValue);
    }
    
    public static JSONPath of(final String path, final Feature... features) {
        if ("#-1".equals(path)) {
            return PreviousPath.INSTANCE;
        }
        return new JSONPathParser(path).parse(features);
    }
    
    static JSONPathFilter.Operator parseOperator(final JSONReader jsonReader) {
        JSONPathFilter.Operator operator = null;
        switch (jsonReader.ch) {
            case '<': {
                jsonReader.next();
                if (jsonReader.ch == '=') {
                    jsonReader.next();
                    operator = JSONPathFilter.Operator.LE;
                    break;
                }
                if (jsonReader.ch == '>') {
                    jsonReader.next();
                    operator = JSONPathFilter.Operator.NE;
                    break;
                }
                operator = JSONPathFilter.Operator.LT;
                break;
            }
            case '=': {
                jsonReader.next();
                if (jsonReader.ch == '~') {
                    jsonReader.next();
                    operator = JSONPathFilter.Operator.REG_MATCH;
                    break;
                }
                if (jsonReader.ch == '=') {
                    jsonReader.next();
                    operator = JSONPathFilter.Operator.EQ;
                    break;
                }
                operator = JSONPathFilter.Operator.EQ;
                break;
            }
            case '!': {
                jsonReader.next();
                if (jsonReader.ch == '=') {
                    jsonReader.next();
                    operator = JSONPathFilter.Operator.NE;
                    break;
                }
                throw new JSONException("not support operator : !" + jsonReader.ch);
            }
            case '>': {
                jsonReader.next();
                if (jsonReader.ch == '=') {
                    jsonReader.next();
                    operator = JSONPathFilter.Operator.GE;
                    break;
                }
                operator = JSONPathFilter.Operator.GT;
                break;
            }
            case 'L':
            case 'l': {
                jsonReader.readFieldNameHashCodeUnquote();
                final String fieldName = jsonReader.getFieldName();
                if ("like".equalsIgnoreCase(fieldName)) {
                    operator = JSONPathFilter.Operator.LIKE;
                    break;
                }
                throw new JSONException("not support operator : " + fieldName);
            }
            case 'N':
            case 'n': {
                jsonReader.readFieldNameHashCodeUnquote();
                String fieldName = jsonReader.getFieldName();
                if ("nin".equalsIgnoreCase(fieldName)) {
                    operator = JSONPathFilter.Operator.NOT_IN;
                    break;
                }
                if (!"not".equalsIgnoreCase(fieldName)) {
                    throw new JSONException("not support operator : " + fieldName);
                }
                jsonReader.readFieldNameHashCodeUnquote();
                fieldName = jsonReader.getFieldName();
                if ("like".equalsIgnoreCase(fieldName)) {
                    operator = JSONPathFilter.Operator.NOT_LIKE;
                    break;
                }
                if ("rlike".equalsIgnoreCase(fieldName)) {
                    operator = JSONPathFilter.Operator.NOT_RLIKE;
                    break;
                }
                if ("in".equalsIgnoreCase(fieldName)) {
                    operator = JSONPathFilter.Operator.NOT_IN;
                    break;
                }
                if ("between".equalsIgnoreCase(fieldName)) {
                    operator = JSONPathFilter.Operator.NOT_BETWEEN;
                    break;
                }
                throw new JSONException("not support operator : " + fieldName);
            }
            case 'I':
            case 'i': {
                jsonReader.readFieldNameHashCodeUnquote();
                final String fieldName = jsonReader.getFieldName();
                if ("in".equalsIgnoreCase(fieldName)) {
                    operator = JSONPathFilter.Operator.IN;
                    break;
                }
                throw new JSONException("not support operator : " + fieldName);
            }
            case 'R':
            case 'r': {
                jsonReader.readFieldNameHashCodeUnquote();
                final String fieldName = jsonReader.getFieldName();
                if ("rlike".equalsIgnoreCase(fieldName)) {
                    operator = JSONPathFilter.Operator.RLIKE;
                    break;
                }
                throw new JSONException("not support operator : " + fieldName);
            }
            case 'B':
            case 'b': {
                jsonReader.readFieldNameHashCodeUnquote();
                final String fieldName = jsonReader.getFieldName();
                if ("between".equalsIgnoreCase(fieldName)) {
                    operator = JSONPathFilter.Operator.BETWEEN;
                    break;
                }
                throw new JSONException("not support operator : " + fieldName);
            }
            case 'S':
            case 's': {
                jsonReader.readFieldNameHashCodeUnquote();
                String fieldName = jsonReader.getFieldName();
                if (!"starts".equalsIgnoreCase(fieldName)) {
                    throw new JSONException("not support operator : " + fieldName);
                }
                jsonReader.readFieldNameHashCodeUnquote();
                fieldName = jsonReader.getFieldName();
                if (!"with".equalsIgnoreCase(fieldName)) {
                    throw new JSONException("not support operator : " + fieldName);
                }
                operator = JSONPathFilter.Operator.STARTS_WITH;
                break;
            }
            case 'E':
            case 'e': {
                jsonReader.readFieldNameHashCodeUnquote();
                String fieldName = jsonReader.getFieldName();
                if (!"ends".equalsIgnoreCase(fieldName)) {
                    throw new JSONException("not support operator : " + fieldName);
                }
                jsonReader.readFieldNameHashCodeUnquote();
                fieldName = jsonReader.getFieldName();
                if (!"with".equalsIgnoreCase(fieldName)) {
                    throw new JSONException("not support operator : " + fieldName);
                }
                operator = JSONPathFilter.Operator.ENDS_WITH;
                break;
            }
            default: {
                jsonReader.readFieldNameHashCodeUnquote();
                throw new JSONException("not support operator : " + jsonReader.getFieldName());
            }
        }
        return operator;
    }
    
    static {
        PARSE_CONTEXT = JSONFactory.createReadContext();
    }
    
    static final class PreviousPath extends JSONPath
    {
        static final PreviousPath INSTANCE;
        
        PreviousPath(final String path) {
            super(path, new Feature[0]);
        }
        
        @Override
        public boolean isRef() {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public boolean isPrevious() {
            return true;
        }
        
        @Override
        public boolean contains(final Object rootObject) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public Object eval(final Object rootObject) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public Object extract(final JSONReader jsonReader) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public String extractScalar(final JSONReader jsonReader) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public void set(final Object rootObject, final Object value) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public void set(final Object rootObject, final Object value, final JSONReader.Feature... readerFeatures) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public void setCallback(final Object rootObject, final BiFunction callback) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public JSONPath getParent() {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public void setInt(final Object rootObject, final int value) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public void setLong(final Object rootObject, final long value) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public boolean remove(final Object rootObject) {
            throw new JSONException("unsupported operation");
        }
        
        static {
            INSTANCE = new PreviousPath("#-1");
        }
    }
    
    static final class RootPath extends JSONPath
    {
        static final RootPath INSTANCE;
        
        private RootPath() {
            super("$", new Feature[0]);
        }
        
        @Override
        public boolean isRef() {
            return true;
        }
        
        @Override
        public boolean contains(final Object object) {
            return false;
        }
        
        @Override
        public Object eval(final Object object) {
            return object;
        }
        
        @Override
        public Object extract(final JSONReader jsonReader) {
            if (jsonReader == null) {
                return null;
            }
            return jsonReader.readAny();
        }
        
        @Override
        public String extractScalar(final JSONReader jsonReader) {
            final Object any = jsonReader.readAny();
            return JSON.toJSONString(any);
        }
        
        @Override
        public void set(final Object object, final Object value) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public void set(final Object object, final Object value, final JSONReader.Feature... readerFeatures) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public void setCallback(final Object object, final BiFunction callback) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public void setInt(final Object object, final int value) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public void setLong(final Object object, final long value) {
            throw new JSONException("unsupported operation");
        }
        
        @Override
        public boolean remove(final Object object) {
            return false;
        }
        
        @Override
        public JSONPath getParent() {
            return null;
        }
        
        static {
            INSTANCE = new RootPath();
        }
    }
    
    static final class Context
    {
        final JSONPath path;
        final Context parent;
        final JSONPathSegment current;
        final JSONPathSegment next;
        final long readerFeatures;
        Object root;
        Object value;
        boolean eval;
        
        Context(final JSONPath path, final Context parent, final JSONPathSegment current, final JSONPathSegment next, final long readerFeatures) {
            this.path = path;
            this.current = current;
            this.next = next;
            this.parent = parent;
            this.readerFeatures = readerFeatures;
        }
    }
    
    static class Sequence
    {
        final List values;
        
        public Sequence(final List values) {
            this.values = values;
        }
    }
    
    public enum Feature
    {
        AlwaysReturnList(1L), 
        NullOnError(2L), 
        KeepNullValue(4L);
        
        public final long mask;
        
        private Feature(final long mask) {
            this.mask = mask;
        }
        
        private static /* synthetic */ Feature[] $values() {
            return new Feature[] { Feature.AlwaysReturnList, Feature.NullOnError, Feature.KeepNullValue };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
