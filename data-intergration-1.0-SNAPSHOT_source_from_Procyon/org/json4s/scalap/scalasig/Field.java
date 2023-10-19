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

@ScalaSignature(bytes = "\u0006\u0001\u00055d\u0001B\u0001\u0003\u0001.\u0011QAR5fY\u0012T!a\u0001\u0003\u0002\u0011M\u001c\u0017\r\\1tS\u001eT!!\u0002\u0004\u0002\rM\u001c\u0017\r\\1q\u0015\t9\u0001\"\u0001\u0004kg>tGg\u001d\u0006\u0002\u0013\u0005\u0019qN]4\u0004\u0001M!\u0001\u0001\u0004\n\u0016!\ti\u0001#D\u0001\u000f\u0015\u0005y\u0011!B:dC2\f\u0017BA\t\u000f\u0005\u0019\te.\u001f*fMB\u0011QbE\u0005\u0003)9\u0011q\u0001\u0015:pIV\u001cG\u000f\u0005\u0002\u000e-%\u0011qC\u0004\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\t3\u0001\u0011)\u001a!C\u00015\u0005)a\r\\1hgV\t1\u0004\u0005\u0002\u000e9%\u0011QD\u0004\u0002\u0004\u0013:$\b\u0002C\u0010\u0001\u0005#\u0005\u000b\u0011B\u000e\u0002\r\u0019d\u0017mZ:!\u0011!\t\u0003A!f\u0001\n\u0003Q\u0012!\u00038b[\u0016Le\u000eZ3y\u0011!\u0019\u0003A!E!\u0002\u0013Y\u0012A\u00038b[\u0016Le\u000eZ3yA!AQ\u0005\u0001BK\u0002\u0013\u0005!$A\beKN\u001c'/\u001b9u_JLe\u000eZ3y\u0011!9\u0003A!E!\u0002\u0013Y\u0012\u0001\u00053fg\u000e\u0014\u0018\u000e\u001d;pe&sG-\u001a=!\u0011!I\u0003A!f\u0001\n\u0003Q\u0013AC1uiJL'-\u001e;fgV\t1\u0006E\u0002-i]r!!\f\u001a\u000f\u00059\nT\"A\u0018\u000b\u0005AR\u0011A\u0002\u001fs_>$h(C\u0001\u0010\u0013\t\u0019d\"A\u0004qC\u000e\\\u0017mZ3\n\u0005U2$aA*fc*\u00111G\u0004\t\u0003qej\u0011AA\u0005\u0003u\t\u0011\u0011\"\u0011;ue&\u0014W\u000f^3\t\u0011q\u0002!\u0011#Q\u0001\n-\n1\"\u0019;ue&\u0014W\u000f^3tA!)a\b\u0001C\u0001\u007f\u00051A(\u001b8jiz\"R\u0001Q!C\u0007\u0012\u0003\"\u0001\u000f\u0001\t\u000bei\u0004\u0019A\u000e\t\u000b\u0005j\u0004\u0019A\u000e\t\u000b\u0015j\u0004\u0019A\u000e\t\u000b%j\u0004\u0019A\u0016\t\u000f\u0019\u0003\u0011\u0011!C\u0001\u000f\u0006!1m\u001c9z)\u0015\u0001\u0005*\u0013&L\u0011\u001dIR\t%AA\u0002mAq!I#\u0011\u0002\u0003\u00071\u0004C\u0004&\u000bB\u0005\t\u0019A\u000e\t\u000f%*\u0005\u0013!a\u0001W!9Q\nAI\u0001\n\u0003q\u0015AD2paf$C-\u001a4bk2$H%M\u000b\u0002\u001f*\u00121\u0004U\u0016\u0002#B\u0011!kV\u0007\u0002'*\u0011A+V\u0001\nk:\u001c\u0007.Z2lK\u0012T!A\u0016\b\u0002\u0015\u0005tgn\u001c;bi&|g.\u0003\u0002Y'\n\tRO\\2iK\u000e\\W\r\u001a,be&\fgnY3\t\u000fi\u0003\u0011\u0013!C\u0001\u001d\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\u0012\u0004b\u0002/\u0001#\u0003%\tAT\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00134\u0011\u001dq\u0006!%A\u0005\u0002}\u000babY8qs\u0012\"WMZ1vYR$C'F\u0001aU\tY\u0003\u000bC\u0004c\u0001\u0005\u0005I\u0011I2\u0002\u001bA\u0014x\u000eZ;diB\u0013XMZ5y+\u0005!\u0007CA3k\u001b\u00051'BA4i\u0003\u0011a\u0017M\\4\u000b\u0003%\fAA[1wC&\u00111N\u001a\u0002\u0007'R\u0014\u0018N\\4\t\u000f5\u0004\u0011\u0011!C\u00015\u0005a\u0001O]8ek\u000e$\u0018I]5us\"9q\u000eAA\u0001\n\u0003\u0001\u0018A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0003cR\u0004\"!\u0004:\n\u0005Mt!aA!os\"9QO\\A\u0001\u0002\u0004Y\u0012a\u0001=%c!9q\u000fAA\u0001\n\u0003B\u0018a\u00049s_\u0012,8\r^%uKJ\fGo\u001c:\u0016\u0003e\u00042A_?r\u001b\u0005Y(B\u0001?\u000f\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0003}n\u0014\u0001\"\u0013;fe\u0006$xN\u001d\u0005\n\u0003\u0003\u0001\u0011\u0011!C\u0001\u0003\u0007\t\u0001bY1o\u000bF,\u0018\r\u001c\u000b\u0005\u0003\u000b\tY\u0001E\u0002\u000e\u0003\u000fI1!!\u0003\u000f\u0005\u001d\u0011un\u001c7fC:Dq!^@\u0002\u0002\u0003\u0007\u0011\u000fC\u0005\u0002\u0010\u0001\t\t\u0011\"\u0011\u0002\u0012\u0005A\u0001.Y:i\u0007>$W\rF\u0001\u001c\u0011%\t)\u0002AA\u0001\n\u0003\n9\"\u0001\u0005u_N#(/\u001b8h)\u0005!\u0007\"CA\u000e\u0001\u0005\u0005I\u0011IA\u000f\u0003\u0019)\u0017/^1mgR!\u0011QAA\u0010\u0011!)\u0018\u0011DA\u0001\u0002\u0004\tx!CA\u0012\u0005\u0005\u0005\t\u0012AA\u0013\u0003\u00151\u0015.\u001a7e!\rA\u0014q\u0005\u0004\t\u0003\t\t\t\u0011#\u0001\u0002*M)\u0011qEA\u0016+AI\u0011QFA\u001a7mY2\u0006Q\u0007\u0003\u0003_Q1!!\r\u000f\u0003\u001d\u0011XO\u001c;j[\u0016LA!!\u000e\u00020\t\t\u0012IY:ue\u0006\u001cGOR;oGRLwN\u001c\u001b\t\u000fy\n9\u0003\"\u0001\u0002:Q\u0011\u0011Q\u0005\u0005\u000b\u0003+\t9#!A\u0005F\u0005]\u0001BCA \u0003O\t\t\u0011\"!\u0002B\u0005)\u0011\r\u001d9msRI\u0001)a\u0011\u0002F\u0005\u001d\u0013\u0011\n\u0005\u00073\u0005u\u0002\u0019A\u000e\t\r\u0005\ni\u00041\u0001\u001c\u0011\u0019)\u0013Q\ba\u00017!1\u0011&!\u0010A\u0002-B!\"!\u0014\u0002(\u0005\u0005I\u0011QA(\u0003\u001d)h.\u00199qYf$B!!\u0015\u0002^A)Q\"a\u0015\u0002X%\u0019\u0011Q\u000b\b\u0003\r=\u0003H/[8o!\u001di\u0011\u0011L\u000e\u001c7-J1!a\u0017\u000f\u0005\u0019!V\u000f\u001d7fi!I\u0011qLA&\u0003\u0003\u0005\r\u0001Q\u0001\u0004q\u0012\u0002\u0004BCA2\u0003O\t\t\u0011\"\u0003\u0002f\u0005Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\t\t9\u0007E\u0002f\u0003SJ1!a\u001bg\u0005\u0019y%M[3di\u0002")
public class Field implements Product, Serializable
{
    private final int flags;
    private final int nameIndex;
    private final int descriptorIndex;
    private final Seq<Attribute> attributes;
    
