// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import org.apache.commons.codec.binary.Hex;
import scala.reflect.ClassTag$;
import scala.collection.immutable.IndexedSeq$;
import scala.runtime.RichInt$;
import scala.collection.TraversableOnce;
import org.apache.commons.codec.digest.DigestUtils;
import scala.collection.mutable.StringBuilder;
import scala.Function1;
import scala.MatchError;
import io.pivotal.greenplum.spark.conf.GreenplumOptions;
import org.apache.spark.sql.types.AtomicType;
import scala.collection.Seq$;
import scala.Function0;
import scala.util.Try$;
import scala.util.Try;
import org.apache.spark.sql.types.StructType;
import scala.None$;
import org.apache.spark.sql.types.DateType$;
import org.apache.spark.sql.types.TimestampType$;
import org.apache.spark.sql.types.LongType$;
import org.apache.spark.sql.types.IntegerType$;
import scala.runtime.BoxesRunTime;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import org.apache.spark.sql.types.DecimalType;
import org.apache.spark.sql.types.ShortType$;
import org.apache.spark.sql.types.DoubleType$;
import org.apache.spark.sql.types.FloatType$;
import org.apache.spark.sql.types.BooleanType$;
import org.apache.spark.sql.types.BinaryType$;
import scala.Some;
import org.apache.spark.sql.types.StringType$;
import scala.Option;
import org.apache.spark.sql.types.DataType;

public final class GreenplumTableManager$
{
    public static final GreenplumTableManager$ MODULE$;
    
    static {
        new GreenplumTableManager$();
    }
    
    public Option<String> gpdbColumnType(final DataType dt) {
        Object module$;
        if (StringType$.MODULE$.equals(dt)) {
            module$ = new Some((Object)"TEXT");
        }
        else if (BinaryType$.MODULE$.equals(dt)) {
            module$ = new Some((Object)"BYTEA");
        }
        else if (BooleanType$.MODULE$.equals(dt)) {
            module$ = new Some((Object)"BOOLEAN");
        }
        else if (FloatType$.MODULE$.equals(dt)) {
            module$ = new Some((Object)"FLOAT4");
        }
        else if (DoubleType$.MODULE$.equals(dt)) {
            module$ = new Some((Object)"FLOAT8");
        }
        else if (ShortType$.MODULE$.equals(dt)) {
            module$ = new Some((Object)"SMALLINT");
        }
        else if (dt instanceof DecimalType) {
            final DecimalType decimalType = (DecimalType)dt;
            module$ = new Some((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "NUMERIC(", ",", ")" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { BoxesRunTime.boxToInteger(decimalType.precision()), BoxesRunTime.boxToInteger(decimalType.scale()) })));
        }
        else if (IntegerType$.MODULE$.equals(dt)) {
            module$ = new Some((Object)"INTEGER");
        }
        else if (LongType$.MODULE$.equals(dt)) {
            module$ = new Some((Object)"BIGINT");
        }
        else if (TimestampType$.MODULE$.equals(dt)) {
            module$ = new Some((Object)"TIMESTAMP");
        }
        else if (DateType$.MODULE$.equals(dt)) {
            module$ = new Some((Object)"DATE");
        }
        else {
            module$ = None$.MODULE$;
        }
        return (Option<String>)module$;
    }
    
    public Try<String> createTableColumnList(final StructType sparkSchema) {
        return (Try<String>)Try$.MODULE$.apply((Function0)new GreenplumTableManager$$anonfun$createTableColumnList.GreenplumTableManager$$anonfun$createTableColumnList$1(sparkSchema));
    }
    
    public Seq<DataType> getSupportedSparkDataTypes() {
        return (Seq<DataType>)Seq$.MODULE$.apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new AtomicType[] { (AtomicType)StringType$.MODULE$, (AtomicType)BinaryType$.MODULE$, (AtomicType)BooleanType$.MODULE$, (AtomicType)FloatType$.MODULE$, (AtomicType)DoubleType$.MODULE$, (AtomicType)ShortType$.MODULE$, (AtomicType)new GreenplumTableManager$$anon.GreenplumTableManager$$anon$1(), (AtomicType)IntegerType$.MODULE$, (AtomicType)LongType$.MODULE$, (AtomicType)TimestampType$.MODULE$, (AtomicType)DateType$.MODULE$ }));
    }
    
    public Try<String> createTableStatement(final GreenplumQualifiedName tableName, final StructType schema, final GreenplumOptions options) {
        final Option<String> distributedBy = options.distributedBy();
        String s;
        if (distributedBy instanceof Some) {
            final String columns = (String)((Some)distributedBy).x();
            s = new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { " DISTRIBUTED BY (", ")" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { columns }));
        }
        else {
            if (!None$.MODULE$.equals(distributedBy)) {
                throw new MatchError((Object)distributedBy);
            }
            s = " DISTRIBUTED RANDOMLY";
        }
        final String distribution = s;
        return (Try<String>)this.createTableColumnList(schema).map((Function1)new GreenplumTableManager$$anonfun$createTableStatement.GreenplumTableManager$$anonfun$createTableStatement$1(tableName, distribution));
    }
    
    public String generateExternalTableNamePrefix(final String applicationId, final String tableName) {
        final String applicationTableHash = this.foldedMd5Hex(new StringBuilder().append((Object)applicationId).append((Object)tableName).toString());
        return new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "spark_", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { applicationTableHash }));
    }
    
    public String generateExternalTableNameColumnPrefix(final String applicationId, final String table, final Seq<String> columns) {
        final String applicationTableHash = this.generateExternalTableNamePrefix(applicationId, table);
        final String columnsHash = this.foldedMd5Hex(columns.mkString(","));
        return new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "", "_", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { applicationTableHash, columnsHash }));
    }
    
    public String generateExternalTableName(final String applicationId, final String table, final String executorId, final long threadId, final Seq<String> columns) {
        final String prefix = this.generateExternalTableNameColumnPrefix(applicationId, table, columns);
        return new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "", "_", "_", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { prefix, executorId, BoxesRunTime.boxToLong(threadId) }));
    }
    
    public Seq<String> generateExternalTableName$default$5() {
        return (Seq<String>)Seq$.MODULE$.empty();
    }
    
    private String foldedMd5Hex(final String s) {
        final byte[] md5In16 = DigestUtils.md5(s);
        final byte[] md5In17 = (byte[])((TraversableOnce)RichInt$.MODULE$.to$extension0(Predef$.MODULE$.intWrapper(0), 7).map((Function1)new GreenplumTableManager$$anonfun.GreenplumTableManager$$anonfun$2(md5In16), IndexedSeq$.MODULE$.canBuildFrom())).toArray(ClassTag$.MODULE$.Byte());
        final String md5In16Hex = Hex.encodeHexString(md5In17);
        return md5In16Hex;
    }
    
    private GreenplumTableManager$() {
        MODULE$ = this;
    }
}
