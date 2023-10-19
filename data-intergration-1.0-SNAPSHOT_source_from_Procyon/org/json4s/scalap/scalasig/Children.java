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
import scala.collection.Seq;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001db\u0001B\u0001\u0003\u0001.\u0011\u0001b\u00115jY\u0012\u0014XM\u001c\u0006\u0003\u0007\u0011\t\u0001b]2bY\u0006\u001c\u0018n\u001a\u0006\u0003\u000b\u0019\taa]2bY\u0006\u0004(BA\u0004\t\u0003\u0019Q7o\u001c85g*\t\u0011\"A\u0002pe\u001e\u001c\u0001a\u0005\u0003\u0001\u0019I)\u0002CA\u0007\u0011\u001b\u0005q!\"A\b\u0002\u000bM\u001c\u0017\r\\1\n\u0005Eq!AB!osJ+g\r\u0005\u0002\u000e'%\u0011AC\u0004\u0002\b!J|G-^2u!\tia#\u0003\u0002\u0018\u001d\ta1+\u001a:jC2L'0\u00192mK\"A\u0011\u0004\u0001BK\u0002\u0013\u0005!$\u0001\u0006ts6\u0014w\u000e\u001c*fMN,\u0012a\u0007\t\u00049\u0011:cBA\u000f#\u001d\tq\u0012%D\u0001 \u0015\t\u0001#\"\u0001\u0004=e>|GOP\u0005\u0002\u001f%\u00111ED\u0001\ba\u0006\u001c7.Y4f\u0013\t)cEA\u0002TKFT!a\t\b\u0011\u00055A\u0013BA\u0015\u000f\u0005\rIe\u000e\u001e\u0005\tW\u0001\u0011\t\u0012)A\u00057\u0005Y1/_7c_2\u0014VMZ:!\u0011\u0015i\u0003\u0001\"\u0001/\u0003\u0019a\u0014N\\5u}Q\u0011q&\r\t\u0003a\u0001i\u0011A\u0001\u0005\u000631\u0002\ra\u0007\u0005\bg\u0001\t\t\u0011\"\u00015\u0003\u0011\u0019w\u000e]=\u0015\u0005=*\u0004bB\r3!\u0003\u0005\ra\u0007\u0005\bo\u0001\t\n\u0011\"\u00019\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIE*\u0012!\u000f\u0016\u00037iZ\u0013a\u000f\t\u0003y\u0005k\u0011!\u0010\u0006\u0003}}\n\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0005\u0001s\u0011AC1o]>$\u0018\r^5p]&\u0011!)\u0010\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007b\u0002#\u0001\u0003\u0003%\t%R\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u0003\u0019\u0003\"a\u0012'\u000e\u0003!S!!\u0013&\u0002\t1\fgn\u001a\u0006\u0002\u0017\u0006!!.\u0019<b\u0013\ti\u0005J\u0001\u0004TiJLgn\u001a\u0005\b\u001f\u0002\t\t\u0011\"\u0001Q\u00031\u0001(o\u001c3vGR\f%/\u001b;z+\u00059\u0003b\u0002*\u0001\u0003\u0003%\taU\u0001\u000faJ|G-^2u\u000b2,W.\u001a8u)\t!v\u000b\u0005\u0002\u000e+&\u0011aK\u0004\u0002\u0004\u0003:L\bb\u0002-R\u0003\u0003\u0005\raJ\u0001\u0004q\u0012\n\u0004b\u0002.\u0001\u0003\u0003%\teW\u0001\u0010aJ|G-^2u\u0013R,'/\u0019;peV\tA\fE\u0002^ARk\u0011A\u0018\u0006\u0003?:\t!bY8mY\u0016\u001cG/[8o\u0013\t\tgL\u0001\u0005Ji\u0016\u0014\u0018\r^8s\u0011\u001d\u0019\u0007!!A\u0005\u0002\u0011\f\u0001bY1o\u000bF,\u0018\r\u001c\u000b\u0003K\"\u0004\"!\u00044\n\u0005\u001dt!a\u0002\"p_2,\u0017M\u001c\u0005\b1\n\f\t\u00111\u0001U\u0011\u001dQ\u0007!!A\u0005B-\f\u0001\u0002[1tQ\u000e{G-\u001a\u000b\u0002O!9Q\u000eAA\u0001\n\u0003r\u0017\u0001\u0003;p'R\u0014\u0018N\\4\u0015\u0003\u0019Cq\u0001\u001d\u0001\u0002\u0002\u0013\u0005\u0013/\u0001\u0004fcV\fGn\u001d\u000b\u0003KJDq\u0001W8\u0002\u0002\u0003\u0007AkB\u0004u\u0005\u0005\u0005\t\u0012A;\u0002\u0011\rC\u0017\u000e\u001c3sK:\u0004\"\u0001\r<\u0007\u000f\u0005\u0011\u0011\u0011!E\u0001oN\u0019a\u000f_\u000b\u0011\ted8dL\u0007\u0002u*\u00111PD\u0001\beVtG/[7f\u0013\ti(PA\tBEN$(/Y2u\rVt7\r^5p]FBQ!\f<\u0005\u0002}$\u0012!\u001e\u0005\b[Z\f\t\u0011\"\u0012o\u0011%\t)A^A\u0001\n\u0003\u000b9!A\u0003baBd\u0017\u0010F\u00020\u0003\u0013Aa!GA\u0002\u0001\u0004Y\u0002\"CA\u0007m\u0006\u0005I\u0011QA\b\u0003\u001d)h.\u00199qYf$B!!\u0005\u0002\u0018A!Q\"a\u0005\u001c\u0013\r\t)B\u0004\u0002\u0007\u001fB$\u0018n\u001c8\t\u0013\u0005e\u00111BA\u0001\u0002\u0004y\u0013a\u0001=%a!I\u0011Q\u0004<\u0002\u0002\u0013%\u0011qD\u0001\fe\u0016\fGMU3t_24X\r\u0006\u0002\u0002\"A\u0019q)a\t\n\u0007\u0005\u0015\u0002J\u0001\u0004PE*,7\r\u001e")
public class Children implements Product, Serializable
{
    private final Seq<Object> symbolRefs;
    
