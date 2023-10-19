// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.Serializable;

public final class $tilde$ implements Serializable
{
    public static final $tilde$ MODULE$;
    
    static {
        new $tilde$();
    }
    
    @Override
    public final String toString() {
        return "~";
    }
    
    public <A, B> $tilde<A, B> apply(final A _1, final B _2) {
        return new $tilde<A, B>(_1, _2);
    }
    
    public <A, B> Option<Tuple2<A, B>> unapply(final $tilde<A, B> x$0) {
        return (Option<Tuple2<A, B>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0._1(), (Object)x$0._2())));
    }
    
    private Object readResolve() {
        return $tilde$.MODULE$;
    }
    
    private $tilde$() {
        MODULE$ = this;
    }
}
