// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.time.LocalDate;
import com.alibaba.fastjson2.util.DateUtils;
import java.util.Date;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.writer.FieldWriter;
import java.util.HashMap;
import com.alibaba.fastjson2.reader.ObjectReaderNoneDefaultConstructor;
import com.alibaba.fastjson2.reader.ObjectReaderBean;
import java.time.ZoneId;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;
import com.alibaba.fastjson2.filter.PropertyFilter;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.LabelFilter;
import com.alibaba.fastjson2.filter.ContextValueFilter;
import com.alibaba.fastjson2.filter.ContextNameFilter;
import com.alibaba.fastjson2.filter.BeforeFilter;
import com.alibaba.fastjson2.filter.AfterFilter;
import com.alibaba.fastjson2.modules.ObjectWriterModule;
import com.alibaba.fastjson2.modules.ObjectReaderModule;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.io.OutputStream;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.nio.ByteBuffer;
import com.alibaba.fastjson2.util.MultiType;
import com.alibaba.fastjson2.util.MapMultiValueType;
import com.alibaba.fastjson2.filter.Filter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.Reader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface JSON
{
    public static final String VERSION = "2.0.39";
    
    default Object parse(final String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final char ch = reader.current();
            Object object;
            if (context.objectSupplier == null && (context.features & JSONReader.Feature.UseNativeObject.mask) == 0x0L && (ch == '{' || ch == '[')) {
                if (ch == '{') {
                    final JSONObject jsonObject = new JSONObject();
                    reader.read(jsonObject, 0L);
                    object = jsonObject;
                }
                else {
                    final JSONArray array = new JSONArray();
                    reader.read(array);
                    object = array;
                }
                if (reader.resolveTasks != null) {
                    reader.handleResolveTasks(object);
                }
            }
            else {
                final ObjectReader<?> objectReader = (ObjectReader<?>)provider.getObjectReader(Object.class, false);
                object = objectReader.readObject(reader, null, null, 0L);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final Object o = object;
            if (reader != null) {
                reader.close();
            }
            return o;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default Object parse(final String text, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider, features);
        final ObjectReader<?> objectReader = (ObjectReader<?>)provider.getObjectReader(Object.class, false);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            context.config(features);
            final Object object = objectReader.readObject(reader, null, null, 0L);
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final Object o = object;
            if (reader != null) {
                reader.close();
            }
            return o;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default Object parse(final String text, final int offset, final int length, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty() || length == 0) {
            return null;
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider, features);
        final ObjectReader<?> objectReader = (ObjectReader<?>)provider.getObjectReader(Object.class, false);
        final JSONReader reader = JSONReader.of(text, offset, length, context);
        try {
            final Object object = objectReader.readObject(reader, null, null, 0L);
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final Object o = object;
            if (reader != null) {
                reader.close();
            }
            return o;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default Object parse(final String text, final JSONReader.Context context) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final ObjectReader<?> objectReader = (ObjectReader<?>)context.provider.getObjectReader(Object.class, false);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final Object object = objectReader.readObject(reader, null, null, 0L);
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final Object o = object;
            if (reader != null) {
                reader.close();
            }
            return o;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default Object parse(final byte[] bytes, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider, features);
        final ObjectReader<?> objectReader = (ObjectReader<?>)provider.getObjectReader(Object.class, false);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            final Object object = objectReader.readObject(reader, null, null, 0L);
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final Object o = object;
            if (reader != null) {
                reader.close();
            }
            return o;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default Object parse(final char[] chars, final JSONReader.Feature... features) {
        if (chars == null || chars.length == 0) {
            return null;
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider, features);
        final ObjectReader<?> objectReader = (ObjectReader<?>)provider.getObjectReader(Object.class, false);
        final JSONReader reader = JSONReader.of(chars, context);
        try {
            final Object object = objectReader.readObject(reader, null, null, 0L);
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final Object o = object;
            if (reader != null) {
                reader.close();
            }
            return o;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final JSONReader reader = JSONReader.of(text, context);
        try {
            if (reader.nextIfNull()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final String text, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            if (reader.nextIfNull()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final String text, final int offset, final int length, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty() || length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(text, offset, length, context);
        try {
            if (reader.nextIfNull()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final String text, final int offset, final int length, final JSONReader.Context context) {
        if (text == null || text.isEmpty() || length == 0) {
            return null;
        }
        final JSONReader reader = JSONReader.of(text, offset, length, context);
        try {
            if (reader.nextIfNull()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final String text, final JSONReader.Context context) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader reader = JSONReader.of(text, context);
        try {
            if (reader.nextIfNull()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final Reader input, final JSONReader.Feature... features) {
        if (input == null) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(input, context);
        try {
            if (reader.isEnd()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final InputStream input, final JSONReader.Feature... features) {
        if (input == null) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(input, StandardCharsets.UTF_8, context);
        try {
            if (reader.isEnd()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            if (reader.nextIfNull()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final char[] chars) {
        if (chars == null || chars.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final JSONReader reader = JSONReader.of(chars, context);
        try {
            if (reader.nextIfNull()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final InputStream in, final Charset charset) {
        if (in == null) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final JSONReader reader = JSONReader.of(in, charset, context);
        try {
            if (reader.nextIfNull()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final URL url) {
        if (url == null) {
            return null;
        }
        try {
            final InputStream is = url.openStream();
            try {
                final JSONObject object = parseObject(is, StandardCharsets.UTF_8);
                if (is != null) {
                    is.close();
                }
                return object;
            }
            catch (Throwable t) {
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        catch (IOException e) {
            throw new JSONException("JSON#parseObject cannot parse '" + url + "'", e);
        }
    }
    
    default JSONObject parseObject(final byte[] bytes, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            if (reader.nextIfNull()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final byte[] bytes, final int offset, final int length, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0 || length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(bytes, offset, length, context);
        try {
            if (reader.nextIfNull()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final char[] chars, final int offset, final int length, final JSONReader.Feature... features) {
        if (chars == null || chars.length == 0 || length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(chars, offset, length, context);
        try {
            if (reader.nextIfNull()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONObject parseObject(final byte[] bytes, final int offset, final int length, final Charset charset, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0 || length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(bytes, offset, length, charset, context);
        try {
            if (reader.nextIfNull()) {
                final JSONObject jsonObject = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonObject;
            }
            final JSONObject object = new JSONObject();
            reader.read(object, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONObject jsonObject2 = object;
            if (reader != null) {
                reader.close();
            }
            return jsonObject2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default <T> T parseObject(final String text, final Class<T> clazz) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider);
        final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(clazz, (JSONFactory.defaultReaderFeatures & JSONReader.Feature.FieldBased.mask) != 0x0L);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final T object = objectReader.readObject(reader, clazz, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final String text, final Class<T> clazz, final Filter filter, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(filter, features);
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.provider.getObjectReader(clazz, fieldBased);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            if (reader.nextIfNull()) {
                final T t = null;
                if (reader != null) {
                    reader.close();
                }
                return t;
            }
            final T object = objectReader.readObject(reader, clazz, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t2 = object;
            if (reader != null) {
                reader.close();
            }
            return t2;
        }
        catch (Throwable t3) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t3.addSuppressed(exception);
                }
            }
            throw t3;
        }
    }
    
    default <T> T parseObject(final String text, final Type type, final String format, final Filter[] filters, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider(), null, filters, features);
        context.setDateFormat(format);
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.provider.getObjectReader(type, fieldBased);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            if (reader.nextIfNull()) {
                final T t = null;
                if (reader != null) {
                    reader.close();
                }
                return t;
            }
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t2 = object;
            if (reader != null) {
                reader.close();
            }
            return t2;
        }
        catch (Throwable t3) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t3.addSuppressed(exception);
                }
            }
            throw t3;
        }
    }
    
    default <T> T parseObject(final String text, final Type type) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider);
        final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(type, (JSONFactory.defaultReaderFeatures & JSONReader.Feature.FieldBased.mask) != 0x0L);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T extends Map<String, Object>> T parseObject(final String text, final MapMultiValueType<T> type) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final Map<String, Object> map = object;
            if (reader != null) {
                reader.close();
            }
            return (T)map;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default <T> T parseObject(final String text, final Type... types) {
        return parseObject(text, new MultiType(types));
    }
    
    default <T> T parseObject(final String text, final TypeReference<T> typeReference, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final Type type = typeReference.getType();
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.provider.getObjectReader(type, fieldBased);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final String text, final TypeReference<T> typeReference, final Filter filter, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(filter, features);
        final Type type = typeReference.getType();
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.provider.getObjectReader(type, fieldBased);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final String text, final Class<T> clazz, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.provider.getObjectReader(clazz, fieldBased);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final T object = objectReader.readObject(reader, clazz, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final String text, final int offset, final int length, final Class<T> clazz, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty() || length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.provider.getObjectReader(clazz, fieldBased);
        final JSONReader reader = JSONReader.of(text, offset, length, context);
        try {
            final T object = objectReader.readObject(reader, clazz, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final String text, final Class<T> clazz, final JSONReader.Context context) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.provider.getObjectReader(clazz, fieldBased);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final T object = objectReader.readObject(reader, clazz, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final String text, final Class<T> clazz, final String format, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        if (format != null && !format.isEmpty()) {
            context.setDateFormat(format);
        }
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.provider.getObjectReader(clazz, fieldBased);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final T object = objectReader.readObject(reader, clazz, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final String text, final Type type, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final String text, final Type type, final Filter filter, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(filter, features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final String text, final Type type, final String format, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        if (format != null && !format.isEmpty()) {
            context.setDateFormat(format);
        }
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final char[] chars, final int offset, final int length, final Type type, final JSONReader.Feature... features) {
        if (chars == null || chars.length == 0 || length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(chars, offset, length, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final char[] chars, final Class<T> clazz) {
        if (chars == null || chars.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(clazz);
        final JSONReader reader = JSONReader.of(chars, context);
        try {
            final T object = objectReader.readObject(reader, clazz, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final int offset, final int length, final Type type, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0 || length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(bytes, offset, length, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final Type type) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider);
        final ObjectReader<T> objectReader = (ObjectReader<T>)provider.getObjectReader(clazz, (JSONFactory.defaultReaderFeatures & JSONReader.Feature.FieldBased.mask) != 0x0L);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            final T object = objectReader.readObject(reader, clazz, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final Class<T> clazz, final Filter filter, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(filter, features);
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.provider.getObjectReader(clazz, fieldBased);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            final T object = objectReader.readObject(reader, clazz, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final Class<T> clazz, final JSONReader.Context context) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.provider.getObjectReader(clazz, fieldBased);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            final T object = objectReader.readObject(reader, clazz, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final Type type, final String format, final Filter[] filters, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final JSONReader.Context context = new JSONReader.Context(JSONFactory.getDefaultObjectReaderProvider(), null, filters, features);
        context.setDateFormat(format);
        final boolean fieldBased = (context.features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.provider.getObjectReader(type, fieldBased);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final Class<T> clazz, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(clazz);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            final T object = objectReader.readObject(reader, clazz, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final Type type, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final char[] chars, final Class<T> objectClass, final JSONReader.Feature... features) {
        if (chars == null || chars.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(objectClass);
        final JSONReader reader = JSONReader.of(chars, context);
        try {
            final T object = objectReader.readObject(reader, objectClass, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final char[] chars, final Type type, final JSONReader.Feature... features) {
        if (chars == null || chars.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(chars, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final Type type, final Filter filter, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(filter, features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final Type type, final String format, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        if (format != null && !format.isEmpty()) {
            context.setDateFormat(format);
        }
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final ByteBuffer buffer, final Class<T> objectClass) {
        if (buffer == null) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(objectClass);
        final JSONReader reader = JSONReader.of(buffer, null, context);
        try {
            final T object = objectReader.readObject(reader, objectClass, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final Reader input, final Type type, final JSONReader.Feature... features) {
        if (input == null) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(input, context);
        try {
            if (reader.isEnd()) {
                final T t = null;
                if (reader != null) {
                    reader.close();
                }
                return t;
            }
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t2 = object;
            if (reader != null) {
                reader.close();
            }
            return t2;
        }
        catch (Throwable t3) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t3.addSuppressed(exception);
                }
            }
            throw t3;
        }
    }
    
    default <T> T parseObject(final InputStream input, final Type type, final JSONReader.Feature... features) {
        if (input == null) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        context.config(features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(input, StandardCharsets.UTF_8, context);
        try {
            if (reader.isEnd()) {
                final T t = null;
                if (reader != null) {
                    reader.close();
                }
                return t;
            }
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t2 = object;
            if (reader != null) {
                reader.close();
            }
            return t2;
        }
        catch (Throwable t3) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t3.addSuppressed(exception);
                }
            }
            throw t3;
        }
    }
    
    default <T> T parseObject(final URL url, final Type type, final JSONReader.Feature... features) {
        if (url == null) {
            return null;
        }
        try {
            final InputStream is = url.openStream();
            try {
                final T object = parseObject(is, type, features);
                if (is != null) {
                    is.close();
                }
                return object;
            }
            catch (Throwable t) {
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        catch (IOException e) {
            throw new JSONException("parseObject error", e);
        }
    }
    
    default <T> T parseObject(final URL url, final Class<T> objectClass, final JSONReader.Feature... features) {
        if (url == null) {
            return null;
        }
        try {
            final InputStream is = url.openStream();
            try {
                final T object = parseObject(is, objectClass, features);
                if (is != null) {
                    is.close();
                }
                return object;
            }
            catch (Throwable t) {
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        catch (IOException e) {
            throw new JSONException("JSON#parseObject cannot parse '" + url + "' to '" + objectClass + "'", e);
        }
    }
    
    default <T> T parseObject(final URL url, final Function<JSONObject, T> function, final JSONReader.Feature... features) {
        if (url == null) {
            return null;
        }
        try {
            final InputStream is = url.openStream();
            try {
                final JSONObject object = parseObject(is, features);
                if (object == null) {
                    final T t = null;
                    if (is != null) {
                        is.close();
                    }
                    return t;
                }
                final T apply = function.apply(object);
                if (is != null) {
                    is.close();
                }
                return apply;
            }
            catch (Throwable t2) {
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (Throwable exception) {
                        t2.addSuppressed(exception);
                    }
                }
                throw t2;
            }
        }
        catch (IOException e) {
            throw new JSONException("JSON#parseObject cannot parse '" + url + "'", e);
        }
    }
    
    default <T> T parseObject(final InputStream input, final Type type, final String format, final JSONReader.Feature... features) {
        if (input == null) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        if (format != null && !format.isEmpty()) {
            context.setDateFormat(format);
        }
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(input, StandardCharsets.UTF_8, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final InputStream input, final Charset charset, final Type type, final JSONReader.Feature... features) {
        if (input == null) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(input, charset, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final int offset, final int length, final Charset charset, final Type type) {
        if (bytes == null || bytes.length == 0 || length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(bytes, offset, length, charset, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final int offset, final int length, final Charset charset, final Class<T> type) {
        if (bytes == null || bytes.length == 0 || length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(bytes, offset, length, charset, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> T parseObject(final byte[] bytes, final int offset, final int length, final Charset charset, final Class<T> type, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0 || length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final ObjectReader<T> objectReader = (ObjectReader<T>)context.getObjectReader(type);
        final JSONReader reader = JSONReader.of(bytes, offset, length, charset, context);
        try {
            final T object = objectReader.readObject(reader, type, null, 0L);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(object);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final T t = object;
            if (reader != null) {
                reader.close();
            }
            return t;
        }
        catch (Throwable t2) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t2.addSuppressed(exception);
                }
            }
            throw t2;
        }
    }
    
    default <T> void parseObject(final InputStream input, final Type type, final Consumer<T> consumer, final JSONReader.Feature... features) {
        parseObject(input, StandardCharsets.UTF_8, '\n', type, consumer, features);
    }
    
    default <T> void parseObject(final InputStream input, final Charset charset, final char delimiter, final Type type, final Consumer<T> consumer, final JSONReader.Feature... features) {
        final int cacheIndex = System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1;
        final JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[cacheIndex];
        byte[] bytes = JSONFactory.BYTES_UPDATER.getAndSet(cacheItem, null);
        final int bufferSize = 524288;
        if (bytes == null) {
            bytes = new byte[bufferSize];
        }
        int offset = 0;
        int start = 0;
        ObjectReader<? extends T> objectReader = null;
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        try {
            while (true) {
                final int n = input.read(bytes, offset, bytes.length - offset);
                if (n == -1) {
                    break;
                }
                int k = offset;
                offset += n;
                boolean dispose = false;
                while (k < offset) {
                    if (bytes[k] == delimiter) {
                        final int end = k;
                        final JSONReader jsonReader = JSONReader.of(bytes, start, end - start, charset, context);
                        if (objectReader == null) {
                            objectReader = (ObjectReader<? extends T>)context.getObjectReader(type);
                        }
                        final T object = (T)objectReader.readObject(jsonReader, type, null, 0L);
                        if (jsonReader.resolveTasks != null) {
                            jsonReader.handleResolveTasks(object);
                        }
                        if (jsonReader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                            throw new JSONException(jsonReader.info("input not end"));
                        }
                        consumer.accept(object);
                        start = end + 1;
                        dispose = true;
                    }
                    ++k;
                }
                if (offset != bytes.length) {
                    continue;
                }
                if (dispose) {
                    final int len = bytes.length - start;
                    System.arraycopy(bytes, start, bytes, 0, len);
                    start = 0;
                    offset = len;
                }
                else {
                    bytes = Arrays.copyOf(bytes, bytes.length + bufferSize);
                }
            }
        }
        catch (IOException e) {
            throw new JSONException("JSON#parseObject cannot parse the 'InputStream' to '" + type + "'", e);
        }
        finally {
            JSONFactory.BYTES_UPDATER.lazySet(cacheItem, bytes);
        }
    }
    
    default <T> void parseObject(final Reader input, final char delimiter, final Type type, final Consumer<T> consumer) {
        final int cacheIndex = System.identityHashCode(Thread.currentThread()) & JSONFactory.CACHE_ITEMS.length - 1;
        final JSONFactory.CacheItem cacheItem = JSONFactory.CACHE_ITEMS[cacheIndex];
        char[] chars = JSONFactory.CHARS_UPDATER.getAndSet(cacheItem, null);
        if (chars == null) {
            chars = new char[8192];
        }
        int offset = 0;
        int start = 0;
        ObjectReader<? extends T> objectReader = null;
        final JSONReader.Context context = JSONFactory.createReadContext();
        try {
            while (true) {
                final int n = input.read(chars, offset, chars.length - offset);
                if (n == -1) {
                    break;
                }
                int k = offset;
                offset += n;
                boolean dispose = false;
                while (k < offset) {
                    if (chars[k] == delimiter) {
                        final int end = k;
                        final JSONReader jsonReader = JSONReader.of(chars, start, end - start, context);
                        if (objectReader == null) {
                            objectReader = (ObjectReader<? extends T>)context.getObjectReader(type);
                        }
                        consumer.accept((T)objectReader.readObject(jsonReader, type, null, 0L));
                        start = end + 1;
                        dispose = true;
                    }
                    ++k;
                }
                if (offset != chars.length) {
                    continue;
                }
                if (dispose) {
                    final int len = chars.length - start;
                    System.arraycopy(chars, start, chars, 0, len);
                    start = 0;
                    offset = len;
                }
                else {
                    chars = Arrays.copyOf(chars, chars.length + 8192);
                }
            }
        }
        catch (IOException e) {
            throw new JSONException("JSON#parseObject cannot parse the 'Reader' to '" + type + "'", e);
        }
        finally {
            JSONFactory.CHARS_UPDATER.lazySet(cacheItem, chars);
        }
    }
    
    default JSONArray parseArray(final String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final JSONReader reader = JSONReader.of(text, context);
        try {
            if (reader.nextIfNull()) {
                final JSONArray jsonArray = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonArray;
            }
            final JSONArray array = new JSONArray();
            reader.read(array);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(array);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONArray jsonArray2 = array;
            if (reader != null) {
                reader.close();
            }
            return jsonArray2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONArray parseArray(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            if (reader.nextIfNull()) {
                final JSONArray jsonArray = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonArray;
            }
            final JSONArray array = new JSONArray();
            reader.read(array);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(array);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONArray jsonArray2 = array;
            if (reader != null) {
                reader.close();
            }
            return jsonArray2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONArray parseArray(final byte[] bytes, final int offset, final int length, final Charset charset) {
        if (bytes == null || bytes.length == 0 || length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final JSONReader reader = JSONReader.of(bytes, offset, length, charset, context);
        try {
            if (reader.nextIfNull()) {
                final JSONArray jsonArray = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonArray;
            }
            final JSONArray array = new JSONArray();
            reader.read(array);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(array);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONArray jsonArray2 = array;
            if (reader != null) {
                reader.close();
            }
            return jsonArray2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONArray parseArray(final char[] chars) {
        if (chars == null || chars.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final JSONReader reader = JSONReader.of(chars, context);
        try {
            if (reader.nextIfNull()) {
                final JSONArray jsonArray = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonArray;
            }
            final JSONArray array = new JSONArray();
            reader.read(array);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(array);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONArray jsonArray2 = array;
            if (reader != null) {
                reader.close();
            }
            return jsonArray2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONArray parseArray(final String text, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            if (reader.nextIfNull()) {
                final JSONArray jsonArray = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonArray;
            }
            final JSONArray array = new JSONArray();
            reader.read(array);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(array);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONArray jsonArray2 = array;
            if (reader != null) {
                reader.close();
            }
            return jsonArray2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default JSONArray parseArray(final URL url, final JSONReader.Feature... features) {
        if (url == null) {
            return null;
        }
        try {
            final InputStream is = url.openStream();
            try {
                final JSONArray array = parseArray(is, features);
                if (is != null) {
                    is.close();
                }
                return array;
            }
            catch (Throwable t) {
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        catch (IOException e) {
            throw new JSONException("JSON#parseArray cannot parse '" + url + "' to '" + JSONArray.class + "'", e);
        }
    }
    
    default JSONArray parseArray(final InputStream in, final JSONReader.Feature... features) {
        if (in == null) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(in, StandardCharsets.UTF_8, context);
        try {
            if (reader.nextIfNull()) {
                final JSONArray jsonArray = null;
                if (reader != null) {
                    reader.close();
                }
                return jsonArray;
            }
            final JSONArray array = new JSONArray();
            reader.read(array);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(array);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final JSONArray jsonArray2 = array;
            if (reader != null) {
                reader.close();
            }
            return jsonArray2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final String text, final Type type, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final List<T> list = (List<T>)reader.readArray(type);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final List<T> list2 = list;
            if (reader != null) {
                reader.close();
            }
            return list2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final String text, final Type type) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final List<T> list = (List<T>)reader.readArray(type);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final List<T> list2 = list;
            if (reader != null) {
                reader.close();
            }
            return list2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final String text, final Class<T> type) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final List<T> list = (List<T>)reader.readArray(type);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final List<T> list2 = list;
            if (reader != null) {
                reader.close();
            }
            return list2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final String text, final Type... types) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext();
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final List<T> list = (List<T>)reader.readList(types);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final List<T> list2 = list;
            if (reader != null) {
                reader.close();
            }
            return list2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final String text, final Class<T> type, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            final List<T> list = (List<T>)reader.readArray(type);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final List<T> list2 = list;
            if (reader != null) {
                reader.close();
            }
            return list2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final char[] chars, final Class<T> type, final JSONReader.Feature... features) {
        if (chars == null || chars.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(chars, context);
        try {
            final List<T> list = (List<T>)reader.readArray(type);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final List<T> list2 = list;
            if (reader != null) {
                reader.close();
            }
            return list2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final String text, final Type[] types, final JSONReader.Feature... features) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(text, context);
        try {
            if (reader.nextIfNull()) {
                final List<T> list = null;
                if (reader != null) {
                    reader.close();
                }
                return list;
            }
            reader.startArray();
            final List<T> array = new ArrayList<T>(types.length);
            for (int i = 0; i < types.length; ++i) {
                array.add(reader.read(types[i]));
            }
            reader.endArray();
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(array);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final List<T> list2 = array;
            if (reader != null) {
                reader.close();
            }
            return list2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final byte[] bytes, final Type type, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            final List<T> list = (List<T>)reader.readArray(type);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final List<T> list2 = list;
            if (reader != null) {
                reader.close();
            }
            return list2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final byte[] bytes, final Class<T> type, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(bytes, context);
        try {
            final List<T> list = (List<T>)reader.readArray(type);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final List<T> list2 = list;
            if (reader != null) {
                reader.close();
            }
            return list2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default <T> List<T> parseArray(final byte[] bytes, final int offset, final int length, final Charset charset, final Class<T> type, final JSONReader.Feature... features) {
        if (bytes == null || bytes.length == 0 || length == 0) {
            return null;
        }
        final JSONReader.Context context = JSONFactory.createReadContext(features);
        final JSONReader reader = JSONReader.of(bytes, offset, length, charset, context);
        try {
            final List<T> list = (List<T>)reader.readArray(type);
            if (reader.resolveTasks != null) {
                reader.handleResolveTasks(list);
            }
            if (reader.ch != '\u001a' && (context.features & JSONReader.Feature.IgnoreCheckClose.mask) == 0x0L) {
                throw new JSONException(reader.info("input not end"));
            }
            final List<T> list2 = list;
            if (reader != null) {
                reader.close();
            }
            return list2;
        }
        catch (Throwable t) {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
    }
    
    default String toJSONString(final Object object) {
        final ObjectWriterProvider provider = JSONFactory.defaultObjectWriterProvider;
        final JSONWriter.Context context = new JSONWriter.Context(provider);
        try {
            final JSONWriter writer = JSONWriter.of(context);
            try {
                if (object == null) {
                    writer.writeNull();
                }
                else {
                    writer.rootObject = object;
                    writer.path = JSONWriter.Path.ROOT;
                    final Class<?> valueClass = object.getClass();
                    if (valueClass == JSONObject.class && context.features == 0L) {
                        writer.write((JSONObject)object);
                    }
                    else {
                        final ObjectWriter<?> objectWriter = (ObjectWriter<?>)provider.getObjectWriter(valueClass, valueClass, (JSONFactory.defaultWriterFeatures & JSONWriter.Feature.FieldBased.mask) != 0x0L);
                        objectWriter.write(writer, object, null, null, 0L);
                    }
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
        catch (NullPointerException | NumberFormatException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            throw new JSONException("JSON#toJSONString cannot serialize '" + object + "'", e);
        }
    }
    
    default String toJSONString(final Object object, JSONWriter.Context context) {
        if (context == null) {
            context = JSONFactory.createWriteContext();
        }
        try {
            final JSONWriter writer = JSONWriter.of(context);
            try {
                if (object == null) {
                    writer.writeNull();
                }
                else {
                    writer.rootObject = object;
                    writer.path = JSONWriter.Path.ROOT;
                    final Class<?> valueClass = object.getClass();
                    final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                    objectWriter.write(writer, object, null, null, 0L);
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
        catch (NullPointerException | NumberFormatException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            throw new JSONException("JSON#toJSONString cannot serialize '" + object + "'", e);
        }
    }
    
    default String toJSONString(final Object object, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        final JSONWriter writer = JSONWriter.of(context);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                final Class<?> valueClass = object.getClass();
                final boolean fieldBased = (context.features & JSONWriter.Feature.FieldBased.mask) != 0x0L;
                final ObjectWriter<?> objectWriter = (ObjectWriter<?>)context.provider.getObjectWriter(valueClass, valueClass, fieldBased);
                objectWriter.write(writer, object, null, null, 0L);
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
    
    default String toJSONString(final Object object, final Filter filter, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        final JSONWriter writer = JSONWriter.of(context);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                if (filter != null) {
                    writer.context.configFilter(filter);
                }
                final Class<?> valueClass = object.getClass();
                final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                objectWriter.write(writer, object, null, null, 0L);
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
    
    default String toJSONString(final Object object, final Filter[] filters, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        if (filters != null && filters.length != 0) {
            context.configFilter(filters);
        }
        final JSONWriter writer = JSONWriter.of(context);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                final Class<?> valueClass = object.getClass();
                final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                objectWriter.write(writer, object, null, null, 0L);
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
    
    default String toJSONString(final Object object, final String format, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        if (format != null && !format.isEmpty()) {
            context.setDateFormat(format);
        }
        final JSONWriter writer = JSONWriter.of(context);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                final Class<?> valueClass = object.getClass();
                final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                objectWriter.write(writer, object, null, null, 0L);
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
    
    default String toJSONString(final Object object, final String format, final Filter[] filters, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        if (format != null && !format.isEmpty()) {
            context.setDateFormat(format);
        }
        if (filters != null && filters.length != 0) {
            context.configFilter(filters);
        }
        final JSONWriter writer = JSONWriter.of(context);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                final Class<?> valueClass = object.getClass();
                final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                objectWriter.write(writer, object, null, null, 0L);
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
    
    default byte[] toJSONBytes(final Object object) {
        final ObjectWriterProvider provider = JSONFactory.defaultObjectWriterProvider;
        final JSONWriter.Context context = new JSONWriter.Context(provider);
        final JSONWriter writer = JSONWriter.ofUTF8(context);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                final Class<?> valueClass = object.getClass();
                if (valueClass == JSONObject.class && writer.context.features == 0L) {
                    writer.write((JSONObject)object);
                }
                else {
                    final ObjectWriter<?> objectWriter = (ObjectWriter<?>)provider.getObjectWriter(valueClass, valueClass, (JSONFactory.defaultWriterFeatures & JSONWriter.Feature.FieldBased.mask) != 0x0L);
                    objectWriter.write(writer, object, null, null, 0L);
                }
            }
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
    
    default byte[] toJSONBytes(final Object object, final String format, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        if (format != null && !format.isEmpty()) {
            context.setDateFormat(format);
        }
        final JSONWriter writer = JSONWriter.ofUTF8(context);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                final Class<?> valueClass = object.getClass();
                final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                objectWriter.write(writer, object, null, null, 0L);
            }
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
    
    default byte[] toJSONBytes(final Object object, final Filter... filters) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider);
        if (filters != null && filters.length != 0) {
            context.configFilter(filters);
        }
        final JSONWriter writer = JSONWriter.ofUTF8(context);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                final Class<?> valueClass = object.getClass();
                final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                objectWriter.write(writer, object, null, null, 0L);
            }
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
    
    default byte[] toJSONBytes(final Object object, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        final JSONWriter writer = JSONWriter.ofUTF8(context);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                final Class<?> valueClass = object.getClass();
                final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                objectWriter.write(writer, object, null, null, 0L);
            }
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
    
    default byte[] toJSONBytes(final Object object, final Filter[] filters, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        if (filters != null && filters.length != 0) {
            context.configFilter(filters);
        }
        final JSONWriter writer = JSONWriter.ofUTF8(context);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                final Class<?> valueClass = object.getClass();
                final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                objectWriter.write(writer, object, null, null, 0L);
            }
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
    
    default byte[] toJSONBytes(final Object object, final String format, final Filter[] filters, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        if (format != null && !format.isEmpty()) {
            context.setDateFormat(format);
        }
        if (filters != null && filters.length != 0) {
            context.configFilter(filters);
        }
        final JSONWriter writer = JSONWriter.ofUTF8(context);
        try {
            if (object == null) {
                writer.writeNull();
            }
            else {
                writer.rootObject = object;
                writer.path = JSONWriter.Path.ROOT;
                final Class<?> valueClass = object.getClass();
                final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                objectWriter.write(writer, object, null, null, 0L);
            }
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
    
    default int writeTo(final OutputStream out, final Object object) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider);
        try {
            final JSONWriter writer = JSONWriter.ofUTF8(context);
            try {
                if (object == null) {
                    writer.writeNull();
                }
                else {
                    writer.rootObject = object;
                    writer.path = JSONWriter.Path.ROOT;
                    final Class<?> valueClass = object.getClass();
                    final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                    objectWriter.write(writer, object, null, null, 0L);
                }
                final int flushTo = writer.flushTo(out);
                if (writer != null) {
                    writer.close();
                }
                return flushTo;
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
        catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }
    
    default int writeTo(final OutputStream out, final Object object, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        try {
            final JSONWriter writer = JSONWriter.ofUTF8(context);
            try {
                if (object == null) {
                    writer.writeNull();
                }
                else {
                    writer.rootObject = object;
                    writer.path = JSONWriter.Path.ROOT;
                    final Class<?> valueClass = object.getClass();
                    final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                    objectWriter.write(writer, object, null, null, 0L);
                }
                final int flushTo = writer.flushTo(out);
                if (writer != null) {
                    writer.close();
                }
                return flushTo;
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
        catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }
    
    default int writeTo(final OutputStream out, final Object object, final Filter[] filters, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        if (filters != null && filters.length != 0) {
            context.configFilter(filters);
        }
        try {
            final JSONWriter writer = JSONWriter.ofUTF8(context);
            try {
                if (object == null) {
                    writer.writeNull();
                }
                else {
                    writer.rootObject = object;
                    writer.path = JSONWriter.Path.ROOT;
                    final Class<?> valueClass = object.getClass();
                    final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                    objectWriter.write(writer, object, null, null, 0L);
                }
                final int flushTo = writer.flushTo(out);
                if (writer != null) {
                    writer.close();
                }
                return flushTo;
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
        catch (Exception e) {
            throw new JSONException("JSON#writeTo cannot serialize '" + object + "' to 'OutputStream'", e);
        }
    }
    
    default int writeTo(final OutputStream out, final Object object, final String format, final Filter[] filters, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
        if (format != null && !format.isEmpty()) {
            context.setDateFormat(format);
        }
        if (filters != null && filters.length != 0) {
            context.configFilter(filters);
        }
        try {
            final JSONWriter writer = JSONWriter.ofUTF8(context);
            try {
                if (object == null) {
                    writer.writeNull();
                }
                else {
                    writer.rootObject = object;
                    writer.path = JSONWriter.Path.ROOT;
                    final Class<?> valueClass = object.getClass();
                    final ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                    objectWriter.write(writer, object, null, null, 0L);
                }
                final int flushTo = writer.flushTo(out);
                if (writer != null) {
                    writer.close();
                }
                return flushTo;
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
        catch (Exception e) {
            throw new JSONException("JSON#writeTo cannot serialize '" + object + "' to 'OutputStream'", e);
        }
    }
    
    default boolean isValid(final String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        try {
            final JSONReader jsonReader = JSONReader.of(text);
            try {
                jsonReader.skipValue();
                final boolean b = jsonReader.isEnd() && !jsonReader.comma;
                if (jsonReader != null) {
                    jsonReader.close();
                }
                return b;
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
        catch (JSONException error) {
            return false;
        }
    }
    
    default boolean isValid(final char[] chars) {
        if (chars == null || chars.length == 0) {
            return false;
        }
        try {
            final JSONReader jsonReader = JSONReader.of(chars);
            try {
                jsonReader.skipValue();
                final boolean end = jsonReader.isEnd();
                if (jsonReader != null) {
                    jsonReader.close();
                }
                return end;
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
        catch (JSONException error) {
            return false;
        }
    }
    
    default boolean isValidObject(final String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        try {
            final JSONReader jsonReader = JSONReader.of(text);
            try {
                if (!jsonReader.isObject()) {
                    final boolean b = false;
                    if (jsonReader != null) {
                        jsonReader.close();
                    }
                    return b;
                }
                jsonReader.skipValue();
                final boolean end = jsonReader.isEnd();
                if (jsonReader != null) {
                    jsonReader.close();
                }
                return end;
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
        catch (JSONException error) {
            return false;
        }
    }
    
    default boolean isValidObject(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return false;
        }
        try {
            final JSONReader jsonReader = JSONReader.of(bytes);
            try {
                if (!jsonReader.isObject()) {
                    final boolean b = false;
                    if (jsonReader != null) {
                        jsonReader.close();
                    }
                    return b;
                }
                jsonReader.skipValue();
                final boolean end = jsonReader.isEnd();
                if (jsonReader != null) {
                    jsonReader.close();
                }
                return end;
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
        catch (JSONException error) {
            return false;
        }
    }
    
    default boolean isValidArray(final String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        try {
            final JSONReader jsonReader = JSONReader.of(text);
            try {
                if (!jsonReader.isArray()) {
                    final boolean b = false;
                    if (jsonReader != null) {
                        jsonReader.close();
                    }
                    return b;
                }
                jsonReader.skipValue();
                final boolean end = jsonReader.isEnd();
                if (jsonReader != null) {
                    jsonReader.close();
                }
                return end;
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
        catch (JSONException error) {
            return false;
        }
    }
    
    default boolean isValid(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return false;
        }
        try {
            final JSONReader jsonReader = JSONReader.of(bytes);
            try {
                jsonReader.skipValue();
                final boolean end = jsonReader.isEnd();
                if (jsonReader != null) {
                    jsonReader.close();
                }
                return end;
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
        catch (JSONException error) {
            return false;
        }
    }
    
    default boolean isValidArray(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return false;
        }
        try {
            final JSONReader jsonReader = JSONReader.of(bytes);
            try {
                if (!jsonReader.isArray()) {
                    final boolean b = false;
                    if (jsonReader != null) {
                        jsonReader.close();
                    }
                    return b;
                }
                jsonReader.skipValue();
                final boolean end = jsonReader.isEnd();
                if (jsonReader != null) {
                    jsonReader.close();
                }
                return end;
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
        catch (JSONException error) {
            return false;
        }
    }
    
    default boolean isValid(final byte[] bytes, final int offset, final int length, final Charset charset) {
        if (bytes == null || bytes.length == 0 || length == 0) {
            return false;
        }
        try {
            final JSONReader jsonReader = JSONReader.of(bytes, offset, length, charset);
            try {
                jsonReader.skipValue();
                final boolean end = jsonReader.isEnd();
                if (jsonReader != null) {
                    jsonReader.close();
                }
                return end;
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
        catch (JSONException error) {
            return false;
        }
    }
    
    default Object toJSON(final Object object) {
        return toJSON(object, (JSONWriter.Feature[])null);
    }
    
    default Object toJSON(final Object object, final JSONWriter.Feature... features) {
        if (object == null) {
            return null;
        }
        if (object instanceof JSONObject || object instanceof JSONArray) {
            return object;
        }
        final JSONWriter.Context writeContext = (features == null) ? JSONFactory.createWriteContext() : JSONFactory.createWriteContext(features);
        final Class<?> valueClass = object.getClass();
        final ObjectWriter<?> objectWriter = writeContext.getObjectWriter(valueClass, valueClass);
        if (objectWriter instanceof ObjectWriterAdapter && !writeContext.isEnabled(JSONWriter.Feature.ReferenceDetection)) {
            final ObjectWriterAdapter objectWriterAdapter = (ObjectWriterAdapter)objectWriter;
            return objectWriterAdapter.toJSONObject(object);
        }
        String str;
        try {
            final JSONWriter writer = JSONWriter.of(writeContext);
            try {
                objectWriter.write(writer, object, null, null, 0L);
                str = writer.toString();
                if (writer != null) {
                    writer.close();
                }
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
        catch (NullPointerException | NumberFormatException ex3) {
            final RuntimeException ex2;
            final RuntimeException ex = ex2;
            throw new JSONException("toJSONString error", ex);
        }
        return parse(str);
    }
    
    default <T> T to(final Class<T> clazz, final Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof JSONObject) {
            return ((JSONObject)object).to(clazz, new JSONReader.Feature[0]);
        }
        return TypeUtils.cast(object, clazz, JSONFactory.getDefaultObjectReaderProvider());
    }
    
    @Deprecated
    default <T> T toJavaObject(final Object object, final Class<T> clazz) {
        return to(clazz, object);
    }
    
    default void mixIn(final Class<?> target, final Class<?> mixinSource) {
        JSONFactory.defaultObjectWriterProvider.mixIn(target, mixinSource);
        JSONFactory.getDefaultObjectReaderProvider().mixIn(target, mixinSource);
    }
    
    default ObjectReader<?> register(final Type type, final ObjectReader<?> objectReader) {
        return (ObjectReader<?>)JSONFactory.getDefaultObjectReaderProvider().register(type, objectReader);
    }
    
    default ObjectReader<?> register(final Type type, final ObjectReader<?> objectReader, final boolean fieldBased) {
        return (ObjectReader<?>)JSONFactory.getDefaultObjectReaderProvider().register(type, objectReader, fieldBased);
    }
    
    default ObjectReader<?> registerIfAbsent(final Type type, final ObjectReader<?> objectReader) {
        return (ObjectReader<?>)JSONFactory.getDefaultObjectReaderProvider().registerIfAbsent(type, objectReader);
    }
    
    default ObjectReader<?> registerIfAbsent(final Type type, final ObjectReader<?> objectReader, final boolean fieldBased) {
        return (ObjectReader<?>)JSONFactory.getDefaultObjectReaderProvider().registerIfAbsent(type, objectReader, fieldBased);
    }
    
    default boolean register(final ObjectReaderModule objectReaderModule) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        return provider.register(objectReaderModule);
    }
    
    default void registerSeeAlsoSubType(final Class subTypeClass) {
        registerSeeAlsoSubType(subTypeClass, null);
    }
    
    default void registerSeeAlsoSubType(final Class subTypeClass, final String subTypeClassName) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        provider.registerSeeAlsoSubType(subTypeClass, subTypeClassName);
    }
    
    default boolean register(final ObjectWriterModule objectWriterModule) {
        return JSONFactory.getDefaultObjectWriterProvider().register(objectWriterModule);
    }
    
    default ObjectWriter<?> register(final Type type, final ObjectWriter<?> objectWriter) {
        return (ObjectWriter<?>)JSONFactory.getDefaultObjectWriterProvider().register(type, objectWriter);
    }
    
    default ObjectWriter<?> register(final Type type, final ObjectWriter<?> objectWriter, final boolean fieldBased) {
        return (ObjectWriter<?>)JSONFactory.getDefaultObjectWriterProvider().register(type, objectWriter, fieldBased);
    }
    
    default ObjectWriter<?> registerIfAbsent(final Type type, final ObjectWriter<?> objectWriter) {
        return (ObjectWriter<?>)JSONFactory.getDefaultObjectWriterProvider().registerIfAbsent(type, objectWriter);
    }
    
    default ObjectWriter<?> registerIfAbsent(final Type type, final ObjectWriter<?> objectWriter, final boolean fieldBased) {
        return (ObjectWriter<?>)JSONFactory.getDefaultObjectWriterProvider().registerIfAbsent(type, objectWriter, fieldBased);
    }
    
    default void register(final Class type, final Filter filter) {
        final boolean writerFilter = filter instanceof AfterFilter || filter instanceof BeforeFilter || filter instanceof ContextNameFilter || filter instanceof ContextValueFilter || filter instanceof LabelFilter || filter instanceof NameFilter || filter instanceof PropertyFilter || filter instanceof PropertyPreFilter || filter instanceof ValueFilter;
        if (writerFilter) {
            final ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter(type);
            objectWriter.setFilter(filter);
        }
    }
    
    default void config(final JSONReader.Feature... features) {
        for (int i = 0; i < features.length; ++i) {
            final JSONReader.Feature feature = features[i];
            if (feature == JSONReader.Feature.SupportAutoType) {
                throw new JSONException("not support config global autotype support");
            }
            JSONFactory.defaultReaderFeatures |= feature.mask;
        }
    }
    
    default void config(final JSONReader.Feature feature, final boolean state) {
        if (feature == JSONReader.Feature.SupportAutoType && state) {
            throw new JSONException("not support config global autotype support");
        }
        if (state) {
            JSONFactory.defaultReaderFeatures |= feature.mask;
        }
        else {
            JSONFactory.defaultReaderFeatures &= ~feature.mask;
        }
    }
    
    default boolean isEnabled(final JSONReader.Feature feature) {
        return (JSONFactory.defaultReaderFeatures & feature.mask) != 0x0L;
    }
    
    default void configReaderDateFormat(final String dateFormat) {
        JSONFactory.defaultReaderFormat = dateFormat;
    }
    
    default void configWriterDateFormat(final String dateFormat) {
        JSONFactory.defaultWriterFormat = dateFormat;
    }
    
    default void configReaderZoneId(final ZoneId zoneId) {
        JSONFactory.defaultReaderZoneId = zoneId;
    }
    
    default void configWriterZoneId(final ZoneId zoneId) {
        JSONFactory.defaultWriterZoneId = zoneId;
    }
    
    default void config(final JSONWriter.Feature... features) {
        for (int i = 0; i < features.length; ++i) {
            JSONFactory.defaultWriterFeatures |= features[i].mask;
        }
    }
    
    default void config(final JSONWriter.Feature feature, final boolean state) {
        if (state) {
            JSONFactory.defaultWriterFeatures |= feature.mask;
        }
        else {
            JSONFactory.defaultWriterFeatures &= ~feature.mask;
        }
    }
    
    default boolean isEnabled(final JSONWriter.Feature feature) {
        return (JSONFactory.defaultWriterFeatures & feature.mask) != 0x0L;
    }
    
    default <T> T copy(final T object, final JSONWriter.Feature... features) {
        if (object == null) {
            return null;
        }
        final Class<?> objectClass = object.getClass();
        if (ObjectWriterProvider.isPrimitiveOrEnum(objectClass)) {
            return object;
        }
        boolean fieldBased = false;
        boolean beanToArray = false;
        long featuresValue = 0L;
        for (int i = 0; i < features.length; ++i) {
            final JSONWriter.Feature feature = features[i];
            featuresValue |= feature.mask;
            if (feature == JSONWriter.Feature.FieldBased) {
                fieldBased = true;
            }
            else if (feature == JSONWriter.Feature.BeanToArray) {
                beanToArray = true;
            }
        }
        final ObjectWriter objectWriter = JSONFactory.defaultObjectWriterProvider.getObjectWriter(objectClass, objectClass, fieldBased);
        final ObjectReader objectReader = JSONFactory.defaultObjectReaderProvider.getObjectReader(objectClass, fieldBased);
        if (objectWriter instanceof ObjectWriterAdapter && objectReader instanceof ObjectReaderBean) {
            final List<FieldWriter> fieldWriters = (List<FieldWriter>)objectWriter.getFieldWriters();
            if (objectReader instanceof ObjectReaderNoneDefaultConstructor) {
                final Map<String, Object> map = new HashMap<String, Object>(fieldWriters.size());
                for (int j = 0; j < fieldWriters.size(); ++j) {
                    final FieldWriter fieldWriter = fieldWriters.get(j);
                    final Object fieldValue = fieldWriter.getFieldValue(object);
                    map.put(fieldWriter.fieldName, fieldValue);
                }
                return objectReader.createInstance(map, featuresValue);
            }
            final T instance = objectReader.createInstance(featuresValue);
            for (int j = 0; j < fieldWriters.size(); ++j) {
                final FieldWriter fieldWriter = fieldWriters.get(j);
                final FieldReader fieldReader = objectReader.getFieldReader(fieldWriter.fieldName);
                if (fieldReader != null) {
                    final Object fieldValue2 = fieldWriter.getFieldValue(object);
                    final Object fieldValueCopied = copy(fieldValue2, new JSONWriter.Feature[0]);
                    fieldReader.accept(instance, fieldValueCopied);
                }
            }
            return instance;
        }
        else {
            final JSONWriter writer = JSONWriter.ofJSONB(features);
            byte[] jsonbBytes;
            try {
                writer.config(JSONWriter.Feature.WriteClassName);
                objectWriter.writeJSONB(writer, object, null, null, 0L);
                jsonbBytes = writer.getBytes();
                if (writer != null) {
                    writer.close();
                }
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
            final JSONReader jsonReader = JSONReader.ofJSONB(jsonbBytes, JSONReader.Feature.SupportAutoType, JSONReader.Feature.SupportClassForName);
            try {
                if (beanToArray) {
                    jsonReader.context.config(JSONReader.Feature.SupportArrayToBean);
                }
                final T jsonbObject = objectReader.readJSONBObject(jsonReader, null, null, featuresValue);
                if (jsonReader != null) {
                    jsonReader.close();
                }
                return jsonbObject;
            }
            catch (Throwable t2) {
                if (jsonReader != null) {
                    try {
                        jsonReader.close();
                    }
                    catch (Throwable exception2) {
                        t2.addSuppressed(exception2);
                    }
                }
                throw t2;
            }
        }
    }
    
    default <T> T copyTo(final Object object, final Class<T> targetClass, final JSONWriter.Feature... features) {
        if (object == null) {
            return null;
        }
        final Class<?> objectClass = object.getClass();
        boolean fieldBased = false;
        boolean beanToArray = false;
        long featuresValue = 0L;
        for (int i = 0; i < features.length; ++i) {
            final JSONWriter.Feature feature = features[i];
            featuresValue |= feature.mask;
            if (feature == JSONWriter.Feature.FieldBased) {
                fieldBased = true;
            }
            else if (feature == JSONWriter.Feature.BeanToArray) {
                beanToArray = true;
            }
        }
        final ObjectWriter objectWriter = JSONFactory.defaultObjectWriterProvider.getObjectWriter(objectClass, objectClass, fieldBased);
        final ObjectReader objectReader = JSONFactory.defaultObjectReaderProvider.getObjectReader(targetClass, fieldBased);
        if (objectWriter instanceof ObjectWriterAdapter && objectReader instanceof ObjectReaderBean) {
            final List<FieldWriter> fieldWriters = (List<FieldWriter>)objectWriter.getFieldWriters();
            if (objectReader instanceof ObjectReaderNoneDefaultConstructor) {
                final Map<String, Object> map = new HashMap<String, Object>(fieldWriters.size());
                for (int j = 0; j < fieldWriters.size(); ++j) {
                    final FieldWriter fieldWriter = fieldWriters.get(j);
                    final Object fieldValue = fieldWriter.getFieldValue(object);
                    map.put(fieldWriter.fieldName, fieldValue);
                }
                return objectReader.createInstance(map, featuresValue);
            }
            final T instance = objectReader.createInstance(featuresValue);
            for (int j = 0; j < fieldWriters.size(); ++j) {
                final FieldWriter fieldWriter = fieldWriters.get(j);
                final FieldReader fieldReader = objectReader.getFieldReader(fieldWriter.fieldName);
                if (fieldReader != null) {
                    final Object fieldValue2 = fieldWriter.getFieldValue(object);
                    Object fieldValueCopied;
                    if (fieldWriter.fieldClass == Date.class && fieldReader.fieldClass == String.class) {
                        fieldValueCopied = DateUtils.format((Date)fieldValue2, fieldWriter.format);
                    }
                    else if (fieldWriter.fieldClass == LocalDate.class && fieldReader.fieldClass == String.class) {
                        fieldValueCopied = DateUtils.format((LocalDate)fieldValue2, fieldWriter.format);
                    }
                    else if (fieldValue2 == null || fieldReader.supportAcceptType(fieldValue2.getClass())) {
                        fieldValueCopied = fieldValue2;
                    }
                    else {
                        fieldValueCopied = copy(fieldValue2, new JSONWriter.Feature[0]);
                    }
                    fieldReader.accept(instance, fieldValueCopied);
                }
            }
            return instance;
        }
        else {
            final JSONWriter writer = JSONWriter.ofJSONB(features);
            byte[] jsonbBytes;
            try {
                writer.config(JSONWriter.Feature.WriteClassName);
                objectWriter.writeJSONB(writer, object, null, null, 0L);
                jsonbBytes = writer.getBytes();
                if (writer != null) {
                    writer.close();
                }
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
            final JSONReader jsonReader = JSONReader.ofJSONB(jsonbBytes, JSONReader.Feature.SupportAutoType, JSONReader.Feature.SupportClassForName);
            try {
                if (beanToArray) {
                    jsonReader.context.config(JSONReader.Feature.SupportArrayToBean);
                }
                final T jsonbObject = objectReader.readJSONBObject(jsonReader, null, null, 0L);
                if (jsonReader != null) {
                    jsonReader.close();
                }
                return jsonbObject;
            }
            catch (Throwable t2) {
                if (jsonReader != null) {
                    try {
                        jsonReader.close();
                    }
                    catch (Throwable exception2) {
                        t2.addSuppressed(exception2);
                    }
                }
                throw t2;
            }
        }
    }
}
