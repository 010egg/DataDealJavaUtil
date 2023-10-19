// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Product$class;
import scala.runtime.Statics;
import scala.runtime.ScalaRunTime$;
import scala.runtime.BoxesRunTime;
import scala.Tuple4;
import scala.Option;
import java.util.concurrent.atomic.AtomicBoolean;
import scala.Function1;
import scala.collection.immutable.List;
import org.apache.spark.sql.Row;
import scala.collection.Iterator;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005mg\u0001B\u0001\u0003\u00016\u0011Q\u0002U1si&$\u0018n\u001c8ECR\f'BA\u0002\u0005\u00035)\u0007\u0010^3s]\u0006dG/\u00192mK*\u0011QAB\u0001\u0006gB\f'o\u001b\u0006\u0003\u000f!\t\u0011b\u001a:fK:\u0004H.^7\u000b\u0005%Q\u0011a\u00029jm>$\u0018\r\u001c\u0006\u0002\u0017\u0005\u0011\u0011n\\\u0002\u0001'\u0011\u0001a\u0002F\f\u0011\u0005=\u0011R\"\u0001\t\u000b\u0003E\tQa]2bY\u0006L!a\u0005\t\u0003\r\u0005s\u0017PU3g!\tyQ#\u0003\u0002\u0017!\t9\u0001K]8ek\u000e$\bCA\b\u0019\u0013\tI\u0002C\u0001\u0007TKJL\u0017\r\\5{C\ndW\r\u0003\u0005\u001c\u0001\tU\r\u0011\"\u0001\u001d\u00039\u0001\u0018M\u001d;ji&|g.\u00138eKb,\u0012!\b\t\u0003\u001fyI!a\b\t\u0003\u0007%sG\u000f\u0003\u0005\"\u0001\tE\t\u0015!\u0003\u001e\u0003=\u0001\u0018M\u001d;ji&|g.\u00138eKb\u0004\u0003\u0002C\u0012\u0001\u0005+\u0007I\u0011\u0001\u0013\u0002\u0017I|w/\u0013;fe\u0006$xN]\u000b\u0002KA\u0019aEL\u0019\u000f\u0005\u001dbcB\u0001\u0015,\u001b\u0005I#B\u0001\u0016\r\u0003\u0019a$o\\8u}%\t\u0011#\u0003\u0002.!\u00059\u0001/Y2lC\u001e,\u0017BA\u00181\u0005!IE/\u001a:bi>\u0014(BA\u0017\u0011!\t\u0011$(D\u00014\u0015\t!T'A\u0002tc2T!!\u0002\u001c\u000b\u0005]B\u0014AB1qC\u000eDWMC\u0001:\u0003\ry'oZ\u0005\u0003wM\u00121AU8x\u0011!i\u0004A!E!\u0002\u0013)\u0013\u0001\u0004:po&#XM]1u_J\u0004\u0003\u0002C \u0001\u0005#\u0007I\u0011\u0001!\u0002\tI|wo]\u000b\u0002\u0003B\u0019aEQ\u0019\n\u0005\r\u0003$\u0001\u0002'jgRD\u0001\"\u0012\u0001\u0003\u0002\u0004%\tAR\u0001\te><8o\u0018\u0013fcR\u0011qI\u0013\t\u0003\u001f!K!!\u0013\t\u0003\tUs\u0017\u000e\u001e\u0005\b\u0017\u0012\u000b\t\u00111\u0001B\u0003\rAH%\r\u0005\t\u001b\u0002\u0011\t\u0012)Q\u0005\u0003\u0006)!o\\<tA!Aq\n\u0001BK\u0002\u0013\u0005\u0001+\u0001\bs_^$&/\u00198tM>\u0014X.\u001a:\u0016\u0003E\u0003Ba\u0004*2c%\u00111\u000b\u0005\u0002\n\rVt7\r^5p]FB\u0001\"\u0016\u0001\u0003\u0012\u0003\u0006I!U\u0001\u0010e><HK]1og\u001a|'/\\3sA!)q\u000b\u0001C\u00011\u00061A(\u001b8jiz\"R!W.];z\u0003\"A\u0017\u0001\u000e\u0003\tAQa\u0007,A\u0002uAqa\t,\u0011\u0002\u0003\u0007Q\u0005C\u0004@-B\u0005\t\u0019A!\t\u000f=3\u0006\u0013!a\u0001#\"9\u0001\r\u0001b\u0001\n\u0003\t\u0017a\u00025b]\u0012dW\rZ\u000b\u0002EB\u00111\r\\\u0007\u0002I*\u0011QMZ\u0001\u0007CR|W.[2\u000b\u0005\u001dD\u0017AC2p]\u000e,(O]3oi*\u0011\u0011N[\u0001\u0005kRLGNC\u0001l\u0003\u0011Q\u0017M^1\n\u00055$'!D!u_6L7MQ8pY\u0016\fg\u000e\u0003\u0004p\u0001\u0001\u0006IAY\u0001\tQ\u0006tG\r\\3eA!9\u0011\u000fAA\u0001\n\u0003\u0011\u0018\u0001B2paf$R!W:ukZDqa\u00079\u0011\u0002\u0003\u0007Q\u0004C\u0004$aB\u0005\t\u0019A\u0013\t\u000f}\u0002\b\u0013!a\u0001\u0003\"9q\n\u001dI\u0001\u0002\u0004\t\u0006b\u0002=\u0001#\u0003%\t!_\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00132+\u0005Q(FA\u000f|W\u0005a\bcA?\u0002\u00065\taPC\u0002\u0000\u0003\u0003\t\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0007\u0005\r\u0001#\u0001\u0006b]:|G/\u0019;j_:L1!a\u0002\u007f\u0005E)hn\u00195fG.,GMV1sS\u0006t7-\u001a\u0005\n\u0003\u0017\u0001\u0011\u0013!C\u0001\u0003\u001b\tabY8qs\u0012\"WMZ1vYR$#'\u0006\u0002\u0002\u0010)\u0012Qe\u001f\u0005\n\u0003'\u0001\u0011\u0013!C\u0001\u0003+\tabY8qs\u0012\"WMZ1vYR$3'\u0006\u0002\u0002\u0018)\u0012\u0011i\u001f\u0005\n\u00037\u0001\u0011\u0013!C\u0001\u0003;\tabY8qs\u0012\"WMZ1vYR$C'\u0006\u0002\u0002 )\u0012\u0011k\u001f\u0005\n\u0003G\u0001\u0011\u0011!C!\u0003K\tQ\u0002\u001d:pIV\u001cG\u000f\u0015:fM&DXCAA\u0014!\u0011\tI#a\f\u000e\u0005\u0005-\"bAA\u0017U\u0006!A.\u00198h\u0013\u0011\t\t$a\u000b\u0003\rM#(/\u001b8h\u0011!\t)\u0004AA\u0001\n\u0003a\u0012\u0001\u00049s_\u0012,8\r^!sSRL\b\"CA\u001d\u0001\u0005\u0005I\u0011AA\u001e\u00039\u0001(o\u001c3vGR,E.Z7f]R$B!!\u0010\u0002DA\u0019q\"a\u0010\n\u0007\u0005\u0005\u0003CA\u0002B]fD\u0001bSA\u001c\u0003\u0003\u0005\r!\b\u0005\n\u0003\u000f\u0002\u0011\u0011!C!\u0003\u0013\nq\u0002\u001d:pIV\u001cG/\u0013;fe\u0006$xN]\u000b\u0003\u0003\u0017\u0002b!!\u0014\u0002T\u0005uRBAA(\u0015\r\t\t\u0006E\u0001\u000bG>dG.Z2uS>t\u0017bA\u0018\u0002P!I\u0011q\u000b\u0001\u0002\u0002\u0013\u0005\u0011\u0011L\u0001\tG\u0006tW)];bYR!\u00111LA1!\ry\u0011QL\u0005\u0004\u0003?\u0002\"a\u0002\"p_2,\u0017M\u001c\u0005\n\u0017\u0006U\u0013\u0011!a\u0001\u0003{A\u0011\"!\u001a\u0001\u0003\u0003%\t%a\u001a\u0002\u0011!\f7\u000f[\"pI\u0016$\u0012!\b\u0005\n\u0003W\u0002\u0011\u0011!C!\u0003[\n\u0001\u0002^8TiJLgn\u001a\u000b\u0003\u0003OA\u0011\"!\u001d\u0001\u0003\u0003%\t%a\u001d\u0002\r\u0015\fX/\u00197t)\u0011\tY&!\u001e\t\u0013-\u000by'!AA\u0002\u0005ur!CA=\u0005\u0005\u0005\t\u0012AA>\u00035\u0001\u0016M\u001d;ji&|g\u000eR1uCB\u0019!,! \u0007\u0011\u0005\u0011\u0011\u0011!E\u0001\u0003\u007f\u001aR!! \u0002\u0002^\u0001\u0012\"a!\u0002\nv)\u0013)U-\u000e\u0005\u0005\u0015%bAAD!\u00059!/\u001e8uS6,\u0017\u0002BAF\u0003\u000b\u0013\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c85\u0011\u001d9\u0016Q\u0010C\u0001\u0003\u001f#\"!a\u001f\t\u0015\u0005-\u0014QPA\u0001\n\u000b\ni\u0007\u0003\u0006\u0002\u0016\u0006u\u0014\u0011!CA\u0003/\u000bQ!\u00199qYf$\u0012\"WAM\u00037\u000bi*a(\t\rm\t\u0019\n1\u0001\u001e\u0011!\u0019\u00131\u0013I\u0001\u0002\u0004)\u0003\u0002C \u0002\u0014B\u0005\t\u0019A!\t\u0011=\u000b\u0019\n%AA\u0002EC!\"a)\u0002~\u0005\u0005I\u0011QAS\u0003\u001d)h.\u00199qYf$B!a*\u00024B)q\"!+\u0002.&\u0019\u00111\u0016\t\u0003\r=\u0003H/[8o!\u001dy\u0011qV\u000f&\u0003FK1!!-\u0011\u0005\u0019!V\u000f\u001d7fi!I\u0011QWAQ\u0003\u0003\u0005\r!W\u0001\u0004q\u0012\u0002\u0004BCA]\u0003{\n\n\u0011\"\u0001\u0002\u000e\u0005YB\u0005\\3tg&t\u0017\u000e\u001e\u0013he\u0016\fG/\u001a:%I\u00164\u0017-\u001e7uIIB!\"!0\u0002~E\u0005I\u0011AA\u000b\u0003m!C.Z:tS:LG\u000fJ4sK\u0006$XM\u001d\u0013eK\u001a\fW\u000f\u001c;%g!Q\u0011\u0011YA?#\u0003%\t!!\b\u00027\u0011bWm]:j]&$He\u001a:fCR,'\u000f\n3fM\u0006,H\u000e\u001e\u00135\u0011)\t)-! \u0012\u0002\u0013\u0005\u0011QB\u0001\u0010CB\u0004H.\u001f\u0013eK\u001a\fW\u000f\u001c;%e!Q\u0011\u0011ZA?#\u0003%\t!!\u0006\u0002\u001f\u0005\u0004\b\u000f\\=%I\u00164\u0017-\u001e7uIMB!\"!4\u0002~E\u0005I\u0011AA\u000f\u0003=\t\u0007\u000f\u001d7zI\u0011,g-Y;mi\u0012\"\u0004BCAi\u0003{\n\t\u0011\"\u0003\u0002T\u0006Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\t\t)\u000e\u0005\u0003\u0002*\u0005]\u0017\u0002BAm\u0003W\u0011aa\u00142kK\u000e$\b")
public class PartitionData implements Product, Serializable
{
    private final int partitionIndex;
    private final Iterator<Row> rowIterator;
    private List<Row> rows;
    private final Function1<Row, Row> rowTransformer;
    private final AtomicBoolean handled;
    
