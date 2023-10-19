// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Some;
import scala.None$;
import scala.Tuple4;
import scala.Option;
import scala.Serializable;
import java.lang.reflect.Field;
import scala.runtime.AbstractFunction4;

public final class PropertyDescriptor$ extends AbstractFunction4<String, String, ScalaType, Field, PropertyDescriptor> implements Serializable
{
    public static final PropertyDescriptor$ MODULE$;
    
    static {
        new PropertyDescriptor$();
    }
    
    public final String toString() {
        return "PropertyDescriptor";
    }
    
    public PropertyDescriptor apply(final String name, final String mangledName, final ScalaType returnType, final Field field) {
        return new PropertyDescriptor(name, mangledName, returnType, field);
    }
    
    public Option<Tuple4<String, String, ScalaType, Field>> unapply(final PropertyDescriptor x$0) {
        return (Option<Tuple4<String, String, ScalaType, Field>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple4((Object)x$0.name(), (Object)x$0.mangledName(), (Object)x$0.returnType(), (Object)x$0.field())));
    }
    
    private Object readResolve() {
        return PropertyDescriptor$.MODULE$;
    }
    
    private PropertyDescriptor$() {
        MODULE$ = this;
    }
}
