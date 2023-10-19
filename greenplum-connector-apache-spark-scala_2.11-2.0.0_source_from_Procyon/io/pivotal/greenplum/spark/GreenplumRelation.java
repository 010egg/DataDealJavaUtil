// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.collection.Seq;
import org.apache.spark.sql.Row;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.sources.Filter;
import scala.Function1;
import scala.reflect.ClassTag$;
import scala.Array$;
import scala.Predef$;
import io.pivotal.greenplum.spark.jdbc.ColumnValueRange;
import org.apache.spark.sql.types.StructField;
import scala.Tuple3;
import scala.Option;
import org.slf4j.Logger;
import scala.Function0;
import org.apache.spark.sql.SQLContext;
import io.pivotal.greenplum.spark.conf.GreenplumOptions;
import org.apache.spark.sql.types.StructType;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;
import org.apache.spark.sql.sources.PrunedFilteredScan;
import org.apache.spark.sql.sources.BaseRelation;

@ScalaSignature(bytes = "\u0006\u0001\t5b!B\u0001\u0003\u0001\nQ!!E$sK\u0016t\u0007\u000f\\;n%\u0016d\u0017\r^5p]*\u00111\u0001B\u0001\u0006gB\f'o\u001b\u0006\u0003\u000b\u0019\t\u0011b\u001a:fK:\u0004H.^7\u000b\u0005\u001dA\u0011a\u00029jm>$\u0018\r\u001c\u0006\u0002\u0013\u0005\u0011\u0011n\\\n\u0006\u0001-A2$\t\t\u0003\u0019Yi\u0011!\u0004\u0006\u0003\u001d=\tqa]8ve\u000e,7O\u0003\u0002\u0011#\u0005\u00191/\u001d7\u000b\u0005\r\u0011\"BA\n\u0015\u0003\u0019\t\u0007/Y2iK*\tQ#A\u0002pe\u001eL!aF\u0007\u0003\u0019\t\u000b7/\u001a*fY\u0006$\u0018n\u001c8\u0011\u00051I\u0012B\u0001\u000e\u000e\u0005I\u0001&/\u001e8fI\u001aKG\u000e^3sK\u0012\u001c6-\u00198\u0011\u0005qyR\"A\u000f\u000b\u0003y\tQa]2bY\u0006L!\u0001I\u000f\u0003\u000fA\u0013x\u000eZ;diB\u0011ADI\u0005\u0003Gu\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001\"\n\u0001\u0003\u0016\u0004%\taJ\u0001\u0007g\u000eDW-\\1\u0004\u0001U\t\u0001\u0006\u0005\u0002*Y5\t!F\u0003\u0002,\u001f\u0005)A/\u001f9fg&\u0011QF\u000b\u0002\u000b'R\u0014Xo\u0019;UsB,\u0007\u0002C\u0018\u0001\u0005#\u0005\u000b\u0011\u0002\u0015\u0002\u000fM\u001c\u0007.Z7bA!A\u0011\u0007\u0001BK\u0002\u0013\u0005!'A\u0003qCJ$8/F\u00014!\raBGN\u0005\u0003ku\u0011Q!\u0011:sCf\u0004\"a\u000e\u001d\u000e\u0003\tI!!\u000f\u0002\u0003%\u001d\u0013X-\u001a8qYVl\u0007+\u0019:uSRLwN\u001c\u0005\tw\u0001\u0011\t\u0012)A\u0005g\u00051\u0001/\u0019:ug\u0002B\u0001\"\u0010\u0001\u0003\u0016\u0004%\tAP\u0001\u0011OJ,WM\u001c9mk6|\u0005\u000f^5p]N,\u0012a\u0010\t\u0003\u0001\u000ek\u0011!\u0011\u0006\u0003\u0005\n\tAaY8oM&\u0011A)\u0011\u0002\u0011\u000fJ,WM\u001c9mk6|\u0005\u000f^5p]ND\u0001B\u0012\u0001\u0003\u0012\u0003\u0006IaP\u0001\u0012OJ,WM\u001c9mk6|\u0005\u000f^5p]N\u0004\u0003\u0002\u0003%\u0001\u0005\u000b\u0007I\u0011A%\u0002\u0015M\fHnQ8oi\u0016DH/F\u0001K!\tYE*D\u0001\u0010\u0013\tiuB\u0001\u0006T#2\u001buN\u001c;fqRD\u0001b\u0014\u0001\u0003\u0002\u0003\u0006IAS\u0001\fgFd7i\u001c8uKb$\b\u0005\u000b\u0002O#B\u0011ADU\u0005\u0003'v\u0011\u0011\u0002\u001e:b]NLWM\u001c;\t\u000bU\u0003A\u0011\u0001,\u0002\rqJg.\u001b;?)\u00119&l\u0017/\u0015\u0005aK\u0006CA\u001c\u0001\u0011\u0015AE\u000b1\u0001K\u0011\u0015)C\u000b1\u0001)\u0011\u0015\tD\u000b1\u00014\u0011\u0015iD\u000b1\u0001@\u0011\u001dq\u0006A1A\u0005B}\u000baB\\3fI\u000e{gN^3sg&|g.F\u0001a!\ta\u0012-\u0003\u0002c;\t9!i\\8mK\u0006t\u0007B\u00023\u0001A\u0003%\u0001-A\boK\u0016$7i\u001c8wKJ\u001c\u0018n\u001c8!\u0011\u00151\u0007\u0001\"\u0003h\u0003Y\u0001(o\u001c6fGR\u001c6\r[3nCR{7i\u001c7v[:\u001cHc\u0001\u0015iS\")Q%\u001aa\u0001Q!)!.\u001aa\u0001W\u000691m\u001c7v[:\u001c\bc\u0001\u000f5YB\u0011Q\u000e\u001d\b\u000399L!a\\\u000f\u0002\rA\u0013X\rZ3g\u0013\t\t(O\u0001\u0004TiJLgn\u001a\u0006\u0003_vAQ\u0001\u001e\u0001\u0005BU\f\u0001#\u001e8iC:$G.\u001a3GS2$XM]:\u0015\u0005YT\bc\u0001\u000f5oB\u0011A\u0002_\u0005\u0003s6\u0011aAR5mi\u0016\u0014\b\"B>t\u0001\u00041\u0018a\u00024jYR,'o\u001d\u0005\u0006{\u0002!\tE`\u0001\nEVLG\u000eZ*dC:$Ra`A\t\u0003+\u0001b!!\u0001\u0002\b\u0005-QBAA\u0002\u0015\r\t)!E\u0001\u0004e\u0012$\u0017\u0002BA\u0005\u0003\u0007\u00111A\u0015#E!\rY\u0015QB\u0005\u0004\u0003\u001fy!a\u0001*po\"1\u00111\u0003?A\u0002-\fqB]3rk&\u0014X\rZ\"pYVlgn\u001d\u0005\u0006wr\u0004\rA\u001e\u0005\n\u00033\u0001\u0011\u0011!C\u0001\u00037\tAaY8qsRA\u0011QDA\u0011\u0003G\t)\u0003F\u0002Y\u0003?Aa\u0001SA\f\u0001\u0004Q\u0005\u0002C\u0013\u0002\u0018A\u0005\t\u0019\u0001\u0015\t\u0011E\n9\u0002%AA\u0002MB\u0001\"PA\f!\u0003\u0005\ra\u0010\u0005\n\u0003S\u0001\u0011\u0013!C\u0001\u0003W\tabY8qs\u0012\"WMZ1vYR$\u0013'\u0006\u0002\u0002.)\u001a\u0001&a\f,\u0005\u0005E\u0002\u0003BA\u001a\u0003{i!!!\u000e\u000b\t\u0005]\u0012\u0011H\u0001\nk:\u001c\u0007.Z2lK\u0012T1!a\u000f\u001e\u0003)\tgN\\8uCRLwN\\\u0005\u0005\u0003\u007f\t)DA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016D\u0011\"a\u0011\u0001#\u0003%\t!!\u0012\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%eU\u0011\u0011q\t\u0016\u0004g\u0005=\u0002\"CA&\u0001E\u0005I\u0011AA'\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIM*\"!a\u0014+\u0007}\ny\u0003C\u0005\u0002T\u0001\t\t\u0011\"\u0011\u0002V\u0005i\u0001O]8ek\u000e$\bK]3gSb,\"!a\u0016\u0011\t\u0005e\u00131M\u0007\u0003\u00037RA!!\u0018\u0002`\u0005!A.\u00198h\u0015\t\t\t'\u0001\u0003kCZ\f\u0017bA9\u0002\\!I\u0011q\r\u0001\u0002\u0002\u0013\u0005\u0011\u0011N\u0001\raJ|G-^2u\u0003JLG/_\u000b\u0003\u0003W\u00022\u0001HA7\u0013\r\ty'\b\u0002\u0004\u0013:$\b\"CA:\u0001\u0005\u0005I\u0011AA;\u00039\u0001(o\u001c3vGR,E.Z7f]R$B!a\u001e\u0002~A\u0019A$!\u001f\n\u0007\u0005mTDA\u0002B]fD!\"a \u0002r\u0005\u0005\t\u0019AA6\u0003\rAH%\r\u0005\n\u0003\u0007\u0003\u0011\u0011!C!\u0003\u000b\u000bq\u0002\u001d:pIV\u001cG/\u0013;fe\u0006$xN]\u000b\u0003\u0003\u000f\u0003b!!#\u0002\u0010\u0006]TBAAF\u0015\r\ti)H\u0001\u000bG>dG.Z2uS>t\u0017\u0002BAI\u0003\u0017\u0013\u0001\"\u0013;fe\u0006$xN\u001d\u0005\n\u0003+\u0003\u0011\u0011!C\u0001\u0003/\u000b\u0001bY1o\u000bF,\u0018\r\u001c\u000b\u0004A\u0006e\u0005BCA@\u0003'\u000b\t\u00111\u0001\u0002x!I\u0011Q\u0014\u0001\u0002\u0002\u0013\u0005\u0013qT\u0001\tQ\u0006\u001c\bnQ8eKR\u0011\u00111\u000e\u0005\n\u0003G\u0003\u0011\u0011!C!\u0003K\u000b\u0001\u0002^8TiJLgn\u001a\u000b\u0003\u0003/B\u0011\"!+\u0001\u0003\u0003%\t%a+\u0002\r\u0015\fX/\u00197t)\r\u0001\u0017Q\u0016\u0005\u000b\u0003\u007f\n9+!AA\u0002\u0005]t\u0001CAY\u0005!%!!a-\u0002#\u001d\u0013X-\u001a8qYVl'+\u001a7bi&|g\u000eE\u00028\u0003k3q!\u0001\u0002\t\n\t\t9lE\u0004\u00026\u0006e\u0016qX\u0011\u0011\u0007q\tY,C\u0002\u0002>v\u0011a!\u00118z%\u00164\u0007cA\u001c\u0002B&\u0019\u00111\u0019\u0002\u0003\u000f1{wmZ5oO\"9Q+!.\u0005\u0002\u0005\u001dGCAAZ\u0011!\tY-!.\u0005\u0002\u00055\u0017aD2pYVlg\u000eU1si&$\u0018n\u001c8\u0015\u000fM\ny-a5\u0002^\"A\u0011\u0011[Ae\u0001\u0004\tY'A\nsKF,Xm\u001d;fIB\u000b'\u000f^5uS>t7\u000f\u0003\u0005\u0002V\u0006%\u0007\u0019AAl\u0003=\u0001\u0018M\u001d;ji&|gnQ8mk6t\u0007cA\u0015\u0002Z&\u0019\u00111\u001c\u0016\u0003\u0017M#(/^2u\r&,G\u000e\u001a\u0005\t\u0003?\fI\r1\u0001\u0002b\u0006Qa/\u00197vKJ\u000bgnZ3\u0011\t\u0005\r\u0018\u0011^\u0007\u0003\u0003KT1!a:\u0003\u0003\u0011QGMY2\n\t\u0005-\u0018Q\u001d\u0002\u0011\u0007>dW/\u001c8WC2,XMU1oO\u0016D\u0001\"a<\u00026\u0012\u0005\u0011\u0011_\u0001\u0012g\u0016<W.\u001a8u!\u0006\u0014H/\u001b;j_:\u001cHcA\u001a\u0002t\"A\u0011Q_Aw\u0001\u0004\t90\u0001\u0006tK\u001elWM\u001c;JIN\u0004B\u0001\b\u001b\u0002l!Q\u00111`A[\u0003\u0003%\t)!@\u0002\u000b\u0005\u0004\b\u000f\\=\u0015\u0011\u0005}(Q\u0001B\u0004\u0005\u0013!2\u0001\u0017B\u0001\u0011\u0019A\u0015\u0011 a\u0001\u0015\"\u001a!\u0011A)\t\r\u0015\nI\u00101\u0001)\u0011\u0019\t\u0014\u0011 a\u0001g!1Q(!?A\u0002}B!B!\u0004\u00026\u0006\u0005I\u0011\u0011B\b\u0003\u001d)h.\u00199qYf$BA!\u0005\u0003\u001eA)ADa\u0005\u0003\u0018%\u0019!QC\u000f\u0003\r=\u0003H/[8o!\u0019a\"\u0011\u0004\u00154\u007f%\u0019!1D\u000f\u0003\rQ+\b\u000f\\34\u0011%\u0011yBa\u0003\u0002\u0002\u0003\u0007\u0001,A\u0002yIAB!Ba\t\u00026\u0006\u0005I\u0011\u0002B\u0013\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\t\u001d\u0002\u0003BA-\u0005SIAAa\u000b\u0002\\\t1qJ\u00196fGR\u0004")
public class GreenplumRelation extends BaseRelation implements PrunedFilteredScan, Product, Serializable
{
    private final StructType schema;
    private final GreenplumPartition[] parts;
    private final GreenplumOptions greenplumOptions;
    private final transient SQLContext sqlContext;
    private final boolean needConversion;
    
