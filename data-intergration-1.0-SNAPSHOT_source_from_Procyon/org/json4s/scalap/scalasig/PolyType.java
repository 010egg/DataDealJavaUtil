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

@ScalaSignature(bytes = "\u0006\u0001\u0005-c\u0001B\u0001\u0003\u0001.\u0011\u0001\u0002U8msRK\b/\u001a\u0006\u0003\u0007\u0011\t\u0001b]2bY\u0006\u001c\u0018n\u001a\u0006\u0003\u000b\u0019\taa]2bY\u0006\u0004(BA\u0004\t\u0003\u0019Q7o\u001c85g*\t\u0011\"A\u0002pe\u001e\u001c\u0001a\u0005\u0003\u0001\u0019A1\u0002CA\u0007\u000f\u001b\u0005\u0011\u0011BA\b\u0003\u0005\u0011!\u0016\u0010]3\u0011\u0005E!R\"\u0001\n\u000b\u0003M\tQa]2bY\u0006L!!\u0006\n\u0003\u000fA\u0013x\u000eZ;diB\u0011\u0011cF\u0005\u00031I\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001B\u0007\u0001\u0003\u0016\u0004%\taG\u0001\bif\u0004XMU3g+\u0005a\u0001\u0002C\u000f\u0001\u0005#\u0005\u000b\u0011\u0002\u0007\u0002\u0011QL\b/\u001a*fM\u0002B\u0001b\b\u0001\u0003\u0016\u0004%\t\u0001I\u0001\bgfl'm\u001c7t+\u0005\t\u0003c\u0001\u0012+[9\u00111\u0005\u000b\b\u0003I\u001dj\u0011!\n\u0006\u0003M)\ta\u0001\u0010:p_Rt\u0014\"A\n\n\u0005%\u0012\u0012a\u00029bG.\fw-Z\u0005\u0003W1\u00121aU3r\u0015\tI#\u0003\u0005\u0002\u000e]%\u0011qF\u0001\u0002\u000b)f\u0004XmU=nE>d\u0007\u0002C\u0019\u0001\u0005#\u0005\u000b\u0011B\u0011\u0002\u0011MLXNY8mg\u0002BQa\r\u0001\u0005\u0002Q\na\u0001P5oSRtDcA\u001b7oA\u0011Q\u0002\u0001\u0005\u00065I\u0002\r\u0001\u0004\u0005\u0006?I\u0002\r!\t\u0005\bs\u0001\t\t\u0011\"\u0001;\u0003\u0011\u0019w\u000e]=\u0015\u0007UZD\bC\u0004\u001bqA\u0005\t\u0019\u0001\u0007\t\u000f}A\u0004\u0013!a\u0001C!9a\bAI\u0001\n\u0003y\u0014AD2paf$C-\u001a4bk2$H%M\u000b\u0002\u0001*\u0012A\"Q\u0016\u0002\u0005B\u00111\tS\u0007\u0002\t*\u0011QIR\u0001\nk:\u001c\u0007.Z2lK\u0012T!a\u0012\n\u0002\u0015\u0005tgn\u001c;bi&|g.\u0003\u0002J\t\n\tRO\\2iK\u000e\\W\r\u001a,be&\fgnY3\t\u000f-\u0003\u0011\u0013!C\u0001\u0019\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\u0012T#A'+\u0005\u0005\n\u0005bB(\u0001\u0003\u0003%\t\u0005U\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u0003E\u0003\"AU,\u000e\u0003MS!\u0001V+\u0002\t1\fgn\u001a\u0006\u0002-\u0006!!.\u0019<b\u0013\tA6K\u0001\u0004TiJLgn\u001a\u0005\b5\u0002\t\t\u0011\"\u0001\\\u00031\u0001(o\u001c3vGR\f%/\u001b;z+\u0005a\u0006CA\t^\u0013\tq&CA\u0002J]RDq\u0001\u0019\u0001\u0002\u0002\u0013\u0005\u0011-\u0001\bqe>$Wo\u0019;FY\u0016lWM\u001c;\u0015\u0005\t,\u0007CA\td\u0013\t!'CA\u0002B]fDqAZ0\u0002\u0002\u0003\u0007A,A\u0002yIEBq\u0001\u001b\u0001\u0002\u0002\u0013\u0005\u0013.A\bqe>$Wo\u0019;Ji\u0016\u0014\u0018\r^8s+\u0005Q\u0007cA6oE6\tAN\u0003\u0002n%\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\u0005=d'\u0001C%uKJ\fGo\u001c:\t\u000fE\u0004\u0011\u0011!C\u0001e\u0006A1-\u00198FcV\fG\u000e\u0006\u0002tmB\u0011\u0011\u0003^\u0005\u0003kJ\u0011qAQ8pY\u0016\fg\u000eC\u0004ga\u0006\u0005\t\u0019\u00012\t\u000fa\u0004\u0011\u0011!C!s\u0006A\u0001.Y:i\u0007>$W\rF\u0001]\u0011\u001dY\b!!A\u0005Bq\f\u0001\u0002^8TiJLgn\u001a\u000b\u0002#\"9a\u0010AA\u0001\n\u0003z\u0018AB3rk\u0006d7\u000fF\u0002t\u0003\u0003AqAZ?\u0002\u0002\u0003\u0007!mB\u0005\u0002\u0006\t\t\t\u0011#\u0001\u0002\b\u0005A\u0001k\u001c7z)f\u0004X\rE\u0002\u000e\u0003\u00131\u0001\"\u0001\u0002\u0002\u0002#\u0005\u00111B\n\u0006\u0003\u0013\tiA\u0006\t\b\u0003\u001f\t)\u0002D\u00116\u001b\t\t\tBC\u0002\u0002\u0014I\tqA];oi&lW-\u0003\u0003\u0002\u0018\u0005E!!E!cgR\u0014\u0018m\u0019;Gk:\u001cG/[8oe!91'!\u0003\u0005\u0002\u0005mACAA\u0004\u0011!Y\u0018\u0011BA\u0001\n\u000bb\bBCA\u0011\u0003\u0013\t\t\u0011\"!\u0002$\u0005)\u0011\r\u001d9msR)Q'!\n\u0002(!1!$a\bA\u00021AaaHA\u0010\u0001\u0004\t\u0003BCA\u0016\u0003\u0013\t\t\u0011\"!\u0002.\u00059QO\\1qa2LH\u0003BA\u0018\u0003w\u0001R!EA\u0019\u0003kI1!a\r\u0013\u0005\u0019y\u0005\u000f^5p]B)\u0011#a\u000e\rC%\u0019\u0011\u0011\b\n\u0003\rQ+\b\u000f\\33\u0011%\ti$!\u000b\u0002\u0002\u0003\u0007Q'A\u0002yIAB!\"!\u0011\u0002\n\u0005\u0005I\u0011BA\"\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005\u0015\u0003c\u0001*\u0002H%\u0019\u0011\u0011J*\u0003\r=\u0013'.Z2u\u0001")
public class PolyType extends Type implements Product, Serializable
{
    private final Type typeRef;
    private final Seq<TypeSymbol> symbols;
    
