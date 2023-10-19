// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

public abstract class DynamicJValueImplicits$class
{
    public static JsonAST.JValue dynamic2Jv(final DynamicJValueImplicits $this, final DynamicJValue dynJv) {
        return dynJv.raw();
    }
    
    public static MonadicJValue dynamic2monadic(final DynamicJValueImplicits $this, final DynamicJValue dynJv) {
        return new MonadicJValue(dynJv.raw());
    }
    
    public static DynamicJValue dyn(final DynamicJValueImplicits $this, final JsonAST.JValue jv) {
        return new DynamicJValue(jv);
    }
    
    public static void $init$(final DynamicJValueImplicits $this) {
    }
}
