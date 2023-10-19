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
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005Mb\u0001B\u0001\u0003\u0001.\u0011!bU5oO2,G+\u001f9f\u0015\t\u0019A!\u0001\u0005tG\u0006d\u0017m]5h\u0015\t)a!\u0001\u0004tG\u0006d\u0017\r\u001d\u0006\u0003\u000f!\taA[:p]R\u001a(\"A\u0005\u0002\u0007=\u0014xm\u0001\u0001\u0014\t\u0001a\u0001C\u0006\t\u0003\u001b9i\u0011AA\u0005\u0003\u001f\t\u0011A\u0001V=qKB\u0011\u0011\u0003F\u0007\u0002%)\t1#A\u0003tG\u0006d\u0017-\u0003\u0002\u0016%\t9\u0001K]8ek\u000e$\bCA\t\u0018\u0013\tA\"C\u0001\u0007TKJL\u0017\r\\5{C\ndW\r\u0003\u0005\u001b\u0001\tU\r\u0011\"\u0001\u001c\u0003\u001d!\u0018\u0010]3SK\u001a,\u0012\u0001\u0004\u0005\t;\u0001\u0011\t\u0012)A\u0005\u0019\u0005AA/\u001f9f%\u00164\u0007\u0005\u0003\u0005 \u0001\tU\r\u0011\"\u0001!\u0003\u0019\u0019\u00180\u001c2pYV\t\u0011\u0005\u0005\u0002\u000eE%\u00111E\u0001\u0002\u0007'fl'm\u001c7\t\u0011\u0015\u0002!\u0011#Q\u0001\n\u0005\nqa]=nE>d\u0007\u0005C\u0003(\u0001\u0011\u0005\u0001&\u0001\u0004=S:LGO\u0010\u000b\u0004S)Z\u0003CA\u0007\u0001\u0011\u0015Qb\u00051\u0001\r\u0011\u0015yb\u00051\u0001\"\u0011\u001di\u0003!!A\u0005\u00029\nAaY8qsR\u0019\u0011f\f\u0019\t\u000fia\u0003\u0013!a\u0001\u0019!9q\u0004\fI\u0001\u0002\u0004\t\u0003b\u0002\u001a\u0001#\u0003%\taM\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00132+\u0005!$F\u0001\u00076W\u00051\u0004CA\u001c=\u001b\u0005A$BA\u001d;\u0003%)hn\u00195fG.,GM\u0003\u0002<%\u0005Q\u0011M\u001c8pi\u0006$\u0018n\u001c8\n\u0005uB$!E;oG\",7m[3e-\u0006\u0014\u0018.\u00198dK\"9q\bAI\u0001\n\u0003\u0001\u0015AD2paf$C-\u001a4bk2$HEM\u000b\u0002\u0003*\u0012\u0011%\u000e\u0005\b\u0007\u0002\t\t\u0011\"\u0011E\u00035\u0001(o\u001c3vGR\u0004&/\u001a4jqV\tQ\t\u0005\u0002G\u00176\tqI\u0003\u0002I\u0013\u0006!A.\u00198h\u0015\u0005Q\u0015\u0001\u00026bm\u0006L!\u0001T$\u0003\rM#(/\u001b8h\u0011\u001dq\u0005!!A\u0005\u0002=\u000bA\u0002\u001d:pIV\u001cG/\u0011:jif,\u0012\u0001\u0015\t\u0003#EK!A\u0015\n\u0003\u0007%sG\u000fC\u0004U\u0001\u0005\u0005I\u0011A+\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u0011a+\u0017\t\u0003#]K!\u0001\u0017\n\u0003\u0007\u0005s\u0017\u0010C\u0004['\u0006\u0005\t\u0019\u0001)\u0002\u0007a$\u0013\u0007C\u0004]\u0001\u0005\u0005I\u0011I/\u0002\u001fA\u0014x\u000eZ;di&#XM]1u_J,\u0012A\u0018\t\u0004?\n4V\"\u00011\u000b\u0005\u0005\u0014\u0012AC2pY2,7\r^5p]&\u00111\r\u0019\u0002\t\u0013R,'/\u0019;pe\"9Q\rAA\u0001\n\u00031\u0017\u0001C2b]\u0016\u000bX/\u00197\u0015\u0005\u001dT\u0007CA\ti\u0013\tI'CA\u0004C_>dW-\u00198\t\u000fi#\u0017\u0011!a\u0001-\"9A\u000eAA\u0001\n\u0003j\u0017\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u0003ACqa\u001c\u0001\u0002\u0002\u0013\u0005\u0003/\u0001\u0005u_N#(/\u001b8h)\u0005)\u0005b\u0002:\u0001\u0003\u0003%\te]\u0001\u0007KF,\u0018\r\\:\u0015\u0005\u001d$\bb\u0002.r\u0003\u0003\u0005\rAV\u0004\bm\n\t\t\u0011#\u0001x\u0003)\u0019\u0016N\\4mKRK\b/\u001a\t\u0003\u001ba4q!\u0001\u0002\u0002\u0002#\u0005\u0011pE\u0002yuZ\u0001Ra\u001f@\rC%j\u0011\u0001 \u0006\u0003{J\tqA];oi&lW-\u0003\u0002\u0000y\n\t\u0012IY:ue\u0006\u001cGOR;oGRLwN\u001c\u001a\t\r\u001dBH\u0011AA\u0002)\u00059\bbB8y\u0003\u0003%)\u0005\u001d\u0005\n\u0003\u0013A\u0018\u0011!CA\u0003\u0017\tQ!\u00199qYf$R!KA\u0007\u0003\u001fAaAGA\u0004\u0001\u0004a\u0001BB\u0010\u0002\b\u0001\u0007\u0011\u0005C\u0005\u0002\u0014a\f\t\u0011\"!\u0002\u0016\u00059QO\\1qa2LH\u0003BA\f\u0003G\u0001R!EA\r\u0003;I1!a\u0007\u0013\u0005\u0019y\u0005\u000f^5p]B)\u0011#a\b\rC%\u0019\u0011\u0011\u0005\n\u0003\rQ+\b\u000f\\33\u0011%\t)#!\u0005\u0002\u0002\u0003\u0007\u0011&A\u0002yIAB\u0011\"!\u000by\u0003\u0003%I!a\u000b\u0002\u0017I,\u0017\r\u001a*fg>dg/\u001a\u000b\u0003\u0003[\u00012ARA\u0018\u0013\r\t\td\u0012\u0002\u0007\u001f\nTWm\u0019;")
public class SingleType extends Type implements Product, Serializable
{
    private final Type typeRef;
    private final Symbol symbol;
    
