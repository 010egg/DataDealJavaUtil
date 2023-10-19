// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

public class ByteConverter
{
    private ByteConverter() {
    }
    
    public static long int8(final byte[] bytes, final int idx) {
        return ((long)(bytes[idx + 0] & 0xFF) << 56) + ((long)(bytes[idx + 1] & 0xFF) << 48) + ((long)(bytes[idx + 2] & 0xFF) << 40) + ((long)(bytes[idx + 3] & 0xFF) << 32) + ((long)(bytes[idx + 4] & 0xFF) << 24) + ((long)(bytes[idx + 5] & 0xFF) << 16) + ((long)(bytes[idx + 6] & 0xFF) << 8) + (bytes[idx + 7] & 0xFF);
    }
    
    public static int int4(final byte[] bytes, final int idx) {
        return ((bytes[idx] & 0xFF) << 24) + ((bytes[idx + 1] & 0xFF) << 16) + ((bytes[idx + 2] & 0xFF) << 8) + (bytes[idx + 3] & 0xFF);
    }
    
    public static short int2(final byte[] bytes, final int idx) {
        return (short)(((bytes[idx] & 0xFF) << 8) + (bytes[idx + 1] & 0xFF));
    }
    
    public static float float4(final byte[] bytes, final int idx) {
        return Float.intBitsToFloat(int4(bytes, idx));
    }
    
    public static double float8(final byte[] bytes, final int idx) {
        return Double.longBitsToDouble(int8(bytes, idx));
    }
    
    public static void int8(final byte[] target, final int idx, final long value) {
        target[idx + 0] = (byte)(value >>> 56);
        target[idx + 1] = (byte)(value >>> 48);
        target[idx + 2] = (byte)(value >>> 40);
        target[idx + 3] = (byte)(value >>> 32);
        target[idx + 4] = (byte)(value >>> 24);
        target[idx + 5] = (byte)(value >>> 16);
        target[idx + 6] = (byte)(value >>> 8);
        target[idx + 7] = (byte)value;
    }
    
    public static void int4(final byte[] target, final int idx, final int value) {
        target[idx + 0] = (byte)(value >>> 24);
        target[idx + 1] = (byte)(value >>> 16);
        target[idx + 2] = (byte)(value >>> 8);
        target[idx + 3] = (byte)value;
    }
    
    public static void int2(final byte[] target, final int idx, final int value) {
        target[idx + 0] = (byte)(value >>> 8);
        target[idx + 1] = (byte)value;
    }
    
    public static void float4(final byte[] target, final int idx, final float value) {
        int4(target, idx, Float.floatToRawIntBits(value));
    }
    
    public static void float8(final byte[] target, final int idx, final double value) {
        int8(target, idx, Double.doubleToRawLongBits(value));
    }
}
