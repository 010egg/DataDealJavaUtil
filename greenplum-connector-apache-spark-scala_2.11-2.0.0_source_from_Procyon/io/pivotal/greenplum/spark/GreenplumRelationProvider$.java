// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.util.Left$;
import org.apache.spark.sql.types.DataType;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.collection.mutable.StringBuilder;
import org.apache.spark.sql.types.LongType$;
import scala.Unit$;
import scala.package$;
import org.apache.spark.sql.types.IntegerType$;
import scala.runtime.BoxedUnit;
import scala.util.Either;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.DataFrameReader;

public final class GreenplumRelationProvider$
{
    public static final GreenplumRelationProvider$ MODULE$;
    
    static {
        new GreenplumRelationProvider$();
    }
    
    public <A> DataFrameReader apply(final DataFrameReader t) {
        return t;
    }
    
    public DataFrameReader GreenplumDataFrameReader(final DataFrameReader reader) {
        return reader;
    }
    
    public Either<BoxedUnit, IllegalArgumentException> checkPartitionColumnType(final StructField column) {
        final DataType dataType = column.dataType();
        Object o;
        if (IntegerType$.MODULE$.equals(dataType)) {
            final Left$ left = package$.MODULE$.Left();
            final Unit$ module$ = Unit$.MODULE$;
            o = left.apply((Object)BoxedUnit.UNIT);
        }
        else if (LongType$.MODULE$.equals(dataType)) {
            final Left$ left2 = package$.MODULE$.Left();
            final Unit$ module$2 = Unit$.MODULE$;
            o = left2.apply((Object)BoxedUnit.UNIT);
        }
        else {
            final String message = new StringBuilder().append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "data type of '", "' is not supported for partitioning; " })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { column.name() }))).append((Object)"supported data types are bigint, bigserial, integer, and serial").toString();
            o = package$.MODULE$.Right().apply((Object)new IllegalArgumentException(message));
        }
        return (Either<BoxedUnit, IllegalArgumentException>)o;
    }
    
    private GreenplumRelationProvider$() {
        MODULE$ = this;
    }
}
