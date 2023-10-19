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
import scala.Tuple7;
import scala.Option;
import scala.collection.Seq;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005Mf\u0001B\u0001\u0003\u0001.\u0011qb\u00117bgN4\u0015\u000e\\3IK\u0006$WM\u001d\u0006\u0003\u0007\u0011\t\u0001b]2bY\u0006\u001c\u0018n\u001a\u0006\u0003\u000b\u0019\taa]2bY\u0006\u0004(BA\u0004\t\u0003\u0019Q7o\u001c85g*\t\u0011\"A\u0002pe\u001e\u001c\u0001a\u0005\u0003\u0001\u0019I)\u0002CA\u0007\u0011\u001b\u0005q!\"A\b\u0002\u000bM\u001c\u0017\r\\1\n\u0005Eq!AB!osJ+g\r\u0005\u0002\u000e'%\u0011AC\u0004\u0002\b!J|G-^2u!\tia#\u0003\u0002\u0018\u001d\ta1+\u001a:jC2L'0\u00192mK\"A\u0011\u0004\u0001BK\u0002\u0013\u0005!$A\u0003nS:|'/F\u0001\u001c!\tiA$\u0003\u0002\u001e\u001d\t\u0019\u0011J\u001c;\t\u0011}\u0001!\u0011#Q\u0001\nm\ta!\\5o_J\u0004\u0003\u0002C\u0011\u0001\u0005+\u0007I\u0011\u0001\u000e\u0002\u000b5\f'n\u001c:\t\u0011\r\u0002!\u0011#Q\u0001\nm\ta!\\1k_J\u0004\u0003\u0002C\u0013\u0001\u0005+\u0007I\u0011\u0001\u0014\u0002\u0013\r|gn\u001d;b]R\u001cX#A\u0014\u0011\u0005!JS\"\u0001\u0002\n\u0005)\u0012!\u0001D\"p]N$\u0018M\u001c;Q_>d\u0007\u0002\u0003\u0017\u0001\u0005#\u0005\u000b\u0011B\u0014\u0002\u0015\r|gn\u001d;b]R\u001c\b\u0005\u0003\u0005/\u0001\tU\r\u0011\"\u0001\u001b\u0003\u00151G.Y4t\u0011!\u0001\u0004A!E!\u0002\u0013Y\u0012A\u00024mC\u001e\u001c\b\u0005\u0003\u00053\u0001\tU\r\u0011\"\u0001\u001b\u0003)\u0019G.Y:t\u0013:$W\r\u001f\u0005\ti\u0001\u0011\t\u0012)A\u00057\u0005Y1\r\\1tg&sG-\u001a=!\u0011!1\u0004A!f\u0001\n\u0003Q\u0012aD:va\u0016\u00148\t\\1tg&sG-\u001a=\t\u0011a\u0002!\u0011#Q\u0001\nm\t\u0001c];qKJ\u001cE.Y:t\u0013:$W\r\u001f\u0011\t\u0011i\u0002!Q3A\u0005\u0002m\n!\"\u001b8uKJ4\u0017mY3t+\u0005a\u0004cA\u001fF79\u0011ah\u0011\b\u0003\u007f\tk\u0011\u0001\u0011\u0006\u0003\u0003*\ta\u0001\u0010:p_Rt\u0014\"A\b\n\u0005\u0011s\u0011a\u00029bG.\fw-Z\u0005\u0003\r\u001e\u00131aU3r\u0015\t!e\u0002\u0003\u0005J\u0001\tE\t\u0015!\u0003=\u0003-Ig\u000e^3sM\u0006\u001cWm\u001d\u0011\t\u000b-\u0003A\u0011\u0001'\u0002\rqJg.\u001b;?)!iej\u0014)R%N#\u0006C\u0001\u0015\u0001\u0011\u0015I\"\n1\u0001\u001c\u0011\u0015\t#\n1\u0001\u001c\u0011\u0015)#\n1\u0001(\u0011\u0015q#\n1\u0001\u001c\u0011\u0015\u0011$\n1\u0001\u001c\u0011\u00151$\n1\u0001\u001c\u0011\u0015Q$\n1\u0001=\u0011\u00151\u0006\u0001\"\u0001X\u0003!\u0019wN\\:uC:$HC\u0001-\\!\ti\u0011,\u0003\u0002[\u001d\t\u0019\u0011I\\=\t\u000bq+\u0006\u0019A\u000e\u0002\u000b%tG-\u001a=\t\u000fy\u0003\u0011\u0011!C\u0001?\u0006!1m\u001c9z)!i\u0005-\u00192dI\u00164\u0007bB\r^!\u0003\u0005\ra\u0007\u0005\bCu\u0003\n\u00111\u0001\u001c\u0011\u001d)S\f%AA\u0002\u001dBqAL/\u0011\u0002\u0003\u00071\u0004C\u00043;B\u0005\t\u0019A\u000e\t\u000fYj\u0006\u0013!a\u00017!9!(\u0018I\u0001\u0002\u0004a\u0004b\u00025\u0001#\u0003%\t![\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00132+\u0005Q'FA\u000elW\u0005a\u0007CA7s\u001b\u0005q'BA8q\u0003%)hn\u00195fG.,GM\u0003\u0002r\u001d\u0005Q\u0011M\u001c8pi\u0006$\u0018n\u001c8\n\u0005Mt'!E;oG\",7m[3e-\u0006\u0014\u0018.\u00198dK\"9Q\u000fAI\u0001\n\u0003I\u0017AD2paf$C-\u001a4bk2$HE\r\u0005\bo\u0002\t\n\u0011\"\u0001y\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIM*\u0012!\u001f\u0016\u0003O-Dqa\u001f\u0001\u0012\u0002\u0013\u0005\u0011.\u0001\bd_BLH\u0005Z3gCVdG\u000f\n\u001b\t\u000fu\u0004\u0011\u0013!C\u0001S\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012*\u0004bB@\u0001#\u0003%\t![\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00137\u0011%\t\u0019\u0001AI\u0001\n\u0003\t)!\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u001c\u0016\u0005\u0005\u001d!F\u0001\u001fl\u0011%\tY\u0001AA\u0001\n\u0003\ni!A\u0007qe>$Wo\u0019;Qe\u00164\u0017\u000e_\u000b\u0003\u0003\u001f\u0001B!!\u0005\u0002\u001c5\u0011\u00111\u0003\u0006\u0005\u0003+\t9\"\u0001\u0003mC:<'BAA\r\u0003\u0011Q\u0017M^1\n\t\u0005u\u00111\u0003\u0002\u0007'R\u0014\u0018N\\4\t\u0011\u0005\u0005\u0002!!A\u0005\u0002i\tA\u0002\u001d:pIV\u001cG/\u0011:jifD\u0011\"!\n\u0001\u0003\u0003%\t!a\n\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u0019\u0001,!\u000b\t\u0013\u0005-\u00121EA\u0001\u0002\u0004Y\u0012a\u0001=%c!I\u0011q\u0006\u0001\u0002\u0002\u0013\u0005\u0013\u0011G\u0001\u0010aJ|G-^2u\u0013R,'/\u0019;peV\u0011\u00111\u0007\t\u0006\u0003k\tY\u0004W\u0007\u0003\u0003oQ1!!\u000f\u000f\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0005\u0003{\t9D\u0001\u0005Ji\u0016\u0014\u0018\r^8s\u0011%\t\t\u0005AA\u0001\n\u0003\t\u0019%\u0001\u0005dC:,\u0015/^1m)\u0011\t)%a\u0013\u0011\u00075\t9%C\u0002\u0002J9\u0011qAQ8pY\u0016\fg\u000eC\u0005\u0002,\u0005}\u0012\u0011!a\u00011\"I\u0011q\n\u0001\u0002\u0002\u0013\u0005\u0013\u0011K\u0001\tQ\u0006\u001c\bnQ8eKR\t1\u0004C\u0005\u0002V\u0001\t\t\u0011\"\u0011\u0002X\u0005AAo\\*ue&tw\r\u0006\u0002\u0002\u0010!I\u00111\f\u0001\u0002\u0002\u0013\u0005\u0013QL\u0001\u0007KF,\u0018\r\\:\u0015\t\u0005\u0015\u0013q\f\u0005\n\u0003W\tI&!AA\u0002a;\u0011\"a\u0019\u0003\u0003\u0003E\t!!\u001a\u0002\u001f\rc\u0017m]:GS2,\u0007*Z1eKJ\u00042\u0001KA4\r!\t!!!A\t\u0002\u0005%4#BA4\u0003W*\u0002\u0003DA7\u0003gZ2dJ\u000e\u001c7qjUBAA8\u0015\r\t\tHD\u0001\beVtG/[7f\u0013\u0011\t)(a\u001c\u0003#\u0005\u00137\u000f\u001e:bGR4UO\\2uS>tw\u0007C\u0004L\u0003O\"\t!!\u001f\u0015\u0005\u0005\u0015\u0004BCA+\u0003O\n\t\u0011\"\u0012\u0002X!Q\u0011qPA4\u0003\u0003%\t)!!\u0002\u000b\u0005\u0004\b\u000f\\=\u0015\u001f5\u000b\u0019)!\"\u0002\b\u0006%\u00151RAG\u0003\u001fCa!GA?\u0001\u0004Y\u0002BB\u0011\u0002~\u0001\u00071\u0004\u0003\u0004&\u0003{\u0002\ra\n\u0005\u0007]\u0005u\u0004\u0019A\u000e\t\rI\ni\b1\u0001\u001c\u0011\u00191\u0014Q\u0010a\u00017!1!(! A\u0002qB!\"a%\u0002h\u0005\u0005I\u0011QAK\u0003\u001d)h.\u00199qYf$B!a&\u0002$B)Q\"!'\u0002\u001e&\u0019\u00111\u0014\b\u0003\r=\u0003H/[8o!)i\u0011qT\u000e\u001cOmY2\u0004P\u0005\u0004\u0003Cs!A\u0002+va2,w\u0007C\u0005\u0002&\u0006E\u0015\u0011!a\u0001\u001b\u0006\u0019\u0001\u0010\n\u0019\t\u0015\u0005%\u0016qMA\u0001\n\u0013\tY+A\u0006sK\u0006$'+Z:pYZ,GCAAW!\u0011\t\t\"a,\n\t\u0005E\u00161\u0003\u0002\u0007\u001f\nTWm\u0019;")
public class ClassFileHeader implements Product, Serializable
{
    private final int minor;
    private final int major;
    private final ConstantPool constants;
    private final int flags;
    private final int classIndex;
    private final int superClassIndex;
    private final Seq<Object> interfaces;
    
