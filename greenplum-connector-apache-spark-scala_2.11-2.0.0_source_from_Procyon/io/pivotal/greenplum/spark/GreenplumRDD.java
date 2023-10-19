// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.reflect.ClassTag$;
import scala.collection.immutable.Nil$;
import org.apache.spark.SparkContext;
import io.pivotal.greenplum.spark.externaltable.GreenplumRowIterator;
import scala.collection.Iterator;
import org.apache.spark.TaskContext;
import org.apache.spark.Partition;
import org.apache.spark.sql.jdbc.JdbcDialect;
import scala.Option;
import org.apache.spark.sql.sources.Filter;
import scala.collection.Seq;
import io.pivotal.greenplum.spark.conf.GreenplumOptions;
import org.apache.spark.sql.types.StructType;
import scala.reflect.ScalaSignature;
import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.rdd.RDD;

@ScalaSignature(bytes = "\u0006\u0001\u0005mc\u0001B\u0001\u0003\u0001-\u0011Ab\u0012:fK:\u0004H.^7S\t\u0012S!a\u0001\u0003\u0002\u000bM\u0004\u0018M]6\u000b\u0005\u00151\u0011!C4sK\u0016t\u0007\u000f\\;n\u0015\t9\u0001\"A\u0004qSZ|G/\u00197\u000b\u0003%\t!![8\u0004\u0001M\u0011\u0001\u0001\u0004\t\u0004\u001bU9R\"\u0001\b\u000b\u0005=\u0001\u0012a\u0001:eI*\u00111!\u0005\u0006\u0003%M\ta!\u00199bG\",'\"\u0001\u000b\u0002\u0007=\u0014x-\u0003\u0002\u0017\u001d\t\u0019!\u000b\u0012#\u0011\u0005aiR\"A\r\u000b\u0005iY\u0012\u0001C2bi\u0006d\u0017p\u001d;\u000b\u0005q\u0001\u0012aA:rY&\u0011a$\u0007\u0002\f\u0013:$XM\u001d8bYJ{w\u000f\u0003\u0005!\u0001\t\u0005\t\u0015!\u0003\"\u0003\t\u00198\r\u0005\u0002#G5\t\u0001#\u0003\u0002%!\ta1\u000b]1sW\u000e{g\u000e^3yi\"Aa\u0005\u0001B\u0001B\u0003%q%\u0001\u0004tG\",W.\u0019\t\u0003Q-j\u0011!\u000b\u0006\u0003Um\tQ\u0001^=qKNL!\u0001L\u0015\u0003\u0015M#(/^2u)f\u0004X\r\u0003\u0005/\u0001\t\u0005\t\u0015!\u00030\u0003)\u0001\u0018M\u001d;ji&|gn\u001d\t\u0004aM*T\"A\u0019\u000b\u0003I\nQa]2bY\u0006L!\u0001N\u0019\u0003\u000b\u0005\u0013(/Y=\u0011\u0005Y:T\"\u0001\u0002\n\u0005a\u0012!AE$sK\u0016t\u0007\u000f\\;n!\u0006\u0014H/\u001b;j_:D\u0001B\u000f\u0001\u0003\u0002\u0003\u0006IaO\u0001\u0011OJ,WM\u001c9mk6|\u0005\u000f^5p]N\u0004\"\u0001P \u000e\u0003uR!A\u0010\u0002\u0002\t\r|gNZ\u0005\u0003\u0001v\u0012\u0001c\u0012:fK:\u0004H.^7PaRLwN\\:\t\u0011\t\u0003!\u0011!Q\u0001\n\r\u000bqB]3rk&\u0014X\rZ\"pYVlgn\u001d\t\u0004\t2{eBA#K\u001d\t1\u0015*D\u0001H\u0015\tA%\"\u0001\u0004=e>|GOP\u0005\u0002e%\u00111*M\u0001\ba\u0006\u001c7.Y4f\u0013\tieJA\u0002TKFT!aS\u0019\u0011\u0005A\u001bfB\u0001\u0019R\u0013\t\u0011\u0016'\u0001\u0004Qe\u0016$WMZ\u0005\u0003)V\u0013aa\u0015;sS:<'B\u0001*2\u0011!9\u0006A!A!\u0002\u0013A\u0016a\u00024jYR,'o\u001d\t\u0004aMJ\u0006C\u0001.^\u001b\u0005Y&B\u0001/\u001c\u0003\u001d\u0019x.\u001e:dKNL!AX.\u0003\r\u0019KG\u000e^3s\u0011\u0015\u0001\u0007\u0001\"\u0001b\u0003\u0019a\u0014N\\5u}Q9!m\u00193fM\u001eD\u0007C\u0001\u001c\u0001\u0011\u0015\u0001s\f1\u0001\"\u0011\u00151s\f1\u0001(\u0011\u0015qs\f1\u00010\u0011\u0015Qt\f1\u0001<\u0011\u0015\u0011u\f1\u0001D\u0011\u00159v\f1\u0001Y\u0011\u001dQ\u0007A1A\u0005\n-\fQ\"\u00199qY&\u001c\u0017\r^5p]&#W#A(\t\r5\u0004\u0001\u0015!\u0003P\u00039\t\u0007\u000f\u001d7jG\u0006$\u0018n\u001c8JI\u0002BQa\u001c\u0001\u0005BA\fQbZ3u!\u0006\u0014H/\u001b;j_:\u001cX#A9\u0011\u0007A\u001a$\u000f\u0005\u0002#g&\u0011A\u000f\u0005\u0002\n!\u0006\u0014H/\u001b;j_:DQA\u001e\u0001\u0005B]\fqaY8naV$X\rF\u0002ywv\u00042\u0001R=\u0018\u0013\tQhJ\u0001\u0005Ji\u0016\u0014\u0018\r^8s\u0011\u0015aX\u000f1\u0001s\u0003\u001d!\b.\u001a)beRDQA`;A\u0002}\fqaY8oi\u0016DH\u000fE\u0002#\u0003\u0003I1!a\u0001\u0011\u0005-!\u0016m]6D_:$X\r\u001f;\b\u000f\u0005\u001d!\u0001#\u0001\u0002\n\u0005aqI]3f]BdW/\u001c*E\tB\u0019a'a\u0003\u0007\r\u0005\u0011\u0001\u0012AA\u0007'\u0019\tY!a\u0004\u0002\u0016A\u0019\u0001'!\u0005\n\u0007\u0005M\u0011G\u0001\u0004B]f\u0014VM\u001a\t\u0004a\u0005]\u0011bAA\rc\ta1+\u001a:jC2L'0\u00192mK\"9\u0001-a\u0003\u0005\u0002\u0005uACAA\u0005\u0011!\t\t#a\u0003\u0005\u0002\u0005\r\u0012!D2p[BLG.\u001a$jYR,'\u000f\u0006\u0004\u0002&\u0005-\u0012q\u0006\t\u0005a\u0005\u001dr*C\u0002\u0002*E\u0012aa\u00149uS>t\u0007bBA\u0017\u0003?\u0001\r!W\u0001\u0002M\"A\u0011\u0011GA\u0010\u0001\u0004\t\u0019$A\u0004eS\u0006dWm\u0019;\u0011\t\u0005U\u00121H\u0007\u0003\u0003oQ1!!\u000f\u001c\u0003\u0011QGMY2\n\t\u0005u\u0012q\u0007\u0002\f\u0015\u0012\u00147\rR5bY\u0016\u001cG\u000f\u0003\u0005\u0002\"\u0005-A\u0011AA!)\u0011\t)#a\u0011\t\u000f\u00055\u0012q\ba\u00013\"Q\u0011qIA\u0006\u0003\u0003%I!!\u0013\u0002\u0017I,\u0017\r\u001a*fg>dg/\u001a\u000b\u0003\u0003\u0017\u0002B!!\u0014\u0002X5\u0011\u0011q\n\u0006\u0005\u0003#\n\u0019&\u0001\u0003mC:<'BAA+\u0003\u0011Q\u0017M^1\n\t\u0005e\u0013q\n\u0002\u0007\u001f\nTWm\u0019;")
public class GreenplumRDD extends RDD<InternalRow>
{
    private final StructType schema;
    private final GreenplumPartition[] partitions;
    private final GreenplumOptions greenplumOptions;
    private final Seq<String> requiredColumns;
    private final Filter[] filters;
    private final String applicationId;
    
