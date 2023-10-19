// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction1;

public final class AliasSymbol$ extends AbstractFunction1<SymbolInfo, AliasSymbol> implements Serializable
{
    public static final AliasSymbol$ MODULE$;
    
    static {
        new AliasSymbol$();
    }
    
    public final String toString() {
        return "AliasSymbol";
    }
    
    public AliasSymbol apply(final SymbolInfo symbolInfo) {
        return new AliasSymbol(symbolInfo);
    }
    
    public Option<SymbolInfo> unapply(final AliasSymbol x$0) {
        return (Option<SymbolInfo>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.symbolInfo()));
    }
    
    private Object readResolve() {
        return AliasSymbol$.MODULE$;
    }
    
    private AliasSymbol$() {
        MODULE$ = this;
    }
}
