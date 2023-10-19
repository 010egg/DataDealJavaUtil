// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.collection.TraversableOnce;
import scala.util.Failure;
import scala.StringContext;
import scala.Predef$;
import scala.collection.mutable.StringBuilder;
import scala.collection.GenSeq;
import scala.util.Success;
import scala.collection.Seq$;
import scala.util.Try;
import scala.collection.Seq;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import org.slf4j.Logger;
import org.apache.spark.sql.Row;
import scala.Function1;
import io.pivotal.greenplum.spark.Logging;

public final class RowTransformer$ implements Logging
{
    public static final RowTransformer$ MODULE$;
    private final Function1<Row, Row> identityFunction;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    static {
        new RowTransformer$();
    }
    
    @Override
    public Logger io$pivotal$greenplum$spark$Logging$$log_() {
        return this.io$pivotal$greenplum$spark$Logging$$log_;
    }
    
    @TraitSetter
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
    
    public Function1<Row, Row> identityFunction() {
        return this.identityFunction;
    }
    
    public Try<Function1<Row, Row>> getFunction(final Seq<String> sparkCols, final Seq<String> gpdbCols) {
        final Seq lowercaseSparkCols = (Seq)sparkCols.map((Function1)new RowTransformer$$anonfun.RowTransformer$$anonfun$2(), Seq$.MODULE$.canBuildFrom());
        final Seq lowercaseGPDBCols = (Seq)gpdbCols.map((Function1)new RowTransformer$$anonfun.RowTransformer$$anonfun$3(), Seq$.MODULE$.canBuildFrom());
        if (lowercaseSparkCols.equals((Object)lowercaseGPDBCols)) {
            this.log().debug("RowTransformer.getfunction returning identity function...");
            return (Try<Function1<Row, Row>>)new Success((Object)this.identityFunction());
        }
        if (!lowercaseSparkCols.toSet().equals((Object)lowercaseGPDBCols.toSet())) {
            final String missingColumns = this.formatColumnList((Seq<String>)lowercaseGPDBCols.diff((GenSeq)lowercaseSparkCols));
            final String extraColumns = this.formatColumnList((Seq<String>)lowercaseSparkCols.diff((GenSeq)lowercaseGPDBCols));
            if (!extraColumns.isEmpty()) {
                this.log().warn(new StringBuilder().append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Spark dataframe contains extra column[s] ", " " })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { extraColumns }))).append((Object)"that will be ignored when writing to Greenplum Database table.").toString());
            }
            if (!missingColumns.isEmpty()) {
                return (Try<Function1<Row, Row>>)new Failure((Throwable)new RuntimeException(new StringBuilder().append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Spark DataFrame must include column[s] ", " " })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { missingColumns }))).append((Object)"when writing to Greenplum Database table.").toString()));
            }
        }
        final Seq sparkColIndexFromGpdbColIndex = (Seq)lowercaseGPDBCols.map((Function1)new RowTransformer$$anonfun.RowTransformer$$anonfun$4(lowercaseSparkCols), Seq$.MODULE$.canBuildFrom());
        final Function1 reorderColumns = (Function1)new RowTransformer$$anonfun.RowTransformer$$anonfun$5(lowercaseGPDBCols, sparkColIndexFromGpdbColIndex);
        return (Try<Function1<Row, Row>>)new Success((Object)reorderColumns);
    }
    
    private String formatColumnList(final Seq<String> columns) {
        return ((TraversableOnce)columns.map((Function1)new RowTransformer$$anonfun$formatColumnList.RowTransformer$$anonfun$formatColumnList$1(), Seq$.MODULE$.canBuildFrom())).mkString(", ");
    }
    
    private RowTransformer$() {
        Logging$class.$init$(MODULE$ = this);
        this.identityFunction = (Function1<Row, Row>)new RowTransformer$$anonfun.RowTransformer$$anonfun$1();
    }
}
