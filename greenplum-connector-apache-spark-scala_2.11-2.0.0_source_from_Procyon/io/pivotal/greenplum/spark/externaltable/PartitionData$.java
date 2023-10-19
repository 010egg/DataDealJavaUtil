// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Tuple4;
import scala.Option;
import scala.Serializable;
import scala.Function1;
import scala.collection.immutable.List;
import org.apache.spark.sql.Row;
import scala.collection.Iterator;
import scala.runtime.AbstractFunction4;

public final class PartitionData$ extends AbstractFunction4<Object, Iterator<Row>, List<Row>, Function1<Row, Row>, PartitionData> implements Serializable
{
    public static final PartitionData$ MODULE$;
    
    static {
        new PartitionData$();
    }
    
    public final String toString() {
        return "PartitionData";
    }
    
    public PartitionData apply(final int partitionIndex, final Iterator<Row> rowIterator, final List<Row> rows, final Function1<Row, Row> rowTransformer) {
        return new PartitionData(partitionIndex, rowIterator, rows, rowTransformer);
    }
    
    public Option<Tuple4<Object, Iterator<Row>, List<Row>, Function1<Row, Row>>> unapply(final PartitionData x$0) {
        return (Option<Tuple4<Object, Iterator<Row>, List<Row>, Function1<Row, Row>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple4((Object)BoxesRunTime.boxToInteger(x$0.partitionIndex()), (Object)x$0.rowIterator(), (Object)x$0.rows(), (Object)x$0.rowTransformer())));
    }
    
    public Iterator<Row> $lessinit$greater$default$2() {
        return null;
    }
    
    public List<Row> $lessinit$greater$default$3() {
        return null;
    }
    
    public Function1<Row, Row> $lessinit$greater$default$4() {
        return RowTransformer$.MODULE$.identityFunction();
    }
    
    public Iterator<Row> apply$default$2() {
        return null;
    }
    
    public List<Row> apply$default$3() {
        return null;
    }
    
    public Function1<Row, Row> apply$default$4() {
        return RowTransformer$.MODULE$.identityFunction();
    }
    
    private Object readResolve() {
        return PartitionData$.MODULE$;
    }
    
    private PartitionData$() {
        MODULE$ = this;
    }
}
