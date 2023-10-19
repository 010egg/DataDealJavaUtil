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

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001db\u0001B\u0001\u0003\u0001.\u0011a\u0002V=qK\n{WO\u001c3t)f\u0004XM\u0003\u0002\u0004\t\u0005A1oY1mCNLwM\u0003\u0002\u0006\r\u000511oY1mCBT!a\u0002\u0005\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005I\u0011aA8sO\u000e\u00011\u0003\u0002\u0001\r!Y\u0001\"!\u0004\b\u000e\u0003\tI!a\u0004\u0002\u0003\tQK\b/\u001a\t\u0003#Qi\u0011A\u0005\u0006\u0002'\u0005)1oY1mC&\u0011QC\u0005\u0002\b!J|G-^2u!\t\tr#\u0003\u0002\u0019%\ta1+\u001a:jC2L'0\u00192mK\"A!\u0004\u0001BK\u0002\u0013\u00051$A\u0003m_^,'/F\u0001\r\u0011!i\u0002A!E!\u0002\u0013a\u0011A\u00027po\u0016\u0014\b\u0005\u0003\u0005 \u0001\tU\r\u0011\"\u0001\u001c\u0003\u0015)\b\u000f]3s\u0011!\t\u0003A!E!\u0002\u0013a\u0011AB;qa\u0016\u0014\b\u0005C\u0003$\u0001\u0011\u0005A%\u0001\u0004=S:LGO\u0010\u000b\u0004K\u0019:\u0003CA\u0007\u0001\u0011\u0015Q\"\u00051\u0001\r\u0011\u0015y\"\u00051\u0001\r\u0011\u001dI\u0003!!A\u0005\u0002)\nAaY8qsR\u0019Qe\u000b\u0017\t\u000fiA\u0003\u0013!a\u0001\u0019!9q\u0004\u000bI\u0001\u0002\u0004a\u0001b\u0002\u0018\u0001#\u0003%\taL\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00132+\u0005\u0001$F\u0001\u00072W\u0005\u0011\u0004CA\u001a9\u001b\u0005!$BA\u001b7\u0003%)hn\u00195fG.,GM\u0003\u00028%\u0005Q\u0011M\u001c8pi\u0006$\u0018n\u001c8\n\u0005e\"$!E;oG\",7m[3e-\u0006\u0014\u0018.\u00198dK\"91\bAI\u0001\n\u0003y\u0013AD2paf$C-\u001a4bk2$HE\r\u0005\b{\u0001\t\t\u0011\"\u0011?\u00035\u0001(o\u001c3vGR\u0004&/\u001a4jqV\tq\b\u0005\u0002A\u000b6\t\u0011I\u0003\u0002C\u0007\u0006!A.\u00198h\u0015\u0005!\u0015\u0001\u00026bm\u0006L!AR!\u0003\rM#(/\u001b8h\u0011\u001dA\u0005!!A\u0005\u0002%\u000bA\u0002\u001d:pIV\u001cG/\u0011:jif,\u0012A\u0013\t\u0003#-K!\u0001\u0014\n\u0003\u0007%sG\u000fC\u0004O\u0001\u0005\u0005I\u0011A(\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u0011\u0001k\u0015\t\u0003#EK!A\u0015\n\u0003\u0007\u0005s\u0017\u0010C\u0004U\u001b\u0006\u0005\t\u0019\u0001&\u0002\u0007a$\u0013\u0007C\u0004W\u0001\u0005\u0005I\u0011I,\u0002\u001fA\u0014x\u000eZ;di&#XM]1u_J,\u0012\u0001\u0017\t\u00043r\u0003V\"\u0001.\u000b\u0005m\u0013\u0012AC2pY2,7\r^5p]&\u0011QL\u0017\u0002\t\u0013R,'/\u0019;pe\"9q\fAA\u0001\n\u0003\u0001\u0017\u0001C2b]\u0016\u000bX/\u00197\u0015\u0005\u0005$\u0007CA\tc\u0013\t\u0019'CA\u0004C_>dW-\u00198\t\u000fQs\u0016\u0011!a\u0001!\"9a\rAA\u0001\n\u0003:\u0017\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u0003)Cq!\u001b\u0001\u0002\u0002\u0013\u0005#.\u0001\u0005u_N#(/\u001b8h)\u0005y\u0004b\u00027\u0001\u0003\u0003%\t%\\\u0001\u0007KF,\u0018\r\\:\u0015\u0005\u0005t\u0007b\u0002+l\u0003\u0003\u0005\r\u0001U\u0004\ba\n\t\t\u0011#\u0001r\u00039!\u0016\u0010]3C_VtGm\u001d+za\u0016\u0004\"!\u0004:\u0007\u000f\u0005\u0011\u0011\u0011!E\u0001gN\u0019!\u000f\u001e\f\u0011\u000bUDH\u0002D\u0013\u000e\u0003YT!a\u001e\n\u0002\u000fI,h\u000e^5nK&\u0011\u0011P\u001e\u0002\u0012\u0003\n\u001cHO]1di\u001a+hn\u0019;j_:\u0014\u0004\"B\u0012s\t\u0003YH#A9\t\u000f%\u0014\u0018\u0011!C#U\"9aP]A\u0001\n\u0003{\u0018!B1qa2LH#B\u0013\u0002\u0002\u0005\r\u0001\"\u0002\u000e~\u0001\u0004a\u0001\"B\u0010~\u0001\u0004a\u0001\"CA\u0004e\u0006\u0005I\u0011QA\u0005\u0003\u001d)h.\u00199qYf$B!a\u0003\u0002\u0018A)\u0011#!\u0004\u0002\u0012%\u0019\u0011q\u0002\n\u0003\r=\u0003H/[8o!\u0015\t\u00121\u0003\u0007\r\u0013\r\t)B\u0005\u0002\u0007)V\u0004H.\u001a\u001a\t\u0013\u0005e\u0011QAA\u0001\u0002\u0004)\u0013a\u0001=%a!I\u0011Q\u0004:\u0002\u0002\u0013%\u0011qD\u0001\fe\u0016\fGMU3t_24X\r\u0006\u0002\u0002\"A\u0019\u0001)a\t\n\u0007\u0005\u0015\u0012I\u0001\u0004PE*,7\r\u001e")
public class TypeBoundsType extends Type implements Product, Serializable
{
    private final Type lower;
    private final Type upper;
    
