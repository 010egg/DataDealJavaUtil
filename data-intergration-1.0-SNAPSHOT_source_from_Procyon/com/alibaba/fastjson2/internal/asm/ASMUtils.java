// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.internal.asm;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.Date;
import com.alibaba.fastjson2.annotation.JSONType;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONPathCompilerReflect;
import java.util.function.BiConsumer;
import java.util.function.ObjDoubleConsumer;
import com.alibaba.fastjson2.function.ObjFloatConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.ObjIntConsumer;
import com.alibaba.fastjson2.function.ObjShortConsumer;
import com.alibaba.fastjson2.function.ObjByteConsumer;
import com.alibaba.fastjson2.function.ObjCharConsumer;
import com.alibaba.fastjson2.function.ObjBoolConsumer;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.reader.CharArrayValueConsumer;
import com.alibaba.fastjson2.reader.ByteArrayValueConsumer;
import com.alibaba.fastjson2.reader.ObjectReader12;
import com.alibaba.fastjson2.reader.ObjectReader11;
import com.alibaba.fastjson2.reader.ObjectReader10;
import com.alibaba.fastjson2.reader.ObjectReader9;
import com.alibaba.fastjson2.reader.ObjectReader8;
import com.alibaba.fastjson2.reader.ObjectReader7;
import com.alibaba.fastjson2.reader.ObjectReader6;
import com.alibaba.fastjson2.reader.ObjectReader5;
import com.alibaba.fastjson2.reader.ObjectReader4;
import com.alibaba.fastjson2.reader.ObjectReader3;
import com.alibaba.fastjson2.reader.ObjectReader2;
import com.alibaba.fastjson2.reader.ObjectReader1;
import com.alibaba.fastjson2.reader.ObjectReaderAdapter;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.writer.ObjectWriter12;
import com.alibaba.fastjson2.writer.ObjectWriter11;
import com.alibaba.fastjson2.writer.ObjectWriter10;
import com.alibaba.fastjson2.writer.ObjectWriter9;
import com.alibaba.fastjson2.writer.ObjectWriter8;
import com.alibaba.fastjson2.writer.ObjectWriter7;
import com.alibaba.fastjson2.writer.ObjectWriter6;
import com.alibaba.fastjson2.writer.ObjectWriter5;
import com.alibaba.fastjson2.writer.ObjectWriter4;
import com.alibaba.fastjson2.writer.ObjectWriter3;
import com.alibaba.fastjson2.writer.ObjectWriter2;
import com.alibaba.fastjson2.writer.ObjectWriter1;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import com.alibaba.fastjson2.util.JDKUtils;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.io.Closeable;
import com.alibaba.fastjson2.util.IOUtils;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.format.DateTimeParseException;
import java.lang.reflect.Constructor;
import java.lang.reflect.AccessibleObject;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;

