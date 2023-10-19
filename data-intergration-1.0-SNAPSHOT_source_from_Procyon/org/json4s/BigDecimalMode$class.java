// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigDecimal;
import scala.math.BigDecimal$;

public abstract class BigDecimalMode$class
{
    public static JsonAST.JValue double2jvalue(final BigDecimalMode $this, final double x) {
        return new JsonAST.JDecimal(BigDecimal$.MODULE$.double2bigDecimal(x));
    }
    
    public static JsonAST.JValue float2jvalue(final BigDecimalMode $this, final float x) {
        return new JsonAST.JDecimal(BigDecimal$.MODULE$.double2bigDecimal((double)x));
    }
    
    public static JsonAST.JValue bigdecimal2jvalue(final BigDecimalMode $this, final BigDecimal x) {
        return new JsonAST.JDecimal(x);
    }
    
    public static void $init$(final BigDecimalMode $this) {
    }
}