    public static Option<Seq<Object>> unapply(final Children x$0) {
        return Children$.MODULE$.unapply(x$0);
    }
    
    public static Children apply(final Seq<Object> symbolRefs) {
        return Children$.MODULE$.apply(symbolRefs);
    }
    
    public static <A> Function1<Seq<Object>, A> andThen(final Function1<Children, A> function1) {
        return (Function1<Seq<Object>, A>)Children$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, Children> compose(final Function1<A, Seq<Object>> function1) {
        return (Function1<A, Children>)Children$.MODULE$.compose((Function1)function1);
    }
    
    public Seq<Object> symbolRefs() {
        return this.symbolRefs;
    }
    
    public Children copy(final Seq<Object> symbolRefs) {
        return new Children(symbolRefs);
    }
    
    public Seq<Object> copy$default$1() {
        return this.symbolRefs();
    }
    
    public String productPrefix() {
        return "Children";
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
                return this.symbolRefs();
            }
        }
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof Children;
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
            if (x$1 instanceof Children) {
                final Children children = (Children)x$1;
                final Seq<Object> symbolRefs = this.symbolRefs();
                final Seq<Object> symbolRefs2 = children.symbolRefs();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (symbolRefs == null) {
                            if (symbolRefs2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!symbolRefs.equals(symbolRefs2)) {
                            break Label_0076;
                        }
                        if (children.canEqual(this)) {
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
    
    public Children(final Seq<Object> symbolRefs) {
        this.symbolRefs = symbolRefs;
        Product$class.$init$((Product)this);
    }
}
