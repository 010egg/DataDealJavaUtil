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

public final class ExistentialType$ extends AbstractFunction2<Type, Seq<Symbol>, ExistentialType> implements Serializable
{
    public static final ExistentialType$ MODULE$;
    
    static {
        new ExistentialType$();
    }
    
    public final String toString() {
        return "ExistentialType";
    }
    
    public ExistentialType apply(final Type typeRef, final Seq<Symbol> symbols) {
        return new ExistentialType(typeRef, symbols);
    }
    
    public Option<Tuple2<Type, Seq<Symbol>>> unapply(final ExistentialType x$0) {
        return (Option<Tuple2<Type, Seq<Symbol>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.typeRef(), (Object)x$0.symbols())));
    }
    
    private Object readResolve() {
        return ExistentialType$.MODULE$;
    }
    
    private ExistentialType$() {
        MODULE$ = this;
    }
}
