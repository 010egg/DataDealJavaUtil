// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.Product;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple4;
import scala.Option;
import java.lang.reflect.Field;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005Ee\u0001B\u0001\u0003\u0001&\u0011!\u0003\u0015:pa\u0016\u0014H/\u001f#fg\u000e\u0014\u0018\u000e\u001d;pe*\u00111\u0001B\u0001\be\u00164G.Z2u\u0015\t)a!\u0001\u0004kg>tGg\u001d\u0006\u0002\u000f\u0005\u0019qN]4\u0004\u0001M!\u0001A\u0003\b\u0015!\tYA\"D\u0001\u0003\u0013\ti!A\u0001\u0006EKN\u001c'/\u001b9u_J\u0004\"a\u0004\n\u000e\u0003AQ\u0011!E\u0001\u0006g\u000e\fG.Y\u0005\u0003'A\u0011q\u0001\u0015:pIV\u001cG\u000f\u0005\u0002\u0010+%\u0011a\u0003\u0005\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\t1\u0001\u0011)\u001a!C\u00013\u0005!a.Y7f+\u0005Q\u0002CA\u000e\u001f\u001d\tyA$\u0003\u0002\u001e!\u00051\u0001K]3eK\u001aL!a\b\u0011\u0003\rM#(/\u001b8h\u0015\ti\u0002\u0003\u0003\u0005#\u0001\tE\t\u0015!\u0003\u001b\u0003\u0015q\u0017-\\3!\u0011!!\u0003A!f\u0001\n\u0003I\u0012aC7b]\u001edW\r\u001a(b[\u0016D\u0001B\n\u0001\u0003\u0012\u0003\u0006IAG\u0001\r[\u0006tw\r\\3e\u001d\u0006lW\r\t\u0005\tQ\u0001\u0011)\u001a!C\u0001S\u0005Q!/\u001a;ve:$\u0016\u0010]3\u0016\u0003)\u0002\"aC\u0016\n\u00051\u0012!!C*dC2\fG+\u001f9f\u0011!q\u0003A!E!\u0002\u0013Q\u0013a\u0003:fiV\u0014h\u000eV=qK\u0002B\u0001\u0002\r\u0001\u0003\u0016\u0004%\t!M\u0001\u0006M&,G\u000eZ\u000b\u0002eA\u00111'O\u0007\u0002i)\u00111!\u000e\u0006\u0003m]\nA\u0001\\1oO*\t\u0001(\u0001\u0003kCZ\f\u0017B\u0001\u001e5\u0005\u00151\u0015.\u001a7e\u0011!a\u0004A!E!\u0002\u0013\u0011\u0014A\u00024jK2$\u0007\u0005C\u0003?\u0001\u0011\u0005q(\u0001\u0004=S:LGO\u0010\u000b\u0006\u0001\u0006\u00135\t\u0012\t\u0003\u0017\u0001AQ\u0001G\u001fA\u0002iAQ\u0001J\u001fA\u0002iAQ\u0001K\u001fA\u0002)BQ\u0001M\u001fA\u0002IBQA\u0012\u0001\u0005\u0002\u001d\u000b1a]3u)\rA5\n\u0015\t\u0003\u001f%K!A\u0013\t\u0003\tUs\u0017\u000e\u001e\u0005\u0006\u0019\u0016\u0003\r!T\u0001\te\u0016\u001cW-\u001b<feB\u0011qBT\u0005\u0003\u001fB\u00111!\u00118z\u0011\u0015\tV\t1\u0001N\u0003\u00151\u0018\r\\;f\u0011\u0015\u0019\u0006\u0001\"\u0001U\u0003\r9W\r\u001e\u000b\u0003+f\u0003\"AV,\u000e\u0003UJ!\u0001W\u001b\u0003\r=\u0013'.Z2u\u0011\u0015a%\u000b1\u0001[!\ty1,\u0003\u0002]!\t1\u0011I\\=SK\u001aDqA\u0018\u0001\u0002\u0002\u0013\u0005q,\u0001\u0003d_BLH#\u0002!aC\n\u001c\u0007b\u0002\r^!\u0003\u0005\rA\u0007\u0005\bIu\u0003\n\u00111\u0001\u001b\u0011\u001dAS\f%AA\u0002)Bq\u0001M/\u0011\u0002\u0003\u0007!\u0007C\u0004f\u0001E\u0005I\u0011\u00014\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%cU\tqM\u000b\u0002\u001bQ.\n\u0011\u000e\u0005\u0002k_6\t1N\u0003\u0002m[\u0006IQO\\2iK\u000e\\W\r\u001a\u0006\u0003]B\t!\"\u00198o_R\fG/[8o\u0013\t\u00018NA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016DqA\u001d\u0001\u0012\u0002\u0013\u0005a-\u0001\bd_BLH\u0005Z3gCVdG\u000f\n\u001a\t\u000fQ\u0004\u0011\u0013!C\u0001k\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\u001aT#\u0001<+\u0005)B\u0007b\u0002=\u0001#\u0003%\t!_\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00135+\u0005Q(F\u0001\u001ai\u0011\u001da\b!!A\u0005Bu\fQ\u0002\u001d:pIV\u001cG\u000f\u0015:fM&DX#\u0001@\u0011\u0005Y{\u0018BA\u00106\u0011%\t\u0019\u0001AA\u0001\n\u0003\t)!\u0001\u0007qe>$Wo\u0019;Be&$\u00180\u0006\u0002\u0002\bA\u0019q\"!\u0003\n\u0007\u0005-\u0001CA\u0002J]RD\u0011\"a\u0004\u0001\u0003\u0003%\t!!\u0005\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u0019Q*a\u0005\t\u0015\u0005U\u0011QBA\u0001\u0002\u0004\t9!A\u0002yIEB\u0011\"!\u0007\u0001\u0003\u0003%\t%a\u0007\u0002\u001fA\u0014x\u000eZ;di&#XM]1u_J,\"!!\b\u0011\u000b\u0005}\u0011QE'\u000e\u0005\u0005\u0005\"bAA\u0012!\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\t\u0005\u001d\u0012\u0011\u0005\u0002\t\u0013R,'/\u0019;pe\"I\u00111\u0006\u0001\u0002\u0002\u0013\u0005\u0011QF\u0001\tG\u0006tW)];bYR!\u0011qFA\u001b!\ry\u0011\u0011G\u0005\u0004\u0003g\u0001\"a\u0002\"p_2,\u0017M\u001c\u0005\n\u0003+\tI#!AA\u00025C\u0011\"!\u000f\u0001\u0003\u0003%\t%a\u000f\u0002\u0011!\f7\u000f[\"pI\u0016$\"!a\u0002\t\u0013\u0005}\u0002!!A\u0005B\u0005\u0005\u0013\u0001\u0003;p'R\u0014\u0018N\\4\u0015\u0003yD\u0011\"!\u0012\u0001\u0003\u0003%\t%a\u0012\u0002\r\u0015\fX/\u00197t)\u0011\ty#!\u0013\t\u0013\u0005U\u00111IA\u0001\u0002\u0004iu!CA'\u0005\u0005\u0005\t\u0012AA(\u0003I\u0001&o\u001c9feRLH)Z:de&\u0004Ho\u001c:\u0011\u0007-\t\tF\u0002\u0005\u0002\u0005\u0005\u0005\t\u0012AA*'\u0015\t\t&!\u0016\u0015!%\t9&!\u0018\u001b5)\u0012\u0004)\u0004\u0002\u0002Z)\u0019\u00111\f\t\u0002\u000fI,h\u000e^5nK&!\u0011qLA-\u0005E\t%m\u001d;sC\u000e$h)\u001e8di&|g\u000e\u000e\u0005\b}\u0005EC\u0011AA2)\t\ty\u0005\u0003\u0006\u0002@\u0005E\u0013\u0011!C#\u0003\u0003B!\"!\u001b\u0002R\u0005\u0005I\u0011QA6\u0003\u0015\t\u0007\u000f\u001d7z)%\u0001\u0015QNA8\u0003c\n\u0019\b\u0003\u0004\u0019\u0003O\u0002\rA\u0007\u0005\u0007I\u0005\u001d\u0004\u0019\u0001\u000e\t\r!\n9\u00071\u0001+\u0011\u0019\u0001\u0014q\ra\u0001e!Q\u0011qOA)\u0003\u0003%\t)!\u001f\u0002\u000fUt\u0017\r\u001d9msR!\u00111PAD!\u0015y\u0011QPAA\u0013\r\ty\b\u0005\u0002\u0007\u001fB$\u0018n\u001c8\u0011\u000f=\t\u0019I\u0007\u000e+e%\u0019\u0011Q\u0011\t\u0003\rQ+\b\u000f\\35\u0011%\tI)!\u001e\u0002\u0002\u0003\u0007\u0001)A\u0002yIAB!\"!$\u0002R\u0005\u0005I\u0011BAH\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0003U\u0003")
public class PropertyDescriptor extends Descriptor
{
    private final String name;
    private final String mangledName;
    private final ScalaType returnType;
    private final Field field;
    
