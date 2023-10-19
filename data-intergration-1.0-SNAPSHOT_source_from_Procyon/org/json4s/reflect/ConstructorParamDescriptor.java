// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.runtime.Statics;
import scala.Product;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.runtime.BoxedUnit;
import scala.Function1;
import scala.Tuple5;
import scala.Function0;
import scala.Option;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005Ue\u0001B\u0001\u0003\u0001&\u0011!dQ8ogR\u0014Xo\u0019;peB\u000b'/Y7EKN\u001c'/\u001b9u_JT!a\u0001\u0003\u0002\u000fI,g\r\\3di*\u0011QAB\u0001\u0007UN|g\u000eN:\u000b\u0003\u001d\t1a\u001c:h\u0007\u0001\u0019B\u0001\u0001\u0006\u000f)A\u00111\u0002D\u0007\u0002\u0005%\u0011QB\u0001\u0002\u000b\t\u0016\u001c8M]5qi>\u0014\bCA\b\u0013\u001b\u0005\u0001\"\"A\t\u0002\u000bM\u001c\u0017\r\\1\n\u0005M\u0001\"a\u0002)s_\u0012,8\r\u001e\t\u0003\u001fUI!A\u0006\t\u0003\u0019M+'/[1mSj\f'\r\\3\t\u0011a\u0001!Q3A\u0005\u0002e\tAA\\1nKV\t!\u0004\u0005\u0002\u001c=9\u0011q\u0002H\u0005\u0003;A\ta\u0001\u0015:fI\u00164\u0017BA\u0010!\u0005\u0019\u0019FO]5oO*\u0011Q\u0004\u0005\u0005\tE\u0001\u0011\t\u0012)A\u00055\u0005)a.Y7fA!AA\u0005\u0001BK\u0002\u0013\u0005\u0011$A\u0006nC:<G.\u001a3OC6,\u0007\u0002\u0003\u0014\u0001\u0005#\u0005\u000b\u0011\u0002\u000e\u0002\u00195\fgn\u001a7fI:\u000bW.\u001a\u0011\t\u0011!\u0002!Q3A\u0005\u0002%\n\u0001\"\u0019:h\u0013:$W\r_\u000b\u0002UA\u0011qbK\u0005\u0003YA\u00111!\u00138u\u0011!q\u0003A!E!\u0002\u0013Q\u0013!C1sO&sG-\u001a=!\u0011!\u0001\u0004A!f\u0001\n\u0003\t\u0014aB1sORK\b/Z\u000b\u0002eA\u00111bM\u0005\u0003i\t\u0011\u0011bU2bY\u0006$\u0016\u0010]3\t\u0011Y\u0002!\u0011#Q\u0001\nI\n\u0001\"\u0019:h)f\u0004X\r\t\u0005\tq\u0001\u0011)\u001a!C\u0001s\u0005aA-\u001a4bk2$h+\u00197vKV\t!\bE\u0002\u0010wuJ!\u0001\u0010\t\u0003\r=\u0003H/[8o!\rya\bQ\u0005\u0003\u007fA\u0011\u0011BR;oGRLwN\u001c\u0019\u0011\u0005=\t\u0015B\u0001\"\u0011\u0005\r\te.\u001f\u0005\t\t\u0002\u0011\t\u0012)A\u0005u\u0005iA-\u001a4bk2$h+\u00197vK\u0002BQA\u0012\u0001\u0005\u0002\u001d\u000ba\u0001P5oSRtDC\u0002%J\u0015.cU\n\u0005\u0002\f\u0001!)\u0001$\u0012a\u00015!)A%\u0012a\u00015!)\u0001&\u0012a\u0001U!)\u0001'\u0012a\u0001e!)\u0001(\u0012a\u0001u!Aq\n\u0001EC\u0002\u0013\u0005\u0001+\u0001\u0006jg>\u0003H/[8oC2,\u0012!\u0015\t\u0003\u001fIK!a\u0015\t\u0003\u000f\t{w\u000e\\3b]\"AQ\u000b\u0001E\u0001B\u0003&\u0011+A\u0006jg>\u0003H/[8oC2\u0004\u0003\u0002C,\u0001\u0011\u000b\u0007I\u0011\u0001)\u0002\u0015!\f7\u000fR3gCVdG\u000f\u0003\u0005Z\u0001!\u0005\t\u0015)\u0003R\u0003-A\u0017m\u001d#fM\u0006,H\u000e\u001e\u0011\t\u000fm\u0003\u0011\u0011!C\u00019\u0006!1m\u001c9z)\u0019AULX0aC\"9\u0001D\u0017I\u0001\u0002\u0004Q\u0002b\u0002\u0013[!\u0003\u0005\rA\u0007\u0005\bQi\u0003\n\u00111\u0001+\u0011\u001d\u0001$\f%AA\u0002IBq\u0001\u000f.\u0011\u0002\u0003\u0007!\bC\u0004d\u0001E\u0005I\u0011\u00013\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%cU\tQM\u000b\u0002\u001bM.\nq\r\u0005\u0002i[6\t\u0011N\u0003\u0002kW\u0006IQO\\2iK\u000e\\W\r\u001a\u0006\u0003YB\t!\"\u00198o_R\fG/[8o\u0013\tq\u0017NA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016Dq\u0001\u001d\u0001\u0012\u0002\u0013\u0005A-\u0001\bd_BLH\u0005Z3gCVdG\u000f\n\u001a\t\u000fI\u0004\u0011\u0013!C\u0001g\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\u001aT#\u0001;+\u0005)2\u0007b\u0002<\u0001#\u0003%\ta^\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00135+\u0005A(F\u0001\u001ag\u0011\u001dQ\b!%A\u0005\u0002m\fabY8qs\u0012\"WMZ1vYR$S'F\u0001}U\tQd\rC\u0004\u007f\u0001\u0005\u0005I\u0011I@\u0002\u001bA\u0014x\u000eZ;diB\u0013XMZ5y+\t\t\t\u0001\u0005\u0003\u0002\u0004\u00055QBAA\u0003\u0015\u0011\t9!!\u0003\u0002\t1\fgn\u001a\u0006\u0003\u0003\u0017\tAA[1wC&\u0019q$!\u0002\t\u0011\u0005E\u0001!!A\u0005\u0002%\nA\u0002\u001d:pIV\u001cG/\u0011:jifD\u0011\"!\u0006\u0001\u0003\u0003%\t!a\u0006\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u0019\u0001)!\u0007\t\u0013\u0005m\u00111CA\u0001\u0002\u0004Q\u0013a\u0001=%c!I\u0011q\u0004\u0001\u0002\u0002\u0013\u0005\u0013\u0011E\u0001\u0010aJ|G-^2u\u0013R,'/\u0019;peV\u0011\u00111\u0005\t\u0006\u0003K\tY\u0003Q\u0007\u0003\u0003OQ1!!\u000b\u0011\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0005\u0003[\t9C\u0001\u0005Ji\u0016\u0014\u0018\r^8s\u0011%\t\t\u0004AA\u0001\n\u0003\t\u0019$\u0001\u0005dC:,\u0015/^1m)\r\t\u0016Q\u0007\u0005\n\u00037\ty#!AA\u0002\u0001C\u0011\"!\u000f\u0001\u0003\u0003%\t%a\u000f\u0002\u0011!\f7\u000f[\"pI\u0016$\u0012A\u000b\u0005\n\u0003\u007f\u0001\u0011\u0011!C!\u0003\u0003\n\u0001\u0002^8TiJLgn\u001a\u000b\u0003\u0003\u0003A\u0011\"!\u0012\u0001\u0003\u0003%\t%a\u0012\u0002\r\u0015\fX/\u00197t)\r\t\u0016\u0011\n\u0005\n\u00037\t\u0019%!AA\u0002\u0001;\u0011\"!\u0014\u0003\u0003\u0003E\t!a\u0014\u00025\r{gn\u001d;sk\u000e$xN\u001d)be\u0006lG)Z:de&\u0004Ho\u001c:\u0011\u0007-\t\tF\u0002\u0005\u0002\u0005\u0005\u0005\t\u0012AA*'\u0015\t\t&!\u0016\u0015!)\t9&!\u0018\u001b5)\u0012$\bS\u0007\u0003\u00033R1!a\u0017\u0011\u0003\u001d\u0011XO\u001c;j[\u0016LA!a\u0018\u0002Z\t\t\u0012IY:ue\u0006\u001cGOR;oGRLwN\\\u001b\t\u000f\u0019\u000b\t\u0006\"\u0001\u0002dQ\u0011\u0011q\n\u0005\u000b\u0003\u007f\t\t&!A\u0005F\u0005\u0005\u0003BCA5\u0003#\n\t\u0011\"!\u0002l\u0005)\u0011\r\u001d9msRY\u0001*!\u001c\u0002p\u0005E\u00141OA;\u0011\u0019A\u0012q\ra\u00015!1A%a\u001aA\u0002iAa\u0001KA4\u0001\u0004Q\u0003B\u0002\u0019\u0002h\u0001\u0007!\u0007\u0003\u00049\u0003O\u0002\rA\u000f\u0005\u000b\u0003s\n\t&!A\u0005\u0002\u0006m\u0014aB;oCB\u0004H.\u001f\u000b\u0005\u0003{\n)\t\u0005\u0003\u0010w\u0005}\u0004\u0003C\b\u0002\u0002jQ\"F\r\u001e\n\u0007\u0005\r\u0005C\u0001\u0004UkBdW-\u000e\u0005\n\u0003\u000f\u000b9(!AA\u0002!\u000b1\u0001\u001f\u00131\u0011)\tY)!\u0015\u0002\u0002\u0013%\u0011QR\u0001\fe\u0016\fGMU3t_24X\r\u0006\u0002\u0002\u0010B!\u00111AAI\u0013\u0011\t\u0019*!\u0002\u0003\r=\u0013'.Z2u\u0001")
public class ConstructorParamDescriptor extends Descriptor
{
    private final String name;
    private final String mangledName;
    private final int argIndex;
    private final ScalaType argType;
    private final Option<Function0<Object>> defaultValue;
    private boolean isOptional;
    private boolean hasDefault;
    private volatile byte bitmap$0;
    