    public static Function1<Row, Row> apply$default$4() {
        return PartitionData$.MODULE$.apply$default$4();
    }
    
    public static List<Row> apply$default$3() {
        return PartitionData$.MODULE$.apply$default$3();
    }
    
    public static Iterator<Row> apply$default$2() {
        return PartitionData$.MODULE$.apply$default$2();
    }
    
    public static Function1<Row, Row> $lessinit$greater$default$4() {
        return PartitionData$.MODULE$.$lessinit$greater$default$4();
    }
    
    public static List<Row> $lessinit$greater$default$3() {
        return PartitionData$.MODULE$.$lessinit$greater$default$3();
    }
    
    public static Iterator<Row> $lessinit$greater$default$2() {
        return PartitionData$.MODULE$.$lessinit$greater$default$2();
    }
    
    public static Option<Tuple4<Object, Iterator<Row>, List<Row>, Function1<Row, Row>>> unapply(final PartitionData x$0) {
        return PartitionData$.MODULE$.unapply(x$0);
    }
    
    public static PartitionData apply(final int partitionIndex, final Iterator<Row> rowIterator, final List<Row> rows, final Function1<Row, Row> rowTransformer) {
        return PartitionData$.MODULE$.apply(partitionIndex, rowIterator, rows, rowTransformer);
    }
    
