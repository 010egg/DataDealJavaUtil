// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.io.OutputStream;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.reader.ObjectReaderBean;
import java.io.IOException;
import java.io.InputStream;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.util.MultiType;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.util.ParameterizedTypeImpl;
import java.util.List;
import java.lang.reflect.Type;

public interface JSONB
{
    default void dump(final byte[] jsonbBytes) {
        System.out.println(toJSONString(jsonbBytes, true));
    }
    
    default void dump(final byte[] jsonbBytes, final SymbolTable symbolTable) {
        final JSONBDump dump = new JSONBDump(jsonbBytes, symbolTable, true);
        final String str = dump.toString();
        System.out.println(str);
    }
    
    default byte[] toBytes(final boolean v) {
        return new byte[] { (byte)(v ? -79 : -80) };
    }
    
    default byte[] toBytes(final int i) {
        if (i >= -16 && i <= 47) {
            return new byte[] { (byte)i };
        }
        final JSONWriter jsonWriter = JSONWriter.ofJSONB();
        try {
            jsonWriter.writeInt32(i);
            final byte[] bytes = jsonWriter.getBytes();
            if (jsonWriter != null) {
                jsonWriter.close();
            }
            return bytes;
        }
        catch (Throwable t) {
            if (jsonWriter != null) {
                try {
                    jsonWriter.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default byte[] toBytes(final byte i) {
        final JSONWriter jsonWriter = JSONWriter.ofJSONB();
        try {
            jsonWriter.writeInt8(i);
            final byte[] bytes = jsonWriter.getBytes();
            if (jsonWriter != null) {
                jsonWriter.close();
            }
            return bytes;
        }
        catch (Throwable t) {
            if (jsonWriter != null) {
                try {
                    jsonWriter.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default byte[] toBytes(final short i) {
        final JSONWriter jsonWriter = JSONWriter.ofJSONB();
        try {
            jsonWriter.writeInt16(i);
            final byte[] bytes = jsonWriter.getBytes();
            if (jsonWriter != null) {
                jsonWriter.close();
            }
            return bytes;
        }
        catch (Throwable t) {
            if (jsonWriter != null) {
                try {
                    jsonWriter.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default byte[] toBytes(final long i) {
        if (i >= -8L && i <= 15L) {
            return new byte[] { (byte)(-40L + (i + 8L)) };
        }
        final JSONWriter jsonWriter = JSONWriter.ofJSONB();
        try {
            jsonWriter.writeInt64(i);
            final byte[] bytes = jsonWriter.getBytes();
            if (jsonWriter != null) {
                jsonWriter.close();
            }
            return bytes;
        }
        catch (Throwable t) {
            if (jsonWriter != null) {
                try {
                    jsonWriter.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default int writeInt(final byte[] bytes, final int off, final int i) {
        if (i >= -16 && i <= 47) {
            bytes[off] = (byte)i;
            return 1;
        }
        if (i >= -2048 && i <= 2047) {
            bytes[off] = (byte)(56 + (i >> 8));
            bytes[off + 1] = (byte)i;
            return 2;
        }
        if (i >= -262144 && i <= 262143) {
            bytes[off] = (byte)(68 + (i >> 16));
            bytes[off + 1] = (byte)(i >> 8);
            bytes[off + 2] = (byte)i;
            return 3;
        }
        bytes[off] = 72;
        bytes[off + 1] = (byte)(i >>> 24);
        bytes[off + 2] = (byte)(i >>> 16);
        bytes[off + 3] = (byte)(i >>> 8);
        bytes[off + 4] = (byte)i;
        return 5;
    }
    
    default Object parse(final byte[] jsonbBytes, final JSONReader.Feature... features) {
        final JSONReaderJSONB reader = new JSONReaderJSONB(new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider(), features), jsonbBytes, 0, jsonbBytes.length);
        try {
            final Object object = reader.readAnyObject();
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final Object o = object;
            reader.close();
            return o;
        }
        catch (Throwable t) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default Object parse(final byte[] jsonbBytes, final SymbolTable symbolTable, final JSONReader.Feature... features) {
        final JSONReaderJSONB reader = new JSONReaderJSONB(new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider(), symbolTable, features), jsonbBytes, 0, jsonbBytes.length);
        try {
            final Object object = reader.readAnyObject();
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final Object o = object;
            reader.close();
            return o;
        }
        catch (Throwable t) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final byte[] jsonbBytes) {
        final JSONReaderJSONB reader = new JSONReaderJSONB(new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider()), jsonbBytes, 0, jsonbBytes.length);
        try {
            final JSONObject object = (JSONObject)reader.readObject();
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final JSONObject jsonObject = object;
            reader.close();
            return jsonObject;
        }
        catch (Throwable t) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final byte[] jsonbBytes, final JSONReader.Feature... features) {
        final JSONReaderJSONB reader = new JSONReaderJSONB(new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider(), features), jsonbBytes, 0, jsonbBytes.length);
        try {
            final JSONObject object = (JSONObject)reader.readObject();
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final JSONObject jsonObject = object;
            reader.close();
            return jsonObject;
        }
        catch (Throwable t) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default JSONArray parseArray(final byte[] jsonbBytes) {
        final JSONReaderJSONB reader = new JSONReaderJSONB(new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider()), jsonbBytes, 0, jsonbBytes.length);
        try {
            final JSONArray array = (JSONArray)reader.readArray();
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(array);
            }
            final JSONArray jsonArray = array;
            reader.close();
            return jsonArray;
        }
        catch (Throwable t) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final byte[] jsonbBytes, final Type type) {
        if (jsonbBytes == null || jsonbBytes.length == 0) {
            return null;
        }
        final Type paramType = new ParameterizedTypeImpl(new Type[] { type }, null, List.class);
        final JSONReaderJSONB reader = new JSONReaderJSONB(new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider()), jsonbBytes, 0, jsonbBytes.length);
        try {
            final List<T> list = reader.read(paramType);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            final List<T> list2 = list;
            reader.close();
            return list2;
        }
        catch (Throwable t) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final byte[] jsonbBytes, final Type type, final JSONReader.Feature... features) {
        if (jsonbBytes == null || jsonbBytes.length == 0) {
            return null;
        }
        final Type paramType = new ParameterizedTypeImpl(new Type[] { type }, null, List.class);
        final JSONReaderJSONB reader = new JSONReaderJSONB(new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider(), features), jsonbBytes, 0, jsonbBytes.length);
        try {
            final List<T> list = reader.read(paramType);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            final List<T> list2 = list;
            reader.close();
            return list2;
        }
        catch (Throwable t) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final byte[] jsonbBytes, final Type... types) {
        if (jsonbBytes == null || jsonbBytes.length == 0) {
            return null;
        }
        final JSONReaderJSONB reader = new JSONReaderJSONB(new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider()), jsonbBytes, 0, jsonbBytes.length);
        try {
            final List<T> list = (List<T>)reader.readList(types);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            final List<T> list2 = list;
            reader.close();
            return list2;
        }
        catch (Throwable t) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final byte[] jsonbBytes, final Type[] types, final JSONReader.Feature... features) {
        if (jsonbBytes == null || jsonbBytes.length == 0) {
            return null;
        }
        final JSONReaderJSONB reader = new JSONReaderJSONB(new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider(), features), jsonbBytes, 0, jsonbBytes.length);
        try {
            final List<T> list = (List<T>)reader.readList(types);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            final List<T> list2 = list;
            reader.close();
            return list2;
        }
        catch (Throwable t) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final Class<T> objectClass) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReaderJSONB jsonReader = new JSONReaderJSONB(new JSONReader.Context(provider), jsonbBytes, 0, jsonbBytes.length);
        try {
            Object object;
            if (objectClass == Object.class) {
                object = jsonReader.readAny();
            }
            else {
                final ObjectReader objectReader = provider.getObjectReader(objectClass, (JSONFactory.defaultReaderFeatures & JSONReader.Feature.FieldBased.mask) != 0x0L);
                object = objectReader.readJSONBObject(jsonReader, objectClass, null, 0L);
            }
            if (jsonReader.resolveTasks != null) {
                jsonReader.handleResolveTasks(object);
            }
            final Object o = object;
            jsonReader.close();
            return (T)o;
        }
        catch (Throwable t) {
            try {
                jsonReader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final Type objectType) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final ObjectReader objectReader = provider.getObjectReader(objectType);
        final JSONReaderJSONB jsonReader = new JSONReaderJSONB(new JSONReader.Context(provider), jsonbBytes, 0, jsonbBytes.length);
        try {
            final T object = objectReader.readJSONBObject(jsonReader, objectType, null, 0L);
            if (jsonReader.resolveTasks != null) {
                jsonReader.handleResolveTasks(object);
            }
            final T t = object;
            jsonReader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                jsonReader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final Type... types) {
        return parseObject(jsonbBytes, new MultiType(types));
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final Type objectType, final SymbolTable symbolTable) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final ObjectReader objectReader = provider.getObjectReader(objectType);
        final JSONReaderJSONB reader = new JSONReaderJSONB(new JSONReader.Context(provider, symbolTable), jsonbBytes, 0, jsonbBytes.length);
        try {
            final T object = objectReader.readJSONBObject(reader, objectType, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final T t = object;
            reader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final Type objectType, final SymbolTable symbolTable, final JSONReader.Feature... features) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider, symbolTable, features);
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = provider.getObjectReader(objectType, fieldBased);
        final JSONReaderJSONB reader = new JSONReaderJSONB(context, jsonbBytes, 0, jsonbBytes.length);
        try {
            final T object = objectReader.readJSONBObject(reader, objectType, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final T t = object;
            reader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final Class<T> objectClass, final Filter filter, final JSONReader.Feature... features) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider, filter, features);
        final JSONReaderJSONB jsonReader = new JSONReaderJSONB(context, jsonbBytes, 0, jsonbBytes.length);
        try {
            for (int i = 0; i < features.length; ++i) {
                final JSONReader.Context context2 = context;
                context2.features |= features[i].mask;
            }
            Object object;
            if (objectClass == Object.class) {
                object = jsonReader.readAnyObject();
            }
            else {
                final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
                final ObjectReader objectReader = provider.getObjectReader(objectClass, fieldBased);
                object = objectReader.readJSONBObject(jsonReader, objectClass, null, 0L);
            }
            if (jsonReader.resolveTasks != null) {
                jsonReader.handleResolveTasks(object);
            }
            final Object o = object;
            jsonReader.close();
            return (T)o;
        }
        catch (Throwable t) {
            try {
                jsonReader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final Type objectType, final SymbolTable symbolTable, final Filter[] filters, final JSONReader.Feature... features) {
        if (jsonbBytes == null || jsonbBytes.length == 0) {
            return null;
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider, symbolTable, filters, features);
        final JSONReaderJSONB jsonReader = new JSONReaderJSONB(context, jsonbBytes, 0, jsonbBytes.length);
        try {
            for (int i = 0; i < features.length; ++i) {
                final JSONReader.Context context2 = context;
                context2.features |= features[i].mask;
            }
            Object object;
            if (objectType == Object.class) {
                object = jsonReader.readAnyObject();
            }
            else {
                final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
                final ObjectReader objectReader = provider.getObjectReader(objectType, fieldBased);
                object = objectReader.readJSONBObject(jsonReader, objectType, null, 0L);
            }
            if (jsonReader.resolveTasks != null) {
                jsonReader.handleResolveTasks(object);
            }
            final Object o = object;
            jsonReader.close();
            return (T)o;
        }
        catch (Throwable t) {
            try {
                jsonReader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default <T> T copy(final T object, final JSONWriter.Feature... features) {
        return JSON.copy(object, features);
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final TypeReference typeReference, final JSONReader.Feature... features) {
        return parseObject(jsonbBytes, typeReference.getType(), features);
    }
    
    default <T> T parseObject(final InputStream in, final Class objectClass, final JSONReader.Feature... features) throws IOException {
        return parseObject(in, objectClass, JSONFactory.createReadContext(features));
    }
    
    default <T> T parseObject(final InputStream in, final Type objectType, final JSONReader.Feature... features) throws IOException {
        return parseObject(in, objectType, JSONFactory.createReadContext(features));
    }
    
    default <T> T parseObject(final InputStream in, final Type objectType, final JSONReader.Context context) {
        final JSONReaderJSONB jsonReader = new JSONReaderJSONB(context, in);
        try {
            Object object;
            if (objectType == Object.class) {
                object = jsonReader.readAny();
            }
            else {
                final ObjectReader objectReader = context.getObjectReader(objectType);
                object = objectReader.readJSONBObject(jsonReader, objectType, null, 0L);
            }
            if (jsonReader.resolveTasks != null) {
                jsonReader.handleResolveTasks(object);
            }
            final Object o = object;
            jsonReader.close();
            return (T)o;
        }
        catch (Throwable t) {
            try {
                jsonReader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default <T> T parseObject(final InputStream in, final Class objectClass, final JSONReader.Context context) {
        final JSONReaderJSONB jsonReader = new JSONReaderJSONB(context, in);
        try {
            Object object;
            if (objectClass == Object.class) {
                object = jsonReader.readAny();
            }
            else {
                final ObjectReader objectReader = context.getObjectReader(objectClass);
                object = objectReader.readJSONBObject(jsonReader, objectClass, null, 0L);
            }
            if (jsonReader.resolveTasks != null) {
                jsonReader.handleResolveTasks(object);
            }
            final Object o = object;
            jsonReader.close();
            return (T)o;
        }
        catch (Throwable t) {
            try {
                jsonReader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default <T> T parseObject(final InputStream in, final int length, final Type objectType, final JSONReader.Context context) throws IOException {
        final int cacheIndex = System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1;
        final JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[cacheIndex];
        byte[] bytes = JSONFactory.BYTES_UPDATER.getAndSet(cacheItem, null);
        if (bytes == null) {
            bytes = new byte[8192];
        }
        try {
            if (bytes.length < length) {
                bytes = new byte[length];
            }
            final int read = in.read(bytes, 0, length);
            if (read != length) {
                throw new IllegalArgumentException("deserialize failed. expected read length: " + length + " but actual read: " + read);
            }
            return parseObject(bytes, 0, length, objectType, context);
        }
        finally {
            JSONFactory.BYTES_UPDATER.lazySet(cacheItem, bytes);
        }
    }
    
    default <T> T parseObject(final InputStream in, final int length, final Type objectType, final JSONReader.Feature... features) throws IOException {
        final int cacheIndex = System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1;
        final JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[cacheIndex];
        byte[] bytes = JSONFactory.BYTES_UPDATER.getAndSet(cacheItem, null);
        if (bytes == null) {
            bytes = new byte[8192];
        }
        try {
            if (bytes.length < length) {
                bytes = new byte[length];
            }
            final int read = in.read(bytes, 0, length);
            if (read != length) {
                throw new IllegalArgumentException("deserialize failed. expected read length: " + length + " but actual read: " + read);
            }
            return parseObject(bytes, 0, length, objectType, features);
        }
        finally {
            JSONFactory.BYTES_UPDATER.lazySet(cacheItem, bytes);
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final Class<T> objectClass, final JSONReader.Feature... features) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider, features);
        final JSONReaderJSONB jsonReader = new JSONReaderJSONB(context, jsonbBytes, 0, jsonbBytes.length);
        try {
            Object object;
            if (objectClass == Object.class) {
                object = jsonReader.readAnyObject();
            }
            else {
                final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
                final ObjectReader objectReader = provider.getObjectReader(objectClass, fieldBased);
                if ((context.features & JSONReader.Feature.SupportArrayToBean.mask) != 0x0L && jsonReader.isArray() && objectReader instanceof ObjectReaderBean) {
                    object = objectReader.readArrayMappingJSONBObject(jsonReader, objectClass, null, 0L);
                }
                else {
                    object = objectReader.readJSONBObject(jsonReader, objectClass, null, 0L);
                }
            }
            if (jsonReader.resolveTasks != null) {
                jsonReader.handleResolveTasks(object);
            }
            final Object o = object;
            jsonReader.close();
            return (T)o;
        }
        catch (Throwable t) {
            try {
                jsonReader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final Class<T> objectClass, final JSONReader.Context context) {
        final JSONReaderJSONB jsonReader = new JSONReaderJSONB(context, jsonbBytes, 0, jsonbBytes.length);
        try {
            Object object;
            if (objectClass == Object.class) {
                object = jsonReader.readAnyObject();
            }
            else {
                final ObjectReader objectReader = context.provider.getObjectReader(objectClass, (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L);
                if ((context.features & JSONReader.Feature.SupportArrayToBean.mask) != 0x0L && jsonReader.isArray() && objectReader instanceof ObjectReaderBean) {
                    object = objectReader.readArrayMappingJSONBObject(jsonReader, objectClass, null, 0L);
                }
                else {
                    object = objectReader.readJSONBObject(jsonReader, objectClass, null, 0L);
                }
            }
            if (jsonReader.resolveTasks != null) {
                jsonReader.handleResolveTasks(object);
            }
            final Object o = object;
            jsonReader.close();
            return (T)o;
        }
        catch (Throwable t) {
            try {
                jsonReader.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final Type objectClass, final JSONReader.Feature... features) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider, features);
        final ObjectReader objectReader = provider.getObjectReader(objectClass, (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L);
        final JSONReaderJSONB reader = new JSONReaderJSONB(context, jsonbBytes, 0, jsonbBytes.length);
        try {
            final T object = objectReader.readJSONBObject(reader, objectClass, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final T t = object;
            reader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final int off, final int len, final Class<T> objectClass) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider);
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = provider.getObjectReader(objectClass, fieldBased);
        final JSONReaderJSONB reader = new JSONReaderJSONB(context, jsonbBytes, off, len);
        try {
            final T object = objectReader.readJSONBObject(reader, objectClass, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final T t = object;
            reader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final int off, final int len, final Type type) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider);
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = provider.getObjectReader(type, fieldBased);
        final JSONReaderJSONB reader = new JSONReaderJSONB(context, jsonbBytes, off, len);
        try {
            final T object = objectReader.readJSONBObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final T t = object;
            reader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final int off, final int len, final Class<T> objectClass, final JSONReader.Feature... features) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider, features);
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = provider.getObjectReader(objectClass, fieldBased);
        final JSONReaderJSONB reader = new JSONReaderJSONB(context, jsonbBytes, off, len);
        try {
            final T object = objectReader.readJSONBObject(reader, objectClass, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final T t = object;
            reader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final int off, final int len, final Type objectType, final JSONReader.Context context) {
        final JSONReaderJSONB reader = new JSONReaderJSONB(context, jsonbBytes, off, len);
        try {
            final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
            final ObjectReader objectReader = context.provider.getObjectReader(objectType, fieldBased);
            final T object = objectReader.readJSONBObject(reader, objectType, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final T t = object;
            reader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final int off, final int len, final Type objectType, final JSONReader.Feature... features) {
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReaderJSONB reader = new JSONReaderJSONB(context, jsonbBytes, off, len);
        try {
            final ObjectReader objectReader = reader.getObjectReader(objectType);
            final T object = objectReader.readJSONBObject(reader, objectType, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final T t = object;
            reader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final int off, final int len, final Class<T> objectClass, final SymbolTable symbolTable) {
        final JSONReader.Context context = JSONFactory.createReadContext(symbolTable);
        final ObjectReader objectReader = context.getObjectReader(objectClass);
        final JSONReaderJSONB reader = new JSONReaderJSONB(context, jsonbBytes, off, len);
        try {
            final T object = objectReader.readJSONBObject(reader, objectClass, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final T t = object;
            reader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final int off, final int len, final Type objectClass, final SymbolTable symbolTable) {
        final JSONReader.Context context = JSONFactory.createReadContext(symbolTable);
        final ObjectReader objectReader = context.getObjectReader(objectClass);
        final JSONReaderJSONB reader = new JSONReaderJSONB(context, jsonbBytes, off, len);
        try {
            final T object = objectReader.readJSONBObject(reader, objectClass, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final T t = object;
            reader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final int off, final int len, final Class<T> objectClass, final SymbolTable symbolTable, final JSONReader.Feature... features) {
        final JSONReader.Context context = JSONFactory.createReadContext(symbolTable, features);
        final ObjectReader objectReader = context.getObjectReader(objectClass);
        final JSONReaderJSONB reader = new JSONReaderJSONB(context, jsonbBytes, off, len);
        try {
            final T object = objectReader.readJSONBObject(reader, objectClass, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final T t = object;
            reader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] jsonbBytes, final int off, final int len, final Type objectClass, final SymbolTable symbolTable, final JSONReader.Feature... features) {
        final JSONReader.Context context = JSONFactory.createReadContext(symbolTable, features);
        final ObjectReader objectReader = context.getObjectReader(objectClass);
        final JSONReaderJSONB reader = new JSONReaderJSONB(context, jsonbBytes, off, len);
        try {
            final T object = objectReader.readJSONBObject(reader, objectClass, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            final T t = object;
            reader.close();
            return t;
        }
        catch (Throwable t2) {
            try {
                reader.close();
            }
            catch (Throwable exception) {
                t2.addSuppressed(exception);
            }
            throw t2;
        }
    }
    
    default byte[] toBytes(final String str) {
        if (str == null) {
            return new byte[] { -81 };
        }
        if (JDKUtils.JVM_VERSION == 8) {
            final char[] chars = JDKUtils.getCharArray(str);
            final int strlen = chars.length;
            if (strlen <= 47) {
                boolean ascii = true;
                for (int i = 0; i < strlen; ++i) {
                    if (chars[i] > '\u007f') {
                        ascii = false;
                        break;
                    }
                }
                if (ascii) {
                    final byte[] bytes = new byte[chars.length + 1];
                    bytes[0] = (byte)(strlen + 73);
                    for (int j = 0; j < strlen; ++j) {
                        bytes[j + 1] = (byte)chars[j];
                    }
                    return bytes;
                }
            }
        }
        else if (JDKUtils.STRING_VALUE != null) {
            final int coder = JDKUtils.STRING_CODER.applyAsInt(str);
            if (coder == 0) {
                final byte[] value = JDKUtils.STRING_VALUE.apply(str);
                final int strlen2 = value.length;
                if (strlen2 <= 47) {
                    final byte[] bytes = new byte[value.length + 1];
                    bytes[0] = (byte)(strlen2 + 73);
                    System.arraycopy(value, 0, bytes, 1, value.length);
                    return bytes;
                }
            }
        }
        final JSONWriterJSONB writer = new JSONWriterJSONB(new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider), null);
        try {
            writer.writeString(str);
            final byte[] bytes2 = writer.getBytes();
            writer.close();
            return bytes2;
        }
        catch (Throwable t) {
            try {
                writer.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default byte[] toBytes(final String str, final Charset charset) {
        if (str == null) {
            return new byte[] { -81 };
        }
        byte type;
        if (charset == StandardCharsets.UTF_16) {
            type = 123;
        }
        else if (charset == StandardCharsets.UTF_16BE) {
            type = 125;
        }
        else if (charset == StandardCharsets.UTF_16LE) {
            type = 124;
        }
        else if (charset == StandardCharsets.UTF_8) {
            type = 122;
        }
        else if (charset == StandardCharsets.US_ASCII || charset == StandardCharsets.ISO_8859_1) {
            type = 121;
        }
        else {
            if (charset == null || !"GB18030".equals(charset.name())) {
                return toBytes(str);
            }
            type = 126;
        }
        final byte[] utf16 = str.getBytes(charset);
        int byteslen = 2 + utf16.length;
        if (utf16.length <= 47) {
            byteslen += 0;
        }
        else if (utf16.length <= 2047) {
            ++byteslen;
        }
        else if (utf16.length <= 262143) {
            byteslen += 2;
        }
        else {
            byteslen += 4;
        }
        final byte[] bytes = new byte[byteslen];
        bytes[0] = type;
        int off = 1;
        off += writeInt(bytes, off, utf16.length);
        System.arraycopy(utf16, 0, bytes, off, utf16.length);
        return bytes;
    }
    
    default byte[] toBytes(final Object object) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider);
        final JSONWriterJSONB writer = new JSONWriterJSONB(context, null);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                final Class<?> valueClass = object.getClass();
                final boolean fieldBased = (context.features & JSONWriter.Feature.FieldBased.mask) != 0x0L;
                final ObjectWriter objectWriter = context.provider.getObjectWriter(valueClass, valueClass, fieldBased);
                objectWriter.writeJSONB(writer, object, null, null, 0L);
            }
            final byte[] bytes = writer.getBytes();
            writer.close();
            return bytes;
        }
        catch (Throwable t) {
            try {
                writer.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default byte[] toBytes(final Object object, JSONWriter.Context context) {
        if (context == null) {
            context = JSONFactory.createWriteContext();
        }
        final JSONWriterJSONB writer = new JSONWriterJSONB(context, null);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                final boolean fieldBased = (context.features & JSONWriter.Feature.FieldBased.mask) != 0x0L;
                final Class<?> valueClass = object.getClass();
                final ObjectWriter objectWriter = context.provider.getObjectWriter(valueClass, valueClass, fieldBased);
                if ((context.features & JSONWriter.Feature.BeanToArray.mask) != 0x0L) {
                    objectWriter.writeArrayMappingJSONB(writer, object, null, null, 0L);
                }
                else {
                    objectWriter.writeJSONB(writer, object, null, null, 0L);
                }
            }
            final byte[] bytes = writer.getBytes();
            writer.close();
            return bytes;
        }
        catch (Throwable t) {
            try {
                writer.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default byte[] toBytes(final Object object, final SymbolTable symbolTable) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider);
        final JSONWriterJSONB writer = new JSONWriterJSONB(context, symbolTable);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.setRootObject(object);
                final Class<?> valueClass = object.getClass();
                final ObjectWriter objectWriter = context.getObjectWriter(valueClass, valueClass);
                objectWriter.writeJSONB(writer, object, null, null, 0L);
            }
            final byte[] bytes = writer.getBytes();
            writer.close();
            return bytes;
        }
        catch (Throwable t) {
            try {
                writer.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default byte[] toBytes(final Object object, final SymbolTable symbolTable, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        final JSONWriterJSONB writer = new JSONWriterJSONB(context, symbolTable);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.setRootObject(object);
                final Class<?> valueClass = object.getClass();
                final boolean fieldBased = (context.features & JSONWriter.Feature.FieldBased.mask) != 0x0L;
                final ObjectWriter objectWriter = context.provider.getObjectWriter(valueClass, valueClass, fieldBased);
                if ((context.features & JSONWriter.Feature.BeanToArray.mask) != 0x0L) {
                    objectWriter.writeArrayMappingJSONB(writer, object, null, null, 0L);
                }
                else {
                    objectWriter.writeJSONB(writer, object, null, null, 0L);
                }
            }
            final byte[] bytes = writer.getBytes();
            writer.close();
            return bytes;
        }
        catch (Throwable t) {
            try {
                writer.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default byte[] toBytes(final Object object, final SymbolTable symbolTable, final Filter[] filters, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        context.configFilter(filters);
        final JSONWriterJSONB writer = new JSONWriterJSONB(context, symbolTable);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.setRootObject(object);
                final Class<?> valueClass = object.getClass();
                final boolean fieldBased = (context.features & JSONWriter.Feature.FieldBased.mask) != 0x0L;
                final ObjectWriter objectWriter = context.provider.getObjectWriter(valueClass, valueClass, fieldBased);
                if ((context.features & JSONWriter.Feature.BeanToArray.mask) != 0x0L) {
                    objectWriter.writeArrayMappingJSONB(writer, object, null, null, 0L);
                }
                else {
                    objectWriter.writeJSONB(writer, object, null, null, 0L);
                }
            }
            final byte[] bytes = writer.getBytes();
            writer.close();
            return bytes;
        }
        catch (Throwable t) {
            try {
                writer.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default byte[] toBytes(final Object object, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        final JSONWriterJSONB writer = new JSONWriterJSONB(context, null);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                final boolean fieldBased = (context.features & JSONWriter.Feature.FieldBased.mask) != 0x0L;
                final Class<?> valueClass = object.getClass();
                final ObjectWriter objectWriter = context.provider.getObjectWriter(valueClass, valueClass, fieldBased);
                if ((context.features & JSONWriter.Feature.BeanToArray.mask) != 0x0L) {
                    objectWriter.writeArrayMappingJSONB(writer, object, null, null, 0L);
                }
                else {
                    objectWriter.writeJSONB(writer, object, null, null, 0L);
                }
            }
            final byte[] bytes = writer.getBytes();
            writer.close();
            return bytes;
        }
        catch (Throwable t) {
            try {
                writer.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    default SymbolTable symbolTable(final String... names) {
        return new SymbolTable(names);
    }
    
    default String toJSONString(final byte[] jsonbBytes) {
        return new JSONBDump(jsonbBytes, false).toString();
    }
    
    default String toJSONString(final byte[] jsonbBytes, final boolean raw) {
        return new JSONBDump(jsonbBytes, raw).toString();
    }
    
    default String toJSONString(final byte[] jsonbBytes, final SymbolTable symbolTable) {
        return new JSONBDump(jsonbBytes, symbolTable, false).toString();
    }
    
    default int writeTo(final OutputStream out, final Object object, final JSONWriter.Feature... features) {
        try {
            final JSONWriterJSONB writer = new JSONWriterJSONB(new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider), null);
            try {
                writer.config(features);
                if (object == null) {
                    writer.writeNull();
                }
                else {
                    writer.setRootObject(object);
                    final Class<?> valueClass = object.getClass();
                    final ObjectWriter objectWriter = writer.getObjectWriter(valueClass, valueClass);
                    objectWriter.writeJSONB(writer, object, null, null, 0L);
                }
                final int flushTo = writer.flushTo(out);
                writer.close();
                return flushTo;
            }
            catch (Throwable t) {
                try {
                    writer.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
                throw t;
            }
        }
        catch (IOException e) {
            throw new JSONException("writeJSONString error", e);
        }
    }
    
    default byte[] fromJSONString(final String str) {
        return toBytes(JSON.parse(str));
    }
    
    default byte[] fromJSONBytes(final byte[] jsonUtf8Bytes) {
        final JSONReader reader = JSONReader.of(jsonUtf8Bytes);
        final ObjectReader objectReader = reader.getObjectReader(Object.class);
        final Object object = objectReader.readObject(reader, null, null, 0L);
        return toBytes(object);
    }
    
    default String typeName(final byte type) {
        switch (type) {
            case -90: {
                return "OBJECT " + Integer.toString(type);
            }
            case -91: {
                return "OBJECT_END " + Integer.toString(type);
            }
            case -109: {
                return "REFERENCE " + Integer.toString(type);
            }
            case Byte.MAX_VALUE: {
                return "SYMBOL " + Integer.toString(type);
            }
            case -81: {
                return "NULL " + Integer.toString(type);
            }
            case -79: {
                return "TRUE " + Integer.toString(type);
            }
            case -80: {
                return "FALSE " + Integer.toString(type);
            }
            case 122: {
                return "STR_UTF8 " + Integer.toString(type);
            }
            case 123: {
                return "STR_UTF16 " + Integer.toString(type);
            }
            case 124: {
                return "STR_UTF16LE " + Integer.toString(type);
            }
            case 125: {
                return "STR_UTF16BE " + Integer.toString(type);
            }
            case -67: {
                return "INT8 " + Integer.toString(type);
            }
            case -68: {
                return "INT16 " + Integer.toString(type);
            }
            case 72: {
                return "INT32 " + Integer.toString(type);
            }
            case -66:
            case -65: {
                return "INT64 " + Integer.toString(type);
            }
            case -74:
            case -73: {
                return "FLOAT " + Integer.toString(type);
            }
            case -78:
            case -77:
            case -76:
            case -75: {
                return "DOUBLE " + Integer.toString(type);
            }
            case -70:
            case -69: {
                return "BIGINT " + Integer.toString(type);
            }
            case -72:
            case -71: {
                return "DECIMAL " + Integer.toString(type);
            }
            case -89: {
                return "LOCAL_TIME " + Integer.toString(type);
            }
            case -111: {
                return "BINARY " + Integer.toString(type);
            }
            case -88: {
                return "LOCAL_DATETIME " + Integer.toString(type);
            }
            case -82: {
                return "TIMESTAMP " + Integer.toString(type);
            }
            case -83: {
                return "TIMESTAMP_MINUTES " + Integer.toString(type);
            }
            case -84: {
                return "TIMESTAMP_SECONDS " + Integer.toString(type);
            }
            case -85: {
                return "TIMESTAMP_MILLIS " + Integer.toString(type);
            }
            case -86: {
                return "TIMESTAMP_WITH_TIMEZONE " + Integer.toString(type);
            }
            case -87: {
                return "LOCAL_DATE " + Integer.toString(type);
            }
            case -110: {
                return "TYPED_ANY " + Integer.toString(type);
            }
            default: {
                if (type >= -108 && type <= -92) {
                    return "ARRAY " + Integer.toString(type);
                }
                if (type >= 73 && type <= 121) {
                    return "STR_ASCII " + Integer.toString(type);
                }
                if (type >= -16 && type <= 47) {
                    return "INT32 " + Integer.toString(type);
                }
                if (type >= 48 && type <= 63) {
                    return "INT32 " + Integer.toString(type);
                }
                if (type >= 64 && type <= 71) {
                    return "INT32 " + Integer.toString(type);
                }
                if (type >= -40 && type <= -17) {
                    return "INT64 " + Integer.toString(type);
                }
                if (type >= -56 && type <= -41) {
                    return "INT64 " + Integer.toString(type);
                }
                if (type >= -64 && type <= -57) {
                    return "INT64 " + Integer.toString(type);
                }
                return Integer.toString(type);
            }
        }
    }
    
    public interface Constants
    {
        public static final byte BC_CHAR = -112;
        public static final byte BC_BINARY = -111;
        public static final byte BC_TYPED_ANY = -110;
        public static final byte BC_REFERENCE = -109;
        public static final int ARRAY_FIX_LEN = 15;
        public static final byte BC_ARRAY_FIX_0 = -108;
        public static final byte BC_ARRAY_FIX_MIN = -108;
        public static final byte BC_ARRAY_FIX_MAX = -93;
        public static final byte BC_ARRAY = -92;
        public static final byte BC_OBJECT_END = -91;
        public static final byte BC_OBJECT = -90;
        public static final byte BC_LOCAL_TIME = -89;
        public static final byte BC_LOCAL_DATETIME = -88;
        public static final byte BC_LOCAL_DATE = -87;
        public static final byte BC_TIMESTAMP_WITH_TIMEZONE = -86;
        public static final byte BC_TIMESTAMP_MILLIS = -85;
        public static final byte BC_TIMESTAMP_SECONDS = -84;
        public static final byte BC_TIMESTAMP_MINUTES = -83;
        public static final byte BC_TIMESTAMP = -82;
        public static final byte BC_NULL = -81;
        public static final byte BC_FALSE = -80;
        public static final byte BC_TRUE = -79;
        public static final byte BC_DOUBLE_NUM_0 = -78;
        public static final byte BC_DOUBLE_NUM_1 = -77;
        public static final byte BC_DOUBLE_LONG = -76;
        public static final byte BC_DOUBLE = -75;
        public static final byte BC_FLOAT_INT = -74;
        public static final byte BC_FLOAT = -73;
        public static final byte BC_DECIMAL_LONG = -72;
        public static final byte BC_DECIMAL = -71;
        public static final byte BC_BIGINT_LONG = -70;
        public static final byte BC_BIGINT = -69;
        public static final byte BC_INT16 = -68;
        public static final byte BC_INT8 = -67;
        public static final byte BC_INT64 = -66;
        public static final byte BC_INT64_INT = -65;
        public static final int INT64_SHORT_MIN = -262144;
        public static final int INT64_SHORT_MAX = 262143;
        public static final int INT64_BYTE_MIN = -2048;
        public static final int INT64_BYTE_MAX = 2047;
        public static final byte BC_INT64_SHORT_MIN = -64;
        public static final byte BC_INT64_SHORT_ZERO = -60;
        public static final byte BC_INT64_SHORT_MAX = -57;
        public static final byte BC_INT64_BYTE_MIN = -56;
        public static final byte BC_INT64_BYTE_ZERO = -48;
        public static final byte BC_INT64_BYTE_MAX = -41;
        public static final byte BC_INT64_NUM_MIN = -40;
        public static final byte BC_INT64_NUM_MAX = -17;
        public static final int INT64_NUM_LOW_VALUE = -8;
        public static final int INT64_NUM_HIGH_VALUE = 15;
        public static final byte BC_INT32_NUM_0 = 0;
        public static final byte BC_INT32_NUM_1 = 1;
        public static final byte BC_INT32_NUM_16 = 16;
        public static final byte BC_INT32_NUM_MIN = -16;
        public static final byte BC_INT32_NUM_MAX = 47;
        public static final byte BC_INT32_BYTE_MIN = 48;
        public static final byte BC_INT32_BYTE_ZERO = 56;
        public static final byte BC_INT32_BYTE_MAX = 63;
        public static final byte BC_INT32_SHORT_MIN = 64;
        public static final byte BC_INT32_SHORT_ZERO = 68;
        public static final byte BC_INT32_SHORT_MAX = 71;
        public static final byte BC_INT32 = 72;
        public static final int INT32_BYTE_MIN = -2048;
        public static final int INT32_BYTE_MAX = 2047;
        public static final int INT32_SHORT_MIN = -262144;
        public static final int INT32_SHORT_MAX = 262143;
        public static final byte BC_STR_ASCII_FIX_0 = 73;
        public static final byte BC_STR_ASCII_FIX_1 = 74;
        public static final byte BC_STR_ASCII_FIX_4 = 77;
        public static final byte BC_STR_ASCII_FIX_5 = 78;
        public static final byte BC_STR_ASCII_FIX_32 = 105;
        public static final byte BC_STR_ASCII_FIX_36 = 109;
        public static final int STR_ASCII_FIX_LEN = 47;
        public static final byte BC_STR_ASCII_FIX_MIN = 73;
        public static final byte BC_STR_ASCII_FIX_MAX = 120;
        public static final byte BC_STR_ASCII = 121;
        public static final byte BC_STR_UTF8 = 122;
        public static final byte BC_STR_UTF16 = 123;
        public static final byte BC_STR_UTF16LE = 124;
        public static final byte BC_STR_UTF16BE = 125;
        public static final byte BC_STR_GB18030 = 126;
        public static final byte BC_SYMBOL = Byte.MAX_VALUE;
    }
}
