// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Tuple3;
import scala.Serializable;
import scala.Option;
import scala.runtime.AbstractFunction3;

public final class WebException$ extends AbstractFunction3<Object, String, Option<Throwable>, WebException> implements Serializable
{
    public static final WebException$ MODULE$;
    
    static {
        new WebException$();
    }
    
    public final String toString() {
        return "WebException";
    }
    
    public WebException apply(final int code, final String message, final Option<Throwable> cause) {
        return new WebException(code, message, cause);
    }
    
    public Option<Tuple3<Object, String, Option<Throwable>>> unapply(final WebException x$0) {
        return (Option<Tuple3<Object, String, Option<Throwable>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)BoxesRunTime.boxToInteger(x$0.code()), (Object)x$0.message(), (Object)x$0.cause())));
    }
    
    public String $lessinit$greater$default$2() {
        return null;
    }
    
    public Option<Throwable> $lessinit$greater$default$3() {
        return (Option<Throwable>)None$.MODULE$;
    }
    
    public String apply$default$2() {
        return null;
    }
    
    public Option<Throwable> apply$default$3() {
        return (Option<Throwable>)None$.MODULE$;
    }
    
    private Object readResolve() {
        return WebException$.MODULE$;
    }
    
    private WebException$() {
        MODULE$ = this;
    }
}