    public static Option<String> compileFilter(final Filter f) {
        return GreenplumRDD$.MODULE$.compileFilter(f);
    }
    
    public static Option<String> compileFilter(final Filter f, final JdbcDialect dialect) {
        return GreenplumRDD$.MODULE$.compileFilter(f, dialect);
    }
    
    private String applicationId() {
        return this.applicationId;
    }
    
    public Partition[] getPartitions() {
        return (Partition[])this.partitions;
    }
    
    public Iterator<InternalRow> compute(final Partition thePart, final TaskContext context) {
        final GreenplumPartition partition = (GreenplumPartition)thePart;
        return (Iterator<InternalRow>)new GreenplumRowIterator(this.applicationId(), partition, this.schema, this.greenplumOptions, this.requiredColumns, this.filters);
    }
    
    public GreenplumRDD(final SparkContext sc, final StructType schema, final GreenplumPartition[] partitions, final GreenplumOptions greenplumOptions, final Seq<String> requiredColumns, final Filter[] filters) {
        this.schema = schema;
        this.partitions = partitions;
        this.greenplumOptions = greenplumOptions;
        this.requiredColumns = requiredColumns;
        this.filters = filters;
        super(sc, (Seq)Nil$.MODULE$, ClassTag$.MODULE$.apply((Class)InternalRow.class));
        this.applicationId = sc.applicationId();
    }
}
