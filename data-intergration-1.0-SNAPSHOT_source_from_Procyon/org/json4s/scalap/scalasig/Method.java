// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.Statics;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple4;
import scala.Option;
import scala.collection.Seq;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u00055d\u0001B\u0001\u0003\u0001.\u0011a!T3uQ>$'BA\u0002\u0005\u0003!\u00198-\u00197bg&<'BA\u0003\u0007\u0003\u0019\u00198-\u00197ba*\u0011q\u0001C\u0001\u0007UN|g\u000eN:\u000b\u0003%\t1a\u001c:h\u0007\u0001\u0019B\u0001\u0001\u0007\u0013+A\u0011Q\u0002E\u0007\u0002\u001d)\tq\"A\u0003tG\u0006d\u0017-\u0003\u0002\u0012\u001d\t1\u0011I\\=SK\u001a\u0004\"!D\n\n\u0005Qq!a\u0002)s_\u0012,8\r\u001e\t\u0003\u001bYI!a\u0006\b\u0003\u0019M+'/[1mSj\f'\r\\3\t\u0011e\u0001!Q3A\u0005\u0002i\tQA\u001a7bON,\u0012a\u0007\t\u0003\u001bqI!!\b\b\u0003\u0007%sG\u000f\u0003\u0005 \u0001\tE\t\u0015!\u0003\u001c\u0003\u00191G.Y4tA!A\u0011\u0005\u0001BK\u0002\u0013\u0005!$A\u0005oC6,\u0017J\u001c3fq\"A1\u0005\u0001B\tB\u0003%1$\u0001\u0006oC6,\u0017J\u001c3fq\u0002B\u0001\"\n\u0001\u0003\u0016\u0004%\tAG\u0001\u0010I\u0016\u001c8M]5qi>\u0014\u0018J\u001c3fq\"Aq\u0005\u0001B\tB\u0003%1$\u0001\teKN\u001c'/\u001b9u_JLe\u000eZ3yA!A\u0011\u0006\u0001BK\u0002\u0013\u0005!&\u0001\u0006biR\u0014\u0018NY;uKN,\u0012a\u000b\t\u0004YQ:dBA\u00173\u001d\tq\u0013'D\u00010\u0015\t\u0001$\"\u0001\u0004=e>|GOP\u0005\u0002\u001f%\u00111GD\u0001\ba\u0006\u001c7.Y4f\u0013\t)dGA\u0002TKFT!a\r\b\u0011\u0005aJT\"\u0001\u0002\n\u0005i\u0012!!C!uiJL'-\u001e;f\u0011!a\u0004A!E!\u0002\u0013Y\u0013aC1uiJL'-\u001e;fg\u0002BQA\u0010\u0001\u0005\u0002}\na\u0001P5oSRtD#\u0002!B\u0005\u000e#\u0005C\u0001\u001d\u0001\u0011\u0015IR\b1\u0001\u001c\u0011\u0015\tS\b1\u0001\u001c\u0011\u0015)S\b1\u0001\u001c\u0011\u0015IS\b1\u0001,\u0011\u001d1\u0005!!A\u0005\u0002\u001d\u000bAaY8qsR)\u0001\tS%K\u0017\"9\u0011$\u0012I\u0001\u0002\u0004Y\u0002bB\u0011F!\u0003\u0005\ra\u0007\u0005\bK\u0015\u0003\n\u00111\u0001\u001c\u0011\u001dIS\t%AA\u0002-Bq!\u0014\u0001\u0012\u0002\u0013\u0005a*\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u0019\u0016\u0003=S#a\u0007),\u0003E\u0003\"AU,\u000e\u0003MS!\u0001V+\u0002\u0013Ut7\r[3dW\u0016$'B\u0001,\u000f\u0003)\tgN\\8uCRLwN\\\u0005\u00031N\u0013\u0011#\u001e8dQ\u0016\u001c7.\u001a3WCJL\u0017M\\2f\u0011\u001dQ\u0006!%A\u0005\u00029\u000babY8qs\u0012\"WMZ1vYR$#\u0007C\u0004]\u0001E\u0005I\u0011\u0001(\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%g!9a\fAI\u0001\n\u0003y\u0016AD2paf$C-\u001a4bk2$H\u0005N\u000b\u0002A*\u00121\u0006\u0015\u0005\bE\u0002\t\t\u0011\"\u0011d\u00035\u0001(o\u001c3vGR\u0004&/\u001a4jqV\tA\r\u0005\u0002fU6\taM\u0003\u0002hQ\u0006!A.\u00198h\u0015\u0005I\u0017\u0001\u00026bm\u0006L!a\u001b4\u0003\rM#(/\u001b8h\u0011\u001di\u0007!!A\u0005\u0002i\tA\u0002\u001d:pIV\u001cG/\u0011:jifDqa\u001c\u0001\u0002\u0002\u0013\u0005\u0001/\u0001\bqe>$Wo\u0019;FY\u0016lWM\u001c;\u0015\u0005E$\bCA\u0007s\u0013\t\u0019hBA\u0002B]fDq!\u001e8\u0002\u0002\u0003\u00071$A\u0002yIEBqa\u001e\u0001\u0002\u0002\u0013\u0005\u00030A\bqe>$Wo\u0019;Ji\u0016\u0014\u0018\r^8s+\u0005I\bc\u0001>~c6\t1P\u0003\u0002}\u001d\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\u0005y\\(\u0001C%uKJ\fGo\u001c:\t\u0013\u0005\u0005\u0001!!A\u0005\u0002\u0005\r\u0011\u0001C2b]\u0016\u000bX/\u00197\u0015\t\u0005\u0015\u00111\u0002\t\u0004\u001b\u0005\u001d\u0011bAA\u0005\u001d\t9!i\\8mK\u0006t\u0007bB;\u0000\u0003\u0003\u0005\r!\u001d\u0005\n\u0003\u001f\u0001\u0011\u0011!C!\u0003#\t\u0001\u0002[1tQ\u000e{G-\u001a\u000b\u00027!I\u0011Q\u0003\u0001\u0002\u0002\u0013\u0005\u0013qC\u0001\ti>\u001cFO]5oOR\tA\rC\u0005\u0002\u001c\u0001\t\t\u0011\"\u0011\u0002\u001e\u00051Q-];bYN$B!!\u0002\u0002 !AQ/!\u0007\u0002\u0002\u0003\u0007\u0011oB\u0005\u0002$\t\t\t\u0011#\u0001\u0002&\u00051Q*\u001a;i_\u0012\u00042\u0001OA\u0014\r!\t!!!A\t\u0002\u0005%2#BA\u0014\u0003W)\u0002#CA\u0017\u0003gY2dG\u0016A\u001b\t\tyCC\u0002\u000229\tqA];oi&lW-\u0003\u0003\u00026\u0005=\"!E!cgR\u0014\u0018m\u0019;Gk:\u001cG/[8oi!9a(a\n\u0005\u0002\u0005eBCAA\u0013\u0011)\t)\"a\n\u0002\u0002\u0013\u0015\u0013q\u0003\u0005\u000b\u0003\u007f\t9#!A\u0005\u0002\u0006\u0005\u0013!B1qa2LH#\u0003!\u0002D\u0005\u0015\u0013qIA%\u0011\u0019I\u0012Q\ba\u00017!1\u0011%!\u0010A\u0002mAa!JA\u001f\u0001\u0004Y\u0002BB\u0015\u0002>\u0001\u00071\u0006\u0003\u0006\u0002N\u0005\u001d\u0012\u0011!CA\u0003\u001f\nq!\u001e8baBd\u0017\u0010\u0006\u0003\u0002R\u0005u\u0003#B\u0007\u0002T\u0005]\u0013bAA+\u001d\t1q\n\u001d;j_:\u0004r!DA-7mY2&C\u0002\u0002\\9\u0011a\u0001V;qY\u0016$\u0004\"CA0\u0003\u0017\n\t\u00111\u0001A\u0003\rAH\u0005\r\u0005\u000b\u0003G\n9#!A\u0005\n\u0005\u0015\u0014a\u0003:fC\u0012\u0014Vm]8mm\u0016$\"!a\u001a\u0011\u0007\u0015\fI'C\u0002\u0002l\u0019\u0014aa\u00142kK\u000e$\b")
public class Method implements Product, Serializable
{
    private final int flags;
    private final int nameIndex;
    private final int descriptorIndex;
    private final Seq<Attribute> attributes;
    
