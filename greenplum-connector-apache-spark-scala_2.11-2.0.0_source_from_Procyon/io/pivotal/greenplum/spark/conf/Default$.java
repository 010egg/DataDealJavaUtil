// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.conf;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction1;

public final class Default$ extends AbstractFunction1<String, Default> implements Serializable
{
    public static final Default$ MODULE$;
    
    static {
        new Default$();
    }
    
    public final String toString() {
        return "Default";
    }
    
    public Default apply(final String value) {
        return new Default(value);
    }
    
    public Option<String> unapply(final Default x$0) {
        return (Option<String>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.value()));
    }
    
    private Object readResolve() {
        return Default$.MODULE$;
    }
    
    private Default$() {
        MODULE$ = this;
    }
}
