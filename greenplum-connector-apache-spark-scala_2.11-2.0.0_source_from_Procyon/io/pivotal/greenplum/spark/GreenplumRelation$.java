// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.Some;
import scala.None$;
import scala.Tuple3;
import scala.Option;
import org.apache.spark.sql.SQLContext;
import io.pivotal.greenplum.spark.conf.GreenplumOptions;
import org.apache.spark.sql.types.StructType;
import scala.Array$;
import scala.reflect.ClassTag$;
import scala.Function1;
import scala.runtime.RichInt$;
import scala.runtime.LongRef;
import scala.runtime.ObjectRef;
import scala.collection.mutable.ArrayBuffer;
import io.pivotal.greenplum.spark.externaltable.SqlObjectNameUtils$;
import scala.Predef$;
import io.pivotal.greenplum.spark.jdbc.ColumnValueRange;
import org.apache.spark.sql.types.StructField;
import scala.Function0;
import org.slf4j.Logger;
import scala.Serializable;

public final class GreenplumRelation$ implements Logging, Serializable
{
    public static final GreenplumRelation$ MODULE$;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    static {
        new GreenplumRelation$();
    }
    
    @Override
    public Logger io$pivotal$greenplum$spark$Logging$$log_() {
        return this.io$pivotal$greenplum$spark$Logging$$log_;
    }
    
    @Override
    public void io$pivotal$greenplum$spark$Logging$$log__$eq(final Logger x$1) {
        this.io$pivotal$greenplum$spark$Logging$$log_ = x$1;
    }
    
    @Override
    public Logger log() {
        return Logging$class.log(this);
    }
    
    @Override
    public void logWarning(final Function0<String> msg) {
        Logging$class.logWarning(this, msg);
    }
    
    @Override
    public void logDebug(final Function0<String> msg) {
        Logging$class.logDebug(this, msg);
    }
    
    public GreenplumPartition[] columnPartition(final int requestedPartitions, final StructField partitionColumn, final ColumnValueRange valueRange) {
        GreenplumPartition[] array;
        if (this.onlyOnePartition$1(partitionColumn, valueRange)) {
            array = new GreenplumPartition[] { new GreenplumPartition("1 = 1", 0) };
        }
        else {
            final long lowerBound = valueRange.min();
            final long upperBound = valueRange.max();
            final long distinctValuesInRange = upperBound - lowerBound;
            Predef$.MODULE$.require(lowerBound <= upperBound, (Function0)new GreenplumRelation$$anonfun$columnPartition.GreenplumRelation$$anonfun$columnPartition$1(lowerBound, upperBound));
            int n;
            if (distinctValuesInRange >= requestedPartitions || this.overflows$1(distinctValuesInRange)) {
                n = requestedPartitions;
            }
            else {
                this.logWarning((Function0<String>)new GreenplumRelation$$anonfun.GreenplumRelation$$anonfun$1(requestedPartitions, lowerBound, upperBound, distinctValuesInRange));
                n = (int)distinctValuesInRange;
            }
            final int numPartitions = n;
            final long stride = upperBound / numPartitions - lowerBound / numPartitions;
            final String column = SqlObjectNameUtils$.MODULE$.escape(partitionColumn.name());
            final boolean nullable = partitionColumn.nullable();
            final ObjectRef partitionArrayBuf = ObjectRef.create((Object)new ArrayBuffer());
            final LongRef currentPartitionBoundaryValue = LongRef.create(lowerBound);
            RichInt$.MODULE$.until$extension0(Predef$.MODULE$.intWrapper(0), numPartitions).foreach((Function1)new GreenplumRelation$$anonfun$columnPartition.GreenplumRelation$$anonfun$columnPartition$2(numPartitions, stride, column, nullable, partitionArrayBuf, currentPartitionBoundaryValue));
            array = (GreenplumPartition[])((ArrayBuffer)partitionArrayBuf.elem).toArray(ClassTag$.MODULE$.apply((Class)GreenplumPartition.class));
        }
        return array;
    }
    
    public GreenplumPartition[] segmentPartitions(final int[] segmentIds) {
        return (GreenplumPartition[])Predef$.MODULE$.intArrayOps(segmentIds).map((Function1)new GreenplumRelation$$anonfun$segmentPartitions.GreenplumRelation$$anonfun$segmentPartitions$1(), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)GreenplumPartition.class)));
    }
    
    public GreenplumRelation apply(final StructType schema, final GreenplumPartition[] parts, final GreenplumOptions greenplumOptions, final SQLContext sqlContext) {
        return new GreenplumRelation(schema, parts, greenplumOptions, sqlContext);
    }
    
    public Option<Tuple3<StructType, GreenplumPartition[], GreenplumOptions>> unapply(final GreenplumRelation x$0) {
        return (Option<Tuple3<StructType, GreenplumPartition[], GreenplumOptions>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)x$0.schema(), (Object)x$0.parts(), (Object)x$0.greenplumOptions())));
    }
    
    private Object readResolve() {
        return GreenplumRelation$.MODULE$;
    }
    
    private final boolean onlyOnePartition$1(final StructField partitionColumn$1, final ColumnValueRange valueRange$1) {
        return partitionColumn$1 == null || valueRange$1 == null || valueRange$1.min() == valueRange$1.max();
    }
    
    private final boolean overflows$1(final long positiveValue) {
        return positiveValue < 0L;
    }
    
    private GreenplumRelation$() {
        Logging$class.$init$(MODULE$ = this);
    }
}
