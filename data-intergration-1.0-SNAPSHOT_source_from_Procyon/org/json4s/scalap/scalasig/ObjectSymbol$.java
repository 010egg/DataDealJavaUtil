// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction1;

public final class ObjectSymbol$ extends AbstractFunction1<SymbolInfo, ObjectSymbol> implements Serializable
{
    public static final ObjectSymbol$ MODULE$;
    
    static {
        new ObjectSymbol$();
    }
    
    public final String toString() {
        return "ObjectSymbol";
    }
    
    public ObjectSymbol apply(final SymbolInfo symbolInfo) {
        return new ObjectSymbol(symbolInfo);
    }
    
    public Option<SymbolInfo> unapply(final ObjectSymbol x$0) {
        return (Option<SymbolInfo>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.symbolInfo()));
    }
    
    private Object readResolve() {
        return ObjectSymbol$.MODULE$;
    }
    
    private ObjectSymbol$() {
        MODULE$ = this;
    }
}