    public static Option<Tuple5<String, String, Object, ScalaType, Option<Function0<Object>>>> unapply(final ConstructorParamDescriptor x$0) {
        return ConstructorParamDescriptor$.MODULE$.unapply(x$0);
    }
    
    public static ConstructorParamDescriptor apply(final String name, final String mangledName, final int argIndex, final ScalaType argType, final Option<Function0<Object>> defaultValue) {
        return ConstructorParamDescriptor$.MODULE$.apply(name, mangledName, argIndex, argType, defaultValue);
    }
    
    public static Function1<Tuple5<String, String, Object, ScalaType, Option<Function0<Object>>>, ConstructorParamDescriptor> tupled() {
        return (Function1<Tuple5<String, String, Object, ScalaType, Option<Function0<Object>>>, ConstructorParamDescriptor>)ConstructorParamDescriptor$.MODULE$.tupled();
    }
    
    public static Function1<String, Function1<String, Function1<Object, Function1<ScalaType, Function1<Option<Function0<Object>>, ConstructorParamDescriptor>>>>> curried() {
        return (Function1<String, Function1<String, Function1<Object, Function1<ScalaType, Function1<Option<Function0<Object>>, ConstructorParamDescriptor>>>>>)ConstructorParamDescriptor$.MODULE$.curried();
    }
    
