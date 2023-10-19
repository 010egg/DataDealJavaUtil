// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Function1;
import org.json4s.reflect.ScalaSigReader$;
import scala.Option;

public final class Compat$
{
    public static final Compat$ MODULE$;
    
    static {
        new Compat$();
    }
    
    public Option<Object> makeCollection(final Class<?> clazz, final Object array) {
        return (Option<Object>)ScalaSigReader$.MODULE$.companions(clazz.getName(), ScalaSigReader$.MODULE$.companions$default$2(), ScalaSigReader$.MODULE$.companions$default$3()).flatMap((Function1)new Compat$$anonfun$makeCollection.Compat$$anonfun$makeCollection$1()).map((Function1)new Compat$$anonfun$makeCollection.Compat$$anonfun$makeCollection$2(array));
    }
    
    private Compat$() {
        MODULE$ = this;
    }
}