    public static Option<Tuple7<Object, Object, ConstantPool, Object, Object, Object, Seq<Object>>> unapply(final ClassFileHeader x$0) {
        return ClassFileHeader$.MODULE$.unapply(x$0);
    }
    
    public static ClassFileHeader apply(final int minor, final int major, final ConstantPool constants, final int flags, final int classIndex, final int superClassIndex, final Seq<Object> interfaces) {
        return ClassFileHeader$.MODULE$.apply(minor, major, constants, flags, classIndex, superClassIndex, interfaces);
    }
    
    public static Function1<Tuple7<Object, Object, ConstantPool, Object, Object, Object, Seq<Object>>, ClassFileHeader> tupled() {
        return (Function1<Tuple7<Object, Object, ConstantPool, Object, Object, Object, Seq<Object>>, ClassFileHeader>)ClassFileHeader$.MODULE$.tupled();
    }
    
    public static Function1 curried() {
        return ClassFileHeader$.MODULE$.curried();
    }
    
    public int minor() {
        return this.minor;
    }
    
    public int major() {
        return this.major;
    }
    
    public ConstantPool constants() {
        return this.constants;
    }
    
    public int flags() {
        return this.flags;
    }
    
    public int classIndex() {
        return this.classIndex;
    }
    
    public int superClassIndex() {
        return this.superClassIndex;
    }
    
