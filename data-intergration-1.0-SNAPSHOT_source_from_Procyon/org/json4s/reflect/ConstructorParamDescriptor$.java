// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Tuple5;
import scala.Serializable;
import scala.Function0;
import scala.Option;
import scala.runtime.AbstractFunction5;

public final class ConstructorParamDescriptor$ extends AbstractFunction5<String, String, Object, ScalaType, Option<Function0<Object>>, ConstructorParamDescriptor> implements Serializable
{
    public static final ConstructorParamDescriptor$ MODULE$;
    
    static {
        new ConstructorParamDescriptor$();
    }
    
    public final String toString() {
        return "ConstructorParamDescriptor";
    }
    
    public ConstructorParamDescriptor apply(final String name, final String mangledName, final int argIndex, final ScalaType argType, final Option<Function0<Object>> defaultValue) {
        return new ConstructorParamDescriptor(name, mangledName, argIndex, argType, defaultValue);
    }
    
    public Option<Tuple5<String, String, Object, ScalaType, Option<Function0<Object>>>> unapply(final ConstructorParamDescriptor x$0) {
        return (Option<Tuple5<String, String, Object, ScalaType, Option<Function0<Object>>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple5((Object)x$0.name(), (Object)x$0.mangledName(), (Object)BoxesRunTime.boxToInteger(x$0.argIndex()), (Object)x$0.argType(), (Object)x$0.defaultValue())));
    }
    
    private Object readResolve() {
        return ConstructorParamDescriptor$.MODULE$;
    }
    
    private ConstructorParamDescriptor$() {
        MODULE$ = this;
    }
}