public class ASMUtils
{
    public static final String TYPE_UNSAFE_UTILS;
    public static final String TYPE_OBJECT_WRITER_ADAPTER;
    public static final String TYPE_OBJECT_WRITER_1;
    public static final String TYPE_OBJECT_WRITER_2;
    public static final String TYPE_OBJECT_WRITER_3;
    public static final String TYPE_OBJECT_WRITER_4;
    public static final String TYPE_OBJECT_WRITER_5;
    public static final String TYPE_OBJECT_WRITER_6;
    public static final String TYPE_OBJECT_WRITER_7;
    public static final String TYPE_OBJECT_WRITER_8;
    public static final String TYPE_OBJECT_WRITER_9;
    public static final String TYPE_OBJECT_WRITER_10;
    public static final String TYPE_OBJECT_WRITER_11;
    public static final String TYPE_OBJECT_WRITER_12;
    public static final String TYPE_FIELD_READE;
    public static final String TYPE_JSON_READER;
    public static final String TYPE_OBJECT_READER;
    public static final String TYPE_OBJECT_READER_ADAPTER;
    public static final String TYPE_OBJECT_READER_1;
    public static final String TYPE_OBJECT_READER_2;
    public static final String TYPE_OBJECT_READER_3;
    public static final String TYPE_OBJECT_READER_4;
    public static final String TYPE_OBJECT_READER_5;
    public static final String TYPE_OBJECT_READER_6;
    public static final String TYPE_OBJECT_READER_7;
    public static final String TYPE_OBJECT_READER_8;
    public static final String TYPE_OBJECT_READER_9;
    public static final String TYPE_OBJECT_READER_10;
    public static final String TYPE_OBJECT_READER_11;
    public static final String TYPE_OBJECT_READER_12;
    public static final String TYPE_BYTE_ARRAY_VALUE_CONSUMER;
    public static final String TYPE_CHAR_ARRAY_VALUE_CONSUMER;
    public static final String TYPE_TYPE_UTILS;
    public static final String TYPE_DATE_UTILS;
    public static final String TYPE_OBJECT_WRITER;
    public static final String TYPE_JSON_WRITER;
    public static final String TYPE_FIELD_WRITER;
    public static final String TYPE_OBJECT = "java/lang/Object";
    public static final String DESC_FIELD_WRITER;
    public static final String DESC_FIELD_WRITER_ARRAY;
    public static final String DESC_FIELD_READER;
    public static final String DESC_FIELD_READER_ARRAY;
    public static final String DESC_JSON_READER;
    public static final String DESC_JSON_WRITER;
    public static final String DESC_OBJECT_READER;
    public static final String DESC_OBJECT_WRITER;
    public static final String DESC_SUPPLIER = "Ljava/util/function/Supplier;";
    public static final String DESC_JSONSCHEMA;
    static final Map<MethodInfo, String[]> paramMapping;
    static final Map<Class, String> descMapping;
    static final Map<Class, String> typeMapping;
    static final AtomicReference<char[]> descCacheRef;
    
    public static String type(final Class<?> clazz) {
        final String type = ASMUtils.typeMapping.get(clazz);
        if (type != null) {
            return type;
        }
        if (clazz.isArray()) {
            return "[" + desc(clazz.getComponentType());
        }
        return clazz.getName().replace('.', '/');
    }
    
    public static String desc(final Class<?> clazz) {
        final String desc = ASMUtils.descMapping.get(clazz);
        if (desc != null) {
            return desc;
        }
        if (clazz.isArray()) {
            final Class<?> componentType = clazz.getComponentType();
            return "[" + desc(componentType);
        }
        final String className = clazz.getName();
        char[] chars = ASMUtils.descCacheRef.getAndSet(null);
        if (chars == null) {
            chars = new char[512];
        }
        chars[0] = 'L';
        className.getChars(0, className.length(), chars, 1);
        for (int i = 1; i < chars.length; ++i) {
            if (chars[i] == '.') {
                chars[i] = '/';
            }
        }
        chars[className.length() + 1] = ';';
        final String str = new String(chars, 0, className.length() + 2);
        ASMUtils.descCacheRef.compareAndSet(null, chars);
        return str;
    }
    
