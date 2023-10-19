// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;

public final class Error$ implements Serializable
{
    public static final Error$ MODULE$;
    
    static {
        new Error$();
    }
    
    @Override
    public final String toString() {
        return "Error";
    }
    
    public <X> Error<X> apply(final X error) {
        return new Error<X>(error);
    }
    
    public <X> Option<X> unapply(final Error<X> x$0) {
        return (Option<X>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.error()));
    }
    
    private Object readResolve() {
        return Error$.MODULE$;
    }
    
    private Error$() {
        MODULE$ = this;
    }
}
