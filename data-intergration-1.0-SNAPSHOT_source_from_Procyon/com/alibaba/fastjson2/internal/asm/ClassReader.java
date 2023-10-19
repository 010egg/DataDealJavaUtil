// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.internal.asm;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ClassReader
{
    public final byte[] b;
    private final int[] items;
    private final String[] strings;
    private final int maxStringLength;
    public final int header;
    
    public ClassReader(final InputStream is) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buf = new byte[1024];
        while (true) {
            final int len = is.read(buf);
            if (len == -1) {
                break;
            }
            if (len <= 0) {
                continue;
            }
            out.write(buf, 0, len);
        }
        is.close();
        this.b = out.toByteArray();
        this.items = new int[this.readUnsignedShort(8)];
        final int n = this.items.length;
        this.strings = new String[n];
        int max = 0;
        int index = 10;
        for (int i = 1; i < n; ++i) {
            this.items[i] = index + 1;
            int size = 0;
            switch (this.b[index]) {
                case 3:
                case 4:
                case 9:
                case 10:
                case 11:
                case 12:
                case 18: {
                    size = 5;
                    break;
                }
                case 5:
                case 6: {
                    size = 9;
                    ++i;
                    break;
                }
                case 15: {
                    size = 4;
                    break;
                }
                case 1: {
                    size = 3 + this.readUnsignedShort(index + 1);
                    if (size > max) {
                        max = size;
                        break;
                    }
                    break;
                }
                default: {
                    size = 3;
                    break;
                }
            }
            index += size;
        }
        this.maxStringLength = max;
        this.header = index;
    }
    
    public void accept(final TypeCollector classVisitor) {
        final char[] c = new char[this.maxStringLength];
        int u = this.header;
        final int len = this.readUnsignedShort(u + 6);
        u += 8;
        for (int i = 0; i < len; ++i) {
            u += 2;
        }
        int v = u;
        int i = this.readUnsignedShort(v);
        v += 2;
        while (i > 0) {
            int j = this.readUnsignedShort(v + 6);
            v += 8;
            while (j > 0) {
                v += 6 + this.readInt(v + 2);
                --j;
            }
            --i;
        }
        i = this.readUnsignedShort(v);
        v += 2;
        while (i > 0) {
            int j = this.readUnsignedShort(v + 6);
            v += 8;
            while (j > 0) {
                v += 6 + this.readInt(v + 2);
                --j;
            }
            --i;
        }
        i = this.readUnsignedShort(v);
        v += 2;
        while (i > 0) {
            v += 6 + this.readInt(v + 2);
            --i;
        }
        i = this.readUnsignedShort(u);
        u += 2;
        while (i > 0) {
            int j = this.readUnsignedShort(u + 6);
            u += 8;
            while (j > 0) {
                u += 6 + this.readInt(u + 2);
                --j;
            }
            --i;
        }
        i = this.readUnsignedShort(u);
        u += 2;
        while (i > 0) {
            u = this.readMethod(classVisitor, c, u);
            --i;
        }
    }
    
    private int readMethod(final TypeCollector classVisitor, final char[] c, int u) {
        final int access = this.readUnsignedShort(u);
        final String name = this.readUTF8(u + 2, c);
        final String desc = this.readUTF8(u + 4, c);
        int v = 0;
        int j = this.readUnsignedShort(u + 6);
        u += 8;
        while (j > 0) {
            final String attrName = this.readUTF8(u, c);
            final int attrSize = this.readInt(u + 2);
            u += 6;
            if ("Code".equals(attrName)) {
                v = u;
            }
            u += attrSize;
            --j;
        }
        final MethodCollector mv = classVisitor.visitMethod(access, name, desc);
        if (mv != null && v != 0) {
            final int codeLength = this.readInt(v + 4);
            v += 8;
            final int codeStart = v;
            v += codeLength;
            j = this.readUnsignedShort(v);
            v += 2;
            while (j > 0) {
                v += 8;
                --j;
            }
            int varTable = 0;
            int varTypeTable = 0;
            j = this.readUnsignedShort(v);
            v += 2;
            while (j > 0) {
                final String attrName = this.readUTF8(v, c);
                if ("LocalVariableTable".equals(attrName)) {
                    varTable = v + 6;
                }
                else if ("LocalVariableTypeTable".equals(attrName)) {
                    varTypeTable = v + 6;
                }
                v += 6 + this.readInt(v + 2);
                --j;
            }
            v = codeStart;
            if (varTable != 0) {
                if (varTypeTable != 0) {
                    int k = this.readUnsignedShort(varTypeTable) * 3;
                    int w = varTypeTable + 2;
                    for (int[] typeTable = new int[k]; k > 0; typeTable[--k] = w + 6, typeTable[--k] = this.readUnsignedShort(w + 8), typeTable[--k] = this.readUnsignedShort(w), w += 10) {}
                }
                int k = this.readUnsignedShort(varTable);
                int w = varTable + 2;
                while (k > 0) {
                    final int index = this.readUnsignedShort(w + 8);
                    mv.visitLocalVariable(this.readUTF8(w + 4, c), index);
                    w += 10;
                    --k;
                }
            }
        }
        return u;
    }
    
    private int readUnsignedShort(final int index) {
        final byte[] b = this.b;
        return (b[index] & 0xFF) << 8 | (b[index + 1] & 0xFF);
    }
    
    private int readInt(final int index) {
        final byte[] b = this.b;
        return (b[index] & 0xFF) << 24 | (b[index + 1] & 0xFF) << 16 | (b[index + 2] & 0xFF) << 8 | (b[index + 3] & 0xFF);
    }
    
    private String readUTF8(int index, final char[] buf) {
        final int item = this.readUnsignedShort(index);
        final String s = this.strings[item];
        if (s != null) {
            return s;
        }
        index = this.items[item];
        return this.strings[item] = this.readUTF(index + 2, this.readUnsignedShort(index), buf);
    }
    
    private String readUTF(int index, final int utfLen, final char[] buf) {
        final int endIndex = index + utfLen;
        final byte[] b = this.b;
        int strLen = 0;
        int st = 0;
        char cc = '\0';
        while (index < endIndex) {
            int c = b[index++];
            switch (st) {
                case 0: {
                    c &= 0xFF;
                    if (c < 128) {
                        buf[strLen++] = (char)c;
                        continue;
                    }
                    if (c < 224 && c > 191) {
                        cc = (char)(c & 0x1F);
                        st = 1;
                        continue;
                    }
                    cc = (char)(c & 0xF);
                    st = 2;
                    continue;
                }
                case 1: {
                    buf[strLen++] = (char)(cc << 6 | (c & 0x3F));
                    st = 0;
                    continue;
                }
                case 2: {
                    cc = (char)(cc << 6 | (c & 0x3F));
                    st = 1;
                    continue;
                }
            }
        }
        return new String(buf, 0, strLen);
    }
}
