// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.function.ObjDoubleConsumer;
import com.alibaba.fastjson2.function.ObjFloatConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.ObjIntConsumer;
import com.alibaba.fastjson2.function.ObjShortConsumer;
import com.alibaba.fastjson2.function.ObjByteConsumer;
import com.alibaba.fastjson2.function.ObjCharConsumer;
import com.alibaba.fastjson2.function.ObjBoolConsumer;
import java.util.function.Consumer;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;
import java.util.function.BiConsumer;
import com.alibaba.fastjson2.function.FieldConsumer;
import com.alibaba.fastjson2.function.FieldBiConsumer;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.OptionalInt;
import java.util.Optional;
import java.util.Collection;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import java.util.Calendar;
import java.util.Date;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.util.UUID;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.IdentityHashMap;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.util.Fnv;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import com.alibaba.fastjson2.internal.CodeGenUtils;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import com.alibaba.fastjson2.internal.asm.Label;
import com.alibaba.fastjson2.internal.asm.MethodWriter;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.internal.asm.ASMUtils;
import com.alibaba.fastjson2.internal.asm.ClassWriter;
import java.util.function.Function;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import java.lang.reflect.Constructor;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.codec.BeanInfo;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.util.Locale;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.util.IOUtils;
import java.util.Map;
import com.alibaba.fastjson2.util.DynamicClassLoader;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectReaderCreatorASM extends ObjectReaderCreator
{
    public static final ObjectReaderCreatorASM INSTANCE;
    protected static final AtomicLong seed;
    protected final DynamicClassLoader classLoader;
    static final String METHOD_DESC_GET_ITEM_OBJECT_READER;
    static final String METHOD_DESC_GET_OBJECT_READER_1;
    static final String METHOD_DESC_INIT;
    static final String METHOD_DESC_ADAPTER_INIT;
    static final String METHOD_DESC_READ_OBJECT;
    static final String METHOD_DESC_GET_FIELD_READER;
    static final String METHOD_DESC_READ_FIELD_VALUE;
    static final String METHOD_DESC_ADD_RESOLVE_TASK;
    static final String METHOD_DESC_ADD_RESOLVE_TASK_2;
    static final String METHOD_DESC_CHECK_ARRAY_AUTO_TYPE;
    static final String METHOD_DESC_PROCESS_EXTRA;
    static final String METHOD_DESC_JSON_READER_CHECK_ARRAY_AUTO_TYPE;
    static final int THIS = 0;
    static final String packageName;
    static final Map<Class, FieldReaderInfo> infos;
    static final String[] fieldItemObjectReader;
    
    static String fieldObjectReader(final int i) {
        switch (i) {
            case 0: {
                return "objectReader0";
            }
            case 1: {
                return "objectReader1";
            }
            case 2: {
                return "objectReader2";
            }
            case 3: {
                return "objectReader3";
            }
            case 4: {
                return "objectReader4";
            }
            case 5: {
                return "objectReader5";
            }
            case 6: {
                return "objectReader6";
            }
            case 7: {
                return "objectReader7";
            }
            case 8: {
                return "objectReader8";
            }
            case 9: {
                return "objectReader9";
            }
            case 10: {
                return "objectReader10";
            }
            case 11: {
                return "objectReader11";
            }
            case 12: {
                return "objectReader12";
            }
            case 13: {
                return "objectReader13";
            }
            case 14: {
                return "objectReader14";
            }
            case 15: {
                return "objectReader15";
            }
            default: {
                final String base = "objectReader";
                final int size = IOUtils.stringSize(i);
                final char[] chars = new char[base.length() + size];
                base.getChars(0, base.length(), chars, 0);
                IOUtils.getChars(i, chars.length, chars);
                return new String(chars);
            }
        }
    }
    
    static String fieldItemObjectReader(final int i) {
        String fieldName = ObjectReaderCreatorASM.fieldItemObjectReader[i];
        if (fieldName != null) {
            return fieldName;
        }
        final String base = "itemReader";
        final int size = IOUtils.stringSize(i);
        final char[] chars = new char[base.length() + size];
        base.getChars(0, base.length(), chars, 0);
        IOUtils.getChars(i, chars.length, chars);
        fieldName = (ObjectReaderCreatorASM.fieldItemObjectReader[i] = new String(chars));
        return fieldName;
    }
    
    @Override
    public <T> FieldReader<T> createFieldReader(final Class objectClass, final Type objectType, final String fieldName, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final String schema, final Type fieldType, final Class fieldClass, final Field field, final ObjectReader initReader) {
        return super.createFieldReader(objectClass, objectType, fieldName, ordinal, features, format, locale, defaultValue, schema, fieldType, fieldClass, field, initReader);
    }
    
    public ObjectReaderCreatorASM(final ClassLoader classLoader) {
        this.classLoader = (DynamicClassLoader)((classLoader instanceof DynamicClassLoader) ? classLoader : new DynamicClassLoader(classLoader));
    }
    
    @Override
    public <T> ObjectReader<T> createObjectReader(final Class<T> objectClass, final Type objectType, boolean fieldBased, final ObjectReaderProvider provider) {
        final boolean externalClass = objectClass != null && this.classLoader.isExternalClass(objectClass);
        final int objectClassModifiers = objectClass.getModifiers();
        if (Modifier.isAbstract(objectClassModifiers) || Modifier.isInterface(objectClassModifiers)) {
            return super.createObjectReader(objectClass, objectType, fieldBased, provider);
        }
        final BeanInfo beanInfo = new BeanInfo();
        provider.getBeanInfo(beanInfo, objectClass);
        if (externalClass || !Modifier.isPublic(objectClassModifiers)) {
            final BeanInfo beanInfo2 = beanInfo;
            beanInfo2.readerFeatures |= 0x40000000000000L;
        }
        if (beanInfo.deserializer != null && ObjectReader.class.isAssignableFrom(beanInfo.deserializer)) {
            try {
                final Constructor constructor = beanInfo.deserializer.getDeclaredConstructor((Class[])new Class[0]);
                constructor.setAccessible(true);
                return constructor.newInstance(new Object[0]);
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("create deserializer error", e);
            }
        }
        if (fieldBased && objectClass.isInterface()) {
            fieldBased = false;
        }
        if (Enum.class.isAssignableFrom(objectClass) && (beanInfo.createMethod == null || beanInfo.createMethod.getParameterCount() == 1)) {
            return (ObjectReader<T>)this.createEnumReader(objectClass, beanInfo.createMethod, provider);
        }
        if (beanInfo.creatorConstructor != null || beanInfo.createMethod != null) {
            return this.createObjectReaderWithCreator(objectClass, objectType, provider, beanInfo);
        }
        if (beanInfo.builder != null) {
            return this.createObjectReaderWithBuilder(objectClass, objectType, provider, beanInfo);
        }
        if (Throwable.class.isAssignableFrom(objectClass) || BeanUtils.isExtendedMap(objectClass)) {
            return super.createObjectReader(objectClass, objectType, fieldBased, provider);
        }
        final FieldReader[] fieldReaderArray = this.createFieldReaders(objectClass, objectType, beanInfo, fieldBased, provider);
        boolean match = true;
        if (!fieldBased) {
            if (JDKUtils.JVM_VERSION >= 9 && objectClass == StackTraceElement.class) {
                try {
                    final Constructor<StackTraceElement> constructor2 = StackTraceElement.class.getConstructor(String.class, String.class, String.class, String.class, String.class, String.class, Integer.TYPE);
                    return this.createObjectReaderNoneDefaultConstructor(constructor2, "", "classLoaderName", "moduleName", "moduleVersion", "declaringClass", "methodName", "fileName", "lineNumber");
                }
                catch (NoSuchMethodException ex3) {}
                catch (SecurityException ex4) {}
            }
            for (final FieldReader fieldReader : fieldReaderArray) {
                final Method method = fieldReader.method;
                if (fieldReader.isReadOnly() || fieldReader.isUnwrapped()) {
                    match = false;
                    break;
                }
                if ((fieldReader.features & 0x8000000000000L) != 0x0L) {
                    match = false;
                    break;
                }
            }
        }
        if (beanInfo.autoTypeBeforeHandler != null) {
            match = false;
        }
        if (match) {
            for (final FieldReader fieldReader : fieldReaderArray) {
                if (fieldReader.defaultValue != null || fieldReader.schema != null) {
                    match = false;
                    break;
                }
                final Class fieldClass = fieldReader.fieldClass;
                if (!Modifier.isPublic(fieldClass.getModifiers())) {
                    match = false;
                    break;
                }
            }
        }
        if (match && beanInfo.schema != null && !beanInfo.schema.isEmpty()) {
            match = false;
        }
        if (!match) {
            return super.createObjectReader(objectClass, objectType, fieldBased, provider);
        }
        Constructor defaultConstructor = null;
        if (!Modifier.isInterface(objectClassModifiers) && !Modifier.isAbstract(objectClassModifiers)) {
            final Constructor constructor3 = BeanUtils.getDefaultConstructor(objectClass, true);
            if (constructor3 != null) {
                defaultConstructor = constructor3;
                try {
                    constructor3.setAccessible(true);
                }
                catch (SecurityException ex5) {}
            }
        }
        if (beanInfo.seeAlso != null && beanInfo.seeAlso.length != 0) {
            return this.createObjectReaderSeeAlso(objectClass, beanInfo.typeKey, beanInfo.seeAlso, beanInfo.seeAlsoNames, beanInfo.seeAlsoDefault, fieldReaderArray);
        }
        if (!fieldBased && defaultConstructor == null) {
            return super.createObjectReader(objectClass, objectType, false, provider);
        }
        return (ObjectReader<T>)this.jitObjectReader(objectClass, objectType, fieldBased, externalClass, objectClassModifiers, beanInfo, null, fieldReaderArray, defaultConstructor);
    }
    
    @Override
    public <T> ObjectReader<T> createObjectReader(final Class<T> objectClass, final String typeKey, final long features, final JSONSchema schema, final Supplier<T> defaultCreator, final Function buildFunction, final FieldReader... fieldReaders) {
        if (objectClass == null && defaultCreator != null && buildFunction == null) {
            boolean allFunction = true;
            for (int i = 0; i < fieldReaders.length; ++i) {
                final FieldReader fieldReader = fieldReaders[i];
                if (fieldReader.getFunction() == null) {
                    allFunction = false;
                    break;
                }
            }
            if (allFunction) {
                final BeanInfo beanInfo = new BeanInfo();
                return (ObjectReader<T>)this.jitObjectReader(objectClass, objectClass, false, false, 0, beanInfo, defaultCreator, fieldReaders, null);
            }
        }
        return super.createObjectReader(objectClass, typeKey, features, schema, defaultCreator, buildFunction, fieldReaders);
    }
    
    private <T> ObjectReaderBean jitObjectReader(final Class<T> objectClass, final Type objectType, final boolean fieldBased, final boolean externalClass, final int objectClassModifiers, final BeanInfo beanInfo, final Supplier<T> defaultCreator, final FieldReader[] fieldReaderArray, final Constructor defaultConstructor) {
        final ClassWriter cw = new ClassWriter(e -> objectClass.getName().equals(e) ? objectClass : null);
        final ObjectWriteContext context = new ObjectWriteContext(objectClass, cw, externalClass, fieldReaderArray);
        final String className = "ORG_" + ObjectReaderCreatorASM.seed.incrementAndGet() + "_" + fieldReaderArray.length + ((objectClass == null) ? "" : ("_" + objectClass.getSimpleName()));
        final Package pkg = ObjectReaderCreatorASM.class.getPackage();
        String classNameFull;
        String classNameType;
        if (pkg != null) {
            final String packageName = pkg.getName();
            final int packageNameLength = packageName.length();
            final int charsLength = packageNameLength + 1 + className.length();
            final char[] chars = new char[charsLength];
            packageName.getChars(0, packageName.length(), chars, 0);
            chars[packageNameLength] = '.';
            className.getChars(0, className.length(), chars, packageNameLength + 1);
            classNameFull = new String(chars);
            chars[packageNameLength] = '/';
            for (int i = 0; i < packageNameLength; ++i) {
                if (chars[i] == '.') {
                    chars[i] = '/';
                }
            }
            classNameType = new String(chars);
        }
        else {
            classNameType = className;
            classNameFull = className;
        }
        final boolean generatedFields = fieldReaderArray.length < 128;
        String objectReaderSuper = null;
        switch (fieldReaderArray.length) {
            case 1: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_1;
                break;
            }
            case 2: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_2;
                break;
            }
            case 3: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_3;
                break;
            }
            case 4: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_4;
                break;
            }
            case 5: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_5;
                break;
            }
            case 6: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_6;
                break;
            }
            case 7: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_7;
                break;
            }
            case 8: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_8;
                break;
            }
            case 9: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_9;
                break;
            }
            case 10: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_10;
                break;
            }
            case 11: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_11;
                break;
            }
            case 12: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_12;
                break;
            }
            default: {
                objectReaderSuper = ASMUtils.TYPE_OBJECT_READER_ADAPTER;
                break;
            }
        }
        if (generatedFields) {
            this.genFields(fieldReaderArray, cw, objectReaderSuper);
        }
        cw.visit(52, 49, classNameType, objectReaderSuper, new String[0]);
        final int CLASS = 1;
        final int SUPPLIER = 2;
        final int FIELD_READER_ARRAY = 3;
        final MethodWriter mw = cw.visitMethod(1, "<init>", ObjectReaderCreatorASM.METHOD_DESC_INIT, (fieldReaderArray.length <= 12) ? 32 : 128);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        if (beanInfo.typeKey != null) {
            mw.visitLdcInsn(beanInfo.typeKey);
        }
        else {
            mw.visitInsn(1);
        }
        mw.visitInsn(1);
        mw.visitLdcInsn(beanInfo.readerFeatures);
        mw.visitInsn(1);
        mw.visitVarInsn(25, 2);
        mw.visitInsn(1);
        mw.visitVarInsn(25, 3);
        mw.visitMethodInsn(183, objectReaderSuper, "<init>", ObjectReaderCreatorASM.METHOD_DESC_ADAPTER_INIT, false);
        this.genInitFields(fieldReaderArray, classNameType, generatedFields, 0, 3, mw, objectReaderSuper);
        mw.visitInsn(177);
        mw.visitMaxs(3, 3);
        final String TYPE_OBJECT = (objectClass == null) ? "java/lang/Object" : ASMUtils.type(objectClass);
        final String methodName = (fieldBased && defaultConstructor == null) ? "createInstance0" : "createInstance";
        if (fieldBased && (defaultConstructor == null || !Modifier.isPublic(defaultConstructor.getModifiers()) || !Modifier.isPublic(objectClass.getModifiers()))) {
            final MethodWriter mw2 = cw.visitMethod(1, methodName, "(J)Ljava/lang/Object;", 32);
            mw2.visitFieldInsn(178, ASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
            mw2.visitVarInsn(25, 0);
            mw2.visitFieldInsn(180, ASMUtils.TYPE_OBJECT_READER_ADAPTER, "objectClass", "Ljava/lang/Class;");
            mw2.visitMethodInsn(182, "sun/misc/Unsafe", "allocateInstance", "(Ljava/lang/Class;)Ljava/lang/Object;", false);
            mw2.visitInsn(176);
            mw2.visitMaxs(3, 3);
        }
        else if (defaultConstructor != null && Modifier.isPublic(defaultConstructor.getModifiers()) && Modifier.isPublic(objectClass.getModifiers())) {
            final MethodWriter mw2 = cw.visitMethod(1, methodName, "(J)Ljava/lang/Object;", 32);
            newObject(mw2, TYPE_OBJECT, defaultConstructor);
            mw2.visitInsn(176);
            mw2.visitMaxs(3, 3);
        }
        Supplier<T> supplier;
        if (defaultConstructor != null) {
            final boolean publicObject = Modifier.isPublic(objectClassModifiers) && !this.classLoader.isExternalClass(objectClass);
            final boolean jit = !publicObject || !Modifier.isPublic(defaultConstructor.getModifiers());
            supplier = this.createSupplier(defaultConstructor, jit);
        }
        else {
            supplier = defaultCreator;
        }
        if (generatedFields) {
            long readerFeatures = beanInfo.readerFeatures;
            if (fieldBased) {
                readerFeatures |= JSONReader.Feature.FieldBased.mask;
            }
            final ObjectReaderAdapter objectReaderAdapter = new ObjectReaderAdapter(objectClass, beanInfo.typeKey, beanInfo.typeName, readerFeatures, null, (Supplier<T>)supplier, null, fieldReaderArray);
            this.genMethodReadJSONBObject(context, defaultConstructor, readerFeatures, TYPE_OBJECT, fieldReaderArray, cw, classNameType, objectReaderAdapter);
            this.genMethodReadJSONBObjectArrayMapping(context, defaultConstructor, readerFeatures, TYPE_OBJECT, fieldReaderArray, cw, classNameType, objectReaderAdapter);
            this.genMethodReadObject(context, defaultConstructor, readerFeatures, TYPE_OBJECT, fieldReaderArray, cw, classNameType, objectReaderAdapter);
            if (objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_ADAPTER || objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_1 || objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_2 || objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_3 || objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_4 || objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_5 || objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_6 || objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_7 || objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_8 || objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_9 || objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_10 || objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_11 || objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_12) {
                this.genMethodGetFieldReader(fieldReaderArray, cw, classNameType, objectReaderAdapter);
                this.genMethodGetFieldReaderLCase(fieldReaderArray, cw, classNameType, objectReaderAdapter);
            }
        }
        final byte[] code = cw.toByteArray();
        try {
            final Class<?> readerClass = this.classLoader.defineClassPublic(classNameFull, code, 0, code.length);
            final Constructor<?> constructor = readerClass.getConstructors()[0];
            return (ObjectReaderBean)constructor.newInstance(objectClass, supplier, fieldReaderArray);
        }
        catch (Throwable e2) {
            throw new JSONException("create objectReader error" + ((objectType == null) ? "" : (", objectType " + objectType.getTypeName())), e2);
        }
    }
    
    private static void newObject(final MethodWriter mw, final String TYPE_OBJECT, final Constructor defaultConstructor) {
        mw.visitTypeInsn(187, TYPE_OBJECT);
        mw.visitInsn(89);
        if (defaultConstructor.getParameterCount() == 0) {
            mw.visitMethodInsn(183, TYPE_OBJECT, "<init>", "()V", false);
        }
        else {
            final Class paramType = defaultConstructor.getParameterTypes()[0];
            mw.visitInsn(1);
            mw.visitMethodInsn(183, TYPE_OBJECT, "<init>", "(" + ASMUtils.desc(paramType) + ")V", false);
        }
    }
    
    private void genMethodGetFieldReader(final FieldReader[] fieldReaderArray, final ClassWriter cw, final String classNameType, final ObjectReaderAdapter objectReaderAdapter) {
        final MethodWriter mw = cw.visitMethod(1, "getFieldReader", "(J)" + ASMUtils.DESC_FIELD_READER, 512);
        final int HASH_CODE_64 = 1;
        final int HASH_CODE_65 = 3;
        final Label rtnlt = new Label();
        if (fieldReaderArray.length > 6) {
            final Map<Integer, List<Long>> map = new TreeMap<Integer, List<Long>>();
            for (int i = 0; i < objectReaderAdapter.hashCodes.length; ++i) {
                final long hashCode64 = objectReaderAdapter.hashCodes[i];
                final int hashCode65 = (int)(hashCode64 ^ hashCode64 >>> 32);
                final List<Long> hashCode64List = map.computeIfAbsent(Integer.valueOf(hashCode65), k -> new ArrayList());
                hashCode64List.add(hashCode64);
            }
            final int[] hashCode32Keys = new int[map.size()];
            int off = 0;
            for (final Integer key : map.keySet()) {
                hashCode32Keys[off++] = key;
            }
            Arrays.sort(hashCode32Keys);
            mw.visitVarInsn(22, 1);
            mw.visitVarInsn(22, 1);
            mw.visitVarInsn(16, 32);
            mw.visitInsn(125);
            mw.visitInsn(131);
            mw.visitInsn(136);
            mw.visitVarInsn(54, 3);
            final Label dflt = new Label();
            final Label[] labels = new Label[hashCode32Keys.length];
            for (int j = 0; j < labels.length; ++j) {
                labels[j] = new Label();
            }
            mw.visitVarInsn(21, 3);
            mw.visitLookupSwitchInsn(dflt, hashCode32Keys, labels);
            for (int j = 0; j < labels.length; ++j) {
                mw.visitLabel(labels[j]);
                final int hashCode66 = hashCode32Keys[j];
                final List<Long> hashCode64Array = map.get(hashCode66);
                for (final long hashCode67 : hashCode64Array) {
                    mw.visitVarInsn(22, 1);
                    mw.visitLdcInsn(hashCode67);
                    mw.visitInsn(148);
                    mw.visitJumpInsn(154, dflt);
                    final int m = Arrays.binarySearch(objectReaderAdapter.hashCodes, hashCode67);
                    final int index = objectReaderAdapter.mapping[m];
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(index), ASMUtils.DESC_FIELD_READER);
                    mw.visitJumpInsn(167, rtnlt);
                }
                mw.visitJumpInsn(167, dflt);
            }
            mw.visitLabel(dflt);
        }
        else {
            for (int k = 0; k < fieldReaderArray.length; ++k) {
                final Label next_ = new Label();
                final Label get_ = new Label();
                final String fieldName = fieldReaderArray[k].fieldName;
                final long hashCode68 = fieldReaderArray[k].fieldNameHash;
                mw.visitVarInsn(22, 1);
                mw.visitLdcInsn(hashCode68);
                mw.visitInsn(148);
                mw.visitJumpInsn(154, next_);
                mw.visitLabel(get_);
                mw.visitVarInsn(25, 0);
                mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(k), ASMUtils.DESC_FIELD_READER);
                mw.visitJumpInsn(167, rtnlt);
                mw.visitLabel(next_);
            }
        }
        mw.visitInsn(1);
        mw.visitInsn(176);
        mw.visitLabel(rtnlt);
        mw.visitInsn(176);
        mw.visitMaxs(5, 5);
    }
    
    private void genMethodGetFieldReaderLCase(final FieldReader[] fieldReaderArray, final ClassWriter cw, final String classNameType, final ObjectReaderAdapter objectReaderAdapter) {
        final MethodWriter mw = cw.visitMethod(1, "getFieldReaderLCase", "(J)" + ASMUtils.DESC_FIELD_READER, 512);
        final int HASH_CODE_64 = 1;
        final int HASH_CODE_65 = 3;
        final Label rtnlt = new Label();
        if (fieldReaderArray.length > 6) {
            final Map<Integer, List<Long>> map = new TreeMap<Integer, List<Long>>();
            for (int i = 0; i < objectReaderAdapter.hashCodesLCase.length; ++i) {
                final long hashCode64 = objectReaderAdapter.hashCodesLCase[i];
                final int hashCode65 = (int)(hashCode64 ^ hashCode64 >>> 32);
                final List<Long> hashCode64List = map.computeIfAbsent(Integer.valueOf(hashCode65), k -> new ArrayList());
                hashCode64List.add(hashCode64);
            }
            final int[] hashCode32Keys = new int[map.size()];
            int off = 0;
            for (final Integer key : map.keySet()) {
                hashCode32Keys[off++] = key;
            }
            Arrays.sort(hashCode32Keys);
            mw.visitVarInsn(22, 1);
            mw.visitVarInsn(22, 1);
            mw.visitVarInsn(16, 32);
            mw.visitInsn(125);
            mw.visitInsn(131);
            mw.visitInsn(136);
            mw.visitVarInsn(54, 3);
            final Label dflt = new Label();
            final Label[] labels = new Label[hashCode32Keys.length];
            for (int j = 0; j < labels.length; ++j) {
                labels[j] = new Label();
            }
            mw.visitVarInsn(21, 3);
            mw.visitLookupSwitchInsn(dflt, hashCode32Keys, labels);
            for (int j = 0; j < labels.length; ++j) {
                mw.visitLabel(labels[j]);
                final int hashCode66 = hashCode32Keys[j];
                final List<Long> hashCode64Array = map.get(hashCode66);
                for (final long hashCode67 : hashCode64Array) {
                    mw.visitVarInsn(22, 1);
                    mw.visitLdcInsn(hashCode67);
                    mw.visitInsn(148);
                    mw.visitJumpInsn(154, dflt);
                    final int m = Arrays.binarySearch(objectReaderAdapter.hashCodesLCase, hashCode67);
                    final int index = objectReaderAdapter.mappingLCase[m];
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(index), ASMUtils.DESC_FIELD_READER);
                    mw.visitJumpInsn(167, rtnlt);
                }
                mw.visitJumpInsn(167, dflt);
            }
            mw.visitLabel(dflt);
        }
        else {
            for (int k = 0; k < fieldReaderArray.length; ++k) {
                final Label next_ = new Label();
                final Label get_ = new Label();
                final String fieldName = fieldReaderArray[k].fieldName;
                final long hashCode68 = fieldReaderArray[k].fieldNameHashLCase;
                mw.visitVarInsn(22, 1);
                mw.visitLdcInsn(hashCode68);
                mw.visitInsn(148);
                mw.visitJumpInsn(154, next_);
                mw.visitLabel(get_);
                mw.visitVarInsn(25, 0);
                mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(k), ASMUtils.DESC_FIELD_READER);
                mw.visitJumpInsn(167, rtnlt);
                mw.visitLabel(next_);
            }
        }
        mw.visitInsn(1);
        mw.visitInsn(176);
        mw.visitLabel(rtnlt);
        mw.visitInsn(176);
        mw.visitMaxs(5, 5);
    }
    
    private void genInitFields(final FieldReader[] fieldReaderArray, final String classNameType, final boolean generatedFields, final int THIS, final int FIELD_READER_ARRAY, final MethodWriter mw, final String objectReaderSuper) {
        if (objectReaderSuper != ASMUtils.TYPE_OBJECT_READER_ADAPTER || !generatedFields) {
            return;
        }
        for (int i = 0; i < fieldReaderArray.length; ++i) {
            mw.visitVarInsn(25, THIS);
            mw.visitVarInsn(25, FIELD_READER_ARRAY);
            switch (i) {
                case 0: {
                    mw.visitInsn(3);
                    break;
                }
                case 1: {
                    mw.visitInsn(4);
                    break;
                }
                case 2: {
                    mw.visitInsn(5);
                    break;
                }
                case 3: {
                    mw.visitInsn(6);
                    break;
                }
                case 4: {
                    mw.visitInsn(7);
                    break;
                }
                case 5: {
                    mw.visitInsn(8);
                    break;
                }
                default: {
                    if (i >= 128) {
                        mw.visitIntInsn(17, i);
                        break;
                    }
                    mw.visitIntInsn(16, i);
                    break;
                }
            }
            mw.visitInsn(50);
            mw.visitFieldInsn(181, classNameType, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
        }
    }
    
    private void genFields(final FieldReader[] fieldReaderArray, final ClassWriter cw, final String objectReaderSuper) {
        if (objectReaderSuper == ASMUtils.TYPE_OBJECT_READER_ADAPTER) {
            for (int i = 0; i < fieldReaderArray.length; ++i) {
                cw.visitField(1, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
            }
            for (int i = 0; i < fieldReaderArray.length; ++i) {
                cw.visitField(1, fieldObjectReader(i), ASMUtils.DESC_OBJECT_READER);
            }
        }
        for (int i = 0; i < fieldReaderArray.length; ++i) {
            final Class fieldClass = fieldReaderArray[i].fieldClass;
            if (List.class.isAssignableFrom(fieldClass)) {
                cw.visitField(1, fieldItemObjectReader(i), ASMUtils.DESC_OBJECT_READER);
            }
        }
    }
    
    private <T> void genMethodReadJSONBObject(final ObjectWriteContext context, final Constructor defaultConstructor, final long readerFeatures, final String TYPE_OBJECT, final FieldReader[] fieldReaderArray, final ClassWriter cw, final String classNameType, final ObjectReaderAdapter objectReaderAdapter) {
        final Class objectClass = context.objectClass;
        final boolean fieldBased = (readerFeatures & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final MethodWriter mw = cw.visitMethod(1, "readJSONBObject", ObjectReaderCreatorASM.METHOD_DESC_READ_OBJECT, 2048);
        final int JSON_READER = 1;
        final int FIELD_TYPE = 2;
        final int FIELD_NAME = 3;
        final int FEATURES = 4;
        final int OBJECT = 6;
        final int ENTRY_CNT = 7;
        final int I = 8;
        final int HASH_CODE64 = 9;
        final int HASH_CODE_32 = 11;
        final int ITEM_CNT = 12;
        final int J = 13;
        final int FIELD_READER = 14;
        final int AUTO_TYPE_OBJECT_READER = 15;
        this.genCheckAutoType(classNameType, mw, 1, 2, 3, 4, 15);
        int varIndex = 16;
        final Map<Object, Integer> variants = new HashMap<Object, Integer>();
        final Label notNull_ = new Label();
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfNull", "()Z", false);
        mw.visitJumpInsn(153, notNull_);
        mw.visitInsn(1);
        mw.visitInsn(176);
        mw.visitLabel(notNull_);
        if (objectClass != null && !Serializable.class.isAssignableFrom(objectClass)) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, classNameType, "objectClass", "Ljava/lang/Class;");
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "errorOnNoneSerializable", "(Ljava/lang/Class;)V", false);
        }
        final Label object_ = new Label();
        final Label startArray_ = new Label();
        final Label endArray_ = new Label();
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isArray", "()Z", false);
        mw.visitJumpInsn(153, object_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isSupportBeanArray", "()Z", false);
        mw.visitJumpInsn(153, endArray_);
        mw.visitLabel(startArray_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "startArray", "()I", false);
        mw.visitVarInsn(54, 7);
        this.genCreateObject(mw, context, classNameType, TYPE_OBJECT, 4, fieldBased, defaultConstructor, objectReaderAdapter.creator);
        mw.visitVarInsn(58, 6);
        final Label fieldEnd_ = new Label();
        for (int i = 0; i < fieldReaderArray.length; ++i) {
            mw.visitVarInsn(21, 7);
            mw.visitLdcInsn(i + 1);
            mw.visitJumpInsn(161, fieldEnd_);
            final FieldReader fieldReader = fieldReaderArray[i];
            varIndex = this.genReadFieldValue(context, fieldReader, fieldBased, classNameType, mw, 0, 1, 6, 4, varIndex, variants, 12, 13, i, true, TYPE_OBJECT);
        }
        skipRest(mw, 1, fieldReaderArray.length, 7, 13, fieldEnd_);
        mw.visitLabel(fieldEnd_);
        mw.visitVarInsn(25, 6);
        mw.visitInsn(176);
        mw.visitLabel(endArray_);
        mw.visitLabel(object_);
        this.genCreateObject(mw, context, classNameType, TYPE_OBJECT, 4, fieldBased, defaultConstructor, objectReaderAdapter.creator);
        mw.visitVarInsn(58, 6);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfObjectStart", "()Z", false);
        mw.visitInsn(87);
        this.genCreateObject(mw, context, classNameType, TYPE_OBJECT, 4, fieldBased, defaultConstructor, objectReaderAdapter.creator);
        mw.visitVarInsn(58, 6);
        final Label for_start_i_ = new Label();
        final Label for_end_i_ = new Label();
        final Label for_inc_i_ = new Label();
        mw.visitInsn(3);
        mw.visitVarInsn(54, 8);
        mw.visitLabel(for_start_i_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfObjectEnd", "()Z", false);
        mw.visitJumpInsn(154, for_end_i_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readFieldNameHashCode", "()J", false);
        mw.visitInsn(92);
        mw.visitVarInsn(55, 9);
        mw.visitInsn(9);
        mw.visitInsn(148);
        mw.visitJumpInsn(153, for_inc_i_);
        final Label endAutoType_ = new Label();
        mw.visitVarInsn(22, 9);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, classNameType, "typeKeyHashCode", "J");
        mw.visitInsn(148);
        mw.visitJumpInsn(154, endAutoType_);
        mw.visitVarInsn(22, 9);
        mw.visitInsn(9);
        mw.visitInsn(148);
        mw.visitJumpInsn(153, endAutoType_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, classNameType, "autoType", "(" + ASMUtils.DESC_JSON_READER + ")Ljava/lang/Object;", false);
        mw.visitVarInsn(58, 6);
        mw.visitJumpInsn(167, for_end_i_);
        mw.visitLabel(endAutoType_);
        if (fieldReaderArray.length > 6) {
            final Map<Integer, List<Long>> map = new TreeMap<Integer, List<Long>>();
            for (int j = 0; j < objectReaderAdapter.hashCodes.length; ++j) {
                final long hashCode64 = objectReaderAdapter.hashCodes[j];
                final int hashCode65 = (int)(hashCode64 ^ hashCode64 >>> 32);
                final List<Long> hashCode64List = map.computeIfAbsent(Integer.valueOf(hashCode65), k -> new ArrayList());
                hashCode64List.add(hashCode64);
            }
            final int[] hashCode32Keys = new int[map.size()];
            int off = 0;
            for (final Integer key : map.keySet()) {
                hashCode32Keys[off++] = key;
            }
            Arrays.sort(hashCode32Keys);
            mw.visitVarInsn(22, 9);
            mw.visitVarInsn(22, 9);
            mw.visitVarInsn(16, 32);
            mw.visitInsn(125);
            mw.visitInsn(131);
            mw.visitInsn(136);
            mw.visitVarInsn(54, 11);
            final Label dflt = new Label();
            final Label[] labels = new Label[hashCode32Keys.length];
            for (int k = 0; k < labels.length; ++k) {
                labels[k] = new Label();
            }
            mw.visitVarInsn(21, 11);
            mw.visitLookupSwitchInsn(dflt, hashCode32Keys, labels);
            for (int k = 0; k < labels.length; ++k) {
                mw.visitLabel(labels[k]);
                final int hashCode66 = hashCode32Keys[k];
                final List<Long> hashCode64Array = map.get(hashCode66);
                for (final long hashCode67 : hashCode64Array) {
                    mw.visitVarInsn(22, 9);
                    mw.visitLdcInsn(hashCode67);
                    mw.visitInsn(148);
                    mw.visitJumpInsn(154, dflt);
                    final int m = Arrays.binarySearch(objectReaderAdapter.hashCodes, hashCode67);
                    final int index = objectReaderAdapter.mapping[m];
                    final FieldReader fieldReader2 = fieldReaderArray[index];
                    varIndex = this.genReadFieldValue(context, fieldReader2, fieldBased, classNameType, mw, 0, 1, 6, 4, varIndex, variants, 12, 13, index, true, TYPE_OBJECT);
                    mw.visitJumpInsn(167, for_inc_i_);
                }
                mw.visitJumpInsn(167, for_inc_i_);
            }
            mw.visitLabel(dflt);
            final Label fieldReaderNull_ = new Label();
            if ((readerFeatures & JSONReader.Feature.SupportSmartMatch.mask) == 0x0L) {
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(22, 4);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isSupportSmartMatch", "(J)Z", false);
                mw.visitJumpInsn(153, fieldReaderNull_);
            }
            mw.visitVarInsn(25, 0);
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "getNameHashCodeLCase", "()J", false);
            mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_READER, "getFieldReaderLCase", ObjectReaderCreatorASM.METHOD_DESC_GET_FIELD_READER, true);
            mw.visitInsn(89);
            mw.visitVarInsn(58, 14);
            mw.visitJumpInsn(198, fieldReaderNull_);
            mw.visitVarInsn(25, 14);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, 6);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_READE, "readFieldValueJSONB", ObjectReaderCreatorASM.METHOD_DESC_READ_FIELD_VALUE, false);
            mw.visitJumpInsn(167, for_inc_i_);
            mw.visitLabel(fieldReaderNull_);
        }
        else {
            for (int l = 0; l < fieldReaderArray.length; ++l) {
                final Label next_ = new Label();
                final FieldReader fieldReader3 = fieldReaderArray[l];
                final long hashCode68 = Fnv.hashCode64(fieldReader3.fieldName);
                mw.visitVarInsn(22, 9);
                mw.visitLdcInsn(hashCode68);
                mw.visitInsn(148);
                mw.visitJumpInsn(154, next_);
                varIndex = this.genReadFieldValue(context, fieldReader3, fieldBased, classNameType, mw, 0, 1, 6, 4, varIndex, variants, 12, 13, l, true, TYPE_OBJECT);
                mw.visitJumpInsn(167, for_inc_i_);
                mw.visitLabel(next_);
            }
            final Label processExtra_ = new Label();
            if ((readerFeatures & JSONReader.Feature.SupportSmartMatch.mask) == 0x0L) {
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(22, 4);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isSupportSmartMatch", "(J)Z", false);
                mw.visitJumpInsn(153, processExtra_);
            }
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "getNameHashCodeLCase", "()J", false);
            mw.visitVarInsn(55, 9);
            for (int j = 0; j < fieldReaderArray.length; ++j) {
                final Label next_2 = new Label();
                final FieldReader fieldReader4 = fieldReaderArray[j];
                final long hashCode69 = Fnv.hashCode64(fieldReader4.fieldName);
                mw.visitVarInsn(22, 9);
                mw.visitLdcInsn(hashCode69);
                mw.visitInsn(148);
                mw.visitJumpInsn(154, next_2);
                varIndex = this.genReadFieldValue(context, fieldReader4, fieldBased, classNameType, mw, 0, 1, 6, 4, varIndex, variants, 12, 13, j, true, TYPE_OBJECT);
                mw.visitJumpInsn(167, for_inc_i_);
                mw.visitLabel(next_2);
            }
            mw.visitLabel(processExtra_);
        }
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 6);
        mw.visitMethodInsn(182, ASMUtils.TYPE_OBJECT_READER_ADAPTER, "processExtra", ObjectReaderCreatorASM.METHOD_DESC_PROCESS_EXTRA, false);
        mw.visitJumpInsn(167, for_inc_i_);
        mw.visitLabel(for_inc_i_);
        mw.visitIincInsn(8, 1);
        mw.visitJumpInsn(167, for_start_i_);
        mw.visitLabel(for_end_i_);
        mw.visitVarInsn(25, 6);
        mw.visitInsn(176);
        mw.visitMaxs(5, 10);
    }
    
    private static void skipRest(final MethodWriter mw, final int JSON_READER, final int start, final int ENTRY_CNT, final int J, final Label label) {
        final Label for_start_j_ = new Label();
        final Label for_inc_j_ = new Label();
        mw.visitLdcInsn(start);
        mw.visitVarInsn(54, J);
        mw.visitLabel(for_start_j_);
        mw.visitVarInsn(21, J);
        mw.visitVarInsn(21, ENTRY_CNT);
        mw.visitJumpInsn(162, label);
        mw.visitVarInsn(25, JSON_READER);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "skipValue", "()V", false);
        mw.visitLabel(for_inc_j_);
        mw.visitIincInsn(J, 1);
        mw.visitJumpInsn(167, for_start_j_);
    }
    
    private <T> void genMethodReadJSONBObjectArrayMapping(final ObjectWriteContext context, final Constructor defaultConstructor, final long readerFeatures, final String TYPE_OBJECT, final FieldReader[] fieldReaderArray, final ClassWriter cw, final String classNameType, final ObjectReaderAdapter objectReaderAdapter) {
        final boolean fieldBased = (readerFeatures & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final MethodWriter mw = cw.visitMethod(1, "readArrayMappingJSONBObject", ObjectReaderCreatorASM.METHOD_DESC_READ_OBJECT, 512);
        final int JSON_READER = 1;
        final int FIELD_TYPE = 2;
        final int FIELD_NAME = 3;
        final int FEATURES = 4;
        final int OBJECT = 6;
        final int ENTRY_CNT = 7;
        final int ITEM_CNT = 8;
        final int J = 9;
        final int AUTO_TYPE_OBJECT_READER = 10;
        this.genCheckAutoType(classNameType, mw, 1, 2, 3, 4, 10);
        int varIndex = 11;
        final Map<Object, Integer> variants = new HashMap<Object, Integer>();
        final Label notNull_ = new Label();
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfNull", "()Z", false);
        mw.visitJumpInsn(153, notNull_);
        mw.visitInsn(1);
        mw.visitInsn(176);
        mw.visitLabel(notNull_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "startArray", "()I", false);
        mw.visitVarInsn(54, 7);
        this.genCreateObject(mw, context, classNameType, TYPE_OBJECT, 4, fieldBased, defaultConstructor, objectReaderAdapter.creator);
        mw.visitVarInsn(58, 6);
        final Label fieldEnd_ = new Label();
        for (int i = 0; i < fieldReaderArray.length; ++i) {
            mw.visitVarInsn(21, 7);
            mw.visitLdcInsn(i + 1);
            mw.visitJumpInsn(161, fieldEnd_);
            final FieldReader fieldReader = fieldReaderArray[i];
            varIndex = this.genReadFieldValue(context, fieldReader, fieldBased, classNameType, mw, 0, 1, 6, 4, varIndex, variants, 8, 9, i, true, TYPE_OBJECT);
        }
        skipRest(mw, 1, fieldReaderArray.length, 7, 9, fieldEnd_);
        mw.visitLabel(fieldEnd_);
        mw.visitVarInsn(25, 6);
        mw.visitInsn(176);
        mw.visitMaxs(5, 10);
    }
    
    private void genCheckAutoType(final String classNameType, final MethodWriter mw, final int JSON_READER, final int FIELD_TYPE, final int FIELD_NAME, final int FEATURES, final int AUTO_TYPE_OBJECT_READER) {
        final Label checkArrayAutoTypeNull_ = new Label();
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, JSON_READER);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, classNameType, "objectClass", "Ljava/lang/Class;");
        mw.visitVarInsn(22, FEATURES);
        mw.visitMethodInsn(182, classNameType, "checkAutoType", ObjectReaderCreatorASM.METHOD_DESC_JSON_READER_CHECK_ARRAY_AUTO_TYPE, false);
        mw.visitInsn(89);
        mw.visitVarInsn(58, AUTO_TYPE_OBJECT_READER);
        mw.visitJumpInsn(198, checkArrayAutoTypeNull_);
        mw.visitVarInsn(25, AUTO_TYPE_OBJECT_READER);
        mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_READER, "getObjectClass", "()Ljava/lang/Class;", true);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, classNameType, "objectClass", "Ljava/lang/Class;");
        mw.visitJumpInsn(165, checkArrayAutoTypeNull_);
        mw.visitVarInsn(25, AUTO_TYPE_OBJECT_READER);
        mw.visitVarInsn(25, JSON_READER);
        mw.visitVarInsn(25, FIELD_TYPE);
        mw.visitVarInsn(25, FIELD_NAME);
        mw.visitVarInsn(22, FEATURES);
        mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_READER, "readJSONBObject", ObjectReaderCreatorASM.METHOD_DESC_READ_OBJECT, true);
        mw.visitInsn(176);
        mw.visitLabel(checkArrayAutoTypeNull_);
    }
    
    private <T> void genMethodReadObject(final ObjectWriteContext context, final Constructor defaultConstructor, final long readerFeatures, final String TYPE_OBJECT, final FieldReader[] fieldReaderArray, final ClassWriter cw, final String classNameType, final ObjectReaderAdapter objectReaderAdapter) {
        final boolean fieldBased = (readerFeatures & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final MethodWriter mw = cw.visitMethod(1, "readObject", ObjectReaderCreatorASM.METHOD_DESC_READ_OBJECT, 2048);
        final int JSON_READER = 1;
        final int FIELD_TYPE = 2;
        final int FIELD_NAME = 3;
        final int FEATURES = 4;
        final int OBJECT = 6;
        final int I = 7;
        final int HASH_CODE64 = 8;
        final int HASH_CODE_32 = 10;
        final int ITEM_CNT = 11;
        final int J = 12;
        final int FIELD_READER = 13;
        int varIndex = 14;
        final Map<Object, Integer> variants = new HashMap<Object, Integer>();
        final Label json_ = new Label();
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 1);
        mw.visitFieldInsn(180, ASMUtils.TYPE_JSON_READER, "jsonb", "Z");
        mw.visitJumpInsn(153, json_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitVarInsn(25, 3);
        mw.visitVarInsn(22, 4);
        mw.visitMethodInsn(182, classNameType, "readJSONBObject", ObjectReaderCreatorASM.METHOD_DESC_READ_OBJECT, false);
        mw.visitInsn(176);
        mw.visitLabel(json_);
        final Label object_ = new Label();
        final Label singleItemArray_ = new Label();
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isArray", "()Z", false);
        mw.visitJumpInsn(153, object_);
        if ((readerFeatures & JSONReader.Feature.SupportArrayToBean.mask) == 0x0L) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(22, 4);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isSupportBeanArray", "(J)Z", false);
            mw.visitJumpInsn(153, singleItemArray_);
        }
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfArrayStart", "()Z", false);
        this.genCreateObject(mw, context, classNameType, TYPE_OBJECT, 4, fieldBased, defaultConstructor, objectReaderAdapter.creator);
        mw.visitVarInsn(58, 6);
        int fieldNameLengthMin = 0;
        int fieldNameLengthMax = 0;
        for (int i = 0; i < fieldReaderArray.length; ++i) {
            final FieldReader fieldReader = fieldReaderArray[i];
            final int fieldNameLength = fieldReader.fieldName.getBytes(StandardCharsets.UTF_8).length;
            if (i == 0) {
                fieldNameLengthMin = fieldNameLength;
                fieldNameLengthMax = fieldNameLength;
            }
            else {
                fieldNameLengthMin = Math.min(fieldNameLength, fieldNameLengthMin);
                fieldNameLengthMax = Math.max(fieldNameLength, fieldNameLengthMax);
            }
            varIndex = this.genReadFieldValue(context, fieldReader, fieldBased, classNameType, mw, 0, 1, 6, 4, varIndex, variants, 11, 12, i, false, TYPE_OBJECT);
        }
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfArrayEnd", "()Z", false);
        mw.visitInsn(87);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfComma", "()Z", false);
        mw.visitInsn(87);
        mw.visitVarInsn(25, 6);
        mw.visitInsn(176);
        mw.visitLabel(singleItemArray_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitVarInsn(25, 3);
        mw.visitVarInsn(22, 4);
        mw.visitMethodInsn(182, classNameType, "processObjectInputSingleItemArray", ObjectReaderCreatorASM.METHOD_DESC_READ_OBJECT, false);
        mw.visitInsn(176);
        mw.visitLabel(object_);
        final Label notNull_ = new Label();
        final Label end_ = new Label();
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfObjectStart", "()Z", false);
        mw.visitJumpInsn(154, notNull_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfNullOrEmptyString", "()Z", false);
        mw.visitJumpInsn(153, notNull_);
        mw.visitInsn(1);
        mw.visitVarInsn(58, 6);
        mw.visitJumpInsn(167, end_);
        mw.visitLabel(notNull_);
        this.genCreateObject(mw, context, classNameType, TYPE_OBJECT, 4, fieldBased, defaultConstructor, objectReaderAdapter.creator);
        mw.visitVarInsn(58, 6);
        final Label for_start_i_ = new Label();
        final Label for_end_i_ = new Label();
        final Label for_inc_i_ = new Label();
        mw.visitInsn(3);
        mw.visitVarInsn(54, 7);
        mw.visitLabel(for_start_i_);
        final Label hashCode64Start = new Label();
        final Label hashCode64End = new Label();
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfObjectEnd", "()Z", false);
        mw.visitJumpInsn(154, for_end_i_);
        if (fieldNameLengthMin >= 5 && fieldNameLengthMax <= 7) {
            varIndex = this.genRead57(context, TYPE_OBJECT, fieldReaderArray, classNameType, fieldBased, mw, 1, 4, 6, 11, 12, varIndex, variants, for_inc_i_, hashCode64Start);
        }
        else if (fieldNameLengthMin >= 2 && fieldNameLengthMax <= 43) {
            varIndex = this.genRead243(context, TYPE_OBJECT, fieldReaderArray, classNameType, fieldBased, mw, 1, 4, 6, 11, 12, varIndex, variants, for_inc_i_, hashCode64Start);
        }
        mw.visitLabel(hashCode64Start);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readFieldNameHashCode", "()J", false);
        mw.visitInsn(92);
        mw.visitVarInsn(55, 8);
        mw.visitLdcInsn(-1L);
        mw.visitInsn(148);
        mw.visitJumpInsn(153, for_end_i_);
        mw.visitLabel(hashCode64End);
        final Label noneAutoType_ = new Label();
        mw.visitVarInsn(21, 7);
        mw.visitJumpInsn(154, noneAutoType_);
        mw.visitVarInsn(22, 8);
        mw.visitLdcInsn(ObjectReader.HASH_TYPE);
        mw.visitInsn(148);
        mw.visitJumpInsn(154, noneAutoType_);
        if ((readerFeatures & JSONReader.Feature.SupportAutoType.mask) == 0x0L) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(22, 4);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isSupportAutoTypeOrHandler", "(J)Z", false);
            mw.visitJumpInsn(153, noneAutoType_);
        }
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, classNameType, "objectClass", "Ljava/lang/Class;");
        mw.visitVarInsn(22, 4);
        mw.visitMethodInsn(182, ASMUtils.TYPE_OBJECT_READER_ADAPTER, "auoType", "(" + ASMUtils.desc(JSONReader.class) + "Ljava/lang/Class;J)Ljava/lang/Object;", false);
        mw.visitInsn(176);
        mw.visitLabel(noneAutoType_);
        if (fieldReaderArray.length > 6) {
            final Map<Integer, List<Long>> map = new TreeMap<Integer, List<Long>>();
            for (int j = 0; j < objectReaderAdapter.hashCodes.length; ++j) {
                final long hashCode64 = objectReaderAdapter.hashCodes[j];
                final int hashCode65 = (int)(hashCode64 ^ hashCode64 >>> 32);
                final List<Long> hashCode64List = map.computeIfAbsent(Integer.valueOf(hashCode65), k -> new ArrayList());
                hashCode64List.add(hashCode64);
            }
            final int[] hashCode32Keys = new int[map.size()];
            int off = 0;
            for (final Integer key : map.keySet()) {
                hashCode32Keys[off++] = key;
            }
            Arrays.sort(hashCode32Keys);
            mw.visitVarInsn(22, 8);
            mw.visitVarInsn(22, 8);
            mw.visitVarInsn(16, 32);
            mw.visitInsn(125);
            mw.visitInsn(131);
            mw.visitInsn(136);
            mw.visitVarInsn(54, 10);
            final Label dflt = new Label();
            final Label[] labels = new Label[hashCode32Keys.length];
            for (int k = 0; k < labels.length; ++k) {
                labels[k] = new Label();
            }
            mw.visitVarInsn(21, 10);
            mw.visitLookupSwitchInsn(dflt, hashCode32Keys, labels);
            for (int k = 0; k < labels.length; ++k) {
                mw.visitLabel(labels[k]);
                final int hashCode66 = hashCode32Keys[k];
                final List<Long> hashCode64Array = map.get(hashCode66);
                for (final long hashCode67 : hashCode64Array) {
                    mw.visitVarInsn(22, 8);
                    mw.visitLdcInsn(hashCode67);
                    mw.visitInsn(148);
                    mw.visitJumpInsn(154, dflt);
                    final int m = Arrays.binarySearch(objectReaderAdapter.hashCodes, hashCode67);
                    final int index = objectReaderAdapter.mapping[m];
                    final FieldReader fieldReader2 = fieldReaderArray[index];
                    varIndex = this.genReadFieldValue(context, fieldReader2, fieldBased, classNameType, mw, 0, 1, 6, 4, varIndex, variants, 11, 12, index, false, TYPE_OBJECT);
                    mw.visitJumpInsn(167, for_inc_i_);
                }
                mw.visitJumpInsn(167, for_inc_i_);
            }
            mw.visitLabel(dflt);
            final Label fieldReaderNull_ = new Label();
            if ((readerFeatures & JSONReader.Feature.SupportSmartMatch.mask) == 0x0L) {
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(22, 4);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isSupportSmartMatch", "(J)Z", false);
                mw.visitJumpInsn(153, fieldReaderNull_);
            }
            mw.visitVarInsn(25, 0);
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "getNameHashCodeLCase", "()J", false);
            mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_READER, "getFieldReaderLCase", ObjectReaderCreatorASM.METHOD_DESC_GET_FIELD_READER, true);
            mw.visitInsn(89);
            mw.visitVarInsn(58, 13);
            mw.visitJumpInsn(198, fieldReaderNull_);
            mw.visitVarInsn(25, 13);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, 6);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_READE, "readFieldValue", ObjectReaderCreatorASM.METHOD_DESC_READ_FIELD_VALUE, false);
            mw.visitJumpInsn(167, for_inc_i_);
            mw.visitLabel(fieldReaderNull_);
        }
        else {
            for (int l = 0; l < fieldReaderArray.length; ++l) {
                final Label next_ = new Label();
                final Label get_ = new Label();
                final FieldReader fieldReader3 = fieldReaderArray[l];
                final String fieldName = fieldReader3.fieldName;
                final long hashCode68 = fieldReader3.fieldNameHash;
                mw.visitVarInsn(22, 8);
                mw.visitLdcInsn(hashCode68);
                mw.visitInsn(148);
                mw.visitJumpInsn(154, next_);
                mw.visitLabel(get_);
                varIndex = this.genReadFieldValue(context, fieldReader3, fieldBased, classNameType, mw, 0, 1, 6, 4, varIndex, variants, 11, 12, l, false, TYPE_OBJECT);
                mw.visitJumpInsn(167, for_inc_i_);
                mw.visitLabel(next_);
            }
            final Label processExtra_ = new Label();
            if ((readerFeatures & JSONReader.Feature.SupportSmartMatch.mask) == 0x0L) {
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(22, 4);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isSupportSmartMatch", "(J)Z", false);
                mw.visitJumpInsn(153, processExtra_);
            }
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "getNameHashCodeLCase", "()J", false);
            mw.visitVarInsn(55, 8);
            for (int j = 0; j < fieldReaderArray.length; ++j) {
                final Label next_2 = new Label();
                final Label get_2 = new Label();
                final FieldReader fieldReader4 = fieldReaderArray[j];
                final String fieldName2 = fieldReader4.fieldName;
                final long hashCode69 = fieldReader4.fieldNameHash;
                final long hashCode64LCase = fieldReader4.fieldNameHashLCase;
                mw.visitVarInsn(22, 8);
                mw.visitLdcInsn(hashCode69);
                mw.visitInsn(148);
                mw.visitJumpInsn(153, get_2);
                if (hashCode64LCase != hashCode69) {
                    mw.visitVarInsn(22, 8);
                    mw.visitLdcInsn(hashCode64LCase);
                    mw.visitInsn(148);
                    mw.visitJumpInsn(154, next_2);
                }
                else {
                    mw.visitJumpInsn(167, next_2);
                }
                mw.visitLabel(get_2);
                varIndex = this.genReadFieldValue(context, fieldReader4, fieldBased, classNameType, mw, 0, 1, 6, 4, varIndex, variants, 11, 12, j, false, TYPE_OBJECT);
                mw.visitJumpInsn(167, for_inc_i_);
                mw.visitLabel(next_2);
            }
            mw.visitLabel(processExtra_);
        }
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 6);
        mw.visitMethodInsn(182, ASMUtils.TYPE_OBJECT_READER_ADAPTER, "processExtra", ObjectReaderCreatorASM.METHOD_DESC_PROCESS_EXTRA, false);
        mw.visitJumpInsn(167, for_inc_i_);
        mw.visitLabel(for_inc_i_);
        mw.visitIincInsn(7, 1);
        mw.visitJumpInsn(167, for_start_i_);
        mw.visitLabel(for_end_i_);
        mw.visitLabel(end_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfComma", "()Z", false);
        mw.visitInsn(87);
        mw.visitVarInsn(25, 6);
        mw.visitInsn(176);
        mw.visitMaxs(5, 10);
    }
    
    private int genRead243(final ObjectWriteContext context, final String TYPE_OBJECT, final FieldReader[] fieldReaderArray, final String classNameType, final boolean fieldBased, final MethodWriter mw, final int JSON_READER, final int FEATURES, final int OBJECT, final int ITEM_CNT, final int J, int varIndex, final Map<Object, Integer> variants, final Label for_inc_i_, final Label hashCode64Start) {
        final IdentityHashMap<FieldReader, Integer> readerIndexMap = new IdentityHashMap<FieldReader, Integer>();
        final Map<Integer, List<FieldReader>> name0Map = new TreeMap<Integer, List<FieldReader>>();
        for (int i = 0; i < fieldReaderArray.length; ++i) {
            final FieldReader fieldReader = fieldReaderArray[i];
            readerIndexMap.put(fieldReader, i);
            final byte[] fieldName = fieldReader.fieldName.getBytes(StandardCharsets.UTF_8);
            final byte[] name0Bytes = new byte[4];
            name0Bytes[0] = 34;
            if (fieldName.length == 2) {
                System.arraycopy(fieldName, 0, name0Bytes, 1, 2);
                name0Bytes[3] = 34;
            }
            else {
                System.arraycopy(fieldName, 0, name0Bytes, 1, 3);
            }
            final int name0 = JDKUtils.UNSAFE.getInt(name0Bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
            List<FieldReader> fieldReaders = name0Map.get(name0);
            if (fieldReaders == null) {
                fieldReaders = new ArrayList<FieldReader>();
                name0Map.put(name0, fieldReaders);
            }
            fieldReaders.add(fieldReader);
        }
        final Label dflt = new Label();
        final int[] switchKeys = new int[name0Map.size()];
        final Label[] labels = new Label[name0Map.size()];
        final Iterator it = name0Map.keySet().iterator();
        for (int j = 0; j < labels.length; ++j) {
            labels[j] = new Label();
            switchKeys[j] = it.next();
        }
        mw.visitVarInsn(25, JSON_READER);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "getRawInt", "()I", false);
        mw.visitLookupSwitchInsn(dflt, switchKeys, labels);
        for (int k = 0; k < labels.length; ++k) {
            mw.visitLabel(labels[k]);
            final int name0 = switchKeys[k];
            final List<FieldReader> fieldReaders = name0Map.get(name0);
            for (int l = 0; l < fieldReaders.size(); ++l) {
                Label nextJ = null;
                if (l + 1 != fieldReaders.size()) {
                    nextJ = new Label();
                }
                final FieldReader fieldReader2 = fieldReaders.get(l);
                final int fieldReaderIndex = readerIndexMap.get(fieldReader2);
                final byte[] fieldName2 = fieldReader2.fieldName.getBytes(StandardCharsets.UTF_8);
                final int fieldNameLength = fieldName2.length;
                switch (fieldNameLength) {
                    case 2: {
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match2", "()Z", false);
                        break;
                    }
                    case 3: {
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match3", "()Z", false);
                        break;
                    }
                    case 4: {
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(fieldName2[3]);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match4", "(B)Z", false);
                        break;
                    }
                    case 5: {
                        final byte[] bytes4 = { fieldName2[3], fieldName2[4], 34, 58 };
                        final int name2 = JDKUtils.UNSAFE.getInt(bytes4, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name2);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match5", "(I)Z", false);
                        break;
                    }
                    case 6: {
                        final byte[] bytes4 = { fieldName2[3], fieldName2[4], fieldName2[5], 34 };
                        final int name2 = JDKUtils.UNSAFE.getInt(bytes4, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name2);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match6", "(I)Z", false);
                        break;
                    }
                    case 7: {
                        final int name3 = JDKUtils.UNSAFE.getInt(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name3);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match7", "(I)Z", false);
                        break;
                    }
                    case 8: {
                        final int name3 = JDKUtils.UNSAFE.getInt(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name3);
                        mw.visitLdcInsn(fieldName2[7]);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match8", "(IB)Z", false);
                        break;
                    }
                    case 9: {
                        final byte[] bytes5 = new byte[8];
                        System.arraycopy(fieldName2, 3, bytes5, 0, 6);
                        bytes5[6] = 34;
                        bytes5[7] = 58;
                        final long name4 = JDKUtils.UNSAFE.getLong(bytes5, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name4);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match9", "(J)Z", false);
                        break;
                    }
                    case 10: {
                        final byte[] bytes5 = new byte[8];
                        System.arraycopy(fieldName2, 3, bytes5, 0, 7);
                        bytes5[7] = 34;
                        final long name4 = JDKUtils.UNSAFE.getLong(bytes5, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name4);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match10", "(J)Z", false);
                        break;
                    }
                    case 11: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match11", "(J)Z", false);
                        break;
                    }
                    case 12: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(fieldName2[11]);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match12", "(JB)Z", false);
                        break;
                    }
                    case 13: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final byte[] bytes6 = { fieldName2[11], fieldName2[12], 34, 58 };
                        final int name6 = JDKUtils.UNSAFE.getInt(bytes6, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name6);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match13", "(JI)Z", false);
                        break;
                    }
                    case 14: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final byte[] bytes6 = { fieldName2[11], fieldName2[12], fieldName2[13], 34 };
                        final int name6 = JDKUtils.UNSAFE.getInt(bytes6, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name6);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match14", "(JI)Z", false);
                        break;
                    }
                    case 15: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final int name7 = JDKUtils.UNSAFE.getInt(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name7);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match15", "(JI)Z", false);
                        break;
                    }
                    case 16: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final int name7 = JDKUtils.UNSAFE.getInt(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name7);
                        mw.visitLdcInsn(fieldName2[15]);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match16", "(JIB)Z", false);
                        break;
                    }
                    case 17: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final byte[] bytes7 = new byte[8];
                        System.arraycopy(fieldName2, 11, bytes7, 0, 6);
                        bytes7[6] = 34;
                        bytes7[7] = 58;
                        final long name8 = JDKUtils.UNSAFE.getLong(bytes7, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name8);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match17", "(JJ)Z", false);
                        break;
                    }
                    case 18: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final byte[] bytes7 = new byte[8];
                        System.arraycopy(fieldName2, 11, bytes7, 0, 7);
                        bytes7[7] = 34;
                        final long name8 = JDKUtils.UNSAFE.getLong(bytes7, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name8);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match18", "(JJ)Z", false);
                        break;
                    }
                    case 19: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match19", "(JJ)Z", false);
                        break;
                    }
                    case 20: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(fieldName2[19]);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match20", "(JJB)Z", false);
                        break;
                    }
                    case 21: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final byte[] bytes8 = { fieldName2[19], fieldName2[20], 34, 58 };
                        final int name10 = JDKUtils.UNSAFE.getInt(bytes8, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name10);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match21", "(JJI)Z", false);
                        break;
                    }
                    case 22: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final byte[] bytes8 = { fieldName2[19], fieldName2[20], fieldName2[21], 34 };
                        final int name10 = JDKUtils.UNSAFE.getInt(bytes8, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name10);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match22", "(JJI)Z", false);
                        break;
                    }
                    case 23: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final int name11 = JDKUtils.UNSAFE.getInt(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name11);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match23", "(JJI)Z", false);
                        break;
                    }
                    case 24: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final int name11 = JDKUtils.UNSAFE.getInt(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name11);
                        mw.visitLdcInsn(fieldName2[23]);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match24", "(JJIB)Z", false);
                        break;
                    }
                    case 25: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final byte[] bytes9 = new byte[8];
                        System.arraycopy(fieldName2, 19, bytes9, 0, 6);
                        bytes9[6] = 34;
                        bytes9[7] = 58;
                        final long name12 = JDKUtils.UNSAFE.getLong(bytes9, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name12);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match25", "(JJJ)Z", false);
                        break;
                    }
                    case 26: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final byte[] bytes9 = new byte[8];
                        System.arraycopy(fieldName2, 19, bytes9, 0, 7);
                        bytes9[7] = 34;
                        final long name12 = JDKUtils.UNSAFE.getLong(bytes9, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name12);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match26", "(JJJ)Z", false);
                        break;
                    }
                    case 27: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match27", "(JJJ)Z", false);
                        break;
                    }
                    case 28: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(fieldName2[27]);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match28", "(JJJB)Z", false);
                        break;
                    }
                    case 29: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final byte[] bytes10 = { fieldName2[27], fieldName2[28], 34, 58 };
                        final int name14 = JDKUtils.UNSAFE.getInt(bytes10, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name14);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match29", "(JJJI)Z", false);
                        break;
                    }
                    case 30: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final byte[] bytes10 = { fieldName2[27], fieldName2[28], fieldName2[29], 34 };
                        final int name14 = JDKUtils.UNSAFE.getInt(bytes10, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name14);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match30", "(JJJI)Z", false);
                        break;
                    }
                    case 31: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final int name15 = JDKUtils.UNSAFE.getInt(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 27L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name15);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match31", "(JJJI)Z", false);
                        break;
                    }
                    case 32: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final int name15 = JDKUtils.UNSAFE.getInt(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 27L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name15);
                        mw.visitLdcInsn(fieldName2[31]);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match32", "(JJJIB)Z", false);
                        break;
                    }
                    case 33: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final byte[] bytes11 = new byte[8];
                        System.arraycopy(fieldName2, 27, bytes11, 0, 6);
                        bytes11[6] = 34;
                        bytes11[7] = 58;
                        final long name16 = JDKUtils.UNSAFE.getLong(bytes11, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name16);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match33", "(JJJJ)Z", false);
                        break;
                    }
                    case 34: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final byte[] bytes11 = new byte[8];
                        System.arraycopy(fieldName2, 27, bytes11, 0, 7);
                        bytes11[7] = 34;
                        final long name16 = JDKUtils.UNSAFE.getLong(bytes11, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name16);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match34", "(JJJJ)Z", false);
                        break;
                    }
                    case 35: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final long name17 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 27L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name17);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match35", "(JJJJ)Z", false);
                        break;
                    }
                    case 36: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final long name17 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 27L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name17);
                        mw.visitLdcInsn(fieldName2[35]);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match36", "(JJJJB)Z", false);
                        break;
                    }
                    case 37: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final long name17 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 27L);
                        final byte[] bytes12 = { fieldName2[35], fieldName2[36], 34, 58 };
                        final int name18 = JDKUtils.UNSAFE.getInt(bytes12, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name17);
                        mw.visitLdcInsn(name18);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match37", "(JJJJI)Z", false);
                        break;
                    }
                    case 38: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final long name17 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 27L);
                        final byte[] bytes12 = { fieldName2[35], fieldName2[36], fieldName2[37], 34 };
                        final int name18 = JDKUtils.UNSAFE.getInt(bytes12, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name17);
                        mw.visitLdcInsn(name18);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match38", "(JJJJI)Z", false);
                        break;
                    }
                    case 39: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final long name17 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 27L);
                        final int name19 = JDKUtils.UNSAFE.getInt(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 35L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name17);
                        mw.visitLdcInsn(name19);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match39", "(JJJJI)Z", false);
                        break;
                    }
                    case 40: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final long name17 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 27L);
                        final int name19 = JDKUtils.UNSAFE.getInt(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 35L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name17);
                        mw.visitLdcInsn(name19);
                        mw.visitLdcInsn(fieldName2[39]);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match40", "(JJJJIB)Z", false);
                        break;
                    }
                    case 41: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final long name17 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 27L);
                        final byte[] bytes13 = new byte[8];
                        System.arraycopy(fieldName2, 35, bytes13, 0, 6);
                        bytes13[6] = 34;
                        bytes13[7] = 58;
                        final long name20 = JDKUtils.UNSAFE.getLong(bytes13, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name17);
                        mw.visitLdcInsn(name20);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match41", "(JJJJJ)Z", false);
                        break;
                    }
                    case 42: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final long name17 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 27L);
                        final byte[] bytes13 = new byte[8];
                        System.arraycopy(fieldName2, 35, bytes13, 0, 7);
                        bytes13[7] = 34;
                        final long name20 = JDKUtils.UNSAFE.getLong(bytes13, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name17);
                        mw.visitLdcInsn(name20);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match42", "(JJJJJ)Z", false);
                        break;
                    }
                    case 43: {
                        final long name5 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                        final long name9 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 11L);
                        final long name13 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 19L);
                        final long name17 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 27L);
                        final long name21 = JDKUtils.UNSAFE.getLong(fieldName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 35L);
                        mw.visitVarInsn(25, JSON_READER);
                        mw.visitLdcInsn(name5);
                        mw.visitLdcInsn(name9);
                        mw.visitLdcInsn(name13);
                        mw.visitLdcInsn(name17);
                        mw.visitLdcInsn(name21);
                        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfName4Match43", "(JJJJJ)Z", false);
                        break;
                    }
                    default: {
                        throw new IllegalStateException("fieldNameLength " + fieldNameLength);
                    }
                }
                mw.visitJumpInsn(153, (nextJ != null) ? nextJ : hashCode64Start);
                varIndex = this.genReadFieldValue(context, fieldReader2, fieldBased, classNameType, mw, 0, JSON_READER, OBJECT, FEATURES, varIndex, variants, ITEM_CNT, J, fieldReaderIndex, false, TYPE_OBJECT);
                mw.visitJumpInsn(167, for_inc_i_);
                if (nextJ != null) {
                    mw.visitLabel(nextJ);
                }
            }
            mw.visitJumpInsn(167, dflt);
        }
        mw.visitLabel(dflt);
        return varIndex;
    }
    
    private int genRead57(final ObjectWriteContext context, final String TYPE_OBJECT, final FieldReader[] fieldReaderArray, final String classNameType, final boolean fieldBased, final MethodWriter mw, final int JSON_READER, final int FEATURES, final int OBJECT, final int ITEM_CNT, final int J, int varIndex, final Map<Object, Integer> variants, final Label for_inc_i_, final Label hashCode64Start) {
        Integer RAW_LONG = variants.get("RAW_LONG");
        if (RAW_LONG == null) {
            variants.put("RAW_LONG", RAW_LONG = varIndex);
            varIndex += 2;
        }
        mw.visitVarInsn(25, JSON_READER);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "getRawLong", "()J", false);
        mw.visitInsn(92);
        mw.visitVarInsn(55, RAW_LONG);
        mw.visitInsn(9);
        mw.visitInsn(148);
        mw.visitJumpInsn(153, hashCode64Start);
        for (int i = 0; i < fieldReaderArray.length; ++i) {
            final Label next_ = new Label();
            final FieldReader fieldReader = fieldReaderArray[i];
            final byte[] fieldName = fieldReader.fieldName.getBytes(StandardCharsets.UTF_8);
            final int fieldNameLength = fieldName.length;
            final byte[] bytes8 = new byte[8];
            String nextMethodName = null;
            switch (fieldNameLength) {
                case 5: {
                    bytes8[0] = 34;
                    System.arraycopy(fieldName, 0, bytes8, 1, 5);
                    bytes8[6] = 34;
                    bytes8[7] = 58;
                    nextMethodName = "nextIfName8Match0";
                    break;
                }
                case 6: {
                    bytes8[0] = 34;
                    System.arraycopy(fieldName, 0, bytes8, 1, 6);
                    bytes8[7] = 34;
                    nextMethodName = "nextIfName8Match1";
                    break;
                }
                case 7: {
                    bytes8[0] = 34;
                    System.arraycopy(fieldName, 0, bytes8, 1, 7);
                    nextMethodName = "nextIfName8Match2";
                    break;
                }
                default: {
                    throw new IllegalStateException("length " + fieldNameLength);
                }
            }
            final long rawLong = JDKUtils.UNSAFE.getLong(bytes8, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
            mw.visitVarInsn(22, RAW_LONG);
            mw.visitLdcInsn(rawLong);
            mw.visitInsn(148);
            mw.visitJumpInsn(154, next_);
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, nextMethodName, "()Z", false);
            mw.visitJumpInsn(153, hashCode64Start);
            varIndex = this.genReadFieldValue(context, fieldReader, fieldBased, classNameType, mw, 0, JSON_READER, OBJECT, FEATURES, varIndex, variants, ITEM_CNT, J, i, false, TYPE_OBJECT);
            mw.visitJumpInsn(167, for_inc_i_);
            mw.visitLabel(next_);
        }
        return varIndex;
    }
    
    private <T> void genCreateObject(final MethodWriter mw, final ObjectWriteContext context, final String classNameType, final String TYPE_OBJECT, final int FEATURES, final boolean fieldBased, final Constructor defaultConstructor, final Supplier creator) {
        final Class objectClass = context.objectClass;
        final int JSON_READER = 1;
        final int objectModifiers = (objectClass == null) ? 1 : objectClass.getModifiers();
        final boolean publicObject = Modifier.isPublic(objectModifiers) && (objectClass == null || !this.classLoader.isExternalClass(objectClass));
        if (defaultConstructor == null || !publicObject || !Modifier.isPublic(defaultConstructor.getModifiers())) {
            if (creator != null) {
                mw.visitVarInsn(25, 0);
                mw.visitFieldInsn(180, classNameType, "creator", "Ljava/util/function/Supplier;");
                mw.visitMethodInsn(185, "java/util/function/Supplier", "get", "()Ljava/lang/Object;", true);
            }
            else {
                mw.visitVarInsn(25, 0);
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(22, FEATURES);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "features", "(J)J", false);
                mw.visitMethodInsn(182, classNameType, "createInstance", "(J)Ljava/lang/Object;", false);
            }
            if (publicObject) {
                mw.visitTypeInsn(192, TYPE_OBJECT);
            }
        }
        else {
            newObject(mw, TYPE_OBJECT, defaultConstructor);
        }
        if (context.hasStringField) {
            final Label endInitStringAsEmpty_ = new Label();
            final Label addResolveTask_ = new Label();
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isInitStringFieldAsEmpty", "()Z", false);
            mw.visitJumpInsn(153, endInitStringAsEmpty_);
            mw.visitInsn(89);
            mw.visitVarInsn(25, 0);
            mw.visitInsn(95);
            mw.visitMethodInsn(182, classNameType, "initStringFieldAsEmpty", "(Ljava/lang/Object;)V", false);
            mw.visitLabel(endInitStringAsEmpty_);
        }
    }
    
    private <T> int genReadFieldValue(final ObjectWriteContext context, final FieldReader fieldReader, final boolean fieldBased, final String classNameType, final MethodWriter mw, final int THIS, final int JSON_READER, final int OBJECT, final int FEATURES, int varIndex, final Map<Object, Integer> variants, final int ITEM_CNT, final int J, final int i, final boolean jsonb, final String TYPE_OBJECT) {
        final Class objectClass = context.objectClass;
        final Class fieldClass = fieldReader.fieldClass;
        final Type fieldType = fieldReader.fieldType;
        final long fieldFeatures = fieldReader.features;
        final String format = fieldReader.format;
        final Type itemType = fieldReader.itemType;
        if ((fieldFeatures & JSONReader.Feature.NullOnError.mask) != 0x0L) {
            mw.visitVarInsn(25, THIS);
            mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
            mw.visitVarInsn(25, JSON_READER);
            mw.visitVarInsn(25, OBJECT);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_READE, "readFieldValue", ObjectReaderCreatorASM.METHOD_DESC_READ_FIELD_VALUE, false);
            return varIndex;
        }
        final Field field = fieldReader.field;
        final Method method = fieldReader.method;
        final Label endSet_ = new Label();
        final String TYPE_FIELD_CLASS = ASMUtils.type(fieldClass);
        final String DESC_FIELD_CLASS = ASMUtils.desc(fieldClass);
        mw.visitVarInsn(25, OBJECT);
        int fieldModifier = 0;
        if ((fieldBased || method == null) && field != null) {
            fieldModifier = field.getModifiers();
        }
        if (fieldBased && Modifier.isPublic(objectClass.getModifiers()) && Modifier.isPublic(fieldModifier) && !Modifier.isFinal(fieldModifier) && !this.classLoader.isExternalClass(objectClass)) {
            mw.visitTypeInsn(192, TYPE_OBJECT);
        }
        if (fieldClass == Boolean.TYPE) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readBoolValue", "()Z", false);
        }
        else if (fieldClass == Byte.TYPE) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readInt32Value", "()I", false);
        }
        else if (fieldClass == Short.TYPE) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readInt32Value", "()I", false);
        }
        else if (fieldClass == Integer.TYPE) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readInt32Value", "()I", false);
        }
        else if (fieldClass == Long.TYPE) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readInt64Value", "()J", false);
        }
        else if (fieldClass == Float.TYPE) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readFloatValue", "()F", false);
        }
        else if (fieldClass == Double.TYPE) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readDoubleValue", "()D", false);
        }
        else if (fieldClass == Character.TYPE) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readCharValue", "()C", false);
        }
        else if (fieldClass == String.class) {
            mw.visitVarInsn(25, JSON_READER);
            final Label null_ = new Label();
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readString", "()Ljava/lang/String;", false);
            mw.visitInsn(89);
            mw.visitJumpInsn(198, null_);
            if ("trim".equals(format)) {
                mw.visitMethodInsn(182, "java/lang/String", "trim", "()Ljava/lang/String;", false);
            }
            mw.visitLabel(null_);
        }
        else if (fieldClass == Byte.class) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readInt8", "()Ljava/lang/Byte;", false);
        }
        else if (fieldClass == Short.class) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readInt16", "()Ljava/lang/Short;", false);
        }
        else if (fieldClass == Integer.class) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readInt32", "()Ljava/lang/Integer;", false);
        }
        else if (fieldClass == Long.class) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readInt64", "()Ljava/lang/Long;", false);
        }
        else if (fieldClass == Float.class) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readFloat", "()Ljava/lang/Float;", false);
        }
        else if (fieldClass == Double.class) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readDouble", "()Ljava/lang/Double;", false);
        }
        else if (fieldClass == BigDecimal.class) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readBigDecimal", "()Ljava/math/BigDecimal;", false);
        }
        else if (fieldClass == BigInteger.class) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readBigInteger", "()Ljava/math/BigInteger;", false);
        }
        else if (fieldClass == Number.class) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readNumber", "()Ljava/lang/Number;", false);
        }
        else if (fieldClass == UUID.class) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readUUID", "()Ljava/util/UUID;", false);
        }
        else if (fieldClass == LocalDate.class && fieldReader.format == null) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readLocalDate", "()Ljava/time/LocalDate;", false);
        }
        else if (fieldClass == OffsetDateTime.class && fieldReader.format == null) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readOffsetDateTime", "()Ljava/time/OffsetDateTime;", false);
        }
        else if (fieldClass == Date.class && fieldReader.format == null) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readDate", "()Ljava/util/Date;", false);
        }
        else if (fieldClass == Calendar.class && fieldReader.format == null) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readCalendar", "()Ljava/util/Calendar;", false);
        }
        else {
            final Label endObject_ = new Label();
            Integer REFERENCE = variants.get("REFERENCE");
            if (REFERENCE == null) {
                variants.put("REFERENCE", REFERENCE = varIndex);
                ++varIndex;
            }
            if (!ObjectWriterProvider.isPrimitiveOrEnum(fieldClass)) {
                final Label endReference_ = new Label();
                final Label addResolveTask_ = new Label();
                mw.visitVarInsn(25, JSON_READER);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isReference", "()Z", false);
                mw.visitJumpInsn(153, endReference_);
                mw.visitVarInsn(25, JSON_READER);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readReference", "()Ljava/lang/String;", false);
                mw.visitInsn(89);
                mw.visitVarInsn(58, REFERENCE);
                mw.visitLdcInsn("..");
                mw.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                mw.visitJumpInsn(153, addResolveTask_);
                if (objectClass != null && fieldClass.isAssignableFrom(objectClass)) {
                    mw.visitVarInsn(25, OBJECT);
                    mw.visitJumpInsn(167, endObject_);
                }
                mw.visitLabel(addResolveTask_);
                mw.visitVarInsn(25, THIS);
                mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
                mw.visitVarInsn(25, JSON_READER);
                mw.visitVarInsn(25, OBJECT);
                mw.visitVarInsn(25, REFERENCE);
                mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_READE, "addResolveTask", ObjectReaderCreatorASM.METHOD_DESC_ADD_RESOLVE_TASK, false);
                mw.visitInsn(87);
                mw.visitJumpInsn(167, endSet_);
                mw.visitLabel(endReference_);
            }
            if (!fieldReader.fieldClassSerializable) {
                final Label endIgnoreCheck_ = new Label();
                mw.visitVarInsn(25, JSON_READER);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isIgnoreNoneSerializable", "()Z", false);
                mw.visitJumpInsn(153, endIgnoreCheck_);
                mw.visitVarInsn(25, JSON_READER);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "skipValue", "()V", false);
                mw.visitInsn(87);
                mw.visitJumpInsn(167, endSet_);
                mw.visitLabel(endIgnoreCheck_);
            }
            boolean list = List.class.isAssignableFrom(fieldClass) && !fieldClass.getName().startsWith("com.google.common.collect.Immutable");
            if (list) {
                final Class itemClass = TypeUtils.getMapping(itemType);
                if (itemClass != null && Collection.class.isAssignableFrom(itemClass)) {
                    list = false;
                }
            }
            if (list) {
                varIndex = this.genReadFieldValueList(fieldReader, classNameType, mw, THIS, JSON_READER, OBJECT, FEATURES, varIndex, variants, ITEM_CNT, J, i, jsonb, objectClass, fieldClass, fieldType, fieldFeatures, itemType, TYPE_FIELD_CLASS, REFERENCE);
            }
            else {
                final String FIELD_OBJECT_READER = fieldObjectReader(i);
                final Label valueNotNull_ = new Label();
                mw.visitVarInsn(25, JSON_READER);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfNull", "()Z", false);
                mw.visitJumpInsn(153, valueNotNull_);
                if (fieldClass == Optional.class) {
                    mw.visitMethodInsn(184, "java/util/Optional", "empty", "()Ljava/util/Optional;", false);
                }
                else if (fieldClass == OptionalInt.class) {
                    mw.visitMethodInsn(184, "java/util/OptionalInt", "empty", "()Ljava/util/OptionalInt;", false);
                }
                else if (fieldClass == OptionalLong.class) {
                    mw.visitMethodInsn(184, "java/util/OptionalLong", "empty", "()Ljava/util/OptionalLong;", false);
                }
                else if (fieldClass == OptionalDouble.class) {
                    mw.visitMethodInsn(184, "java/util/OptionalDouble", "empty", "()Ljava/util/OptionalDouble;", false);
                }
                else {
                    mw.visitInsn(1);
                }
                mw.visitJumpInsn(167, endObject_);
                mw.visitLabel(valueNotNull_);
                if (fieldClass == String[].class) {
                    mw.visitVarInsn(25, JSON_READER);
                    mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readStringArray", "()[Ljava/lang/String;", false);
                }
                else if (fieldClass == int[].class) {
                    mw.visitVarInsn(25, JSON_READER);
                    mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readInt32ValueArray", "()[I", false);
                }
                else if (fieldClass == long[].class) {
                    mw.visitVarInsn(25, JSON_READER);
                    mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readInt64ValueArray", "()[J", false);
                }
                else {
                    if (Enum.class.isAssignableFrom(fieldClass) & !jsonb) {
                        this.genReadEnumValueRaw(fieldReader, classNameType, mw, THIS, JSON_READER, i, jsonb, fieldType, fieldClass, fieldFeatures, FIELD_OBJECT_READER);
                    }
                    else {
                        this.genReadObject(fieldReader, classNameType, mw, THIS, JSON_READER, i, jsonb, fieldType, fieldFeatures, FIELD_OBJECT_READER);
                    }
                    if (method != null || ((objectClass == null || Modifier.isPublic(objectClass.getModifiers())) && Modifier.isPublic(fieldModifier) && !Modifier.isFinal(fieldModifier) && !this.classLoader.isExternalClass(objectClass))) {
                        mw.visitTypeInsn(192, TYPE_FIELD_CLASS);
                    }
                    if (fieldReader.noneStaticMemberClass) {
                        try {
                            final Field this2 = fieldClass.getDeclaredField("this$0");
                            final long fieldOffset = JDKUtils.UNSAFE.objectFieldOffset(this2);
                            mw.visitInsn(89);
                            mw.visitFieldInsn(178, ASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
                            mw.visitInsn(95);
                            mw.visitLdcInsn(fieldOffset);
                            mw.visitVarInsn(25, OBJECT);
                            mw.visitMethodInsn(182, "sun/misc/Unsafe", "putObject", "(Ljava/lang/Object;JLjava/lang/Object;)V", false);
                        }
                        catch (NoSuchFieldException ex) {}
                    }
                }
            }
            mw.visitLabel(endObject_);
            if (!jsonb) {
                mw.visitVarInsn(25, JSON_READER);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfComma", "()Z", false);
                mw.visitInsn(87);
            }
        }
        if (field != null) {
            final String fieldClassName = fieldClass.getName();
            final boolean setDirect = (objectClass.getModifiers() & 0x1) != 0x0 && (fieldModifier & 0x1) != 0x0 && (fieldModifier & 0x10) == 0x0 && (ObjectWriterProvider.isPrimitiveOrEnum(fieldClass) || fieldClassName.startsWith("java.") || fieldClass.getClassLoader() == ObjectReaderProvider.FASTJSON2_CLASS_LOADER) && !this.classLoader.isExternalClass(objectClass) && field.getDeclaringClass() == objectClass;
            if (setDirect) {
                mw.visitFieldInsn(181, TYPE_OBJECT, field.getName(), DESC_FIELD_CLASS);
            }
            else {
                Integer FIELD_VALUE = variants.get(fieldClass);
                if (FIELD_VALUE == null) {
                    variants.put(fieldClass, FIELD_VALUE = varIndex);
                    if (fieldClass == Long.TYPE || fieldClass == Double.TYPE) {
                        varIndex += 2;
                    }
                    else {
                        ++varIndex;
                    }
                }
                String methodName;
                String methodDes;
                int LOAD;
                if (fieldClass == Integer.TYPE) {
                    methodName = "putInt";
                    methodDes = "(Ljava/lang/Object;JI)V";
                    mw.visitVarInsn(54, FIELD_VALUE);
                    LOAD = 21;
                }
                else if (fieldClass == Long.TYPE) {
                    methodName = "putLong";
                    methodDes = "(Ljava/lang/Object;JJ)V";
                    mw.visitVarInsn(55, FIELD_VALUE);
                    LOAD = 22;
                }
                else if (fieldClass == Float.TYPE) {
                    methodName = "putFloat";
                    methodDes = "(Ljava/lang/Object;JF)V";
                    mw.visitVarInsn(56, FIELD_VALUE);
                    LOAD = 23;
                }
                else if (fieldClass == Double.TYPE) {
                    methodName = "putDouble";
                    methodDes = "(Ljava/lang/Object;JD)V";
                    mw.visitVarInsn(57, FIELD_VALUE);
                    LOAD = 24;
                }
                else if (fieldClass == Character.TYPE) {
                    methodName = "putChar";
                    methodDes = "(Ljava/lang/Object;JC)V";
                    mw.visitVarInsn(54, FIELD_VALUE);
                    LOAD = 21;
                }
                else if (fieldClass == Byte.TYPE) {
                    methodName = "putByte";
                    methodDes = "(Ljava/lang/Object;JB)V";
                    mw.visitVarInsn(54, FIELD_VALUE);
                    LOAD = 21;
                }
                else if (fieldClass == Short.TYPE) {
                    methodName = "putShort";
                    methodDes = "(Ljava/lang/Object;JS)V";
                    mw.visitVarInsn(54, FIELD_VALUE);
                    LOAD = 21;
                }
                else if (fieldClass == Boolean.TYPE) {
                    methodName = "putBoolean";
                    methodDes = "(Ljava/lang/Object;JZ)V";
                    mw.visitVarInsn(54, FIELD_VALUE);
                    LOAD = 21;
                }
                else {
                    methodName = "putObject";
                    methodDes = "(Ljava/lang/Object;JLjava/lang/Object;)V";
                    mw.visitVarInsn(58, FIELD_VALUE);
                    LOAD = 25;
                }
                mw.visitFieldInsn(178, ASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
                mw.visitInsn(95);
                mw.visitLdcInsn(JDKUtils.UNSAFE.objectFieldOffset(field));
                mw.visitVarInsn(LOAD, FIELD_VALUE);
                mw.visitMethodInsn(182, "sun/misc/Unsafe", methodName, methodDes, false);
            }
        }
        else {
            final boolean invokeFieldReaderAccept = context.externalClass || method == null || !context.publicClass;
            if (invokeFieldReaderAccept) {
                Integer FIELD_VALUE2 = variants.get(fieldClass);
                if (FIELD_VALUE2 == null) {
                    variants.put(fieldClass, FIELD_VALUE2 = varIndex);
                    if (fieldClass == Long.TYPE || fieldClass == Double.TYPE) {
                        varIndex += 2;
                    }
                    else {
                        ++varIndex;
                    }
                }
                String acceptMethodDesc;
                int LOAD2;
                if (fieldClass == Boolean.TYPE) {
                    acceptMethodDesc = "(Ljava/lang/Object;Z)V";
                    mw.visitVarInsn(54, FIELD_VALUE2);
                    LOAD2 = 21;
                }
                else if (fieldClass == Byte.TYPE) {
                    acceptMethodDesc = "(Ljava/lang/Object;B)V";
                    mw.visitVarInsn(54, FIELD_VALUE2);
                    LOAD2 = 21;
                }
                else if (fieldClass == Short.TYPE) {
                    acceptMethodDesc = "(Ljava/lang/Object;S)V";
                    mw.visitVarInsn(54, FIELD_VALUE2);
                    LOAD2 = 21;
                }
                else if (fieldClass == Integer.TYPE) {
                    acceptMethodDesc = "(Ljava/lang/Object;I)V";
                    mw.visitVarInsn(54, FIELD_VALUE2);
                    LOAD2 = 21;
                }
                else if (fieldClass == Long.TYPE) {
                    acceptMethodDesc = "(Ljava/lang/Object;J)V";
                    mw.visitVarInsn(55, FIELD_VALUE2);
                    LOAD2 = 22;
                }
                else if (fieldClass == Character.TYPE) {
                    acceptMethodDesc = "(Ljava/lang/Object;C)V";
                    mw.visitVarInsn(54, FIELD_VALUE2);
                    LOAD2 = 21;
                }
                else if (fieldClass == Float.TYPE) {
                    acceptMethodDesc = "(Ljava/lang/Object;F)V";
                    mw.visitVarInsn(56, FIELD_VALUE2);
                    LOAD2 = 23;
                }
                else if (fieldClass == Double.TYPE) {
                    acceptMethodDesc = "(Ljava/lang/Object;D)V";
                    mw.visitVarInsn(57, FIELD_VALUE2);
                    LOAD2 = 24;
                }
                else {
                    acceptMethodDesc = "(Ljava/lang/Object;Ljava/lang/Object;)V";
                    mw.visitVarInsn(58, FIELD_VALUE2);
                    LOAD2 = 25;
                }
                mw.visitVarInsn(25, THIS);
                mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
                final BiConsumer function = fieldReader.getFunction();
                if (function instanceof FieldBiConsumer) {
                    final FieldBiConsumer fieldBiConsumer = (FieldBiConsumer)function;
                    mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_READE, "getFunction", "()Ljava/util/function/BiConsumer;", false);
                    mw.visitTypeInsn(192, ASMUtils.type(FieldBiConsumer.class));
                    mw.visitFieldInsn(180, ASMUtils.type(FieldBiConsumer.class), "consumer", ASMUtils.desc(FieldConsumer.class));
                    mw.visitInsn(95);
                    mw.visitLdcInsn(fieldBiConsumer.fieldIndex);
                    mw.visitVarInsn(LOAD2, FIELD_VALUE2);
                    mw.visitMethodInsn(185, ASMUtils.type(FieldConsumer.class), "accept", "(Ljava/lang/Object;ILjava/lang/Object;)V", true);
                }
                else {
                    mw.visitInsn(95);
                    mw.visitVarInsn(LOAD2, FIELD_VALUE2);
                    mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_READE, "accept", acceptMethodDesc, false);
                }
            }
            else {
                final Class<?> returnType = method.getReturnType();
                final String methodName2 = method.getName();
                String methodDesc = null;
                if (returnType == Void.TYPE) {
                    if (fieldClass == Boolean.TYPE) {
                        methodDesc = "(Z)V";
                    }
                    else if (fieldClass == Byte.TYPE) {
                        methodDesc = "(B)V";
                    }
                    else if (fieldClass == Short.TYPE) {
                        methodDesc = "(S)V";
                    }
                    else if (fieldClass == Integer.TYPE) {
                        methodDesc = "(I)V";
                    }
                    else if (fieldClass == Long.TYPE) {
                        methodDesc = "(J)V";
                    }
                    else if (fieldClass == Character.TYPE) {
                        methodDesc = "(C)V";
                    }
                    else if (fieldClass == Float.TYPE) {
                        methodDesc = "(F)V";
                    }
                    else if (fieldClass == Double.TYPE) {
                        methodDesc = "(D)V";
                    }
                    else if (fieldClass == Boolean.class) {
                        methodDesc = "(Ljava/lang/Boolean;)V";
                    }
                    else if (fieldClass == Integer.class) {
                        methodDesc = "(Ljava/lang/Integer;)V";
                    }
                    else if (fieldClass == Long.class) {
                        methodDesc = "(Ljava/lang/Long;)V";
                    }
                    else if (fieldClass == Float.class) {
                        methodDesc = "(Ljava/lang/Float;)V";
                    }
                    else if (fieldClass == Double.class) {
                        methodDesc = "(Ljava/lang/Double;)V";
                    }
                    else if (fieldClass == BigDecimal.class) {
                        methodDesc = "(Ljava/math/BigDecimal;)V";
                    }
                    else if (fieldClass == String.class) {
                        methodDesc = "(Ljava/lang/String;)V";
                    }
                    else if (fieldClass == UUID.class) {
                        methodDesc = "(Ljava/util/UUID;)V";
                    }
                    else if (fieldClass == List.class) {
                        methodDesc = "(Ljava/util/List;)V";
                    }
                    else if (fieldClass == Map.class) {
                        methodDesc = "(Ljava/util/Map;)V";
                    }
                }
                if (methodDesc == null) {
                    methodDesc = "(" + DESC_FIELD_CLASS + ")" + ASMUtils.desc(returnType);
                }
                mw.visitMethodInsn(182, TYPE_OBJECT, methodName2, methodDesc, false);
                if (returnType != Void.TYPE) {
                    mw.visitInsn(87);
                }
            }
        }
        mw.visitLabel(endSet_);
        return varIndex;
    }
    
    private void genReadObject(final FieldReader fieldReader, final String classNameType, final MethodWriter mw, final int THIS, final int JSON_READER, final int i, final boolean jsonb, final Type fieldType, final long fieldFeatures, final String FIELD_OBJECT_READER) {
        final Label notNull_ = new Label();
        mw.visitVarInsn(25, THIS);
        mw.visitFieldInsn(180, classNameType, FIELD_OBJECT_READER, ASMUtils.DESC_OBJECT_READER);
        mw.visitJumpInsn(199, notNull_);
        mw.visitVarInsn(25, THIS);
        mw.visitVarInsn(25, THIS);
        mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
        mw.visitVarInsn(25, JSON_READER);
        mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_READE, "getObjectReader", ObjectReaderCreatorASM.METHOD_DESC_GET_OBJECT_READER_1, false);
        mw.visitFieldInsn(181, classNameType, FIELD_OBJECT_READER, ASMUtils.DESC_OBJECT_READER);
        mw.visitLabel(notNull_);
        mw.visitVarInsn(25, THIS);
        mw.visitFieldInsn(180, classNameType, FIELD_OBJECT_READER, ASMUtils.DESC_OBJECT_READER);
        mw.visitVarInsn(25, JSON_READER);
        this.gwGetFieldType(classNameType, mw, THIS, i, fieldType);
        mw.visitLdcInsn(fieldReader.fieldName);
        mw.visitLdcInsn(fieldFeatures);
        mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_READER, jsonb ? "readJSONBObject" : "readObject", ObjectReaderCreatorASM.METHOD_DESC_READ_OBJECT, true);
    }
    
    private void genReadEnumValueRaw(final FieldReader fieldReader, final String classNameType, final MethodWriter mw, final int THIS, final int JSON_READER, final int fieldIndex, final boolean jsonb, final Type fieldType, final Class fieldClass, final long fieldFeatures, final String FIELD_OBJECT_READER) {
        final Object[] enums = fieldClass.getEnumConstants();
        final Map<Integer, List<Enum>> name0Map = new TreeMap<Integer, List<Enum>>();
        int nameLengthMin = 0;
        int nameLengthMax = 0;
        if (enums != null) {
            for (int i = 0; i < enums.length; ++i) {
                final Enum e = (Enum)enums[i];
                final byte[] enumName = e.name().getBytes(StandardCharsets.UTF_8);
                final int nameLength = enumName.length;
                if (i == 0) {
                    nameLengthMin = nameLength;
                    nameLengthMax = nameLength;
                }
                else {
                    nameLengthMin = Math.min(nameLength, nameLengthMin);
                    nameLengthMax = Math.max(nameLength, nameLengthMax);
                }
                final byte[] name0Bytes = new byte[4];
                name0Bytes[0] = 34;
                if (enumName.length == 2) {
                    System.arraycopy(enumName, 0, name0Bytes, 1, 2);
                    name0Bytes[3] = 34;
                }
                else if (enumName.length >= 3) {
                    System.arraycopy(enumName, 0, name0Bytes, 1, 3);
                }
                final int name0 = JDKUtils.UNSAFE.getInt(name0Bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                List<Enum> enumList = name0Map.get(name0);
                if (enumList == null) {
                    enumList = new ArrayList<Enum>();
                    name0Map.put(name0, enumList);
                }
                enumList.add(e);
            }
        }
        final Label dflt = new Label();
        final Label enumEnd = new Label();
        final Label notNull_ = new Label();
        mw.visitVarInsn(25, THIS);
        mw.visitFieldInsn(180, classNameType, FIELD_OBJECT_READER, ASMUtils.DESC_OBJECT_READER);
        mw.visitJumpInsn(199, notNull_);
        mw.visitVarInsn(25, THIS);
        mw.visitVarInsn(25, THIS);
        mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(fieldIndex), ASMUtils.DESC_FIELD_READER);
        mw.visitVarInsn(25, JSON_READER);
        mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_READE, "getObjectReader", ObjectReaderCreatorASM.METHOD_DESC_GET_OBJECT_READER_1, false);
        mw.visitFieldInsn(181, classNameType, FIELD_OBJECT_READER, ASMUtils.DESC_OBJECT_READER);
        mw.visitLabel(notNull_);
        mw.visitVarInsn(25, THIS);
        mw.visitFieldInsn(180, classNameType, FIELD_OBJECT_READER, ASMUtils.DESC_OBJECT_READER);
        mw.visitTypeInsn(193, ASMUtils.type(ObjectReaderImplEnum.class));
        mw.visitJumpInsn(153, dflt);
        if (nameLengthMin >= 2 && nameLengthMax <= 11) {
            final int[] switchKeys = new int[name0Map.size()];
            final Label[] labels = new Label[name0Map.size()];
            final Iterator it = name0Map.keySet().iterator();
            for (int j = 0; j < labels.length; ++j) {
                labels[j] = new Label();
                switchKeys[j] = it.next();
            }
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "getRawInt", "()I", false);
            mw.visitLookupSwitchInsn(dflt, switchKeys, labels);
            for (int k = 0; k < labels.length; ++k) {
                mw.visitLabel(labels[k]);
                final int name2 = switchKeys[k];
                final List<Enum> enumList2 = name0Map.get(name2);
                for (int l = 0; l < enumList2.size(); ++l) {
                    Label nextJ = null;
                    if (l > 0) {
                        nextJ = new Label();
                    }
                    final Enum e2 = enumList2.get(l);
                    final byte[] enumName2 = e2.name().getBytes(StandardCharsets.UTF_8);
                    final int fieldNameLength = enumName2.length;
                    switch (fieldNameLength) {
                        case 2: {
                            mw.visitVarInsn(25, JSON_READER);
                            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfValue4Match2", "()Z", false);
                            break;
                        }
                        case 3: {
                            mw.visitVarInsn(25, JSON_READER);
                            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfValue4Match3", "()Z", false);
                            break;
                        }
                        case 4: {
                            mw.visitVarInsn(25, JSON_READER);
                            mw.visitLdcInsn(enumName2[3]);
                            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfValue4Match4", "(B)Z", false);
                            break;
                        }
                        case 5: {
                            mw.visitVarInsn(25, JSON_READER);
                            mw.visitLdcInsn(enumName2[3]);
                            mw.visitLdcInsn(enumName2[4]);
                            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfValue4Match5", "(BB)Z", false);
                            break;
                        }
                        case 6: {
                            final byte[] bytes4 = { enumName2[3], enumName2[4], enumName2[5], 34 };
                            final int name3 = JDKUtils.UNSAFE.getInt(bytes4, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                            mw.visitVarInsn(25, JSON_READER);
                            mw.visitLdcInsn(name3);
                            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfValue4Match6", "(I)Z", false);
                            break;
                        }
                        case 7: {
                            final int name4 = JDKUtils.UNSAFE.getInt(enumName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                            mw.visitVarInsn(25, JSON_READER);
                            mw.visitLdcInsn(name4);
                            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfValue4Match7", "(I)Z", false);
                            break;
                        }
                        case 8: {
                            final int name4 = JDKUtils.UNSAFE.getInt(enumName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                            mw.visitVarInsn(25, JSON_READER);
                            mw.visitLdcInsn(name4);
                            mw.visitLdcInsn(enumName2[7]);
                            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfValue4Match8", "(IB)Z", false);
                            break;
                        }
                        case 9: {
                            final int name4 = JDKUtils.UNSAFE.getInt(enumName2, JDKUtils.ARRAY_BYTE_BASE_OFFSET + 3L);
                            mw.visitVarInsn(25, JSON_READER);
                            mw.visitLdcInsn(name4);
                            mw.visitLdcInsn(enumName2[7]);
                            mw.visitLdcInsn(enumName2[8]);
                            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfValue4Match9", "(IBB)Z", false);
                            break;
                        }
                        case 10: {
                            final byte[] bytes5 = new byte[8];
                            System.arraycopy(enumName2, 3, bytes5, 0, 7);
                            bytes5[7] = 34;
                            final long name5 = JDKUtils.UNSAFE.getLong(bytes5, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                            mw.visitVarInsn(25, JSON_READER);
                            mw.visitLdcInsn(name5);
                            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfValue4Match10", "(J)Z", false);
                            break;
                        }
                        case 11: {
                            final byte[] bytes5 = new byte[8];
                            System.arraycopy(enumName2, 3, bytes5, 0, 8);
                            final long name5 = JDKUtils.UNSAFE.getLong(bytes5, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                            mw.visitVarInsn(25, JSON_READER);
                            mw.visitLdcInsn(name5);
                            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfValue4Match11", "(J)Z", false);
                            break;
                        }
                        default: {
                            throw new IllegalStateException("fieldNameLength " + fieldNameLength);
                        }
                    }
                    mw.visitJumpInsn(153, (nextJ != null) ? nextJ : dflt);
                    mw.visitVarInsn(25, THIS);
                    mw.visitFieldInsn(180, classNameType, FIELD_OBJECT_READER, ASMUtils.DESC_OBJECT_READER);
                    mw.visitTypeInsn(192, ASMUtils.type(ObjectReaderImplEnum.class));
                    mw.visitLdcInsn(e2.ordinal());
                    mw.visitMethodInsn(182, ASMUtils.type(ObjectReaderImplEnum.class), "getEnumByOrdinal", "(I)Ljava/lang/Enum;", false);
                    mw.visitJumpInsn(167, enumEnd);
                    if (nextJ != null) {
                        mw.visitLabel(nextJ);
                    }
                }
                mw.visitJumpInsn(167, dflt);
            }
        }
        mw.visitLabel(dflt);
        mw.visitVarInsn(25, THIS);
        mw.visitFieldInsn(180, classNameType, FIELD_OBJECT_READER, ASMUtils.DESC_OBJECT_READER);
        mw.visitVarInsn(25, JSON_READER);
        this.gwGetFieldType(classNameType, mw, THIS, fieldIndex, fieldType);
        mw.visitLdcInsn(fieldReader.fieldName);
        mw.visitLdcInsn(fieldFeatures);
        mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_READER, jsonb ? "readJSONBObject" : "readObject", ObjectReaderCreatorASM.METHOD_DESC_READ_OBJECT, true);
        mw.visitLabel(enumEnd);
    }
    
    private int genReadFieldValueList(final FieldReader fieldReader, final String classNameType, final MethodWriter mw, final int THIS, final int JSON_READER, final int OBJECT, final int FEATURES, int varIndex, final Map<Object, Integer> variants, final int ITEM_CNT, final int J, final int i, final boolean jsonb, final Class objectClass, final Class fieldClass, final Type fieldType, final long fieldFeatures, Type itemType, final String TYPE_FIELD_CLASS, final Integer REFERENCE) {
        if (itemType == null) {
            itemType = Object.class;
        }
        final Class itemClass = TypeUtils.getMapping(itemType);
        final String ITEM_OBJECT_READER = fieldItemObjectReader(i);
        Integer LIST = variants.get(fieldClass);
        if (LIST == null) {
            variants.put(fieldClass, LIST = varIndex);
            ++varIndex;
        }
        Integer AUTO_TYPE_OBJECT_READER = variants.get(ObjectReader.class);
        if (AUTO_TYPE_OBJECT_READER == null) {
            variants.put(fieldClass, AUTO_TYPE_OBJECT_READER = varIndex);
            ++varIndex;
        }
        final String LIST_TYPE = fieldClass.isInterface() ? "java/util/ArrayList" : TYPE_FIELD_CLASS;
        final Label loadList_ = new Label();
        final Label listNotNull_ = new Label();
        final boolean initCapacity = JDKUtils.JVM_VERSION == 8 && "java/util/ArrayList".equals(LIST_TYPE);
        if (jsonb) {
            final Label checkAutoTypeNull_ = new Label();
            mw.visitVarInsn(25, THIS);
            mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_READE, "checkObjectAutoType", ObjectReaderCreatorASM.METHOD_DESC_CHECK_ARRAY_AUTO_TYPE, false);
            mw.visitInsn(89);
            mw.visitVarInsn(58, AUTO_TYPE_OBJECT_READER);
            mw.visitJumpInsn(198, checkAutoTypeNull_);
            mw.visitVarInsn(25, AUTO_TYPE_OBJECT_READER);
            mw.visitVarInsn(25, JSON_READER);
            this.gwGetFieldType(classNameType, mw, THIS, i, fieldType);
            mw.visitLdcInsn(fieldReader.fieldName);
            mw.visitLdcInsn(fieldFeatures);
            mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_READER, "readJSONBObject", ObjectReaderCreatorASM.METHOD_DESC_READ_OBJECT, true);
            mw.visitTypeInsn(192, TYPE_FIELD_CLASS);
            mw.visitVarInsn(58, LIST);
            mw.visitJumpInsn(167, loadList_);
            mw.visitLabel(checkAutoTypeNull_);
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "startArray", "()I", false);
            mw.visitInsn(89);
            mw.visitVarInsn(54, ITEM_CNT);
            mw.visitLdcInsn(-1);
            mw.visitJumpInsn(160, listNotNull_);
            mw.visitInsn(1);
            mw.visitVarInsn(58, LIST);
            mw.visitJumpInsn(167, loadList_);
            mw.visitLabel(listNotNull_);
            mw.visitTypeInsn(187, LIST_TYPE);
            mw.visitInsn(89);
            if (initCapacity) {
                mw.visitVarInsn(21, ITEM_CNT);
                mw.visitMethodInsn(183, LIST_TYPE, "<init>", "(I)V", false);
            }
            else {
                mw.visitMethodInsn(183, LIST_TYPE, "<init>", "()V", false);
            }
            mw.visitVarInsn(58, LIST);
        }
        else {
            final Label match_ = new Label();
            final Label skipValue_ = new Label();
            final Label loadNull_ = new Label();
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfNull", "()Z", false);
            mw.visitJumpInsn(154, loadNull_);
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfArrayStart", "()Z", false);
            mw.visitJumpInsn(154, match_);
            if (itemClass == String.class) {
                mw.visitVarInsn(25, JSON_READER);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isString", "()Z", false);
                mw.visitJumpInsn(153, skipValue_);
                mw.visitTypeInsn(187, LIST_TYPE);
                mw.visitInsn(89);
                if (initCapacity) {
                    mw.visitLdcInsn(10);
                    mw.visitMethodInsn(183, LIST_TYPE, "<init>", "(I)V", false);
                }
                else {
                    mw.visitMethodInsn(183, LIST_TYPE, "<init>", "()V", false);
                }
                mw.visitVarInsn(58, LIST);
                mw.visitVarInsn(25, JSON_READER);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfNullOrEmptyString", "()Z", false);
                mw.visitJumpInsn(154, loadList_);
                mw.visitVarInsn(25, LIST);
                mw.visitVarInsn(25, JSON_READER);
                if (itemClass == String.class) {
                    mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readString", "()Ljava/lang/String;", false);
                }
                mw.visitMethodInsn(185, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
                mw.visitInsn(87);
                mw.visitJumpInsn(167, loadList_);
            }
            else if (itemType instanceof Class) {
                mw.visitVarInsn(25, JSON_READER);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfNullOrEmptyString", "()Z", false);
                mw.visitJumpInsn(154, loadNull_);
                mw.visitTypeInsn(187, LIST_TYPE);
                mw.visitInsn(89);
                if (initCapacity) {
                    mw.visitLdcInsn(10);
                    mw.visitMethodInsn(183, LIST_TYPE, "<init>", "(I)V", false);
                }
                else {
                    mw.visitMethodInsn(183, LIST_TYPE, "<init>", "()V", false);
                }
                mw.visitVarInsn(58, LIST);
                mw.visitVarInsn(25, JSON_READER);
                mw.visitVarInsn(25, LIST);
                mw.visitLdcInsn((Class)itemType);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readArray", "(Ljava/util/List;Ljava/lang/reflect/Type;)V", false);
                mw.visitJumpInsn(167, loadList_);
            }
            mw.visitLabel(skipValue_);
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "skipValue", "()V", false);
            mw.visitLabel(loadNull_);
            mw.visitInsn(1);
            mw.visitVarInsn(58, LIST);
            mw.visitJumpInsn(167, loadList_);
            mw.visitLabel(match_);
            mw.visitTypeInsn(187, LIST_TYPE);
            mw.visitInsn(89);
            if (initCapacity) {
                mw.visitLdcInsn(10);
                mw.visitMethodInsn(183, LIST_TYPE, "<init>", "(I)V", false);
            }
            else {
                mw.visitMethodInsn(183, LIST_TYPE, "<init>", "()V", false);
            }
            mw.visitVarInsn(58, LIST);
        }
        final Label for_start_j_ = new Label();
        final Label for_end_j_ = new Label();
        final Label for_inc_j_ = new Label();
        mw.visitInsn(3);
        mw.visitVarInsn(54, J);
        mw.visitLabel(for_start_j_);
        if (jsonb) {
            mw.visitVarInsn(21, J);
            mw.visitVarInsn(21, ITEM_CNT);
            mw.visitJumpInsn(162, for_end_j_);
        }
        else {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfArrayEnd", "()Z", false);
            mw.visitJumpInsn(154, for_end_j_);
        }
        if (itemType == String.class) {
            mw.visitVarInsn(25, LIST);
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readString", "()Ljava/lang/String;", false);
        }
        else if (itemType == Integer.class) {
            mw.visitVarInsn(25, LIST);
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readInt32", "()Ljava/lang/Integer;", false);
        }
        else if (itemType == Long.class) {
            mw.visitVarInsn(25, LIST);
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readInt64", "()Ljava/lang/Long;", false);
        }
        else {
            final Label notNull_ = new Label();
            mw.visitVarInsn(25, THIS);
            mw.visitFieldInsn(180, classNameType, ITEM_OBJECT_READER, ASMUtils.DESC_OBJECT_READER);
            mw.visitJumpInsn(199, notNull_);
            mw.visitVarInsn(25, THIS);
            mw.visitVarInsn(25, THIS);
            mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_READE, "getItemObjectReader", ObjectReaderCreatorASM.METHOD_DESC_GET_ITEM_OBJECT_READER, false);
            mw.visitFieldInsn(181, classNameType, ITEM_OBJECT_READER, ASMUtils.DESC_OBJECT_READER);
            mw.visitLabel(notNull_);
            final Label endReference_ = new Label();
            final Label addResolveTask_ = new Label();
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "isReference", "()Z", false);
            mw.visitJumpInsn(153, endReference_);
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "readReference", "()Ljava/lang/String;", false);
            mw.visitInsn(89);
            mw.visitVarInsn(58, REFERENCE);
            mw.visitLdcInsn("..");
            mw.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
            mw.visitJumpInsn(153, addResolveTask_);
            if (fieldClass.isAssignableFrom(objectClass)) {
                mw.visitVarInsn(25, LIST);
                mw.visitVarInsn(25, OBJECT);
                mw.visitMethodInsn(185, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
                mw.visitInsn(87);
                mw.visitJumpInsn(167, for_inc_j_);
            }
            mw.visitLabel(addResolveTask_);
            mw.visitVarInsn(25, LIST);
            mw.visitInsn(1);
            mw.visitMethodInsn(185, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
            mw.visitInsn(87);
            mw.visitVarInsn(25, THIS);
            mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
            mw.visitVarInsn(25, JSON_READER);
            mw.visitVarInsn(25, LIST);
            mw.visitVarInsn(21, J);
            mw.visitVarInsn(25, REFERENCE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_READE, "addResolveTask", ObjectReaderCreatorASM.METHOD_DESC_ADD_RESOLVE_TASK_2, false);
            mw.visitJumpInsn(167, for_inc_j_);
            mw.visitLabel(endReference_);
            mw.visitVarInsn(25, LIST);
            mw.visitVarInsn(25, THIS);
            mw.visitFieldInsn(180, classNameType, ITEM_OBJECT_READER, ASMUtils.DESC_OBJECT_READER);
            mw.visitVarInsn(25, JSON_READER);
            this.gwGetFieldType(classNameType, mw, THIS, i, fieldType);
            mw.visitLdcInsn(fieldReader.fieldName);
            mw.visitVarInsn(22, FEATURES);
            mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_READER, jsonb ? "readJSONBObject" : "readObject", ObjectReaderCreatorASM.METHOD_DESC_READ_OBJECT, true);
        }
        mw.visitMethodInsn(185, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
        mw.visitInsn(87);
        if (!jsonb) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfComma", "()Z", false);
            mw.visitInsn(87);
        }
        mw.visitLabel(for_inc_j_);
        mw.visitIincInsn(J, 1);
        mw.visitJumpInsn(167, for_start_j_);
        mw.visitLabel(for_end_j_);
        if (!jsonb) {
            mw.visitVarInsn(25, JSON_READER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_READER, "nextIfComma", "()Z", false);
            mw.visitInsn(87);
        }
        mw.visitLabel(loadList_);
        mw.visitVarInsn(25, LIST);
        return varIndex;
    }
    
    private void gwGetFieldType(final String classNameType, final MethodWriter mw, final int THIS, final int i, final Type fieldType) {
        if (fieldType instanceof Class) {
            final Class fieldClass = (Class)fieldType;
            final String fieldClassName = fieldClass.getName();
            final boolean publicClass = Modifier.isPublic(fieldClass.getModifiers());
            final boolean internalClass = fieldClassName.startsWith("java.") || fieldClass == JSONArray.class || fieldClass == JSONObject.class;
            if (publicClass && internalClass) {
                mw.visitLdcInsn((Class)fieldType);
                return;
            }
        }
        mw.visitVarInsn(25, THIS);
        mw.visitFieldInsn(180, classNameType, CodeGenUtils.fieldReader(i), ASMUtils.DESC_FIELD_READER);
        mw.visitFieldInsn(180, ASMUtils.TYPE_FIELD_READE, "fieldType", "Ljava/lang/reflect/Type;");
    }
    
    @Override
    public Function<Consumer, ByteArrayValueConsumer> createByteArrayValueConsumerCreator(final Class objectClass, final FieldReader[] fieldReaderArray) {
        return (Function<Consumer, ByteArrayValueConsumer>)this.createValueConsumer0(objectClass, fieldReaderArray, true);
    }
    
    @Override
    public Function<Consumer, CharArrayValueConsumer> createCharArrayValueConsumerCreator(final Class objectClass, final FieldReader[] fieldReaderArray) {
        return (Function<Consumer, CharArrayValueConsumer>)this.createValueConsumer0(objectClass, fieldReaderArray, false);
    }
    
    private Function createValueConsumer0(final Class objectClass, final FieldReader[] fieldReaderArray, final boolean bytes) {
        final Constructor defaultConstructor = BeanUtils.getDefaultConstructor(objectClass, false);
        if (defaultConstructor == null || !Modifier.isPublic(objectClass.getModifiers())) {
            return null;
        }
        final ClassWriter cw = new ClassWriter(e -> objectClass.getName().equals(e) ? objectClass : null);
        final String className = (bytes ? "VBACG_" : "VCACG_") + ObjectReaderCreatorASM.seed.incrementAndGet() + "_" + fieldReaderArray.length + "_" + objectClass.getSimpleName();
        final Package pkg = ObjectReaderCreatorASM.class.getPackage();
        String classNameFull;
        String classNameType;
        if (pkg != null) {
            final String packageName = pkg.getName();
            final int packageNameLength = packageName.length();
            final int charsLength = packageNameLength + 1 + className.length();
            final char[] chars = new char[charsLength];
            packageName.getChars(0, packageName.length(), chars, 0);
            chars[packageNameLength] = '.';
            className.getChars(0, className.length(), chars, packageNameLength + 1);
            classNameFull = new String(chars);
            chars[packageNameLength] = '/';
            for (int i = 0; i < packageNameLength; ++i) {
                if (chars[i] == '.') {
                    chars[i] = '/';
                }
            }
            classNameType = new String(chars);
        }
        else {
            classNameType = className;
            classNameFull = className;
        }
        final String TYPE_OBJECT = ASMUtils.type(objectClass);
        final String DESC_OBJECT = ASMUtils.desc(objectClass);
        cw.visitField(17, "consumer", "Ljava/util/function/Consumer;");
        cw.visitField(1, "object", DESC_OBJECT);
        cw.visit(52, 49, classNameType, "java/lang/Object", new String[] { bytes ? ASMUtils.TYPE_BYTE_ARRAY_VALUE_CONSUMER : ASMUtils.TYPE_CHAR_ARRAY_VALUE_CONSUMER });
        final int CONSUMER = 1;
        final MethodWriter mw = cw.visitMethod(1, "<init>", "(Ljava/util/function/Consumer;)V", 32);
        mw.visitVarInsn(25, 0);
        mw.visitMethodInsn(183, "java/lang/Object", "<init>", "()V", false);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitFieldInsn(181, classNameType, "consumer", "Ljava/util/function/Consumer;");
        mw.visitInsn(177);
        mw.visitMaxs(3, 3);
        MethodWriter mw2 = cw.visitMethod(1, "beforeRow", "(I)V", 32);
        mw2.visitVarInsn(25, 0);
        newObject(mw2, TYPE_OBJECT, defaultConstructor);
        mw2.visitFieldInsn(181, classNameType, "object", DESC_OBJECT);
        mw2.visitInsn(177);
        mw2.visitMaxs(3, 3);
        mw2 = cw.visitMethod(1, "afterRow", "(I)V", 32);
        mw2.visitVarInsn(25, 0);
        mw2.visitFieldInsn(180, classNameType, "consumer", "Ljava/util/function/Consumer;");
        mw2.visitVarInsn(25, 0);
        mw2.visitFieldInsn(180, classNameType, "object", DESC_OBJECT);
        mw2.visitMethodInsn(185, "java/util/function/Consumer", "accept", "(Ljava/lang/Object;)V", true);
        mw2.visitVarInsn(25, 0);
        mw2.visitInsn(1);
        mw2.visitFieldInsn(181, classNameType, "object", DESC_OBJECT);
        mw2.visitInsn(177);
        mw2.visitMaxs(3, 3);
        final int ROW = 1;
        final int COLUMN = 2;
        final int BYTES = 3;
        final int OFF = 4;
        final int LEN = 5;
        final int CHARSET = 6;
        String methodDesc;
        if (bytes) {
            methodDesc = "(II[BIILjava/nio/charset/Charset;)V";
        }
        else {
            methodDesc = "(II[CII)V";
        }
        final MethodWriter mw3 = cw.visitMethod(1, "accept", methodDesc, 32);
        final Label switch_ = new Label();
        final Label L0_ = new Label();
        final Label L1_ = new Label();
        mw3.visitVarInsn(21, 5);
        mw3.visitJumpInsn(154, L0_);
        mw3.visitInsn(177);
        mw3.visitLabel(L0_);
        mw3.visitVarInsn(21, 2);
        mw3.visitJumpInsn(156, L1_);
        mw3.visitInsn(177);
        mw3.visitLabel(L1_);
        mw3.visitVarInsn(21, 2);
        mw3.visitLdcInsn(fieldReaderArray.length);
        mw3.visitJumpInsn(164, switch_);
        mw3.visitInsn(177);
        mw3.visitLabel(switch_);
        final Label dflt = new Label();
        final Label[] labels = new Label[fieldReaderArray.length];
        final int[] columns = new int[fieldReaderArray.length];
        for (int j = 0; j < columns.length; ++j) {
            labels[columns[j] = j] = new Label();
        }
        mw3.visitVarInsn(21, 2);
        mw3.visitLookupSwitchInsn(dflt, columns, labels);
        for (int j = 0; j < labels.length; ++j) {
            mw3.visitLabel(labels[j]);
            final FieldReader fieldReader = fieldReaderArray[j];
            final Field field = fieldReader.field;
            final Class fieldClass = fieldReader.fieldClass;
            final Type fieldType = fieldReader.fieldType;
            mw3.visitVarInsn(25, 0);
            mw3.visitFieldInsn(180, classNameType, "object", DESC_OBJECT);
            String DESC_FIELD_CLASS;
            String DESC_METHOD;
            if (fieldType == Integer.class || fieldType == Integer.TYPE || fieldType == Short.class || fieldType == Short.TYPE || fieldType == Byte.class || fieldType == Byte.TYPE) {
                mw3.visitVarInsn(25, 3);
                mw3.visitVarInsn(21, 4);
                mw3.visitVarInsn(21, 5);
                mw3.visitMethodInsn(184, ASMUtils.TYPE_TYPE_UTILS, "parseInt", bytes ? "([BII)I" : "([CII)I", false);
                if (fieldType == Short.TYPE) {
                    DESC_FIELD_CLASS = "S";
                    DESC_METHOD = "(S)V";
                }
                else if (fieldType == Short.class) {
                    mw3.visitMethodInsn(184, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
                    DESC_FIELD_CLASS = "Ljava/lang/Short;";
                    DESC_METHOD = "(Ljava/lang/Short;)V";
                }
                else if (fieldType == Byte.TYPE) {
                    DESC_FIELD_CLASS = "B";
                    DESC_METHOD = "(B)V";
                }
                else if (fieldType == Byte.class) {
                    mw3.visitMethodInsn(184, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
                    DESC_FIELD_CLASS = "Ljava/lang/Byte;";
                    DESC_METHOD = "(Ljava/lang/Byte;)V";
                }
                else if (fieldType == Integer.TYPE) {
                    DESC_FIELD_CLASS = "I";
                    DESC_METHOD = "(I)V";
                }
                else {
                    mw3.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
                    DESC_FIELD_CLASS = "Ljava/lang/Integer;";
                    DESC_METHOD = "(Ljava/lang/Integer;)V";
                }
            }
            else if (fieldType == Long.class || fieldType == Long.TYPE) {
                mw3.visitVarInsn(25, 3);
                mw3.visitVarInsn(21, 4);
                mw3.visitVarInsn(21, 5);
                mw3.visitMethodInsn(184, ASMUtils.TYPE_TYPE_UTILS, "parseLong", bytes ? "([BII)J" : "([CII)J", false);
                if (fieldType == Long.TYPE) {
                    DESC_FIELD_CLASS = "J";
                    DESC_METHOD = "(J)V";
                }
                else {
                    mw3.visitMethodInsn(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
                    DESC_FIELD_CLASS = "Ljava/lang/Long;";
                    DESC_METHOD = "(Ljava/lang/Long;)V";
                }
            }
            else if (fieldType == Float.class || fieldType == Float.TYPE) {
                mw3.visitVarInsn(25, 3);
                mw3.visitVarInsn(21, 4);
                mw3.visitVarInsn(21, 5);
                mw3.visitMethodInsn(184, ASMUtils.TYPE_TYPE_UTILS, "parseFloat", bytes ? "([BII)F" : "([CII)F", false);
                if (fieldType == Float.TYPE) {
                    DESC_FIELD_CLASS = "F";
                    DESC_METHOD = "(F)V";
                }
                else {
                    mw3.visitMethodInsn(184, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
                    DESC_FIELD_CLASS = "Ljava/lang/Float;";
                    DESC_METHOD = "(Ljava/lang/Float;)V";
                }
            }
            else if (fieldType == Double.class || fieldType == Double.TYPE) {
                mw3.visitVarInsn(25, 3);
                mw3.visitVarInsn(21, 4);
                mw3.visitVarInsn(21, 5);
                mw3.visitMethodInsn(184, ASMUtils.TYPE_TYPE_UTILS, "parseDouble", bytes ? "([BII)D" : "([CII)D", false);
                if (fieldType == Double.TYPE) {
                    DESC_FIELD_CLASS = "D";
                    DESC_METHOD = "(D)V";
                }
                else {
                    mw3.visitMethodInsn(184, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
                    DESC_FIELD_CLASS = "Ljava/lang/Double;";
                    DESC_METHOD = "(Ljava/lang/Double;)V";
                }
            }
            else if (fieldType == Boolean.class || fieldType == Boolean.TYPE) {
                mw3.visitVarInsn(25, 3);
                mw3.visitVarInsn(21, 4);
                mw3.visitVarInsn(21, 5);
                mw3.visitMethodInsn(184, ASMUtils.TYPE_TYPE_UTILS, "parseBoolean", bytes ? "([BII)Ljava/lang/Boolean;" : "([CII)Ljava/lang/Boolean;", false);
                if (fieldType == Boolean.TYPE) {
                    mw3.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z", false);
                    DESC_FIELD_CLASS = "Z";
                    DESC_METHOD = "(Z)V";
                }
                else {
                    DESC_FIELD_CLASS = "Ljava/lang/Boolean;";
                    DESC_METHOD = "(Ljava/lang/Boolean;)V";
                }
            }
            else if (fieldType == Date.class) {
                mw3.visitTypeInsn(187, "java/util/Date");
                mw3.visitInsn(89);
                mw3.visitVarInsn(25, 3);
                mw3.visitVarInsn(21, 4);
                mw3.visitVarInsn(21, 5);
                if (bytes) {
                    mw3.visitVarInsn(25, 6);
                    mw3.visitMethodInsn(184, ASMUtils.TYPE_DATE_UTILS, "parseMillis", "([BIILjava/nio/charset/Charset;)J", false);
                }
                else {
                    mw3.visitMethodInsn(184, ASMUtils.TYPE_DATE_UTILS, "parseMillis", "([CII)J", false);
                }
                mw3.visitMethodInsn(183, "java/util/Date", "<init>", "(J)V", false);
                DESC_FIELD_CLASS = "Ljava/util/Date;";
                DESC_METHOD = "(Ljava/util/Date;)V";
            }
            else if (fieldType == BigDecimal.class) {
                mw3.visitVarInsn(25, 3);
                mw3.visitVarInsn(21, 4);
                mw3.visitVarInsn(21, 5);
                mw3.visitMethodInsn(184, ASMUtils.TYPE_TYPE_UTILS, "parseBigDecimal", bytes ? "([BII)Ljava/math/BigDecimal;" : "([CII)Ljava/math/BigDecimal;", false);
                DESC_FIELD_CLASS = "Ljava/math/BigDecimal;";
                DESC_METHOD = "(Ljava/math/BigDecimal;)V";
            }
            else {
                mw3.visitTypeInsn(187, "java/lang/String");
                mw3.visitInsn(89);
                mw3.visitVarInsn(25, 3);
                mw3.visitVarInsn(21, 4);
                mw3.visitVarInsn(21, 5);
                if (bytes) {
                    mw3.visitVarInsn(25, 6);
                    mw3.visitMethodInsn(183, "java/lang/String", "<init>", "([BIILjava/nio/charset/Charset;)V", false);
                }
                else {
                    mw3.visitMethodInsn(183, "java/lang/String", "<init>", "([CII)V", false);
                }
                if (fieldType == String.class) {
                    DESC_FIELD_CLASS = "Ljava/lang/String;";
                    DESC_METHOD = "(Ljava/lang/String;)V";
                }
                else {
                    DESC_FIELD_CLASS = ASMUtils.desc(fieldClass);
                    if (fieldClass == Character.TYPE) {
                        DESC_METHOD = "(C)V";
                    }
                    else {
                        DESC_METHOD = "(" + DESC_FIELD_CLASS + ")V";
                    }
                    mw3.visitLdcInsn(fieldClass);
                    mw3.visitMethodInsn(184, ASMUtils.TYPE_TYPE_UTILS, "cast", "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;", false);
                    mw3.visitTypeInsn(192, ASMUtils.type(fieldClass));
                }
            }
            if (fieldReader.method != null) {
                if (fieldReader.method.getReturnType() != Void.TYPE) {
                    return null;
                }
                mw3.visitMethodInsn(182, TYPE_OBJECT, fieldReader.method.getName(), DESC_METHOD, false);
            }
            else {
                if (field == null) {
                    return null;
                }
                mw3.visitFieldInsn(181, TYPE_OBJECT, field.getName(), DESC_FIELD_CLASS);
            }
            mw3.visitJumpInsn(167, dflt);
        }
        mw3.visitLabel(dflt);
        mw3.visitInsn(177);
        mw3.visitMaxs(3, 3);
        final byte[] code = cw.toByteArray();
        try {
            final Class<?> consumerClass = this.classLoader.defineClassPublic(classNameFull, code, 0, code.length);
            final Constructor<?> constructor = consumerClass.getConstructor(Consumer.class);
            final Constructor<Object> constructor2;
            final ReflectiveOperationException ex;
            ReflectiveOperationException e2;
            return c -> {
                try {
                    return constructor2.newInstance(c);
                }
                catch (InstantiationException | IllegalAccessException | InvocationTargetException ex2) {
                    e2 = ex;
                    throw new JSONException("create ByteArrayValueConsumer error", e2);
                }
            };
        }
        catch (Throwable e3) {
            e3.printStackTrace();
            return null;
        }
    }
    
    static {
        INSTANCE = new ObjectReaderCreatorASM(DynamicClassLoader.getInstance());
        seed = new AtomicLong();
        METHOD_DESC_GET_ITEM_OBJECT_READER = "(" + ASMUtils.DESC_JSON_READER + ")" + ASMUtils.DESC_OBJECT_READER;
        METHOD_DESC_GET_OBJECT_READER_1 = "(" + ASMUtils.DESC_JSON_READER + ")" + ASMUtils.DESC_OBJECT_READER;
        METHOD_DESC_INIT = "(Ljava/lang/Class;Ljava/util/function/Supplier;" + ASMUtils.DESC_FIELD_READER_ARRAY + ")V";
        METHOD_DESC_ADAPTER_INIT = "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;J" + ASMUtils.DESC_JSONSCHEMA + "Ljava/util/function/Supplier;" + "Ljava/util/function/Function;" + ASMUtils.DESC_FIELD_READER_ARRAY + ")V";
        METHOD_DESC_READ_OBJECT = "(" + ASMUtils.DESC_JSON_READER + "Ljava/lang/reflect/Type;Ljava/lang/Object;J)Ljava/lang/Object;";
        METHOD_DESC_GET_FIELD_READER = "(J)" + ASMUtils.DESC_FIELD_READER;
        METHOD_DESC_READ_FIELD_VALUE = "(" + ASMUtils.DESC_JSON_READER + "Ljava/lang/Object;)V";
        METHOD_DESC_ADD_RESOLVE_TASK = "(" + ASMUtils.DESC_JSON_READER + "Ljava/lang/Object;Ljava/lang/String;)V";
        METHOD_DESC_ADD_RESOLVE_TASK_2 = "(" + ASMUtils.DESC_JSON_READER + "Ljava/util/List;ILjava/lang/String;)V";
        METHOD_DESC_CHECK_ARRAY_AUTO_TYPE = "(" + ASMUtils.DESC_JSON_READER + ")" + ASMUtils.DESC_OBJECT_READER;
        METHOD_DESC_PROCESS_EXTRA = "(" + ASMUtils.DESC_JSON_READER + "Ljava/lang/Object;)V";
        METHOD_DESC_JSON_READER_CHECK_ARRAY_AUTO_TYPE = "(" + ASMUtils.DESC_JSON_READER + "Ljava/lang/Class;J)" + ASMUtils.DESC_OBJECT_READER;
        infos = new HashMap<Class, FieldReaderInfo>();
        final Package pkg = ObjectReaderCreatorASM.class.getPackage();
        packageName = ((pkg != null) ? pkg.getName() : "");
        ObjectReaderCreatorASM.infos.put(Boolean.TYPE, new FieldReaderInfo(ASMUtils.type(ObjBoolConsumer.class), "(Ljava/lang/Object;Z)V", "(Z)V", 21, "readFieldBoolValue", "()Z", 54));
        ObjectReaderCreatorASM.infos.put(Character.TYPE, new FieldReaderInfo(ASMUtils.type(ObjCharConsumer.class), "(Ljava/lang/Object;C)V", "(C)V", 21, "readInt32Value", "()C", 54));
        ObjectReaderCreatorASM.infos.put(Byte.TYPE, new FieldReaderInfo(ASMUtils.type(ObjByteConsumer.class), "(Ljava/lang/Object;B)V", "(B)V", 21, "readInt32Value", "()B", 54));
        ObjectReaderCreatorASM.infos.put(Short.TYPE, new FieldReaderInfo(ASMUtils.type(ObjShortConsumer.class), "(Ljava/lang/Object;S)V", "(S)V", 21, "readInt32Value", "()S", 54));
        ObjectReaderCreatorASM.infos.put(Integer.TYPE, new FieldReaderInfo(ASMUtils.type(ObjIntConsumer.class), "(Ljava/lang/Object;I)V", "(I)V", 21, "readInt32Value", "()I", 54));
        ObjectReaderCreatorASM.infos.put(Long.TYPE, new FieldReaderInfo(ASMUtils.type(ObjLongConsumer.class), "(Ljava/lang/Object;J)V", "(J)V", 22, "readInt64Value", "()V", 55));
        ObjectReaderCreatorASM.infos.put(Float.TYPE, new FieldReaderInfo(ASMUtils.type(ObjFloatConsumer.class), "(Ljava/lang/Object;F)V", "(F)V", 23, "readFieldFloatValue", "()F", 56));
        ObjectReaderCreatorASM.infos.put(Double.TYPE, new FieldReaderInfo(ASMUtils.type(ObjDoubleConsumer.class), "(Ljava/lang/Object;D)V", "(D)V", 24, "readFloatDoubleValue", "()D", 57));
        ObjectReaderCreatorASM.infos.put(String.class, new FieldReaderInfo(ASMUtils.type(BiConsumer.class), "(Ljava/lang/Object;Ljava/lang/Object;)V", "(Ljava/lang/String;)V", 25, "readString", "()Ljava/lang/String;", 58));
        ObjectReaderCreatorASM.infos.put(Integer.class, new FieldReaderInfo(ASMUtils.type(BiConsumer.class), "(Ljava/lang/Object;Ljava/lang/Integer;)V", "(Ljava/lang/Integer;)V", 25, "readInt32", "()Ljava/lang/Integer;", 58));
        fieldItemObjectReader = new String[1024];
    }
    
    private static class FieldReaderInfo
    {
        final String interfaceDesc;
        final String acceptDesc;
        final String setterDesc;
        final int loadCode;
        final String readMethodName;
        final String readMethodDesc;
        final int storeCode;
        
        FieldReaderInfo(final String interfaceDesc, final String acceptDesc, final String setterDesc, final int loadCode, final String readMethodName, final String readMethodDesc, final int storeCode) {
            this.interfaceDesc = interfaceDesc;
            this.acceptDesc = acceptDesc;
            this.setterDesc = setterDesc;
            this.loadCode = loadCode;
            this.readMethodName = readMethodName;
            this.readMethodDesc = readMethodDesc;
            this.storeCode = storeCode;
        }
    }
    
    static class ObjectWriteContext
    {
        final Class objectClass;
        final ClassWriter cw;
        final boolean publicClass;
        final boolean externalClass;
        final FieldReader[] fieldReaders;
        final boolean hasStringField;
        
        public ObjectWriteContext(final Class objectClass, final ClassWriter cw, final boolean externalClass, final FieldReader[] fieldReaders) {
            this.objectClass = objectClass;
            this.cw = cw;
            this.publicClass = (objectClass == null || Modifier.isPublic(objectClass.getModifiers()));
            this.externalClass = externalClass;
            this.fieldReaders = fieldReaders;
            boolean hasStringField = false;
            for (final FieldReader fieldReader : fieldReaders) {
                if (fieldReader.fieldClass == String.class) {
                    hasStringField = true;
                    break;
                }
            }
            this.hasStringField = hasStringField;
        }
    }
}
