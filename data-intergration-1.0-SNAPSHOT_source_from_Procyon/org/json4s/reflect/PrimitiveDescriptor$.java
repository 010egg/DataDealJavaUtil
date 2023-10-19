// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.Serializable;
import scala.Function0;
import scala.Option;
import scala.runtime.AbstractFunction2;

public final class PrimitiveDescriptor$ extends AbstractFunction2<ScalaType, Option<Function0<Object>>, PrimitiveDescriptor> implements Serializable
{
    public static final PrimitiveDescriptor$ MODULE$;
    
    static {
        new PrimitiveDescriptor$();
    }
    
    public final String toString() {
        return "PrimitiveDescriptor";
    }
    
    public PrimitiveDescriptor apply(final ScalaType erasure, final Option<Function0<Object>> default) {
        return new PrimitiveDescriptor(erasure, default);
    }
    
    public Option<Tuple2<ScalaType, Option<Function0<Object>>>> unapply(final PrimitiveDescriptor x$0) {
        return (Option<Tuple2<ScalaType, Option<Function0<Object>>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.erasure(), (Object)x$0.default())));
    }
    
    public Option<Function0<Object>> $lessinit$greater$default$2() {
        return (Option<Function0<Object>>)None$.MODULE$;
    }
    
    public Option<Function0<Object>> apply$default$2() {
        return (Option<Function0<Object>>)None$.MODULE$;
    }
    
    private Object readResolve() {
        return PrimitiveDescriptor$.MODULE$;
    }
    
    private PrimitiveDescriptor$() {
        MODULE$ = this;
    }
}
