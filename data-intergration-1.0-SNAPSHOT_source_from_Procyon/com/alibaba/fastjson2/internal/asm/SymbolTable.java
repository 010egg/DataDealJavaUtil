// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.internal.asm;

final class SymbolTable
{
    final ClassWriter classWriter;
    String className;
    private int entryCount;
    private Symbol[] entries;
    int constantPoolCount;
    final ByteVector constantPool;
    private int typeCount;
    Symbol[] typeTable;
    
    SymbolTable(final ClassWriter classWriter) {
        this.classWriter = classWriter;
        this.entries = new Symbol[256];
        this.constantPoolCount = 1;
        this.constantPool = new ByteVector(4096);
    }
    
    int setMajorVersionAndClassName(final int majorVersion, final String className) {
        this.className = className;
        return this.addConstantUtf8Reference(7, className).index;
    }
    
    private Symbol put(final Symbol entry) {
        if (this.entryCount > this.entries.length * 3 / 4) {
            final int currentCapacity = this.entries.length;
            final int newCapacity = currentCapacity * 2 + 1;
            final Symbol[] newEntries = new Symbol[newCapacity];
            for (int i = currentCapacity - 1; i >= 0; --i) {
                Symbol nextEntry;
                for (Symbol currentEntry = this.entries[i]; currentEntry != null; currentEntry = nextEntry) {
                    final int newCurrentEntryIndex = currentEntry.hashCode % newCapacity;
                    nextEntry = currentEntry.next;
                    currentEntry.next = newEntries[newCurrentEntryIndex];
                    newEntries[newCurrentEntryIndex] = currentEntry;
                }
            }
            this.entries = newEntries;
        }
        ++this.entryCount;
        final int index = entry.hashCode % this.entries.length;
        entry.next = this.entries[index];
        return this.entries[index] = entry;
    }
    
    Symbol addConstantMemberReference(final int tag, final String owner, final String name, final String descriptor) {
        final int hashCode = Integer.MAX_VALUE & tag + owner.hashCode() * name.hashCode() * descriptor.hashCode();
        for (Symbol entry = this.entries[hashCode % this.entries.length]; entry != null; entry = entry.next) {
            if (entry.tag == tag && entry.hashCode == hashCode && entry.owner.equals(owner) && entry.name.equals(name) && entry.value.equals(descriptor)) {
                return entry;
            }
        }
        this.constantPool.put122(tag, this.addConstantUtf8Reference(7, owner).index, this.addConstantNameAndType(name, descriptor));
        return this.put(new Symbol(this.constantPoolCount++, tag, owner, name, descriptor, 0L, hashCode));
    }
    
    Symbol addConstantIntegerOrFloat(final int value) {
        final int hashCode = Integer.MAX_VALUE & 3 + value;
        for (Symbol entry = this.entries[hashCode % this.entries.length]; entry != null; entry = entry.next) {
            if (entry.tag == 3 && entry.hashCode == hashCode && entry.data == value) {
                return entry;
            }
        }
        this.constantPool.putByte(3).putInt(value);
        return this.put(new Symbol(this.constantPoolCount++, 3, null, null, null, value, hashCode));
    }
    
    Symbol addConstantLongOrDouble(final long value) {
        final int hashCode = Integer.MAX_VALUE & 5 + (int)value + (int)(value >>> 32);
        for (Symbol entry = this.entries[hashCode % this.entries.length]; entry != null; entry = entry.next) {
            if (entry.tag == 5 && entry.hashCode == hashCode && entry.data == value) {
                return entry;
            }
        }
        final int index = this.constantPoolCount;
        this.constantPool.putByte(5).putLong(value);
        this.constantPoolCount += 2;
        return this.put(new Symbol(index, 5, null, null, null, value, hashCode));
    }
    
    int addConstantNameAndType(final String name, final String descriptor) {
        final int tag = 12;
        final int hashCode = Integer.MAX_VALUE & 12 + name.hashCode() * descriptor.hashCode();
        for (Symbol entry = this.entries[hashCode % this.entries.length]; entry != null; entry = entry.next) {
            if (entry.tag == 12 && entry.hashCode == hashCode && entry.name.equals(name) && entry.value.equals(descriptor)) {
                return entry.index;
            }
        }
        this.constantPool.put122(12, this.addConstantUtf8(name), this.addConstantUtf8(descriptor));
        return this.put(new Symbol(this.constantPoolCount++, 12, null, name, descriptor, 0L, hashCode)).index;
    }
    
