// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction1;

public final class TypeSymbol$ extends AbstractFunction1<SymbolInfo, TypeSymbol> implements Serializable
{
    public static final TypeSymbol$ MODULE$;
    
    static {
        new TypeSymbol$();
    }
    
    public final String toString() {
        return "TypeSymbol";
    }
    
    public TypeSymbol apply(final SymbolInfo symbolInfo) {
        return new TypeSymbol(symbolInfo);
    }
    
    public Option<SymbolInfo> unapply(final TypeSymbol x$0) {
        return (Option<SymbolInfo>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.symbolInfo()));
    }
    
    private Object readResolve() {
        return TypeSymbol$.MODULE$;
    }
    
    private TypeSymbol$() {
        MODULE$ = this;
    }
}
