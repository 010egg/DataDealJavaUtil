// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Product;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple5;
import scala.Option;
import scala.collection.Seq;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u0005f\u0001B\u0001\u0003\u0001&\u00111cU5oO2,Go\u001c8EKN\u001c'/\u001b9u_JT!a\u0001\u0003\u0002\u000fI,g\r\\3di*\u0011QAB\u0001\u0007UN|g\u000eN:\u000b\u0003\u001d\t1a\u001c:h\u0007\u0001\u0019B\u0001\u0001\u0006\u000f)A\u00111\u0002D\u0007\u0002\u0005%\u0011QB\u0001\u0002\u000b\t\u0016\u001c8M]5qi>\u0014\bCA\b\u0013\u001b\u0005\u0001\"\"A\t\u0002\u000bM\u001c\u0017\r\\1\n\u0005M\u0001\"a\u0002)s_\u0012,8\r\u001e\t\u0003\u001fUI!A\u0006\t\u0003\u0019M+'/[1mSj\f'\r\\3\t\u0011a\u0001!Q3A\u0005\u0002e\t!b]5na2,g*Y7f+\u0005Q\u0002CA\u000e\u001f\u001d\tyA$\u0003\u0002\u001e!\u00051\u0001K]3eK\u001aL!a\b\u0011\u0003\rM#(/\u001b8h\u0015\ti\u0002\u0003\u0003\u0005#\u0001\tE\t\u0015!\u0003\u001b\u0003-\u0019\u0018.\u001c9mK:\u000bW.\u001a\u0011\t\u0011\u0011\u0002!Q3A\u0005\u0002e\t\u0001BZ;mY:\u000bW.\u001a\u0005\tM\u0001\u0011\t\u0012)A\u00055\u0005Ia-\u001e7m\u001d\u0006lW\r\t\u0005\tQ\u0001\u0011)\u001a!C\u0001S\u00059QM]1tkJ,W#\u0001\u0016\u0011\u0005-Y\u0013B\u0001\u0017\u0003\u0005%\u00196-\u00197b)f\u0004X\r\u0003\u0005/\u0001\tE\t\u0015!\u0003+\u0003!)'/Y:ve\u0016\u0004\u0003\u0002\u0003\u0019\u0001\u0005+\u0007I\u0011A\u0019\u0002\u0011%t7\u000f^1oG\u0016,\u0012A\r\t\u0003\u001fMJ!\u0001\u000e\t\u0003\r\u0005s\u0017PU3g\u0011!1\u0004A!E!\u0002\u0013\u0011\u0014!C5ogR\fgnY3!\u0011!A\u0004A!f\u0001\n\u0003I\u0014A\u00039s_B,'\u000f^5fgV\t!\bE\u0002<\u0007\u001as!\u0001P!\u000f\u0005u\u0002U\"\u0001 \u000b\u0005}B\u0011A\u0002\u001fs_>$h(C\u0001\u0012\u0013\t\u0011\u0005#A\u0004qC\u000e\\\u0017mZ3\n\u0005\u0011+%aA*fc*\u0011!\t\u0005\t\u0003\u0017\u001dK!\u0001\u0013\u0002\u0003%A\u0013x\u000e]3sif$Um]2sSB$xN\u001d\u0005\t\u0015\u0002\u0011\t\u0012)A\u0005u\u0005Y\u0001O]8qKJ$\u0018.Z:!\u0011\u0015a\u0005\u0001\"\u0001N\u0003\u0019a\u0014N\\5u}Q1aj\u0014)R%N\u0003\"a\u0003\u0001\t\u000baY\u0005\u0019\u0001\u000e\t\u000b\u0011Z\u0005\u0019\u0001\u000e\t\u000b!Z\u0005\u0019\u0001\u0016\t\u000bAZ\u0005\u0019\u0001\u001a\t\u000baZ\u0005\u0019\u0001\u001e\t\u000fU\u0003\u0011\u0011!C\u0001-\u0006!1m\u001c9z)\u0019qu\u000bW-[7\"9\u0001\u0004\u0016I\u0001\u0002\u0004Q\u0002b\u0002\u0013U!\u0003\u0005\rA\u0007\u0005\bQQ\u0003\n\u00111\u0001+\u0011\u001d\u0001D\u000b%AA\u0002IBq\u0001\u000f+\u0011\u0002\u0003\u0007!\bC\u0004^\u0001E\u0005I\u0011\u00010\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%cU\tqL\u000b\u0002\u001bA.\n\u0011\r\u0005\u0002cO6\t1M\u0003\u0002eK\u0006IQO\\2iK\u000e\\W\r\u001a\u0006\u0003MB\t!\"\u00198o_R\fG/[8o\u0013\tA7MA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016DqA\u001b\u0001\u0012\u0002\u0013\u0005a,\u0001\bd_BLH\u0005Z3gCVdG\u000f\n\u001a\t\u000f1\u0004\u0011\u0013!C\u0001[\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\u001aT#\u00018+\u0005)\u0002\u0007b\u00029\u0001#\u0003%\t!]\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00135+\u0005\u0011(F\u0001\u001aa\u0011\u001d!\b!%A\u0005\u0002U\fabY8qs\u0012\"WMZ1vYR$S'F\u0001wU\tQ\u0004\rC\u0004y\u0001\u0005\u0005I\u0011I=\u0002\u001bA\u0014x\u000eZ;diB\u0013XMZ5y+\u0005Q\bcA>\u0002\u00025\tAP\u0003\u0002~}\u0006!A.\u00198h\u0015\u0005y\u0018\u0001\u00026bm\u0006L!a\b?\t\u0013\u0005\u0015\u0001!!A\u0005\u0002\u0005\u001d\u0011\u0001\u00049s_\u0012,8\r^!sSRLXCAA\u0005!\ry\u00111B\u0005\u0004\u0003\u001b\u0001\"aA%oi\"I\u0011\u0011\u0003\u0001\u0002\u0002\u0013\u0005\u00111C\u0001\u000faJ|G-^2u\u000b2,W.\u001a8u)\u0011\t)\"a\u0007\u0011\u0007=\t9\"C\u0002\u0002\u001aA\u00111!\u00118z\u0011)\ti\"a\u0004\u0002\u0002\u0003\u0007\u0011\u0011B\u0001\u0004q\u0012\n\u0004\"CA\u0011\u0001\u0005\u0005I\u0011IA\u0012\u0003=\u0001(o\u001c3vGRLE/\u001a:bi>\u0014XCAA\u0013!\u0019\t9#!\f\u0002\u00165\u0011\u0011\u0011\u0006\u0006\u0004\u0003W\u0001\u0012AC2pY2,7\r^5p]&!\u0011qFA\u0015\u0005!IE/\u001a:bi>\u0014\b\"CA\u001a\u0001\u0005\u0005I\u0011AA\u001b\u0003!\u0019\u0017M\\#rk\u0006dG\u0003BA\u001c\u0003{\u00012aDA\u001d\u0013\r\tY\u0004\u0005\u0002\b\u0005>|G.Z1o\u0011)\ti\"!\r\u0002\u0002\u0003\u0007\u0011Q\u0003\u0005\n\u0003\u0003\u0002\u0011\u0011!C!\u0003\u0007\n\u0001\u0002[1tQ\u000e{G-\u001a\u000b\u0003\u0003\u0013A\u0011\"a\u0012\u0001\u0003\u0003%\t%!\u0013\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012A\u001f\u0005\n\u0003\u001b\u0002\u0011\u0011!C!\u0003\u001f\na!Z9vC2\u001cH\u0003BA\u001c\u0003#B!\"!\b\u0002L\u0005\u0005\t\u0019AA\u000b\u000f%\t)FAA\u0001\u0012\u0003\t9&A\nTS:<G.\u001a;p]\u0012+7o\u0019:jaR|'\u000fE\u0002\f\u000332\u0001\"\u0001\u0002\u0002\u0002#\u0005\u00111L\n\u0006\u00033\ni\u0006\u0006\t\u000b\u0003?\n)G\u0007\u000e+eirUBAA1\u0015\r\t\u0019\u0007E\u0001\beVtG/[7f\u0013\u0011\t9'!\u0019\u0003#\u0005\u00137\u000f\u001e:bGR4UO\\2uS>tW\u0007C\u0004M\u00033\"\t!a\u001b\u0015\u0005\u0005]\u0003BCA$\u00033\n\t\u0011\"\u0012\u0002J!Q\u0011\u0011OA-\u0003\u0003%\t)a\u001d\u0002\u000b\u0005\u0004\b\u000f\\=\u0015\u00179\u000b)(a\u001e\u0002z\u0005m\u0014Q\u0010\u0005\u00071\u0005=\u0004\u0019\u0001\u000e\t\r\u0011\ny\u00071\u0001\u001b\u0011\u0019A\u0013q\u000ea\u0001U!1\u0001'a\u001cA\u0002IBa\u0001OA8\u0001\u0004Q\u0004BCAA\u00033\n\t\u0011\"!\u0002\u0004\u00069QO\\1qa2LH\u0003BAC\u0003#\u0003RaDAD\u0003\u0017K1!!#\u0011\u0005\u0019y\u0005\u000f^5p]BAq\"!$\u001b5)\u0012$(C\u0002\u0002\u0010B\u0011a\u0001V;qY\u0016,\u0004\"CAJ\u0003\u007f\n\t\u00111\u0001O\u0003\rAH\u0005\r\u0005\u000b\u0003/\u000bI&!A\u0005\n\u0005e\u0015a\u0003:fC\u0012\u0014Vm]8mm\u0016$\"!a'\u0011\u0007m\fi*C\u0002\u0002 r\u0014aa\u00142kK\u000e$\b")
public class SingletonDescriptor extends Descriptor
{
    private final String simpleName;
    private final String fullName;
    private final ScalaType erasure;
    private final Object instance;
    private final Seq<PropertyDescriptor> properties;
    
