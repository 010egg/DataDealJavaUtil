// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Some;
import scala.None$;
import scala.Tuple6;
import scala.Serializable;
import scala.collection.Seq;
import scala.Option;
import scala.runtime.AbstractFunction6;

public final class ClassDescriptor$ extends AbstractFunction6<String, String, ScalaType, Option<SingletonDescriptor>, Seq<ConstructorDescriptor>, Seq<PropertyDescriptor>, ClassDescriptor> implements Serializable
{
    public static final ClassDescriptor$ MODULE$;
    
    static {
        new ClassDescriptor$();
    }
    
    public final String toString() {
        return "ClassDescriptor";
    }
    
    public ClassDescriptor apply(final String simpleName, final String fullName, final ScalaType erasure, final Option<SingletonDescriptor> companion, final Seq<ConstructorDescriptor> constructors, final Seq<PropertyDescriptor> properties) {
        return new ClassDescriptor(simpleName, fullName, erasure, companion, constructors, properties);
    }
    
    public Option<Tuple6<String, String, ScalaType, Option<SingletonDescriptor>, Seq<ConstructorDescriptor>, Seq<PropertyDescriptor>>> unapply(final ClassDescriptor x$0) {
        return (Option<Tuple6<String, String, ScalaType, Option<SingletonDescriptor>, Seq<ConstructorDescriptor>, Seq<PropertyDescriptor>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple6((Object)x$0.simpleName(), (Object)x$0.fullName(), (Object)x$0.erasure(), (Object)x$0.companion(), (Object)x$0.constructors(), (Object)x$0.properties())));
    }
    
    private Object readResolve() {
        return ClassDescriptor$.MODULE$;
    }
    
    private ClassDescriptor$() {
        MODULE$ = this;
    }
}
