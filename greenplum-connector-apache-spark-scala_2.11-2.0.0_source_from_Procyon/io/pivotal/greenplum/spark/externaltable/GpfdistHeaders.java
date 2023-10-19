// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Product$class;
import scala.runtime.Statics;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import java.lang.constant.Constable;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple3;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u00055c\u0001B\u0001\u0003\u00016\u0011ab\u00129gI&\u001cH\u000fS3bI\u0016\u00148O\u0003\u0002\u0004\t\u0005iQ\r\u001f;fe:\fG\u000e^1cY\u0016T!!\u0002\u0004\u0002\u000bM\u0004\u0018M]6\u000b\u0005\u001dA\u0011!C4sK\u0016t\u0007\u000f\\;n\u0015\tI!\"A\u0004qSZ|G/\u00197\u000b\u0003-\t!![8\u0004\u0001M!\u0001A\u0004\u000b\u0018!\ty!#D\u0001\u0011\u0015\u0005\t\u0012!B:dC2\f\u0017BA\n\u0011\u0005\u0019\te.\u001f*fMB\u0011q\"F\u0005\u0003-A\u0011q\u0001\u0015:pIV\u001cG\u000f\u0005\u0002\u00101%\u0011\u0011\u0004\u0005\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\t7\u0001\u0011)\u001a!C\u00019\u0005iAO]1og\u0006\u001cG/[8o\u0013\u0012,\u0012!\b\t\u0003=\u0005r!aD\u0010\n\u0005\u0001\u0002\u0012A\u0002)sK\u0012,g-\u0003\u0002#G\t11\u000b\u001e:j]\u001eT!\u0001\t\t\t\u0011\u0015\u0002!\u0011#Q\u0001\nu\ta\u0002\u001e:b]N\f7\r^5p]&#\u0007\u0005\u0003\u0005(\u0001\tU\r\u0011\"\u0001)\u0003%\u0019XmZ7f]RLE-F\u0001*!\ty!&\u0003\u0002,!\t\u0019\u0011J\u001c;\t\u00115\u0002!\u0011#Q\u0001\n%\n!b]3h[\u0016tG/\u00133!\u0011!y\u0003A!f\u0001\n\u0003A\u0013\u0001D:fO6,g\u000e^\"pk:$\b\u0002C\u0019\u0001\u0005#\u0005\u000b\u0011B\u0015\u0002\u001bM,w-\\3oi\u000e{WO\u001c;!\u0011\u0015\u0019\u0004\u0001\"\u00015\u0003\u0019a\u0014N\\5u}Q!Qg\u000e\u001d:!\t1\u0004!D\u0001\u0003\u0011\u0015Y\"\u00071\u0001\u001e\u0011\u00159#\u00071\u0001*\u0011\u0015y#\u00071\u0001*\u0011\u001dY\u0004!!A\u0005\u0002q\nAaY8qsR!Q'\u0010 @\u0011\u001dY\"\b%AA\u0002uAqa\n\u001e\u0011\u0002\u0003\u0007\u0011\u0006C\u00040uA\u0005\t\u0019A\u0015\t\u000f\u0005\u0003\u0011\u0013!C\u0001\u0005\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\nT#A\"+\u0005u!5&A#\u0011\u0005\u0019[U\"A$\u000b\u0005!K\u0015!C;oG\",7m[3e\u0015\tQ\u0005#\u0001\u0006b]:|G/\u0019;j_:L!\u0001T$\u0003#Ut7\r[3dW\u0016$g+\u0019:jC:\u001cW\rC\u0004O\u0001E\u0005I\u0011A(\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%eU\t\u0001K\u000b\u0002*\t\"9!\u000bAI\u0001\n\u0003y\u0015AD2paf$C-\u001a4bk2$He\r\u0005\b)\u0002\t\t\u0011\"\u0011V\u00035\u0001(o\u001c3vGR\u0004&/\u001a4jqV\ta\u000b\u0005\u0002X96\t\u0001L\u0003\u0002Z5\u0006!A.\u00198h\u0015\u0005Y\u0016\u0001\u00026bm\u0006L!A\t-\t\u000fy\u0003\u0011\u0011!C\u0001Q\u0005a\u0001O]8ek\u000e$\u0018I]5us\"9\u0001\rAA\u0001\n\u0003\t\u0017A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0003E\u0016\u0004\"aD2\n\u0005\u0011\u0004\"aA!os\"9amXA\u0001\u0002\u0004I\u0013a\u0001=%c!9\u0001\u000eAA\u0001\n\u0003J\u0017a\u00049s_\u0012,8\r^%uKJ\fGo\u001c:\u0016\u0003)\u00042a\u001b8c\u001b\u0005a'BA7\u0011\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0003_2\u0014\u0001\"\u0013;fe\u0006$xN\u001d\u0005\bc\u0002\t\t\u0011\"\u0001s\u0003!\u0019\u0017M\\#rk\u0006dGCA:w!\tyA/\u0003\u0002v!\t9!i\\8mK\u0006t\u0007b\u00024q\u0003\u0003\u0005\rA\u0019\u0005\bq\u0002\t\t\u0011\"\u0011z\u0003!A\u0017m\u001d5D_\u0012,G#A\u0015\t\u000fm\u0004\u0011\u0011!C!y\u0006AAo\\*ue&tw\rF\u0001W\u0011\u001dq\b!!A\u0005B}\fa!Z9vC2\u001cHcA:\u0002\u0002!9a-`A\u0001\u0002\u0004\u0011w!CA\u0003\u0005\u0005\u0005\t\u0012AA\u0004\u000399\u0005O\u001a3jgRDU-\u00193feN\u00042ANA\u0005\r!\t!!!A\t\u0002\u0005-1#BA\u0005\u0003\u001b9\u0002\u0003CA\b\u0003+i\u0012&K\u001b\u000e\u0005\u0005E!bAA\n!\u00059!/\u001e8uS6,\u0017\u0002BA\f\u0003#\u0011\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c84\u0011\u001d\u0019\u0014\u0011\u0002C\u0001\u00037!\"!a\u0002\t\u0011m\fI!!A\u0005FqD!\"!\t\u0002\n\u0005\u0005I\u0011QA\u0012\u0003\u0015\t\u0007\u000f\u001d7z)\u001d)\u0014QEA\u0014\u0003SAaaGA\u0010\u0001\u0004i\u0002BB\u0014\u0002 \u0001\u0007\u0011\u0006\u0003\u00040\u0003?\u0001\r!\u000b\u0005\u000b\u0003[\tI!!A\u0005\u0002\u0006=\u0012aB;oCB\u0004H.\u001f\u000b\u0005\u0003c\ti\u0004E\u0003\u0010\u0003g\t9$C\u0002\u00026A\u0011aa\u00149uS>t\u0007CB\b\u0002:uI\u0013&C\u0002\u0002<A\u0011a\u0001V;qY\u0016\u001c\u0004\"CA \u0003W\t\t\u00111\u00016\u0003\rAH\u0005\r\u0005\u000b\u0003\u0007\nI!!A\u0005\n\u0005\u0015\u0013a\u0003:fC\u0012\u0014Vm]8mm\u0016$\"!a\u0012\u0011\u0007]\u000bI%C\u0002\u0002La\u0013aa\u00142kK\u000e$\b")
public class GpfdistHeaders implements Product, Serializable
{
    private final String transactionId;
    private final int segmentId;
    private final int segmentCount;
    