    public static Option<Tuple5<String, String, ScalaType, Object, Seq<PropertyDescriptor>>> unapply(final SingletonDescriptor x$0) {
        return SingletonDescriptor$.MODULE$.unapply(x$0);
    }
    
    public static SingletonDescriptor apply(final String simpleName, final String fullName, final ScalaType erasure, final Object instance, final Seq<PropertyDescriptor> properties) {
        return SingletonDescriptor$.MODULE$.apply(simpleName, fullName, erasure, instance, properties);
    }
    
    public static Function1<Tuple5<String, String, ScalaType, Object, Seq<PropertyDescriptor>>, SingletonDescriptor> tupled() {
        return (Function1<Tuple5<String, String, ScalaType, Object, Seq<PropertyDescriptor>>, SingletonDescriptor>)SingletonDescriptor$.MODULE$.tupled();
    }
    
    public static Function1<String, Function1<String, Function1<ScalaType, Function1<Object, Function1<Seq<PropertyDescriptor>, SingletonDescriptor>>>>> curried() {
        return (Function1<String, Function1<String, Function1<ScalaType, Function1<Object, Function1<Seq<PropertyDescriptor>, SingletonDescriptor>>>>>)SingletonDescriptor$.MODULE$.curried();
    }
    
    public String simpleName() {
        return this.simpleName;
    }
    
