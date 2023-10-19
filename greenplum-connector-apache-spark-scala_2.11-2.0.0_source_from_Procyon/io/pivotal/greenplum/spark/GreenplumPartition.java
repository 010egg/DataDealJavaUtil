// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import java.lang.constant.Constable;
import scala.runtime.BoxesRunTime;
import org.apache.spark.Partition$class;
import scala.Function1;
import scala.Tuple2;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Product;
import org.apache.spark.Partition;

@ScalaSignature(bytes = "\u0006\u0001\u0005=c\u0001B\u0001\u0003\u0001.\u0011!c\u0012:fK:\u0004H.^7QCJ$\u0018\u000e^5p]*\u00111\u0001B\u0001\u0006gB\f'o\u001b\u0006\u0003\u000b\u0019\t\u0011b\u001a:fK:\u0004H.^7\u000b\u0005\u001dA\u0011a\u00029jm>$\u0018\r\u001c\u0006\u0002\u0013\u0005\u0011\u0011n\\\u0002\u0001'\u0015\u0001ABE\u000e\u001f!\ti\u0001#D\u0001\u000f\u0015\u0005y\u0011!B:dC2\f\u0017BA\t\u000f\u0005\u0019\te.\u001f*fMB\u00111#G\u0007\u0002))\u00111!\u0006\u0006\u0003-]\ta!\u00199bG\",'\"\u0001\r\u0002\u0007=\u0014x-\u0003\u0002\u001b)\tI\u0001+\u0019:uSRLwN\u001c\t\u0003\u001bqI!!\b\b\u0003\u000fA\u0013x\u000eZ;diB\u0011QbH\u0005\u0003A9\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001B\t\u0001\u0003\u0016\u0004%\taI\u0001\fo\",'/Z\"mCV\u001cX-F\u0001%!\t)\u0003F\u0004\u0002\u000eM%\u0011qED\u0001\u0007!J,G-\u001a4\n\u0005%R#AB*ue&twM\u0003\u0002(\u001d!AA\u0006\u0001B\tB\u0003%A%\u0001\u0007xQ\u0016\u0014Xm\u00117bkN,\u0007\u0005\u0003\u0005/\u0001\tU\r\u0011\"\u00010\u0003\rIG\r_\u000b\u0002aA\u0011Q\"M\u0005\u0003e9\u00111!\u00138u\u0011!!\u0004A!E!\u0002\u0013\u0001\u0014\u0001B5eq\u0002BQA\u000e\u0001\u0005\u0002]\na\u0001P5oSRtDc\u0001\u001d;wA\u0011\u0011\bA\u0007\u0002\u0005!)!%\u000ea\u0001I!)a&\u000ea\u0001a!)Q\b\u0001C!_\u0005)\u0011N\u001c3fq\")q\b\u0001C!\u0001\u0006A\u0001.Y:i\u0007>$W\rF\u00011\u0011\u0015\u0011\u0005\u0001\"\u0011D\u0003\u0019)\u0017/^1mgR\u0011Ai\u0012\t\u0003\u001b\u0015K!A\u0012\b\u0003\u000f\t{w\u000e\\3b]\")\u0001*\u0011a\u0001\u0013\u0006)q\u000e\u001e5feB\u0011QBS\u0005\u0003\u0017:\u00111!\u00118z\u0011\u001di\u0005!!A\u0005\u00029\u000bAaY8qsR\u0019\u0001h\u0014)\t\u000f\tb\u0005\u0013!a\u0001I!9a\u0006\u0014I\u0001\u0002\u0004\u0001\u0004b\u0002*\u0001#\u0003%\taU\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00132+\u0005!&F\u0001\u0013VW\u00051\u0006CA,]\u001b\u0005A&BA-[\u0003%)hn\u00195fG.,GM\u0003\u0002\\\u001d\u0005Q\u0011M\u001c8pi\u0006$\u0018n\u001c8\n\u0005uC&!E;oG\",7m[3e-\u0006\u0014\u0018.\u00198dK\"9q\fAI\u0001\n\u0003\u0001\u0017AD2paf$C-\u001a4bk2$HEM\u000b\u0002C*\u0012\u0001'\u0016\u0005\bG\u0002\t\t\u0011\"\u0011e\u00035\u0001(o\u001c3vGR\u0004&/\u001a4jqV\tQ\r\u0005\u0002gW6\tqM\u0003\u0002iS\u0006!A.\u00198h\u0015\u0005Q\u0017\u0001\u00026bm\u0006L!!K4\t\u000f5\u0004\u0011\u0011!C\u0001_\u0005a\u0001O]8ek\u000e$\u0018I]5us\"9q\u000eAA\u0001\n\u0003\u0001\u0018A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0003\u0013FDqA\u001d8\u0002\u0002\u0003\u0007\u0001'A\u0002yIEBq\u0001\u001e\u0001\u0002\u0002\u0013\u0005S/A\bqe>$Wo\u0019;Ji\u0016\u0014\u0018\r^8s+\u00051\bcA<{\u00136\t\u0001P\u0003\u0002z\u001d\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\u0005mD(\u0001C%uKJ\fGo\u001c:\t\u000fu\u0004\u0011\u0011!C\u0001}\u0006A1-\u00198FcV\fG\u000e\u0006\u0002E\u007f\"9!\u000f`A\u0001\u0002\u0004I\u0005\"CA\u0002\u0001\u0005\u0005I\u0011IA\u0003\u0003!!xn\u0015;sS:<G#A3\b\u0013\u0005%!!!A\t\u0002\u0005-\u0011AE$sK\u0016t\u0007\u000f\\;n!\u0006\u0014H/\u001b;j_:\u00042!OA\u0007\r!\t!!!A\t\u0002\u0005=1#BA\u0007\u0003#q\u0002cBA\n\u00033!\u0003\u0007O\u0007\u0003\u0003+Q1!a\u0006\u000f\u0003\u001d\u0011XO\u001c;j[\u0016LA!a\u0007\u0002\u0016\t\t\u0012IY:ue\u0006\u001cGOR;oGRLwN\u001c\u001a\t\u000fY\ni\u0001\"\u0001\u0002 Q\u0011\u00111\u0002\u0005\u000b\u0003\u0007\ti!!A\u0005F\u0005\u0015\u0001BCA\u0013\u0003\u001b\t\t\u0011\"!\u0002(\u0005)\u0011\r\u001d9msR)\u0001(!\u000b\u0002,!1!%a\tA\u0002\u0011BaALA\u0012\u0001\u0004\u0001\u0004BCA\u0018\u0003\u001b\t\t\u0011\"!\u00022\u00059QO\\1qa2LH\u0003BA\u001a\u0003\u007f\u0001R!DA\u001b\u0003sI1!a\u000e\u000f\u0005\u0019y\u0005\u000f^5p]B)Q\"a\u000f%a%\u0019\u0011Q\b\b\u0003\rQ+\b\u000f\\33\u0011%\t\t%!\f\u0002\u0002\u0003\u0007\u0001(A\u0002yIAB!\"!\u0012\u0002\u000e\u0005\u0005I\u0011BA$\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005%\u0003c\u00014\u0002L%\u0019\u0011QJ4\u0003\r=\u0013'.Z2u\u0001")
public class GreenplumPartition implements Partition, Product
{
    private final String whereClause;
    private final int idx;
    
