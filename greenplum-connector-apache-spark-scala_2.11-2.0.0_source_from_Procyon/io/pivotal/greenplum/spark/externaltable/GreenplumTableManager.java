// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.reflect.ClassTag$;
import scala.Array$;
import scala.collection.immutable.Nil$;
import java.sql.ResultSet;
import io.pivotal.greenplum.spark.GreenplumCSVFormat$;
import scala.collection.immutable.StringOps;
import scala.collection.mutable.StringBuilder;
import io.pivotal.greenplum.spark.GpfdistLocation;
import scala.PartialFunction;
import io.pivotal.greenplum.spark.ErrorHandling$;
import scala.runtime.BoxedUnit;
import scala.Function1;
import scala.StringContext;
import scala.Predef$;
import scala.runtime.BoxesRunTime;
import scala.MatchError;
import scala.util.Success;
import scala.util.Failure;
import org.apache.spark.sql.SaveMode;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import scala.Option;
import org.apache.spark.sql.types.DataType;
import scala.util.Try;
import io.pivotal.greenplum.spark.conf.GreenplumOptions;
import org.apache.spark.sql.types.StructType;
import scala.collection.Seq;
import org.slf4j.Logger;
import io.pivotal.greenplum.spark.SqlExecutor;
import scala.reflect.ScalaSignature;
import io.pivotal.greenplum.spark.Logging;