    public static Option<Tuple4<String, String, ScalaType, Field>> unapply(final PropertyDescriptor x$0) {
        return PropertyDescriptor$.MODULE$.unapply(x$0);
    }
    
    public static PropertyDescriptor apply(final String name, final String mangledName, final ScalaType returnType, final Field field) {
        return PropertyDescriptor$.MODULE$.apply(name, mangledName, returnType, field);
    }
    
    public static Function1<Tuple4<String, String, ScalaType, Field>, PropertyDescriptor> tupled() {
        return (Function1<Tuple4<String, String, ScalaType, Field>, PropertyDescriptor>)PropertyDescriptor$.MODULE$.tupled();
    }
    
    public static Function1<String, Function1<String, Function1<ScalaType, Function1<Field, PropertyDescriptor>>>> curried() {
        return (Function1<String, Function1<String, Function1<ScalaType, Function1<Field, PropertyDescriptor>>>>)PropertyDescriptor$.MODULE$.curried();
    }
    
    public String name() {
        return this.name;
    }
    
    public String mangledName() {
        return this.mangledName;
    }
    
    public ScalaType returnType() {
        return this.returnType;
    }
    
    public Field field() {
        return this.field;
    }
    
    public void set(final Object receiver, final Object value) {
        this.field().set(receiver, value);
    }
    
