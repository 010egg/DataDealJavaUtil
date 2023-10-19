// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Tuple3;
import scala.Option;
import scala.Serializable;
import scala.collection.Seq;
import scala.runtime.AbstractFunction3;

public final class ConstructorDescriptor$ extends AbstractFunction3<Seq<ConstructorParamDescriptor>, Executable, Object, ConstructorDescriptor> implements Serializable
{
    public static final ConstructorDescriptor$ MODULE$;
    
    static {
        new ConstructorDescriptor$();
    }
    
    public final String toString() {
        return "ConstructorDescriptor";
    }
    
    public ConstructorDescriptor apply(final Seq<ConstructorParamDescriptor> params, final Executable constructor, final boolean isPrimary) {
        return new ConstructorDescriptor(params, constructor, isPrimary);
    }
    
    public Option<Tuple3<Seq<ConstructorParamDescriptor>, Executable, Object>> unapply(final ConstructorDescriptor x$0) {
        return (Option<Tuple3<Seq<ConstructorParamDescriptor>, Executable, Object>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)x$0.params(), (Object)x$0.constructor(), (Object)BoxesRunTime.boxToBoolean(x$0.isPrimary()))));
    }
    
    private Object readResolve() {
        return ConstructorDescriptor$.MODULE$;
    }
    
    private ConstructorDescriptor$() {
        MODULE$ = this;
    }
}
