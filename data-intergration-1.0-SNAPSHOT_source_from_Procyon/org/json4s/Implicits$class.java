// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigInt;
import scala.math.BigInt$;

public abstract class Implicits$class
{
    public static JsonAST.JValue short2jvalue(final Implicits $this, final short x) {
        return new JsonAST.JInt(BigInt$.MODULE$.int2bigInt((int)x));
    }
    
    public static JsonAST.JValue byte2jvalue(final Implicits $this, final byte x) {
        return new JsonAST.JInt(BigInt$.MODULE$.int2bigInt((int)x));
    }
    
    public static JsonAST.JValue char2jvalue(final Implicits $this, final char x) {
        return new JsonAST.JInt(BigInt$.MODULE$.int2bigInt((int)x));
    }
    
    public static JsonAST.JValue int2jvalue(final Implicits $this, final int x) {
        return new JsonAST.JInt(BigInt$.MODULE$.int2bigInt(x));
    }
    
    public static JsonAST.JValue long2jvalue(final Implicits $this, final long x) {
        return new JsonAST.JInt(BigInt$.MODULE$.long2bigInt(x));
    }
    
    public static JsonAST.JValue bigint2jvalue(final Implicits $this, final BigInt x) {
        return new JsonAST.JInt(x);
    }
    
    public static JsonAST.JValue boolean2jvalue(final Implicits $this, final boolean x) {
        return new JsonAST.JBool(x);
    }
    
    public static JsonAST.JValue string2jvalue(final Implicits $this, final String x) {
        return new JsonAST.JString(x);
    }
    
    public static void $init$(final Implicits $this) {
    }
}