    public Object get(final Object receiver) {
        return this.field().get(receiver);
    }
    
    public PropertyDescriptor copy(final String name, final String mangledName, final ScalaType returnType, final Field field) {
        return new PropertyDescriptor(name, mangledName, returnType, field);
    }
    
    public String copy$default$1() {
        return this.name();
    }
    
    public String copy$default$2() {
        return this.mangledName();
    }
    
    public ScalaType copy$default$3() {
        return this.returnType();
    }
    
    public Field copy$default$4() {
        return this.field();
    }
    
    @Override
    public String productPrefix() {
        return "PropertyDescriptor";
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
                o = this.field();
                break;
            }
            case 2: {
                o = this.returnType();
                break;
            }
            case 1: {
                o = this.mangledName();
                break;
            }
            case 0: {
                o = this.name();
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
        return x$1 instanceof PropertyDescriptor;
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
            if (x$1 instanceof PropertyDescriptor) {
                final PropertyDescriptor propertyDescriptor = (PropertyDescriptor)x$1;
                final String name = this.name();
                final String name2 = propertyDescriptor.name();
                boolean b = false;
                Label_0173: {
                    Label_0172: {
                        if (name == null) {
                            if (name2 != null) {
                                break Label_0172;
                            }
                        }
                        else if (!name.equals(name2)) {
                            break Label_0172;
                        }
                        final String mangledName = this.mangledName();
                        final String mangledName2 = propertyDescriptor.mangledName();
                        if (mangledName == null) {
                            if (mangledName2 != null) {
                                break Label_0172;
                            }
                        }
                        else if (!mangledName.equals(mangledName2)) {
                            break Label_0172;
                        }
                        final ScalaType returnType = this.returnType();
                        final ScalaType returnType2 = propertyDescriptor.returnType();
                        if (returnType == null) {
                            if (returnType2 != null) {
                                break Label_0172;
                            }
                        }
                        else if (!returnType.equals(returnType2)) {
                            break Label_0172;
                        }
                        final Field field = this.field();
                        final Field field2 = propertyDescriptor.field();
                        if (field == null) {
                            if (field2 != null) {
                                break Label_0172;
                            }
                        }
                        else if (!field.equals(field2)) {
                            break Label_0172;
                        }
                        if (propertyDescriptor.canEqual(this)) {
                            b = true;
                            break Label_0173;
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
    
    public PropertyDescriptor(final String name, final String mangledName, final ScalaType returnType, final Field field) {
        this.name = name;
        this.mangledName = mangledName;
        this.returnType = returnType;
        this.field = field;
    }
}
