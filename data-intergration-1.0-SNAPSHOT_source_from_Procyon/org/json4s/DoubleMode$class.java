// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigDecimal;

public abstract class DoubleMode$class
{
    public static JsonAST.JValue double2jvalue(final DoubleMode $this, final double x) {
        return new JsonAST.JDouble(x);
    }
    
    public static JsonAST.JValue float2jvalue(final DoubleMode $this, final float x) {
        return new JsonAST.JDouble(x);
    }
    
    public static JsonAST.JValue bigdecimal2jvalue(final DoubleMode $this, final BigDecimal x) {
        return new JsonAST.JDouble(x.doubleValue());
    }
    
    public static void $init$(final DoubleMode $this) {
    }
}