    public static Function1<Tuple4<Object, Iterator<Row>, List<Row>, Function1<Row, Row>>, PartitionData> tupled() {
        return (Function1<Tuple4<Object, Iterator<Row>, List<Row>, Function1<Row, Row>>, PartitionData>)PartitionData$.MODULE$.tupled();
    }
    
    public static Function1<Object, Function1<Iterator<Row>, Function1<List<Row>, Function1<Function1<Row, Row>, PartitionData>>>> curried() {
        return (Function1<Object, Function1<Iterator<Row>, Function1<List<Row>, Function1<Function1<Row, Row>, PartitionData>>>>)PartitionData$.MODULE$.curried();
    }
    
    public int partitionIndex() {
        return this.partitionIndex;
    }
    
    public Iterator<Row> rowIterator() {
        return this.rowIterator;
    }
    
    public List<Row> rows() {
        return this.rows;
    }
    
    public void rows_$eq(final List<Row> x$1) {
        this.rows = x$1;
    }
    
    public Function1<Row, Row> rowTransformer() {
        return this.rowTransformer;
    }
    
    public AtomicBoolean handled() {
        return this.handled;
    }
    
    public PartitionData copy(final int partitionIndex, final Iterator<Row> rowIterator, final List<Row> rows, final Function1<Row, Row> rowTransformer) {
        return new PartitionData(partitionIndex, rowIterator, rows, rowTransformer);
    }
    
