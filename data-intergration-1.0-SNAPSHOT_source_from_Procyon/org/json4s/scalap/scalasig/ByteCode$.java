// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

public final class ByteCode$
{
    public static final ByteCode$ MODULE$;
    
    static {
        new ByteCode$();
    }
    
    public ByteCode apply(final byte[] bytes) {
        return new ByteCode(bytes, 0, bytes.length);
    }
    
    public ByteCode forClass(final Class<?> clazz) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //     4: astore_2        /* name */
        //     5: new             Lscala/collection/mutable/StringBuilder;
        //     8: dup            
        //     9: invokespecial   scala/collection/mutable/StringBuilder.<init>:()V
        //    12: aload_2         /* name */
        //    13: aload_2         /* name */
        //    14: bipush          46
        //    16: invokevirtual   java/lang/String.lastIndexOf:(I)I
        //    19: iconst_1       
        //    20: iadd           
        //    21: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //    24: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //    27: ldc             ".class"
        //    29: invokevirtual   scala/collection/mutable/StringBuilder.append:(Ljava/lang/Object;)Lscala/collection/mutable/StringBuilder;
        //    32: invokevirtual   scala/collection/mutable/StringBuilder.toString:()Ljava/lang/String;
        //    35: astore_3        /* subPath */
        //    36: aload_1         /* clazz */
        //    37: aload_3         /* subPath */
        //    38: invokevirtual   java/lang/Class.getResourceAsStream:(Ljava/lang/String;)Ljava/io/InputStream;
        //    41: astore          in
        //    43: aload           in
        //    45: invokevirtual   java/io/InputStream.available:()I
        //    48: istore          rest
        //    50: iload           rest
        //    52: newarray        B
        //    54: astore          bytes
        //    56: iload           rest
        //    58: iconst_0       
        //    59: if_icmple       105
        //    62: aload           in
        //    64: aload           bytes
        //    66: aload           bytes
        //    68: arraylength    
        //    69: iload           rest
        //    71: isub           
        //    72: iload           rest
        //    74: invokevirtual   java/io/InputStream.read:([BII)I
        //    77: istore          res
        //    79: iload           res
        //    81: iconst_m1      
        //    82: if_icmpne       95
        //    85: new             Ljava/io/IOException;
        //    88: dup            
        //    89: ldc             "read error"
        //    91: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //    94: athrow         
        //    95: iload           rest
        //    97: iload           res
        //    99: isub           
        //   100: istore          rest
        //   102: goto            56
        //   105: aload_0         /* this */
        //   106: aload           bytes
        //   108: invokevirtual   org/json4s/scalap/scalasig/ByteCode$.apply:([B)Lorg/json4s/scalap/scalasig/ByteCode;
        //   111: aload           in
        //   113: invokevirtual   java/io/InputStream.close:()V
        //   116: areturn        
        //   117: astore          5
        //   119: aload           4
        //   121: invokevirtual   java/io/InputStream.close:()V
        //   124: aload           5
        //   126: athrow         
        //    Signature:
        //  (Ljava/lang/Class<*>;)Lorg/json4s/scalap/scalasig/ByteCode;
        //    StackMapTable: 00 04 FF 00 38 00 08 07 00 02 07 00 1A 07 00 23 07 00 23 07 00 3A 00 01 07 00 59 00 00 FC 00 26 01 FA 00 09 FF 00 0B 00 05 07 00 02 07 00 1A 07 00 23 07 00 23 07 00 3A 00 01 07 00 5B
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  43     111    117    127    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private ByteCode$() {
        MODULE$ = this;
    }
}