    public static Option<Tuple2<String, Object>> unapply(final GreenplumPartition x$0) {
        return GreenplumPartition$.MODULE$.unapply(x$0);
    }
    
    public static GreenplumPartition apply(final String whereClause, final int idx) {
        return GreenplumPartition$.MODULE$.apply(whereClause, idx);
    }
    
    public static Function1<Tuple2<String, Object>, GreenplumPartition> tupled() {
        return (Function1<Tuple2<String, Object>, GreenplumPartition>)GreenplumPartition$.MODULE$.tupled();
    }
    
    public static Function1<String, Function1<Object, GreenplumPartition>> curried() {
        return (Function1<String, Function1<Object, GreenplumPartition>>)GreenplumPartition$.MODULE$.curried();
    }
    
    public String whereClause() {
        return this.whereClause;
    }
    
    public int idx() {
        return this.idx;
    }
    
    public int index() {
        return this.idx();
    }
    
    @Override
    public int hashCode() {
        return Partition$class.hashCode((Partition)this);
    }
    
    @Override
    public boolean equals(final Object other) {
        boolean b;
        if (other instanceof GreenplumPartition) {
            final GreenplumPartition greenplumPartition = (GreenplumPartition)other;
            b = (this.whereClause().equals(greenplumPartition.whereClause()) && this.idx() == greenplumPartition.idx());
        }
        else {
            b = false;
        }
        return b;
    }
    
    public GreenplumPartition copy(final String whereClause, final int idx) {
        return new GreenplumPartition(whereClause, idx);
    }
    
    public String copy$default$1() {
        return this.whereClause();
    }
    
    public int copy$default$2() {
        return this.idx();
    }
    
    public String productPrefix() {
        return "GreenplumPartition";
    }
    
    public int productArity() {
        return 2;
    }
    
    public Object productElement(final int x$1) {
        Constable constable = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 1: {
                constable = BoxesRunTime.boxToInteger(this.idx());
                break;
            }
            case 0: {
                constable = this.whereClause();
                break;
            }
        }
        return constable;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof GreenplumPartition;
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public GreenplumPartition(final String whereClause, final int idx) {
        this.whereClause = whereClause;
        this.idx = idx;
        Partition$class.$init$((Partition)this);
        Product$class.$init$((Product)this);
    }
}