@ScalaSignature(bytes = "\u0006\u0001\u0005}h\u0001B\u0001\u0003\u00015\u0011Qc\u0012:fK:\u0004H.^7UC\ndW-T1oC\u001e,'O\u0003\u0002\u0004\t\u0005iQ\r\u001f;fe:\fG\u000e^1cY\u0016T!!\u0002\u0004\u0002\u000bM\u0004\u0018M]6\u000b\u0005\u001dA\u0011!C4sK\u0016t\u0007\u000f\\;n\u0015\tI!\"A\u0004qSZ|G/\u00197\u000b\u0003-\t!![8\u0004\u0001M\u0019\u0001A\u0004\u000b\u0011\u0005=\u0011R\"\u0001\t\u000b\u0003E\tQa]2bY\u0006L!a\u0005\t\u0003\r\u0005s\u0017PU3g!\t)b#D\u0001\u0005\u0013\t9BAA\u0004M_\u001e<\u0017N\\4\t\u0011e\u0001!\u0011!Q\u0001\ni\t1b]9m\u000bb,7-\u001e;peB\u0011QcG\u0005\u00039\u0011\u00111bU9m\u000bb,7-\u001e;pe\")a\u0004\u0001C\u0001?\u00051A(\u001b8jiz\"\"\u0001\t\u0012\u0011\u0005\u0005\u0002Q\"\u0001\u0002\t\u000bei\u0002\u0019\u0001\u000e\t\u000b\u0011\u0002A\u0011A\u0013\u0002)A\u0014X\r]1sKR\u000b'\r\\3G_J<&/\u001b;f)\u00151s\u0006N\"L!\r9#\u0006L\u0007\u0002Q)\u0011\u0011\u0006E\u0001\u0005kRLG.\u0003\u0002,Q\t\u0019AK]=\u0011\u0005=i\u0013B\u0001\u0018\u0011\u0005\u001d\u0011un\u001c7fC:DQ\u0001M\u0012A\u0002E\nQ\u0001^1cY\u0016\u0004\"!\t\u001a\n\u0005M\u0012!AF$sK\u0016t\u0007\u000f\\;n#V\fG.\u001b4jK\u0012t\u0015-\\3\t\u000bU\u001a\u0003\u0019\u0001\u001c\u0002\u0017M\u0004\u0018M]6TG\",W.\u0019\t\u0003o\u0005k\u0011\u0001\u000f\u0006\u0003si\nQ\u0001^=qKNT!a\u000f\u001f\u0002\u0007M\fHN\u0003\u0002\u0006{)\u0011ahP\u0001\u0007CB\f7\r[3\u000b\u0003\u0001\u000b1a\u001c:h\u0013\t\u0011\u0005H\u0001\u0006TiJ,8\r\u001e+za\u0016DQ\u0001R\u0012A\u0002\u0015\u000bqa\u001c9uS>t7\u000f\u0005\u0002G\u00136\tqI\u0003\u0002I\t\u0005!1m\u001c8g\u0013\tQuI\u0001\tHe\u0016,g\u000e\u001d7v[>\u0003H/[8og\"9Aj\tI\u0001\u0002\u0004i\u0015\u0001B7pI\u0016\u0004\"AT(\u000e\u0003iJ!\u0001\u0015\u001e\u0003\u0011M\u000bg/Z'pI\u0016DaA\u0015\u0001\u0005\u0002\t\u0019\u0016AD8wKJ<(/\u001b;f)\u0006\u0014G.\u001a\u000b\u0005)bK&\fE\u0002(UU\u0003\"a\u0004,\n\u0005]\u0003\"AB!osZ\u000bG\u000eC\u00031#\u0002\u0007\u0011\u0007C\u00036#\u0002\u0007a\u0007C\u0003E#\u0002\u0007Q\tC\u0003]\u0001\u0011%Q,A\u0006de\u0016\fG/\u001a+bE2,G\u0003\u00020cG\u0012\u00042a\n\u0016`!\ty\u0001-\u0003\u0002b!\t!QK\\5u\u0011\u0015\u00014\f1\u00012\u0011\u0015)4\f1\u00017\u0011\u0015!5\f1\u0001F\u0011\u00151\u0007\u0001\"\u0001h\u0003\u0019\u001a'/Z1uKJ+\u0017\rZ1cY\u0016,\u0005\u0010^3s]\u0006dG+\u00192mK&3gj\u001c;Fq&\u001cHo\u001d\u000b\u0005=\"TG\u000eC\u0003jK\u0002\u0007\u0011'A\u0007j]R,'O\\1m)\u0006\u0014G.\u001a\u0005\u0006W\u0016\u0004\r!M\u0001\u000eKb$XM\u001d8bYR\u000b'\r\\3\t\u000b5,\u0007\u0019\u00018\u0002\u001f\u001d\u0004h\rZ5ti2{7-\u0019;j_:\u0004\"!F8\n\u0005A$!aD$qM\u0012L7\u000f\u001e'pG\u0006$\u0018n\u001c8\t\u000bI\u0004A\u0011B:\u00027\r\u0014X-\u0019;f%\u0016\fG-\u00192mK\u0016CH/\u001a:oC2$\u0016M\u00197f)\u0011qF/\u001e<\t\u000b%\f\b\u0019A\u0019\t\u000b-\f\b\u0019A\u0019\t\u000b5\f\b\u0019\u00018\t\u000ba\u0004A\u0011A=\u0002\u0013\r|\u0007/\u001f+bE2,G\u0003\u0002>\u007f\u0003\u0003\u00012a\n\u0016|!\tyA0\u0003\u0002~!\t\u0019\u0011J\u001c;\t\u000b}<\b\u0019A\u0019\u0002\rM|WO]2f\u0011\u0019\t\u0019a\u001ea\u0001c\u00051A/\u0019:hKRDq!a\u0002\u0001\t\u0013\tI!\u0001\u0010de\u0016\fG/\u001a*fC\u0012\f'\r\\3FqR,'O\\1m)\u0006\u0014G.Z*rYRA\u00111BA\r\u00037\ti\u0002\u0005\u0003\u0002\u000e\u0005MabA\b\u0002\u0010%\u0019\u0011\u0011\u0003\t\u0002\rA\u0013X\rZ3g\u0013\u0011\t)\"a\u0006\u0003\rM#(/\u001b8h\u0015\r\t\t\u0002\u0005\u0005\u0007S\u0006\u0015\u0001\u0019A\u0019\t\r-\f)\u00011\u00012\u0011\u0019i\u0017Q\u0001a\u0001]\"9\u0011\u0011\u0005\u0001\u0005\u0002\u0005\r\u0012AD4fi\u000e{G.^7o\u001d\u0006lWm\u001d\u000b\u0005\u0003K\ty\u0004\u0005\u0003(U\u0005\u001d\u0002CBA\u0015\u0003s\tYA\u0004\u0003\u0002,\u0005Ub\u0002BA\u0017\u0003gi!!a\f\u000b\u0007\u0005EB\"\u0001\u0004=e>|GOP\u0005\u0002#%\u0019\u0011q\u0007\t\u0002\u000fA\f7m[1hK&!\u00111HA\u001f\u0005\r\u0019V-\u001d\u0006\u0004\u0003o\u0001\u0002B\u0002\u0019\u0002 \u0001\u0007\u0011\u0007C\u0004\u0002D\u0001!\t!!\u0012\u0002\u0017Q\f'\r\\3Fq&\u001cHo\u001d\u000b\u0004M\u0005\u001d\u0003B\u0002\u0019\u0002B\u0001\u0007\u0011\u0007\u0003\u0005\u0002L\u0001!\tAAA'\u00035!(/\u001e8dCR,G+\u00192mKR\u0019a%a\u0014\t\rA\nI\u00051\u00012\u0011!\t\u0019\u0006\u0001C\u0001\u0005\u0005U\u0013!\u00033s_B$\u0016M\u00197f)\r1\u0013q\u000b\u0005\u0007a\u0005E\u0003\u0019A\u0019\t\u0013\u0005m\u0003!%A\u0005\u0002\u0005u\u0013A\b9sKB\f'/\u001a+bE2,gi\u001c:Xe&$X\r\n3fM\u0006,H\u000e\u001e\u00135+\t\tyFK\u0002N\u0003CZ#!a\u0019\u0011\t\u0005\u0015\u0014qN\u0007\u0003\u0003ORA!!\u001b\u0002l\u0005IQO\\2iK\u000e\\W\r\u001a\u0006\u0004\u0003[\u0002\u0012AC1o]>$\u0018\r^5p]&!\u0011\u0011OA4\u0005E)hn\u00195fG.,GMV1sS\u0006t7-Z\u0004\b\u0003k\u0012\u0001\u0012AA<\u0003U9%/Z3oa2,X\u000eV1cY\u0016l\u0015M\\1hKJ\u00042!IA=\r\u0019\t!\u0001#\u0001\u0002|M\u0019\u0011\u0011\u0010\b\t\u000fy\tI\b\"\u0001\u0002\u0000Q\u0011\u0011q\u000f\u0005\t\u0003\u0007\u000bI\b\"\u0001\u0002\u0006\u0006qq\r\u001d3c\u0007>dW/\u001c8UsB,G\u0003BAD\u0003\u001b\u0003RaDAE\u0003\u0017I1!a#\u0011\u0005\u0019y\u0005\u000f^5p]\"A\u0011qRAA\u0001\u0004\t\t*\u0001\u0002eiB\u0019q'a%\n\u0007\u0005U\u0005H\u0001\u0005ECR\fG+\u001f9f\u0011!\tI*!\u001f\u0005\u0002\u0005m\u0015!F2sK\u0006$X\rV1cY\u0016\u001cu\u000e\\;n]2K7\u000f\u001e\u000b\u0005\u0003;\u000by\n\u0005\u0003(U\u0005-\u0001BB\u001b\u0002\u0018\u0002\u0007a\u0007\u0003\u0005\u0002$\u0006eD\u0011AAS\u0003i9W\r^*vaB|'\u000f^3e'B\f'o\u001b#bi\u0006$\u0016\u0010]3t+\t\t9\u000b\u0005\u0004\u0002*\u0005e\u0012\u0011\u0013\u0005\t\u0003W\u000bI\b\"\u0001\u0002.\u0006!2M]3bi\u0016$\u0016M\u00197f'R\fG/Z7f]R$\u0002\"!(\u00020\u0006M\u0016q\u0017\u0005\b\u0003c\u000bI\u000b1\u00012\u0003%!\u0018M\u00197f\u001d\u0006lW\rC\u0004\u00026\u0006%\u0006\u0019\u0001\u001c\u0002\rM\u001c\u0007.Z7b\u0011\u0019!\u0015\u0011\u0016a\u0001\u000b\"A\u00111XA=\t\u0003\ti,A\u0010hK:,'/\u0019;f\u000bb$XM\u001d8bYR\u000b'\r\\3OC6,\u0007K]3gSb$b!a\u0003\u0002@\u0006\r\u0007\u0002CAa\u0003s\u0003\r!a\u0003\u0002\u001b\u0005\u0004\b\u000f\\5dCRLwN\\%e\u0011!\t\t,!/A\u0002\u0005-\u0001\"CAd\u0003s\"\tAAAe\u0003\u0015:WM\\3sCR,W\t\u001f;fe:\fG\u000eV1cY\u0016t\u0015-\\3D_2,XN\u001c)sK\u001aL\u0007\u0010\u0006\u0005\u0002\f\u0005-\u0017QZAh\u0011!\t\t-!2A\u0002\u0005-\u0001b\u0002\u0019\u0002F\u0002\u0007\u00111\u0002\u0005\t\u0003#\f)\r1\u0001\u0002(\u000591m\u001c7v[:\u001c\b\u0002CAk\u0003s\"\t!a6\u00023\u001d,g.\u001a:bi\u0016,\u0005\u0010^3s]\u0006dG+\u00192mK:\u000bW.\u001a\u000b\r\u0003\u0017\tI.a7\u0002^\u0006\u0005\u00181\u001e\u0005\t\u0003\u0003\f\u0019\u000e1\u0001\u0002\f!9\u0001'a5A\u0002\u0005-\u0001\u0002CAp\u0003'\u0004\r!a\u0003\u0002\u0015\u0015DXmY;u_JLE\r\u0003\u0005\u0002d\u0006M\u0007\u0019AAs\u0003!!\bN]3bI&#\u0007cA\b\u0002h&\u0019\u0011\u0011\u001e\t\u0003\t1{gn\u001a\u0005\u000b\u0003#\f\u0019\u000e%AA\u0002\u0005\u001d\u0002\u0002CAx\u0003s\"I!!=\u0002\u0019\u0019|G\u000eZ3e\u001b\u0012,\u0004*\u001a=\u0015\t\u0005-\u00111\u001f\u0005\t\u0003k\fi\u000f1\u0001\u0002\f\u0005\t1\u000f\u0003\u0006\u0002z\u0006e\u0014\u0013!C\u0001\u0003w\f1eZ3oKJ\fG/Z#yi\u0016\u0014h.\u00197UC\ndWMT1nK\u0012\"WMZ1vYR$S'\u0006\u0002\u0002~*\"\u0011qEA1\u0001")
public class GreenplumTableManager implements Logging
{
    public final SqlExecutor io$pivotal$greenplum$spark$externaltable$GreenplumTableManager$$sqlExecutor;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    public static Seq<String> generateExternalTableName$default$5() {
        return GreenplumTableManager$.MODULE$.generateExternalTableName$default$5();
    }
    
