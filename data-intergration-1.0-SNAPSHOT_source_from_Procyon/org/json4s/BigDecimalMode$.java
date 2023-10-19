// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigInt;
import scala.math.BigDecimal;

public final class BigDecimalMode$ implements Implicits, BigDecimalMode
{
    public static final BigDecimalMode$ MODULE$;
    
    static {
        new BigDecimalMode$();
    }
    
    @Override
    public JsonAST.JValue double2jvalue(final double x) {
        return BigDecimalMode$class.double2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue float2jvalue(final float x) {
        return BigDecimalMode$class.float2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue bigdecimal2jvalue(final BigDecimal x) {
        return BigDecimalMode$class.bigdecimal2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue short2jvalue(final short x) {
        return Implicits$class.short2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue byte2jvalue(final byte x) {
        return Implicits$class.byte2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue char2jvalue(final char x) {
        return Implicits$class.char2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue int2jvalue(final int x) {
        return Implicits$class.int2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue long2jvalue(final long x) {
        return Implicits$class.long2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue bigint2jvalue(final BigInt x) {
        return Implicits$class.bigint2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue boolean2jvalue(final boolean x) {
        return Implicits$class.boolean2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue string2jvalue(final String x) {
        return Implicits$class.string2jvalue(this, x);
    }
    
    private BigDecimalMode$() {
        Implicits$class.$init$(MODULE$ = this);
        BigDecimalMode$class.$init$(this);
    }
}
