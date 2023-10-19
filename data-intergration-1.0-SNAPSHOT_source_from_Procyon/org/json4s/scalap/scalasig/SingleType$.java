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

public final class SingleType$ extends AbstractFunction2<Type, Symbol, SingleType> implements Serializable
{
    public static final SingleType$ MODULE$;
    
    static {
        new SingleType$();
    }
    
    public final String toString() {
        return "SingleType";
    }
    
    public SingleType apply(final Type typeRef, final Symbol symbol) {
        return new SingleType(typeRef, symbol);
    }
    
    public Option<Tuple2<Type, Symbol>> unapply(final SingleType x$0) {
        return (Option<Tuple2<Type, Symbol>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.typeRef(), (Object)x$0.symbol())));
    }
    
    private Object readResolve() {
        return SingleType$.MODULE$;
    }
    
    private SingleType$() {
        MODULE$ = this;
    }
}