    public static Option<Tuple4<Object, Object, Object, Seq<Attribute>>> unapply(final Method x$0) {
        return Method$.MODULE$.unapply(x$0);
    }
    
    public static Method apply(final int flags, final int nameIndex, final int descriptorIndex, final Seq<Attribute> attributes) {
        return Method$.MODULE$.apply(flags, nameIndex, descriptorIndex, attributes);
    }
    
    public static Function1<Tuple4<Object, Object, Object, Seq<Attribute>>, Method> tupled() {
        return (Function1<Tuple4<Object, Object, Object, Seq<Attribute>>, Method>)Method$.MODULE$.tupled();
    }
    
    public static Function1<Object, Function1<Object, Function1<Object, Function1<Seq<Attribute>, Method>>>> curried() {
        return (Function1<Object, Function1<Object, Function1<Object, Function1<Seq<Attribute>, Method>>>>)Method$.MODULE$.curried();
    }
    
    public int flags() {
        return this.flags;
    }
    
    public int nameIndex() {
        return this.nameIndex;
    }
    
    public int descriptorIndex() {
        return this.descriptorIndex;
    }
    
    public Seq<Attribute> attributes() {
        return this.attributes;
    }
    
    public Method copy(final int flags, final int nameIndex, final int descriptorIndex, final Seq<Attribute> attributes) {
        return new Method(flags, nameIndex, descriptorIndex, attributes);
    }
    
