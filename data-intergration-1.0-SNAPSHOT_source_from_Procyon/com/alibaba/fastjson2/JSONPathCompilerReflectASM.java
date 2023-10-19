// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import com.alibaba.fastjson2.internal.asm.MethodWriter;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.util.function.Function;
import com.alibaba.fastjson2.internal.asm.ClassWriter;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.internal.asm.ASMUtils;
import java.lang.reflect.Modifier;
import com.alibaba.fastjson2.util.DynamicClassLoader;
import java.util.concurrent.atomic.AtomicLong;

class JSONPathCompilerReflectASM extends JSONPathCompilerReflect
{
    static final AtomicLong seed;
    static final JSONPathCompilerReflectASM INSTANCE;
    static final String DESC_OBJECT_READER;
    static final String DESC_FIELD_READER;
    static final String DESC_OBJECT_WRITER;
    static final String DESC_FIELD_WRITER;
    static final String TYPE_SINGLE_NAME_PATH_TYPED;
    static final String METHOD_SINGLE_NAME_PATH_TYPED_INIT;
    static final int THIS = 0;
    protected final DynamicClassLoader classLoader;
    
    public JSONPathCompilerReflectASM(final DynamicClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    private boolean support(final Class objectClass) {
        final boolean externalClass = this.classLoader.isExternalClass(objectClass);
        final int objectClassModifiers = objectClass.getModifiers();
        return Modifier.isAbstract(objectClassModifiers) || Modifier.isInterface(objectClassModifiers) || !Modifier.isPublic(objectClassModifiers) || externalClass;
    }
    
    @Override
    protected JSONPath compileSingleNamePath(final Class objectClass, final JSONPathSingleName path) {
        if (this.support(objectClass)) {
            return super.compileSingleNamePath(objectClass, path);
        }
        final String fieldName = path.name;
        final String TYPE_OBJECT = ASMUtils.type(objectClass);
        final ObjectReader objectReader = path.getReaderContext().getObjectReader(objectClass);
        final FieldReader fieldReader = objectReader.getFieldReader(fieldName);
        final ObjectWriter objectWriter = path.getWriterContext().getObjectWriter((Class<Object>)objectClass);
        final FieldWriter fieldWriter = objectWriter.getFieldWriter(fieldName);
        final ClassWriter cw = new ClassWriter(null);
        final String className = "JSONPath_" + JSONPathCompilerReflectASM.seed.incrementAndGet();
        final Package pkg = JSONPathCompilerReflectASM.class.getPackage();
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
        cw.visit(52, 49, classNameType, JSONPathCompilerReflectASM.TYPE_SINGLE_NAME_PATH_TYPED, new String[0]);
        final int PATH = 1;
        final int CLASS = 2;
        final int OBJECT_READER = 3;
        final int FIELD_READER = 4;
        final int OBJECT_WRITER = 5;
        final int FIELD_WRITER = 6;
        final MethodWriter mw = cw.visitMethod(1, "<init>", JSONPathCompilerReflectASM.METHOD_SINGLE_NAME_PATH_TYPED_INIT, 64);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitVarInsn(25, 3);
        mw.visitVarInsn(25, 4);
        mw.visitVarInsn(25, 5);
        mw.visitVarInsn(25, 6);
        mw.visitMethodInsn(183, JSONPathCompilerReflectASM.TYPE_SINGLE_NAME_PATH_TYPED, "<init>", JSONPathCompilerReflectASM.METHOD_SINGLE_NAME_PATH_TYPED_INIT, false);
        mw.visitInsn(177);
        mw.visitMaxs(3, 3);
        if (fieldReader != null) {
            final Class fieldClass = fieldReader.fieldClass;
            final int OBJECT = 1;
            final int VALUE = 2;
            if (fieldClass == Integer.TYPE) {
                final MethodWriter mw2 = cw.visitMethod(1, "setInt", "(Ljava/lang/Object;I)V", 64);
                mw2.visitVarInsn(25, OBJECT);
                mw2.visitTypeInsn(192, TYPE_OBJECT);
                mw2.visitVarInsn(21, VALUE);
                this.gwSetValue(mw2, TYPE_OBJECT, fieldReader);
                mw2.visitInsn(177);
                mw2.visitMaxs(2, 2);
            }
            if (fieldClass == Long.TYPE) {
                final MethodWriter mw2 = cw.visitMethod(1, "setLong", "(Ljava/lang/Object;J)V", 64);
                mw2.visitVarInsn(25, OBJECT);
                mw2.visitTypeInsn(192, TYPE_OBJECT);
                mw2.visitVarInsn(22, VALUE);
                this.gwSetValue(mw2, TYPE_OBJECT, fieldReader);
                mw2.visitInsn(177);
                mw2.visitMaxs(2, 2);
            }
            final MethodWriter mw2 = cw.visitMethod(1, "set", "(Ljava/lang/Object;Ljava/lang/Object;)V", 64);
            mw2.visitVarInsn(25, OBJECT);
            mw2.visitTypeInsn(192, TYPE_OBJECT);
            mw2.visitVarInsn(25, VALUE);
            if (fieldClass == Integer.TYPE) {
                mw2.visitTypeInsn(192, "java/lang/Number");
                mw2.visitMethodInsn(182, "java/lang/Number", "intValue", "()I", false);
            }
            else if (fieldClass == Long.TYPE) {
                mw2.visitTypeInsn(192, "java/lang/Number");
                mw2.visitMethodInsn(182, "java/lang/Number", "longValue", "()J", false);
            }
            else if (fieldClass == Float.TYPE) {
                mw2.visitTypeInsn(192, "java/lang/Number");
                mw2.visitMethodInsn(182, "java/lang/Number", "floatValue", "()F", false);
            }
            else if (fieldClass == Double.TYPE) {
                mw2.visitTypeInsn(192, "java/lang/Number");
                mw2.visitMethodInsn(182, "java/lang/Number", "doubleValue", "()D", false);
            }
            else if (fieldClass == Short.TYPE) {
                mw2.visitTypeInsn(192, "java/lang/Number");
                mw2.visitMethodInsn(182, "java/lang/Number", "shortValue", "()S", false);
            }
            else if (fieldClass == Byte.TYPE) {
                mw2.visitTypeInsn(192, "java/lang/Number");
                mw2.visitMethodInsn(182, "java/lang/Number", "byteValue", "()B", false);
            }
            else if (fieldClass == Boolean.TYPE) {
                mw2.visitTypeInsn(192, "java/lang/Boolean");
                mw2.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z", false);
            }
            else if (fieldClass == Character.TYPE) {
                mw2.visitTypeInsn(192, "java/lang/Character");
                mw2.visitMethodInsn(182, "java/lang/Character", "charValue", "()C", false);
            }
            this.gwSetValue(mw2, TYPE_OBJECT, fieldReader);
            mw2.visitInsn(177);
            mw2.visitMaxs(2, 2);
        }
        if (fieldWriter != null) {
            final Class fieldClass = fieldReader.fieldClass;
            final int OBJECT = 1;
            final MethodWriter mw3 = cw.visitMethod(1, "eval", "(Ljava/lang/Object;)Ljava/lang/Object;", 64);
            mw3.visitVarInsn(25, OBJECT);
            mw3.visitTypeInsn(192, TYPE_OBJECT);
            this.gwGetValue(mw3, TYPE_OBJECT, fieldWriter);
            if (fieldClass == Integer.TYPE) {
                mw3.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
            }
            else if (fieldClass == Long.TYPE) {
                mw3.visitMethodInsn(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
            }
            else if (fieldClass == Float.TYPE) {
                mw3.visitMethodInsn(184, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
            }
            else if (fieldClass == Double.TYPE) {
                mw3.visitMethodInsn(184, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
            }
            else if (fieldClass == Short.TYPE) {
                mw3.visitMethodInsn(184, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
            }
            else if (fieldClass == Byte.TYPE) {
                mw3.visitMethodInsn(184, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
            }
            else if (fieldClass == Boolean.TYPE) {
                mw3.visitMethodInsn(184, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
            }
            else if (fieldClass == Character.TYPE) {
                mw3.visitMethodInsn(184, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
            }
            mw3.visitInsn(176);
            mw3.visitMaxs(2, 2);
        }
        final byte[] code = cw.toByteArray();
        final Class<?> readerClass = this.classLoader.defineClassPublic(classNameFull, code, 0, code.length);
        try {
            final Constructor<?> constructor = readerClass.getConstructors()[0];
            return (JSONPath)constructor.newInstance(path.path, objectClass, objectReader, fieldReader, objectWriter, fieldWriter);
        }
        catch (Throwable e) {
            throw new JSONException("compile jsonpath error, path " + path.path + ", objectType " + objectClass.getTypeName(), e);
        }
    }
    
    private void gwSetValue(final MethodWriter mw, final String TYPE_OBJECT, final FieldReader fieldReader) {
        final Method method = fieldReader.method;
        final Field field = fieldReader.field;
        final Class fieldClass = fieldReader.fieldClass;
        final String fieldClassDesc = ASMUtils.desc(fieldClass);
        if (method != null) {
            final Class<?> returnType = method.getReturnType();
            final String methodDesc = '(' + fieldClassDesc + ')' + ASMUtils.desc(returnType);
            mw.visitMethodInsn(182, TYPE_OBJECT, method.getName(), methodDesc, false);
            if (returnType != Void.TYPE) {
                mw.visitInsn(87);
            }
        }
        else {
            mw.visitFieldInsn(181, TYPE_OBJECT, field.getName(), fieldClassDesc);
        }
    }
    
    private void gwGetValue(final MethodWriter mw, final String TYPE_OBJECT, final FieldWriter fieldWriter) {
        final Method method = fieldWriter.method;
        final Field field = fieldWriter.field;
        final Class fieldClass = fieldWriter.fieldClass;
        final String fieldClassDesc = ASMUtils.desc(fieldClass);
        if (method != null) {
            final String methodDesc = "()" + fieldClassDesc;
            mw.visitMethodInsn(182, TYPE_OBJECT, method.getName(), methodDesc, false);
        }
        else {
            mw.visitFieldInsn(180, TYPE_OBJECT, field.getName(), fieldClassDesc);
        }
    }
    
    static {
        seed = new AtomicLong();
        INSTANCE = new JSONPathCompilerReflectASM(DynamicClassLoader.getInstance());
        DESC_OBJECT_READER = ASMUtils.desc(ObjectReader.class);
        DESC_FIELD_READER = ASMUtils.desc(FieldReader.class);
        DESC_OBJECT_WRITER = ASMUtils.desc(ObjectWriter.class);
        DESC_FIELD_WRITER = ASMUtils.desc(FieldWriter.class);
        TYPE_SINGLE_NAME_PATH_TYPED = ASMUtils.type(SingleNamePathTyped.class);
        METHOD_SINGLE_NAME_PATH_TYPED_INIT = "(Ljava/lang/String;Ljava/lang/Class;" + JSONPathCompilerReflectASM.DESC_OBJECT_READER + JSONPathCompilerReflectASM.DESC_FIELD_READER + JSONPathCompilerReflectASM.DESC_OBJECT_WRITER + JSONPathCompilerReflectASM.DESC_FIELD_WRITER + ")V";
    }
}
