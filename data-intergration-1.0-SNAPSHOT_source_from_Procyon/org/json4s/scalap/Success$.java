// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.Serializable;

public final class Success$ implements Serializable
{
    public static final Success$ MODULE$;
    
    static {
        new Success$();
    }
    
    @Override
    public final String toString() {
        return "Success";
    }
    
    public <Out, A> Success<Out, A> apply(final Out out, final A value) {
        return new Success<Out, A>(out, value);
    }
    
    public <Out, A> Option<Tuple2<Out, A>> unapply(final Success<Out, A> x$0) {
        return (Option<Tuple2<Out, A>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.out(), (Object)x$0.value())));
    }
    
    private Object readResolve() {
        return Success$.MODULE$;
    }
    
    private Success$() {
        MODULE$ = this;
    }
}
