// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

public final class DynamicJValue$ implements DynamicJValueImplicits
{
    public static final DynamicJValue$ MODULE$;
    
    static {
        new DynamicJValue$();
    }
    
    @Override
    public JsonAST.JValue dynamic2Jv(final DynamicJValue dynJv) {
        return DynamicJValueImplicits$class.dynamic2Jv(this, dynJv);
    }
    
    @Override
    public MonadicJValue dynamic2monadic(final DynamicJValue dynJv) {
        return DynamicJValueImplicits$class.dynamic2monadic(this, dynJv);
    }
    
    @Override
    public DynamicJValue dyn(final JsonAST.JValue jv) {
        return DynamicJValueImplicits$class.dyn(this, jv);
    }
    
    private DynamicJValue$() {
        DynamicJValueImplicits$class.$init$(MODULE$ = this);
    }
}