    public static Option<Tuple4<Object, Object, Object, Seq<Attribute>>> unapply(final Field x$0) {
        return Field$.MODULE$.unapply(x$0);
    }
    
    public static Field apply(final int flags, final int nameIndex, final int descriptorIndex, final Seq<Attribute> attributes) {
        return Field$.MODULE$.apply(flags, nameIndex, descriptorIndex, attributes);
    }
    
    public static Function1<Tuple4<Object, Object, Object, Seq<Attribute>>, Field> tupled() {
        return (Function1<Tuple4<Object, Object, Object, Seq<Attribute>>, Field>)Field$.MODULE$.tupled();
    }
    
    public static Function1<Object, Function1<Object, Function1<Object, Function1<Seq<Attribute>, Field>>>> curried() {
        return (Function1<Object, Function1<Object, Function1<Object, Function1<Seq<Attribute>, Field>>>>)Field$.MODULE$.curried();
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
    
    public Field copy(final int flags, final int nameIndex, final int descriptorIndex, final Seq<Attribute> attributes) {
        return new Field(flags, nameIndex, descriptorIndex, attributes);
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
        return "Field";
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
        return x$1 instanceof Field;
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
            if (x$1 instanceof Field) {
                final Field field = (Field)x$1;
                boolean b = false;
                Label_0113: {
                    Label_0112: {
                        if (this.flags() == field.flags() && this.nameIndex() == field.nameIndex() && this.descriptorIndex() == field.descriptorIndex()) {
                            final Seq<Attribute> attributes = this.attributes();
                            final Seq<Attribute> attributes2 = field.attributes();
                            if (attributes == null) {
                                if (attributes2 != null) {
                                    break Label_0112;
                                }
                            }
                            else if (!attributes.equals(attributes2)) {
                                break Label_0112;
                            }
                            if (field.canEqual(this)) {
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
    
    public Field(final int flags, final int nameIndex, final int descriptorIndex, final Seq<Attribute> attributes) {
        this.flags = flags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
        Product$class.$init$((Product)this);
    }
}
