// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.util.JDKUtils;
import java.io.Closeable;
import com.alibaba.fastjson2.util.IOUtils;
import java.io.IOException;
import java.security.AccessController;
import java.io.InputStream;
import com.alibaba.fastjson2.filter.ExtraProcessor;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.writer.ObjectWriterCreator;
import com.alibaba.fastjson2.reader.ObjectReaderCreator;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.function.Function;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.time.ZoneId;

public final class JSONFactory
{
    static volatile Throwable initErrorLast;
    public static final String CREATOR;
    public static final String PROPERTY_DENY_PROPERTY = "fastjson2.parser.deny";
    public static final String PROPERTY_AUTO_TYPE_ACCEPT = "fastjson2.autoTypeAccept";
    public static final String PROPERTY_AUTO_TYPE_HANDLER = "fastjson2.autoTypeHandler";
    public static final String PROPERTY_AUTO_TYPE_BEFORE_HANDLER = "fastjson2.autoTypeBeforeHandler";
    static boolean useJacksonAnnotation;
    static long defaultReaderFeatures;
    static String defaultReaderFormat;
    static ZoneId defaultReaderZoneId;
    static long defaultWriterFeatures;
    static String defaultWriterFormat;
    static ZoneId defaultWriterZoneId;
    static Supplier<Map> defaultObjectSupplier;
    static Supplier<List> defaultArraySupplier;
    static final NameCacheEntry[] NAME_CACHE;
    static final NameCacheEntry2[] NAME_CACHE2;
    static final Function<JSONWriter.Context, JSONWriter> INCUBATOR_VECTOR_WRITER_CREATOR_UTF8;
    static final Function<JSONWriter.Context, JSONWriter> INCUBATOR_VECTOR_WRITER_CREATOR_UTF16;
    static final JSONReaderUTF8Creator INCUBATOR_VECTOR_READER_CREATOR_ASCII;
    static final JSONReaderUTF8Creator INCUBATOR_VECTOR_READER_CREATOR_UTF8;
    static final JSONReaderUTF16Creator INCUBATOR_VECTOR_READER_CREATOR_UTF16;
    static final BigDecimal LOW;
    static final BigDecimal HIGH;
    static final BigInteger LOW_BIGINT;
    static final BigInteger HIGH_BIGINT;
    static final char[] CA;
    static final int[] DIGITS2;
    static final float[] FLOAT_10_POW;
    static final double[] DOUBLE_10_POW;
    static final Double DOUBLE_ZERO;
    static final CacheItem[] CACHE_ITEMS;
    static final int CACHE_THRESHOLD = 1048576;
    static final AtomicReferenceFieldUpdater<CacheItem, char[]> CHARS_UPDATER;
    static final AtomicReferenceFieldUpdater<CacheItem, byte[]> BYTES_UPDATER;
    static final Properties DEFAULT_PROPERTIES;
    static final ObjectWriterProvider defaultObjectWriterProvider;
    static final ObjectReaderProvider defaultObjectReaderProvider;
    static final JSONPathCompiler defaultJSONPathCompiler;
    static final ThreadLocal<ObjectReaderCreator> readerCreatorLocal;
    static final ThreadLocal<ObjectReaderProvider> readerProviderLocal;
    static final ThreadLocal<ObjectWriterCreator> writerCreatorLocal;
    static final ThreadLocal<JSONPathCompiler> jsonPathCompilerLocal;
    static final ObjectReader<JSONArray> ARRAY_READER;
    static final ObjectReader<JSONObject> OBJECT_READER;
    static final byte[] UUID_VALUES;
    
    public static String getProperty(final String key) {
        return JSONFactory.DEFAULT_PROPERTIES.getProperty(key);
    }
    
    public static boolean isUseJacksonAnnotation() {
        return JSONFactory.useJacksonAnnotation;
    }
    
    public static void setUseJacksonAnnotation(final boolean useJacksonAnnotation) {
        JSONFactory.useJacksonAnnotation = useJacksonAnnotation;
    }
    
    public static void setDefaultObjectSupplier(final Supplier<Map> objectSupplier) {
        JSONFactory.defaultObjectSupplier = objectSupplier;
    }
    
    public static void setDefaultArraySupplier(final Supplier<List> arraySupplier) {
        JSONFactory.defaultArraySupplier = arraySupplier;
    }
    
    public static Supplier<Map> getDefaultObjectSupplier() {
        return JSONFactory.defaultObjectSupplier;
    }
    
    public static Supplier<List> getDefaultArraySupplier() {
        return JSONFactory.defaultArraySupplier;
    }
    
