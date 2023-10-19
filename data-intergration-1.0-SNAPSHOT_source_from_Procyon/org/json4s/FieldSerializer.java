// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Product$class;
import scala.runtime.Statics;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Tuple3;
import scala.reflect.Manifest;
import scala.Option;
import scala.Tuple2;
import scala.PartialFunction;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005}h\u0001B\u0001\u0003\u0001\u001e\u0011qBR5fY\u0012\u001cVM]5bY&TXM\u001d\u0006\u0003\u0007\u0011\taA[:p]R\u001a(\"A\u0003\u0002\u0007=\u0014xm\u0001\u0001\u0016\u0005!Y5\u0003\u0002\u0001\n\u001fI\u0001\"AC\u0007\u000e\u0003-Q\u0011\u0001D\u0001\u0006g\u000e\fG.Y\u0005\u0003\u001d-\u0011a!\u00118z%\u00164\u0007C\u0001\u0006\u0011\u0013\t\t2BA\u0004Qe>$Wo\u0019;\u0011\u0005)\u0019\u0012B\u0001\u000b\f\u00051\u0019VM]5bY&T\u0018M\u00197f\u0011!1\u0002A!f\u0001\n\u00039\u0012AC:fe&\fG.\u001b>feV\t\u0001\u0004\u0005\u0003\u000b3mA\u0013B\u0001\u000e\f\u0005=\u0001\u0016M\u001d;jC24UO\\2uS>t\u0007\u0003\u0002\u0006\u001d=\u0015J!!H\u0006\u0003\rQ+\b\u000f\\33!\ty\"E\u0004\u0002\u000bA%\u0011\u0011eC\u0001\u0007!J,G-\u001a4\n\u0005\r\"#AB*ue&twM\u0003\u0002\"\u0017A\u0011!BJ\u0005\u0003O-\u00111!\u00118z!\rQ\u0011fG\u0005\u0003U-\u0011aa\u00149uS>t\u0007\u0002\u0003\u0017\u0001\u0005#\u0005\u000b\u0011\u0002\r\u0002\u0017M,'/[1mSj,'\u000f\t\u0005\t]\u0001\u0011)\u001a!C\u0001_\u0005aA-Z:fe&\fG.\u001b>feV\t\u0001\u0007\u0005\u0003\u000b3E\n\u0004C\u0001\u001a7\u001d\t\u0019D'D\u0001\u0003\u0013\t)$!A\u0004qC\u000e\\\u0017mZ3\n\u0005]B$A\u0002&GS\u0016dGM\u0003\u00026\u0005!A!\b\u0001B\tB\u0003%\u0001'A\u0007eKN,'/[1mSj,'\u000f\t\u0005\ty\u0001\u0011)\u001a!C\u0001{\u0005q\u0011N\\2mk\u0012,G*\u0019>z-\u0006dW#\u0001 \u0011\u0005)y\u0014B\u0001!\f\u0005\u001d\u0011un\u001c7fC:D\u0001B\u0011\u0001\u0003\u0012\u0003\u0006IAP\u0001\u0010S:\u001cG.\u001e3f\u0019\u0006T\u0018PV1mA!AA\t\u0001BC\u0002\u0013\rQ)\u0001\u0002nMV\ta\tE\u0002 \u000f&K!\u0001\u0013\u0013\u0003\u00115\u000bg.\u001b4fgR\u0004\"AS&\r\u0001\u0011)A\n\u0001b\u0001\u001b\n\t\u0011)\u0005\u0002OKA\u0011!bT\u0005\u0003!.\u0011qAT8uQ&tw\r\u0003\u0005S\u0001\t\u0005\t\u0015!\u0003G\u0003\rig\r\t\u0005\u0006)\u0002!\t!V\u0001\u0007y%t\u0017\u000e\u001e \u0015\tYK&l\u0017\u000b\u0003/b\u00032a\r\u0001J\u0011\u0015!5\u000bq\u0001G\u0011\u001d12\u000b%AA\u0002aAqAL*\u0011\u0002\u0003\u0007\u0001\u0007C\u0004='B\u0005\t\u0019\u0001 \t\u000fu\u0003\u0011\u0011!C\u0001=\u0006!1m\u001c9z+\ty6\r\u0006\u0003aM\u001eDGCA1e!\r\u0019\u0004A\u0019\t\u0003\u0015\u000e$Q\u0001\u0014/C\u00025CQ\u0001\u0012/A\u0004\u0015\u00042aH$c\u0011\u001d1B\f%AA\u0002aAqA\f/\u0011\u0002\u0003\u0007\u0001\u0007C\u0004=9B\u0005\t\u0019\u0001 \t\u000f)\u0004\u0011\u0013!C\u0001W\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\nTC\u00017x+\u0005i'F\u0001\roW\u0005y\u0007C\u00019v\u001b\u0005\t(B\u0001:t\u0003%)hn\u00195fG.,GM\u0003\u0002u\u0017\u0005Q\u0011M\u001c8pi\u0006$\u0018n\u001c8\n\u0005Y\f(!E;oG\",7m[3e-\u0006\u0014\u0018.\u00198dK\u0012)A*\u001bb\u0001\u001b\"9\u0011\u0010AI\u0001\n\u0003Q\u0018AD2paf$C-\u001a4bk2$HEM\u000b\u0003wv,\u0012\u0001 \u0016\u0003a9$Q\u0001\u0014=C\u00025C\u0001b \u0001\u0012\u0002\u0013\u0005\u0011\u0011A\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00134+\u0011\t\u0019!a\u0002\u0016\u0005\u0005\u0015!F\u0001 o\t\u0015aeP1\u0001N\u0011%\tY\u0001AA\u0001\n\u0003\ni!A\u0007qe>$Wo\u0019;Qe\u00164\u0017\u000e_\u000b\u0003\u0003\u001f\u0001B!!\u0005\u0002\u001c5\u0011\u00111\u0003\u0006\u0005\u0003+\t9\"\u0001\u0003mC:<'BAA\r\u0003\u0011Q\u0017M^1\n\u0007\r\n\u0019\u0002C\u0005\u0002 \u0001\t\t\u0011\"\u0001\u0002\"\u0005a\u0001O]8ek\u000e$\u0018I]5usV\u0011\u00111\u0005\t\u0004\u0015\u0005\u0015\u0012bAA\u0014\u0017\t\u0019\u0011J\u001c;\t\u0013\u0005-\u0002!!A\u0005\u0002\u00055\u0012A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0004K\u0005=\u0002BCA\u0019\u0003S\t\t\u00111\u0001\u0002$\u0005\u0019\u0001\u0010J\u0019\t\u0013\u0005U\u0002!!A\u0005B\u0005]\u0012a\u00049s_\u0012,8\r^%uKJ\fGo\u001c:\u0016\u0005\u0005e\u0002#BA\u001e\u0003\u0003*SBAA\u001f\u0015\r\tydC\u0001\u000bG>dG.Z2uS>t\u0017\u0002BA\"\u0003{\u0011\u0001\"\u0013;fe\u0006$xN\u001d\u0005\n\u0003\u000f\u0002\u0011\u0011!C\u0001\u0003\u0013\n\u0001bY1o\u000bF,\u0018\r\u001c\u000b\u0004}\u0005-\u0003\"CA\u0019\u0003\u000b\n\t\u00111\u0001&\u0011%\ty\u0005AA\u0001\n\u0003\n\t&\u0001\u0005iCND7i\u001c3f)\t\t\u0019\u0003C\u0005\u0002V\u0001\t\t\u0011\"\u0011\u0002X\u0005AAo\\*ue&tw\r\u0006\u0002\u0002\u0010!I\u00111\f\u0001\u0002\u0002\u0013\u0005\u0013QL\u0001\u0007KF,\u0018\r\\:\u0015\u0007y\ny\u0006C\u0005\u00022\u0005e\u0013\u0011!a\u0001K\u001d9\u00111\r\u0002\t\u0002\u0005\u0015\u0014a\u0004$jK2$7+\u001a:jC2L'0\u001a:\u0011\u0007M\n9G\u0002\u0004\u0002\u0005!\u0005\u0011\u0011N\n\u0005\u0003OJ!\u0003C\u0004U\u0003O\"\t!!\u001c\u0015\u0005\u0005\u0015\u0004\u0002CA9\u0003O\"\t!a\u001d\u0002\u0015I,g.Y7f\rJ|W\u000eF\u00031\u0003k\nI\bC\u0004\u0002x\u0005=\u0004\u0019\u0001\u0010\u0002\t9\fW.\u001a\u0005\b\u0003w\ny\u00071\u0001\u001f\u0003\u001dqWm\u001e(b[\u0016D\u0001\"a \u0002h\u0011\u0005\u0011\u0011Q\u0001\u0007S\u001etwN]3\u0015\u0007a\t\u0019\tC\u0004\u0002x\u0005u\u0004\u0019\u0001\u0010\t\u0011\u0005\u001d\u0015q\rC\u0001\u0003\u0013\u000b\u0001B]3oC6,Gk\u001c\u000b\u00061\u0005-\u0015Q\u0012\u0005\b\u0003o\n)\t1\u0001\u001f\u0011\u001d\tY(!\"A\u0002yA!\"!%\u0002h\u0005\u0005I\u0011QAJ\u0003\u0015\t\u0007\u000f\u001d7z+\u0011\t)*!(\u0015\u0011\u0005]\u00151UAS\u0003O#B!!'\u0002 B!1\u0007AAN!\rQ\u0015Q\u0014\u0003\u0007\u0019\u0006=%\u0019A'\t\u000f\u0011\u000by\tq\u0001\u0002\"B!qdRAN\u0011!1\u0012q\u0012I\u0001\u0002\u0004A\u0002\u0002\u0003\u0018\u0002\u0010B\u0005\t\u0019\u0001\u0019\t\u0011q\ny\t%AA\u0002yB!\"a+\u0002h\u0005\u0005I\u0011QAW\u0003\u001d)h.\u00199qYf,B!a,\u0002BR!\u0011\u0011WA]!\u0011Q\u0011&a-\u0011\r)\t)\f\u0007\u0019?\u0013\r\t9l\u0003\u0002\u0007)V\u0004H.Z\u001a\t\u0015\u0005m\u0016\u0011VA\u0001\u0002\u0004\ti,A\u0002yIA\u0002Ba\r\u0001\u0002@B\u0019!*!1\u0005\r1\u000bIK1\u0001N\u0011)\t)-a\u001a\u0012\u0002\u0013\u0005\u0011qY\u0001\u001cI1,7o]5oSR$sM]3bi\u0016\u0014H\u0005Z3gCVdG\u000fJ\u0019\u0016\u00071\fI\r\u0002\u0004M\u0003\u0007\u0014\r!\u0014\u0005\u000b\u0003\u001b\f9'%A\u0005\u0002\u0005=\u0017a\u0007\u0013mKN\u001c\u0018N\\5uI\u001d\u0014X-\u0019;fe\u0012\"WMZ1vYR$#'F\u0002|\u0003#$a\u0001TAf\u0005\u0004i\u0005BCAk\u0003O\n\n\u0011\"\u0001\u0002X\u0006YB\u0005\\3tg&t\u0017\u000e\u001e\u0013he\u0016\fG/\u001a:%I\u00164\u0017-\u001e7uIM*B!a\u0001\u0002Z\u00121A*a5C\u00025C!\"!8\u0002hE\u0005I\u0011AAp\u0003=\t\u0007\u000f\u001d7zI\u0011,g-Y;mi\u0012\nTc\u00017\u0002b\u00121A*a7C\u00025C!\"!:\u0002hE\u0005I\u0011AAt\u0003=\t\u0007\u000f\u001d7zI\u0011,g-Y;mi\u0012\u0012TcA>\u0002j\u00121A*a9C\u00025C!\"!<\u0002hE\u0005I\u0011AAx\u0003=\t\u0007\u000f\u001d7zI\u0011,g-Y;mi\u0012\u001aT\u0003BA\u0002\u0003c$a\u0001TAv\u0005\u0004i\u0005BCA{\u0003O\n\t\u0011\"\u0003\u0002x\u0006Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\t\tI\u0010\u0005\u0003\u0002\u0012\u0005m\u0018\u0002BA\u007f\u0003'\u0011aa\u00142kK\u000e$\b")
public class FieldSerializer<A> implements Product, Serializable
{
    private final PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> serializer;
    private final PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> deserializer;
    private final boolean includeLazyVal;
    private final Manifest<A> mf;
    
