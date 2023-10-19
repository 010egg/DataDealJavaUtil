// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.Serializable;
import scala.collection.Seq;
import scala.runtime.AbstractFunction2;

public final class PolyType$ extends AbstractFunction2<Type, Seq<TypeSymbol>, PolyType> implements Serializable
{
    public static final PolyType$ MODULE$;
    
    static {
        new PolyType$();
    }
    
    public final String toString() {
        return "PolyType";
    }
    
    public PolyType apply(final Type typeRef, final Seq<TypeSymbol> symbols) {
        return new PolyType(typeRef, symbols);
    }
    
    public Option<Tuple2<Type, Seq<TypeSymbol>>> unapply(final PolyType x$0) {
        return (Option<Tuple2<Type, Seq<TypeSymbol>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.typeRef(), (Object)x$0.symbols())));
    }
    
    private Object readResolve() {
        return PolyType$.MODULE$;
    }
    
    private PolyType$() {
        MODULE$ = this;
    }
}