    public static Option<Tuple2<Type, Type>> unapply(final TypeBoundsType x$0) {
        return TypeBoundsType$.MODULE$.unapply(x$0);
    }
    
    public static TypeBoundsType apply(final Type lower, final Type upper) {
        return TypeBoundsType$.MODULE$.apply(lower, upper);
    }
    
    public static Function1<Tuple2<Type, Type>, TypeBoundsType> tupled() {
        return (Function1<Tuple2<Type, Type>, TypeBoundsType>)TypeBoundsType$.MODULE$.tupled();
    }
    
    public static Function1<Type, Function1<Type, TypeBoundsType>> curried() {
        return (Function1<Type, Function1<Type, TypeBoundsType>>)TypeBoundsType$.MODULE$.curried();
    }
    
    public Type lower() {
        return this.lower;
    }
    
    public Type upper() {
        return this.upper;
    }
    
    public TypeBoundsType copy(final Type lower, final Type upper) {
        return new TypeBoundsType(lower, upper);
    }
    
    public Type copy$default$1() {
        return this.lower();
    }
    
    public Type copy$default$2() {
        return this.upper();
    }
    
    public String productPrefix() {
        return "TypeBoundsType";
    }
    
    public int productArity() {
        return 2;
    }
    
    public Object productElement(final int x$1) {
        Type type = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 1: {
                type = this.upper();
                break;
            }
            case 0: {
                type = this.lower();
                break;
            }
        }
        return type;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof TypeBoundsType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof TypeBoundsType) {
                final TypeBoundsType typeBoundsType = (TypeBoundsType)x$1;
                final Type lower = this.lower();
                final Type lower2 = typeBoundsType.lower();
                boolean b = false;
                Label_0109: {
                    Label_0108: {
                        if (lower == null) {
                            if (lower2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!lower.equals(lower2)) {
                            break Label_0108;
                        }
                        final Type upper = this.upper();
                        final Type upper2 = typeBoundsType.upper();
                        if (upper == null) {
                            if (upper2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!upper.equals(upper2)) {
                            break Label_0108;
                        }
                        if (typeBoundsType.canEqual(this)) {
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
    
    public TypeBoundsType(final Type lower, final Type upper) {
        this.lower = lower;
        this.upper = upper;
        Product$class.$init$((Product)this);
    }
}