    public static String generateExternalTableName(final String applicationId, final String table, final String executorId, final long threadId, final Seq<String> columns) {
        return GreenplumTableManager$.MODULE$.generateExternalTableName(applicationId, table, executorId, threadId, columns);
    }
    
    public static String generateExternalTableNamePrefix(final String applicationId, final String tableName) {
        return GreenplumTableManager$.MODULE$.generateExternalTableNamePrefix(applicationId, tableName);
    }
    
    public static Try<String> createTableStatement(final GreenplumQualifiedName tableName, final StructType schema, final GreenplumOptions options) {
        return GreenplumTableManager$.MODULE$.createTableStatement(tableName, schema, options);
    }
    
    public static Seq<DataType> getSupportedSparkDataTypes() {
        return GreenplumTableManager$.MODULE$.getSupportedSparkDataTypes();
    }
    
    public static Try<String> createTableColumnList(final StructType sparkSchema) {
        return GreenplumTableManager$.MODULE$.createTableColumnList(sparkSchema);
    }
    
    public static Option<String> gpdbColumnType(final DataType dt) {
        return GreenplumTableManager$.MODULE$.gpdbColumnType(dt);
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
    
    public Try<Object> prepareTableForWrite(final GreenplumQualifiedName table, final StructType sparkSchema, final GreenplumOptions options, final SaveMode mode) {
        final Try<Object> tableExists = this.tableExists(table);
        Object o;
        if (tableExists instanceof Failure) {
            final Throwable exception = ((Failure)tableExists).exception();
            o = new Failure(exception);
        }
        else {
            if (!(tableExists instanceof Success)) {
                throw new MatchError((Object)tableExists);
            }
            final boolean exists = BoxesRunTime.unboxToBoolean(((Success)tableExists).value());
            Object o2 = null;
            Label_0246: {
                if (exists) {
                    final SaveMode errorIfExists = SaveMode.ErrorIfExists;
                    Label_0165: {
                        if (mode == null) {
                            if (errorIfExists != null) {
                                break Label_0165;
                            }
                        }
                        else if (!mode.equals(errorIfExists)) {
                            break Label_0165;
                        }
                        final RuntimeException ex;
                        o2 = new Failure((Throwable)ex);
                        ex = new RuntimeException(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Table ", " exists, and SaveMode.ErrorIfExists was specified" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { table })));
                        break Label_0246;
                    }
                    final SaveMode overwrite = SaveMode.Overwrite;
                    Label_0214: {
                        if (mode == null) {
                            if (overwrite != null) {
                                break Label_0214;
                            }
                        }
                        else if (!mode.equals(overwrite)) {
                            break Label_0214;
                        }
                        o2 = this.overwriteTable(table, sparkSchema, options).map((Function1)new GreenplumTableManager$$anonfun$prepareTableForWrite.GreenplumTableManager$$anonfun$prepareTableForWrite$1(this));
                        break Label_0246;
                    }
                    o2 = new Success((Object)BoxesRunTime.boxToBoolean(false));
                }
                else {
                    o2 = this.io$pivotal$greenplum$spark$externaltable$GreenplumTableManager$$createTable(table, sparkSchema, options).map((Function1)new GreenplumTableManager$$anonfun$prepareTableForWrite.GreenplumTableManager$$anonfun$prepareTableForWrite$2(this));
                }
            }
            o = o2;
        }
        return (Try<Object>)o;
    }
    
    public SaveMode prepareTableForWrite$default$4() {
        return SaveMode.ErrorIfExists;
    }
    
    public Try<Object> overwriteTable(final GreenplumQualifiedName table, final StructType sparkSchema, final GreenplumOptions options) {
        return (Try<Object>)(options.truncateTable() ? this.truncateTable(table) : this.dropTable(table).flatMap((Function1)new GreenplumTableManager$$anonfun$overwriteTable.GreenplumTableManager$$anonfun$overwriteTable$1(this, table, sparkSchema, options)));
    }
    
    public Try<BoxedUnit> io$pivotal$greenplum$spark$externaltable$GreenplumTableManager$$createTable(final GreenplumQualifiedName table, final StructType sparkSchema, final GreenplumOptions options) {
        return (Try<BoxedUnit>)GreenplumTableManager$.MODULE$.createTableStatement(table, sparkSchema, options).flatMap((Function1)new GreenplumTableManager$$anonfun$io$pivotal$greenplum$spark$externaltable$GreenplumTableManager$$createTable.GreenplumTableManager$$anonfun$io$pivotal$greenplum$spark$externaltable$GreenplumTableManager$$createTable$1(this, table)).recoverWith((PartialFunction)ErrorHandling$.MODULE$.wrapErrorMessage(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Exception while creating table ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { table }))));
    }
    
