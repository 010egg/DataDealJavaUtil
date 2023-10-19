// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.util.Try;
import scala.PartialFunction;

public final class ErrorHandling$
{
    public static final ErrorHandling$ MODULE$;
    
    static {
        new ErrorHandling$();
    }
    
    public <U> PartialFunction<Throwable, Try<U>> wrapErrorMessage(final String message) {
        return (PartialFunction<Throwable, Try<U>>)new ErrorHandling$$anonfun$wrapErrorMessage.ErrorHandling$$anonfun$wrapErrorMessage$1(message);
    }
    
    private ErrorHandling$() {
        MODULE$ = this;
    }
}
