// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005=a\u0001B\u0001\u0003\u0001.\u0011\u0011CT;mY\u0006\u0014\u00180T3uQ>$G+\u001f9f\u0015\t\u0019A!\u0001\u0005tG\u0006d\u0017m]5h\u0015\t)a!\u0001\u0004tG\u0006d\u0017\r\u001d\u0006\u0003\u000f!\taA[:p]R\u001a(\"A\u0005\u0002\u0007=\u0014xm\u0001\u0001\u0014\t\u0001a\u0001C\u0006\t\u0003\u001b9i\u0011AA\u0005\u0003\u001f\t\u0011A\u0001V=qKB\u0011\u0011\u0003F\u0007\u0002%)\t1#A\u0003tG\u0006d\u0017-\u0003\u0002\u0016%\t9\u0001K]8ek\u000e$\bCA\t\u0018\u0013\tA\"C\u0001\u0007TKJL\u0017\r\\5{C\ndW\r\u0003\u0005\u001b\u0001\tU\r\u0011\"\u0001\u001c\u0003)\u0011Xm];miRK\b/Z\u000b\u0002\u0019!AQ\u0004\u0001B\tB\u0003%A\"A\u0006sKN,H\u000e\u001e+za\u0016\u0004\u0003\"B\u0010\u0001\t\u0003\u0001\u0013A\u0002\u001fj]&$h\b\u0006\u0002\"EA\u0011Q\u0002\u0001\u0005\u00065y\u0001\r\u0001\u0004\u0005\bI\u0001\t\t\u0011\"\u0001&\u0003\u0011\u0019w\u000e]=\u0015\u0005\u00052\u0003b\u0002\u000e$!\u0003\u0005\r\u0001\u0004\u0005\bQ\u0001\t\n\u0011\"\u0001*\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIE*\u0012A\u000b\u0016\u0003\u0019-Z\u0013\u0001\f\t\u0003[Ij\u0011A\f\u0006\u0003_A\n\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0005E\u0012\u0012AC1o]>$\u0018\r^5p]&\u00111G\f\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007bB\u001b\u0001\u0003\u0003%\tEN\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u0003]\u0002\"\u0001O\u001f\u000e\u0003eR!AO\u001e\u0002\t1\fgn\u001a\u0006\u0002y\u0005!!.\u0019<b\u0013\tq\u0014H\u0001\u0004TiJLgn\u001a\u0005\b\u0001\u0002\t\t\u0011\"\u0001B\u00031\u0001(o\u001c3vGR\f%/\u001b;z+\u0005\u0011\u0005CA\tD\u0013\t!%CA\u0002J]RDqA\u0012\u0001\u0002\u0002\u0013\u0005q)\u0001\bqe>$Wo\u0019;FY\u0016lWM\u001c;\u0015\u0005![\u0005CA\tJ\u0013\tQ%CA\u0002B]fDq\u0001T#\u0002\u0002\u0003\u0007!)A\u0002yIEBqA\u0014\u0001\u0002\u0002\u0013\u0005s*A\bqe>$Wo\u0019;Ji\u0016\u0014\u0018\r^8s+\u0005\u0001\u0006cA)U\u00116\t!K\u0003\u0002T%\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\u0005U\u0013&\u0001C%uKJ\fGo\u001c:\t\u000f]\u0003\u0011\u0011!C\u00011\u0006A1-\u00198FcV\fG\u000e\u0006\u0002Z9B\u0011\u0011CW\u0005\u00037J\u0011qAQ8pY\u0016\fg\u000eC\u0004M-\u0006\u0005\t\u0019\u0001%\t\u000fy\u0003\u0011\u0011!C!?\u0006A\u0001.Y:i\u0007>$W\rF\u0001C\u0011\u001d\t\u0007!!A\u0005B\t\f\u0001\u0002^8TiJLgn\u001a\u000b\u0002o!9A\rAA\u0001\n\u0003*\u0017AB3rk\u0006d7\u000f\u0006\u0002ZM\"9AjYA\u0001\u0002\u0004Aua\u00025\u0003\u0003\u0003E\t![\u0001\u0012\u001dVdG.\u0019:z\u001b\u0016$\bn\u001c3UsB,\u0007CA\u0007k\r\u001d\t!!!A\t\u0002-\u001c2A\u001b7\u0017!\u0011i\u0007\u000fD\u0011\u000e\u00039T!a\u001c\n\u0002\u000fI,h\u000e^5nK&\u0011\u0011O\u001c\u0002\u0012\u0003\n\u001cHO]1di\u001a+hn\u0019;j_:\f\u0004\"B\u0010k\t\u0003\u0019H#A5\t\u000f\u0005T\u0017\u0011!C#E\"9aO[A\u0001\n\u0003;\u0018!B1qa2LHCA\u0011y\u0011\u0015QR\u000f1\u0001\r\u0011\u001dQ(.!A\u0005\u0002n\fq!\u001e8baBd\u0017\u0010\u0006\u0002}\u007fB\u0019\u0011# \u0007\n\u0005y\u0014\"AB(qi&|g\u000e\u0003\u0005\u0002\u0002e\f\t\u00111\u0001\"\u0003\rAH\u0005\r\u0005\n\u0003\u000bQ\u0017\u0011!C\u0005\u0003\u000f\t1B]3bIJ+7o\u001c7wKR\u0011\u0011\u0011\u0002\t\u0004q\u0005-\u0011bAA\u0007s\t1qJ\u00196fGR\u0004")
public class NullaryMethodType extends Type implements Product, Serializable
{
    private final Type resultType;
    
    public static Option<Type> unapply(final NullaryMethodType x$0) {
        return NullaryMethodType$.MODULE$.unapply(x$0);
    }
    
    public static NullaryMethodType apply(final Type resultType) {
        return NullaryMethodType$.MODULE$.apply(resultType);
    }
    
    public static <A> Function1<Type, A> andThen(final Function1<NullaryMethodType, A> function1) {
        return (Function1<Type, A>)NullaryMethodType$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, NullaryMethodType> compose(final Function1<A, Type> function1) {
        return (Function1<A, NullaryMethodType>)NullaryMethodType$.MODULE$.compose((Function1)function1);
    }
    
    public Type resultType() {
        return this.resultType;
    }
    
    public NullaryMethodType copy(final Type resultType) {
        return new NullaryMethodType(resultType);
    }
    
    public Type copy$default$1() {
        return this.resultType();
    }
    
    public String productPrefix() {
        return "NullaryMethodType";
    }
    
    public int productArity() {
        return 1;
    }
    
    public Object productElement(final int x$1) {
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 0: {
                return this.resultType();
            }
        }
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof NullaryMethodType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof NullaryMethodType) {
                final NullaryMethodType nullaryMethodType = (NullaryMethodType)x$1;
                final Type resultType = this.resultType();
                final Type resultType2 = nullaryMethodType.resultType();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (resultType == null) {
                            if (resultType2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!resultType.equals(resultType2)) {
                            break Label_0076;
                        }
                        if (nullaryMethodType.canEqual(this)) {
                            b = true;
                            break Label_0077;
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
    
    public NullaryMethodType(final Type resultType) {
        this.resultType = resultType;
        Product$class.$init$((Product)this);
    }
}