    public static Option<Tuple2<Type, Seq<TypeSymbol>>> unapply(final PolyType x$0) {
        return PolyType$.MODULE$.unapply(x$0);
    }
    
    public static PolyType apply(final Type typeRef, final Seq<TypeSymbol> symbols) {
        return PolyType$.MODULE$.apply(typeRef, symbols);
    }
    
    public static Function1<Tuple2<Type, Seq<TypeSymbol>>, PolyType> tupled() {
        return (Function1<Tuple2<Type, Seq<TypeSymbol>>, PolyType>)PolyType$.MODULE$.tupled();
    }
    
    public static Function1<Type, Function1<Seq<TypeSymbol>, PolyType>> curried() {
        return (Function1<Type, Function1<Seq<TypeSymbol>, PolyType>>)PolyType$.MODULE$.curried();
    }
    
    public Type typeRef() {
        return this.typeRef;
    }
    
    public Seq<TypeSymbol> symbols() {
        return this.symbols;
    }
    
    public PolyType copy(final Type typeRef, final Seq<TypeSymbol> symbols) {
        return new PolyType(typeRef, symbols);
    }
    
    public Type copy$default$1() {
        return this.typeRef();
    }
    
    public Seq<TypeSymbol> copy$default$2() {
        return this.symbols();
    }
    
    public String productPrefix() {
        return "PolyType";
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
                o = this.symbols();
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
        return x$1 instanceof PolyType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof PolyType) {
                final PolyType polyType = (PolyType)x$1;
                final Type typeRef = this.typeRef();
                final Type typeRef2 = polyType.typeRef();
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
                        final Seq<TypeSymbol> symbols = this.symbols();
                        final Seq<TypeSymbol> symbols2 = polyType.symbols();
                        if (symbols == null) {
                            if (symbols2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!symbols.equals(symbols2)) {
                            break Label_0108;
                        }
                        if (polyType.canEqual(this)) {
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
    
    public PolyType(final Type typeRef, final Seq<TypeSymbol> symbols) {
        this.typeRef = typeRef;
        this.symbols = symbols;
        Product$class.$init$((Product)this);
    }
}
