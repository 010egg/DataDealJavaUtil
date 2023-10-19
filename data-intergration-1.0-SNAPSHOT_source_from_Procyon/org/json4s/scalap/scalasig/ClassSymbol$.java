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

public final class ClassSymbol$ extends AbstractFunction2<SymbolInfo, Option<Object>, ClassSymbol> implements Serializable
{
    public static final ClassSymbol$ MODULE$;
    
    static {
        new ClassSymbol$();
    }
    
    public final String toString() {
        return "ClassSymbol";
    }
    
    public ClassSymbol apply(final SymbolInfo symbolInfo, final Option<Object> thisTypeRef) {
        return new ClassSymbol(symbolInfo, thisTypeRef);
    }
    
    public Option<Tuple2<SymbolInfo, Option<Object>>> unapply(final ClassSymbol x$0) {
        return (Option<Tuple2<SymbolInfo, Option<Object>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.symbolInfo(), (Object)x$0.thisTypeRef())));
    }
    
    private Object readResolve() {
        return ClassSymbol$.MODULE$;
    }
    
    private ClassSymbol$() {
        MODULE$ = this;
    }
}
