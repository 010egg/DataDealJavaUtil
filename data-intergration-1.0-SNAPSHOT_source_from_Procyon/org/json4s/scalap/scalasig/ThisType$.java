// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction1;

public final class ThisType$ extends AbstractFunction1<Symbol, ThisType> implements Serializable
{
    public static final ThisType$ MODULE$;
    
    static {
        new ThisType$();
    }
    
    public final String toString() {
        return "ThisType";
    }
    
    public ThisType apply(final Symbol symbol) {
        return new ThisType(symbol);
    }
    
    public Option<Symbol> unapply(final ThisType x$0) {
        return (Option<Symbol>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.symbol()));
    }
    
    private Object readResolve() {
        return ThisType$.MODULE$;
    }
    
    private ThisType$() {
        MODULE$ = this;
    }
}