    public static void logDebug(final Function0<String> msg) {
        GreenplumRelation$.MODULE$.logDebug(msg);
    }
    
    public static void logWarning(final Function0<String> msg) {
        GreenplumRelation$.MODULE$.logWarning(msg);
    }
    
    public static Logger log() {
        return GreenplumRelation$.MODULE$.log();
    }
    
    public static Option<Tuple3<StructType, GreenplumPartition[], GreenplumOptions>> unapply(final GreenplumRelation x$0) {
        return GreenplumRelation$.MODULE$.unapply(x$0);
    }
    
    public static GreenplumRelation apply(final StructType schema, final GreenplumPartition[] parts, final GreenplumOptions greenplumOptions, final SQLContext sqlContext) {
        return GreenplumRelation$.MODULE$.apply(schema, parts, greenplumOptions, sqlContext);
    }
    
    public static GreenplumPartition[] segmentPartitions(final int[] segmentIds) {
        return GreenplumRelation$.MODULE$.segmentPartitions(segmentIds);
    }
    
    public static GreenplumPartition[] columnPartition(final int requestedPartitions, final StructField partitionColumn, final ColumnValueRange valueRange) {
        return GreenplumRelation$.MODULE$.columnPartition(requestedPartitions, partitionColumn, valueRange);
    }
    
