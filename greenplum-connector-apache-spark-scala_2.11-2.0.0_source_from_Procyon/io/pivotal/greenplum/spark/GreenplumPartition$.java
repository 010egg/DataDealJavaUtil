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

public final class GreenplumPartition$ extends AbstractFunction2<String, Object, GreenplumPartition> implements Serializable
{
    public static final GreenplumPartition$ MODULE$;
    
    static {
        new GreenplumPartition$();
    }
    
    public final String toString() {
        return "GreenplumPartition";
    }
    
    public GreenplumPartition apply(final String whereClause, final int idx) {
        return new GreenplumPartition(whereClause, idx);
    }
    
    public Option<Tuple2<String, Object>> unapply(final GreenplumPartition x$0) {
        return (Option<Tuple2<String, Object>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.whereClause(), (Object)BoxesRunTime.boxToInteger(x$0.idx()))));
    }
    
    private Object readResolve() {
        return GreenplumPartition$.MODULE$;
    }
    
    private GreenplumPartition$() {
        MODULE$ = this;
    }
}
