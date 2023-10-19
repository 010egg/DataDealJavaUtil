// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.jdbc;

import scala.runtime.BoxesRunTime;
import scala.Some;
import scala.Tuple2$mcJJ$sp;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction2;

public final class ColumnValueRange$ extends AbstractFunction2<Object, Object, ColumnValueRange> implements Serializable
{
    public static final ColumnValueRange$ MODULE$;
    
    static {
        new ColumnValueRange$();
    }
    
    public final String toString() {
        return "ColumnValueRange";
    }
    
    public ColumnValueRange apply(final long min, final long max) {
        return new ColumnValueRange(min, max);
    }
    
    public Option<Tuple2<Object, Object>> unapply(final ColumnValueRange x$0) {
        return (Option<Tuple2<Object, Object>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2$mcJJ$sp(x$0.min(), x$0.max())));
    }
    
    private Object readResolve() {
        return ColumnValueRange$.MODULE$;
    }
    
    private ColumnValueRange$() {
        MODULE$ = this;
    }
}
