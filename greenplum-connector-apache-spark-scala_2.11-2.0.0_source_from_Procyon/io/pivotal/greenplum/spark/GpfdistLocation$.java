// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction2;

public final class GpfdistLocation$ extends AbstractFunction2<String, Object, GpfdistLocation> implements Serializable
{
    public static final GpfdistLocation$ MODULE$;
    
    static {
        new GpfdistLocation$();
    }
    
    public final String toString() {
        return "GpfdistLocation";
    }
    
    public GpfdistLocation apply(final String server, final int port) {
        return new GpfdistLocation(server, port);
    }
    
    public Option<Tuple2<String, Object>> unapply(final GpfdistLocation x$0) {
        return (Option<Tuple2<String, Object>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.server(), (Object)BoxesRunTime.boxToInteger(x$0.port()))));
    }
    
    private Object readResolve() {
        return GpfdistLocation$.MODULE$;
    }
    
    private GpfdistLocation$() {
        MODULE$ = this;
    }
}