    public String fullName() {
        return this.fullName;
    }
    
    public ScalaType erasure() {
        return this.erasure;
    }
    
    public Object instance() {
        return this.instance;
    }
    
    public Seq<PropertyDescriptor> properties() {
        return this.properties;
    }
    
    public SingletonDescriptor copy(final String simpleName, final String fullName, final ScalaType erasure, final Object instance, final Seq<PropertyDescriptor> properties) {
        return new SingletonDescriptor(simpleName, fullName, erasure, instance, properties);
    }
    
    public String copy$default$1() {
        return this.simpleName();
    }
    
    public String copy$default$2() {
        return this.fullName();
    }
    
    public ScalaType copy$default$3() {
        return this.erasure();
    }
    
    public Object copy$default$4() {
        return this.instance();
    }
    
    public Seq<PropertyDescriptor> copy$default$5() {
        return this.properties();
    }
    
    @Override
    public String productPrefix() {
        return "SingletonDescriptor";
    }
    
    public int productArity() {
        return 5;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 4: {
                o = this.properties();
                break;
            }
            case 3: {
                o = this.instance();
                break;
            }
            case 2: {
                o = this.erasure();
                break;
            }
            case 1: {
                o = this.fullName();
                break;
            }
            case 0: {
                o = this.simpleName();
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
        return x$1 instanceof SingletonDescriptor;
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
            if (x$1 instanceof SingletonDescriptor) {
                final SingletonDescriptor singletonDescriptor = (SingletonDescriptor)x$1;
                final String simpleName = this.simpleName();
                final String simpleName2 = singletonDescriptor.simpleName();
                boolean b = false;
                Label_0188: {
                    Label_0187: {
                        if (simpleName == null) {
                            if (simpleName2 != null) {
                                break Label_0187;
                            }
                        }
                        else if (!simpleName.equals(simpleName2)) {
                            break Label_0187;
                        }
                        final String fullName = this.fullName();
                        final String fullName2 = singletonDescriptor.fullName();
                        if (fullName == null) {
                            if (fullName2 != null) {
                                break Label_0187;
                            }
                        }
                        else if (!fullName.equals(fullName2)) {
                            break Label_0187;
                        }
                        final ScalaType erasure = this.erasure();
                        final ScalaType erasure2 = singletonDescriptor.erasure();
                        if (erasure == null) {
                            if (erasure2 != null) {
                                break Label_0187;
                            }
                        }
                        else if (!erasure.equals(erasure2)) {
                            break Label_0187;
                        }
                        if (BoxesRunTime.equals(this.instance(), singletonDescriptor.instance())) {
                            final Seq<PropertyDescriptor> properties = this.properties();
                            final Seq<PropertyDescriptor> properties2 = singletonDescriptor.properties();
                            if (properties == null) {
                                if (properties2 != null) {
                                    break Label_0187;
                                }
                            }
                            else if (!properties.equals(properties2)) {
                                break Label_0187;
                            }
                            if (singletonDescriptor.canEqual(this)) {
                                b = true;
                                break Label_0188;
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
    
    public SingletonDescriptor(final String simpleName, final String fullName, final ScalaType erasure, final Object instance, final Seq<PropertyDescriptor> properties) {
        this.simpleName = simpleName;
        this.fullName = fullName;
        this.erasure = erasure;
        this.instance = instance;
        this.properties = properties;
    }
}
