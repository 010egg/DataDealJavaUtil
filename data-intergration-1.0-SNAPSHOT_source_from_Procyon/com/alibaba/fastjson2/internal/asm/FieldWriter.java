// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.internal.asm;

public final class FieldWriter
{
    FieldWriter fv;
    private final int accessFlags;
    private final int nameIndex;
    private final int descriptorIndex;
    
    FieldWriter(final SymbolTable symbolTable, final int access, final String name, final String descriptor) {
        this.accessFlags = access;
        this.nameIndex = symbolTable.addConstantUtf8(name);
        this.descriptorIndex = symbolTable.addConstantUtf8(descriptor);
    }
    
    void putFieldInfo(final ByteVector output) {
        final int mask = 0;
        output.putShort(this.accessFlags & ~mask).putShort(this.nameIndex).putShort(this.descriptorIndex);
        final int attributesCount = 0;
        output.putShort(attributesCount);
    }
}