    public Try<BoxedUnit> createReadableExternalTableIfNotExists(final GreenplumQualifiedName internalTable, final GreenplumQualifiedName externalTable, final GpfdistLocation gpfdistLocation) {
        boolean b = false;
        Success success = null;
        final Try<Object> tableExists = this.tableExists(externalTable);
        if (tableExists instanceof Success) {
            b = true;
            success = (Success)tableExists;
            if (BoxesRunTime.unboxToBoolean(success.value())) {
                final Object readableExternalTable = new Success((Object)BoxedUnit.UNIT);
                return (Try<BoxedUnit>)readableExternalTable;
            }
        }
        Object readableExternalTable;
        if (b && !BoxesRunTime.unboxToBoolean(success.value())) {
            readableExternalTable = this.createReadableExternalTable(internalTable, externalTable, gpfdistLocation);
        }
        else {
            if (!(tableExists instanceof Failure)) {
                throw new MatchError((Object)tableExists);
            }
            final Throwable exception = ((Failure)tableExists).exception();
            readableExternalTable = new Failure(exception);
        }
        return (Try<BoxedUnit>)readableExternalTable;
    }
    
    private Try<BoxedUnit> createReadableExternalTable(final GreenplumQualifiedName internalTable, final GreenplumQualifiedName externalTable, final GpfdistLocation gpfdistLocation) {
        final String sql = this.createReadableExternalTableSql(internalTable, externalTable, gpfdistLocation);
        final Try<Object> execute = this.io$pivotal$greenplum$spark$externaltable$GreenplumTableManager$$sqlExecutor.execute(sql);
        Object o;
        if (execute instanceof Success) {
            if (this.log().isDebugEnabled()) {
                this.log().debug(new StringBuilder().append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "External table ", " not found, " })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { externalTable }))).append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "created table with port=", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { BoxesRunTime.boxToInteger(gpfdistLocation.port()) }))).toString());
            }
            o = new Success((Object)BoxedUnit.UNIT);
        }
        else {
            if (!(execute instanceof Failure)) {
                throw new MatchError((Object)execute);
            }
            final Throwable exception = ((Failure)execute).exception();
            this.log().error(new StringBuilder().append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Exception while creating external table ", " " })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { externalTable }))).append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "with port=", ": ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { BoxesRunTime.boxToInteger(gpfdistLocation.port()), exception }))).toString());
            o = new Failure(exception);
        }
        return (Try<BoxedUnit>)o;
    }
    
    public Try<Object> copyTable(final GreenplumQualifiedName source, final GreenplumQualifiedName target) {
        final String sqlQuery = new StringOps(Predef$.MODULE$.augmentString(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "INSERT INTO ", "\n         |SELECT *\n         |FROM ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { target, source })))).stripMargin();
        return this.io$pivotal$greenplum$spark$externaltable$GreenplumTableManager$$sqlExecutor.executeUpdate(sqlQuery);
    }
    
    private String createReadableExternalTableSql(final GreenplumQualifiedName internalTable, final GreenplumQualifiedName externalTable, final GpfdistLocation gpfdistLocation) {
        final String url = gpfdistLocation.generate(externalTable.name());
        final String sqlQuery = new StringOps(Predef$.MODULE$.augmentString(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "CREATE READABLE EXTERNAL TABLE\n         |", " (LIKE ", ")\n         |LOCATION ('", "')\n         |FORMAT 'CSV'\n         |(DELIMITER AS '", "'\n         | NULL AS '", "')\n         |ENCODING '", "'" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { externalTable, internalTable, url, BoxesRunTime.boxToCharacter(GreenplumCSVFormat$.MODULE$.CHAR_DELIMITER()), GreenplumCSVFormat$.MODULE$.VALUE_OF_NULL(), GreenplumCSVFormat$.MODULE$.DEFAULT_ENCODING() })))).stripMargin();
        if (this.log().isDebugEnabled()) {
            this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Create readable external table query: ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { sqlQuery })));
        }
        return sqlQuery;
    }
    
    public Try<Seq<String>> getColumnNames(final GreenplumQualifiedName table) {
        return this.io$pivotal$greenplum$spark$externaltable$GreenplumTableManager$$sqlExecutor.executeQuery(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "SELECT * FROM ", " LIMIT 0" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { table })), (scala.Function1<ResultSet, Seq<String>>)new GreenplumTableManager$$anonfun$getColumnNames.GreenplumTableManager$$anonfun$getColumnNames$1(this));
    }
    
    public Try<Object> tableExists(final GreenplumQualifiedName table) {
        final String sqlQuery = new StringOps(Predef$.MODULE$.augmentString(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "SELECT EXISTS (\n         |SELECT 1\n         |FROM information_schema.tables\n         |WHERE table_schema = ?\n         |AND table_name = ?);" })).s((Seq)Nil$.MODULE$))).stripMargin();
        return (Try<Object>)this.io$pivotal$greenplum$spark$externaltable$GreenplumTableManager$$sqlExecutor.executeQuery(sqlQuery, (Object[])Array$.MODULE$.apply((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { table.schema(), table.name() }), ClassTag$.MODULE$.Any()), (scala.Function1<ResultSet, Object>)new GreenplumTableManager$$anonfun$tableExists.GreenplumTableManager$$anonfun$tableExists$1(this)).recoverWith((PartialFunction)ErrorHandling$.MODULE$.wrapErrorMessage(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Cannot determine if table ", " exists" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { table }))));
    }
    
    public Try<Object> truncateTable(final GreenplumQualifiedName table) {
        final String sqlQuery = new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "TRUNCATE TABLE ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { table }));
        return (Try<Object>)this.io$pivotal$greenplum$spark$externaltable$GreenplumTableManager$$sqlExecutor.execute(sqlQuery).recoverWith((PartialFunction)ErrorHandling$.MODULE$.wrapErrorMessage(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Exception while truncating table ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { table }))));
    }
    
    public Try<Object> dropTable(final GreenplumQualifiedName table) {
        final String sqlQuery = new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "DROP TABLE ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { table }));
        return (Try<Object>)this.io$pivotal$greenplum$spark$externaltable$GreenplumTableManager$$sqlExecutor.execute(sqlQuery).recoverWith((PartialFunction)ErrorHandling$.MODULE$.wrapErrorMessage(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Exception while dropping table ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { table }))));
    }
    
    public GreenplumTableManager(final SqlExecutor sqlExecutor) {
        this.io$pivotal$greenplum$spark$externaltable$GreenplumTableManager$$sqlExecutor = sqlExecutor;
        Logging$class.$init$(this);
    }
}
