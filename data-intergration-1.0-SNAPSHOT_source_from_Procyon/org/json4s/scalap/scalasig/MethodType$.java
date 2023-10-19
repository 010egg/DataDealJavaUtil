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

public final class MethodType$ extends AbstractFunction2<Type, Seq<Symbol>, MethodType> implements Serializable
{
    public static final MethodType$ MODULE$;
    
    static {
        new MethodType$();
    }
    
    public final String toString() {
        return "MethodType";
    }
    
    public MethodType apply(final Type resultType, final Seq<Symbol> paramSymbols) {
        return new MethodType(resultType, paramSymbols);
    }
    
    public Option<Tuple2<Type, Seq<Symbol>>> unapply(final MethodType x$0) {
        return (Option<Tuple2<Type, Seq<Symbol>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.resultType(), (Object)x$0.paramSymbols())));
    }
    
    private Object readResolve() {
        return MethodType$.MODULE$;
    }
    
    private MethodType$() {
        MODULE$ = this;
    }
}
