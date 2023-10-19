// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.internal.asm;

import com.alibaba.fastjson2.JSONException;

public final class MethodWriter
{
    MethodWriter mv;
    private final SymbolTable symbolTable;
    private final int accessFlags;
    private final int nameIndex;
    private final String name;
    private final int descriptorIndex;
    private final String descriptor;
    private int maxStack;
    private int maxLocals;
    private final ByteVector code;
    int stackMapTableNumberOfEntries;
    private ByteVector stackMapTableEntries;
    private final Label firstBasicBlock;
    private Label lastBasicBlock;
    private Label currentBasicBlock;
    private int[] previousFrame;
    private int[] currentFrame;
    boolean hasAsmInstructions;
    private int lastBytecodeOffset;
    
    MethodWriter(final SymbolTable symbolTable, final int access, final String name, final String descriptor, final int codeInitCapacity) {
        this.symbolTable = symbolTable;
        this.accessFlags = ("<init>".equals(name) ? (access | 0x40000) : access);
        this.nameIndex = symbolTable.addConstantUtf8(name);
        this.name = name;
        this.descriptorIndex = symbolTable.addConstantUtf8(descriptor);
        this.descriptor = descriptor;
        this.code = new ByteVector(codeInitCapacity);
        int argumentsSize = Type.getArgumentsAndReturnSizes(descriptor) >> 2;
        if ((access & 0x8) != 0x0) {
            --argumentsSize;
        }
        this.maxLocals = argumentsSize;
        this.visitLabel(this.firstBasicBlock = new Label());
    }
    
    public void visitInsn(final int opcode) {
        this.lastBytecodeOffset = this.code.length;
        this.code.putByte(opcode);
        if (this.currentBasicBlock != null) {
            this.currentBasicBlock.frame.execute(opcode, 0, null, null);
            if ((opcode >= 172 && opcode <= 177) || opcode == 191) {
                this.endCurrentBasicBlockWithNoSuccessor();
            }
        }
    }
    
    public void visitIntInsn(final int opcode, final int operand) {
        this.lastBytecodeOffset = this.code.length;
        if (opcode == 17) {
            this.code.put12(opcode, operand);
        }
        else {
            this.code.put11(opcode, operand);
        }
        if (this.currentBasicBlock != null) {
            this.currentBasicBlock.frame.execute(opcode, operand, null, null);
        }
    }
    
    public void visitVarInsn(final int opcode, final int var) {
        this.lastBytecodeOffset = this.code.length;
        if (var < 4 && opcode != 169) {
            int optimizedOpcode;
            if (opcode < 54) {
                optimizedOpcode = 26 + (opcode - 21 << 2) + var;
            }
            else {
                optimizedOpcode = 59 + (opcode - 54 << 2) + var;
            }
            this.code.putByte(optimizedOpcode);
        }
        else if (var >= 256) {
            this.code.putByte(196).put12(opcode, var);
        }
        else {
            this.code.put11(opcode, var);
        }
        if (this.currentBasicBlock != null) {
            this.currentBasicBlock.frame.execute(opcode, var, null, null);
        }
        int currentMaxLocals;
        if (opcode == 22 || opcode == 24 || opcode == 55 || opcode == 57) {
            currentMaxLocals = var + 2;
        }
        else {
            currentMaxLocals = var + 1;
        }
        if (currentMaxLocals > this.maxLocals) {
            this.maxLocals = currentMaxLocals;
        }
    }
    
