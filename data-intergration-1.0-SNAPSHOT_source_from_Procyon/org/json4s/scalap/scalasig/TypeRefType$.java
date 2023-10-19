// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Tuple3;
import scala.Option;
import scala.Serializable;
import scala.collection.Seq;
import scala.runtime.AbstractFunction3;

public final class TypeRefType$ extends AbstractFunction3<Type, Symbol, Seq<Type>, TypeRefType> implements Serializable
{
    public static final TypeRefType$ MODULE$;
    
    static {
        new TypeRefType$();
    }
    
    public final String toString() {
        return "TypeRefType";
    }
    
    public TypeRefType apply(final Type prefix, final Symbol symbol, final Seq<Type> typeArgs) {
        return new TypeRefType(prefix, symbol, typeArgs);
    }
    
    public Option<Tuple3<Type, Symbol, Seq<Type>>> unapply(final TypeRefType x$0) {
        return (Option<Tuple3<Type, Symbol, Seq<Type>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)x$0.prefix(), (Object)x$0.symbol(), (Object)x$0.typeArgs())));
    }
    
    private Object readResolve() {
        return TypeRefType$.MODULE$;
    }
    
    private TypeRefType$() {
        MODULE$ = this;
    }
}
