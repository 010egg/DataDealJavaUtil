// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.internal.asm;

public class Label
{
    static final int FLAG_DEBUG_ONLY = 1;
    static final int FLAG_JUMP_TARGET = 2;
    static final int FLAG_RESOLVED = 4;
    static final int FLAG_REACHABLE = 8;
    static final int FORWARD_REFERENCES_CAPACITY_INCREMENT = 6;
    static final int FORWARD_REFERENCE_TYPE_MASK = -268435456;
    static final int FORWARD_REFERENCE_TYPE_SHORT = 268435456;
    static final int FORWARD_REFERENCE_TYPE_WIDE = 536870912;
    static final int FORWARD_REFERENCE_HANDLE_MASK = 268435455;
    static final Label EMPTY_LIST;
    public Object info;
    short flags;
    int bytecodeOffset;
    private int[] forwardReferences;
    short outputStackMax;
    Frame frame;
    Label nextBasicBlock;
    Edge outgoingEdges;
    Label nextListElement;
    
    final Label getCanonicalInstance() {
        return (this.frame == null) ? this : this.frame.owner;
    }
    
    final void put(final ByteVector code, final int sourceInsnBytecodeOffset, final boolean wideReference) {
        if ((this.flags & 0x4) == 0x0) {
            if (wideReference) {
                this.addForwardReference(sourceInsnBytecodeOffset, 536870912, code.length);
                code.putInt(-1);
            }
            else {
                this.addForwardReference(sourceInsnBytecodeOffset, 268435456, code.length);
                code.putShort(-1);
            }
        }
        else if (wideReference) {
            code.putInt(this.bytecodeOffset - sourceInsnBytecodeOffset);
        }
        else {
            code.putShort(this.bytecodeOffset - sourceInsnBytecodeOffset);
        }
    }
    
    private void addForwardReference(final int sourceInsnBytecodeOffset, final int referenceType, final int referenceHandle) {
        if (this.forwardReferences == null) {
            this.forwardReferences = new int[6];
        }
        int lastElementIndex = this.forwardReferences[0];
        if (lastElementIndex + 2 >= this.forwardReferences.length) {
            final int[] newValues = new int[this.forwardReferences.length + 6];
            System.arraycopy(this.forwardReferences, 0, newValues, 0, this.forwardReferences.length);
            this.forwardReferences = newValues;
        }
        this.forwardReferences[++lastElementIndex] = sourceInsnBytecodeOffset;
        this.forwardReferences[++lastElementIndex] = (referenceType | referenceHandle);
        this.forwardReferences[0] = lastElementIndex;
    }
    
    final boolean resolve(final byte[] code, final int bytecodeOffset) {
        this.flags |= 0x4;
        this.bytecodeOffset = bytecodeOffset;
        if (this.forwardReferences == null) {
            return false;
        }
        boolean hasAsmInstructions = false;
        for (int i = this.forwardReferences[0]; i > 0; i -= 2) {
            final int sourceInsnBytecodeOffset = this.forwardReferences[i - 1];
            final int reference = this.forwardReferences[i];
            final int relativeOffset = bytecodeOffset - sourceInsnBytecodeOffset;
            int handle = reference & 0xFFFFFFF;
            if ((reference & 0xF0000000) == 0x10000000) {
                if (relativeOffset < -32768 || relativeOffset > 32767) {
                    final int opcode = code[sourceInsnBytecodeOffset] & 0xFF;
                    if (opcode < 198) {
                        code[sourceInsnBytecodeOffset] = (byte)(opcode + 49);
                    }
                    else {
                        code[sourceInsnBytecodeOffset] = (byte)(opcode + 20);
                    }
                    hasAsmInstructions = true;
                }
            }
            else {
                code[handle++] = (byte)(relativeOffset >>> 24);
                code[handle++] = (byte)(relativeOffset >>> 16);
            }
            code[handle++] = (byte)(relativeOffset >>> 8);
            code[handle] = (byte)relativeOffset;
        }
        return hasAsmInstructions;
    }
    
    static {
        EMPTY_LIST = new Label();
    }
}
