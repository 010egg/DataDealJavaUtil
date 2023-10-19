// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc;

import java.util.HashMap;
import org.postgresql.util.ByteConverter;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Connection;
import org.postgresql.core.TypeInfo;
import java.util.Map;

abstract class PrimitiveArraySupport<A>
{
    private static final PrimitiveArraySupport<long[]> LONG_ARRAY;
    private static final PrimitiveArraySupport<int[]> INT_ARRAY;
    private static final PrimitiveArraySupport<short[]> SHORT_ARRAY;
    private static final PrimitiveArraySupport<double[]> DOUBLE_ARRAY;
    private static final PrimitiveArraySupport<float[]> FLOAT_ARRAY;
    private static final PrimitiveArraySupport<boolean[]> BOOLEAN_ARRAY;
    private static final PrimitiveArraySupport<String[]> STRING_ARRAY;
    private static final Map<Class, PrimitiveArraySupport> ARRAY_CLASS_TO_SUPPORT;
    
    public abstract int getDefaultArrayTypeOid(final TypeInfo p0);
    
    public abstract String toArrayString(final char p0, final A p1);
    
    public abstract void appendArray(final StringBuilder p0, final char p1, final A p2);
    
    public boolean supportBinaryRepresentation() {
        return true;
    }
    
    public abstract byte[] toBinaryRepresentation(final Connection p0, final A p1) throws SQLFeatureNotSupportedException;
    
    public static boolean isSupportedPrimitiveArray(final Object obj) {
        return obj != null && PrimitiveArraySupport.ARRAY_CLASS_TO_SUPPORT.containsKey(obj.getClass());
    }
    
    public static <A> PrimitiveArraySupport<A> getArraySupport(final A array) {
        return PrimitiveArraySupport.ARRAY_CLASS_TO_SUPPORT.get(array.getClass());
    }
    
