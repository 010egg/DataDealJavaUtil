// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Tuple6;
import scala.Serializable;
import scala.Option;
import scala.runtime.AbstractFunction6;

public final class SymbolInfo$ extends AbstractFunction6<String, Symbol, Object, Option<Object>, Object, ScalaSig.Entry, SymbolInfo> implements Serializable
{
    public static final SymbolInfo$ MODULE$;
    
    static {
        new SymbolInfo$();
    }
    
    public final String toString() {
        return "SymbolInfo";
    }
    
    public SymbolInfo apply(final String name, final Symbol owner, final int flags, final Option<Object> privateWithin, final int info, final ScalaSig.Entry entry) {
        return new SymbolInfo(name, owner, flags, privateWithin, info, entry);
    }
    
    public Option<Tuple6<String, Symbol, Object, Option<Object>, Object, ScalaSig.Entry>> unapply(final SymbolInfo x$0) {
        return (Option<Tuple6<String, Symbol, Object, Option<Object>, Object, ScalaSig.Entry>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple6((Object)x$0.name(), (Object)x$0.owner(), (Object)BoxesRunTime.boxToInteger(x$0.flags()), (Object)x$0.privateWithin(), (Object)BoxesRunTime.boxToInteger(x$0.info()), (Object)x$0.entry())));
    }
    
    private Object readResolve() {
        return SymbolInfo$.MODULE$;
    }
    
    private SymbolInfo$() {
        MODULE$ = this;
    }
}
