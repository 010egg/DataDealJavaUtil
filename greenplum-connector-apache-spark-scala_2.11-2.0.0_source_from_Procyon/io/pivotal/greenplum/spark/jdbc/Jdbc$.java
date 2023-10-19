// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.jdbc;

import scala.collection.immutable.Vector$;
import scala.collection.immutable.Vector;
import org.apache.spark.sql.types.AtomicType;
import java.sql.JDBCType;
import org.apache.spark.sql.types.BinaryType$;
import org.apache.spark.sql.types.BooleanType$;
import org.apache.spark.sql.types.StringType$;
import org.apache.spark.sql.types.DateType$;
import org.apache.spark.sql.types.ShortType$;
import org.apache.spark.sql.types.DecimalType$;
import org.apache.spark.sql.types.DecimalType;
import org.apache.spark.sql.types.DoubleType$;
import org.apache.spark.sql.types.FloatType$;
import org.apache.spark.sql.types.IntegerType$;
import org.apache.spark.sql.types.LongType$;
import org.apache.spark.sql.types.TimestampType$;
import java.sql.SQLException;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import scala.collection.immutable.IndexedSeq$;
import scala.runtime.RichInt$;
import java.sql.ResultSetMetaData;
import org.apache.spark.sql.types.StructType;
import scala.util.Either;
import scala.collection.mutable.StringBuilder;
import scala.runtime.BoxedUnit;
import io.pivotal.greenplum.spark.GreenplumCSVFormat$;
import io.pivotal.greenplum.spark.GpfdistLocation;
import scala.reflect.ClassTag;
import java.sql.ResultSet;
import scala.reflect.ClassTag$;
import scala.runtime.BoxesRunTime;
import resource.ManagedResource;
import resource.ExtractableManagedResource;
import scala.reflect.OptManifest;
import resource.Resource;
import java.sql.Statement;
import scala.reflect.ClassManifestFactory$;
import resource.Resource$;
import resource.package$;
import scala.collection.immutable.StringOps;
import scala.StringContext;
import scala.Predef$;
import scala.Function1;
import scala.collection.Seq$;
import scala.collection.TraversableOnce;
import scala.collection.Seq;
import io.pivotal.greenplum.spark.externaltable.GreenplumQualifiedName;
import java.sql.Connection;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import org.slf4j.Logger;
import io.pivotal.greenplum.spark.Logging;