    public static <A> boolean apply$default$3() {
        return FieldSerializer$.MODULE$.apply$default$3();
    }
    
    public static <A> PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> apply$default$2() {
        return FieldSerializer$.MODULE$.apply$default$2();
    }
    
    public static <A> PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> apply$default$1() {
        return FieldSerializer$.MODULE$.apply$default$1();
    }
    
    public static <A> boolean $lessinit$greater$default$3() {
        return FieldSerializer$.MODULE$.$lessinit$greater$default$3();
    }
    
    public static <A> PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> $lessinit$greater$default$2() {
        return FieldSerializer$.MODULE$.$lessinit$greater$default$2();
    }
    
    public static <A> PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> $lessinit$greater$default$1() {
        return FieldSerializer$.MODULE$.$lessinit$greater$default$1();
    }
    
    public static <A> Option<Tuple3<PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>>, PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>>, Object>> unapply(final FieldSerializer<A> x$0) {
        return FieldSerializer$.MODULE$.unapply(x$0);
    }
    
    public static <A> FieldSerializer<A> apply(final PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> serializer, final PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> deserializer, final boolean includeLazyVal, final Manifest<A> mf) {
        return FieldSerializer$.MODULE$.apply(serializer, deserializer, includeLazyVal, mf);
    }
    