    public void visitTypeInsn(final int opcode, final String type) {
        this.lastBytecodeOffset = this.code.length;
        final Symbol typeSymbol = this.symbolTable.addConstantUtf8Reference(7, type);
        this.code.put12(opcode, typeSymbol.index);
        if (this.currentBasicBlock != null) {
            this.currentBasicBlock.frame.execute(opcode, this.lastBytecodeOffset, typeSymbol, this.symbolTable);
        }
    }
    
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String descriptor) {
        this.lastBytecodeOffset = this.code.length;
        final Symbol fieldrefSymbol = this.symbolTable.addConstantMemberReference(9, owner, name, descriptor);
        this.code.put12(opcode, fieldrefSymbol.index);
        if (this.currentBasicBlock != null) {
            this.currentBasicBlock.frame.execute(opcode, 0, fieldrefSymbol, this.symbolTable);
        }
    }
    
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String descriptor, final boolean isInterface) {
        this.lastBytecodeOffset = this.code.length;
        final Symbol methodrefSymbol = this.symbolTable.addConstantMemberReference(isInterface ? 11 : 10, owner, name, descriptor);
        if (opcode == 185) {
            this.code.put12(185, methodrefSymbol.index).put11(methodrefSymbol.getArgumentsAndReturnSizes() >> 2, 0);
        }
        else {
            this.code.put12(opcode, methodrefSymbol.index);
        }
        if (this.currentBasicBlock != null) {
            this.currentBasicBlock.frame.execute(opcode, 0, methodrefSymbol, this.symbolTable);
        }
    }
    
    public void visitJumpInsn(final int opcode, final Label label) {
        this.lastBytecodeOffset = this.code.length;
        final int baseOpcode = (opcode >= 200) ? (opcode - 33) : opcode;
        final boolean nextInsnIsJumpTarget = false;
        if ((label.flags & 0x4) != 0x0 && label.bytecodeOffset - this.code.length < -32768) {
            throw new JSONException("not supported");
        }
        if (baseOpcode != opcode) {
            this.code.putByte(opcode);
            label.put(this.code, this.code.length - 1, true);
        }
        else {
            this.code.putByte(baseOpcode);
            label.put(this.code, this.code.length - 1, false);
        }
        if (this.currentBasicBlock != null) {
            Label nextBasicBlock = null;
            this.currentBasicBlock.frame.execute(baseOpcode, 0, null, null);
            final Label canonicalInstance = label.getCanonicalInstance();
            canonicalInstance.flags |= 0x2;
            this.addSuccessorToCurrentBasicBlock(label);
            if (baseOpcode != 167) {
                nextBasicBlock = new Label();
            }
            if (nextBasicBlock != null) {
                this.visitLabel(nextBasicBlock);
            }
            if (baseOpcode == 167) {
                this.endCurrentBasicBlockWithNoSuccessor();
            }
        }
    }
    
    public void visitLabel(final Label label) {
        this.hasAsmInstructions |= label.resolve(this.code.data, this.code.length);
        if ((label.flags & 0x1) != 0x0) {
            return;
        }
        if (this.currentBasicBlock != null) {
            if (label.bytecodeOffset == this.currentBasicBlock.bytecodeOffset) {
                final Label currentBasicBlock = this.currentBasicBlock;
                currentBasicBlock.flags |= (short)(label.flags & 0x2);
                label.frame = this.currentBasicBlock.frame;
                return;
            }
            this.addSuccessorToCurrentBasicBlock(label);
        }
        if (this.lastBasicBlock != null) {
            if (label.bytecodeOffset == this.lastBasicBlock.bytecodeOffset) {
                final Label lastBasicBlock = this.lastBasicBlock;
                lastBasicBlock.flags |= (short)(label.flags & 0x2);
                label.frame = this.lastBasicBlock.frame;
                this.currentBasicBlock = this.lastBasicBlock;
                return;
            }
            this.lastBasicBlock.nextBasicBlock = label;
        }
        this.lastBasicBlock = label;
        this.currentBasicBlock = label;
        label.frame = new Frame(label);
    }
    
    public void visitLdcInsn(final String value) {
        final int CONSTANT_STRING_TAG = 8;
        this.lastBytecodeOffset = this.code.length;
        final Symbol constantSymbol = this.symbolTable.addConstantUtf8Reference(8, value);
        final int constantIndex = constantSymbol.index;
        if (constantIndex >= 256) {
            this.code.put12(19, constantIndex);
        }
        else {
            this.code.put11(18, constantIndex);
        }
        if (this.currentBasicBlock != null) {
            this.currentBasicBlock.frame.execute(18, 0, constantSymbol, this.symbolTable);
        }
    }
    
    public void visitLdcInsn(final Class value) {
        final String desc = ASMUtils.desc(value);
        final Type type = Type.getTypeInternal(desc, 0, desc.length());
        this.lastBytecodeOffset = this.code.length;
        final int typeSort = (type.sort == 12) ? 10 : type.sort;
        Symbol constantSymbol;
        if (typeSort == 10) {
            constantSymbol = this.symbolTable.addConstantUtf8Reference(7, type.valueBuffer.substring(type.valueBegin, type.valueEnd));
        }
        else {
            constantSymbol = this.symbolTable.addConstantUtf8Reference(7, type.getDescriptor());
        }
        final int constantIndex = constantSymbol.index;
        if (constantIndex >= 256) {
            this.code.put12(19, constantIndex);
        }
        else {
            this.code.put11(18, constantIndex);
        }
        if (this.currentBasicBlock != null) {
            this.currentBasicBlock.frame.execute(18, 0, constantSymbol, this.symbolTable);
        }
    }
    
    public void visitLdcInsn(final int value) {
        this.lastBytecodeOffset = this.code.length;
        final Symbol constantSymbol = this.symbolTable.addConstantIntegerOrFloat(value);
        final int constantIndex = constantSymbol.index;
        if (constantIndex >= 256) {
            this.code.put12(19, constantIndex);
        }
        else {
            this.code.put11(18, constantIndex);
        }
        if (this.currentBasicBlock != null) {
            this.currentBasicBlock.frame.execute(18, 0, constantSymbol, this.symbolTable);
        }
    }
    
    public void visitLdcInsn(final long value) {
        this.lastBytecodeOffset = this.code.length;
        final Symbol constantSymbol = this.symbolTable.addConstantLongOrDouble(value);
        final int constantIndex = constantSymbol.index;
        this.code.put12(20, constantIndex);
        if (this.currentBasicBlock != null) {
            this.currentBasicBlock.frame.execute(18, 0, constantSymbol, this.symbolTable);
        }
    }
    
    public void visitIincInsn(final int var, final int increment) {
        this.lastBytecodeOffset = this.code.length;
        if (var > 255 || increment > 127 || increment < -128) {
            this.code.putByte(196).put12(132, var).putShort(increment);
        }
        else {
            this.code.putByte(132).put11(var, increment);
        }
        if (this.currentBasicBlock != null) {
            this.currentBasicBlock.frame.execute(132, var, null, null);
        }
    }
    
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
        this.lastBytecodeOffset = this.code.length;
        this.code.putByte(171).putByteArray(null, 0, (4 - this.code.length % 4) % 4);
        dflt.put(this.code, this.lastBytecodeOffset, true);
        this.code.putInt(labels.length);
        for (int i = 0; i < labels.length; ++i) {
            this.code.putInt(keys[i]);
            labels[i].put(this.code, this.lastBytecodeOffset, true);
        }
        this.visitSwitchInsn(dflt, labels);
    }
    
    private void visitSwitchInsn(final Label dflt, final Label[] labels) {
        if (this.currentBasicBlock != null) {
            this.currentBasicBlock.frame.execute(171, 0, null, null);
            this.addSuccessorToCurrentBasicBlock(dflt);
            final Label canonicalInstance = dflt.getCanonicalInstance();
            canonicalInstance.flags |= 0x2;
            for (int i = 0; i < labels.length; ++i) {
                final Label label = labels[i];
                this.addSuccessorToCurrentBasicBlock(label);
                final Label canonicalInstance2 = label.getCanonicalInstance();
                canonicalInstance2.flags |= 0x2;
            }
            this.endCurrentBasicBlockWithNoSuccessor();
        }
    }
    
    public void visitMaxs(final int maxStack, final int maxLocals) {
        final Frame firstFrame = this.firstBasicBlock.frame;
        firstFrame.setInputFrameFromDescriptor(this.symbolTable, this.accessFlags, this.descriptor, this.maxLocals);
        firstFrame.accept(this);
        Label listOfBlocksToProcess = this.firstBasicBlock;
        listOfBlocksToProcess.nextListElement = Label.EMPTY_LIST;
        int maxStackSize = 0;
        while (listOfBlocksToProcess != Label.EMPTY_LIST) {
            final Label basicBlock = listOfBlocksToProcess;
            listOfBlocksToProcess = listOfBlocksToProcess.nextListElement;
            basicBlock.nextListElement = null;
            final Label label = basicBlock;
            label.flags |= 0x8;
            final int maxBlockStackSize = basicBlock.frame.inputStack.length + basicBlock.outputStackMax;
            if (maxBlockStackSize > maxStackSize) {
                maxStackSize = maxBlockStackSize;
            }
            for (Edge outgoingEdge = basicBlock.outgoingEdges; outgoingEdge != null; outgoingEdge = outgoingEdge.nextEdge) {
                final Label successorBlock = outgoingEdge.successor.getCanonicalInstance();
                final boolean successorBlockChanged = basicBlock.frame.merge(this.symbolTable, successorBlock.frame);
                if (successorBlockChanged && successorBlock.nextListElement == null) {
                    successorBlock.nextListElement = listOfBlocksToProcess;
                    listOfBlocksToProcess = successorBlock;
                }
            }
        }
        for (Label basicBlock = this.firstBasicBlock; basicBlock != null; basicBlock = basicBlock.nextBasicBlock) {
            if ((basicBlock.flags & 0xA) == 0xA) {
                basicBlock.frame.accept(this);
            }
            if ((basicBlock.flags & 0x8) == 0x0) {
                final Label nextBasicBlock = basicBlock.nextBasicBlock;
                final int startOffset = basicBlock.bytecodeOffset;
                final int endOffset = ((nextBasicBlock == null) ? this.code.length : nextBasicBlock.bytecodeOffset) - 1;
                if (endOffset >= startOffset) {
                    for (int i = startOffset; i < endOffset; ++i) {
                        this.code.data[i] = 0;
                    }
                    this.code.data[endOffset] = -65;
                    final int frameIndex = this.visitFrameStart(startOffset, 0, 1);
                    this.currentFrame[frameIndex] = (0x800000 | this.symbolTable.addType("java/lang/Throwable"));
                    this.visitFrameEnd();
                    maxStackSize = Math.max(maxStackSize, 1);
                }
            }
        }
        this.maxStack = maxStackSize;
    }
    
    private void addSuccessorToCurrentBasicBlock(final Label successor) {
        this.currentBasicBlock.outgoingEdges = new Edge(successor, this.currentBasicBlock.outgoingEdges);
    }
    
    private void endCurrentBasicBlockWithNoSuccessor() {
        final Label nextBasicBlock = new Label();
        nextBasicBlock.frame = new Frame(nextBasicBlock);
        nextBasicBlock.resolve(this.code.data, this.code.length);
        this.lastBasicBlock.nextBasicBlock = nextBasicBlock;
        this.lastBasicBlock = nextBasicBlock;
        this.currentBasicBlock = null;
    }
    
    int visitFrameStart(final int offset, final int numLocal, final int numStack) {
        final int frameLength = 3 + numLocal + numStack;
        if (this.currentFrame == null || this.currentFrame.length < frameLength) {
            this.currentFrame = new int[frameLength];
        }
        this.currentFrame[0] = offset;
        this.currentFrame[1] = numLocal;
        this.currentFrame[2] = numStack;
        return 3;
    }
    
    void visitAbstractType(final int frameIndex, final int abstractType) {
        this.currentFrame[frameIndex] = abstractType;
    }
    
    void visitFrameEnd() {
        if (this.previousFrame != null) {
            if (this.stackMapTableEntries == null) {
                this.stackMapTableEntries = new ByteVector(2048);
            }
            this.putFrame();
            ++this.stackMapTableNumberOfEntries;
        }
        this.previousFrame = this.currentFrame;
        this.currentFrame = null;
    }
    
    private void putFrame() {
        final int numLocal = this.currentFrame[1];
        final int numStack = this.currentFrame[2];
        final int offsetDelta = (this.stackMapTableNumberOfEntries == 0) ? this.currentFrame[0] : (this.currentFrame[0] - this.previousFrame[0] - 1);
        final int previousNumlocal = this.previousFrame[1];
        final int numLocalDelta = numLocal - previousNumlocal;
        int type = 255;
        if (numStack == 0) {
            switch (numLocalDelta) {
                case -3:
                case -2:
                case -1: {
                    type = 248;
                    break;
                }
                case 0: {
                    type = ((offsetDelta < 64) ? 0 : 251);
                    break;
                }
                case 1:
                case 2:
                case 3: {
                    type = 252;
                    break;
                }
            }
        }
        else if (numLocalDelta == 0 && numStack == 1) {
            type = ((offsetDelta < 63) ? 64 : 247);
        }
        if (type != 255) {
            int frameIndex = 3;
            for (int i = 0; i < previousNumlocal && i < numLocal; ++i) {
                if (this.currentFrame[frameIndex] != this.previousFrame[frameIndex]) {
                    type = 255;
                    break;
                }
                ++frameIndex;
            }
        }
        switch (type) {
            case 0: {
                this.stackMapTableEntries.putByte(offsetDelta);
                break;
            }
            case 64: {
                this.stackMapTableEntries.putByte(64 + offsetDelta);
                this.putAbstractTypes(3 + numLocal, 4 + numLocal);
                break;
            }
            case 247: {
                this.stackMapTableEntries.putByte(247).putShort(offsetDelta);
                this.putAbstractTypes(3 + numLocal, 4 + numLocal);
                break;
            }
            case 251: {
                this.stackMapTableEntries.putByte(251).putShort(offsetDelta);
                break;
            }
            case 248: {
                this.stackMapTableEntries.putByte(251 + numLocalDelta).putShort(offsetDelta);
                break;
            }
            case 252: {
                this.stackMapTableEntries.putByte(251 + numLocalDelta).putShort(offsetDelta);
                this.putAbstractTypes(3 + previousNumlocal, 3 + numLocal);
                break;
            }
            default: {
                this.stackMapTableEntries.putByte(255).putShort(offsetDelta).putShort(numLocal);
                this.putAbstractTypes(3, 3 + numLocal);
                this.stackMapTableEntries.putShort(numStack);
                this.putAbstractTypes(3 + numLocal, 3 + numLocal + numStack);
                break;
            }
        }
    }
    
    private void putAbstractTypes(final int start, final int end) {
        for (int i = start; i < end; ++i) {
            final int abstractType = this.currentFrame[i];
            final ByteVector output = this.stackMapTableEntries;
            int arrayDimensions = (abstractType & 0xFC000000) >> 26;
            if (arrayDimensions == 0) {
                final int typeValue = abstractType & 0xFFFFF;
                switch (abstractType & 0x3C00000) {
                    case 4194304: {
                        output.putByte(typeValue);
                        break;
                    }
                    case 8388608: {
                        output.putByte(7).putShort(this.symbolTable.addConstantUtf8Reference(7, this.symbolTable.typeTable[typeValue].value).index);
                        break;
                    }
                    case 12582912: {
                        output.putByte(8).putShort((int)this.symbolTable.typeTable[typeValue].data);
                        break;
                    }
                    default: {
                        throw new AssertionError();
                    }
                }
            }
            else {
                final StringBuilder typeDescriptor = new StringBuilder();
                while (arrayDimensions-- > 0) {
                    typeDescriptor.append('[');
                }
                if ((abstractType & 0x3C00000) == 0x800000) {
                    typeDescriptor.append('L').append(this.symbolTable.typeTable[abstractType & 0xFFFFF].value).append(';');
                }
                else {
                    switch (abstractType & 0xFFFFF) {
                        case 9: {
                            typeDescriptor.append('Z');
                            break;
                        }
                        case 10: {
                            typeDescriptor.append('B');
                            break;
                        }
                        case 11: {
                            typeDescriptor.append('C');
                            break;
                        }
                        case 12: {
                            typeDescriptor.append('S');
                            break;
                        }
                        case 1: {
                            typeDescriptor.append('I');
                            break;
                        }
                        case 2: {
                            typeDescriptor.append('F');
                            break;
                        }
                        case 4: {
                            typeDescriptor.append('J');
                            break;
                        }
                        case 3: {
                            typeDescriptor.append('D');
                            break;
                        }
                        default: {
                            throw new AssertionError();
                        }
                    }
                }
                output.putByte(7).putShort(this.symbolTable.addConstantUtf8Reference(7, typeDescriptor.toString()).index);
            }
        }
    }
    
    int computeMethodInfoSize() {
        int size = 8;
        if (this.code.length > 0) {
            if (this.code.length > 65535) {
                throw new JSONException("Method too large: " + this.symbolTable.className + "." + this.name + " " + this.descriptor + ", length " + this.code.length);
            }
            this.symbolTable.addConstantUtf8("Code");
            size += 16 + this.code.length + 2;
            if (this.stackMapTableEntries != null) {
                this.symbolTable.addConstantUtf8("StackMapTable");
                size += 8 + this.stackMapTableEntries.length;
            }
        }
        return size;
    }
    
    void putMethodInfo(final ByteVector output) {
        final int mask = 0;
        output.putShort(this.accessFlags & ~mask).putShort(this.nameIndex).putShort(this.descriptorIndex);
        int attributeCount = 0;
        if (this.code.length > 0) {
            ++attributeCount;
        }
        output.putShort(attributeCount);
        if (this.code.length > 0) {
            int size = 10 + this.code.length + 2;
            int codeAttributeCount = 0;
            if (this.stackMapTableEntries != null) {
                size += 8 + this.stackMapTableEntries.length;
                ++codeAttributeCount;
            }
            output.putShort(this.symbolTable.addConstantUtf8("Code")).putInt(size).putShort(this.maxStack).putShort(this.maxLocals).putInt(this.code.length).putByteArray(this.code.data, 0, this.code.length);
            output.putShort(0);
            output.putShort(codeAttributeCount);
            if (this.stackMapTableEntries != null) {
                final boolean useStackMapTable = true;
                output.putShort(this.symbolTable.addConstantUtf8("StackMapTable")).putInt(2 + this.stackMapTableEntries.length).putShort(this.stackMapTableNumberOfEntries).putByteArray(this.stackMapTableEntries.data, 0, this.stackMapTableEntries.length);
            }
        }
    }
}
