// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.internal.asm;

import com.alibaba.fastjson2.util.TypeUtils;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson2.JSONException;
import java.util.function.Function;

public class ClassWriter
{
    private final Function<String, Class> typeProvider;
    private int version;
    private final SymbolTable symbolTable;
    private int accessFlags;
    private int thisClass;
    private int superClass;
    private int interfaceCount;
    private int[] interfaces;
    private FieldWriter firstField;
    private FieldWriter lastField;
    private MethodWriter firstMethod;
    private MethodWriter lastMethod;
    
    public ClassWriter(final Function<String, Class> typeProvider) {
        this.symbolTable = new SymbolTable(this);
        this.typeProvider = typeProvider;
    }
    
    public final void visit(final int version, final int access, final String name, final String superName, final String[] interfaces) {
        this.version = version;
        this.accessFlags = access;
        this.thisClass = this.symbolTable.setMajorVersionAndClassName(version & 0xFFFF, name);
        this.superClass = ((superName == null) ? 0 : this.symbolTable.addConstantUtf8Reference(7, superName).index);
        if (interfaces != null && interfaces.length > 0) {
            this.interfaceCount = interfaces.length;
            this.interfaces = new int[this.interfaceCount];
            for (int i = 0; i < this.interfaceCount; ++i) {
                this.interfaces[i] = this.symbolTable.addConstantUtf8Reference(7, interfaces[i]).index;
            }
        }
    }
    
    public final FieldWriter visitField(final int access, final String name, final String descriptor) {
        final FieldWriter fieldWriter = new FieldWriter(this.symbolTable, access, name, descriptor);
        if (this.firstField == null) {
            this.firstField = fieldWriter;
        }
        else {
            this.lastField.fv = fieldWriter;
        }
        return this.lastField = fieldWriter;
    }
    
    public final MethodWriter visitMethod(final int access, final String name, final String descriptor, final int codeInitCapacity) {
        final MethodWriter methodWriter = new MethodWriter(this.symbolTable, access, name, descriptor, codeInitCapacity);
        if (this.firstMethod == null) {
            this.firstMethod = methodWriter;
        }
        else {
            this.lastMethod.mv = methodWriter;
        }
        return this.lastMethod = methodWriter;
    }
    
    public byte[] toByteArray() {
        int size = 24 + 2 * this.interfaceCount;
        int fieldsCount = 0;
        for (FieldWriter fieldWriter = this.firstField; fieldWriter != null; fieldWriter = fieldWriter.fv) {
            ++fieldsCount;
            size += 8;
        }
        int methodsCount = 0;
        for (MethodWriter methodWriter = this.firstMethod; methodWriter != null; methodWriter = methodWriter.mv) {
            ++methodsCount;
            size += methodWriter.computeMethodInfoSize();
        }
        final int attributesCount = 0;
        size += this.symbolTable.constantPool.length;
        final int constantPoolCount = this.symbolTable.constantPoolCount;
        if (constantPoolCount > 65535) {
            throw new JSONException("Class too large: " + this.symbolTable.className + ", constantPoolCount " + constantPoolCount);
        }
        final ByteVector result = new ByteVector(size);
        result.putInt(-889275714).putInt(this.version);
        result.putShort(constantPoolCount).putByteArray(this.symbolTable.constantPool.data, 0, this.symbolTable.constantPool.length);
        final int mask = 0;
        result.putShort(this.accessFlags & ~mask).putShort(this.thisClass).putShort(this.superClass);
        result.putShort(this.interfaceCount);
        for (int i = 0; i < this.interfaceCount; ++i) {
            result.putShort(this.interfaces[i]);
        }
        result.putShort(fieldsCount);
        for (FieldWriter fieldWriter = this.firstField; fieldWriter != null; fieldWriter = fieldWriter.fv) {
            fieldWriter.putFieldInfo(result);
        }
        result.putShort(methodsCount);
        boolean hasFrames = false;
        boolean hasAsmInstructions = false;
        for (MethodWriter methodWriter = this.firstMethod; methodWriter != null; methodWriter = methodWriter.mv) {
            hasFrames |= (methodWriter.stackMapTableNumberOfEntries > 0);
            hasAsmInstructions |= methodWriter.hasAsmInstructions;
            methodWriter.putMethodInfo(result);
        }
        result.putShort(attributesCount);
        if (hasAsmInstructions) {
            throw new UnsupportedOperationException();
        }
        return result.data;
    }
    
    protected Class loadClass(final String type) {
        switch (type) {
            case "java/util/List": {
                return List.class;
            }
            case "java/util/ArrayList": {
                return ArrayList.class;
            }
            case "java/lang/Object": {
                return Object.class;
            }
            default: {
                final String className1 = type.replace('/', '.');
                Class clazz = null;
                if (this.typeProvider != null) {
                    clazz = this.typeProvider.apply(className1);
                }
                if (clazz == null) {
                    clazz = TypeUtils.loadClass(className1);
                }
                return clazz;
            }
        }
    }
    
    protected String getCommonSuperClass(final String type1, final String type2) {
        Class<?> class1 = (Class<?>)this.loadClass(type1);
        if (class1 == null) {
            throw new JSONException("class not found " + type1);
        }
        final Class<?> class2 = (Class<?>)this.loadClass(type2);
        if (class2 == null) {
            return "java/lang/Object";
        }
        if (class1.isAssignableFrom(class2)) {
            return type1;
        }
        if (class2.isAssignableFrom(class1)) {
            return type2;
        }
        if (class1.isInterface() || class2.isInterface()) {
            return "java/lang/Object";
        }
        do {
            class1 = class1.getSuperclass();
        } while (!class1.isAssignableFrom(class2));
        return class1.getName().replace('.', '/');
    }
}