    public int copy$default$1() {
        return this.partitionIndex();
    }
    
    public Iterator<Row> copy$default$2() {
        return this.rowIterator();
    }
    
    public List<Row> copy$default$3() {
        return this.rows();
    }
    
    public Function1<Row, Row> copy$default$4() {
        return this.rowTransformer();
    }
    
    public String productPrefix() {
        return "PartitionData";
    }
    
    public int productArity() {
        return 4;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 3: {
                o = this.rowTransformer();
                break;
            }
            case 2: {
                o = this.rows();
                break;
            }
            case 1: {
                o = this.rowIterator();
                break;
            }
            case 0: {
                o = BoxesRunTime.boxToInteger(this.partitionIndex());
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof PartitionData;
    }
    
    @Override
    public int hashCode() {
        return Statics.finalizeHash(Statics.mix(Statics.mix(Statics.mix(Statics.mix(-889275714, this.partitionIndex()), Statics.anyHash((Object)this.rowIterator())), Statics.anyHash((Object)this.rows())), Statics.anyHash((Object)this.rowTransformer())), 4);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof PartitionData) {
                final PartitionData partitionData = (PartitionData)x$1;
                boolean b = false;
                Label_0153: {
                    Label_0152: {
                        if (this.partitionIndex() == partitionData.partitionIndex()) {
                            final Iterator<Row> rowIterator = this.rowIterator();
                            final Iterator<Row> rowIterator2 = partitionData.rowIterator();
                            if (rowIterator == null) {
                                if (rowIterator2 != null) {
                                    break Label_0152;
                                }
                            }
                            else if (!rowIterator.equals(rowIterator2)) {
                                break Label_0152;
                            }
                            final List<Row> rows = this.rows();
                            final List<Row> rows2 = partitionData.rows();
                            if (rows == null) {
                                if (rows2 != null) {
                                    break Label_0152;
                                }
                            }
                            else if (!rows.equals(rows2)) {
                                break Label_0152;
                            }
                            final Function1<Row, Row> rowTransformer = this.rowTransformer();
                            final Function1<Row, Row> rowTransformer2 = partitionData.rowTransformer();
                            if (rowTransformer == null) {
                                if (rowTransformer2 != null) {
                                    break Label_0152;
                                }
                            }
                            else if (!rowTransformer.equals(rowTransformer2)) {
                                break Label_0152;
                            }
                            if (partitionData.canEqual(this)) {
                                b = true;
                                break Label_0153;
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
    
    public PartitionData(final int partitionIndex, final Iterator<Row> rowIterator, final List<Row> rows, final Function1<Row, Row> rowTransformer) {
        this.partitionIndex = partitionIndex;
        this.rowIterator = rowIterator;
        this.rows = rows;
        this.rowTransformer = rowTransformer;
        Product$class.$init$((Product)this);
        this.handled = new AtomicBoolean(false);
    }
}
