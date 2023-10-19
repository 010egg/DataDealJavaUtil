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

public final class ShortTypeHints$ extends AbstractFunction1<List<Class<?>>, ShortTypeHints> implements Serializable
{
    public static final ShortTypeHints$ MODULE$;
    
    static {
        new ShortTypeHints$();
    }
    
    public final String toString() {
        return "ShortTypeHints";
    }
    
    public ShortTypeHints apply(final List<Class<?>> hints) {
        return new ShortTypeHints(hints);
    }
    
    public Option<List<Class<?>>> unapply(final ShortTypeHints x$0) {
        return (Option<List<Class<?>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.hints()));
    }
    
    private Object readResolve() {
        return ShortTypeHints$.MODULE$;
    }
    
    private ShortTypeHints$() {
        MODULE$ = this;
    }
}