    public Seq<Object> interfaces() {
        return this.interfaces;
    }
    
    public Object constant(final int index) {
        return this.constants().apply(index);
    }
    
    public ClassFileHeader copy(final int minor, final int major, final ConstantPool constants, final int flags, final int classIndex, final int superClassIndex, final Seq<Object> interfaces) {
        return new ClassFileHeader(minor, major, constants, flags, classIndex, superClassIndex, interfaces);
    }
    
    public int copy$default$1() {
        return this.minor();
    }
    
    public int copy$default$2() {
        return this.major();
    }
    
    public ConstantPool copy$default$3() {
        return this.constants();
    }
    
    public int copy$default$4() {
        return this.flags();
    }
    
    public int copy$default$5() {
        return this.classIndex();
    }
    
    public int copy$default$6() {
        return this.superClassIndex();
    }
    
    public Seq<Object> copy$default$7() {
        return this.interfaces();
    }
    
    public String productPrefix() {
        return "ClassFileHeader";
    }
    
    public int productArity() {
        return 7;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 6: {
                o = this.interfaces();
                break;
            }
            case 5: {
                o = BoxesRunTime.boxToInteger(this.superClassIndex());
                break;
            }
            case 4: {
                o = BoxesRunTime.boxToInteger(this.classIndex());
                break;
            }
            case 3: {
                o = BoxesRunTime.boxToInteger(this.flags());
                break;
            }
            case 2: {
                o = this.constants();
                break;
            }
            case 1: {
                o = BoxesRunTime.boxToInteger(this.major());
                break;
            }
            case 0: {
                o = BoxesRunTime.boxToInteger(this.minor());
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ClassFileHeader;
    }
    
    @Override
    public int hashCode() {
        return Statics.finalizeHash(Statics.mix(Statics.mix(Statics.mix(Statics.mix(Statics.mix(Statics.mix(Statics.mix(-889275714, this.minor()), this.major()), Statics.anyHash((Object)this.constants())), this.flags()), this.classIndex()), this.superClassIndex()), Statics.anyHash((Object)this.interfaces())), 7);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ClassFileHeader) {
                final ClassFileHeader classFileHeader = (ClassFileHeader)x$1;
                boolean b = false;
                Label_0169: {
                    Label_0168: {
                        if (this.minor() == classFileHeader.minor() && this.major() == classFileHeader.major()) {
                            final ConstantPool constants = this.constants();
                            final ConstantPool constants2 = classFileHeader.constants();
                            if (constants == null) {
                                if (constants2 != null) {
                                    break Label_0168;
                                }
                            }
                            else if (!constants.equals(constants2)) {
                                break Label_0168;
                            }
                            if (this.flags() == classFileHeader.flags() && this.classIndex() == classFileHeader.classIndex() && this.superClassIndex() == classFileHeader.superClassIndex()) {
                                final Seq<Object> interfaces = this.interfaces();
                                final Seq<Object> interfaces2 = classFileHeader.interfaces();
                                if (interfaces == null) {
                                    if (interfaces2 != null) {
                                        break Label_0168;
                                    }
                                }
                                else if (!interfaces.equals(interfaces2)) {
                                    break Label_0168;
                                }
                                if (classFileHeader.canEqual(this)) {
                                    b = true;
                                    break Label_0169;
                                }
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
    
    public ClassFileHeader(final int minor, final int major, final ConstantPool constants, final int flags, final int classIndex, final int superClassIndex, final Seq<Object> interfaces) {
        this.minor = minor;
        this.major = major;
        this.constants = constants;
        this.flags = flags;
        this.classIndex = classIndex;
        this.superClassIndex = superClassIndex;
        this.interfaces = interfaces;
        Product$class.$init$((Product)this);
    }
}
