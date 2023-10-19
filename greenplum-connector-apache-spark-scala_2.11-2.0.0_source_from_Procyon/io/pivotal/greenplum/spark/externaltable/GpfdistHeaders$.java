// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Tuple3;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction3;

public final class GpfdistHeaders$ extends AbstractFunction3<String, Object, Object, GpfdistHeaders> implements Serializable
{
    public static final GpfdistHeaders$ MODULE$;
    
    static {
        new GpfdistHeaders$();
    }
    
    public final String toString() {
        return "GpfdistHeaders";
    }
    
    public GpfdistHeaders apply(final String transactionId, final int segmentId, final int segmentCount) {
        return new GpfdistHeaders(transactionId, segmentId, segmentCount);
    }
    
    public Option<Tuple3<String, Object, Object>> unapply(final GpfdistHeaders x$0) {
        return (Option<Tuple3<String, Object, Object>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)x$0.transactionId(), (Object)BoxesRunTime.boxToInteger(x$0.segmentId()), (Object)BoxesRunTime.boxToInteger(x$0.segmentCount()))));
    }
    
    private Object readResolve() {
        return GpfdistHeaders$.MODULE$;
    }
    
    private GpfdistHeaders$() {
        MODULE$ = this;
    }
}
