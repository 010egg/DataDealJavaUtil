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
import scala.collection.Seq;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005-c\u0001B\u0001\u0003\u0001.\u0011!\"T3uQ>$G+\u001f9f\u0015\t\u0019A!\u0001\u0005tG\u0006d\u0017m]5h\u0015\t)a!\u0001\u0004tG\u0006d\u0017\r\u001d\u0006\u0003\u000f!\taA[:p]R\u001a(\"A\u0005\u0002\u0007=\u0014xm\u0001\u0001\u0014\t\u0001a\u0001C\u0006\t\u0003\u001b9i\u0011AA\u0005\u0003\u001f\t\u0011A\u0001V=qKB\u0011\u0011\u0003F\u0007\u0002%)\t1#A\u0003tG\u0006d\u0017-\u0003\u0002\u0016%\t9\u0001K]8ek\u000e$\bCA\t\u0018\u0013\tA\"C\u0001\u0007TKJL\u0017\r\\5{C\ndW\r\u0003\u0005\u001b\u0001\tU\r\u0011\"\u0001\u001c\u0003)\u0011Xm];miRK\b/Z\u000b\u0002\u0019!AQ\u0004\u0001B\tB\u0003%A\"A\u0006sKN,H\u000e\u001e+za\u0016\u0004\u0003\u0002C\u0010\u0001\u0005+\u0007I\u0011\u0001\u0011\u0002\u0019A\f'/Y7Ts6\u0014w\u000e\\:\u0016\u0003\u0005\u00022A\t\u0016.\u001d\t\u0019\u0003F\u0004\u0002%O5\tQE\u0003\u0002'\u0015\u00051AH]8pizJ\u0011aE\u0005\u0003SI\tq\u0001]1dW\u0006<W-\u0003\u0002,Y\t\u00191+Z9\u000b\u0005%\u0012\u0002CA\u0007/\u0013\ty#A\u0001\u0004Ts6\u0014w\u000e\u001c\u0005\tc\u0001\u0011\t\u0012)A\u0005C\u0005i\u0001/\u0019:b[NKXNY8mg\u0002BQa\r\u0001\u0005\u0002Q\na\u0001P5oSRtDcA\u001b7oA\u0011Q\u0002\u0001\u0005\u00065I\u0002\r\u0001\u0004\u0005\u0006?I\u0002\r!\t\u0005\bs\u0001\t\t\u0011\"\u0001;\u0003\u0011\u0019w\u000e]=\u0015\u0007UZD\bC\u0004\u001bqA\u0005\t\u0019\u0001\u0007\t\u000f}A\u0004\u0013!a\u0001C!9a\bAI\u0001\n\u0003y\u0014AD2paf$C-\u001a4bk2$H%M\u000b\u0002\u0001*\u0012A\"Q\u0016\u0002\u0005B\u00111\tS\u0007\u0002\t*\u0011QIR\u0001\nk:\u001c\u0007.Z2lK\u0012T!a\u0012\n\u0002\u0015\u0005tgn\u001c;bi&|g.\u0003\u0002J\t\n\tRO\\2iK\u000e\\W\r\u001a,be&\fgnY3\t\u000f-\u0003\u0011\u0013!C\u0001\u0019\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\u0012T#A'+\u0005\u0005\n\u0005bB(\u0001\u0003\u0003%\t\u0005U\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u0003E\u0003\"AU,\u000e\u0003MS!\u0001V+\u0002\t1\fgn\u001a\u0006\u0002-\u0006!!.\u0019<b\u0013\tA6K\u0001\u0004TiJLgn\u001a\u0005\b5\u0002\t\t\u0011\"\u0001\\\u00031\u0001(o\u001c3vGR\f%/\u001b;z+\u0005a\u0006CA\t^\u0013\tq&CA\u0002J]RDq\u0001\u0019\u0001\u0002\u0002\u0013\u0005\u0011-\u0001\bqe>$Wo\u0019;FY\u0016lWM\u001c;\u0015\u0005\t,\u0007CA\td\u0013\t!'CA\u0002B]fDqAZ0\u0002\u0002\u0003\u0007A,A\u0002yIEBq\u0001\u001b\u0001\u0002\u0002\u0013\u0005\u0013.A\bqe>$Wo\u0019;Ji\u0016\u0014\u0018\r^8s+\u0005Q\u0007cA6oE6\tAN\u0003\u0002n%\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\u0005=d'\u0001C%uKJ\fGo\u001c:\t\u000fE\u0004\u0011\u0011!C\u0001e\u0006A1-\u00198FcV\fG\u000e\u0006\u0002tmB\u0011\u0011\u0003^\u0005\u0003kJ\u0011qAQ8pY\u0016\fg\u000eC\u0004ga\u0006\u0005\t\u0019\u00012\t\u000fa\u0004\u0011\u0011!C!s\u0006A\u0001.Y:i\u0007>$W\rF\u0001]\u0011\u001dY\b!!A\u0005Bq\f\u0001\u0002^8TiJLgn\u001a\u000b\u0002#\"9a\u0010AA\u0001\n\u0003z\u0018AB3rk\u0006d7\u000fF\u0002t\u0003\u0003AqAZ?\u0002\u0002\u0003\u0007!mB\u0005\u0002\u0006\t\t\t\u0011#\u0001\u0002\b\u0005QQ*\u001a;i_\u0012$\u0016\u0010]3\u0011\u00075\tIA\u0002\u0005\u0002\u0005\u0005\u0005\t\u0012AA\u0006'\u0015\tI!!\u0004\u0017!\u001d\ty!!\u0006\rCUj!!!\u0005\u000b\u0007\u0005M!#A\u0004sk:$\u0018.\\3\n\t\u0005]\u0011\u0011\u0003\u0002\u0012\u0003\n\u001cHO]1di\u001a+hn\u0019;j_:\u0014\u0004bB\u001a\u0002\n\u0011\u0005\u00111\u0004\u000b\u0003\u0003\u000fA\u0001b_A\u0005\u0003\u0003%)\u0005 \u0005\u000b\u0003C\tI!!A\u0005\u0002\u0006\r\u0012!B1qa2LH#B\u001b\u0002&\u0005\u001d\u0002B\u0002\u000e\u0002 \u0001\u0007A\u0002\u0003\u0004 \u0003?\u0001\r!\t\u0005\u000b\u0003W\tI!!A\u0005\u0002\u00065\u0012aB;oCB\u0004H.\u001f\u000b\u0005\u0003_\tY\u0004E\u0003\u0012\u0003c\t)$C\u0002\u00024I\u0011aa\u00149uS>t\u0007#B\t\u000281\t\u0013bAA\u001d%\t1A+\u001e9mKJB\u0011\"!\u0010\u0002*\u0005\u0005\t\u0019A\u001b\u0002\u0007a$\u0003\u0007\u0003\u0006\u0002B\u0005%\u0011\u0011!C\u0005\u0003\u0007\n1B]3bIJ+7o\u001c7wKR\u0011\u0011Q\t\t\u0004%\u0006\u001d\u0013bAA%'\n1qJ\u00196fGR\u0004")
public class MethodType extends Type implements Product, Serializable
{
    private final Type resultType;
    private final Seq<Symbol> paramSymbols;
    