    public int copy$default$1() {
        return this.flags();
    }
    
    public int copy$default$2() {
        return this.nameIndex();
    }
    
    public int copy$default$3() {
        return this.descriptorIndex();
    }
    
    public Seq<Attribute> copy$default$4() {
        return this.attributes();
    }
    
    public String productPrefix() {
        return "Method";
    }
    
    public int productArity() {
        return 4;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 3: {
                o = this.attributes();
                break;
            }
            case 2: {
                o = BoxesRunTime.boxToInteger(this.descriptorIndex());
                break;
            }
            case 1: {
                o = BoxesRunTime.boxToInteger(this.nameIndex());
                break;
            }
            case 0: {
                o = BoxesRunTime.boxToInteger(this.flags());
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof Method;
    }
    
    @Override
    public int hashCode() {
        return Statics.finalizeHash(Statics.mix(Statics.mix(Statics.mix(Statics.mix(-889275714, this.flags()), this.nameIndex()), this.descriptorIndex()), Statics.anyHash((Object)this.attributes())), 4);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof Method) {
                final Method method = (Method)x$1;
                boolean b = false;
                Label_0113: {
                    Label_0112: {
                        if (this.flags() == method.flags() && this.nameIndex() == method.nameIndex() && this.descriptorIndex() == method.descriptorIndex()) {
                            final Seq<Attribute> attributes = this.attributes();
                            final Seq<Attribute> attributes2 = method.attributes();
                            if (attributes == null) {
                                if (attributes2 != null) {
                                    break Label_0112;
                                }
                            }
                            else if (!attributes.equals(attributes2)) {
                                break Label_0112;
                            }
                            if (method.canEqual(this)) {
                                b = true;
                                break Label_0113;
                            }
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
    
    public Method(final int flags, final int nameIndex, final int descriptorIndex, final Seq<Attribute> attributes) {
        this.flags = flags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
        Product$class.$init$((Product)this);
    }
}
