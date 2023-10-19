// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple2;
import scala.Option;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005-c\u0001B\u0001\u0003\u0001.\u00111BU3gS:,G\rV=qK*\u00111\u0001B\u0001\tg\u000e\fG.Y:jO*\u0011QAB\u0001\u0007g\u000e\fG.\u00199\u000b\u0005\u001dA\u0011A\u00026t_:$4OC\u0001\n\u0003\ry'oZ\u0002\u0001'\u0011\u0001A\u0002\u0005\f\u0011\u00055qQ\"\u0001\u0002\n\u0005=\u0011!\u0001\u0002+za\u0016\u0004\"!\u0005\u000b\u000e\u0003IQ\u0011aE\u0001\u0006g\u000e\fG.Y\u0005\u0003+I\u0011q\u0001\u0015:pIV\u001cG\u000f\u0005\u0002\u0012/%\u0011\u0001D\u0005\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\t5\u0001\u0011)\u001a!C\u00017\u0005A1\r\\1tgNKX.F\u0001\u001d!\tiQ$\u0003\u0002\u001f\u0005\t11+_7c_2D\u0001\u0002\t\u0001\u0003\u0012\u0003\u0006I\u0001H\u0001\nG2\f7o]*z[\u0002B\u0001B\t\u0001\u0003\u0016\u0004%\taI\u0001\tif\u0004XMU3ggV\tA\u0005E\u0002&[1q!AJ\u0016\u000f\u0005\u001dRS\"\u0001\u0015\u000b\u0005%R\u0011A\u0002\u001fs_>$h(C\u0001\u0014\u0013\ta##A\u0004qC\u000e\\\u0017mZ3\n\u00059z#\u0001\u0002'jgRT!\u0001\f\n\t\u0011E\u0002!\u0011#Q\u0001\n\u0011\n\u0011\u0002^=qKJ+gm\u001d\u0011\t\u000bM\u0002A\u0011\u0001\u001b\u0002\rqJg.\u001b;?)\r)dg\u000e\t\u0003\u001b\u0001AQA\u0007\u001aA\u0002qAQA\t\u001aA\u0002\u0011Bq!\u000f\u0001\u0002\u0002\u0013\u0005!(\u0001\u0003d_BLHcA\u001b<y!9!\u0004\u000fI\u0001\u0002\u0004a\u0002b\u0002\u00129!\u0003\u0005\r\u0001\n\u0005\b}\u0001\t\n\u0011\"\u0001@\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIE*\u0012\u0001\u0011\u0016\u00039\u0005[\u0013A\u0011\t\u0003\u0007\"k\u0011\u0001\u0012\u0006\u0003\u000b\u001a\u000b\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0005\u001d\u0013\u0012AC1o]>$\u0018\r^5p]&\u0011\u0011\n\u0012\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007bB&\u0001#\u0003%\t\u0001T\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00133+\u0005i%F\u0001\u0013B\u0011\u001dy\u0005!!A\u0005BA\u000bQ\u0002\u001d:pIV\u001cG\u000f\u0015:fM&DX#A)\u0011\u0005I;V\"A*\u000b\u0005Q+\u0016\u0001\u00027b]\u001eT\u0011AV\u0001\u0005U\u00064\u0018-\u0003\u0002Y'\n11\u000b\u001e:j]\u001eDqA\u0017\u0001\u0002\u0002\u0013\u00051,\u0001\u0007qe>$Wo\u0019;Be&$\u00180F\u0001]!\t\tR,\u0003\u0002_%\t\u0019\u0011J\u001c;\t\u000f\u0001\u0004\u0011\u0011!C\u0001C\u0006q\u0001O]8ek\u000e$X\t\\3nK:$HC\u00012f!\t\t2-\u0003\u0002e%\t\u0019\u0011I\\=\t\u000f\u0019|\u0016\u0011!a\u00019\u0006\u0019\u0001\u0010J\u0019\t\u000f!\u0004\u0011\u0011!C!S\u0006y\u0001O]8ek\u000e$\u0018\n^3sCR|'/F\u0001k!\rYgNY\u0007\u0002Y*\u0011QNE\u0001\u000bG>dG.Z2uS>t\u0017BA8m\u0005!IE/\u001a:bi>\u0014\bbB9\u0001\u0003\u0003%\tA]\u0001\tG\u0006tW)];bYR\u00111O\u001e\t\u0003#QL!!\u001e\n\u0003\u000f\t{w\u000e\\3b]\"9a\r]A\u0001\u0002\u0004\u0011\u0007b\u0002=\u0001\u0003\u0003%\t%_\u0001\tQ\u0006\u001c\bnQ8eKR\tA\fC\u0004|\u0001\u0005\u0005I\u0011\t?\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012!\u0015\u0005\b}\u0002\t\t\u0011\"\u0011\u0000\u0003\u0019)\u0017/^1mgR\u00191/!\u0001\t\u000f\u0019l\u0018\u0011!a\u0001E\u001eI\u0011Q\u0001\u0002\u0002\u0002#\u0005\u0011qA\u0001\f%\u00164\u0017N\\3e)f\u0004X\rE\u0002\u000e\u0003\u00131\u0001\"\u0001\u0002\u0002\u0002#\u0005\u00111B\n\u0006\u0003\u0013\tiA\u0006\t\b\u0003\u001f\t)\u0002\b\u00136\u001b\t\t\tBC\u0002\u0002\u0014I\tqA];oi&lW-\u0003\u0003\u0002\u0018\u0005E!!E!cgR\u0014\u0018m\u0019;Gk:\u001cG/[8oe!91'!\u0003\u0005\u0002\u0005mACAA\u0004\u0011!Y\u0018\u0011BA\u0001\n\u000bb\bBCA\u0011\u0003\u0013\t\t\u0011\"!\u0002$\u0005)\u0011\r\u001d9msR)Q'!\n\u0002(!1!$a\bA\u0002qAaAIA\u0010\u0001\u0004!\u0003BCA\u0016\u0003\u0013\t\t\u0011\"!\u0002.\u00059QO\\1qa2LH\u0003BA\u0018\u0003w\u0001R!EA\u0019\u0003kI1!a\r\u0013\u0005\u0019y\u0005\u000f^5p]B)\u0011#a\u000e\u001dI%\u0019\u0011\u0011\b\n\u0003\rQ+\b\u000f\\33\u0011%\ti$!\u000b\u0002\u0002\u0003\u0007Q'A\u0002yIAB!\"!\u0011\u0002\n\u0005\u0005I\u0011BA\"\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005\u0015\u0003c\u0001*\u0002H%\u0019\u0011\u0011J*\u0003\r=\u0013'.Z2u\u0001")
public class RefinedType extends Type implements Product, Serializable
{
    private final Symbol classSym;
    private final List<Type> typeRefs;
    
