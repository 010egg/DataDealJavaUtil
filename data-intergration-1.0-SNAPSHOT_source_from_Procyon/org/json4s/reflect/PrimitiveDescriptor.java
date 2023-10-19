// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Product;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple2;
import scala.Function0;
import scala.Option;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005}b\u0001B\u0001\u0003\u0001&\u00111\u0003\u0015:j[&$\u0018N^3EKN\u001c'/\u001b9u_JT!a\u0001\u0003\u0002\u000fI,g\r\\3di*\u0011QAB\u0001\u0007UN|g\u000eN:\u000b\u0003\u001d\t1a\u001c:h\u0007\u0001\u0019B\u0001\u0001\u0006\u000f)A\u00111\u0002D\u0007\u0002\u0005%\u0011QB\u0001\u0002\u0011\u001f\nTWm\u0019;EKN\u001c'/\u001b9u_J\u0004\"a\u0004\n\u000e\u0003AQ\u0011!E\u0001\u0006g\u000e\fG.Y\u0005\u0003'A\u0011q\u0001\u0015:pIV\u001cG\u000f\u0005\u0002\u0010+%\u0011a\u0003\u0005\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\t1\u0001\u0011)\u001a!C\u00013\u00059QM]1tkJ,W#\u0001\u000e\u0011\u0005-Y\u0012B\u0001\u000f\u0003\u0005%\u00196-\u00197b)f\u0004X\r\u0003\u0005\u001f\u0001\tE\t\u0015!\u0003\u001b\u0003!)'/Y:ve\u0016\u0004\u0003\u0002\u0003\u0011\u0001\u0005+\u0007I\u0011A\u0011\u0002\u000f\u0011,g-Y;miV\t!\u0005E\u0002\u0010G\u0015J!\u0001\n\t\u0003\r=\u0003H/[8o!\rya\u0005K\u0005\u0003OA\u0011\u0011BR;oGRLwN\u001c\u0019\u0011\u0005=I\u0013B\u0001\u0016\u0011\u0005\r\te.\u001f\u0005\tY\u0001\u0011\t\u0012)A\u0005E\u0005AA-\u001a4bk2$\b\u0005C\u0003/\u0001\u0011\u0005q&\u0001\u0004=S:LGO\u0010\u000b\u0004aE\u0012\u0004CA\u0006\u0001\u0011\u0015AR\u00061\u0001\u001b\u0011\u001d\u0001S\u0006%AA\u0002\tBq\u0001\u000e\u0001\u0002\u0002\u0013\u0005Q'\u0001\u0003d_BLHc\u0001\u00197o!9\u0001d\rI\u0001\u0002\u0004Q\u0002b\u0002\u00114!\u0003\u0005\rA\t\u0005\bs\u0001\t\n\u0011\"\u0001;\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIE*\u0012a\u000f\u0016\u00035qZ\u0013!\u0010\t\u0003}\rk\u0011a\u0010\u0006\u0003\u0001\u0006\u000b\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0005\t\u0003\u0012AC1o]>$\u0018\r^5p]&\u0011Ai\u0010\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007b\u0002$\u0001#\u0003%\taR\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00133+\u0005A%F\u0001\u0012=\u0011\u001dQ\u0005!!A\u0005B-\u000bQ\u0002\u001d:pIV\u001cG\u000f\u0015:fM&DX#\u0001'\u0011\u00055\u0013V\"\u0001(\u000b\u0005=\u0003\u0016\u0001\u00027b]\u001eT\u0011!U\u0001\u0005U\u00064\u0018-\u0003\u0002T\u001d\n11\u000b\u001e:j]\u001eDq!\u0016\u0001\u0002\u0002\u0013\u0005a+\u0001\u0007qe>$Wo\u0019;Be&$\u00180F\u0001X!\ty\u0001,\u0003\u0002Z!\t\u0019\u0011J\u001c;\t\u000fm\u0003\u0011\u0011!C\u00019\u0006q\u0001O]8ek\u000e$X\t\\3nK:$HC\u0001\u0015^\u0011\u001dq&,!AA\u0002]\u000b1\u0001\u001f\u00132\u0011\u001d\u0001\u0007!!A\u0005B\u0005\fq\u0002\u001d:pIV\u001cG/\u0013;fe\u0006$xN]\u000b\u0002EB\u00191M\u001a\u0015\u000e\u0003\u0011T!!\u001a\t\u0002\u0015\r|G\u000e\\3di&|g.\u0003\u0002hI\nA\u0011\n^3sCR|'\u000fC\u0004j\u0001\u0005\u0005I\u0011\u00016\u0002\u0011\r\fg.R9vC2$\"a\u001b8\u0011\u0005=a\u0017BA7\u0011\u0005\u001d\u0011un\u001c7fC:DqA\u00185\u0002\u0002\u0003\u0007\u0001\u0006C\u0004q\u0001\u0005\u0005I\u0011I9\u0002\u0011!\f7\u000f[\"pI\u0016$\u0012a\u0016\u0005\bg\u0002\t\t\u0011\"\u0011u\u0003!!xn\u0015;sS:<G#\u0001'\t\u000fY\u0004\u0011\u0011!C!o\u00061Q-];bYN$\"a\u001b=\t\u000fy+\u0018\u0011!a\u0001Q\u001d9!PAA\u0001\u0012\u0003Y\u0018a\u0005)sS6LG/\u001b<f\t\u0016\u001c8M]5qi>\u0014\bCA\u0006}\r\u001d\t!!!A\t\u0002u\u001c2\u0001 @\u0015!\u0019y\u0018Q\u0001\u000e#a5\u0011\u0011\u0011\u0001\u0006\u0004\u0003\u0007\u0001\u0012a\u0002:v]RLW.Z\u0005\u0005\u0003\u000f\t\tAA\tBEN$(/Y2u\rVt7\r^5p]JBaA\f?\u0005\u0002\u0005-A#A>\t\u000fMd\u0018\u0011!C#i\"I\u0011\u0011\u0003?\u0002\u0002\u0013\u0005\u00151C\u0001\u0006CB\u0004H.\u001f\u000b\u0006a\u0005U\u0011q\u0003\u0005\u00071\u0005=\u0001\u0019\u0001\u000e\t\u0011\u0001\ny\u0001%AA\u0002\tB\u0011\"a\u0007}\u0003\u0003%\t)!\b\u0002\u000fUt\u0017\r\u001d9msR!\u0011qDA\u0014!\u0011y1%!\t\u0011\u000b=\t\u0019C\u0007\u0012\n\u0007\u0005\u0015\u0002C\u0001\u0004UkBdWM\r\u0005\n\u0003S\tI\"!AA\u0002A\n1\u0001\u001f\u00131\u0011!\ti\u0003`I\u0001\n\u00039\u0015a\u0007\u0013mKN\u001c\u0018N\\5uI\u001d\u0014X-\u0019;fe\u0012\"WMZ1vYR$#\u0007\u0003\u0005\u00022q\f\n\u0011\"\u0001H\u0003=\t\u0007\u000f\u001d7zI\u0011,g-Y;mi\u0012\u0012\u0004\"CA\u001by\u0006\u0005I\u0011BA\u001c\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005e\u0002cA'\u0002<%\u0019\u0011Q\b(\u0003\r=\u0013'.Z2u\u0001")
public class PrimitiveDescriptor extends ObjectDescriptor
{
    private final ScalaType erasure;
    private final Option<Function0<Object>> default;
    