    public static String[] lookupParameterNames(final AccessibleObject methodOrCtor) {
        if (methodOrCtor instanceof Constructor) {
            final Constructor constructor = (Constructor)methodOrCtor;
            final Class[] parameterTypes = constructor.getParameterTypes();
            final Class declaringClass = constructor.getDeclaringClass();
            if (declaringClass == DateTimeParseException.class) {
                if (parameterTypes.length == 3) {
                    if (parameterTypes[0] == String.class && parameterTypes[1] == CharSequence.class && parameterTypes[2] == Integer.TYPE) {
                        return new String[] { "message", "parsedString", "errorIndex" };
                    }
                }
                else if (parameterTypes.length == 4 && parameterTypes[0] == String.class && parameterTypes[1] == CharSequence.class && parameterTypes[2] == Integer.TYPE && parameterTypes[3] == Throwable.class) {
                    return new String[] { "message", "parsedString", "errorIndex", "cause" };
                }
            }
            if (Throwable.class.isAssignableFrom(declaringClass)) {
                switch (parameterTypes.length) {
                    case 1: {
                        if (parameterTypes[0] == String.class) {
                            return new String[] { "message" };
                        }
                        if (Throwable.class.isAssignableFrom(parameterTypes[0])) {
                            return new String[] { "cause" };
                        }
                        break;
                    }
                    case 2: {
                        if (parameterTypes[0] == String.class && Throwable.class.isAssignableFrom(parameterTypes[1])) {
                            return new String[] { "message", "cause" };
                        }
                        break;
                    }
                }
            }
        }
        Class<?>[] types;
        String name;
        Class<?> declaringClass2;
        int paramCount;
        if (methodOrCtor instanceof Method) {
            final Method method = (Method)methodOrCtor;
            types = method.getParameterTypes();
            name = method.getName();
            declaringClass2 = method.getDeclaringClass();
            paramCount = method.getParameterCount();
        }
        else {
            final Constructor<?> constructor2 = (Constructor<?>)methodOrCtor;
            types = constructor2.getParameterTypes();
            declaringClass2 = constructor2.getDeclaringClass();
            name = "<init>";
            paramCount = constructor2.getParameterCount();
        }
        if (types.length == 0) {
            return new String[paramCount];
        }
        String[] paramNames = ASMUtils.paramMapping.get(new MethodInfo(declaringClass2.getName(), name, types));
        if (paramNames != null) {
            return paramNames;
        }
        ClassLoader classLoader = declaringClass2.getClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        final String className = declaringClass2.getName();
        final String resourceName = className.replace('.', '/') + ".class";
        final InputStream is = classLoader.getResourceAsStream(resourceName);
        if (is != null) {
            try {
                final ClassReader reader = new ClassReader(is);
                final TypeCollector visitor = new TypeCollector(name, types);
                reader.accept(visitor);
                paramNames = visitor.getParameterNamesForMethod();
                if (paramNames != null && paramNames.length == paramCount - 1) {
                    final Class<?> dd = declaringClass2.getDeclaringClass();
                    if (dd != null && dd.equals(types[0])) {
                        final String[] strings = new String[paramCount];
                        strings[0] = "this$0";
                        System.arraycopy(paramNames, 0, strings, 1, paramNames.length);
                        paramNames = strings;
                    }
                }
                return paramNames;
            }
            catch (IOException ex) {}
            finally {
                IOUtils.close(is);
            }
        }
        paramNames = new String[paramCount];
        int i;
        if (types[0] == declaringClass2.getDeclaringClass() && !Modifier.isStatic(declaringClass2.getModifiers())) {
            paramNames[0] = "this.$0";
            i = 1;
        }
        else {
            i = 0;
        }
        while (i < paramNames.length) {
            paramNames[i] = "arg" + i;
            ++i;
        }
        return paramNames;
    }
    
