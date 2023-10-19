// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Tuple3;
import scala.Serializable;
import scala.Option;
import scala.runtime.AbstractFunction3;

public final class ExternalSymbol$ extends AbstractFunction3<String, Option<Symbol>, ScalaSig.Entry, ExternalSymbol> implements Serializable
{
    public static final ExternalSymbol$ MODULE$;
    
    static {
        new ExternalSymbol$();
    }
    
    public final String toString() {
        return "ExternalSymbol";
    }
    
    public ExternalSymbol apply(final String name, final Option<Symbol> parent, final ScalaSig.Entry entry) {
        return new ExternalSymbol(name, parent, entry);
    }
    
    public Option<Tuple3<String, Option<Symbol>, ScalaSig.Entry>> unapply(final ExternalSymbol x$0) {
        return (Option<Tuple3<String, Option<Symbol>, ScalaSig.Entry>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)x$0.name(), (Object)x$0.parent(), (Object)x$0.entry())));
    }
    
    private Object readResolve() {
        return ExternalSymbol$.MODULE$;
    }
    
    private ExternalSymbol$() {
        MODULE$ = this;
    }
}
