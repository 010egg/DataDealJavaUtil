// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.collection.immutable.List;
import scala.runtime.AbstractFunction1;

public final class FullTypeHints$ extends AbstractFunction1<List<Class<?>>, FullTypeHints> implements Serializable
{
    public static final FullTypeHints$ MODULE$;
    
    static {
        new FullTypeHints$();
    }
    
    public final String toString() {
        return "FullTypeHints";
    }
    
    public FullTypeHints apply(final List<Class<?>> hints) {
        return new FullTypeHints(hints);
    }
    
    public Option<List<Class<?>>> unapply(final FullTypeHints x$0) {
        return (Option<List<Class<?>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.hints()));
    }
    
    private Object readResolve() {
        return FullTypeHints$.MODULE$;
    }
    
    private FullTypeHints$() {
        MODULE$ = this;
    }
}
