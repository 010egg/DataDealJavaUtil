// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Some;
import scala.None$;
import scala.Tuple5;
import scala.Option;
import scala.Serializable;
import scala.collection.Seq;
import scala.runtime.AbstractFunction5;

public final class SingletonDescriptor$ extends AbstractFunction5<String, String, ScalaType, Object, Seq<PropertyDescriptor>, SingletonDescriptor> implements Serializable
{
    public static final SingletonDescriptor$ MODULE$;
    
    static {
        new SingletonDescriptor$();
    }
    
    public final String toString() {
        return "SingletonDescriptor";
    }
    
    public SingletonDescriptor apply(final String simpleName, final String fullName, final ScalaType erasure, final Object instance, final Seq<PropertyDescriptor> properties) {
        return new SingletonDescriptor(simpleName, fullName, erasure, instance, properties);
    }
    
    public Option<Tuple5<String, String, ScalaType, Object, Seq<PropertyDescriptor>>> unapply(final SingletonDescriptor x$0) {
        return (Option<Tuple5<String, String, ScalaType, Object, Seq<PropertyDescriptor>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple5((Object)x$0.simpleName(), (Object)x$0.fullName(), (Object)x$0.erasure(), x$0.instance(), (Object)x$0.properties())));
    }
    
    private Object readResolve() {
        return SingletonDescriptor$.MODULE$;
    }
    
    private SingletonDescriptor$() {
        MODULE$ = this;
    }
}
