// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.Serializable;
import scala.Option;
import scala.runtime.AbstractFunction2;

public final class MethodSymbol$ extends AbstractFunction2<SymbolInfo, Option<Object>, MethodSymbol> implements Serializable
{
    public static final MethodSymbol$ MODULE$;
    
    static {
        new MethodSymbol$();
    }
    
    public final String toString() {
        return "MethodSymbol";
    }
    
    public MethodSymbol apply(final SymbolInfo symbolInfo, final Option<Object> aliasRef) {
        return new MethodSymbol(symbolInfo, aliasRef);
    }
    
    public Option<Tuple2<SymbolInfo, Option<Object>>> unapply(final MethodSymbol x$0) {
        return (Option<Tuple2<SymbolInfo, Option<Object>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.symbolInfo(), (Object)x$0.aliasRef())));
    }
    
    private Object readResolve() {
        return MethodSymbol$.MODULE$;
    }
    
    private MethodSymbol$() {
        MODULE$ = this;
    }
}