    public static Option<Tuple2<Symbol, List<Type>>> unapply(final RefinedType x$0) {
        return RefinedType$.MODULE$.unapply(x$0);
    }
    
    public static RefinedType apply(final Symbol classSym, final List<Type> typeRefs) {
        return RefinedType$.MODULE$.apply(classSym, typeRefs);
    }
    
    public static Function1<Tuple2<Symbol, List<Type>>, RefinedType> tupled() {
        return (Function1<Tuple2<Symbol, List<Type>>, RefinedType>)RefinedType$.MODULE$.tupled();
    }
    
    public static Function1<Symbol, Function1<List<Type>, RefinedType>> curried() {
        return (Function1<Symbol, Function1<List<Type>, RefinedType>>)RefinedType$.MODULE$.curried();
    }
    
    public Symbol classSym() {
        return this.classSym;
    }
    
    public List<Type> typeRefs() {
        return this.typeRefs;
    }
    
    public RefinedType copy(final Symbol classSym, final List<Type> typeRefs) {
        return new RefinedType(classSym, typeRefs);
    }
    
    public Symbol copy$default$1() {
        return this.classSym();
    }
    
    public List<Type> copy$default$2() {
        return this.typeRefs();
    }
    
    public String productPrefix() {
        return "RefinedType";
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
                o = this.typeRefs();
                break;
            }
            case 0: {
                o = this.classSym();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof RefinedType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof RefinedType) {
                final RefinedType refinedType = (RefinedType)x$1;
                final Symbol classSym = this.classSym();
                final Symbol classSym2 = refinedType.classSym();
                boolean b = false;
                Label_0109: {
                    Label_0108: {
                        if (classSym == null) {
                            if (classSym2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!classSym.equals(classSym2)) {
                            break Label_0108;
                        }
                        final List<Type> typeRefs = this.typeRefs();
                        final List<Type> typeRefs2 = refinedType.typeRefs();
                        if (typeRefs == null) {
                            if (typeRefs2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!typeRefs.equals(typeRefs2)) {
                            break Label_0108;
                        }
                        if (refinedType.canEqual(this)) {
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
    
    public RefinedType(final Symbol classSym, final List<Type> typeRefs) {
        this.classSym = classSym;
        this.typeRefs = typeRefs;
        Product$class.$init$((Product)this);
    }
}