    public StructType schema() {
        return this.schema;
    }
    
    public GreenplumPartition[] parts() {
        return this.parts;
    }
    
    public GreenplumOptions greenplumOptions() {
        return this.greenplumOptions;
    }
    
    public SQLContext sqlContext() {
        return this.sqlContext;
    }
    
    public boolean needConversion() {
        return this.needConversion;
    }
    
    private StructType projectSchemaToColumns(final StructType schema, final String[] columns) {
        return new StructType((StructField[])Predef$.MODULE$.refArrayOps((Object[])columns).map((Function1)new GreenplumRelation$$anonfun$projectSchemaToColumns.GreenplumRelation$$anonfun$projectSchemaToColumns$1(this, schema), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)StructField.class))));
    }
    
    public Filter[] unhandledFilters(final Filter[] filters) {
        return (Filter[])Predef$.MODULE$.refArrayOps((Object[])filters).filter((Function1)new GreenplumRelation$$anonfun$unhandledFilters.GreenplumRelation$$anonfun$unhandledFilters$1(this));
    }
    
    public RDD<Row> buildScan(final String[] requiredColumns, final Filter[] filters) {
        final StructType projectedSchema = this.projectSchemaToColumns(this.schema(), requiredColumns);
        return (RDD<Row>)new GreenplumRDD(this.sqlContext().sparkContext(), projectedSchema, this.parts(), this.greenplumOptions(), (Seq<String>)Predef$.MODULE$.wrapRefArray((Object[])requiredColumns), filters);
    }
    
    public GreenplumRelation copy(final StructType schema, final GreenplumPartition[] parts, final GreenplumOptions greenplumOptions, final SQLContext sqlContext) {
        return new GreenplumRelation(schema, parts, greenplumOptions, sqlContext);
    }
    
    public StructType copy$default$1() {
        return this.schema();
    }
    
    public GreenplumPartition[] copy$default$2() {
        return this.parts();
    }
    
    public GreenplumOptions copy$default$3() {
        return this.greenplumOptions();
    }
    
    public String productPrefix() {
        return "GreenplumRelation";
    }
    
    public int productArity() {
        return 3;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 2: {
                o = this.greenplumOptions();
                break;
            }
            case 1: {
                o = this.parts();
                break;
            }
            case 0: {
                o = this.schema();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof GreenplumRelation;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof GreenplumRelation) {
                final GreenplumRelation greenplumRelation = (GreenplumRelation)x$1;
                final StructType schema = this.schema();
                final StructType schema2 = greenplumRelation.schema();
                boolean b = false;
                Label_0121: {
                    Label_0120: {
                        if (schema == null) {
                            if (schema2 != null) {
                                break Label_0120;
                            }
                        }
                        else if (!schema.equals(schema2)) {
                            break Label_0120;
                        }
                        if (this.parts() == greenplumRelation.parts()) {
                            final GreenplumOptions greenplumOptions = this.greenplumOptions();
                            final GreenplumOptions greenplumOptions2 = greenplumRelation.greenplumOptions();
                            if (greenplumOptions == null) {
                                if (greenplumOptions2 != null) {
                                    break Label_0120;
                                }
                            }
                            else if (!greenplumOptions.equals(greenplumOptions2)) {
                                break Label_0120;
                            }
                            if (greenplumRelation.canEqual(this)) {
                                b = true;
                                break Label_0121;
                            }
                        }
                    }
                    b = false;
                }
                if (b) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public GreenplumRelation(final StructType schema, final GreenplumPartition[] parts, final GreenplumOptions greenplumOptions, final SQLContext sqlContext) {
        this.schema = schema;
        this.parts = parts;
        this.greenplumOptions = greenplumOptions;
        this.sqlContext = sqlContext;
        Product$class.$init$((Product)this);
        this.needConversion = false;
    }
}