    public static Option<Tuple2<Type, Symbol>> unapply(final SingleType x$0) {
        return SingleType$.MODULE$.unapply(x$0);
    }
    
    public static SingleType apply(final Type typeRef, final Symbol symbol) {
        return SingleType$.MODULE$.apply(typeRef, symbol);
    }
    
    public static Function1<Tuple2<Type, Symbol>, SingleType> tupled() {
        return (Function1<Tuple2<Type, Symbol>, SingleType>)SingleType$.MODULE$.tupled();
    }
    
    public static Function1<Type, Function1<Symbol, SingleType>> curried() {
        return (Function1<Type, Function1<Symbol, SingleType>>)SingleType$.MODULE$.curried();
    }
    
    public Type typeRef() {
        return this.typeRef;
    }
    
    public Symbol symbol() {
        return this.symbol;
    }
    
    public SingleType copy(final Type typeRef, final Symbol symbol) {
        return new SingleType(typeRef, symbol);
    }
    
    public Type copy$default$1() {
        return this.typeRef();
    }
    
    public Symbol copy$default$2() {
        return this.symbol();
    }
    
    public String productPrefix() {
        return "SingleType";
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
                o = this.symbol();
                break;
            }
            case 0: {
                o = this.typeRef();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof SingleType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof SingleType) {
                final SingleType singleType = (SingleType)x$1;
                final Type typeRef = this.typeRef();
                final Type typeRef2 = singleType.typeRef();
                boolean b = false;
                Label_0109: {
                    Label_0108: {
                        if (typeRef == null) {
                            if (typeRef2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!typeRef.equals(typeRef2)) {
                            break Label_0108;
                        }
                        final Symbol symbol = this.symbol();
                        final Symbol symbol2 = singleType.symbol();
                        if (symbol == null) {
                            if (symbol2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!symbol.equals(symbol2)) {
                            break Label_0108;
                        }
                        if (singleType.canEqual(this)) {
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
    
    public SingleType(final Type typeRef, final Symbol symbol) {
        this.typeRef = typeRef;
        this.symbol = symbol;
        Product$class.$init$((Product)this);
    }
}