    int addConstantUtf8(final String value) {
        final int CONSTANT_UTF8_TAG = 1;
        final int hashCode = Integer.MAX_VALUE & 1 + value.hashCode();
        for (Symbol entry = this.entries[hashCode % this.entries.length]; entry != null; entry = entry.next) {
            if (entry.tag == 1 && entry.hashCode == hashCode && entry.value.equals(value)) {
                return entry.index;
            }
        }
        this.constantPool.putByte(1).putUTF8(value);
        return this.put(new Symbol(this.constantPoolCount++, 1, null, null, value, 0L, hashCode)).index;
    }
    
    Symbol addConstantUtf8Reference(final int tag, final String value) {
        final int hashCode = Integer.MAX_VALUE & tag + value.hashCode();
        for (Symbol entry = this.entries[hashCode % this.entries.length]; entry != null; entry = entry.next) {
            if (entry.tag == tag && entry.hashCode == hashCode && entry.value.equals(value)) {
                return entry;
            }
        }
        this.constantPool.put12(tag, this.addConstantUtf8(value));
        return this.put(new Symbol(this.constantPoolCount++, tag, null, null, value, 0L, hashCode));
    }
    
    int addType(final String value) {
        final int TYPE_TAG = 128;
        final int hashCode = Integer.MAX_VALUE & 128 + value.hashCode();
        for (Symbol entry = this.entries[hashCode % this.entries.length]; entry != null; entry = entry.next) {
            if (entry.tag == 128 && entry.hashCode == hashCode && entry.value.equals(value)) {
                return entry.index;
            }
        }
        return this.addTypeInternal(new Symbol(this.typeCount, 128, null, null, value, 0L, hashCode));
    }
    
    int addUninitializedType(final String value, final int bytecodeOffset) {
        final int UNINITIALIZED_TYPE_TAG = 129;
        final int hashCode = Integer.MAX_VALUE & 129 + value.hashCode() + bytecodeOffset;
        for (Symbol entry = this.entries[hashCode % this.entries.length]; entry != null; entry = entry.next) {
            if (entry.tag == 129 && entry.hashCode == hashCode && entry.data == bytecodeOffset && entry.value.equals(value)) {
                return entry.index;
            }
        }
        return this.addTypeInternal(new Symbol(this.typeCount, 129, null, null, value, bytecodeOffset, hashCode));
    }
    
    int addMergedType(final int typeTableIndex1, final int typeTableIndex2) {
        final int MERGED_TYPE_TAG = 130;
        final long data = (typeTableIndex1 < typeTableIndex2) ? ((long)typeTableIndex1 | (long)typeTableIndex2 << 32) : ((long)typeTableIndex2 | (long)typeTableIndex1 << 32);
        final int hashCode = Integer.MAX_VALUE & 130 + typeTableIndex1 + typeTableIndex2;
        for (Symbol entry = this.entries[hashCode % this.entries.length]; entry != null; entry = entry.next) {
            if (entry.tag == 130 && entry.hashCode == hashCode && entry.data == data) {
                return entry.info;
            }
        }
        final String type1 = this.typeTable[typeTableIndex1].value;
        final String type2 = this.typeTable[typeTableIndex2].value;
        final int commonSuperTypeIndex = this.addType(this.classWriter.getCommonSuperClass(type1, type2));
        return this.put(new Symbol(this.typeCount, 130, null, null, null, data, hashCode)).info = commonSuperTypeIndex;
    }
    
    private int addTypeInternal(final Symbol entry) {
        if (this.typeTable == null) {
            this.typeTable = new Symbol[16];
        }
        if (this.typeCount == this.typeTable.length) {
            final Symbol[] newTypeTable = new Symbol[2 * this.typeTable.length];
            System.arraycopy(this.typeTable, 0, newTypeTable, 0, this.typeTable.length);
            this.typeTable = newTypeTable;
        }
        this.typeTable[this.typeCount++] = entry;
        return this.put(entry).index;
    }
}
