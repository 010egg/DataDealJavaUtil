// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction2;

public final class TypeBoundsType$ extends AbstractFunction2<Type, Type, TypeBoundsType> implements Serializable
{
    public static final TypeBoundsType$ MODULE$;
    
    static {
        new TypeBoundsType$();
    }
    
    public final String toString() {
        return "TypeBoundsType";
    }
    
    public TypeBoundsType apply(final Type lower, final Type upper) {
        return new TypeBoundsType(lower, upper);
    }
    
    public Option<Tuple2<Type, Type>> unapply(final TypeBoundsType x$0) {
        return (Option<Tuple2<Type, Type>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.lower(), (Object)x$0.upper())));
    }
    
    private Object readResolve() {
        return TypeBoundsType$.MODULE$;
    }
    
    private TypeBoundsType$() {
        MODULE$ = this;
    }
}