public final class Jdbc$ implements Logging
{
    public static final Jdbc$ MODULE$;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    static {
        new Jdbc$();
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
    
    public void copyTable(final Connection conn, final GreenplumQualifiedName srcTable, final GreenplumQualifiedName dstTable, final String predicate, final Seq<String> columns) {
        final String colNames = columns.isEmpty() ? "1" : ((TraversableOnce)columns.map((Function1)new Jdbc$$anonfun.Jdbc$$anonfun$1(), Seq$.MODULE$.canBuildFrom())).mkString(",");
        final String sqlQuery = new StringOps(Predef$.MODULE$.augmentString(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "\n         |INSERT INTO ", "\n         |SELECT ", "\n         |FROM ", "\n         |WHERE ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { dstTable, colNames, srcTable, predicate })))).stripMargin();
        final ExtractableManagedResource result = package$.MODULE$.managed((scala.Function0<Object>)new Jdbc$$anonfun.Jdbc$$anonfun$2(conn), Resource$.MODULE$.statementResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)Statement.class)).map((scala.Function1<Object, Object>)new Jdbc$$anonfun.Jdbc$$anonfun$3(sqlQuery));
        result.tried().get();
    }
    
    public boolean externalTableExists(final Connection conn, final GreenplumQualifiedName table) {
        final String sqlQuery = new StringOps(Predef$.MODULE$.augmentString(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "SELECT EXISTS (\n         |SELECT 1\n         |FROM information_schema.tables\n         |WHERE table_schema = '", "'\n         |AND table_name = '", "' );" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { table.schema(), table.name() })))).stripMargin();
        final ManagedResource result = package$.MODULE$.managed((scala.Function0<Object>)new Jdbc$$anonfun.Jdbc$$anonfun$4(conn), Resource$.MODULE$.statementResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)Statement.class)).flatMap((scala.Function1<Object, ManagedResource<Object>>)new Jdbc$$anonfun.Jdbc$$anonfun$5(sqlQuery));
        return BoxesRunTime.unboxToBoolean(result.map((Function1)new Jdbc$$anonfun$externalTableExists.Jdbc$$anonfun$externalTableExists$1()).opt().get());
    }
    
    public Seq<String> getColumnsMetadata(final Connection conn, final GreenplumQualifiedName table, final Seq<String> columnNames) {
        final String schema = table.schema();
        final String srcTable = table.name();
        final String columnNamesJoined = columnNames.mkString("','");
        final String sqlQuery = new StringOps(Predef$.MODULE$.augmentString(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "\n         |SELECT '\"' || a.attname || '\" ' ||\n         |pg_catalog.format_type(a.atttypid, a.atttypmod) as column_metadata\n         |FROM pg_catalog.pg_attribute a, pg_class b, pg_namespace n\n         |WHERE n.nspname = '", "'\n         |AND n.oid = b.relnamespace\n         |AND a.attrelid = b.oid\n         |AND b.relname = '", "'\n         |AND a.attnum > 0 AND NOT a.attisdropped\n         |AND a.attname IN ('", "')\n         |ORDER BY strpos(\n         |E'\\\\'", "\\\\'',\n         |E'\\\\'' || a.attname || E'\\\\'')\n       " })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { schema, srcTable, columnNamesJoined, columnNames.mkString("\\',\\'") })))).stripMargin();
        final Seq results = this.retrieveResults(conn, sqlQuery, (scala.Function1<ResultSet, Object>)new Jdbc$$anonfun.Jdbc$$anonfun$6(), (scala.reflect.ClassTag<Object>)ClassTag$.MODULE$.apply((Class)String.class));
        if (results.size() != columnNames.size()) {
            throw new RuntimeException(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Error retrieving metadata for ", " for ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { columnNames.mkString(","), srcTable })));
        }
        return (Seq<String>)results;
    }
    
    public void createGpfdistWritableExternalTable(final Connection conn, final GreenplumQualifiedName srcTable, final GreenplumQualifiedName extTable, final GpfdistLocation gpfdistLocation, final Seq<String> columns) {
        final String url = gpfdistLocation.generate(extTable.name());
        final String projectedColumnsMetadata = columns.isEmpty() ? "dummy int" : this.getColumnsMetadata(conn, srcTable, columns).mkString(",");
        final String sqlQuery = new StringOps(Predef$.MODULE$.augmentString(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "CREATE WRITABLE EXTERNAL TABLE ", " (", ")\n         |LOCATION ('", "')\n         |FORMAT 'CSV'\n         |(DELIMITER '", "'\n         | NULL AS '", "')\n         |ENCODING '", "'\n         |DISTRIBUTED RANDOMLY" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { extTable, projectedColumnsMetadata, url, BoxesRunTime.boxToCharacter(GreenplumCSVFormat$.MODULE$.CHAR_DELIMITER()), GreenplumCSVFormat$.MODULE$.VALUE_OF_NULL(), GreenplumCSVFormat$.MODULE$.DEFAULT_ENCODING() })))).stripMargin();
        this.log().debug(sqlQuery);
        package$.MODULE$.managed((scala.Function0<Object>)new Jdbc$$anonfun$createGpfdistWritableExternalTable.Jdbc$$anonfun$createGpfdistWritableExternalTable$1(conn), Resource$.MODULE$.statementResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)Statement.class)).foreach((scala.Function1<Object, BoxedUnit>)new Jdbc$$anonfun$createGpfdistWritableExternalTable.Jdbc$$anonfun$createGpfdistWritableExternalTable$2(sqlQuery));
    }
    
    public void dropExternalTables(final Connection conn, final String schema, final String externalTableNamePrefix) {
        if (this.log().isDebugEnabled()) {
            this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Dropping external tables with prefix '", "'" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { externalTableNamePrefix })));
        }
        final String select = new StringBuilder().append((Object)"SELECT table_name FROM information_schema.tables ").append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "WHERE table_schema = '", "' AND table_name LIKE '", "%'" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { schema, externalTableNamePrefix }))).toString();
        package$.MODULE$.managed((scala.Function0<Object>)new Jdbc$$anonfun$dropExternalTables.Jdbc$$anonfun$dropExternalTables$1(conn), Resource$.MODULE$.statementResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)Statement.class)).foreach((scala.Function1<Object, BoxedUnit>)new Jdbc$$anonfun$dropExternalTables.Jdbc$$anonfun$dropExternalTables$2(schema, select));
    }
    
    public int[] retrieveSegmentIds(final Connection conn) {
        final String sql = "SELECT content FROM gp_segment_configuration WHERE content != -1 ORDER BY content";
        return (int[])this.retrieveResults(conn, sql, (scala.Function1<ResultSet, Object>)new Jdbc$$anonfun$retrieveSegmentIds.Jdbc$$anonfun$retrieveSegmentIds$1(), (scala.reflect.ClassTag<Object>)ClassTag$.MODULE$.Int()).toArray(ClassTag$.MODULE$.Int());
    }
    
    public <T> Seq<T> retrieveResults(final Connection conn, final String sqlQuery, final Function1<ResultSet, T> getter, final ClassTag<T> evidence$1) {
        final ManagedResource result = package$.MODULE$.managed((scala.Function0<Object>)new Jdbc$$anonfun.Jdbc$$anonfun$8(conn), Resource$.MODULE$.statementResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)Statement.class)).flatMap((scala.Function1<Object, ManagedResource<Object>>)new Jdbc$$anonfun.Jdbc$$anonfun$9(sqlQuery, (Function1)getter, (ClassTag)evidence$1));
        return (Seq<T>)result.map((Function1)new Jdbc$$anonfun$retrieveResults.Jdbc$$anonfun$retrieveResults$1()).opt().get();
    }
    
    public ColumnValueRange computeColumnValueRange(final Connection conn, final GreenplumQualifiedName table, final String columnName) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   io/pivotal/greenplum/spark/externaltable/GreenplumQualifiedName.name:()Ljava/lang/String;
        //     4: astore          tableName
        //     6: new             Lscala/collection/immutable/StringOps;
        //     9: dup            
        //    10: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //    13: new             Lscala/StringContext;
        //    16: dup            
        //    17: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //    20: iconst_3       
        //    21: anewarray       Ljava/lang/String;
        //    24: dup            
        //    25: iconst_0       
        //    26: ldc_w           "SELECT CAST(UNNEST(string_to_array(array_to_string\n       |             (most_common_vals || histogram_bounds, ','), ',')) AS\n       |              FLOAT) AS combined_histogram\n       |      FROM pg_stats ps\n       |      WHERE tablename = '"
        //    29: aastore        
        //    30: dup            
        //    31: iconst_1       
        //    32: ldc_w           "'\n       |      AND ps.attname = '"
        //    35: aastore        
        //    36: dup            
        //    37: iconst_2       
        //    38: ldc_w           "'\n       |      AND histogram_bounds IS NOT NULL\n       "
        //    41: aastore        
        //    42: checkcast       [Ljava/lang/Object;
        //    45: invokevirtual   scala/Predef$.wrapRefArray:([Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //    48: invokespecial   scala/StringContext.<init>:(Lscala/collection/Seq;)V
        //    51: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //    54: iconst_2       
        //    55: anewarray       Ljava/lang/Object;
        //    58: dup            
        //    59: iconst_0       
        //    60: aload           tableName
        //    62: aastore        
        //    63: dup            
        //    64: iconst_1       
        //    65: aload_3         /* columnName */
        //    66: aastore        
        //    67: invokevirtual   scala/Predef$.genericWrapArray:(Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //    70: invokevirtual   scala/StringContext.s:(Lscala/collection/Seq;)Ljava/lang/String;
        //    73: invokevirtual   scala/Predef$.augmentString:(Ljava/lang/String;)Ljava/lang/String;
        //    76: invokespecial   scala/collection/immutable/StringOps.<init>:(Ljava/lang/String;)V
        //    79: invokevirtual   scala/collection/immutable/StringOps.stripMargin:()Ljava/lang/String;
        //    82: astore          sqlStatSubQuery
        //    84: new             Lscala/collection/immutable/StringOps;
        //    87: dup            
        //    88: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //    91: new             Lscala/StringContext;
        //    94: dup            
        //    95: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //    98: iconst_2       
        //    99: anewarray       Ljava/lang/String;
        //   102: dup            
        //   103: iconst_0       
        //   104: ldc_w           "\n         |SELECT\n         |  MIN(combined_histogram) approx_min,\n         |  MAX(combined_histogram) approx_max\n         |FROM ("
        //   107: aastore        
        //   108: dup            
        //   109: iconst_1       
        //   110: ldc_w           ") t\n         |      HAVING MIN(combined_histogram) IS NOT NULL\n         |      AND MAX(combined_histogram) IS NOT NULL"
        //   113: aastore        
        //   114: checkcast       [Ljava/lang/Object;
        //   117: invokevirtual   scala/Predef$.wrapRefArray:([Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //   120: invokespecial   scala/StringContext.<init>:(Lscala/collection/Seq;)V
        //   123: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   126: iconst_1       
        //   127: anewarray       Ljava/lang/Object;
        //   130: dup            
        //   131: iconst_0       
        //   132: aload           sqlStatSubQuery
        //   134: aastore        
        //   135: invokevirtual   scala/Predef$.genericWrapArray:(Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //   138: invokevirtual   scala/StringContext.s:(Lscala/collection/Seq;)Ljava/lang/String;
        //   141: invokevirtual   scala/Predef$.augmentString:(Ljava/lang/String;)Ljava/lang/String;
        //   144: invokespecial   scala/collection/immutable/StringOps.<init>:(Ljava/lang/String;)V
        //   147: invokevirtual   scala/collection/immutable/StringOps.stripMargin:()Ljava/lang/String;
        //   150: astore          sqlStatQuery
        //   152: aload_0         /* this */
        //   153: aload_1         /* conn */
        //   154: aload           sqlStatQuery
        //   156: invokespecial   io/pivotal/greenplum/spark/jdbc/Jdbc$.queryColumnValueRange:(Ljava/sql/Connection;Ljava/lang/String;)Lscala/util/Either;
        //   159: astore          7
        //   161: aload           7
        //   163: instanceof      Lscala/util/Right;
        //   166: ifeq            262
        //   169: aload           7
        //   171: checkcast       Lscala/util/Right;
        //   174: astore          8
        //   176: aload           8
        //   178: invokevirtual   scala/util/Right.b:()Ljava/lang/Object;
        //   181: checkcast       Lio/pivotal/greenplum/spark/jdbc/ColumnValueRange;
        //   184: astore          statsRange
        //   186: aload_0         /* this */
        //   187: invokevirtual   io/pivotal/greenplum/spark/jdbc/Jdbc$.log:()Lorg/slf4j/Logger;
        //   190: new             Lscala/StringContext;
        //   193: dup            
        //   194: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   197: iconst_3       
        //   198: anewarray       Ljava/lang/String;
        //   201: dup            
        //   202: iconst_0       
        //   203: ldc_w           "Using approximate statistics data for column "
        //   206: aastore        
        //   207: dup            
        //   208: iconst_1       
        //   209: ldc_w           "."
        //   212: aastore        
        //   213: dup            
        //   214: iconst_2       
        //   215: ldc_w           " range"
        //   218: aastore        
        //   219: checkcast       [Ljava/lang/Object;
        //   222: invokevirtual   scala/Predef$.wrapRefArray:([Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //   225: invokespecial   scala/StringContext.<init>:(Lscala/collection/Seq;)V
        //   228: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   231: iconst_2       
        //   232: anewarray       Ljava/lang/Object;
        //   235: dup            
        //   236: iconst_0       
        //   237: aload           tableName
        //   239: aastore        
        //   240: dup            
        //   241: iconst_1       
        //   242: aload_3         /* columnName */
        //   243: aastore        
        //   244: invokevirtual   scala/Predef$.genericWrapArray:(Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //   247: invokevirtual   scala/StringContext.s:(Lscala/collection/Seq;)Ljava/lang/String;
        //   250: invokeinterface org/slf4j/Logger.info:(Ljava/lang/String;)V
        //   255: aload           statsRange
        //   257: astore          10
        //   259: goto            468
        //   262: aload           7
        //   264: instanceof      Lscala/util/Left;
        //   267: ifeq            584
        //   270: aload_0         /* this */
        //   271: invokevirtual   io/pivotal/greenplum/spark/jdbc/Jdbc$.log:()Lorg/slf4j/Logger;
        //   274: new             Lscala/StringContext;
        //   277: dup            
        //   278: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   281: iconst_3       
        //   282: anewarray       Ljava/lang/String;
        //   285: dup            
        //   286: iconst_0       
        //   287: ldc_w           "Using accurate aggregated data for column "
        //   290: aastore        
        //   291: dup            
        //   292: iconst_1       
        //   293: ldc_w           "."
        //   296: aastore        
        //   297: dup            
        //   298: iconst_2       
        //   299: ldc_w           " range"
        //   302: aastore        
        //   303: checkcast       [Ljava/lang/Object;
        //   306: invokevirtual   scala/Predef$.wrapRefArray:([Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //   309: invokespecial   scala/StringContext.<init>:(Lscala/collection/Seq;)V
        //   312: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   315: iconst_2       
        //   316: anewarray       Ljava/lang/Object;
        //   319: dup            
        //   320: iconst_0       
        //   321: aload           tableName
        //   323: aastore        
        //   324: dup            
        //   325: iconst_1       
        //   326: aload_3         /* columnName */
        //   327: aastore        
        //   328: invokevirtual   scala/Predef$.genericWrapArray:(Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //   331: invokevirtual   scala/StringContext.s:(Lscala/collection/Seq;)Ljava/lang/String;
        //   334: invokeinterface org/slf4j/Logger.info:(Ljava/lang/String;)V
        //   339: new             Lscala/collection/immutable/StringOps;
        //   342: dup            
        //   343: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   346: new             Lscala/StringContext;
        //   349: dup            
        //   350: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   353: iconst_4       
        //   354: anewarray       Ljava/lang/String;
        //   357: dup            
        //   358: iconst_0       
        //   359: ldc_w           "SELECT MIN(\""
        //   362: aastore        
        //   363: dup            
        //   364: iconst_1       
        //   365: ldc_w           "\"), MAX(\""
        //   368: aastore        
        //   369: dup            
        //   370: iconst_2       
        //   371: ldc_w           "\")\n             | FROM "
        //   374: aastore        
        //   375: dup            
        //   376: iconst_3       
        //   377: ldc_w           ";"
        //   380: aastore        
        //   381: checkcast       [Ljava/lang/Object;
        //   384: invokevirtual   scala/Predef$.wrapRefArray:([Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //   387: invokespecial   scala/StringContext.<init>:(Lscala/collection/Seq;)V
        //   390: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   393: iconst_3       
        //   394: anewarray       Ljava/lang/Object;
        //   397: dup            
        //   398: iconst_0       
        //   399: aload_3         /* columnName */
        //   400: aastore        
        //   401: dup            
        //   402: iconst_1       
        //   403: aload_3         /* columnName */
        //   404: aastore        
        //   405: dup            
        //   406: iconst_2       
        //   407: aload_2         /* table */
        //   408: aastore        
        //   409: invokevirtual   scala/Predef$.genericWrapArray:(Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //   412: invokevirtual   scala/StringContext.s:(Lscala/collection/Seq;)Ljava/lang/String;
        //   415: invokevirtual   scala/Predef$.augmentString:(Ljava/lang/String;)Ljava/lang/String;
        //   418: invokespecial   scala/collection/immutable/StringOps.<init>:(Ljava/lang/String;)V
        //   421: invokevirtual   scala/collection/immutable/StringOps.stripMargin:()Ljava/lang/String;
        //   424: astore          sqlAggQuery
        //   426: aload_0         /* this */
        //   427: aload_1         /* conn */
        //   428: aload           sqlAggQuery
        //   430: invokespecial   io/pivotal/greenplum/spark/jdbc/Jdbc$.queryColumnValueRange:(Ljava/sql/Connection;Ljava/lang/String;)Lscala/util/Either;
        //   433: astore          12
        //   435: aload           12
        //   437: instanceof      Lscala/util/Right;
        //   440: ifeq            471
        //   443: aload           12
        //   445: checkcast       Lscala/util/Right;
        //   448: astore          13
        //   450: aload           13
        //   452: invokevirtual   scala/util/Right.b:()Ljava/lang/Object;
        //   455: checkcast       Lio/pivotal/greenplum/spark/jdbc/ColumnValueRange;
        //   458: astore          aggRange
        //   460: aload           aggRange
        //   462: astore          15
        //   464: aload           15
        //   466: astore          10
        //   468: aload           10
        //   470: areturn        
        //   471: aload           12
        //   473: instanceof      Lscala/util/Left;
        //   476: ifeq            574
        //   479: aload           12
        //   481: checkcast       Lscala/util/Left;
        //   484: astore          16
        //   486: aload           16
        //   488: invokevirtual   scala/util/Left.a:()Ljava/lang/Object;
        //   491: checkcast       Ljava/lang/String;
        //   494: astore          error
        //   496: aload_0         /* this */
        //   497: invokevirtual   io/pivotal/greenplum/spark/jdbc/Jdbc$.log:()Lorg/slf4j/Logger;
        //   500: aload           error
        //   502: invokeinterface org/slf4j/Logger.error:(Ljava/lang/String;)V
        //   507: new             Ljava/lang/IllegalArgumentException;
        //   510: dup            
        //   511: new             Lscala/StringContext;
        //   514: dup            
        //   515: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   518: iconst_3       
        //   519: anewarray       Ljava/lang/String;
        //   522: dup            
        //   523: iconst_0       
        //   524: ldc_w           "Unable to determine range for a column "
        //   527: aastore        
        //   528: dup            
        //   529: iconst_1       
        //   530: ldc_w           "."
        //   533: aastore        
        //   534: dup            
        //   535: iconst_2       
        //   536: ldc             ""
        //   538: aastore        
        //   539: checkcast       [Ljava/lang/Object;
        //   542: invokevirtual   scala/Predef$.wrapRefArray:([Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //   545: invokespecial   scala/StringContext.<init>:(Lscala/collection/Seq;)V
        //   548: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   551: iconst_2       
        //   552: anewarray       Ljava/lang/Object;
        //   555: dup            
        //   556: iconst_0       
        //   557: aload           4
        //   559: aastore        
        //   560: dup            
        //   561: iconst_1       
        //   562: aload_3         /* columnName */
        //   563: aastore        
        //   564: invokevirtual   scala/Predef$.genericWrapArray:(Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //   567: invokevirtual   scala/StringContext.s:(Lscala/collection/Seq;)Ljava/lang/String;
        //   570: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   573: athrow         
        //   574: new             Lscala/MatchError;
        //   577: dup            
        //   578: aload           12
        //   580: invokespecial   scala/MatchError.<init>:(Ljava/lang/Object;)V
        //   583: athrow         
        //   584: new             Lscala/MatchError;
        //   587: dup            
        //   588: aload           7
        //   590: invokespecial   scala/MatchError.<init>:(Ljava/lang/Object;)V
        //   593: athrow         
        //    StackMapTable: 00 05 FF 01 06 00 08 07 00 02 07 01 3D 07 00 F6 07 00 7F 07 00 7F 07 00 7F 07 00 7F 07 01 DA 00 00 FE 00 CD 00 00 07 01 CF FF 00 02 00 0D 07 00 02 07 01 3D 07 00 F6 07 00 7F 07 00 7F 07 00 7F 07 00 7F 07 01 DA 00 00 00 07 00 7F 07 01 DA 00 00 FB 00 66 FF 00 09 00 08 07 00 02 07 01 3D 07 00 F6 07 00 7F 07 00 7F 07 00 7F 07 00 7F 07 01 DA 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private Either<String, ColumnValueRange> queryColumnValueRange(final Connection conn, final String sqlQuery) {
        final ManagedResource result = package$.MODULE$.managed((scala.Function0<Object>)new Jdbc$$anonfun.Jdbc$$anonfun$10(conn), Resource$.MODULE$.statementResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)Statement.class)).flatMap((scala.Function1<Object, ManagedResource<Object>>)new Jdbc$$anonfun.Jdbc$$anonfun$11(sqlQuery));
        return (Either<String, ColumnValueRange>)result.map((Function1)new Jdbc$$anonfun$queryColumnValueRange.Jdbc$$anonfun$queryColumnValueRange$1()).opt().getOrElse((Function0)new Jdbc$$anonfun$queryColumnValueRange.Jdbc$$anonfun$queryColumnValueRange$2(sqlQuery));
    }
    
    public StructType resolveTable(final Connection conn, final String url, final GreenplumQualifiedName table) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //     7: new             Lscala/StringContext;
        //    10: dup            
        //    11: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //    14: iconst_2       
        //    15: anewarray       Ljava/lang/String;
        //    18: dup            
        //    19: iconst_0       
        //    20: ldc_w           "SELECT * FROM "
        //    23: aastore        
        //    24: dup            
        //    25: iconst_1       
        //    26: ldc_w           " LIMIT 0"
        //    29: aastore        
        //    30: checkcast       [Ljava/lang/Object;
        //    33: invokevirtual   scala/Predef$.wrapRefArray:([Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //    36: invokespecial   scala/StringContext.<init>:(Lscala/collection/Seq;)V
        //    39: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //    42: iconst_1       
        //    43: anewarray       Ljava/lang/Object;
        //    46: dup            
        //    47: iconst_0       
        //    48: aload_3         /* table */
        //    49: aastore        
        //    50: invokevirtual   scala/Predef$.genericWrapArray:(Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //    53: invokevirtual   scala/StringContext.s:(Lscala/collection/Seq;)Ljava/lang/String;
        //    56: invokevirtual   scala/Predef$.augmentString:(Ljava/lang/String;)Ljava/lang/String;
        //    59: invokespecial   scala/collection/immutable/StringOps.<init>:(Ljava/lang/String;)V
        //    62: invokevirtual   scala/collection/immutable/StringOps.stripMargin:()Ljava/lang/String;
        //    65: astore          sql
        //    67: aload_1         /* conn */
        //    68: invokeinterface java/sql/Connection.createStatement:()Ljava/sql/Statement;
        //    73: astore          statement
        //    75: aload           statement
        //    77: ldc_w           "set optimizer = off;"
        //    80: invokeinterface java/sql/Statement.execute:(Ljava/lang/String;)Z
        //    85: pop            
        //    86: aload           statement
        //    88: aload           sql
        //    90: invokeinterface java/sql/Statement.executeQuery:(Ljava/lang/String;)Ljava/sql/ResultSet;
        //    95: astore          rs
        //    97: aload_0         /* this */
        //    98: aload           rs
        //   100: invokeinterface java/sql/ResultSet.getMetaData:()Ljava/sql/ResultSetMetaData;
        //   105: invokevirtual   io/pivotal/greenplum/spark/jdbc/Jdbc$.getSchema:(Ljava/sql/ResultSetMetaData;)Lorg/apache/spark/sql/types/StructType;
        //   108: aload           rs
        //   110: invokeinterface java/sql/ResultSet.close:()V
        //   115: aload           statement
        //   117: invokeinterface java/sql/Statement.close:()V
        //   122: areturn        
        //   123: astore          8
        //   125: aload           7
        //   127: invokeinterface java/sql/ResultSet.close:()V
        //   132: aload           8
        //   134: athrow         
        //   135: astore          6
        //   137: aload           5
        //   139: invokeinterface java/sql/Statement.close:()V
        //   144: aload           6
        //   146: athrow         
        //    StackMapTable: 00 02 FF 00 7B 00 08 07 00 02 07 01 3D 07 00 7F 07 00 F6 07 00 7F 07 00 C5 00 07 02 1C 00 01 07 02 2A FF 00 0B 00 06 07 00 02 07 01 3D 07 00 7F 07 00 F6 07 00 7F 07 00 C5 00 01 07 02 2A
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  97     108    123    135    Any
        //  123    135    135    147    Any
        //  75     115    135    147    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public Seq<String> getColumnNames(final ResultSetMetaData meta) {
        return (Seq<String>)RichInt$.MODULE$.to$extension0(Predef$.MODULE$.intWrapper(1), meta.getColumnCount()).map((Function1)new Jdbc$$anonfun$getColumnNames.Jdbc$$anonfun$getColumnNames$1(meta), IndexedSeq$.MODULE$.canBuildFrom());
    }
    
    public StructType getSchema(final ResultSetMetaData meta) {
        final StructField[] fields = new StructField[meta.getColumnCount()];
        RichInt$.MODULE$.to$extension0(Predef$.MODULE$.intWrapper(1), meta.getColumnCount()).foreach$mVc$sp((Function1)new Jdbc$$anonfun$getSchema.Jdbc$$anonfun$getSchema$1(meta, fields));
        return new StructType(fields);
    }
    
    public DataType io$pivotal$greenplum$spark$jdbc$Jdbc$$getCatalystType(final int sqlType, final int precision, final int scale, final boolean signed) {
        Object o = null;
        switch (sqlType) {
            default: {
                throw new SQLException(new StringBuilder().append((Object)"Unrecognized SQL type ").append((Object)BoxesRunTime.boxToInteger(sqlType)).toString());
            }
            case 92:
            case 93: {
                o = TimestampType$.MODULE$;
                break;
            }
            case -8: {
                o = LongType$.MODULE$;
                break;
            }
            case 4: {
                o = (signed ? IntegerType$.MODULE$ : LongType$.MODULE$);
                break;
            }
            case 6:
            case 7: {
                o = FloatType$.MODULE$;
                break;
            }
            case 8: {
                o = DoubleType$.MODULE$;
                break;
            }
            case 2:
            case 3: {
                o = ((precision != 0 || scale != 0) ? new DecimalType(precision, scale) : DecimalType$.MODULE$.SYSTEM_DEFAULT());
                break;
            }
            case -6: {
                o = IntegerType$.MODULE$;
                break;
            }
            case 5: {
                o = ShortType$.MODULE$;
                break;
            }
            case 91: {
                o = DateType$.MODULE$;
                break;
            }
            case -16:
            case -15:
            case -9:
            case -1:
            case 1:
            case 12:
            case 2002:
            case 2005:
            case 2006:
            case 2009:
            case 2011: {
                o = StringType$.MODULE$;
                break;
            }
            case 16: {
                o = BooleanType$.MODULE$;
                break;
            }
            case -7: {
                o = ((precision == 1) ? BooleanType$.MODULE$ : BinaryType$.MODULE$);
                break;
            }
            case -4:
            case -3:
            case -2:
            case 2004: {
                o = BinaryType$.MODULE$;
                break;
            }
            case -5: {
                o = (signed ? LongType$.MODULE$ : new DecimalType(20, 0));
                break;
            }
            case 0:
            case 70:
            case 1111:
            case 2000:
            case 2001:
            case 2003:
            case 2012:
            case 2013:
            case 2014: {
                o = null;
                break;
            }
        }
        final AtomicType answer = (AtomicType)o;
        if (answer == null) {
            throw new IllegalArgumentException(new StringBuilder().append((Object)"Unsupported type ").append((Object)JDBCType.valueOf(sqlType).getName()).toString());
        }
        return (DataType)answer;
    }
    
    public <T> Vector<T> io$pivotal$greenplum$spark$jdbc$Jdbc$$collectFrom(final ResultSet resultSet, final Function1<ResultSet, T> getter, final ClassTag<T> evidence$2) {
        return (Vector<T>)this.inner$1(scala.package$.MODULE$.Vector().empty(), resultSet, getter);
    }
    
    public String getDistributedTransactionId(final Connection conn) {
        final String sqlQuery = new StringOps(Predef$.MODULE$.augmentString("\n        |SELECT x.distributed_id\n        |FROM pg_settings s, gp_distributed_xacts x\n        |WHERE s.setting::int = x.gp_session_id\n        |AND s.name = 'gp_session_id'\n        |AND x.state IN ('Active Distributed', 'None')\n      ")).stripMargin();
        final ManagedResource result = package$.MODULE$.managed((scala.Function0<Object>)new Jdbc$$anonfun.Jdbc$$anonfun$13(conn), Resource$.MODULE$.statementResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)Statement.class)).flatMap((scala.Function1<Object, ManagedResource<Object>>)new Jdbc$$anonfun.Jdbc$$anonfun$14(sqlQuery));
        return (String)result.map((Function1)new Jdbc$$anonfun$getDistributedTransactionId.Jdbc$$anonfun$getDistributedTransactionId$1()).opt().get();
    }
    
    private final Vector inner$1(Vector acc, ResultSet rs, final Function1 getter$2) {
        while (rs.next()) {
            final Vector vector = (Vector)acc.$colon$plus(getter$2.apply((Object)rs), Vector$.MODULE$.canBuildFrom());
            rs = rs;
            acc = vector;
        }
        return acc;
    }
    
    private Jdbc$() {
        Logging$class.$init$(MODULE$ = this);
    }
}