    static {
        TYPE_UNSAFE_UTILS = JDKUtils.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_ADAPTER = ObjectWriterAdapter.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_1 = ObjectWriter1.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_2 = ObjectWriter2.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_3 = ObjectWriter3.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_4 = ObjectWriter4.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_5 = ObjectWriter5.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_6 = ObjectWriter6.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_7 = ObjectWriter7.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_8 = ObjectWriter8.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_9 = ObjectWriter9.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_10 = ObjectWriter10.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_11 = ObjectWriter11.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER_12 = ObjectWriter12.class.getName().replace('.', '/');
        TYPE_FIELD_READE = FieldReader.class.getName().replace('.', '/');
        TYPE_JSON_READER = JSONReader.class.getName().replace('.', '/');
        TYPE_OBJECT_READER = ObjectReader.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_ADAPTER = ObjectReaderAdapter.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_1 = ObjectReader1.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_2 = ObjectReader2.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_3 = ObjectReader3.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_4 = ObjectReader4.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_5 = ObjectReader5.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_6 = ObjectReader6.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_7 = ObjectReader7.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_8 = ObjectReader8.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_9 = ObjectReader9.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_10 = ObjectReader10.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_11 = ObjectReader11.class.getName().replace('.', '/');
        TYPE_OBJECT_READER_12 = ObjectReader12.class.getName().replace('.', '/');
        TYPE_BYTE_ARRAY_VALUE_CONSUMER = ByteArrayValueConsumer.class.getName().replace('.', '/');
        TYPE_CHAR_ARRAY_VALUE_CONSUMER = CharArrayValueConsumer.class.getName().replace('.', '/');
        TYPE_TYPE_UTILS = TypeUtils.class.getName().replace('.', '/');
        TYPE_DATE_UTILS = DateUtils.class.getName().replace('.', '/');
        TYPE_OBJECT_WRITER = ObjectWriter.class.getName().replace('.', '/');
        TYPE_JSON_WRITER = JSONWriter.class.getName().replace('.', '/');
        TYPE_FIELD_WRITER = FieldWriter.class.getName().replace('.', '/');
        DESC_FIELD_WRITER = 'L' + FieldWriter.class.getName().replace('.', '/') + ';';
        DESC_FIELD_WRITER_ARRAY = "[" + ASMUtils.DESC_FIELD_WRITER;
        DESC_FIELD_READER = 'L' + FieldReader.class.getName().replace('.', '/') + ';';
        DESC_FIELD_READER_ARRAY = "[" + ASMUtils.DESC_FIELD_READER;
        DESC_JSON_READER = 'L' + ASMUtils.TYPE_JSON_READER + ';';
        DESC_JSON_WRITER = 'L' + ASMUtils.TYPE_JSON_WRITER + ';';
        DESC_OBJECT_READER = 'L' + ASMUtils.TYPE_OBJECT_READER + ';';
        DESC_OBJECT_WRITER = 'L' + ASMUtils.TYPE_OBJECT_WRITER + ';';
        DESC_JSONSCHEMA = 'L' + JSONSchema.class.getName().replace('.', '/') + ';';
        paramMapping = new HashMap<MethodInfo, String[]>();
        descMapping = new HashMap<Class, String>();
        typeMapping = new HashMap<Class, String>();
        ASMUtils.paramMapping.put(new MethodInfo("com.alibaba.fastjson2.util.ParameterizedTypeImpl", "<init>", new String[] { "[Ljava.lang.reflect.Type;", "java.lang.reflect.Type", "java.lang.reflect.Type" }), new String[] { "actualTypeArguments", "ownerType", "rawType" });
        ASMUtils.paramMapping.put(new MethodInfo("org.apache.commons.lang3.tuple.Triple", "of", new String[] { "java.lang.Object", "java.lang.Object", "java.lang.Object" }), new String[] { "left", "middle", "right" });
        ASMUtils.paramMapping.put(new MethodInfo("org.apache.commons.lang3.tuple.MutableTriple", "<init>", new String[] { "java.lang.Object", "java.lang.Object", "java.lang.Object" }), new String[] { "left", "middle", "right" });
        ASMUtils.paramMapping.put(new MethodInfo("org.javamoney.moneta.Money", "<init>", new String[] { "java.math.BigDecimal", "javax.money.CurrencyUnit", "javax.money.MonetaryContext" }), new String[] { "number", "currency", "monetaryContext" });
        ASMUtils.paramMapping.put(new MethodInfo("org.javamoney.moneta.Money", "<init>", new String[] { "java.math.BigDecimal", "javax.money.CurrencyUnit" }), new String[] { "number", "currency" });
        ASMUtils.descMapping.put(Integer.TYPE, "I");
        ASMUtils.descMapping.put(Void.TYPE, "V");
        ASMUtils.descMapping.put(Boolean.TYPE, "Z");
        ASMUtils.descMapping.put(Character.TYPE, "C");
        ASMUtils.descMapping.put(Byte.TYPE, "B");
        ASMUtils.descMapping.put(Short.TYPE, "S");
        ASMUtils.descMapping.put(Float.TYPE, "F");
        ASMUtils.descMapping.put(Long.TYPE, "J");
        ASMUtils.descMapping.put(Double.TYPE, "D");
        ASMUtils.typeMapping.put(Integer.TYPE, "I");
        ASMUtils.typeMapping.put(Void.TYPE, "V");
        ASMUtils.typeMapping.put(Boolean.TYPE, "Z");
        ASMUtils.typeMapping.put(Character.TYPE, "C");
        ASMUtils.typeMapping.put(Byte.TYPE, "B");
        ASMUtils.typeMapping.put(Short.TYPE, "S");
        ASMUtils.typeMapping.put(Float.TYPE, "F");
        ASMUtils.typeMapping.put(Long.TYPE, "J");
        ASMUtils.typeMapping.put(Double.TYPE, "D");
        final Class[] array;
        final Class[] classes = array = new Class[] { String.class, List.class, Collection.class, ObjectReader.class, ObjectReader1.class, ObjectReader2.class, ObjectReader3.class, ObjectReader4.class, ObjectReader5.class, ObjectReader6.class, ObjectReader7.class, ObjectReader8.class, ObjectReader9.class, ObjectReader10.class, ObjectReader11.class, ObjectReader12.class, ObjectReaderAdapter.class, FieldReader.class, JSONReader.class, ObjBoolConsumer.class, ObjCharConsumer.class, ObjByteConsumer.class, ObjShortConsumer.class, ObjIntConsumer.class, ObjLongConsumer.class, ObjFloatConsumer.class, ObjDoubleConsumer.class, BiConsumer.class, JDKUtils.class, ObjectWriterAdapter.class, ObjectWriter1.class, ObjectWriter2.class, ObjectWriter3.class, ObjectWriter4.class, ObjectWriter5.class, ObjectWriter6.class, ObjectWriter7.class, ObjectWriter8.class, ObjectWriter9.class, ObjectWriter10.class, ObjectWriter11.class, ObjectWriter12.class, FieldWriter.class, JSONPathCompilerReflect.SingleNamePathTyped.class, JSONWriter.Context.class, JSONB.class, JSONSchema.class, JSONType.class, Date.class, Supplier.class };
        for (final Class objectType : array) {
            final String type = objectType.getName().replace('.', '/');
            ASMUtils.typeMapping.put(objectType, type);
            final String desc = 'L' + type + ';';
            ASMUtils.descMapping.put(objectType, desc);
        }
        ASMUtils.typeMapping.put(JSONWriter.class, ASMUtils.TYPE_JSON_WRITER);
        ASMUtils.descMapping.put(JSONWriter.class, ASMUtils.DESC_JSON_WRITER);
        ASMUtils.typeMapping.put(ObjectWriter.class, ASMUtils.TYPE_OBJECT_WRITER);
        ASMUtils.descMapping.put(ObjectWriter.class, ASMUtils.DESC_OBJECT_WRITER);
        ASMUtils.descMapping.put(FieldWriter[].class, ASMUtils.DESC_FIELD_WRITER_ARRAY);
        ASMUtils.descMapping.put(FieldReader[].class, ASMUtils.DESC_FIELD_READER_ARRAY);
        descCacheRef = new AtomicReference<char[]>();
    }
    
    static class MethodInfo
    {
        final String className;
        final String methodName;
        final String[] paramTypeNames;
        int hash;
        
        public MethodInfo(final String className, final String methodName, final String[] paramTypeNames) {
            this.className = className;
            this.methodName = methodName;
            this.paramTypeNames = paramTypeNames;
        }
        
        public MethodInfo(final String className, final String methodName, final Class[] paramTypes) {
            this.className = className;
            this.methodName = methodName;
            this.paramTypeNames = new String[paramTypes.length];
            for (int i = 0; i < paramTypes.length; ++i) {
                this.paramTypeNames[i] = paramTypes[i].getName();
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final MethodInfo that = (MethodInfo)o;
            return Objects.equals(this.className, that.className) && Objects.equals(this.methodName, that.methodName) && Arrays.equals(this.paramTypeNames, that.paramTypeNames);
        }
        
        @Override
        public int hashCode() {
            if (this.hash == 0) {
                int result = Objects.hash(this.className, this.methodName);
                result = 31 * result + Arrays.hashCode(this.paramTypeNames);
                this.hash = result;
            }
            return this.hash;
        }
    }
}