    public static Option<Tuple3<String, Object, Object>> unapply(final GpfdistHeaders x$0) {
        return GpfdistHeaders$.MODULE$.unapply(x$0);
    }
    
    public static GpfdistHeaders apply(final String transactionId, final int segmentId, final int segmentCount) {
        return GpfdistHeaders$.MODULE$.apply(transactionId, segmentId, segmentCount);
    }
    
    public static Function1<Tuple3<String, Object, Object>, GpfdistHeaders> tupled() {
        return (Function1<Tuple3<String, Object, Object>, GpfdistHeaders>)GpfdistHeaders$.MODULE$.tupled();
    }
    
    public static Function1<String, Function1<Object, Function1<Object, GpfdistHeaders>>> curried() {
        return (Function1<String, Function1<Object, Function1<Object, GpfdistHeaders>>>)GpfdistHeaders$.MODULE$.curried();
    }
    
    public String transactionId() {
        return this.transactionId;
    }
    
    public int segmentId() {
        return this.segmentId;
    }
    
    public int segmentCount() {
        return this.segmentCount;
    }
    
    public GpfdistHeaders copy(final String transactionId, final int segmentId, final int segmentCount) {
        return new GpfdistHeaders(transactionId, segmentId, segmentCount);
    }
    
    public String copy$default$1() {
        return this.transactionId();
    }
    
    public int copy$default$2() {
        return this.segmentId();
    }
    
    public int copy$default$3() {
        return this.segmentCount();
    }
    
    public String productPrefix() {
        return "GpfdistHeaders";
    }
    
    public int productArity() {
        return 3;
    }
    
    public Object productElement(final int x$1) {
        Constable constable = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 2: {
                constable = BoxesRunTime.boxToInteger(this.segmentCount());
                break;
            }
            case 1: {
                constable = BoxesRunTime.boxToInteger(this.segmentId());
                break;
            }
            case 0: {
                constable = this.transactionId();
                break;
            }
        }
        return constable;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof GpfdistHeaders;
    }
    
    @Override
    public int hashCode() {
        return Statics.finalizeHash(Statics.mix(Statics.mix(Statics.mix(-889275714, Statics.anyHash((Object)this.transactionId())), this.segmentId()), this.segmentCount()), 3);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof GpfdistHeaders) {
                final GpfdistHeaders gpfdistHeaders = (GpfdistHeaders)x$1;
                final String transactionId = this.transactionId();
                final String transactionId2 = gpfdistHeaders.transactionId();
                boolean b = false;
                Label_0101: {
                    Label_0100: {
                        if (transactionId == null) {
                            if (transactionId2 != null) {
                                break Label_0100;
                            }
                        }
                        else if (!transactionId.equals(transactionId2)) {
                            break Label_0100;
                        }
                        if (this.segmentId() == gpfdistHeaders.segmentId() && this.segmentCount() == gpfdistHeaders.segmentCount() && gpfdistHeaders.canEqual(this)) {
                            b = true;
                            break Label_0101;
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
    
    public GpfdistHeaders(final String transactionId, final int segmentId, final int segmentCount) {
        this.transactionId = transactionId;
        this.segmentId = segmentId;
        this.segmentCount = segmentCount;
        Product$class.$init$((Product)this);
    }
}
