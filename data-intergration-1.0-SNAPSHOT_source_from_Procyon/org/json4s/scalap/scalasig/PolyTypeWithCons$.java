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

public final class PolyTypeWithCons$ extends AbstractFunction3<Type, Seq<TypeSymbol>, String, PolyTypeWithCons> implements Serializable
{
    public static final PolyTypeWithCons$ MODULE$;
    
    static {
        new PolyTypeWithCons$();
    }
    
    public final String toString() {
        return "PolyTypeWithCons";
    }
    
    public PolyTypeWithCons apply(final Type typeRef, final Seq<TypeSymbol> symbols, final String cons) {
        return new PolyTypeWithCons(typeRef, symbols, cons);
    }
    
    public Option<Tuple3<Type, Seq<TypeSymbol>, String>> unapply(final PolyTypeWithCons x$0) {
        return (Option<Tuple3<Type, Seq<TypeSymbol>, String>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)x$0.typeRef(), (Object)x$0.symbols(), (Object)x$0.cons())));
    }
    
    private Object readResolve() {
        return PolyTypeWithCons$.MODULE$;
    }
    
    private PolyTypeWithCons$() {
        MODULE$ = this;
    }
}