    public static Option<Tuple2<Type, Seq<Symbol>>> unapply(final MethodType x$0) {
        return MethodType$.MODULE$.unapply(x$0);
    }
    
    public static MethodType apply(final Type resultType, final Seq<Symbol> paramSymbols) {
        return MethodType$.MODULE$.apply(resultType, paramSymbols);
    }
    
    public static Function1<Tuple2<Type, Seq<Symbol>>, MethodType> tupled() {
        return (Function1<Tuple2<Type, Seq<Symbol>>, MethodType>)MethodType$.MODULE$.tupled();
    }
    
    public static Function1<Type, Function1<Seq<Symbol>, MethodType>> curried() {
        return (Function1<Type, Function1<Seq<Symbol>, MethodType>>)MethodType$.MODULE$.curried();
    }
    
    public Type resultType() {
        return this.resultType;
    }
    
    public Seq<Symbol> paramSymbols() {
        return this.paramSymbols;
    }
    
    public MethodType copy(final Type resultType, final Seq<Symbol> paramSymbols) {
        return new MethodType(resultType, paramSymbols);
    }
    
    public Type copy$default$1() {
        return this.resultType();
    }
    
    public Seq<Symbol> copy$default$2() {
        return this.paramSymbols();
    }
    
    public String productPrefix() {
        return "MethodType";
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
                o = this.paramSymbols();
                break;
            }
            case 0: {
                o = this.resultType();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof MethodType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof MethodType) {
                final MethodType methodType = (MethodType)x$1;
                final Type resultType = this.resultType();
                final Type resultType2 = methodType.resultType();
                boolean b = false;
                Label_0109: {
                    Label_0108: {
                        if (resultType == null) {
                            if (resultType2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!resultType.equals(resultType2)) {
                            break Label_0108;
                        }
                        final Seq<Symbol> paramSymbols = this.paramSymbols();
                        final Seq<Symbol> paramSymbols2 = methodType.paramSymbols();
                        if (paramSymbols == null) {
                            if (paramSymbols2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!paramSymbols.equals(paramSymbols2)) {
                            break Label_0108;
                        }
                        if (methodType.canEqual(this)) {
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
    
    public MethodType(final Type resultType, final Seq<Symbol> paramSymbols) {
        this.resultType = resultType;
        this.paramSymbols = paramSymbols;
        Product$class.$init$((Product)this);
    }
}
