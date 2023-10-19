// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

public final class ByteCodecs$
{
    public static final ByteCodecs$ MODULE$;
    
    static {
        new ByteCodecs$();
    }
    
    public int regenerateZero(final byte[] src) {
        int i = 0;
        final int srclen = src.length;
        int j = 0;
        while (i < srclen) {
            final int in = src[i] & 0xFF;
            if (in == 192 && (src[i + 1] & 0xFF) == 0x80) {
                src[j] = 127;
                i += 2;
            }
            else if (in == 0) {
                src[j] = 127;
                ++i;
            }
            else {
                src[j] = (byte)(in - 1);
                ++i;
            }
            ++j;
        }
        return j;
    }
    
    public int decode7to8(final byte[] src, final int srclen) {
        int i = 0;
        int j = 0;
        final int dstlen = (srclen * 7 + 7) / 8;
        while (i + 7 < srclen) {
            int out = src[i];
            byte in = src[i + 1];
            src[j] = (byte)(out | (in & 0x1) << 7);
            out = in >>> 1;
            in = src[i + 2];
            src[j + 1] = (byte)(out | (in & 0x3) << 6);
            out = in >>> 2;
            in = src[i + 3];
            src[j + 2] = (byte)(out | (in & 0x7) << 5);
            out = in >>> 3;
            in = src[i + 4];
            src[j + 3] = (byte)(out | (in & 0xF) << 4);
            out = in >>> 4;
            in = src[i + 5];
            src[j + 4] = (byte)(out | (in & 0x1F) << 3);
            out = in >>> 5;
            in = src[i + 6];
            src[j + 5] = (byte)(out | (in & 0x3F) << 2);
            out = in >>> 6;
            in = src[i + 7];
            src[j + 6] = (byte)(out | in << 1);
            i += 8;
            j += 7;
        }
        if (i < srclen) {
            int out2 = src[i];
            if (i + 1 < srclen) {
                byte in2 = src[i + 1];
                src[j] = (byte)(out2 | (in2 & 0x1) << 7);
                ++j;
                out2 = in2 >>> 1;
                if (i + 2 < srclen) {
                    in2 = src[i + 2];
                    src[j] = (byte)(out2 | (in2 & 0x3) << 6);
                    ++j;
                    out2 = in2 >>> 2;
                    if (i + 3 < srclen) {
                        in2 = src[i + 3];
                        src[j] = (byte)(out2 | (in2 & 0x7) << 5);
                        ++j;
                        out2 = in2 >>> 3;
                        if (i + 4 < srclen) {
                            in2 = src[i + 4];
                            src[j] = (byte)(out2 | (in2 & 0xF) << 4);
                            ++j;
                            out2 = in2 >>> 4;
                            if (i + 5 < srclen) {
                                in2 = src[i + 5];
                                src[j] = (byte)(out2 | (in2 & 0x1F) << 3);
                                ++j;
                                out2 = in2 >>> 5;
                                if (i + 6 < srclen) {
                                    in2 = src[i + 6];
                                    src[j] = (byte)(out2 | (in2 & 0x3F) << 2);
                                    ++j;
                                    out2 = in2 >>> 6;
                                }
                            }
                        }
                    }
                }
            }
            if (j < dstlen) {
                src[j] = (byte)out2;
            }
        }
        return dstlen;
    }
    
    public int decode(final byte[] xs) {
        final int len = this.regenerateZero(xs);
        return this.decode7to8(xs, len);
    }
    
    private ByteCodecs$() {
        MODULE$ = this;
    }
}