    public static PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> renameTo(final String name, final String newName) {
        return FieldSerializer$.MODULE$.renameTo(name, newName);
    }
    
    public static PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> ignore(final String name) {
        return FieldSerializer$.MODULE$.ignore(name);
    }
    
    public static PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> renameFrom(final String name, final String newName) {
        return FieldSerializer$.MODULE$.renameFrom(name, newName);
    }
    
    public PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> serializer() {
        return this.serializer;
    }
    
    public PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> deserializer() {
        return this.deserializer;
    }
    
    public boolean includeLazyVal() {
        return this.includeLazyVal;
    }
    
    public Manifest<A> mf() {
        return this.mf;
    }
    
    public <A> FieldSerializer<A> copy(final PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> serializer, final PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> deserializer, final boolean includeLazyVal, final Manifest<A> mf) {
        return new FieldSerializer<A>(serializer, deserializer, includeLazyVal, mf);
    }
    
    public <A> PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> copy$default$1() {
        return this.serializer();
    }
    
    public <A> PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> copy$default$2() {
        return this.deserializer();
    }
    
    public <A> boolean copy$default$3() {
        return this.includeLazyVal();
    }
    
    public String productPrefix() {
        return "FieldSerializer";
    }
    
    public int productArity() {
        return 3;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 2: {
                o = BoxesRunTime.boxToBoolean(this.includeLazyVal());
                break;
            }
            case 1: {
                o = this.deserializer();
                break;
            }
            case 0: {
                o = this.serializer();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof FieldSerializer;
    }
    
    @Override
    public int hashCode() {
        return Statics.finalizeHash(Statics.mix(Statics.mix(Statics.mix(-889275714, Statics.anyHash((Object)this.serializer())), Statics.anyHash((Object)this.deserializer())), this.includeLazyVal() ? 1231 : 1237), 3);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof FieldSerializer) {
                final FieldSerializer fieldSerializer = (FieldSerializer)x$1;
                final PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> serializer = this.serializer();
                final PartialFunction serializer2 = fieldSerializer.serializer();
                boolean b = false;
                Label_0121: {
                    Label_0120: {
                        if (serializer == null) {
                            if (serializer2 != null) {
                                break Label_0120;
                            }
                        }
                        else if (!serializer.equals(serializer2)) {
                            break Label_0120;
                        }
                        final PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> deserializer = this.deserializer();
                        final PartialFunction deserializer2 = fieldSerializer.deserializer();
                        if (deserializer == null) {
                            if (deserializer2 != null) {
                                break Label_0120;
                            }
                        }
                        else if (!deserializer.equals(deserializer2)) {
                            break Label_0120;
                        }
                        if (this.includeLazyVal() == fieldSerializer.includeLazyVal() && fieldSerializer.canEqual(this)) {
                            b = true;
                            break Label_0121;
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
    
    public FieldSerializer(final PartialFunction<Tuple2<String, Object>, Option<Tuple2<String, Object>>> serializer, final PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> deserializer, final boolean includeLazyVal, final Manifest<A> mf) {
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.includeLazyVal = includeLazyVal;
        this.mf = mf;
        Product$class.$init$((Product)this);
    }
}