    private boolean isOptional$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x1) == 0) {
                this.isOptional = this.argType().isOption();
                this.bitmap$0 |= 0x1;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.isOptional;
        }
    }
    
    private boolean hasDefault$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x2) == 0) {
                this.hasDefault = this.defaultValue().isDefined();
                this.bitmap$0 |= 0x2;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.hasDefault;
        }
    }
    
    public String name() {
        return this.name;
    }
    
    public String mangledName() {
        return this.mangledName;
    }
    
    public int argIndex() {
        return this.argIndex;
    }
    
    public ScalaType argType() {
        return this.argType;
    }
    
    public Option<Function0<Object>> defaultValue() {
        return this.defaultValue;
    }
    
    public boolean isOptional() {
        return ((byte)(this.bitmap$0 & 0x1) == 0) ? this.isOptional$lzycompute() : this.isOptional;
    }
    
    public boolean hasDefault() {
        return ((byte)(this.bitmap$0 & 0x2) == 0) ? this.hasDefault$lzycompute() : this.hasDefault;
    }
    
    public ConstructorParamDescriptor copy(final String name, final String mangledName, final int argIndex, final ScalaType argType, final Option<Function0<Object>> defaultValue) {
        return new ConstructorParamDescriptor(name, mangledName, argIndex, argType, defaultValue);
    }
    
    public String copy$default$1() {
        return this.name();
    }
    
    public String copy$default$2() {
        return this.mangledName();
    }
    
    public int copy$default$3() {
        return this.argIndex();
    }
    
    public ScalaType copy$default$4() {
        return this.argType();
    }
    
    public Option<Function0<Object>> copy$default$5() {
        return this.defaultValue();
    }
    
    @Override
    public String productPrefix() {
        return "ConstructorParamDescriptor";
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
                o = this.defaultValue();
                break;
            }
            case 3: {
                o = this.argType();
                break;
            }
            case 2: {
                o = BoxesRunTime.boxToInteger(this.argIndex());
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
        return x$1 instanceof ConstructorParamDescriptor;
    }
    
    @Override
    public int hashCode() {
        return Statics.finalizeHash(Statics.mix(Statics.mix(Statics.mix(Statics.mix(Statics.mix(-889275714, Statics.anyHash((Object)this.name())), Statics.anyHash((Object)this.mangledName())), this.argIndex()), Statics.anyHash((Object)this.argType())), Statics.anyHash((Object)this.defaultValue())), 5);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ConstructorParamDescriptor) {
                final ConstructorParamDescriptor constructorParamDescriptor = (ConstructorParamDescriptor)x$1;
                final String name = this.name();
                final String name2 = constructorParamDescriptor.name();
                boolean b = false;
                Label_0185: {
                    Label_0184: {
                        if (name == null) {
                            if (name2 != null) {
                                break Label_0184;
                            }
                        }
                        else if (!name.equals(name2)) {
                            break Label_0184;
                        }
                        final String mangledName = this.mangledName();
                        final String mangledName2 = constructorParamDescriptor.mangledName();
                        if (mangledName == null) {
                            if (mangledName2 != null) {
                                break Label_0184;
                            }
                        }
                        else if (!mangledName.equals(mangledName2)) {
                            break Label_0184;
                        }
                        if (this.argIndex() == constructorParamDescriptor.argIndex()) {
                            final ScalaType argType = this.argType();
                            final ScalaType argType2 = constructorParamDescriptor.argType();
                            if (argType == null) {
                                if (argType2 != null) {
                                    break Label_0184;
                                }
                            }
                            else if (!argType.equals(argType2)) {
                                break Label_0184;
                            }
                            final Option<Function0<Object>> defaultValue = this.defaultValue();
                            final Option<Function0<Object>> defaultValue2 = constructorParamDescriptor.defaultValue();
                            if (defaultValue == null) {
                                if (defaultValue2 != null) {
                                    break Label_0184;
                                }
                            }
                            else if (!defaultValue.equals(defaultValue2)) {
                                break Label_0184;
                            }
                            if (constructorParamDescriptor.canEqual(this)) {
                                b = true;
                                break Label_0185;
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
    
    public ConstructorParamDescriptor(final String name, final String mangledName, final int argIndex, final ScalaType argType, final Option<Function0<Object>> defaultValue) {
        this.name = name;
        this.mangledName = mangledName;
        this.argIndex = argIndex;
        this.argType = argType;
        this.defaultValue = defaultValue;
    }
}