    static {
        LONG_ARRAY = new PrimitiveArraySupport<long[]>() {
            @Override
            public int getDefaultArrayTypeOid(final TypeInfo tiCache) {
                return 1016;
            }
            
            @Override
            public String toArrayString(final char delim, final long[] array) {
                final StringBuilder sb = new StringBuilder(Math.max(64, array.length * 8));
                this.appendArray(sb, delim, array);
                return sb.toString();
            }
            
            @Override
            public void appendArray(final StringBuilder sb, final char delim, final long[] array) {
                sb.append('{');
                for (int i = 0; i < array.length; ++i) {
                    if (i > 0) {
                        sb.append(delim);
                    }
                    sb.append(array[i]);
                }
                sb.append('}');
            }
            
            @Override
            public byte[] toBinaryRepresentation(final Connection connection, final long[] array) {
                final int length = 20 + 12 * array.length;
                final byte[] bytes = new byte[length];
                ByteConverter.int4(bytes, 0, 1);
                ByteConverter.int4(bytes, 4, 0);
                ByteConverter.int4(bytes, 8, 20);
                ByteConverter.int4(bytes, 12, array.length);
                int idx = 20;
                for (int i = 0; i < array.length; ++i) {
                    bytes[idx + 3] = 8;
                    ByteConverter.int8(bytes, idx + 4, array[i]);
                    idx += 12;
                }
                return bytes;
            }
        };
        INT_ARRAY = new PrimitiveArraySupport<int[]>() {
            @Override
            public int getDefaultArrayTypeOid(final TypeInfo tiCache) {
                return 1007;
            }
            
            @Override
            public String toArrayString(final char delim, final int[] array) {
                final StringBuilder sb = new StringBuilder(Math.max(32, array.length * 6));
                this.appendArray(sb, delim, array);
                return sb.toString();
            }
            
            @Override
            public void appendArray(final StringBuilder sb, final char delim, final int[] array) {
                sb.append('{');
                for (int i = 0; i < array.length; ++i) {
                    if (i > 0) {
                        sb.append(delim);
                    }
                    sb.append(array[i]);
                }
                sb.append('}');
            }
            
            @Override
            public byte[] toBinaryRepresentation(final Connection connection, final int[] array) {
                final int length = 20 + 8 * array.length;
                final byte[] bytes = new byte[length];
                ByteConverter.int4(bytes, 0, 1);
                ByteConverter.int4(bytes, 4, 0);
                ByteConverter.int4(bytes, 8, 23);
                ByteConverter.int4(bytes, 12, array.length);
                int idx = 20;
                for (int i = 0; i < array.length; ++i) {
                    bytes[idx + 3] = 4;
                    ByteConverter.int4(bytes, idx + 4, array[i]);
                    idx += 8;
                }
                return bytes;
            }
        };
        SHORT_ARRAY = new PrimitiveArraySupport<short[]>() {
            @Override
            public int getDefaultArrayTypeOid(final TypeInfo tiCache) {
                return 1005;
            }
            
            @Override
            public String toArrayString(final char delim, final short[] array) {
                final StringBuilder sb = new StringBuilder(Math.max(32, array.length * 4));
                this.appendArray(sb, delim, array);
                return sb.toString();
            }
            
            @Override
            public void appendArray(final StringBuilder sb, final char delim, final short[] array) {
                sb.append('{');
                for (int i = 0; i < array.length; ++i) {
                    if (i > 0) {
                        sb.append(delim);
                    }
                    sb.append(array[i]);
                }
                sb.append('}');
            }
            
            @Override
            public byte[] toBinaryRepresentation(final Connection connection, final short[] array) {
                final int length = 20 + 6 * array.length;
                final byte[] bytes = new byte[length];
                ByteConverter.int4(bytes, 0, 1);
                ByteConverter.int4(bytes, 4, 0);
                ByteConverter.int4(bytes, 8, 21);
                ByteConverter.int4(bytes, 12, array.length);
                int idx = 20;
                for (int i = 0; i < array.length; ++i) {
                    bytes[idx + 3] = 2;
                    ByteConverter.int2(bytes, idx + 4, array[i]);
                    idx += 6;
                }
                return bytes;
            }
        };
        DOUBLE_ARRAY = new PrimitiveArraySupport<double[]>() {
            @Override
            public int getDefaultArrayTypeOid(final TypeInfo tiCache) {
                return 1022;
            }
            
            @Override
            public String toArrayString(final char delim, final double[] array) {
                final StringBuilder sb = new StringBuilder(Math.max(64, array.length * 8));
                this.appendArray(sb, delim, array);
                return sb.toString();
            }
            
            @Override
            public void appendArray(final StringBuilder sb, final char delim, final double[] array) {
                sb.append('{');
                for (int i = 0; i < array.length; ++i) {
                    if (i > 0) {
                        sb.append(delim);
                    }
                    sb.append('\"');
                    sb.append(array[i]);
                    sb.append('\"');
                }
                sb.append('}');
            }
            
            @Override
            public byte[] toBinaryRepresentation(final Connection connection, final double[] array) {
                final int length = 20 + 12 * array.length;
                final byte[] bytes = new byte[length];
                ByteConverter.int4(bytes, 0, 1);
                ByteConverter.int4(bytes, 4, 0);
                ByteConverter.int4(bytes, 8, 701);
                ByteConverter.int4(bytes, 12, array.length);
                int idx = 20;
                for (int i = 0; i < array.length; ++i) {
                    bytes[idx + 3] = 8;
                    ByteConverter.float8(bytes, idx + 4, array[i]);
                    idx += 12;
                }
                return bytes;
            }
        };
        FLOAT_ARRAY = new PrimitiveArraySupport<float[]>() {
            @Override
            public int getDefaultArrayTypeOid(final TypeInfo tiCache) {
                return 1021;
            }
            
            @Override
            public String toArrayString(final char delim, final float[] array) {
                final StringBuilder sb = new StringBuilder(Math.max(64, array.length * 8));
                this.appendArray(sb, delim, array);
                return sb.toString();
            }
            
            @Override
            public void appendArray(final StringBuilder sb, final char delim, final float[] array) {
                sb.append('{');
                for (int i = 0; i < array.length; ++i) {
                    if (i > 0) {
                        sb.append(delim);
                    }
                    sb.append('\"');
                    sb.append(array[i]);
                    sb.append('\"');
                }
                sb.append('}');
            }
            
            @Override
            public byte[] toBinaryRepresentation(final Connection connection, final float[] array) {
                final int length = 20 + 8 * array.length;
                final byte[] bytes = new byte[length];
                ByteConverter.int4(bytes, 0, 1);
                ByteConverter.int4(bytes, 4, 0);
                ByteConverter.int4(bytes, 8, 700);
                ByteConverter.int4(bytes, 12, array.length);
                int idx = 20;
                for (int i = 0; i < array.length; ++i) {
                    bytes[idx + 3] = 4;
                    ByteConverter.float4(bytes, idx + 4, array[i]);
                    idx += 8;
                }
                return bytes;
            }
        };
        BOOLEAN_ARRAY = new PrimitiveArraySupport<boolean[]>() {
            @Override
            public int getDefaultArrayTypeOid(final TypeInfo tiCache) {
                return 1000;
            }
            
            @Override
            public String toArrayString(final char delim, final boolean[] array) {
                final StringBuilder sb = new StringBuilder(Math.max(64, array.length * 8));
                this.appendArray(sb, delim, array);
                return sb.toString();
            }
            
            @Override
            public void appendArray(final StringBuilder sb, final char delim, final boolean[] array) {
                sb.append('{');
                for (int i = 0; i < array.length; ++i) {
                    if (i > 0) {
                        sb.append(delim);
                    }
                    sb.append(array[i] ? '1' : '0');
                }
                sb.append('}');
            }
            
            @Override
            public byte[] toBinaryRepresentation(final Connection connection, final boolean[] array) throws SQLFeatureNotSupportedException {
                final int length = 20 + 5 * array.length;
                final byte[] bytes = new byte[length];
                ByteConverter.int4(bytes, 0, 1);
                ByteConverter.int4(bytes, 4, 0);
                ByteConverter.int4(bytes, 8, 16);
                ByteConverter.int4(bytes, 12, array.length);
                int idx = 20;
                for (int i = 0; i < array.length; ++i) {
                    bytes[idx + 3] = 1;
                    ByteConverter.bool(bytes, idx + 4, array[i]);
                    idx += 5;
                }
                return bytes;
            }
        };
        STRING_ARRAY = new PrimitiveArraySupport<String[]>() {
            @Override
            public int getDefaultArrayTypeOid(final TypeInfo tiCache) {
                return 1015;
            }
            
            @Override
            public String toArrayString(final char delim, final String[] array) {
                final StringBuilder sb = new StringBuilder(Math.max(64, array.length * 8));
                this.appendArray(sb, delim, array);
                return sb.toString();
            }
            
            @Override
            public void appendArray(final StringBuilder sb, final char delim, final String[] array) {
                sb.append('{');
                for (int i = 0; i < array.length; ++i) {
                    if (i > 0) {
                        sb.append(delim);
                    }
                    if (array[i] == null) {
                        sb.append('N');
                        sb.append('U');
                        sb.append('L');
                        sb.append('L');
                    }
                    else {
                        PgArray.escapeArrayElement(sb, array[i]);
                    }
                }
                sb.append('}');
            }
            
            @Override
            public boolean supportBinaryRepresentation() {
                return false;
            }
            
            @Override
            public byte[] toBinaryRepresentation(final Connection connection, final String[] array) throws SQLFeatureNotSupportedException {
                throw new SQLFeatureNotSupportedException();
            }
        };
        (ARRAY_CLASS_TO_SUPPORT = new HashMap<Class, PrimitiveArraySupport>(10)).put(long[].class, PrimitiveArraySupport.LONG_ARRAY);
        PrimitiveArraySupport.ARRAY_CLASS_TO_SUPPORT.put(int[].class, PrimitiveArraySupport.INT_ARRAY);
        PrimitiveArraySupport.ARRAY_CLASS_TO_SUPPORT.put(short[].class, PrimitiveArraySupport.SHORT_ARRAY);
        PrimitiveArraySupport.ARRAY_CLASS_TO_SUPPORT.put(double[].class, PrimitiveArraySupport.DOUBLE_ARRAY);
        PrimitiveArraySupport.ARRAY_CLASS_TO_SUPPORT.put(float[].class, PrimitiveArraySupport.FLOAT_ARRAY);
        PrimitiveArraySupport.ARRAY_CLASS_TO_SUPPORT.put(boolean[].class, PrimitiveArraySupport.BOOLEAN_ARRAY);
        PrimitiveArraySupport.ARRAY_CLASS_TO_SUPPORT.put(String[].class, PrimitiveArraySupport.STRING_ARRAY);
    }
}