    public static JSONWriter.Context createWriteContext() {
        return new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider);
    }
    
    public static JSONWriter.Context createWriteContext(final ObjectWriterProvider provider, final JSONWriter.Feature... features) {
        final JSONWriter.Context context = new JSONWriter.Context(provider);
        context.config(features);
        return context;
    }
    
    public static JSONWriter.Context createWriteContext(final JSONWriter.Feature... features) {
        return new JSONWriter.Context(JSONFactory.defaultObjectWriterProvider, features);
    }
    
    public static JSONReader.Context createReadContext() {
        final ObjectReaderProvider provider = getDefaultObjectReaderProvider();
        return new JSONReader.Context(provider);
    }
    
    public static JSONReader.Context createReadContext(final long features) {
        final ObjectReaderProvider provider = getDefaultObjectReaderProvider();
        return new JSONReader.Context(provider, features);
    }
    
    public static JSONReader.Context createReadContext(final JSONReader.Feature... features) {
        final JSONReader.Context context = new JSONReader.Context(getDefaultObjectReaderProvider());
        for (int i = 0; i < features.length; ++i) {
            final JSONReader.Context context2 = context;
            context2.features |= features[i].mask;
        }
        return context;
    }
    
    public static JSONReader.Context createReadContext(final Filter filter, final JSONReader.Feature... features) {
        final JSONReader.Context context = new JSONReader.Context(getDefaultObjectReaderProvider());
        if (filter instanceof JSONReader.AutoTypeBeforeHandler) {
            context.autoTypeBeforeHandler = (JSONReader.AutoTypeBeforeHandler)filter;
        }
        if (filter instanceof ExtraProcessor) {
            context.extraProcessor = (ExtraProcessor)filter;
        }
        for (int i = 0; i < features.length; ++i) {
            final JSONReader.Context context2 = context;
            context2.features |= features[i].mask;
        }
        return context;
    }
    
    public static JSONReader.Context createReadContext(ObjectReaderProvider provider, final JSONReader.Feature... features) {
        if (provider == null) {
            provider = getDefaultObjectReaderProvider();
        }
        final JSONReader.Context context = new JSONReader.Context(provider);
        context.config(features);
        return context;
    }
    
    public static JSONReader.Context createReadContext(final SymbolTable symbolTable) {
        final ObjectReaderProvider provider = getDefaultObjectReaderProvider();
        return new JSONReader.Context(provider, symbolTable);
    }
    
    public static JSONReader.Context createReadContext(final SymbolTable symbolTable, final JSONReader.Feature... features) {
        final ObjectReaderProvider provider = getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider, symbolTable);
        context.config(features);
        return context;
    }
    
    public static JSONReader.Context createReadContext(final Supplier<Map> objectSupplier, final JSONReader.Feature... features) {
        final ObjectReaderProvider provider = getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider);
        context.setObjectSupplier(objectSupplier);
        context.config(features);
        return context;
    }
    
    public static JSONReader.Context createReadContext(final Supplier<Map> objectSupplier, final Supplier<List> arraySupplier, final JSONReader.Feature... features) {
        final ObjectReaderProvider provider = getDefaultObjectReaderProvider();
        final JSONReader.Context context = new JSONReader.Context(provider);
        context.setObjectSupplier(objectSupplier);
        context.setArraySupplier(arraySupplier);
        context.config(features);
        return context;
    }
    
    public static ObjectWriterProvider getDefaultObjectWriterProvider() {
        return JSONFactory.defaultObjectWriterProvider;
    }
    
    public static ObjectReaderProvider getDefaultObjectReaderProvider() {
        final ObjectReaderProvider providerLocal = JSONFactory.readerProviderLocal.get();
        if (providerLocal != null) {
            return providerLocal;
        }
        return JSONFactory.defaultObjectReaderProvider;
    }
    
    public static JSONPathCompiler getDefaultJSONPathCompiler() {
        final JSONPathCompiler compilerLocal = JSONFactory.jsonPathCompilerLocal.get();
        if (compilerLocal != null) {
            return compilerLocal;
        }
        return JSONFactory.defaultJSONPathCompiler;
    }
    
    public static void setContextReaderCreator(final ObjectReaderCreator creator) {
        JSONFactory.readerCreatorLocal.set(creator);
    }
    
    public static void setContextObjectReaderProvider(final ObjectReaderProvider creator) {
        JSONFactory.readerProviderLocal.set(creator);
    }
    
    public static ObjectReaderCreator getContextReaderCreator() {
        return JSONFactory.readerCreatorLocal.get();
    }
    
    public static void setContextJSONPathCompiler(final JSONPathCompiler compiler) {
        JSONFactory.jsonPathCompilerLocal.set(compiler);
    }
    
    public static void setContextWriterCreator(final ObjectWriterCreator creator) {
        JSONFactory.writerCreatorLocal.set(creator);
    }
    
    public static ObjectWriterCreator getContextWriterCreator() {
        return JSONFactory.writerCreatorLocal.get();
    }
    
    static {
        NAME_CACHE = new NameCacheEntry[8192];
        NAME_CACHE2 = new NameCacheEntry2[8192];
        LOW = BigDecimal.valueOf(-9007199254740991L);
        HIGH = BigDecimal.valueOf(9007199254740991L);
        LOW_BIGINT = BigInteger.valueOf(-9007199254740991L);
        HIGH_BIGINT = BigInteger.valueOf(9007199254740991L);
        CA = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
        DIGITS2 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, 0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 11, 12, 13, 14, 15 };
        FLOAT_10_POW = new float[] { 1.0f, 10.0f, 100.0f, 1000.0f, 10000.0f, 100000.0f, 1000000.0f, 1.0E7f, 1.0E8f, 1.0E9f, 1.0E10f };
        DOUBLE_10_POW = new double[] { 1.0, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 1.0E7, 1.0E8, 1.0E9, 1.0E10, 1.0E11, 1.0E12, 1.0E13, 1.0E14, 1.0E15, 1.0E16, 1.0E17, 1.0E18, 1.0E19, 1.0E20, 1.0E21, 1.0E22 };
        DOUBLE_ZERO = 0.0;
        final Properties properties = new Properties();
        final ClassLoader cl;
        final String resourceFile;
        final InputStream inputStream = AccessController.doPrivileged(() -> {
            cl = Thread.currentThread().getContextClassLoader();
            resourceFile = "fastjson2.properties";
            if (cl != null) {
                return cl.getResourceAsStream("fastjson2.properties");
            }
            else {
                return ClassLoader.getSystemResourceAsStream("fastjson2.properties");
            }
        });
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            }
            catch (IOException ex) {}
            finally {
                IOUtils.close(inputStream);
            }
        }
        DEFAULT_PROPERTIES = properties;
        String property = System.getProperty("fastjson2.creator");
        if (property != null) {
            property = property.trim();
        }
        if (property == null || property.isEmpty()) {
            property = properties.getProperty("fastjson2.creator");
            if (property != null) {
                property = property.trim();
            }
        }
        CREATOR = ((property == null) ? "asm" : property);
        property = System.getProperty("fastjson2.useJacksonAnnotation");
        if (property != null) {
            property = property.trim();
        }
        if (property == null || property.isEmpty()) {
            property = properties.getProperty("fastjson2.useJacksonAnnotation");
            if (property != null) {
                property = property.trim();
            }
        }
        JSONFactory.useJacksonAnnotation = !"false".equals(property);
        boolean readerVector = false;
        String property2 = System.getProperty("fastjson2.readerVector");
        if (property2 != null) {
            property2 = property2.trim();
            if (property2.isEmpty()) {
                property2 = properties.getProperty("fastjson2.readerVector");
                if (property2 != null) {
                    property2 = property2.trim();
                }
            }
            readerVector = !"false".equals(property2);
        }
        Function<JSONWriter.Context, JSONWriter> incubatorVectorCreatorUTF8 = null;
        Function<JSONWriter.Context, JSONWriter> incubatorVectorCreatorUTF9 = null;
        JSONReaderUTF8Creator readerCreatorASCII = null;
        JSONReaderUTF8Creator readerCreatorUTF8 = null;
        JSONReaderUTF16Creator readerCreatorUTF9 = null;
        if (JDKUtils.VECTOR_SUPPORT) {
            if (JDKUtils.VECTOR_BIT_LENGTH >= 64) {
                try {
                    final Class<?> factoryClass = Class.forName("com.alibaba.fastjson2.JSONWriterUTF8Vector$Factory");
                    incubatorVectorCreatorUTF8 = (Function<JSONWriter.Context, JSONWriter>)factoryClass.newInstance();
                }
                catch (Throwable e) {
                    JSONFactory.initErrorLast = e;
                }
                try {
                    final Class<?> factoryClass = Class.forName("com.alibaba.fastjson2.JSONWriterUTF16Vector$Factory");
                    incubatorVectorCreatorUTF9 = (Function<JSONWriter.Context, JSONWriter>)factoryClass.newInstance();
                }
                catch (Throwable e) {
                    JSONFactory.initErrorLast = e;
                }
                if (readerVector) {
                    try {
                        final Class<?> factoryClass = Class.forName("com.alibaba.fastjson2.JSONReaderASCIIVector$Factory");
                        readerCreatorASCII = (JSONReaderUTF8Creator)factoryClass.newInstance();
                    }
                    catch (Throwable e) {
                        JSONFactory.initErrorLast = e;
                    }
                    try {
                        final Class<?> factoryClass = Class.forName("com.alibaba.fastjson2.JSONReaderUTF8Vector$Factory");
                        readerCreatorUTF8 = (JSONReaderUTF8Creator)factoryClass.newInstance();
                    }
                    catch (Throwable e) {
                        JSONFactory.initErrorLast = e;
                    }
                }
            }
            if (JDKUtils.VECTOR_BIT_LENGTH >= 128 && readerVector) {
                try {
                    final Class<?> factoryClass = Class.forName("com.alibaba.fastjson2.JSONReaderUTF16Vector$Factory");
                    readerCreatorUTF9 = (JSONReaderUTF16Creator)factoryClass.newInstance();
                }
                catch (Throwable e) {
                    JSONFactory.initErrorLast = e;
                }
            }
        }
        INCUBATOR_VECTOR_WRITER_CREATOR_UTF8 = incubatorVectorCreatorUTF8;
        INCUBATOR_VECTOR_WRITER_CREATOR_UTF16 = incubatorVectorCreatorUTF9;
        INCUBATOR_VECTOR_READER_CREATOR_ASCII = readerCreatorASCII;
        INCUBATOR_VECTOR_READER_CREATOR_UTF8 = readerCreatorUTF8;
        INCUBATOR_VECTOR_READER_CREATOR_UTF16 = readerCreatorUTF9;
        final CacheItem[] items = new CacheItem[16];
        for (int i = 0; i < items.length; ++i) {
            items[i] = new CacheItem();
        }
        CACHE_ITEMS = items;
        CHARS_UPDATER = AtomicReferenceFieldUpdater.newUpdater(CacheItem.class, char[].class, "chars");
        BYTES_UPDATER = AtomicReferenceFieldUpdater.newUpdater(CacheItem.class, byte[].class, "bytes");
        defaultObjectWriterProvider = new ObjectWriterProvider();
        defaultObjectReaderProvider = new ObjectReaderProvider();
        JSONPathCompilerReflect compiler = null;
        final String creator = JSONFactory.CREATOR;
        switch (creator) {
            case "reflect":
            case "lambda": {
                compiler = JSONPathCompilerReflect.INSTANCE;
                break;
            }
            default: {
                try {
                    if (!JDKUtils.ANDROID && !JDKUtils.GRAAL) {
                        compiler = JSONPathCompilerReflectASM.INSTANCE;
                    }
                }
                catch (Throwable t) {}
                if (compiler == null) {
                    compiler = JSONPathCompilerReflect.INSTANCE;
                    break;
                }
                break;
            }
        }
        defaultJSONPathCompiler = compiler;
        readerCreatorLocal = new ThreadLocal<ObjectReaderCreator>();
        readerProviderLocal = new ThreadLocal<ObjectReaderProvider>();
        writerCreatorLocal = new ThreadLocal<ObjectWriterCreator>();
        jsonPathCompilerLocal = new ThreadLocal<JSONPathCompiler>();
        ARRAY_READER = getDefaultObjectReaderProvider().getObjectReader(JSONArray.class);
        OBJECT_READER = getDefaultObjectReaderProvider().getObjectReader(JSONObject.class);
        UUID_VALUES = new byte[55];
        for (char c = '0'; c <= '9'; ++c) {
            JSONFactory.UUID_VALUES[c - '0'] = (byte)(c - '0');
        }
        for (char c = 'a'; c <= 'f'; ++c) {
            JSONFactory.UUID_VALUES[c - '0'] = (byte)(c - 'a' + 10);
        }
        for (char c = 'A'; c <= 'F'; ++c) {
            JSONFactory.UUID_VALUES[c - '0'] = (byte)(c - 'A' + 10);
        }
    }
    
    static final class NameCacheEntry
    {
        final String name;
        final long value;
        
        public NameCacheEntry(final String name, final long value) {
            this.name = name;
            this.value = value;
        }
    }
    
    static final class NameCacheEntry2
    {
        final String name;
        final long value0;
        final long value1;
        
        public NameCacheEntry2(final String name, final long value0, final long value1) {
            this.name = name;
            this.value0 = value0;
            this.value1 = value1;
        }
    }
    
    static final class CacheItem
    {
        volatile char[] chars;
        volatile byte[] bytes;
    }
    
    public interface JSONPathCompiler
    {
        JSONPath compile(final Class p0, final JSONPath p1);
    }
    
    interface JSONReaderUTF8Creator
    {
        JSONReader create(final JSONReader.Context p0, final String p1, final byte[] p2, final int p3, final int p4);
    }
    
    interface JSONReaderUTF16Creator
    {
        JSONReader create(final JSONReader.Context p0, final String p1, final char[] p2, final int p3, final int p4);
    }
}