    public static Option<Function0<Object>> apply$default$2() {
        return PrimitiveDescriptor$.MODULE$.apply$default$2();
    }
    
    public static Option<Function0<Object>> $lessinit$greater$default$2() {
        return PrimitiveDescriptor$.MODULE$.$lessinit$greater$default$2();
    }
    
    public static Option<Tuple2<ScalaType, Option<Function0<Object>>>> unapply(final PrimitiveDescriptor x$0) {
        return PrimitiveDescriptor$.MODULE$.unapply(x$0);
    }
    
    public static PrimitiveDescriptor apply(final ScalaType erasure, final Option<Function0<Object>> default1) {
        return PrimitiveDescriptor$.MODULE$.apply(erasure, default1);
    }
    
    public static Function1<Tuple2<ScalaType, Option<Function0<Object>>>, PrimitiveDescriptor> tupled() {
        return (Function1<Tuple2<ScalaType, Option<Function0<Object>>>, PrimitiveDescriptor>)PrimitiveDescriptor$.MODULE$.tupled();
    }
    
    public static Function1<ScalaType, Function1<Option<Function0<Object>>, PrimitiveDescriptor>> curried() {
        return (Function1<ScalaType, Function1<Option<Function0<Object>>, PrimitiveDescriptor>>)PrimitiveDescriptor$.MODULE$.curried();
    }
    
    public ScalaType erasure() {
        return this.erasure;
    }
    
    public Option<Function0<Object>> default() {
        return this.default;
    }
    
    public PrimitiveDescriptor copy(final ScalaType erasure, final Option<Function0<Object>> default) {
        return new PrimitiveDescriptor(erasure, default);
    }
    
    public ScalaType copy$default$1() {
        return this.erasure();
    }
    
    public Option<Function0<Object>> copy$default$2() {
        return this.default();
    }
    
    @Override
    public String productPrefix() {
        return "PrimitiveDescriptor";
    }
    
    public int productArity() {
        return 2;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 1: {
                o = this.default();
                break;
            }
            case 0: {
                o = this.erasure();
                break;
            }
        }
        return o;
    }
    
    @Override
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof PrimitiveDescriptor;
    }
    
    @Override
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof PrimitiveDescriptor) {
                final PrimitiveDescriptor primitiveDescriptor = (PrimitiveDescriptor)x$1;
                final ScalaType erasure = this.erasure();
                final ScalaType erasure2 = primitiveDescriptor.erasure();
                boolean b = false;
                Label_0109: {
                    Label_0108: {
                        if (erasure == null) {
                            if (erasure2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!erasure.equals(erasure2)) {
                            break Label_0108;
                        }
                        final Option<Function0<Object>> default1 = this.default();
                        final Option<Function0<Object>> default2 = primitiveDescriptor.default();
                        if (default1 == null) {
                            if (default2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!default1.equals(default2)) {
                            break Label_0108;
                        }
                        if (primitiveDescriptor.canEqual(this)) {
                            b = true;
                            break Label_0109;
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
    
    public PrimitiveDescriptor(final ScalaType erasure, final Option<Function0<Object>> default) {
        this.erasure = erasure;
        this.default = default;
    }
}
